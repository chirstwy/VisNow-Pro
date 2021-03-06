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

package pl.edu.icm.visnow.lib.basic.testdata.TestRegularField1D;

import java.util.ArrayList;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author  Krzysztof S. Nowinski, University of Warsaw, ICM
 */
public class GUI extends javax.swing.JPanel
{
   int res = 100;
   /** Creates new form SliceUI */
   public GUI()
   {
      initComponents();
   }
   
   public int getResolution()
   {
       return res;
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

      dimSlider = new javax.swing.JSlider();
      jPanel1 = new javax.swing.JPanel();

      setMinimumSize(new java.awt.Dimension(180, 70));
      setPreferredSize(new java.awt.Dimension(200, 75));
      setLayout(new java.awt.GridBagLayout());

      dimSlider.setFont(new java.awt.Font("Dialog", 0, 8)); // NOI18N
      dimSlider.setMajorTickSpacing(200);
      dimSlider.setMaximum(1000);
      dimSlider.setMinorTickSpacing(25);
      dimSlider.setPaintLabels(true);
      dimSlider.setPaintTicks(true);
      dimSlider.setSnapToTicks(true);
      dimSlider.setValue(100);
      dimSlider.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "resolution", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 10))); // NOI18N
      dimSlider.setMinimumSize(new java.awt.Dimension(120, 50));
      dimSlider.setPreferredSize(new java.awt.Dimension(200, 65));
      dimSlider.addChangeListener(new javax.swing.event.ChangeListener()
      {
         public void stateChanged(javax.swing.event.ChangeEvent evt)
         {
            dimSliderStateChanged(evt);
         }
      });
      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.gridwidth = 2;
      gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
      gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
      gridBagConstraints.weightx = 1.0;
      add(dimSlider, gridBagConstraints);
      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 2;
      gridBagConstraints.gridwidth = 2;
      gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
      gridBagConstraints.weighty = 1.0;
      add(jPanel1, gridBagConstraints);
   }// </editor-fold>//GEN-END:initComponents

   private void dimSliderStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_dimSliderStateChanged
   {//GEN-HEADEREND:event_dimSliderStateChanged
      if (!dimSlider.getValueIsAdjusting() && res != dimSlider.getValue())
      {
         res = dimSlider.getValue();
         fireStateChanged();
      }
   }//GEN-LAST:event_dimSliderStateChanged
   /**
    * Utility field holding list of ChangeListeners.
    */
   private transient ArrayList<ChangeListener> changeListenerList = new ArrayList<ChangeListener>();

   /**
    * Registers ChangeListener to receive events.
    * @param listener The listener to register.
    */
   public synchronized void addChangeListener(ChangeListener listener) {
      changeListenerList.add(listener);
   }

   /**
    * Removes ChangeListener from the list of listeners.
    * @param listener The listener to remove.
    */
   public synchronized void removeChangeListener(ChangeListener listener) {
      changeListenerList.remove(listener);
   }

   /**
    * Notifies all registered listeners about the event.
    *
    * @param object Parameter #1 of the <CODE>ChangeEvent<CODE> constructor.
    */
   private void fireStateChanged() {
      ChangeEvent e = new ChangeEvent(this);
      for (ChangeListener listener : changeListenerList) {
         listener.stateChanged(e);
      }
   }

      
   // Variables declaration - do not modify//GEN-BEGIN:variables
   private javax.swing.JSlider dimSlider;
   private javax.swing.JPanel jPanel1;
   // End of variables declaration//GEN-END:variables
   
}
