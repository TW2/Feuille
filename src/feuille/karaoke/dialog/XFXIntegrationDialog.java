/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.karaoke.dialog;

import feuille.karaoke.xfxintegration.Jitter;
import feuille.karaoke.xfxintegration.Fax;
import feuille.karaoke.xfxintegration.OIntegration;
import feuille.karaoke.xfxintegration.Animation;
import feuille.karaoke.xfxintegration.MoveVC;
import feuille.karaoke.xfxintegration.GColor2;
import feuille.karaoke.xfxintegration.XShad;
import feuille.karaoke.xfxintegration.Fay;
import feuille.karaoke.xfxintegration.GColor1;
import feuille.karaoke.xfxintegration.Position;
import feuille.karaoke.xfxintegration.Z;
import feuille.karaoke.xfxintegration.Transparency;
import feuille.karaoke.xfxintegration.Karaoke;
import feuille.karaoke.xfxintegration.Shad;
import feuille.karaoke.xfxintegration.Bold;
import feuille.karaoke.xfxintegration.Fsvp;
import feuille.karaoke.xfxintegration.Reset;
import feuille.karaoke.xfxintegration.Mdz;
import feuille.karaoke.xfxintegration.Bord;
import feuille.karaoke.xfxintegration.Origin;
import feuille.karaoke.xfxintegration.FontScaleY;
import feuille.karaoke.xfxintegration.Clip;
import feuille.karaoke.xfxintegration.MoveS3;
import feuille.karaoke.xfxintegration.Md;
import feuille.karaoke.xfxintegration.Transparency4;
import feuille.karaoke.xfxintegration.FontRotationZ;
import feuille.karaoke.xfxintegration.GTransparency3;
import feuille.karaoke.xfxintegration.FontScaleX;
import feuille.karaoke.xfxintegration.Color1;
import feuille.karaoke.xfxintegration.GTransparency4;
import feuille.karaoke.xfxintegration.SuperString;
import feuille.karaoke.xfxintegration.Underline;
import feuille.karaoke.xfxintegration.GColor4;
import feuille.karaoke.xfxintegration.YShad;
import feuille.karaoke.xfxintegration.WrappingStyle;
import feuille.karaoke.xfxintegration.Color4;
import feuille.karaoke.xfxintegration.Blur;
import feuille.karaoke.xfxintegration.ClipWithDrawings;
import feuille.karaoke.xfxintegration.Alignment;
import feuille.karaoke.xfxintegration.GTransparency1;
import feuille.karaoke.xfxintegration.KaraokeOutline;
import feuille.karaoke.xfxintegration.FontSpacing;
import feuille.karaoke.xfxintegration.FontRotationX;
import feuille.karaoke.xfxintegration.BlurEdge;
import feuille.karaoke.xfxintegration.Fade;
import feuille.karaoke.xfxintegration.Params;
import feuille.karaoke.xfxintegration.FontRotationY;
import feuille.karaoke.xfxintegration.MoveR;
import feuille.karaoke.xfxintegration.Italic;
import feuille.karaoke.xfxintegration.Color3;
import feuille.karaoke.xfxintegration.Frs;
import feuille.karaoke.xfxintegration.Image1;
import feuille.karaoke.xfxintegration.GTransparency2;
import feuille.karaoke.xfxintegration.Image2;
import feuille.karaoke.xfxintegration.Mdx;
import feuille.karaoke.xfxintegration.KaraokeFill;
import feuille.karaoke.xfxintegration.Image3;
import feuille.karaoke.xfxintegration.Distort;
import feuille.karaoke.xfxintegration.FontName;
import feuille.karaoke.xfxintegration.GColor3;
import feuille.karaoke.xfxintegration.Move;
import feuille.karaoke.xfxintegration.MoveS4;
import feuille.karaoke.xfxintegration.IClip;
import feuille.karaoke.xfxintegration.Color2;
import feuille.karaoke.xfxintegration.FontEncoding;
import feuille.karaoke.xfxintegration.FontSize;
import feuille.karaoke.xfxintegration.FontScale;
import feuille.karaoke.xfxintegration.Strikeout;
import feuille.karaoke.xfxintegration.Transparency3;
import feuille.karaoke.xfxintegration.Mdy;
import feuille.karaoke.xfxintegration.Transparency1;
import feuille.karaoke.xfxintegration.AlignmentOld;
import feuille.karaoke.xfxintegration.Image4;
import feuille.karaoke.xfxintegration.YBord;
import feuille.karaoke.xfxintegration.IClipWithDrawings;
import feuille.karaoke.xfxintegration.XBord;
import feuille.karaoke.xfxintegration.Transparency2;
import feuille.karaoke.xfxintegration.Fad;
import java.awt.Frame;
import java.util.Enumeration;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import feuille.karaoke.editor.xfxintObjectTableEditor;
import feuille.karaoke.renderer.XFXIntListRenderer;
import feuille.karaoke.renderer.XFXIntTreeRenderer;
import feuille.karaoke.renderer.xfxintObjectTableRenderer;
import feuille.karaoke.renderer.xfxintParamsTableRenderer;
import feuille.lib.Language;

/**
 *
 * @author The Wingate 2940
 */
public class XFXIntegrationDialog extends javax.swing.JDialog implements TableModelListener {
    
    private ButtonPressed bp;
    private Language localeLanguage = feuille.MainFrame.getLanguage();
    private Frame frame;
    private DefaultComboBoxModel dcbmEffects;
    private DefaultTreeModel dtmEffectsList;
    private DefaultMutableTreeNode dmtnRoot;
    private DefaultMutableTreeNode dmtnInit;
    private DefaultMutableTreeNode dmtnAnim;
    private DefaultTableModel dtmParams;
    private String stropt = "Not in animation tag !";

    /**
     * Creates new form XFXIntegrationDialog
     */
    public XFXIntegrationDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        init();
        frame = parent;
        
