package feuille.module.editor.base.bookmark;

import java.awt.*;

public class CompletedBookmark extends Bookmark {
    CompletedBookmark(String name) {
        super(name);
        color = Color.green;
    }
}
