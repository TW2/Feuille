/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.grafx;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author util2
 */
public abstract class ACurveShape extends AShape {

    private List<Point2D> controlPoints = new ArrayList<>();
    
    public ACurveShape() {
    }

    public List<Point2D> getControlPoints() {
        return controlPoints;
    }

    public void setControlPoints(List<Point2D> controlPoints) {
        this.controlPoints = controlPoints;
    }
    
    public void addControlPoint(Point2D p2d){
        controlPoints.add(p2d);
    }
    
    public void insertControlPoint(Point2D p2d, int index) {
        try{
            controlPoints.set(index, p2d);
        }catch(IndexOutOfBoundsException ex){
            
        }        
    }
    
    public void removeLastControlPoint(){
        if(controlPoints.isEmpty() == false){
            controlPoints.remove(controlPoints.size() - 1);
        }
    }
    
    public void removeControlPoint(int index){
        try{
            controlPoints.remove(index);
        }catch(IndexOutOfBoundsException ex){
            
        }
    }
    
    public void clearControlPoints(){
        controlPoints.clear();
    }
    
    public void alterControlPoint(Point2D p2d, int index){
        List<Point2D> cps = new ArrayList<>();
        for(int i = 0; i<controlPoints.size(); i++){
            cps.add(i == index ? p2d : controlPoints.get(i));
        }
        controlPoints = cps;
    }

    public int getControlPointsCount(){
        return controlPoints.size();
    }
}
