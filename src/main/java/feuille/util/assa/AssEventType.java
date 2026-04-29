package feuille.util.assa;

import feuille.util.Loader;

public enum AssEventType {
    Comment(Loader.language("event.type.comment", "Comment")),
    Dialogue(Loader.language("event.type.dialogue", "Dialogue")),
    Tagged(Loader.language("event.type.tagged", "Tagged"));

    final String name;

    AssEventType(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
