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
public class WrappingStyle extends OIntegration {
    
    Params wrappingstyle;
    
    public WrappingStyle(){
        name = lang.getValueOf("popm_q")!=null ? lang.getValueOf("popm_q") : "Wrapping style";
        String p1 = lang.getValueOf("xfxparam_55")!=null ? lang.getValueOf("xfxparam_55") : "Wrapping style";
        wrappingstyle = new Params(p1, new WrappingString(), false);
        paramsList.add(wrappingstyle);
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
        Pattern p = Pattern.compile("q(.+)");
        Matcher m = p.matcher(commands);
        if(m.matches()){
            String x = m.group(1);
            if(x.contains("$")){
                wrappingstyle.setState(Params.State.SuperString);
                wrappingstyle.setBackup(new WrappingString());
                wrappingstyle.setParameter(new SuperString(x)); paramsList.set(0, wrappingstyle);                
            }else{
                wrappingstyle.setParameter(new WrappingString(x)); paramsList.set(0, wrappingstyle);
            }            
        }
    }

    @Override
    public void update() {
        wrappingstyle = paramsList.get(0);
        if(wrappingstyle.getParameter() instanceof WrappingString){
            WrappingString ws = (WrappingString)wrappingstyle.getParameter();
            commands = "\\q"+ws.getSelectedWrapping();
        }else{//SuperString
            SuperString ss = (SuperString)wrappingstyle.getParameter();
            commands = "\\q"+ss.getSuperString();
        }
    }
}
