/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyTest;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 *
 * @author dinamix
 */
public class Instruments {
    public static void main(String[] args) throws MidiUnavailableException {
        Synthesizer synthesizer = MidiSystem.getSynthesizer();
        synthesizer.open();
        Instrument[] orchestra = synthesizer.getAvailableInstruments();

        final StringBuilder sb = new StringBuilder();
        String eol = System.getProperty("line.separator");
        sb.append("The orchestra has ");
        sb.append(orchestra.length);
        sb.append(" instruments.");
        sb.append(eol);
        for (Instrument instrument : orchestra) {
            sb.append(instrument.toString());
            sb.append(eol);
        }
        synthesizer.close();
        System.out.println(sb);
//        Runnable r = new Runnable() {
//
//            @Override
//            public void run() {
//                JOptionPane.showMessageDialog(null,
//                        new JScrollPane(new JTextArea(sb.toString(), 20, 30)));
//            }
//        };
//        SwingUtilities.invokeLater(r);
    }
}
