/* VisNow
 Copyright (C) 2006-2013 University of Warsaw, ICM

 This file is part of GNU Classpath.

 GNU Classpath is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2, or (at your option)
 any later version.

 GNU Classpath is distributed in the hope that it will be useful, but
 WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with GNU Classpath; see the file COPYING.  If not, write to the 
 University of Warsaw, Interdisciplinary Centre for Mathematical and 
 Computational Modelling, Pawinskiego 5a, 02-106 Warsaw, Poland. 

 Linking this library statically or dynamically with other modules is
 making a combined work based on this library.  Thus, the terms and
 conditions of the GNU General Public License cover the whole
 combination.

 As a special exception, the copyright holders of this library give you
 permission to link this library with independent modules to produce an
 executable, regardless of the license terms of these independent
 modules, and to copy and distribute the resulting executable under
 terms of your choice, provided that you also meet, for each linked
 independent module, the terms and conditions of the license of that
 module.  An independent module is a module which is not derived from
 or based on this library.  If you modify this library, you may extend
 this exception to your version of the library, but you are not
 obligated to do so.  If you do not wish to do so, delete this
 exception statement from your version. */
package pl.edu.icm.visnow.lib.basic.utilities.KernelEditor;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Michal Lyczek (lyczek@icm.edu.pl)
 * @author Piotr Wendykier (piotrw@icm.edu.pl)
 */
public class GUI extends javax.swing.JPanel {

    private Params params = new Params();

    public GUI() {
        initComponents();
        setTable();
    }

    public Params getParams() {
        return params;
    }

    public void setParams(Params params) {
        this.params = params;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        rSlider = new javax.swing.JSlider();
        normalizeCheckBox = new javax.swing.JCheckBox();
        jLabel5 = new javax.swing.JLabel();
        presetCombo = new javax.swing.JComboBox();
        gaussianWidthSlider = new pl.edu.icm.visnow.gui.widgets.FloatSlider();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTabbedPane1 = new javax.swing.JTabbedPane();

        setLayout(new java.awt.GridBagLayout());

        jLabel1.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel1.setText("Dimensions");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 5, 5);
        add(jLabel1, gridBagConstraints);

        jComboBox1.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1d", "2d", "3d" }));
        jComboBox1.setSelectedIndex(1);
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
        add(jComboBox1, gridBagConstraints);

        rSlider.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        rSlider.setMajorTickSpacing(2);
        rSlider.setMaximum(10);
        rSlider.setMinimum(1);
        rSlider.setMinorTickSpacing(1);
        rSlider.setPaintLabels(true);
        rSlider.setPaintTicks(true);
        rSlider.setSnapToTicks(true);
        rSlider.setValue(1);
        rSlider.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "radius", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12))); // NOI18N
        rSlider.setName(""); // NOI18N
        rSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rSliderStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
        add(rSlider, gridBagConstraints);

        normalizeCheckBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        normalizeCheckBox.setText("Normalize");
        normalizeCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        normalizeCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        normalizeCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                normalizeCheckBoxItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
        add(normalizeCheckBox, gridBagConstraints);

        jLabel5.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel5.setText("Preset masks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 5, 5);
        add(jLabel5, gridBagConstraints);

        presetCombo.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        presetCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "constant", "gaussian", "linear", "conical", "custom" }));
        presetCombo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                presetComboItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
        add(presetCombo, gridBagConstraints);

        gaussianWidthSlider.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "gausian sigma", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12))); // NOI18N
        gaussianWidthSlider.setEnabled(false);
        gaussianWidthSlider.setMax(2.0F);
        gaussianWidthSlider.setMin(0.5F);
        gaussianWidthSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                gaussianWidthSliderStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
        add(gaussianWidthSlider, gridBagConstraints);

        jScrollPane1.setViewportView(jTabbedPane1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 200;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        add(jScrollPane1, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        int rank = 2;
        switch (jComboBox1.getSelectedIndex()) {
            case 0:
                rank = 1;
                break;
            case 1:
                rank = 2;
                break;
            case 2:
                rank = 3;
                break;
        }
        if (rank != params.getRank()) {
            params.setRank(rank);
        }
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void rSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rSliderStateChanged
        if (!rSlider.getValueIsAdjusting()) {
            int rxsize = rSlider.getValue();
            if (rxsize != params.getRadius()) {
                params.setRadius(rxsize);

            }
        }

}//GEN-LAST:event_rSliderStateChanged

    private void normalizeCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_normalizeCheckBoxItemStateChanged
        boolean normalized = params.isNormalized();
        if (normalized != normalizeCheckBox.isSelected()) {
            params.setNormalized(normalizeCheckBox.isSelected());
        }
}//GEN-LAST:event_normalizeCheckBoxItemStateChanged

private void presetComboItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_presetComboItemStateChanged
    try {
        gaussianWidthSlider.setEnabled(presetCombo.getSelectedIndex() == 1);
        int type = params.getKernelType();
        if (type != presetCombo.getSelectedIndex()) {
            params.setKernelType(presetCombo.getSelectedIndex());
        }
    } catch (ArrayIndexOutOfBoundsException ex) {
    }

}//GEN-LAST:event_presetComboItemStateChanged

