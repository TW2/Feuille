/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.scripting;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
public class XmlSnippetHandler {
    /*   Structure à lire :
     * <snippetcollection>
     *      <snippet type="">
     *          <name>Name</name>
     *          <elements>
     *              <element author="" description="" language="">code</element>
     *              <element author="" description="" language="">code</element>
     *              <element author="" description="" language="">code</element>
     *              ...
     *          </elements>
     *      </snippet>
     * </snippetcollection> */
    
    SnippetHandler sh;
    
    public XmlSnippetHandler(String path) throws ParserConfigurationException, SAXException, IOException{
        SAXParserFactory fabrique = SAXParserFactory.newInstance();
        SAXParser parseur = fabrique.newSAXParser();

        File fichier = new File(path);
        sh = new SnippetHandler();
        parseur.parse(fichier, sh);
    }
    
    public List<Snippet> getSnippets(){
        return sh.getSnippets();
    }
    
    public class SnippetHandler extends DefaultHandler{
        
        //résultats de notre parsing
	private List<Snippet> sniList;
        private Snippet sni;
        private SnippetElement se;
        private String currentType, currentName, currentAuthor, currentDescription,
                currentLanguage, currentCode, currentElement;
	//flags nous indiquant la position du parseur
	private boolean inSnippetColection, inSnippet, inName, inElements, inElement;
	//buffer nous permettant de récupérer les données 
	private StringBuffer buffer;
        
        public SnippetHandler(){
            super();
        }
        
        public List<Snippet> getSnippets(){
            return sniList;
        }
        
        //détection d'ouverture de balise
        @Override
        public void startElement(String uri, String localName,
			String qName, Attributes attributes) throws SAXException{
            if(qName.equals("snippetcollection")){
                sniList = new ArrayList<Snippet>();
                inSnippetColection = true;
            }else{
                buffer = new StringBuffer();
                if(qName.equals("snippet")){
                    sni = new Snippet();
                    currentType = attributes.getValue(0);
                    inSnippet = true;
                }else if(qName.equals("name")){
                    inName = true;
                }else if(qName.equals("elements")){
                    inElements = true;
                }else if(qName.equals("element")){
                    se = new SnippetElement();
                    currentAuthor = attributes.getValue(0);
                    currentDescription = attributes.getValue(1);
                    currentLanguage = attributes.getValue(2);
                    inElement = true;
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
            if(qName.equals("snippetcollection")){
                inSnippetColection = false;
            }else{
                if(qName.equals("snippet")){
                    sni.setType(currentType);
                    sni.setTitle(currentName);
                    sniList.add(sni);
                    buffer = null;
                    inSnippet = false;
                }else if(qName.equals("name")){
                    currentName = buffer.toString();
                    buffer = null;
                    inName = false;
                }else if(qName.equals("elements")){
                    buffer = null;
                    inElements = false;
                }else if(qName.equals("element")){
                    currentCode = buffer.toString();
                    buffer = null;
                    se.setAuthor(currentAuthor);
                    se.setDescription(currentDescription);
                    se.setLanguageCode(currentLanguage);
                    se.setCode(currentCode);
                    sni.addSnippetElement(se);
                    inElement = false;
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
        
    }
    
}
