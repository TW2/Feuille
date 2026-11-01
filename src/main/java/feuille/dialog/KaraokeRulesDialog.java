package feuille.dialog;

import feuille.filefilter.JavascriptFileFilter;
import feuille.io.Settings;
import feuille.io.js.Function;
import feuille.model.FunctionsTableModel;
import feuille.util.DialogResult;
import feuille.io.Exchange;
import feuille.util.Loader;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class KaraokeRulesDialog extends JDialog {

    private final Exchange exchange;

    private Settings settings;
    private DialogResult dialogResult;

    //=============================================
    private final FunctionsTableModel functionsTableModel;
    private final JTable functionsTable;
    private final RSyntaxTextArea textArea;

    public KaraokeRulesDialog(Exchange exchange) {
        super(exchange.getMainFrame());
        this.exchange = exchange;
        settings = null;
        setTitle("Karaoke Rules");
        setModal(true);
        setResizable(false);
        setSize(700, 600);

        dialogResult = DialogResult.None;

        JPanel contentPane = new JPanel(new BorderLayout());

        JButton btnOK = new JButton(Loader.language("dialog.ok", "OK"));
        btnOK.addActionListener(this::btnOKActionPerformed);
        JButton btnCancel = new JButton(Loader.language("dialog.cancel", "Cancel"));
        btnCancel.addActionListener(this::btnCancelActionPerformed);

        setContentPane(contentPane);
        getRootPane().setDefaultButton(btnOK);

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel resultPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        resultPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        resultPanel.add(btnOK);
        resultPanel.add(btnCancel);

        contentPane.add(tabbedPane, BorderLayout.CENTER);
        contentPane.add(resultPanel, BorderLayout.SOUTH);

        Border bdTab = BorderFactory.createLineBorder(
                UIManager.getColor("TabbedPane.underlineColor"));

        //===================================================
        // Main panel
        JPanel rulesMainPanel = new JPanel(new BorderLayout());
        // Second Panel (center / with functionsMainPanel and codeEditorScrollPane)
        JPanel functionsAndEditorsPanel = new JPanel(new GridLayout(2, 1));
        JPanel functionsPanel = new JPanel(new BorderLayout());
        JPanel functionsControlsPanel = new JPanel(new GridLayout(1, 3));

        JButton btnAddFunction = new JButton(Loader.language("dialog.add", "Add script"));
        btnAddFunction.addActionListener(this::btnAddFunctionActionPerformed);
        functionsControlsPanel.add(btnAddFunction);

        JButton btnRemoveFunction = new JButton(Loader.language("dialog.remove", "Remove script"));
        btnRemoveFunction.addActionListener(e -> {
            // TODO
        });
        functionsControlsPanel.add(btnRemoveFunction);

        JButton btnReloadFunctions = new JButton(Loader.language("dialog.reload", "Reload"));
        btnReloadFunctions.addActionListener(e -> {
            // TODO
        });
        functionsControlsPanel.add(btnReloadFunctions);

        functionsTableModel = new FunctionsTableModel();
        functionsTable = new JTable(functionsTableModel);
        functionsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        functionsTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        functionsTable.getColumnModel().getColumn(1).setPreferredWidth(298);
        functionsTable.getColumnModel().getColumn(2).setPreferredWidth(298);
        for(Function fun : exchange.getMakeItAutomation().getFunctions()) {
            functionsTableModel.setValueAt(fun, -1, 0);
            functionsTableModel.setValueAt(true, -1, 0);
        }

        textArea = new RSyntaxTextArea(20, 60);
        textArea.setFont(textArea.getFont().deriveFont(18f));
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
        textArea.setCodeFoldingEnabled(true);
        RTextScrollPane codeEditorScrollPane = new RTextScrollPane(textArea);

        functionsPanel.add(new JScrollPane(functionsTable), BorderLayout.CENTER);
        functionsPanel.add(functionsControlsPanel, BorderLayout.SOUTH);

        functionsAndEditorsPanel.add(functionsPanel);
        functionsAndEditorsPanel.add(codeEditorScrollPane);

        rulesMainPanel.add(functionsAndEditorsPanel, BorderLayout.CENTER);
        tabbedPane.add(Loader.language("dialog.rules.auto", "Automation"), rulesMainPanel);
    }

    public void showDialog(Settings settings){
        if(settings == null) return;
        this.settings = settings;
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

    //--------------------------------------------------------------------

    public List<Function> getFunctions() {
        List<Function> functions = new ArrayList<>();
        for(int i=0; i<functionsTableModel.getRowCount(); i++) {
            functions.add((Function) functionsTableModel.getValueAt(i, -1));
        }
        return functions;
    }

    public void setFunctions(List<Function> functions) {
        functionsTable.removeAll();
        for (Function fun : functions) {
            setFunctionTableValue(-1, fun);
        }
    }

    /**
     * Add or change a value (column is unused and can be filled with any integer value)
     * @param row if <code>row == -1</code> add a new value otherwise change the value
     * @param function the function which can be called
     */
    public void setFunctionTableValue(int row, Function function){
        functionsTableModel.setValueAt(function, row, -1);
    }

    public Object getFunctionTableValue(int row, int column){
        return functionsTableModel.getValueAt(row, column);
    }

    public void removeFunctionTableValue(int row){
        functionsTableModel.removeRow(row);
    }

    public void btnAddFunctionActionPerformed(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setDialogType(JFileChooser.SAVE_DIALOG);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.addChoosableFileFilter(new JavascriptFileFilter());
        int z = chooser.showSaveDialog(exchange.getMainFrame());
        if (z == JFileChooser.APPROVE_OPTION) {
            List<String> folders = exchange.getMakeItAutomation().getFolders();
            File selected = chooser.getSelectedFile();
            if(!folders.contains(selected.getParent())){
                folders.add(selected.getParent());
            }
            try(PrintWriter pw = new PrintWriter(selected)){
                pw.println(textArea.getText());
            }catch(Exception ex){
                Loader.dialogErr(ex.getMessage());
            }
            exchange.getMakeItAutomation().reload();
            for(Function fun : exchange.getMakeItAutomation().getFunctions()) {
                if(!functionsTableModel.hasFunction(fun)){
                    setFunctionTableValue(-1, fun);
                }
            }
            functionsTable.updateUI();
        }
    }
}