        if(localeLanguage.getValueOf("buttonOk")!=null){OK_Button.setText(localeLanguage.getValueOf("buttonOk"));}
        if(localeLanguage.getValueOf("buttonCancel")!=null){Cancel_Button.setText(localeLanguage.getValueOf("buttonCancel"));}
        if(localeLanguage.getValueOf("treeEffects")!=null){dmtnRoot.setUserObject(localeLanguage.getValueOf("treeEffects"));}
        if(localeLanguage.getValueOf("treeInitialization")!=null){dmtnInit.setUserObject(localeLanguage.getValueOf("treeInitialization"));}
        if(localeLanguage.getValueOf("treeAnimations")!=null){dmtnAnim.setUserObject(localeLanguage.getValueOf("treeAnimations"));}
        if(localeLanguage.getValueOf("optpXFXInit")!=null){stropt = localeLanguage.getValueOf("optpXFXInit");}
        if(localeLanguage.getValueOf("toolXFXAddInit")!=null){btnAddInit.setToolTipText(localeLanguage.getValueOf("toolXFXAddInit"));}
        if(localeLanguage.getValueOf("toolXFXAddAnim")!=null){btnAdd.setToolTipText(localeLanguage.getValueOf("toolXFXAddAnim"));}
        if(localeLanguage.getValueOf("toolXFXDelete")!=null){btnRemove.setToolTipText(localeLanguage.getValueOf("toolXFXDelete"));}
        if(localeLanguage.getValueOf("toolXFXChange")!=null){btnOption.setToolTipText(localeLanguage.getValueOf("toolXFXChange"));}
        if(localeLanguage.getValueOf("popmXFXActive")!=null){popmParamActive.setText(localeLanguage.getValueOf("popmXFXActive"));}
        if(localeLanguage.getValueOf("popmXFXInactive")!=null){popmParamInactive.setText(localeLanguage.getValueOf("popmXFXInactive"));}
                
