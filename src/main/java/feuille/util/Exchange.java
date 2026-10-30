package feuille.util;

import feuille.FeuilleFrame;
import feuille.module.audio.SyncPane;
import feuille.module.editor.assa.*;
import feuille.module.editor.voyager.Voyager;
import feuille.module.editor.voyager.VoyagersTable;
import feuille.module.editor.EditorPanel;
import feuille.module.video.ViewGst;
import feuille.util.assa.AssEventType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Exchange {

    private FeuilleFrame mainFrame;
    private VoyagersTable voyagersTable;
    private SyncPane syncPane;
    private EditorPanel editorPanel;
    private ViewGst viewGStreamer;

    private ASS ass;
    private ISO_3166 flag1;
    private ISO_3166 flag2;
    private AssTime startTime;
    private AssTime endTime;
    private AssStyle currentStyle;
    private final List<AssStylesCollection> stylesCollections;
    private AssStylesCollection currentStylesCollection;
    private List<AssActor> actors;
    private List<AssEffect> effects;

    public Exchange() {
        ass = new ASS();
        flag1 = ISO_3166.getISO_3166(Locale.getDefault().getISO3Country());
        flag2 = ISO_3166.getISO_3166(Locale.getDefault().getISO3Country());
        startTime = new AssTime(0);
        endTime = new AssTime(0);
        currentStyle = new AssStyle();
        stylesCollections = new ArrayList<>();
        currentStylesCollection = null;
        actors = new ArrayList<>();
        effects = new ArrayList<>();
        initStyles();
    }

    public FeuilleFrame getMainFrame() {
        return mainFrame;
    }

    public void setMainFrame(FeuilleFrame mainFrame) {
        this.mainFrame = mainFrame;
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

    public List<AssActor> getActors() {
        return actors;
    }

    public void setActors(List<AssActor> actors) {
        this.actors = actors;
    }

    public List<AssEffect> getEffects() {
        return effects;
    }

    public void setEffects(List<AssEffect> effects) {
        this.effects = effects;
    }

    public ViewGst getViewGst() {
        return viewGStreamer;
    }

    public void setViewGst(ViewGst viewGStreamer) {
        this.viewGStreamer = viewGStreamer;
    }

    public void addEvent(AssEvent event, ISO_3166 f1, ISO_3166 f2){
        voyagersTable.addEvent(event, f1, f2);
        setTemporarySubtitlesFile();
    }

    public void replaceEvent(AssEvent event, ISO_3166 f1, ISO_3166 f2){
        voyagersTable.replaceEvent(event, f1, f2);
        setTemporarySubtitlesFile();
    }

    public void beforeEvent(AssEvent event, ISO_3166 f1, ISO_3166 f2){
        voyagersTable.beforeEvent(event, f1, f2);
        setTemporarySubtitlesFile();
    }

    public void afterEvent(AssEvent event, ISO_3166 f1, ISO_3166 f2){
        voyagersTable.afterEvent(event, f1, f2);
        setTemporarySubtitlesFile();
    }

    public void setEditorEvent(Voyager voyager){
        editorPanel.addToPanel(voyager);
        currentStyle = voyager.getEvent().getStyle();
    }

    public AssStyle getCurrentStyle() {
        return currentStyle;
    }

    public void setCurrentStyle(AssStyle currentStyle) {
        this.currentStyle = currentStyle;
    }

    public List<AssStylesCollection> getStylesCollections() {
        return stylesCollections;
    }

    public AssTime getStartTime() {
        return startTime;
    }

    public void setStartTime(AssTime startTime) {
        this.startTime = startTime;
    }

    public AssTime getEndTime() {
        return endTime;
    }

    public void setEndTime(AssTime endTime) {
        this.endTime = endTime;
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
            }else if(v.getEvent().getStart().toAss().equals(lastVoyager.getEvent().getStart().toAss())
                    && v.getEvent().getEnd().toAss().equals(lastVoyager.getEvent().getEnd().toAss())
                    && !v.getLanguage().getAlpha3().equals(lastVoyager.getLanguage().getAlpha3())){
                lastVoyager.getVoyagers().add(v);
            }else{
                list.add(lastVoyager);
                list.add(v);
                lastVoyager = null;
            }
        }
        list.add(lastVoyager);
        voyagersTable.getVoyagers().clear();
        voyagersTable.getVoyagers().addAll(list);
        voyagersTable.repaint();
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
        ass.setStyles(getAss().getStyles());
        ass.setActors(getActors());

        ASS.write(ass, filepath);
    }

    public void clearEvents(){
        voyagersTable.getVoyagers().clear();
    }

    public void setTemporarySubtitlesFile(){
        if(editorPanel.getFlag1() == null) return;
        if(editorPanel.getFlag2() == null) return;
        ASS newASS = new ASS();
        if(editorPanel.getFlag1().equals(editorPanel.getFlag2())){
            for(Voyager voyager : voyagersTable.getVoyagers()){
                if (voyager == null) continue;
                newASS.getEvents().add(voyager.getEvent());
            }
        }else{
            for(Voyager voyager : voyagersTable.getVoyagers()){
                if (voyager == null) continue;
                for(Voyager v : voyager.getVoyagers()){
                    if(editorPanel.getFlag2().equals(v.getLanguage())){
                        newASS.getEvents().add(v.getEvent());
                    }
                }
            }
        }
        newASS.setInfos(ass.getInfos());
        newASS.setStyles(ass.getStyles());
        getViewGst().getSubtitles().loadTrack(ASS.stringify(newASS));
    }

    private void initStyles(){
        AssStylesCollection newAssStylesCollection = new AssStylesCollection("Default collection");
        newAssStylesCollection.getStyles().add(currentStyle);
        stylesCollections.add(newAssStylesCollection);
        currentStylesCollection = newAssStylesCollection;
        ass.getStyles().add(currentStyle);
    }

    public AssStylesCollection getCurrentStylesCollection() {
        return currentStylesCollection;
    }

    public void setCurrentStylesCollection(AssStylesCollection currentStylesCollection) {
        this.currentStylesCollection = currentStylesCollection;
    }
}
