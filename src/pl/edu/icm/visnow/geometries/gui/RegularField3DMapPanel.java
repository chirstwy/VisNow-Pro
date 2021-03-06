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

package pl.edu.icm.visnow.geometries.gui;

import java.awt.Color;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import pl.edu.icm.visnow.geometries.parameters.RegularField3dParams;
import pl.edu.icm.visnow.gui.swingwrappers.TestPanel4;

/**
 *
 ** @author Krzysztof S. Nowinski, University of Warsaw ICM
 */
public class RegularField3DMapPanel extends javax.swing.JPanel
{

    protected RegularField3dParams params = new RegularField3dParams();
    protected int[] gridDens = new int[] {20,50,100, 300};

    /** Creates new form RegularField3DMapPanel */
    public RegularField3DMapPanel() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        boxColorChooser = new javax.swing.JColorChooser();
        dataButtonGroup = new javax.swing.ButtonGroup();
        dataPanel = new javax.swing.JPanel();
        boxColorButton = new javax.swing.JButton();
        gridTypeCombo = new javax.swing.JComboBox();
        gridCombo = new javax.swing.JComboBox();
        showExternFacesPanel = new javax.swing.JPanel();
        iMinSurfBox = new javax.swing.JCheckBox();
        iMaxSurfBox = new javax.swing.JCheckBox();
        jMinSurfBox = new javax.swing.JCheckBox();
        jMaxSurfBox = new javax.swing.JCheckBox();
        kMaxSurfBox = new javax.swing.JCheckBox();
        kMinSurfBox = new javax.swing.JCheckBox();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        sliceModePanel = new javax.swing.JPanel();
        sliceButton = new javax.swing.JRadioButton();
        avgButton = new javax.swing.JRadioButton();
        maxButton = new javax.swing.JRadioButton();

