/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package smallboxforfansub.karaoke.lib;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;


/**
 * <p>This class is a panel to show colors in the transparency dialog.<br />
 * Cette classe est un panel qui montre des couleurs pour la dialogue de transparence.</p>
 * @author The Wingate 2940
 */
public class TransparencyPreview extends javax.swing.JPanel {
    
    private int unitX = 0;
    private int unitY = 0;
    private Float alpha = 1f;
    
    /** <p>Create a new TransparencyPreview.<br />
     * Crée un nouveau TransparencyPreview.</p> */
    public TransparencyPreview(){
        
    }

    /** <p>Paint colors.<br />Peint des couleurs.</p> */
    @Override
    public void paint(Graphics g){
        Graphics2D g2d = (Graphics2D)g;
        unitX = getWidth()/10;
        unitY = getHeight()/2;

        //Set background to white and paint.
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        //Create a grid
        int i = 0;
        g2d.setColor(Color.gray);
        while (i <= getWidth()){
            g2d.fillRect(i, 0, 10, 10);
            g2d.fillRect(i+10, 10, 10, 10);
            g2d.fillRect(i, 20, 10, 10);
            g2d.fillRect(i+10, 30, 10, 10);
            g2d.fillRect(i, 40, 10, 10);
            g2d.fillRect(i+10, 50, 10, 10);
            g2d.fillRect(i, 60, 10, 10);
            g2d.fillRect(i+10, 70, 10, 10);
            g2d.fillRect(i, 80, 10, 10);
            g2d.fillRect(i+10, 90, 10, 10);
            i += 20;
        }
        
        //Show 10 colors with alpha
        Composite originalComposite = g2d.getComposite();//Get default
        g2d.setComposite(makeComposite(alpha));//Change the transparency
        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, unitX, unitY);
        g2d.setColor(Color.gray);
        g2d.fillRect(unitX*1, 0, unitX, unitY);
        g2d.setColor(Color.white);
        g2d.fillRect(unitX*2, 0, unitX, unitY);
        g2d.setColor(Color.yellow);
        g2d.fillRect(unitX*3, 0, unitX, unitY);
        g2d.setColor(Color.orange);
        g2d.fillRect(unitX*4, 0, unitX, unitY);
        g2d.setColor(Color.red);
        g2d.fillRect(unitX*5, 0, unitX, unitY);
        g2d.setColor(Color.magenta);
        g2d.fillRect(unitX*6, 0, unitX, unitY);
        g2d.setColor(Color.pink);
        g2d.fillRect(unitX*7, 0, unitX, unitY);
        g2d.setColor(Color.blue);
        g2d.fillRect(unitX*8, 0, unitX, unitY);
        g2d.setColor(Color.cyan);
        g2d.fillRect(unitX*9, 0, unitX, unitY);
        g2d.setColor(Color.green);
        g2d.fillRect(unitX*10, 0, unitX, unitY);
        g2d.setComposite(originalComposite);//Reset default

        //Show 10 colors without alpha
        g2d.setColor(Color.black);
        g2d.fillRect(0, unitY, unitX, unitY);
        g2d.setColor(Color.gray);
        g2d.fillRect(unitX*1, unitY, unitX, unitY);
        g2d.setColor(Color.white);
        g2d.fillRect(unitX*2, unitY, unitX, unitY);
        g2d.setColor(Color.yellow);
        g2d.fillRect(unitX*3, unitY, unitX, unitY);
        g2d.setColor(Color.orange);
        g2d.fillRect(unitX*4, unitY, unitX, unitY);
        g2d.setColor(Color.red);
        g2d.fillRect(unitX*5, unitY, unitX, unitY);
        g2d.setColor(Color.magenta);
        g2d.fillRect(unitX*6, unitY, unitX, unitY);
        g2d.setColor(Color.pink);
        g2d.fillRect(unitX*7, unitY, unitX, unitY);
        g2d.setColor(Color.blue);
        g2d.fillRect(unitX*8, unitY, unitX, unitY);
        g2d.setColor(Color.cyan);
        g2d.fillRect(unitX*9, unitY, unitX, unitY);
        g2d.setColor(Color.green);
        g2d.fillRect(unitX*10, unitY, unitX, unitY);
    }
    
    /** <p>Create transparency.<br />Crée de la transparence.</p> */
    private AlphaComposite makeComposite(float alpha) {
        int type = AlphaComposite.SRC_OVER;
        return(AlphaComposite.getInstance(type, alpha));
    }

    /** <p>Update the tranparency.<br />Met à jour la transparence.</p> */
    public void updateAlpha(Float alpha){
        this.alpha = alpha;
        this.repaint();
    }

}
