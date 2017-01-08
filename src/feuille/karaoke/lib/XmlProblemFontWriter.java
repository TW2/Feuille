/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.karaoke.lib;

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

/**
 *
 * @author The Wingate 2940
 */
public class XmlProblemFontWriter {
    
    // Storage of FxObjects
    List<FontWithCoef> lpo = new ArrayList<FontWithCoef>();

    /** <p>Create a new XmlPresetWriter.<br />
     * Crée un nouveau XmlPresetWriter.</p> */
    public XmlProblemFontWriter(){

    }
    
    /** <p>Container of FxObjects.<br />Conteneur de FxObject.</p> */
    public class ProblemFontSource extends org.xml.sax.InputSource{
        
        // Storage of FxObjects
        List<FontWithCoef> lpo = new ArrayList<FontWithCoef>();

        /** <p>Create a new XmlPresetSource.<br />
         * Crée un nouveau XmlPresetSource.</p> */
        public ProblemFontSource(List<FontWithCoef> lpo){
            super();
            this.lpo = lpo;

        }
        
        /** <p>Get a list of FxObject.<br />
         * Obtient une liste de FxObject.</p> */
        public List<FontWithCoef> getProblemFontList(){
            return lpo;
        }
    }
    
    /** <p>The way to read an XML of XmlPresets (XFX).<br />
     * Comment lire un XML de XmlPresets (XFX).</p> */
    public class ProblemFontReader implements org.xml.sax.XMLReader{

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

            if(!(input instanceof ProblemFontSource)){
                throw new SAXException("The object isn't a ParticleSource");
            }
            if(chandler == null){
                throw new SAXException("ContentHandler not defined");
            }

            ProblemFontSource source = (ProblemFontSource)input;
            List<FontWithCoef> lpo = source.getProblemFontList();

            // Main element - beginning
            chandler.startDocument();
            chandler.startElement("", "sheet", "sheet", attributes);

            // ParticleOjects element
            for(FontWithCoef po : lpo){

                // ParticleOject element - beginning
                chandler.startElement("", "pfont", "pfont", attributes);
                
                // Name block
                chandler.startElement("", "name", "name", attributes);
                char[] name = po.getFontName().toCharArray();
                chandler.characters(name,0,name.length);
                chandler.endElement("", "name", "name");
                
                // Mode block
                chandler.startElement("", "coef", "coef", attributes);
                char[] coef = po.getCoefInPercent().toCharArray();
                chandler.characters(coef,0,coef.length);
                chandler.endElement("", "coef", "coef");

                // ParticleOject element - end
                chandler.endElement("", "pfont", "pfont");
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
    public boolean createProblemFont(String path){
        org.xml.sax.XMLReader pread = new ProblemFontReader();
        InputSource psource = new ProblemFontSource(lpo);
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
    public void setProblemFontList(List<FontWithCoef> lpo){
        this.lpo = lpo;
    }
    
}
