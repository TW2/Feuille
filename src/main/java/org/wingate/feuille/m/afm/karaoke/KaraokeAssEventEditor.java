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
package org.wingate.feuille.m.afm.karaoke;

import java.awt.Component;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;
import org.wingate.feuille.ass.AssEvent;

/**
 *
 * @author util2
 */
public class KaraokeAssEventEditor extends AbstractCellEditor implements TableCellEditor {
    
    private AssEvent event;    
    private final JTextField tf;

    public KaraokeAssEventEditor() {
        tf = new JTextField();
        
        tf.addFocusListener(new FocusAdapter(){
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                tf.setText(event.getText());
            }
        });
        
        tf.addCaretListener((e) -> {
           event.setText(tf.getText());
        });
    }

    @Override
    public Object getCellEditorValue() {
        // On a fini d'éditer, on passe la valeur à la table
        return event;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        // On obtient une valeur à éditer
        if(value instanceof AssEvent ev){
            event = ev;
        }
        return tf;
    }    
}
