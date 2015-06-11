/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.jmei2midi;

import ca.mcgill.music.ddmal.mei.MeiElement;
import java.util.HashMap;

/**
 *
 * @author dinamix
 */
public class MeiMeasure {
    //Will be used to keep track of tupletSpans
    private HashMap<String,MeiElement> tupletSpansStart;
    private HashMap<String,MeiElement> tupletSpansEnd;
    private int num; //used for intermediate tuplet notes
    private int numbase;
    
    //Keeps track of accidentals in a measure
    private HashMap<String,String> accidentals;
    private String oct;
    
    public MeiMeasure() {
        tupletSpansStart = new HashMap<>();
        tupletSpansEnd = new HashMap<>();
        accidentals = new HashMap<>();
        num = 1;
        numbase = 1;
    }
    
    public MeiElement getTupletSpanStart(String id) {
        return tupletSpansStart.get(id);
    }
     
    public void setTupletSpanStart(MeiElement element) {
        tupletSpansStart.put(element.getAttribute("startid"), element);
    }
    
    public boolean hasTupletSpans() {
        return tupletSpansStart.size() > 0;
    }
    
    public MeiElement getTupletSpanEnd(String id) {
        return tupletSpansEnd.get(id);
    }
     
    public void setTupletSpanEnd(MeiElement element) {
        tupletSpansEnd.put(element.getAttribute("endid"), element);
    }
    
    public int getNum() {
        return num;
    }
    
    public void setNum(int num) {
        this.num = num;
    }
    
    public int getNumBase() {
        return numbase;
    }
    
    public void setNumBase(int numBase) {
        this.numbase = numBase;
    }
    
    public void setAccidental(String accidName, String accid) {
        if(accid.equals("n") &&
           accidentals.containsKey(accidName)) {
            accidentals.remove(accidName);
        }
        else {
            accidentals.put(accidName, accid);
        }
    }
    
    public String getAccidental(String accidName) {
        return accidentals.get(accidName);
    }
    
    public boolean hasAccidental(String accidName) {
        return accidentals.containsKey(accidName);
    }
    
    public String getOct() {
        return oct;
    }
    
    public void setOct(String oct) {
        this.oct = oct;
    }
}
