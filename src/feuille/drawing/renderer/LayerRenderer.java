/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.drawing.renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.SystemColor;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.border.LineBorder;
import feuille.drawing.lib.Layer;

/**
 *
 * @author The Wingate 2940
 */
public class LayerRenderer extends javax.swing.JPanel
        implements javax.swing.ListCellRenderer {
    
    JLabel lblText = new JLabel();
    JLabel lblColor = new JLabel();
    
    public LayerRenderer(){
        setOpaque(true);
        setLayout(null);
        setPreferredSize(new Dimension(getWidth(), 28));
        this.add(lblText);
        lblText.setLocation(0, 0);
        lblText.setSize(77, 28);
        lblText.setOpaque(true);
        this.add(lblColor);
        lblColor.setLocation(77, 0);
        lblColor.setSize(60, 28);
        lblColor.setOpaque(true);
        lblColor.setBorder(new LineBorder(Color.black, 2));
    }

    @Override
    public Component getListCellRendererComponent(JList list, 
    Object value, int index, boolean isSelected, boolean cellHasFocus) {
        
        if(value instanceof Layer){
            Layer layer = (Layer)value;
            
            if(layer.getName().isEmpty()){
                lblText.setText("  ID "+index);
            }else{
                lblText.setText("  "+layer.getName());
            }
            
            if(isSelected==true){
                lblText.setBackground(SystemColor.controlHighlight);  
            }else{
                lblText.setBackground(Color.white);            
            }
            lblColor.setBackground(layer.getColor());
            
            
            
        }
        
        return this;
    }
    
}
