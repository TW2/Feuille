/*
 * tableTypeRenderer.java
 *
 * Created on 11 mai 2006, 00:14
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package feuille.karaoke.renderer;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.border.Border;

/**
 *
 * @author The Wingate 2940
 */
public class tableTypeRenderer extends javax.swing.JLabel
        implements javax.swing.table.TableCellRenderer {
    
    /** Couleur de Dialogue - Color for a Dialogue event */
    private Color fcDialogue = Color.black;
    /** Couleur de Karaoke - Color for a Karaoke event */
    private Color fcKaraoke = Color.green;
    /** Couleur de Commentaire - Color for a Comment event */
    private Color fcComment = Color.gray;
    /** Couleur de Image - Color for a Picture event, not used in Funsub */
    private Color fcPicture = Color.yellow;
    /** Couleur de Son - Color for a Sound event, not used in Funsub */
    private Color fcAudio = Color.magenta;
    /** Couleur de Film - Color for a Movie event, not used in Funsub */
    private Color fcVideo = Color.blue;
    /** Couleur de Commande - Color for a Command event, not used in Funsub */
    private Color fcCommand = Color.orange;
    
    /** Couleur de Dialogue - Color for a Dialogue event */
    private Color bcDialogue = Color.white;
    /** Couleur de Karaoke - Color for a Karaoke event */
    private Color bcKaraoke = Color.green.brighter();
    /** Couleur de Commentaire - Color for a Comment event */
    private Color bcComment = Color.gray.brighter();
    /** Couleur de Image - Color for a Picture event, not used in Funsub */
    private Color bcPicture = Color.yellow.brighter();
    /** Couleur de Son - Color for a Sound event, not used in Funsub */
    private Color bcAudio = Color.magenta.brighter();
    /** Couleur de Film - Color for a Movie event, not used in Funsub */
    private Color bcVideo = Color.blue.brighter();
    /** Couleur de Commande - Color for a Command event, not used in Funsub */
    private Color bcCommand = Color.orange.brighter();
    
    /** Couleur de selection - Color of selection */
    private Color cSelection = new Color(184,207,229);
    /** Couleur de bordure de selection - BorderColor of selection */
    private Color bordSelect = new Color(99,130,191);
    
    /** Champ d�finissant une case de <b>Dialogue</b> */
    public static int DIALOGUE = 0;
    /** Champ d�finissant une case de <b>Karaoke</b> */
    public static int KARAOKE = 1;
    /** Champ d�finissant une case de <b>Commentaire</b> */
    public static int COMMENT = 2;
    /** Champ d�finissant une case d'<b>Image</b> */
    public static int PICTURE = 3;
    /** Champ d�finissant une case d'<b>Audio</b> */
    public static int AUDIO = 4;
    /** Champ d�finissant une case de <b>Vid�o</b> */
    public static int VIDEO = 5;
    /** Champ d�finissant une case de <b>Commande</b> */
    public static int COMMAND = 6;
    
    public enum Column{
        ID(0), TYPE(1), LAYER(2), MARGINS(3), START(4),
        END(5), TOTAL(6), STYLE(7), NAME(8), EFFECTS(9),
        TEXT(10), FX(11);
        
        private int id;
        
        Column(int id){
            this.id = id;
        }
        
        public int getId(){
            return id;
        }
    }
    
    public enum TextType{
        StripAll, Normal, WithItems;
    }
    
    TextType texttype = TextType.Normal;
    
    Border unselectedBorder = null;
    Border selectedBorder = null;
    Border selectedBorderWithoutFocus = null;
    
    Font fontText = new Font("Arial Unicode MS",Font.PLAIN,12);
    
    /** Creates a new instance of tableTypeRenderer
     * @param opaque si transparent alors <i>false</i> */
    public tableTypeRenderer(boolean opaque) {
        this.setOpaque(opaque);
    }
    
    /** Cr�e une nouvelle instance de tableTypeRenderer
     * @param back une couleur de fond ou <i>null</i>
     * @param fore une couleur de texte ou <i>null</i> */
    public tableTypeRenderer(Color back, Color fore) {
        this.setOpaque(true);
        if (back!=null){
            bcDialogue = back;
            bcKaraoke = back;
            bcComment = back;
            bcPicture = back;
            bcAudio = back;
            bcVideo = back;
            bcCommand = back;
        }
        if (fore!=null){
            fcDialogue = fore;
            fcKaraoke = fore;
            fcComment = fore;
            fcPicture = fore;
            fcAudio = fore;
            fcVideo = fore;
            fcCommand = fore;
        }
    }
    
    /** Composant g�rant l'affichage dans le JLabel */
    @Override
    public Component getTableCellRendererComponent( JTable table, Object string
	, boolean isSelected, boolean hasFocus, int row, int column) {
        
        //Obtient le texte et le transmet au JLabel (renderer)
        String s = (String)string;
            
        if(column == table.getColumn(Column.TEXT.getId()).getModelIndex()){
            // Show rendered text / Affiche le texte avec le rendu
            this.setText(withTextRender(texttype,s));
        }else if(column == table.getColumn(Column.ID.getId()).getModelIndex()){
            // Show line's number / Affiche le numéro de la ligne
            this.setText(withLineID(row));
        }else{
            this.setText(s);
        }
        
        
        //Obtient le texte de la premi�re case du JTable [type d'�v�nement]
        String t = (String)table.getValueAt(row,
                table.getColumn(Column.TYPE.getId()).getModelIndex());
        
        //Puis d�fini les couleurs d'une case par d�faut
        this.setForeground(fcDialogue);
        this.setBackground(isSelected==true? cSelection : bcDialogue);
        
        //Puis transmet la couleur du texte au JLabel (renderer)
        //Puis transmet la couleur de fond au JLabel (renderer)/* � faire */
        if(t!=null && t.equalsIgnoreCase("null")==false){
            if(t.equalsIgnoreCase("D")==true){
                this.setForeground(fcDialogue);
                this.setBackground(isSelected==true? cSelection : bcDialogue);
            }else if(t.equalsIgnoreCase("K")==true){
                this.setForeground(fcKaraoke);
                this.setBackground(isSelected==true? cSelection : bcKaraoke);
            }else if(t.equalsIgnoreCase("#")==true){
                this.setForeground(fcComment);
                this.setBackground(isSelected==true? cSelection : bcComment);
            }else if(t.equalsIgnoreCase("P")==true){
                this.setForeground(fcPicture);
                this.setBackground(isSelected==true? cSelection : bcPicture);
            }else if(t.equalsIgnoreCase("S")==true){
                this.setForeground(fcAudio);
                this.setBackground(isSelected==true? cSelection : bcAudio);
            }else if(t.equalsIgnoreCase("M")==true){
                this.setForeground(fcVideo);
                this.setBackground(isSelected==true? cSelection : bcVideo);
            }else if(t.equalsIgnoreCase("C")==true){
                this.setForeground(fcCommand);
                this.setBackground(isSelected==true? cSelection : bcCommand);
            }
        }else{
            setForeground(Color.black);
            setBackground(isSelected==true? cSelection : Color.white);
        }
        
        //D�fini les bordures de la s�lection
        if (isSelected && hasFocus){
            if (selectedBorder == null){
                selectedBorder = BorderFactory.createMatteBorder(
                        1,1,1,1, bordSelect);
            }
        setBorder(selectedBorder);
        }else if (isSelected && !hasFocus){
            if (selectedBorderWithoutFocus == null){
                selectedBorderWithoutFocus = BorderFactory.createMatteBorder(
                        1,1,1,1, cSelection);
            }
        setBorder(selectedBorderWithoutFocus);
        }else{
            unselectedBorder = BorderFactory.createMatteBorder(
                                1,1,1,1, bcDialogue);
            if(t!=null && t.equalsIgnoreCase("null")==false){
                if(t.equalsIgnoreCase("D")==true){
                    unselectedBorder = BorderFactory.createMatteBorder(
                            1,1,1,1, bcDialogue);
                }else if(t.equalsIgnoreCase("K")==true){
                    unselectedBorder = BorderFactory.createMatteBorder(
                            1,1,1,1, bcKaraoke);
                }else if(t.equalsIgnoreCase("#")==true){
                    unselectedBorder = BorderFactory.createMatteBorder(
                            1,1,1,1, bcComment);
                }else if(t.equalsIgnoreCase("P")==true){
                    unselectedBorder = BorderFactory.createMatteBorder(
                            1,1,1,1, bcPicture);
                }else if(t.equalsIgnoreCase("S")==true){
                    unselectedBorder = BorderFactory.createMatteBorder(
                            1,1,1,1, bcAudio);
                }else if(t.equalsIgnoreCase("M")==true){
                    unselectedBorder = BorderFactory.createMatteBorder(
                            1,1,1,1, bcVideo);
                }else if(t.equalsIgnoreCase("C")==true){
                    unselectedBorder = BorderFactory.createMatteBorder(
                            1,1,1,1, bcCommand);
                }
            }else{
                
            }
        setBorder(unselectedBorder);
        }
        
        return this;
    }
    
    /** D�finit la couleur du texte
     * @param type L'�l�ment voulu, voir les champs statiques
     * @param c Une couleur de fond */
    public void setTextColor(int type, Color c){
        switch(type){
            case 0: fcDialogue = c; break;
            case 1: fcKaraoke = c; break;
            case 2: fcComment = c; break;
            case 3: fcPicture = c; break;
            case 4: fcAudio = c; break;
            case 5: fcVideo = c; break;
            case 6: fcCommand = c; break;
        }
    }
    
    /** Obtiend la couleur du texte
     * @param type L'�l�ment voulu, voir les champs statiques */
    public Color getTextColor(int type){
        Color c = null;
        switch(type){
            case 0: c = fcDialogue; break;
            case 1: c = fcKaraoke; break;
            case 2: c = fcComment; break;
            case 3: c = fcPicture; break;
            case 4: c = fcAudio; break;
            case 5: c = fcVideo; break;
            case 6: c = fcCommand; break;
        }
        return c;
    }
    
    /** D�finit la couleur du fond
     * @param type L'�l�ment voulu, voir les champs statiques
     * @param c Une couleur de fond */
    public void setBackColor(int type, Color c){
        switch(type){
            case 0: bcDialogue = c; break;
            case 1: bcKaraoke = c; break;
            case 2: bcComment = c; break;
            case 3: bcPicture = c; break;
            case 4: bcAudio = c; break;
            case 5: bcVideo = c; break;
            case 6: bcCommand = c; break;
        }
    }
    
    /** Obtiend la couleur du fond
     * @param type L'�l�ment voulu, voir les champs statiques */
    public Color getBackColor(int type){
        Color c = null;
        switch(type){
            case 0: c = bcDialogue; break;
            case 1: c = bcKaraoke; break;
            case 2: c = bcComment; break;
            case 3: c = bcPicture; break;
            case 4: c = bcAudio; break;
            case 5: c = bcVideo; break;
            case 6: c = bcCommand; break;
        }
        return c;
    }
    
    /** D�finit la couleur du texte pour toutes les cases
     * @param c Une couleur de texte */
    public void setTextColor(Color c){
        fcDialogue = c;
        fcKaraoke = c;
        fcComment = c;
        fcPicture = c;
        fcAudio = c;
        fcVideo = c;
        fcCommand = c;
    }
    
    /** D�finit la couleur du fond pour toutes les cases
     * @param c Une couleur de fond */
    public void setBackColor(Color c){
        bcDialogue = c;
        bcKaraoke = c;
        bcComment = c;
        bcPicture = c;
        bcAudio = c;
        bcVideo = c;
        bcCommand = c;
    }
    
    /** D�finit la police du texte
     * @param f un objet <b>Font</b> */
    @Override
    public void setFont(Font f){
        fontText = f;
    }
    
    /** D�finit la police du texte
     * @param nom Le nom de la police
     * @param fontStyle Le style de la police, BOLD, ITALIC, PLAIN ou BOLD+ITALIC
     * @param size La taille de police */
    public void setFont(String nom, int fontStyle, int size){
        fontText = new Font(nom,fontStyle,size);
    }
    
    /** Obtiend la police du texte */
    @Override
    public Font getFont(){
        return fontText;
    }
    
    /** <p>Set the TextType</p> */
    public void setTextType(TextType texttype){
        this.texttype = texttype;
    }
    
    /** <p>Get the TextType</p> */
    public TextType getTextType(){
        return texttype;
    }
    
    /** Change the details of edit lines. Display tags,
     * sign or just the strip text. */
    public String withTextRender(TextType tt, String text){
        // Change text as follow :
        // StripAll - clears all tags.
        // Normal - nothing is done.
        // WithItems - replace tags by specials characters.
        String str = "";
        switch(tt){
            case StripAll:
                // Strip text if the text contains edit marks.
                if(text.contains("{\\")){
                    try{
                        str = text.replaceAll("\\{[^\\}]+\\}", "");
                    }catch(Exception e){
                        str = text;
                    }
                }else{
                    str = text;
                }
                break;
            case WithItems:
                // Replace tags by items if the text contains edit marks.
                if(text.contains("{\\")){
                    try{
                        str = text.replaceAll("\\{[^\\}]+\\}", "◆");
                    }catch(Exception e){
                        str = text;
                    }
                }else{
                    str = text;
                }
                break;
            case Normal:
                // Do nothing.
                str = text;
                break;
        }
        
        return str;
    }
    
    public String withLineID(int rowId){
        String str = String.valueOf(rowId+1);
        
        if(str.length()==1){
            str = "00"+str;
        }else if(str.length()==2){
            str = "0"+str;
        }
        
        return str;
    }
}
