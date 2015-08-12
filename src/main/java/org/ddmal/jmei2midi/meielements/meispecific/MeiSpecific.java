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
public abstract class MeiSpecific {
    
    private final MeiElement element;
    
    protected MeiSpecific(MeiElement element) {
        this.element = element;
    }
}
