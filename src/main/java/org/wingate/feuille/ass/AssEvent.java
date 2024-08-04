/*
 * Copyright (C) 2018 util2
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
package org.wingate.feuille.ass;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.wingate.feuille.util.ISO_3166;

/**
 *
 * @author util2
 */
public class AssEvent {
    
    public enum LineType{
        Dialogue("Dialogue", "Dialogue"),
        Comment("Comment", "Comment"),
        Proposal("#Proposal", "#Proposal"),
        Request("#Request", "#Request"),
        Sound("#ASX_Sound", "Sound"),
        Movie("#ASX_Movie", "Movie"),
        Picture("#ASX_Picture", "Picture"),
        Commands("#ASX_Commands", "Command");
        
        String type;
        String origin;
        
        private LineType(String type, String origin){
            this.type = type;
            this.origin = origin;
        }

        @Override
        public String toString() {
            return type;
        }
        
        public static LineType from(String description){
            LineType lineType = Dialogue;
            for(LineType lt : values()){
                if(lt.toString().equalsIgnoreCase(description) == true){
                    lineType = lt;
                    break;
                }
                if(lt.getOrigin().equalsIgnoreCase(description) == true){
                    lineType = lt;
                    break;
                }
            }
            return lineType;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public String getOrigin() {
            return origin;
        }
        
    }
    
    private ASS ass = null;
    private int UID = -1;
    private int eventIndex = -1;
    private LineType lineType = LineType.Comment;
    private int layer = 0;
    private int marginL = 0;
    private int marginR = 0;
    private int marginV = 0; 
    private AssTime time = new AssTime(0L, 1000L);
    private AssStyle style = AssStyle.getDefault();
    private String name = "";
    private String effect = "";
    private String text = "";
    private List<String> splitKaraoke = new ArrayList<>();
    private Map<ISO_3166, String> allTexts = new HashMap<>();
    
    private BufferedImage image;

    public AssEvent() {        
        image = null;        
    }
    
    /**
     * Get an Event object from an ASS event line
     * Format: Layer, Start, End, Style, Name, MarginL, MarginR, MarginV, Effect, Text
     * @param ASS an ASS line
     * @param styles
     * @return An Event object
     */
    public static AssEvent createFromASS(String ASS, Map<String, AssStyle> styles){
        AssEvent ev = new AssEvent();
        String[] array = ASS.split(",", 10);
        if(array.length == 10){
            // Line type (Format)
            switch(array[0].substring(0, array[0].indexOf(":"))){
                case "Dialogue" -> ev.setLineType(LineType.Dialogue);
                case "Comment" -> ev.setLineType(LineType.Comment);
                case "#Proposal" -> ev.setLineType(LineType.Proposal);
                case "#Request" -> ev.setLineType(LineType.Request);
                default -> ev.setLineType(LineType.Comment);             }
            // Layer
            ev.setLayer(Integer.parseInt(array[0].substring(array[0].lastIndexOf(":") + 2)));
            // Start - End
            ev.getTime().setStartString(array[1]);
            ev.getTime().setEndString(array[2]);
            // Style
            ev.setStyle(styles.get(array[3]));
            // Name
            ev.setName(array[4]);
            // Margins LRV
            ev.setMarginL(Integer.parseInt(array[5]));
            ev.setMarginR(Integer.parseInt(array[6]));
            ev.setMarginV(Integer.parseInt(array[7]));
            // Effect
            ev.setEffect(array[8]);
            // Text
            ev.setText(array[9]);
        }
        return ev;
    }
    
