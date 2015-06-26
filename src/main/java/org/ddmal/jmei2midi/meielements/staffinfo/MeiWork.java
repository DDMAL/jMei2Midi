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
 *
 * @author dinamix
 */
public class MeiWork {
    private int n;
    private String keyName;
    private String keyMode;
    //Could remove this altogether actually
    private String keysig; //Only used with scoredef, not work
                           //keysig is specific to MeiStaff, not MeiWork
    private String meterCount;
    private String meterUnit;
    private String tempo;
    private HashMap<Integer,String> instrVoice;
    
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
    
    public void addInstrVoice(int n, String instrument) {
        instrVoice.put(n, instrument);
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
     * @return 
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(n)
                .toHashCode();
    }
}
