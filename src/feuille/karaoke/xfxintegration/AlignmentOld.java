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
public class AlignmentOld extends OIntegration {
    
    Params align;
    
    public AlignmentOld(){
        name = lang.getValueOf("popm_a")!=null ? lang.getValueOf("popm_a") : "Alignment (old)";
        String p1 = lang.getValueOf("xfxparam_01")!=null ? lang.getValueOf("xfxparam_01") : "Alignment";
        align = new Params(p1, new AlignOldString(), false);
        paramsList.add(align);
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
        //command : \aX where X is an integer from 1 to 11 except 4 and 8
        Pattern p = Pattern.compile("a(.+)");
        Matcher m = p.matcher(commands);
        if(m.matches()){
            String x = m.group(1);
            if(x.contains("$")){
                align.setState(Params.State.SuperString);
                align.setBackup(new AlignOldString());
                align.setParameter(new SuperString(x)); paramsList.set(0, align);                
            }else{
                align.setParameter(new AlignOldString(x)); paramsList.set(0, align);
            }
        }        
    }

    @Override
    public void update() {
        align = paramsList.get(0);
        if(align.getParameter() instanceof AlignOldString){
            AlignOldString aos = (AlignOldString)align.getParameter();
            commands = "\\a"+aos.getSelectedAlignOld();
        }else{//SuperString
            SuperString ss = (SuperString)align.getParameter();
            commands = "\\a"+ss.getSuperString();
        }
        AlignOldString aos = (AlignOldString)align.getParameter();
        commands = "\\a"+aos.getSelectedAlignOld();
    }
    
}
