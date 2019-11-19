/*
 * Copyright (C) 2019 util2
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package feuille.panel;

import feuille.util.ISO_3166;
import feuille.util.Language;

/**
 *
 * @author util2
 */
public class PanelGroup {
    
    ASSEditor assEditor;
    Chat chat;
    Draw draw;
    FXEditor fxEditor;
    MacroEditor macroEditor;
    Table table;
    Video video;
    Wave wave;
    Wave spectrogram;

    public PanelGroup() {
        
    }

    public void setAssEditor(ASSEditor assEditor) {
        this.assEditor = assEditor;
    }

    public ASSEditor getAssEditor() {
        return assEditor;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public Chat getChat() {
        return chat;
    }

    public void setDraw(Draw draw) {
        this.draw = draw;
    }

    public Draw getDraw() {
        return draw;
    }

    public void setFxEditor(FXEditor fxEditor) {
        this.fxEditor = fxEditor;
    }

    public FXEditor getFxEditor() {
        return fxEditor;
    }

    public void setMacroEditor(MacroEditor macroEditor) {
        this.macroEditor = macroEditor;
    }

    public MacroEditor getMacroEditor() {
        return macroEditor;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public Table getTable() {
        return table;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public Video getVideo() {
        return video;
    }

    public void setWave(Wave wave) {
        this.wave = wave;
    }

    public Wave getWave() {
        return wave;
    }

    public void setSpectrogram(Wave spectrogram) {
        this.spectrogram = spectrogram;
    }

    public Wave getSpectrogram() {
        return spectrogram;
    }

    public static PanelGroup create(Language chosen, ISO_3166 get) {
        PanelGroup pg = new PanelGroup();
        pg.assEditor = new ASSEditor();
        pg.assEditor.initializeASSEditor(chosen, get);
        pg.chat = new Chat();
        pg.chat.initializeChat(chosen, get);
        pg.draw = new Draw();
        pg.fxEditor = new FXEditor();
        pg.fxEditor.initializeFxEditor(chosen, get);
        pg.macroEditor = new MacroEditor();
        pg.table = new Table();
        pg.table.initializeTable(chosen, get);
        pg.video = new Video();
        pg.wave = new Wave();
        pg.spectrogram = new Wave();
        return pg;            
    }
    
}
