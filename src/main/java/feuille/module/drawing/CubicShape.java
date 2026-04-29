package feuille.module.drawing;

import feuille.util.Loader;

import java.awt.*;
import java.awt.geom.*;

public class CubicShape extends AShape {

    public CubicShape() {
        name = Loader.language("shape.cubic", "Cubic Bézier");
    }

    @Override
    public void draw(Graphics2D g) {
        // Draw changes
        if(changedStartPoint != null || changedEndPoint != null
                || changedCP1.getPoint() != null || changedCP2.getPoint() != null){
            Point2D s = changedStartPoint != null ? cs(changedStartPoint) : cs(startPoint);
            Point2D e = changedEndPoint != null ? cs(changedEndPoint) : cs(endPoint);
            Point2D cp1 = changedCP1.getPoint() != null ? cs(changedCP1.getPoint()) : cs(CP1.getPoint());
            Point2D cp2 = changedCP2.getPoint() != null ? cs(changedCP2.getPoint()) : cs(CP2.getPoint());
            drawChangedCubic(g, s, cp1, cp2, e);
        }

        // Draw Quadratic (no changes)
        drawCubic(g, cs(startPoint), cs(CP1.getPoint()), cs(CP2.getPoint()), cs(endPoint));
    }

    // TODO verify scale and shift for the following method
    public void calculateControlPoint(){
        Point2D p1 = new Point2D.Double(
                ((endPoint.getX()*scale - startPoint.getX()*scale) * 1 / 3) + startPoint.getX()*scale,
                ((endPoint.getY()*scale - startPoint.getY()*scale) * 1 / 3) + startPoint.getY()*scale
        );
        CP1.getPoint().setLocation(new Point2D.Double(p1.getX()/scale, p1.getY()/scale));
        Point2D p2 = new Point2D.Double(
                ((endPoint.getX()*scale - startPoint.getX()*scale) * 2 / 3) + startPoint.getX()*scale,
                ((endPoint.getY()*scale - startPoint.getY()*scale) * 2 / 3) + startPoint.getY()*scale
        );
        CP2.getPoint().setLocation(new Point2D.Double(p2.getX()/scale, p2.getY()/scale));
    }
}
