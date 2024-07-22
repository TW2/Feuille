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

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author util2
 */
public class AssStyle {
    
    private String Name = "Default";
    private String Fontname = "Arial";
    private double Fontsize = 28d;
    private String PrimaryColour = "0000FFFF";
    private String SecondaryColour = "0000FFFF";
    private String OutlineColor = "00000000";
    private String BackColour = "00000000";
    private boolean Bold = false;
    private boolean Italic = false;
    private boolean Underline = false;
    private boolean Strikeout = false;
    private int ScaleX = 100;
    private int ScaleY = 100;
    private float Spacing = 0;
    private float Angle = 0;
    private int BorderStyle = 1;
    private double Outline = 2d;
    private double Shadow = 0d;
    private int Alignment = 2;
    private int MarginL = 10;
    private int MarginR = 10;
    private int MarginV = 10;
    private int MarginB = 10;
    private int MarginT = 10;
    //private int AlphaLevel = 0;
    private int Encoding = 1;
    //private int RelativeTo = 0;

    public AssStyle() {
        
    }
    
    /**
     * Get a style from ASS line
     * Format: Name, Fontname, Fontsize, 
     * PrimaryColour, SecondaryColour, OutlineColour, BackColour,
     * Bold, Italic, Underline, StrikeOut, ScaleX, ScaleY, Spacing, Angle,
     * BorderStyle, Outline, Shadow, Alignment, MarginL, MarginR, MarginV, Encoding
     * @param ASS The line
     * @return A Style object
     */
    public static AssStyle create(String ASS){
        AssStyle style = new AssStyle();
        String[] array = ASS.split(",");
        // 00 - Name
        style.setName(array[0].substring("Style: ".length()));
        // 01 - Fontname
        style.setFontname(array[1]);
        // 02 - Fontsize
        style.setFontsize(Integer.parseInt(array[2]));
        // 03 - PrimaryColour
        style.PrimaryColour = array[3].substring(2);
        // 04 - SecondaryColour
        style.SecondaryColour = array[4].substring(2);
        // 05 - OutlineColour
        style.OutlineColor = array[5].substring(2);
        // 06 - BackColour
        style.BackColour = array[6].substring(2);
        // 07 - Bold
        style.setBold(array[7].contains("1") || array[7].contains("-1"));
        // 08 - Italic
        style.setItalic(array[8].contains("1") || array[8].contains("-1"));
        // 09 - Underline
        style.setUnderline(array[9].contains("1") || array[9].contains("-1"));
        // 10 - StrikeOut
        style.setStrikeout(array[10].contains("1") || array[10].contains("-1"));
        // 11 - ScaleX
        style.setScaleX(Integer.parseInt(array[11]));
        // 12 - ScaleY
        style.setScaleY(Integer.parseInt(array[12]));
        // 13 - Spacing
        style.setSpacing(Float.parseFloat(array[13]));
        // 14 - Angle
        style.setAngle(Float.parseFloat(array[14]));
        // 15 - BorderStyle
        style.setBorderStyle(Integer.parseInt(array[15]));
        // 16 - Outline
        style.setOutline(Math.round(Float.parseFloat(array[16])));
        // 17 - Shadow
        style.setShadow(Math.round(Float.parseFloat(array[17])));
        // 18 - Alignment
        style.setAlignment(Integer.parseInt(array[18]));
        // 19 - MarginL
        style.setMarginL(Integer.parseInt(array[19]));
        // 20 - MarginR
        style.setMarginR(Integer.parseInt(array[20]));
        // 21 - MarginV
        style.setMarginV(Integer.parseInt(array[21]));
        // 22 - Encoding
        style.setEncoding(Integer.parseInt(array[22]));
        return style;
    }
    
    public static AssStyle getDefault(){
        return AssStyle.create(AssStyle.getStyle());
    }
    
