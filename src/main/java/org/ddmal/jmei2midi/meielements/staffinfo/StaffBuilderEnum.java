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
 * List of mei elements that correspond to staff builder objects.
 * Any element that extends MeiStaffBuilder should be added to this list.
 * Enum values are stored within a HashSet at JVM startup.
 * @author Tristano Tenaglia
 */
public enum StaffBuilderEnum {
    /**
     * MEI staffdef element
     */
    staffDef, 
    /**
     * MEI scoredef element
     */
    scoreDef, 
    /**
     * MEI work element
     */
    work;
   
    /**
     * Set where all enum elements are stored for easy lookup.
     */
    private static final Set<String> staffBuilderHashNames = new HashSet<>();
   
    static {
        for(StaffBuilderEnum value : EnumSet.allOf(StaffBuilderEnum.class)) {
            staffBuilderHashNames.add(value.name());
        }
    }
    
    /**
     * Check to see if this enum contains the given name.
     * @param name name of mei element to be checked
     * @return true if the given name is part of the enum
     */
    public static boolean contains(String name) {
        return staffBuilderHashNames.contains(name);
    }
}
