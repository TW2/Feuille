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
public class Position extends OIntegration {
    
    Params posX, posY;
    
    public Position(){
        name = lang.getValueOf("popm_pos")!=null ? lang.getValueOf("popm_pos") : "Position";
        String p1 = lang.getValueOf("xfxparam_52")!=null ? lang.getValueOf("xfxparam_52") : "Position on X";
        String p2 = lang.getValueOf("xfxparam_53")!=null ? lang.getValueOf("xfxparam_53") : "Position on Y";
        posX = new Params(p1, "0", false);
        paramsList.add(posX);
        posY = new Params(p2, "0", false);
        paramsList.add(posY);
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
        Pattern p = Pattern.compile("pos\\((.+),(.+)");
        Matcher m = p.matcher(commands);
        if(m.matches()){
            String x;
            x = m.group(1); posX.setParameter(x); paramsList.set(0, posX);
            x = m.group(2); posY.setParameter(x); paramsList.set(1, posY);
        }
    }

    @Override
    public void update() {
        posX = paramsList.get(0); String s1 = (String)posX.getParameter();
        posY = paramsList.get(1); String s2 = (String)posY.getParameter();
        commands = "\\pos("+s1+","+s2+")";
    }
    
}
