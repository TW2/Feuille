/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smallboxforfansub.drawing.lib;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import smallboxforfansub.drawing.operation.Center;
import smallboxforfansub.drawing.operation.Resize;
import smallboxforfansub.drawing.operation.Shear;
import smallboxforfansub.drawing.operation.Translation;

/**
 * Une couche regroupe l'ensemble des éléments nécessaires à son bon fonctionnement.
 * @author The Wingate 2940
 */
public class Layer {
    
    java.awt.Point first = null;
    java.awt.Point last = null;
    ShapesList slist = new ShapesList();
    List<IShape> changelist = null;
    List<Remember> reList = new ArrayList<Remember>();
    
    boolean isSelected = false;
    boolean isFirst = false;
    
    Color pathColor = Color.green;
    String name = "";
    
    private Center center = new Center();
    private Resize resize = new Resize();
    private Shear shear = new Shear();
    private Translation translation = new Translation();
    
    public Layer(){
        
    }
    
    /** Configure le 1er point. */
    public void setFirstPoint(java.awt.Point first){
        this.first = first;
    }
    
    /** Obtient le 1er point. */
    public java.awt.Point getFirstPoint(){
        return first;
    }
    
    /** Configure le 2eme point. */
    public void setLastPoint(java.awt.Point last){
        this.last = last;
    }
    
    /** Obtient le 2eme point. */
    public java.awt.Point getLastPoint(){
        return last;
    }
    
    /** Configure la liste de forme. */
    public void setShapesList(ShapesList slist){
        this.slist = slist;
    }
    
    /** Obtient la liste de forme. */
    public ShapesList getShapesList(){
        return slist;
    }
    
    /** Configure la liste des changements. */
    public void setChangelist(java.util.List<IShape> changelist){
        this.changelist = changelist;
    }
    
    /** Obtient la liste des changements. */
    public java.util.List<IShape> getChangelist(){
        return changelist;
    }
    
