/*
 * Copyright (C) 2024 util2
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
package org.wingate.feuille.m.afm.karaoke.io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.wingate.feuille.m.afm.karaoke.CodeType;
import org.wingate.feuille.m.afm.karaoke.sfx.LineSyllableBasicSFX;
import org.wingate.feuille.m.afm.karaoke.sfx.LineSyllableComplexSFX;
import org.wingate.feuille.m.afm.karaoke.sfx.LineSyllablePeriodSFX;
import org.wingate.feuille.m.afm.karaoke.sfx.LineSyllableRandomSFX;
import org.wingate.feuille.m.afm.karaoke.sfx.LineSyllableSymSFX;
import org.wingate.feuille.m.afm.karaoke.sfx.SFXAbstract;
import org.wingate.feuille.m.afm.karaoke.sfx.SFXCode;

/**
 *
 * @author util2
 */
public class EffectsIO {

    public EffectsIO() {
    }
    
    /*
    On SFXAbstract:
    
    protected String name;
    protected String humanName;
    protected String helper;
    protected List<SFXCode> codes = new ArrayList<>();
    protected List<String> templates = new ArrayList<>();
    
    File:
    
    # Header
    Name: name
    HumanName: humanName
    Helper: helper
    
    # Scripting
    ScriptName: scriptName
    Author: author
    Version: version
    Description: description
    UpdateDetails: update
    CodeType: codeType
    {
    one script
    }
    
    # Templates
    {
    template line
    },
    */
    
    public static void save(String path, SFXAbstract sfx){
        try(PrintWriter pw = new PrintWriter(path, StandardCharsets.UTF_8);){
            pw.println("# Feuille Karaoke SFX Sheet");
            pw.println("# See: https://github.com/TW2/Feuille");
            pw.println("# Discord: https://discord.gg/ef8xvA9wsF");
            pw.println();
            pw.println("# Header");
            pw.println("Name: " + sfx.getName());
            pw.println("HumanName: " + sfx.getHumanName());
            pw.println("Helper: " + sfx.getHelper());
            pw.println();
            if(sfx.getCodes().isEmpty() == false){
                pw.println("# Scripting");
            }
            for(SFXCode code : sfx.getCodes()){                
                pw.println("ScriptName: " + code.getScriptName());
                pw.println("Author: " + code.getAuthor());
                pw.println("Version: " + code.getVersion());
                pw.println("Description: " + code.getDescription());
                pw.println("UpdateDetails: " + code.getUpdateDetails());
                switch(code.getCodeType()){
                    case JavaScript -> { pw.println("CodeType: JavaScript"); }
                    case Lua -> { pw.println("CodeType: Lua"); }
                    case Python -> { pw.println("CodeType: Python"); }
                    case Ruby -> { pw.println("CodeType: Ruby"); }
                }                
                pw.println("[[");
                pw.println(code.getContent());
                pw.println("]]");
                pw.println();
            }
            if(sfx.getTemplates().isEmpty() == false){
                pw.println("# Templates");
            }
            for(String template : sfx.getTemplates()){
                pw.println("[[");
                pw.println(template);
                pw.println("]],");
            }
        } catch (IOException ex) {
            Logger.getLogger(EffectsIO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static SFXAbstract load(String path){
        SFXAbstract sfx = null;
        try(FileReader fr = new FileReader(path);
                BufferedReader br = new BufferedReader(fr);){
            String line, s, c = null;
            SFXCode code = null;
            while((line = br.readLine()) != null){
                if(line.startsWith("Name: ")){
                    s = line.substring("Name: ".length());
                    if(s.isEmpty() == false){
                        switch(s){
                            case "Basic karaoke" -> { sfx = new LineSyllableBasicSFX(); }
                            case "Complex karaoke" -> { sfx = new LineSyllableComplexSFX(); }
                            case "Periodic karaoke" -> { sfx = new LineSyllablePeriodSFX(); }
                            case "Random karaoke" -> { sfx = new LineSyllableRandomSFX(); }
                            case "Symmetric karaoke" -> { sfx = new LineSyllableSymSFX(); }
                        }
                    }
                }else if(line.startsWith("HumanName: ") && sfx != null){
                    s = line.substring("HumanName: ".length());
                    if(s.isEmpty() == false) sfx.setHumanName(s);
                }else if(line.startsWith("Helper: ") && sfx != null){
                    s = line.substring("Helper: ".length());
                    if(s.isEmpty() == false) sfx.setHelper(s);
                }else if(line.startsWith("ScriptName: ") && sfx != null){
                    s = line.substring("ScriptName: ".length());
                    if(s.isEmpty() == false){
                        code = new SFXCode();
                        code.setScriptName(s);
                    }
                }else if(line.startsWith("Author: ") && sfx != null){
                    s = line.substring("Author: ".length());
                    if(s.isEmpty() == false && code != null) code.setAuthor(s);
                }else if(line.startsWith("Version: ") && sfx != null){
                    s = line.substring("Version: ".length());
                    if(s.isEmpty() == false && code != null) code.setVersion(s);
                }else if(line.startsWith("Description: ") && sfx != null){
                    s = line.substring("Description: ".length());
                    if(s.isEmpty() == false && code != null) code.setDescription(s);
                }else if(line.startsWith("UpdateDetails: ") && sfx != null){
                    s = line.substring("UpdateDetails: ".length());
                    if(s.isEmpty() == false && code != null) code.setUpdateDetails(s);
                }else if(line.startsWith("CodeType: ") && sfx != null){
                    s = line.substring("CodeType: ".length());
                    if(s.isEmpty() == false && code != null){
                        switch(s){
                            case "JavaScript" -> { code.setCodeType(CodeType.JavaScript); }
                            case "Lua" -> { code.setCodeType(CodeType.Lua); }
                            case "Python" -> { code.setCodeType(CodeType.Python); }
                            case "Ruby" -> { code.setCodeType(CodeType.Ruby); }
                        }
                    }
                }else if(line.startsWith("[[") && c == null && sfx != null){
                    c = "";
                }else if(line.startsWith("]]") == false && c != null && sfx != null){
                    c += code != null ? line + "\n" : line;
                }else if(line.startsWith("]]") && sfx != null && code != null){
                    code.setContent(c);
                    sfx.getCodes().add(code);
                    c = null;
                    code = null;
                }else if(line.startsWith("]]") && sfx != null && code == null){
                    sfx.getTemplates().add(c);
                    c = null;
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(EffectsIO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EffectsIO.class.getName()).log(Level.SEVERE, null, ex);
        }        
        return sfx;
    }
}
