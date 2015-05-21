/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.jmei2midi;

import ca.mcgill.music.ddmal.mei.MeiDocument;
import ca.mcgill.music.ddmal.mei.MeiElement;
import ca.mcgill.music.ddmal.mei.MeiXmlReader;
import java.util.Arrays;
import java.util.List;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Sequence;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author dinamix
 */
public class MeiSequenceTest {
    
    public MeiSequenceTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of getSequence method, of class MeiSequence.
     */
    @Ignore
    @Test
    public void testGetSequence() {

    }

    /**
     * Test of recursiveDFS method, of class MeiSequence.
     */
    @Ignore
    @Test
    public void testRecursiveDFS() {

    }

    /**
     * Test of processElement method, of class MeiSequence.
     */
    @Ignore
    @Test
    public void testProcessElement() {

    }

    /**
     * Test of processParent method, of class MeiSequence.
     */
    @Ignore
    @Test
    public void testProcessParent() {

    }

    /**
     * Test of processScoreDef method, of class MeiSequence.
     * Will need to add tempo when process MeiHead is completed.
     * @throws javax.sound.midi.InvalidMidiDataException
     */
    @Test
    public void testProcessScoreDef() throws InvalidMidiDataException {
        //Test 1 : treble-clef-out
        String filename = "/Users/dinamix/Documents/mei/"
                + "mei-test-set/MEI-files/clefs/treble-clef-out.mei";
        MeiSequence trebleclefout;
        try {
            trebleclefout = new MeiSequence(filename);
        }
        catch(InvalidMidiDataException imde) {
            throw new InvalidMidiDataException("scoreDef MIDI error");
        }
        System.out.println(Arrays.toString(trebleclefout.getDefaults()));
        String[] expected = {null,"4","4",null,null};
        assertArrayEquals(expected, trebleclefout.getDefaults());
        
        //Test 2 : Beethoven_op.18
        String filename2 = "/Users/dinamix/Documents/mei/"
                + "music-encoding/samples/MEI2013/Music/Complete examples/Beethoven_op.18.mei";
        MeiSequence beethoven18;
        try {
            beethoven18 = new MeiSequence(filename2);
        }
        catch(InvalidMidiDataException imde) {
            throw new InvalidMidiDataException("scoreDef MIDI error 2");
        }
        System.out.println(Arrays.toString(beethoven18.getDefaults()));
        String[] expected2 = {null,"2","4","1f","minor"};
        assertArrayEquals(expected2, beethoven18.getDefaults());
        
        //Test 3 : Berlioz
        String filename3 = "/Users/dinamix/Documents/mei/"
                + "music-encoding/samples/MEI2013/Music/Complete examples/Berlioz_Symphony_op25.mei";
        MeiSequence berlioz2;
        try {
            berlioz2 = new MeiSequence(filename3);
        }
        catch(InvalidMidiDataException imde) {
            throw new InvalidMidiDataException("scoreDef MIDI error 2");
        }
        System.out.println(Arrays.toString(berlioz2.getDefaults()));
        String[] expected3 = {null,"6","8","0","major"};
        assertArrayEquals(expected3, berlioz2.getDefaults());
    }

    /**
     * Test of processStaffDef method, of class MeiSequence.
     */
    @Ignore
    @Test
    public void testProcessStaffDef() {

    }

    /**
     * Test of processChord method, of class MeiSequence.
     */
    @Ignore
    @Test
    public void testProcessChord() {

    }

    /**
     * Test of processNote method, of class MeiSequence.
     */
    @Ignore
    @Test
    public void testProcessNote() {

    }

    /**
     * Test of processRest method, of class MeiSequence.
     */
    @Ignore
    @Test
    public void testProcessRest() {

    }
    
}
