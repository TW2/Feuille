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
package org.wingate.feuille.subs.ass;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author util2
 */
public class AssComboBoxRenderer extends JLabel implements ListCellRenderer{

    public AssComboBoxRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
        
        String s;
        switch(value){
            case null -> { s = ""; }
            case AssStyle x -> { s = x.getName(); }
            case AssActor x -> { s = x.getName(); }
            case AssEffect x -> { s = x.getName(); }
            case Font x -> { s = x.getFamily(); }
            default -> { s = ""; }
        }
        setText(s);
        
        if(isSelected){
            setBackground(Color.blue.brighter());
            setForeground(Color.white);
        }else{
            setBackground(Color.white);
            setForeground(Color.black);
        }
        
        return this;
    }
}
