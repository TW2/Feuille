/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package feuille.karaoke.renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.SystemColor;
import javax.swing.JList;
import feuille.karaoke.lib.AssStyle;

/**
 *
 * @author The Wingate 2940
 */
public class listStyleRenderer extends javax.swing.JLabel
        implements javax.swing.ListCellRenderer {

    public listStyleRenderer(){
        setOpaque(true);
        setPreferredSize(new Dimension(getWidth(), 28));
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {

        if(value instanceof AssStyle){
            AssStyle as = (AssStyle)value;
            
            setBackground(isSelected ? SystemColor.textHighlight : Color.white);
            setForeground(isSelected ? Color.white : Color.black);
            setText("<html><h3>"+as.getName());
        }

        return this;
    }

}
