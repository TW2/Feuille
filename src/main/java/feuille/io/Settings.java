package feuille.io;

import com.formdev.flatlaf.FlatLaf;
import feuille.FeuilleFrame;
import feuille.util.Loader;
import feuille.theme.Theme;
import feuille.theme.ThemeCollection;

import javax.swing.*;
import java.io.*;

public class Settings {

    private String userSettingsPath;
    private String theme;
    private String language;
    private String automationMakeItPath;
    private String codeMethodsPath;

    private Settings() {
        userSettingsPath = null;
        theme = null;
        language = null;
        automationMakeItPath = null;
        codeMethodsPath = null;
    }

    public static void saveSettings(Settings settings) {
        File app = new File(new File("").getAbsolutePath());
        File rawSettings = new File(app, ".settings");
        try(PrintWriter out = new PrintWriter(rawSettings)) {
            saveParam(out, "User path for settings file",
                    "USER_SET_PATH", settings.userSettingsPath);
            saveParam(out, "Theme name",
                    "THEME", settings.theme);
            saveParam(out, "Language",
                    "LANGUAGE", settings.language);
            saveParam(out, "MakeIt path",
                    "MAKE_IT", settings.automationMakeItPath);
            saveParam(out, "CodeMethods path",
                    "CODE_METHODS", settings.codeMethodsPath);
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
                settings.userSettingsPath = loadParam(line, "USER_SET_PATH");
                settings.theme = loadParam(line, "THEME");
                settings.language = loadParam(line, "LANGUAGE");
                settings.automationMakeItPath = loadParam(line, "MAKE_IT");
                settings.codeMethodsPath = loadParam(line, "CODE_METHODS");

                // Do it later
                if(settings.theme != null) {
                    Theme<? extends FlatLaf> theme = null;
                    for(Theme<? extends FlatLaf> th : new ThemeCollection()) {
                        if(th.getName().equalsIgnoreCase(settings.theme)) {
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
            }
        }catch(IOException e){
            Loader.consoleErr(e.getMessage());
        }
        return settings;
    }

    private static void saveParam(PrintWriter out, String comment, String startLine, String param){
        if(param == null) return;
        out.println("# " + comment);
        out.println(startLine.replace(" ", "_").toUpperCase() + "=" + param);
    }

    private static String loadParam(String line, String startLine){
        if(line.startsWith(startLine + "=")){
            return line.split("=")[1];
        }
        return null;
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

    public String getAutomationMakeItPath() {
        return automationMakeItPath;
    }

    public void setAutomationMakeItPath(String automationMakeItPath) {
        this.automationMakeItPath = automationMakeItPath;
    }

    public String getCodeMethodsPath() {
        return codeMethodsPath;
    }

    public void setCodeMethodsPath(String codeMethodsPath) {
        this.codeMethodsPath = codeMethodsPath;
    }
}
