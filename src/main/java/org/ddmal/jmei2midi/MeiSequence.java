/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.jmei2midi;

import org.ddmal.jmei2midi.meielements.MeiWork;
import org.ddmal.jmei2midi.meielements.MeiStaff;
import org.ddmal.jmei2midi.meielements.MeiMeasure;
import org.ddmal.jmei2midi.meielements.MeiRepeat;
import ca.mcgill.music.ddmal.mei.MeiAttribute;
import ca.mcgill.music.ddmal.mei.MeiDocument;
import ca.mcgill.music.ddmal.mei.MeiElement;
import ca.mcgill.music.ddmal.mei.MeiXmlReader;
import java.util.HashMap;
import java.util.List;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;
import org.ddmal.midiUtilities.ConvertToMidi;
import org.ddmal.midiUtilities.ConvertToMidiWithStats;
import org.ddmal.midiUtilities.MidiBuildMessage;
/**
 *
 * @author dinamix
 */
public class MeiSequence {
    
    private MeiDocument document;
    
    
    private Sequence sequence;
    
    
    private MeiStaff currentStaff;
    private HashMap<Integer,MeiStaff> staffs; //staffs store in hashmap
                                              //for easier access throughout
    
    //This will contain metdata and be related to mdivs and scoreDefs
    //If no other information is given then defaults will be used
    private int currentMovement; //this will hold the current mdiv number
    private HashMap<Integer,MeiWork> works; //this accounts for changes in <scoreDef>
                                            //that may or may not need to be global
    
    //Stores info specific to a certain measure such as
    //accidentals and tupletSpans
    private MeiMeasure currentMeasure;
    
    
    //Store all mei repeat starts and ends
    private MeiRepeat repeats;
    
    
    //This allows a sequence to generate some mei stats
    //Such as invalid tempo and invalid instruments
    private MeiStatTracker stats;
    
    /**
     * Constructor that creates a MIDI Sequence given an MEI filename.
     * @param filename  
     * @throws javax.sound.midi.InvalidMidiDataException 
     */
    public MeiSequence(String filename) throws InvalidMidiDataException {
        
        //Read in MEI XML file
        document = MeiXmlReader.loadFile(filename);
        
        //Instantiate staffs as ArrayList
        staffs = new HashMap<>();
        currentStaff = new MeiStaff(0); //in case there are no n attributes for 
                                        //staffs within measures
        
        //Create new defaults array for default values
        //So far we need 5 values for:
        //tempo, meter.count, meter.unit, key.sig and key.mode
        works = new HashMap<>();
        
        //Instantiate new MeiRepeat to store all repeats
        repeats = new MeiRepeat();
        
        //Create a new MeiStatTracker
        stats = new MeiStatTracker(filename);
        
        //Turn the document into a MIDI sequence
        //It is returned in the class sequence variable
        documentToSequence();
    }
    
    /**
     * Constructor that creates a MIDI Sequence given an MEI filename and
     * also keeps track of some mei stats using MeiStatTracker reference.
     * @param filename 
     * @param stats 
     * @throws javax.sound.midi.InvalidMidiDataException 
     */
    public MeiSequence(String filename,
                       MeiStatTracker stats) throws InvalidMidiDataException {
        
        //Read in MEI XML file
        document = MeiXmlReader.loadFile(filename);
        
        //Instantiate staffs as ArrayList
        staffs = new HashMap<>();
        currentStaff = new MeiStaff(0); //in case there are no n attributes for
                                        //staffs within measures
        
        //Create new defaults array for default values
        //So far we need 5 values for:
        //tempo, meter.count, meter.unit, key.sig and key.mode
        works = new HashMap<>();
        
        //Instantiate new MeiRepeat to store all repeats
        repeats = new MeiRepeat();
        
        //Update a stats object by reference
        this.stats = stats;
        stats.setFileName(filename);
        
        //Turn the document into a MIDI sequence
        //It is returned in the class sequence variable
        documentToSequence();
    }
    
    /**
     * Returns Java MIDI Sequence created from MeiSequence MEI file input.
     * @return 
     */
    public Sequence getSequence() {
        return this.sequence;
    }
    
    /**
     * RETURN STRING[] DEFAULTS FOR TESTING PURPOSES
     * @return 
     */
    public HashMap<Integer,MeiWork> getWorks() {
        return this.works;
    }
    
    /**
     * RETURN HASHMAP\<MEISTAFF\> STAFFS FOR TESTING PURPOSES
     * @return 
     */
    public HashMap<Integer,MeiStaff> getStaffs() {
        return this.staffs;
    }
    
    /**
     * Returns MeiStatTracker created from any invalid data needed.
     * @return 
     */
    public MeiStatTracker getStats() {
        return this.stats;
    }

    /**
     * Converts given MEI document to MIDI sequence object.
     * mei is the root element of any mei document.
     * mei children will be meiHead and music.
     * meiHead is for tempo and music is for notes and other changes.
     * @throws javax.sound.midi.InvalidMidiDataException
     */
    private void documentToSequence() throws InvalidMidiDataException {
        try {
            sequence = new Sequence(Sequence.PPQ, 256);
        }
        catch(InvalidMidiDataException imde) {
            throw new InvalidMidiDataException(
                    "Problem with instantiating Java Sequence.");
        }
        MeiElement mei = document.getRootElement();
        for(MeiElement element : mei.getChildren()) {
            recursiveDFS(element);//<meihead> and <music> will be passed
        }
    }
    
