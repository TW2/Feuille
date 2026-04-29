package feuille.module.drawing;

import feuille.util.Loader;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

public class BSplineShape extends AShape {

    public BSplineShape() {
        name = Loader.language("shape.spline", "Bézier Spline");
    }

    @Override
    public void draw(Graphics2D g) {
        // Draw line
        g.setColor(shapeColor);
        g.draw(new Line2D.Double(startPoint, endPoint));

        // Draw grip
        g.setColor(gripColor);

        // Start
        g.fill(new Rectangle2D.Double(
                startPoint.getX() - gripSize / 2,
                startPoint.getY() - gripSize / 2,
                gripSize,
                gripSize
        ));

        // End
        g.fill(new Rectangle2D.Double(
                endPoint.getX() - gripSize / 2,
                endPoint.getY() - gripSize / 2,
                gripSize,
                gripSize
        ));
    }
}
