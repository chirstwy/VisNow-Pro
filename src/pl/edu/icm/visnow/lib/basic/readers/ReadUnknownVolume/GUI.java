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
exception statement from your version.
*/
//</editor-fold>

package pl.edu.icm.visnow.lib.basic.readers.ReadUnknownVolume;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ComponentColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import pl.edu.icm.visnow.datasets.dataarrays.DataArray;
import pl.edu.icm.visnow.geometries.gui.RegularField3DMapPanel;
import pl.edu.icm.visnow.geometries.gui.RegularFieldPresentationGUI;
import pl.edu.icm.visnow.system.main.VisNow;

/**
 *
 * @author Krzysztof S. Nowinski, University of Warsaw, ICM
 */
public class GUI extends javax.swing.JPanel
{

   private Hashtable<Integer, JLabel> labelsTable = new Hashtable<Integer, JLabel>();
   private JFileChooser dataFileChooser = new JFileChooser();
   private String fileName = null;
   private float[] scale = new float[3];
   private int size;
   private float minV, maxV;
   private int nx = 256, ny = 256, nz = 0, skip = 0, tailLen = 0, sliceSkip = 0;
   private BufferedImage im0 = null;
   private BufferedImage im1 = null;
   private BufferedImage im2 = null;
   private int dataType = DataArray.FIELD_DATA_BYTE;
   private byte[] bData = null;
   private short[] sData = null;
   private int[] iData = null;
   private float[] fData = null;
   private String lastPath = null;

   public int getDataType()
   {
      return dataType;
   }

   public byte[] getBData()
   {
      return bData;
   }

   public int[] getIData()
   {
      return iData;
   }

   public short[] getSData()
   {
      return sData;
   }

   public int getNz()
   {
      return nz;
   }

   public int getSkip()
   {
      return skip;
   }

   public int getSliceSkip()
   {
      return sliceSkip;
   }

   public String getFileName()
   {
      return fileName;
   }

   public float[] getScale()
   {
      return scale;
   }

   public int getNx()
   {
      return nx;
   }

   public int getNy()
   {
      return ny;
   }

   private JPanel map0 = new JPanel()
   {

      public void paint(Graphics g)
      {
         Graphics2D gr = (Graphics2D) g;
         gr.setColor(Color.DARK_GRAY);
         gr.fillRect(0, 0, getWidth(), getHeight());
         if (im0 != null)
         {
            gr.drawImage(im0, 0, 0, getWidth(), getHeight(), null);
         }

      }
   };
   private JPanel map1 = new JPanel()
   {

      public void paint(Graphics g)
      {
         Graphics2D gr = (Graphics2D) g;
         gr.setColor(Color.DARK_GRAY);
         gr.fillRect(0, 0, getWidth(), getHeight());
         if (im1 != null)
         {
            gr.drawImage(im1, 0, 0, getWidth(), getHeight(), null);
         }

      }
   };
   private JPanel map2 = new JPanel()
   {

      public void paint(Graphics g)
      {
         Graphics2D gr = (Graphics2D) g;
         gr.setColor(Color.DARK_GRAY);
         gr.fillRect(0, 0, getWidth(), getHeight());
         if (im2 != null)
         {
            gr.drawImage(im2, 0, 0, getWidth(), getHeight(), null);
         }

      }
   };

   /**
    * Creates new form VolumeReaderUI
    */
   public GUI()
   {
      initComponents();
      for (int i = 0; i <= 500; i += 100)
      {
         labelsTable.put(i, new JLabel("" + i / 100));
      }
      xSlider.setLabelTable(labelsTable);
      ySlider.setLabelTable(labelsTable);
      zSlider.setLabelTable(labelsTable);
      leftSlicePanel.add(map0, BorderLayout.CENTER);
      centerSlicePanel.add(map1, BorderLayout.CENTER);
      rightSlicePanel.add(map2, BorderLayout.CENTER);
      dataGroup.add(byteButton);
      dataGroup.add(shortButton);
      dataGroup.add(intButton);
   }

   private void refresh()
   {
      sliceSkip = sliceSkipSlider.getValue();
      ny = Integer.parseInt(nyField.getText());
      nx = Integer.parseInt(nxField.getText());
      nz = ((int) size - tailLen) / (nx * ny + sliceSkip);
      skip = nx * skipLinesSlider.getValue() + skipPixelsSlider.getValue();
      nzField.setText("" + nz);
      im0 = makeXYImage(skip);
      map0.repaint();
      im1 = makeXZImage(skip);
      map1.repaint();
      im2 = makeYZImage(skip);
      map2.repaint();
   }

