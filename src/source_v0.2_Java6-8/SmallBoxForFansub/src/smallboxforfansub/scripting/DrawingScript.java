/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smallboxforfansub.scripting;

/**
 *
 * @author The Wingate 2940
 */
public class DrawingScript {
    
    private String name = "";                                   // The effect's name. 
    private String description = "";                            // Its description.
    private String version = "";                                // Its version.
    private String author = "";                                 // Its author.
    private String scriptPathname = "";                         // File to read.
    private String function = "";                               // Function to load.
    private CodeType ct = CodeType.Unknown;                    // Langage du script.
    
    public DrawingScript(){
        
    }
    
    public DrawingScript(String name, String description, String version,
            String author, String scriptpathname, String function){
        this.name = name;
        this.description = description;
        this.version = version;
        this.author = author;
        this.scriptPathname = scriptpathname;
        this.function = function;
        if(scriptpathname.endsWith(".rb")){
            ct = CodeType.JRuby;
        }else if(scriptpathname.endsWith(".py")){
            ct = CodeType.Jython;
        }
    }
    
    public enum CodeType{
        Unknown, JRuby, Jython;
    }
    
    public void setScript(String name, String description, String version,
            String author, String scriptpathname, String function){
        this.name = name;
        this.description = description;
        this.version = version;
        this.author = author;
        this.scriptPathname = scriptpathname;
        this.function = function;
        if(scriptpathname.endsWith(".rb")){
            ct = CodeType.JRuby;
        }else if(scriptpathname.endsWith(".py")){
            ct = CodeType.Jython;
        }
    }
    
    @Override
    public String toString(){
        return name;
    }
    
    public void setCodeType(CodeType ct){
        this.ct = ct;
    }
    
    public CodeType getCodeType(){
        return ct;
    }
    
    /** <p>Set the name of this FxObject.<br />
     * Définit le nom de ce FxObject.</p> */
    public void setName(String name){
        this.name = name;
    }
    
    /** <p>Get the name of this FxObject.<br />
     * Obtient le nom de ce FxObject.</p> */
    public String getName(){
        return name;
    }
    
    /** <p>Set the description of this FxObject.<br />
     * Définit la description de ce FxObject.</p> */
    public void setDescription(String description){
        this.description = description;
    }
    
    /** <p>Get the description of this FxObject.<br />
     * Obtient la description de ce FxObject.</p> */
    public String getDescription(){
        return description;
    }
    
    /** <p>Set the full pathname of the script which contains the effects.<br />
     * Définit le chemin du script contenant les effets.</p> */
    public void setScriptPathname(String scriptPathname){
        this.scriptPathname = scriptPathname;
    }
    
    /** <p>Get the full pathname of the script which contains the effects.<br />
     * Obtient le chemin du script contenant les effets.</p> */
    public String getScriptPathname(){
        return scriptPathname;
    }
    
    /** <p>Set the function to load.<br />
     * Définit la fonction à lancer.</p> */
    public void setFunction(String function){
        this.function = function;
    }
    
    /** <p>Get the function to load.<br />
     * Obtient la fonction à lancer.</p> */
    public String getFunction(){
        return function;
    }
    
    /** <p>Set the version.<br />
     * Définit la version.</p> */
    public void setVersion(String version){
        this.version = version;
    }
    
    /** <p>Get the version.<br />
     * Obtient la version.</p> */
    public String getVersion(){
        return version;
    }
    
    /** <p>Set the author.<br />
     * Définit l'auteur.</p> */
    public void setAuthor(String author){
        this.author = author;
    }
    
    /** <p>Get the author.<br />
     * Obtient l'auteur.</p> */
    public String getAuthor(){
        return author;
    }
    
}
