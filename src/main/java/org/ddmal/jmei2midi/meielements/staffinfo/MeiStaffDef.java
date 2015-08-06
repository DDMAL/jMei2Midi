/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.jmei2midi.meielements.staffinfo;

import org.ddmal.jmei2midi.meielements.general.MeiMdiv;

import ca.mcgill.music.ddmal.mei.MeiElement;

import java.util.Map;

import javax.sound.midi.Sequence;

import org.ddmal.jmei2midi.MeiStatTracker;

/**
 * MeiStaffDef builds an MeiStaff and changes the staffs and works accordingly.
 * @author Tristano Tenaglia
 */
public class MeiStaffDef extends MeiStaffBuilder {
    
    private MeiElement staffDef;
    
    
    /**
     * Build an appropriate mei staff from staff def attributes
     * and then populate the staffs and works map accordingly.
     * @param sequence the sequence to be updated
     * @param stats the stats to be updated
     * @param staffs the staffs map to be updated
     * @param works the works map to be updated
     * @param currentMdiv the current movement being processed
     * @param currentStaff not used
     * @param staffDef the staffdef mei element being parsed
     */
    //May need to add instrDef but so far this doesn't seem necessary
    //as instrDef can give random midi data for channels (Mozart Quintet)
    public MeiStaffDef(Sequence sequence, 
                       MeiStatTracker stats, 
                       Map<Integer, MeiStaff> staffs, 
                       Map<Integer, MeiWork> works, 
                       MeiMdiv currentMdiv, 
                       MeiStaff currentStaff,
                       MeiElement staffDef) {
        super(sequence, stats, staffs, works, currentMdiv, currentStaff, staffDef);
        this.staffDef = staffDef;
        
        //Start building an mei staff
        MeiStaff thisStaff;       
        
        //Set up necessary attributes
        int n = 1; // n = 1 in case it does not exist to place in hash
        String nString = staffDef.getAttribute("n");
        String count = staffDef.getAttribute("meter.count");
        String unit = staffDef.getAttribute("meter.unit");
        String label = staffDef.getAttribute("label");
        String keysig = staffDef.getAttribute("key.sig");
        String keymode = staffDef.getAttribute("key.mode");
        String tempo = works.get(currentMdiv.getCurrentMovement()).getTempo();
        
        //CASE 1: N ATTRIBUTE DNE
        //Check if n attribute exists
        if(attributeExists(nString)) {
            n = Integer.parseInt(nString);
        }
        
        //CASE 2: N ATTRIBUTE DOES EXIST
        //If staff not created yet, then instantiate it
        //and put it into HashMap<MeiStaff>
        if(!staffs.containsKey(n)) {
            thisStaff = createMeiStaff(n, count, unit,
                                        tempo, label, keysig, keymode);
            staffs.put(n, thisStaff);
        }
        //If staff already created and all values are non-null
        //then check it and make appropriate changes
        //Notes: Tempo not checked because will be checked in scoreDef
        //       Null check done for optimization
        //       If they are all null and already contained then don't do anything
        else {
            thisStaff = staffs.get(n);
            if(!MeiStaffMidiLogic.checkCopy(thisStaff,count,unit,label,keysig,keymode,tempo)) {
                thisStaff = updateMeiStaff(thisStaff, count, unit, 
                                           tempo, label, keysig, keymode);
                staffs.replace(n, thisStaff);
            }
        }
    }
}
