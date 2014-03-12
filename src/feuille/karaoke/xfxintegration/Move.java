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
public class Move extends OIntegration {
    
    Params posX1, posY1, posX2, posY2, t1, t2;
    
    public Move(){
        name = lang.getValueOf("popm_move")!=null ? lang.getValueOf("popm_move") : "Position in real time";
        String p1 = lang.getValueOf("xfxparam_32")!=null ? lang.getValueOf("xfxparam_32") : "Position on X1 (start)";
        String p2 = lang.getValueOf("xfxparam_33")!=null ? lang.getValueOf("xfxparam_33") : "Position on Y1 (start)";
        String p3 = lang.getValueOf("xfxparam_34")!=null ? lang.getValueOf("xfxparam_34") : "Position on X2 (end)";
        String p4 = lang.getValueOf("xfxparam_35")!=null ? lang.getValueOf("xfxparam_35") : "Position on Y2 (end)";
        String p5 = lang.getValueOf("xfxparam_02")!=null ? lang.getValueOf("xfxparam_02") : "Start";
        String p6 = lang.getValueOf("xfxparam_03")!=null ? lang.getValueOf("xfxparam_03") : "End";
        posX1 = new Params(p1, "0", false);
        paramsList.add(posX1);
        posY1 = new Params(p2, "0", false);
        paramsList.add(posY1);
        posX2 = new Params(p3, "0", false);
        paramsList.add(posX2);
        posY2 = new Params(p4, "0", false);
        paramsList.add(posY2);
        t1 = new Params(p5, "0", true);
        paramsList.add(t1);
        t2 = new Params(p6, "0", true);
        paramsList.add(t2);
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
        String[] table = commands.split(",");
        if(table.length==6){
            Pattern p = Pattern.compile("move\\((.+),(.+),(.+),(.+),(.+),(.+)");
            Matcher m = p.matcher(commands);
            if(m.matches()){
                String x;
                x = m.group(1); posX1.setParameter(x); paramsList.set(0, posX1);
                x = m.group(2); posY1.setParameter(x); paramsList.set(1, posY1);
                x = m.group(3); posX2.setParameter(x); paramsList.set(2, posX2);
                x = m.group(4); posY2.setParameter(x); paramsList.set(3, posY2);
                x = m.group(5); t1.setParameter(x); paramsList.set(4, t1);
                x = m.group(6); t2.setParameter(x); paramsList.set(5, t2);
            }
        }else{
            Pattern p = Pattern.compile("move\\((.+),(.+),(.+),(.+)");
            Matcher m = p.matcher(commands);
            if(m.matches()){
                String x;
                x = m.group(1); posX1.setParameter(x); paramsList.set(0, posX1);
                x = m.group(2); posY1.setParameter(x); paramsList.set(1, posY1);
                x = m.group(3); posX2.setParameter(x); paramsList.set(2, posX2);
                x = m.group(4); posY2.setParameter(x); paramsList.set(3, posY2);
                t1.setInactive(true); paramsList.set(4, t1);
                t2.setInactive(true); paramsList.set(5, t2);
            }
        }
        
    }

    @Override
    public void update() {
        posX1 = paramsList.get(0); String s1 = (String)posX1.getParameter();
        posY1 = paramsList.get(1); String s2 = (String)posY1.getParameter();
        posX2 = paramsList.get(2); String s3 = (String)posX2.getParameter();
        posY2 = paramsList.get(3); String s4 = (String)posY2.getParameter();
        t1 = paramsList.get(4); String s5 = (String)t1.getParameter();
        t2 = paramsList.get(5); String s6 = (String)t2.getParameter();
        
        if(t1.isInactive() && t2.isInactive()){
            commands = "\\move("+s1+","+s2+","+s3+","+s4+")";
        }else{
            commands = "\\move("+s1+","+s2+","+s3+","+s4+","+s5+","+s6+")";
        }
    }
}
