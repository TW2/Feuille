package feuille.dialog;

import feuille.io.Settings;
import feuille.io.assa.AssActor;
import feuille.io.assa.AssStyle;
import feuille.module.editor.voyager.PlaceholderTextField;
import feuille.util.DialogResult;
import feuille.util.Exchange;
import feuille.util.Loader;
import feuille.io.assa.AssActorType;
import feuille.io.assa.AssColor;
import feuille.util.renderer.AssActorRenderer;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AssActorsDialog extends JDialog {

    private final Exchange exchange;

    private Settings settings;
    private AssStyle currentStyle;
    private DialogResult dialogResult;

    //=============================================
    private final DefaultComboBoxModel<AssActor> actorsModel;
    private final JComboBox<AssActor> cbActors;
    private final JRadioButton rbBoy;
    private final JRadioButton rbGirl;
    private final JRadioButton rbMan;
    private final JRadioButton rbWoman;
    private final JRadioButton rbNarrator;
    private final JRadioButton rbRobot;
    private final JRadioButton rbUnknown;
    private final JLabel lblImage;
    private final JLabel lblColor;
    private final PlaceholderTextField tfName;

    public AssActorsDialog(Exchange exchange){
        super(exchange.getMainFrame());
        this.exchange = exchange;
        settings = null;
        currentStyle = new AssStyle();
        setTitle("Actors Settings");
        setModal(true);
        setResizable(false);
        setSize(640, 500);

        dialogResult = DialogResult.None;

        JPanel contentPane = new JPanel(new BorderLayout());

        JButton btnOK = new JButton(Loader.language("dialog.ok", "OK"));
        btnOK.addActionListener(this::btnOKActionPerformed);
        JButton btnCancel = new JButton(Loader.language("dialog.cancel", "Cancel"));
        btnCancel.addActionListener(this::btnCancelActionPerformed);

        setContentPane(contentPane);
        getRootPane().setDefaultButton(btnOK);

        actorsModel = new DefaultComboBoxModel<>();
        cbActors = new JComboBox<>(actorsModel);
        cbActors.setRenderer(new AssActorRenderer());
        cbActors.addActionListener(this::cbActorsActionPerformed);
        JPanel actorKindPanel = new JPanel((new GridLayout(1, 7)));
        lblImage = new JLabel();
        lblImage.setHorizontalAlignment(JLabel.CENTER);
        lblImage.setPreferredSize(new Dimension(256, 256));
        JButton btnChangeImage = new JButton(Loader.language("actor.change.image", "Change image"));
        btnChangeImage.addActionListener(_ -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setDialogType(JFileChooser.OPEN_DIALOG);
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.addChoosableFileFilter(new FileFilter() {
                final String[] extensions = {"jpg", "png", "gif", "bmp"};

                @Override
                public boolean accept(File f) {
                    if(f.isDirectory()) return true;
                    for(String ext : extensions){
                        if(f.getName().endsWith("."+ext)) return true;
                    }
                    return false;
                }

                @Override
                public String getDescription() {
                    return Loader.language("ext.jpg.png.gif.bmp", "Images (*.jpg, *.png, *.gif, *.bmp)");
                }
            });
            int z = chooser.showOpenDialog(exchange.getMainFrame());
            if (z == JFileChooser.APPROVE_OPTION) {
                if(cbActors.getSelectedItem() instanceof AssActor v){
                    try {
                        BufferedImage image = ImageIO.read(chooser.getSelectedFile());
                        if(image != null){
                            if(image.getWidth() > 256 || image.getHeight() > 256){
                                double ratioX = 256 / (double) image.getWidth();
                                double ratioY = 256 / (double) image.getHeight();
                                double ratio = Math.min(ratioX, ratioY);
                                int w = (int) (image.getWidth() * ratio);
                                int h = (int) (image.getHeight() * ratio);
                                int x = (256 - w) / 2;
                                int y = (256 - h) / 2;
                                BufferedImage scaledImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                                Graphics2D g = (Graphics2D) scaledImage.getGraphics();
                                g.drawImage(image, x, y, w, h, null);
                                g.dispose();
                                v.setImage(scaledImage);
                                lblImage.setIcon(new ImageIcon(scaledImage));
                            }else{
                                v.setImage(image);
                                lblImage.setIcon(new ImageIcon(image));
                            }
                        }
                    } catch (IOException e) {
                        Loader.dialogErr(e.getMessage());
                    }
                }
            }
        });
        JPanel namePanel = new JPanel(new BorderLayout());
        JLabel lblName = new JLabel(Loader.language("actor.chosen.name", "Name: "));
        tfName = new PlaceholderTextField();
        tfName.setPreferredSize(new Dimension(150, 26));
        tfName.setPlaceholder("Actor name or Name");
        JButton btnChangeName = new JButton(Loader.language("actor.change.name", "Change"));
        btnChangeName.addActionListener((_)->{
            if(cbActors.getSelectedItem() instanceof AssActor v){
                v.setName(tfName.getText());
                cbActors.updateUI();
            }
        });
        lblColor = new JLabel();
        lblColor.setHorizontalAlignment(JLabel.CENTER);
        lblColor.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        lblColor.setPreferredSize(new Dimension(256, 26));
        lblColor.setOpaque(true);
        lblColor.setBackground(Color.black);
        lblColor.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if(cbActors.getSelectedItem() instanceof AssActor v){
                    Color color = JColorChooser.showDialog(exchange.getMainFrame(), "Choose a color", lblColor.getBackground());
                    if(color != null){
                        lblColor.setBackground(color);
                        v.setColor(new AssColor(color));
                        cbActors.updateUI();
                    }
                }
            }
        });
        JPanel controlsPanel = new JPanel(new GridLayout(1, 2));
        JButton btnAddActor = new JButton(Loader.language("actor.add", "Add actor"));
        JButton btnRemoveActor = new JButton(Loader.language("actor.remove", "Remove actor"));
        controlsPanel.add(btnAddActor);
        controlsPanel.add(btnRemoveActor);

        rbBoy = new JRadioButton(Loader.language("actor.boy", "Boy"));
        rbGirl = new JRadioButton(Loader.language("actor.girl", "Girl"));
        rbMan = new JRadioButton(Loader.language("actor.man", "Man"));
        rbWoman = new JRadioButton(Loader.language("actor.woman", "Woman"));
        rbNarrator = new JRadioButton(Loader.language("actor.narrator", "Narrator"));
        rbRobot = new JRadioButton(Loader.language("actor.robot", "Robot"));
        rbUnknown = new JRadioButton(Loader.language("actor.unknown", "Unknown"));
        ButtonGroup bgActorKind = new ButtonGroup();
        bgActorKind.add(rbBoy);
        bgActorKind.add(rbGirl);
        bgActorKind.add(rbMan);
        bgActorKind.add(rbWoman);
        bgActorKind.add(rbNarrator);
        bgActorKind.add(rbRobot);
        bgActorKind.add(rbUnknown);
        rbUnknown.setSelected(true);
        rbBoy.addActionListener(this::rbBoyActionPerformed);
        rbGirl.addActionListener(this::rbGirlActionPerformed);
        rbMan.addActionListener(this::rbManActionPerformed);
        rbWoman.addActionListener(this::rbWomanActionPerformed);
        rbNarrator.addActionListener(this::rbNarratorActionPerformed);
        rbRobot.addActionListener(this::rbRobotActionPerformed);
        rbUnknown.addActionListener(this::rbUnknownActionPerformed);
        actorKindPanel.add(rbBoy);
        actorKindPanel.add(rbGirl);
        actorKindPanel.add(rbMan);
        actorKindPanel.add(rbWoman);
        actorKindPanel.add(rbNarrator);
        actorKindPanel.add(rbRobot);
        actorKindPanel.add(rbUnknown);

        namePanel.add(lblName, BorderLayout.WEST);
        namePanel.add(tfName, BorderLayout.CENTER);
        namePanel.add(btnChangeName, BorderLayout.EAST);

        btnAddActor.addActionListener((_)->{
            AssActor newActor = new AssActor();
            String name = JOptionPane.showInputDialog(Loader.language("actor.name.input", "Choose a name for your actor: "));
            newActor.setName(name);

            AssActorTypeDialog at = new AssActorTypeDialog(exchange);
            at.showDialog();
            if(at.getDialogResult() == DialogResult.OK){
                newActor.setAssActorType(at.getActorType());
            }else{
                newActor.setAssActorType(AssActorType.Unknown);
            }

            Color color = JColorChooser.showDialog(exchange.getMainFrame(), "Choose a color for your actor", null);
            newActor.setColor(new AssColor(color == null ? Color.black : color));
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setDialogType(JFileChooser.OPEN_DIALOG);
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.addChoosableFileFilter(new FileFilter() {
                final String[] extensions = {"jpg", "png", "gif", "bmp"};

                @Override
                public boolean accept(File f) {
                    if(f.isDirectory()) return true;
                    for(String ext : extensions){
                        if(f.getName().endsWith("."+ext)) return true;
                    }
                    return false;
                }

                @Override
                public String getDescription() {
                    return Loader.language("ext.jpg.png.gif.bmp", "Images (*.jpg, *.png, *.gif, *.bmp)");
                }
            });
            int z = chooser.showOpenDialog(exchange.getMainFrame());
            if(z == JFileChooser.APPROVE_OPTION){
                try {
                    BufferedImage image = ImageIO.read(chooser.getSelectedFile());
                    if(image != null){
                        if(image.getWidth() > 256 || image.getHeight() > 256){
                            double ratioX = 256 / (double) image.getWidth();
                            double ratioY = 256 / (double) image.getHeight();
                            double ratio = Math.min(ratioX, ratioY);
                            int w = (int) (image.getWidth() * ratio);
                            int h = (int) (image.getHeight() * ratio);
                            int x = (256 - w) / 2;
                            int y = (256 - h) / 2;
                            BufferedImage scaledImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                            Graphics2D g = (Graphics2D) scaledImage.getGraphics();
                            g.drawImage(image, x, y, w, h, null);
                            g.dispose();
                            newActor.setImage(scaledImage);
                        }else{
                            newActor.setImage(image);
                        }
                    }
                } catch (IOException e) {
                    Loader.dialogErr(e.getMessage());
                }
            }
            actorsModel.addElement(newActor);
            int index = -1;
            for(int i=0; i<actorsModel.getSize(); i++){
                if(cbActors.getItemAt(i).equals(newActor)){
                    index = i;
                    break;
                }
            }
            if(index != -1){
                cbActors.setSelectedIndex(index);
            }
        });
        btnRemoveActor.addActionListener((_)->{
            if(cbActors.getSelectedIndex() == -1) return;
            int index = cbActors.getSelectedIndex();
            cbActors.removeItemAt(index);
            if(cbActors.getItemCount() != 0){
                cbActors.setSelectedIndex(0);
                cbActors.updateUI();
            }
            if(cbActors.getItemCount() == 0){
                AssActor def = new AssActor();
                cbActors.addItem(def);
                cbActors.setSelectedItem(def);
                cbActors.updateUI();
            }
        });

        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.add(lblImage, BorderLayout.CENTER);

        JPanel changeImagePanel = new JPanel(new BorderLayout());
        changeImagePanel.add(btnChangeImage, BorderLayout.CENTER);

        JPanel colorPanel = new JPanel(new BorderLayout());
        colorPanel.add(lblColor, BorderLayout.CENTER);

        JPanel actorsPanel = new JPanel();
        actorsPanel.setLayout(new BoxLayout(actorsPanel, BoxLayout.PAGE_AXIS));
        actorsPanel.add(cbActors);
        actorsPanel.add(actorKindPanel);
        actorsPanel.add(imagePanel);
        actorsPanel.add(changeImagePanel);
        actorsPanel.add(colorPanel);
        actorsPanel.add(namePanel);
        actorsPanel.add(controlsPanel);

        JPanel resultPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        resultPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        resultPanel.add(btnOK);
        resultPanel.add(btnCancel);

        contentPane.add(actorsPanel, BorderLayout.CENTER);
        contentPane.add(resultPanel, BorderLayout.SOUTH);

    }

    public void showDialog(Settings settings) {
        if(settings == null) return;
        this.settings = settings;
        setLocationRelativeTo(exchange.getMainFrame());
        setVisible(true);
    }

    public Settings getSettings() {
        return settings;
    }

    public AssStyle getCurrentStyle() {
        return currentStyle;
    }

    public void setCurrentStyle(AssStyle currentStyle) {
        this.currentStyle = currentStyle;
    }

    public void setActorsList(List<AssActor> actors){
        actorsModel.addAll(actors);
    }

    public List<AssActor> getActorsList(){
        List<AssActor> list = new ArrayList<>();
        for(int i=0; i<actorsModel.getSize(); i++){
            list.add(actorsModel.getElementAt(i));
        }
        return list;
    }

    public void selectActor(AssActor actor) {
        int index = -1;
        for(int i=0; i<actorsModel.getSize(); i++){
            if(actorsModel.getElementAt(i).equals(actor)){
                index = i;
                break;
            }
        }
        cbActors.setSelectedIndex(index != -1 ? index : 0);
        if(cbActors.getSelectedIndex() != -1){
            switch(actor.getAssActorType()){
                case Boy -> rbBoy.setSelected(true);
                case Girl -> rbGirl.setSelected(true);
                case Man -> rbMan.setSelected(true);
                case Woman -> rbWoman.setSelected(true);
                case Robot -> rbRobot.setSelected(true);
                case Narrator -> rbNarrator.setSelected(true);
                default -> rbUnknown.setSelected(true);
            }
        }
        tfName.setText(actor.getName());
        lblColor.setBackground(actor.getColor().getColor());
        try{
            lblImage.setIcon(new ImageIcon(actor.getImage()));
        }catch(Exception _){}
    }

    public DialogResult getDialogResult() {
        return dialogResult;
    }

    public void btnOKActionPerformed(ActionEvent e) {
        dialogResult = DialogResult.OK;
        setVisible(false);
        dispose();
    }

    public void btnCancelActionPerformed(ActionEvent e) {
        dialogResult = DialogResult.Cancel;
        setVisible(false);
        dispose();
    }

    public void cbActorsActionPerformed(ActionEvent e) {
        if(cbActors.getSelectedItem() instanceof AssActor v){
            try{
                lblImage.setIcon(new ImageIcon(v.getImage()));
            }catch(Exception _){
                lblImage.setIcon(null);
            }
            tfName.setText(v.getName());
            lblColor.setBackground(v.getColor().getColor());
            switch(v.getAssActorType()){
                case Boy -> rbBoy.setSelected(true);
                case Girl -> rbGirl.setSelected(true);
                case Man -> rbMan.setSelected(true);
                case Woman -> rbWoman.setSelected(true);
                case Robot -> rbRobot.setSelected(true);
                case Narrator -> rbNarrator.setSelected(true);
                default -> rbUnknown.setSelected(true);
            }
        }
    }

    public void rbBoyActionPerformed(ActionEvent e) {
        if(cbActors.getSelectedItem() instanceof AssActor v){
            v.setAssActorType(AssActorType.Boy);
            cbActors.updateUI();
        }
    }

    public void rbGirlActionPerformed(ActionEvent e) {
        if(cbActors.getSelectedItem() instanceof AssActor v){
            v.setAssActorType(AssActorType.Girl);
            cbActors.updateUI();
        }
    }

    public void rbManActionPerformed(ActionEvent e) {
        if(cbActors.getSelectedItem() instanceof AssActor v){
            v.setAssActorType(AssActorType.Man);
            cbActors.updateUI();
        }
    }

    public void rbWomanActionPerformed(ActionEvent e) {
        if(cbActors.getSelectedItem() instanceof AssActor v){
            v.setAssActorType(AssActorType.Woman);
            cbActors.updateUI();
        }
    }

    public void rbNarratorActionPerformed(ActionEvent e) {
        if(cbActors.getSelectedItem() instanceof AssActor v){
            v.setAssActorType(AssActorType.Narrator);
            cbActors.updateUI();
        }
    }

    public void rbRobotActionPerformed(ActionEvent e) {
        if(cbActors.getSelectedItem() instanceof AssActor v){
            v.setAssActorType(AssActorType.Robot);
            cbActors.updateUI();
        }
    }

    public void rbUnknownActionPerformed(ActionEvent e) {
        if(cbActors.getSelectedItem() instanceof AssActor v){
            v.setAssActorType(AssActorType.Unknown);
            cbActors.updateUI();
        }
    }
}
