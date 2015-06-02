/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.jmei2midi;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 *
 * @author dinamix
 */
public class MeiStatTrackerTest {
    
    @Rule public ExpectedException exception = ExpectedException.none();
    
    public MeiStatTrackerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of getFileName method, of class MeiStatTracker.
     */
    @Ignore
    @Test
    public void testGetFileName() {
    }

    /**
     * Test of setFileName method, of class MeiStatTracker.
     */
    @Ignore
    @Test
    public void testSetFileName() {
    }

    /**
     * Test of getIncorrectFiles method, of class MeiStatTracker.
     */
    @Ignore
    @Test
    public void testGetIncorrectFiles() {
    }

    /**
     * Test of getInvalidInstruments method, of class MeiStatTracker.
     */
    @Ignore
    @Test
    public void testGetInvalidInstruments() {
    }

    /**
     * Test of addInvalidInstruments method, of class MeiStatTracker.
     */
    @Test
    public void testAddInvalidInstruments() {
        String expectedfilename = "test.mei";
        MeiStatTracker test = new MeiStatTracker("test.mei");
        test.addInvalidInstrument("sitar");
        test.addInvalidTempo("rubatissimo");
        boolean actualfile = test.getIncorrectFiles().containsKey("test.mei");
        assertTrue(actualfile);
        System.out.println(test);
        //Check for exception so we did not add incorrect file twice
        //exception.expect(IndexOutOfBoundsException.class);
        //String notaddedfile = test.getIncorrectFiles().get(1);
    }

    /**
     * Test of getInvalidTempos method, of class MeiStatTracker.
     */
    @Ignore
    @Test
    public void testGetInvalidTempos() {
    }

    /**
     * Test of addInvalidTempos method, of class MeiStatTracker.
     */
    @Ignore
    @Test
    public void testAddInvalidTempos() {
    }
    
}
