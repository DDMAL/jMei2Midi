/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.jmei2midi.meielements.meispecific;

import ca.mcgill.music.ddmal.mei.MeiElement;

/**
 * An example of a non-midi information class that can contain mei data.
 * This for example is analogous to the grace attribute from an mei note element.
 * 
 * @author Tristano Tenaglia
 */
public class MeiGraceNote extends MeiSpecific {
    
    private final String graceAttribute;
    
    public MeiGraceNote(MeiElement element) {
        super(element);
        this.graceAttribute = element.getAttribute("grace");
    }
    
    public String getGraceAttribute() {
        return graceAttribute;
    }
}
