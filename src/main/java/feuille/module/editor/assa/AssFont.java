package feuille.module.editor.assa;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;

public class AssFont {
    private String name;
    private float size;
    private boolean bold;
    private boolean italic;
    private boolean underline;
    private boolean strikeout;

    public AssFont(String name, float size, boolean bold, boolean italic, boolean underline, boolean strikeout) {
        this.name = name;
        this.size = size;
        this.bold = bold;
        this.italic = italic;
        this.underline = underline;
        this.strikeout = strikeout;
    }

    public AssFont(String name, float size) {
        this.name = name;
        this.size = size;
        bold = false;
        italic = false;
        underline = false;
        strikeout = false;
    }

    public AssFont(String name) {
        this(name, 52f);
    }

    public AssFont() {
        this("Serif", 52f);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public boolean isBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public boolean isItalic() {
        return italic;
    }

    public void setItalic(boolean italic) {
        this.italic = italic;
    }

    public boolean isUnderline() {
        return underline;
    }

    public void setUnderline(boolean underline) {
        this.underline = underline;
    }

    public boolean isStrikeout() {
        return strikeout;
    }

    public void setStrikeout(boolean strikeout) {
        this.strikeout = strikeout;
    }

    public void setFont(Font x){
        Object obj;
        name = x.getFamily();
        size = x.getSize2D();
        bold = x.isBold();
        italic = x.isItalic();
        obj = x.getAttributes().get(TextAttribute.UNDERLINE);
        if(obj instanceof Integer v){
            underline = v != -1;
        }
        obj = x.getAttributes().get(TextAttribute.STRIKETHROUGH);
        if(obj instanceof Boolean v){
            strikeout = v;
        }
    }

    public Font getFont(){
        final Map<TextAttribute, Object> map = new HashMap<>();
        map.put(TextAttribute.FAMILY, name);
        map.put(TextAttribute.SIZE, size);
        map.put(TextAttribute.WEIGHT,
                bold ? TextAttribute.WEIGHT_BOLD : TextAttribute.WEIGHT_REGULAR);
        map.put(TextAttribute.POSTURE,
                italic ? TextAttribute.POSTURE_OBLIQUE : TextAttribute.POSTURE_REGULAR);
        map.put(TextAttribute.UNDERLINE,
                underline ? TextAttribute.UNDERLINE_ON : Integer.valueOf(-1));
        map.put(TextAttribute.STRIKETHROUGH,
                strikeout ? TextAttribute.STRIKETHROUGH_ON : Boolean.valueOf(false));
        return new Font(map);
    }
}
