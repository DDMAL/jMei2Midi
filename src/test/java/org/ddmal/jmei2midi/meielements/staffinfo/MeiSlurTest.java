package org.ddmal.jmei2midi.meielements.staffinfo;

import org.ddmal.jmei2midi.MeiSequence;
import org.ddmal.jmei2midi.meielements.meispecific.MeiSlurNote;
import org.ddmal.jmei2midi.meielements.meispecific.MeiSpecificStorage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by dinamix on 7/11/16.
 */
public class MeiSlurTest {
    private File chopinTest;
    private MeiSequence chopinMeiSequence;
    @Before
    public void setUp() throws Exception {
        chopinTest = new File("./mei-test/CompleteExamples/Chopin_Etude_op.10_no.9_2013.mei");
        chopinMeiSequence = new MeiSequence(chopinTest);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void checkNoteSlur() throws Exception {
        MeiSpecificStorage meiSpecificStorage = chopinMeiSequence.getNonMidiStorage();
        List<MeiSlurNote> slurNoteList =  meiSpecificStorage.getSlurNoteList();
        assertEquals(361, slurNoteList.size());
    }

}