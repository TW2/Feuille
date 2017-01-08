/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.analysis;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import feuille.analysis.dialog.AnalyzeDialog;
import feuille.filter.SubtitleFilter;
import feuille.analysis.lib.BarChart;
import feuille.analysis.lib.BarChart3D;
import feuille.analysis.lib.LineChangeObject;
import feuille.analysis.lib.PieChart;
import feuille.analysis.lib.PieChart3D;
import feuille.analysis.renderer.CompareRenderer;
import feuille.karaoke.lib.AssIO;
import feuille.karaoke.lib.AssInfos;
import feuille.karaoke.lib.AssNameCollection;
import feuille.karaoke.lib.AssStyleCollection;
import feuille.karaoke.lib.ProgramLine;
import feuille.lib.Language;

/**
 *
 * @author The Wingate 2940
 */
public class AnalysisPanel extends javax.swing.JPanel {

    private DefaultTableModel firstModel, secondModel;
    private AssStyleCollection firstStyleCollection = new AssStyleCollection();    
    private AssStyleCollection secondStyleCollection = new AssStyleCollection();
    private AssInfos firstInfos = new AssInfos();
    private AssInfos secondInfos = new AssInfos();
    private AssNameCollection firstNameCollection = new AssNameCollection();
    private AssNameCollection secondNameCollection = new AssNameCollection();
    private CompareRenderer firstCR = new CompareRenderer();
    private CompareRenderer secondCR = new CompareRenderer();
    //VLCj vlc = new VLCj();
    private List<ProgramLine> firstSubList = new ArrayList<>();
    private List<ProgramLine> secondSubList = new ArrayList<>();
    //StartFrame sf = new StartFrame();
    //static JLabel lblCurrentTime = new JLabel();
    private Map<String, Integer> firstchartmap = new HashMap<>();
    private Map<String, Integer> secondchartmap = new HashMap<>();  
    
    private Frame frame;
    private Language localeLanguage = feuille.MainFrame.getLanguage();
    
    /**
     * Creates new form AnalysisPanel
     */
    public AnalysisPanel(Frame frame) {
        this.frame = frame;
        initComponents();
        init();
    }
    
