/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.karaoke.lib;

/**
 *
 * @author The Wingate 2940
 */
public class ParticleObject {
    
    String name = "";                                   // The effect's name. 
    String description = "";                            // Its description.
    String version = "";                                // Its version.
    String author = "";                                 // Its author.
    String nblayers = "";                               // Layers.
    String firstlayer = "0";                            // The main layer.
    String scriptPathname = "";                         // File to read.
    String function = "";                               // Function to load.

    String moment = "";                                 // Moment of effect
    String commands = "";                               // Commands to use
    String time = "0";                                  // Option of moment
    String image = "";                                  // Preview
    String style = "";                                  // One style in ASS format
    String collect = "";                                // Collection
    
//    String posCorrection = "1.50";                      // Position correction
//    String spaCorrection = "10";                        // Space correction
    String videoWidth = "1280";                         // Video width
    String videoHeight = "720";                         // Video height
    String posY = "50";                                 // Position on Y
    Mode mode = Mode.Normal;                            // Mode of functionment
    String rubyCode = "";                               // Ruby code (variables)
    Type type = Type.Syllable;                          // Mode of functionment
    
    boolean inUse = false;                              // State in the list.
    
    public ParticleObject(){
        
    }
    
    public ParticleObject(String name, String description,
            String scriptPathname, String function){
        this.name = name;
        this.description = description;
        this.scriptPathname = scriptPathname;
        this.function = function;
    }
    
    // <editor-fold defaultstate="collapsed" desc=" Main methods ">
    
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
    
    /** <p>Get the style.<br />
     * Obtient le style.</p> */
    public String getStyleName(){
        String stylename = style.substring(style.indexOf(":")+2, style.indexOf(","));        
        return stylename;
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
    
    /** <p>Set the correction of the position.<br />
     * Définit la correction de la position.</p> */
//    public void setPosCorrection(String posCorrection){
//        this.posCorrection = posCorrection;
//    }

    /** <p>Get the correction of the position.<br />
     * Obtient la correction de la position.</p> */
//    public String getPosCorrection(){
//        return posCorrection;
//    }
    
    /** <p>Set the correction of the space.<br />
     * Définit la correction de l'espace.</p> */
//    public void setSpaCorrection(String spaCorrection){
//        this.spaCorrection = spaCorrection;
//    }

    /** <p>Get the correction of the space.<br />
     * Obtient la correction de l'espace.</p> */
//    public String getSpaCorrection(){
//        return spaCorrection;
//    }
    
    /** <p>Set the width of the video size.<br />
     * Définit la largeur de la vidéo.</p> */
    public void setVideoWidth(String videoWidth){
        this.videoWidth = videoWidth;
    }

    /** <p>Get the width of the video size.<br />
     * Obtient la largeur de la vidéo.</p> */
    public String getVideoWidth(){
        return videoWidth;
    }
    
    /** <p>Set the height of the video size.<br />
     * Définit la hauteur de la vidéo.</p> */
    public void setVideoHeight(String videoHeight){
        this.videoHeight = videoHeight;
    }

    /** <p>Get the height of the video size.<br />
     * Obtient la hauteur de la vidéo.</p> */
    public String getVideoHeight(){
        return videoHeight;
    }
    
    /** <p>Set the height of the video size.<br />
     * Définit la hauteur de la vidéo.</p> */
    public void setPosY(String posY){
        this.posY = posY;
    }

    /** <p>Get the height of the video size.<br />
     * Obtient la hauteur de la vidéo.</p> */
    public String getPosY(){
        return posY;
    }
    
    public enum Mode{
        Normal("Normal"),
        Periodic("Periodic"),
        Random("Random"),
        Symmetric("Symmetric");
        
        private String mode;
        
        Mode(String mode){
            this.mode = mode;
        }
        
        @Override
        public String toString(){
            return mode;
        }
    }
    
    /** <p>Set the height of the video size.<br />
     * Définit la hauteur de la vidéo.</p> */
    public void setMode(String mode){
        if(mode.equalsIgnoreCase("Normal")){
            this.mode = Mode.Normal;
        }else if(mode.equalsIgnoreCase("Periodic")){
            this.mode = Mode.Periodic;
        }else if(mode.equalsIgnoreCase("Random")){
            this.mode = Mode.Random;
        }else if(mode.equalsIgnoreCase("Symmetric")){
            this.mode = Mode.Symmetric;
        }else{
            this.mode = Mode.Normal;
        }        
    }

    /** <p>Get the height of the video size.<br />
     * Obtient la hauteur de la vidéo.</p> */
    public String getMode(){
        return mode.toString();
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
    
    public enum Type{
        Sentence("Sentence"),
        Syllable("Syllable");
        
        private String type;
        
        Type(String type){
            this.type = type;
        }
        
        @Override
        public String toString(){
            return type;
        }
    }
    
    /** <p>Set the height of the video size.<br />
     * Définit la hauteur de la vidéo.</p> */
    public void setType(String type){
        if(type.equalsIgnoreCase("Sentence")){
            this.type = Type.Sentence;
        }else{
            this.type = Type.Syllable;
        }        
    }

    /** <p>Get the height of the video size.<br />
     * Obtient la hauteur de la vidéo.</p> */
    public String getType(){
        return type.toString();
    }
    
    // </editor-fold>
    
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
    
    public boolean isTheSame(ParticleObject po){
        if(!name.equals(po.getName())){return false;}
        if(!description.equals(po.getDescription())){return false;}
        if(!version.equals(po.getVersion())){return false;}
        if(!author.equals(po.getAuthor())){return false;}
        if(!nblayers.equals(po.getNbLayers())){return false;}
        if(!firstlayer.equals(po.getFirstLayer())){return false;}
        if(!scriptPathname.equals(po.getScriptPathname())){return false;}
        if(!function.equals(po.getFunction())){return false;}
        return true;
    }
    
}
