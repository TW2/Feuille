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
public class Karaoke extends OIntegration {
    
    Params duration;
    
    public Karaoke(){
        name = lang.getValueOf("popm_k")!=null ? lang.getValueOf("popm_k") : "Simple karaoke";
        String p1 = lang.getValueOf("xfxparam_31")!=null ? lang.getValueOf("xfxparam_31") : "Duration";
        duration = new Params(p1, "0", false);
        paramsList.add(duration);
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
        Pattern p = Pattern.compile("k(.+)");
        Matcher m = p.matcher(commands);
        if(m.matches()){
            String x = m.group(1);
            duration.setParameter(x); paramsList.set(0, duration);
        }
    }

    @Override
    public void update() {
        duration = paramsList.get(0);
        String s = (String)duration.getParameter();
        commands = "\\k"+s;
    }
}
