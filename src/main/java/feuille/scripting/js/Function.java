package feuille.scripting.js;

import org.graalvm.polyglot.Value;

public class Function {

    private final String name;
    private final Value function;
    private boolean active;

    public Function(Value function, String name) {
        this.function = function;
        this.name = name;
        active = true;
    }

    public String getName() {
        return name;
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
