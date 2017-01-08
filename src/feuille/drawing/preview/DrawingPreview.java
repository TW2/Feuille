/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.drawing.preview;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.GeneralPath;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import feuille.drawing.adf.DrawingObject;
import feuille.drawing.adf.LayerContent;
import feuille.drawing.adf.XmlDrawingHandler;
import feuille.drawing.lib.BSpline;
import feuille.drawing.lib.Bezier;
import feuille.drawing.lib.Command;
import feuille.drawing.lib.ControlPoint;
import feuille.drawing.lib.IShape;
import feuille.drawing.lib.Layer;
import feuille.drawing.lib.Line;
import feuille.drawing.lib.Move;
import feuille.drawing.lib.Point;
import feuille.drawing.lib.ReStart;
import feuille.drawing.lib.ShapesList;

/**
 *
 * @author The Wingate 2940
 */
public class DrawingPreview extends JComponent implements PropertyChangeListener {
    
    File file = null;
    private int firstX, lastX, firstY, lastY, ampX, ampY, amp;
    String[] commands; //String commands = "";
    List<Layer> layers = new ArrayList<Layer>();
    String newComs = "";
    private List<String> listComs;
    ShapesList slist = new ShapesList();
    java.awt.Point first = null;
    java.awt.Point last = null;
    private GeneralPath gpath = null;
    private Float gpAlpha = 0.2f;
    private boolean isADF = false;

    @SuppressWarnings("LeakingThisInConstructor")
    public DrawingPreview(JFileChooser fc) {
        setPreferredSize(new Dimension(100, 100));
        fc.addPropertyChangeListener(this);
        firstX=0; lastX=0; firstY=0; lastY=0; ampX=0; ampY=0; amp=0;
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String prop = evt.getPropertyName();

        //If the directory changed, don't show an image.
        if (JFileChooser.DIRECTORY_CHANGED_PROPERTY.equals(prop)) {
            file = null;
            isADF = false;

        //If a file became selected, find out which one.
        } else if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(prop)) {
            try{
                file = (File) evt.getNewValue();
                if(file.getName().endsWith(".adf")){
                    isADF = true;
                }else{
                    isADF = false;
                }
            }catch(Exception exc){
                
            }
        }

        //Update the preview accordingly.
        try{
            if (isADF) {
                if (isShowing()) {
                    if(file.getName().endsWith(".adf")){
                        layers = getLayersFromFile(file.getAbsolutePath());
                    }
                }
            }
            repaint();
        }catch(Exception exc){
                
        }        
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        
//        // Clear all with a blank.
//        g2.setBackground(Color.white);
//        g2.fillRect(0, 0, 100, 100);
        
