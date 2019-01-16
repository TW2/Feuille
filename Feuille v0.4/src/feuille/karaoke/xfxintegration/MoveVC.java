/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.karaoke.xfxintegration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;

/**
 * \movevc(x1,y1,x2,y2[,t1,t2])
 * \movevc(x1,y1)
 * @author The Wingate 2940
 */
public class MoveVC extends OIntegration {
    
    Params x1, y1, x2, y2, t1, t2;
    
    public MoveVC(){
        name = lang.getValueOf("popm_movevc")!=null ? lang.getValueOf("popm_movevc") : "MoveVC";
        String p1 = lang.getValueOf("xfxparam_32")!=null ? lang.getValueOf("xfxparam_32") : "Position on X1 (start)";
        String p2 = lang.getValueOf("xfxparam_33")!=null ? lang.getValueOf("xfxparam_33") : "Position on Y1 (start)";
        String p3 = lang.getValueOf("xfxparam_34")!=null ? lang.getValueOf("xfxparam_34") : "Position on X2 (end)";
        String p4 = lang.getValueOf("xfxparam_35")!=null ? lang.getValueOf("xfxparam_35") : "Position on Y2 (end)";
        String p5 = lang.getValueOf("xfxparam_02")!=null ? lang.getValueOf("xfxparam_02") : "Start";
        String p6 = lang.getValueOf("xfxparam_03")!=null ? lang.getValueOf("xfxparam_03") : "End";
        x1 = new Params(p1, "0", false); paramsList.add(x1);
        y1 = new Params(p2, "0", false); paramsList.add(y1);
        x2 = new Params(p3, "0", true); paramsList.add(x2);
        y2 = new Params(p4, "0", true); paramsList.add(y2);
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
        if(table.length==6){
            Pattern p = Pattern.compile("movevc\\((.+),(.+),(.+),(.+),(.+),(.+)");
            Matcher m = p.matcher(commands);
            if(m.matches()){
                String x;
                x = m.group(1); x1.setParameter(x); paramsList.set(0, x1);
                x = m.group(2); y1.setParameter(x); paramsList.set(1, y1);
                x = m.group(3); x2.setParameter(x); paramsList.set(2, x2);
                x = m.group(4); y2.setParameter(x); paramsList.set(3, y2);
                x = m.group(5); t1.setParameter(x); paramsList.set(4, t1);
                x = m.group(6); t2.setParameter(x); paramsList.set(5, t2);
            }
        }else if(table.length==4){
            Pattern p = Pattern.compile("movevc\\((.+),(.+),(.+),(.+)");
            Matcher m = p.matcher(commands);
            if(m.matches()){
                String x;
                x = m.group(1); x1.setParameter(x); paramsList.set(0, x1);
                x = m.group(2); y1.setParameter(x); paramsList.set(1, y1);
                x = m.group(3); x2.setParameter(x); paramsList.set(2, x2);
                x = m.group(4); y2.setParameter(x); paramsList.set(3, y2);
                t1.setInactive(true); paramsList.set(4, t1);
                t2.setInactive(true); paramsList.set(5, t2);
            }
        }else{
            Pattern p = Pattern.compile("movevc\\((.+),(.+)");
            Matcher m = p.matcher(commands);
            if(m.matches()){
                String x;
                x = m.group(1); x1.setParameter(x); paramsList.set(0, x1);
                x = m.group(2); y1.setParameter(x); paramsList.set(1, y1);
                x2.setInactive(true); paramsList.set(2, x2);
                y2.setInactive(true); paramsList.set(3, y2);
                t1.setInactive(true); paramsList.set(4, t1);
                t2.setInactive(true); paramsList.set(5, t2);
            }
        }
        
    }

    @Override
    public void update() {
        x1 = paramsList.get(0); String s1 = (String)x1.getParameter();
        y1 = paramsList.get(1); String s2 = (String)y1.getParameter();
        x2 = paramsList.get(2); String s3 = (String)x2.getParameter();
        y2 = paramsList.get(3); String s4 = (String)y2.getParameter();
        t1 = paramsList.get(4); String s5 = (String)t1.getParameter();
        t2 = paramsList.get(5); String s6 = (String)t2.getParameter();
        
        if(t1.isInactive() & t2.isInactive() & x2.isInactive() & y2.isInactive()){
            commands = "\\movevc("+s1+","+s2+")";
        }else if(t1.isInactive() & t2.isInactive() & !x2.isInactive() & !y2.isInactive()){
            commands = "\\movevc("+s1+","+s2+","+s3+","+s4+")";
        }else{
            commands = "\\movevc("+s1+","+s2+","+s3+","+s4+","+s5+","+s6+")";
        }
    }
}
