/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.midiUtilities;

import java.util.HashMap;
import org.ddmal.jmei2midi.MeiStatTracker;

/**
 *
 * @author dinamix
 */
public class ConvertToMidiWithStats {
    
    //Can't use this hash because to many variations on instruments.
    //Leaving here for the time being.
    /*public static final HashMap<String, Integer> midiInstr = new HashMap<>();
    static {
        midiInstr.put("piano", 0);
        midiInstr.put("cello", 42);
        midiInstr.put("violin", 40);
        midiInstr.put("viola", 41);
        midiInstr.put("contrabass", 43);
        midiInstr.put("soprano sax", 64);
        midiInstr.put("alto sax", 65);
        midiInstr.put("tenor sax", 66);
        midiInstr.put("baritone sax", 67);
        midiInstr.put("harpsichord", 6);
        midiInstr.put("clavichord", 7);
        midiInstr.put("glockenspiel", 11);
        midiInstr.put("tubular bell", 16);
        midiInstr.put("organ", 19);
        midiInstr.put("guitar", 24);
        midiInstr.put("harp", 46);
        midiInstr.put("timpani", 49);
        midiInstr.put("trumpet", 56);
        midiInstr.put("trombone", 57);
        midiInstr.put("tuba", 58);
        midiInstr.put("euphonium", 58);
        midiInstr.put("french horn", 60);
        midiInstr.put("brass", 61);
        midiInstr.put("sax", 65);
        midiInstr.put("oboe", 68);
        midiInstr.put("english horn", 69);
        midiInstr.put("horn", 69);
        midiInstr.put("bassoon", 70);
        midiInstr.put("clarinet", 71);
        midiInstr.put("piccolo", 72);
        midiInstr.put("flute", 73);
        midiInstr.put("fiddle", 110);
        midiInstr.put("voice", 54);
        midiInstr.put("soprano", 54);
        midiInstr.put("sopran", 54);
        midiInstr.put("alto", 54);
        midiInstr.put("tenor", 54);
        midiInstr.put("baritone", 54);
        midiInstr.put("bass", 54);
    }*/
    
    /**
     * Converts a string percussion from mei into an appropriate
     * midi instrument note number in channel 9.
     * -1 is returned for an invalid instrument so that validity
     * can be check when comparing mei element labels and metadata.
     * Note: Some words are cut short because examples were found
     * with this abbrevation and because it does not impact the actual
     * search of the word, an abbreviation makes for a more 
     * thorough check.
     * http://www.midi.org/techspecs/gm1sound.php
     * @param instr
     * @param stats
     * @return 
     */
    public static int instrToPerc(String instr, MeiStatTracker stats) {
        int percInstr = 0;
        if(instr == null) {
            return -1;
        }
        instr=instr.toLowerCase();
        if(instr.contains("snare")) {
            percInstr = 38;
        }
        else if(instr.contains("bass drum")) {
            percInstr = 35;
        }
        else {
            percInstr = -1;
            stats.addInvalidInstrument(instr);
        }
        return percInstr;
    }
    
    /**
     * NOTE: COULD USE A HASHMAP HERE BUT IMPROPER SPELLING FROM MEI USERS
     * MAKES ME SOMEWHAT HESITANT
     * Converts a string instrVoice from mei into an appropriate
     * midi instrument number.
     * -1 is returned for an invalid instrument so that validity
     * can be check when comparing mei element labels and metadata.
     * Note: Some words are cut short because examples were found
     * with this abbrevation and because it does not impact the actual
     * search of the word, an abbreviation makes for a more 
     * thorough check.
     * http://www.midi.org/techspecs/gm1sound.php
     * @param instr
     * @param stats
     * @return midi number of string instr
     */
    public static int instrToMidi(String instr, MeiStatTracker stats) {
        if(instr == null) {
            return -1;
        }
        int midiInstr;
        instr = instr.toLowerCase();
        if(instr.contains("piano")) {
            midiInstr = 0;
        }
        else if(instr.contains("double bass")) {
            midiInstr = 34;
        }
        else if(instr.contains("cello")) {
            midiInstr = 42;
        }
        else if(instr.contains("violin")) {
            midiInstr = 40;
        }
        else if(instr.contains("viola")) {
            midiInstr = 41;
        }
        else if(instr.contains("contrabass")) {
            midiInstr = 43;
        }
        else if(instr.contains("soprano sax")) {
            midiInstr = 64;
        }
        else if(instr.contains("alto sax")) {
            midiInstr = 65;
        }
        else if(instr.contains("tenor sax")) {
            midiInstr = 66;
        }
        else if(instr.contains("baritone sax")) {
            midiInstr = 67;
        }
        else if(instr.contains("harpsi")) {
            midiInstr = 6;
        }
        else if(instr.contains("clavi")) {
            midiInstr = 7;
        }
        else if(instr.contains("glocken")) {
            midiInstr = 11;
        }
        else if(instr.contains("tubular bell")) {
            midiInstr = 16;
        }
        else if(instr.contains("organ")) {
            midiInstr = 19;
        }
        else if(instr.contains("guitar")) {
            midiInstr = 24;
        }
        else if(instr.contains("harp")) {
            midiInstr = 46;
        }
        else if(instr.contains("timpani")) {
            midiInstr = 49;
        }
        else if(instr.contains("trumpet")) {
            midiInstr = 56;
        }
        else if(instr.contains("trombone")) {
            midiInstr = 57;
        }
        else if(instr.contains("tuba") ||
                instr.contains("euphonium")) {
            midiInstr = 58;
        }
        else if(instr.contains("french horn")) {
            midiInstr = 60;
        }
        else if(instr.contains("brass")) {
            midiInstr = 61;
        }
        else if(instr.contains("sax")) {
            midiInstr = 65;
        }
        else if(instr.contains("oboe")) {
            midiInstr = 68;
        }
        else if(instr.contains("english") ||
                instr.contains("horn")) {
            midiInstr = 69;
        }
        else if(instr.contains("bassoon")) {
            midiInstr = 70;
        }
        else if(instr.contains("clarin")) {
            midiInstr = 71;
        }
        else if(instr.contains("piccolo")) {
            midiInstr = 72;
        }
        else if(instr.contains("flute")) {
            midiInstr = 73;
        }
        else if(instr.contains("recorder")) {
            midiInstr = 74;
        }
        else if(instr.contains("fiddle")) {
            midiInstr = 110;
        }
        else if(instr.contains("voice") ||
                instr.contains("sopran") ||
                instr.contains("alt") ||
                instr.contains("tenor") ||
                instr.contains("barit") ||
                instr.contains("bass")) {
            midiInstr = 54;
        }
        else {
            //Return -1 for invalid instrument and record stats
            midiInstr = -1;
            stats.addInvalidInstrument(instr);
        }
        return midiInstr;
    }
    
