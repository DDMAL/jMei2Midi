/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.jmei2midi;

import ca.mcgill.music.ddmal.mei.MeiAttribute;
import ca.mcgill.music.ddmal.mei.MeiDocument;
import ca.mcgill.music.ddmal.mei.MeiElement;
import ca.mcgill.music.ddmal.mei.MeiXmlReader;
import java.util.ArrayList;
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
    
    private HashMap<Integer,MeiStaff> staffs; //staffs store in hashmap
                                              //for easier access throughout
    
    //Probably won't use this because of sequence.getTracks()
    private List<Track> tracks; //keep tracks in sequence for simpler reference
                                //and for >16 staffs
                                //Will relate to the n attribute of MeiStaff
                                //and will go into the list accordingly.
                                //Account for channel 9 drums and >15 in 15
    
    //This will contain metdata and be related to mdivs and scoreDefs
    //If no other information is given then defaults will be used
    private int currentMovement; //this will hold the current mdiv number
    private HashMap<Integer,MeiWork> works; //this accounts for changes in <scoreDef>
                                            //that may or may not need to be global
    
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
        
        //Instantiate tracks as ArrayList
        tracks = new ArrayList<>();
        
        //Instantiate staffs as ArrayList
        staffs = new HashMap<>();
        
        //Create new defaults array for default values
        //So far we need 5 values for:
        //tempo, meter.count, meter.unit, key.sig and key.mode
        works = new HashMap<>();
        
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
        
        //Instantiate tracks as ArrayList
        tracks = new ArrayList<>();
        
        //Instantiate staffs as ArrayList
        staffs = new HashMap<>();
        
        //Create new defaults array for default values
        //So far we need 5 values for:
        //tempo, meter.count, meter.unit, key.sig and key.mode
        works = new HashMap<>();
        
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
        //Get children of measure which will probably be <staff>
        else if(element.getName().equals("measure")) {
            processParent(element);
        }
        //Get children of staff which will probably be <layer>
        //If staff has more than 2 children, need to consider offset
        //Check staff n attribute and then pass on MeiStaff
        else if(element.getName().equals("staff")) {
            processStaff(element);
        }
        else if(element.getName().equals("layer")) {
            //Get children of layer which will be note, rest or mRest
            //Layers within a staff will all start at the same tick
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
        int midiInstr = ConvertToMidiWithStats.instrToMidi(label, stats);
        int percInstr = ConvertToMidiWithStats.instrToPerc(label, stats);
        if(midiInstr > -1) {
            thisStaff.setLabelInstr(label, midiInstr);
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
        
        if(attributeExists(tempo)) {
            //ConvertToMidiWithStats.tempoToBpm(tempo, stats);
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
        int midiInstr = ConvertToMidiWithStats.instrToMidi(label, stats);
        int percInstr = ConvertToMidiWithStats.instrToPerc(label, stats);
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
        track.add(MidiBuildMessage.createKeySignature(key, quality, tick));
        track.add(MidiBuildMessage.createProgramChange(midiLabel, tick, channel));
        track.add(MidiBuildMessage.createTrackTempo(bpm, tick));
    }
    
    /**
     * Process the mei staff element accordingly.
     * @TODO
     * Need to include copyof element for things like erlkonig.
     * @TODO
     * @param staff 
     */
    private void processStaff(MeiElement staff) {
        //n is a required attribute of staff
        int staffN = Integer.parseInt(staff.getAttribute("n"));
        MeiStaff thisStaff = staffs.get(staffN);
        //This gets all layers within staff in order to be processed
        List<MeiElement> layers = staff.getDescendantsByName("layer");
        processLayers(layers,thisStaff);
        //Parent will be processed for any extra elements
        //such as score/staffDefs
        processParent(staff);
    }
    
    private void processLayers(List<MeiElement> layers, MeiStaff thisStaff) {
        //Layers will use layer offset as tick count
        for(MeiElement layer : layers) {
            processLayer(layer,thisStaff);
        }
        //will set general tick count here after all layers are done
        long newTick = thisStaff.getTick() + thisStaff.getTickLayer();
        thisStaff.setTick(newTick);
    }
    
    private void processLayer(MeiElement layer, MeiStaff thisStaff) {
        for(MeiElement child : layer.getChildren()) {
            //Reset tick layer offset every time we have a new layer
            thisStaff.setTickLayer(0);
            processLayerChild(child,thisStaff);
        }
    }
    
    private void processLayerChild(MeiElement child, MeiStaff thisStaff) {
        if(child.getName().equals("note")) {
            processNote(child,thisStaff);
        }
        else if(child.getName().equals("beam")) {
            
        }
        else if(child.getName().equals("chord")) {
            
        }
        else if(child.getName().equals("rest")) {
            
        }
        else if(child.getName().equals("mRest")) {
            
        }
        else if(child.getName().equals("tuplet")) {
            int tuple = Integer.parseInt(child.getAttribute("num"));
            thisStaff.setTuplet(tuple);
            
            //Process here
            
            //Add mod of odd number to tick once it finishes the tuplet
            thisStaff.setTick(thisStaff.getTick() + 256%tuple);
        }
        else {
            processParent(child);
        }
    }
    
    public void processBeam(MeiElement parent) {
        
    }
    
    public void processChord(MeiElement parent) {
        
    }
    
    public void processNote(MeiElement note, MeiStaff thisStaff) {
        String pname = note.getAttribute("pname");
        String oct = note.getAttribute("oct");
        String accid = note.getAttribute("accid");
        String dur = note.getAttribute("dur");
        String tie = note.getAttribute("tie");
        
        //Get the proper pitch given accidental or key signature
        int nPitch;
        if(attributeExists(accid)) {
            nPitch = ConvertToMidi.NoteToMidi(pname, oct, accid);
        }
        else { 
            nPitch = ConvertToMidi.NoteToMidi(pname, oct, 
                                    thisStaff.getKeysigMap().get(pname));
        }
        
        //Make appropriate note
        Track thisTrack = sequence.getTracks()[thisStaff.getN()-1];
        if(attributeExists(tie)) {
            addMidiTieNote();
        }
        else {
            addMidiNote();
        }
    }
    
    public void addMidiNote() {
        
    }
    
    public void addMidiTieNote() {
        
    }
    
    public void processMrest(MeiElement parent) {
        
    }
    
    public void processRest(MeiElement parent) {
        
    }
    
    public void processTuplet(MeiElement tuplet) {
        
    }
    
    /**
     * Helper method to standardize if an attribute exists within an element.
     * @param attribute
     * @return true if attribute exists
     */
    private boolean attributeExists(String attribute) {
        return attribute != null;
    }
}
