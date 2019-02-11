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
package feuille.io;

import java.awt.Color;
import java.awt.Font;

/**
 *
 * @author util2
 */
public class Style {
    
    private static String Name = "Default";
    private static String Fontname = "Arial";
    private static double Fontsize = 28;
    private static String PrimaryColour = "0000FFFF";
    private static String SecondaryColour = "0000FFFF";
    private static String OutlineColor = "00000000";
    private static String BackColour = "00000000";
    private static boolean Bold = false;
    private static boolean Italic = false;
    private static boolean Underline = false;
    private static boolean Strikeout = false;
    private static double ScaleX = 100;
    private static double ScaleY = 100;
    private static double Spacing = 0;
    private static double Angle = 0;
    private static int BorderStyle = 1;
    private static double Outline = 2;
    private static double Shadow = 0;
    private static int Alignment = 2;
    private static int MarginL = 10;
    private static int MarginR = 10;
    private static int MarginV = 10;
    private static int MarginB = 10;
    private static int MarginT = 10;
    //private static int AlphaLevel = 0;
    private static int Encoding = 0;
    //private static int RelativeTo = 0;

    public Style() {
        
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
    public static Style create(String ASS){
        Style style = new Style();
        String[] array = ASS.split(",");
        // 00 - Name
        style.setName(array[0].substring("Style: ".length()));
        // 01 - Fontname
        style.setFontname(array[1]);
        // 02 - Fontsize
        style.setFontsize(Double.parseDouble(array[2]));
        // 03 - PrimaryColour
        Style.PrimaryColour = array[3].substring(2);
        // 04 - SecondaryColour
        Style.SecondaryColour = array[4].substring(2);
        // 05 - OutlineColour
        Style.OutlineColor = array[5].substring(2);
        // 06 - BackColour
        Style.BackColour = array[6].substring(2);
        // 07 - Bold
        style.setBold(array[7].contains("1"));
        // 08 - Italic
        style.setItalic(array[8].contains("1"));
        // 09 - Underline
        style.setUnderline(array[9].contains("1"));
        // 10 - StrikeOut
        style.setStrikeout(array[10].contains("1"));
        // 11 - ScaleX
        style.setScaleX(Double.parseDouble(array[11]));
        // 12 - ScaleY
        style.setScaleY(Double.parseDouble(array[12]));
        // 13 - Spacing
        style.setSpacing(Double.parseDouble(array[13]));
        // 14 - Angle
        style.setAngle(Double.parseDouble(array[14]));
        // 15 - BorderStyle
        style.setBorderStyle(Integer.parseInt(array[15]));
        // 16 - Outline
        style.setOutline(Double.parseDouble(array[16]));
        // 17 - Shadow
        style.setShadow(Double.parseDouble(array[17]));
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
    
    public static Style getDefault(){
        return Style.create(Style.getStyle());
    }
    
    public static String getStyle(){
        return "Style: "
                + Name + ","
                + Fontname + "," 
                + Math.ceil(Fontsize) + ","
                + "&H" + PrimaryColour + ","
                + "&H" + SecondaryColour + ","
                + "&H" + OutlineColor + ","
                + "&H" + BackColour + ","
                + (Bold == true ? "1" : "0") + ","
                + (Italic == true ? "1" : "0") + ","
                + (Underline == true ? "1" : "0") + ","
                + (Strikeout == true ? "1" : "0") + ","
                + Math.ceil(ScaleX) + ","
                + Math.ceil(ScaleY) + ","
                + Math.ceil(Spacing) + ","
                + Math.ceil(Angle) + ","
                + Integer.toString(BorderStyle) + ","
                + Double.toString(Outline) + ","
                + Double.toString(Shadow) + ","
                + Integer.toString(Alignment) + ","
                + Integer.toString(MarginL) + ","
                + Integer.toString(MarginR) + ","
                + Integer.toString(MarginV) + ","
                + Integer.toString(Encoding);
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
        String sa = Integer.toString(color.getAlpha(), 16);
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
        return new Color(red, green, blue, alpha);
    }
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Karaoke color">

    /**
     * Set the karaoke color.
     * @param color A color
     */
    public void setKaraokeColor(Color color){
        String sa = Integer.toString(color.getAlpha(), 16);
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
        return new Color(red, green, blue, alpha);
    }
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Border (outline) color">

    /**
     * Set the border (outline) color.
     * @param color A color
     */
    public void setBordColor(Color color){
        String sa = Integer.toString(color.getAlpha(), 16);
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
        return new Color(red, green, blue, alpha);
    }
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Shadow color">

    /**
     * Set the shadow color.
     * @param color A color
     */
    public void setShadColor(Color color){
        String sa = Integer.toString(color.getAlpha(), 16);
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
        return new Color(red, green, blue, alpha);
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
    public void setScaleX(double scx){
        ScaleX = scx;
    }

    /**
     * Get the scale on X.
     * @return The scale of the x axis (\fscx) in percent (integer) where 0% is the minimum
     */
    public double getScaleX(){
        return ScaleX;
    }

    /**
     * Set the scale on Y.
     * @param scy The scale of the y axis (\fscy) in percent (integer) where 0% is the minimum
     */
    public void setScaleY(double scy){
        ScaleY = scy;
    }

    /**
     * Get the scale on Y.
     * @return The scale of the y axis (\fscy) in percent (integer) where 0% is the minimum
     */
    public double getScaleY(){
        return ScaleY;
    }
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Angle Z">
    
    /**
     * Set the angle on Z.
     * @param angle An angle (\frz) in degrees
     */
    public void setAngle(double angle){
        Angle = angle;
    }

    /**
     * Get the angle on Z.
     * @return An angle (\frz) in degrees
     */
    public double getAngle(){
        return Angle;
    }
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Spacing">

    /**
     * Set the spacing between letters.
     * @param spacing A spacing value
     */
    public void setSpacing(double spacing){
        Spacing = spacing;
    }

    /**
     * Get the spacing between letters.
     * @return A spacing value
     */
    public double getSpacing(){
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
    
}
