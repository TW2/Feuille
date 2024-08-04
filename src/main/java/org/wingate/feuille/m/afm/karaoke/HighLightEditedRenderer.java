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
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import org.wingate.feuille.util.DrawColor;

/**
 *
 * @author util2
 */
public class HighLightEditedRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        
        if(table.getModel() instanceof KaraokeTableModel model){
            BiEvent bev = model.getEvents().get(row);
            setText("0");
            if(bev.getTransformedAssEvents().isEmpty()){
                setBackground(DrawColor.blue_violet.getColor(.3f));
            }else{
                setBackground(DrawColor.yellow.getColor(.5f));
            }
        }
        
        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);        
    }
    
}
