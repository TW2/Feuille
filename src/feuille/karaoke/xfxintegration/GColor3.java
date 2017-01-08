/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.karaoke.xfxintegration;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;

/**
 *
 * @author The Wingate 2940
 */
public class GColor3 extends OIntegration {
    
    Params one, two, three, four;
    
    public GColor3(){
        name = lang.getValueOf("popm_3vc")!=null ? lang.getValueOf("popm_3vc") : "Gradients on border (color)";
        String p1 = lang.getValueOf("xfxparam_08")!=null ? lang.getValueOf("xfxparam_08") : "Color";
        one = new Params(p1, Color.orange, false); paramsList.add(one);
        two = new Params(p1, Color.orange, false); paramsList.add(two);
        three = new Params(p1, Color.red, false); paramsList.add(three);
        four = new Params(p1, Color.red, false); paramsList.add(four);
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
        Pattern p = Pattern.compile("3vc\\((.+),(.+),(.+),(.+)");
        Matcher m = p.matcher(commands);
        if(m.matches()){
            String x;
            x = m.group(1);
            if(x.contains("$")){
                one.setState(Params.State.SuperString);
                one.setBackup(Color.orange);
                one.setParameter(new SuperString(x)); paramsList.set(0, one);                
            }else{
                try{
                    one.setParameter(bgrToColor(x)); paramsList.set(0, one);
                }catch(Exception exc){
                    one.setParameter(Color.orange); paramsList.set(0, one);
                }
            }
            x = m.group(2);
            if(x.contains("$")){
                two.setState(Params.State.SuperString);
                two.setBackup(Color.orange);
                two.setParameter(new SuperString(x)); paramsList.set(1, two);                
            }else{
                try{
                    two.setParameter(bgrToColor(x)); paramsList.set(1, two);
                }catch(Exception exc){
                    two.setParameter(Color.orange); paramsList.set(1, two);
                }
            }
            x = m.group(3);
            if(x.contains("$")){
                three.setState(Params.State.SuperString);
                three.setBackup(Color.red);
                three.setParameter(new SuperString(x)); paramsList.set(2, three);                
            }else{
                try{
                    three.setParameter(bgrToColor(x)); paramsList.set(2, three);
                }catch(Exception exc){
                    three.setParameter(Color.red); paramsList.set(2, three);
                }
            }
            x = m.group(4);
            if(x.contains("$")){
                four.setState(Params.State.SuperString);
                four.setBackup(Color.red);
                four.setParameter(new SuperString(x)); paramsList.set(3, four);                
            }else{
                try{
                    four.setParameter(bgrToColor(x)); paramsList.set(3, four);
                }catch(Exception exc){
                    four.setParameter(Color.red); paramsList.set(3, four);
                }
            }
        }
    }

    @Override
    public void update() {
        String s1, s2, s3, s4;
        one = paramsList.get(0);
        if(one.getParameter() instanceof Color){
            Color c1 = (Color)one.getParameter(); s1 = colorToBgr(c1);
        }else{//SuperString
            SuperString ss = (SuperString)one.getParameter(); s1 = ss.getSuperString();
        }
        two = paramsList.get(1);
        if(two.getParameter() instanceof Color){
            Color c2 = (Color)two.getParameter(); s2 = colorToBgr(c2);
        }else{//SuperString
            SuperString ss = (SuperString)two.getParameter(); s2 = ss.getSuperString();
        }
        three = paramsList.get(2);
        if(three.getParameter() instanceof Color){
            Color c3 = (Color)three.getParameter(); s3 = colorToBgr(c3);
        }else{//SuperString
            SuperString ss = (SuperString)three.getParameter(); s3 = ss.getSuperString();
        }        
        four = paramsList.get(3);
        if(four.getParameter() instanceof Color){
            Color c4 = (Color)four.getParameter(); s4 = colorToBgr(c4);
        }else{//SuperString
            SuperString ss = (SuperString)four.getParameter(); s4 = ss.getSuperString();
        }        
        commands = "\\3vc("+s1+","+s2+","+s3+","+s4+")";
    }
}
