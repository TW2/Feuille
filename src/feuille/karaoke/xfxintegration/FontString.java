/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.karaoke.xfxintegration;

import java.awt.GraphicsEnvironment;

/**
 *
 * @author The Wingate 2940
 */
public class FontString {
    
    String[] table = null;
    String selectedFont = "";
    
    public FontString(){
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        table = ge.getAvailableFontFamilyNames();
        selectedFont = table[0];
    }
    
    public FontString(String s){
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        table = ge.getAvailableFontFamilyNames();
        selectedFont = s;
    }
    
    public void setSelectedFont(String s){
        selectedFont = s;
    }
    
    public String getSelectedFont(){
        return selectedFont;
    }
    
    public String[] getFonts(){
        return table;
    }
    
    @Override
    public String toString(){
        return selectedFont;
    }
    
}
