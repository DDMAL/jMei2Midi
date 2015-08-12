/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.jmei2midi.meielements.layerchild;

import ca.mcgill.music.ddmal.mei.MeiElement;
import java.util.List;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;
import org.ddmal.jmei2midi.meielements.general.MeiMeasure;
import org.ddmal.jmei2midi.meielements.meispecific.MeiSpecificStorage;
import org.ddmal.jmei2midi.meielements.staffinfo.MeiStaff;
import org.ddmal.midiUtilities.ConvertToMidi;
import org.ddmal.midiUtilities.MidiBuildEvent;

/**
 * Converts an MEI note into a MIDI note depending on its
 * parent elements and other appropriate elements in the document.
 * <p>These other elements currently include ties, chords and grace notes.</p>
 * @author Tristano Tenaglia
 */
public class MeiNote extends LayerChild {
    private final MeiElement note;
    private final int pitch;
    private final long startTick;
    private final long endTick;
    
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
     * @param currentStaff the current staff to be processed
     * @param currentMeasure the current measure to be processed
     * @param sequence the current sequence to be added to
     * @param note the mei note element to be processed
     */
    public MeiNote(MeiStaff currentStaff, MeiMeasure currentMeasure, 
                   Sequence sequence, MeiElement note,
                   MeiSpecificStorage nonMidiStorage) {
        super(currentStaff, currentMeasure, sequence, note, nonMidiStorage);
        this.note = note;
        
        //Used to use note outisde of function for now
        currentStaff.setLayerChild(note);
        
        //Get the correct note pitch with name, octave and accidental
        this.pitch = getMidiPitch();
        
        //Get the proper start and end Ticks for this note
        this.startTick = currentStaff.getTick() + currentStaff.getTickLayer();
        this.endTick = startTick + getDurToTick();
        
        //Check parents to see how to process this chord
        checkNoteParents(pitch,startTick,endTick);
        
        //Remove current note from currentStaff has
        currentStaff.removeLayerChild("note");
    }
    
    /**
     * Note check to see how the midi note should be processed depending on if
     * the note is within a chord or held by a tie.
     * ASSUMPTION
     * This may need to be altered for tie attributes in chords.
     * @param nPitch the pitch of the current note begin processed
     * @param startTick the start tick of the current note being processed
     * @param endTick the end tick of the current note being processed+
     */
    private void checkNoteParents(int nPitch,
                                  long startTick,
                                  long endTick) {
        Track thisTrack = sequence.getTracks()[currentStaff.getN()-1];
        MeiElement chord = currentStaff.getLayerChild("chord");
        String graceChord = (chord != null) ? chord.getAttribute("grace") : null;
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
            startTick = startTick - (endTick - startTick)/4;
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
     * @param thisTrack the track to which this note on will be added
     * @param nPitch the pitch of the note to be added to thisTrack
     * @param startTick the start tick of the note to be added to thisTrack
     * @param endTick the end tick of the note to be added to thisTrack
     */
    public void addMidiNoteOn(Track thisTrack,
                            int nPitch, 
                            long startTick,
                            long endTick) {
        try {
            thisTrack.add(MidiBuildEvent.createNoteOnEvent(nPitch, 
                                                         startTick, 
                                                         currentStaff.getChannel()));
        }
        catch(InvalidMidiDataException imde) {
            System.out.println("Invalid note " + note);
            System.exit(1);
        }
    }
    
    /**
     * Add the given midi note off event to the specified track.
     * @param thisTrack the track to which this note off will be added
     * @param nPitch the pitch of the note to be added to thisTrack
     * @param startTick the start tick of the note to be added to thisTrack
     * @param endTick the end tick of the note to be added to thisTrack
     */
    public void addMidiNoteOff(Track thisTrack,
                            int nPitch, 
                            long startTick,
                            long endTick) {
        try {
            thisTrack.add(MidiBuildEvent.createNoteOffEvent(nPitch, 
                                                          endTick, 
                                                          currentStaff.getChannel()));
        }
        catch(InvalidMidiDataException imde) {
            System.out.println("Invalid note " + note);
            System.exit(1); 
        }
    }
    
    /**
     * Check tie attribute to see whether we initialize or terminate
     * a given midi notes.
     * i will initialize the note and t will terminate the midi note.
     * if an m ( or other letter ) is found then nothing will be done
     * @param thisTrack the track to which this note will be added
     * @param tie the type of tie given by mei tie attribute or element
     * @param nPitch the pitch of the note to be added to thisTrack
     * @param startTick the start tick of the note to be added to thisTrack
     * @param endTick the end tick of the note to be added to thisTrack
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
     * Convert the current mei element note to the appropriate midi
     * integer pitch depending on the accidentals of the key and current measure.
     * @return the appropriate midi integer value of the pitch
     */
    private int getMidiPitch() {
        //Attributes taken here to be processed here
        String pname = note.getAttribute("pname");
        String oct = note.getAttribute("oct");
        String accid = getAccidental();
        //Get the proper pitch given accidental or key signature
        int nPitch;
        if(attributeExists(accid) && attributeExists(oct)) {
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
     * Get appropriate accidental depending on if we have an accid
     * attribute or an accid child, else returns null.
     * @return accid given as an mei letter from either accid attribute or element
     */
    private String getAccidental() {
        String accidAttr = note.getAttribute("accid");
        List<MeiElement> accidChild = note.getChildrenByName("accid");
        
        if(attributeExists(accidAttr)) {
            return accidAttr;
        }
        else if(!accidChild.isEmpty()){
            return accidChild.get(0).getAttribute("accid");
        }
        else {
            return null;
        }
    }
    
    @Override
    /**
     * Converts given duration string to a long tick value for midi.
     * thiStaff can be populated with a tuplet or note which will then
     * be used to compute tuplet and dot values in the duration.
     * ASSUMPTION
     * This assumes that if not dur is given in element or chord
     * then the note takes up a full measure.
     * @return long tick value of dur string
     */
    public long getDurToTick() {
        //CHECK IN HERE FOR TUPLETS FROM tupletSpan
        //If in tuplet then use that or else check tupletSpan
        int num = currentMeasure.getNum();
        int numbase = currentMeasure.getNumBase();
        String dur = getDurString();
        int dots = getDots();
        return ConvertToMidi.durToTick(dur,num,numbase,dots);
    }
    
    @Override
    /**
     * Fetches the appropriate duration of a given Mei element depending on
     * whether it's a note, a chord or a rest/space.
     * If no duration is found, then a dur = "0" is returned.
     * @return duration of element in string form
     */
    public String getDurString() {
        String dur;
        //For notes and rests with dur attributes
        if(attributeExists(note.getAttribute("dur"))) {
            dur = note.getAttribute("dur");
            if(dur.contains("breve") || dur.contains("long")) {
                dur = "0.5";
            }
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
    
    @Override
    /**
     * Fetches the appropriate dots for the given mei note depending
     * on if it's a chord or a note.
     * If no dots are found, then a dots = 0 value is returned.
     * @return number of corresponding dots for note
     */
    public int getDots() {
        int dots;
        if(currentStaff.getLayerChild("chord") != null &&
           currentStaff.getLayerChild("chord").getAttribute("dots") != null) {
            dots = Integer.parseInt(currentStaff.getLayerChild("chord").getAttribute("dots"));
        }
        else {
            dots = getAttributeToInt("dots", note, 0);
        }
        return dots;
    }
}
