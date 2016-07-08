package org.ddmal.jmei2midi.meielements.meispecific;

import org.ddmal.jmei2midi.MeiSequence;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by dinamix on 5/9/16.
 */
public class MeiGraceNoteTest {
    @Test
    public void getGraceAttribute() throws Exception {
        String scarlatti = "./mei-test/CompleteExamples/Scarlatti_Sonata_in_C_major.mei";
        MeiSequence sequence = new MeiSequence(scarlatti);
        MeiSpecificStorage nonMidiStorage = sequence.getNonMidiStorage();
        List<MeiGraceNote> graceNoteList = nonMidiStorage.getGraceNoteList();

        MeiGraceNote firstGraceNote = graceNoteList.get(0);
        String firstGraceNoteMeasure = firstGraceNote.getLocationInScore();
        String expectedLocation = "4";
        assertEquals(expectedLocation,firstGraceNoteMeasure);
    }

}