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

package pl.edu.icm.visnow.lib.basic.mappers.SegmentedSurfaces;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import pl.edu.icm.visnow.datasets.RegularField;
import pl.edu.icm.visnow.system.main.VisNow;


/**
 *
 ** @author Krzysztof S. Nowinski, University of Warsaw ICM
 */
public class GUI extends javax.swing.JPanel
{
    protected Params params;
    protected RegularField inField = null;
    protected boolean newField = true;
   private static final int[] preferredColumnWidth = new int[]{180,40};
    /** Creates new form GUI */
   public GUI()
   {
      initComponents();
      for (int i = 0; i < 2; i++)
         volumesTable.getColumnModel().getColumn(i).setPreferredWidth(preferredColumnWidth[i]);
      cropUI.addChangeListener(new ChangeListener()
      {
         public void stateChanged(ChangeEvent e)
         {
            params.setLowUp(cropUI.getLow(), cropUI.getUp());
            updateParams();
         }
      });
      downsizeUI.addChangeListener(new ChangeListener()
      {
         public void stateChanged(ChangeEvent e)
         {
            params.setDownsize(downsizeUI.getDownsize());
            updateParams();
         }
      });
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

        cropUI = new pl.edu.icm.visnow.lib.gui.CropUI();
        downsizeUI = new pl.edu.icm.visnow.lib.gui.DownsizeUI();
        smoothBox = new javax.swing.JCheckBox();
        smoothSpinner = new javax.swing.JSpinner();
        jScrollPane2 = new javax.swing.JScrollPane();
        volumesTable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();

        setMinimumSize(new java.awt.Dimension(195, 550));
        setPreferredSize(new java.awt.Dimension(223, 650));
        setRequestFocusEnabled(false);
        setVerifyInputWhenFocusTarget(false);
        setLayout(new java.awt.GridBagLayout());

        cropUI.setMinimumSize(new java.awt.Dimension(150, 185));
        cropUI.setPreferredSize(new java.awt.Dimension(180, 190));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        add(cropUI, gridBagConstraints);

        downsizeUI.setMinimumSize(new java.awt.Dimension(180, 70));
        downsizeUI.setPreferredSize(new java.awt.Dimension(222, 70));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        add(downsizeUI, gridBagConstraints);

        smoothBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        smoothBox.setText("auto smooth level");
        smoothBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                smoothBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 2, 5);
        add(smoothBox, gridBagConstraints);

        smoothSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                smoothSpinnerStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        add(smoothSpinner, gridBagConstraints);

        jScrollPane2.setMinimumSize(new java.awt.Dimension(180, 200));
        jScrollPane2.setPreferredSize(new java.awt.Dimension(230, 403));

        volumesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"background", null}
            },
            new String [] {
                "object", "volume"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Float.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(volumesTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        add(jScrollPane2, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jPanel2, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void smoothBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_smoothBoxActionPerformed
    {//GEN-HEADEREND:event_smoothBoxActionPerformed
       if (params != null)
       {
          params.setSmoothing(smoothBox.isSelected());
          params.fireStateChanged();
       }
}//GEN-LAST:event_smoothBoxActionPerformed

    private void smoothSpinnerStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_smoothSpinnerStateChanged
    {//GEN-HEADEREND:event_smoothSpinnerStateChanged
       if (params != null)
       {
          params.setSmoothSteps((Integer)smoothSpinner.getValue());
          if (smoothBox.isSelected())
             params.fireStateChanged();
       }
}//GEN-LAST:event_smoothSpinnerStateChanged

   /**
    * Set the value of params
    *
    * @param params new value of params
    */
   public void setParams(Params params)
   {
      this.params = params;
   }

   public void setInField(RegularField inField)
   {
      params.setActive(false);
      newField = true;
      this.inField = inField;
      cropUI.setNewExtents(inField.getDims());
      int[] downsizes = new int[3];
      for (int i = 0; i < 3; i++)
         downsizes[i] = (inField.getDims()[i] + 127) / 128;
      downsizeUI.setDownsize(downsizes);
      if (params == null)
         return;
      params.setLowUp(cropUI.getLow(), cropUI.getUp());
      params.setDownsize(downsizes);
      params.setNThreads(VisNow.availableProcessors());
      newField = false;
      params.setActive(true);
   }
   
   private void updateParams()
   {
      if (params == null)
         return;
      params.setActive(false);
      params.setDownsize(downsizeUI.getDownsize());
      params.setLowUp(cropUI.getLow(), cropUI.getUp());
      params.setNThreads(VisNow.availableProcessors());
      params.setActive(true);
   }
   
   public void setVolumeTableContent(Object[][] data)
   {
      volumesTable.setModel(new DefaultTableModel(data, new String[]{"object", "volume"}));
   }
   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected pl.edu.icm.visnow.lib.gui.CropUI cropUI;
    protected pl.edu.icm.visnow.lib.gui.DownsizeUI downsizeUI;
    protected javax.swing.JPanel jPanel2;
    protected javax.swing.JScrollPane jScrollPane2;
    protected javax.swing.JCheckBox smoothBox;
    protected javax.swing.JSpinner smoothSpinner;
    protected javax.swing.JTable volumesTable;
    // End of variables declaration//GEN-END:variables

}