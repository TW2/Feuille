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
package org.wingate.feuille.m;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import org.wingate.feuille.util.DrawColor;

/**
 *
 * @author util2
 */
public class KnownColorRenderer extends JPanel implements ListCellRenderer {
    
    private final JLabel lblColor;
    private final JLabel lblName;

    public KnownColorRenderer() {
        lblColor = new JLabel();
        lblName = new JLabel();
        
        lblColor.setOpaque(true);
        lblName.setOpaque(true);
        
        lblColor.setBackground(Color.white);
        
        setLayout(new BorderLayout());
        add(lblColor, BorderLayout.WEST);
        add(lblName, BorderLayout.CENTER);
        
        lblColor.setPreferredSize(new java.awt.Dimension(40, HEIGHT));
        lblColor.setBorder(new LineBorder(Color.black));
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        
        Color bg = UIManager.getColor("List.background");
        Color fg = UIManager.getColor("List.foreground");
        
        setBackground(bg);
        setForeground(fg);
        lblName.setBackground(bg);
        lblName.setForeground(fg);
        
        if(value instanceof DrawColor x){
            String compo = String.format("(R:%d,G:%d,B:%d)", x.getRed(), x.getGreen(), x.getBlue());
            lblName.setText("  " + x.getName() + " (" + x.getHTML() + ") " + compo);
            lblColor.setBackground(x.getColor());
        }
        
        if(isSelected){
            if(list.hasFocus()){
                bg = UIManager.getColor("List.selectionBackground");
                fg = UIManager.getColor("List.selectionForeground");
            }else{
                bg = UIManager.getColor("List.selectionInactiveBackground");
                fg = UIManager.getColor("List.selectionInactiveForeground");
            }
            setBackground(bg);
            setForeground(fg);
            lblName.setBackground(bg);
            lblName.setForeground(fg);
        }
        
        return this;
    }
}
