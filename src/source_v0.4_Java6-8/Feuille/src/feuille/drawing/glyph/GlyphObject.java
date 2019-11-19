/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.drawing.glyph;

import java.awt.Font;

/**
 *
 * @author The Wingate 2940
 */
public class GlyphObject {
    
    String s = "A";
    Font f = new Font("Serif", Font.PLAIN, 50);
    
    public GlyphObject(){
        
    }
    
    public GlyphObject(String s){
        this.s = s;
    }
    
    public GlyphObject(String s, Font f){
        this.s = s;
        this.f = f;
    }
    
    public void setGlyph(String s){
        this.s = s;
    }
    
    public String getGlyph(){
        return s;
    }
    
    public void setFont(Font f){
        this.f = f;
    }
    
    public void setFont(String name){
        f = new Font(name, f.getStyle(), f.getSize());
    }
    
    public void setFont(int style){
        f = f.deriveFont(style);
    }
    
    public void setFont(float size){
        f = f.deriveFont(size);
    }
    
    public Font getFont(){
        return f;
    }
    
}
