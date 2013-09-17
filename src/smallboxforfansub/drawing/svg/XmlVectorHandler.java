/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smallboxforfansub.drawing.svg;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import smallboxforfansub.drawing.adf.LayerContent;

/**
 *
 * @author The Wingate 2940
 */
public class XmlVectorHandler {
    /*   Structure à lire :
     * <?xml version="1.0" standalone="no"?>
     * <!DOCTYPE svg PUBLIC "-//W3C//DTD SVG 1.1//EN" "http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd">
     * <svg width="centimètres" height="centimètres" viewBox="0 0 400 400" xmlns="http://www.w3.org/2000/svg" version="1.1">
     *      <title>TITRE</title>
     *      <desc>DESCRIPTION</desc>
     *      <rect x="1" y="1" width="398" height="398" fill="none" stroke="blue" />
     *      <path d="M 100 100 L 300 100 L 200 300 z" fill="red" stroke="blue" stroke-width="3" />
     * </svg>
     */
    
    VectorHandler vh;
    
    public XmlVectorHandler(String path) throws ParserConfigurationException, SAXException, IOException{
        SAXParserFactory fabrique = SAXParserFactory.newInstance();
        SAXParser parseur = fabrique.newSAXParser();

        File fichier = new File(path);        
        
        //Crée un fichier de lecture temporaire (sans entête) :
        FileInputStream fis = new FileInputStream(fichier);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
        String newline;
        
        File file = new File(fichier.getParentFile(),"temp.svg");
        FileOutputStream fos = new FileOutputStream(file);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
        PrintWriter pw = new PrintWriter(bw);
        
        while((newline=br.readLine())!=null){
            if(newline.contains("?xml")==false && newline.contains("!DOCTYPE")==false && newline.contains(".dtd")==false){
                pw.println(newline); pw.flush();
            }
        }
        pw.close(); bw.close(); fos.close();
        br.close(); fis.close();
        //---------------------------------------------------
        
        vh = new VectorHandler();
        parseur.parse(file, vh);
        
        file.delete();
    }
    
    public VectorObject getVectorObject(){
        return vh.getVectorObject();
    }
    
    public class VectorHandler extends DefaultHandler{
        
        //résultats de notre parsing
	private VectorObject vro;
        private LayerContent lc;
        private String currentPath, currentColor;
	//flags nous indiquant la position du parseur
	private boolean inDrawing, inLayer, inTranslate, inRect;
	//buffer nous permettant de récupérer les données 
	private StringBuffer buffer;
        
        public VectorHandler(){
            super();
        }
        
        public VectorObject getVectorObject(){
            return vro;
        }
        
        //détection d'ouverture de balise
        @Override
        public void startElement(String uri, String localName,
			String qName, Attributes attributes) throws SAXException{
            if(qName.equals("svg")){
                vro = new VectorObject();
                inDrawing = true;
            }else{
                buffer = new StringBuffer();
                if(qName.equals("path")){
                    lc = new LayerContent();
                    currentPath = attributes.getValue("d"); //d="PATH"
                    currentColor = attributes.getValue("fill"); //fill="COLOR"
                    inLayer = true;
                }{
                    //erreur, on peut lever une exception
//                        throw new SAXException("Balise "+qName+" inconnue.");
                }
            }
        }
        
        //détection fin de balise
        @Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException{
            if(qName.equals("svg")){
                inDrawing = false;
            }else{
                if(qName.equals("path")){
                    lc.setName("");
                    lc.fromHTMLColor("0000FF");//currentColor
                    lc.setAssCommands(SVGtoASS(currentPath));
                    vro.addLayer(lc);
                    buffer = null;
                    inLayer = false;
                }else{
                    //erreur, on peut lever une exception
//                        throw new SAXException("Balise "+qName+" inconnue.");
                }
            }
        }
        
        //détection de caractères
        @Override
	public void characters(char[] ch,int start, int length)
			throws SAXException{
            String lecture = new String(ch,start,length);
            if(buffer != null) {
                buffer.append(lecture);
            }       
	}
        
	//début du parsing
        @Override
	public void startDocument() throws SAXException {
//            System.out.println("Début du parsing");
	}
        
