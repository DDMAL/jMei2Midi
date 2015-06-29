/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.jmei2midi;

import java.util.List;
import javax.sound.midi.InvalidMidiDataException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 *
 * @author dinamix
 */
public class MeiStatTrackerTest {
    
    @Rule public ExpectedException exception = ExpectedException.none();
    
    public MeiStatTrackerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of getFileName method, of class MeiStatTracker.
     */
    @Test
    public void testGetFileName() {
        String expectedfilename = "test.mei";
        MeiStatTracker test = new MeiStatTracker(expectedfilename);
        String actualfile = test.getFileName();
        boolean hasFile = test.getAllFiles().containsKey(expectedfilename);
        assertEquals("test.mei",actualfile);
        assertTrue(hasFile);
    }

    /**
     * Test of setFileName method, of class MeiStatTracker.
     */
    @Test
    public void testSetFileName() {
        String expectedfilename = "test.mei";
        MeiStatTracker test = new MeiStatTracker();
        test.setFileName(expectedfilename);
        String actualfile = test.getFileName();
        boolean hasFile = test.getAllFiles().containsKey(expectedfilename);
        assertEquals("test.mei",actualfile);
        assertTrue(hasFile);
    }

    /**
     * Test of getIncorrectFiles method, of class MeiStatTracker.
     */
    @Test
    public void testGetIncorrectFiles() {
        String expectedfilename = "test.mei";
        MeiStatTracker test = new MeiStatTracker("test.mei");
        test.addInvalidInstrument("wronginstrument");
        boolean actualfile = test.getIncorrectFiles().containsKey("test.mei");
        assertTrue(actualfile);
    }

    /**
     * Test of getInvalidInstruments method, of class MeiStatTracker.
     * @throws javax.sound.midi.InvalidMidiDataException
     */
    //@Test
    public void testGetInvalidInstruments() throws InvalidMidiDataException {
        String expectedfilename = "/Users/dinamix/Documents/music-encoding/"
                + "samples/MEI2013/Music/Complete examples/Altenburg_concerto_C_major.mei";
        MeiStatTracker stats = new MeiStatTracker(expectedfilename);
        MeiSequence sequence = new MeiSequence(expectedfilename, stats);
        List<String> invalidInstruments = stats.getInvalidInstruments().get(expectedfilename);
        assertTrue(invalidInstruments.contains("prinzipal-chor 1"));
        assertTrue(invalidInstruments.contains("prinzipal-2.chor"));
    }

    /**
     * Test of addInvalidInstruments method, of class MeiStatTracker.
     */
    @Test
    public void testAddInvalidInstruments() {
        String expectedfilename = "test.mei";
        MeiStatTracker test = new MeiStatTracker("test.mei");
        test.addInvalidInstrument("sitar");
        boolean actualfile = test.getIncorrectFiles().containsKey("test.mei");
        assertTrue(actualfile);
        assertTrue(test.getInvalidInstruments().get(expectedfilename).contains("sitar"));
    }

    /**
     * Test of getInvalidTempos method, of class MeiStatTracker.
     * @throws javax.sound.midi.InvalidMidiDataException
     */
    //@Test
    public void testGetInvalidTempos() throws InvalidMidiDataException {
        String expectedfilename = "/Users/dinamix/Documents/music-encoding/"
                + "samples/MEI2013/Music/Complete examples/McFerrin_Don't_worry.mei";
        MeiStatTracker stats = new MeiStatTracker(expectedfilename);
        MeiSequence sequence = new MeiSequence(expectedfilename, stats);
        List<String> invalidTempos = stats.getInvalidTempos().get(expectedfilename);
        assertTrue(invalidTempos.contains("undefined"));
    }

    /**
     * Test of addInvalidTempos method, of class MeiStatTracker.
     */
    @Test
    public void testAddInvalidTempos() {
        String expectedfilename = "test.mei";
        MeiStatTracker test = new MeiStatTracker(expectedfilename);
        test.addInvalidTempo("rubatissimo");
        boolean actualfile = test.getIncorrectFiles().containsKey("test.mei");
        assertTrue(actualfile);
        assertTrue(test.getInvalidTempos().get(expectedfilename).contains("rubatissimo"));
    }
    
}
