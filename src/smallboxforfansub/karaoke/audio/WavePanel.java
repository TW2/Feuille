/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * WavePanel.java
 *
 * Created on 30 sept. 2011, 09:09:09
 */
package smallboxforfansub.karaoke.audio;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Player;
import javax.media.Time;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

/**
 *
 * @author The Wingate 2940
 */
public class WavePanel extends javax.swing.JPanel implements Runnable {
    
    File fichierSon = null; //Le fichier son qu'on se prépare à lire/timer
    AudioInputStream ais; //Le flux contenant le fichier
    Player audioPlayer; //Ce qui permet de lire le fichier    
    Thread audioThread; //Une tâche: affichage temps écoulé et progression
    File fichierImage = new File("d:/waveOutput.png"); //Contient la forme d'onde générée
    Image image = null;
    
    Color cCourbe = Color.gray; //Couleur de la courbe
    Color cCourant = Color.magenta; //Couleur de la barre de progression
    Color cZone1 = Color.yellow; //Couleur de la barre d'encadrement inférieure
    Color cZone2 = Color.red; //Couleur de la barre d'encadrement supérieur
    
    int x=0; //Variable du temps t
    int x1=0; //Variable de limite inférieure d'encadrement
    int x2=0; //Variable de limite supérieure d'encadrement
    
    double T=0.0; //Temps total en secondes
    double t=0.0; //Temps à l'instant t
    double t1=0.0; //Temps de limite inférieure d'encadrement
    double t2=0.0; //Temps de limite supérieure d'encadrement
    
    String debut="0.00.00.00"; //Temps de limite inférieure d'encadrement
    String fin="0.00.00.00"; //Temps de limite supérieure d'encadrement
    
    int totalPixels = 0; //Pixels des images générés
    int displayZone = 0; //Pixel de la vue actuel par rapport à la scrollbar (valeur absolue de la scrollbar)
        
    Tags tags = new Tags();
    Tag currenttag = new Tag();
    Tag leftTag = new Tag();
    Tag rightTag = new Tag();
    boolean tagMove = false;
    int kx1 = 0;
    int kx2 = 0;
    double kt1 = 0.0;
    double kt2 = 0.0;
    private Float alpha = 0.2f; //Pour connaitre la transparence de l'image (0f = transparent ; 1f=opaque).
        
    java.util.List<String> arlFichierImage = new java.util.ArrayList<String>();
    
    

