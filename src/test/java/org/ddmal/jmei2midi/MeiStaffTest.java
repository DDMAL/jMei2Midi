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
        MeiStaff CMajor = new MeiStaff(0, "", "", "0", "","","");
        HashMap<String,String> expectCMajor = CMajor.getKeysigMap();
        assertNull(expectCMajor.get("f"));
        
        MeiStaff GMajor = new MeiStaff(0, "", "", "1s", "","","");
        HashMap<String,String> expectGMajor = GMajor.getKeysigMap();
        HashMap<String,String> actualGMajor = new HashMap<String,String>();
        actualGMajor.put("f", "s");
        assertEquals(actualGMajor,expectGMajor);
        
        MeiStaff ASMinor = new MeiStaff(0, "", "", "7s", "","","");
        HashMap<String,String> expectASMinor = ASMinor.getKeysigMap();
        HashMap<String,String> actualASMinor = new HashMap<String,String>();
        actualASMinor.put("f", "s");
        actualASMinor.put("c", "s");
        actualASMinor.put("g", "s");
        actualASMinor.put("d", "s");
        actualASMinor.put("a", "s");
        actualASMinor.put("e", "s");
        actualASMinor.put("b", "s");
        assertEquals(actualASMinor, expectASMinor);
        assertTrue(expectASMinor.containsKey("f"));
        assertTrue(expectASMinor.containsKey("c"));
        assertTrue(expectASMinor.containsKey("g"));
        assertTrue(expectASMinor.containsKey("d"));
        assertTrue(expectASMinor.containsKey("a"));
        assertTrue(expectASMinor.containsKey("e"));
        assertTrue(expectASMinor.containsKey("b"));
        assertNull(expectASMinor.get("q"));
        
        MeiStaff AFMinor = new MeiStaff(0, "", "", "7f", "","","");
        HashMap<String,String> expectAFMinor = AFMinor.getKeysigMap();
        HashMap<String,String> actualAFMinor = new HashMap<String,String>();
        actualAFMinor.put("b", "f");
        actualAFMinor.put("e", "f");
        actualAFMinor.put("a", "f");
        actualAFMinor.put("d", "f");
        actualAFMinor.put("g", "f");
        actualAFMinor.put("c", "f");
        actualAFMinor.put("f", "f");
        assertEquals(actualAFMinor,expectAFMinor);
        assertTrue(expectAFMinor.containsKey("b"));
        assertTrue(expectAFMinor.containsKey("e"));
        assertTrue(expectAFMinor.containsKey("a"));
        assertTrue(expectAFMinor.containsKey("d"));
        assertTrue(expectAFMinor.containsKey("g"));
        assertTrue(expectAFMinor.containsKey("c"));
        assertTrue(expectAFMinor.containsKey("f"));
        assertNull(expectAFMinor.get("x"));
    }
    
}
