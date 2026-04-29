package feuille.module.editor.assa.render;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

public abstract class AGraphicElement implements IGraphicElement {

    protected GraphicType graphicType;
    protected Dimension size;
    protected Point2D insertPoint;
    protected Point2D relativeToInsertPoint;
    protected GeneralPath shape;

    public AGraphicElement(){
        graphicType = GraphicType.Letter;
        size = new Dimension();
        insertPoint = new Point2D.Double();
    }

    @Override
    public GraphicType getType() {
        return graphicType;
    }

    @Override
    public void setType(GraphicType graphicType) {
        this.graphicType = graphicType;
    }

    @Override
    public Dimension getSize() {
        return size;
    }

    @Override
    public void setSize(Dimension size) {
        this.size = size;
    }

    @Override
    public Point2D getInsertPoint() {
        return insertPoint;
    }

    @Override
    public void setInsertPoint(Point2D insertPoint) {
        this.insertPoint = insertPoint;
    }

    @Override
    public Point2D getRelativeToInsertPoint() {
        return relativeToInsertPoint;
    }

    @Override
    public void setRelativeToInsertPoint(Point2D relative) {
        this.relativeToInsertPoint = relative;
    }

    @Override
    public GeneralPath getShape() {
        return shape;
    }

    @Override
    public void setShape(GeneralPath sh) {
        shape = sh;
    }

    @Override
    public void draw(Graphics2D g) {
        g.draw(shape);
    }

    @Override
    public void fill(Graphics2D g) {
        g.fill(shape);
    }
}
