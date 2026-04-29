package feuille.module.editor.assa.render;

import java.awt.*;
import java.util.*;
import java.util.List;

public enum Tag {
    Reset("Reset", "r", 1), // r
    Bold("Bold", "b", 1), // b
    Italic("Italic", "i", 1), // i
    Underline("Underline", "u", 1), // u
    StrikeOut("StrikeOut", "s", 1), // s
    FontName("FontName", "fn", 1), // fn
    FontSize("FontSize", "fs", 1), // fs
    PrimaryColor("PrimaryColor", "c", 1), // c
    TextColor("TextColor", "1c", 1), // 1c
    KaraokeColor("KaraokeColor", "2c", 1), // 2c
    OutlineColor("OutlineColor", "3c", 1), // 3c
    ShadowColor("ShadowColor", "4c", 1), // 4c
    Alpha("Alpha", "alpha", 1), // alpha
    TextAlpha("TextAlpha", "1a", 1), // 1a
    KaraokeAlpha("KaraokeAlpha", "2a", 1), // 2a
    OutlineAlpha("OutlineAlpha", "3a", 1), // 3a
    ShadowAlpha("ShadowAlpha", "4a", 1), // 4a
    Scale("Scale", "fsc", 1), // fsc
    ScaleX("ScaleX", "fscx", 1), // fsc(x)
    ScaleY("ScaleY", "fscy", 1), // fcs(y)
    Spacing("Spacing", "fsp", 1), // fsp
    Rotation("Rotation", "fr", 1), // fr
    RotationX("RotationX", "frx", 1), // fr(x)
    RotationY("RotationY", "fry", 1), // fr(y)
    RotationZ("RotationZ", "frz", 1), // fr(z)
    BorderStyle("BorderStyle", "bs", 1), // bs
    OutlineThickness("OutlineThickness", "bord", 1), // bord
    OutlineThicknessX("OutlineThicknessX", "xbord", 1), // (x)bord
    OutlineThicknessY("OutlineThicknessY", "ybord", 1), // (y)bord
    ShadowShift("ShadowShift", "shad", 1), // shad
    ShadowShiftX("ShadowShiftX", "xshad", 1), // (x)shad
    ShadowShiftY("ShadowShiftY", "yshad", 1), // (y)shad
    BlurEdge("BlurEdge", "be", 1), // be
    Blur("GaussianBlur", "blur", 1), // blur
    AlignmentLegacy("AlignmentLegacy", "a", 1), // a
    AlignmentNumPad("AlignmentNumPad", "an", 1), // an
    MarginL("MarginL", "ml", 1), // ml
    MarginR("MarginR", "mr", 1), // mr
    MarginV("MarginV", "mv", 1), // mv
    MarginT("MarginT", "mt", 1), // mt
    MarginB("MarginB", "mb", 1), // mb
    FontEncoding("FontEncoding", "fe", 1), // fe
    Origin("Origin", "org", 2), // org
    ShearX("ShearX", "fax", 1), // fa(x)
    ShearY("ShearY", "fay", 1), // fa(y)
    Karaoke("Karaoke", "k", 1), // k
    KaraokeFillLegacy("KaraokeFillLegacy", "K", 1), // K
    KaraokeFill("KaraokeFill", "kf", 1), // kf
    KaraokeOutline("KaraokeOutline", "ko", 1), // ko
    WrapStyle("WrapStyle", "q", 1), // q
    Position("Position", "pos", 2), // pos
    Movement("Movement", "move", 6), // move
    FadeSimple("FadeSimple", "fad", 2), // fad
    FadeComplex("FadeComplex", "fade", 7), // fade
    Animation("Animation", "t", 3), // t
    ClipRectangle("ClipRectangle", "clip", 4), // clip
    InvisibleClipRectangle("InvisibleClipRectangle", "iclip", 4), // (i)clip
    ClipDrawing("ClipDrawing", "clip", 2), // clip
    InvisibleClipDrawing("InvisibleClipDrawing", "iclip", 2), // (i)clip
    Drawing("Drawing", "p", 1), // p
    BaselineOffset("BaselineOffset", "pbo", 1); // pbo

