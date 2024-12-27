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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author util2
 */
public class AssFxDefault {
    
    private final List<State> states = new ArrayList<>();

    public AssFxDefault(String text, double ms) {
        
        int b1 = text.indexOf("{");
        int b2 = text.indexOf("}", b1);
        int stateIndex = 0;
        
        if(text.startsWith("{") && b2 != -1){
            // Has overrides
            String overrides = text.substring(b1 + 1, b2);
            
            if(!overrides.isEmpty()){
                State s = new State();
                String fx = overrides;
                State ref = s;
                
                s.setItalic(italic(ref.getItalic(), fx));
                s.setBold(bold(ref.getBold(), fx));
                s.setUnderline(underline(ref.getUnderline(), fx));
                s.setStrikeout(strikeout(ref.getStrikeout(), fx));
                s.setBord(bord(ref.getBord(), fx));
                s.setBordx(bordx(ref.getBordx(), fx));
                s.setBordy(bordy(ref.getBordy(), fx));
                s.setShad(shad(ref.getShad(), fx));
                s.setShadx(shadx(ref.getShadx(), fx));
                s.setShady(shady(ref.getShady(), fx));
                s.setBlurEdge(bluredge(ref.getBlurEdge(), fx));
                s.setBlur(blur(ref.getBlur(), fx));
                s.setFontname(fontname(ref.getFontname(), fx));
                s.setFontsize(fontsize(ref.getFontsize(), fx));
                s.setScalex(scalex(ref.getScalex(), fx));
                s.setScaley(scaley(ref.getScaley(), fx));
                s.setSpacing(spacing(ref.getSpacing(), fx));
                s.setFrx(frx(ref.getFrx(), fx));
                s.setFry(fry(ref.getFry(), fx));
                s.setFrz(frz(ref.getFrz(), fx));
                s.setFax(fax(ref.getFax(), fx));
                s.setFay(fay(ref.getFay(), fx));
                s.setEncoding(encoding(ref.getEncoding(), fx));
                s.setcText(cText(ref.getcText(), fx));
                s.setcKaraoke(cKara(ref.getcKaraoke(), fx));
                s.setcOutline(cBord(ref.getcOutline(), fx));
                s.setcShadow(cShad(ref.getcShadow(), fx));
                s.setAlign(align(ref.getAlign(), fx));
                s.setQ(q(ref.getQ(), fx));
                //s.setReset(reset(ref.getReset(), fx, ass, event));
                //s.setPosition(position(ref.getPosition(), fx, ass, event));
                s.setOrigin(origin(ref.getOrigin(), fx));
                
                states.add(s);
                
                stateIndex++;
            }else{
                states.add(new State());
                
                stateIndex++;
            }
        }
        
        if(text.length() > b2 + 1){
            String inner = text.substring(b2 + 1);
            if(inner.toLowerCase().contains("\\k")){
                // Has inner karaoke
                Pattern p = Pattern.compile("\\{(?<tag>[\\\\k\\\\K\\\\kf\\\\ko]+)(?<cstime>\\d+)(?<fx>[^\\}]*)\\}(?<syl>[^\\{]+)");
                Matcher m = p.matcher(inner);                
                
                while(m.find()){
                    State s = new State(
                            new Karaoke(
                                    KaraokeStyle.getStyle(m.group("tag")),
                                    Double.parseDouble(m.group("cstime")) * 10d,
                                    m.group("syl")
                            ),
                            ms,
                            ms,
                            inner
                    );
                    
                    String fx = m.group("fx");
                    
                    State ref = stateIndex > 0 ? states.get(stateIndex) : new State();
                    
                    s.setItalic(italic(ref.getItalic(), fx));
                    s.setBold(bold(ref.getBold(), fx));
                    s.setUnderline(underline(ref.getUnderline(), fx));
                    s.setStrikeout(strikeout(ref.getStrikeout(), fx));
                    s.setBord(bord(ref.getBord(), fx));
                    s.setBordx(bordx(ref.getBordx(), fx));
                    s.setBordy(bordy(ref.getBordy(), fx));
                    s.setShad(shad(ref.getShad(), fx));
                    s.setShadx(shadx(ref.getShadx(), fx));
                    s.setShady(shady(ref.getShady(), fx));
                    s.setBlurEdge(bluredge(ref.getBlurEdge(), fx));
                    s.setBlur(blur(ref.getBlur(), fx));
                    s.setFontname(fontname(ref.getFontname(), fx));
                    s.setFontsize(fontsize(ref.getFontsize(), fx));
                    s.setScalex(scalex(ref.getScalex(), fx));
                    s.setScaley(scaley(ref.getScaley(), fx));
                    s.setSpacing(spacing(ref.getSpacing(), fx));
                    s.setFrx(frx(ref.getFrx(), fx));
                    s.setFry(fry(ref.getFry(), fx));
                    s.setFrz(frz(ref.getFrz(), fx));
                    s.setFax(fax(ref.getFax(), fx));
                    s.setFay(fay(ref.getFay(), fx));
                    s.setEncoding(encoding(ref.getEncoding(), fx));
                    s.setcText(cText(ref.getcText(), fx));
                    s.setcKaraoke(cKara(ref.getcKaraoke(), fx));
                    s.setcOutline(cBord(ref.getcOutline(), fx));
                    s.setcShadow(cShad(ref.getcShadow(), fx));
                    s.setAlign(align(ref.getAlign(), fx));
                    s.setQ(q(ref.getQ(), fx));
                    //s.setReset(reset(ref.getReset(), fx, ass, event));
                    //s.setPosition(position(ref.getPosition(), fx, ass, event));
                    s.setOrigin(origin(ref.getOrigin(), fx));

                    // TODO apply effects

                    states.add(s);
                    
                    stateIndex++;
                }
            }else{
                // No karaoke
                String[] t = text.split("\\{");
                for(String str : t){
                    int index = str.lastIndexOf("}") + 1;
                    State s = new State(null, ms, ms, str.substring(index));
                    
                    State ref = stateIndex > 0 ? states.get(stateIndex) : new State();
                    String fx = str;
                    
                    s.setItalic(italic(ref.getItalic(), fx));
                    s.setBold(bold(ref.getBold(), fx));
                    s.setUnderline(underline(ref.getUnderline(), fx));
                    s.setStrikeout(strikeout(ref.getStrikeout(), fx));
                    s.setBord(bord(ref.getBord(), fx));
                    s.setBordx(bordx(ref.getBordx(), fx));
                    s.setBordy(bordy(ref.getBordy(), fx));
                    s.setShad(shad(ref.getShad(), fx));
                    s.setShadx(shadx(ref.getShadx(), fx));
                    s.setShady(shady(ref.getShady(), fx));
                    s.setBlurEdge(bluredge(ref.getBlurEdge(), fx));
                    s.setBlur(blur(ref.getBlur(), fx));
                    s.setFontname(fontname(ref.getFontname(), fx));
                    s.setFontsize(fontsize(ref.getFontsize(), fx));
                    s.setScalex(scalex(ref.getScalex(), fx));
                    s.setScaley(scaley(ref.getScaley(), fx));                   
                    s.setSpacing(spacing(ref.getSpacing(), fx));
                    s.setFrx(frx(ref.getFrx(), fx));
                    s.setFry(fry(ref.getFry(), fx));
                    s.setFrz(frz(ref.getFrz(), fx));
                    s.setFax(fax(ref.getFax(), fx));
                    s.setFay(fay(ref.getFay(), fx));
                    s.setEncoding(encoding(ref.getEncoding(), fx));
                    s.setcText(cText(ref.getcText(), fx));
                    s.setcKaraoke(cKara(ref.getcKaraoke(), fx));
                    s.setcOutline(cBord(ref.getcOutline(), fx));
                    s.setcShadow(cShad(ref.getcShadow(), fx));
                    s.setAlign(align(ref.getAlign(), fx));
                    s.setQ(q(ref.getQ(), fx));
                    //s.setReset(reset(ref.getReset(), fx, ass, event));
                    //s.setPosition(position(ref.getPosition(), fx, ass, event));
                    s.setOrigin(origin(ref.getOrigin(), fx));
                    
                    states.add(s);
                }
            }
        }
        
    }
    
