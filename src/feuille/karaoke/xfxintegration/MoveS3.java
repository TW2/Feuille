/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.karaoke.xfxintegration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;

/**
 * \moves3(x1,x2,x2,y2,x3,y3[,t1,t2])
 * @author The Wingate 2940
 */
public class MoveS3 extends OIntegration {
    
    Params x1,y1,x2,y2,x3,y3,t1,t2;
    
    public MoveS3(){
        name = lang.getValueOf("popm_moves3")!=null ? lang.getValueOf("popm_moves3") : "MoveS3";
        String p1 = lang.getValueOf("xfxparam_32")!=null ? lang.getValueOf("xfxparam_32") : "Position on X1 (start)";
        String p2 = lang.getValueOf("xfxparam_33")!=null ? lang.getValueOf("xfxparam_33") : "Position on Y1 (start)";
        String p3 = lang.getValueOf("xfxparam_02")!=null ? lang.getValueOf("xfxparam_02") : "Start";
        String p4 = lang.getValueOf("xfxparam_03")!=null ? lang.getValueOf("xfxparam_03") : "End";
        String p5 = lang.getValueOf("xfxparam_40")!=null ? lang.getValueOf("xfxparam_40") : "Position on X2 (middle)";
        String p6 = lang.getValueOf("xfxparam_41")!=null ? lang.getValueOf("xfxparam_41") : "Position on Y2 (middle)";
        String p7 = lang.getValueOf("xfxparam_42")!=null ? lang.getValueOf("xfxparam_42") : "Position on X3 (end)";
        String p8 = lang.getValueOf("xfxparam_43")!=null ? lang.getValueOf("xfxparam_43") : "Position on Y3 (end)";
        x1 = new Params(p1, "0", false); paramsList.add(x1);
        y1 = new Params(p2, "0", false); paramsList.add(y1);
        x2 = new Params(p5, "0", false); paramsList.add(x2);
        y2 = new Params(p6, "0", false); paramsList.add(y2);
        x3 = new Params(p7, "0", false); paramsList.add(x3);
        y3 = new Params(p8, "0", false); paramsList.add(y3);
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
        if(table.length==8){
            Pattern p = Pattern.compile("moves3\\((.+),(.+),(.+),(.+),(.+),(.+),(.+),(.+)");
            Matcher m = p.matcher(commands);
            if(m.matches()){
                String x;
                x = m.group(1); x1.setParameter(x); paramsList.set(0, x1);
                x = m.group(2); y1.setParameter(x); paramsList.set(1, y1);
                x = m.group(3); x2.setParameter(x); paramsList.set(2, x2);
                x = m.group(4); y2.setParameter(x); paramsList.set(3, y2);
                x = m.group(5); x3.setParameter(x); paramsList.set(4, x3);
                x = m.group(6); y3.setParameter(x); paramsList.set(5, y3);
                x = m.group(7); t1.setParameter(x); paramsList.set(6, t1);
                x = m.group(8); t2.setParameter(x); paramsList.set(7, t2);
            }
        }else{
            Pattern p = Pattern.compile("moves3\\((.+),(.+),(.+),(.+),(.+),(.+)");
            Matcher m = p.matcher(commands);
            if(m.matches()){
                String x;
                x = m.group(1); x1.setParameter(x); paramsList.set(0, x1);
                x = m.group(2); y1.setParameter(x); paramsList.set(1, y1);
                x = m.group(3); x2.setParameter(x); paramsList.set(2, x2);
                x = m.group(4); y2.setParameter(x); paramsList.set(3, y2);
                x = m.group(5); x3.setParameter(x); paramsList.set(4, x3);
                x = m.group(6); y3.setParameter(x); paramsList.set(5, y3);
                t1.setInactive(true); paramsList.set(6, t1);
                t2.setInactive(true); paramsList.set(7, t2);
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
        t1 = paramsList.get(6); String s7 = (String)t1.getParameter();
        t2 = paramsList.get(7); String s8 = (String)t2.getParameter();
        
        if(t1.isInactive() && t2.isInactive()){
            commands = "\\moves3("+s1+","+s2+","+s3+","+s4+","+s5+","+s6+")";
        }else{
            commands = "\\moves3("+s1+","+s2+","+s3+","+s4+","+s5+","+s6+","+s7+","+s8+")";
        }
    }
}
