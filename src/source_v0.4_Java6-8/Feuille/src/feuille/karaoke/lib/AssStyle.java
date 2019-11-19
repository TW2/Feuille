/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package feuille.karaoke.lib;

/**
 * <p>This class is a storage for an ASS or ASS2 style.<br />
 * Cette classe est un espace de stockage pour une style ASS ou ASS2.</p>
 * @author The Wingate 2940
 */
public class AssStyle {

    /** <p>Create a basic ASS style : 'Default'.<br />
     * Cr&#233;e un style ASS basique : 'Default'.</p> */
    public AssStyle(){
        
    }
    
    /* Voici les variables */
    
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
    private int MarginB = 10;
    private int MarginT = 10;
    private int AlphaLevel = 0;
    private int Encoding = 0;
    private int RelativeTo = 0;
    
    /* Voici l'&#233;numu&#233;ration pour acc&#233;der aux variables */
    
    /** <p>A choice of elements for ASS or ASS2 style.<br />
     * Un choix d'éléments pour un style ASS ou ASS2.</p> 
     * @see setElement
     * @see getElement */
    public enum AssStyleType{
        name, fontname, fontsize, primarycolour, secondarycolour,
        outlinecolour, backcolour, bold, italic, underline,
        strikeout, scaleX, scaleY, spacing, angle, borderstyle,
        outline, shadow, alignment, marginL, marginR, marginV,
        marginB, marginT, alphalevel, encoding, relativeto;
    }
    
    /* Voici les m&#233;thodes */

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

    /** <p>Set the size of the bottom margin.<br />
     * Définit la taille de la marge du bas.</p> */
    public void setMarginB(int mB){
        MarginB = mB;
    }

    /** <p>Get the size of the bottom margin.<br />
     * Obtient la taille de la marge du bas.</p> */
    public int getMarginB(){
        return MarginB;
    }

    /** <p>Set the size of the top margin.<br />
     * Définit la taille de la marge du haut.</p> */
    public void setMarginT(int mT){
        MarginT = mT;
    }

