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
package org.wingate.feuille.subs.ass;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.wingate.feuille.util.ISO_3166;

/**
 *
 * @author util2
 */
public class ASS {
    
    public enum Section {
        ScriptInfos, Aegisub, Styles, Fonts, Graphics, Events, Actors, FXs
    }
    
    // ASS
    private AssInfos infos = new AssInfos();
    private List<AssStyle> styles = new ArrayList<>();
    private List<AssEvent> events = new ArrayList<>();
    private List<AssAttachment> attachs = new ArrayList<>();
    
    // Feuille SubSystem for ASS
    private List<AssFont> fonts = new ArrayList<>();
    private List<AssActor> actors = new ArrayList<>();
    private List<AssEffect> effects = new ArrayList<>();
    
    // Feuille link languages process (language, path)
    private AssTranslateTo translations = new AssTranslateTo();

    public ASS() {
        styles.add(new AssStyle());
        actors.add(new AssActor());
        effects.add(new AssEffect());
    }

    public AssInfos getInfos() {
        return infos;
    }

    public void setInfos(AssInfos infos) {
        this.infos = infos;
    }

    public List<AssStyle> getStyles() {
        return styles;
    }

    public void setStyles(List<AssStyle> styles) {
        this.styles = styles;
    }

    public List<AssEvent> getEvents() {
        return events;
    }

    public void setEvents(List<AssEvent> events) {
        this.events = events;
    }

    public List<AssAttachment> getAttachs() {
        return attachs;
    }

    public void setAttachs(List<AssAttachment> attachs) {
        this.attachs = attachs;
    }

    public List<AssFont> getFonts() {
        return fonts;
    }

    public void setFonts(List<AssFont> fonts) {
        this.fonts = fonts;
    }

    public List<AssActor> getActors() {
        return actors;
    }

    public void setActors(List<AssActor> actors) {
        this.actors = actors;
    }

    public List<AssEffect> getEffects() {
        return effects;
    }

    public void setEffects(List<AssEffect> effects) {
        this.effects = effects;
    }

