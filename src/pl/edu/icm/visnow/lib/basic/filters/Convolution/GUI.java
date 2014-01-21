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
package pl.edu.icm.visnow.lib.basic.filters.Convolution;

import java.util.Arrays;
import java.util.Vector;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import pl.edu.icm.visnow.datasets.RegularField;
import pl.edu.icm.visnow.lib.utils.PaddingType;

/**
 *
 * @author Michal Lyczek (lyczek@icm.edu.pl)
 */
public class GUI extends javax.swing.JPanel {

    private Params params = new Params();

    public Params getParams() {
        return params;
    }

    public void setParams(Params params) {
        this.params = params;
    }

    /**
     * Creates new form ConvolutionPanel
     */
    public GUI() {
        initComponents();
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
        cbPadding = new javax.swing.JComboBox();
        normalizeCheckBox = new javax.swing.JCheckBox();
        jLabel3 = new javax.swing.JLabel();
        dataComponentList = new pl.edu.icm.visnow.lib.gui.DataComponentList();

        setMinimumSize(new java.awt.Dimension(164, 92));
        setLayout(new java.awt.GridBagLayout());

        jLabel1.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel1.setText("Padding");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 12, 0, 0);
        add(jLabel1, gridBagConstraints);

        cbPadding.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        cbPadding.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "zeros", "fixed", "periodic", "reflected" }));
        cbPadding.setSelectedIndex(1);
        cbPadding.setName("cbPadding"); // NOI18N
        cbPadding.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbPaddingItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 12, 0, 12);
        add(cbPadding, gridBagConstraints);

        normalizeCheckBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        normalizeCheckBox.setSelected(true);
        normalizeCheckBox.setText("Normalize kernel");
        normalizeCheckBox.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                normalizeCheckBoxStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(18, 12, 24, 0);
        add(normalizeCheckBox, gridBagConstraints);

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel3.setText("Components");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(12, 12, 0, 0);
        add(jLabel3, gridBagConstraints);

        dataComponentList.setPreferredSize(new java.awt.Dimension(100, 50));
        dataComponentList.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                dataComponentListStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.ipady = 138;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 12, 0, 12);
        add(dataComponentList, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void dataComponentListStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_dataComponentListStateChanged
        if(dataComponentList.getSelectedComponents() != null) {
            if(params.getComponents() != null) {
                int[] arr1 = params.getComponents();
                Arrays.sort(arr1);
                int[] arr2 = dataComponentList.getSelectedComponents();
                Arrays.sort(arr2);
                if(!Arrays.equals(arr1, arr2)) {
                    params.setComponents(dataComponentList.getSelectedComponents());
                }
            }
            else {
                params.setComponents(dataComponentList.getSelectedComponents());
            }
        }
    }//GEN-LAST:event_dataComponentListStateChanged

    private void cbPaddingItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbPaddingItemStateChanged
        PaddingType padding;
        switch (cbPadding.getSelectedIndex()) {
            case 0:
                padding = PaddingType.ZERO;
                break;
            case 1:
                padding = PaddingType.FIXED;
                break;
            case 2:
                padding = PaddingType.PERIODIC;
                break;
            case 3:
                padding = PaddingType.REFLECTED;
                break;
            default:
                padding = PaddingType.FIXED;
        }
        if (padding != params.getPaddingType()) {
            params.setPaddingType(padding);
        }
    }//GEN-LAST:event_cbPaddingItemStateChanged

    private void normalizeCheckBoxStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_normalizeCheckBoxStateChanged
        if(params.isNormalizeKernel() != normalizeCheckBox.isSelected()) {
            params.setNormalizeKernel(normalizeCheckBox.isSelected());
        }
    }//GEN-LAST:event_normalizeCheckBoxStateChanged

    
 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cbPadding;
    private pl.edu.icm.visnow.lib.gui.DataComponentList dataComponentList;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JCheckBox normalizeCheckBox;
    // End of variables declaration//GEN-END:variables
    private Vector<ChangeListener> changeListeners = new Vector<ChangeListener>();

    /**
     * Registers ChangeListener to receive events.
     *
     * @param listener The listener to register.
     */
    public synchronized void addChangeListener(ChangeListener listener) {
        changeListeners.add(listener);
    }

    /**
     * Removes ChangeListener from the list of listeners.
     *
     * @param listener The listener to remove.
     */
    public synchronized void removeChangeListener(ChangeListener listener) {
        changeListeners.remove(listener);
    }

    /**
     * Notifies all registered listeners about the event.
     *
     * @param object Parameter #1 of the <CODE>ChangeEvent<CODE> constructor.
     */
    private void fireStateChanged() {
        ChangeEvent e = new ChangeEvent(this);
        for (ChangeListener listener : changeListeners) {
            listener.stateChanged(e);
        }
    }
    
    public void setInFieldData(RegularField field) {
        dataComponentList.setScalarComponentsOnly(true);
        dataComponentList.setInField(field);
    }
}
