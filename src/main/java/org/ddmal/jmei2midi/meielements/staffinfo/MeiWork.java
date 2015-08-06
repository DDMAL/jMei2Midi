/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.jmei2midi.meielements.staffinfo;

import java.util.HashMap;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * MeiWork will keep track of all the required data from
 * an mei work element. This will then be used to populate
 * the MeiStaff object if information there is missing.
 * These are essentially default values from the mei metadata
 * in case the music data is not provided.
 * @author Tristano Tenaglia
 */
public class MeiWork {
    /**
     * An n attribute that uniquely identifies a work
     */
    private int n;
    
    /**
     * The name of the key for this work 
     */
    private String keyName;
    
    /**
     * The name of the mode for this work 
     */
    private String keyMode;
    /**
     * The key signature used by this work
     */
    private String keysig;
    
    /**
     * The meter count for this work
     */
    private String meterCount;
    /**
     * The meter unit for this work
     */
    private String meterUnit;
    /**
     * Temp for this work as a string
     */
    private String tempo;
    /**
     * Name of instruments given by instrvoice elements
     * within the work element as a child of work
     */
    private HashMap<Integer,String> instrVoice;
    
    /**
     * Default constructor in case no information is given.
     * @param n n attribute to uniquely identify this work
     */
    public MeiWork(int n) {
        this.n = n;
        this.keyName = "c";
        this.keyMode = "major";
        this.keysig = "0";
        this.meterCount = "4";
        this.meterUnit = "4";
        this.tempo = "default"; //default for now
        instrVoice = new HashMap<>();
    }
    
    /**
     * MeiWork constructor based on any required data.
     * @param n n attribute to uniquely identify work
     * @param keyName name of the key that this work uses
     * @param keyMode the key mode that this work uses
     * @param meterCount the meter count of this work
     * @param meterUnit the meter unit of this work
     * @param tempo the tempo of this work
     */
    public MeiWork(int n,
                   String keyName,
                   String keyMode,
                   String meterCount,
                   String meterUnit,
                   String tempo) {
        this.n = n;
        this.keyName = keyName;
        this.keyMode = keyMode;
        this.meterCount = meterCount;
        this.meterUnit = meterUnit;
        this.tempo = tempo;
        instrVoice = new HashMap<>();
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
    }

    /**
     * @return the keyName
     */
    public String getKeyName() {
        return keyName;
    }

    /**
     * Set the keyName if the given attribute exists
     * @param keyName the keyName to set
     */
    public void setKeyName(String keyName) {
        if(MeiStaffMidiLogic.attributeExists(keyName)) {
            this.keyName = keyName;
        }
    }

    /**
     * @return the keyMode
     */
    public String getKeyMode() {
        return keyMode;
    }

    /**
     * Set the keymode if the given attribute exists
     * @param keyMode the keyMode to set
     */
    public void setKeyMode(String keyMode) {
        if(MeiStaffMidiLogic.attributeExists(keysig)) {
            this.keyMode = keyMode;
        }
    }

    /**
     * @return the keysig
     */
    public String getKeysig() {
        return keysig;
    }

    /**
     * Set the key signature if the given attribute exists
     * @param keysig the keysig to set
     */
    public void setKeysig(String keysig) {
        if(MeiStaffMidiLogic.attributeExists(keysig)) {
            this.keysig = keysig;
        }
    }

    /**
     * @return the meterCount
     */
    public String getMeterCount() {
        return meterCount;
    }

    /**
     * Set the meter count if the given attribute exists
     * @param meterCount the meterCount to set
     */
    public void setMeterCount(String meterCount) {
        if(MeiStaffMidiLogic.attributeExists(meterCount)) {
            this.meterCount = meterCount;
        }
    }

    /**
     * @return the meterUnit
     */
    public String getMeterUnit() {
        return meterUnit;
    }

    /**
     * Set the meter unit if the given attribute exists
     * @param meterUnit the meterUnit to set
     */
    public void setMeterUnit(String meterUnit) {
        if(MeiStaffMidiLogic.attributeExists(meterUnit)) {
            this.meterUnit = meterUnit;
        }
    }

    /**
     * @return the tempo
     */
    public String getTempo() {
        return tempo;
    }

    /**
     * @param tempo the tempo to set
     */
    public void setTempo(String tempo) {
        this.tempo = tempo;
    }

    /**
     * @return the instrVoice
     */
    public HashMap<Integer,String> getInstrVoice() {
        return instrVoice;
    }
    
    /**
     * Add an instrument to the instrVoice list.
     * @param n unique n attribute of instrument to be added
     * @param instrument name of instrument given in mei
     */
    public void addInstrVoice(int n, String instrument) {
        instrVoice.put(n, instrument);
    }
    
    /**
     * Compare this element to another object.
     * @return true if keyname, keymode, tempo,
     * 				metercount, meterunit and instrvoice
     * 				are all equal or else return false
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
        MeiWork rhs = (MeiWork) obj;
        //Note: Does not depend on keysig because keysig specific to staff
        return new EqualsBuilder()
                .append(keyName, rhs.keyName)
                .append(keyMode, rhs.keyMode)
                .append(tempo, rhs.tempo)
                .append(meterCount, rhs.meterCount)
                .append(meterUnit, rhs.meterUnit)
                .append(instrVoice, rhs.instrVoice)
                .isEquals();
    }

    /**
     * Overridden hashcode.
     * @return hashcode base on unique n attribute
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(n)
                .toHashCode();
    }
}
