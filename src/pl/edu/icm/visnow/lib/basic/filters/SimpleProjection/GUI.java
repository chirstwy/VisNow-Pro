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

package pl.edu.icm.visnow.lib.basic.filters.SimpleProjection;

import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author  Bartosz Borucki (babor@icm.edu.pl)
 * Warsaw University, Interdisciplinary Centre
 * for Mathematical and Computational Modelling
 */
public class GUI extends javax.swing.JPanel {
    private Params params = new Params();
    private boolean silent = false;

    private int[] inFieldDims = null;
    
    
    /** Creates new form GUI */
    public GUI() {
        initComponents();
        DefaultComboBoxModel cbm = new DefaultComboBoxModel(Params.methodsStrings);
        methodCB.setModel(cbm);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        methodCB = new javax.swing.JComboBox();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        xRB_L0 = new javax.swing.JRadioButton();
        yRB_L0 = new javax.swing.JRadioButton();
        zRB_L0 = new javax.swing.JRadioButton();
        jPanel2 = new javax.swing.JPanel();
        secondLevelCB = new javax.swing.JCheckBox();
        xRB_L1 = new javax.swing.JRadioButton();
        yRB_L1 = new javax.swing.JRadioButton();
        zRB_L1 = new javax.swing.JRadioButton();

        setLayout(new java.awt.GridBagLayout());

        jPanel3.setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Regular field projection");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        jPanel3.add(jLabel1, gridBagConstraints);

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel3.setText("projection method:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 0, 0);
        jPanel3.add(jLabel3, gridBagConstraints);

        methodCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        methodCB.setMinimumSize(new java.awt.Dimension(150, 24));
        methodCB.setPreferredSize(new java.awt.Dimension(150, 24));
        methodCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                methodCBActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 10);
        jPanel3.add(methodCB, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        add(jPanel3, gridBagConstraints);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel4.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel4.setText("first projection axis:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel1.add(jLabel4, gridBagConstraints);

        buttonGroup1.add(xRB_L0);
        xRB_L0.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        xRB_L0.setText("x");
        xRB_L0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xRB_L0ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel1.add(xRB_L0, gridBagConstraints);

        buttonGroup1.add(yRB_L0);
        yRB_L0.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        yRB_L0.setText("y");
        yRB_L0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yRB_L0ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel1.add(yRB_L0, gridBagConstraints);

        buttonGroup1.add(zRB_L0);
        zRB_L0.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        zRB_L0.setText("z");
        zRB_L0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zRB_L0ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel1.add(zRB_L0, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 0, 0);
        add(jPanel1, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        secondLevelCB.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        secondLevelCB.setText(" second projection axis");
        secondLevelCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                secondLevelCBActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        jPanel2.add(secondLevelCB, gridBagConstraints);

        buttonGroup2.add(xRB_L1);
        xRB_L1.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        xRB_L1.setSelected(true);
        xRB_L1.setText("x");
        xRB_L1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xRB_L1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel2.add(xRB_L1, gridBagConstraints);

        buttonGroup2.add(yRB_L1);
        yRB_L1.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        yRB_L1.setText("y");
        yRB_L1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yRB_L1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel2.add(yRB_L1, gridBagConstraints);

        buttonGroup2.add(zRB_L1);
        zRB_L1.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        zRB_L1.setText("z");
        zRB_L1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zRB_L1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel2.add(zRB_L1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 0, 0);
        add(jPanel2, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void updateGUI() {
        silent = true;
        if(inFieldDims == null || inFieldDims.length == 1) {
            methodCB.setEnabled(false);
            xRB_L0.setEnabled(false);
            yRB_L0.setEnabled(false);
            zRB_L0.setEnabled(false);
            secondLevelCB.setEnabled(false);
            xRB_L1.setEnabled(false);
            yRB_L1.setEnabled(false);
            zRB_L1.setEnabled(false);
            silent = false;
            return;
        }

        methodCB.setEnabled(true);
        methodCB.setSelectedIndex(params.getMethod());

        xRB_L0.setEnabled(true);
        yRB_L0.setEnabled(true);
        if(inFieldDims.length == 3) {
            zRB_L0.setEnabled(true);
            secondLevelCB.setEnabled(true);
        }

        switch(params.getAxisL0()) {
            case 0:
                xRB_L0.setSelected(true);
                break;
            case 1:
                yRB_L0.setSelected(true);
                break;
            case 2:
                zRB_L0.setSelected(true);
                break;
        }
        switch(params.getAxisL1()) {
            case 0:
                xRB_L1.setSelected(true);
                break;
            case 1:
                yRB_L1.setSelected(true);
                break;
            case 2:
                zRB_L1.setSelected(true);
                break;
        }

        if(params.getLevels() == 2) {
            secondLevelCB.setSelected(true);
            xRB_L1.setEnabled(inFieldDims.length == 3 && params.getAxisL0() != 0);
            yRB_L1.setEnabled(inFieldDims.length == 3 && params.getAxisL0() != 1);
            zRB_L1.setEnabled(inFieldDims.length == 3 && params.getAxisL0() != 2);
        } else {
            secondLevelCB.setSelected(false);
            xRB_L1.setEnabled(false);
            yRB_L1.setEnabled(false);
            zRB_L1.setEnabled(false);
        }

        silent = false;
    }

    private void xRB_L0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xRB_L0ActionPerformed
        if(silent) return;
        if(xRB_L0.isSelected())
            params.setAxisL0(0);
    }//GEN-LAST:event_xRB_L0ActionPerformed

    private void yRB_L0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yRB_L0ActionPerformed
        if(silent) return;
        if(yRB_L0.isSelected())
            params.setAxisL0(1);
    }//GEN-LAST:event_yRB_L0ActionPerformed

    private void zRB_L0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zRB_L0ActionPerformed
        if(silent) return;
        if(zRB_L0.isSelected())
            params.setAxisL0(2);
    }//GEN-LAST:event_zRB_L0ActionPerformed

    private void methodCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_methodCBActionPerformed
        if(silent) return;
        params.setMethod(methodCB.getSelectedIndex());
    }//GEN-LAST:event_methodCBActionPerformed

    private void xRB_L1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xRB_L1ActionPerformed
        if(silent) return;
        if(xRB_L1.isSelected())
            params.setAxisL1(0);
    }//GEN-LAST:event_xRB_L1ActionPerformed

