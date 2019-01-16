/*
 * PressePapier.java
 *
 * Created on 28 octobre 2006, 20:12
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package feuille.karaoke.lib;
import java.awt.Toolkit;
import java.awt.datatransfer.*;

/**
 * <p>This class is a module for the clipboard.<br />
 * Cette classe est un module pour le presse-papier.</p>
 * @author The Wingate 2940
 */
public class Clipboard {
    
    /** <p>Creates a new Clipboard.<br />
     * Crée un nouveau Clipboard.</p> */
    public Clipboard() {
    }
    
    /** <p>Copy an element in the clipboard.<br />
     * Copie un élément texte dans le presse papier.</p>
     * @param s The text to copy. */
     public void CCopy(String s){
         try{
             StringSelection ss = new StringSelection(s);
             Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss,null);
         }catch(IllegalStateException ise){
             /** Le presse-papier n'est pas disponible */
         }
     }
     
     /** <p>Paste an element incoming from clipboard.<br />
      * Colle un élément texte venant du presse papier.</p> */
     public String CPaste(){
         String s = "";
         Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
         try {
             /** V�rification que le contenu est de type texte. */
             if( t!=null && t.isDataFlavorSupported(DataFlavor.stringFlavor) ) {
                 s = (String)t.getTransferData(DataFlavor.stringFlavor);
             }
         }catch(UnsupportedFlavorException ufe){
         }catch(java.io.IOException ioe){}
         return s;
     }
    
}
