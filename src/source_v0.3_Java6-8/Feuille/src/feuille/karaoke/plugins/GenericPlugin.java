/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package feuille.karaoke.plugins;

/**
 *
 * @author Unknown User
 */
public interface GenericPlugin {

    /** This is the name of this plugin. */
    public String getPluginName();

    /** The plugin description. */
    public String getDescription();

    /** The plugin help topic. */
    public String getHelp();

    /** A more readable name for this plugin. */
    public String getDisplayName();

    /** The authors of this plugin; maybe fame is coming. */
    public String getAuthors();

}
