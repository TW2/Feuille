package org.wingate.feuille.lib.table;

import org.wingate.feuille.subs.ass.*;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;
import java.util.ArrayList;
import java.util.List;

public class VersionsTableModel extends AbstractTableModel {

    public enum DealWith {
        NoSet, Version1, Version2;
    }

    private final JTable table;
    private AssStatistics stats;
    private List<Version> versions;
    private Version version1ToDisplay;
    private Version version2ToDisplay;
    private DealWith dealWith;

    private TableTextRenderer tableTextRenderer;

    public VersionsTableModel(JTable table) {
        this.table = table;
        stats = new AssStatistics();
        versions = new ArrayList<>();
        version1ToDisplay = null;
        version2ToDisplay = null;
        dealWith = DealWith.NoSet;

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
        table.setDefaultRenderer(AssTimeExtra.class, new AssTimeExtra.Renderer());
        table.setDefaultRenderer(AssEvent.class, new AssEvent.Renderer());
        table.setDefaultRenderer(AssEvent.Type.class, new TableAssEventTypeRenderer());
    }

    public void updateColumnSize(){
        final TableColumnModel cm = table.getColumnModel();

        for(int i=0; i<table.getColumnCount(); i++){
            switch(i){
                case 0, 1, 7, 8, 9 -> { cm.getColumn(i).setPreferredWidth(40); }
                case 2, 11 -> { cm.getColumn(i).setPreferredWidth(50); }
                case 3, 4 -> { cm.getColumn(i).setPreferredWidth(70); }
                case 5, 6 -> { cm.getColumn(i).setPreferredWidth(130); }
                case 10 -> { cm.getColumn(i).setPreferredWidth(170); }
                case 12 -> { cm.getColumn(i).setPreferredWidth(1000); }
            }
        }

        table.updateUI();
    }

    public List<Version> getVersions() {
        return versions;
    }

    public void setVersions(List<Version> versions) {
        this.versions = versions;
    }

    public Version getVersion1ToDisplay() {
        return version1ToDisplay;
    }

    public void setVersion1ToDisplay(Version version1ToDisplay) {
        this.version1ToDisplay = version1ToDisplay;
    }

    public Version getVersion2ToDisplay() {
        return version2ToDisplay;
    }

    public void setVersion2ToDisplay(Version version2ToDisplay) {
        this.version2ToDisplay = version2ToDisplay;
    }

    public DealWith getDealWith() {
        return dealWith;
    }

    public void setDealWith(DealWith dealWith) {
        this.dealWith = dealWith;
    }

    public JTable getTable() {
        return table;
    }

    @Override
    public int getRowCount() {
        if(versions.isEmpty()) return 0;

        int max = 0;
        for(Version v : versions){
            max = Math.max(max, v.getEvents().size());
        }
        return max;
    }

    @Override
    public int getColumnCount() {
        return 13;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch(columnIndex){
            case 0 -> { return Integer.class; } // Line Number
            case 1 -> { return AssEvent.Type.class; }
            case 2, 7, 8, 9 -> { return Integer.class; }
            case 3, 4 -> { return AssTimeExtra.class; }
            case 5 -> { return AssStyle.class; }
            case 6 -> { return AssActor.class; }
            case 10 -> { return AssEffect.class; }
            case 11 -> { return AssStatistics.class; } // Statistics
            case 12 -> { return AssEvent.class; }
        }

        return super.getColumnClass(columnIndex);
    }

    @Override
    public String getColumnName(int column) {
        switch(column){
            case 0 -> { return "#"; }
            case 1 -> { return "Type"; }
            case 2 -> { return "Layer"; }
            case 3 -> { return "Start"; }
            case 4 -> { return "End"; }
            case 5 -> { return "Style"; }
            case 6 -> { return "Actor"; }
            case 7 -> { return "ML"; }
            case 8 -> { return "MR"; }
            case 9 -> { return "MV"; }
            case 10 -> { return "FX"; }
            case 11 -> { return "Stats"; } // Statistics
            case 12 -> { return "Text"; }
        }

        return super.getColumnName(column);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        Object obj = null;
        AssEvent event = null, before = null, after = null;

        switch(dealWith){
            case Version1 -> {
                for(Version v : versions){
                    if(v == version1ToDisplay){
                        try{
                            event = v.getEvents().get(rowIndex);
                        }catch (Exception _){}
                        try{
                            before = v.getEvents().get(rowIndex - 1);
                        }catch (Exception _){}
                        try{
                            after = v.getEvents().get(rowIndex + 1);
                        }catch (Exception _){}

                        break;
                    }
                }
            }
            case Version2 -> {
                for(Version v : versions){
                    if(v == version2ToDisplay){
                        try{
                            event = v.getEvents().get(rowIndex);
                        }catch (Exception _){}
                        try{
                            before = v.getEvents().get(rowIndex - 1);
                        }catch (Exception _){}
                        try{
                            after = v.getEvents().get(rowIndex + 1);
                        }catch (Exception _){}

                        break;
                    }
                }
            }
            case NoSet -> { return null; }
        }

        if(event == null) return null;


        stats.setEvent(event);

        // Set AssTimeExtra (start)
        AssTimeExtra eStart = new AssTimeExtra(event.getStart(), event.getStart());
        if(before != null && rowIndex > 0){
            double msStart = eStart.getTime().getMsTime();
            double lastEnd = before.getEnd().getMsTime();
            eStart.setDifference(new AssTime(msStart - lastEnd));
        }

        // Set AssTimeExtra (end)
        AssTimeExtra eEnd = new AssTimeExtra(event.getEnd(), event.getEnd());
        if(after != null && rowIndex > 0 && rowIndex < getRowCount() - 1){
            double msEnd = eEnd.getTime().getMsTime();
            double nextStart = after.getStart().getMsTime();
            eEnd.setDifference(new AssTime(nextStart - msEnd));
        }

        switch(columnIndex){
            case 0 -> { obj = rowIndex + 1; }
            case 1 -> { obj = event.getType(); }
            case 2 -> { obj = event.getLayer(); }
            case 3 -> { obj = eStart; }
            case 4 -> { obj = eEnd; }
            case 5 -> { obj = event.getStyle(); }
            case 6 -> { obj = event.getName(); }
            case 7 -> { obj = event.getMarginL(); }
            case 8 -> { obj = event.getMarginR(); }
            case 9 -> { obj = event.getMarginV(); }
            case 10 -> { obj = event.getEffect(); }
            case 11 -> { obj = stats; } // Stats
            case 12 -> { obj = event; }
        }

        return obj;
    }