    private double italic(double lastValue, String fx){
        return getFromDouble(lastValue, fx, "\\i", "\\\\i(\\d+\\.*\\d*)");
    }
    
    private double bold(double lastValue, String fx){
        return getFromDouble(lastValue, fx, "\\b", "\\\\b(\\d+\\.*\\d*)");
    }
    
    private double underline(double lastValue, String fx){
        return getFromDouble(lastValue, fx, "\\u", "\\\\u(\\d+\\.*\\d*)");
    }
    
    private double strikeout(double lastValue, String fx){
        return getFromDouble(lastValue, fx, "\\s", "\\\\s(\\d+\\.*\\d*)");
    }
    
    private double bord(double lastValue, String fx){
        return getFromDouble(lastValue, fx, "\\bord", "\\\\bord(\\d+\\.*\\d*)");
    }
    
    private double bordx(double lastValue, String fx){
        return getFromDouble(lastValue, fx, "\\xbord", "\\\\xbord(\\d+\\.*\\d*)");
    }
    
    private double bordy(double lastValue, String fx){
        return getFromDouble(lastValue, fx, "\\ybord", "\\\\ybord(\\d+\\.*\\d*)");
    }
    
    private double shad(double lastValue, String fx){
        return getFromDouble(lastValue, fx, "\\shad", "\\\\shad(\\d+\\.*\\d*)");
    }
    