        TableColumn column;
        for (int i = 0; i < 2; i++) {
            column = tableParams.getColumnModel().getColumn(i);
            switch(i){
                case 0:
                    if(localeLanguage.getValueOf("tableParameters")!=null){
                        column.setHeaderValue(localeLanguage.getValueOf("tableParameters"));
                    }
                    break;
                case 1:
                    if(localeLanguage.getValueOf("tableSettings")!=null){
                        column.setHeaderValue(localeLanguage.getValueOf("tableSettings"));
                    }
                    break;
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tablePopup = new javax.swing.JPopupMenu();
        popmParamActive = new javax.swing.JMenuItem();
        popmParamInactive = new javax.swing.JMenuItem();
        jScrollPane1 = new javax.swing.JScrollPane();
        treeEffectsList = new javax.swing.JTree();
        jToolBar1 = new javax.swing.JToolBar();
        cbEffects = new javax.swing.JComboBox();
        btnAddInit = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();
        lblTitle = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableParams = new javax.swing.JTable();
        OK_Button = new javax.swing.JButton();
        Cancel_Button = new javax.swing.JButton();
        btnOption = new javax.swing.JButton();

        popmParamActive.setText("Activate the parameter");
        popmParamActive.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmParamActiveActionPerformed(evt);
            }
        });
        tablePopup.add(popmParamActive);

        popmParamInactive.setText("Deactivate the parameter");
        popmParamInactive.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popmParamInactiveActionPerformed(evt);
            }
        });
        tablePopup.add(popmParamInactive);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        treeEffectsList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                treeEffectsListMouseClicked(evt);
            }
        });
        treeEffectsList.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                treeEffectsListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(treeEffectsList);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        cbEffects.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jToolBar1.add(cbEffects);

        btnAddInit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/32px-Crystal_Clear_action_edit_add-yellow.png"))); // NOI18N
        btnAddInit.setToolTipText("Add in Init");
        btnAddInit.setFocusable(false);
        btnAddInit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAddInit.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAddInit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddInitActionPerformed(evt);
            }
        });
        jToolBar1.add(btnAddInit);

        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/32px-Crystal_Clear_action_edit_add-blue.png"))); // NOI18N
        btnAdd.setToolTipText("Add");
        btnAdd.setFocusable(false);
        btnAdd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAdd.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        jToolBar1.add(btnAdd);

        btnRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smallboxforfansub/images/32px-Crystal_Clear_action_edit_remove.png"))); // NOI18N
        btnRemove.setToolTipText("Remove");
        btnRemove.setFocusable(false);
        btnRemove.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRemove.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveActionPerformed(evt);
            }
        });
        jToolBar1.add(btnRemove);

        lblTitle.setText("jLabel1");

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        tableParams.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tableParams.setComponentPopupMenu(tablePopup);
        tableParams.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                tableParamsMouseExited(evt);
            }
        });
        tableParams.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tableParamsFocusLost(evt);
            }
        });
        jScrollPane2.setViewportView(tableParams);

        OK_Button.setText("OK");
        OK_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OK_ButtonActionPerformed(evt);
            }
        });

        Cancel_Button.setText("Cancel");
        Cancel_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Cancel_ButtonActionPerformed(evt);
            }
        });

        btnOption.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/48px-Quick_restart.png"))); // NOI18N
        btnOption.setToolTipText("Options");
        btnOption.setEnabled(false);
        btnOption.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOptionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnOption, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(Cancel_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(OK_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 469, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnOption, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(OK_Button)
                    .addComponent(Cancel_Button))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void OK_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OK_ButtonActionPerformed
        bp = ButtonPressed.OK_BUTTON;
        dispose();
    }//GEN-LAST:event_OK_ButtonActionPerformed

    private void Cancel_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Cancel_ButtonActionPerformed
        bp = ButtonPressed.CANCEL_BUTTON;
        dispose();
    }//GEN-LAST:event_Cancel_ButtonActionPerformed

    private void btnAddInitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddInitActionPerformed
        // Add initialization
        if(cbEffects.getSelectedIndex()!=-1){
            try{
                Object o = cbEffects.getSelectedItem();
                if(o instanceof Karaoke){
//                    boolean found = false;
//                    for (Enumeration e = dmtnInit.children() ; e.hasMoreElements() ;) {
//                        if(e.nextElement() instanceof Karaoke){found=true;}
//                        if(e.nextElement() instanceof KaraokeFill){found=true;}
//                        if(e.nextElement() instanceof KaraokeOutline){found=true;}
//                    }
//                    if(found==false){
                        dmtnInit.add(new DefaultMutableTreeNode(new Karaoke()));
//                    }                
                }else if(o instanceof KaraokeFill){
                    dmtnInit.add(new DefaultMutableTreeNode(new KaraokeFill()));
                }else if(o instanceof KaraokeOutline){
                    dmtnInit.add(new DefaultMutableTreeNode(new KaraokeOutline()));
                }else if(o instanceof Animation){
                    dmtnAnim.add(new DefaultMutableTreeNode(new Animation()));
                }else if(o instanceof Reset){
                    dmtnInit.add(new DefaultMutableTreeNode(new Reset()));
                }else if(o instanceof FontName){
                    dmtnInit.add(new DefaultMutableTreeNode(new FontName()));
                }else if(o instanceof FontEncoding){
                    dmtnInit.add(new DefaultMutableTreeNode(new FontEncoding()));
                }else if(o instanceof WrappingStyle){
                    dmtnInit.add(new DefaultMutableTreeNode(new WrappingStyle()));
                }else if(o instanceof AlignmentOld){
                    dmtnInit.add(new DefaultMutableTreeNode(new AlignmentOld()));
                }else if(o instanceof Alignment){
                    dmtnInit.add(new DefaultMutableTreeNode(new Alignment()));
                }else if(o instanceof Position){
                    dmtnInit.add(new DefaultMutableTreeNode(new Position()));
                }else if(o instanceof Move){
                    dmtnInit.add(new DefaultMutableTreeNode(new Move()));
                }else if(o instanceof Origin){
                    dmtnInit.add(new DefaultMutableTreeNode(new Origin()));
                }else if(o instanceof Fad){
                    dmtnInit.add(new DefaultMutableTreeNode(new Fad()));
                }else if(o instanceof Fade){
                    dmtnInit.add(new DefaultMutableTreeNode(new Fade()));
                }else if(o instanceof ClipWithDrawings){
                    dmtnInit.add(new DefaultMutableTreeNode(new ClipWithDrawings()));
                }else if(o instanceof IClipWithDrawings){
                    dmtnInit.add(new DefaultMutableTreeNode(new IClipWithDrawings()));
                }else if(o instanceof MoveR){
                    dmtnInit.add(new DefaultMutableTreeNode(new MoveR()));
                }else if(o instanceof MoveS3){
                    dmtnInit.add(new DefaultMutableTreeNode(new MoveS3()));
                }else if(o instanceof MoveS4){
                    dmtnInit.add(new DefaultMutableTreeNode(new MoveS4()));
                }else if(o instanceof MoveVC){
                    dmtnInit.add(new DefaultMutableTreeNode(new MoveVC()));
                }else if(o instanceof Bold){
                    dmtnInit.add(new DefaultMutableTreeNode(new Bold()));
                }else if(o instanceof Italic){
                    dmtnInit.add(new DefaultMutableTreeNode(new Italic()));
                }else if(o instanceof Underline){
                    dmtnInit.add(new DefaultMutableTreeNode(new Underline()));
                }else if(o instanceof Strikeout){
                    dmtnInit.add(new DefaultMutableTreeNode(new Strikeout()));
                }else if(o instanceof Bord){
                    dmtnInit.add(new DefaultMutableTreeNode(new Bord()));
                }else if(o instanceof Shad){
                    dmtnInit.add(new DefaultMutableTreeNode(new Shad()));
                }else if(o instanceof BlurEdge){
                    dmtnInit.add(new DefaultMutableTreeNode(new BlurEdge()));
                }else if(o instanceof FontSize){
                    dmtnInit.add(new DefaultMutableTreeNode(new FontSize()));
                }else if(o instanceof FontScaleX){
                    dmtnInit.add(new DefaultMutableTreeNode(new FontScaleX()));
                }else if(o instanceof FontScaleY){
                    dmtnInit.add(new DefaultMutableTreeNode(new FontScaleY()));
                }else if(o instanceof FontSpacing){
                    dmtnInit.add(new DefaultMutableTreeNode(new FontSpacing()));
                }else if(o instanceof FontRotationX){
                    dmtnInit.add(new DefaultMutableTreeNode(new FontRotationX()));
                }else if(o instanceof FontRotationY){
                    dmtnInit.add(new DefaultMutableTreeNode(new FontRotationY()));
                }else if(o instanceof FontRotationZ){
                    dmtnInit.add(new DefaultMutableTreeNode(new FontRotationZ()));
                }else if(o instanceof Color1){
                    dmtnInit.add(new DefaultMutableTreeNode(new Color1()));
                }else if(o instanceof Color2){
                    dmtnInit.add(new DefaultMutableTreeNode(new Color2()));
                }else if(o instanceof Color3){
                    dmtnInit.add(new DefaultMutableTreeNode(new Color3()));
                }else if(o instanceof Color4){
                    dmtnInit.add(new DefaultMutableTreeNode(new Color4()));
                }else if(o instanceof Transparency){
                    dmtnInit.add(new DefaultMutableTreeNode(new Transparency()));
                }else if(o instanceof Transparency1){
                    dmtnInit.add(new DefaultMutableTreeNode(new Transparency1()));
                }else if(o instanceof Transparency2){
                    dmtnInit.add(new DefaultMutableTreeNode(new Transparency2()));
                }else if(o instanceof Transparency3){
                    dmtnInit.add(new DefaultMutableTreeNode(new Transparency3()));
                }else if(o instanceof Transparency4){
                    dmtnInit.add(new DefaultMutableTreeNode(new Transparency4()));
                }else if(o instanceof Clip){
                    dmtnInit.add(new DefaultMutableTreeNode(new Clip()));
                }else if(o instanceof XBord){
                    dmtnInit.add(new DefaultMutableTreeNode(new XBord()));
                }else if(o instanceof YBord){
                    dmtnInit.add(new DefaultMutableTreeNode(new YBord()));
                }else if(o instanceof XShad){
                    dmtnInit.add(new DefaultMutableTreeNode(new XShad()));
                }else if(o instanceof YShad){
                    dmtnInit.add(new DefaultMutableTreeNode(new YShad()));
                }else if(o instanceof Blur){
                    dmtnInit.add(new DefaultMutableTreeNode(new Blur()));
                }else if(o instanceof Fax){
                    dmtnInit.add(new DefaultMutableTreeNode(new Fax()));
                }else if(o instanceof Fay){
                    dmtnInit.add(new DefaultMutableTreeNode(new Fay()));
                }else if(o instanceof IClip){
                    dmtnInit.add(new DefaultMutableTreeNode(new IClip()));
                }else if(o instanceof FontScale){
                    dmtnInit.add(new DefaultMutableTreeNode(new FontScale()));
                }else if(o instanceof Fsvp){
                    dmtnInit.add(new DefaultMutableTreeNode(new Fsvp()));
                }else if(o instanceof Frs){
                    dmtnInit.add(new DefaultMutableTreeNode(new Frs()));
                }else if(o instanceof Z){
                    dmtnInit.add(new DefaultMutableTreeNode(new Z()));
                }else if(o instanceof Distort){
                    dmtnInit.add(new DefaultMutableTreeNode(new Distort()));
                }else if(o instanceof Md){
                    dmtnInit.add(new DefaultMutableTreeNode(new Md()));
                }else if(o instanceof Mdx){
                    dmtnInit.add(new DefaultMutableTreeNode(new Mdx()));
                }else if(o instanceof Mdy){
                    dmtnInit.add(new DefaultMutableTreeNode(new Mdy()));
                }else if(o instanceof Mdz){
                    dmtnInit.add(new DefaultMutableTreeNode(new Mdz()));
                }else if(o instanceof GColor1){
                    dmtnInit.add(new DefaultMutableTreeNode(new GColor1()));
                }else if(o instanceof GColor2){
                    dmtnInit.add(new DefaultMutableTreeNode(new GColor2()));
                }else if(o instanceof GColor3){
                    dmtnInit.add(new DefaultMutableTreeNode(new GColor3()));
                }else if(o instanceof GColor4){
                    dmtnInit.add(new DefaultMutableTreeNode(new GColor4()));
                }else if(o instanceof GTransparency1){
                    dmtnInit.add(new DefaultMutableTreeNode(new GTransparency1()));
                }else if(o instanceof GTransparency2){
                    dmtnInit.add(new DefaultMutableTreeNode(new GTransparency2()));
                }else if(o instanceof GTransparency3){
                    dmtnInit.add(new DefaultMutableTreeNode(new GTransparency3()));
                }else if(o instanceof GTransparency4){
                    dmtnInit.add(new DefaultMutableTreeNode(new GTransparency4()));
                }else if(o instanceof Image1){
                    dmtnInit.add(new DefaultMutableTreeNode(new Image1()));
                }else if(o instanceof Image2){
                    dmtnInit.add(new DefaultMutableTreeNode(new Image2()));
                }else if(o instanceof Image3){
                    dmtnInit.add(new DefaultMutableTreeNode(new Image3()));
                }else if(o instanceof Image4){
                    dmtnInit.add(new DefaultMutableTreeNode(new Image4()));
                }else if(o instanceof Jitter){
                    dmtnInit.add(new DefaultMutableTreeNode(new Jitter()));
                }
                treeEffectsList.expandRow(dmtnRoot.getIndex(dmtnInit));
                treeEffectsList.expandRow(dmtnRoot.getIndex(dmtnAnim));
                treeEffectsList.updateUI();
            }catch(Exception exc){
                
            }            
        }
    }//GEN-LAST:event_btnAddInitActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // Add animation
        if(cbEffects.getSelectedIndex()!=-1){
            OIntegration o = (OIntegration)cbEffects.getSelectedItem();            
            if(o instanceof Karaoke | o instanceof KaraokeFill
                    | o instanceof KaraokeOutline | o instanceof Reset
                    | o instanceof FontName | o instanceof FontEncoding
                    | o instanceof WrappingStyle | o instanceof AlignmentOld
                    | o instanceof Alignment | o instanceof Position
                    | o instanceof Move | o instanceof Origin
                    | o instanceof Fad | o instanceof Fade
                    | o instanceof ClipWithDrawings | o instanceof IClipWithDrawings
                    | o instanceof MoveR | o instanceof MoveS3
                    | o instanceof MoveS4 | o instanceof MoveVC){
                JOptionPane.showMessageDialog(frame, stropt, o.getName(), JOptionPane.INFORMATION_MESSAGE);
            }else{
                try{
                    DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)treeEffectsList.getSelectionPath().getLastPathComponent();
                    if(dmtn.getUserObject() instanceof Animation){
                        if(o instanceof Bold){
                            dmtn.add(new DefaultMutableTreeNode(new Bold()));
                        }else if(o instanceof Italic){
                            dmtn.add(new DefaultMutableTreeNode(new Italic()));
                        }else if(o instanceof Underline){
                            dmtn.add(new DefaultMutableTreeNode(new Underline()));
                        }else if(o instanceof Strikeout){
                            dmtn.add(new DefaultMutableTreeNode(new Strikeout()));
                        }else if(o instanceof Bord){
                            dmtn.add(new DefaultMutableTreeNode(new Bord()));
                        }else if(o instanceof Shad){
                            dmtn.add(new DefaultMutableTreeNode(new Shad()));
                        }else if(o instanceof BlurEdge){
                            dmtn.add(new DefaultMutableTreeNode(new BlurEdge()));
                        }else if(o instanceof FontSize){
                            dmtn.add(new DefaultMutableTreeNode(new FontSize()));
                        }else if(o instanceof FontScaleX){
                            dmtn.add(new DefaultMutableTreeNode(new FontScaleX()));
                        }else if(o instanceof FontScaleY){
                            dmtn.add(new DefaultMutableTreeNode(new FontScaleY()));
                        }else if(o instanceof FontSpacing){
                            dmtn.add(new DefaultMutableTreeNode(new FontSpacing()));
                        }else if(o instanceof FontRotationX){
                            dmtn.add(new DefaultMutableTreeNode(new FontRotationX()));
                        }else if(o instanceof FontRotationY){
                            dmtn.add(new DefaultMutableTreeNode(new FontRotationY()));
                        }else if(o instanceof FontRotationZ){
                            dmtn.add(new DefaultMutableTreeNode(new FontRotationZ()));
                        }else if(o instanceof Color1){
                            dmtn.add(new DefaultMutableTreeNode(new Color1()));
                        }else if(o instanceof Color2){
                            dmtn.add(new DefaultMutableTreeNode(new Color2()));
                        }else if(o instanceof Color3){
                            dmtn.add(new DefaultMutableTreeNode(new Color3()));
                        }else if(o instanceof Color4){
                            dmtn.add(new DefaultMutableTreeNode(new Color4()));
                        }else if(o instanceof Transparency){
                            dmtn.add(new DefaultMutableTreeNode(new Transparency()));
                        }else if(o instanceof Transparency1){
                            dmtn.add(new DefaultMutableTreeNode(new Transparency1()));
                        }else if(o instanceof Transparency2){
                            dmtn.add(new DefaultMutableTreeNode(new Transparency2()));
                        }else if(o instanceof Transparency3){
                            dmtn.add(new DefaultMutableTreeNode(new Transparency3()));
                        }else if(o instanceof Transparency4){
                            dmtn.add(new DefaultMutableTreeNode(new Transparency4()));
                        }else if(o instanceof Clip){
                            dmtn.add(new DefaultMutableTreeNode(new Clip()));
                        }else if(o instanceof XBord){
                            dmtn.add(new DefaultMutableTreeNode(new XBord()));
                        }else if(o instanceof YBord){
                            dmtn.add(new DefaultMutableTreeNode(new YBord()));
                        }else if(o instanceof XShad){
                            dmtn.add(new DefaultMutableTreeNode(new XShad()));
                        }else if(o instanceof YShad){
                            dmtn.add(new DefaultMutableTreeNode(new YShad()));
                        }else if(o instanceof Blur){
                            dmtn.add(new DefaultMutableTreeNode(new Blur()));
                        }else if(o instanceof Fax){
                            dmtn.add(new DefaultMutableTreeNode(new Fax()));
                        }else if(o instanceof Fay){
                            dmtn.add(new DefaultMutableTreeNode(new Fay()));
                        }else if(o instanceof IClip){
                            dmtn.add(new DefaultMutableTreeNode(new IClip()));
                        }else if(o instanceof FontScale){
                            dmtn.add(new DefaultMutableTreeNode(new FontScale()));
                        }else if(o instanceof Fsvp){
                            dmtn.add(new DefaultMutableTreeNode(new Fsvp()));
                        }else if(o instanceof Frs){
                            dmtn.add(new DefaultMutableTreeNode(new Frs()));
                        }else if(o instanceof Z){
                            dmtn.add(new DefaultMutableTreeNode(new Z()));
                        }else if(o instanceof Distort){
                            dmtn.add(new DefaultMutableTreeNode(new Distort()));
                        }else if(o instanceof Md){
                            dmtn.add(new DefaultMutableTreeNode(new Md()));
                        }else if(o instanceof Mdx){
                            dmtn.add(new DefaultMutableTreeNode(new Mdx()));
                        }else if(o instanceof Mdy){
                            dmtn.add(new DefaultMutableTreeNode(new Mdy()));
                        }else if(o instanceof Mdz){
                            dmtn.add(new DefaultMutableTreeNode(new Mdz()));
                        }else if(o instanceof GColor1){
                            dmtn.add(new DefaultMutableTreeNode(new GColor1()));
                        }else if(o instanceof GColor2){
                            dmtn.add(new DefaultMutableTreeNode(new GColor2()));
                        }else if(o instanceof GColor3){
                            dmtn.add(new DefaultMutableTreeNode(new GColor3()));
                        }else if(o instanceof GColor4){
                            dmtn.add(new DefaultMutableTreeNode(new GColor4()));
                        }else if(o instanceof GTransparency1){
                            dmtn.add(new DefaultMutableTreeNode(new GTransparency1()));
                        }else if(o instanceof GTransparency2){
                            dmtn.add(new DefaultMutableTreeNode(new GTransparency2()));
                        }else if(o instanceof GTransparency3){
                            dmtn.add(new DefaultMutableTreeNode(new GTransparency3()));
                        }else if(o instanceof GTransparency4){
                            dmtn.add(new DefaultMutableTreeNode(new GTransparency4()));
                        }else if(o instanceof Image1){
                            dmtn.add(new DefaultMutableTreeNode(new Image1()));
                        }else if(o instanceof Image2){
                            dmtn.add(new DefaultMutableTreeNode(new Image2()));
                        }else if(o instanceof Image3){
                            dmtn.add(new DefaultMutableTreeNode(new Image3()));
                        }else if(o instanceof Image4){
                            dmtn.add(new DefaultMutableTreeNode(new Image4()));
                        }else if(o instanceof Jitter){
                            dmtn.add(new DefaultMutableTreeNode(new Jitter()));
                        }
                    }
                    treeEffectsList.expandRow(dmtnRoot.getIndex(dmtn));
                    treeEffectsList.updateUI();
                }catch(Exception exc){
                    
                }   
            }
            treeEffectsList.updateUI();
        }
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveActionPerformed
        // Remove
        try{
            DefaultMutableTreeNode dmtn = 
                    (DefaultMutableTreeNode)treeEffectsList.getSelectionPath().getLastPathComponent();
            dmtn.removeFromParent();
            treeEffectsList.updateUI();
        }catch(Exception exc){
            
        }
    }//GEN-LAST:event_btnRemoveActionPerformed

    private void treeEffectsListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_treeEffectsListMouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_treeEffectsListMouseClicked

    private void tableParamsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tableParamsFocusLost
        // TODO add your handling code here:
