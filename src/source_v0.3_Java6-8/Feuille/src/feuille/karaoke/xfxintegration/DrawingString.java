/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.karaoke.xfxintegration;

/**
 *
 * @author The Wingate 2940
 */
public class DrawingString {
    
    String drawing = "m 0 0 l 10 0 l 10 10 l 0 10";
    
    public DrawingString(){
        
    }
    
    public DrawingString(String s){
        drawing = s;
    }
    
    public void setDrawing(String s){
        drawing = s;
    }
    
    public String getDrawing(){
        return drawing;
    }
    
    @Override
    public String toString(){
        return drawing;
    }
    
}
