/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.karaoke.xfxintegration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;

/**
 * \4img(path_to_png_file[,xoffset,yoffset])
 * @author The Wingate 2940
 */
public class Image4 extends OIntegration {
    
    Params path_to_png_file,xoffset,yoffset;
    
    public Image4(){
        name = lang.getValueOf("popm_4img")!=null ? lang.getValueOf("popm_4img") : "Image fill on shader";
        String p1 = lang.getValueOf("xfxparam_22")!=null ? lang.getValueOf("xfxparam_22") : "File";
        String p2 = lang.getValueOf("xfxparam_23")!=null ? lang.getValueOf("xfxparam_23") : "Offset on X";
        String p3 = lang.getValueOf("xfxparam_24")!=null ? lang.getValueOf("xfxparam_24") : "Offset on Y";
        path_to_png_file = new Params(p1, new ImageString(), false); paramsList.add(path_to_png_file);
        xoffset = new Params(p2, "0", true); paramsList.add(xoffset);
        yoffset = new Params(p3, "0", true); paramsList.add(yoffset);
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
        if(commands.contains(",")){
            Pattern p = Pattern.compile("4img\\((.+),(.+),(.+)");
            Matcher m = p.matcher(commands);
            if(m.matches()){
                String x = m.group(1);
                if(x.contains("$")){
                    path_to_png_file.setState(Params.State.SuperString);
                    path_to_png_file.setBackup(new ImageString());
                    path_to_png_file.setParameter(new SuperString(x)); paramsList.set(0, path_to_png_file);                
                }else{
                    path_to_png_file.setParameter(new ImageString(x)); paramsList.set(0, path_to_png_file);
                }
                x = m.group(2); xoffset.setParameter(x); paramsList.set(1, xoffset);
                x = m.group(3); yoffset.setParameter(x); paramsList.set(2, yoffset);
            }
        }else{
            Pattern p = Pattern.compile("4img\\((.+)");
            Matcher m = p.matcher(commands);
            if(m.matches()){
                String x = m.group(1);
                if(x.contains("$")){
                    path_to_png_file.setState(Params.State.SuperString);
                    path_to_png_file.setBackup(new ImageString());
                    path_to_png_file.setParameter(new SuperString(x)); paramsList.set(0, path_to_png_file);                
                }else{
                    path_to_png_file.setParameter(new ImageString(x)); paramsList.set(0, path_to_png_file);
                }
                xoffset.setInactive(true); paramsList.set(1, xoffset);
                yoffset.setInactive(true); paramsList.set(2, yoffset);
            }
        }
        
    }

    @Override
    public void update() {
        path_to_png_file = paramsList.get(0); String s1;
        if(path_to_png_file.getParameter() instanceof ImageString){
            ImageString is = (ImageString)path_to_png_file.getParameter(); s1 = is.getImage();
        }else{//SuperString
            SuperString ss = (SuperString)path_to_png_file.getParameter(); s1 = ss.getSuperString();
        }
        xoffset = paramsList.get(1); String x = (String)xoffset.getParameter();
        yoffset = paramsList.get(2); String y = (String)yoffset.getParameter();
        
        if(xoffset.isInactive() && yoffset.isInactive()){
            commands = "\\4img("+s1+")";
        }else{
            commands = "\\4img("+s1+","+x+","+y+")";
        }
    }
}
