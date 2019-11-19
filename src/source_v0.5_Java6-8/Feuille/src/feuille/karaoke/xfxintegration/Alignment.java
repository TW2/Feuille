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
public class Alignment extends OIntegration {
    
    Params align;
    
    public Alignment(){
        name = lang.getValueOf("popm_an")!=null ? lang.getValueOf("popm_an") : "Alignment";
        String p1 = lang.getValueOf("xfxparam_01")!=null ? lang.getValueOf("xfxparam_01") : "Alignment";
        align = new Params(p1, new AlignString(), false);
        paramsList.add(align);
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
        //command : \anX where X is an integer from 1 to 9
        Pattern p = Pattern.compile("an(.+)");
        Matcher m = p.matcher(commands);
        if(m.matches()){
            String x = m.group(1);
            if(x.contains("$")){
                align.setState(Params.State.SuperString);
                align.setBackup(new AlignString());
                align.setParameter(new SuperString(x)); paramsList.set(0, align);                
            }else{
                align.setParameter(new AlignString(x)); paramsList.set(0, align);
            }
        }
    }

    @Override
    public void update() {
        align = paramsList.get(0);
        if(align.getParameter() instanceof AlignString){
            AlignString as = (AlignString)align.getParameter();
            commands = "\\an"+as.getSelectedAlign();
        }else{//SuperString
            SuperString ss = (SuperString)align.getParameter();
            commands = "\\an"+ss.getSuperString();
        }
    }
    
}
