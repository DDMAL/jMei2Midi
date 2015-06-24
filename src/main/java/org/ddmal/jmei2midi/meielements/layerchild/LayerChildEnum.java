/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.jmei2midi.meielements.layerchild;

import java.util.EnumSet;
import java.util.HashSet;

/**
 *
 * @author dinamix
 */
public enum LayerChildEnum {
    
    note, rest, mRest, space, mSpace, chord, tuplet;
    
    private static final HashSet<String> layerChildHashNames = new HashSet<>();
    
    static {
        for(LayerChildEnum value : EnumSet.allOf(LayerChildEnum.class)) {
            layerChildHashNames.add(value.name());
        }
    }
    
    public static boolean contains(String name) {
        return layerChildHashNames.contains(name);
    }
    
}
