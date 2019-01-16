/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.scripting;

/**
 *
 * @author The Wingate 2940
 */
public class SButton extends AObject {
    
    public SButton(){
            
        }
        
        public SButton(String pluginname, String displayname, String path,
                String function, String version, String description,
                String author, String help, String type){
            this.pluginname = pluginname;
            this.displayname = displayname;
            this.path = path;
            this.function = function;
            this.version = version;
            this.description = description;
            this.author = author;
            this.help = help;
            this.type = type;
        }
        
        public void setSButton(String pluginname, String displayname, String path,
                String function, String version, String description,
                String author, String help, String type){
            this.pluginname = pluginname;
            this.displayname = displayname;
            this.path = path;
            this.function = function;
            this.version = version;
            this.description = description;
            this.author = author;
            this.help = help;
            this.type = type;
        }
        
        @Override
        public String getPluginType() {
            return "button";
        }
    
}
