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
public class KaraokeOutline extends OIntegration {

    Params duration;
    
    public KaraokeOutline(){
        name = lang.getValueOf("popm_ko")!=null ? lang.getValueOf("popm_ko") : "Karaoke with outline";
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
        ImageIcon ii = new ImageIcon(getClass().getResource("20px-Crystal_Clear_app_ksame2.png"));
        return ii;
    }
    
    @Override
    public void init() {
        Pattern p = Pattern.compile("ko(.+)");
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
        commands = "\\ko"+s;
    }
}
