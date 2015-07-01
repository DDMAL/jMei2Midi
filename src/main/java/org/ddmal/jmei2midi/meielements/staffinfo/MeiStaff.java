/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.jmei2midi.meielements.staffinfo;

import ca.mcgill.music.ddmal.mei.MeiElement;
import java.util.HashMap;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.ddmal.midiUtilities.ConvertToMidi;

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
    
    
    private long tick; //current tick count of this staff
    private long layerOffset; //included to account for multiple layers
                             //starting at the same time
                             //only need to use with 2 or more layers
    
    
    private String keymode; //major or minor
                            //only do once at the beginning
                            //dont keep updating throughout
    private String keysig; //mei keysig like "2s" or "4f" or "0"
    private HashMap<String,String> keysigMap; //current key / accidentals might need reference
                                              //this is used to check each note
    
    private String label; //usually used for instrument name
    private int midiLabel;
    
    
    private String meterCount; //need for things like mRest
    private String meterUnit;
    
    
    private HashMap<String,MeiElement> layerChild; //holds all layer children names
                                                   //as keys with the element itself
                                                   //as values
    
    /**
     * Default MeiStaff Constructor.
     * Should not be used because all values assumed.
     * Only created to account for someone forgetting to input
     * some attributes in scoreDef or staffDef such as in
     * Debussy Mandoline where the keymode and keysig are "unclear".
     * Label and tempo will be adjusted accordingly.
     */
    public MeiStaff(int n) {
        this.n = n;
        this.computeChannel();
        this.tempo = "default";
        this.computeBpm();
        this.label = "default";
        this.tick = 0;
        this.layerOffset = 0;
        this.keysig = "0";
        this.keymode = "major";
        this.computeKeysigMap();
        this.meterCount = "4";
        this.meterUnit = "4";
        layerChild = new HashMap<>();
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
        this.computeBpm();
        this.setLabel(label);
        this.tick = 0;
        this.layerOffset = 0;
        this.keysig = keysig;
        this.keymode = keymode.toLowerCase();
        this.computeKeysigMap();
        this.meterCount = meterCount;
        this.meterUnit = meterUnit;
        layerChild = new HashMap<>();
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
     * @return channel
     */
    public int getChannel() {
        return this.channel;
    }
       
    /**
     * Computes the appropriate MIDI channel given the MEI n attribute.
     * Want to start from 0 but n start from 1.
     * Want to add one if we have 9-15 so that we skip over channel 9.
     * If n is >= 16, then we use channel 15 for remaining tracks.
     * This is required for each instrument.
     */
    private void computeChannel() {
        if(this.n <= 9) {
            this.channel = this.n - 1;
        }
        else if(this.n < 16) {
            this.channel = this.n;
        }
        else {
            this.channel = 15;
        }
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }
    
    public void setLabel(String label) {
        this.label = label;
        if(ConvertToMidi.instrToMidi(label) > -1) {
            this.computeMidiLabel();
        }
        else if(ConvertToMidi.instrToPerc(label) > -1) {
            this.computeMidiPerc();
            this.channel = 9;
        }
    }

    /**
     * Optimization to set a percussion instrument once you know that it
     * is a percussion instrument.
     * Used for verification of instrument in MeiSequence.
     * @param label the label to set
     * @param midiPerc
     */
    public void setLabelPerc(String label, int midiPerc) {
        this.label = label;
        this.midiLabel = midiPerc;
        this.channel = 9;
    }
    
    /**
     * Optimization to set a instrument once you know that it
     * is a instrument.
     * @param label
     * @param midiLabel 
     */
    public void setLabelInstr(String label, int midiLabel) {
        this.label = label;
        this.midiLabel = midiLabel;
    }
    
    /**
     * @return midi conversion of label string
     */
    public int getMidiLabel() {
        return midiLabel;
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
     * @return current tick before layers
     */
    public long getTick() {
        return this.tick;
    }
    
    /**
     * @return current tick within a layer
     */
    public long getTickLayer() {
        return this.layerOffset;
    }
    
    /**
     * Should be at the end of a staff element.
     * @param tick 
     */
    public void setTick(long tick) {
        this.tick = tick;
    }
    
    /**
     * Should be used within a layer element.
     * @param tick 
     */
    public void setTickLayer(long tick) {
        this.layerOffset = tick;
    }
    
    /**
     * Should be updated during a layer and can reset for multiple layers.
     * @param layerOffset 
     */
    public void setLayerOffset(int layerOffset) {
        this.layerOffset = layerOffset;
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
    
    public HashMap<String,MeiElement> getLayerChildMap() {
        return layerChild;
    }
    
    public MeiElement getLayerChild(String element) {
        return layerChild.get(element);
    }
    
    public void setLayerChild(MeiElement element) {
        layerChild.put(element.getName(), element);
    }
    
    public void removeLayerChild(String elementName) {
        layerChild.remove(elementName);
    }
    
    /**
     * This will convert a String to an appropriate bpm equivalent
     * (default = 90).
     * Will need to find Adagio... norms and do string conversion.
     */
    private void computeBpm() {
        this.bpm = ConvertToMidi.tempoToBpm(tempo);
    }
    
    /**
     * This will convert a String to an appropriate midi
     * instrument equivalent.
     * May return -1 which would cause a Midi exception.
     */
    private void computeMidiLabel() {
        this.midiLabel = ConvertToMidi.instrToMidi(this.label);
    }
    
    private void computeMidiPerc() {
        this.midiLabel = ConvertToMidi.instrToPerc(this.label);
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
            keysigMap = new HashMap<>(); //null means C major
        }
        else {
            keysigMap = new HashMap<>();
            if (keysig.substring(1, 2).equals("s")) {
                String keys[] = {"f", "c", "g", "d", "a", "e", "b"};
                for (int i = 0; i < numberOfAccidentals; i++) {
                    keysigMap.put(keys[i], "s");
                }
            } else if (keysig.substring(1, 2).equals("f")) {
                String keys[] = {"b", "e", "a", "d", "g", "c", "f"};
                for (int i = 0; i < numberOfAccidentals; i++) {
                    keysigMap.put(keys[i], "f");
                }
            }
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