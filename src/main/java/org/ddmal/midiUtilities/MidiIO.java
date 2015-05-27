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
    //Maybe should create my own exception to deal with Midi IO
    public static void write(Sequence sequence, String filename) throws IOException {
        MidiSystem.write(sequence, 1, new File(filename));
    }
}
