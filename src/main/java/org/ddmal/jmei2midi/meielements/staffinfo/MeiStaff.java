/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.jmei2midi.meielements.staffinfo;

import ca.mcgill.music.ddmal.mei.MeiElement;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.ddmal.midiUtilities.ConvertToMidi;

/**
 * MeiStaff will keep track of each staff element in an MEI document.
 * Specifically will hold attributes n (channel), label (instrument),
 * key.sig (keysig), meter.count (meter)...
 * @author Tristano Tenaglia
 */
public class MeiStaff {
	
    /**
     * n attribute in mei document 
     */
    private int n;
    /**
     * converted n attribute to fit in MIDI channels/tracks
     */
    private int channel;
    
    
    /**
     * given string tempo from mei label in scoredef/staffdef
     * or from mei work 
     */
    private String tempo;
    /**
     * converted tempo above to beats per minute
     */
    private int bpm;
    
    /**
     * current tick count of this staff
     */
    private long tick;
    /**
     * included to account for multiple layers
     * starting at the same time,
     * only need to use with 2 or more layers
     */
    private long layerOffset;   
    
    /**
     * major or minor
     * only do once at the beginning
     * dont keep updating throughout
     */
    private String keymode;
    /**
     * mei key signature from keysig attribute 
     * like "2s" or "4f" or "0
     */
    private String keysig;
    /**
     * Associates key : note, value : accidental
     * (i.e. key : f, value : s = g major) 
     */
    private Map<String,String> keysigMap;
    
    /**
     * usually used for instrument name in mei
     */
    private String label;
    /**
     * label converted to midi appropriate name if valid
     */
    private int midiLabel;
    
    
    /**
     * the count of a meter,
     * need for things like mRest
     */
    private String meterCount;
    /**
     * the unit of a meter
     */
    private String meterUnit;
    
    
    /**
     * holds all layer children names
     * as keys with the element itself
     * as values.
     * this could be removed if object hierarchy is perfected
     */
    private HashMap<String,MeiElement> layerChild;
    
    /**
     * Default MeiStaff Constructor.
     * Should not be used because all values assumed.
     * Only created to account for someone forgetting to input
     * some attributes in scoreDef or staffDef such as in
     * Debussy Mandoline where the keymode and keysig are "unclear".
     * Label and tempo will be adjusted accordingly.
     * @param n the n value taken by mei or given to this staff
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
     * @param n the n value taken by mei or given to this staff
     * @param tempo the tempo given to this staff either by mei or default
     * @param label usually the instrument name taken from label or mei work
     * @param keysig the key signature given to this staff
     * @param keymode the key mode given to this staff
     * @param meterCount the meter count for this staff
     * @param meterUnit the meter unit for this staff
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
     * If n <= 9, channel - n-1.
     * If 9 < n < 16 then channel = n.
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
    
    /**
     * Set the label and compute the appropriate midi label.
     * @param label the label to be set
     */
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
     * @param midiPerc the perc midi number to be set
     */
    public void setLabelPerc(String label, int midiPerc) {
        this.label = label;
        this.midiLabel = midiPerc;
        this.channel = 9;
    }
    
    /**
     * Optimization to set a instrument once you know that it
     * is a instrument.
     * @param label the label to be set
     * @param midiLabel the midi instrument number to be set
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
     * Keymode of staff, set to lowercase for consistency
     * @param keymode the keymode to set
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
     * Set the key signature given an mei keysig attribute
     * @param keysig key signature to be set
     */
    public void setKeysig(String keysig) {
        this.keysig = keysig;
        this.computeKeysigMap();
    }

    /**
     * @return the keysigMap
     */
    public Map<String,String> getKeysigMap() {
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
     * Set the temp and will change bpm accordingly
     * @param tempo the tempo to set
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
     * @return the layer child map
     */
    public HashMap<String,MeiElement> getLayerChildMap() {
        return layerChild;
    }
    
    /**
     * Get a specific layerchild element if it is
     * in the layerchild map
     * @param element layerchild element to be retrieved
     * @return specified element in the layerchild map
     */
    public MeiElement getLayerChild(String element) {
        return layerChild.get(element);
    }
    
    
    /**
     * Add a layerchild mei element to the layerchild map
     * @param element layerchild mei element to be added
     */
    public void setLayerChild(MeiElement element) {
        layerChild.put(element.getName(), element);
    }
    
    /**
     * Remove a layerchild mei element from the layerchild map
     * @param elementName name of mei layerchild element to be removed
     */
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
    
    /**
     * This will convert a String to an appropriate midi
     * percussion instrument equivalent.
     * May return -1 which would cause a Midi exception
     */
    private void computeMidiPerc() {
        this.midiLabel = ConvertToMidi.instrToPerc(this.label);
    }
    
    /**
     * Assigns KeysigMap a HashMap for the key signature and this should be checked with
     * each note for any accidental in the given key.
     * HashMap holds key,value pair as the specified accidental in the key
     * signature also dependent on the current key.
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
     * @return true if n, channel, tempo, bpm, tick,
     * 		   layeroffset, keymode, keysig, label, metercount,
     * 		   meterunit and keysig map are equal and
     * 		   true if same object or else return false.
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
     * Overriden hashcode.
     * @return hashcode built by unique n value
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(n)
                .toHashCode();
    }
}