    /**
     * Converts tempo string to bpm int based on Wikipedia data of 
     * basic tempo markings.
     * http://en.wikipedia.org/wiki/Tempo
     * @param tempo
     * @param stats
     * @return tempo string converted to bpm int
     */
    public static int tempoToBpm(String tempo, MeiStatTracker stats) {
        int bpm = 90; //default for now
        tempo = (tempo != null) ? tempo.toLowerCase() : "default"; //for consistency put all to lower case

        if(tempo.contains("larghissimo")) {
            bpm = 24; 
        }
        else if(tempo.contains("grave")) {
            bpm = 35;
        }
        else if(tempo.contains("largo")) {
            bpm = 50;
        }
        else if(tempo.contains("lento")) {
            bpm = 55;
        }
        else if(tempo.contains("larghetto")) {
            bpm = 63;
        }       
        else if(tempo.contains("adagio")) {
            bpm = 71;
        }
        else if(tempo.contains("adagietto")) {
            bpm = 74;
        }
        else if(tempo.contains("default")) {
            bpm = 90;
        }
        else if(tempo.contains("andante")) {
            bpm = 92;
        }
        else if(tempo.contains("andantino")) {
            bpm = 94;
        }
        else if(tempo.contains("marcia moderato")) {
            bpm = 84;
        }
        else if(tempo.contains("andante moderato")) {
            bpm = 102;
        }
        else if(tempo.contains("moderato")) {
            bpm = 114;
        }
        else if(tempo.contains("allegretto")) {
            bpm = 116;
        }
        else if(tempo.contains("allegro moderato")) {
            bpm = 118;
        }
        else if(tempo.contains("allegro")) {
            bpm = 144;
        }
        else if(tempo.contains("vivace")) {
            bpm = 172;
        }
        else if(tempo.contains("vivacissimo") ||
                tempo.contains("allegrissimo") ||
                tempo.contains("allegro vivace")) {
            bpm = 174;
        }
        else if(tempo.contains("presto")) {
            bpm = 184;
        }
        else if(tempo.contains("prestissimo")) {
            bpm = 200;
        }
        else {
            //Add invalid stats
            stats.addInvalidTempo(tempo);
        }
        return bpm;
    }
    
    /**
     * Converts MEI note to appropriate Midi int using note element attributes
     * pname, oct and accid.
     * This will throw an exception through the java midi sequencer
     * if the notes are out of midi bounds.
     * @param pname
     * @param oct
     * @return midi int given mei note values
     */
    public static int NoteToMidi(String pname, String oct, String accid) {
        //difference between accid and accid.ges?
        int midiNote = 0; //start with C0
        int octave = 1;
        if(oct != null && !oct.equals("0")) {
                octave = (Integer.parseInt(oct)+1)*12;//get proper octave
        }
        else {
                octave = 0;
        }
        
        //Get midi base note
        if(pname.equalsIgnoreCase("c")) {
            midiNote = 0;
        }
        else if(pname.equalsIgnoreCase("d")) {
            midiNote = 2;
        }
        else if(pname.equalsIgnoreCase("e")) {
            midiNote = 4;
        }
        else if(pname.equalsIgnoreCase("f")) {
            midiNote = 5;
        }
        else if(pname.equalsIgnoreCase("g")) {
            midiNote = 7;
        }
        else if(pname.equalsIgnoreCase("a")) {
            midiNote = 9;
        }
        else if(pname.equalsIgnoreCase("b")) {
            midiNote = 11;
        }
        
        //Account for accidentals if not null
        if(accid != null) {
            if(accid.equalsIgnoreCase("ss") ||
                    accid.equalsIgnoreCase("x")) {
                midiNote += 2;
            }
            else if(accid.equalsIgnoreCase("ff")) {
                midiNote -= 2;
            }
            else if(accid.contains("s")) {
                midiNote++;    
            }
            else if(accid.contains("f")) {
                midiNote--;
            }
        }
        return midiNote + octave;
    }
}
