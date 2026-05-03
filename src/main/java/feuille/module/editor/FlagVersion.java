package feuille.module.editor;

import feuille.util.DialogResult;
import feuille.util.ISO_3166;
import feuille.util.Loader;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FlagVersion extends JPanel {
    private final JTextPane paneSrc;
    private final JTextPane paneDst;

    private final JButton btnVersionsEdit;
    private final JButton btnDisplay1Lock;
    private final JButton btnDisplay2Lock;
    private final JComboBox<ISO_3166> cbDisplay1;
    private final JComboBox<ISO_3166> cbDisplay2;
    private final DefaultComboBoxModel<ISO_3166> display1Model;
    private final DefaultComboBoxModel<ISO_3166> display2Model;

    private final ImageIcon linkOK;
    private final ImageIcon linkNOT;

    public FlagVersion(ISO_3166 iso, JTextPane pSrc, JTextPane pDst){

        linkOK = Loader.fromResource("/images/thin-linkOK.png", 8, 16);
        linkNOT = Loader.fromResource("/images/thin-linkNOT.png", 8, 16);

        btnVersionsEdit = new JButton(Loader.fromResource("/images/16 engrenage mauve.png", 16, 16));

        display1Model = new DefaultComboBoxModel<>();
        display1Model.addElement(ISO_3166.getISO_3166(Locale.getDefault().getISO3Country()));
        cbDisplay1 = new JComboBox<>(display1Model);
        cbDisplay1.setRenderer(new ComboBoxVersionRenderer());
        cbDisplay1.setPreferredSize(new Dimension(60, 22));
        btnDisplay1Lock = new JButton(linkNOT);

        display2Model = new DefaultComboBoxModel<>();
        display2Model.addElement(ISO_3166.getISO_3166(Locale.getDefault().getISO3Country()));
        cbDisplay2 = new JComboBox<>(display2Model);
        cbDisplay2.setRenderer(new ComboBoxVersionRenderer());
        cbDisplay2.setPreferredSize(new Dimension(60, 22));
        btnDisplay2Lock = new JButton(linkNOT);

        paneSrc = pSrc;
        paneDst = pDst;

        JPanel p1 = new JPanel(new BorderLayout());
        p1.add(cbDisplay1, BorderLayout.CENTER);
        p1.add(btnDisplay1Lock, BorderLayout.EAST);

        JPanel p2 = new JPanel(new BorderLayout());
        p2.add(cbDisplay2, BorderLayout.CENTER);
        p2.add(btnDisplay2Lock, BorderLayout.EAST);

        JPanel p3 = new JPanel(new GridLayout(1, 2, 2, 1));
        p3.add(p1);
        p3.add(p2);

        setLayout(new BorderLayout(2,0));
        add(btnVersionsEdit, BorderLayout.WEST);
        add(p3, BorderLayout.CENTER);

        btnVersionsEdit.addActionListener(e -> {
            try{
                VersionsDialog dialog = new VersionsDialog(new Frame(), true, iso);
                final List<ISO_3166> lng = new ArrayList<>();
                for(int i=0; i<display1Model.getSize(); i++){
                    ISO_3166 x = display1Model.getElementAt(i);
                    if(!lng.contains(x)){
                        lng.add(x);
                    }
                }
                dialog.showDialog(lng);
                if(dialog.getDialogResult() == DialogResult.OK){
                    display1Model.removeAllElements();
                    display2Model.removeAllElements();
                    display1Model.addAll(dialog.getList());
                    display2Model.addAll(dialog.getList());
                }
            }catch(Exception ex){
                System.err.println("Exception has occurred at " + e.getSource());
                Logger.getLogger(FlagVersion.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        btnDisplay1Lock.addActionListener(e -> {
            try{
                boolean b = paneSrc.isEditable();
                btnDisplay1Lock.setIcon(b ? linkOK : linkNOT);
                paneSrc.setEditable(!b);
                cbDisplay1.setEnabled(!b);
            }catch(Exception ex){
                System.err.println("Exception has occurred at " + e.getSource());
                Logger.getLogger(FlagVersion.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        btnDisplay2Lock.addActionListener(e -> {
            try{
                boolean b = paneDst.isEditable();
                btnDisplay2Lock.setIcon(b ? linkOK : linkNOT);
                paneDst.setEditable(!b);
                cbDisplay2.setEnabled(!b);
            }catch(Exception ex){
                System.err.println("Exception has occurred at " + e.getSource());
                Logger.getLogger(FlagVersion.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        cbDisplay1.addActionListener(e -> {
            try{

            }catch(Exception ex){
                System.err.println("Exception has occurred at " + e.getSource());
                Logger.getLogger(FlagVersion.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        cbDisplay2.addActionListener(e -> {
            try{

            }catch(Exception ex){
                System.err.println("Exception has occurred at " + e.getSource());
                Logger.getLogger(FlagVersion.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    public static class ComboBoxVersionRenderer extends JPanel implements ListCellRenderer<ISO_3166> {

        private final JLabel lblFlag;

        public ComboBoxVersionRenderer(){
            lblFlag = new JLabel();
            setLayout(new BorderLayout());
            add(lblFlag, BorderLayout.CENTER);
        }

        @Override
        public Component getListCellRendererComponent(
                JList<? extends ISO_3166> list,
                ISO_3166 value,
                int index,
                boolean isSelected,
                boolean cellHasFocus) {

            setForeground(isSelected ?
                    UIManager.getColor("List.selectionForeground") :
                    UIManager.getColor("List.foreground"));

            setBackground(isSelected ?
                    UIManager.getColor("List.selectionBackground") :
                    UIManager.getColor("List.background"));

            try{
                lblFlag.setIcon(Loader.locations(value));
            }catch(Exception _){

            }

            return this;
        }
    }

    public int getCbDisplay1Value() {
        return cbDisplay1.getSelectedIndex();
    }

    public int getCbDisplay2Value() {
        return cbDisplay2.getSelectedIndex();
    }

    public ISO_3166 getFlag1(){
        return display1Model.getElementAt(cbDisplay1.getSelectedIndex());
    }

    public ISO_3166 getFlag2(){
        return display2Model.getElementAt(cbDisplay2.getSelectedIndex());
    }

    public boolean flag2HasMany(){
        return display2Model.getSize() > 0;
    }
}
