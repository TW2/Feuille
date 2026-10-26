package feuille.dialog;

import feuille.util.DialogResult;
import feuille.util.Exchange;
import feuille.util.Loader;
import feuille.util.assa.AssActorType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AssActorTypeDialog extends JDialog {

    private final Exchange exchange;
    private DialogResult dialogResult;


    private final JRadioButton rbBoy;
    private final JRadioButton rbGirl;
    private final JRadioButton rbMan;
    private final JRadioButton rbWoman;
    private final JRadioButton rbNarrator;
    private final JRadioButton rbRobot;
    private final JRadioButton rbUnknown;

    public AssActorTypeDialog(Exchange exchange){
        super(exchange.getMainFrame());
        this.exchange = exchange;

        setTitle("Actor Kind");
        setModal(true);
        setResizable(false);
        setSize(190, 260);

        dialogResult = DialogResult.None;

        JPanel contentPane = new JPanel(new BorderLayout());

        JButton btnOK = new JButton(Loader.language("dialog.ok", "OK"));
        btnOK.addActionListener(this::btnOKActionPerformed);
        JButton btnCancel = new JButton(Loader.language("dialog.cancel", "Cancel"));
        btnCancel.addActionListener(this::btnCancelActionPerformed);

        setContentPane(contentPane);
        getRootPane().setDefaultButton(btnOK);

        JPanel actorKindPanel = new JPanel((new GridLayout(7, 1)));
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
        actorKindPanel.add(rbBoy);
        actorKindPanel.add(rbGirl);
        actorKindPanel.add(rbMan);
        actorKindPanel.add(rbWoman);
        actorKindPanel.add(rbNarrator);
        actorKindPanel.add(rbRobot);
        actorKindPanel.add(rbUnknown);

        JPanel resultPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        resultPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        resultPanel.add(btnOK);
        resultPanel.add(btnCancel);

        contentPane.add(actorKindPanel, BorderLayout.CENTER);
        contentPane.add(resultPanel, BorderLayout.SOUTH);
    }

    public void showDialog() {
        setLocationRelativeTo(exchange.getMainFrame());
        setVisible(true);
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

    public AssActorType getActorType() {
        AssActorType at = null;
        if(rbBoy.isSelected()) at = AssActorType.Boy;
        if(rbGirl.isSelected()) at = AssActorType.Girl;
        if(rbMan.isSelected()) at = AssActorType.Man;
        if(rbWoman.isSelected()) at = AssActorType.Woman;
        if(rbNarrator.isSelected()) at = AssActorType.Narrator;
        if(rbRobot.isSelected()) at = AssActorType.Robot;
        if(rbUnknown.isSelected()) at = AssActorType.Unknown;
        return at;
    }
}
