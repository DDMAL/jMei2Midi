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
 * All comparisons are made by the MEI element ID.
 * @author dinamix
 */
public class MeiRepeat extends MeiGeneral {
    private Stack<String> startRepeats;
    private String startRepeat;
    private String endRepeat;
    private boolean inRepeat;
    
    private String ending;
    
    public MeiRepeat() {
        startRepeats = new Stack<>();
        startRepeat = null;
        endRepeat = null;
        inRepeat = false;
        ending = null;
    }
    
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
    
    private void processRepeat(String repeat, MeiElement measure) {
        if(repeat.equals("rptstart")) {
            startRepeats.push(measure.getId());
            startRepeat = measure.getId();
        }
        else if(repeat.equals("rptend")) {
            endRepeat = measure.getId();
        }
    }
    
    public String getStartRepeats() {
        return startRepeats.peek();
    }
    
    public String removeStartRepeats() {
        return startRepeats.pop();
    }
    
    public boolean hasStartRepeats() {
        return !startRepeats.empty();
    }
    
    public String getStartRepeat() {
        return startRepeat;
    }
    
    public boolean hasStartRepeat() {
        return startRepeat != null;
    }
    
    public void resetStartRepeat() {
        startRepeat = null;
    }
    
    public String getEndRepeat() {
        return endRepeat;
    }
    
    public boolean hasEndRepeat() {
        return endRepeat != null;
    }
    
    public void resetEndRepeat() {
        endRepeat = null;
    }
    
    public void setEnding(MeiElement ending) {
        this.ending = ending.getId();
    }
    
    public void resetEnding() {
        ending = null;
    }
    
    public String getEnding() {
        return ending;
    }
    
    public boolean hasEnding() {
        return ending != null;
    }
    
    //If there is an end repeat then we are currently performing a repeat
    //at the end of an end repeat, this should be reset and the appropriate
    //start repeat should be removed.
    public boolean inRepeat() {
        return inRepeat;
    }
    
    public void setInRepeat() {
        inRepeat = true;
    }
    
    public void resetInRepeat() {
        inRepeat = false;
    }
    
    public boolean compareEndID(MeiElement measure) {
        return measure.getId().equals(endRepeat);
    }
    
    public boolean compareStartID(MeiElement measure) {
        return measure.getId().equals(getStartRepeat());
    }
    
    //CHECK FROM START REPEATS
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
