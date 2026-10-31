package feuille.module.editor.base.voyager;

import feuille.util.DrawColor;

public class CheckedBookmark extends Bookmark {
    CheckedBookmark(String name) {
        super(name);
        color = DrawColor.gold.getColor();
    }
}