    final String name;
    final String tag;
    final int maxParameters;

    Tag(String name, String tag, int maxParameters){
        this.name = name;
        this.tag = tag;
        this.maxParameters = maxParameters;
    }

    public static Map<Tag, String> search(String tag){
        Map<Tag, String> tagAndParameters = new HashMap<>();
        for(Tag at : Arrays
                .stream(values())
                .sorted(Comparator.comparingInt(Tag::getTagLength))
                .toList()
                .reversed()){

            if(tag.startsWith(at.tag)){
                tagAndParameters.put(at, tag);
                break;
            }
        }
        return tagAndParameters;
    }

    /**
     * Extract tags in text and do a list of these tags
     * @param str raw line with backslashes and tags
     * @return a separated tags list
     */
    public static List<Map<Tag, String>> getTagsFrom(String str){
        final List<Map<Tag, String>> tags = new ArrayList<>();
        String[] t = str.split("\\\\");
        for(String s : t){
            if(!s.isEmpty()){
                tags.add(search(s));
            }
        }
        return tags;
    }

    public static Object valuesOf(Map.Entry<Tag, String> entry){
        Tag tag = entry.getKey();
        String value = entry.getValue();
        switch(tag){
            // Integer case
            case AlignmentLegacy, AlignmentNumPad, BaselineOffset, BorderStyle,
                 FontEncoding, Karaoke, KaraokeFill, KaraokeFillLegacy, KaraokeOutline,
                 MarginL, MarginR, MarginV, MarginT, MarginB, WrapStyle -> {
                String s = value.replace(tag.getTag(), "");
                if(!s.isEmpty()) return Integer.parseInt(s);
            }
            // Float case
            case FontSize -> {
                String s = value.replace(tag.getTag(), "");
                if(!s.isEmpty()) return Float.parseFloat(s);
            }
            // Double case
            case Blur, BlurEdge, OutlineThickness, OutlineThicknessX,
                 OutlineThicknessY, ShadowShift, ShadowShiftX, ShadowShiftY,
                 Rotation, RotationX, RotationY, RotationZ, Scale, ScaleX, ScaleY,
                 Spacing, ShearX, ShearY -> {
                String s = value.replace(tag.getTag(), "");
                if(!s.isEmpty()) return Double.parseDouble(s);
            }
            // Boolean case
            case Bold, Italic, Underline, StrikeOut -> {
                String s = value.replace(tag.getTag(), "");
                if(!s.isEmpty()) return s.equals("1");
            }
            // Color case
            case TextColor, KaraokeColor, OutlineColor, ShadowColor -> {
                value = value.replace("&", "");
                value = value.replace("H", "");
                value = value.replace(tag.getTag(), "");
                if(value.isEmpty()) return null;
                int r = Integer.parseInt(value.substring(4), 16);
                int g = Integer.parseInt(value.substring(2, 4), 16);
                int b = Integer.parseInt(value.substring(0, 2), 16);
                return new Color(r, g, b);
            }
            // Alpha case
            case TextAlpha, KaraokeAlpha, OutlineAlpha, ShadowAlpha, Alpha -> {
                value = value.replace("&", "");
                value = value.replace("H", "");
                value = value.replace(tag.getTag(), "");
                if(value.isEmpty()) return null;
                return 255 - Integer.parseInt(value, 16);
            }
            // 2 parameters case
            case FadeSimple, Position, Origin -> {
                value = value.replace(tag.getTag(), "");

            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public String getTag() {
        return tag;
    }

    private int getTagLength(){
        return tag.length();
    }

    public int getMaxParameters() {
        return maxParameters;
    }
}
