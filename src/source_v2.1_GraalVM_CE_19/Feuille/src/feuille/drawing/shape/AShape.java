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

import feuille.MainFrame;
import feuille.drawing.shape.vector.FeuilleCubicCurve;
import feuille.drawing.shape.vector.FeuilleQuadCurve;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author util2
 */
public abstract class AShape implements IShape {

    protected int uniqueID = 0;
    protected List<FeuillePoint> points = new ArrayList<>();
    protected List<AShape> group = new ArrayList<>();
    protected ShapeType shapeType = ShapeType.Group;
    protected int layer = 0;
    protected Color layerColor = Color.green;
    protected String layerName = MainFrame.getLanguage().getTranslated("ShapeDefaultName", MainFrame.getISOCountry(), "Default");
    protected FeuilleGlue selected = new FeuilleGlue();
    
    public AShape() {
        
    }

    public void setUniqueID(int uniqueID) {
        this.uniqueID = uniqueID;
    }

    public int getUniqueID() {
        return uniqueID;
    }

    public void setGroup(List<AShape> group) {
        this.group = group;
    }

    public List<AShape> getGroup() {
        return group;
    }
    
    public void addShapeToGroup(AShape sh){
        boolean add = true;
        for(AShape shape : group){
            if(shape.getUniqueID() == sh.getUniqueID()){
                add = false;
                break;
            }
        }
        if(add == true){
            group.add(sh);
        }
    }
    
    public void removeShapeToGroup(AShape sh){
        int index = -1;
        for(int i=0; i<group.size(); i++){
            AShape shape = group.get(i);
            if(shape.getUniqueID() == sh.getUniqueID()){
                index = i;
                break;
            }
        }
        if(index != -1){
            group.remove(index);
        }
    }
    
    public void clearGroup(){
        group.clear();
    }
    
    public boolean isMemberOfGroup(AShape sh){
        boolean member = false;
        for(AShape shape : group){
            if(shape.getUniqueID() == sh.getUniqueID()){
                member = true;
                break;
            }
        }
        return member;
    }

    @Override
    public void setPoints(List<FeuillePoint> points) {
        this.points = points;
    }

    @Override
    public List<FeuillePoint> getPoints() {
        return points;
    }
    
    public void addPoint(FeuillePoint fp){
        boolean add = true;
        for(FeuillePoint search : points){
            if(search.getPoint().getX() == fp.getPoint().getX()
                    && search.getPoint().getY() == fp.getPoint().getY()){
                add = false;
                break;
            }
        }
        if(add == true){
            points.add(fp);
        }
    }
    
    public void removePoint(FeuillePoint fp){
        int index = -1;
        for(int i=0; i<points.size(); i++){
            FeuillePoint search = points.get(i);
            if(search.getPoint().getX() == fp.getPoint().getX()
                    && search.getPoint().getY() == fp.getPoint().getY()){
                index = i;
                break;
            }
        }
        if(index != -1){
            points.remove(index);
        }
    }
    
    public void clearPoints(){
        points.clear();
    }
    
    public FeuillePoint getPoint(FeuillePoint.FeuillePointType type){
        FeuillePoint fp = null;
        for(FeuillePoint search : points){
            if(search.getType().equals(type) == true){
                fp = search;
                break;
            }
        }        
        return fp;
    }
    
    public FeuillePoint getPoint(int uniqueID){
        FeuillePoint fp = null;
        for(FeuillePoint search : points){
            if(search.getUniqueID() == uniqueID){
                fp = search;
                break;
            }
        }        
        return fp;
    }
    
    public static AShape cloner(AShape sh, Point2D startPoint, Point2D endPoint){
        AShape shape = FeuilleShapeInvokator.createShape(sh.getShapeType());
        
        // Check if the shape contains a start value
        if(sh.getPoints().size() > 0){
            FeuillePoint fp = sh.getPoint(FeuillePoint.FeuillePointType.Start);
            shape.addPoint(FeuillePoint.create(FeuillePoint.FeuillePointType.Start, 0, startPoint != null ? startPoint : fp.getPoint()));
        }
        
        // Check if the shape contains an end value
        if(sh.getPoints().size() > 1){
            FeuillePoint fp = sh.getPoint(FeuillePoint.FeuillePointType.End);
            shape.addPoint(FeuillePoint.create(FeuillePoint.FeuillePointType.End, 1, endPoint != null ? endPoint : fp.getPoint()));
        }
        
        // Check if the quadratic curve has one control point before cloning it
        if(sh.getShapeType() == ShapeType.QuadraticCurve && sh.getPoints().size() == 3){
            FeuillePoint fp = sh.getPoint(FeuillePoint.FeuillePointType.ControlPoint);
            shape.addPoint(FeuillePoint.create(FeuillePoint.FeuillePointType.ControlPoint, 2, fp.getPoint()));
        }else if(sh.getShapeType() == ShapeType.QuadraticCurve && sh.getPoints().size() > 1){
            Point2D start = startPoint != null ? startPoint : sh.getPoint(FeuillePoint.FeuillePointType.Start).getPoint();
            Point2D end = endPoint != null ? endPoint : sh.getPoint(FeuillePoint.FeuillePointType.End).getPoint();
            shape.addPoint(FeuillePoint.create(FeuillePoint.FeuillePointType.ControlPoint, 2, FeuilleQuadCurve.retrieveControlPoint(start, end)));
        }
        
        // Check if the cubic curve has two control points before cloning them
        if(sh.getShapeType() == ShapeType.CubicCurve && sh.getPoints().size() == 4){
            FeuillePoint fp1 = sh.getPoint(FeuillePoint.FeuillePointType.ControlPoint1);
            FeuillePoint fp2 = sh.getPoint(FeuillePoint.FeuillePointType.ControlPoint2);
            shape.addPoint(FeuillePoint.create(FeuillePoint.FeuillePointType.ControlPoint1, 2, fp1.getPoint()));
            shape.addPoint(FeuillePoint.create(FeuillePoint.FeuillePointType.ControlPoint2, 3, fp2.getPoint()));
        }else if(sh.getShapeType() == ShapeType.CubicCurve && sh.getPoints().size() > 1){
            Point2D start = startPoint != null ? startPoint : sh.getPoint(FeuillePoint.FeuillePointType.Start).getPoint();
            Point2D end = endPoint != null ? endPoint : sh.getPoint(FeuillePoint.FeuillePointType.End).getPoint();
            shape.addPoint(FeuillePoint.create(FeuillePoint.FeuillePointType.ControlPoint1, 2, FeuilleCubicCurve.retrieveControlPoint1(start, end)));
            shape.addPoint(FeuillePoint.create(FeuillePoint.FeuillePointType.ControlPoint2, 3, FeuilleCubicCurve.retrieveControlPoint2(start, end)));
        }
        
        return shape;
    }
    
    @Override
    public String toString() {
        return layerName + " - " + layer;
    }

    @Override
    public ShapeType getShapeType() {
        return shapeType;
    }

    @Override
    public void setLayer(int layer) {
        this.layer = layer;
    }

    @Override
    public int getLayer() {
        return layer;
    }

    @Override
    public void setLayerColor(Color layerColor) {
        this.layerColor = layerColor;
    }

    @Override
    public Color getLayerColor() {
        return layerColor;
    }

    @Override
    public void setLayerName(String layerName) {
        this.layerName = layerName;
    }

    @Override
    public String getLayerName() {
        return layerName;
    }

    public void setSelected(FeuilleGlue selected) {
        this.selected = selected;
    }

    public FeuilleGlue getSelected() {
        return selected;
    }
    
}