    /**
     * Recursive DFS through MEI document.
     * Sequentially goes through all tags 
     * then either processParent() or processElement() is called.
     * @param root 
     */
    private void recursiveDFS(MeiElement root) {
        processElement(root);
    }
    
    /**
     * Each MEI element processed accordingly.
     * Some elements may only be processed within other elements
     * such as a note.
     * @param element can be any MEI tag
     */
    //COULD TEST WITH SWITCH STATEMENT FOR NEATNESS
    //would need to check if switch uses .equals() or == (probably .equals())
    private void processElement(MeiElement element) {
        //See if this element is a copy and then process accordingly
        checkCopy(element);
        
        //Get children of meihead which will be needed for workDesc
        if(element.getName().equals("meiHead")) {
            processParent(element);
        }
        //Get children of workDesc which will be work
        else if(element.getName().equals("workDesc")) {
            processParent(element);
        }
        //Process work by creatin MeiWork objects
        //and populating the works hashmap
        //This is used for default settings for each mdiv
        else if(element.getName().equals("work")) {
            processWork(element);
        }
        //Get children of mdiv which will probably be <body>
        else if(element.getName().equals("music")) {
            processParent(element);
        }
        //Get children of mdiv which will probably be <mdiv>
        else if(element.getName().equals("body")) {
            processParent(element);
        }
        //Get children of mdiv which will probably be <score>
        else if(element.getName().equals("mdiv")) {
            processMdiv(element);
        }
        //Get children of parts which will probably be <part>
        else if(element.getName().equals("parts") ||
                element.getName().equals("part")) {
            processParent(element);
        }
        //Get children of score which will probably be <scoreDef>
        //or <section>
        else if(element.getName().equals("score")) {
            processParent(element);
        }
        //Explicitly process scoreDef so that we maintain the
        //meter count between staffs.
        else if(element.getName().equals("scoreDef")) {
            processScoreDef(element);
        }
        //Get children of staffGrp which will probably be <staffDeff>
        else if(element.getName().equals("staffGrp")) {
            processParent(element);
        }
        //PROCESS STAFFDEF WHICH WILL CREATE NEW MIDI CHANNEL
        //If no instrument is found, then maybe no need to change
        else if(element.getName().equals("staffDef")) {
            processStaffDef(element);
        }
        //Get children of section which will probably be <measure>
        else if(element.getName().equals("section")) {
            processParent(element);
        }
        //Get children of ending which will probably be measure
        //And process to ensure that repeat is dealth with
        else if(element.getName().equals("ending")) {
            processEnding(element);
        }
        //Get children of measure which will probably be <staff>
        else if(element.getName().equals("measure")) {
            processMeasure(element);
        }
        //Get children of staff which will probably be <layer>
        //If staff has more than 2 children, need to consider offset
        //Check staff n attribute and then pass on MeiStaff
        else if(element.getName().equals("staff")) {
            processStaff(element);
        }
        //May contain layer or staff and
        //may be contained by measure or
        else if(element.getName().equals("ossia")) {
            processParent(element);
        }
        //Get children of layer which will be note, rest or mRest
        //Layers within a staff will all start at the same tick
        else if(element.getName().equals("layer")) {
            processLayer(element);
        }
        else if(element.getName().equals("beam")) {
            //Get children of layer which will be note, rest or mRest
        }
        else if(element.getName().equals("chord")) {
            //Get children of layer which will be note, rest or mRest
            //need to keep track of duration for each note
        }
        else if(element.getName().equals("note")) {
            //Process note to MIDI
            //Consider accidentals and key sig
            //(n = natural, s = sharp, f = flat) 
            //accid/accid.ges overrules key sig
        }
        else if(element.getName().equals("rest")) {
            //Process rest to MIDI
        }
        else if(element.getName().equals("mRest")) {
            //Complete measure rest
            //use meter to calculate full measure rest
        }
        else if(element.getName().equals("keySig")) {
            //change in keySig in MeiStaff
        }
    }
    
    /**
     * Process a generic element(parent) and iteratively pass children to
     * processElement(child).
     * @param parent 
     */
    private void processParent(MeiElement parent) {
        for(MeiElement child : parent.getChildren()) {
            processElement(child);
        }
    }
    
