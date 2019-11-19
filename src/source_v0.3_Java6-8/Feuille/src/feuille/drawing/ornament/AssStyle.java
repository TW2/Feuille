/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.drawing.ornament;

/**
 *
 * @author The Wingate 2940
 */
public class AssStyle {
    
    private String Name = "Default";
    private String Fontname = "Arial";
    private double Fontsize = 28;
    private String PrimaryColour = "0000FFFF";
    private String SecondaryColour = "0000FFFF";
    private String OutlineColor = "00000000";
    private String BackColour = "00000000";
    private boolean Bold = false;
    private boolean Italic = false;
    private boolean Underline = false;
    private boolean Strikeout = false;
    private double ScaleX = 100;
    private double ScaleY = 100;
    private double Spacing = 0;
    private double Angle = 0;
    private int BorderStyle = 1;
    private int Outline = 2;
    private int Shadow = 0;
    private int Alignment = 2;
    private int MarginL = 10;
    private int MarginR = 10;
    private int MarginV = 10;
    private int Encoding = 0;
    
    public AssStyle(){
        
    }
    
    //<editor-fold defaultstate="collapsed" desc=" Get Set methods ">

    /** <p>Set the name.<br />Définit le nom.</p> */
    public void setName(String name){
        Name = name;
    }

    /** <p>Get the name.<br />Obtient le nom.</p> */
    public String getName(){
        return Name;
    }

    /** <p>Set the font name.<br />Définit le nom de la police.</p> */
    public void setFontname(String fontname){
        Fontname = fontname;
    }

    /** <p>Get the font name.<br />Obtient le nom de la police.</p> */
    public String getFontname(){
        return Fontname;
    }

    /** <p>Set the font size.<br />Définit la taille de la police.</p> */
    public void setFontsize(double fontsize){
        Fontsize = fontsize;
    }

    /** <p>Get the font size.<br />Obtient la taille de la police.</p> */
    public double getFontsize(){
        return Fontsize;
    }
    
    /** <p>Get the font.<br />Obtient la police.</p> */
    public java.awt.Font getFont(){
        int styleOfFont = java.awt.Font.PLAIN;
        if(getBold() && getItalic()){
            styleOfFont = java.awt.Font.BOLD+java.awt.Font.ITALIC;
        }else if(getBold()){
            styleOfFont = java.awt.Font.BOLD;
        }else if(getItalic()){
            styleOfFont = java.awt.Font.ITALIC;
        }
        java.awt.Font font = new java.awt.Font(
                getFontname(),styleOfFont,(int)getFontsize());
        return font;
    }

    /** <p>Put the text color in BBGGRR format with the alpha.<br />
     * Définit la couleur du texte au format BBVVRR avec l'alpha.</p> */
    public void setTextColor(String textcolor, String alpha){
        if(alpha.length()==1){alpha="0"+alpha;}
        PrimaryColour = alpha+textcolor;
    }

    /** <p>Put the text color in BBGGRR format with the alpha.<br />
     * Définit la couleur du texte au format BBVVRR avec l'alpha.</p> */
    public void setTextColor(java.awt.Color textcolor, String alpha){
        String sRed = Integer.toString(textcolor.getRed(), 16);
        if(sRed.length()==1){sRed="0"+sRed;}
        String sGreen = Integer.toString(textcolor.getGreen(), 16);
        if(sGreen.length()==1){sGreen="0"+sGreen;}
        String sBlue = Integer.toString(textcolor.getBlue(), 16);
        if(sBlue.length()==1){sBlue="0"+sBlue;}
        if(alpha.length()==1){alpha="0"+alpha;}
        PrimaryColour = alpha+sBlue+sGreen+sRed;
    }

    /** <p>Get the text color in string format.<br />
     * Obtient la chaine de la couleur du texte.</p> */
    public String getTextSColor(){
        return PrimaryColour.substring(2);
    }

    /** <p>Get the text alpha in string format.<br />
     * Obtient la chaine de l'alpha.</p> */
    public String getTextAlpha(){
        return PrimaryColour.substring(0,2);
    }

    /** <p>Get the text color.<br />
     * Obtient la couleur du texte.</p> */
    public java.awt.Color getTextCColor(){
        int blue = Integer.parseInt(PrimaryColour.substring(2,4), 16);
        int green = Integer.parseInt(PrimaryColour.substring(4,6), 16);
        int red = Integer.parseInt(PrimaryColour.substring(6), 16);
        return new java.awt.Color(red,green,blue);
    }

