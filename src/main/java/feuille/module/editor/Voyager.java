package feuille.module.editor;

import feuille.module.editor.assa.AssEvent;
import feuille.util.ISO_3166;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Voyager {
    private ISO_3166 language;
    private final List<Voyager> voyagers;
    private final List<Bookmark> bookmarks;

    private boolean visible;
    private boolean group;
    private boolean selected;
    private boolean collapsed;
    private String note;
    private AssEvent event;

    public Voyager(ISO_3166 language, boolean visible, AssEvent event) {
        voyagers = new ArrayList<>();
        bookmarks = new ArrayList<>();
        this.language = language;
        this.visible = visible;
        this.event = event;
        group = false;
        selected = false;
        collapsed = true;
        note = "";
    }

    public Voyager() {
        this(ISO_3166.getISO_3166(Locale.getDefault().getISO3Country()), false, null);
    }

    public List<Voyager> getVoyagers() {
        return voyagers;
    }

    public ISO_3166 getLanguage() {
        return language;
    }

    public List<Bookmark> getBookmarks() {
        return bookmarks;
    }

    public void setLanguage(ISO_3166 language) {
        this.language = language;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public AssEvent getEvent() {
        return event;
    }

    public void setEvent(AssEvent event) {
        this.event = event;
    }

    public boolean isGroup() {
        return group;
    }

    public void setGroup(boolean group) {
        this.group = group;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isCollapsed() {
        return collapsed;
    }

    public void setCollapsed(boolean collapsed) {
        this.collapsed = collapsed;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
