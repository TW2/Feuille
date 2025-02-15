package org.wingate.feuille.lib.table;

import org.wingate.feuille.subs.ass.*;
import org.wingate.feuille.util.DrawColor;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class TableAssActorRenderer extends JLabel implements TableCellRenderer {

    public TableAssActorRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {

        AssEvent event = null;
        Object obj = table.getModel().getValueAt(row, 12);
        if(obj instanceof AssEvent ev){
            event = ev;
        }

        Color bg;

        if(event != null && event.getType() == AssEvent.Type.Comment){
            bg = DrawColor.violet.getColor();
        }else{
            // Get table background (avoid searching from any other way)
            // FlatLaf properties >> Table.background
            bg = UIManager.getColor("Table.background");
        }

        // Get table foreground (avoid searching from any other way)
        // FlatLaf properties >> Table.foreground
        Color fg = UIManager.getColor("Table.foreground");

        if(value instanceof AssActor x){
            setText(x.toString());
        }

        if(isSelected){
            bg = UIManager.getColor("Table.selectionBackground");
            fg = UIManager.getColor("Table.selectionForeground");
        }

        // Set color to label
        setBackground(bg);
        setForeground(fg);

        return this;
    }
}