        setBorder(javax.swing.BorderFactory.createTitledBorder(null, "show data", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 10))); // NOI18N
        setRequestFocusEnabled(false);
        setLayout(new java.awt.GridBagLayout());

        dataPanel.setLayout(new java.awt.GridBagLayout());

        boxColorButton.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        boxColorButton.setText("color");
        boxColorButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        boxColorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boxColorButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        dataPanel.add(boxColorButton, gridBagConstraints);

        gridTypeCombo.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        gridTypeCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "no grid", "outline", "point grid", "line grid" }));
        gridTypeCombo.setSelectedIndex(1);
        gridTypeCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gridTypeComboActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        dataPanel.add(gridTypeCombo, gridBagConstraints);

        gridCombo.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        gridCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "coarse", "fine", "very fine" }));
        gridCombo.setEnabled(false);
        gridCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gridComboActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        dataPanel.add(gridCombo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(dataPanel, gridBagConstraints);

        showExternFacesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "show extern faces", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 10))); // NOI18N
        showExternFacesPanel.setLayout(new java.awt.GridBagLayout());

        iMinSurfBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        iMinSurfBox.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        iMinSurfBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                iMinSurfBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        showExternFacesPanel.add(iMinSurfBox, gridBagConstraints);

        iMaxSurfBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        iMaxSurfBox.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        iMaxSurfBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                iMaxSurfBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        showExternFacesPanel.add(iMaxSurfBox, gridBagConstraints);

        jMinSurfBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jMinSurfBox.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jMinSurfBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMinSurfBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        showExternFacesPanel.add(jMinSurfBox, gridBagConstraints);

        jMaxSurfBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jMaxSurfBox.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jMaxSurfBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMaxSurfBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        showExternFacesPanel.add(jMaxSurfBox, gridBagConstraints);

        kMaxSurfBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        kMaxSurfBox.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        kMaxSurfBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kMaxSurfBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        showExternFacesPanel.add(kMaxSurfBox, gridBagConstraints);

        kMinSurfBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        kMinSurfBox.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        kMinSurfBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kMinSurfBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        showExternFacesPanel.add(kMinSurfBox, gridBagConstraints);

        jLabel6.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel6.setText("max");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        showExternFacesPanel.add(jLabel6, gridBagConstraints);

        jLabel7.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel7.setText("min");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        showExternFacesPanel.add(jLabel7, gridBagConstraints);

        jLabel8.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("i");
        jLabel8.setMaximumSize(new java.awt.Dimension(20, 15));
        jLabel8.setMinimumSize(new java.awt.Dimension(20, 15));
        jLabel8.setPreferredSize(new java.awt.Dimension(20, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        showExternFacesPanel.add(jLabel8, gridBagConstraints);

        jLabel9.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("j");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        showExternFacesPanel.add(jLabel9, gridBagConstraints);

        jLabel10.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("k");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        showExternFacesPanel.add(jLabel10, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        add(showExternFacesPanel, gridBagConstraints);

        sliceModePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "color extern faces by", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 10))); // NOI18N
        sliceModePanel.setEnabled(false);
        sliceModePanel.setLayout(new java.awt.GridBagLayout());

        dataButtonGroup.add(sliceButton);
        sliceButton.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        sliceButton.setSelected(true);
        sliceButton.setText("slice");
        sliceButton.setEnabled(false);
        sliceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sliceButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        sliceModePanel.add(sliceButton, gridBagConstraints);

        dataButtonGroup.add(avgButton);
        avgButton.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        avgButton.setText("avg");
        avgButton.setEnabled(false);
        avgButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                avgButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        sliceModePanel.add(avgButton, gridBagConstraints);

        dataButtonGroup.add(maxButton);
        maxButton.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        maxButton.setText("max");
        maxButton.setEnabled(false);
        maxButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                maxButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        sliceModePanel.add(maxButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(sliceModePanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void iMinSurfBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_iMinSurfBoxActionPerformed
    {//GEN-HEADEREND:event_iMinSurfBoxActionPerformed
       params.setSurFaces(0, 0, iMinSurfBox.isSelected());
       updateSliceSelection();
    }//GEN-LAST:event_iMinSurfBoxActionPerformed

    private void iMaxSurfBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_iMaxSurfBoxActionPerformed
    {//GEN-HEADEREND:event_iMaxSurfBoxActionPerformed
       params.setSurFaces(0, 1, iMaxSurfBox.isSelected());
       updateSliceSelection();
    }//GEN-LAST:event_iMaxSurfBoxActionPerformed

    private void jMinSurfBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMinSurfBoxActionPerformed
    {//GEN-HEADEREND:event_jMinSurfBoxActionPerformed
       params.setSurFaces(1, 0, jMinSurfBox.isSelected());
       updateSliceSelection();
    }//GEN-LAST:event_jMinSurfBoxActionPerformed

    private void jMaxSurfBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMaxSurfBoxActionPerformed
    {//GEN-HEADEREND:event_jMaxSurfBoxActionPerformed
       params.setSurFaces(1, 1, jMaxSurfBox.isSelected());
       updateSliceSelection();
    }//GEN-LAST:event_jMaxSurfBoxActionPerformed

    private void kMinSurfBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_kMinSurfBoxActionPerformed
    {//GEN-HEADEREND:event_kMinSurfBoxActionPerformed
       params.setSurFaces(2, 0, kMinSurfBox.isSelected());
       updateSliceSelection();
    }//GEN-LAST:event_kMinSurfBoxActionPerformed

    private void kMaxSurfBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_kMaxSurfBoxActionPerformed
    {//GEN-HEADEREND:event_kMaxSurfBoxActionPerformed
       params.setSurFaces(2, 1, kMaxSurfBox.isSelected());
       updateSliceSelection();
    }//GEN-LAST:event_kMaxSurfBoxActionPerformed

    private void boxColorButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_boxColorButtonActionPerformed
    {//GEN-HEADEREND:event_boxColorButtonActionPerformed
         Color c =  JColorChooser.showDialog(this, "background", params.getBoxColor());
         if (c!=null)
         {
            params.setBoxColor(c);
            boxColorButton.setBackground(c);
         }       // TODO add your handling code here:
    }//GEN-LAST:event_boxColorButtonActionPerformed

    private void updateSliceMode()
    {
       if (sliceButton.isSelected())  params.setDataMap(RegularField3dParams.SLICE);
       if (avgButton.isSelected())    params.setDataMap(RegularField3dParams.AVG);
       if (maxButton.isSelected())    params.setDataMap(RegularField3dParams.MAX);
    }
    
    private void sliceButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_sliceButtonActionPerformed
    {//GEN-HEADEREND:event_sliceButtonActionPerformed
       updateSliceMode();
    }//GEN-LAST:event_sliceButtonActionPerformed

    private void avgButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_avgButtonActionPerformed
    {//GEN-HEADEREND:event_avgButtonActionPerformed
       updateSliceMode();
    }//GEN-LAST:event_avgButtonActionPerformed

    private void maxButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_maxButtonActionPerformed
    {//GEN-HEADEREND:event_maxButtonActionPerformed
       updateSliceMode();
    }//GEN-LAST:event_maxButtonActionPerformed

    private void gridComboActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_gridComboActionPerformed
    {//GEN-HEADEREND:event_gridComboActionPerformed
       params.setGridLines(gridDens[gridCombo.getSelectedIndex()]);
    }//GEN-LAST:event_gridComboActionPerformed

    private void gridTypeComboActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_gridTypeComboActionPerformed
    {//GEN-HEADEREND:event_gridTypeComboActionPerformed
       params.setGridType(gridTypeCombo.getSelectedIndex());
       gridCombo.setEnabled(gridTypeCombo.getSelectedIndex() > 1);
    }//GEN-LAST:event_gridTypeComboActionPerformed

   public void setParams(RegularField3dParams params)
   {
      this.params = params;
   }

   /**
    * Enables slice selection panel if any of extern faces is selected; Disables panel otherwise.
    */
    private void updateSliceSelection() {
        enableSliceSelection(kMaxSurfBox.isSelected() || kMinSurfBox.isSelected()
                        || iMaxSurfBox.isSelected() || iMinSurfBox.isSelected()
                        || jMaxSurfBox.isSelected() || jMinSurfBox.isSelected());
    }

    private void enableSliceSelection(boolean enabled) {
        sliceModePanel.setEnabled(enabled);
        sliceButton.setEnabled(enabled);
        avgButton.setEnabled(enabled);
        maxButton.setEnabled(enabled);
    }

    @Override
    public void setEnabled(boolean enabled) {
        sliceModePanel.setEnabled(enabled);
        dataPanel.setEnabled(enabled);
        showExternFacesPanel.setEnabled(enabled);

        gridCombo.setEnabled(enabled);
        gridTypeCombo.setEnabled(enabled);
        boxColorButton.setEnabled(enabled);
        boxColorChooser.setEnabled(enabled);


        iMaxSurfBox.setEnabled(enabled);
        iMinSurfBox.setEnabled(enabled);
        jMaxSurfBox.setEnabled(enabled);
        jMinSurfBox.setEnabled(enabled);
        kMaxSurfBox.setEnabled(enabled);
        kMinSurfBox.setEnabled(enabled);

        avgButton.setEnabled(enabled);
        maxButton.setEnabled(enabled);
        sliceButton.setEnabled(enabled);
    }

    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JRadioButton avgButton;
    protected javax.swing.JButton boxColorButton;
    protected javax.swing.JColorChooser boxColorChooser;
    protected javax.swing.ButtonGroup dataButtonGroup;
    protected javax.swing.JPanel dataPanel;
    protected javax.swing.JComboBox gridCombo;
    protected javax.swing.JComboBox gridTypeCombo;
    protected javax.swing.JCheckBox iMaxSurfBox;
    protected javax.swing.JCheckBox iMinSurfBox;
    protected javax.swing.JLabel jLabel10;
    protected javax.swing.JLabel jLabel6;
    protected javax.swing.JLabel jLabel7;
    protected javax.swing.JLabel jLabel8;
    protected javax.swing.JLabel jLabel9;
    protected javax.swing.JCheckBox jMaxSurfBox;
    protected javax.swing.JCheckBox jMinSurfBox;
    protected javax.swing.JCheckBox kMaxSurfBox;
    protected javax.swing.JCheckBox kMinSurfBox;
    protected javax.swing.JRadioButton maxButton;
    protected javax.swing.JPanel showExternFacesPanel;
    protected javax.swing.JRadioButton sliceButton;
    protected javax.swing.JPanel sliceModePanel;
    // End of variables declaration//GEN-END:variables

    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        final RegularField3DMapPanel p = new  RegularField3DMapPanel();
        f.add(p);
        f.pack();
        f.setVisible(true);
    }    
}
