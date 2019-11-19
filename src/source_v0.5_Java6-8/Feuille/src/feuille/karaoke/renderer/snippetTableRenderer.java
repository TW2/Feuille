/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.karaoke.renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.SystemColor;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import feuille.scripting.Snippet;
import feuille.scripting.SnippetElement;

/**
 *
 * @author The Wingate 2940
 */
public class snippetTableRenderer extends JLabel implements TableCellRenderer {
    
    public snippetTableRenderer(){
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        
        if (value instanceof URL){
            URL url = (URL)value;
            setText("");
            setIcon(new ImageIcon(url));
        }
        
        if(value instanceof String){
            String s = (String)value;
            setText(s);
            setIcon(null);
        }
        
        if(value instanceof Snippet){
            Snippet sni = (Snippet)value;
            setText(sni.toString());
            setIcon(null);
        }
        
        if(value instanceof SnippetElement){
            SnippetElement se = (SnippetElement)value;
            setText(se.toString());
            setIcon(null);
        }
        
        setForeground(isSelected==true? Color.white : Color.black);
        if(isEven(row)==true){            
            setBackground(isSelected==true? SystemColor.activeCaption : SystemColor.controlHighlight);
            if(System.getProperty("os.name").equalsIgnoreCase("Mac OS X")){
                Color cActive = new Color(56,117,215);
                Color cHighLight = new Color(208,208,208);
                setBackground(isSelected==true? cActive : cHighLight);
            }
        }else{
            setBackground(isSelected==true? SystemColor.activeCaption : Color.white);
            if(System.getProperty("os.name").equalsIgnoreCase("Mac OS X")){
                Color cActive = new Color(56,117,215);
                setBackground(isSelected==true? cActive : Color.white);
            }
        }
        
        return this;
    }
    
    private boolean isEven(int number){
        String s = Integer.toString(number);
        if(s.endsWith("1")){
            return false;
        }else if(s.endsWith("3")){
            return false;
        }else if(s.endsWith("5")){
            return false;
        }else if(s.endsWith("7")){
            return false;
        }else if(s.endsWith("9")){
            return false;
        }else{
            return true;
        }        
    }
    
}
