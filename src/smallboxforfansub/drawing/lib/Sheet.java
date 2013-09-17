/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smallboxforfansub.drawing.lib;

import smallboxforfansub.drawing.lib.ShapesList;
import smallboxforfansub.drawing.lib.ReStart;
import smallboxforfansub.drawing.lib.Point;
import smallboxforfansub.drawing.lib.Move;
import smallboxforfansub.drawing.lib.Line;
import smallboxforfansub.drawing.lib.Layer;
import smallboxforfansub.drawing.lib.IShape;
import smallboxforfansub.drawing.lib.ControlPoint;
import smallboxforfansub.drawing.lib.Bezier;
import smallboxforfansub.drawing.lib.BSpline;
import java.awt.*;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.GeneralPath;
import javax.swing.ImageIcon;
import smallboxforfansub.drawing.operation.Center;
import smallboxforfansub.drawing.operation.Resize;
import smallboxforfansub.drawing.operation.Selection;
import smallboxforfansub.drawing.operation.Shear;
import smallboxforfansub.drawing.operation.Translation;

/**
 *
 * @author The Wingate 2940
 */
public class Sheet extends javax.swing.JPanel {
    
    //Pour dessiner toutes les formes, tout le temps.
    ShapesList slist = new ShapesList();
    //Pour visualiser une ligne aux coordonnées du pointeur de la souris.
    private int mouseX = -1;
    private int mouseY = -1;
    //Pour pouvoir déplacer l'image de fond (s'il y en a une).
    private int imageX = 0;
    private int imageY = 0;
    //Pour connaitre l'image à dessiner (s'il y en a une).
    private ImageIcon img = null;
    //Pour connaitre la transparence de l'image (0f = transparent ; 1f=opaque).
    private Float alpha = 1f;
    //Pour remplir la zone avec une couleur
    private GeneralPath gp = null;
    //Pour rendre transparente la zone
    private Float gpAlpha = 0.2f;
    
    private Image imgDraft = null;

    private int scale = 1;
    private Thickness thickness = Thickness.Big;
    
    private java.util.List<Layer> layerList = null;
    
    private Selection selection = new Selection();
    private java.util.List<Center> centers = new java.util.ArrayList<Center>();
    private java.util.List<Resize> resizes = new java.util.ArrayList<Resize>();
    private java.util.List<Shear> shears = new java.util.ArrayList<Shear>();
    private java.util.List<Translation> translations = new java.util.ArrayList<Translation>();
    private java.util.List<smallboxforfansub.drawing.ornament.IShape> oml = new java.util.ArrayList<smallboxforfansub.drawing.ornament.IShape>();
    
    public Sheet(){
        setLayout(null);
    }
    
    public enum Thickness{
        Big(10),Large(8),Medium(6),Small(4);

        private int thick;

        Thickness(int thick){
            this.thick = thick;
        }

        public int getThickness(){
            return thick;
        }
    }
    