    /** Obtient le chemin à partir des formes contenu dans slist. */
    public GeneralPath getGeneralPath(){
        GeneralPath gp = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
        int count = 0; IShape lastShape = null;
        for(IShape s : slist.getShapes()){
            // Add to the path
            if(s instanceof Line){
                if(lastShape!=null && lastShape instanceof BSpline){
                    BSpline bs = (BSpline)lastShape;
                    if(bs.isNextExist()==true){
                        gp.lineTo(bs.getLastPoint().getX(), bs.getLastPoint().getY());
                    }
                }
                Line l = (Line)s;
                if(count==0){
                    gp.moveTo(l.getOriginPoint().getX(), l.getOriginPoint().getY());
                }else{
                    gp.lineTo(l.getLastPoint().getX(), l.getLastPoint().getY());
                }
            }else if(s instanceof Bezier){
                if(lastShape!=null && lastShape instanceof BSpline){
                    BSpline bs = (BSpline)lastShape;
                    if(bs.isNextExist()==true){
                        gp.lineTo(bs.getLastPoint().getX(), bs.getLastPoint().getY());
                    }
                }
                Bezier b = (Bezier)s;
                if(count==0){
                    gp.moveTo(b.getOriginPoint().getX(), b.getOriginPoint().getY());
                }else{
                    gp.curveTo(b.getControl1Point().getX(), b.getControl1Point().getY(),
                            b.getControl2Point().getX(), b.getControl2Point().getY(),
                            b.getLastPoint().getX(), b.getLastPoint().getY());
                }
            }else if(s instanceof Point){
                if(lastShape!=null && lastShape instanceof BSpline){
                    BSpline bs = (BSpline)lastShape;
                    if(bs.isNextExist()==true){
                        gp.lineTo(bs.getLastPoint().getX(), bs.getLastPoint().getY());
                    }
                }
                //If this is the first point (we always start drawing with a point)
                if(count==0){
                    Point p = (Point)s;
                    gp.moveTo(p.getOriginPoint().getX(), p.getOriginPoint().getY());
                }
            }else if(s instanceof BSpline){
                if(lastShape!=null && lastShape instanceof BSpline){
                    BSpline bs = (BSpline)lastShape;
                    if(bs.isNextExist()==true){
                        gp.lineTo(bs.getLastPoint().getX(), bs.getLastPoint().getY());
                    }
                }
                BSpline bs = (BSpline)s;
                List<Bezier> lb = bs.getAllBeziers();
                int index = 0;
                for(Bezier b : lb){
                    if(index==0){
                        //approximation de la première ligne qui devrait être un bézier
                        //calcul du premier point de controle (d'après le point de contrôle du bézier)
                        int xo = (int)b.getOriginPoint().getX();
                        int yo = (int)b.getOriginPoint().getY();
                        int xa = (int)b.getControl1Point().getX();
                        int ya = (int)b.getControl1Point().getY();
                        int xdiff = xa-xo; int ydiff = ya-yo;
                        int xb = xo-xdiff; int yb = yo-ydiff;
                        gp.quadTo(xb, yb, xo, yo);
                    }
                    try{                         
                        gp.curveTo(b.getControl1Point().getX(), b.getControl1Point().getY(),
                                    b.getControl2Point().getX(), b.getControl2Point().getY(),
                                    b.getLastPoint().getX(), b.getLastPoint().getY());
                    }catch(Exception e){
                        System.out.println(e.getMessage());
                    }
                    index+=1;
                }
                //on essaie de fermer la forme si demandé
                if(bs.isClosed()==true){
                    List<ControlPoint> lcp = bs.getControlPoints();
                    Bezier lastbezier = lb.get(lb.size()-1);
                    //approximation de la première ligne qui devrait être un bézier
                    //calcul du premier point de controle (d'après le point de contrôle du bézier)
                    int xo = (int)lastbezier.getLastPoint().getX();
                    int yo = (int)lastbezier.getLastPoint().getY();
                    int xa = (int)lastbezier.getControl2Point().getX();
                    int ya = (int)lastbezier.getControl2Point().getY();
                    int xdiff = xa-xo; int ydiff = ya-yo;
                    int xb = xo-xdiff; int yb = yo-ydiff;
                    int last_index = lcp.size()-1;
                    gp.curveTo(
                            xb,
                            yb,
                            lcp.get(last_index).getOriginPoint().getX(),
                            lcp.get(last_index).getOriginPoint().getY(),
                            lcp.get(0).getOriginPoint().getX(),
                            lcp.get(0).getOriginPoint().getY());
                }
                //on essaie d'étendre la forme si demandé
                if(bs.isNextExist()==true){
                    Bezier lastbezier = lb.get(lb.size()-1);
                    //approximation de la première ligne qui devrait être un bézier
                    //calcul du premier point de controle (d'après le point de contrôle du bézier)
                    int xo = (int)lastbezier.getLastPoint().getX();
                    int yo = (int)lastbezier.getLastPoint().getY();
                    int xa = (int)lastbezier.getControl2Point().getX();
                    int ya = (int)lastbezier.getControl2Point().getY();
                    int xdiff = xa-xo; int ydiff = ya-yo;
                    int xb = xo-xdiff; int yb = yo-ydiff;
                    gp.quadTo(
                            xb, 
                            yb, 
                            bs.getNextPoint().getX(),
                            bs.getNextPoint().getY());                    
                }
            }else if(s instanceof Move){
                if(lastShape!=null && lastShape instanceof BSpline){
                    BSpline bs = (BSpline)lastShape;
                    if(bs.isNextExist()==true){
                        gp.lineTo(bs.getLastPoint().getX(), bs.getLastPoint().getY());
                    }
                }
                //If this is the first point (we always start drawing with a point)
                Move m = (Move)s;
                try{
                    gp.lineTo(m.getLastPoint().getX(), m.getLastPoint().getY());
                }catch(Exception e){
                    gp.moveTo(m.getLastPoint().getX(), m.getLastPoint().getY());
                }                
            }else if(s instanceof ReStart){
                if(lastShape!=null && lastShape instanceof BSpline){
                    BSpline bs = (BSpline)lastShape;
                    if(bs.isNextExist()==true){
                        gp.lineTo(bs.getLastPoint().getX(), bs.getLastPoint().getY());
                    }
                }
                //If this is the first point (we always start drawing with a point)
                ReStart m = (ReStart)s;
                gp.moveTo(m.getLastPoint().getX(), m.getLastPoint().getY());
            }
            count+=1;
            lastShape = s;
        }
        return gp;
    }
    
