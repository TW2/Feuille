package feuille;

import feuille.dialog.SettingsDialog;
import feuille.io.Settings;
import feuille.module.drawing.AssSketchpad;
import feuille.module.editor.assa.ASS;
import feuille.module.editor.AssEditor;
import feuille.util.DialogResult;
import feuille.util.Exchange;
import feuille.util.Loader;
import feuille.util.Welcome;
import feuille.util.filefilter.ASSFileFilter;
import feuille.util.filefilter.AudioFileFilter;
import feuille.util.filefilter.MediaFileFilter;
import feuille.util.filefilter.SSAFileFilter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FeuilleFrame extends JFrame {

    public static Settings settings;

    private final Exchange exchange;

    private final JLayeredPane embedPanel;
    private final List<Component> components;

    private final Welcome welcome;
    private final AssSketchpad sketchpad;
    private final AssEditor assEditor;

    private final JMenuBar myMenuBar;
    private final JMenu welcomeMenu;
    private final JMenu sketchpadMenu;
    private final JMenu assEditorMenu;

    private String saveAssFile = null;

    public FeuilleFrame() throws HeadlessException {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1920, 1000);
        setTitle("Feuille v0.6");

        settings = Settings.loadSettings(this);

        exchange = new Exchange();

        exchange.setMainFrame(this);

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

        welcomeMenu = getWelcomeMenu();
        sketchpadMenu = getSketchpadMenu();
        assEditorMenu = getAssEditorMenu();

        myMenuBar = createMenu();
        setJMenuBar(myMenuBar);

        highlightMenu(welcomeMenu);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(embedPanel, BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                Settings.saveSettings(settings);
                exchange.getViewVLC().close();
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);

                welcome.forceResize(e.getComponent().getWidth(), e.getComponent().getHeight());
                sketchpad.forceResize(e.getComponent().getWidth(), e.getComponent().getHeight());
                assEditor.forceResize(e.getComponent().getWidth(), e.getComponent().getHeight());
            }
        });
    }

    private JMenuBar createMenu(){
        JMenuBar mnu = new JMenuBar();

        //------------------------------------------------------------------------

        JMenu mFile = new JMenu(Loader.language("menu.file", "File"));
        mnu.add(mFile);

        JMenu mFileMod = new JMenu(Loader.language("menu.module", "Modules"));
        mFile.add(mFileMod);

        //------------------------------------------------------------------------

        JMenuItem mShowWelcome = new JMenuItem(Loader.language("menu.welcome.mod", "Show Welcome"));
        mShowWelcome.addActionListener(this::mShowWelcomeActionPerformed);
        mFileMod.add(mShowWelcome);

        //------------------------------------------------------------------------

        JMenuItem mShowDraw = new JMenuItem(Loader.language("menu.draw.mod", "Show sketchpad"));
        mShowDraw.addActionListener(this::mShowDrawActionPerformed);
        mFileMod.add(mShowDraw);

        //------------------------------------------------------------------------

        JMenuItem mShowAssEditor = new JMenuItem(Loader.language("menu.editor.ass.mod", "Show ASS editor"));
        mShowAssEditor.addActionListener(this::mShowAssEditorActionPerformed);
        mFileMod.add(mShowAssEditor);

        //------------------------------------------------------------------------

        mFile.add(new JSeparator());

        JMenuItem mFileSettings = new JMenuItem(Loader.language("menu.settings", "Settings"));
        mFileSettings.addActionListener(this::mSettingsActionPerformed);
        mFile.add(mFileSettings);

        mFile.add(new JSeparator());

        JMenuItem mFileQuit = new JMenuItem(Loader.language("menu.quit", "Quit"));
        mFileQuit.addActionListener((e)-> {
            Settings.saveSettings(settings);
            System.exit(0);
        });
        mFile.add(mFileQuit);

        //------------------------------------------------------------------------

        return mnu;
    }

    private JMenu getWelcomeMenu() {
        JMenu mnuWelcome = new JMenu(Loader.language("menu.welcome", "Welcome"));

        JMenuItem mLicense = new JMenuItem(Loader.language("menu.welcome.lic", "Show license"));
        mLicense.addActionListener(this::mLicenseActionPerformed);
        mnuWelcome.add(mLicense);

        return(mnuWelcome);
    }

    private JMenu getSketchpadMenu() {
        return(new JMenu(Loader.language("menu.drawing", "Sketchpad")));
    }

    private JMenu getAssEditorMenu(){
        JMenu mFileModAssEditor = new JMenu(Loader.language("menu.editor.ass", "ASS Editor"));

        JMenuItem mNewASS = new JMenuItem(Loader.language("menu.editor.ass.nv.ass", "Start a new ASS"));
        mNewASS.addActionListener(this::mNewASSActionPerformed);
        mFileModAssEditor.add(mNewASS);

        JMenuItem mOpenASS = new JMenuItem(Loader.language("menu.editor.ass.op.ass", "Open an ASS file..."));
        mOpenASS.addActionListener(this::mOpenASSActionPerformed);
        mFileModAssEditor.add(mOpenASS);

        JMenuItem mSaveAsASS = new JMenuItem(Loader.language("menu.editor.ass.sv.cur.ass", "Save the current ASS..."));
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

        return mFileModAssEditor;
    }

    // Show welcome
    public void mShowWelcomeActionPerformed(ActionEvent event){
        setOnTop(welcome);
        highlightMenu(welcomeMenu);
    }

    // Show sketchpad
    public void mShowDrawActionPerformed(ActionEvent event){
        setOnTop(sketchpad);
        highlightMenu(sketchpadMenu);
    }

    // Show ass editor
    public void mShowAssEditorActionPerformed(ActionEvent event){
        setOnTop(assEditor);
        highlightMenu(assEditorMenu);
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

    private void highlightMenu(JMenu menu){
        for(Component component : myMenuBar.getComponents()){
            if(component.hashCode() == welcomeMenu.hashCode()){
                myMenuBar.remove(component);
            }else if(component.hashCode() == sketchpadMenu.hashCode()){
                myMenuBar.remove(component);
            }else if(component.hashCode() == assEditorMenu.hashCode()){
                myMenuBar.remove(component);
            }
        }

        myMenuBar.add(menu);
        myMenuBar.revalidate();
        myMenuBar.updateUI();
        myMenuBar.repaint();
    }

    public void mLicenseActionPerformed(ActionEvent event) {
        File file = new File(Objects.requireNonNull(
                FeuilleFrame.class.getResource("/help/GPLv3.html")).getPath());
        displayWelcomeDocument(file);
    }

    private void displayWelcomeDocument(File file){
        if(!file.exists()) return;
        try(BufferedReader br = new BufferedReader(new FileReader(file))){
            String line;
            StringBuilder document = new StringBuilder();
            while((line = br.readLine()) != null){
                document.append(line).append("\n");
            }
            welcome.setContent(document.toString());
        } catch (IOException e) {
            Loader.consoleErr(e.getMessage());
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
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new MediaFileFilter());
        int z = fileChooser.showOpenDialog(this);
        if(z == JFileChooser.APPROVE_OPTION){
            // Set audio waveform and spectrum
            exchange.getSyncPane().openMedia(
                    fileChooser.getSelectedFile().getPath(),
                    15_000L
            );
            // Set video
            exchange.getViewVLC().setMrl(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }

    public void mOpenAudioActionPerformed(ActionEvent event){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new AudioFileFilter());
        fileChooser.addChoosableFileFilter(new MediaFileFilter());
        int z = fileChooser.showOpenDialog(this);
        if(z == JFileChooser.APPROVE_OPTION){
            // Set audio waveform and spectrum
            exchange.getSyncPane().openMedia(
                    fileChooser.getSelectedFile().getPath(),
                    15_000L
            );
            // Set video
            exchange.getViewVLC().setMrl(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }

    public void mCloseVideoActionPerformed(ActionEvent event){

    }

    public void mCloseAudioActionPerformed(ActionEvent event){

    }

    public void mSettingsActionPerformed(ActionEvent event){
        SettingsDialog settingsDialog = new SettingsDialog();
        settingsDialog.setUserSettingsPath(settings.getUserSettingsPath());
        settingsDialog.setTheme(settings.getTheme());
        settingsDialog.setLanguage(settings.getLanguage());
        settingsDialog.showDialog(this);
        if(settingsDialog.getDialogResult() == DialogResult.OK){
            // TODO
            settings.setUserSettingsPath(settingsDialog.getUserSettingsPath());
            settings.setTheme(settingsDialog.getTheme());
            settings.setLanguage(settingsDialog.getLanguage());
        }
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        FeuilleFrame.settings = settings;
    }
}
