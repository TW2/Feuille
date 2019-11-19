/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.karaoke.lib;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;

/**
 * <p>This class is an ASS style preview panel.<br />
 * Cette classe est un panel de prévisualisation de style ASS.</p>
 * @author The Wingate 2940
 */
public class AssStylePreview extends javax.swing.JPanel {
    
    private String strToRender = "Java !?";
    private AssStyle as = new AssStyle();
    private Shape foreShape = null;
    private Shape backShape = null;
    
//    private Font font = new Font(Font.SERIF, Font.PLAIN, 20);
//    private Color TextColor = Color.yellow;
    private float TextAlpha = 0f;
//    private Color OutlineColor = Color.black;
    private float OutlineAlpha = 0f;
//    private Color ShadowColor = Color.black;
    private float ShadowAlpha = 0f;
    
    /** <p>Create a new panel with a default text.<br />
     * Crée un nouveau panel avec un texte par défaut.</p> */
    public AssStylePreview(){
        setOpaque(true);
        updateText();
    }
    
    /** <p>Create a new panel with a defined text.<br />
     * Crée un nouveau panel avec un texte défini.</p> */
    public AssStylePreview(String strToRender){
        setOpaque(true);
        this.strToRender = strToRender;
        updateText();
    }
    
    /** <p>Set the text to render.<br />
     * Définit le texte pour le rendu.</p> */
    public void setString(String strToRender){
        this.strToRender = strToRender;
        updateText();
        repaint();
    }
    
    /** <p>Set the ASS style and refresh the panel.<br />
     * Définit le style ASS et rafraichit le panel.</p> */
    public void setAssStyle(AssStyle as){
        this.as = as;
        updateText();
        repaint();
    }
    
    /** <p>Paint on the panel.<br />Peint le panel.</p> */
    @Override
    public void paint(Graphics g){
        Graphics2D g2 = (Graphics2D)g;
        
        g2.setColor(Color.white);
        g2.fillRect(0, 0, getWidth(), getHeight());
        
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        
        Composite originalComposite = g2.getComposite();//Get default tranparency
        
        //Back (Shadow)
//        g2.setComposite(makeComposite(ShadowAlpha));//Change the transparency
        g2.setColor(as.getBackCColor());
        g2.fill(backShape);
        
        //Border (Outline)
//        g2.setComposite(makeComposite(OutlineAlpha));//Change the transparency
        Stroke stroke = g2.getStroke();
        g2.setStroke(new BasicStroke(4.0f));
        g2.setColor(as.getOutlineCColor());
        g2.draw(foreShape);
        g2.setStroke(stroke);
        
        //Text
//        g2.setComposite(makeComposite(TextAlpha));//Change the transparency
        g2.setColor(as.getTextCColor());
        g2.fill(foreShape);
        
        g2.setComposite(originalComposite);//Reset default
    }
    
    /** <p>Update the text with the good font.<br />
     * Met à jour le texte avec la bonne police.</p> */
    private void updateText(){
        TextLayout tl = new TextLayout(
                strToRender, 
                as.getFont().deriveFont(50f), 
                new FontRenderContext(null, false, false));
        AffineTransform at = new AffineTransform();
	at.translate(0+20, (float)tl.getBounds().getHeight()+20);
        foreShape = tl.getOutline(at);
        at = new AffineTransform();
	at.translate(0+25, (float)tl.getBounds().getHeight()+25);
        backShape = tl.getOutline(at);
    }
    
    /** <p>Set the font.<br />Définit la police.</p> */
    public void setFont(String fontname, double fontsize){
        as.setFontname(fontname);
        as.setFontsize(fontsize);
        refresh();
    }
    
    /** <p>Set the color of the text.<br />Définit la couleur du texte.</p> */
    public void setTextColor(Color text, int alpha){
        as.setTextColor(text, Integer.toString(alpha, 16));
        TextAlpha = (float)alpha;
        refresh();
    }
    
    /** <p>Set the color of the border.<br />Définit la couleur de la bordure.</p> */
    public void setOutlineColor(Color outline, int alpha){
        as.setOutlineColor(outline, Integer.toString(alpha, 16));
        OutlineAlpha = (float)alpha;
        refresh();
    }
    
    /** <p>Set the color of the shadow.<br />Définit la couleur de l'ombre.</p> */
    public void setShadowColor(Color text, int alpha){
        as.setBackColor(text, Integer.toString(alpha, 16));
        ShadowAlpha = (float)alpha;
        refresh();
    }
    
    /** <p>SRefresh the panel.<br />Rafraichit le panel.</p> */
    public void refresh(){
        updateText();
        repaint();
    }
    
    /** <p>Management of the transparency.<br />Gestion de la transparence.</p> */
    private AlphaComposite makeComposite(float alpha) {
        int type = AlphaComposite.SRC_OVER;
        return(AlphaComposite.getInstance(type, alpha));
    }
}
