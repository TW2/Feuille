package feuille.module.editor.assa.render;

import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

public class Drawing extends AGraphicElement {

    public Drawing() {
        graphicType = GraphicType.Drawing;
    }

    @Override
    public void setShape(GeneralPath sh) {
        size = sh.getBounds().getSize();
        double x = sh.getBounds2D().getX();
        double y = sh.getBounds2D().getY();
        insertPoint = new Point2D.Double(x, y);
        relativeToInsertPoint = new Point2D.Double();
        shape = sh;
    }
}
