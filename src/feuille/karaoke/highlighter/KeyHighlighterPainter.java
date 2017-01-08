/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.karaoke.highlighter;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;

/**
 *
 * @author The Wingate 2940
 */
public class KeyHighlighterPainter implements Highlighter.HighlightPainter {
    
    Color color = Color.blue;
    
    public KeyHighlighterPainter(){
        
    }
    
    public KeyHighlighterPainter(Color color){
        this.color = color;
    }

    @Override
    public void paint(Graphics g, int p0, int p1, Shape bounds, JTextComponent c) {
        g.setColor(color);
        
        Rectangle r0 = null, r1 = null;
        try {
            r0 = c.modelToView(p0);
            r1 = c.modelToView(p1);
        } catch (BadLocationException ex) {
            //Nothing
        }
        
        if (r0 != null && r1 != null) {
            if(c instanceof JTextField){
                g.drawString(c.getText().substring(p0, p1), r0.x, r0.y+r0.height-2);
            }else if(c instanceof JTextArea){
                String str = c.getText().substring(p0, p1);
                int strMeasure = g.getFontMetrics().stringWidth(str);
                if(r0.x+strMeasure>c.getWidth()-12){
                    int posX = r0.x, posY = r0.y+r0.height-3; double countLine = 1;
                    
                    for(char ch : str.toCharArray()){
                        int chMeasure = g.getFontMetrics().charWidth(ch);
                        if(posX+chMeasure>c.getWidth()-12){
                            
                            Double d1 = new Double(posX+"");
                            Double d2 = new Double(strMeasure+"");
                            Double d3 = new Double(c.getWidth()+"");
                                                        
                            if((d1+d2)/d3>countLine){
                                posX = 6;
                                posY += r0.height;
                                countLine += 1.0d;
                            }
                        }
                        g.drawString(ch+"", posX, posY);
                        posX += chMeasure;
                    }
                }else{
                    g.drawString(str, r0.x, r0.y+r0.height-3);
                }
            }
        }
    }
    
}
