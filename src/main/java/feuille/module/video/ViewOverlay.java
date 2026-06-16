package feuille.module.video;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class ViewOverlay {

    public enum Mode {
        // Nothing
        None,

        // Timeline
        TimeBar,

        // Drawing mode
        Point,
        ControlPoint,
        Line,
        Quadratic,
        Cubic,
        BSpline;
    }

    private Mode mode;
    private final Color overlayBackColor;
    private final Color overlayForeColor;

    public ViewOverlay() {
        mode = Mode.None;
        overlayBackColor = new Color(255, 255, 0, 153);
        overlayForeColor = Color.black;
    }

    public void drawOverlay(Graphics2D g, int xa, int ya, int w, int h) {

        Rectangle2D r = new Rectangle2D.Double(xa + 20, ya + h - 60, w - 40, 40);
        g.setColor(overlayBackColor);
        g.fill(r);

        g.setColor(overlayForeColor);

        double x  = 20 + 1;
        double y  = h - 60 + 1;

        Rectangle2D c = null;

        switch (mode) {
            case TimeBar -> c = new Rectangle2D.Double(x, y, 40 - 2, 40 - 2);
            case Point -> c = new Rectangle2D.Double(x+40, y, 40 - 2, 40 - 2);
            case ControlPoint -> c = new Rectangle2D.Double(x+80, y, 40 - 2, 40 - 2);
            case Line -> c = new Rectangle2D.Double(x+120, y, 40 - 2, 40 - 2);
            case Quadratic -> c = new Rectangle2D.Double(x+160, y, 40 - 2, 40 - 2);
            case Cubic -> c = new Rectangle2D.Double(x+200, y, 40 - 2, 40 - 2);
            case BSpline -> c = new Rectangle2D.Double(x+240, y, 40 - 2, 40 - 2);
            default -> { /* None */ }
        }
        g.draw(c);
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }
}
