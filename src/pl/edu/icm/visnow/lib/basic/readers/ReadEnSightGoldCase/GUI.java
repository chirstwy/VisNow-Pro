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

package pl.edu.icm.visnow.lib.basic.readers.ReadEnSightGoldCase;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import pl.edu.icm.visnow.lib.gui.Browser;
import pl.edu.icm.visnow.lib.gui.grid.GridFrame;
import pl.edu.icm.visnow.lib.utils.io.InputSource;
import pl.edu.icm.visnow.system.main.VisNow;

/**
 *
 * @author Krzysztof S. Nowinski, University of Warsaw, ICM
 */
public class GUI extends javax.swing.JPanel
{

   private JFileChooser dataFileChooser = new JFileChooser();
   private GridFrame gridFrame = new GridFrame();
   private FileNameExtensionFilter dataFilter;
   private Params params = new Params();
   private String lastPath = null;
   private String[] extensions = new String[]
   {
      "case", "CASE", "encas", "ENCAS"
   };
   private Browser browser = new Browser(extensions);

   /**
    * Creates new form GUI
    */
   public GUI()
   {
      initComponents();
      dataFileChooser.setLocation(0, 0);
      browser.setVisible(false);
      browser.addChangeListener(new ChangeListener()
      {
         public void stateChanged(ChangeEvent e)
         {
            params.setSource(InputSource.URL);
            params.setFileName(browser.getCurrentURL());
            fileNameField.setText(params.getFileName());
            params.fireStateChanged();
         }
      });

      gridFrame.setVisible(false);
      gridFrame.setFileExtensions(extensions);
      gridFrame.setSingleFile(true);
      gridFrame.addChangeListener(new ChangeListener()
      {
         public void stateChanged(ChangeEvent e)
         {
            params.setSource(InputSource.GRID);
            params.setFileName(VisNow.getTmpDirPath() + File.separator + gridFrame.getTransferredFileNames()[0]);
            fileNameField.setText(params.getFileName());
            params.fireStateChanged();
         }
      });
   }

   public GUI(String title, String dataFileDesc, String[] extensions)
   {
      initComponents();
      moduleLabel.setText(title);
      this.extensions = extensions;
      switch (extensions.length)
      {
      case 1:
         dataFilter = new FileNameExtensionFilter(dataFileDesc, extensions[0]);
         break;
      case 2:
         dataFilter = new FileNameExtensionFilter(dataFileDesc, extensions[0], extensions[1]);
         break;
      case 3:
         dataFilter = new FileNameExtensionFilter(dataFileDesc, extensions[0], extensions[1], 
                                                                extensions[2]);
         break;
      case 4:
         dataFilter = new FileNameExtensionFilter(dataFileDesc, extensions[0], extensions[1], 
                                                                extensions[2], extensions[3]);
         break;
      }
      dataFileChooser.setFileFilter(dataFilter);
      browser.setVisible(false);
      browser.addChangeListener(new ChangeListener()
      {
         public void stateChanged(ChangeEvent e)
         {
            params.setSource(InputSource.URL);
            params.setFileName(browser.getCurrentURL());
            fileNameField.setText(params.getFileName());
            params.fireStateChanged();
         }
      });

      gridFrame.setVisible(false);
      gridFrame.setFileExtensions(extensions);
      gridFrame.setSingleFile(true);
      gridFrame.addChangeListener(new ChangeListener()
      {
         public void stateChanged(ChangeEvent e)
         {
            params.setSource(InputSource.GRID);
            params.setFileName(VisNow.getTmpDirPath() + File.separator + gridFrame.getTransferredFileNames()[0]);
            fileNameField.setText(params.getFileName());
            params.fireStateChanged();
         }
      });
   }

   /**
    * This method is called from within the constructor to initialize the form.
    * WARNING: Do NOT modify this code. The content of this method is always
    * regenerated by the Form Editor.
    */
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents()
   {
      java.awt.GridBagConstraints gridBagConstraints;

      buttonGroup1 = new javax.swing.ButtonGroup();
      selectButton = new javax.swing.JButton();
      fileNameField = new javax.swing.JTextField();
      moduleLabel = new javax.swing.JLabel();
      rereadButton = new javax.swing.JButton();
      displayBox = new javax.swing.JCheckBox();
      jScrollPane1 = new javax.swing.JScrollPane();
      fieldDescription = new javax.swing.JLabel();
      fileButton = new javax.swing.JRadioButton();
      urlButton = new javax.swing.JRadioButton();
      separateCheckBox = new javax.swing.JCheckBox();

      setBorder(javax.swing.BorderFactory.createTitledBorder(""));
      setMinimumSize(new java.awt.Dimension(180, 500));
      setPreferredSize(new java.awt.Dimension(200, 600));
      setRequestFocusEnabled(false);
      setLayout(new java.awt.GridBagLayout());

      selectButton.setText("browse");
      selectButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
      selectButton.setMaximumSize(new java.awt.Dimension(90, 20));
      selectButton.setMinimumSize(new java.awt.Dimension(90, 20));
      selectButton.setPreferredSize(new java.awt.Dimension(90, 20));
      selectButton.addActionListener(new java.awt.event.ActionListener()
      {
         public void actionPerformed(java.awt.event.ActionEvent evt)
         {
            selectButtonActionPerformed(evt);
         }
      });
      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 2;
      gridBagConstraints.gridwidth = 3;
      gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
      gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
      gridBagConstraints.weightx = 1.0;
      add(selectButton, gridBagConstraints);

      fileNameField.setMinimumSize(new java.awt.Dimension(4, 20));
      fileNameField.setPreferredSize(new java.awt.Dimension(4, 22));
      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 3;
      gridBagConstraints.gridwidth = 3;
      gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
      gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
      add(fileNameField, gridBagConstraints);

      moduleLabel.setText("module");
      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
      add(moduleLabel, gridBagConstraints);

      rereadButton.setText("reread");
      rereadButton.setEnabled(false);
      rereadButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
      rereadButton.setMaximumSize(new java.awt.Dimension(90, 20));
      rereadButton.setMinimumSize(new java.awt.Dimension(90, 20));
      rereadButton.setPreferredSize(new java.awt.Dimension(90, 20));
      rereadButton.addActionListener(new java.awt.event.ActionListener()
      {
         public void actionPerformed(java.awt.event.ActionEvent evt)
         {
            rereadButtonActionPerformed(evt);
         }
      });
      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx = 2;
      gridBagConstraints.gridy = 4;
      gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
      gridBagConstraints.weightx = 1.0;
      gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 0);
      add(rereadButton, gridBagConstraints);

      displayBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
      displayBox.setSelected(true);
      displayBox.setText("display");
      displayBox.addActionListener(new java.awt.event.ActionListener()
      {
         public void actionPerformed(java.awt.event.ActionEvent evt)
         {
            displayBoxActionPerformed(evt);
         }
      });
      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx = 2;
      gridBagConstraints.gridy = 5;
      gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
      add(displayBox, gridBagConstraints);

      fieldDescription.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
      fieldDescription.setText(null);
      fieldDescription.setVerticalAlignment(javax.swing.SwingConstants.TOP);
      fieldDescription.setMaximumSize(new java.awt.Dimension(700, 250));
      fieldDescription.setMinimumSize(new java.awt.Dimension(400, 170));
      fieldDescription.setPreferredSize(new java.awt.Dimension(500, 200));
      jScrollPane1.setViewportView(fieldDescription);

      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 6;
      gridBagConstraints.gridwidth = 3;
      gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
      gridBagConstraints.weightx = 1.0;
      gridBagConstraints.weighty = 1.0;
      add(jScrollPane1, gridBagConstraints);

      buttonGroup1.add(fileButton);
      fileButton.setSelected(true);
      fileButton.setText("file");
      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
      gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
      add(fileButton, gridBagConstraints);

      buttonGroup1.add(urlButton);
      urlButton.setText("URL");
      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
      gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
      add(urlButton, gridBagConstraints);

      separateCheckBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
      separateCheckBox.setSelected(true);
      separateCheckBox.setText("parts as cell sets");
      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 5;
      gridBagConstraints.gridwidth = 2;
      add(separateCheckBox, gridBagConstraints);
   }// </editor-fold>//GEN-END:initComponents

   private void selectButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_selectButtonActionPerformed
   {//GEN-HEADEREND:event_selectButtonActionPerformed
      String fileName = null;
      if (fileButton.isSelected())
      {
         if (lastPath == null)
            dataFileChooser.setCurrentDirectory(new File(VisNow.get().getMainConfig().getUsableDataPath(ReadEnSightGoldCase.class)));
         else
            dataFileChooser.setCurrentDirectory(new File(lastPath));

         int returnVal = dataFileChooser.showOpenDialog(this);
         if (returnVal == JFileChooser.APPROVE_OPTION)
         {
            fileName = dataFileChooser.getSelectedFile().getAbsolutePath();
            lastPath = fileName.substring(0, fileName.lastIndexOf(File.separator));
            VisNow.get().getMainConfig().setLastDataPath(lastPath, ReadEnSightGoldCase.class);
            rereadButton.setEnabled(true);
            fileNameField.setText(fileName);
            params.setFileName(fileName);
            params.setSource(InputSource.FILE);
            params.setMaterialsAsSets(separateCheckBox.isSelected());
            params.fireStateChanged();
         }
      } else if (urlButton.isSelected())
      {
         browser.setVisible(true);
      } 
   }//GEN-LAST:event_selectButtonActionPerformed

private void rereadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rereadButtonActionPerformed
   params.fireStateChanged();
}//GEN-LAST:event_rereadButtonActionPerformed

private void displayBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_displayBoxActionPerformed
{//GEN-HEADEREND:event_displayBoxActionPerformed
   params.setValue("show", new Boolean(displayBox.isSelected()));
}//GEN-LAST:event_displayBoxActionPerformed

   public Params getParams()
   {
      return params;
   }

   public void setFieldDescription(String s)
   {
      fieldDescription.setText(s);
   }
   // Variables declaration - do not modify//GEN-BEGIN:variables
   private javax.swing.ButtonGroup buttonGroup1;
   private javax.swing.JCheckBox displayBox;
   private javax.swing.JLabel fieldDescription;
   private javax.swing.JRadioButton fileButton;
   private javax.swing.JTextField fileNameField;
   private javax.swing.JScrollPane jScrollPane1;
   private javax.swing.JLabel moduleLabel;
   private javax.swing.JButton rereadButton;
   private javax.swing.JButton selectButton;
   private javax.swing.JCheckBox separateCheckBox;
   private javax.swing.JRadioButton urlButton;
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
