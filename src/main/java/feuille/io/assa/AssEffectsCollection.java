package feuille.io.assa;

import java.util.ArrayList;
import java.util.List;

public class AssEffectsCollection {
    private String name;
    private final List<AssEffect> effects;

    public AssEffectsCollection(String name) {
        this.name = name;
        effects = new ArrayList<>();
    }

    public List<AssEffect> getEffects() {
        return effects;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
