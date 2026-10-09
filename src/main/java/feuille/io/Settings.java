package feuille.io;

import feuille.util.Loader;

import java.io.*;

public class Settings {

    private String userSettingsPath;

    private Settings() {
        userSettingsPath = null;
    }

    public static void saveSettings(Settings settings) {
        File app = new File(new File("").getAbsolutePath());
        File rawSettings = new File(app, ".settings.txt");
        try(PrintWriter out = new PrintWriter(rawSettings)) {
            out.println("# User path for settings file");
            out.println("USER_SET_PATH="+settings.userSettingsPath);
        }catch(FileNotFoundException e){
            Loader.consoleErr(e.getMessage());
        }
    }

    public static Settings loadSettings() {
        Settings settings = new Settings();
        File app = new File(new File("").getAbsolutePath());
        File rawSettings = new File(app, ".settings.txt");
        try(BufferedReader in = new BufferedReader(new FileReader(rawSettings))){
            String line;
            while((line = in.readLine()) != null) {
                if(line.startsWith("#")) continue;
                if(line.startsWith("USER_SET_PATH=")) {
                    settings.userSettingsPath = line.split("=")[1];
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
}