    public static ASS read(String path){
        ASS ass = new ASS();
        
        // Sections :
        // ScriptInfos, Aegisub, Styles, Fonts, Graphics, Events, Actors, FXs
        try(FileReader fr = new FileReader(path, StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(fr);){
            
            String line;
            Section sec = Section.ScriptInfos;
            
            while((line = br.readLine()) != null){
                if(line.isEmpty()) continue;
                
                // Define section
                if(line.startsWith("[Script Infos]")){
                    sec = Section.ScriptInfos; continue;
                }else if(line.startsWith("[Aegisus Project Garbage]")){
                    sec = Section.Aegisub; continue;
                }else if(line.startsWith("[V4+ Styles]")){
                    sec = Section.Styles; continue;
                }else if(line.startsWith("[Fonts]")){
                    sec = Section.Fonts; continue;
                }else if(line.startsWith("[Graphics]")){
                    sec = Section.Graphics; continue;
                }else if(line.startsWith("[Actors]")){
                    sec = Section.Actors; continue;
                }else if(line.startsWith("[FXs]")){
                    sec = Section.FXs; continue;
                }else if(line.startsWith("[Events]")){
                    sec = Section.Events; continue;
                }
                
                if(sec == Section.ScriptInfos){
                    AssInfos.read(ass.getInfos(), br);
                }
                
                if(line.startsWith("Style: ") && sec == Section.Styles){
                    AssStyle style = new AssStyle(line);
                    ass.getStyles().add(style);
                    ass.getFonts().add(style.getAssFont());
                }else if(line.startsWith("fontname: ") && sec == Section.Fonts){
                    // UUDECODE doesn't work
                    String name = line.substring("fontname: ".length());
                    File f = AssUU.readUU(br, new File(name).getPath());
                    ass.getAttachs().add(new AssAttachment(f.getPath(), AssAttachment.Type.Font));                    
                }else if(line.startsWith("filename: ") && sec == Section.Graphics){
                    // UUDECODE doesn't work
                    String name = line.substring("filename: ".length());
                    File f = AssUU.readUU(br, new File(name).getPath());
                    ass.getAttachs().add(new AssAttachment(f.getPath(), AssAttachment.Type.Image));                    
                }else if(line.startsWith("Actor: ") && sec == Section.Actors){
                    ass.getActors().add(AssActor.fromLine(line));
                }else if(line.startsWith("FX: ") && sec == Section.FXs){
                    ass.getEffects().add(AssEffect.fromLine(line));
                }else if(line.startsWith("Dialogue: ") && sec == Section.Events){
                    try{
                        AssTranslateTo tr = ass.getInfos().getTranslations();
                        ISO_3166 cur;
                        if(tr == null){
                            cur = ISO_3166.getISO_3166(Locale.getDefault().getISO3Country());
                            tr = AssTranslateTo.createFirst(cur);
                        }else{
                            cur = tr.getVersions().getLast().getIso();
                        }
                        ass.getEvents().add(AssEvent.createFromRawLine(line, ass.getStyles(), ass.getActors(), ass.getEffects()));
                    }catch(Exception exc){
                        // Last line - End of events
                    }
                }else if(line.startsWith("Comment: ") && sec == Section.Events){
                    try{
                        AssTranslateTo tr = ass.getInfos().getTranslations();
                        ISO_3166 cur;
                        if(tr == null){
                            cur = ISO_3166.getISO_3166(Locale.getDefault().getISO3Country());
                            tr = AssTranslateTo.createFirst(cur);
                        }else{
                            cur = tr.getVersions().getLast().getIso();
                        }
                        ass.getEvents().add(AssEvent.createFromRawLine(line, ass.getStyles(), ass.getActors(), ass.getEffects()));
                    }catch(Exception exc){
                        // Last line - End of events
                    }                    
                }
            }
        } catch (IOException | AssColorException ex) {
            Logger.getLogger(ASS.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return ass;
    }
    
    public static void write(ASS ass, ISO_3166 iso, String path){
        try(PrintWriter pw = new PrintWriter(path, StandardCharsets.UTF_8);){
            // Script Info
            pw.println("[Script Info]");
            pw.println("; Script generated by Feuille :: Leaf in English ::");
            pw.println("; https://github.com/TW2/Feuille");
            AssInfos.writeInfos(ass.getInfos(), pw);
            pw.println();
            
            // Styles
            pw.println("[V4+ Styles]");
            pw.println("Format: Name, Fontname, Fontsize, PrimaryColour, SecondaryColour, OutlineColour, BackColour," +
                    " Bold, Italic, Underline, StrikeOut, ScaleX, ScaleY, Spacing, Angle, BorderStyle, Outline, Shadow," +
                    " Alignment, MarginL, MarginR, MarginV, Encoding");
            for(AssStyle style : ass.getStyles()){
                pw.println(style.toRawLine());
            }
            pw.println();
            
            if(!ass.getAttachs().isEmpty()){
                List<AssAttachment> withFonts = new ArrayList<>();
                List<AssAttachment> withImages = new ArrayList<>();
                for(AssAttachment attach : ass.getAttachs()){
                    switch(attach.getType()){
                        case Font -> { withFonts.add(attach); }
                        case Image -> { withImages.add(attach); }
                        case None -> { /* Nothing */ }
                    }
                }
                // Fonts
                if(!withFonts.isEmpty()){
                    pw.println("[Fonts]");
                }                
                for(AssAttachment attach : withFonts){
                    File file = new File(attach.getPath());
                    pw.println("fontname: " + file.getName());
                    AssUU.writeUU(file, pw);
                    pw.println();
                }
                // Graphics
                if(!withImages.isEmpty()){
                    pw.println("[Graphics]");
                }                
                for(AssAttachment attach : withImages){
                    File file = new File(attach.getPath());
                    pw.println("filename: " + file.getName());
                    AssUU.writeUU(file, pw);
                    pw.println();
                }
                pw.println();
            }
            
            if(!ass.getActors().isEmpty()){
                pw.println("[Actors]");
                pw.println("Format: Name, Color, Kind, Image");
                for(AssActor actor : ass.getActors()){
                    pw.println(actor.toLine());
                }
                pw.println();
            }
            
            if(!ass.getEffects().isEmpty()){
                pw.println("[FXs]");
                pw.println("Format: Name, Effect");
                for(AssEffect fx : ass.getEffects()){
                    pw.println(fx.toLine());
                }
                pw.println();
            }
            
            // Events
            pw.println("[Events]");
            pw.println("Format: Layer, Start, End, Style, Name, MarginL, MarginR, MarginV, Effect, Text");
            for(AssEvent event : ass.getEvents()){
                pw.println(event.toRawLine());
            }
        } catch (IOException | AssColorException ex) {
            Logger.getLogger(ASS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static ASS inMemory(){
        return new ASS();
    }
}
