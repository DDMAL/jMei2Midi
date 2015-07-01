/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.jmei2midi.meielements.layerchild;

import ca.mcgill.music.ddmal.mei.MeiElement;
import java.util.ArrayList;
import java.util.List;
import javax.sound.midi.Sequence;
import org.ddmal.jmei2midi.meielements.general.MeiMeasure;
import org.ddmal.jmei2midi.meielements.staffinfo.MeiStaff;
import org.ddmal.midiUtilities.ConvertToMidi;

/**
 *
 * @author dinamix
 */
public class MeiChord extends LayerChild {
    private MeiElement chord;
    private long tick;
    private List<MeiNote> noteList;

    public MeiChord(MeiStaff currentStaff, MeiMeasure currentMeasure, Sequence sequence,
                    MeiElement chord) {
        super(currentStaff, currentMeasure, sequence, chord);
        this.chord = chord;
        noteList = new ArrayList<>();
        
        //Set chord as layerChild for future use
        currentStaff.setLayerChild(chord);
        
        //Get duration of a note because they will all be the same
        //Put dur into layerChild hash here
        String tickString = getDurString();
        this.tick = getDurToTick();
        
        //Process chord children
        //Children are obtained by name because sometimes
        //chord has null children which should not be passed down
        for(MeiElement note : chord.getChildrenByName("note")) {
            noteList.add(new MeiNote(currentStaff, currentMeasure, sequence, note));
        }
        
        //Set layer tick appropriately
        currentStaff.setTickLayer(currentStaff.getTickLayer() + tick);
        currentStaff.removeLayerChild("chord");
    }
    
    @Override
    /**
     * Converts given duration string to a long tick value for midi.
     * thiStaff can be populated with a tuplet or note which will then
     * be used to compute tuplet and dot values in the duration.
     * @param dur
     * @return long tick value of dur string
     */
    protected long getDurToTick() {
        //CHECK IN HERE FOR TUPLETS FROM tupletSpan
        //If in tuplet then use that or else check tupletSpan
        int num = currentMeasure.getNum();
        int numbase = currentMeasure.getNumBase();
        String dur = getDurString();
        int dots = getDots();
        return ConvertToMidi.durToTick(dur,num,numbase,dots);
    }

    @Override
    protected String getDurString() {
        String chordDur = chord.getAttribute("dur");
        if(!attributeExists(chordDur)) {
            List<MeiElement> notes = chord.getChildrenByName("note");
            chordDur = notes.get(0).getAttribute("dur");
        }
        return chordDur;
    }
    
    //This is not used right now
    //Could use it to pass down dots from chord to notes
    @Override
    protected int getDots() {
        return getAttributeToInt("dots", chord, 0);
    }
}