    private void init() {
        //Configuration du Look&Feel
        try {
            javax.swing.UIManager.setLookAndFeel(new NimbusLookAndFeel());
            javax.swing.SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception exc) {
            System.out.println("Nimbus LookAndFeel not loaded : "+exc);
        }
        
        //Configuration des tables
        String[] fHead = new String[]{"#", "T", "L", "Marg.", "Start", "End",
                "Total", "Style", "Name", "Effect", "Text", "Change"};
        firstModel = new DefaultTableModel(null,fHead){
            Class[] types = new Class [] {
                    String.class, String.class, String.class,
                    String.class, String.class, String.class,
                    String.class, String.class, String.class,
                    String.class, String.class, LineChangeObject.class};
            boolean[] canEdit = new boolean [] {
                    false, false, false,
                    false, false, false,
                    false, false, false,
                    false, false, false};
            @Override
            public Class getColumnClass(int columnIndex) {return types [columnIndex];}
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {return canEdit [columnIndex];}
        };
        
        firstTable.setModel(firstModel);
        
        TableColumn column;
        for (int i = 0; i < 12; i++) {
            column = firstTable.getColumnModel().getColumn(i);
            switch(i){
                case 0: column.setPreferredWidth(30); column.setIdentifier(Column.ID.getId()); break; //ID
                case 1: column.setPreferredWidth(30); column.setIdentifier(Column.TYPE.getId()); break; //Type
                case 2: column.setPreferredWidth(30); column.setIdentifier(Column.LAYER.getId()); break; //Layer
                case 3: column.setPreferredWidth(60); column.setIdentifier(Column.MARGINS.getId()); break; //Margins
                case 4: column.setPreferredWidth(90); column.setIdentifier(Column.START.getId()); break; //Start
                case 5: column.setPreferredWidth(90); column.setIdentifier(Column.END.getId()); break; //End
                case 6: column.setPreferredWidth(90); column.setIdentifier(Column.TOTAL.getId()); break; //Total time
                case 7: column.setPreferredWidth(80); column.setIdentifier(Column.STYLE.getId()); break; //Style
                case 8: column.setPreferredWidth(80); column.setIdentifier(Column.NAME.getId()); break; //Name
                case 9: column.setPreferredWidth(20); column.setIdentifier(Column.EFFECTS.getId()); break; //Effects
                case 10: column.setPreferredWidth(700); column.setIdentifier(Column.TEXT.getId()); break; //Text
                case 11: column.setPreferredWidth(20); column.setIdentifier(Column.FX.getId()); break; //LineChangeObject
            }
        }
        
        firstTable.setDefaultRenderer(String.class, firstCR);
        firstTable.setDefaultRenderer(LineChangeObject.class, new CompareRenderer());
        
        String[] sHead = new String[]{"#", "T", "L", "Marg.", "Start", "End",
                "Total", "Style", "Name", "Effect", "Text", "Change"};
        secondModel = new DefaultTableModel(null,sHead){
            Class[] types = new Class [] {
                    String.class, String.class, String.class,
                    String.class, String.class, String.class,
                    String.class, String.class, String.class,
                    String.class, String.class, LineChangeObject.class};
            boolean[] canEdit = new boolean [] {
                    false, false, false,
                    false, false, false,
                    false, false, false,
                    false, false, false};
            @Override
            public Class getColumnClass(int columnIndex) {return types [columnIndex];}
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {return canEdit [columnIndex];}
        };
        
        secondTable.setModel(secondModel);
        
        for (int i = 0; i < 12; i++) {
            column = secondTable.getColumnModel().getColumn(i);
            switch(i){
                case 0: column.setPreferredWidth(30); column.setIdentifier(Column.ID.getId()); break; //ID
                case 1: column.setPreferredWidth(30); column.setIdentifier(Column.TYPE.getId()); break; //Type
                case 2: column.setPreferredWidth(30); column.setIdentifier(Column.LAYER.getId()); break; //Layer
                case 3: column.setPreferredWidth(60); column.setIdentifier(Column.MARGINS.getId()); break; //Margins
                case 4: column.setPreferredWidth(90); column.setIdentifier(Column.START.getId()); break; //Start
                case 5: column.setPreferredWidth(90); column.setIdentifier(Column.END.getId()); break; //End
                case 6: column.setPreferredWidth(90); column.setIdentifier(Column.TOTAL.getId()); break; //Total time
                case 7: column.setPreferredWidth(80); column.setIdentifier(Column.STYLE.getId()); break; //Style
                case 8: column.setPreferredWidth(80); column.setIdentifier(Column.NAME.getId()); break; //Name
                case 9: column.setPreferredWidth(20); column.setIdentifier(Column.EFFECTS.getId()); break; //Effects
                case 10: column.setPreferredWidth(700); column.setIdentifier(Column.TEXT.getId()); break; //Text
                case 11: column.setPreferredWidth(20); column.setIdentifier(Column.FX.getId()); break; //LineChangeObject
            }
        }
        
        secondTable.setDefaultRenderer(String.class, secondCR);
        secondTable.setDefaultRenderer(LineChangeObject.class, new CompareRenderer());
        
        //VLC
        //jPanel1.add(vlc);
        
        //CurrentTime label
        //lblCurrentTime.setForeground(Color.magenta);
        //lblCurrentTime.setText("0:00:00.00");
        //jToolBar1.add(lblCurrentTime, 10);
        
        if(localeLanguage.getValueOf("ifrFirstTable")!=null){
            ifrFirstTable.setTitle(localeLanguage.getValueOf("ifrFirstTable"));}
        if(localeLanguage.getValueOf("ifrSecondTable")!=null){
            ifrSecondTable.setTitle(localeLanguage.getValueOf("ifrSecondTable"));} 
        if(localeLanguage.getValueOf("ifrFirstReport")!=null){
            ifrFirstReport.setTitle(localeLanguage.getValueOf("ifrFirstReport"));} 
        if(localeLanguage.getValueOf("ifrSecondReport")!=null){
            ifrSecondReport.setTitle(localeLanguage.getValueOf("ifrSecondReport"));}        
        if(localeLanguage.getValueOf("toolOpen")!=null){
            btnOpenOld.setToolTipText(localeLanguage.getValueOf("toolOpen"));
            btnOpenNew.setToolTipText(localeLanguage.getValueOf("toolOpen"));}
        if(localeLanguage.getValueOf("toolAnalysis")!=null){
            btnAnalysis.setToolTipText(localeLanguage.getValueOf("toolAnalysis"));}
        if(localeLanguage.getValueOf("toolNormal")!=null){
            tbOldNormal.setToolTipText(localeLanguage.getValueOf("toolNormal"));
            tbNewNormal.setToolTipText(localeLanguage.getValueOf("toolNormal"));}
        if(localeLanguage.getValueOf("toolItems")!=null){
            tbOldItems.setToolTipText(localeLanguage.getValueOf("toolItems"));
            tbNewItems.setToolTipText(localeLanguage.getValueOf("toolItems"));}
        if(localeLanguage.getValueOf("toolStrip")!=null){
            tbOldStripAll.setToolTipText(localeLanguage.getValueOf("toolStrip"));
            tbNewStripAll.setToolTipText(localeLanguage.getValueOf("toolStrip"));}
    }
    
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
    
    public JInternalFrame getIfrFirstTable(){
        return ifrFirstTable;
    }
    
    public JInternalFrame getIfrFirstReport(){
        return ifrFirstReport;
    }
    
    public JInternalFrame getIfrSecondTable(){
        return ifrSecondTable;
    }
    
    public JInternalFrame getIfrSecondReport(){
        return ifrSecondReport;
    }
    
