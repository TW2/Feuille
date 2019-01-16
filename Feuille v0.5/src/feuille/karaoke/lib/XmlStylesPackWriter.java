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
 * <p>This class is a XML file writer (Save StylesPack file).<br />
 * Cette classe est un enregistreur de fichier XML (Sauvegarde les fichiers StylesPack).</p>
 * @author The Wingate 2940
 */
public class XmlStylesPackWriter {

    // Storage of StylesPack
    List<StylesPack> lsp = new ArrayList<StylesPack>();

    /** <p>Create a new XmlStylesPackWriter.<br />
     * Crée un nouveau XmlStylesPackWriter.</p> */
    public XmlStylesPackWriter(){

    }

    /** <p>Create a new XML file.<br />Crée un nouveau fichier XML.</p> */
    public boolean createStylesPack(String path){
        org.xml.sax.XMLReader pread = new XmlStylesPackReader();
        InputSource psource = new XmlStylesPackSource(lsp);
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
            ex.printStackTrace();
            return false;
        } catch (TransformerException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    /** <p>Set a new list of StylesPack.<br />
     * Définit une nouvelle liste StylesPack.</p> */
    public void setStylesPackList(List<StylesPack> lsp){
        this.lsp = lsp;
    }

    /** <p>Container of StylesPack.<br />Conteneur de StylesPack.</p> */
    public class XmlStylesPackSource extends org.xml.sax.InputSource{
        // Storage of StylesPack
        List<StylesPack> lsp = new ArrayList<StylesPack>();

        /** <p>Create a new XmlStylesPackSource.<br />
         * Crée un nouveau XmlStylesPackSource.</p> */
        public XmlStylesPackSource(List<StylesPack> lsp){
            super();
            this.lsp = lsp;
        }

        /** <p>Get a list of StylesPack.<br />
         * Obtient une liste de StylesPack.</p> */
        public List<StylesPack> getStylesPackList(){
            return lsp;
        }
    }

    /** <p>The way to read an XML of StylesPack.<br />
     * Comment lire un XML de StylesPack.</p> */
    public class XmlStylesPackReader implements org.xml.sax.XMLReader{

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
        @SuppressWarnings("BooleanConstructorCall")
        public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
            try{
                features.put(name, new Boolean(value));
            }catch(Exception ex){
                ex.printStackTrace();
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
                ex.printStackTrace();
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
            if(!(input instanceof XmlStylesPackSource)){
                throw new SAXException("The object isn't a XmlStylesPackSource");
            }
            if(chandler == null){
                throw new SAXException("ContentHandler not defined");
            }

            XmlStylesPackSource source = (XmlStylesPackSource)input;
            List<StylesPack> lsp = source.getStylesPackList();

            // Main element - beginning
            chandler.startDocument();
            chandler.startElement("", "root", "root", attributes);

            // StylesPack element
            for(StylesPack sp : lsp){

                // StylesPack element - beginning
                chandler.startElement("", "pack", "pack", attributes);

                // Name block
                chandler.startElement("", "name", "name", attributes);
                char[] name = sp.getPack().toCharArray();
                chandler.characters(name,0,name.length);
                chandler.endElement("", "name", "name");

                // Styles block
                String fillin = "";
                List<AssStyle> las = new ArrayList<AssStyle>();
                AssStyleCollection asc = sp.getCollection();
                for(AssStyle as : asc.getMembers()){
                    as.setName(as.getName().trim());
                    las.add(as);
                }
                fillin = AssStyle.linkAssStyles(las);
                chandler.startElement("", "styles", "styles", attributes);
                char[] styles = fillin.toCharArray();
                chandler.characters(styles,0,styles.length);
                chandler.endElement("", "styles", "styles");

                // StylesPack element - end
                chandler.endElement("", "pack", "pack");
            }

            // Main element - end
            chandler.endElement("", "root", "root");
            chandler.endDocument();
        }

        @Override
        public void parse(String systemId) throws IOException, SAXException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

    }
}
