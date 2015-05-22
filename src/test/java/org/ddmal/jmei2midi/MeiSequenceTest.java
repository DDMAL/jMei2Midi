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
import java.util.HashMap;
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
     * Test of processWork method, of class MeiSequence.
     */
    @Test
    public void testProcessWork() throws InvalidMidiDataException {
        //This doesnt work strangely
        //maybe debug from here
        /*Test 1 : Czerny
        String filename = "/Users/dinamix/Documents/mei/"
                + "music-encoding/samples/MEI2013/Music/Complete examples/Czerny_op603_6.mei";       
        MeiSequence czerny = new MeiSequence(filename);
        MeiWork expected = new MeiWork(1, "d", "minor", "4", "4", null);
        expected.addInstrVoice(1, "Organ");
        MeiWork actual = czerny.getWorks().get(1);
        assertEquals(expected, actual);*/
        
        //Test 2 Beethoven
        String filename2 = "/Users/dinamix/Documents/mei/"
                + "music-encoding/samples/MEI2013/Music/Complete examples/Beethoven_op.18.mei";       
        MeiSequence beethoven = new MeiSequence(filename2);
        MeiWork expected2 = new MeiWork(1, "f", "major", "3", "4", "Allegro con brio");
        expected2.addInstrVoice(1, "Violin I");
        expected2.addInstrVoice(2, "Violin II");
        expected2.addInstrVoice(3, "Viola");
        expected2.addInstrVoice(4, "Violoncello");
        MeiWork actual2 = beethoven.getWorks().get(1);
        assertEquals(expected2, actual2);
        
        MeiWork expected3 = new MeiWork(2, "f", "major", "9", "8", "Adagio affettuoso ed appasionato");
        expected3.addInstrVoice(1, "Violin I");
        expected3.addInstrVoice(2, "Violin II");
        expected3.addInstrVoice(3, "Viola");
        expected3.addInstrVoice(4, "Violoncello");
        MeiWork actual3 = beethoven.getWorks().get(2);
    }

    /**
     * Test of processScoreDef method, of class MeiSequence.
     * Will need to add tempo when process MeiHead is completed.
     * @throws javax.sound.midi.InvalidMidiDataException
     */
    @Ignore
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
        HashMap<Integer,MeiStaff> expected = new HashMap<>();
        expected.put(1, new MeiStaff(1, "Adagio", "", "0", "major", "4", "4"));
        assertEquals(expected, trebleclefout.getWorks());
        
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
        HashMap<Integer,MeiStaff> expected2 = new HashMap<>();
        expected2.put(1, new MeiStaff(1, "Adagio", "", "1f", "minor", "2", "4"));
        assertEquals(expected2, beethoven18.getWorks());
        
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
        HashMap<Integer,MeiStaff> expected3 = new HashMap<>();
        expected3.put(1, new MeiStaff(1, "Adagio", "", "0", "major", "6", "8"));
        assertEquals(expected3, berlioz2.getWorks());
    }

    /**
     * Test of processStaffDef method, of class MeiSequence.
     */
    @Ignore
    @Test
    public void testProcessStaffDef() {
        
    }
    
    @Test
    public void testCreateMeiStaff() throws InvalidMidiDataException {
        //Test 1 : treble-clef-out
        //No n attribute given in many of the mei-test-set files
        String filename = "/Users/dinamix/Documents/mei/"
                + "mei-test-set/MEI-files/clefs/treble-clef-out.mei";
        MeiSequence trebleclefout;
        try {
            trebleclefout = new MeiSequence(filename);
        }
        catch(InvalidMidiDataException imde) {
            throw new InvalidMidiDataException("createMeiStaff error");
        }
        MeiStaff actual = trebleclefout.getStaffs().get(1);
        MeiStaff expected = new MeiStaff(1, "Adagio", "Piano", "0", "major", "4", "4");
        HashMap<Integer,MeiStaff> expectedMap = new HashMap<>();
        expectedMap.put(1, expected);
        assertEquals(expected,actual);
        assertEquals(expectedMap, trebleclefout.getStaffs());
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
