package org.ddmal.jmei2midi.meielements.general;

import org.ddmal.jmei2midi.meielements.staffinfo.MeiSlur;

/**
 * Created by dinamix on 7/11/16.
 */
public class MeiData {
    private MeiSlur slurs;

    public MeiData() {
        slurs = new MeiSlur();
    }

    public MeiSlur getSlurs() {
        return slurs;
    }
}