   BufferedImage makeXYImage(int st)
   {
      int[] rgba = new int[4];
      ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
      int[] nBits =
      {
         8, 8, 8, 8
      };
      ComponentColorModel colorModel = new ComponentColorModel(cs, nBits,
              true, true, Transparency.TRANSLUCENT, 0);
      WritableRaster raster =
              colorModel.createCompatibleWritableRaster(nx, ny);
      BufferedImage bImage =
              new BufferedImage(colorModel, raster, false, null);
      int sz = nz / 2;
      switch (dataType)
      {
      case DataArray.FIELD_DATA_BYTE:
         for (int i1 = 0, j = st + sz * nx * ny; i1 < ny; i1++)
         {
            for (int i0 = 0; i0 < nx; i0++, j++)
            {
               int c = bData[j];
               for (int k = 0; k < 3; k++)
                  rgba[k] = c;
               rgba[3] = 0xFF;
               raster.setPixel(i0, i1, rgba);
            }
         }
         break;
      case DataArray.FIELD_DATA_SHORT:
         for (int i1 = 0, j = st + sz * nx * ny; i1 < ny; i1++)
         {
            for (int i0 = 0; i0 < nx; i0++, j++)
            {
               int c = (int)(255 * (sData[j] - minV)/(maxV - minV));
               for (int k = 0; k < 3; k++)
                  rgba[k] = c;
               rgba[3] = 0xFF;
               raster.setPixel(i0, i1, rgba);
            }
         }
         break;
      case DataArray.FIELD_DATA_INT:
         for (int i1 = 0, j = st + sz * nx * ny; i1 < ny; i1++)
         {
            for (int i0 = 0; i0 < nx; i0++, j++)
            {
               int c = (int)(255 * (iData[j] - minV)/(maxV - minV));
               for (int k = 0; k < 3; k++)
                  rgba[k] = c;
               rgba[3] = 0xFF;
               raster.setPixel(i0, i1, rgba);
            }
         }
         break;
      case DataArray.FIELD_DATA_FLOAT:
         for (int i1 = 0, j = st + sz * nx * ny; i1 < ny; i1++)
         {
            for (int i0 = 0; i0 < nx; i0++, j++)
            {
               int c = (int)(255.0f * (fData[j] - minV)/(maxV - minV));
               for (int k = 0; k < 3; k++)
                  rgba[k] = c;
               rgba[3] = 0xFF;
               raster.setPixel(i0, i1, rgba);
            }
         }
         break;
      }
      return bImage;
   }

   BufferedImage makeXZImage(int st)
   {
      int[] rgba = new int[4];
      ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
      int[] nBits =
      {
         8, 8, 8, 8
      };
      ComponentColorModel colorModel = new ComponentColorModel(cs, nBits,
              true, true, Transparency.TRANSLUCENT, 0);
      WritableRaster raster =
              colorModel.createCompatibleWritableRaster(nx, nz);
      BufferedImage bImage =
              new BufferedImage(colorModel, raster, false, null);
      int sy = ny / 2;
      switch (dataType)
      {
      case DataArray.FIELD_DATA_BYTE:
      for (int i1 = 0; i1 < nz; i1++)
      {
         for (int i0 = 0, j = st + sy * nx + i1 * (nx * ny + sliceSkip); i0 < nx; i0++, j++)
            {
               int c = bData[j];
               for (int k = 0; k < 3; k++)
                  rgba[k] = c;
               rgba[3] = 0xFF;
               raster.setPixel(i0, i1, rgba);
            }
         }
         break;
      case DataArray.FIELD_DATA_SHORT:
      for (int i1 = 0; i1 < nz; i1++)
      {
         for (int i0 = 0, j = st + sy * nx + i1 * (nx * ny + sliceSkip); i0 < nx; i0++, j++)
            {
               int c = (int)(255 * (sData[j] - minV)/(maxV - minV));
               for (int k = 0; k < 3; k++)
                  rgba[k] = c;
               rgba[3] = 0xFF;
               raster.setPixel(i0, i1, rgba);
            }
         }
         break;
      case DataArray.FIELD_DATA_INT:
      for (int i1 = 0; i1 < nz; i1++)
      {
         for (int i0 = 0, j = st + sy * nx + i1 * (nx * ny + sliceSkip); i0 < nx; i0++, j++)
            {
               int c = (int)(255 * (iData[j] - minV)/(maxV - minV));
               for (int k = 0; k < 3; k++)
                  rgba[k] = c;
               rgba[3] = 0xFF;
               raster.setPixel(i0, i1, rgba);
            }
         }
         break;
      case DataArray.FIELD_DATA_FLOAT:
      for (int i1 = 0; i1 < nz; i1++)
      {
         for (int i0 = 0, j = st + sy * nx + i1 * (nx * ny + sliceSkip); i0 < nx; i0++, j++)
            {
               int c = (int)(255.0f * (fData[j] - minV)/(maxV - minV));
               for (int k = 0; k < 3; k++)
                  rgba[k] = c;
               rgba[3] = 0xFF;
               raster.setPixel(i0, i1, rgba);
            }
         }
         break;
      }
      return bImage;
   }

