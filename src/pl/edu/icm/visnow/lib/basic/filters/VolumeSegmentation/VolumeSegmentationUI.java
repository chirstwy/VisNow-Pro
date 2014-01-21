//<editor-fold defaultstate="collapsed" desc=" COPYRIGHT AND LICENSE ">
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
//</editor-fold>

package pl.edu.icm.visnow.lib.basic.filters.VolumeSegmentation;

import java.awt.Dimension;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import pl.edu.icm.visnow.datasets.RegularField;
import pl.edu.icm.visnow.datasets.dataarrays.DataArray;
import pl.edu.icm.visnow.lib.basic.viewers.FieldViewer3D.DataProvider.DataProviderParams;
import pl.edu.icm.visnow.system.main.VisNow;

/**
 *
 * @author  Krzysztof S. Nowinski, University of Warsaw, ICM
 */
public class VolumeSegmentationUI extends JPanel
{
   private static final String[] selectionTableHeader = new String[]
   {
      "selection", "vol (vx)", "vol", "% vol"
   };
   private static final Class[] selectionTableTypes = new Class[]
   {
      String.class, Integer.class, Float.class, Float.class
   };
   private static final int[] selectionColumnWidth = new int[]{120,120,80,50};
   private static final String VOL_FILE_PROPERTY = "visnow.paths.volumeSegmentation.volumeFile";
   private DefaultTableModel selectionTableModel = new DefaultTableModel(selectionTableHeader, 0);
   private int nVoxels = 0;
   private float voxelVol = 1;

   protected RegularField inField = null;
   protected VolumeSegmentation parentModule  = null;
   protected Vector<String> segmentedSetNames = new Vector<String>();
   protected Vector<String> allowedNames      = new Vector<String>();
   protected ArrayList<Integer> componentIndices = new ArrayList<Integer>();
   protected float bgrThrLow = 0;
   protected float bgrThrUp = 255;
   protected boolean bgrInvert = false;
   protected DataArray da;

   protected ChangeListener backgroundChangeListener = new ChangeListener()
   {
      @Override
      public void stateChanged(ChangeEvent e)
      {
         if(!(e.getSource() instanceof DataProviderParams))
             return;

         bgrThrLow = ((DataProviderParams)e.getSource()).getSimpleOverlayLow();
         bgrThrUp = ((DataProviderParams)e.getSource()).getSimpleOverlayUp();
         bgrInvert = ((DataProviderParams)e.getSource()).isSimpleOverlayInvert();
         //backgroundThresholdButton.setText("set as background voxels in range ["+bgrThrLow+","+bgrThrUp+"]");
      }
   };
   /** Creates new form VolRenderUI */
   @SuppressWarnings("unchecked")
   public VolumeSegmentationUI()
   {
      initComponents();
      segmentedSetNames.clear();
      segmentedSetNames.add("unassigned");
      segmentedSetList.setListData(segmentedSetNames);
      segmentedSetList.setSelectedIndex(0);
      denoiseModeButton.setTexts(new String[]{"no denoising", "denoise slices", "denoise volume"});
      denoiseModeButton.setIcons(null);
      allowedNames.clear();
      allowedNames.add("unassigned");
      allowedNames.add("background");
      allowedList.setListData(allowedNames);
      allowedList.setSelectedIndex(0);
      for (int i = 0; i < selectionColumnWidth.length; i++)
         selectionTable.getColumnModel().getColumn(i).setPreferredWidth(selectionColumnWidth[i]);
      selectionTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
      dataComponentSelector.setScalarComponentsOnly(true);
   }

