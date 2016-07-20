package org.ddmal.jmei2midi.meielements.meispecific;

import ca.mcgill.music.ddmal.mei.MeiElement;
import org.ddmal.jmei2midi.meielements.general.MeiMeasure;

/**
 * Created by dinamix on 7/11/16.
 */
public class MeiSlurNote extends MeiSpecific {
    public MeiSlurNote(MeiElement element, MeiMeasure measure) {
        super(element);
        type = "slurNote";
        String measureNumber = measure.getMeasureNumber();
        locationInScore = attributeExists(measureNumber) ? measureNumber : "No measure number specified in MEI";
    }
}
