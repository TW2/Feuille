/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.lib;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
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
public class XmlLangHandler {
    
    LangHandler lh;
    
    public XmlLangHandler(String path) 
            throws ParserConfigurationException, SAXException, IOException{
        SAXParserFactory fabrique = SAXParserFactory.newInstance();
        SAXParser parseur = fabrique.newSAXParser();

        File fichier = new File(path);
        lh = new LangHandler();
        parseur.parse(fichier, lh);
    }
    
    public Map<String, String> getLangMap(){
        return lh.getLangMap();
    }
    
    public class LangHandler extends DefaultHandler{
        
        //résultats de notre parsing
	private Map<String, String> myMap;
        String key, value;
	//flags nous indiquant la position du parseur
	private boolean inSheet, inLang, inKey, inValue;
        //buffer nous permettant de récupérer les données 
	private StringBuffer buffer;
        
        public LangHandler(){
            super();
	}
        
        public Map<String, String> getLangMap(){
            return myMap;
        }
        
        @Override
        public void startElement(String uri, String localName,
			String qName, Attributes attributes) throws SAXException{
            if(qName.equals("sheet")){
                myMap = new LinkedHashMap<String, String>();
                inSheet = true;
            }else if(qName.equals("lang")){
                inLang = true;
            }else {
                buffer = new StringBuffer();
                if(qName.equals("key")){
                        inKey = true;
                }else if(qName.equals("value")){
                        inValue = true;
                }else{
                    //Rien
                }
            }
        }
        
        @Override
        public void endElement(String uri, String localName, String qName)
			throws SAXException{            
            if(qName.equals("sheet")){
                    inSheet = false;
            }else if(qName.equals("lang")){
                    myMap.put(key, value);
                    key = null;
                    value = null;
                    inLang = false;
            }else if(qName.equals("key")){
                    key = buffer.toString();
                    buffer = null;
                    inKey = false;
            }else if(qName.equals("value")){
                    value = buffer.toString();
                    buffer = null;
                    inValue = false;
            }else{
                //Rien
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
            //System.out.println("Début du parsing");
	}
        
	//fin du parsing
        @Override
	public void endDocument() throws SAXException {
            //System.out.println("Fin du parsing");
            //System.out.println("Resultats du parsing");
            //Résultats
	}
    }
    
}
