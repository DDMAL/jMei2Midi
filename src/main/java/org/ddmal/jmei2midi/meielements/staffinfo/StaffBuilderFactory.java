/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.jmei2midi.meielements.staffinfo;

import ca.mcgill.music.ddmal.mei.MeiElement;
import java.util.HashMap;
import javax.sound.midi.Sequence;
import org.ddmal.jmei2midi.MeiStatTracker;
import org.ddmal.jmei2midi.meielements.general.MeiMdiv;

/**
 *
 * @author dinamix
 */
public class StaffBuilderFactory {
    public static MeiStaffBuilder buildStaffBuilder(Sequence sequence,
                                                    MeiStatTracker stats,
                                                    HashMap<Integer,MeiStaff> staffs,
                                                    HashMap<Integer,MeiWork> works,
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
