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

package pl.edu.icm.visnow.lib.gui;

import java.util.ArrayList;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import pl.edu.icm.visnow.datasets.Field;
import pl.edu.icm.visnow.datasets.dataarrays.DataArray;

/**
 *
 * @author  Krzysztof S. Nowinski, University of Warsaw, ICM
 */
public class DataComponentList extends javax.swing.JPanel
{
   protected Field inField = null;
   protected boolean scalarComponentsOnly = false;
   protected boolean vectorComponentsOnly = false;
   protected boolean addNullComponent     = false;
   protected boolean simpleNumericOnly    = true;
   protected int nScalarComps = 0;
   protected int nVectorComps = 0;
   protected int nItems = 0;
   protected int nComps = 0;
   protected Vector<String>  compNames = new Vector<String>();
   protected int[] compIndices;
   protected boolean active = true;
   protected String[]  extraNames = null;
   protected int[] extraIndices;
   protected DefaultListModel componentListModel = new DefaultListModel();
   protected boolean[] componentSelections;
   /** Creates new form DataComponentSelector */
   public DataComponentList()
   {
      initComponents();
   }
   
   /** This method is called from within the constructor to
    * initialize the form.
    * WARNING: Do NOT modify this code. The content of this method is
    * always regenerated by the Form Editor.
    */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        componentList = new javax.swing.JList();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.BorderLayout());

        componentList.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        componentList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { " " };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        componentList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                componentListValueChanged(evt);
            }
        });
        jPanel1.add(componentList, java.awt.BorderLayout.CENTER);

        jScrollPane1.setViewportView(jPanel1);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

   private void componentListValueChanged(javax.swing.event.ListSelectionEvent evt)//GEN-FIRST:event_componentListValueChanged
   {//GEN-HEADEREND:event_componentListValueChanged
      fireStateChanged();
   }//GEN-LAST:event_componentListValueChanged

   protected void updateComponentNames()
   {
      active = false;
      if(inField == null) {
          nComps = 0;
          componentList.setModel(new DefaultListModel());
          return;
      }
      ArrayList<DataArray> components = inField.getData();
      nComps = components.size();
      compNames.clear();
      nItems = 0;
      if (extraNames != null && extraIndices != null && extraIndices.length == extraNames.length)
         compIndices = new int[nComps+extraIndices.length+1];
      else
         compIndices = new int[nComps+1];
      if (simpleNumericOnly)
      {
         if (scalarComponentsOnly)
         {
            nScalarComps = 0;
            for (int i = 0; i < nComps; i++)
               if (components.get(i).isSimpleNumeric() && components.get(i).getVeclen()==1)
               {
                   compNames.add(components.get(i).getName());
                   compIndices[nScalarComps] = i;
                   nScalarComps++;
                   nItems ++;
               }
         }
         else if (vectorComponentsOnly)
         {
            nVectorComps = 0;
            for (int i = 0; i < nComps; i++)
               if (components.get(i).isSimpleNumeric() && components.get(i).getVeclen()>1)
               {
                   compNames.add(components.get(i).getName());
                   compIndices[nVectorComps] = i;
                   nVectorComps++;
                   nItems ++;
               }
         }
         else
         {
            for (int i = 0; i < nComps; i++)
            {
               if (components.get(i).isSimpleNumeric())
               compNames.add(components.get(i).getName());
               compIndices[i] = i;
            }
            nItems = nComps;
         }
      }
      else
      {
         if (scalarComponentsOnly)
         {
            nScalarComps = 0;
            for (int i = 0; i < nComps; i++)
               if (components.get(i).getVeclen()==1)
               {
                   compNames.add(components.get(i).getName());
                   compIndices[nScalarComps] = i;
                   nScalarComps++;
                   nItems ++;
               }
         }
         else if (vectorComponentsOnly)
         {
            nVectorComps = 0;
            for (int i = 0; i < nComps; i++)
               if (components.get(i).getVeclen()>1)
               {
                   compNames.add(components.get(i).getName());
                   compIndices[nVectorComps] = i;
                   nVectorComps++;
                   nItems ++;
               }
         }
         else
         {
            for (int i = 0; i < nComps; i++)
            {
               compNames.add(components.get(i).getName());
               compIndices[i] = i;
            }
            nItems = nComps;
         }
      }
      if (extraNames != null && extraIndices != null && extraIndices.length == extraNames.length)
         for (int i = 0; i < extraIndices.length; i++, nItems++)
         {
            compNames.add(extraNames[i]);
            compIndices[nItems] = extraIndices[i];
         }
      if (addNullComponent)
      {
         compNames.add("null");
         compIndices[nItems] = -1;
      }
      componentListModel = new DefaultListModel();
      for (String cn : compNames)
         componentListModel.addElement(cn);
      componentList.setModel(componentListModel);
      if (compNames.isEmpty())
         componentList.setSelectedIndex(-1);
      else
         componentList.setSelectedIndex(0);
      active = true; 
      fireStateChanged();
   }
   
   public void setScalarComponentsOnly(boolean scalarComponentsOnly)
   {
      if (inField !=null && scalarComponentsOnly != this.scalarComponentsOnly)
         updateComponentNames();
      this.scalarComponentsOnly = scalarComponentsOnly;
   }

   public void setAddNullComponent(boolean addNullComponent)
   {
      if (inField !=null && addNullComponent != this.addNullComponent)
         updateComponentNames();
      this.addNullComponent = addNullComponent;
   }

   public void setVectorComponentsOnly(boolean vectorComponentsOnly)
   {
      if (inField !=null && vectorComponentsOnly != this.vectorComponentsOnly)
         updateComponentNames();
      this.vectorComponentsOnly = vectorComponentsOnly;
   }
   
   public void setInField(Field inField)
   {
      if (inField == null || inField.isDataCompatibleWith(this.inField))
         return;
      this.inField = inField;
      componentSelections = new boolean[inField.getNData()];
      updateComponentNames();
      if(inField.getNData() > 0)
         componentList.setSelectedIndex(0);
   }
   
   public void setSelectedIndex(int n)
   {
      componentList.setSelectedIndex(n);
   }
   
   public boolean isNullSelected()
   {
      return (addNullComponent && componentList.getSelectedIndex()==compNames.size()-1);
   }
   
   public int getComponent()
   {
      if(isNullSelected())
           return -1;
      int k = componentList.getSelectedIndex();
      if (k<0 || k>=compIndices.length) return -1;
      return compIndices[k];
   }

   public int[] getSelectedComponents()
   {
      int[] selIndices = componentList.getSelectedIndices();
      if(selIndices == null)
           return null;
      int[] selComps = new int[selIndices.length];
      for (int i = 0; i < selComps.length; i++)
      {
         int k = selIndices[i];
         if (k<0 || k>=compIndices.length)
            selComps[i] = -1;
         else
            selComps[i] = compIndices[k];
      }
      return selComps;
   }
   
   public boolean[] getComponentSelections()
   {
      
      int[] selIndices = componentList.getSelectedIndices();
      if(selIndices == null)
           return null;
      for (int i = 0; i < componentSelections.length; i++)
         componentSelections[i] = false;
      for (int i = 0; i < selIndices.length; i++)
         if (selIndices[i] >= 0 && selIndices[i] < componentSelections.length)
            componentSelections[selIndices[i]] = true;
      return componentSelections;
   }

   public void setComponent(int k) {
       if(k < 0 && addNullComponent) {
           componentList.setSelectedIndex(componentListModel.getSize()-1);
           return;
       }

       if (k<0 || k>=nComps) return;
       for (int i = 0; i < compIndices.length; i++) {
           if(compIndices[i] == k) {
               componentList.setSelectedIndex(i);
               break;
           }
       }
   }
   
   public void selectAll()
   {
      int[] selIndices = new int[nItems];
      for (int i = 0; i < selIndices.length; i++)
         selIndices[i] = i;
      componentList.setSelectedIndices(selIndices);
   }

   public int getnItems()
   {
      return nItems;
   }

   public int getnScalarComps()
   {
      return nScalarComps;
   }

   public int getnVectorComps()
   {
      return nVectorComps;
   }
   
   public void addExtraItems(String[] names, int[] ind)
   {
      extraNames = names;
      extraIndices = ind;
   }


   public int[] getSelectedItems()
   {
      return componentList.getSelectedIndices();
   }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList componentList;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
      /**
    * Utility field holding list of ChangeListeners.
    */
   protected transient ArrayList<ChangeListener> changeListenerList = new ArrayList<ChangeListener>();

   /**
    * Registers ChangeListener to receive events.
    * @param listener The listener to register.
    */
   public synchronized void addChangeListener(ChangeListener listener)
   {
      changeListenerList.add(listener);
   }

   /**
    * Removes ChangeListener from the list of listeners.
    * @param listener The listener to remove.
    */
   public synchronized void removeChangeListener(ChangeListener listener)
   {
      changeListenerList.remove(listener);
   }

   /**
    * Notifies all registered listeners about the event.
    *
    * @param object Parameter #1 of the <CODE>ChangeEvent<CODE> constructor.
    */
   protected void fireStateChanged()
   {
      ChangeEvent e = new ChangeEvent(this);
      for (ChangeListener listener : changeListenerList)
      {
         listener.stateChanged(e);
      }
   }

    @Override
   public void setEnabled(boolean enabled) {
        componentList.setEnabled(enabled);
   }

}
