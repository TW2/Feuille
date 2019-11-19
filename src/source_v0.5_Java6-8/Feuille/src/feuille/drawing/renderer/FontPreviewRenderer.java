/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.drawing.renderer;

import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author The Wingate 2940
 */
public class FontPreviewRenderer extends JLabel implements ListCellRenderer {
    
    private Font f = new Font("Arial",Font.PLAIN, 200);
    
    public FontPreviewRenderer(){
        
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value,
    int index, boolean isSelected, boolean cellHasFocus) {
        
        if (value instanceof Font){
            f = (Font)value;
        }
        setText(f.getFontName());
        
        return this;
    }
    
}