    public static String getStyle(){
        AssStyle style = new AssStyle();
        return "Style: "
                + style.Name + ","
                + style.Fontname + "," 
                + Integer.toString((int)Math.ceil(style.Fontsize)) + ","
                + "&H" + style.PrimaryColour + ","
                + "&H" + style.SecondaryColour + ","
                + "&H" + style.OutlineColor + ","
                + "&H" + style.BackColour + ","
                + (style.Bold == true ? "1" : "0") + ","
                + (style.Italic == true ? "1" : "0") + ","
                + (style.Underline == true ? "1" : "0") + ","
                + (style.Strikeout == true ? "1" : "0") + ","
                + Integer.toString((int)Math.ceil(style.ScaleX)) + ","
                + Integer.toString((int)Math.ceil(style.ScaleY)) + ","
                + Float.toString(style.Spacing) + ","
                + Float.toString(style.Angle) + ","
                + Integer.toString(style.BorderStyle) + ","
                + Integer.toString((int) Math.round(style.Outline)) + ","
                + Integer.toString((int) Math.round(style.Shadow)) + ","
                + Integer.toString(style.Alignment) + ","
                + Integer.toString(style.MarginL) + ","
                + Integer.toString(style.MarginR) + ","
                + Integer.toString(style.MarginV) + ","
                + Integer.toString(style.Encoding);
    }
    
    //<editor-fold defaultstate="collapsed" desc="Setter Getter">
    
    //<editor-fold defaultstate="collapsed" desc="Name">
    
    /**
     * Set the name.
     * @param name A name
     */
    public void setName(String name){
        Name = name;
    }

    /**
     * Get the name.
     * @return A name
     */
    public String getName(){
        return Name;
    }
    
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Font">
    
    /**
     * Set the font name.
     * @param fontname A font name
     */
    public void setFontname(String fontname){
        Fontname = fontname;
    }

    /**
     * Get the font name.
     * @return A font name
     */
    public String getFontname(){
        return Fontname;
    }

    /**
     * Set the font size.
     * @param fontsize A font size
     */
    public void setFontsize(double fontsize){
        Fontsize = fontsize;
    }

    /**
     * Get the font size.
     * @return A font size
     */
    public double getFontsize(){
        return Fontsize;
    }
    
    /**
     * Get the font.
     * @return A font
     */
    public Font getFont(){
        int styleOfFont = Font.PLAIN;
        if(isBold() && isItalic()){
            styleOfFont = Font.BOLD+Font.ITALIC;
        }else if(isBold()){
            styleOfFont = Font.BOLD;
        }else if(isItalic()){
            styleOfFont = Font.ITALIC;
        }
        Font font = new Font(getFontname(),styleOfFont,(int)getFontsize());
        return font;
    }
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Text color">

    /**
     * Set the text color.
     * @param color A color
     */
    public void setTextColor(Color color){
        String sa = Integer.toString(255-color.getAlpha(), 16);
        String sb = Integer.toString(color.getBlue(), 16);
        String sg = Integer.toString(color.getGreen(), 16);
        String sr = Integer.toString(color.getRed(), 16);
        if(sa.length() == 1){ sa = "0".concat(sa); }
        if(sb.length() == 1){ sb = "0".concat(sb); }
        if(sg.length() == 1){ sg = "0".concat(sg); }
        if(sr.length() == 1){ sr = "0".concat(sr); }
        PrimaryColour = sa+sb+sg+sr;
    }

    /**
     * Get the text color in string format.(with or without alpha)
     * @param alpha if we want or not
     * @return String hexadecimal without alpha
     */
    public String getTextColorHex(boolean alpha){
        return alpha == true ? PrimaryColour : PrimaryColour.substring(2);
    }

    /**
     * Get the text in hexadecimal format.
     * @return String hexadecimal alpha
     */
    public String getTextAlphaHex(){
        return PrimaryColour.substring(0,2);
    }
    
    /**
     * Get the text
     * @return String number alpha
     */
    public int getTextAlphaStr(){
        return Integer.parseInt(PrimaryColour.substring(0, 2), 16);
    }

    /**
     * Get the text color.
     * @return A color
     */
    public Color getTextColor(){
        int alpha = Integer.parseInt(PrimaryColour.substring(0, 2), 16);
        int blue = Integer.parseInt(PrimaryColour.substring(2, 4), 16);
        int green = Integer.parseInt(PrimaryColour.substring(4, 6), 16);
        int red = Integer.parseInt(PrimaryColour.substring(6), 16);
        return new Color(red, green, blue, 255-alpha);
    }
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Karaoke color">

    /**
     * Set the karaoke color.
     * @param color A color
     */
    public void setKaraokeColor(Color color){
        String sa = Integer.toString(255-color.getAlpha(), 16);
        String sb = Integer.toString(color.getBlue(), 16);
        String sg = Integer.toString(color.getGreen(), 16);
        String sr = Integer.toString(color.getRed(), 16);
        if(sa.length() == 1){ sa = "0".concat(sa); }
        if(sb.length() == 1){ sb = "0".concat(sb); }
        if(sg.length() == 1){ sg = "0".concat(sg); }
        if(sr.length() == 1){ sr = "0".concat(sr); }
        SecondaryColour = sa+sb+sg+sr;
    }

