package feuille.io;

import com.formdev.flatlaf.FlatLaf;
import feuille.FeuilleFrame;
import feuille.util.Loader;
import feuille.util.theme.Theme;
import feuille.util.theme.ThemeCollection;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Settings {

    private String userSettingsPath;
    private String theme;
    private String language;

    private Settings() {
        userSettingsPath = null;
        theme = null;
        language = null;
    }

    public static void saveSettings(Settings settings) {
        File app = new File(new File("").getAbsolutePath());
        File rawSettings = new File(app, ".settings");
        try(PrintWriter out = new PrintWriter(rawSettings)) {
            out.println("# User path for settings file");
            out.println("USER_SET_PATH="+settings.userSettingsPath);
            out.println(("# Theme name"));
            out.println("THEME="+settings.theme);
            out.println("# Language");
            out.println("LANGUAGE="+settings.language);
        }catch(FileNotFoundException e){
            Loader.consoleErr(e.getMessage());
        }
    }

    public static Settings loadSettings(FeuilleFrame parent) {
        Settings settings = new Settings();
        File app = new File(new File("").getAbsolutePath());
        File rawSettings = new File(app, ".settings");
        try(BufferedReader in = new BufferedReader(new FileReader(rawSettings))){
            String line;
            while((line = in.readLine()) != null) {
                if(line.startsWith("#")) continue;
                if(line.startsWith("USER_SET_PATH=")) {
                    settings.userSettingsPath = line.split("=")[1];
                }else if(line.startsWith("THEME=")) {
                    String choice = line.split("=")[1];
                    if(choice != null && !choice.equalsIgnoreCase("null")) {
                        settings.theme = choice;
                        Theme<? extends FlatLaf> theme = null;
                        for(Theme<? extends FlatLaf> th : new ThemeCollection()) {
                            if(th.getName().equalsIgnoreCase(choice)) {
                                theme = th;
                            }
                        }
                        if(theme == null) continue;
                        try{
                            UIManager.setLookAndFeel(theme.getLaf());
                            SwingUtilities.updateComponentTreeUI(parent);
                        }catch(Exception ex){
                            Loader.consoleErr(ex.getMessage());
                        }
                    }
                }else if(line.startsWith("LANGUAGE=")) {
                    String choice = line.split("=")[1];
                    if(choice != null && !choice.equalsIgnoreCase("null")) {
                        settings.language = choice;
                    }
                }
            }
        }catch(IOException e){
            Loader.consoleErr(e.getMessage());
        }
        return settings;
    }

    public String getUserSettingsPath() {
        return userSettingsPath;
    }

    public void setUserSettingsPath(String userSettingsPath) {
        this.userSettingsPath = userSettingsPath;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