    @Override
    public void paint(Graphics g){
        //Charge la classe Graphics2D pour pouvoir avoir accès à ses méthodes.
        Graphics2D g2d = (Graphics2D)g;
        //Définit la couleur blanche comme couleur de fond par défaut et colorie le fond.
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        g2d.scale(scale, scale);
        
        //Essaie de charger puis dessiner une image et changer sa transparence et sa position.
        if(img!=null){
            Composite originalComposite = g2d.getComposite();
            g2d.setComposite(makeComposite(alpha));
            g2d.drawImage(img.getImage(), imageX, imageY, null);
            g2d.setComposite(originalComposite);
        }
        
        //Essaie de charger puis dessiner une image et changer sa transparence et sa position.
        if(imgDraft!=null){
            Composite originalComposite = g2d.getComposite();
            g2d.setComposite(makeComposite(alpha));
            g2d.drawImage(imgDraft, 0, 0, null);
            g2d.setComposite(originalComposite);
        }
        
        //Définit et dessine les axes du milieu et le quadrillage.
        g2d.setColor(new Color(216,255,253));
        g2d.setStroke(new BasicStroke(2f));
        g2d.drawLine((getWidth()/scale)/2, 0, (getWidth()/scale)/2, getHeight()); //vertical
        g2d.drawLine(0, (getHeight()/scale)/2, getWidth(), (getHeight()/scale)/2); //horizontal
        g2d.setColor(new Color(234,216,255));
        g2d.setStroke(new BasicStroke(1f));
        int i = getWidth()/2;
        while(i>=0){ i=i-25; g2d.drawLine(i, 0, i, getHeight()); }
        i = getWidth()/2;
        while(i<=getWidth()){ i=i+25; g2d.drawLine(i, 0, i, getHeight()); }
        i = getHeight()/2;
        while(i>=0){ i=i-25; g2d.drawLine(0, i, getWidth(), i); }
        i = getHeight()/2;
        while(i<=getHeight()){ i=i+25; g2d.drawLine(0, i, getWidth(), i); }
        
        
        
        // Montre le path en remplissant la zone
        if(layerList!=null){
            Composite originalComposite = g2d.getComposite();//Get default
            g2d.setComposite(makeComposite(gpAlpha));//Change the transparency
            for(Layer lay : layerList){
                if(lay!=null && lay.isSelected()==false){
                    g2d.setColor(lay.getColor());
                    g2d.fill(lay.getGeneralPath());
                }else if(lay!=null && lay.isSelected()==true){
                    g2d.setColor(lay.getColor());
                    g2d.fill(lay.getGeneralPath());
                }
            }
            g2d.setComposite(originalComposite);//Reset default
        }else{
            if(gp!=null){
                Composite originalComposite = g2d.getComposite();//Get default
                g2d.setComposite(makeComposite(gpAlpha));//Change the transparency
                g2d.setColor(Color.green);
                g2d.fill(gp);
                g2d.setComposite(originalComposite);//Reset default
            }
        }
        
        //Dessine les axes correspondant au curseur de la souris.
        g2d.setColor(Color.pink);
        g2d.drawLine(mouseX, 0, mouseX, getHeight());
        g2d.drawLine(0, mouseY, getWidth(), mouseY);

        //Dessine les coordonnées près des axes correspondant au curseur de la souris.
        g2d.drawString(
                (mouseX-(getWidth()/scale)/2)+";"
                +(mouseY-(getHeight()/scale)/2),mouseX+5, mouseY-5);
        
        //Dessine les nombres aux bords ; ils correspondent aux coordonnées.
//        g2d.setColor(Color.darkGray);
//        i = (getWidth()/scale)/2;
//        while(i>=0){ i=i-50; g2d.drawString(Integer.toString(i-(getWidth()/scale)/2), i-10, 10); }
//        i = (getWidth()/scale)/2;
//        while(i<=getWidth()){ i=i+50; g2d.drawString(Integer.toString(i-(getWidth()/scale)/2), i-10, 10); }
//        i = (getHeight()/scale)/2;
//        while(i>=0){ i=i-50; g2d.drawString(Integer.toString((i-(getHeight()/scale)/2)), 10, i+5); }
//        i = (getHeight()/scale)/2;
//        while(i<=getHeight()){ i=i+50; g2d.drawString(Integer.toString((i-(getHeight()/scale)/2)), 10, i+5); }        
        
        //Change la couleur en rouge (cette ligne est inutile, merci de ne pas en tenir compte.)
        g2d.setColor(Color.red);
        
        // Consulte la liste des formes et les dessine en utilisant des couleurs différentes.
        for(IShape s : slist.getShapes()){
            if (s instanceof Line){
                Line l = (Line)s;
                g2d.setColor(Color.red);
                g2d.drawLine((int)l.getOriginPoint().getX(),(int)l.getOriginPoint().getY(),
                        (int)l.getLastPoint().getX(),(int)l.getLastPoint().getY());
            }else if (s instanceof Bezier){
                Bezier b = (Bezier)s;
                CubicCurve2D c = new CubicCurve2D.Double();
                c.setCurve((int)b.getOriginPoint().getX(), (int)b.getOriginPoint().getY(),
                        (int)b.getControl1Point().getX(), (int)b.getControl1Point().getY(),
                        (int)b.getControl2Point().getX(), (int)b.getControl2Point().getY(),
                        (int)b.getLastPoint().getX(), (int)b.getLastPoint().getY());
                g2d.setColor(Color.magenta);
                g2d.draw(c);

                //fait en sorte de mieux voir les points de contrôle
                g2d.setColor(Color.gray);
                java.awt.Stroke stroke = g2d.getStroke();
                float[] dash = {5, 5};
                g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT,
                                  BasicStroke.JOIN_MITER, 5,
                                  dash, 0));
                g2d.drawLine((int)b.getOriginPoint().getX(),(int)b.getOriginPoint().getY(),
                        (int)b.getControl1Point().getX(),(int)b.getControl1Point().getY());
                g2d.drawLine((int)b.getControl1Point().getX(),(int)b.getControl1Point().getY(),
                        (int)b.getControl2Point().getX(), (int)b.getControl2Point().getY());
                g2d.drawLine((int)b.getControl2Point().getX(), (int)b.getControl2Point().getY(),
                        (int)b.getLastPoint().getX(), (int)b.getLastPoint().getY());
                g2d.setStroke(stroke);
            }else if (s instanceof Point){
                Point p = (Point)s;
                int x = (int)p.getOriginPoint().getX();
                int y = (int)p.getOriginPoint().getY();
                g2d.setColor(Color.blue);
                g2d.fillRect(
                    x-thickness.getThickness()/2,
                    y-thickness.getThickness()/2,
                    thickness.getThickness(),
                    thickness.getThickness());
            }else if(s instanceof ControlPoint){
                ControlPoint cp = (ControlPoint)s;
                int x = (int)cp.getOriginPoint().getX();
                int y = (int)cp.getOriginPoint().getY();
                g2d.setColor(Color.orange);
                g2d.fillOval(
                    x-thickness.getThickness()/2,
                    y-thickness.getThickness()/2,
                    thickness.getThickness(),
                    thickness.getThickness());
            }else if (s instanceof BSpline){
                BSpline bs = (BSpline)s;
                ControlPoint c = null;
                for(ControlPoint cp : bs.getControlPoints()){                    
                    g2d.setColor(Color.orange);
                    int x1 = (int)cp.getOriginPoint().getX();
                    int y1 = (int)cp.getOriginPoint().getY();
                    g2d.drawOval(
                        x1-thickness.getThickness()/2,
                        y1-thickness.getThickness()/2,
                        thickness.getThickness(),
                        thickness.getThickness());
                    g2d.setColor(Color.gray);
                    g2d.fillOval(
                        x1-thickness.getThickness()/2,
                        y1-thickness.getThickness()/2,
                        thickness.getThickness(),
                        thickness.getThickness());
                    if(c!=null){
                        g2d.setColor(Color.orange);
                        java.awt.Stroke stroke = g2d.getStroke();
                        float[] dash = {5, 5};
                        g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT,
                                        BasicStroke.JOIN_MITER, 5,
                                        dash, 0));
                        g2d.drawLine((int)c.getOriginPoint().getX(),(int)c.getOriginPoint().getY(),
                                (int)cp.getOriginPoint().getX(),(int)cp.getOriginPoint().getY());
                        g2d.setStroke(stroke);
                    }
                    c = cp;
                }
                g2d.setColor(Color.black);
                bs.getBSplineCurve().paintCurve(g2d, 5, true);
                if(bs.isNextExist()){
                    g2d.setColor(Color.pink);
                    g2d.fillRect(
                    (int)bs.getNextPoint().getX()-thickness.getThickness()/2,
                    (int)bs.getNextPoint().getY()-thickness.getThickness()/2,
                    thickness.getThickness(),
                    thickness.getThickness());
                }
            }else if (s instanceof Move){
                Move m = (Move)s;
                int x = (int)m.getLastPoint().getX();
                int y = (int)m.getLastPoint().getY();
                g2d.setColor(new Color(152,0,255));
                g2d.fillRect(
                    x-thickness.getThickness()/2,
                    y-thickness.getThickness()/2,
                    thickness.getThickness(),
                    thickness.getThickness());
            }else if (s instanceof ReStart){
                ReStart m = (ReStart)s;
                int x = (int)m.getLastPoint().getX();
                int y = (int)m.getLastPoint().getY();
                g2d.setColor(new Color(255,110,33));
                g2d.fillRect(
                    x-thickness.getThickness()/2,
                    y-thickness.getThickness()/2,
                    thickness.getThickness(),
                    thickness.getThickness());
            }
        }
        
        //Dessine le centre de rotation s'il existe
        for(Resize resize : resizes){
            if(resize.isSet()){
                g2d.setColor(Color.magenta);
                g2d.fillOval(resize.getX()-15, resize.getY()-15, 30, 30);
                g2d.setColor(new Color(199,0,255));
                if(resize.getPreviewShapes().isEmpty()==false){
                    for(IShape s : resize.getPreviewShapes()){
                        if (s instanceof Line){
                            Line l = (Line)s;
                            g2d.drawLine((int)l.getOriginPoint().getX(),(int)l.getOriginPoint().getY(),
                                    (int)l.getLastPoint().getX(),(int)l.getLastPoint().getY());
                        }else if (s instanceof Bezier){
                            Bezier b = (Bezier)s;
                            CubicCurve2D c = new CubicCurve2D.Double();
                            c.setCurve((int)b.getOriginPoint().getX(), (int)b.getOriginPoint().getY(),
                                    (int)b.getControl1Point().getX(), (int)b.getControl1Point().getY(),
                                    (int)b.getControl2Point().getX(), (int)b.getControl2Point().getY(),
                                    (int)b.getLastPoint().getX(), (int)b.getLastPoint().getY());
                            g2d.draw(c);

                            //fait en sorte de mieux voir les points de contrôle
                            java.awt.Stroke stroke = g2d.getStroke();
                            float[] dash = {5, 5};
                            g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT,
                                              BasicStroke.JOIN_MITER, 5,
                                              dash, 0));
                            g2d.drawLine((int)b.getOriginPoint().getX(),(int)b.getOriginPoint().getY(),
                                    (int)b.getControl1Point().getX(),(int)b.getControl1Point().getY());
                            g2d.drawLine((int)b.getControl1Point().getX(),(int)b.getControl1Point().getY(),
                                    (int)b.getControl2Point().getX(), (int)b.getControl2Point().getY());
                            g2d.drawLine((int)b.getControl2Point().getX(), (int)b.getControl2Point().getY(),
                                    (int)b.getLastPoint().getX(), (int)b.getLastPoint().getY());
                            g2d.setStroke(stroke);
                        }else if (s instanceof Point){
                            Point p = (Point)s;
                            int x = (int)p.getOriginPoint().getX();
                            int y = (int)p.getOriginPoint().getY();
                            g2d.fillRect(
                                x-thickness.getThickness()/2,
                                y-thickness.getThickness()/2,
                                thickness.getThickness(),
                                thickness.getThickness());
                        }else if(s instanceof ControlPoint){
                            ControlPoint cp = (ControlPoint)s;
                            int x = (int)cp.getOriginPoint().getX();
                            int y = (int)cp.getOriginPoint().getY();
                            g2d.fillOval(
                                x-thickness.getThickness()/2,
                                y-thickness.getThickness()/2,
                                thickness.getThickness(),
                                thickness.getThickness());
                        }else if (s instanceof BSpline){
                            BSpline bs = (BSpline)s;
                            ControlPoint c = null;
                            for(ControlPoint cp : bs.getControlPoints()){
                                int x1 = (int)cp.getOriginPoint().getX();
                                int y1 = (int)cp.getOriginPoint().getY();
                                g2d.drawOval(
                                    x1-thickness.getThickness()/2,
                                    y1-thickness.getThickness()/2,
                                    thickness.getThickness(),
                                    thickness.getThickness());
                                g2d.fillOval(
                                    x1-thickness.getThickness()/2,
                                    y1-thickness.getThickness()/2,
                                    thickness.getThickness(),
                                    thickness.getThickness());
                                if(c!=null){
                                    java.awt.Stroke stroke = g2d.getStroke();
                                    float[] dash = {5, 5};
                                    g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT,
                                                    BasicStroke.JOIN_MITER, 5,
                                                    dash, 0));
                                    g2d.drawLine((int)c.getOriginPoint().getX(),(int)c.getOriginPoint().getY(),
                                            (int)cp.getOriginPoint().getX(),(int)cp.getOriginPoint().getY());
                                    g2d.setStroke(stroke);
                                }
                                c = cp;
                            }
                            bs.getBSplineCurve().paintCurve(g2d, 5, true);
                            if(bs.isNextExist()){
                                g2d.fillRect(
                                (int)bs.getNextPoint().getX()-thickness.getThickness()/2,
                                (int)bs.getNextPoint().getY()-thickness.getThickness()/2,
                                thickness.getThickness(),
                                thickness.getThickness());
                            }
                        }else if (s instanceof Move){
                            Move m = (Move)s;
                            int x = (int)m.getLastPoint().getX();
                            int y = (int)m.getLastPoint().getY();
                            g2d.fillRect(
                                x-thickness.getThickness()/2,
                                y-thickness.getThickness()/2,
                                thickness.getThickness(),
                                thickness.getThickness());
                        }else if (s instanceof ReStart){
                            ReStart m = (ReStart)s;
                            int x = (int)m.getLastPoint().getX();
                            int y = (int)m.getLastPoint().getY();
                            g2d.fillRect(
                                x-thickness.getThickness()/2,
                                y-thickness.getThickness()/2,
                                thickness.getThickness(),
                                thickness.getThickness());
                        }
                    }
                }
            }   
        }
                 
        
        //Dessine le centre de rotation s'il existe
        for(Center center : centers){
            if(center.isSet()){
                g2d.setColor(Color.pink);
                g2d.fillOval(center.getX()-15, center.getY()-15, 30, 30);
                g2d.setColor(new Color(199,0,255));
                if(center.getPreviewShapes().isEmpty()==false){
                    for(IShape s : center.getPreviewShapes()){
                        if (s instanceof Line){
                            Line l = (Line)s;
                            g2d.drawLine((int)l.getOriginPoint().getX(),(int)l.getOriginPoint().getY(),
                                    (int)l.getLastPoint().getX(),(int)l.getLastPoint().getY());
                        }else if (s instanceof Bezier){
                            Bezier b = (Bezier)s;
                            CubicCurve2D c = new CubicCurve2D.Double();
                            c.setCurve((int)b.getOriginPoint().getX(), (int)b.getOriginPoint().getY(),
                                    (int)b.getControl1Point().getX(), (int)b.getControl1Point().getY(),
                                    (int)b.getControl2Point().getX(), (int)b.getControl2Point().getY(),
                                    (int)b.getLastPoint().getX(), (int)b.getLastPoint().getY());
                            g2d.draw(c);

                            //fait en sorte de mieux voir les points de contrôle
                            java.awt.Stroke stroke = g2d.getStroke();
                            float[] dash = {5, 5};
                            g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT,
                                              BasicStroke.JOIN_MITER, 5,
                                              dash, 0));
                            g2d.drawLine((int)b.getOriginPoint().getX(),(int)b.getOriginPoint().getY(),
                                    (int)b.getControl1Point().getX(),(int)b.getControl1Point().getY());
                            g2d.drawLine((int)b.getControl1Point().getX(),(int)b.getControl1Point().getY(),
                                    (int)b.getControl2Point().getX(), (int)b.getControl2Point().getY());
                            g2d.drawLine((int)b.getControl2Point().getX(), (int)b.getControl2Point().getY(),
                                    (int)b.getLastPoint().getX(), (int)b.getLastPoint().getY());
                            g2d.setStroke(stroke);
                        }else if (s instanceof Point){
                            Point p = (Point)s;
                            int x = (int)p.getOriginPoint().getX();
                            int y = (int)p.getOriginPoint().getY();
                            g2d.fillRect(
                                x-thickness.getThickness()/2,
                                y-thickness.getThickness()/2,
                                thickness.getThickness(),
                                thickness.getThickness());
                        }else if(s instanceof ControlPoint){
                            ControlPoint cp = (ControlPoint)s;
                            int x = (int)cp.getOriginPoint().getX();
                            int y = (int)cp.getOriginPoint().getY();
                            g2d.fillOval(
                                x-thickness.getThickness()/2,
                                y-thickness.getThickness()/2,
                                thickness.getThickness(),
                                thickness.getThickness());
                        }else if (s instanceof BSpline){
                            BSpline bs = (BSpline)s;
                            ControlPoint c = null;
                            for(ControlPoint cp : bs.getControlPoints()){
                                int x1 = (int)cp.getOriginPoint().getX();
                                int y1 = (int)cp.getOriginPoint().getY();
                                g2d.drawOval(
                                    x1-thickness.getThickness()/2,
                                    y1-thickness.getThickness()/2,
                                    thickness.getThickness(),
                                    thickness.getThickness());
                                g2d.fillOval(
                                    x1-thickness.getThickness()/2,
                                    y1-thickness.getThickness()/2,
                                    thickness.getThickness(),
                                    thickness.getThickness());
                                if(c!=null){
                                    java.awt.Stroke stroke = g2d.getStroke();
                                    float[] dash = {5, 5};
                                    g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT,
                                                    BasicStroke.JOIN_MITER, 5,
                                                    dash, 0));
                                    g2d.drawLine((int)c.getOriginPoint().getX(),(int)c.getOriginPoint().getY(),
                                            (int)cp.getOriginPoint().getX(),(int)cp.getOriginPoint().getY());
                                    g2d.setStroke(stroke);
                                }
                                c = cp;
                            }
                            bs.getBSplineCurve().paintCurve(g2d, 5, true);
                            if(bs.isNextExist()){
                                g2d.fillRect(
                                (int)bs.getNextPoint().getX()-thickness.getThickness()/2,
                                (int)bs.getNextPoint().getY()-thickness.getThickness()/2,
                                thickness.getThickness(),
                                thickness.getThickness());
                            }
                        }else if (s instanceof Move){
                            Move m = (Move)s;
                            int x = (int)m.getLastPoint().getX();
                            int y = (int)m.getLastPoint().getY();
                            g2d.fillRect(
                                x-thickness.getThickness()/2,
                                y-thickness.getThickness()/2,
                                thickness.getThickness(),
                                thickness.getThickness());
                        }else if (s instanceof ReStart){
                            ReStart m = (ReStart)s;
                            int x = (int)m.getLastPoint().getX();
                            int y = (int)m.getLastPoint().getY();
                            g2d.fillRect(
                                x-thickness.getThickness()/2,
                                y-thickness.getThickness()/2,
                                thickness.getThickness(),
                                thickness.getThickness());
                        }
                    }
                }
            }
        }
                
        
        //Dessine le rectangle de sélection de groupe
        if(selection.exists()){
            selection.drawSelection(g2d);
        }
        
        // RE-Consulte la liste des formes et entoure les formes sélectionnées.
        for(IShape s : slist.getShapes()){
            if(s.isFirstInSelection()){
                g2d.setColor(new Color(147,71,255));
            }else{
                g2d.setColor(Color.red);
            }            
            g2d.setStroke(new BasicStroke(3f));
            if(s instanceof Point){
                Point p = (Point)s;
                if(p.isInSelection()){
                    int x = (int)p.getOriginPoint().getX();
                    int y = (int)p.getOriginPoint().getY();
                    int delta = thickness.getThickness()+7;
                    g2d.drawOval(x-delta/2, y-delta/2, delta, delta);
                }                
            }
            if(s instanceof ControlPoint){
                ControlPoint cp = (ControlPoint)s;
                if(cp.isInSelection()){
                    int x = (int)cp.getOriginPoint().getX();
                    int y = (int)cp.getOriginPoint().getY();
                    int delta = thickness.getThickness()+7;
                    g2d.drawOval(x-delta/2, y-delta/2, delta, delta);
                }                
            }
            if(s instanceof ReStart){
                ReStart m = (ReStart)s;
                if(m.isInSelection()){
                    int x = (int)m.getLastPoint().getX();
                    int y = (int)m.getLastPoint().getY();
                    int delta = thickness.getThickness()+7;
                    g2d.drawOval(x-delta/2, y-delta/2, delta, delta);
                }                
            }
            if(s instanceof Move){
                Move n = (Move)s;
                if(n.isInSelection()){
                    int x = (int)n.getLastPoint().getX();
                    int y = (int)n.getLastPoint().getY();
                    int delta = thickness.getThickness()+7;
                    g2d.drawOval(x-delta/2, y-delta/2, delta, delta);
                }
            }
            if(s instanceof BSpline){
                BSpline bs = (BSpline)s;
                for(ControlPoint cp : bs.getControlPoints()){
                    if(cp.isInSelection()){
                        int x = (int)cp.getOriginPoint().getX();
                        int y = (int)cp.getOriginPoint().getY();
                        int delta = thickness.getThickness()+7;
                        g2d.drawOval(x-delta/2, y-delta/2, delta, delta);
                    }
                }
            }
        }
        
        //Dessine le rectangle génénal du shearing
        for(Shear shear : shears){
            if(shear.isSet()){
                shear.drawLimits(g2d);
                g2d.setColor(new Color(199,0,255));
                if(shear.getPreviewShapes().isEmpty()==false){
                    for(IShape s : shear.getPreviewShapes()){
                        if (s instanceof Line){
                            Line l = (Line)s;
                            g2d.drawLine((int)l.getOriginPoint().getX(),(int)l.getOriginPoint().getY(),
                                    (int)l.getLastPoint().getX(),(int)l.getLastPoint().getY());
                        }else if (s instanceof Bezier){
                            Bezier b = (Bezier)s;
                            CubicCurve2D c = new CubicCurve2D.Double();
                            c.setCurve((int)b.getOriginPoint().getX(), (int)b.getOriginPoint().getY(),
                                    (int)b.getControl1Point().getX(), (int)b.getControl1Point().getY(),
                                    (int)b.getControl2Point().getX(), (int)b.getControl2Point().getY(),
                                    (int)b.getLastPoint().getX(), (int)b.getLastPoint().getY());
                            g2d.draw(c);

                            //fait en sorte de mieux voir les points de contrôle
                            java.awt.Stroke stroke = g2d.getStroke();
                            float[] dash = {5, 5};
                            g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT,
                                              BasicStroke.JOIN_MITER, 5,
                                              dash, 0));
                            g2d.drawLine((int)b.getOriginPoint().getX(),(int)b.getOriginPoint().getY(),
                                    (int)b.getControl1Point().getX(),(int)b.getControl1Point().getY());
                            g2d.drawLine((int)b.getControl1Point().getX(),(int)b.getControl1Point().getY(),
                                    (int)b.getControl2Point().getX(), (int)b.getControl2Point().getY());
                            g2d.drawLine((int)b.getControl2Point().getX(), (int)b.getControl2Point().getY(),
                                    (int)b.getLastPoint().getX(), (int)b.getLastPoint().getY());
                            g2d.setStroke(stroke);
                        }else if (s instanceof Point){
                            Point p = (Point)s;
                            int x = (int)p.getOriginPoint().getX();
                            int y = (int)p.getOriginPoint().getY();
                            g2d.fillRect(
                                x-thickness.getThickness()/2,
                                y-thickness.getThickness()/2,
                                thickness.getThickness(),
                                thickness.getThickness());
                        }else if(s instanceof ControlPoint){
                            ControlPoint cp = (ControlPoint)s;
                            int x = (int)cp.getOriginPoint().getX();
                            int y = (int)cp.getOriginPoint().getY();
                            g2d.fillOval(
                                x-thickness.getThickness()/2,
                                y-thickness.getThickness()/2,
                                thickness.getThickness(),
                                thickness.getThickness());
                        }else if (s instanceof BSpline){
                            BSpline bs = (BSpline)s;
                            ControlPoint c = null;
                            for(ControlPoint cp : bs.getControlPoints()){
                                int x1 = (int)cp.getOriginPoint().getX();
                                int y1 = (int)cp.getOriginPoint().getY();
                                g2d.drawOval(
                                    x1-thickness.getThickness()/2,
                                    y1-thickness.getThickness()/2,
                                    thickness.getThickness(),
                                    thickness.getThickness());
                                g2d.fillOval(
                                    x1-thickness.getThickness()/2,
                                    y1-thickness.getThickness()/2,
                                    thickness.getThickness(),
                                    thickness.getThickness());
                                if(c!=null){
                                    java.awt.Stroke stroke = g2d.getStroke();
                                    float[] dash = {5, 5};
                                    g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT,
                                                    BasicStroke.JOIN_MITER, 5,
                                                    dash, 0));
                                    g2d.drawLine((int)c.getOriginPoint().getX(),(int)c.getOriginPoint().getY(),
                                            (int)cp.getOriginPoint().getX(),(int)cp.getOriginPoint().getY());
                                    g2d.setStroke(stroke);
                                }
                                c = cp;
                            }
                            bs.getBSplineCurve().paintCurve(g2d, 5, true);
                            if(bs.isNextExist()){
                                g2d.fillRect(
                                (int)bs.getNextPoint().getX()-thickness.getThickness()/2,
                                (int)bs.getNextPoint().getY()-thickness.getThickness()/2,
                                thickness.getThickness(),
                                thickness.getThickness());
                            }
                        }else if (s instanceof Move){
                            Move m = (Move)s;
                            int x = (int)m.getLastPoint().getX();
                            int y = (int)m.getLastPoint().getY();
                            g2d.fillRect(
                                x-thickness.getThickness()/2,
                                y-thickness.getThickness()/2,
                                thickness.getThickness(),
                                thickness.getThickness());
                        }else if (s instanceof ReStart){
                            ReStart m = (ReStart)s;
                            int x = (int)m.getLastPoint().getX();
                            int y = (int)m.getLastPoint().getY();
                            g2d.fillRect(
                                x-thickness.getThickness()/2,
                                y-thickness.getThickness()/2,
                                thickness.getThickness(),
                                thickness.getThickness());
                        }
                    }
                }
            } 
        }
        
        g2d.setStroke(new BasicStroke(1f));
        
        for(Translation translation : translations){
            if(translation.isSet()){
                g2d.setColor(Color.pink);
                g2d.fillOval(translation.getX()-15, translation.getY()-15, 30, 30);
                g2d.setColor(new Color(199,0,255));
                if(translation.getPreviewShapes().isEmpty()==false){
                    for(IShape s : translation.getPreviewShapes()){
                        if (s instanceof Line){
                            Line l = (Line)s;
                            g2d.drawLine((int)l.getOriginPoint().getX(),(int)l.getOriginPoint().getY(),
                                    (int)l.getLastPoint().getX(),(int)l.getLastPoint().getY());
                        }else if (s instanceof Bezier){
                            Bezier b = (Bezier)s;
                            CubicCurve2D c = new CubicCurve2D.Double();
                            c.setCurve((int)b.getOriginPoint().getX(), (int)b.getOriginPoint().getY(),
                                    (int)b.getControl1Point().getX(), (int)b.getControl1Point().getY(),
                                    (int)b.getControl2Point().getX(), (int)b.getControl2Point().getY(),
                                    (int)b.getLastPoint().getX(), (int)b.getLastPoint().getY());
                            g2d.draw(c);

                            //fait en sorte de mieux voir les points de contrôle
                            java.awt.Stroke stroke = g2d.getStroke();
                            float[] dash = {5, 5};
                            g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT,
                                              BasicStroke.JOIN_MITER, 5,
                                              dash, 0));
                            g2d.drawLine((int)b.getOriginPoint().getX(),(int)b.getOriginPoint().getY(),
                                    (int)b.getControl1Point().getX(),(int)b.getControl1Point().getY());
                            g2d.drawLine((int)b.getControl1Point().getX(),(int)b.getControl1Point().getY(),
                                    (int)b.getControl2Point().getX(), (int)b.getControl2Point().getY());
                            g2d.drawLine((int)b.getControl2Point().getX(), (int)b.getControl2Point().getY(),
                                    (int)b.getLastPoint().getX(), (int)b.getLastPoint().getY());
                            g2d.setStroke(stroke);
                        }else if (s instanceof Point){
                            Point p = (Point)s;
                            int x = (int)p.getOriginPoint().getX();
                            int y = (int)p.getOriginPoint().getY();
                            g2d.fillRect(
                                x-thickness.getThickness()/2,
                                y-thickness.getThickness()/2,
                                thickness.getThickness(),
                                thickness.getThickness());
                        }else if(s instanceof ControlPoint){
                            ControlPoint cp = (ControlPoint)s;
                            int x = (int)cp.getOriginPoint().getX();
                            int y = (int)cp.getOriginPoint().getY();
                            g2d.fillOval(
                                x-thickness.getThickness()/2,
                                y-thickness.getThickness()/2,
                                thickness.getThickness(),
                                thickness.getThickness());
                        }else if (s instanceof BSpline){
                            BSpline bs = (BSpline)s;
                            ControlPoint c = null;
                            for(ControlPoint cp : bs.getControlPoints()){
                                int x1 = (int)cp.getOriginPoint().getX();
                                int y1 = (int)cp.getOriginPoint().getY();
                                g2d.drawOval(
                                    x1-thickness.getThickness()/2,
                                    y1-thickness.getThickness()/2,
                                    thickness.getThickness(),
                                    thickness.getThickness());
                                g2d.fillOval(
                                    x1-thickness.getThickness()/2,
                                    y1-thickness.getThickness()/2,
                                    thickness.getThickness(),
                                    thickness.getThickness());
                                if(c!=null){
                                    java.awt.Stroke stroke = g2d.getStroke();
                                    float[] dash = {5, 5};
                                    g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT,
                                                    BasicStroke.JOIN_MITER, 5,
                                                    dash, 0));
                                    g2d.drawLine((int)c.getOriginPoint().getX(),(int)c.getOriginPoint().getY(),
                                            (int)cp.getOriginPoint().getX(),(int)cp.getOriginPoint().getY());
                                    g2d.setStroke(stroke);
                                }
                                c = cp;
                            }
                            bs.getBSplineCurve().paintCurve(g2d, 5, true);
                            if(bs.isNextExist()){
                                g2d.fillRect(
                                (int)bs.getNextPoint().getX()-thickness.getThickness()/2,
                                (int)bs.getNextPoint().getY()-thickness.getThickness()/2,
                                thickness.getThickness(),
                                thickness.getThickness());
                            }
                        }else if (s instanceof Move){
                            Move m = (Move)s;
                            int x = (int)m.getLastPoint().getX();
                            int y = (int)m.getLastPoint().getY();
                            g2d.fillRect(
                                x-thickness.getThickness()/2,
                                y-thickness.getThickness()/2,
                                thickness.getThickness(),
                                thickness.getThickness());
                        }else if (s instanceof ReStart){
                            ReStart m = (ReStart)s;
                            int x = (int)m.getLastPoint().getX();
                            int y = (int)m.getLastPoint().getY();
                            g2d.fillRect(
                                x-thickness.getThickness()/2,
                                y-thickness.getThickness()/2,
                                thickness.getThickness(),
                                thickness.getThickness());
                        }
                    }
                }
            }
        }
        
        for(smallboxforfansub.drawing.ornament.IShape s : oml){
            if(s instanceof smallboxforfansub.drawing.ornament.OrnMMLine){
                smallboxforfansub.drawing.ornament.OrnMMLine l = (smallboxforfansub.drawing.ornament.OrnMMLine)s;
                l.draw(g2d, Color.blue);
            }else if(s instanceof smallboxforfansub.drawing.ornament.OrnMMBezier){
                smallboxforfansub.drawing.ornament.OrnMMBezier b = (smallboxforfansub.drawing.ornament.OrnMMBezier)s;
                b.draw(g2d, Color.blue);
            }else if(s instanceof smallboxforfansub.drawing.ornament.OrnPoint){
                smallboxforfansub.drawing.ornament.OrnPoint p = (smallboxforfansub.drawing.ornament.OrnPoint)s;
                p.draw(g2d, Color.blue);
            }else if(s instanceof smallboxforfansub.drawing.ornament.OrnControlPoint){
                smallboxforfansub.drawing.ornament.OrnControlPoint cp = (smallboxforfansub.drawing.ornament.OrnControlPoint)s;
                cp.draw(g2d, Color.blue);
            }
        }
    }
    
    public void updateThickness(Thickness thickness){
        this.thickness = thickness;
    }
    
    /** Met à jour la liste de formes et demande une mise à jour de l'affichage. */
    public void updateShapesList(ShapesList slist){
        this.slist = slist;
        this.repaint();
    }

    /** Demande une mise à jour de l'affichage. */
    public void updateDrawing(){
        repaint();
    }

    /** Met à jour les coordonnées de position de la souris. */
    public void updateMousePosition(int mouseX, int mouseY){
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    /** Met à jour les coordonnées de position de l'image
     * en ajoutant la valeur de déplacement. */
    public void updateImagePosition(int x, int y){
        imageX = imageX+x;
        imageY = imageY+y;
    }

    /** Met à jour l'image. */
    public void updateImage(ImageIcon img){
        this.img = img;
    }

    /** Met à jour la transparence de l'image. */
    public void updateImageTransparency(Float alpha){
        this.alpha = alpha;
    }

    /** Met à jour le chemin de la zone. */
    public void updateGeneralPath(GeneralPath gp){
        this.gp = gp;
    }

    /** Met à jour la transparence pour la zone. */
    public void updateGeneralPathTransparency(Float gpAlpha){
        this.gpAlpha = gpAlpha;
    }

    // Gestion de la transparence
    private AlphaComposite makeComposite(float alpha) {
        int type = AlphaComposite.SRC_OVER;
        return(AlphaComposite.getInstance(type, alpha));
    }

    public void setScaleXY(int scale){
        this.scale = scale;
        setPreferredSize(new java.awt.Dimension(setSizeOfDrawing()*scale,setSizeOfDrawing()*scale));
        revalidate();
        repaint();
    }

    public int getScaleXY(){
        return scale;
    }

    public void updateImageRealPosition(int x, int y){
        imageX = x;
        imageY = y;
    }

    private int setSizeOfDrawing(){
        java.awt.Toolkit toolkit = java.awt.Toolkit.getDefaultToolkit();
        java.awt.Dimension dim = toolkit.getScreenSize();
        int big = (int)dim.getWidth();
        if(dim.getWidth()<dim.getHeight()){
            big = (int)dim.getHeight();
        }
        int size = 1000;
        while(size<big){
            size+=500;
        }
        return size;
    }
    
    public void setLayerList(java.util.List<Layer> layerList){
        this.layerList = layerList;
    }
    
    public void updateDraft(Image imgDraft){
        this.imgDraft = imgDraft;
    }
    
    public int getImagePositionX(){
        return imageX;
    }
    
    public int getImagePositionY(){
        return imageY;
    }
    
    public int getImageWidth(){
        return img.getIconWidth();
    }
    
    public int getImageHeight(){
        return img.getIconHeight();
    }
    
    public void updateSelection(Selection selection, boolean clear){
        if(clear==true){
            selection.cleanList(slist);
        }
        this.selection = selection;
        repaint();
    }
    
    public void updateCenter(Center center){
        centers.clear();
        centers.add(center);
        repaint();
    }
    
    public void updateResize(Resize resize){
        resizes.clear();
        resizes.add(resize);
        repaint();
    }
    
    public void updateShear(Shear shear){
        shears.clear();
        shears.add(shear);
        repaint();
    }
    
    public void updateTranslation(Translation translation){
        translations.clear();
        translations.add(translation);
        repaint();
    }
    
    public void updateCenter(java.util.List<Center> c){
        centers.clear();
        centers.addAll(c);
        repaint();
    }
    
    public void updateResize(java.util.List<Resize> r){
        resizes.clear();
        resizes.addAll(r);
        repaint();
    }
    
    public void updateShear(java.util.List<Shear> s){
        shears.clear();
        shears.addAll(s);
        repaint();
    }
    
    public void updateTranslation(java.util.List<Translation> t){
        translations.clear();
        translations.addAll(t);
        repaint();
    }
    
    public void updateOrnamentForMain(java.util.List<smallboxforfansub.drawing.ornament.IShape> oml){
        this.oml = oml;
        repaint();
    }
}