    /** Obtient si oui ou non la couche doit être dessinée. */
    public boolean isSelected(){
        return isSelected;
    }
    
    /** Définit si oui ou non cette couche est sélectionné. */
    public void setSelected(boolean b){
        isSelected = b;
    }
    
    /** Obtient si oui ou non la couche est la première. */
    public boolean isFirst(){
        return isFirst;
    }
    
    /** Définit si oui ou non cette couche est la première. */
    public void setFirst(boolean b){
        isFirst = b;
    }
    
    /** Obtient la couleur du chemin. */
    public Color getColor(){
        return pathColor;
    }
    
    /** Définit la couleur du chemin. */
    public void setColor(Color c){
        pathColor = c;
    }
    
    /** Obtient le nom. */
    public String getName(){
        return name;
    }
    
    /** Définit le nom. */
    public void setName(String name){
        this.name = name;
    }
    
    /** Configure la liste des changements. */
    public void setRememberlist(List<Remember> reList){
        this.reList = reList;
    }
    
    /** Configure la liste des changements. */
    public List<Remember> getRememberlist(boolean removeInactive){
        if(removeInactive==true){
            for(Remember re : reList){
                if(re.isActive()==false){
                    reList.remove(re);
                }
            }
            
        }
        return reList;
    }
    
    public void addRemember(IShape s){
        removeRemember();
        Remember re = new Remember(s);
        re.setActive(true);
        reList.add(re);
//        dlm.addElement(re);
    }
    
    public void virtualRemoveRemember(){
        //Obtient le dernier Remember actif
        Object[] table = reList.toArray();
        for(int i=table.length-1;i>=0;i--){
            if(table[i] instanceof Remember){
                Remember re = (Remember)table[i];
                if(re.isActive()){
                    re.setActive(false);
//                    listRemember.repaint();
                    return;
                }
            }
        }
    }
    
    public void addVirtualRemember(){
        //Obtient le dernier Remember actif
        Object[] table = reList.toArray();
        for(int i=0;i<table.length;i++){
            if(table[i] instanceof Remember){
                Remember re = (Remember)table[i];
                if(re.isActive()==false){
                    re.setActive(true);
                    getShapesList().addShape(re.getShape());
//                    listRemember.repaint();
//                    sh.updateGeneralPath(lay.getGeneralPath());
//                    sh.updateShapesList(lay.getShapesList());
//                    updateCommands();
                    return;
                }
            }
        }
    }
    
    public void removeRemember(){
        //Obtient le dernier Remember actif
        for(Object o : reList.toArray()){
            if(o instanceof Remember){
                Remember re = (Remember)o;
                if(re.isActive()==false){
                    reList.remove(re);
                }
            }
        }
    }
    
    public void clearRemembers(){
        reList.clear();
    }
    
    public ShapesList getCopiedShapes(){
        ShapesList sl = getShapesList();        
        ShapesList newSL = new ShapesList();
        for(IShape s : sl.getCopiesList()){
            newSL.addShape(s);
        }
        return newSL;
    }
    
