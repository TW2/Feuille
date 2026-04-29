package feuille;

import feuille.module.drawing.ui.AssSketchpad;
import feuille.util.Loader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class FeuilleFrame extends JFrame {

    private final JPanel embedPanel;
    private final AssSketchpad sketchpad;

    public FeuilleFrame() throws HeadlessException {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1920, 1000);
        setTitle("Feuille v0.6");

        embedPanel = new JPanel(new BorderLayout());
        sketchpad = new AssSketchpad();

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(embedPanel, BorderLayout.CENTER);

        setJMenuBar(createMenu());
    }

    private JMenuBar createMenu(){
        JMenuBar mnu = new JMenuBar();

        JMenu mFile = new JMenu(Loader.language("menu.file", "File"));
        mnu.add(mFile);

        JMenu mFileMod = new JMenu(Loader.language("menu.module", "Modules"));
        mFile.add(mFileMod);

        JMenuItem mShowDraw = new JMenuItem(Loader.language("menu.draw.mod", "Show sketchpad"));
        mShowDraw.addActionListener(this::mShowDrawActionPerformed);
        mFileMod.add(mShowDraw);

        JSeparator sepMod = new JSeparator();
        mFileMod.add(sepMod);

        JMenu mFileModDraw = new JMenu(Loader.language("menu.drawing", "Sketchpad"));
        mFileMod.add(mFileModDraw);

        JSeparator sepQuit = new JSeparator();
        mFile.add(sepQuit);

        JMenuItem mFileQuit = new JMenuItem(Loader.language("menu.quit", "Quit"));
        mFileQuit.addActionListener(e -> System.exit(0));
        mFile.add(mFileQuit);

        return mnu;
    }

    // Show sketchpad
    public void mShowDrawActionPerformed(ActionEvent event){
        embedPanel.removeAll();
        embedPanel.add(sketchpad, BorderLayout.CENTER);
        sketchpad.revalidate();
        sketchpad.getCanvas().repaint();
    }
}
