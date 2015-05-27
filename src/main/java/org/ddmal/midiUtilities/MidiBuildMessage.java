/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.midiUtilities;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.ShortMessage;

/**
 *
 * @author dinamix
 */
public class MidiBuildMessage {
    
    private static final int VEL = 64; //VELOCITY
    
    /**
     * Creates a MIDI meta message for key signature and key mode
     * given the MEI key.sig = keysig and key.mode = quality.
     * @param keysig
     * @param quality
     * @param lTick
     * @return 
     */
    public static MidiEvent createKeySignature(String keysig,
                                               String quality,
                                               int lTick) {
        byte[] bytearray = keysigToByteArray(keysig,quality);
        MetaMessage setKeysig = new MetaMessage();
        try {
            setKeysig.setMessage(0x59, bytearray, bytearray.length);
        }
        catch(InvalidMidiDataException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        return new MidiEvent(setKeysig, lTick);
    }
    
    /**
     * Converts MEI key.sig and key.mode to a byte array for MIDI
     * key signature meta message.
     * @param keysig
     * @param quality
     * @return byte[] MIDI key signature data bytes
     */
    public static byte[] keysigToByteArray(String keysig,
                                           String quality) {
        byte bQuality = (byte)0x00;
        if(quality.toLowerCase().equals("minor")) {
            bQuality = (byte)0x01;
        }
        int numOfAccidental = Integer.parseInt(keysig.substring(0,1));
        if(numOfAccidental == 0) {
            return new byte[]{(byte)0x00,bQuality};
        }
        if(keysig.substring(1, 2).toLowerCase().equals("f")) {
            numOfAccidental = 0 - numOfAccidental;
        }
        byte key = (byte)numOfAccidental;
        return new byte[]{key,bQuality};
    }
    
    /**
     * Creates a Program Change MidiEvent given an event, a tick and a channel.
     * @param pEvent
     * @param lTick
     * @param nChannel
     * @return 
     */
    public static MidiEvent createProgramChange(int pEvent,
                                                int lTick,
                                                int nChannel) {
        ShortMessage programChange = new ShortMessage();
        try {
            programChange.setMessage(ShortMessage.PROGRAM_CHANGE, 
                                     nChannel, 
                                     pEvent, 
                                     0);
        }
        catch(InvalidMidiDataException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        return new MidiEvent(programChange, lTick);
    }

    /**
     * Input bpm (beats per minute) and starting tick and returns a MidiEvent
     * MetaMessage of the given bpm tempo.
     * This should be placed at the start of a track or on a tempo change.
     * @param bpm
     * @param lTick
     * @return
     */
    public static MidiEvent createTrackTempo(int bpm, int lTick) {
        byte[] bytearray = bpmToByteArray(bpm);
        MetaMessage setTempo = new MetaMessage();
        try {
            setTempo.setMessage(0x51, bytearray, bytearray.length);
        } catch (InvalidMidiDataException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        return new MidiEvent(setTempo, lTick);
    }

    /**
     * Convert beats per minute (bpm) to a byte array based on
     * http://www.recordingblogs.com/sa/tabid/88/Default.aspx?topic=MIDI+Set+Tempo+meta+message.
     * Will return new byte[]{(byte) 0x00} if bpm = 0.
     * Will pad on zeros if hex string conversion has odd length.
     * @param bpm beats per minute
     * @return 
     */
    public static byte[] bpmToByteArray(int bpm) {
        //DEFAULT TO 80 or 90 bpm if negative or 0 given
        //NEED TO ASK MUSICOLOGIST ABOUT AVERAGE
        if(bpm <= 0) {
            bpm = 90;
            //return new byte[]{(byte) 0x00};
        }
        //Gets bpm into ms per quarter note
        int mspermin = 60000000;
        String hex = Integer.toHexString(mspermin / bpm);
        if (hex.length() % 2 != 0) {
            hex = "0" + hex; //pad on zero if odd number of hex
        }
        //Create an appropriate byte array
        int length = hex.length();
        byte[] bytes = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            bytes[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                                  + Character.digit(hex.charAt(i + 1), 16));
        }
        return bytes;
    }

    /**
     * Creates a NOTE_ON MidiEvent given a pitch, tick and channel.
     * @param nPitch
     * @param lTick
     * @param nChannel
     * @return 
     */
    public static MidiEvent createNoteOnEvent(int nPitch,
                                               long lTick,
                                               int nChannel) {
        return createNoteEvent(ShortMessage.NOTE_ON,
                               nPitch,
                               VEL,
                               lTick,
                               nChannel);
    }

    /**
     * Creates a NOTE_OFF MidiEvent given a pitch, tick and channel.
     * @param nPitch
     * @param lTick
     * @param nChannel
     * @return 
     */   
    public static MidiEvent createNoteOffEvent(int nPitch,
                                                long lTick,
                                                int nChannel) {
        return createNoteEvent(ShortMessage.NOTE_OFF,
                               nPitch,
                               0,
                               lTick,
                               nChannel);
    }
    
    /**
     * Creates general Note Event MidiEvent given parameters.
     * All invalid midi data exceptions are caught here and stack trace is printed.
     * @param nCommand
     * @param nPitch
     * @param nVelocity
     * @param lTick
     * @param nChannel
     * @return 
     */
    private static MidiEvent createNoteEvent(int nCommand,
                                             int nPitch,
                                             int nVelocity,
                                             long lTick,
                                             int nChannel) {
        ShortMessage message = new ShortMessage();
        try {
            message.setMessage(nCommand, nChannel, nPitch, nVelocity);
        } catch (InvalidMidiDataException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        return new MidiEvent(message, lTick);
    }
}