    /**
     * Get the karaoke color in string format.(with or without alpha)
     * @param alpha if we want or not
     * @return String hexadecimal without alpha
     */
    public String getKaraokeColorHex(boolean alpha){
        return alpha == true ? SecondaryColour : SecondaryColour.substring(2);
    }

    /**
     * Get the karaoke in hexadecimal format.
     * @return String hexadecimal alpha
     */
    public String getKaraokeAlphaHex(){
        return SecondaryColour.substring(0,2);
    }
    
    /**
     * Get the karaoke
     * @return String number alpha
     */
    public int getKaraokeAlphaStr(){
        return Integer.parseInt(SecondaryColour.substring(0, 2), 16);
    }

    /**
     * Get the karaoke color.
     * @return A color
     */
    public Color getKaraokeColor(){
        int alpha = Integer.parseInt(SecondaryColour.substring(0, 2), 16);
        int blue = Integer.parseInt(SecondaryColour.substring(2, 4), 16);
        int green = Integer.parseInt(SecondaryColour.substring(4, 6), 16);
        int red = Integer.parseInt(SecondaryColour.substring(6), 16);
        return new Color(red, green, blue, 255-alpha);
    }
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Border (outline) color">

    /**
     * Set the border (outline) color.
     * @param color A color
     */
    public void setBordColor(Color color){
        String sa = Integer.toString(255-color.getAlpha(), 16);
        String sb = Integer.toString(color.getBlue(), 16);
        String sg = Integer.toString(color.getGreen(), 16);
        String sr = Integer.toString(color.getRed(), 16);
        if(sa.length() == 1){ sa = "0".concat(sa); }
        if(sb.length() == 1){ sb = "0".concat(sb); }
        if(sg.length() == 1){ sg = "0".concat(sg); }
        if(sr.length() == 1){ sr = "0".concat(sr); }
        OutlineColor = sa+sb+sg+sr;
    }

    /**
     * Get the border (outline) color in string format.(with or without alpha)
     * @param alpha if we want or not
     * @return String hexadecimal without alpha
     */
    public String getBordColorHex(boolean alpha){
        return alpha == true ? OutlineColor : OutlineColor.substring(2);
    }

    /**
     * Get the border (outline) in hexadecimal format.
     * @return String hexadecimal alpha
     */
    public String getBordAlphaHex(){
        return OutlineColor.substring(0,2);
    }
    
    /**
     * Get the border (outline)
     * @return String number alpha
     */
    public int getBordAlphaStr(){
        return Integer.parseInt(OutlineColor.substring(0, 2), 16);
    }

    /**
     * Get the border (outline) color.
     * @return A color
     */
    public Color getBordColor(){
        int alpha = Integer.parseInt(OutlineColor.substring(0, 2), 16);
        int blue = Integer.parseInt(OutlineColor.substring(2, 4), 16);
        int green = Integer.parseInt(OutlineColor.substring(4, 6), 16);
        int red = Integer.parseInt(OutlineColor.substring(6), 16);
        return new Color(red, green, blue, 255-alpha);
    }
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Shadow color">

    /**
     * Set the shadow color.
     * @param color A color
     */
    public void setShadColor(Color color){
        String sa = Integer.toString(255-color.getAlpha(), 16);
        String sb = Integer.toString(color.getBlue(), 16);
        String sg = Integer.toString(color.getGreen(), 16);
        String sr = Integer.toString(color.getRed(), 16);
        if(sa.length() == 1){ sa = "0".concat(sa); }
        if(sb.length() == 1){ sb = "0".concat(sb); }
        if(sg.length() == 1){ sg = "0".concat(sg); }
        if(sr.length() == 1){ sr = "0".concat(sr); }
        BackColour = sa+sb+sg+sr;
    }

    /**
     * Get the shadow color in string format.(with or without alpha)
     * @param alpha if we want or not
     * @return String hexadecimal without alpha
     */
    public String getShadColorHex(boolean alpha){
        return alpha == true ? BackColour : BackColour.substring(2);
    }

    /**
     * Get the shadow alpha in hexadecimal format.
     * @return String hexadecimal alpha
     */
    public String getShadAlphaHex(){
        return BackColour.substring(0,2);
    }
    
