package feuille.util;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public enum FFMpegColor {
    AliceBlue("AliceBlue", 0xF0F8FF),
    AntiqueWhite("AntiqueWhite", 0xFAEBD7),
    Aqua("Aqua", 0x00FFFF),
    Aquamarine("Aquamarine", 0x7FFFD4),
    Azure("Azure", 0xF0FFFF),
    Beige("Beige", 0xF5F5DC),
    Bisque("Bisque", 0xFFE4C4),
    Black("Black", 0x000000),
    BlanchedAlmond("BlanchedAlmond", 0xFFEBCD),
    Blue("Blue", 0x0000FF),
    BlueViolet("BlueViolet", 0x8A2BE2),
    Brown("Brown", 0xA52A2A),
    BurlyWood("BurlyWood", 0xDEB887),
    CadetBlue("CadetBlue", 0x5F9EA0),
    Chartreuse("Chartreuse", 0x7FFF00),
    Chocolate("Chocolate", 0xD2691E),
    Coral("Coral", 0xFF7F50),
    CornFlowerBlue("CornflowerBlue", 0x6495ED),
    Cornsilk("Cornsilk", 0xFFF8DC),
    Crimson("Crimson", 0xDC143C),
    Cyan("Cyan", 0x00FFFF),
    DarkBlue("DarkBlue", 0x00008B),
    DarkCyan("DarkCyan", 0x008B8B),
    DarkGoldenRod("DarkGoldenRod", 0xB8860B),
    DarkGray("DarkGray", 0xA9A9A9),
    DarkGreen("DarkGreen", 0x006400),
    DarkKhaki("DarkKhaki", 0xBDB76B),
    DarkMagenta("DarkMagenta", 0x8B008B),
    DarkOliveGreen("DarkOliveGreen", 0x556B2F),
    DarkOrange("Darkorange", 0xFF8C00),
    DarkOrchid("DarkOrchid", 0x9932CC),
    DarkRed("DarkRed", 0x8B0000),
    DarkSalmon("DarkSalmon", 0xE9967A),
    DarkSeaGreen("DarkSeaGreen", 0x8FBC8F),
    DarkSlateBlue("DarkSlateBlue", 0x483D8B),
    DarkSlateGray("DarkSlateGray", 0x2F4F4F),
    DarkTurquoise("DarkTurquoise", 0x00CED1),
    DarkViolet("DarkViolet", 0x9400D3),
    DeepPink("DeepPink", 0xFF1493),
    DeepSkyBlue("DeepSkyBlue", 0x00BFFF),
    DimGray("DimGray", 0x696969),
    DodgerBlue("DodgerBlue", 0x1E90FF),
    FireBrick("FireBrick", 0xB22222),
    FloralWhite("FloralWhite", 0xFFFAF0),
    ForestGreen("ForestGreen", 0x228B22),
    Fuchsia("Fuchsia", 0xFF00FF),
    Gainsboro("Gainsboro", 0xDCDCDC),
    GhostWhite("GhostWhite", 0xF8F8FF),
    Gold("Gold", 0xFFD700),
    GoldenRod("GoldenRod", 0xDAA520),
    Gray("Gray", 0x808080),
    Green("Green", 0x008000),
    GreenYellow("GreenYellow", 0xADFF2F),
    HoneyDew("HoneyDew", 0xF0FFF0),
    HotPink("HotPink", 0xFF69B4),
    IndianRed("IndianRed", 0xCD5C5C),
    Indigo("Indigo", 0x4B0082),
    Ivory("Ivory", 0xFFFFF0),
    Khaki("Khaki", 0xF0E68C),
    Lavender("Lavender", 0xE6E6FA),
    LavenderBlush("LavenderBlush", 0xFFF0F5),
    LawnGreen("LawnGreen", 0x7CFC00),
    LemonChiffon("LemonChiffon", 0xFFFACD),
    LightBlue("LightBlue", 0xADD8E6),
    LightCoral("LightCoral", 0xF08080),
    LightCyan("LightCyan", 0xE0FFFF),
    LightGoldenRodYellow("LightGoldenRodYellow", 0xFAFAD2),
    LightGreen("LightGreen", 0x90EE90),
    LightGrey("LightGrey", 0xD3D3D3),
    LightPink("LightPink", 0xFFB6C1),
    LightSalmon("LightSalmon", 0xFFA07A),
    LightSeaGreen("LightSeaGreen", 0x20B2AA),
    LightSkyBlue("LightSkyBlue", 0x87CEFA),
    LightSlateGray("LightSlateGray", 0x778899),
    LightSteelBlue("LightSteelBlue", 0xB0C4DE),
    LightYellow("LightYellow", 0xFFFFE0),
    Lime("Lime", 0x00FF00),
    LimeGreen("LimeGreen", 0x32CD32),
    Linen("Linen", 0xFAF0E6),
    Magenta("Magenta", 0xFF00FF),
    Maroon("Maroon", 0x800000),
    MediumAquaMarine("MediumAquaMarine", 0x66CDAA),
    MediumBlue("MediumBlue", 0x0000CD),
    MediumOrchid("MediumOrchid", 0xBA55D3),
    MediumPurple("MediumPurple", 0x9370D8),
    MediumSeaGreen("MediumSeaGreen", 0x3CB371),
    MediumSlateBlue("MediumSlateBlue", 0x7B68EE),
    MediumSpringGreen("MediumSpringGreen", 0x00FA9A),
    MediumTurquoise("MediumTurquoise", 0x48D1CC),
    MediumVioletRed("MediumVioletRed", 0xC71585),
    MidnightBlue("MidnightBlue", 0x191970),
    MintCream("MintCream", 0xF5FFFA),
    MistyRose("MistyRose", 0xFFE4E1),
    Moccasin("Moccasin", 0xFFE4B5),
    NavajoWhite("NavajoWhite", 0xFFDEAD),
    Navy("Navy", 0x000080),
    OldLace("OldLace", 0xFDF5E6),
    Olive("Olive", 0x808000),
    OliveDrab("OliveDrab", 0x6B8E23),
    Orange("Orange", 0xFFA500),
    OrangeRed("OrangeRed", 0xFF4500),
    Orchid("Orchid", 0xDA70D6),
    PaleGoldenRod("PaleGoldenRod", 0xEEE8AA),
    PaleGreen("PaleGreen", 0x98FB98),
    PaleTurquoise("PaleTurquoise", 0xAFEEEE),
    PaleVioletRed("PaleVioletRed", 0xD87093),
    PapayaWhip("PapayaWhip", 0xFFEFD5),
    PeachPuff("PeachPuff", 0xFFDAB9),
    Peru("Peru", 0xCD853F),
    Pink("Pink", 0xFFC0CB),
    Plum("Plum", 0xDDA0DD),
    PowderBlue("PowderBlue", 0xB0E0E6),
    Purple("Purple", 0x800080),
    Red("Red", 0xFF0000),
    RosyBrown("RosyBrown", 0xBC8F8F),
    RoyalBlue("RoyalBlue", 0x4169E1),
    SaddleBrown("SaddleBrown", 0x8B4513),
    Salmon("Salmon", 0xFA8072),
    SandyBrown("SandyBrown", 0xF4A460),
    SeaGreen("SeaGreen", 0x2E8B57),
    SeaShell("SeaShell", 0xFFF5EE),
    Sienna("Sienna", 0xA0522D),
    Silver("Silver", 0xC0C0C0),
    SkyBlue("SkyBlue", 0x87CEEB),
    SlateBlue("SlateBlue", 0x6A5ACD),
    SlateGray("SlateGray", 0x708090),
    Snow("Snow", 0xFFFAFA),
    SpringGreen("SpringGreen", 0x00FF7F),
    SteelBlue("SteelBlue", 0x4682B4),
    Tan("Tan", 0xD2B48C),
    Teal("Teal", 0x008080),
    Thistle("Thistle", 0xD8BFD8),
    Tomato("Tomato", 0xFF6347),
    Turquoise("Turquoise", 0x40E0D0),
    Violet("Violet", 0xEE82EE),
    Wheat("Wheat", 0xF5DEB3),
    White("White", 0xFFFFFF),
    WhiteSmoke("WhiteSmoke", 0xF5F5F5),
    Yellow("Yellow", 0xFFFF00),
    YellowGreen("YellowGreen", 0x9ACD32);

    final String name;
    final int hexadecimal;

    FFMpegColor(String name, int hexadecimal){
        this.name = name;
        this.hexadecimal = hexadecimal;
    }

    public String getName() {
        return name;
    }

    public int getHexadecimal() {
        return hexadecimal;
    }

    public List<FFMpegColor> getOrderedColors(){
        List<FFMpegColor> colors = Arrays.asList(values());
        colors.sort(Comparator.comparingInt(FFMpegColor::getHexadecimal));
        return colors;
    }

    public static FFMpegColor get(String name){
        for(FFMpegColor c : values()){
            if(c.name.equalsIgnoreCase(name)){
                return c;
            }
        }
        return null;
    }

    public static FFMpegColor get(int value){
        for(FFMpegColor c : values()){
            if(c.hexadecimal == value){
                return c;
            }
        }
        return null;
    }
}
