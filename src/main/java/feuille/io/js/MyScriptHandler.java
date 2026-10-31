package feuille.io.js;

import feuille.util.Exchange;
import org.graalvm.polyglot.Value;

import java.io.File;
import java.nio.file.Path;

public class MyScriptHandler {

    private final Exchange exchange;

    private final ScriptEngineManager scripts;
    private Value result = null;

    public MyScriptHandler(Exchange exchange) {
        this.exchange = exchange;
        scripts = new ScriptEngineManager();
    }

    public void discover(String... path){
        // Load scripts
        for(String p : path){
            if(p == null) continue;
            scripts.loadAll(Path.of(p));
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
