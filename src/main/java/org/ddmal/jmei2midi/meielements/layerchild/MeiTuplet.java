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
 *
 * @author dinamix
 */
public class MeiTuplet extends LayerChild {
    private MeiElement tuplet;
    
    /**
     * Process a tuplet element by using the num and numbase attribute.
     * NOTE
     * If the current measure has a tuplet span and a tuplet for the same
     * notes then the remainder will be calculated twice.
     * @param currentStaff
     * @param currentMeasure
     * @param sequence
     * @param tuplet 
     */
    public MeiTuplet(MeiStaff currentStaff, MeiMeasure currentMeasure, Sequence sequence,
                     MeiElement tuplet) {
        super(currentStaff, currentMeasure, sequence, tuplet);
        this.tuplet = tuplet;
        
        //Give tuplet to MeiStaff
        currentStaff.setLayerChild(tuplet);
        int num = getAttributeToInt("num", tuplet, 1);
        int numbase = getAttributeToInt("numbase", tuplet, 1);
        currentMeasure.setNum(num);
        currentMeasure.setNumBase(numbase);
            
        //Process here with tuplet info
        for(MeiElement child : tuplet.getChildren()) {
            LayerChildFactory.buildLayerChild(currentStaff, currentMeasure, sequence, child);
        }
            
        //Add mod of odd number to tick once it finishes the tuplet
        //And remove tuplet from hash
        currentStaff.setTick(currentStaff.getTick() 
                          + ConvertToMidi.tickRemainder(num));
        currentMeasure.setNum(1);
        currentMeasure.setNumBase(1);
        currentStaff.getLayerChildMap().remove("tuplet");
    }
    
    @Override
    public long getDurToTick() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getDurString() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getDots() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
