package feuille.io;

import feuille.module.editor.assa.ASS;
import feuille.module.editor.assa.AssActor;
import feuille.module.editor.assa.AssEffect;
import feuille.module.editor.assa.AssStyle;
import feuille.module.editor.voyager.Bookmark;
import feuille.module.editor.voyager.Voyager;
import org.xml.sax.*;
import org.xml.sax.helpers.AttributesImpl;

import javax.xml.transform.*;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlVoyagersWriter {

    /*
     Structure :
     <voyagers>
        <info>
            <video src= width= height= />
            <audio src= />
        </info>
        <styles>
            <style name= >
                raw ass line for the style
            </style>
        </styles>
        <events>
            <event type= layer= start= end= style= actor= ml= mr= mv= effects= >
                <iso lang= text= />
                    <bookmark type= content= link= />
                </iso>
            </event>
        </events>
        <actors>
            <actor name= >
                bitmap
            </actor>
        </actors>
        <effects>
            <effect name= language= >
                content
            </effect>
        </effects>
     </voyagers>
     */

    private final List<Voyager> voyagers;
    private final ASS ass;

    public XmlVoyagersWriter(List<Voyager> voyagers, ASS ass){
        this.voyagers = voyagers;
        this.ass = ass;
    }

    public static class VoyagerSource extends InputSource {
        private final List<Voyager> voyagers;
        private final ASS ass;

        public VoyagerSource(List<Voyager> voyagers, ASS ass){
            super();
            this.voyagers = voyagers;
            this.ass = ass;
        }

        public List<Voyager> getVoyagers() {
            return voyagers;
        }

        public ASS getAss() {
            return ass;
        }
    }

    public static class VoyagerReader implements XMLReader{

        private ContentHandler contentHandler;
        private final AttributesImpl attributes = new AttributesImpl();
        private final Map<String,Boolean> features = new HashMap<>();
        private final Map<String,Object> properties = new HashMap<>();
        private EntityResolver resolver;
        private DTDHandler dtdHandler;
        private ErrorHandler errorHandler;

        @Override
        public boolean getFeature(String name) {
            return features.get(name);
        }

        @Override
        public void setFeature(String name, boolean value) {
            try{
                features.put(name, value);
            }catch(Exception _){
            }
        }

        @Override
        public Object getProperty(String name) {
            return properties.get(name);
        }

        @Override
        public void setProperty(String name, Object value) {
            try{
                properties.put(name, value);
            }catch(Exception _){
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
            this.dtdHandler = handler;
        }

        @Override
        public DTDHandler getDTDHandler() {
            return dtdHandler;
        }

        @Override
        public void setContentHandler(ContentHandler handler) {
            this.contentHandler = handler;
        }

        @Override
        public ContentHandler getContentHandler() {
            return contentHandler;
        }

        @Override
        public void setErrorHandler(ErrorHandler handler) {
            this.errorHandler = handler;
        }

        @Override
        public ErrorHandler getErrorHandler() {
            return errorHandler;
        }

        @Override
        public void parse(InputSource input) throws IOException, SAXException {

            if(!(input instanceof VoyagerSource source)){
                throw new SAXException("The object isn't a VoyagerSource");
            }
            if(contentHandler == null){
                throw new SAXException("ContentHandler not defined");
            }

            List<Voyager> voyagers = source.getVoyagers();
            ASS ass = source.getAss();

            // Start the document with 'voyagers'
            // <voyagers>
            startDocument();

            // Entering 'info' loop
            // <info>
            startElement("info");
            //TODO loop info
            // <video src= width= height= />
            // <audio src= />
            // Closing 'info' loop
            // </info>
            endElement("info");

            // Entering 'styles' loop
            // <styles>
            startElement("styles");
            for(AssStyle style : ass.getStyles()){
                // A new 'style' tag
                // <style name= >
                startElement("style", new AttributesSetter("name", style.getName()));
                // Content of Style
                putText(style.toRawLine());
                // A release of this 'style' tag
                // </style>
                endElement("style");
            }
            // Closing 'styles' loop
            // </styles>
            endElement("styles");

            // Entering 'events' loop
            // <events>
            startElement("events");
            for(Voyager voyager : voyagers){
                // A new 'event' tag
                // <event type= layer= start= end= style= actor= ml= mr= mv= effects= >
                startElement("event",
                        new AttributesSetter("type", voyager.getEvent().getType().getName()),
                        new AttributesSetter("layer", Integer.toString(voyager.getEvent().getLayer())),
                        new AttributesSetter("start", voyager.getEvent().getStart().toAss()),
                        new AttributesSetter("end", voyager.getEvent().getEnd().toAss()),
                        new AttributesSetter("style", voyager.getEvent().getStyle().getName()),
                        new AttributesSetter("actor", voyager.getEvent().getName().getName()),
                        new AttributesSetter("ml", Integer.toString(voyager.getEvent().getMarginL())),
                        new AttributesSetter("mr", Integer.toString(voyager.getEvent().getMarginR())),
                        new AttributesSetter("mv", Integer.toString(voyager.getEvent().getMarginV())),
                        new AttributesSetter("effects", voyager.getEvent().getEffect().getName())
                );
                // Entering 'iso' loop
                for(Voyager v : voyager.getVoyagers()){
                    // <iso lang= text= >
                    startElement("iso",
                            new AttributesSetter("lang", v.getLanguage().getAlpha3()),
                            new AttributesSetter("text", v.getEvent().getText())
                    );
                    for(Bookmark b : v.getBookmarks()){
                        // <bookmark type= content= link= />
                        startElement("bookmark",
                                new AttributesSetter("name", b.getName()),
                                new AttributesSetter("type", ""),
                                new AttributesSetter("content", ""),
                                new AttributesSetter("link", "")
                        );
                    }
                    // </iso>
                    endElement("iso");
                }
                // A release of this 'style' tag
                endElement("event");
            }
            // Closing 'events' loop
            // </events>
            endElement("events");

            // Entering 'actors' tag
            // <actors>
            startElement("actors");
            for(AssActor a : ass.getActors()){
                // <actor name= >
                startElement("actor",
                        new AttributesSetter("name", a.getName())
                );
                // bitmap
                putText(a.getB64Image());
                // </actor>
                endElement("actor");
            }
            // Closing 'actors' tag
            // </actors>
            endElement("actors");

            // Entering 'effects' tag
            startElement("effects");
            for(AssEffect e : ass.getEffects()){
                // <effect name= language= >
                startElement("effect",
                        new AttributesSetter("name", e.getName()),
                        new AttributesSetter("language", "lua")
                );
                // effect code
                putText(e.getEffect());
                // </effect>
                endElement("effect");
            }
            // Closing 'effects' tag
            endElement("effects");

            // End the document with 'voyagers'
            endDocument();
        }

        @Override
        public void parse(String systemId) {
            throw new UnsupportedOperationException("Not supported yet.");
        }



        private void startDocument() throws SAXException {
            contentHandler.startDocument();
            contentHandler.startElement("", "voyagers", "voyagers", attributes);
        }

        private void endDocument() throws SAXException {
            contentHandler.endElement("", "voyagers", "voyagers");
            contentHandler.endDocument();
        }

        private void startElement(String tag, AttributesSetter... attributesSetters) throws SAXException {
            attributes.clear();

            for(AttributesSetter attributesSetter : attributesSetters){
                attributes.addAttribute("", attributesSetter.getName(), attributesSetter.getName(), "CDATA", attributesSetter.getValue());
            }

            contentHandler.startElement("", tag, tag, attributes);
        }

        private void endElement(String tag) throws SAXException {
            contentHandler.endElement("", tag, tag);
        }

        private void putText(String text) throws SAXException {
            char[] key = text.toCharArray();
            contentHandler.characters(key, 0, key.length);
        }

        public static class AttributesSetter {
            private final String name;
            private final String value;

            public AttributesSetter(String name, String value) {
                this.name = name;
                this.value = value;
            }

            public String getName() {
                return name;
            }

            public String getValue() {
                return value;
            }
        }

    }

    public boolean createXML(String path){
        XMLReader reader = new VoyagerReader();
        InputSource inputSource = new VoyagerSource(voyagers, ass);
        Source source = new SAXSource(reader, inputSource);

        File file = new File(path);
        Result res = new StreamResult(file);

        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer;
            transformer = factory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.transform(source, res);
        } catch (TransformerException ex) {
            return false;
        }

        return true;
    }
}
