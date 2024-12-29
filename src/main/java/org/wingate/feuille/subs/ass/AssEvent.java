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

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;
import org.wingate.feuille.util.DrawColor;
import org.wingate.feuille.util.ISO_3166;
import org.wingate.feuille.util.Load;

/**
 *
 * @author util2
 */
public class AssEvent {

    public enum Type{
        Comment("Comment"),
        Dialogue("Dialogue");
        
        final String name;
        
        private Type(String name){
            this.name = name;
        }

        public String getName() {
            return name;
        }
        
    }

    private Type type = Type.Dialogue;
    private int layer = 0;
    private AssTime start = new AssTime();
    private AssTime end = new AssTime();
    private AssStyle style = new AssStyle();
    private AssActor name = new AssActor();
    private int marginL = 0;
    private int marginR = 0;
    private int marginV = 0;
    private AssEffect effect = new AssEffect();
    private AssTranslateTo translations;
    private ISO_3166 srcLanguage;
    private ISO_3166 dstLanguage;

    public AssEvent(AssTranslateTo translations, ISO_3166 srcLanguage, ISO_3166 dstLanguage){
        this.translations = translations;
        this.srcLanguage = srcLanguage;
        this.dstLanguage = dstLanguage == null ? ISO_3166.getISO_3166(Locale.getDefault().getISO3Country()) : dstLanguage;
    }

    public static AssEvent createFromRawLine(AssTranslateTo translations, ISO_3166 srcLanguage, ISO_3166 dstLanguage, String rawline, List<AssStyle> ls, List<AssActor> la, List<AssEffect> lf) {
        AssEvent event = new AssEvent(translations, srcLanguage, dstLanguage);

        String[] t = rawline.split(",", 10);

        event.type    = t[0].startsWith("Dialogue: ") ? Type.Dialogue : Type.Comment;
        event.layer   = Integer.parseInt(t[0].substring(t[0].indexOf(" ") + 1)); // layer
        event.start   = AssTime.create(t[1]); // start
        event.end     = AssTime.create(t[2]); // end
        event.style   = getStyleFromName(ls, t[3]); // style
        event.name    = getActorFromName(la, t[4]); // name
        event.marginL = Integer.parseInt(t[5]); // marginL
        event.marginR = Integer.parseInt(t[6]); // marginR
        event.marginV = Integer.parseInt(t[7]); // marginV
        event.effect  = getEffectFromName(lf, t[8]); // effect
        event.translations.getVersions().getLast().getTexts().add(t[9]); // text (original)

        return event;
    }
    
    public String toRawLine(ISO_3166 src, int index){
        StringBuilder sb = new StringBuilder(getType().getName() + ": ");
        sb.append(getLayer()).append(",");
        sb.append(getStart().toAss()).append(",");
        sb.append(getEnd().toAss()).append(",");
        sb.append(getStyle().getName()).append(",");
        sb.append(getName().getName()).append(",");
        sb.append(getMarginL()).append(",");
        sb.append(getMarginR()).append(",");
        sb.append(getMarginV()).append(",");
        sb.append(getEffect().getName()).append(",");
        if(src != null){
            sb.append(translations.getVersion(src).getTexts().get(index));
        }
        return sb.toString();
    }
    
    private static AssStyle getStyleFromName(List<AssStyle> l, String name){
        AssStyle v = new AssStyle();
        
        for(AssStyle s : l){
            if(s.getName().equals(name)){
                v = s;
                break;
            }
        }
        
        return v;
    }
    
    private static AssActor getActorFromName(List<AssActor> l, String name){
        AssActor v = new AssActor();
        
        for(AssActor s : l){
            if(s.getName().equals(name)){
                v = s;
                break;
            }
        }
        
        return v;
    }
    
