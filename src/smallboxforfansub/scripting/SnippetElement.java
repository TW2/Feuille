/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smallboxforfansub.scripting;

import smallboxforfansub.lib.Language;
import smallboxforfansub.lib.Language.ISO_3166;



/**
 *
 * @author The Wingate 2940
 */
public class SnippetElement {
    
    private String author = "";
    private String description = "";
    private String code = "";
    private ISO_3166 iso;
    
    public SnippetElement(){
        iso = Language.getDefaultISO_3166();
    }
    
    public void setAuthor(String author){
        this.author = author;
    }
    
    public String getAuthor(){
        return author;
    }
    
    public void setDescription(String description){
        this.description = description;
    }
    
    public String getDescription(){
        return description;
    }
    
    public void setCode(String code){
        this.code = code;
    }
    
    public String getCode(){
        return code;
    }
    
    public void setLanguage(ISO_3166 iso){
        this.iso = iso;
    }
    
    public ISO_3166 getLanguage(){
        return iso;
    }
    
    public void setLanguageCode(String code){
        this.iso = Language.getFromCode(code);
    }
    
    public String getLanguageCode(){
        return iso.getAlpha3();
    }
    
    @Override
    public String toString(){
        return code;
    }
}
