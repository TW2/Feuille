/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.scripting;

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
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.AttributesImpl;

/**
 *
 * @author The Wingate 2940
 */
public class XmlSnippetWriter {
    /*   Structure à écrire :
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
    
    List<Snippet> sniList = new ArrayList<Snippet>();
    
    public XmlSnippetWriter(){
        
    }
    
    public class SnippetsSource extends InputSource {
        List<Snippet> sniList = new ArrayList<Snippet>();
        
        public SnippetsSource(List<Snippet> sniList){
            super();
            this.sniList = sniList;
        }
        
        public List<Snippet> getSnippets(){
            return sniList;
        }
    }
    
    public class SnippetsReader implements XMLReader {
        
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
            /*   Structure à écrire :
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
            
            if(!(input instanceof SnippetsSource)){
                throw new SAXException("The object isn't a SnippetSource");
            }
            if(chandler == null){
                throw new SAXException("ContentHandler not defined");
            }
            
            SnippetsSource source = (SnippetsSource)input;
            List<Snippet> sniList = source.getSnippets();
            
            // Main element - beginning
            chandler.startDocument();
            chandler.startElement("", "snippetcollection", "snippetcollection", attributes);
            
            for(Snippet sni : sniList){
                
                // Snippet element - beginning
                attributes.addAttribute("", "", "type", "string", sni.getType());
                chandler.startElement("", "snippet", "snippet", attributes);
                attributes.clear();

                // Name block
                chandler.startElement("", "name", "name", attributes);
                char[] name = sni.getTitle().toCharArray();
                chandler.characters(name,0,name.length);
                chandler.endElement("", "name", "name");

                // Elements element - beginning
                chandler.startElement("", "elements", "elements", attributes);

                for(SnippetElement se : sni.getSnippetElements()){
                    // Name block
                    attributes.addAttribute("", "", "author", "string", se.getAuthor());
                    attributes.addAttribute("", "", "description", "string", se.getDescription());
                    attributes.addAttribute("", "", "language", "string", se.getLanguageCode());
                    chandler.startElement("", "element", "element", attributes);
                    char[] element = se.getCode().toCharArray();
                    chandler.characters(element,0,element.length);
                    chandler.endElement("", "element", "element");
                    attributes.clear();
                }

                chandler.endElement("", "elements", "elements");

                chandler.endElement("", "snippet", "snippet");
                
            }
            
            chandler.endElement("", "snippetcollection", "snippetcollection");
            chandler.endDocument();
        }
        
        @Override
        public void parse(String systemId) throws IOException, SAXException {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
    }
    
    public boolean createSnippets(String path){
        XMLReader pread = new SnippetsReader();
        InputSource psource = new SnippetsSource(sniList);
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
    
    public void setSnippets(List<Snippet> sniList){
        this.sniList = sniList;
    }
    
}
