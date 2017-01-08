/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package feuille.karaoke;

//import java.io.BufferedReader;

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
import feuille.karaoke.lib.FxObject;

//import java.io.FileInputStream;
//import java.io.FileReader;
//import java.io.InputStreamReader;
//import java.util.ArrayList;
//import java.util.List;
//import org.xml.sax.Attributes;
//import org.xml.sax.InputSource;
//import org.xml.sax.XMLReader;
//import org.xml.sax.helpers.XMLReaderFactory;

/**
 * <p>This class is a XML file reader (Open XFX file).<br />
 * Cette classe est un lecteur de fichier XML (Ouvre les XFX).</p>
 * @author The Wingate 2940
 */
//public class XmlPresetHandler extends org.xml.sax.helpers.DefaultHandler {
//    
//    private String currentName = "";
//    private String currentType = "";
//    private String currentMoment = "";
//    private String currentCommands = "";
//    private String currentTime = "";
//    private String currentImage = "";
//    private String currentAuthor = "";
//    private String currentComment = "";
//    private String currentLayer = "";
//    private String currentStyle = "";
//    private String currentCollect = "";
//    private String currentRuby = "";
//    private static String currentFilePath = "";
//    private ScannedElement currentScannedElement = ScannedElement.Nothing;
//    
//    private static String currentChars = "";
//    private static List<FxObject> lfxo = new ArrayList<FxObject>();
//    
//    /** <p>A choice of field of the XML.<br />
//     * Un choix de champ du XML.</p> */
//    public enum ScannedElement{
//        Nothing("nothing"), Name("name"), Type("type"), Moment("moment"),
//        Commands("commands"), Time("time"), Image("image"), Author("author"),
//        Comment("comment"), Layer("layer"), Style("style"), Collect("collect"),
//        Ruby("ruby"), FilePath("filepath");
//        
//        private String se;
//        
//        ScannedElement(String se){
//            this.se = se;
//        }
//        
//        public String getScannedType(){
//            return se;
//        }
//        
//    }
//    
//    /** <p>Create a new XmlPresetHandler.<br />
//     * Crée un nouveau XmlPresetHandler.</p> */
//    public XmlPresetHandler(){
//        super();
//    }
//    
//    // <editor-fold defaultstate="collapsed" desc=" XML Methods ">
//    
//    public static void main (String args[])
//	throws Exception
//    {
//	XMLReader xr = XMLReaderFactory.createXMLReader();
//	XmlPresetHandler handler = new XmlPresetHandler();
//	xr.setContentHandler(handler);
//	xr.setErrorHandler(handler);
//
//        // Parse each file provided on the
//        // command line.
//	for (int i = 0; i < args.length; i++) {
//	    FileReader r = new FileReader(args[i]);
//	    xr.parse(new InputSource(r));
//	}
//    }
//    
//    /** <p>Read the XML file and return a table of FxObject.<br />
//     * Lit le fichier XML et renvoie un tableau de FxObject.</p>
//     * @param path The absolute path of the XML file. */
//    public static FxObject[] startProcess(String path) throws Exception{
//        // Reset list (because this is a static method)
//        lfxo = new ArrayList<FxObject>();
//
//        // Start treatment
//        XMLReader xr = XMLReaderFactory.createXMLReader();
//	XmlPresetHandler handler = new XmlPresetHandler();
//	xr.setContentHandler(handler);
//	xr.setErrorHandler(handler);
//        
//        currentFilePath = path;
//        
//        FileInputStream fis = new FileInputStream(path);
//        BufferedReader br = new BufferedReader(
//                    new InputStreamReader(fis, "UTF-8"));
//        xr.parse(new InputSource(br));
//        return lfxo.toArray(new FxObject[0]);
//    }
//    
//    /** <p>When the reader find the marker of the start of the XML then 
//     * this method is called. We can do something if we want. 
//     * I do nothing personally.<br />Quand le lecteur trouve la balise de début 
//     * du fichier XML alors cette méthode est appelée. Nous pouvons y faire 
//     * quelque chose si nous voulons. Personnellement je ne fais rien.</p> */
//    @Override
//    public void startDocument(){
////	System.out.println("Start document");
//    }
//
//    /** <p>When the reader find the marker of the end of the XML then 
//     * this method is called. We can do something if we want. 
//     * I do nothing personally.<br />Quand le lecteur trouve la balise de fin 
//     * du fichier XML alors cette méthode est appelée. Nous pouvons y faire 
//     * quelque chose si nous voulons. Personnellement je ne fais rien.</p> */
//    @Override
//    public void endDocument(){
////	System.out.println("End document");
//    }
//
//    /** <p>When the reader find the marker of the start of an element then 
//     * this method is called. We can do something if we want. 
//     * I keep a value of the recent scanned element.<br />
//     * Quand le lecteur trouve la balise de début d'élément alors cette 
//     * méthode est appelée. Nous pouvons y faire quelque chose si nous voulons. 
//     * Je garde une valeur qui est l'élément scanné.</p> */
//    @Override
//    public void startElement(String uri, String name,
//			      String qName, Attributes atts){
////	if ("".equals (uri))
////	    System.out.println("Start element: " + qName);
////	else
////	    System.out.println("Start element: {" + uri + "}" + name);
//        
//        currentScannedElement = setScannedType(qName);
//    }
//
//    /** <p>When the reader find the marker of the end of an element then 
//     * this method is called. We can do something if we want. 
//     * I create a FxObject and put it in a list or just keep its data
//     * to variable.<br />
//     * Quand le lecteur trouve la balise de fin d'élément alors cette 
//     * méthode est appelée. Nous pouvons y faire quelque chose si nous voulons. 
//     * Je crée un FxObject et le met dans une liste ou je garde juste ses
//     * données dans une variable.</p> */
//    @Override
//    public void endElement(String uri, String name, String qName){
////	if ("".equals (uri))
////	    System.out.println("End element: " + qName);
////	else
////	    System.out.println("End element:   {" + uri + "}" + name);
//        
//        if(qName.equals("effect")){
//            FxObject fxo = new FxObject(currentName, currentComment,
//            currentFilePath, currentType);
//            fxo.setFxObjectType(currentFilePath);
//            fxo.setAuthor(currentAuthor);
//            fxo.setFirstLayer(currentLayer);
//            fxo.setNbLayers("");
//            fxo.setVersion("");
//            fxo.setCollect(currentCollect);
//            fxo.setCommands(currentCommands);
//            fxo.setImage(currentImage);
//            fxo.setMoment(currentMoment);
//            fxo.setStyle(currentStyle);
//            fxo.setTime(currentTime);
//            fxo.setRubyCode(currentRuby);
//            lfxo.add(fxo);
//            resetCurrentData();
//        }else if(!qName.equals("effect") & !qName.equals("sheet")){
//            setCurrentData(currentScannedElement, formatString(currentChars));
//            currentChars = "";
//            currentScannedElement = setScannedType("nothing");
//        }
//    }
//    
//    /** <p>Read a character. I create a new string with it.<br />
//     * Lit un caracter. Je crée une nouvelle chaine avec.</p> */
//    @Override
//    public void characters(char ch[], int start, int length){
////	System.out.print("Characters:    \"");
//	for (int i = start; i < start + length; i++) {
//	    switch (ch[i]) {
//	    case '\\':
////		System.out.print("\\\\");
//        currentChars += ch[i];
//		break;
//	    case '"':
////		System.out.print("\\\"");
//		break;
//	    case '\n':
////		System.out.print("\\n");
//		break;
//	    case '\r':
////		System.out.print("\\r");
//		break;
//	    case '\t':
////		System.out.print("\\t");
//		break;
//	    default:
////		System.out.print(ch[i]);
//        currentChars += ch[i];
//		break;
//	    }
//	}
////	System.out.print("\"\n");
//    }
//    
//    // </editor-fold>
//    
//    /** <p>Reset variables.<br />Efface les variables.</p> */
//    public void resetCurrentData(){
//        currentName = "";
//        currentType = "";
//        currentMoment = "";
//        currentCommands = "";
//        currentTime = "";
//        currentImage = "";
//        currentAuthor = "";
//        currentComment = "";
//        currentLayer = "";
//        currentStyle = "";
//        currentCollect = "";
//        currentRuby = "";
//        currentScannedElement = ScannedElement.Nothing;
//        currentChars = "";
//    }
//    
//    /** <p>Fill in a variable.<br />Remplit une variable.</p> */
//    public void setCurrentData(ScannedElement se, String data){
//        switch(se){
//            case Name: currentName = data;
//            case Type: currentType = data;
//            case Moment: currentMoment = data;
//            case Commands: currentCommands = data;
//            case Time: currentTime = data;
//            case Image: currentImage = data;
//            case Author: currentAuthor = data;
//            case Comment: currentComment = data;
//            case Layer: currentLayer = data;
//            case Style: currentStyle = data;
//            case Collect: currentCollect = data;
//            case Ruby: currentRuby = data;
//        }
//    }
//    
//    /** <p>Get a variable value.<br />Obtient la valeur d'un variable.</p> */
//    public String getCurrentData(ScannedElement se){
//        switch(se){
//            case Name: return currentName;
//            case Type: return currentType;
//            case Moment: return currentMoment;
//            case Commands: return currentCommands;
//            case Time: return currentTime;
//            case Image: return currentImage;
//            case Author: return currentAuthor;
//            case Comment: return currentComment;
//            case Layer: return currentLayer;
//            case Style: return currentStyle;
//            case Collect: return currentCollect;
//            case Ruby: return currentRuby;
//            default: return "";
//        }
//    }
//    
//    /** <p>Return the type of the scanned element.<br />
//     * Retourne le type de l'élément scanné.</p> */
//    public ScannedElement setScannedType(String wse){
//        for(ScannedElement sex : ScannedElement.values()){
//            if(sex.se.equals(wse)){return sex;}
//        }
//        return ScannedElement.Nothing;
//    }
//    
//    /** <p>Just reformat a little a string by removing spaces and lines return.
//     * <br />Reformete juste un peu une chaine en enlevant les espaces et retours à la ligne.</p> */
//    public String formatString(String s){
//        if(s.startsWith("\\n")){s = "";}
//        while(s.startsWith(" ")){s = s.substring(1);}
//        return s;
//    }
//
//}

