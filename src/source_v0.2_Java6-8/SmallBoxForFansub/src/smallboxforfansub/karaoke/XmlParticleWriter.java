/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smallboxforfansub.karaoke;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.transform.*;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.*;
import org.xml.sax.helpers.AttributesImpl;
import smallboxforfansub.karaoke.lib.ParticleObject;

/**
 *
 * @author The Wingate 2940
 */
public class XmlParticleWriter {
    
    // Storage of FxObjects
    List<ParticleObject> lpo = new ArrayList<ParticleObject>();

    /** <p>Create a new XmlPresetWriter.<br />
     * Crée un nouveau XmlPresetWriter.</p> */
    public XmlParticleWriter(){

    }
    
    /** <p>Container of FxObjects.<br />Conteneur de FxObject.</p> */
    public class ParticleSource extends org.xml.sax.InputSource{
        
        // Storage of FxObjects
        List<ParticleObject> lpo = new ArrayList<ParticleObject>();

        /** <p>Create a new XmlPresetSource.<br />
         * Crée un nouveau XmlPresetSource.</p> */
        public ParticleSource(List<ParticleObject> lpo){
            super();
            this.lpo = lpo;

        }
        
        /** <p>Get a list of FxObject.<br />
         * Obtient une liste de FxObject.</p> */
        public List<ParticleObject> getParticleObjectList(){
            return lpo;
        }
    }
    
    /** <p>The way to read an XML of XmlPresets (XFX).<br />
     * Comment lire un XML de XmlPresets (XFX).</p> */
    public class ParticleReader implements org.xml.sax.XMLReader{

        private ContentHandler chandler;
        private AttributesImpl attributes = new AttributesImpl();
        private Map<String,Boolean> features = new HashMap<String,Boolean>();
        private Map<String,Object> properties = new HashMap<String,Object>();
        private EntityResolver resolver;
        private DTDHandler dhandler;
        private ErrorHandler ehandler;
        
        @Override
        public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
            return features.get(name).booleanValue();
        }

