/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.jmei2midi;

import org.ddmal.jmei2midi.meielements.general.MeiData;
import org.ddmal.jmei2midi.meielements.staffinfo.*;
import org.ddmal.jmei2midi.meielements.general.MeiMeasure;
import org.ddmal.jmei2midi.meielements.general.MeiRepeat;

import ca.mcgill.music.ddmal.mei.MeiDocument;
import ca.mcgill.music.ddmal.mei.MeiElement;
import ca.mcgill.music.ddmal.mei.MeiXmlReader;
import ca.mcgill.music.ddmal.mei.MeiXmlReader.MeiXmlReadException;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Sequence;

import org.ddmal.jmei2midi.meielements.general.MeiMdiv;
import org.ddmal.jmei2midi.meielements.layerchild.LayerChildEnum;
import org.ddmal.jmei2midi.meielements.layerchild.LayerChildFactory;
import org.ddmal.jmei2midi.meielements.meispecific.MeiSpecificStorage;
import org.ddmal.midiUtilities.ConvertToMidi;

/**
 * Build an MEISequence containing all MEI information. This is done using a
 * recursive DFS for the XML MEI document and class hierarchies for the
 * conversion from MEI to MIDI. The MEI information is added to the MIDI
 * Sequence sequentially as it is parsed. 
 * 
 * <p>For further development, it would be
 * highly recommended to first read through and understand the MeiSequence
 * class. One will notice that any newly implemented MEI element can be done so
 * in 1 of 3 ways.</p>
 * 
 * <p>1. If the MEI element is a general element that affects the recursive XML
 * stack, then it should be added to the meielements.general package and the
 * class should extend MeiGeneral.</p>
 * 
 * <p>2. If the MEI element is involved with the
 * staff information (i.e. key, instrument, mode...), then it should be added to
 * the meielement.staffinfo package and the class may or may not extend
 * MeiStaffBuilder. It is very likely that the staffinfo package can be simply
 * updated to deal with minor changes.</p>
 * 
 * <p>3. If the MEI element is a child of the
 * MEI layer element and/or involved with partial/direct midi information, then
 * it should be added to the meielements.layerchild package and the class should
 * extend LayerChild.</p>
 * 
 * @author Tristano Tenaglia
 */
public class MeiSequence {

	/**
	 * MEIDocument that will be used to create this MeiSequence.
	 */
	private MeiDocument document;

	/**
	 * The MIDI Sequence of this MEI document.
	 */
	private Sequence sequence;

	/**
	 * The current staff we are editing during parsing.
	 */
	private MeiStaff currentStaff;

	/**
	 * All staffs used throughout this MEI document. Key : n attribute or
	 * sequential, Value : MeiStaff object
	 */
	private Map<Integer, MeiStaff> staffs;

	/**
	 * This will contain metadata and be related to mdiv and scoreDef tags. If
	 * no other information is given then defaults will be used. This accounts
	 * for changes in scoreDef tags as well.
	 */
	private Map<Integer, MeiWork> works;

	/**
	 * Current movement is set in the current mDiv object.
	 */
	private MeiMdiv currentMdiv;

	/**
	 * WARNING: THIS COULD BE CHANGED BECAUSE TUPLETSPANS ARE NOT SPECIFIC TO
	 * MEASURE. Stores info specific to a certain measure such as accidentals
	 * and tupletSpans.
	 */
	private MeiMeasure currentMeasure;

	/**
	 * Store all mei repeat starts and ends.
	 */
	private MeiRepeat repeats;

	private MeiData meiData;

	/**
	 * This allows a sequence to generate some mei stats, such as invalid tempo
	 * and invalid instruments.
	 */
	private MeiStatTracker stats;
        
        /**
         * Adding non-midi storage element here
         * This element is only a demonstration and is
         * purely for developmental purposes
         */
        private MeiSpecificStorage nonMidiStorage;

