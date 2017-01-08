/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.karaoke.lib;

import java.awt.Font;

/**
 *
 * @author The Wingate 2940
 */
public class FontWithCoef {
    Font font; double coef;
    
    public FontWithCoef(Font font, double coef){
            this.font = font;
            this.coef = coef;
    }
    
    public FontWithCoef(Font font, int percent){
            this.font = font;
            this.coef = percent/100d;
    }
    
    public FontWithCoef(){
            font = new Font("Dialog", Font.PLAIN, 12);
            coef = 1;
    }
    
    public String getFontName(){
        return font.getName();
    }
    
    public double getCoef(){
        return coef;
    }
    
    public String getCoefInPercent(){
        return (coef*100)+"";
    }
    
    public void setFontName(String name){
        font = new Font(name, Font.PLAIN, 12);
    }
    
    public void setCoef(double coef){
        this.coef = coef;
    }
    
    public void setCoef(String percent){
        try{
            percent = percent.replace(".0", "");
            int p = Integer.parseInt(percent);
            coef = p/100d;
        }catch(NumberFormatException nfe){
            coef = 1;
        }        
    }
}
