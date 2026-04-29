package feuille.module.editor.assa.render;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

public interface IGraphicElement {

    GraphicType getType();
    void setType(GraphicType graphicType);

    Dimension getSize();
    void setSize(Dimension size);

    // Insert point is the bottom left one by default
    Point2D getInsertPoint();
    void setInsertPoint(Point2D insertPoint);

    Point2D getRelativeToInsertPoint();
    void setRelativeToInsertPoint(Point2D relative);

    void draw(Graphics2D g);
    void fill(Graphics2D g);

    GeneralPath getShape();
    void setShape(GeneralPath sh);
}
