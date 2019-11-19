/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.scripting;

/**
 *
 * @author The Wingate 2940
 */
public abstract class AObject implements IObject {
    
    protected String pluginname = "";
    protected String displayname = "";
    protected String path = "";
    protected String function = "";
    protected String version = "";
    protected String description = "";
    protected String author = "";
    protected String help = "";
    protected String type = "";

    public AObject(){

    }

    @Override
    public String getPluginName() {
        return pluginname;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getHelp() {
        return help;
    }

    @Override
    public String getDisplayName() {
        return displayname;
    }

    @Override
    public String getAuthors() {
        return author;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String getFunction() {
        return function;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public String getType() {
        return type;
    }

}
