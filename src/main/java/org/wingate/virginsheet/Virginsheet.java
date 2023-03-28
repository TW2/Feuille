package org.wingate.virginsheet;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.EventQueue;
import java.util.HashMap;
import java.util.Map;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author util2
 */
public class Virginsheet {
    
    public static final String SEPARATOR = "::";
    public static final String SOFTWARE = "Feuille";
    public static final String NEW_NAME = "Virgin Sheet";
    public static final String VERSION = "3.0";
    public static final String SLOGAN = "Reforest earth now!";

    public static void main(String[] args) {
        System.out.println("Hello World!");
        
        EventQueue.invokeLater(() -> {
            FlatLightLaf.setup();
            
            // TODO bypass
            ThemeDialog th = new ThemeDialog(new javax.swing.JFrame(), true);
            th.showDialog();
            
            FlatLaf theme = th.getSelectedTheme();
            
            MainFrame mf = new MainFrame();
            mf.setTitle(String.format("%s v%s %s %s | %s",
                    SOFTWARE, VERSION, SEPARATOR, NEW_NAME, SLOGAN));
            mf.setSize(1900, 900);
            
            mf.setLocationRelativeTo(null);
            mf.setVisible(true);
            
            try{
                UIManager.setLookAndFeel(theme);
                SwingUtilities.updateComponentTreeUI(mf);
            }catch(UnsupportedLookAndFeelException exc){
                System.err.println("Look and feel error!");
            }
        });
    }
    
    public static FlatLaf useBloodyGolden(){
        // La classe de base / Base class
        FlatLaf laf = new FlatDarkLaf();
        
        // Les modifications / Settings
        Map<String, String> settings = new HashMap<>();
        settings.put("@accentColor", "#FF3200");
        settings.put("@foreground", "#FFCC00");
        laf.setExtraDefaults(settings);
        
        return laf;
    }
    
    public static FlatLaf usePatriotLight(){
        // La classe de base / Base class
        FlatLaf laf = new FlatLightLaf();
        
        // Les modifications / Settings
        Map<String, String> settings = new HashMap<>();
        settings.put("@accentColor", "#FF3200");
        settings.put("@foreground", "#6073FF");
        laf.setExtraDefaults(settings);
        
        return laf;
    }
    
    public static FlatLaf useSatanDark(){
        // La classe de base / Base class
        FlatLaf laf = new FlatDarkLaf();
        
        // Les modifications / Settings
        Map<String, String> settings = new HashMap<>();
        settings.put("@accentColor", "#F26D00");
        settings.put("@foreground", "#FF4744");
        laf.setExtraDefaults(settings);
        
        return laf;
    }
    
    public static FlatLaf useGirlPowerLight(){
        // La classe de base / Base class
        FlatLaf laf = new FlatLightLaf();
        
        // Les modifications / Settings
        Map<String, String> settings = new HashMap<>();
        settings.put("@accentColor", "#FF84F0");
        settings.put("@background", "#FFCCE5");
        laf.setExtraDefaults(settings);
        
        return laf;
    }
    
    public static FlatLaf useMilitaryDark(){
        // La classe de base / Base class
        FlatLaf laf = new FlatDarkLaf();
        
        // Les modifications / Settings
        Map<String, String> settings = new HashMap<>();
        settings.put("@accentColor", "#CEEF2B");
        settings.put("@background", "#185111");
        laf.setExtraDefaults(settings);
        
        return laf;
    }
    
    public static FlatLaf useArcticLight(){
        // La classe de base / Base class
        FlatLaf laf = new FlatLightLaf();
        
        // Les modifications / Settings
        Map<String, String> settings = new HashMap<>();
        settings.put("@background", "#C4E4FF");
        laf.setExtraDefaults(settings);
        
        return laf;
    }
    
    public static FlatLaf useLemonLight(){
        // La classe de base / Base class
        FlatLaf laf = new FlatLightLaf();
        
        // Les modifications / Settings
        Map<String, String> settings = new HashMap<>();
        settings.put("@accentColor", "#E7FF38");
        settings.put("@background", "#E4FFC9");
        laf.setExtraDefaults(settings);
        
        return laf;
    }
    
    public static FlatLaf useRanchDark(){
        // La classe de base / Base class
        FlatLaf laf = new FlatDarkLaf();
        
        // Les modifications / Settings
        Map<String, String> settings = new HashMap<>();
        settings.put("@accentColor", "#FFA23F");
        settings.put("@background", "#77481B");
        laf.setExtraDefaults(settings);
        
        return laf;
    }
    
    public static FlatLaf useNuclearDark(){
        // La classe de base / Base class
        FlatLaf laf = new FlatDarkLaf();
        
        // Les modifications / Settings
        Map<String, String> settings = new HashMap<>();
        settings.put("@accentColor", "#667E99");
        settings.put("@foreground", "#99A2A8");
        laf.setExtraDefaults(settings);
        
        return laf;
    }
}
