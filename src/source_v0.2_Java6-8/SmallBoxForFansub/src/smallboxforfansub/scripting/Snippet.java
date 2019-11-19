/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package smallboxforfansub.scripting;

import java.util.ArrayList;
import java.util.List;
import smallboxforfansub.lib.Language.ISO_3166;



/**
 * <p>This class is a storage for a snippet.<br />
 * Cette classe est un espace de stockage pour un snippet.</p>
 * @author The Wingate 2940
 */
public class Snippet {

    private String title = "";
    private String element = "";
    private String description = "";
    private String author = "";
    private String type = "Ruby";
    private List<SnippetElement> snielem = new ArrayList<SnippetElement>();

    /** <p>Create a new snippet.<br />Crée un nouveau snippet.</p> */
    public Snippet(){

    }

    /** <p>Set the title.<br />Définit le titre.</p> */
    public void setTitle(String title){
        this.title = title;
    }

    /** <p>Get the title.<br />Obtient le titre.</p> */
    public String getTitle(){
        return title;
    }

    /** <p>Set an element with JRuby code.<br />
     * Définit un élément avec du code JRuby.</p> */
    public void setElement(String element){
        this.element = element;
    }

    /** <p>Get an element.<br />Obtient un élément.</p> */
    public String getElement(){
        element = element.replaceAll("\\[br\\]", "\n");
        element = element.replaceAll("\\[sp\\]", " ");
        element = element.replaceAll("\\[quote\\]", "\"");
        return element;
    }

    /** <p>Set a description.<br />Définit une description.</p> */
    public void setDescription(String description){
        this.description = description;
    }

    /** <p>Get a description.<br />Obtient une description.</p> */
    public String getDescription(){
        return description;
    }

    /** <p>Set the author.<br />Définit l'auteur.</p> */
    public void setAuthor(String author){
        this.author = author;
    }

    /** <p>Get the author.<br />Obtient l'auteur.</p> */
    public String getAuthor(){
        return author;
    }
    
    /** <p>Set the type.<br />Définit le type.</p> */
    public void setType(String type){
        this.type = type;
    }

    /** <p>Get the type.<br />Obtient le type.</p> */
    public String getType(){
        return type;
    }

//    /** <p>Get an element in one line.<br />
//     * Obtient un élément en une ligne.</p> */
//    @Override
//    public String toString(){
//        String s = element;
//        s = s.replaceAll("\\[br\\]", "\n");
//        s = s.replaceAll("\\[sp\\]", " ");
//        s = s.replaceAll("\\[quote\\]", "\"");
//        s = s.replaceAll("#[^\n]+", "");
//        return s;
//    }
    
    /** <p>Get an element in one line.<br />
     * Obtient un élément en une ligne.</p> */
    @Override
    public String toString(){
        return title;
    }
    
    public void addSnippetElement(SnippetElement se){
        snielem.add(se);
    }
    
    public void addSnippetElement(String author, String description, String code, ISO_3166 iso){
        SnippetElement se = new SnippetElement();
        se.setAuthor(author);
        se.setDescription(description);
        se.setCode(code);
        se.setLanguage(iso);
        addSnippetElement(se);
    }
    
    public void deleteSnippetElement(SnippetElement se){
        snielem.remove(se);
//        for(SnippetElement x : snielem){
//            if(x.equals(se)){
//                snielem.remove(x);
//            }
//        }
    }
    
    public void deleteSnippetElement(String author, String description, String code, ISO_3166 iso){
        SnippetElement se = new SnippetElement();
        se.setAuthor(author);
        se.setDescription(description);
        se.setCode(code);
        se.setLanguage(iso);
        for(SnippetElement x : snielem){
            if(x.equals(se)){
                snielem.remove(x);
            }
        }
    }
    
    public void setSnippetElements(List<SnippetElement> snielem){
        this.snielem = snielem;
    }
    
    public List<SnippetElement> getSnippetElements(){
        return snielem;
    }

}
