/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.karaoke.xfxintegration;

/**
 *
 * @author The Wingate 2940
 */
public class ImageString {
    
    String image = "";
    
    public ImageString(){
        
    }
    
    public ImageString(String s){
        image = s;
    }
    
    public void setImage(String s){
        image = s;
    }
    
    public String getImage(){
        return image;
    }
    
    @Override
    public String toString(){
        return image;
    }
    
}
