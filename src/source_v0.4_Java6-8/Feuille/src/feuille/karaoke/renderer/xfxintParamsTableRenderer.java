/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.karaoke.renderer;

import feuille.karaoke.xfxintegration.Params;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author The Wingate 2940
 */
public class xfxintParamsTableRenderer extends JLabel implements TableCellRenderer {

    public xfxintParamsTableRenderer(){
        setOpaque(true);
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        
        if(value instanceof Params){
            Params p = (Params)value;
            setText("  "+p.toString());
            
            if(p.isInactive()){
                setForeground(Color.red.darker());
            }else{
                setForeground(Color.green.darker());
            }
        }
        
        if(isSelected){
            setBackground(new Color(219,231,255));
        }else{
            setBackground(Color.white);
        }
        
        return this;
    }
    
}