    @Override
    public void setValueAt(Object v, int rowIndex, int columnIndex) {

        AssEvent event = null;
        Version version = null;

        switch(dealWith){
            case Version1 -> {
                for(Version x : versions){
                    if(x == version1ToDisplay){
                        try{
                            event = x.getEvents().get(rowIndex);
                            version = x;
                        }catch (Exception _){}

                        break;
                    }
                }
            }
            case Version2 -> {
                for(Version x : versions){
                    if(x == version2ToDisplay){
                        try{
                            event = x.getEvents().get(rowIndex);
                            version = x;
                        }catch (Exception _){}

                        break;
                    }
                }
            }
            case NoSet -> { return; }
        }

        if(event == null) return;

        switch(columnIndex){
            // 0 Line Number
            case 1 -> { if(v instanceof AssEvent.Type x) event.setType(x); }
            case 2 -> { if(v instanceof Integer x) event.setLayer(x); }
            case 3 -> { if(v instanceof AssTimeExtra x) event.setStart(x.getTime()); }
            case 4 -> { if(v instanceof AssTimeExtra x) event.setEnd(x.getTime()); }
            case 5 -> { if(v instanceof AssStyle x) event.setStyle(x); }
            case 6 -> { if(v instanceof AssActor x) event.setName(x); }
            case 7 -> { if(v instanceof Integer x) event.setMarginL(x); }
            case 8 -> { if(v instanceof Integer x) event.setMarginR(x); }
            case 9 -> { if(v instanceof Integer x) event.setMarginV(x); }
            case 10 -> { if(v instanceof AssEffect x) event.setEffect(x); }
            case 11 -> { if(v instanceof AssStatistics x) stats = x; }
            case 12 -> { if(v instanceof AssEvent x) event = x; }
        }
        version.getEvents().set(rowIndex, event);
        fireTableCellUpdated(rowIndex, columnIndex);
        table.updateUI();
    }

    public void addValue(AssEvent event, boolean changeV1, boolean changeV2){
        // Add Version V1
        if(changeV1){
            version1ToDisplay.getEvents().add(event);
            fireTableRowsInserted(getRowCount(), getRowCount());
            table.updateUI();
        }
        // Add Version V2
        if(changeV2){
            version2ToDisplay.getEvents().add(event);
            fireTableRowsInserted(getRowCount(), getRowCount());
            table.updateUI();
        }
    }

    public void insertValueAt(AssEvent event, int row, boolean changeV1, boolean changeV2){
        // Version V1
        if(changeV1){
            version1ToDisplay.getEvents().add(row, event);
            fireTableRowsInserted(row, row);
            table.updateUI();
        }
        // Version V2
        if(changeV2){
            version2ToDisplay.getEvents().add(row, event);
            fireTableRowsInserted(row, row);
            table.updateUI();
        }
    }

    public void removeValueAt(int row, boolean changeV1, boolean changeV2){
        // Version V1
        if(changeV1){
            version1ToDisplay.getEvents().remove(row);
            fireTableRowsDeleted(row, row);
            table.updateUI();
        }
        // Version V2
        if(changeV2){
            version2ToDisplay.getEvents().remove(row);
            fireTableRowsDeleted(row, row);
            table.updateUI();
        }
    }

    public void replaceValueAt(AssEvent event, int row, boolean changeV1, boolean changeV2){
        // Version V1
        if(changeV1){
            version1ToDisplay.getEvents().set(row, event);
            fireTableRowsUpdated(row, row);
            table.updateUI();
        }
        // Version V2
        if(changeV2){
            version2ToDisplay.getEvents().set(row, event);
            fireTableRowsUpdated(row, row);
            table.updateUI();
        }
    }

    public TableTextRenderer getTableTextRenderer() {
        return tableTextRenderer;
    }

    public void setTableTextRenderer(TableTextRenderer tableTextRenderer) {
        this.tableTextRenderer = tableTextRenderer;
    }
}
