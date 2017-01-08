/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package feuille.xtrn;

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
 * @author Yves
 */
public class XmlAegiHandler {
    
    /*   Structure à lire :
     * <aegilist>
     *      <aegiobj ef="" mode="" tr="">
     *          <name></name>
     *          <authors></authors>
     *          <description></description>
     *          <commands></commands>
     *      </aegiobj>
     *      ...
     * </aegilist> */
    
    AegiHandler aegihand;
    
    public XmlAegiHandler(String path) throws ParserConfigurationException, SAXException, IOException{
        SAXParserFactory fabrique = SAXParserFactory.newInstance();
        SAXParser parseur = fabrique.newSAXParser();

        File fichier = new File(path);
        aegihand = new AegiHandler();
        parseur.parse(fichier, aegihand);
    }
    
    public List<AegiObject> getAegiObjectList(){
        return aegihand.getAegiObjectList();
    }
    
    public class AegiHandler extends DefaultHandler{
        
        //résultats de notre parsing
	private List<AegiObject> aegilist;
        private AegiObject aegi;
        private String currentEffectType, currentModeType, currentTreatmentType;
	//buffer nous permettant de récupérer les données 
	private StringBuffer buffer;
        
        public AegiHandler(){
            super();
        }
        
        public List<AegiObject> getAegiObjectList(){
            return aegilist;
        }
        
        //détection d'ouverture de balise
        @Override
        public void startElement(String uri, String localName,
			String qName, Attributes attributes) throws SAXException{
            if(qName.equals("aegilist")){
                aegilist = new ArrayList<>();
            }else{
                buffer = new StringBuffer();
                if(qName.equals("aegiobj")){
                    aegi = new AegiObject();
                    currentEffectType = attributes.getValue("ef");
                    currentModeType = attributes.getValue("mode");
                    currentTreatmentType = attributes.getValue("tr");
                }else if(qName.equals("name")){
                    
                }else if(qName.equals("authors")){
                    
                }else if(qName.equals("description")){
                    
                }else if(qName.equals("commands")){
                    
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
            if(qName.equals("aegilist")){
                
            }else{
                if(qName.equals("aegiobj")){
                    aegi.setEffectType(currentEffectType);
                    aegi.setModeType(currentModeType);
                    aegi.setTreatmentType(currentTreatmentType);
                    aegilist.add(aegi);
                }else if(qName.equals("name")){
                    aegi.setName(buffer.toString());
                    buffer = null;
                }else if(qName.equals("authors")){
                    aegi.setAuthors(buffer.toString());
                    buffer = null;
                }else if(qName.equals("description")){
                    aegi.setDescription(buffer.toString());
                    buffer = null;
                }else if(qName.equals("commands")){
                    aegi.setCommands(buffer.toString());
                    buffer = null;
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
