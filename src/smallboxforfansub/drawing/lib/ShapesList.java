/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package smallboxforfansub.drawing.lib;

import smallboxforfansub.drawing.lib.ReStart;
import smallboxforfansub.drawing.lib.Point;
import smallboxforfansub.drawing.lib.Move;
import smallboxforfansub.drawing.lib.Line;
import smallboxforfansub.drawing.lib.IShape;
import smallboxforfansub.drawing.lib.ControlPoint;
import smallboxforfansub.drawing.lib.Bezier;
import smallboxforfansub.drawing.lib.BSpline;
import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe sert de liste de formes. Elle est appellée par la méthode de
 * dessin afin de pouvoir redessinner toutes les formes à l'écran.
 * @author The Wingate 2940
 */
public class ShapesList {

    //L'objet contenant toutes les formes dans une liste.
    List<IShape> slist = new ArrayList<IShape>();

    /** Ajoute une forme à la liste. */
    public void addShape(IShape s){
        slist.add(s);
    }

    /** Supprime la dernière forme de la liste. */
    public void removeLastShape(){
        slist.remove(slist.size()-1);
    }

    /** Renvoie la dernière forme de la liste. */
    public IShape getLastShape(){
        return slist.get(slist.size()-1);
    }

    /** Renvoie le dernier élément "point" de la liste. */
    public Point getLastPoint(){
        for(int i = slist.size()-1; i >= 0; i--){
            if(slist.get(i) instanceof Point){
                return (Point)slist.get(i);
            }
        }
        return null;
    }

    /** Renvoie le premier élément "point" de la liste. */
    public Point getFirstPoint(){
        for(int i = 0; i < slist.size(); i++){
            if(slist.get(i) instanceof Point){
                return (Point)slist.get(i);
            }
        }
        return null;
    }

    /** Enlève la forme demandée de la liste. */
    public void removeShape(IShape s){
        slist.remove(s);
    }

    /** Vide la liste des formes. */
    public void removeAllShapes(){
        slist.clear();
    }

    /** Remplace une forme par une autre. */
    public void replaceShape(IShape s1, IShape s2){
        int sindex = slist.indexOf(s1);
        slist.remove(sindex);
        slist.add(sindex, s2);
    }

    /** Obtient toutes les formes de la liste. */
    public List<IShape> getShapes(){
        return slist;
    }

    /** Crée une liste des éléments contenu à la coordonnée. */
    public List<IShape> getShapesAtPoint(java.awt.Point p){
        // Get all points at coordinate
        List<IShape> pointlist = new ArrayList<IShape>();
        for (IShape s : slist){
            if (s instanceof Point){
                Point point = (Point)s;
                if (point.isPointisinRectangle(p)){
                    s.setMarked(true);
                    pointlist.add(s);
                }
            }
            if (s instanceof Line){
                Line line = (Line)s;
                if (line.isPointisNear(p)){
                    s.setMarked(true);
                    pointlist.add(s);
                }
            }
            if (s instanceof Bezier){
                Bezier bezier = (Bezier)s;
                if (bezier.isPointisNear(p)){
                    s.setMarked(true);
                    pointlist.add(s);
                }
            }
            if (s instanceof ControlPoint){
                ControlPoint cp = (ControlPoint)s;
                if (cp.isPointisinRectangle(p)){
                    for (IShape search : slist){
                        if (search instanceof Bezier){
                            Bezier bezier = (Bezier)search;
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
            if (s instanceof BSpline){
                BSpline bs = (BSpline)s;
                if (bs.isPointisNear(p)){
                    s.setMarked(true);
                    pointlist.add(s);
                }
            }
            if (s instanceof Move){
                Move move = (Move)s;
                if (move.isPointisNear(p)){
                    s.setMarked(true);
                    pointlist.add(s);
                }
            }
            if (s instanceof ReStart){
                ReStart move = (ReStart)s;
                if (move.isPointisNear(p)){
                    s.setMarked(true);
                    pointlist.add(s);
                }
            }
        }
        return pointlist;
    }
    
    /** Obtient le nombre de Shape dans cette liste. */
    public int getSize(){
        return slist.size();
    }
    
    public List<IShape> getCopiesList(){
        List<IShape> newList = new ArrayList<IShape>();
        for(IShape s : slist){
            if(s instanceof BSpline){
                BSpline shape = (BSpline)s;
                newList.add((BSpline)shape.clone());
            }else if(s instanceof Bezier){
                Bezier shape = (Bezier)s;
                newList.add((Bezier)shape.clone());
            }else if(s instanceof ControlPoint){
                ControlPoint shape = (ControlPoint)s;
                newList.add((ControlPoint)shape.clone());
            }else if(s instanceof Line){
                Line shape = (Line)s;
                newList.add((Line)shape.clone());
            }else if(s instanceof Move){
                Move shape = (Move)s;
                newList.add((Move)shape.clone());
            }else if(s instanceof Point){
                Point shape = (Point)s;
                newList.add((Point)shape.clone());
            }else if(s instanceof ReStart){
                ReStart shape = (ReStart)s;
                newList.add((ReStart)shape.clone());
            }
        }
        return newList;
    }
}
