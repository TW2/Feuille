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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.wingate.feuille.util.ISO_3166;

/**
 *
 * @author util2
 */
public class AssEvent {
    public enum Type{
        Comment("Comment"),
        Dialogue("Dialogue");
        
        String name;
        
        private Type(String name){
            this.name = name;
        }

        public String getName() {
            return name;
        }
        
    }
    private Type type;
    private int layer;
    private AssTime start;
    private AssTime end;
    private AssStyle style;
    private AssActor name;
    private int marginL;
    private int marginR;
    private int marginV;
    private AssEffect effect;
    private String text;
    private Map<ISO_3166, String> links;

    public AssEvent(Type type, int layer, AssTime start, AssTime end,
            AssStyle style, AssActor name, int marginL, int marginR, int marginV,
            AssEffect effect, String text) {
        this.type = type;
        this.layer = layer;
        this.start = start;
        this.end = end;
        this.style = style;
        this.name = name;
        this.marginL = marginL;
        this.marginR = marginR;
        this.marginV = marginV;
        this.effect = effect;
        this.text = text;
        
        links = new HashMap<>();
    }

    public AssEvent() {
        this(
                Type.Dialogue,
                0, // layer
                new AssTime(), // start
                new AssTime(), // end
                new AssStyle(), // style
                new AssActor(), // name
                0, // marginL
                0, // marginR
                0, // marginV
                new AssEffect(), // effect
                "" // text
        );
    }
    
    public AssEvent(String rawline,
            List<AssStyle> ls, List<AssActor> la, List<AssEffect> lf){
        String[] t = rawline.split(",", 10);
        
        type    = t[0].startsWith("Dialogue: ") ? Type.Dialogue : Type.Comment;
        layer   = Integer.parseInt(t[0].substring(t[0].indexOf(" ") + 1)); // layer
        start   = AssTime.create(t[1]); // start
        end     = AssTime.create(t[2]); // end
        style   = getStyleFromName(ls, t[3]); // style
        name    = getActorFromName(la, t[4]); // name
        marginL = Integer.parseInt(t[5]); // marginL
        marginR = Integer.parseInt(t[6]); // marginR
        marginV = Integer.parseInt(t[7]); // marginV
        effect  = getEffectFromName(lf, t[8]); // effect
        text    = t[9]; // text
        
        links = new HashMap<>();
    }
    
    public String toRawLine(){
        StringBuilder sb = new StringBuilder(getType().getName() + ": ");
        sb = sb.append(getLayer()).append(",");
        sb = sb.append(getStart().toAss()).append(",");
        sb = sb.append(getEnd().toAss()).append(",");
        sb = sb.append(getStyle().getName()).append(",");
        sb = sb.append(getName().getName()).append(",");
        sb = sb.append(getMarginL()).append(",");
        sb = sb.append(getMarginR()).append(",");
        sb = sb.append(getMarginV()).append(",");
        sb = sb.append(getEffect().getName()).append(",");
        sb = sb.append(getText());
        return sb.toString();
    }
    
    public String toRawLine(ISO_3166 link){
        StringBuilder sb = new StringBuilder(getType().getName() + ": ");
        sb = sb.append(getLayer()).append(",");
        sb = sb.append(getStart().toAss()).append(",");
        sb = sb.append(getEnd().toAss()).append(",");
        sb = sb.append(getStyle().getName()).append(",");
        sb = sb.append(getName().getName()).append(",");
        sb = sb.append(getMarginL()).append(",");
        sb = sb.append(getMarginR()).append(",");
        sb = sb.append(getMarginV()).append(",");
        sb = sb.append(getEffect().getName()).append(",");
        sb = sb.append(links.get(link));
        return sb.toString();
    }
    
    private AssStyle getStyleFromName(List<AssStyle> l, String name){
        AssStyle v = new AssStyle();
        
        for(AssStyle s : l){
            if(s.getName().equals(name)){
                v = s;
                break;
            }
        }
        
        return v;
    }
    
    private AssActor getActorFromName(List<AssActor> l, String name){
        AssActor v = new AssActor();
        
        for(AssActor s : l){
            if(s.getName().equals(name)){
                v = s;
                break;
            }
        }
        
        return v;
    }
    
    private AssEffect getEffectFromName(List<AssEffect> l, String name){
        AssEffect v = new AssEffect();
        
        for(AssEffect s : l){
            if(s.getName().equals(name)){
                v = s;
                break;
            }
        }
        
        return v;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public AssTime getStart() {
        return start;
    }

    public void setStart(AssTime start) {
        this.start = start;
    }

    public AssTime getEnd() {
        return end;
    }

    public void setEnd(AssTime end) {
        this.end = end;
    }

    public AssStyle getStyle() {
        return style;
    }

    public void setStyle(AssStyle style) {
        this.style = style;
    }

    public AssActor getName() {
        return name;
    }

    public void setName(AssActor name) {
        this.name = name;
    }

    public int getMarginL() {
        return marginL;
    }

    public void setMarginL(int marginL) {
        this.marginL = marginL;
    }

    public int getMarginR() {
        return marginR;
    }

    public void setMarginR(int marginR) {
        this.marginR = marginR;
    }

    public int getMarginV() {
        return marginV;
    }

    public void setMarginV(int marginV) {
        this.marginV = marginV;
    }

    public AssEffect getEffect() {
        return effect;
    }

    public void setEffect(AssEffect effect) {
        this.effect = effect;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText(ISO_3166 link) {
        return links.get(link);
    }

    public void setText(ISO_3166 link, String text) {
        links.put(link, text);
    }

    public Map<ISO_3166, String> getLinks() {
        return links;
    }

    public void setLinks(Map<ISO_3166, String> links) {
        this.links = links;
    }
    
    public float getCPS(){
        // Character per second
        String s = text;
        
        Pattern p = Pattern.compile("\\{[^\\}]*(?<text>[^\\{]*)");
        Matcher m = p.matcher(s);
        
        StringBuilder sb = new StringBuilder();
        
        while(m.find()){
            sb.append(m.group("text"));
        }
        
        if(sb.isEmpty() == false) s = sb.toString();
        
        s = s.replace("\\N", "");
        s = s.replace(" ", "");
        s = s.replace(".", "");
        s = s.replace("?", "");
        s = s.replace("!", "");        
        s = s.replace(",", "");
        s = s.replace(";", "");
        
        return s.length() / 60f;
    }
    
    public float getCPL(){
        // Character per line minus space
        // Taking the greatest piece of line (if two lines separated by \N)
        String s = text;
        
        Pattern p = Pattern.compile("\\{[^\\}]*(?<text>[^\\{]*)");
        Matcher m = p.matcher(s);
        
        StringBuilder sb = new StringBuilder();
        
        while(m.find()){
            sb.append(m.group("text"));
        }
        
        if(sb.isEmpty() == false) s = sb.toString();
        
        s = s.replace(" ", "");
        if(s.contains("\\N")){
            String[] t = s.split("\\\\N");
            String sText = t[0];
            for(String str : t){
                if(str.length() > sText.length()) sText = str;
            }
            s = sText;
        }
        
        return (float)s.length();
    }
}
