/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.jmei2midi.meielements.meispecific;

import ca.mcgill.music.ddmal.mei.MeiElement;

/**
 * The factory pattern for MeiSpecific. This is just an example and the input
 * parameters should be thought through and then updated appropriately,
 * depending on what is required from the non-midi mei data.
 * 
 * @author Tristano Tenaglia
 */
public class MeiSpecificFactory {
    public static MeiSpecific build(String attributeName,
                                    MeiElement element) {
        MeiSpecific meiSpecific;
        switch(attributeName) {
            case "grace" :
                meiSpecific = new MeiGraceNote(element);
                break;
                
            default :
                meiSpecific = null;
        }
        return meiSpecific;
    }
}
