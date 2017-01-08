/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.drawing.adf;

import java.io.File;
import java.io.IOException;
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
public class XmlDrawingHandler {
    /*   Structure à lire :
     * <drawing>
     *      <layers>
     *          <layer name="" color="">commands</layer>
     *		<layer name="" color="">commands</layer>
     *		<layer name="" color="">commands</layer>
     *		...
     *      </layers>
     *      <image>IMAGE EN BASE64</image>
     *      <photo x="" y="">PHOTO EN BASE64</photo>
     * </drawing> */
    
    DrawingHandler dh;
    
    public XmlDrawingHandler(String path) throws ParserConfigurationException, SAXException, IOException{
        SAXParserFactory fabrique = SAXParserFactory.newInstance();
        SAXParser parseur = fabrique.newSAXParser();

        File fichier = new File(path);
        dh = new DrawingHandler();
        parseur.parse(fichier, dh);
    }
    
    public DrawingObject getDrawingObject(){
        return dh.getDrawingObject();
    }
    
    public class DrawingHandler extends DefaultHandler{
        
        //résultats de notre parsing
	private DrawingObject dro;
        private LayerContent lc;
        private String currentName, currentColor, currentX, currentY;
	//flags nous indiquant la position du parseur
	private boolean inDrawing, inLayers, inLayer, inImage, inPhoto;
	//buffer nous permettant de récupérer les données 
	private StringBuffer buffer;
        
        public DrawingHandler(){
            super();
        }
        
        public DrawingObject getDrawingObject(){
            return dro;
        }
        
        //détection d'ouverture de balise
        @Override
        public void startElement(String uri, String localName,
			String qName, Attributes attributes) throws SAXException{
            if(qName.equals("drawing")){
                dro = new DrawingObject();
                inDrawing = true;
            }else{
                buffer = new StringBuffer();
                if(qName.equals("layers")){
                    inLayers = true;
                }else if(qName.equals("layer")){
                    lc = new LayerContent();
                    currentName = attributes.getValue(0);
                    currentColor = attributes.getValue(1);
                    inLayer = true;
                }else if(qName.equals("image")){
                    inImage = true;
                }else if(qName.equals("photo")){
                    currentX = attributes.getValue(0);
                    currentY = attributes.getValue(1);
                    inPhoto = true;
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
            if(qName.equals("drawing")){
                inDrawing = false;
            }else{
                if(qName.equals("layers")){
                    inLayers = false;
                }else if(qName.equals("layer")){
                    lc.setName(currentName);
                    lc.fromHTMLColor(currentColor);
                    lc.setAssCommands(buffer.toString());
                    dro.addLayer(lc);
                    buffer = null;
                    inLayer = false;
                }else if(qName.equals("image")){
                    try {
                        dro.imageFromBase64(buffer.toString());
                    } catch (IOException ex) {
                        //Dommage pour nous xD;
                    }
                    buffer = null;
                    inImage = false;
                }else if(qName.equals("photo")){
                    try {
                        dro.iconFromBase64(buffer.toString());
                    } catch (IOException ex) {
                        //Dommage pour nous xD;
                    }
                    dro.setIconPosition(Integer.parseInt(currentX), Integer.parseInt(currentY));
                    buffer = null;
                    inPhoto = false;
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
