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
 * LayerChild is the parent class of all children of the mei
 * layer element. Any future elements that are children of layer
 * should extend LayerChild.
 * 
 * <p>This ensures that all subclasses
 * receive the appropriate variable and necessary functions
 * that need to be computed before midi info is created.
 * All sub classes can also make use of the sequence,
 * currentStaff, currentMeasure and element.</p>
 * @author Tristano Tenaglia
 */
public abstract class LayerChild {
    protected final Sequence sequence;
    protected final MeiStaff currentStaff;
    protected final MeiMeasure currentMeasure;
    protected final MeiElement element;
    
    /**
     * Constructor for layerChild that sets up the currentStaff
     * and currentMeasure. 
     * @param currentStaff The current MeiStaff object being used.
     * @param currentMeasure The current MeiMeasure object being used.
     * @param sequence The Java MIDI Sequence Object which is being added to.
     * @param element The current MeiEelement that is being processed.
     */
    public LayerChild(MeiStaff currentStaff, MeiMeasure currentMeasure,
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
     * then the note has a duration of "0".
     * @return long tick value of dur string
     */
    public abstract long getDurToTick();
    
    /**
     * Fetches the appropriate duration of a given Mei element depending on
     * whether it's a note, a chord or a rest/space.
     * If no duration is found, then a dur = "0" is returned.
     * @return duration of element in string form
     */
    public abstract String getDurString();
    
    /**
     * Fetches the appropriate dots for the given mei element depending
     * on if it's a chord or a note.
     * If no dots are found, then a dots = 0 value is returned.
     * @return number of corresponding dots for element
     */
    public abstract int getDots();
    
     /**
     * Helper method to standardize if an attribute exists within an element.
     * @param attribute any mei element attribute
     * @return true if attribute exists
     */
    public boolean attributeExists(String attribute) {
        return attribute != null;
    }
    
    /**
     * Helper method to convert an mei element attribute to an int given
     * a specified default value if the attribute DNE.
     * @param name The name of the attribute to get from ele.
     * @param ele The MeiElement object to get the attribute from.
     * @param intDefault The value to give to the attribute if it is not found.
     * @return the attribute value of the name attribute of ele if it exists,
     * 		   or else the intDefault.
     */
    public int getAttributeToInt(String name, MeiElement ele, int intDefault) {
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
