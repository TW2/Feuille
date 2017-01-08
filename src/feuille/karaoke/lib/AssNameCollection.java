/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package feuille.karaoke.lib;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>This class is a collection for all names.<br />
 * Cette classe est une collection pour tous les noms.</p>
 * @author The Wingate 2940
 */
public class AssNameCollection {

    /** <p>Create a virgin collection.<br />
     * Cr&#233;e une collection vierge.</p> */
    public AssNameCollection(){
        
    }
    
    /** <p>Create a collection with a value.<br />
     * Cr&#233;e une collection avec une valeur.</p> */
    public AssNameCollection(String name){        
        addMember(name);
    }
    
    /* Voici les variables */
    
    private List<String> monArrayList = new ArrayList<String>();
    
    /* Voici les m&#233;thodes */
    
    /** <p>Add a name.<br />Ajoute un nom.</p> */
    public void addMember(String name)
    throws UnsupportedOperationException, NullPointerException,
            IllegalArgumentException{
        if(name.equals(null)){
            name = "";
        }
        if(monArrayList.contains(name)==false){
            monArrayList.add(name);
        }        
    }
    
    /** <p>Delete a name.<br />Supprime un nom.</p> */
    public void deleteMember(String name)
    throws UnsupportedOperationException, NullPointerException{
        monArrayList.remove(name);
    }
    
    /** <p>Delete a name and add one another.<br />
     * Supprime un nom et en ajoute un autre.</p>  */
    public void modifyMember(String oldName, String NewName)
    throws UnsupportedOperationException, NullPointerException,
            IllegalArgumentException{
        monArrayList.remove(oldName);
        monArrayList.add(NewName);
    }
    
    /** <p>Get all names in a collection.<br />
     * Obtient tous les noms à travers une collection.</p> */
    public String[] getMembers(){
        String[] s = monArrayList.toArray(new String [0]);
        return s;
    }
    
    /** <p>Get the count of all names in the collection.<br />
     * Obtient le compte des noms contenus dans la collection.</p> */
    public int getSize(){
        return monArrayList.size();
    }
}
