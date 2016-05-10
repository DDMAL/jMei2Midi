/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.jmei2midi.meielements.meispecific;

import ca.mcgill.music.ddmal.mei.MeiElement;
import org.ddmal.jmei2midi.meielements.general.MeiMeasure;

/**
 * An example of a non-midi information class that can contain mei data.
 * This for example is analogous to the grace attribute from an mei note element.
 * 
 * @author Tristano Tenaglia
 */
public class MeiGraceNote extends MeiSpecific {

    private final String graceAttribute;
    private final String pitch;
    private final long tick;
    private final int channel;

    public MeiGraceNote(MeiElement element, MeiMeasure measure, String pitch, long tick, int channel) {
        super(element);
        this.pitch = pitch;
        this.tick = tick;
        this.channel = channel;

        graceAttribute = element.getAttribute("grace");
        type = "graceNote";

        String pname = element.getAttribute("pname");
        value = attributeExists(pname) ? pname : "No note name in MEI";

        String measureNumber = measure.getMeasureNumber();
        locationInScore = attributeExists(measureNumber) ? measureNumber : "No measure number specified in MEI";
    }
    
    public String getGraceAttribute() {
        return graceAttribute;
    }

    public String getPitch() {
        return pitch;
    }

    public long getTick() {
        return tick;
    }

    public int getChannel() {
        return channel;
    }
}
