/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.karaoke.xfxintegration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;

/**
 * \fade(a1,a2,a3,t1,t2,t3,t4)
 * @author The Wingate 2940
 */
public class Fade extends OIntegration {
    
    Params a1,a2,a3,t1,t2,t3,t4;
    
    public Fade(){
        name = lang.getValueOf("popm_fade")!=null ? lang.getValueOf("popm_fade") : "Fade";
        String p1 = lang.getValueOf("xfxparam_09")!=null ? lang.getValueOf("xfxparam_09") : "Fade in time";
        String p2 = lang.getValueOf("xfxparam_10")!=null ? lang.getValueOf("xfxparam_10") : "Fade out time";
        String p3 = lang.getValueOf("xfxparam_11")!=null ? lang.getValueOf("xfxparam_11") : "Start time";
        String p4 = lang.getValueOf("xfxparam_12")!=null ? lang.getValueOf("xfxparam_12") : "End time";
        String p5 = lang.getValueOf("xfxparam_13")!=null ? lang.getValueOf("xfxparam_13") : "Alpha 1";
        String p6 = lang.getValueOf("xfxparam_14")!=null ? lang.getValueOf("xfxparam_14") : "Alpha 2";
        String p7 = lang.getValueOf("xfxparam_15")!=null ? lang.getValueOf("xfxparam_15") : "Alpha 3";
        a1 = new Params(p5, "0", false); paramsList.add(a1);
        a2 = new Params(p6, "0", false); paramsList.add(a2);
        a3 = new Params(p7, "0", false); paramsList.add(a3);
        t1 = new Params(p3, "0", false); paramsList.add(t1);
        t2 = new Params(p1, "0", false); paramsList.add(t2);
        t3 = new Params(p2, "0", false); paramsList.add(t3);
        t4 = new Params(p4, "0", false); paramsList.add(t4);
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
        Pattern p = Pattern.compile("fade\\((.+),(.+),(.+),(.+),(.+),(.+),(.+)");
        Matcher m = p.matcher(commands);
        if(m.matches()){
            String x;
            x = m.group(1); a1.setParameter(x); paramsList.set(0, a1);
            x = m.group(2); a2.setParameter(x); paramsList.set(1, a2);
            x = m.group(3); a3.setParameter(x); paramsList.set(2, a3);
            x = m.group(4); t1.setParameter(x); paramsList.set(3, t1);
            x = m.group(5); t2.setParameter(x); paramsList.set(4, t2);
            x = m.group(6); t3.setParameter(x); paramsList.set(5, t3);
            x = m.group(7); t4.setParameter(x); paramsList.set(6, t4);
        }
    }

    @Override
    public void update() {
        a1 = paramsList.get(0); String s1 = (String)a1.getParameter();
        a2 = paramsList.get(1); String s2 = (String)a2.getParameter();
        a3 = paramsList.get(2); String s3 = (String)a3.getParameter();
        t1 = paramsList.get(3); String s4 = (String)t1.getParameter();
        t2 = paramsList.get(4); String s5 = (String)t2.getParameter();
        t3 = paramsList.get(5); String s6 = (String)t3.getParameter();
        t4 = paramsList.get(6); String s7 = (String)t4.getParameter();
        commands = "\\fade("+s1+","+s2+","+s3+","+s4+","+s5+","+s6+","+s7+")";
    }
}
