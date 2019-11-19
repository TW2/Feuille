package feuille.karaoke.lib;

import java.io.*;
//import java.nio.charset.*;
import java.awt.Color;
import java.util.List;
import java.util.regex.*;
import javax.swing.table.DefaultTableModel;
import feuille.karaoke.lib.AssInfos.AssInfosType;
import feuille.karaoke.lib.AssStyle.AssStyleType;
import feuille.lib.Configuration;

/* Created on 4 Août 2005, 13:30 */
/** <p>This class is a module for input/output functions.<br />
 * Cette classe est un module pour les fonctions d'entrée/sortie.</p>  
 * @author The Wingate 2940
 */
public class AssIO {
    
    /** <p>Create a new AssIO.<br />
     * Crée un nouveau AssIO.</p> */
    public AssIO(){
        //Nothing
    }
    
    // <editor-fold defaultstate="collapsed" desc=" Ruby Editor ">
    /** <p>Open ruby file and return the contents.<br />
     * Ouvre un fichier ruby et retourne son contenu.</p> */
    public String openRubyFile(String path){
        String rubyCode = "";
        File file = new File(path);
        try{
            FileInputStream fis = new FileInputStream(file);
            java.io.BufferedReader br = new java.io.BufferedReader(
                    new java.io.InputStreamReader(fis, "UTF-8"));
            String newline;
            while((newline=br.readLine())!=null){
                rubyCode += newline+"\n";
            }
            br.close(); fis.close();
            return rubyCode;
        }catch (Exception exc){
            return "";
        }        
    }

