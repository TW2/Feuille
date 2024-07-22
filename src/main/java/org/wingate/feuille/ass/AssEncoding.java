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
package org.wingate.feuille.ass;

/**
 *
 * @author util2
 */
public enum AssEncoding {
    ANSI(0, "ANSI"),
    Default(1, "Default"),
    Symbol(2, "Symbol"),
    Mac(77, "Mac"),
    Shift_JIS(128, "Shift_JIS"),
    Hangeul(129, "Hangeul"),
    Johab(130, "Johab"),
    GB2312(134, "GB2312"),
    BIG5(136, "Chinese BIG5"),
    Greek(161, "Greek"),
    Turkish(162, "Turkish"),
    Vietnamese(163, "Vietnamese"),
    Hebrew(177, "Hebrew"),
    Arabic(178, "Arabic"),
    Baltic(186, "Baltic"),
    Russian(204, "Russian"),
    Thai(222, "Thai"),
    EastEuropean(238, "East European"),
    OEM(255, "OEM");
    
    
    int codepage;
    String name;
    
    private AssEncoding(int codepage, String name){
        this.codepage = codepage;
        this.name = name;
    }

    public int getCodepage() {
        return codepage;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("%d - %s", codepage, name);
    }
    
    public static AssEncoding get(Object value){
        AssEncoding enc = ANSI;
        
        for(AssEncoding encoding : values()){
            switch (value) {
                case Integer x -> {
                    if(x == encoding.getCodepage()){
                        enc = encoding;
                        break;
                    }
                }
                case String x -> {
                    if(x.equalsIgnoreCase(encoding.getName())){
                        enc = encoding;
                        break;
                    }
                }
                default -> {
                }
            }
        }
        
        return enc;
    }
}
