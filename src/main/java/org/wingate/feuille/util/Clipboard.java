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
        StringSelection selection = new StringSelection(text);
        Toolkit.getDefaultToolkit().getSystemClipboard().getContents(selection);
    }
    
    public static String pasteString(){
        String text = "";
        Transferable tr = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
        if(tr == null) return text;
        if(tr.isDataFlavorSupported(DataFlavor.stringFlavor)){
            try {
                if(tr.getTransferData(DataFlavor.stringFlavor) instanceof String v){
                    text = v;
                }                    
            } catch (UnsupportedFlavorException | IOException ex) {
                Logger.getLogger(Clipboard.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return text;
    }
}
