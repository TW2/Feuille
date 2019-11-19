/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package smallboxforfansub.karaoke.lib;

/**
 * <p>This class is a convert tool for lines.<br />
 * Cette classe est un outil de conversion pour les lignes.</p>
 * @author The Wingate 2940
 */
public class ProgramLine {
    // Format du programme :
    // "Type", "Layer", "Margins", "Start", "End", "Total time",
    // "Style", "Name", "Text"
    // avec pour les marges, LVR au lieu de LRV.

    private LineType type = LineType.Nothing;
    private String layer = "0";
    private String margins = "0,0,0";
    private Time start = new Time();
    private Time end = new Time();
    private Time totaltime = new Time();
    private String style = "Default";
    private String name = "";
    private String effect = "";
    private String text = "";
    
    /** <p>A choice of type of line.<br />Un choix de type de ligne.</p> */
    public enum LineType{
        Dialogue("D","Dialogue"),Comment("#","Comment"), Picture("P","Picture"),
        Sound("S","Sound"), Movie("M","Movie"), Command("C","Command"),
        Karaoke("K","Karaoke"), Nothing("?","Nothing"),
        HardComment(";","HardComment"), 
        CommentedKaraoke("#K","CommentedKaraoke");

        protected String label;
        protected String ltype;

        /** Constructor */
        LineType(String pLabel, String ltype){
            this.label = pLabel;
            this.ltype = ltype;
        }

        public String getLabel(){
            return this.label;
        }
        
        public String getLineType(){
            return this.ltype;
        }
        
        public String getASSType(){
            if(label.equals("D") | label.equals("K")){
                return "Dialogue";
            }else if(label.equals("#") | label.equals("#K")){
                return "Comment";
            }else if(label.equals("P")){
                return "Picture";
            }else if(label.equals("S")){
                return "Sound";
            }else if(label.equals("M")){
                return "Movie";
            }else if(label.equals("C")){
                return "Command";
            }else{
                return "Dialogue";
            }
        }
        
    }
    
    /** <p>Use a program line in default empty form.<br />
     * Utilise une ligne dans un formulaire vide par défaut.</p> */
    public ProgramLine(){
        
    }
    
    // <editor-fold defaultstate="collapsed" desc=" Dealing with line... ">
    
    /** <p>Set the type of line.<br />Définit le type de ligne.</p> */
    public void setLineType(LineType lt){
        type = lt;
    }
    
    /** <p>Set the type of line with itself.<br />
     * Définit le type de ligne.</p> */
    public void setLineType(String line){
        if(line.startsWith("Comment") && line.contains("\\k")){
            type = LineType.CommentedKaraoke;
        }else if(line.contains("\\k")){
            type = LineType.Karaoke;
        }else if(line.startsWith("Dialogue")){
            type = LineType.Dialogue;
        }else if(line.startsWith("Comment")){
            type = LineType.Comment;
        }else if(line.startsWith("Picture")){
            type = LineType.Picture;
        }else if(line.startsWith("Sound")){
            type = LineType.Sound;
        }else if(line.startsWith("Movie")){
            type = LineType.Movie;
        }else if(line.startsWith("Command")){
            type = LineType.Command;
        }else if(isCommentLine(line)){
            type = LineType.HardComment;
        }else{
            type = LineType.Nothing;
        }
    }

    /** <p>Set the type of line with itself (Function for ProgamtoASS reformatting).<br />
     * Définit le type de ligne.</p> */
    public void setLineType2(String line){
        if(line.startsWith("#")){
            type = LineType.Comment;
        }else if(line.startsWith("D")){
            type = LineType.Dialogue;
        }else if(line.startsWith("K")){
            type = LineType.Dialogue;
        }else if(line.startsWith("P")){
            type = LineType.Picture;
        }else if(line.startsWith("S")){
            type = LineType.Sound;
        }else if(line.startsWith("M")){
            type = LineType.Movie;
        }else if(line.startsWith("C")){
            type = LineType.Command;
        }else{
            type = LineType.Nothing;
        }
    }
    
    /** <p>Get the type of line in LineType object (Get the String
     * representation with LineType.getLabel())<br />
     * Obtient le type de ligne en un objet LineType.</p> */
    public LineType getLineType(){
        return type;
    }
    
