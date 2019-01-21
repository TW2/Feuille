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

/**
 *
 * @author util2
 */
public class AlphaBox {

    public AlphaBox() {
    }
    
    //==========================================================================
    // IO Methods
    //==========================================================================
    
    public static int hexaToInteger(String hexa){
        return Integer.parseInt(hexa, 16);
    }
    
    public static String integerToHexa(int value){
        return Integer.toHexString(value);
    }
    
    public static float hexaToPercent(String hexa){
        return Integer.parseInt(hexa, 16) / 255f / 100f;
    }
    
    public static String percentToHexa(float value){
        return integerToHexa(percentToInteger(value));
    }
    
    public static int percentToInteger(float percent){
        return Math.round(percent * 100f * 255f);
    }
    
    public static float integerToPercent(int value){
        return value / 255f / 100f;
    }
}
