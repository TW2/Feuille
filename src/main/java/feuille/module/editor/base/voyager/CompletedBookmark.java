package feuille.module.editor.base.voyager;

import java.awt.*;

public class CompletedBookmark extends Bookmark {
    CompletedBookmark(String name) {
        super(name);
        color = Color.green;
    }
}
