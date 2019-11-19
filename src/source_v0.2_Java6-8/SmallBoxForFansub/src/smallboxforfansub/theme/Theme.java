/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smallboxforfansub.theme;

import java.awt.Color;

/**
 *
 * @author The Wingate 2940
 */
public class Theme implements ITheme {
    
    Color c1 = new Color(51,98,140);        //nimbusSbase
    Color c2 = new Color(214,217,223);      //control
    Color c3 = new Color(0,0,0);            //text
    Color c4 = new Color(242,242,189);      //info
    String name = "Default";
    
    public Theme(){
        
    }

    public Theme(Color nimbusBase, Color control, String name){
        c1 = nimbusBase;
        c2 = control;
        this.name = name;
    }
    
    public Theme(Color nimbusBase, Color control, Color text, String name){
        c1 = nimbusBase;
        c2 = control;
        c3 = text;
        this.name = name;
    }
    
    public Theme(Color nimbusBase, Color control, Color text, Color info, String name){
        c1 = nimbusBase;
        c2 = control;
        c3 = text;
        c4 = info;
        this.name = name;
    }
    
    @Override
    public void setNimbusBaseColor(Color c) {
        c1 = c;
    }

    @Override
    public Color getNimbusBaseColor() {
        return c1;
    }

    @Override
    public void setControlColor(Color c) {
        c2 = c;
    }

    @Override
    public Color getControlColor() {
        return c2;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public String toString(){
        return name;
    }

    @Override
    public void setTextColor(Color c) {
        c3 = c;
    }

    @Override
    public Color getTextColor() {
        return c3;
    }

    @Override
    public void setInfoColor(Color c) {
        c4 = c;
    }

    @Override
    public Color getInfoColor() {
        return c4;
    }
    
}
