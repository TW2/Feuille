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

import java.awt.Color;

/**
 *
 * @author util2
 */
public class AssStyle implements Cloneable {
    private String name;
    private AssFont assFont;
    private AssColor textColor;
    private AssColor karaokeColor;
    private AssColor outlineColor;
    private AssColor shadowColor;
    private float scaleX;
    private float scaleY;
    private float spacing;
    private float angleZ;
    private int borderStyle;
    private float outline;
    private float shadow;
    private AssAlignment alignment;
    private int marginL;
    private int marginR;
    private int marginV;
    private int encoding;

    public AssStyle(String name, AssFont assFont, AssColor textColor, AssColor karaokeColor, AssColor outlineColor, AssColor shadowColor, float scaleX, float scaleY, float spacing, float angleZ, int borderStyle, float outline, float shadow, AssAlignment alignment, int marginL, int marginR, int marginV, int encoding) {
        this.name = name;
        this.assFont = assFont;
        this.textColor = textColor;
        this.karaokeColor = karaokeColor;
        this.outlineColor = outlineColor;
        this.shadowColor = shadowColor;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.spacing = spacing;
        this.angleZ = angleZ;
        this.borderStyle = borderStyle;
        this.outline = outline;
        this.shadow = shadow;
        this.alignment = alignment;
        this.marginL = marginL;
        this.marginR = marginR;
        this.marginV = marginV;
        this.encoding = encoding;
    }

    public AssStyle() {
        this(
                "Default", // Name
                new AssFont("Arial"), // Font
                new AssColor(Color.white), // Text color
                new AssColor(Color.red), // Karaoke color
                new AssColor(Color.black), // Outline color
                new AssColor(Color.black), // Shadow color
                1f, // Scale X
                1f, // Scale Y
                0f, // Spacing
                0f, // Angle Z
                1, // BorderStyle (1 normal, 3 rectangle)
                2f, // Outline
                2f, // Shadow
                new AssAlignment(), // ASS Alignment
                0, // Margin Left
                0, // Margin Right
                0, // Margin Vertical
                1 // Encoding
        );
    }
    
    public AssStyle(String rawline) throws AssColorException{
        String[] t = rawline.split(",");
        
        AssFont font = new AssFont(
                t[1], // Name
                Float.parseFloat(t[2]), // Size
                t[7].equalsIgnoreCase("-1"), // Bold
                t[8].equalsIgnoreCase("-1"), // Italic
                t[9].equalsIgnoreCase("-1"), // Underline
                t[10].equalsIgnoreCase("-1") // Strikeout
        );
        
        name            = t[0].substring(t[0].indexOf(" ") + 1); // Name
        assFont         = font; // Font
        textColor       = AssColor.fromScheme(t[3], AssColor.Scheme.ABGR); // Text color
        karaokeColor    = AssColor.fromScheme(t[4], AssColor.Scheme.ABGR); // Karaoke color
        outlineColor    = AssColor.fromScheme(t[5], AssColor.Scheme.ABGR); // Outline color
        shadowColor     = AssColor.fromScheme(t[6], AssColor.Scheme.ABGR); // Shadow color
        scaleX          = Float.parseFloat(t[11]); // Scale X
        scaleY          = Float.parseFloat(t[12]); // Scale Y
        spacing         = Float.parseFloat(t[13]); // Spacing
        angleZ          = Float.parseFloat(t[14]); // Angle Z
        borderStyle     = t[15].equalsIgnoreCase("1") ? 1 : 3; // BorderStyle (1 normal, 3 rectangle)
        outline         = Float.parseFloat(t[16]); // Outline
        shadow          = Float.parseFloat(t[17]); // Shadow
        alignment       = new AssAlignment(Integer.parseInt(t[18])); // ASS Alignment
        marginL         = Integer.parseInt(t[19]); // Margin Left
        marginR         = Integer.parseInt(t[20]); // Margin Right
        marginV         = Integer.parseInt(t[21]); // Margin Vertical
        encoding        = Integer.parseInt(t[22]); // Encoding
    }
    