//        for(int i=0; i<dtmParams.getRowCount(); i++){
//            Params p = (Params)tableParams.getValueAt(i, 0);
//            Object o = tableParams.getValueAt(i, 1);
//            p.setParameter(o);
//        }
    }//GEN-LAST:event_tableParamsFocusLost

    private void tableParamsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableParamsMouseExited
        // TODO add your handling code here:
        
    }//GEN-LAST:event_tableParamsMouseExited

    private void treeEffectsListValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_treeEffectsListValueChanged
        // TODO add your handling code here:
        for (int i=tableParams.getRowCount()-1;i>=0;i--){
            dtmParams.removeRow(i);
        }
        lblTitle.setText("");
        DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)evt.getNewLeadSelectionPath().getLastPathComponent();
        if(dmtn.getUserObject() instanceof OIntegration){
            OIntegration o = (OIntegration)dmtn.getUserObject();
            int notString = 0;
            for(Params p : o.getParams()){
                Object[] object = new Object[]{p,p.getParameter()};
                dtmParams.addRow(object);
                if(p.getParameter() instanceof String){
                    //Do nothing
                }else{
                    notString += 1;
                }
            }
            lblTitle.setText("<html><h2>"+o.getName());
            if(notString>0){//We have custom(s) object
                btnOption.setEnabled(true);
            }else{//We have only String object(s)
                btnOption.setEnabled(false);
            }
        }
    }//GEN-LAST:event_treeEffectsListValueChanged

    private void popmParamActiveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmParamActiveActionPerformed
        // Activate the parameter
        if(tableParams.getSelectedRow()!=-1){
            int row = tableParams.getSelectedRow();
            Params p = (Params)dtmParams.getValueAt(row, 0);
            if(p.getBeInactive()==true){
                p.setInactive(false);
            }
        }
    }//GEN-LAST:event_popmParamActiveActionPerformed

    private void popmParamInactiveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popmParamInactiveActionPerformed
        // Deactivate the parameter
        if(tableParams.getSelectedRow()!=-1){
            int row = tableParams.getSelectedRow();
            Params p = (Params)dtmParams.getValueAt(row, 0);
            if(p.getBeInactive()==true){
                p.setInactive(true);
            }            
        }
    }//GEN-LAST:event_popmParamInactiveActionPerformed

    private void btnOptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOptionActionPerformed
        // Change the mode
        if(tableParams.getSelectedRow()!=-1){
            int row = tableParams.getSelectedRow();
            Params p = (Params)dtmParams.getValueAt(row, 0);
            if(p.getParameter() instanceof String){
                //Nothing
            }else{
                if(p.getState()==Params.State.Normal){
                    p.setState(Params.State.SuperString);
                    p.setBackup(p.getParameter());
                    p.setParameter(p.getSuperString());
                    dtmParams.setValueAt(p.getParameter(), row, 1);
                }else{
                    p.setState(Params.State.Normal);
                    p.setSuperString((SuperString)p.getParameter());
                    p.setParameter(p.getBackup());
                    dtmParams.setValueAt(p.getParameter(), row, 1);
                }
            }
        }
    }//GEN-LAST:event_btnOptionActionPerformed

    @Override
    public void tableChanged(TableModelEvent e) {
        try{
            int row = e.getFirstRow();
            TableModel model = (TableModel)e.getSource();
            Params p = (Params)model.getValueAt(row, 0);
            Object o = model.getValueAt(row, 1);
            p.setParameter(o);
        }catch(Exception exc){
            
        }
    }

    public enum ButtonPressed{
        NONE, OK_BUTTON, CANCEL_BUTTON;
    }
    
    private void init(){
        //Set up combobox
        dcbmEffects = new DefaultComboBoxModel();
        cbEffects.setModel(dcbmEffects);
        cbEffects.setRenderer(new XFXIntListRenderer());
        dcbmEffects.addElement(new Karaoke());
        dcbmEffects.addElement(new KaraokeFill());
        dcbmEffects.addElement(new KaraokeOutline());
        dcbmEffects.addElement(new Animation());
        dcbmEffects.addElement(new Reset());
        dcbmEffects.addElement(new FontName());
        dcbmEffects.addElement(new FontEncoding());
        dcbmEffects.addElement(new WrappingStyle());
        dcbmEffects.addElement(new AlignmentOld());
        dcbmEffects.addElement(new Alignment());
        dcbmEffects.addElement(new Position());
        dcbmEffects.addElement(new Move());
        dcbmEffects.addElement(new Origin());
        dcbmEffects.addElement(new Fad());
        dcbmEffects.addElement(new Fade());
        dcbmEffects.addElement(new ClipWithDrawings());
        dcbmEffects.addElement(new IClipWithDrawings());
        dcbmEffects.addElement(new MoveR());
        dcbmEffects.addElement(new MoveS3());
        dcbmEffects.addElement(new MoveS4());
        dcbmEffects.addElement(new MoveVC());
        dcbmEffects.addElement(new Bold());
        dcbmEffects.addElement(new Italic());
        dcbmEffects.addElement(new Underline());
        dcbmEffects.addElement(new Strikeout());
        dcbmEffects.addElement(new Bord());
        dcbmEffects.addElement(new Shad());
        dcbmEffects.addElement(new BlurEdge());
        dcbmEffects.addElement(new FontSize());
        dcbmEffects.addElement(new FontScaleX());
        dcbmEffects.addElement(new FontScaleY());
        dcbmEffects.addElement(new FontSpacing());
        dcbmEffects.addElement(new FontRotationX());
        dcbmEffects.addElement(new FontRotationY());
        dcbmEffects.addElement(new FontRotationZ());
        dcbmEffects.addElement(new Color1());
        dcbmEffects.addElement(new Color2());
        dcbmEffects.addElement(new Color3());
        dcbmEffects.addElement(new Color4());
        dcbmEffects.addElement(new Transparency());
        dcbmEffects.addElement(new Transparency1());
        dcbmEffects.addElement(new Transparency2());
        dcbmEffects.addElement(new Transparency3());
        dcbmEffects.addElement(new Transparency4());
        dcbmEffects.addElement(new Clip());
        dcbmEffects.addElement(new XBord());
        dcbmEffects.addElement(new YBord());
        dcbmEffects.addElement(new XShad());
        dcbmEffects.addElement(new YShad());
        dcbmEffects.addElement(new Blur());
        dcbmEffects.addElement(new Fax());
        dcbmEffects.addElement(new Fay());
        dcbmEffects.addElement(new IClip());
        dcbmEffects.addElement(new FontScale());
        dcbmEffects.addElement(new Fsvp());
        dcbmEffects.addElement(new Frs());
        dcbmEffects.addElement(new Z());
        dcbmEffects.addElement(new Distort());
        dcbmEffects.addElement(new Md());
        dcbmEffects.addElement(new Mdx());
        dcbmEffects.addElement(new Mdy());
        dcbmEffects.addElement(new Mdz());
        dcbmEffects.addElement(new GColor1());
        dcbmEffects.addElement(new GColor2());
        dcbmEffects.addElement(new GColor3());
        dcbmEffects.addElement(new GColor4());
        dcbmEffects.addElement(new GTransparency1());
        dcbmEffects.addElement(new GTransparency2());
        dcbmEffects.addElement(new GTransparency3());
        dcbmEffects.addElement(new GTransparency4());
        dcbmEffects.addElement(new Image1());
        dcbmEffects.addElement(new Image2());
        dcbmEffects.addElement(new Image3());
        dcbmEffects.addElement(new Image4());
        dcbmEffects.addElement(new Jitter());
        
        
        //Set up tree
        dmtnRoot = new DefaultMutableTreeNode();
        dmtnRoot.setUserObject("Effects");
        dmtnInit = new DefaultMutableTreeNode();
        dmtnInit.setUserObject("Initialization");
        dmtnAnim = new DefaultMutableTreeNode();
        dmtnAnim.setUserObject("Animations");
        dtmEffectsList = new DefaultTreeModel(dmtnRoot);
        treeEffectsList.setModel(dtmEffectsList);
        dmtnRoot.add(dmtnInit);
        dmtnRoot.add(dmtnAnim);
        treeEffectsList.expandRow(dmtnRoot.getIndex(dmtnInit));
        treeEffectsList.expandRow(dmtnRoot.getIndex(dmtnAnim));
        treeEffectsList.setCellRenderer(new XFXIntTreeRenderer());
        
        //Set up table
        String[] paramsHead = new String[]{"Parameters", "Settings"};
        dtmParams = new DefaultTableModel(null, paramsHead){
            Class[] types = new Class [] {
                    Params.class, Object.class};
            boolean[] canEdit = new boolean [] {
                    false, true};
            @Override
            public Class getColumnClass(int columnIndex) {return types [columnIndex];}
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {return canEdit [columnIndex];}
        };
        tableParams.setModel(dtmParams);
        TableColumn column;
        for (int i = 0; i < 2; i++) {
            column = tableParams.getColumnModel().getColumn(i);
            switch(i){
                case 0:
                    column.setPreferredWidth(400);
                    break; //Parameters
                case 1:
                    column.setPreferredWidth(400);
                    break; //Settings
            }
        }
        tableParams.setRowHeight(30);
        tableParams.setDefaultRenderer(Params.class, new xfxintParamsTableRenderer());
        tableParams.setDefaultRenderer(Object.class, new xfxintObjectTableRenderer());
        dtmParams.addTableModelListener(this);
        tableParams.setDefaultEditor(Object.class, new xfxintObjectTableEditor(frame));
        
        //Set up label
        lblTitle.setText("");
        
    }
    
    private void initCommands(String coms){
        if(coms.contains("\\")==true && coms.contains("<example")==false){
            coms = coms.replaceAll("\\{", "");
            coms = coms.replaceAll("\\}", "");
            String[] table = coms.split("\\\\");
            //If we are in the case of initialization (the first case)
            DefaultMutableTreeNode dmtn = dmtnInit;
            for(String s : table){
                if(s.endsWith(")")){
                    s = s.substring(0, s.length()-1);
                }
                if(s.startsWith("t")){
                    Animation x = new Animation(); x.setCommands(s); x.init();
                    //If we are in the case of animation (the second case)
                    DefaultMutableTreeNode newAnimation = new DefaultMutableTreeNode(x);
                    dmtnAnim.add(newAnimation);
                    dmtn = newAnimation;
                }else if(s.startsWith("an")){
                    Alignment x = new Alignment(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("blur")){
                    Blur x = new Blur(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("bord")){
                    Bord x = new Bord(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("be")){
                    BlurEdge x = new BlurEdge(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("b")){
                    Bold x = new Bold(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("clip") && s.split(",").length==4){
                    Clip x = new Clip(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));                    
                }else if(s.startsWith("clip")){
                    ClipWithDrawings x = new ClipWithDrawings(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("1c&H")){
                    Color1 x = new Color1(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("2c&H")){
                    Color2 x = new Color2(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("3c&H")){
                    Color3 x = new Color3(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("4c&H")){
                    Color4 x = new Color4(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("fade")){
                    Fade x = new Fade(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("fad")){
                    Fad x = new Fad(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("fax")){
                    Fax x = new Fax(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("fay")){
                    Fay x = new Fay(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("fe")){
                    FontEncoding x = new FontEncoding(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("fn")){
                    FontName x = new FontName(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("frx")){
                    FontRotationX x = new FontRotationX(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("fry")){
                    FontRotationY x = new FontRotationY(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("frz")){
                    FontRotationZ x = new FontRotationZ(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("fscx")){
                    FontScaleX x = new FontScaleX(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("fscy")){
                    FontScaleY x = new FontScaleY(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("fsc")){
                    FontScale x = new FontScale(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("fsvp")){
                    Fsvp x = new Fsvp(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("fsp")){
                    FontSpacing x = new FontSpacing(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("fs")){
                    FontSize x = new FontSize(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("frs")){
                    Frs x = new Frs(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("1vc")){
                    GColor1 x = new GColor1(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("2vc")){
                    GColor2 x = new GColor2(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("3vc")){
                    GColor3 x = new GColor3(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("4vc")){
                    GColor4 x = new GColor4(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("1va")){
                    GTransparency1 x = new GTransparency1(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("2va")){
                    GTransparency2 x = new GTransparency2(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("3va")){
                    GTransparency3 x = new GTransparency3(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("4va")){
                    GTransparency4 x = new GTransparency4(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("iclip") && s.split(",").length==4){
                    IClip x = new IClip(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));                    
                }else if(s.startsWith("iclip")){
                    IClipWithDrawings x = new IClipWithDrawings(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("1img")){
                    Image1 x = new Image1(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("2img")){
                    Image2 x = new Image2(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("3img")){
                    Image3 x = new Image3(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("4img")){
                    Image4 x = new Image4(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("i")){
                    Italic x = new Italic(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("jitter")){
                    Jitter x = new Jitter(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("kf")){
                    KaraokeFill x = new KaraokeFill(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("ko")){
                    KaraokeOutline x = new KaraokeOutline(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("k")){
                    Karaoke x = new Karaoke(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("mdx")){
                    Mdx x = new Mdx(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("mdy")){
                    Mdy x = new Mdy(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("mdz")){
                    Mdz x = new Mdz(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("md")){
                    Md x = new Md(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("mover")){
                    MoveR x = new MoveR(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("moves3")){
                    MoveS3 x = new MoveS3(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("moves4")){
                    MoveS4 x = new MoveS4(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("movevc")){
                    MoveVC x = new MoveVC(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("move")){
                    Move x = new Move(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("org")){
                    Origin x = new Origin(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("pos")){
                    Position x = new Position(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("r")){
                    Reset x = new Reset(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("shad")){
                    Shad x = new Shad(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("s")){
                    Strikeout x = new Strikeout(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("alpha&H")){
                    Transparency x = new Transparency(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("a")){
                    AlignmentOld x = new AlignmentOld(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("1a&H")){
                    Transparency1 x = new Transparency1(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("2a&H")){
                    Transparency2 x = new Transparency2(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("3a&H")){
                    Transparency3 x = new Transparency3(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("4a&H")){
                    Transparency4 x = new Transparency4(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("u")){
                    Underline x = new Underline(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("q")){
                    WrappingStyle x = new WrappingStyle(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("xbord")){
                    XBord x = new XBord(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("xshad")){
                    XShad x = new XShad(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("ybord")){
                    YBord x = new YBord(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("yshad")){
                    YShad x = new YShad(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }else if(s.startsWith("z")){
                    Z x = new Z(); x.setCommands(s); x.init();
                    dmtn.add(new DefaultMutableTreeNode(x));
                }
            }
            treeEffectsList.expandRow(dmtnRoot.getIndex(dmtnInit));
            treeEffectsList.expandRow(dmtnRoot.getIndex(dmtnAnim));
            treeEffectsList.updateUI();
        }
    }
    
    private String updateCommands(){
        String s = "";
        //Initialization
        for (Enumeration e = dmtnInit.children() ; e.hasMoreElements() ;) {
            DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)e.nextElement();
            OIntegration obj = (OIntegration)dmtn.getUserObject();
            obj.update(); s = s + obj.getCommands();
        }
        //Animations
        for (Enumeration e = dmtnAnim.children() ; e.hasMoreElements() ;) {
            DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)e.nextElement();            
            OIntegration obj = (OIntegration)dmtn.getUserObject();
            obj.update(); s = s + obj.getCommands();
            for (Enumeration e2 = dmtn.children() ; e2.hasMoreElements() ;) {
                DefaultMutableTreeNode dmtn2 = (DefaultMutableTreeNode)e2.nextElement();            
                OIntegration obj2 = (OIntegration)dmtn2.getUserObject();
                obj2.update(); s = s + obj2.getCommands();
            }
            s = s + ")";
        }
        s = "{" + s + "}";
        return s;
    }
    
    public String showDialog(String s){
        initCommands(s);
        setVisible(true);

        if(bp.equals(ButtonPressed.OK_BUTTON)){
            return updateCommands();
        }else{
            return "";
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(XFXIntegrationDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(XFXIntegrationDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(XFXIntegrationDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(XFXIntegrationDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                XFXIntegrationDialog dialog = new XFXIntegrationDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Cancel_Button;
    private javax.swing.JButton OK_Button;
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnAddInit;
    private javax.swing.JButton btnOption;
    private javax.swing.JButton btnRemove;
    private javax.swing.JComboBox cbEffects;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JMenuItem popmParamActive;
    private javax.swing.JMenuItem popmParamInactive;
    private javax.swing.JTable tableParams;
    private javax.swing.JPopupMenu tablePopup;
    private javax.swing.JTree treeEffectsList;
    // End of variables declaration//GEN-END:variables
}
