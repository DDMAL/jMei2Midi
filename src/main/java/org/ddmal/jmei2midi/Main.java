/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.jmei2midi;

import ca.mcgill.music.ddmal.mei.MeiDocument;
import ca.mcgill.music.ddmal.mei.MeiElement;
import ca.mcgill.music.ddmal.mei.MeiXmlReader;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import javax.sound.midi.InvalidMidiDataException;
import org.ddmal.midiUtilities.MidiIO;


/**
 * JUST USED FOR TESTING RUNNING CODE NOW.
 * @author dinamix
 */
public class Main {
    public static void main(String[] args) throws InvalidMidiDataException {
        String root = "/Users/dinamix/Documents/mei/mei-test-set/MEI/repeats/";
        File rootFile = new File(root);
        for(String filename : rootFile.list()) {
            MeiSequence test = new MeiSequence(root + filename);
            MidiIO.write(test.getSequence(), "midi/MEI/repeats/" 
                                            + filename.replace("mei", "midi"));
        }
        
        /*String file = "/Users/dinamix/Documents/mei/mei-test-set/MEI/repeats/3-repeats.mei";
        MeiSequence test = new MeiSequence(file);
        MidiIO.write(test.getSequence(), "midi/MEI/Complete examples/Saint-Saens_LeCarnevalDesAnimmaux.midi");*/
        
        /*MeiDocument doc = MeiXmlReader.loadFile("/Users/dinamix/Documents/mei/music-encoding/samples/MEI2013/Music/Complete examples/Czerny_op603_6.mei");
        List<MeiElement> measures = doc.getElementsByName("measure");
        int i = 1;
        for(MeiElement measure : measures) {
            List<MeiElement> ties = measure.getDescendantsByName("tie");
            for(MeiElement tie : ties) {
                String n = tie.getAttribute("endid");
                if(n == null) {
                    System.out.println(measure.getAttribute("n") + " " + tie.getId() + " " + i);
                }
                i++;
            }
        }*/
    }      
}
