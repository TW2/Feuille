/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.drawing.ornament;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import feuille.drawing.lib.BSpline;
import feuille.drawing.lib.Bezier;
import feuille.drawing.lib.ControlPoint;
import feuille.drawing.lib.IShape;
import feuille.drawing.lib.Layer;
import feuille.drawing.lib.Line;
import feuille.drawing.lib.Move;
import feuille.drawing.lib.ReStart;

/**
 *
 * @author The Wingate 2940
 */
public class OrnDo {
    
    AssLine al = new AssLine();
    OrnLayer ol = new OrnLayer();
    int resx = 1280;
    
    private double currentX = 0;    
    private List<Double> positionX = new ArrayList<Double>();
    private List<Double> positionY = new ArrayList<Double>();
    
    public OrnDo(){
        
    }
    
    // <editor-fold defaultstate="collapsed" desc="Processus">
    
    private double processXY(long mst, long duration, feuille.drawing.ornament.IShape s, double xpos1, double ypos1){
        double xpos2 = 0d;
        if(s.getDuration().contains("/")){
            int denom = Integer.parseInt(s.getDuration().substring(s.getDuration().lastIndexOf("/")+1));
            duration = duration / denom;
        }else if(s.getDuration().equals("0")==false){
            duration = Math.abs(Long.parseLong(s.getDuration()));
        }
        java.awt.Point S = s.getOriginPoint();
        java.awt.Point E = s.getLastPoint();
        //Longueur sur x et y
        double distanceX = E.getX() - S.getX();
        double distanceY = E.getY() - S.getY();
        //Produit en croix
        //distanceX = duration
        //x = mst
        //x = mst*distanceX / duration
        
        long entTime = 0;
        while(entTime < duration){
            if(s instanceof OrnMMLine){
                OrnMMLine l = (OrnMMLine)s;
                double x = entTime * distanceX / duration + xpos1; xpos2 = x;
                double y = l.getY(x) + ypos1;
                positionX.add(x); System.out.println("Pour "+entTime+", on a x="+x+" | "+(x-1000));
                positionY.add(y); System.out.println("Pour "+entTime+", on a y="+y+" | "+(y-1000));
            }else if(s instanceof OrnMMBezier){
                OrnMMBezier b = (OrnMMBezier)s;
                
            }
            entTime += mst;
        }
        return xpos2;
    }
    
    private void processXY(long mst, long duration, feuille.drawing.ornament.IShape s){
        if(s.getDuration().contains("/")){
            int denom = Integer.parseInt(s.getDuration().substring(s.getDuration().lastIndexOf("/")+1));
            duration = duration / denom;
        }else if(s.getDuration().equals("0")==false){
            duration = Math.abs(Long.parseLong(s.getDuration()));
        }
        
        java.awt.Point S = s.getOriginPoint();
        java.awt.Point E = s.getLastPoint();
        //Longueur sur x et y
        double distanceX = E.getX() - S.getX(); // double distanceY = E.getY() - S.getY();
        
        long entTime = 0;
        while(entTime < duration){
            if(s instanceof OrnMMLine){
                OrnMMLine l = (OrnMMLine)s;
                //Produit en croix
                //distanceX = duration
                //x = mst
                //x = mst*distanceX / duration
                double x = entTime * distanceX / duration;
                double y = l.getY(x);
                positionX.add(x); System.out.println("Pour "+entTime+", on a x="+x+" | "+(x-1000));
                positionY.add(y); System.out.println("Pour "+entTime+", on a y="+y+" | "+(y-1000));
            }else if(s instanceof OrnMMBezier){
                OrnMMBezier b = (OrnMMBezier)s;
                //On obtient le temps de la forme avec duration
                //On sait que pour la forme t=0 lorsque x et y sont à P0 et t=1 losraque x et y sopnt à P3
                //Il nous faut donc trouver x et y en fonction de t
                //Il nous faut d'abord déterminer t qui est fonction du temps de la forme
                // t=1 <> duration
                // t=? <> entTime
                //De ce produit en croix découle : t=? = entTime * t=1 / duration
                //Simplifions ? = entTime /duration
                double t = (double)entTime / (double)duration;
                double x = b.getX(t) - b.getX(0);
                double y = b.getY(t) - b.getY(0);
                positionX.add(x); System.out.println("Pour "+t+" ("+entTime+"), on a x="+x+" | "+(x-1000));
                positionY.add(y); System.out.println("Pour "+t+" ("+entTime+"), on a y="+y+" | "+(y-1000));
            }
            entTime += mst;
        }
    }
    