    public void glyphToShape(String glyph, Font f){
        BufferedImage bi = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.createGraphics();
        Graphics2D g2d = (Graphics2D)g;
        FontRenderContext frc = g2d.getFontRenderContext();
        TextLayout txt = new TextLayout(glyph, f, frc);
        AffineTransform transform = new AffineTransform();
        transform.setToTranslation(500, 500);
        java.awt.Shape outline = txt.getOutline(transform);
        
        PathIterator pi = outline.getPathIterator(transform);
        double lastX = 0, lastY = 0, beginX = 0, beginY = 0;

        while(!pi.isDone()) {
            float[] coords = new float[6];
            int type = pi.currentSegment(coords);

            switch(type) {
                case PathIterator.SEG_MOVETO :                        
                    ReStart m = new ReStart();
                    m.setOriginPoint((int)coords[0], (int)coords[1]);
                    m.setLastPoint((int)coords[0], (int)coords[1]);
                    slist.addShape(m); addRemember(m);
                    //Point p = new Point();
                    //p.setOriginPoint((int)coords[0], (int)coords[1]);
                    //p.setLastPoint((int)coords[0], (int)coords[1]);
                    //slist.addShape(p); addRemember(p);
                    beginX = coords[0]; beginY = coords[1];
                    lastX = coords[0]; lastY = coords[1];
                    break;
                case PathIterator.SEG_LINETO :
                    Point p2 = new Point();
                    p2.setOriginPoint((int)lastX, (int)lastY);
                    p2.setLastPoint((int)lastX, (int)lastY);
                    slist.addShape(p2); addRemember(p2);
                    Line l = new Line();
                    l.setOriginPoint((int)lastX, (int)lastY);
                    l.setLastPoint((int)coords[0], (int)coords[1]);
                    slist.addShape(l); addRemember(l);                    
                    lastX = coords[0]; lastY = coords[1];
                    break;
                case PathIterator.SEG_QUADTO :
                    Point pq = new Point();
                    pq.setOriginPoint((int)lastX, (int)lastY);
                    pq.setLastPoint((int)lastX, (int)lastY);
                    slist.addShape(pq); addRemember(pq);
                    Bezier q = Bezier.createCubicFromQuad(
                            (int)lastX, (int)lastY, 
                            (int)coords[0], (int)coords[1], 
                            (int)coords[2], (int)coords[3]);
                    slist.addShape(q); addRemember(q);                    
                    ControlPoint cp1q = q.getControl1();
                    ControlPoint cp2q = q.getControl2();
                    slist.addShape(cp1q); addRemember(cp1q);
                    slist.addShape(cp2q); addRemember(cp2q);
                    lastX = coords[2]; lastY = coords[3];
                    break;
                case PathIterator.SEG_CUBICTO :
                    Point p4 = new Point();
                    p4.setOriginPoint((int)lastX, (int)lastY);
                    p4.setLastPoint((int)lastX, (int)lastY);
                    slist.addShape(p4); addRemember(p4);
                    Bezier c = new Bezier();
                    c.setOriginPoint((int)lastX, (int)lastY);
                    c.setLastPoint((int)coords[2], (int)coords[3]);
                    c.setControl1Point((int)coords[0], (int)coords[1]);
                    c.setControl2Point((int)coords[2], (int)coords[3]);
                    slist.addShape(c); addRemember(c);                    
                    ControlPoint cp1 = c.getControl1();
                    ControlPoint cp2 = c.getControl2();
                    slist.addShape(cp1); addRemember(cp1);
                    slist.addShape(cp2); addRemember(cp2);
                    lastX = coords[4]; lastY = coords[5];
                    break;
                case PathIterator.SEG_CLOSE :
                    Point p5 = new Point();
                    p5.setOriginPoint((int)lastX, (int)lastY);
                    p5.setLastPoint((int)lastX, (int)lastY);
                    slist.addShape(p5); addRemember(p5);
                    Line l2 = new Line();
                    l2.setOriginPoint((int)lastX, (int)lastY);
                    l2.setLastPoint((int)beginX, (int)beginY);
                    slist.addShape(l2); addRemember(l2);                    
            }
            pi.next();
        }
        setFirstPoint(new java.awt.Point((int)lastX, (int)lastY));
        setLastPoint(new java.awt.Point((int)lastX, (int)lastY));
    }
    
    /** Met à jour le first point et le last point avec la liste
     * de shapes interne */
    public void updateEndPoint(){
        Point endPoint = slist.getLastPoint();
        setFirstPoint(endPoint.getLastPoint());
        setLastPoint(endPoint.getLastPoint());
    }
    
    public Center getCenter(){
        return center;
    }
    
    public Resize getResize(){
        return resize;
    }
    
    public Shear getShear(){
        return shear;
    }
    
    public Translation getTranslation(){
        return translation;
    }
    
