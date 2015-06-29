/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.jmei2midi;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Tristano
 */
public class MainTest {
    
    public MainTest() {
    }

    /**
     * Test of main method, of class Main.
     */
    @Test
    public void testMain() throws Exception {
        System.out.println("main");
        String[] args = null;
        Main.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of readWriteFile method, of class Main.
     */
    @Test
    public void testReadWriteFile() throws Exception {
        System.out.println("readWriteFile");
        String fileNameIn = "";
        Main.readWriteFile(fileNameIn);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of readDirectory method, of class Main.
     */
    @Test
    public void testReadDirectory() throws Exception {
        System.out.println("readDirectory");
        String dirNameIn = "";
        String dirNameOut = "";
        Main.readDirectory(dirNameIn, dirNameOut);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
