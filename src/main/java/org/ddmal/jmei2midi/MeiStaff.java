/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.jmei2midi;

import java.util.HashMap;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
    private int bpm; //converted tempo to beats per minute
    
    
    private int tick; //current tick count of this staff
    private int layerOffset; //included to account for multiple layers
                             //starting at the same time
                             //only need to use with 2 or more layers
    
    
    private String keymode; //major or minor
                            //only do once at the beginning
                            //dont keep updating throughout
    private String keysig; //mei keysig like "2s" or "4f" or "0"
    private HashMap<String,String> keysigMap; //current key / accidentals might need reference
                                              //this is used to check each note
    
    private String label; //usually used for instrument name
    
    
    private String meterCount; //need for things like mRest
    private String meterUnit;
    
    /**
     * Default MeiStaff Constructor.
     * Should not be used because all values assumed.
     * Only created to account for someone forgetting to input
     * required attributes in scoreDef or staffDef.
     */
        public MeiStaff(int n) {
        this.n = n;
        this.computeChannel();
        this.tempo = "Adagio";
        this.label = "Piano";
        this.computeBpm();
        this.tick = 0;
        this.keysig = "0";
        this.keymode = "major";
        this.computeKeysigMap();
        this.meterCount = "4";
        this.meterUnit = "4";
    }
    
    /**
     * General MeiStaff Constructor.
     * @param n
     * @param tempo
     * @param label
     * @param keysig
     * @param keymode
     * @param meterCount
     * @param meterUnit
     */
    public MeiStaff(int n, 
                    String tempo,  
                    String label,
                    String keysig, 
                    String keymode,
                    String meterCount,
                    String meterUnit) {
        this.n = n;
        this.computeChannel();
        this.tempo = tempo;
        this.label = label;
        this.computeBpm();
        this.tick = 0;
        this.keysig = keysig;
        this.keymode = keymode.toLowerCase();
        this.computeKeysigMap();
        this.meterCount = meterCount;
        this.meterUnit = meterUnit;
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
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
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
     * @return the keymode
     */
    public String getKeymode() {
        return keymode;
    }

    /**
     * @param keymode the keymode to set
     * Set to lowercase for consistency
     */
    public void setKeymode(String keymode) {
        this.keymode = keymode.toLowerCase();
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
     * @return the meterCount
     */
    public String getMeterCount() {
        return meterCount;
    }

    /**
     * @param meterCount the meterCount to set
     */
    public void setMeterCount(String meterCount) {
        this.meterCount = meterCount;
    }
    
        /**
     * @return the meterUnit
     */
    public String getMeterUnit() {
        return meterUnit;
    }

    /**
     * @param meterUnit the meterUnit to set
     */
    public void setMeterUnit(String meterUnit) {
        this.meterUnit = meterUnit;
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
     * ***** Special case : C Major is null. *****
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
    
     /**
     * Compare this element to another object.
     * @return 
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        MeiStaff rhs = (MeiStaff) obj;
        return new EqualsBuilder()
                .append(n, rhs.n)
                .append(channel, rhs.channel)
                .append(tempo, rhs.tempo)
                .append(bpm, rhs.bpm)
                .append(tick, rhs.tick)
                .append(layerOffset, rhs.layerOffset)
                .append(keymode, rhs.keymode)
                .append(keysig, rhs.keysig)
                .append(label, rhs.label)
                .append(meterCount, rhs.meterCount)
                .append(meterUnit, rhs.meterUnit)
                .append(keysigMap, this.keysigMap)
                .isEquals();
    }

    /**
     * Overridden hashcode.
     * @return 
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(n)
                .toHashCode();
    }
}
