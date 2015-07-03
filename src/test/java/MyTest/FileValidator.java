/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyTest;

import ca.mcgill.music.ddmal.mei.MeiXmlReader.MeiXmlReadException;
import java.io.File;
import java.io.IOException;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import org.ddmal.jmei2midi.MeiSequence;

/**
 *
 * @author dinamix
 */
public class FileValidator 
{
    public static Sequence getValidSequence(File file)
            throws Exception {
        Sequence sequence;
        if(file.getPath().matches(".*.mei$")) {
            sequence = getMEISequence(file);
        }
        else {
            sequence = getMIDISequence(file);
        }
        return sequence;
    }

    public static Sequence getMIDISequence(File file)
            throws Exception {
        Sequence sequence = null;
        try {
            sequence = MidiSystem.getSequence(file);
        } 
        catch (IOException e) {
            throw new Exception("The specified path, " + file + ", does not refer to a valid file.");
        } 
        catch (InvalidMidiDataException e) {
            throw new Exception("The specified file, " + file + ", is not a valid MIDI file.");
        }
        return sequence;
    }

    public static Sequence getMEISequence(File file) 
            throws Exception, MeiXmlReadException {
        Sequence sequence = null;
        try {
            MeiSequence meiSequence = new MeiSequence(file);
            sequence = meiSequence.getSequence();
        } 
        catch (InvalidMidiDataException e) {
            throw new Exception("The specified file, " + file + ", is not a valid MIDI file.");
        }
        catch (MeiXmlReadException e) {
            throw new MeiXmlReadException(e.getMessage()); //invalid mei file
        }
        
        return sequence;
    }
}