    private double shadx(double lastValue, String fx){
        return getFromDouble(lastValue, fx, "\\xshad", "\\\\xshad(\\d+\\.*\\d*)");
    }
    
    private double shady(double lastValue, String fx){
        return getFromDouble(lastValue, fx, "\\yshad", "\\\\yshad(\\d+\\.*\\d*)");
    }
    
    private double bluredge(double lastValue, String fx){
        return getFromDouble(lastValue, fx, "\\be", "\\\\be(\\d+\\.*\\d*)");
    }
    
    private double blur(double lastValue, String fx){
        return getFromDouble(lastValue, fx, "\\blur", "\\\\blur(\\d+\\.*\\d*)");
    }
    
    private String fontname(String lastValue, String fx){
        return getFromString(lastValue, fx, "\\fn", "\\\\fn([^\\\\\\}]+)");
    }
    
    private double fontsize(double lastValue, String fx){
        return getFromDouble(lastValue, fx, "\\fs", "\\\\fs(\\d+\\.*\\d*)");
    }
    
    private double scalex(double lastValue, String fx){
        return getFromDouble(lastValue, fx, "\\fscx", "\\\\fscx(\\d+\\.*\\d*)");
    }
    
    private double scaley(double lastValue, String fx){
        return getFromDouble(lastValue, fx, "\\fscy", "\\\\fscy(\\d+\\.*\\d*)");
    }
    
    private double spacing(double lastValue, String fx){
        return getFromDouble(lastValue, fx, "\\fsp", "\\\\fsp(\\d+\\.*\\d*)");
    }
    
    private double frx(double lastValue, String fx){
        return getFromDouble(lastValue, fx, "\\frx", "\\\\frx(\\d+\\.*\\d*)");
    }
    
    private double fry(double lastValue, String fx){
        return getFromDouble(lastValue, fx, "\\fry", "\\\\fry(\\d+\\.*\\d*)");
    }
    
    private double frz(double lastValue, String fx){
        return getFromDouble(lastValue, fx, "\\fr", "\\\\fr[z]*(\\d+\\.*\\d*)");
    }
    
    private double fax(double lastValue, String fx){
        return getFromDouble(lastValue, fx, "\\fax", "\\\\fax(\\d+\\.*\\d*)");
    }
    
    private double fay(double lastValue, String fx){
        return getFromDouble(lastValue, fx, "\\fay", "\\\\fay(\\d+\\.*\\d*)");
    }
    
    private int encoding(int lastValue, String fx){
        return getFromInt(lastValue, fx, "\\fe", "\\\\fe([^\\\\\\}]+)");
    }
    
    private Color cText(Color lastValue, String fx){
        return getFromColor(lastValue, fx, "\\\\alpha([^\\\\\\}]+)", "\\\\1a([^\\\\\\}]+)", "\\\\[1]?c([^\\\\\\}]+)");
    }
    
    private Color cKara(Color lastValue, String fx){
        return getFromColor(lastValue, fx, "\\\\alpha([^\\\\\\}]+)", "\\\\2a([^\\\\\\}]+)", "\\\\2c([^\\\\\\}]+)");
    }
    