    /** <p>Put the karaoke color in BBGGRR format with the alpha.<br />
     * Définit la couleur du karaoke au format BBVVRR avec l'alpha.</p> */
    public void setKaraColor(String textcolor, String alpha){
        if(alpha.length()==1){alpha="0"+alpha;}
        SecondaryColour = alpha+textcolor;
    }

    /** <p>Put the karaoke color in BBGGRR format with the alpha.<br />
     * Définit la couleur du karaoke au format BBVVRR avec l'alpha.</p> */
    public void setKaraColor(java.awt.Color textcolor, String alpha){
        String sRed = Integer.toString(textcolor.getRed(), 16);
        if(sRed.length()==1){sRed="0"+sRed;}
        String sGreen = Integer.toString(textcolor.getGreen(), 16);
        if(sGreen.length()==1){sGreen="0"+sGreen;}
        String sBlue = Integer.toString(textcolor.getBlue(), 16);
        if(sBlue.length()==1){sBlue="0"+sBlue;}
        if(alpha.length()==1){alpha="0"+alpha;}
        SecondaryColour = alpha+sBlue+sGreen+sRed;
    }

    /** <p>Get the karaoke color in string format.<br />
     * Obtient la chaine de la couleur du karaoke.</p> */
    public String getKaraSColor(){
        return SecondaryColour.substring(2);
    }

    /** <p>Get the karaoke alpha in string format.<br />
     * Obtient la chaine de l'alpha.</p> */
    public String getKaraAlpha(){
        return SecondaryColour.substring(0,2);
    }

    /** <p>Get the karaoke color.<br />
     * Obtient la couleur du karaoke.</p> */
    public java.awt.Color getKaraCColor(){
        int blue = Integer.parseInt(SecondaryColour.substring(2,4), 16);
        int green = Integer.parseInt(SecondaryColour.substring(4,6), 16);
        int red = Integer.parseInt(SecondaryColour.substring(6), 16);
        return new java.awt.Color(red,green,blue);
    }

    /** <p>Put the outline color in BBGGRR format with the alpha.<br />
     * Définit la couleur de la bordure au format BBVVRR avec l'alpha.</p> */
    public void setOutlineColor(String textcolor, String alpha){
        if(alpha.length()==1){alpha="0"+alpha;}
        OutlineColor = alpha+textcolor;
    }

    /** <p>Put the outline color in BBGGRR format with the alpha.<br />
     * Définit la couleur de la bordure au format BBVVRR avec l'alpha.</p> */
    public void setOutlineColor(java.awt.Color textcolor, String alpha){
        String sRed = Integer.toString(textcolor.getRed(), 16);
        if(sRed.length()==1){sRed="0"+sRed;}
        String sGreen = Integer.toString(textcolor.getGreen(), 16);
        if(sGreen.length()==1){sGreen="0"+sGreen;}
        String sBlue = Integer.toString(textcolor.getBlue(), 16);
        if(sBlue.length()==1){sBlue="0"+sBlue;}
        if(alpha.length()==1){alpha="0"+alpha;}
        OutlineColor = alpha+sBlue+sGreen+sRed;
    }

    /** <p>Get the outline color in string format.<br />
     * Obtient la chaine de la couleur de la bordure.</p> */
    public String getOutlineSColor(){
        return OutlineColor.substring(2);
    }

    /** <p>Get the outline alpha in string format.<br />
     * Obtient la chaine de l'alpha.</p> */
    public String getOutlineAlpha(){
        return OutlineColor.substring(0,2);
    }

    /** <p>Get the outline color.<br />
     * Obtient la couleur de la bordure.</p> */
    public java.awt.Color getOutlineCColor(){
        int blue = Integer.parseInt(OutlineColor.substring(2,4), 16);
        int green = Integer.parseInt(OutlineColor.substring(4,6), 16);
        int red = Integer.parseInt(OutlineColor.substring(6), 16);
        return new java.awt.Color(red,green,blue);
    }

    /** <p>Put the shadow color in BBGGRR format with the alpha.<br />
     * Définit la couleur de l'ombre au format BBVVRR avec l'alpha.</p> */
    public void setBackColor(String textcolor, String alpha){
        if(alpha.length()==1){alpha="0"+alpha;}
        BackColour = alpha+textcolor;
    }

    /** <p>Put the shadow color in BBGGRR format with the alpha.<br />
     * Définit la couleur de l'ombre au format BBVVRR avec l'alpha.</p> */
    public void setBackColor(java.awt.Color textcolor, String alpha){
        String sRed = Integer.toString(textcolor.getRed(), 16);
        if(sRed.length()==1){sRed="0"+sRed;}
        String sGreen = Integer.toString(textcolor.getGreen(), 16);
        if(sGreen.length()==1){sGreen="0"+sGreen;}
        String sBlue = Integer.toString(textcolor.getBlue(), 16);
        if(sBlue.length()==1){sBlue="0"+sBlue;}
        if(alpha.length()==1){alpha="0"+alpha;}
        BackColour = alpha+sBlue+sGreen+sRed;
    }

