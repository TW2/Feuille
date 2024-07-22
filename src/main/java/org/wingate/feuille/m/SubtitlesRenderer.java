/*
 * Copyright (C) 2024 util2
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
package org.wingate.feuille.m;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import org.wingate.feuille.ass.AssEvent;

/**
 *
 * @author util2
 */
public class SubtitlesRenderer {
    
    public SubtitlesRenderer() {
    }
    
    public static BufferedImage get(int width, int height, double seconds, AssEvent ev){
        if(width == 0) width = 1280;
        if(height == 0) height = 720;
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        Font oldFont = g.getFont();
        Font font = ev.getStyle().getFont().deriveFont(ev.getStyle().getFont().getSize2D() * 2f);
        Shape outline = generateOutline(font, ev.getText());
        Shape inner, outer;
        
        inner = getInner(outline);
        outer = getOuter(outline, (float)ev.getStyle().getOutline() * 4f);        
        
        AffineTransform transform = new AffineTransform();
        transform.translate(
                (width - inner.getBounds2D().getWidth()) / 2,
                height - outer.getBounds2D().getHeight() - 100
        );
        
        AffineTransform oldTransform = g.getTransform();
        
        g.setTransform(transform);

//        // On dessine l'intérieur
//        g.setColor(Color.white);
//        g.fill(inner);

        // On dessine l'extérieur
        g.setColor(Color.white);
        g.fill(outer);
        
        g.setTransform(oldTransform);
        g.setFont(oldFont);
        
        g.dispose();
        return img;
    }
    
    public static Shape generateOutline(Font font, String text){
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        TextLayout textLayout = new TextLayout(text, font, g.getFontRenderContext());
        Shape outline = textLayout.getOutline(null);
        g.dispose();
        return outline;
    }
    
    public static Shape getOuter(Shape outline, float size){
        size = 100f;
        
        // Calcul du centre
        Rectangle2D boundaries = outline.getBounds2D();
        Point2D center = new Point2D.Double(boundaries.getCenterX(), boundaries.getCenterY());
        
        // On applique la taille en pixels de la bordure
        GeneralPath gp = new GeneralPath(PathIterator.WIND_EVEN_ODD);
        for(PathIterator pi = outline.getPathIterator(null); !pi.isDone(); pi.next()){
            double[] coords = new double[6];
            int type = pi.currentSegment(coords);
            
            Point2D p0 = new Point2D.Double(coords[0], coords[1]);
            Point2D p1 = new Point2D.Double(coords[2], coords[3]);
            Point2D p2 = new Point2D.Double(coords[4], coords[5]);
            
            switch(type){
                case PathIterator.SEG_MOVETO -> {
                    Point2D O = Geometry.resize(center, p0, size);
                    gp.moveTo(O.getX(), O.getY());
                }
                case PathIterator.SEG_LINETO,
                        PathIterator.SEG_CLOSE -> {
                    Point2D A = Geometry.resize(center, p0, size);
                    gp.lineTo(A.getX(), A.getY());
                }
                case PathIterator.SEG_QUADTO -> {
                    Point2D A = Geometry.resize(center, p0, size);
                    Point2D B = Geometry.resize(center, p1, size);
                    gp.quadTo(A.getX(), A.getY(), B.getX(), B.getY());
                }
                case PathIterator.SEG_CUBICTO -> {
                    Point2D A = Geometry.resize(center, p0, size);
                    Point2D B = Geometry.resize(center, p1, size);
                    Point2D C = Geometry.resize(center, p2, size);
                    gp.curveTo(A.getX(), A.getY(), B.getX(), B.getY(), C.getX(), C.getY());
                }
            }
        }
        
        // On sépare la bordure de l'intérieur de la forme
        Area plain = new Area(outline);
        Area border = new Area(gp);
        
        
        return gp;
    }
    
    public static Shape getInner(Shape outline){
        return outline;
    }
}
