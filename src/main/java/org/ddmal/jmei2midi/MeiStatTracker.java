/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.jmei2midi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//SHOULD PROBABLY CONNECT FILENAME TO INVALID INSTRUMENTS/TEMPOS
//ALSO CHECK CONTAINS IF NOT GOING TO ADD

/**
 * This class is used before the mei file parsing
 * and passed to the parse in order to keep track of any
 * invalid files and other strange occurences.
 * Processing happens within the running code and not in this class.
 * @author dinamix
 */
public class MeiStatTracker {
    //Maintain current filename being parsed
    private String fileName;
    
    //Stores the incorrect filename as key
    //and incorrect file info as value
    private HashMap<String,List<String>> incorrectFiles;

    /**
     * New MeiStatTracker which will keep track of invalid mei2midi inputs
     * such as invalid instruments and tempos.
     * @param filename 
     */
    public MeiStatTracker(String filename) {
        //MAY NOT NEED FILENAME WHEN USING FOR MULTIPLE FILES
        this.fileName = filename;
        incorrectFiles = new HashMap<>();
    }
    
    /**
     * 
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
    }
    
    /**
     * @return the incorrectFiles
     */
    public HashMap<String,List<String>> getIncorrectFiles() {
        return incorrectFiles;
    }
    
    /**
     * Add incorrect to the list of incorrect files.
     * @param incorrect
     */
    private void addIncorrectFile(String incorrect) {
        if(!incorrectFiles.containsKey(incorrect)) {
            incorrectFiles.put(fileName, new ArrayList<String>());
        }
    }
    
    /**
     * Add invalid to the list of invalid instruments.
     * @param invalid 
     */
    public void addInvalidInstrument(String invalid) {
        addStat("Instrument",invalid);
    }
    
    /**
     * Add invalid to the list of invalid tempos.
     * @param invalid 
     */
    public void addInvalidTempo(String invalid) {
        addStat("Tempo", invalid);
    }
    
    /**
     * Add a generic stat given the stat type.
     * This will valide the stat and the file name
     * to make sure they are not iterating more than once.
     * @param type
     * @param stat 
     */
    private void addStat(String type, String stat) {
        addIncorrectFile(this.getFileName());
        String newStat = type + ": " + stat;
        List<String> thisFile = incorrectFiles.get(fileName);
        if(!thisFile.contains(newStat)) {
            thisFile.add(newStat);
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