    /** Rotation d'un point par rapport à un autre.
     * @param xo Centre en xo
     * @param yo Centre en yo
     * @param xa Abscisse du point
     * @param ya Ordonnée du point
     * @param angle Angle en degré (positif ou négatif)
     * @return Le point modifié avec la rotation */
    private java.awt.Point rotateWithPoint(int xo, int yo, int xa, int ya, double angle){
        java.awt.Point S = new java.awt.Point(xo, yo);
        java.awt.Point P = new java.awt.Point(xa, ya);
        java.awt.Point Pprime = P;
        double SP = Math.sqrt(Math.pow(P.getX()-S.getX(), 2)+Math.pow(P.getY()-S.getY(), 2));
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
        double xPprime = SP * Math.cos(Math.toRadians(angle+angle_PSN)) + S.getX();
        double yPprime = SP * Math.sin(Math.toRadians(angle+angle_PSN)) + S.getY();
        Pprime.setLocation(xPprime, yPprime);
        return Pprime;
    }
    
    /** Rotation d'un point par rapport à un autre.
     * @param xo Centre en xo
     * @param yo Centre en yo
     * @param angle Angle en degré (positif ou négatif) */
    public void rotate(int xo, int yo, double angle, Layer lay){
        if(lay==null){
            //TODO getCurrentLayer()
//            lay = asssketchpad.AssSketchpad.getCurrentLayer();
        }
        java.awt.Point pa; int xa, ya;
        for(IShape s : lay.getShapesList().getShapes()){
            if(s instanceof ReStart){
                ReStart m = (ReStart)s;
                xa = m.getOriginPoint().x; ya = m.getOriginPoint().y;
                pa = rotateWithPoint(xo, yo, xa, ya, angle);
                m.setOriginPoint(pa.x, pa.y);
                xa = m.getLastPoint().x; ya = m.getLastPoint().y;
                pa = rotateWithPoint(xo, yo, xa, ya, angle);
                m.setLastPoint(pa.x, pa.y);
            }else if(s instanceof Move){
                Move n = (Move)s;
                xa = n.getOriginPoint().x; ya = n.getOriginPoint().y;
                pa = rotateWithPoint(xo, yo, xa, ya, angle);
                n.setOriginPoint(pa.x, pa.y);
                xa = n.getLastPoint().x; ya = n.getLastPoint().y;
                pa = rotateWithPoint(xo, yo, xa, ya, angle);
                n.setLastPoint(pa.x, pa.y);
            }else if(s instanceof Point){
                Point p = (Point)s;
                xa = p.getOriginPoint().x; ya = p.getOriginPoint().y;
                pa = rotateWithPoint(xo, yo, xa, ya, angle);
                p.setOriginPoint(pa.x, pa.y);
                xa = p.getLastPoint().x; ya = p.getLastPoint().y;
                pa = rotateWithPoint(xo, yo, xa, ya, angle);
                p.setLastPoint(pa.x, pa.y);
            }else if(s instanceof Line){
                Line l = (Line)s;
                xa = l.getOriginPoint().x; ya = l.getOriginPoint().y;
                pa = rotateWithPoint(xo, yo, xa, ya, angle);
                l.setOriginPoint(pa.x, pa.y);
                xa = l.getLastPoint().x; ya = l.getLastPoint().y;
                pa = rotateWithPoint(xo, yo, xa, ya, angle);
                l.setLastPoint(pa.x, pa.y);
            }else if(s instanceof Bezier){
                Bezier b = (Bezier)s;
                xa = b.getOriginPoint().x; ya = b.getOriginPoint().y;
                pa = rotateWithPoint(xo, yo, xa, ya, angle);
                b.setOriginPoint(pa.x, pa.y);
                xa = b.getLastPoint().x; ya = b.getLastPoint().y;
                pa = rotateWithPoint(xo, yo, xa, ya, angle);
                b.setLastPoint(pa.x, pa.y);
            }else if(s instanceof ControlPoint){
                ControlPoint cp = (ControlPoint)s;
                xa = cp.getOriginPoint().x; ya = cp.getOriginPoint().y;
                pa = rotateWithPoint(xo, yo, xa, ya, angle);
                cp.setOriginPoint(pa.x, pa.y);
                xa = cp.getLastPoint().x; ya = cp.getLastPoint().y;
                pa = rotateWithPoint(xo, yo, xa, ya, angle);
                cp.setLastPoint(pa.x, pa.y);
            }else if(s instanceof BSpline){                
                BSpline bs = (BSpline)s;
                xa = bs.getOriginPoint().x; ya = bs.getOriginPoint().y;
                pa = rotateWithPoint(xo, yo, xa, ya, angle);
                bs.setOriginPoint(pa.x, pa.y);
                xa = bs.getLastPoint().x; ya = bs.getLastPoint().y;
                pa = rotateWithPoint(xo, yo, xa, ya, angle);
                bs.setLastPoint(pa.x, pa.y);
                for(ControlPoint cp : bs.getControlPoints()){
                    xa = cp.getOriginPoint().x; ya = cp.getOriginPoint().y;
                    pa = rotateWithPoint(xo, yo, xa, ya, angle);
                    cp.setOriginPoint(pa.x, pa.y);
                    cp.setLastPoint(pa.x, pa.y);
                }
                if(bs.isNextExist()){
                    xa = bs.getNextPoint().x; ya = bs.getNextPoint().y;
                    pa = rotateWithPoint(xo, yo, xa, ya, angle);
                    bs.setNextPoint(pa.x, pa.y);
                }
                //TODO : Corriger le bug présent = un point s'intercale entre
                //le premier controlpoint et le second controlpoint faussant la
                //manoeuvre. La fonction suivante corrige temporairement le bug.
                bs.removePointAt(0);
            }
        }
    }
    
