/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package smallboxforfansub.karaoke.lib;

/**
 * <p>This class is a storage for a pack of styles.<br />
 * Cette classe est un espace de stockage pour un paquet de styles.</p>
 * @author The Wingate 2940
 */
public class StylesPack {

    private String pack = "?";
    private AssStyleCollection asc = null;

    /** <p>Create a new link between a name and a collection.<br />
     * Crée un nouveau lien entre un nom et une collection.</p> */
    public StylesPack(){
        //do nothing
    }

    /** <p>Create a new link between a name and a collection.<br />
     * Crée un nouveau lien entre un nom et une collection.</p> */
    public StylesPack(String pack, AssStyleCollection asc){
        this.pack = pack;
        this.asc = asc;
    }

    /** <p>Set the name.<br />Définit le nom.</p> */
    public void setPack(String pack){
        this.pack = pack;
    }

    /** <p>Get the name.<br />Obtient le nom.</p> */
    public String getPack(){
        return pack;
    }

    /** <p>Set the collection.<br />Définit la collection</p> */
    public void setCollection(AssStyleCollection asc){
        this.asc = asc;
    }

    /** <p>Get the collection.<br />Obtient la collection.</p> */
    public AssStyleCollection getCollection(){
        return asc;
    }

    /** <p>Get the name.<br />Obtient le nom.</p> */
    @Override
    public String toString(){
        return pack;
    }

}
