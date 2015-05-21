/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.jmei2midi;

import java.util.HashMap;

/**
 * MeiStaff will keep track of each staff element in an MEI document.
 * Specifically will hold attributes n (channel), label (instrument),
 * key.sig (keysig), meter.count (meter)...
 * @author dinamix
 */
public class MeiStaff {
    private int n; //n attribute in mei document
    private int channel; //converted n attribute to fit in MIDI channels/tracks
    private String tempo; //given string tempo
    private String label; //usually used for instrument name
    private int bpm; //converted tempo to beats per minute
    private int tick; //current tick count of this staff
    private int layerOffset; //included to account for multiple layers
                             //starting at the same time
                             //only need to use with 2 or more layers
    private String quality; //major or minor
                            //only do once at the beginning
                            //dont keep updating throughout
    private String keysig; //mei keysig like "2s" or "4f" or "0"
    private HashMap<String,String> keysigMap; //current key / accidentals might need reference
                                              //this is used to check each note
    private String meter; //need for things like mRest
    
    public MeiStaff(int n, 
                    String tempo,  
                    String label,
                    String keysig, 
                    String meter) {
        this.n = n;
        this.computeChannel();
        this.tempo = tempo;
        this.label = label;
        this.computeBpm();
        this.tick = 0;
        this.keysig = keysig;
        this.computeKeysigMap();
        this.meter = meter;
    }

    /**
     * @return the n
     */
    public int getN() {
        return n;
    }

    /**
     * @param n the n to set
     */
    public void setN(int n) {
        this.n = n;
        computeChannel();
    }
    
    /**
     * Computes the appropriate MIDI channel given the MEI n attribute.
     * This is required for each instrument.
     */
    private void computeChannel() {
        if(this.n < 9) {
            this.channel = this.n;
        }
        else if(this.n < 16) {
            this.channel = this.n + 1;
        }
        else {
            this.channel = 15;
        }
    }
    
    /**
     * @return the keysig
     */
    public String getKeysig() {
        return keysig;
    }
    
    /**
     * @param keysig
     * @set the keysig
     * @set the keysigMap
     */
    public void setKeysig(String keysig) {
        this.keysig = keysig;
        this.computeKeysigMap();
    }

    /**
     * @return the keysigMap
     */
    public HashMap<String,String> getKeysigMap() {
        return keysigMap;
    }

    /**
     * Used to copy over keysigMap without recomputing HashMap with computeKey()
     * @param keysig the keysig to set
     * @param keysigMap the keysigMap to set
     */
    public void setKeysigMap(String keysig, HashMap<String,String> keysigMap) {
        this.keysig = keysig;
        this.keysigMap = keysigMap;
    }

    /**
     * @return the meter
     */
    public String getMeter() {
        return meter;
    }

    /**
     * @param meter the meter to set
     */
    public void setMeter(String meter) {
        this.meter = meter;
    }

    /**
     * @return the tempo
     */
    public String getTempo() {
        return tempo;
    }

    /**
     * @param tempo the tempo to set
     * Will change bpm accordingly;
     */
    public void setTempo(String tempo) {
        this.tempo = tempo;
        computeBpm();
    }

    /**
     * @return the bpm
     */
    public int getBpm() {
        return bpm;
    }
    
    /**
     * This will convert a String to an appropriate bpm equivalent
     * (default = 90).
     * Will need to find Adagio... norms and do string conversion.
     */
    private void computeBpm() {
        bpm = 90;
    }
    
    /**
     * Returns a HashMap for the key signature and this should be checked with
     * each note for any accidental in the given key.
     * HashMap holds key,value pair as the specified accidental in the key
     * signature also dependent on the current key.
     * @param keysignature the value of the MEI key.sig 
     * @return 
     */
    private void computeKeysigMap() {
        int numberOfAccidentals = Integer.parseInt(keysig.substring(0,1));
        if(numberOfAccidentals == 0) {
            keysigMap = null; //null means C major
        }
        else {
            HashMap<String, String> keysignature = new HashMap<String, String>();
            if (keysig.substring(1, 2).equals("s")) {
                String keys[] = {"f", "c", "g", "d", "a", "e", "b"};
                for (int i = 0; i < numberOfAccidentals; i++) {
                    keysignature.put(keys[i], keys[i]);
                }
            } else if (keysig.substring(1, 2).equals("f")) {
                String keys[] = {"b", "e", "a", "d", "g", "c", "f"};
                for (int i = 0; i < numberOfAccidentals; i++) {
                    keysignature.put(keys[i], keys[i]);
                }
            }
            keysigMap = keysignature;
        }
    }
}
