/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.karaoke.xfxintegration;

/**
 *
 * @author The Wingate 2940
 */
public class TransparencyString {
    
    String trans = "00";
    
    public TransparencyString(){
        
    }
    
    public TransparencyString(String s){
        trans = s;
    }
    
    public void setTransparency(String s){
        trans = s;
    }
    
    public String getTransparency(){
        return trans;
    }
    
    @Override
    public String toString(){
        return trans;
    }
    
}
