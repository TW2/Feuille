/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smallboxforfansub.karaoke.lib;

import smallboxforfansub.karaoke.lib.FontWithCoef;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author The Wingate 2940
 */
public class ProblemFont {
    
    private List<FontWithCoef> list = new ArrayList<FontWithCoef>();
    
    public ProblemFont(){
//        list.add(new FontWithCoef(new Font("Juliet", Font.PLAIN, 12), 0.67));
//        list.add(new FontWithCoef(new Font("A Love of Thunder", Font.PLAIN, 12), 0.82));
//        list.add(new FontWithCoef(new Font("Almagro", Font.PLAIN, 12), 0.72));
//        list.add(new FontWithCoef(new Font("Andalus", Font.PLAIN, 12), 0.68));
    }
    
    /*
     * Good font :
     * Aharoni ; 
     */
    
    public double coefProblemFont(Font font){
        for(FontWithCoef fwc : list){
            if(fwc.getFontName().equalsIgnoreCase(font.getName())){
                return fwc.getCoef();
            }
        }
        return 1;
    }
    
    public void addFont(FontWithCoef fwc){
        if(list.contains(fwc)==false){
            list.add(fwc);
        }        
    }
    
    public void removeFont(FontWithCoef fwc){
        if(list.contains(fwc)){
            list.remove(fwc);
        }        
    }
    
    public List<FontWithCoef> getProblemFont(){
        return list;
    }
    
    public void setProblemFont(List<FontWithCoef> list){
        this.list = list;
    }
    
}
