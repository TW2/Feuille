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
public class FontEncoding extends OIntegration {
    
    Params fontencoding;
    
    public FontEncoding(){
        name = lang.getValueOf("popm_fe")!=null ? lang.getValueOf("popm_fe") : "Font encoding";
        String p1 = lang.getValueOf("xfxparam_16")!=null ? lang.getValueOf("xfxparam_16") : "Font encoding";
        fontencoding = new Params(p1, new EncodingString(), false);
        paramsList.add(fontencoding);
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
        Pattern p = Pattern.compile("fe(.+)");
        Matcher m = p.matcher(commands);
        if(m.matches()){
            String x = m.group(1);
            if(x.contains("$")){
                fontencoding.setState(Params.State.SuperString);
                fontencoding.setBackup(new EncodingString());
                fontencoding.setParameter(new SuperString(x)); paramsList.set(0, fontencoding);                
            }else{
                fontencoding.setParameter(new EncodingString(x)); paramsList.set(0, fontencoding);
            }
        }
    }

    @Override
    public void update() {
        fontencoding = paramsList.get(0);
        if(fontencoding.getParameter() instanceof EncodingString){
            EncodingString es = (EncodingString)fontencoding.getParameter();
            commands = "\\fe"+es.getSelectedEncoding();
        }else{//SuperString
            SuperString ss = (SuperString)fontencoding.getParameter();
            commands = "\\fe"+ss.getSuperString();
        }
        
    }
}
