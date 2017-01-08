/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.karaoke.xfxintegration;

/**
 *
 * @author The Wingate 2940
 */
public class SuperString {
    
    String s = "";
    
    public SuperString(){
        
    }
    
    public SuperString(String s){
        this.s = s;
    }
    
    public void setSuperString(String s){
        this.s = s;
    }
    
    public String getSuperString(){
        return s;
    }
    
    @Override
    public String toString(){
        return s;        
    }
}
