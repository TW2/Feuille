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
package feuille.drawing.shape;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.List;

/**
 *
 * @author util2
 */
public interface IShape {
    
    public ShapeType getShapeType();
    public void setLayer(int layer);
    public int getLayer();
    public void setLayerColor(Color layerColor);
    public Color getLayerColor();
    public void setLayerName(String layerName);
    public String getLayerName();
    public void setPoints(List<FeuillePoint> points);
    public List<FeuillePoint> getPoints();
    public boolean isAtPoint(FeuillePoint fp);
    public void selectShape(Point2D p2d);
    public void draw(Graphics2D g2d, Dimension render, float scale);
    
}
