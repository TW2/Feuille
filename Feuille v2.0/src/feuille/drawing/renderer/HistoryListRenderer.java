/*
 * Copyright (C) 2019 util2
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
package feuille.drawing.renderer;

import feuille.drawing.HistoryItem;
import feuille.panel.Draw;
import feuille.util.DrawColor;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

/**
 *
 * @author util2
 */
public class HistoryListRenderer extends JPanel implements ListCellRenderer {

    JLabel labelIcon = new JLabel("");
    JLabel labelText = new JLabel("");
    Font font = labelText.getFont();
    Draw draw;
    
    public HistoryListRenderer(Draw draw) {
        this.draw = draw;
        init();
    }
    
    private void init(){
        setLayout(new BorderLayout());
        labelIcon.setOpaque(true);
        labelIcon.setBackground(Color.white);
        labelText.setOpaque(true);
        labelText.setBackground(Color.white);
        add(labelIcon, BorderLayout.WEST);
        add(labelText, BorderLayout.CENTER);
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        
        setPreferredSize(new Dimension(list.getWidth(), 40));
        labelIcon.setPreferredSize(new Dimension(40, 40));
        
        if(value instanceof HistoryItem){
            HistoryItem h = (HistoryItem)value;
            labelIcon.setIcon(h.getIcon());
            labelText.setText(h.getText());
            labelIcon.setBackground(Color.white);
            labelText.setBackground(Color.white);
            labelText.setForeground(Color.black);
            labelText.setFont(font);
            
            int current = draw.getHistory().getCurrentIndex();
            int allValues = draw.getHistory().getObjects().size();
            if(current < allValues && index > current){
                labelIcon.setBackground(DrawColor.white_smoke.getColor());
                labelText.setBackground(DrawColor.white_smoke.getColor());
                labelText.setForeground(Color.red);
                labelText.setFont(font.deriveFont(Font.ITALIC));
            }
        }
        
        return this;
    }
    
}
