/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.jmei2midi;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import javax.sound.midi.InvalidMidiDataException;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author Tristano
 */
public class MainTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    
    /**
     * Reassign output stream to new print stream so system output can be tested.
     */
    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }
    
    @After
    public void cleanupStreams() {
        System.setOut(null);
    }

    /**
     * Test of main method, of class Main.
     * @throws javax.sound.midi.InvalidMidiDataException
     */
    @Test
    public void testMain() throws InvalidMidiDataException {
        String[] args = {"1","2","3"};
        Main.main(args);
        assertEquals("Input should be of type : java -jar jMei2Midi-1.0-jar-with-dependencies.jar \"filenamein\" \"filenameout\"".trim()
                     ,outContent.toString().trim());
        
        outContent.reset();
        String[] args1 = {"1"};
        Main.main(args1);
        assertEquals("Converting from 1\nERROR.\n"
                        + "File note found " + args[0] + ".\n"
                        + "Input should be of type : "
                        + "java -jar jMei2Midi-1.0-jar-with-dependencies.jar \"filenamein\"",
                    outContent.toString().trim());
        
        outContent.reset();
        String[] args2 = {"1","2"};
        Main.main(args2);
        assertEquals("Converting from 1 to 2\nERROR.\n"
                        + "File note found " + args[0] + " and " + args[1] + ".\n"
                        + "Input should be of type : "
                        + "java -jar jMei2Midi-1.0-jar-with-dependencies.jar \"filenamein\" \"filenameout\"",
                    outContent.toString().trim());
    }

    /**
     * Test of readWriteFile method, of class Main.
     * @throws javax.sound.midi.InvalidMidiDataException
     */
    @Test
    public void testReadWriteFile() throws InvalidMidiDataException {
        System.out.println("readWriteFile");
        String fileNameIn = "mei-test/CompleteExamples/Ives_TheCage.mei";
        Main.readWriteFile(fileNameIn);
        File ives = new File("midi-test/CompleteExamples/Ives_TheCage.midi");
        assertTrue(ives.isFile());
    }

    /**
     * Test of readDirectory method, of class Main.
     * Currently is tested with all mei files given in complete examples.
     * @throws javax.sound.midi.InvalidMidiDataException
     */
    //@Test
    public void testReadDirectory() throws InvalidMidiDataException {
        //Test Complete Examples
        System.out.println("readDirectory CompleteExamples");
        String CEIn = "mei-test/CompleteExamples/";
        String CEOut = "midi-test/CompleteExamples/";
        Main.readDirectory(CEIn, CEOut);
        
        File dirFileNameIn = new File(CEIn);
        File[] fileArrayIn = dirFileNameIn.listFiles();
        
        File dirFileNameOut = new File(CEOut);
        File[] fileArrayOut = dirFileNameOut.listFiles();
        
        assertEquals(fileArrayIn.length, fileArrayOut.length);
        
        for(int i = 0; i < fileArrayIn.length; i++) {
            assertEquals(fileArrayIn[i].toString().replaceAll("mei", ""),
                         fileArrayOut[i].toString().replaceAll("midi", ""));
        }
        
        //Test mei-test-set
        System.out.println("readDirectory mei-test-set");
        String testIn = "mei-test/mei-test-set/";
        String testOut = "midi-test/mei-test-set/";
        Main.readDirectory(testIn, testOut);
        
        File dirTestIn = new File(testIn);
        File[] dirTestArrayIn = dirTestIn.listFiles();
        
        File dirTestOut = new File(testOut);
        File[] dirTestArrayOut = dirTestOut.listFiles();
        
        assertEquals(dirTestArrayIn.length, dirTestArrayOut.length);
    }
}