    /**
     * Get the shadow alpha.
     * @return String number alpha
     */
    public int getShadAlphaStr(){
        return Integer.parseInt(BackColour.substring(0, 2), 16);
    }

    /**
     * Get the shadow color.
     * @return A color
     */
    public Color getShadColor(){
        int alpha = Integer.parseInt(BackColour.substring(0, 2), 16);
        int blue = Integer.parseInt(BackColour.substring(2, 4), 16);
        int green = Integer.parseInt(BackColour.substring(4, 6), 16);
        int red = Integer.parseInt(BackColour.substring(6), 16);
        return new Color(red, green, blue, 255-alpha);
    }
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Font Style">

    /**
     * Set a bold value
     * @param bold true if bold otherwise false
     */
    public void setBold(boolean bold){
        Bold = bold;
    }

    /**
     * Get the value of bold.
     * @return true if bold otherwise false
     */
    public boolean isBold(){
        return Bold;
    }

    /**
     * Set an italic value
     * @param italic true if italic otherwise false
     */
    public void setItalic(boolean italic){
        Italic = italic;
    }

    /**
     * Get the value of italic.
     * @return true if italic otherwise false
     */
    public boolean isItalic(){
        return Italic;
    }

    /**
     * Set an underline value
     * @param underline true if underline otherwise false
     */
    public void setUnderline(boolean underline){
        Underline = underline;
    }

    /**
     * Get the value of underline.
     * @return true if underline otherwise false
     */
    public boolean isUnderline(){
        return Underline;
    }

    /**
     * Set a strikeout value
     * @param strikeout true if strikeout otherwise false
     */
    public void setStrikeout(boolean strikeout){
        Strikeout = strikeout;
    }

    /**
     * Get the value of strikeout.
     * @return true if strikeout otherwise false
     */
    public boolean isStrikeout(){
        return Strikeout;
    }
    
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Scale X Y">
    
    /**
     * Set the scale on X.
     * @param scx The scale of the x axis (\fscx) in percent (integer) where 0% is the minimum
     */
    public void setScaleX(int scx){
        ScaleX = scx;
    }

    /**
     * Get the scale on X.
     * @return The scale of the x axis (\fscx) in percent (integer) where 0% is the minimum
     */
    public int getScaleX(){
        return ScaleX;
    }

    /**
     * Set the scale on Y.
     * @param scy The scale of the y axis (\fscy) in percent (integer) where 0% is the minimum
     */
    public void setScaleY(int scy){
        ScaleY = scy;
    }

    /**
     * Get the scale on Y.
     * @return The scale of the y axis (\fscy) in percent (integer) where 0% is the minimum
     */
    public int getScaleY(){
        return ScaleY;
    }
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Angle Z">
    
    /**
     * Set the angle on Z.
     * @param angle An angle (\frz) in degrees
     */
    public void setAngle(float angle){
        Angle = angle;
    }

    /**
     * Get the angle on Z.
     * @return An angle (\frz) in degrees
     */
    public float getAngle(){
        return Angle;
    }
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Spacing">

    /**
     * Set the spacing between letters.
     * @param spacing A spacing value
     */
    public void setSpacing(float spacing){
        Spacing = spacing;
    }

    /**
     * Get the spacing between letters.
     * @return A spacing value
     */
    public float getSpacing(){
        return Spacing;
    }
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="BorderStyle">
    
    /**
     * Set the border style.
     * @param bs 3 if there is an opaque box otherwise 1
     */
    public void setBorderStyle(int bs){
        BorderStyle = bs;
    }
    
    /**
     * Set the border style.
     * @param bs true if there is an opaque box otherwise false
     */
    public void setBorderStyle(boolean bs){
        BorderStyle = bs == true ? 3 : 1;
    }
    
    /**
     * Get the border style.
     * @return 3 if there is an opaque box otherwise 1
     */
    public int getBorderStyle(){
        return BorderStyle;
    }
    
    /**
     * Get the border style.
     * @return true if there is an opaque box otherwise false 
     */
    public boolean isBorderStyleOpaque(){
        return BorderStyle != 1;
    }
    
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Outline">
    
    /**
     * Set the width of the outline.
     * @param outline An outline thickness
     */
    public void setOutline(double outline){
        Outline = outline;
    }

    /**
     * Get the width of the outline.
     * @return An outline thickness
     */
    public double getOutline(){
        return Outline;
    }
    
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Shadow">
    
