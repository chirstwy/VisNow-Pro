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

package pl.edu.icm.visnow.lib.basic.readers.ReadFloatDataArray;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import pl.edu.icm.visnow.system.main.VisNow;

/**
 *
 * @author  Krzysztof S. Nowinski, University of Warsaw, ICM
 */
public class GUI extends javax.swing.JPanel
{

   private JFileChooser dataFileChooser = new JFileChooser();
   private FileNameExtensionFilter dataFilter;
   private Params params;
   private String lastPath = null;

   /** Creates new form GUI */
   public GUI()
   {
      initComponents();
      dataFileChooser.setLocation(0,0);
   }

   public GUI(String dataFileDesc, String ext0, String ext1)
   {
      initComponents();
      dataFilter = new FileNameExtensionFilter(dataFileDesc, ext0, ext1);
      dataFileChooser.setFileFilter(dataFilter);
   }

   /** This method is called from within the constructor to
    * initialize the form.
    * WARNING: Do NOT modify this code. The content of this method is
    * always regenerated by the Form Editor.
    */
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {
      java.awt.GridBagConstraints gridBagConstraints;

      selectButton = new javax.swing.JButton();
      fileNameField = new javax.swing.JTextField();
      rereadButton = new javax.swing.JButton();
      jLabel1 = new javax.swing.JLabel();
      jLabel2 = new javax.swing.JLabel();
      dataNameField = new javax.swing.JTextField();
      dataUnitField = new javax.swing.JTextField();
      jScrollPane1 = new javax.swing.JScrollPane();
      dataArrayDescription = new javax.swing.JLabel();
      vlenSpinner = new javax.swing.JSpinner();
      jLabel3 = new javax.swing.JLabel();

      setBorder(javax.swing.BorderFactory.createTitledBorder(""));
      setMinimumSize(new java.awt.Dimension(180, 500));
      setOpaque(false);
      setPreferredSize(new java.awt.Dimension(200, 600));
      setRequestFocusEnabled(false);
      setLayout(new java.awt.GridBagLayout());

      selectButton.setText("browse");
      selectButton.setMaximumSize(new java.awt.Dimension(90, 20));
      selectButton.setMinimumSize(new java.awt.Dimension(90, 20));
      selectButton.setPreferredSize(new java.awt.Dimension(90, 20));
      selectButton.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            selectButtonActionPerformed(evt);
         }
      });
      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
      gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
      add(selectButton, gridBagConstraints);

      fileNameField.setMinimumSize(new java.awt.Dimension(4, 20));
      fileNameField.setPreferredSize(new java.awt.Dimension(4, 22));
      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.gridwidth = 2;
      gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
      gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
      add(fileNameField, gridBagConstraints);

      rereadButton.setText("reread");
      rereadButton.setEnabled(false);
      rereadButton.setMaximumSize(new java.awt.Dimension(90, 20));
      rereadButton.setMinimumSize(new java.awt.Dimension(90, 20));
      rereadButton.setPreferredSize(new java.awt.Dimension(90, 20));
      rereadButton.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            rereadButtonActionPerformed(evt);
         }
      });
      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 2;
      gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
      gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
      gridBagConstraints.insets = new java.awt.Insets(1, 0, 8, 0);
      add(rereadButton, gridBagConstraints);

      jLabel1.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
      jLabel1.setText("name");
      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 3;
      gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
      gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 9);
      add(jLabel1, gridBagConstraints);

      jLabel2.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
      jLabel2.setText("unit");
      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 4;
      gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
      gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 9);
      add(jLabel2, gridBagConstraints);

      dataNameField.setText("data");
      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 3;
      gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
      add(dataNameField, gridBagConstraints);
      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 4;
      gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
      gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 0);
      add(dataUnitField, gridBagConstraints);

      dataArrayDescription.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
      dataArrayDescription.setText(null);
      dataArrayDescription.setVerticalAlignment(javax.swing.SwingConstants.TOP);
      dataArrayDescription.setMaximumSize(new java.awt.Dimension(700, 250));
      dataArrayDescription.setMinimumSize(new java.awt.Dimension(400, 170));
      dataArrayDescription.setPreferredSize(new java.awt.Dimension(500, 200));
      jScrollPane1.setViewportView(dataArrayDescription);

      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 6;
      gridBagConstraints.gridwidth = 2;
      gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
      gridBagConstraints.weightx = 1.0;
      gridBagConstraints.weighty = 1.0;
      add(jScrollPane1, gridBagConstraints);

      vlenSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 5;
      gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
      gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
      add(vlenSpinner, gridBagConstraints);

      jLabel3.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
      jLabel3.setText("veclen");
      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 5;
      gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
      gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 9);
      add(jLabel3, gridBagConstraints);
   }// </editor-fold>//GEN-END:initComponents

   private void selectButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_selectButtonActionPerformed
   {//GEN-HEADEREND:event_selectButtonActionPerformed
      String fileName = null;
      if (lastPath == null)
          dataFileChooser.setCurrentDirectory(new File(VisNow.get().getMainConfig().getUsableDataPath(ReadFloatDataArray.class)));
      else
         dataFileChooser.setCurrentDirectory(new File(lastPath));

      int returnVal = dataFileChooser.showOpenDialog(this);
      if (returnVal == JFileChooser.APPROVE_OPTION)
      {
         fileName = dataFileChooser.getSelectedFile().getAbsolutePath();
         lastPath = fileName.substring(0, fileName.lastIndexOf(File.separator));
         VisNow.get().getMainConfig().setLastDataPath(lastPath,ReadFloatDataArray.class);
         rereadButton.setEnabled(true);
         fileNameField.setText(params.getFileName());
         params.setFileName(fileName);
         params.setDataName(dataNameField.getText());
         params.setDataUnits(dataUnitField.getText());
         params.setVeclen((Integer)(vlenSpinner.getValue()));
         params.fireStateChanged();
      }
   }//GEN-LAST:event_selectButtonActionPerformed

private void rereadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rereadButtonActionPerformed
   params.fireStateChanged();
}//GEN-LAST:event_rereadButtonActionPerformed

   public void setFieldDescription(String s)
   {
      dataArrayDescription.setText(s);
   }
   // Variables declaration - do not modify//GEN-BEGIN:variables
   private javax.swing.JLabel dataArrayDescription;
   private javax.swing.JTextField dataNameField;
   private javax.swing.JTextField dataUnitField;
   private javax.swing.JTextField fileNameField;
   private javax.swing.JLabel jLabel1;
   private javax.swing.JLabel jLabel2;
   private javax.swing.JLabel jLabel3;
   private javax.swing.JScrollPane jScrollPane1;
   private javax.swing.JButton rereadButton;
   private javax.swing.JButton selectButton;
   private javax.swing.JSpinner vlenSpinner;
   // End of variables declaration//GEN-END:variables

   /**
    * @param params the params to set
    */
   public void setParams(Params params)
   {
      this.params = params;
   }

   public String getLastPath()
   {
      return lastPath;
   }

    void activateOpenDialog() {
        selectButtonActionPerformed(null);
    }
}
