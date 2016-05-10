/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.jmei2midi.meielements.meispecific;

import ca.mcgill.music.ddmal.mei.MeiElement;

/**
 * This abstract class represents the idea for a higher-order hierarchy
 * of all non-midi mei data. IT IS HIGHLY RECOMMENDED THAT THIS INFORMATION
 * BE ABSTRACTED SOMEHOW EITHER OUTSIDE OF JMEI2MIDI OR FROM ITS OWN SEPARATE
 * PARSER. CURRENTLY THIS IS FOR DEVELOPMENT PURPOSES ONLY.
 * 
 * @author Tristano Tenaglia
 */
public class MeiSpecific {
    
    private final MeiElement element;
    protected String type;
    protected String value;
    protected String locationInScore;

    public MeiSpecific(MeiElement element, String key, String value, String locationInScore) {
        this.element = element;
        this.type = key;
        this.value = value;
        this.locationInScore = locationInScore;
    }

    protected MeiSpecific(MeiElement element) {
        this(element,null,null,null);
    }

    public MeiElement getElement() {
        return element;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public String getLocationInScore() {
        return locationInScore;
    }

    /**
     * Helper method to standardize if an attribute exists within an element.
     * @param attribute Given attribute from some MEI element.
     * @return true if attribute exists
     */
    protected boolean attributeExists(String attribute) {
        return attribute != null;
    }

}