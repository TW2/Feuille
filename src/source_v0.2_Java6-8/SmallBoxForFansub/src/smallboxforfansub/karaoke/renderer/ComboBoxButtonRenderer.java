/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smallboxforfansub.karaoke.renderer;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import smallboxforfansub.scripting.SButton;

/**
 *
 * @author The Wingate 2940
 */
public class ComboBoxButtonRenderer extends JButton implements ListCellRenderer {
    
    public ComboBoxButtonRenderer(){
        
    }
    
    @Override
    public Component getListCellRendererComponent(JList list, Object value,
    int index, boolean isSelected, boolean cellHasFocus) {
        
        if(value instanceof SButton){
            SButton sb = (SButton)value;
            setText("<html><h3>"+sb.getDisplayName());
            if(sb.getType().equalsIgnoreCase("function")){
                setBackground(new Color(0,21,119));
                setForeground(Color.white);
            }else if(sb.getType().equalsIgnoreCase("macro")){
                setBackground(new Color(38,96,2));
                setForeground(Color.white);
            }else if(sb.getType().equalsIgnoreCase("shortcut")){
                setBackground(new Color(181,6,0));
                setForeground(Color.white);
            }else if(sb.getType().equalsIgnoreCase("command")){
                setBackground(Color.darkGray);
                setForeground(Color.white);
            }
        }
        
        return this;
    }
    
}
