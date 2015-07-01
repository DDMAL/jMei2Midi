/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.jmei2midi;

import ca.mcgill.music.ddmal.mei.MeiDocument;
import ca.mcgill.music.ddmal.mei.MeiElement;
import ca.mcgill.music.ddmal.mei.MeiXmlReader;
import java.io.File;
import java.util.List;
import javax.sound.midi.InvalidMidiDataException;
import org.ddmal.midiUtilities.MidiIO;


/**
 * 
 * @author dinamix
 */
public class Main {
    
    private static final String jarName = "jMei2Midi-1.0-jar-with-dependencies.jar";
    private static final String defaultInput = "mei-test/CompleteExamples/";
    private static final String defaultOutput = "midi-test/CompleteExamples/";
    
    public static void main(String[] args) throws InvalidMidiDataException {
        if(args.length == 0) {
            System.out.println("Converting from " + defaultInput + " to " + defaultOutput);
            readDirectory(defaultInput, defaultOutput);
            System.out.println("Finished!");
        }
        else if(args.length == 1) {
            try {
                System.out.println("Converting from " + args[0]);
                readWriteFile(args[0]);
                System.out.println("Finished!");
            }
            catch(Exception ex) {
                System.out.println("ERROR.\n"
                        + "File note found " + args[0] + ".\n"
                        + "Input should be of type : "
                        + "java -jar " + jarName + " \"filenamein\"");
            }
        }
        else if(args.length == 2) {
            try {
                System.out.println("Converting from " + args[0] + " to " + args[1]);
                readDirectory(args[0], args[1]);
                System.out.println("Finished!");
            }
            catch(Exception ex) {
                System.out.println("ERROR.\n"
                        + "File note found " + args[0] + " and " + args[1] + ".\n"
                        + "Input should be of type : "
                        + "java -jar " + jarName + " \"filenamein\" \"filenameout\"");
            }
        }
        else {
            System.out.println("Input should be of type : "
                        + "java -jar " + jarName + " \"filenamein\" \"filenameout\"");
        }
    }
    
    /**
     * Reads mei file from fileNameIn and outputs to the appropriate
     * midi-test file which needs to be created by the user.
     * @param fileNameIn
     * @throws InvalidMidiDataException 
     */
    public static void readWriteFile(String fileNameIn) throws InvalidMidiDataException {
        MeiSequence test = new MeiSequence(fileNameIn);
        String[] fileNameArray = fileNameIn.split("/");
        String fileName = fileNameArray[fileNameArray.length - 1].replaceAll("mei", "midi");
        MidiIO.write(test.getSequence(), "midi-test/CompleteExamples/"
                                        + fileName);
    }
    
    /**
     * Reads all mei files from dirNameIn and recursively calls any sub-directories
     * and all files are put into the appropriate user-made midi-test files.
     * @param dirNameIn
     * @param dirNameOut
     * @throws InvalidMidiDataException 
     */
    public static void readDirectory(String dirNameIn, String dirNameOut) throws InvalidMidiDataException {
        File dirNameFile = new File(dirNameIn);
        for(String filename : dirNameFile.list()) {
            File file = new File(filename);
            if(file.isDirectory()) {
                String path = file.getAbsolutePath();
                readDirectory(path, path.replaceAll("mei-test", "midi-test"));
            }
            if(!filename.contains(".mei")) {
               continue; //skip non mei files
            }
     
            System.out.println("Converting file " + filename);
            MeiSequence mei = new MeiSequence(dirNameIn + filename);
            MidiIO.write(mei.getSequence(), dirNameOut 
                                            + filename.replace("mei", "midi"));
        }    
    }
    
    //This is for testing purposes only to help find
    //strange bugs in mei encoding
    private static void findBugs(String fileName) {
        MeiDocument doc = MeiXmlReader.loadFile(fileName);
        List<MeiElement> measures = doc.getElementsByName("measure");
        int i = 1;
        for(MeiElement measure : measures) {
            System.out.println(measure + " " + measure.getAttribute("n"));
            List<MeiElement> notes = measure.getDescendantsByName("note");
            for(MeiElement note : notes) {
                String pname = note.getAttribute("pname");
                String oct = note.getAttribute("oct");
                String dur = note.getAttribute("dur");
                if(pname == null || oct == null || dur == null) {
                    System.out.println(note + " " + pname + " " + oct + " " + dur);
                }  
                //i++;
            }
        } 
    }
}