    /** <p>Get the shadow color in string format.<br />
     * Obtient la chaine de la couleur de l'ombre.</p> */
    public String getBackSColor(){
        return BackColour.substring(2);
    }

    /** <p>Get the shadow alpha in string format.<br />
     * Obtient la chaine de l'alpha.</p> */
    public String getBackAlpha(){
        return BackColour.substring(0,2);
    }

    /** <p>Get the shadow color.<br />
     * Obtient la couleur de l'ombre.</p> */
    public java.awt.Color getBackCColor(){
        int blue = Integer.parseInt(BackColour.substring(2,4), 16);
        int green = Integer.parseInt(BackColour.substring(4,6), 16);
        int red = Integer.parseInt(BackColour.substring(6), 16);
        return new java.awt.Color(red,green,blue);
    }

    /** <p>Set if this value 'bold' is 'true' or 'false'.<br />
     * Définit si cette valeur 'gras' est 'vrai' au 'faux'.</p> */
    public void setBold(boolean bold){
        Bold = bold;
    }

    /** <p>Get the value of 'bold'.<br />
     * Obtient la valeur de 'gras'.</p> */
    public boolean getBold(){
        return Bold;
    }

    /** <p>Set if this value 'italic' is 'true' or 'false'.<br />
     * Définit si cette valeur 'italique' est 'vrai' au 'faux'.</p> */
    public void setItalic(boolean italic){
        Italic = italic;
    }

    /** <p>Get the value of 'italic'.<br />
     * Obtient la valeur de 'italique'.</p> */
    public boolean getItalic(){
        return Italic;
    }

    /** <p>Set if this value 'underline' is 'true' or 'false'.<br />
     * Définit si cette valeur 'souligné' est 'vrai' au 'faux'.</p> */
    public void setUnderline(boolean underline){
        Underline = underline;
    }

    /** <p>Get the value of 'underline'.<br />
     * Obtient la valeur de 'souligné'.</p> */
    public boolean getUnderline(){
        return Underline;
    }

    /** <p>Set if this value 'strikeout' is 'true' or 'false'.<br />
     * Définit si cette valeur 'barré' est 'vrai' au 'faux'.</p> */
    public void setStrikeout(boolean strikeout){
        Strikeout = strikeout;
    }

    /** <p>Get the value of 'strikeout'.<br />
     * Obtient la valeur de 'barré'.</p> */
    public boolean getStrikeout(){
        return Strikeout;
    }

    /** <p>Set the scale on X.<br />Définit l'échelle en X.</p> */
    public void setScaleX(double scx){
        ScaleX = scx;
    }

    /** <p>Get the scale on X.<br />Obtient l'&chelle en X.</p> */
    public double getScaleX(){
        return ScaleX;
    }

    /** <p>Set the scale on Y.<br />Définit l'échelle en Y.</p> */
    public void setScaleY(double scy){
        ScaleY = scy;
    }

    /** <p>Get the scale on Y.<br />Obtient l'échelle en Y.</p> */
    public double getScaleY(){
        return ScaleY;
    }
    
    /** <p>Set the angle on Z.<br />Définit l'angle sur Z.</p> */
    public void setAngle(double angle){
        Angle = angle;
    }

    /** <p>Get the angle on Z.<br />Obtient l'angle sur Z.</p> */
    public double getAngle(){
        return Angle;
    }

    /** <p>Set the spacing between letters.<br />
     * Définit l'espace entre les lettres.</p> */
    public void setSpacing(double spacing){
        Spacing = spacing;
    }

    /** <p>Get the spacing between letters.<br />
     * Obtient l'espace entre les lettres.</p> */
    public double getSpacing(){
        return Spacing;
    }
    
    /** <p>Set the border style.<br />
     * If bs=1 -> no opaque box.<br />
     * If bs=3 -> opaque box.<br /><br />
     * Définit le style de bordure.<br />
     * Si bs=1 -> pas de boîte opaque.<br />
     * Si bs=3 -> boîte opaque.</p> */
    public void setBorderStyle(int bs){
        BorderStyle = bs;
    }
    
