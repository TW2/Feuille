/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smallboxforfansub.drawing.renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.SystemColor;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import smallboxforfansub.scripting.DrawingScript;

/**
 *
 * @author The Wingate 2940
 */
public class ScriptsListRenderer extends JLabel implements ListCellRenderer {
    
    ImageIcon iRuby, iPython;
    
    public ScriptsListRenderer(){
        setOpaque(true);
        iRuby = new ImageIcon(getClass().getResource("AFM-mRubyScript.png"));
        iPython = new ImageIcon(getClass().getResource("AFM-mPythonScript.png"));
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
        if(value instanceof DrawingScript){
            DrawingScript scr = (DrawingScript)value;
            
            setText(scr.getName());
            if(scr.getCodeType() == DrawingScript.CodeType.JRuby){
                setIcon(iRuby);
            }else if(scr.getCodeType() == DrawingScript.CodeType.Jython){
                setIcon(iPython);
            }
            
            if(isSelected){
                setBackground(SystemColor.activeCaption);
                setForeground(Color.black);
            }else{
                setBackground(Color.white);
                setForeground(Color.gray);
            }
            
        }
        
        return this;
    }
    
}