    /**
     * Get an Event object from an ASS event line
     * Format: Layer, Start, End, Style, Name, MarginL, MarginR, MarginV, Effect, Text
     * @param ASS an ASS line
     * @return An Event object
     */
    public static AssEvent createFromASS(String ASS){
        AssEvent ev = new AssEvent();
        String[] array = ASS.split(",", 10);
        if(array.length == 10){
            // Line type (Format)
            switch(array[0].substring(0, array[0].indexOf(":"))){
                case "Dialogue" -> ev.setLineType(LineType.Dialogue);
                case "Comment" -> ev.setLineType(LineType.Comment);
                case "#Proposal" -> ev.setLineType(LineType.Proposal);
                case "#Request" -> ev.setLineType(LineType.Request);
                default -> ev.setLineType(LineType.Comment);             }
            // Layer
            ev.setLayer(Integer.parseInt(array[0].substring(array[0].lastIndexOf(":") + 2)));
            // Start - End
            ev.getTime().setStartString(array[1]);
            ev.getTime().setEndString(array[2]);
            // Style
            ev.setStyle(AssStyle.getDefault());
            // Name
            ev.setName(array[4]);
            // Margins LRV
            ev.setMarginL(Integer.parseInt(array[5]));
            ev.setMarginR(Integer.parseInt(array[6]));
            ev.setMarginV(Integer.parseInt(array[7]));
            // Effect
            ev.setEffect(array[8]);
            // Text
            ev.setText(array[9]);
        }
        return ev;
    }
    
    /**
     * Get an Event object from an ASS event line
     * Format: Layer, Start, End, Style, Name, MarginL, MarginR, MarginV, Effect, Text
     * @param SSA
     * @param styles
     * @return An Event object
     */
    public static AssEvent createFromSSA(String SSA, Map<String, AssStyle> styles){
        AssEvent ev = new AssEvent();
        String[] array = SSA.split(",", 10);
        if(array.length == 10){
            // Line type (Format)
            switch(array[0].substring(0, array[0].indexOf(":"))){
                case "Dialogue" -> ev.setLineType(LineType.Dialogue);
                case "Comment" -> ev.setLineType(LineType.Comment);
                case "#Proposal" -> ev.setLineType(LineType.Proposal);
                case "#Request" -> ev.setLineType(LineType.Request);
                default -> ev.setLineType(LineType.Comment);             }
            // Layer (dropped cause marked at this location)
            // Start - End
            ev.getTime().setStartString(array[1]);
            ev.getTime().setEndString(array[2]);
            // Style
            ev.setStyle(styles.get(array[3]));
            // Name
            ev.setName(array[4]);
            // Margins LRV
            ev.setMarginL(Integer.parseInt(array[5]));
            ev.setMarginR(Integer.parseInt(array[6]));
            ev.setMarginV(Integer.parseInt(array[7]));
            // Effect
            ev.setEffect(array[8]);
            // Text
            ev.setText(array[9]);
        }
        return ev;
    }
    
    /**
     * Get an Event object from an ASS event line
     * Format: Layer, Start, End, Style, Name, MarginL, MarginR, MarginV, Effect, Text
     * @param SSA
     * @return An Event object
     */
    public static AssEvent createFromSSA(String SSA){
        AssEvent ev = new AssEvent();
        String[] array = SSA.split(",", 10);
        if(array.length == 10){
            // Line type (Format)
            switch(array[0].substring(0, array[0].indexOf(":"))){
                case "Dialogue" -> ev.setLineType(LineType.Dialogue);
                case "Comment" -> ev.setLineType(LineType.Comment);
                case "#Proposal" -> ev.setLineType(LineType.Proposal);
                case "#Request" -> ev.setLineType(LineType.Request);
                default -> ev.setLineType(LineType.Comment);             }
            // Layer (dropped cause marked at this location)
            // Start - End
            ev.getTime().setStartString(array[1]);
            ev.getTime().setEndString(array[2]);
            // Style
            ev.setStyle(AssStyle.getDefault());
            // Name
            ev.setName(array[4]);
            // Margins LRV
            ev.setMarginL(Integer.parseInt(array[5]));
            ev.setMarginR(Integer.parseInt(array[6]));
            ev.setMarginV(Integer.parseInt(array[7]));
            // Effect
            ev.setEffect(array[8]);
            // Text
            ev.setText(array[9]);
        }
        return ev;
    }
    
