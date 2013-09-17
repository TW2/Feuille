/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smallboxforfansub.karaoke.xfxintegration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;

/**
 *
 * @author The Wingate 2940
 */
public class FontRotationY extends OIntegration {
    
    Params value;
    
    public FontRotationY(){
        name = lang.getValueOf("popm_fry")!=null ? lang.getValueOf("popm_fry") : "Font rotation ou Y";
        String p1 = lang.getValueOf("xfxparam_18")!=null ? lang.getValueOf("xfxparam_18") : "Angle";
        value = new Params(p1, "0", false); paramsList.add(value);
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
        Pattern p = Pattern.compile("fry(.+)");
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
        commands = "\\fry"+s;
    }
}
