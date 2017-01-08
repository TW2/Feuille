/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.karaoke.xfxintegration;

/**
 *
 * @author The Wingate 2940
 */
public class AlignOldString {
    
    AlignOld align = AlignOld.A2;
    
    public AlignOldString(){
        
    }
    
    public AlignOldString(String s){
        try{
            align = align.getAlignOldFrom(Integer.parseInt(s));
        }catch(Exception exc){
            align = AlignOld.A2;
        }
    }
    
    public enum AlignOld{
        A1(1,"A1"),A2(2,"A2"),A3(3,"A3"),
        A9(9,"A9"),A10(10,"A10"),A11(11,"A11"),
        A5(5,"A5"),A6(6,"A6"),A7(7,"A7");

        private int number;
        private String sa;

        /** <p>Create a new Encoding.<br />Crée un nouveau Encoding.</p> */
        AlignOld(int number, String sa){
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
        public String getAlignOld(){
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
        public AlignOld getAlignOldFrom(int number){
            AlignOld ao;
            switch(number){
                case 1: ao=AlignOld.A1; break;
                case 2: ao=AlignOld.A2; break;
                case 3: ao=AlignOld.A3; break;
                case 5: ao=AlignOld.A5; break;
                case 6: ao=AlignOld.A6; break;
                case 7: ao=AlignOld.A7; break;
                case 9: ao=AlignOld.A9; break;
                case 10: ao=AlignOld.A10; break;
                case 11: ao=AlignOld.A11; break;
                default: ao=AlignOld.A2; break;
            }
            return ao;
        }
    }
    
    public void setSelectedAlignOld(String s){
        try{
            align = align.getAlignOldFrom(Integer.parseInt(s));
        }catch(Exception exc){
            align = AlignOld.A2;
        }
    }
    
    public void setSelectedAlignOld(AlignOld align){
        this.align = align;
    }
    
    public String getSelectedAlignOld(){
        return Integer.toString(align.getNumber());
    }
    
    @Override
    public String toString(){
        return Integer.toString(align.getNumber());
    }
    
}
