/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.analysis.renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;
import feuille.analysis.lib.LineChangeObject;

/**
 *
 * @author The Wingate 2940
 */
public class CompareRenderer extends JLabel implements TableCellRenderer {
    
    // Couleurs générales
    private Color fcDialogue = Color.black;
    private Color bcDialogue = Color.white;    
    private Color fcNew = new Color(210,255,193).darker();
    private Color bcNew = new Color(210,255,193);
    private Color fcOld = new Color(255,152,142).darker();
    private Color bcOld = new Color(255,152,142);
    
    // Couleur et bordure de selection
    private Color cSelection = new Color(184,207,229);
    private Color bordSelect = new Color(99,130,191);
    
    TextType texttype = TextType.Normal;
    
    Border unselectedBorder = null;
    Border selectedBorder = null;
    Border selectedBorderWithoutFocus = null;

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
    
    public CompareRenderer(){
        setOpaque(true);
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        
        //On obtient le LineChangeObject pour la ligne
        LineChangeObject lco;
        Object o = table.getValueAt(row, 11);
        if(o instanceof String){
            lco = new LineChangeObject();
            table.setValueAt(lco, row, 11);
        }else if(o instanceof LineChangeObject){
            lco = (LineChangeObject)o;
        }else{
            lco = new LineChangeObject();
            table.setValueAt(lco, row, 11);
        }
        
        if(value instanceof String){
            String s = (String)value;
            setFont(getFont().deriveFont(Font.PLAIN));
            
            //Affiche le rendu
            if(column == table.getColumn(Column.TEXT.getId()).getModelIndex()){ //Texte
                setText(withTextRender(texttype,s));
                if(lco.getSentenceState()==LineChangeObject.SentenceState.Added){
                    setBackground(isSelected==true ? cSelection : bcNew);
                }else if(lco.getSentenceState()==LineChangeObject.SentenceState.Deleted){
                    setBackground(isSelected==true ? cSelection : bcOld);
                }else if(lco.getSentenceState()==LineChangeObject.SentenceState.Double){
                    setBackground(isSelected==true ? cSelection : bcDialogue);
                }else{
                    setBackground(isSelected==true ? cSelection : bcDialogue);
                }
            }else if(column == table.getColumn(Column.ID.getId()).getModelIndex()){ //Numéro de ligne
                setText(withLineID(row));
                setBackground(isSelected==true? cSelection : new Color(255,211,231));
            }else if(column == table.getColumn(Column.NAME.getId()).getModelIndex()){ //Nom
                setText(s);
                if(lco.getNameState()==LineChangeObject.NameState.New){
                    setBackground(isSelected==true ? cSelection : bcNew);
                }else if(lco.getNameState()==LineChangeObject.NameState.Old){
                    setBackground(isSelected==true ? cSelection : bcOld);
                }else{
                    setBackground(isSelected==true ? cSelection : bcDialogue);
                }
            }else if(column == table.getColumn(Column.STYLE.getId()).getModelIndex()){ //Style
                setText(s);
                if(lco.getStyleState()==LineChangeObject.StyleState.New){
                    setBackground(isSelected==true ? cSelection : bcNew);
                }else if(lco.getStyleState()==LineChangeObject.StyleState.Old){
                    setBackground(isSelected==true ? cSelection : bcOld);
                }else{
                    setBackground(isSelected==true ? cSelection : bcDialogue);
                }
            }else if(column == table.getColumn(Column.TOTAL.getId()).getModelIndex()){ //Total
                setText(s);
                if(lco.getTimeState()==LineChangeObject.TimeState.Shift){
                    setBackground(isSelected==true ? cSelection : new Color(168,223,255));
                }else if(lco.getTimeState()==LineChangeObject.TimeState.New){
                    setBackground(isSelected==true ? cSelection : bcNew);
                }else if(lco.getTimeState()==LineChangeObject.TimeState.Old){
                    setBackground(isSelected==true ? cSelection : bcOld);
                }else{
                    setBackground(isSelected==true ? cSelection : bcDialogue);
                }
            }else if(column == table.getColumn(Column.START.getId()).getModelIndex()){ //Début
                setText(s);
                if(lco.getTimeState()==LineChangeObject.TimeState.Shift){
                    setBackground(isSelected==true ? cSelection : new Color(168,223,255));
                }else if(lco.getTimeState()==LineChangeObject.TimeState.New){
                    setBackground(isSelected==true ? cSelection : bcNew);
                }else if(lco.getTimeState()==LineChangeObject.TimeState.Old){
                    setBackground(isSelected==true ? cSelection : bcOld);
                }else{
                    setBackground(isSelected==true ? cSelection : bcDialogue);
                }
            }else if(column == table.getColumn(Column.END.getId()).getModelIndex()){ //Fin
                setText(s);
                if(lco.getTimeState()==LineChangeObject.TimeState.Shift){
                    setBackground(isSelected==true ? cSelection : new Color(168,223,255));
                }else if(lco.getTimeState()==LineChangeObject.TimeState.New){
                    setBackground(isSelected==true ? cSelection : bcNew);
                }else if(lco.getTimeState()==LineChangeObject.TimeState.Old){
                    setBackground(isSelected==true ? cSelection : bcOld);
                }else{
                    setBackground(isSelected==true ? cSelection : bcDialogue);
                }
            }else{
                setText(s);
                setBackground(isSelected==true? cSelection : Color.white);
            }
        }
        
//        if(value instanceof LineChangeObject){
//            LineChangeObject lco = (LineChangeObject)value;
//            if(column == table.getColumn(Column.TEXT.getId()).getModelIndex()){ //Texte
//                if(lco.getSentenceState()==LineChangeObject.SentenceState.Added){
//                    setBackground(isSelected==true? cSelection : bcNew);
//                    setForeground(fcNew);
//                }else if(lco.getSentenceState()==LineChangeObject.SentenceState.Deleted){
//                    setBackground(isSelected==true? cSelection : bcOld);
//                    setForeground(fcOld);
//                }                
//            }
//        }
        
        
        
        return this;
    }
    
    public void setTextType(TextType texttype){
        this.texttype = texttype;
    }
    
    public TextType getTextType(){
        return texttype;
    }
    
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
