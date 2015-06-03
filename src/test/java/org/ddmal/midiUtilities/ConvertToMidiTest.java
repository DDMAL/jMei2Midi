/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.midiUtilities;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author dinamix
 */
public class ConvertToMidiTest {
    
    public ConvertToMidiTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of tempoToBpm method, of class ConvertToMidi.
     */
    @Ignore
    @Test
    public void testTempoToBpm() {
    }
    
    /**
     * Test of DurToTick method, of class ConvertToMidi.
     */
    @Test
    public void testDurToTick() {
        assertEquals(128,ConvertToMidi.durToTick("8"));
        assertEquals(512,ConvertToMidi.durToTick("2"));
    }

    /**
     * Test of NoteToMidi method, of class ConvertToMidi.
     */
    @Test
    public void testNoteToMidi() {
        //Note C
        String c = "c";
        int actualC;
        int expectedC;
        for(Integer i = 0; i <= 11; i++) {
            actualC = ConvertToMidi.NoteToMidi(c, i.toString(), null);
            expectedC = i * 12;
            assertEquals(expectedC, actualC);
        }
        
        //C-sharp
        for(Integer i = 0; i <= 11; i++) {
            actualC = ConvertToMidi.NoteToMidi(c, i.toString(), "s");
            expectedC = 1 + i * 12;
            assertEquals(expectedC, actualC);
        }
        
        //C-sharp-sharp
        for(Integer i = 0; i <= 11; i++) {
            actualC = ConvertToMidi.NoteToMidi(c, i.toString(), "ss");
            expectedC = 2 + i * 12;
            assertEquals(expectedC, actualC);
        }
        
        //C-flat
        for(Integer i = 0; i <= 11; i++) {
            actualC = ConvertToMidi.NoteToMidi(c, i.toString(), "f");
            expectedC = -1 + i * 12;
            assertEquals(expectedC, actualC);
        }
        
        //C-flat-flat
        for(Integer i = 0; i <= 11; i++) {
            actualC = ConvertToMidi.NoteToMidi(c, i.toString(), "ff");
            expectedC = -2 + i * 12;
            assertEquals(expectedC, actualC);
        }
        
        //Note D
        String d = "d";
        int actualD;
        int expectedD;
        for(Integer i = 0; i <= 11; i++) {
            actualD = ConvertToMidi.NoteToMidi(d, i.toString(), null);
            expectedD = 2 + i * 12;
            assertEquals(expectedD, actualD);
        }
        
        //Note E
        String e = "e";
        int actualE;
        int expectedE;
        for(Integer i = 0; i <= 11; i++) {
            actualE = ConvertToMidi.NoteToMidi(e, i.toString(), null);
            expectedE = 4 + i * 12;
            assertEquals(expectedE, actualE);
        }
        
        //Note F
        String f = "f";
        int actualF;
        int expectedF;
        for(Integer i = 0; i <= 11; i++) {
            actualF = ConvertToMidi.NoteToMidi(f, i.toString(), null);
            expectedF = 5 + i * 12;
            assertEquals(expectedF, actualF);
        }
        
        //Note G
        String g = "g";
        int actualG;
        int expectedG;
        for(Integer i = 0; i <= 11; i++) {
            actualG = ConvertToMidi.NoteToMidi(g, i.toString(), null);
            expectedG = 7 + i * 12;
            assertEquals(expectedG, actualG);
        }
        
        //Note A
        String a = "a";
        int actualA;
        int expectedA;
        for(Integer i = 0; i <= 11; i++) {
            actualA = ConvertToMidi.NoteToMidi(a, i.toString(), null);
            expectedA = 9 + i * 12;
            assertEquals(expectedA, actualA);
        }
        
        //Note B
        String b = "b";
        int actualB;
        int expectedB;
        for(Integer i = 0; i <= 11; i++) {
            actualB = ConvertToMidi.NoteToMidi(b, i.toString(), null);
            expectedB = 11 + i * 12;
            assertEquals(expectedB, actualB);
        }
    }
    
}
