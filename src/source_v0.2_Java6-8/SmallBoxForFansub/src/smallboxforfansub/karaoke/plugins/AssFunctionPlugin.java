/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package smallboxforfansub.karaoke.plugins;

/**
 *
 * @author Unknown User
 */
public interface AssFunctionPlugin extends GenericPlugin {

    /** The 1st main method.
     * in : A line from the main table.
     * out : A line to include in the table. */
    public String forOneLine();

    /** The 2nd main method.
     * in : Lines from the main table.
     * out : Line(s) to include in the table. */
    public String forFewLines();

    /** The commands to do on each (group of) letter(s) */
    public void setCommands(String commands);

    /** The name of the XMLPreset. */
    public void setXMLPresetName(String name);

    /** All layers of the effects. */
    public void setNbLayers(String nbLayers);

    /** The layer of the beginning of treatment. */
    public void setFirstLayer(String firstLayer);

    /** The moment of the effects :
     * Before, Meantime or After. */
    public void setMoment(String moment);

    /** The time to use with moment.
     * Before may produce negative time.
     * After may produce positive time. */
    public void setTime(String momentTime);

    /** All styles of the effects. */
    public void setStyle(String styles);
    
    /** Ruby code for the effects. */
    public void setRubyCode(String code);

}
