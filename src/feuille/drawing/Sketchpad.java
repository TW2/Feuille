/*
 * Copyright (C) 2019 util2
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package feuille.drawing;

import feuille.drawing.shape.AShape;
import feuille.drawing.shape.FeuillePoint;
import feuille.drawing.shape.FeuilleShapeInvokator;
import feuille.drawing.shape.ShapeType;
import feuille.drawing.shape.vector.FeuilleBSplineCurve;
import feuille.drawing.shape.vector.FeuilleCubicCurve;
import feuille.drawing.shape.vector.FeuilleLine;
import feuille.drawing.shape.vector.FeuilleQuadCurve;
import feuille.panel.Draw;
import feuille.util.DrawColor;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

/**
 *
 * @author util2
 */
public class Sketchpad extends JPanel {
    
    private Draw draw = null;
    
    private boolean showShape = true;
    
    private Color minUnitColor = DrawColor.violet.getColor();
    private Color medUnitColor = DrawColor.maroon.getColor();
    private Color maxUnitColor = DrawColor.black.getColor();
    
    private float scale = 1f; // from 1f to 10f
    private static final int UNIT = 50;
    
    private AShape currentShape = null;
    private Point userLocation = new Point(0, 0);
    private Point cursor = new Point(0, 0);
    private FeuillePoint cursorLastUnknown = FeuillePoint.create(FeuillePoint.FeuillePointType.Unknown, -1);
    private ShapeType toolInCourse = ShapeType.Unknown;

    public Sketchpad(Draw draw) {
        this.draw = draw;
        init();
    }
    
