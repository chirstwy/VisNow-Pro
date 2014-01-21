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

package pl.edu.icm.visnow.lib.basic.viewers.MultiViewer3D;


import javax.swing.ImageIcon;
import javax.swing.JFrame;
import pl.edu.icm.visnow.geometries.viewer3d.controls.Display3DControlsPanel;
import pl.edu.icm.visnow.system.main.VisNow;

public class GUI extends javax.swing.JPanel
{
   private JFrame detachedFrame = null;
   private MultiViewer3D viewerModule = null;
   private Display3DControlsPanel controlsPanel = null;

   /** Creates new form GUI */
   public GUI()
   {
      initComponents();
   }

   /** This method is called from within the constructor to
    * initialize the form.
    * WARNING: Do NOT modify this code. The content of this method is
    * always regenerated by the Form Editor.
    */
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents()
   {
      java.awt.GridBagConstraints gridBagConstraints;

      jPanel1 = new javax.swing.JPanel();
      controlsContainer = new javax.swing.JPanel();
      jPanel2 = new javax.swing.JPanel();
      show3DFrameButton = new javax.swing.JButton();

      setLayout(new java.awt.GridBagLayout());
      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 2;
      gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
      gridBagConstraints.weightx = 1.0;
      gridBagConstraints.weighty = 1.0;
      add(jPanel1, gridBagConstraints);

      controlsContainer.setLayout(new java.awt.BorderLayout());
      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
      gridBagConstraints.weightx = 1.0;
      gridBagConstraints.insets = new java.awt.Insets(7, 0, 0, 0);
      add(controlsContainer, gridBagConstraints);

      jPanel2.setLayout(new java.awt.GridLayout(1, 2));

      show3DFrameButton.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
      show3DFrameButton.setText("<html>show<p>display frame</html>");
      show3DFrameButton.setMargin(new java.awt.Insets(2, 8, 2, 8));
      show3DFrameButton.setMaximumSize(new java.awt.Dimension(120, 40));
      show3DFrameButton.addActionListener(new java.awt.event.ActionListener()
      {
         public void actionPerformed(java.awt.event.ActionEvent evt)
         {
            show3DFrameButtonActionPerformed(evt);
         }
      });
      jPanel2.add(show3DFrameButton);

      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
      add(jPanel2, gridBagConstraints);
   }// </editor-fold>//GEN-END:initComponents

   private void show3DFrameButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_show3DFrameButtonActionPerformed
   {//GEN-HEADEREND:event_show3DFrameButtonActionPerformed
      viewerModule.showWindow();
   }//GEN-LAST:event_show3DFrameButtonActionPerformed

   public void setControlsPanel(Display3DControlsPanel panel)
   {
      this.controlsPanel = panel;
      detachedFrame = new JFrame(); detachedFrame.setIconImage(new ImageIcon(getClass().getResource( VisNow.getIconPath() )).getImage());
      detachedFrame.addWindowListener(new java.awt.event.WindowAdapter()
      {
         @Override
         public void windowClosing(java.awt.event.WindowEvent evt)
         {
            controlsPanel.releaseLightEdit();
         }
      });
      detachedFrame.setBounds(300, 50, 272,457);
      detachedFrame.add(controlsPanel, java.awt.BorderLayout.CENTER);
      viewerModule.setTransientControlsFrame(detachedFrame);
   }

   public void setViewerModule(MultiViewer3D viewerModule)
   {
      this.viewerModule = viewerModule;
   }
   
   
   // Variables declaration - do not modify//GEN-BEGIN:variables
   private javax.swing.JPanel controlsContainer;
   private javax.swing.JPanel jPanel1;
   private javax.swing.JPanel jPanel2;
   private javax.swing.JButton show3DFrameButton;
   // End of variables declaration//GEN-END:variables
}
