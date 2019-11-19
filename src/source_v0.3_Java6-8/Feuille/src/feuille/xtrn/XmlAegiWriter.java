/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package feuille.xtrn;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.helpers.AttributesImpl;

/**
 *
 * @author Yves
 */
public class XmlAegiWriter {
    
    /*   Structure à écrire :
     * <aegilist>
     *      <aegiobj ef="" mode="" tr="">
     *          <name></name>
     *          <authors></authors>
     *          <description></description>
     *          <commands></commands>
     *      </aegiobj>
     *      ...
     * </aegilist> */
    
    // Storage of FxObjects
    List<AegiObject> aegilist = new ArrayList<>();

    /** <p>Create a new XmlPresetWriter.<br />
     * Crée un nouveau XmlPresetWriter.</p> */
    public XmlAegiWriter(){

    }
    
    /** <p>Container of FxObjects.<br />Conteneur de FxObject.</p> */
    public class AegiSource extends org.xml.sax.InputSource{
        
        // Storage of FxObjects
        List<AegiObject> aegilist = new ArrayList<>();

        /** <p>Create a new XmlPresetSource.<br />
         * Crée un nouveau XmlPresetSource.</p>
         * @param aegilist */
        public AegiSource(List<AegiObject> aegilist){
            super();
            this.aegilist = aegilist;

        }
        
        /** <p>Get a list of FxObject.<br />
         * Obtient une liste de FxObject.</p> */
        public List<AegiObject> getAegiObjectList(){
            return aegilist;
        }
    }
    
    /** <p>The way to read an XML of XmlPresets (XFX).<br />
     * Comment lire un XML de XmlPresets (XFX).</p> */
    public class AegiReader implements org.xml.sax.XMLReader{

        private ContentHandler chandler;
        private AttributesImpl attributes = new AttributesImpl();
        private Map<String,Boolean> features = new HashMap<>();
        private Map<String,Object> properties = new HashMap<>();
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

            if(!(input instanceof AegiSource)){
                throw new SAXException("The object isn't a ParticleSource");
            }
            if(chandler == null){
                throw new SAXException("ContentHandler not defined");
            }

            AegiSource source = (AegiSource)input;
            List<AegiObject> aegilist = source.getAegiObjectList();

            // Main element - beginning
            chandler.startDocument();
            chandler.startElement("", "aegilist", "aegilist", attributes);

            // AegiObjects element
            for(AegiObject ao : aegilist){

                // AegiObject element - beginning
                attributes.addAttribute("", "", "tr", "tr", ao.getTreatmentType().toString());
                attributes.addAttribute("", "", "mode", "mode", ao.getModeType().toString());
                attributes.addAttribute("", "", "ef", "ef", ao.getEffectType().toString());
                chandler.startElement("", "aegiobj", "aegiobj", attributes);
                attributes.clear();
                
                // Name block
                chandler.startElement("", "name", "name", attributes);
                char[] name = ao.getName().toCharArray();
                chandler.characters(name,0,name.length);
                chandler.endElement("", "name", "name");
                
                // Authors block
                chandler.startElement("", "authors", "authors", attributes);
                char[] authors = ao.getAuthors().toCharArray();
                chandler.characters(authors,0,authors.length);
                chandler.endElement("", "authors", "authors");
                
                // Description block
                chandler.startElement("", "description", "description", attributes);
                char[] description = ao.getDescription().toCharArray();
                chandler.characters(description,0,description.length);
                chandler.endElement("", "description", "description");
                
                // Commands block
                chandler.startElement("", "commands", "commands", attributes);
                char[] commands = ao.getCommands().toCharArray();
                chandler.characters(commands,0,commands.length);
                chandler.endElement("", "commands", "commands");

                // AegiObject element - end
                chandler.endElement("", "aegiobj", "aegiobj");
            }

            // Main element - end
            chandler.endElement("", "aegilist", "aegilist");
            chandler.endDocument();

        }

        @Override
        public void parse(String systemId) throws IOException, SAXException {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
    }
    
    /** <p>Create a new XML file.<br />Crée un nouveau fichier XML.</p>
     * @param path
     * @return  */
    public boolean createAegiBase(String path){
        org.xml.sax.XMLReader pread = new AegiReader();
        InputSource psource = new AegiSource(aegilist);
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
     * Définit une nouvelle liste FxObject.</p>
     * @param aegilist */
    public void setAegiObjectList(List<AegiObject> aegilist){
        this.aegilist = aegilist;
    }
    
}
