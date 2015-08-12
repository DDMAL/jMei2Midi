/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.midiUtilities;

import java.io.File;
import java.io.IOException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;

/**
 * This class is a convenience class to take care of input and
 * output for general midi data. Currently it has a write method
 * which will write out a given sequence to a midi file given a file name.
 * @author Tristano Tenaglia
 */
public final class MidiIO {
    /**
     * Writes out a midi file given a midi sequence and file name.
     * This midi file is of file type 1.
     * @param sequence sequence that will be written to file
     * @param filename filename of midi file that will be created
     * @throws IOException 
     */
    public static void write(Sequence sequence, String filename) 
    		throws IOException {
    	MidiSystem.write(sequence, 1, new File(filename));
    }
}
