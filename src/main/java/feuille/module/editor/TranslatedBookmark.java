package feuille.module.editor;

import feuille.util.DrawColor;

public class TranslatedBookmark extends Bookmark {
    TranslatedBookmark(String name) {
        super(name);
        color = DrawColor.deep_pink.getColor();
    }
}
