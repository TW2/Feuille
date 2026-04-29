package feuille.util;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class Clipboard {
    private Clipboard() {
    }

    public static void copyString(String text){
        try{
            StringSelection ss = new StringSelection(text);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
        }catch(HeadlessException exc){
            /* Presse-papier occupé ou erreur */
        }
    }

    public static String pasteString(){
        String text = "";
        Transferable tr = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
        try{
            if(tr != null && tr.isDataFlavorSupported(DataFlavor.stringFlavor)){
                text = (String)tr.getTransferData(DataFlavor.stringFlavor);
            }
        }catch(UnsupportedFlavorException | IOException exc){
            /* Presse-papier occupé ou erreur */
        }
        return text;
    }
}
