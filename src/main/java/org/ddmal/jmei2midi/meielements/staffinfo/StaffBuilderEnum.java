/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.jmei2midi.meielements.staffinfo;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author dinamix
 */
public enum StaffBuilderEnum {
    staffDef, scoreDef, work;
    
    private static final Set<String> staffBuilderHashNames = new HashSet<>();
   
    static {
        for(StaffBuilderEnum value : EnumSet.allOf(StaffBuilderEnum.class)) {
            staffBuilderHashNames.add(value.name());
        }
    }
    
    public static boolean contains(String name) {
        return staffBuilderHashNames.contains(name);
    }
}