    public String toRawLine() throws AssColorException{
        StringBuilder line = new StringBuilder("Style: ");
        line = line.append(getName()).append(",");
        line = line.append(getAssFont().getName()).append(",");
        line = line.append(Math.round(getAssFont().getSize())).append(",");
        line = line.append(AssColor.withScheme(getTextColor().getColor(), AssColor.Scheme.ABGR)).append(",");
        line = line.append(AssColor.withScheme(getKaraokeColor().getColor(), AssColor.Scheme.ABGR)).append(",");
        line = line.append(AssColor.withScheme(getOutlineColor().getColor(), AssColor.Scheme.ABGR)).append(",");
        line = line.append(AssColor.withScheme(getShadowColor().getColor(), AssColor.Scheme.ABGR)).append(",");
        line = line.append((getAssFont().isBold() ? "-1" : "0")).append(",");
        line = line.append((getAssFont().isItalic()? "-1" : "0")).append(",");
        line = line.append((getAssFont().isUnderline()? "-1" : "0")).append(",");
        line = line.append((getAssFont().isStrikeout()? "-1" : "0")).append(",");
        line = line.append(getScaleX()).append(",");
        line = line.append(getScaleY()).append(",");
        line = line.append(getSpacing()).append(",");
        line = line.append(getAngleZ()).append(",");
        line = line.append(getBorderStyle()).append(",");
        line = line.append(getOutline()).append(",");
        line = line.append(getShadow()).append(",");
        line = line.append(getAlignment().getNumber()).append(",");
        line = line.append(getMarginL()).append(",");
        line = line.append(getMarginR()).append(",");
        line = line.append(getMarginV()).append(",");
        line = line.append(getEncoding());
        
        return line.toString();
    }

    @Override
    public AssStyle clone() throws CloneNotSupportedException {
        AssStyle style = (AssStyle) super.clone();
        style.setAssFont(assFont.clone());
        style.setTextColor(textColor.clone());
        style.setKaraokeColor(karaokeColor.clone());
        style.setOutlineColor(outlineColor.clone());
        style.setShadowColor(shadowColor.clone());
        style.setAlignment(alignment.clone());
        return style;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AssFont getAssFont() {
        return assFont;
    }

    public void setAssFont(AssFont assFont) {
        this.assFont = assFont;
    }

    public AssColor getTextColor() {
        return textColor;
    }

    public void setTextColor(AssColor textColor) {
        this.textColor = textColor;
    }

    public AssColor getKaraokeColor() {
        return karaokeColor;
    }

    public void setKaraokeColor(AssColor karaokeColor) {
        this.karaokeColor = karaokeColor;
    }

    public AssColor getOutlineColor() {
        return outlineColor;
    }

    public void setOutlineColor(AssColor outlineColor) {
        this.outlineColor = outlineColor;
    }

    public AssColor getShadowColor() {
        return shadowColor;
    }

    public void setShadowColor(AssColor shadowColor) {
        this.shadowColor = shadowColor;
    }

    public float getScaleX() {
        return scaleX;
    }

    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
    }

    public float getScaleY() {
        return scaleY;
    }

    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
    }

    public float getSpacing() {
        return spacing;
    }

    public void setSpacing(float spacing) {
        this.spacing = spacing;
    }

    public float getAngleZ() {
        return angleZ;
    }

    public void setAngleZ(float angleZ) {
        this.angleZ = angleZ;
    }

    public int getBorderStyle() {
        return borderStyle;
    }

    public void setBorderStyle(int borderStyle) {
        this.borderStyle = borderStyle;
    }

    public float getOutline() {
        return outline;
    }

    public void setOutline(float outline) {
        this.outline = outline;
    }

    public float getShadow() {
        return shadow;
    }

    public void setShadow(float shadow) {
        this.shadow = shadow;
    }

    public AssAlignment getAlignment() {
        return alignment;
    }

    public void setAlignment(AssAlignment alignment) {
        this.alignment = alignment;
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

    public int getEncoding() {
        return encoding;
    }

    public void setEncoding(int encoding) {
        this.encoding = encoding;
    }

    @Override
    public String toString() {
        return getName();
    }
    
}
