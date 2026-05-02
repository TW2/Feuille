package feuille;

import feuille.module.drawing.ui.AssSketchpad;
import feuille.module.editor.assa.ASS;
import feuille.module.editor.assa.ui.AssEditor;
import feuille.util.Exchange;
import feuille.util.Loader;
import feuille.util.Welcome;
import feuille.util.filefilter.ASSFileFilter;
import feuille.util.filefilter.AudioFileFilter;
import feuille.util.filefilter.MediaFileFilter;
import feuille.util.filefilter.SSAFileFilter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class FeuilleFrame extends JFrame {

    private final Exchange exchange;

    private final JLayeredPane embedPanel;
    private final List<Component> components;

    private final Welcome welcome;
    private final AssSketchpad sketchpad;
    private final AssEditor assEditor;

    private String saveAssFile = null;

    public FeuilleFrame() throws HeadlessException {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1920, 1000);
        setTitle("Feuille v0.6");

        exchange = new Exchange();

        embedPanel = new JLayeredPane();
        components = new ArrayList<>();

        // index 0 : 1
        welcome = new Welcome(exchange, 1920, 1000);
        embedPanel.add(welcome, 0);
        components.add(welcome);

        // index 1 : 2
        sketchpad = new AssSketchpad(exchange, 1920, 1000);
        embedPanel.add(sketchpad, 1);
        components.add(sketchpad);

        // index 2 : 3
        assEditor = new AssEditor(exchange, 1920, 1000);
        embedPanel.add(assEditor, 2);
        components.add(assEditor);

        setOnTop(welcome);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(embedPanel, BorderLayout.CENTER);

        setJMenuBar(createMenu());
    }

    private JMenuBar createMenu(){
        JMenuBar mnu = new JMenuBar();

        //------------------------------------------------------------------------

        JMenu mFile = new JMenu(Loader.language("menu.file", "File"));
        mnu.add(mFile);

        JMenu mFileMod = new JMenu(Loader.language("menu.module", "Modules"));
        mFile.add(mFileMod);

        //------------------------------------------------------------------------

        JMenu mFileModWelcome = new JMenu(Loader.language("menu.welcome", "Welcome"));
        mFileMod.add(mFileModWelcome);

        JMenuItem mShowWelcome = new JMenuItem(Loader.language("menu.welcome.mod", "Show Welcome"));
        mShowWelcome.addActionListener(this::mShowWelcomeActionPerformed);
        mFileModWelcome.add(mShowWelcome);

        mFileMod.add(new JSeparator());

        //------------------------------------------------------------------------

        JMenu mFileModDraw = new JMenu(Loader.language("menu.drawing", "Sketchpad"));
        mFileMod.add(mFileModDraw);

        JMenuItem mShowDraw = new JMenuItem(Loader.language("menu.draw.mod", "Show sketchpad"));
        mShowDraw.addActionListener(this::mShowDrawActionPerformed);
        mFileModDraw.add(mShowDraw);

        mFileMod.add(new JSeparator());

        //------------------------------------------------------------------------

        JMenu mFileModAssEditor = new JMenu(Loader.language("menu.editor.ass", "ASS Editor"));
        mFileMod.add(mFileModAssEditor);

        JMenuItem mShowAssEditor = new JMenuItem(Loader.language("menu.editor.ass.mod", "Show ASS editor"));
        mShowAssEditor.addActionListener(this::mShowAssEditorActionPerformed);
        mFileModAssEditor.add(mShowAssEditor);

        mFileModAssEditor.add(new JSeparator());

        JMenuItem mNewASS = new JMenuItem(Loader.language("menu.editor.ass.nv.ass", "Start a new ASS"));
        mNewASS.addActionListener(this::mNewASSActionPerformed);
        mFileModAssEditor.add(mNewASS);

        JMenuItem mOpenASS = new JMenuItem(Loader.language("menu.editor.ass.op.ass", "Open an ASS file..."));
        mOpenASS.addActionListener(this::mOpenASSActionPerformed);
        mFileModAssEditor.add(mOpenASS);

        JMenuItem mSaveAsASS = new JMenuItem(Loader.language("menu.editor.ass.sv.ass", "Save the current ASS..."));
        mSaveAsASS.addActionListener(this::mSaveAsASSActionPerformed);
        mFileModAssEditor.add(mSaveAsASS);

        JMenuItem mSaveASS = new JMenuItem(Loader.language("menu.editor.ass.sv.ass", "Save ASS"));
        mSaveASS.addActionListener(this::mSaveASSActionPerformed);
        mFileModAssEditor.add(mSaveASS);

        JMenuItem mCloseASS = new JMenuItem(Loader.language("menu.editor.ass.cl.ass", "Close ASS"));
        mCloseASS.addActionListener(this::mCloseASSActionPerformed);
        mFileModAssEditor.add(mCloseASS);

        mFileModAssEditor.add(new JSeparator());

        JCheckBoxMenuItem mAutoSaveASS = new JCheckBoxMenuItem(Loader.language("menu.editor.ass.asv.ass", "Autosave ASS"), false);
        mAutoSaveASS.addActionListener(this::mAutoSaveASSActionPerformed);
        mFileModAssEditor.add(mAutoSaveASS);

        mFileModAssEditor.add(new JSeparator());

        JMenuItem mOpenVideo = new JMenuItem(Loader.language("menu.editor.ass.op.video", "Open video..."));
        mOpenVideo.addActionListener(this::mOpenVideoActionPerformed);
        mFileModAssEditor.add(mOpenVideo);

        JMenuItem mOpenAudio = new JMenuItem(Loader.language("menu.editor.ass.op.audio", "Open audio..."));
        mOpenAudio.addActionListener(this::mOpenAudioActionPerformed);
        mFileModAssEditor.add(mOpenAudio);

        mFileModAssEditor.add(new JSeparator());

        JMenuItem mCloseVideo = new JMenuItem(Loader.language("menu.editor.ass.cl.video", "Close video"));
        mCloseVideo.addActionListener(this::mCloseVideoActionPerformed);
        mFileModAssEditor.add(mCloseVideo);

        JMenuItem mCloseAudio = new JMenuItem(Loader.language("menu.editor.ass.cl.audio", "Close audio"));
        mCloseAudio.addActionListener(this::mCloseAudioActionPerformed);
        mFileModAssEditor.add(mCloseAudio);

//        mFileMod.add(new JSeparator());

        //------------------------------------------------------------------------

        mFile.add(new JSeparator());

        JMenuItem mFileQuit = new JMenuItem(Loader.language("menu.quit", "Quit"));
        mFileQuit.addActionListener(e -> System.exit(0));
        mFile.add(mFileQuit);

        //------------------------------------------------------------------------

        return mnu;
    }

    // Show welcome
    public void mShowWelcomeActionPerformed(ActionEvent event){
        setOnTop(welcome);
    }

    // Show sketchpad
    public void mShowDrawActionPerformed(ActionEvent event){
        setOnTop(sketchpad);
    }

    // Show ass editor
    public void mShowAssEditorActionPerformed(ActionEvent event){
        setOnTop(assEditor);
    }

    private void setOnTop(Component c){
        int z = 1;
        for(Component component : components){
            if(component.hashCode() == c.hashCode()){
                embedPanel.setPosition(component, 0);
                component.setVisible(true);
            }else{
                embedPanel.setPosition(component, z);
                component.setVisible(false);
                z++;
            }
        }
    }

    public void mNewASSActionPerformed(ActionEvent event){
        exchange.clearEvents();
    }

    public void mOpenASSActionPerformed(ActionEvent event){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new ASSFileFilter());
        fileChooser.addChoosableFileFilter(new SSAFileFilter());
        int z = fileChooser.showOpenDialog(this);
        if(z == JFileChooser.APPROVE_OPTION){
            exchange.openASS(ASS.read(fileChooser.getSelectedFile().getPath()));
        }
    }

    public void mSaveAsASSActionPerformed(ActionEvent event){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new ASSFileFilter());
        fileChooser.addChoosableFileFilter(new SSAFileFilter());
        int z = fileChooser.showSaveDialog(this);
        if(z == JFileChooser.APPROVE_OPTION){
            String filepath = fileChooser.getSelectedFile().getPath();
            if(!filepath.endsWith(".ssa") && !filepath.endsWith(".ass")){
                filepath = filepath.concat(".ass");
            }
            exchange.saveAsASS(filepath);
            saveAssFile = filepath;
        }
    }

    public void mSaveASSActionPerformed(ActionEvent event){
        if(saveAssFile != null){
            exchange.saveAsASS(saveAssFile);
        }else{
            mSaveAsASSActionPerformed(event);
        }
    }

    public void mCloseASSActionPerformed(ActionEvent event){
        exchange.clearEvents();
    }

    public void mAutoSaveASSActionPerformed(ActionEvent event){

    }

    public void mOpenVideoActionPerformed(ActionEvent event){

    }

    public void mOpenAudioActionPerformed(ActionEvent event){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new AudioFileFilter());
        fileChooser.addChoosableFileFilter(new MediaFileFilter());
        int z = fileChooser.showOpenDialog(this);
        if(z == JFileChooser.APPROVE_OPTION){
            exchange.getSyncPane().openMedia(
                    fileChooser.getSelectedFile().getPath(),
                    15_000L
            );
            exchange.getMpeg().setMedia(fileChooser.getSelectedFile().getPath());
        }
    }

    public void mCloseVideoActionPerformed(ActionEvent event){

    }

    public void mCloseAudioActionPerformed(ActionEvent event){

    }
}
