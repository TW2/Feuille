/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smallboxforfansub.karaoke.editor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.TableCellEditor;
import smallboxforfansub.karaoke.dialog.AssAlphaDialog;
import smallboxforfansub.karaoke.dialog.DrawingChooserDialog;
import smallboxforfansub.filter.PngFilter;
import smallboxforfansub.karaoke.xfxintegration.*;
import smallboxforfansub.karaoke.xfxintegration.AlignOldString.AlignOld;
import smallboxforfansub.karaoke.xfxintegration.AlignString.Align;
import smallboxforfansub.karaoke.xfxintegration.EncodingString.Encoding;
import smallboxforfansub.karaoke.xfxintegration.WrappingString.Wrapping;
import smallboxforfansub.karaoke.lib.ImagePreview;

/**
 *
 * @author The Wingate 2940
 */
public class xfxintObjectTableEditor extends AbstractCellEditor implements TableCellEditor {

    Object obj = null;
    Color color;
    String str = "";
    FontString fs = null;
    EncodingString es = null;
    WrappingString ws = null;
    AlignOldString aos = null;
    AlignString as = null;
    DrawingString ds = null;
    TransparencyString ts = null;
    ImageString is = null;
    SuperString ss = null;
    
    ActionListener alCOLOR, alDRAWSTRING, alTRANSSTRING, alIMAGESTRING;
    FocusListener flSTRING, flFONTSTRING, flENCOSTRING, flWRAPSTRING, flAOSTRING, flASTRING, flSSTRING;
    CaretListener clSTRING, clSSTRING;
    ItemListener ilFONTSTRING, ilENCOSTRING, ilWRAPSTRING, ilAOSTRING, ilASTRING;
    
    JButton button, buttonDRAW, buttonTRANS, buttonIMAGE;
    JTextField textfield, textfield2;
    JComboBox comboboxFont; DefaultComboBoxModel dcbmFont;
    JComboBox comboboxEnco; DefaultComboBoxModel dcbmEnco;
    JComboBox comboboxWrap; DefaultComboBoxModel dcbmWrap;
    JComboBox comboboxAlignOld; DefaultComboBoxModel dcbmAlignOld;
    JComboBox comboboxAlign; DefaultComboBoxModel dcbmAlign;
    
    DrawingChooserDialog drawingchooser;
    AssAlphaDialog assalpha;
    JFileChooser filechooser;
    JColorChooser colorChooser;
    JDialog dialog;
    
    protected static final String EDIT = "edit";
    
    private Frame frame;
    
