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
import java.awt.Color;

/**
 *
 * @author The Wingate 2940
 */
public class Remember{
    
    boolean isActive = false; //Pour savoir si une forme est annulé ou non.
    IShape s = null;
    
    public Remember(){
        
    }
    
    public Remember(IShape s){
        this.s = s;
    }
    
    public void setActive(boolean isActive){
        this.isActive = isActive;
    }
    
    public boolean isActive(){
        return isActive;
    }
    
    public Color getColorShape(){
        return getColorShape(null);
    }
    
    public Color getColorShape(IShape next){
        if(next==null){
            next = s;
        }
        if(s instanceof ReStart){
            return new Color(255,211,191);
        }
        if(s instanceof Move){
            return new Color(230,201,255);
        }
        if(s instanceof Line){
            return new Color(255,173,173);
        }
        if(s instanceof Bezier){
            return new Color(255,178,242);
        }
        if(s instanceof BSpline){
            return new Color(147,155,255);
        }
        if(s instanceof Point && next instanceof Line){
            return new Color(255,191,191);
        }
        if(s instanceof Point && next instanceof Bezier){
            return new Color(255,198,242);
        }
        if(s instanceof Point && next instanceof BSpline){
            return new Color(167,175,255);
        }
        if(s instanceof ControlPoint){
            return new Color(255,198,242);
        }
        return Color.white;
    }
    
    public String getName(){
        if(s instanceof ReStart){
            return "ReStart";
        }
        if(s instanceof Move){
            return "Move";
        }
        if(s instanceof Line){
            return "Line";
        }
        if(s instanceof Bezier){
            return "Bézier";
        }
        if(s instanceof BSpline){
            return "BSpline";
        }
        if(s instanceof Point){
            return "Point";
        }
        if(s instanceof ControlPoint){
            return "Control point";
        }
        return "???";
    }
    
    @Override
    public String toString(){
        return getName();
    }
    
    public IShape getShape(){
        return s;
    }
}
