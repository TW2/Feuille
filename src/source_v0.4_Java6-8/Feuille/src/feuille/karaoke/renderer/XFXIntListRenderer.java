/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.karaoke.renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.SystemColor;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import feuille.karaoke.xfxintegration.OIntegration;

/**
 *
 * @author The Wingate 2940
 */
public class XFXIntListRenderer extends JLabel implements ListCellRenderer {
    
    public XFXIntListRenderer(){
        setOpaque(true);
        setPreferredSize(new Dimension(200, 20));
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
        
        if(value instanceof OIntegration){
            OIntegration oi = (OIntegration)value;            
            setBackground(isSelected ? SystemColor.textHighlight : Color.white);
            setForeground(isSelected ? Color.white : Color.black);
            setText(oi.getName());
            setIcon(oi.getIcon());
        }
        
        return this;
    }
    
}