   BufferedImage makeYZImage(int st)
   {
      int[] rgba = new int[4];
      ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
      int[] nBits =
      {
         8, 8, 8, 8
      };
      ComponentColorModel colorModel = new ComponentColorModel(cs, nBits,
              true, true, Transparency.TRANSLUCENT, 0);
      WritableRaster raster =
              colorModel.createCompatibleWritableRaster(ny, nz);
      BufferedImage bImage =
              new BufferedImage(colorModel, raster, false, null);
      switch (dataType)
      {
      case DataArray.FIELD_DATA_BYTE:
      for (int i1 = 0; i1 < nz; i1++)
      {
         for (int i0 = 0, j = st + nx / 2 + i1 * (nx * ny + sliceSkip); i0 < ny; i0++, j += nx)
            {
               int c = bData[j];
               for (int k = 0; k < 3; k++)
                  rgba[k] = c;
               rgba[3] = 0xFF;
               raster.setPixel(i0, i1, rgba);
            }
         }
         break;
      case DataArray.FIELD_DATA_SHORT:
      for (int i1 = 0; i1 < nz; i1++)
      {
         for (int i0 = 0, j = st + nx / 2 + i1 * (nx * ny + sliceSkip); i0 < ny; i0++, j += nx)
            {
               int c = (int)(255 * (sData[j] - minV)/(maxV - minV));
               for (int k = 0; k < 3; k++)
                  rgba[k] = c;
               rgba[3] = 0xFF;
               raster.setPixel(i0, i1, rgba);
            }
         }
         break;
      case DataArray.FIELD_DATA_INT:
      for (int i1 = 0; i1 < nz; i1++)
      {
         for (int i0 = 0, j = st + nx / 2 + i1 * (nx * ny + sliceSkip); i0 < ny; i0++, j += nx)
            {
               int c = (int)(255 * (iData[j] - minV)/(maxV - minV));
               for (int k = 0; k < 3; k++)
                  rgba[k] = c;
               rgba[3] = 0xFF;
               raster.setPixel(i0, i1, rgba);
            }
         }
         break;
      case DataArray.FIELD_DATA_FLOAT:
      for (int i1 = 0; i1 < nz; i1++)
      {
         for (int i0 = 0, j = st + nx / 2 + i1 * (nx * ny + sliceSkip); i0 < ny; i0++, j += nx)
            {
               int c = (int)(255.0f * (fData[j] - minV)/(maxV - minV));
               for (int k = 0; k < 3; k++)
                  rgba[k] = c;
               rgba[3] = 0xFF;
               raster.setPixel(i0, i1, rgba);
            }
         }
         break;
      }
      return bImage;
   }
   
