/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.jmei2midi.meielements.general;

import ca.mcgill.music.ddmal.mei.MeiElement;
import java.util.Stack;

/**
 * This class will build itself given MEI measures and will perform
 * repeats based on stack-string comparisons.
 * All comparisons are made by the MEI element ID (generally given by a measure).
 * Currently used by the processEnding(MeiElement) function in MeiSequence class.
 * @author Tristano Tenaglia
 */
public class MeiRepeat extends MeiGeneral {
	
	/**
	 * Start repeats currently in piece.
	 */
    private Stack<String> startRepeats;
    
    /**
     * The most recent start repeat.
     */
    private String startRepeat;
    
    /**
     * The most recent end repeat.
     */
    private String endRepeat;
    
    /**
     * Whether the current function call stack 
     * is currently a repeat or not.
     */
    private boolean inRepeat;
    
    /**
     * Ending element id of the most recent repeat.
     */
    private String ending;
    
    /**
     * Instantiate all object variables.
     */
    public MeiRepeat() {
        startRepeats = new Stack<>();
        startRepeat = null;
        endRepeat = null;
        inRepeat = false;
        ending = null;
    }
    
    /**
     * Checks if there is a repeat given by the right or left
     * attribute within this mei measure element.
     * @param measure measure mei element that needs to be checked.
     */
    public void checkRepeatInMeasure(MeiElement measure) {
        //Assertion for my use right now
        assert measure != null;
        assert measure.getName().equals("measure");
        String right = measure.getAttribute("right");
        String left = measure.getAttribute("left");
        
        //Both if in case we have one measure that is repeated
        if(right != null) {
            processRepeat(right,measure);
        }
        if(left != null) {
            processRepeat(left,measure);
        }
    }
    
    /**
     * Check for whether this is a repeat start or end.
     * @param repeat attribute value of this repeat.
     * @param measure measure mei element whose ID needs to be checked.
     */
    private void processRepeat(String repeat, MeiElement measure) {
        if(repeat.equals("rptstart")) {
            startRepeats.push(measure.getId());
            startRepeat = measure.getId();
        }
        else if(repeat.equals("rptend")) {
            endRepeat = measure.getId();
        }
    }
    
    /**
     * Get most recent start repeat ID for this document.
     * @return most recent start repeat
     */
    public String getStartRepeats() {
        return startRepeats.peek();
    }
    
    /**
     * Remove most recent start repeat for this document.
     * @return remove recent start repeat
     */
    public String removeStartRepeats() {
        return startRepeats.pop();
    }
    
    /**
     * Check if there are any start repeats to be processed.
     * @return true if there are start repeats.
     */
    public boolean hasStartRepeats() {
        return !startRepeats.empty();
    }
    
    /**
     * Get currently processed start repeat ID.
     * @return currently processed start repeat ID.
     */
    public String getStartRepeat() {
        return startRepeat;
    }
    
    /**
     * Check if a repeat is currently being processed.
     * @return true if we are currently processing a repeat.
     */
    public boolean hasStartRepeat() {
        return startRepeat != null;
    }
    
    /**
     * Used when the start repeat has been reached in the
     * DFS for this repeat.
     */
    public void resetStartRepeat() {
        startRepeat = null;
    }
    
    /**
     * Get currently processed end repeat ID.
     * @return currently processed end repeat ID.
     */
    public String getEndRepeat() {
        return endRepeat;
    }
    
    /**
     * Check if an end repeat is currently being processed.
     * @return true if we are processing an end repeat.
     */
    public boolean hasEndRepeat() {
        return endRepeat != null;
    }
    
    /**
     * Used once the end repeat has been reached for the
     * DFS traversal of this repeat.
     */
    public void resetEndRepeat() {
        endRepeat = null;
    }
    
    /**
     * Set ending id given the appropriate ending mei element.
     * @param ending ending mei element to be processed.
     */
    public void setEnding(MeiElement ending) {
        this.ending = ending.getId();
    }
    
    /**
     * USed once the appropriate ending element has been reached
     * in the DFS traversal.
     */
    public void resetEnding() {
        ending = null;
    }
    
    /**
     * Get the ending mei element ID that is currently being processed.
     * @return currently processed ending element ID.
     */
    public String getEnding() {
        return ending;
    }
    
    /**
     * Used to check if we have passed the current ending
     * mei element id or not.
     * @return true if we do not have an ending id.
     */
    public boolean hasEnding() {
        return ending != null;
    }
    
    /**
     * If there is an end repeat then we are currently performing a repeat
     * at the end of an end repeat, this should be reset and the appropriate
     * start repeat should be removed.
     * @return true if we are currently in the DFS traversal of a repeat.
     */
    public boolean inRepeat() {
        return inRepeat;
    }
    
    /**
     * Used to show that we are currently in a repeat.
     */
    public void setInRepeat() {
        inRepeat = true;
    }
    
    /**
     * Used to show that we have completed a repeat.
     */
    public void resetInRepeat() {
        inRepeat = false;
    }
    
    /**
     * Check end id of this measure element to see if
     * we have reached the end of the repeat.
     * @param measure measure mei element to be compared to.
     * @return true if we have reached the appropriate end measure.
     */
    public boolean compareEndID(MeiElement measure) {
        return measure.getId().equals(endRepeat);
    }
    
    /**
     * Check start id if we have reached the start of the repeat.
     * @param measure measure mei element to be compared to.
     * @return true if we have reached the appropriate start measure.
     */
    public boolean compareStartID(MeiElement measure) {
        return measure.getId().equals(getStartRepeat());
    }
    
    /**
     * Check for whether or not this measure needs to be processed
     * during a repeated DFS of the mei document.
     * This is done in the processMeasure() function in MeiSequence class.
     * @param measure measure mei element to be checked for repeat.
     * @return true if we need to process this measure.
     */
    public boolean toProcess(MeiElement measure) {
        boolean process;
        if(!inRepeat) {
            checkRepeatInMeasure(measure);
            process = true;
        }
        else if(inRepeat && !hasEndRepeat()) {
            process = false;
        }
        else if(inRepeat && getEndRepeat().equals(measure.getId())) {
            resetEndRepeat();
            process = true;
        }
        else if(inRepeat && hasStartRepeat() && hasEndRepeat()) {
            if(getStartRepeat().equals(measure.getId())) {
                removeStartRepeats();
                resetStartRepeat();
                process = true;
            }
            else {
                process = false;
            }
        }
        else {
            process = true;
        }
        return process;
    }
}