    private static AssEffect getEffectFromName(List<AssEffect> l, String name){
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

    public AssTranslateTo getTranslations() {
        return translations;
    }

    public void setTranslations(AssTranslateTo translations) {
        this.translations = translations;
    }

    public float getCPS(int index){
        // Character per second
        String s = translations.getVersion(dstLanguage).getTexts().get(index);
        
        Pattern p = Pattern.compile("\\{[^\\}]*(?<text>[^\\{]*)");
        Matcher m = p.matcher(s);
        
        StringBuilder sb = new StringBuilder();
        
        while(m.find()){
            sb.append(m.group("text"));
        }
        
        if(!sb.isEmpty()) s = sb.toString();
        
        s = s.replace("\\N", "");
        s = s.replace(" ", "");
        s = s.replace(".", "");
        s = s.replace("?", "");
        s = s.replace("!", "");        
        s = s.replace(",", "");
        s = s.replace(";", "");
        
        return s.length() / 60f;
    }
    
    public float getCPL(int index){
        // Character per line minus space
        // Taking the greatest piece of line (if two lines separated by \N)
        String s = translations.getVersion(dstLanguage).getTexts().get(index);
        
        Pattern p = Pattern.compile("\\{[^\\}]*(?<text>[^\\{]*)");
        Matcher m = p.matcher(s);
        
        StringBuilder sb = new StringBuilder();
        
        while(m.find()){
            sb.append(m.group("text"));
        }
        
        if(!sb.isEmpty()) s = sb.toString();
        
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

    public static class Renderer extends JPanel implements TableCellRenderer {
        
        public enum Stripped {
            Off, Partially, On; 
        }
        
        private Stripped strippedOrigin = Stripped.Partially;
        private Stripped strippedTranslation = Stripped.Partially;
        private String partiallyStrippedSymbol = "◆";

        private final JLabel flagOrigin;
        private final JLabel textOrigin;

        private final JLabel flagTranslation;
        private final JLabel textTranslation;

        public Renderer() {
            JPanel withFlagOrigin = new JPanel();
            flagOrigin = new JLabel("");
            flagOrigin.setSize(20*4/3, 20);
            textOrigin = new JLabel("");
            textOrigin.setOpaque(true);

            JPanel withFlagTranslation = new JPanel();
            flagTranslation = new JLabel("");
            flagTranslation.setSize(20*4/3, 20);
            textTranslation = new JLabel("");
            textTranslation.setOpaque(true);
            
            withFlagOrigin.setLayout(new BorderLayout());
            withFlagOrigin.add(flagOrigin, BorderLayout.WEST);
            withFlagOrigin.add(textOrigin, BorderLayout.CENTER);
            
            withFlagTranslation.setLayout(new BorderLayout());
            withFlagTranslation.add(flagTranslation, BorderLayout.WEST);
            withFlagTranslation.add(textTranslation, BorderLayout.CENTER);
            
            setLayout(new GridLayout(2, 1, 1, 2));
            add(withFlagOrigin);
            add(withFlagTranslation);
        }
        

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            Color bg;
            
            if(value instanceof AssEvent event){
                AlphaComposite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
                
                textOrigin.setText(applyStrip(event.translations.getVersion(event.srcLanguage).getTexts().get(row), strippedOrigin));
                textTranslation.setText(applyStrip(event.translations.getLastVersion(event.dstLanguage).getTexts().get(row), strippedTranslation));
                
                if(event.getType() == Type.Comment){
                    bg = DrawColor.violet.getColor();
                }else{
                    // Get table background (avoid searching from any other way)
                    // FlatLaf properties >> Table.background
                    bg = UIManager.getColor("Table.background");
                }

                // Get table foreground (avoid searching from any other way)
                // FlatLaf properties >> Table.foreground
                Color fg = UIManager.getColor("Table.foreground");
                
                if(isSelected){
                    bg = UIManager.getColor("Table.selectionBackground");
                    fg = UIManager.getColor("Table.selectionForeground");
                }

                // Set color to label
                setBackground(bg);
                setForeground(fg);
                
                textOrigin.setBackground(bg);
                textOrigin.setForeground(fg);
                
                textTranslation.setBackground(bg);
                textTranslation.setForeground(fg);


                ImageIcon iOrigin = Load.fromResource("/org/wingate/feuille/" + event.translations.getVersion(event.srcLanguage).getIso().getAlpha2().toLowerCase() + ".gif");
                BufferedImage imgOrigin = new BufferedImage(20*4/3, 20, BufferedImage.TYPE_INT_RGB);
                Graphics2D gOrigin = imgOrigin.createGraphics();
                gOrigin.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                gOrigin.setColor(bg);
                gOrigin.fillRect(0, 0, 20*4/3, 20);
                gOrigin.setComposite(alpha);
                gOrigin.drawImage(iOrigin.getImage(), 0, 0, 20*4/3, 20, null);
                gOrigin.dispose();
                flagOrigin.setIcon(new ImageIcon(imgOrigin));
                
                ImageIcon iTranslation = Load.fromResource("/org/wingate/feuille/" + event.dstLanguage.getAlpha2().toLowerCase() + ".gif");
                BufferedImage imgTranslation = new BufferedImage(20*4/3, 20, BufferedImage.TYPE_INT_RGB);                
                Graphics2D gTranslation = imgTranslation.createGraphics();
                gTranslation.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                gTranslation.setColor(bg);
                gTranslation.fillRect(0, 0, 20*4/3, 20);
                gTranslation.setComposite(alpha);
                gTranslation.drawImage(iTranslation.getImage(), 0, 0, 20*4/3, 20, null);
                gTranslation.dispose();
                flagTranslation.setIcon(new ImageIcon(imgTranslation));
            }
            
            return this;
        }
        
        private String applyStrip(String s, Stripped stripped){
            try{
                String str = "";
                switch(stripped){
                    case On -> {
                        if(s.contains("{\\")){
                            try {
                                str = s.replaceAll("\\{[^\\}]+\\}", "");
                            } catch (Exception e) {
                                str = s;
                            }
                        }else{
                            str = s;
                        }
                    }
                    case Partially -> {
                        if(s.contains("{\\")){
                            try {
                                str = s.replaceAll("\\{[^\\}]+\\}", partiallyStrippedSymbol);
                            } catch (Exception e) {
                                str = s;
                            }
                        }else{
                            str = s;
                        }
                    }
                    case Off -> {
                        str = s;
                    }
                }
                return str;
            }catch(Exception _){
            }
            return s;
        }

        public void setStrippedAll(Stripped stripped) {
            strippedOrigin = stripped;
            strippedTranslation = stripped;
        }

        public void setStrippedOrigin(Stripped stripped) {
            strippedOrigin = stripped;
        }

        public void setStrippedTranslation(Stripped stripped) {
            strippedTranslation = stripped;
        }

        public void setPartiallyStrippedSymbol(String partiallyStrippedSymbol) {
            this.partiallyStrippedSymbol = partiallyStrippedSymbol;
        }
        
    }
}
