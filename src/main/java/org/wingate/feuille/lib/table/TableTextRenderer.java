package org.wingate.feuille.lib.table;

import org.wingate.feuille.subs.ass.*;
import org.wingate.feuille.util.DrawColor;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class TableTextRenderer extends JLabel implements TableCellRenderer {

    public enum Stripped {
        Off, Partially, On;
    }

    private Stripped stripped = Stripped.Partially;
    private String partiallyStrippedSymbol = "◆";

    public TableTextRenderer() {
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

        if(value instanceof String x){
            setText(applyStrip(x));
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

    private String applyStrip(String s){
        String str = "";
        switch(stripped){
            case On -> {
                if(s.contains("{\\")){
                    try {
                        str = s.replaceAll("\\{[^\\}]+\\}", "");
                    } catch (Exception e) {
                        str = s;
                    }
                }else{
                    str = s;
                }
            }
            case Partially -> {
                if(s.contains("{\\")){
                    try {
                        str = s.replaceAll("\\{[^\\}]+\\}", partiallyStrippedSymbol);
                    } catch (Exception e) {
                        str = s;
                    }
                }else{
                    str = s;
                }
            }
            case Off -> {
                str = s;
            }
        }
        return str;
    }

    public void setStripped(Stripped stripped) {
        this.stripped = stripped;
    }

    public void setPartiallyStrippedSymbol(String partiallyStrippedSymbol) {
        this.partiallyStrippedSymbol = partiallyStrippedSymbol;
    }

}
