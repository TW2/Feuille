/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package smallboxforfansub.karaoke.renderer;

import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import smallboxforfansub.karaoke.lib.FxObject;
import smallboxforfansub.karaoke.lib.ParticleObject;

/**
 *
 * @author Unknown User
 */
public class treeFxRenderer extends javax.swing.tree.DefaultTreeCellRenderer {

    private Icon rubyIcon, xmlIcon, particleIcon, pythonIcon;

    public treeFxRenderer(){

    }

    public treeFxRenderer(Icon rubyIcon, Icon xmlIcon, Icon particleIcon, Icon pythonIcon){
        this.rubyIcon = rubyIcon;
        this.xmlIcon = xmlIcon;
        this.particleIcon = particleIcon;
        this.pythonIcon = pythonIcon;
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
            
            if(leaf){//If it isn't a folder. If it is an user object.
                if(tn.getUserObject() instanceof FxObject){//Ruby, Python, XFX
                    FxObject fxo = (FxObject)tn.getUserObject();
                    if(fxo.getFxObjectType() == FxObject.FxObjectType.Ruby){
                        setIcon(rubyIcon);
                    }else if(fxo.getFxObjectType() == FxObject.FxObjectType.Python){
                        setIcon(pythonIcon);
                    }else if(fxo.getFxObjectType() == FxObject.FxObjectType.XMLPreset){
                        setIcon(xmlIcon);
                    }
                    setText(fxo.getName());
                }else if(tn.getUserObject() instanceof  ParticleObject){//Particle
                    ParticleObject po = (ParticleObject)tn.getUserObject();
                    setIcon(particleIcon);
                    setText(po.getName());
                }
            }
            
            
        }

        return this;
    }

    public void setRubyIcon(Icon rubyIcon){
        this.rubyIcon = rubyIcon;
    }

    public void setXmlIcon(Icon xmlIcon){
        this.xmlIcon = xmlIcon;
    }
    
    public void setParticleIcon(Icon particleIcon){
        this.particleIcon = particleIcon;
    }
    
    public void setPythonIcon(Icon pythonIcon){
        this.pythonIcon = pythonIcon;
    }
    
}
