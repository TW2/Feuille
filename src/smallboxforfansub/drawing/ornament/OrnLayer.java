/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smallboxforfansub.drawing.ornament;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author The Wingate 2940
 */
public class OrnLayer {
    
    java.awt.Point first = null;
    java.awt.Point last = null;
    List<smallboxforfansub.drawing.ornament.IShape> ashapes = new ArrayList<smallboxforfansub.drawing.ornament.IShape>();
    List<smallboxforfansub.drawing.ornament.IShape> change_shapes = new ArrayList<smallboxforfansub.drawing.ornament.IShape>();
    
    public OrnLayer(){
        
    }
    
    public void addShape(smallboxforfansub.drawing.ornament.IShape s){
        ashapes.add(s);
    }
    
    public void replaceShape(smallboxforfansub.drawing.ornament.IShape old, smallboxforfansub.drawing.ornament.IShape s){
        int index = ashapes.indexOf(old);
        ashapes.remove(old);
        ashapes.add(index, s);
    }
    
    public void removeShape(smallboxforfansub.drawing.ornament.IShape s){
        ashapes.remove(s);
    }
    
    public void clearShapes(){
        ashapes.clear();
        first = null;
        last = null;
    }
    
    public List<smallboxforfansub.drawing.ornament.IShape> getList(){
        return ashapes;
    }
    
    /** Configure le 1er point. */
    public void setFirstPoint(java.awt.Point first){
        this.first = first;
    }
    
    /** Obtient le 1er point. */
    public java.awt.Point getFirstPoint(){
        return first;
    }
    
    /** Configure le 2eme point. */
    public void setLastPoint(java.awt.Point last){
        this.last = last;
    }
    
    /** Obtient le 2eme point. */
    public java.awt.Point getLastPoint(){
        return last;
    }
    
    public void updateFirstAndLastPoint(){
        if(ashapes.isEmpty()==false){
            first = ashapes.get(0).getOriginPoint();
            last = ashapes.get(ashapes.size()-1).getLastPoint();
        }
    }
    
    public void setChangeList(List<smallboxforfansub.drawing.ornament.IShape> change_shapes){
        this.change_shapes = change_shapes;
    }
    
    public List<smallboxforfansub.drawing.ornament.IShape> getChangeList(){
        return change_shapes;
    }
    
    public List<smallboxforfansub.drawing.ornament.IShape> getShapesAtPoint(java.awt.Point p){
        // Get all points at coordinate
        List<smallboxforfansub.drawing.ornament.IShape> pointlist = new ArrayList<smallboxforfansub.drawing.ornament.IShape>();
        for (smallboxforfansub.drawing.ornament.IShape s : ashapes){
            if (s instanceof OrnPoint){
                OrnPoint point = (OrnPoint)s;
                if (point.isPointisinRectangle(p)){
                    s.setMarked(true);
                    pointlist.add(s);
                }
            }
            if (s instanceof OrnMMLine){
                OrnMMLine line = (OrnMMLine)s;
                if (line.isPointisNear(p)){
                    s.setMarked(true);
                    pointlist.add(s);
                }
            }
            if (s instanceof OrnSMLine){
                OrnSMLine line = (OrnSMLine)s;
                if (line.isPointisNear(p)){
                    s.setMarked(true);
                    pointlist.add(s);
                }
            }
            if (s instanceof OrnMMBezier){
                OrnMMBezier bezier = (OrnMMBezier)s;
                if (bezier.isPointisNear(p)){
                    s.setMarked(true);
                    pointlist.add(s);
                }
            }
            if (s instanceof OrnSMBezier){
                OrnSMBezier bezier = (OrnSMBezier)s;
                if (bezier.isPointisNear(p)){
                    s.setMarked(true);
                    pointlist.add(s);
                }
            }
            if (s instanceof OrnControlPoint){
                OrnControlPoint cp = (OrnControlPoint)s;
                if (cp.isPointisinRectangle(p)){
                    for (Iterator<IShape> it = ashapes.iterator(); it.hasNext();) {
                        smallboxforfansub.drawing.ornament.IShape search = it.next();
                        if (search instanceof OrnMMBezier){
                            OrnMMBezier bezier = (OrnMMBezier)search;
                            if(bezier.getControl1().equals(cp)){
                                search.setMarked(true);
//                                pointlist.add(bezier.getControl2());
                                pointlist.add(search);
                            }else if(bezier.getControl2().equals(cp)){
                                search.setMarked(true);
//                                pointlist.add(bezier.getControl1());
                                pointlist.add(search);
                            }
                        }
                        if (search instanceof OrnSMBezier){
                            OrnSMBezier bezier = (OrnSMBezier)search;
                            if(bezier.getControl1().equals(cp)){
                                search.setMarked(true);
//                                pointlist.add(bezier.getControl2());
                                pointlist.add(search);
                            }else if(bezier.getControl2().equals(cp)){
                                search.setMarked(true);
//                                pointlist.add(bezier.getControl1());
                                pointlist.add(search);
                            }
                        }
                    }
                    s.setMarked(true);
                    pointlist.add(s);
                }
            }
//            if (s instanceof smallboxforfansub.drawing.BSpline){
//                smallboxforfansub.drawing.BSpline bs = (smallboxforfansub.drawing.BSpline)s;
//                if (bs.isPointisNear(p)){
//                    s.setMarked(true);
//                    pointlist.add(s);
//                }
//            }
        }
        return pointlist;
    }
    
    public smallboxforfansub.drawing.ornament.IShape getLastShape(){
        return ashapes.get(ashapes.size()-1);
    }
    
    public smallboxforfansub.drawing.ornament.IShape getFirstShape(){
        return ashapes.get(0);
    }
    
    public OrnPoint getLastPointOfShapes(){
        for (int i=ashapes.size()-1; i>=0; i--){
            smallboxforfansub.drawing.ornament.IShape s = ashapes.get(i);
            if(s instanceof OrnPoint){
                return (OrnPoint)s;
            }
        }
        return null;
    }
}
