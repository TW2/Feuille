/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smallboxforfansub.drawing.adf;

import java.awt.Color;

/**
 *
 * @author The Wingate 2940
 */
public class LayerContent {
    
    private String name = "ID 0";
    private Color color = Color.green;
    private String asscommands = "";
    
    public LayerContent(){
        
    }
    
    public LayerContent(String name, Color color, String asscommands){
        this.name = name;
        this.color = color;
        this.asscommands = asscommands;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getName(){
        return name;
    }
    
    public void setColor(Color c){
        color = c;
    }
    
    public Color getColor(){
        return color;
    }
    
    public void setAssCommands(String asscommands){
        this.asscommands = asscommands;
    }
    
    public String getAssCommands(){
        return asscommands;
    }
    
    public void fromHTMLColor(String HTMLColor){
        if(HTMLColor.startsWith("#")){HTMLColor=HTMLColor.substring(1);}
        String red = HTMLColor.substring(0, 2);
        String green = HTMLColor.substring(2, 4);
        String blue = HTMLColor.substring(4);
        int r = Integer.parseInt(red, 16);
        int g = Integer.parseInt(green, 16);
        int b = Integer.parseInt(blue, 16);
        color = new Color(r,g,b);
    }
    
    public String toHTMLColor(){
        String red = Integer.toHexString(color.getRed()).toUpperCase();
        String green = Integer.toHexString(color.getGreen()).toUpperCase();
        String blue = Integer.toHexString(color.getBlue()).toUpperCase();
        if(red.length()<2){red="0"+red;}
        if(green.length()<2){green="0"+green;}
        if(blue.length()<2){blue="0"+blue;}
        return "#"+red+green+blue;
    }
}
