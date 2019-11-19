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
public class Mdx extends OIntegration {
    
    Params value;
    
    public Mdx(){
        name = lang.getValueOf("popm_mdx")!=null ? lang.getValueOf("popm_mdx") : "Boundaries deforming on X";
        String p1 = lang.getValueOf("xfxparam_05")!=null ? lang.getValueOf("xfxparam_05") : "Value";
        value = new Params(p1, "0", false); paramsList.add(value);
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
        Pattern p = Pattern.compile("mdx(.+)");
        Matcher m = p.matcher(commands);
        if(m.matches()){
            String x = m.group(1);
            value.setParameter(x); paramsList.set(0, value);
        }
    }

    @Override
    public void update() {
        value = paramsList.get(0);
        String s = (String)value.getParameter();
        commands = "\\mdx"+s;
    }
}
