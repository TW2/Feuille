package feuille.module.drawing;

import feuille.util.Loader;

import java.awt.*;

public class LineShape extends AShape {

    public LineShape() {
        name = Loader.language("shape.line", "Line");
    }

    @Override
    public void draw(Graphics2D g) {
        // Draw changes
        if(changedStartPoint != null || changedEndPoint != null){
            if(changedStartPoint != null && changedEndPoint != null){
                drawChangedLine(g, cs(changedStartPoint), cs(changedEndPoint));
            }else if(changedEndPoint == null){
                drawChangedLine(g, cs(changedStartPoint), cs(endPoint));
            }else{
                drawChangedLine(g, cs(startPoint), cs(changedEndPoint));
            }
        }

        // Draw line (no changes)
        drawLine(g, cs(startPoint), cs(endPoint));
    }
}
