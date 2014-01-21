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

package pl.edu.icm.visnow.geometries.viewer3d;

import java.awt.Color;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.filechooser.FileNameExtensionFilter;
import pl.edu.icm.visnow.system.main.VisNow;
import pl.edu.icm.visnow.system.swing.filechooser.VNFileChooser;

/**
 *
 * @author  Krzysztof S. Nowinski, University of Warsaw, ICM
 */
public class Display3DFrame extends JFrame
{

   private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(Display3DFrame.class);
   private Display3DPanel displayPanel = new Display3DPanel();
   private JFileChooser chooser = new JFileChooser();

   /** Creates new form Display3DFrame */
   public Display3DFrame()
   {
 	   chooser.setCurrentDirectory(new File(VisNow.get().getMainConfig().getWorkeffectPath()));
           chooser.setAcceptAllFileFilterUsed(false);
      JPopupMenu.setDefaultLightWeightPopupEnabled(false);
      log.debug("init frame");
      initComponents();
      setBounds(0, 20, VisNow.displayWidth, VisNow.displayHeight);
      log.debug("adding displayPanel");
      getContentPane().add(displayPanel, java.awt.BorderLayout.CENTER);
      stereoItem.setEnabled(displayPanel.getStereoAvailable());
      displayPanel.setParentFrame(this);

   }

   /** This method is called from within the constructor to
    * initialize the form.
    * WARNING: Do NOT modify this code. The content of this method is
    * always regenerated by the Form Editor.
    */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        saveAsItem = new javax.swing.JMenuItem();
        ctrlsItem = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        resetItem = new javax.swing.JMenuItem();
        bgrItem = new javax.swing.JMenuItem();
        stereoItem = new javax.swing.JCheckBoxMenuItem();
        transformMenu = new javax.swing.JMenu();
        objItem = new javax.swing.JMenuItem();
        lightItem = new javax.swing.JMenuItem();
        cameraItem = new javax.swing.JMenuItem();
        viewLockInfo = new javax.swing.JMenu();
        lockMenuItem = new javax.swing.JMenuItem();
        unlockMenuItem = new javax.swing.JMenuItem();

        setTitle("Viewer 3D");
        setBounds(new java.awt.Rectangle(0, 20, 600, 600));
        setIconImage(new ImageIcon(getClass().getResource("/pl/edu/icm/visnow/gui/icons/big/visnow.png")).getImage());

        jMenu1.setText("File");

        saveAsItem.setText("save as...");
        saveAsItem.setToolTipText("");
        saveAsItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsItemActionPerformed(evt);
            }
        });
        jMenu1.add(saveAsItem);

        ctrlsItem.setText("open controls");
        ctrlsItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctrlsItemActionPerformed(evt);
            }
        });
        jMenu1.add(ctrlsItem);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");

        resetItem.setText("reset and normalize");
        resetItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetItemActionPerformed(evt);
            }
        });
        jMenu2.add(resetItem);

        bgrItem.setText("background color");
        bgrItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bgrItemActionPerformed(evt);
            }
        });
        jMenu2.add(bgrItem);

        stereoItem.setText("stereo");
        stereoItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stereoItemActionPerformed(evt);
            }
        });
        jMenu2.add(stereoItem);

        jMenuBar1.add(jMenu2);

        transformMenu.setText("transforming object");

        objItem.setText("object");
        objItem.setSelected(true);
        transformMenu.add(objItem);

        lightItem.setText("light");
        transformMenu.add(lightItem);

        cameraItem.setText("camera");
        transformMenu.add(cameraItem);

        jMenuBar1.add(transformMenu);

        viewLockInfo.setText("view unlocked");

        lockMenuItem.setText("lock");
        lockMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lockMenuItemActionPerformed(evt);
            }
        });
        viewLockInfo.add(lockMenuItem);

        unlockMenuItem.setText("unlock");
        unlockMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                unlockMenuItemActionPerformed(evt);
            }
        });
        viewLockInfo.add(unlockMenuItem);

        jMenuBar1.add(viewLockInfo);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void stereoItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stereoItemActionPerformed
   displayPanel.setStereoEnable(stereoItem.isSelected());
}//GEN-LAST:event_stereoItemActionPerformed

private void bgrItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bgrItemActionPerformed
   Color c = JColorChooser.showDialog(this, "background", displayPanel.getBackgroundColor());
   if (c != null)
      displayPanel.setBackgroundColor(c);
}//GEN-LAST:event_bgrItemActionPerformed

