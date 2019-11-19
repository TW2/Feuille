/*
 * Copyright (C) 2018 util2
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
package feuille.renderer;

import feuille.io.Event;
import feuille.util.DrawColor;
import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author util2
 */
public class AssEventTableRenderer extends JLabel implements TableCellRenderer {

    private List<Event> events = new ArrayList<>();
    
    private Color lineNumberColor = DrawColor.khaki.getColor();
    private Color dialogueColor = Color.white;
    private Color commentColor = DrawColor.pale_green.getColor();
    private Color proposalColor = DrawColor.gold.getColor();
    private Color requestColor = DrawColor.light_sky_blue.getColor();
    
    public AssEventTableRenderer(List<Event> events) {
        this.events = events;
        init();
    }
    
    private void init(){
        setOpaque(true);
    }
    
    public void updateEvents(List<Event> events){
        this.events = events;
        updateUI();
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if(value instanceof String){
            // Fill the background with a color
            switch(events.get(row).getLineType()){
                case Dialogue: setBackground(dialogueColor); break;
                case Comment: setBackground(commentColor); break;
                case Proposal: setBackground(proposalColor); break;
                case Request: setBackground(requestColor); break;
            }
            setForeground(Color.black);
            setText((String)table.getValueAt(row, column));
        } 
        
        // Filler when selected
        if(isSelected == true){
            setBackground(DrawColor.alice_blue.getColor());            
        }
        
        if(value instanceof String && column == 0){
            // Fill the number color and display the number
            setBackground(lineNumberColor);
            setForeground(Color.black);
            setText(Integer.toString(row + 1));
        }
        
        return this;
    }

    public void setLineNumberColor(Color lineNumberColor) {
        this.lineNumberColor = lineNumberColor;
    }

    public Color getLineNumberColor() {
        return lineNumberColor;
    }

    public void setDialogueColor(Color dialogueColor) {
        this.dialogueColor = dialogueColor;
    }

    public Color getDialogueColor() {
        return dialogueColor;
    }

    public void setCommentColor(Color commentColor) {
        this.commentColor = commentColor;
    }

    public Color getCommentColor() {
        return commentColor;
    }

    public void setProposalColor(Color proposalColor) {
        this.proposalColor = proposalColor;
    }

    public Color getProposalColor() {
        return proposalColor;
    }

    public void setRequestColor(Color requestColor) {
        this.requestColor = requestColor;
    }

    public Color getRequestColor() {
        return requestColor;
    }
    
    
}