    /** <p>Set the border style.<br />
     * If bs=false -> no opaque box.<br />
     * If bs=true -> opaque box.<br /><br />
     * Définit le style de bordure.<br />
     * Si bs=faux -> pas de boîte opaque.<br />
     * Si bs=vrai -> boîte opaque.</p> */
    public void setBorderStyle(boolean bs){
        if(bs==true){BorderStyle=3;}//Opaque box
        if(bs==false){BorderStyle=1;}//No opaque box
    }
    
    /** <p>Get the border style.<br />
     * If no opaque box -> 1.<br />
     * If opaque box -> 3.<br /><br />
     * Obtient le style de bordure.<br />
     * Si pas de boîte opaque -> 1.<br />
     * Si boîte opaque -> 3.</p> */
    public int getBorderStyle(){
        return BorderStyle;
    }
    
    /** <p>Get the border style.<br />
     * If no opaque box -> false.<br />
     * If opaque box -> true.<br /><br />
     * Obtient le style de bordure.<br />
     * Si pas de boîte opaque -> faux.<br />
     * Si boîte opaque -> vrai.</p> */
    public boolean getBorderSStyle(){
        if(BorderStyle==1){//No opaque box
            return false;
        }else{//Opaque box
            return true;
        }
    }

    /** <p>Set the width of the outline.<br />
     * Définit la largeur de la bordure.</p> */
    public void setOutline(int outline){
        Outline = outline;
    }

    /** <p>Get the width of the outline.<br />
     * Obtient la largeur de la bordure.</p> */
    public int getOutline(){
        return Outline;
    }

    /** <p>Set the depth of the shadow.<br />
     * Définit la profondeur de l'ombre.</p> */
    public void setShadow(int shadow){
        Shadow = shadow;
    }

    /** <p>Get the depth of the shadow.<br />
     * Obtient la profondeur de l'ombre.</p> */
    public int getShadow(){
        return Shadow;
    }

    /** <p>Set the alignment of the text.<br />
     * Définit l'alignement du texte.</p> */
    public void setAlignment(int alignment){
        Alignment = alignment;
    }

    /** <p>Get the alignment of the text.<br />
     * Obtient l'alignement du texte.</p> */
    public int getAlignment(){
        return Alignment;
    }

    /** <p>Set the size of the left margin.<br />
     * Définit la taille de la marge gauche.</p> */
    public void setMarginL(int mL){
        MarginL = mL;
    }

    /** <p>Get the size of the left margin.<br />
     * Obtient la taille de la marge gauche.</p> */
    public int getMarginL(){
        return MarginL;
    }

    /** <p>Set the size of the right margin.<br />
     * Définit la taille de la marge droite.</p> */
    public void setMarginR(int mR){
        MarginR = mR;
    }

    /** <p>Get the size of the right margin.<br />
     * Obtient la taille de la marge droite.</p> */
    public int getMarginR(){
        return MarginR;
    }

    /** <p>Set the size of the vertical margin.<br />
     * Définit la taille de la marge verticale.</p> */
    public void setMarginV(int mV){
        MarginV = mV;
    }

    /** <p>Get the size of the vertical margin.<br />
     * Obtient la taille de la marge verticale.</p> */
    public int getMarginV(){
        return MarginV;
    }

    /** <p>Set the encoding of the text.<br />
     * Définit l'encodage du texte.</p> */
    public void setEncoding(int encoding){
        Encoding = encoding;
    }

    /** <p>Get the encoding of the text.<br />
     * Obtient l'encodage du texte.</p> */
    public int getEncoding(){
        return Encoding;
    }

    //</editor-fold>
    
    /** <p>Return the ASS line.<br />Retourne la ligne ASS.</p> */
    public String toAssStyleString(){
        String s = "Style: ";
        s+=Name+","+Fontname+","+Fontsize+",&H"+PrimaryColour+
                ",&H"+SecondaryColour+",&H"+OutlineColor+",&H"+BackColour+
                ","+fromBoolean(Bold)+","+fromBoolean(Italic)+
                ","+fromBoolean(Underline)+","+fromBoolean(Strikeout)+
                ","+ScaleX+","+ScaleY+","+Spacing+","+Angle+","+BorderStyle+
                ","+Outline+","+Shadow+","+Alignment+","+MarginL+
                ","+MarginR+","+MarginV+","+Encoding;
        return s;
    }
    
    /** <p>Try to convert a boolean into a string. 
     * If the boolean is "-1"true then the value is "-1" else "0".<br />
     * Essaie de convertir un booléen en chaine. 
     * Si le booléen est vrai alors c'est "-1" sinon "0".</p> */
    public String fromBoolean(boolean b){
        if(b==true){
            return "-1";
        }else{
            return "0";
        }
    }
}