    private java.awt.Point resizeWithPoint(int x, int y, int xa, int ya, double percent){
        //S est le point issu d'un ReStart, c'est donc le point de référence
        //P est le point d'insertion (pour lequel on veut calculer P')
        java.awt.Point S = new java.awt.Point(x, y);
        java.awt.Point P = new java.awt.Point(xa, ya);
        java.awt.Point Pprime = P;
        //Si P est égal à S alors, on n'a pas besoin de faire le calcule on retourne le même point.
        if(P.equals(S)){ return P; }
        //On veut que le point S soit toujours l'origine
        //On donc calcule la distance de S à P pour en sortir une distance en fonction du pourcentage
        double SP = java.awt.geom.Point2D.distance(S.getX(), S.getY(), P.getX(), P.getY());
        double SPprime = SP*percent/100;
        //On calcule l'angle S afin de savoir où resituer le point P'
        double tanS = (P.getY() - S.getY()) / (P.getX() - S.getX());
        double angleS = Math.toDegrees(Math.atan(tanS));
        if(P.getX() - S.getX() > 0 && P.getY() - S.getY() >= 0){
            angleS = Math.toDegrees(Math.atan(tanS));
        }
        if(P.getX() - S.getX() > 0 && P.getY() - S.getY() < 0){
            angleS = Math.toDegrees(Math.atan(tanS)+2*Math.PI);
        }
        if(P.getX() - S.getX() < 0){
            angleS = Math.toDegrees(Math.atan(tanS)+Math.PI);
        }
        //La distance en fonction du pourcentage vient s'ajouter aux coordonnées de S avec l'angle S.
        double xPprime = SPprime * Math.cos(Math.toRadians(angleS)) + S.getX();
        double yPprime = SPprime * Math.sin(Math.toRadians(angleS)) + S.getY();
        //S.x-(S.x-xo+dx-xo)+(P.getX()-dx)*percent/100, S.y-(S.y-yo+dy-yo)+(P.getY()-dy)*percent/100
        //S.x-(S.x-dx)+(P.getX()-dx)*percent/100, S.y-(S.y-dy)+(P.getY()-dy)*percent/100
        Pprime.setLocation(xPprime,yPprime);
        return Pprime;
    }
    