    private void updateChartPanel(JPanel chartPanel, List<Object> objectToAdd){
        //Enlève tous les objets
        chartPanel.removeAll();
        //Création d'une scrollbar
        final JScrollBar sb = new JScrollBar();
        //Redimensionnement et configuration du panel conteneur
        JPanel pcontainer = new JPanel(null);
        pcontainer.setBackground(Color.white);
        //Redimensionnement et configuration du panel cible
        final JPanel ptarget = new JPanel(null);
        ptarget.setSize(500, objectToAdd.size()*250);
        //Configuration du panel source
        chartPanel.setLayout(new BorderLayout());
        chartPanel.add(sb, BorderLayout.EAST);
        chartPanel.add(pcontainer, BorderLayout.CENTER);
        //Configuration du panel conteneur
        pcontainer.add(ptarget);
        
        ptarget.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if(e.getWheelRotation()>0){
                    sb.setValue(sb.getValue()+100);
                }else{
                    sb.setValue(sb.getValue()-100);
                }
            }
        });
        
        sb.setVisible(true);
        sb.setMaximum(ptarget.getHeight());//(-spFirstData.getHeight()+20)
        sb.addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                ptarget.setLocation(0, -e.getValue());
            }
        });
        
        //Ajout des éléments
        int x = 0, y = 0;
        for(Object o : objectToAdd){
            if(o instanceof BarChart){
                BarChart bc = (BarChart)o;
                ptarget.add(bc);
                bc.setLocation(x, y);
                bc.setSize(500, 250);
                y += 250;
            }
            if(o instanceof BarChart3D){
                BarChart3D bc3D = (BarChart3D)o;
                ptarget.add(bc3D);
                bc3D.setLocation(x, y);
                bc3D.setSize(500, 250);
                y += 250;
            }
            if(o instanceof PieChart){
                PieChart pc = (PieChart)o;
                ptarget.add(pc);
                pc.setLocation(x, y);
                pc.setSize(500, 250);
                y += 250;
            }
            if(o instanceof PieChart3D){
                PieChart3D pc3D = (PieChart3D)o;
                ptarget.add(pc3D);
                pc3D.setLocation(x, y);
                pc3D.setSize(500, 250);
                y += 250;
            }
        }        
    }
    
    //<editor-fold defaultstate="collapsed" desc=" Méthodes de comparaison et de recherche ">
    private void compareBySentence(){
        //On veut identifier les phrases en double (ou plus), c'est pour ça que
        //l'on va utiliser des Map afin d'avoir comme clé les phrases et comme
        //valeur leur nombre de fois dans le script
        Map<String, Integer> firstMap = new HashMap<String, Integer>();
        Map<String, Integer> secondMap = new HashMap<String, Integer>();
        
        //Traitement - Crée un rapport des changements -------------------------
        firstchartmap.clear(); secondchartmap.clear();
        //----------------------------------------------------------------------
        
        // On recherche les vieilles phrases
        for(int i=0;i<firstModel.getRowCount();i++){
            String firstSentence = (String)firstModel.getValueAt(i, 10);
            String firstStyle = (String)firstModel.getValueAt(i, 7);
            String firstName = (String)firstModel.getValueAt(i, 8);
            String firstStart = (String)firstModel.getValueAt(i, 4);
            String firstEnd = (String)firstModel.getValueAt(i, 5);
            String firstTotal = (String)firstModel.getValueAt(i, 6);
            LineChangeObject firstLCO = (LineChangeObject)firstModel.getValueAt(i, 11);
            
            //Traitement - Nombre de fois dans le script -----------------------
            if(firstMap.containsKey(firstSentence)){
                //On trouve une clé identique, on extrait le nombre de fois
                //puis on le modifie pour rerentrer la clé
                int value = firstMap.get(firstSentence);
                firstMap.put(firstSentence, value+1);
            }else{
                //On ne trouve pas cette clé, donc on l'ajoute
                firstMap.put(firstSentence, 1);
            }
            //------------------------------------------------------------------
            
            for(int j=0;j<secondModel.getRowCount();j++){
                String secondSentence = (String)secondModel.getValueAt(j, 10);
                String secondStyle = (String)secondModel.getValueAt(j, 7);
                String secondName = (String)secondModel.getValueAt(j, 8);
                String secondStart = (String)secondModel.getValueAt(j, 4);
                String secondEnd = (String)secondModel.getValueAt(j, 5);
                String secondTotal = (String)secondModel.getValueAt(j, 6);
                LineChangeObject secondLCO = (LineChangeObject)secondModel.getValueAt(j, 11);
                
                //Traitement - Nombre de fois dans le script -------------------
                if(secondMap.containsKey(secondSentence)){
                    //On trouve une clé identique, on extrait le nombre de fois
                    //puis on le modifie pour rerentrer la clé
                    int value = secondMap.get(secondSentence);
                    secondMap.put(secondSentence, value+1);
                }else{
                    //On ne trouve pas cette clé, donc on l'ajoute
                    secondMap.put(secondSentence, 1);
                }
                //--------------------------------------------------------------
                
                // Si la phrase est la même que la phrase recherchée alors elle
                // apparaît en double
                if(firstSentence.equals(secondSentence)){
                    firstLCO.changeSentenceState(LineChangeObject.SentenceState.Double);
                    secondLCO.changeSentenceState(LineChangeObject.SentenceState.Double);
                    //Traitement - Crée un rapport des changements -------------
                    incrementChartMap(firstchartmap, "Same lines");
                    incrementChartMap(secondchartmap, "Same lines");
                    //----------------------------------------------------------
                }
                
                // Si la phrase est en double, on regarde si des choses ont changé ou pas
                if(firstLCO.getSentenceState()==LineChangeObject.SentenceState.Double && firstLCO.getSentenceState()==secondLCO.getSentenceState()
                        && firstMap.get(firstSentence)==secondMap.get(secondSentence) && firstSentence.equals(secondSentence)){
                    // Cas 1 : Le style est le même, on change rien
                    // Cas 2 : Le style a changé, on répercute les changements
                    if(firstStyle.equals(secondStyle)==false && firstLCO.getStyleState()==LineChangeObject.StyleState.Unchanged){
                        firstLCO.changeStyleState(LineChangeObject.StyleState.Old);
                        secondLCO.changeStyleState(LineChangeObject.StyleState.New);
                        //Traitement - Crée un rapport des changements ---------
                        incrementChartMap(firstchartmap, "Old styles");
                        incrementChartMap(secondchartmap, "New styles");
                        //------------------------------------------------------
                    }
                    // Cas 1 : Le nom est le même, on change rien
                    // Cas 2 : Le nom a changé, on répercute les changements
                    if(firstName.equals(secondName)==false && firstLCO.getNameState()==LineChangeObject.NameState.Unchanged){
                        firstLCO.changeNameState(LineChangeObject.NameState.Old);
                        secondLCO.changeNameState(LineChangeObject.NameState.New);
                        //Traitement - Crée un rapport des changements ---------
                        incrementChartMap(firstchartmap, "Old names");
                        incrementChartMap(secondchartmap, "New names");
                        //------------------------------------------------------
                    }
                    // Cas 1 : Le temps total n'a pas changé, ni début et fin, on fait rien
                    // Cas 2 : Le temps total n'a pas changé, sauf le début et la fin, on répercute un décalage
                    if(firstTotal.equals(secondTotal) && firstStart.equals(secondStart)==false && firstLCO.getTimeState()==LineChangeObject.TimeState.Unchanged){
                        firstLCO.changeTimeState(LineChangeObject.TimeState.Shift);
                        secondLCO.changeTimeState(LineChangeObject.TimeState.Shift);
                        //Traitement - Crée un rapport des changements ---------
                        incrementChartMap(firstchartmap, "Synch. shifts");
                        incrementChartMap(secondchartmap, "Synch. shifts");
                        //------------------------------------------------------
                    }
                    // Cas 3 : Le temps total a changé
                    if(firstTotal.equals(secondTotal)==false && firstLCO.getTimeState()==LineChangeObject.TimeState.Unchanged){
                        firstLCO.changeTimeState(LineChangeObject.TimeState.Old);
                        secondLCO.changeTimeState(LineChangeObject.TimeState.New);
                        //Traitement - Crée un rapport des changements ---------
                        incrementChartMap(firstchartmap, "Old synch.");
                        incrementChartMap(secondchartmap, "New synch.");
                        //------------------------------------------------------
                    }
                }
                secondModel.setValueAt(secondLCO, j, 11);
            }
            // Si la phrase recherché est encore au status sentence unknown alors
            // c'est qu'elle est vieille et qu'elle a été supprimée dans le nouveau script
            if(firstLCO.getSentenceState()==LineChangeObject.SentenceState.Unknown){
                firstLCO.changeSentenceState(LineChangeObject.SentenceState.Deleted);
                //Traitement - Crée un rapport des changements -----------------
                incrementChartMap(firstchartmap, "Deleted lines");
                //--------------------------------------------------------------
            }
            firstModel.setValueAt(firstLCO, i, 11);
            
            //Initialise la seconde map (pour un bon recomptage)
            secondMap.clear();
        }
        // On recherche les nouvelles phrases
        for(int i=0;i<secondModel.getRowCount();i++){
            LineChangeObject secondLCO = (LineChangeObject)secondModel.getValueAt(i, 11);
            if(secondLCO.getSentenceState()==LineChangeObject.SentenceState.Unknown){
                secondLCO.changeSentenceState(LineChangeObject.SentenceState.Added);
                //Traitement - Crée un rapport des changements -----------------
                incrementChartMap(secondchartmap, "Added lines");
                //--------------------------------------------------------------
            }
            secondModel.setValueAt(secondLCO, i, 11);
        }
        
        firstTable.repaint();
        secondTable.repaint();
        firstTable.updateUI();
        secondTable.updateUI();
    }
    
    private void compareByStartTime(){
        //On veut identifier les temps en double (ou plus), c'est pour ça que
        //l'on va utiliser des Map afin d'avoir comme clé les temps et comme
        //valeur leur nombre de fois dans le script
        Map<String, Integer> firstMap = new HashMap<String, Integer>();
        Map<String, Integer> secondMap = new HashMap<String, Integer>();
        
        //Traitement - Crée un rapport des changements -------------------------
        firstchartmap.clear(); secondchartmap.clear();
        //----------------------------------------------------------------------
        
        // On recherche les vieilles phrases
        for(int i=0;i<firstModel.getRowCount();i++){
            String firstSentence = (String)firstModel.getValueAt(i, 10);
            String firstStyle = (String)firstModel.getValueAt(i, 7);
            String firstName = (String)firstModel.getValueAt(i, 8);
            String firstStart = (String)firstModel.getValueAt(i, 4);
            String firstEnd = (String)firstModel.getValueAt(i, 5);
            String firstTotal = (String)firstModel.getValueAt(i, 6);
            LineChangeObject firstLCO = (LineChangeObject)firstModel.getValueAt(i, 11);
            
            //Traitement - Nombre de fois dans le script -----------------------
            if(firstMap.containsKey(firstStart)){
                //On trouve une clé identique, on extrait le nombre de fois
                //puis on le modifie pour rerentrer la clé
                int value = firstMap.get(firstStart);
                firstMap.put(firstStart, value+1);
            }else{
                //On ne trouve pas cette clé, donc on l'ajoute
                firstMap.put(firstStart, 1);
            }
            //------------------------------------------------------------------
            
            for(int j=0;j<secondModel.getRowCount();j++){
                String secondSentence = (String)secondModel.getValueAt(j, 10);
                String secondStyle = (String)secondModel.getValueAt(j, 7);
                String secondName = (String)secondModel.getValueAt(j, 8);
                String secondStart = (String)secondModel.getValueAt(j, 4);
                String secondEnd = (String)secondModel.getValueAt(j, 5);
                String secondTotal = (String)secondModel.getValueAt(j, 6);
                LineChangeObject secondLCO = (LineChangeObject)secondModel.getValueAt(j, 11);
                
                //Traitement - Nombre de fois dans le script -------------------
                if(secondMap.containsKey(secondStart)){
                    //On trouve une clé identique, on extrait le nombre de fois
                    //puis on le modifie pour rerentrer la clé
                    int value = secondMap.get(secondStart);
                    secondMap.put(secondStart, value+1);
                }else{
                    //On ne trouve pas cette clé, donc on l'ajoute
                    secondMap.put(secondStart, 1);
                }
                //--------------------------------------------------------------
                
                // Si le temps est la même que la phrase recherchée alors elle
                // apparaît en double
                if(firstStart.equals(secondStart) && firstEnd.equals(secondEnd)){
                    firstLCO.changeTimeState(LineChangeObject.TimeState.Double);
                    secondLCO.changeTimeState(LineChangeObject.TimeState.Double);
                    //Traitement - Crée un rapport des changements -------------
                    incrementChartMap(firstchartmap, "Same synch.");
                    incrementChartMap(secondchartmap, "Same synch.");
                    //----------------------------------------------------------
                }
                
                // Si la phrase est en double, on regarde si des choses ont changé ou pas
                if(firstLCO.getTimeState()==LineChangeObject.TimeState.Double && firstLCO.getTimeState()==secondLCO.getTimeState()
                        && firstMap.get(firstStart)==secondMap.get(secondStart) && firstStart.equals(secondStart) && firstEnd.equals(secondEnd)){
                    // Cas 1 : Le style est le même, on change rien
                    // Cas 2 : Le style a changé, on répercute les changements
                    if(firstStyle.equals(secondStyle)==false && firstLCO.getStyleState()==LineChangeObject.StyleState.Unchanged){
                        firstLCO.changeStyleState(LineChangeObject.StyleState.Old);
                        secondLCO.changeStyleState(LineChangeObject.StyleState.New);
                        //Traitement - Crée un rapport des changements ---------
                        incrementChartMap(firstchartmap, "Old styles");
                        incrementChartMap(secondchartmap, "New styles");
                        //------------------------------------------------------
                    }
                    // Cas 1 : Le nom est le même, on change rien
                    // Cas 2 : Le nom a changé, on répercute les changements
                    if(firstName.equals(secondName)==false && firstLCO.getNameState()==LineChangeObject.NameState.Unchanged){
                        firstLCO.changeNameState(LineChangeObject.NameState.Old);
                        secondLCO.changeNameState(LineChangeObject.NameState.New);
                        //Traitement - Crée un rapport des changements ---------
                        incrementChartMap(firstchartmap, "Old names");
                        incrementChartMap(secondchartmap, "New names");
                        //------------------------------------------------------
                    }
                    // Cas 1 : La phrase n'a pas changé, on ne fait rien
                    // Cas 2 : La phrase a changé, on répercute ce changement
                    if(firstSentence.equals(secondSentence)==false && firstLCO.getSentenceState()==LineChangeObject.SentenceState.Unknown){
                        firstLCO.changeSentenceState(LineChangeObject.SentenceState.Deleted);
                        secondLCO.changeSentenceState(LineChangeObject.SentenceState.Added);
                        //Traitement - Crée un rapport des changements ---------
                        incrementChartMap(firstchartmap, "Deleted lines");
                        incrementChartMap(secondchartmap, "Added lines");
                        //------------------------------------------------------
                    }
                }
                secondModel.setValueAt(secondLCO, j, 11);
            }
            // Si le temps recherché est encore au status sentence unchanged alors
            // c'est qu'il est vieux et qu'il a été supprimé dans le nouveau script
            if(firstLCO.getTimeState()==LineChangeObject.TimeState.Unchanged){
                firstLCO.changeTimeState(LineChangeObject.TimeState.Old);
                //Traitement - Crée un rapport des changements -----------------
                incrementChartMap(firstchartmap, "Old synch.");
                //--------------------------------------------------------------
            }
            firstModel.setValueAt(firstLCO, i, 11);
            
            //Initialise la seconde map (pour un bon recomptage)
            secondMap.clear();
        }
        // On recherche les nouveaux temps
        for(int i=0;i<secondModel.getRowCount();i++){
            LineChangeObject secondLCO = (LineChangeObject)secondModel.getValueAt(i, 11);
            if(secondLCO.getTimeState()==LineChangeObject.TimeState.Unchanged){
                secondLCO.changeTimeState(LineChangeObject.TimeState.New);
                //Traitement - Crée un rapport des changements -----------------
                incrementChartMap(secondchartmap, "New synch.");
                //--------------------------------------------------------------
            }
            secondModel.setValueAt(secondLCO, i, 11);
        }
        
        firstTable.repaint();
        secondTable.repaint();
        firstTable.updateUI();
        secondTable.updateUI();
    }
    
    private void incrementChartMap(Map<String, Integer> chartmap, String key){
        if(chartmap.containsKey(key)){
            //On trouve une clé identique, on extrait le nombre de fois
            //puis on le modifie pour rerentrer la clé
            int kvalue = chartmap.get(key);
            chartmap.put(key, kvalue+1);
        }else{
            //On ne trouve pas cette clé, donc on l'ajoute
            chartmap.put(key, 1);
        }
    }
    
    private Map<String, Integer> searchForWord(String word){
        Map<String, Integer> map = new HashMap<String, Integer>();
        
        for(int i=0;i<firstModel.getRowCount();i++){
            String firstSentence = (String)firstModel.getValueAt(i, 10);
            
            if(firstSentence.contains(word)){
                Pattern p = Pattern.compile("\\s"+word+"\\s");
                Matcher m = p.matcher(firstSentence);
                while(m.find()){
                    incrementChartMap(map, "Before");
                }
            }
        }
        
        for(int i=0;i<secondModel.getRowCount();i++){
            String secondSentence = (String)secondModel.getValueAt(i, 10);
            
            if(secondSentence.contains(word)){
                Pattern p = Pattern.compile("\\s"+word+"\\s");
                Matcher m = p.matcher(secondSentence);
                while(m.find()){
                    incrementChartMap(map, "After");
                }
            }
        }
        
        return map;
    }
    //</editor-fold>
    
    private void setupLineChangeObject(){
        for(int i=0;i<firstModel.getRowCount();i++){
            Object o = firstModel.getValueAt(i, 11);
            if(o.getClass() != LineChangeObject.class){
                firstModel.setValueAt(new LineChangeObject(), i, 11);
            }
        }
        for(int i=0;i<secondModel.getRowCount();i++){
            Object o = secondModel.getValueAt(i, 11);
            if(o.getClass() != LineChangeObject.class){
                secondModel.setValueAt(new LineChangeObject(), i, 11);
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bgOld = new javax.swing.ButtonGroup();
        bgNew = new javax.swing.ButtonGroup();
        fcASS = new javax.swing.JFileChooser();
        jDesktopPane1 = new javax.swing.JDesktopPane();
        ifrFirstTable = new javax.swing.JInternalFrame();
        jToolBar1 = new javax.swing.JToolBar();
        btnOpenOld = new javax.swing.JButton();
        btnAnalysis = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        tbOldNormal = new javax.swing.JToggleButton();
        tbOldItems = new javax.swing.JToggleButton();
        tbOldStripAll = new javax.swing.JToggleButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        firstTable = new javax.swing.JTable();
        ifrSecondTable = new javax.swing.JInternalFrame();
        jToolBar2 = new javax.swing.JToolBar();
        btnOpenNew = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        tbNewNormal = new javax.swing.JToggleButton();
        tbNewItems = new javax.swing.JToggleButton();
        tbNewStripAll = new javax.swing.JToggleButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        secondTable = new javax.swing.JTable();
        ifrFirstReport = new javax.swing.JInternalFrame();
        firstChartPanel = new javax.swing.JPanel();
        ifrSecondReport = new javax.swing.JInternalFrame();
        secondChartPanel = new javax.swing.JPanel();

        ifrFirstTable.setIconifiable(true);
        ifrFirstTable.setMaximizable(true);
        ifrFirstTable.setResizable(true);
        ifrFirstTable.setTitle("Ancient");
        ifrFirstTable.setVisible(true);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        btnOpenOld.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/32px-Crystal_Clear_filesystem_folder_grey_open.png"))); // NOI18N
        btnOpenOld.setFocusable(false);
        btnOpenOld.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenOld.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnOpenOld.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenOldActionPerformed(evt);
            }
        });
        jToolBar1.add(btnOpenOld);

        btnAnalysis.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/32px-Crystal_Clear_app_kdvi.png"))); // NOI18N
        btnAnalysis.setFocusable(false);
        btnAnalysis.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAnalysis.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAnalysis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnalysisActionPerformed(evt);
            }
        });
        jToolBar1.add(btnAnalysis);
        jToolBar1.add(jSeparator1);

        bgOld.add(tbOldNormal);
        tbOldNormal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/AFM-NormalMode.png"))); // NOI18N
        tbOldNormal.setSelected(true);
        tbOldNormal.setFocusable(false);
        tbOldNormal.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        tbOldNormal.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        tbOldNormal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbOldNormalActionPerformed(evt);
            }
        });
        jToolBar1.add(tbOldNormal);

        bgOld.add(tbOldItems);
        tbOldItems.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/AFM-ItemsMode.png"))); // NOI18N
        tbOldItems.setFocusable(false);
        tbOldItems.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        tbOldItems.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        tbOldItems.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbOldItemsActionPerformed(evt);
            }
        });
        jToolBar1.add(tbOldItems);

        bgOld.add(tbOldStripAll);
        tbOldStripAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/AFM-StripMode.png"))); // NOI18N
        tbOldStripAll.setFocusable(false);
        tbOldStripAll.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        tbOldStripAll.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        tbOldStripAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbOldStripAllActionPerformed(evt);
            }
        });
        jToolBar1.add(tbOldStripAll);

        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        firstTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(firstTable);

        javax.swing.GroupLayout ifrFirstTableLayout = new javax.swing.GroupLayout(ifrFirstTable.getContentPane());
        ifrFirstTable.getContentPane().setLayout(ifrFirstTableLayout);
        ifrFirstTableLayout.setHorizontalGroup(
            ifrFirstTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 474, Short.MAX_VALUE)
        );
        ifrFirstTableLayout.setVerticalGroup(
            ifrFirstTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ifrFirstTableLayout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE))
        );

        jDesktopPane1.add(ifrFirstTable);
        ifrFirstTable.setBounds(10, 10, 490, 220);

        ifrSecondTable.setIconifiable(true);
        ifrSecondTable.setMaximizable(true);
        ifrSecondTable.setResizable(true);
        ifrSecondTable.setTitle("Nouveau");
        ifrSecondTable.setVisible(true);

        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);

        btnOpenNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/32px-Crystal_Clear_filesystem_folder_grey_open.png"))); // NOI18N
        btnOpenNew.setFocusable(false);
        btnOpenNew.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenNew.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnOpenNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenNewActionPerformed(evt);
            }
        });
        jToolBar2.add(btnOpenNew);
        jToolBar2.add(jSeparator2);

        bgNew.add(tbNewNormal);
        tbNewNormal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/AFM-NormalMode.png"))); // NOI18N
        tbNewNormal.setSelected(true);
        tbNewNormal.setFocusable(false);
        tbNewNormal.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        tbNewNormal.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        tbNewNormal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbNewNormalActionPerformed(evt);
            }
        });
        jToolBar2.add(tbNewNormal);

        bgNew.add(tbNewItems);
        tbNewItems.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/AFM-ItemsMode.png"))); // NOI18N
        tbNewItems.setFocusable(false);
        tbNewItems.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        tbNewItems.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        tbNewItems.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbNewItemsActionPerformed(evt);
            }
        });
        jToolBar2.add(tbNewItems);

        bgNew.add(tbNewStripAll);
        tbNewStripAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/AFM-StripMode.png"))); // NOI18N
        tbNewStripAll.setFocusable(false);
        tbNewStripAll.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        tbNewStripAll.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        tbNewStripAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbNewStripAllActionPerformed(evt);
            }
        });
        jToolBar2.add(tbNewStripAll);

        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        secondTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(secondTable);

        javax.swing.GroupLayout ifrSecondTableLayout = new javax.swing.GroupLayout(ifrSecondTable.getContentPane());
        ifrSecondTable.getContentPane().setLayout(ifrSecondTableLayout);
        ifrSecondTableLayout.setHorizontalGroup(
            ifrSecondTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 474, Short.MAX_VALUE)
        );
        ifrSecondTableLayout.setVerticalGroup(
            ifrSecondTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ifrSecondTableLayout.createSequentialGroup()
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE))
        );

        jDesktopPane1.add(ifrSecondTable);
        ifrSecondTable.setBounds(10, 240, 490, 210);

        ifrFirstReport.setIconifiable(true);
        ifrFirstReport.setMaximizable(true);
        ifrFirstReport.setResizable(true);
        ifrFirstReport.setTitle("Avant");
        ifrFirstReport.setVisible(true);

        javax.swing.GroupLayout firstChartPanelLayout = new javax.swing.GroupLayout(firstChartPanel);
        firstChartPanel.setLayout(firstChartPanelLayout);
        firstChartPanelLayout.setHorizontalGroup(
            firstChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 514, Short.MAX_VALUE)
        );
        firstChartPanelLayout.setVerticalGroup(
            firstChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 191, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout ifrFirstReportLayout = new javax.swing.GroupLayout(ifrFirstReport.getContentPane());
        ifrFirstReport.getContentPane().setLayout(ifrFirstReportLayout);
        ifrFirstReportLayout.setHorizontalGroup(
            ifrFirstReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(firstChartPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        ifrFirstReportLayout.setVerticalGroup(
            ifrFirstReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(firstChartPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jDesktopPane1.add(ifrFirstReport);
        ifrFirstReport.setBounds(510, 10, 530, 220);

        ifrSecondReport.setIconifiable(true);
        ifrSecondReport.setMaximizable(true);
        ifrSecondReport.setResizable(true);
        ifrSecondReport.setTitle("Après");
        ifrSecondReport.setVisible(true);

        javax.swing.GroupLayout secondChartPanelLayout = new javax.swing.GroupLayout(secondChartPanel);
        secondChartPanel.setLayout(secondChartPanelLayout);
        secondChartPanelLayout.setHorizontalGroup(
            secondChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 514, Short.MAX_VALUE)
        );
        secondChartPanelLayout.setVerticalGroup(
            secondChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 181, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout ifrSecondReportLayout = new javax.swing.GroupLayout(ifrSecondReport.getContentPane());
        ifrSecondReport.getContentPane().setLayout(ifrSecondReportLayout);
        ifrSecondReportLayout.setHorizontalGroup(
            ifrSecondReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(secondChartPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        ifrSecondReportLayout.setVerticalGroup(
            ifrSecondReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(secondChartPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jDesktopPane1.add(ifrSecondReport);
        ifrSecondReport.setBounds(510, 240, 530, 210);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1053, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 654, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tbOldNormalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tbOldNormalActionPerformed
        firstCR.setTextType(CompareRenderer.TextType.Normal);
        firstTable.updateUI();
    }//GEN-LAST:event_tbOldNormalActionPerformed

    private void tbOldItemsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tbOldItemsActionPerformed
        firstCR.setTextType(CompareRenderer.TextType.WithItems);
        firstTable.updateUI();
    }//GEN-LAST:event_tbOldItemsActionPerformed

    private void tbOldStripAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tbOldStripAllActionPerformed
        firstCR.setTextType(CompareRenderer.TextType.StripAll);
        firstTable.updateUI();
    }//GEN-LAST:event_tbOldStripAllActionPerformed

    private void tbNewNormalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tbNewNormalActionPerformed
        secondCR.setTextType(CompareRenderer.TextType.Normal);
        secondTable.updateUI();
    }//GEN-LAST:event_tbNewNormalActionPerformed

    private void tbNewItemsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tbNewItemsActionPerformed
        secondCR.setTextType(CompareRenderer.TextType.WithItems);
        secondTable.updateUI();
    }//GEN-LAST:event_tbNewItemsActionPerformed

    private void tbNewStripAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tbNewStripAllActionPerformed
        secondCR.setTextType(CompareRenderer.TextType.StripAll);
        secondTable.updateUI();
    }//GEN-LAST:event_tbNewStripAllActionPerformed

    private void btnOpenOldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenOldActionPerformed
        //Effacage du tableau
        try{
            for (int i=firstModel.getRowCount()-1;i>=0;i--){
                firstModel.removeRow(i);
            }
            firstSubList.clear();
            //vlc.setSubLists(firstSubList, secondSubList);
        }catch(Exception exc){}
        //Remplissage du tableau
        for (FileFilter f : fcASS.getChoosableFileFilters()){
            fcASS.removeChoosableFileFilter(f);
        }
        fcASS.addChoosableFileFilter(new SubtitleFilter());
        fcASS.setAccessory(null);
        int z = this.fcASS.showOpenDialog(this);
        if (z == javax.swing.JFileChooser.APPROVE_OPTION){
            firstStyleCollection = new AssStyleCollection();

            // Open file
            if(fcASS.getSelectedFile().getName().endsWith("ssa")){
                AssIO aio = new AssIO();
                aio.LireFichierSSAi2(fcASS.getSelectedFile().getAbsolutePath(),
                        firstModel,firstInfos,firstStyleCollection,firstNameCollection,false);
                aio.LireFichierSSAi2_Minimal(fcASS.getSelectedFile().getAbsolutePath(), firstSubList);
                //setFirstInfos();
            }
            if(fcASS.getSelectedFile().getName().endsWith("ass")){
                AssIO aio = new AssIO();
                aio.LireFichierASSi2(fcASS.getSelectedFile().getAbsolutePath(),
                        firstModel,firstInfos,firstStyleCollection,firstNameCollection,false);
                aio.LireFichierASSi2_Minimal(fcASS.getSelectedFile().getAbsolutePath(), firstSubList);
                //setFirstInfos();
            }
        }
    }//GEN-LAST:event_btnOpenOldActionPerformed

    private void btnOpenNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenNewActionPerformed
        //Effacage du tableau
        try{
            for (int i=secondModel.getRowCount()-1;i>=0;i--){
                secondModel.removeRow(i);
            }
            secondSubList.clear();
            //vlc.setSubLists(firstSubList, secondSubList);
        }catch(Exception exc){}
        //Remplissage du tableau
        for (FileFilter f : fcASS.getChoosableFileFilters()){
            fcASS.removeChoosableFileFilter(f);
        }
        fcASS.addChoosableFileFilter(new SubtitleFilter());
        fcASS.setAccessory(null);
        int z = this.fcASS.showOpenDialog(this);
        if (z == javax.swing.JFileChooser.APPROVE_OPTION){
            secondStyleCollection = new AssStyleCollection();

            // Open file
            if(fcASS.getSelectedFile().getName().endsWith("ssa")){
                AssIO aio = new AssIO();
                aio.LireFichierSSAi2(fcASS.getSelectedFile().getAbsolutePath(),
                        secondModel,secondInfos,secondStyleCollection,secondNameCollection,false);
                aio.LireFichierSSAi2_Minimal(fcASS.getSelectedFile().getAbsolutePath(), secondSubList);
                //setSecondInfos();
            }
            if(fcASS.getSelectedFile().getName().endsWith("ass")){
                AssIO aio = new AssIO();
                aio.LireFichierASSi2(fcASS.getSelectedFile().getAbsolutePath(),
                        secondModel,secondInfos,secondStyleCollection,secondNameCollection,false);
                aio.LireFichierASSi2_Minimal(fcASS.getSelectedFile().getAbsolutePath(), secondSubList);
                //setSecondInfos();
            }
        }
    }//GEN-LAST:event_btnOpenNewActionPerformed

    private void btnAnalysisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnalysisActionPerformed
        AnalyzeDialog ad = new AnalyzeDialog(frame, true);
        ad.setLocationRelativeTo(null);
        boolean value = ad.showDialog();
        if(value==true){
            List<Object> lFirstObjects = new ArrayList<Object>();
            List<Object> lSecondObjects = new ArrayList<Object>();
            setupLineChangeObject();
            if(ad.compareBySentence()==true){
                compareBySentence();
                if(ad.makeBarChart()==true){                
                    lFirstObjects.add(new BarChart3D("Old ASS", firstchartmap));
                    lSecondObjects.add(new BarChart3D("New ASS", secondchartmap));
                }
                if(ad.makePieChart()==true){
                    lFirstObjects.add(new PieChart3D("Old ASS", firstchartmap));
                    lSecondObjects.add(new PieChart3D("New ASS", secondchartmap));
                }
            }else if(ad.compareByTime()==true){
                compareByStartTime();
                if(ad.makeBarChart()==true){                
                    lFirstObjects.add(new BarChart3D("Old ASS", firstchartmap));
                    lSecondObjects.add(new BarChart3D("New ASS", secondchartmap));
                }
                if(ad.makePieChart()==true){
                    lFirstObjects.add(new PieChart3D("Old ASS", firstchartmap));
                    lSecondObjects.add(new PieChart3D("New ASS", secondchartmap));
                }
            }else if(ad.compareByStyle()==true){
                
            }
            if(ad.makeWords()==true){
                List<String> words = ad.getWords();
                List<Map<String, Integer>> wordsList = new ArrayList<Map<String, Integer>>();
                for(String word : words){
                    Map<String, Integer> map = searchForWord(word);
                    wordsList.add(map);
                }
                if(ad.makeBarChart()){
                    int index = 0;
                    for(Map map : wordsList){                        
                        lSecondObjects.add(new BarChart3D("The \""+ words.get(index) +"\" word", map));
                        index += 1;
                    }
                }
                if(ad.makePieChart()){
                    int index = 0;
                    for(Map map : wordsList){                        
                        lSecondObjects.add(new PieChart3D("The \""+ words.get(index) +"\" word", map));
                        index += 1;
                    }
                }
            }
            updateChartPanel(firstChartPanel, lFirstObjects);
            updateChartPanel(secondChartPanel, lSecondObjects);
        }
    }//GEN-LAST:event_btnAnalysisActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgNew;
    private javax.swing.ButtonGroup bgOld;
    private javax.swing.JButton btnAnalysis;
    private javax.swing.JButton btnOpenNew;
    private javax.swing.JButton btnOpenOld;
    private javax.swing.JFileChooser fcASS;
    private javax.swing.JPanel firstChartPanel;
    private javax.swing.JTable firstTable;
    private javax.swing.JInternalFrame ifrFirstReport;
    private javax.swing.JInternalFrame ifrFirstTable;
    private javax.swing.JInternalFrame ifrSecondReport;
    private javax.swing.JInternalFrame ifrSecondTable;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JPanel secondChartPanel;
    private javax.swing.JTable secondTable;
    private javax.swing.JToggleButton tbNewItems;
    private javax.swing.JToggleButton tbNewNormal;
    private javax.swing.JToggleButton tbNewStripAll;
    private javax.swing.JToggleButton tbOldItems;
    private javax.swing.JToggleButton tbOldNormal;
    private javax.swing.JToggleButton tbOldStripAll;
    // End of variables declaration//GEN-END:variables
}
