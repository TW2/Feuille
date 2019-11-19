/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smallboxforfansub.drawing.operation;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import smallboxforfansub.drawing.lib.BSpline;
import smallboxforfansub.drawing.lib.Bezier;
import smallboxforfansub.drawing.lib.Command;
import smallboxforfansub.drawing.lib.ControlPoint;
import smallboxforfansub.drawing.lib.IShape;
import smallboxforfansub.drawing.lib.Layer;
import smallboxforfansub.drawing.lib.Line;
import smallboxforfansub.drawing.lib.Move;
import smallboxforfansub.drawing.lib.Point;
import smallboxforfansub.drawing.lib.ReStart;
import smallboxforfansub.drawing.lib.ShapesList;

/**
 *
 * @author The Wingate 2940
 */
public class Selection {
    
    private int fromX, fromY, toX, toY;
    private Float gpAlpha = 0.2f;
    private List<IShape> selectedShape = new ArrayList<IShape>();
    
    private String localCommandCopy = "";
    
    public Selection(){
        fromX = fromY = toX = toY = -1;
    }
    
    public void setStartPoint(java.awt.Point p){
        setStartPoint(p.x, p.y);
    }
    
    public void setStartPoint(int x, int y){
        fromX = x;
        fromY = y;
    }
    
    public java.awt.Point getStartPoint(){
        return new java.awt.Point(fromX, fromY);
    }
    
    public int getStartPointX(){
        return fromX;
    }
    
    public int getStartPointY(){
        return fromY;
    }
    
    public void setEndPoint(java.awt.Point p){
        setEndPoint(p.x, p.y);
    }
    
    public void setEndPoint(int x, int y){
        toX = x;
        toY = y;
    }
    
    public java.awt.Point getEndPoint(){
        return new java.awt.Point(toX, toY);
    }
    
    public int getEndPointX(){
        return toX;
    }
    
    public int getEndPointY(){
        return toY;
    }
    
    public void searchForShapes(ShapesList slist){
        Rectangle area = new Rectangle(fromX, fromY, toX-fromX, toY-fromY);        
        for(IShape s : slist.getShapes()){
            if(s instanceof Point && area.contains(s.getLastPoint())){
                Point p = (Point)s;
                p.setInSelection(true);
                addSelectedShape(p);
            }
            if(s instanceof ControlPoint && area.contains(s.getLastPoint())){
                ControlPoint cp = (ControlPoint)s;
                findAndSelectBezier(slist, cp);
//                cp.setInSelection(true);                
            }
            if(s instanceof ReStart && area.contains(s.getLastPoint())){
                ReStart m = (ReStart)s;
                m.setInSelection(true);
                addSelectedShape(m);
            }
            if(s instanceof Move && area.contains(s.getLastPoint())){
                Move n = (Move)s;
                n.setInSelection(true);
                addSelectedShape(n);
            }
            if(s instanceof BSpline){
                BSpline bs = (BSpline)s;
                boolean foundCP = false;
                for(ControlPoint cp : bs.getControlPoints()){
                    if(area.contains(cp.getLastPoint())){
                        foundCP = true;
                    }
                }
                if(foundCP==true){
                    for(ControlPoint cp : bs.getControlPoints()){
                        cp.setInSelection(true);                        
                    }
                    
                    addSelectedShape(bs);
                }                
            }
        }
        
        addRangeOfSelectedShape(slist);
    }
    
    private void findAndSelectBezier(ShapesList slist, ControlPoint cp){
        int index = 0;
        for(IShape s : slist.getShapes()){
            if(s instanceof Bezier){
                Bezier b = (Bezier)s;
                if(cp.equals(b.getControl1()) | cp.equals(b.getControl2())){
                    addSelectedShape(b);
                    
                    //Sélection des points de contrôle
                    b.getControl1().setInSelection(true); addSelectedShape(b.getControl1());
                    b.getControl2().setInSelection(true); addSelectedShape(b.getControl2());
                    
                    
                    //Sélection des points
                    int count = 0;
                    for(int i = index; i >= 0; i--){
                        if(slist.getShapes().get(i) instanceof Point && count < 2){
                            Point p = (Point)slist.getShapes().get(i);
                            if(p.hasCoordinates(b.getOriginPoint()) | p.hasCoordinates(b.getLastPoint())){
                                p.setInSelection(true); addSelectedShape(p);
                                count += 1;
                            }
                        }
                        if(slist.getShapes().get(i) instanceof ReStart && count < 2){
                            ReStart p = (ReStart)slist.getShapes().get(i);
                            if(p.hasCoordinates(b.getOriginPoint()) | p.hasCoordinates(b.getLastPoint())){
                                p.setInSelection(true); addSelectedShape(p);
                                count += 1;
                            }
                        }
                        if(slist.getShapes().get(i) instanceof Move && count < 2){
                            Move p = (Move)slist.getShapes().get(i);
                            if(p.hasCoordinates(b.getOriginPoint()) | p.hasCoordinates(b.getLastPoint())){
                                p.setInSelection(true); addSelectedShape(p);
                                count += 1;
                            }
                        }
                    }
                    return;
                }
            }
            index += 1;
        }
    }
    
    public void clear(){
        fromX = fromY = toX = toY = -1;
    }
    
    public void cleanList(ShapesList slist){
        for(IShape s : slist.getShapes()){
            s.setInSelection(false);
            s.setFirstInSelection(false);
        }
        selectedShape.clear();
    }
    
    public boolean exists(){
        if(fromX!=-1){
            return true;
        }
        return false;
    }
    
    // Gestion de la transparence
    private AlphaComposite makeComposite(float alpha) {
        int type = AlphaComposite.SRC_OVER;
        return(AlphaComposite.getInstance(type, alpha));
    }
    
    public void drawSelection(Graphics2D g2d){
        Composite originalComposite = g2d.getComposite();//Get default
        g2d.setComposite(makeComposite(gpAlpha));//Change the transparency
        g2d.setColor(Color.red);
        int width = toX-fromX;
        int height = toY-fromY;
        g2d.fillRect(fromX, fromY, width, height);
        g2d.setComposite(originalComposite);//Reset default
    }
    
    private void addSelectedShape(IShape s){
        if(selectedShape.contains(s)==false){
            selectedShape.add(s);
        }
    }
    
