/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.karaoke.xfxintegration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;

/**
 * \jitter(left,right,up,down,period[,seed])
 * @author The Wingate 2940
 */
public class Jitter extends OIntegration {
    
    Params left,right,up,down,period,seed;
    
    public Jitter(){
        name = lang.getValueOf("popm_jitter")!=null ? lang.getValueOf("popm_jitter") : "Shaking";
        String p1 = lang.getValueOf("xfxparam_25")!=null ? lang.getValueOf("xfxparam_25") : "Left";
        String p2 = lang.getValueOf("xfxparam_26")!=null ? lang.getValueOf("xfxparam_26") : "Right";
        String p3 = lang.getValueOf("xfxparam_27")!=null ? lang.getValueOf("xfxparam_27") : "Up";
        String p4 = lang.getValueOf("xfxparam_28")!=null ? lang.getValueOf("xfxparam_28") : "Down";
        String p5 = lang.getValueOf("xfxparam_29")!=null ? lang.getValueOf("xfxparam_29") : "Period";
        String p6 = lang.getValueOf("xfxparam_30")!=null ? lang.getValueOf("xfxparam_30") : "Seed";
        left = new Params(p1, "0", false); paramsList.add(left);
        right = new Params(p2, "0", false); paramsList.add(right);
        up = new Params(p3, "0", false); paramsList.add(up);
        down = new Params(p4, "0", false); paramsList.add(down);
        period = new Params(p5, "0", false); paramsList.add(period);
        seed = new Params(p6, "0", true); paramsList.add(seed);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ImageIcon getIcon() {
        ImageIcon ii = new ImageIcon(getClass().getResource("20px-Crystal_Clear_app_ksame.png"));
        return ii;
    }
    
    @Override
    public void init() {
        String[] table = commands.split(",");
        if(table.length==6){
            Pattern p = Pattern.compile("jitter\\((.+),(.+),(.+),(.+),(.+),(.+)");
            Matcher m = p.matcher(commands);
            if(m.matches()){
                String x;
                x = m.group(1); left.setParameter(x); paramsList.set(0, left);
                x = m.group(2); right.setParameter(x); paramsList.set(1, right);
                x = m.group(3); up.setParameter(x); paramsList.set(2, up);
                x = m.group(4); down.setParameter(x); paramsList.set(3, down);
                x = m.group(5); period.setParameter(x); paramsList.set(4, period);
                x = m.group(6); seed.setParameter(x); paramsList.set(5, seed);
            }
        }else{
            Pattern p = Pattern.compile("jitter\\((.+),(.+),(.+),(.+),(.+)");
            Matcher m = p.matcher(commands);
            if(m.matches()){
                String x;
                x = m.group(1); left.setParameter(x); paramsList.set(0, left);
                x = m.group(2); right.setParameter(x); paramsList.set(1, right);
                x = m.group(3); up.setParameter(x); paramsList.set(2, up);
                x = m.group(4); down.setParameter(x); paramsList.set(3, down);
                x = m.group(5); period.setParameter(x); paramsList.set(4, period);
                seed.setInactive(true); paramsList.set(5, seed);
            }
        }
        
    }

    @Override
    public void update() {
        left = paramsList.get(0); String s1 = (String)left.getParameter();
        right = paramsList.get(1); String s2 = (String)right.getParameter();
        up = paramsList.get(2); String s3 = (String)up.getParameter();
        down = paramsList.get(3); String s4 = (String)down.getParameter();
        period = paramsList.get(4); String s5 = (String)period.getParameter();
        seed = paramsList.get(5); String s6 = (String)seed.getParameter();
        
        if(seed.isInactive()){
            commands = "\\jitter("+s1+","+s2+","+s3+","+s4+","+s5+")";
        }else{
            commands = "\\jitter("+s1+","+s2+","+s3+","+s4+","+s5+","+s6+")";
        }
    }
}
