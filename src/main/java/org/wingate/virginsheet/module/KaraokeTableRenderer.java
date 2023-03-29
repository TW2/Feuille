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
package org.wingate.virginsheet.module;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;
import org.wingate.assj.AssEvent;
import org.wingate.virginsheet.Theme;

/**
 *
 * @author util2
 */
public class KaraokeTableRenderer extends JLabel implements TableCellRenderer {

    private final KaraokeTableModel assTableModel;
    private final KaraokePanel karaokePanel;
    
    public KaraokeTableRenderer(KaraokeTableModel assTableModel, KaraokePanel karaokePanel) {
        this.assTableModel = assTableModel;
        this.karaokePanel = karaokePanel;
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        
        AssEvent ev = assTableModel.getAss().getEvents().get(row);
        
        if(value instanceof String s && column != 12){
            setHorizontalAlignment(SwingConstants.CENTER);
            setText(s);
        }else if(value instanceof String s){
            setHorizontalAlignment(SwingConstants.LEFT);
            setText(s);
        }else if(value instanceof Integer i){
            setHorizontalAlignment(SwingConstants.CENTER);
            setText(Integer.toString(i));
        }
        
        if(isSelected){
            setBackground(karaokePanel.getTheme().getType() == Theme.Type.Light ?
                    new Color(201, 221, 255) : Color.blue);
            setForeground(karaokePanel.getTheme().getType() == Theme.Type.Light ?
                    Color.black : Color.white);
        }else{
            switch(ev.getLineType()){
                case Commands, Movie, Picture, Sound -> {
                    setBackground(karaokePanel.getTheme().getType() == Theme.Type.Light ?
                            Color.lightGray : Color.darkGray);
                }
                case Comment -> {
                    setBackground(karaokePanel.getTheme().getType() == Theme.Type.Light ?
                            new Color(175, 255, 194) : new Color(0, 86, 4));
                }
                case Proposal -> {
                    setBackground(karaokePanel.getTheme().getType() == Theme.Type.Light ?
                            Color.pink : new Color(114, 4, 112));
                }
                case Request -> {
                    setBackground(karaokePanel.getTheme().getType() == Theme.Type.Light ?
                            new Color(255, 238, 173) : new Color(54, 134, 150));
                }
                case Dialogue -> {
                    setBackground(karaokePanel.getTheme().getType() == Theme.Type.Light ?
                            Color.white : Color.black);
                }
            }
                        
            setForeground(karaokePanel.getTheme().getType() == Theme.Type.Light ?
                    Color.black : Color.white);
        }
        
        if(column == 0){
            setBackground(Color.gray);
            setForeground(Color.white);
        }
        
        return this;
    }
    
}
