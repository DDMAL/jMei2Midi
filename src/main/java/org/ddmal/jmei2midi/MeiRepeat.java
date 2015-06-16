/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.jmei2midi;

import ca.mcgill.music.ddmal.mei.MeiElement;
import java.util.Stack;

/**
 * This class will build itself given MEI measures and will perform
 * repeats based on stack-string comparisons.
 * All comparisons are made by the MEI element ID.
 * @author dinamix
 */
public class MeiRepeat {
    private Stack<String> startRepeats;
    private String endRepeat;
    private boolean inRepeat;
    
    public MeiRepeat() {
        startRepeats = new Stack<>();
        endRepeat = null;
        inRepeat = false;
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
        }
        else if(repeat.equals("rptend")) {
            endRepeat = measure.getId();
        }
    }
    
    public String getStartRepeat() {
        return startRepeats.peek();
    }
    
    public String removeStartRepeat() {
        return startRepeats.pop();
    }
    
    public boolean hasStartRepeat() {
        return !startRepeats.empty();
    }
    
    public String getEndRepeat() {
        return endRepeat;
    }
    
    public void resetEndRepeat() {
        endRepeat = null;
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
        if(!inRepeat()) {
            checkRepeatInMeasure(measure);
            process = true;
        }
        else if(getEndRepeat() != null && hasStartRepeat()) {
            if(!getStartRepeat().equals(measure.getId())) {
                process = false;
            }
            removeStartRepeat();
            process = true;
        }
        else if(inRepeat && getEndRepeat() == null) {
            process = false;
        }
        else if(inRepeat && getEndRepeat().equals(measure.getId())) {
            resetEndRepeat();
            process = true;
        }
        else {
            process = true;
        }
        return process;
    }
}
