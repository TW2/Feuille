package feuille.io.js;

import org.graalvm.polyglot.Value;

public class Function {

    private final String name;
    private final String category;
    private final Value function;

    public Function(Value function, String name, String category) {
        this.function = function;
        this.name = name;
        this.category = category;
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
}
