/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smallboxforfansub.karaoke.xfxintegration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;

/**
 * \distort(u1,v1,u2,v2,u3,v3)
 * @author The Wingate 2940
 */
public class Distort extends OIntegration {
    
    Params u1,v1,u2,v2,u3,v3;
    
    public Distort(){
        name = lang.getValueOf("popm_distort")!=null ? lang.getValueOf("popm_distort") : "Distortion";
        u1 = new Params("U1", "0", false); paramsList.add(u1);
        v1 = new Params("V1", "0", false); paramsList.add(v1);
        u2 = new Params("U2", "0", false); paramsList.add(u2);
        v2 = new Params("V2", "0", false); paramsList.add(v2);
        u3 = new Params("U3", "0", false); paramsList.add(u3);
        v3 = new Params("V3", "0", false); paramsList.add(v3);
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
        Pattern p = Pattern.compile("distort\\((.+),(.+),(.+),(.+),(.+),(.+)");
        Matcher m = p.matcher(commands);
        if(m.matches()){
            String x;
            x = m.group(1); u1.setParameter(x); paramsList.set(0, u1);
            x = m.group(2); v1.setParameter(x); paramsList.set(1, v1);
            x = m.group(3); u2.setParameter(x); paramsList.set(2, u2);
            x = m.group(4); v2.setParameter(x); paramsList.set(3, v2);
            x = m.group(5); u3.setParameter(x); paramsList.set(4, u3);
            x = m.group(6); v3.setParameter(x); paramsList.set(5, v3);
        }
    }

    @Override
    public void update() {
        u1 = paramsList.get(0); String s1 = (String)u1.getParameter();
        v1 = paramsList.get(1); String s2 = (String)v1.getParameter();
        u2 = paramsList.get(2); String s3 = (String)u2.getParameter();
        v2 = paramsList.get(3); String s4 = (String)v2.getParameter();
        u3 = paramsList.get(4); String s5 = (String)u3.getParameter();
        v3 = paramsList.get(5); String s6 = (String)v3.getParameter();
        commands = "\\distort("+s1+","+s2+","+s3+","+s4+","+s5+","+s6+")";
    }
}
