package org.wingate.feuille;

import com.formdev.flatlaf.FlatLightLaf;
import org.wingate.feuille.lib.TablePanel;
import org.wingate.feuille.util.ISO_3166;
import org.wingate.feuille.util.Load;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author util2
 */
public class Feuille {

    private static final String VERSION = "0.0.1_2024-10";
    private static final int WIDTH = 1900;
    private static final int HEIGHT = 1000;
    private ISO_3166 iso;
    private String NAME;
    private String EARTH;
    private String WARS;

    private JPanel mainPanel;
    private TablePanel tablePanel;

    public static void main(String[] args) {
        ISO_3166 iso = null;

        if(args.length > 0){
            for(int i=0; i<args.length; i++){
                switch(args[i]){
                    case "-v", "--version" -> {
                        System.out.println("Version is " + VERSION);
                        System.out.println("To obtain a list of commands, type: -h or --help");
                        System.exit(0);
                    }
                    case "-h", "--help" -> {
                        System.out.println("Version is " + VERSION);
                        System.out.println("Available commands:");
                        System.out.println("""
                            -h, --help          Show the help
                            -v, --version       Show the Feuille release version
                            -lang <alpha3>      Force a language with ISO-3166-1 alpha 3 country code, example: -lang usa
                            """);
                        System.exit(0);
                    }
                    case "-lang" -> {
                        if(args.length > i + 1) {
                            try{
                                iso = ISO_3166.getISO_3166(args[i + 1]);
                            }catch(Exception _){ }
                        }
                    }
                }
            }
        }

        FlatLightLaf.setup();
        new Feuille(iso);
    }



    public Feuille(ISO_3166 iso){
        EventQueue.invokeLater(()->{
            // Arguments options
            this.iso = iso;
            NAME = Load.language("app_name", "Feuille", iso);
            EARTH = Load.language("msg_reforest", "Reforest Earth now", iso);
            WARS = Load.language("msg_wars", "And stop wars!", iso);

            // Start loading...
            System.out.printf("<<< %s >>>%n", NAME);
            System.out.printf("%s is starting...%n", NAME);

            // Prepare panels
            mainPanel = new JPanel();
            mainPanel.setLayout(new BorderLayout());
            tablePanel = new TablePanel(WIDTH, HEIGHT, iso);

            // Prepare main application
            JFrame frm = makeFeuille();

            System.out.printf("%s has started!%n", NAME);
        });
    }

    private JFrame makeFeuille(){
        JFrame frm = new JFrame();
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frm.setTitle(String.format("%s :: %s :: %s",NAME, EARTH, WARS));
        frm.setSize(WIDTH, HEIGHT);
        frm.setLocationRelativeTo(null);

        JMenuBar menu = new JMenuBar();
        menu.add(makeFileMenu());
        frm.setJMenuBar(menu);
        frm.getContentPane().setLayout(new BorderLayout());
        frm.getContentPane().add(mainPanel, BorderLayout.CENTER);

        frm.setVisible(true);

        return frm;
    }

    private JMenu makeFileMenu() {
        if(iso != null) System.out.println(iso.getCountry());
        JMenu mnuFile = new JMenu(Load.language("mnuFile", "File", iso));

        JMenu mFileTable = new JMenu(Load.language("mFileTable", "Table", iso));
        mnuFile.add(mFileTable);
        JMenuItem mFileTableSwitch = new JMenuItem(Load.language("mFileTableSwitch", "View yggdrasil", iso));
        mFileTableSwitch.setIcon(Load.fromResource("/org/wingate/feuille/16 losange carre.png"));
        mFileTableSwitch.addActionListener(e -> {mainPanel.removeAll(); mainPanel.add(tablePanel, BorderLayout.CENTER); tablePanel.updateUI(); tablePanel.refreshDividers();});
        mFileTable.add(mFileTableSwitch);

        JMenuItem mFileQuit = new JMenuItem(Load.language("mFileQuit", "Quit", iso));
        mFileQuit.setIcon(Load.fromResource("/org/wingate/feuille/16 cross-small.png"));
        mFileQuit.addActionListener(e -> System.exit(0));
        mnuFile.add(mFileQuit);

        return mnuFile;
    }
}
