/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package feuille.karaoke.renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.SystemColor;
import javax.swing.JTable;

/**
 *
 * @author The Wingate 2940
 */
public class resultTableRenderer extends javax.swing.JLabel
        implements javax.swing.table.TableCellRenderer {

    public enum TextType{
        StripAll, Normal, WithItems;
    }

    TextType texttype = TextType.Normal;
    Font fontText = new Font("Arial Unicode MS",Font.PLAIN,12);

    public resultTableRenderer(boolean opaque) {
        this.setOpaque(opaque);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        //Obtient le texte et le transmet au JLabel (renderer)
        String s = (String)value;
        
        if(column == 10){
            // Show rendered text / Affiche le texte avec le rendu
            this.setText(withTextRender(texttype,s));
        }else if(column == 0){
            // Show line's number / Affiche le numéro de la ligne
            this.setText(withLineID(row));
        }else{
            this.setText(s);
        }

        //Set default colors / Puis d�fini les couleurs d'une case par d�faut
        this.setForeground(isSelected==true? Color.white : Color.black);
        if(isEven(row)==true){
            setBackground(isSelected==true? SystemColor.activeCaption : SystemColor.controlHighlight);
            if(System.getProperty("os.name").equalsIgnoreCase("Mac OS X")){
                Color cActive = new Color(56,117,215);
                Color cHighLight = new Color(208,208,208);
                setBackground(isSelected==true? cActive : cHighLight);
            }
        }else{
            setBackground(isSelected==true? SystemColor.activeCaption : Color.white);
            if(System.getProperty("os.name").equalsIgnoreCase("Mac OS X")){
                Color cActive = new Color(56,117,215);
                setBackground(isSelected==true? cActive : Color.white);
            }
        }
        

        return this;
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
    
    private boolean isEven(int number){
        String s = Integer.toString(number);
        if(s.endsWith("1")){
            return false;
        }else if(s.endsWith("3")){
            return false;
        }else if(s.endsWith("5")){
            return false;
        }else if(s.endsWith("7")){
            return false;
        }else if(s.endsWith("9")){
            return false;
        }else{
            return true;
        }        
    }

}