        if(isADF==true){
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

            for(Layer layer : layers){
                //Draw the shape (general path)
                if(layer.getGeneralPath()!=null){
                    Composite originalComposite = g2.getComposite();//Get default
                    g2.setComposite(makeComposite(gpAlpha));//Change the transparency
                    g2.setColor(layer.getColor());
                    g2.fill(layer.getGeneralPath());
                    g2.setComposite(originalComposite);//Reset default
                }

                //Draw the shape (lines and points)
                // Consulte la liste des formes et les dessine en utilisant des couleurs différentes.
                for(IShape s : layer.getShapesList().getShapes()){
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
                    }else if (s instanceof BSpline){                        
                        BSpline bs = (BSpline)s;
                        //§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§
                        // Don't draw the points :
//                        ControlPoint c = null;
//                        for(ControlPoint cp : bs.getControlPoints()){                    
//                            g2.setColor(Color.orange);
//                            int x1 = (int)cp.getOriginPoint().getX();
//                            int y1 = (int)cp.getOriginPoint().getY();
//                            g2.drawOval(
//                                x1-thickness.getThickness()/2,
//                                y1-thickness.getThickness()/2,
//                                thickness.getThickness(),
//                                thickness.getThickness());
//                            g2.setColor(Color.gray);
//                            g2.fillOval(
//                                x1-thickness.getThickness()/2,
//                                y1-thickness.getThickness()/2,
//                                thickness.getThickness(),
//                                thickness.getThickness());
//                            if(c!=null){
//                                g2.setColor(Color.orange);
//                                java.awt.Stroke stroke = g2.getStroke();
//                                float[] dash = {5, 5};
//                                g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT,
//                                                BasicStroke.JOIN_MITER, 5,
//                                                dash, 0));
//                                g2.drawLine((int)c.getOriginPoint().getX(),(int)c.getOriginPoint().getY(),
//                                        (int)cp.getOriginPoint().getX(),(int)cp.getOriginPoint().getY());
//                                g2.setStroke(stroke);
//                            }
//                            c = cp;
//                        }
                        //§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§
                        g2.setColor(Color.black);
                        bs.getBSplineCurve().paintCurve(g2, 5, true);
                        //§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§
                        // Don't draw the points :
//                        if(bs.isNextExist()){
//                            g2.setColor(Color.pink);
//                            g2.fillRect(
//                            (int)bs.getNextPoint().getX()-thickness.getThickness()/2,
//                            (int)bs.getNextPoint().getY()-thickness.getThickness()/2,
//                            thickness.getThickness(),
//                            thickness.getThickness());
//                        }
                        //§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§
                    }
                }
            }            
        }        
    }
    
    private String openDrawingFile(String path){
        String dcommands;
        File f = new File(path);
        try{
            FileInputStream fis = new FileInputStream(f);
            java.io.BufferedReader br = new java.io.BufferedReader(
                    new java.io.InputStreamReader(fis, "UTF-8"));
            String line = br.readLine();
            String[] fragments = line.split(";");
            dcommands = fragments[2];
            br.close(); fis.close();
            return dcommands;
        }catch (Exception exc){
            return "";
        }
    }
    
    private List<Layer> getLayersFromFile(String path){
        try {
            XmlDrawingHandler xdh = new XmlDrawingHandler(path);
            DrawingObject dro = xdh.getDrawingObject();
            List<Layer> layerList = new ArrayList<Layer>();
            for(LayerContent lc : dro.getLayers()){
                Layer lay = new Layer();
                lay.setName(lc.getName());
                lay.setColor(lc.getColor());
                //ShapesList shapesList = new ShapesList();
                //********** PROCESS pour ajustement à la zone ***************** 
                listComs = getListOfCommands(lc.getAssCommands());              //Get the list of commands and use it with firstX, lastX, firstY, lastY.
                firstX = getFirstX(); lastX = getLastX();                       //These values are mininum and maximum on x and y.
                firstY = getFirstY(); lastY = getLastY();
                ampX = Math.abs(firstX)+Math.abs(lastX);                        //Calculate the distance between firstX and lastX
                ampY = Math.abs(firstY)+Math.abs(lastY);                        //and between firstY and lastY.
                if(ampX>ampY){                                                  //Just keep the highest distance
                    amp = ampX;
                }else{
                    amp = ampY;
                }
                newComs = translateX(lc.getAssCommands(), firstX);              //Get new commands because we have to draw a shape in 100x100 square.
                newComs = translateY(newComs, firstY);
                newComs = scale(newComs);
                //**************************************************************
                //updateShapeList(getListOfCommands(newComs), shapesList);
                //lay.setShapesList(shapesList);
                shapesFromCommands(newComs, lay);                               //Mise à jour des formes
                layerList.add(lay);
            }
            return layerList;
        } catch (Exception ex) {
            return null;
        }
    }
    
    private int getFirstX(){
        int fX=0; int x;
        
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
        int lX=0; int x;
        
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
        int fY=0; int y;
        
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
        int lY=0; int y;
        
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
    
    //Met à jour le dessin à partir des commandes ASS
    private void shapesFromCommands(String com, Layer lay){
        List<Command> commandList = new ArrayList<Command>();
        Pattern pat = Pattern.compile("([a-z]*)\\s*(-*\\d*)\\s*(-*\\d*)\\s*");
        Matcher mat = pat.matcher(com);
        while(mat.find()){
            //System.out.println(mat.group(1)+" <> "+mat.group(2)+" | "+mat.group(3));
            try{
                String param = mat.group(1);
                if(param.equalsIgnoreCase("c")){
                    Command c = new Command(Command.Type.Close);
                    commandList.add(c);
                }
                int xs = Integer.parseInt(mat.group(2));
                int ys = Integer.parseInt(mat.group(3));
                //System.out.println("+++ "+angle);
                
                if(param.equalsIgnoreCase("m")){
                    Command c = new Command(Command.Type.ReStart); c.add_X(xs); c.add_Y(ys);
                    commandList.add(c);
                }
                if(param.equalsIgnoreCase("l")){
                    Command c = new Command(Command.Type.Line); c.add_X(xs); c.add_Y(ys);
                    commandList.add(c);
                }
                if(param.equalsIgnoreCase("b")){
                    Command c = new Command(Command.Type.Bezier); c.add_X(xs); c.add_Y(ys);
                    commandList.add(c);
                }
                if(param.equalsIgnoreCase("s")){
                    Command c = new Command(Command.Type.BSpline); c.add_X(xs); c.add_Y(ys);
                    commandList.add(c);
                }
                if(param.equalsIgnoreCase("n")){
                    Command c = new Command(Command.Type.Move); c.add_X(xs); c.add_Y(ys);
                    commandList.add(c);
                }
                if(param.equalsIgnoreCase("p")){
                    Command c = new Command(Command.Type.Extend); c.add_X(xs); c.add_Y(ys);
                    commandList.add(c);
                }
                if(param.equalsIgnoreCase("") & commandList.get(commandList.size()-1).getType()==Command.Type.Bezier){
                    Command c = commandList.get(commandList.size()-1); c.add_X(xs); c.add_Y(ys);
                }
                if(param.equalsIgnoreCase("") & commandList.get(commandList.size()-1).getType()==Command.Type.BSpline){
                    Command c = commandList.get(commandList.size()-1); c.add_X(xs); c.add_Y(ys);
                }
            }catch(Exception e){
                //Nombre non existant
            }            
        }
        
        lay.getShapesList().removeAllShapes();
        lay.clearRemembers();
        java.awt.Point p_init = new java.awt.Point();
        Command last_command = null;
        
        for(Command c : commandList){
            if(c.getType()==Command.Type.ReStart){
                ReStart p = c.getStartPoint((int)p_init.getX(), (int)p_init.getY());                
                lay.getShapesList().addShape(p); lay.addRemember(p);
                p_init = p.getLastPoint();
            }
            if(c.getType()==Command.Type.Line){
                Line l = c.getLine((int)p_init.getX(), (int)p_init.getY());
                Point p = new Point((int)l.getLastPoint().getX(), (int)l.getLastPoint().getY());
                lay.getShapesList().addShape(p); lay.addRemember(p);
                lay.getShapesList().addShape(l); lay.addRemember(l);
                p_init = l.getLastPoint();
            }
            if(c.getType()==Command.Type.Bezier){
                Bezier b = c.getBezier((int)p_init.getX(), (int)p_init.getY());
                Point p = new Point((int)b.getLastPoint().getX(), (int)b.getLastPoint().getY());
                lay.getShapesList().addShape(p); lay.addRemember(p);
                lay.getShapesList().addShape(b); lay.addRemember(b);
                lay.getShapesList().addShape(b.getControl1()); lay.addRemember(b.getControl1());
                lay.getShapesList().addShape(b.getControl2()); lay.addRemember(b.getControl2());
                p_init = b.getLastPoint();
            }
            if(c.getType()==Command.Type.BSpline){
                BSpline b = c.getBSpline((int)p_init.getX(), (int)p_init.getY());
                Point p = new Point((int)b.getLastPoint().getX(), (int)b.getLastPoint().getY());
                lay.getShapesList().addShape(p); lay.addRemember(p);
                lay.getShapesList().addShape(b); lay.addRemember(b);
                p_init = b.getLastPoint();
            }
            if(c.getType()==Command.Type.Extend && last_command.getType()==Command.Type.BSpline){
                BSpline b = (BSpline)lay.getShapesList().getLastShape();
                Point p = c.getExtendPoint();
                if(b.isClosed()==false){
                    b.setNextPoint((int)p.getLastPoint().getX(), (int)p.getLastPoint().getY());
                    b.addPoint((int)p.getLastPoint().getX(), (int)p.getLastPoint().getY());
                    b.setLastPoint((int)p.getLastPoint().getX(), (int)p.getLastPoint().getY());
                }
                p_init = b.getLastPoint();
            }
            if(c.getType()==Command.Type.Close && last_command.getType()==Command.Type.BSpline){
                BSpline b = (BSpline)lay.getShapesList().getLastShape();
                if(b.isNextExist()==false){
                    b.setClosed(true);
                }
                p_init = b.getLastPoint();
            }
            if(c.getType()==Command.Type.Move){
                Move m = c.getMove((int)p_init.getX(), (int)p_init.getY());
                lay.getShapesList().addShape(m); lay.addRemember(m);
                p_init = m.getLastPoint();
            }
            last_command = c;
        }
        lay.setFirstPoint(p_init);
        lay.setLastPoint(p_init);
    }
    
    private String translateX(String commands, int firstX){
        String newCommands=""; int x, y;
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
        String newCommands=""; int x, y;
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
        String newCommands=""; int x, y;
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
        updateShapeList(commands, slist);
    }
    
    private void updateShapeList(List<String> commands, ShapesList SL){
        SL.removeAllShapes();
        java.awt.Point porigin, plast; porigin = null; plast = null;
        for(String s : commands){
            if(s.startsWith("m")){
                String[] result = s.substring(2).split(" ");
                porigin = new java.awt.Point(Integer.parseInt(result[0]),Integer.parseInt(result[1]));
                Point point =
                        new Point((int)porigin.getX(),(int)porigin.getY());
                SL.addShape(point);
            }else if (s.startsWith("l")){
                String[] result = s.substring(2).split(" ");
                plast = new java.awt.Point(Integer.parseInt(result[0]),Integer.parseInt(result[1]));
                Point point =
                        new Point((int)plast.getX(),(int)plast.getY());
                SL.addShape(point);
                Line line = new Line();
                line.setOriginPoint((int)porigin.getX(),(int)porigin.getY());
                line.setLastPoint((int)plast.getX(),(int)plast.getY());
                SL.addShape(line);
                porigin = plast;
            }else if (s.startsWith("b")){
                String[] result = s.substring(2).split(" ");
                plast = new java.awt.Point(Integer.parseInt(result[4]),Integer.parseInt(result[5]));
                java.awt.Point p1 = new java.awt.Point(Integer.parseInt(result[0]),Integer.parseInt(result[1]));
                java.awt.Point p2 = new java.awt.Point(Integer.parseInt(result[2]),Integer.parseInt(result[3]));
                Point point =
                        new Point((int)plast.getX(),(int)plast.getY());
                SL.addShape(point);
                Bezier b =
                        new Bezier((int)porigin.getX(),
                            (int)porigin.getY(), (int)plast.getX(), (int)plast.getY());
                ControlPoint cp1 =
                        new ControlPoint((int)p1.getX(), (int)p1.getY());
                b.setControl1(cp1);
                ControlPoint cp2 =
                        new ControlPoint((int)p2.getX(), (int)p2.getY());
                b.setControl2(cp2);
                SL.addShape(b);
                SL.addShape(cp1);
                SL.addShape(cp2);
                porigin = plast;
            }
        }
//        gpath = getGeneralPath(SL);
        first = new java.awt.Point((int)porigin.getX(), (int)porigin.getY());
        last = new java.awt.Point((int)plast.getX(), (int)plast.getY());
    }
    
//    private GeneralPath getGeneralPath(ShapesList SL){
//        GeneralPath gp = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
//        int count = 0;
//        for(Shape s : SL.getShapes()){
//            // Add to the path
//            if(s instanceof Line){
//                Line l = (Line)s;
//                if(count==0){
//                    gp.moveTo(l.getOriginPoint().getX(), l.getOriginPoint().getY());
//                }else{
//                    gp.lineTo(l.getLastPoint().getX(), l.getLastPoint().getY());
//                }
//            }else if(s instanceof Bezier){
//                Bezier b = (Bezier)s;
//                if(count==0){
//                    gp.moveTo(b.getOriginPoint().getX(), b.getOriginPoint().getY());
//                }else{
//                    gp.curveTo(b.getControl1Point().getX(), b.getControl1Point().getY(),
//                            b.getControl2Point().getX(), b.getControl2Point().getY(),
//                            b.getLastPoint().getX(), b.getLastPoint().getY());
//                }
//            }else if(s instanceof Point){
//                //If this is the first point (we always start drawing with a point)
//                if(count==0){
//                    Point p = (Point)s;
//                    gp.moveTo(p.getOriginPoint().getX(), p.getOriginPoint().getY());
//                }
//            }
//            count+=1;
//        }
//        return gp;
//    }
    
    private AlphaComposite makeComposite(float alpha) {
        int type = AlphaComposite.SRC_OVER;
        return(AlphaComposite.getInstance(type, alpha));
    }
    
}
