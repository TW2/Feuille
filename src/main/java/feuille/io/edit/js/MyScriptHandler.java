package feuille.io.edit.js;

import feuille.io.Exchange;
import org.graalvm.polyglot.Value;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MyScriptHandler {

    private final Exchange exchange;

    private final List<String> knownPath;

    private final ScriptEngineManager scripts;
    private Value result = null;

    public MyScriptHandler(Exchange exchange) {
        this.exchange = exchange;
        knownPath = new ArrayList<>();
        scripts = new ScriptEngineManager();
    }

    public void discover(String... path){
        // Load scripts
        for(String p : path){
            if(p == null) continue;
            if(knownPath.contains(p)) continue;
            scripts.loadAll(Path.of(p));
            knownPath.add(p);
        }
    }

    public void execute(String functionName, Object... args){
        Methods methods = new Methods(exchange);
        scripts.getContext().getBindings("js").putMember("api", methods);
        result = scripts.call(functionName, args);
    }

    public Value getResult(){
        return result;
    }

    public ScriptEngineManager getScripts() {
        return scripts;
    }
}
