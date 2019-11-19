/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.karaoke.xfxintegration;

/**
 *
 * @author The Wingate 2940
 */
public class AlignString {
    
    Align align = Align.A2;
    
    public AlignString(){
        
    }
    
    public AlignString(String s){
        try{
            align = align.getAlignFrom(Integer.parseInt(s));
        }catch(Exception exc){
            align = Align.A2;
        }
    }
    
    public enum Align{
        A1(1,"A1"),A2(2,"A2"),A3(3,"A3"),
        A4(4,"A4"),A5(5,"A5"),A6(6,"A6"),
        A7(7,"A7"),A8(8,"A8"),A9(9,"A9");

        private int number;
        private String sa;

        /** <p>Create a new Encoding.<br />Crée un nouveau Encoding.</p> */
        Align(int number, String sa){
            this.number = number;
            this.sa = sa;
        }

        /** <p>Return the code of the encoding.<br />
         * Retourne le code de l'encodage.</p> */
        public int getNumber(){
            return number;
        }

        /** <p>Return the name of the encoding.<br />
         * Retourne le nom de l'encodage (en anglais).</p> */
        public String getAlign(){
            return sa;
        }

        /** <p>Return the string "'code' - 'encoding'".<br />
         * Retourne la chaine "'code' - 'encodage'".</p> */
        @Override
        public String toString(){
            return number+" - "+sa;
        }

        /** <p>Return the encoding with the given code.<br />
         * Retourne l'encodage avec le code donné.</p> */
        public Align getAlignFrom(int number){
            Align a;
            switch(number){
                case 1: a=Align.A1; break;
                case 2: a=Align.A2; break;
                case 3: a=Align.A3; break;
                case 4: a=Align.A4; break;
                case 5: a=Align.A5; break;
                case 6: a=Align.A6; break;
                case 7: a=Align.A7; break;
                case 8: a=Align.A8; break;
                case 9: a=Align.A9; break;
                default: a=Align.A2; break;
            }
            return a;
        }
    }
    
    public void setSelectedAlign(String s){
        try{
            align = align.getAlignFrom(Integer.parseInt(s));
        }catch(Exception exc){
            align = Align.A2;
        }
    }
    
    public void setSelectedAlign(Align align){
        this.align = align;
    }
    
    public String getSelectedAlign(){
        return Integer.toString(align.getNumber());
    }
    
    @Override
    public String toString(){
        return Integer.toString(align.getNumber());
    }
    
}
