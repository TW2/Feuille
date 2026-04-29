package feuille.module.drawing;

import java.awt.geom.Point2D;

public class ControlPoint {

    public enum Type {
        CP0, CP1, CP2
    }

    private Type type;
    private Point2D point;

    public ControlPoint(Type type, Point2D point) {
        this.type = type;
        this.point = point;
    }

    public ControlPoint(Type type) {
        this(type, new Point2D.Double());
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Point2D getPoint() {
        return point;
    }

    public void setPoint(Point2D point) {
        this.point = point;
    }
}
