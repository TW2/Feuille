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
public class GTransparency2 extends OIntegration {
    
    Params one, two, three, four;
    
    public GTransparency2(){
        name = lang.getValueOf("popm_2va")!=null ? lang.getValueOf("popm_2va") : "Gradients on karaoke (transparency)";
        String p1 = lang.getValueOf("xfxparam_21")!=null ? lang.getValueOf("xfxparam_21") : "Transparency";
        one = new Params(p1, new TransparencyString(), false); paramsList.add(one);
        two = new Params(p1, new TransparencyString(), false); paramsList.add(two);
        three = new Params(p1, new TransparencyString(), false); paramsList.add(three);
        four = new Params(p1, new TransparencyString(), false); paramsList.add(four);
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
        Pattern p = Pattern.compile("2va\\((.+),(.+),(.+),(.+)");
        Matcher m = p.matcher(commands);
        if(m.matches()){
            String x;
            x = m.group(1);
            if(x.contains("$")){
                one.setState(Params.State.SuperString);
                one.setBackup(new TransparencyString());
                one.setParameter(new SuperString(x)); paramsList.set(0, one);                
            }else{
                one.setParameter(new TransparencyString(x)); paramsList.set(0, one);
            }
            x = m.group(2);
            if(x.contains("$")){
                two.setState(Params.State.SuperString);
                two.setBackup(new TransparencyString());
                two.setParameter(new SuperString(x)); paramsList.set(1, two);                
            }else{
                two.setParameter(new TransparencyString(x)); paramsList.set(1, two);
            }
            x = m.group(3);
            if(x.contains("$")){
                three.setState(Params.State.SuperString);
                three.setBackup(new TransparencyString());
                three.setParameter(new SuperString(x)); paramsList.set(2, three);                
            }else{
                three.setParameter(new TransparencyString(x)); paramsList.set(2, three);
            }
            x = m.group(4);
            if(x.contains("$")){
                four.setState(Params.State.SuperString);
                four.setBackup(new TransparencyString());
                four.setParameter(new SuperString(x)); paramsList.set(3, four);                
            }else{
                four.setParameter(new TransparencyString(x)); paramsList.set(3, four);
            }
        }
    }

    @Override
    public void update() {
        String s1, s2, s3, s4;
        one = paramsList.get(0);
        if(one.getParameter() instanceof TransparencyString){
            TransparencyString ts1 = (TransparencyString)one.getParameter(); s1 = ts1.getTransparency();
        }else{//SuperString
            SuperString ss = (SuperString)one.getParameter(); s1 = ss.getSuperString();
        }
        two = paramsList.get(1);
        if(two.getParameter() instanceof TransparencyString){
            TransparencyString ts2 = (TransparencyString)two.getParameter(); s2 = ts2.getTransparency();
        }else{//SuperString
            SuperString ss = (SuperString)two.getParameter(); s2 = ss.getSuperString();
        }
        three = paramsList.get(2);
        if(three.getParameter() instanceof TransparencyString){
            TransparencyString ts3 = (TransparencyString)three.getParameter(); s3 = ts3.getTransparency();
        }else{//SuperString
            SuperString ss = (SuperString)three.getParameter(); s3 = ss.getSuperString();
        }        
        four = paramsList.get(3);
        if(four.getParameter() instanceof TransparencyString){
            TransparencyString ts4 = (TransparencyString)four.getParameter(); s4 = ts4.getTransparency();
        }else{//SuperString
            SuperString ss = (SuperString)four.getParameter(); s4 = ss.getSuperString();
        }        
        commands = "\\2va("+s1+","+s2+","+s3+","+s4+")";
    }
}