    /** <p>Set the layer.<br />Définit la couche.</p> */
    public void setLayer(String layer){
        this.layer = layer;
    }
    
    /** <p>Get the layer.<br />Obtient la couche.</p> */
    public String getLayer(){
        return layer;
    }
    
    /** <p>Set the margins.<br />Définit les marges.</p> */
    public void setMargins(String margins){
        this.margins = margins;
    }
    
    /** <p>Set the margins.<br />Définit les marges.</p> */
    public void setMargins(String l, String r, String v)
            throws NumberFormatException{
        margins = Integer.parseInt(l)+","
                +Integer.parseInt(v)+","
                +Integer.parseInt(r);
    }
    
    /** <p>Get the margins.<br />Obtient les marges.</p> */
    public String getMargins(){
        return margins;
    }
    
    /** <p>Get the margin L.<br />Obtient la marge L.</p> */
    public String getMarginL(){
        return margins.split(",")[0];
    }
    
    /** <p>Get the margin L.<br />Obtient la marge L.</p> */
    public String getBasedASSMarginL(){
        String left = getMarginL();
        if(left.length()==1){ left = "000"+left; }
        if(left.length()==2){ left = "00"+left; }
        if(left.length()==3){ left = "0"+left; }
        return left;
    }
    
    /** <p>Get the margin V.<br />Obtient la marge V.</p> */
    public String getMarginV(){
        return margins.split(",")[1];
    }
    
    /** <p>Get the margin V.<br />Obtient la marge V.</p> */
    public String getBasedASSMarginV(){
        String vertical = getMarginV();
        if(vertical.length()==1){ vertical = "000"+vertical; }
        if(vertical.length()==2){ vertical = "00"+vertical; }
        if(vertical.length()==3){ vertical = "0"+vertical; }
        return vertical;
    }
    
    /** <p>Get the margin R.<br />Obtient la marge R.</p> */
    public String getMarginR(){
        return margins.split(",")[2];
    }
    
    /** <p>Get the margin R.<br />Obtient la marge R.</p> */
    public String getBasedASSMarginR(){
        String right = getMarginR();
        if(right.length()==1){ right = "000"+right; }
        if(right.length()==2){ right = "00"+right; }
        if(right.length()==3){ right = "0"+right; }
        return right;
    }
    
    /** <p>Set the start time.<br />Définit le temps du début.</p> */
    public void setStart(Time start){
        this.start = start;
    }
    
    /** <p>Set the start time.<br />Définit le temps du début.</p> */
    public void setStart(String h, String m, String s, String c)
            throws NumberFormatException{
        start.setHours(Integer.parseInt(h));
        start.setMinutes(Integer.parseInt(m));
        start.setSeconds(Integer.parseInt(s));
        start.setMilliseconds(Integer.parseInt(c)*10);
    }
    
    /** <p>Get the start time.<br />Obtient le temps du début.</p> */
    public Time getStart(){
        return start;
    }
    
    /** <p>Set the end time.<br />Définit le temps de fin.</p> */
    public void setEnd(Time end){
        this.end = end;
    }
    
    /** <p>Set the end time.<br />Définit le temps de fin.</p> */
    public void setEnd(String h, String m, String s, String c)
            throws NumberFormatException{
        end.setHours(Integer.parseInt(h));
        end.setMinutes(Integer.parseInt(m));
        end.setSeconds(Integer.parseInt(s));
        end.setMilliseconds(Integer.parseInt(c)*10);
    }
    
    /** <p>Get the end time.<br />Obtient le temps de fin.</p> */
    public Time getEnd(){
        return end;
    }
    
    /** <p>Set the total time.<br />Définit le temps total.</p> */
    public void setTotaltime(Time totaltime){
        this.totaltime = totaltime;
    }
    
    /** <p>Set the total time.<br />Définit le temps total.</p> */
    public void setTotaltime(String h, String m, String s, String c)
            throws NumberFormatException{
        totaltime.setHours(Integer.parseInt(h));
        totaltime.setMinutes(Integer.parseInt(m));
        totaltime.setSeconds(Integer.parseInt(s));
        totaltime.setMilliseconds(Integer.parseInt(c)*10);
    }
    
