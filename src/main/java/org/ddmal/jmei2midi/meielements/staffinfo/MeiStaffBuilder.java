/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.jmei2midi.meielements.staffinfo;

import org.ddmal.jmei2midi.meielements.general.MeiData;
import org.ddmal.jmei2midi.meielements.general.MeiMdiv;

import ca.mcgill.music.ddmal.mei.MeiElement;

import java.util.HashMap;
import java.util.Map;

import javax.sound.midi.Sequence;

import org.ddmal.jmei2midi.MeiStatTracker;
import org.ddmal.midiUtilities.ConvertToMidi;
import org.ddmal.midiUtilities.ConvertToMidiWithStats;

/**
 * MeiStaffBuilder is an abstract class that is
 * used to regulate classes that need to build or modify
 * general midi staff information, specifically MeiWork and MeiStaff.
 * All classes the take part in building staff elements should extend this class.
 * @author Tristano Tenaglia
 */
public abstract class MeiStaffBuilder {
    
    /**
     * The sequence to be added to
     */
    protected Sequence sequence;
    
    /**
     * The stats object to be added to
     */
    protected MeiStatTracker stats;
    
    /**
     * The staffs currently in this mei document
     */
    protected Map<Integer, MeiStaff> staffs;
    
    /**
     * The works currently in this mei document
     */
    protected Map<Integer, MeiWork> works;
    
    /**
     * The current movement that is being processed
     */
    protected MeiMdiv currentMdiv;
    
    /**
     * The current staff that is being processed
     */
    protected MeiStaff currentStaff;
    
    /**
     * The current mei element that is being processed
     */
    protected MeiElement element;

    protected MeiData meiData;
    
    /**
     * The abstract method that needs to be called by all
     * subclasses to ensure that all appropriate information
     * is passed down.
     * @param sequence The sequence to be added to
     * @param stats The stats object to be added to
     * @param staffs The staffs currently in this mei document
     * @param works The works currently in this mei document
     * @param currentMdiv The current movement that is being processed
     * @param currentStaff The current staff that is being processed
     * @param element The current mei element that is being processed
     */
    protected MeiStaffBuilder(Sequence sequence,
                           MeiStatTracker stats,
                           Map<Integer,MeiStaff> staffs,
                           Map<Integer,MeiWork> works,
                           MeiMdiv currentMdiv,
                           MeiStaff currentStaff,
                           MeiElement element,
                              MeiData meiData) {
        this.sequence = sequence;
        this.stats = stats;
        this.staffs = staffs;
        this.works = works;
        this.currentMdiv = currentMdiv;
        this.currentStaff = currentStaff;
        this.element = element;
        this.meiData = meiData;
    }
    
    /**
     * Helper method to standardize if an attribute exists within an element.
     * @param attribute mei attribute to be checked
     * @return true if attribute exists
     */
    protected boolean attributeExists(String attribute) {
        return attribute != null;
    }
    
    /**
     * NOTE : STATS ARE USED IN HERE,
     * MeiStaff object populated with new values in new staffDef.
     * When a staff already exists but a new staffDef is found with the
     * same n attribute then the given attributes are changed accordingly.
     * Since MeiStaff has already been created,
     * we know that thisStaff will already either have
     * old attributes or default attributes.
     * @param thisStaff the staff to be updated
     * @param count the meter count to update thisStaff
     * @param unit the meter unit to update thisStaff
     * @param label the label to update thisStaff
     * @param keysig the key signature to update thisStaff
     * @param keymode the key mode to update thisStaff
     * @return newly populated MeiStaff object with al the above values
     */
    protected MeiStaff updateMeiStaff(MeiStaff thisStaff,
                                      String count,                                     
                                      String unit,
                                      String tempo,
                                      String label,
                                      String keysig,
                                      String keymode) {
        //Stats checking
        ConvertToMidiWithStats.tempoToBpm(tempo, stats);
        if(attributeExists(tempo)) {
            thisStaff.setTempo(tempo);
        }
        if(attributeExists(count)) {
            thisStaff.setMeterCount(count);
        }
        if(attributeExists(unit)) {
            thisStaff.setMeterUnit(unit);
        }
        //Check if label exists and if it is a valid instrument
        int midiInstr = ConvertToMidiWithStats.instrToMidi(label,stats);
        int percInstr = (midiInstr > -1) ? 0 : ConvertToMidiWithStats.instrToPerc(label,stats);
        if(midiInstr > -1) {
            thisStaff.setLabelInstr(label,midiInstr);
        }
        else if(percInstr > -1) {
            thisStaff.setLabelPerc(label, percInstr);
        }
        else {
            thisStaff.setLabel(processNewLabel(thisStaff.getN(),label));
        }
        if(attributeExists(keysig)) {
            thisStaff.setKeysig(keysig);
        }
        if(attributeExists(keymode)) {
            thisStaff.setKeymode(keymode);
        }
        MeiStaffMidiLogic.buildMidiTrack(thisStaff,this.sequence);
        return thisStaff;
    }
    
