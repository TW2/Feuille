/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.drawing.renderer;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import feuille.drawing.glyph.GlyphObject;

/**
 *
 * @author The Wingate 2940
 */
public class GlyphRenderer extends JLabel implements ListCellRenderer {
    
    public GlyphRenderer(){
        setOpaque(true);
        setSize(getWidth(),80);
        setHorizontalAlignment(CENTER);
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
        
        if(value instanceof GlyphObject){
            GlyphObject go = (GlyphObject)value;
            setFont(go.getFont());
            setText(go.getGlyph());
            setForeground(Color.black);
            setBackground(Color.white);
        }
        
        if(isSelected){
            setForeground(Color.red);
            setBackground(new Color(240,240,240));
        }
                
        return this;
    }
    
}