	/**
	 * Constructor that creates a MIDI Sequence given an MEI filename and also
	 * keeps track of some mei stats using MeiStatTracker reference.
	 * 
	 * @param file
	 *            File object for this.
	 * @param stats
	 *            MeiStatTracker object for this.
	 * @throws javax.sound.midi.InvalidMidiDataException
	 * @throws MeiXmlReader.MeiXmlReadException
	 */
	public MeiSequence(File file, MeiStatTracker stats)
			throws InvalidMidiDataException, MeiXmlReadException {
		this.staffs = new HashMap<>();
		this.currentStaff = new MeiStaff(1); // 1 in case there are no n
												// attributes for staffs within
												// measures
		this.works = new HashMap<>();
		this.currentMdiv = new MeiMdiv();
		this.repeats = new MeiRepeat();
		this.meiData = new MeiData();

		this.document = MeiXmlReader.loadFile(file);
		this.stats = stats;
                
		//MeiSpecificStorage instantiated here
        this.nonMidiStorage = new MeiSpecificStorage();
                
		stats.setFileName(file.getPath());
		documentToSequence();
	}

	/**
	 * Constructor given only a filename. This will generate a unique
	 * MeiStatTracker object.
	 * 
	 * @param filename
	 *            Name of the file to be given to this.
	 * @throws InvalidMidiDataException
	 * @throws MeiXmlReadException
	 */
	public MeiSequence(String filename) throws InvalidMidiDataException,
			MeiXmlReadException {
		this(new File(filename), new MeiStatTracker(filename));
	}

	/**
	 * Constructor given only a file object. This will generate a unique
	 * MeiStatTracker object.
	 * 
	 * @param file
	 *            File object to be given to this.
	 * @throws InvalidMidiDataException
	 * @throws MeiXmlReadException
	 */
	public MeiSequence(File file) throws InvalidMidiDataException,
			MeiXmlReadException {
		this(file, new MeiStatTracker(file.getPath()));
	}

	/**
	 * Constructor given a file name and stats object. This stats object can be
	 * re-used in other files.
	 * 
	 * @param filename
	 *            Name of the file to be given to this.
	 * @param stats
	 *            MeiStatTracker object for this.
	 * @throws InvalidMidiDataException
	 * @throws MeiXmlReadException
	 */
	public MeiSequence(String filename, MeiStatTracker stats)
			throws InvalidMidiDataException, MeiXmlReadException {
		this(new File(filename), stats);
	}

	/**
	 * Java MIDI Sequence created from MeiSequence MEI file input.
	 * @return Java MIDI Sequence created from MeiSequence MEI file input.
	 */
	public Sequence getSequence() {
		return this.sequence;
	}

	/**
	 * @return STRING[] DEFAULTS FOR TESTING PURPOSES
	 */
	protected Map<Integer, MeiWork> getWorks() {
		return this.works;
	}

	/**
	 * @return HASHMAP\<MEISTAFF\> STAFFS FOR TESTING PURPOSES
	 */
	protected Map<Integer, MeiStaff> getStaffs() {
		return this.staffs;
	}

	/**
	 * A stat tracker which currently finds invalid instruments and tempos
	 * in a given mei document.
	 * @return MeiStatTracker created from any invalid data needed.
	 */
	public MeiStatTracker getStats() {
		return this.stats;
	}
        
        /**
         * Returns the non midi stored mei information
         * this implementation is temporary and should be thought through
         * @return non midi mei info
         */
        public MeiSpecificStorage getNonMidiStorage() {
            return this.nonMidiStorage;
        }

	/**
	 * Converts given MEI document to MIDI sequence object. mei is the root
	 * element of any mei document. mei direct children will be meiHead and
	 * music.
	 * 
	 * @throws javax.sound.midi.InvalidMidiDataException
	 */
	private void documentToSequence() throws InvalidMidiDataException {
		sequence = new Sequence(Sequence.PPQ, 256);
		recursiveDFS(document.getRootElement());
	}

	/**
	 * Recursive DFS through MEI document.
	 * This ensures that all tags are seen by the search.
	 * 
	 * @param root The root mei XML element (i.e. mei tag).
	 */
	private void recursiveDFS(MeiElement root) {
		processElement(root);
	}

