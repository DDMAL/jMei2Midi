/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.jmei2midi;

import ca.mcgill.music.ddmal.mei.MeiDocument;
import ca.mcgill.music.ddmal.mei.MeiElement;
import ca.mcgill.music.ddmal.mei.MeiXmlReader;
import ca.mcgill.music.ddmal.mei.MeiXmlReader.MeiXmlReadException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.sound.midi.InvalidMidiDataException;
import org.ddmal.midiUtilities.MidiIO;


/**
 * Command Line UI to convert either a file or a folder of MEI files to MIDI.
 * A folder will be traversed and will convert any file
 * ending with .mei into a .midi file.
 * If a folder input is used, then a folder output must be used.
 * @author dinamix
 */
public class Main {
    
    //This is the automatic name of the jar build
    private static final String jarName = "jMei2Midi-1.0-jar-with-dependencies.jar";
    
    /**
     * Start Command line from here. 
     */
    public static void main(String[] args) {
        List<String> errorLog = new ArrayList<>();
        if(args.length == 2) {
            System.out.println("Converting from " + args[0] + " to " + args[1]);
            checkFileType(args,errorLog);
            if(errorLog.isEmpty()) {
                System.out.println("Finished Successfully!");
            }
            else {
                System.out.println(Arrays.toString(errorLog.<String>toArray()));
            }
        }
        else {
            System.err.println("Input should be of type : "
                  + "java -jar " + jarName + " \"filenamein\" \"filenameout\"");
        }
    }
    
    /**
     * Check if the input is a file or a directory.
     * @param errorLog List(String) to log errors.
     */
    public static void checkFileType(String[] args, List<String> errorLog) {
        File args0 = new File(args[0]);
        File args1 = new File(args[1]);
        if(args0.isFile()) {
            readWriteFile(args[0],args[1],errorLog);
        }
        else if(args0.isDirectory() && args1.isDirectory()) {
            readDirectory(args[0], args[1],errorLog);
        }
        else {
            errorLog.add("Input should be of type : "
                  + "java -jar " + jarName + " \"filenamein\" \"filenameout\"");
        }
    }
    
    /**
     * Reads mei file from fileNameIn and outputs to the appropriate
     * midi-test file in the same dir as the .rar.
     * @param fileNameIn File name to be converted.
     * @param fileNameOut File name converted to.
     * @param errorLog List(String) to log errors.
     */
    public static void readWriteFile(String fileNameIn, String fileNameOut, List<String> errorLog) {
        MeiSequence test;
        try {
            test = new MeiSequence(fileNameIn);
            String[] fileNameArray = fileNameIn.split("/");
            String fileName = fileNameArray[fileNameArray.length - 1].replaceAll("mei", "midi");
            MidiIO.write(test.getSequence(), fileNameOut);
        }
        catch(InvalidMidiDataException | MeiXmlReadException ex) {
            errorLog.add("Error found in file : " + fileNameIn + ". Error Message : " + ex.getMessage());
        }
    }
    
    /**
     * Reads all mei files from dirNameIn and recursively calls any sub-directories
     * and all files are put into the appropriate user-made midi-test files.
     * @param dirNameIn Directory name to be converted.
     * @param dirNameOut Directory to be converted to.
     * @param errorLog List(String) to log errors.
     */
    public static void readDirectory(String dirNameIn, String dirNameOut, List<String> errorLog) {
        List<Path> meiList;
        try {
            meiList = Files.walk(Paths.get(dirNameIn))
                           .filter(name -> name.toString().endsWith(".mei"))
                           .collect(Collectors.toList());
        }
        catch(IOException ioe) {
            //Exception will only be thrown at the start of Paths.get()
            errorLog.add("Error with starting file in : " + dirNameIn);
            meiList = new ArrayList<>();
        }
        
        for(Path meiPath : meiList) {
            String filename = meiPath.getFileName().toString();
            System.out.println("Converting file " + filename);
            
            MeiSequence mei;
            try {
                mei = new MeiSequence(dirNameIn + filename);
            }
            catch(InvalidMidiDataException | MeiXmlReadException ex) {
                errorLog.add("Error found in file : " + filename 
                           + ". Error Message : " + ex.getMessage());
                continue;
            }
            
            MidiIO.write(mei.getSequence(), dirNameOut + File.separator
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
