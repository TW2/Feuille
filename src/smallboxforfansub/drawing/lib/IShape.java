/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package smallboxforfansub.drawing.lib;

/**
 * Voici l'interface qui définit ce qu'une forme doit avoir.
 * @author The Wingate 2940
 */
public interface IShape {

    /** Donne la commande ASS spécifique à la "ligne". */
    public String getCommand();

    /** Définit le point de 'origine' */
    public void setOriginPoint(int x, int y);

    /** Obtient le point de 'origine' */
    public java.awt.Point getOriginPoint();

    /** Définit le point de 'dernier' */
    public void setLastPoint(int x, int y);

    /** Obtient le point de 'dernier' */
    public java.awt.Point getLastPoint();

    /** Marque l'élément comme 'en train de changer'. */
    public void setMarked(boolean b);

    /** Obtient le status de changement de l'élément. */
    public boolean getMarked();
    
    /** Marque l'élément comme sélectionné */
    public void setInSelection(boolean b);
    
    /** Obtient le status de la sélection */
    public boolean isInSelection();
    
    /** Marque l'élément comme sélectionné */
    public void setFirstInSelection(boolean b);
    
    /** Obtient le status de la sélection */
    public boolean isFirstInSelection();

}
