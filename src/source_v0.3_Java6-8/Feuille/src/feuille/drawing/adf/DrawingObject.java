/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.drawing.adf;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author The Wingate 2940
 */
public class DrawingObject {
    
    private List<LayerContent> layers = new ArrayList<LayerContent>();
    private BufferedImage img = null;
    private ImageIcon icon = null;
    int posx = 0, posy = 0;
    
    public DrawingObject(){
        
    }
    
    //**************************************************************************
    //------------------------------------------------------------------ COUCHES
    //**************************************************************************
    
    public void addLayer(String name, Color c, String commands){
        LayerContent lc = new LayerContent(name, c, commands);
        layers.add(lc);
    }
    
    public void addLayer(LayerContent lc){
        layers.add(lc);
    }
    
    public List<LayerContent> getLayers(){
        return layers;
    }
    
    //**************************************************************************
    //------------------------------------------------------- DESSIN A MAIN LEVE
    //**************************************************************************
    
    public void setImage(BufferedImage img){
        this.img = img;
    }
    
    public BufferedImage getImage(){
        return img;
    }
    
    public String imageToBase64() throws IOException{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, "png", baos);
        byte[] bytes = baos.toByteArray();
        String value = Base64.encodeBytes(bytes);
        return value;
    }
    
    public void imageFromBase64(String s) throws IOException{
        byte[] bytes = Base64.decode(s);
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        img = ImageIO.read(bais);        
    }
    
    //**************************************************************************
    //------------------------------------------------------------ IMAGE AJOUTEE
    //**************************************************************************
    
    public void setIcon(ImageIcon icon){
        this.icon = icon;
    }
    
    public ImageIcon getIcon(){
        return icon;
    }
    
    public String iconToBase64() throws IOException{
        if(icon==null){
            return "";
        }else{
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BufferedImage bi = toBufferedImage(icon);
            ImageIO.write(bi, "png", baos);
            byte[] bytes = baos.toByteArray();
            String value = Base64.encodeBytes(bytes);
            return value;
        }        
    }
    
    public void iconFromBase64(String s) throws IOException{
        if(s.isEmpty()){
            icon = null;
        }else{
            byte[] bytes = Base64.decode(s);
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            BufferedImage bi = ImageIO.read(bais);
            icon = new ImageIcon(bi);
        }        
    }
    
    private BufferedImage toBufferedImage(ImageIcon icon) {
        Image image = icon.getImage();

        /** On crée la nouvelle image */
        BufferedImage bufferedImage = new BufferedImage(
                    image.getWidth(null),
                    image.getHeight(null),
                    BufferedImage.TYPE_INT_ARGB );

        Graphics g = bufferedImage.createGraphics();
        g.drawImage(image,0,0,null);
        g.dispose();

        return bufferedImage;
    }
    
    public void setIconPosition(int x, int y){
        posx = x;
        posy = y;
    }
    
    public void setIconPositionX(int x){
        posx = x;
    }
    
    public void setIconPositionY(int y){
        posy = y;
    }
    
    public int getIconPositionX(){
        return posx;
    }
    
    public int getIconPositionY(){
        return posy;
    }

}