    /** <p>Get the size of the top margin.<br />
     * Obtient la taille de la marge du haut.</p> */
    public int getMarginT(){
        return MarginT;
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

    //<editor-fold defaultstate="collapsed" desc=" Import Export methods ">

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

    /** <p>Store the elements of this style.<br />
     * Stocke les éléments de ce style.</p> */
    public void fromAssStyleString(String style){
        String[] table = style.split(",");
        Name = table[0].substring(7);
        Fontname = table[1];
        Fontsize = Double.parseDouble(table[2]);
        PrimaryColour = table[3].substring(2);
        SecondaryColour = table[4].substring(2);
        OutlineColor = table[5].substring(2);
        BackColour = table[6].substring(2);
        Bold = toBoolean(table[7]);
        Italic = toBoolean(table[8]);
        Underline = toBoolean(table[9]);
        Strikeout = toBoolean(table[10]);
        ScaleX = Double.parseDouble(table[11]);
        ScaleY = Double.parseDouble(table[12]);
        Spacing = Double.parseDouble(table[13]);
        Angle = Double.parseDouble(table[14]);
        BorderStyle = Integer.parseInt(table[15]);
        Outline = Integer.parseInt(table[16]);
        Shadow = Integer.parseInt(table[17]);
        Alignment = Integer.parseInt(table[18]);
        MarginL = Integer.parseInt(table[19]);
        MarginR = Integer.parseInt(table[20]);
        MarginV = Integer.parseInt(table[21]);
        Encoding = Integer.parseInt(table[22]);
    }

    /** <p>Create a linked list of several styles in a string.<br />
     * Crée une liste relié de quelques styles en les mettant dans une chaine.</p> 
     * @see unlinkAssStyles */
    public static String linkAssStyles(java.util.List<AssStyle> ast){
        String s = "";
        for(AssStyle as : ast){
            s = s+";"+as.toAssStyleString();
        }
        return s;
    }

    /** <p>Get a list of styles from a string.<br />
     * Obtient une list de styles à partir d'une chaine.</p>
     * @see linkAssStyles */
    public static java.util.List<AssStyle> unlinkAssStyles(String s){
        String[] table = s.split(";");
        java.util.List<AssStyle> lst = new java.util.ArrayList<AssStyle>();
        for(String str : table){
            if(str.length()>0){
                AssStyle as = new AssStyle();
                as.fromAssStyleString(str);
                lst.add(as);
            }
        }
        return lst;
    }
    
    /** <p>Get the clone of an ASS style.<br />
     * Obtient le clone d'un style ASS.</p> */
    public static AssStyle cloneAssStyle(AssStyle org){
        AssStyle copy = new AssStyle();
        copy.setName(org.getName());
        copy.setFontname(org.getFontname());
        copy.setFontsize(org.getFontsize());
        copy.setTextColor(org.getTextCColor(), org.getTextAlpha());
        copy.setKaraColor(org.getKaraCColor(), org.getKaraAlpha());
        copy.setOutlineColor(org.getOutlineCColor(), org.getOutlineAlpha());
        copy.setBackColor(org.getBackCColor(), org.getBackAlpha());
        copy.setBold(org.getBold());
        copy.setItalic(org.getItalic());
        copy.setUnderline(org.getUnderline());
        copy.setStrikeout(org.getStrikeout());
        copy.setScaleX(org.getScaleX());
        copy.setScaleY(org.getScaleY());
        copy.setSpacing(org.getSpacing());
        copy.setAngle(org.getAngle());
        copy.setBorderStyle(org.getBorderStyle());
        copy.setOutline(org.getOutline());
        copy.setShadow(org.getShadow());
        copy.setAlignment(org.getAlignment());
        copy.setMarginL(org.getMarginL());
        copy.setMarginR(org.getMarginR());
        copy.setMarginV(org.getMarginV());
        copy.setMarginB(org.getMarginB());
        copy.setMarginT(org.getMarginT());
        copy.setEncoding(org.getEncoding());
        return copy;
    }

    //</editor-fold>
    
    /** <p>Set an element. Which can not be a color or an alignment.<br />
     * Définit un élément. Lequel ne peut pas être une couleur ou un alignement.</p> */
    public void setElement(AssStyleType ast, String value){
        switch(ast){
            case name: Name = value; break;
            case fontname: Fontname = value; break;
            case fontsize: Fontsize = toDouble(value,28); break;
            case bold: Bold = toBoolean(value); break;
            case italic: Italic = toBoolean(value); break;
            case underline: Underline = toBoolean(value); break;
            case strikeout: Strikeout = toBoolean(value); break;
            case scaleX: ScaleX = toDouble(value,100); break;
            case scaleY: ScaleY = toDouble(value,100); break;
            case spacing: Spacing = toDouble(value,0); break;
            case angle: Angle = toDouble(value,0); break;
            case borderstyle: BorderStyle = toInt(value,1); break;
            case outline: Outline = toInt(value,2); break;
            case shadow: Shadow = toInt(value,0); break;
            case marginL: MarginL = toInt(value,10); break;
            case marginR: MarginR = toInt(value,10); break;
            case marginV: MarginV = toInt(value,10); break;
            case marginB: MarginB = toInt(value,10); break;
            case marginT: MarginT = toInt(value,10); break;
            case alphalevel: AlphaLevel = toInt(value,0); break;
            case encoding: Encoding = toInt(value,0); break;
            case relativeto: RelativeTo = toInt(value,0); break;
        }
    }
    
    /** <p>Set an element. Which can be only a color or an alignment.<br />
     * Définit un élément. Lequel ne peut être qu'une couleur ou un alignement.</p> */
    public void setElement(AssStyleType ast, String value, String version){
        switch(ast){
            case primarycolour:
                PrimaryColour = toColor(value,version,"0000FFFF"); break;
            case secondarycolour:
                SecondaryColour = toColor(value,version,"0000FFFF"); break;
            case outlinecolour:
                OutlineColor = toColor(value,version,"00000000"); break;
            case backcolour:
                BackColour = toColor(value,version,"00000000"); break;
            case alignment:
                Alignment = toAlignment(value,version,2); break;
        }
    }
    
    /** <p>Get an element. Which can not be a color or an alignment.<br />
     * Obtient un élément. Lequel ne peut pas être une couleur ou un alignement.</p> */
    public String getElement(AssStyleType ast){
        switch(ast){
            case name: return Name;
            case fontname: return Fontname;
            case fontsize: return Integer.toString((int)Fontsize);
            case bold: return fromBoolean(Bold);
            case italic: return fromBoolean(Italic);
            case underline: return fromBoolean(Underline);
            case strikeout: return fromBoolean(Strikeout);
            case scaleX: return Integer.toString((int)ScaleX);
            case scaleY: return Integer.toString((int)ScaleY);
            case spacing: return Integer.toString((int)Spacing);
            case angle: return Integer.toString((int)Angle);
            case borderstyle: return Integer.toString(BorderStyle);
            case outline: return Integer.toString(Outline);
            case shadow: return Integer.toString(Shadow);
            case marginL: return Integer.toString(MarginL);
            case marginR: return Integer.toString(MarginR);
            case marginV: return Integer.toString(MarginV);
            case marginB: return Integer.toString(MarginB);
            case marginT: return Integer.toString(MarginT);
            case alphalevel: return Integer.toString(AlphaLevel);
            case encoding: return Integer.toString(Encoding);
            case relativeto: return Integer.toString(RelativeTo);
            default: return "";
        }
    }
    
    /** <p>Get an element. Which can be only a color or an alignment.<br />
     * Obtient un élément. Lequel ne peut être qu'une couleur ou un alignement.</p> */
    public String getElement(AssStyleType ast, String version){
        switch(ast){
            case primarycolour:
                if(version.contains("+")){//ASS
                    return "&H"+PrimaryColour;
                }else{//SSA
                    return toSsaColor(PrimaryColour);
                }
            case secondarycolour:
                if(version.contains("+")){//ASS
                    return "&H"+SecondaryColour;
                }else{//SSA
                    return toSsaColor(SecondaryColour);
                }
            case outlinecolour:
                if(version.contains("+")){//ASS
                    return "&H"+OutlineColor;
                }else{//SSA
                    return toSsaColor(OutlineColor);
                }
            case backcolour:
                if(version.contains("+")){//ASS
                    return "&H"+BackColour;
                }else{//SSA
                    return toSsaColor(BackColour);
                }
            case alignment:
                if(version.contains("+")){//ASS
                    return Integer.toString(Alignment);
                }else{//SSA
                    return toSsaAlignment(Alignment);
                }
            default: return "";
        }
    }
    
    /** <p>Try to convert a string into a double.<br />
     * Essaie de convertir une chaine en double.</p> */
    private double toDouble(String s, double def){
        double value = def;
        try{
            value = Double.parseDouble(s);
        }catch(NumberFormatException nfe){
            
        }
        return value;
    }
    
    /** <p>Try to convert a string into a boolean. 
     * If the string is "-1" then it's true else it's false.<br />
     * Essaie de convertir une chaine en booléen. 
     * Si la chaine est "-1" alors c'est vrai sinon c'est faux.</p> */
    public boolean toBoolean(String s){
        if(s.equals("-1")){
            return true;
        }else{
            return false;
        }        
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
    
    /** <p>Try to convert a string into a integer.<br />
     * Essaie de convertir une chaine en entier.</p> */
    private int toInt(String s, int def){
        int value = def;
        try{
            value = Integer.parseInt(s);
        }catch(NumberFormatException nfe){
            
        }
        return value;
    }
    
    /** <p>Return a color in ASS format including the alpha. From an ASS or SSA color.<br />
     * Retourne une couleur au format ASS incluant l'alpha. Depuis une couleur ASS ou SSA.</p> */
    private String toColor(String s, String version, String def){
        String value = def;
        if(version.contains("+")){
            //Traitement du script ASS
            try{
                value = s.substring(2);
            }catch(IndexOutOfBoundsException ioobe){
                
            }            
        }else{
            //Traitement du script SSA
            String color;
            String trans;
            
            // On retrouve les entiers puis les digits de la couleur
            // et de la transparence.
            try{
                // Conversion String > Integer
                int ssaValue = Integer.parseInt(s);
                int transValue = Integer.parseInt(
                        getElement(AssStyleType.alphalevel));
                
                // Conversion Integer > Hexadecimale
                color = Integer.toHexString(ssaValue);
                trans = Integer.toHexString(transValue);
            }catch(NumberFormatException nfe){
                
                return value;
            }
            
            // On fait en sorte que le nombre hexadecimal de la couleur
            // ait 6 digits.
            if(color.length()<2){        // Il y a un seul digit.
                color = "00000"+color;
            }else if(color.length()<3){  // Il y a deux digits.
                color = "0000"+color;
            }else if(color.length()<4){  // Il y a trois digits.
                color = "000"+color;
            }else if(color.length()<5){  // Il y a quatre digits.
                color = "00"+color;
            }else if(color.length()<6){  // Il y a cinq digits.
                color = "0"+color;
            }
            
            // On fait en sorte que le nombre hexadecimal de la transparence
            // ait 2 digits.
            if(trans.length()<2){        // Il y a un seul digit.
                trans = "0"+trans;
            }
            
            // On concat&#233;ne les deux valeurs.
            value = trans+color;
        }
        return value;
    }
    
    /** <p>Return a color in SSA format from a color in ASS format.<br />
     * Retourne une couleur au format SSA depuis une couleur au format ASS.</p> */
    private String toSsaColor(String s){        
        try{
            String value = s.substring(2);
            value = String.valueOf(Integer.parseInt(value, 16));
            return value;
        }catch(IndexOutOfBoundsException ioobe){
            return "0";
        }catch(NumberFormatException nbe){
            return "0";
        }
    }
    
    /** <p>Return an alignment in ASS format from an alignment in ASS or SSA format.<br />
     * Retourne un alignement au format ASS depuis un alignement au format ASS ou SSA.</p> */
    private int toAlignment(String s, String version, int def){
        int value = def;
        if(version.contains("+")){
            //Traitement du script ASS
            if(s.equals("1")){return 1;}
            if(s.equals("2")){return 2;}
            if(s.equals("3")){return 3;}
            if(s.equals("4")){return 4;}
            if(s.equals("5")){return 5;}
            if(s.equals("6")){return 6;}
            if(s.equals("7")){return 7;}
            if(s.equals("8")){return 8;}
            if(s.equals("9")){return 9;}
        }else{
            //Traitement du script SSA
            if(s.equals("1")){return 1;}
            if(s.equals("2")){return 2;}
            if(s.equals("3")){return 3;}
            if(s.equals("9")){return 4;}
            if(s.equals("10")){return 5;}
            if(s.equals("11")){return 6;}
            if(s.equals("5")){return 7;}
            if(s.equals("6")){return 8;}
            if(s.equals("7")){return 9;}
        }
        return value;
    }
    
    /** <p>Return an alignment in SSA format from an alignment in ASS format.<br />
     * Retourne un alignement au format SSA depuis un alignement au format ASS.</p> */
    private String toSsaAlignment(int value){
        if(value==1){return "1";}
        if(value==2){return "2";}
        if(value==3){return "3";}
        if(value==4){return "9";}
        if(value==5){return "10";}
        if(value==6){return "11";}
        if(value==7){return "5";}
        if(value==8){return "6";}
        if(value==9){return "7";}
        
        return "2"; //Par d&#233;faut
    }

    /** <p>Get the name of this style.<br />
     * Obtient le nom de ce style.</p> */
    @Override    
    public String toString(){
        return getName();
    }
    
    /** <p>Get a clone of this style.<br />Retourne un clone de ce style.</p>
     * @throws CloneNotSupportedException */
    public AssStyle getClone() throws CloneNotSupportedException{
        return (AssStyle)this.clone();
    }
    
}
