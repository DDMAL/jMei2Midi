/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.jmei2midi.meielements.layerchild;

import ca.mcgill.music.ddmal.mei.MeiElement;
import javax.sound.midi.Sequence;
import org.ddmal.jmei2midi.meielements.general.MeiMeasure;
import org.ddmal.jmei2midi.meielements.staffinfo.MeiStaff;

/**
 * LayerChild is an abstract class which ensures that all subclasses
 * receive the appropriate variable and necessary functions
 * that need to be computed before midi info is created.
 * @author dinamix
 */
public abstract class LayerChild {
    protected final Sequence sequence;
    protected final MeiStaff currentStaff;
    protected final MeiMeasure currentMeasure;
    protected final MeiElement element;
    
    /**
     * Constructor for layerChild that sets up the currentStaff
     * and currentMeasure.
     * Protected so that it cannot be instantiated alone. 
     * @param currentStaff
     * @param currentMeasure
     * @param sequence
     * @param element
     */
    protected LayerChild(MeiStaff currentStaff, MeiMeasure currentMeasure,
                         Sequence sequence, MeiElement element) {
        this.currentStaff = currentStaff;
        this.currentMeasure = currentMeasure;
        this.sequence = sequence;
        this.element = element;
    }
    
    /**
     * Converts given duration string to a long tick value for midi.
     * thiStaff can be populated with a tuplet or note which will then
     * be used to compute tuplet and dot values in the duration.
     * ASSUMPTION
     * This assumes that if not dur is given in element or chord
     * then the note takes up a full measure.
     * @return long tick value of dur string
     */
    protected abstract long getDurToTick();
    
    /**
     * Fetches the appropriate duration of a given Mei element depending on
     * whether it's a note, a chord or a rest/space.
     * If no duration is found, then a dur = "0" is returned.
     * @return duration of element in string form
     */
    protected abstract String getDurString();
    
    /**
     * Fetches the appropriate dots for the given mei element depending
     * on if it's a chord or a note.
     * If no dots are found, then a dots = 0 value is returned.
     * @return number of corresponding dots for element
     */
    protected abstract int getDots();
    
     /**
     * Helper method to standardize if an attribute exists within an element.
     * @param attribute
     * @return true if attribute exists
     */
    protected boolean attributeExists(String attribute) {
        return attribute != null;
    }
    
    /**
     * Helper method to convert an mei element attribute to an int given
     * a specified default value if the attribute DNE.
     * @param name
     * @param ele
     * @param intDefault
     * @return 
     */
    protected int getAttributeToInt(String name, MeiElement ele, int intDefault) {
        int attInt = intDefault;
        if(ele != null) {
            String attString = ele.getAttribute(name);
            if(attributeExists(attString)) {
                attInt = Integer.parseInt(attString);
            }
        }
        return attInt;
    }
}
