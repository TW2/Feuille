package feuille.module.editor.assa.render;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Char extends AGraphicElement {

    private String character;
    private final List<Map<Tag, String>> tags;
    private Font font;
    private double advance; // Advance
    private double extraSpacing; // Spacing
    private double height; // Height (ascent leading descent)

    public Char() {
        graphicType = GraphicType.Letter;
        tags = new ArrayList<>();
        font = null;
        extraSpacing = 0d;
    }

    @Override
    public void setShape(GeneralPath sh) {
        size = sh.getBounds().getSize();
        double x = sh.getBounds2D().getX();
        double y = sh.getBounds2D().getY();
        insertPoint = new Point2D.Double(x, y);
        relativeToInsertPoint = new Point2D.Double();
        shape = sh;
        height = sh.getBounds2D().getHeight();
    }

    public void addTag(Map<Tag, String> tag){
        tags.add(tag);
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public List<Map<Tag, String>> getTags() {
        return tags;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public double getAdvance() {
        return advance;
    }

    public void setAdvance(double advance) {
        this.advance = advance;
    }

    public double getExtraSpacing() {
        return extraSpacing;
    }

    public void setExtraSpacing(double extraSpacing) {
        this.extraSpacing = extraSpacing;
    }

    public double getHeight() {
        return height;
    }
}
