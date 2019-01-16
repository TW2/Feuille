/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.karaoke.xfxintegration;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import feuille.lib.Language;

/**
 *
 * @author The Wingate 2940
 */
public abstract class OIntegration implements IIntegration {
    
    protected String commands = "";
    protected List<Params> paramsList = new ArrayList<Params>();
    protected Language lang = feuille.MainFrame.getLanguage();
    protected String name = "";
    
    @Override
    public String toString(){
        return getName();
    }
    
    @Override
    public String getCommands(){
        return commands;
    }
    
    @Override
    public void setCommands(String commands){
        this.commands = commands;
    }
    
    @Override
    public List<Params> getParams(){
        return paramsList;
    }
    
    @Override
    public void setParams(List<Params> paramsList){
        this.paramsList = paramsList;
    }
    
    protected Color bgrToColor(String bgr){
        String blue, green, red;
        blue = bgr.substring(0, 2);
        green = bgr.substring(2, 4);
        red = bgr.substring(4);
        int b, g, r;
        b = Integer.parseInt(blue, 16);
        g = Integer.parseInt(green, 16);
        r = Integer.parseInt(red, 16);
        return new Color(r,g,b);
    }
    
    protected String colorToBgr(Color c){
        int r, g, b;
        r = c.getRed();
        g = c.getGreen();
        b = c.getBlue();
        String blue, green, red;
        blue = Integer.toString(b, 16); if(blue.length()<2){blue="0"+blue;}
        green = Integer.toString(g, 16); if(green.length()<2){green="0"+green;}
        red = Integer.toString(r, 16); if(red.length()<2){red="0"+red;}
        String bgr = blue+green+red;
        return bgr.toUpperCase();
    }
}
