package org.wingate.feuille.lib.table;

public class SubsNote {
    public enum Kind {
        None, Bookmark;
    }

    private Kind kind;
    private Object attach;
    private int index;

    public SubsNote(Kind kind, Object attach) {
        this.kind = kind;
        this.attach = attach;
        index = -1;
    }

    public SubsNote() {
        this(Kind.None, null);
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

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
