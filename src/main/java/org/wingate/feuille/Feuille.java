package org.wingate.feuille;

import com.formdev.flatlaf.FlatLightLaf;
import java.awt.EventQueue;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.wingate.feuille.theme.Theme;

/**
 *
 * @author util2
 */
public class Feuille {
    
    public static final String SOFTWARE = "Feuille";
    public static final String VERSION = "1.1";
    public static final String SLOGAN = "Reforest earth now!";

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            System.out.println("Feuille is loading...");
            FlatLightLaf.setup();
            
            // TODO bypass
            ThemeDialog th = new ThemeDialog(new javax.swing.JFrame(), true);
            th.showDialog();
            
            Theme theme = th.getSelectedTheme();
            
            MainFrame mf = new MainFrame(theme);
            mf.setTitle(String.format("%s v%s (%s)",
                    SOFTWARE, VERSION, SLOGAN));
            mf.setSize(1900, 900);
            
            mf.setLocationRelativeTo(null);
            mf.setVisible(true);
            
            try{
                UIManager.setLookAndFeel(theme.getTheme());
                SwingUtilities.updateComponentTreeUI(mf);
            }catch(UnsupportedLookAndFeelException exc){
                System.err.println("Look and feel error!");
            }
            System.out.println("Feuille is running!");
        });
    }
}
