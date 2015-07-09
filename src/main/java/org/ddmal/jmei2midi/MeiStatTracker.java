/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.jmei2midi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * MeiStatTracker stores a List<String> of all files that
 * have been processed together with HashMaps of
 * incorrectFiles, invalidInstrument and invalidTempos.
 * Each HashMap contains files as keys with the corresponding
 * incorrect List<String> as a value.
 * 
 * USAGE:
 * An MeiStatTracker should be instantiated before analyzing
 * a set of MEI files. It should then be passed through
 * the instantiation of an MeiSequence object.
 * Not passing a reference will result in a new ArrayList<String>
 * for the current file.
 * 
 * This class is used before the mei file parsing
 * and passed to the parse in order to keep track of any
 * invalid files and other strange occurences.
 * Processing happens within the running code and not in this class.
 * @author dinamix
 */
public final class MeiStatTracker {
    //Maintain current filename being parsed
    private String fileName;
    
    //Stores all file names regardless of if correct
    private final List<String> allFiles;
    
    //Stores the incorrect filename as key
    //and incorrect file info as value
    private final HashMap<String,List<String>> incorrectFiles;
    
    //Stores invalid instruments related to incorrect files
    private final HashMap<String,List<String>> invalidInstruments;
    
    //Stores invalid tempos related to incorrect files
    private final HashMap<String,List<String>> invalidTempos;

    /**
     * New MeiStatTracker which will keep track of invalid mei2midi inputs
     * such as invalid instruments and tempos. DEFAULT
     */
    public MeiStatTracker() {
        allFiles = new ArrayList<>();
        incorrectFiles = new HashMap<>();
        invalidInstruments = new HashMap<>();
        invalidTempos = new HashMap<>();
    }
    
    /**
     * New MeiStatTracker which will keep track of invalid mei2midi inputs
     * such as invalid instruments and tempos. WITH FILENAME
     * @param filename
     */
    public MeiStatTracker(String filename) {
        allFiles = new ArrayList<>();
        incorrectFiles = new HashMap<>();
        invalidInstruments = new HashMap<>();
        invalidTempos = new HashMap<>();
        setFileName(filename);
    }
    
    /**
     * @return the current filename
     */
    public String getFileName() {
        return fileName;
    }
    
    /**
     * Set new filename.
     * @param filename 
     */
    public void setFileName(String filename) {
        this.fileName = filename;
        if(!allFiles.contains(filename)) {
            allFiles.add(filename);
        }
    }
    
    /**
     * A List<String> of all files that this object has seen.
     * @return all filenames
     */
    public List<String> getAllFiles() {
        return allFiles;
    }
    
    /**
     * A HashMap of all incorrect files this object has seen.
     * @return the incorrectFiles
     */
    public HashMap<String,List<String>> getIncorrectFiles() {
        return incorrectFiles;
    }
    
    /**
     * A HashMap of all invalid instruments this object has seen.
     * Each list of invalid instruments (value) corresponds to the
     * appropriate file (key).
     * @return invalidInstruments
     */
    public HashMap<String,List<String>> getInvalidInstruments() {
        return invalidInstruments;
    }
    
    /**
     * A HashMap of all invalid tempos this object has seen.
     * Each list of invalid tempos (value) corresponds to the
     * appropriate file (key).
     * @return invalidTempos 
     */
    public HashMap<String,List<String>> getInvalidTempos() {
        return invalidTempos;
    }
    
    /**
     * Add invalid instrument for the current file.
     * @param invalid 
     */
    public void addInvalidInstrument(String invalid) {
        addStat("Instrument",invalid);
    }
    
    /**
     * Add invalid tempo for the current file.
     * @param invalid 
     */
    public void addInvalidTempo(String invalid) {
        addStat("Tempo", invalid);
    }
    
    /**
     * Add incorrect to each has as long as the incorrect file has
     * not already been added.
     * @param incorrect
     */
    private void addIncorrectFile(String incorrect) {
        if(!incorrectFiles.containsKey(incorrect)) {
            incorrectFiles.put(fileName, new ArrayList<String>());
            invalidInstruments.put(fileName, new ArrayList<String>());
            invalidTempos.put(fileName, new ArrayList<String>());
        }
    }
    
    /**
     * Add a generic stat given the stat type.
     * This will validate the stat and the file name
     * to make sure they are not iterating more than once.
     * @param type
     * @param stat 
     */
    private void addStat(String type, String stat) {
        addIncorrectFile(this.getFileName());
        List<String> thisHash = getStatHash(type).get(fileName);
        List<String> thisFile = incorrectFiles.get(fileName);
        String newStat = type + ": " + stat;

        if(!thisFile.contains(newStat)) {
            thisFile.add(newStat);
        }
        if(!thisHash.contains(stat)) {
            thisHash.add(stat);
        }
    }
    
    /**
     * Add a specific stat and type to its appropriate hash.
     * This validates file existence from the call in addStat().
     * @param type
     * @param stat 
     */
    private HashMap<String,List<String>> getStatHash(String type) {
        switch (type) {
            case "Tempo":
                return invalidTempos;
            case "Instrument":
                return invalidInstruments;
            default :
                return new HashMap<>();
        }
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(String key : incorrectFiles.keySet()) {
            sb.append("Invalid File: ").append(key).append("\n");
            for(String value : incorrectFiles.get(key)) {
                sb.append(value).append("\n");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
