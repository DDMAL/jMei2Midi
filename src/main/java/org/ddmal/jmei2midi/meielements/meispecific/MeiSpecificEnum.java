/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.jmei2midi.meielements.meispecific;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * The initial idea to check the factory pattern that goes along with MeiSpecific.
 * @author Tristano Tenaglia
 */
public enum MeiSpecificEnum {
    
    /**
     * MEI grace notes
     */
    grace;
    
    private static final Set<String> meiSpecificHashNames = new HashSet<>();
    
    static {
        for(MeiSpecificEnum value : EnumSet.allOf(MeiSpecificEnum.class)) {
            meiSpecificHashNames.add(value.name());
        }
    }
    
    public static boolean contains(String name) {
        return meiSpecificHashNames.contains(name);
    }
}
