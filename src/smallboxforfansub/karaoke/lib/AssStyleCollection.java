/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package smallboxforfansub.karaoke.lib;

import smallboxforfansub.karaoke.lib.AssStyle;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>This class is a collection for all ASS styles.<br />
 * Cette classe est une collection pour tous les styles ASS.</p>
 * @author The Wingate 2940
 */
public class AssStyleCollection {

    /** <p>Create a new collection of styles.<br />
     * Crée une nouvelle collection de styles.</p> */
    public AssStyleCollection(){
        //Do nothing
    }

    /** <p>Define a Map to create a collection of AssStyles.<br />
     * Définit une map pour créer une collection de AssStyles.</p> */
    private Map<String,AssStyle> myMap = new HashMap<String,AssStyle>();

    /** <p>Add an AssStyle (the key is the name of this AssStyle).<br />
     * Ajoute un style ASS (la clé est le nom de ce style ASS).</p> */
    public void addMember(String styleName, AssStyle as){
        if(myMap.containsKey(styleName)==false){
            myMap.put(styleName, as);
        }        
    }

    /** <p>Delete an AssStyle (the key is the name of this AssStyle).<br />
     * Supprime un style ASS (la clé est le nom de ce style ASS).</p> */
    public void deleteMember(String styleName){
        myMap.remove(styleName);
    }

    /** <p>Get an AssStyle (the key is the name of this AssStyle).<br />
     * Obtient un style ASS (la clé est le nom de ce style ASS).</p> */
    public AssStyle getMember(String styleName){
        return myMap.get(styleName);
    }

    /** <p>Get all AssStyles of the collection.<br />
     * Obtient tous les styles ASS de la collection.</p> */
    public Collection<AssStyle> getMembers(){
        return myMap.values();
    }

    /** <p>Change an AssStyle (the key is the name of this AssStyle).<br />
     * Change un style ASS (la clé est le nom de ce style ASS).</p> */
    public void changeMember(String styleName, AssStyle as){
        deleteMember(styleName);
        addMember(styleName, as);
    }

}
