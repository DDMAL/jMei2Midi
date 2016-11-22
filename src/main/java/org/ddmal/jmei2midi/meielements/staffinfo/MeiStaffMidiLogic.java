/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.jmei2midi.meielements.staffinfo;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;
import org.ddmal.midiUtilities.MidiBuildEvent;

/**
 * A final static class which contains some convenience methods for
 * building midi data and checking for copies of data.
 * @author Tristano Tenaglia
 */
public final class MeiStaffMidiLogic {
    
    /**
     * Helper method to standardize if an attribute exists within an element.
     * @param attribute attribute to be checked 
     * @return true if attribute exists
     */
    public static boolean attributeExists(String attribute) {
        return attribute != null;
    }
    
    /**
     * Optimization method to check if a current staff and a new
     * mei staff element are the same or if the new element
     * has only unnecessary information.
     * If they are the same they then return true or else it returns false.
     * @param thisStaff staff to be checked
     * @param count meter count to be checked
     * @param unit meter unit to be checked
     * @param label label/instrument to be checked
     * @param keysig key signature to be checked
     * @param keymode key mode to be checked
     * @param tempo tempo to be checked
     * @return true if all above values are either equal to thisStaff's
     * 		   similar elements or null, otherwise return false
     */
    public static boolean checkCopy(MeiStaff thisStaff,
                              String count,
                              String unit,
                              String label,
                              String keysig,
                              String keymode,
                              String tempo) {
        return (thisStaff.getMeterCount().equals(count) || count == null) &&
                (thisStaff.getMeterUnit().equals(unit) || unit == null) &&
                (thisStaff.getLabel().equals(label) || label == null) &&
                (thisStaff.getKeysig().equals(keysig) || keysig == null) &&
                (thisStaff.getKeymode().equals(keymode) || keymode == null) &&
                (thisStaff.getTempo().equals(tempo) || tempo == null);
    }
    

     /**
      * Build a midi track from staff to add to sequence.
     * @param staff staff to be processed
     * @param sequence sequence to be added to from staff data
     */
    public static void buildMidiTrack(MeiStaff staff,
                                 Sequence sequence) {
        Track newTrack;
        //The info that will be changed
        int thisN = staff.getN();
        int thisChannel = staff.getChannel();
        long thisTick = staff.getTick();
        String thisKey = staff.getKeysig();
        String thisQuality = staff.getKeymode();
        int thisBpm = staff.getBpm();
        int thisProgram = staff.getMidiLabel();
        String meterCount = staff.getMeterCount();
        String meterUnit = staff.getMeterUnit();
        
        //If tracks does not have this n track
        //Then create a new track for the sequence
        if(sequence.getTracks().length < thisN) {
            newTrack = sequence.createTrack();
        }
        else {
            newTrack = sequence.getTracks()[thisN-1];
        }
        addEventsToTrack(newTrack, 
                         thisChannel, 
                         thisTick, 
                         thisKey, 
                         thisQuality, 
                         thisProgram, 
                         thisBpm,
                         meterCount,
                         meterUnit);
    }
     
     /**
     * Sends keysig, program change and tempo midi events to the specified track.
     * This could be optimized to check previous events and not send
     * if they are the same but they may be tricky.
     * @param track track to be added to
     * @param channel chanell to be added to
     * @param tick tick of the message being added
     * @param key key signature to be added
     * @param quality key quality to be added
     * @param midiLabel label for program change to be added
     * @param bpm bpm for track tempo to be added
     */
    public static void addEventsToTrack(Track track,
                                  int channel,
                                  long tick,
                                  String key,
                                  String quality,
                                  int midiLabel,
                                  int bpm,
                                   String meterCount,
                                   String meterUnit) {
        try {
            track.add(MidiBuildEvent.createKeySignature(key, quality, tick));
            track.add(MidiBuildEvent.createProgramChange(midiLabel, tick, channel));
            track.add(MidiBuildEvent.createTrackTempo(bpm, tick));
            track.add(MidiBuildEvent.createTimeSignature(meterCount, meterUnit, tick));
        }
        catch(InvalidMidiDataException imde) {
            imde.printStackTrace();
            System.exit(1);
        }
    }
}
