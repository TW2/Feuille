/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.karaoke.xfxintegration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;

/**
 * \moves4(x1,x2,x2,y2,x3,y3,x4,y4[,t1,t2])
 * @author The Wingate 2940
 */
public class MoveS4 extends OIntegration {
    
    Params x1,y1,x2,y2,x3,y3,x4,y4,t1,t2;
    
    public MoveS4(){
        name = lang.getValueOf("popm_moves4")!=null ? lang.getValueOf("popm_moves4") : "MoveS4";
        String p1 = lang.getValueOf("xfxparam_32")!=null ? lang.getValueOf("xfxparam_32") : "Position on X1 (start)";
        String p2 = lang.getValueOf("xfxparam_33")!=null ? lang.getValueOf("xfxparam_33") : "Position on Y1 (start)";
        String p3 = lang.getValueOf("xfxparam_02")!=null ? lang.getValueOf("xfxparam_02") : "Start";
        String p4 = lang.getValueOf("xfxparam_03")!=null ? lang.getValueOf("xfxparam_03") : "End";
        String p5 = lang.getValueOf("xfxparam_44")!=null ? lang.getValueOf("xfxparam_44") : "Position on X2 (2/4)";
        String p6 = lang.getValueOf("xfxparam_45")!=null ? lang.getValueOf("xfxparam_45") : "Position on Y2 (2/4)";
        String p7 = lang.getValueOf("xfxparam_46")!=null ? lang.getValueOf("xfxparam_46") : "Position on X3 (3/4)";
        String p8 = lang.getValueOf("xfxparam_47")!=null ? lang.getValueOf("xfxparam_47") : "Position on Y3 (3/4)";
        String p9 = lang.getValueOf("xfxparam_48")!=null ? lang.getValueOf("xfxparam_48") : "Position on X4 (end)";
        String p10 = lang.getValueOf("xfxparam_49")!=null ? lang.getValueOf("xfxparam_49") : "Position on Y4 (end)";
        x1 = new Params(p1, "0", false); paramsList.add(x1);
        y1 = new Params(p2, "0", false); paramsList.add(y1);
        x2 = new Params(p5, "0", false); paramsList.add(x2);
        y2 = new Params(p6, "0", false); paramsList.add(y2);
        x3 = new Params(p7, "0", false); paramsList.add(x3);
        y3 = new Params(p8, "0", false); paramsList.add(y3);
        x4 = new Params(p9, "0", false); paramsList.add(x4);
        y4 = new Params(p10, "0", false); paramsList.add(y4);
        t1 = new Params(p3, "0", true); paramsList.add(t1);
        t2 = new Params(p4, "0", true); paramsList.add(t2);
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
            Pattern p = Pattern.compile("moves4\\((.+),(.+),(.+),(.+),(.+),(.+),(.+),(.+),(.+),(.+)");
            Matcher m = p.matcher(commands);
            if(m.matches()){
                String x;
                x = m.group(1); x1.setParameter(x); paramsList.set(0, x1);
                x = m.group(2); y1.setParameter(x); paramsList.set(1, y1);
                x = m.group(3); x2.setParameter(x); paramsList.set(2, x2);
                x = m.group(4); y2.setParameter(x); paramsList.set(3, y2);
                x = m.group(5); x3.setParameter(x); paramsList.set(4, x3);
                x = m.group(6); y3.setParameter(x); paramsList.set(5, y3);
                x = m.group(7); x4.setParameter(x); paramsList.set(6, x4);
                x = m.group(8); y4.setParameter(x); paramsList.set(7, y4);
                x = m.group(9); t1.setParameter(x); paramsList.set(8, t1);
                x = m.group(10); t2.setParameter(x); paramsList.set(9, t2);
            }
        }else{
            Pattern p = Pattern.compile("moves4\\((.+),(.+),(.+),(.+),(.+),(.+),(.+),(.+)");
            Matcher m = p.matcher(commands);
            if(m.matches()){
                String x;
                x = m.group(1); x1.setParameter(x); paramsList.set(0, x1);
                x = m.group(2); y1.setParameter(x); paramsList.set(1, y1);
                x = m.group(3); x2.setParameter(x); paramsList.set(2, x2);
                x = m.group(4); y2.setParameter(x); paramsList.set(3, y2);
                x = m.group(5); x3.setParameter(x); paramsList.set(4, x3);
                x = m.group(6); y3.setParameter(x); paramsList.set(5, y3);
                x = m.group(7); x4.setParameter(x); paramsList.set(6, x4);
                x = m.group(8); y4.setParameter(x); paramsList.set(7, y4);
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
        x3 = paramsList.get(4); String s5 = (String)x3.getParameter();
        y3 = paramsList.get(5); String s6 = (String)y3.getParameter();
        x4 = paramsList.get(6); String s7 = (String)x4.getParameter();
        y4 = paramsList.get(7); String s8 = (String)y4.getParameter();
        t1 = paramsList.get(8); String s9 = (String)t1.getParameter();
        t2 = paramsList.get(9); String s10 = (String)t2.getParameter();
        
        if(t1.isInactive() && t2.isInactive()){
            commands = "\\moves4("+s1+","+s2+","+s3+","+s4+","+s5+","+s6+","+s7+","+s8+")";
        }else{
            commands = "\\moves4("+s1+","+s2+","+s3+","+s4+","+s5+","+s6+","+s7+","+s8+","+s9+","+s10+")";
        }
    }
}