	/**
	 * Each MEI element processed as accordingly. Some elements may only be
	 * processed within other elements such as a note. Also checks for copyOf
	 * and sameAs attribute.
	 * 
	 * Processing :
	 * 1. LayerChild is created.
	 * 2. StaffBuilder is created.
	 * 3. MeiGeneral is created.
	 * 
	 * @param element Can be any MEI tag.
	 */
	private void processElement(MeiElement element) {
		// See if this element is a copy and then process accordingly
		checkForCopyOf(element);

		String name = element.getName();
		if (LayerChildEnum.contains(name)) {
			checkLayerStart(element);
			LayerChildFactory.buildLayerChild(currentStaff, currentMeasure,
					sequence, element, nonMidiStorage, meiData);
			checkLayerEnd(element);
		} else if (StaffBuilderEnum.contains(name)) {
			StaffBuilderFactory.buildStaffBuilder(sequence, stats, staffs,
					works, currentMdiv, currentStaff, element, meiData);
			processParent(element);
		} else {
			processGeneral(element);
		}
	}

	/**
	 * Process a generic element(parent) and iteratively pass children to
	 * processElement(child).
	 * 
	 * @param parent The mei element whose children will be processed.
	 */
	private void processParent(MeiElement parent) {
		for (MeiElement child : parent.getChildren()) {
			processElement(child);
		}
	}

	/**
	 * Process general mei elements that are sub classes of MeiGeneral or other
	 * unique processing cases such as repeats and incips. Otherwise, children
	 * are found and process accordingly.
	 * Generally, these elements directly impact the recursive function call stack
	 * and so they cannot be naturally implemented into their own class hierarchy.
	 * 
	 * @param element General mei element which will be processed.
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
			break; // used to skip over incip music in metadata

		default:
			processParent(element);
			break;
		}
	}

	/**
	 * Set the currentMdiv to the appropriate movement.
	 * 
	 * @param mdiv mdiv mei element that will be processed.
	 */
	private void processMdiv(MeiElement mdiv) {
		currentMdiv.setCurrentMovement(mdiv);
		processParent(mdiv);
	}

	/**
	 * Ending measure is used for alternative repeats and so a check is made in
	 * order to not repeat the ending on the second DFS pass. The regular
	 * repeat pattern of "rptstart" and "rptend" are used for the actual
	 * repeating mechanism.
	 * 
	 * @param ending ending mei element that will be processed.
	 */
	private void processEnding(MeiElement ending) {
		// If we are not on an ending, then go through and repeat
		if (!repeats.hasEnding()) {
			repeats.setEnding(ending);
			processParent(ending);
			repeats.resetEnding();
		}
		// If we are already on a repeat then check if it is the same ending
		else if (repeats.getEnding().equals(ending.getId())) {
			repeats.resetEndRepeat();
		}
	}

	/**
	 * MEI measure will be processed so that tick offsets between staffs will be
	 * the same. It will also fetch measure children such as tupletSpan and tie
	 * elements to be processed sequentially later.
	 * 
	 * @param measure measure mei element that will be processed.
	 */
	private void processMeasure(MeiElement measure) {
		//Check for a repeat within a measure
		if (repeats.toProcess(measure)) {
			// Set up a new measure object every time we have a new measure
			// This will implicitly reset the hashmaps within MeiMeasure
			// And check for repeats within the measure
			currentMeasure = new MeiMeasure(measure);

			// Process the slurs found in this measure before the measure starts
			List<MeiElement> slurList = measure.getDescendantsByName("slur");
			processSlurs(slurList);

			// Process the measure elements' children
			processParent(measure);
			
			/*
			 * CAUTION
			 * The following code has been thoroughly thought through
			 * and should be changed very carefully.
			 * It allows odd mei encoding (such as ties not ending or offset rests)
			 * errors to not propagate past a measure by
			 * naturally reseting the tick count at the start of every measure.
			 */
			
			// If there is a rest on same staff but different
			// layer then layers will not match up.
			// This forces same ending on each measure
			long longLayer = 0;
			for (MeiStaff thisStaff : staffs.values()) {
				if (thisStaff.getTickLayer() > longLayer) {
					longLayer = thisStaff.getTickLayer();
				}
			}
			// set all staff ticks appropriately
			for (MeiStaff thisStaff : staffs.values()) {
				thisStaff.setTick(thisStaff.getTick() + longLayer);
				thisStaff.setTickLayer(0);
			}
			
			/*
			 * END OF CAUTION
			 */

			// Check and perform a typical (non-ending) repeat
			if (repeats.compareEndID(measure) && !repeats.inRepeat()) {
				repeats.setInRepeat(); // in repeat
				processParent(document.getRootElement()); //DFS through repeat
				repeats.resetInRepeat();// not in repeat
			}
		}
	}