    /**
     * Populates the work HashMap.
     * If an n attribute is not provided then we know we have more than
     * one work and so more than one mdiv.
     * If no n attribute is found, then we only have 1 mdiv.
     * @param work 
     */
    private void processWork(MeiElement work) {
        MeiWork thisWork;
        int n = 1;
        String nString = work.getAttribute("n");
        //If work does not have n then we assume 
        //that n = 1
        if(!attributeExists(nString)) {
            thisWork = createMeiWork(work,n);
            works.put(n, thisWork);
        }
        //If we have n="x" then we have >=1 movement
        //and so we get the new n
        else {
            n = Integer.parseInt(nString);
            thisWork = createMeiWork(work,n);
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
            if(ele.getName().equals("key")) {
                newWork.setKeyName(ele.getAttribute("pname"));
                newWork.setKeyMode(ele.getAttribute("mode"));
            }
            else if(ele.getName().equals("meter")) {
                newWork.setMeterCount(ele.getAttribute("count"));
                newWork.setMeterUnit(ele.getAttribute("unit"));
            }
            else if(ele.getName().equals("tempo")) {
                newWork.setTempo(ele.getValue());
            }
            else if(ele.getName().equals("perfMedium")) {
                processPerfMedium(ele,newWork);
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
    
    /**
     * Change global movement if n attribute is given in mdiv.
     * @param mdiv 
     */
    private void processMdiv(MeiElement mdiv) {
        int n = 1;
        String nString = mdiv.getAttribute("n");
        if(attributeExists(nString)) {
            n = Integer.parseInt(nString);
        }
        currentMovement = n;
        processParent(mdiv);
    }
    
    /**
     * Stores meter.count, meter.unit and key.sig in String[] defaults.
     * These will be used for staffDef elements as default values.
     * Will replace metaData if needed.
     * @param scoreDef passed scoreDef tag
     */
    private void processScoreDef(MeiElement scoreDef) {
        //Keep default key.sig and then use
        //staff def key.sig on other staffs
        MeiWork work = works.get(currentMovement);
        String count = scoreDef.getAttribute("meter.count");
        String unit = scoreDef.getAttribute("meter.unit");
        String keysig = scoreDef.getAttribute("key.sig");
        String keymode = scoreDef.getAttribute("key.mode");
        if(attributeExists(count)) {
            work.setMeterCount(count);
        }
        if(attributeExists(unit)) {
            work.setMeterUnit(unit);
        }
        if(attributeExists(keysig)) {
            work.setKeysig(keysig);
        }
        if(attributeExists(keymode)) {
            work.setKeyMode(keymode);
        }
        //For a scoreDef change randomly in the file
        //If staffs not empty and all new attributes are non-null, 
        //then update them.
        //If they are empty, then it will be created in processStaffDef()
        if(!staffs.isEmpty() &&
           (attributeExists(count) || attributeExists(unit) ||
            attributeExists(keysig) || attributeExists(keymode))) {
            updateStaffs(work);
        }
        processParent(scoreDef);
    }
    
    /**
     * New scoreDef found during the piece will update all defined
     * staffs accordingly.
     */
    private void updateStaffs(MeiWork work) {
        String count = work.getMeterCount();
        String unit = work.getMeterUnit();
        String tempo = work.getTempo();
        String keysig = work.getKeysig();
        String keymode = work.getKeyMode();
        //Update each staff accordingly
        for(Integer i : staffs.keySet()) {
            //Optimization to check if there was a change
            //If not then don't send another midi message
            if(!checkCopy(staffs.get(i), count, unit, staffs.get(i).getLabel(), keysig, keymode, tempo)) {
                updateMeiStaff(staffs.get(i), 
                                  count,
                                  unit,
                                  tempo,
                                  null, //label not in MeiWork
                                  keysig,
                                  keymode);
            }
        }
       
        //@TODO
        //At the end we need to create or change MIDI tracks
        //@TODO
    }
    
    /**
     * THIS MAY NOT BE NECESSARY, LEAVING IT HERE FOR NOW
     * Process a staffGrp element such that a staffDef will define
     * a new staff and a staffGrp will define a group of staffs
     * with the same instrument.
     * @param staffGrp 
     */
    private void processStaffGrp(MeiElement staffGrp) {
        String uniqueID = staffGrp.getId();
        for(MeiElement staffElement : staffGrp.getChildren()) {
            if(staffElement.getName().equals("staffDef")) {
                processStaffDef(staffElement);
            }
            else if(staffElement.getName().equals("staffGrp")) {
                processStaffGrp(staffElement);
            }
        }
    }
    
    //May need to add instrDef but so far this doesn't seem necessary
    //as instrDef can give random midi data for channels (Mozart Quintet)
    private void processStaffDef(MeiElement staffDef) {
        MeiStaff thisStaff;       
        
        //Set up necessary attributes
        int n = 1; // n = 1 in case it does not exist to place in hash
        String nString = staffDef.getAttribute("n");
        String count = staffDef.getAttribute("meter.count");
        String unit = staffDef.getAttribute("meter.unit");
        String label = staffDef.getAttribute("label");
        String keysig = staffDef.getAttribute("key.sig");
        String keymode = staffDef.getAttribute("key.mode");
        String tempo = works.get(currentMovement).getTempo();
        
        //CASE 1: N ATTRIBUTE DNE
        //Check if n attribute exists
        if(attributeExists(nString)) {
            n = Integer.parseInt(nString);
        }
        /*If n does not exist, then assume we only have 1 staff and create it
        //THIS SHOULD ONLY HAPPEN IF WE HAVE 1 STAFF
        //AND EVEN THEN N SHOULD BE 1
        //n = 0 if n attribute DNE
        else {
            thisStaff = createMeiStaff(n, count, unit, label, keysig, keymode);
            staffs.put(n, thisStaff);
        }*/
        
        //CASE 2: N ATTRIBUTE DOES EXIST
        //If staff not created yet, then instantiate it
        //and put it into HashMap<MeiStaff>
        if(!staffs.containsKey(n)) {
            thisStaff = createMeiStaff(n, count, unit,
                                        tempo, label, keysig, keymode);
            staffs.put(n, thisStaff);
        }
        //If staff already created and all values are non-null
        //then check it and make appropriate changes
        //Notes: Tempo not checked because will be checked in scoreDef
        //       Null check done for optimization
        //       If they are all null and already contained then don't do anything
        else {
            thisStaff = staffs.get(n);
            if(!checkCopy(thisStaff,count,unit,label,keysig,keymode,tempo)) {
                thisStaff = updateMeiStaff(thisStaff, count, unit, 
                                           tempo, label, keysig, keymode);
                staffs.replace(n, thisStaff);
            }
        }
        //@TODO
        //At the end we need to create or change MIDI tracks
        //@TODO
    }
    
    /**
     * Optimization method to check if a current staff and a new
     * mei staff element are the same or if the new element
     * has only unnecessary information.
     * If they are the same the then return true or else it returns false.
     * @param thisStaff
     * @param count
     * @param unit
     * @param label
     * @param keysig
     * @param keymode
     * @param tempo
     * @return 
     */
    private boolean checkCopy(MeiStaff thisStaff,
                              String count,
                              String unit,
                              String label,
                              String keysig,
                              String keymode,
                              String tempo) {
        return (thisStaff.getMeterCount().equals(count) || count == null) &&
                (thisStaff.getMeterUnit().equals(unit) || unit == null) &&
                (thisStaff.getLabel().equals(label) || label == null) &&
                (thisStaff.getKeysig().equals(keysig) || keysig == null) &&
                (thisStaff.getKeymode().equals(keymode) || keymode == null) &&
                (thisStaff.getTempo().equals(tempo) || tempo == null);
    }
    
    /**
     * NOTE STATS ARE USED IN HERE
     * MeiStaff object populated with new values in new staffDef.
     * When a staff already exists but a new staffDef is found with the
     * same n attribute then the given attributes are changed accordingly.
     * Since MeiStaff has already been created,
     * we know that thisStaff will already either have
     * old attributes or default attributes.
     * @param thisStaff
     * @param count
     * @param unit
     * @param label
     * @param keysig
     * @param keymode
     * @return newly populated MeiStaff object
     */
    private MeiStaff updateMeiStaff(MeiStaff thisStaff,
                                      String count,                                     
                                      String unit,
                                      String tempo,
                                      String label,
                                      String keysig,
                                      String keymode) {
        //Stats checking
        ConvertToMidiWithStats.tempoToBpm(tempo, stats);
        if(attributeExists(tempo)) {
            thisStaff.setTempo(tempo);
        }
        if(attributeExists(count)) {
            thisStaff.setMeterCount(count);
        }
        if(attributeExists(unit)) {
            thisStaff.setMeterUnit(unit);
        }
        //Check if label exists and if it is a valid instrument
        int midiInstr = ConvertToMidiWithStats.instrToMidi(label,stats);
        int percInstr = (midiInstr > -1) ? 0 : ConvertToMidiWithStats.instrToPerc(label,stats);
        if(midiInstr > -1) {
            thisStaff.setLabelInstr(label,midiInstr);
        }
        else if(percInstr > -1) {
            thisStaff.setLabelPerc(label, percInstr);
        }
        else {
            thisStaff.setLabel(processNewLabel(thisStaff.getN(),label));
        }
        if(attributeExists(keysig)) {
            thisStaff.setKeysig(keysig);
        }
        if(attributeExists(keymode)) {
            thisStaff.setKeymode(keymode);
        }
        buildMidiTrack(thisStaff);
        return thisStaff;
    }
    
    /**
     * NOTE STATS ARE USED IN HERE
     * MeiStaff object created when n attribute is not given.
     * This should not happen but maybe with only 1 staff
     * the composer may assume that there is no n attribute.
     * @param n
     * @param count
     * @param unit
     * @param label
     * @param keysig
     * @param keymode
     * @return new staff element with filled in values
     */
    private MeiStaff createMeiStaff(int n,
                                    String count,
                                    String unit,
                                    String tempo,                                    
                                    String label,
                                    String keysig,
                                    String keymode) {
        MeiWork work = works.get(currentMovement);
        MeiStaff newStaff = new MeiStaff(n);
        
        ConvertToMidiWithStats.tempoToBpm(tempo, stats); //check invalid
        if(attributeExists(tempo)) {
            newStaff.setTempo(tempo);
        }
        if(attributeExists(count)) {
            newStaff.setMeterCount(count);
        }
        else {
            newStaff.setMeterCount(work.getMeterCount());
        }
        if(attributeExists(unit)) {
            newStaff.setMeterUnit(unit);
        }
        else {
            newStaff.setMeterUnit(work.getMeterUnit());
        }
        //Check if label exists and if it is a valid instrument
        int midiInstr = ConvertToMidiWithStats.instrToMidi(label,stats);
        int percInstr = (midiInstr > -1) ? 0 : ConvertToMidiWithStats.instrToPerc(label,stats);
        if(midiInstr > -1) {
            newStaff.setLabelInstr(label,midiInstr);
        }
        else if(percInstr > -1) {
            newStaff.setLabelPerc(label, percInstr);
        }
        //or else we use works defaults
        else {
            newStaff.setLabel(processNewLabel(n,label));
        }
        if(attributeExists(keysig)) {
            newStaff.setKeysig(keysig);
        }
        else {
            newStaff.setKeysig(work.getKeysig());
        }
        if(attributeExists(keymode)) {
            newStaff.setKeymode(keymode);
        }
        else if(attributeExists(work.getKeyMode())) {
            newStaff.setKeymode(work.getKeyMode());
        }
        buildMidiTrack(newStaff);
        return newStaff;
    }
    
    /**
     * WARNING
     * ORDER OF IF STATEMENTS DOES MATTER FOR CASE OF MULTI-STAFF INSTRUMENTS.
     * Will check if piano first or else will take from works.
     * If nothing in works but already has a value != "default
     * then it will take the new value.
     * Or else it will return Voice or if there are 2 staffs, Piano.
     * @param n
     * @param label
     * @return 
     */
    private String processNewLabel(int n,String label) {
        //Check if there is a work
        MeiWork work = works.get(currentMovement);
        if(work != null) {
            HashMap<Integer,String> instrVoice = work.getInstrVoice();
            //If there is no label and the previous label is a multi-staff
            //instrument then copy the previous instrument
            if((label == null) &&
                staffs.containsKey(n-1) &&
                (staffs.get(n-1).getLabel().toLowerCase().contains("piano") ||
                staffs.get(n-1).getLabel().toLowerCase().contains("organ") ||
                staffs.get(n-1).getLabel().toLowerCase().contains("harpsi"))) {
                for(int i = n-1; i >= 0; i--) {
                    if(instrVoice.containsKey(i)) {
                        return instrVoice.get(i);
                    }
                }
            }
            //Check if there is a valid instrVoice
            else if(instrVoice.containsKey(n)) {
                String instr = instrVoice.get(n);
                boolean instrValid = ConvertToMidi.instrToMidi(instr) > -1;
                if(instrValid) {
                    return instr;
                }
            }
            //Or if we previously had an instrument here
            else if(staffs.containsKey(n) &&
                    !staffs.get(n).getLabel().equals("default")) {
                return staffs.get(n).getLabel();
            } 
        }   
        //This is if nothing is found
        //Default is voice
        //Piano is used if we have only 2 staffs
        String instrument = "Voice";
        if(staffs.size() == 2) {
            instrument = "Piano";
        }
        return instrument;
    }
    
    /**
     * Builds a new midi track if not already in sequence or else
     * it will fetch the old track based on the given staff.
     * Midi events are then added through the addEventsToTrack().
     * @param staff 
     */
    private void buildMidiTrack(MeiStaff staff) {
        Track newTrack;
        //The info that will be changed
        int thisN = staff.getN();
        int thisChannel = staff.getChannel();
        long thisTick = staff.getTick();
        String thisKey = staff.getKeysig();
        String thisQuality = staff.getKeymode();
        int thisBpm = staff.getBpm();
        int thisProgram = staff.getMidiLabel();
        
        //If tracks does not have this n track
        //Then create a new track for the sequence
        if(sequence.getTracks().length < thisN) {
            newTrack = sequence.createTrack();
        }
        else {
            newTrack = sequence.getTracks()[thisN-1];
        }
        addEventsToTrack(newTrack, 
                         thisChannel, 
                         thisTick, 
                         thisKey, 
                         thisQuality, 
                         thisProgram, 
                         thisBpm);
    }
    
    /**
     * Sends appropriate midi events to the specified track.
     * This could be optimized to check previous events and not send
     * if they are the same but they may be tricky.
     * @param track
     * @param channel
     * @param tick
     * @param key
     * @param quality
     * @param midiLabel
     * @param bpm 
     */
    private void addEventsToTrack(Track track,
                                  int channel,
                                  long tick,
                                  String key,
                                  String quality,
                                  int midiLabel,
                                  int bpm) {
        try {
            track.add(MidiBuildMessage.createKeySignature(key, quality, tick));
            track.add(MidiBuildMessage.createProgramChange(midiLabel, tick, channel));
            track.add(MidiBuildMessage.createTrackTempo(bpm, tick));
        }
        catch(InvalidMidiDataException imde) {
            imde.printStackTrace();
            System.exit(1);
        }
    }
    
    /**
     * Ending measure is used for alternative repeats and so a check is made
     * in order to not repeat the ending on the second repeat but the regular
     * repeat pattern of "rptstart" and "rptend" are used for the actual
     * repeating mechanism.
     * @param ending 
     */
    private void processEnding(MeiElement ending) {
        //If we are not on an ending, then go through and repeat
        if(!repeats.hasEnding()) {
            repeats.setEnding(ending);
            processParent(ending);
            repeats.resetEnding();
        }
        //If we are already on a repeat then check if it is the same ending
        else if(repeats.getEnding().equals(ending.getId())) {
            repeats.resetEndRepeat();
        }
    }
    
    /**
     * MEI measure will be processed so that tick offsets between
     * staffs will be the same and to also fetch measure children such as
     * tupletSpan and tie elements to be processed sequentially later.
     * @param measure 
     */
    private void processMeasure(MeiElement measure) {
        if(repeats.toProcess(measure)) {
            //Set up a new measure object every time we have a new measure
            //This will impliclitly reset the hashmaps
            //And check for repeats within the measure
            currentMeasure = new MeiMeasure(measure);
        
            //Process the measure element
            processParent(measure);
        
            //If there is a rest on same staff but different
            //layer then layers will not match up
            //This forces same ending on each measure
            long longLayer = 0;
            for(MeiStaff thisStaff : staffs.values()) {
                if(thisStaff.getTickLayer() > longLayer) {
                    longLayer = thisStaff.getTickLayer();
                }
            }
            //set all staff ticks appropriately
            for(MeiStaff thisStaff : staffs.values()) {
                thisStaff.setTick(thisStaff.getTick() + longLayer);
                thisStaff.setTickLayer(0);
            }
        
            //Check and perform a typical (non-ending) repeat
            if(repeats.compareEndID(measure) &&
               !repeats.inRepeat()) {
                repeats.setInRepeat(); //in repeat
                processParent(document.getRootElement());
                repeats.resetInRepeat();//not in repeat
            }
        }
    }
    
    /**
     * Process the mei staff element accordingly.
     * This will process layers of staff and will then process
     * any other children normally through processElement().
     * @param staff 
     */
    private void processStaff(MeiElement staff) {
        //Verify n or else assume sequential ordering between staffs
        //if no information about staffs are given then it will start at 1
        //which matches with the staffDefs in the staffs hash
        String nString = staff.getAttribute("n");
        int staffN = (nString != null) ? Integer.parseInt(nString) : currentStaff.getN() + 1;
        currentStaff = staffs.get(staffN);
        
        processParent(staff);
    }
    
    private void processLayer(MeiElement layer) {
        //Reset tick layer offset every time we have a new layer
        currentStaff.setTickLayer(0);
        //Process the layer
        processParentLayer(layer);
    }
    
    /**
     * All possible contained by layer elements will go through here for
     * further processing.
     * If this does not consider a specified element then the children of that
     * element will be processed.
     * @param child 
     */
    private void processLayerChild(MeiElement child) {
        //Process other elements that might effect this one
        checkLayerCopy(child);
        checkElementStart(child);
        
        String name = child.getName();
        if(name.equals("note")) {
            processNote(child);
        }
        else if(name.equals("beam")) {
            processParentLayer(child);
        }
        else if(name.equals("chord")) {
            processChord(child);
        }
        else if(name.equals("rest") ||
                name.equals("mRest") ||
                name.equals("mSpace") ||
                name.equals("space")) {
            processRest(child);
        }
        else if(name.equals("tuplet")) {
            processTuplet(child);
        }
        else if(name.equals("scoreDef")) {
            processScoreDef(child);
        }
        else if(name.equals("staffDef")) {
            processStaffDef(child);
        }
        //Maybe else is not the safest idea
        else {
            processParentLayer(child);
        }
        
        //Unprocess elements that might effect this one
        checkElementEnd(child);
    }
    
    /**
     * Checks any non-sequential mei element that contains a start id
     * and sets retained information accordingly.
     * Currently used for tupletSpan element.
     * @param child 
     */
    private void checkElementStart(MeiElement child) {
        //Check whether a start tuplet or end tuplet is there
        //One note cannot have more than one tuplet value on it
        MeiElement tupletSpan = currentMeasure.getStart("tupletSpan",child.getId());
        if(tupletSpan != null) {
            int num = Integer.parseInt(tupletSpan.getAttribute("num"));
            int numbase = Integer.parseInt(tupletSpan.getAttribute("numbase"));
            currentMeasure.setNum(num);
            currentMeasure.setNumBase(numbase);
        }
    }
    
    private void checkLayerCopy(MeiElement element) {
        String copyof = element.getAttribute("copyof");
        String sameas = element.getAttribute("sameas");
        if(copyof != null) {
            copyof = copyof.replaceAll("#", "");
            MeiElement copyElement = document.getElementById(copyof);
            processLayerChild(copyElement);
        }
        else if(sameas != null) {
            sameas = sameas.replaceAll("#", "");
            MeiElement sameElement = document.getElementById(sameas);
            processLayerChild(sameElement);
        }
    }
    
    private void checkCopy(MeiElement element) {
        String copyof = element.getAttribute("copyof");
        String sameas = element.getAttribute("sameas");
        if(copyof != null) {
            copyof = copyof.replaceAll("#", "");
            MeiElement copyElement = document.getElementById(copyof);
            processParent(copyElement);
        }
        else if(sameas != null) {
            sameas = sameas.replaceAll("#", "");
            MeiElement sameElement = document.getElementById(sameas);
            processParent(sameElement);
        }
    }
    
    /**
     * Checks any non-sequential mei element that contains an end id and
     * resets any previous information that needs to be changed.
     * Currently used for tupletSpan element.
     * @param child 
     */
    private void checkElementEnd(MeiElement child) {
        //Check whether a start tuplet or end tuplet is there
        //One note cannot have more than one tuplet value on it
        //ASSUMPTION
        //This supercedes the tuplet element in importance
        if(currentMeasure.getEnd("tupletSpan",child.getId()) != null) {
            //Add tick tick remainder here
            currentStaff.setTick(currentStaff.getTick() + 
                    ConvertToMidi.tickRemainder(currentMeasure.getNum()));
            currentMeasure.setNum(1);
            currentMeasure.setNumBase(1);
        }
    }
    
    public void processParentLayer(MeiElement parent) {
        for(MeiElement child : parent.getChildren()) {
            processLayerChild(child);
        }
    }
    
    public void processChord(MeiElement chord) {
        //Set chord as layerChild for future use
        currentStaff.setLayerChild(chord);
        
        //Get duration of a note because they will all be the same
        //Put dur into layerChild hash here
        long tick = 0;
        String chordDur = chord.getAttribute("dur");
        if(attributeExists(chordDur)) {
            tick = getDurToTick(chord);
        }
        else {
            List<MeiElement> notes = chord.getChildrenByName("note");
            if(notes.size() > 0) {
                tick = getDurToTick(notes.get(0));
            }
        }
        
        //Process chord children
        processParentLayer(chord);
        
        //Set layer tick appropriately
        currentStaff.setTickLayer(currentStaff.getTickLayer() + tick);
        currentStaff.removeLayerChild("chord");
    }
    
    /**
     * Midi Conversion done here and then these conversions are
     * processed in addMidiNote to add the note to the appropriately
     * given track.
     * Anything that has to do with the duration or pitch of the note
     * is dealt with in here. Extra tick consideration are done in the
     * checkLayerParents().
     * Also, for chords, ticks are added at the end of the chord so that
     * each note within the chord starts at the same time. This is done
     * in processChord().
     * @param note to add
     */
    public void processNote(MeiElement note) {
        //Used to use note outisde of function for now
        currentStaff.setLayerChild(note);
        
        //Get the correct note pitch with name, octave and accidental
        int nPitch = getMidiPitch(note);
        
        //Get the proper start and end Ticks for this note
        long startTick = currentStaff.getTick() + currentStaff.getTickLayer();
        long endTick = startTick + getDurToTick(note);
        
        //Check parents to see how to process this chord
        checkLayerParents(nPitch,startTick,endTick);
        
        //Remove current note from currentStaff has
        currentStaff.removeLayerChild("note");
    }
    
    public int getMidiPitch(MeiElement note) {
        //Attributes taken here to be processed here
        String pname = note.getAttribute("pname");
        String oct = note.getAttribute("oct");
        String accid = note.getAttribute("accid");
        String dur = note.getAttribute("dur");
        //Could add accidental child here
        
        //Get the proper pitch given accidental or key signature
        int nPitch;
        if(attributeExists(accid)) {
            nPitch = ConvertToMidi.NoteToMidi(pname, oct, accid);
            currentMeasure.setAccidental(pname, accid);
            currentMeasure.setOct(oct);
        }
        else if(currentMeasure.hasAccidental(pname) &&
                currentMeasure.getOct().equals(oct)) {
            nPitch = ConvertToMidi.NoteToMidi(pname, oct, 
                                    currentMeasure.getAccidental(pname));
        }
        else { 
            nPitch = ConvertToMidi.NoteToMidi(pname, oct, 
                                    currentStaff.getKeysigMap().get(pname));
        }
        return nPitch;
    }
    
    /**
     * Note check to see how the midi note should be processed depending on if
     * the note is within a chord or held by a tie.
     * ASSUMPTION
     * This may need to be altered for tie attributes in chords if an examples
     * is found.
     * @param nPitch
     * @param startTick
     * @param endTick
     */
    public void checkLayerParents(int nPitch,
                                  long startTick,
                                  long endTick) {
        Track thisTrack = sequence.getTracks()[currentStaff.getN()-1];
        MeiElement chord = currentStaff.getLayerChild("chord");
        String graceChord = (chord != null) ? chord.getAttribute("grace") : null;
        MeiElement note = currentStaff.getLayerChild("note");
        String graceNote = note.getAttribute("grace");
        String tieAttribute = note.getAttribute("tie");
        String tieStartID = null;
        String tieEndID = null;
        if(currentMeasure.has("tie")) {
            MeiElement startTie = currentMeasure.getStart("tie", note.getId());
            tieStartID = (startTie != null) ? startTie.getAttribute("startid") : null;
            MeiElement endTie = currentMeasure.getEnd("tie", note.getId());
            tieEndID = (endTie != null) ? endTie.getAttribute("endid") : null;
        }
        
        if(graceNote != null || graceChord != null) {
            //If grace note then move backwards and start next note on beat
            startTick = startTick - (endTick - startTick);
            endTick = startTick;
            addMidiNoteOn(thisTrack, nPitch, startTick, endTick);
            addMidiNoteOff(thisTrack, nPitch, startTick, endTick);
        }
        else if(chord != null && tieAttribute != null) {
            addMidiTieNote(thisTrack, tieAttribute, nPitch, startTick, endTick);
        }
        else if(chord != null && tieStartID != null) {
           addMidiNoteOn(thisTrack, nPitch, startTick, endTick);
        }
        else if(chord != null && tieEndID != null) {
            addMidiNoteOff(thisTrack, nPitch, startTick, endTick);
        }
        else if(chord != null) {
            String tieChord = chord.getAttribute("tie");
            if(tieChord != null) {
                addMidiTieNote(thisTrack, tieChord, nPitch, startTick, endTick);
            }
            else {
                addMidiNoteOn(thisTrack, nPitch, startTick, endTick);
                addMidiNoteOff(thisTrack, nPitch, startTick, endTick);
            }
        }
        else if(tieAttribute != null) {
            addMidiTieNote(thisTrack, tieAttribute, nPitch, startTick, endTick);
            currentStaff.setTickLayer(endTick - currentStaff.getTick());
        }
        else if(tieStartID != null) {
            addMidiNoteOn(thisTrack, nPitch, startTick, endTick);
            currentStaff.setTickLayer(endTick - currentStaff.getTick());
        }
        else if(tieEndID != null) {
            addMidiNoteOff(thisTrack, nPitch, startTick, endTick);
            currentStaff.setTickLayer(endTick - currentStaff.getTick());
        }
        else {
            addMidiNoteOn(thisTrack, nPitch, startTick, endTick);
            addMidiNoteOff(thisTrack, nPitch, startTick, endTick);
            currentStaff.setTickLayer(endTick - currentStaff.getTick());
        }
    }
    
    /**
     * Add the given midi note on event to the specified track.
     * This assumes that both a note-on and note-off even is required.
     * @param thisTrack
     * @param nPitch
     * @param startTick
     * @param endTick
     */
    public void addMidiNoteOn(Track thisTrack,
                            int nPitch, 
                            long startTick,
                            long endTick) {
        try {
            thisTrack.add(MidiBuildMessage.createNoteOnEvent(nPitch, 
                                                         startTick, 
                                                         currentStaff.getChannel()));
        }
        catch(InvalidMidiDataException imde) {
            imde.printStackTrace();
            System.exit(1); 
        }
    }
    
    /**
     * Add the given midi note off event to the specified track.
     * @param thisTrack
     * @param nPitch
     * @param startTick
     * @param endTick
     */
    public void addMidiNoteOff(Track thisTrack,
                            int nPitch, 
                            long startTick,
                            long endTick) {
        try {
            thisTrack.add(MidiBuildMessage.createNoteOffEvent(nPitch, 
                                                          endTick, 
                                                          currentStaff.getChannel()));
        }
        catch(InvalidMidiDataException imde) {
            imde.printStackTrace();
            System.exit(1); 
        }
    }
    
    /**
     * Check tie attribute to see whether we initialize or terminate
     * a given midi notes.
     * i will initialize the note and t will terminate the midi note.
     * if an m ( or other letter ) is found then nothing will be done
     * @param thisTrack
     * @param tie
     * @param nPitch
     * @param startTick
     * @param endTick
     */
    public void addMidiTieNote(Track thisTrack,
                            String tie,
                            int nPitch, 
                            long startTick,
                            long endTick) {
        if(tie.contains("i")) {
            addMidiNoteOn(thisTrack, nPitch, startTick, endTick);
        }
        else if(tie.contains("t")) {
            addMidiNoteOff(thisTrack, nPitch, startTick, endTick);
        }
    }
    
    /**
     * Process a rest by only adding to the layer tick count of currentStaff
     * without actually creating any midi events.
     * @param rest
     */
    public void processRest(MeiElement rest) {
        //Get the proper start and end Ticks for this rest
        long startTick = currentStaff.getTick() + currentStaff.getTickLayer();
        long endTick = startTick + getDurToTick(rest);
        
        //Change the layers tick accordingly
        currentStaff.setTickLayer(endTick - currentStaff.getTick());
    }
    
    /**
     * Process a tuplet element by using the num and numbase attribute.
     * NOTE
     * If the current measure has a tuplet span and a tuplet for the same
     * notes then the remainder will be calculated twice.
     * @param tuplet 
     */
    public void processTuplet(MeiElement tuplet) {
        //Give tuplet to MeiStaff
        currentStaff.setLayerChild(tuplet);
        int num = getAttributeToInt("num", tuplet, 1);
        int numbase = getAttributeToInt("numbase", tuplet, 1);
        currentMeasure.setNum(num);
        currentMeasure.setNumBase(numbase);
            
        //Process here with tuplet info
        processParentLayer(tuplet);
            
        //Add mod of odd number to tick once it finishes the tuplet
        //And remove tuplet from hash
        currentStaff.setTick(currentStaff.getTick() 
                          + ConvertToMidi.tickRemainder(num));
        currentMeasure.setNum(1);
        currentMeasure.setNumBase(1);
        currentStaff.getLayerChildMap().remove("tuplet");
    }
    
    /**
     * Helper method to standardize if an attribute exists within an element.
     * @param attribute
     * @return true if attribute exists
     */
    private boolean attributeExists(String attribute) {
        return attribute != null;
    }
    
    /**
     * Helper method to convert an mei element attribute to an int given
     * a specified default value if the attribute DNE.
     * @param name
     * @param ele
     * @param intDefault
     * @return 
     */
    private int getAttributeToInt(String name, MeiElement ele, int intDefault) {
        int attInt = intDefault;
        if(ele != null) {
            String attString = ele.getAttribute(name);
            if(attributeExists(attString)) {
                attInt = Integer.parseInt(attString);
            }
        }
        return attInt;
    }
    
    /**
     * Converts given duration string to a long tick value for midi.
     * thiStaff can be populated with a tuplet or note which will then
     * be used to compute tuplet and dot values in the duration.
     * ASSUMPTION
     * This assumes that if not dur is given in element or chord
     * then the note takes up a full measure.
     * @param dur
     * @return long tick value of dur string
     */
    private long getDurToTick(MeiElement element) {
        //CHECK IN HERE FOR TUPLETS FROM tupletSpan
        //If in tuplet then use that or else check tupletSpan
        int num = currentMeasure.getNum();
        int numbase = currentMeasure.getNumBase();
        String dur = getDurString(element);
        int dots = getDots(element);
        return ConvertToMidi.durToTick(dur,num,numbase,dots);
    }
    
    /**
     * Fetches the appropriate duration of a given Mei element depending on
     * whether it's a note, a chord or a rest/space.
     * If no duration is found, then a dur = "0" is returned.
     * ASSUMPTION
     * Grace notes are considered to be 1 ticks now.
     * @param element
     * @return duration of element in string form
     */
    private String getDurString(MeiElement element) {
        String dur;
        //Else assume it is an entire measure
        //Could replace this with 
        //else if(element.getName().equals("mRest" || "mSpace))
        if(element.getName().equals("mRest") ||
           element.getName().equals("mSpace")){
            double count = Integer.parseInt(currentStaff.getMeterCount());
            double unit = Integer.parseInt(currentStaff.getMeterUnit());
            dur = String.valueOf(unit / count);
        }
        //For notes and rests with dur attributes
        else if(attributeExists(element.getAttribute("dur"))) {
            dur = element.getAttribute("dur");
            //GRACE NOTE DURATION CAN BE CHANGED HERE
        }
        else if(currentStaff.getLayerChild("chord") != null &&
                currentStaff.getLayerChild("chord").getAttribute("dur") != null) {
            dur = currentStaff.getLayerChild("chord").getAttribute("dur");
        }
        else {
            dur = "0";
        }
        return dur;
    }
    
    /**
     * Fetches the appropriate dots for the given mei element depending
     * on if it's a chord or a note.
     * If no dots are found, then a dots = 0 value is returned.
     * @param element
     * @return number of corresponding dots for element
     */
    private int getDots(MeiElement element) {
        int dots;
        if(currentStaff.getLayerChild("chord") != null &&
           currentStaff.getLayerChild("chord").getAttribute("dots") != null) {
            dots = Integer.parseInt(currentStaff.getLayerChild("chord").getAttribute("dots"));
        }
        else {
            dots = getAttributeToInt("dots", element, 0);
        }
        return dots;
    }
}
