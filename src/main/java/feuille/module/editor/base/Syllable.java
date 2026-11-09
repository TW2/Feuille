package feuille.module.editor.base;

public class Syllable {
    private String text;
    private long msDuration;

    public Syllable(String text, long msDuration) {
        this.text = text;
        this.msDuration = msDuration;
    }

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    public long getMsDuration() {
        return msDuration;
    }

    public void setMsDuration(long msDuration) {
        this.msDuration = msDuration;
    }
}
