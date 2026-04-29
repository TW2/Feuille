package feuille.module.drawing;

import java.awt.*;
import java.awt.geom.*;
import java.util.HashMap;
import java.util.Map;

public abstract class AShape implements IShape {

    protected double scale = 1d;
    protected Point2D shiftPos = new Point2D.Double();
    protected Point2D centerPos = new Point2D.Double();

    protected ControlPoint CP0;
    protected ControlPoint CP1;
    protected ControlPoint CP2;
    protected ControlPoint changedCP0;
    protected ControlPoint changedCP1;
    protected ControlPoint changedCP2;

    protected String name;
    protected Color shapeColor, selectedShapeColor;
    protected Color gripColor, selectedGripColor;
    protected Color constructionColor;
    protected Color changementColor;
    protected double gripSize;
    protected GripStyle gripStyle;
    protected double thickness;
    protected double constructionThickness;
    protected Point2D startPoint, endPoint;
    protected Point2D changedStartPoint, changedEndPoint;

    public AShape(){
        CP0 = new ControlPoint(ControlPoint.Type.CP0);
        CP1 = new ControlPoint(ControlPoint.Type.CP1);
        CP2 = new ControlPoint(ControlPoint.Type.CP2);
        changedCP0 = new ControlPoint(ControlPoint.Type.CP0, null);
        changedCP1 = new ControlPoint(ControlPoint.Type.CP1, null);
        changedCP2 = new ControlPoint(ControlPoint.Type.CP2, null);
        name = "Unknown Shape";
        shapeColor = Color.blue;
        gripColor = Color.red;
        selectedShapeColor = Color.blue;
        selectedGripColor = Color.red;
        constructionColor = Color.magenta;
        changementColor = Color.magenta.darker();
        gripSize = 10d;
        gripStyle = GripStyle.Square;
        thickness = 1d;
        constructionThickness = 1d;
        startPoint = new Point2D.Double();
        endPoint = new Point2D.Double();
        changedStartPoint = null;
        changedEndPoint = null;
    }

    public ControlPoint getCP0() {
        return CP0;
    }

    public void setCP0(ControlPoint CP0) {
        this.CP0 = CP0;
    }

    public ControlPoint getCP1() {
        return CP1;
    }

    public void setCP1(ControlPoint CP1) {
        this.CP1 = CP1;
    }

    public ControlPoint getCP2() {
        return CP2;
    }

    public void setCP2(ControlPoint CP2) {
        this.CP2 = CP2;
    }

    public ControlPoint getChangedCP0() {
        return changedCP0;
    }

    public void setChangedCP0(ControlPoint changedCP0) {
        this.changedCP0 = changedCP0;
    }

    public ControlPoint getChangedCP1() {
        return changedCP1;
    }

    public void setChangedCP1(ControlPoint changedCP1) {
        this.changedCP1 = changedCP1;
    }

    public ControlPoint getChangedCP2() {
        return changedCP2;
    }