   /** This method is called from within the constructor to
    * initialize the form.
    * WARNING: Do NOT modify this code. The content of this method is
    * always regenerated by the Form Editor.
    */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        selectFieldGroup = new javax.swing.ButtonGroup();
        modeGroup = new javax.swing.ButtonGroup();
        helpPane = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        volumeFileChooser = new javax.swing.JFileChooser();
        clearPointsButton = new javax.swing.JButton();
        resetButton = new javax.swing.JButton();
        outputResultsButton = new javax.swing.JButton();
        acceptSelectionButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        backgroundThresholdButton = new javax.swing.JButton();
        removeFromButton = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        extentSlider = new pl.edu.icm.visnow.gui.widgets.ExtendedSlider();
        computeSimilarityButton = new javax.swing.JButton();
        distSlicesBox = new javax.swing.JCheckBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        componentTable = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        computeRangeSegmentationButton = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        dataComponentSelector = new pl.edu.icm.visnow.lib.gui.DataComponentSelector();
        extentSlider1 = new pl.edu.icm.visnow.gui.widgets.ExtendedSlider();
        clearPointButton = new javax.swing.JButton();
        cropButton = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        selectionTable = new javax.swing.JTable();
        denoiseModeButton = new pl.edu.icm.visnow.gui.widgets.MultistateButton();
        outputVolumesButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        viewer3DContentComboBox = new javax.swing.JComboBox();
        jPanel6 = new javax.swing.JPanel();
        segmentedSetNameField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        segmentedSetList = new javax.swing.JList();
        jScrollPane3 = new javax.swing.JScrollPane();
        allowedList = new javax.swing.JList();
        rangeSlider = new pl.edu.icm.visnow.gui.widgets.FloatSubRangeSlider.ExtendedFloatSubRangeSlider();
        thresholdSlider = new javax.swing.JSlider();

        helpPane.setPreferredSize(new java.awt.Dimension(400, 300));

        jTextArea1.setColumns(20);
        jTextArea1.setEditable(false);
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(200);
        jTextArea1.setText("The module contains tools for segmentation of multiple subsets and editing them.\nBasic segmentation:\nstart with ");
        jTextArea1.setWrapStyleWord(true);
        jTextArea1.setAutoscrolls(false);
        helpPane.setViewportView(jTextArea1);

        setMinimumSize(new java.awt.Dimension(250, 970));
        setPreferredSize(new java.awt.Dimension(260, 970));
        setLayout(new java.awt.GridBagLayout());

        clearPointsButton.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        clearPointsButton.setForeground(new java.awt.Color(153, 0, 51));
        clearPointsButton.setText("clear last selection");
        clearPointsButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        clearPointsButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        clearPointsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearPointsButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(clearPointsButton, gridBagConstraints);

        resetButton.setForeground(new java.awt.Color(153, 0, 0));
        resetButton.setText("reset segmentation");
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        add(resetButton, gridBagConstraints);

        outputResultsButton.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        outputResultsButton.setText("output field");
        outputResultsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                outputResultsButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        add(outputResultsButton, gridBagConstraints);

        acceptSelectionButton.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        acceptSelectionButton.setText("accept/add to ");
        acceptSelectionButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        acceptSelectionButton.setMargin(new java.awt.Insets(2, 14, 2, 2));
        acceptSelectionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                acceptSelectionButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(acceptSelectionButton, gridBagConstraints);

        jPanel2.setLayout(new java.awt.BorderLayout());

