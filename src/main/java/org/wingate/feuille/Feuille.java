package org.wingate.feuille;

import com.formdev.flatlaf.FlatLightLaf;
import java.awt.EventQueue;

/**
 *
 * @author util2
 */
public class Feuille {

    public static void main(String[] args) {
        EventQueue.invokeLater(()->{
            System.out.println("<<< Feuille >>>");
            System.out.println("Feuille is starting...");
            FlatLightLaf.setup();
            MainFrame mf = new MainFrame();
            mf.setTitle("Feuille :: Reforest Earth now :: And stop wars!");
            mf.setSize(1900, 1000);
            mf.setLocationRelativeTo(null);
            mf.setVisible(true);
            mf.setTableSplitter(.5d, .5d, .5d);
            System.out.println("Feuille has started!");
        });
    }
}