    private void yRB_L1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yRB_L1ActionPerformed
        if(silent) return;
        if(yRB_L1.isSelected())
            params.setAxisL1(1);
    }//GEN-LAST:event_yRB_L1ActionPerformed

    private void zRB_L1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zRB_L1ActionPerformed
        if(silent) return;
        if(zRB_L1.isSelected())
            params.setAxisL1(2);
    }//GEN-LAST:event_zRB_L1ActionPerformed

    private void secondLevelCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_secondLevelCBActionPerformed
        if(silent) return;
        params.setLevels(secondLevelCB.isSelected() ? 2 : 1);
    }//GEN-LAST:event_secondLevelCBActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JComboBox methodCB;
    private javax.swing.JCheckBox secondLevelCB;
    private javax.swing.JRadioButton xRB_L0;
    private javax.swing.JRadioButton xRB_L1;
    private javax.swing.JRadioButton yRB_L0;
    private javax.swing.JRadioButton yRB_L1;
    private javax.swing.JRadioButton zRB_L0;
    private javax.swing.JRadioButton zRB_L1;
    // End of variables declaration//GEN-END:variables

    public void setParams(Params params) {
        this.params = params;
        this.params.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                updateGUI();
            }
        });
        updateGUI();
    }

    /**
     * @param inFieldDims the inFieldDims to set
     */
    public void setInFieldDims(int[] inFieldDims) {
        this.inFieldDims = inFieldDims;
        updateGUI();
    }

    
}
