package org.wingate.feuille.lib.table;

import org.wingate.feuille.subs.ass.ASS;
import org.wingate.feuille.util.ISO_3166;

import java.util.Locale;

public class VirtualVersion {
    private int version;
    private String author;
    private String updates;
    private ISO_3166 language;
    private ASS script;

    public VirtualVersion(int version, String author, String updates, ISO_3166 language, ASS script) {
        this.version = version;
        this.author = author;
        this.updates = updates;
        this.language = language;
        this.script = script;
    }

    public VirtualVersion() {
        this(0, "Unknown author", "No description",
                ISO_3166.getISO_3166(Locale.getDefault().getISO3Country()), new ASS());
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUpdates() {
        return updates;
    }

    public void setUpdates(String updates) {
        this.updates = updates;
    }

    public ISO_3166 getLanguage() {
        return language;
    }

    public void setLanguage(ISO_3166 language) {
        this.language = language;
    }

    public ASS getScript() {
        return script;
    }

    public void setScript(ASS script) {
        this.script = script;
    }
}
