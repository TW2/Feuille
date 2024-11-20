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
public class AssColor implements Cloneable {
    private Color color;

    public AssColor(Color color, int assAlpha) {
        this.color = new Color(
                color.getRed(), // R
                color.getGreen(), // G
                color.getBlue(), // B
                255 - assAlpha  // A
        );
    }
    
    public AssColor(Color c){
        this(c, 0);
    }

    public AssColor() {
        this(Color.yellow);
    }
    
    @Override
    protected AssColor clone() throws CloneNotSupportedException {
        return (AssColor) super.clone();
    }
    
    public enum Scheme{
        BGR, ABGR, RGB, ARGB, BGRA, RGBA, HTML, ALPHA;
    }
    
    public static AssColor fromScheme(String code, Scheme scheme) throws AssColorException{
        AssColor c;
        if(code == null || code.isEmpty()) return new AssColor();
        code = code.replace("H", "");
        code = code.replace("&", "");
        code = code.replace("#", "");
        
        switch(scheme){
            case ABGR -> {
                if(!code.toLowerCase().matches("[a-f0-9]{8}")){
                    throw new AssColorException("Malformed code: Not ABGR");
                }
                    
                c = new AssColor(new Color(
                        Integer.parseInt(code.substring(6), 16),    // R
                        Integer.parseInt(code.substring(4, 6), 16), // G
                        Integer.parseInt(code.substring(2, 4), 16), // B
                        Integer.parseInt(code.substring(0, 2), 16)  // A
                ));
            }
            case BGRA -> {
                if(!code.toLowerCase().matches("[a-f0-9]{8}")){
                    throw new AssColorException("Malformed code: Not BGRA");
                }
                    
                c = new AssColor(new Color(                        
                        Integer.parseInt(code.substring(4, 6), 16), // R
                        Integer.parseInt(code.substring(2, 4), 16), // G
                        Integer.parseInt(code.substring(0, 2), 16), // B
                        Integer.parseInt(code.substring(6), 16)     // A
                ));
            }
            case BGR -> {
                if(!code.toLowerCase().matches("[a-f0-9]{6}")){
                    throw new AssColorException("Malformed code: Not BGR");
                }
                    
                c = new AssColor(new Color(                        
                        Integer.parseInt(code.substring(4), 16),    // R
                        Integer.parseInt(code.substring(2, 4), 16), // G
                        Integer.parseInt(code.substring(0, 2), 16), // B
                        255                                         // A
                ));
            }
            case ARGB -> {
                if(!code.toLowerCase().matches("[a-f0-9]{8}")){
                    throw new AssColorException("Malformed code: Not ARGB");
                }
                    
                c = new AssColor(new Color(                        
                        Integer.parseInt(code.substring(2, 4), 16), // R
                        Integer.parseInt(code.substring(4, 6), 16), // G
                        Integer.parseInt(code.substring(6), 16),    // B
                        Integer.parseInt(code.substring(0, 2), 16)  // A
                ));
            }
            case RGBA -> {
                if(!code.toLowerCase().matches("[a-f0-9]{8}")){
                    throw new AssColorException("Malformed code: Not RGBA");
                }
                    
                c = new AssColor(new Color(                        
                        Integer.parseInt(code.substring(0, 2), 16), // R
                        Integer.parseInt(code.substring(2, 4), 16), // G
                        Integer.parseInt(code.substring(4, 6), 16), // B
                        Integer.parseInt(code.substring(6), 16)     // A
                ));
            }
            case RGB, HTML -> {
                if(!code.toLowerCase().matches("[a-f0-9]{6}")){
                    throw new AssColorException("Malformed code: Not RGB or HTML 6 digits");
                }
                    
                c = new AssColor(new Color(                        
                        Integer.parseInt(code.substring(0, 2), 16), // R
                        Integer.parseInt(code.substring(2, 4), 16), // G
                        Integer.parseInt(code.substring(4), 16),    // B
                        255                                         // A
                ));
            }
            default -> { c = new AssColor(); }
        }
        
        return c;
    }
    
    public static String withScheme(Color c, Scheme scheme) throws AssColorException{
        String s;
        
        String a = Integer.toHexString(255 - c.getAlpha()); if(a.length() == 1) a = "0"+a;
        String r = Integer.toHexString(c.getRed()); if(r.length() == 1) r = "0"+r;
        String g = Integer.toHexString(c.getGreen()); if(g.length() == 1) g = "0"+g;
        String b = Integer.toHexString(c.getBlue()); if(b.length() == 1) b = "0"+b;
        
        switch (scheme) {
            case ABGR -> {
                s = "&H" + a + b + g + r; // Used in style
            }
            case BGR -> {
                s = "&H" + b + g + r + "&"; // Used in event for colors
            }
            case HTML -> {
                s = "#" + b + g + r; // Not used
            }
            case ALPHA -> {
                s = "&H" + a + "&"; // Used in event for alphas
            }
            default -> {
                throw new AssColorException("Unexpected error!");
            }
        }
        return s;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
