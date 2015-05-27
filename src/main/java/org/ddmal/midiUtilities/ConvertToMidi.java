/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.midiUtilities;

/**
 *
 * @author dinamix
 */
public class ConvertToMidi {
    
    /**
     * Converts tempo string to bpm int based on Wikipedia data of 
     * basic tempo markings.
     * http://en.wikipedia.org/wiki/Tempo
     * @param tempo
     * @return tempo string converted to bpm int
     */
    public static int tempoToBpm(String tempo) {
        int bpm = 90; //default for now
        tempo = tempo.toLowerCase(); //for consistency put all to lower case

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
        return bpm;
    }
    
    /**
     * Converts MEI note to appropriate Midi int using note element attributes
     * pname, oct and accid.
     * @param pname
     * @param oct
     * @return midi int given mei note values
     */
    public static int NoteToMidi(String pname, String oct, String accid) {
        //difference between accid and accid.ges?
        int midiNote = 0; //start with C0
        int octave = Integer.parseInt(oct)*12;//get proper octave
        
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
            if(accid.equalsIgnoreCase("s")) {
                midiNote++;    
            }
            else if(accid.equalsIgnoreCase("ss")) {
                midiNote += 2;
            }
            else if(accid.equalsIgnoreCase("f")) {
                midiNote--;
            }
            else if(accid.equalsIgnoreCase("ff")) {
                midiNote -= 2;
            }
        }
        return midiNote + octave;
    }
}
