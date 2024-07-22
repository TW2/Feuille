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
package org.wingate.feuille.m.ygg.table;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import org.wingate.feuille.ass.AssEvent;
import org.wingate.feuille.ass.AssTime;
import org.wingate.feuille.util.DrawColor;

/**
 *
 * @author util2
 */
public class AssTableRenderer extends DefaultTableCellRenderer {

    public AssTableRenderer() {
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        Color bg = UIManager.getColor("Table.background");
        Color fg = UIManager.getColor("Table.foreground");
        
        setBackground(bg == null ? Color.white : bg);
        setForeground(fg == null ? Color.black : fg);
        
        switch (value) {
            case Integer x -> { setText(Integer.toString(x)); }
            case AssTime x -> {
                if(column == 3) setText(x.getStartString());
                if(column == 4) setText(x.getEndString());
                if(column == 5) setText(x.getDurationString());
            }
            case AssEvent.LineType x -> { setText(x.toString()); }
            case String x -> { setText(x); }
            default -> {}
        }
        
        if(((AssEvent.LineType)table.getValueAt(row, 1)).compareTo(AssEvent.LineType.Comment) == 0){
            setBackground(DrawColor.green.getColor(0.2f));
            setForeground(DrawColor.green.getColor());
        }
        
        if(isSelected){
            if(table.hasFocus()){
                bg = UIManager.getColor("Table.selectionBackground");
                fg = UIManager.getColor("Table.selectionForeground");
                
                setBackground(bg == null ? DrawColor.corn_flower_blue.getColor() : bg);
                setForeground(fg == null ? Color.white : fg);
            }else{
                bg = UIManager.getColor("Table.selectionInactiveBackground");
                fg = UIManager.getColor("Table.selectionInactiveForeground");
                
                setBackground(bg == null ? Color.lightGray : bg);
                setForeground(fg == null ? Color.black : fg);              
            }
        }
        
        return this;
    }
}
