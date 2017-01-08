/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package feuille.karaoke.lib;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>This class is a container for keeping the numbers of lines
 * for one FxObject. Only the ForFewLines mode need an object
 * like that. After we can treat each line that call a FxObject at once.<br />
 * Cette classe est un conteneur utilisable pour stocker les
 * numéros des lignes pour un seul FxObject. Seul le mode ForFewLines
 * en a besoin. Nous pourrons ensuite traiter toutes les lignes
 * concernées par un FxObject en une seul fois.</p>
 * @author The Wingate 2940
 */
public class FxoLines {

    FxObject fxo = null;
    List<Integer> linesList = new ArrayList<Integer>();

    /** <p>Create a new FxoLines.<br />Crée un nouveau FxoLines.</p> */
    public FxoLines(){
        //Nothing
    }

    /** <p>Create a new FxoLines with a FxObject.<br />
     * Crée un nouveau FxoLines avec un FxObject.</p> */
    public FxoLines(FxObject fxo){
        this.fxo = fxo;
    }

    /** <p>Set the FxObject of this FxoLines object.<br />
     * Définit le FxObject de ce FxoLines.</p> */
    public void setFxObject(FxObject fxo){
        this.fxo = fxo;
    }

    /** <p>Get the FxObject of this FxoLines object.<br />
     * Obtient le FxObject de ce FxoLines.</p> */
    public FxObject getFxObject(){
        return fxo;
    }

    /** <p>Add only a new line.<br />Ajoute une nouvelle ligne.</p> */
    public void addLine(int line){
        if(linesList.contains(line)==false){
            linesList.add(line);
        }        
    }

    /** <p>Check if the line is already in the list or not.<br />
     * Vérifie si la ligne est déjà dans la liste ou non.</p> */
    public boolean isInList(int line){
        return linesList.contains(line);
    }

    /** <p>Get a table of lines.<br />Retourne une table de lignes.</p> */
    public int[] getLines(){
        int[] lines = new int[linesList.size()];
        for(int i=0; i<linesList.size();i++){
            lines[i] = (Integer)linesList.get(i);
        }
        return lines;
    }



}
