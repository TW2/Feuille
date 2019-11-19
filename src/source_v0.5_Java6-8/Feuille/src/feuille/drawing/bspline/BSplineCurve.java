/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.drawing.bspline;

import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Trouvé sur le projet http://code.google.com/p/curvecreator/
 * @author The Wingate 2940
 */
public class BSplineCurve extends Curve {
    protected List<Double> knots = new ArrayList<Double>();
    protected int degree = 3;

    public BSplineCurve(Point2D startPoint) {
        addControlPoint(startPoint);
        updateKnots();
    }
   
    public BSplineCurve(BezierCurve bezierCurve) {
        getControlPoints().addAll(bezierCurve.getControlPoints());
        degree = bezierCurve.getControlPoints().size() - 1;
        updateKnots();
        for (int i = 0; i < knots.size(); i++) {
            knots.set(i, new Double(i / (degree + 1)));
        }
    }

    public BSplineCurve(List<Point2D> controlPoints, List<Double> knots, int degree) throws IllegalArgumentException {
        setControlPoints(controlPoints);
        this.degree = degree;
        this.knots = knots;
        if(!isValid()) {
            throw new IllegalArgumentException("B-Spline curve is not valid.");
        }
    }
   
    @Override
    public final boolean isValid() {
        return getControlPoints().size() > degree && knots.size() == getControlPoints().size() + degree + 1;
    }

    @Override
    public void paintCurve(Graphics2D g, int quality, boolean adaptive) {
        if(adaptive) {
            paintAdaptive(g, quality);
        } else {
            paintUniform(g, quality);
        }
    }

    @Override
    public final void addControlPoint(Point2D point) {
        getControlPoints().add(point);
        updateKnots();
    }

    @Override
    public void removeControlPoint(Point2D point) {
        getControlPoints().remove(point);
        updateKnots();
    }

    private void paintUniform(Graphics2D g, int quality) {
        int numPoints = quality * 2 * getControlPoints().size();
        Point2D[] points = new Point2D[numPoints];
        double minU = knots.get(degree);
        double maxU = knots.get(knots.size() - 1 - degree);
        if (minU >= maxU) {
            return;
        }

        for (int currentPoint = 0; currentPoint < points.length; currentPoint++) {
            double u = (((maxU - minU) / (points.length - 0.9999999)) * currentPoint) + minU;
            double N[] = getBasis(u, getControlPoints().size() - 1, degree);
            double x = 0, y = 0;

            for (int i = 0; i < getControlPoints().size(); i++) {
                Point2D control = getControlPoints().get(i);
                x += control.getX() * N[i];
                y += control.getY() * N[i];
            }

            points[currentPoint] = new Point2D.Double(x, y);
        }
       
        for (int i = 0; i < points.length - 1; i++) {
            Line2D l = new Line2D.Double(points[i], points[i + 1]);
            g.draw(l);
        }
    }

    private void paintAdaptive(Graphics2D g, int quality) {
        for (int i = degree; i < getControlPoints().size(); i++) {
            extractBezierCurve(i).paintCurve(g, quality, true);
        }

    }

    public List<BezierCurve> extractAllBezierCurves() {
        List<BezierCurve> curves = new ArrayList<BezierCurve>();
        for (int i = degree; i < getControlPoints().size(); i++) {
            curves.add(extractBezierCurve(i));
        }
        return curves;
    }
   
    private BezierCurve extractBezierCurve(int index) {
        ArrayList<Double> tempKnots = new ArrayList<Double>();
        ArrayList<Point2D> tempControlPoints = new ArrayList<Point2D>();

        for (int i = index - degree; i <= index; i++) {
            Point2D cp = getControlPoints().get(i);
            tempControlPoints.add(new Point2D.Double(cp.getX(), cp.getY()));
        }

        for (int i = index - degree; i <= index + degree + 1; i++) {
            double kn = knots.get(i);
            tempKnots.add(kn);
        }
        for (;;) {
            int j = 0;
            for (int i = degree - 1; i > 0; i--) {
                if (tempKnots.get(i) < tempKnots.get(degree)) {
                    j = i;
                    break;
                }
                j = 0;
            }
            if (j == 0) {
                break;
            }
           
            for (int i = 0; i < j; i++) {
                double knotKI1 = tempKnots.get(degree + i + 1);
                double knotK = tempKnots.get(degree);
                double knotI1 = tempKnots.get(i + 1);
                Point2D cpI = tempControlPoints.get(i);
                Point2D cpI1 = tempControlPoints.get(i + 1);
                cpI.setLocation(
                        ((knotKI1 - knotK) / (knotKI1 - knotI1)) * cpI.getX() + ((knotK - knotI1) / (knotKI1 - knotI1)) * cpI1.getX(),
                        ((knotKI1 - knotK) / (knotKI1 - knotI1)) * cpI.getY() + ((knotK - knotI1) / (knotKI1 - knotI1)) * cpI1.getY());
            }

            for (int i = 0; i < j; i++) {
                tempKnots.set(i, tempKnots.get(i + 1));
            }
            tempKnots.set(j, tempKnots.get(degree));
        }



        for (;;) {
            int j = 0;
            for (int i = degree + 2; i < degree * 2 + 1; i++) {
                if (tempKnots.get(i) > tempKnots.get(degree + 1)) {
                    j = i;
                    break;
                }
                j = 0;
            }
            if (j == 0) {
                break;
            }

            for (int i = degree; i >= j - degree; i--) {
                double knotKI = tempKnots.get(degree + i);
                double knotK1 = tempKnots.get(degree + 1);
                double knotI = tempKnots.get(i);
                Point2D cpIm1 = tempControlPoints.get(i - 1);
                Point2D cpI = tempControlPoints.get(i);
                cpI.setLocation(
                        ((knotKI - knotK1) / (knotKI - knotI)) * cpIm1.getX() + ((knotK1 - knotI) / (knotKI - knotI)) * cpI.getX(),
                        ((knotKI - knotK1) / (knotKI - knotI)) * cpIm1.getY() + ((knotK1 - knotI) / (knotKI - knotI)) * cpI.getY());
            }

            for (int i = degree * 2 + 1; i > j; i--) {
                tempKnots.set(i, tempKnots.get(i - 1));
            }
            tempKnots.set(j, tempKnots.get(degree + 1));
        }
       
        return new BezierCurve(tempControlPoints);
    }

    private double[] getBasis(double u, int n, int degree) {
        double[] basis = new double[n + 2];
        for (int i = 0; i < basis.length; i++) {
            basis[i] = (u >= knots.get(i) && u < knots.get(i + 1)) ? 1 : 0;
        }

        for (int k = 1; k <= degree; k++) {
            for (int i = 0; i <= n; i++) {
                double a = (u - knots.get(i)) / (knots.get(i + k) - knots.get(i));
                double b = (knots.get(i + k + 1) - u) / (knots.get(i + k + 1) - knots.get(i + 1));

                basis[i] = basis[i] * a + basis[i + 1] * b;
            }
        }

        return basis;
    }

    private void updateKnots() {
        knots = new ArrayList<Double>(getControlPoints().size() + degree + 1);
        for (int i = 0; i < getControlPoints().size() + degree + 1; i++) {
            knots.add(new Double(i));
        }
    }

    public int getDegree() {
        return degree;
    }

    public List<Double> getKnots() {
        return knots;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public void setKnots(List<Double> knots) {
        this.knots = knots;
    }
}
