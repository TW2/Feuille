package feuille.component;

import feuille.util.Loader;

import javax.swing.*;
import java.awt.*;

public class CodeLanguage extends JPanel {

    public enum Language {
        Groovy, JavaScript, Kotlin, Lua, Python, Ruby
    }

    private final JComboBox<Language> cbLanguage;

    public CodeLanguage(){
        DefaultComboBoxModel<Language> model = new DefaultComboBoxModel<>();
        cbLanguage = new JComboBox<>(model);
        cbLanguage.setRenderer(new CodeLanguageRenderer());

        for(Language l : Language.values()){
            model.addElement(l);
        }

        cbLanguage.setSelectedItem(Language.JavaScript);

        setLayout(new BorderLayout());
        add(cbLanguage, BorderLayout.CENTER);
    }

    public Language getLanguage() {
        return (Language)cbLanguage.getSelectedItem();
    }

    public void setLanguage(Language language) {
        cbLanguage.setSelectedItem(language);
        cbLanguage.updateUI();
    }

    public JComboBox<Language> getComboBoxLanguage() {
        return cbLanguage;
    }

    public static class CodeLanguageRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if(value instanceof Language v){
                ImageIcon icon;
                switch(v){
                    case Groovy -> {
                        icon = Loader.fromResource("/images/32-code-groovy.png", 32, 32);
                    }
                    case Kotlin -> {
                        icon = Loader.fromResource("/images/32-code-kotlin.png", 32, 32);
                    }
                    case Lua -> {
                        icon = Loader.fromResource("/images/32-code-lua.png", 32, 32);
                    }
                    case Python -> {
                        icon = Loader.fromResource("/images/32-code-python.png", 32, 32);
                    }
                    case Ruby -> {
                        icon = Loader.fromResource("/images/32-code-ruby.png", 32, 32);
                    }
                    default -> {
                        icon = Loader.fromResource("/images/32-code-javascript.png", 32, 32);
                    }
                }
                setIcon(icon);
            }

            return this;
        }
    }
}
