/*
 * Copyright (C) 2023 util2
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
package org.wingate.feuille;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author util2
 */
public class Theme {
    
    public enum Type {
        Light, Dark;
    }
    
    private FlatLaf theme = new FlatLightLaf();
    private String name = "Light";
    private String author = "";
    private Type type = Type.Light;

    public Theme() {
        
    }
    
    public Theme(Type type, FlatLaf theme, String name, String author) {
        this.type = type;
        this.theme = theme == null ? new FlatLightLaf() : theme;
        this.name = name == null || name.isEmpty() ? "Unknown name theme" : name;
        this.author = author == null || author.isEmpty() ? "" : author;
    }
    
    public static Theme create(String filename){        
        Theme th = new Theme();
        
        // La classe de base / Base class
        th.theme = new FlatLightLaf();
        th.type = Type.Light;
        
        // Les modifications / Settings
        Map<String, String> settings = new HashMap<>();
        
        try(FileReader fr = new FileReader(filename); BufferedReader br = new BufferedReader(fr);){
            String line;
            while((line = br.readLine()) != null){
                if(line.contains("# dark")){
                    th.theme = new FlatDarkLaf();
                    th.type = Type.Dark;
                }else if(line.contains("# name: ")){
                    th.name = line.substring("# name: ".length());
                }else if(line.contains("# author: ")){
                    th.author = line.substring("# author: ".length());
                }else{
                    if(line.isEmpty() == true) continue;
                    try{
                        String[] t = line.split(" ", 1);
                        settings.put(t[0], t[1]);
                    }catch(Exception exc){
                        // Do not block if an error occurs, it is maybe an author error!
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Theme.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Theme.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        th.theme.setExtraDefaults(settings);
        
        return th;
    }

    public FlatLaf getTheme() {
        return theme;
    }

    public void setTheme(FlatLaf theme) {
        this.theme = theme;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        String s = name + " theme";
        if(author.isEmpty() == false) s += " by " + author;
        return s;
    }
    
}
