/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.karaoke.xfxintegration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;

/**
 * \clip([scale,]some drawings)
 * @author The Wingate 2940
 */
public class ClipWithDrawings extends OIntegration {
    
    Params scale, drawing;
    
    public ClipWithDrawings(){
        name = lang.getValueOf("popm_clip2")!=null ? lang.getValueOf("popm_clip2") : "Clip with drawings";
        String p1 = lang.getValueOf("xfxparam_06")!=null ? lang.getValueOf("xfxparam_06") : "Scale";
        String p2 = lang.getValueOf("xfxparam_07")!=null ? lang.getValueOf("xfxparam_07") : "Drawings";
        scale = new Params(p1, "1", true); paramsList.add(scale);
        drawing = new Params(p2, new DrawingString(), false); paramsList.add(drawing);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ImageIcon getIcon() {
        ImageIcon ii = new ImageIcon(getClass().getResource("20px-Crystal_Clear_app_ksame2.png"));
        return ii;
    }

    @Override
    public void init() {
        if(commands.contains(",")){
            Pattern p = Pattern.compile("clip\\((.+),(.+)");
            Matcher m = p.matcher(commands);
            if(m.matches()){
                String x;
                x = m.group(1);
                scale.setParameter(x); paramsList.set(0, scale);
                x = m.group(2);
                if(x.contains("$")){
                    drawing.setState(Params.State.SuperString);
                    drawing.setBackup(new DrawingString());
                    drawing.setParameter(new SuperString(x)); paramsList.set(1, drawing);                
                }else{
                    drawing.setParameter(new DrawingString(x)); paramsList.set(1, drawing);
                }
            }
        }else{
            Pattern p = Pattern.compile("clip\\((.+)");
            Matcher m = p.matcher(commands);
            if(m.matches()){
                String x;
                x = m.group(1);
                if(x.contains("$")){
                    drawing.setState(Params.State.SuperString);
                    drawing.setBackup(new DrawingString());
                    drawing.setParameter(new SuperString(x)); paramsList.set(1, drawing);                
                }else{
                    drawing.setParameter(new DrawingString(x)); paramsList.set(1, drawing);
                }
                scale.setInactive(true); paramsList.set(0, scale);
            }
        }
        
    }

    @Override
    public void update() {
        scale = paramsList.get(0); String s1 = (String)scale.getParameter();
        drawing = paramsList.get(1);
        if(drawing.getParameter() instanceof DrawingString){
            DrawingString s2 = (DrawingString)drawing.getParameter();
            if(scale.isInactive()){
                commands = "\\clip("+s2.getDrawing()+")";
            }else{
                commands = "\\clip("+s1+","+s2.getDrawing()+")";
            }
        }else{//SuperString
            SuperString s2 = (SuperString)drawing.getParameter();
            if(scale.isInactive()){
                commands = "\\clip("+s2.getSuperString()+")";
            }else{
                commands = "\\clip("+s1+","+s2.getSuperString()+")";
            }
        }
        
    }
    
    
}
