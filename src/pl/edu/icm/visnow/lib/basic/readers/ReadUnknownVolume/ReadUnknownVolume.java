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

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import pl.edu.icm.visnow.datasets.RegularField;
import pl.edu.icm.visnow.datasets.dataarrays.DataArray;
import pl.edu.icm.visnow.engine.core.OutputEgg;
import pl.edu.icm.visnow.geometries.parameters.RegularField3dParams;
import pl.edu.icm.visnow.lib.templates.visualization.modules.RegularOutFieldVisualizationModule;
import pl.edu.icm.visnow.lib.types.VNGeometryObject;
import pl.edu.icm.visnow.lib.types.VNRegularField;
import pl.edu.icm.visnow.lib.utils.SwingInstancer;
import pl.edu.icm.visnow.system.main.VisNow;

/**
 * @author  Krzysztof S. Nowinski (know@icm.edu.pl)
 * Warsaw University, Interdisciplinary Centre
 * for Mathematical and Computational Modelling
 */
public class ReadUnknownVolume extends RegularOutFieldVisualizationModule
{

   /**
    * Creates a new instance of CreateGrid
    */
   protected GUI computeUI = null;
   protected JFrame uiFrame = null;
   protected RegularField3dParams regularField3DmapParams = new RegularField3dParams();
   protected boolean fromGUI = false;
//   protected RegularFieldGeometry regularFieldGeometry = null;
//   protected OpenBranchGroup outGroup = null;

   public ReadUnknownVolume()
   {
      regularField3DmapParams.addChangeListener(new ChangeListener()
      {
         @Override
         public void stateChanged(ChangeEvent evt)
         {
            show();
         }
      });
      SwingInstancer.swingRunAndWait(new Runnable()
      {
         @Override
         public void run()
         {
            computeUI = new GUI();
            uiFrame = new JFrame(); uiFrame.setIconImage(new ImageIcon(getClass().getResource( VisNow.getIconPath() )).getImage());
            uiFrame.setTitle("read unknown volume");
            uiFrame.getContentPane().setLayout(new java.awt.BorderLayout());
            uiFrame.getContentPane().add(computeUI);
            uiFrame.pack();
            uiFrame.setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
            uiFrame.setVisible(true);
         }
      });
      computeUI.addChangeListener(new ChangeListener()
      {
         @Override
         public void stateChanged(ChangeEvent evt)
         {
            startAction();
         }
      });
   }

   @Override
   public void onDelete()
   {
      uiFrame.dispose();
      detach();
   }

   @Override
   public boolean isGenerator() {
      return true;
   }

   public static OutputEgg[] outputEggs = null;   

   public void update()
   {
      int[] dims = new int[]{computeUI.getNx(),computeUI.getNy(),computeUI.getNz()};
      outField = new RegularField(dims);    
      float[][] pts = new float[2][3];
      switch (computeUI.getDataType())
      {
      case DataArray.FIELD_DATA_BYTE:
         byte[] inBData = computeUI.getBData();
         byte[] bData = new byte[dims[0] * dims[1] * dims[2]];
         for (int i = 0, j = computeUI.getSkip(), l = 0; i < dims[2]; i++)
         {
            for (int k = 0; k < dims[1]; k++)
               for (int m = 0; m < dims[0]; m++, l++, j++)
                  bData[l] = inBData[j];
            j += computeUI.getSliceSkip();
         }
         outField.addData(DataArray.create(bData, 1, "volume"));
         break;
      case DataArray.FIELD_DATA_SHORT:
         short[] inSData = computeUI.getSData();
         short[] sData = new short[dims[0] * dims[1] * dims[2]];
         for (int i = 0, j = computeUI.getSkip(), l = 0; i < dims[2]; i++)
         {
            for (int k = 0; k < dims[1]; k++)
               for (int m = 0; m < dims[0]; m++, l++, j++)
                  sData[l] = inSData[j];
            j += computeUI.getSliceSkip();
         }
         outField.addData(DataArray.create(sData, 1, "volume"));
         break;
      case DataArray.FIELD_DATA_INT:
         int[] inIData = computeUI.getIData();
         int[] iData = new int[dims[0] * dims[1] * dims[2]];
         for (int i = 0, j = computeUI.getSkip(), l = 0; i < dims[2]; i++)
         {
            for (int k = 0; k < dims[1]; k++)
               for (int m = 0; m < dims[0]; m++, l++, j++)
                  iData[l] = inIData[j];
            j += computeUI.getSliceSkip();
         }
         outField.addData(DataArray.create(iData, 1, "volume"));
         break;
      }
      pts[1][0] = dims[0] - 1.f;
      pts[1][1] = dims[1] - 1.f;
      pts[1][2] = dims[2] - 1.f;
      pts[0][0] = pts[0][1] = pts[0][2] = 0.f;
      //outField.setDims(dims);
      outField.setExtents(pts);
      if (computeUI.getScale() == null || computeUI.getScale().length != 3)
      {
         float[] sc =
         {
            1.f, 1.f, 1.f
         };
         outField.setScale(sc);
      } else
         outField.setScale(computeUI.getScale());
   }


   @Override
   public void onActive()
   {
      update();
      computeUI.setFieldDescription(outField.toString());
      prepareOutputGeometry();
      show();
      setOutputValue("volume", new VNRegularField(outField));
      setOutputValue("field object", new VNGeometryObject(outObj));
   }
   
   @Override
   public void onInitFinishedLocal() {
       if(isForceFlag()) 
           computeUI.activateOpenDialog();
       
   }
   
}