    /** <p>Save a ruby file with the given contents (text).<br />
     * Sauvegarde un fichier ruby avec le contenu donné (text).</p> */
    public void saveRubyFile(String path, String text){
        File file = new File(path);
        try{
            text = text + "\n"; //Error treatment (see pattern)
            FileOutputStream fos = new FileOutputStream(file);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
            PrintWriter pw = new PrintWriter(bw);
            Pattern p = Pattern.compile("([^\n]*)\n");
            Matcher m = p.matcher(text);
            while(m.find()){
                pw.print(m.group());
                pw.flush();
            }
            pw.close(); bw.close(); fos.close();
        }catch(Exception exc){
            
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" Drawing Editor ">
    /** <p>Open a drawing file and return the drawing commands.<br />
     * Ouvre un fichier dessin et retourne les commandes de dessin.</p> */
    public String openDrawingFile(String path){
        String commands;
        File file = new File(path);
        try{
            FileInputStream fis = new FileInputStream(file);
            java.io.BufferedReader br = new java.io.BufferedReader(
                    new java.io.InputStreamReader(fis, "UTF-8"));
            commands = br.readLine();
            br.close(); fis.close();
            return commands;
        }catch (Exception exc){
            return "";
        }
    }

    /** <p>Save a drawing file with the given commands.<br />
     * Sauvegarde un fichier dessin avec les commandes données.</p> */
    public void saveDrawingFile(String path, String commands){
        File file = new File(path);
        try{
            FileOutputStream fos = new FileOutputStream(file);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
            PrintWriter pw = new PrintWriter(bw);
            pw.print(commands);
            pw.flush();
            pw.close(); bw.close(); fos.close();
        }catch(Exception exc){

        }
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" Configuration ">
    /** <p>Read a file and return the configuration.<br />
     * Lit un fichier et retourne la configuration.</p> */
    public Configuration ReadConfig(){
        File file = new File(
                getAssFxMakerDirectory()+
                File.separator+"Configuration.ini");
        Configuration cfg = new Configuration();
        try{
            FileInputStream fis = new FileInputStream(file);
            java.io.BufferedReader br = new java.io.BufferedReader(
                    new java.io.InputStreamReader(fis, "UTF-8"));
            String newline;

            //Reading of file
            while((newline=br.readLine())!=null){
                if(newline.startsWith(Configuration.Type.DOCS_PATH.getString())){
                    String s = newline.substring(Configuration.Type.DOCS_PATH.getString().length()+1);
                    cfg.put(Configuration.Type.DOCS_PATH, s);//Documents as help
                }
                if(newline.startsWith(Configuration.Type.CODE_EDITOR.getString())){
                    String s = newline.substring(Configuration.Type.CODE_EDITOR.getString().length()+1);
                    cfg.put(Configuration.Type.CODE_EDITOR, s);//rubyEditor
                }
                if(newline.startsWith(Configuration.Type.FONT.getString())){
                    String s = newline.substring(Configuration.Type.FONT.getString().length()+1);
                    cfg.put(Configuration.Type.FONT, s);//font of Org/ResTable
                }
                if(newline.startsWith(Configuration.Type.BACKGD_IMAGE.getString())){
                    String s = newline.substring(Configuration.Type.BACKGD_IMAGE.getString().length()+1);
                    cfg.put(Configuration.Type.BACKGD_IMAGE, s);//font of Org/ResTable
                }
                if(newline.startsWith(Configuration.Type.THEME.getString())){
                    String s = newline.substring(Configuration.Type.THEME.getString().length()+1);
                    cfg.put(Configuration.Type.THEME, s);//main theme
                }
                if(newline.startsWith(Configuration.Type.ORG_TABLE_DISPLAY.getString())){
                    String s = newline.substring(Configuration.Type.ORG_TABLE_DISPLAY.getString().length()+1);
                    cfg.put(Configuration.Type.ORG_TABLE_DISPLAY, s);//display of tables
                }
                if(newline.startsWith(Configuration.Type.RES_TABLE_DISPLAY.getString())){
                    String s = newline.substring(Configuration.Type.RES_TABLE_DISPLAY.getString().length()+1);
                    cfg.put(Configuration.Type.RES_TABLE_DISPLAY, s);//display of tables
                }
                if(newline.startsWith(Configuration.Type.CHK_UPDATE.getString())){
                    String s = newline.substring(Configuration.Type.CHK_UPDATE.getString().length()+1);
                    cfg.put(Configuration.Type.CHK_UPDATE, s);//check for update
                }
                if(newline.startsWith(Configuration.Type.FORCE_ISO.getString())){
                    String s = newline.substring(Configuration.Type.FORCE_ISO.getString().length()+1);
                    cfg.put(Configuration.Type.FORCE_ISO, s);//force iso language
                }
                if(newline.startsWith(Configuration.Type.KARA_MODULE.getString())){
                    String s = newline.substring(Configuration.Type.KARA_MODULE.getString().length()+1);
                    cfg.put(Configuration.Type.KARA_MODULE, s);//force iso language
                }
                if(newline.startsWith(Configuration.Type.CODE_MODULE.getString())){
                    String s = newline.substring(Configuration.Type.CODE_MODULE.getString().length()+1);
                    cfg.put(Configuration.Type.CODE_MODULE, s);//force iso language
                }
                if(newline.startsWith(Configuration.Type.DRAW_MODULE.getString())){
                    String s = newline.substring(Configuration.Type.DRAW_MODULE.getString().length()+1);
                    cfg.put(Configuration.Type.DRAW_MODULE, s);//force iso language
                }
                if(newline.startsWith(Configuration.Type.ANAL_MODULE.getString())){
                    String s = newline.substring(Configuration.Type.ANAL_MODULE.getString().length()+1);
                    cfg.put(Configuration.Type.ANAL_MODULE, s);//force iso language
                }
                if(newline.startsWith(Configuration.Type.STARTWITH.getString())){
                    String s = newline.substring(Configuration.Type.STARTWITH.getString().length()+1);
                    cfg.put(Configuration.Type.STARTWITH, s);//force iso language
                }
            }
            br.close(); fis.close();
            return cfg;
        }catch(Exception exc){
            return null;
        }
    }

    /** <p>Save a file with the given configuration.<br />
     * Sauvegarde un fichier avec la configuration donnée.</p> */
    public void SaveConfig(Configuration cfg){
        File file = new File(
                getAssFxMakerDirectory()+
                File.separator+"Configuration.ini");
        try{
            FileOutputStream fos = new FileOutputStream(file);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
            PrintWriter pw = new PrintWriter(bw);
            pw.println(Configuration.Type.DOCS_PATH.getString()+" "+cfg.get(Configuration.Type.DOCS_PATH));
            pw.println(Configuration.Type.CODE_EDITOR.getString()+" "+cfg.get(Configuration.Type.CODE_EDITOR));
            pw.println(Configuration.Type.FONT.getString()+" "+cfg.get(Configuration.Type.FONT));
            pw.println(Configuration.Type.BACKGD_IMAGE.getString()+" "+cfg.get(Configuration.Type.BACKGD_IMAGE));
            pw.println(Configuration.Type.THEME.getString()+" "+cfg.get(Configuration.Type.THEME));
            pw.println(Configuration.Type.ORG_TABLE_DISPLAY.getString()+" "+cfg.get(Configuration.Type.ORG_TABLE_DISPLAY));
            pw.println(Configuration.Type.RES_TABLE_DISPLAY.getString()+" "+cfg.get(Configuration.Type.RES_TABLE_DISPLAY));
            pw.println(Configuration.Type.CHK_UPDATE.getString()+" "+cfg.get(Configuration.Type.CHK_UPDATE));
            pw.println(Configuration.Type.FORCE_ISO.getString()+" "+cfg.get(Configuration.Type.FORCE_ISO));
            pw.println(Configuration.Type.KARA_MODULE.getString()+" "+cfg.get(Configuration.Type.KARA_MODULE));
            pw.println(Configuration.Type.CODE_MODULE.getString()+" "+cfg.get(Configuration.Type.CODE_MODULE));
            pw.println(Configuration.Type.DRAW_MODULE.getString()+" "+cfg.get(Configuration.Type.DRAW_MODULE));
            pw.println(Configuration.Type.ANAL_MODULE.getString()+" "+cfg.get(Configuration.Type.ANAL_MODULE));
            pw.println(Configuration.Type.STARTWITH.getString()+" "+cfg.get(Configuration.Type.STARTWITH));
            pw.println("");
            pw.flush();
            pw.close(); bw.close(); fos.close();
        }catch(Exception exc){

        }
    }
    
    /** <p>Check if the configuration file exist.<br />
     * Vérifie si un fichier de configuration existe.</p> */
    public boolean HasConfigFile(){
        File file = new File(
                getAssFxMakerDirectory()+
                File.separator+"Configuration.ini");
        return file.exists();
    }

    /** <p>Create a configuration file and set up all paths if this file doesn't exist.<br />
     * Crée un fichier de configuration et définit tous les chemins si le fichier n'existe pas.</p> */
    public void createConfigFile(){
        String sep = File.separator;
        String dir = getAssFxMakerDirectory()+File.separator;
        File file = new File(dir+"Configuration.ini");
        
        try{
            FileOutputStream fos = new FileOutputStream(file);
            BufferedWriter bw = new BufferedWriter(
                    new OutputStreamWriter(fos, "UTF-8"));
            PrintWriter pw = new PrintWriter(bw);
            //AssFxMaker uses :
//            pw.println("FX_PATH: "+dir+"Effects"+sep);
//            pw.println("XFX_PATH: "+dir+"Effects"+sep+"afm-effects.xml");
//            pw.println("FXPLUG_PATH: "+dir+"Effects"+sep+"Plugins"+sep);
//            pw.println("DOCS_PATH: "+dir+"Docs"+sep);
//            pw.println("RUBY_EDITOR: ");
//            pw.println("DRAW_EDITOR: ");
//            pw.println("FONT: ");
//            pw.println("THEME: ");
//            pw.println("TABLE_DISPLAY: ");
//            pw.println("CHK_UPDATE: ");
//            pw.println("FORCE_ISO: ");
            
            //Now Feuille uses :
            pw.println("DOCS_PATH: "+dir+"docs"+sep);
            pw.println("CODE_EDITOR: ");
            pw.println("FONT: "+dir+"docs"+sep+"FreeSans.ttf");
            pw.println("BACKGD_IMAGE: ");
            pw.println("THEME: ");
            pw.println("ORG_TABLE_DISPLAY: ");
            pw.println("RES_TABLE_DISPLAY: ");
            pw.println("FORCE_ISO: ---");
            pw.println("KARA_MODULE: yes");
            pw.println("CODE_MODULE: yes");
            pw.println("DRAW_MODULE: yes");
            pw.println("ANAL_MODULE: yes");
            pw.println("STARTWITH: welc");
            
            pw.println("");
            pw.flush();
            pw.close(); bw.close(); fos.close();
        }catch(Exception exc){

        }

        //If we are on Linux then create two shortcuts :
        if(System.getProperty("os.name").equalsIgnoreCase("Linux")){
            createLinuxScripts();
        }
    }

    /** <p>Find the path where AssFxMaker is executed.<br />
     * Trouve le chemin d'où AssFxMaker est executé.</p> */
    private String getAssFxMakerDirectory(){
        if(System.getProperty("os.name").equalsIgnoreCase("Mac OS X")){
            java.io.File file = new java.io.File("");
            return file.getAbsolutePath();
        }
        String path = System.getProperty("user.dir");
        //System.out.println("user dir is : "+path);
        if(path.toLowerCase().contains("jre")){
            File f = new File(getClass().getProtectionDomain()
                    .getCodeSource().getLocation().toString()
                    .substring(6));
            path = f.getParent();
            //System.out.println("by class is : "+path);
        }
        //TODO Check if the path is valid otherwise open a dialog box
//        java.io.File fa = new java.io.File(path+File.separator+"AssFxMaker.jar");
//        if(fa.exists()==false){
//            javax.swing.JFileChooser fc = new javax.swing.JFileChooser();
//            // Clear the list of file filters.
//            for (javax.swing.filechooser.FileFilter f : fc.getChoosableFileFilters()){
//                fc.removeChoosableFileFilter(f);
//            }
//            fc.addChoosableFileFilter(new assfxmaker.lib.filter.JarFilter());
//            fc.setDialogTitle("Please define the path of AssFxMaker.jar");
//            int z = fc.showOpenDialog(null);
//            fc.setLocation(0, 0);
//            if (z == javax.swing.JFileChooser.APPROVE_OPTION){
//                path = fc.getSelectedFile().getParent();
//            }
//            System.out.println("Par ce point on pass !!!");
//        }
        return path;
    }
    
    /** <p>Create two files for an easy launch of AssFxMaker and ZDrawingLite.<br />
     * Crée deux fichiers pour un lancement facile de AssFxMaker et ZDrawingLite.</p> */
    public void createLinuxScripts(){
        //AssFxMaker
        File file = new File(
                getAssFxMakerDirectory()+
                File.separator+"Feuille.sh");
        try{
            FileOutputStream fos = new FileOutputStream(file);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
            PrintWriter pw = new PrintWriter(bw);
            pw.println("java -jar \""+getAssFxMakerDirectory()+File.separator+"SmallBoxForFansub.jar\"");
            pw.flush();
            pw.close(); bw.close(); fos.close();
            
            file.setExecutable(true);
        }catch(Exception exc){
            
        }

        //ZDrawingLite (now AssSketchpad)
//        file = new File(
//                getAssFxMakerDirectory()+
//                File.separator+"AssSketchpad.sh");
//        try{
//            FileOutputStream fos = new FileOutputStream(file);
//            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
//            PrintWriter pw = new PrintWriter(bw);
//            pw.println("java -jar \""+getAssFxMakerDirectory()+File.separator+
//                    "lib"+File.separator+"AssSketchpad.jar\"");
//            pw.flush();
//            pw.close(); bw.close(); fos.close();
//            
//            file.setExecutable(true);
//        }catch(Exception exc){
//            
//        }
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" EntreeSortie Fichier SSA ">
    
    /* Ces méthodes permettent de créer et d'écrire dans un fichier de
     * sous-titres Sub Station Alpha (*.ssa). On écrit les éléments de:
     * ssaTime > Time
     * ssaKara > Karaoke
     * On se doit de faire des fichiers compatibles à Sub Station Alpha
     * n'utilisant pas Unicode ainsi que des fichiers non standard en Unicode
     * pour les utiliser dans les logiciels d'encodage afin
     * de supporter le japonais, le chinois, le coréen, le russe,...
     */
    
    /** <p>Read a SSA file and put it in a JTable with 9 columns.<br />
     * Lit des fichiers Sub Station Alpha et de les mettre dans un JTable de 9 colonnes.</p>
     * @param String <b color="blue">pathFile</b> - Contient le nom de fichier.
     * @param DefaultTableModel <b color="blue">t</b> - R&#233;f&#232;re au model du JTable employ&#233;. */
    public void LireFichierSSAi2(String pathFile, DefaultTableModel t,
            AssInfos ai, AssStyleCollection asc, AssNameCollection anc,
            boolean addLines){
        
        //On cr?e une cha?ne ? retourner
        String newline;
        ProgramLine pl;
        
        try{
            
            // Encoding detection
            FileReader fr = new FileReader(pathFile);
            String charset = detectCharset(fr);
            fr.close();

            // Start process to read file...
            FileInputStream fis = new FileInputStream(pathFile);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(fis, charset));
        
        //On lit le fichier
        while((newline=br.readLine())!=null){
            
            try{
                
                /* Traitement de l'ent�te */
                
                if(newline.startsWith("Title") && !newline.substring(7).isEmpty()){
                    ai.setElement(AssInfosType.title, newline.substring(7));
                }
                
                if(newline.startsWith("Original Script") && !newline.substring(17).isEmpty()){
                    ai.setElement(AssInfosType.authors, newline.substring(17));
                }
                
                if(newline.startsWith("Original Translation") && !newline.substring(22).isEmpty()){
                    ai.setElement(AssInfosType.translators, newline.substring(22));
                }
                
                if(newline.startsWith("Original Editing") && !newline.substring(18).isEmpty()){
                    ai.setElement(AssInfosType.editors, newline.substring(18));
                }
                
                if(newline.startsWith("Original Timing") && !newline.substring(17).isEmpty()){
                    ai.setElement(AssInfosType.timers, newline.substring(17));
                }
                
                if(newline.startsWith("Original Script Checking") && !newline.substring(26).isEmpty()){
                    ai.setElement(AssInfosType.checkers, newline.substring(26));
                }
                
                if(newline.startsWith("Synch Point") && !newline.substring(13).isEmpty()){
                    ai.setElement(AssInfosType.synchpoint, newline.substring(13));
                }
                
                if(newline.startsWith("Script Updated By") && !newline.substring(19).isEmpty()){
                    ai.setElement(AssInfosType.updateby, newline.substring(19));
                }
                
                if(newline.startsWith("Update Details") && !newline.substring(16).isEmpty()){
                    ai.setElement(AssInfosType.updates, newline.substring(16));
                }
                
                if(newline.startsWith("ScriptType") && !newline.substring(12).isEmpty()){
                    ai.setElement(AssInfosType.scripttype, newline.substring(12));
                }
                
                if(newline.startsWith("Collisions") && !newline.substring(12).isEmpty()){
                    ai.setElement(AssInfosType.collisions, newline.substring(12));
                }
                
                if(newline.startsWith("PlayResX") && !newline.substring(10).isEmpty()){
                    ai.setElement(AssInfosType.playresx, newline.substring(10));
                }
                
                if(newline.startsWith("PlayResY") && !newline.substring(10).isEmpty()){
                    ai.setElement(AssInfosType.playresy, newline.substring(10));
                }
                
                if(newline.startsWith("PlayDepth") && !newline.substring(11).isEmpty()){
                    ai.setElement(AssInfosType.playdepth, newline.substring(11));
                }
                
                if(newline.startsWith("Timer") && !newline.substring(7).isEmpty()){
                    ai.setElement(AssInfosType.timerspeed, newline.substring(7));
                }
                
                if(newline.startsWith("Audio") && !newline.substring(7).isEmpty()){
                    ai.setElement(AssInfosType.audios, newline.substring(7));
                }
                
                if(newline.startsWith("Video") && !newline.substring(7).isEmpty()){
                    ai.setElement(AssInfosType.video, newline.substring(7));
                }
                
                /* Traitement des styles */
                
                if(newline.startsWith("Style:")){
                    String[] listStyle = newline.substring(7).split(",");
                    AssStyle as = new AssStyle();
                    as.setElement(AssStyleType.name, listStyle[0]);
                    as.setElement(AssStyleType.fontname, listStyle[1]);
                    as.setElement(AssStyleType.fontsize, listStyle[2]);
                    as.setElement(AssStyleType.primarycolour, listStyle[3],"");
                    as.setElement(AssStyleType.secondarycolour, listStyle[4],"");
                    as.setElement(AssStyleType.outlinecolour, listStyle[5],"");
                    as.setElement(AssStyleType.backcolour, listStyle[6],"");
                    as.setElement(AssStyleType.bold, listStyle[7]);
                    as.setElement(AssStyleType.italic, listStyle[8]);
                    as.setElement(AssStyleType.borderstyle, listStyle[9]);                    
                    as.setElement(AssStyleType.outline, listStyle[10]);
                    as.setElement(AssStyleType.shadow, listStyle[11]);
                    as.setElement(AssStyleType.alignment, listStyle[12],"");
                    as.setElement(AssStyleType.marginL, listStyle[13]);
                    as.setElement(AssStyleType.marginR, listStyle[14]);
                    as.setElement(AssStyleType.marginV, listStyle[15]);
                    as.setElement(AssStyleType.alphalevel, listStyle[16]);
                    as.setElement(AssStyleType.encoding, listStyle[17]);
                    asc.addMember(listStyle[0], as);
                }
                
                /* Traitement des �venements */
                
                pl = Format(newline,ModeFormat.SSAToProgram);

                if(pl.getLineType().getLabel()
                        .equals(ProgramLine.LineType.Nothing.getLabel())==false){
                    t.addRow(pl.toRow());
                }
                
            }catch(IndexOutOfBoundsException ioobe){
                //erreurs = ioobe.getMessage();
            }
            
        }
                
        //On ferme le flux puis le fichier
        br.close();
        fis.close();
       
        } catch (java.io.FileNotFoundException e2){
            System.out.println("Fichier non trouv? - Missing File Error");
        } catch (SecurityException se){
            System.out.println("Acc?s refus? - Access denied");
        } catch (java.io.UnsupportedEncodingException uee){
            System.out.println("Encodage non support? - Unsupported encoding");
        } catch (java.io.IOException e1){
            System.out.println("Erreur lors de la lecture - Read Error");
        }
        
    }

    /** <p>Extract styles from SSA files.<br />
     * Extrait les styles d'un fichier SSA.</p> */
    public java.util.List<AssStyle> ExtractSSAStyles(String pathFile){

        //On cr?e une cha?ne ? retourner
        String newline;
        java.util.List<AssStyle> las = new java.util.ArrayList<AssStyle>();

        try{

            // Encoding detection
            FileReader fr = new FileReader(pathFile);
            String charset = detectCharset(fr);
            fr.close();

            // Start process to read file...
            FileInputStream fis = new FileInputStream(pathFile);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(fis, charset));

        //On lit le fichier
        while((newline=br.readLine())!=null){
            try{
                if(newline.startsWith("Style:")){
                    String[] listStyle = newline.substring(7).split(",");
                    AssStyle as = new AssStyle();
                    as.setElement(AssStyleType.name, listStyle[0]);
                    as.setElement(AssStyleType.fontname, listStyle[1]);
                    as.setElement(AssStyleType.fontsize, listStyle[2]);
                    as.setElement(AssStyleType.primarycolour, listStyle[3],"");
                    as.setElement(AssStyleType.secondarycolour, listStyle[4],"");
                    as.setElement(AssStyleType.outlinecolour, listStyle[5],"");
                    as.setElement(AssStyleType.backcolour, listStyle[6],"");
                    as.setElement(AssStyleType.bold, listStyle[7]);
                    as.setElement(AssStyleType.italic, listStyle[8]);
                    as.setElement(AssStyleType.borderstyle, listStyle[9]);
                    as.setElement(AssStyleType.outline, listStyle[10]);
                    as.setElement(AssStyleType.shadow, listStyle[11]);
                    as.setElement(AssStyleType.alignment, listStyle[12],"");
                    as.setElement(AssStyleType.marginL, listStyle[13]);
                    as.setElement(AssStyleType.marginR, listStyle[14]);
                    as.setElement(AssStyleType.marginV, listStyle[15]);
                    as.setElement(AssStyleType.alphalevel, listStyle[16]);
                    as.setElement(AssStyleType.encoding, listStyle[17]);
                    las.add(as);
                }
            }catch(IndexOutOfBoundsException ioobe){
                //erreurs = ioobe.getMessage();
            }
        }

        //On ferme le flux puis le fichier
        br.close();
        fis.close();

        } catch (java.io.FileNotFoundException e2){
            System.out.println("Fichier non trouv? - Missing File Error");
        } catch (SecurityException se){
            System.out.println("Acc?s refus? - Access denied");
        } catch (java.io.UnsupportedEncodingException uee){
            System.out.println("Encodage non support? - Unsupported encoding");
        } catch (java.io.IOException e1){
            System.out.println("Erreur lors de la lecture - Read Error");
        }

        return las;
    }
    
    /** <p>Read a SSA file and put it in a JTable with 9 columns.<br />
     * Lit des fichiers Sub Station Alpha et de les mettre dans un JTable de 9 colonnes.</p>
     * @param String <b color="blue">pathFile</b> - Contient le nom de fichier.
     * @param DefaultTableModel <b color="blue">t</b> - R&#233;f&#232;re au model du JTable employ&#233;. */
    public void LireFichierSSAi2_Minimal(String pathFile, List<ProgramLine> sublist){
        
        //On cr?e une cha?ne ? retourner
        String newline;
        //ProgramLine pl = new ProgramLine();
        
        try{
            
            // Encoding detection
            FileReader fr = new FileReader(pathFile);
            String charset = detectCharset(fr);
            fr.close();

            // Start process to read file...
            FileInputStream fis = new FileInputStream(pathFile);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(fis, charset));
        
        //On lit le fichier
        while((newline=br.readLine())!=null){
            
            try{
                
                /* Traitement des �venements */
                
                if(newline.startsWith("Dialogue:") | 
                        newline.startsWith("Comment:") |
                        newline.startsWith("Picture:") |
                        newline.startsWith("Sound:") |
                        newline.startsWith("Movie:") |
                        newline.startsWith("Command:")){
                    ProgramLine sub = Format(newline, ModeFormat.SSAToProgram);
                    sublist.add(sub);
                }
                
            }catch(IndexOutOfBoundsException ioobe){
                //erreurs = ioobe.getMessage();
            }
            
        }
                
        //On ferme le flux puis le fichier
        br.close();
        fis.close();
       
        } catch (java.io.FileNotFoundException e2){
            System.out.println("Fichier non trouv? - Missing File Error");
        } catch (SecurityException se){
            System.out.println("Acc?s refus? - Access denied");
        } catch (java.io.UnsupportedEncodingException uee){
            System.out.println("Encodage non support? - Unsupported encoding");
        } catch (java.io.IOException e1){
            System.out.println("Erreur lors de la lecture - Read Error");
        }
        
    }
    
    // </editor-fold>
 
    // <editor-fold defaultstate="collapsed" desc=" EntreeSortie Fichier ASS ">
    
     /* Ces méthodes permettent de créer et d'écrire dans un fichier de
     * sous-titres Advanced Sub Station (*.ass). On écrit les éléments de:
     * ssaTime > Time
     * ssaKara > Karaoke
     * On se doit de faire des fichiers compatibles à Sub Station Alpha
     * n'utilisant pas Unicode ainsi que des fichiers non standard en Unicode
     * pour les utiliser dans les logiciels d'encodage afin
     * de supporter le japonais, le chinois, le coréen, le russe,...
     */
    
    /** <p>Write some informations using a stream.<br />
     * Ecrit quelques infornations en utilisant un flux.</p> */
    private boolean WriteASSInfos(PrintWriter p, String thing, String info){
        if (thing.length()>0){
            p.println(info+": "+thing);
            return true;
        }
        return false;
    }
    
    /** <p>A choice of functioning and formatting.<br />
     * Un choix de fonctionnement et de formatage.<br /><br />
     * <table><tr><td>ProgramToASS</td>
     * <td>Software to ASS. A line readable by the program is reformated in an ASS line.<br />
     * Logiciel à ASS. Une ligne lisible par le programme est reformatée en une ligne ASS.</td></tr>
     * <tr><td>ASSToProgram</td>
     * <td>ASS to Software. An ASS line is formated in a line readable by the program.<br />
     * ASS à Logiciel. Une ligne ASS est formatée en une ligne lisible par le programme.</td></tr>
     * <tr><td>SSAToProgram</td>
     * <td>SSA to Software. A SSA line is formated in a line readable by the program.<br />
     * SSA à Logiciel. Une ligne SSA est formatée en une ligne lisible par le programme.</td></tr></table>
     * </p> */
    public enum ModeFormat{
        ProgramToASS, ASSToProgram, SSAToProgram;
    }
    
    /** <p>Return a line readable by the program.<br />
     * Retourne une ligne lisible par le programme.</p>
     * @param o An object which may be an ASS line or a SSA line or a line readable by the program.
     * @param mf A choice of functioning and formatting. */
    public static ProgramLine Format(Object o, ModeFormat mf){
        
        // Format d'une ligne SSA :
        // Dialogue: Marked=0,0:00:16.84,0:00:19.24,Default,,0000,0000,0000,,
        // Mikoto ne s'est toujours pas réveillée?
        // regex = ([a-zA-Z]+):\sMarked=0,(\d+):(\d+):(\d+).(\d+),
        // (\d+):(\d+):(\d+).(\d+),(\w+),(\w+),(\d+),(\d+),(\d+),
        // (\w+),(.*)
        
        // Format d'une ligne ASS :
        // Dialogue: 0,0:01:40.79,0:01:42.19,GakuenManabi,,0000,0000,0000,,
        // Nous sommes en retard !
        
        // Format du programme :
        // "Type", "Layer", "Margins", "Start", "End", "Total time",
        // "Style", "Name", "Text"
        // avec pour les marges LVR au lieu de LRV.
        ProgramLine pl = new ProgramLine();
        
        switch(mf){
            case SSAToProgram:
                try{
                    Pattern p = Pattern.compile("([^:]+):\\s[a-zA-Z=]*(\\d+)," +
                            "(\\d+):(\\d+):(\\d+).(\\d+)," +
                            "(\\d+):(\\d+):(\\d+).(\\d+)," +
                            "([^,]+),([^,]*)," +
                            "(\\d+),(\\d+),(\\d+),([^,]*),(.*)");
                    Matcher m = p.matcher((String)o.toString());
                    m.find();
                    
                    // The object
                    // System.out.println(m.toString());
                    
                    // The entire line
                    // System.out.println(m.group());
                    
                    // Type                    
                    // System.out.println(m.group(1));
                    pl.setLineType(m.group());
                    // System.out.println(pl.getLineType());
                    
                    // Layer
                    // System.out.println(m.group(2));
                    pl.setLayer(m.group(2));
                    
                    // Start
                    // System.out.println(m.group(3));
                    // System.out.println(m.group(4));
                    // System.out.println(m.group(5));
                    // System.out.println(m.group(6));
                    pl.setStart(m.group(3), m.group(4), m.group(5), m.group(6));
                    
                    // End
                    // System.out.println(m.group(7));
                    // System.out.println(m.group(8));
                    // System.out.println(m.group(9));
                    // System.out.println(m.group(10));
                    pl.setEnd(m.group(7), m.group(8), m.group(9), m.group(10));
                    
                    // Set up the total time
                    pl.setTotaltime(pl.getStart(), pl.getEnd());
                    
                    // Style
                    // System.out.println(m.group(11));
                    pl.setStyle(m.group(11));
                    
                    // Name
                    // System.out.println(m.group(12));
                    pl.setName(m.group(12));
                    
                    // Margin L, R, V
                    // System.out.println(m.group(13));
                    // System.out.println(m.group(14));
                    // System.out.println(m.group(15));
                    pl.setMargins(m.group(13), m.group(14), m.group(15));
                    
                    // Effects
                    // System.out.println(m.group(16));
                    pl.setEffect(m.group(16));
                    
                    // Text
                    // System.out.println(m.group(17));
                    pl.setText(m.group(17));
                    
                }catch(Exception exc){
                    //System.out.println(exc.getMessage());
                }
                break;
            case ASSToProgram:
                try{
                    Pattern p = Pattern.compile("([^:]+):\\s[a-zA-Z=]*(\\d+)," +
                            "(\\d+):(\\d+):(\\d+).(\\d+)," +
                            "(\\d+):(\\d+):(\\d+).(\\d+)," +
                            "([^,]+),([^,]*)," +
                            "(\\d+),(\\d+),(\\d+),([^,]*),(.*)");
                    Matcher m = p.matcher((String)o.toString());
                    m.find();
                    
                    pl.setLineType(m.group());// Type
                    pl.setLayer(m.group(2));// Layer
                    pl.setStart(m.group(3), m.group(4), m.group(5), m.group(6));// Start
                    pl.setEnd(m.group(7), m.group(8), m.group(9), m.group(10));// End
                    pl.setTotaltime(pl.getStart(), pl.getEnd());// Set up the total time
                    pl.setStyle(m.group(11));// Style
                    pl.setName(m.group(12));// Name
                    pl.setMargins(m.group(13), m.group(14), m.group(15));// Margin L, R, V
                    pl.setEffect(m.group(16));// Effects
                    pl.setText(m.group(17));// Text
                }catch(Exception exc){
                    //System.out.println(exc.getMessage());
                }
                break;
            case ProgramToASS:
                // Format du programme :
                // ID(0), Type(1), Layer(2), Margins(3),
                // Start(4), End(5), Total time(6), Style(7),
                // Name(8), Effects(9), Text(10), FX(11) 
                try{
                    String[] s = (String[])o;
                    pl.setLineType2(s[1]);// Type
                    pl.setLayer(s[2]);// Layer
                    Pattern p = Pattern.compile("(\\d+).(\\d+).(\\d+).(\\d+)");
                    Matcher m = p.matcher((String)s[4].toString()); m.find();
                    pl.setStart(m.group(1), m.group(2), m.group(3), m.group(4));// Start
                    m = p.matcher((String)s[5].toString()); m.find();
                    pl.setEnd(m.group(1), m.group(2), m.group(3), m.group(4));// End
                    pl.setTotaltime(pl.getStart(), pl.getEnd());// Set up the total time
                    pl.setStyle(s[7]);// Style
                    pl.setName(s[8]);// Name
                    p = Pattern.compile("([^,]+),([^,]+),(.*)");
                    m = p.matcher((String)s[3].toString()); m.find();
                    pl.setMargins(m.group(1), m.group(2), m.group(3));// Margin L, R, V
                    pl.setEffect(s[9]);// Effects
                    pl.setText(s[10]);// Text
                }catch(Exception exc){
                    //System.out.println(exc.getMessage());
                }
                break;
        }
        
        return pl;
    }
    
    /** <p>Write an ASS file.<br />
     * Ecrit un fichier ASS.</p> */
    public void EcrireFichierASS2(String pathFile, DefaultTableModel t,
            AssInfos ai, AssStyleCollection asc, String asUnicode){
        //On cr�e des nouveaux String pour g�rer le contenu
        String compatible = "Undefined"; String aEcrire;
        if(pathFile.endsWith(".ass")==false){
            pathFile = pathFile + ".ass"; //Setting up the extension
        }        
        
        try{
        //On cr�e un objet qui peut ouvrir/cr�er le fichier
        FileWriter fw = new FileWriter(pathFile); //ANSI, Unicode, ASCII
        FileOutputStream fos = null; //Tous sauf le d�faut: Unicode et ASCII
        
        //On cr�e un objet pour �crire dans le fichier
        PrintWriter pw; //Tous les encodages: ANSI, Unicode, ASCII
        BufferedWriter bw = null; //Tous sauf le d�faut: Unicode et ASCII
        
        //On ouvre le fichier soit en ANSI, soit en Unicode ou ASCII
        if (asUnicode.compareTo("")==0){
            //On ouvre/cr�e le fichier en ANSI
            fw = new FileWriter(pathFile);
            //On cr�e un flux pour l'�criture des caract�res
            pw = new PrintWriter(fw);
            compatible="Default text encoding";
        }else{
            //On ouvre/cr�e le fichier en Unicode
            fos = new FileOutputStream(pathFile);
            //On cr�e un flux pour l'�criture des caract�res
            bw = new BufferedWriter(new OutputStreamWriter(fos, asUnicode));
            pw = new PrintWriter(bw);
            if (asUnicode.compareTo("ISO-8859-1")==0){
                compatible="Encoding: ISO-8859-1 or Latin";}
            if (asUnicode.compareTo("US-ASCII")==0){
                compatible="Encoding: ASCII or Basic latin";}
            if (asUnicode.compareTo("UTF-8")==0){
                compatible="Encoding: UTF-8";}
            if (asUnicode.compareTo("UTF-16")==0){
                compatible="Encoding: UTF-16";}
            if (asUnicode.compareTo("UTF-16BE")==0){
                compatible="Encoding: UTF-16 big endian";}
            if (asUnicode.compareTo("UTF-16LE")==0){
                compatible="Encoding: UTF-16 little endian";}
            //compatible="Unicode";
        }
        
        
        //On �crit dans le fichier
        //HEAD
        pw.println("[Script Info]");
        pw.println("; This is an Advanced Sub Station Alpha v4+ script."
                + " For Sub Station Alpha info and downloads,"
                + " go to http://www.eswat.demon.co.uk/");
        pw.println("; Created by AssFxMaker(Funsub project)."
                + " \"Fansub is for everyone and forever !\" -"
                + " Chien-Rouge@http://redaire.wordpress.com/"); //Created with Funsub //Make easy Fansub for fun
        pw.println("; "+compatible); //Developed under NetBeans4.1 - compatible JSDK1.5.0
        WriteASSInfos(pw,ai.getElement(AssInfosType.title),"Title");
        WriteASSInfos(pw,ai.getElement(AssInfosType.authors),"Original Script");
        WriteASSInfos(pw,ai.getElement(AssInfosType.translators),"Original Translation");
        WriteASSInfos(pw,ai.getElement(AssInfosType.editors),"Original Editing");
        WriteASSInfos(pw,ai.getElement(AssInfosType.timers),"Original Timing");
        WriteASSInfos(pw,ai.getElement(AssInfosType.checkers),"Original Script Checking");
        WriteASSInfos(pw,ai.getElement(AssInfosType.synchpoint),"Synch Point");
        WriteASSInfos(pw,ai.getElement(AssInfosType.updateby),"Script Updated By");
        WriteASSInfos(pw,ai.getElement(AssInfosType.updates),"Update Details");
        pw.println("ScriptType: v4.00+");
        WriteASSInfos(pw,ai.getElement(AssInfosType.collisions),"Collisions");
        WriteASSInfos(pw,ai.getElement(AssInfosType.playresx),"PlayResX");
        WriteASSInfos(pw,ai.getElement(AssInfosType.playresy),"PlayResY");
        WriteASSInfos(pw,ai.getElement(AssInfosType.playdepth),"PlayDepth");
        WriteASSInfos(pw,ai.getElement(AssInfosType.timerspeed),"Timer");
        WriteASSInfos(pw,ai.getElement(AssInfosType.wrapstyle),"WrapStyle");
//        WriteASSInfos(pw,ai.getElement(AssInfosType.title),"Title");
//        WriteASSInfos(pw,ai.getElement(AssInfosType.title),"Title");
//        WriteASSInfos(pw,ai.getElement(AssInfosType.title),"Title");
//        if (rens[15]!=null && rens[15]!=""){pw.println("Wav: 0, 0,"+rens[15]);}
//        if (rens[15]!=null && rens[15]!=""){pw.println("LastWav: 1");}
//        if (rens[16]!=null && rens[16]!=""){pw.println("Video: "+rens[16]);}
//        if (rens[18]!=null && rens[18]!=""){pw.println("EpNumber: "+rens[18]);}
//        if (rens[19]!=null && rens[19]!=""){pw.println("EpTitle: "+rens[19]);}
//        if (rens[20]!=null && rens[20]!=""){pw.println("EpDesc: "+rens[20]);}
//        if (rens[21]!=null && rens[21]!=""){pw.println("EpGenre: "+rens[21]);}
//        if (rens[22]!=null && rens[22]!=""){pw.println("EpEditor: "+rens[22]);}
//        if (rens[23]!=null && rens[23]!=""){pw.println("EpLicense: "+rens[23]);}
//        if (rens[24]!=null && rens[24]!=""){pw.println("EpLicZone: "+rens[24]);}
//        if (rens[25]!=null && rens[25]!=""){pw.println("EpLicType: "+rens[25]);}
        pw.println("");
        
        //STYLE
        pw.println("[V4+ Styles]");
        pw.println("Format: Name, Fontname, Fontsize, PrimaryColour, SecondaryColour, OutlineColour, BackColour, Bold, Italic" +
                ", Underline, StrikeOut, ScaleX, ScaleY, Spacing, Angle" +
                ", BorderStyle, Outline, Shadow, Alignment, MarginL, MarginR, MarginV, Encoding");
        //boucle de styles....
        
        if(asc.getMembers().isEmpty()){ // If there is no style
            asc.addMember("Default", new AssStyle());
        }
        
        for (AssStyle as : asc.getMembers()){
            aEcrire = "Style: "+as.getElement(AssStyleType.name)+","; //Name
            aEcrire += as.getElement(AssStyleType.fontname) + ","; //Font
            aEcrire += as.getElement(AssStyleType.fontsize) + ","; //Font
            aEcrire += as.getElement(AssStyleType.primarycolour, "+") + ","; //Color1
            aEcrire += as.getElement(AssStyleType.secondarycolour, "+") + ","; //Color2
            aEcrire += as.getElement(AssStyleType.outlinecolour, "+") + ","; //Color3
            aEcrire += as.getElement(AssStyleType.backcolour, "+") + ","; //Shadow
            aEcrire += as.getElement(AssStyleType.bold) + ",";//Bold
            aEcrire += as.getElement(AssStyleType.italic) +",";//Italic
            aEcrire += as.getElement(AssStyleType.underline) +",";//Underline
            aEcrire += as.getElement(AssStyleType.strikeout) +",";//StrikeOut
            aEcrire += as.getElement(AssStyleType.scaleX) +",";//ScaleX
            aEcrire += as.getElement(AssStyleType.scaleY) +",";//ScaleY
            aEcrire += as.getElement(AssStyleType.spacing) +",";//Spacing
            aEcrire += as.getElement(AssStyleType.angle) +",";//Angle
            aEcrire += as.getElement(AssStyleType.borderstyle) + ",";//BorderStyle
            aEcrire += as.getElement(AssStyleType.outline) + ","; //Outline
            aEcrire += as.getElement(AssStyleType.shadow) + ","; //Shadow
            aEcrire += as.getElement(AssStyleType.alignment, "+") + ",";//Alignment
            aEcrire += as.getElement(AssStyleType.marginL) + ","; //Margin L
            aEcrire += as.getElement(AssStyleType.marginR) + ","; //Margin R
            aEcrire += as.getElement(AssStyleType.marginV) + ","; //Margin V
            aEcrire += as.getElement(AssStyleType.encoding); //Encoding
            pw.println(aEcrire);
        }        
        pw.println("");
        
        //BODY
        pw.println("[Events]");
        pw.println("Format: Layer, Start, End, Style, Name, MarginL, MarginR, MarginV, Effect, Text");
        //boucle de body
//        for(int j=0;j<t.getRowCount();j++){
//            if ((String)t.getValueAt(j, 0)==null ||
//                    ((String)t.getValueAt(j, 0)).length()==0){
//                break; // Ignore bad lines
//            }
//            aEcrire = ReturnEventType((String)t.getValueAt(j, 0)) + ": "; //Format:
//            aEcrire = aEcrire + (String)t.getValueAt(j, 1)+","; //Layer
//            aEcrire = aEcrire + ReturnStartEnd((String)t.getValueAt(j, 3), (String)t.getValueAt(j, 4))+","; //Start, End
//            aEcrire = aEcrire + (String)t.getValueAt(j, 6)+","; //Style
//            aEcrire = aEcrire + (String)t.getValueAt(j, 7)+","; //Name
//            aEcrire = aEcrire + ReturnMargins((String)t.getValueAt(j, 2))+","; //Margins LRV
//            aEcrire = aEcrire + ReturnEffects((String)t.getValueAt(j, 8))+","; //Effect
//            aEcrire = aEcrire + ReturnText((String)t.getValueAt(j, 8)); //Text
//            pw.println(aEcrire);
//        }
        
        ProgramLine pl;
        for(int j=0;j<t.getRowCount();j++){
            // Format du programme :
            // ID(0), Type(1), Layer(2), Margins(3),
            // Start(4), End(5), Total time(6), Style(7),
            // Name(8), Effects(9), Text(10), FX(11) 
            String[] ts = new String[12];
            for (int i=0;i<12;i++){
                ts[i] = t.getValueAt(j, i).toString();
            }
            pl = Format(ts,ModeFormat.ProgramToASS);
            pw.println(pl.toAssLine());
        }
        
        pw.println("");
        
        //On ferme le flux puis le fichier
        pw.close();
        if (asUnicode.compareTo("")==0){
            fw.close();
        }else{
            bw.close();
            fos.close();
        }
        
        } catch (java.io.FileNotFoundException e2){
            System.out.println("Fichier non trouv� - Missing File Error");
        } catch (SecurityException se){
            System.out.println("Acc�s refus� - Access denied");
        } catch (java.io.UnsupportedEncodingException uee){
            System.out.println("Encodage non support� - Unsupported encoding");
        } catch (java.io.IOException e1){
            System.out.println("Fichier non trouv� - Missing File Error");            
        }
    }
    
    /** <p>Read an ASS file and fill in a table.<br />
     * Lit un fichier ASS et replit une table.</p>
     * @param pathFile The name of the file using its absolute path.
     * @param t - The model of the table to fill in. */
    public void LireFichierASSi2(String pathFile, DefaultTableModel t,
            AssInfos ai, AssStyleCollection asc, AssNameCollection anc,
            boolean addLines){
        
        //On cr?e une cha?ne ? retourner
        String newline;
        ProgramLine pl;
        
        try{
            // Encoding detection
            FileReader fr = new FileReader(pathFile);
            String charset = detectCharset(fr);
            fr.close();

            // Start process to read file...
            FileInputStream fis = new FileInputStream(pathFile);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(fis, charset));
        
        //On lit le fichier
        while((newline=br.readLine())!=null){
            
            try{
                
                /* Traitement de l'ent�te */
                
                if(newline.startsWith("Title") && !newline.substring(7).isEmpty()){
                    ai.setElement(AssInfosType.title, newline.substring(7));
                }
                
                if(newline.startsWith("Original Script") && !newline.substring(17).isEmpty()){
                    ai.setElement(AssInfosType.authors, newline.substring(17));
                }
                
                if(newline.startsWith("Original Translation") && !newline.substring(22).isEmpty()){
                    ai.setElement(AssInfosType.translators, newline.substring(22));
                }
                
                if(newline.startsWith("Original Editing") && !newline.substring(18).isEmpty()){
                    ai.setElement(AssInfosType.editors, newline.substring(18));
                }
                
                if(newline.startsWith("Original Timing") && !newline.substring(17).isEmpty()){
                    ai.setElement(AssInfosType.timers, newline.substring(17));
                }
                
                if(newline.startsWith("Original Script Checking") && !newline.substring(26).isEmpty()){
                    ai.setElement(AssInfosType.checkers, newline.substring(26));
                }
                
                if(newline.startsWith("Synch Point") && !newline.substring(13).isEmpty()){
                    ai.setElement(AssInfosType.synchpoint, newline.substring(13));
                }
                
                if(newline.startsWith("Script Updated By") && !newline.substring(19).isEmpty()){
                    ai.setElement(AssInfosType.updateby, newline.substring(19));
                }
                
                if(newline.startsWith("Update Details") && !newline.substring(16).isEmpty()){
                    ai.setElement(AssInfosType.updates, newline.substring(16));
                }
                
                if(newline.startsWith("ScriptType") && !newline.substring(12).isEmpty()){
                    ai.setElement(AssInfosType.scripttype, newline.substring(12));
                }
                
                if(newline.startsWith("Collisions") && !newline.substring(12).isEmpty()){
                    ai.setElement(AssInfosType.collisions, newline.substring(12));
                }
                
                if(newline.startsWith("PlayResX") && !newline.substring(10).isEmpty()){
                    ai.setElement(AssInfosType.playresx, newline.substring(10));
                }
                
                if(newline.startsWith("PlayResY") && !newline.substring(10).isEmpty()){
                    ai.setElement(AssInfosType.playresy, newline.substring(10));
                }
                
                if(newline.startsWith("PlayDepth") && !newline.substring(11).isEmpty()){
                    ai.setElement(AssInfosType.playdepth, newline.substring(11));
                }
                
                if(newline.startsWith("Timer") && !newline.substring(7).isEmpty()){
                    ai.setElement(AssInfosType.timerspeed, newline.substring(7));
                }
                
                if(newline.startsWith("Audio") && !newline.substring(7).isEmpty()){
                    ai.setElement(AssInfosType.audios, newline.substring(7));
                }
                
                if(newline.startsWith("Video") && !newline.substring(7).isEmpty()){
                    ai.setElement(AssInfosType.video, newline.substring(7));
                }
                
                /* Traitement des styles */
                
                if(newline.startsWith("Style:") &&
                        ai.getElement(AssInfosType.scripttype).equalsIgnoreCase("V4.00+")){
                    String[] listStyle = newline.substring(7).split(",");
                    AssStyle as = new AssStyle();
                    as.setElement(AssStyleType.name, listStyle[0]);
                    as.setElement(AssStyleType.fontname, listStyle[1]);
                    as.setElement(AssStyleType.fontsize, listStyle[2]);
                    as.setElement(AssStyleType.primarycolour, listStyle[3],"+");
                    as.setElement(AssStyleType.secondarycolour, listStyle[4],"+");
                    as.setElement(AssStyleType.outlinecolour, listStyle[5],"+");
                    as.setElement(AssStyleType.backcolour, listStyle[6],"+");
                    as.setElement(AssStyleType.bold, listStyle[7]);
                    as.setElement(AssStyleType.italic, listStyle[8]);
                    as.setElement(AssStyleType.underline, listStyle[9]);
                    as.setElement(AssStyleType.strikeout, listStyle[10]);
                    as.setElement(AssStyleType.scaleX, listStyle[11]);
                    as.setElement(AssStyleType.scaleY, listStyle[12]);
                    as.setElement(AssStyleType.spacing, listStyle[13]);
                    as.setElement(AssStyleType.angle, listStyle[14]);
                    as.setElement(AssStyleType.borderstyle, listStyle[15]);                    
                    as.setElement(AssStyleType.outline, listStyle[16]);
                    as.setElement(AssStyleType.shadow, listStyle[17]);
                    as.setElement(AssStyleType.alignment, listStyle[18],"+");
                    as.setElement(AssStyleType.marginL, listStyle[19]);
                    as.setElement(AssStyleType.marginR, listStyle[20]);
                    as.setElement(AssStyleType.marginV, listStyle[21]);
                    as.setElement(AssStyleType.encoding, listStyle[22]);
                    asc.addMember(listStyle[0], as);
                }else if(newline.startsWith("Style:") &&
                        ai.getElement(AssInfosType.scripttype).equalsIgnoreCase("V4.00++")){
                    String[] listStyle = newline.substring(7).split(",");
                    AssStyle as = new AssStyle();
                    as.setElement(AssStyleType.name, listStyle[0]);
                    as.setElement(AssStyleType.fontname, listStyle[1]);
                    as.setElement(AssStyleType.fontsize, listStyle[2]);
                    as.setElement(AssStyleType.primarycolour, listStyle[3],"+");
                    as.setElement(AssStyleType.secondarycolour, listStyle[4],"+");
                    as.setElement(AssStyleType.outlinecolour, listStyle[5],"+");
                    as.setElement(AssStyleType.backcolour, listStyle[6],"+");
                    as.setElement(AssStyleType.bold, listStyle[7]);
                    as.setElement(AssStyleType.italic, listStyle[8]);
                    as.setElement(AssStyleType.underline, listStyle[9]);
                    as.setElement(AssStyleType.strikeout, listStyle[10]);
                    as.setElement(AssStyleType.scaleX, listStyle[11]);
                    as.setElement(AssStyleType.scaleY, listStyle[12]);
                    as.setElement(AssStyleType.spacing, listStyle[13]);
                    as.setElement(AssStyleType.angle, listStyle[14]);
                    as.setElement(AssStyleType.borderstyle, listStyle[15]);                    
                    as.setElement(AssStyleType.outline, listStyle[16]);
                    as.setElement(AssStyleType.shadow, listStyle[17]);
                    as.setElement(AssStyleType.alignment, listStyle[18],"+");
                    as.setElement(AssStyleType.marginL, listStyle[19]);
                    as.setElement(AssStyleType.marginR, listStyle[20]);
                    as.setElement(AssStyleType.marginT, listStyle[21]);
                    as.setElement(AssStyleType.marginB, listStyle[22]);
                    as.setElement(AssStyleType.encoding, listStyle[23]);
                    as.setElement(AssStyleType.relativeto, listStyle[24]);
                    asc.addMember(listStyle[0], as);
                }
                
                /* Traitement des �venements */
                
                pl = Format(newline,ModeFormat.ASSToProgram);

                if(pl.getLineType().getLabel()
                        .equals(ProgramLine.LineType.Nothing.getLabel())==false){
                    t.addRow(pl.toRow());
                }
                
            }catch(IndexOutOfBoundsException ioobe){
                //erreurs = ioobe.getMessage();
            }
            
        }
                
        //On ferme le flux puis le fichier
        br.close();
        fis.close();        
       
        } catch (java.io.FileNotFoundException e2){
            System.out.println("Fichier non trouv? - Missing File Error");
        } catch (SecurityException se){
            System.out.println("Acc?s refus? - Access denied");
        } catch (java.io.UnsupportedEncodingException uee){
            System.out.println("Encodage non support? - Unsupported encoding");
        } catch (java.io.IOException e1){
            System.out.println("Erreur lors de la lecture - Read Error");
        }
        
    }

