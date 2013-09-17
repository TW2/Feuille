/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smallboxforfansub.karaoke.xfxintegration;

import java.util.List;
import javax.swing.ImageIcon;

/**
 *
 * @author The Wingate 2940
 */
public interface IIntegration {
    
    public String getName();
    
    @Override
    public String toString();
    
    public String getCommands();
    
    public void setCommands(String commands);
    
    public List<Params> getParams();
    
    public void setParams(List<Params> paramsList);
    
    public ImageIcon getIcon();
    
    public void init();
    
    public void update();
    
}
