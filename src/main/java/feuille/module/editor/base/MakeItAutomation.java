package feuille.module.editor.base;

import feuille.io.Settings;
import feuille.io.edit.js.Function;
import feuille.io.edit.js.MyScriptHandler;
import feuille.io.Exchange;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MakeItAutomation {

    private final MyScriptHandler myScriptHandler;
    private final List<String> folders = new ArrayList<>();
    private List<Function> functions;

    public MakeItAutomation(Exchange exchange) {
        myScriptHandler = new MyScriptHandler(exchange);
    }

    public void setPaths(Settings settings){
        File app = new File(new File("").getAbsolutePath());
        File folder = new File(app + "/settings/scripting/makeit");
        folders.add(folder.getAbsolutePath());
        folders.add(settings.getAutomationMakeItPath());
    }

    private List<Function> getList(){
        List<Function> list = new ArrayList<>();
        for(Map.Entry<String, Function> entry : myScriptHandler.getScripts().getRegistry().getFunctions().entrySet()){
            list.add(entry.getValue());
        }
        return list;
    }

    public void reload(){
        myScriptHandler.discover(folders.toArray(new String[0]));
        functions = getList();
    }

    public List<Function> getFunctions() {
        return functions;
    }

    public void execute(String functionName, Object... args) {
        myScriptHandler.execute(functionName, args);
    }

    public List<String> getFolders() {
        return folders;
    }
}
