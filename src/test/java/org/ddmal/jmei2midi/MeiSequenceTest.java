/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.jmei2midi;

import java.util.HashMap;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;
import org.ddmal.jmei2midi.meielements.staffinfo.MeiStaff;
import org.ddmal.jmei2midi.meielements.staffinfo.MeiWork;
import org.ddmal.midiUtilities.ConvertToMidi;
import org.ddmal.midiUtilities.MidiBuildMessage;
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
     * @throws javax.sound.midi.InvalidMidiDataException
     */
    @Test
    public void testGetSequence() throws InvalidMidiDataException {
        //Test for treble-clef-out
        String filename = "mei-test/mei-test-set/clefs/treble-clef.mei";
        MeiSequence trebleclefout = new MeiSequence(filename);
        Track[] actualtco = trebleclefout.getSequence().getTracks();
        Sequence sequence = new Sequence(Sequence.PPQ, 256, 1);
        Track[] expectedtco = sequence.getTracks();
        expectedtco[0].add(MidiBuildMessage.createKeySignature("0", "major", 0));
        expectedtco[0].add(MidiBuildMessage.createProgramChange(54, 0, 0));
        expectedtco[0].add(MidiBuildMessage.createTrackTempo(90, 0));
        
        int note = ConvertToMidi.NoteToMidi("g", "4", null);
        expectedtco[0].add(MidiBuildMessage.createNoteOnEvent(note, 0, 0));
        expectedtco[0].add(MidiBuildMessage.createNoteOffEvent(note, 1024, 0));
        expectedtco[0].add(new MidiEvent(new MetaMessage(0x2F, new byte[0], 0), 0));
        for(int x = 0; x < actualtco.length; x++) {
            for(int i = 0; i < actualtco[x].size(); i++) {
                byte[] actualbytes = actualtco[x].get(i).getMessage().getMessage();
                byte[] expectedbytes = expectedtco[x].get(i).getMessage().getMessage();
                assertArrayEquals(expectedbytes, actualbytes);
                assertEquals(expectedtco[x].get(i).getTick(), actualtco[x].get(i).getTick());
            }
        }
        for(int x = 0; x < expectedtco.length; x++) {
            for(int i = 0; i < expectedtco[x].size(); i++) {
                byte[] actualbytes = actualtco[x].get(i).getMessage().getMessage();
                byte[] expectedbytes = expectedtco[x].get(i).getMessage().getMessage();
                assertArrayEquals(expectedbytes, actualbytes);
                assertEquals(expectedtco[x].get(i).getTick(), actualtco[x].get(i).getTick());
            }
        }
    }
    
    /**
     * Test of processWork method, of class MeiSequence.
     */
    @Test
    public void testProcessWork() throws InvalidMidiDataException {
        //Test 1 : Czerny
        String filename = "mei-test/CompleteExamples/Czerny_op603_6.mei";       
        MeiSequence czerny = new MeiSequence(filename);
        MeiWork expected = new MeiWork(1, "d", "minor", "4", "4", "default");
        expected.addInstrVoice(1, "Organ");
        MeiWork actual = czerny.getWorks().get(1);
        assertEquals(expected, actual);
        
        //Test 2 Beethoven
        String filename2 = "mei-test/CompleteExamples/Beethoven_op.18.mei";       
        MeiSequence beethoven = new MeiSequence(filename2);
        MeiWork expectedB = new MeiWork(1, "f", "major", "3", "4", "Allegro con brio");
        expectedB.addInstrVoice(1, "Violin I");
        expectedB.addInstrVoice(2, "Violin II");
        expectedB.addInstrVoice(3, "Viola");
        expectedB.addInstrVoice(4, "Violoncello");
        MeiWork actualB = beethoven.getWorks().get(1);
        assertEquals(expectedB, actualB);
        
        MeiWork expectedB2 = new MeiWork(2, "f", "minor", "9", "8", "Adagio affettuoso ed appasionato");
        expectedB2.addInstrVoice(1, "Violin I");
        expectedB2.addInstrVoice(2, "Violin II");
        expectedB2.addInstrVoice(3, "Viola");
        expectedB2.addInstrVoice(4, "Violoncello");
        MeiWork actualB2 = beethoven.getWorks().get(2);
        assertEquals(expectedB2, actualB2);
        
        MeiWork expectedB3 = new MeiWork(3, "f", "major", "3", "4", "Scherzo. Allegro molto.");
        expectedB3.addInstrVoice(1, "Violin I");
        expectedB3.addInstrVoice(2, "Violin II");
        expectedB3.addInstrVoice(3, "Viola");
        expectedB3.addInstrVoice(4, "Violoncello");
        MeiWork actualB3 = beethoven.getWorks().get(3);
        assertEquals(expectedB3, actualB3);
        
        MeiWork expectedB4 = new MeiWork(4, "f", "minor", "2", "4", "Allegro.");
        expectedB4.addInstrVoice(1, "Violin I");
        expectedB4.addInstrVoice(2, "Violin II");
        expectedB4.addInstrVoice(3, "Viola");
        expectedB4.addInstrVoice(4, "Violoncello");
        MeiWork actualB4 = beethoven.getWorks().get(4);
        assertEquals(expectedB4, actualB4);
        
        HashMap<Integer,MeiWork> actualWorkB = new HashMap<>();
        actualWorkB.put(1, actualB);
        actualWorkB.put(2, actualB2);
        actualWorkB.put(3, actualB3);
        actualWorkB.put(4, actualB4);
        assertEquals(actualWorkB,beethoven.getWorks());
        
        //Test Grieg butterfly
        String filenameG = "mei-test/CompleteExamples/Grieg_op.43_butterfly.mei";
        MeiSequence griegB = new MeiSequence(filenameG);
        MeiWork actualG = griegB.getWorks().get(1);
        MeiWork expectedG = new MeiWork(1, "a", "major", "4", "4", "Allegro grazioso");
        expectedG.addInstrVoice(1, "Piano");
        assertEquals(expectedG,actualG);
        
        //Test Brahms String Quartet
        String filenameBrahms = "mei-test/CompleteExamples/Brahms_StringQuartet_Op51_No1.mei";
        MeiSequence brahmsS = new MeiSequence(filenameBrahms);
        MeiWork actualBS = brahmsS.getWorks().get(1);
        MeiWork expectedBS = new MeiWork(1, "c", "major", "3", "4", "Poco Adagio.");
        expectedBS.addInstrVoice(1, "Violin I");
        expectedBS.addInstrVoice(2, "Violin II");
        expectedBS.addInstrVoice(3, "Viola");
        expectedBS.addInstrVoice(4, "Violoncello");
        assertEquals(expectedBS, actualBS);
        
        //Test Brahms WieMelodien
        String filenameBrahmsW = "mei-test/CompleteExamples/Brahms_WieMelodienZiehtEsMir.mei";
        MeiSequence brahmsW = new MeiSequence(filenameBrahmsW);
        MeiWork actualBW = brahmsW.getWorks().get(1);
        MeiWork expectedBW = new MeiWork(1, "a", "major", "2", "2", "default");
        expectedBW.addInstrVoice(1, "Voice");
        expectedBW.addInstrVoice(2, "Piano");
        assertEquals(expectedBW, actualBW);
        
        //Test Vivaldi - switches scoreDef
        String filenameViv = "mei-test/CompleteExamples/Vivaldi_Op8_No.2.mei";
        MeiSequence viv = new MeiSequence(filenameViv);
        HashMap<Integer,MeiWork> actualViv = viv.getWorks();
        
        MeiWork expectedViv1 = new MeiWork(1, "g", "minor", "3", "8", "Allegro non molto");
        expectedViv1.addInstrVoice(1, "Violino Principale");
        expectedViv1.addInstrVoice(2, "Violino Primo");
        expectedViv1.addInstrVoice(3, "Violino Secondo");
        expectedViv1.addInstrVoice(4, "Alto Viola");
        expectedViv1.addInstrVoice(5, "Organo e Violoncello");
        assertEquals(expectedViv1, actualViv.get(1));
        
        MeiWork expectedViv2 = new MeiWork(2, "g", "minor", "4", "4", "Adagio");
        expectedViv2.addInstrVoice(1, "Violino Principale");
        expectedViv2.addInstrVoice(2, "Violino Primo");
        expectedViv2.addInstrVoice(3, "Violino Secondo");
        expectedViv2.addInstrVoice(4, "Alto Viola");
        expectedViv2.addInstrVoice(5, "Organo e Violoncello");
        assertEquals(expectedViv2, actualViv.get(2));
        
        MeiWork expectedViv3 = new MeiWork(3, "g", "minor", "3", "4", "Presto");
        expectedViv3.addInstrVoice(1, "Violino Principale");
        expectedViv3.addInstrVoice(2, "Violino Primo");
        expectedViv3.addInstrVoice(3, "Violino Secondo");
        expectedViv3.addInstrVoice(4, "Alto Viola");
        expectedViv3.addInstrVoice(5, "Organo e Violoncello");
        assertEquals(expectedViv3, actualViv.get(3));
        
        HashMap<Integer,MeiWork> expectViv = new HashMap<>();
        expectViv.put(1, expectedViv1);
        expectViv.put(2, expectedViv2);
        expectViv.put(3, expectedViv3);
        assertEquals(expectViv, actualViv);
        
        //Test JSB_BWV1047
        String filenameJSB = "mei-test/CompleteExamples/JSB_BWV1047_2.mei";
        MeiSequence JSB = new MeiSequence(filenameJSB);
        MeiWork actualJSB = JSB.getWorks().get(1);
        MeiWork expectedJSB = new MeiWork(1, "f", "major", "3", "4", "Andante");
        expectedJSB.addInstrVoice(1, "Flute");
        expectedJSB.addInstrVoice(2, "Oboe");
        expectedJSB.addInstrVoice(3, "Violin");
        expectedJSB.addInstrVoice(4, "Violoncello e Cembalo");
        assertEquals(expectedJSB, actualJSB);
        
        //Test Hummel - switches scoreDef
        String filenameHum = "mei-test/CompleteExamples/Hummel_op.67_No.11.mei";
        MeiSequence Hum = new MeiSequence(filenameHum);
        MeiWork actualHum = Hum.getWorks().get(1);
        MeiWork expectedHum = new MeiWork(1, "b", "major", "9", "4", "Allegro");
        expectedHum.addInstrVoice(1, "Piano");
        assertEquals(expectedHum, actualHum);
        
        //Test Joplin Elite - switch scoreDef key.sig
        String filenameJop = "mei-test/CompleteExamples/Joplin_Elite_Syncopations.mei";  
        MeiSequence Jop = new MeiSequence(filenameJop);
        MeiWork actualJop = Jop.getWorks().get(1);
        MeiWork expectedJop = new MeiWork(1, "f", "major", "2", "4", "Not fast");
        expectedJop.addInstrVoice(1, "Piano");
        assertEquals(expectedJop, actualJop);
        //Prints out invalid tempo of "Not fast"
        //System.out.println(Jop.getStats());
        
        //Test Chopin Etude
        String filenameChopin = "mei-test/CompleteExamples/Chopin_Etude_op.10_no.9_2013.mei";  
        MeiSequence Chopin = new MeiSequence(filenameChopin);
        MeiWork actualChopin = Chopin.getWorks().get(1);
        MeiWork expectedChopin = new MeiWork(1, "f", "minor", "6", "8", "Allegro molto agitato");
        expectedChopin.addInstrVoice(1, "Piano");
        assertEquals(expectedChopin, actualChopin);
        
        //Test Gluck
        String filenameGluck = "mei-test/CompleteExamples/Gluck_CheFaroSenzaEuridice.mei";  
        MeiSequence Gluck = new MeiSequence(filenameGluck);
        MeiWork actualGluck = Gluck.getWorks().get(1);
        MeiWork expectedGluck = new MeiWork(1, "c", "major", "4", "4", "Andante con moto");
        expectedGluck.addInstrVoice(1, "Soprano");
        expectedGluck.addInstrVoice(2, "Violin I");
        expectedGluck.addInstrVoice(3, "Violin II");
        expectedGluck.addInstrVoice(4, "Viola");
        expectedGluck.addInstrVoice(5, "Harpsichord");
        expectedGluck.addInstrVoice(6, "Violoncello");
        assertEquals(expectedGluck, actualGluck);
        
        //Test Beethoven Hymn to joe
        //A very inconsistent example between work and staff labels
        String filenameBHJ = "mei-test/CompleteExamples/Beethoven_Hymn_to_joy.mei";
        MeiSequence BHJ = new MeiSequence(filenameBHJ);
        MeiWork actualBHJ = BHJ.getWorks().get(1);
        MeiWork expectedBHJ = new MeiWork(1, "d", "major", "4", "4", "Allegro assai");
        expectedBHJ.addInstrVoice(1, "Sopran");
        expectedBHJ.addInstrVoice(2, "Alt");
        expectedBHJ.addInstrVoice(3, "Tenor");
        expectedBHJ.addInstrVoice(4, "Bass");
        expectedBHJ.addInstrVoice(5, "piccolo");
        expectedBHJ.addInstrVoice(6, "2 flutes");
        expectedBHJ.addInstrVoice(7, "2 oboes");
        expectedBHJ.addInstrVoice(8, "2 clarinets");
        expectedBHJ.addInstrVoice(9, "2 bassoon");
        expectedBHJ.addInstrVoice(10, "contrabassoon");
        expectedBHJ.addInstrVoice(11, "4 horns");
        expectedBHJ.addInstrVoice(12, "2 trumpets");
        expectedBHJ.addInstrVoice(13, "3 trombones");
        expectedBHJ.addInstrVoice(14, "timpani");
        expectedBHJ.addInstrVoice(15, "cymbals");
        expectedBHJ.addInstrVoice(16, "triangle");
        expectedBHJ.addInstrVoice(17, "bass drum");
        expectedBHJ.addInstrVoice(18, "string orchestra");
        assertEquals(expectedBHJ, actualBHJ);
        
        //Test McFerrin
        String filenameMcFerrin = "mei-test/CompleteExamples/McFerrin_Don't_worry.mei";
        MeiSequence McFerrin = new MeiSequence(filenameMcFerrin);
        MeiWork actualMcFerrin = McFerrin.getWorks().get(1);
        MeiWork expectedMcFerrin = new MeiWork(1, "c", "major", "4", "4", "undefined");
        expectedMcFerrin.addInstrVoice(1, "Voice");
        assertEquals(expectedMcFerrin, actualMcFerrin);
        
        //Test Telemann Concert
        String filenameTelemann = "mei-test/CompleteExamples/Telemann_Concert.mei";
        MeiSequence Telemann = new MeiSequence(filenameTelemann);
        MeiWork actualTelemann = Telemann.getWorks().get(1);
        MeiWork expectedTelemann = new MeiWork(1, "d", "major", "6", "8", "Adagio");
        expectedTelemann.addInstrVoice(1, "Trumpet in C 1");
        expectedTelemann.addInstrVoice(2, "Trumpet in C 2");
        expectedTelemann.addInstrVoice(3, "Trumpet in C 3");
        expectedTelemann.addInstrVoice(4, "Timpani");
        expectedTelemann.addInstrVoice(5, "Organ");
        assertEquals(expectedTelemann, actualTelemann);
    }

    /**
     * Test of processScoreDef method, of class MeiSequence.
     * Will need to add tempo when process MeiHead is completed.
     * @throws javax.sound.midi.InvalidMidiDataException
     */
    @Test
    public void testProcessScoreDef() throws InvalidMidiDataException {
        //Test 1 : treble-clef-out
        String filename = "mei-test/mei-test-set/clefs/treble-clef.mei";
        MeiSequence trebleclefout;
        try {
            trebleclefout = new MeiSequence(filename);
            for(MeiStaff staff : trebleclefout.getStaffs().values()) {
                staff.setTick(0);
                staff.setTickLayer(0);
            }
        }
        catch(InvalidMidiDataException imde) {
            throw new InvalidMidiDataException("scoreDef MIDI error");
        }
        HashMap<Integer,MeiStaff> expected = new HashMap<>();
        expected.put(1, new MeiStaff(1, "default", "Voice", "0", "major", "4", "4"));
        assertEquals(expected, trebleclefout.getStaffs());
        
        //Test 2 : Beethoven_op.18
        String filename2 = "mei-test/CompleteExamples/Beethoven_op.18.mei";
        MeiSequence beethoven18;
        try {
            beethoven18 = new MeiSequence(filename2);
            for(MeiStaff staff : beethoven18.getStaffs().values()) {
                staff.setTick(0);
                staff.setTickLayer(0);
            }
        }
        catch(InvalidMidiDataException imde) {
            throw new InvalidMidiDataException("scoreDef MIDI error 2");
        }
        HashMap<Integer,MeiStaff> expected2 = new HashMap<>();
        expected2.put(1, new MeiStaff(1, "Allegro.", "Violin I", "1f", "minor", "2", "4"));
        assertEquals(expected2.get(1), beethoven18.getStaffs().get(1));
        
        //Test 3 : Berlioz
        String filename3 = "mei-test/CompleteExamples/Berlioz_Symphony_op25.mei";
        MeiSequence berlioz2;
        try {
            berlioz2 = new MeiSequence(filename3);
            for(MeiStaff staff : berlioz2.getStaffs().values()) {
                staff.setTick(0);
                staff.setTickLayer(0);
            }
        }
        catch(InvalidMidiDataException imde) {
            throw new InvalidMidiDataException("scoreDef MIDI error 2");
        }
        HashMap<Integer,MeiStaff> expected3 = new HashMap<>();
        expected3.put(1, new MeiStaff(1, "Allegretto grazioso", "Tenor", "0", "major", "6", "8"));
        assertEquals(expected3.get(1), berlioz2.getStaffs().get(1));
    }

    /**
     * Test of processStaffDef method, of class MeiSequence.
     * Sets all ticks to 0 in order to just test the staffs.
     * @throws javax.sound.midi.InvalidMidiDataException
     */
    @Test
    public void testProcessStaffDef() throws InvalidMidiDataException {
        //Test 1 : Czerny (Organ w/ 3 staves)
        String czernyfile = "mei-test/CompleteExamples/Czerny_op603_6.mei";       
        MeiSequence czerny = new MeiSequence(czernyfile);
        HashMap<Integer,MeiStaff> actualCz = czerny.getStaffs();
        for(MeiStaff staff : actualCz.values()) {
                staff.setTick(0);
                staff.setTickLayer(0);
        }
        HashMap<Integer,MeiStaff> expectedCz = new HashMap<>();
        expectedCz.put(1, new MeiStaff(1, "default", "Organ", "1f", "minor", "4", "4"));
        expectedCz.put(2, new MeiStaff(2, "default", "Organ", "1f", "minor", "4", "4"));
        expectedCz.put(3, new MeiStaff(3, "default", "Organ", "1f", "minor", "4", "4"));
        assertEquals(expectedCz, actualCz);
        
        //Test 2 : Grieg butterfly
        String griegfile = "mei-test/CompleteExamples/Grieg_op.43_butterfly.mei";
        MeiSequence grieg = new MeiSequence(griegfile);
        HashMap<Integer,MeiStaff> actualGr = grieg.getStaffs();
        for(MeiStaff staff : actualGr.values()) {
                staff.setTick(0);
                staff.setTickLayer(0);
        }
        HashMap<Integer,MeiStaff> expectedGr = new HashMap<>();
        expectedGr.put(1, new MeiStaff(1, "Allegro grazioso", "Piano", "3s", "major", "4", "4"));
        expectedGr.put(2, new MeiStaff(2, "Allegro grazioso", "Piano", "3s", "major", "4", "4"));
        assertEquals(expectedGr, actualGr);
        
        //Test : Brahms String quartet
        String filenameBrahms = "mei-test/CompleteExamples/Brahms_StringQuartet_Op51_No1.mei";
        MeiSequence brahmsS = new MeiSequence(filenameBrahms);
        HashMap<Integer,MeiStaff> actualBS = brahmsS.getStaffs();
        for(MeiStaff staff : actualBS.values()) {
                staff.setTick(0);
                staff.setTickLayer(0);
        }
        HashMap<Integer,MeiStaff> expectedBS = new HashMap<>();
        expectedBS.put(1, new MeiStaff(1, "Poco Adagio.", "Violin I", "4f", "major", "3", "4"));
        expectedBS.put(2, new MeiStaff(2, "Poco Adagio.", "Violin II", "4f", "major", "3", "4"));
        expectedBS.put(3, new MeiStaff(3, "Poco Adagio.", "Viola", "4f", "major", "3", "4"));
        expectedBS.put(4, new MeiStaff(4, "Poco Adagio.", "Violoncello", "4f", "major", "3", "4"));
        //Strange staffDef @ n=6 but we add it to make test work (never used in piece)
        expectedBS.put(6, new MeiStaff(6,"Poco Adagio.", "Voice", "4f", "major", "3", "4"));
        assertEquals(expectedBS, actualBS);
        
        //Test : Brahms Wie
        String filenameBrahmsW = "mei-test/CompleteExamples/Brahms_WieMelodienZiehtEsMir.mei";
        MeiSequence brahmsW = new MeiSequence(filenameBrahmsW);
        HashMap<Integer,MeiStaff> actualBW = brahmsW.getStaffs();
        for(MeiStaff staff : actualBW.values()) {
                staff.setTick(0);
                staff.setTickLayer(0);
        }
        HashMap<Integer,MeiStaff> expectedBW = new HashMap<>();
        expectedBW.put(1, new MeiStaff(1, "default", "Voice", "3s", "major", "2", "2"));
        expectedBW.put(2, new MeiStaff(2, "default", "Piano", "3s", "major", "2", "2"));
        expectedBW.put(3, new MeiStaff(3, "default", "Piano", "3s", "major", "2", "2"));
        assertEquals(expectedBW, actualBW);
        
        //Test : Vivaldi
        String filenameViv = "mei-test/CompleteExamples/Vivaldi_Op8_No.2.mei";
        MeiSequence viv = new MeiSequence(filenameViv);
        HashMap<Integer,MeiStaff> actualViv = viv.getStaffs();
        for(MeiStaff staff : actualViv.values()) {
                staff.setTick(0);
                staff.setTickLayer(0);
        }
        HashMap<Integer,MeiStaff> expectViv = new HashMap<>();
        expectViv.put(1, new MeiStaff(1, "Presto", "Violino Principale", "2f", "minor", "3", "4"));
        expectViv.put(2, new MeiStaff(2, "Presto", "Violino Primo", "2f", "minor", "3", "4"));
        expectViv.put(3, new MeiStaff(3, "Presto", "Violino Secondo", "2f", "minor", "3", "4"));
        expectViv.put(4, new MeiStaff(4, "Presto", "Alto Viola", "2f", "minor", "3", "4"));
        expectViv.put(5, new MeiStaff(5, "Presto", "Organo e Violencello", "2f", "minor", "3", "4"));
        assertEquals(expectViv, actualViv);
        
        //Test : JSB
        String filenameJSB = "mei-test/CompleteExamples/JSB_BWV1047_2.mei";
        MeiSequence JSB = new MeiSequence(filenameJSB);
        HashMap<Integer,MeiStaff> actualJSB = JSB.getStaffs();
        for(MeiStaff staff : actualJSB.values()) {
                staff.setTick(0);
                staff.setTickLayer(0);
        }
        HashMap<Integer,MeiStaff> expectJSB = new HashMap<>();
        expectJSB.put(1, new MeiStaff(1, "Andante", "Flute", "1f", "major", "3", "4"));
        expectJSB.put(2, new MeiStaff(2, "Andante", "Oboe", "1f", "major", "3", "4"));
        expectJSB.put(3, new MeiStaff(3, "Andante", "Violin", "1f", "major", "3", "4"));
        expectJSB.put(4, new MeiStaff(4, "Andante", "Violoncello e Cembalo", "1f", "major", "3", "4"));
        assertEquals(expectJSB, actualJSB);
        
        //Test : Hummel - switch scoreDef
        String filenameHum = "mei-test/CompleteExamples/Hummel_op.67_No.11.mei";
        MeiSequence Hum = new MeiSequence(filenameHum);
        HashMap<Integer,MeiStaff> actualHum = Hum.getStaffs();
        for(MeiStaff staff : actualHum.values()) {
                staff.setTick(0);
                staff.setTickLayer(0);
        }
        HashMap<Integer,MeiStaff> expectedHum = new HashMap<>();
        expectedHum.put(1, new MeiStaff(1, "Allegro", "Piano", "5s", "major", "9", "4"));
        expectedHum.put(2, new MeiStaff(2, "Allegro", "Piano", "5s", "major", "9", "4"));
        assertEquals(expectedHum, actualHum);
        
        //Test Joplin Elite - switch scoreDef key.sig
        String filenameJop = "mei-test/CompleteExamples/Joplin_Elite_Syncopations.mei";  
        MeiSequence Jop = new MeiSequence(filenameJop);
        HashMap<Integer,MeiStaff> actualJop = Jop.getStaffs();
        for(MeiStaff staff : actualJop.values()) {
                staff.setTick(0);
                staff.setTickLayer(0);
        }
        HashMap<Integer,MeiStaff> expectedJop = new HashMap<>();
        expectedJop.put(1, new MeiStaff(1, "Not fast", "Piano", "2f", "major", "2", "4"));
        expectedJop.put(2, new MeiStaff(2, "Not fast", "Piano", "2f", "major", "2", "4"));
        assertEquals(expectedJop, actualJop);
        
        //Test Debussy Mandolin
        String filenameDeb = "mei-test/CompleteExamples/Debussy_Mandoline.mei";  
        MeiSequence Deb = new MeiSequence(filenameDeb);
        HashMap<Integer,MeiStaff> actualDeb = Deb.getStaffs();
        for(MeiStaff staff : actualDeb.values()) {
                staff.setTick(0);
                staff.setTickLayer(0);
        }
        HashMap<Integer,MeiStaff> expectedDeb = new HashMap<>();
        //Voice good because label is chant and so since chant is not valid
        //we take voice in meihead as the instrument instead
        expectedDeb.put(1, new MeiStaff(1, "Allegretto vivace", "Voice", "0", "major", "6", "8"));
        expectedDeb.put(2, new MeiStaff(2, "Allegretto vivace", "Piano", "0", "major", "6", "8"));
        expectedDeb.put(3, new MeiStaff(3, "Allegretto vivace", "Piano", "0", "major", "6", "8"));
        assertEquals(expectedDeb, actualDeb);
        //System.out.println(Deb.getStats());
        
        //Test Chopin Etude
        String filenameChopin = "mei-test/CompleteExamples/Chopin_Etude_op.10_no.9_2013.mei";  
        MeiSequence Chopin = new MeiSequence(filenameChopin);
        HashMap<Integer,MeiStaff> actualChopin = Chopin.getStaffs();
        for(MeiStaff staff : actualChopin.values()) {
                staff.setTick(0);
                staff.setTickLayer(0);
        }
        HashMap<Integer,MeiStaff> expectedChopin = new HashMap<>();
        expectedChopin.put(1, new MeiStaff(1, "Allegro molto agitato", "Piano", "4f", "minor", "6", "8"));
        expectedChopin.put(2, new MeiStaff(2, "Allegro molto agitato", "Piano", "4f", "minor", "6", "8"));
        assertEquals(expectedChopin, actualChopin);
        
        //Test Gluck
        String filenameGluck = "mei-test/CompleteExamples/Gluck_CheFaroSenzaEuridice.mei";  
        MeiSequence Gluck = new MeiSequence(filenameGluck);
        HashMap<Integer,MeiStaff> actualGluck = Gluck.getStaffs();
        for(MeiStaff staff : actualGluck.values()) {
                staff.setTick(0);
                staff.setTickLayer(0);
        }
        HashMap<Integer,MeiStaff> expectedGluck = new HashMap<>();
        expectedGluck.put(1, new MeiStaff(1, "Andante con moto", "Soprano", "0", "major", "4", "4"));
        expectedGluck.put(2, new MeiStaff(2, "Andante con moto", "Violin I", "0", "major", "4", "4"));
        expectedGluck.put(3, new MeiStaff(3, "Andante con moto", "Violin II", "0", "major", "4", "4"));
        expectedGluck.put(4, new MeiStaff(4, "Andante con moto", "Viola", "0", "major", "4", "4"));
        expectedGluck.put(5, new MeiStaff(5, "Andante con moto", "Harpsichord", "0", "major", "4", "4"));
        expectedGluck.put(6, new MeiStaff(6, "Andante con moto", "Harpsichord", "0", "major", "4", "4"));
        expectedGluck.put(7, new MeiStaff(7, "Andante con moto", "Cello", "0", "major", "4", "4"));
        assertEquals(expectedGluck, actualGluck);
        
        //Test Beethoven Hymn to joe
        String filenameBHJ = "mei-test/CompleteExamples/Beethoven_Hymn_to_joy.mei";
        MeiSequence BHJ = new MeiSequence(filenameBHJ);
        HashMap<Integer,MeiStaff> actualBHJ = BHJ.getStaffs();
        for(MeiStaff staff : actualBHJ.values()) {
                staff.setTick(0);
                staff.setTickLayer(0);
        }
        HashMap<Integer,MeiStaff> expectedBHJ = new HashMap<>();
        expectedBHJ.put(1, new MeiStaff(1, "Allegro assai", "Soprano", "2s", "major", "4", "4"));
        expectedBHJ.put(2, new MeiStaff(2, "Allegro assai", "Alto", "2s", "major", "4", "4"));
        expectedBHJ.put(3, new MeiStaff(3, "Allegro assai", "Tenor", "2s", "major", "4", "4"));
        expectedBHJ.put(4, new MeiStaff(4, "Allegro assai", "Baritone", "2s", "major", "4", "4"));
        expectedBHJ.put(5, new MeiStaff(5, "Allegro assai", "Bass", "2s", "major", "4", "4"));
        expectedBHJ.put(6, new MeiStaff(6, "Allegro assai", "Piccolo", "2s", "major", "4", "4"));
        expectedBHJ.put(7, new MeiStaff(7, "Allegro assai", "Flute", "2s", "major", "4", "4"));
        expectedBHJ.put(8, new MeiStaff(8, "Allegro assai", "Oboe", "2s", "major", "4", "4"));
        expectedBHJ.put(9, new MeiStaff(9, "Allegro assai", "Bassoon", "2s", "major", "4", "4"));
        expectedBHJ.put(10, new MeiStaff(10, "Allegro assai", "Clarinet in B", "4s", "major", "4", "4"));
        expectedBHJ.put(11, new MeiStaff(11, "Allegro assai", "Bass Clarinet", "4s", "major", "4", "4"));
        expectedBHJ.put(12, new MeiStaff(12, "Allegro assai", "Alto Sax.", "5s", "major", "4", "4"));
        expectedBHJ.put(13, new MeiStaff(13, "Allegro assai", "Tenor Sax.", "4s", "major", "4", "4"));
        expectedBHJ.put(14, new MeiStaff(14, "Allegro assai", "Baritone Sax.", "5s", "major", "4", "4"));
        expectedBHJ.put(15, new MeiStaff(15, "Allegro assai", "Trumpet in B", "4s", "major", "4", "4"));
        expectedBHJ.put(16, new MeiStaff(16, "Allegro assai", "Horn in F 1", "3s", "major", "4", "4"));
        expectedBHJ.put(17, new MeiStaff(17, "Allegro assai", "Trombone", "2s", "major", "4", "4"));
        expectedBHJ.put(18, new MeiStaff(18, "Allegro assai", "Bass Trombone", "2s", "major", "4", "4"));
        expectedBHJ.put(19, new MeiStaff(19, "Allegro assai", "Euphonium", "2s", "major", "4", "4"));
        expectedBHJ.put(20, new MeiStaff(20, "Allegro assai", "Tuba", "2s", "major", "4", "4"));
        expectedBHJ.put(21, new MeiStaff(21, "Allegro assai", "Timpani", "2s", "major", "4", "4"));
        expectedBHJ.put(22, new MeiStaff(22, "Allegro assai", "Tubular Bells", "2s", "major", "4", "4"));
        expectedBHJ.put(23, new MeiStaff(23, "Allegro assai", "Glockenspiel", "2s", "major", "4", "4"));
        expectedBHJ.put(24, new MeiStaff(24, "Allegro assai", "Snare", "2s", "major", "4", "4"));
        assertEquals(expectedBHJ, actualBHJ);
        
        //Test McFerrin
        String filenameMcFerrin = "mei-test/CompleteExamples/McFerrin_Don't_worry.mei";
        MeiSequence McFerrin = new MeiSequence(filenameMcFerrin);
        HashMap<Integer,MeiStaff> actualMcFerrin = McFerrin.getStaffs();
        for(MeiStaff staff : actualMcFerrin.values()) {
                staff.setTick(0);
                staff.setTickLayer(0);
        }
        HashMap<Integer,MeiStaff> expectedMcFerrin = new HashMap<>();
        expectedMcFerrin.put(1, new MeiStaff(1, "undefined", "Voice", "0", "major", "4", "4"));
        assertEquals(expectedMcFerrin, actualMcFerrin);
        
        //Test Telemann
        String filenameTelemann = "mei-test/CompleteExamples/Telemann_Concert.mei";
        MeiSequence Telemann = new MeiSequence(filenameTelemann);
        HashMap<Integer,MeiStaff> actualTelemann = Telemann.getStaffs();
        for(MeiStaff staff : actualTelemann.values()) {
                staff.setTick(0);
                staff.setTickLayer(0);
        }
        HashMap<Integer,MeiStaff> expectedTelemann = new HashMap<>();
        expectedTelemann.put(1, new MeiStaff(1, "Adagio", "Trumpet in C 1", "2s", "major", "6", "8"));
        expectedTelemann.put(2, new MeiStaff(2, "Adagio", "Trumpet in C 2", "2s", "major", "6", "8"));
        expectedTelemann.put(3, new MeiStaff(3, "Adagio", "Trumpet in C 3", "2s", "major", "6", "8"));
        expectedTelemann.put(4, new MeiStaff(4, "Adagio", "Timpani", "2s", "major", "6", "8"));
        expectedTelemann.put(5, new MeiStaff(5, "Adagio", "Organ", "2s", "major", "6", "8"));
        expectedTelemann.put(6, new MeiStaff(6, "Adagio", "Organ", "2s", "major", "6", "8"));
        assertEquals(expectedTelemann, actualTelemann);
    }
    
    /**
     * Test of createMeiStaff method, of class MeiSequence.
     * @throws InvalidMidiDataException 
     */
    @Test
    public void testCreateMeiStaff() throws InvalidMidiDataException {
        //Test 1 : treble-clef-out
        //No n attribute given in many of the mei-test-set files
        String filename = "mei-test/mei-test-set/clefs/treble-clef.mei";
        MeiSequence trebleclefout;
        try {
            trebleclefout = new MeiSequence(filename);
            for(MeiStaff staff : trebleclefout.getStaffs().values()) {
                staff.setTick(0);
                staff.setTickLayer(0);
            }
        }
        catch(InvalidMidiDataException imde) {
            throw new InvalidMidiDataException("createMeiStaff error");
        }
        MeiStaff actual = trebleclefout.getStaffs().get(1);
        MeiStaff expected = new MeiStaff(1, "default", "Voice", "0", "major", "4", "4");
        HashMap<Integer,MeiStaff> expectedMap = new HashMap<>();
        expectedMap.put(1, expected);
        assertEquals(expected,actual);
        assertEquals(expectedMap, trebleclefout.getStaffs());
    }
    
    /**
     * WARNING
     * TEST ONLY WORKS IF WE DO NOT PROCESS MIDI NOTES
     * COMMENT OUT LINE MIDINOTE GENERATORS TO SEE IF THIS WORKS
     * Test of buildMidiTrack method, of class MeiSequence.
     * Have to compare actual byte arrays separately because the track
     * equals method only compares the reference and not the actual value.
     */
    @Ignore
    @Test
    public void testBuildMidiTrack() throws InvalidMidiDataException {
        //Test for treble-clef-out
        String filename = "mei-test/mei-test-set/clefs/treble-clef.mei";
        MeiSequence trebleclefout = new MeiSequence(filename);
        Track[] actualtco = trebleclefout.getSequence().getTracks();
        Sequence sequence = new Sequence(Sequence.PPQ, 256);
        sequence.createTrack();
        Track[] expectedtco = sequence.getTracks();
        expectedtco[0].add(MidiBuildMessage.createKeySignature("0", "major", 0));
        expectedtco[0].add(MidiBuildMessage.createProgramChange(54, 0, 0));
        expectedtco[0].add(MidiBuildMessage.createTrackTempo(90, 0));
        expectedtco[0].add(new MidiEvent(new MetaMessage(0x2F, new byte[0], 0), 0));
        for(int x = 0; x < actualtco.length; x++) {
            for(int i = 0; i < actualtco[x].size(); i++) {
                byte[] actualbytes = actualtco[x].get(i).getMessage().getMessage();
                byte[] expectedbytes = expectedtco[x].get(i).getMessage().getMessage();
                assertArrayEquals(expectedbytes, actualbytes);
            }
        }
        for(int x = 0; x < expectedtco.length; x++) {
            for(int i = 0; i < expectedtco[x].size(); i++) {
                byte[] actualbytes = actualtco[x].get(i).getMessage().getMessage();
                byte[] expectedbytes = expectedtco[x].get(i).getMessage().getMessage();
                assertArrayEquals(expectedbytes, actualbytes);
            }
        }
        
        //Test Czerny
        String filenameCzerny = "mei-test/CompleteExamples/Czerny_op603_6.mei";
        MeiSequence Czerny = new MeiSequence(filenameCzerny);
        Track[] actualCzerny = Czerny.getSequence().getTracks();
        Sequence sequenceCzerny = new Sequence(Sequence.PPQ, 256,3);
        Track[] expectedCzerny = sequenceCzerny.getTracks();
        expectedCzerny[0].add(MidiBuildMessage.createKeySignature("1f", "minor", 0));
        expectedCzerny[0].add(MidiBuildMessage.createProgramChange(19, 0, 0));
        expectedCzerny[0].add(MidiBuildMessage.createTrackTempo(90, 0));
        expectedCzerny[0].add(new MidiEvent(new MetaMessage(0x2F, new byte[0], 0), 0));
        expectedCzerny[1].add(MidiBuildMessage.createKeySignature("1f", "minor", 0));
        expectedCzerny[1].add(MidiBuildMessage.createProgramChange(19, 0, 1));
        expectedCzerny[1].add(MidiBuildMessage.createTrackTempo(90, 0));
        expectedCzerny[1].add(new MidiEvent(new MetaMessage(0x2F, new byte[0], 0), 0));
        expectedCzerny[2].add(MidiBuildMessage.createKeySignature("1f", "minor", 0));
        expectedCzerny[2].add(MidiBuildMessage.createProgramChange(19, 0, 2));
        expectedCzerny[2].add(MidiBuildMessage.createTrackTempo(90, 0));
        expectedCzerny[2].add(new MidiEvent(new MetaMessage(0x2F, new byte[0], 0), 0));
        for(int x = 0; x < actualCzerny.length; x++) {
            for(int i = 0; i < actualCzerny[x].size(); i++) {
                byte[] actualbytes = actualCzerny[x].get(i).getMessage().getMessage();
                byte[] expectedbytes = expectedCzerny[x].get(i).getMessage().getMessage();
                assertArrayEquals(expectedbytes, actualbytes);
            }
        }
        for(int x = 0; x < expectedCzerny.length; x++) {
            for(int i = 0; i < expectedCzerny[x].size(); i++) {
                byte[] actualbytes = actualCzerny[x].get(i).getMessage().getMessage();
                byte[] expectedbytes = expectedCzerny[x].get(i).getMessage().getMessage();
                assertArrayEquals(expectedbytes, actualbytes);
            }
        }
        
        //Test Chopin Etude
        String filenameChopin = "mei-test/CompleteExamples/Chopin_Etude_op.10_no.9_2013.mei";
        MeiSequence Chopin = new MeiSequence(filenameChopin);
        Track[] actualChopin = Chopin.getSequence().getTracks();
        Sequence sequenceChopin = new Sequence(Sequence.PPQ, 256,2);
        Track[] expectedChopin = sequenceChopin.getTracks();
        expectedChopin[0].add(MidiBuildMessage.createKeySignature("4f", "minor", 0));
        expectedChopin[0].add(MidiBuildMessage.createProgramChange(0, 0, 0));
        expectedChopin[0].add(MidiBuildMessage.createTrackTempo(144, 0));
        expectedChopin[0].add(new MidiEvent(new MetaMessage(0x2F, new byte[0], 0), 0));
        expectedChopin[1].add(MidiBuildMessage.createKeySignature("4f", "minor", 0));
        expectedChopin[1].add(MidiBuildMessage.createProgramChange(0, 0, 1));
        expectedChopin[1].add(MidiBuildMessage.createTrackTempo(144, 0));
        expectedChopin[1].add(new MidiEvent(new MetaMessage(0x2F, new byte[0], 0), 0));
        for(int x = 0; x < actualChopin.length; x++) {
            for(int i = 0; i < actualChopin[x].size(); i++) {
                byte[] actualbytes = actualChopin[x].get(i).getMessage().getMessage();
                byte[] expectedbytes = expectedChopin[x].get(i).getMessage().getMessage();
                assertArrayEquals(expectedbytes, actualbytes);
            }
        }
        for(int x = 0; x < expectedChopin.length; x++) {
            for(int i = 0; i < expectedChopin[x].size(); i++) {
                byte[] actualbytes = actualChopin[x].get(i).getMessage().getMessage();
                byte[] expectedbytes = expectedChopin[x].get(i).getMessage().getMessage();
                assertArrayEquals(expectedbytes, actualbytes);
            }
        }
        
        //Test Joplin
        String filenameJoplin = "mei-test/CompleteExamples/Joplin_Elite_Syncopations.mei";
        MeiSequence Joplin = new MeiSequence(filenameJoplin);
        Track[] actualJoplin = Joplin.getSequence().getTracks();
        assertEquals(2, actualJoplin.length);
        Sequence sequenceJoplin = new Sequence(Sequence.PPQ, 256,2);
        Track[] expectedJoplin = sequenceJoplin.getTracks();
        expectedJoplin[0].add(MidiBuildMessage.createKeySignature("1f", "major", 0));
        expectedJoplin[0].add(MidiBuildMessage.createProgramChange(0, 0, 0));
        expectedJoplin[0].add(MidiBuildMessage.createTrackTempo(90, 0));
        expectedJoplin[0].add(new MidiEvent(new MetaMessage(0x2F, new byte[0], 0), 0));
        expectedJoplin[1].add(MidiBuildMessage.createKeySignature("1f", "major", 0));
        expectedJoplin[1].add(MidiBuildMessage.createProgramChange(0, 0, 1));
        expectedJoplin[1].add(MidiBuildMessage.createTrackTempo(90, 0));
        expectedJoplin[1].add(new MidiEvent(new MetaMessage(0x2F, new byte[0], 0), 0));
        expectedJoplin[0].add(MidiBuildMessage.createKeySignature("2f", "major", 0));
        expectedJoplin[0].add(MidiBuildMessage.createProgramChange(0, 0, 0));
        expectedJoplin[0].add(MidiBuildMessage.createTrackTempo(90, 0));
        expectedJoplin[0].add(new MidiEvent(new MetaMessage(0x2F, new byte[0], 0), 0));
        expectedJoplin[1].add(MidiBuildMessage.createKeySignature("2f", "major", 0));
        expectedJoplin[1].add(MidiBuildMessage.createProgramChange(0, 0, 1));
        expectedJoplin[1].add(MidiBuildMessage.createTrackTempo(90, 0));
        expectedJoplin[1].add(new MidiEvent(new MetaMessage(0x2F, new byte[0], 0), 0));
        for(int x = 0; x < actualJoplin.length; x++) {
            for(int i = 0; i < actualJoplin[x].size(); i++) {
                byte[] actualbytes = actualJoplin[x].get(i).getMessage().getMessage();
                byte[] expectedbytes = expectedJoplin[x].get(i).getMessage().getMessage();
                assertArrayEquals(expectedbytes, actualbytes);
            }
        }
        for(int x = 0; x < expectedJoplin.length; x++) {
            for(int i = 0; i < expectedJoplin[x].size(); i++) {
                byte[] actualbytes = actualJoplin[x].get(i).getMessage().getMessage();
                byte[] expectedbytes = expectedJoplin[x].get(i).getMessage().getMessage();
                assertArrayEquals(expectedbytes, actualbytes);
            }
        }
        
        //Test Beethoven - lots of scoreDefs and staffDefs
        //Did not test just looked at output
        //Strange: There are n=6 but creates for 5 and 6
        //however n=6 is completely unnecessary and random...
        //Maybe test later
        String filenameBeeth = "mei-test/CompleteExamples/Beethoven_op.18.mei";
        MeiSequence Beeth = new MeiSequence(filenameBeeth);
        Track[] actualBeeth = Beeth.getSequence().getTracks();
        assertEquals(6, actualBeeth.length);
        Sequence sequenceBeeth = new Sequence(Sequence.PPQ, 256,6);
        Track[] expectedBeeth = sequenceBeeth.getTracks();
        for(int x = 0; x < actualBeeth.length; x++) {
            for(int i = 0; i < actualBeeth[x].size(); i++) {
                byte[] actualbytes = actualBeeth[x].get(i).getMessage().getMessage();
                //System.out.println(Arrays.toString(actualbytes));
                //byte[] expectedbytes = expectedBeeth[x].get(i).getMessage().getMessage();
                //assertArrayEquals(expectedbytes, actualbytes);
            }
        }
        
        //Test JSB_2
        String filenameJSB = "mei-test/CompleteExamples/JSB_BWV1047_2.mei";
        MeiSequence JSB = new MeiSequence(filenameJSB);
        Track[] actualJSB = JSB.getSequence().getTracks();
        assertEquals(4, actualJSB.length);
        Sequence sequenceJSB = new Sequence(Sequence.PPQ, 256,4);
        Track[] expectedJSB = sequenceJSB.getTracks();
        expectedJSB[0].add(MidiBuildMessage.createKeySignature("1f", "major", 0));
        expectedJSB[0].add(MidiBuildMessage.createProgramChange(73, 0, 0));
        expectedJSB[0].add(MidiBuildMessage.createTrackTempo(92, 0));
        expectedJSB[0].add(new MidiEvent(new MetaMessage(0x2F, new byte[0], 0), 0));
        expectedJSB[1].add(MidiBuildMessage.createKeySignature("1f", "major", 0));
        expectedJSB[1].add(MidiBuildMessage.createProgramChange(68, 0, 1));
        expectedJSB[1].add(MidiBuildMessage.createTrackTempo(92, 0));
        expectedJSB[1].add(new MidiEvent(new MetaMessage(0x2F, new byte[0], 0), 0));
        expectedJSB[2].add(MidiBuildMessage.createKeySignature("1f", "major", 0));
        expectedJSB[2].add(MidiBuildMessage.createProgramChange(40, 0, 2));
        expectedJSB[2].add(MidiBuildMessage.createTrackTempo(92, 0));
        expectedJSB[2].add(new MidiEvent(new MetaMessage(0x2F, new byte[0], 0), 0));
        expectedJSB[3].add(MidiBuildMessage.createKeySignature("1f", "major", 0));
        expectedJSB[3].add(MidiBuildMessage.createProgramChange(42, 0, 3));
        expectedJSB[3].add(MidiBuildMessage.createTrackTempo(92, 0));
        expectedJSB[3].add(new MidiEvent(new MetaMessage(0x2F, new byte[0], 0), 0));
        for(int x = 0; x < actualJSB.length; x++) {
            for(int i = 0; i < actualJSB[x].size(); i++) {
                byte[] actualbytes = actualJSB[x].get(i).getMessage().getMessage();
                byte[] expectedbytes = expectedJSB[x].get(i).getMessage().getMessage();
                assertArrayEquals(expectedbytes, actualbytes);
            }
        }
        for(int x = 0; x < expectedJSB.length; x++) {
            for(int i = 0; i < expectedJSB[x].size(); i++) {
                byte[] actualbytes = actualJSB[x].get(i).getMessage().getMessage();
                byte[] expectedbytes = expectedJSB[x].get(i).getMessage().getMessage();
                assertArrayEquals(expectedbytes, actualbytes);
            }
        }
        
        //Test Vivaldi
        //Nasty bug: Violoncello and violencello mistake for staff 5
        String filenameViv = "mei-test/CompleteExamples/Vivaldi_Op8_No.2.mei";
        MeiSequence Viv = new MeiSequence(filenameViv);
        Track[] actualViv = Viv.getSequence().getTracks();
        Sequence sequenceViv = new Sequence(Sequence.PPQ, 256,5);
        Track[] expectedViv = sequenceViv.getTracks();
        expectedViv[0].add(MidiBuildMessage.createKeySignature("2f", "minor", 0));
        expectedViv[0].add(MidiBuildMessage.createProgramChange(40, 0, 0));
        expectedViv[0].add(MidiBuildMessage.createTrackTempo(144, 0));
        
        expectedViv[0].add(MidiBuildMessage.createKeySignature("2f", "minor", 0));
        expectedViv[0].add(MidiBuildMessage.createProgramChange(40, 0, 0));
        expectedViv[0].add(MidiBuildMessage.createTrackTempo(144, 0));
        
        expectedViv[0].add(MidiBuildMessage.createKeySignature("2f", "minor", 0));
        expectedViv[0].add(MidiBuildMessage.createProgramChange(40, 0, 0));
        expectedViv[0].add(MidiBuildMessage.createTrackTempo(144, 0));
        
        expectedViv[0].add(MidiBuildMessage.createKeySignature("2f", "minor", 0));
        expectedViv[0].add(MidiBuildMessage.createProgramChange(40, 0, 0));
        expectedViv[0].add(MidiBuildMessage.createTrackTempo(71, 0));
        
        expectedViv[0].add(MidiBuildMessage.createKeySignature("2f", "minor", 0));
        expectedViv[0].add(MidiBuildMessage.createProgramChange(40, 0, 0));
        expectedViv[0].add(MidiBuildMessage.createTrackTempo(184, 0));
        expectedViv[0].add(new MidiEvent(new MetaMessage(0x2F, new byte[0], 0), 0));
        
        expectedViv[1].add(MidiBuildMessage.createKeySignature("2f", "minor", 0));
        expectedViv[1].add(MidiBuildMessage.createProgramChange(40, 0, 1));
        expectedViv[1].add(MidiBuildMessage.createTrackTempo(144, 0));
        
        expectedViv[1].add(MidiBuildMessage.createKeySignature("2f", "minor", 0));
        expectedViv[1].add(MidiBuildMessage.createProgramChange(40, 0, 1));
        expectedViv[1].add(MidiBuildMessage.createTrackTempo(144, 0));
        
        expectedViv[1].add(MidiBuildMessage.createKeySignature("2f", "minor", 0));
        expectedViv[1].add(MidiBuildMessage.createProgramChange(40, 0, 1));
        expectedViv[1].add(MidiBuildMessage.createTrackTempo(144, 0));
        
        expectedViv[1].add(MidiBuildMessage.createKeySignature("2f", "minor", 0));
        expectedViv[1].add(MidiBuildMessage.createProgramChange(40, 0, 1));
        expectedViv[1].add(MidiBuildMessage.createTrackTempo(71, 0));
        
        expectedViv[1].add(MidiBuildMessage.createKeySignature("2f", "minor", 0));
        expectedViv[1].add(MidiBuildMessage.createProgramChange(40, 0, 1));
        expectedViv[1].add(MidiBuildMessage.createTrackTempo(184, 0));
        expectedViv[1].add(new MidiEvent(new MetaMessage(0x2F, new byte[0], 0), 0));
        
        expectedViv[2].add(MidiBuildMessage.createKeySignature("2f", "minor", 0));
        expectedViv[2].add(MidiBuildMessage.createProgramChange(40, 0, 2));
        expectedViv[2].add(MidiBuildMessage.createTrackTempo(144, 0));
        
        expectedViv[2].add(MidiBuildMessage.createKeySignature("2f", "minor", 0));
        expectedViv[2].add(MidiBuildMessage.createProgramChange(40, 0, 2));
        expectedViv[2].add(MidiBuildMessage.createTrackTempo(144, 0));
        
        expectedViv[2].add(MidiBuildMessage.createKeySignature("2f", "minor", 0));
        expectedViv[2].add(MidiBuildMessage.createProgramChange(40, 0, 2));
        expectedViv[2].add(MidiBuildMessage.createTrackTempo(144, 0));
        
        expectedViv[2].add(MidiBuildMessage.createKeySignature("2f", "minor", 0));
        expectedViv[2].add(MidiBuildMessage.createProgramChange(40, 0, 2));
        expectedViv[2].add(MidiBuildMessage.createTrackTempo(71, 0));
        
        expectedViv[2].add(MidiBuildMessage.createKeySignature("2f", "minor", 0));
        expectedViv[2].add(MidiBuildMessage.createProgramChange(40, 0, 2));
        expectedViv[2].add(MidiBuildMessage.createTrackTempo(184, 0));
        expectedViv[2].add(new MidiEvent(new MetaMessage(0x2F, new byte[0], 0), 0));
        
        expectedViv[3].add(MidiBuildMessage.createKeySignature("2f", "minor", 0));
        expectedViv[3].add(MidiBuildMessage.createProgramChange(41, 0, 3));
        expectedViv[3].add(MidiBuildMessage.createTrackTempo(144, 0));
        
        expectedViv[3].add(MidiBuildMessage.createKeySignature("2f", "minor", 0));
        expectedViv[3].add(MidiBuildMessage.createProgramChange(41, 0, 3));
        expectedViv[3].add(MidiBuildMessage.createTrackTempo(144, 0));
        
        expectedViv[3].add(MidiBuildMessage.createKeySignature("2f", "minor", 0));
        expectedViv[3].add(MidiBuildMessage.createProgramChange(41, 0, 3));
        expectedViv[3].add(MidiBuildMessage.createTrackTempo(144, 0));
        
        expectedViv[3].add(MidiBuildMessage.createKeySignature("2f", "minor", 0));
        expectedViv[3].add(MidiBuildMessage.createProgramChange(41, 0, 3));
        expectedViv[3].add(MidiBuildMessage.createTrackTempo(71, 0));
        
        expectedViv[3].add(MidiBuildMessage.createKeySignature("2f", "minor", 0));
        expectedViv[3].add(MidiBuildMessage.createProgramChange(41, 0, 3));
        expectedViv[3].add(MidiBuildMessage.createTrackTempo(184, 0));
        expectedViv[3].add(new MidiEvent(new MetaMessage(0x2F, new byte[0], 0), 0));
        
        expectedViv[4].add(MidiBuildMessage.createKeySignature("2f", "minor", 0));
        expectedViv[4].add(MidiBuildMessage.createProgramChange(42, 0, 4));
        expectedViv[4].add(MidiBuildMessage.createTrackTempo(144, 0));
        
        expectedViv[4].add(MidiBuildMessage.createKeySignature("2f", "minor", 0));
        expectedViv[4].add(MidiBuildMessage.createProgramChange(42, 0, 4));
        expectedViv[4].add(MidiBuildMessage.createTrackTempo(144, 0));
        
        expectedViv[4].add(MidiBuildMessage.createKeySignature("2f", "minor", 0));
        expectedViv[4].add(MidiBuildMessage.createProgramChange(42, 0, 4));
        expectedViv[4].add(MidiBuildMessage.createTrackTempo(144, 0));
        
        //Doubles occur here because of violencello and so code does not think
        //that it is the same. Could try with contain maybe
        expectedViv[4].add(MidiBuildMessage.createKeySignature("2f", "minor", 0));
        expectedViv[4].add(MidiBuildMessage.createProgramChange(42, 0, 4));
        expectedViv[4].add(MidiBuildMessage.createTrackTempo(71, 0));
        
        expectedViv[4].add(MidiBuildMessage.createKeySignature("2f", "minor", 0));
        expectedViv[4].add(MidiBuildMessage.createProgramChange(42, 0, 4));
        expectedViv[4].add(MidiBuildMessage.createTrackTempo(71, 0));
        
        expectedViv[4].add(MidiBuildMessage.createKeySignature("2f", "minor", 0));
        expectedViv[4].add(MidiBuildMessage.createProgramChange(42, 0, 4));
        expectedViv[4].add(MidiBuildMessage.createTrackTempo(184, 0));
        
        expectedViv[4].add(MidiBuildMessage.createKeySignature("2f", "minor", 0));
        expectedViv[4].add(MidiBuildMessage.createProgramChange(42, 0, 4));
        expectedViv[4].add(MidiBuildMessage.createTrackTempo(184, 0));
        expectedViv[4].add(new MidiEvent(new MetaMessage(0x2F, new byte[0], 0), 0));
        for(int x = 0; x < expectedViv.length; x++) {
            for(int i = 0; i < expectedViv[x].size(); i++) {
                byte[] actualbytes = actualViv[x].get(i).getMessage().getMessage();
                byte[] expectedbytes = expectedViv[x].get(i).getMessage().getMessage();
                assertArrayEquals(expectedbytes, actualbytes);
            }
        }
        for(int x = 0; x < actualViv.length; x++) {
            for(int i = 0; i < actualViv[x].size(); i++) {
                byte[] actualbytes = actualViv[x].get(i).getMessage().getMessage();
                byte[] expectedbytes = expectedViv[x].get(i).getMessage().getMessage();
                assertArrayEquals(expectedbytes, actualbytes);
            }
        }
        
        //Test Gluck
        String filenameGluck = "mei-test/CompleteExamples/Gluck_CheFaroSenzaEuridice.mei";
        MeiSequence Gluck = new MeiSequence(filenameGluck);
        Track[] actualGluck = Gluck.getSequence().getTracks();
        Sequence sequenceGluck = new Sequence(Sequence.PPQ, 256,7);
        Track[] expectedGluck = sequenceGluck.getTracks();
        expectedGluck[0].add(MidiBuildMessage.createKeySignature("0", "major", 0));
        expectedGluck[0].add(MidiBuildMessage.createProgramChange(54, 0, 0));
        expectedGluck[0].add(MidiBuildMessage.createTrackTempo(92, 0));
        expectedGluck[0].add(new MidiEvent(new MetaMessage(0x2F, new byte[0], 0), 0));
        
        expectedGluck[1].add(MidiBuildMessage.createKeySignature("0", "major", 0));
        expectedGluck[1].add(MidiBuildMessage.createProgramChange(40, 0, 1));
        expectedGluck[1].add(MidiBuildMessage.createTrackTempo(92, 0));
        expectedGluck[1].add(new MidiEvent(new MetaMessage(0x2F, new byte[0], 0), 0));
        
        expectedGluck[2].add(MidiBuildMessage.createKeySignature("0", "major", 0));
        expectedGluck[2].add(MidiBuildMessage.createProgramChange(40, 0, 2));
        expectedGluck[2].add(MidiBuildMessage.createTrackTempo(92, 0));
        expectedGluck[2].add(new MidiEvent(new MetaMessage(0x2F, new byte[0], 0), 0));
        
        expectedGluck[3].add(MidiBuildMessage.createKeySignature("0", "major", 0));
        expectedGluck[3].add(MidiBuildMessage.createProgramChange(41, 0, 3));
        expectedGluck[3].add(MidiBuildMessage.createTrackTempo(92, 0));
        expectedGluck[3].add(new MidiEvent(new MetaMessage(0x2F, new byte[0], 0), 0));
        
        expectedGluck[4].add(MidiBuildMessage.createKeySignature("0", "major", 0));
        expectedGluck[4].add(MidiBuildMessage.createProgramChange(6, 0, 4));
        expectedGluck[4].add(MidiBuildMessage.createTrackTempo(92, 0));
        expectedGluck[4].add(new MidiEvent(new MetaMessage(0x2F, new byte[0], 0), 0));
        
        expectedGluck[5].add(MidiBuildMessage.createKeySignature("0", "major", 0));
        expectedGluck[5].add(MidiBuildMessage.createProgramChange(6, 0, 5));
        expectedGluck[5].add(MidiBuildMessage.createTrackTempo(92, 0));
        expectedGluck[5].add(new MidiEvent(new MetaMessage(0x2F, new byte[0], 0), 0));
        
        expectedGluck[6].add(MidiBuildMessage.createKeySignature("0", "major", 0));
        expectedGluck[6].add(MidiBuildMessage.createProgramChange(42, 0, 6));
        expectedGluck[6].add(MidiBuildMessage.createTrackTempo(92, 0));
        expectedGluck[6].add(new MidiEvent(new MetaMessage(0x2F, new byte[0], 0), 0));
        for(int x = 0; x < expectedGluck.length; x++) {
            for(int i = 0; i < expectedGluck[x].size(); i++) {
                byte[] actualbytes = actualGluck[x].get(i).getMessage().getMessage();
                byte[] expectedbytes = expectedGluck[x].get(i).getMessage().getMessage();
                assertArrayEquals(expectedbytes, actualbytes);
            }
        }
        for(int x = 0; x < actualGluck.length; x++) {
            for(int i = 0; i < actualGluck[x].size(); i++) {
                byte[] actualbytes = actualGluck[x].get(i).getMessage().getMessage();
                byte[] expectedbytes = expectedGluck[x].get(i).getMessage().getMessage();
                assertArrayEquals(expectedbytes, actualbytes);
            }
        }
        
        //Test Beethoven Hymn to Joy
        //Very good, the extra staffDefs were skipped due to optimizations
        String filenameBHJ = "mei-test/CompleteExamples/Beethoven_Hymn_to_joy.mei";
        MeiSequence BHJ = new MeiSequence(filenameBHJ);
        Track[] actualBHJ = BHJ.getSequence().getTracks();
        Sequence sequenceBHJ = new Sequence(Sequence.PPQ, 256,24);
        Track[] expectedBHJ = sequenceBHJ.getTracks();
        //Soprano
        expectedBHJ[0].add(MidiBuildMessage.createKeySignature("2s", "major", 0));
        expectedBHJ[0].add(MidiBuildMessage.createProgramChange(54, 0, 0));
        expectedBHJ[0].add(MidiBuildMessage.createTrackTempo(144, 0));
        expectedBHJ[0].add(new MidiEvent(new MetaMessage(0x2F, new byte[0], 0), 0));
        //Alto
        expectedBHJ[1].add(MidiBuildMessage.createKeySignature("2s", "major", 0));
        expectedBHJ[1].add(MidiBuildMessage.createProgramChange(54, 0, 1));
        expectedBHJ[1].add(MidiBuildMessage.createTrackTempo(144, 0));
        expectedBHJ[1].add(new MidiEvent(new MetaMessage(0x2F, new byte[0], 0), 0));
        //Tenor
        expectedBHJ[2].add(MidiBuildMessage.createKeySignature("2s", "major", 0));
        expectedBHJ[2].add(MidiBuildMessage.createProgramChange(54, 0, 2));
        expectedBHJ[2].add(MidiBuildMessage.createTrackTempo(144, 0));
        expectedBHJ[2].add(new MidiEvent(new MetaMessage(0x2F, new byte[0], 0), 0));
        //Baritone
        expectedBHJ[3].add(MidiBuildMessage.createKeySignature("2s", "major", 0));
        expectedBHJ[3].add(MidiBuildMessage.createProgramChange(54, 0, 3));
        expectedBHJ[3].add(MidiBuildMessage.createTrackTempo(144, 0));
        expectedBHJ[3].add(new MidiEvent(new MetaMessage(0x2F, new byte[0], 0), 0));
        //Bass
        expectedBHJ[4].add(MidiBuildMessage.createKeySignature("2s", "major", 0));
        expectedBHJ[4].add(MidiBuildMessage.createProgramChange(54, 0, 4));
        expectedBHJ[4].add(MidiBuildMessage.createTrackTempo(144, 0));
        expectedBHJ[4].add(new MidiEvent(new MetaMessage(0x2F, new byte[0], 0), 0));
        //Piccolo
        expectedBHJ[5].add(MidiBuildMessage.createKeySignature("2s", "major", 0));
        expectedBHJ[5].add(MidiBuildMessage.createProgramChange(72, 0, 5));
        expectedBHJ[5].add(MidiBuildMessage.createTrackTempo(144, 0));
        expectedBHJ[5].add(new MidiEvent(new MetaMessage(0x2F, new byte[0], 0), 0));
        //Flute
        expectedBHJ[6].add(MidiBuildMessage.createKeySignature("2s", "major", 0));
        expectedBHJ[6].add(MidiBuildMessage.createProgramChange(73, 0, 6));
        expectedBHJ[6].add(MidiBuildMessage.createTrackTempo(144, 0));
        expectedBHJ[6].add(new MidiEvent(new MetaMessage(0x2F, new byte[0], 0), 0));
        //Oboe
        expectedBHJ[7].add(MidiBuildMessage.createKeySignature("2s", "major", 0));
        expectedBHJ[7].add(MidiBuildMessage.createProgramChange(68, 0, 7));
        expectedBHJ[7].add(MidiBuildMessage.createTrackTempo(144, 0));
        expectedBHJ[7].add(new MidiEvent(new MetaMessage(0x2F, new byte[0], 0), 0));
        //Bassoon
        expectedBHJ[8].add(MidiBuildMessage.createKeySignature("2s", "major", 0));
        expectedBHJ[8].add(MidiBuildMessage.createProgramChange(70, 0, 8));
        expectedBHJ[8].add(MidiBuildMessage.createTrackTempo(144, 0));
        expectedBHJ[8].add(new MidiEvent(new MetaMessage(0x2F, new byte[0], 0), 0));
        //Clarinet in B
        expectedBHJ[9].add(MidiBuildMessage.createKeySignature("4s", "major", 0));
        expectedBHJ[9].add(MidiBuildMessage.createProgramChange(71, 0, 10));
        expectedBHJ[9].add(MidiBuildMessage.createTrackTempo(144, 0));
        expectedBHJ[9].add(new MidiEvent(new MetaMessage(0x2F, new byte[0], 0), 0));
        //Bass Clarinet
        expectedBHJ[10].add(MidiBuildMessage.createKeySignature("4s", "major", 0));
        expectedBHJ[10].add(MidiBuildMessage.createProgramChange(71, 0, 11));
        expectedBHJ[10].add(MidiBuildMessage.createTrackTempo(144, 0));
        expectedBHJ[10].add(new MidiEvent(new MetaMessage(0x2F, new byte[0], 0), 0));
        //Alto sax
        expectedBHJ[11].add(MidiBuildMessage.createKeySignature("5s", "major", 0));
        expectedBHJ[11].add(MidiBuildMessage.createProgramChange(65, 0, 12));
        expectedBHJ[11].add(MidiBuildMessage.createTrackTempo(144, 0));
        expectedBHJ[11].add(new MidiEvent(new MetaMessage(0x2F, new byte[0], 0), 0));
        //Tenor sax
        expectedBHJ[12].add(MidiBuildMessage.createKeySignature("4s", "major", 0));
        expectedBHJ[12].add(MidiBuildMessage.createProgramChange(66, 0, 13));
        expectedBHJ[12].add(MidiBuildMessage.createTrackTempo(144, 0));
        expectedBHJ[12].add(new MidiEvent(new MetaMessage(0x2F, new byte[0], 0), 0));
        //Baritone sax
        expectedBHJ[13].add(MidiBuildMessage.createKeySignature("5s", "major", 0));
        expectedBHJ[13].add(MidiBuildMessage.createProgramChange(67, 0, 14));
        expectedBHJ[13].add(MidiBuildMessage.createTrackTempo(144, 0));
        expectedBHJ[13].add(new MidiEvent(new MetaMessage(0x2F, new byte[0], 0), 0));
        //Trumpet in B
        expectedBHJ[14].add(MidiBuildMessage.createKeySignature("4s", "major", 0));
        expectedBHJ[14].add(MidiBuildMessage.createProgramChange(56, 0, 15));
        expectedBHJ[14].add(MidiBuildMessage.createTrackTempo(144, 0));
        expectedBHJ[14].add(new MidiEvent(new MetaMessage(0x2F, new byte[0], 0), 0));
        //Horn in F 1
        expectedBHJ[15].add(MidiBuildMessage.createKeySignature("3s", "major", 0));
        expectedBHJ[15].add(MidiBuildMessage.createProgramChange(69, 0, 15));
        expectedBHJ[15].add(MidiBuildMessage.createTrackTempo(144, 0));
        expectedBHJ[15].add(new MidiEvent(new MetaMessage(0x2F, new byte[0], 0), 0));
        //Trombone
        expectedBHJ[16].add(MidiBuildMessage.createKeySignature("2s", "major", 0));
        expectedBHJ[16].add(MidiBuildMessage.createProgramChange(57, 0, 15));
        expectedBHJ[16].add(MidiBuildMessage.createTrackTempo(144, 0));
        expectedBHJ[16].add(new MidiEvent(new MetaMessage(0x2F, new byte[0], 0), 0));
        //Bass Trombone
        expectedBHJ[17].add(MidiBuildMessage.createKeySignature("2s", "major", 0));
        expectedBHJ[17].add(MidiBuildMessage.createProgramChange(57, 0, 15));
        expectedBHJ[17].add(MidiBuildMessage.createTrackTempo(144, 0));
        expectedBHJ[17].add(new MidiEvent(new MetaMessage(0x2F, new byte[0], 0), 0));
        //Euphonium
        expectedBHJ[18].add(MidiBuildMessage.createKeySignature("2s", "major", 0));
        expectedBHJ[18].add(MidiBuildMessage.createProgramChange(58, 0, 15));
        expectedBHJ[18].add(MidiBuildMessage.createTrackTempo(144, 0));
        expectedBHJ[18].add(new MidiEvent(new MetaMessage(0x2F, new byte[0], 0), 0));
        //Tuba
        expectedBHJ[19].add(MidiBuildMessage.createKeySignature("2s", "major", 0));
        expectedBHJ[19].add(MidiBuildMessage.createProgramChange(58, 0, 15));
        expectedBHJ[19].add(MidiBuildMessage.createTrackTempo(144, 0));
        expectedBHJ[19].add(new MidiEvent(new MetaMessage(0x2F, new byte[0], 0), 0));
        //Timpani
        expectedBHJ[20].add(MidiBuildMessage.createKeySignature("2s", "major", 0));
        expectedBHJ[20].add(MidiBuildMessage.createProgramChange(49, 0, 15));
        expectedBHJ[20].add(MidiBuildMessage.createTrackTempo(144, 0));
        expectedBHJ[20].add(new MidiEvent(new MetaMessage(0x2F, new byte[0], 0), 0));
        //Tubular Bells
        expectedBHJ[21].add(MidiBuildMessage.createKeySignature("2s", "major", 0));
        expectedBHJ[21].add(MidiBuildMessage.createProgramChange(16, 0, 15));
        expectedBHJ[21].add(MidiBuildMessage.createTrackTempo(144, 0));
        expectedBHJ[21].add(new MidiEvent(new MetaMessage(0x2F, new byte[0], 0), 0));
        //Glockenspiel
        expectedBHJ[22].add(MidiBuildMessage.createKeySignature("2s", "major", 0));
        expectedBHJ[22].add(MidiBuildMessage.createProgramChange(11, 0, 15));
        expectedBHJ[22].add(MidiBuildMessage.createTrackTempo(144, 0));
        expectedBHJ[22].add(new MidiEvent(new MetaMessage(0x2F, new byte[0], 0), 0));
        //Snare
        expectedBHJ[23].add(MidiBuildMessage.createKeySignature("2s", "major", 0));
        expectedBHJ[23].add(MidiBuildMessage.createProgramChange(38, 0, 9));
        expectedBHJ[23].add(MidiBuildMessage.createTrackTempo(144, 0));
        expectedBHJ[23].add(new MidiEvent(new MetaMessage(0x2F, new byte[0], 0), 0));
        for(int x = 0; x < actualBHJ.length; x++) {
            for(int i = 0; i < actualBHJ[x].size(); i++) {
                byte[] actualbytes = actualBHJ[x].get(i).getMessage().getMessage();
                byte[] expectedbytes = expectedBHJ[x].get(i).getMessage().getMessage();
                assertArrayEquals(expectedbytes, actualbytes);
            }
        }
        for(int x = 0; x < expectedBHJ.length; x++) {
            for(int i = 0; i < expectedBHJ[x].size(); i++) {
                byte[] actualbytes = actualBHJ[x].get(i).getMessage().getMessage();
                byte[] expectedbytes = expectedBHJ[x].get(i).getMessage().getMessage();
                assertArrayEquals(expectedbytes, actualbytes);
            }
        }
    }

    /**
     * Test of processNote method, of class MeiSequence.
     */
    @Test
    public void testProcessTrack() throws InvalidMidiDataException {
        //Test for beam-placement-out
        String filenameBeamP = "mei-test/mei-test-set/beams-and-stems/beam-placement.mei";
        MeiSequence beamP = new MeiSequence(filenameBeamP);
        Track[] actualbeamP = beamP.getSequence().getTracks();
        Sequence sequenceBeamP = new Sequence(Sequence.PPQ, 256, 1);
        Track[] expectedbeamP = sequenceBeamP.getTracks();
        expectedbeamP[0].add(MidiBuildMessage.createKeySignature("0", "major", 0));
        expectedbeamP[0].add(MidiBuildMessage.createProgramChange(54, 0, 0));
        expectedbeamP[0].add(MidiBuildMessage.createTrackTempo(90, 0));
        
        int beam1note1 = ConvertToMidi.NoteToMidi("e", "6", null);
        expectedbeamP[0].add(MidiBuildMessage.createNoteOnEvent(beam1note1, 0, 0));
        expectedbeamP[0].add(MidiBuildMessage.createNoteOffEvent(beam1note1, 128, 0));

        int beam1note2 = ConvertToMidi.NoteToMidi("d", "6", null);
        expectedbeamP[0].add(MidiBuildMessage.createNoteOnEvent(beam1note2, 128, 0));
        expectedbeamP[0].add(MidiBuildMessage.createNoteOffEvent(beam1note2, 256, 0));
        
        int beam1note3 = ConvertToMidi.NoteToMidi("b", "5", null);
        expectedbeamP[0].add(MidiBuildMessage.createNoteOnEvent(beam1note3, 256, 0));
        expectedbeamP[0].add(MidiBuildMessage.createNoteOffEvent(beam1note3, 384, 0));
        
        int beam1note4 = ConvertToMidi.NoteToMidi("e", "5", null);
        expectedbeamP[0].add(MidiBuildMessage.createNoteOnEvent(beam1note4, 384, 0));
        expectedbeamP[0].add(MidiBuildMessage.createNoteOffEvent(beam1note4, 512, 0));
        
        int beam1note5 = ConvertToMidi.NoteToMidi("g", "5", null);
        expectedbeamP[0].add(MidiBuildMessage.createNoteOnEvent(beam1note5, 512, 0));
        expectedbeamP[0].add(MidiBuildMessage.createNoteOffEvent(beam1note5, 640, 0));
        
        int beam1note6 = ConvertToMidi.NoteToMidi("g", "4", null);
        expectedbeamP[0].add(MidiBuildMessage.createNoteOnEvent(beam1note6, 640, 0));
        expectedbeamP[0].add(MidiBuildMessage.createNoteOffEvent(beam1note6, 768, 0));
        
        int beam2note1 = ConvertToMidi.NoteToMidi("g", "3", null);
        expectedbeamP[0].add(MidiBuildMessage.createNoteOnEvent(beam2note1, 768, 0));
        expectedbeamP[0].add(MidiBuildMessage.createNoteOffEvent(beam2note1, 896, 0));
        
        int beam2note2 = ConvertToMidi.NoteToMidi("c", "4", null);
        expectedbeamP[0].add(MidiBuildMessage.createNoteOnEvent(beam2note2, 896, 0));
        expectedbeamP[0].add(MidiBuildMessage.createNoteOffEvent(beam2note2, 1024, 0));
        
        int beam2note3 = ConvertToMidi.NoteToMidi("d", "4", null);
        expectedbeamP[0].add(MidiBuildMessage.createNoteOnEvent(beam2note3, 1024, 0));
        expectedbeamP[0].add(MidiBuildMessage.createNoteOffEvent(beam2note3, 1152, 0));
        
        int beam2note4 = ConvertToMidi.NoteToMidi("a", "4", null);
        expectedbeamP[0].add(MidiBuildMessage.createNoteOnEvent(beam2note4, 1152, 0));
        expectedbeamP[0].add(MidiBuildMessage.createNoteOffEvent(beam2note4, 1280, 0));
        
        int beam2note5 = ConvertToMidi.NoteToMidi("f", "4", null);
        expectedbeamP[0].add(MidiBuildMessage.createNoteOnEvent(beam2note5, 1280, 0));
        expectedbeamP[0].add(MidiBuildMessage.createNoteOffEvent(beam2note5, 1408, 0));
        
        int beam2note6 = ConvertToMidi.NoteToMidi("f", "5", null);
        expectedbeamP[0].add(MidiBuildMessage.createNoteOnEvent(beam2note6, 1408, 0));
        expectedbeamP[0].add(MidiBuildMessage.createNoteOffEvent(beam2note6, 1536, 0));
        expectedbeamP[0].add(new MidiEvent(new MetaMessage(0x2F, new byte[0], 0), 0));
        for(int x = 0; x < actualbeamP.length; x++) {
            for(int i = 0; i < actualbeamP[x].size(); i++) {
                byte[] actualbytes = actualbeamP[x].get(i).getMessage().getMessage();
                byte[] expectedbytes = expectedbeamP[x].get(i).getMessage().getMessage();
                assertArrayEquals(expectedbytes, actualbytes);
                assertEquals(expectedbeamP[x].get(i).getTick(), actualbeamP[x].get(i).getTick());
            }
        }
        for(int x = 0; x < expectedbeamP.length; x++) {
            for(int i = 0; i < expectedbeamP[x].size(); i++) {
                byte[] actualbytes = actualbeamP[x].get(i).getMessage().getMessage();
                byte[] expectedbytes = expectedbeamP[x].get(i).getMessage().getMessage();
                assertArrayEquals(expectedbytes, actualbytes);
                assertEquals(expectedbeamP[x].get(i).getTick(), actualbeamP[x].get(i).getTick());
            }
        }   
        
        //Test for dotted-4
        String filenameDotted4 = "mei-test/mei-test-set/chords/dotted-4.mei";
        MeiSequence Dotted4 = new MeiSequence(filenameDotted4);
        Track[] actualDotted4 = Dotted4.getSequence().getTracks();
        Sequence sequenceDotted4 = new Sequence(Sequence.PPQ, 256, 1);
        Track[] expectedDotted4 = sequenceDotted4.getTracks();
        expectedDotted4[0].add(MidiBuildMessage.createKeySignature("0", "major", 0));
        expectedDotted4[0].add(MidiBuildMessage.createProgramChange(54, 0, 0));
        expectedDotted4[0].add(MidiBuildMessage.createTrackTempo(90, 0));
        
        int dotted4m1n1 = ConvertToMidi.NoteToMidi("d", "5", null);
        expectedDotted4[0].add(MidiBuildMessage.createNoteOnEvent(dotted4m1n1, 0, 0));
        expectedDotted4[0].add(MidiBuildMessage.createNoteOffEvent(dotted4m1n1, 768, 0));
        
        int dotted4m1n2 = ConvertToMidi.NoteToMidi("c", "5", null);
        expectedDotted4[0].add(MidiBuildMessage.createNoteOnEvent(dotted4m1n2, 0, 0));
        expectedDotted4[0].add(MidiBuildMessage.createNoteOffEvent(dotted4m1n2, 512, 0));
        
        int dotted4m1n3 = ConvertToMidi.NoteToMidi("a", "4", null);
        expectedDotted4[0].add(MidiBuildMessage.createNoteOnEvent(dotted4m1n3, 512, 0));
        expectedDotted4[0].add(MidiBuildMessage.createNoteOffEvent(dotted4m1n3, 768, 0));
        
        int dotted4m2n1 = ConvertToMidi.NoteToMidi("c", "5", null);
        expectedDotted4[0].add(MidiBuildMessage.createNoteOnEvent(dotted4m2n1, 768, 0));
        expectedDotted4[0].add(MidiBuildMessage.createNoteOffEvent(dotted4m2n1, 1536, 0));
        
        int dotted4m2n2 = ConvertToMidi.NoteToMidi("b", "4", null);
        expectedDotted4[0].add(MidiBuildMessage.createNoteOnEvent(dotted4m2n2, 768, 0));
        expectedDotted4[0].add(MidiBuildMessage.createNoteOffEvent(dotted4m2n2, 1280, 0));
        
        int dotted4m2n3 = ConvertToMidi.NoteToMidi("g", "4", null);
        expectedDotted4[0].add(MidiBuildMessage.createNoteOnEvent(dotted4m2n3, 1280, 0));
        expectedDotted4[0].add(MidiBuildMessage.createNoteOffEvent(dotted4m2n3, 1536, 0));
        expectedDotted4[0].add(new MidiEvent(new MetaMessage(0x2F, new byte[0], 0), 0));
        for(int x = 0; x < actualDotted4.length; x++) {
            for(int i = 0; i < actualDotted4[x].size(); i++) {
                byte[] actualbytes = actualDotted4[x].get(i).getMessage().getMessage();
                byte[] expectedbytes = expectedDotted4[x].get(i).getMessage().getMessage();
                assertArrayEquals(expectedbytes, actualbytes);
                assertEquals(expectedDotted4[x].get(i).getTick(), actualDotted4[x].get(i).getTick());
            }
        }
        for(int x = 0; x < expectedDotted4.length; x++) {
            for(int i = 0; i < expectedDotted4[x].size(); i++) {
                byte[] actualbytes = actualDotted4[x].get(i).getMessage().getMessage();
                byte[] expectedbytes = expectedDotted4[x].get(i).getMessage().getMessage();
                assertArrayEquals(expectedbytes, actualbytes);
                assertEquals(expectedDotted4[x].get(i).getTick(), actualDotted4[x].get(i).getTick());
            }
        }
        
        //Test for slurs with rests
        String filenameSlurwRest = "mei-test/mei-test-set/ties-slurs/slurs-with-rests.mei";
        MeiSequence SlurwRest = new MeiSequence(filenameSlurwRest);
        Track[] actualSlurwRest = SlurwRest.getSequence().getTracks();
        Sequence sequenceSlurwRest = new Sequence(Sequence.PPQ, 256, 1);
        Track[] expectedSlurwRest = sequenceSlurwRest.getTracks();
        expectedSlurwRest[0].add(MidiBuildMessage.createKeySignature("0", "major", 0));
        expectedSlurwRest[0].add(MidiBuildMessage.createProgramChange(54, 0, 0));
        expectedSlurwRest[0].add(MidiBuildMessage.createTrackTempo(90, 0));
        
        int slurwrestm1n1 = ConvertToMidi.NoteToMidi("g", "4", null);
        expectedSlurwRest[0].add(MidiBuildMessage.createNoteOnEvent(slurwrestm1n1, 0, 0));
        expectedSlurwRest[0].add(MidiBuildMessage.createNoteOffEvent(slurwrestm1n1, 256, 0));
        
        int slurwrestm1n2 = ConvertToMidi.NoteToMidi("b", "4", null);
        expectedSlurwRest[0].add(MidiBuildMessage.createNoteOnEvent(slurwrestm1n2, 512, 0));
        expectedSlurwRest[0].add(MidiBuildMessage.createNoteOffEvent(slurwrestm1n2, 768, 0));
        
        int slurwrestm1n3 = ConvertToMidi.NoteToMidi("a", "4", null);
        expectedSlurwRest[0].add(MidiBuildMessage.createNoteOnEvent(slurwrestm1n3, 896, 0));
        expectedSlurwRest[0].add(MidiBuildMessage.createNoteOffEvent(slurwrestm1n3, 1024, 0));
        
        int slurwrestm1n4 = ConvertToMidi.NoteToMidi("g", "4", null);
        expectedSlurwRest[0].add(MidiBuildMessage.createNoteOnEvent(slurwrestm1n4, 1024, 0));
        expectedSlurwRest[0].add(MidiBuildMessage.createNoteOffEvent(slurwrestm1n4, 1280, 0));
        
        int slurwrestm1n5 = ConvertToMidi.NoteToMidi("c", "5", null);
        expectedSlurwRest[0].add(MidiBuildMessage.createNoteOnEvent(slurwrestm1n5, 3072, 0));
        expectedSlurwRest[0].add(MidiBuildMessage.createNoteOffEvent(slurwrestm1n5, 3328, 0));
        expectedSlurwRest[0].add(new MidiEvent(new MetaMessage(0x2F, new byte[0], 0), 0));
        for(int x = 0; x < actualSlurwRest.length; x++) {
            for(int i = 0; i < actualSlurwRest[x].size(); i++) {
                byte[] actualbytes = actualSlurwRest[x].get(i).getMessage().getMessage();
                byte[] expectedbytes = expectedSlurwRest[x].get(i).getMessage().getMessage();
                assertArrayEquals(expectedbytes, actualbytes);
                assertEquals(expectedSlurwRest[x].get(i).getTick(), actualSlurwRest[x].get(i).getTick());
            }
        }
        for(int x = 0; x < expectedSlurwRest.length; x++) {
            for(int i = 0; i < expectedSlurwRest[x].size(); i++) {
                byte[] actualbytes = actualSlurwRest[x].get(i).getMessage().getMessage();
                byte[] expectedbytes = expectedSlurwRest[x].get(i).getMessage().getMessage();
                assertArrayEquals(expectedbytes, actualbytes);
                assertEquals(expectedSlurwRest[x].get(i).getTick(), actualSlurwRest[x].get(i).getTick());
            }
        }
        
        //Test for slurs with tuplets 2
        String filenameTuplets2 = "mei-test/mei-test-set/tuplets/tuplets-2.mei";
        MeiSequence Tuplets2 = new MeiSequence(filenameTuplets2);
        Track[] actualTuplets2 = Tuplets2.getSequence().getTracks();
        Sequence sequenceTuplets2 = new Sequence(Sequence.PPQ, 256, 1);
        Track[] expectedTuplets2 = sequenceTuplets2.getTracks();
        expectedTuplets2[0].add(MidiBuildMessage.createKeySignature("0", "major", 0));
        expectedTuplets2[0].add(MidiBuildMessage.createProgramChange(54, 0, 0));
        expectedTuplets2[0].add(MidiBuildMessage.createTrackTempo(90, 0));
        
        int tuplets2m1n1 = ConvertToMidi.NoteToMidi("f", "4", null);
        expectedTuplets2[0].add(MidiBuildMessage.createNoteOnEvent(tuplets2m1n1, 768, 0));
        expectedTuplets2[0].add(MidiBuildMessage.createNoteOffEvent(tuplets2m1n1, 960, 0));
        
        int tuplets2m1n2 = ConvertToMidi.NoteToMidi("g", "4", null);
        expectedTuplets2[0].add(MidiBuildMessage.createNoteOnEvent(tuplets2m1n2, 960, 0));
        expectedTuplets2[0].add(MidiBuildMessage.createNoteOffEvent(tuplets2m1n2, 1152, 0));
        
        int tuplets2m1n3 = ConvertToMidi.NoteToMidi("a", "4", null);
        expectedTuplets2[0].add(MidiBuildMessage.createNoteOnEvent(tuplets2m1n3, 1152, 0));
        expectedTuplets2[0].add(MidiBuildMessage.createNoteOffEvent(tuplets2m1n3, 1344, 0));
        
        int tuplets2m1n4 = ConvertToMidi.NoteToMidi("a", "4", null);
        expectedTuplets2[0].add(MidiBuildMessage.createNoteOnEvent(tuplets2m1n4, 1152, 0));
        expectedTuplets2[0].add(MidiBuildMessage.createNoteOffEvent(tuplets2m1n4, 1344, 0));
        
        int tuplets2m1n5 = ConvertToMidi.NoteToMidi("b", "4", null);
        expectedTuplets2[0].add(MidiBuildMessage.createNoteOnEvent(tuplets2m1n5, 1344, 0));
        expectedTuplets2[0].add(MidiBuildMessage.createNoteOffEvent(tuplets2m1n5, 1536, 0));
        
        expectedTuplets2[0].add(MidiBuildMessage.createKeySignature("0", "major", 1536));
        expectedTuplets2[0].add(MidiBuildMessage.createProgramChange(54, 1536, 0));
        expectedTuplets2[0].add(MidiBuildMessage.createTrackTempo(90, 1536));
        
        int tuplets2m2n1 = ConvertToMidi.NoteToMidi("c", "5", null);
        expectedTuplets2[0].add(MidiBuildMessage.createNoteOnEvent(tuplets2m2n1, 1536, 0));
        expectedTuplets2[0].add(MidiBuildMessage.createNoteOffEvent(tuplets2m2n1, 1664, 0));
        
        int tuplets2m2n2 = ConvertToMidi.NoteToMidi("a", "4", null);
        expectedTuplets2[0].add(MidiBuildMessage.createNoteOnEvent(tuplets2m2n2, 1664, 0));
        expectedTuplets2[0].add(MidiBuildMessage.createNoteOffEvent(tuplets2m2n2, 1792, 0));
        
        int tuplets2m2n3 = ConvertToMidi.NoteToMidi("f", "5", null);
        expectedTuplets2[0].add(MidiBuildMessage.createNoteOnEvent(tuplets2m2n3, 1792, 0));
        expectedTuplets2[0].add(MidiBuildMessage.createNoteOffEvent(tuplets2m2n3, 1920, 0));
        
        int tuplets2m2n4 = ConvertToMidi.NoteToMidi("d", "5", null);
        expectedTuplets2[0].add(MidiBuildMessage.createNoteOnEvent(tuplets2m2n4, 1920, 0));
        expectedTuplets2[0].add(MidiBuildMessage.createNoteOffEvent(tuplets2m2n4, 2048, 0));
        
        int tuplets2m2n5 = ConvertToMidi.NoteToMidi("b", "4", null);
        expectedTuplets2[0].add(MidiBuildMessage.createNoteOnEvent(tuplets2m2n5, 2048, 0));
        expectedTuplets2[0].add(MidiBuildMessage.createNoteOffEvent(tuplets2m2n5, 2240, 0));
        
        int tuplets2m2n6 = ConvertToMidi.NoteToMidi("f", "4", null);
        expectedTuplets2[0].add(MidiBuildMessage.createNoteOnEvent(tuplets2m2n6, 2240, 0));
        expectedTuplets2[0].add(MidiBuildMessage.createNoteOffEvent(tuplets2m2n6, 2432, 0));
        
        int tuplets2m3n1 = ConvertToMidi.NoteToMidi("g", "4", null);
        expectedTuplets2[0].add(MidiBuildMessage.createNoteOnEvent(tuplets2m3n1, 2432, 0));
        expectedTuplets2[0].add(MidiBuildMessage.createNoteOffEvent(tuplets2m3n1, 2602, 0));
        
        int tuplets2m3n2 = ConvertToMidi.NoteToMidi("a", "4", null);
        expectedTuplets2[0].add(MidiBuildMessage.createNoteOnEvent(tuplets2m3n2, 2602, 0));
        expectedTuplets2[0].add(MidiBuildMessage.createNoteOffEvent(tuplets2m3n2, 2943, 0));
        
        int tuplets2m3n3 = ConvertToMidi.NoteToMidi("b", "4", null);
        expectedTuplets2[0].add(MidiBuildMessage.createNoteOnEvent(tuplets2m3n3, 2944, 0));
        expectedTuplets2[0].add(MidiBuildMessage.createNoteOffEvent(tuplets2m3n3, 3072, 0));
        
        int tuplets2m3n4 = ConvertToMidi.NoteToMidi("d", "5", null);
        expectedTuplets2[0].add(MidiBuildMessage.createNoteOnEvent(tuplets2m3n4, 3072, 0));
        expectedTuplets2[0].add(MidiBuildMessage.createNoteOffEvent(tuplets2m3n4, 3242, 0));
        
        int tuplets2m4n1 = ConvertToMidi.NoteToMidi("a", "4", null);
        expectedTuplets2[0].add(MidiBuildMessage.createNoteOnEvent(tuplets2m4n1, 3243, 0));
        expectedTuplets2[0].add(MidiBuildMessage.createNoteOffEvent(tuplets2m4n1, 3307, 0));
        
        int tuplets2m4n2 = ConvertToMidi.NoteToMidi("b", "4", null);
        expectedTuplets2[0].add(MidiBuildMessage.createNoteOnEvent(tuplets2m4n2, 3307, 0));
        expectedTuplets2[0].add(MidiBuildMessage.createNoteOffEvent(tuplets2m4n2, 3371, 0));
        
        int tuplets2m4n3 = ConvertToMidi.NoteToMidi("c", "5", null);
        expectedTuplets2[0].add(MidiBuildMessage.createNoteOnEvent(tuplets2m4n3, 3371, 0));
        expectedTuplets2[0].add(MidiBuildMessage.createNoteOffEvent(tuplets2m4n3, 3435, 0));
        
        int tuplets2m4n4 = ConvertToMidi.NoteToMidi("d", "5", null);
        expectedTuplets2[0].add(MidiBuildMessage.createNoteOnEvent(tuplets2m4n4, 3435, 0));
        expectedTuplets2[0].add(MidiBuildMessage.createNoteOffEvent(tuplets2m4n4, 3581, 0));
        for(int x = 0; x < actualTuplets2.length; x++) {
            for(int i = 0; i < actualTuplets2[x].size(); i++) {
                byte[] actualbytes = actualTuplets2[x].get(i).getMessage().getMessage();
                byte[] expectedbytes = expectedTuplets2[x].get(i).getMessage().getMessage();
                assertArrayEquals(expectedbytes, actualbytes);
                assertEquals(expectedTuplets2[x].get(i).getTick(), actualTuplets2[x].get(i).getTick());
            }
        }
        for(int x = 0; x < expectedTuplets2.length; x++) {
            for(int i = 0; i < expectedTuplets2[x].size(); i++) {
                byte[] actualbytes = actualTuplets2[x].get(i).getMessage().getMessage();
                byte[] expectedbytes = expectedTuplets2[x].get(i).getMessage().getMessage();
                assertArrayEquals(expectedbytes, actualbytes);
                assertEquals(expectedTuplets2[x].get(i).getTick(), actualTuplets2[x].get(i).getTick());
            }
        }
        
        //Test for slurs with tuplets 2 with tuplet spans
        String filenameMyTuplets2 = "mei-test/mei-test-set/tuplets/tuplets-2.mei";
        MeiSequence MyTuplets2 = new MeiSequence(filenameMyTuplets2);
        Track[] actualMyTuplets2 = MyTuplets2.getSequence().getTracks();
        Sequence sequenceMyTuplets2 = new Sequence(Sequence.PPQ, 256, 1);
        Track[] expectedMyTuplets2 = sequenceMyTuplets2.getTracks();
        expectedMyTuplets2[0].add(MidiBuildMessage.createKeySignature("0", "major", 0));
        expectedMyTuplets2[0].add(MidiBuildMessage.createProgramChange(54, 0, 0));
        expectedMyTuplets2[0].add(MidiBuildMessage.createTrackTempo(90, 0));
        
        int mytuplets2m1n1 = ConvertToMidi.NoteToMidi("f", "4", null);
        expectedMyTuplets2[0].add(MidiBuildMessage.createNoteOnEvent(mytuplets2m1n1, 768, 0));
        expectedMyTuplets2[0].add(MidiBuildMessage.createNoteOffEvent(mytuplets2m1n1, 960, 0));
        
        int mytuplets2m1n2 = ConvertToMidi.NoteToMidi("g", "4", null);
        expectedMyTuplets2[0].add(MidiBuildMessage.createNoteOnEvent(mytuplets2m1n2, 960, 0));
        expectedMyTuplets2[0].add(MidiBuildMessage.createNoteOffEvent(mytuplets2m1n2, 1152, 0));
        
        int mytuplets2m1n3 = ConvertToMidi.NoteToMidi("a", "4", null);
        expectedMyTuplets2[0].add(MidiBuildMessage.createNoteOnEvent(mytuplets2m1n3, 1152, 0));
        expectedMyTuplets2[0].add(MidiBuildMessage.createNoteOffEvent(mytuplets2m1n3, 1344, 0));
        
        int mytuplets2m1n4 = ConvertToMidi.NoteToMidi("a", "4", null);
        expectedMyTuplets2[0].add(MidiBuildMessage.createNoteOnEvent(mytuplets2m1n4, 1152, 0));
        expectedMyTuplets2[0].add(MidiBuildMessage.createNoteOffEvent(mytuplets2m1n4, 1344, 0));
        
        int mytuplets2m1n5 = ConvertToMidi.NoteToMidi("b", "4", null);
        expectedMyTuplets2[0].add(MidiBuildMessage.createNoteOnEvent(mytuplets2m1n5, 1344, 0));
        expectedMyTuplets2[0].add(MidiBuildMessage.createNoteOffEvent(mytuplets2m1n5, 1536, 0));
        
        expectedMyTuplets2[0].add(MidiBuildMessage.createKeySignature("0", "major", 1536));
        expectedMyTuplets2[0].add(MidiBuildMessage.createProgramChange(54, 1536, 0));
        expectedMyTuplets2[0].add(MidiBuildMessage.createTrackTempo(90, 1536));
        
        int mytuplets2m2n1 = ConvertToMidi.NoteToMidi("c", "5", null);
        expectedMyTuplets2[0].add(MidiBuildMessage.createNoteOnEvent(mytuplets2m2n1, 1536, 0));
        expectedMyTuplets2[0].add(MidiBuildMessage.createNoteOffEvent(mytuplets2m2n1, 1664, 0));
        
        int mytuplets2m2n2 = ConvertToMidi.NoteToMidi("a", "4", null);
        expectedMyTuplets2[0].add(MidiBuildMessage.createNoteOnEvent(mytuplets2m2n2, 1664, 0));
        expectedMyTuplets2[0].add(MidiBuildMessage.createNoteOffEvent(mytuplets2m2n2, 1792, 0));
        
        int mytuplets2m2n3 = ConvertToMidi.NoteToMidi("f", "5", null);
        expectedMyTuplets2[0].add(MidiBuildMessage.createNoteOnEvent(mytuplets2m2n3, 1792, 0));
        expectedMyTuplets2[0].add(MidiBuildMessage.createNoteOffEvent(mytuplets2m2n3, 1920, 0));
        
        int mytuplets2m2n4 = ConvertToMidi.NoteToMidi("d", "5", null);
        expectedMyTuplets2[0].add(MidiBuildMessage.createNoteOnEvent(mytuplets2m2n4, 1920, 0));
        expectedMyTuplets2[0].add(MidiBuildMessage.createNoteOffEvent(mytuplets2m2n4, 2048, 0));
        
        int mytuplets2m2n5 = ConvertToMidi.NoteToMidi("b", "4", null);
        expectedMyTuplets2[0].add(MidiBuildMessage.createNoteOnEvent(mytuplets2m2n5, 2048, 0));
        expectedMyTuplets2[0].add(MidiBuildMessage.createNoteOffEvent(mytuplets2m2n5, 2240, 0));
        
        int mytuplets2m2n6 = ConvertToMidi.NoteToMidi("f", "4", null);
        expectedMyTuplets2[0].add(MidiBuildMessage.createNoteOnEvent(mytuplets2m2n6, 2240, 0));
        expectedMyTuplets2[0].add(MidiBuildMessage.createNoteOffEvent(mytuplets2m2n6, 2432, 0));
        
        int mytuplets2m3n1 = ConvertToMidi.NoteToMidi("g", "4", null);
        expectedMyTuplets2[0].add(MidiBuildMessage.createNoteOnEvent(mytuplets2m3n1, 2432, 0));
        expectedMyTuplets2[0].add(MidiBuildMessage.createNoteOffEvent(mytuplets2m3n1, 2602, 0));
        
        int mytuplets2m3n2 = ConvertToMidi.NoteToMidi("a", "4", null);
        expectedMyTuplets2[0].add(MidiBuildMessage.createNoteOnEvent(mytuplets2m3n2, 2602, 0));
        expectedMyTuplets2[0].add(MidiBuildMessage.createNoteOffEvent(mytuplets2m3n2, 2943, 0));
        
        int mytuplets2m3n3 = ConvertToMidi.NoteToMidi("b", "4", null);
        expectedMyTuplets2[0].add(MidiBuildMessage.createNoteOnEvent(mytuplets2m3n3, 2944, 0));
        expectedMyTuplets2[0].add(MidiBuildMessage.createNoteOffEvent(mytuplets2m3n3, 3072, 0));
        
        int mytuplets2m3n4 = ConvertToMidi.NoteToMidi("d", "5", null);
        expectedMyTuplets2[0].add(MidiBuildMessage.createNoteOnEvent(mytuplets2m3n4, 3072, 0));
        expectedMyTuplets2[0].add(MidiBuildMessage.createNoteOffEvent(mytuplets2m3n4, 3242, 0));
        
        int mytuplets2m4n1 = ConvertToMidi.NoteToMidi("a", "4", null);
        expectedMyTuplets2[0].add(MidiBuildMessage.createNoteOnEvent(mytuplets2m4n1, 3243, 0));
        expectedMyTuplets2[0].add(MidiBuildMessage.createNoteOffEvent(mytuplets2m4n1, 3307, 0));
        
        int mytuplets2m4n2 = ConvertToMidi.NoteToMidi("b", "4", null);
        expectedMyTuplets2[0].add(MidiBuildMessage.createNoteOnEvent(mytuplets2m4n2, 3307, 0));
        expectedMyTuplets2[0].add(MidiBuildMessage.createNoteOffEvent(mytuplets2m4n2, 3371, 0));
        
        int mytuplets2m4n3 = ConvertToMidi.NoteToMidi("c", "5", null);
        expectedMyTuplets2[0].add(MidiBuildMessage.createNoteOnEvent(mytuplets2m4n3, 3371, 0));
        expectedMyTuplets2[0].add(MidiBuildMessage.createNoteOffEvent(mytuplets2m4n3, 3435, 0));
        
        int mytuplets2m4n4 = ConvertToMidi.NoteToMidi("d", "5", null);
        expectedMyTuplets2[0].add(MidiBuildMessage.createNoteOnEvent(mytuplets2m4n4, 3435, 0));
        expectedMyTuplets2[0].add(MidiBuildMessage.createNoteOffEvent(mytuplets2m4n4, 3581, 0));
        for(int x = 0; x < actualMyTuplets2.length; x++) {
            for(int i = 0; i < actualMyTuplets2[x].size(); i++) {
                byte[] actualbytes = actualMyTuplets2[x].get(i).getMessage().getMessage();
                byte[] expectedbytes = expectedMyTuplets2[x].get(i).getMessage().getMessage();
                assertArrayEquals(expectedbytes, actualbytes);
                assertEquals(expectedMyTuplets2[x].get(i).getTick(), actualMyTuplets2[x].get(i).getTick());
            }
        }
        for(int x = 0; x < expectedMyTuplets2.length; x++) {
            for(int i = 0; i < expectedMyTuplets2[x].size(); i++) {
                byte[] actualbytes = actualMyTuplets2[x].get(i).getMessage().getMessage();
                byte[] expectedbytes = expectedMyTuplets2[x].get(i).getMessage().getMessage();
                assertArrayEquals(expectedbytes, actualbytes);
                assertEquals(expectedMyTuplets2[x].get(i).getTick(), actualMyTuplets2[x].get(i).getTick());
            }
        }
        
        //Test for slurs with tuplets 2
        String filenameTiesStems = "mei-test/mei-test-set/ties-slurs/ties-stems.mei";
        MeiSequence TiesStems = new MeiSequence(filenameTiesStems);
        Track[] actualTiesStems = TiesStems.getSequence().getTracks();
        Sequence sequenceTiesStems = new Sequence(Sequence.PPQ, 256, 1);
        Track[] expectedTiesStems = sequenceTiesStems.getTracks();
        expectedTiesStems[0].add(MidiBuildMessage.createKeySignature("0", "major", 0));
        expectedTiesStems[0].add(MidiBuildMessage.createProgramChange(54, 0, 0));
        expectedTiesStems[0].add(MidiBuildMessage.createTrackTempo(90, 0));
        
        int TiesStemsm1n1 = ConvertToMidi.NoteToMidi("g", "4", null);
        expectedTiesStems[0].add(MidiBuildMessage.createNoteOnEvent(TiesStemsm1n1, 0, 0));
        expectedTiesStems[0].add(MidiBuildMessage.createNoteOffEvent(TiesStemsm1n1, 384, 0));
        
        int TiesStemsm1n2 = ConvertToMidi.NoteToMidi("e", "5", null);
        expectedTiesStems[0].add(MidiBuildMessage.createNoteOnEvent(TiesStemsm1n2, 384, 0));
        expectedTiesStems[0].add(MidiBuildMessage.createNoteOffEvent(TiesStemsm1n2, 512, 0));
        
        int TiesStemsm1n3 = ConvertToMidi.NoteToMidi("f", "5", null);
        expectedTiesStems[0].add(MidiBuildMessage.createNoteOnEvent(TiesStemsm1n3, 512, 0));
        expectedTiesStems[0].add(MidiBuildMessage.createNoteOffEvent(TiesStemsm1n3, 896, 0));
        
        int TiesStemsm1n5 = ConvertToMidi.NoteToMidi("g", "4", null);
        expectedTiesStems[0].add(MidiBuildMessage.createNoteOnEvent(TiesStemsm1n5, 512, 0));
        expectedTiesStems[0].add(MidiBuildMessage.createNoteOffEvent(TiesStemsm1n5, 896, 0));
        for(int x = 0; x < actualTiesStems.length; x++) {
            for(int i = 0; i < actualTiesStems[x].size(); i++) {
                byte[] actualbytes = actualTiesStems[x].get(i).getMessage().getMessage();
                byte[] expectedbytes = expectedTiesStems[x].get(i).getMessage().getMessage();
                assertArrayEquals(expectedbytes, actualbytes);
                assertEquals(expectedTiesStems[x].get(i).getTick(), actualTiesStems[x].get(i).getTick());
            }
        }
        for(int x = 0; x < expectedTiesStems.length; x++) {
            for(int i = 0; i < expectedTiesStems[x].size(); i++) {
                byte[] actualbytes = actualTiesStems[x].get(i).getMessage().getMessage();
                byte[] expectedbytes = expectedTiesStems[x].get(i).getMessage().getMessage();
                assertArrayEquals(expectedbytes, actualbytes);
                assertEquals(expectedTiesStems[x].get(i).getTick(), actualTiesStems[x].get(i).getTick());
            }
        }
        
        //Test for ties-unison
        String filenameTiesUnison = "mei-test/mei-test-set/ties-slurs/ties-unison.mei";
        MeiSequence TiesUnison = new MeiSequence(filenameTiesUnison);
        Track[] actualTiesUnison = TiesUnison.getSequence().getTracks();
        Sequence sequenceTiesUnison = new Sequence(Sequence.PPQ, 256, 1);
        Track[] expectedTiesUnison = sequenceTiesUnison.getTracks();
        expectedTiesUnison[0].add(MidiBuildMessage.createKeySignature("0", "major", 0));
        expectedTiesUnison[0].add(MidiBuildMessage.createProgramChange(54, 0, 0));
        expectedTiesUnison[0].add(MidiBuildMessage.createTrackTempo(90, 0));
        
        int TiesUnisonm1n1 = ConvertToMidi.NoteToMidi("b", "4", null);
        expectedTiesUnison[0].add(MidiBuildMessage.createNoteOnEvent(TiesUnisonm1n1, 0, 0));
        expectedTiesUnison[0].add(MidiBuildMessage.createNoteOffEvent(TiesUnisonm1n1, 640, 0));
        
        int TiesUnisonm1n2 = ConvertToMidi.NoteToMidi("b", "4", null);
        expectedTiesUnison[0].add(MidiBuildMessage.createNoteOnEvent(TiesUnisonm1n2, 0, 0));
        expectedTiesUnison[0].add(MidiBuildMessage.createNoteOffEvent(TiesUnisonm1n2, 640, 0));
        for(int x = 0; x < actualTiesUnison.length; x++) {
            for(int i = 0; i < actualTiesUnison[x].size(); i++) {
                byte[] actualbytes = actualTiesUnison[x].get(i).getMessage().getMessage();
                byte[] expectedbytes = expectedTiesUnison[x].get(i).getMessage().getMessage();
                assertArrayEquals(expectedbytes, actualbytes);
                assertEquals(expectedTiesUnison[x].get(i).getTick(), actualTiesUnison[x].get(i).getTick());
            }
        }
        for(int x = 0; x < expectedTiesUnison.length; x++) {
            for(int i = 0; i < expectedTiesUnison[x].size(); i++) {
                byte[] actualbytes = actualTiesUnison[x].get(i).getMessage().getMessage();
                byte[] expectedbytes = expectedTiesUnison[x].get(i).getMessage().getMessage();
                assertArrayEquals(expectedbytes, actualbytes);
                assertEquals(expectedTiesUnison[x].get(i).getTick(), actualTiesUnison[x].get(i).getTick());
            }
        }
        
        //Test for my ties-unison - with tie elements instead of attributes
        String filenameMyTiesUnison = "mei-test/mei-test-set/ties-slurs/ties-unison.mei";
        MeiSequence MyTiesUnison = new MeiSequence(filenameMyTiesUnison);
        Track[] actualMyTiesUnison = MyTiesUnison.getSequence().getTracks();
        Sequence sequenceMyTiesUnison = new Sequence(Sequence.PPQ, 256, 1);
        Track[] expectedMyTiesUnison = sequenceMyTiesUnison.getTracks();
        expectedMyTiesUnison[0].add(MidiBuildMessage.createKeySignature("0", "major", 0));
        expectedMyTiesUnison[0].add(MidiBuildMessage.createProgramChange(54, 0, 0));
        expectedMyTiesUnison[0].add(MidiBuildMessage.createTrackTempo(90, 0));
        
        int MyTiesUnisonm1n1 = ConvertToMidi.NoteToMidi("b", "4", null);
        expectedMyTiesUnison[0].add(MidiBuildMessage.createNoteOnEvent(MyTiesUnisonm1n1, 0, 0));
        expectedMyTiesUnison[0].add(MidiBuildMessage.createNoteOffEvent(MyTiesUnisonm1n1, 640, 0));
        
        int MyTiesUnisonm1n2 = ConvertToMidi.NoteToMidi("b", "4", null);
        expectedMyTiesUnison[0].add(MidiBuildMessage.createNoteOnEvent(MyTiesUnisonm1n2, 0, 0));
        expectedMyTiesUnison[0].add(MidiBuildMessage.createNoteOffEvent(MyTiesUnisonm1n2, 640, 0));
        for(int x = 0; x < actualMyTiesUnison.length; x++) {
            for(int i = 0; i < actualMyTiesUnison[x].size(); i++) {
                byte[] actualbytes = actualMyTiesUnison[x].get(i).getMessage().getMessage();
                byte[] expectedbytes = expectedMyTiesUnison[x].get(i).getMessage().getMessage();
                assertArrayEquals(expectedbytes, actualbytes);
                assertEquals(expectedMyTiesUnison[x].get(i).getTick(), actualMyTiesUnison[x].get(i).getTick());
            }
        }
        for(int x = 0; x < expectedMyTiesUnison.length; x++) {
            for(int i = 0; i < expectedMyTiesUnison[x].size(); i++) {
                byte[] actualbytes = actualMyTiesUnison[x].get(i).getMessage().getMessage();
                byte[] expectedbytes = expectedMyTiesUnison[x].get(i).getMessage().getMessage();
                assertArrayEquals(expectedbytes, actualbytes);
                assertEquals(expectedMyTiesUnison[x].get(i).getTick(), actualMyTiesUnison[x].get(i).getTick());
            }
        }
        
        //Test for 3 repeats
        String filenameThreeRepeats = "mei-test/mei-test-set/repeats/3-repeats.mei";
        MeiSequence ThreeRepeats = new MeiSequence(filenameThreeRepeats);
        Track[] actualThreeRepeats = ThreeRepeats.getSequence().getTracks();
        Sequence sequenceThreeRepeats = new Sequence(Sequence.PPQ, 256, 1);
        Track[] expectedThreeRepeats = sequenceThreeRepeats.getTracks();
        expectedThreeRepeats[0].add(MidiBuildMessage.createKeySignature("0", "major", 0));
        expectedThreeRepeats[0].add(MidiBuildMessage.createProgramChange(54, 0, 0));
        expectedThreeRepeats[0].add(MidiBuildMessage.createTrackTempo(90, 0));
        
        int ThreeRepeatsm1n1 = ConvertToMidi.NoteToMidi("g", "4", null);
        expectedThreeRepeats[0].add(MidiBuildMessage.createNoteOnEvent(ThreeRepeatsm1n1, 0, 0));
        expectedThreeRepeats[0].add(MidiBuildMessage.createNoteOffEvent(ThreeRepeatsm1n1, 768, 0));
        
        int ThreeRepeatsm2n1 = ConvertToMidi.NoteToMidi("a", "4", null);
        expectedThreeRepeats[0].add(MidiBuildMessage.createNoteOnEvent(ThreeRepeatsm2n1, 768, 0));
        expectedThreeRepeats[0].add(MidiBuildMessage.createNoteOffEvent(ThreeRepeatsm2n1, 1536, 0));
        
        int ThreeRepeatsm3n1 = ConvertToMidi.NoteToMidi("b", "4", null);
        expectedThreeRepeats[0].add(MidiBuildMessage.createNoteOnEvent(ThreeRepeatsm3n1, 1536, 0));
        expectedThreeRepeats[0].add(MidiBuildMessage.createNoteOffEvent(ThreeRepeatsm3n1, 2304, 0));
        
        int ThreeRepeatsm1n2 = ConvertToMidi.NoteToMidi("g", "4", null);
        expectedThreeRepeats[0].add(MidiBuildMessage.createNoteOnEvent(ThreeRepeatsm1n2, 2304, 0));
        expectedThreeRepeats[0].add(MidiBuildMessage.createNoteOffEvent(ThreeRepeatsm1n2, 3072, 0));
        
        int ThreeRepeatsm2n2 = ConvertToMidi.NoteToMidi("a", "4", null);
        expectedThreeRepeats[0].add(MidiBuildMessage.createNoteOnEvent(ThreeRepeatsm2n2, 3072, 0));
        expectedThreeRepeats[0].add(MidiBuildMessage.createNoteOffEvent(ThreeRepeatsm2n2, 3840, 0));
        
        int ThreeRepeatsm3n2 = ConvertToMidi.NoteToMidi("b", "4", null);
        expectedThreeRepeats[0].add(MidiBuildMessage.createNoteOnEvent(ThreeRepeatsm3n2, 3840, 0));
        expectedThreeRepeats[0].add(MidiBuildMessage.createNoteOffEvent(ThreeRepeatsm3n2, 4608, 0));
        
        int ThreeRepeatsm4n1 = ConvertToMidi.NoteToMidi("c", "5", null);
        expectedThreeRepeats[0].add(MidiBuildMessage.createNoteOnEvent(ThreeRepeatsm4n1, 4608, 0));
        expectedThreeRepeats[0].add(MidiBuildMessage.createNoteOffEvent(ThreeRepeatsm4n1, 5376, 0));
        for(int x = 0; x < actualThreeRepeats.length; x++) {
            for(int i = 0; i < actualThreeRepeats[x].size(); i++) {
                byte[] actualbytes = actualThreeRepeats[x].get(i).getMessage().getMessage();
                byte[] expectedbytes = expectedThreeRepeats[x].get(i).getMessage().getMessage();
                assertArrayEquals(expectedbytes, actualbytes);
                assertEquals(expectedThreeRepeats[x].get(i).getTick(), actualThreeRepeats[x].get(i).getTick());
            }
        }
        for(int x = 0; x < expectedThreeRepeats.length; x++) {
            for(int i = 0; i < expectedThreeRepeats[x].size(); i++) {
                byte[] actualbytes = actualThreeRepeats[x].get(i).getMessage().getMessage();
                byte[] expectedbytes = expectedThreeRepeats[x].get(i).getMessage().getMessage();
                assertArrayEquals(expectedbytes, actualbytes);
                assertEquals(expectedThreeRepeats[x].get(i).getTick(), actualThreeRepeats[x].get(i).getTick());
            }
        }
        
        //Test for repeats positioning
        String filenameRepeatsPositioning = "mei-test/mei-test-set/repeats/repeats-positioning.mei";
        MeiSequence RepeatsPositioning = new MeiSequence(filenameRepeatsPositioning);
        Track[] actualRepeatsPositioning = RepeatsPositioning.getSequence().getTracks();
        Sequence sequenceRepeatsPositioning = new Sequence(Sequence.PPQ, 256, 1);
        Track[] expectedRepeatsPositioning = sequenceRepeatsPositioning.getTracks();
        expectedRepeatsPositioning[0].add(MidiBuildMessage.createKeySignature("2s", "major", 0));
        expectedRepeatsPositioning[0].add(MidiBuildMessage.createProgramChange(54, 0, 0));
        expectedRepeatsPositioning[0].add(MidiBuildMessage.createTrackTempo(90, 0));
        
        int RepeatsPositioningm1n1 = ConvertToMidi.NoteToMidi("b", "4", null);
        expectedRepeatsPositioning[0].add(MidiBuildMessage.createNoteOnEvent(RepeatsPositioningm1n1, 0, 0));
        expectedRepeatsPositioning[0].add(MidiBuildMessage.createNoteOffEvent(RepeatsPositioningm1n1, 768, 0));
        
        int RepeatsPositioningm2n1 = ConvertToMidi.NoteToMidi("f", "5", "s");
        expectedRepeatsPositioning[0].add(MidiBuildMessage.createNoteOnEvent(RepeatsPositioningm2n1, 768, 0));
        expectedRepeatsPositioning[0].add(MidiBuildMessage.createNoteOffEvent(RepeatsPositioningm2n1, 1536, 0));
        
        expectedRepeatsPositioning[0].add(MidiBuildMessage.createKeySignature("0", "major", 1536));
        expectedRepeatsPositioning[0].add(MidiBuildMessage.createProgramChange(54, 1536, 0));
        expectedRepeatsPositioning[0].add(MidiBuildMessage.createTrackTempo(90, 1536));
        
        expectedRepeatsPositioning[0].add(MidiBuildMessage.createKeySignature("2s", "major", 1536));
        expectedRepeatsPositioning[0].add(MidiBuildMessage.createProgramChange(54, 1536, 0));
        expectedRepeatsPositioning[0].add(MidiBuildMessage.createTrackTempo(90, 1536));
        
        int RepeatsPositioningm1n2 = ConvertToMidi.NoteToMidi("b", "4", null);
        expectedRepeatsPositioning[0].add(MidiBuildMessage.createNoteOnEvent(RepeatsPositioningm1n2, 1536, 0));
        expectedRepeatsPositioning[0].add(MidiBuildMessage.createNoteOffEvent(RepeatsPositioningm1n2, 2304, 0));
        
        int RepeatsPositioningm2n2 = ConvertToMidi.NoteToMidi("f", "5", "s");
        expectedRepeatsPositioning[0].add(MidiBuildMessage.createNoteOnEvent(RepeatsPositioningm2n2, 2304, 0));
        expectedRepeatsPositioning[0].add(MidiBuildMessage.createNoteOffEvent(RepeatsPositioningm2n2, 3072, 0));
        
        expectedRepeatsPositioning[0].add(MidiBuildMessage.createKeySignature("2f", "major", 3072));
        expectedRepeatsPositioning[0].add(MidiBuildMessage.createProgramChange(54, 3072, 0));
        expectedRepeatsPositioning[0].add(MidiBuildMessage.createTrackTempo(90, 3072));
        
        int RepeatsPositioningm3n1 = ConvertToMidi.NoteToMidi("f", "3", null);
        expectedRepeatsPositioning[0].add(MidiBuildMessage.createNoteOnEvent(RepeatsPositioningm3n1, 3072, 0));
        expectedRepeatsPositioning[0].add(MidiBuildMessage.createNoteOffEvent(RepeatsPositioningm3n1, 3840, 0));
        
        int RepeatsPositioningm5n1 = ConvertToMidi.NoteToMidi("b", "3", "f");
        expectedRepeatsPositioning[0].add(MidiBuildMessage.createNoteOnEvent(RepeatsPositioningm5n1, 3840, 0));
        expectedRepeatsPositioning[0].add(MidiBuildMessage.createNoteOffEvent(RepeatsPositioningm5n1, 4608, 0));
        
        expectedRepeatsPositioning[0].add(MidiBuildMessage.createKeySignature("0", "major", 4608));
        expectedRepeatsPositioning[0].add(MidiBuildMessage.createProgramChange(54, 4608, 0));
        expectedRepeatsPositioning[0].add(MidiBuildMessage.createTrackTempo(90, 4608));
        
        expectedRepeatsPositioning[0].add(MidiBuildMessage.createKeySignature("2s", "major", 4608));
        expectedRepeatsPositioning[0].add(MidiBuildMessage.createProgramChange(54, 4608, 0));
        expectedRepeatsPositioning[0].add(MidiBuildMessage.createTrackTempo(90, 4608));
        
        expectedRepeatsPositioning[0].add(MidiBuildMessage.createKeySignature("2f", "major", 4608));
        expectedRepeatsPositioning[0].add(MidiBuildMessage.createProgramChange(54, 4608, 0));
        expectedRepeatsPositioning[0].add(MidiBuildMessage.createTrackTempo(90, 4608));
        
        int RepeatsPositioningm3n2 = ConvertToMidi.NoteToMidi("f", "3", null);
        expectedRepeatsPositioning[0].add(MidiBuildMessage.createNoteOnEvent(RepeatsPositioningm3n2, 4608, 0));
        expectedRepeatsPositioning[0].add(MidiBuildMessage.createNoteOffEvent(RepeatsPositioningm3n2, 5376, 0));
        
        int RepeatsPositioningm5n2 = ConvertToMidi.NoteToMidi("b", "3", "f");
        expectedRepeatsPositioning[0].add(MidiBuildMessage.createNoteOnEvent(RepeatsPositioningm5n2, 5376, 0));
        expectedRepeatsPositioning[0].add(MidiBuildMessage.createNoteOffEvent(RepeatsPositioningm5n2, 6144, 0));
        for(int x = 0; x < actualRepeatsPositioning.length; x++) {
            for(int i = 0; i < actualRepeatsPositioning[x].size(); i++) {
                byte[] actualbytes = actualRepeatsPositioning[x].get(i).getMessage().getMessage();
                byte[] expectedbytes = expectedRepeatsPositioning[x].get(i).getMessage().getMessage();
                assertArrayEquals(expectedbytes, actualbytes);
                assertEquals(expectedRepeatsPositioning[x].get(i).getTick(), actualRepeatsPositioning[x].get(i).getTick());
            }
        }
        for(int x = 0; x < expectedRepeatsPositioning.length; x++) {
            for(int i = 0; i < expectedRepeatsPositioning[x].size(); i++) {
                byte[] actualbytes = actualRepeatsPositioning[x].get(i).getMessage().getMessage();
                byte[] expectedbytes = expectedRepeatsPositioning[x].get(i).getMessage().getMessage();
                assertArrayEquals(expectedbytes, actualbytes);
                assertEquals(expectedRepeatsPositioning[x].get(i).getTick(), actualRepeatsPositioning[x].get(i).getTick());
            }
        }
    }
}