    public void resize(java.awt.Point M, double percent, Layer lay){
        if(lay==null){
            //TODO getCurrentLayer()
//            lay = asssketchpad.AssSketchpad.getCurrentLayer();
        }
        java.awt.Point pa; int xa, ya;
        for(IShape s : lay.getShapesList().getShapes()){
            if(s instanceof ReStart){
                ReStart m = (ReStart)s;
                xa = m.getOriginPoint().x; ya = m.getOriginPoint().y;
                pa = resizeWithPoint(M.x, M.y, xa, ya, percent);
                m.setOriginPoint(pa.x, pa.y);
                xa = m.getLastPoint().x; ya = m.getLastPoint().y;
                pa = resizeWithPoint(M.x, M.y, xa, ya, percent);
                m.setLastPoint(pa.x, pa.y);
            }else if(s instanceof Move){
                Move n = (Move)s;
                xa = n.getOriginPoint().x; ya = n.getOriginPoint().y;
                pa = resizeWithPoint(M.x, M.y, xa, ya, percent);
                n.setOriginPoint(pa.x, pa.y);
                xa = n.getLastPoint().x; ya = n.getLastPoint().y;
                pa = resizeWithPoint(M.x, M.y, xa, ya, percent);
                n.setLastPoint(pa.x, pa.y);
            }else if(s instanceof Point){
                Point p = (Point)s;
                xa = p.getOriginPoint().x; ya = p.getOriginPoint().y;
                pa = resizeWithPoint(M.x, M.y, xa, ya, percent);
                p.setOriginPoint(pa.x, pa.y);
                xa = p.getLastPoint().x; ya = p.getLastPoint().y;
                pa = resizeWithPoint(M.x, M.y, xa, ya, percent);
                p.setLastPoint(pa.x, pa.y);
            }else if(s instanceof Line){
                Line l = (Line)s;
                xa = l.getOriginPoint().x; ya = l.getOriginPoint().y;
                pa = resizeWithPoint(M.x, M.y, xa, ya, percent);
                l.setOriginPoint(pa.x, pa.y);
                xa = l.getLastPoint().x; ya = l.getLastPoint().y;
                pa = resizeWithPoint(M.x, M.y, xa, ya, percent);
                l.setLastPoint(pa.x, pa.y);
            }else if(s instanceof Bezier){
                Bezier b = (Bezier)s;
                xa = b.getOriginPoint().x; ya = b.getOriginPoint().y;
                pa = resizeWithPoint(M.x, M.y, xa, ya, percent);
                b.setOriginPoint(pa.x, pa.y);
                xa = b.getLastPoint().x; ya = b.getLastPoint().y;
                pa = resizeWithPoint(M.x, M.y, xa, ya, percent);
                b.setLastPoint(pa.x, pa.y);
            }else if(s instanceof ControlPoint){
                ControlPoint cp = (ControlPoint)s;
                xa = cp.getOriginPoint().x; ya = cp.getOriginPoint().y;
                pa = resizeWithPoint(M.x, M.y, xa, ya, percent);
                cp.setOriginPoint(pa.x, pa.y);
                xa = cp.getLastPoint().x; ya = cp.getLastPoint().y;
                pa = resizeWithPoint(M.x, M.y, xa, ya, percent);
                cp.setLastPoint(pa.x, pa.y);
            }else if(s instanceof BSpline){
                BSpline bs = (BSpline)s;
                xa = bs.getOriginPoint().x; ya = bs.getOriginPoint().y;
                pa = resizeWithPoint(M.x, M.y, xa, ya, percent);
                bs.setOriginPoint(pa.x, pa.y);
                xa = bs.getLastPoint().x; ya = bs.getLastPoint().y;
                pa = resizeWithPoint(M.x, M.y, xa, ya, percent);
                bs.setLastPoint(pa.x, pa.y);
                for(ControlPoint cp : bs.getControlPoints()){
                    xa = cp.getOriginPoint().x; ya = cp.getOriginPoint().y;
                    pa = resizeWithPoint(M.x, M.y, xa, ya, percent);
                    cp.setOriginPoint(pa.x, pa.y);
                    cp.setLastPoint(pa.x, pa.y);
                }
                if(bs.isNextExist()){
                    xa = bs.getNextPoint().x; ya = bs.getNextPoint().y;
                    pa = resizeWithPoint(M.x, M.y, xa, ya, percent);
                    bs.setNextPoint(pa.x, pa.y);
                }
            }
        }
    }
    
    private java.awt.Point translateWithPoint(double rdx, double rdy, int xa, int ya){
        java.awt.Point P = new java.awt.Point(xa, ya);
        java.awt.Point Pprime = P;
        double xPprime = P.getX() + rdx;
        double yPprime = P.getY() + rdy;
        Pprime.setLocation(xPprime, yPprime);
        return Pprime;
    }
    
