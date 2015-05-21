/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.jmei2midi;

import javax.sound.midi.InvalidMidiDataException;


/**
 * JUST USED FOR TESTING RUNNING CODE NOW.
 * @author dinamix
 */
public class Main {
    public static void main(String[] args) throws InvalidMidiDataException {
        MeiSequence test = new MeiSequence("/Users/dinamix/Documents/mei/"
                + "mei-test-set/MEI-files/clefs/treble-clef-out.mei");
    }  
}
