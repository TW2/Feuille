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
public class FontSize extends OIntegration {
    
    Params size;
    
    public FontSize(){
        name = lang.getValueOf("popm_fs")!=null ? lang.getValueOf("popm_fs") : "Font size";
        String p1 = lang.getValueOf("xfxparam_20")!=null ? lang.getValueOf("xfxparam_20") : "Size";
        size = new Params(p1, "20", false); paramsList.add(size);
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
        Pattern p = Pattern.compile("fs(.+)");
        Matcher m = p.matcher(commands);
        if(m.matches()){
            String x = m.group(1);
            size.setParameter(x); paramsList.set(0, size);
        }
    }

    @Override
    public void update() {
        size = paramsList.get(0);
        String s = (String)size.getParameter();
        commands = "\\fs"+s;
    }
}
