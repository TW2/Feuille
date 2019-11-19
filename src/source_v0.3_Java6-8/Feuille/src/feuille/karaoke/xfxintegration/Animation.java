/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.karaoke.xfxintegration;

import javax.swing.ImageIcon;

/**
 *
 * @author The Wingate 2940
 */
public class Animation extends OIntegration {
    
    Params start, end, accel;
    
    public Animation(){
        name = lang.getValueOf("popm_t")!=null ? lang.getValueOf("popm_t") : "Animation";
        String p1 = lang.getValueOf("xfxparam_02")!=null ? lang.getValueOf("xfxparam_02") : "Start";
        String p2 = lang.getValueOf("xfxparam_03")!=null ? lang.getValueOf("xfxparam_03") : "End";
        String p3 = lang.getValueOf("xfxparam_04")!=null ? lang.getValueOf("xfxparam_04") : "Acceleration";
        start = new Params(p1, "%sK", true); paramsList.add(start);
        end = new Params(p2,"%eK", true); paramsList.add(end);
        accel = new Params(p3,"0", true); paramsList.add(accel);
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
        if(commands.contains(",")){
            commands = commands.substring(2);
            String[] table = commands.split(",");
            if(table.length==2){//contains start and end
                //command : \t(start,end,animation commands)
                start.setParameter(table[0]); paramsList.set(0, start);
                end.setParameter(table[1]); paramsList.set(1, end);
                accel.setInactive(true); paramsList.set(2, accel);
            }else if(table.length==3){//contains start, end and accel
                //command : \t(start,end,accel,animation commands)
                start.setParameter(table[0]); paramsList.set(0, start);
                end.setParameter(table[1]); paramsList.set(1, end);
                accel.setParameter(table[2]); paramsList.set(2, accel);
            }
        }else{
            start.setInactive(true); paramsList.set(0, start);
            end.setInactive(true); paramsList.set(1, end);
            accel.setInactive(true); paramsList.set(2, accel);
        }
    }

    @Override
    public void update() {
        start = paramsList.get(0); String sstart = (String)start.getParameter();
        end = paramsList.get(1); String send = (String)end.getParameter();
        accel = paramsList.get(2); String saccel = (String)accel.getParameter();
        if(start.isInactive() & end.isInactive() & accel.isInactive()){
            commands = "\\t(";
        }else if(accel.isInactive()){
            commands = "\\t("+sstart+","+send+",";
        }else{
            commands = "\\t("+sstart+","+send+","+saccel+",";
        }        
    }
    
    
    
}
