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

package pl.edu.icm.visnow.lib.basic.filters.CropDown;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import pl.edu.icm.visnow.datasets.RegularField;

/**
 *
 * @author  Krzysztof S. Nowinski, University of Warsaw, ICM
 */

public class CropDownGUI extends JPanel
{
   private CropDownParams params = new CropDownParams();

   /** Creates new form CropDownUI */
   public CropDownGUI()
   {
      initComponents();
      cropUI.setDynamic(false);
      cropUI.addChangeListener(new ChangeListener(){
         public void stateChanged(ChangeEvent e)
         {
            params.setLow(cropUI.getLow());
            params.setUp(cropUI.getUp());
            params.fireStateChanged();
         }
      });
      downsizeUI.addChangeListener(new ChangeListener(){
         public void stateChanged(ChangeEvent e)
         {
            params.setDownsize(downsizeUI.getDownsize());
            params.fireStateChanged();
         }
      });
   }
   /** This method is called from within the constructor to
    * initialize the form.
    * WARNING: Do NOT modify this code. The content of this method is
    * always regenerated by the Form Editor.
    */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        waitToggle = new javax.swing.JToggleButton();
        cropUI = new pl.edu.icm.visnow.lib.gui.CropUI();
        downsizeUI = new pl.edu.icm.visnow.lib.gui.DownsizeUI();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));

        setLayout(new java.awt.GridBagLayout());

        waitToggle.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        waitToggle.setText("wait");
        waitToggle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                waitToggleActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(waitToggle, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        add(cropUI, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(downsizeUI, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.weighty = 1.0;
        add(filler1, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

   private void waitToggleActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_waitToggleActionPerformed
   {//GEN-HEADEREND:event_waitToggleActionPerformed
      params.setActive(!waitToggle.isSelected());
   }//GEN-LAST:event_waitToggleActionPerformed
   
    /**
     * Setter for property inField.
     * @param inField New value of property inField.
     */
   public void setInField(RegularField inField)
   {
      params.setActive(false);
      cropUI.setNewExtents(inField.getDims());
      params.setLow(cropUI.getLow());
      params.setUp(cropUI.getUp());
      int[] downsizes = new int[3];
      switch (inField.getDims().length)
      {
         case 3:
            for (int i = 0; i < inField.getDims().length; i++)
               downsizes[i] = (inField.getDims()[i] + 99) / 100;
            break;
         case 2:
            for (int i = 0; i < inField.getDims().length; i++)
               downsizes[i] = (inField.getDims()[i] + 999) / 1000;
            break;
         case 1:
            for (int i = 0; i < inField.getDims().length; i++)
               downsizes[i] = (inField.getDims()[i] + 999999) / 1000000;
            break;
      }
      downsizeUI.setDownsize(downsizes);
      params.setDownsize(downsizes);
      params.setActive(!waitToggle.isSelected());
      params.fireStateChanged();

   }
   public void setParams(CropDownParams params)
   {
      this.params = params;
   }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public pl.edu.icm.visnow.lib.gui.CropUI cropUI;
    public pl.edu.icm.visnow.lib.gui.DownsizeUI downsizeUI;
    public javax.swing.Box.Filler filler1;
    public javax.swing.JToggleButton waitToggle;
    // End of variables declaration//GEN-END:variables

    public static void main(String[] args) {
        JFrame f = new JFrame();        
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.add(new CropDownGUI());
        f.pack();
        f.setVisible(true);
        
    }
}
