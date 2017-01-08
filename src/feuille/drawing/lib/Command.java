/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.drawing.lib;

import feuille.drawing.lib.Bezier;
import feuille.drawing.lib.BSpline;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author The Wingate 2940
 */
public class Command {
    
    List<Integer> xList = new ArrayList<Integer>();
    List<Integer> yList = new ArrayList<Integer>();
    Type type = Type.Void;
    
    public enum Type{
        Void, ReStart, Line, Bezier, BSpline, Close, Extend, Move;
    }
    
    public Command(Type type){
        this.type = type;
    }
    
    public void add_X(int x){
        xList.add(x);
    }
    
    public void add_Y(int y){
        yList.add(y);
    }
    
    public Type getType(){
        return type;
    }
    
    public Point getStartPoint(){
        try{
            return new Point(xList.get(0), yList.get(0));
        }catch(Exception e){
            System.out.println("There is a problem with your start point"
                    + " (command = m X Y)");
            return null;
        }
    }
    
    public ReStart getStartPoint(int last_x, int last_y){
        try{
            ReStart m = new ReStart();
            m.setOriginPoint(last_x, last_y);
            m.setLastPoint(xList.get(0), yList.get(0));
            return m;
        }catch(Exception e){
            System.out.println("There is a problem with your start point"
                    + " (command = m X Y)");
            return null;
        }
    }
    
    public Line getLine(int last_x, int last_y){
        try{
            Line l = new Line();
            l.setOriginPoint(last_x, last_y);
            l.setLastPoint(xList.get(0), yList.get(0));
            return l;
        }catch(Exception e){
            System.out.println("There is a problem with your line shape"
                    + " (command = l X Y)");
            return null;
        }
    }
    
    public Bezier getBezier(int last_x, int last_y){
        try{
            Bezier b = new Bezier();
            b.setOriginPoint(last_x, last_y);
            b.setControl1Point(xList.get(0), yList.get(0));
            b.setControl2Point(xList.get(1), yList.get(1));
            b.setLastPoint(xList.get(2), yList.get(2));
            return b;
        }catch(Exception e){
            System.out.println("There is a problem with your bezier shape"
                    + " (command = b X Y X Y X Y)");
            return null;
        }        
    }
    
    public BSpline getBSpline(int last_x, int last_y){
        try{
            BSpline bs = new BSpline(last_x, last_y);
            int xc = last_x; int yc = last_y;
            for(int i=0;i<xList.size();i++){
                if(xc!=xList.get(i) | yc!=yList.get(i)){
                    bs.addPoint(xList.get(i), yList.get(i));
                }
                xc = xList.get(i); yc = yList.get(i);                
            }
            return bs;
        }catch(Exception e){
            System.out.println("There is a problem with your bspline shape"
                    + " (command = s Xa Ya Xb Yb Xc Yc ... Xn Yn)");
            return null;
        }
        
    }
    
    public Point getExtendPoint(){
        try{
            return new Point(xList.get(0), yList.get(0));
        }catch(Exception e){
            System.out.println("There is a problem with your extend bspline"
                    + " point shape (command = p X Y)");
            return null;
        }
    }
    
    public Move getMove(int last_x, int last_y){
        try{
            Move n = new Move();
            n.setOriginPoint(last_x, last_y);
            n.setLastPoint(xList.get(0), yList.get(0));
            return n;
        }catch(Exception e){
            System.out.println("There is a problem with your move shape"
                    + " (command = n X Y)");
            return null;
        }
    }
}
