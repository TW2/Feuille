/*
 * Copyright (C) 2022 util2
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
package org.wingate.virginsheet.util;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

/**
 *
 * @author util2
 */
public class Clipboard {
    
    public Clipboard() {
    }    
    
    public static void CCopy(String s){
        try{
            StringSelection ss = new StringSelection(s);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss,null);
        }catch(IllegalStateException ise){
            /* Le presse-papier n'est pas disponible */
        }
    }
     
    public static String CPaste(){
        String s = "";
        Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
        try {
            /* Vérification que le contenu est de type texte. */
            if( t!=null && t.isDataFlavorSupported(DataFlavor.stringFlavor) ) {
                s = (String)t.getTransferData(DataFlavor.stringFlavor);
            }
        }catch(UnsupportedFlavorException | java.io.IOException ufe){
            
        }
        return s;
    }
}