    /**
     * Set the depth of the shadow.
     * @param shadow A shadow thickness
     */
    public void setShadow(double shadow){
        Shadow = shadow;
    }

    /**
     * Get the depth of the shadow.
     * @return A shadow thickness
     */
    public double getShadow(){
        return Shadow;
    }
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Alignment">

    /**
     * Set the alignment of the text.
     * @param alignment An alignment
     */
    public void setAlignment(int alignment){
        Alignment = alignment;
    }

    /**
     * Get the alignment of the text.
     * @return An alignment
     */
    public int getAlignment(){
        return Alignment;
    }
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Margins">

    /**
     * Set the size of the left margin.
     * @param mL A left margin value
     */
    public void setMarginL(int mL){
        MarginL = mL;
    }

    /**
     * Get the size of the left margin.
     * @return Value of left margin
     */
    public int getMarginL(){
        return MarginL;
    }

    /**
     * Set the size of the right margin.
     * @param mR A right margin value
     */
    public void setMarginR(int mR){
        MarginR = mR;
    }

    /**
     * Get the size of the right margin.
     * @return Value of right margin
     */
    public int getMarginR(){
        return MarginR;
    }

    /**
     * Set the size of the vertical margin.
     * @param mV A vartical margin value
     */
    public void setMarginV(int mV){
        MarginV = mV;
    }

    /**
     * Get the size of the vertical margin.
     * @return Value of vertical margin
     */
    public int getMarginV(){
        return MarginV;
    }

    /**
     * Set the size of the bottom margin.
     * @param mB A bottom margin value
     */
    public void setMarginB(int mB){
        MarginB = mB;
    }

    /**
     * Get the size of the bottom margin.
     * @return Value of bottom margin
     */
    public int getMarginB(){
        return MarginB;
    }

    /**
     * Set the size of the top margin.
     * @param mT A top margin value
     */
    public void setMarginT(int mT){
        MarginT = mT;
    }

    /**
     * Get the size of the top margin.
     * @return Value of top margin
     */
    public int getMarginT(){
        return MarginT;
    }
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Encoding">
    
    /**
     * Set the encoding of the text.
     * @param encoding An encoding value
     */
    public void setEncoding(int encoding){
        Encoding = encoding;
    }

    /**
     * Get the encoding of the text.
     * @return An encoding value
     */
    public int getEncoding(){
        return Encoding;
    }
    
    //</editor-fold>
    
    //</editor-fold>

    @Override
    public String toString() {
        return Name;
    }
    
    public static String toAssStyleLine(AssStyle style){        
        return "Style: "
                + style.getName() + ","
                + style.getFontname() + "," 
                + Integer.toString((int)Math.ceil(style.getFontsize())) + ","
                + "&H" + style.getTextColorHex(true) + ","
                + "&H" + style.getKaraokeColorHex(true) + ","
                + "&H" + style.getBordColorHex(true) + ","
                + "&H" + style.getShadColorHex(true) + ","
                + (style.isBold() == true ? "1" : "0") + ","
                + (style.isItalic() == true ? "1" : "0") + ","
                + (style.isUnderline() == true ? "1" : "0") + ","
                + (style.isStrikeout() == true ? "1" : "0") + ","
                + Integer.toString((int)Math.ceil(style.getScaleX())) + ","
                + Integer.toString((int)Math.ceil(style.getScaleY())) + ","
                + Float.toString(style.getSpacing()) + ","
                + Float.toString(style.getAngle()) + ","
                + Integer.toString(style.getBorderStyle()) + ","
                + Integer.toString((int) Math.round(style.getOutline())) + ","
                + Integer.toString((int) Math.round(style.getShadow())) + ","
                + Integer.toString(style.getAlignment()) + ","
                + Integer.toString(style.getMarginL()) + ","
                + Integer.toString(style.getMarginR()) + ","
                + Integer.toString(style.getMarginV()) + ","
                + Integer.toString(style.getEncoding());
    }
    
