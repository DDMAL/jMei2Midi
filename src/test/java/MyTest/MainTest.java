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
                new File("/Users/dinamix/Documents/mei/"
                        + "music-encoding/samples/MEI2013/Music/Complete examples/Ives_TheCage.mei"));

        List<MeiElement> list = doc.getElementsByName("music");

        MeiElement music = list.get(0);
        List<MeiElement> staffGrp = music.getDescendantsByName("staffGrp");
        for(MeiElement ele : staffGrp) {
            System.out.println(ele.getId());
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
