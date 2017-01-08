/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.drawing.adf;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
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
 * @author The Wingate 2940
 */
public class XmlDrawingWriter {
    /*   Structure à écrire :
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
    
    DrawingObject dro = new DrawingObject();
    
    public XmlDrawingWriter(){
        
    }
    
    public class DROSource extends org.xml.sax.InputSource{
        
        DrawingObject dro = new DrawingObject();
        
        public DROSource(DrawingObject dro){
            super();
            this.dro = dro;
        }
        
        public DrawingObject getDrawingObject(){
            return dro;
        }
        
    }
    
    public class DROReader implements org.xml.sax.XMLReader{
        
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
            
            if(!(input instanceof DROSource)){
                throw new SAXException("The object isn't a DROSource");
            }
            if(chandler == null){
                throw new SAXException("ContentHandler not defined");
            }
            
            DROSource source = (DROSource)input;
            DrawingObject dro = source.getDrawingObject();
            
            // Main element - beginning
            chandler.startDocument();
            chandler.startElement("", "drawing", "drawing", attributes);
            
            // Layers element - beginning
            chandler.startElement("", "layers", "layers", attributes);
            
            for(LayerContent lc : dro.getLayers()){
                
                // Layer block                
                attributes.addAttribute("", "", "name", "string", lc.getName());
                attributes.addAttribute("", "", "color", "string", lc.toHTMLColor());
                chandler.startElement("", "layer", "layer", attributes);
                char[] commands = lc.getAssCommands().toCharArray();
                chandler.characters(commands,0,commands.length);
                chandler.endElement("", "layer", "layer");
                attributes.clear();
                
            }
            
            chandler.endElement("", "layers", "layers");
            
            // Image block
            chandler.startElement("", "image", "image", attributes);
            char[] image = dro.imageToBase64().toCharArray();
            chandler.characters(image,0,image.length);
            chandler.endElement("", "image", "image");
            
            // Image block
            attributes.addAttribute("", "", "x", "string", Integer.toString(dro.getIconPositionX()));
            attributes.addAttribute("", "", "y", "string", Integer.toString(dro.getIconPositionY()));
            chandler.startElement("", "photo", "photo", attributes);
            char[] photo = dro.iconToBase64().toCharArray();
            chandler.characters(photo,0,photo.length);
            chandler.endElement("", "photo", "photo");
            attributes.clear();
            
            chandler.endElement("", "drawing", "drawing");
            chandler.endDocument();
        }

        @Override
        public void parse(String systemId) throws IOException, SAXException {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
    }
    
    public boolean createDrawing(String path){
        org.xml.sax.XMLReader pread = new DROReader();
        InputSource psource = new DROSource(dro);
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
    
    public void setDrawingObject(DrawingObject dro){
        this.dro = dro;
    }
}
