/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.jmei2midi.meielements.staffinfo;

import org.ddmal.jmei2midi.meielements.general.MeiMdiv;
import ca.mcgill.music.ddmal.mei.MeiElement;
import java.util.HashMap;
import java.util.List;
import javax.sound.midi.Sequence;
import org.ddmal.jmei2midi.MeiStatTracker;

/**
 *
 * @author dinamix
 */
public class MeiWorkInfo extends MeiStaffBuilder{
    
    private MeiElement work;
    
    /**
     * Populates the work HashMap.
     * If an n attribute is not provided then we know we have more than
     * one work and so more than one mdiv.
     * If no n attribute is found, then we only have 1 mdiv.
     * @param sequence
     * @param stats
     * @param staffs
     * @param works
     * @param currentMdiv
     * @param work 
     */
    public MeiWorkInfo(Sequence sequence,
                       MeiStatTracker stats,
                       HashMap<Integer, MeiStaff> staffs, 
                       HashMap<Integer, MeiWork> works, 
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
     * Creates an MeiWork object with the key, meter, tempo and instrVoice tags.
     * @param work
     * @return 
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
     * @param element
     * @param newWork 
     */
    private void processPerfMedium(MeiElement element, MeiWork newWork) {
        //need to add cast list here
        for(MeiElement child : element.getChildren()) {
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
     * @param instrList
     * @param newWork 
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
            newWork.addInstrVoice(n, instrVoice.getValue());
            n++; //Double check this in testing
                 //maybe not the best coding practice
        }
    }
    
}
