/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package feuille.karaoke;

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
import feuille.karaoke.lib.FxObject;

/**
 * <p>This class is a XML file writer (Save XFX file).<br />
 * Cette classe est un enregistreur de fichier XML (Sauvegarde les fichiers XFX).</p>
 * @author The Wingate 2940
 */
public class XmlPresetWriter {
    
    /** <p>Container of FxObjects.<br />Conteneur de FxObject.</p> */
    public class XmlPresetSource extends org.xml.sax.InputSource{
        
        // Storage of FxObjects
        List<FxObject> lfxo = new ArrayList<FxObject>();

        /** <p>Create a new XmlPresetSource.<br />
         * Crée un nouveau XmlPresetSource.</p> */
        public XmlPresetSource(List<FxObject> lfxo){
            super();
            this.lfxo = lfxo;

        }
        
        /** <p>Get a list of FxObject.<br />
         * Obtient une liste de FxObject.</p> */
        public List<FxObject> getFxObjectList(){
            return lfxo;
        }
    }
    
    /** <p>The way to read an XML of XmlPresets (XFX).<br />
     * Comment lire un XML de XmlPresets (XFX).</p> */
    public class XmlPresetReader implements org.xml.sax.XMLReader{

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

            if(!(input instanceof XmlPresetSource)){
                throw new SAXException("The object isn't a XmlPresetSource");
            }
            if(chandler == null){
                throw new SAXException("ContentHandler not defined");
            }

            XmlPresetSource source = (XmlPresetSource)input;
            List<FxObject> lfxo = source.getFxObjectList();

            // Main element - beginning
            chandler.startDocument();
            chandler.startElement("", "sheet", "sheet", attributes);

            // FxOjects element
            for(FxObject fxo : lfxo){

                // FxOject element - beginning
                chandler.startElement("", "effect", "effect", attributes);
                
                // Name block
                chandler.startElement("", "name", "name", attributes);
                char[] name = fxo.getName().toCharArray();
                chandler.characters(name,0,name.length);
                chandler.endElement("", "name", "name");

                // Type block
                chandler.startElement("", "type", "type", attributes);
                char[] type = fxo.getFunction().toCharArray();
                chandler.characters(type,0,type.length);
                chandler.endElement("", "type", "type");
                
                // Moment block
                chandler.startElement("", "moment", "moment", attributes);
                char[] moment = fxo.getMoment().toCharArray() ;
                chandler.characters(moment,0,moment.length);
                chandler.endElement("", "moment", "moment");
                
                // Commands block
                chandler.startElement("", "commands", "commands", attributes);
                char[] commands = fxo.getCommands().toCharArray() ;
                chandler.characters(commands,0,commands.length);
                chandler.endElement("", "commands", "commands");
                
                // Time block
                chandler.startElement("", "time", "time", attributes);
                char[] time = fxo.getTime().toCharArray() ;
                chandler.characters(time,0,time.length);
                chandler.endElement("", "time", "time");
                
                // Image block
                chandler.startElement("", "image", "image", attributes);
                char[] image = fxo.getImage().toCharArray() ;
                chandler.characters(image,0,image.length);
                chandler.endElement("", "image", "image");
                
                // Author block
                chandler.startElement("", "author", "author", attributes);
                char[] author = fxo.getAuthor().toCharArray() ;
                chandler.characters(author,0,author.length);
                chandler.endElement("", "author", "author");
                
                // Comment block
                chandler.startElement("", "comment", "comment", attributes);
                char[] comment = fxo.getDescription().toCharArray() ;
                chandler.characters(comment,0,comment.length);
                chandler.endElement("", "comment", "comment");
                
                // Layer block
                chandler.startElement("", "layer", "layer", attributes);
                char[] layer = fxo.getFirstLayer().toCharArray() ;
                chandler.characters(layer,0,layer.length);
                chandler.endElement("", "layer", "layer");
                
                // Style block
                chandler.startElement("", "style", "style", attributes);
                char[] style = fxo.getStyle().toCharArray() ;
                chandler.characters(style,0,style.length);
                chandler.endElement("", "style", "style");
                
                // Collect block
                chandler.startElement("", "collect", "collect", attributes);
                char[] collect = fxo.getCollect().toCharArray() ;
                chandler.characters(collect,0,collect.length);
                chandler.endElement("", "collect", "collect");
                
                // Ruby block
                chandler.startElement("", "ruby", "ruby", attributes);
                char[] ruby = fxo.getRubyCode().toCharArray() ;
                chandler.characters(ruby,0,ruby.length);
                chandler.endElement("", "ruby", "ruby");
                
                // FxOject element - end
                chandler.endElement("", "effect", "effect");
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
    
    // Storage of FxObjects
    List<FxObject> lfxo = new ArrayList<FxObject>();

    /** <p>Create a new XmlPresetWriter.<br />
     * Crée un nouveau XmlPresetWriter.</p> */
    public XmlPresetWriter(){

    }
    
    /** <p>Create a new XML file.<br />Crée un nouveau fichier XML.</p> */
    public boolean createXmlPreset(String path){
        org.xml.sax.XMLReader pread = new XmlPresetReader();
        InputSource psource = new XmlPresetSource(lfxo);
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
    public void setFxObjectList(List<FxObject> lfxo){
        this.lfxo = lfxo;
    }

}
