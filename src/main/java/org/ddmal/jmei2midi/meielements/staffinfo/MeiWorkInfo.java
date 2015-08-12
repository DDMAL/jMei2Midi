/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.jmei2midi.meielements.staffinfo;

import org.ddmal.jmei2midi.meielements.general.MeiMdiv;

import ca.mcgill.music.ddmal.mei.MeiElement;

import java.util.List;
import java.util.Map;

import javax.sound.midi.Sequence;

import org.ddmal.jmei2midi.MeiStatTracker;

/**
 * Used to build meiwork object and populate the works hashmap.
 * @author Tristano Tenaglia
 */
public class MeiWorkInfo extends MeiStaffBuilder{
    
    /**
     * The mei work element to be processed
     */
    private MeiElement work;
    
    /**
     * Populates the work HashMap with the given mei work.
     * If an n attribute is not provided then we know we have more than
     * one work and so more than one mdiv.
     * If no n attribute is found, then we only have 1 mdiv.
     * @param sequence the sequence to be added to
     * @param stats the stats to be added to
     * @param staffs not used
     * @param works works to be added to
     * @param currentMdiv not used
     * @param work work to be parsed
     */
    public MeiWorkInfo(Sequence sequence,
                       MeiStatTracker stats,
                       Map<Integer, MeiStaff> staffs, 
                       Map<Integer, MeiWork> works, 
                       MeiMdiv currentMdiv, 
                       MeiStaff currentStaff,
                       MeiElement work) {
        super(sequence, stats, staffs, works, currentMdiv, currentStaff, work);
        this.work = work;
        MeiWork thisWork;
        int n = 1;
        String nString = this.work.getAttribute("n");
        //If work does not have n then we assume 
        //that n = 1
        if(!attributeExists(nString)) {
            thisWork = createMeiWork(this.work,n);
            works.put(n, thisWork);
        }
        //If we have n="x" then we have >=1 movement
        //and so we get the new n
        else {
            n = Integer.parseInt(nString);
            thisWork = createMeiWork(this.work,n);
            works.put(n, thisWork);
        }
    }
    
    /**
     * Creates an MeiWork object with the key, meter, tempo and perfMedium tags.
     * @param work mei work element to be processed
     * @param n n value of work to be obtained
     * @return the new meiwork object from the info in the mei work element
     */
    private MeiWork createMeiWork(MeiElement work, int n) {
        MeiWork newWork = new MeiWork(n);
        List<MeiElement> workBuild = work.getChildren();
        for(MeiElement ele : workBuild) {
            switch (ele.getName()) {
                case "key":
                    newWork.setKeyName(ele.getAttribute("pname"));
                    newWork.setKeyMode(ele.getAttribute("mode"));
                    break;
                case "meter":
                    newWork.setMeterCount(ele.getAttribute("count"));
                    newWork.setMeterUnit(ele.getAttribute("unit"));
                    break;
                case "tempo":
                    newWork.setTempo(ele.getValue());
                    break;
                case "perfMedium":
                    processPerfMedium(ele,newWork);
                    break;
            }
        }
        return newWork;
    }
    
    /**
     * Splits up perfMedium element into castList and instrumentation
     * elements to get descendant castItem and instrVoice in order.
     * @param perfMedium mei perfmedium element to be processed
     * @param newWork meiwork object to be added to
     */
    private void processPerfMedium(MeiElement perfMedium, MeiWork newWork) {
        //need to add cast list here
        for(MeiElement child : perfMedium.getChildren()) {
            switch (child.getName()) {
                case "castList":
                    populateInstrVoice(child.getDescendantsByName("castItem"), newWork);
                    break;
                case "instrumentation":
                    populateInstrVoice(child.getDescendantsByName("instrVoice"),newWork);
                    break;
            }
        }
    }
    
    /**
     * Iterates through all instrVoice tags within a perfMedium/Instrumentation
     * tag.
     * If no n attribute is given, then sequential iteration is assumed.
     * @param instrList all mei instrvoice elements within a perfmedium mei element
     * @param newWork meiwork object to be added to
     */
    private void populateInstrVoice(List<MeiElement> instrList, MeiWork newWork) {
        int n = 1;
        //If we have cast list then start from that point
        if(newWork.getInstrVoice().size() > 0) {
            n = newWork.getInstrVoice().size() + 1;
        }
        for(MeiElement instrVoice : instrList) {
            String value = instrVoice.getValue();
            String nString = instrVoice.getAttribute("n");
            if(attributeExists(nString)) {
                n = Integer.parseInt(nString);
            }
            newWork.addInstrVoice(n, value);
            n++;
        }
    }
    
}
