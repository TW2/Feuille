/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feuille.analysis.lib;

import java.awt.Color;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

/**
 *
 * @author The Wingate 2940
 */
public class PieChart3D extends JPanel {
    
    /**
     * Create a new pie chart with the data
     * @param theme The title of this chart
     * @param report The categories with their percent or number
     */
    public PieChart3D(String theme, Map<String, Integer> report){        
        //Set the chart data
        DefaultPieDataset piedataset = new DefaultPieDataset();
        for(String key : report.keySet()){
            piedataset.setValue(key, report.get(key));
        }
        
        //Set the chart configuration
        JFreeChart chart = ChartFactory.createPieChart3D(theme, piedataset, true, true, false);
        ImageIcon iicon = new ImageIcon(getClass().getResource("background.png"));
        chart.getPlot().setBackgroundImage(iicon.getImage());
        chart.getPlot().setForegroundAlpha(0.8f);
        ChartPanel cp = new ChartPanel(chart);
        
        //Add the chart to our panel
        setLayout(null);
        cp.setSize(500, 250);
        add(cp);
        
        //Our panel
        setSize(500,250);
        setBackground(Color.white);
    }
}
