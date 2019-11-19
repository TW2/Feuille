/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package feuille.karaoke.lib;

/**
 * <p>This class is a storage for effects.<br />
 * Cette classe est un espace de stockage pour les effets.<br />
 * These effects can use 'internal methods + XML (XFX)' or 
 * an external 'ruby' script 
 * (and maybe an external 'JavaScript' script [not implemented]).
 * But I want to take advantage of JRuby to execute my/your 
 * script easily. Ruby is a very easy language and has a good 
 * integration in Java. So we can do Java/Ruby script.<br /><br />
 * A FxObject will be an element of the effects list and will be 
 * the link between AssFxMaker and your script. </p>
 * @author The WinGate 2940
 */
public class FxObject {
    
    FxObjectType fxoType = FxObjectType.Unknown;       // The effect's type. 
    String name = "";                                   // The effect's name. 
    String description = "";                            // Its description.
    String version = "";                                // Its version.
    String author = "";                                 // Its author.
    String nblayers = "";                               // Layers.
    String firstlayer = "";                             // The main layer.
    String scriptPathname = "";                         // File to read.
    String function = "";                               // Function to load.

    String moment = "";                                 // Moment of effect
    String commands = "";                               // Commands to use
    String time = "";                                   // Option of moment
    String image = "";                                  // Preview
    String style = "";                                  // Styles list
    String collect = "";                                // Collection
    String rubyCode = "";                               // Ruby code (variables)
    
    boolean inUse = false;                              // State in the list.

    FxObjectXmlFunc xmlfunc = FxObjectXmlFunc.None;
    
    
    // These are technologies which can be used by a FxObject
    /** <p>A choice of functionnality.<br />
     * Un choix de fonctionnalité.<br />
     * <table><tr><td width="100">Functionnality :</td><td>Actions :</td></tr>
     * <tr><td>XMLPreset</td><td>Allow the use of XFX.</td></tr>
     * <tr><td>Ruby</td><td>Allow the use of JRuby script.</td></tr>
     * <tr><td>Python</td><td>Allow the use of Jython script.</td></tr>
     * <tr><td>JavaScript</td><td>Not implemented.</td></tr>
     * <tr><td>Unknown</td><td>No effects.</td></tr></table></p> */
    public enum FxObjectType{
        XMLPreset, Ruby, JavaScript, Python, Unknown;
    }
    
    /** <p>Create a new FxObject.<br />
     * Crée un nouveau FxObject.</p> */
    public FxObject(){
        
    }
    
    /** <p>Create a new FxObject.<br />
     * Crée un nouveau FxObject.</p>
     * @param name The name of this object.
     * @param description A small descripttion.
     * @param scriptPathname The absolute path.
     * @param function The function to execute. */
    public FxObject(String name, String description,
            String scriptPathname, String function){
        this.name = name;
        this.description = description;
        this.scriptPathname = scriptPathname;
        this.function = function;
        setFxObjectType(scriptPathname);
    }
    
    // <editor-fold defaultstate="collapsed" desc=" Main methods ">
    
    /** <p>Set the FxObject type.<br />
     * Définit le type de FxObject.</p> */
    public void setFxObjectType(FxObjectType fxoType){
        this.fxoType = fxoType;
    }
    
