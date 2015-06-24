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
public final class MeiMdiv extends MeiGeneral {
    private int currentMovement;
    
    public MeiMdiv() {
        this.currentMovement = 1;
    }
    
    public MeiMdiv(MeiElement mdiv) {
        setCurrentMovement(mdiv);
    }
    
    public int getCurrentMovement() {
        return currentMovement;
    }
    
    public void setCurrentMovement(MeiElement mdiv) {
        int n = 1;
        String nString = mdiv.getAttribute("n");
        if(attributeExists(nString)) {
            n = Integer.parseInt(nString);
        }
        currentMovement = n;
    }
}
