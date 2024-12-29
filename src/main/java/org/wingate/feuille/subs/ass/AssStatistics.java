/*
 * Copyright (C) 2024 util2
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.wingate.feuille.subs.ass;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.table.TableCellRenderer;
import org.wingate.feuille.util.DrawColor;

/**
 *
 * @author util2
 */
public class AssStatistics {
    private AssEvent event;

    public AssStatistics() {
    }

    public AssEvent getEvent() {
        return event;
    }

    public void setEvent(AssEvent event) {
        this.event = event;
    }
    
    public static class Renderer extends JPanel implements TableCellRenderer {
        
        private final JPanel panCPL = new JPanel();
        private final JPanel panCPS = new JPanel();
        private final JLabel lblCPLText = new JLabel("");
        private final JLabel lblCPLColor = new JLabel("");
        private final JLabel lblCPSText = new JLabel("");
        private final JLabel lblCPSColor = new JLabel("");
        // 60 characters by line for highest inline
        // Minimum: 3, Maximum: 5000, Threshold: wanted good value
        private float cplThreshold = 60f;

        public Renderer() {
            setLayout(new GridLayout(2, 1, 1, 2));
            add(panCPL);
            add(panCPS);
            
            lblCPLText.setOpaque(true);
            lblCPSText.setOpaque(true);
            lblCPLColor.setOpaque(true);
            lblCPSColor.setOpaque(true);
            
            panCPL.setLayout(new BorderLayout());
            panCPL.add(lblCPLText, BorderLayout.CENTER);
            panCPL.add(lblCPLColor, BorderLayout.EAST);
            
            panCPS.setLayout(new BorderLayout());
            panCPS.add(lblCPSText, BorderLayout.CENTER);
            panCPS.add(lblCPSColor, BorderLayout.EAST);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            if(value instanceof AssStatistics x){
                Color bg;
                if(x.getEvent().getType() == AssEvent.Type.Comment){
                    bg = DrawColor.violet.getColor();
                }else{
                    // Get table background (avoid searching from any other way)
                    // FlatLaf properties >> Table.background
                    bg = UIManager.getColor("Table.background");
                }
                
                // Get table foreground (avoid searching from any other way)
                // FlatLaf properties >> Table.foreground
                Color fg = UIManager.getColor("Table.foreground");
                
                if(isSelected){
                    bg = UIManager.getColor("Table.selectionBackground");
                    fg = UIManager.getColor("Table.selectionForeground");
                }
                
                setBackground(bg);
                
                // Get height of cell
                int size = table.getRowHeight(row) / 2;
                
                // Set text (CPL)
                lblCPLText.setHorizontalAlignment(SwingConstants.CENTER);
                String cpl = Integer.toString(Math.round(x.getEvent().getCPL(row)));
                lblCPLText.setText(cpl);
                lblCPLText.setBackground(bg);
                lblCPLText.setForeground(fg);                
                lblCPLColor.setPreferredSize(new java.awt.Dimension(size, size));                
                // Set background color to border (make fake background)
                lblCPLColor.setBorder(new LineBorder(bg, size / 4));
                // Set stats color to real background
                lblCPLColor.setBackground(getCplStats(x.getEvent().getCPL(row)));
                setToolTipText(String.format("%s %.2f", "CPL", x.getEvent().getCPL(row)));
                
                // Set text (CPS)
                lblCPSText.setHorizontalAlignment(SwingConstants.CENTER);
                lblCPSText.setText(String.format("%.2f", x.getEvent().getCPS(row)));
                lblCPSText.setBackground(bg);
                lblCPSText.setForeground(fg);
                lblCPSColor.setPreferredSize(new java.awt.Dimension(size, size));                
                // Set background color to border (make fake background)
                lblCPSColor.setBorder(new LineBorder(bg, size / 4));
                // Set stats color to real background
                lblCPSColor.setBackground(getCpsStats(x.getEvent().getCPS(row)));
                setToolTipText(String.format("%s %.2f", "CPS", x.getEvent().getCPS(row)));
            }
            
            return this;
        }
        
        private Color getCpsStats(float cps){
            Color R = Color.red;
            Color Y = Color.yellow;
            Color G = Color.green;
            // Red to Yellow to Green
            // 0   to 0.5    to 1
            if(cps == 0.0f) return R;
            if(cps == 0.5f) return Y;
            if(cps >= 1.0f) return G;

            if(0.0f < cps && cps < 0.5f){
                float v = cps * 2f;
                return new Color(
                        (int)((R.getRed() * v) + (Y.getRed() * (1 - v))),
                        (int)((R.getGreen() * v) + (Y.getGreen() * (1 - v))),
                        (int)((R.getBlue() * v) + (Y.getBlue() * (1 - v)))
                );
            }else{
                float v = (cps - 0.5f) * 2f;
                return new Color(
                        (int)((Y.getRed() * v) + (G.getRed() * (1 - v))),
                        (int)((Y.getGreen() * v) + (G.getGreen() * (1 - v))),
                        (int)((Y.getBlue() * v) + (G.getBlue() * (1 - v)))
                );
            }
        }
        
        private Color getCplStats(float cpl){
            // Minimum      0.0f >> 0 (green)
            // Threshold    0.5f >> threshold (yellow)
            // Maximum      1.0f >> threshold x2 (red)
            float max = cplThreshold * 2f;
            float u = cpl / max;
            
            Color R = Color.red;
            Color Y = Color.yellow;
            Color G = Color.green;
            // Green to Yellow to Red
            // 0     to 0.5    to 1
            if(u <= 0.0f) return G;
            if(u == 0.5f) return Y;
            if(u >= 1.0f) return R;

            if(0.0f < u && u < 0.5f){
                float v = u * 2f;
                return new Color(
                        (int)((G.getRed() * v) + (Y.getRed() * (1 - v))),
                        (int)((G.getGreen() * v) + (Y.getGreen() * (1 - v))),
                        (int)((G.getBlue() * v) + (Y.getBlue() * (1 - v)))
                );
            }else{
                float v = (u - 0.5f) * 2f;
                return new Color(
                        (int)((Y.getRed() * v) + (R.getRed() * (1 - v))),
                        (int)((Y.getGreen() * v) + (R.getGreen() * (1 - v))),
                        (int)((Y.getBlue() * v) + (R.getBlue() * (1 - v)))
                );
            }
        }

        public float getCplThreshold() {
            return cplThreshold;
        }

        public void setCplThreshold(float cplThreshold) {
            this.cplThreshold = cplThreshold < 3 ? 3 : (cplThreshold > 5000 ? 5000 : cplThreshold);
        }
        
    }
}
