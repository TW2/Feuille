package feuille.util;

import feuille.FeuilleFrame;
import feuille.module.audio.SyncPane;
import feuille.module.editor.assa.*;
import feuille.module.editor.voyager.Voyager;
import feuille.module.editor.voyager.VoyagersTable;
import feuille.module.editor.EditorPanel;
import feuille.module.video.ViewFMP;
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
    private ViewFMP viewFMP;

    private ASS ass;
    private ISO_3166 flag1;
    private ISO_3166 flag2;
    private AssTime startTime;
    private AssTime endTime;
    private AssStyle currentStyle;
    private final List<AssStylesCollection> stylesCollections;
    private AssStylesCollection currentStylesCollection;

    public Exchange() {
        ass = new ASS();
        flag1 = ISO_3166.getISO_3166(Locale.getDefault().getISO3Country());
        flag2 = ISO_3166.getISO_3166(Locale.getDefault().getISO3Country());
        startTime = new AssTime(0);
        endTime = new AssTime(0);
        currentStyle = new AssStyle();
        stylesCollections = new ArrayList<>();
        currentStylesCollection = null;
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

    public ViewFMP getViewFMP() {
        return viewFMP;
    }

    public void setViewFMP(ViewFMP viewFMP) {
        this.viewFMP = viewFMP;
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

    public void setTemporarySubtitlesFile(){
        if(editorPanel.getFlag1() == null) return;
        if(editorPanel.getFlag2() == null) return;
        File app = new File(new File("").getAbsolutePath());
        File folder = new File(app.getPath() +
                File.separator + "settings" +
                File.separator + "preview"
        );
        File script = new File(folder, "editflag2.ass");
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
        ASS.write(newASS, script.getPath());
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

    public static class SpyPlaying implements Runnable {
        public enum State {
            PLAYING,
            PAUSED,
            RESUMED,
            STOPPED
        }

        private State currentState = State.STOPPED;

        private final Exchange exchange;
        private volatile boolean running;
        private Thread thread;
        private long currentTime;
        private long startUs;
        private long endUs;
        private double fps;

        @Override
        public void run() {
            long time = 0;
            long startTime = System.nanoTime();
            long to = 0;
            boolean controlSet = false;
            while(running){
                if(currentState == State.RESUMED){
                    startTime = System.nanoTime() - currentTime;
                }
                if(currentState == State.PLAYING || currentState == State.RESUMED){
                    time = System.nanoTime() - startTime;
                    currentTime = time;
                    if(startUs != 0 && endUs != 0){
                        if(!controlSet){
                            to = System.nanoTime() + ((endUs * 1000) - (startUs * 1000));
                            controlSet = true;
                        }
                        currentTime = time + (startUs * 1000L);
                        if(System.nanoTime() >= to){
                            stopTriggered();
                        }
                    }

                }
                exchange.getSyncPane().getWaveform().setMsCurrent(currentTime / 1_000_000);
                exchange.getSyncPane().getWaveform().repaint();
            }
        }

        public SpyPlaying(Exchange exchange) {
            this.exchange = exchange;
            thread = null;
            running = false;
            currentTime = 0L;
            startUs = 0L;
            endUs = 0L;
            fps = 24000d / 1001d;
        }

        public void setFps(double fps){
            this.fps = fps;
        }

        public double getFps(){
            return fps;
        }

        public void usDiscovered(long startUs, long endUs) {
            this.startUs = startUs;
            this.endUs = endUs;
        }

        public void usReset(){
            this.startUs = 0;
            this.endUs = 0;
        }

        public void playTriggered(){
            if(currentState == State.PAUSED){
                resumeTriggered();
            }else{
                currentState = State.PLAYING;
                running = true;
                thread = new Thread(this);
                thread.start();
            }
        }

        public void pauseTriggered(){
            currentState = State.PAUSED;
            running = false;
        }

        public void resumeTriggered(){
            currentState = State.RESUMED;
            running = true;
        }

        public void stopTriggered(){
            currentState = State.STOPPED;
            running = false;
            if(thread != null){
                if(thread.isAlive() || !thread.isInterrupted()){
                    thread.interrupt();
                    thread = null;
                }
            }
            usReset();
        }
    }
}
