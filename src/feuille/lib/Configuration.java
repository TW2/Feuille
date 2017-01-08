/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package feuille.lib;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>This class is a storage for the configuration.<br />
 * Cette classe est un espace de stockage pour la configuration.</p>
 * @author The Wingate 2940
 */
public class Configuration {
    
    @SuppressWarnings("MapReplaceableByEnumMap")
    private Map<Type,String> myMap = new HashMap<>();

    /** <p>A choice of type of line in an ASS file.<br />
     * Ceci est une énumération du type de ligne que l'on trouve dans les fichiers ASS.</p> */
    public enum Type{
        //AssFxMaker uses :
        //FX_PATH("FX_PATH:"), XFX_PATH("XFX_PATH:"), FXPLUG_PATH("FXPLUG_PATH:"),
        //DOCS_PATH("DOCS_PATH:"), RUBY_EDITOR("RUBY_EDITOR:"), DRAW_EDITOR("DRAW_EDITOR:"),
        //FONT("FONT:"), THEME("THEME:"), TABLE_DISPLAY("TABLE_DISPLAY:"), 
        //CHK_UPDATE("CHK_UPDATE:"), FORCE_ISO("FORCE_ISO:");
        
        //Now Feuille uses :
        DOCS_PATH("DOCS_PATH:"), CODE_EDITOR("CODE_EDITOR:"), FONT("FONT:"), 
        BACKGD_IMAGE("BACKGD_IMAGE:"), THEME("THEME:"), 
        ORG_TABLE_DISPLAY("ORG_TABLE_DISPLAY:"), RES_TABLE_DISPLAY("RES_TABLE_DISPLAY:"),
        CHK_UPDATE("CHK_UPDATE:"), FORCE_ISO("FORCE_ISO:"), KARA_MODULE("KARA_MODULE:"),
        CODE_MODULE("CODE_MODULE:"), DRAW_MODULE("DRAW_MODULE:"), ANAL_MODULE("ANAL_MODULE:"),
        STARTWITH("STARTWITH:");

        private String cfg;

        Type(String cfg){
            this.cfg = cfg;
        }

        public String getString(){
            return cfg;
        }
    }

    /** <p>Create a new Configuration.<br />Crée un nouveau Configuration.</p> */
    public Configuration(){

    }

    /** <p>Put a value to the storage.<br />
     * Insert une valeur dans l'espace de stockage.</p>
     * @see Type */
    public void put(Type type, String value){
        myMap.put(type, value);
    }

    /** <p>Get a value.<br />Obtient une valeur.</p>
     * @see Type */
    public String get(Type type){
        return myMap.get(type);
    }
    
    /** <p>Get all values.<br />OLbtient toutes les valeurs.</p> */
    public Collection<String> getValues(){
        return myMap.values();
    }

}