    /** <p>Get the FxObject type.<br />
     * Obtient le type de FxObject.</p> */
    public FxObjectType getFxObjectType(){
        return fxoType;
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
    
    /** <p>Set the nblayers.<br />
     * Définit le nombre de couches.</p> */
    public void setNbLayers(String nblayers){
        this.nblayers = nblayers;
    }
    
    /** <p>Get the nblayers.<br />
     * Obtient le nombre de couches.</p> */
    public String getNbLayers(){
        return nblayers;
    }
    
    /** <p>Set the firstlayer.<br />
     * Définit le chiffre de la première couche.</p> */
    public void setFirstLayer(String firstlayer){
        this.firstlayer = firstlayer;
    }
    
    /** <p>Get the firstlayer.<br />
     * Obtient le chiffre de la première couche.</p> */
    public String getFirstLayer(){
        return firstlayer;
    }

    /** <p>Set the moment.<br />
     * Définit le moment.</p> */
    public void setMoment(String moment){
        this.moment = moment;
    }

    /** <p>Get the moment.<br />
     * Obtient le moment.</p> */
    public String getMoment(){
        return moment;
    }

    /** <p>Set the commands.<br />
     * Définit les commandes.</p> */
    public void setCommands(String commands){
        this.commands = commands;
    }

    /** <p>Get the commands.<br />
     * Obtient les commandes.</p> */
    public String getCommands(){
        return commands;
    }

    /** <p>Set the time.<br />
     * Définit le temps.</p> */
    public void setTime(String time){
        this.time = time;
    }

    /** <p>Get the time.<br />
     * Obtient le temps.</p> */
    public String getTime(){
        return time;
    }

    /** <p>Set the image.<br />
     * Définit l'image.</p> */
    public void setImage(String image){
        this.image = image;
    }

    /** <p>Get the image.<br />
     * Obtient l'image.</p> */
    public String getImage(){
        return image;
    }

    /** <p>Set the style.<br />
     * Définit le style.</p> */
    public void setStyle(String style){
        this.style = style;
    }

    /** <p>Get the style.<br />
     * Obtient le style.</p> */
    public String getStyle(){
        return style;
    }

    /** <p>Set the collection.<br />
     * Définit la collection.</p> */
    public void setCollect(String collect){
        this.collect = collect;
    }

    /** <p>Get the collection.<br />
     * Obtient la collection.</p> */
    public String getCollect(){
        return collect;
    }

    /** <p>Set the XFX function.<br />
     * Définit la fonction XFX.</p> */
    public void setXmlFunc(FxObjectXmlFunc xmlfunc){
        this.xmlfunc = xmlfunc;
    }
    
    /** <p>Get the XFX function.<br />
     * Obtient la fonction XFX.</p> */
    public FxObjectXmlFunc getXmlFunc(){
        return xmlfunc;
    }
    
    /** <p>Set the name of this FxObject.<br />
     * Définit le nom de ce FxObject.</p> */
    public void setRubyCode(String code){
        this.rubyCode = code;
    }
    
    /** <p>Get the name of this FxObject.<br />
     * Obtient le nom de ce FxObject.</p> */
    public String getRubyCode(){
        return rubyCode;
    }
    
    // </editor-fold>
    
    /** <p>Set the FxObject type.<br />
     * Définit le type de FxObject.</p> */
    public void setFxObjectType(String scriptPathname){
        if (scriptPathname.endsWith(".xml")){
            fxoType = FxObjectType.XMLPreset;
        }else if(scriptPathname.endsWith(".rb")){
            fxoType = FxObjectType.Ruby;
        }else if(scriptPathname.endsWith(".py")){
            fxoType = FxObjectType.Python;
        }else if(scriptPathname.endsWith(".js")){
            fxoType = FxObjectType.JavaScript;
        }else{
            fxoType = FxObjectType.Unknown;
        }
    }
    
    /** <p>Set the status of this FxObject.<br />
     * Définit l'état du FxObject.</p> */
    public void setStatus(boolean inUse){
        this.inUse = inUse;
    }
    
    /** <p>Get the status of this FxObject.<br />
     * Obtient l'état du FxObject.</p> */
    public boolean getStatus(){
        return inUse;
    }
    
    /** <p>Compare this FxObject with another.<br />
     * Compare ce FxObject à un autre.</p> */
    public boolean isTheSame(FxObject fxo){
        if(!fxoType.equals(fxo.getFxObjectType())){return false;}
        if(!name.equals(fxo.getName())){return false;}
        if(!description.equals(fxo.getDescription())){return false;}
        if(!version.equals(fxo.getVersion())){return false;}
        if(!author.equals(fxo.getAuthor())){return false;}
        if(!nblayers.equals(fxo.getNbLayers())){return false;}
        if(!firstlayer.equals(fxo.getFirstLayer())){return false;}
        if(!scriptPathname.equals(fxo.getScriptPathname())){return false;}
        if(!function.equals(fxo.getFunction())){return false;}
        return true;
    }

    /*
     * FUNCTIONS
     * --------
     * Line-Word-Basic (not implemented)
     * Line-Syllabe-Basic (mode normal, will be implemented)
     * Line-Syllabe-Period (assfxmaker specific, will be implemented)
     * Line-Syllabe-Random (assfxmaker specific, will be implemented)
     * Line-Syllabe-Symmetric (assfxmaker specific, will be implemented)
     * Line-Letter-Basic (will be implemented)
     * Syllabe-Syllabe-Basic (not implemented)
     * Syllabe-Syllabe-Fixed (assfxmaker specific, will be implemented)
     * Syllabe-Letter-Basic (not implemented)
     * Syllabe-Letter-Fixed (not implemented)
     * Letter-Letter-Basic (not implemented)
     * Letter-Letter-Fixed (not implemented)
     */
    /** <p>A choice of methods. (old, now replaced by plugins)<br />
     * Un choix de méthodes. (vieux, remplacé maintenant par les plugins)</p> */
     public enum FxObjectXmlFunc{
        LineWordBasic("LineWordBasic"),
        LineSyllabeBasic("LineSyllabeBasic"),
        LineSyllabePeriod("LineSyllabePeriod"),
        LineSyllabeRandom("LineSyllabeRandom"),
        LineSyllabeSymmetric("LineSyllabeSymmetric"),
        LineLetterBasic("LineLetterBasic"),
        SyllabeSyllabeBasic("SyllabeSyllabeBasic"),
        SyllabeSyllabeFixed("SyllabeSyllabeFixed"),
        SyllabeLetterBasic("SyllabeLetterBasic"),
        SyllabeLetterFixed("SyllabeLetterFixed"),
        LetterLetterBasic("LetterLetterBasic"),
        LetterLetterFixed("LetterLetterFixed"),
        None("nothing");

        private String fxoXF;
        
        FxObjectXmlFunc(String fxoXF){
            this.fxoXF = fxoXF;
        }

        public String getXmlFunc(){
            return fxoXF;
        }
    }

}