private void gaussianWidthSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_gaussianWidthSliderStateChanged
    if (presetCombo.getSelectedIndex() == 1) {
        if (!gaussianWidthSlider.isAdjusting()) {
            float gaussianSigma = params.getGaussianSigma();
            if (gaussianSigma != gaussianWidthSlider.getVal()) {
                params.setGaussianSigma(gaussianWidthSlider.getVal());
            }
        }
    }
}//GEN-LAST:event_gaussianWidthSliderStateChanged

    public void setTable() {
        try {
            jTabbedPane1.removeAll();

            int size = 2 * params.getRadius() + 1;
            float[] data = params.getKernel();
            int rank = params.getRank();
            JTable[] tables;
            if (rank == 3) {
                tables = new JTable[size];
            } else {
                tables = new JTable[1];
            }

            if (rank == 3) {
                for (int z = 0; z < size; z++) {
                    tables[z] = new JTable();
                    DefaultTableModel defaultModel = new DefaultTableModel(size, size);

                    tables[z].setModel(defaultModel);
                    tables[z].setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                    for (int i = 0; i < tables[z].getColumnModel().getColumnCount(); i++) {
                        tables[z].getColumnModel().getColumn(i).setPreferredWidth(40);
                    }
                    for (int y = 0; y < size; y++) {
                        for (int x = 0; x < size; x++) {
                            float elem = data[z * size * size + y * size + x];
                            tables[z].setValueAt(String.format("%4.2f", elem), y, x);
                        }
                    }
                    if (params.getKernelType() == Params.CUSTOM) {
                        tables[z].setEnabled(true);
                        tables[z].addKeyListener(new MyKeyListener());
                    } else {
                        tables[z].setEnabled(false);
                    }
                    jTabbedPane1.addTab("" + z, tables[z]);
                }
            } else if (rank == 2) {
                tables[0] = new JTable();
                DefaultTableModel defaultModel = new DefaultTableModel(size, size);
                tables[0].setModel(defaultModel);
                tables[0].setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                for (int i = 0; i < tables[0].getColumnModel().getColumnCount(); i++) {
                    tables[0].getColumnModel().getColumn(i).setPreferredWidth(40);
                }
                for (int y = 0; y < size; y++) {
                    for (int x = 0; x < size; x++) {
                        float elem = data[y * size + x];
                        tables[0].setValueAt(String.format("%4.2f", elem), y, x);
                    }
                }
                if (params.getKernelType() == Params.CUSTOM) {
                    tables[0].setEnabled(true);
                    tables[0].addKeyListener(new MyKeyListener());
                } else {
                    tables[0].setEnabled(false);
                }
                jTabbedPane1.addTab("0", tables[0]);
            } else if (rank == 1) {
                tables[0] = new JTable();
                DefaultTableModel defaultModel = new DefaultTableModel(1, size);
                tables[0].setModel(defaultModel);
                tables[0].setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                for (int i = 0; i < tables[0].getColumnModel().getColumnCount(); i++) {
                    tables[0].getColumnModel().getColumn(i).setPreferredWidth(40);
                }
                for (int x = 0; x < size; x++) {
                    float elem = data[x];
                    tables[0].setValueAt(String.format("%4.2f", elem), 0, x);
                }
                if (params.getKernelType() == Params.CUSTOM) {
                    tables[0].setEnabled(true);
                    tables[0].addKeyListener(new MyKeyListener());
                } else {
                    tables[0].setEnabled(false);
                }
                jTabbedPane1.addTab("0", tables[0]);
            }
        } catch (ArrayIndexOutOfBoundsException a) {
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private pl.edu.icm.visnow.gui.widgets.FloatSlider gaussianWidthSlider;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JCheckBox normalizeCheckBox;
    private javax.swing.JComboBox presetCombo;
    private javax.swing.JSlider rSlider;
    // End of variables declaration//GEN-END:variables

    private void updateOutField() {
        try {
            if (params.getKernelType() == Params.CUSTOM) {
                int size = 2 * params.getRadius() + 1;
                float[] data = params.getKernel();
                int rank = params.getRank();
                int i = 0;
                if (rank == 3) {
                    for (int z = 0; z < size; z++) {
                        for (int y = 0; y < size; y++) {
                            for (int x = 0; x < size; x++, i++) {
                                data[i] = Float.valueOf((String) ((JTable) jTabbedPane1.getComponentAt(z)).getValueAt(y, x));

                            }

                        }
                    }
                } else if (rank == 2) {
                    for (int y = 0; y < size; y++) {
                        for (int x = 0; x < size; x++, i++) {
                            data[i] = Float.valueOf((String) ((JTable) jTabbedPane1.getComponentAt(0)).getValueAt(y, x));

                        }
                    }
                } else if (rank == 1) {
                    for (int x = 0; x < size; x++, i++) {
                        data[i] = Float.valueOf((String) ((JTable) jTabbedPane1.getComponentAt(0)).getValueAt(0, x));

                    }
                }
                params.setKernel(data);
            }
        } catch (ArrayIndexOutOfBoundsException a) {
        }
    }

    class MyKeyListener implements KeyListener {

        @Override
        public void keyPressed(KeyEvent e) {
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == 10) {
                updateOutField();
                params.fireStateChanged();
            }
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }
    }

    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.add(new GUI());
        f.pack();
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

}
