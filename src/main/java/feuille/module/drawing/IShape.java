package feuille.module.drawing;

import java.awt.*;
import java.awt.geom.Point2D;

public interface IShape {
    String getName();
    void draw(Graphics2D g);
    void setShapeColor(Color c);
    void setGripColor(Color c);
    void setSelectedShapeColor(Color c);
    void setSelectedGripColor(Color c);
    void setConstructionColor(Color c);
    void setGripSize(double size);
    void setGripStyle(GripStyle style);
    void setThickness(double thickness);
    void setConstructionThickness(double thickness);
    void setStartPoint(Point2D p);
    void setEndPoint(Point2D p);
    Point2D getNearestPoint(double x, double y);
    Point2D getStartPoint();
    Point2D getEndPoint();
}
