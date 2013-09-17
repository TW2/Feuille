/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smallboxforfansub.karaoke.renderer;

import java.awt.Component;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import smallboxforfansub.karaoke.xfxintegration.OIntegration;

/**
 *
 * @author The Wingate 2940
 */
public class XFXIntTreeRenderer extends DefaultTreeCellRenderer {
    
    public XFXIntTreeRenderer(){
        
    }
    
    @Override
    public Component getTreeCellRendererComponent(
            JTree tree, Object value, boolean sel, boolean expanded,
            boolean leaf, int row, boolean hasFocus) {
        
        super.getTreeCellRendererComponent(
                        tree, value, sel,
                        expanded, leaf, row,
                        hasFocus);
        
        if(value instanceof DefaultMutableTreeNode){
            DefaultMutableTreeNode tn = (DefaultMutableTreeNode)value;
            if(leaf){
                if(tn.getUserObject() instanceof OIntegration){
                    OIntegration oi = (OIntegration)tn.getUserObject();
                    setText(oi.getName());
                    setIcon(oi.getIcon());
                }
            }
        }
        
        return this;
    }
    
}
