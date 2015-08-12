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
 * MeiScoreDef will appropriately update works and staffs to account for
 * any changes in the current scoreDef element and will build any appropriate
 * MeiWork and MeiStaff.
 * @author dinamix
 */
public class MeiScoreDef extends MeiStaffBuilder {
    /**
     * The mei scoreDef element to be processed.
     */
    private final MeiElement scoreDef;
    
    /**
     * Constructor will set the appropriate elements of an meiWork objects and then
     * update any meiStaff objects accordingly.
     * @param sequence the sequence object to be added to
     * @param stats the stats object to be modified
     * @param staffs the staffs hashmap to be modified or added to
     * @param works the works hashmap to be modified or added to
     * @param currentMdiv the current movement we are in (mdiv mei element)
     * @param currentStaff the current staff that we are processing
     * @param scoreDef this mei scoredef element
     */
    public MeiScoreDef(Sequence sequence,
                       MeiStatTracker stats,
                       Map<Integer, MeiStaff> staffs, 
                       Map<Integer, MeiWork> works, 
                       MeiMdiv currentMdiv,
                       MeiStaff currentStaff,
                       MeiElement scoreDef) {
        super(sequence, stats, staffs, works, currentMdiv, currentStaff, scoreDef);
        this.scoreDef = scoreDef;
        //Keep default key.sig and then use
        //staff def key.sig on other staffs
        MeiWork work = works.get(currentMdiv.getCurrentMovement());
        String count = scoreDef.getAttribute("meter.count");
        String unit = scoreDef.getAttribute("meter.unit");
        String keysig = scoreDef.getAttribute("key.sig");
        String keymode = scoreDef.getAttribute("key.mode");
        if(attributeExists(count)) {
            work.setMeterCount(count);
        }
        if(attributeExists(unit)) {
            work.setMeterUnit(unit);
        }
        if(attributeExists(keysig)) {
            work.setKeysig(keysig);
        }
        if(attributeExists(keymode)) {
            work.setKeyMode(keymode);
        }
        //For a scoreDef change randomly in the file
        //If staffs not empty and all new attributes are non-null, 
        //then update them.
        //If they are empty, then it will be created in processStaffDef()
        if(!staffs.isEmpty() &&
           (attributeExists(count) || attributeExists(unit) ||
            attributeExists(keysig) || attributeExists(keymode))) {
            updateStaffs(work);
        }
    }
    
    /**
     * New scoreDef found during the piece will update all defined
     * staffs accordingly
     * @param work the mei work element to update all the staffs
     */
    private void updateStaffs(MeiWork work) {
        String count = work.getMeterCount();
        String unit = work.getMeterUnit();
        String tempo = work.getTempo();
        String keysig = work.getKeysig();
        String keymode = work.getKeyMode();
        //Update each staff accordingly
        for(Integer i : staffs.keySet()) {
            //Optimization to check if there was a change
            //If not then don't send another midi message
            if(!MeiStaffMidiLogic.checkCopy(staffs.get(i), count, unit, staffs.get(i).getLabel(), keysig, keymode, tempo)) {
                updateMeiStaff(staffs.get(i), 
                                  count,
                                  unit,
                                  tempo,
                                  null, //label not in MeiWork
                                  keysig,
                                  keymode);
            }
        }
    }
}
