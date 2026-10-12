package feuille.util.theme;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import feuille.util.Loader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CustomTheme {

    private FlatLaf laf;
    private String name;
    private String author;
    private ThemeType themeType;

    public CustomTheme() {
        laf = new FlatLightLaf();
        name = "Unknown name";
        author = "Unknown author";
        themeType = ThemeType.Light;
    }

    public static CustomTheme create(String filename){
        CustomTheme th = new CustomTheme();

        // Base class
        th.themeType = ThemeType.Light;

        // Settings
        Map<String, String> settings = new HashMap<>();

        try(FileReader fr = new FileReader(filename); BufferedReader br = new BufferedReader(fr);){
            String line;
            while((line = br.readLine()) != null){
                if(line.contains("# dark")){
                    th.themeType = ThemeType.Dark;
                }else if(line.contains("# light")){
                    th.themeType = ThemeType.Light;
                }else if(line.contains("# name: ")){
                    th.name = line.substring("# name: ".length());
                }else if(line.contains("# author: ")){
                    th.author = line.substring("# author: ".length());
                }else{
                    if(line.isEmpty()) continue;
                    try{
                        String[] t = line.split(" ", 1);
                        settings.put(t[0], t[1]);
                    }catch(Exception exc){
                        // Do not block if an error occurs, it is maybe an author error!
                    }
                }
            }
        } catch (IOException e) {
            Loader.consoleErr(e.getMessage());
        }

        th.laf = th.themeType == ThemeType.Dark ? new FlatDarkLaf() : new FlatLightLaf();
        th.laf.setExtraDefaults(settings);

        return th;
    }

    public FlatLaf getFlatLaf(){
        return laf;
    }

    public String getName(){
        return name;
    }
    public String getAuthor(){
        return author;
    }

    public ThemeType getThemeType(){
        return themeType;
    }

    @Override
    public String toString() {
        String s = name + " theme";
        if(!author.isEmpty()) s += " (author: " + author + ")";
        return s;
    }
}
