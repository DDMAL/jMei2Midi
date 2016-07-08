/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.jmei2midi.meielements.layerchild;

import ca.mcgill.music.ddmal.mei.MeiElement;
import javax.sound.midi.Sequence;
import org.ddmal.jmei2midi.meielements.general.MeiMeasure;
import org.ddmal.jmei2midi.meielements.meispecific.MeiSpecificStorage;
import org.ddmal.jmei2midi.meielements.staffinfo.MeiStaff;

/**
 * A factory design pattern to build the appropriate LayerChild object given the
 * specified MeiElement child object.
 *
 * <p>
 * Currently this is used in the MeiSequence class' processElement() function.
 * Before building a LayerChild, the LayerChildEnum class should validate the
 * appropriate MeiElement object. If not validated and the MeiElement is
 * invalid, this class will return null.</p>
 *
 * @author Tristano Tenaglia
 */
public final class LayerChildFactory {

    /**
     * Used to avoid object instantiation as this class should only be called
     * statically.
     */
    private LayerChildFactory() {
        super();
    }

    /**
     * Builder method to choose the appropriate LayerChild object to instantiate
     * based on the given MeiElement child object's name.
     *
     * @param currentStaff the current MeiStaff object being used
     * @param currentMeasure the current MeiMeasure object being used
     * @param sequence the current Sequence being added to
     * @param child the MeiElement whose name will produce the appropriate
     * LayerChild object
     * @return the appropriate LayerChild object based on child name and if the
     * name is not a valid LayerChild sub class, return null
     */
    public static LayerChild buildLayerChild(MeiStaff currentStaff,
                                             MeiMeasure currentMeasure,
                                             Sequence sequence,
                                             MeiElement child,
                                             MeiSpecificStorage nonMidiStorage) {
        LayerChild layerChild;
        switch (child.getName()) {
            case "note":
                layerChild = new MeiNote(currentStaff, currentMeasure, sequence, child, nonMidiStorage);
                break;

            case "rest":
                layerChild = new MeiRest(currentStaff, currentMeasure, sequence, child, nonMidiStorage);
                break;

            case "space":
                layerChild = new MeiRest(currentStaff, currentMeasure, sequence, child, nonMidiStorage);
                break;

            case "mRest":
                layerChild = new MeiMrest(currentStaff, currentMeasure, sequence, child, nonMidiStorage);
                break;

            case "mSpace":
                layerChild = new MeiMrest(currentStaff, currentMeasure, sequence, child, nonMidiStorage);
                break;

            case "chord":
                layerChild = new MeiChord(currentStaff, currentMeasure, sequence, child, nonMidiStorage);
                break;

            case "tuplet":
                layerChild = new MeiTuplet(currentStaff, currentMeasure, sequence, child, nonMidiStorage);
                break;

            default:
                layerChild = null;
        }
        return layerChild;
    }
}
