package feuille.module.editor.voyager;

import feuille.util.Loader;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ElementsComboBox<T> extends JPanel {
    private final JComboBox<T> comboBox = new JComboBox<>();
    private final DefaultComboBoxModel<T> model = new DefaultComboBoxModel<>();
    private final JButton button = new JButton();

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

    public List<T> getObjects(){
        List<T> list = new ArrayList<>();
        for(int i=0; i<comboBox.getItemCount(); i++){
            list.add(comboBox.getItemAt(i));
        }
        return list;
    }

    public void setObjects(List<T> list){
        model.removeAllElements();
        for(T obj : list){
            model.addElement(obj);
        }
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

    public void setImage(ImageIcon image){
        button.setIcon(image);
    }
}