        backgroundThresholdButton.setText("set background voxels (overlay)");
        backgroundThresholdButton.setMargin(new java.awt.Insets(2, 1, 2, 1));
        backgroundThresholdButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backgroundThresholdButtonActionPerformed(evt);
            }
        });
        jPanel2.add(backgroundThresholdButton, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(jPanel2, gridBagConstraints);

        removeFromButton.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        removeFromButton.setText("remove from");
        removeFromButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        removeFromButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeFromButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(removeFromButton, gridBagConstraints);

        jTabbedPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Selection methods and parameters", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 10))); // NOI18N
        jTabbedPane1.setMinimumSize(new java.awt.Dimension(193, 200));
        jTabbedPane1.setPreferredSize(new java.awt.Dimension(259, 239));

        jPanel3.setLayout(new java.awt.GridBagLayout());

        extentSlider.setScaleType(pl.edu.icm.visnow.gui.widgets.ExtendedSlider.ScaleType.LOGARITHMIC);
        extentSlider.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "max computing steps", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 10))); // NOI18N
        extentSlider.setMax(1000.0F);
        extentSlider.setMin(10.0F);
        extentSlider.setMinimumSize(new java.awt.Dimension(90, 65));
        extentSlider.setPreferredSize(new java.awt.Dimension(200, 67));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        jPanel3.add(extentSlider, gridBagConstraints);

        computeSimilarityButton.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        computeSimilarityButton.setText("compute similarity ");
        computeSimilarityButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        computeSimilarityButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                computeSimilarityButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        jPanel3.add(computeSimilarityButton, gridBagConstraints);

        distSlicesBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        distSlicesBox.setText("show similarity field slices");
        distSlicesBox.setEnabled(false);
        distSlicesBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                distSlicesBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel3.add(distSlicesBox, gridBagConstraints);

        jScrollPane2.setMinimumSize(new java.awt.Dimension(160, 60));
        jScrollPane2.setPreferredSize(new java.awt.Dimension(203, 120));

        componentTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"component 0",  new Float(1.0)}
            },
            new String [] {
                "data component", "weight"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Float.class
            };
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(componentTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        jPanel3.add(jScrollPane2, gridBagConstraints);

        jTabbedPane1.addTab("similarity", jPanel3);

        jPanel4.setLayout(new java.awt.GridBagLayout());

        computeRangeSegmentationButton.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        computeRangeSegmentationButton.setText("compute");
        computeRangeSegmentationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                computeRangeSegmentationButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        jPanel4.add(computeRangeSegmentationButton, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        jPanel4.add(jPanel5, gridBagConstraints);

        dataComponentSelector.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                dataComponentSelectorStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel4.add(dataComponentSelector, gridBagConstraints);

        extentSlider1.setScaleType(pl.edu.icm.visnow.gui.widgets.ExtendedSlider.ScaleType.LOGARITHMIC);
        extentSlider1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "max distance", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 10))); // NOI18N
        extentSlider1.setMax(1000.0F);
        extentSlider1.setMin(10.0F);
        extentSlider1.setMinimumSize(new java.awt.Dimension(90, 65));
        extentSlider1.setPreferredSize(new java.awt.Dimension(200, 67));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        jPanel4.add(extentSlider1, gridBagConstraints);

        jTabbedPane1.addTab("range", jPanel4);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        add(jTabbedPane1, gridBagConstraints);

        clearPointButton.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        clearPointButton.setText("clear last point");
        clearPointButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        clearPointButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearPointButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(clearPointButton, gridBagConstraints);

        cropButton.setText("crop (volume renderer)");
        cropButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cropButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        add(cropButton, gridBagConstraints);

        jScrollPane4.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane4.setMinimumSize(new java.awt.Dimension(180, 80));
        jScrollPane4.setPreferredSize(new java.awt.Dimension(220, 300));

        selectionTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "selection", "vol (vx)", "vol", "% vol"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.Float.class, java.lang.Float.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        selectionTable.setMinimumSize(new java.awt.Dimension(370, 64));
        selectionTable.setPreferredSize(new java.awt.Dimension(390, 500));
        selectionTable.setRequestFocusEnabled(false);
        jScrollPane4.setViewportView(selectionTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jScrollPane4, gridBagConstraints);

        denoiseModeButton.setText("multistateButton1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        add(denoiseModeButton, gridBagConstraints);

        outputVolumesButton.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        outputVolumesButton.setText("save volumes");
        outputVolumesButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        outputVolumesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                outputVolumesButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LAST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        add(outputVolumesButton, gridBagConstraints);

        jLabel1.setText("output segmentation results");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 0, 0);
        add(jLabel1, gridBagConstraints);

        viewer3DContentComboBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        viewer3DContentComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "show data volume", "show computed similarity distance", "show result field" }));
        viewer3DContentComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                viewer3DContentComboBoxItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 7, 0);
        add(viewer3DContentComboBox, gridBagConstraints);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "selection areas", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 10))); // NOI18N
        jPanel6.setMinimumSize(new java.awt.Dimension(180, 220));
        jPanel6.setPreferredSize(new java.awt.Dimension(200, 350));
        jPanel6.setLayout(new java.awt.GridBagLayout());

        segmentedSetNameField.setMinimumSize(new java.awt.Dimension(4, 25));
        segmentedSetNameField.setPreferredSize(new java.awt.Dimension(66, 25));
        segmentedSetNameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                segmentedSetNameFieldActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel6.add(segmentedSetNameField, gridBagConstraints);

        jLabel4.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel4.setText("new selection:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        jPanel6.add(jLabel4, gridBagConstraints);

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel3.setText("allow selection from:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 2, 0);
        jPanel6.add(jLabel3, gridBagConstraints);

        jScrollPane1.setMinimumSize(new java.awt.Dimension(180, 100));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(200, 200));

        segmentedSetList.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        segmentedSetList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "background", " " };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        segmentedSetList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        segmentedSetList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                segmentedSetListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(segmentedSetList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel6.add(jScrollPane1, gridBagConstraints);

        jScrollPane3.setMinimumSize(new java.awt.Dimension(180, 122));
        jScrollPane3.setPreferredSize(new java.awt.Dimension(200, 150));

        allowedList.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        allowedList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "unassigned", "free" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        allowedList.setMaximumSize(new java.awt.Dimension(300, 200));
        allowedList.setMinimumSize(new java.awt.Dimension(180, 60));
        allowedList.setPreferredSize(new java.awt.Dimension(200, 100));
        jScrollPane3.setViewportView(allowedList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.7;
        jPanel6.add(jScrollPane3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        add(jPanel6, gridBagConstraints);

        rangeSlider.setBorder(javax.swing.BorderFactory.createTitledBorder("valid values range"));
        rangeSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rangeSliderStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(rangeSlider, gridBagConstraints);

        thresholdSlider.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        thresholdSlider.setMajorTickSpacing(20);
        thresholdSlider.setMinorTickSpacing(2);
        thresholdSlider.setPaintLabels(true);
        thresholdSlider.setPaintTicks(true);
        thresholdSlider.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "similarity threshold", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 10))); // NOI18N
        thresholdSlider.setPreferredSize(new java.awt.Dimension(250, 64));
        thresholdSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                thresholdSliderStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 2, 0);
        add(thresholdSlider, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents


   public void setPresentation(boolean simple)
   {
//      allowedList.setVisible(!simple);
      backgroundThresholdButton.setVisible(!simple);
      componentTable.setVisible(!simple);
      cropButton.setVisible(!simple);
//      jLabel3.setVisible(!simple);
      jPanel2.setVisible(!simple);
      jScrollPane2.setVisible(!simple);
//      jScrollPane3.setVisible(!simple);
      if (simple)
      {
         Dimension simpleDim = new Dimension(200, 160);
         jScrollPane4.setMinimumSize(simpleDim);
         jScrollPane4.setPreferredSize(simpleDim);
      }
      validate();
   }

   private void thresholdSliderStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_thresholdSliderStateChanged
   {//GEN-HEADEREND:event_thresholdSliderStateChanged
      if (parentModule == null || thresholdSlider.getValueIsAdjusting())
         return;
      parentModule.setThreshold(thresholdSlider.getValue());
   }//GEN-LAST:event_thresholdSliderStateChanged

   private void computeSimilarityButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_computeSimilarityButtonActionPerformed
   {//GEN-HEADEREND:event_computeSimilarityButtonActionPerformed
      if (parentModule != null && segmentedSetList.getSelectedIndex() >= 0)
      {
         boolean[] allowed = new boolean[allowedList.getModel().getSize()];
         for (int i = 0; i < allowed.length; i++)
            allowed[i] = false;
         for (int i = 0; i < allowed.length; i++)
            allowed[i] = allowedList.isSelectedIndex(i);
         float[] weights = new float[componentTable.getRowCount()];
         for (int i = 0; i < componentTable.getRowCount(); i++)
            weights[i] = (Float)componentTable.getValueAt(i, 1);

         parentModule.computeSimilarityField(allowed, componentIndices, weights, (int)extentSlider.getVal());
      }
      distSlicesBox.setEnabled(true);
   }//GEN-LAST:event_computeSimilarityButtonActionPerformed

   private void clearPointsButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_clearPointsButtonActionPerformed
   {//GEN-HEADEREND:event_clearPointsButtonActionPerformed
      if (parentModule != null)
         parentModule.resetSelection();
      distSlicesBox.setEnabled(false);
   }//GEN-LAST:event_clearPointsButtonActionPerformed

   private void distSlicesBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_distSlicesBoxActionPerformed
      if (parentModule != null)
         parentModule.showDistFieldSlices(distSlicesBox.isSelected());
   }//GEN-LAST:event_distSlicesBoxActionPerformed

   @SuppressWarnings("unchecked")
   private void resetButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_resetButtonActionPerformed
   {//GEN-HEADEREND:event_resetButtonActionPerformed
      segmentedSetNames.clear();
      segmentedSetNames.add("background");
      segmentedSetList.setListData(segmentedSetNames);
      segmentedSetList.setSelectedIndex(0);
      allowedNames.clear();
      allowedNames.add("unassigned");
      allowedNames.add("background");
      allowedList.setListData(allowedNames);
      allowedList.setSelectedIndex(0);
      if (parentModule != null)
         parentModule.reset();
}//GEN-LAST:event_resetButtonActionPerformed

   private void outputResultsButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_outputResultsButtonActionPerformed
   {//GEN-HEADEREND:event_outputResultsButtonActionPerformed
      if (parentModule != null)
         parentModule.outputResultField();
}//GEN-LAST:event_outputResultsButtonActionPerformed

   private void acceptSelectionButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_acceptSelectionButtonActionPerformed
   {//GEN-HEADEREND:event_acceptSelectionButtonActionPerformed
      if (parentModule != null)
         parentModule.addSelection();
      updateSelectionTable();
      fireSelectionDone();
}//GEN-LAST:event_acceptSelectionButtonActionPerformed

   private void segmentedSetListValueChanged(javax.swing.event.ListSelectionEvent evt)//GEN-FIRST:event_segmentedSetListValueChanged
   {//GEN-HEADEREND:event_segmentedSetListValueChanged
      acceptSelectionButton.setText("accept/add to "+segmentedSetList.getSelectedValue());
      removeFromButton.setText("cut from "+segmentedSetList.getSelectedValue());
      if (parentModule != null)
         parentModule.setSegIndex(segmentedSetList.getSelectedIndex());
   }//GEN-LAST:event_segmentedSetListValueChanged

   private void removeFromButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_removeFromButtonActionPerformed
   {//GEN-HEADEREND:event_removeFromButtonActionPerformed
      if (parentModule != null)
         parentModule.subtractSelection();
      updateSelectionTable();
   }//GEN-LAST:event_removeFromButtonActionPerformed

   @SuppressWarnings("unchecked")
   private void segmentedSetNameFieldActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_segmentedSetNameFieldActionPerformed
   {//GEN-HEADEREND:event_segmentedSetNameFieldActionPerformed
      if (segmentedSetNameField.getText().isEmpty())
         return;
      segmentedSetNames.add(segmentedSetNameField.getText());
      segmentedSetList.setListData(segmentedSetNames);
      segmentedSetList.setSelectedIndex(segmentedSetNames.size() - 1);
      allowedNames.add(segmentedSetNameField.getText());
      allowedList.setListData(allowedNames);
      allowedList.setSelectedIndex(0);
      if (parentModule != null)
         parentModule.setMaxSegNumber(allowedNames.size());
      segmentedSetNameField.setText("");
   }//GEN-LAST:event_segmentedSetNameFieldActionPerformed

   private void clearPointButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_clearPointButtonActionPerformed
   {//GEN-HEADEREND:event_clearPointButtonActionPerformed
      if (parentModule != null)
         parentModule.clearLastPoint();
}//GEN-LAST:event_clearPointButtonActionPerformed

   private void cropButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cropButtonActionPerformed
   {//GEN-HEADEREND:event_cropButtonActionPerformed
      if (parentModule != null)
         parentModule.cropField();
   }//GEN-LAST:event_cropButtonActionPerformed

   private void backgroundThresholdButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_backgroundThresholdButtonActionPerformed
   {//GEN-HEADEREND:event_backgroundThresholdButtonActionPerformed
      if (parentModule != null)
         parentModule.setBackgroundThresholdRange(bgrThrLow, bgrThrUp, bgrInvert);
}//GEN-LAST:event_backgroundThresholdButtonActionPerformed

   private void outputVolumesButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_outputVolumesButtonActionPerformed
   {//GEN-HEADEREND:event_outputVolumesButtonActionPerformed
      if (parentModule == null || parentModule.getInField() == null)
         return;
      String fileName = VisNow.get().getMainConfig().getProperty(VOL_FILE_PROPERTY);
      if (fileName != null)
         volumeFileChooser.setSelectedFile(new File(fileName));
      else
         volumeFileChooser.setCurrentDirectory(new File(VisNow.get().getMainConfig().getDefaultDataPath()));
      int returnVal = volumeFileChooser.showOpenDialog(this);
      if (returnVal == JFileChooser.APPROVE_OPTION)
      {
         fileName = volumeFileChooser.getSelectedFile().getAbsolutePath();
         try
         {
            PrintWriter resWriter = new PrintWriter(new FileWriter(fileName, true));
            VisNow.get().getMainConfig().setProperty(VOL_FILE_PROPERTY, fileName);
            resWriter.print(parentModule.getInField().getData(0).getName());
            for (int i = 1; i < selectionTable.getRowCount(); i++)
            {
               for (int j = 0; j < selectionTable.getColumnCount(); j++)
                  resWriter.print(";  " + selectionTable.getModel().getValueAt(i, j).toString());
            }
            resWriter.println();
            resWriter.close();
         } catch (Exception e)
         {
         }
      }
   }//GEN-LAST:event_outputVolumesButtonActionPerformed

   private void viewer3DContentComboBoxItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_viewer3DContentComboBoxItemStateChanged
   {//GEN-HEADEREND:event_viewer3DContentComboBoxItemStateChanged
      parentModule.renderVolume(viewer3DContentComboBox.getSelectedIndex());
   }//GEN-LAST:event_viewer3DContentComboBoxItemStateChanged

   private void computeRangeSegmentationButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_computeRangeSegmentationButtonActionPerformed
   {//GEN-HEADEREND:event_computeRangeSegmentationButtonActionPerformed
      if (parentModule != null && segmentedSetList.getSelectedIndex() >= 0)
      {
         boolean[] allowed = new boolean[allowedList.getModel().getSize()];
         for (int i = 0; i < allowed.length; i++)
            allowed[i] = false;
         for (int i = 0; i < allowed.length; i++)
            allowed[i] = allowedList.isSelectedIndex(i);
         float[] weights = new float[componentTable.getRowCount()];
         for (int i = 0; i < componentTable.getRowCount(); i++)
            weights[i] = (Float)componentTable.getValueAt(i, 1);
         parentModule.computeDistanceField(allowed, dataComponentSelector.getComponent(), (int)extentSlider1.getVal());
      }
      distSlicesBox.setEnabled(true);
   }//GEN-LAST:event_computeRangeSegmentationButtonActionPerformed

   private void rangeSliderStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_rangeSliderStateChanged
   {//GEN-HEADEREND:event_rangeSliderStateChanged
      if (parentModule == null || rangeSlider.isAdjusting() || da == null)
         return;
      float d = (da.getMaxv() - da.getMinv()) / (da.getPhysMax() - da.getPhysMin());
      parentModule.setRange(da.getMinv() + d *(rangeSlider.getLow() -da.getPhysMin()),
                            da.getMinv() + d *(rangeSlider.getUp() -da.getPhysMin()));
   }//GEN-LAST:event_rangeSliderStateChanged

   private void dataComponentSelectorStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_dataComponentSelectorStateChanged
   {//GEN-HEADEREND:event_dataComponentSelectorStateChanged
      DataArray da = inField.getData(dataComponentSelector.getComponent());
      rangeSlider.setMinMax(da.getPhysMin(), da.getPhysMax());
   }//GEN-LAST:event_dataComponentSelectorStateChanged

   public void setParentModule(VolumeSegmentation parentModule)
   {
      this.parentModule = parentModule;
   }

   public void setInfield (RegularField in)
   {
      DefaultTableModel componentTableModel =
              new DefaultTableModel(new Object[][]{ },
                                    new String[]{"data component", "weight"})
      {
         Class[] types = new Class[]{String.class, Float.class};
         boolean[] canEdit = new boolean[]{false, true};
      };
      componentIndices.clear();

      inField = in;
      for (int i = 0; i < in.getNData(); i++)
         if (in.getData(i).isSimpleNumeric() && in.getData(i).getVeclen() == 1)
         {
            componentIndices.add(i);
            componentTableModel.addRow(new Object[] {in.getData(i).getName(), 1.f});
         }
      componentTable.setModel (componentTableModel);
      int[] dims = in.getDims();
      nVoxels = 1;
      for (int i = 0; i < dims.length; i++)
         nVoxels *= dims[i];
      float[][] a = in.getAffine();
      voxelVol = Math.abs(a[0][0] * a[1][1] * a[2][2] + a[0][1] * a[1][2] * a[2][0] + a[0][2] * a[1][0] * a[2][1] -
                          a[0][2] * a[1][1] * a[2][0] - a[0][0] * a[1][2] * a[2][1] - a[0][1] * a[1][0] * a[2][2]);
      selectionTableModel.addRow(new Object[] {"unassigned", nVoxels, nVoxels * voxelVol, 100});
      selectionTable.setModel(selectionTableModel);
      dataComponentSelector.setDataSchema(in.getDataSchema());
      da = in.getData(dataComponentSelector.getComponent());
      rangeSlider.setParams(da.getPhysMin(), da.getPhysMin(), da.getPhysMax(), da.getPhysMax());
   }

   private void updateSelectionTable()
   {
      if (parentModule == null)
         return;
      selectionTableModel = new DefaultTableModel(selectionTableHeader, 0);
      int[] voxelCounts = parentModule.getVolumes();
      for (int i = 0; i < voxelCounts.length; i++)
      {
         int j = voxelCounts[i];
         if (j == 0) continue;
         if (i == 0)
            selectionTableModel.addRow(new Object[] {"unassigned", j, j * voxelVol, (100.f * j) / nVoxels});
         else
            selectionTableModel.addRow(new Object[] {segmentedSetNames.get(i - 1), j, j * voxelVol, (100.f * j) / nVoxels});
      }
      selectionTable.setModel(selectionTableModel);
   }

   public boolean presmooth()
   {
      return (denoiseModeButton.getState() == 2);
   }

   public boolean presmoothSlices()
   {
      return (denoiseModeButton.getState() == 1);
   }

   public int getExtent()
   {
      return (int) extentSlider.getVal();
   }

   public ArrayList<String> getAllowedNames()
   {
      ArrayList<String> out = new ArrayList<String>();
      out.addAll(allowedNames);
      return out;
   }

   public JScrollPane getHelpPane()
   {
      return helpPane;
   }

   public ChangeListener getBackgroundChangeListener()
   {
      return backgroundChangeListener;
   }

   public void setDenoise(int i)
   {
      denoiseModeButton.setState(i);
   }

   public void setSetName(String set)
   {
      segmentedSetNameField.setText(set);
      segmentedSetNameFieldActionPerformed(null);
   }

   public void setSetAllowed(int[] allowed)
   {
      int maxAllowed = allowedList.getModel().getSize();
      allowedList.clearSelection();
      for (int i = 0; i < allowed.length; i++)
      {
         int j = allowed[i];
         if (j >=0 && j < maxAllowed)
            allowedList.addSelectionInterval(j, j);
      }
   }

   public void outputVolumes()
   {
      outputVolumesButtonActionPerformed(null);
   }
   //</editor-fold>
   //<editor-fold defaultstate="collapsed" desc=" Know Change listeners ">
   /**
    * Utility field holding list of ChangeListeners.
    */
   protected transient ArrayList<ChangeListener> changeListenerList = new ArrayList<ChangeListener>();

   /**
    * Registers ChangeListener to receive events.
    * @param listener The listener to register.
    */
   public synchronized void addSelectionDoneListener(ChangeListener listener)
   {
      changeListenerList.add(listener);
   }

   /**
    * Removes ChangeListener from the list of listeners.
    * @param listener The listener to remove.
    */
   public synchronized void removeSelectionDoneListener(ChangeListener listener)
   {
      changeListenerList.remove(listener);
   }

   /**
    * Notifies all registered listeners about the event.
    *
    * @param object Parameter #1 of the <CODE>ChangeEvent<CODE> constructor.
    */
   public void fireSelectionDone()
   {
      ChangeEvent e = new ChangeEvent(this);
      for (int i = 0; i < changeListenerList.size(); i++) {
          changeListenerList.get(i).stateChanged(e);
       }
   }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton acceptSelectionButton;
    private javax.swing.JList allowedList;
    private javax.swing.JButton backgroundThresholdButton;
    private javax.swing.JButton clearPointButton;
    private javax.swing.JButton clearPointsButton;
    private javax.swing.JTable componentTable;
    private javax.swing.JButton computeRangeSegmentationButton;
    private javax.swing.JButton computeSimilarityButton;
    private javax.swing.JButton cropButton;
    private pl.edu.icm.visnow.lib.gui.DataComponentSelector dataComponentSelector;
    private pl.edu.icm.visnow.gui.widgets.MultistateButton denoiseModeButton;
    private javax.swing.JCheckBox distSlicesBox;
    private pl.edu.icm.visnow.gui.widgets.ExtendedSlider extentSlider;
    private pl.edu.icm.visnow.gui.widgets.ExtendedSlider extentSlider1;
    private javax.swing.JScrollPane helpPane;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.ButtonGroup modeGroup;
    private javax.swing.JButton outputResultsButton;
    private javax.swing.JButton outputVolumesButton;
    private pl.edu.icm.visnow.gui.widgets.FloatSubRangeSlider.ExtendedFloatSubRangeSlider rangeSlider;
    private javax.swing.JButton removeFromButton;
    private javax.swing.JButton resetButton;
    private javax.swing.JList segmentedSetList;
    private javax.swing.JTextField segmentedSetNameField;
    private javax.swing.ButtonGroup selectFieldGroup;
    private javax.swing.JTable selectionTable;
    private javax.swing.JSlider thresholdSlider;
    private javax.swing.JComboBox viewer3DContentComboBox;
    private javax.swing.JFileChooser volumeFileChooser;
    // End of variables declaration//GEN-END:variables
}
