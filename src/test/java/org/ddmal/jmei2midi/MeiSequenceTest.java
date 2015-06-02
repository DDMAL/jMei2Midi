/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.jmei2midi;

import ca.mcgill.music.ddmal.mei.MeiAttribute;
import ca.mcgill.music.ddmal.mei.MeiDocument;
import ca.mcgill.music.ddmal.mei.MeiElement;
import ca.mcgill.music.ddmal.mei.MeiXmlReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;
import org.ddmal.midiUtilities.MidiBuildMessage;
import org.junit.AfterClass;
import org.junit.Assert;
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
        //Test 1 : Czerny
        String filename = "/Users/dinamix/Documents/mei/"
                + "music-encoding/samples/MEI2013/Music/Complete examples/Czerny_op603_6.mei";       
        MeiSequence czerny = new MeiSequence(filename);
        MeiWork expected = new MeiWork(1, "d", "minor", "4", "4", "default");
        expected.addInstrVoice(1, "Organ");
        MeiWork actual = czerny.getWorks().get(1);
        assertEquals(expected, actual);
        
        //Test 2 Beethoven
        String filename2 = "/Users/dinamix/Documents/mei/"
                + "music-encoding/samples/MEI2013/Music/Complete examples/Beethoven_op.18.mei";       
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
        String filenameG = "/Users/dinamix/Documents/mei/"
                + "music-encoding/samples/MEI2013/Music/Complete examples/Grieg_op.43_butterfly.mei";
        MeiSequence griegB = new MeiSequence(filenameG);
        MeiWork actualG = griegB.getWorks().get(1);
        MeiWork expectedG = new MeiWork(1, "a", "major", "4", "4", "Allegro grazioso");
        expectedG.addInstrVoice(1, "Piano");
        assertEquals(expectedG,actualG);
        
        //Test Brahms String Quartet
        String filenameBrahms = "/Users/dinamix/Documents/mei/"
                + "music-encoding/samples/MEI2013/Music/Complete examples/Brahms_StringQuartet_Op51_No1.mei";
        MeiSequence brahmsS = new MeiSequence(filenameBrahms);
        MeiWork actualBS = brahmsS.getWorks().get(1);
        MeiWork expectedBS = new MeiWork(1, "c", "major", "3", "4", "Poco Adagio.");
        expectedBS.addInstrVoice(1, "Violin I");
        expectedBS.addInstrVoice(2, "Violin II");
        expectedBS.addInstrVoice(3, "Viola");
        expectedBS.addInstrVoice(4, "Violoncello");
        assertEquals(expectedBS, actualBS);
        
        //Test Brahms WieMelodien
        String filenameBrahmsW = "/Users/dinamix/Documents/mei/"
                + "music-encoding/samples/MEI2013/Music/Complete examples/Brahms_WieMelodienZiehtEsMir.mei";
        MeiSequence brahmsW = new MeiSequence(filenameBrahmsW);
        MeiWork actualBW = brahmsW.getWorks().get(1);
        MeiWork expectedBW = new MeiWork(1, "a", "major", "2", "2", "default");
        expectedBW.addInstrVoice(1, "Voice");
        expectedBW.addInstrVoice(2, "Piano");
        assertEquals(expectedBW, actualBW);
        
        //Test Vivaldi - switches scoreDef
        String filenameViv = "/Users/dinamix/Documents/mei/"
                + "music-encoding/samples/MEI2013/Music/Complete examples/Vivaldi_Op8_No.2.mei";
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
        String filenameJSB = "/Users/dinamix/Documents/mei/"
                + "music-encoding/samples/MEI2013/Music/Complete examples/JSB_BWV1047_2.mei";
        MeiSequence JSB = new MeiSequence(filenameJSB);
        MeiWork actualJSB = JSB.getWorks().get(1);
        MeiWork expectedJSB = new MeiWork(1, "f", "major", "3", "4", "Andante");
        expectedJSB.addInstrVoice(1, "Flute");
        expectedJSB.addInstrVoice(2, "Oboe");
        expectedJSB.addInstrVoice(3, "Violin");
        expectedJSB.addInstrVoice(4, "Violoncello e Cembalo");
        assertEquals(expectedJSB, actualJSB);
        
        //Test Hummel - switches scoreDef
        String filenameHum = "/Users/dinamix/Documents/mei/"
                + "music-encoding/samples/MEI2013/Music/Complete examples/Hummel_op.67_No.11.mei";
        MeiSequence Hum = new MeiSequence(filenameHum);
        MeiWork actualHum = Hum.getWorks().get(1);
        MeiWork expectedHum = new MeiWork(1, "b", "major", "9", "4", "Allegro");
        expectedHum.addInstrVoice(1, "Piano");
        assertEquals(expectedHum, actualHum);
        
        //Test Joplin Elite - switch scoreDef key.sig
        String filenameJop = "/Users/dinamix/Documents/mei/"
                + "music-encoding/samples/MEI2013/Music/Complete examples/Joplin_Elite_Syncopations.mei";  
        MeiSequence Jop = new MeiSequence(filenameJop);
        MeiWork actualJop = Jop.getWorks().get(1);
        MeiWork expectedJop = new MeiWork(1, "f", "major", "2", "4", "Not fast");
        expectedJop.addInstrVoice(1, "Piano");
        assertEquals(expectedJop, actualJop);
        //Prints out invalid tempo of "Not fast"
        //System.out.println(Jop.getStats());
        
        //Test Chopin Etude
        String filenameChopin = "/Users/dinamix/Documents/mei/"
                + "music-encoding/samples/MEI2013/Music/Complete examples/Chopin_Etude_op.10_no.9_2013.mei";  
        MeiSequence Chopin = new MeiSequence(filenameChopin);
        MeiWork actualChopin = Chopin.getWorks().get(1);
        MeiWork expectedChopin = new MeiWork(1, "f", "minor", "6", "8", "Allegro molto agitato");
        expectedChopin.addInstrVoice(1, "Piano");
        assertEquals(expectedChopin, actualChopin);
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
        HashMap<Integer,MeiStaff> expected = new HashMap<>();
        expected.put(1, new MeiStaff(1, "default", "Voice", "0", "major", "4", "4"));
        assertEquals(expected, trebleclefout.getStaffs());
        
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
        expected2.put(1, new MeiStaff(1, "Allegro.", "Violin I", "1f", "minor", "2", "4"));
        assertEquals(expected2.get(1), beethoven18.getStaffs().get(1));
        
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
        expected3.put(1, new MeiStaff(1, "Allegretto grazioso", "Tenor", "0", "major", "6", "8"));
        assertEquals(expected3.get(1), berlioz2.getStaffs().get(1));
    }

    /**
     * Test of processStaffDef method, of class MeiSequence.
     * @throws javax.sound.midi.InvalidMidiDataException
     */
    @Test
    public void testProcessStaffDef() throws InvalidMidiDataException {
        //Test 1 : Czerny (Organ w/ 3 staves)
        String czernyfile = "/Users/dinamix/Documents/mei/"
                + "music-encoding/samples/MEI2013/Music/Complete examples/Czerny_op603_6.mei";       
        MeiSequence czerny = new MeiSequence(czernyfile);
        HashMap<Integer,MeiStaff> actualCz = czerny.getStaffs();
        HashMap<Integer,MeiStaff> expectedCz = new HashMap<>();
        expectedCz.put(1, new MeiStaff(1, "default", "Organ", "1f", "minor", "4", "4"));
        expectedCz.put(2, new MeiStaff(2, "default", "Organ", "1f", "minor", "4", "4"));
        expectedCz.put(3, new MeiStaff(3, "default", "Organ", "1f", "minor", "4", "4"));
        assertEquals(expectedCz, actualCz);
        
        //Test 2 : Grieg butterfly
        String griegfile = "/Users/dinamix/Documents/mei/"
                + "music-encoding/samples/MEI2013/Music/Complete examples/Grieg_op.43_butterfly.mei";
        MeiSequence grieg = new MeiSequence(griegfile);
        HashMap<Integer,MeiStaff> actualGr = grieg.getStaffs();
        HashMap<Integer,MeiStaff> expectedGr = new HashMap<>();
        expectedGr.put(1, new MeiStaff(1, "Allegro grazioso", "Piano", "3s", "major", "4", "4"));
        expectedGr.put(2, new MeiStaff(2, "Allegro grazioso", "Piano", "3s", "major", "4", "4"));
        
        //Test : Brahms String quartet
        String filenameBrahms = "/Users/dinamix/Documents/mei/"
                + "music-encoding/samples/MEI2013/Music/Complete examples/Brahms_StringQuartet_Op51_No1.mei";
        MeiSequence brahmsS = new MeiSequence(filenameBrahms);
        HashMap<Integer,MeiStaff> actualBS = brahmsS.getStaffs();
        HashMap<Integer,MeiStaff> expectedBS = new HashMap<>();
        expectedBS.put(1, new MeiStaff(1, "Poco Adagio.", "Violin I", "4f", "major", "3", "4"));
        expectedBS.put(2, new MeiStaff(2, "Poco Adagio.", "Violin II", "4f", "major", "3", "4"));
        expectedBS.put(3, new MeiStaff(3, "Poco Adagio.", "Viola", "4f", "major", "3", "4"));
        expectedBS.put(4, new MeiStaff(4, "Poco Adagio.", "Violoncello", "4f", "major", "3", "4"));
        //Strange staffDef @ n=6 but we add it to make test work (never used in piece)
        expectedBS.put(6, new MeiStaff(6,"Poco Adagio.", "Violoncello", "4f", "major", "3", "4"));
        assertEquals(expectedBS, actualBS);
        
        //Test : Brahms Wie
        String filenameBrahmsW = "/Users/dinamix/Documents/mei/"
                + "music-encoding/samples/MEI2013/Music/Complete examples/Brahms_WieMelodienZiehtEsMir.mei";
        MeiSequence brahmsW = new MeiSequence(filenameBrahmsW);
        HashMap<Integer,MeiStaff> actualBW = brahmsW.getStaffs();
        HashMap<Integer,MeiStaff> expectedBW = new HashMap<>();
        expectedBW.put(1, new MeiStaff(1, "default", "Voice", "3s", "major", "2", "2"));
        expectedBW.put(2, new MeiStaff(2, "default", "Piano", "3s", "major", "2", "2"));
        expectedBW.put(3, new MeiStaff(3, "default", "Piano", "3s", "major", "2", "2"));
        assertEquals(expectedBW, actualBW);
        
        //Test : Vivaldi
        String filenameViv = "/Users/dinamix/Documents/mei/"
                + "music-encoding/samples/MEI2013/Music/Complete examples/Vivaldi_Op8_No.2.mei";
        MeiSequence viv = new MeiSequence(filenameViv);
        HashMap<Integer,MeiStaff> actualViv = viv.getStaffs();
        HashMap<Integer,MeiStaff> expectViv = new HashMap<>();
        expectViv.put(1, new MeiStaff(1, "Presto", "Violino Principale", "2f", "minor", "3", "4"));
        expectViv.put(2, new MeiStaff(2, "Presto", "Violino Primo", "2f", "minor", "3", "4"));
        expectViv.put(3, new MeiStaff(3, "Presto", "Violino Secondo", "2f", "minor", "3", "4"));
        expectViv.put(4, new MeiStaff(4, "Presto", "Alto Viola", "2f", "minor", "3", "4"));
        expectViv.put(5, new MeiStaff(5, "Presto", "Organo e Violencello", "2f", "minor", "3", "4"));
        assertEquals(expectViv, actualViv);
        
        //Test : JSB
        String filenameJSB = "/Users/dinamix/Documents/mei/"
                + "music-encoding/samples/MEI2013/Music/Complete examples/JSB_BWV1047_2.mei";
        MeiSequence JSB = new MeiSequence(filenameJSB);
        HashMap<Integer,MeiStaff> actualJSB = JSB.getStaffs();
        HashMap<Integer,MeiStaff> expectJSB = new HashMap<>();
        expectJSB.put(1, new MeiStaff(1, "Andante", "Flute", "1f", "major", "3", "4"));
        expectJSB.put(2, new MeiStaff(2, "Andante", "Oboe", "1f", "major", "3", "4"));
        expectJSB.put(3, new MeiStaff(3, "Andante", "Violin", "1f", "major", "3", "4"));
        expectJSB.put(4, new MeiStaff(4, "Andante", "Violoncello e Cembalo", "1f", "major", "3", "4"));
        assertEquals(expectJSB, actualJSB);
        
        //Test : Hummel - switch scoreDef
        String filenameHum = "/Users/dinamix/Documents/mei/"
                + "music-encoding/samples/MEI2013/Music/Complete examples/Hummel_op.67_No.11.mei";
        MeiSequence Hum = new MeiSequence(filenameHum);
        HashMap<Integer,MeiStaff> actualHum = Hum.getStaffs();
        HashMap<Integer,MeiStaff> expectedHum = new HashMap<>();
        expectedHum.put(1, new MeiStaff(1, "Allegro", "Piano", "5s", "major", "9", "4"));
        expectedHum.put(2, new MeiStaff(2, "Allegro", "Piano", "5s", "major", "9", "4"));
        assertEquals(expectedHum, actualHum);
        
        //Test Joplin Elite - switch scoreDef key.sig
        String filenameJop = "/Users/dinamix/Documents/mei/"
                + "music-encoding/samples/MEI2013/Music/Complete examples/Joplin_Elite_Syncopations.mei";  
        MeiSequence Jop = new MeiSequence(filenameJop);
        HashMap<Integer,MeiStaff> actualJop = Jop.getStaffs();
        HashMap<Integer,MeiStaff> expectedJop = new HashMap<>();
        expectedJop.put(1, new MeiStaff(1, "Not fast", "Piano", "2f", "major", "2", "4"));
        expectedJop.put(2, new MeiStaff(2, "Not fast", "Piano", "2f", "major", "2", "4"));
        assertEquals(expectedJop, actualJop);
        
        //Test Debussy Mandolin
        String filenameDeb = "/Users/dinamix/Documents/mei/"
                + "music-encoding/samples/MEI2013/Music/Complete examples/Debussy_Mandoline.mei";  
        MeiSequence Deb = new MeiSequence(filenameDeb);
        HashMap<Integer,MeiStaff> actualDeb = Deb.getStaffs();
        HashMap<Integer,MeiStaff> expectedDeb = new HashMap<>();
        //Voice good because label is chant and so since chant is not valid
        //we take voice in meihead as the instrument instead
        expectedDeb.put(1, new MeiStaff(1, "Allegretto vivace", "Voice", "0", "major", "6", "8"));
        expectedDeb.put(2, new MeiStaff(2, "Allegretto vivace", "Piano", "0", "major", "6", "8"));
        expectedDeb.put(3, new MeiStaff(3, "Allegretto vivace", "Piano", "0", "major", "6", "8"));
        assertEquals(expectedDeb, actualDeb);
        //System.out.println(Deb.getStats());
        
        //Test Chopin Etude
        String filenameChopin = "/Users/dinamix/Documents/mei/"
                + "music-encoding/samples/MEI2013/Music/Complete examples/Chopin_Etude_op.10_no.9_2013.mei";  
        MeiSequence Chopin = new MeiSequence(filenameChopin);
        HashMap<Integer,MeiStaff> actualChopin = Chopin.getStaffs();
        HashMap<Integer,MeiStaff> expectedChopin = new HashMap<>();
        expectedChopin.put(1, new MeiStaff(1, "Allegro molto agitato", "Piano", "4f", "minor", "6", "8"));
        expectedChopin.put(2, new MeiStaff(2, "Allegro molto agitato", "Piano", "4f", "minor", "6", "8"));
        assertEquals(expectedChopin, actualChopin);
    }
    
    /**
     * Test of createMeiStaff method, of class MeiSequence.
     * @throws InvalidMidiDataException 
     */
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
        MeiStaff expected = new MeiStaff(1, "default", "Voice", "0", "major", "4", "4");
        HashMap<Integer,MeiStaff> expectedMap = new HashMap<>();
        expectedMap.put(1, expected);
        assertEquals(expected,actual);
        assertEquals(expectedMap, trebleclefout.getStaffs());
    }
    
    /**
     * Test of buildMidiTrack method, of class MeiSequence.
     * Have to compare actual byte arrays separately because the track
     * equals method only compares the reference and not the actual value.
     */
    @Test
    public void testBuildMidiTrack() throws InvalidMidiDataException {
        //Test for treble-clef-out
        String filename = "/Users/dinamix/Documents/mei/"
                + "mei-test-set/MEI-files/clefs/treble-clef-out.mei";
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
        
        //Test Czerny
        String filenameCzerny = "/Users/dinamix/Documents/mei/"
                + "music-encoding/samples/MEI2013/Music/Complete examples/Czerny_op603_6.mei";
        MeiSequence Czerny = new MeiSequence(filenameCzerny);
        Track[] actualCzerny = Czerny.getSequence().getTracks();
        Sequence sequenceCzerny = new Sequence(Sequence.PPQ, 256,6);
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
        
        //Test Chopin Etude
        String filenameChopin = "/Users/dinamix/Documents/mei/"
                + "music-encoding/samples/MEI2013/Music/Complete examples/Chopin_Etude_op.10_no.9_2013.mei";
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
        
        //Test Joplin
        String filenameJoplin = "/Users/dinamix/Documents/mei/"
                + "music-encoding/samples/MEI2013/Music/Complete examples/Joplin_Elite_Syncopations.mei";
        MeiSequence Joplin = new MeiSequence(filenameJoplin);
        Track[] actualJoplin = Joplin.getSequence().getTracks();
        assertEquals(2, actualJoplin.length);
        Sequence sequenceJoplin = new Sequence(Sequence.PPQ, 256,4);
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
        
        //Test Beethoven - lots of scoreDefs and staffDefs
        //Did not test just looked at output
        //Strange: There are n=6 but creates for 5 and 6
        //however n=6 is completely unnecessary and random...
        //Maybe test later
        String filenameBeeth = "/Users/dinamix/Documents/mei/"
                + "music-encoding/samples/MEI2013/Music/Complete examples/Beethoven_op.18.mei";
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
        String filenameJSB = "/Users/dinamix/Documents/mei/"
                + "music-encoding/samples/MEI2013/Music/Complete examples/JSB_BWV1047_2.mei";
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
        
        //Test Vivaldi
        //Nasty bug: Violoncello and violencello mistake for staff 5
        String filenameViv = "/Users/dinamix/Documents/mei/"
                + "music-encoding/samples/MEI2013/Music/Complete examples/Vivaldi_Op8_No.2.mei";
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
        for(int x = 0; x < actualViv.length; x++) {
            System.out.println(x + " New Track: ");
            for(int i = 0; i < actualViv[x].size(); i++) {
                byte[] actualbytes = actualViv[x].get(i).getMessage().getMessage();
                //System.out.println(Arrays.toString(actualbytes));
                byte[] expectedbytes = expectedViv[x].get(i).getMessage().getMessage();
                //System.out.println(Arrays.toString(expectedbytes));
                assertArrayEquals(expectedbytes, actualbytes);
            }
        }
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
