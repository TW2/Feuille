package feuille.module.drawing;

import feuille.util.Loader;

import java.awt.*;
import java.awt.geom.*;

public class QuadraticShape extends AShape {

    public QuadraticShape() {
        name = Loader.language("shape.quadratic", "Quadratic Bézier");
    }

    @Override
    public void draw(Graphics2D g) {
        // Draw changes
        if(changedStartPoint != null || changedEndPoint != null || changedCP0.getPoint() != null){
            Point2D s = changedStartPoint != null ? cs(changedStartPoint) : cs(startPoint);
            Point2D e = changedEndPoint != null ? cs(changedEndPoint) : cs(endPoint);
            Point2D cp0 = changedCP0.getPoint() != null ? cs(changedCP0.getPoint()) : cs(CP0.getPoint());
            drawChangedQuad(g, s, cp0, e);
        }

        // Draw Quadratic (no changes)
        drawQuad(g, cs(startPoint), cs(CP0.getPoint()), cs(endPoint));
    }

    // TODO verify scale and shift for the following method
    public void calculateControlPoint(){
        Point2D p = new Point2D.Double(
                ((endPoint.getX()*scale - startPoint.getX()*scale) / 2) + startPoint.getX()*scale,
                ((endPoint.getY()*scale - startPoint.getY()*scale) / 2) + startPoint.getY()*scale
        );
        CP0.getPoint().setLocation(new Point2D.Double(p.getX()/scale, p.getY()/scale));
    }
}