    private void init(){
        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if(e.getWheelRotation() < 0 && scale < 10f){                    
                    scale += 0.5f;
                    if(scale > 10f) { scale = 10f; }
                }else if(e.getWheelRotation() > 0 && scale > 1f){
                    scale -= 0.5f;
                    if(scale < 1f) { scale = 1f; }
                }
                draw.setScale(scale);
                repaint();
            }
        });
        
        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseMoved(MouseEvent e) {
                cursor = e.getPoint();
                draw.setCoordinates(
                        Math.round((e.getX() - getWidth()/2) / scale), 
                        Math.round((e.getY() - getHeight()/2) / scale));
                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                cursor = e.getPoint();
                draw.setCoordinates(
                        Math.round((e.getX() - getWidth()/2) / scale), 
                        Math.round((e.getY() - getHeight()/2) / scale));
                repaint();
            }
        });
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(toolInCourse != ShapeType.Unknown){
                    addPoint(e.getPoint());
                }                
            }
        });
    }
    
    public void setScale(float scale){
        this.scale = scale;
        repaint();
    }

    public void setToolInCourse(ShapeType toolInCourse) {
        this.toolInCourse = toolInCourse;
    }
    
    public void recall(){
        if(cursorLastUnknown.getUniqueID() == -1){
            return;
        }
        showShape = true;
        Point2D pScaled = new Point2D.Double(cursorLastUnknown.getPoint().getX() * scale, cursorLastUnknown.getPoint().getY() * scale);
        
        AShape as = FeuilleShapeInvokator.createShape(currentShape.getShapeType(), currentShape.getPoints().get(0).getPoint(), pScaled);
        currentShape = FeuilleShapeInvokator.changeShapeType(as, toolInCourse, pScaled);
        
        repaint();
    }
    
    public void stop(){
        showShape = false;
        repaint();
    }
    
    public void addPoint(Point point){
        // Example of coordinate that works >> (cursor.x - getWidth()/2) / scale)
        Point2D pUnScaled = new Point2D.Double((point.x - getWidth()/2) / scale, (point.y - getHeight()/2) / scale);
        cursorLastUnknown.setPoint(pUnScaled);
        cursorLastUnknown.setUniqueID(-1000);
        if(currentShape == null){
            // We don't have anything, let's create a new one with toolInCourse (which is a ShapeType)
            if(toolInCourse != ShapeType.Unknown){
                currentShape = FeuilleShapeInvokator.createShape(toolInCourse, pUnScaled); // Start point only
                
                repaint();
            }            
        }else{
            // We have a start point because currentShape != null and we want a complete shape
            if(toolInCourse != ShapeType.Unknown){
                // Treatment of current shape
                currentShape.addPoint(FeuillePoint.create(FeuillePoint.FeuillePointType.End, 1, pUnScaled));
                draw.getHistory().addItem(HistoryItemType.getFromVector(toolInCourse), currentShape);
                draw.getHistoryList().updateUI();
                
                // Preview to see the future shape line to pointer
                currentShape = FeuilleShapeInvokator.createShape(toolInCourse, pUnScaled); // Start point only
                
                repaint();
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        Stroke oldStroke = g2d.getStroke();
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        // Let's start with rules
        // The sketchpad visible surface is not defined
        // For one rule:
        // -> 1 minor unit each 50px
        // -> 1 median unit each 100px
        // -> 1 major unit each 500px
        // UNIT is 50px
        drawVertical(g2d, UNIT);
        drawVertical(g2d, -UNIT);
        drawHorizontal(g2d, UNIT);
        drawHorizontal(g2d, -UNIT);
        
        // Draw currentShape first point and preview        
        if(currentShape != null && showShape == true){
            for(FeuillePoint fp : currentShape.getPoints()){
                fp.draw(g2d, getSize(), scale);
            }
            
            // Example : (point.x - getWidth()/2) / scale
            Point2D p = new Point2D.Double(
                    (cursor.getX() - getWidth()/2) / scale,
                    (cursor.getY() - getHeight()/2) / scale);
            AShape copy = AShape.cloner(currentShape, null, p);
            AShape previewShape = FeuilleShapeInvokator.changeShapeType(copy, toolInCourse, p);
            
            previewShape.draw(g2d, getSize(), scale);
        }
        
        // Draw elements
        for(AShape shape : draw.getHistory().getListOfShapes()){            
            shape.draw(g2d, getSize(), scale);
        }
        
        //Dessine les axes correspondant au curseur de la souris.
        g2d.setColor(Color.pink);
        g2d.drawLine(cursor.x, 0, cursor.x, getHeight());
        g2d.drawLine(0, cursor.y, getWidth(), cursor.y);

        //Dessine les coordonnées près des axes correspondant au curseur de la souris.
        g2d.drawString(
                ((cursor.x - getWidth()/2) / scale) + ";"
                +((cursor.y - getHeight()/2) / scale), cursor.x + 5, cursor.y - 5);
        
        //-----------------------------------------------
        // Test pour aligner :
//        g2d.setColor(DrawColor.dark_green.getColor());        
//        Rectangle2D r = new Rectangle2D.Double(
//                (100 * scale + getWidth() / 2) - 15d,
//                (100 * scale + getHeight() / 2) - 15d,
//                30d,
//                30d);
//        g2d.fill(r);
//        g2d.setColor(DrawColor.violet.getColor());
//        r = new Rectangle2D.Double(
//                (100 * scale + getWidth() / 2) - 15d,
//                (-100 * scale + getHeight() / 2) - 15d,
//                30d,
//                30d);
//        g2d.fill(r);
        //-----------------------------------------------
        
        g2d.setStroke(oldStroke);
    }
    
    private void drawVertical(Graphics2D g2d, int unit){
        int xOrigin = getWidth() / 2 + userLocation.x;
        
        int xValue = xOrigin;
        int graduation = 0;
        
        // North rule and South rule (taking care of scale)
        FontMetrics fm = g2d.getFontMetrics();
        while(xValue >= Math.abs(unit) & xValue <= getWidth() - Math.abs(unit)){
            // Set text and calculation of width
            String graduationNumber = Integer.toString(graduation);
            int xGraduationNumber = xValue - fm.stringWidth(graduationNumber) / 2;
            
            // X
            if(isMultiple(graduation, 500f) == true && xValue != xOrigin){
                //==============================================================
                // 500px
                //--------------------------------------------------------------
                g2d.setStroke(new BasicStroke(1f));
                g2d.setColor(Color.cyan);
                // From TOP to BOTTOM
                g2d.drawLine(xValue, 0, xValue, getHeight());
                //---
                g2d.setStroke(new BasicStroke(3f));
                g2d.setColor(maxUnitColor);
                // TOP
                g2d.drawLine(xValue, 0, xValue, 20);
                g2d.drawString(graduationNumber, xGraduationNumber, 34);
                // BOTTOM
                g2d.drawLine(xValue, getHeight() - 20, xValue, getHeight());
                g2d.drawString(graduationNumber, xGraduationNumber, getHeight() - 7 - fm.getHeight());
            }else if(isMultiple(graduation, 100f) == true && xValue != xOrigin){
                //==============================================================
                // 100px
                //--------------------------------------------------------------
                g2d.setStroke(new BasicStroke(1f));
                g2d.setColor(Color.cyan);
                // From TOP to BOTTOM
                g2d.drawLine(xValue, 0, xValue, getHeight());
                //---
                g2d.setStroke(new BasicStroke(3f));
                g2d.setColor(medUnitColor);
                // TOP
                g2d.drawLine(xValue, 0, xValue, 15);
                g2d.drawString(graduationNumber, xGraduationNumber, 34);
                // BOTTOM
                g2d.drawLine(xValue, getHeight() - 15, xValue, getHeight());
                g2d.drawString(graduationNumber, xGraduationNumber, getHeight() - 7 - fm.getHeight());
            }else if(xValue != xOrigin){
                //==============================================================
                // 50px
                //--------------------------------------------------------------
                g2d.setStroke(new BasicStroke(1f));
                g2d.setColor(Color.cyan);
                // From TOP to BOTTOM
                g2d.drawLine(xValue, 0, xValue, getHeight());
                //---
                g2d.setStroke(new BasicStroke(3f));
                g2d.setColor(minUnitColor);
                // TOP
                g2d.drawLine(xValue, 0, xValue, 10);
                g2d.drawString(graduationNumber, xGraduationNumber, 34);
                // BOTTOM
                g2d.drawLine(xValue, getHeight() - 10, xValue, getHeight());
                g2d.drawString(graduationNumber, xGraduationNumber, getHeight() - 7 - fm.getHeight());
            }else{
                g2d.setStroke(new BasicStroke(3f));
                g2d.setColor(Color.cyan);
                // From TOP to BOTTOM
                g2d.drawLine(xValue, 0, xValue, getHeight());
            }
            
            xValue += unit * scale;
            graduation += unit;
        }
    }
    
    private void drawHorizontal(Graphics2D g2d, int unit){
        int yOrigin = getHeight() / 2 + userLocation.y;
        
        int yValue = yOrigin;
        int graduation = 0;
        
        // West rule and East rule (taking care of scale)
        FontMetrics fm = g2d.getFontMetrics();
        while(yValue >= Math.abs(unit) & yValue <= getHeight() - Math.abs(unit)){
            // Set text and calculation of width
            String graduationNumber = Integer.toString(graduation);
            int wordWidth = fm.stringWidth(graduationNumber);
            
            // Y
            if(isMultiple(graduation, 500f) == true && yValue != yOrigin){
                //==============================================================
                // 500px
                //--------------------------------------------------------------
                g2d.setStroke(new BasicStroke(1f));
                g2d.setColor(Color.cyan);
                // From TOP to BOTTOM
                g2d.drawLine(0, yValue, getWidth(), yValue);
                //---
                g2d.setStroke(new BasicStroke(3f));
                g2d.setColor(maxUnitColor);
                // TOP
                g2d.drawLine(0, yValue, 20, yValue);
                g2d.drawString(graduationNumber, 24, yValue + fm.getHeight() / 3);
                // BOTTOM
                g2d.drawLine(getWidth() - 20, yValue, getWidth(), yValue);
                g2d.drawString(graduationNumber, getWidth() - 24 - wordWidth, yValue + fm.getHeight() / 3);
            }else if(isMultiple(graduation, 100f) == true && yValue != yOrigin){
                //==============================================================
                // 100px
                //--------------------------------------------------------------
                g2d.setStroke(new BasicStroke(1f));
                g2d.setColor(Color.cyan);
                // From TOP to BOTTOM
                g2d.drawLine(0, yValue, getWidth(), yValue);
                //---
                g2d.setStroke(new BasicStroke(3f));
                g2d.setColor(medUnitColor);
                // TOP
                g2d.drawLine(0, yValue, 15, yValue);
                g2d.drawString(graduationNumber, 24, yValue + fm.getHeight() / 3);
                // BOTTOM
                g2d.drawLine(getWidth() - 15, yValue, getWidth(), yValue);
                g2d.drawString(graduationNumber, getWidth() - 24 - wordWidth, yValue + fm.getHeight() / 3);
            }else if(yValue != yOrigin){
                //==============================================================
                // 50px
                //--------------------------------------------------------------
                g2d.setStroke(new BasicStroke(1f));
                g2d.setColor(Color.cyan);
                // From TOP to BOTTOM
                g2d.drawLine(0, yValue, getWidth(), yValue);
                //---
                g2d.setStroke(new BasicStroke(3f));
                g2d.setColor(minUnitColor);
                // TOP
                g2d.drawLine(0, yValue, 10, yValue);
                g2d.drawString(graduationNumber, 24, yValue + fm.getHeight() / 3);
                // BOTTOM
                g2d.drawLine(getWidth() - 10, yValue, getWidth(), yValue);
                g2d.drawString(graduationNumber, getWidth() - 24 - wordWidth, yValue + fm.getHeight() / 3);
            }else{
                g2d.setStroke(new BasicStroke(3f));
                g2d.setColor(Color.cyan);
                // From TOP to BOTTOM
                g2d.drawLine(0, yValue, getWidth(), yValue);
            }
            
            yValue += unit * scale;
            graduation += unit;
        }
    }
    
    private boolean isMultiple(float number, float unit){
        float result = number % unit;
        return result == 0f;
    }
    
}
