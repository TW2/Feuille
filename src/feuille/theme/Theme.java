/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.theme;

import java.awt.Color;
import java.awt.Frame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 *
 * @author The Wingate 2940
 */
public class Theme implements ITheme {
    
    private String name = "Default";
    
    private Color control = new Color(214,217,223);                             //control
    private Color info = new Color(242,242,189);                                //info
    private Color nimbusAlertYellow = new Color(255,220,35);                    //nimbusAlertYellow
    private Color nimbusBase = new Color(51,98,140);                            //nimbusBase
    private Color nimbusDisabledText = new Color(142,143,145);                  //nimbusDisabledText
    private Color nimbusFocus = new Color(115,164,209);                         //nimbusFocus
    private Color nimbusGreen = new Color(176,179,50);                          //nimbusGreen
    private Color nimbusInfoBlue = new Color(47,92,180);                        //nimbusInfoBlue
    private Color nimbusLightBackground = new Color(255,255,255);               //nimbusLightBackground
    private Color nimbusOrange = new Color(191,98,4);                           //nimbusOrange
    private Color nimbusRed = new Color(169,46,34);                             //nimbusRed
    private Color nimbusSelectedText = new Color(255,255,255);                  //nimbusSelectedText
    private Color nimbusSelectionBackground = new Color(57,105,138);            //nimbusSelectionBackground    
    private Color text = new Color(0,0,0);                                      //text
    
    private Color activeCaption = new Color(186,190,198);                       //activeCaption
    private Color background = new Color(214,217,223);                          //background
    private Color controlDkShadow = new Color(164,171,184);                     //controlDkShadow
    private Color controlHighlight = new Color(233,236,242);                    //controlHighlight
    private Color controlLHighlight = new Color(247,248,250);                   //controlLHighlight
    private Color controlShadow = new Color(204,211,224);                       //controlShadow
    private Color controlText = new Color(0,0,0);                               //controlText
    private Color desktop = new Color(61,96,121);                               //desktop
    private Color inactiveCaption = new Color(189,193,200);                     //inactiveCaption
    private Color infoText = new Color(0,0,0);                                  //infoText
    private Color menu = new Color(237,239,242);                                //menu
    private Color menuText = new Color(0,0,0);                                  //menuText
    private Color nimbusBlueGrey = new Color(169,176,190);                      //nimbusBlueGrey
    private Color nimbusBorder = new Color(146,151,161);                        //nimbusBorder
    private Color nimbusSelection = new Color(57,105,138);                      //nimbusSelection
    private Color scrollbar = new Color(205,208,213);                           //scrollbar
    private Color textBackground = new Color(57,105,138);                       //textBackground
    private Color textForeground = new Color(0,0,0);                            //textForeground
    private Color textHighlight = new Color(57,105,138);                        //textHighlight
    private Color textHighlightText = new Color(255,255,255);                   //textHighlightText
    private Color textInactiveText = new Color(142,143,145);                    //textInactiveText
    
    public enum ColorType{
        control("control"), info("info"), nimbusAlertYellow("nimbusAlertYellow"),
        nimbusBase("nimbusBase"), nimbusDisabledText("nimbusDisabledText"),
        nimbusFocus("nimbusFocus"), nimbusGreen("nimbusGreen"), 
        nimbusInfoBlue("nimbusInfoBlue"), nimbusLightBackground("nimbusLightBackground"),
        nimbusOrange("nimbusOrange"), nimbusRed("nimbusRed"),
        nimbusSelectedText("nimbusSelectedText"), nimbusSelectionBackground("nimbusSelectionBackground"),
        text("text"), activeCaption("activeCaption"), background("background"),
        controlDkShadow("controlDkShadow"), controlHighlight("controlHighlight"),
        controlLHighlight("controlLHighlight"), controlShadow("controlShadow"),
        controlText("controlText"), desktop("desktop"), inactiveCaption("inactiveCaption"),
        infoText("infoText"), menu("menu"), menuText("menuText"), nimbusBlueGrey("nimbusBlueGrey"),
        nimbusBorder("nimbusBorder"), nimbusSelection("nimbusSelection"), scrollbar("scrollbar"),
        textBackground("textBackground"), textForeground("textForeground"),
        textHighlight("textHighlight"), textHighlightText("textHighlightText"),
        textInactiveText("textInactiveText");
        
        String key;
        
        ColorType(String key){
            this.key = key;
        }
        
        public String getKey(){
            return key;
        }
    }
    
    public Theme(){
        
    }

    public Theme(Color nimbusBase, Color control, String name){
        this.nimbusBase = nimbusBase;
        this.control = control;
        this.name = name;
    }
    
    public Theme(Color nimbusBase, Color control, Color text, String name){
        this.nimbusBase = nimbusBase;
        this.control = control;
        this.text = text;
        this.name = name;
    }
    
    public Theme(Color nimbusBase, Color control, Color text, Color info, String name){
        this.nimbusBase = nimbusBase;
        this.control = control;
        this.text = text;
        this.info = info;
        this.name = name;
    }
    
    @Override
    public void setNimbusBaseColor(Color c) {
        nimbusBase = c;
    }

    @Override
    public Color getNimbusBaseColor() {
        return nimbusBase;
    }

