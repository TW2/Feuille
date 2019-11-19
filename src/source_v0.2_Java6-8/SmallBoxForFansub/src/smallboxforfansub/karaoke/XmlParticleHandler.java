/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smallboxforfansub.karaoke;

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
import smallboxforfansub.karaoke.lib.ParticleObject;


/**
 *
 * @author The Wingate 2940
 */
public class XmlParticleHandler {
    
    ParticleHandler ph;
    
    public XmlParticleHandler(String path) 
            throws ParserConfigurationException, SAXException, IOException{
        SAXParserFactory fabrique = SAXParserFactory.newInstance();
        SAXParser parseur = fabrique.newSAXParser();

        File fichier = new File(path);
        ph = new ParticleHandler();
        parseur.parse(fichier, ph);
    }
    
    public List<ParticleObject> getParticleObjectList(){
        return ph.getParticleList();
    }
    
    public class ParticleHandler extends DefaultHandler{
	//résultats de notre parsing
	private List<ParticleObject> lpo;
	private ParticleObject po;
	//flags nous indiquant la position du parseur
	private boolean inSheet, inParticle, inName, inMoment, inCommands,
                inTime, inPoscorrection, inSpacorrection, inVideowidth, 
                inVideoheight, inImage, inAuthors, inComment, inLayer, inStyle,
                inCollect, inPosY, inMode, inRubyCode, inType;
	//buffer nous permettant de récupérer les données 
	private StringBuffer buffer;

	// simple constructeur
	public ParticleHandler(){
            super();
	}
        
        public List<ParticleObject> getParticleList(){
            return lpo;
        }
        
	//détection d'ouverture de balise
        @Override
	public void startElement(String uri, String localName,
			String qName, Attributes attributes) throws SAXException{
            if(qName.equals("sheet")){
                lpo = new LinkedList<ParticleObject>();
                inSheet = true;
            }else if(qName.equals("particle")){
                po = new ParticleObject();
                inParticle = true;
            }else {
                buffer = new StringBuffer();
                if(qName.equals("name")){
                        inName = true;
                }else if(qName.equals("mode")){
                        inMode = true;
                }else if(qName.equals("moment")){
                        inMoment = true;
                }else if(qName.equals("commands")){
                        inCommands = true;
                }else if(qName.equals("time")){
                        inTime = true;
                }else if(qName.equals("poscorrection")){
                        inPoscorrection = true;
                }else if(qName.equals("spacorrection")){
                        inSpacorrection = true;
                }else if(qName.equals("videowidth")){
                        inVideowidth = true;
                }else if(qName.equals("videoheight")){
                        inVideoheight = true;
                }else if(qName.equals("posy")){
                        inPosY = true;
                }else if(qName.equals("image")){
                        inImage = true;
                }else if(qName.equals("authors")){
                        inAuthors = true;
                }else if(qName.equals("comment")){
                        inComment = true;
                }else if(qName.equals("layer")){
                        inLayer = true;
                }else if(qName.equals("style")){
                        inStyle = true;
                }else if(qName.equals("collect")){
                        inCollect = true;
                }else if(qName.equals("ruby")){
                        inRubyCode = true;
                }else if(qName.equals("type")){
                        inType = true;
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
            }else if(qName.equals("particle")){
                    lpo.add(po);
                    po = null;
                    inParticle = false;
            }else if(qName.equals("name")){
                    po.setName(buffer.toString());
                    buffer = null;
                    inName = false;
            }else if(qName.equals("mode")){
                    po.setMode(buffer.toString());
                    buffer = null;
                    inMode = false;
            }else if(qName.equals("moment")){
                    po.setMoment(buffer.toString());
                    buffer = null;
                    inMoment = false;
            }else if(qName.equals("commands")){
                    po.setCommands(buffer.toString());
                    buffer = null;
                    inCommands = false;
            }else if(qName.equals("time")){
                    po.setTime(buffer.toString());
                    buffer = null;
                    inTime = false;
            }else if(qName.equals("poscorrection")){
//                    po.setPosCorrection(buffer.toString());
//                    buffer = null;
//                    inPoscorrection = false;
            }else if(qName.equals("spacorrection")){
//                    po.setSpaCorrection(buffer.toString());
//                    buffer = null;
//                    inSpacorrection = false;
            }else if(qName.equals("videowidth")){
                    po.setVideoWidth(buffer.toString());
                    buffer = null;
                    inVideowidth = false;
            }else if(qName.equals("videoheight")){
                    po.setVideoHeight(buffer.toString());
                    buffer = null;
                    inVideoheight = false;
            }else if(qName.equals("posy")){
                    po.setPosY(buffer.toString());
                    buffer = null;
                    inPosY = false;
            }else if(qName.equals("image")){
                    po.setImage(buffer.toString());
                    buffer = null;
                    inImage = false;
            }else if(qName.equals("authors")){
                    po.setAuthor(buffer.toString());
                    buffer = null;
                    inAuthors= false;
            }else if(qName.equals("comment")){
                    po.setDescription(buffer.toString());
                    buffer = null;
                    inComment = false;
            }else if(qName.equals("layer")){
                    po.setFirstLayer(buffer.toString());
                    buffer = null;
                    inLayer = false;
            }else if(qName.equals("style")){
                    po.setStyle(buffer.toString());
                    buffer = null;
                    inStyle = false;
            }else if(qName.equals("collect")){
                    po.setCollect(buffer.toString());
                    buffer = null;
                    inCollect = false;
            }else if(qName.equals("ruby")){
                    po.setRubyCode(buffer.toString());
                    buffer = null;
                    inRubyCode = false;
            }else if(qName.equals("type")){
                    po.setType(buffer.toString());
                    buffer = null;
                    inType = false;
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
//            for(ParticleObject p : lpo){
//                    System.out.println(p);
//            }
	}
    }

}
