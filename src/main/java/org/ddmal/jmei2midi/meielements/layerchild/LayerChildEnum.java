/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.jmei2midi.meielements.layerchild;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * This enum contains all mei elements that are children of the mei element
 * layer, which currently subclass the LayerChild class. All elements that
 * subclass the LayerChild class should be added to this enum. Enum values
 * are stored within a HashSet at JVM startup.
 *
 * @author Tristano Tenaglia
 */
public enum LayerChildEnum {

    /**
     * MEI note element
     */
    note,
    /**
     * MEI rest element
     */
    rest,
    /**
     * MEI mRest element (i.e. a full measure rest)
     */
    mRest,
    /**
     * MEI space element
     */
    space,
    /**
     * MEI mSpace element (i.e. a full measure space)
     */
    mSpace,
    /**
     * MEI chord element
     */
    chord,
    /**
     * MEI tuplet element
     */
    tuplet;

    /**
     * Set where all enum elements are stored for easy lookup.
     */
    private static final Set<String> layerChildHashNames = new HashSet<>();

    static {
        for (LayerChildEnum value : EnumSet.allOf(LayerChildEnum.class)) {
            layerChildHashNames.add(value.name());
        }
    }

    /**
     * Check to see if this enum contains the given name.
     *
     * @param name Name of mei element to be checked
     * @return true if the given name is part of this enum
     */
    public static boolean contains(String name) {
        return layerChildHashNames.contains(name);
    }

}
