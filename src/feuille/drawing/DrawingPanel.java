/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.drawing;

import feuille.drawing.lib.ControlPoint;
import feuille.drawing.lib.Sheet;
import feuille.drawing.lib.Clipboard;
import feuille.drawing.lib.IShape;
import feuille.drawing.lib.Remember;
import feuille.drawing.lib.Line;
import feuille.drawing.lib.SheetVBorder;
import feuille.drawing.lib.ReStart;
import feuille.drawing.lib.Layer;
import feuille.drawing.lib.BSpline;
import feuille.drawing.lib.ShapesList;
import feuille.drawing.lib.Bezier;
import feuille.drawing.lib.Command;
import feuille.drawing.lib.SheetHBorder;
import feuille.drawing.lib.Point;
import feuille.drawing.lib.Move;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import feuille.drawing.adf.DrawingObject;
import feuille.drawing.adf.LayerContent;
import feuille.drawing.adf.XmlDrawingHandler;
import feuille.drawing.adf.XmlDrawingWriter;
import feuille.drawing.dialog.OpenGlyphDialog;
import feuille.drawing.dialog.RotationDialog;
import feuille.drawing.dialog.SelectGeoDialog;
import feuille.filter.AssFilter;
import feuille.filter.DrawingFilter;
import feuille.filter.ImageFilter;
import feuille.filter.SVGFilter;
import feuille.drawing.operation.Center;
import feuille.drawing.operation.Resize;
import feuille.drawing.operation.Selection;
import feuille.drawing.operation.Shear;
import feuille.drawing.operation.Translation;
import feuille.drawing.ornament.AShape;
import feuille.drawing.ornament.AssLine;
import feuille.drawing.ornament.OrnControlPoint;
import feuille.drawing.ornament.OrnDo;
import feuille.drawing.ornament.OrnLayer;
import feuille.drawing.ornament.OrnMMBezier;
import feuille.drawing.ornament.OrnMMLine;
import feuille.drawing.ornament.OrnPoint;
import feuille.drawing.preview.DrawingPreview;
import feuille.drawing.preview.ImagePreview;
import feuille.drawing.renderer.LayerRenderer;
import feuille.drawing.renderer.RememberRenderer;
import feuille.drawing.renderer.ScriptsListRenderer;
import feuille.drawing.svg.VectorObject;
import feuille.drawing.svg.XmlVectorHandler;
import feuille.scripting.DrawingScript;
import feuille.scripting.ScriptPlugin;

/**
 *
 * @author The Wingate 2940
 */
public class DrawingPanel extends javax.swing.JPanel {

    static Sheet sh = new Sheet();
    private ImageIcon img = null;
    private static int scale = 1;
    private int imageMoveUnit = 10;
    private String lastDrawing = null;
    private String lastImage = null;
    private java.awt.Frame parentFrame = null; 
    private Thickness thickness = Thickness.Big;
    private static DefaultListModel dlm = new DefaultListModel();
    private static DefaultListModel dlmRemember = new DefaultListModel();
    private DefaultListModel dlmScript = new DefaultListModel();
    private BufferedImage imgDraft;
    private Graphics2D graDraft;
    private int penSizeUnit = 6;
    private int rubberSizeUnit = 6;
    private Color penColor = Color.darkGray;
    private Selection selection = new Selection();
    private SheetHBorder shhb;
    private SheetVBorder shvb;
    private ScriptPlugin scripting;
    private List<AssLine> karaokeOrnament = new ArrayList<>();
    private OrnLayer ornlayForMain = new OrnLayer();
    private OrnLayer ornlayForShape = new OrnLayer();
    private SpinnerNumberModel spinMMModel, spinSMModel;
    private DefaultTableModel dtmOrnForMain;
    private feuille.lib.Language localeLanguage;
    
    private String strOK_Button = "OK";
    private String strCancel_Button = "Annuler";
    private String strRedMessage = "<html><i>Vous devez faire un clic droit sur le dessin avant d'ouvrir cette fenêtre.";
    private String strMessage = "Angle de rotation";
    
    private String docs;
    private Frame frame;
    
    /**
     * Creates new form DrawingPanel
     */
    public DrawingPanel(String docs, Frame frame, feuille.lib.Language lang) {
        initComponents();
        localeLanguage = lang;
        init();
        this.docs = docs;
        this.frame = frame;
        setLanguageAndConfigure(lang);
    }
    
