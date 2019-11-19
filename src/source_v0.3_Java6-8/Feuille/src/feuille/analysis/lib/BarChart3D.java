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
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author The Wingate 2940
 */
public class BarChart3D extends JPanel {
    
    /**
     * Create a new pie chart with the data
     * @param theme The title of this chart
     * @param report The categories with their percent or number
     */
    public BarChart3D(String theme, Map<String, Integer> report){        
        //Set the chart data
        DefaultCategoryDataset bardataset = new DefaultCategoryDataset();
        for(String key : report.keySet()){
            bardataset.setValue(report.get(key), key, key);
        }
        
        //Set the chart configuration
        JFreeChart chart = ChartFactory.createBarChart3D(theme, "", "Lines", bardataset, PlotOrientation.VERTICAL, true, true, false);
        ImageIcon iicon = new ImageIcon(getClass().getResource("background.png"));
        chart.getPlot().setBackgroundImage(iicon.getImage());
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
