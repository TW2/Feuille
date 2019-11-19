/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smallboxforfansub.karaoke.xfxintegration;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;

/**
 *
 * @author The Wingate 2940
 */
public class Color2 extends OIntegration {
    
    Params value;
    
    public Color2(){
        name = lang.getValueOf("popm_2c")!=null ? lang.getValueOf("popm_2c") : "Karaoke color";
        String p1 = lang.getValueOf("xfxparam_08")!=null ? lang.getValueOf("xfxparam_08") : "Color";
        value = new Params(p1, Color.white, false); paramsList.add(value);
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
        Pattern p = Pattern.compile("2c&H(.+)&");
        Matcher m = p.matcher(commands);
        if(m.matches()){
            String x = m.group(1);
            if(x.contains("$")){
                value.setState(Params.State.SuperString);
                value.setBackup(Color.white);
                value.setParameter(new SuperString(x)); paramsList.set(0, value);                
            }else{
                try{
                    value.setParameter(bgrToColor(x)); paramsList.set(0, value);
                }catch(Exception exc){
                    value.setParameter(Color.white); paramsList.set(0, value);
                }
            }
        }
    }

    @Override
    public void update() {
        value = paramsList.get(0);
        if(value.getParameter() instanceof Color){
            Color c = (Color)value.getParameter();
            commands = "\\2c&H"+colorToBgr(c)+"&";
        }else{//SuperString
            SuperString ss = (SuperString)value.getParameter();
            commands = "\\2c&H"+ss.getSuperString()+"&";
        }
    }
}
