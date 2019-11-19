/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smallboxforfansub.karaoke.renderer;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.SystemColor;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JList;
import smallboxforfansub.drawing.lib.Bezier;
import smallboxforfansub.drawing.lib.ControlPoint;
import smallboxforfansub.drawing.lib.IShape;
import smallboxforfansub.drawing.lib.Line;
import smallboxforfansub.drawing.lib.Point;
import smallboxforfansub.drawing.lib.ShapesList;
import smallboxforfansub.karaoke.lib.MiniDraw;

/**
 *
 * @author The Wingate 2940
 */
public class drawingChooserRenderer extends javax.swing.JPanel
        implements javax.swing.ListCellRenderer {
    
    String commands = ""; String newComs = ""; String name = "";
    private int h = 100;
    private int w = 100;
    private boolean isSelected = false;
    private int firstX, lastX, firstY, lastY, ampX, ampY, amp;
    private Sens mySens = Sens.Undifined;
    private List<String> listComs;
    
    ShapesList slist = new ShapesList();
    java.awt.Point first = null;
    java.awt.Point last = null;
    private GeneralPath gpath = null;
    private Float gpAlpha = 0.2f;
    
    private enum Sens{
        Undifined, Horizontal, Vertical;
    }
    
    public drawingChooserRenderer(){
        setOpaque(true);
        setPreferredSize(new java.awt.Dimension(getWidth(), h+1));
        firstX=0; lastX=0; firstY=0; lastY=0; ampX=0; ampY=0; amp=0;
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value,
    int index, boolean isSelected, boolean cellHasFocus) {
        
        //Get the object MiniDraw which save commands and file informations.
        MiniDraw md = (MiniDraw)value;
        commands = md.getCommands();
        name = md.getFile().getName();
        //Get the width. It will be used in the paint method.
        w = list.getWidth();
        
        this.isSelected = isSelected;
        
        //Get the list of commands and use it with firstX, lastX, firstY, lastY.
        //These values are mininum and maximum on x and y.
        listComs = getListOfCommands(commands);
        firstX = getFirstX(); lastX = getLastX();
        firstY = getFirstY(); lastY = getLastY();
        
        //Calculate the distance between firstX and lastX
        //and between firstY and lastY.
        ampX = Math.abs(firstX)+Math.abs(lastX);
        ampY = Math.abs(firstY)+Math.abs(lastY);
        
        //Just keep the highest distance
        if(ampX>ampY){
            mySens = Sens.Horizontal; amp = ampX;
        }else{
            mySens = Sens.Vertical; amp = ampY;
        }
        
        //Get new commands because we have to draw a shape in 100x100 square.
        newComs = translateX(commands, firstX);
        newComs = translateY(newComs, firstY);
        newComs = scale(newComs);
        
        //Update the shape list which be used in the paint method.
        updateShapeList(getListOfCommands(newComs));        
        
        return this;
    }
    
    @Override
    public void paint(Graphics g){
        
        Graphics2D g2 = (Graphics2D) g;
        
        // Clear all with a blank.
        g2.setBackground(getBackground());
        g2.fillRect(0, 0, w, h);
        
        // If selected, change the background and draw a rectangle.
        if(isSelected==true){
            //Draw a background
            g2.setColor(new Color(249,255,237));
            g2.fillRect(0, 0, w, h);
            //Draw a rounded border
            java.awt.Stroke stroke = g2.getStroke();
            g2.setStroke(new BasicStroke(3.0f));
            g2.setColor(SystemColor.controlHighlight);
            g2.drawRoundRect(0, 0, w-2, h, 5, 5);            
            g2.setStroke(stroke);
        }
        
        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        //These lines has been used for the debug :
//        g2.setColor(Color.red);
//        g2.drawString(commands, 10, 10);
//        
//        g2.setColor(Color.blue);
//        g2.drawString(String.valueOf(firstX), 10, 30);
//        g2.drawString(String.valueOf(lastX), 50, 30);
//        g2.drawString(String.valueOf(firstY), 10, 50);
//        g2.drawString(String.valueOf(lastY), 50, 50);
//        g2.drawString(String.valueOf(ampX), 90, 30);
//        g2.drawString(String.valueOf(ampY), 90, 50);
//        g2.drawString(mySens.toString(), 10, 70);
//        g2.drawString(newComs, 10, 90);
        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        
        //Draw the shape (general path)
        if(gpath!=null){
            Composite originalComposite = g2.getComposite();//Get default
            g2.setComposite(makeComposite(gpAlpha));//Change the transparency
            g2.setColor(Color.green);
            g2.fill(gpath);
            g2.setComposite(originalComposite);//Reset default
        }
        
        //Draw the shape (lines and points)
        // Consulte la liste des formes et les dessine en utilisant des couleurs différentes.
        for(IShape s : slist.getShapes()){
            if (s instanceof Line){
                Line l = (Line)s;
                g2.setColor(Color.red);
                g2.drawLine((int)l.getOriginPoint().getX(),(int)l.getOriginPoint().getY(),
                        (int)l.getLastPoint().getX(),(int)l.getLastPoint().getY());
            }else if (s instanceof Bezier){
                Bezier b = (Bezier)s;
                CubicCurve2D c = new CubicCurve2D.Double();
                c.setCurve((int)b.getOriginPoint().getX(), (int)b.getOriginPoint().getY(),
                        (int)b.getControl1Point().getX(), (int)b.getControl1Point().getY(),
                        (int)b.getControl2Point().getX(), (int)b.getControl2Point().getY(),
                        (int)b.getLastPoint().getX(), (int)b.getLastPoint().getY());
                g2.setColor(Color.magenta);
                g2.draw(c);

                //§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§
                // Don't draw the dashed lines :
//                //fait en sorte de mieux voir les points de contrôle
//                g2.setColor(Color.gray);
//                java.awt.Stroke stroke = g2.getStroke();
//                float[] dash = {5, 5};
//                g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT,
//                                  BasicStroke.JOIN_MITER, 5,
//                                  dash, 0));
//                g2.drawLine((int)b.getOriginPoint().getX(),(int)b.getOriginPoint().getY(),
//                        (int)b.getControl1Point().getX(),(int)b.getControl1Point().getY());
//                g2.drawLine((int)b.getControl1Point().getX(),(int)b.getControl1Point().getY(),
//                        (int)b.getControl2Point().getX(), (int)b.getControl2Point().getY());
//                g2.drawLine((int)b.getControl2Point().getX(), (int)b.getControl2Point().getY(),
//                        (int)b.getLastPoint().getX(), (int)b.getLastPoint().getY());
//                g2.setStroke(stroke);
                //§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§
            }else if (s instanceof Point){
                //§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§
                // Don't draw the points :
//                zdrawinglite.Point p = (zdrawinglite.Point)s;
//                int x = (int)p.getOriginPoint().getX();
//                int y = (int)p.getOriginPoint().getY();
//                g2.setColor(Color.blue);
//                g2.fillRect(x-10/2,y-10/2,10,10);
                //§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§
            }else if(s instanceof ControlPoint){
                //§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§
                // Don't draw the points :
//                zdrawinglite.ControlPoint cp = (zdrawinglite.ControlPoint)s;
//                int x = (int)cp.getOriginPoint().getX();
//                int y = (int)cp.getOriginPoint().getY();
//                g2.setColor(Color.orange);
//                g2.fillOval(x-10/2,y-10/2,10,10);
                //§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§
            }
        }
        
        //Draw the name of the file :
        g2.setColor(Color.black);
        g2.drawString(name, 10, 95);
        
    }
    
    private int getFirstX(){
        int fX=0; int x=0;
        
        for(String s : listComs){
            if(s.startsWith("m") | s.startsWith("l")){
                String[] numbers = s.substring(2).split(" ");
                x = Integer.parseInt(numbers[0]); if(x<fX){fX=x;}                
            }
            if(s.startsWith("b")){
                String[] numbers = s.substring(2).split(" ");
                x = Integer.parseInt(numbers[0]); if(x<fX){fX=x;}
                x = Integer.parseInt(numbers[2]); if(x<fX){fX=x;}
                x = Integer.parseInt(numbers[4]); if(x<fX){fX=x;}
            }
        }
        return fX;
    }
    
    private int getLastX(){
        int lX=0; int x=0;
        
        for(String s : listComs){
            if(s.startsWith("m") | s.startsWith("l")){
                String[] numbers = s.substring(2).split(" ");
                x = Integer.parseInt(numbers[0]); if(x>lX){lX=x;}                
            }
            if(s.startsWith("b")){
                String[] numbers = s.substring(2).split(" ");
                x = Integer.parseInt(numbers[0]); if(x>lX){lX=x;}
                x = Integer.parseInt(numbers[2]); if(x>lX){lX=x;}
                x = Integer.parseInt(numbers[4]); if(x>lX){lX=x;}
            }
        }
        return lX;
    }
    
    private int getFirstY(){
        int fY=0; int y=0;
        
        for(String s : listComs){
            if(s.startsWith("m") | s.startsWith("l")){
                String[] numbers = s.substring(2).split(" ");
                y = Integer.parseInt(numbers[1]); if(y<fY){fY=y;}                
            }
            if(s.startsWith("b")){
                String[] numbers = s.substring(2).split(" ");
                y = Integer.parseInt(numbers[1]); if(y<fY){fY=y;}
                y = Integer.parseInt(numbers[3]); if(y<fY){fY=y;}
                y = Integer.parseInt(numbers[5]); if(y<fY){fY=y;}
            }
        }
        return fY;
    }
    
    private int getLastY(){
        int lY=0; int y=0;
        
        for(String s : listComs){
            if(s.startsWith("m") | s.startsWith("l")){
                String[] numbers = s.substring(2).split(" ");
                y = Integer.parseInt(numbers[1]); if(y>lY){lY=y;}                
            }
            if(s.startsWith("b")){
                String[] numbers = s.substring(2).split(" ");
                y = Integer.parseInt(numbers[1]); if(y>lY){lY=y;}
                y = Integer.parseInt(numbers[3]); if(y>lY){lY=y;}
                y = Integer.parseInt(numbers[5]); if(y>lY){lY=y;}
            }
        }
        return lY;
    }
    
    private List<String> getListOfCommands(String com){
        List<String> coms = new ArrayList<String>();
        while (com.length() > 0){
            if(com.startsWith("m") | com.startsWith("l") | com.startsWith("b")){
                int index_of_line = com.substring(1).indexOf("l")+1;
                int index_of_bezier = com.substring(1).indexOf("b")+1;
                if((index_of_line!=0 && index_of_line<index_of_bezier) | (index_of_bezier==0 && index_of_bezier<index_of_line)){
                    coms.add(com.substring(0, index_of_line-1));
                    com = com.substring(index_of_line);
                }else if((index_of_bezier!=0 && index_of_bezier<index_of_line) | (index_of_line==0 && index_of_line<index_of_bezier)){
                    coms.add(com.substring(0, index_of_bezier-1));
                    com = com.substring(index_of_bezier);
                }else if(index_of_bezier==0 | index_of_line==0){
                    if(com.endsWith(" ")){
                        coms.add(com.substring(0,com.lastIndexOf(" ")));
                    }else{
                        coms.add(com.substring(0));
                    }
                    com = "";
                }
            }
        }
        return coms;
    }
    
    private String translateX(String commands, int firstX){
        String newCommands=""; int x=0; int y=0;
        List<String> newListComs = getListOfCommands(commands);
        
        if(firstX<0){
            for(String s : newListComs){
                if(s.startsWith("m") | s.startsWith("l")){
                    String[] numbers = s.substring(2).split(" ");
                    x = Integer.parseInt(numbers[0])+Math.abs(firstX);
                    y = Integer.parseInt(numbers[1]);
                    newCommands += s.substring(0, 2)+x+" "+y+" ";
                }
                if(s.startsWith("b")){
                    String[] numbers = s.substring(2).split(" ");
                    x = Integer.parseInt(numbers[0])+Math.abs(firstX);
                    y = Integer.parseInt(numbers[1]);
                    newCommands += s.substring(0, 2)+x+" "+y+" ";
                    x = Integer.parseInt(numbers[2])+Math.abs(firstX);
                    y = Integer.parseInt(numbers[3]);
                    newCommands += x+" "+y+" ";
                    x = Integer.parseInt(numbers[4])+Math.abs(firstX);
                    y = Integer.parseInt(numbers[5]);
                    newCommands += x+" "+y+" ";
                }
            }
        }
        
        if(firstX>=0){
            for(String s : newListComs){
                if(s.startsWith("m") | s.startsWith("l")){
                    String[] numbers = s.substring(2).split(" ");
                    x = Integer.parseInt(numbers[0])-Math.abs(firstX);
                    y = Integer.parseInt(numbers[1]);
                    newCommands += s.substring(0, 2)+x+" "+y+" ";
                }
                if(s.startsWith("b")){
                    String[] numbers = s.substring(2).split(" ");
                    x = Integer.parseInt(numbers[0])-Math.abs(firstX);
                    y = Integer.parseInt(numbers[1]);
                    newCommands += s.substring(0, 2)+x+" "+y+" ";
                    x = Integer.parseInt(numbers[2])-Math.abs(firstX);
                    y = Integer.parseInt(numbers[3]);
                    newCommands += x+" "+y+" ";
                    x = Integer.parseInt(numbers[4])-Math.abs(firstX);
                    y = Integer.parseInt(numbers[5]);
                    newCommands += x+" "+y+" ";
                }
            }
        }
        return newCommands;
    }
    
    private String translateY(String commands, int firstY){
        String newCommands=""; int x=0; int y=0;
        List<String> newListComs = getListOfCommands(commands);
        
        if(firstY<0){
            for(String s : newListComs){
                if(s.startsWith("m") | s.startsWith("l")){
                    String[] numbers = s.substring(2).split(" ");
                    x = Integer.parseInt(numbers[0]);
                    y = Integer.parseInt(numbers[1])+Math.abs(firstY);
                    newCommands += s.substring(0, 2)+x+" "+y+" ";
                }
                if(s.startsWith("b")){
                    String[] numbers = s.substring(2).split(" ");
                    x = Integer.parseInt(numbers[0]);
                    y = Integer.parseInt(numbers[1])+Math.abs(firstY);
                    newCommands += s.substring(0, 2)+x+" "+y+" ";
                    x = Integer.parseInt(numbers[2]);
                    y = Integer.parseInt(numbers[3])+Math.abs(firstY);
                    newCommands += x+" "+y+" ";
                    x = Integer.parseInt(numbers[4]);
                    y = Integer.parseInt(numbers[5])+Math.abs(firstY);
                    newCommands += x+" "+y+" ";
                }
            }
        }
        
        if(firstY>=0){
            for(String s : newListComs){
                if(s.startsWith("m") | s.startsWith("l")){
                    String[] numbers = s.substring(2).split(" ");
                    x = Integer.parseInt(numbers[0]);
                    y = Integer.parseInt(numbers[1])-Math.abs(firstY);
                    newCommands += s.substring(0, 2)+x+" "+y+" ";
                }
                if(s.startsWith("b")){
                    String[] numbers = s.substring(2).split(" ");
                    x = Integer.parseInt(numbers[0]);
                    y = Integer.parseInt(numbers[1])-Math.abs(firstY);
                    newCommands += s.substring(0, 2)+x+" "+y+" ";
                    x = Integer.parseInt(numbers[2]);
                    y = Integer.parseInt(numbers[3])-Math.abs(firstY);
                    newCommands += x+" "+y+" ";
                    x = Integer.parseInt(numbers[4]);
                    y = Integer.parseInt(numbers[5])-Math.abs(firstY);
                    newCommands += x+" "+y+" ";
                }
            }
        }
        return newCommands;
    }
    
    private String scale(String commands){
        String newCommands=""; int x=0; int y=0;
        List<String> newListComs = getListOfCommands(commands);
        
        for(String s : newListComs){            
            if(s.startsWith("m") | s.startsWith("l")){
                String[] numbers = s.substring(2).split(" ");
                x = Math.round(Integer.parseInt(numbers[0])*100/amp);
                y = Math.round(Integer.parseInt(numbers[1])*100/amp);
                newCommands += s.substring(0, 2)+x+" "+y+" ";
            }
            if(s.startsWith("b")){
                String[] numbers = s.substring(2).split(" ");
                x = Math.round(Integer.parseInt(numbers[0])*100/amp);
                y = Math.round(Integer.parseInt(numbers[1])*100/amp);
                newCommands += s.substring(0, 2)+x+" "+y+" ";
                x = Math.round(Integer.parseInt(numbers[2])*100/amp);
                y = Math.round(Integer.parseInt(numbers[3])*100/amp);
                newCommands += x+" "+y+" ";
                x = Math.round(Integer.parseInt(numbers[4])*100/amp);
                y = Math.round(Integer.parseInt(numbers[5])*100/amp);
                newCommands += x+" "+y+" ";
            }
        }
        
        return newCommands;
    }
    
    private void updateShapeList(List<String> commands){
        slist.removeAllShapes();
        java.awt.Point porigin, plast; porigin = null; plast = null;
        for(String s : commands){
            if(s.startsWith("m")){
                String[] result = s.substring(2).split(" ");
                porigin = new java.awt.Point(Integer.parseInt(result[0]),Integer.parseInt(result[1]));
                Point point =
                        new Point((int)porigin.getX(),(int)porigin.getY());
                slist.addShape(point);
            }else if (s.startsWith("l")){
                String[] result = s.substring(2).split(" ");
                plast = new java.awt.Point(Integer.parseInt(result[0]),Integer.parseInt(result[1]));
                Point point =
                        new Point((int)plast.getX(),(int)plast.getY());
                slist.addShape(point);
                Line line = new Line();
                line.setOriginPoint((int)porigin.getX(),(int)porigin.getY());
                line.setLastPoint((int)plast.getX(),(int)plast.getY());
                slist.addShape(line);
                porigin = plast;
            }else if (s.startsWith("b")){
                String[] result = s.substring(2).split(" ");
                plast = new java.awt.Point(Integer.parseInt(result[4]),Integer.parseInt(result[5]));
                java.awt.Point p1 = new java.awt.Point(Integer.parseInt(result[0]),Integer.parseInt(result[1]));
                java.awt.Point p2 = new java.awt.Point(Integer.parseInt(result[2]),Integer.parseInt(result[3]));
                Point point =
                        new Point((int)plast.getX(),(int)plast.getY());
                slist.addShape(point);
                Bezier b =
                        new Bezier((int)porigin.getX(),
                            (int)porigin.getY(), (int)plast.getX(), (int)plast.getY());
                ControlPoint cp1 =
                        new ControlPoint((int)p1.getX(), (int)p1.getY());
                b.setControl1(cp1);
                ControlPoint cp2 =
                        new ControlPoint((int)p2.getX(), (int)p2.getY());
                b.setControl2(cp2);
                slist.addShape(b);
                slist.addShape(cp1);
                slist.addShape(cp2);
                porigin = plast;
            }
        }
        gpath = getGeneralPath();
        first = new java.awt.Point((int)porigin.getX(), (int)porigin.getY());
        last = new java.awt.Point((int)plast.getX(), (int)plast.getY());
    }
    
    private GeneralPath getGeneralPath(){
        GeneralPath gp = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
        int count = 0;
        for(IShape s : slist.getShapes()){
            // Add to the path
            if(s instanceof Line){
                Line l = (Line)s;
                if(count==0){
                    gp.moveTo(l.getOriginPoint().getX(), l.getOriginPoint().getY());
                }else{
                    gp.lineTo(l.getLastPoint().getX(), l.getLastPoint().getY());
                }
            }else if(s instanceof Bezier){
                Bezier b = (Bezier)s;
                if(count==0){
                    gp.moveTo(b.getOriginPoint().getX(), b.getOriginPoint().getY());
                }else{
                    gp.curveTo(b.getControl1Point().getX(), b.getControl1Point().getY(),
                            b.getControl2Point().getX(), b.getControl2Point().getY(),
                            b.getLastPoint().getX(), b.getLastPoint().getY());
                }
            }else if(s instanceof Point){
                //If this is the first point (we always start drawing with a point)
                if(count==0){
                    Point p = (Point)s;
                    gp.moveTo(p.getOriginPoint().getX(), p.getOriginPoint().getY());
                }
            }
            count+=1;
        }
        return gp;
    }
    
    private AlphaComposite makeComposite(float alpha) {
        int type = AlphaComposite.SRC_OVER;
        return(AlphaComposite.getInstance(type, alpha));
    }
    
}
