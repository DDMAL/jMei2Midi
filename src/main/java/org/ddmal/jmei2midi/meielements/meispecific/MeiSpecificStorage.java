/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.jmei2midi.meielements.meispecific;

import java.util.ArrayList;
import java.util.List;

/**
 * Another example of how to store non-midi mei data that could be used with
 * or without the supplied factory pattern within this package. Currently, if
 * an MeiSequence is made, this will be able to count and return all
 * gracenotes/gracechords within the mei document.
 * 
 * @author Tristano Tenaglia
 */
public class MeiSpecificStorage {
    
    private final List<MeiSpecific> graceNoteList;
    
    public MeiSpecificStorage() {
        graceNoteList = new ArrayList<>();
    }
    
    public int getNumberOfGraceNotes() {
        return graceNoteList.size();
    }
    
    public void addGraceNote(MeiSpecific graceNote) {
        graceNoteList.add(graceNote);
    }
}
