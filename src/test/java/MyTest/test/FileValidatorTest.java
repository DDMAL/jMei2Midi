/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyTest.test;

import MyTest.FileValidator;
import java.io.File;
import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 *
 * @author dinamix
 */
public class FileValidatorTest {
    
    @Rule public ExpectedException exception = ExpectedException.none();
    
    @Test
    public void testGetMEISequence() 
            throws Exception {
        File invalid = new File("mei-test/Invalid_Altenburg.mei");
        exception.expect(Exception.class);
        FileValidator.getValidSequence(invalid);
    }
    
    @Test
    public void testGetMIDISequence()
            throws Exception {
        File dne = new File("dne.midi");
        exception.expect(Exception.class);
        FileValidator.getValidSequence(dne);
    }
}