    /** <p>Extract styles from an ASS file.<br />
     * Extrait les styles à partie d'un fichier ASS.</p> */
    public java.util.List<AssStyle> ExtractASSStyles(String pathFile){

        //On cr?e une cha?ne ? retourner
        String newline; String scriptType = "";
        java.util.List<AssStyle> las = new java.util.ArrayList<AssStyle>();

        try{
            // Encoding detection
            FileReader fr = new FileReader(pathFile);
            String charset = detectCharset(fr);
            fr.close();

            // Start process to read file...
            FileInputStream fis = new FileInputStream(pathFile);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(fis, charset));

        //On lit le fichier
        while((newline=br.readLine())!=null){
            try{
                if(newline.startsWith("ScriptType") && !newline.substring(12).isEmpty()){
                    scriptType = newline.substring(12);
                }

                if(newline.startsWith("Style:") && scriptType.equalsIgnoreCase("V4.00+")){
                    String[] listStyle = newline.substring(7).split(",");
                    AssStyle as = new AssStyle();
                    as.setElement(AssStyleType.name, listStyle[0]);
                    as.setElement(AssStyleType.fontname, listStyle[1]);
                    as.setElement(AssStyleType.fontsize, listStyle[2]);
                    as.setElement(AssStyleType.primarycolour, listStyle[3],"+");
                    as.setElement(AssStyleType.secondarycolour, listStyle[4],"+");
                    as.setElement(AssStyleType.outlinecolour, listStyle[5],"+");
                    as.setElement(AssStyleType.backcolour, listStyle[6],"+");
                    as.setElement(AssStyleType.bold, listStyle[7]);
                    as.setElement(AssStyleType.italic, listStyle[8]);
                    as.setElement(AssStyleType.underline, listStyle[9]);
                    as.setElement(AssStyleType.strikeout, listStyle[10]);
                    as.setElement(AssStyleType.scaleX, listStyle[11]);
                    as.setElement(AssStyleType.scaleY, listStyle[12]);
                    as.setElement(AssStyleType.spacing, listStyle[13]);
                    as.setElement(AssStyleType.angle, listStyle[14]);
                    as.setElement(AssStyleType.borderstyle, listStyle[15]);
                    as.setElement(AssStyleType.outline, listStyle[16]);
                    as.setElement(AssStyleType.shadow, listStyle[17]);
                    as.setElement(AssStyleType.alignment, listStyle[18],"+");
                    as.setElement(AssStyleType.marginL, listStyle[19]);
                    as.setElement(AssStyleType.marginR, listStyle[20]);
                    as.setElement(AssStyleType.marginV, listStyle[21]);
                    as.setElement(AssStyleType.encoding, listStyle[22]);
                    las.add(as);
                }else if(newline.startsWith("Style:") && scriptType.equalsIgnoreCase("V4.00++")){
                    String[] listStyle = newline.substring(7).split(",");
                    AssStyle as = new AssStyle();
                    as.setElement(AssStyleType.name, listStyle[0]);
                    as.setElement(AssStyleType.fontname, listStyle[1]);
                    as.setElement(AssStyleType.fontsize, listStyle[2]);
                    as.setElement(AssStyleType.primarycolour, listStyle[3],"+");
                    as.setElement(AssStyleType.secondarycolour, listStyle[4],"+");
                    as.setElement(AssStyleType.outlinecolour, listStyle[5],"+");
                    as.setElement(AssStyleType.backcolour, listStyle[6],"+");
                    as.setElement(AssStyleType.bold, listStyle[7]);
                    as.setElement(AssStyleType.italic, listStyle[8]);
                    as.setElement(AssStyleType.underline, listStyle[9]);
                    as.setElement(AssStyleType.strikeout, listStyle[10]);
                    as.setElement(AssStyleType.scaleX, listStyle[11]);
                    as.setElement(AssStyleType.scaleY, listStyle[12]);
                    as.setElement(AssStyleType.spacing, listStyle[13]);
                    as.setElement(AssStyleType.angle, listStyle[14]);
                    as.setElement(AssStyleType.borderstyle, listStyle[15]);
                    as.setElement(AssStyleType.outline, listStyle[16]);
                    as.setElement(AssStyleType.shadow, listStyle[17]);
                    as.setElement(AssStyleType.alignment, listStyle[18],"+");
                    as.setElement(AssStyleType.marginL, listStyle[19]);
                    as.setElement(AssStyleType.marginR, listStyle[20]);
                    as.setElement(AssStyleType.marginT, listStyle[21]);
                    as.setElement(AssStyleType.marginB, listStyle[22]);
                    as.setElement(AssStyleType.encoding, listStyle[23]);
                    as.setElement(AssStyleType.relativeto, listStyle[24]);
                    las.add(as);
                }
            }catch(IndexOutOfBoundsException ioobe){
                //erreurs = ioobe.getMessage();
            }
        }

        //On ferme le flux puis le fichier
        br.close();
        fis.close();

        } catch (java.io.FileNotFoundException e2){
            System.out.println("Fichier non trouv? - Missing File Error");
        } catch (SecurityException se){
            System.out.println("Acc?s refus? - Access denied");
        } catch (java.io.UnsupportedEncodingException uee){
            System.out.println("Encodage non support? - Unsupported encoding");
        } catch (java.io.IOException e1){
            System.out.println("Erreur lors de la lecture - Read Error");
        }
        return las;
    }
    
    /** <p>Read an ASS file and fill in a table.<br />
     * Lit un fichier ASS et replit une table.</p>
     * @param pathFile The name of the file using its absolute path.
     * @param t - The model of the table to fill in. */
    public void LireFichierASSi2_Minimal(String pathFile, List<ProgramLine> sublist){
        
        //On cr?e une cha?ne ? retourner
        String newline;
        
        try{
            // Encoding detection
            FileReader fr = new FileReader(pathFile);
            String charset = detectCharset(fr);
            fr.close();

            // Start process to read file...
            FileInputStream fis = new FileInputStream(pathFile);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(fis, charset));
        
        //On lit le fichier
        while((newline=br.readLine())!=null){
            
            try{
                
                /* Traitement des �venements */
                
                if(newline.startsWith("Dialogue:") | 
                        newline.startsWith("Comment:") |
                        newline.startsWith("Picture:") |
                        newline.startsWith("Sound:") |
                        newline.startsWith("Movie:") |
                        newline.startsWith("Command:")){
                    ProgramLine sub = Format(newline, ModeFormat.ASSToProgram);
                    sublist.add(sub);
                }
                
            }catch(IndexOutOfBoundsException ioobe){
                //erreurs = ioobe.getMessage();
            }
            
        }
                
        //On ferme le flux puis le fichier
        br.close();
        fis.close();        
       
        } catch (java.io.FileNotFoundException e2){
            System.out.println("Fichier non trouv? - Missing File Error");
        } catch (SecurityException se){
            System.out.println("Acc?s refus? - Access denied");
        } catch (java.io.UnsupportedEncodingException uee){
            System.out.println("Encodage non support? - Unsupported encoding");
        } catch (java.io.IOException e1){
            System.out.println("Erreur lors de la lecture - Read Error");
        }
        
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" Detection of charset ">
    
    // Byte Order mark :
    // Bytes                    Encoding Form
    // 00 00 FE FF              UTF-32, big-endian
    // FF FE 00 00              UTF-32, little-endian
    // FE FF                    UTF-16, big-endian
    // FF FE                    UTF-16, little-endian
    // EF BB BF                 UTF-8
    
    // ÿ is \u00FF and þ is \u00FE
    
    /** <p>Try to get a correct charset<br />
     * Essaie d'obtenir le bon encodage des caractères.</p> */
    public String detectCharset(String pathname){
        String charset = "";
        
        try {
            // Open the file using system default encoding
            FileReader fr = new FileReader(pathname);
            
            // Try to get a correct charset
            charset = detectCharset(fr);
            
            // Close the file
            fr.close();
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
        
        return charset;
    }
    
    /** <p>Try to get a correct charset<br />
     * Essaie d'obtenir le bon encodage des caractères.</p>
     * <table><tr><td colspan="2">Byte Order mark :</td><td></td></tr>
     * <tr><td width="100">Bytes</td><td>Encoding Form</td></tr>
     * <tr><td>00 00 FE FF</td><td>UTF-32, big-endian</td></tr>
     * <tr><td>FF FE 00 00</td><td>UTF-32, little-endian</td></tr>
     * <tr><td>FE FF</td><td>UTF-16, big-endian</td></tr>
     * <tr><td>FF FE</td><td>UTF-16, little-endian</td></tr>
     * <tr><td>EF BB BF</td><td>UTF-8</td></tr></table> */
    public String detectCharset(FileReader fr){
        String charset = ""; String newline;
        
        try {
            // Create a buffer to read the stream
            BufferedReader br = new BufferedReader(fr);
            
            // Scan for encoding marks
            while ((newline = br.readLine()) != null) {
                
                if(newline.startsWith("[\u0000\u0000") |
                        newline.startsWith("\u00FF\u00FE\u0000\u0000")){
                    charset = "UTF-32LE";
                }else if(newline.startsWith("\u0000\u0000[") |
                        newline.startsWith("\u0000\u0000\u00FE\u00FF")){
                    charset = "UTF-32BE";
                }else if(newline.startsWith("[\u0000") |
                        newline.startsWith("\u00FF\u00FE")){
                    charset = "UTF-16LE";
                }else if(newline.startsWith("\u0000[") |
                        newline.startsWith("\u00FE\u00FF")){
                    charset = "UTF-16BE";
                }else if(newline.startsWith("\u00EF\u00BB\u00BF")){
                    charset = "UTF-8";
                }
                
                // If a charset was found then close the stream
                // and the return charset encoding.
                if (charset.length()!=0){
                    br.close();
                    return charset;
                }
            }
            
            // If nothing was found then set the encoding to system default.
            if (charset.length()==0){
                charset = fr.getEncoding();
            }
        
            // Close the stream
            br.close();
            
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
        
        return charset;
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" AssIO - Clipboard <> Table ">
    
    /** <p>Convert and paste an ASS line to the table.<br />
     * Convertit et colle une ligne ASS dans une table.</p> */
    public void pasteInsert(DefaultTableModel t, AssNameCollection anc)
            throws IOException{
        Clipboard c = new Clipboard();
        StringReader sr = new StringReader(c.CPaste());
        BufferedReader br = new BufferedReader(sr);
        String newline; ProgramLine pl;
        
        //On lit le flux
        while((newline=br.readLine())!=null){
            
            /* Traitement des �venements */
            
            pl = Format(newline,ModeFormat.SSAToProgram);

            if(pl.getLineType().getLabel()
                    .equals(ProgramLine.LineType.Nothing.getLabel())==false){
                t.addRow(pl.toRow());
            }
            
//            if(newline.startsWith("Dialogue:")){
//                newline = newline.substring(10);
//                lt = LineType.Dialogue;
//            }else if(newline.startsWith("Comment:")){
//                newline = newline.substring(9);
//                lt = LineType.Comment;
//            }else if(newline.startsWith("Picture:")){
//                newline = newline.substring(9);
//                lt = LineType.Picture;
//            }else if(newline.startsWith("Sound:")){
//                newline = newline.substring(7);
//                lt = LineType.Sound;
//            }else if(newline.startsWith("Movie:")){
//                newline = newline.substring(7);
//                lt = LineType.Movie;
//            }else if(newline.startsWith("Command:")){
//                newline = newline.substring(9);
//                lt = LineType.Command;
//            }

            
            
//            if(lt.equals(LineType.Nothing)==false){
//                int margL, margV, margR; margL=0; margV=0; margR=0;
//                String[] strDialogue = {"",""}; String str5, str6, TotalTime;
//
//                //Traitement du tableau
//                strDialogue = RemplirTableau(newline, "ass");
//                str5 = strDialogue[1].replaceAll(":", "."); // Reformatage du temps (0:00:00.00 > 0.00.00.00)
//                str6 = strDialogue[2].replaceAll(":", "."); // Reformatage du temps (0:00:00.00 > 0.00.00.00)
//                // On veut maintenant mettre les margins sous le format - Margins LVR (0,0,0)
//                try{margL = Integer.parseInt(strDialogue[5]);}catch(NumberFormatException nfe){margL=0;}
//                try{margR = Integer.parseInt(strDialogue[6]);}catch(NumberFormatException nfe){margR=0;}
//                try{margV = Integer.parseInt(strDialogue[7]);}catch(NumberFormatException nfe){margV=0;}
//                // On doit calculer la dur?e du temps (Total time=End-Start)
//                TotalTime = ReturnTotalTime(str5, str6);
//                // On doit remettre la chaine dans l'ordre de traitement de JTable
//                // On replace directement et on s'occupe des margins LRV>LVR et du temps total
//                // "Type", "Layer", "Margins", "Start", "End", "Total time", "Style", "Name", "Text"
//                anc.addMember(strDialogue[4]); //Ajoute (ou non) un nom.
//
//                Object[] aInserer2 = {lt.getLabel(),strDialogue[0],
//                margL+","+margV+","+margR,str5,str6,TotalTime,
//                strDialogue[3],strDialogue[4],strDialogue[9]};
//                t.addRow(aInserer2);
//            }
            
        }
        
        //On ferme les flux
        br.close(); sr.close();
        
    }
    
    /** <p>Copy selected lines from a table to the clipboard.<br />
     * Copie les lignes sélectionnées d'une table et l'envoie vers le presse-papier. */
    public void copySelectedLines(DefaultTableModel t, javax.swing.JTable tb){
        Clipboard c = new Clipboard();
        String newline = ""; ProgramLine pl;
        
        for (int row : tb.getSelectedRows()){
            // Format du programme :
            // ID(0), Type(1), Layer(2), Margins(3),
            // Start(4), End(5), Total time(6), Style(7),
            // Name(8), Effects(9), Text(10), FX(11) 
            String[] ts = new String[12];
            for (int i=0;i<12;i++){
                ts[i] = t.getValueAt(row, i).toString();
            }
            pl = Format(ts,ModeFormat.ProgramToASS);
            newline = newline + pl.toAssLine() + "\n";
        }
        
        c.CCopy(newline); //Copy to the clipboard
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" Old things (remember) ">
    /* Start process to read file...
     * Entry 00 : RUBY_PATH:                Entry 01 : XML_PATH:
     * Entry 02 : FXPLUG_PATH:              Entry 03 : DOCS_PATH:
     * Entry 04 : RUBY_EDITOR:              Entry 05 : DRAW_EDITOR: 
     *
     * Entry 00 : FX_PATH:                  Entry 01 : XFX_PATH:
     * Entry 02 : FXPLUG_PATH:              Entry 03 : DOCS_PATH:
     * Entry 04 : RUBY_EDITOR:              Entry 05 : DRAW_EDITOR:
     * Read the Config file to set up all paths needed */
    //    public String[] ReadConfig(){
//        File file = new File(
//                getAssFxMakerDirectory()+
//                File.separator+"Configuration.ini");
//        String[] table = new String[20];
//        try{
//            FileInputStream fis = new FileInputStream(file);
//            java.io.BufferedReader br = new java.io.BufferedReader(
//                    new java.io.InputStreamReader(fis, "UTF-8"));
//            String newline = "";
//
//            //Reading of file
//            while((newline=br.readLine())!=null){
//                if(newline.startsWith("FX_PATH:")){
//                    table[0] = newline.substring(9);// Ruby, Snippet
//                }
//                if(newline.startsWith("XFX_PATH:")){
//                    table[1] = newline.substring(10);//xmlScripts
//                }
//                if(newline.startsWith("FXPLUG_PATH:")){
//                    table[2] = newline.substring(13);//javaPlugins
//                }
//                if(newline.startsWith("DOCS_PATH:")){
//                    table[3] = newline.substring(11);//Documents as help
//                }
//                if(newline.startsWith("RUBY_EDITOR:")){
//                    table[4] = newline.substring(13);//rubyEditor
//                }
//                if(newline.startsWith("DRAW_EDITOR:")){
//                    table[5] = newline.substring(13);//drawingEditor
//                }
//                if(newline.startsWith("FONT:")){
//                    table[6] = newline.substring(6);//font of Org/ResTable
//                }
//                if(newline.startsWith("THEME:")){
//                    table[7] = newline.substring(7);//font of Org/ResTable
//                }
//                if(newline.startsWith("TABLE_DISPLAY:")){
//                    table[8] = newline.substring(15);//display of tables
//                }
//            }
//            br.close(); fis.close();
//            return table;
//        }catch(Exception exc){
//            return null;
//        }
//    }
    
//    public void SaveConfig(String[] table){
//        File file = new File(
//                getAssFxMakerDirectory()+
//                File.separator+"Configuration.ini");
//        try{
//            FileOutputStream fos = new FileOutputStream(file);
//            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
//            PrintWriter pw = new PrintWriter(bw);
//            pw.println("FX_PATH: "+table[0]);
//            pw.println("XFX_PATH: "+table[1]);
//            pw.println("FXPLUG_PATH: "+table[2]);
//            pw.println("DOCS_PATH: "+table[3]);
//            pw.println("RUBY_EDITOR: "+table[4]);
//            pw.println("DRAW_EDITOR: "+table[5]);
//            pw.println("FONT: "+table[6]);
//            pw.println("THEME: "+table[7]);
//            pw.println("TABLE_DISPLAY: "+table[8]);
//            pw.println("");
//            pw.flush();
//            pw.close(); bw.close(); fos.close();
//        }catch(Exception exc){
//
//        }
//    }
    
//    private ModeFormat autoDetectMF(Object o){
//        ModeFormat mf = null;
//        
//        return mf;
//    }
    
//    /** D�tection de l'UTF-8 avec un flux d'entr�e normalement en Latin */
//    private boolean detecterFluxUTF_8(String path){
//        try{
//            FileReader fr = new FileReader(path);
//            BufferedReader br = new BufferedReader(fr);
//            String cherche = "";
//            //On �crit une expression r�guli�re pouvant d�terminer si l'emploi
//            // de caract�res non occidentaux a �t� utilis� dans le fichier; ceci
//            // en dernier recours des autres expressions r�guli�res �tant
//            // recherch�es en index.
//            // On cherche tout caract�re non inclus entre u0000 et u00FF
//            // sachant que l'ASCII va de u0000 � u007F et
//            // l'ISO-8859-1 va de u0000 � u00FF. Ce qui confirme notre recherche
//            // de caract�res unicode UTF-8.
//            Pattern p = Pattern.compile("[^\u0000-\u00FF]");
//            Matcher m;
//            
//            //On sort au premier caract�re trouv�
//            while((cherche=br.readLine())!=null){
//                if (cherche.indexOf("ä")!=-1){
//                    br.close(); fr.close(); return true;}
//                if (cherche.indexOf("â")!=-1){
//                    br.close(); fr.close(); return true;}
//                if (cherche.indexOf("à")!=-1){
//                    br.close(); fr.close(); return true;}
//                if (cherche.indexOf("é")!=-1){
//                    br.close(); fr.close(); return true;}
//                if (cherche.indexOf("è")!=-1){
//                    br.close(); fr.close(); return true;}
//                if (cherche.indexOf("ê")!=-1){
//                    br.close(); fr.close(); return true;}
//                if (cherche.indexOf("ë")!=-1){
//                    br.close(); fr.close(); return true;}
//                if (cherche.indexOf("î")!=-1){
//                    br.close(); fr.close(); return true;}
//                if (cherche.indexOf("ï")!=-1){
//                    br.close(); fr.close(); return true;}
//                if (cherche.indexOf("ô")!=-1){
//                    br.close(); fr.close(); return true;}
//                if (cherche.indexOf("ö")!=-1){
//                    br.close(); fr.close(); return true;}
//                if (cherche.indexOf("ù")!=-1){
//                    br.close(); fr.close(); return true;}
//                if (cherche.indexOf("ü")!=-1){
//                    br.close(); fr.close(); return true;}
//                if (cherche.indexOf("û")!=-1){
//                    br.close(); fr.close(); return true;}
//                if (cherche.indexOf("ñ")!=-1){
//                    br.close(); fr.close(); return true;}
//                if (cherche.indexOf("ç")!=-1){
//                    br.close(); fr.close(); return true;}
//                if (cherche.indexOf("€")!=-1){
//                    br.close(); fr.close(); return true;}
//                if (cherche.indexOf("£")!=-1){
//                    br.close(); fr.close(); return true;}
//                if (cherche.indexOf("¤")!=-1){
//                    br.close(); fr.close(); return true;}
//                if (cherche.indexOf("µ")!=-1){
//                    br.close(); fr.close(); return true;}
//                if (cherche.indexOf("§")!=-1){
//                    br.close(); fr.close(); return true;}
//                if (cherche.indexOf("°")!=-1){
//                    br.close(); fr.close(); return true;}
//                if (cherche.indexOf("²")!=-1){
//                    br.close(); fr.close(); return true;}
//                if (cherche.indexOf("¨")!=-1){
//                    br.close(); fr.close(); return true;}
//                //m = p.matcher(cherche);
//                //if (m.find()==true){
//                //    br.close(); fr.close(); return true;}
//            }
//            //On ferme les flux
//            br.close(); fr.close(); 
//            
//        }catch(java.io.IOException ioe){
//            return false;
//        }
//        return false;
//    }
    
//    // Reformatage du texte � �crire en SSA - ComboBox(Format):
//    // Copie alt�r�e de ComboReturnEvent@FunsubMod
//    private String ReturnEventType(String selection){
//        String str = "";
//        if(selection.compareTo("D")==0){str=forCB[0];}
//        if(selection.compareTo("#")==0){str=forCB[1];}
//        if(selection.compareTo("P")==0){str=forCB[2];}
//        if(selection.compareTo("S")==0){str=forCB[3];}
//        if(selection.compareTo("M")==0){str=forCB[4];}
//        if(selection.compareTo("C")==0){str=forCB[5];}
//        return str;
//    }
    
//    // Reformatage du texte � �crire en SSA - (Start, End):
//    private String ReturnStartEnd(String s, String e){
//        String str = s + "," + e;
//        try{
//        StringBuilder sb01 = new StringBuilder(s);
//        StringBuilder sb02 = new StringBuilder(e);
//        //sb01.deleteCharAt(s.length()-1); sb01.deleteCharAt(0);
//        //sb02.deleteCharAt(e.length()-1); sb02.deleteCharAt(0);
//        sb01.replace(1, 2, ":"); sb01.replace(4, 5, ":");
//        sb02.replace(1, 2, ":"); sb02.replace(4, 5, ":");
//        s = sb01.toString();
//        e = sb02.toString();
//        }catch (java.lang.Exception exc){
//            return "error,error";
//        }
//        str = s + "," + e;
//        return str;
//    }
    
//    private String ReturnTotalTime(String s, String e){
//        String str = "";
//        int sH, sM, sS, sC, eH, eM, eS, eC;
//        try{
//            sH = Integer.parseInt(s.substring(0, s.indexOf(".")));//0
//            sM = Integer.parseInt(s.substring(s.indexOf(".")+1, s.indexOf(".", 3)));//3
//            sS = Integer.parseInt(s.substring(s.indexOf(".", 3)+1, s.indexOf(".", 6)))*100+
//                    Integer.parseInt(s.substring(s.indexOf(".", 6)+1, s.indexOf(".", 6)+3));//6
//            //sC = ;//9
//            eH = Integer.parseInt(e.substring(0, e.indexOf(".")));//0
//            eM = Integer.parseInt(e.substring(e.indexOf(".")+1, e.indexOf(".", 3)));//3
//            eS = Integer.parseInt(e.substring(e.indexOf(".", 3)+1, e.indexOf(".", 6)))*100+
//                    Integer.parseInt(e.substring(e.indexOf(".", 6)+1, e.indexOf(".", 6)+3));//6
//            //eC = ;//9
//        }catch (java.lang.NumberFormatException nfe){
//            return "format error";
//        }
//        int Start = sH*360000 + sM*6000 + sS;
//        int End = eH*360000 + eM*6000 + eS;
//        int TotalTime = End - Start;
//        int hour = TotalTime/360000;
//        int min = (TotalTime - 360000*hour)/6000;
//        int sec = (TotalTime - 360000*hour - 6000*min)/100;
//        int cSec = TotalTime - 360000*hour - 6000*min - 100*sec;
//        String Smin, Ssec, Scent;
//        if (min<10){Smin = "0"+min;}else{Smin = String.valueOf(min);}
//        if (sec<10){Ssec = "0"+sec;}else{Ssec = String.valueOf(sec);}
//        if (cSec<10){Scent = "0"+cSec;}else{Scent = String.valueOf(cSec);}
//        str = hour + "." + Smin + "." + Ssec + "." + Scent;
//        return str;
//    }
    
//    // Reformatage du texte � �crire en SSA - (Margins):
//    private String ReturnMargins(String mar){
//        String str = mar;
//        String[] t = mar.split(",");
//        for(int i=0;i<3;i++){
//            boolean negative = false;
//            if(t[i].startsWith("-")){
//                negative = true;
//                StringBuilder sb01 = new StringBuilder(t[i]);
//                sb01.deleteCharAt(0);
//                t[i] = sb01.toString();
//            }
//            if(t[i].length()==1){t[i] = "000"+t[i];}
//            if(t[i].length()==2){t[i] = "00"+t[i];}
//            if(t[i].length()==3){t[i] = "0"+t[i];}
//            if(negative==true){t[i] = "-"+t[i];}
//        }
//        String l = t[0]; //Left
//        String v = t[1]; //Vertical
//        String r = t[2]; //Right
//        str = l + "," + r + "," + v;
//        return str;
//    }
    
//    // Reformatage du texte � �crire en SSA/ASS - (Effect):
//    private String ReturnEffects(String textLine){
//        String str = "";
//        if(textLine.indexOf("\\k")>-1){
//            str = "Karaoke";
//        }else if(textLine.indexOf("Scroll up;")>-1){
//            try{
//            str = textLine.substring(textLine.indexOf("Scroll"),
//                    textLine.indexOf(",", textLine.indexOf("Scroll")));
//            }catch (java.lang.IndexOutOfBoundsException ioob){
//                str = "Scroll up";
//            }
//        }else if(textLine.indexOf("Scroll down;")>-1){
//            try{
//            str = textLine.substring(textLine.indexOf("Scroll"),
//                    textLine.indexOf(",", textLine.indexOf("Scroll")));
//            }catch (java.lang.IndexOutOfBoundsException ioob){
//                str = "Scroll down";
//            }
//        }else if(textLine.indexOf("Banner;")>-1){
//            try{
//            str = textLine.substring(textLine.indexOf("Banner"),
//                    textLine.indexOf(",", textLine.indexOf("Banner")));
//            }catch (java.lang.IndexOutOfBoundsException ioob){
//                str = "Banner";
//            }
//        }else{
//            str = "";
//        }
//        return str;
//    }
    
//    // Reformatage du texte � �crire en SSA/ASS - (Texte)
//    private String ReturnText(String textLine){
//        String str = "";
//        if(textLine.indexOf("Scroll up;")>-1){
//            try{
//            str = textLine.substring(textLine.indexOf(",", textLine.indexOf("Scroll"))+1);
//            }catch (java.lang.IndexOutOfBoundsException ioob){
//                str = "";
//            }            
//        }else if(textLine.indexOf("Scroll down;")>-1){
//            try{
//            str = textLine.substring(textLine.indexOf(",", textLine.indexOf("Scroll"))+1);
//            }catch (java.lang.IndexOutOfBoundsException ioob){
//                str = "";
//            }
//        }else if(textLine.indexOf("Banner;")>-1){
//            try{
//            str = textLine.substring(textLine.indexOf(",", textLine.indexOf("Banner"))+1);
//            }catch (java.lang.IndexOutOfBoundsException ioob){
//                str = "";
//            }
//        }else{
//            try{
//                textLine = textLine.replaceAll("Scroll downn","Scroll down");
//                textLine = textLine.replaceAll("Scroll upp","Scroll up");
//                textLine = textLine.replaceAll("Bannerr","Banner");
//            }catch (java.util.regex.PatternSyntaxException pse){
//                //Nothing
//            }            
//            str = textLine;
//        }
//        return str;
//    }
    
//    // Reformatage du texte � �crire en SSA - (Marked):
//    private String ReturnMarked(){
//        return "0";
//    }
    
//    /** Formate une couleur en entier � partir de la couleur donn�e
//     * SSA          (ASS)
//     * 0            (H000000)
//     * 16777215     (HFFFFFF)
//     */
//    private int LongColorSSA(Color c){
//        int x = 0;
//        int red = c.getRed();
//        int green = c.getGreen();
//        int blue = c.getBlue();
//        int i1, i2, i3, i4, i5, i6;
//        i1 = red / 16;
//        i2 = red - (i1 * 16);
//        i3 = green / 16;
//        i4 = green - (i3 * 16);
//        i5 = blue / 16;
//        i6 = blue - (i5 * 16);
//        x = 1048576*i6 + 65536*i5 + 4096*i4 + 256*i3 + 16*i2 + 1*i1;
//        return x;
//    }
    
//    private Color LongColorSSA(int lg){
//        Color c;
//        if(lg==-2147483640){return Color.black;}
//        int i1, i2, i3, i4, i5, i6;
//        i6 = lg/1048576;    lg = lg - (i6*1048576);
//        i5 = lg/65536;      lg = lg - (i5*65536);
//        i4 = lg/4096;       lg = lg - (i4*4096);
//        i3 = lg/256;        lg = lg - (i3*256);
//        i2 = lg/16;         lg = lg - (i2*16);
//        i1 = lg/1;
//        c = new Color((i1*16)+i2,(i3*16)+i4,(i5*16)+i6);
//        return c;
//    }
     
//     private Color LongColorASS(String lg){
//        Color c;
//        int[] value = new int[8];
//        int ired, igreen, iblue, ialpha;
//        for (int i=2;i<10;i++){
//            if(lg.toUpperCase().charAt(i)=='A'){
//                value[i-2] = 10;
//            }else if(lg.toUpperCase().charAt(i)=='B'){
//                value[i-2] = 11;
//            }else if(lg.toUpperCase().charAt(i)=='C'){
//                value[i-2] = 12;
//            }else if(lg.toUpperCase().charAt(i)=='D'){
//                value[i-2] = 13;
//            }else if(lg.toUpperCase().charAt(i)=='E'){
//                value[i-2] = 14;
//            }else if(lg.toUpperCase().charAt(i)=='F'){
//                value[i-2] = 15;
//            }else{
//                try{
//                    value[i-2] = Integer.parseInt(lg.substring(i, i+1));
//                }catch (java.lang.NumberFormatException nfe){
//                    //Nothing
//                }
//            }
//        }
//        ialpha = value[0]*16+value[1];
//        iblue = value[2]*16+value[3];
//        igreen = value[4]*16+value[5];
//        ired = value[6]*16+value[7];
//        c = new Color(ired,igreen,iblue,ialpha);
//        return c;
//    }
     
//     private String[] RemplirTableau(String str, String format){
//        String[] returnStr = new String[10];
//        
//        if (format.equalsIgnoreCase("SSA")==true || format.equalsIgnoreCase("ASS")==true){
//            String str2 = str;
//            if (format.equalsIgnoreCase("SSA")==true){
//                try{str2 = str2.replaceAll("Marked=", "");
//                }catch(java.util.regex.PatternSyntaxException pse){}
//            }
//            for (int count=0;count<9;count++){
//                int index = str2.indexOf(",");
//                try{returnStr[count] = str2.substring(0, index);
//                }catch(java.lang.IndexOutOfBoundsException ioobe){}
//                try{str2 = str2.substring(index+1);
//                }catch(java.lang.IndexOutOfBoundsException ioobe){}
//            }
//            returnStr[9] = str2;
//        }else if (format.equalsIgnoreCase("SUB")==true){
//            
//        }else if (format.equalsIgnoreCase("SRT")==true){
//            
//        }
//        
//        return returnStr;
//    }
    
    // </editor-fold>
    
    /** <p>Return a color in ASS format (BBGGRR).<br />
     * Retourne une couleur dans le format ASS (BBVVRR)</p> */
     public String LongColorASS(Color c){
        String sortie;
        int red = c.getRed();
        int green = c.getGreen();
        int blue = c.getBlue();
        int alpha = c.getAlpha();
        String Sred = Integer.toHexString(red).toUpperCase();
        String Sgreen = Integer.toHexString(green).toUpperCase();
        String Sblue = Integer.toHexString(blue).toUpperCase();
        String Salpha = Integer.toHexString(alpha).toUpperCase();
        if (Sred.length()==1){Sred="0"+Sred;}
        if (Sgreen.length()==1){Sgreen="0"+Sgreen;}
        if (Sblue.length()==1){Sblue="0"+Sblue;}
        if (Salpha.length()==1){Salpha="0"+Salpha;}
        sortie = "&H" + Salpha + Sblue + Sgreen + Sred;
        return sortie;
    }
     
    /** <p>Test : Create a virgin file.<br />
      * Test : Créer un fichier vide.</p> */
    void MakeNullFile(String pathFile){
        try{
        //On cr�e le fichier
        FileWriter fw = new FileWriter(pathFile);
        
        //On cr�e un flux pour l'�criture des caract�res
        PrintWriter pw = new PrintWriter(fw);
        
        //On �crit dans le fichier
        pw.print("null file for testing only");
        
        //On ferme le flux puis le fichier
        pw.close();
        fw.close();
        } catch (java.io.IOException e1){
            System.out.println("Fichier non trouv� - Missing File Error");            
        }
    }
    
}