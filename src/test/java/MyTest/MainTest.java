/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyTest;

import ca.mcgill.music.ddmal.mei.MeiAttribute;
import ca.mcgill.music.ddmal.mei.MeiDocument;
import ca.mcgill.music.ddmal.mei.MeiElement;
import ca.mcgill.music.ddmal.mei.MeiXmlReader;
import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author dinamix
 */
public class MainTest {

    private static HashMap<String, String> hash = new HashMap<String, String>();

    public static void main(String[] args) {
        MeiDocument doc = MeiXmlReader.loadFile(
                new File("/Users/dinamix/Documents/mei/mei-test-set/MEI-files/clefs/treble-clef-out.mei"));

        MeiElement music = doc.getRootElement();

        String testNull = music.getAttribute("key.sig");
        System.out.println(testNull);
        
        if (music.getAttribute("meiversion") != null) {
            System.out.println(music.getAttribute("meiversion"));
        }
        //processChildren(music);
    }

    public static void processChildren(MeiElement parent) {
        for (MeiElement child : parent.getChildren()) {
            System.out.println("Print child" + child);
        }
    }

    public static void recursiveDFS(MeiElement parent) {
        processElement(parent);
        for (MeiElement child : parent.getChildren()) {
            recursiveDFS(child);
        }
    }

    public static void processElement(MeiElement parent) {
        System.out.println("Printing parent " + parent);
    }
}