	//fin du parsing
        @Override
	public void endDocument() throws SAXException {
//            System.out.println("Fin du parsing");
//            System.out.println("Resultats du parsing");
//            for(ParticleObject p : lpo){
//                    System.out.println(p);
//            }
	}
        
        //======================================================================
        //   Retraitement des données
        //======================================================================
        
        private String SVGtoASS(String SVGpath){
            String assCommands = SVGpath;
                        
//            String assCommands = SVGpath.toLowerCase();            
//            if(assCommands.contains("h")){
//                assCommands = assCommands.replaceAll("h", "l");
//            }            
//            if(assCommands.contains("v")){
//                assCommands = assCommands.replaceAll("v", "l");
//            }
//            if(assCommands.contains("c")){
//                assCommands = assCommands.replaceAll("c", "b");
//            }
//            if(assCommands.contains("z")){
//                assCommands = assCommands.replaceAll("z", "c");
//            }
            
            //Remplace deux espaces par un espace
            while(assCommands.contains("  ")){
                assCommands = assCommands.replaceAll("  ", " ");
            }
            
            //Remplace les chiffres à virgule par des entiers
            while(assCommands.contains(".")){
                String start = assCommands.substring(0, assCommands.indexOf("."));
                int next1, next2; String end = "";
                try{
                    next1 = assCommands.indexOf(" ", assCommands.indexOf("."));
                    next2 = assCommands.indexOf(",", assCommands.indexOf("."));
                    int next = next1<next2 ? next1 : next2;
                    if(next1 == -1){ next = next2; }
                    if(next2 == -1){ next = next1; }
                    end = assCommands.substring(next);
                }catch (IndexOutOfBoundsException ex){
                    
                }
                assCommands = start + end;
            }
            
            //Remplace les virgules des coordonnées par des espaces
            if(assCommands.contains(",")){
                assCommands = assCommands.replaceAll(",", " ");
            }
            
            List<String> SVGTags = getCleanSVGTags(assCommands);
            String verylast = "", first, second, last, close, newCommands = "";
            for(String tag : SVGTags){
                
                //Parser pour le MoveTo en absolu
                if(tag.contains("M")){
                    Pattern p = Pattern.compile("M?\\s?(-*\\d+\\s+-*\\d+)");
                    Matcher m = p.matcher(tag);
                    if(m.find()){                        
                        newCommands = newCommands.isEmpty() ? "m " + m.group(1) : newCommands + " m " + m.group(1);
                        verylast = m.group(1); //Point
                    }
                }
                
                //Parser pour le MoveTo en relatif
                if(tag.contains("m")){
                    Pattern p = Pattern.compile("m?\\s?(-*\\d+\\s+-*\\d+)");
                    Matcher m = p.matcher(tag);
                    if(m.find()){
                        int x = getX(verylast), y = getY(verylast), xa = getX(m.group(1)), ya = getY(m.group(1));
                        newCommands = newCommands + " m " + (x+xa) + " " + (y+ya);
                        verylast = (x+xa) + " " + (y+ya); //Point
                    }
                }
                
                //Parser pour le LineTo en absolu
                if(tag.contains("L")){
                    Pattern p = Pattern.compile("L?\\s?(-*\\d+\\s+-*\\d+)");
                    Matcher m = p.matcher(tag);
                    if(m.find()){                        
                        newCommands = newCommands + " l " + m.group(1);
                        verylast = m.group(1); //Point
                    }
                }
                
                //Parser pour le LineTo en relatif
                if(tag.contains("l")){
                    Pattern p = Pattern.compile("l?\\s?(-*\\d+\\s+-*\\d+)");
                    Matcher m = p.matcher(tag);
                    if(m.find()){
                        int x = getX(verylast), y = getY(verylast), xa = getX(m.group(1)), ya = getY(m.group(1));
                        newCommands = newCommands + " l " + (x+xa) + " " + (y+ya);
                        verylast = (x+xa) + " " + (y+ya); //Point
                    }
                }
                
                //Parser pour le Horizontal LineTo en absolu
                if(tag.contains("H")){
                    Pattern p = Pattern.compile("H?\\s?(-*\\d+)");
                    Matcher m = p.matcher(tag);
                    if(m.find()){
                        int y = getY(verylast), xa = Integer.parseInt(m.group(1)), ya = y;
                        newCommands = newCommands + " l " + xa + " " + ya;
                        verylast = xa + " " + ya; //Point
                    }
                }
                
                //Parser pour le Horizontal LineTo en relatif
                if(tag.contains("h")){
                    Pattern p = Pattern.compile("h?\\s?(-*\\d+)");
                    Matcher m = p.matcher(tag);
                    if(m.find()){
                        int x = getX(verylast), y = getY(verylast), xa = Integer.parseInt(m.group(1)), ya = y;
                        newCommands = newCommands + " l " + (x+xa) + " " + ya;
                        verylast = (x+xa) + " " + ya; //Point
                    }
                }
                
                //Parser pour le Vertical LineTo en absolu
                if(tag.contains("V")){
                    Pattern p = Pattern.compile("V?\\s?(-*\\d+)");
                    Matcher m = p.matcher(tag);
                    if(m.find()){
                        int x = getX(verylast), xa = x, ya = Integer.parseInt(m.group(1));
                        newCommands = newCommands + " l " + xa + " " + ya;
                        verylast = xa + " " + ya; //Point
                    }
                }
                
                //Parser pour le Vertical LineTo en relatif
                if(tag.contains("v")){
                    Pattern p = Pattern.compile("v?\\s?(-*\\d+)");
                    Matcher m = p.matcher(tag);
                    if(m.find()){
                        int x = getX(verylast), y = getY(verylast), xa = x, ya = Integer.parseInt(m.group(1));
                        newCommands = newCommands + " l " + xa + " " + (y+ya);
                        verylast = xa + " " + (y+ya); //Point
                    }
                }
                
                //Parser pour le CurveTo en absolu
                if(tag.contains("C")){
                    Pattern p = Pattern.compile("C?\\s?(-*\\d+\\s+-*\\d+)\\s+(-*\\d+\\s+-*\\d+)\\s+(-*\\d+\\s+-*\\d+)\\s*(z*Z*)");
                    Matcher m = p.matcher(tag);
                    int count = 0;
                    while(m.find()){                        
                        first = m.group(1); //Control point 1 si b ou point si l
                        second = m.group(2); //Control point 2 si b ou rien si l
                        last = m.group(3); //Point si b ou rien si l
                        close = m.group(4); //Close si b ou rien si l

                        if(verylast.equalsIgnoreCase(first) && second.equalsIgnoreCase(last)){
                            newCommands = newCommands + " l " + last;
                        }else{
                            newCommands = newCommands + " b " + first + " " + second + " " + last;
                            if(close.isEmpty()==false){
                                newCommands = newCommands + " c";
                            }
                        }
                        verylast = last;
                        
                        count += 1;
                    }
                    if(count>1 && newCommands.contains("b")){//Transformation en BSpline
                        for(int i=0; i<count; i++){
                            int index = newCommands.lastIndexOf("b");
                            if (i==count-1){
                                newCommands = newCommands.substring(0, index) + "s" + newCommands.substring(index+1);
                            }else{
                                newCommands = newCommands.substring(0, index) + "" + newCommands.substring(index+1);
                            }
                            
                        }                        
                    }
                }
                
                //Parser pour le CurveTo en relatif
                if(tag.contains("c")){
                    Pattern p = Pattern.compile("c?\\s?(-*\\d+\\s+-*\\d+)\\s+(-*\\d+\\s+-*\\d+)\\s+(-*\\d+\\s+-*\\d+)\\s*(z*Z*)");
                    Matcher m = p.matcher(tag);
                    int count = 0;
                    while(m.find()){
                        int x = getX(verylast), y = getY(verylast);
                        int xa = getX(m.group(1))+x, ya = getY(m.group(1))+y; //Control point 1
                        int xb = getX(m.group(2))+xa, yb = getY(m.group(2))+ya; //Control point 2
                        int xc = getX(m.group(3))+xb, yc = getY(m.group(3))+yb; //Last
                        close = m.group(4); //Close si b ou rien si l

                        if(verylast.equalsIgnoreCase(xa + " " + ya) && (xb + " " + yb).equalsIgnoreCase((xc + " " + yc))){
                            newCommands = newCommands + " l " + xc + " " + yc;
                        }else{
                            newCommands = newCommands + " b " + xa + " " + ya + " " + xb + " " + yb + " " + xc + " " + yc;
                            if(close.isEmpty()==false){
                                newCommands = newCommands + " c";
                            }
                        }
                        verylast = xc + " " + yc;
                        
                        count += 1;
                    }
                    if(count>1 && newCommands.contains("b")){//Transformation en BSpline
                        for(int i=0; i<count; i++){
                            int index = newCommands.lastIndexOf("b");
                            if (i==count-1){
                                newCommands = newCommands.substring(0, index) + "s" + newCommands.substring(index+1);
                            }else{
                                newCommands = newCommands.substring(0, index) + "" + newCommands.substring(index+1);
                            }
                            
                        }                        
                    }
                }
                
            }
            assCommands = newCommands;
            
            //Vérifie et remplace les longues chaines de commandes par des courtes
//            List<String> tags = getCleanTags(assCommands);
//            String verylast = "", first, second, last, close, newCommands = "";
//            for(String tag : tags){
//                if(tag.contains("m")){
//                    Pattern p = Pattern.compile("m?\\s?(-*\\d+\\s+-*\\d+)");
//                    Matcher m = p.matcher(tag);
//                    if(m.find()){
//                        verylast = m.group(1); //Point
//                        newCommands = newCommands.isEmpty() ? m.group(0) : newCommands + " " + m.group(0);
//                    }
//                }
//                if(tag.contains("l")){
//                    Pattern p = Pattern.compile("l?\\s?(-*\\d+\\s+-*\\d+)");
//                    Matcher m = p.matcher(tag);
//                    if(m.find()){
//                        verylast = m.group(1); //Point
//                        newCommands = newCommands + " " + m.group(0);
//                    }
//                }
//                if(tag.contains("b")){
//                    Pattern p = Pattern.compile("b?\\s?(-*\\d+\\s+-*\\d+)\\s+(-*\\d+\\s+-*\\d+)\\s+(-*\\d+\\s+-*\\d+)\\s?(c?)");
//                    Matcher m = p.matcher(tag);
//                    while(m.find()){
//                        first = m.group(1); //Control point 1 si b ou point si l
//                        second = m.group(2); //Control point 2 si b ou rien si l
//                        last = m.group(3); //Point si b ou rien si l
//                        close = m.group(4); //Close si b ou rien si l
//
//                        if(verylast.equalsIgnoreCase(first) && second.equalsIgnoreCase(last)){
//                            newCommands = newCommands + " l " + last;
//                        }else{
//                            newCommands = newCommands + " b " + first + " " + second + " " + last;
//                            if(close.isEmpty()==false){
//                                newCommands = newCommands + " c";
//                            }
//                        }
//                        verylast = last;
//                    }                    
//                }
//            }
//            assCommands = newCommands;
            
            
            return assCommands;
        }
        
