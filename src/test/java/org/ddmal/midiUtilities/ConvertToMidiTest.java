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
     * Test of instrToPerc method, of class ConvertToMidi.
     */
    @Test
    public void testInstrToPerc() {
        int nill = ConvertToMidi.instrToPerc(null);
        assertEquals(-1, nill);
        
        int snare = ConvertToMidi.instrToPerc("snare");
        assertEquals(38, snare);
        
        int defaul = ConvertToMidi.instrToPerc("default");
        assertEquals(-1, defaul);
    }
    
    /**
     * Test of instrToMidi method, of class ConvertToMidi.
     */
    @Test
    public void testInstrToMidi() {
        int nill = ConvertToMidi.instrToMidi(null);
        assertEquals(-1, nill);
        
        int piano = ConvertToMidi.instrToMidi("piano");
        assertEquals(0, piano);
        
        int defaul = ConvertToMidi.instrToMidi("default");
        assertEquals(-1, defaul);
    }
 
    /**
     * Test of tempoToBpm method, of class ConvertToMidi.
     */
    @Test
    public void testTempoToBpm() {
        int adagio = ConvertToMidi.tempoToBpm("adagio");
        assertEquals(71, adagio);
        
        int defaul = ConvertToMidi.tempoToBpm("default");
        assertEquals(90, defaul);
    }
    
    /**
     * Test of DurToTick method, of class ConvertToMidi.
     */
    @Test
    public void testDurToTick() {
        assertEquals(128,ConvertToMidi.durToTick("8",1,1,0));
        assertEquals(512,ConvertToMidi.durToTick("2",1,1,0));
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
            expectedC = i != 0 ? (i+1) * 12 : 0;
            assertEquals(expectedC, actualC);
        }
        assertEquals(60,ConvertToMidi.NoteToMidi("c","4",null));
        
        //C-sharp
        for(Integer i = 0; i <= 11; i++) {
            actualC = ConvertToMidi.NoteToMidi(c, i.toString(), "s");
            expectedC = i != 0 ? 1 + (i+1) * 12 : 1;
            assertEquals(expectedC, actualC);
        }
        
        //C-sharp-sharp
        for(Integer i = 0; i <= 11; i++) {
            actualC = ConvertToMidi.NoteToMidi(c, i.toString(), "ss");
            expectedC = i != 0 ? 2 + (i+1) * 12 : 2;
            assertEquals(expectedC, actualC);
        }
        
        //C-flat
        for(Integer i = 0; i <= 11; i++) {
            actualC = ConvertToMidi.NoteToMidi(c, i.toString(), "f");
            expectedC = i != 0 ? -1 + (i+1) * 12 : -1;
            assertEquals(expectedC, actualC);
        }
        
        //C-flat-flat
        for(Integer i = 0; i <= 11; i++) {
            actualC = ConvertToMidi.NoteToMidi(c, i.toString(), "ff");
            expectedC = i != 0 ? -2 + (i+1) * 12 : -2;
            assertEquals(expectedC, actualC);
        }
        
        //Note D
        String d = "d";
        int actualD;
        int expectedD;
        for(Integer i = 0; i <= 11; i++) {
            actualD = ConvertToMidi.NoteToMidi(d, i.toString(), null);
            expectedD = i != 0 ? 2 + (i+1) * 12 : 2;
            assertEquals(expectedD, actualD);
        }
        
        //Note E
        String e = "e";
        int actualE;
        int expectedE;
        for(Integer i = 0; i <= 11; i++) {
            actualE = ConvertToMidi.NoteToMidi(e, i.toString(), null);
            expectedE = i != 0 ? 4 + (i+1) * 12 : 4;
            assertEquals(expectedE, actualE);
        }
        
        //Note F
        String f = "f";
        int actualF;
        int expectedF;
        for(Integer i = 0; i <= 11; i++) {
            actualF = ConvertToMidi.NoteToMidi(f, i.toString(), null);
            expectedF = i != 0 ? 5 + (i+1) * 12 : 5;
            assertEquals(expectedF, actualF);
        }
        
        //Note G
        String g = "g";
        int actualG;
        int expectedG;
        for(Integer i = 0; i <= 11; i++) {
            actualG = ConvertToMidi.NoteToMidi(g, i.toString(), null);
            expectedG = i != 0 ? 7 + (i+1) * 12 : 7;
            assertEquals(expectedG, actualG);
        }
        
        //Note A
        String a = "a";
        int actualA;
        int expectedA;
        for(Integer i = 0; i <= 11; i++) {
            actualA = ConvertToMidi.NoteToMidi(a, i.toString(), null);
            expectedA = i != 0 ? 9 + (i+1) * 12 : 9;
            assertEquals(expectedA, actualA);
        }
        
        //Note B
        String b = "b";
        int actualB;
        int expectedB;
        for(Integer i = 0; i <= 11; i++) {
            actualB = ConvertToMidi.NoteToMidi(b, i.toString(), null);
            expectedB = i != 0 ? 11 + (i+1) * 12 : 11;
            assertEquals(expectedB, actualB);
        }
    }
    
}
