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
public class FontName extends OIntegration {
    
    Params fontname;
    
    public FontName(){
        name = lang.getValueOf("popm_fn")!=null ? lang.getValueOf("popm_fn") : "Font name";
        String p1 = lang.getValueOf("xfxparam_17")!=null ? lang.getValueOf("xfxparam_17") : "Font name";
        fontname = new Params(p1, new FontString(), false); paramsList.add(fontname);
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
        Pattern p = Pattern.compile("fn(.+)");
        Matcher m = p.matcher(commands);
        if(m.matches()){
            String x = m.group(1);
            if(x.contains("$")){
                fontname.setState(Params.State.SuperString);
                fontname.setBackup(new FontString());
                fontname.setParameter(new SuperString(x)); paramsList.set(0, fontname);                
            }else{
                fontname.setParameter(new FontString(x)); paramsList.set(0, fontname);
            }
        }
    }

    @Override
    public void update() {
        fontname = paramsList.get(0);
        if(fontname.getParameter() instanceof FontString){
            FontString fs = (FontString)fontname.getParameter();
            commands = "\\fn"+fs.getSelectedFont();
        }else{//SuperString
            SuperString ss = (SuperString)fontname.getParameter();
            commands = "\\fn"+ss.getSuperString();
        }
        
    }
}
