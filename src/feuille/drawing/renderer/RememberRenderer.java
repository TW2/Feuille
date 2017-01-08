/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.drawing.renderer;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.SystemColor;
import javax.swing.JList;
import javax.swing.SwingConstants;
import feuille.drawing.lib.Point;
import feuille.drawing.lib.Remember;

/**
 *
 * @author The Wingate 2940
 */
public class RememberRenderer extends javax.swing.JLabel
        implements javax.swing.ListCellRenderer {

    public RememberRenderer(){
        setPreferredSize(new Dimension(getWidth(), 28));
        setOpaque(true);
    }
    
    @Override
    public Component getListCellRendererComponent(JList list, Object value,
    int index, boolean isSelected, boolean cellHasFocus) {
        if(value instanceof Remember){
            Remember re = (Remember)value;
            
            if(re.isActive()){
                setText("<html><b><i>"+re.getName());
            }else{
                setText("<html><i>"+re.getName());
            }
            
            setHorizontalAlignment(SwingConstants.CENTER);
            
            if(isSelected==true){
                setBackground(SystemColor.controlHighlight);  
            }else{
                //On sait que :
                // Line = Point + Line
                // Bézier = Point + Bézier + Control point + Control point
                // BSpline = Point + BSpline
                // ReStart = ReStart
                // Move = Move
                if(re.getShape() instanceof Point){
                    try{
                        Remember nr = (Remember)list.getModel().getElementAt(index+1);
                        setBackground(re.getColorShape(nr.getShape()));
                    }catch(ArrayIndexOutOfBoundsException e){
                        setBackground(re.getColorShape());  
                    }
                }else{
                    setBackground(re.getColorShape());  
                }                          
            }
        }
        
        return this;
    }
    
}
