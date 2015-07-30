/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.jmei2midi.meielements.layerchild;

import ca.mcgill.music.ddmal.mei.MeiElement;
import javax.sound.midi.Sequence;
import org.ddmal.jmei2midi.meielements.general.MeiMeasure;
import org.ddmal.jmei2midi.meielements.staffinfo.MeiStaff;
import org.ddmal.midiUtilities.ConvertToMidi;

/**
 * A full measure rest which creates a silence for an entire measure
 * by simply incrementing the tick count appropriately.
 * @author Tristano Tenaglia
 */
public class MeiMrest extends LayerChild {
	
    /**
     * The mei rest element. 
     */
    private final MeiElement rest;
    
    /**
     * The start tick of this rest. 
     */
    private final long startTick;
    
    /**
     * The end tick of this rest. 
     */
    private final long endTick;
    
    /**
     * Process a rest by only adding to the layer tick count of currentStaff
     * without actually creating any midi events.
     * @param currentStaff the current MeiStaff being processed
     * @param currentMeasure the current MeiMeasure being processed
     * @param sequence the current MIDI sequence being added to
     * @param rest the mei rest element being processed
     */
    public MeiMrest(MeiStaff currentStaff, MeiMeasure currentMeasure, Sequence sequence,
                   MeiElement rest) {
        super(currentStaff, currentMeasure, sequence, rest);
        this.rest = rest;
        
        //Get the proper start and end Ticks for this rest
        this.startTick = currentStaff.getTick() + currentStaff.getTickLayer();
        this.endTick = startTick + getDurToTick();
        
        //Change the layers tick accordingly
        currentStaff.setTickLayer(endTick - currentStaff.getTick());
    }
    
    @Override
    /**
     * Converts given duration string to a long tick value for midi.
     * thiStaff can be populated with a tuplet or note which will then
     * be used to compute tuplet and dot values in the duration.
     * ASSUMPTION
     * This assumes that if not dur is given in element or chord
     * then the note has a duration of "0".
     * @return long tick value of dur string
     */
    public long getDurToTick() {
        //COULD CHECK IN HERE FOR TUPLETS FROM tupletSpan
        //If in tuplet then use that or else check tupletSpan
        int num = currentMeasure.getNum();
        int numbase = currentMeasure.getNumBase();
        String dur = getDurString();
        int dots = getDots();
        return ConvertToMidi.durToTick(dur,num,numbase,dots);
    }
    
    @Override
    /**
     * Fetches the appropriate duration of a given Mei rest depending on
     * whether it's a note, a chord or a rest/space.
     * If no duration is found, then a dur = "0" is returned.
     * @return duration of rest in string form
     */
    public String getDurString() {
        double count = Integer.parseInt(currentStaff.getMeterCount());
        double unit = Integer.parseInt(currentStaff.getMeterUnit());
        String dur = String.valueOf(unit / count);
        return dur;
    }
    
    @Override
    /**
     * Fetches the appropriate dots for the given mei element depending
     * on if it's a chord or a note.
     * If no dots are found, then a dots = 0 value is returned.
     * @return number of corresponding dots for element
     */
    public int getDots() {
        int dots = getAttributeToInt("dots", rest, 0);
        return dots;
    }   
}
