/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyTest.test;

import MyTest.SequenceTest;
import java.util.Arrays;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
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
public class TestSequenceTest {
    
    @Rule public ExpectedException exception = ExpectedException.none();
    
    public TestSequenceTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of bpmToByteArray method, of class SequenceTest.
     */
    @Test
    public void testBpmToByteArray() {
        assertArrayEquals(new byte[]{(byte) 0x07, (byte) 0xA1, (byte) 0x20},
                          SequenceTest.bpmToByteArray(120));
        assertArrayEquals(new byte[]{(byte) 0x0C, (byte) 0x0B, (byte) 0xE1},
                          SequenceTest.bpmToByteArray(76));
        assertArrayEquals(new byte[]{(byte) 0x65, (byte) 0xB9, (byte) 0xAA},
                          SequenceTest.bpmToByteArray(9));
        assertArrayEquals(new byte[]{(byte) 0xEA, (byte) 0x60},
                          SequenceTest.bpmToByteArray(1000));
        assertArrayEquals(new byte[]{(byte) 0x03, (byte) 0x93, (byte) 0x87, (byte) 0x00},
                          SequenceTest.bpmToByteArray(1));
        assertArrayEquals(new byte[]{(byte) 0x0A, (byte) 0x2C, (byte) 0x2A},
                          SequenceTest.bpmToByteArray(0));
        assertArrayEquals(new byte[]{(byte) 0x01},
                          SequenceTest.bpmToByteArray(60000000));
    }
    
    /**
     * Test of keysigToByteArray method of class SequenceTest.
     */
    @Test
    public void testKeysigToByteArray() {
        assertArrayEquals(new byte[]{0x01,0x01},
                          SequenceTest.keysigToByteArray("1s", "minor"));
        assertArrayEquals(new byte[]{-0x07,0x00},
                          SequenceTest.keysigToByteArray("7f", "major"));
        assertArrayEquals(new byte[]{0x07,0x01}, 
                          SequenceTest.keysigToByteArray("7s", "minor"));
        assertArrayEquals(new byte[]{0x00,0x00},
                          SequenceTest.keysigToByteArray("0", "major"));
        assertArrayEquals(new byte[]{0x00,0x01},
                          SequenceTest.keysigToByteArray("0", "MiNoR"));
    }
    
    /**
     * Test of createKeySignature method of class SequenceTest.
     */
    @Test
    public void testCreateKeySignature() throws InvalidMidiDataException {
        byte[] bytes = new byte[]{0x07,0x01};
        MidiMessage testMes = new MetaMessage(0x59,bytes,2);
        MidiEvent testEventExpect = new MidiEvent(testMes,0);
        MidiEvent testEventActual = SequenceTest.createKeySignature("7s", "minor", 0);
        
        assertArrayEquals(testEventExpect.getMessage().getMessage(),
                          testEventActual.getMessage().getMessage());
        assertEquals(testEventExpect.getTick(),
                     testEventActual.getTick());
        assertEquals(testEventExpect.getMessage().getLength(),
                     testEventActual.getMessage().getLength());
        
        //Check for exception here
        exception.expect(InvalidMidiDataException.class);
        MidiEvent testExc = new MidiEvent(new MetaMessage(20000,bytes,4),0);
    }
    
    /**
     * Test of createTrackTempo method, of class SequenceTest.
     * NOTE : I think MidiEvent.equals() only compares by reference and 
     * does not compare a deeper copy of the object.
     * @throws InvalidMidiDataException
     */
    @Test 
    public void testCreateTrackTempo() throws InvalidMidiDataException {
        byte[] bytes = new byte[]{(byte) 0x07, (byte) 0xA1, (byte) 0x20};
        MidiMessage testMes = new MetaMessage(0x51,bytes,3);
        MidiEvent testEventExpect = new MidiEvent(testMes,0);
        MidiEvent testEventActual = SequenceTest.createTrackTempo(120, 0);
        byte[] expected = testEventExpect.getMessage().getMessage();
        byte[] actual = testEventActual.getMessage().getMessage();
        //System.out.println("actual : " + Arrays.toString(actual) + " expected : " + Arrays.toString(expected));
        
        assertArrayEquals(expected,actual);
        assertEquals(testEventExpect.getTick(),testEventActual.getTick());
        assertArrayEquals(testEventExpect.getMessage().getMessage(),testEventActual.getMessage().getMessage());
        assertEquals(testEventExpect.getMessage().getLength(),testEventActual.getMessage().getLength());
        
        //Check for exception here
        exception.expect(InvalidMidiDataException.class);
        MidiEvent testExc = new MidiEvent(new MetaMessage(1000,bytes,3),0);
    }
    
    /**
     * Test of createProgramChange method, of class SequenceTest.
     * NOTE : I think MidiEvent.equals() only compares by reference and 
     * does not compare a deeper copy of the object.
     * @throws InvalidMidiDataException 
     */
    @Test
    public void testCreateProgramChange() throws InvalidMidiDataException{
        ShortMessage expMess = new ShortMessage(ShortMessage.PROGRAM_CHANGE, 0, 8, 0);
        MidiEvent expected = new MidiEvent(expMess, 0);
        assertArrayEquals(expected.getMessage().getMessage(),
                     SequenceTest.createProgramChange(8, 0, 0).getMessage().getMessage());
        assertEquals(expected.getTick(),
                     SequenceTest.createProgramChange(8, 0, 0).getTick());
        
        //Check for exception here
        exception.expect(InvalidMidiDataException.class);
        MidiEvent testExc = new MidiEvent(new ShortMessage(1000),0);
    }
}
