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
package pl.edu.icm.visnow.lib.basic.writers.ImageWriter;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import pl.edu.icm.visnow.system.main.VisNow;
import pl.edu.icm.visnow.system.swing.filechooser.VNFileChooser;

public class ImageWriterGUI extends javax.swing.JPanel {

    protected ImageWriterParams params = new ImageWriterParams();
    private String lastPath = null;

    /**
     * Creates new form EmptyVisnowModuleGUI
     */
    public ImageWriterGUI() {
        initComponents();
        FileNameExtensionFilter imageFilter = new FileNameExtensionFilter("Image files", "jpg", "jpeg", "JPG", "JPEG", "png", "PNG");
        fileChooser.setFileFilter(imageFilter);

    }

    /**
     * Set the value of params
     *
     * @param params new value of params
     */
    public void setParams(ImageWriterParams params) {
        this.params = params;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        fileChooser = new javax.swing.JFileChooser();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel3 = new javax.swing.JPanel();
        flipHCB = new javax.swing.JCheckBox();
        flipVCB = new javax.swing.JCheckBox();
        jPanel1 = new javax.swing.JPanel();
        jpegRB = new javax.swing.JRadioButton();
        pngRB = new javax.swing.JRadioButton();
        writeButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();

        setMinimumSize(new java.awt.Dimension(200, 500));
        setPreferredSize(new java.awt.Dimension(200, 500));
        setLayout(new java.awt.GridBagLayout());

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("pl/edu/icm/visnow/lib/basic/writers/ImageWriter/Bundle"); // NOI18N
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("ImageWriterGUI.jPanel3.border.title"))); // NOI18N
        jPanel3.setLayout(new java.awt.GridBagLayout());

        flipHCB.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        flipHCB.setText(bundle.getString("ImageWriterGUI.flipHCB.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        jPanel3.add(flipHCB, gridBagConstraints);

        flipVCB.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        flipVCB.setText(bundle.getString("ImageWriterGUI.flipVCB.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel3.add(flipVCB, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        add(jPanel3, gridBagConstraints);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("ImageWriterGUI.jPanel1.border.title"))); // NOI18N
        jPanel1.setLayout(new java.awt.GridBagLayout());

        buttonGroup1.add(jpegRB);
        jpegRB.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        jpegRB.setSelected(true);
        jpegRB.setText(bundle.getString("ImageWriterGUI.jpegRB.text")); // NOI18N
        jpegRB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jpegRBActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(jpegRB, gridBagConstraints);

        buttonGroup1.add(pngRB);
        pngRB.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        pngRB.setText(bundle.getString("ImageWriterGUI.pngRB.text")); // NOI18N
        pngRB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pngRBActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(pngRB, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        add(jPanel1, gridBagConstraints);

        writeButton.setText(bundle.getString("ImageWriterGUI.writeButton.text")); // NOI18N
        writeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                writeButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(writeButton, gridBagConstraints);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 200, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 337, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jPanel2, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void jpegRBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jpegRBActionPerformed
        updateFormat();
    }//GEN-LAST:event_jpegRBActionPerformed

    private void pngRBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pngRBActionPerformed
        updateFormat();
    }//GEN-LAST:event_pngRBActionPerformed

    private void writeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_writeButtonActionPerformed
        if (lastPath == null) {
            fileChooser.setCurrentDirectory(new File(VisNow.get().getMainConfig().getLastDataPath(ImageWriter.class)));
        } else {
            fileChooser.setCurrentDirectory(new File(lastPath));
        }

        int returnVal = fileChooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            FileNameExtensionFilter ext;
            if (pngRB.isSelected()) {
                ext = new FileNameExtensionFilter("", "png", "PNG");
            } else {
                ext = new FileNameExtensionFilter("", "jpg", "JPG");
            }

            String path = VNFileChooser.filenameWithExtenstionAddedIfNecessary(fileChooser.getSelectedFile(), ext);
            lastPath = path.substring(0, path.lastIndexOf(File.separator));
            params.setFileName(path);
        }

    }//GEN-LAST:event_writeButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JFileChooser fileChooser;
    private javax.swing.JCheckBox flipHCB;
    private javax.swing.JCheckBox flipVCB;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JRadioButton jpegRB;
    private javax.swing.JRadioButton pngRB;
    private javax.swing.JButton writeButton;
    // End of variables declaration//GEN-END:variables

    private void updateFormat() {
        if (jpegRB.isSelected()) {
            params.setFormat("JPEG");
        } else if (pngRB.isSelected()) {
            params.setFormat("PNG");
        }
    }
}
