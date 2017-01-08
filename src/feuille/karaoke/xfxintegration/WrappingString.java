/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.karaoke.xfxintegration;

/**
 *
 * @author The Wingate 2940
 */
public class WrappingString {
    
    Wrapping wrap = Wrapping.Q0;
    
    public WrappingString(){
        
    }
    
    public WrappingString(String s){
        try{
            wrap = wrap.getWrappingFrom(Integer.parseInt(s));
        }catch(Exception exc){
            wrap = Wrapping.Q0;
        }
    }
    
    public enum Wrapping{
        Q0(0,"Q0"),Q1(1,"Q1"),Q2(2,"Q2"),Q3(3,"Q3");

        private int number;
        private String sw;

        /** <p>Create a new Encoding.<br />Crée un nouveau Encoding.</p> */
        Wrapping(int number, String sw){
            this.number = number;
            this.sw = sw;
        }

        /** <p>Return the code of the encoding.<br />
         * Retourne le code de l'encodage.</p> */
        public int getNumber(){
            return number;
        }

        /** <p>Return the name of the encoding.<br />
         * Retourne le nom de l'encodage (en anglais).</p> */
        public String getWrapping(){
            return sw;
        }

        /** <p>Return the string "'code' - 'encoding'".<br />
         * Retourne la chaine "'code' - 'encodage'".</p> */
        @Override
        public String toString(){
            return number+" - "+sw;
        }

        /** <p>Return the encoding with the given code.<br />
         * Retourne l'encodage avec le code donné.</p> */
        public Wrapping getWrappingFrom(int number){
            Wrapping w;
            switch(number){
                case 0: w=Wrapping.Q0; break;
                case 1: w=Wrapping.Q1; break;
                case 2: w=Wrapping.Q2; break;
                case 3: w=Wrapping.Q3; break;
                default: w=Wrapping.Q0; break;
            }
            return w;
        }
    }
    
    public void setSelectedWrapping(String s){
        try{
            wrap = wrap.getWrappingFrom(Integer.parseInt(s));
        }catch(Exception exc){
            wrap = Wrapping.Q0;
        }
    }
    
    public void setSelectedWrapping(Wrapping wrap){
        this.wrap = wrap;
    }
    
    public String getSelectedWrapping(){
        return Integer.toString(wrap.getNumber());
    }
    
    @Override
    public String toString(){
        return Integer.toString(wrap.getNumber());
    }
    
}
