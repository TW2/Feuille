package org.wingate.feuille.ctrl;

import org.wingate.feuille.dialog.idea.VersionDetailsDialog;
import org.wingate.feuille.subs.ass.AssTranslateTo;
import org.wingate.feuille.util.DialogResult;
import org.wingate.feuille.util.ISO_3166;
import org.wingate.feuille.util.Load;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;

public class FlagButtons extends JPanel {

    private AssTranslateTo translations;

    private JTextPane paneSrc;
    private JTextPane paneDst;

    private final JLabel lblSrc;
    private final JButton btnSrcVersionDetails;
    private final JButton btnSrcLock;
    private ISO_3166 src;

    private final JLabel lblDst;
    private final JButton btnDstVersionDetails;
    private final JButton btnDstLock;
    private ISO_3166 dst;

    private final ImageIcon linkOK;
    private final ImageIcon linkNOT;

    public FlagButtons(ISO_3166 iso){
        linkOK = Load.fromResource("/org/wingate/feuille/thin-linkOK.png");
        linkNOT = Load.fromResource("/org/wingate/feuille/thin-linkNOT.png");

        src = ISO_3166.getISO_3166(Locale.getDefault().getISO3Country());
        dst = ISO_3166.getISO_3166(Locale.getDefault().getISO3Country());

        lblSrc = new JLabel(Load.fromResource("/org/wingate/feuille/util/" + src.getAlpha2() + ".gif"));
        lblDst = new JLabel(Load.fromResource("/org/wingate/feuille/util/" + dst.getAlpha2() + ".gif"));

        btnSrcVersionDetails = new JButton(Load.fromResource("/org/wingate/feuille/thin-present-red.png"));
        btnDstVersionDetails = new JButton(Load.fromResource("/org/wingate/feuille/thin-present-green.png"));

        btnSrcLock = new JButton(linkNOT);
        btnDstLock = new JButton(linkNOT);

        paneSrc = null;
        paneDst = null;

        JPanel panSrc = new JPanel(new BorderLayout());
        JPanel panDst = new JPanel(new BorderLayout());

        setLayout(new GridLayout(1,2, 2,0));
        add(panSrc);
        add(panDst);

        panSrc.add(btnSrcVersionDetails, BorderLayout.WEST);
        panSrc.add(lblSrc, BorderLayout.CENTER);
        panSrc.add(btnSrcLock, BorderLayout.EAST);

        panDst.add(btnDstVersionDetails, BorderLayout.WEST);
        panDst.add(lblDst, BorderLayout.CENTER);
        panDst.add(btnDstLock, BorderLayout.EAST);

        btnSrcLock.addActionListener(e -> {
            boolean b = btnSrcLock.getIcon().hashCode() == linkOK.hashCode();
            lockUnlock(!b, paneSrc, btnSrcVersionDetails, lblSrc);
            btnSrcLock.setIcon(b ? linkNOT : linkOK);
        });

        btnDstLock.addActionListener(e -> {
            boolean b = btnDstLock.getIcon().hashCode() == linkOK.hashCode();
            lockUnlock(!b, paneDst, btnDstVersionDetails, lblDst);
            btnDstLock.setIcon(b ? linkNOT : linkOK);
        });

        btnSrcVersionDetails.addActionListener(e -> {
            VersionDetailsDialog dialog = new VersionDetailsDialog(new java.awt.Frame(), true, iso);
            dialog.lockDst();
            dialog.showDialog(src, dst, translations);
            if(dialog.getDialogResult() == DialogResult.OK){
                translations = dialog.getTranslations();
                src = dialog.getSrc();
                lblSrc.setIcon(Load.fromResource("/org/wingate/feuille/util/" + src.getAlpha2() + ".gif"));
            }
        });

        btnDstVersionDetails.addActionListener(e -> {
            VersionDetailsDialog dialog = new VersionDetailsDialog(new java.awt.Frame(), true, iso);
            dialog.lockSrc();
            dialog.showDialog(src, dst, translations);
            if(dialog.getDialogResult() == DialogResult.OK){
                // Do the next call before all others
                // Because dialog.getTranslations() can crash and indicate an error
                // If error then do not change anything
                translations = dialog.getTranslations();
                boolean b = dialog.noError();
                if(b){
                    dst = dialog.getDst();
                    lblDst.setIcon(Load.fromResource("/org/wingate/feuille/util/" + dst.getAlpha2() + ".gif"));
                }
            }
        });
    }

    private void lockUnlock(boolean b, JTextPane pane, JButton version, JLabel flag){
        pane.setEnabled(!b);
        version.setEnabled(!b);
        flag.setEnabled(!b);
    }

    public void setPaneSrc(JTextPane paneSrc) {
        this.paneSrc = paneSrc;
    }

    public void setPaneDst(JTextPane paneDst) {
        this.paneDst = paneDst;
    }

    public void setSrc(ISO_3166 src) {
        this.src = src;
    }

    public void setDst(ISO_3166 dst) {
        this.dst = dst;
    }

    public AssTranslateTo getTranslations() {
        return translations;
    }

    public void setTranslations(AssTranslateTo translations) {
        this.translations = translations;
    }

    public void showLabel(){
        lblSrc.setIcon(Load.fromResource("/org/wingate/feuille/util/" + src.getAlpha2() + ".gif"));
        lblDst.setIcon(Load.fromResource("/org/wingate/feuille/util/" + dst.getAlpha2() + ".gif"));
    }
}
