/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.karaoke.xfxintegration;

/**
 *
 * @author The Wingate 2940
 */
public class EncodingString {
    
    Encoding enc = Encoding.DEFAULT;
    
    public EncodingString(){
        
    }
    
    public EncodingString(String s){
        try{
            enc = enc.getEncodingFrom(Integer.parseInt(s));
        }catch(Exception exc){
            enc = Encoding.DEFAULT;
        }
    }
    
    public enum Encoding{
        ANSI(0,"ANSI"),DEFAULT(1,"Default"),SYMBOL(2,"Symbol"),MAC(77,"Mac"),
        SHIFT_JIS(128,"Shift-JIS"),HANGUL(129,"Hangeul"),JOHAB(130,"Johab"),
        GB2312(134,"GB2312"),BIG5(136,"Chinese BIG5"),GREEK(161,"Greek"),
        TURKISH(162,"Turkish"),VIETNAMESE(163,"Vietnamese"),
        HEBREW(177,"Hebrew"),ARABIC(178,"Arabic"),BALTIC(186,"Baltic"),
        RUSSIAN(204,"Russian"),THAI(222,"Thai"),EAST_EURO(238,"East european"),
        OEM(255,"OEM");

        private int number;
        private String sEnco;

        /** <p>Create a new Encoding.<br />Crée un nouveau Encoding.</p> */
        Encoding(int number, String sEnco){
            this.number = number;
            this.sEnco = sEnco;
        }

        /** <p>Return the code of the encoding.<br />
         * Retourne le code de l'encodage.</p> */
        public int getNumber(){
            return number;
        }

        /** <p>Return the name of the encoding.<br />
         * Retourne le nom de l'encodage (en anglais).</p> */
        public String getEncoding(){
            return sEnco;
        }

        /** <p>Return the string "'code' - 'encoding'".<br />
         * Retourne la chaine "'code' - 'encodage'".</p> */
        @Override
        public String toString(){
            return number+" - "+sEnco;
        }

        /** <p>Return the encoding with the given code.<br />
         * Retourne l'encodage avec le code donné.</p> */
        public Encoding getEncodingFrom(int number){
            Encoding e;
            switch(number){
                case 0: e=Encoding.ANSI; break;
                case 1: e=Encoding.DEFAULT; break;
                case 2: e=Encoding.SYMBOL; break;
                case 77: e=Encoding.MAC; break;
                case 128: e=Encoding.SHIFT_JIS; break;
                case 129: e=Encoding.HANGUL; break;
                case 130: e=Encoding.JOHAB; break;
                case 134: e=Encoding.GB2312; break;
                case 136: e=Encoding.BIG5; break;
                case 161: e=Encoding.GREEK; break;
                case 162: e=Encoding.TURKISH; break;
                case 163: e=Encoding.VIETNAMESE; break;
                case 177: e=Encoding.HEBREW; break;
                case 178: e=Encoding.ARABIC; break;
                case 186: e=Encoding.BALTIC; break;
                case 204: e=Encoding.RUSSIAN; break;
                case 222: e=Encoding.THAI; break;
                case 238: e=Encoding.EAST_EURO; break;
                case 255: e=Encoding.OEM; break;
                default: e=Encoding.DEFAULT; break;
            }
            return e;
        }
    }
    
    public void setSelectedEncoding(String s){
        try{
            enc = enc.getEncodingFrom(Integer.parseInt(s));
        }catch(Exception exc){
            enc = Encoding.DEFAULT;
        }
    }
    
    public void setSelectedEncoding(Encoding enc){
        this.enc = enc;
    }
    
    public String getSelectedEncoding(){
        return Integer.toString(enc.getNumber());
    }
    
    @Override
    public String toString(){
        return Integer.toString(enc.getNumber());
    }
    
}
