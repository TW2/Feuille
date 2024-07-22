/*
 * Copyright (C) 2024 util2
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
package org.wingate.feuille.m.afm.karaoke;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

/**
 *
 * @author util2
 */
public class CodePanel extends JPanel {
    
    private final RSyntaxTextArea textArea;
    private final RTextScrollPane scrollPane;
    private CodeType codeType;

    public CodePanel() {
        setLayout(new BorderLayout());
        textArea = new RSyntaxTextArea(20, 60);
        textArea.setCodeFoldingEnabled(true);
        scrollPane = new RTextScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);
        codeType = null;
    }
    
    public void setCodeType(CodeType codeType){
        this.codeType = codeType;
        changeType();
    }
    
    private void changeType(){
        switch(codeType){
            case JavaScript -> {
                textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
            }
            case Python -> {
                textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_PYTHON);
            }
            case Ruby -> {
                textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_RUBY);
            }
            case Lua -> {
                textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_LUA);
            }
        }
    }
}
