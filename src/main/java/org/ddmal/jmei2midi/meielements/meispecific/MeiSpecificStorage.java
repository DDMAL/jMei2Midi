/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.jmei2midi.meielements.meispecific;

import ca.mcgill.music.ddmal.mei.MeiElement;

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
    
    private final List<MeiGraceNote> graceNoteList;
    private final List<MeiSlurNote> slurNoteList;
    private final List<MeiSpecific> generalList;
    
    public MeiSpecificStorage() {
        generalList = new ArrayList<>();
        slurNoteList = new ArrayList<>();
        graceNoteList = new ArrayList<>();
    }

    public void addMeiElement(MeiElement element, String key, String value, String location) {
        generalList.add(new MeiSpecific(element,key,value,location));
    }

    public List<MeiGraceNote> getGraceNoteList() {
        return graceNoteList;
    }
    
    public void addGraceNote(MeiGraceNote graceNote) {
        graceNoteList.add(graceNote);
    }

    public List<MeiSlurNote> getSlurNoteList() {
        return slurNoteList;
    }

    public void addSlurNote(MeiSlurNote slurNote) {
        slurNoteList.add(slurNote);
    }
}