    private Color cBord(Color lastValue, String fx){
        return getFromColor(lastValue, fx, "\\\\alpha([^\\\\\\}]+)", "\\\\3a([^\\\\\\}]+)", "\\\\3c([^\\\\\\}]+)");
    }
    
    private Color cShad(Color lastValue, String fx){
        return getFromColor(lastValue, fx, "\\\\alpha([^\\\\\\}]+)", "\\\\4a([^\\\\\\}]+)", "\\\\4c([^\\\\\\}]+)");
    }
    
    private int align(int lastValue, String fx){
        int align = lastValue;
        if(fx.contains("\\an")){
            align = getFromInt(lastValue, fx, "\\an", "\\\\an(\\d+)");
        }else if(fx.contains("\\a")){
            align = getFromInt(lastValue, fx, "\\a", "\\\\a(\\d+)");
        }
        return align;
    }
    
    private int q(int lastValue, String fx){
        return getFromInt(lastValue, fx, "\\q", "\\\\q([^\\\\\\}]+)");
    }
    
    private AssStyle reset(AssStyle lastValue, String fx, ASS ass, AssEvent event){
        if(fx.contains("\\r")){
            Pattern p = Pattern.compile("\\\\r([^\\\\\\}]+)");
            Matcher m = p.matcher(fx);
            if(m.find()){
                String style = m.group(1);
                if(style.isEmpty() == true){
                    return event.getStyle();
                }else{
                    int index = -1;
                    for(int i=0; i<ass.getStyles().size(); i++){
                        AssStyle sty = ass.getStyles().get(i);
                        if(sty.getName().equals(style)){
                            index = i;
                            break;
                        }
                    }
                    if(index != -1){
                        return ass.getStyles().get(index);
                    }                    
                }
            }
        }
        return lastValue;
    }
    
    private Point2D position(Point2D lastValue, String fx, ASS ass, AssEvent event){
        if(fx.contains("\\pos")){
            Pattern p = Pattern.compile("\\\\pos\\((?<x>\\d+\\.*\\d*),(?<y>\\d+\\.*\\d*)\\)");
            Matcher m = p.matcher(fx);
            if(m.find()){
                
            }
        }
        if(fx.contains("\\move")){
            Pattern p = Pattern.compile("\\\\move\\((?<x1>\\d+\\.*\\d*),(?<y1>\\d+\\.*\\d*),(?<x2>\\d+\\.*\\d*),(?<y2>\\d+\\.*\\d*),*(?<t1>\\d*),*(?<t2>\\d*)\\)");
            Matcher m = p.matcher(fx);
            if(m.find()){
                
            }
        }
        return lastValue;
    }
    
    private Point2D origin(Point2D lastValue, String fx){
        if(fx.contains("\\org")){
            Pattern p = Pattern.compile("\\\\org\\((?<x>\\d+\\.*\\d*),(?<y>\\d+\\.*\\d*)\\)");
            Matcher m = p.matcher(fx);
            if(m.find()){
                
            }
        }
        return lastValue;
    }
    
    private String getFromString(String lastValue, String fx, String search, String regex){
        if(fx.contains(search)){
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(fx);
            if(m.find()){
                return m.group(1);
            }
        }
        return lastValue;
    }
    
    private double getFromDouble(double lastValue, String fx, String search, String regex){
        if(fx.contains(search)){
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(fx);
            if(m.find()){
                return Double.parseDouble(m.group(1));
            }
        }
        return lastValue;
    }
    
    private int getFromInt(int lastValue, String fx, String search, String regex){
        if(fx.contains(search)){
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(fx);
            if(m.find()){
                return Integer.parseInt(m.group(1));
            }
        }
        return lastValue;
    }
    
