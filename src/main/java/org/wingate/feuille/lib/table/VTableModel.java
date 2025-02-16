package org.wingate.feuille.lib.table;

import org.wingate.feuille.subs.ass.*;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VTableModel extends AbstractTableModel {

    /*
    ViewVersion has :
    - ISO_3166 (language)
    - Ass (script)
    - ViewRevision (revision)
        While ViewRevision has :
        - version
        - author
        - updates
        and can be incremented
     */

    /*
    Table columns :
    - Folder 'clickable'        (0)     (object is SubsFolder)
    - Note 'clickable'          (1)     (object is SubsNote)
    - Version flag 'clickable'  (2)     (object is a virtual ViewRevision flag)
    - Line number               (3)     (object is a virtual event index)
    - Type                      (4)     ASS -> (AssEvent -> dialogue, comment)
    - Layer                     (5)     ASS -> (AssEvent -> layer)
    - Start of event            (6)     ASS -> (AssEvent -> AssTime)
    - End of event              (7)     ASS -> (AssEvent -> AssTime)
    - Style                     (8)     ASS -> (AssEvent -> AssStyle)
    - Actor or Name             (9)     ASS -> (AssEvent -> AssActor)
    - MarginL                   (10)    ASS -> (AssEvent -> margin left)
    - MarginR                   (11)    ASS -> (AssEvent -> margin right)
    - MarginV                   (12)    ASS -> (AssEvent -> margin vertical)
    - Effect                    (13)    ASS -> (AssEvent -> AssEffect)
    - Statistics                (14)    CPS
    - Statistics                (15)    CPL
    - Text                      (16)    ASS -> (AssEvent -> text)
     */

    /**
     *
     */
    private List<SubsFolder> folders = new ArrayList<>();

    /**
     *
     */
    private List<SubsNote> notes = new ArrayList<>();

    /**
     * Contains all versions of authors
     */
    private List<VirtualVersion> versions = new ArrayList<>();

    /**
     * currentVersion is version of ViewRevision (default is 0; do not use negative index)
     */
    private int currentVersion;

    private AssStatistics stats;
    private TableTextRenderer tableTextRenderer;
    private final JTable table;

    public VTableModel(JTable table){
        this.table = table;
        versions.add(new VirtualVersion());
        currentVersion = 0;
        stats = new AssStatistics();

        tableTextRenderer = new TableTextRenderer();

        init();
    }

    private void init(){
        table.setModel(this);
        table.setDefaultRenderer(AssStatistics.class, new AssStatistics.Renderer());

        table.setDefaultRenderer(String.class, tableTextRenderer);
        table.setDefaultRenderer(Integer.class, new TableIntegerRenderer());
        table.setDefaultRenderer(AssActor.class, new TableAssActorRenderer());
        table.setDefaultRenderer(AssEffect.class, new TableAssEffectRenderer());
        table.setDefaultRenderer(AssStyle.class, new TableAssStyleRenderer());
        table.setDefaultRenderer(AssTime.class, new TableAssTimeRenderer());
        table.setDefaultRenderer(AssEvent.class, new AssEvent.Renderer());
        table.setDefaultRenderer(AssEvent.Type.class, new TableAssEventTypeRenderer());
    }

    public void updateColumnSize(){
        final TableColumnModel cm = table.getColumnModel();

        for(int i=0; i<table.getColumnCount(); i++){
            switch(i){
                case 0, 1, 2, 3, 4, 10, 11, 12 -> { cm.getColumn(i).setPreferredWidth(40); }
                case 5, 14, 15 -> { cm.getColumn(i).setPreferredWidth(50); }
                case 6, 7 -> { cm.getColumn(i).setPreferredWidth(70); }
                case 8, 9 -> { cm.getColumn(i).setPreferredWidth(130); }
                case 13 -> { cm.getColumn(i).setPreferredWidth(170); }
                case 16 -> { cm.getColumn(i).setPreferredWidth(1000); }
            }
        }

        table.updateUI();
    }

    @Override
    public int getRowCount() {
        return versions.get(currentVersion).getScript() != null ?
                versions.get(currentVersion).getScript().getEvents().size() : 0;
    }

    @Override
    public int getColumnCount() {
        return 17;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch(columnIndex){
            // 0: Folder (SubsFolder)
            case 0 -> { return SubsFolder.class; }
            // 1: Note (SubsNote)
            case 1 -> { return SubsNote.class; }
            // 2: ViewVersion (inside ViewRevision), 3: Line number, 5: Layer, 10-11-12: margins
            case 2, 3, 5, 10, 11, 12 -> { return Integer.class; }
            // 4: LineType
            case 4 -> { return AssEvent.Type.class; }
            // 6: Start time, 7: End time
            case 6, 7 -> { return AssTime.class; }
            // 8: Style
            case 8 -> { return AssStyle.class; }
            // 9: Actor or Name
            case 9 -> { return AssActor.class; }
            // 13: Effect
            case 13 -> { return AssEffect.class; }
            // 14, 15: Statistics (14: CPS, 15: CPL)
            case 14, 15 -> { return AssStatistics.class; }
            // 16: Text
            case 16 -> { return AssEvent.class; }
        }

        return super.getColumnClass(columnIndex);
    }

    @Override
    public String getColumnName(int column) {
        switch(column){
            case 0 -> { return "G"; } // Folder (Group)
            case 1 -> { return "N"; } // Note
            case 2 -> { return "V"; } // ViewVersion
            case 3 -> { return "#"; } // Line number
            case 4 -> { return "Type"; }
            case 5 -> { return "Layer"; }
            case 6 -> { return "Start"; }
            case 7 -> { return "End"; }
            case 8 -> { return "Style"; }
            case 9 -> { return "Actor"; }
            case 10 -> { return "ML"; }
            case 11 -> { return "MR"; }
            case 12 -> { return "MV"; }
            case 13 -> { return "FX"; }
            case 14 -> { return "CPS"; } // Statistics
            case 15 -> { return "CPL"; } // Statistics
            case 16 -> { return "Text"; }
        }

        return super.getColumnName(column);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        switch(columnIndex){
            case 0 -> { return true; } // Folder (Group)
            case 1 -> { return true; } // Note
            case 2 -> { return false; } // ViewVersion
            case 3 -> { return false; } // Line number
            case 4 -> { return false; } // Type
            case 5 -> { return false; } // Layer
            case 6 -> { return false; } // Start
            case 7 -> { return false; } // End
            case 8 -> { return false; } // Style
            case 9 -> { return false; } // Actor or Name
            case 10 -> { return false; } // ML
            case 11 -> { return false; } // MR
            case 12 -> { return false; } // MV
            case 13 -> { return false; } // Effect
            case 14 -> { return false; } // CPS
            case 15 -> { return false; } // CPL
            case 16 -> { return false; } // Text
            default -> { return false; }
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        int lineIndex = 0;

        SubsFolder folder = null;
        for(SubsFolder sf : folders){
            if(sf.getMinIndex() <= rowIndex && rowIndex < sf.getMaxIndex()){
                lineIndex++;
                folder = sf;
            }
        }

        SubsNote note = null;
        for(SubsNote sn : notes){
            if(sn.getIndex() == rowIndex){
                note = sn;
                break;
            }
        }

        VirtualVersion vVersion = versions.get(currentVersion);

        AssEvent event = versions.get(currentVersion).getScript().getEvents().get(rowIndex + lineIndex);
        stats.setEvent(event);

        switch(columnIndex){
            case 0 -> { return folder; }
            case 1 -> { return note; }
            case 2 -> { return vVersion; }
            case 3 -> { return rowIndex + lineIndex + 1; }
            case 4 -> { return event.getType(); }
            case 5 -> { return event.getLayer(); }
            case 6 -> { return event.getStart(); }
            case 7 -> { return event.getEnd(); }
            case 8 -> { return event.getStyle(); }
            case 9 -> { return event.getName(); }
            case 10 -> { return event.getMarginL(); }
            case 11 -> { return event.getMarginR(); }
            case 12 -> { return event.getMarginV(); }
            case 13 -> { return event.getEffect(); }
            case 14, 15 -> { return stats; } // Stats
            case 16 -> { return event; }
            default -> { return null; }
        }

    }

    @Override
    public void setValueAt(Object v, int rowIndex, int columnIndex) {

        int lineIndex = 0;

        for(SubsFolder sf : folders){
            if(sf.getMinIndex() <= rowIndex && rowIndex < sf.getMaxIndex()){
                lineIndex++;
            }
        }

        AssEvent event = versions.get(currentVersion).getScript().getEvents().get(rowIndex + lineIndex);

        switch(columnIndex){
            // 0 Line Number
            case 4 -> { if(v instanceof AssEvent.Type x) event.setType(x); }
            case 5 -> { if(v instanceof Integer x) event.setLayer(x); }
            case 6 -> { if(v instanceof AssTime x) event.setStart(x); }
            case 7 -> { if(v instanceof AssTime x) event.setEnd(x); }
            case 8 -> { if(v instanceof AssStyle x) event.setStyle(x); }
            case 9 -> { if(v instanceof AssActor x) event.setName(x); }
            case 10 -> { if(v instanceof Integer x) event.setMarginL(x); }
            case 11 -> { if(v instanceof Integer x) event.setMarginR(x); }
            case 12 -> { if(v instanceof Integer x) event.setMarginV(x); }
            case 13 -> { if(v instanceof AssEffect x) event.setEffect(x); }
            case 14, 15 -> { if(v instanceof AssStatistics x) stats = x; }
            case 16 -> { if(v instanceof AssEvent x) event = x; }
        }

        versions.get(currentVersion).getScript().getEvents().set(rowIndex + lineIndex, event);
        fireTableCellUpdated(rowIndex, columnIndex);
        table.updateUI();
    }

    public void addValue(AssEvent event, int version){
        try{
            int v = currentVersion;
            if(version >= 0 && version < versions.size()) v = version;
            versions.get(v).getScript().getEvents().add(event);
            fireTableRowsInserted(getRowCount(), getRowCount());
            table.updateUI();
        }catch(Exception ex){
            Logger.getLogger(VTableModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void insertValueAt(AssEvent event, int version, int row){
        try{
            int v = currentVersion;
            if(version >= 0 && version < versions.size()) v = version;
            versions.get(v).getScript().getEvents().add(row, event);
            fireTableRowsInserted(row, row);
            table.updateUI();
        }catch(Exception ex){
            Logger.getLogger(VTableModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void removeValueAt(int version, int row){
        try{
            int v = currentVersion;
            if(version >= 0 && version < versions.size()) v = version;
            versions.get(v).getScript().getEvents().remove(row);
            fireTableRowsDeleted(row, row);
            table.updateUI();
        }catch(Exception ex){
            Logger.getLogger(VTableModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void replaceValueAt(AssEvent event, int version, int row){
        try{
            int v = currentVersion;
            if(version >= 0 && version < versions.size()) v = version;
            versions.get(v).getScript().getEvents().set(row, event);
            fireTableRowsUpdated(row, row);
            table.updateUI();
        }catch(Exception ex){
            Logger.getLogger(VTableModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public AssEvent retrieveValueAt(int row, int version){
        try{
            int v = currentVersion;
            if(version >= 0 && version < versions.size()) v = version;
            return versions.get(v).getScript().getEvents().get(row);
        }catch(Exception ex){
            Logger.getLogger(VTableModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<VirtualVersion> getVersions() {
        return versions;
    }

    public void setVersions(List<VirtualVersion> versions) {
        this.versions = versions;
    }

    public int getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(int currentVersion) {
        this.currentVersion = currentVersion;
    }
}
