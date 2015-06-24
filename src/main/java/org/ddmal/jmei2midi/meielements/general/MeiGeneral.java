/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.jmei2midi.meielements.general;

import ca.mcgill.music.ddmal.mei.MeiElement;

/**
 *
 * @author dinamix
 */
public abstract class MeiGeneral {
    
    private MeiElement element;
    
    protected MeiGeneral(MeiElement element) {
        this.element = element;
    }
    
    protected MeiGeneral() {
        //For default insantiations
    }
    
    /**
     * Helper method to standardize if an attribute exists within an element.
     * @param attribute
     * @return true if attribute exists
     */
    protected boolean attributeExists(String attribute) {
        return attribute != null;
    }
}