    // </editor-fold>
    
    public List<Double> getPosX(){
        return positionX;
    }
    
    public List<Double> getPosY(){
        return positionY;
    }
    
    // <editor-fold defaultstate="collapsed" desc="Pas de mouvement">
    
    public List<AssLine> getLinesForNoMove(OrnLayer olay, List<AssLine> asslines, Object[] olayers, int imgX, int imgY){
        //Un nouveau tableau dynamique pour enregistrer les données DANS l'ORDRE.
        List<AssLine> newlist = new ArrayList<AssLine>();
        //On traite d'abord le dessin qui doit aller derrière
        for(AssLine line : asslines){
            int countLayers = 1;
            for(Object o : olayers){
                if(o instanceof Layer){
                    Layer lay = (Layer)o;
                    String commands = getOrnamentCommands(lay,0,0,imgX,imgY);
                    String newhead = line.changeLayer(al.getHead(), countLayers+"");
                    String newbody = "{\\p1}"+commands+"{\\p0}";
                    newlist.add(new AssLine(newhead,newbody));
                    countLayers+=1;
                }
            }
        }
        //On traite ensuite le dessin qui doit aller dessus avec les \clip
        for(AssLine line : asslines){
            int countLayers = 101;
            for(Object o : olayers){
                if(o instanceof Layer){
                    Layer lay = (Layer)o;
                    String commands = getOrnamentCommands(lay,0,0,imgX,imgY);
                    String newhead = line.changeLayer(al.getHead(), countLayers+"");
                    String newbody = line.try_addToBody("{\\clip("+commands+")}");
                    newlist.add(new AssLine(newhead,newbody));
                    countLayers+=1;
                }
            }
        }
        return newlist;
    }
    