    /** Creates new form WavePanel */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public WavePanel() {
        initComponents();
        init();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(204, 255, 204));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                formMouseReleased(evt);
            }
        });
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                formMouseDragged(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseReleased
        if(evt.getButton()==2){
            if(tags.isSet()){
                for(Tag stag : tags.getTags()){
                    if(currenttag.equals(stag)){
                        currenttag.setX(evt.getX());
                        currenttag = new Tag();
                        repaint();
                        tagMove = false;
                    }
                }
            }
        }
    }//GEN-LAST:event_formMouseReleased

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        if(evt.getButton()==2){
            if(tags.isSet()){
                for(Tag stag : tags.getTags()){
                    if(stag.mouseOver(evt.getX())){
                        currenttag = stag;
                        repaint();
                        tagMove = true;
                    }
                }
            }
        }
    }//GEN-LAST:event_formMousePressed

    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
        if(tagMove==true){
            if(tags.isSet()){
                for(Tag stag : tags.getTags()){
                    if(currenttag.equals(stag)){
                        if(leftTag!=null && leftTag.equals(stag)){ leftTag.setX(evt.getX()); kx1=evt.getX(); }
                        if(rightTag.equals(stag)){ rightTag.setX(evt.getX()); kx2=evt.getX(); }
                        currenttag.setX(evt.getX());
                        repaint();
                    }
                }
            }
        }
    }//GEN-LAST:event_formMouseDragged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void init(){
        arlFichierImage.clear();
        totalPixels = 0;
        new wpMouseListener(this);
    }
    
    // <editor-fold defaultstate="collapsed" desc="JavaSound API + JMF API methods">
    private boolean writeInImageOutput(FileImageOutputStream fileIOS,
            BufferedImage bi, boolean andCloseIt){
        try{
            //Appends new graphics to the output
            ImageIO.write(bi, "PNG", fileIOS);
            if(andCloseIt==false){
                fileIOS.flush();
            }else{
                fileIOS.close();
            }
            
        }catch (java.io.IOException ioe){
            System.out.println("PNG Aborted");
            return false;
        }
        return true;
    }
    
    /** Crée un flux audio */
    public void createAudioInputStream(File f){
        try{
        ais = AudioSystem.getAudioInputStream(f);
        fichierSon = f;
        }catch ( javax.sound.sampled.UnsupportedAudioFileException uafe){
        }catch( java.io.IOException ioe){            
        }     
        if(ais!=null){
            //createWaveForm(null,0,0);
            //wav();
            new_wav2(fichierImage.getAbsolutePath()); //Crée le(s) fichier(s) image(s).
            prepareToPlay(); // On prépare le player et on définit le temps T
        }
    }
    
    public void new_wav2(String imagePath){
        byte[] audioBytes;
        AudioFormat format = ais.getFormat();
        
        //Flux dans lequel l'image est générée
        FileImageOutputStream f = null;
        
        //Cache de l'image; à écrire dans le flux
        BufferedImage bi = null;
        
        //Outil de dessin dans l'image
        Graphics2D g2 = null;
        
        //On cherche à déterminer combien d'images générer en fonction des
        //bytes et de déterminer la taille des images.
        //On part sur l'échelle 833.334 samples = 1.000 pixels
        int nbBytes = (int)ais.getFrameLength()*format.getFrameSize();
        System.out.println("Nombre de bytes : "+nbBytes);
        int nbTableau = nbBytes/833334;
        System.out.println("Nombre de tableaux : "+nbTableau+" Réel : "+(nbBytes/833334));
        int bytesRestants = nbBytes-833334*nbTableau;
        System.out.println("Nombre de bytes restants : "+bytesRestants);
        int tailleDerniereImage = Math.round(1000*bytesRestants/833334);
        System.out.println("Largeur de la dernière image : "+tailleDerniereImage);
        

                
        for(int tab=0;tab<=nbTableau;tab++){
            //Fichier du dessin
            String new_imagePath = imagePath.substring(0, imagePath.lastIndexOf("."))+"-"+tab+".png";
            try{
                f = new FileImageOutputStream(new File(new_imagePath));
                arlFichierImage.add(new_imagePath);
            }catch (Exception exc){
            }
            
            //Remplissage de audioBytes
            try {
                if(tab==nbTableau){
                    audioBytes = new byte[bytesRestants];
                    ais.read(audioBytes);
                }else{
                    audioBytes = new byte[833334];
                    ais.read(audioBytes);
                }
            } catch (Exception ex) {
                System.out.println("Erreur monumentale"); //Cà bloque là
                return;
            }

            //Renseignement sur la zone graphique à peindre
            int h = getSize().height;
            //Renseignement pour la génération
            byte my_byte = 0;
            double y_last = 0;
            
            //Mise en place d'un compteur
            //int compteur2 = 0;// Compteur de boucles de samples

            //Exploitation de audioBytes - mise en forme des lignes graphiques
            int maValeur = 0;
            //Le nombre de samples à traiter
            int nlengthInSamples = 0;
            //Le nombre de samples sur une zone de 30000
            int nbSamplesInArea = 200000;
            //Le nombre de pixels total de l'image
            int nbPixelsInImage = 1000;
            if(tab==nbTableau){
                nbPixelsInImage = tailleDerniereImage;
                totalPixels += tailleDerniereImage;
            }else{
                totalPixels += 1000;
            }
            

            if(format.getSampleSizeInBits() == 16){
                //On se renseigne sur la nombre de samples
                nlengthInSamples = audioBytes.length / 2;

                //On veut 1000;
                nbSamplesInArea = nlengthInSamples;

                //Crée une nouvelle image
                //System.out.println("16|Taille horizontal de l'image: "+nbPixelsInImage);
                //System.out.println("16|Nombre de samples: "+nlengthInSamples);
                //System.out.println("16|Nombre de samples par zone dans le buffer: "+nbSamplesInArea);
                bi = new BufferedImage(nbPixelsInImage,getHeight(),
                        BufferedImage.TYPE_INT_ARGB_PRE);
                //Crée un outil pour dessiner
                //System.out.println("16|1°point OK");
                g2 = bi.createGraphics();
                //System.out.println("16|2°point OK");
                //Change la couleur du dessin
                g2.setColor(this.cCourbe);
                //System.out.println("16|3°point OK");
                if (format.isBigEndian()) {
                    for (int i = 0; i < nlengthInSamples; i++) {
                        /* First byte is MSB (high order) */
                        int MSB = (int) audioBytes[2*i];
                        /* Second byte is LSB (low order) */
                        int LSB = (int) audioBytes[2*i+1];
                        maValeur = MSB << 8 | (255 & LSB);
                        my_byte = (byte) (128 * maValeur / 32768 );
                        double gx = i/(nbSamplesInArea/1000);
                        //double gx = (i - compteur2*nbSamplesInArea)/(nbSamplesInArea/30000);
                        //(double) (w*i)/nlengthInSamples;
                        double y_new = (double) (h * (128 - my_byte) / 256);
                        g2.draw(new Line2D.Double(gx, y_last, gx, y_new));
                        bi.flush();
                        y_last = y_new;
                    }
                }else{
                    for (int i = 0; i < nlengthInSamples; i++) {
                        /* First byte is LSB (low order) */
                        int LSB = (int) audioBytes[2*i];
                        /* Second byte is MSB (high order) */
                        int MSB = (int) audioBytes[2*i+1];
                        maValeur = MSB << 8 | (255 & LSB);
                        my_byte = (byte) (128 * maValeur / 32768 );
                        double gx = i/(nbSamplesInArea/1000);
                        //double gx = (i - compteur2*nbSamplesInArea)/(nbSamplesInArea/30000);
                        //(double) (w*i)/nlengthInSamples;
                        double y_new = (double) (h * (128 - my_byte) / 256);
                        g2.draw(new Line2D.Double(gx, y_last, gx, y_new));
                        bi.flush();
                        y_last = y_new;
                    }
                }
            }else if(format.getSampleSizeInBits() == 8){
                nlengthInSamples = audioBytes.length;
                nbSamplesInArea = nlengthInSamples;
                //System.out.println("08|Taille horizontal de l'image: "+nbPixelsInImage);
                //System.out.println("08|Nombre de samples: "+nlengthInSamples);
                //System.out.println("08|Nombre de samples par zone dans le buffer: "+nbSamplesInArea);
                bi = new BufferedImage(nbPixelsInImage,getHeight(),
                        BufferedImage.TYPE_INT_ARGB_PRE);
                //System.out.println("08|1°point OK");
                g2 = bi.createGraphics();
                //System.out.println("08|2°point OK");
                g2.setColor(this.cCourbe);            
                //System.out.println("08|3°point OK");
                if (format.getEncoding().toString().startsWith("PCM_SIGN")) {
                    for (int i = 0; i < audioBytes.length; i++) {
                        maValeur = audioBytes[i];
                        my_byte = (byte)maValeur;
                        double gx = i/(nbSamplesInArea/1000);
                        //double gx = i/(nbSamplesInArea/30000);
                        //(double) (w*i)/nlengthInSamples;
                        double y_new = (double) (h * (128 - my_byte) / 256);
                        g2.draw(new Line2D.Double(gx, y_last, gx, y_new));
                        bi.flush();
                        y_last = y_new;
                    }
                }else{
                    for (int i = 0; i < audioBytes.length; i++) {
                        maValeur = audioBytes[i] - 128;
                        my_byte = (byte)maValeur;
                        double gx = i/(nbSamplesInArea/1000);
                        //double gx = i/(nbSamplesInArea/30000);
                        //(double) (w*i)/nlengthInSamples;
                        double y_new = (double) (h * (128 - my_byte) / 256);
                        g2.draw(new Line2D.Double(gx, y_last, gx, y_new));
                        bi.flush();
                        y_last = y_new;
                    }
                }
            }
            //Sauvegarde le fichier image contenant la forme d'onde
            this.writeInImageOutput(f,bi,true);//Sauvegarde le flux restant
            //try{ImageIO.write(bi, "PNG", fichierImage);
            //}catch(IOException ioe){System.out.println("PNG Aborted");}
            image = Toolkit.getDefaultToolkit().getImage(fichierImage.getPath());
//            setSize(nbPixelsInImage,getHeight());
//            sb2.setMaximum(nbPixelsInImage-90);
            System.out.println(tab+"/"+nbTableau+" file(s) :: PNG -OK");

            
        }
        
        repaint();
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Funsub (TimingGood)">
    // Cet espace contient les méthodes de Funsub ou plutôt du composant
    // TimingGood. Revues et corrigées pour WaveSound.
    
    /** Repaint le composant de temps en faisant avancer la barre de temps t */
    private void progressMe(){
        if(audioPlayer!=null){
            int oldx = x;
            x = timeToPixel(audioPlayer.getMediaTime().getSeconds(),true);
            if(oldx!=x){
                repaint();
            }
        }
    }
    
    /** Retourne le pixel sur x approximatif du temps recherché */
    private int timeToPixel(double time, boolean isNano){
        int tpx = 0; //pixel = nano*pixel/nano
        if(isNano==true){
            tpx=(int)(time*getWidth()/T);
        }else{
            tpx=(int)time*getWidth()/(int)T;
        }        
        return tpx;
    }
    
    public void createNewSound(String path){
        try{
            audioPlayer = Manager.createRealizedPlayer(new MediaLocator("file:///"+path));            
        }catch (javax.media.CannotRealizeException cre){
            System.out.println("non réalisable");
        }catch (javax.media.NoPlayerException npe){
           //Nothing            
        }catch (java.io.IOException ioe){
            //Nothing
        }
        
        //On se renseigne sur le temps du média
        T=audioPlayer.getDuration().getSeconds();
    }
    
    public void prepareToPlay(){
        createNewSound(fichierSon.getAbsolutePath());
    }
    
    /** Ferme un son */
    public void deleteASound(){
        if(audioPlayer!=null){
            System.out.println("Bip, on passe");
            audioPlayer.deallocate();
            //audioPlayer.close();
        }
    }
    
    /** Joue un son en entier */
    public void playAllSound(){
        stopSound();//Arrêt préventif
        if (audioPlayer!=null){
            startTH();
            audioPlayer.setMediaTime(new Time(0.0));
            audioPlayer.start();
        }
    }
    
    /** Joue un son à partir de t */
    public void playSound(){
        stopSound();//Arrêt préventif
        if (audioPlayer!=null){
            startTH();
            audioPlayer.setMediaTime(new Time(t));
            audioPlayer.start();
        }
    }
    
    /** Joue un son de t1 à t2 */
    public void playSoundFrom(){
        stopSound();//Arrêt préventif
        if (audioPlayer!=null){
            if(tags.isSet()){
                audioPlayer.setMediaTime(new Time(pixelToTime(kx1)));
                audioPlayer.setStopTime(new Time(pixelToTime(kx2)));
            }else{
                audioPlayer.setMediaTime(new Time(t1));
                audioPlayer.setStopTime(new Time(t2));
            }            
            startTH();            
            audioPlayer.start();
        }
    }
    
    /** Joue un son de t1-0.5 à t1 */
    public void playSoundBeforeBegin(){
        stopSound();//Arrêt préventif
        if (audioPlayer!=null){
            if(tags.isSet()){
                audioPlayer.setMediaTime(new Time(pixelToTime(kx1)-0.5));
                audioPlayer.setStopTime(new Time(pixelToTime(kx1)));
            }else{
                audioPlayer.setMediaTime(new Time(t1-0.5));
                audioPlayer.setStopTime(new Time(t1));
            }
            startTH();            
            audioPlayer.start();
        }
    }
    /** Joue un son de t1 à t1+0.5 */
    public void playSoundAfterBegin(){
        stopSound();//Arrêt préventif
        if (audioPlayer!=null){
            if(tags.isSet()){
                audioPlayer.setMediaTime(new Time(pixelToTime(kx1)));
                audioPlayer.setStopTime(new Time(pixelToTime(kx1)+0.5));
            }else{
                audioPlayer.setMediaTime(new Time(t1));
                audioPlayer.setStopTime(new Time(t1+0.5));
            }
            startTH();            
            audioPlayer.start();
        }
    }
    /** Joue un son de t2-0.5 à t2 */
    public void playSoundBeforeEnd(){
        stopSound();//Arrêt préventif
        if (audioPlayer!=null){
            if(tags.isSet()){
                audioPlayer.setMediaTime(new Time(pixelToTime(kx2)-0.5));
                audioPlayer.setStopTime(new Time(pixelToTime(kx2)));
            }else{
                audioPlayer.setMediaTime(new Time(t2-0.5));
                audioPlayer.setStopTime(new Time(t2));
            }
            startTH();            
            audioPlayer.start();
        }
    }
    /** Joue un son de t2 à t2+0.5 */
    public void playSoundAfterEnd(){
        stopSound();//Arrêt préventif
        if (audioPlayer!=null){
            if(tags.isSet()){
                audioPlayer.setMediaTime(new Time(pixelToTime(kx2)));
                audioPlayer.setStopTime(new Time(pixelToTime(kx2)+0.5));
            }else{
                audioPlayer.setMediaTime(new Time(t2));
                audioPlayer.setStopTime(new Time(t2+0.5));
            }
            startTH();            
            audioPlayer.start();
        }
    }
    
    /** Arrête un son */
    public void stopSound(){
        if (audioPlayer!=null){
            stopTH();
            audioPlayer.stop();
        }
    }
    
    /** Retourne t1 sous la forme 0.00.00.00 */
    public String getStart(){
        return debut;
    }
    
    /** Retourne t2 sous la forme 0.00.00.00 */
    public String getEnd(){
        return fin;
    }
    
    /** La couleur de la forme d'onde */
    public void setGraphColor(Color c){
        cCourbe = c;
    }
    
    /** La couleur de la barre de progession sur la forme d'onde */
    public void setProgressColor(Color c){
        cCourant = c;
    }
    
    /** La couleur de la barre de début de zone sur la forme d'onde */
    public void setT1Color(Color c){
        cZone1 = c;
    }
    
    /** La couleur de la barre de fin de zone sur la forme d'onde */
    public void setT2Color(Color c){
        cZone2 = c;
    }
    
    /** Démarre la/les tâche/s */
    private void startTH() {
        audioThread = new Thread(this);
        audioThread.start();
    }
    
    /** Arrête la/les tâche/s */
    private void stopTH() {
        if (audioThread != null) {
            audioThread.interrupt();
        }
        audioThread = null;
    }
    
    /** Retourne le temps t sous la forme 0.00.00.00 du temps recherché */
    private String timeToTiming(double time){
        String timing = "";
        
        time=time*100; //Support des centièmes de secondes
        int hour = (int)time/360000;
        int min = ((int)time - 360000*hour)/6000;
        int sec = ((int)time - 360000*hour - 6000*min)/100;
        int cSec = (int)time - 360000*hour - 6000*min - 100*sec;
        
        String Smin, Ssec, Scent;
        if (min<10){Smin = "0"+min;}else{Smin = String.valueOf(min);}
        if (sec<10){Ssec = "0"+sec;}else{Ssec = String.valueOf(sec);}
        if (cSec<10){Scent = "0"+cSec;}else{Scent = String.valueOf(cSec);}
        
        timing = hour + "." + Smin + "." + Ssec + "." + Scent;
        return timing;
    }
    
    /** Retourne le temps t en secondes du pixel recherché */
    private double pixelToTime(int px){
        double ptx = 0.0; //nano = pixel*nano/pixel
        ptx = px*T/getWidth();
        
        return ptx;
    }
    
    /** Retourne le temps t sous la forme 0.00.00.00 du pixel recherché */
    private String pixelToTiming(int px){
        String timing = "";
        double ptx = 0.0; ptx = px*T/getWidth(); //secondes
        
        ptx=ptx*100; //Support des centièmes de secondes
        int hour = (int)ptx/360000;
        int min = ((int)ptx - 360000*hour)/6000;
        int sec = ((int)ptx - 360000*hour - 6000*min)/100;
        int cSec = (int)ptx - 360000*hour - 6000*min - 100*sec;
        
        String Smin, Ssec, Scent;
        if (min<10){Smin = "0"+min;}else{Smin = String.valueOf(min);}
        if (sec<10){Ssec = "0"+sec;}else{Ssec = String.valueOf(sec);}
        if (cSec<10){Scent = "0"+cSec;}else{Scent = String.valueOf(cSec);}
        
        timing = hour + "." + Smin + "." + Ssec + "." + Scent;
        return timing;
    }
    // </editor-fold>
    
    public void setImageFilePath(String image){
        fichierImage = new File(image);
    }
    
    public int getTotalPixels(){
        return totalPixels;
    }
    
    public void setDisplayZone(int displayZone){
        this.displayZone = displayZone;
    }
    
    @Override
    public void paint(Graphics g){
            
        //Efface le tout
        Graphics2D g2 = (Graphics2D) g;
        g2.setBackground(Color.white);
        g2.clearRect(0, 0, getWidth(), getHeight());
        
        //Dessine une forme d'onde
        if(arlFichierImage.isEmpty()==false){
            int count = 0;
            for(String s : arlFichierImage){
                //if(count-30000<displayZone && count+30000>displayZone){
                //    javax.swing.ImageIcon img2 = new javax.swing.ImageIcon(s);
                //    g2.drawImage(img2.getImage(),0+count,0,null);
                //}
                //count += 30000;
                //if(count-1000<displayZone && count+1000>displayZone){
                //    javax.swing.ImageIcon img2 = new javax.swing.ImageIcon(s);
                //    g2.drawImage(img2.getImage(),0+count,0,null);
                //}
                //count += 1000;
                javax.swing.ImageIcon img2 = new javax.swing.ImageIcon(s);
                g2.drawImage(img2.getImage(),0+count,0,null);
                count += 1000;
            }
        }
        
        //Définit une couleur blanche
        g.setColor(cCourant);
        //Dessine la barre de progression
        g.drawLine(x,0,x,this.getHeight());

        //Définit une couleur jaune
        g.setColor(cZone1);
        //Dessine le limiteur d'encadrement inférieure
        g.drawLine(x1,0,x1,this.getHeight());

        //Définit une couleur rouge
        g.setColor(cZone2);
        //Dessine le limiteur d'encadrement supérieure
        g.drawLine(x2,0,x2,this.getHeight());
        
        try{
            if(leftTag.isSet()==true && rightTag.isSet()==true){
                g.setColor(Color.orange);
                Composite originalComposite = g2.getComposite();
                g2.setComposite(makeComposite(alpha));
                g2.fillRect(leftTag.getX(), 0, rightTag.getX()-leftTag.getX(), this.getHeight());
                g2.setComposite(originalComposite);
            }
        }catch(NullPointerException npe){
            //NullPOinterException = no tag available at the laft or the right
            int xa = 0; int xb = 0;
            if(leftTag==null){ xa = kx1; }else{ xa = leftTag.getX(); }
            if(rightTag==null){ xb = kx2; }else{ xb = rightTag.getX(); }
            g.setColor(Color.orange);
            Composite originalComposite = g2.getComposite();
            g2.setComposite(makeComposite(alpha));
            g2.fillRect(xa, 0, xb-xa, this.getHeight());
            g2.setComposite(originalComposite);
        }catch(Exception exc){
            //Nothing to do; We are dead if we reach this area.
        }        
        
        if(tags.isSet()==true){
            for(int i=0;i<tags.getTags().size();i++){
                Tag tag = tags.getTags().get(i);
                if(currenttag.equals(tag)){
                    g.setColor(Color.blue);   
                }else{
                    g.setColor(Color.orange);                        
                }
                g.drawLine(tag.getX(),0,tag.getX(),this.getHeight());
                g.setColor(Color.green);
                g.drawString(tag.getSyllable(), tag.getX()-25, 10);
                g.setColor(cZone1);
                g.drawLine(tags.getStart(),0,tags.getStart(),this.getHeight());
                g.setColor(cZone2);
                g.drawLine(tags.getEnd(),0,tags.getEnd(),this.getHeight());
            }
        }
    }
    
    public void modifyTags(String sentence, int x1, int x2){
        tags = new Tags(sentence, x1, x2);
        this.x1 = x1; debut = pixelToTiming(x1); t1 = pixelToTime(x1);
        this.x2 = x2; fin = pixelToTiming(x2);   t2 = pixelToTime(x2);
        repaint();
    }
    
    public void modifyTags(String sentence){
        modifyTags(sentence, x1, x2);
    }
    
    // Gestion de la transparence
    private AlphaComposite makeComposite(float alpha) {
        int type = AlphaComposite.SRC_OVER;
        return(AlphaComposite.getInstance(type, alpha));
    }
    
    public String getKaraoke(){
        String sentence = "";
        if(tags.isSet()==true){
            double sDebut = pixelToTime(tags.getStart());
            double sCumul = 0d;
            for(int i=0;i<tags.getTags().size();i++){
                Tag tag = tags.getTags().get(i);
                double second = pixelToTime(tag.getX());
                //System.out.println("Seconds = "+second);
                double phase = second - sCumul - sDebut;
                double cSec = phase*100;
                sentence = sentence + "{\\k"+(int)cSec+"}"+tag.getSyllable();
                sCumul = second - sDebut;
            }
            //System.out.println("Karaoke = "+sentence);
        }
        return sentence;
    }
    
    public void modifyKaraokeTags(String sentence){
        Pattern p = Pattern.compile("(\\d+)\\}([^\\{]*)");
        Matcher m = p.matcher(sentence);
        
        List<String> syl = new ArrayList<String>();
        List<Integer> time = new ArrayList<Integer>();
        
        while(m.find()){
            time.add(Integer.parseInt(m.group(1)));
            syl.add(m.group(2));
        }        
        
        String new_sentence = "";
        List<Integer> pixel = new ArrayList<Integer>();
        int icumul = 0;
        
        for(int i : time){
            double d = i/100d;
            int pt  = timeToPixel(d, true);
            pixel.add(x1 + icumul + pt);
            System.out.println(x1 + icumul + pt);
            icumul += pt;
        }
        
        List<Tag> tag = new ArrayList<Tag>();
        
        for (int i=0;i<syl.size();i++){
            Tag t4g = new Tag(syl.get(i), pixel.get(i));
            tag.add(t4g);
            new_sentence += syl.get(i);
        }
        
        tags = new Tags(tag, new_sentence, x1, x2);
        debut = pixelToTiming(x1); t1 = pixelToTime(x1);
        fin = pixelToTiming(x2);   t2 = pixelToTime(x2);
        repaint();
    }
    
    public void resetTags(){
        tags = new Tags();
        currenttag = new Tag();
        leftTag = new Tag();
        rightTag = new Tag();
        repaint();
    }
    
    public int DoubleToInteger(double d){
        long result = Math.round(d);
        String s = Long.toString(result);
        int i = Integer.parseInt(s);
        return i;
    }
    
    @Override
    @SuppressWarnings("static-access")
    public void run() {
        while (audioThread != null) {
           if (audioPlayer!=null && audioPlayer.getState()!=audioPlayer.Realizing){
               progressMe();
           }
       }
    }
    
    public class wpMouseListener extends java.awt.event.MouseAdapter {
    
        protected WavePanel wp;

        /** Creates a new instance of controlPanelMouseListener */
        @SuppressWarnings("LeakingThisInConstructor")
        public wpMouseListener(WavePanel wp) {
            super();
            this.wp = wp;
            wp.addMouseListener(this);
        }

        /** Retourne le composant controlPanel en cours */
        public WavePanel getWavePanel(){
            return wp;
        }

        @Override
        public void mouseClicked(java.awt.event.MouseEvent e){
            if (javax.swing.SwingUtilities.isLeftMouseButton(e)){
                leftClickAction(e);
            }else if(javax.swing.SwingUtilities.isMiddleMouseButton(e)){
                middleClickAction(e);
            }else{
                rightClickAction(e);
            }
        }

        protected void rightClickAction(java.awt.event.MouseEvent e){            
            x2=e.getX();
            wp.repaint();
            fin = pixelToTiming(x2);
            t2 = pixelToTime(x2);
//            System.out.println("Valeurs -> x2 = "+x2+" t2 = "+t2+"\nFin = "+fin);
        }

        protected void middleClickAction(java.awt.event.MouseEvent e){
            x=e.getX();
            wp.repaint();
        }

        protected void leftClickAction(java.awt.event.MouseEvent e){
            if(tags.isSet()){
                Tag tag = tags.getLeftTag(e.getX());
                leftTag = tag;
                if(tag!=null){
                    kx1 = tag.getX();
                }else{
                    kx1 = x1;
                }
                tag = tags.getRightTag(e.getX());
                rightTag = tag;
                if(tag!=null){
                    kx2 = tag.getX();
                }else{
                    kx2 = tags.getLastTag().getX();
                }
                wp.repaint();
                System.out.println("lefttag - "+kx1+" / righttag - "+kx2);
            }else{
                x1=e.getX();
                wp.repaint();
                debut = pixelToTiming(x1);
                t1 = pixelToTime(x1);
//                System.out.println("Valeurs -> x1 = "+x1+" t1 = "+t1+"\nDébut = "+debut);
            }
        }
        
    }
}