	/**
	 * Setup all the slur data for future use. In particular being able to know
	 * which notes are slurred and not.
	 * @param slurs A list of MEI slur elements found.
     */
	private void processSlurs(List<MeiElement> slurs) {
		for(MeiElement slur: slurs) {
			meiData.getSlurs().addSlur(slur);
		}
	}

	/**
	 * Process the mei staff element accordingly. This will process layers of
	 * staff and will then process any other children normally through
	 * processElement().
	 * 
	 * @param staff staff mei element that will be processed.
	 */
	private void processStaff(MeiElement staff) {
		// Verify n or else assume sequential ordering between staffs
		// if no information about staffs are given then it will start at 1
		// which matches with the staffDefs in the staffs hash
		String nString = staff.getAttribute("n");

		int staffN = (nString != null) ? Integer.parseInt(nString)
				: currentStaff.getN() + 1;

		// This is a check for if the null n attribute was encoded incorrectly
		MeiStaff newStaff;
		if (staffs.containsKey(staffN)) {
			newStaff = staffs.get(staffN);
		} else {
			newStaff = currentStaff;
		}
		currentStaff = newStaff;

		processParent(staff);
	}

	/**
	 * Process a layer by setting the current tick layer count to 0.
	 * 
	 * @param layer layer mei element that will be processed.
	 */
	private void processLayer(MeiElement layer) {
		currentStaff.setTickLayer(0);
		processParent(layer);
	}

	/**
	 * Checks for a copyof or sameas attribute in any mei element and will
	 * process the element with the corresponding id.
	 * 
	 * @param element copyof or sameas mei element that will be processed.
	 */
	private void checkForCopyOf(MeiElement element) {
		String copyof = element.getAttribute("copyof");
		String sameas = element.getAttribute("sameas");
		if (copyof != null) {
			copyof = copyof.replaceAll("#", "");
			MeiElement copyElement = document.getElementById(copyof);
			processParent(copyElement);
		} else if (sameas != null) {
			sameas = sameas.replaceAll("#", "");
			MeiElement sameElement = document.getElementById(sameas);
			processParent(sameElement);
		}
	}

	/**
	 * Checks any non-sequential mei element that contains a start id and sets
	 * retained information accordingly. Currently used for tupletSpan element.
	 * 
	 * @param child child mei element that will be checked for current starting tupletSpans.
	 */
	private void checkLayerStart(MeiElement child) {
		// Check whether a start tuplet or end tuplet is there
		// One note cannot have more than one tuplet value on it
		MeiElement tupletSpan = currentMeasure.getStart("tupletSpan",
				child.getId());
		if (tupletSpan != null) {
			int num = Integer.parseInt(tupletSpan.getAttribute("num"));
			int numbase = Integer.parseInt(tupletSpan.getAttribute("numbase"));
			currentMeasure.setNum(num);
			currentMeasure.setNumBase(numbase);
		}
	}

	/**
	 * Checks any non-sequential mei element that contains an end id and resets
	 * any previous information that needs to be changed. Currently used for
	 * tupletSpan element.
	 * 
	 * @param child child mei element that will be checked for current ending tupletSpans.
	 */
	private void checkLayerEnd(MeiElement child) {
		// Check whether a start tuplet or end tuplet is there
		// One note cannot have more than one tuplet value on it
		// ASSUMPTION
		// This supercedes the tuplet element in importance
		if (currentMeasure.getEnd("tupletSpan", child.getId()) != null) {
			// Add tick tick remainder here
			currentStaff.setTick(currentStaff.getTick()
					+ ConvertToMidi.tickRemainder(currentMeasure.getNum()));
			currentMeasure.setNum(1);
			currentMeasure.setNumBase(1);
		}
	}
}
