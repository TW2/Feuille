package org.wingate.feuille.lib.table;

import java.util.ArrayList;
import java.util.List;

public class SubsNote {
    public enum Kind {
        None, Bookmark;
    }

    private Kind kind;
    private Object attach;

    public SubsNote(Kind kind, Object attach){
        this.kind = kind;
        this.attach = attach;
    }

    public SubsNote() {
        kind = Kind.None;
        attach = null;
    }

    public Kind getKind() {
        return kind;
    }

    public void setKind(Kind kind) {
        this.kind = kind;
    }

    public Object getAttach() {
        return attach;
    }

    public void setAttach(Object attach) {
        this.attach = attach;
    }
}
