/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smallboxforfansub.karaoke.renderer;

import smallboxforfansub.karaoke.xfxintegration.*;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author The Wingate 2940
 */
public class xfxintObjectTableRenderer extends JLabel implements TableCellRenderer {

    public xfxintObjectTableRenderer(){
        setOpaque(true);
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        
        if(value instanceof Color){
            Color c = (Color)value;
            setBackground(c);
            setForeground(new Color(255-c.getRed(),255-c.getGreen(),255-c.getBlue()));
            setText(colorToBgr(c));
            setBorder(new LineBorder(Color.white, 2));
        }else if(value instanceof String){
            String str = (String)value;
            setBackground(isSelected ? new Color(219,231,255) : Color.white);
            setForeground(Color.black);
            setText(str);
            setBorder(null);
        }else if(value instanceof FontString){
            FontString fs = (FontString)value;
            setBackground(isSelected ? new Color(219,231,255) : Color.white);
            setForeground(Color.black);
            setText(fs.getSelectedFont());
            setBorder(null);
        }else if(value instanceof EncodingString){
            EncodingString es = (EncodingString)value;
            setBackground(isSelected ? new Color(219,231,255) : Color.white);
            setForeground(Color.black);
            setText(es.getSelectedEncoding());
            setBorder(null);
        }else if(value instanceof WrappingString){
            WrappingString ws = (WrappingString)value;
            setBackground(isSelected ? new Color(219,231,255) : Color.white);
            setForeground(Color.black);
            setText(ws.getSelectedWrapping());
            setBorder(null);
        }else if(value instanceof AlignOldString){
            AlignOldString aos = (AlignOldString)value;
            setBackground(isSelected ? new Color(219,231,255) : Color.white);
            setForeground(Color.black);
            setText(aos.getSelectedAlignOld());
            setBorder(null);
        }else if(value instanceof AlignString){
            AlignString as = (AlignString)value;
            setBackground(isSelected ? new Color(219,231,255) : Color.white);
            setForeground(Color.black);
            setText(as.getSelectedAlign());
            setBorder(null);
        }else if(value instanceof DrawingString){
            DrawingString ds = (DrawingString)value;
            setBackground(isSelected ? new Color(219,231,255) : Color.white);
            setForeground(Color.black);
            setText(ds.getDrawing());
            setBorder(null);
        }else if(value instanceof TransparencyString){
            TransparencyString ts = (TransparencyString)value;
            setBackground(isSelected ? new Color(219,231,255) : Color.white);
            setForeground(Color.black);
            setText(ts.getTransparency());
            setBorder(null);
        }else if(value instanceof ImageString){
            ImageString is = (ImageString)value;
            setBackground(isSelected ? new Color(219,231,255) : Color.white);
            setForeground(Color.black);
            setText(is.getImage());
            setBorder(null);
        }else if(value instanceof SuperString){
            SuperString ss = (SuperString)value;
            setBackground(isSelected ? new Color(219,231,255) : Color.white);
            setForeground(Color.black);
            setText(ss.getSuperString());
            setBorder(null);
        }
                
        return this;
    }
    
    private String colorToBgr(Color c){
        int r, g, b;
        r = c.getRed();
        g = c.getGreen();
        b = c.getBlue();
        String blue, green, red;
        blue = Integer.toString(b, 16); if(blue.length()<2){blue="0"+blue;}
        green = Integer.toString(g, 16); if(green.length()<2){green="0"+green;}
        red = Integer.toString(r, 16); if(red.length()<2){red="0"+red;}
        String bgr = blue+green+red;
        return bgr.toUpperCase();
    }
    
}
