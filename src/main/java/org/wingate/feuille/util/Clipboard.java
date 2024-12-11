/*
 * Copyright (C) 2024 util2
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.wingate.feuille.util;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author util2
 */
public class Clipboard {

    public Clipboard() {
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
