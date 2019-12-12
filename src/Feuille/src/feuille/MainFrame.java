/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille;

import feuille.grafx.AShape;
import feuille.grafx.GCubic;
import feuille.grafx.GLine;
import feuille.grafx.GMoveM;
import feuille.grafx.GMoveN;
import feuille.grafx.GQuadratic;
import feuille.grafx.GSpline;
import feuille.lib.HistoricPanel;
import feuille.lib.LayerPanel;
import feuille.lib.Sheet;
import feuille.lib.SheetHBorder;
import feuille.lib.SheetVBorder;
import feuille.listener.EditionCursorListener;
import feuille.listener.EditionCursorEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.EventListenerList;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

/**
 *
 * @author util2
 */
public class MainFrame extends javax.swing.JFrame {

    List<AShape> shapes = new ArrayList<>();
    int uniqueID = 0;
    
    HistoricPanel historicPanel;
    LayerPanel layerPanel;
    
    JScrollPane spDrawing;
    Sheet sheet;
    SheetHBorder hrule;
    SheetVBorder vrule;
    
    // Aide à repérer le point d'insertion (ajout)
    Point lastClicked = null;
    // Aide à repérer le point de modification à la souris (édition)
    boolean inEdition = false;
    EditionCursorEvent evtEdition = null;
    
    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
        init();
    }
    
    private void init(){
        // Center
        setLocationRelativeTo(null);
        
        // L&F
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (UnsupportedLookAndFeelException ex) {
            //Do nothing
        }
        
//        // Icon
//        URL url = getClass().getResource("000.png");
//        java.awt.Toolkit kit = java.awt.Toolkit.getDefaultToolkit();
//        java.awt.Image img = kit.createImage(url);
//        setIconImage(img);

        historicPanel = new HistoricPanel(this);
        histoPanel.setLayout(new BorderLayout());
        histoPanel.add(historicPanel, BorderLayout.CENTER);
        
        layerPanel = new LayerPanel(this);
        laysPanel.setLayout(new BorderLayout());
        laysPanel.add(layerPanel, BorderLayout.CENTER);

        sheet = new Sheet(this);
        spDrawing = new JScrollPane(sheet);
        sheet.setPreferredSize(new java.awt.Dimension(setSizeOfDrawing(),setSizeOfDrawing()));
        sheet.revalidate();        
        
        hrule = new SheetHBorder(setSizeOfDrawing());
        vrule = new SheetVBorder(setSizeOfDrawing());        
        spDrawing.setColumnHeaderView(hrule);
        spDrawing.setRowHeaderView(vrule);
        spDrawing.setOpaque(true);
        spDrawing.setBackground(Color.white);
        
        jPanel6.setLayout(new BorderLayout());
        jPanel6.add(spDrawing, BorderLayout.CENTER);
        
        // Center the view
        Rectangle boundsCenterView = spDrawing.getViewport().getViewRect();
        Dimension sizeCenterView = spDrawing.getViewport().getViewSize();
        int xCenterView = (sizeCenterView.width - boundsCenterView.width) * 6 / 15;
        int yCenterView = (sizeCenterView.height - boundsCenterView.height) * 17 / 40;
        spDrawing.getViewport().setViewPosition(new Point(xCenterView, yCenterView));
        
        spDrawing.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point pCurrent = new Point(
                        e.getPoint().x - vrule.getWidth() + spDrawing.getHorizontalScrollBar().getValue(),
                        e.getPoint().y - hrule.getHeight() + spDrawing.getVerticalScrollBar().getValue()
                );
                
                if(toggleLine.isSelected()){
                    addShape(shapes.isEmpty() ?
                            new GLine(pCurrent, pCurrent, uniqueID++) :
                            new GLine(lastClicked, pCurrent, uniqueID++)
                    );
                    lastClicked = pCurrent;
                    sheet.updateShapesList(shapes);
                }else if(toggleCubic.isSelected()){
                    addShape(shapes.isEmpty() ?
                            new GCubic(pCurrent, pCurrent, uniqueID++) :
                            new GCubic(lastClicked, pCurrent, uniqueID++)
                    );
                    lastClicked = pCurrent;
                    sheet.updateShapesList(shapes);
                }else if(toggleQuad.isSelected()){
                    addShape(shapes.isEmpty() ?
                            new GQuadratic(pCurrent, pCurrent, uniqueID++) :
                            new GQuadratic(lastClicked, pCurrent, uniqueID++)
                    );
                    lastClicked = pCurrent;
                    sheet.updateShapesList(shapes);
                }else if(toggleSpline.isSelected()){
                    // TODO add Spline to drawing area
                }else if(toggleMoveTool1.isSelected()){
                    addShape(shapes.isEmpty() ?
                            new GMoveM(pCurrent, pCurrent, uniqueID++) :
                            new GMoveM(lastClicked, pCurrent, uniqueID++)
                    );
                    lastClicked = pCurrent;
                    sheet.updateShapesList(shapes);
                }else if(toggleMoveTool2.isSelected()){
                    addShape(shapes.isEmpty() ?
                            new GMoveN(pCurrent, pCurrent, uniqueID++) :
                            new GMoveN(lastClicked, pCurrent, uniqueID++)
                    );
                    lastClicked = pCurrent;
                    sheet.updateShapesList(shapes);
                }else if(toggleCursor.isSelected()){                    
                    inEdition = !inEdition;
                    if(inEdition == true){
                        evtEdition = new EditionCursorEvent(pCurrent, shapes);
                        fireEditionCursorMoved(evtEdition);
                    }else{
                        if(evtEdition != null){
                            evtEdition.getNow(false);
                            // Si le point changé est dans la dernière forme
                            AShape last = shapes.get(shapes.size() - 1); // La dernière forme
                            if(evtEdition.getUniqueIDList().contains(last.getUniqueID()) == true){
                                List<EditionCursorEvent.Change> chs = evtEdition.getWhatHasChanged();
                                EditionCursorEvent.Change lastChange = chs.get(chs.size() - 1);
                                if(lastChange == EditionCursorEvent.Change.End){
                                    lastClicked = pCurrent;
                                }                                
                            }
                            evtEdition = null;
                            fireEditionCursorEndClick();
                        }
                    }
                }
                
                sheet.updateDrawing();
            }
            
        });
        
        spDrawing.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Point pCurrent = new Point(
                        e.getPoint().x - vrule.getWidth() + spDrawing.getHorizontalScrollBar().getValue(),
                        e.getPoint().y - hrule.getHeight() + spDrawing.getVerticalScrollBar().getValue()
                );                
                
                if(toggleCursor.isSelected() && inEdition == true){                    
                    evtEdition.setCursor(pCurrent);
                    fireEditionCursorMoved(evtEdition);
                }
                
                sheet.updateMousePosition(pCurrent.x, pCurrent.y);
                sheet.updateDrawing();
            }
            
        });
        
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                jSplitPane1.setDividerLocation(e.getComponent().getHeight() - 300);
            }
        });
    }
    
    private void addShape(AShape sh){
        // Vérifie si les points de début et de fin sont identique
        if(shapes.size() == 1 && isSamePoint(shapes.get(0).getStartPoint(), shapes.get(0).getEndPoint()) == true){
            // Si on a un point identique on doit changer le point de fin
            AShape oldShape = shapes.get(0);
            oldShape.setStartPoint(sh.getEndPoint());
            // Si on a affaire à une courbe on génère les points de contrôle via le constructeur
            if(oldShape instanceof GCubic){
                oldShape = new GCubic(oldShape.getStartPoint(), oldShape.getEndPoint(), oldShape.getUniqueID());
            }else if(oldShape instanceof GQuadratic){
                oldShape = new GQuadratic(oldShape.getStartPoint(), oldShape.getEndPoint(), oldShape.getUniqueID());
            }else if(oldShape instanceof GSpline){
                
            }
            shapes.clear();
            shapes.add(oldShape);
        }else{
            shapes.add(sh);
        }
    }
    
    private boolean isSamePoint(Point2D a, Point2D b){
        return a.distance(b) == 0d;
    }
    
    private boolean isClosePoint(Point2D a, Point2D b){
        return a.distance(b) < 10d;
    }
    
    private int setSizeOfDrawing(){
        java.awt.Toolkit toolkit = java.awt.Toolkit.getDefaultToolkit();
        java.awt.Dimension dim = toolkit.getScreenSize();
        int big = (int)dim.getWidth();
        if(dim.getWidth()<dim.getHeight()){
            big = (int)dim.getHeight();
        }
        int size = 1000;
        while(size<big){
            size+=500;
        }
        return size;
    }
    
    public void setCursorLabelText(int x, int y){
        jLabel1.setText(x+","+y);
    }
    
    // <editor-fold defaultstate="collapsed" desc="Events">
    //==========================================================================
    // Events
    //==========================================================================

    private final EventListenerList listeners = new EventListenerList();

    public void addEditionCursorListener(EditionCursorListener listener) {
        listeners.add(EditionCursorListener.class, (EditionCursorListener)listener);
    }

    public void removeEditionCursorListener(EditionCursorListener listener) {
        listeners.remove(EditionCursorListener.class, (EditionCursorListener)listener);
    }

    public Object[] getListeners() {
        return listeners.getListenerList();
    }

    protected void fireEditionCursorMoved(EditionCursorEvent event) {
        for(Object o : getListeners()){
            if(o instanceof EditionCursorListener){
                EditionCursorListener listen = (EditionCursorListener)o;
                listen.cursorMoved(event);
                break;
            }
        }
    }
    
    protected void fireEditionCursorBeginClick(EditionCursorEvent event) {
        for(Object o : getListeners()){
            if(o instanceof EditionCursorListener){
                EditionCursorListener listen = (EditionCursorListener)o;
                listen.cursorBeginClick(event);
                break;
            }
        }
    }
    
    protected void fireEditionCursorEndClick() {
        for(Object o : getListeners()){
            if(o instanceof EditionCursorListener){
                EditionCursorListener listen = (EditionCursorListener)o;
                listen.cursorEndClick();
                break;
            }
        }
    }
    // </editor-fold>

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bgTools = new javax.swing.ButtonGroup();
        bgMultiLayers = new javax.swing.ButtonGroup();
        jSplitPane1 = new javax.swing.JSplitPane();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        btnNew = new javax.swing.JButton();
        btnOpen = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnOpenFont = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        btnMoveLeft = new javax.swing.JButton();
        btnMoveUp = new javax.swing.JButton();
        btnOpenImage = new javax.swing.JButton();
        btnMoveCenter = new javax.swing.JButton();
        btnClearImage = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jSlider1 = new javax.swing.JSlider();
        jLabel3 = new javax.swing.JLabel();
        jSlider2 = new javax.swing.JSlider();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jSlider3 = new javax.swing.JSlider();
        jSlider4 = new javax.swing.JSlider();
        btnMoveRight = new javax.swing.JButton();
        toggleGrid = new javax.swing.JToggleButton();
        toggleCursor = new javax.swing.JToggleButton();
        toggleLine = new javax.swing.JToggleButton();
        toggleCubic = new javax.swing.JToggleButton();
        btnMagicWand = new javax.swing.JButton();
        btnMoveBottom = new javax.swing.JButton();
        toggleMoveTool1 = new javax.swing.JToggleButton();
        toggleMoveTool2 = new javax.swing.JToggleButton();
        toggleQuad = new javax.swing.JToggleButton();
        toggleSpline = new javax.swing.JToggleButton();
        toggleSelection = new javax.swing.JToggleButton();
        toggleParll = new javax.swing.JToggleButton();
        togglePerpd = new javax.swing.JToggleButton();
        toggleEyeAnime = new javax.swing.JToggleButton();
        toggleOneLayer = new javax.swing.JToggleButton();
        toggleAllLayers = new javax.swing.JToggleButton();
        toggleMoveOp = new javax.swing.JToggleButton();
        toggleRotateOp = new javax.swing.JToggleButton();
        toggleScaleOp = new javax.swing.JToggleButton();
        toggleShearOp = new javax.swing.JToggleButton();
        btnCopy = new javax.swing.JButton();
        btnPaste = new javax.swing.JButton();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        histoPanel = new javax.swing.JPanel();
        laysPanel = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jSplitPane1.setDividerLocation(600);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jPanel3.setLayout(null);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/32_newdocument.png"))); // NOI18N
        btnNew.setToolTipText("New drawing");
        jPanel3.add(btnNew);
        btnNew.setBounds(0, 0, 40, 40);

        btnOpen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/32_folder.png"))); // NOI18N
        btnOpen.setToolTipText("Open a drawing");
        jPanel3.add(btnOpen);
        btnOpen.setBounds(40, 0, 40, 40);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/32_floppydisk.png"))); // NOI18N
        btnSave.setToolTipText("Save a drawing");
        jPanel3.add(btnSave);
        btnSave.setBounds(80, 0, 40, 40);

        btnOpenFont.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/32px-Crystal_Clear_mimetype_font_type1.png"))); // NOI18N
        btnOpenFont.setToolTipText("Open a font glyph");
        jPanel3.add(btnOpenFont);
        btnOpenFont.setBounds(120, 0, 40, 40);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("0,0");
        jPanel3.add(jLabel1);
        jLabel1.setBounds(160, 0, 80, 40);

        btnMoveLeft.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/f04.gif"))); // NOI18N
        btnMoveLeft.setToolTipText("Move an image to the left");
        jPanel3.add(btnMoveLeft);
        btnMoveLeft.setBounds(0, 180, 40, 40);

        btnMoveUp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/f02.gif"))); // NOI18N
        btnMoveUp.setToolTipText("Move the image to the up");
        jPanel3.add(btnMoveUp);
        btnMoveUp.setBounds(40, 140, 40, 40);

        btnOpenImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/32px-Crystal_Clear_app_kpaint.png"))); // NOI18N
        btnOpenImage.setToolTipText("Open an image");
        jPanel3.add(btnOpenImage);
        btnOpenImage.setBounds(80, 140, 40, 40);

        btnMoveCenter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/f05.gif"))); // NOI18N
        btnMoveCenter.setToolTipText("Center an image");
        jPanel3.add(btnMoveCenter);
        btnMoveCenter.setBounds(40, 180, 40, 40);

        btnClearImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/32px-Crystal_Clear_app_windows_users.png"))); // NOI18N
        btnClearImage.setToolTipText("Clear an image");
        jPanel3.add(btnClearImage);
        btnClearImage.setBounds(120, 140, 40, 40);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Alpha of layer :");
        jPanel3.add(jLabel2);
        jLabel2.setBounds(0, 40, 120, 20);
        jPanel3.add(jSlider1);
        jSlider1.setBounds(0, 60, 120, 26);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Size of display :");
        jPanel3.add(jLabel3);
        jLabel3.setBounds(120, 40, 120, 20);
        jPanel3.add(jSlider2);
        jSlider2.setBounds(120, 60, 120, 26);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Alpha of image :");
        jPanel3.add(jLabel4);
        jLabel4.setBounds(0, 90, 120, 20);

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Size of image :");
        jPanel3.add(jLabel5);
        jLabel5.setBounds(120, 90, 120, 20);
        jPanel3.add(jSlider3);
        jSlider3.setBounds(0, 110, 120, 26);
        jPanel3.add(jSlider4);
        jSlider4.setBounds(120, 110, 120, 26);

        btnMoveRight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/f06.gif"))); // NOI18N
        btnMoveRight.setToolTipText("Move an image to the right");
        jPanel3.add(btnMoveRight);
        btnMoveRight.setBounds(80, 180, 40, 40);

        toggleGrid.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/gridlocker.png"))); // NOI18N
        toggleGrid.setToolTipText("De/Activate grid");
        jPanel3.add(toggleGrid);
        toggleGrid.setBounds(0, 140, 40, 40);

        bgTools.add(toggleCursor);
        toggleCursor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/32_cur.png"))); // NOI18N
        toggleCursor.setSelected(true);
        toggleCursor.setToolTipText("Select the cursor");
        jPanel3.add(toggleCursor);
        toggleCursor.setBounds(120, 180, 40, 40);

        bgTools.add(toggleLine);
        toggleLine.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/AFM-DrawingLine.png"))); // NOI18N
        toggleLine.setToolTipText("Line tool");
        jPanel3.add(toggleLine);
        toggleLine.setBounds(160, 180, 40, 40);

        bgTools.add(toggleCubic);
        toggleCubic.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/AFM-DrawingBezier.png"))); // NOI18N
        toggleCubic.setToolTipText("Cubic tool");
        jPanel3.add(toggleCubic);
        toggleCubic.setBounds(200, 180, 40, 40);

        btnMagicWand.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/magic-wand.png"))); // NOI18N
        btnMagicWand.setToolTipText("Magic wand tool");
        jPanel3.add(btnMagicWand);
        btnMagicWand.setBounds(0, 220, 40, 40);

        btnMoveBottom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/f08.gif"))); // NOI18N
        btnMoveBottom.setToolTipText("Move an image to the bottom");
        jPanel3.add(btnMoveBottom);
        btnMoveBottom.setBounds(40, 220, 40, 40);

        bgTools.add(toggleMoveTool1);
        toggleMoveTool1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/move m.png"))); // NOI18N
        toggleMoveTool1.setToolTipText("Move tool (m ass command)");
        jPanel3.add(toggleMoveTool1);
        toggleMoveTool1.setBounds(80, 220, 40, 40);

        bgTools.add(toggleMoveTool2);
        toggleMoveTool2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/move n.png"))); // NOI18N
        toggleMoveTool2.setToolTipText("Move tool (n ass command)");
        jPanel3.add(toggleMoveTool2);
        toggleMoveTool2.setBounds(120, 220, 40, 40);

        bgTools.add(toggleQuad);
        toggleQuad.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/afm splines 03.png"))); // NOI18N
        toggleQuad.setToolTipText("Quadratic tool");
        jPanel3.add(toggleQuad);
        toggleQuad.setBounds(160, 220, 40, 40);

        bgTools.add(toggleSpline);
        toggleSpline.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/AFM-DrawingBSpline.png"))); // NOI18N
        toggleSpline.setToolTipText("Spline tool");
        jPanel3.add(toggleSpline);
        toggleSpline.setBounds(200, 220, 40, 40);

        bgTools.add(toggleSelection);
        toggleSelection.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/32px-Selection.png"))); // NOI18N
        toggleSelection.setToolTipText("Selection tool");
        jPanel3.add(toggleSelection);
        toggleSelection.setBounds(0, 260, 40, 40);

        bgTools.add(toggleParll);
        toggleParll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/32_parallele.png"))); // NOI18N
        toggleParll.setToolTipText("Parll tool");
        jPanel3.add(toggleParll);
        toggleParll.setBounds(40, 260, 40, 40);

        bgTools.add(togglePerpd);
        togglePerpd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/32_perpendiculaire.png"))); // NOI18N
        togglePerpd.setToolTipText("Perpd tool");
        jPanel3.add(togglePerpd);
        togglePerpd.setBounds(80, 260, 40, 40);

        toggleEyeAnime.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/32_eye.png"))); // NOI18N
        toggleEyeAnime.setToolTipText("Eye tool");
        jPanel3.add(toggleEyeAnime);
        toggleEyeAnime.setBounds(120, 260, 40, 40);

        bgMultiLayers.add(toggleOneLayer);
        toggleOneLayer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/32_layers_just_one.png"))); // NOI18N
        toggleOneLayer.setSelected(true);
        toggleOneLayer.setToolTipText("View only one layer");
        jPanel3.add(toggleOneLayer);
        toggleOneLayer.setBounds(160, 260, 40, 40);

        bgMultiLayers.add(toggleAllLayers);
        toggleAllLayers.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/32_layers_three.png"))); // NOI18N
        toggleAllLayers.setToolTipText("View all layers");
        jPanel3.add(toggleAllLayers);
        toggleAllLayers.setBounds(200, 260, 40, 40);

        bgTools.add(toggleMoveOp);
        toggleMoveOp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/AFM-translate.png"))); // NOI18N
        toggleMoveOp.setToolTipText("Operation - translate - tool");
        jPanel3.add(toggleMoveOp);
        toggleMoveOp.setBounds(0, 300, 40, 40);

        bgTools.add(toggleRotateOp);
        toggleRotateOp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/AFM-rotate.png"))); // NOI18N
        toggleRotateOp.setToolTipText("Operation - rotate - tool");
        jPanel3.add(toggleRotateOp);
        toggleRotateOp.setBounds(40, 300, 40, 40);

        bgTools.add(toggleScaleOp);
        toggleScaleOp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/AFM-scale.png"))); // NOI18N
        toggleScaleOp.setToolTipText("Operation - scale - tool");
        jPanel3.add(toggleScaleOp);
        toggleScaleOp.setBounds(80, 300, 40, 40);

        bgTools.add(toggleShearOp);
        toggleShearOp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/AFM-shear.png"))); // NOI18N
        toggleShearOp.setToolTipText("Operation - shear - tool");
        jPanel3.add(toggleShearOp);
        toggleShearOp.setBounds(120, 300, 40, 40);

        btnCopy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/32px-Crystal_Clear_action_editcopy.png"))); // NOI18N
        btnCopy.setToolTipText("Copy");
        jPanel3.add(btnCopy);
        btnCopy.setBounds(160, 300, 40, 40);

        btnPaste.setIcon(new javax.swing.ImageIcon(getClass().getResource("/feuille/images/32px-Crystal_Clear_action_editpaste.png"))); // NOI18N
        btnPaste.setToolTipText("Paste");
        jPanel3.add(btnPaste);
        btnPaste.setBounds(200, 300, 40, 40);

        javax.swing.GroupLayout histoPanelLayout = new javax.swing.GroupLayout(histoPanel);
        histoPanel.setLayout(histoPanelLayout);
        histoPanelLayout.setHorizontalGroup(
            histoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        histoPanelLayout.setVerticalGroup(
            histoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jTabbedPane3.addTab("Historic", histoPanel);

        javax.swing.GroupLayout laysPanelLayout = new javax.swing.GroupLayout(laysPanel);
        laysPanel.setLayout(laysPanelLayout);
        laysPanelLayout.setHorizontalGroup(
            laysPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        laysPanelLayout.setVerticalGroup(
            laysPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jTabbedPane3.addTab("Layers", laysPanel);

        jPanel6.setBackground(new java.awt.Color(204, 153, 255));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 903, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTabbedPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE))
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Drawing", jPanel1);

        jSplitPane1.setTopComponent(jTabbedPane1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1152, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 211, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("tab1", jPanel2);

        jSplitPane1.setRightComponent(jTabbedPane2);

        getContentPane().add(jSplitPane1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgMultiLayers;
    private javax.swing.ButtonGroup bgTools;
    private javax.swing.JButton btnClearImage;
    private javax.swing.JButton btnCopy;
    private javax.swing.JButton btnMagicWand;
    private javax.swing.JButton btnMoveBottom;
    private javax.swing.JButton btnMoveCenter;
    private javax.swing.JButton btnMoveLeft;
    private javax.swing.JButton btnMoveRight;
    private javax.swing.JButton btnMoveUp;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnOpen;
    private javax.swing.JButton btnOpenFont;
    private javax.swing.JButton btnOpenImage;
    private javax.swing.JButton btnPaste;
    private javax.swing.JButton btnSave;
    private javax.swing.JPanel histoPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JSlider jSlider2;
    private javax.swing.JSlider jSlider3;
    private javax.swing.JSlider jSlider4;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JPanel laysPanel;
    private javax.swing.JToggleButton toggleAllLayers;
    private javax.swing.JToggleButton toggleCubic;
    private javax.swing.JToggleButton toggleCursor;
    private javax.swing.JToggleButton toggleEyeAnime;
    private javax.swing.JToggleButton toggleGrid;
    private javax.swing.JToggleButton toggleLine;
    private javax.swing.JToggleButton toggleMoveOp;
    private javax.swing.JToggleButton toggleMoveTool1;
    private javax.swing.JToggleButton toggleMoveTool2;
    private javax.swing.JToggleButton toggleOneLayer;
    private javax.swing.JToggleButton toggleParll;
    private javax.swing.JToggleButton togglePerpd;
    private javax.swing.JToggleButton toggleQuad;
    private javax.swing.JToggleButton toggleRotateOp;
    private javax.swing.JToggleButton toggleScaleOp;
    private javax.swing.JToggleButton toggleSelection;
    private javax.swing.JToggleButton toggleShearOp;
    private javax.swing.JToggleButton toggleSpline;
    // End of variables declaration//GEN-END:variables
}
