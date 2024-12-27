package org.wingate.feuille.ctrl;

import org.wingate.feuille.util.Load;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ElementsComboBox<T> extends javax.swing.JPanel {
    private final JComboBox<T> comboBox = new JComboBox<>();
    private final DefaultComboBoxModel<T> model = new DefaultComboBoxModel<>();
    private final JButton button = new JButton(Load.fromResource("/org/wingate/feuille/16 losange carre.png"));

    public ElementsComboBox(){
        comboBox.setModel(model);

        setLayout(new BorderLayout());

        add(comboBox, BorderLayout.CENTER);
        add(button, BorderLayout.EAST);

        setMaximumSize(new Dimension(90, 22));
        setPreferredSize(new Dimension(90, 22));
    }

    public JButton getButton() {
        return button;
    }

    public java.util.List<T> getObjects(){
        java.util.List<T> list = new ArrayList<>();

        for(int i=0; i<model.getSize(); i++){
            list.add(model.getElementAt(i));
        }

        return list;
    }

    public void setObjects(java.util.List<T> objects, boolean add){
        if(!add){
            model.removeAllElements();
        }
        model.addAll(objects);
    }

    public void addObject(T obj){
        model.addElement(obj);
    }

    public void removeObject(T obj){
        model.removeElement(obj);
    }

    public void clear(){
        model.removeAllElements();
    }

    public T getSelectedItem(){
        return (T)comboBox.getSelectedItem();
    }

    public void setSelected(T obj){
        model.setSelectedItem(obj);
    }
}
