package feuille.module.editor;

import java.awt.*;

public abstract class Bookmark {
    protected String name;
    protected Color color;

    Bookmark(String name) {
        this.name = name;
        color = Color.black;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
