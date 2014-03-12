/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.karaoke.xfxintegration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;

/**
 * \mover(x1,y1,x2,y2,angle1,angle2,radius1,radius2[,t1,t2])
 * @author The Wingate 2940
 */
public class MoveR extends OIntegration {
    
    Params x1,y1,x2,y2,angle1,angle2,radius1,radius2,t1,t2;
    
    public MoveR(){
        name = lang.getValueOf("popm_mover")!=null ? lang.getValueOf("popm_mover") : "MoveR";
        String p1 = lang.getValueOf("xfxparam_32")!=null ? lang.getValueOf("xfxparam_32") : "Position on X1 (start)";
        String p2 = lang.getValueOf("xfxparam_33")!=null ? lang.getValueOf("xfxparam_33") : "Position on Y1 (start)";
        String p3 = lang.getValueOf("xfxparam_34")!=null ? lang.getValueOf("xfxparam_34") : "Position on X2 (end)";
        String p4 = lang.getValueOf("xfxparam_35")!=null ? lang.getValueOf("xfxparam_35") : "Position on Y2 (end)";
        String p5 = lang.getValueOf("xfxparam_02")!=null ? lang.getValueOf("xfxparam_02") : "Start";
        String p6 = lang.getValueOf("xfxparam_03")!=null ? lang.getValueOf("xfxparam_03") : "End";
        String p7 = lang.getValueOf("xfxparam_36")!=null ? lang.getValueOf("xfxparam_36") : "Angle at start";
        String p8 = lang.getValueOf("xfxparam_37")!=null ? lang.getValueOf("xfxparam_37") : "Angle at end";
        String p9 = lang.getValueOf("xfxparam_38")!=null ? lang.getValueOf("xfxparam_38") : "Radius at start";
        String p10 = lang.getValueOf("xfxparam_39")!=null ? lang.getValueOf("xfxparam_39") : "Radius at end";
        x1 = new Params(p1, "0", false); paramsList.add(x1);
        y1 = new Params(p2, "0", false); paramsList.add(y1);
        x2 = new Params(p3, "0", false); paramsList.add(x2);
        y2 = new Params(p4, "0", false); paramsList.add(y2);
        angle1 = new Params(p7, "0", false); paramsList.add(angle1);
        angle2 = new Params(p8, "0", false); paramsList.add(angle2);
        radius1 = new Params(p9, "0", false); paramsList.add(radius1);
        radius2 = new Params(p10, "0", false); paramsList.add(radius2);
        t1 = new Params(p5, "0", true); paramsList.add(t1);
        t2 = new Params(p6, "0", true); paramsList.add(t2);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ImageIcon getIcon() {
        ImageIcon ii = new ImageIcon(getClass().getResource("20px-Crystal_Clear_app_ksame.png"));
        return ii;
    }
    
    @Override
    public void init() {
        String[] table = commands.split(",");
        if(table.length==10){
            Pattern p = Pattern.compile("mover\\((.+),(.+),(.+),(.+),(.+),(.+),(.+),(.+),(.+),(.+)");
            Matcher m = p.matcher(commands);
            if(m.matches()){
                String x;
                x = m.group(1); x1.setParameter(x); paramsList.set(0, x1);
                x = m.group(2); y1.setParameter(x); paramsList.set(1, y1);
                x = m.group(3); x2.setParameter(x); paramsList.set(2, x2);
                x = m.group(4); y2.setParameter(x); paramsList.set(3, y2);
                x = m.group(5); angle1.setParameter(x); paramsList.set(4, angle1);
                x = m.group(6); angle2.setParameter(x); paramsList.set(5, angle2);
                x = m.group(7); radius1.setParameter(x); paramsList.set(6, radius1);
                x = m.group(8); radius2.setParameter(x); paramsList.set(7, radius2);
                x = m.group(9); t1.setParameter(x); paramsList.set(8, t1);
                x = m.group(10); t2.setParameter(x); paramsList.set(9, t2);
            }
        }else{
            Pattern p = Pattern.compile("mover\\((.+),(.+),(.+),(.+),(.+),(.+),(.+),(.+)");
            Matcher m = p.matcher(commands);
            if(m.matches()){
                String x;
                x = m.group(1); x1.setParameter(x); paramsList.set(0, x1);
                x = m.group(2); y1.setParameter(x); paramsList.set(1, y1);
                x = m.group(3); x2.setParameter(x); paramsList.set(2, x2);
                x = m.group(4); y2.setParameter(x); paramsList.set(3, y2);
                x = m.group(5); angle1.setParameter(x); paramsList.set(4, angle1);
                x = m.group(6); angle2.setParameter(x); paramsList.set(5, angle2);
                x = m.group(7); radius1.setParameter(x); paramsList.set(6, radius1);
                x = m.group(8); radius2.setParameter(x); paramsList.set(7, radius2);
                t1.setInactive(true); paramsList.set(8, t1);
                t2.setInactive(true); paramsList.set(9, t2);
            }
        }
        
    }

    @Override
    public void update() {
        x1 = paramsList.get(0); String s1 = (String)x1.getParameter();
        y1 = paramsList.get(1); String s2 = (String)y1.getParameter();
        x2 = paramsList.get(2); String s3 = (String)x2.getParameter();
        y2 = paramsList.get(3); String s4 = (String)y2.getParameter();
        angle1 = paramsList.get(4); String s5 = (String)angle1.getParameter();
        angle2 = paramsList.get(5); String s6 = (String)angle2.getParameter();
        radius1 = paramsList.get(6); String s7 = (String)radius1.getParameter();
        radius2 = paramsList.get(7); String s8 = (String)radius2.getParameter();
        t1 = paramsList.get(8); String s9 = (String)t1.getParameter();
        t2 = paramsList.get(9); String s10 = (String)t2.getParameter();
        
        if(t1.isInactive() && t2.isInactive()){
            commands = "\\mover("+s1+","+s2+","+s3+","+s4+","+s5+","+s6+","+s7+","+s8+")";
        }else{
            commands = "\\mover("+s1+","+s2+","+s3+","+s4+","+s5+","+s6+","+s7+","+s8+","+s9+","+s10+")";
        }
    }
}