    private void init(){
        spSheet.setViewportView(sh);
        sh.setPreferredSize(new java.awt.Dimension(setSizeOfDrawing(),setSizeOfDrawing()));
        sh.revalidate();
        
        shhb = new SheetHBorder(setSizeOfDrawing());
        shvb = new SheetVBorder(setSizeOfDrawing());        
        spSheet.setColumnHeaderView(shhb);
        spSheet.setRowHeaderView(shvb);
        spSheet.setOpaque(true);
        spSheet.setBackground(Color.white);
        
        lstLayer.setModel(dlm);
        Layer layer = new Layer();
        layer.setSelected(true);
        layer.setFirst(true);
        dlm.addElement(layer);
        lstLayer.setSelectedValue(layer, true);
        lstLayer.setCellRenderer(new LayerRenderer());
        
        java.util.List<Layer> listlay = new ArrayList<>();
        for(Object o : dlm.toArray()){
            if( o instanceof Layer){
                Layer lay = (Layer)o;
                if(listlay.contains(lay)==false){
                    listlay.add(lay);
                    sh.updateShapesList(lay.getShapesList());
                }
            }
        }
        sh.setLayerList(listlay);
        int size = setSizeOfDrawing();
        imgDraft = new BufferedImage(size,size,BufferedImage.TYPE_INT_ARGB);
        graDraft = (Graphics2D)imgDraft.getGraphics();
        graDraft.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        
        listRemember.setModel(dlmRemember);
        listRemember.setCellRenderer(new RememberRenderer());
        
        listScript.setModel(dlmScript);
        listScript.setCellRenderer(new ScriptsListRenderer());
        
        spinMMModel = new SpinnerNumberModel(400, 4, 30000, 1);
        spiOrnMMFreq.setModel(spinMMModel);
        
        String[] forMainHead = new String[]{"Forme", "Durée"};
        
        if(localeLanguage.getValueOf("assSketchpadOrnShape")!=null){
            forMainHead[0] = localeLanguage.getValueOf("assSketchpadOrnShape");}
        if(localeLanguage.getValueOf("assSketchpadOrnDuration")!=null){
            forMainHead[1] = localeLanguage.getValueOf("assSketchpadOrnDuration");}
        
        dtmOrnForMain = new DefaultTableModel(
                null,
                forMainHead
        ){
            Class[] types = new Class [] {
                    AShape.class, java.lang.String.class};
            boolean[] canEdit = new boolean [] {
                    false, true};
            @Override
            public Class getColumnClass(int columnIndex) {return types [columnIndex];}
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {return canEdit [columnIndex];}
        };        
        ornMMTable.setModel(dtmOrnForMain);
        TableColumn column;
        for (int i = 0; i < 2; i++) {
            column = ornMMTable.getColumnModel().getColumn(i);
            switch(i){
                case 0:
                    column.setPreferredWidth(30);
                    break; //Forme
                case 1:
                    column.setPreferredWidth(30);
                    break; //Durée
            }
        }
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
    
    public void setScriptPlugin(ScriptPlugin splug){
        scripting = splug;
        for(Object o : scripting.getObjectsList()){
            if(o instanceof DrawingScript){
                boolean ajout = true;
                for(int i=0;i<dlmScript.getSize();i++){
                    DrawingScript scr1 = (DrawingScript)o;
                    DrawingScript scr2 = (DrawingScript)dlmScript.get(i);
                    if(scr1.getFunction().equalsIgnoreCase(scr2.getFunction())){
                        ajout = false;
                    }
                }
                if(ajout = true){
                    dlmScript.addElement(o);
                }
            }                        
        }        
    }
    
    public JInternalFrame getIfrFile(){
        return ifrOpenSave;
    }
    
    public JInternalFrame getIfrDraw(){
        return ifrMainDraw;
    }
    
    public JInternalFrame getIfrImage(){
        return ifrImage;
    }
    
    public JInternalFrame getIfrShape(){
        return ifrShape;
    }
    
    public JInternalFrame getIfrMode(){
        return ifrMode;
    }
    
    public JInternalFrame getIfrOperations(){
        return ifrOperations;
    }
    
    public JInternalFrame getIfrScripts(){
        return ifrScript;
    }
    
    public JInternalFrame getIfrHistoric(){
        return ifrHistoric;
    }
    
    public JInternalFrame getIfrLayers(){
        return ifrLayers;
    }
    
    public JInternalFrame getIfrSketchpad(){
        return ifrDrawing;
    }
    
    public JInternalFrame getIfrOrnament(){
        return ifrOrnament;
    }
    
    public JInternalFrame getIfrAssCommands(){
        return ifrAssCommands;
    }
    
    public void updateThickness(Thickness thickness){
        this.thickness = thickness;
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
    
    //Teste si le point a et le point b sont à la même coordonnée.
    private boolean isSamePoint(java.awt.Point a, java.awt.Point b){
        if(a.getX()!=b.getX()){return false;}
        if(a.getY()!=b.getY()){return false;}
        return true;
    }

    //Teste si les deux points sont proches.
    private boolean isNear(java.awt.Point area, java.awt.Point test){
        int x = (int)area.getX();
        int y = (int)area.getY();
        java.awt.Rectangle rect = new java.awt.Rectangle(
                    x-thickness.getThickness()/2,
                    y-thickness.getThickness()/2,
                    thickness.getThickness(),
                    thickness.getThickness());
        return rect.contains(test);
    }

//    //Met à jour les commandes ASS à partir du dessin
//    private void updateCommands(){
//        String commands = "";
//        Layer lay = getCurrentLayer();
//        try{
//            for(Shape s : lay.getShapesList().getShapes()){
//                if(s instanceof Line){
//                    Line line = (Line)s;
//                    int x = (int)line.getLastPoint().getX();
//                    int y = (int)line.getLastPoint().getY();
//                    int xb = x-(sh.getWidth()/scale)/2;
//                    int yb = y-(sh.getHeight()/scale)/2;
//                    commands = commands + "l "+xb+" "+yb+" ";
//                }else if(s instanceof Bezier){
//                    Bezier bezier = (Bezier)s;
//                    int x1 = (int)bezier.getControl1().getOriginPoint().getX();
//                    int y1 = (int)bezier.getControl1().getOriginPoint().getY();
//                    int x2 = (int)bezier.getControl2().getOriginPoint().getX();
//                    int y2 = (int)bezier.getControl2().getOriginPoint().getY();
//                    int x3 = (int)bezier.getLastPoint().getX();
//                    int y3 = (int)bezier.getLastPoint().getY();
//                    int xe = x1-(sh.getWidth()/scale)/2;
//                    int ye = y1-(sh.getHeight()/scale)/2;
//                    int xf = x2-(sh.getWidth()/scale)/2;
//                    int yf = y2-(sh.getHeight()/scale)/2;
//                    int xg = x3-(sh.getWidth()/scale)/2;
//                    int yg = y3-(sh.getHeight()/scale)/2;
//                    commands = commands + "b "+xe+" "+ye+" "+xf+" "+yf+" "+xg+" "+yg+" ";
//                }else if(s instanceof BSpline){
//                    BSpline bs = (BSpline)s;
//                    List<ControlPoint> lcp = bs.getControlPoints();
//                    int lastcp = lcp.size()-1;
//                    commands = commands + "s ";
//                    for(ControlPoint cp : lcp){
//                        int x = (int)cp.getOriginPoint().getX();
//                        int y = (int)cp.getOriginPoint().getY();
//                        int xi = x-(sh.getWidth()/scale)/2;
//                        int yi = y-(sh.getHeight()/scale)/2;
//                        if(bs.isNextExist()==true && cp.equals(lcp.get(lastcp))==true){
//                            //rien
//                        }else{
//                            commands = commands + xi+" "+yi+" ";
//                        }
//                    }
//                    if(bs.isClosed()==true){
//                        commands = commands + "c ";
//                    }
//                    if(bs.isNextExist()==true){
//                        int x = (int)bs.getNextPoint().getX();
//                        int y = (int)bs.getNextPoint().getY();
//                        int xi = x-(sh.getWidth()/scale)/2;
//                        int yi = y-(sh.getHeight()/scale)/2;
//                        commands = commands + "p "+xi+" "+yi+" ";
//                    }
//                }else if(s instanceof Move){
//                    Move move = (Move)s;
//                    int x = (int)move.getLastPoint().getX();
//                    int y = (int)move.getLastPoint().getY();
//                    int xb = x-(sh.getWidth()/scale)/2;
//                    int yb = y-(sh.getHeight()/scale)/2;
//                    commands = commands + "n "+xb+" "+yb+" ";
//                }else if(s instanceof ReStart){
//                    ReStart move = (ReStart)s;
//                    int x = (int)move.getLastPoint().getX();
//                    int y = (int)move.getLastPoint().getY();
//                    int xb = x-(sh.getWidth()/scale)/2;
//                    int yb = y-(sh.getHeight()/scale)/2;
//                    commands = commands + "m "+xb+" "+yb+" ";
//                }
//            }
////            try{
////                Point p = lay.getShapesList().getFirstPoint();
////                int x0 = (int)p.getOriginPoint().getX();
////                int y0 = (int)p.getOriginPoint().getY();
////                int xz = x0-(sh.getWidth()/scale)/2;
////                int yz = y0-(sh.getHeight()/scale)/2;
////                commands = "m "+xz+" "+yz+" " + commands;
////            }catch(Exception e){
////                
////            }
//            tfAssCommands.setText(commands);
//        }catch(Exception exc){
//            //exc.printStackTrace();
//        }        
//    }
    
    //Met à jour les commandes ASS à partir du dessin
    private String commandsFromShapes(Layer lay){
        String commands = "";
        try{
            for(IShape s : lay.getShapesList().getShapes()){
                if(s instanceof Line){
                    Line line = (Line)s;
                    int x = (int)line.getLastPoint().getX();
                    int y = (int)line.getLastPoint().getY();
                    int xb = x-(sh.getWidth()/scale)/2;
                    int yb = y-(sh.getHeight()/scale)/2;
                    commands = commands + "l "+xb+" "+yb+" ";
                }else if(s instanceof Bezier){
                    Bezier bezier = (Bezier)s;
                    int x1 = (int)bezier.getControl1().getOriginPoint().getX();
                    int y1 = (int)bezier.getControl1().getOriginPoint().getY();
                    int x2 = (int)bezier.getControl2().getOriginPoint().getX();
                    int y2 = (int)bezier.getControl2().getOriginPoint().getY();
                    int x3 = (int)bezier.getLastPoint().getX();
                    int y3 = (int)bezier.getLastPoint().getY();
                    int xe = x1-(sh.getWidth()/scale)/2;
                    int ye = y1-(sh.getHeight()/scale)/2;
                    int xf = x2-(sh.getWidth()/scale)/2;
                    int yf = y2-(sh.getHeight()/scale)/2;
                    int xg = x3-(sh.getWidth()/scale)/2;
                    int yg = y3-(sh.getHeight()/scale)/2;
                    commands = commands + "b "+xe+" "+ye+" "+xf+" "+yf+" "+xg+" "+yg+" ";
                }else if(s instanceof BSpline){
                    BSpline bs = (BSpline)s;
                    List<ControlPoint> lcp = bs.getControlPoints();
                    int lastcp = lcp.size()-1;
                    commands = commands + "s ";
                    for(ControlPoint cp : lcp){
                        int x = (int)cp.getOriginPoint().getX();
                        int y = (int)cp.getOriginPoint().getY();
                        int xi = x-(sh.getWidth()/scale)/2;
                        int yi = y-(sh.getHeight()/scale)/2;
                        if(bs.isNextExist()==true && cp.equals(lcp.get(lastcp))==true){
                            //rien
                        }else{
                            commands = commands + xi+" "+yi+" ";
                        }
                    }
                    if(bs.isClosed()==true){
                        commands = commands + "c ";
                    }
                    if(bs.isNextExist()==true){
                        int x = (int)bs.getNextPoint().getX();
                        int y = (int)bs.getNextPoint().getY();
                        int xi = x-(sh.getWidth()/scale)/2;
                        int yi = y-(sh.getHeight()/scale)/2;
                        commands = commands + "p "+xi+" "+yi+" ";
                    }
                }else if(s instanceof Move){
                    Move move = (Move)s;
                    int x = (int)move.getLastPoint().getX();
                    int y = (int)move.getLastPoint().getY();
                    int xb = x-(sh.getWidth()/scale)/2;
                    int yb = y-(sh.getHeight()/scale)/2;
                    commands = commands + "n "+xb+" "+yb+" ";
                }else if(s instanceof ReStart){
                    ReStart move = (ReStart)s;
                    int x = (int)move.getLastPoint().getX();
                    int y = (int)move.getLastPoint().getY();
                    int xb = x-(sh.getWidth()/scale)/2;
                    int yb = y-(sh.getHeight()/scale)/2;
                    commands = commands + "m "+xb+" "+yb+" ";
                }
            }
//            Point p = lay.getShapesList().getFirstPoint();
//            int x0 = (int)p.getOriginPoint().getX();
//            int y0 = (int)p.getOriginPoint().getY();
//            int xz = x0-(sh.getWidth()/scale)/2;
//            int yz = y0-(sh.getHeight()/scale)/2;
//            commands = "m "+xz+" "+yz+" " + commands;
            return commands;
        }catch(Exception exc){
            //exc.printStackTrace();
            return "";
        }
    }

    //Met à jour le dessin à partir des commandes ASS
    public static void shapesFromCommands(String com, Layer lay, int transX, int transY, java.awt.Point center, int angle){
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
                int xs = (sh.getWidth()/2)/scale+Integer.parseInt(mat.group(2))+transX;
                int ys = (sh.getWidth()/2)/scale+Integer.parseInt(mat.group(3))+transY;
                //System.out.println("+++ "+angle);
                if(angle!=0){
                    int xc = Integer.parseInt(mat.group(2));
                    int yc = Integer.parseInt(mat.group(3));
                    xs = (int)Math.round(xc*Math.cos(Math.toRadians(angle))+yc*Math.sin(Math.toRadians(angle)));
                    ys = (int)Math.round(-xc*Math.sin(Math.toRadians(angle))+yc*Math.cos(Math.toRadians(angle)));
                    xs = xs + (sh.getWidth()/2)/scale;
                    ys = ys + (sh.getWidth()/2)/scale;
                    
                }
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
        
        if(lay==null){lay = getCurrentLayer();}        
        lay.getShapesList().removeAllShapes();
        lay.clearRemembers(); dlmRemember.clear();
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
        
        updateRemember(lay);
        sh.updateGeneralPath(lay.getGeneralPath());
        sh.updateShapesList(lay.getShapesList());
        lay.setFirstPoint(p_init);
        lay.setLastPoint(p_init);
        

//        List<String> commands = new ArrayList<String>();
//        while (com.length() > 0){
//            if(com.startsWith("m") | com.startsWith("l") | com.startsWith("b")){
//                int index_of_line = com.substring(1).indexOf("l")+1;
//                int index_of_bezier = com.substring(1).indexOf("b")+1;
//                if((index_of_line!=0 && index_of_line<index_of_bezier) | (index_of_bezier==0 && index_of_bezier<index_of_line)){
//                    commands.add(com.substring(0, index_of_line-1));
//                    com = com.substring(index_of_line);
//                }else if((index_of_bezier!=0 && index_of_bezier<index_of_line) | (index_of_line==0 && index_of_line<index_of_bezier)){
//                    commands.add(com.substring(0, index_of_bezier-1));
//                    com = com.substring(index_of_bezier);
//                }else if(index_of_bezier==0 | index_of_line==0){
//                    if(com.endsWith(" ")){
//                        commands.add(com.substring(0,com.lastIndexOf(" ")));
//                    }else{
//                        commands.add(com.substring(0));
//                    }
//                    com = "";
//                }
//            }
//        }
//        if(lay==null){lay = getCurrentLayer();}        
//        lay.getShapesList().removeAllShapes();
//        java.awt.Point porigin, plast; porigin = null; plast = null;
//        for(String s : commands){
//            if(s.startsWith("m")){
//                String[] result = s.substring(2).split(" ");
//                porigin = new java.awt.Point(
//                        (sh.getWidth()/2)/scale+Integer.parseInt(result[0]),
//                        (sh.getHeight()/2)/scale+Integer.parseInt(result[1]));
//                Point point =
//                        new Point((int)porigin.getX(),(int)porigin.getY());
//                lay.getShapesList().addShape(point);
//            }else if (s.startsWith("l")){
//                String[] result = s.substring(2).split(" ");
//                plast = new java.awt.Point(
//                        (sh.getWidth()/2)/scale+Integer.parseInt(result[0]),
//                        (sh.getHeight()/2)/scale+Integer.parseInt(result[1]));
//                Point point =
//                        new Point((int)plast.getX(),(int)plast.getY());
//                lay.getShapesList().addShape(point);
//                Line line = new Line();
//                line.setOriginPoint((int)porigin.getX(),(int)porigin.getY());
//                line.setLastPoint((int)plast.getX(),(int)plast.getY());
//                lay.getShapesList().addShape(line);
//                porigin = plast;
//            }else if (s.startsWith("b")){
//                String[] result = s.substring(2).split(" ");
//                plast = new java.awt.Point(
//                        (sh.getWidth()/2)/scale+Integer.parseInt(result[4]),
//                        (sh.getHeight()/2)/scale+Integer.parseInt(result[5]));
//                java.awt.Point p1 = new java.awt.Point(
//                        (sh.getWidth()/2)/scale+Integer.parseInt(result[0]),
//                        (sh.getHeight()/2)/scale+Integer.parseInt(result[1]));
//                java.awt.Point p2 = new java.awt.Point(
//                        (sh.getWidth()/2)/scale+Integer.parseInt(result[2]),
//                        (sh.getHeight()/2)/scale+Integer.parseInt(result[3]));
//                Point point =
//                        new Point((int)plast.getX(),(int)plast.getY());
//                lay.getShapesList().addShape(point);
//                Bezier b =
//                        new Bezier((int)porigin.getX(),
//                            (int)porigin.getY(), (int)plast.getX(), (int)plast.getY());
//                ControlPoint cp1 =
//                        new ControlPoint((int)p1.getX(), (int)p1.getY());
//                b.setControl1(cp1);
//                ControlPoint cp2 =
//                        new ControlPoint((int)p2.getX(), (int)p2.getY());
//                b.setControl2(cp2);
//                lay.getShapesList().addShape(b);
//                lay.getShapesList().addShape(cp1);
//                lay.getShapesList().addShape(cp2);
//                porigin = plast;
//            }
//        }
//        sh.updateGeneralPath(lay.getGeneralPath());
//        sh.updateShapesList(lay.getShapesList());
//        lay.setFirstPoint(new java.awt.Point((int)porigin.getX(), (int)porigin.getY()));
//        lay.setLastPoint(new java.awt.Point((int)plast.getX(), (int)plast.getY()));
    }
    
    private void fixCommands(){
        //TODO : Comprendre pourquoi les commandes merdent et font que
        //on ne peut plus les modifier à la souris avant un shapesFromCommands
        tfAssCommands.setText(updateCommands());
        shapesFromCommands(getCommands(), getCurrentLayer(), 0, 0, null, 0);
    }
    
//    public String openDrawingFile(String path){
//        String commands = "";
//        File file = new File(path);
//        try{
//            FileInputStream fis = new FileInputStream(file);
//            java.io.BufferedReader br = new java.io.BufferedReader(
//                    new java.io.InputStreamReader(fis, "UTF-8"));
//            commands = br.readLine();
//            br.close(); fis.close();
//            lastDrawing = file.getAbsolutePath();
//            return commands;
//        }catch (Exception exc){
//            return "";
//        }
//    }

//    public void saveDrawingFile(String path, String commands){
//        File file = new File(path);
//        try{
//            FileOutputStream fos = new FileOutputStream(file);
//            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
//            PrintWriter pw = new PrintWriter(bw);
//            pw.print(commands);
//            pw.flush();
//            pw.close(); bw.close(); fos.close();
//            lastDrawing = file.getAbsolutePath();
//        }catch(Exception exc){
//
//        }
//    }
    
    public void openSVGFile(String path){
        try {
            XmlVectorHandler xvh = new XmlVectorHandler(path);
            VectorObject vro = xvh.getVectorObject();
                        
            //Layers
            int count = 0;
            dlm.clear();
            for(LayerContent lc : vro.getLayers()){
                Layer lay = new Layer();
                lay.setName(lc.getName());
                lay.setColor(lc.getColor());
                shapesFromCommands(lc.getAssCommands(),lay,0,0,null,0);
                if(count==0){
                    lay.setFirst(true);
                    lay.setSelected(false);
                    dlm.addElement(lay);
                    tfAssCommands.setText(lc.getAssCommands());
                }else{
                    lay.setSelected(false);
                    dlm.addElement(lay);
                    tfAssCommands.setText(lc.getAssCommands());
                }
                count += 1;
                lastDrawing = path;
                lstLayer.setSelectedIndex(count-1);
                Layer lastLayer = (Layer)lstLayer.getSelectedValue();
                lastLayer.setSelected(true);

                java.util.List<Layer> listlay = new ArrayList<Layer>();
                for(Object o : dlm.toArray()){
                    if( o instanceof Layer){
                        Layer lay2 = (Layer)o;
                        if(listlay.contains(lay2)==false){
                            listlay.add(lay2);
                            sh.updateShapesList(lay2.getShapesList());
                        }
                    }
                }
                sh.setLayerList(listlay);
            }
            
        } catch (Exception ex) {
        }
    }
    
    public void openDrawingFile(String path){
        String line;
        File file = new File(path);
        try{
            FileInputStream fis = new FileInputStream(file);
            java.io.BufferedReader br = new java.io.BufferedReader(
                    new java.io.InputStreamReader(fis, "UTF-8"));
            dlm.clear();
            int count = 0;
            while((line = br.readLine())!=null){
                String[] fragments = line.split(";");
                Layer lay = new Layer();
                lay.setName(fragments[0]);
                String[] colors = fragments[1].split(",");
                lay.setColor(new Color(Integer.parseInt(colors[0]),
                        Integer.parseInt(colors[1]),
                        Integer.parseInt(colors[2])));
                shapesFromCommands(fragments[2],lay,0,0,null,0);
                if(count==0){
                    lay.setFirst(true);
                    lay.setSelected(false);
                    dlm.addElement(lay);                    
                }else{
                    lay.setSelected(false);
                    dlm.addElement(lay);
                    tfAssCommands.setText(fragments[2]);
                }                
                count += 1;                
            }
            br.close(); fis.close();
            lastDrawing = file.getAbsolutePath();
            lstLayer.setSelectedIndex(count-1);
            Layer lastLayer = (Layer)lstLayer.getSelectedValue();
            lastLayer.setSelected(true);
            
            java.util.List<Layer> listlay = new ArrayList<Layer>();
            for(Object o : dlm.toArray()){
                if( o instanceof Layer){
                    Layer lay = (Layer)o;
                    if(listlay.contains(lay)==false){
                        listlay.add(lay);
                        sh.updateShapesList(lay.getShapesList());
                    }
                }
            }
            sh.setLayerList(listlay);
        }catch (Exception exc){
        }
    }
    
    public void saveDrawingFile(String path){
        File file = new File(path);
        try{
            FileOutputStream fos = new FileOutputStream(file);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
            PrintWriter pw = new PrintWriter(bw);
            int count = 0;
            for(Object o : dlm.toArray()){
                if(o instanceof Layer){
                    Layer lay = (Layer)o;
                    String name = lay.getName();
                    if(name.isEmpty()){name="ID "+count;}
                    pw.println(
                            name+";"+
                            lay.getColor().getRed()+","+
                            lay.getColor().getGreen()+","+
                            lay.getColor().getBlue()+";"+
                            commandsFromShapes(lay));
                    pw.flush();
                    count += 1;
                }                
            }            
            pw.close(); bw.close(); fos.close();
            lastDrawing = file.getAbsolutePath();
        }catch(Exception exc){

        }
    }
    
    public void openDrawingFile2(String path){
        try {
            XmlDrawingHandler xdh = new XmlDrawingHandler(path);
            DrawingObject dro = xdh.getDrawingObject();
                        
            //Layers
            int count = 0;
            dlm.clear();
            for(LayerContent lc : dro.getLayers()){
                Layer lay = new Layer();
                lay.setName(lc.getName());
                lay.setColor(lc.getColor());
                shapesFromCommands(lc.getAssCommands(),lay,0,0,null,0);
                if(count==0){
                    lay.setFirst(true);
                    lay.setSelected(false);
                    dlm.addElement(lay);
                    tfAssCommands.setText(lc.getAssCommands());
                }else{
                    lay.setSelected(false);
                    dlm.addElement(lay);
                    tfAssCommands.setText(lc.getAssCommands());
                }
                count += 1;
                lastDrawing = path;
                lstLayer.setSelectedIndex(count-1);
                Layer lastLayer = (Layer)lstLayer.getSelectedValue();
                lastLayer.setSelected(true);

                java.util.List<Layer> listlay = new ArrayList<Layer>();
                for(Object o : dlm.toArray()){
                    if( o instanceof Layer){
                        Layer lay2 = (Layer)o;
                        if(listlay.contains(lay2)==false){
                            listlay.add(lay2);
                            sh.updateShapesList(lay2.getShapesList());
                        }
                    }
                }
                sh.setLayerList(listlay);
            }
            
            //Image
            imgDraft = dro.getImage();
            graDraft = (Graphics2D)imgDraft.getGraphics();
            graDraft.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            sh.updateDraft(imgDraft);
            
            //Photo
            img = dro.getIcon();
            sh.updateImage(img);
            sh.updateImageRealPosition(dro.getIconPositionX(), dro.getIconPositionY());
            
            sh.updateDrawing();
        } catch (Exception ex) {
        }
    }
    
    public void saveDrawingFile2(String path){
        DrawingObject dro = new DrawingObject();
        
        for(Object o : dlm.toArray()){
            if(o instanceof Layer){
                Layer lay = (Layer)o;
                dro.addLayer(lay.getName(), lay.getColor(), commandsFromShapes(lay));                
            }                
        }
        
        dro.setImage(imgDraft);
        
        dro.setIcon(img);
        dro.setIconPositionX(sh.getImagePositionX());
        dro.setIconPositionY(sh.getImagePositionY());
        
        XmlDrawingWriter xdw = new XmlDrawingWriter();
        xdw.setDrawingObject(dro);
        xdw.createDrawing(path);
    }
    
    public String[] getAssCommandsOfFile(String path){
        try{
            XmlDrawingHandler xdh = new XmlDrawingHandler(path);
            DrawingObject dro = xdh.getDrawingObject();
            String[] table = new String[dro.getLayers().size()];
            int count = 0;
            for(LayerContent lc : dro.getLayers()){
                table[count] = lc.getAssCommands();
                count += 1;
            }
            return table;
        }catch(Exception ex){
            return null;
        }
    }
    
     /** Get commands from the text area. */
    public String getCommands(){
        return tfAssCommands.getText();
    }

    /** Get the path of the last drawing. */
    public String getLastDrawing(){
        return lastDrawing;
    }
    
    /** Open the drawing specified by the path. Open ADF Files only. */
    public void openDrawing(String path){
        if(path.endsWith(".adf")){
//            openDrawingFile(path);
            openDrawingFile2(path);
//            try {
//                shapesFromCommands(commands,null);
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
        }        
    }
    
    /** Get the path of the last image. */
    public String getLastImage(){
        return lastImage;
    }

    /** Open an image in the drawing area. */
    public void openImage(String path){
        img = new ImageIcon(path);
        sh.updateImage(img);
        sh.updateDrawing();
    }

    /** Get the value of the image opacity. */
    public int getImageOpacity(){
        return slideImageAlpha.getValue();
    }

    /** Set the value of the image opacity. */
    public void setImageOpacity(int value){
        slideImageAlpha.setValue(value);
    }

    /** Get the value of the shape opacity. */
    public int getShapeOpacity(){
        return slideDrawingAlpha.getValue();
    }

    /** Set the value of the shape opacity. */
    public void setShapeOpacity(int value){
        slideDrawingAlpha.setValue(value);
    }

    /** Get the value of the scale. */
    public int getScale(){
        return slideScale.getValue();
    }

    /** Set the value of the scale. */
    public void setScale(int value){
        slideScale.setValue(value);
    }
    
    /** Set the width of the text area which contains commands */
    public void setCommandsWidth(int width){
        tfAssCommands.setSize(width, tfAssCommands.getHeight());
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
    
    /** Obtient les commandes pour l'ornement à partir de la couche sélectionnée. 
     * @param lay La couche sélectionnée.
     * @param xt Le décalage sur x.
     * @param yt Le décalage sur y.
     * @return La commande de l'ornement.
     */
    public String getOrnamentCommands(Layer lay, int xt, int yt){
        //On obtient la position de l'image (valeur à toujours soustraire)
        int posX = sh.getImagePositionX() + xt;
        int posY = sh.getImagePositionY() + yt;
        String commands = "";
        try{
            for(IShape s : lay.getShapesList().getShapes()){
                if(s instanceof Line){
                    Line line = (Line)s;
                    int x = (int)line.getLastPoint().getX();
                    int y = (int)line.getLastPoint().getY();
                    int xb = x-posX;
                    int yb = y-posY;
                    commands = commands + "l "+xb+" "+yb+" ";
                }else if(s instanceof Bezier){
                    Bezier bezier = (Bezier)s;
                    int x1 = (int)bezier.getControl1().getOriginPoint().getX();
                    int y1 = (int)bezier.getControl1().getOriginPoint().getY();
                    int x2 = (int)bezier.getControl2().getOriginPoint().getX();
                    int y2 = (int)bezier.getControl2().getOriginPoint().getY();
                    int x3 = (int)bezier.getLastPoint().getX();
                    int y3 = (int)bezier.getLastPoint().getY();
                    int xe = x1-posX;
                    int ye = y1-posY;
                    int xf = x2-posX;
                    int yf = y2-posY;
                    int xg = x3-posX;
                    int yg = y3-posY;
                    commands = commands + "b "+xe+" "+ye+" "+xf+" "+yf+" "+xg+" "+yg+" ";
                }else if(s instanceof BSpline){
                    BSpline bs = (BSpline)s;
                    List<ControlPoint> lcp = bs.getControlPoints();
                    int lastcp = lcp.size()-1;
                    commands = commands + "s ";
                    for(ControlPoint cp : lcp){
                        int x = (int)cp.getOriginPoint().getX();
                        int y = (int)cp.getOriginPoint().getY();
                        int xi = x-posX;
                        int yi = y-posY;
                        if(bs.isNextExist()==true && cp.equals(lcp.get(lastcp))==true){
                            //rien
                        }else{
                            commands = commands + xi+" "+yi+" ";
                        }
                    }
                    if(bs.isClosed()==true){
                        commands = commands + "c ";
                    }
                    if(bs.isNextExist()==true){
                        int x = (int)bs.getNextPoint().getX();
                        int y = (int)bs.getNextPoint().getY();
                        int xi = x-posX;
                        int yi = y-posY;
                        commands = commands + "p "+xi+" "+yi+" ";
                    }
                }else if(s instanceof Move){
                    Move move = (Move)s;
                    int x = (int)move.getLastPoint().getX();
                    int y = (int)move.getLastPoint().getY();
                    int xb = x-posX;
                    int yb = y-posY;
                    commands = commands + "n "+xb+" "+yb+" ";
                }else if(s instanceof ReStart){
                    ReStart move = (ReStart)s;
                    int x = (int)move.getLastPoint().getX();
                    int y = (int)move.getLastPoint().getY();
                    int xb = x-posX;
                    int yb = y-posY;
                    commands = commands + "m "+xb+" "+yb+" ";
                }
            }
            return commands;
        }catch(Exception exc){
            return "";
        }
    }
    
    // <editor-fold defaultstate="collapsed" desc=" Communication avec l'extérieur ">
    
    public static String updateCommands(){
        String commands = "";
        Layer lay = getCurrentLayer();
        try{
            for(IShape s : lay.getShapesList().getShapes()){
                if(s instanceof Line){
                    Line line = (Line)s;
                    int x = (int)line.getLastPoint().getX();
                    int y = (int)line.getLastPoint().getY();
                    int xb = x-(sh.getWidth()/scale)/2;
                    int yb = y-(sh.getHeight()/scale)/2;
                    commands = commands + "l "+xb+" "+yb+" ";
                }else if(s instanceof Bezier){
                    Bezier bezier = (Bezier)s;
                    int x1 = (int)bezier.getControl1().getOriginPoint().getX();
                    int y1 = (int)bezier.getControl1().getOriginPoint().getY();
                    int x2 = (int)bezier.getControl2().getOriginPoint().getX();
                    int y2 = (int)bezier.getControl2().getOriginPoint().getY();
                    int x3 = (int)bezier.getLastPoint().getX();
                    int y3 = (int)bezier.getLastPoint().getY();
                    int xe = x1-(sh.getWidth()/scale)/2;
                    int ye = y1-(sh.getHeight()/scale)/2;
                    int xf = x2-(sh.getWidth()/scale)/2;
                    int yf = y2-(sh.getHeight()/scale)/2;
                    int xg = x3-(sh.getWidth()/scale)/2;
                    int yg = y3-(sh.getHeight()/scale)/2;
                    commands = commands + "b "+xe+" "+ye+" "+xf+" "+yf+" "+xg+" "+yg+" ";
                }else if(s instanceof BSpline){
                    BSpline bs = (BSpline)s;
                    List<ControlPoint> lcp = bs.getControlPoints();
                    int lastcp = lcp.size()-1;
                    commands = commands + "s ";
                    for(ControlPoint cp : lcp){
                        int x = (int)cp.getOriginPoint().getX();
                        int y = (int)cp.getOriginPoint().getY();
                        int xi = x-(sh.getWidth()/scale)/2;
                        int yi = y-(sh.getHeight()/scale)/2;
                        if(bs.isNextExist()==true && cp.equals(lcp.get(lastcp))==true){
                            //rien
                        }else{
                            commands = commands + xi+" "+yi+" ";
                        }
                    }
                    if(bs.isClosed()==true){
                        commands = commands + "c ";
                    }
                    if(bs.isNextExist()==true){
                        int x = (int)bs.getNextPoint().getX();
                        int y = (int)bs.getNextPoint().getY();
                        int xi = x-(sh.getWidth()/scale)/2;
                        int yi = y-(sh.getHeight()/scale)/2;
                        commands = commands + "p "+xi+" "+yi+" ";
                    }
                }else if(s instanceof Move){
                    Move move = (Move)s;
                    int x = (int)move.getLastPoint().getX();
                    int y = (int)move.getLastPoint().getY();
                    int xb = x-(sh.getWidth()/scale)/2;
                    int yb = y-(sh.getHeight()/scale)/2;
                    commands = commands + "n "+xb+" "+yb+" ";
                }else if(s instanceof ReStart){
                    ReStart move = (ReStart)s;
                    int x = (int)move.getLastPoint().getX();
                    int y = (int)move.getLastPoint().getY();
                    int xb = x-(sh.getWidth()/scale)/2;
                    int yb = y-(sh.getHeight()/scale)/2;
                    commands = commands + "m "+xb+" "+yb+" ";
                }
            }
            return commands;
        }catch(Exception exc){
            return null;
        }        
    }
    
    public static void updateRemember(Layer lay){
        if(lay==null){
            lay = getCurrentLayer();
        }
        dlmRemember.clear();
            for(Remember re : lay.getRememberlist(true)){
                if(dlmRemember.contains(re)==false){
                    dlmRemember.addElement(re);
                }            
            }
    }
    
    public static Layer getCurrentLayer(){
        for(Object o : dlm.toArray()){
            if(o instanceof Layer){
                Layer lay = (Layer)o;
                if(lay.isSelected()==true){
                    return lay;
                }
            }
        }
        return null;
    }
    
    /** Renvoie une liste des couches liées par la notation Couche X Ln.
     * Renvoir toutes les couches portant la notion Ln. par rapport à la couche
     * en cours. */
    public List<Layer> getLinkedLayers(){
        List<Layer> layers = new ArrayList<Layer>();
        Layer real_lay = getCurrentLayer();
        try{
            int groupNumber = Integer.parseInt(real_lay.getName()
                    .substring(real_lay.getName().lastIndexOf("L")+1));
            for(Object o : dlm.toArray()){
                Layer lay = (Layer)o;
                if(lay.getName().contains(("L"+groupNumber))){
                    layers.add(lay);
                }
            }
            return layers;
        }catch(Exception e){
            return layers;
        }            
    }
    
    public static void setAssCommands(){
        tfAssCommands.setText(updateCommands());
    }
    
    public static Sheet getSheet(){
        return sh;
    }
    
    public static boolean changeLayer(int id){
        try{
            lstLayer.setSelectedIndex(id);
            return true;
        }catch(Exception e){
            return false;
        }
    }
    
    public static int createLayer(String name, int r, int g, int b){
        Color c;
        try{
            c = new Color(r,g,b);
        }catch(Exception e){
            c = Color.green;
        }
        if(name==null | name.equals("")){
            name = "ID "+dlm.toArray().length;
        }
        
        Layer lay = new Layer();
        lay.setName(name);
        lay.setColor(c);
        dlm.addElement(lay);
        lstLayer.setSelectedValue(lay, true);
        java.util.List<Layer> listlay = new ArrayList<Layer>();
        for(Object o : dlm.toArray()){
            if( o instanceof Layer){
                Layer layer = (Layer)o;
                if(listlay.contains(layer)==false){
                    listlay.add(layer);
                    sh.updateShapesList(layer.getShapesList());
                }
            }
        }
        sh.setLayerList(listlay);
        sh.updateDrawing();
        
        return dlm.getSize()-1;
    }
    
    public static void updateLayerList(){
        lstLayer.updateUI();
    }
    
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" Langage ">
    private List<File> listFiles = new ArrayList<File>();
    
    /** <p>A choice of countries.<br />Un choix de pays.</p> */
    public enum ISO_3166{
        Afghanistan("AF","AFG","Afghanistan"),
        Albania("AL","ALB","Albania"),
        Algeria("DZ","DZA","Algeria"),
        American_Samoa("AS","ASM","American Samoa"),
        Andorra("AD","AND","Andorra"),
        Angola("AO","AGO","Angola"),
        Anguilla("AI","AIA","Anguilla"),
        Antarctica("AQ","ATA","Antarctica"),
        Antigua_and_Barbuda("AG","ATG","Antigua and Barbuda"),
        Argentina("AR","ARG","Argentina"),
        Armenia("AM","ARM","Armenia"),
        Aruba("AW","ABW","Aruba"),
        Australia("AU","AUS","Australia"),
        Austria("AT","AUT","Austria"),
        Azerbaijan("AZ","AZE","Azerbaijan"),
        Bahamas("BS","BHS","Bahamas"),
        Bahrain("BH","BHR","Bahrain"),
        Bangladesh("BD","BGD","Bangladesh"),
        Barbados("BB","BRB","Barbados"),
        Belarus("BY","BLR","Belarus"),
        Belgium("BE","BEL","Belgium"),
        Belize("BZ","BLZ","Belize"),
        Benin("BJ","BEN","Benin"),
        Bermuda("BM","BMU","Bermuda"),
        Bhutan("BT","BTN","Bhutan"),
        Bolivia("BO","BOL","Bolivia"),
        Bosnia_and_Herzegovina("BA","BIH","Bosnia and Herzegovina"),
        Botswana("BW","BWA","Botswana"),
        Bouvet_Island("BV","BVT","Bouvet Island"),
        Brazil("BR","BRA","Brazil"),
        British_Indian_Ocean_Territory("IO","IOT","British Indian Ocean Territory"),
        British_Virgin_Islands("VG","VGB","British Virgin Islands"),
        Brunei_Darussalam("BN","BRN","Brunei Darussalam"),
        Bulgaria("BG","BGR","Bulgaria"),
        Burkina_Faso("BF","BFA","Burkina Faso"),
        Burundi("BI","BDI","Burundi"),
        Cambodia("KH","KHM","Cambodia"),
        Cameroon("CM","CMR","Cameroon"),
        Canada("CA","CAN","Canada"),
        Cape_Verde("CV","CPV","Cape Verde"),
        Cayman_Islands("KY", "CYM", "Cayman Islands"),
        Central_African_Republic("CF", "CAF", "Central African Republic"),
        Chad("TD", "TCD", "Chad"),
        Chile("CL", "CHL", "Chile"),
        China("CN", "CHN", "China"),
        Christmas_Island("CX", "CXR", "Christmas Island"),
        Cocos_Islands("CC", "CCK", "Cocos (Keeling), Islands"),
        Colombia("CO", "COL", "Colombia"),
        Comoros("KM", "COM", "Comoros"),
        Congo1("CD", "COD", "Congo"),
        Congo2("CG", "COG", "Congo"),
        Cook_Islands("CK", "COK", "Cook Islands"),
        Costa_Rica("CR", "CRI", "Costa Rica"),
        Cote_DIvoire("CI", "CIV", "Cote D'Ivoire"),
        Cuba("CU", "CUB", "Cuba"),
        Cyprus("CY", "CYP", "Cyprus"),
        Czech("CZ", "CZE", "Czech"),
        Denmark("DK", "DNK", "Denmark"),
        Djibouti("DJ", "DJI", "Djibouti"),
        Dominica("DM", "DMA", "Dominica"),
        Dominican("DO", "DOM", "Dominican"),
        Ecuador("EC", "ECU", "Ecuador"),
        Egypt("EG", "EGY", "Egypt"),
        El_Salvador("SV", "SLV", "El Salvador"),
        Equatorial_Guinea("GQ", "GNQ", "Equatorial Guinea"),
        Eritrea("ER", "ERI", "Eritrea"),
        Estonia("EE", "EST", "Estonia"),
        Ethiopia("ET", "ETH", "Ethiopia"),
        Faeroe_Islands("FO", "FRO", "Faeroe Islands"),
        Falkland_Islands("FK", "FLK", "Falkland Islands (Malvinas),"),
        Fiji("FJ", "FJI", "Fiji"),
        Finland("FI", "FIN", "Finland"),
        France("FR", "FRA", "France"),
        French_Guiana("GF", "GUF", "French Guiana"),
        French_Polynesia("PF", "PYF", "French Polynesia"),
        French_Southern_Territories("TF", "ATF", "French Southern Territories"),
        Gabon("GA", "GAB", "Gabon"),
        Gambia("GM", "GMB", "Gambia"),
        Georgia("GE", "GEO", "Georgia"),
        Germany("DE", "DEU", "Germany"),
        Ghana("GH", "GHA", "Ghana"),
        Gibraltar("GI", "GIB", "Gibraltar"),
        Greece("GR", "GRC", "Greece"),
        Greenland("GL", "GRL", "Greenland"),
        Grenada("GD", "GRD", "Grenada"),
        Guadaloupe("GP", "GLP", "Guadaloupe"),
        Guam("GU", "GUM", "Guam"),
        Guatemala("GT", "GTM", "Guatemala"),
        Guinea("GN", "GIN", "Guinea"),
        Guinea_Bissau("GW", "GNB", "Guinea-Bissau"),
        Guyana("GY", "GUY", "Guyana"),
        Haiti("HT", "HTI", "Haiti"),
        Heard_and_McDonald_Islands("HM", "HMD", "Heard and McDonald Islands"),
        Holy_See("VA", "VAT", "Holy See (Vatican City State),"),
        Honduras("HN", "HND", "Honduras"),
        Hong_Kong("HK", "HKG", "Hong Kong"),
        Hrvatska("HR", "HRV", "Hrvatska (Croatia),"),
        Hungary("HU", "HUN", "Hungary"),
        Iceland("IS", "ISL", "Iceland"),
        India("IN", "IND", "India"),
        Indonesia("ID", "IDN", "Indonesia"),
        Iran("IR", "IRN", "Iran"),
        Iraq("IQ", "IRQ", "Iraq"),
        Ireland("IE", "IRL", "Ireland"),
        Israel("IL", "ISR", "Israel"),
        Italy("IT", "ITA", "Italy"),
        Jamaica("JM", "JAM", "Jamaica"),
        Japan("JP", "JPN", "Japan"),
        Jordan("JO", "JOR", "Jordan"),
        Kazakhstan("KZ", "KAZ", "Kazakhstan"),
        Kenya("KE", "KEN", "Kenya"),
        Kiribati("KI", "KIR", "Kiribati"),
        Korea1("KP", "PRK", "Korea"),
        Korea2("KR", "KOR", "Korea"),
        Kuwait("KW", "KWT", "Kuwait"),
        Kyrgyz_Republic("KG", "KGZ", "Kyrgyz Republic"),
        Lao_Peoples_Democratic_Republic("LA", "LAO", "Lao People's Democratic Republic"),
        Latvia("LV", "LVA", "Latvia"),
        Lebanon("LB", "LBN", "Lebanon"),
        Lesotho("LS", "LSO", "Lesotho"),
        Liberia("LR", "LBR", "Liberia"),
        Libyan("LY", "LBY", "Libyan"),
        Liechtenstein("LI", "LIE", "Liechtenstein"),
        Lithuania("LT", "LTU", "Lithuania"),
        Luxembourg("LU", "LUX", "Luxembourg"),
        Macao("MO", "MAC", "Macao"),
        Macedonia("MK", "MKD", "Macedonia"),
        Madagascar("MG", "MDG", "Madagascar"),
        Malawi("MW", "MWI", "Malawi"),
        Malaysia("MY", "MYS", "Malaysia"),
        Maldives("MV", "MDV", "Maldives"),
        Mali("ML", "MLI", "Mali"),
        Malta("MT", "MLT", "Malta"),
        Marshall_Islands("MH", "MHL", "Marshall Islands"),
        Martinique("MQ", "MTQ", "Martinique"),
        Mauritania("MR", "MRT", "Mauritania"),
        Mauritius("MU", "MUS", "Mauritius"),
        Mayotte("YT", "MYT", "Mayotte"),
        Mexico("MX", "MEX", "Mexico"),
        Micronesia("FM", "FSM", "Micronesia"),
        Moldova("MD", "MDA", "Moldova"),
        Monaco("MC", "MCO", "Monaco"),
        Mongolia("MN", "MNG", "Mongolia"),
        Montserrat("MS", "MSR", "Montserrat"),
        Morocco("MA", "MAR", "Morocco"),
        Mozambique("MZ", "MOZ", "Mozambique"),
        Myanmar("MM", "MMR", "Myanmar"),
        Namibia("NA", "NAM", "Namibia"),
        Nauru("NR", "NRU", "Nauru"),
        Nepal("NP", "NPL", "Nepal"),
        Netherlands_Antilles("AN", "ANT", "Netherlands Antilles"),
        Netherlands("NL", "NLD", "Netherlands"),
        New_Caledonia("NC", "NCL", "New Caledonia"),
        New_Zealand("NZ", "NZL", "New Zealand"),
        Nicaragua("NI", "NIC", "Nicaragua"),
        Niger("NE", "NER", "Niger"),
        Nigeria("NG", "NGA", "Nigeria"),
        Niue("NU", "NIU", "Niue"),
        Norfolk_Island("NF", "NFK", "Norfolk Island"),
        Northern_Mariana_Islands("MP", "MNP", "Northern Mariana Islands"),
        Norway("NO", "NOR", "Norway"),
        Oman("OM", "OMN", "Oman"),
        Pakistan("PK", "PAK", "Pakistan"),
        Palau("PW", "PLW", "Palau"),
        Palestinian_Territory("PS", "PSE", "Palestinian Territory"),
        Panama("PA", "PAN", "Panama"),
        Papua_New_Guinea("PG", "PNG", "Papua New Guinea"),
        Paraguay("PY", "PRY", "Paraguay"),
        Peru("PE", "PER", "Peru"),
        Philippines("PH", "PHL", "Philippines"),
        Pitcairn_Island("PN", "PCN", "Pitcairn Island"),
        Poland("PL", "POL", "Poland"),
        Portugal("PT", "PRT", "Portugal"),
        Puerto_Rico("PR", "PRI", "Puerto Rico"),
        Qatar("QA", "QAT", "Qatar"),
        Reunion("RE", "REU", "Reunion"),
        Romania("RO", "ROU", "Romania"),
        Russian_Federation("RU", "RUS", "Russian Federation"),
        Rwanda("RW", "RWA", "Rwanda"),
        St__Helena("SH", "SHN", "St. Helena"),
        St__Kitts_and_Nevis("KN", "KNA", "St. Kitts and Nevis"),
        St__Lucia("LC", "LCA", "St. Lucia"),
        St__Pierre_and_Miquelon("PM", "SPM", "St. Pierre and Miquelon"),
        St__Vincent_and_the_Grenadines("VC", "VCT", "St. Vincent and the Grenadines"),
        Samoa("WS", "WSM", "Samoa"),
        San_Marino("SM", "SMR", "San Marino"),
        Sao_Tome_and_Principe("ST", "STP", "Sao Tome and Principe"),
        Saudi_Arabia("SA", "SAU", "Saudi Arabia"),
        Senegal("SN", "SEN", "Senegal"),
        Serbia_and_Montenegro("CS", "SCG", "Serbia and Montenegro"),
        Seychelles("SC", "SYC", "Seychelles"),
        Sierra_Leone("SL", "SLE", "Sierra Leone"),
        Singapore("SG", "SGP", "Singapore"),
        Slovakia("SK", "SVK", "Slovakia (Slovak Republic),"),
        Slovenia("SI", "SVN", "Slovenia"),
        Solomon_Islands("SB", "SLB", "Solomon Islands"),
        Somalia("SO", "SOM", "Somalia"),
        South_Africa("ZA", "ZAF", "South Africa"),
        South_Georgia_and_the_South_Sandwich_Islands("GS", "SGS", "South Georgia and the South Sandwich Islands"),
        Spain("ES", "ESP", "Spain"),
        Sri_Lanka("LK", "LKA", "Sri Lanka"),
        Sudan("SD", "SDN", "Sudan"),
        Suriname("SR", "SUR", "Suriname"),
        Svalbard___Jan_Mayen_Islands("SJ", "SJM", "Svalbard & Jan Mayen Islands"),
        Swaziland("SZ", "SWZ", "Swaziland"),
        Sweden("SE", "SWE", "Sweden"),
        Switzerland("CH", "CHE", "Switzerland"),
        Syrian_Arab_Republic("SY", "SYR", "Syrian Arab Republic"),
        Taiwan("TW", "TWN", "Taiwan"),
        Tajikistan("TJ", "TJK", "Tajikistan"),
        Tanzania("TZ", "TZA", "Tanzania"),
        Thailand("TH", "THA", "Thailand"),
        Timor_Leste("TL", "TLS", "Timor-Leste"),
        Togo("TG", "TGO", "Togo"),
        Tokelau("TK", "TKL", "Tokelau (Tokelau Islands),"),
        Tonga("TO", "TON", "Tonga"),
        Trinidad_and_Tobago("TT", "TTO", "Trinidad and Tobago"),
        Tunisia("TN", "TUN", "Tunisia"),
        Turkey("TR", "TUR", "Turkey"),
        Turkmenistan("TM", "TKM", "Turkmenistan"),
        Turks_and_Caicos_Islands("TC", "TCA", "Turks and Caicos Islands"),
        Tuvalu("TV", "TUV", "Tuvalu"),
        US_Virgin_Islands("VI", "VIR", "US Virgin Islands"),
        Uganda("UG", "UGA", "Uganda"),
        Ukraine("UA", "UKR", "Ukraine"),
        United_Arab_Emirates("AE", "ARE", "United Arab Emirates"),
        United_Kingdom_of_Great_Britain___N__Ireland("GB", "GBR", "United Kingdom of Great Britain & N. Ireland"),
        United_States_Minor_Outlying_Islands("UM", "UMI", "United States Minor Outlying Islands"),
        United_States_of_America("US", "USA", "United States of America"),
        Uruguay("UY", "URY", "Uruguay"),
        Uzbekistan("UZ", "UZB", "Uzbekistan"),
        Vanuatu("VU", "VUT", "Vanuatu"),
        Venezuela("VE", "VEN", "Venezuela"),
        Viet_Nam("VN", "VNM", "Viet Nam"),
        Wallis_and_Futuna_Islands("WF", "WLF", "Wallis and Futuna Islands"),
        Western_Sahara("EH", "ESH", "Western Sahara"),
        Yemen("YE", "YEM", "Yemen"),
        Zambia("ZM", "ZMB", "Zambia"),
        Zimbabwe("ZW", "ZWE", "Zimbabwe"),
        British_Antarctic_Territory("BQ", "ATB", "British Antarctic Territory"),
        Burma("BU", "BUR", "Burma"),
        Byelorussian("BY", "BYS", "Byelorussian"),
        Canton___Enderbury_Islands("CT", "CTE", "Canton & Enderbury Islands"),
        Czechoslovakia("CS", "CSK", "Czechoslovakia"),
        Dahomey("DY", "DHY", "Dahomey"),
        Dronning_Maud_Land("NQ", "ATN", "Dronning Maud Land"),
        East_Timor("TP", "TMP", "East Timor"),
        Ethiopia2("ET", "ETH", "Ethiopia"),
        France2("FX", "FXX", "France"),
        French_fars_and_Issas("AI", "AFI", "French fars and Issas"),
        French_Southern_and_Antarctic_Territories("FQ", "ATF", "French Southern and Antarctic Territories"),
        German_Democratic_Republic("DD", "DDR", "German Democratic Republic"),
        Germany2("DE", "DEU", "Germany"),
        Gilbert___Ellice_Islands("GE", "GEL", "Gilbert & Ellice Islands"),
        Johnston_Island("JT", "JTN", "Johnston Island"),
        Midway_Islands("MI", "MID", "Midway Islands"),
        Netherlands_Antilles2("AN", "ANT", "Netherlands Antilles"),
        Neutral_Zone("NT", "NTZ", "Neutral Zone"),
        New_Hebrides("NH", "NHB", "New Hebrides"),
        Pacific_Islands("PC", "PCI", "Pacific Islands"),
        Panama2("PA", "PAN", "Panama"),
        Panama_Canal_Zone("PZ", "PCZ", "Panama Canal Zone"),
        Romania2("RO", "ROM", "Romania"),
        St__Kitts_Nevis_Anguilla("KN", "KNA", "St. Kitts-Nevis-Anguilla"),
        Sikkim("SK", "SKM", "Sikkim"),
        Southern_Rhodesia("RH", "RHO", "Southern Rhodesia"),
        Spanish_Sahara("EH", "ESH", "Spanish Sahara"),
        US_Miscellaneous_Pacific_Islands("PU", "PUS", "US Miscellaneous Pacific Islands"),
        USSR("SU", "SUN", "USSR"),
        Upper_Volta("HV", "HVO", "Upper Volta"),
        Vatican_City_State("VA", "VAT", "Vatican City State (Holy See)"),
        Viet_Nam2("VD", "VDR", "Viet-Nam"),
        Wake_Island("WK", "WAK", "Wake Island"),
        Yemen1("YD", "YMD", "Yemen"),
        Yemen2("YE", "YEM", "Yemen"),
        Yugoslavia1("YU", "YUG", "Yugoslavia"),
        Yugoslavia2("YU", "YUG", "Yugoslavia"),
        Zaire("ZR", "ZAR", "Zaire"),
        Unknown("XX", "XXX", "Unknown");
        
        private String alpha_2;
        private String alpha_3;
        private String name;

        ISO_3166(String alpha_2, String alpha_3, String name){
            this.alpha_2 = alpha_2;
            this.alpha_3 = alpha_3;
            this.name = name;
        }
        
        public String getAlpha2(){
            return alpha_2;
        }
        
        public String getAlpha3(){
            return alpha_3;
        }
        
        public String getCountry(){
            return name;
        }
        
        /** Find a value of ISO_3166 by searching for the alpha 2 code or 
         * the alpha 3 code or the name of the country. The name of the 
         * countries are in English only. */
        public ISO_3166 getISO_3166(String search){
            ISO_3166 iso = ISO_3166.Unknown;
            for(ISO_3166 x : ISO_3166.values()){
                if(search.equalsIgnoreCase(x.getAlpha2())){
                    iso = x;
                }
                if(search.equalsIgnoreCase(x.getAlpha3())){
                    iso = x;
                }
                if(search.equalsIgnoreCase(x.getCountry())){
                    iso = x;
                }
            }
            return iso;
        }
    }
    
    public enum Language{
        File, Quit, About, To_Draw, Image, Shape, Scale, Operations, Layers,
        Historic, Sketchpad, Ass_Commands, Cut, Copy, Paste, Delete, Choose_Color,
        Rename_Layer, Clear_Sketch, Rubber_Size, Pen_Size, By_Value, New, Open,
        Save, Line, Curve, Pen, Rubber, Clear_Image, Open_Image, Top, Left, 
        Center, Right, Transparency_Image, Bottom, Transparency_Shape,
        Scale_Size, Translate, Rotate, Add_Layer, Remove_Layer, Layers_List,
        Undo, Pen_Color, OK_Button, Cancel_Button, TransX, TransY, RedRotateMessage,
        RotateMessage, UpdateFromComs, BSpline, BSplineExtend,BSplineClose, GoToA,
        GoToB, Duplicate;
    }
    
    public void setLanguage(Language l, String translation){
        switch(l){
            case File : 
//                jMenu1.setText(translation); 
                ifrOpenSave.setTitle(translation); break;
//            case Quit : mnuQuit.setText(translation); break;
//            case About : mnuAbout.setText(translation); break;
            case To_Draw : ifrMainDraw.setTitle(translation); break;
            case Image : ifrImage.setTitle(translation); break;
            case Shape : ifrShape.setTitle(translation); break;
//            case Scale : ifrScale.setTitle(translation); break;
            case Operations : ifrOperations.setTitle(translation); break;
            case Layers : ifrLayers.setTitle(translation); break;
            case Historic : ifrHistoric.setTitle(translation); break;
            case Sketchpad : ifrDrawing.setTitle(translation); break;
            case Ass_Commands : lblAssCommands.setText(translation); break;
            case Cut : popmCut.setText(translation); break;
            case Copy : popmCopy.setText(translation); break;
            case Paste : popmPaste.setText(translation); break;
            case Delete : popmDelete.setText(translation); break;
            case Choose_Color : 
                popmColor.setText(translation); 
                popmLayerColor.setText(translation); break;
            case Rename_Layer : 
                popmName.setText(translation); 
                popmLayerName.setText(translation); break;
            case Clear_Sketch : popmClearSketch.setText(translation); break;
            case Rubber_Size : 
                popmRubberSize2.setText(translation+" : 2px");
                popmRubberSize4.setText(translation+" : 4px"); 
                popmRubberSize6.setText(translation+" : 6px"); 
                popmRubberSize8.setText(translation+" : 8px"); 
                popmRubberSize10.setText(translation+" : 10px"); break;
            case Pen_Size : 
                popmPenSize2.setText(translation+" : 2px");
                popmPenSize4.setText(translation+" : 4px"); 
                popmPenSize6.setText(translation+" : 6px");
                popmPenSize8.setText(translation+" : 8px");
                popmPenSize10.setText(translation+" : 10px"); break;
            case By_Value :
                popmImageMove1.setText(translation+" 1");
                popmImageMove5.setText(translation+" 5");
                popmImageMove10.setText(translation+" 10");
                popmImageMove50.setText(translation+" 50");
                popmImageMove100.setText(translation+" 100"); break;
            case New : btnNew.setToolTipText(translation); break;
            case Open : btnOpen.setToolTipText(translation); break;
            case Save : btnSave.setToolTipText(translation); break;
            case Line : tbShapeLine.setToolTipText(translation); break;
            case Curve : tbShapeBezier.setToolTipText(translation); break;
            case Pen : tbFreeDrawing.setToolTipText(translation); break;
            case Rubber : tbFreeClear.setToolTipText(translation); break;
            case Clear_Image : btnUnloadImage.setToolTipText(translation); break;
            case Open_Image : btnImage.setToolTipText(translation); break;
            case Top : btnTopImage.setToolTipText(translation); break;
            case Left : btnLeftImage.setToolTipText(translation); break;
            case Center : btnCentreImage.setToolTipText(translation); break;
            case Right : btnRightImage.setToolTipText(translation); break;
            case Transparency_Image :
                slideImageAlpha.setToolTipText(translation);
                lblImageAlpha.setToolTipText(translation); break;
            case Bottom : btnBottomImage.setToolTipText(translation); break;
            case Transparency_Shape :
                slideDrawingAlpha.setToolTipText(translation);
                lblDrawingAlpha.setToolTipText(translation); break;
            case Scale_Size :
                slideScale.setToolTipText(translation);
                lblScale.setToolTipText(translation); break;
            case Translate : tbTranslation.setToolTipText(translation); break;
            //case Rotate : btnRotation.setToolTipText(translation); break;
            case Add_Layer : btnAddLayer.setToolTipText(translation); break;
            case Remove_Layer : btnDeleteLayer.setToolTipText(translation); break;
            case Layers_List : lstLayer.setToolTipText(translation); break;
            case Undo : btnHistorique.setToolTipText(translation); break;
            case Pen_Color : popmPenColor.setText(translation); break;
            case OK_Button : strOK_Button = translation; break;
            case Cancel_Button : strCancel_Button = translation; break;
            //case TransX : strTransX = translation; break;
            //case TransY : strTransY = translation; break;
            case RedRotateMessage : strRedMessage = translation; break;
            case RotateMessage : strMessage = translation; break;
            case UpdateFromComs : popmUpdate.setText(translation); break;
            case BSpline : tbShapeBSpline1.setToolTipText(translation); break;
            case BSplineExtend : tbNextPoint.setToolTipText(translation); break;
            case BSplineClose : btnCloseBSpline.setToolTipText(translation); break;
            case GoToA : tbMove.setToolTipText(translation);  break;
            case GoToB : tbReStart.setToolTipText(translation); break;
            case Duplicate : popmDuplicateLayer.setText(translation); break;
        }
    }
    
    /** <p>Initialize the class.<br />Initialise la classe.</p> */
    public void init(Locale loc){
        if (loc==Locale.US | loc==Locale.UK | loc==Locale.ENGLISH | loc==Locale.CANADA){
            setLanguage(Language.File, "File");
            setLanguage(Language.Quit, "Quit");
            setLanguage(Language.About, "About");
            setLanguage(Language.To_Draw, "To draw");
            setLanguage(Language.Image, "Image");
            setLanguage(Language.Shape, "Shape");
            setLanguage(Language.Scale, "Scale");
            setLanguage(Language.Operations, "Operations");
            setLanguage(Language.Layers, "Layers");            
            setLanguage(Language.Historic, "Historic");
            setLanguage(Language.Sketchpad, "Sketchpad");
            setLanguage(Language.Ass_Commands, "Ass commands : ");
            setLanguage(Language.Cut, "Cut");
            setLanguage(Language.Copy, "Copy");
            setLanguage(Language.Paste, "Paste");
            setLanguage(Language.Delete, "Delete");
            setLanguage(Language.Choose_Color, "Choose a color...");
            setLanguage(Language.Rename_Layer, "Rename the layer");            
            setLanguage(Language.Clear_Sketch, "Clear the sketch");
            setLanguage(Language.Rubber_Size, "Rubber size");
            setLanguage(Language.Pen_Size, "Pen size");
            setLanguage(Language.By_Value, "By");
            setLanguage(Language.New, "Create a new drawing");
            setLanguage(Language.Open, "Open a file");
            setLanguage(Language.Save, "Save a file");
            setLanguage(Language.Line, "Draw lines");
            setLanguage(Language.Curve, "Draw curves");            
            setLanguage(Language.Pen, "Draw a sketch");
            setLanguage(Language.Rubber, "Clear a sketch");
            setLanguage(Language.Clear_Image, "Clear image");
            setLanguage(Language.Open_Image, "Open image");
            setLanguage(Language.Top, "Move an image to the top");
            setLanguage(Language.Left, "Move an image to the left");
            setLanguage(Language.Center, "Center an image");
            setLanguage(Language.Right, "Move an image to the right");
            setLanguage(Language.Transparency_Image, "Transparency of image");            
            setLanguage(Language.Bottom, "Move an image to the bottom");            
            setLanguage(Language.Transparency_Shape, "Transparency of shape");
            setLanguage(Language.Scale_Size, "Scale size");
            setLanguage(Language.Translate, "Translate the drawing");
            setLanguage(Language.Rotate, "Rotate the drawing");
            setLanguage(Language.Add_Layer, "Add a layer");
            setLanguage(Language.Remove_Layer, "Remove a layer");
            setLanguage(Language.Layers_List, "Layers list");
            setLanguage(Language.Undo, "Clear a point of the current layer");
            setLanguage(Language.Pen_Color, "Choose the color of the pen...");            
            setLanguage(Language.OK_Button, "OK");            
            setLanguage(Language.Cancel_Button, "Cancel");
            setLanguage(Language.TransX, "Translation on X");
            setLanguage(Language.TransY, "Translation on Y");
            setLanguage(Language.RedRotateMessage, "");
            setLanguage(Language.RotateMessage, "Angle of rotation");
            setLanguage(Language.UpdateFromComs, "Update");
            setLanguage(Language.BSpline, "Draw a bspline");
            setLanguage(Language.BSplineClose, "Close a bspline");
            setLanguage(Language.BSplineExtend, "Extend a bspline");
            setLanguage(Language.GoToA, "Go to (n command)");
            setLanguage(Language.GoToB, "Go to (m command)");
            setLanguage(Language.Duplicate, "Duplicate this layer");
        }else if (loc==Locale.FRANCE | loc==Locale.FRENCH | loc==Locale.CANADA_FRENCH){
            //Logiciel déjà en Français
        }else{
            //Recherche des fichiers *.lang
            File folder = new File(docs);
            for(File sf : folder.listFiles()){
                if(sf.getName().endsWith(".lang")){
                    if(listFiles.contains(sf)==false){
                        listFiles.add(sf);
                    }
                }
            }
            
            //Si l'ordinateur tourne en Italien et que le fichier de traduction
            //est aussi en Italien alors on charge le fichier en Italien.
            //Tous les pays de la norme ISO3166 marchent selon l'exemple ci-dessus.
            ISO_3166 ISOcode = ISO_3166.Unknown;
            for (File sf : listFiles){
                String code = sf.getName().substring(0, sf.getName().indexOf("."));                
                ISOcode = ISOcode.getISO_3166(code);
                if(loc.getISO3Country().equalsIgnoreCase(ISOcode.getAlpha3())){
                    try{
                        FileInputStream fis = new FileInputStream(sf);
                        java.io.BufferedReader br = new java.io.BufferedReader(
                                new java.io.InputStreamReader(fis, "UTF-8"));
                        String newline = "";

                        //Reading of file
                        while((newline=br.readLine())!=null){
                            String value = newline.substring(newline.indexOf(":")+1);
                            if(newline.startsWith("assSketchpadFile")){setLanguage(Language.File, value);}
                            if(newline.startsWith("assSketchpadQuit")){setLanguage(Language.Quit, value);}
                            if(newline.startsWith("assSketchpadAbout")){setLanguage(Language.About, value);}
                            if(newline.startsWith("assSketchpadToDraw")){setLanguage(Language.To_Draw, value);}
                            if(newline.startsWith("assSketchpadImage")){setLanguage(Language.Image, value);}
                            if(newline.startsWith("assSketchpadShape")){setLanguage(Language.Shape, value);}
                            if(newline.startsWith("assSketchpadScale")){setLanguage(Language.Scale, value);}
                            if(newline.startsWith("assSketchpadOperations")){setLanguage(Language.Operations, value);}
                            if(newline.startsWith("assSketchpadLayers")){setLanguage(Language.Layers, value);}
                            if(newline.startsWith("assSketchpadHistoric")){setLanguage(Language.Historic, value);}
                            if(newline.startsWith("assSketchpadSketchpad")){setLanguage(Language.Sketchpad, value);}
                            if(newline.startsWith("assSketchpadAss_Commands")){setLanguage(Language.Ass_Commands, value);}
                            if(newline.startsWith("assSketchpadCut")){setLanguage(Language.Cut, value);}
                            if(newline.startsWith("assSketchpadCopy")){setLanguage(Language.Copy, value);}
                            if(newline.startsWith("assSketchpadPaste")){setLanguage(Language.Paste, value);}
                            if(newline.startsWith("assSketchpadDelete")){setLanguage(Language.Delete, value);}
                            if(newline.startsWith("assSketchpadChoose_Color")){setLanguage(Language.Choose_Color, value);}
                            if(newline.startsWith("assSketchpadRename_Layer")){setLanguage(Language.Rename_Layer, value);}
                            if(newline.startsWith("assSketchpadClear_Sketch")){setLanguage(Language.Clear_Sketch, value);}
                            if(newline.startsWith("assSketchpadRubber_Size")){setLanguage(Language.Rubber_Size, value);}
                            if(newline.startsWith("assSketchpadPen_Size")){setLanguage(Language.Pen_Size, value);}
                            if(newline.startsWith("assSketchpadBy_Value")){setLanguage(Language.By_Value, value);}
                            if(newline.startsWith("assSketchpadNew")){setLanguage(Language.New, value);}
                            if(newline.startsWith("assSketchpadOpen")){setLanguage(Language.Open, value);}
                            if(newline.startsWith("assSketchpadSave")){setLanguage(Language.Save, value);}
                            if(newline.startsWith("assSketchpadLine")){setLanguage(Language.Line, value);}
                            if(newline.startsWith("assSketchpadCurve")){setLanguage(Language.Curve, value);}
                            if(newline.startsWith("assSketchpadPen")){setLanguage(Language.Pen, value);}
                            if(newline.startsWith("assSketchpadRubber")){setLanguage(Language.Rubber, value);}
                            if(newline.startsWith("assSketchpadClear_Image")){setLanguage(Language.Clear_Image, value);}
                            if(newline.startsWith("assSketchpadOpen_Image")){setLanguage(Language.Open_Image, value);}
                            if(newline.startsWith("assSketchpadTop")){setLanguage(Language.Top, value);}
                            if(newline.startsWith("assSketchpadLeft")){setLanguage(Language.Left, value);}
                            if(newline.startsWith("assSketchpadCenter")){setLanguage(Language.Center, value);}
                            if(newline.startsWith("assSketchpadRight")){setLanguage(Language.Right, value);}
                            if(newline.startsWith("assSketchpadTrans_Image")){setLanguage(Language.Transparency_Image, value);}
                            if(newline.startsWith("assSketchpadBottom")){setLanguage(Language.Bottom, value);}
                            if(newline.startsWith("assSketchpadTrans_Shape")){setLanguage(Language.Transparency_Shape, value);}
                            if(newline.startsWith("assSketchpadScale_Size")){setLanguage(Language.Scale_Size, value);}
                            if(newline.startsWith("assSketchpadTranslate")){setLanguage(Language.Translate, value);}
                            if(newline.startsWith("assSketchpadRotate")){setLanguage(Language.Rotate, value);}
                            if(newline.startsWith("assSketchpadAdd_Layer")){setLanguage(Language.Add_Layer, value);}
                            if(newline.startsWith("assSketchpadRemove_Layer")){setLanguage(Language.Remove_Layer, value);}
                            if(newline.startsWith("assSketchpadLayers_List")){setLanguage(Language.Layers_List, value);}
                            if(newline.startsWith("assSketchpadUndo")){setLanguage(Language.Undo, value);}
                            if(newline.startsWith("assSketchpadPen_Color")){setLanguage(Language.Pen_Color, value);}
                            if(newline.startsWith("assSketchpadOK_Button")){setLanguage(Language.OK_Button, value);}
                            if(newline.startsWith("assSketchpadCancel_Button")){setLanguage(Language.Cancel_Button, value);}
                            if(newline.startsWith("assSketchpadTransX")){setLanguage(Language.TransX, value);}
                            if(newline.startsWith("assSketchpadTransY")){setLanguage(Language.TransY, value);}
                            if(newline.startsWith("assSketchpadRedRotateMessage")){setLanguage(Language.RedRotateMessage, value);}
                            if(newline.startsWith("assSketchpadRotateMessage")){setLanguage(Language.RotateMessage, value);}
                            if(newline.startsWith("assSketchpadUpdateFromComs")){setLanguage(Language.UpdateFromComs, value);}
                            if(newline.startsWith("assSketchpadBSpline")){setLanguage(Language.BSpline, value);}
                            if(newline.startsWith("assSketchpadBSplineClose")){setLanguage(Language.BSplineClose, value);}
                            if(newline.startsWith("assSketchpadBSplineExtend")){setLanguage(Language.BSplineExtend, value);}
                            if(newline.startsWith("assSketchpadGoToA")){setLanguage(Language.GoToA, value);}
                            if(newline.startsWith("assSketchpadGoToB")){setLanguage(Language.GoToB, value);}
                            if(newline.startsWith("assSketchpadDuplicate")){setLanguage(Language.Duplicate, value);}
                            
                        }
                        br.close(); fis.close();
                    }catch(Exception exc){
                        //Nothing
                    }
                }
            }
        }
    }
    
    
    // </editor-fold>
    
    private String getAssSketchpadDirectory(){
        if(System.getProperty("os.name").equalsIgnoreCase("Mac OS X")){
            java.io.File file = new java.io.File("");
            return file.getAbsolutePath();
        }
        String path = System.getProperty("user.dir");
        //System.out.println("user dir is : "+path);
        if(path.toLowerCase().contains("jre")){
            File f = new File(getClass().getProtectionDomain()
                    .getCodeSource().getLocation().toString()
                    .substring(6));
            path = f.getParent();
            //System.out.println("by class is : "+path);
        }
        return path;
    }
    
    private void setLanguageAndConfigure(feuille.lib.Language lang){
        if(lang!=null){localeLanguage = lang;}
        if(localeLanguage.getValueOf("assSketchpadFile")!=null){
            setLanguage(Language.File,
                    localeLanguage.getValueOf("assSketchpadFile"));}
        if(localeLanguage.getValueOf("assSketchpadToDraw")!=null){
            setLanguage(Language.To_Draw,
                    localeLanguage.getValueOf("assSketchpadToDraw"));}
        if(localeLanguage.getValueOf("assSketchpadImage")!=null){
            setLanguage(Language.Image,
                    localeLanguage.getValueOf("assSketchpadImage"));}
        if(localeLanguage.getValueOf("assSketchpadShape")!=null){
            setLanguage(Language.Shape,
                    localeLanguage.getValueOf("assSketchpadShape"));}
        if(localeLanguage.getValueOf("assSketchpadScale")!=null){
            setLanguage(Language.Scale,
                    localeLanguage.getValueOf("assSketchpadScale"));}
        if(localeLanguage.getValueOf("assSketchpadOperations")!=null){
            setLanguage(Language.Operations,
                    localeLanguage.getValueOf("assSketchpadOperations"));}
        if(localeLanguage.getValueOf("assSketchpadLayers")!=null){
            setLanguage(Language.Layers,
                    localeLanguage.getValueOf("assSketchpadLayers"));}
        if(localeLanguage.getValueOf("assSketchpadHistoric")!=null){
            setLanguage(Language.Historic,
                    localeLanguage.getValueOf("assSketchpadHistoric"));}
        if(localeLanguage.getValueOf("assSketchpadSketchpad")!=null){
            setLanguage(Language.Sketchpad,
                    localeLanguage.getValueOf("assSketchpadSketchpad"));}
        if(localeLanguage.getValueOf("assSketchpadAss_Commands")!=null){
            setLanguage(Language.Ass_Commands,
                    localeLanguage.getValueOf("assSketchpadAss_Commands"));}
        if(localeLanguage.getValueOf("assSketchpadCut")!=null){
            setLanguage(Language.Cut,
                    localeLanguage.getValueOf("assSketchpadCut"));}
        if(localeLanguage.getValueOf("assSketchpadCopy")!=null){
            setLanguage(Language.Copy,
                    localeLanguage.getValueOf("assSketchpadCopy"));}
        if(localeLanguage.getValueOf("assSketchpadPaste")!=null){
            setLanguage(Language.Paste,
                    localeLanguage.getValueOf("assSketchpadPaste"));}
        if(localeLanguage.getValueOf("assSketchpadDelete")!=null){
            setLanguage(Language.Delete,
                    localeLanguage.getValueOf("assSketchpadDelete"));}
        if(localeLanguage.getValueOf("assSketchpadUpdateFromComs")!=null){
            setLanguage(Language.UpdateFromComs,
                    localeLanguage.getValueOf("assSketchpadUpdateFromComs"));}
        if(localeLanguage.getValueOf("assSketchpadChoose_Color")!=null){
            setLanguage(Language.Choose_Color,
                    localeLanguage.getValueOf("assSketchpadChoose_Color"));}
        if(localeLanguage.getValueOf("assSketchpadRename_Layer")!=null){
            setLanguage(Language.Rename_Layer,
                    localeLanguage.getValueOf("assSketchpadRename_Layer"));}
        if(localeLanguage.getValueOf("assSketchpadClear_Sketch")!=null){
            setLanguage(Language.Clear_Sketch,
                    localeLanguage.getValueOf("assSketchpadClear_Sketch"));}
        if(localeLanguage.getValueOf("assSketchpadRubber_Size")!=null){
            setLanguage(Language.Rubber_Size,
                    localeLanguage.getValueOf("assSketchpadRubber_Size"));}
        if(localeLanguage.getValueOf("assSketchpadPen_Size")!=null){
            setLanguage(Language.Pen_Size,
                    localeLanguage.getValueOf("assSketchpadPen_Size"));}
        if(localeLanguage.getValueOf("assSketchpadBy_Value")!=null){
            setLanguage(Language.By_Value,
                    localeLanguage.getValueOf("assSketchpadBy_Value"));}
        if(localeLanguage.getValueOf("assSketchpadNew")!=null){
            setLanguage(Language.New,
                    localeLanguage.getValueOf("assSketchpadNew"));}
        if(localeLanguage.getValueOf("assSketchpadOpen")!=null){
            setLanguage(Language.Open,
                    localeLanguage.getValueOf("assSketchpadOpen"));}
        if(localeLanguage.getValueOf("assSketchpadSave")!=null){
            setLanguage(Language.Save,
                    localeLanguage.getValueOf("assSketchpadSave"));}
        if(localeLanguage.getValueOf("assSketchpadLine")!=null){
            setLanguage(Language.Line,
                    localeLanguage.getValueOf("assSketchpadLine"));}
        if(localeLanguage.getValueOf("assSketchpadCurve")!=null){
            setLanguage(Language.Curve,
                    localeLanguage.getValueOf("assSketchpadCurve"));}
        if(localeLanguage.getValueOf("assSketchpadPen")!=null){
            setLanguage(Language.Pen,
                    localeLanguage.getValueOf("assSketchpadPen"));}
        if(localeLanguage.getValueOf("assSketchpadRubber")!=null){
            setLanguage(Language.Rubber,
                    localeLanguage.getValueOf("assSketchpadRubber"));}
        if(localeLanguage.getValueOf("assSketchpadClear_Image")!=null){
            setLanguage(Language.Clear_Image,
                    localeLanguage.getValueOf("assSketchpadClear_Image"));}
        if(localeLanguage.getValueOf("assSketchpadOpen_Image")!=null){
            setLanguage(Language.Open_Image,
                    localeLanguage.getValueOf("assSketchpadOpen_Image"));}
        if(localeLanguage.getValueOf("assSketchpadTop")!=null){
            setLanguage(Language.Top,
                    localeLanguage.getValueOf("assSketchpadTop"));}
        if(localeLanguage.getValueOf("assSketchpadLeft")!=null){
            setLanguage(Language.Left,
                    localeLanguage.getValueOf("assSketchpadLeft"));}
        if(localeLanguage.getValueOf("assSketchpadCenter")!=null){
            setLanguage(Language.Center,
                    localeLanguage.getValueOf("assSketchpadCenter"));}
        if(localeLanguage.getValueOf("assSketchpadRight")!=null){
            setLanguage(Language.Right,
                    localeLanguage.getValueOf("assSketchpadRight"));}
        if(localeLanguage.getValueOf("assSketchpadTrans_Image")!=null){
            setLanguage(Language.Transparency_Image,
                    localeLanguage.getValueOf("assSketchpadTrans_Image"));}
        if(localeLanguage.getValueOf("assSketchpadBottom")!=null){
            setLanguage(Language.Bottom,
                    localeLanguage.getValueOf("assSketchpadBottom"));}
        if(localeLanguage.getValueOf("assSketchpadTrans_Shape")!=null){
            setLanguage(Language.Transparency_Shape,
                    localeLanguage.getValueOf("assSketchpadTrans_Shape"));}
        if(localeLanguage.getValueOf("assSketchpadScale_Size")!=null){
            setLanguage(Language.Scale_Size,
                    localeLanguage.getValueOf("assSketchpadScale_Size"));}
        if(localeLanguage.getValueOf("assSketchpadTranslate")!=null){
            setLanguage(Language.Translate,
                    localeLanguage.getValueOf("assSketchpadTranslate"));}
        if(localeLanguage.getValueOf("assSketchpadRotate")!=null){
            setLanguage(Language.Rotate,
                    localeLanguage.getValueOf("assSketchpadRotate"));}
        if(localeLanguage.getValueOf("assSketchpadAdd_Layer")!=null){
            setLanguage(Language.Add_Layer,
                    localeLanguage.getValueOf("assSketchpadAdd_Layer"));}
        if(localeLanguage.getValueOf("assSketchpadRemove_Layer")!=null){
            setLanguage(Language.Remove_Layer,
                    localeLanguage.getValueOf("assSketchpadRemove_Layer"));}
        if(localeLanguage.getValueOf("assSketchpadLayers_List")!=null){
            setLanguage(Language.Layers_List,
                    localeLanguage.getValueOf("assSketchpadLayers_List"));}
        if(localeLanguage.getValueOf("assSketchpadUndo")!=null){
            setLanguage(Language.Undo,
                    localeLanguage.getValueOf("assSketchpadUndo"));}
        if(localeLanguage.getValueOf("assSketchpadPen_Color")!=null){
            setLanguage(Language.Pen_Color,
                    localeLanguage.getValueOf("assSketchpadPen_Color"));}
        if(localeLanguage.getValueOf("assSketchpadOK_Button")!=null){
            setLanguage(Language.OK_Button,
                    localeLanguage.getValueOf("assSketchpadOK_Button"));}
        if(localeLanguage.getValueOf("assSketchpadCancel_Button")!=null){
            setLanguage(Language.Cancel_Button,
                    localeLanguage.getValueOf("assSketchpadCancel_Button"));}
        if(localeLanguage.getValueOf("assSketchpadTransX")!=null){
            setLanguage(Language.TransX,
                    localeLanguage.getValueOf("assSketchpadTransX"));}
        if(localeLanguage.getValueOf("assSketchpadTransY")!=null){
            setLanguage(Language.TransY,
                    localeLanguage.getValueOf("assSketchpadTransY"));}
        if(localeLanguage.getValueOf("assSketchpadRedRotateMessage")!=null){
            setLanguage(Language.RedRotateMessage,
                    localeLanguage.getValueOf("assSketchpadRedRotateMessage"));}
        if(localeLanguage.getValueOf("assSketchpadRotateMessage")!=null){
            setLanguage(Language.RotateMessage,
                    localeLanguage.getValueOf("assSketchpadRotateMessage"));}        
        if(localeLanguage.getValueOf("assSketchpadBSpline")!=null){
            setLanguage(Language.BSpline,
                    localeLanguage.getValueOf("assSketchpadBSpline"));}
        if(localeLanguage.getValueOf("assSketchpadBSplineClose")!=null){
            setLanguage(Language.BSplineClose,
                    localeLanguage.getValueOf("assSketchpadBSplineClose"));}
        if(localeLanguage.getValueOf("assSketchpadBSplineExtend")!=null){
            setLanguage(Language.BSplineExtend,
                    localeLanguage.getValueOf("assSketchpadBSplineExtend"));}
        if(localeLanguage.getValueOf("assSketchpadGoToA")!=null){
            setLanguage(Language.GoToA,
                    localeLanguage.getValueOf("assSketchpadGoToA"));}
        if(localeLanguage.getValueOf("assSketchpadGoToB")!=null){
            setLanguage(Language.GoToB,
                    localeLanguage.getValueOf("assSketchpadGoToB"));}
        if(localeLanguage.getValueOf("assSketchpadDuplicate")!=null){
            setLanguage(Language.Duplicate,
                    localeLanguage.getValueOf("assSketchpadDuplicate"));}
        if(localeLanguage.getValueOf("assSketchpadMode")!=null){
            ifrMode.setToolTipText(localeLanguage.getValueOf("assSketchpadMode"));}
        if(localeLanguage.getValueOf("assSketchpadScript")!=null){
            ifrScript.setToolTipText(localeLanguage.getValueOf("assSketchpadScript"));}
        if(localeLanguage.getValueOf("assSketchpadAssComs")!=null){
            ifrAssCommands.setToolTipText(localeLanguage.getValueOf("assSketchpadAssComs"));}
        if(localeLanguage.getValueOf("assSketchpadOrnament")!=null){
            ifrOrnament.setToolTipText(localeLanguage.getValueOf("assSketchpadOrnament"));}
        
        if(localeLanguage.getValueOf("assSketchpadImpFonts")!=null){
            btnOpenFonts.setToolTipText(localeLanguage.getValueOf("assSketchpadImpFonts"));}
        if(localeLanguage.getValueOf("assSketchpadNormalMode")!=null){
            tbNormalMode.setToolTipText(localeLanguage.getValueOf("assSketchpadNormalMode"));}
        if(localeLanguage.getValueOf("assSketchpadOrnMode")!=null){
            tbOrnamentMode.setToolTipText(localeLanguage.getValueOf("assSketchpadOrnMode"));}
        if(localeLanguage.getValueOf("assSketchpadResize")!=null){
            tbResize.setToolTipText(localeLanguage.getValueOf("assSketchpadResize"));}
        if(localeLanguage.getValueOf("assSketchpadShear")!=null){
            tbShear.setToolTipText(localeLanguage.getValueOf("assSketchpadShear"));}
        if(localeLanguage.getValueOf("assSketchpadSelect")!=null){
            tbSelection.setToolTipText(localeLanguage.getValueOf("assSketchpadSelect"));}
        if(localeLanguage.getValueOf("assSketchpadReady")!=null){
            btnOrnReady.setText(localeLanguage.getValueOf("assSketchpadReady"));}
        if(localeLanguage.getValueOf("assSketchpadSteadyGo")!=null){
            btnOrnGenerate.setText(localeLanguage.getValueOf("assSketchpadSteadyGo"));}
        if(localeLanguage.getValueOf("assSketchpadGMovement")!=null){
            lblMainMove.setText(localeLanguage.getValueOf("assSketchpadGMovement"));}
        if(localeLanguage.getValueOf("assSketchpadOrnYes")!=null){
            rbOrnMainMoveOn.setText(localeLanguage.getValueOf("assSketchpadOrnYes"));}
        if(localeLanguage.getValueOf("assSketchpadOrnNo")!=null){
            rbOrnMainMoveOff.setText(localeLanguage.getValueOf("assSketchpadOrnNo"));}
        if(localeLanguage.getValueOf("assSketchpadFrequency")!=null){
            lblOrnMMFreq.setText(localeLanguage.getValueOf("assSketchpadFrequency"));}
        if(localeLanguage.getValueOf("assSketchpadSelCopyPaste")!=null){
            popmSelectionCopyPaste.setText(localeLanguage.getValueOf("assSketchpadSelCopyPaste"));}
        if(localeLanguage.getValueOf("assSketchpadSelSym")!=null){
            popmSelectionSym.setText(localeLanguage.getValueOf("assSketchpadSelSym"));}
        if(localeLanguage.getValueOf("assSketchpadSelGeo1A")!=null){
            popmSelectionGeo1A.setText(localeLanguage.getValueOf("assSketchpadSelGeo1A"));}
        if(localeLanguage.getValueOf("assSketchpadSelGeo1B")!=null){
            popmSelectionGeo1B.setText(localeLanguage.getValueOf("assSketchpadSelGeo1B"));}
        if(localeLanguage.getValueOf("assSketchpadSelGeo2A")!=null){
            popmSelectionGeo2A.setText(localeLanguage.getValueOf("assSketchpadSelGeo2A"));}
        if(localeLanguage.getValueOf("assSketchpadSelGeo2B")!=null){
            popmSelectionGeo2B.setText(localeLanguage.getValueOf("assSketchpadSelGeo2B"));}
        if(localeLanguage.getValueOf("assSketchpadSelGeo3A")!=null){
            popmSelectionGeo3A.setText(localeLanguage.getValueOf("assSketchpadSelGeo3A"));}
        if(localeLanguage.getValueOf("assSketchpadSelGeo3B")!=null){
            popmSelectionGeo3B.setText(localeLanguage.getValueOf("assSketchpadSelGeo3B"));}
        if(localeLanguage.getValueOf("assSketchpadSelGeoPlus")!=null){
            popmSelectionGeoPlus.setText(localeLanguage.getValueOf("assSketchpadSelGeoPlus"));}
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bgDraw = new javax.swing.ButtonGroup();
        bgMode = new javax.swing.ButtonGroup();
        bgOrnMainMove = new javax.swing.ButtonGroup();
        fcZDrawing = new javax.swing.JFileChooser();
        popLayer = new javax.swing.JPopupMenu();
        popmColor = new javax.swing.JMenuItem();
        popmName = new javax.swing.JMenuItem();
        popImageMove = new javax.swing.JPopupMenu();
        popmImageMove1 = new javax.swing.JRadioButtonMenuItem();
        popmImageMove5 = new javax.swing.JRadioButtonMenuItem();
        popmImageMove10 = new javax.swing.JRadioButtonMenuItem();
        popmImageMove50 = new javax.swing.JRadioButtonMenuItem();
        popmImageMove100 = new javax.swing.JRadioButtonMenuItem();
        popAssCom = new javax.swing.JPopupMenu();
        popmCut = new javax.swing.JMenuItem();
        popmCopy = new javax.swing.JMenuItem();
        popmPaste = new javax.swing.JMenuItem();
        popmDelete = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        popmUpdate = new javax.swing.JMenuItem();
        popPenSize = new javax.swing.JPopupMenu();
        popmPenSize2 = new javax.swing.JRadioButtonMenuItem();
        popmPenSize4 = new javax.swing.JRadioButtonMenuItem();
        popmPenSize6 = new javax.swing.JRadioButtonMenuItem();
        popmPenSize8 = new javax.swing.JRadioButtonMenuItem();
        popmPenSize10 = new javax.swing.JRadioButtonMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        popmPenColor = new javax.swing.JMenuItem();
        popDrawing = new javax.swing.JPopupMenu();
        popmLayerColor = new javax.swing.JMenuItem();
        popmLayerName = new javax.swing.JMenuItem();
        popmDuplicateLayer = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        popmClearSketch = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        popmSelectionCopyPaste = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        popmSelectionSym = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        popmSelectionGeo1A = new javax.swing.JMenuItem();
        popmSelectionGeo1B = new javax.swing.JMenuItem();
        popmSelectionGeo2A = new javax.swing.JMenuItem();
        popmSelectionGeo2B = new javax.swing.JMenuItem();
        popmSelectionGeo3A = new javax.swing.JMenuItem();
        popmSelectionGeo3B = new javax.swing.JMenuItem();
        popmSelectionGeoPlus = new javax.swing.JMenuItem();
        popRubberSize = new javax.swing.JPopupMenu();
        popmRubberSize2 = new javax.swing.JRadioButtonMenuItem();
        popmRubberSize4 = new javax.swing.JRadioButtonMenuItem();
        popmRubberSize6 = new javax.swing.JRadioButtonMenuItem();
        popmRubberSize8 = new javax.swing.JRadioButtonMenuItem();
        popmRubberSize10 = new javax.swing.JRadioButtonMenuItem();
        bgPenSize = new javax.swing.ButtonGroup();
        bgImageMove = new javax.swing.ButtonGroup();
        bgRubberSize = new javax.swing.ButtonGroup();
        bgOrnShapeMove = new javax.swing.ButtonGroup();
        jDesktopPane1 = new javax.swing.JDesktopPane();
        ifrOpenSave = new javax.swing.JInternalFrame();
        btnNew = new javax.swing.JButton();
        btnOpen = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnOpenFonts = new javax.swing.JButton();
        ifrMainDraw = new javax.swing.JInternalFrame();
        tbShapeLine = new javax.swing.JToggleButton();
        tbShapeBezier = new javax.swing.JToggleButton();
        tbFreeDrawing = new javax.swing.JToggleButton();
        tbFreeClear = new javax.swing.JToggleButton();
        tbShapeBSpline1 = new javax.swing.JToggleButton();
        tbNextPoint = new javax.swing.JToggleButton();
        tbReStart = new javax.swing.JToggleButton();
        tbMove = new javax.swing.JToggleButton();
        btnCloseBSpline = new javax.swing.JButton();
        ifrImage = new javax.swing.JInternalFrame();
        btnUnloadImage = new javax.swing.JButton();
        btnImage = new javax.swing.JButton();
        btnTopImage = new javax.swing.JButton();
        btnCentreImage = new javax.swing.JButton();
        btnLeftImage = new javax.swing.JButton();
        btnRightImage = new javax.swing.JButton();
        btnBottomImage = new javax.swing.JButton();
        slideImageAlpha = new javax.swing.JSlider();
        lblImageAlpha = new javax.swing.JLabel();
        ifrShape = new javax.swing.JInternalFrame();
        slideScale = new javax.swing.JSlider();
        slideDrawingAlpha = new javax.swing.JSlider();
        lblDrawingAlpha = new javax.swing.JLabel();
        lblScale = new javax.swing.JLabel();
        ifrMode = new javax.swing.JInternalFrame();
        tbNormalMode = new javax.swing.JToggleButton();
        tbOrnamentMode = new javax.swing.JToggleButton();
        ifrOperations = new javax.swing.JInternalFrame();
        tbTranslation = new javax.swing.JToggleButton();
        tbRotation = new javax.swing.JToggleButton();
        tbResize = new javax.swing.JToggleButton();
        tbShear = new javax.swing.JToggleButton();
        tbSelection = new javax.swing.JToggleButton();
        ifrScript = new javax.swing.JInternalFrame();
        btnApplyScript = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        listScript = new javax.swing.JList();
        ifrHistoric = new javax.swing.JInternalFrame();
        btnHistorique = new javax.swing.JButton();
        btnRedo = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        listRemember = new javax.swing.JList();
        ifrLayers = new javax.swing.JInternalFrame();
        btnAddLayer = new javax.swing.JButton();
        btnDeleteLayer = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        lstLayer = new javax.swing.JList();
        ifrDrawing = new javax.swing.JInternalFrame();
        spSheet = new javax.swing.JScrollPane();
        ifrOrnament = new javax.swing.JInternalFrame();
        btnOrnReady = new javax.swing.JButton();
        btnOrnGenerate = new javax.swing.JButton();
        lblMainMove = new javax.swing.JLabel();
        rbOrnMainMoveOff = new javax.swing.JRadioButton();
        rbOrnMainMoveOn = new javax.swing.JRadioButton();
        spiOrnMMFreq = new javax.swing.JSpinner();
        lblOrnMMFreq = new javax.swing.JLabel();
        tbOrnMMLine = new javax.swing.JToggleButton();
        tbOrnMMBezier = new javax.swing.JToggleButton();
        tbOrnMMBSpline = new javax.swing.JToggleButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        ornMMTable = new javax.swing.JTable();
        btnOrnMMClear = new javax.swing.JButton();
        ifrAssCommands = new javax.swing.JInternalFrame();
        lblCoordinates = new javax.swing.JLabel();
        lblAssCommands = new javax.swing.JLabel();
        tfAssCommands = new javax.swing.JTextField();

        popmColor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_kcoloredit.png"))); // NOI18N
        popmColor.setText("Choisir une couleur...");
        popmColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmColorActionPerformed(evt);
            }
        });
        popLayer.add(popmColor);

        popmName.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_action_editcopy.png"))); // NOI18N
        popmName.setText("Renommer la couche");
        popmName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmNameActionPerformed(evt);
            }
        });
        popLayer.add(popmName);

        bgImageMove.add(popmImageMove1);
        popmImageMove1.setText("Par 1");
        popmImageMove1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmImageMove1ActionPerformed(evt);
            }
        });
        popImageMove.add(popmImageMove1);

        bgImageMove.add(popmImageMove5);
        popmImageMove5.setText("Par 5");
        popmImageMove5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmImageMove5ActionPerformed(evt);
            }
        });
        popImageMove.add(popmImageMove5);

        bgImageMove.add(popmImageMove10);
        popmImageMove10.setSelected(true);
        popmImageMove10.setText("Par 10");
        popmImageMove10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmImageMove10ActionPerformed(evt);
            }
        });
        popImageMove.add(popmImageMove10);

        bgImageMove.add(popmImageMove50);
        popmImageMove50.setText("Par 50");
        popmImageMove50.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmImageMove50ActionPerformed(evt);
            }
        });
        popImageMove.add(popmImageMove50);

        bgImageMove.add(popmImageMove100);
        popmImageMove100.setText("Par 100");
        popmImageMove100.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmImageMove100ActionPerformed(evt);
            }
        });
        popImageMove.add(popmImageMove100);

        popmCut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_action_editcut.png"))); // NOI18N
        popmCut.setText("Couper");
        popmCut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmCutActionPerformed(evt);
            }
        });
        popAssCom.add(popmCut);

        popmCopy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_action_editcopy.png"))); // NOI18N
        popmCopy.setText("Copier");
        popmCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmCopyActionPerformed(evt);
            }
        });
        popAssCom.add(popmCopy);

        popmPaste.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_action_editpaste.png"))); // NOI18N
        popmPaste.setText("Coller");
        popmPaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmPasteActionPerformed(evt);
            }
        });
        popAssCom.add(popmPaste);

        popmDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_action_button_cancel.png"))); // NOI18N
        popmDelete.setText("Supprimer");
        popmDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmDeleteActionPerformed(evt);
            }
        });
        popAssCom.add(popmDelete);
        popAssCom.add(jSeparator3);

        popmUpdate.setText("Mettre à jour");
        popmUpdate.setToolTipText("");
        popmUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmUpdateActionPerformed(evt);
            }
        });
        popAssCom.add(popmUpdate);

        bgPenSize.add(popmPenSize2);
        popmPenSize2.setText("Taille du crayon : 02 px");
        popmPenSize2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20 piceau 01.png"))); // NOI18N
        popmPenSize2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmPenSize2ActionPerformed(evt);
            }
        });
        popPenSize.add(popmPenSize2);

        bgPenSize.add(popmPenSize4);
        popmPenSize4.setText("Taille du crayon : 04 px");
        popmPenSize4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20 piceau 02.png"))); // NOI18N
        popmPenSize4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmPenSize4ActionPerformed(evt);
            }
        });
        popPenSize.add(popmPenSize4);

        bgPenSize.add(popmPenSize6);
        popmPenSize6.setSelected(true);
        popmPenSize6.setText("Taille du crayon : 06 px");
        popmPenSize6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20 piceau 03.png"))); // NOI18N
        popmPenSize6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmPenSize6ActionPerformed(evt);
            }
        });
        popPenSize.add(popmPenSize6);

        bgPenSize.add(popmPenSize8);
        popmPenSize8.setText("Taille du crayon : 08 px");
        popmPenSize8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20 piceau 04.png"))); // NOI18N
        popmPenSize8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmPenSize8ActionPerformed(evt);
            }
        });
        popPenSize.add(popmPenSize8);

        bgPenSize.add(popmPenSize10);
        popmPenSize10.setText("Taille du crayon : 10 px");
        popmPenSize10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20 piceau 05.png"))); // NOI18N
        popmPenSize10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmPenSize10ActionPerformed(evt);
            }
        });
        popPenSize.add(popmPenSize10);
        popPenSize.add(jSeparator2);

        popmPenColor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_kcoloredit.png"))); // NOI18N
        popmPenColor.setText("Choisir la couleur du crayon...");
        popmPenColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmPenColorActionPerformed(evt);
            }
        });
        popPenSize.add(popmPenColor);

        popmLayerColor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_app_kcoloredit.png"))); // NOI18N
        popmLayerColor.setText("Choisir une couleur...");
        popmLayerColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmLayerColorActionPerformed(evt);
            }
        });
        popDrawing.add(popmLayerColor);

        popmLayerName.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_action_editcopy.png"))); // NOI18N
        popmLayerName.setText("Renommer la couche");
        popmLayerName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmLayerNameActionPerformed(evt);
            }
        });
        popDrawing.add(popmLayerName);

        popmDuplicateLayer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/funsub-dupliquer.png"))); // NOI18N
        popmDuplicateLayer.setText("Dupliquer la couche");
        popmDuplicateLayer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmDuplicateLayerActionPerformed(evt);
            }
        });
        popDrawing.add(popmDuplicateLayer);
        popDrawing.add(jSeparator1);

        popmClearSketch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Crystal_Clear_action_button_cancel.png"))); // NOI18N
        popmClearSketch.setText("Effacer le croquis");
        popmClearSketch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmClearSketchActionPerformed(evt);
            }
        });
        popDrawing.add(popmClearSketch);
        popDrawing.add(jSeparator4);

        popmSelectionCopyPaste.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Selection.png"))); // NOI18N
        popmSelectionCopyPaste.setText("Copier à la suite une fois");
        popmSelectionCopyPaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmSelectionCopyPasteActionPerformed(evt);
            }
        });
        popDrawing.add(popmSelectionCopyPaste);
        popDrawing.add(jSeparator6);

        popmSelectionSym.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Selection.png"))); // NOI18N
        popmSelectionSym.setText("Faire la symétrie");
        popmSelectionSym.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmSelectionSymActionPerformed(evt);
            }
        });
        popDrawing.add(popmSelectionSym);
        popDrawing.add(jSeparator5);

        popmSelectionGeo1A.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Selection.png"))); // NOI18N
        popmSelectionGeo1A.setText("Tour forme 'Ligne' horaire");
        popmSelectionGeo1A.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmSelectionGeo1AActionPerformed(evt);
            }
        });
        popDrawing.add(popmSelectionGeo1A);

        popmSelectionGeo1B.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Selection.png"))); // NOI18N
        popmSelectionGeo1B.setText("Tour forme 'Ligne' antihoraire");
        popmSelectionGeo1B.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmSelectionGeo1BActionPerformed(evt);
            }
        });
        popDrawing.add(popmSelectionGeo1B);

        popmSelectionGeo2A.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Selection.png"))); // NOI18N
        popmSelectionGeo2A.setText("Tour forme 'Triangle' horaire");
        popmSelectionGeo2A.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmSelectionGeo2AActionPerformed(evt);
            }
        });
        popDrawing.add(popmSelectionGeo2A);

        popmSelectionGeo2B.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Selection.png"))); // NOI18N
        popmSelectionGeo2B.setText("Tour forme 'Triangle' antihoraire");
        popmSelectionGeo2B.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmSelectionGeo2BActionPerformed(evt);
            }
        });
        popDrawing.add(popmSelectionGeo2B);

        popmSelectionGeo3A.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Selection.png"))); // NOI18N
        popmSelectionGeo3A.setText("Tour forme 'Carré' horaire");
        popmSelectionGeo3A.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmSelectionGeo3AActionPerformed(evt);
            }
        });
        popDrawing.add(popmSelectionGeo3A);

        popmSelectionGeo3B.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Selection.png"))); // NOI18N
        popmSelectionGeo3B.setText("Tour forme 'Carré' antihoraire");
        popmSelectionGeo3B.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmSelectionGeo3BActionPerformed(evt);
            }
        });
        popDrawing.add(popmSelectionGeo3B);

        popmSelectionGeoPlus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20px-Selection.png"))); // NOI18N
        popmSelectionGeoPlus.setText("Tour autre forme...");
        popmSelectionGeoPlus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmSelectionGeoPlusActionPerformed(evt);
            }
        });
        popDrawing.add(popmSelectionGeoPlus);

        bgRubberSize.add(popmRubberSize2);
        popmRubberSize2.setText("Taille de la gomme : 02 px");
        popmRubberSize2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20 piceau 01.png"))); // NOI18N
        popmRubberSize2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmRubberSize2ActionPerformed(evt);
            }
        });
        popRubberSize.add(popmRubberSize2);

        bgRubberSize.add(popmRubberSize4);
        popmRubberSize4.setText("Taille de la gomme : 04 px");
        popmRubberSize4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20 piceau 02.png"))); // NOI18N
        popmRubberSize4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmRubberSize4ActionPerformed(evt);
            }
        });
        popRubberSize.add(popmRubberSize4);

        bgRubberSize.add(popmRubberSize6);
        popmRubberSize6.setSelected(true);
        popmRubberSize6.setText("Taille de la gomme : 06 px");
        popmRubberSize6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20 piceau 03.png"))); // NOI18N
        popmRubberSize6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmRubberSize6ActionPerformed(evt);
            }
        });
        popRubberSize.add(popmRubberSize6);

        bgRubberSize.add(popmRubberSize8);
        popmRubberSize8.setText("Taille de la gomme : 08 px");
        popmRubberSize8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20 piceau 04.png"))); // NOI18N
        popmRubberSize8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmRubberSize8ActionPerformed(evt);
            }
        });
        popRubberSize.add(popmRubberSize8);

        bgRubberSize.add(popmRubberSize10);
        popmRubberSize10.setText("Taille de la gomme : 10 px");
        popmRubberSize10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/20 piceau 05.png"))); // NOI18N
        popmRubberSize10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmRubberSize10ActionPerformed(evt);
            }
        });
        popRubberSize.add(popmRubberSize10);

        setLayout(new java.awt.BorderLayout());

        ifrOpenSave.setIconifiable(true);
        ifrOpenSave.setTitle("Fichier");
        ifrOpenSave.setVisible(true);
        ifrOpenSave.getContentPane().setLayout(null);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/32px-Crystal_Clear_action_filenew.png"))); // NOI18N
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        ifrOpenSave.getContentPane().add(btnNew);
        btnNew.setBounds(0, 0, 40, 40);

        btnOpen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/32px-Crystal_Clear_filesystem_folder_grey_open.png"))); // NOI18N
        btnOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenActionPerformed(evt);
            }
        });
        ifrOpenSave.getContentPane().add(btnOpen);
        btnOpen.setBounds(40, 0, 40, 40);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/32px-Crystal_Clear_device_floppy_unmount.png"))); // NOI18N
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        ifrOpenSave.getContentPane().add(btnSave);
        btnSave.setBounds(80, 0, 40, 40);

        btnOpenFonts.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/32px-Crystal_Clear_mimetype_font_type1.png"))); // NOI18N
        btnOpenFonts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenFontsActionPerformed(evt);
            }
        });
        ifrOpenSave.getContentPane().add(btnOpenFonts);
        btnOpenFonts.setBounds(120, 0, 40, 40);

        jDesktopPane1.add(ifrOpenSave);
        ifrOpenSave.setBounds(10, 10, 180, 70);

        ifrMainDraw.setIconifiable(true);
        ifrMainDraw.setTitle("Pour dessiner");
        ifrMainDraw.setVisible(true);
        ifrMainDraw.getContentPane().setLayout(null);

        bgDraw.add(tbShapeLine);
        tbShapeLine.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/AFM-DrawingLine.png"))); // NOI18N
        tbShapeLine.setSelected(true);
        ifrMainDraw.getContentPane().add(tbShapeLine);
        tbShapeLine.setBounds(0, 0, 40, 40);

        bgDraw.add(tbShapeBezier);
        tbShapeBezier.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/AFM-DrawingBezier.png"))); // NOI18N
        ifrMainDraw.getContentPane().add(tbShapeBezier);
        tbShapeBezier.setBounds(40, 0, 40, 40);

        bgDraw.add(tbFreeDrawing);
        tbFreeDrawing.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/32-Crayon.png"))); // NOI18N
        tbFreeDrawing.setComponentPopupMenu(popPenSize);
        ifrMainDraw.getContentPane().add(tbFreeDrawing);
        tbFreeDrawing.setBounds(80, 0, 40, 40);

        bgDraw.add(tbFreeClear);
        tbFreeClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/32-Gomme.png"))); // NOI18N
        tbFreeClear.setComponentPopupMenu(popRubberSize);
        ifrMainDraw.getContentPane().add(tbFreeClear);
        tbFreeClear.setBounds(120, 0, 40, 40);

        bgDraw.add(tbShapeBSpline1);
        tbShapeBSpline1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/AFM-DrawingBSpline.png"))); // NOI18N
        ifrMainDraw.getContentPane().add(tbShapeBSpline1);
        tbShapeBSpline1.setBounds(0, 40, 40, 40);

        bgDraw.add(tbNextPoint);
        tbNextPoint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/AFM-NextAfterBSpline.png"))); // NOI18N
        ifrMainDraw.getContentPane().add(tbNextPoint);
        tbNextPoint.setBounds(80, 40, 40, 40);

        bgDraw.add(tbReStart);
        tbReStart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/move m.png"))); // NOI18N
        ifrMainDraw.getContentPane().add(tbReStart);
        tbReStart.setBounds(0, 80, 40, 40);

        bgDraw.add(tbMove);
        tbMove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/move n.png"))); // NOI18N
        ifrMainDraw.getContentPane().add(tbMove);
        tbMove.setBounds(40, 80, 40, 40);

        btnCloseBSpline.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/AFM-DrawingCloseBSpline.png"))); // NOI18N
        btnCloseBSpline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseBSplineActionPerformed(evt);
            }
        });
        ifrMainDraw.getContentPane().add(btnCloseBSpline);
        btnCloseBSpline.setBounds(40, 40, 40, 40);

        jDesktopPane1.add(ifrMainDraw);
        ifrMainDraw.setBounds(10, 90, 180, 150);

        ifrImage.setIconifiable(true);
        ifrImage.setTitle("Image");
        ifrImage.setVisible(true);
        ifrImage.getContentPane().setLayout(null);

        btnUnloadImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/32px-Crystal_Clear_app_windows_users.png"))); // NOI18N
        btnUnloadImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUnloadImageActionPerformed(evt);
            }
        });
        ifrImage.getContentPane().add(btnUnloadImage);
        btnUnloadImage.setBounds(0, 0, 40, 40);

        btnImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/32px-Crystal_Clear_app_kpaint.png"))); // NOI18N
        btnImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImageActionPerformed(evt);
            }
        });
        ifrImage.getContentPane().add(btnImage);
        btnImage.setBounds(40, 0, 40, 40);

        btnTopImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/f02.gif"))); // NOI18N
        btnTopImage.setComponentPopupMenu(popImageMove);
        btnTopImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTopImageActionPerformed(evt);
            }
        });
        ifrImage.getContentPane().add(btnTopImage);
        btnTopImage.setBounds(80, 0, 40, 40);

        btnCentreImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/f05.gif"))); // NOI18N
        btnCentreImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCentreImageActionPerformed(evt);
            }
        });
        ifrImage.getContentPane().add(btnCentreImage);
        btnCentreImage.setBounds(80, 40, 40, 40);

        btnLeftImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/f04.gif"))); // NOI18N
        btnLeftImage.setComponentPopupMenu(popImageMove);
        btnLeftImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLeftImageActionPerformed(evt);
            }
        });
        ifrImage.getContentPane().add(btnLeftImage);
        btnLeftImage.setBounds(40, 40, 40, 40);

        btnRightImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/f06.gif"))); // NOI18N
        btnRightImage.setComponentPopupMenu(popImageMove);
        btnRightImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRightImageActionPerformed(evt);
            }
        });
        ifrImage.getContentPane().add(btnRightImage);
        btnRightImage.setBounds(120, 40, 40, 40);

        btnBottomImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/f08.gif"))); // NOI18N
        btnBottomImage.setComponentPopupMenu(popImageMove);
        btnBottomImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBottomImageActionPerformed(evt);
            }
        });
        ifrImage.getContentPane().add(btnBottomImage);
        btnBottomImage.setBounds(80, 80, 40, 40);

        slideImageAlpha.setValue(100);
        slideImageAlpha.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                slideImageAlphaStateChanged(evt);
            }
        });
        ifrImage.getContentPane().add(slideImageAlpha);
        slideImageAlpha.setBounds(0, 80, 80, 26);

        lblImageAlpha.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblImageAlpha.setText("100%");
        ifrImage.getContentPane().add(lblImageAlpha);
        lblImageAlpha.setBounds(0, 100, 80, 20);

        jDesktopPane1.add(ifrImage);
        ifrImage.setBounds(10, 250, 180, 150);

        ifrShape.setIconifiable(true);
        ifrShape.setTitle("Forme et échelle");
        ifrShape.setVisible(true);
        ifrShape.getContentPane().setLayout(null);

        slideScale.setMaximum(5);
        slideScale.setMinimum(1);
        slideScale.setValue(1);
        slideScale.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                slideScaleStateChanged(evt);
            }
        });
        ifrShape.getContentPane().add(slideScale);
        slideScale.setBounds(80, 0, 80, 26);

        slideDrawingAlpha.setValue(20);
        slideDrawingAlpha.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                slideDrawingAlphaStateChanged(evt);
            }
        });
        ifrShape.getContentPane().add(slideDrawingAlpha);
        slideDrawingAlpha.setBounds(0, 0, 80, 26);

        lblDrawingAlpha.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDrawingAlpha.setText("20%");
        ifrShape.getContentPane().add(lblDrawingAlpha);
        lblDrawingAlpha.setBounds(0, 20, 80, 20);

        lblScale.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblScale.setText("1");
        ifrShape.getContentPane().add(lblScale);
        lblScale.setBounds(80, 20, 80, 20);

        jDesktopPane1.add(ifrShape);
        ifrShape.setBounds(10, 410, 180, 70);

        ifrMode.setIconifiable(true);
        ifrMode.setTitle("Mode");
        ifrMode.setVisible(true);
        ifrMode.getContentPane().setLayout(null);

        bgMode.add(tbNormalMode);
        tbNormalMode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/32-Rose.png"))); // NOI18N
        tbNormalMode.setSelected(true);
        ifrMode.getContentPane().add(tbNormalMode);
        tbNormalMode.setBounds(0, 0, 40, 40);

        bgMode.add(tbOrnamentMode);
        tbOrnamentMode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/32-ornement.png"))); // NOI18N
        ifrMode.getContentPane().add(tbOrnamentMode);
        tbOrnamentMode.setBounds(40, 0, 40, 40);

        jDesktopPane1.add(ifrMode);
        ifrMode.setBounds(10, 490, 180, 70);

        ifrOperations.setIconifiable(true);
        ifrOperations.setTitle("Opérations");
        ifrOperations.setVisible(true);
        ifrOperations.getContentPane().setLayout(null);

        bgDraw.add(tbTranslation);
        tbTranslation.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/AFM-translate.png"))); // NOI18N
        ifrOperations.getContentPane().add(tbTranslation);
        tbTranslation.setBounds(0, 0, 40, 40);

        bgDraw.add(tbRotation);
        tbRotation.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/AFM-rotate.png"))); // NOI18N
        ifrOperations.getContentPane().add(tbRotation);
        tbRotation.setBounds(40, 0, 40, 40);

        bgDraw.add(tbResize);
        tbResize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/AFM-scale.png"))); // NOI18N
        ifrOperations.getContentPane().add(tbResize);
        tbResize.setBounds(80, 0, 40, 40);

        bgDraw.add(tbShear);
        tbShear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/AFM-shear.png"))); // NOI18N
        tbShear.setEnabled(false);
        ifrOperations.getContentPane().add(tbShear);
        tbShear.setBounds(120, 0, 40, 40);

        bgDraw.add(tbSelection);
        tbSelection.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/32px-Selection.png"))); // NOI18N
        ifrOperations.getContentPane().add(tbSelection);
        tbSelection.setBounds(0, 40, 40, 40);

        jDesktopPane1.add(ifrOperations);
        ifrOperations.setBounds(10, 570, 180, 110);

        ifrScript.setIconifiable(true);
        ifrScript.setResizable(true);
        ifrScript.setTitle("Vos scripts");
        ifrScript.setVisible(true);
        ifrScript.getContentPane().setLayout(null);

        btnApplyScript.setText(">>");
        btnApplyScript.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApplyScriptActionPerformed(evt);
            }
        });
        ifrScript.getContentPane().add(btnApplyScript);
        btnApplyScript.setBounds(0, 0, 160, 20);

        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        listScript.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(listScript);

        ifrScript.getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(0, 20, 160, 110);

        jDesktopPane1.add(ifrScript);
        ifrScript.setBounds(10, 690, 180, 160);

        ifrHistoric.setIconifiable(true);
        ifrHistoric.setResizable(true);
        ifrHistoric.setTitle("Historique");
        ifrHistoric.setVisible(true);
        ifrHistoric.getContentPane().setLayout(null);

        btnHistorique.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/32px-Crystal_Clear_action_back.png"))); // NOI18N
        btnHistorique.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHistoriqueActionPerformed(evt);
            }
        });
        ifrHistoric.getContentPane().add(btnHistorique);
        btnHistorique.setBounds(0, 0, 40, 40);

        btnRedo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/32px-Crystal_Clear_action_forward.png"))); // NOI18N
        btnRedo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRedoActionPerformed(evt);
            }
        });
        ifrHistoric.getContentPane().add(btnRedo);
        btnRedo.setBounds(40, 0, 40, 40);

        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        listRemember.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(listRemember);

        ifrHistoric.getContentPane().add(jScrollPane2);
        jScrollPane2.setBounds(0, 40, 160, 280);

        jDesktopPane1.add(ifrHistoric);
        ifrHistoric.setBounds(390, 90, 180, 350);

        ifrLayers.setIconifiable(true);
        ifrLayers.setResizable(true);
        ifrLayers.setTitle("Couches");
        ifrLayers.setVisible(true);
        ifrLayers.getContentPane().setLayout(null);

        btnAddLayer.setText("+");
        btnAddLayer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddLayerActionPerformed(evt);
            }
        });
        ifrLayers.getContentPane().add(btnAddLayer);
        btnAddLayer.setBounds(0, 0, 80, 20);

        btnDeleteLayer.setText("-");
        btnDeleteLayer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteLayerActionPerformed(evt);
            }
        });
        ifrLayers.getContentPane().add(btnDeleteLayer);
        btnDeleteLayer.setBounds(80, 0, 80, 20);

        jScrollPane3.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        lstLayer.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        lstLayer.setComponentPopupMenu(popLayer);
        lstLayer.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstLayerValueChanged(evt);
            }
        });
        jScrollPane3.setViewportView(lstLayer);

        ifrLayers.getContentPane().add(jScrollPane3);
        jScrollPane3.setBounds(0, 20, 160, 100);

        jDesktopPane1.add(ifrLayers);
        ifrLayers.setBounds(200, 410, 180, 150);

        ifrDrawing.setIconifiable(true);
        ifrDrawing.setMaximizable(true);
        ifrDrawing.setResizable(true);
        ifrDrawing.setTitle("Bloc à dessin");
        ifrDrawing.setVisible(true);

        spSheet.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        spSheet.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        spSheet.setComponentPopupMenu(popDrawing);
        spSheet.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                spSheetMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                spSheetMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                spSheetMouseReleased(evt);
            }
        });
        spSheet.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                spSheetMouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                spSheetMouseMoved(evt);
            }
        });

        javax.swing.GroupLayout ifrDrawingLayout = new javax.swing.GroupLayout(ifrDrawing.getContentPane());
        ifrDrawing.getContentPane().setLayout(ifrDrawingLayout);
        ifrDrawingLayout.setHorizontalGroup(
            ifrDrawingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(spSheet, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
        );
        ifrDrawingLayout.setVerticalGroup(
            ifrDrawingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(spSheet, javax.swing.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)
        );

        jDesktopPane1.add(ifrDrawing);
        ifrDrawing.setBounds(200, 90, 180, 310);

        ifrOrnament.setIconifiable(true);
        ifrOrnament.setTitle("Ornement");
        ifrOrnament.setVisible(true);
        ifrOrnament.getContentPane().setLayout(null);

        btnOrnReady.setForeground(new java.awt.Color(255, 0, 0));
        btnOrnReady.setText("Prêt !");
        btnOrnReady.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOrnReadyActionPerformed(evt);
            }
        });
        ifrOrnament.getContentPane().add(btnOrnReady);
        btnOrnReady.setBounds(0, 0, 80, 20);

        btnOrnGenerate.setForeground(new java.awt.Color(255, 0, 0));
        btnOrnGenerate.setText("Généré !");
        btnOrnGenerate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOrnGenerateActionPerformed(evt);
            }
        });
        ifrOrnament.getContentPane().add(btnOrnGenerate);
        btnOrnGenerate.setBounds(80, 0, 80, 20);

        lblMainMove.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMainMove.setText("Mouvement général");
        ifrOrnament.getContentPane().add(lblMainMove);
        lblMainMove.setBounds(0, 20, 160, 20);

        bgOrnMainMove.add(rbOrnMainMoveOff);
        rbOrnMainMoveOff.setSelected(true);
        rbOrnMainMoveOff.setText("Non");
        rbOrnMainMoveOff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbOrnMainMoveOffActionPerformed(evt);
            }
        });
        ifrOrnament.getContentPane().add(rbOrnMainMoveOff);
        rbOrnMainMoveOff.setBounds(0, 40, 80, 20);

        bgOrnMainMove.add(rbOrnMainMoveOn);
        rbOrnMainMoveOn.setText("Oui");
        rbOrnMainMoveOn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbOrnMainMoveOnActionPerformed(evt);
            }
        });
        ifrOrnament.getContentPane().add(rbOrnMainMoveOn);
        rbOrnMainMoveOn.setBounds(80, 40, 80, 20);

        spiOrnMMFreq.setEnabled(false);
        ifrOrnament.getContentPane().add(spiOrnMMFreq);
        spiOrnMMFreq.setBounds(79, 60, 80, 30);

        lblOrnMMFreq.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblOrnMMFreq.setText("Fréquence :");
        lblOrnMMFreq.setEnabled(false);
        ifrOrnament.getContentPane().add(lblOrnMMFreq);
        lblOrnMMFreq.setBounds(0, 60, 80, 30);

        bgDraw.add(tbOrnMMLine);
        tbOrnMMLine.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/AFM-DrawingLine-OrnMainMove.png"))); // NOI18N
        tbOrnMMLine.setEnabled(false);
        ifrOrnament.getContentPane().add(tbOrnMMLine);
        tbOrnMMLine.setBounds(0, 90, 40, 40);

        bgDraw.add(tbOrnMMBezier);
        tbOrnMMBezier.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/AFM-DrawingBezier-OrnMainMove.png"))); // NOI18N
        tbOrnMMBezier.setEnabled(false);
        ifrOrnament.getContentPane().add(tbOrnMMBezier);
        tbOrnMMBezier.setBounds(40, 90, 40, 40);

        bgDraw.add(tbOrnMMBSpline);
        tbOrnMMBSpline.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/AFM-DrawingBSpline-OrnMainMove.png"))); // NOI18N
        tbOrnMMBSpline.setEnabled(false);
        ifrOrnament.getContentPane().add(tbOrnMMBSpline);
        tbOrnMMBSpline.setBounds(80, 90, 40, 40);

        jScrollPane4.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        ornMMTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane4.setViewportView(ornMMTable);

        ifrOrnament.getContentPane().add(jScrollPane4);
        jScrollPane4.setBounds(0, 130, 160, 80);

        btnOrnMMClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/edit-clear-4.png"))); // NOI18N
        btnOrnMMClear.setEnabled(false);
        btnOrnMMClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOrnMMClearActionPerformed(evt);
            }
        });
        ifrOrnament.getContentPane().add(btnOrnMMClear);
        btnOrnMMClear.setBounds(120, 90, 40, 40);

        jDesktopPane1.add(ifrOrnament);
        ifrOrnament.setBounds(200, 570, 180, 240);

        ifrAssCommands.setIconifiable(true);
        ifrAssCommands.setResizable(true);
        ifrAssCommands.setTitle("Commandes ASS");
        ifrAssCommands.setVisible(true);

        lblCoordinates.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCoordinates.setText("<html><h2>-100 ; -100");

        lblAssCommands.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblAssCommands.setText("Commandes ASS : ");

        tfAssCommands.setText("jTextField1");
        tfAssCommands.setComponentPopupMenu(popAssCom);

        javax.swing.GroupLayout ifrAssCommandsLayout = new javax.swing.GroupLayout(ifrAssCommands.getContentPane());
        ifrAssCommands.getContentPane().setLayout(ifrAssCommandsLayout);
        ifrAssCommandsLayout.setHorizontalGroup(
            ifrAssCommandsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ifrAssCommandsLayout.createSequentialGroup()
                .addComponent(lblCoordinates, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblAssCommands, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfAssCommands, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE))
        );
        ifrAssCommandsLayout.setVerticalGroup(
            ifrAssCommandsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ifrAssCommandsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(lblCoordinates)
                .addComponent(lblAssCommands, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(tfAssCommands, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jDesktopPane1.add(ifrAssCommands);
        ifrAssCommands.setBounds(200, 10, 670, 70);

        add(jDesktopPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void spSheetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_spSheetMouseClicked
        // QUAND ON CLIC SUR UN BOUTON DE LA SOURIS
    if(tbShapeBezier.isSelected() | tbShapeLine.isSelected()){
       // On récupère les coordonnées sur le composant sheet.
        int xa = evt.getXOnScreen()-(int)sh.getLocationOnScreen().getX();
        int ya = evt.getYOnScreen()-(int)sh.getLocationOnScreen().getY();
        Layer lay = getCurrentLayer();

        // Si on clique sur le bouton 1 de la souris (le bouton gauche).
        if(evt.getButton()==1){
            //On fait cette action que si on n'a jamais rien fait d'autre (que la première fois)
            if (lay.getFirstPoint()==null){
                //first recupère les coordonnées du tout premier point qu'on pose.
                lay.setFirstPoint(new java.awt.Point(xa/scale,ya/scale));
                ReStart m = new ReStart(xa/scale, ya/scale, xa/scale, ya/scale);
                lay.getShapesList().addShape(m); lay.addRemember(m);
            }else{
                //p est un nouveau élément "point" (voir classe Point), on récupère ses coordonnées.
                Point p = new Point(xa/scale, ya/scale);
                //on ajoute à la liste cet élément "point" (voir classe Point)
                lay.getShapesList().addShape(p); lay.addRemember(p);
            }            
            //montre la zone
            sh.updateGeneralPath(lay.getGeneralPath());
            //on l'ajout à la list de sheet afin qu'il apparaisse sur le dessin
            sh.updateShapesList(lay.getShapesList());

            //on définit ce point comme dernier point posé
            lay.setLastPoint(new java.awt.Point(xa/scale,ya/scale));

            //si last et first ne sont pas à la même coordonnée
            if(isSamePoint(lay.getFirstPoint(),lay.getLastPoint())==false){
                //si le bouton de création de "ligne" (voir classe Line) est enfoncé
                if(tbShapeLine.isSelected()){
                    //on crée une "ligne" (voir classe Line)
                    Line l = new Line();
                    //on la configure
                    l.setOriginPoint(
                            (int)lay.getFirstPoint().getX(),
                            (int)lay.getFirstPoint().getY());
                    l.setLastPoint(
                            (int)lay.getLastPoint().getX(), 
                            (int)lay.getLastPoint().getY());
                    //on l'ajoute à la liste
                    lay.getShapesList().addShape(l); lay.addRemember(l);
                    //montre la zone
                    sh.updateGeneralPath(lay.getGeneralPath());
                    sh.updateShapesList(lay.getShapesList());
                    //si le bouton de création de "bézier" (voir classe Bezier) est enfoncé
                    //voir aussi : http://fr.wikipedia.org/wiki/Courbe_de_B%C3%A9zier
                }else if(tbShapeBezier.isSelected()){
                    //on crée un "bézier" (voir classe Bezier) et on le configure
                    Bezier b = new Bezier(
                            (int)lay.getFirstPoint().getX(), 
                            (int)lay.getFirstPoint().getY(), 
                            (int)lay.getLastPoint().getX(), 
                            (int)lay.getLastPoint().getY());
                    //création des "controlpoint" (voir classe ControlPoint)
                    //servant de point de control pour la courbe de bézier
                    ControlPoint cp1 = b.getControl1();
                    ControlPoint cp2 = b.getControl2();
                    //on ajoute tout à la liste
                    lay.getShapesList().addShape(b); lay.addRemember(b);
                    lay.getShapesList().addShape(cp1); lay.addRemember(cp1);
                    lay.getShapesList().addShape(cp2); lay.addRemember(cp2);
                    //montre la zone
                    sh.updateGeneralPath(lay.getGeneralPath());
                    sh.updateShapesList(lay.getShapesList());
                }
                //first prend les coordonnées de last afin que l'on puisse se souvenir
                //du dernier point posé lors d'un nouveau clic gauche de souris
                lay.setFirstPoint(lay.getLastPoint());
                //on met à jour les commandes de dessin ASS
                tfAssCommands.setText(updateCommands());
                updateRemember(lay);
            }
            // Si on clique sur le bouton 3 de la souris (le bouton droit).
        }else if(evt.getButton()==3){
            //rotatePoint = new java.awt.Point(xa/scale,ya/scale);
    //            java.awt.Point p2d = new java.awt.Point(xa/scale,ya/scale);
    //            java.util.List<zdrawinglite.Shape> mylist = slist.getShapesAtPoint(p2d);
    //            for (zdrawinglite.Shape s : slist.getShapes()){
    //                for(zdrawinglite.Shape sb : mylist){
    //                    if (s.equals(sb)){
    //                        if(s instanceof zdrawinglite.Point){
    //                            zdrawinglite.Point point = (zdrawinglite.Point)s;
    //                            point.setSelected(true);
    //                        }
    //                    }
    //                }
    //            }
        } 
    }else if(tbShapeBSpline1.isSelected()){
        // On récupère les coordonnées sur le composant sheet.
        int xa = evt.getXOnScreen()-(int)sh.getLocationOnScreen().getX();
        int ya = evt.getYOnScreen()-(int)sh.getLocationOnScreen().getY();
        Layer lay = getCurrentLayer();
        
        // Si on clique sur le bouton 1 de la souris (le bouton gauche).
        if(evt.getButton()==1){
            //On fait cette action que si on n'a jamais rien fait d'autre (que la première fois)
            if (lay.getFirstPoint()==null){
                //first recupère les coordonnées du tout premier point qu'on pose.
                lay.setFirstPoint(new java.awt.Point(xa/scale,ya/scale));
                ReStart m = new ReStart(xa/scale, ya/scale, xa/scale, ya/scale);
                lay.getShapesList().addShape(m); lay.addRemember(m);
            }
            
            try{
                IShape s = lay.getShapesList().getLastShape();
                if(s instanceof BSpline){
                    //ne rien faire
                }else{
                    if(s!=null){
                        //on crée d'abord un point, car le ASS marche comme ça
                        Point p = new Point(
                                (int)lay.getFirstPoint().getX(), 
                                (int)lay.getFirstPoint().getY());
                        //on crée une bspline et on la configure
                        BSpline b = new BSpline(
                                (int)lay.getFirstPoint().getX(), 
                                (int)lay.getFirstPoint().getY());
                        //on ajoute tout à la liste
                        lay.getShapesList().addShape(p); lay.addRemember(p);
                        lay.getShapesList().addShape(b); lay.addRemember(b);
                    }
                }                
            }catch(Exception e){
                System.out.println(e.getMessage());
                //on crée d'abord un point, car le ASS marche comme ça
                        Point p = new Point(
                                (int)lay.getFirstPoint().getX(), 
                                (int)lay.getFirstPoint().getY());
                //on crée une bspline et on la configure                
                BSpline b = new BSpline(
                        (int)lay.getFirstPoint().getX(), 
                        (int)lay.getFirstPoint().getY());
                //on ajoute tout à la liste
                lay.getShapesList().addShape(p); lay.addRemember(p);
                lay.getShapesList().addShape(b); lay.addRemember(b);
            }           
            
            
            //on définit ce point comme dernier point posé
            lay.setLastPoint(new java.awt.Point(xa/scale,ya/scale));
            
            //si last et first ne sont pas à la même coordonnée
            if(isSamePoint(lay.getFirstPoint(),lay.getLastPoint())==false){
                IShape s = lay.getShapesList().getLastShape();
                if(s instanceof BSpline){
                    BSpline bs = (BSpline)s;
                        bs.addPoint(
                                (int)lay.getLastPoint().getX(), 
                                (int)lay.getLastPoint().getY());
                }else{
                    //nothing
                }
            }
            
            //montre la zone
            sh.updateGeneralPath(lay.getGeneralPath());
            sh.updateShapesList(lay.getShapesList());
        }
        
        //first prend les coordonnées de last afin que l'on puisse se souvenir
        //du dernier point posé lors d'un nouveau clic gauche de souris
        lay.setFirstPoint(lay.getLastPoint());
        //on met à jour les commandes de dessin ASS
        tfAssCommands.setText(updateCommands());
        updateRemember(lay);
    }else if(tbNextPoint.isSelected()){
        // On récupère les coordonnées sur le composant sheet.
        int xa = evt.getXOnScreen()-(int)sh.getLocationOnScreen().getX();
        int ya = evt.getYOnScreen()-(int)sh.getLocationOnScreen().getY();
        Layer lay = getCurrentLayer();
        
        // Si on clique sur le bouton 1 de la souris (le bouton gauche).
        if(evt.getButton()==1){
            IShape s = lay.getShapesList().getLastShape();
            if(s instanceof BSpline){
                BSpline bs = (BSpline)s;
                if(bs.isClosed()==false){
                    bs.setNextPoint(xa/scale, ya/scale);
                    bs.addPoint(xa/scale, ya/scale);
                    bs.setLastPoint(xa/scale, ya/scale);
                    //first prend les coordonnées de last afin que l'on puisse se souvenir
                    //du dernier point posé lors d'un nouveau clic gauche de souris
                    lay.setFirstPoint(bs.getLastPoint());
                }                
            }            
        }        
        //montre la zone
        sh.updateGeneralPath(lay.getGeneralPath());
        sh.updateShapesList(lay.getShapesList());
        //on met à jour les commandes de dessin ASS
        tfAssCommands.setText(updateCommands());
    }else if(tbMove.isSelected()){
        // On récupère les coordonnées sur le composant sheet.
        int xa = evt.getXOnScreen()-(int)sh.getLocationOnScreen().getX();
        int ya = evt.getYOnScreen()-(int)sh.getLocationOnScreen().getY();
        Layer lay = getCurrentLayer();
        
        // Si on clique sur le bouton 1 de la souris (le bouton gauche).
        if(evt.getButton()==1){
            //On fait cette action que si on n'a jamais rien fait d'autre (que la première fois)
            if (lay.getFirstPoint()==null){
                //first recupère les coordonnées du tout premier point qu'on pose.
                lay.setFirstPoint(new java.awt.Point(xa/scale,ya/scale));
                Move m = new Move(xa/scale, ya/scale, xa/scale, ya/scale);
                lay.getShapesList().addShape(m); lay.addRemember(m);
            }
            
            //on définit ce point comme dernier point posé
            lay.setLastPoint(new java.awt.Point(xa/scale,ya/scale));
            
            //si last et first ne sont pas à la même coordonnée
            if(isSamePoint(lay.getFirstPoint(),lay.getLastPoint())==false){
                //on crée une "ligne" (voir classe Line)
                Move m = new Move();
                //on la configure
                m.setOriginPoint(
                        (int)lay.getFirstPoint().getX(),
                        (int)lay.getFirstPoint().getY());
                m.setLastPoint(
                        (int)lay.getLastPoint().getX(), 
                        (int)lay.getLastPoint().getY());
                //on l'ajoute à la liste
                lay.getShapesList().addShape(m); lay.addRemember(m);
                //montre la zone
                sh.updateGeneralPath(lay.getGeneralPath());
                sh.updateShapesList(lay.getShapesList());
            }
        }
        //first prend les coordonnées de last afin que l'on puisse se souvenir
        //du dernier point posé lors d'un nouveau clic gauche de souris
        lay.setFirstPoint(lay.getLastPoint());
        //on met à jour les commandes de dessin ASS
        tfAssCommands.setText(updateCommands());
        updateRemember(lay);
    }else if(tbReStart.isSelected()){
        // On récupère les coordonnées sur le composant sheet.
        int xa = evt.getXOnScreen()-(int)sh.getLocationOnScreen().getX();
        int ya = evt.getYOnScreen()-(int)sh.getLocationOnScreen().getY();
        Layer lay = getCurrentLayer();
        
        // Si on clique sur le bouton 1 de la souris (le bouton gauche).
        if(evt.getButton()==1){
            //On fait cette action que si on n'a jamais rien fait d'autre (que la première fois)
            if (lay.getFirstPoint()==null){
                //first recupère les coordonnées du tout premier point qu'on pose.
                lay.setFirstPoint(new java.awt.Point(xa/scale,ya/scale));
                ReStart m = new ReStart(xa/scale, ya/scale, xa/scale, ya/scale);
                lay.getShapesList().addShape(m); lay.addRemember(m);
            }
            
            //on définit ce point comme dernier point posé
            lay.setLastPoint(new java.awt.Point(xa/scale,ya/scale));
            
            //si last et first ne sont pas à la même coordonnée
            if(isSamePoint(lay.getFirstPoint(),lay.getLastPoint())==false){
                //on crée une "ligne" (voir classe Line)
                ReStart m = new ReStart();
                //on la configure
                m.setOriginPoint(
                        (int)lay.getFirstPoint().getX(),
                        (int)lay.getFirstPoint().getY());
                m.setLastPoint(
                        (int)lay.getLastPoint().getX(), 
                        (int)lay.getLastPoint().getY());
                //on l'ajoute à la liste
                lay.getShapesList().addShape(m); lay.addRemember(m);
                //montre la zone
                sh.updateGeneralPath(lay.getGeneralPath());
                sh.updateShapesList(lay.getShapesList());
            }
        }
        //first prend les coordonnées de last afin que l'on puisse se souvenir
        //du dernier point posé lors d'un nouveau clic gauche de souris
        lay.setFirstPoint(lay.getLastPoint());
        //on met à jour les commandes de dessin ASS
        tfAssCommands.setText(updateCommands());
        updateRemember(lay);
    }else if(tbTranslation.isSelected()){
        if(tbNormalMode.isSelected()){
            Layer lay = getCurrentLayer();
            if(evt.getButton()==1 && lay.getTranslation().isSet()==false){
                int xa = evt.getXOnScreen()-(int)sh.getLocationOnScreen().getX();
                int ya = evt.getYOnScreen()-(int)sh.getLocationOnScreen().getY();
                lay.getTranslation().setTranslation(new java.awt.Point(xa, ya));
                sh.updateTranslation(lay.getTranslation());
            }
        }else if(tbOrnamentMode.isSelected()){
            List<Layer> layers = getLinkedLayers();
            for(Layer lay : layers){
                if(evt.getButton()==1 && lay.getTranslation().isSet()==false){
                    int xa = evt.getXOnScreen()-(int)sh.getLocationOnScreen().getX();
                    int ya = evt.getYOnScreen()-(int)sh.getLocationOnScreen().getY();
                    lay.getTranslation().setTranslation(new java.awt.Point(xa, ya));
                    sh.updateTranslation(lay.getTranslation());
                }
            }
        }
    }else if(tbRotation.isSelected()){
        if(tbNormalMode.isSelected()){
            Layer lay = getCurrentLayer();
            if(evt.getButton()==1 && lay.getCenter().isSet()==false){
                int xa = evt.getXOnScreen()-(int)sh.getLocationOnScreen().getX();
                int ya = evt.getYOnScreen()-(int)sh.getLocationOnScreen().getY();
                lay.getCenter().setCenter(xa, ya);
                sh.updateCenter(lay.getCenter());
            }
            if(evt.getButton()==2){
                RotationDialog rd = new RotationDialog(parentFrame, true);
                rd.setLangOKButton(strOK_Button);
                rd.setLangCancelButton(strCancel_Button);
                rd.setLangRedMessage(strRedMessage);
                rd.setLangMessage(strMessage);
                rd.setLocationRelativeTo(null);//Centre le fenêtre
                double angle = (double) rd.showDialog();
                lay.rotate(lay.getCenter().getX(), lay.getCenter().getY(), angle, lay);
                lay.getCenter().clear();
                sh.updateCenter(lay.getCenter());
                sh.updateDrawing();
                tfAssCommands.setText(updateCommands());
            }
        }else if(tbOrnamentMode.isSelected()){
            List<Layer> layers = getLinkedLayers();
            for(Layer lay : layers){
                if(evt.getButton()==1 && lay.getCenter().isSet()==false){
                    int xa = evt.getXOnScreen()-(int)sh.getLocationOnScreen().getX();
                    int ya = evt.getYOnScreen()-(int)sh.getLocationOnScreen().getY();
                    lay.getCenter().setCenter(xa, ya);
                    sh.updateCenter(lay.getCenter());
                }
            }
        }
    }else if(tbFreeDrawing.isSelected()){
        
    }else if(tbResize.isSelected()){
        if(tbNormalMode.isSelected()){
            Layer lay = getCurrentLayer();
            if(evt.getButton()==1 && lay.getResize().isSet()==false){
                int xa = evt.getXOnScreen()-(int)sh.getLocationOnScreen().getX();
                int ya = evt.getYOnScreen()-(int)sh.getLocationOnScreen().getY();
                lay.getResize().setResize(xa, ya);
                sh.updateResize(lay.getResize());
            }
        }else if(tbOrnamentMode.isSelected()){
            List<Layer> layers = getLinkedLayers();
            for(Layer lay : layers){
                if(evt.getButton()==1 && lay.getResize().isSet()==false){
                    int xa = evt.getXOnScreen()-(int)sh.getLocationOnScreen().getX();
                    int ya = evt.getYOnScreen()-(int)sh.getLocationOnScreen().getY();
                    lay.getResize().setResize(xa, ya);
                    sh.updateResize(lay.getResize());
                }
            }
        }
    }else if(tbShear.isSelected()){
        if(tbNormalMode.isSelected()){
            Layer lay = getCurrentLayer();
            if(evt.getButton()==1 && lay.getShear().isSet()==false){
                int xa = evt.getXOnScreen()-(int)sh.getLocationOnScreen().getX();
                int ya = evt.getYOnScreen()-(int)sh.getLocationOnScreen().getY();
                lay.getShear().setup(getCurrentLayer().getShapesList().getShapes());
                lay.getShear().setShearPoint(xa, ya);
                sh.updateShear(lay.getShear());
            }
        }else if(tbOrnamentMode.isSelected()){
            List<Layer> layers = getLinkedLayers();
            for(Layer lay : layers){
                if(evt.getButton()==1 && lay.getShear().isSet()==false){
                    int xa = evt.getXOnScreen()-(int)sh.getLocationOnScreen().getX();
                    int ya = evt.getYOnScreen()-(int)sh.getLocationOnScreen().getY();
                    lay.getShear().setup(getCurrentLayer().getShapesList().getShapes());
                    lay.getShear().setShearPoint(xa, ya);
                    sh.updateShear(lay.getShear());
                }
            }
        }
        
    }else if(tbOrnMMLine.isSelected()){
        int xa = evt.getXOnScreen()-(int)sh.getLocationOnScreen().getX();
        int ya = evt.getYOnScreen()-(int)sh.getLocationOnScreen().getY();
        if(evt.getButton()==1){//VOIR PLUS HAUT LE FONCTIONNEMENT - Même chose que Line
            if (ornlayForMain.getFirstPoint()==null){
                ornlayForMain.setFirstPoint(new java.awt.Point(xa/scale,ya/scale));                
                OrnPoint p = new OrnPoint(xa/scale, ya/scale);                
                ornlayForMain.addShape(p);
            }else{
                OrnPoint p = new OrnPoint(xa/scale, ya/scale);
                ornlayForMain.addShape(p);
            }
            sh.updateOrnamentForMain(ornlayForMain.getList());
            ornlayForMain.setLastPoint(new java.awt.Point(xa/scale,ya/scale));
            if(isSamePoint(ornlayForMain.getFirstPoint(),ornlayForMain.getLastPoint())==false){
                OrnMMLine l = new OrnMMLine(
                        ornlayForMain.getFirstPoint().x, ornlayForMain.getFirstPoint().y,
                        ornlayForMain.getLastPoint().x, ornlayForMain.getLastPoint().y);                
//                String value = JOptionPane.showInputDialog(this, "<html><b>Veuillez indiquer le temps de cette phase :</b>\n"
//                        + "- ne mettez rien pour que le temps soit égal au temps de la ligne de karaoké\n"
//                        + "- mettez le signe \"/\" suivit d'un nombre pour une fraction du temps"
//                        + ", exemple pour avoir la moitié du temps de la ligne mettez \"/2\"\n"
//                        + "- mettez un chiffre pour avoir ce temps précis\n"
//                        + "Notez bien que le temps est en millisecondes.");
//                if(value.isEmpty()){value = "0";}
                ornlayForMain.addShape(l);
                dtmOrnForMain.addRow(new Object[]{l,"1000"});
            }
            sh.updateOrnamentForMain(ornlayForMain.getList());
            ornlayForMain.setFirstPoint(ornlayForMain.getLastPoint());
        }
    }else if(tbOrnMMBezier.isSelected()){
        int xa = evt.getXOnScreen()-(int)sh.getLocationOnScreen().getX();
        int ya = evt.getYOnScreen()-(int)sh.getLocationOnScreen().getY();
        if(evt.getButton()==1){//VOIR PLUS HAUT LE FONCTIONNEMENT - Même chose que Line
            if (ornlayForMain.getFirstPoint()==null){
                ornlayForMain.setFirstPoint(new java.awt.Point(xa/scale,ya/scale));                
                OrnPoint p = new OrnPoint(xa/scale, ya/scale);                
                ornlayForMain.addShape(p);
            }else{
                OrnPoint p = new OrnPoint(xa/scale, ya/scale);
                ornlayForMain.addShape(p);
            }
            sh.updateOrnamentForMain(ornlayForMain.getList());
            ornlayForMain.setLastPoint(new java.awt.Point(xa/scale,ya/scale));
            if(isSamePoint(ornlayForMain.getFirstPoint(),ornlayForMain.getLastPoint())==false){
                OrnMMBezier b = new OrnMMBezier(
                        ornlayForMain.getFirstPoint().x, ornlayForMain.getFirstPoint().y,
                        ornlayForMain.getLastPoint().x, ornlayForMain.getLastPoint().y);                
                OrnControlPoint cp1 = b.getControl1();
                OrnControlPoint cp2 = b.getControl2();
//                String value = JOptionPane.showInputDialog(this, "<html><b>Veuillez indiquer le temps de cette phase :</b>\n"
//                        + "- ne mettez rien pour que le temps soit égal au temps de la ligne de karaoké\n"
//                        + "- mettez le signe \"/\" suivit d'un nombre pour une fraction du temps"
//                        + ", exemple pour avoir la moitié du temps de la ligne mettez \"/2\"\n"
//                        + "- mettez un chiffre pour avoir ce temps précis\n"
//                        + "Notez bien que le temps est en millisecondes.");
//                if(value.isEmpty()){value = "0";}
                ornlayForMain.addShape(b);
                ornlayForMain.addShape(cp1);
                ornlayForMain.addShape(cp2);
                dtmOrnForMain.addRow(new Object[]{b,"1000"});
            }
            sh.updateOrnamentForMain(ornlayForMain.getList());   
            ornlayForMain.setFirstPoint(ornlayForMain.getLastPoint());            
        }
    }
    //ON CONTRÔLE LA LISTE DES SHAPE POUR VOIR SI ON DOIT PERMETTRE
    //LE BOUTON CloseBSpline D'ÊTRE DISPONIBLE
    try{
        Layer lay = getCurrentLayer();
        IShape shape = lay.getShapesList().getLastShape();
        if(shape instanceof BSpline){
            btnCloseBSpline.setEnabled(true);
        }else{
            btnCloseBSpline.setEnabled(false);
        }
    }catch(Exception e){
        
    }    
    }//GEN-LAST:event_spSheetMouseClicked

    private void spSheetMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_spSheetMousePressed
        // QUAND ON APPUIE SUR UN BOUTON DE LA SOURIS
    if(tbShapeBezier.isSelected() | tbShapeLine.isSelected()
            | tbMove.isSelected() | tbReStart.isSelected()){
        //Prépare la mise à jour de la position des points proches du pointeur
        //quand on actionne le bouton du milieu de la souris afin de déplacer les formes.
        //on marque chaque élément concerné par un déplacment
        if(evt.getButton()==2){
            Layer lay = getCurrentLayer();
            int xa = evt.getXOnScreen()-(int)sh.getLocationOnScreen().getX();
            int ya = evt.getYOnScreen()-(int)sh.getLocationOnScreen().getY();
            java.awt.Point p2d = new java.awt.Point(xa/scale,ya/scale);
            lay.setChangelist(lay.getShapesList().getShapesAtPoint(p2d));
            for (IShape s : lay.getShapesList().getShapes()){
                for(IShape sb : lay.getChangelist()){
                    if (s.equals(sb)){
                        s.setMarked(true);
                        sb.setMarked(true);
                    }
                }
            }
        }
    }else if (tbShapeBSpline1.isSelected() | tbNextPoint.isSelected()){
        if(evt.getButton()==2){
            Layer lay = getCurrentLayer();
            int xa = evt.getXOnScreen()-(int)sh.getLocationOnScreen().getX();
            int ya = evt.getYOnScreen()-(int)sh.getLocationOnScreen().getY();
            java.awt.Point p2d = new java.awt.Point(xa/scale,ya/scale);
            lay.setChangelist(lay.getShapesList().getShapesAtPoint(p2d));
            for (IShape s : lay.getShapesList().getShapes()){
                for(IShape sb : lay.getChangelist()){
                    if (s.equals(sb)){
                        s.setMarked(true);
                        sb.setMarked(true);
                    }
                }
            }
        }
    }else if(tbTranslation.isSelected()){
//        if(evt.getButton()==1){
//            Layer lay = getCurrentLayer();
//            int xa = evt.getXOnScreen()-(int)sh.getLocationOnScreen().getX();
//            int ya = evt.getYOnScreen()-(int)sh.getLocationOnScreen().getY();
//            beforeTranslation = new java.awt.Point(xa,ya);
//        } 
        Layer lay = getCurrentLayer();
        if(tbNormalMode.isSelected() && lay.getTranslation().isSet()){
            if(evt.getButton()==1){
                int xa = evt.getXOnScreen()-(int)sh.getLocationOnScreen().getX();
                int ya = evt.getYOnScreen()-(int)sh.getLocationOnScreen().getY();
                lay.getTranslation().setDistance(xa, ya);
                lay.getTranslation().setTranslatonPreview(getCurrentLayer().getShapesList().getShapes());
                sh.updateTranslation(lay.getTranslation());
            }
        }else if(tbOrnamentMode.isSelected() && lay.getTranslation().isSet()){
            List<Layer> layers = getLinkedLayers();
            for(Layer lay2 : layers){
                if(evt.getButton()==1){
                    int xa = evt.getXOnScreen()-(int)sh.getLocationOnScreen().getX();
                    int ya = evt.getYOnScreen()-(int)sh.getLocationOnScreen().getY();
                    lay2.getTranslation().setDistance(xa, ya);
                    lay2.getTranslation().setTranslatonPreview(lay2.getShapesList().getShapes());
                    sh.updateTranslation(lay2.getTranslation());
                }
            }
        }
    }else if(tbFreeDrawing.isSelected()){
        
    }else if(tbSelection.isSelected() && evt.getButton()==1){
        // On récupère les coordonnées sur le composant sheet.
        int xa = evt.getXOnScreen()-(int)sh.getLocationOnScreen().getX();
        int ya = evt.getYOnScreen()-(int)sh.getLocationOnScreen().getY();
        selection.setStartPoint(xa, ya);
        selection.setEndPoint(xa, ya);
        sh.updateSelection(selection, false);
        sh.updateDrawing();
    }else if(tbRotation.isSelected()){
        Layer lay = getCurrentLayer();
        if(tbNormalMode.isSelected() && lay.getCenter().isSet()){
            if(evt.getButton()==1){
                int xa = evt.getXOnScreen()-(int)sh.getLocationOnScreen().getX();
                int ya = evt.getYOnScreen()-(int)sh.getLocationOnScreen().getY();
                lay.getCenter().setRotation(xa, ya);
                lay.getCenter().setRotationPreview(getCurrentLayer().getShapesList().getShapes(), 0d);
                sh.updateCenter(lay.getCenter());
            }
        }else if(tbOrnamentMode.isSelected() && lay.getCenter().isSet()){
            List<Layer> layers = getLinkedLayers();
            for(Layer lay2 : layers){
                if(evt.getButton()==1){
                    int xa = evt.getXOnScreen()-(int)sh.getLocationOnScreen().getX();
                    int ya = evt.getYOnScreen()-(int)sh.getLocationOnScreen().getY();
                    lay2.getCenter().setRotation(xa, ya);
                    lay2.getCenter().setRotationPreview(lay2.getShapesList().getShapes(), 0d);
                    sh.updateCenter(lay2.getCenter());
                }
            }
        }                
    }else if(tbResize.isSelected()){
        Layer lay = getCurrentLayer();
        if(tbNormalMode.isSelected() && lay.getResize().isSet()){
            if(evt.getButton()==1){
                int xa = evt.getXOnScreen()-(int)sh.getLocationOnScreen().getX();
                int ya = evt.getYOnScreen()-(int)sh.getLocationOnScreen().getY();
                lay.getResize().setDistance(xa, ya);
                lay.getResize().setResizePreview(getCurrentLayer().getShapesList().getShapes(), 0d);
                sh.updateResize(lay.getResize());
            }
        }else if(tbOrnamentMode.isSelected() && lay.getResize().isSet()){
            List<Layer> layers = getLinkedLayers();
            for(Layer lay2 : layers){
                if(evt.getButton()==1){
                    int xa = evt.getXOnScreen()-(int)sh.getLocationOnScreen().getX();
                    int ya = evt.getYOnScreen()-(int)sh.getLocationOnScreen().getY();
                    lay2.getResize().setDistance(xa, ya);
                    lay2.getResize().setResizePreview(lay2.getShapesList().getShapes(), 0d);
                    sh.updateResize(lay2.getResize());
                }
            }
        }
    }else if(tbShear.isSelected()){
        Layer lay = getCurrentLayer();
        if(tbNormalMode.isSelected() && lay.getShear().isSet()){
            if(evt.getButton()==1){
                int xa = evt.getXOnScreen()-(int)sh.getLocationOnScreen().getX();
                int ya = evt.getYOnScreen()-(int)sh.getLocationOnScreen().getY();
                lay.getShear().setMovePoint(xa, ya);
                lay.getShear().setShearPreview(getCurrentLayer().getShapesList().getShapes());
                sh.updateShear(lay.getShear());
            }
        }else if(tbOrnamentMode.isSelected() && lay.getShear().isSet()){
            List<Layer> layers = getLinkedLayers();
            for(Layer lay2 : layers){
                if(evt.getButton()==1){
                    int xa = evt.getXOnScreen()-(int)sh.getLocationOnScreen().getX();
                    int ya = evt.getYOnScreen()-(int)sh.getLocationOnScreen().getY();
                    lay2.getShear().setMovePoint(xa, ya);
                    lay2.getShear().setShearPreview(lay2.getShapesList().getShapes());
                    sh.updateShear(lay2.getShear());
                }
            }
        }        
    }else if(tbOrnMMLine.isSelected() | tbOrnMMBezier.isSelected()){
        if(evt.getButton()==2){
            int xa = evt.getXOnScreen()-(int)sh.getLocationOnScreen().getX();
            int ya = evt.getYOnScreen()-(int)sh.getLocationOnScreen().getY();
            java.awt.Point p2d = new java.awt.Point(xa/scale,ya/scale);
            ornlayForMain.setChangeList(ornlayForMain.getShapesAtPoint(p2d));
            for (feuille.drawing.ornament.IShape s : ornlayForMain.getList()){
                for (feuille.drawing.ornament.IShape sb : ornlayForMain.getChangeList()) {
                    if (s.equals(sb)){
                        s.setMarked(true);
                        sb.setMarked(true);
                    }
                }
            }
        }
    }
    }//GEN-LAST:event_spSheetMousePressed

    private void spSheetMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_spSheetMouseReleased
        // QUAND ON RELACHE UN BOUTON DE LA SOURIS
    if(tbShapeBezier.isSelected() | tbShapeLine.isSelected()
            | tbShapeBSpline1.isSelected() | tbNextPoint.isSelected()
            | tbMove.isSelected() | tbReStart.isSelected()){
        //Met à jour la position des points et des lignes/courbes.
        Layer lay = getCurrentLayer();
        if(evt.getButton()==2 && lay.getChangelist()!=null){
            int xa = evt.getXOnScreen()-(int)sh.getLocationOnScreen().getX();
            int ya = evt.getYOnScreen()-(int)sh.getLocationOnScreen().getY();
            java.awt.Point p2d = new java.awt.Point(xa/scale,ya/scale);
            for (IShape s : lay.getShapesList().getShapes()){
                if(s.getMarked()==true){
                    s.setMarked(false);
                    if(s instanceof Point){
                        Point point = (Point)s;
                        point.updatePointPosition(p2d);
                    }else if(s instanceof Line){
                        Line line = (Line)s;
                        line.updatePointPosition(p2d);
                        line.updatehasEnded();
                    }else if(s instanceof ControlPoint){
                        ControlPoint cp = (ControlPoint)s;
                        cp.updatePointPosition(p2d);
                    }else if(s instanceof Bezier){
                        Bezier b = (Bezier)s;
                        b.updatePointPosition(p2d);
                        b.updatehasEnded();
                    }else if(s instanceof BSpline){
                        BSpline b = (BSpline)s;                        
                        b.updatePointPosition(p2d);
                        b.updateLastUsedControlPoint();
                        b.updatehasEnded();
                    }else if(s instanceof Move){
                        Move move = (Move)s;
                        move.updatePointPosition(p2d);
                        move.updatehasEnded();
                    }else if(s instanceof ReStart){
                        ReStart move = (ReStart)s;
                        move.updatePointPosition(p2d);
                        move.updatehasEnded();
                    }
                }
            }
            //montre la zone
            sh.updateGeneralPath(lay.getGeneralPath());
            //on met à jour la liste de sheet
            sh.updateShapesList(lay.getShapesList());
            //on vide changelist
            lay.setChangelist(null);
            //on met à jour les commandes ASS
            tfAssCommands.setText(updateCommands());
        }
    }else if(tbFreeDrawing.isSelected()){
        
    }else if(tbFreeClear.isSelected()){
        
    }else if(tbTranslation.isSelected()){
        Layer lay = getCurrentLayer();
        if(tbNormalMode.isSelected() && lay.getTranslation().isSet()){
            if(evt.getButton()==1){
                lay.translate(lay.getShapesList().getShapes(), lay.getTranslation().getDX(), lay.getTranslation().getDY(), lay);
                lay.getTranslation().clear();
                sh.updateTranslation(lay.getTranslation());
                fixCommands();
            }
        }else if(tbOrnamentMode.isSelected() && lay.getTranslation().isSet()){
            List<Layer> layers = getLinkedLayers();
            for(Layer lay2 : layers){
                if(evt.getButton()==1){
                    lay2.translate(lay2.getShapesList().getShapes(), lay2.getTranslation().getDX(), lay2.getTranslation().getDY(), lay2);
                    lay2.getTranslation().clear();
                    sh.updateTranslation(lay2.getTranslation());
                    fixCommands();
                }
            }
        }
    }else if(tbSelection.isSelected() && evt.getButton()==1){
        // On récupère les coordonnées sur le composant sheet.
        int xa = evt.getXOnScreen()-(int)sh.getLocationOnScreen().getX();
        int ya = evt.getYOnScreen()-(int)sh.getLocationOnScreen().getY();
        selection.setEndPoint(xa, ya);
        Layer lay = getCurrentLayer();
        selection.searchForShapes(lay.getShapesList());
        selection.clear();
        sh.updateSelection(selection, false);
        sh.updateDrawing();
    }else if(tbRotation.isSelected()){
        Layer lay = getCurrentLayer();
        if(tbNormalMode.isSelected() && lay.getCenter().isSet()){
            if(evt.getButton()==1){
                lay.rotate(lay.getCenter().getX(), lay.getCenter().getY(), lay.getCenter().getAngle(), lay);
                lay.getCenter().clear();
                sh.updateCenter(lay.getCenter());
                fixCommands();
            }
        }else if(tbOrnamentMode.isSelected() && lay.getCenter().isSet()){
            List<Layer> layers = getLinkedLayers();
            for(Layer lay2 : layers){
                if(evt.getButton()==1){
                    lay2.rotate(lay2.getCenter().getX(), lay2.getCenter().getY(), lay2.getCenter().getAngle(), lay2);
                    lay2.getCenter().clear();
                    sh.updateCenter(lay2.getCenter());
                    fixCommands();
                }
            }
        }    
    }else if(tbResize.isSelected()){
        Layer lay = getCurrentLayer();
        if(tbNormalMode.isSelected() && lay.getResize().isSet()){
            if(evt.getButton()==1){
                lay.resize(lay.getResize().getReStartPoint(lay.getShapesList().getShapes()), lay.getResize().getPercent(), lay);
                lay.getResize().clear();
                sh.updateResize(lay.getResize());
                fixCommands();
            }
        }else if(tbOrnamentMode.isSelected() && lay.getResize().isSet()){
            List<Layer> layers = getLinkedLayers();
            for(Layer lay2 : layers){
                if(evt.getButton()==1){
                    lay2.resize(lay.getResize().getReStartPoint(lay2.getShapesList().getShapes()), lay2.getResize().getPercent(), lay2);
                    lay2.getResize().clear();
                    sh.updateResize(lay2.getResize());
                    fixCommands();
                }
            }
        }        
    }else if(tbShear.isSelected()){
        Layer lay = getCurrentLayer();
        if(tbNormalMode.isSelected() && lay.getShear().isSet()){
            if(evt.getButton()==1){
                //TODO Preview
                lay.getShear().clear();
                sh.updateShear(lay.getShear());
                //fixCommands();
            }
        }else if(tbOrnamentMode.isSelected() && lay.getShear().isSet()){
            List<Layer> layers = getLinkedLayers();
            for(Layer lay2 : layers){
                if(evt.getButton()==1){
                    //TODO Preview
                    lay2.getShear().clear();
                    sh.updateShear(lay2.getShear());
                    //fixCommands();
                }
            }
        }
    }else if(tbOrnMMLine.isSelected() | tbOrnMMBezier.isSelected()){
        if(evt.getButton()==2 && ornlayForMain.getChangeList()!=null){
            int xa = evt.getXOnScreen()-(int)sh.getLocationOnScreen().getX();
            int ya = evt.getYOnScreen()-(int)sh.getLocationOnScreen().getY();
            java.awt.Point p2d = new java.awt.Point(xa/scale,ya/scale);
            for (feuille.drawing.ornament.IShape s : ornlayForMain.getList()) {
                if(s.getMarked()==true){
                    s.setMarked(false);
                    if(s instanceof OrnPoint){
                        OrnPoint point = (OrnPoint)s;
                        point.updatePointPosition(p2d);
                    }else if(s instanceof OrnMMLine){
                        OrnMMLine line = (OrnMMLine)s;
                        line.updatePointPosition(p2d);
                        line.updatehasEnded();
                    }else if(s instanceof OrnControlPoint){
                        OrnControlPoint cp = (OrnControlPoint)s;
                        cp.updatePointPosition(p2d);
                    }else if(s instanceof OrnMMBezier){
                        OrnMMBezier b = (OrnMMBezier)s;
                        b.updatePointPosition(p2d);
                        b.updatehasEnded();
                    }else if(s instanceof BSpline){
//                        BSpline b = (BSpline)s;                        
//                        b.updatePointPosition(p2d);
//                        b.updateLastUsedControlPoint();
//                        b.updatehasEnded();
                    }
                }
            }            
            sh.updateOrnamentForMain(ornlayForMain.getList());//on met à jour la liste de sheet
            ornlayForMain.setChangeList(null);//on vide changelist
        }
    }        
    }//GEN-LAST:event_spSheetMouseReleased

    private void spSheetMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_spSheetMouseDragged
        // QUAND ON DEPLACE LA SOURIS AVEC UN BOUTON ENFONCE (LE BOUTON GAUCHE)
    if(tbShapeBezier.isSelected() | tbShapeLine.isSelected() 
            | tbMove.isSelected() | tbReStart.isSelected()
            | tbShapeBSpline1.isSelected() | tbNextPoint.isSelected()){
        // Si changelist n'est pas vide alors il y a modification en cours
        Layer lay = getCurrentLayer();
        if(lay.getChangelist()!=null){
            // On récupère les coordonnées sur le composant sheet.
            int xa = evt.getXOnScreen()-(int)sh.getLocationOnScreen().getX();
            int ya = evt.getYOnScreen()-(int)sh.getLocationOnScreen().getY();
            java.awt.Point p2d = new java.awt.Point(xa/scale,ya/scale);

            //cette méthode ne sert plus à rien, car on ne veut pas n'importe quelle Shape
            //            if(isNear(p2d,slist.getLastShape().getLastPoint2D())){
            //                first=new Point2D.Double(xa,ya);
            //            }

            //ayant besoin que du type "point" (de Shape) on récupère le dernier "point" posé
            //on récupère d'abord avec la méthode getLastPoint de la liste le "point"
            //puis avec l'autre méthode getLastPoint de "point", on récupère le java.awt.Point
            if(tbShapeBezier.isSelected() | tbShapeLine.isSelected() 
            | tbMove.isSelected() | tbReStart.isSelected()){
                if(isNear(p2d,lay.getShapesList().getLastPoint().getLastPoint())){
                    lay.setFirstPoint(new java.awt.Point(xa/scale,ya/scale));
                }
            }else{//tbShapeBSpline1.isSelected() | tbNextPoint.isSelected()
                if(isNear(p2d,lay.getShapesList().getLastShape().getOriginPoint())){
                    lay.setFirstPoint(new java.awt.Point(xa/scale,ya/scale));
                }
            }            

            //on met à jour les coordonnées des éléments qui ont été marqués
            for (IShape s : lay.getShapesList().getShapes()){
                if(s.getMarked()==true){
                    if(s instanceof Point){
                        Point point = (Point)s;
                        point.updatePointPosition(p2d);
                    }else if(s instanceof Line){
                        Line line = (Line)s;
                        line.updatePointPosition(p2d);
                    }else if(s instanceof ControlPoint){
                        ControlPoint cp = (ControlPoint)s;
                        cp.updatePointPosition(p2d);
                    }else if(s instanceof Bezier){
                        Bezier b = (Bezier)s;
                        b.updatePointPosition(p2d);
                    }else if(s instanceof Move){
                        Move m = (Move)s;
                        m.updatePointPosition(p2d);
                    }else if(s instanceof ReStart){
                        ReStart m = (ReStart)s;
                        m.updatePointPosition(p2d);
                    }else if(s instanceof BSpline){
                        BSpline bs = (BSpline)s;
                        bs.updatePointPosition(p2d);
                        bs.updateLastUsedControlPoint();
                    }
                }
            }
            //on met à jour les coordonnées dans cette méthode afin de voir les traits
            //vertical et horizontal lorsqu'on bouge la souris
            sh.updateMousePosition(xa/scale, ya/scale);
            //montre la zone
            sh.updateGeneralPath(lay.getGeneralPath());
            //on met à jour le dessin
            sh.updateDrawing();
            //on met à jour l'affichage des coordonnées
            int xb = xa/scale-(sh.getWidth()/scale)/2;
            int yb = ya/scale-(sh.getHeight()/scale)/2;
            lblCoordinates.setText("<html><h2>"+xb+";"+yb);
            //on met à jour les commandes ASS
            tfAssCommands.setText(updateCommands());
        }
    }else if(tbTranslation.isSelected()){
//        tfAssCommands.setText(updateCommands()); //On s'assure d'avoir les bonnes commandes
//        String commands = tfAssCommands.getText(); //On récupère les commandes        
//        // On récupère les coordonnées sur le composant sheet.
//        int xa = evt.getXOnScreen()-(int)sh.getLocationOnScreen().getX();
//        int ya = evt.getYOnScreen()-(int)sh.getLocationOnScreen().getY();
//        Layer lay = getCurrentLayer();
//        int xDiff = xa-(int)beforeTranslation.getX();
//        int yDiff = ya-(int)beforeTranslation.getY();
//        shapesFromCommands(commands, lay, xDiff, yDiff,null,0);
//        //on met à jour les commandes ASS
//        beforeTranslation = new java.awt.Point(xa,ya);
//        tfAssCommands.setText(updateCommands());
        Layer lay = getCurrentLayer();
        if(tbNormalMode.isSelected() && lay.getTranslation().isSet()){
            int xa = evt.getXOnScreen()-(int)sh.getLocationOnScreen().getX();
            int ya = evt.getYOnScreen()-(int)sh.getLocationOnScreen().getY();
            lay.getTranslation().setDistance(xa, ya);
            lay.getTranslation().setTranslatonPreview(lay.getShapesList().getShapes());
            sh.updateTranslation(lay.getTranslation());
        }else if(tbOrnamentMode.isSelected() && lay.getTranslation().isSet()){
            List<Layer> layers = getLinkedLayers();
            List<Translation> translations = new ArrayList<Translation>();
            for(Layer lay2 : layers){
                int xa = evt.getXOnScreen()-(int)sh.getLocationOnScreen().getX();
                int ya = evt.getYOnScreen()-(int)sh.getLocationOnScreen().getY();
                lay2.getTranslation().setDistance(xa, ya);
                lay2.getTranslation().setTranslatonPreview(lay2.getShapesList().getShapes());
//                sh.updateCenter(lay2.getCenter());
                translations.add(lay2.getTranslation());
            }
            sh.updateTranslation(translations);
        }
    }else if(tbFreeDrawing.isSelected()){
        // On récupère les coordonnées sur le composant sheet.
        int xa = evt.getXOnScreen()-(int)sh.getLocationOnScreen().getX();
        int ya = evt.getYOnScreen()-(int)sh.getLocationOnScreen().getY();
        graDraft.fillOval((xa/scale)-(penSizeUnit/2), (ya/scale)-(penSizeUnit/2), penSizeUnit, penSizeUnit);
        graDraft.setColor(penColor);
        sh.updateDraft(imgDraft);
        sh.updateDrawing();
    }else if(tbFreeClear.isSelected()){
        // On récupère les coordonnées sur le composant sheet.
        int xa = evt.getXOnScreen()-(int)sh.getLocationOnScreen().getX();
        int ya = evt.getYOnScreen()-(int)sh.getLocationOnScreen().getY();
        Composite originalComposite = graDraft.getComposite();
        AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f);
        graDraft.setComposite(composite);
        graDraft.setColor(new Color(0, 0, 0, 0));
        graDraft.fillOval((xa/scale)-(rubberSizeUnit/2), (ya/scale)-(rubberSizeUnit/2), rubberSizeUnit, rubberSizeUnit);
        graDraft.setComposite(originalComposite);
        sh.updateDraft(imgDraft);
        sh.updateDrawing();
    }else if(tbSelection.isSelected()){
        // On récupère les coordonnées sur le composant sheet.
        int xa = evt.getXOnScreen()-(int)sh.getLocationOnScreen().getX();
        int ya = evt.getYOnScreen()-(int)sh.getLocationOnScreen().getY();
        selection.setEndPoint(xa, ya);
        sh.updateSelection(selection, false);
        sh.updateDrawing();
    }else if(tbRotation.isSelected()){
        Layer lay = getCurrentLayer();
        if(tbNormalMode.isSelected() && lay.getCenter().isSet()){
            int xa = evt.getXOnScreen()-(int)sh.getLocationOnScreen().getX();
            int ya = evt.getYOnScreen()-(int)sh.getLocationOnScreen().getY();
            lay.getCenter().setRotation(xa, ya);
            lay.getCenter().setRotationPreview(lay.getShapesList().getShapes(), 0d);
            sh.updateCenter(lay.getCenter());
        }else if(tbOrnamentMode.isSelected() && lay.getCenter().isSet()){
            List<Layer> layers = getLinkedLayers();
            List<Center> centers = new ArrayList<Center>();
            for(Layer lay2 : layers){
                int xa = evt.getXOnScreen()-(int)sh.getLocationOnScreen().getX();
                int ya = evt.getYOnScreen()-(int)sh.getLocationOnScreen().getY();
                lay2.getCenter().setRotation(xa, ya);
                lay2.getCenter().setRotationPreview(lay2.getShapesList().getShapes(), 0d);
//                sh.updateCenter(lay2.getCenter());
                centers.add(lay2.getCenter());
            }
            sh.updateCenter(centers);
        }        
    }else if(tbResize.isSelected()){
        Layer lay = getCurrentLayer();
        if(tbNormalMode.isSelected() && lay.getResize().isSet()){
            int xa = evt.getXOnScreen()-(int)sh.getLocationOnScreen().getX();
            int ya = evt.getYOnScreen()-(int)sh.getLocationOnScreen().getY();
            lay.getResize().setDistance(xa, ya);
            lay.getResize().setResizePreview(lay.getShapesList().getShapes(), 0d);
            sh.updateResize(lay.getResize());
        }else if(tbOrnamentMode.isSelected() && lay.getResize().isSet()){
            List<Layer> layers = getLinkedLayers();
            List<Resize> resizes = new ArrayList<Resize>();
            for(Layer lay2 : layers){
                int xa = evt.getXOnScreen()-(int)sh.getLocationOnScreen().getX();
                int ya = evt.getYOnScreen()-(int)sh.getLocationOnScreen().getY();
                lay2.getResize().setDistance(xa, ya);
                lay2.getResize().setResizePreview(lay2.getShapesList().getShapes(), 0d);
//                sh.updateResize(lay2.getResize());
                resizes.add(lay2.getResize());
            }
            sh.updateResize(resizes);
        }        
    }else if(tbShear.isSelected()){
        Layer lay = getCurrentLayer();
        if(tbNormalMode.isSelected() && lay.getShear().isSet()){
            int xa = evt.getXOnScreen()-(int)sh.getLocationOnScreen().getX();
            int ya = evt.getYOnScreen()-(int)sh.getLocationOnScreen().getY();
            lay.getShear().setShearPoint(xa, ya);
            lay.getShear().setShearPreview(lay.getShapesList().getShapes());
            sh.updateShear(lay.getShear());
        }else if(tbOrnamentMode.isSelected() && lay.getShear().isSet()){
            List<Layer> layers = getLinkedLayers();
            List<Shear> shears = new ArrayList<Shear>();
            for(Layer lay2 : layers){
                int xa = evt.getXOnScreen()-(int)sh.getLocationOnScreen().getX();
                int ya = evt.getYOnScreen()-(int)sh.getLocationOnScreen().getY();
                lay2.getShear().setShearPoint(xa, ya);
                lay2.getShear().setShearPreview(lay2.getShapesList().getShapes());
//                sh.updateShear(lay2.getShear());
                shears.add(lay2.getShear());
            }
            sh.updateShear(shears);
        }
    }else if(tbOrnMMLine.isSelected() | tbOrnMMBezier.isSelected()){
        if(ornlayForMain.getChangeList()!=null){
            int xa = evt.getXOnScreen()-(int)sh.getLocationOnScreen().getX();
            int ya = evt.getYOnScreen()-(int)sh.getLocationOnScreen().getY();
            java.awt.Point p2d = new java.awt.Point(xa/scale,ya/scale);
            if(tbOrnMMLine.isSelected() | tbOrnMMBezier.isSelected()){
                if(ornlayForMain.getLastPointOfShapes().getLastPoint()!=null){
                    if(isNear(p2d,ornlayForMain.getLastPointOfShapes().getLastPoint())){
                        ornlayForMain.setFirstPoint(new java.awt.Point(xa/scale,ya/scale));
                    }
                }                
            }else{//tbShapeBSpline1.isSelected() | tbNextPoint.isSelected()
//                if(isNear(p2d,lay.getShapesList().getLastShape().getOriginPoint())){
//                    lay.setFirstPoint(new java.awt.Point(xa/scale,ya/scale));
//                }
            }
            
            //on met à jour les coordonnées des éléments qui ont été marqués
            for (feuille.drawing.ornament.IShape s : ornlayForMain.getList()){
                if(s.getMarked()==true){
                    if(s instanceof OrnPoint){
                        OrnPoint point = (OrnPoint)s;
                        point.updatePointPosition(p2d);
                    }else if(s instanceof OrnMMLine){
                        OrnMMLine line = (OrnMMLine)s;
                        line.updatePointPosition(p2d);
                    }else if(s instanceof OrnControlPoint){
                        OrnControlPoint cp = (OrnControlPoint)s;
                        cp.updatePointPosition(p2d);
                    }else if(s instanceof OrnMMBezier){
                        OrnMMBezier b = (OrnMMBezier)s;
                        b.updatePointPosition(p2d);
                    }else if(s instanceof BSpline){
//                        BSpline bs = (BSpline)s;
//                        bs.updatePointPosition(p2d);
//                        bs.updateLastUsedControlPoint();
                    }
                }
            }
            //on met à jour les coordonnées dans cette méthode afin de voir les traits
            //vertical et horizontal lorsqu'on bouge la souris
            sh.updateMousePosition(xa/scale, ya/scale);
            sh.updateOrnamentForMain(ornlayForMain.getList());
            //on met à jour l'affichage des coordonnées
            int xb = xa/scale-(sh.getWidth()/scale)/2;
            int yb = ya/scale-(sh.getHeight()/scale)/2;
            lblCoordinates.setText("<html><h2>"+xb+";"+yb);
        }
    }
    }//GEN-LAST:event_spSheetMouseDragged

    private void spSheetMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_spSheetMouseMoved
        // QUAND ON DEPLACE LA SOURIS
    if(tbShapeBezier.isSelected() | tbShapeLine.isSelected()
            | tbFreeDrawing.isSelected() | tbFreeClear.isSelected()
            | tbShapeBSpline1.isSelected() | tbNextPoint.isSelected()
            | tbMove.isSelected() | tbTranslation.isSelected()
            | tbReStart.isSelected() | tbSelection.isSelected()
            | tbRotation.isSelected() | tbResize.isSelected()
            | tbShear.isSelected() | tbOrnMMBSpline.isSelected()
            | tbOrnMMBezier.isSelected() | tbOrnMMLine.isSelected()){
        // On récupère les coordonnées sur le composant sheet.
        int xa = evt.getXOnScreen()-(int)sh.getLocationOnScreen().getX();
        int ya = evt.getYOnScreen()-(int)sh.getLocationOnScreen().getY();
        //on met à jour les coordonnées dans cette méthode afin de voir les traits
        //vertical et horizontal lorsqu'on bouge la souris
        sh.updateMousePosition(xa/scale, ya/scale);
        //on met à jour le dessin
        sh.updateDrawing();
        //on met à jour l'affichage des coordonnées
        int xb = xa/scale-(sh.getWidth()/scale)/2;
        int yb = ya/scale-(sh.getHeight()/scale)/2;
        lblCoordinates.setText("<html><h2>"+xb+";"+yb);
    }
    }//GEN-LAST:event_spSheetMouseMoved

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Réinitialise la zone de dessin en supprimant tout.
    int count = 0;
    for(Object o : dlm.toArray()){
        if(o instanceof Layer){
            Layer lay = (Layer)o;
            lay.getShapesList().removeAllShapes();
            lay.setFirstPoint(null);
            if(count!=0){
                dlm.removeElement(o);
            }
            count += 1;
        }
    }
    img = null;
    sh.updateImage(img);
    sh.updateImageRealPosition(0,0);
    sh.updateGeneralPath(null);    
    int size = setSizeOfDrawing();
    imgDraft = new BufferedImage(size,size,BufferedImage.TYPE_INT_ARGB);
    graDraft = (Graphics2D)imgDraft.getGraphics();
    graDraft.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
    //On laisse la zone transparente donc on n'excute plus le code suivant :
    //graDraft.setColor(Color.white);
    //graDraft.fillRect(0, 0, size, size);
    sh.updateDraft(imgDraft);    
    sh.updateDrawing();
    tfAssCommands.setText("");
    lstLayer.setSelectedIndex(0);
    getCurrentLayer().clearRemembers();
    dlmRemember.clear();
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenActionPerformed
        // Ouvre une image au format *.adf
    for (FileFilter f : fcZDrawing.getChoosableFileFilters()){
        fcZDrawing.removeChoosableFileFilter(f);
    }
    fcZDrawing.setAccessory(new DrawingPreview(fcZDrawing));
    fcZDrawing.addChoosableFileFilter(new DrawingFilter());
    fcZDrawing.addChoosableFileFilter(new SVGFilter());    
    int z = fcZDrawing.showOpenDialog(this);
    if (z == JFileChooser.APPROVE_OPTION){
        String path = fcZDrawing.getSelectedFile().getAbsolutePath();
        if(path.endsWith(".svg")){
            openSVGFile(path);
        }else if(path.endsWith(".adf")){
            openDrawingFile2(path);
        }
//        String commands = openDrawingFile(
//                fcZDrawing.getSelectedFile().getAbsolutePath());
        // Méthode 1 :
//        openDrawingFile(fcZDrawing.getSelectedFile().getAbsolutePath());
//        String pathJPG = fcZDrawing.getSelectedFile().getAbsolutePath().replace(".adf", ".jpg");
//        File fJPG = new File(pathJPG);
//        if(fJPG.exists()){
//            try {
//                imgDraft = ImageIO.read(fJPG);
//                graDraft = (Graphics2D)imgDraft.getGraphics();
//                graDraft.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//                        RenderingHints.VALUE_ANTIALIAS_ON);
//                sh.updateDraft(imgDraft);
//                sh.updateDrawing();
//            } catch (IOException ex) {
////                ex.printStackTrace();
//            }
//        }
        // Méthode 2 :
//        openDrawingFile2(path);
        //tfAssCommands.setText(commands);
//        try {
//            shapesFromCommands(commands,null);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
    }
    }//GEN-LAST:event_btnOpenActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Sauvegarde les formes de la planche à dessin.
    for (FileFilter f : fcZDrawing.getChoosableFileFilters()){
        fcZDrawing.removeChoosableFileFilter(f);
    }
    fcZDrawing.setAccessory(null);
    fcZDrawing.addChoosableFileFilter(new DrawingFilter());
    int z = fcZDrawing.showSaveDialog(this);
    if (z == JFileChooser.APPROVE_OPTION){
        String path = fcZDrawing.getSelectedFile().getAbsolutePath();
        if(path.endsWith(".adf")==false){path = path + ".adf";}
        // Méthode 1 :
//        saveDrawingFile(path);
//        String pathJPG = path.replace(".adf", ".jpg");
//        try {
//            ImageIO.write(imgDraft, "JPG", new File(pathJPG));
//        } catch (FileNotFoundException ex) {
////            ex.printStackTrace();
//        } catch (IOException ex) {
////            ex.printStackTrace();
//        }
        // Méthode 2 :
        saveDrawingFile2(path);
    }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnOpenFontsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenFontsActionPerformed
        OpenGlyphDialog ogd = new OpenGlyphDialog(frame, true);
        boolean val = ogd.showDialog();
        if(val==true){
            Font f = ogd.getGlyphFont().deriveFont((float)ogd.getGlyphSize());
            Layer lay = getCurrentLayer();
            lay.glyphToShape(ogd.getGlyph(), f);
            //montre la zone
            sh.updateGeneralPath(lay.getGeneralPath());
            sh.updateShapesList(lay.getShapesList());
            //on met à jour les commandes de dessin ASS
            tfAssCommands.setText(updateCommands());
            //on met à jour les remember
            updateRemember(lay);
        }
    }//GEN-LAST:event_btnOpenFontsActionPerformed

    private void btnUnloadImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUnloadImageActionPerformed
        // Réinitialise la zone de dessin en l'image.
    img = null;
    sh.updateImage(img);
    sh.updateImageRealPosition(0,0);
    sh.updateDrawing();
    }//GEN-LAST:event_btnUnloadImageActionPerformed

    private void btnImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImageActionPerformed
        //Ouvre une image d'arrière-plan et change l'état des boutons de déplacement.
    for (FileFilter f : fcZDrawing.getChoosableFileFilters()){
        fcZDrawing.removeChoosableFileFilter(f);
    }
    fcZDrawing.setDialogTitle("Ouvrir une image...");
    fcZDrawing.setAccessory(new ImagePreview(fcZDrawing));
    fcZDrawing.setDialogType(JFileChooser.OPEN_DIALOG);
    fcZDrawing.setFileFilter(new ImageFilter());
    int z = fcZDrawing.showOpenDialog(this);
    if (z == JFileChooser.APPROVE_OPTION){
        img = new ImageIcon(fcZDrawing.getSelectedFile().getAbsolutePath());
        lastImage = fcZDrawing.getSelectedFile().getAbsolutePath();
        sh.updateImage(img);
        sh.updateDrawing();
    }
    }//GEN-LAST:event_btnImageActionPerformed

    private void btnTopImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTopImageActionPerformed
        // Déplacement de l'image d'arrière-plan vers le haut.
    sh.updateImagePosition(0, -imageMoveUnit);
    sh.updateDrawing();
    }//GEN-LAST:event_btnTopImageActionPerformed

    private void btnLeftImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLeftImageActionPerformed
        // Déplacement de l'image d'arrière-plan vers la gauche.
    sh.updateImagePosition(-imageMoveUnit, 0);
    sh.updateDrawing();
    }//GEN-LAST:event_btnLeftImageActionPerformed

    private void btnCentreImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCentreImageActionPerformed
        if(img!=null){
        int x = sh.getWidth()/2 - img.getIconWidth()/2;
        int y = sh.getHeight()/2 - img.getIconHeight()/2;
        sh.updateImageRealPosition(x, y);
        sh.updateDrawing();
    }
    }//GEN-LAST:event_btnCentreImageActionPerformed

    private void btnRightImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRightImageActionPerformed
        // Déplacement de l'image d'arrière-plan vers la droite.
    sh.updateImagePosition(imageMoveUnit, 0);
    sh.updateDrawing();
    }//GEN-LAST:event_btnRightImageActionPerformed

    private void btnBottomImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBottomImageActionPerformed
        // Déplacement de l'image d'arrière-plan vers le bas.
    sh.updateImagePosition(0, imageMoveUnit);
    sh.updateDrawing();
    }//GEN-LAST:event_btnBottomImageActionPerformed

    private void slideImageAlphaStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_slideImageAlphaStateChanged
        //Change la transparence de l'image d'arrière-plan.
    Float f = slideImageAlpha.getValue()/100f;
    sh.updateImageTransparency(f);
    sh.updateDrawing();
    lblImageAlpha.setText(slideImageAlpha.getValue()+"%");
    }//GEN-LAST:event_slideImageAlphaStateChanged

    private void slideDrawingAlphaStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_slideDrawingAlphaStateChanged
        float fgp = slideDrawingAlpha.getValue()/100f;
    sh.updateGeneralPathTransparency(fgp); sh.updateDrawing();
    lblDrawingAlpha.setText(slideDrawingAlpha.getValue()+"%");
    }//GEN-LAST:event_slideDrawingAlphaStateChanged

    private void slideScaleStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_slideScaleStateChanged
        Layer lay = getCurrentLayer();
    switch(slideScale.getValue()){
        case 1:
            updateThickness(Thickness.Big);
            sh.updateThickness(Sheet.Thickness.Big);
            for(IShape s : lay.getShapesList().getShapes()){
                if(s instanceof Line){
                    Line l = (Line)s; l.updateThickness(Line.Thickness.Big);
                }else if(s instanceof Bezier){
                    Bezier b = (Bezier)s; b.updateThickness(Bezier.Thickness.Big);
                }else if(s instanceof Point){
                    Point p = (Point)s; p.updateThickness(Point.Thickness.Big);
                }else if(s instanceof ControlPoint){
                    ControlPoint cp = (ControlPoint)s; cp.updateThickness(ControlPoint.Thickness.Big);
                }
            }
            break;
        case 2:
            updateThickness(Thickness.Big);
            sh.updateThickness(Sheet.Thickness.Big);
            for(IShape s : lay.getShapesList().getShapes()){
                if(s instanceof Line){
                    Line l = (Line)s; l.updateThickness(Line.Thickness.Big);
                }else if(s instanceof Bezier){
                    Bezier b = (Bezier)s; b.updateThickness(Bezier.Thickness.Big);
                }else if(s instanceof Point){
                    Point p = (Point)s; p.updateThickness(Point.Thickness.Big);
                }else if(s instanceof ControlPoint){
                    ControlPoint cp = (ControlPoint)s; cp.updateThickness(ControlPoint.Thickness.Big);
                }
            }
            break;
        case 3:
            updateThickness(Thickness.Large);
            sh.updateThickness(Sheet.Thickness.Large);
            for(IShape s : lay.getShapesList().getShapes()){
                if(s instanceof Line){
                    Line l = (Line)s; l.updateThickness(Line.Thickness.Large);
                }else if(s instanceof Bezier){
                    Bezier b = (Bezier)s; b.updateThickness(Bezier.Thickness.Large);
                }else if(s instanceof Point){
                    Point p = (Point)s; p.updateThickness(Point.Thickness.Large);
                }else if(s instanceof ControlPoint){
                    ControlPoint cp = (ControlPoint)s; cp.updateThickness(ControlPoint.Thickness.Large);
                }
            }
            break;
        case 4:
            updateThickness(Thickness.Medium);
            sh.updateThickness(Sheet.Thickness.Medium);
            for(IShape s : lay.getShapesList().getShapes()){
                if(s instanceof Line){
                    Line l = (Line)s; l.updateThickness(Line.Thickness.Medium);
                }else if(s instanceof Bezier){
                    Bezier b = (Bezier)s; b.updateThickness(Bezier.Thickness.Medium);
                }else if(s instanceof Point){
                    Point p = (Point)s; p.updateThickness(Point.Thickness.Medium);
                }else if(s instanceof ControlPoint){
                    ControlPoint cp = (ControlPoint)s; cp.updateThickness(ControlPoint.Thickness.Medium);
                }
            }
            break;
        case 5:
            updateThickness(Thickness.Small);
            sh.updateThickness(Sheet.Thickness.Small);
            for(IShape s : lay.getShapesList().getShapes()){
                if(s instanceof Line){
                    Line l = (Line)s; l.updateThickness(Line.Thickness.Small);
                }else if(s instanceof Bezier){
                    Bezier b = (Bezier)s; b.updateThickness(Bezier.Thickness.Small);
                }else if(s instanceof Point){
                    Point p = (Point)s; p.updateThickness(Point.Thickness.Small);
                }else if(s instanceof ControlPoint){
                    ControlPoint cp = (ControlPoint)s; cp.updateThickness(ControlPoint.Thickness.Small);
                }
            }
            break;
        default:
            break;
    }
    scale = slideScale.getValue(); sh.setScaleXY(slideScale.getValue());
    lblScale.setText(slideScale.getValue()+"");
    shhb.setScaleXY(slideScale.getValue());
    shvb.setScaleXY(slideScale.getValue());
    }//GEN-LAST:event_slideScaleStateChanged

    private void btnAddLayerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddLayerActionPerformed
        // Ajouter un calque.
    Layer lay = new Layer();
    dlm.addElement(lay);
    lstLayer.setSelectedValue(lay, true);
    java.util.List<Layer> listlay = new ArrayList<Layer>();
    for(Object o : dlm.toArray()){
        if( o instanceof Layer){
            Layer layer = (Layer)o;
            if(listlay.contains(layer)==false){
                listlay.add(layer);
                sh.updateShapesList(layer.getShapesList());
            }
        }
    }
    sh.setLayerList(listlay);
    sh.updateDrawing();
    }//GEN-LAST:event_btnAddLayerActionPerformed

    private void btnDeleteLayerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteLayerActionPerformed
        // Enlever un calque.
    Layer lay = (Layer)lstLayer.getSelectedValue();
    if(lay.isFirst()==false){
        dlm.removeElement(lay);
        java.util.List<Layer> listlay = new ArrayList<Layer>();
        for(Object o : dlm.toArray()){
            if( o instanceof Layer){
                Layer layer = (Layer)o;
                if(listlay.contains(layer)==false){
                    listlay.add(layer);
                    sh.updateShapesList(layer.getShapesList());
                }
            }
        }
        sh.setLayerList(listlay);
        sh.updateDrawing();
        lstLayer.setSelectedIndex(0);
    }
    }//GEN-LAST:event_btnDeleteLayerActionPerformed

    private void lstLayerValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lstLayerValueChanged
        // Met à jour le dessin par l'intermédiaire des couches.
        for(Object o : dlm.toArray()){
        if(o instanceof Layer){
            Layer layer = (Layer)o;
            if(lstLayer.getSelectedValue()==layer){
                layer.setSelected(true);
                sh.updateShapesList(layer.getShapesList());
                updateRemember(layer);
            }else{
                layer.setSelected(false);
            }            
        }
        tfAssCommands.setText(updateCommands());
        sh.updateDrawing();
    }
    }//GEN-LAST:event_lstLayerValueChanged

    private void btnHistoriqueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHistoriqueActionPerformed
        // Supprimer une ligne ou une courbe.
    Layer lay = (Layer)lstLayer.getSelectedValue();
    ShapesList sl = lay.getShapesList();
    if(sl.getSize()>0){
        IShape last = sl.getLastShape();
        if(last instanceof Line){
            sl.removeLastShape(); lay.virtualRemoveRemember(); //Supprime une ligne.
            sl.removeLastShape(); lay.virtualRemoveRemember(); //Supprime un point.
            try{
                Point p = sl.getLastPoint(); //Récupère le dernier Point de la liste.
                lay.setFirstPoint(p.getLastPoint()); //Définit la dernière position java.awt.Point.
                lay.setLastPoint(p.getLastPoint()); //Définit la dernière position java.awt.Point.   
            }catch(Exception e){}                 
            sh.updateDrawing();
        }else if (last instanceof ControlPoint){
            sl.removeLastShape(); lay.virtualRemoveRemember(); //Supprime un point de contrôle.
            sl.removeLastShape(); lay.virtualRemoveRemember(); //Supprime un point de contrôle.
            sl.removeLastShape(); lay.virtualRemoveRemember(); //Supprime une ligne.
            sl.removeLastShape(); lay.virtualRemoveRemember(); //Supprime un point.
            try{
                Point p = sl.getLastPoint(); //Récupère le dernier Point de la liste.
                lay.setFirstPoint(p.getLastPoint()); //Définit la dernière position java.awt.Point.
                lay.setLastPoint(p.getLastPoint()); //Définit la dernière position java.awt.Point.   
            }catch(Exception e){}     
            sh.updateDrawing();
        }else if (last instanceof BSpline){
            sl.removeLastShape(); lay.virtualRemoveRemember(); //Supprime une bspline.
            sl.removeLastShape(); lay.virtualRemoveRemember(); //Supprime un point.
            try{
                Point p = sl.getLastPoint(); //Récupère le dernier Point de la liste.
                lay.setFirstPoint(p.getLastPoint()); //Définit la dernière position java.awt.Point.
                lay.setLastPoint(p.getLastPoint()); //Définit la dernière position java.awt.Point.   
            }catch(Exception e){}        
            sh.updateDrawing();
        }else{
            sl.removeLastShape(); lay.virtualRemoveRemember(); //Supprime le premier point posé.
            try{
                Point p = sl.getLastPoint(); //Récupère le dernier Point de la liste.
                lay.setFirstPoint(p.getLastPoint()); //Définit la dernière position java.awt.Point.
                lay.setLastPoint(p.getLastPoint()); //Définit la dernière position java.awt.Point.    
            }catch(Exception e){
                lay.setFirstPoint(null); //Définit la dernière position java.awt.Point.
                lay.setLastPoint(null); //Définit la dernière position java.awt.Point.   
            }                 
            sh.updateDrawing();
        }
    }
    listRemember.repaint();
    tfAssCommands.setText(updateCommands());
    }//GEN-LAST:event_btnHistoriqueActionPerformed

    private void btnRedoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRedoActionPerformed
        Layer lay = getCurrentLayer();
        lay.addVirtualRemember();
        listRemember.repaint();
        
        ShapesList sl = lay.getShapesList();
        try{
            Point p = sl.getLastPoint(); //Récupère le dernier Point de la liste.
            lay.setFirstPoint(p.getLastPoint()); //Définit la dernière position java.awt.Point.
            lay.setLastPoint(p.getLastPoint()); //Définit la dernière position java.awt.Point.    
        }catch(Exception e){
            lay.setFirstPoint(null); //Définit la dernière position java.awt.Point.
            lay.setLastPoint(null); //Définit la dernière position java.awt.Point.   
        }
        
        sh.updateGeneralPath(lay.getGeneralPath());
        sh.updateShapesList(lay.getShapesList());
        tfAssCommands.setText(updateCommands());
    }//GEN-LAST:event_btnRedoActionPerformed

    private void popmColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmColorActionPerformed
        // Choisir les couleurs de transparence.
        Layer lay = (Layer)lstLayer.getSelectedValue();
        Color c = JColorChooser.showDialog(frame,
            "Choisir une couleur de transparence...",
            lay.getColor());
        lay.setColor(c);
        sh.updateDrawing();
        lstLayer.repaint();
    }//GEN-LAST:event_popmColorActionPerformed

    private void popmNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmNameActionPerformed
        // Choisir un nom de couche.
        Layer lay = (Layer)lstLayer.getSelectedValue();
        String s = (String)JOptionPane.showInputDialog(frame,
            "Choisir un nom.", "Nom", JOptionPane.QUESTION_MESSAGE);
        if(s.isEmpty()==false){
            lay.setName(s);
            lstLayer.repaint();
        }
    }//GEN-LAST:event_popmNameActionPerformed

    private void popmImageMove1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmImageMove1ActionPerformed
        imageMoveUnit = 1;
    }//GEN-LAST:event_popmImageMove1ActionPerformed

    private void popmImageMove5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmImageMove5ActionPerformed
        imageMoveUnit = 5;
    }//GEN-LAST:event_popmImageMove5ActionPerformed

    private void popmImageMove10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmImageMove10ActionPerformed
        imageMoveUnit = 10;
    }//GEN-LAST:event_popmImageMove10ActionPerformed

    private void popmImageMove50ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmImageMove50ActionPerformed
        imageMoveUnit = 50;
    }//GEN-LAST:event_popmImageMove50ActionPerformed

    private void popmImageMove100ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmImageMove100ActionPerformed
        imageMoveUnit = 100;
    }//GEN-LAST:event_popmImageMove100ActionPerformed

    private void popmCutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmCutActionPerformed
        // Coupe le texte
        try{
            Clipboard cp = new Clipboard();
            cp.CCopy(tfAssCommands.getSelectedText());
            String s = tfAssCommands.getText();
            int sStart = tfAssCommands.getSelectionStart();
            int sEnd = tfAssCommands.getSelectionEnd();
            tfAssCommands.setText(s.substring(0, sStart)+s.substring(sEnd));
        }catch (Exception exc){/*Nothing*/}
    }//GEN-LAST:event_popmCutActionPerformed

    private void popmCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmCopyActionPerformed
        // Copie le texte
        try{
            Clipboard cp = new Clipboard();
            cp.CCopy(tfAssCommands.getSelectedText());
        }catch (Exception exc){/*Nothing*/}
    }//GEN-LAST:event_popmCopyActionPerformed

    private void popmPasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmPasteActionPerformed
        // Colle le texte
        try{
            Clipboard cp = new Clipboard();
            String s = tfAssCommands.getText();
            int sStart = tfAssCommands.getSelectionStart();
            int sEnd = tfAssCommands.getSelectionEnd();
            tfAssCommands.setText(s.substring(0, sStart)+cp.CPaste()+s.substring(sEnd));
        }catch (Exception exc){/*Nothing*/}
    }//GEN-LAST:event_popmPasteActionPerformed

    private void popmDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmDeleteActionPerformed
        // Supprime le texte
        String s = tfAssCommands.getText();
        int sStart = tfAssCommands.getSelectionStart();
        int sEnd = tfAssCommands.getSelectionEnd();
        tfAssCommands.setText(s.substring(0, sStart)+s.substring(sEnd));
    }//GEN-LAST:event_popmDeleteActionPerformed

    private void popmUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmUpdateActionPerformed
        //Essaie de générer un dessin à l'aide des commandes ASS sélectionnées.
        Layer lay = (Layer)lstLayer.getSelectedValue();
        try {shapesFromCommands(tfAssCommands.getSelectedText(),lay,0,0,null,0);
        } catch (Exception ex) {/*nothing*/}
    }//GEN-LAST:event_popmUpdateActionPerformed

    private void popmPenSize2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmPenSize2ActionPerformed
        penSizeUnit = 2;
    }//GEN-LAST:event_popmPenSize2ActionPerformed

    private void popmPenSize4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmPenSize4ActionPerformed
        penSizeUnit = 4;
    }//GEN-LAST:event_popmPenSize4ActionPerformed

    private void popmPenSize6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmPenSize6ActionPerformed
        penSizeUnit = 6;
    }//GEN-LAST:event_popmPenSize6ActionPerformed

    private void popmPenSize8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmPenSize8ActionPerformed
        penSizeUnit = 8;
    }//GEN-LAST:event_popmPenSize8ActionPerformed

    private void popmPenSize10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmPenSize10ActionPerformed
        penSizeUnit = 10;
    }//GEN-LAST:event_popmPenSize10ActionPerformed

    private void popmPenColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmPenColorActionPerformed
        // Choisir la couleur du crayon.
        Color c = JColorChooser.showDialog(this,
            "Choisir une couleur pour le crayon...",
            penColor);
        penColor = c;
    }//GEN-LAST:event_popmPenColorActionPerformed

    private void popmLayerColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmLayerColorActionPerformed
        // Choisir les couleurs de transparence.
        Layer lay = (Layer)lstLayer.getSelectedValue();
        Color c = JColorChooser.showDialog(this,
            "Choisir une couleur de transparence...",
            lay.getColor());
        lay.setColor(c);
        sh.updateDrawing();
        lstLayer.repaint();
    }//GEN-LAST:event_popmLayerColorActionPerformed

    private void popmLayerNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmLayerNameActionPerformed
        // Choisir un nom de couche.
        Layer lay = (Layer)lstLayer.getSelectedValue();
        String s = (String)JOptionPane.showInputDialog(this,
            "Choisir un nom.", "Nom", JOptionPane.QUESTION_MESSAGE);
        if(s.isEmpty()==false){
            lay.setName(s);
            lstLayer.repaint();
        }
    }//GEN-LAST:event_popmLayerNameActionPerformed

    private void popmDuplicateLayerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmDuplicateLayerActionPerformed
        // Dupliquer la couche
        if(tbNormalMode.isSelected()){
            Layer lay = getCurrentLayer();
            List<Remember> lr = lay.getRememberlist(false);
            // Ajouter un calque.
            Layer lay2 = new Layer();
            lay2.setRememberlist(lr);
            lay2.setShapesList(lay.getCopiedShapes());
            dlm.addElement(lay2);
            lstLayer.setSelectedValue(lay2, true);
            List<Layer> listlay = new ArrayList<Layer>();
            for(Object o : dlm.toArray()){
                if( o instanceof Layer){
                    Layer layer = (Layer)o;
                    if(listlay.contains(layer)==false){
                        listlay.add(layer);
                        sh.updateShapesList(layer.getShapesList());
                    }
                }
            }
            sh.setLayerList(listlay);
            sh.updateDrawing();
        }else if(tbOrnamentMode.isSelected()){
            List<Layer> layers = getLinkedLayers();
            for(Layer lay : layers){
                List<Remember> lr = lay.getRememberlist(false);
                // Ajouter un calque.
                Layer lay2 = new Layer();
                lay2.setRememberlist(lr);
                lay2.setShapesList(lay.getCopiedShapes());
                dlm.addElement(lay2);
                lstLayer.setSelectedValue(lay2, true);
                List<Layer> listlay = new ArrayList<Layer>();
                for(Object o : dlm.toArray()){
                    if( o instanceof Layer){
                        Layer layer = (Layer)o;
                        if(listlay.contains(layer)==false){
                            listlay.add(layer);
                            sh.updateShapesList(layer.getShapesList());
                        }
                    }
                }
                sh.setLayerList(listlay);
                sh.updateDrawing();
            }
        }
    }//GEN-LAST:event_popmDuplicateLayerActionPerformed

    private void popmClearSketchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmClearSketchActionPerformed
        int size = setSizeOfDrawing();
        imgDraft = new BufferedImage(size,size,BufferedImage.TYPE_INT_ARGB);
        graDraft = (Graphics2D)imgDraft.getGraphics();
        graDraft.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        //On laisse la zone transparente donc on n'excute plus le code suivant :
        //graDraft.setColor(Color.white);
        //graDraft.fillRect(0, 0, size, size);
        sh.updateDraft(imgDraft);
        sh.updateDrawing();
    }//GEN-LAST:event_popmClearSketchActionPerformed

    private void popmSelectionCopyPasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmSelectionCopyPasteActionPerformed
        // CopyPaste shape
        Layer lay = getCurrentLayer();
        List<IShape> shapes = selection.shapesOneAfter(lay, 2);
        sh.updateSelection(selection, true);
        lay.getShapesList().removeAllShapes();
        lay.clearRemembers();
        for(IShape s : shapes){
            lay.getShapesList().addShape(s); lay.addRemember(s);
        }
        updateRemember(lay);
        sh.updateGeneralPath(lay.getGeneralPath());
        sh.updateShapesList(lay.getShapesList());
        lay.updateEndPoint();
        tfAssCommands.setText(updateCommands());
        shapesFromCommands(tfAssCommands.getText(), lay, 0, 0, null, 0);
    }//GEN-LAST:event_popmSelectionCopyPasteActionPerformed

    private void popmSelectionSymActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmSelectionSymActionPerformed
        // Coller la forme sélectionnée symétrique
        Layer lay = getCurrentLayer();
        List<IShape> shapes = selection.shapesWithSym(lay);
        sh.updateSelection(selection, true);
        lay.getShapesList().removeAllShapes();
        lay.clearRemembers();
        for(IShape s : shapes){
            lay.getShapesList().addShape(s); lay.addRemember(s);
        }
        updateRemember(lay);
        sh.updateGeneralPath(lay.getGeneralPath());
        sh.updateShapesList(lay.getShapesList());
        lay.updateEndPoint();
        tfAssCommands.setText(updateCommands());
    }//GEN-LAST:event_popmSelectionSymActionPerformed

    private void popmSelectionGeo1AActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmSelectionGeo1AActionPerformed
        // Réaliser un forme quelconque selon une autre forme
        Layer lay = getCurrentLayer();
        List<IShape> shapes = selection.shapesAroundASide(lay, 2, 180);
        sh.updateSelection(selection, true);
        lay.getShapesList().removeAllShapes();
        lay.clearRemembers();
        for(IShape s : shapes){
            lay.getShapesList().addShape(s); lay.addRemember(s);
        }
        updateRemember(lay);
        sh.updateGeneralPath(lay.getGeneralPath());
        sh.updateShapesList(lay.getShapesList());
        lay.updateEndPoint();
        tfAssCommands.setText(updateCommands());
        shapesFromCommands(tfAssCommands.getText(), lay, 0, 0, null, 0);
    }//GEN-LAST:event_popmSelectionGeo1AActionPerformed

    private void popmSelectionGeo1BActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmSelectionGeo1BActionPerformed
        // Réaliser un forme quelconque selon une autre forme
        Layer lay = getCurrentLayer();
        List<IShape> shapes = selection.shapesAroundASide(lay, 2, -180);
        sh.updateSelection(selection, true);
        lay.getShapesList().removeAllShapes();
        lay.clearRemembers();
        for(IShape s : shapes){
            lay.getShapesList().addShape(s); lay.addRemember(s);
        }
        updateRemember(lay);
        sh.updateGeneralPath(lay.getGeneralPath());
        sh.updateShapesList(lay.getShapesList());
        lay.updateEndPoint();
        tfAssCommands.setText(updateCommands());
        shapesFromCommands(tfAssCommands.getText(), lay, 0, 0, null, 0);
    }//GEN-LAST:event_popmSelectionGeo1BActionPerformed

    private void popmSelectionGeo2AActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmSelectionGeo2AActionPerformed
        // Réaliser un forme quelconque selon une autre forme
        Layer lay = getCurrentLayer();
        List<IShape> shapes = selection.shapesAroundASide(lay, 3, 120);
        sh.updateSelection(selection, true);
        lay.getShapesList().removeAllShapes();
        lay.clearRemembers();
        for(IShape s : shapes){
            lay.getShapesList().addShape(s); lay.addRemember(s);
        }
        updateRemember(lay);
        sh.updateGeneralPath(lay.getGeneralPath());
        sh.updateShapesList(lay.getShapesList());
        lay.updateEndPoint();
        tfAssCommands.setText(updateCommands());
        shapesFromCommands(tfAssCommands.getText(), lay, 0, 0, null, 0);
    }//GEN-LAST:event_popmSelectionGeo2AActionPerformed

    private void popmSelectionGeo2BActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmSelectionGeo2BActionPerformed
        // Réaliser un forme quelconque selon une autre forme
        Layer lay = getCurrentLayer();
        List<IShape> shapes = selection.shapesAroundASide(lay, 3, -120);
        sh.updateSelection(selection, true);
        lay.getShapesList().removeAllShapes();
        lay.clearRemembers();
        for(IShape s : shapes){
            lay.getShapesList().addShape(s); lay.addRemember(s);
        }
        updateRemember(lay);
        sh.updateGeneralPath(lay.getGeneralPath());
        sh.updateShapesList(lay.getShapesList());
        lay.updateEndPoint();
        tfAssCommands.setText(updateCommands());
        shapesFromCommands(tfAssCommands.getText(), lay, 0, 0, null, 0);
    }//GEN-LAST:event_popmSelectionGeo2BActionPerformed

    private void popmSelectionGeo3AActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmSelectionGeo3AActionPerformed
        // Réaliser un forme quelconque selon une autre forme
        Layer lay = getCurrentLayer();
        List<IShape> shapes = selection.shapesAroundASide(lay, 4, 90);
        sh.updateSelection(selection, true);
        lay.getShapesList().removeAllShapes();
        lay.clearRemembers();
        for(IShape s : shapes){
            lay.getShapesList().addShape(s); lay.addRemember(s);
        }
        updateRemember(lay);
        sh.updateGeneralPath(lay.getGeneralPath());
        sh.updateShapesList(lay.getShapesList());
        lay.updateEndPoint();
        tfAssCommands.setText(updateCommands());
        shapesFromCommands(tfAssCommands.getText(), lay, 0, 0, null, 0);
    }//GEN-LAST:event_popmSelectionGeo3AActionPerformed

    private void popmSelectionGeo3BActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmSelectionGeo3BActionPerformed
        // Réaliser un forme quelconque selon une autre forme
        Layer lay = getCurrentLayer();
        List<IShape> shapes = selection.shapesAroundASide(lay, 4, -90);
        sh.updateSelection(selection, true);
        lay.getShapesList().removeAllShapes();
        lay.clearRemembers();
        for(IShape s : shapes){
            lay.getShapesList().addShape(s); lay.addRemember(s);
        }
        updateRemember(lay);
        sh.updateGeneralPath(lay.getGeneralPath());
        sh.updateShapesList(lay.getShapesList());
        lay.updateEndPoint();
        tfAssCommands.setText(updateCommands());
        shapesFromCommands(tfAssCommands.getText(), lay, 0, 0, null, 0);
    }//GEN-LAST:event_popmSelectionGeo3BActionPerformed

    private void popmSelectionGeoPlusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmSelectionGeoPlusActionPerformed
        // Réaliser un forme quelconque selon une autre forme
        SelectGeoDialog sgd = new SelectGeoDialog(frame, true);
        boolean b = sgd.showDialog();
        if(b==true){
            Layer lay = getCurrentLayer();
            List<IShape> shapes = selection.shapesAroundASide(lay, sgd.getSide(), sgd.getAngle());
            sh.updateSelection(selection, true);
            lay.getShapesList().removeAllShapes();
            lay.clearRemembers();
            for(IShape s : shapes){
                lay.getShapesList().addShape(s); lay.addRemember(s);
            }
            updateRemember(lay);
            sh.updateGeneralPath(lay.getGeneralPath());
            sh.updateShapesList(lay.getShapesList());
            lay.updateEndPoint();
            tfAssCommands.setText(updateCommands());
            shapesFromCommands(tfAssCommands.getText(), lay, 0, 0, null, 0);
        }
    }//GEN-LAST:event_popmSelectionGeoPlusActionPerformed

    private void popmRubberSize2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmRubberSize2ActionPerformed
        rubberSizeUnit = 2;
    }//GEN-LAST:event_popmRubberSize2ActionPerformed

    private void popmRubberSize4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmRubberSize4ActionPerformed
        rubberSizeUnit = 4;
    }//GEN-LAST:event_popmRubberSize4ActionPerformed

    private void popmRubberSize6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmRubberSize6ActionPerformed
        rubberSizeUnit = 6;
    }//GEN-LAST:event_popmRubberSize6ActionPerformed

    private void popmRubberSize8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmRubberSize8ActionPerformed
        rubberSizeUnit = 8;
    }//GEN-LAST:event_popmRubberSize8ActionPerformed

    private void popmRubberSize10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmRubberSize10ActionPerformed
        rubberSizeUnit = 10;
    }//GEN-LAST:event_popmRubberSize10ActionPerformed

    private void btnApplyScriptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApplyScriptActionPerformed
        // Appliquer des scripts
        if(listScript.getSelectedIndex()!=-1){
            Object o = listScript.getSelectedValue();
            if(o instanceof DrawingScript){
                DrawingScript scr = (DrawingScript)o;
                scripting.runScriptAndDo(scr);
            }
        }
    }//GEN-LAST:event_btnApplyScriptActionPerformed

    private void btnOrnReadyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOrnReadyActionPerformed
        try {
            // Préparer le karaoké en le stockant en mémoire
            Clipboard cp = new Clipboard();
            BufferedReader br = new BufferedReader(new StringReader((cp.CPaste())));
            karaokeOrnament.clear();
            String line;
            while((line = br.readLine())!=null){
                AssLine al = new AssLine(line);
                karaokeOrnament.add(al);
            }
            btnOrnReady.setForeground(Color.green);
            btnOrnGenerate.setForeground(Color.red);
        } catch (IOException ex) {
            btnOrnReady.setForeground(Color.red);
            btnOrnGenerate.setForeground(Color.red);
        }
    }//GEN-LAST:event_btnOrnReadyActionPerformed

    private void btnOrnGenerateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOrnGenerateActionPerformed
        // Générer et copier dans le presse-papier
        if(img != null){
            if(rbOrnMainMoveOff.isSelected()){
                for (FileFilter f : fcZDrawing.getChoosableFileFilters()){
                    fcZDrawing.removeChoosableFileFilter(f);
                }
                fcZDrawing.setAccessory(new DrawingPreview(fcZDrawing));
                fcZDrawing.addChoosableFileFilter(new AssFilter());
                int z = fcZDrawing.showSaveDialog(this);
                if (z == JFileChooser.APPROVE_OPTION){
                    try{
                        OrnDo orndo = new OrnDo();
                        orndo.getLinesForNoMove2(ornlayForMain, karaokeOrnament,
                                dlm.toArray(), sh.getImagePositionX(),
                                sh.getImagePositionY(),
                                fcZDrawing.getSelectedFile().getAbsolutePath(),
                                sh.getImageWidth(), sh.getImageHeight());
                    }catch(Exception e){
                        
                    }
                }
                btnOrnGenerate.setForeground(Color.green);
            }else if(rbOrnMainMoveOn.isSelected() && ornlayForMain.getList().isEmpty()==false){
                for (FileFilter f : fcZDrawing.getChoosableFileFilters()){
                    fcZDrawing.removeChoosableFileFilter(f);
                }
                fcZDrawing.setAccessory(new DrawingPreview(fcZDrawing));
                fcZDrawing.addChoosableFileFilter(new AssFilter());
                int z = fcZDrawing.showSaveDialog(this);
                if (z == JFileChooser.APPROVE_OPTION){
                    try{
                        //Récupération des valeurs dans le tableau
                        for(int i=0; i<dtmOrnForMain.getRowCount(); i++){
                            feuille.drawing.ornament.IShape s = (feuille.drawing.ornament.IShape)dtmOrnForMain.getValueAt(i, 0);
                            String str = (String)dtmOrnForMain.getValueAt(i, 1);
                            s.setDuration(str);
                        }
                        OrnDo orndo = new OrnDo();
                        orndo.getLinesForMainMoveOnly3(
                        ornlayForMain, karaokeOrnament, spinMMModel.getNumber().intValue(),
                        dlm.toArray(), sh.getImagePositionX(), sh.getImagePositionY(),
                        fcZDrawing.getSelectedFile().getAbsolutePath(),
                        sh.getImageWidth(), sh.getImageHeight());
                    }catch(Exception e){
                        
                    }
                }
                btnOrnGenerate.setForeground(Color.green);
            }
        }else{
            JOptionPane.showMessageDialog(this, "Veuillez ouvrir une image", "Impossible de calculer les coordonnées", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnOrnGenerateActionPerformed

    private void btnOrnMMClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOrnMMClearActionPerformed
        // Effacer les mouvements généraux (mode Ornement)
        ornlayForMain.clearShapes();
        sh.updateOrnamentForMain(ornlayForMain.getList());
        try{
            for (int i=dtmOrnForMain.getRowCount()-1;i>=0;i--){
                dtmOrnForMain.removeRow(i);
            }
        }catch(Exception exc){}
    }//GEN-LAST:event_btnOrnMMClearActionPerformed

    private void rbOrnMainMoveOffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbOrnMainMoveOffActionPerformed
        // On a appuyé sur Non pour les mouvements généraux >> Pas d'accès aux contrôles
//        tbOrnMMBSpline.setEnabled(false);
        tbOrnMMBezier.setEnabled(false);
        tbOrnMMLine.setEnabled(false);
        btnOrnMMClear.setEnabled(false);
        lblOrnMMFreq.setEnabled(false);
        spiOrnMMFreq.setEnabled(false);
    }//GEN-LAST:event_rbOrnMainMoveOffActionPerformed

    private void rbOrnMainMoveOnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbOrnMainMoveOnActionPerformed
        // On a appuyé sur Oui pour les mouvements généraux >> Accès aux contrôles
