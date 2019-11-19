/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.karaoke.xfxintegration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;

/**
 * \iclip(x1,y1,x2,y2)
 * @author The Wingate 2940
 */
public class IClip extends OIntegration {
    
    Params x1,y1,x2,y2;
    
    public IClip(){
        name = lang.getValueOf("popm_iclip")!=null ? lang.getValueOf("popm_iclip") : "Region of invisibiliry";
        x1 = new Params("X1", "0", false); paramsList.add(x1);
        y1 = new Params("Y1", "0", false); paramsList.add(y1);
        x2 = new Params("X2", "0", false); paramsList.add(x2);
        y2 = new Params("Y2", "0", false); paramsList.add(y2);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ImageIcon getIcon() {
        ImageIcon ii = new ImageIcon(getClass().getResource("20px-Crystal_Clear_app_ksame4.png"));
        return ii;
    }
    
    @Override
    public void init() {
        Pattern p = Pattern.compile("iclip\\((.+),(.+),(.+),(.+)");
        Matcher m = p.matcher(commands);
        if(m.matches()){
            String x;
            x = m.group(1); x1.setParameter(x); paramsList.set(0, x1);
            x = m.group(2); y1.setParameter(x); paramsList.set(1, y1);
            x = m.group(3); x2.setParameter(x); paramsList.set(2, x2);
            x = m.group(4); y2.setParameter(x); paramsList.set(3, y2);
        }
    }

    @Override
    public void update() {
        x1 = paramsList.get(0); String s1 = (String)x1.getParameter();
        y1 = paramsList.get(1); String s2 = (String)y1.getParameter();
        x2 = paramsList.get(2); String s3 = (String)x2.getParameter();
        y2 = paramsList.get(3); String s4 = (String)y2.getParameter();
        commands = "\\iclip("+s1+","+s2+","+s3+","+s4+")";
    }
}
