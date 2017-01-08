/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.theme;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author The Wingate 2940
 */
public class ThemeCollection {
    
//    Default(new Color(51,98,140),new Color(214,217,223),"Default"),
//    PinkViolet(new Color(153,0,153),new Color(255,204,255),"PinkViolet"),
//    GreenArmy(new Color(0,102,0),new Color(149,173,149),"GreenArmy"),        
//    BloodyRed(new Color(153,0,51),new Color(255,204,204),"BloodyRed"),
//    OrangeJuice(new Color(255,153,0),new Color(255,224,161),"OrangeJuice"),
//    WhiteIce(new Color(153,255,255),new Color(255,255,255),"WhiteIce"),
//    Ubuntu(new Color(188,121,51),new Color(239,235,231),"Ubuntu"),
//    GreenJungle(new Color(89,114,0),new Color(233,255,213),"GreenJungle"),
//    BlueViolet(new Color(76,85,255),new Color(224,229,255),"BlueViolet"),
//    GreenGrass(new Color(51,136,16),new Color(251,255,239),"GreenGrass"),
//    GrayMetal(new Color(91,135,71),new Color(213,213,213),"GrayMetal"),
//    IceWithViolet(new Color(112,107,255),new Color(255,255,255),"IceWithViolet"),
//    IceWithOrange(new Color(255,114,63),new Color(255,255,255),"IceWithOrange"),
//    IceWithYellow(new Color(255,246,91),new Color(255,255,255),"IceWithYellow");
    
    private List<Theme> internalTheme = new ArrayList<>();
    static List<Theme> externalTheme = new ArrayList<>();
    
    public ThemeCollection(){
           
    }
    
    public void setup(){
        internalTheme.add(new Theme());//Load the Default theme.
        internalTheme.add(new Theme(new Color(153,0,153),new Color(255,204,255),"PinkViolet"));
        internalTheme.add(new Theme(new Color(0,102,0),new Color(149,173,149),"GreenArmy"));
        internalTheme.add(new Theme(new Color(153,0,51),new Color(255,204,204),"BloodyRed"));
        internalTheme.add(new Theme(new Color(255,153,0),new Color(255,224,161),"OrangeJuice"));
        internalTheme.add(new Theme(new Color(153,255,255),new Color(255,255,255),"WhiteIce"));
        internalTheme.add(new Theme(new Color(188,121,51),new Color(239,235,231),"Ubuntu"));
        internalTheme.add(new Theme(new Color(89,114,0),new Color(233,255,213),"GreenJungle"));
        internalTheme.add(new Theme(new Color(76,85,255),new Color(224,229,255),"BlueViolet"));
        internalTheme.add(new Theme(new Color(51,136,16),new Color(251,255,239),"GreenGrass"));
        internalTheme.add(new Theme(new Color(91,135,71),new Color(213,213,213),"GrayMetal"));
        internalTheme.add(new Theme(new Color(112,107,255),new Color(255,255,255),"IceWithViolet"));
        internalTheme.add(new Theme(new Color(255,114,63),new Color(255,255,255),"IceWithOrange"));
        internalTheme.add(new Theme(new Color(255,246,91),new Color(255,255,255),"IceWithYellow"));
    }
    
    public static void addExternalTheme(Theme th){
        externalTheme.add(th);
    }
    
    public static void deleteExternalTheme(Theme th){
        externalTheme.remove(th);
    }
    
    public List<Theme> getInternalThemes(){
        return internalTheme;
    }
    
    public List<Theme> getExternalThemes(){
        return externalTheme;
    }
    
    public List<Theme> getSortedThemes(){
        List<Theme> sth = new ArrayList<>();
        sth.addAll(internalTheme);
        sth.addAll(externalTheme);
        Collections.sort(sth, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Theme)o1).getName()).compareTo(((Theme)o2).getName());
            }
	});
        return sth;
    }
    
    public Theme getTheme(String name){
        for(Theme th : internalTheme){
            if(th.getName().equalsIgnoreCase(name)){
                return th;
            }
        }
        for(Theme th : externalTheme){
            if(th.getName().equalsIgnoreCase(name)){
                return th;
            }
        }
        return new Theme();
    }
    
}