    /** <p>Get the total time.<br />Obtient le temps total.</p> */
    public Time getTotaltime(){
        return totaltime;
    }
    
    /** <p>Set the style.<br />Définit le style.</p> */
    public void setStyle(String style){
        this.style = style;
    }
    
    /** <p>Get the style.<br />Obtient le style.</p> */
    public String getStyle(){
        return style;
    }
    
    /** <p>Set the name.<br />Définit le nom.</p> */
    public void setName(String name){
        this.name = name;
    }
    
    /** <p>Get the name.<br />Obtient le nom.</p> */
    public String getName(){
        return name;
    }
    
    /** <p>Set the effect.<br />Définit l'effet.</p> */
    public void setEffect(String effect){
        this.effect = effect;
    }
    
    /** <p>Get the effect.<br />Obtient l'effet.</p> */
    public String getEffect(){
        return effect;
    }
    
    /** <p>Set the text.<br />Définit le texte.</p> */
    public void setText(String text){
        this.text = text;
    }
    
    /** <p>Get the text.<br />Obtient le texte.</p> */
    public String getText(){
        return text;
    }
    
    /** <p>Find the totaltime with start and end.<br />
     * Trouve le temps total avec le début et la fin.</p> */
    public void setTotaltime(Time start, Time end){
        Time t = new Time();
        t = t.substract(start, end);
        totaltime = t;
    }
    
    /** <p>Search for line of comments and return 'false' for informations.<br />
     * Recherche la ligne de commentaires et retourne 'faux' pour les informations.</p> */
    private boolean isCommentLine(String line){
        boolean b = false;
        
        if(!line.contains("Title") &
                !line.contains("Original Script") &
                !line.contains("Original Translation") &
                !line.contains("Original Editing") &
                !line.contains("Original Timing") &
                !line.contains("Original Script Checking") &
                !line.contains("Synch Point") &
                !line.contains("Script Updated By") &
                !line.contains("Update Details") &
                !line.contains("ScriptType") &
                !line.contains("Collisions") &
                !line.contains("PlayResX") &
                !line.contains("PlayResY") &
                !line.contains("PlayDepth") &
                !line.contains("Timer") &
                !line.contains("Audio") &
                !line.contains("Video")){
            return true;
        }
        
        return b;
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" Get objects... ">
    
    /** <p>Get a row to insert.<br />
     * Obtient une ligne à insérer dans une table.</p> */
    public Object[] toRow(){
        Object[] aInserer2 = {
            "",
            getLineType().getLabel(),
            getLayer(),
            getMargins(),
            getStart().toProgramBasedASSTime(),
            getEnd().toProgramBasedASSTime(),
            getTotaltime().toProgramBasedASSTime(),
            getStyle(),
            getName(),
            getEffect(),
            getText(),
            ""
        };
        
        return aInserer2;
    }
    
    /** <p>Get a line.<br />Obtient une ligne.</p> */
    public String toLine(){
        String s = "";
        s += getLineType().getLineType() + ": ";
        s += getLayer() + ",";
        s += getStart().toASSTime() + ",";
        s += getEnd().toASSTime() + ",";
        s += getStyle() + ",";
        s += getName() + ",";
        s += getBasedASSMarginL() + ",";
        s += getBasedASSMarginR() + ",";
        s += getBasedASSMarginV() + ",";
        s += getEffect() + ",";
        s += getText();
        return s;
    }
    
    /** <p>Get an ASS line.<br />Obtient une ligne ASS.</p> */
    public String toAssLine(){
        String s = "";
        s += getLineType().getASSType() + ": ";
        s += getLayer() + ",";
        s += getStart().toASSTime() + ",";
        s += getEnd().toASSTime() + ",";
        s += getStyle() + ",";
        s += getName() + ",";
        s += getBasedASSMarginL() + ",";
        s += getBasedASSMarginR() + ",";
        s += getBasedASSMarginV() + ",";
        s += getEffect() + ",";
        s += getText();
        return s;
    }
    
    // </editor-fold>
    
       
}
