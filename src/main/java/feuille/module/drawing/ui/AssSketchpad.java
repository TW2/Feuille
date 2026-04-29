package feuille.module.drawing.ui;

import feuille.module.drawing.AssCanvas;
import feuille.module.drawing.SelectedTool;
import feuille.util.Loader;

import javax.swing.*;
import java.awt.*;

public class AssSketchpad extends JPanel {

    private final AssCanvas canvas;
    private final JToolBar shapeToolbar;
    private SelectedTool selectedTool = SelectedTool.Line;

    public AssSketchpad() {
        setLayout(new BorderLayout());

        canvas = new AssCanvas(this);
        add(canvas, BorderLayout.CENTER);

        shapeToolbar = new JToolBar(JToolBar.VERTICAL);
        populateShapeToolbar();
        add(shapeToolbar, BorderLayout.WEST);
    }

    public SelectedTool getSelectedTool() {
        return selectedTool;
    }

    public AssCanvas getCanvas() {
        return canvas;
    }

    private void populateShapeToolbar(){
        ButtonGroup bgShape = new ButtonGroup();

        JToggleButton line = new JToggleButton(
                Loader.fromResource("/images/32-feuille-line.png", 32, 32));
        line.setToolTipText(Loader.language("shape.line", "Line"));
        line.addActionListener(e -> selectedTool = SelectedTool.Line);
        shapeToolbar.add(line);
        bgShape.add(line);

        JToggleButton q = new JToggleButton(
                Loader.fromResource("/images/32-feuille-q.png", 32, 32));
        q.setToolTipText(Loader.language("shape.quadratic", "Quadratic Bézier"));
        q.addActionListener(e -> selectedTool = SelectedTool.Quadratic);
        shapeToolbar.add(q);
        bgShape.add(q);

        JToggleButton c = new JToggleButton(
                Loader.fromResource("/images/32-feuille-c.png", 32, 32));
        c.setToolTipText(Loader.language("shape.cubic", "Quadratic Bézier"));
        c.addActionListener(e -> selectedTool = SelectedTool.Cubic);
        shapeToolbar.add(c);
        bgShape.add(c);

        JToggleButton s = new JToggleButton(
                Loader.fromResource("/images/32-feuille-s.png", 32, 32));
        s.setToolTipText(Loader.language("shape.spline", "Bézier Spline"));
        s.addActionListener(e -> selectedTool = SelectedTool.Spline);
        shapeToolbar.add(s);
        bgShape.add(s);

        line.setSelected(true);
    }
}
