/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille;

import feuille.xtrn.XKCFrame;
import feuille.xtrn.XtrnFrame;

/**
 *
 * @author The Wingate 2940
 */
public class Feuille {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if(args.length > 0){
            if(args[0].equalsIgnoreCase("karaoke") && args[1].isEmpty()==false){
                XKCFrame xkcf = new XKCFrame();
                xkcf.setFolder(args[1]);
                xkcf.setVisible(true);
            }
        }else{
            MainFrame mf = new MainFrame();
            mf.setVisible(true);
        }
        
//        if(args.length==0){
//            
//        }else if(args[0].equalsIgnoreCase("karaoke")){
//            loadKaraokeTool();
//        }else if(args[0].equalsIgnoreCase("drawing")){
//            loadDrawingTool();
//        }else if(args[0].equalsIgnoreCase("analysis")){
//            loadAnalysisTool();
//        }else{
//            // TODO Command line
//        }
    }
    
    private static void loadKaraokeTool(){
//        XtrnFrame xtf = new XtrnFrame();
//        xtf.setXtrn(XtrnFrame.UseXtrn.Karaoke);
//        xtf.setVisible(true);
        XKCFrame xkcf = new XKCFrame();
        xkcf.setVisible(true);
    }
    
    private static void loadDrawingTool(){
        XtrnFrame xtf = new XtrnFrame();
        xtf.setXtrn(XtrnFrame.UseXtrn.Drawing);
        xtf.setVisible(true);
    }
    
    private static void loadAnalysisTool(){
        XtrnFrame xtf = new XtrnFrame();
        xtf.setXtrn(XtrnFrame.UseXtrn.Analysis);
        xtf.setVisible(true);
    }
}
