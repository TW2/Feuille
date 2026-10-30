package feuille.util.renderer;

import feuille.io.assa.AssActor;

import javax.swing.*;
import java.awt.*;

public class AssActorRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if(value instanceof AssActor v){
            setForeground(v.getColor().getColor());
            setFont(getFont().deriveFont(Font.BOLD));
            setText(v.getName());
            setIcon(v.getAssActorType().getIcon());
        }

        return this;
    }
}
