package feuille.module.editor.assa;

import feuille.util.assa.AssAttachmentType;
import feuille.util.assa.AssScriptSection;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ASS {
    // ASS
    private AssInfo infos = new AssInfo();
    private List<AssStyle> styles = new ArrayList<>();
    private List<AssEvent> events = new ArrayList<>();
    private List<AssAttachment> attachs = new ArrayList<>();

    // Feuille SubSystem for ASS
    private List<AssFont> fonts = new ArrayList<>();
    private List<AssActor> actors = new ArrayList<>();
    private List<AssEffect> effects = new ArrayList<>();

    public ASS() {
        styles.add(new AssStyle());
        actors.add(new AssActor());
        effects.add(new AssEffect());
    }

    public AssInfo getInfos() {
        return infos;
    }

    public void setInfos(AssInfo infos) {
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
            AssScriptSection sec = AssScriptSection.ScriptInfos;

            while((line = br.readLine()) != null){
                if(line.isEmpty()) continue;

                // Define section
                if(line.startsWith("[Script Infos]")){
                    sec = AssScriptSection.ScriptInfos; continue;
                }else if(line.startsWith("[Aegisus Project Garbage]")){
                    sec = AssScriptSection.Aegisub; continue;
                }else if(line.startsWith("[V4+ Styles]")){
                    sec = AssScriptSection.Styles; continue;
                }else if(line.startsWith("[Fonts]")){
                    sec = AssScriptSection.Fonts; continue;
                }else if(line.startsWith("[Graphics]")){
                    sec = AssScriptSection.Graphics; continue;
                }else if(line.startsWith("[Actors]")){
                    sec = AssScriptSection.Actors; continue;
                }else if(line.startsWith("[FXs]")){
                    sec = AssScriptSection.FXs; continue;
                }else if(line.startsWith("[Events]")){
                    sec = AssScriptSection.Events; continue;
                }

                if(sec == AssScriptSection.ScriptInfos){
                    AssInfo.read(ass.getInfos(), br);
                }

                if(line.startsWith("Style: ") && sec == AssScriptSection.Styles){
                    AssStyle style = new AssStyle(line);
                    ass.getStyles().add(style);
                    ass.getFonts().add(style.getAssFont());
                }else if(line.startsWith("fontname: ") && sec == AssScriptSection.Fonts){
                    try{
                        // UUDECODE doesn't work
                        String name = line.substring("fontname: ".length());
                        File f = AssUUEncoding.readUU(br, new File(name).getPath());
                        ass.getAttachs().add(new AssAttachment(f.getPath(), AssAttachmentType.Font));
                    }catch(Exception _){ }
                }else if(line.startsWith("filename: ") && sec == AssScriptSection.Graphics){
                    try{
                        // UUDECODE doesn't work
                        String name = line.substring("filename: ".length());
                        File f = AssUUEncoding.readUU(br, new File(name).getPath());
                        ass.getAttachs().add(new AssAttachment(f.getPath(), AssAttachmentType.Image));
                    }catch(Exception _){ }
                }else if(line.startsWith("Actor: ") && sec == AssScriptSection.Actors){
                    try{
                        ass.getActors().add(AssActor.fromLine(line));
                    }catch(Exception _){ }
                }else if(line.startsWith("FX: ") && sec == AssScriptSection.FXs){
                    try{
                        ass.getEffects().add(AssEffect.fromLine(line));
                    }catch(Exception _){ }
                }else if((line.startsWith("Dialogue: ") || line.startsWith("Comment: "))
                        && sec == AssScriptSection.Events){
                    AssEvent event = AssEvent.createFromRawLine(
                            line, ass.getStyles(), ass.getActors(), ass.getEffects());
                    ass.getEvents().add(event);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ASS.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ass;
    }

    public static void write(ASS ass, String path){
        try(PrintWriter pw = new PrintWriter(path, StandardCharsets.UTF_8);){
            // Script Info
            pw.println("[Script Info]");
            pw.println("; Script generated by \\\\ LoliSub //");
            pw.println("; https://github.com/TW2/LoliSub");
            AssInfo.writeInfos(ass.getInfos(), pw);
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
                    AssUUEncoding.writeUU(file, pw);
                    pw.println();
                }
                // Graphics
                if(!withImages.isEmpty()){
                    pw.println("[Graphics]");
                }
                for(AssAttachment attach : withImages){
                    File file = new File(attach.getPath());
                    pw.println("filename: " + file.getName());
                    AssUUEncoding.writeUU(file, pw);
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
        } catch (IOException ex) {
            Logger.getLogger(ASS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static ASS inMemory(){
        return new ASS();
    }
}
