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
 * Mei Measure data which currently keeps track of tupletSpans,
 * ties and accidental information.
 * Ultimately, tupletSpans and ties should be removed and placed
 * in their own classes.
 * @author Tristano Tenaglia
 */
public class MeiMeasure extends MeiGeneral {

    /**
     * This measures number from the n attribute in MEI
     */
    private String measureNumber;

    /**
     * Will be used to keep track of tupletSpans
     */
    private HashMap<String,MeiElement> tupletSpansStart;
    private HashMap<String,MeiElement> tupletSpansEnd;
    private int num; //used for intermediate tuplet notes
    private int numbase;
    
    /**
     * Will be used to keep track of tie elements ( not attributes )
     */
    private HashMap<String,MeiElement> tieStart;
    private HashMap<String,MeiElement> tieEnd;
    
    /**
     * Keeps track of accidentals in a measure.
     */
    private HashMap<String,String> accidentals;
    private String oct;
    
    /**
     * Constructor given an MeiElement measure.
     * @param measure MEI measure element to be passed.
     */
    public MeiMeasure(MeiElement measure) {
        measureNumber = measure.getAttribute("n");
        tupletSpansStart = new HashMap<>();
        tupletSpansEnd = new HashMap<>();
        tieStart = new HashMap<>();
        tieEnd = new HashMap<>();
        accidentals = new HashMap<>();
        num = 1;
        numbase = 1;
        oct = ""; //initialize to avoid null pointer exception
        getEndElements(measure);
    }

    public String getMeasureNumber() {
        return measureNumber;
    }
    
    /**
     * Get the start of id of the given element name (name
     * specified in getHashMap()).
     * @param elementName Element name as given in case statement from getHashMap().
     * @param id ID that we want to get.
     * @return the mei element at the given id if it exists.
     */
    public MeiElement getStart(String elementName, String id) {
        HashMap<String,MeiElement> temp = getHashMap("start",elementName);
        return temp.get(id.replaceAll("#", ""));
    }
     
    /**
     * Add a start id with start element to the specified element name (name
     * specified in getHashMap()).
     * @param elementName Name of the element being added.
     * @param element Object element being added.
     */
    public void setStart(String elementName, MeiElement element) {
        HashMap<String,MeiElement> temp = getHashMap("start", elementName);
        temp.put(element.getAttribute("startid"), element);
    }
    
    /**
     * Set the start of id of the given element name (name
     * specified in getHashMap()).
     * @param elementName Element name as given in case statement from getHashMap().
     * @param id ID that we want to set.
     * @return the mei element at the given id if it exists
     */
    public MeiElement getEnd(String elementName, String id) {
        HashMap<String,MeiElement> temp = getHashMap("end",elementName);
        return temp.get(id.replaceAll("#", ""));
    }
     
    /**
     * Add an end id with end element to the specified element name (name
     * specified in getHashMap()).
     * @param elementName Name of the element wanted.
     * @param element Object mei element being set.
     */
    public void setEnd(String elementName, MeiElement element) {
        HashMap<String,MeiElement> temp = getHashMap("end", elementName);
        temp.put(element.getAttribute("endid"), element);
    }
    
    /**
     * Check to see if current measure has a specified type of element as given
     * in the switch statement in getHashMap().
     * @param mapName Name of element to be checked.
     * @return true if element does currently exist.
     */
    public boolean has(String mapName) {
        return getHashMap("start",mapName).size() > 0;
    }
    
    /**
     * Get HashMap of specified place and element name as given in switch statement.
     * @param place Either start or end ids of the element given by elementName.
     * @param elementName Either tie or tupletSpan currently.
     * @return HashMap specified hashMap if it exists or else returns and empty HashMap.
     */
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
    
    /**
     * Get num attribute of current tupletSpan.
     * @return num Attribute of current tupletSpan.
     */
    public int getNum() {
        return num;
    }
    
    /**
     * Set num attribute of current tupletSpan.
     * @param num Set num of current tupletSpan.
     */
    public void setNum(int num) {
        this.num = num;
    }
    
    /**
     * Get numbase attribute of current tupletSpan.
     * @return numbase Attribute of current tupletSpan.
     */
    public int getNumBase() {
        return numbase;
    }
    
    /**
     * Set numBase attribute of current tupletSpan.
     * @param numBase Set num of current tupletSpan.
     */
    public void setNumBase(int numBase) {
        this.numbase = numBase;
    }
    
    /**
     * Sets the appropriate accidental with accidental name in the accidentals
     * hashmap.
     * @param accidName Name of name that has accidental.
     * @param accid Type of accidental, similar to MEI accid attribute.
     */
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
    
    /**
     * Get accidental of given note name.
     * @param accidName Note name of accidental being checked.
     * @return Type of accidental for given note name.
     */
    public String getAccidental(String accidName) {
        return accidentals.get(accidName);
    }
    
    /**
     * Check if the specified note has an accidental.
     * @param accidName Note name of accidental being checked.
     * @return Whether or not specified note name has accidental.
     */
    public boolean hasAccidental(String accidName) {
        return accidentals.containsKey(accidName);
    }
    
    /**
     * Get the current octave that this measures accidentals are on.
     * @return this measures current octave
     */
    public String getOct() {
        return oct;
    }
    
    /**
     * Set the current octave that this measures accidentals are on.
     * @param oct the octave that this measure's accidentals are on
     */
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
