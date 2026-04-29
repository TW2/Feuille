package feuille.module.editor;

import feuille.module.editor.assa.ASS;
import feuille.module.editor.assa.AssEvent;
import feuille.util.ISO_3166;

import java.util.Locale;

public class Exchange {

    private VoyagersTable voyagersTable;

    private ASS ass;
    private ISO_3166 flag1;
    private ISO_3166 flag2;

    public Exchange() {
        ass = new ASS();
        flag1 = ISO_3166.getISO_3166(Locale.getDefault().getISO3Country());
        flag2 = ISO_3166.getISO_3166(Locale.getDefault().getISO3Country());
    }

    public VoyagersTable getVoyagersTable() {
        return voyagersTable;
    }

    public void setVoyagersTable(VoyagersTable voyagersTable) {
        this.voyagersTable = voyagersTable;
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
}
