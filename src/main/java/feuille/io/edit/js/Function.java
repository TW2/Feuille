package feuille.io.edit.js;

import org.graalvm.polyglot.Value;

public class Function {

    private final String name;
    private final String category;
    private final Value function;
    private boolean active;

    public Function(Value function, String name, String category) {
        this.function = function;
        this.name = name;
        this.category = category;
        active = true;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public Value getFunction() {
        return function;
    }

    @Override
    public String toString() {
        return getName();
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