//        tbOrnMMBSpline.setEnabled(true);
        tbOrnMMBezier.setEnabled(true);
        tbOrnMMLine.setEnabled(true);
        btnOrnMMClear.setEnabled(true);
        lblOrnMMFreq.setEnabled(true);
        spiOrnMMFreq.setEnabled(true);
    }//GEN-LAST:event_rbOrnMainMoveOnActionPerformed

    private void btnCloseBSplineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseBSplineActionPerformed
        //On ferme la BSpline en cours.
        Layer lay = getCurrentLayer();
        IShape shape = lay.getShapesList().getLastShape();
        if(shape instanceof BSpline){
            BSpline bs = (BSpline)shape;
            if(bs.isNextExist()==false){
                bs.setClosed(true);
                //montre la zone
                sh.updateGeneralPath(lay.getGeneralPath());
                sh.updateShapesList(lay.getShapesList());
                tfAssCommands.setText(updateCommands());
            }            
        }
    }//GEN-LAST:event_btnCloseBSplineActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgDraw;
    private javax.swing.ButtonGroup bgImageMove;
    private javax.swing.ButtonGroup bgMode;
    private javax.swing.ButtonGroup bgOrnMainMove;
    private javax.swing.ButtonGroup bgOrnShapeMove;
    private javax.swing.ButtonGroup bgPenSize;
    private javax.swing.ButtonGroup bgRubberSize;
    private javax.swing.JButton btnAddLayer;
    private javax.swing.JButton btnApplyScript;
    private javax.swing.JButton btnBottomImage;
    private javax.swing.JButton btnCentreImage;
    private javax.swing.JButton btnCloseBSpline;
    private javax.swing.JButton btnDeleteLayer;
    private javax.swing.JButton btnHistorique;
    private javax.swing.JButton btnImage;
    private javax.swing.JButton btnLeftImage;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnOpen;
    private javax.swing.JButton btnOpenFonts;
    private javax.swing.JButton btnOrnGenerate;
    private javax.swing.JButton btnOrnMMClear;
    private javax.swing.JButton btnOrnReady;
    private javax.swing.JButton btnRedo;
    private javax.swing.JButton btnRightImage;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnTopImage;
    private javax.swing.JButton btnUnloadImage;
    private javax.swing.JFileChooser fcZDrawing;
    private javax.swing.JInternalFrame ifrAssCommands;
    private javax.swing.JInternalFrame ifrDrawing;
    private javax.swing.JInternalFrame ifrHistoric;
    private javax.swing.JInternalFrame ifrImage;
    private javax.swing.JInternalFrame ifrLayers;
    private javax.swing.JInternalFrame ifrMainDraw;
    private javax.swing.JInternalFrame ifrMode;
    private javax.swing.JInternalFrame ifrOpenSave;
    private javax.swing.JInternalFrame ifrOperations;
    private javax.swing.JInternalFrame ifrOrnament;
    private javax.swing.JInternalFrame ifrScript;
    private javax.swing.JInternalFrame ifrShape;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JLabel lblAssCommands;
    private javax.swing.JLabel lblCoordinates;
    private javax.swing.JLabel lblDrawingAlpha;
    private javax.swing.JLabel lblImageAlpha;
    private javax.swing.JLabel lblMainMove;
    private javax.swing.JLabel lblOrnMMFreq;
    private javax.swing.JLabel lblScale;
    private javax.swing.JList listRemember;
    private javax.swing.JList listScript;
    private static javax.swing.JList lstLayer;
    private javax.swing.JTable ornMMTable;
    private javax.swing.JPopupMenu popAssCom;
    private javax.swing.JPopupMenu popDrawing;
    private javax.swing.JPopupMenu popImageMove;
    private javax.swing.JPopupMenu popLayer;
    private javax.swing.JPopupMenu popPenSize;
    private javax.swing.JPopupMenu popRubberSize;
    private javax.swing.JMenuItem popmClearSketch;
    private javax.swing.JMenuItem popmColor;
    private javax.swing.JMenuItem popmCopy;
    private javax.swing.JMenuItem popmCut;
    private javax.swing.JMenuItem popmDelete;
    private javax.swing.JMenuItem popmDuplicateLayer;
    private javax.swing.JRadioButtonMenuItem popmImageMove1;
    private javax.swing.JRadioButtonMenuItem popmImageMove10;
    private javax.swing.JRadioButtonMenuItem popmImageMove100;
    private javax.swing.JRadioButtonMenuItem popmImageMove5;
    private javax.swing.JRadioButtonMenuItem popmImageMove50;
    private javax.swing.JMenuItem popmLayerColor;
    private javax.swing.JMenuItem popmLayerName;
    private javax.swing.JMenuItem popmName;
    private javax.swing.JMenuItem popmPaste;
    private javax.swing.JMenuItem popmPenColor;
    private javax.swing.JRadioButtonMenuItem popmPenSize10;
    private javax.swing.JRadioButtonMenuItem popmPenSize2;
    private javax.swing.JRadioButtonMenuItem popmPenSize4;
    private javax.swing.JRadioButtonMenuItem popmPenSize6;
    private javax.swing.JRadioButtonMenuItem popmPenSize8;
    private javax.swing.JRadioButtonMenuItem popmRubberSize10;
    private javax.swing.JRadioButtonMenuItem popmRubberSize2;
    private javax.swing.JRadioButtonMenuItem popmRubberSize4;
    private javax.swing.JRadioButtonMenuItem popmRubberSize6;
    private javax.swing.JRadioButtonMenuItem popmRubberSize8;
    private javax.swing.JMenuItem popmSelectionCopyPaste;
    private javax.swing.JMenuItem popmSelectionGeo1A;
    private javax.swing.JMenuItem popmSelectionGeo1B;
    private javax.swing.JMenuItem popmSelectionGeo2A;
    private javax.swing.JMenuItem popmSelectionGeo2B;
    private javax.swing.JMenuItem popmSelectionGeo3A;
    private javax.swing.JMenuItem popmSelectionGeo3B;
    private javax.swing.JMenuItem popmSelectionGeoPlus;
    private javax.swing.JMenuItem popmSelectionSym;
    private javax.swing.JMenuItem popmUpdate;
    private javax.swing.JRadioButton rbOrnMainMoveOff;
    private javax.swing.JRadioButton rbOrnMainMoveOn;
    private javax.swing.JSlider slideDrawingAlpha;
    private javax.swing.JSlider slideImageAlpha;
    private javax.swing.JSlider slideScale;
    private javax.swing.JScrollPane spSheet;
    private javax.swing.JSpinner spiOrnMMFreq;
    private javax.swing.JToggleButton tbFreeClear;
    private javax.swing.JToggleButton tbFreeDrawing;
    private javax.swing.JToggleButton tbMove;
    private javax.swing.JToggleButton tbNextPoint;
    private javax.swing.JToggleButton tbNormalMode;
    private javax.swing.JToggleButton tbOrnMMBSpline;
    private javax.swing.JToggleButton tbOrnMMBezier;
    private javax.swing.JToggleButton tbOrnMMLine;
    private javax.swing.JToggleButton tbOrnamentMode;
    private javax.swing.JToggleButton tbReStart;
    private javax.swing.JToggleButton tbResize;
    private javax.swing.JToggleButton tbRotation;
    private javax.swing.JToggleButton tbSelection;
    private javax.swing.JToggleButton tbShapeBSpline1;
    private javax.swing.JToggleButton tbShapeBezier;
    private javax.swing.JToggleButton tbShapeLine;
    private javax.swing.JToggleButton tbShear;
    private javax.swing.JToggleButton tbTranslation;
    private static javax.swing.JTextField tfAssCommands;
    // End of variables declaration//GEN-END:variables
}