        private List<String> getCleanTags(String assCommands){
            //Crée un schéma (commande)
            List<String> sList = new ArrayList<String>();
            String lastWords = assCommands;
            while(lastWords.indexOf("b")!=-1 | lastWords.indexOf("l")!=-1 | lastWords.indexOf("m")!=-1){
                int[] ta = new int[3];
                ta[0] = lastWords.indexOf("b");
                ta[1] = lastWords.indexOf("l");
                ta[2] = lastWords.indexOf("m");
                
                int m = getSmaller(ta, true);
                
                if(m!=1){
                    int[] tb = new int[3];
                    tb[0] = lastWords.indexOf("b", m+1);
                    tb[1] = lastWords.indexOf("l", m+1);
                    tb[2] = lastWords.indexOf("m", m+1);
                    
                    int n = getSmaller(tb, true);
                    
                    if(n!=-1){
                        sList.add(lastWords.substring(m, n));
                        lastWords = lastWords.substring(n);
                    }else{
                        sList.add(lastWords.substring(m));
                        lastWords = "";
                    }
                }
            }
            
            return sList;
        }
        
        private List<String> getCleanSVGTags(String assCommands){
            //Crée un schéma (commande)
            List<String> sList = new ArrayList<String>();
            String lastWords = assCommands;
            while(lastWords.indexOf("M")!=-1 | lastWords.indexOf("m")!=-1 
                    | lastWords.indexOf("L")!=-1 | lastWords.indexOf("l")!=-1
                    | lastWords.indexOf("H")!=-1 | lastWords.indexOf("h")!=-1
                    | lastWords.indexOf("V")!=-1 | lastWords.indexOf("v")!=-1
                    | lastWords.indexOf("C")!=-1 | lastWords.indexOf("c")!=-1){
                
                int[] ta = new int[16];
                ta[0] = lastWords.indexOf("M");
                ta[1] = lastWords.indexOf("m");
                ta[2] = lastWords.indexOf("L");
                ta[3] = lastWords.indexOf("l");
                ta[4] = lastWords.indexOf("H");
                ta[5] = lastWords.indexOf("h");
                ta[6] = lastWords.indexOf("V");
                ta[7] = lastWords.indexOf("v");
                ta[8] = lastWords.indexOf("C");
                ta[9] = lastWords.indexOf("c");
                ta[10] = lastWords.indexOf("S");
                ta[11] = lastWords.indexOf("s");
                ta[12] = lastWords.indexOf("Q");
                ta[13] = lastWords.indexOf("q");
                ta[14] = lastWords.indexOf("T");
                ta[15] = lastWords.indexOf("t");
                
                int m = getSmaller(ta, true);
                
                if(m!=1){
                    int[] tb = new int[16];
                    tb[0] = lastWords.indexOf("M", m+1);
                    tb[1] = lastWords.indexOf("m", m+1);
                    tb[2] = lastWords.indexOf("L", m+1);
                    tb[3] = lastWords.indexOf("l", m+1);
                    tb[4] = lastWords.indexOf("H", m+1);
                    tb[5] = lastWords.indexOf("h", m+1);
                    tb[6] = lastWords.indexOf("V", m+1);
                    tb[7] = lastWords.indexOf("v", m+1);
                    tb[8] = lastWords.indexOf("C", m+1);
                    tb[9] = lastWords.indexOf("c", m+1);
                    tb[10] = lastWords.indexOf("S", m+1);
                    tb[11] = lastWords.indexOf("s", m+1);
                    tb[12] = lastWords.indexOf("Q", m+1);
                    tb[13] = lastWords.indexOf("q", m+1);
                    tb[14] = lastWords.indexOf("T", m+1);
                    tb[15] = lastWords.indexOf("t", m+1);
                    
                    int n = getSmaller(tb, true);
                    
                    if(n!=-1){
                        sList.add(lastWords.substring(m, n));
                        lastWords = lastWords.substring(n);
                    }else{
                        sList.add(lastWords.substring(m));
                        lastWords = "";
                    }
                }
            }
            
            return sList;
        }
        
        private int getSmaller(int[] table, boolean positive){
            //On crée une list pour pouvoir les trier
            List<Integer> li = new ArrayList<Integer>();
            for(int i = 0; i<table.length; i++){
                li.add(table[i]);
            }
            
            //On effectue le tri
            Collections.sort(li, new Comparator() {
            @Override
                public int compare(Object o1, Object o2) {
                    return ((Comparable) o1).compareTo(o2);
                }
            });
            
            //On renvoie si possible une valeur positive
            if(positive==true){
                for(int i : li){
                    if(i>=0){ return i; }
                }
            }
            
            //Ou alors on renvoie la première valeur
            return li.get(0);
        }
        
        private int getX(String coordinates){
            String[] table = coordinates.split(" ");
            return Integer.parseInt(table[0]);
        }
        
        private int getY(String coordinates){
            String[] table = coordinates.split(" ");
            return Integer.parseInt(table[1]);
        }
        
    }
    
}