    public void setChangedCP2(ControlPoint changedCP2) {
        this.changedCP2 = changedCP2;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setShapeColor(Color c) {
        shapeColor = c;
    }

    @Override
    public void setGripColor(Color c) {
        gripColor = c;
    }

    @Override
    public void setSelectedShapeColor(Color c) {
        selectedShapeColor = c;
    }

    @Override
    public void setSelectedGripColor(Color c) {
        selectedGripColor = c;
    }

    @Override
    public void setConstructionColor(Color c) {
        constructionColor = c;
    }

    @Override
    public void setGripSize(double size) {
        gripSize = size;
    }

    @Override
    public void setGripStyle(GripStyle style) {
        gripStyle = style;
    }

    @Override
    public void setThickness(double thickness) {
        this.thickness = thickness;
    }

    @Override
    public void setConstructionThickness(double thickness) {
        constructionThickness = thickness;
    }

    @Override
    public void setStartPoint(Point2D p) {
        startPoint = p;
    }

    @Override
    public void setEndPoint(Point2D p) {
        endPoint = p;
    }

    @Override
    public Point2D getNearestPoint(double x, double y) {
        final Map<Point2D, Double> distances = new HashMap<>();

        distances.put(startPoint, Point2D.distance(x, y, startPoint.getX(), startPoint.getY()));
        distances.put(endPoint, Point2D.distance(x, y, endPoint.getX(), endPoint.getY()));

        distances.put(CP0.getPoint(), Point2D.distance(x, y, CP0.getPoint().getX(), CP0.getPoint().getY()));
        distances.put(CP1.getPoint(), Point2D.distance(x, y, CP1.getPoint().getX(), CP1.getPoint().getY()));
        distances.put(CP2.getPoint(), Point2D.distance(x, y, CP2.getPoint().getX(), CP2.getPoint().getY()));

        double nearestValue = Point2D.distance(x, y, startPoint.getX(), startPoint.getY());
        Point2D nearest = startPoint;

        for(Map.Entry<Point2D, Double> entry : distances.entrySet()){
            double calculation = Math.min(entry.getValue(), nearestValue);
            if(calculation <= nearestValue){
                nearestValue = calculation;
                nearest = entry.getKey();
            }
        }

        return nearest;
    }

    @Override
    public Point2D getStartPoint() {
        return startPoint;
    }

    @Override
    public Point2D getEndPoint() {
        return endPoint;
    }

    public Point2D getChangedStartPoint() {
        return changedStartPoint;
    }

    public void setChangedStartPoint(Point2D changedStartPoint) {
        this.changedStartPoint = changedStartPoint;
    }

    public Point2D getChangedEndPoint() {
        return changedEndPoint;
    }

    public void setChangedEndPoint(Point2D changedEndPoint) {
        this.changedEndPoint = changedEndPoint;
    }

    public void updateSettings(double scale, Point2D shiftPos, Point2D centerPos){
        this.scale = scale;
        this.shiftPos = shiftPos;
        this.centerPos = centerPos;
    }

    /**
     * Get scaled and shift coordinates
     * @param p point at scale 1:1
     * @return a point with good coordinates
     */
    protected Point2D cs(Point2D p){
        // Distance scale 1:1 and without applied shift (a)
        double dxa = centerPos.getX() - p.getX();
        double dya = centerPos.getY() - p.getY();
        // Distance x scale (b)
        double dxb = dxa * scale;
        double dyb = dya * scale;
        // Scaled and shift coordinates
        return new Point2D.Double(
                shiftPos.getX() * scale + (centerPos.getX() - dxb),
                shiftPos.getY() * scale + (centerPos.getY() - dyb)
        );
    }

    protected void fillPoint(Graphics2D g, Point2D p){
        g.fill(new Rectangle2D.Double(
                p.getX() - gripSize / 2,
                p.getY() - gripSize / 2,
                gripSize,
                gripSize
        ));
    }

    protected void fillControlPoint(Graphics2D g, Point2D p){
        g.fill(new Ellipse2D.Double(
                p.getX() - gripSize / 2,
                p.getY() - gripSize / 2,
                gripSize,
                gripSize
        ));
    }

    protected Stroke getDashedStroke(){
        return new BasicStroke(
                1f,
                BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND,
                1f,
                new float[]{1.5f, 2.5f},
                0f
        );
    }

    protected void drawLine(Graphics2D g, Point2D s, Point2D e){
        Graphics2D ctx = (Graphics2D) g.create();
        //----------------------------------------------------------
        ctx.setColor(shapeColor); // Draw line
        ctx.draw(new Line2D.Double(s, e));
        ctx.setColor(gripColor); // Draw grip
        fillPoint(ctx, s); // Start
        fillPoint(ctx, e); // End
        //----------------------------------------------------------
        ctx.dispose();
    }

    protected void drawChangedLine(Graphics2D g, Point2D s, Point2D e){
        Graphics2D ctx = (Graphics2D) g.create();
        //----------------------------------------------------------
        ctx.setColor(changementColor);
        ctx.setStroke(getDashedStroke());
        ctx.draw(new Line2D.Double(s, e));
        fillPoint(ctx, s); // Start
        fillPoint(ctx, e); // End
        //----------------------------------------------------------
        ctx.dispose();
    }

    protected void drawQuad(Graphics2D g, Point2D s, Point2D cp0, Point2D e){
        Graphics2D ctx = (Graphics2D) g.create();
        //----------------------------------------------------------
        ctx.setColor(shapeColor); // Draw line
        ctx.draw(new QuadCurve2D.Double(
                s.getX(), s.getY(),
                cp0.getX(), cp0.getY(),
                e.getX(), e.getY()
        ));
        ctx.setColor(constructionColor); // Draw construction
        ctx.setStroke(getDashedStroke());
        ctx.draw(new Line2D.Double(s, cp0));
        ctx.draw(new Line2D.Double(cp0, e));
        ctx.setColor(gripColor); // Draw grip
        fillPoint(ctx, s); // Start
        fillPoint(ctx, e); // End
        fillControlPoint(ctx, cp0); // Control point
        //----------------------------------------------------------
        ctx.dispose();
    }

    protected void drawChangedQuad(Graphics2D g, Point2D s, Point2D cp0, Point2D e){
        Graphics2D ctx = (Graphics2D) g.create();
        //----------------------------------------------------------
        ctx.setColor(changementColor);
        ctx.setStroke(getDashedStroke());
        ctx.draw(new QuadCurve2D.Double(
                s.getX(), s.getY(),
                cp0.getX(), cp0.getY(),
                e.getX(), e.getY()
        ));
        ctx.setColor(constructionColor); // Draw construction
        ctx.draw(new Line2D.Double(s, cp0));
        ctx.draw(new Line2D.Double(cp0, e));
        fillPoint(ctx, s); // Start
        fillPoint(ctx, e); // End
        fillControlPoint(ctx, cp0); // CP0
        //----------------------------------------------------------
        ctx.dispose();
    }

    protected void drawCubic(Graphics2D g, Point2D s, Point2D cp1, Point2D cp2, Point2D e){
        Graphics2D ctx = (Graphics2D) g.create();
        //----------------------------------------------------------
        ctx.setColor(shapeColor); // Draw line
        ctx.draw(new CubicCurve2D.Double(
                s.getX(), s.getY(),
                cp1.getX(), cp1.getY(),
                cp2.getX(), cp2.getY(),
                e.getX(), e.getY()
        ));
        ctx.setColor(constructionColor); // Draw construction
        ctx.setStroke(getDashedStroke());
        ctx.draw(new Line2D.Double(s, cp1));
        ctx.draw(new Line2D.Double(cp1, cp2));
        ctx.draw(new Line2D.Double(cp2, e));
        ctx.setColor(gripColor); // Draw grip
        fillPoint(ctx, s); // Start
        fillPoint(ctx, e); // End
        fillControlPoint(ctx, cp1); // CP1
        fillControlPoint(ctx, cp2); // CP2
        //----------------------------------------------------------
        ctx.dispose();
    }

    protected void drawChangedCubic(Graphics2D g, Point2D s, Point2D cp1, Point2D cp2, Point2D e){
        Graphics2D ctx = (Graphics2D) g.create();
        //----------------------------------------------------------
        ctx.setColor(changementColor);
        ctx.setStroke(getDashedStroke());
        ctx.draw(new CubicCurve2D.Double(
                s.getX(), s.getY(),
                cp1.getX(), cp1.getY(),
                cp2.getX(), cp2.getY(),
                e.getX(), e.getY()
        ));
        ctx.setColor(constructionColor); // Draw construction
        ctx.draw(new Line2D.Double(s, cp1));
        ctx.draw(new Line2D.Double(cp1, cp2));
        ctx.draw(new Line2D.Double(cp2, e));
        fillPoint(ctx, s); // Start
        fillPoint(ctx, e); // End
        fillControlPoint(ctx, cp1); // CP1
        fillControlPoint(ctx, cp2); // CP2
        //----------------------------------------------------------
        ctx.dispose();
    }

    public static boolean isSame(Point2D cursor, Point2D p, double errorThreshold){
        Rectangle2D searchArea = new Rectangle2D.Double(
                p.getX() - errorThreshold,
                p.getY() - errorThreshold,
                errorThreshold * 2,
                errorThreshold * 2
        );
        return searchArea.contains(cursor);
    }
}
