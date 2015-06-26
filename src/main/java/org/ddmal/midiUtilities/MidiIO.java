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
 *
 * @author dinamix
 */
public class MidiIO {
    //Might be good to delete file if rebuilt... or not
    public static void write(Sequence sequence, String filename) {
        try {
            MidiSystem.write(sequence, 1, new File(filename));
        }
        catch(IOException ioe) {
            ioe.printStackTrace();
            System.exit(1);
        }
    }
}
