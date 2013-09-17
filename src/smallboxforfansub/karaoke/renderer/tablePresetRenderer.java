/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package smallboxforfansub.karaoke.renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.SystemColor;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.border.Border;

/**
 *
 * @author Unknown User
 */
public class tablePresetRenderer extends javax.swing.JLabel
        implements javax.swing.table.TableCellRenderer {

    private Color cFore = Color.black;
    private Color cLayer = Color.white;
    private Color cLayers = Color.lightGray;
    Border unselectedBorder = null;
    Border selectedBorder = null;
    Border selectedBorderWithoutFocus = null;

    Font fontText = new Font("Arial Unicode MS",Font.PLAIN,12);
    // TODO firstlayer
    private int firstLayer = 0;

    public enum Column{
        LAYER(0), COMMANDS(1);

        private int id;

        Column(int id){
            this.id = id;
        }

        public int getId(){
            return id;
        }
    }
    
    public tablePresetRenderer(){
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        
        //Obtient le texte et le transmet au JLabel (renderer)
        String s = (String)value;

//        System.out.println("--------------- COLUMN --> "+column);

        if(column == table.getColumn(Column.COMMANDS.getId()).getModelIndex()){// Show rendered text / Affiche le texte avec le rendu
            setText(withTextRender(s));
        }else if(column == table.getColumn(Column.LAYER.getId()).getModelIndex()){// Show line's number / Affiche le numéro de la ligne
            setText(withLineID(row));
        }else{
            setText(s);
        }

        //Puis d�fini les couleurs d'une case par d�faut
        setForeground(cFore);
        setBackground(isSelected==true? SystemColor.activeCaption : Color.white);

        //D�fini les bordures de la s�lection
        if (isSelected && hasFocus){
            if (selectedBorder == null){
                selectedBorder = BorderFactory.createMatteBorder(
                        1,1,1,1, SystemColor.activeCaption.darker());
            }
        setBorder(selectedBorder);
        }else if (isSelected && !hasFocus){
            if (selectedBorderWithoutFocus == null){
                selectedBorderWithoutFocus = BorderFactory.createMatteBorder(
                        1,1,1,1, SystemColor.activeCaption);
            }
        setBorder(selectedBorderWithoutFocus);
        }else{
            unselectedBorder = BorderFactory.createMatteBorder(
                                1,1,1,1, Color.black);
        setBorder(unselectedBorder);
        }

        return this;

    }

    public String withTextRender(String text){
        String str;
        str = text;
        return str;
    }

    public String withLineID(int rowId){
        String str = String.valueOf(firstLayer+rowId+1);

        if(str.length()==1){
            str = "00"+str;
        }else if(str.length()==2){
            str = "0"+str;
        }

        return str;
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

    private void setFirstLayer(String firstLayer){
        this.firstLayer = Integer.parseInt(firstLayer);
    }

}
