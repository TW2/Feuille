/*
 * Copyright (C) 2019 util2
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package feuille.video;

import feuille.MainFrame;
import feuille.io.Event;
import feuille.io.Style;
import feuille.util.Time;
import feuille.util.VideoBag;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author util2
 */
public class SubRenderer {
    
    public SubRenderer() {
        
    }
    
    public static BufferedImage getSubLayer(VideoBag vb){
        BufferedImage argbImg = new BufferedImage(vb.getWidth(), vb.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = argbImg.createGraphics();
        Font oldFont = g2d.getFont();
        
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        for(Event ev : MainFrame.getScriptEvents()){
            // Set variables to really understand deal
            int startFrame = Time.getFrame(ev.getStartTime(), vb.getFps());
            int endFrame = Time.getFrame(ev.getEndTime(), vb.getFps());
            int currentFrame = vb.getFrameNumber();
            
            // If currentFrame is between the start one and the end one then:
            if(startFrame <= currentFrame && currentFrame < endFrame){
                // Get pixel size from style
                Style style = ev.getStyle();

                Font currentFont = getSubFont(style);
                g2d.setFont(currentFont);

                FontMetrics m = g2d.getFontMetrics(g2d.getFont());
                double totalSize = currentFont.getSize() * (m.getAscent() + m.getDescent()) / m.getAscent();
                
                // Get point of insertion
                Point2D p2d = getSubAlignment(g2d, ev, vb);
                
                // Draw the text
                g2d.drawString(ev.getText(), (float)p2d.getX(), (float)p2d.getY());

                g2d.setFont(oldFont);
            }
            
        }
        
        g2d.dispose();
        return argbImg;
    }
    
    private static Font getSubFont(Style style, float sizeForced){
        // Get font size from pixels
        long pixelSize = Math.round(70d);//Math.round(style.getFontsize())
        double fontSize = pixelSize * Toolkit.getDefaultToolkit().getScreenResolution() / 72d;
        
        int fontStyle = Font.PLAIN + (style.isBold() ? Font.BOLD : 0) + (style.isItalic() ? Font.ITALIC : 0);
        return new Font(style.getFontname(), fontStyle, (int)fontSize);
    }
    
    private static Font getSubFont(Style style){
        return getSubFont(style, 0f);
    }
    
    private static Point2D getSubAlignment(Graphics2D gra, Event ev, VideoBag vb){
        // Get the font
        Font font = getSubFont(ev.getStyle());
        
        // Calculate the sentence width
        FontMetrics m = gra.getFontMetrics(font);
        double totalWidth = m.stringWidth(ev.getText());
        double totalHeight = font.getSize() * (m.getAscent() + m.getDescent()) / m.getAscent();
        
        // --- Alignment ---   
        int an = ev.getStyle().getAlignment();
        int L = ev.getMarginL() != 0 ? ev.getMarginL() : ev.getStyle().getMarginL();
        int R = ev.getMarginR() != 0 ? ev.getMarginR() : ev.getStyle().getMarginR();
        int V = ev.getMarginV() != 0 ? ev.getMarginV() : ev.getStyle().getMarginV();
        
        // Get X
        double x = 20;
        if(an == 1 | an == 4 | an == 7){
            // Left
            x = L;
        }else if(an == 2 | an == 5 | an == 8){
            // Center
            x = ((vb.getWidth() - L - R) / 2) - (totalWidth / 2);
        }else if(an == 3 | an == 6 | an == 9){
            // Right
            x = vb.getWidth() - R - totalWidth;
        }
        
        // Get Y
        double y = 40;
        if(an == 1 | an == 2 | an == 3){
            // Bottom
            y = vb.getHeight() - V - totalHeight; 
        }else if(an == 4 | an == 5 | an == 6){
            // Middle
            y = ((vb.getHeight() - V * 2) / 2) + (totalHeight / 2);
        }else if(an == 7 | an == 8 | an == 9){
            // Top
            y = V;
        }
        // -----------------
        
        return new Point2D.Double(x, y);
    }
    
}