        @Override
        public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
            try{
                features.put(name, value);
            }catch(Exception ex){
            }            
        }

        @Override
        public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
            return properties.get(name);
        }

        @Override
        public void setProperty(String name, Object value) throws SAXNotRecognizedException, SAXNotSupportedException {
            try{
                properties.put(name, value);
            }catch(Exception ex){
            }  
        }

        @Override
        public void setEntityResolver(EntityResolver resolver) {
            this.resolver = resolver;
        }

        @Override
        public EntityResolver getEntityResolver() {
            return resolver;
        }

        @Override
        public void setDTDHandler(DTDHandler handler) {
            this.dhandler = handler;
        }

        @Override
        public DTDHandler getDTDHandler() {
            return dhandler;
        }

        @Override
        public void setContentHandler(ContentHandler handler) {
            this.chandler = handler;
        }

        @Override
        public ContentHandler getContentHandler() {
            return chandler;
        }

        @Override
        public void setErrorHandler(ErrorHandler handler) {
            this.ehandler = handler;
        }

        @Override
        public ErrorHandler getErrorHandler() {
            return ehandler;
        }

        @Override
        public void parse(InputSource input) throws IOException, SAXException {

            if(!(input instanceof ParticleSource)){
                throw new SAXException("The object isn't a ParticleSource");
            }
            if(chandler == null){
                throw new SAXException("ContentHandler not defined");
            }

            ParticleSource source = (ParticleSource)input;
            List<ParticleObject> lpo = source.getParticleObjectList();

            // Main element - beginning
            chandler.startDocument();
            chandler.startElement("", "sheet", "sheet", attributes);

            // ParticleOjects element
            for(ParticleObject po : lpo){

                // ParticleOject element - beginning
                chandler.startElement("", "particle", "particle", attributes);
                
                // Name block
                chandler.startElement("", "name", "name", attributes);
                char[] name = po.getName().toCharArray();
                chandler.characters(name,0,name.length);
                chandler.endElement("", "name", "name");
                
                // Mode block
                chandler.startElement("", "mode", "mode", attributes);
                char[] mode = po.getMode().toCharArray();
                chandler.characters(mode,0,mode.length);
                chandler.endElement("", "mode", "mode");

                // Moment block
                chandler.startElement("", "moment", "moment", attributes);
                char[] moment = po.getMoment().toCharArray() ;
                chandler.characters(moment,0,moment.length);
                chandler.endElement("", "moment", "moment");
                
                // Commands block
                chandler.startElement("", "commands", "commands", attributes);
                char[] commands = po.getCommands().toCharArray() ;
                chandler.characters(commands,0,commands.length);
                chandler.endElement("", "commands", "commands");
                
                // Time block
                chandler.startElement("", "time", "time", attributes);
                char[] time = po.getTime().toCharArray() ;
                chandler.characters(time,0,time.length);
                chandler.endElement("", "time", "time");
                
                // Position Correction block
//                chandler.startElement("", "poscorrection", "poscorrection", attributes);
//                char[] poscorrection = po.getPosCorrection().toCharArray() ;
//                chandler.characters(poscorrection,0,poscorrection.length);
//                chandler.endElement("", "poscorrection", "poscorrection");
                
                // Space Correction block
//                chandler.startElement("", "spacorrection", "spacorrection", attributes);
//                char[] spacorrection = po.getSpaCorrection().toCharArray() ;
//                chandler.characters(spacorrection,0,spacorrection.length);
//                chandler.endElement("", "spacorrection", "spacorrection");
                
                // Video Width block
                chandler.startElement("", "videowidth", "videowidth", attributes);
                char[] videowidth = po.getVideoWidth().toCharArray() ;
                chandler.characters(videowidth,0,videowidth.length);
                chandler.endElement("", "videowidth", "videowidth");
                
                // Video Height block
                chandler.startElement("", "videoheight", "videoheight", attributes);
                char[] videoheight = po.getVideoHeight().toCharArray() ;
                chandler.characters(videoheight,0,videoheight.length);
                chandler.endElement("", "videoheight", "videoheight");
                
                // PosY block
                chandler.startElement("", "posy", "posy", attributes);
                char[] posy = po.getPosY().toCharArray() ;
                chandler.characters(posy,0,posy.length);
                chandler.endElement("", "posy", "posy");
                
                // Image block
                chandler.startElement("", "image", "image", attributes);
                char[] image = po.getImage().toCharArray() ;
                chandler.characters(image,0,image.length);
                chandler.endElement("", "image", "image");
                
                // Author block
                chandler.startElement("", "author", "author", attributes);
                char[] author = po.getAuthor().toCharArray() ;
                chandler.characters(author,0,author.length);
                chandler.endElement("", "author", "author");
                
                // Comment block
                chandler.startElement("", "comment", "comment", attributes);
                char[] comment = po.getDescription().toCharArray() ;
                chandler.characters(comment,0,comment.length);
                chandler.endElement("", "comment", "comment");
                
                // Layer block
                chandler.startElement("", "layer", "layer", attributes);
                char[] layer = po.getFirstLayer().toCharArray() ;
                chandler.characters(layer,0,layer.length);
                chandler.endElement("", "layer", "layer");
                
                // Style block
                chandler.startElement("", "style", "style", attributes);
                char[] style = po.getStyle().toCharArray() ;
                chandler.characters(style,0,style.length);
                chandler.endElement("", "style", "style");
                
                // Collect block
                chandler.startElement("", "collect", "collect", attributes);
                char[] collect = po.getCollect().toCharArray() ;
                chandler.characters(collect,0,collect.length);
                chandler.endElement("", "collect", "collect");
                
                // RubyCode block
                chandler.startElement("", "ruby", "ruby", attributes);
                char[] ruby = po.getRubyCode().toCharArray() ;
                chandler.characters(ruby,0,ruby.length);
                chandler.endElement("", "ruby", "ruby");
                
                // Type block
                chandler.startElement("", "type", "type", attributes);
                char[] type = po.getType().toCharArray() ;
                chandler.characters(type,0,type.length);
                chandler.endElement("", "type", "type");
                
                // ParticleOject element - end
                chandler.endElement("", "particle", "particle");
            }

            // Main element - end
            chandler.endElement("", "sheet", "sheet");
            chandler.endDocument();

        }

        @Override
        public void parse(String systemId) throws IOException, SAXException {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
    }
    
    /** <p>Create a new XML file.<br />Crée un nouveau fichier XML.</p> */
    public boolean createParticle(String path){
        org.xml.sax.XMLReader pread = new ParticleReader();
        InputSource psource = new ParticleSource(lpo);
        Source source = new SAXSource(pread, psource);

        File file = new File(path);
        Result resultat = new StreamResult(file);
        
        try {
            TransformerFactory fabrique = TransformerFactory.newInstance();
            Transformer transformer;
            transformer = fabrique.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.transform(source, resultat);
        } catch (TransformerConfigurationException ex) {
            return false;
        } catch (TransformerException ex) {
            return false;
        }
        return true;
    }
    
    /** <p>Set a new list of FxObject.<br />
     * Définit une nouvelle liste FxObject.</p> */
    public void setParticleObjectList(List<ParticleObject> lpo){
        this.lpo = lpo;
    }
    
}
