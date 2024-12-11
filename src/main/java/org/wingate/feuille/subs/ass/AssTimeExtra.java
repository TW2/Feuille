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
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;
import org.wingate.feuille.util.DrawColor;

/**
 *
 * @author util2
 */
public class AssTimeExtra {
    private AssTime time;
    private AssTime difference;

    public AssTimeExtra(AssTime time, AssTime difference) {
        this.time = time;
        this.difference = difference;
    }

    public AssTimeExtra() {
        this(new AssTime(0d), new AssTime(0d));
    }

    public AssTime getTime() {
        return time;
    }

    public void setTime(AssTime time) {
        this.time = time;
    }

    public AssTime getDifference() {
        return difference;
    }

    public void setDifference(AssTime difference) {
        this.difference = difference;
    }
    
    public static class Renderer extends JPanel implements TableCellRenderer {
        
        private final JLabel lblTime;
        private final JLabel lblDiff;

        public Renderer() {
            lblTime = new JLabel("");
            lblDiff = new JLabel("");
            
            lblTime.setOpaque(true);
            lblDiff.setOpaque(true);
            
            setLayout(new GridLayout(2, 1, 1, 2));
            
            add(lblTime);
            add(lblDiff);            
        }
        

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            // Get table background (avoid searching from any other way)
            // FlatLaf properties >> Table.background
            Color bg = UIManager.getColor("Table.background");
            // Get table foreground (avoid searching from any other way)
            // FlatLaf properties >> Table.foreground
            Color fg = UIManager.getColor("Table.foreground");
            
            if(table.getValueAt(row, 1) instanceof AssEvent.Type type){
                if(type == AssEvent.Type.Comment){
                    bg = DrawColor.violet.getColor();
                }
            }
            
            if(isSelected){
                bg = UIManager.getColor("Table.selectionBackground");
                fg = UIManager.getColor("Table.selectionForeground");
            }
            
            if(value instanceof AssTimeExtra extra){
                setBackground(bg);
                lblTime.setBackground(bg);
                lblDiff.setBackground(bg);
                
                lblTime.setForeground(fg);
                lblDiff.setForeground(DrawColor.getClosest(fg, 0.5f));
                
                lblTime.setText(extra.getTime().toAss());
                lblDiff.setText(extra.getDifference().toAss());
            }
            
            return this;
        }
        
    } 
}
