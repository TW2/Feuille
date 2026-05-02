package feuille.util;

import feuille.module.audio.ui.SyncPane;
import feuille.module.editor.Voyager;
import feuille.module.editor.VoyagersTable;
import feuille.module.editor.assa.ASS;
import feuille.module.editor.assa.AssEvent;
import feuille.module.editor.assa.ui.EditorPanel;
import feuille.module.video.View;
import feuille.util.assa.AssEventType;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Exchange {

    private VoyagersTable voyagersTable;
    private SyncPane syncPane;
    private EditorPanel editorPanel;
    private View view;

    private ASS ass;
    private ISO_3166 flag1;
    private ISO_3166 flag2;
    private FFMpeg mpeg;

    public Exchange() {
        ass = new ASS();
        flag1 = ISO_3166.getISO_3166(Locale.getDefault().getISO3Country());
        flag2 = ISO_3166.getISO_3166(Locale.getDefault().getISO3Country());
        mpeg = new FFMpeg();
    }

    public VoyagersTable getVoyagersTable() {
        return voyagersTable;
    }

    public void setVoyagersTable(VoyagersTable voyagersTable) {
        this.voyagersTable = voyagersTable;
    }

    public SyncPane getSyncPane() {
        return syncPane;
    }

    public void setSyncPane(SyncPane syncPane) {
        this.syncPane = syncPane;
    }

    public EditorPanel getEditorPanel() {
        return editorPanel;
    }

    public void setEditorPanel(EditorPanel editorPanel) {
        this.editorPanel = editorPanel;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public ASS getAss() {
        return ass;
    }

    public void setAss(ASS ass) {
        this.ass = ass;
    }

    public ISO_3166 getFlag1() {
        return flag1;
    }

    public void setFlag1(ISO_3166 flag1) {
        this.flag1 = flag1;
    }

    public ISO_3166 getFlag2() {
        return flag2;
    }

    public void setFlag2(ISO_3166 flag2) {
        this.flag2 = flag2;
    }

    public FFMpeg getMpeg() {
        return mpeg;
    }

    public void addEvent(AssEvent event, ISO_3166 f1, ISO_3166 f2){
        voyagersTable.addEvent(event, f1, f2);
    }

    public void replaceEvent(AssEvent event, ISO_3166 f1, ISO_3166 f2){
        voyagersTable.replaceEvent(event, f1, f2);
    }

    public void beforeEvent(AssEvent event, ISO_3166 f1, ISO_3166 f2){
        voyagersTable.beforeEvent(event, f1, f2);
    }

    public void afterEvent(AssEvent event, ISO_3166 f1, ISO_3166 f2){
        voyagersTable.afterEvent(event, f1, f2);
    }

    public void setEditorEvent(Voyager voyager){
        //editorPanel.addToPanel(voyager);
    }

    public void deleteEvent(ISO_3166 f1, ISO_3166 f2){
        voyagersTable.deleteEvent(f1, f2);
    }

    public void openASS(ASS ass){
        List<Voyager> list = new ArrayList<>();
        Voyager lastVoyager = null;
        int counter = 0;
        for(AssEvent event : ass.getEvents()) {
            ISO_3166 iso = event.getText().startsWith("{ISO-3166=") ?
                    ISO_3166.getISO_3166(event.getText().substring("{ISO-3166=".length(), event.getText().indexOf("}")))
                    : ISO_3166.getISO_3166(Locale.getDefault().getISO3Country());
            boolean visible = event.getType() != AssEventType.Comment;
            if(event.getText().startsWith("{ISO-3166=")){
                event.setText(event.getText().substring("{ISO-3166=".length() + 4));
            }
            Voyager v = new Voyager(iso, visible, event);

            counter++;

            if(lastVoyager == null) {
                lastVoyager = v;
                System.out.println(1);
            }else if(v.getEvent().getStart().toAss().equals(lastVoyager.getEvent().getStart().toAss())
                    && v.getEvent().getEnd().toAss().equals(lastVoyager.getEvent().getEnd().toAss())
                    && !v.getLanguage().getAlpha3().equals(lastVoyager.getLanguage().getAlpha3())){
                lastVoyager.getVoyagers().add(v);
                System.out.println(2);
            }else{
                list.add(lastVoyager);
                list.add(v);
                lastVoyager = null;
                System.out.println(3);
            }
        }
        list.add(lastVoyager);
        voyagersTable.getVoyagers().clear();
        voyagersTable.getVoyagers().addAll(list);
        voyagersTable.repaint();
        //tablePanel.getTable().loadASS(ass);
    }

    public void saveAsASS(String filepath){
        ASS ass = new ASS();
        if(editorPanel.getFlag1().equals(editorPanel.getFlag2()) && !editorPanel.flag2HasMany()){
            for(Voyager voyager : voyagersTable.getVoyagers()){
                if (voyager == null) continue;
                ass.getEvents().add(voyager.getEvent());
            }
        }else{
            for(Voyager voyager : voyagersTable.getVoyagers()){
                if (voyager == null) continue;
                AssEvent e1 = voyager.getEvent();
                e1.setText("{ISO-3166=" + voyager.getLanguage().getAlpha3() + "}" + voyager.getEvent().getText());
                ass.getEvents().add(e1);
                for(Voyager v : voyager.getVoyagers()){
                    AssEvent e2 = v.getEvent();
                    e2.setText("{ISO-3166=" + v.getLanguage().getAlpha3() + "}" + v.getEvent().getText());
                    ass.getEvents().add(e2);
                }
            }
        }

        ASS.write(ass, filepath);
    }

    public void clearEvents(){
        voyagersTable.getVoyagers().clear();
    }
}
