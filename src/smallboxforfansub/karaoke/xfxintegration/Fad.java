/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smallboxforfansub.karaoke.xfxintegration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;

/**
 * \fad(t1,t2)
 * @author The Wingate 2940
 */
public class Fad extends OIntegration {
    
    Params t1,t2;
    
    public Fad(){
        name = lang.getValueOf("popm_fad")!=null ? lang.getValueOf("popm_fad") : "Fad";
        String p1 = lang.getValueOf("xfxparam_09")!=null ? lang.getValueOf("xfxparam_09") : "Fade in time";
        String p2 = lang.getValueOf("xfxparam_10")!=null ? lang.getValueOf("xfxparam_10") : "Fade out time";
        t1 = new Params(p1, "0", false); paramsList.add(t1);
        t2 = new Params(p2, "0", false); paramsList.add(t2);
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
        Pattern p = Pattern.compile("fad\\((.+),(.+)");
        Matcher m = p.matcher(commands);
        if(m.matches()){
            String x;
            x = m.group(1); t1.setParameter(x); paramsList.set(0, t1);
            x = m.group(2); t2.setParameter(x); paramsList.set(1, t2);
        }
    }

    @Override
    public void update() {
        t1 = paramsList.get(0); String s1 = (String)t1.getParameter();
        t2 = paramsList.get(1); String s2 = (String)t2.getParameter();
        commands = "\\fad("+s1+","+s2+")";
    }
}
