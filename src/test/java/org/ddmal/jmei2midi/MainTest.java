/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.jmei2midi;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import javax.sound.midi.InvalidMidiDataException;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

/**
 *
 * @author Tristano
 */
public class MainTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Rule public TemporaryFolder tempFolder = new TemporaryFolder();
    
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
    
    @After
    public void cleanupTempFolder() {
        tempFolder.delete();
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
        
        String[] args2 = {"1","2"};
        Main.main(args2);
        assertEquals("Converting from 1 to 2\nERROR.\n"
                        + "File note found " + args[0] + " and " + args[1] + ".\n"
                        + "Input should be of type : "
                        + "java -jar jMei2Midi-1.0-jar-with-dependencies.jar \"filenamein\" \"filenameout\"",
                    outContent.toString().trim());
        
        //Reset System.out to standard output
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
    }

    /**
     * Test of readWriteFile method, of class Main.
     * @throws javax.sound.midi.InvalidMidiDataException
     */
    @Test
    public void testReadWriteFile() throws InvalidMidiDataException {
        System.out.println("readWriteFile");
        String fileNameIn = "mei-test/CompleteExamples/Ives_TheCage.mei";
        Main.readWriteFile(fileNameIn,"Ives_TheCage.midi");
        File ives = new File("Ives_TheCage.midi");
        assertTrue(ives.isFile());
        File file = new File("Ives_TheCage.midi");
        file.deleteOnExit();
    }

    /**
     * Test of readDirectory method, of class Main.
     * Currently is tested with all mei files given in complete examples.
     * @throws javax.sound.midi.InvalidMidiDataException
     */
    @Test
    public void testReadDirectory() throws InvalidMidiDataException {
        //Build temporary directory for midi
        File tempDir = tempFolder.newFolder("CompleteExamples");
        String tempDirName = tempDir.getPath();
        
        //Test Complete Examples
        String CEIn = "mei-test/CompleteExamples/";
        Main.readDirectory(CEIn, tempDirName);
        
        //Get all mei files in CompleteExamples
        File dirFileNameIn = new File(CEIn);
        MusicFileFilter fileFilter = new MusicFileFilter();
        File[] fileArrayIn = dirFileNameIn.listFiles(fileFilter);
        
        //Get all midi files in temp directory
        File[] fileArrayOut = tempDir.listFiles();
        
        assertEquals(fileArrayIn.length, fileArrayOut.length);
        for(int i = 0; i < fileArrayIn.length; i++) {
            assertEquals(fileArrayIn[i].getName().replaceAll("mei", ""),
                         fileArrayOut[i].getName().replaceAll("midi", ""));
        }
    }
}
