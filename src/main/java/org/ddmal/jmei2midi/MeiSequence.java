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
/**
 *
 * @author dinamix
 */
public class MeiSequence {
    
    private MeiDocument document;
    
    private Sequence sequence;
    
    private List<MeiStaff> staffs;
    
    private List<Track> tracks; //keep tracks in sequence for simpler reference
                                //and for >16 staffs
                                //Will relate to the n attribute of MeiStaff
                                //and will go into the list accordingly.
                                //Account for channel 9 drums and >15 in 15
    
    //Might need to use List<String> if each staff needs its own defaults
    //Right now these are global defaults taken from scoreDef
    //If no other information is given then defaults will be used
    private String[] defaults;  //this accounts for changes in <scoreDef>
                                //that may or may not need to be global
                                //defaults[0] for tempo
                                //defaults[1] for meter.count
                                //defaults[2] for meter.unit
                                //defaults[3] for key.sig
                                //defaults[4] for key.mode
                               
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
        tracks = new ArrayList<Track>();
        
        //Instantiate staffs as ArrayList
        staffs = new ArrayList<MeiStaff>();
        
        //Create new defaults array for default values
        //So far we need 5 values for:
        //tempo, meter.count, meter.unit, key.sig and key.mode
        defaults = new String[5];
        
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
    public String[] getDefaults() {
        return this.defaults;
    }
    
    /**
     * RETURN LIST\<MEISTAFF\> STAFFS FOR TESTING PURPOSES
     * @return 
     */
    public List<MeiStaff> getStaffs() {
        return this.staffs;
    }

    /**
     * Converts given document to MIDI sequence object.
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
            recursiveDFS(element);
        }
    }
    
    /**
     * Recursive DFS through MEI document.
     * Sequentially goes through all tags 
     * then either processParent() or processElement() is called.
     * @param root 
     */
    public void recursiveDFS(MeiElement root) {
        processElement(root);
    }
    
    
    /**
     * Each MEI element processed accordingly.
     * If element not in switch statement below, then not processed at all.
     * @param element can be any MEI tag
     */
    //COULD TEST WITH SWITCH STATEMENT FOR NEATNESS
    //would need to check if switch uses .equals() or == (probably .equals())
    public void processElement(MeiElement element) {
        //Get children of mdiv which will probably be <body>
        if(element.getName().equals("music")) {
            processParent(element);
        }
        //Get children of mdiv which will probably be <mdiv>
        else if(element.getName().equals("body")) {
            processParent(element);
        }
        //Get children of mdiv which will probably be <score>
        else if(element.getName().equals("mdiv")) {
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
        else if(element.getName().equals("section")) {
            //Get children of section which will probably be <measure>
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
    public void processParent(MeiElement parent) {
        for(MeiElement child : parent.getChildren()) {
            processElement(child);
        }
    }
    
    /**
     * Stores meter.count, meter.unit and key.sig in String[] defaults.
     * These will be used for staffDef elements as default values.
     * @param scoreDef passed scoreDef tag
     */
    public void processScoreDef(MeiElement scoreDef) {
        //Keep default key.sig and then use
        //staff def key.sig on other staffs
        String count = scoreDef.getAttribute("meter.count");
        String unit = scoreDef.getAttribute("meter.unit");
        String keysig = scoreDef.getAttribute("key.sig");
        String keymode = scoreDef.getAttribute("key.mode");
        if(count != null) {
            defaults[1] = count;
        }
        if(unit != null) {
            defaults[2] = unit;
        }
        if(keysig != null) {
            defaults[3] = keysig;
        }
        if(keymode != null) {
            defaults[4] = keymode;
        }
        processParent(scoreDef);
    }
    
    //Need to check n for 10 or greater than 16
    //Then check if n is in staff or else create new MeiStaff
    //And create new Track()
    public void processStaffDef(MeiElement staffDef) {
        MeiStaff thisStaff;
        int n = 0;
        String nString = staffDef.getAttribute("n");
        if(nString != null) {
            n = Integer.parseInt(nString);
        }
        if(staffs.size() > n-1) {
            thisStaff = staffs.get(n);
        }
        String label = staffDef.getAttribute("label");
        String keysig = staffDef.getAttribute("key.sig");
        String keymode = staffDef.getAttribute("key.mode");
        if(label == null) {
            label = "Piano";
        }
        if(keysig == null) {
            
        }
    }
    
    public void processChord(MeiElement parent) {
        
    }
    
    public void processNote(MeiElement parent) {
        
    }
    
    public void processRest(MeiElement parent) {
        
    }
}
