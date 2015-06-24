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
 *
 * @author dinamix
 */
public class LayerChildFactory {
    public static LayerChild buildLayerChild(MeiStaff currentStaff,
                                             MeiMeasure currentMeasure,
                                             Sequence sequence,
                                             MeiElement child) {
        LayerChild layerChild;
        switch(child.getName()) {
            case "note":
                layerChild = new MeiNote(currentStaff, currentMeasure, sequence, child);
                break;
                
            case "rest":
                layerChild = new MeiRest(currentStaff, currentMeasure, sequence, child);
                break;
                
            case "space":
                layerChild = new MeiRest(currentStaff, currentMeasure, sequence, child);
                break;
                
            case "mRest":
                layerChild = new MeiMrest(currentStaff, currentMeasure, sequence, child);
                break;
                
            case "mSpace":
                layerChild = new MeiMrest(currentStaff, currentMeasure, sequence, child);
                break;
                
            case "chord":
                layerChild = new MeiChord(currentStaff, currentMeasure, sequence, child);
                break;
                
            case "tuplet":
                layerChild = new MeiTuplet(currentStaff, currentMeasure, sequence, child);
                break;
                
            default:
                layerChild = null;
        }
        return layerChild;
    }
}
