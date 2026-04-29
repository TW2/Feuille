package feuille.module.editor.assa;

import java.util.ArrayList;
import java.util.List;

public class AssStylesCollection {
    private String name;
    private final List<AssStyle> styles;

    public AssStylesCollection(String name) {
        this.name = name;
        styles = new ArrayList<>();
    }

    public List<AssStyle> getStyles() {
        return styles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
