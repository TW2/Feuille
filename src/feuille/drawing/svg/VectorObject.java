/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.drawing.svg;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import feuille.drawing.adf.LayerContent;

/**
 *
 * @author The Wingate 2940
 */
public class VectorObject {
    
    private List<LayerContent> layers = new ArrayList<LayerContent>();
    
    public VectorObject(){
        
    }
    
    //**************************************************************************
    //------------------------------------------------------------------ COUCHES
    //**************************************************************************
    
    public void addLayer(String name, Color c, String commands){
        LayerContent lc = new LayerContent(name, c, commands);
        layers.add(lc);
    }
    
    public void addLayer(LayerContent lc){
        layers.add(lc);
    }
    
    public List<LayerContent> getLayers(){
        return layers;
    }
    
}
