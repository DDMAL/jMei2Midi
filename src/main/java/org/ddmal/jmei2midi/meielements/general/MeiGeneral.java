/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.jmei2midi.meielements.general;

import ca.mcgill.music.ddmal.mei.MeiElement;

/**
 * Interface to incorporate general MEI elements.
 * These are the elements which effect general aspects of MEI parsing
 * and also those which directly affect the recursive XML stack.
 * @author dinamix
 */
public abstract class MeiGeneral {
    
    private MeiElement element;
    
    /**
     * This is a constructor which corresponds to the specified given element.
     * @param element Element to be used.
     */
    protected MeiGeneral(MeiElement element) {
        this.element = element;
    }
    
    /**
     * Default constructor.
     */
    protected MeiGeneral() {
        //For default insantiations
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