private void resetItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetItemActionPerformed
   displayPanel.reset();
}//GEN-LAST:event_resetItemActionPerformed

private void ctrlsItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctrlsItemActionPerformed
   displayPanel.displayControls();
}//GEN-LAST:event_ctrlsItemActionPerformed

private void saveAsItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsItemActionPerformed
//   JFileChooser chooser = new JFileChooser();
    
//    FileNameExtensionFilter allImagesFilter =
//            new FileNameExtensionFilter("All image files", "jpg", "jpeg", "gif", "png", "bmp", "pcx", "tif", "tiff", "JPG", "JPEG", "GIF", "PNG", "BMP", "TIF", "TIFF", "PCX");
    FileNameExtensionFilter jpegImagesFilter = new FileNameExtensionFilter("JPEG images (*.jpg, *.jpeg)", "jpg", "jpeg", "JPG", "JPEG");
    FileNameExtensionFilter gifImagesFilter =
            new FileNameExtensionFilter("GIF images (*.gif)", "gif", "GIF");
    FileNameExtensionFilter pngImagesFilter =
            new FileNameExtensionFilter("PNG images (*.png)", "png", "PNG");
    FileNameExtensionFilter tiffImagesFilter =
           new FileNameExtensionFilter("TIFF images (*.tif, *.tiff)", "tif", "TIF", "tiff", "TIFF");
    FileNameExtensionFilter bmpImagesFilter =
           new FileNameExtensionFilter("BMP images (*.bmp)", "bmp", "BMP");
    FileNameExtensionFilter pcxImagesFilter =
           new FileNameExtensionFilter("PCX images (*.pcx)", "pcx", "PCX");

    chooser.addChoosableFileFilter(jpegImagesFilter);
    chooser.addChoosableFileFilter(gifImagesFilter);
    chooser.addChoosableFileFilter(pngImagesFilter);
    chooser.addChoosableFileFilter(tiffImagesFilter);
    chooser.addChoosableFileFilter(bmpImagesFilter);
    chooser.addChoosableFileFilter(pcxImagesFilter);
    displayPanel.newOffScreen();
//   chooser.setCurrentDirectory(new File(VisNow.get().getMainConfig().getLastApplicationsPath()));
   int returnVal = chooser.showSaveDialog(null);
   if (returnVal == JFileChooser.APPROVE_OPTION) {
       displayPanel.writeImage(VNFileChooser.fileWithExtensionAddedIfNecessary(chooser.getSelectedFile(), (FileNameExtensionFilter)chooser.getFileFilter()));
   }
}//GEN-LAST:event_saveAsItemActionPerformed

    private void lockMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lockMenuItemActionPerformed
        locked = true;
        displayPanel.setLockView(locked);
    }//GEN-LAST:event_lockMenuItemActionPerformed

    private void unlockMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unlockMenuItemActionPerformed
        locked = false;
        displayPanel.setLockView(locked);
    }//GEN-LAST:event_unlockMenuItemActionPerformed

    boolean locked = false;
    public void setLockInfo(boolean locked) {
        this.locked = locked;
        if(locked) {
            viewLockInfo.setText("VIEW LOCKED");
            viewLockInfo.setForeground(new java.awt.Color(255, 0, 0));
            viewLockInfo.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        } else {
            viewLockInfo.setText("view unlocked");
            viewLockInfo.setForeground(new java.awt.Color(0, 0, 0));
            viewLockInfo.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        }
        
        
    }
    
   public Display3DPanel getDisplayPanel()
   {
      return displayPanel;
   }

   public JMenu getTransformMenu()
   {
      return transformMenu;
   }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem bgrItem;
    private javax.swing.JMenuItem cameraItem;
    private javax.swing.JMenuItem ctrlsItem;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem lightItem;
    private javax.swing.JMenuItem lockMenuItem;
    private javax.swing.JMenuItem objItem;
    private javax.swing.JMenuItem resetItem;
    private javax.swing.JMenuItem saveAsItem;
    private javax.swing.JCheckBoxMenuItem stereoItem;
    private javax.swing.JMenu transformMenu;
    private javax.swing.JMenuItem unlockMenuItem;
    private javax.swing.JMenu viewLockInfo;
    // End of variables declaration//GEN-END:variables
}