    /**
     * NOTE STATS ARE USED IN HERE
     * MeiStaff object created when n attribute is not given.
     * This should not happen but maybe with only 1 staff
     * the composer may assume that there is no n attribute.
     * @param n mei n attribute to uniquely identify this staff
     * @param count meter count given to this staff
     * @param unit meter unit given to this staff
     * @param label label given to this staff
     * @param keysig key signature given to this staff
     * @param keymode key mode given to this staff
     * @return new staff element with filled in above values
     */
    protected MeiStaff createMeiStaff(int n,
                                    String count,
                                    String unit,
                                    String tempo,                                    
                                    String label,
                                    String keysig,
                                    String keymode) {
        MeiWork work = works.get(currentMdiv.getCurrentMovement());
        MeiStaff newStaff = new MeiStaff(n);
        
        ConvertToMidiWithStats.tempoToBpm(tempo, stats); //check invalid
        if(attributeExists(tempo)) {
            newStaff.setTempo(tempo);
        }
        if(attributeExists(count)) {
            newStaff.setMeterCount(count);
        }
        else {
            newStaff.setMeterCount(work.getMeterCount());
        }
        if(attributeExists(unit)) {
            newStaff.setMeterUnit(unit);
        }
        else {
            newStaff.setMeterUnit(work.getMeterUnit());
        }
        //Check if label exists and if it is a valid instrument
        int midiInstr = ConvertToMidiWithStats.instrToMidi(label,stats);
        int percInstr = (midiInstr > -1) ? 0 : ConvertToMidiWithStats.instrToPerc(label,stats);
        if(midiInstr > -1) {
            newStaff.setLabelInstr(label,midiInstr);
        }
        else if(percInstr > -1) {
            newStaff.setLabelPerc(label, percInstr);
        }
        //or else we use works defaults
        else {
            newStaff.setLabel(processNewLabel(n,label));
        }
        if(attributeExists(keysig)) {
            newStaff.setKeysig(keysig);
        }
        else {
            newStaff.setKeysig(work.getKeysig());
        }
        if(attributeExists(keymode)) {
            newStaff.setKeymode(keymode);
        }
        else if(attributeExists(work.getKeyMode())) {
            newStaff.setKeymode(work.getKeyMode());
        }
        MeiStaffMidiLogic.buildMidiTrack(newStaff,sequence);
        return newStaff;
    }
    
    /**
     * WARNING
     * ORDER OF IF STATEMENTS DOES MATTER FOR CASE OF MULTI-STAFF INSTRUMENTS,
     * Will check if piano first or else will take from works,
     * If nothing in works but already has a value != "default
     * then it will take the new value,
     * Or else it will return Voice or if there are 2 staffs, Piano.
     * @param n n value of staff to be given a new label
     * @param label new mei label to be given
     * @return the appropriate instrument label given to the staff
     */
    protected String processNewLabel(int n,String label) {
        //Check if there is a work
        MeiWork work = works.get(currentMdiv.getCurrentMovement());
        if(work != null) {
            HashMap<Integer,String> instrVoice = work.getInstrVoice();
            //If there is no label and the previous label is a multi-staff
            //instrument then copy the previous instrument
            if((label == null) &&
                staffs.containsKey(n-1) &&
                (staffs.get(n-1).getLabel().toLowerCase().contains("piano") ||
                staffs.get(n-1).getLabel().toLowerCase().contains("organ") ||
                staffs.get(n-1).getLabel().toLowerCase().contains("harpsi"))) {
                for(int i = n-1; i >= 0; i--) {
                    if(instrVoice.containsKey(i)) {
                        return instrVoice.get(i);
                    }
                }
            }
            //Check if there is a valid instrVoice
            else if(instrVoice.containsKey(n)) {
                String instr = instrVoice.get(n);
                boolean instrValid = ConvertToMidi.instrToMidi(instr) > -1;
                if(instrValid) {
                    return instr;
                }
            }
            //Or if we previously had an instrument here
            else if(staffs.containsKey(n) &&
                    !staffs.get(n).getLabel().equals("default")) {
                return staffs.get(n).getLabel();
            } 
        }   
        //This is if nothing is found
        //Default is voice
        //Piano is used if we have only 2 staffs
        String instrument = "Voice";
        if(staffs.size() == 2) {
            instrument = "Piano";
        }
        return instrument;
    }
}