   private void readData()
   {  
      size = (int) dataFileChooser.getSelectedFile().length() - startByteSlider.getValue() - startByteSlider.getValue();
      try
      {
         ImageInputStream in = new FileImageInputStream(dataFileChooser.getSelectedFile());
         in.skipBytes(startByteSlider.getValue());
         in.setByteOrder(bigEndianButton.isSelected() ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
         if (byteButton.isSelected())
         {
            dataType = DataArray.FIELD_DATA_BYTE;
            bData = new byte[(int) size];
            in.readFully(bData);
         } else if (shortButton.isSelected())
         {
            dataType = DataArray.FIELD_DATA_SHORT;
            size /= 2;
            sData = new short[size];
            in.readFully(sData, 0, size);
            for (int i = 0; i < size; i++)
            {
               if (sData[i] < minV)
                  minV = sData[i];
               if (sData[i] > maxV)
                  maxV = sData[i];
            }
            System.out.println(minV + " " + maxV);
         } else if (intButton.isSelected())
         {
            dataType = DataArray.FIELD_DATA_INT;
            size /= 4;
            iData = new int[size];
            in.readFully(iData, 0, size);
            for (int i = 0; i < size; i++)
            {
               if (iData[i] < minV)
                  minV = iData[i];
               if (iData[i] > maxV)
                  maxV = iData[i];
            }
            System.out.println(minV + " " + maxV);
         } else if (floatButton.isSelected())
         {
            dataType = DataArray.FIELD_DATA_FLOAT;
            size /= 4;
            fData = new float[size];
            in.readFully(fData, 0, size);
            for (int i = 0; i < size; i++)
            {
               if (fData[i] < minV)
                  minV = fData[i];
               if (fData[i] > maxV)
                  maxV = fData[i];
            }
            System.out.println(minV + " " + maxV);
         }
         datamapRangeSlider.setMinMax(minV, maxV);
         refresh();
         in.close();
      } catch (Exception ex)
      {
         ex.printStackTrace();
      }
   }

   /**
    * This method is called from within the constructor to initialize the form.
    * WARNING: Do NOT modify this code. The content of this method is always
    * regenerated by the Form Editor.
    */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        dataGroup = new javax.swing.ButtonGroup();
        buttonGroup1 = new javax.swing.ButtonGroup();
        leftSlicePanel = new javax.swing.JPanel();
        centerSlicePanel = new javax.swing.JPanel();
        rightSlicePanel = new javax.swing.JPanel();
        controlPanel = new javax.swing.JPanel();
        readButton = new javax.swing.JButton();
        xSlider1 = new javax.swing.JSlider();
        nxField = new javax.swing.JTextField();
        ySlider1 = new javax.swing.JSlider();
        nyField = new javax.swing.JTextField();
        nzField = new javax.swing.JTextField();
        sliceSkipSlider = new javax.swing.JSlider();
        skipField = new javax.swing.JTextField();
        fileNameField = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        xSlider = new javax.swing.JSlider();
        ySlider = new javax.swing.JSlider();
        zSlider = new javax.swing.JSlider();
        skipLinesSlider = new javax.swing.JSlider();
        skipPixelsSlider = new javax.swing.JSlider();
        jPanel1 = new javax.swing.JPanel();
        selectButton = new javax.swing.JButton();
        byteButton = new javax.swing.JRadioButton();
        shortButton = new javax.swing.JRadioButton();
        intButton = new javax.swing.JRadioButton();
        bigEndianButton = new javax.swing.JRadioButton();
        littleEndianButton = new javax.swing.JRadioButton();
        rereadButton = new javax.swing.JButton();
        floatButton = new javax.swing.JRadioButton();
        startByteSlider = new javax.swing.JSlider();
        jLabel1 = new javax.swing.JLabel();
        datamapRangeSlider = new pl.edu.icm.visnow.gui.widgets.FloatSubRangeSlider.ExtendedFloatSubRangeSlider();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        regularFieldPresentationGUI = new pl.edu.icm.visnow.geometries.gui.RegularFieldPresentationGUI();
        jScrollPane1 = new javax.swing.JScrollPane();
        fieldDescription = new javax.swing.JLabel();

        setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        setMinimumSize(new java.awt.Dimension(700, 300));
        setPreferredSize(new java.awt.Dimension(800, 400));
        setLayout(new java.awt.GridBagLayout());

        leftSlicePanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        leftSlicePanel.setMinimumSize(new java.awt.Dimension(200, 300));
        leftSlicePanel.setPreferredSize(new java.awt.Dimension(300, 500));
        leftSlicePanel.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(leftSlicePanel, gridBagConstraints);

        centerSlicePanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        centerSlicePanel.setMinimumSize(new java.awt.Dimension(200, 300));
        centerSlicePanel.setPreferredSize(new java.awt.Dimension(300, 500));
        centerSlicePanel.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(centerSlicePanel, gridBagConstraints);

        rightSlicePanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        rightSlicePanel.setMinimumSize(new java.awt.Dimension(200, 300));
        rightSlicePanel.setPreferredSize(new java.awt.Dimension(300, 500));
        rightSlicePanel.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(rightSlicePanel, gridBagConstraints);

        controlPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        controlPanel.setLayout(new java.awt.GridBagLayout());

        readButton.setText("read file");
        readButton.setPreferredSize(new java.awt.Dimension(60, 25));
        readButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                readButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        controlPanel.add(readButton, gridBagConstraints);

        xSlider1.setFont(new java.awt.Font("Dialog", 0, 8)); // NOI18N
        xSlider1.setMajorTickSpacing(100);
        xSlider1.setMaximum(1000);
        xSlider1.setMinorTickSpacing(5);
        xSlider1.setPaintLabels(true);
        xSlider1.setPaintTicks(true);
        xSlider1.setValue(256);
        xSlider1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "x resolution", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 10))); // NOI18N
        xSlider1.setMinimumSize(new java.awt.Dimension(36, 50));
        xSlider1.setPreferredSize(new java.awt.Dimension(200, 50));
        xSlider1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                xSlider1StateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 2.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 3);
        controlPanel.add(xSlider1, gridBagConstraints);

        nxField.setText("256");
        nxField.setMinimumSize(new java.awt.Dimension(60, 19));
        nxField.setPreferredSize(new java.awt.Dimension(60, 19));
        nxField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nxFieldActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        controlPanel.add(nxField, gridBagConstraints);

        ySlider1.setFont(new java.awt.Font("Dialog", 0, 8)); // NOI18N
        ySlider1.setMajorTickSpacing(100);
        ySlider1.setMaximum(1000);
        ySlider1.setMinorTickSpacing(5);
        ySlider1.setPaintLabels(true);
        ySlider1.setPaintTicks(true);
        ySlider1.setValue(256);
        ySlider1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "y resolution", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 10))); // NOI18N
        ySlider1.setMinimumSize(new java.awt.Dimension(36, 50));
        ySlider1.setPreferredSize(new java.awt.Dimension(200, 50));
        ySlider1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                ySlider1StateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 2.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 3);
        controlPanel.add(ySlider1, gridBagConstraints);

        nyField.setText("256");
        nyField.setPreferredSize(new java.awt.Dimension(60, 19));
        nyField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nyFieldActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        controlPanel.add(nyField, gridBagConstraints);

        nzField.setText("200");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        controlPanel.add(nzField, gridBagConstraints);

        sliceSkipSlider.setFont(new java.awt.Font("Dialog", 0, 8)); // NOI18N
        sliceSkipSlider.setMajorTickSpacing(10);
        sliceSkipSlider.setMaximum(32);
        sliceSkipSlider.setMinorTickSpacing(1);
        sliceSkipSlider.setPaintLabels(true);
        sliceSkipSlider.setPaintTicks(true);
        sliceSkipSlider.setValue(0);
        sliceSkipSlider.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "skip inter slices", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 10))); // NOI18N
        sliceSkipSlider.setMinimumSize(new java.awt.Dimension(36, 50));
        sliceSkipSlider.setOpaque(false);
        sliceSkipSlider.setPreferredSize(new java.awt.Dimension(60, 50));
        sliceSkipSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sliceSkipSliderStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        controlPanel.add(sliceSkipSlider, gridBagConstraints);

        skipField.setText("0");
        skipField.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "skip", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 10))); // NOI18N
        skipField.setMinimumSize(new java.awt.Dimension(70, 35));
        skipField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                skipFieldActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        controlPanel.add(skipField, gridBagConstraints);

        fileNameField.setMinimumSize(new java.awt.Dimension(4, 24));
        fileNameField.setPreferredSize(new java.awt.Dimension(4, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        controlPanel.add(fileNameField, gridBagConstraints);

        jPanel2.setMinimumSize(new java.awt.Dimension(100, 160));
        jPanel2.setPreferredSize(new java.awt.Dimension(120, 165));
        jPanel2.setLayout(new java.awt.GridLayout(3, 0));

        xSlider.setFont(new java.awt.Font("Dialog", 0, 8)); // NOI18N
        xSlider.setMajorTickSpacing(100);
        xSlider.setMaximum(500);
        xSlider.setMinorTickSpacing(5);
        xSlider.setPaintLabels(true);
        xSlider.setPaintTicks(true);
        xSlider.setValue(100);
        xSlider.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(null, "x scale (%)", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 10)), "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 10))); // NOI18N
        xSlider.setMinimumSize(new java.awt.Dimension(100, 50));
        xSlider.setPreferredSize(new java.awt.Dimension(200, 55));
        jPanel2.add(xSlider);

        ySlider.setFont(new java.awt.Font("Dialog", 0, 8)); // NOI18N
        ySlider.setMajorTickSpacing(100);
        ySlider.setMaximum(500);
        ySlider.setMinorTickSpacing(5);
        ySlider.setPaintLabels(true);
        ySlider.setPaintTicks(true);
        ySlider.setValue(100);
        ySlider.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "y scale (%)", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 10))); // NOI18N
        ySlider.setMinimumSize(new java.awt.Dimension(36, 50));
        ySlider.setPreferredSize(new java.awt.Dimension(200, 55));
        jPanel2.add(ySlider);

        zSlider.setFont(new java.awt.Font("Dialog", 0, 8)); // NOI18N
        zSlider.setMajorTickSpacing(100);
        zSlider.setMaximum(500);
        zSlider.setMinorTickSpacing(5);
        zSlider.setPaintLabels(true);
        zSlider.setPaintTicks(true);
        zSlider.setValue(100);
        zSlider.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "z scale (%)", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 10))); // NOI18N
        zSlider.setMinimumSize(new java.awt.Dimension(36, 50));
        zSlider.setPreferredSize(new java.awt.Dimension(200, 55));
        jPanel2.add(zSlider);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        controlPanel.add(jPanel2, gridBagConstraints);

        skipLinesSlider.setFont(new java.awt.Font("Dialog", 0, 8)); // NOI18N
        skipLinesSlider.setMajorTickSpacing(100);
        skipLinesSlider.setMaximum(500);
        skipLinesSlider.setMinorTickSpacing(5);
        skipLinesSlider.setPaintLabels(true);
        skipLinesSlider.setPaintTicks(true);
        skipLinesSlider.setValue(0);
        skipLinesSlider.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "skip rows", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 10))); // NOI18N
        skipLinesSlider.setMinimumSize(new java.awt.Dimension(36, 50));
        skipLinesSlider.setOpaque(false);
        skipLinesSlider.setPreferredSize(new java.awt.Dimension(200, 50));
        skipLinesSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                skipLinesSliderStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 3);
        controlPanel.add(skipLinesSlider, gridBagConstraints);

        skipPixelsSlider.setFont(new java.awt.Font("Dialog", 0, 8)); // NOI18N
        skipPixelsSlider.setMajorTickSpacing(100);
        skipPixelsSlider.setMaximum(500);
        skipPixelsSlider.setMinorTickSpacing(5);
        skipPixelsSlider.setPaintLabels(true);
        skipPixelsSlider.setPaintTicks(true);
        skipPixelsSlider.setValue(0);
        skipPixelsSlider.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "skip pixels", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 10))); // NOI18N
        skipPixelsSlider.setMinimumSize(new java.awt.Dimension(36, 50));
        skipPixelsSlider.setOpaque(false);
        skipPixelsSlider.setPreferredSize(new java.awt.Dimension(200, 50));
        skipPixelsSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                skipPixelsSliderStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 3);
        controlPanel.add(skipPixelsSlider, gridBagConstraints);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        selectButton.setText("select file");
        selectButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        selectButton.setPreferredSize(new java.awt.Dimension(60, 25));
        selectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(selectButton, gridBagConstraints);

        dataGroup.add(byteButton);
        byteButton.setSelected(true);
        byteButton.setText("byte");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(byteButton, gridBagConstraints);

        dataGroup.add(shortButton);
        shortButton.setText("short");
        shortButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                shortButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(shortButton, gridBagConstraints);

        dataGroup.add(intButton);
        intButton.setText("int");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(intButton, gridBagConstraints);

        buttonGroup1.add(bigEndianButton);
        bigEndianButton.setSelected(true);
        bigEndianButton.setText("bigEndian");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel1.add(bigEndianButton, gridBagConstraints);

        buttonGroup1.add(littleEndianButton);
        littleEndianButton.setText("little endian");
        littleEndianButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                littleEndianButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(littleEndianButton, gridBagConstraints);

        rereadButton.setText("reread");
        rereadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rereadButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(rereadButton, gridBagConstraints);

        dataGroup.add(floatButton);
        floatButton.setText("float");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(floatButton, gridBagConstraints);

        startByteSlider.setMaximum(3);
        startByteSlider.setMinorTickSpacing(1);
        startByteSlider.setPaintLabels(true);
        startByteSlider.setPaintTicks(true);
        startByteSlider.setValue(0);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(startByteSlider, gridBagConstraints);

        jLabel1.setText("skip bytes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        jPanel1.add(jLabel1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        controlPanel.add(jPanel1, gridBagConstraints);

        datamapRangeSlider.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "datamap range", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 10))); // NOI18N
        datamapRangeSlider.setMinimumSize(new java.awt.Dimension(96, 65));
        datamapRangeSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                datamapRangeSliderStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        controlPanel.add(datamapRangeSlider, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.weightx = 1.0;
        add(controlPanel, gridBagConstraints);

        jTabbedPane1.setMinimumSize(new java.awt.Dimension(185, 807));
        jTabbedPane1.setPreferredSize(new java.awt.Dimension(205, 812));
        jTabbedPane1.addTab("display", regularFieldPresentationGUI);

        fieldDescription.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        fieldDescription.setText(null);
        fieldDescription.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jScrollPane1.setViewportView(fieldDescription);

        jTabbedPane1.addTab("description", jScrollPane1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        add(jTabbedPane1, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

   private void skipPixelsSliderStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_skipPixelsSliderStateChanged
   {//GEN-HEADEREND:event_skipPixelsSliderStateChanged
      skipField.setText("" + (nx * skipLinesSlider.getValue() + skipPixelsSlider.getValue()));
      refresh(); 
}//GEN-LAST:event_skipPixelsSliderStateChanged

private void skipLinesSliderStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_skipLinesSliderStateChanged
{//GEN-HEADEREND:event_skipLinesSliderStateChanged
   skipField.setText("" + (nx * skipLinesSlider.getValue() + skipPixelsSlider.getValue()));
   refresh();
}//GEN-LAST:event_skipLinesSliderStateChanged

private void skipFieldActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_skipFieldActionPerformed
   {
      refresh();
}//GEN-LAST:event_skipFieldActionPerformed

   private void nyFieldActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_nyFieldActionPerformed
   {//GEN-HEADEREND:event_nyFieldActionPerformed
      refresh();
   }//GEN-LAST:event_nyFieldActionPerformed

   private void nxFieldActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_nxFieldActionPerformed
   {//GEN-HEADEREND:event_nxFieldActionPerformed
      refresh();
   }//GEN-LAST:event_nxFieldActionPerformed

   private void sliceSkipSliderStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_sliceSkipSliderStateChanged
   {//GEN-HEADEREND:event_sliceSkipSliderStateChanged
      refresh();
}//GEN-LAST:event_sliceSkipSliderStateChanged

   private void ySlider1StateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_ySlider1StateChanged
   {//GEN-HEADEREND:event_ySlider1StateChanged
      if (ySlider1.getValue() < 10)
      {
         return;
      }
      nyField.setText("" + ySlider1.getValue());
      skipLinesSlider.setMaximum(ySlider1.getValue());
      refresh();
   }//GEN-LAST:event_ySlider1StateChanged

   private void xSlider1StateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_xSlider1StateChanged
   {//GEN-HEADEREND:event_xSlider1StateChanged
      if (xSlider1.getValue() < 10)
      {
         return;
      }
      nxField.setText("" + xSlider1.getValue());
      skipPixelsSlider.setMaximum(xSlider1.getValue());
      refresh();
   }//GEN-LAST:event_xSlider1StateChanged

   private void readButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_readButtonActionPerformed
   {//GEN-HEADEREND:event_readButtonActionPerformed
      scale[0] = xSlider.getValue() / 100.f;
      scale[1] = ySlider.getValue() / 100.f;
      scale[2] = zSlider.getValue() / 100.f;
      fireStateChanged();
   }//GEN-LAST:event_readButtonActionPerformed

   private void selectButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_selectButtonActionPerformed
   {//GEN-HEADEREND:event_selectButtonActionPerformed
      if (lastPath == null)
      {
         dataFileChooser.setCurrentDirectory(new File(VisNow.get().getMainConfig().getUsableDataPath(ReadUnknownVolume.class)));
      } else
      {
         dataFileChooser.setCurrentDirectory(new File(lastPath));
      }

      int returnVal = dataFileChooser.showOpenDialog(this);
      if (returnVal == JFileChooser.APPROVE_OPTION)
      {
         fileName = dataFileChooser.getSelectedFile().getAbsolutePath();
         lastPath = fileName.substring(0, fileName.lastIndexOf(File.separator));
         VisNow.get().getMainConfig().setLastDataPath(lastPath,ReadUnknownVolume.class);
      }
      fileNameField.setText(fileName);
      readData();

   }//GEN-LAST:event_selectButtonActionPerformed

   private void shortButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_shortButtonActionPerformed
   {//GEN-HEADEREND:event_shortButtonActionPerformed
      // TODO add your handling code here:
}//GEN-LAST:event_shortButtonActionPerformed

   private void littleEndianButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_littleEndianButtonActionPerformed
   {//GEN-HEADEREND:event_littleEndianButtonActionPerformed
      // TODO add your handling code here:
   }//GEN-LAST:event_littleEndianButtonActionPerformed

   private void datamapRangeSliderStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_datamapRangeSliderStateChanged
   {//GEN-HEADEREND:event_datamapRangeSliderStateChanged
      minV = datamapRangeSlider.getLow();
      maxV = datamapRangeSlider.getUp();
      refresh();
   }//GEN-LAST:event_datamapRangeSliderStateChanged

   private void rereadButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_rereadButtonActionPerformed
   {//GEN-HEADEREND:event_rereadButtonActionPerformed
      if (fileName == null)
         selectButtonActionPerformed(evt);
      else
      {
         readData();
      }
   }//GEN-LAST:event_rereadButtonActionPerformed
   /**
    * Utility field holding list of ChangeListeners.
    */
   private transient ArrayList<ChangeListener> changeListenerList =
           new ArrayList<ChangeListener>();

   /**
    * Registers ChangeListener to receive events.
    *
    * @param listener The listener to register.
    */
   public synchronized void addChangeListener(ChangeListener listener)
   {
      changeListenerList.add(listener);
   }

   /**
    * Removes ChangeListener from the list of listeners.
    *
    * @param listener The listener to remove.
    */
   public synchronized void removeChangeListener(ChangeListener listener)
   {
      changeListenerList.remove(listener);
   }

   public void setFieldDescription(String s)
   {
      fieldDescription.setText(s);
   }

   /**
    * Notifies all registered listeners about the event.
    *
    * @param object Parameter #1 of the
    * <CODE>ChangeEvent<CODE> constructor.
    */
   private void fireStateChanged()
   {
      ChangeEvent e = new ChangeEvent(this);
      for (ChangeListener listener : changeListenerList)
      {
         listener.stateChanged(e);
      }
   }

   public RegularFieldPresentationGUI getFieldPresentationGUI()
   {
      return regularFieldPresentationGUI;
   }

   public RegularField3DMapPanel getRegularField3DMapPanel()
   {
      return regularFieldPresentationGUI.getRegularField3DMapPanel();
   }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton bigEndianButton;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JRadioButton byteButton;
    private javax.swing.JPanel centerSlicePanel;
    private javax.swing.JPanel controlPanel;
    private javax.swing.ButtonGroup dataGroup;
    private pl.edu.icm.visnow.gui.widgets.FloatSubRangeSlider.ExtendedFloatSubRangeSlider datamapRangeSlider;
    private javax.swing.JLabel fieldDescription;
    private javax.swing.JTextField fileNameField;
    private javax.swing.JRadioButton floatButton;
    private javax.swing.JRadioButton intButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JPanel leftSlicePanel;
    private javax.swing.JRadioButton littleEndianButton;
    private javax.swing.JTextField nxField;
    private javax.swing.JTextField nyField;
    private javax.swing.JTextField nzField;
    private javax.swing.JButton readButton;
    private pl.edu.icm.visnow.geometries.gui.RegularFieldPresentationGUI regularFieldPresentationGUI;
    private javax.swing.JButton rereadButton;
    private javax.swing.JPanel rightSlicePanel;
    private javax.swing.JButton selectButton;
    private javax.swing.JRadioButton shortButton;
    private javax.swing.JTextField skipField;
    private javax.swing.JSlider skipLinesSlider;
    private javax.swing.JSlider skipPixelsSlider;
    private javax.swing.JSlider sliceSkipSlider;
    private javax.swing.JSlider startByteSlider;
    private javax.swing.JSlider xSlider;
    private javax.swing.JSlider xSlider1;
    private javax.swing.JSlider ySlider;
    private javax.swing.JSlider ySlider1;
    private javax.swing.JSlider zSlider;
    // End of variables declaration//GEN-END:variables

    void activateOpenDialog() {
        selectButtonActionPerformed(null);
    }
}
