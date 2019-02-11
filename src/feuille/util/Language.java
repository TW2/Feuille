/*
 * Copyright (C) 2019 util2
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package feuille.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author util2
 */
public class Language {
    // Instance of main class to retrieve the main folder (user.dir)
    // because user.dir in GraalVM is user.home
    private String userDir = "";
    
    // Data
    private Map<String, List<Value>> translation = new HashMap<>();
    
    // Configuration - For forced or chosen language
    private ISO_3166 iso = ISO_3166.getISO_3166(Locale.getDefault().getISO3Country());
    private boolean forced = false;

    public Language() {
        init();
    }
    
    private void init(){
        // Fill in language by import every LANG_ properties
        Properties prop = new Properties();
        String language = "Nothing";
        
        // Search for application path
        LoaderPath loaderPath = new LoaderPath();
        if(loaderPath.isEnabled() == true){
            userDir = loaderPath.getFolderPath();
        }
        
        // #1 Register the language
        File config = new File(userDir + File.separator + "configuration" + File.separator + "configuration.properties");
        if(config.exists() == true){
            try(InputStream input = new FileInputStream(config)){
                prop.load(input);
                language = prop.getProperty("Forced_Language");
            } catch (IOException ex) {
                Logger.getLogger(Language.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            try(InputStream input = getClass().getResourceAsStream("/configuration/configuration.properties")){
                prop.load(input);
                language = prop.getProperty("Forced_Language");
            } catch (IOException ex) {
                Logger.getLogger(Language.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        iso = language.equalsIgnoreCase("nothing") ? iso : ISO_3166.getISO_3166(language.replaceAll("\"", ""));
        forced = !language.equalsIgnoreCase("nothing");
        
        // 2# Populate the Language class
        File[] files = null;
        if(config.exists() == true){
            File folder = new File(userDir + File.separator + "configuration");
            if(folder.exists()){
                files = folder.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return name.toLowerCase().startsWith("lang_");
                    }
                });
            }
        }else if(new File(getClass().getResource("/configuration").getPath()).exists() == true){
            try{
                // Get only files that contains a language
                files = (new File(getClass().getResource("/configuration").toURI())).listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return name.toLowerCase().startsWith("lang_");
                    }
                });
            } catch (URISyntaxException ex) {
                Logger.getLogger(Language.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
        if(files != null && files.length > 0){
            // Search for languages if there is any
            for(File file : files){
                try(FileInputStream input = new FileInputStream(file); BufferedReader br = new BufferedReader(new InputStreamReader(input, Charset.forName("UTF-8")))){
                    String line;
                    String iso3 = file.getName().substring(file.getName().indexOf("_") + 1, file.getName().lastIndexOf("."));
                    ISO_3166 langISO = ISO_3166.getISO_3166(iso3);
                    while((line = br.readLine()) != null){
                        if(line.startsWith("#") == false && line.isEmpty() == false){
                            String key = line.substring(0, line.indexOf(" ")).trim();
                            String translated = line.substring(line.indexOf(" ")).trim();
                            addTranslation(key, langISO, translated);
                        }                        
                    }                    
                } catch (IOException ex) {
                    Logger.getLogger(Language.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void setTranslation(Map<String, List<Value>> translation) {
        this.translation = translation;
    }

    public Map<String, List<Value>> getTranslation() {
        return translation;
    }
        
    public void addTranslation(String key, ISO_3166 iso, String translated) {
        translated = getWord(translated);// <- For accentued characters
        
        int index = -1;
        if(translation.containsKey(key) == true){
            List<Value> values = translation.get(key);            
            for (int i=0; i<values.size(); i++){
                Value val = values.get(i);
                if(val.getTargetLanguage().equals(iso) == true){
                    index = i;
                    break;
                }
            }            
        }
        
        if(index != -1){
            translation.get(key).get(index).setText(translated);
        }else{
            if(translation.containsKey(key) == true){
                Value val = new Value(translated, iso);
                translation.get(key).add(val);
            }else{
                Value val = new Value(translated, iso);
                List<Value> values = new ArrayList<>();
                values.add(val);
                translation.put(key, values);
            }            
        }        
    }
    
    public String getTranslated(String key, ISO_3166 iso, String defaultDisplay){
        int index = -1;
        if(translation.containsKey(key) == true){
            List<Value> values = translation.get(key);            
            for (int i=0; i<values.size(); i++){
                Value val = values.get(i);
                if(val.getTargetLanguage().equals(iso) == true){
                    index = i;
                    break;
                }
            }            
        }
        
        return index != -1 ? translation.get(key).get(index).getText() : defaultDisplay;
    }
    
    private String getWord(String str){
        Pattern p = Pattern.compile("\\\\u([0-9a-fA-F]{4})");
        Matcher m = p.matcher(str);
        while(m.find()){
            String hexa = m.group(1);
            str = str.replaceAll("\\\\u" + hexa, String.valueOf((char)Integer.parseInt(hexa, 16)));
        }
        return str;
    }

    public Language getInputLanguage() {        
        return this;
    }

    public ISO_3166 getIso() {
        return iso;
    }

    public boolean isForced() {
        return forced;
    }
    
    public static class Value {
        private String text;
        private ISO_3166 targetLanguage;

        public Value() {
        }

        public Value(String text, ISO_3166 targetLanguage) {
            this.text = text;
            this.targetLanguage = targetLanguage;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public void setTargetLanguage(ISO_3166 targetLanguage) {
            this.targetLanguage = targetLanguage;
        }

        public ISO_3166 getTargetLanguage() {
            return targetLanguage;
        }
    }
    
}
