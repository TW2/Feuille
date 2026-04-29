package feuille.module.drawing;

import feuille.module.drawing.ui.AssSketchpad;
import feuille.util.DrawColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class AssCanvas extends JPanel {

    private BufferedImage background = null;
    private boolean hasScaleChange = true;
    private double scaleThreshold = 0.25d;

    private final List<AShape> shapes = new ArrayList<>();
    private AShape lastAddedShape = null, pointShape = null;
    private Point2D lastPoint = null, cursor = new Point2D.Double();
    private boolean start = true;
    private boolean change = false, changeLastPoint = false;
    private boolean moveGrid = false;
    private Color pathFillColor, changedPathFillColor;

    private final AssSketchpad sketchpad;

    private double scale = 1d;
    private Dimension canvasSize = null;
    private Point2D shiftLocation = null;
    private Point2D centerLocation = null;

    public AssCanvas(AssSketchpad sketchpad) {
        this.sketchpad = sketchpad;
        setDoubleBuffered(true);

        pathFillColor = DrawColor.green.getColor(0.3f);
        changedPathFillColor = DrawColor.yellow.getColor(0.3f);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                // Applique une échelle inverse
                // (le dessin est conservé à l'échelle 1:1)
                Point2D u = getUnscaled(e.getPoint());
                Point2D p = AssCanvas.add(u, centerLocation);

                if(e.getButton() == MouseEvent.BUTTON1 && e.isControlDown()){
                    moveGrid = !moveGrid;
                }else if(e.getButton() == MouseEvent.BUTTON1){
                    if(sketchpad.getSelectedTool() == SelectedTool.Line ||
                            sketchpad.getSelectedTool() == SelectedTool.Quadratic ||
                            sketchpad.getSelectedTool() == SelectedTool.Cubic ||
                            sketchpad.getSelectedTool() == SelectedTool.Spline){

                        pointShape = new LineShape();
                        pointShape.setStartPoint(p);
                        pointShape.setEndPoint(p);

                        if(!start){
                            switch (sketchpad.getSelectedTool()){
                                case Line -> lastAddedShape = new LineShape();
                                case Quadratic -> lastAddedShape = new QuadraticShape();
                                case Cubic -> lastAddedShape = new CubicShape();
                                case Spline -> lastAddedShape = new BSplineShape();
                            }
                            lastAddedShape.setStartPoint(lastPoint);
                            lastAddedShape.setEndPoint(p);

                            if(sketchpad.getSelectedTool() == SelectedTool.Quadratic){
                                ((QuadraticShape)lastAddedShape).calculateControlPoint();
                            }else if(sketchpad.getSelectedTool() == SelectedTool.Cubic){
                                ((CubicShape)lastAddedShape).calculateControlPoint();
                            }

                            shapes.add(lastAddedShape);
                        }
                        lastPoint = p;
                        start = false;
                    }
                }else if(e.getButton() == MouseEvent.BUTTON2){
                    change = !change;
                    if(change){
                        for(AShape shape : shapes){
                            if(AShape.isSame(p, shape.startPoint, 10d)){
                                shape.setChangedStartPoint(p);
                            }
                            if(AShape.isSame(p, shape.endPoint, 10d)){
                                shape.setChangedEndPoint(p);
                                if(AShape.isSame(p, lastPoint, 10)){
                                    changeLastPoint = true;
                                }
                            }
                            if(AShape.isSame(p, shape.CP0.getPoint(), 10d)){
                                shape.getChangedCP0().setPoint(p);
                            }
                            if(AShape.isSame(p, shape.CP1.getPoint(), 10d)){
                                shape.getChangedCP1().setPoint(p);
                            }
                            if(AShape.isSame(p, shape.CP2.getPoint(), 10d)){
                                shape.getChangedCP2().setPoint(p);
                            }
                        }
                    }else{
                        // Set up
                        for(AShape shape : shapes){
                            //System.out.println("C: "+cursor+" P: "+p);
                            if(shape.changedStartPoint != null){
                                shape.setStartPoint(p);
                            }
                            if(shape.changedEndPoint != null){
                                shape.setEndPoint(p);
                            }
                            if(shape.getChangedCP0().getPoint() != null){
                                shape.getCP0().setPoint(p);
                            }
                            if(shape.getChangedCP1().getPoint() != null){
                                shape.getCP1().setPoint(p);
                            }
                            if(shape.getChangedCP2().getPoint() != null){
                                shape.getCP2().setPoint(p);
                            }
                            if(changeLastPoint){
                                lastPoint = p;
                                pointShape = new LineShape();
                                pointShape.setStartPoint(p);
                                pointShape.setEndPoint(p);
                            }
                        }
                        // Reset
                        for(AShape shape : shapes){
                            shape.changedStartPoint = null;
                            shape.changedEndPoint = null;
                            shape.getChangedCP0().setPoint(null);
                            shape.getChangedCP1().setPoint(null);
                            shape.getChangedCP2().setPoint(null);
                        }
                        changeLastPoint = false;
                    }
                }

                repaint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);

                // Applique une échelle inverse
                // (le dessin est conservé à l'échelle 1:1)
                cursor = new Point(e.getX(), e.getY());
                Point2D u = getUnscaled(e.getPoint());
                Point2D p = AssCanvas.add(u, centerLocation);

                if(change){
                    for(AShape shape : shapes){
                        if(shape.changedStartPoint != null){
                            shape.setChangedStartPoint(p);
                        }
                        if(shape.changedEndPoint != null){
                            shape.setChangedEndPoint(p);
                        }
                        if(shape.getChangedCP0().getPoint() != null){
                            shape.getChangedCP0().setPoint(p);
                        }
                        if(shape.getChangedCP1().getPoint() != null){
                            shape.getChangedCP1().setPoint(p);
                        }
                        if(shape.getChangedCP2().getPoint() != null){
                            shape.getChangedCP2().setPoint(p);
                        }
                    }
                }

                if(moveGrid){
                    shiftLocation = AssCanvas.add(shiftLocation, AssCanvas.subtract(e.getPoint(), shiftLocation));
                }

                repaint();
            }
        });

        addMouseWheelListener((e) -> {
            if(e.isControlDown()){
                scale = e.getWheelRotation() < 0 ? Math.min(scale + scaleThreshold, 100) : Math.max(scale - scaleThreshold, 0.25);
                hasScaleChange = true;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if(canvasSize == null) canvasSize = new Dimension(getWidth(), getHeight());
        if(centerLocation == null) centerLocation = new Point2D.Double((double) getWidth() /2, (double) getHeight() /2);
        if(shiftLocation == null) shiftLocation = new Point2D.Double();
        //=============================================
        if(hasScaleChange){
            background = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D ctx = background.createGraphics();
            ctx.setColor(Color.white);
            ctx.fillRect(0, 0, getWidth(), getHeight());

            ctx.setColor(Color.black);
            ctx.drawLine(
                    0,
                    (int)centerLocation.getY(),
                    getWidth(),
                    (int)centerLocation.getY()
            );
            ctx.drawLine(
                    (int)centerLocation.getX(),
                    0,
                    (int)centerLocation.getX(),
                    getHeight()
            );

            ctx.setColor(DrawColor.black.getColor(0.1f));
            drawGridFromCenter(ctx, (int) (10*scale)); // Bonne échelle sur la vue.

            ctx.setColor(DrawColor.cyan.getColor(0.2f));
            drawGridFromCenter(ctx, (int) (100*scale)); // Bonne échelle sur la vue.

            ctx.dispose();
            hasScaleChange = false;
        }

        if(background != null){
            g2d.drawImage(background, 0, 0, null);
        }
        //---------------------------------------------
        // Display cursor and its coordinates
        Graphics2D cb = (Graphics2D) g2d.create();
        drawCursor(cb, Color.pink, new Font("Serif", Font.PLAIN, 14));
        cb.dispose();

        //---------------------------------------------
        // Display a text to inform about the resolution (scale)
        Graphics2D ca = (Graphics2D) g2d.create();
        drawInfos(ca, DrawColor.blue_violet.getColor(.5f),
                new Font("Serif", Font.BOLD, 14),
                String.format("Scale : %d%s (Ctrl+Wheel)", (int)(scale * 100d), "%"),
                getWidth(), getHeight(), 9, 0);
        drawInfos(ca, DrawColor.blue.getColor(.5f),
                new Font("Serif", Font.BOLD, 14),
                "Hide/Show layers (Shift)",
                getWidth(), getHeight(), 9, 20);
        drawInfos(ca, DrawColor.chocolate.getColor(.5f),
                new Font("Serif", Font.BOLD, 14),
                "Add point (Mouse button 1)",
                getWidth(), getHeight(), 7, 0);
        drawInfos(ca, DrawColor.chocolate.getColor(.5f),
                new Font("Serif", Font.BOLD, 14),
                "Change point position (Mouse button 2)",
                getWidth(), getHeight(), 7, 20);
        ca.dispose();

        //---------------------------------------------
        if(!shapes.isEmpty()){
            g2d.setColor(change ? changedPathFillColor : pathFillColor);
            g2d.fill(createPathFiller(change, scale)); // Bonne échelle sur la vue.
        }

        for(AShape shape : shapes){
            shape.updateSettings(scale, shiftLocation, centerLocation);
            shape.draw(g2d); // Bonne échelle sur la vue. (scale applied using abstract presets)
        }
        if(pointShape != null){
            pointShape.updateSettings(scale, shiftLocation, centerLocation);
            pointShape.draw(g2d); // Bonne échelle sur la vue. (scale applied using abstract presets)
        }
        //=============================================
        g2d.dispose();
    }

    public Dimension getCanvasSize() {
        return canvasSize;
    }

    public void setCanvasSize(Dimension canvasSize) {
        this.canvasSize = canvasSize;
        setSize(canvasSize);
        repaint();
    }

    public Point2D getCenterLocation() {
        return centerLocation;
    }

    public void setCenterLocation(Point2D centerLocation) {
        this.centerLocation = centerLocation;
        repaint();
    }

    public Point2D getShiftLocation() {
        return shiftLocation;
    }

    public void setShiftLocation(Point2D shiftLocation) {
        this.shiftLocation = shiftLocation;
    }

    private void drawGridFromCenter(Graphics2D g, int threshold){
        for(int i=(int)centerLocation.getY()+threshold; i<getHeight(); i+=threshold){
            g.drawLine(0, i, getWidth(), i);
        }
        for(int i=(int)centerLocation.getY()-threshold; i>=0; i-=threshold){
            g.drawLine(0, i, getWidth(), i);
        }
        for(int i=(int)centerLocation.getX()+threshold; i<getWidth(); i+=threshold){
            g.drawLine(i, 0, i, getHeight());
        }
        for(int i=(int)centerLocation.getX()-threshold; i>=0; i-=threshold){
            g.drawLine(i, 0, i, getHeight());
        }
    }

    /**
     * Get scaled and shift coordinates
     * @param p point at scale 1:1
     * @return a point with good coordinates
     */
    private Point2D cs(Point2D p){
        // Distance scale 1:1 and without applied shift (a)
        double dxa = centerLocation.getX() - p.getX();
        double dya = centerLocation.getY() - p.getY();
        // Distance x scale (b)
        double dxb = dxa * scale;
        double dyb = dya * scale;
        // Scaled and shift coordinates
        return new Point2D.Double(
                shiftLocation.getX() * scale + centerLocation.getX() - dxb,
                shiftLocation.getY() * scale + centerLocation.getY() - dyb
        );
    }

    private Point2D getUnscaled(){
        Point2D pm = new Point2D.Double(
                cursor.getX() * scale + shiftLocation.getX() * scale - centerLocation.getX() * scale,
                cursor.getY() * scale + shiftLocation.getY() * scale - centerLocation.getY() * scale
        );
        return new Point2D.Double(
                pm.getX() / Math.pow(scale, 2),
                pm.getY() / Math.pow(scale, 2)
        );
    }

    private Point2D getUnscaled(Point cur){
        Point2D pm = new Point2D.Double(
                cur.getX() * scale + shiftLocation.getX() * scale - centerLocation.getX() * scale,
                cur.getY() * scale + shiftLocation.getY() * scale - centerLocation.getY() * scale
        );
        return new Point2D.Double(
                pm.getX() / Math.pow(scale, 2),
                pm.getY() / Math.pow(scale, 2)
        );
    }

    /**
     * Create a path from shapes
     * @param c when changes
     * @param s scale to apply
     * @return a general path
     */
    private GeneralPath createPathFiller(boolean c, double s){
        GeneralPath path = new GeneralPath();
        boolean start = true;

        for(AShape shape : shapes){
            switch(shape){
                case LineShape x -> {
                    if(start){
                        Point2D p = c && x.changedStartPoint != null ?
                                cs(x.changedStartPoint) : cs(x.startPoint);
                        path.moveTo(p.getX(), p.getY());
                        start = false;
                    }
                    Point2D p = c && x.changedEndPoint != null ?
                            cs(x.changedEndPoint) : cs(x.endPoint);
                    path.lineTo(p.getX(), p.getY());
                }
                case QuadraticShape x -> {
                    if(start){
                        Point2D p = c && x.changedStartPoint != null ?
                                cs(x.changedStartPoint) : cs(x.startPoint);
                        path.moveTo(p.getX(), p.getY());
                        start = false;
                    }
                    Point2D cp0 = c && x.changedCP0.getPoint() != null ?
                            cs(x.changedCP0.getPoint()) : cs(x.CP0.getPoint());
                    Point2D p = c && x.changedEndPoint != null ?
                            cs(x.changedEndPoint) : cs(x.endPoint);
                    path.quadTo(cp0.getX(), cp0.getY(), p.getX(), p.getY());
                }
                case CubicShape x -> {
                    if(start){
                        Point2D p = c && x.changedStartPoint != null ?
                                cs(x.changedStartPoint) : cs(x.startPoint);
                        path.moveTo(p.getX(), p.getY());
                        start = false;
                    }
                    Point2D cp1 = c && x.changedCP1.getPoint() != null ?
                            cs(x.changedCP1.getPoint()) : cs(x.CP1.getPoint());
                    Point2D cp2 = c && x.changedCP2.getPoint() != null ?
                            cs(x.changedCP2.getPoint()) : cs(x.CP2.getPoint());
                    Point2D p = c && x.changedEndPoint != null ?
                            cs(x.changedEndPoint) : cs(x.endPoint);
                    path.curveTo(cp1.getX(), cp1.getY(), cp2.getX(), cp2.getY(), p.getX(), p.getY());
                }
                default -> { }
            }
        }

        path.closePath();

        return path;
    }

    private void drawCursor(Graphics2D g, Color c, Font f){
        g.setColor(c);
        FontMetrics fm = g.getFontMetrics(f);
        String s1 = String.format("x=%.03f", getUnscaled().getX());
        float w1 = (float) fm.getStringBounds(s1, g).getWidth();
        float h1 = (float) fm.getStringBounds(s1, g).getHeight();
        String s2 = String.format("y=%.03f", getUnscaled().getY());
        float w2 = (float) fm.getStringBounds(s2, g).getWidth();
        float h2 = (float) fm.getStringBounds(s2, g).getHeight();
        g.draw(new Line2D.Double(0, cursor.getY(), getWidth(), cursor.getY()));
        g.draw(new Line2D.Double(cursor.getX(), 0, cursor.getX(), getHeight()));
        float x1 = (float) (cursor.getX() > getWidth() / 2d ? cursor.getX() - w1 - 10 : cursor.getX() + 10);
        float y1 = (float) (cursor.getY() > getHeight() / 2d ? cursor.getY() - h1 - 10 : cursor.getY() + 20);
        g.drawString(s1, x1, y1);
        float x2 = (float) (cursor.getX() > getWidth() / 2d ? cursor.getX() - w2 - 10 : cursor.getX() + 10);
        float y2 = (float) (cursor.getY() > getHeight() / 2d ? cursor.getY() - h2 + 10 : cursor.getY() + 40);
        g.drawString(s2, x2, y2);
    }

    private void drawInfos(Graphics2D g, Color c, Font f, String s, int width, int height, int alphaPos, int sub){
        g.setColor(c);
        FontMetrics fm = g.getFontMetrics(f);
        float w = (float) fm.getStringBounds(s, g).getWidth();
        float h = (float) fm.getStringBounds(s, g).getHeight();
        switch (alphaPos){
            case 1 -> g.drawString(s, 10, height - 10 + sub);
            case 2 -> g.drawString(s, (width - w) / 2, height - 10 + sub);
            case 3 -> g.drawString(s, width - w - 10, height - 10 + sub);
            case 4 -> g.drawString(s, 10, (height - h) / 2 + sub);
            case 5 -> g.drawString(s, (width - w) / 2, (height - h) / 2 + sub);
            case 6 -> g.drawString(s, width - w - 10, (height - h) / 2 + sub);
            case 7 -> g.drawString(s, 10, h + sub);
            case 8 -> g.drawString(s, (width - w) / 2, h + sub);
            case 9 -> g.drawString(s, width - w - 10, h + sub);
        }
    }

    public static Point2D add(Point2D... points){
        if(points.length == 0) return new Point2D.Double();
        if(points.length == 1) return points[0];
        Point2D p = points[0];
        for(int i=1; i<points.length; i++){
            p = new Point2D.Double(p.getX() + points[i].getX(), p.getY() + points[i].getY());
        }
        return p;
    }

    public static Point2D subtract(Point2D... points){
        if(points.length == 0) return new Point2D.Double();
        if(points.length == 1) return points[0];
        Point2D p = points[0];
        for(int i=1; i<points.length; i++){
            p = new Point2D.Double(p.getX() - points[i].getX(), p.getY() - points[i].getY());
        }
        return p;
    }

    public static Point2D multiply(Point2D... points){
        if(points.length == 0) return new Point2D.Double();
        if(points.length == 1) return points[0];
        Point2D p = points[0];
        for(int i=1; i<points.length; i++){
            p = new Point2D.Double(p.getX() * points[i].getX(), p.getY() * points[i].getY());
        }
        return p;
    }

    public static Point2D divide(Point2D... points){
        if(points.length == 0) return new Point2D.Double();
        if(points.length == 1) return points[0];
        Point2D p = points[0];
        for(int i=1; i<points.length; i++){
            if(points[i].getX() == 0d) points[i].setLocation(1d, points[i].getY());
            if(points[i].getY() == 0d) points[i].setLocation(points[i].getX(), 1d);
            p = new Point2D.Double(p.getX() / points[i].getX(), p.getY() / points[i].getY());
        }
        return p;
    }
}