    private void addRangeOfSelectedShape(ShapesList sl){
        int fromPosition = -1, toPosition = -1, i=0;
        //On obtient l'index du premier Shape sélectionné
        while(i<sl.getSize() && fromPosition == -1){
            IShape s = sl.getShapes().get(i);
            
            if(s instanceof Point | s instanceof ReStart && s.isInSelection()){
                try{                    
                    //Créer une shape next et si on plante pas on attribue la valeur
                    IShape next1 = sl.getShapes().get(i+1);
                    fromPosition = i+1;
                }catch(Exception e1){
                    fromPosition = i;
                }           
            }
            
//            if(s instanceof Point && s.isInSelection()){
//                Shape next = sl.getShapes().get(i+1);
//                if(next.isInSelection()){
//                    fromPosition = i;
//                }else if(next instanceof Line){
//                    fromPosition = i;
//                }
//            }else if(s.isInSelection()){
//                fromPosition = i; 
//            }
            i += 1;
        }
        i = sl.getSize()-1;
        //On obtient l'index du dernier Shape sélectionné
        while(i>=0 && toPosition == -1){
            IShape s = sl.getShapes().get(i);
            if(s instanceof Point && s.isInSelection()){
                try{
                    //Créer une shape next et si on plante pas on attribue la valeur
                    IShape next = sl.getShapes().get(i+1);
                    toPosition = i+1;
                }catch(Exception e){
                    toPosition = i;
                }           
            }
            i -= 1;
        }
        //On sélectionne tout ce qui est entre les deux
        if(fromPosition != -1 && toPosition != 1){
            boolean skip = false;
            for(int j=fromPosition; j<=toPosition; j++){                
                IShape s = sl.getShapes().get(j);
                if(s instanceof Line && j==fromPosition){ skip = true;}
                if(s instanceof Bezier && j==fromPosition){ skip = true;}
                if(s instanceof ControlPoint && j==fromPosition+1){ skip = true;}
                if(s instanceof ControlPoint && j==fromPosition+2){ skip = true;}
                if(skip==false){
                    s.setInSelection(true);
                    s.setFirstInSelection(j == fromPosition ? true : false);
                    addSelectedShape(s);
                }
                skip=false;
            }
        }
    }
    
