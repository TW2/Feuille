/*
 * Copyright (C) 2019 util2
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
package feuille.util;

import java.awt.Color;

/**
 *
 * @author util2
 */
public class ColorBox {

    public ColorBox() {
        
    }
    
    //==========================================================================
    // IO methods
    //==========================================================================
    
    public static Color bgrToColor(String bgr){
        try{
            String red   = bgr.substring(4);
            String green = bgr.substring(2, 4);
            String blue  = bgr.substring(0, 2);

            int r = Integer.parseInt(red, 16);
            int g = Integer.parseInt(green, 16);
            int b = Integer.parseInt(blue, 16);
            
            return new Color(r, g, b);
        }catch(NullPointerException | IndexOutOfBoundsException e){
            return Color.black;
        }
    }
    
    public static Color rgbToColor(String rgb){
        try{
            String red   = rgb.substring(0, 2);
            String green = rgb.substring(2, 4);
            String blue  = rgb.substring(4);

            int r = Integer.parseInt(red, 16);
            int g = Integer.parseInt(green, 16);
            int b = Integer.parseInt(blue, 16);
            
            return new Color(r, g, b);
        }catch(NullPointerException | IndexOutOfBoundsException e){
            return Color.black;
        }
    }
    
    public static String colorToBgr(Color c){
        return Integer.toHexString(c.getBlue()) + Integer.toHexString(c.getGreen()) + Integer.toHexString(c.getRed());
    }
    
    public static String colorToRgb(Color c){
        return Integer.toHexString(c.getRed()) + Integer.toHexString(c.getGreen()) + Integer.toHexString(c.getBlue());
    }
    
    public static Color abgrToColor(String abgr){
        try{
            String alpha = abgr.substring(0, 2);
            String red   = abgr.substring(6);
            String green = abgr.substring(4, 6);
            String blue  = abgr.substring(2, 4);

            int a = Integer.parseInt(alpha, 16);
            int r = Integer.parseInt(red, 16);
            int g = Integer.parseInt(green, 16);
            int b = Integer.parseInt(blue, 16);
            
            return new Color(a, r, g, b);
        }catch(NullPointerException | IndexOutOfBoundsException e){
            return Color.black;
        }
    }
    
    public static Color argbToColor(String argb){
        try{
            String alpha = argb.substring(0, 2);
            String red   = argb.substring(2, 4);
            String green = argb.substring(4, 6);
            String blue  = argb.substring(6);

            int a = Integer.parseInt(alpha, 16);
            int r = Integer.parseInt(red, 16);
            int g = Integer.parseInt(green, 16);
            int b = Integer.parseInt(blue, 16);
            
            return new Color(a, r, g, b);
        }catch(NullPointerException | IndexOutOfBoundsException e){
            return Color.black;
        }
    }
    
    public static String colorToAbgr(Color c){
        return Integer.toHexString(c.getAlpha()) + Integer.toHexString(c.getBlue()) + Integer.toHexString(c.getGreen()) + Integer.toHexString(c.getRed());
    }
    
    public static String colorToArgb(Color c){
        return Integer.toHexString(c.getAlpha()) + Integer.toHexString(c.getRed()) + Integer.toHexString(c.getGreen()) + Integer.toHexString(c.getBlue());
    }
}