    /**
     * Get an ASS line for this event
     * Format: Layer, Start, End, Style, Name, MarginL, MarginR, MarginV, Effect, Text
     * @param ev An event
     * @return An ASS formatted line
     */
    public static String getAssEventLine(AssEvent ev){
        if(ev.getStyle() == null) ev.setStyle(AssStyle.getDefault());
        if(ev.getName() == null) ev.setName("");
        if(ev.getEffect() == null) ev.setEffect("");
        if(ev.getText() == null) ev.setText("");
        
        String line = "";
        line += ev.getLineType().toString().concat(": ");
        line += Integer.toString(ev.getLayer()).concat(",");
        line += ev.getTime().getStartString().concat(",");
        line += ev.getTime().getEndString().concat(",");
        line += ev.getStyle().getName().concat(",");
        line += ev.getName().concat(",");
        line += Integer.toString(ev.getMarginL()).concat(",");
        line += Integer.toString(ev.getMarginR()).concat(",");
        line += Integer.toString(ev.getMarginV()).concat(",");
        line += ev.getEffect().concat(",");
        line += ev.getText();
        return line;
    }
    
    public static String getAssEventLine(AssEvent ev, ISO_3166 iso, String replacement){
        if(ev.getStyle() == null) ev.setStyle(AssStyle.getDefault());
        if(ev.getName() == null) ev.setName("");
        if(ev.getEffect() == null) ev.setEffect("");
        if(ev.getText() == null) ev.setText("");
        
        String line = "";
        line += iso.getAlpha3().concat(": ");
        line += Integer.toString(ev.getLayer()).concat(",");
        line += ev.getTime().getStartString().concat(",");
        line += ev.getTime().getEndString().concat(",");
        line += ev.getStyle().getName().concat(",");
        line += ev.getName().concat(",");
        line += Integer.toString(ev.getMarginL()).concat(",");
        line += Integer.toString(ev.getMarginR()).concat(",");
        line += Integer.toString(ev.getMarginV()).concat(",");
        line += ev.getEffect().concat(",");
        line += replacement;
        return line;
    }

    public ASS getAss() {
        return ass;
    }

    public void setAss(ASS ass) {
        this.ass = ass;
    }

    public int getEventIndex() {
        return eventIndex;
    }

    public void setEventIndex(int eventIndex) {
        this.eventIndex = eventIndex;
    }

    public void setLineType(LineType lineType) {
        this.lineType = lineType;
    }

    public LineType getLineType() {
        return lineType;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public int getLayer() {
        return layer;
    }

    public void setMarginL(int marginL) {
        this.marginL = marginL;
    }

    public int getMarginL() {
        return marginL;
    }

    public void setMarginR(int marginR) {
        this.marginR = marginR;
    }

    public int getMarginR() {
        return marginR;
    }

    public void setMarginV(int marginV) {
        this.marginV = marginV;
    }

    public int getMarginV() {
        return marginV;
    }

    public AssTime getTime() {
        return time;
    }

    public void setTime(AssTime time) {
        this.time = time;
    }
    
    public void setStyle(AssStyle style) {
        this.style = style;
    }

    public AssStyle getStyle() {
        return style;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public String getEffect() {
        return effect;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public List<String> getSplitKaraoke() {
        return splitKaraoke;
    }

    public void setSplitKaraoke(List<String> splitKaraoke) {
        this.splitKaraoke = splitKaraoke;
    }
    
    public AssEvent getCopy(){
        AssEvent nv = new AssEvent();
        
        nv.setLineType(lineType);
        nv.setLayer(layer);
        nv.setTime(time);
        nv.setStyle(style);
        nv.setName(name);
        nv.setMarginL(marginL);
        nv.setMarginR(marginR);
        nv.setMarginV(marginV);
        nv.setEffect(effect);
        nv.setText(text);
        
        return nv;
    }
    
    private String type = "AssEvent";
    
    public void setType(String type){
        this.type = type;
    }

    @Override
    public String toString() {
        return getText();
    }

    public int getUID() {
        return UID;
    }

    public void setUID(int UID) {
        this.UID = UID;
    }

    public Map<ISO_3166, String> getAllTexts() {
        return allTexts;
    }

    public void setAllTexts(Map<ISO_3166, String> allTexts) {
        this.allTexts = allTexts;
    }
    
    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
    
    public void clearImage(){
        image = null;
    }
}
