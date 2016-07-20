package org.ddmal.jmei2midi.meielements.staffinfo;

import ca.mcgill.music.ddmal.mei.MeiElement;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dinamix on 7/11/16.
 */
public class MeiSlur {
    private Map<String, MeiElement> slurStartID;
    private Map<String, MeiElement> slurEndID;
    private Map<String, MeiElement> currentSlurs;

    public MeiSlur() {
        slurStartID = new HashMap<>();
        slurEndID = new HashMap<>();
        currentSlurs = new HashMap<>();
    }

    public void addSlur(MeiElement slur) {
        if(slur != null) {
            //Remove # from id as this does not correspond to actual xml id
            String startid = slur.getAttribute("startid");
            String endid = slur.getAttribute("endid");
            if(startid != null && endid != null) {
                startid = startid.replaceAll("#","");
                endid = endid.replaceAll("#","");
                if (startid != null && endid != null) {
                    slurStartID.put(startid, slur);
                    slurEndID.put(endid, slur);
                }
            }
        }
    }

    public boolean checkNoteSlur(MeiElement note) {
        if(note != null) {
            String noteid = note.getId();
            if(slurStartID.containsKey(noteid)) {
                currentSlurs.put(noteid, note);
            }
            if(!currentSlurs.isEmpty()) {
                //Therefore we are currently in a slur because we have a current slur
                if(slurEndID.containsKey(noteid)) {
                    MeiElement slur = slurEndID.get(noteid);
                    String startid = slur.getAttribute("startid").replaceAll("#","");
                    String endid = slur.getAttribute("endid").replaceAll("#","");
                    slurStartID.remove(startid);
                    slurEndID.remove(endid);
                    currentSlurs.remove(startid);
                }
                return true;
            }
        }
        return false;
    }
}
