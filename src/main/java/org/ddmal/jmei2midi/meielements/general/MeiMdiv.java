/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.jmei2midi.meielements.general;

import ca.mcgill.music.ddmal.mei.MeiElement;

/**
 * MEI Element specific to mdiv elements.
 * Can be used to keep track of current movements.
 * @author Tristano Tenaglia
 */
public final class MeiMdiv extends MeiGeneral {
    private int currentMovement;
    
    /**
     * Default constructor, sets movement = 1.
     */
    public MeiMdiv() {
        this.currentMovement = 1;
    }
    
    /**
     * Constructor used with specified mdiv MeiElement given.
     * @param mdiv mdiv element to get current movement number from.
     */
    public MeiMdiv(MeiElement mdiv) {
        setCurrentMovement(mdiv);
    }
    
    /**
     * Get current movement.
     * @return current movement.
     */
    public int getCurrentMovement() {
        return currentMovement;
    }
    
    /**
     * Sets current movement to given mdiv element attribute n value.
     * @param mdiv Given from n attribute in given element.
     */
    public void setCurrentMovement(MeiElement mdiv) {
        int n = 1;
        String nString = mdiv.getAttribute("n");
        if(attributeExists(nString)) {
            n = Integer.parseInt(nString);
        }
        currentMovement = n;
    }
}
