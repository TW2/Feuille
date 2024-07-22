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
package org.wingate.feuille.util;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum DrawColor {
	
    //Colors from http://www.rapidtables.com/web/color/RGB_Color.htm
    marron("Marron","800000",128,0,0),
    dark_red("Dark red","8B0000",139,0,0),
    brown("Brown","A52A2A",165,42,42),
    firebrick("Firebrick","B22222",178,34,34),
    crimson("Crimson","DC143C",220,20,60),
    red("Red","FF0000",255,0,0),
    tomato("Tomato","FF6347",255,99,71),
    coral("Coral","FF7F50",255,127,80),
    indian_red("Indian red","CD5C5C",205,92,92),
    light_coral("Light coral","F08080",240,128,128),
    dark_salmon("Dark salmon","E9967A",233,150,122),
    salmon("Salmon","FA8072",250,128,114),
    light_salmon("Light salmon","FFA07A",255,160,122),
    orange_red("Orange red","FF4500",255,69,0),
    dark_orange("Dark orange","FF8C00",255,140,0),
    orange("Orange","FFA500",255,165,0),
    gold("Gold","FFD700",255,215,0),
    dark_golden_rod("Dark golden rod","B8860B",184,134,11),
    golden_rod("Golden rod","DAA520",218,165,32),
    pale_golden_rod("Pale golden rod","EEE8AA",238,232,170),
    dark_khaki("Dark khaki","BDB76B",189,183,107),
    khaki("Khaki","F0E68C",240,230,140),
    olive("Olive","808000",128,128,0),
    yellow("Yellow","FFFF00",255,255,0),
    yellow_green("Yellow green","9ACD32",154,205,50),
    dark_olive_green("Dark olive green","556B2F",85,107,47),
    olive_drab("Olive drab","6B8E23",107,142,35),
    lawn_green("Lawn green","7CFC00",124,252,0),
    chartreuse("Chartreuse","7FFF00",127,255,0),
    green_yellow("Green yellow","ADFF2F",173,255,47),
    dark_green("Dark green","006400",0,100,0),
    green("Green","008000",0,128,0),
    forest_green("Forest green","228B22",34,139,34),
    lime("Lime","00FF00",0,255,0),
    lime_green("Lime green","32CD32",50,205,50),
    light_green("Light green","90EE90",144,238,144),
    pale_green("Pale green","98FB98",152,251,152),
    dark_sea_green("Dark sea green","8FBC8F",143,188,143),
    medium_spring_green("Medium spring green","00FA9A",0,250,154),
    spring_green("Spring green","00FF7F",0,255,127),
    sea_green("Sea green","2E8B57",46,139,87),
    medium_aqua_marine("Medium aqua marine","66CDAA",102,205,170),
    medium_sea_green("Medium sea green","3CB371",60,179,113),
    light_sea_green("Light sea green","20B2AA",32,178,170),
    dark_slate_gray("Dark slate gray","2F4F4F",47,79,79),
    teal("Teal","008080",0,128,128),
    dark_cyan("Dark cyan","008B8B",0,139,139),
    aqua("Aqua","00FFFF",0,255,255),
    cyan("Cyan","00FFFF",0,255,255),
    light_cyan("Light cyan","E0FFFF",224,255,255),
    dark_turquoise("Dark turquoise","00CED1",0,206,209),
    turquoise("Turquoise","40E0D0",64,224,208),
    medium_turquoise("Medium turquoise","48D1CC",72,209,204),
    pale_turquoise("Pale turquoise","AFEEEE",175,238,238),
    aqua_marine("Aqua marine","7FFFD4",127,255,212),
    powder_blue("Powder blue","B0E0E6",176,224,230),
    cadet_blue("Cadet blue","5F9EA0",95,158,160),
    steel_blue("Steel blue","4682B4",70,130,180),
    corn_flower_blue("Corn flower blue","6495ED",100,149,237),
    deep_sky_blue("Deep sky blue","00BFFF",0,191,255),
    dodger_blue("Dodger blue","1E90FF",30,144,255),
    light_blue("Light blue","ADD8E6",173,216,230),
    sky_blue("Sky blue","87CEEB",135,206,235),
    light_sky_blue("Light sky blue","87CEFA",135,206,250),
    midnight_blue("Midnight blue","191970",25,25,112),
    navy("Navy","000080",0,0,128),
    dark_blue("Dark blue","00008B",0,0,139),
    medium_blue("Medium blue","0000CD",0,0,205),
    blue("Blue","0000FF",0,0,255),
    royal_blue("Royal blue","4169E1",65,105,225),
    blue_violet("Blue violet","8A2BE2",138,43,226),
    indigo("Indigo","4B0082",75,0,130),
    dark_slate_blue("Dark slate blue","483D8B",72,61,139),
    slate_blue("Slate blue","6A5ACD",106,90,205),
    medium_slate_blue("Medium slate blue","7B68EE",123,104,238),
    medium_purple("Medium purple","9370DB",147,112,219),
    dark_magenta("Dark magenta","8B008B",139,0,139),
    dark_violet("Dark violet","9400D3",148,0,211),
    dark_orchid("Dark orchid","9932CC",153,50,204),
    medium_orchid("Medium orchid","BA55D3",186,85,211),
    purple("Purple","800080",128,0,128),
    thistle("Thistle","D8BFD8",216,191,216),
    plum("Plum","DDA0DD",221,160,221),
    violet("Violet","EE82EE",238,130,238),
    magenta("Magenta","FF00FF",255,0,255),
    orchid("Orchid","DA70D6",218,112,214),
    medium_violet_red("Medium violet red","C71585",199,21,133),
    pale_violet_red("Pale violet red","DB7093",219,112,147),
    deep_pink("Deep pink","FF1493",255,20,147),
    hot_pink("Hot pink","FF69B4",255,105,180),
    light_pink("Light pink","FFB6C1",255,182,193),
    pink("Pink","FFC0CB",255,192,203),
    antique_white("Antique white","FAEBD7",250,235,215),
    beige("Beige","F5F5DC",245,245,220),
    bisque("Bisque","FFE4C4",255,228,196),
    blanched_almond("Blanched almond","FFEBCD",255,235,205),
    wheat("Wheat","F5DEB3",245,222,179),
    corn_silk("Corn silk","FFF8DC",255,248,220),
    lemon_chiffon("Lemon chiffon","FFFACD",255,250,205),
    light_golden_rod_yellow("Light golden rod yellow","FAFAD2",250,250,210),
    light_yellow("Light yellow","FFFFE0",255,255,224),
    saddle_brown("Saddle brown","8B4513",139,69,19),
    sienna("Sienna","A0522D",160,82,45),
    chocolate("Chocolate","D2691E",210,105,30),
    peru("Peru","CD853F",205,133,63),
    sandy_brown("Sandy brown","F4A460",244,164,96),
    burly_wood("Burly wood","DEB887",222,184,135),
    tan("Tan","D2B48C",210,180,140),
    rosy_brown("Rosy brown","BC8F8F",188,143,143),
    moccasin("Moccasin","FFE4B5",255,228,181),
    navajo_white("Navajo white","FFDEAD",255,222,173),
    peach_puff("Peach puff","FFDAB9",255,218,185),
    misty_rose("Misty rose","FFE4E1",255,228,225),
    lavender_blush("Lavender blush","FFF0F5",255,240,245),
    linen("Linen","FAF0E6",250,240,230),
    old_lace("Old lace","FDF5E6",253,245,230),
    papaya_whip("Papaya whip","FFEFD5",255,239,213),
    sea_shell("Sea shell","FFF5EE",255,245,238),
    mint_cream("Mint cream","F5FFFA",245,255,250),
    slate_gray("Slate gray","708090",112,128,144),
    light_slate_gray("Light slate gray","778899",119,136,153),
    light_steel_blue("Light steel blue","B0C4DE",176,196,222),
    lavender("Lavender","E6E6FA",230,230,250),
    floral_white("Floral white","FFFAF0",255,250,240),
    alice_blue("Alice blue","F0F8FF",240,248,255),
    ghost_white("Ghost white","F8F8FF",248,248,255),
    honeydew("Honeydew","F0FFF0",240,255,240),
    ivory("Ivory","FFFFF0",255,255,240),
    azure("Azure","F0FFFF",240,255,255),
    snow("Snow","FFFAFA",255,250,250),
    black("Black","000000",0,0,0),
    dim_gray("Dim gray","696969",105,105,105),
    gray("Gray","808080",128,128,128),
    dark_gray("Dark gray","A9A9A9",169,169,169),
    silver("Silver","C0C0C0",192,192,192),
    light_gray("Light gray","D3D3D3",211,211,211),
    gainsboro("Gainsboro","DCDCDC",220,220,220),
    white_smoke("White smoke","F5F5F5",245,245,245),
    white("White","FFFFFF",255,255,255);

    String hex;
    int r;
    int g;
    int b;
    String name;

    DrawColor(String name, String hex, int r, int g, int b) {
            this.name = name;
            this.hex = hex;
            this.r = r;
            this.g = g;
            this.b = b;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
            return getColor(1.0f);
    }

    public Color getColor(float alpha) {
            return new Color(convert(r), convert(g), convert(b), alpha);
    }

    public String getHTML() {
            return "#" + hex;
    }

    public int getRed() {
            return r;
    }

    public int getGreen() {
            return g;
    }

    public int getBlue() {
            return b;
    }

    private float convert(int x) {
            float MAX = 255f;
            float VALUE = (float)x;
            return VALUE / MAX;
    }
    
    public static Color getClosest(Color c){
        return getClosest(c, 1f);
    }
    
    public static Color getClosest(Color c, float alpha){
        Map<DrawColor, Double> map = new HashMap<>();
        
        for(DrawColor x : values()){
            map.put(x, getColorDistance(c, x.getColor()));
        }
        
        DrawColor d = black;
        double distance = Double.MAX_VALUE;
        for(Map.Entry<DrawColor, Double> entry : map.entrySet()){
            if(entry.getValue() <= distance){
                d = entry.getKey();
                distance = Double.min(distance, entry.getValue());
            }
        }
        
        return d.getColor(alpha);
    }
    
    /**
     * https://stackoverflow.com/questions/6334311/whats-the-best-way-to-round-a-color-object-to-the-nearest-color-constant
     * @param c1
     * @param c2
     * @return 
     */
    private static double getColorDistance(Color c1, Color c2){
        int red1 = c1.getRed();
        int red2 = c2.getRed();
        int rmean = (red1 + red2) >> 1;
        int r = red1 - red2;
        int g = c1.getGreen() - c2.getGreen();
        int b = c1.getBlue() - c2.getBlue();
        
        return Math.sqrt((((512+rmean)*r*r)>>8) + 4*g*g + (((767-rmean)*b*b)>>8));
    }
}