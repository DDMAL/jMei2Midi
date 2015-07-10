/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.jmei2midi;

import org.ddmal.jmei2midi.meielements.staffinfo.MeiWork;
import org.ddmal.jmei2midi.meielements.staffinfo.MeiStaff;
import org.ddmal.jmei2midi.meielements.general.MeiMeasure;
import org.ddmal.jmei2midi.meielements.general.MeiRepeat;
import ca.mcgill.music.ddmal.mei.MeiDocument;
import ca.mcgill.music.ddmal.mei.MeiElement;
import ca.mcgill.music.ddmal.mei.MeiXmlReader;
import ca.mcgill.music.ddmal.mei.MeiXmlReader.MeiXmlReadException;
import java.io.File;
import java.util.HashMap;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Sequence;
import org.ddmal.jmei2midi.meielements.general.MeiMdiv;
import org.ddmal.jmei2midi.meielements.layerchild.LayerChildEnum;
import org.ddmal.jmei2midi.meielements.layerchild.LayerChildFactory;
import org.ddmal.jmei2midi.meielements.staffinfo.StaffBuilderEnum;
import org.ddmal.jmei2midi.meielements.staffinfo.StaffBuilderFactory;
import org.ddmal.midiUtilities.ConvertToMidi;
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
    private HashMap<Integer,MeiWork> works; //this accounts for changes in <scoreDef>
    
    //Current movement is set in the current mDiv object
    private MeiMdiv currentMdiv;
    
    //Stores info specific to a certain measure such as
    //accidentals and tupletSpans
    private MeiMeasure currentMeasure;
    
    //Store all mei repeat starts and ends
    private MeiRepeat repeats;
    
    //This allows a sequence to generate some mei stats
    //Such as invalid tempo and invalid instruments
    private MeiStatTracker stats;
    
    /**
     * Constructor that creates a MIDI Sequence given an MEI filename and
     * also keeps track of some mei stats using MeiStatTracker reference.
     * @param file 
     * @param stats 
     * @throws javax.sound.midi.InvalidMidiDataException 
     * @throws MeiXmlReader.MeiXmlReadException
     */
    public MeiSequence(File file, MeiStatTracker stats) 
            throws InvalidMidiDataException, MeiXmlReadException {
        this.staffs = new HashMap<>();
        this.currentStaff = new MeiStaff(1); //1 in case there are no n attributes for staffs within measures
        this.works = new HashMap<>();
        this.currentMdiv = new MeiMdiv();
        this.repeats = new MeiRepeat();
        
        this.document = MeiXmlReader.loadFile(file);
        this.stats = stats;
        stats.setFileName(file.getPath());
        documentToSequence();
    }
    
    public MeiSequence(String filename) 
            throws InvalidMidiDataException, MeiXmlReadException {
        this(new File(filename),new MeiStatTracker(filename));
    }
    
    public MeiSequence(File file) 
            throws InvalidMidiDataException, MeiXmlReadException {
        this(file, new MeiStatTracker(file.getPath()));
    }
    
    public MeiSequence(String filename, MeiStatTracker stats) 
            throws InvalidMidiDataException, MeiXmlReadException {
        this(new File(filename),stats);
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
    protected HashMap<Integer,MeiWork> getWorks() {
        return this.works;
    }
    
    /**
     * RETURN HASHMAP\<MEISTAFF\> STAFFS FOR TESTING PURPOSES
     * @return 
     */
    protected HashMap<Integer,MeiStaff> getStaffs() {
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
     * @throws javax.sound.midi.InvalidMidiDataException
     */
    private void documentToSequence() throws InvalidMidiDataException {
        sequence = new Sequence(Sequence.PPQ, 256);
        recursiveDFS(document.getRootElement());
    }
    
    /**
     * Recursive DFS through MEI document.
     * @param root 
     */
    private void recursiveDFS(MeiElement root) {
        processElement(root);
    }
    
    /**
     * Each MEI element processed accordingly.
     * Some elements may only be processed within other elements
     * such as a note. Also checks for copyOf and sameAs attribute.
     * @param element can be any MEI tag
     */
    private void processElement(MeiElement element) {
        //See if this element is a copy and then process accordingly
        checkForCopyOf(element);
        
        String name = element.getName();
        if(LayerChildEnum.contains(name)) {
            checkLayerStart(element);
            LayerChildFactory.buildLayerChild(currentStaff, 
                                              currentMeasure, 
                                              sequence, 
                                              element);
            checkLayerEnd(element);
        }
        else if(StaffBuilderEnum.contains(name)) {
            StaffBuilderFactory.buildStaffBuilder(sequence, 
                                                  stats, 
                                                  staffs, 
                                                  works, 
                                                  currentMdiv, 
                                                  currentStaff, 
                                                  element);
            processParent(element);
        }
        else {
            processGeneral(element);
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
     * Process general mei elements that are sub classes of MeiGeneral
     * or other unique processing cases such as repeats and incips.
     * Otherwise, children are found and process accordingly.
     * @param element 
     */
    private void processGeneral(MeiElement element) {
        switch (element.getName()) {
            case "mdiv":
                processMdiv(element);
                break;
                
            case "ending":
                processEnding(element);
                break;
                
            case "measure":
                processMeasure(element);
                break;
                
            case "staff":
                processStaff(element);
                break;
                
            case "layer":
                processLayer(element);
                break;
                
            case "incip":
                break; //used to skip over incip music in metadata
                
            default:
                processParent(element);
                break;
        }
    }
    
    /**
     * Set the currentMdiv to the appropriate movement.
     * @param mdiv 
     */
    private void processMdiv(MeiElement mdiv) {
        currentMdiv.setCurrentMovement(mdiv);
        processParent(mdiv);
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
        
        //This is a check for if the null n attribute was encoded incorrectly
        MeiStaff newStaff;
        if(staffs.containsKey(staffN)) {
            newStaff = staffs.get(staffN);
        }
        else {
            newStaff = currentStaff;
        }
        currentStaff = newStaff;
        
        processParent(staff);
    }
    
    /**
     * Process a layer by checking if we need to process music
     * and setting the current tick layer count to 0
     * @param layer 
     */
    private void processLayer(MeiElement layer) {
        currentStaff.setTickLayer(0);
        processParent(layer);
    }
    
    /**
     * Checks for a copyOf or sameAs attribute in any mei element and
     * will process the element with the corresponding id.
     * @param element 
     */
    private void checkForCopyOf(MeiElement element) {
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
     * Checks any non-sequential mei element that contains a start id
     * and sets retained information accordingly.
     * Currently used for tupletSpan element.
     * @param child 
     */
    private void checkLayerStart(MeiElement child) {
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
    
    /**
     * Checks any non-sequential mei element that contains an end id and
     * resets any previous information that needs to be changed.
     * Currently used for tupletSpan element.
     * @param child 
     */
    private void checkLayerEnd(MeiElement child) {
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
}
