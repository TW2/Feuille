/*
 * Copyright (C) 2023 util2
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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 *
 * @author util2
 */
public class KaraokeTreeRenderer extends DefaultTreeCellRenderer {
    
    private final JPanel mainPanel = new JPanel();
    
    private final JLabel iconLabel = new JLabel("");    
    private final JPanel infosPanel = new JPanel();
    
    private final JLabel nameLabel = new JLabel("");
    private final JLabel authorLabel = new JLabel("");
        
    public KaraokeTreeRenderer(){
        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
        
        mainPanel.setLayout(new BorderLayout());
        infosPanel.setLayout(new GridLayout(2, 1));
        
        mainPanel.add(iconLabel, BorderLayout.WEST);
        mainPanel.add(infosPanel, BorderLayout.CENTER);
        
        infosPanel.add(nameLabel);
        infosPanel.add(authorLabel);
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
            boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {

        super.getTreeCellRendererComponent(
                        tree, value, sel,
                        expanded, leaf, row,
                        hasFocus);
        
        if(value instanceof DefaultMutableTreeNode tn){
            if(tn.getUserObject() instanceof KaraokeEffect kfx){
                iconLabel.setIcon(kfx.getIcon());
                nameLabel.setText(kfx.getName());
                authorLabel.setText(kfx.getAuthor());
            }
        }

        return this;
    }
}
