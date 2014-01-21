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

package pl.edu.icm.visnow.geometries.gui;

import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.vecmath.Matrix3f;
import javax.vecmath.Vector3f;
import pl.edu.icm.visnow.datasets.IrregularField;
import pl.edu.icm.visnow.geometries.objects.SignalingTransform3D;
import pl.edu.icm.visnow.geometries.parameters.IrregularFieldDisplayParams;
import pl.edu.icm.visnow.lib.gui.ChangeFiringGUI;
import pl.edu.icm.visnow.system.main.VisNow;
import static java.awt.GridBagConstraints.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

/**
 *
 * @author  Krzysztof S. Nowinski, University of Warsaw, ICM
 */
public class IrregularFieldPresentationGUI extends javax.swing.JPanel
{
   private boolean debug = VisNow.isDebug();
   protected IrregularField inField = null;
   protected int nScalarComps = 0;
   protected int nScalarCellComps = 0;
   protected boolean active = true;
   protected IrregularFieldDisplayParams params = null;
   protected Matrix3f rotMatrix = new Matrix3f(1, 0, 0, 0, 1, 0, 0, 0, 1);
   protected Vector3f transVector = new Vector3f();
   protected float scale = 1.f;
   protected ChangeFiringGUI parentGUI = null;

   /** Creates new form ColoringGUI */
   public IrregularFieldPresentationGUI()
   {
      initComponents();
      dataMappingGUI.setStartNullTransparencyComponent(true);
   }
   
   
   public IrregularFieldPresentationGUI(IrregularField inField, IrregularFieldDisplayParams params)
   {
      initComponents();
      active = false;
      if (params == null || inField == null)
         return;
      this.params = params;
      this.inField = inField;
      dataMappingGUI.setInData(inField, params);
      transformGUI.setTransformParams(params.getTransformParams());
      transformGUI.setTransSensitivity(inField.getDiameter()/500);
      displayPropertiesGUI.setRenderingParams(params);
      displayPropertiesGUI.setNDims(inField.getNCellDims());
      active = true;
   }

    public void setPresentation(boolean simple) {
        Insets insets0 = new Insets(0, 0, 0, 0);
        dataMappingGUI.setPresentation(simple);
        displayPropertiesGUI.setPresentation(simple);

        if (simple) {
            simplePanel.add(dataMappingGUI, new GridBagConstraints(0, 1, 1, 1, 1, 0, NORTH, HORIZONTAL, insets0, 0, 0));
            if (dataMappingGUI.isTransparencyStartNull())
                simplePanel.add(displayPropertiesGUI, new GridBagConstraints(0, 2, 1, 1, 1, 0, NORTH, HORIZONTAL, insets0, 0, 0));
        } else {
            dataMappingPanel.add(dataMappingGUI, java.awt.BorderLayout.NORTH);
            displayPropertiesPanel.add(displayPropertiesGUI, java.awt.BorderLayout.NORTH);
        }
        ((CardLayout) getLayout()).show(this, simple ? "simpleUI" : "extendedUI");        
        revalidate();
        repaint();
    }

   
   /** This method is called from within the constructor to
    * initialize the form.
    * WARNING: Do NOT modify this code. The content of this method is
    * always regenerated by the Form Editor.
    */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        extendedTabbedPane = new javax.swing.JTabbedPane();
        dataMappingScrollPane = new javax.swing.JScrollPane();
        dataMappingPanel = new javax.swing.JPanel();
        dataMappingGUI = new pl.edu.icm.visnow.geometries.gui.DataMappingGUI();
        displayPropertiesScrollPane = new javax.swing.JScrollPane();
        displayPropertiesPanel = new javax.swing.JPanel();
        displayPropertiesGUI = new pl.edu.icm.visnow.geometries.gui.DisplayPropertiesGUI();
        transformScrollPane = new javax.swing.JScrollPane();
        transformPanel = new javax.swing.JPanel();
        transformGUI = new pl.edu.icm.visnow.geometries.gui.TransformPanel();
        simpleScrollPane = new javax.swing.JScrollPane();
        simplePanel = new javax.swing.JPanel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));

        setName(""); // NOI18N
        setRequestFocusEnabled(false);
        setLayout(new java.awt.CardLayout());

        extendedTabbedPane.setToolTipText("regular field presentation");
        extendedTabbedPane.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N

        dataMappingScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        dataMappingPanel.setLayout(new java.awt.BorderLayout());

        dataMappingGUI.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        dataMappingPanel.add(dataMappingGUI, java.awt.BorderLayout.NORTH);

        dataMappingScrollPane.setViewportView(dataMappingPanel);

        extendedTabbedPane.addTab("datamap", dataMappingScrollPane);

        displayPropertiesScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        displayPropertiesPanel.setLayout(new java.awt.BorderLayout());
        displayPropertiesPanel.add(displayPropertiesGUI, java.awt.BorderLayout.NORTH);

        displayPropertiesScrollPane.setViewportView(displayPropertiesPanel);

        extendedTabbedPane.addTab("display", displayPropertiesScrollPane);

        transformScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        transformPanel.setLayout(new java.awt.BorderLayout());
        transformPanel.add(transformGUI, java.awt.BorderLayout.NORTH);

        transformScrollPane.setViewportView(transformPanel);

        extendedTabbedPane.addTab("transform", transformScrollPane);

        add(extendedTabbedPane, "extendedUI");

        simpleScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        simplePanel.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 99;
        gridBagConstraints.weighty = 1.0;
        simplePanel.add(filler1, gridBagConstraints);

        simpleScrollPane.setViewportView(simplePanel);

        add(simpleScrollPane, "simpleUI");
    }// </editor-fold>//GEN-END:initComponents

   public void setSignalingTransform(SignalingTransform3D sigTrans)
   {
      transformGUI.setSigTrans(sigTrans);
   }

   public void fireStateChanged()
   {
      if (inField == null || params == null || !active)
         return;
      int mode = 0;
      params.setDisplayMode(mode);
      params.setActive(true);
   }

   public DisplayPropertiesGUI getDisplayPropertiesGUI()
   {
      return displayPropertiesGUI;
   }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private pl.edu.icm.visnow.geometries.gui.DataMappingGUI dataMappingGUI;
    private javax.swing.JPanel dataMappingPanel;
    private javax.swing.JScrollPane dataMappingScrollPane;
    private pl.edu.icm.visnow.geometries.gui.DisplayPropertiesGUI displayPropertiesGUI;
    private javax.swing.JPanel displayPropertiesPanel;
    private javax.swing.JScrollPane displayPropertiesScrollPane;
    private javax.swing.JTabbedPane extendedTabbedPane;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JPanel simplePanel;
    private javax.swing.JScrollPane simpleScrollPane;
    private pl.edu.icm.visnow.geometries.gui.TransformPanel transformGUI;
    private javax.swing.JPanel transformPanel;
    private javax.swing.JScrollPane transformScrollPane;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the dataMappingGUI
     */
    public pl.edu.icm.visnow.geometries.gui.DataMappingGUI getDataMappingGUI() {
        return dataMappingGUI;
    }

    public static void main(String[] args) {
        UIManager.getDefaults().put("TabbedPane.contentBorderInsets", new Insets(4,0,0,0));
        
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        final IrregularFieldPresentationGUI p = new IrregularFieldPresentationGUI();
        f.add(p);
        f.pack();
        f.addComponentListener(new ComponentAdapter() {
            private boolean toggleSimple = true;

            @Override
            public void componentMoved(ComponentEvent e) {
                p.setPresentation(toggleSimple);
                toggleSimple = !toggleSimple;
            }
        });
        f.setVisible(true);
    }
}