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
/**
 *
 * @author dinamix
 */
public class MeiSequence {
    
    private MeiDocument document;
    
    private Sequence sequence;
    
    private HashMap<Integer,MeiStaff> staffs; //staffs store in hashmap
                                              //for easier access throughout
    
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
                               
    private HashMap<String,String> xmlIds; //not necessary with DFS
    
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
     * If element not in switch statement below, then not processed at all.
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
        else if(element.getName().equals("measure")) {
            //Get children of measure which will probably be <staff>
        }
        else if(element.getName().equals("staff")) {
            //Get children of staff which will probably be <layer>
            //If staff has more than 2 children, need to consider offset
            //Check staff n attribute and then pass on MeiStaff
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
        String count = scoreDef.getAttribute("meter.count");
        String unit = scoreDef.getAttribute("meter.unit");
        String keysig = scoreDef.getAttribute("key.sig");
        String keymode = scoreDef.getAttribute("key.mode");
        if(attributeExists(count)) {
            works.get(currentMovement).setMeterCount(count);
        }
        if(attributeExists(unit)) {
            works.get(currentMovement).setMeterUnit(unit);
        }
        if(attributeExists(keysig)) {
            works.get(currentMovement).setKeysig(keysig);
        }
        if(attributeExists(keymode)) {
            works.get(currentMovement).setKeyMode(keymode);
        }
        //For a scoreDef change randomly in the file
        //If staffs not empty, then update them
        //If they are empty, then it will be created in processStaffDef()
        if(!staffs.isEmpty()) {
            updateStaffs();
        }
        processParent(scoreDef);
    }
    
    /**
     * New scoreDef found during the piece will update all defined
     * staffs accordingly.
     */
    private void updateStaffs() {
        MeiWork work = works.get(currentMovement);
        String count = work.getMeterCount();
        String unit = work.getMeterUnit();
        String tempo = work.getTempo();
        String keysig = work.getKeysig();
        String keymode = work.getKeyMode();
        //Update each staff accordingly
        for(Integer i : staffs.keySet()) {
            updateMeiStaff(staffs.get(i), 
                                  count,
                                  unit,
                                  tempo,
                                  null, //label not in MeiWork for now
                                        //never really found in tests
                                  keysig,
                                  keymode);
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
    
    //START FROM HERE
    //DO SEQUENTIAL CASES WITH DEFAULT BEING LAST CASE
    //Need to check n for 10 or greater than 16
    //Then check if n is in staff or else create new MeiStaff
    //And create new Track()
    private void processStaffDef(MeiElement staffDef) {
        MeiStaff thisStaff;       
        
        //Set up necessary attributes
        int n = 1; // n = 1 in case it does not exist
        String nString = staffDef.getAttribute("n");
        String count = staffDef.getAttribute("meter.count");
        String unit = staffDef.getAttribute("meter.unit");
        String label = staffDef.getAttribute("label"); //could replace w/ null
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
        //If staff already created then check it
        //and make appropriate changes
        if(staffs.containsKey(n)){
            thisStaff = staffs.get(n);
            thisStaff = updateMeiStaff(thisStaff, count, unit, 
                                         tempo, label, keysig, keymode);
            staffs.replace(n, thisStaff);
        }
        //If staff not created yet, then instantiate it
        //and put it into HashMap<MeiStaff>
        else {
            thisStaff = createMeiStaff(n, count, unit,
                                        tempo, label, keysig, keymode);
            staffs.put(n, thisStaff);
        }
        
        //@TODO
        //At the end we need to create or change MIDI tracks
        //@TODO
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
        if(attributeExists(tempo)) {
            thisStaff.setTempo(tempo);
        }
        if(attributeExists(count)) {
            thisStaff.setMeterCount(count);
        }
        if(attributeExists(unit)) {
            thisStaff.setMeterUnit(unit);
        }
        if(attributeExists(label)) {
            thisStaff.setLabel(label);
        }
        if(attributeExists(keysig)) {
            thisStaff.setKeysig(keysig);
        }
        if(attributeExists(keymode)) {
            thisStaff.setKeymode(keymode);
        }
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
        //Check if label exists and if it is valid
        if(attributeExists(label) && 
           ConvertToMidi.instrToMidi(label) >= 0) {
                newStaff.setLabel(label);
        }
        //or else we use works defaults
        else {
            processNewLabel(newStaff, n);
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
        return newStaff;
    }
    
    /**
     * WARNING
     * ASSUMPTION: Piano is last staff.
     * This assumption allows missing instruments to
     * to still get a correct value, such as pianos or organs.
     * If instrument not given in label
     * or instrument not given in meihead
     * then loop back till you find an instrument
     * or else there is no instrument and
     * Piano is default in MeiStaff default object.
     * @param newStaff
     * @param n 
     */
    private void processNewLabel(MeiStaff newStaff, int n) {
        //Check if there is a work
        MeiWork work = works.get(currentMovement);
        if(work != null) {
            HashMap<Integer,String> instrVoice = work.getInstrVoice();
            //Check if there is a valid instrVoice
            if(instrVoice.containsKey(n)) {
                newStaff.setLabel(instrVoice.get(n));
            }
            //If there is no instrVoice then take previous one
            else {
            //Iterate through hashmap backwards
            //till we find an instrument.
            //This will be in the same staffGrp.
                for(int i = n-1; i >= 0; i--) {
                    if(instrVoice.containsKey(i)) {
                        newStaff.setLabel(instrVoice.get(i));
                        break;//once changed dont change anymore
                    }
                }
            } 
        }      
        //Else no instrument given and we default to piano
    }
    
    /**
     * Helper method to standardize if an attribute exists within an element.
     * @param attribute
     * @return true if attribute exists
     */
    private boolean attributeExists(String attribute) {
        return attribute != null;
    }
    
    public void processChord(MeiElement parent) {
        
    }
    
    public void processNote(MeiElement parent) {
        
    }
    
    public void processRest(MeiElement parent) {
        
    }
}