    //Met à jour les commandes ASS à partir du dessin
    public String commandsFromShapes(){
        String commands = "";
        try{
            for(IShape s : selectedShape){
                if(s instanceof Line){
                    Line line = (Line)s;
                    int x = (int)line.getLastPoint().getX();
                    int y = (int)line.getLastPoint().getY();
                    int xb = x-1000;
                    int yb = y-1000;
                    commands = commands + "l "+xb+" "+yb+" ";
                }else if(s instanceof Bezier){
                    Bezier bezier = (Bezier)s;
                    int x1 = (int)bezier.getControl1().getOriginPoint().getX();
                    int y1 = (int)bezier.getControl1().getOriginPoint().getY();
                    int x2 = (int)bezier.getControl2().getOriginPoint().getX();
                    int y2 = (int)bezier.getControl2().getOriginPoint().getY();
                    int x3 = (int)bezier.getLastPoint().getX();
                    int y3 = (int)bezier.getLastPoint().getY();
                    int xe = x1-1000;
                    int ye = y1-1000;
                    int xf = x2-1000;
                    int yf = y2-1000;
                    int xg = x3-1000;
                    int yg = y3-1000;
                    commands = commands + "b "+xe+" "+ye+" "+xf+" "+yf+" "+xg+" "+yg+" ";
                }else if(s instanceof BSpline){
                    BSpline bs = (BSpline)s;
                    List<ControlPoint> lcp = bs.getControlPoints();
                    int lastcp = lcp.size()-1;
                    commands = commands + "s ";
                    for(ControlPoint cp : lcp){
                        int x = (int)cp.getOriginPoint().getX();
                        int y = (int)cp.getOriginPoint().getY();
                        int xi = x-1000;
                        int yi = y-1000;
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
                        int xi = x-1000;
                        int yi = y-1000;
                        commands = commands + "p "+xi+" "+yi+" ";
                    }
                }else if(s instanceof Move){
                    Move move = (Move)s;
                    int x = (int)move.getLastPoint().getX();
                    int y = (int)move.getLastPoint().getY();
                    int xb = x-1000;
                    int yb = y-1000;
                    commands = commands + "n "+xb+" "+yb+" ";
                }else if(s instanceof ReStart){
                    ReStart move = (ReStart)s;
                    int x = (int)move.getLastPoint().getX();
                    int y = (int)move.getLastPoint().getY();
                    int xb = x-1000;
                    int yb = y-1000;
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
    private List<IShape> shapesFromCommands(String com){
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
                int xs = Integer.parseInt(mat.group(2))+1000;
                int ys = Integer.parseInt(mat.group(3))+1000;
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
        
        List<IShape> shapes = new ArrayList<IShape>();
        java.awt.Point p_init = new java.awt.Point();
        Command last_command = null;
        
        for(Command c : commandList){
            if(c.getType()==Command.Type.ReStart){
                ReStart p = c.getStartPoint((int)p_init.getX(), (int)p_init.getY());                
                shapes.add(p);
                p_init = p.getLastPoint();
            }
            if(c.getType()==Command.Type.Line){
                Line l = c.getLine((int)p_init.getX(), (int)p_init.getY());
                Point p = new Point((int)l.getLastPoint().getX(), (int)l.getLastPoint().getY());
                shapes.add(p);
                shapes.add(l);
                p_init = l.getLastPoint();
            }
            if(c.getType()==Command.Type.Bezier){
                Bezier b = c.getBezier((int)p_init.getX(), (int)p_init.getY());
                Point p = new Point((int)b.getLastPoint().getX(), (int)b.getLastPoint().getY());
                shapes.add(p);
                shapes.add(b);
                shapes.add(b.getControl1());
                shapes.add(b.getControl2());
                p_init = b.getLastPoint();
            }
            if(c.getType()==Command.Type.BSpline){
                BSpline b = c.getBSpline((int)p_init.getX(), (int)p_init.getY());
                Point p = new Point((int)b.getLastPoint().getX(), (int)b.getLastPoint().getY());
                shapes.add(p);
                shapes.add(b);
                p_init = b.getLastPoint();
            }
            if(c.getType()==Command.Type.Extend && last_command.getType()==Command.Type.BSpline){
                BSpline b = (BSpline)shapes.get(shapes.size()-1);
                Point p = c.getExtendPoint();
                if(b.isClosed()==false){
                    b.setNextPoint((int)p.getLastPoint().getX(), (int)p.getLastPoint().getY());
                    b.addPoint((int)p.getLastPoint().getX(), (int)p.getLastPoint().getY());
                    b.setLastPoint((int)p.getLastPoint().getX(), (int)p.getLastPoint().getY());
                }
                p_init = b.getLastPoint();
            }
            if(c.getType()==Command.Type.Close && last_command.getType()==Command.Type.BSpline){
                BSpline b = (BSpline)shapes.get(shapes.size()-1);
                if(b.isNextExist()==false){
                    b.setClosed(true);
                }
                p_init = b.getLastPoint();
            }
            if(c.getType()==Command.Type.Move){
                Move m = c.getMove((int)p_init.getX(), (int)p_init.getY());
                shapes.add(m);
                p_init = m.getLastPoint();
            }
            last_command = c;
        }
        
        return shapes;
    }
    
    private Line createLine(Line l, boolean invert){
        Line nl = new Line();
        if(invert==false){            
            nl.setOriginPoint(l.getOriginPoint().x, l.getOriginPoint().y);
            nl.setLastPoint(l.getLastPoint().x, l.getLastPoint().y);
        }else{
            nl.setOriginPoint(l.getLastPoint().x, l.getLastPoint().y);
            nl.setLastPoint(l.getOriginPoint().x, l.getOriginPoint().y);
        }
        return nl;
    }
    
    private Bezier createBezier(Bezier b, boolean invert){
        Bezier nb = new Bezier();
        if(invert==false){            
            nb.setOriginPoint(b.getOriginPoint().x, b.getOriginPoint().y);
            nb.setLastPoint(b.getLastPoint().x, b.getLastPoint().y);
        }else{
            nb.setOriginPoint(b.getLastPoint().x, b.getLastPoint().y);
            nb.setLastPoint(b.getOriginPoint().x, b.getOriginPoint().y);
        }
        return nb;
    }
    
    private BSpline createBSpline(BSpline b, boolean invert){
        BSpline nb = new BSpline();
        if(invert==false){            
            nb.setOriginPoint(b.getOriginPoint().x, b.getOriginPoint().y);
            nb.setLastPoint(b.getLastPoint().x, b.getLastPoint().y);
            for(ControlPoint cp : b.getControlPoints()){
                if(nb.isBSplineCurveExists()==false){
                    nb.setupBSplineCurve(cp.getLastPoint().x, cp.getLastPoint().y);
                }else{
                    nb.addPoint(cp.getLastPoint().x, cp.getLastPoint().y);
                }
            }
            if(b.isNextExist()==true){
                nb.setNextPoint(b.getNextPoint().x, b.getNextPoint().y);
            }
        }else{
            nb.setOriginPoint(b.getLastPoint().x, b.getLastPoint().y);
            nb.setLastPoint(b.getOriginPoint().x, b.getOriginPoint().y);
            nb.addPoint(b.getLastPoint().x, b.getLastPoint().y);
            for(int i=b.getControlPoints().size()-1; i>=1; i--){
                ControlPoint cp = b.getControlPoints().get(i);
                if(nb.isBSplineCurveExists()==false){
                    nb.setupBSplineCurve(cp.getLastPoint().x, cp.getLastPoint().y);
                }else{
                    nb.addPoint(cp.getLastPoint().x, cp.getLastPoint().y);
                }
            }
            nb.setNextPoint(b.getOriginPoint().x, b.getOriginPoint().y);
        }
        return nb;
    }
    
    private Point createPointOfLine(Line l, boolean invert){
        Point np = new Point();
        if(invert==false){            
            np.setOriginPoint(l.getLastPoint().x, l.getLastPoint().y);
            np.setLastPoint(l.getLastPoint().x, l.getLastPoint().y);
        }else{
            np.setOriginPoint(l.getOriginPoint().x, l.getOriginPoint().y);
            np.setLastPoint(l.getOriginPoint().x, l.getOriginPoint().y);
        }
        return np;
    }
    
    private Point createPointOfBezier(Bezier b, boolean invert){
        Point np = new Point();
        if(invert==false){            
            np.setOriginPoint(b.getLastPoint().x, b.getLastPoint().y);
            np.setLastPoint(b.getLastPoint().x, b.getLastPoint().y);
        }else{
            np.setOriginPoint(b.getOriginPoint().x, b.getOriginPoint().y);
            np.setLastPoint(b.getOriginPoint().x, b.getOriginPoint().y);
        }
        return np;
    }
    
    private ControlPoint createControlPointOfBezier(ControlPoint cp, boolean invert){
        ControlPoint ncp = new ControlPoint();
        if(invert==false){            
            ncp.setOriginPoint(cp.getLastPoint().x, cp.getLastPoint().y);
            ncp.setLastPoint(cp.getLastPoint().x, cp.getLastPoint().y);
        }else{
            ncp.setOriginPoint(cp.getOriginPoint().x, cp.getOriginPoint().y);
            ncp.setLastPoint(cp.getOriginPoint().x, cp.getOriginPoint().y);
        }
        return ncp;
    }
    
    private Point createPointOfBSpline(BSpline b, boolean invert){
        Point np = new Point();
        if(invert==false){            
            np.setOriginPoint(b.getOriginPoint().x, b.getOriginPoint().y);
            np.setLastPoint(b.getOriginPoint().x, b.getOriginPoint().y);
        }else{
            np.setOriginPoint(b.getLastPoint().x, b.getLastPoint().y);
            np.setLastPoint(b.getLastPoint().x, b.getLastPoint().y);
        }
        return np;
    }
    
    private void setOriginalShapes(Layer lay, List<IShape> shapes){
        for(IShape s : lay.getShapesList().getShapes()){
            System.out.println("Shape >> "+s);
            if(s instanceof Line){
                Line line = (Line)s;
                shapes.add(createPointOfLine(line, false));
                shapes.add(createLine(line, false));
            }else if(s instanceof Bezier){
                Bezier bezier = (Bezier)s;
                ControlPoint newcp1 = createControlPointOfBezier(bezier.getControl1(), false);
                ControlPoint newcp2 = createControlPointOfBezier(bezier.getControl2(), false);
                Bezier newbezier = createBezier(bezier, false);
                newbezier.setControl1(newcp1);
                newbezier.setControl2(newcp2);
                shapes.add(createPointOfBezier(bezier, false));
                shapes.add(newbezier);
                shapes.add(newcp1);
                shapes.add(newcp2);
            }else if(s instanceof BSpline){
                BSpline bs = (BSpline)s;
                BSpline newbspline = createBSpline(bs, false);
                Point newpoint = createPointOfBSpline(bs, false);                
                shapes.add(newpoint);
                shapes.add(newbspline);
            }else if(s instanceof Move){
                Move move = (Move)s;
                Move newmove = new Move();
                newmove.setOriginPoint(move.getLastPoint().x, move.getLastPoint().y);
                newmove.setLastPoint(move.getLastPoint().x, move.getLastPoint().y);
                shapes.add(newmove);
            }else if(s instanceof ReStart){
                ReStart restart = (ReStart)s;
                ReStart newrestart = new ReStart();
                newrestart.setOriginPoint(restart.getLastPoint().x, restart.getLastPoint().y);
                newrestart.setLastPoint(restart.getLastPoint().x, restart.getLastPoint().y);
                shapes.add(newrestart);
            }
        }
    }
    
    private Point getStartPoint(Layer lay){
        //Obtient le point de référence
        Point startPoint = null;
        for(int i=0; i<lay.getShapesList().getShapes().size(); i++){
            IShape s = lay.getShapesList().getShapes().get(i);
            if(s instanceof Point && startPoint == null && s.isInSelection()){
                Point p = (Point)s;
                startPoint = p;
            }
            if(s instanceof ReStart && startPoint == null && s.isInSelection()){
                ReStart m = (ReStart)s;
                Point p = new Point(m.getLastPoint().x, m.getLastPoint().y);
                startPoint = p;
            }
        }
        return startPoint;
    }
    
    private Point getEndPoint(Layer lay){
        //Obtient le point de référence
        Point endPoint = null;
        for(int i=lay.getShapesList().getShapes().size()-1; i>=0; i--){
            IShape s = lay.getShapesList().getShapes().get(i);
            if(s instanceof Point && endPoint == null && s.isInSelection()){
                Point p = (Point)s;
                endPoint = p;
            }
        }
        return endPoint;
    }
        
    /** Calcule la symétrie du point.
     * @param S Le premier point de l'axe SE.
     * @param E Le deuxième point de l'axe SE.
     * @param P Le point pour lequel on calcule la symétrie.
     * @return Les coordonnées du point P'. */
    private java.awt.Point getSymmetry(java.awt.Point S, java.awt.Point E, java.awt.Point P){
        java.awt.Point Pprime = P;
        System.out.println("-------------------------");
        System.out.println("Point S ("+S.x+";"+S.y+") ["+(S.x-1000)+";"+(S.y-1000)+"]");
        System.out.println("Point E ("+E.x+";"+E.y+") ["+(E.x-1000)+";"+(E.y-1000)+"]");
        System.out.println("Point P ("+P.x+";"+P.y+") ["+(P.x-1000)+";"+(P.y-1000)+"]");
        //Avant de calculer les tangentes, on vérifie que S ou E sont différents
        //de P sinon le résultat sera faussé.
        //Si P = S pas la peine de calculer car P = P' (et division par 0 impossible).
        if(P.equals(S)){
            System.out.println("Impossibilité de calculer avec un point identique.");
            return P;
        }
        //Si P = E pas la peine de calculer car P = P' (et calcul faux).
        if(P.equals(E)){
            System.out.println("Impossibilité de calculer avec un point identique.");
            return P;
        }
        //On a un axe virtuel SE et on va calculer l'angle de cet axe sur le
        //plan XY (S = startPoint - E = endPoint).
        //Soit M la projection de E sur l'axe x.
        //Soit N la projection de P sur l'axe x.
        //Relation de trigonométrie :
        // sin ESM = coté opposé / hypoténuse = EM / SE
        // cos ESM = coté adjacent / hypoténuse = SM / SE
        // tan ESM = coté opposé / coté adjacent = EM / SM
        //On va prendre la tangente de ESM :
        // tan ESM = EM / SM = (yE - yS) / (xE - xS)
        double tan_ESM = (E.getY() - S.getY()) / (E.getX() - S.getX());
        double angle_ESM = Math.toDegrees(Math.atan(tan_ESM));
        if(E.getX() - S.getX() > 0 && E.getY() - S.getY() >= 0){
            angle_ESM = Math.toDegrees(Math.atan(tan_ESM));
        }
        if(E.getX() - S.getX() > 0 && E.getY() - S.getY() < 0){
            angle_ESM = Math.toDegrees(Math.atan(tan_ESM)+2*Math.PI);
        }
        if(E.getX() - S.getX() < 0){
            angle_ESM = Math.toDegrees(Math.atan(tan_ESM)+Math.PI);
        }
//        if(E.getX() - S.getX() == 0 && E.getY() - S.getY() > 0){
//            angle_ESM = Math.PI/2;
//        }
//        if(E.getX() - S.getX() == 0 && E.getY() - S.getY() < 0){
//            angle_ESM = 3*Math.PI/2;
//        }
        System.out.println("Tangente de ESM : "+tan_ESM);
        System.out.println("Angle de ESM : "+angle_ESM);        
        //On va prendre la tangente de PSN :
        // tan PSN = PN / SN = (yP - yS) / (xP - xS)
        double tan_PSN = (P.getY() - S.getY()) / (P.getX() - S.getX());
        double angle_PSN = Math.toDegrees(Math.atan(tan_PSN));
        if(P.getX() - S.getX() > 0 && P.getY() - S.getY() >= 0){
            angle_PSN = Math.toDegrees(Math.atan(tan_PSN));
        }
        if(P.getX() - S.getX() > 0 && P.getY() - S.getY() < 0){
            angle_PSN = Math.toDegrees(Math.atan(tan_PSN)+2*Math.PI);
        }
        if(P.getX() - S.getX() < 0){
            angle_PSN = Math.toDegrees(Math.atan(tan_PSN)+Math.PI);
        }
//        if(P.getX() - S.getX() == 0 && P.getY() - S.getY() > 0){
//            angle_PSN = Math.PI/2;
//        }
//        if(P.getX() - S.getX() == 0 && P.getY() - S.getY() < 0){
//            angle_PSN = 3*Math.PI/2;
//        }
        System.out.println("Tangente de PSN : "+tan_PSN);
        System.out.println("Angle de PSN : "+angle_PSN);
        //On calcule l'angle ESP
        // ESP = ESM - PSN
        double angle_ESP = angle_ESM - angle_PSN;        
        System.out.println("Angle de ESP : "+angle_ESP);
        //On calcule la distance SP avec la formule
        // SP = racine carrée(carré(xP-xS)+carré(yP-yS))
        double SP = Math.sqrt(Math.pow(P.getX()-S.getX(), 2)+Math.pow(P.getY()-S.getY(), 2));
        System.out.println("Distance de SP : "+SP);
        //On reporte l'angle ESP afin de trouver la symétrie
        // ESP' = ESP ; P'SM = ESM + ESP
        double angle_PprimeSM = angle_ESM + angle_ESP;
        System.out.println("Angle de P'SM : "+angle_PprimeSM);
        //On rappelle les règles de trigonométrie :
        // xP' = SP x cos P'SM
        // yP' = SP x sin P'SM
        double xPprime = SP * Math.cos(Math.toRadians(angle_PprimeSM)) + S.x;
        double yPprime = SP * Math.sin(Math.toRadians(angle_PprimeSM)) + S.y;
        System.out.println("xP' : "+xPprime);
        System.out.println("yP' : "+yPprime);
        Pprime.setLocation(xPprime, yPprime);
        System.out.println("Point P' ("+Pprime.x+";"+Pprime.y+") ["+(Pprime.x-1000)+";"+(Pprime.y-1000)+"]");
        return Pprime;
    }
    
    //TODO : Prise en charge des bsplines (revoir points de référence pour bspline)
    public List<IShape> shapesWithSym(Layer lay){
        List<IShape> finalShapes = new ArrayList<IShape>();
        //Parcourt la liste une première fois afin d'avoir le premier coté
        //afin de la recopier sans aucune inversion (sans aucune symétrie)
        setOriginalShapes(lay, finalShapes);
        //////////// SYMETRIE /////////////////////////////////////////////
        //Obtient les points de référence
        Point startPoint = getStartPoint(lay);
        Point endPoint = getEndPoint(lay);
        //Parcourt la liste une deuxième fois afin d'avoir le deuxième coté        
        for(int i=lay.getShapesList().getShapes().size()-1; i >=0; i--){
            IShape s = lay.getShapesList().getShapes().get(i);
            if(s instanceof Line && s.isInSelection()){
                Line line = (Line)s;
                Line newline = new Line();
                Point newpoint = new Point();
                //Le premier point est le dernier point et inversement.
                java.awt.Point pPrime1 = getSymmetry(startPoint.getLastPoint(), endPoint.getLastPoint(), line.getLastPoint());
                newline.setOriginPoint(pPrime1.x, pPrime1.y);
                java.awt.Point pPrime2 = getSymmetry(startPoint.getLastPoint(), endPoint.getLastPoint(), line.getOriginPoint());
                newline.setLastPoint(pPrime2.x, pPrime2.y);
                newpoint.setOriginPoint(pPrime2.x, pPrime2.y);
                newpoint.setLastPoint(pPrime2.x, pPrime2.y);
                finalShapes.add(newpoint);
                finalShapes.add(newline);
            }else if(s instanceof Bezier && s.isInSelection()){
                Bezier bezier = (Bezier)s;
                Bezier newbezier = new Bezier();
                Point newpoint = new Point();
                ControlPoint newcp1 = new ControlPoint();
                ControlPoint newcp2 = new ControlPoint();
                java.awt.Point pPrime1 = getSymmetry(startPoint.getLastPoint(), endPoint.getLastPoint(), bezier.getLastPoint());
                newbezier.setOriginPoint(pPrime1.x, pPrime1.y);
                java.awt.Point pPrime2 = getSymmetry(startPoint.getLastPoint(), endPoint.getLastPoint(), bezier.getOriginPoint());
                newbezier.setLastPoint(pPrime2.x, pPrime2.y);
                newpoint.setOriginPoint(pPrime2.x, pPrime2.y);
                newpoint.setLastPoint(pPrime2.x, pPrime2.y);
                java.awt.Point pPrime3 = getSymmetry(startPoint.getLastPoint(), endPoint.getLastPoint(), bezier.getControl1().getLastPoint());                
                java.awt.Point pPrime4 = getSymmetry(startPoint.getLastPoint(), endPoint.getLastPoint(), bezier.getControl2().getLastPoint());
                newcp1.setOriginPoint(pPrime4.x, pPrime4.y);
                newcp1.setLastPoint(pPrime4.x, pPrime4.y);
                newcp2.setOriginPoint(pPrime3.x, pPrime3.y);
                newcp2.setLastPoint(pPrime3.x, pPrime3.y);
                newbezier.setControl1(newcp1);
                newbezier.setControl2(newcp2);
                finalShapes.add(newpoint);
                finalShapes.add(newbezier);
                finalShapes.add(newcp1);
                finalShapes.add(newcp2);
            }else if(s instanceof BSpline && s.isInSelection()){
                BSpline bs = (BSpline)s;
                java.awt.Point pPrime1; BSpline newbspline; Point newpoint;
                if(bs.isNextExist()==true){
                    pPrime1 = getSymmetry(startPoint.getLastPoint(), endPoint.getLastPoint(), bs.getNextPoint());
                    newbspline = new BSpline(pPrime1.x, pPrime1.y);
                    newpoint = new Point(pPrime1.x, pPrime1.y);
                    pPrime1 = getSymmetry(startPoint.getLastPoint(), endPoint.getLastPoint(), bs.getLastPoint());
                    newbspline.addPoint(pPrime1.x, pPrime1.y);
                }else{
                    pPrime1 = getSymmetry(startPoint.getLastPoint(), endPoint.getLastPoint(), bs.getLastPoint());
                    newbspline = new BSpline(pPrime1.x, pPrime1.y);
                    newpoint = new Point(pPrime1.x, pPrime1.y);
                }
                for(int j=bs.getControlPoints().size()-2; j>=0; j--){
                    ControlPoint cp = bs.getControlPoints().get(i);
                    pPrime1 = getSymmetry(startPoint.getLastPoint(), endPoint.getLastPoint(), cp.getLastPoint());
                    newbspline.addPoint(pPrime1.x, pPrime1.y);
                }
                finalShapes.add(newpoint);
                finalShapes.add(newbspline);
                
//                BSpline bs = (BSpline)s;
//                List<ControlPoint> lcp = bs.getControlPoints();
//                int lastcp = lcp.size()-1;
//                for(ControlPoint cp : lcp){
//                    int x = (int)cp.getOriginPoint().getX();
//                    int y = (int)cp.getOriginPoint().getY();
//
//                    if(bs.isNextExist()==true && cp.equals(lcp.get(lastcp))==true){
//                        //rien
//                    }else{
//                        
//                    }
//                }
//                if(bs.isClosed()==true){
//                    
//                }
//                if(bs.isNextExist()==true){
//                    int x = (int)bs.getNextPoint().getX();
//                    int y = (int)bs.getNextPoint().getY();
//                }
            }else if(s instanceof Move && s.isInSelection()){
                Move move = (Move)s;
                int x = (int)move.getLastPoint().getX();
                int y = (int)move.getLastPoint().getY();
            }else if(s instanceof ReStart && s.isInSelection()){
                ReStart move = (ReStart)s;
                int x = (int)move.getLastPoint().getX();
                int y = (int)move.getLastPoint().getY();
            }
        }
        return finalShapes;        
    }
    
    private java.awt.Point getPointWithAngle(java.awt.Point S, java.awt.Point E, java.awt.Point P, double angle){
        java.awt.Point Pprime = P;        
        //On va ensuite calculer la distance SP
        //(qui formera le rayon de notre cercle trigonométrique)
        double SP = Math.sqrt(Math.pow(P.getX()-S.getX(), 2)+Math.pow(P.getY()-S.getY(), 2));
        //On va prendre la tangente de PSN :
        // tan PSN = PN / SN = (yP - yS) / (xP - xS)
        double tan_PSN = (P.getY() - S.getY()) / (P.getX() - S.getX());
        double angle_PSN = Math.toDegrees(Math.atan(tan_PSN));
        if(P.getX() - S.getX() > 0 && P.getY() - S.getY() >= 0){
            angle_PSN = Math.toDegrees(Math.atan(tan_PSN));
        }
        if(P.getX() - S.getX() > 0 && P.getY() - S.getY() < 0){
            angle_PSN = Math.toDegrees(Math.atan(tan_PSN)+2*Math.PI);
        }
        if(P.getX() - S.getX() < 0){
            angle_PSN = Math.toDegrees(Math.atan(tan_PSN)+Math.PI);
        }
//        if(P.getX() - S.getX() == 0 && P.getY() - S.getY() > 0){
//            angle_PSN = Math.PI/2;
//        }
//        if(P.getX() - S.getX() == 0 && P.getY() - S.getY() < 0){
//            angle_PSN = 3*Math.PI/2;
//        }
        System.out.println("Tangente de PSN : "+tan_PSN);
        System.out.println("Angle de PSN : "+angle_PSN);
        //On va faire une translation de P avec l'axe SE
        P = new java.awt.Point(P.x+(E.x-S.x),P.y+(E.y-S.y));
        System.out.println("S : ("+S.x+";"+S.y+") ["+(S.x-1000)+";"+(S.y-1000)+"]");
        System.out.println("E : ("+E.x+";"+E.y+") ["+(E.x-1000)+";"+(E.y-1000)+"]");
        System.out.println("P avec translation : ("+P.x+";"+P.y+") ["+(P.x-1000)+";"+(P.y-1000)+"]");
        
        if(P.equals(E)){ System.out.println("P=E"); return P; }
//        if(P.equals(S)){ System.out.println("P=S"); return P; }
        //On fait de la trigonométrie
        double xPprime = SP * Math.cos(Math.toRadians(angle+angle_PSN)) + E.x;
        double yPprime = SP * Math.sin(Math.toRadians(angle+angle_PSN)) + E.y;
        System.out.println("angle : "+angle+"; angle_PSN : "+angle_PSN+"; total : "+(angle+angle_PSN));
        System.out.println("xP' : "+xPprime);
        System.out.println("yP' : "+yPprime);
        Pprime.setLocation(xPprime, yPprime);        
        System.out.println("Point P' ("+Pprime.x+";"+Pprime.y+") ["+(Pprime.x-1000)+";"+(Pprime.y-1000)+"]");        
        if(Pprime.equals(S) && Math.abs(angle)!=180d){ return E; }
        return Pprime;
    }
    
    public List<IShape> shapesAroundASide(Layer lay, int side, double angle){
        List<IShape> finalShapes = new ArrayList<IShape>();
        //Parcourt la liste une première fois afin d'avoir le premier coté
        //afin de la recopier sans aucune inversion (sans aucune symétrie)
        setOriginalShapes(lay, finalShapes);
        //////////// OPERATIONS //////////////////////////////////////////
        //Obtient les points de référence
        Point startPoint = getStartPoint(lay);
        Point endPoint = getEndPoint(lay);
        //On fait les opérations en prenant le endPoint comme lastPoint
        //On va ensuite calculer la position des points selon la distance SE
        //sachant que S=startPoint et E=endPoint.
        //Puis basculer le tout suivant un angle défini
        Point lastPoint = endPoint;
        
        for(int i=1;i<side;i++){
            for(IShape s : lay.getShapesList().getShapes()){
                if(s instanceof Line && s.isInSelection()){
                    Line line = (Line)s;
                    Line newline = new Line();
                    Point newpoint = new Point();
                    java.awt.Point pPrime1 = getPointWithAngle(startPoint.getLastPoint(), lastPoint.getLastPoint(), line.getOriginPoint(), angle);
                    newline.setOriginPoint(pPrime1.x, pPrime1.y);
                    java.awt.Point pPrime2 = getPointWithAngle(startPoint.getLastPoint(), lastPoint.getLastPoint(), line.getLastPoint(), angle);
                    newline.setLastPoint(pPrime2.x, pPrime2.y);
                    newpoint.setOriginPoint(pPrime2.x, pPrime2.y);
                    newpoint.setLastPoint(pPrime2.x, pPrime2.y);
                    finalShapes.add(newpoint);
                    finalShapes.add(newline);
                }else if(s instanceof Bezier && s.isInSelection()){
                    Bezier bezier = (Bezier)s;
                    Bezier newbezier = new Bezier();
                    Point newpoint = new Point();
                    ControlPoint newcp1 = new ControlPoint();
                    ControlPoint newcp2 = new ControlPoint();
                    java.awt.Point pPrime1 = getPointWithAngle(startPoint.getLastPoint(), lastPoint.getLastPoint(), bezier.getOriginPoint(), angle);
                    newbezier.setOriginPoint(pPrime1.x, pPrime1.y);
                    java.awt.Point pPrime2 = getPointWithAngle(startPoint.getLastPoint(), lastPoint.getLastPoint(), bezier.getLastPoint(), angle);
                    newbezier.setLastPoint(pPrime2.x, pPrime2.y);
                    newpoint.setOriginPoint(pPrime2.x, pPrime2.y);
                    newpoint.setLastPoint(pPrime2.x, pPrime2.y);
                    java.awt.Point pPrime3 = getPointWithAngle(startPoint.getLastPoint(), lastPoint.getLastPoint(), bezier.getControl1().getLastPoint(), angle);                
                    java.awt.Point pPrime4 = getPointWithAngle(startPoint.getLastPoint(), lastPoint.getLastPoint(), bezier.getControl2().getLastPoint(), angle);
                    newcp1.setOriginPoint(pPrime3.x, pPrime3.y);
                    newcp1.setLastPoint(pPrime3.x, pPrime3.y);
                    newcp2.setOriginPoint(pPrime4.x, pPrime4.y);
                    newcp2.setLastPoint(pPrime4.x, pPrime4.y);
                    newbezier.setControl1(newcp1);
                    newbezier.setControl2(newcp2);
                    finalShapes.add(newpoint);
                    finalShapes.add(newbezier);
                    finalShapes.add(newcp1);
                    finalShapes.add(newcp2);
                }else if(s instanceof BSpline && s.isInSelection()){
//                    BSpline bs = (BSpline)s;
//                    java.awt.Point pPrime1; BSpline newbspline; Point newpoint;
//                    if(bs.isNextExist==true){
//                        pPrime1 = getSymmetry(startPoint.getLastPoint(), endPoint.getLastPoint(), bs.getNextPoint());
//                        newbspline = new BSpline(pPrime1.x, pPrime1.y);
//                        newpoint = new Point(pPrime1.x, pPrime1.y);
//                        pPrime1 = getSymmetry(startPoint.getLastPoint(), endPoint.getLastPoint(), bs.getLastPoint());
//                        newbspline.addPoint(pPrime1.x, pPrime1.y);
//                    }else{
//                        pPrime1 = getSymmetry(startPoint.getLastPoint(), endPoint.getLastPoint(), bs.getLastPoint());
//                        newbspline = new BSpline(pPrime1.x, pPrime1.y);
//                        newpoint = new Point(pPrime1.x, pPrime1.y);
//                    }
//                    for(int j=bs.getControlPoints().size()-2; j>=0; j--){
//                        ControlPoint cp = bs.getControlPoints().get(i);
//                        pPrime1 = getSymmetry(startPoint.getLastPoint(), endPoint.getLastPoint(), cp.getLastPoint());
//                        newbspline.addPoint(pPrime1.x, pPrime1.y);
//                    }
//                    finalShapes.add(newpoint);
//                    finalShapes.add(newbspline);

    //                BSpline bs = (BSpline)s;
    //                List<ControlPoint> lcp = bs.getControlPoints();
    //                int lastcp = lcp.size()-1;
    //                for(ControlPoint cp : lcp){
    //                    int x = (int)cp.getOriginPoint().getX();
    //                    int y = (int)cp.getOriginPoint().getY();
    //
    //                    if(bs.isNextExist()==true && cp.equals(lcp.get(lastcp))==true){
    //                        //rien
    //                    }else{
    //                        
    //                    }
    //                }
    //                if(bs.isClosed()==true){
    //                    
    //                }
    //                if(bs.isNextExist()==true){
    //                    int x = (int)bs.getNextPoint().getX();
    //                    int y = (int)bs.getNextPoint().getY();
    //                }
                }else if(s instanceof Move && s.isInSelection()){
//                    Move move = (Move)s;
//                    int x = (int)move.getLastPoint().getX();
//                    int y = (int)move.getLastPoint().getY();
                }else if(s instanceof ReStart && s.isInSelection()){
//                    ReStart move = (ReStart)s;
//                    int x = (int)move.getLastPoint().getX();
//                    int y = (int)move.getLastPoint().getY();
                }
            }
            startPoint = lastPoint;
            lastPoint = null;
            for(int k=finalShapes.size()-1; k>=0; k--){
                IShape ks = finalShapes.get(k);
                if(ks instanceof Point && lastPoint == null){
                    lastPoint = (Point)ks;
                }
            }
        }
        
        return finalShapes;
    }
    
    private java.awt.Point getSimplePoint(java.awt.Point S, java.awt.Point E, java.awt.Point P){
        java.awt.Point Pprime = P;
        //Ecart de x entre S et E
        double x = E.getX() - S.getX();
        //Ecart de y entre S et E
        double y = E.getY() - S.getY();
        //On reporte l'écart sur P
        Pprime.setLocation(P.getX()+x, P.getY()+y);
        return Pprime;
    }
    
    public List<IShape> shapesOneAfter(Layer lay, int times){
        List<IShape> finalShapes = new ArrayList<IShape>();
        //Parcourt la liste une première fois afin d'avoir le premier coté
        //afin de la recopier sans aucune inversion (sans aucune symétrie)
        setOriginalShapes(lay, finalShapes);
        //////////// OPERATIONS //////////////////////////////////////////
        //Obtient les points de référence
        Point startPoint = getStartPoint(lay);
        Point endPoint = getEndPoint(lay);
        //On fait les opérations en prenant le endPoint comme lastPoint
        //On va ensuite calculer la position des points selon la distance SE
        //sachant que S=startPoint et E=endPoint.
        //Puis basculer le tout suivant un angle défini
        Point lastPoint = endPoint;
        
        for(int i=1;i<times;i++){
            for(IShape s : lay.getShapesList().getShapes()){
                if(s instanceof Line && s.isInSelection()){
                    Line line = (Line)s;
                    Line newline = new Line();
                    Point newpoint = new Point();
                    java.awt.Point pPrime1 = getSimplePoint(startPoint.getLastPoint(), lastPoint.getLastPoint(), line.getOriginPoint());
                    newline.setOriginPoint(pPrime1.x, pPrime1.y);
                    java.awt.Point pPrime2 = getSimplePoint(startPoint.getLastPoint(), lastPoint.getLastPoint(), line.getLastPoint());
                    newline.setLastPoint(pPrime2.x, pPrime2.y);
                    newpoint.setOriginPoint(pPrime2.x, pPrime2.y);
                    newpoint.setLastPoint(pPrime2.x, pPrime2.y);
                    finalShapes.add(newpoint);
                    finalShapes.add(newline);
                }else if(s instanceof Bezier && s.isInSelection()){
                    Bezier bezier = (Bezier)s;
                    Bezier newbezier = new Bezier();
                    Point newpoint = new Point();
                    ControlPoint newcp1 = new ControlPoint();
                    ControlPoint newcp2 = new ControlPoint();
                    java.awt.Point pPrime1 = getSimplePoint(startPoint.getLastPoint(), lastPoint.getLastPoint(), bezier.getOriginPoint());
                    newbezier.setOriginPoint(pPrime1.x, pPrime1.y);
                    java.awt.Point pPrime2 = getSimplePoint(startPoint.getLastPoint(), lastPoint.getLastPoint(), bezier.getLastPoint());
                    newbezier.setLastPoint(pPrime2.x, pPrime2.y);
                    newpoint.setOriginPoint(pPrime2.x, pPrime2.y);
                    newpoint.setLastPoint(pPrime2.x, pPrime2.y);
                    java.awt.Point pPrime3 = getSimplePoint(startPoint.getLastPoint(), lastPoint.getLastPoint(), bezier.getControl1().getLastPoint());                
                    java.awt.Point pPrime4 = getSimplePoint(startPoint.getLastPoint(), lastPoint.getLastPoint(), bezier.getControl2().getLastPoint());
                    newcp1.setOriginPoint(pPrime3.x, pPrime3.y);
                    newcp1.setLastPoint(pPrime3.x, pPrime3.y);
                    newcp2.setOriginPoint(pPrime4.x, pPrime4.y);
                    newcp2.setLastPoint(pPrime4.x, pPrime4.y);
                    newbezier.setControl1(newcp1);
                    newbezier.setControl2(newcp2);
                    finalShapes.add(newpoint);
                    finalShapes.add(newbezier);
                    finalShapes.add(newcp1);
                    finalShapes.add(newcp2);
                }else if(s instanceof BSpline && s.isInSelection()){
//                    BSpline bs = (BSpline)s;
//                    java.awt.Point pPrime1; BSpline newbspline; Point newpoint;
//                    if(bs.isNextExist==true){
//                        pPrime1 = getSymmetry(startPoint.getLastPoint(), endPoint.getLastPoint(), bs.getNextPoint());
//                        newbspline = new BSpline(pPrime1.x, pPrime1.y);
//                        newpoint = new Point(pPrime1.x, pPrime1.y);
//                        pPrime1 = getSymmetry(startPoint.getLastPoint(), endPoint.getLastPoint(), bs.getLastPoint());
//                        newbspline.addPoint(pPrime1.x, pPrime1.y);
//                    }else{
//                        pPrime1 = getSymmetry(startPoint.getLastPoint(), endPoint.getLastPoint(), bs.getLastPoint());
//                        newbspline = new BSpline(pPrime1.x, pPrime1.y);
//                        newpoint = new Point(pPrime1.x, pPrime1.y);
//                    }
//                    for(int j=bs.getControlPoints().size()-2; j>=0; j--){
//                        ControlPoint cp = bs.getControlPoints().get(i);
//                        pPrime1 = getSymmetry(startPoint.getLastPoint(), endPoint.getLastPoint(), cp.getLastPoint());
//                        newbspline.addPoint(pPrime1.x, pPrime1.y);
//                    }
//                    finalShapes.add(newpoint);
//                    finalShapes.add(newbspline);

    //                BSpline bs = (BSpline)s;
    //                List<ControlPoint> lcp = bs.getControlPoints();
    //                int lastcp = lcp.size()-1;
    //                for(ControlPoint cp : lcp){
    //                    int x = (int)cp.getOriginPoint().getX();
    //                    int y = (int)cp.getOriginPoint().getY();
    //
    //                    if(bs.isNextExist()==true && cp.equals(lcp.get(lastcp))==true){
    //                        //rien
    //                    }else{
    //                        
    //                    }
    //                }
    //                if(bs.isClosed()==true){
    //                    
    //                }
    //                if(bs.isNextExist()==true){
    //                    int x = (int)bs.getNextPoint().getX();
    //                    int y = (int)bs.getNextPoint().getY();
    //                }
                }else if(s instanceof Move && s.isInSelection()){
//                    Move move = (Move)s;
//                    int x = (int)move.getLastPoint().getX();
//                    int y = (int)move.getLastPoint().getY();
                }else if(s instanceof ReStart && s.isInSelection()){
//                    ReStart move = (ReStart)s;
//                    int x = (int)move.getLastPoint().getX();
//                    int y = (int)move.getLastPoint().getY();
                }
            }
            startPoint = lastPoint;
            lastPoint = null;
            for(int k=finalShapes.size()-1; k>=0; k--){
                IShape ks = finalShapes.get(k);
                if(ks instanceof Point && lastPoint == null){
                    lastPoint = (Point)ks;
                }
            }
        }
        
        return finalShapes;
    }
}