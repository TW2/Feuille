/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.listener;

/**
 *
 * @author util2
 */
public interface EditionCursor {
    
    public void cursorMoved(EditionCursorEvent event);
    
    public void cursorBeginClick(EditionCursorEvent event);
    
    public void cursorEndClick();
    
}