    public void getLinesForNoMove2(OrnLayer olay, List<AssLine> asslines, Object[] olayers, int imgX, int imgY, String path,
            int imgWidth, int imgHeight) throws FileNotFoundException, UnsupportedEncodingException, IOException{
        // On crée un fichier pour les valeurs de retour.
        FileOutputStream fos = new FileOutputStream(path);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
        PrintWriter pw = new PrintWriter(bw);
        
        // Entête
        pw.println("[Script Info]");
        pw.println("; This is an Advanced Sub Station Alpha v4+ script."
                + " For Sub Station Alpha info and downloads,"
                + " go to http://www.eswat.demon.co.uk/");
        pw.println("; Created by AssSketchpad."
                + " \"Fansub is for everyone and forever !\" -"
                + " Chien-Rouge@http://redaffaire.wordpress.com/");
        pw.println("ScriptType: v4.00+");
        pw.println("PlayResX: "+imgWidth);
        pw.println("PlayResY: "+imgHeight);
        pw.println("");
        
        pw.println("[V4+ Styles]");
        pw.println("Format: Name, Fontname, Fontsize, PrimaryColour, SecondaryColour, OutlineColour, BackColour, Bold, Italic" +
                ", Underline, StrikeOut, ScaleX, ScaleY, Spacing, Angle" +
                ", BorderStyle, Outline, Shadow, Alignment, MarginL, MarginR, MarginV, Encoding");
        
        for(Object o : olayers){
            if(o instanceof Layer){
                Layer lay = (Layer)o;                
                // Création du style pour le dessin
                AssStyle as_one = new AssStyle();
                as_one.setName(lay.getName());
                as_one.setAlignment(7);
                as_one.setMarginL(0);
                as_one.setMarginR(0);
                as_one.setMarginV(0);
                as_one.setTextColor(lay.getColor(), "00");
                pw.println(as_one.toAssStyleString());
                //Création du style pour le texte
                AssStyle as_two = new AssStyle();
                as_two.setName(lay.getName()+"Text");
                as_two.setTextColor(lay.getColor().brighter(), "00");
                pw.println(as_two.toAssStyleString());
            }
        }
        pw.println("");
        pw.println("[Events]");
        pw.println("Format: Layer, Start, End, Style, Name, MarginL, MarginR, MarginV, Effect, Text");
        
        //On traite d'abord le dessin qui doit aller derrière
        for(AssLine line : asslines){
            int countLayers = 1;
            for(Object o : olayers){
                if(o instanceof Layer){
                    Layer lay = (Layer)o;
                    String commands = getOrnamentCommands(lay,0,0,imgX,imgY);
                    String newhead = line.changeLayer(line.getHead(), countLayers+"");
                    newhead = line.changeStyle(newhead, lay.getName());
                    String newbody = "{\\p1}"+commands+"{\\p0}";
                    pw.println(new AssLine(newhead,newbody));
                    countLayers+=1;
                }
            }
        }
        //On traite ensuite le dessin qui doit aller dessus avec les \clip
        for(AssLine line : asslines){
            int countLayers = 101;
            for(Object o : olayers){
                if(o instanceof Layer){
                    Layer lay = (Layer)o;
                    String commands = getOrnamentCommands(lay,0,0,imgX,imgY);
                    String newhead = line.changeLayer(line.getHead(), countLayers+"");
                    newhead = line.changeStyle(newhead, lay.getName()+"Text");
                    String newbody = line.try_addToBody("{\\clip("+commands+")}");
                    pw.println(new AssLine(newhead,newbody));
                    countLayers+=1;
                }
            }
        }
        
        pw.close();
        bw.close();
        fos.close();
        
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Mouvement général">
    
    public List<AssLine> getLinesForMainMoveOnly(OrnLayer olay, List<AssLine> asslines,
            int frequency, Object[] olayers, int imgX, int imgY){
        // On crée un tableau dynamique pour les valeurs de retour.
        List<AssLine> newlines = new ArrayList<AssLine>();
        
        // On utilise deux variables pour stocker la position en cours
        currentX = 0; double resetX;
        
        // Pour chaque forme de la liste des formes (sauf points 
        // et points de contrôle), on éxecute la suite des événements.
        for(feuille.drawing.ornament.IShape s : olay.getList()){
            if(s instanceof OrnMMLine | s instanceof OrnMMBezier){
                
                // A chaque fois qu'on change de forme, on s'assure
                // d'être à la suite de la dernière.
                resetX = currentX;
                
                // Pour chaque lignes de karaoké, on éxecute la suite
                // des commandes (DESSIN DE DESSOUS).
                for(AssLine als : asslines){
                    
                    // On compte les couches pour pouvoir incrémenter sur
                    // les commandes de dessin avec cette variable.
                    int countLayers = 1;
                    
                    // On recherche les positions de x et y en fonction 
                    // du temps de la phrase ou de la forme.
                    currentX = processXY(frequency, Long.parseLong(als.getMillisecondsDur()), s, resetX, 0);
                    List<Double> Xvalues = getPosX();
                    List<Double> Yvalues = getPosY();
                    
                    // Pour chaque couche de dessin on éxecute la suite.
                    for(Object o : olayers){
                        if(o instanceof Layer){
                            Layer lay = (Layer)o;
                            
                            // On obtient les commandes associées.
                            String commands = getOrnamentCommands(lay,0,0,imgX,imgY);
                            
                            // On change le numéro de couche pour cette ligne.
                            String newhead = als.changeLayer(als.getHead(), countLayers+"");
                            
                            // Pour chaque valeur x et y on éxecute la suite.
                            for(int i=0; i<Xvalues.size(); i++){
                                
                                // On transforme ces valeurs Double en String
                                // en les prenant de leur liste.
                                String sx = Xvalues.toArray()[i].toString(); 
                                String sy = Yvalues.toArray()[i].toString();
                                
                                // On crée une variable pour stocker un
                                // bout de commande.
                                String temp;
                                
                                // On crée le code de la commande.
                                if(i==0){
                                    temp = "\\t("+(i+1)*frequency+","+(i+1)*frequency+","+"\\alpha&HFF&)";
                                }else{
                                    temp = "\\alpha&HFF&"+"\\t("+i*frequency+","+i*frequency+","+"\\alpha&H00&)"+
                                            "\\t("+(i+1)*frequency+","+(i+1)*frequency+","+"\\alpha&HFF&)";
                                }
                                
                                // On crée concrètement la nouvelle
                                // ligne de code.
                                String newbody = "{\\pos("+sx+","+sy+")"+temp+"\\p1}"+commands+"{\\p0}";
                                
                                // On ajoute cette ligne de code au
                                // tableau dynamique.
                                newlines.add(new AssLine(newhead,newbody));
                                
                            }
                            
                            // On incrémente le numéro de couche de la ligne.
                            countLayers+=1;
                            
                        }
                    }
                    
                    //On réinitialise les variables.
                    positionX.clear();
                    positionY.clear();
                    
                }
                
                
                // Pour chaque lignes de karaoké, on éxecute la suite
                // des commandes (ZONE DE VISIBILITE).
                for(AssLine als : asslines){
                    
                    // On compte les couches pour pouvoir incrémenter sur
                    // les commandes de dessin avec cette variable.
                    int countLayers = 101;
                    
                    // On recherche les positions de x et y en fonction 
                    // du temps de la phrase ou de la forme.
                    currentX = processXY(frequency, Long.parseLong(als.getMillisecondsDur()), s, resetX, 0);
                    List<Double> Xvalues = getPosX();
                    List<Double> Yvalues = getPosY();
                    
                    // Pour chaque couche de dessin on éxecute la suite.
                    for(Object o : olayers){
                        if(o instanceof Layer){
                            Layer lay = (Layer)o;
                            
                            // On prépare la variable pour les commandes associées.
                            String commands;
                            
                            // On change le numéro de couche pour cette ligne.
                            String newhead = als.changeLayer(als.getHead(), countLayers+"");
                            
                            // Pour chaque valeur x et y on éxecute la suite.
                            for(int i=0; i<Xvalues.size(); i++){
                                
                                // On transforme ces valeurs Double en String
                                // en les prenant de leur liste.
                                String sx = Xvalues.toArray()[i].toString(); 
                                String sy = Yvalues.toArray()[i].toString();
                                
                                // On s'assure que les commandes soient
                                // personnalisés à la ligne et donne une
                                // nouvelle visibilité.
                                int x = (int)Double.parseDouble(sx);
                                int y = (int)Double.parseDouble(sy);
                                commands = getOrnamentCommands(lay,-x,-y,imgX,imgY);
                                
                                // On crée une variable pour stocker un
                                // bout de commande.
                                String temp;
                                
                                // On crée le code de la commande.
                                if(i==0){
                                    temp = "\\t("+(i+1)*frequency+","+(i+1)*frequency+","+"\\alpha&HFF&)";
                                }else{
                                    temp = "\\alpha&HFF&"+"\\t("+i*frequency+","+i*frequency+","+"\\alpha&H00&)"+
                                            "\\t("+(i+1)*frequency+","+(i+1)*frequency+","+"\\alpha&HFF&)";
                                }
                                
                                // On crée concrètement la nouvelle
                                // ligne de code.
                                String newbody = als.try_addToBody("{"+temp+"\\clip("+commands+")}");
                                
                                // On ajoute cette ligne de code au
                                // tableau dynamique.
                                newlines.add(new AssLine(newhead,newbody));
                            }
                            
                            // On incrémente le numéro de couche de la ligne.
                            countLayers+=1;
                            
                        }
                    }
                    
                    //On réinitialise les variables.
                    positionX.clear();
                    positionY.clear();
                    
                }
                
                
            }
        }
        
        return newlines;
    }
    
    public List<AssLine> getLinesForMainMoveOnly2(OrnLayer olay, List<AssLine> asslines,
            int frequency, Object[] olayers, int imgX, int imgY){
        // On crée un tableau dynamique pour les valeurs de retour.
        List<AssLine> newlines = new ArrayList<AssLine>();
        
        // On va boucler sur toutes les lignes
        for(AssLine line : asslines){
            // ON sait que :
            // Au début d'une ligne, le temps est égal à 0
            // Au début d'une ligne, x=0
            long linetime = 0;
            double xpos = 0;
            double ypos = 0;
            
            int countLayers = 1, clipLayers = 100; 
            
            // On va boucler sur toutes les copuches de dessins
            for(Object o : olayers){
                if(o instanceof Layer){
                    Layer lay = (Layer)o;
                    
                    //On va boucler sur toutes les formes formant le chemin.
                    for(feuille.drawing.ornament.IShape s : olay.getList()){
                        if(s instanceof OrnMMLine | s instanceof OrnMMBezier){
                            
                            // On efface les tableaux pour les remplir avec de nouvelles valeurs
                            positionX.clear();
                            positionY.clear();
                            processXY(frequency, Long.parseLong(line.getMillisecondsDur()), s);
                            List<Double> Xvalues = getPosX();
                            List<Double> Yvalues = getPosY();
                            
                            // Pour chaque lignes de karaoké, on éxecute la suite
                            // des commandes (DESSIN DE DESSOUS).
                            //**************************************************
                            // On obtient les commandes associées.
                            String commands = getOrnamentCommands(lay,0,0,imgX,imgY);
                            
                            // On change le numéro de couche pour cette ligne.
                            String newhead = line.changeLayer(line.getHead(), countLayers+"");
                            
                            // Pour chaque valeur x et y on éxecute la suite.
                            for(int i=0; i<Xvalues.size(); i++){
                                
                                // On transforme ces valeurs Double en String
                                // en les prenant de leur liste.
                                double dx = Xvalues.get(i);// String sx = Double.toString(dx);
                                double dy = Yvalues.get(i);// String sy = Double.toString(dy);
                                
                                // On crée une variable pour stocker un
                                // bout de commande.
                                String temp;
                                
                                // On crée le code de la commande.
                                if(i==0 && linetime==0){
                                    temp = "\\t("+(i+1)*frequency+","+(i+1)*frequency+","+"\\alpha&HFF&)";
                                }else{
                                    temp = "\\alpha&HFF&"+"\\t("+(i*frequency+linetime)+","+(i*frequency+linetime)+","+"\\alpha&H00&)"+
                                            "\\t("+((i+1)*frequency+linetime)+","+((i+1)*frequency+linetime)+","+"\\alpha&HFF&)";
                                }
                                
                                // On crée concrètement la nouvelle
                                // ligne de code.
                                String newbody = "{\\pos("+(dx+xpos)+","+(dy+ypos)+")"+temp+"\\p1}"+commands+"{\\p0}";
                                
                                // On ajoute cette ligne de code au
                                // tableau dynamique.
                                newlines.add(new AssLine(newhead,newbody));
                                
                                
                            }
                            
                            if(s.getDuration().contains("/")){
                                int denom = Integer.parseInt(s.getDuration().substring(s.getDuration().lastIndexOf("/")+1));
                                linetime += Long.parseLong(line.getMillisecondsDur()) / denom;
                            }else if(s.getDuration().equals("0")==false){
                                linetime += Math.abs(Long.parseLong(s.getDuration()));
                            }
                            xpos = xpos + Xvalues.get(Xvalues.size()-1);
                            ypos = ypos + Yvalues.get(Yvalues.size()-1);
                            countLayers += 1;
                            
                        }                        
                    }
                    
                    //Reinitilisation
                    linetime = 0;
                    xpos = 0;
                    ypos = 0;
                    
                    
                    //On va boucler sur toutes les formes formant le chemin.
                    for(feuille.drawing.ornament.IShape s : olay.getList()){
                        if(s instanceof OrnMMLine | s instanceof OrnMMBezier){
                            
                            // On efface les tableaux pour les remplir avec de nouvelles valeurs
                            positionX.clear();
                            positionY.clear();
                            processXY(frequency, Long.parseLong(line.getMillisecondsDur()), s);
                            List<Double> Xvalues = getPosX();
                            List<Double> Yvalues = getPosY();
                            
                            // Pour chaque lignes de karaoké, on éxecute la suite
                            // des commandes (ZONE DE VISIBILITE).
                            //**************************************************
                            // On prépare la variable pour les commandes associées.
                            // variable commands
                            
                            // On change le numéro de couche pour cette ligne.
                            String newhead = line.changeLayer(line.getHead(), (countLayers+clipLayers)+"");
                            
                            // Pour chaque valeur x et y on éxecute la suite.
                            for(int i=0; i<Xvalues.size(); i++){
                                
                                // On transforme ces valeurs Double en String
                                // en les prenant de leur liste.
                                double dx = Xvalues.get(i);// String sx = Double.toString(dx);
                                double dy = Yvalues.get(i);// String sy = Double.toString(dy);
                                
                                // On s'assure que les commandes soient
                                // personnalisés à la ligne et donne une
                                // nouvelle visibilité.
                                int x = (int)dx - (int)xpos;
                                int y = (int)dy - (int)ypos;
                                String commands = getOrnamentCommands(lay,-x,-y,imgX,imgY);
                                
                                // On crée une variable pour stocker un
                                // bout de commande.
                                String temp;
                                
                                // On crée le code de la commande.
                                if(i==0 && linetime==0){
                                    temp = "\\t("+(i+1)*frequency+","+(i+1)*frequency+","+"\\alpha&HFF&)";
                                }else{
                                    temp = "\\alpha&HFF&"+"\\t("+(i*frequency+linetime)+","+(i*frequency+linetime)+","+"\\alpha&H00&)"+
                                            "\\t("+((i+1)*frequency+linetime)+","+((i+1)*frequency+linetime)+","+"\\alpha&HFF&)";
                                }
                                
                                // On crée concrètement la nouvelle
                                // ligne de code.
                                String newbody = line.try_addToBody("{"+temp+"\\clip("+commands+")}");
                                
                                // On ajoute cette ligne de code au
                                // tableau dynamique.
                                newlines.add(new AssLine(newhead,newbody));
                            }
                            
                            if(s.getDuration().contains("/")){
                                int denom = Integer.parseInt(s.getDuration().substring(s.getDuration().lastIndexOf("/")+1));
                                linetime += Long.parseLong(line.getMillisecondsDur()) / denom;
                            }else if(s.getDuration().equals("0")==false){
                                linetime += Math.abs(Long.parseLong(s.getDuration()));
                            }
                            xpos = xpos + Xvalues.get(0)-Xvalues.get(Xvalues.size()-1);
                            ypos = ypos + Yvalues.get(0)-Yvalues.get(Yvalues.size()-1);
                            countLayers += 1;
                            
                        }
                    }
                    
                    // On réinitialise la variable
                    //Reinitilisation
                    linetime = 0;
                    xpos = 0;
                    ypos = 0;
                    
                }
            }
        }
        
        return newlines;
    }
    
    public void getLinesForMainMoveOnly3(OrnLayer olay, List<AssLine> asslines,
            int frequency, Object[] olayers, int imgX, int imgY, String path,
            int imgWidth, int imgHeight) throws FileNotFoundException, UnsupportedEncodingException, IOException{
        // On crée un fichier pour les valeurs de retour.
        FileOutputStream fos = new FileOutputStream(path);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
        PrintWriter pw = new PrintWriter(bw);
        
        // Entête
        pw.println("[Script Info]");
        pw.println("; This is an Advanced Sub Station Alpha v4+ script."
                + " For Sub Station Alpha info and downloads,"
                + " go to http://www.eswat.demon.co.uk/");
        pw.println("; Created by AssSketchpad."
                + " \"Fansub is for everyone and forever !\" -"
                + " Chien-Rouge@http://redaffaire.wordpress.com/");
        pw.println("ScriptType: v4.00+");
        pw.println("PlayResX: "+imgWidth);
        pw.println("PlayResY: "+imgHeight);
        pw.println("");
        
        pw.println("[V4+ Styles]");
        pw.println("Format: Name, Fontname, Fontsize, PrimaryColour, SecondaryColour, OutlineColour, BackColour, Bold, Italic" +
                ", Underline, StrikeOut, ScaleX, ScaleY, Spacing, Angle" +
                ", BorderStyle, Outline, Shadow, Alignment, MarginL, MarginR, MarginV, Encoding");
        
        for(Object o : olayers){
            if(o instanceof Layer){
                Layer lay = (Layer)o;                
                // Création du style pour le dessin
                AssStyle as_one = new AssStyle();
                as_one.setName(lay.getName());
                as_one.setAlignment(7);
                as_one.setMarginL(0);
                as_one.setMarginR(0);
                as_one.setMarginV(0);
                as_one.setTextColor(lay.getColor(), "00");
                pw.println(as_one.toAssStyleString());
                //Création du style pour le texte
                AssStyle as_two = new AssStyle();
                as_two.setName(lay.getName()+"Text");
                as_two.setTextColor(lay.getColor().brighter(), "00");
                pw.println(as_two.toAssStyleString());
            }
        }
        pw.println("");
        pw.println("[Events]");
        pw.println("Format: Layer, Start, End, Style, Name, MarginL, MarginR, MarginV, Effect, Text");
        
        // On va boucler sur toutes les lignes
        for(AssLine line : asslines){
            // ON sait que :
            // Au début d'une ligne, le temps est égal à 0
            // Au début d'une ligne, x=0
            long linetime = 0;
            double xpos = 0;
            double ypos = 0;
            
            int countLayers = 1, clipLayers = 100; 
            
            // On va boucler sur toutes les couches de dessins
            for(Object o : olayers){
                if(o instanceof Layer){
                    Layer lay = (Layer)o;
                    
                    //On va boucler sur toutes les formes formant le chemin.
                    for(feuille.drawing.ornament.IShape s : olay.getList()){
                        if(s instanceof OrnMMLine | s instanceof OrnMMBezier){
                            
                            // On efface les tableaux pour les remplir avec de nouvelles valeurs
                            positionX.clear();
                            positionY.clear();
                            processXY(frequency, Long.parseLong(line.getMillisecondsDur()), s);
                            List<Double> Xvalues = getPosX();
                            List<Double> Yvalues = getPosY();
                            
                            // Pour chaque lignes de karaoké, on éxecute la suite
                            // des commandes (DESSIN DE DESSOUS).
                            //**************************************************
                            // On obtient les commandes associées.
                            String commands = getOrnamentCommands(lay,0,0,imgX,imgY);
                            
                            // On change le numéro de couche pour cette ligne.
                            String newhead = line.changeLayer(line.getHead(), countLayers+"");
                            newhead = line.changeStyle(newhead, lay.getName());
                            
                            // Pour chaque valeur x et y on éxecute la suite.
                            for(int i=0; i<Xvalues.size(); i++){
                                
                                // On transforme ces valeurs Double en String
                                // en les prenant de leur liste.
                                double dx = Xvalues.get(i);// String sx = Double.toString(dx);
                                double dy = Yvalues.get(i);// String sy = Double.toString(dy);
                                
                                // On crée une variable pour stocker un
                                // bout de commande.
                                String temp;
                                
                                // On crée le code de la commande.
                                if(i==0 && linetime==0){
                                    temp = "\\t("+(i+1)*frequency+","+(i+1)*frequency+","+"\\alpha&HFF&)";
                                }else{
                                    temp = "\\alpha&HFF&"+"\\t("+(i*frequency+linetime)+","+(i*frequency+linetime)+","+"\\alpha&H00&)"+
                                            "\\t("+((i+1)*frequency+linetime)+","+((i+1)*frequency+linetime)+","+"\\alpha&HFF&)";
                                }
                                
                                // On crée concrètement la nouvelle
                                // ligne de code.
                                String newbody = "{\\pos("+(dx+xpos)+","+(dy+ypos)+")"+temp+"\\p1}"+commands+"{\\p0}";
                                
                                // On ajoute cette ligne de code au
                                // tableau dynamique.
                                pw.println(new AssLine(newhead,newbody));
                                
                                
                            }
                            
                            if(s.getDuration().contains("/")){
                                int denom = Integer.parseInt(s.getDuration().substring(s.getDuration().lastIndexOf("/")+1));
                                linetime += Long.parseLong(line.getMillisecondsDur()) / denom;
                            }else if(s.getDuration().equals("0")==false){
                                linetime += Math.abs(Long.parseLong(s.getDuration()));
                            }
                            xpos = xpos + Xvalues.get(Xvalues.size()-1);
                            ypos = ypos + Yvalues.get(Yvalues.size()-1);
                            countLayers += 1;
                            
                        }                        
                    }
                    
                    //Reinitilisation
                    linetime = 0;
                    xpos = 0;
                    ypos = 0;
                    
                    
                    //On va boucler sur toutes les formes formant le chemin.
                    for(feuille.drawing.ornament.IShape s : olay.getList()){
                        if(s instanceof OrnMMLine | s instanceof OrnMMBezier){
                            
                            // On efface les tableaux pour les remplir avec de nouvelles valeurs
                            positionX.clear();
                            positionY.clear();
                            processXY(frequency, Long.parseLong(line.getMillisecondsDur()), s);
                            List<Double> Xvalues = getPosX();
                            List<Double> Yvalues = getPosY();
                            
                            // Pour chaque lignes de karaoké, on éxecute la suite
                            // des commandes (ZONE DE VISIBILITE).
                            //**************************************************
                            // On prépare la variable pour les commandes associées.
                            // variable commands
                            
                            // On change le numéro de couche pour cette ligne.
                            String newhead = line.changeLayer(line.getHead(), (countLayers+clipLayers)+"");
                            newhead = line.changeStyle(newhead, lay.getName()+"Text");
                            
                            // Pour chaque valeur x et y on éxecute la suite.
                            for(int i=0; i<Xvalues.size(); i++){
                                
                                // On transforme ces valeurs Double en String
                                // en les prenant de leur liste.
                                double dx = Xvalues.get(i);// String sx = Double.toString(dx);
                                double dy = Yvalues.get(i);// String sy = Double.toString(dy);
                                
                                // On s'assure que les commandes soient
                                // personnalisés à la ligne et donne une
                                // nouvelle visibilité.
                                int x = (int)dx - (int)xpos;
                                int y = (int)dy - (int)ypos;
                                String commands = getOrnamentCommands(lay,-x,-y,imgX,imgY);
                                
                                // On crée une variable pour stocker un
                                // bout de commande.
                                String temp;
                                
                                // On crée le code de la commande.
                                if(i==0 && linetime==0){
                                    temp = "\\t("+(i+1)*frequency+","+(i+1)*frequency+","+"\\alpha&HFF&)";
                                }else{
                                    temp = "\\alpha&HFF&"+"\\t("+(i*frequency+linetime)+","+(i*frequency+linetime)+","+"\\alpha&H00&)"+
                                            "\\t("+((i+1)*frequency+linetime)+","+((i+1)*frequency+linetime)+","+"\\alpha&HFF&)";
                                }
                                
                                // On crée concrètement la nouvelle
                                // ligne de code.
                                String newbody = line.try_addToBody("{"+temp+"\\clip("+commands+")}");
                                
                                // On ajoute cette ligne de code au
                                // tableau dynamique.
                                pw.println(new AssLine(newhead,newbody));
                            }
                            
                            if(s.getDuration().contains("/")){
                                int denom = Integer.parseInt(s.getDuration().substring(s.getDuration().lastIndexOf("/")+1));
                                linetime += Long.parseLong(line.getMillisecondsDur()) / denom;
                            }else if(s.getDuration().equals("0")==false){
                                linetime += Math.abs(Long.parseLong(s.getDuration()));
                            }
                            xpos = xpos + Xvalues.get(0)-Xvalues.get(Xvalues.size()-1);
                            ypos = ypos + Yvalues.get(0)-Yvalues.get(Yvalues.size()-1);
                            countLayers += 1;
                            
                        }
                    }
                    
                    // On réinitialise la variable
                    //Reinitilisation
                    linetime = 0;
                    xpos = 0;
                    ypos = 0;
                    
                }
            }
        }
        
        pw.close();
        bw.close();
        fos.close();
    }
    
    // </editor-fold>
    
    
    /** Obtient les commandes pour l'ornement à partir de la couche sélectionnée. 
     * @param lay La couche sélectionnée.
     * @param xt Le décalage sur x.
     * @param yt Le décalage sur y.
     * @return La commande de l'ornement.
     */
    public String getOrnamentCommands(Layer lay, int xt, int yt, int imgX, int imgY){
        //On obtient la position de l'image (valeur à toujours soustraire)
        int posX = imgX + xt;
        int posY = imgY + yt;
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
    
}
