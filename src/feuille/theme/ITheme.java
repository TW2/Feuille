/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.theme;

import java.awt.Color;

/**
 *
 * @author The Wingate 2940
 */
public interface ITheme {
    
    public void setNimbusBaseColor(Color c);
    
    public Color getNimbusBaseColor();
    
    public void setControlColor(Color c);
    
    public Color getControlColor();
    
    public void setTextColor(Color c);
    
    public Color getTextColor();
    
    public void setInfoColor(Color c);
    
    public Color getInfoColor();
    
    public void setName(String name);
    
    public String getName();
    
}
