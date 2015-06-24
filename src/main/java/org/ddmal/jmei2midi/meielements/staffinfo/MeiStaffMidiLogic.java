/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.jmei2midi.meielements.staffinfo;

import java.util.HashMap;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;
import org.ddmal.midiUtilities.ConvertToMidi;
import org.ddmal.midiUtilities.MidiBuildMessage;

/**
 *
 * @author dinamix
 */
public class MeiStaffMidiLogic {
    
    /**
     * Helper method to standardize if an attribute exists within an element.
     * @param attribute
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
     * @param thisStaff
     * @param count
     * @param unit
     * @param label
     * @param keysig
     * @param keymode
     * @param tempo
     * @return 
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
                         thisBpm);
    }
     
     /**
     * Sends appropriate midi events to the specified track.
     * This could be optimized to check previous events and not send
     * if they are the same but they may be tricky.
     * @param track
     * @param channel
     * @param tick
     * @param key
     * @param quality
     * @param midiLabel
     * @param bpm 
     */
    public static void addEventsToTrack(Track track,
                                  int channel,
                                  long tick,
                                  String key,
                                  String quality,
                                  int midiLabel,
                                  int bpm) {
        try {
            track.add(MidiBuildMessage.createKeySignature(key, quality, tick));
            track.add(MidiBuildMessage.createProgramChange(midiLabel, tick, channel));
            track.add(MidiBuildMessage.createTrackTempo(bpm, tick));
        }
        catch(InvalidMidiDataException imde) {
            imde.printStackTrace();
            System.exit(1);
        }
    }
}
