/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.karaoke.lib;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author The Wingate 2940
 */
public class XmlProblemFontHandler {
    
    FontWithCoefHandler ph;
    
    public XmlProblemFontHandler(String path) 
            throws ParserConfigurationException, SAXException, IOException{
        SAXParserFactory fabrique = SAXParserFactory.newInstance();
        SAXParser parseur = fabrique.newSAXParser();

        File fichier = new File(path);
        ph = new FontWithCoefHandler();
        parseur.parse(fichier, ph);
    }
    
    public List<FontWithCoef> getFontWithCoefList(){
        return ph.getFontWithCoefList();
    }
    
    public class FontWithCoefHandler extends DefaultHandler{
	//résultats de notre parsing
	private List<FontWithCoef> lpf;
	private FontWithCoef pf;
	//flags nous indiquant la position du parseur
	private boolean inSheet, inProblemFont, inName, inCoef;
	//buffer nous permettant de récupérer les données 
	private StringBuffer buffer;

	// simple constructeur
	public FontWithCoefHandler(){
            super();
	}
        
        public List<FontWithCoef> getFontWithCoefList(){
            return lpf;
        }
        
	//détection d'ouverture de balise
        @Override
	public void startElement(String uri, String localName,
			String qName, Attributes attributes) throws SAXException{
            if(qName.equals("sheet")){
                lpf = new LinkedList<FontWithCoef>();
                inSheet = true;
            }else if(qName.equals("pfont")){
                pf = new FontWithCoef();
                inProblemFont = true;
            }else {
                buffer = new StringBuffer();
                if(qName.equals("name")){
                        inName = true;
                }else if(qName.equals("coef")){
                        inCoef = true;
                }else{
                        //erreur, on peut lever une exception
//                        throw new SAXException("Balise "+qName+" inconnue.");
                }
            }
	}
	//détection fin de balise
        @Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException{
            if(qName.equals("sheet")){
                    inSheet = false;
            }else if(qName.equals("pfont")){
                    lpf.add(pf);
                    pf = null;
                    inProblemFont = false;
            }else if(qName.equals("name")){
                    pf.setFontName(buffer.toString());
                    buffer = null;
                    inName = false;
            }else if(qName.equals("coef")){
                    pf.setCoef(buffer.toString());
                    buffer = null;
                    inCoef = false;
            }else{
                    //erreur, on peut lever une exception
                    //throw new SAXException("Balise "+qName+" inconnue.");
            }          
	}
	//détection de caractères
        @Override
	public void characters(char[] ch,int start, int length)
			throws SAXException{
            String lecture = new String(ch,start,length);
            if(buffer != null) buffer.append(lecture);       
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
//            for(FontWithCoef p : lpf){
//                    System.out.println(p);
//            }
	}
    }
    
}
