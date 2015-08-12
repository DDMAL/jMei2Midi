/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.jmei2midi.meielements.staffinfo;

import ca.mcgill.music.ddmal.mei.MeiElement;

import java.util.Map;

import javax.sound.midi.Sequence;

import org.ddmal.jmei2midi.MeiStatTracker;
import org.ddmal.jmei2midi.meielements.general.MeiMdiv;

/**
 * A factory design pattern to build the appropriate MeiStaffBuilder object given the
 * specified MeiElement child object.
 *
 * <p>
 * Currently this is used in the MeiSequence class' processElement() function.
 * Before building an MeiStaffBuilder, the StaffBuilderEnum class should validate the
 * appropriate MeiElement object. If not validated and the MeiElement is
 * invalid, this class will return null.</p>
 * @author Tristano Tenaglia
 */
public final class StaffBuilderFactory {
	
	/**
     * Used to avoid object instantiation as this class should only be called
     * statically.
     */
	private StaffBuilderFactory() {
		super();
	}
	
    /**
     * Builder method to choose the appropriate MeiStaffBuilder object to instantiate
     * based on the given MeiElement child object's name.
     * @param sequence the sequence being used
     * @param stats the stats object being used
     * @param staffs the map of all the current staffs
     * @param works the map of all the current works
     * @param currentMdiv the current movement being processed
     * @param currentStaff the current staff being processed
     * @param element the current mei element being parsed
     * @return the appropriate MeiStaffBuilder object if the
     * 		   mei element corresponds to a subclass
     * 		   of the MeiStaffBuilder object, or else return null
     */
    public static MeiStaffBuilder buildStaffBuilder(Sequence sequence,
                                                    MeiStatTracker stats,
                                                    Map<Integer,MeiStaff> staffs,
                                                    Map<Integer,MeiWork> works,
                                                    MeiMdiv currentMdiv,
                                                    MeiStaff currentStaff,
                                                    MeiElement element) {
        MeiStaffBuilder staffBuilder;
        switch(element.getName()) {
            case "staffDef":
                staffBuilder = new MeiStaffDef(sequence, stats, staffs, works, currentMdiv, currentStaff, element);
                break;
                
            case "scoreDef":
                staffBuilder = new MeiScoreDef(sequence, stats, staffs, works, currentMdiv, currentStaff, element);
                break;
                
            case "work":
                staffBuilder = new MeiWorkInfo(sequence, stats, staffs, works, currentMdiv, currentStaff, element);
                break;
                
            default:
                staffBuilder = null;
        }
        return staffBuilder;
    }
}