    public xfxintObjectTableEditor(Frame parent) {
        frame = parent;
        
        //---------------------------------------------------------------- COLOR
        alCOLOR = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (EDIT.equals(e.getActionCommand())) {
                    //The user has clicked the cell, so bring up the dialog.
                    button.setBackground(color);
                    colorChooser.setColor(color);
                    dialog.setVisible(true);
                    fireEditingStopped(); //Make the renderer reappear.
                } else { //User pressed dialog's "OK" button.
                    color = colorChooser.getColor();
                }
            }
        };
        
        button = new JButton();
        button.setActionCommand(EDIT);
        button.addActionListener(alCOLOR);
        button.setBorderPainted(false);
        
        //Set up the dialog that the button brings up.
        colorChooser = new JColorChooser();
        dialog = JColorChooser.createDialog(button,
                                        "Pick a Color",
                                        true,  //modal
                                        colorChooser,
                                        alCOLOR,  //OK button handler
                                        null); //no CANCEL button handler
        //8888888888888888888888888888888888888888888888888888888888888888 COLOR
        
        //--------------------------------------------------------------- STRING
        flSTRING = new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                textfield.setText(str);
            }
            @Override
            public void focusLost(FocusEvent e) {
//                str = textfield.getText();
            }
        };
        
        clSTRING = new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                str = textfield.getText();
            }
        };
        
        textfield = new JTextField();
        textfield.addFocusListener(flSTRING);
        textfield.addCaretListener(clSTRING);
        //888888888888888888888888888888888888888888888888888888888888888 STRING
        
        //----------------------------------------------------------- FONTSTRING
        flFONTSTRING = new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                comboboxFont.setSelectedItem(fs.getSelectedFont());
            }
            @Override
            public void focusLost(FocusEvent e) {
                //nothing
            }
        };
        
        ilFONTSTRING = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                fs.setSelectedFont((String)comboboxFont.getSelectedItem());
            }
        };
        
        comboboxFont = new JComboBox();
        dcbmFont = new DefaultComboBoxModel();
        comboboxFont.setModel(dcbmFont);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontTable = ge.getAvailableFontFamilyNames();
        for(String s : fontTable){
            dcbmFont.addElement(s);
        }
        comboboxFont.addFocusListener(flFONTSTRING);
        comboboxFont.addItemListener(ilFONTSTRING);
        //88888888888888888888888888888888888888888888888888888888888 FONTSTRING
        
        //------------------------------------------------------- ENCODINGSTRING
        flENCOSTRING = new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                comboboxEnco.setSelectedItem(es.getSelectedEncoding());
            }
            @Override
            public void focusLost(FocusEvent e) {
                //nothing
            }
        };
        
        ilENCOSTRING = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                es.setSelectedEncoding((Encoding)comboboxEnco.getSelectedItem());
            }
        };
        
        comboboxEnco = new JComboBox();
        dcbmEnco = new DefaultComboBoxModel();
        comboboxEnco.setModel(dcbmEnco);
        for(Encoding enc : Encoding.values()){
            dcbmEnco.addElement(enc);
        }
        comboboxEnco.addFocusListener(flENCOSTRING);
        comboboxEnco.addItemListener(ilENCOSTRING);
        //8888888888888888888888888888888888888888888888888888888 ENCODINGSTRING
        
        //------------------------------------------------------- WRAPPINGSTRING
        flWRAPSTRING = new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                comboboxWrap.setSelectedItem(ws.getSelectedWrapping());
            }
            @Override
            public void focusLost(FocusEvent e) {
                //nothing
            }
        };
        
        ilWRAPSTRING = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                ws.setSelectedWrapping((Wrapping)comboboxWrap.getSelectedItem());
            }
        };
        
        comboboxWrap = new JComboBox();
        dcbmWrap = new DefaultComboBoxModel();
        comboboxWrap.setModel(dcbmWrap);
        for(Wrapping wrap : Wrapping.values()){
            dcbmWrap.addElement(wrap);
        }
        comboboxWrap.addFocusListener(flWRAPSTRING);
        comboboxWrap.addItemListener(ilWRAPSTRING);
        //8888888888888888888888888888888888888888888888888888888 WRAPPINGSTRING
        
        //------------------------------------------------------------- AOSTRING
        flAOSTRING = new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                comboboxAlignOld.setSelectedItem(aos.getSelectedAlignOld());
            }
            @Override
            public void focusLost(FocusEvent e) {
                //nothing
            }
        };
        
        ilAOSTRING = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                aos.setSelectedAlignOld((AlignOld)comboboxAlignOld.getSelectedItem());
            }
        };
        
        comboboxAlignOld = new JComboBox();
        dcbmAlignOld = new DefaultComboBoxModel();
        comboboxAlignOld.setModel(dcbmAlignOld);
        for(AlignOld ao : AlignOld.values()){
            dcbmAlignOld.addElement(ao);
        }
        comboboxAlignOld.addFocusListener(flAOSTRING);
        comboboxAlignOld.addItemListener(ilAOSTRING);
        //8888888888888888888888888888888888888888888888888888888888888 AOSTRING
        
        //-------------------------------------------------------------- ASTRING
        flASTRING = new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                comboboxAlign.setSelectedItem(as.getSelectedAlign());
            }
            @Override
            public void focusLost(FocusEvent e) {
                //nothing
            }
        };
        
        ilASTRING = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                as.setSelectedAlign((Align)comboboxAlign.getSelectedItem());
            }
        };
        
        comboboxAlign = new JComboBox();
        dcbmAlign= new DefaultComboBoxModel();
        comboboxAlign.setModel(dcbmAlign);
        for(Align a : Align.values()){
            dcbmAlign.addElement(a);
        }
        comboboxAlign.addFocusListener(flASTRING);
        comboboxAlign.addItemListener(ilASTRING);
        //88888888888888888888888888888888888888888888888888888888888888 ASTRING
        
        //-------------------------------------------------------------- DRAWING
        alDRAWSTRING = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (EDIT.equals(e.getActionCommand())) {
                    //The user has clicked the cell, so bring up the dialog.
                    //drawingchooser.setPath(str);
                    drawingchooser.setLocationRelativeTo(null);
                    ds.setDrawing(drawingchooser.showDialog(ds.getDrawing()));
                    fireEditingStopped(); //Make the renderer reappear.
                }
            }
        };
        
        buttonDRAW = new JButton();
        buttonDRAW.setActionCommand(EDIT);
        buttonDRAW.addActionListener(alDRAWSTRING);
        buttonDRAW.setBorderPainted(false);
        drawingchooser = new DrawingChooserDialog(null, true);
        //88888888888888888888888888888888888888888888888888888888888888 DRAWING
        
        //--------------------------------------------------------- TRANSPARENCY
        alTRANSSTRING = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (EDIT.equals(e.getActionCommand())) {
                    //The user has clicked the cell, so bring up the dialog.
                    //drawingchooser.setPath(str);
                    assalpha.setLocationRelativeTo(null);
                    ts.setTransparency(assalpha.showDialog(ts.getTransparency()));
                    fireEditingStopped(); //Make the renderer reappear.
                }
            }
        };
        
        buttonTRANS = new JButton();
        buttonTRANS.setActionCommand(EDIT);
        buttonTRANS.addActionListener(alTRANSSTRING);
        buttonTRANS.setBorderPainted(false);
        assalpha = new AssAlphaDialog(null, true);
        //888888888888888888888888888888888888888888888888888888888 TRANSPARENCY
        
        //---------------------------------------------------------------- IMAGE
        alIMAGESTRING = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (EDIT.equals(e.getActionCommand())) {
                    //The user has clicked the cell, so bring up the dialog.
                    // Clear the list of file filters.
                    for (FileFilter f : filechooser.getChoosableFileFilters()){
                        filechooser.removeChoosableFileFilter(f);
                    }
                    // Add good file filters.
                    filechooser.addChoosableFileFilter(new PngFilter());
                    filechooser.setAccessory(new ImagePreview(filechooser));
                    int z = filechooser.showOpenDialog(frame);
                    if(z == JFileChooser.APPROVE_OPTION){
                        is.setImage(filechooser.getSelectedFile().getAbsolutePath());
                    }                    
                    fireEditingStopped(); //Make the renderer reappear.
                }
            }
        };
        
        buttonIMAGE = new JButton();
        buttonIMAGE.setActionCommand(EDIT);
        buttonIMAGE.addActionListener(alIMAGESTRING);
        buttonIMAGE.setBorderPainted(false);
        filechooser = new JFileChooser();
        //8888888888888888888888888888888888888888888888888888888888888888 IMAGE
        
        //---------------------------------------------------------- SUPERSTRING
        flSSTRING = new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                textfield2.setText(ss.getSuperString());
            }
            @Override
            public void focusLost(FocusEvent e) {
                //Nothing
            }
        };
        
        clSSTRING = new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                ss.setSuperString(textfield2.getText());
            }
        };
        
        textfield2 = new JTextField();
        textfield2.addFocusListener(flSSTRING);
        textfield2.addCaretListener(clSSTRING);
        //8888888888888888888888888888888888888888888888888888888888 SUPERSTRING
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        //Retourne un objet afin que l'on puisse éditer la valeur avec.
        obj = value;
        
        if(value instanceof Color){
            color = (Color)value;
            return button;
        }else if(value instanceof String){
            str = (String)value;
            return textfield;
        }else if(value instanceof FontString){
            fs = (FontString)value;
            return comboboxFont;
        }else if(value instanceof EncodingString){
            es = (EncodingString)value;
            return comboboxEnco;
        }else if(value instanceof WrappingString){
            ws = (WrappingString)value;
            return comboboxWrap;
        }else if(value instanceof AlignOldString){
            aos = (AlignOldString)value;
            return comboboxAlignOld;
        }else if(value instanceof AlignString){
            as = (AlignString)value;
            return comboboxAlign;
        }else if(value instanceof DrawingString){
            ds = (DrawingString)value;
            return buttonDRAW;
        }else if(value instanceof TransparencyString){
            ts = (TransparencyString)value;
            return buttonTRANS;
        }else if(value instanceof ImageString){
            is = (ImageString)value;
            return buttonIMAGE;
        }else if(value instanceof SuperString){
            ss = (SuperString)value;
            return textfield2;
        }
        
        return button;
    }
    
    @Override
    public Object getCellEditorValue() {
        //Retourne la valeur que l'on vient d'éditer.
        if(obj instanceof Color){
            return color;
        }else if(obj instanceof String){
            return str;
        }else if(obj instanceof FontString){
            return fs;
        }else if(obj instanceof EncodingString){
            return es;
        }else if(obj instanceof WrappingString){
            return ws;
        }else if(obj instanceof AlignOldString){
            return aos;
        }else if(obj instanceof AlignString){
            return as;
        }else if(obj instanceof DrawingString){
            return ds;
        }else if(obj instanceof TransparencyString){
            return ts;
        }else if(obj instanceof ImageString){
            return is;
        }else if(obj instanceof SuperString){
            return ss;
        }
        return obj;
    }
    
}
