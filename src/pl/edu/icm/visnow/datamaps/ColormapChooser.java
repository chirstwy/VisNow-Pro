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

package pl.edu.icm.visnow.datamaps;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Hashtable;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;
import pl.edu.icm.visnow.datamaps.widgets.ColorMapCellRenderer;
import pl.edu.icm.visnow.geometries.parameters.ColorComponentParams;
import pl.edu.icm.visnow.system.main.VisNow;

/**
 *
 * @author Krzysztof S. Nowinski, University of Warsaw ICM
 */
public class ColormapChooser extends javax.swing.JPanel
{
   private CustomizableColorMap map = new CustomizableColorMap();
   private Hashtable<Integer, JLabel> stepLabels = new Hashtable<Integer, JLabel>();

   /**
    * Creates new form ColormapChooser
    */
   @SuppressWarnings("unchecked")
   public ColormapChooser()
   {
      initComponents();
      colorMapCombo.setModel(ColorMapManager.getInstance().getColorMap1DListModel());
      cmap2EditPanel.setVisible(false);
      cmap3EditPanel.setVisible(false);
      Font lFont = new Font("Dialog", 0, 10);
      stepLabels.put(5, new JLabel("coarse"));
      stepLabels.get(5).setFont(lFont);
      stepLabels.put(30, new JLabel("fine"));
      stepLabels.get(30).setFont(lFont);
      stepLabels.put(50, new JLabel("continuous"));
      stepLabels.get(50).setFont(lFont);
      stepSlider.setLabelTable(stepLabels);
      brightnessEditor.setMap(map);
      colorEditor0.setBrightness(0);
      colorEditor1.setBrightness(100);
      colorEditor00.setBrightness(100);
      colorEditor05.setBrightness(100);
      colorEditor10.setBrightness(100);
      colorEditor00.setBasicColor(Color.BLUE);
      colorEditor10.setBasicColor(Color.RED);
   }

   public void setParams(ColorComponentParams params)
   {
      map = params.getMap();
      if(colorMapCombo.getSelectedIndex() == map.getMapType())
          return;
      CustomizableColorMap.Active oldActive = map.getActive();
      map.setActive(CustomizableColorMap.Active.SLEEP);
      colorMapCombo.setSelectedIndex(map.getMapType());
      processSelectedIndex(map.getMapType());
      map.setActiveValue(oldActive);
   }

   public int getSelectedIndex()
   {
      return colorMapCombo.getSelectedIndex();
   }

   public void setSelectedIndex(int i)
   {
      CustomizableColorMap.Active oldActive = map.getActive();
      map.setActive(CustomizableColorMap.Active.SLEEP);
      map.setMapIndex(i);
      colorMapCombo.setSelectedIndex(i);
      processSelectedIndex(i);
      map.setActiveValue(oldActive);
   }

   public void setPresentation(boolean simple)
   {
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

        colorMapCombo = new javax.swing.JComboBox();
        stdEditPanel = new javax.swing.JPanel();
        reverseCheckBox = new javax.swing.JCheckBox();
        brightnessEditor = new pl.edu.icm.visnow.datamaps.BrightnessEditor();
        cmap2EditPanel = new javax.swing.JPanel();
        colorEditor0 = new pl.edu.icm.visnow.gui.widgets.ColorEditor();
        colorEditor1 = new pl.edu.icm.visnow.gui.widgets.ColorEditor();
        cmap3EditPanel = new javax.swing.JPanel();
        colorEditor00 = new pl.edu.icm.visnow.gui.widgets.ColorEditor();
        colorEditor05 = new pl.edu.icm.visnow.gui.widgets.ColorEditor();
        colorEditor10 = new pl.edu.icm.visnow.gui.widgets.ColorEditor();
        stepSlider = new javax.swing.JSlider();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        setOpaque(false);
        setVerifyInputWhenFocusTarget(false);
        setLayout(new java.awt.GridBagLayout());

        colorMapCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        colorMapCombo.setRenderer( ColorMapCellRenderer.getInstance() );
        colorMapCombo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                colorMapComboItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 0);
        add(colorMapCombo, gridBagConstraints);

        stdEditPanel.setLayout(new java.awt.GridBagLayout());

        reverseCheckBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        reverseCheckBox.setText("reverse");
        reverseCheckBox.setMaximumSize(new java.awt.Dimension(72, 21));
        reverseCheckBox.setMinimumSize(new java.awt.Dimension(72, 21));
        reverseCheckBox.setPreferredSize(new java.awt.Dimension(72, 21));
        reverseCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reverseCheckBoxActionPerformed(evt);
            }
        });
        stdEditPanel.add(reverseCheckBox, new java.awt.GridBagConstraints());

        brightnessEditor.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                brightnessEditorStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        stdEditPanel.add(brightnessEditor, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        add(stdEditPanel, gridBagConstraints);

        cmap2EditPanel.setLayout(new java.awt.GridBagLayout());

        colorEditor0.setBrightness(0);
        colorEditor0.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                colorEditor0StateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        cmap2EditPanel.add(colorEditor0, gridBagConstraints);

        colorEditor1.setBrightness(1);
        colorEditor1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                colorEditor1StateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        cmap2EditPanel.add(colorEditor1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        add(cmap2EditPanel, gridBagConstraints);

        cmap3EditPanel.setLayout(new java.awt.GridBagLayout());

        colorEditor00.setBrightness(0);
        colorEditor00.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                colorEditor00StateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        cmap3EditPanel.add(colorEditor00, gridBagConstraints);

        colorEditor05.setBrightness(1);
        colorEditor05.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                colorEditor05StateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        cmap3EditPanel.add(colorEditor05, gridBagConstraints);

        colorEditor10.setBrightness(0);
        colorEditor10.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                colorEditor10StateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        cmap3EditPanel.add(colorEditor10, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        add(cmap3EditPanel, gridBagConstraints);

        stepSlider.setFont(new java.awt.Font("Dialog", 0, 8)); // NOI18N
        stepSlider.setMajorTickSpacing(5);
        stepSlider.setMaximum(50);
        stepSlider.setMinimum(5);
        stepSlider.setMinorTickSpacing(1);
        stepSlider.setPaintLabels(true);
        stepSlider.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "colormap steps", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 10))); // NOI18N
        stepSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                stepSliderStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        add(stepSlider, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

   private void colorMapComboItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_colorMapComboItemStateChanged
   {//GEN-HEADEREND:event_colorMapComboItemStateChanged
      int k = colorMapCombo.getSelectedIndex();
      if (k == map.getMapType())
         return;
      map.setMapIndex(k);
      processSelectedIndex(k);
   }//GEN-LAST:event_colorMapComboItemStateChanged

   private void stepSliderStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_stepSliderStateChanged
   {//GEN-HEADEREND:event_stepSliderStateChanged
      map.setAdjusting(stepSlider.getValueIsAdjusting());
      map.setFinalSteps(stepSlider.getValue());
      if (map != brightnessEditor.getMap())
         brightnessEditor.setMap(map);
      brightnessEditor.updateImage();
   }//GEN-LAST:event_stepSliderStateChanged

   private void reverseCheckBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_reverseCheckBoxActionPerformed
   {//GEN-HEADEREND:event_reverseCheckBoxActionPerformed
      map.setReverse(reverseCheckBox.isSelected());
      if (map != brightnessEditor.getMap())
         brightnessEditor.setMap(map);
      brightnessEditor.updateImage();
   }//GEN-LAST:event_reverseCheckBoxActionPerformed

   private void brightnessEditorStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_brightnessEditorStateChanged
   {//GEN-HEADEREND:event_brightnessEditorStateChanged
      map.setAdjusting(brightnessEditor.isAdjusting());
      map.setFinalBrightness(brightnessEditor.getBrightness());
   }//GEN-LAST:event_brightnessEditorStateChanged

   private void colorEditor0StateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_colorEditor0StateChanged
   {//GEN-HEADEREND:event_colorEditor0StateChanged
      map.setAdjusting(colorEditor0.isAdjusting());
      map.setColorTables(colorEditor0.getColorComponents(), colorEditor1.getColorComponents());
   }//GEN-LAST:event_colorEditor0StateChanged

   private void colorEditor1StateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_colorEditor1StateChanged
   {//GEN-HEADEREND:event_colorEditor1StateChanged
      map.setAdjusting(colorEditor1.isAdjusting());
      map.setColorTables(colorEditor0.getColorComponents(), colorEditor1.getColorComponents());
   }//GEN-LAST:event_colorEditor1StateChanged

   private void colorEditor00StateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_colorEditor00StateChanged
   {//GEN-HEADEREND:event_colorEditor00StateChanged
      map.setAdjusting(colorEditor00.isAdjusting());
      map.setColorTables(colorEditor00.getColorComponents(), colorEditor05.getColorComponents(), colorEditor10.getColorComponents());
   }//GEN-LAST:event_colorEditor00StateChanged

   private void colorEditor05StateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_colorEditor05StateChanged
   {//GEN-HEADEREND:event_colorEditor05StateChanged
      map.setAdjusting(colorEditor05.isAdjusting());
      map.setColorTables(colorEditor00.getColorComponents(), colorEditor05.getColorComponents(), colorEditor10.getColorComponents());
   }//GEN-LAST:event_colorEditor05StateChanged

   private void colorEditor10StateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_colorEditor10StateChanged
   {//GEN-HEADEREND:event_colorEditor10StateChanged
       map.setAdjusting(colorEditor10.isAdjusting());
       map.setColorTables(colorEditor00.getColorComponents(), colorEditor05.getColorComponents(), colorEditor10.getColorComponents());
   }//GEN-LAST:event_colorEditor10StateChanged

   public boolean isAdjusting() {
       return stepSlider.getValueIsAdjusting() || brightnessEditor.isAdjusting() ||
              colorEditor0.isAdjusting() || colorEditor1.isAdjusting() || 
              colorEditor00.isAdjusting() || colorEditor05.isAdjusting() || colorEditor10.isAdjusting();
   }
   
   @Override
   public void setEnabled(boolean enabled) {
       super.setEnabled(enabled);
       colorMapCombo.setEnabled(enabled);
       reverseCheckBox.setEnabled(enabled);
       brightnessEditor.setEnabled(enabled);
       stepSlider.setEnabled(enabled);
       repaint();       
   }
       
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private pl.edu.icm.visnow.datamaps.BrightnessEditor brightnessEditor;
    private javax.swing.JPanel cmap2EditPanel;
    private javax.swing.JPanel cmap3EditPanel;
    private pl.edu.icm.visnow.gui.widgets.ColorEditor colorEditor0;
    private pl.edu.icm.visnow.gui.widgets.ColorEditor colorEditor00;
    private pl.edu.icm.visnow.gui.widgets.ColorEditor colorEditor05;
    private pl.edu.icm.visnow.gui.widgets.ColorEditor colorEditor1;
    private pl.edu.icm.visnow.gui.widgets.ColorEditor colorEditor10;
    private javax.swing.JComboBox colorMapCombo;
    private javax.swing.JCheckBox reverseCheckBox;
    private javax.swing.JPanel stdEditPanel;
    private javax.swing.JSlider stepSlider;
    // End of variables declaration//GEN-END:variables

    private void processSelectedIndex(int k) {
      if (map.getMap().getName().equalsIgnoreCase("bicolor"))
      {
         stdEditPanel.setVisible(false);
         cmap2EditPanel.setVisible(true);
         cmap3EditPanel.setVisible(false);
      }
      else if (map.getMap().getName().equalsIgnoreCase("tricolor"))
      {
         stdEditPanel.setVisible(false);
         cmap2EditPanel.setVisible(false);
         cmap3EditPanel.setVisible(true);
      }
      else
      {
         stdEditPanel.setVisible(true);
         cmap2EditPanel.setVisible(false);
         cmap3EditPanel.setVisible(false);
      }
      brightnessEditor.setMap(map);
    }
    
    
    public static void main(String[] args) {
        VisNow.initLogging(true);

        JFrame f = new JFrame();
        f.setLocation(400, 200);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        final ColormapChooser p = new ColormapChooser();
        f.add(p);
        f.pack();
        f.addComponentListener(new ComponentAdapter() {
            private int step = 2;

//            @Override
//            public void componentResized(ComponentEvent e) {
//                componentMoved(e);
//            }
            
            @Override
            public void componentMoved(ComponentEvent e) {
                step = ++step % 3;
                int ind = 4 - step * step;
                p.colorMapCombo.setSelectedIndex(ind);
            }
        });
        f.setVisible(true);

    }
}