    @Override
    public void setControlColor(Color c) {
        control = c;
    }

    @Override
    public Color getControlColor() {
        return control;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public String toString(){
        return name;
    }

    @Override
    public void setTextColor(Color c) {
        text = c;
    }

    @Override
    public Color getTextColor() {
        return text;
    }

    @Override
    public void setInfoColor(Color c) {
        info = c;
    }

    @Override
    public Color getInfoColor() {
        return info;
    }
    
    public void applyTheme(Frame frame){
        UIManager.put(ColorType.control.getKey(),control);
        UIManager.put(ColorType.info.getKey(),info);
        UIManager.put(ColorType.nimbusAlertYellow.getKey(),nimbusAlertYellow);
        UIManager.put(ColorType.nimbusBase.getKey(),nimbusBase);
        UIManager.put(ColorType.nimbusDisabledText.getKey(),nimbusDisabledText);
        UIManager.put(ColorType.nimbusFocus.getKey(),nimbusFocus);
        UIManager.put(ColorType.nimbusGreen.getKey(),nimbusGreen);
        UIManager.put(ColorType.nimbusInfoBlue.getKey(),nimbusInfoBlue);
        UIManager.put(ColorType.nimbusLightBackground.getKey(),nimbusLightBackground);
        UIManager.put(ColorType.nimbusOrange.getKey(),nimbusOrange);
        UIManager.put(ColorType.nimbusRed.getKey(),nimbusRed);
        UIManager.put(ColorType.nimbusSelectedText.getKey(),nimbusSelectedText);
        UIManager.put(ColorType.nimbusSelectionBackground.getKey(),nimbusSelectionBackground);
        UIManager.put(ColorType.text.getKey(),text);
        UIManager.put(ColorType.activeCaption.getKey(),activeCaption);
        UIManager.put(ColorType.background.getKey(),background);
        UIManager.put(ColorType.controlDkShadow.getKey(),controlDkShadow);
        UIManager.put(ColorType.controlHighlight.getKey(),controlHighlight);
        UIManager.put(ColorType.controlLHighlight.getKey(),controlLHighlight);
        UIManager.put(ColorType.controlShadow.getKey(),controlShadow);
        UIManager.put(ColorType.controlText.getKey(),controlText);
        UIManager.put(ColorType.desktop.getKey(),desktop);
        UIManager.put(ColorType.inactiveCaption.getKey(),inactiveCaption);
        UIManager.put(ColorType.infoText.getKey(),infoText);
        UIManager.put(ColorType.menu.getKey(),menu);
        UIManager.put(ColorType.menuText.getKey(),menuText);
        UIManager.put(ColorType.nimbusBlueGrey.getKey(),nimbusBlueGrey);
        UIManager.put(ColorType.nimbusBorder.getKey(),nimbusBorder);
        UIManager.put(ColorType.nimbusSelection.getKey(),nimbusSelection);
        UIManager.put(ColorType.scrollbar.getKey(),scrollbar);
        UIManager.put(ColorType.textBackground.getKey(),textBackground);
        UIManager.put(ColorType.textForeground.getKey(),textForeground);
        UIManager.put(ColorType.textHighlight.getKey(),textHighlight);
        UIManager.put(ColorType.textHighlightText.getKey(),textHighlightText);
        UIManager.put(ColorType.textInactiveText.getKey(),textInactiveText);
        SwingUtilities.updateComponentTreeUI(frame);
    }
    
    public void setColor(ColorType ct, Color c){
        switch(ct){
            case control : control = c; break;
            case info : info = c; break;
            case nimbusAlertYellow : nimbusAlertYellow = c; break;
            case nimbusBase : nimbusBase = c; break;
            case nimbusDisabledText : nimbusDisabledText = c; break;
            case nimbusFocus : nimbusFocus = c; break;
            case nimbusGreen : nimbusGreen = c; break;
            case nimbusInfoBlue : nimbusInfoBlue = c; break;
            case nimbusLightBackground : nimbusLightBackground = c; break;
            case nimbusOrange : nimbusOrange = c; break;
            case nimbusRed : nimbusRed = c; break;
            case nimbusSelectedText : nimbusSelectedText = c; break;
            case nimbusSelectionBackground : nimbusSelectionBackground = c; break;
            case text : text = c; break;
            case activeCaption : activeCaption = c; break;
            case background : background = c; break;
            case controlDkShadow : controlDkShadow = c; break;
            case controlHighlight : controlHighlight = c; break;
            case controlLHighlight : controlLHighlight = c; break;
            case controlShadow : controlShadow = c; break;
            case controlText : controlText = c; break;
            case desktop : desktop = c; break;
            case inactiveCaption : inactiveCaption = c; break;
            case infoText : infoText = c; break;
            case menu : menu = c; break;
            case menuText : menuText = c; break;
            case nimbusBlueGrey : nimbusBlueGrey = c; break;
            case nimbusBorder : nimbusBorder = c; break;
            case nimbusSelection : nimbusSelection = c; break;
            case scrollbar : scrollbar = c; break;
            case textBackground : textBackground = c; break;
            case textForeground : textForeground = c; break;
            case textHighlight : textHighlight = c; break;
            case textHighlightText : textHighlightText = c; break;
            case textInactiveText : textInactiveText = c; break;
        }
    }
}
