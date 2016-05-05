/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.jmei2midi.meielements.meispecific;

import javax.sound.midi.InvalidMidiDataException;
import org.ddmal.jmei2midi.MeiSequence;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Tristano Tenaglia
 */
public class MeiSpecificStorageTest {
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getNumberOfGraceNotes method, of class MeiSpecificStorage.
     * @throws javax.sound.midi.InvalidMidiDataException
     */
    @Test
    public void testGetNumberOfGraceNotes() throws InvalidMidiDataException {
        String scarlatti = "./mei-test/CompleteExamples/Scarlatti_Sonata_in_C_major.mei";
        MeiSequence sequence = new MeiSequence(scarlatti);
        MeiSpecificStorage nonMidiStorage = sequence.getNonMidiStorage();
        
        //counted in file including grace notes and grace chords each
        int expectedGraceNotes = 44;
        int actualGraceNotes = nonMidiStorage.getNumberOfGraceNotes();
        
        assertEquals(expectedGraceNotes, actualGraceNotes);
    }
    
}
