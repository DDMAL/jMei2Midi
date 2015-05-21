/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.jmei2midi;

import java.util.HashMap;
import org.ddmal.jmei2midi.MeiStaff;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author dinamix
 */
public class MeiStaffTest {
    
    public MeiStaffTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of computeBpm method, of class MeiStaff.
     */
    @Ignore
    @Test
    public void testComputeBpm() {
        
    }

    /**
     * Test of computeKey method, of class MeiStaff.
     */
    @Test
    public void testComputeKey() {
        MeiStaff CMajor = new MeiStaff(0, "", "", "0", "");
        HashMap<String,String> expectCMajor = CMajor.getKeysigMap();
        assertNull(expectCMajor);
        
        MeiStaff GMajor = new MeiStaff(0, "", "", "1s", "");
        HashMap<String,String> expectGMajor = GMajor.getKeysigMap();
        HashMap<String,String> actualGMajor = new HashMap<String,String>();
        actualGMajor.put("f", "f");
        assertEquals(actualGMajor,expectGMajor);
        
        MeiStaff ASMinor = new MeiStaff(0, "", "", "7s", "");
        HashMap<String,String> expectASMinor = ASMinor.getKeysigMap();
        HashMap<String,String> actualASMinor = new HashMap<String,String>();
        actualASMinor.put("f", "f");
        actualASMinor.put("c", "c");
        actualASMinor.put("g", "g");
        actualASMinor.put("d", "d");
        actualASMinor.put("a", "a");
        actualASMinor.put("e", "e");
        actualASMinor.put("b", "b");
        assertEquals(actualASMinor, expectASMinor);
        assertTrue(expectASMinor.containsValue("f"));
        assertTrue(expectASMinor.containsValue("c"));
        assertTrue(expectASMinor.containsValue("g"));
        assertTrue(expectASMinor.containsValue("d"));
        assertTrue(expectASMinor.containsValue("a"));
        assertTrue(expectASMinor.containsValue("e"));
        assertTrue(expectASMinor.containsValue("b"));
        
        MeiStaff AFMinor = new MeiStaff(0, "", "", "7f", "");
        HashMap<String,String> expectAFMinor = AFMinor.getKeysigMap();
        HashMap<String,String> actualAFMinor = new HashMap<String,String>();
        actualAFMinor.put("b", "b");
        actualAFMinor.put("e", "e");
        actualAFMinor.put("a", "a");
        actualAFMinor.put("d", "d");
        actualAFMinor.put("g", "g");
        actualAFMinor.put("c", "c");
        actualAFMinor.put("f", "f");
        assertEquals(actualAFMinor,expectAFMinor);
        assertTrue(expectAFMinor.containsValue("b"));
        assertTrue(expectAFMinor.containsValue("e"));
        assertTrue(expectAFMinor.containsValue("a"));
        assertTrue(expectAFMinor.containsValue("d"));
        assertTrue(expectAFMinor.containsValue("g"));
        assertTrue(expectAFMinor.containsValue("c"));
        assertTrue(expectAFMinor.containsValue("f"));
    }
    
}