    private Color getFromColor(Color lastValue, String fx, String... regex){
        int alpha = lastValue.getAlpha();
        Color c = lastValue;
        Color x = c;
        
        Pattern p; Matcher m;
        
        for(String r : regex){
            if(r.contains("a")){ // alpha, 1a, 2a, 3a, 4a
                p = Pattern.compile(r);
                m = p.matcher(fx);
                
                if(m.find()){
                    alpha = 255 - Integer.parseInt(m.group(1));
                }
            }else if(r.contains("c")){ // c, 1c, 2c, 3c, 4c
                p = Pattern.compile(r);
                m = p.matcher(fx);
                
                if(m.find()){
                    try {
                        x = AssColor.fromScheme(m.group(1), AssColor.Scheme.BGR).getColor();
                    } catch (AssColorException ex) {
                        Logger.getLogger(AssFxDefault.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

        return new Color(x.getRed(), x.getGreen(), x.getBlue(), alpha);
    }
    
    public enum KaraokeStyle {
        None,
        Default,
        Fill,
        Outline;

        private KaraokeStyle(){ }

        public static KaraokeStyle getStyle(String tag){
            KaraokeStyle ks = None;

            switch(tag){
                case "kf", "K" -> { ks = Fill; }
                case "ko" -> { ks = Outline; }
                case "k" -> { ks = Default; }
            }

            return ks;
        }
    }
    
    public class Karaoke {
        private final KaraokeStyle kStyle;
        private final double msKara;
        private final String text;

        public Karaoke(KaraokeStyle kStyle, double msKara, String text) {
            this.kStyle = kStyle;
            this.msKara = msKara;
            this.text = text;
        }

        public KaraokeStyle getkStyle() {
            return kStyle;
        }

        public double getMsKara() {
            return msKara;
        }

        public String getText() {
            return text;
        }
    }
    
    public class State {
        
        private Karaoke karaoke;        
        private double msStart;
        private double msEnd;
        private String text;
        
        //ASS Tags (n, N, h) Not concerned.
        //Override tags
        //i1-i0                                     can not animate
        private double italic;
        //b1-b0-b<weight>                           can only animate with weight
        private double bold;
        //u1-u0                                     can not animate
        private double underline;
        //s1-s0                                     can not animate
        private double strikeout;
        //bord<size>                                can animate
        //xbord<size>                               can animate
        //ybord<size>                               can animate
        private double bord;
        private double bordx;
        private double bordy;
        //shad<depth>                               can animate
        //xshad<depth>                              can animate
        //yshad<depth>                              can animate
        private double shad;
        private double shadx;
        private double shady;
        //be0-be1-be<strength>                      can only animate with strength
        private double blurEdge;
        //blur<strength>                            can animate
        private double blur;
        //fn<name>                                  can not animate
        private String fontname;
        //fs<size>                                  can not animate >> animate in Feuille
        private double fontsize;
        //fscx<scale>                               can animate        
        //fscy<scale>                               can animate
        private double scalex;
        private double scaley;
        //fsp<spacing>                              can animate
        private double spacing;
        //frx<amount>                               can animate
        //fry<amount>                               can animate
        //frz<amount>                               can animate
        //fr<amount>                                can animate
        private double frx;
        private double fry;
        private double frz;
        //fax<factor>                               can animate
        //fay<factor>                               can animate
        private double fax;
        private double fay;
        //fe<id>                                    can not animate
        private int encoding;
        //c&H<bb><gg><rr>&                          can animate
        //1c&H<bb><gg><rr>&                         can animate
        //2c&H<bb><gg><rr>&                         can animate
        //3c&H<bb><gg><rr>&                         can animate
        //4c&H<bb><gg><rr>&                         can animate
        //alpha&H<aa>                               can animate
        //1a&H<aa>                                  can animate
        //2a&H<aa>                                  can animate
        //3a&H<aa>                                  can animate
        //4a&H<aa>                                  can animate
        private Color cText;
        private Color cKaraoke;
        private Color cOutline;
        private Color cShadow;
        //an<pos>                                   can not animate
        //a<pos>                                    can not animate
        private int align;
        //k<duration>                               only k-time
        //K<duration>                               only k-time
        //kf<duration>                              only k-time
        //ko<duration>                              only k-time
        // Duration see Karaoke
        //q<style>                                  can not animate
        private int q;
        //r-r<style>                                can not animate
        private AssStyle reset;
        //pos(<X>,<Y>)                              can not animate
        //move(<x1>,<y1>,<x2>,<y2>)                 can not animate
        //move(<x1>,<y1>,<x2>,<y2>,<t1>,<t2>)       can not animate
        private Point2D position;
        //org(<X>,<Y>)                              can not animate
        private Point2D origin;
        //fad(<fadein>,<fadeout>)                   can not animate
        //fade(<a1>,<a2>,<a3>,<t1>,<t2>,<t3>,<t4>)  can not animate
        
        //t(<style modifiers>)                      can not animate
        //t(<accel>,<style modifiers>)              can not animate
        //t(<t1>,<t2>,<style modifiers>)            can not animate
        //t(<t1>,<t2>,<accel>,<style modifiers>)    can not animate
        
        //clip(<x1>,<y1>,<x2>,<y2>)                 can animate
        //iclip(<x1>,<y1>,<x2>,<y2>)                can animate
        //clip(<drawing commands>)                  can not animate
        //clip(<scale>,<drawing commands>)          can not animate
        //iclip(<drawing commands>)                 can not animate
        //iclip(<scale>,<drawing commands>)         can not animate
        
        //p<scale>                                  can not animate
        //p0                                        can not animate
        
        //pbo - Baseline offset                     can not animate
        
        //
        //Drawing commands
        //m <x> <y> - Move
        //n <x> <y> - Move (no closing)
        //l <x> <y> - Line
        //b <x1> <y1> <x2> <y2> <x3> <y3> - Cubic Bézier curve
        //s <x1> <y1> <x2> <y2> <x3> <y3> .. <xN> <yN> - Cubic b-spline
        //p <x> <y> - Extend b-spline
        //c - Close b-spline
        
        public State(Karaoke karaoke, double msStart, double msEnd, String text) {
            this.karaoke = karaoke;
            this.msStart = msStart;
            this.msEnd = msEnd;
            this.text = text;
            
            italic = 0d;
            bold = 0d;
            underline = 0d;
            strikeout = 0d;
            bord = 4d;
            bordx = 0d;
            bordy = 0d;
            shad = 0d;
            shadx = 0d;
            shady = 0d;
            blurEdge = 0d;
            blur = 0d;
            fontname = "serif";
            fontsize = 0d;
            scalex = 0d;
            scaley = 0d;
            spacing = 0d;
            frx = 0d;
            fry = 0d;
            frz = 0d;
            fax = 0d;
            fay = 0d;
            encoding = 1;
            cText = Color.white;
            cKaraoke = Color.yellow;
            cOutline = Color.black;
            cShadow = Color.black;
            align = 2;
            q = 0;
            reset = null;
            position = new Point2D.Double(0, 0);
            origin = new Point2D.Double(0, 0);
        }

        public State() {
            this(null, 0d, 0d, "");
        }

        public Karaoke getKaraoke() {
            return karaoke;
        }

        public void setKaraoke(Karaoke karaoke) {
            this.karaoke = karaoke;
        }

        public double getMsStart() {
            return msStart;
        }

        public void setMsStart(double msStart) {
            this.msStart = msStart;
        }

        public double getMsEnd() {
            return msEnd;
        }

        public void setMsEnd(double msEnd) {
            this.msEnd = msEnd;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public double getItalic() {
            return italic;
        }

        public void setItalic(double italic) {
            this.italic = italic;
        }

        public double getBold() {
            return bold;
        }

        public void setBold(double bold) {
            this.bold = bold;
        }

        public double getUnderline() {
            return underline;
        }

        public void setUnderline(double underline) {
            this.underline = underline;
        }

        public double getStrikeout() {
            return strikeout;
        }

        public void setStrikeout(double strikeout) {
            this.strikeout = strikeout;
        }

        public double getBord() {
            return bord;
        }

        public void setBord(double bord) {
            this.bord = bord;
        }

        public double getBordx() {
            return bordx;
        }

        public void setBordx(double bordx) {
            this.bordx = bordx;
        }

        public double getBordy() {
            return bordy;
        }

        public void setBordy(double bordy) {
            this.bordy = bordy;
        }

        public double getShad() {
            return shad;
        }

        public void setShad(double shad) {
            this.shad = shad;
        }

        public double getShadx() {
            return shadx;
        }

        public void setShadx(double shadx) {
            this.shadx = shadx;
        }

        public double getShady() {
            return shady;
        }

        public void setShady(double shady) {
            this.shady = shady;
        }

        public double getBlurEdge() {
            return blurEdge;
        }

        public void setBlurEdge(double blurEdge) {
            this.blurEdge = blurEdge;
        }

        public double getBlur() {
            return blur;
        }

        public void setBlur(double blur) {
            this.blur = blur;
        }

        public String getFontname() {
            return fontname;
        }

        public void setFontname(String fontname) {
            this.fontname = fontname;
        }

        public double getFontsize() {
            return fontsize;
        }

        public void setFontsize(double fontsize) {
            this.fontsize = fontsize;
        }

        public double getScalex() {
            return scalex;
        }

        public void setScalex(double scalex) {
            this.scalex = scalex;
        }

        public double getScaley() {
            return scaley;
        }

        public void setScaley(double scaley) {
            this.scaley = scaley;
        }

        public double getSpacing() {
            return spacing;
        }

        public void setSpacing(double spacing) {
            this.spacing = spacing;
        }

        public double getFrx() {
            return frx;
        }

        public void setFrx(double frx) {
            this.frx = frx;
        }

        public double getFry() {
            return fry;
        }

        public void setFry(double fry) {
            this.fry = fry;
        }

        public double getFrz() {
            return frz;
        }

        public void setFrz(double frz) {
            this.frz = frz;
        }

        public double getFax() {
            return fax;
        }

        public void setFax(double fax) {
            this.fax = fax;
        }

        public double getFay() {
            return fay;
        }

        public void setFay(double fay) {
            this.fay = fay;
        }

        public int getEncoding() {
            return encoding;
        }

        public void setEncoding(int encoding) {
            this.encoding = encoding;
        }

        public Color getcText() {
            return cText;
        }

        public void setcText(Color cText) {
            this.cText = cText;
        }

        public Color getcKaraoke() {
            return cKaraoke;
        }

        public void setcKaraoke(Color cKaraoke) {
            this.cKaraoke = cKaraoke;
        }

        public Color getcOutline() {
            return cOutline;
        }

        public void setcOutline(Color cOutline) {
            this.cOutline = cOutline;
        }

        public Color getcShadow() {
            return cShadow;
        }

        public void setcShadow(Color cShadow) {
            this.cShadow = cShadow;
        }

        public int getAlign() {
            return align;
        }

        public void setAlign(int align) {
            this.align = align;
        }

        public int getQ() {
            return q;
        }

        public void setQ(int q) {
            this.q = q;
        }

        public AssStyle getReset() {
            return reset;
        }

        public void setReset(AssStyle reset) {
            this.reset = reset;
        }

        public Point2D getPosition() {
            return position;
        }

        public void setPosition(Point2D position) {
            this.position = position;
        }

        public Point2D getOrigin() {
            return origin;
        }

        public void setOrigin(Point2D origin) {
            this.origin = origin;
        }
        
    }
    
    private double getPercent(double start, double end, double ms){
        double min = Math.min(start, end);
        double max = Math.max(start, end);
        double diffMax = max - min;
        double diffCur = ms - min;

        return diffCur / (diffMax <= 0d ? 1d : diffMax);        
    }
    
    /*
    Voici les balises pour ne traiter que la fonte, soit la forme par défaut :
    - fn, fs, b, i, u, s - (compris dans 'AssFont')
    - fe - (encodage du texte, intervient en premier)
    - r - (redéfinit le style d'origine de la ligne ou en attribue un autre)
    Voici les balises pour traiter une forme :
    - fscx/y, fsp, fr/x/y/z, fax/y - (qui peuvent être utilisées en animation)
    - org, a, an, pos, les marges, le dessin - (non utilisable en animation)
    - t, move - (qui crée une animation)
    Voici les résultants sur image :
    - couleurs + transparences, bordure, ombre, flou, transition, karaoke, clip, iclip, q
    */

//    public void getImage(Graphics2D g, double currentMs){
//        AssFont font = event.getStyle().getAssFont();
//        Font f = font.getFont();
//
//        float x = 100f; // offset
//        float y = 100f; // offset
//
//        Point2D ref = new Point2D.Double(x, y);
//        Point2D last = ref;
//
//        double lastSize = 0d;
//        float fsp = 0f;
//        double bord = 0d, xbord = 0d, ybord = 0d;
//        double shad = 0d, xshad = 0d, yshad = 0d;
//
//        // Pour chaque state
//        for(int k=0; k<states.size(); k++){
//
//            // On récupère le state en cours
//            State s = states.get(k);
//
//            // On définit un compteur afin de savoir si on repasse à 0
//            // après que k soit différent de zéro dans la boucle d'affichage
//            int counter = 0;
//
//            // On récupère les glyphes du texte en state
//            GlyphVector gv = f.createGlyphVector(
//                    g.getFontMetrics(f).getFontRenderContext(),
//                    s.getText()
//            );
//
//            // Apply fn, fs, b, i, u, s
//            g.setFont(f);
//
//            // Pour chaque glyphe on dessine
//            for(int i=0; i<gv.getNumGlyphs(); i++){
//
//                //System.out.println(String.format(
//                //        "Anchor: %f + %f + %f = %f",
//                //        ref.getX(), last.getX(), lastSize - fsp,
//                //        ref.getX() + last.getX() + lastSize - fsp
//                //));
//
//                // Si on est sur un autre state, que counter == 0
//                // et que k n'est pas égale à 0, alors on peut avancer
//                // le point d'ancrage afin d'avoir un texte fluide
//                if(counter == 0 && k != 0){
//                    // Le point d'ancrage ref se définit par :
//                    // - le dernier ancrage ref
//                    // - le dernier ancrage last
//                    // - la taille du dernier caractère lastSize si non final
//                    //System.out.println("--");
//                    ref = new Point2D.Double(
//                            ref.getX() + last.getX() + lastSize - fsp,
//                            y
//                    );
//
//                    // Vérification - peut être décommentée
//                    //System.out.println("ref = " + ref.getX());
//                }
//
//                // On récupère les données de bord et shad
//                bord = s.getBord(); xbord = s.getBordx(); ybord = s.getBordy();
//                shad = s.getShad(); xshad = s.getShadx(); yshad = s.getShady();
//
//                // On obtient la forme au point d'ancrage ref
//                Shape sh = gv.getGlyphOutline(i, (float)ref.getX() + fsp, (float)ref.getY());
//
//                // On dessine le texte au point d'ancrage ref
//                // Ombre (shadow)
//                g.setColor(s.getcShadow());
//                g.fill(getAssShadow(sh, shad, xshad, yshad));
//
//                // Bordure (border)
//                g.setColor(s.getcOutline());
//                drawAssOutline(g, sh, bord, xbord, ybord);
//
//                // Karaoké (karaoke)
//                g.setColor(s.getcKaraoke());
//                g.fill(sh);
//
//                // Texte (text)
//                g.setColor(s.getcText());
//                g.fill(sh);
//
//                g.setColor(Color.red);
//                g.fillOval((int)(ref.getX() + fsp), (int)ref.getY(), 8, 8);
//                g.setFont(new Font("Arial", Font.PLAIN, 18));
//                g.drawString(String.valueOf(ref.getX() + fsp), (float)ref.getX() + fsp, (float)ref.getY() + 100);
//                //System.out.println(String.format("Result: %f + %f = %f", ref.getX(), fsp, ref.getX() + fsp));
//
//                // On applique les balises
//                fsp += (float)s.getSpacing();
//                //System.out.println("fsp: " + fsp);
//
//                // On définit ici le nouveau ancrage glyphe
//                last = gv.getGlyphPosition(i);
//                counter++;
//
//                // Vérification - peut être décommentée
//                //System.out.println("point(" + i + ") = " + last.getX());
//
//                FontMetrics fm = g.getFontMetrics(f);
//                lastSize = fm.charWidth(s.getText().charAt(i)) + fsp;
//            }
//
//        }
//    }
    
    private void drawAssOutline(Graphics2D g, Shape sh, double v, double x, double y){
        double value = x != 0 ? x : (y != 0 ? y : v);
        Stroke oldStroke = g.getStroke();
        Shape oldClip = g.getClip();
        
        BasicStroke bs = new BasicStroke((float)value * 2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        g.setStroke(bs);
        
        // TODO fix the size, here in true 4K
        Area rect = new Area(new Rectangle2D.Double(0d, 0d, 4096d, 2160d));
        Area shap = new Area(sh);
        rect.subtract(shap);
        
        g.setClip(rect);
        g.draw(sh);
        
        g.setClip(oldClip);
        g.setStroke(oldStroke);
    }
    
    private Shape getAssShadow(Shape sh, double v, double x, double y){
        Rectangle rModel = sh.getBounds();
        if(rModel.getWidth() <= 0d) return sh;
        
        AffineTransform tr = new AffineTransform();
        tr.translate(x != 0 ? x : v, y != 0 ? y : v);
        Area model = new Area(sh);
        
        return model.createTransformedArea(tr);
    }
}