    public static AssStyle createFromSSA(String SSA){
        AssStyle style = new AssStyle();
        String[] array = SSA.split(",");
        // 00 - Name
        style.setName(array[0].substring("Style: ".length()));
        // 01 - Fontname
        style.setFontname(array[1]);
        // 02 - Fontsize
        style.setFontsize(Integer.parseInt(array[2]));
        // 03 - PrimaryColour (in integer >> translated to BBGGRR)
        style.PrimaryColour = Integer.toString(Integer.parseInt(array[3]), 16);
        // 04 - SecondaryColour (in integer >> translated to BBGGRR)
        style.SecondaryColour = Integer.toString(Integer.parseInt(array[4]), 16);
        // 05 - TertiaryColour (dropped)
        // 06 - BackColour (in integer >> translated to BBGGRR)
        style.BackColour = Integer.toString(Integer.parseInt(array[6]), 16);
        // 07 - Bold
        style.setBold(array[7].contains("1") | array[7].contains("-1"));
        // 08 - Italic
        style.setItalic(array[8].contains("1") | array[8].contains("-1"));
        // XX - Underline (dropped)
        // XX - StrikeOut (dropped)
        // XX - ScaleX (dropped)
        // XX - ScaleY (dropped)
        // XX - Spacing (dropped)
        // XX - Angle (dropped)
        // 09 - BorderStyle
        style.setBorderStyle(Integer.parseInt(array[9]));
        // 10 - Outline
        style.setOutline(Float.parseFloat(array[10]));
        // 11 - Shadow
        style.setShadow(Float.parseFloat(array[11]));
        // 12 - Alignment Legacy
        // (bottom: 1-2-3, top: 5-6-7, middle: 9-10-11)
        // (Left: 1-5-9 Center: 2-6-10 Right: 3-7-11)
        int legacy = Integer.parseInt(array[12]), numpad = 2;
        switch(legacy){
            case 1 -> {numpad = 1;}
            case 2 -> {numpad = 2;}
            case 3 -> {numpad = 3;}
            case 5 -> {numpad = 7;}
            case 6 -> {numpad = 8;}
            case 7 -> {numpad = 9;}
            case 9 -> {numpad = 4;}
            case 10 -> {numpad = 5;}
            case 11 -> {numpad = 6;}
        }
        style.setAlignment(numpad);
        // 13 - MarginL
        style.setMarginL(Integer.parseInt(array[13]));
        // 14 - MarginR
        style.setMarginR(Integer.parseInt(array[14]));
        // 15 - MarginV
        style.setMarginV(Integer.parseInt(array[15]));
        // 16 - AlphaLevel (dropped)
        // 17 - Encoding
        style.setEncoding(Integer.parseInt(array[17]));
        return style;
    }
    
    /**
     * Rassemble tous les tags ASS (et BSS qui ne sont pas ASS)
     * @return map that contains tags
     */
    public Map<String, Object> getTagsFromStyle(){
        Map<String, Object> tags = new HashMap<>();
        
        // Fontname
        tags.put("\\fn", getFontname());
        // Fontsize
        tags.put("\\fs", getFontsize());
        // TextColor
        tags.put("\\1c", getTextColor());
        tags.put("\\1a", Integer.valueOf(getTextAlphaHex(), 16));
        // KaraColor
        tags.put("\\2c", getKaraokeColor());
        tags.put("\\2a", Integer.valueOf(getKaraokeAlphaHex(), 16));
        // OutlineColor
        tags.put("\\3c", getBordColor());
        tags.put("\\3a", Integer.valueOf(getBordAlphaHex(), 16));
        // ShadowColor
        tags.put("\\4c", getShadColor());
        tags.put("\\4a", Integer.valueOf(getShadAlphaHex(), 16));
        // Bold
        tags.put("\\b", isBold());
        // Italic
        tags.put("\\i", isItalic());
        // Underline
        tags.put("\\u", isUnderline());
        // Strikeout
        tags.put("\\s", isStrikeout());
        // Scale X
        tags.put("\\fscx", getScaleX());
        // Scale Y
        tags.put("\\fcsy", getScaleY());
        // Spacing
        tags.put("\\fsp", getSpacing());
        // Angle (Z axis)
        tags.put("\\frz", getAngle());
        // BorderStyle (non-ASS, custom to work with methods)
        tags.put("\\bs", getBorderStyle());
        // Outline
        tags.put("\\bord", getOutline());
        // Shadow
        tags.put("\\shad", getShadow());
        // Alignment
        tags.put("\\an", getAlignment());
        // MarginL (non-ASS, custom to work with methods)
        tags.put("\\ml", getMarginL());
        // MarginR (non-ASS, custom to work with methods)
        tags.put("\\mr", getMarginR());
        // MarginV (non-ASS, custom to work with methods)
        tags.put("\\mv", getMarginV());
        // Encoding (maybe the most suck thing in 2022)
        tags.put("\\fe", getEncoding());
            
        return tags;
    }
}
