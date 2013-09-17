/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smallboxforfansub.scripting;

/**
 *
 * @author The Wingate 2940
 */
public class STab extends AObject {
    
    public STab(){
            
        }
        
        public STab(String pluginname, String displayname, String path,
                String function, String version, String description,
                String author, String help){
            this.pluginname = pluginname;
            this.displayname = displayname;
            this.path = path;
            this.function = function;
            this.version = version;
            this.description = description;
            this.author = author;
            this.help = help;
        }
        
        public void setSTab(String pluginname, String displayname, String path,
                String function, String version, String description,
                String author, String help){
            this.pluginname = pluginname;
            this.displayname = displayname;
            this.path = path;
            this.function = function;
            this.version = version;
            this.description = description;
            this.author = author;
            this.help = help;
        }
        
        @Override
        public String getPluginType() {
            return "tab";
        }
    
}
