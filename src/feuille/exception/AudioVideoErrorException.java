/*
 * Copyright (C) 2018 util2
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
package feuille.exception;

/**
 *
 * @author util2
 */
public class AudioVideoErrorException extends Exception {

    private final String exceptionName = "AudioVideoErrorException";
    private String messageError = "An error has occured!";
    
    public AudioVideoErrorException(String messageError){
        if(messageError.isEmpty() == false){
            this.messageError = messageError;
        }
        // Load a private method in constructor
        // Then the display() method will call a public method to show error.
        // It's forbidden to call a public method in constructor.
        display();
    }

    public AudioVideoErrorException() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private void display(){
        defineError();
    }
    
    public void defineError() {
        System.err.println(exceptionName + ": " + messageError + (getMessage() != null ? "\n" + getMessage() : ""));
    }
    
}
