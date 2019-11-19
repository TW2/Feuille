/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smallboxforfansub.drawing.bspline;

import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Trouvé sur le projet http://code.google.com/p/curvecreator/
 * @author The Wingate 2940
 */
public abstract class Curve {
    public static int LOW_QUALITY = 1,  MEDIUM_QUALITY = 2,  HIGH_QUALITY = 3, EXPORT_QUALITY = 4;
   
    private List<Point2D> controlPoints = new ArrayList<Point2D>();

    public abstract boolean isValid();
   
    /**
     * Paints the curve on the provided graphics object
     * @param g The graphics object to paint on.
     * @param quality The visual-quality of the curve. Must be one of the constants LOW_QUALITY, MEDIUM_QUALITY, HIGH_QUALITY.
     * @param adaptive adaptive rendering or not
     */
    public abstract void paintCurve(Graphics2D g, int quality, boolean adaptive);

    public void paintControlPoints(Graphics2D g) {
        for (Point2D point : controlPoints) {
            Line2D l2 = new Line2D.Double(point, point);
            g.draw(l2);
        }
    }
   
    public void paintControlPolygon(Graphics2D g) {
        Point2D lastPoint = null;
        for (Point2D point : controlPoints) {
            if (lastPoint != null) {
                Line2D line = new Line2D.Double(lastPoint, point);
                g.draw(line);
            }
            lastPoint = point;
        }
    }

    public List<Point2D> getControlPoints() {
        return controlPoints;
    }

    public abstract void addControlPoint(Point2D point);

    public abstract void removeControlPoint(Point2D point);

    public void setControlPoints(List<Point2D> controlPoints) {
        this.controlPoints = controlPoints;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("A curve segment with " + controlPoints.size() + " control points: \n");
        for (Point2D point2D : controlPoints) {
            sb.append(point2D).append("\t");
        }
        return sb.toString();
    }

}
