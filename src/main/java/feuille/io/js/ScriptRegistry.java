package feuille.io.js;

import feuille.util.Loader;
import org.graalvm.polyglot.Value;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Done by ChatGPT, enhanced by TW2
 */
public final class ScriptRegistry {

    private final Map<String, Function> functions = new ConcurrentHashMap<>();

    public void register(String name, String category, Value function) {
        if (name == null || name.isBlank()) return;
        if (!function.canExecute()) return;

        functions.put(name, new Function(function, name, category));
        Loader.consoleInfo("[Scripts] Registered: " + name);
    }

    public Value get(String name) {
        Value function = functions.get(name).getFunction();

        if (function == null) {
            Loader.dialogErr("Unknown script function: " + name);
            return null;
        }

        return function;
    }

    public boolean contains(String name) {
        return functions.containsKey(name);
    }

    public void unregister(String name) {
        functions.remove(name);
    }

    public void clear() {
        functions.clear();
    }

    public int size() {
        return functions.size();
    }

    public Map<String, Function> getFunctions() {
        return functions;
    }
}
