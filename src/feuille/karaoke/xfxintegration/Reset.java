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
public class Reset extends OIntegration {
    
    Params style;
    
    public Reset(){
        name = lang.getValueOf("popm_r")!=null ? lang.getValueOf("popm_r") : "Reset";
        String p1 = lang.getValueOf("xfxparam_54")!=null ? lang.getValueOf("xfxparam_54") : "Style";
        style = new Params(p1, "", false);
        paramsList.add(style);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ImageIcon getIcon() {
        ImageIcon ii = new ImageIcon(getClass().getResource("20px-Crystal_Clear_app_ksame3.png"));
        return ii;
    }
    
    @Override
    public void init() {
        Pattern p = Pattern.compile("r(.*)");
        Matcher m = p.matcher(commands);
        if(m.matches()){
            String x = m.group(1);
            style.setParameter(x); paramsList.set(0, style);
        }
    }

    @Override
    public void update() {
        style = paramsList.get(0);
        String s = (String)style.getParameter();
        commands = "\\r"+s;
    }
}
