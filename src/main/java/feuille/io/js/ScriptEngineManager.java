package feuille.io.js;

import feuille.util.Loader;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.proxy.ProxyExecutable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * Done by ChatGPT, enhanced by TW2
 */
public final class ScriptEngineManager implements AutoCloseable {

    private final ScriptRegistry registry = new ScriptRegistry();
    private final Context context;

    public ScriptEngineManager() {
        this.context = Context
                .newBuilder("js")
                //.allowHostAccess(HostAccess.NONE)
                .build();

        injectRegister();
    }

    /**
     * Injecte register() dans le contexte JavaScript.
     */
    private void injectRegister() {

        ProxyExecutable registerFunction = args -> {
            if(args.length < 2 || args.length > 3) {
                Loader.dialogInfo(
                        """
                                Expected:
                                
                                register(name, function)
                                or
                                register(name, category, function)
                                """
                        );
            }

            if(args.length == 2){
                String name = args[0].asString();
                Value function = args[1];

                registry.register(name, "None", function);
            }else if(args.length == 3){
                String name = args[0].asString();
                String category = args[1].asString();
                Value function = args[2];

                registry.register(name, category, function);
            }

            return null;
        };

        context.getBindings("js")
                .putMember("register", registerFunction);
    }

    /**
     * Charge et exécute un fichier JavaScript.
     */
    public void load(Path path) throws IOException {
        if (!Files.exists(path)) {
            Loader.consoleErr("Script not found: " + path);
            return;
        }

        Loader.consoleInfo("[Scripts] Loading " + path.getFileName());

        context.eval("js", Files.readString(path));
    }

    /**
     * Charge tous les fichiers .js d'un dossier.
     */
    public void loadAll(Path directory) {
        if (!Files.isDirectory(directory)) {
            Loader.consoleErr("Invalid scripts directory: " + directory);
            return;
        }

        try (Stream<Path> paths = Files.list(directory)) {
            paths.filter(path -> path.toString().endsWith(".js")).sorted().forEach(path -> {
                try {
                    load(path);
                }catch(IOException exception) {
                    Loader.consoleErr(exception.getMessage());
                }
            });
        }catch(IOException exception){
            Loader.consoleErr(exception.getMessage());
        }
    }

    /**
     * Exécute une fonction enregistrée.
     */
    public Value call(String name, Object... arguments) {
        Value function = registry.get(name);

        try{
            assert function != null;
            return function.execute(arguments);
        }catch(org.graalvm.polyglot.PolyglotException exception){
            Loader.dialogErr(
                    "[Scripts] Error in function: "
                    + name
                    + "\n"
                    + exception.getMessage()
            );
            throw exception;
        }
    }

    public boolean hasFunction(String name) {
        return registry.contains(name);
    }

    public ScriptRegistry getRegistry() {
        return registry;
    }

    public Context getContext() { return context; }

    @Override
    public void close() {
        registry.clear();
        context.close();
    }
}