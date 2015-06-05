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
import java.util.List;
import javax.sound.midi.InvalidMidiDataException;
import org.ddmal.midiUtilities.MidiIO;


/**
 * JUST USED FOR TESTING RUNNING CODE NOW.
 * @author dinamix
 */
public class Main {
    public static void main(String[] args) throws InvalidMidiDataException {
        String root = "/Users/dinamix/Documents/mei/mei-test-set/MEI/chords/";
        File rootFile = new File(root);
        for(String filename : rootFile.list()) {
            MeiSequence test = new MeiSequence(root + filename);
            MidiIO.write(test.getSequence(), "midi/MEI/chords/" 
                                            + filename.replace("mei", "midi"));
        }
        /*MeiDocument doc = MeiXmlReader.loadFile("/Users/dinamix/Documents/mei/music-encoding/samples/MEI2013/Music/Complete examples/Czerny_op603_6.mei");
        List<MeiElement> notes = doc.getElementsByName("note");
        for(MeiElement note : notes) {
            String dur = note.getAttribute("dur");
            if(dur == null) {
                System.out.println(note.getId());
            }
        }*/
    }  
}
