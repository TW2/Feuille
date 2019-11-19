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
public class Transparency4 extends OIntegration {
    
    Params value;
    
    public Transparency4(){
        name = lang.getValueOf("popm_4a")!=null ? lang.getValueOf("popm_4a") : "Shader transparency";
        String p1 = lang.getValueOf("xfxparam_21")!=null ? lang.getValueOf("xfxparam_21") : "Transparency";
        value = new Params(p1, new TransparencyString(), false); paramsList.add(value);
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
        Pattern p = Pattern.compile("4a&H(.+)&");
        Matcher m = p.matcher(commands);
        if(m.matches()){
            String x = m.group(1);
            if(x.contains("$")){
                value.setState(Params.State.SuperString);
                value.setBackup(new TransparencyString());
                value.setParameter(new SuperString(x)); paramsList.set(0, value);                
            }else{
                value.setParameter(new TransparencyString(x)); paramsList.set(0, value);
            }
        }
    }

    @Override
    public void update() {
        value = paramsList.get(0);
        if(value.getParameter() instanceof TransparencyString){
            TransparencyString ts = (TransparencyString)value.getParameter();
            commands = "\\4a&H"+ts.getTransparency()+"&";
        }else{//SuperString
            SuperString ss = (SuperString)value.getParameter();
            commands = "\\4a&H"+ss.getSuperString()+"&";
        }
    }
}