    public void translate(List<IShape> pshapes, double rdx, double rdy, Layer lay){
        java.awt.Point pa; int xa, ya;
        for(IShape s : lay.getShapesList().getShapes()){
            if(s instanceof ReStart){
                ReStart m = (ReStart)s;
                xa = m.getOriginPoint().x; ya = m.getOriginPoint().y;
                pa = translateWithPoint(rdx, rdy, xa, ya);
                m.setOriginPoint(pa.x, pa.y);
                xa = m.getLastPoint().x; ya = m.getLastPoint().y;
                pa = translateWithPoint(rdx, rdy, xa, ya);
                m.setLastPoint(pa.x, pa.y);
            }else if(s instanceof Move){
                Move n = (Move)s;
                xa = n.getOriginPoint().x; ya = n.getOriginPoint().y;
                pa = translateWithPoint(rdx, rdy, xa, ya);
                n.setOriginPoint(pa.x, pa.y);
                xa = n.getLastPoint().x; ya = n.getLastPoint().y;
                pa = translateWithPoint(rdx, rdy, xa, ya);
                n.setLastPoint(pa.x, pa.y);
            }else if(s instanceof Point){
                Point p = (Point)s;
                xa = p.getOriginPoint().x; ya = p.getOriginPoint().y;
                pa = translateWithPoint(rdx, rdy, xa, ya);
                p.setOriginPoint(pa.x, pa.y);
                xa = p.getLastPoint().x; ya = p.getLastPoint().y;
                pa = translateWithPoint(rdx, rdy, xa, ya);
                p.setLastPoint(pa.x, pa.y);
            }else if(s instanceof Line){
                Line l = (Line)s;
                xa = l.getOriginPoint().x; ya = l.getOriginPoint().y;
                pa = translateWithPoint(rdx, rdy, xa, ya);
                l.setOriginPoint(pa.x, pa.y);
                xa = l.getLastPoint().x; ya = l.getLastPoint().y;
                pa = translateWithPoint(rdx, rdy, xa, ya);
                l.setLastPoint(pa.x, pa.y);
            }else if(s instanceof Bezier){
                Bezier b = (Bezier)s;
                xa = b.getOriginPoint().x; ya = b.getOriginPoint().y;
                pa = translateWithPoint(rdx, rdy, xa, ya);
                b.setOriginPoint(pa.x, pa.y);
                xa = b.getLastPoint().x; ya = b.getLastPoint().y;
                pa = translateWithPoint(rdx, rdy, xa, ya);
                b.setLastPoint(pa.x, pa.y);
            }else if(s instanceof ControlPoint){
                ControlPoint cp = (ControlPoint)s;
                xa = cp.getOriginPoint().x; ya = cp.getOriginPoint().y;
                pa = translateWithPoint(rdx, rdy, xa, ya);
                cp.setOriginPoint(pa.x, pa.y);
                xa = cp.getLastPoint().x; ya = cp.getLastPoint().y;
                pa = translateWithPoint(rdx, rdy, xa, ya);
                cp.setLastPoint(pa.x, pa.y);
            }else if(s instanceof BSpline){
                BSpline bs = (BSpline)s;
                xa = bs.getOriginPoint().x; ya = bs.getOriginPoint().y;
                pa = translateWithPoint(rdx, rdy, xa, ya);
                bs.setOriginPoint(pa.x, pa.y);
                xa = bs.getLastPoint().x; ya = bs.getLastPoint().y;
                pa = translateWithPoint(rdx, rdy, xa, ya);
                bs.setLastPoint(pa.x, pa.y);
                for(ControlPoint cp : bs.getControlPoints()){
                    xa = cp.getOriginPoint().x; ya = cp.getOriginPoint().y;
                    pa = translateWithPoint(rdx, rdy, xa, ya);
                    cp.setOriginPoint(pa.x, pa.y);
                    cp.setLastPoint(pa.x, pa.y);
                }
                if(bs.isNextExist()){
                    xa = bs.getNextPoint().x; ya = bs.getNextPoint().y;
                    pa = translateWithPoint(rdx, rdy, xa, ya);
                    bs.setNextPoint(pa.x, pa.y);
                }
                //TODO : Corriger le bug présent = un point s'intercale entre
                //le premier controlpoint et le second controlpoint faussant la
                //manoeuvre. La fonction suivante corrige temporairement le bug.
                bs.removePointAt(0);
            }
        }
    }
}
