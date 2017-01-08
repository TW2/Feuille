/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.lib;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
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
public class XmlLangWriter {
    
    Map<String, String> myMap = new HashMap<String, String>();
    
    public XmlLangWriter(){
        
    }
    
    public class LangSource extends InputSource{
        
        Map<String, String> myMap = new HashMap<String, String>();
        
        public LangSource(Map<String, String> myMap){
            super();
            this.myMap = myMap;
        }
        
        public Map<String, String> getLangMap(){
            return myMap;
        }
    }
    
    public class LangReader implements XMLReader{

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
            
            if(!(input instanceof LangSource)){
                throw new SAXException("The object isn't a LangSource");
            }
            if(chandler == null){
                throw new SAXException("ContentHandler not defined");
            }
            
            LangSource source = (LangSource)input;
            Map<String, String> myMap = source.getLangMap();
            
            chandler.startDocument();
            chandler.startElement("", "sheet", "sheet", attributes);
            
            for(String s : myMap.keySet()){
                
                chandler.startElement("", "lang", "lang", attributes);
                
                chandler.startElement("", "key", "key", attributes);
                char[] key = s.toCharArray();
                chandler.characters(key,0,key.length);
                chandler.endElement("", "key", "key");
                
                chandler.startElement("", "value", "value", attributes);
                char[] value = myMap.get(s).toCharArray();
                chandler.characters(value,0,value.length);
                chandler.endElement("", "value", "value");
                
                chandler.endElement("", "lang", "lang");
                
            }
            
            chandler.endElement("", "sheet", "sheet");
            chandler.endDocument();
        }

        @Override
        public void parse(String systemId) throws IOException, SAXException {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
    }
    
    public boolean createLang(String path){
        XMLReader pread = new LangReader();
        InputSource psource = new LangSource(myMap);
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

    public void setLangMap(Map<String, String> myMap){
        this.myMap = myMap;
    }
}
