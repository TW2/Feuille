/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package feuille.karaoke.lib;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * <p>This class is a XML file reader (Open Styles pack file).<br />
 * Cette classe est un lecteur de fichier XML (Ouvre les paquets styles).</p>
 * @author The Wingate 2940
 */
public class XmlStylesPackHandler extends org.xml.sax.helpers.DefaultHandler {
    
    private String currentName = "";
    private String currentStyles = "";
    private static String currentFilePath = "";
    private ScannedElement currentScannedElement = ScannedElement.Nothing;
    
    private static String currentChars = "";
    private static List<StylesPack> lsp = new ArrayList<StylesPack>();
    
    /** <p>A choice of field of the XML.<br />
     * Un choix de champ du XML.</p> */
    public enum ScannedElement{
        Nothing("nothing"), Name("name"), Styles("styles"),
        FilePath("filepath");
        
        private String se;
        
        ScannedElement(String se){
            this.se = se;
        }
        
        public String getScannedType(){
            return se;
        }
        
    }
    
    /** <p>Create a new XmlStylesPackHandler.<br />
     * Crée un nouveau XmlStylesPackHandler.</p> */
    public XmlStylesPackHandler(){
        super();
    }
    
    // <editor-fold defaultstate="collapsed" desc=" XML Methods ">
    
    public static void main (String args[])
	throws Exception
    {
	XMLReader xr = XMLReaderFactory.createXMLReader();
	XmlStylesPackHandler handler = new XmlStylesPackHandler();
	xr.setContentHandler(handler);
	xr.setErrorHandler(handler);

        // Parse each file provided on the
        // command line.
	for (int i = 0; i < args.length; i++) {
	    FileReader r = new FileReader(args[i]);
	    xr.parse(new InputSource(r));
	}
    }
    
    /** <p>Read the XML file and return a list of StylesPack.<br />
     * Lit le fichier XML et renvoie une liste de StylesPack.</p>
     * @param path The absolute path of the XML file. */
    public static List<StylesPack> startProcess(String path) throws Exception{
        // Reset list (because this is a static method)
        lsp = new ArrayList<StylesPack>();

        // Start treatment
        XMLReader xr = XMLReaderFactory.createXMLReader();
	XmlStylesPackHandler handler = new XmlStylesPackHandler();
	xr.setContentHandler(handler);
	xr.setErrorHandler(handler);
        
        currentFilePath = path;
        
        FileInputStream fis = new FileInputStream(path);
        BufferedReader br = new BufferedReader(
                    new InputStreamReader(fis, "UTF-8"));
        xr.parse(new InputSource(br));
        return lsp;
    }
    
    /** <p>When the reader find the marker of the start of the XML then 
     * this method is called. We can do something if we want. 
     * I do nothing personally.<br />Quand le lecteur trouve la balise de début 
     * du fichier XML alors cette méthode est appelée. Nous pouvons y faire 
     * quelque chose si nous voulons. Personnellement je ne fais rien.</p> */
    @Override
    public void startDocument(){
//	System.out.println("Start document");
    }

    /** <p>When the reader find the marker of the end of the XML then 
     * this method is called. We can do something if we want. 
     * I do nothing personally.<br />Quand le lecteur trouve la balise de fin 
     * du fichier XML alors cette méthode est appelée. Nous pouvons y faire 
     * quelque chose si nous voulons. Personnellement je ne fais rien.</p> */
    @Override
    public void endDocument(){
//	System.out.println("End document");
    }

    /** <p>When the reader find the marker of the start of an element then 
     * this method is called. We can do something if we want. 
     * I keep a value of the recent scanned element.<br />
     * Quand le lecteur trouve la balise de début d'élément alors cette 
     * méthode est appelée. Nous pouvons y faire quelque chose si nous voulons. 
     * Je garde une valeur qui est l'élément scanné.</p> */
    @Override
    public void startElement(String uri, String name,
			      String qName, Attributes atts){
//	if ("".equals (uri))
//	    System.out.println("Start element: " + qName);
//	else
//	    System.out.println("Start element: {" + uri + "}" + name);
        
        currentScannedElement = setScannedType(qName);
    }

    /** <p>When the reader find the marker of the end of an element then 
     * this method is called. We can do something if we want. 
     * I fill in a list or just keep data to a variable.<br />
     * Quand le lecteur trouve la balise de fin d'élément alors cette 
     * méthode est appelée. Nous pouvons y faire quelque chose si nous voulons. 
     * Je remplis une liste ou je garde juste des données dans une variable.</p> */
    @Override
    public void endElement(String uri, String name, String qName){
//	if ("".equals (uri))
//	    System.out.println("End element: " + qName);
//	else
//	    System.out.println("End element:   {" + uri + "}" + name);
        
        if(qName.equals("pack")){
            List<AssStyle> las = AssStyle.unlinkAssStyles(currentStyles);
            AssStyleCollection asc = new AssStyleCollection();
            for(AssStyle as : las){
                as.setName(as.getName().trim());
                asc.addMember(as.getName(), as);
            }
            StylesPack sp = new StylesPack(currentName, asc);
            lsp.add(sp);
            resetCurrentData();
        }else if(!qName.equals("pack") & !qName.equals("root")){
            setCurrentData(currentScannedElement, formatString(currentChars));
            currentChars = "";
            currentScannedElement = setScannedType("nothing");
        }
    }
    
    /** <p>Read a character. I create a new string with it.<br />
     * Lit un caracter. Je crée une nouvelle chaine avec.</p> */
    @Override
    public void characters(char ch[], int start, int length){
//	System.out.print("Characters:    \"");
	for (int i = start; i < start + length; i++) {
	    switch (ch[i]) {
	    case '\\':
//		System.out.print("\\\\");
        currentChars += ch[i];
		break;
	    case '"':
//		System.out.print("\\\"");
		break;
	    case '\n':
//		System.out.print("\\n");
		break;
	    case '\r':
//		System.out.print("\\r");
		break;
	    case '\t':
//		System.out.print("\\t");
		break;
	    default:
//		System.out.print(ch[i]);
        currentChars += ch[i];
		break;
	    }
	}
//	System.out.print("\"\n");
    }
    
    // </editor-fold>
    
    /** <p>Reset variables.<br />Efface les variables.</p> */
    public void resetCurrentData(){
        currentName = "";
        currentStyles = "";
        currentScannedElement = ScannedElement.Nothing;
        currentChars = "";
    }
    
    /** <p>Fill in a variable.<br />Remplit une variable.</p> */
    public void setCurrentData(ScannedElement se, String data){
        switch(se){
            case Name: currentName = data;
            case Styles: currentStyles = data;
        }
    }
    
    /** <p>Get a variable value.<br />Obtient la valeur d'un variable.</p> */
    public String getCurrentData(ScannedElement se){
        switch(se){
            case Name: return currentName;
            case Styles: return currentStyles;
            default: return "";
        }
    }
    
    /** <p>Return the type of the scanned element.<br />
     * Retourne le type de l'élément scanné.</p> */
    public ScannedElement setScannedType(String wse){
        for(ScannedElement sex : ScannedElement.values()){
            if(sex.se.equals(wse)){return sex;}
        }
        return ScannedElement.Nothing;
    }
    
    /** <p>Just reformat a little a string by removing spaces and lines return.
     * <br />Reformete juste un peu une chaine en enlevant les espaces et retours à la ligne.</p> */
    public String formatString(String s){
        if(s.startsWith("\\n")){s = "";}
        while(s.startsWith(" ")){s = s.substring(1);}
        return s;
    }
    
}
