/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.karaoke.xfxintegration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;

/**
 *
 * @author The Wingate 2940
 */
public class Origin extends OIntegration {
    
    Params orgX, orgY;
    
    public Origin(){
        name = lang.getValueOf("popm_org")!=null ? lang.getValueOf("popm_org") : "Origin";
        String p1 = lang.getValueOf("xfxparam_50")!=null ? lang.getValueOf("xfxparam_50") : "Origin on X";
        String p2 = lang.getValueOf("xfxparam_51")!=null ? lang.getValueOf("xfxparam_51") : "Origin on Y";
        orgX = new Params(p1, "0", false);
        paramsList.add(orgX);
        orgY = new Params(p2, "0", false);
        paramsList.add(orgY);
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
        Pattern p = Pattern.compile("org\\((.+),(.+)");
        Matcher m = p.matcher(commands);
        if(m.matches()){
            String x;
            x = m.group(1); orgX.setParameter(x); paramsList.set(0, orgX);
            x = m.group(2); orgY.setParameter(x); paramsList.set(1, orgY);
        }
    }

    @Override
    public void update() {
        orgX = paramsList.get(0); String s1 = (String)orgX.getParameter();
        orgY = paramsList.get(1); String s2 = (String)orgY.getParameter();
        commands = "\\org("+s1+","+s2+")";
    }
}