//Re-writing the class

public class XmlPresetHandler {
    
    PresetHandler ph;
    File f;
    
    public XmlPresetHandler(String path) 
            throws ParserConfigurationException, SAXException, IOException{
        SAXParserFactory fabrique = SAXParserFactory.newInstance();
        SAXParser parseur = fabrique.newSAXParser();

        File fichier = new File(path); f = fichier;
        ph = new PresetHandler();
        parseur.parse(fichier, ph);
    }
    
    public List<FxObject> getFxObjectList(){
        return ph.getFxObjectList();
    }
    
    public class PresetHandler extends DefaultHandler{
	//résultats de notre parsing
	private List<FxObject> lfxo;
	private FxObject fxo;
	//flags nous indiquant la position du parseur
	private boolean inSheet, inPreset, inName, inMoment, inCommands,
                inTime, inImage, inAuthors, inComment, inLayer, inStyle,
                inCollect, inType, inRubyCode;
	//buffer nous permettant de récupérer les données 
	private StringBuffer buffer;

	// simple constructeur
	public PresetHandler(){
            super();
	}
        
        public List<FxObject> getFxObjectList(){
            return lfxo;
        }
        
	//détection d'ouverture de balise
        @Override
	public void startElement(String uri, String localName,
			String qName, Attributes attributes) throws SAXException{
            if(qName.equals("sheet")){
                lfxo = new LinkedList<FxObject>();
                inSheet = true;
            }else if(qName.equals("effect")){
                fxo = new FxObject();
                inPreset = true;
            }else {
                buffer = new StringBuffer();
                if(qName.equals("name")){
                        inName = true;
                }else if(qName.equals("type")){
                        inType = true;
                }else if(qName.equals("moment")){
                        inMoment = true;
                }else if(qName.equals("commands")){
                        inCommands = true;
                }else if(qName.equals("time")){
                        inTime = true;
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
            }else if(qName.equals("effect")){
                    lfxo.add(fxo);
                    fxo = null;
                    inPreset = false;
            }else if(qName.equals("name")){
                    fxo.setName(buffer.toString());
                    buffer = null;
                    inName = false;
            }else if(qName.equals("type")){
                    fxo.setFxObjectType(f.getAbsolutePath());
                    fxo.setScriptPathname(f.getAbsolutePath());
                    fxo.setFunction(buffer.toString());
                    buffer = null;
                    inType = false;
            }else if(qName.equals("moment")){
                    fxo.setMoment(buffer.toString());
                    buffer = null;
                    inMoment = false;
            }else if(qName.equals("commands")){
                    fxo.setCommands(buffer.toString());
                    buffer = null;
                    inCommands = false;
            }else if(qName.equals("time")){
                    fxo.setTime(buffer.toString());
                    buffer = null;
                    inTime = false;
            }else if(qName.equals("image")){
                    fxo.setImage(buffer.toString());
                    buffer = null;
                    inImage = false;
            }else if(qName.equals("authors")){
                    fxo.setAuthor(buffer.toString());
                    buffer = null;
                    inAuthors= false;
            }else if(qName.equals("comment")){
                    fxo.setDescription(buffer.toString());
                    buffer = null;
                    inComment = false;
            }else if(qName.equals("layer")){
                    fxo.setFirstLayer(buffer.toString());
                    buffer = null;
                    inLayer = false;
            }else if(qName.equals("style")){
                    fxo.setStyle(buffer.toString());
                    buffer = null;
                    inStyle = false;
            }else if(qName.equals("collect")){
                    fxo.setCollect(buffer.toString());
                    buffer = null;
                    inCollect = false;
            }else if(qName.equals("ruby")){
                    fxo.setRubyCode(buffer.toString());
                    buffer = null;
                    inRubyCode = false;
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
//            for(FxObject f : lfxo){
//                    System.out.println(f);
//            }
	}
    }
}