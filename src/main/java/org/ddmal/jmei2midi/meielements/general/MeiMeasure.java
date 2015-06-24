/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.jmei2midi.meielements.general;

import ca.mcgill.music.ddmal.mei.MeiElement;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author dinamix
 */
public class MeiMeasure extends MeiGeneral {
    //Will be used to keep track of tupletSpans
    private HashMap<String,MeiElement> tupletSpansStart;
    private HashMap<String,MeiElement> tupletSpansEnd;
    private int num; //used for intermediate tuplet notes
    private int numbase;
    
    //Will be used to keep track of tie elements ( not attributes )
    private HashMap<String,MeiElement> tieStart;
    private HashMap<String,MeiElement> tieEnd;
    
    //Keeps track of accidentals in a measure
    private HashMap<String,String> accidentals;
    private String oct;
    
    public MeiMeasure(MeiElement measure) {
        tupletSpansStart = new HashMap<>();
        tupletSpansEnd = new HashMap<>();
        tieStart = new HashMap<>();
        tieEnd = new HashMap<>();
        accidentals = new HashMap<>();
        num = 1;
        numbase = 1;
        getEndElements(measure);
    }
    
    public MeiElement getStart(String elementName, String id) {
        HashMap<String,MeiElement> temp = getHashMap("start",elementName);
        return temp.get(id.replaceAll("#", ""));
    }
     
    public void setStart(String elementName, MeiElement element) {
        HashMap<String,MeiElement> temp = getHashMap("start", elementName);
        temp.put(element.getAttribute("startid"), element);
    }
    
    public MeiElement getEnd(String elementName, String id) {
        HashMap<String,MeiElement> temp = getHashMap("end",elementName);
        return temp.get(id.replaceAll("#", ""));
    }
     
    public void setEnd(String elementName, MeiElement element) {
        HashMap<String,MeiElement> temp = getHashMap("end", elementName);
        temp.put(element.getAttribute("endid"), element);
    }
    
    public boolean has(String mapName) {
        return getHashMap("start",mapName).size() > 0;
    }
    
    public HashMap<String,MeiElement> getHashMap(String place, String elementName) {
        HashMap<String,MeiElement> temp = new HashMap<>();
        if(elementName.equals("tupletSpan") &&
           place.equals("start")) {
            temp = tupletSpansStart;
        }
        else if(elementName.equals("tupletSpan") &&
                place.equals("end")) {
            temp = tupletSpansEnd;
        }
        else if(elementName.equals("tie") &&
                place.equals("start")) {
            temp = tieStart;   
        }
        else if(elementName.equals("tie") &&
                place.equals("end")) {
            temp = tieEnd;   
        }
        return temp;
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
            accidentals.put(accidName, accid);
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
    
    /**
     * WARNING
     * SHOULD CREATE SEPERATE TUPLETSPAN CLASS AND TIE CLASS.
     * 
     * This is specifically for elements at the end of a measure that do
     * not have any children but that correspond to a specified
     * start and end id.
     * Examples are tupletSpan and tie.
     * @param measure 
     */
    private void getEndElements(MeiElement measure) {
        //Store all the tuplet spans with specified start and end ids
        List<MeiElement> tupletSpans = measure.getDescendantsByName("tupletSpan");
        if(tupletSpans.size() > 0) {
            for(MeiElement tupletSpan : tupletSpans) {
                this.setStart("tupletSpan",tupletSpan);
                this.setEnd("tupletSpan",tupletSpan);
            }
        }
        
        //Store all tie elemnts with specified start and end ids
        List<MeiElement> ties = measure.getDescendantsByName("tie");
        if(ties.size() > 0) {
            for(MeiElement tie : ties) {
                this.setStart("tie",tie);
                this.setEnd("tie",tie);
            }
        }
    }
}
