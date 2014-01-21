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

package pl.edu.icm.visnow.lib.basic.mappers.CityPlot;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import pl.edu.icm.visnow.datasets.cells.Cell;
import pl.edu.icm.visnow.datasets.CellArray;
import pl.edu.icm.visnow.datasets.CellSet;
import pl.edu.icm.visnow.datasets.dataarrays.DataArray;
import pl.edu.icm.visnow.datasets.IrregularField;
import pl.edu.icm.visnow.engine.core.InputEgg;
import pl.edu.icm.visnow.engine.core.OutputEgg;
import pl.edu.icm.visnow.gui.events.FloatValueModificationEvent;
import pl.edu.icm.visnow.datasets.RegularField;
import pl.edu.icm.visnow.geometries.parameters.AbstractRenderingParams;
import pl.edu.icm.visnow.gui.events.FloatValueModificationListener;
import pl.edu.icm.visnow.lib.templates.visualization.modules.IrregularOutFieldVisualizationModule;
import pl.edu.icm.visnow.lib.types.VNRegularField;
import pl.edu.icm.visnow.lib.utils.SwingInstancer;

/**
 * @author  Krzysztof S. Nowinski (know@icm.edu.pl)
 * Warsaw University, Interdisciplinary Centre
 * for Mathematical and Computational Modelling
 */
public class CityPlot extends IrregularOutFieldVisualizationModule
{

   protected RegularField inField;
   protected GUI computeUI = null;
   protected boolean ignoreUI = false;
   protected boolean fromUI = false;
   protected Params params = null;
   protected int[] inDims = null;
   protected float[] coords = null;
   protected int axis = 0, lastAxis = 0;
   protected int[] cells = null;
   protected boolean[] orientations = null;
   protected int[] edges = null;
   protected boolean[] edgeOrientations = null;
   protected boolean ribbon = true;
   protected float[][] extents = new float[2][3];
   protected float[][] physExtents = new float[2][3];
   

   public CityPlot()
   {
      parameters = params = new Params();
      SwingInstancer.swingRunAndWait(new Runnable()
      {
         public void run()
         {
            computeUI = new GUI();
            computeUI.setParams(params);
            ui.addComputeGUI(computeUI);
         }
      });
      outObj.setName("isosurface");
      params.addChangeListener(new ChangeListener()
      {
         public void stateChanged(ChangeEvent evt)
         {
            fromUI = true;
            if (ignoreUI)
               return;
            if (params.isAdjusting()
                    && irregularFieldGeometry != null)
            {
               updateCoords();
               irregularFieldGeometry.updateCoords();
            } else
               startAction();
         }
      });
      setPanel(ui);
   }
   protected FloatValueModificationListener progressListener = new FloatValueModificationListener()
   {

      public void floatValueChanged(FloatValueModificationEvent e)
      {
         setProgress(e.getVal());
      }
   };
   public static InputEgg[] inputEggs = null;
   public static OutputEgg[] outputEggs = null;

   void updateCells()
   {
      cells = new int[8 * inDims[1] * inDims[0]];
      for (int i = 0 ; i < cells.length; i++)
         cells[i] = i;
      orientations = new boolean[inDims[1] * inDims[0]];
      for (int i = 0; i < orientations.length; i++)
         orientations[i] = true;
   }

   void updateCoords()
   {
      float scale = params.getScale();
      float[] vals = inField.getData(params.getComponent()).getFData();
      float off0 = -.5f * inDims[0];
      float off1 = -.5f * inDims[1];
      float base = 0;
      if (!params.isZeroBased())
         base = scale * inField.getData(params.getComponent()).getMinv();
      extents[0][0] = off0;
      extents[0][1] = off1;
      extents[0][2] = inField.getData(params.getComponent()).getMinv() * scale;
      extents[1][0] = -off0;
      extents[1][1] = -off1;
      extents[1][2] = inField.getData(params.getComponent()).getMaxv() * scale;

      physExtents[0][0] = 0;
      physExtents[0][1] = 0;
      if (params.isZeroBased() && inField.getData(params.getComponent()).getMinv() > 0)
         physExtents[0][2] = 0;
      else
         physExtents[0][2] = inField.getData(params.getComponent()).getMinv();
      physExtents[1][0] = inDims[0];
      physExtents[1][1] = inDims[1];
      physExtents[1][2] = inField.getData(params.getComponent()).getMaxv();
      float xLow = params.getXRange()[0];
      float xUp  = params.getXRange()[1];
      float yLow = params.getYRange()[0];
      float yUp  = params.getYRange()[1];
      switch (params.getType())
      {
         case Params.CONSTANT:
            for (int i = 0, k = 0; i < inDims[1]; i++)
               for (int j = 0; j < inDims[0]; j++, k += 24)
               {
                  float  top = scale * vals[inDims[0] * i + j];
                  float xlow = off0 + j + xLow;
                  float xup  = off0 + j + xUp;
                  float ylow = off1 + i + yLow;
                  float yup  = off1 + i + yUp;
                  coords[k     ] = xlow; coords[k + 1 ] = ylow; coords[k + 2 ] = base ;
                  coords[k + 3 ] = xup;  coords[k + 4 ] = ylow; coords[k + 5 ] = base ;
                  coords[k + 6 ] = xup;  coords[k + 7 ] = yup;  coords[k + 8 ] = base ;
                  coords[k + 9 ] = xlow; coords[k + 10] = yup;  coords[k + 11] = base ;
                  coords[k + 12] = xlow; coords[k + 13] = ylow; coords[k + 14] = top ;
                  coords[k + 15] = xup;  coords[k + 16] = ylow; coords[k + 17] = top ;
                  coords[k + 18] = xup;  coords[k + 19] = yup;  coords[k + 20] = top ;
                  coords[k + 21] = xlow; coords[k + 22] = yup;  coords[k + 23] = top ;
               }
            break;
         case Params.SQUARE:
            float[] xyVals = inField.getData(params.getXYComponent()).getFData();
            float xyValMin = inField.getData(params.getXYComponent()).getMinv();
            float dxy = (xUp - xLow) / (inField.getData(params.getXYComponent()).getMaxv() - xyValMin);
            for (int i = 0, k = 0; i < inDims[1]; i++)
               for (int j = 0; j < inDims[0]; j++, k += 24)
               {
                  float  top = scale * vals[inDims[0] * i + j];
                  float d = dxy * (xyVals[inDims[0] * i + j] - xyValMin);
                  float xlow = off0 + j + xLow;
                  float xup  = off0 + j + xLow + d;
                  float ylow = off1 + i + yLow;
                  float yup  = off1 + i + yLow + d;
                  coords[k     ] = xlow; coords[k + 1 ] = ylow; coords[k + 2 ] = base ;
                  coords[k + 3 ] = xup;  coords[k + 4 ] = ylow; coords[k + 5 ] = base ;
                  coords[k + 6 ] = xup;  coords[k + 7 ] = yup;  coords[k + 8 ] = base ;
                  coords[k + 9 ] = xlow; coords[k + 10] = yup;  coords[k + 11] = base ;
                  coords[k + 12] = xlow; coords[k + 13] = ylow; coords[k + 14] = top ;
                  coords[k + 15] = xup;  coords[k + 16] = ylow; coords[k + 17] = top ;
                  coords[k + 18] = xup;  coords[k + 19] = yup;  coords[k + 20] = top ;
                  coords[k + 21] = xlow; coords[k + 22] = yup;  coords[k + 23] = top ;
               }
            break;
         case Params.RECT:
            float[] xVals = inField.getData(params.getXComponent()).getFData();
            float xValMin = inField.getData(params.getXComponent()).getMinv();
            float dx = (xUp - xLow) / (inField.getData(params.getXComponent()).getMaxv() - xValMin);
            float[] yVals = inField.getData(params.getYComponent()).getFData();
            float yValMin = inField.getData(params.getYComponent()).getMinv();
            float dy = (yUp - yLow) / (inField.getData(params.getYComponent()).getMaxv() - yValMin);
            for (int i = 0, k = 0; i < inDims[1]; i++)
               for (int j = 0; j < inDims[0]; j++, k += 24)
               {
                  float top = scale * vals[inDims[0] * i + j];
                  float d = dx * (xVals[inDims[0] * i + j] - xValMin);
                  float xlow = off0 + j + xLow;
                  float xup  = off0 + j + xLow + d;
                  float dd = dy * (yVals[inDims[0] * i + j] - yValMin);
                  float ylow = off1 + i + yLow;
                  float yup  = off1 + i + yLow + dd;
                  coords[k     ] = xlow; coords[k + 1 ] = ylow; coords[k + 2 ] = base ;
                  coords[k + 3 ] = xup;  coords[k + 4 ] = ylow; coords[k + 5 ] = base ;
                  coords[k + 6 ] = xup;  coords[k + 7 ] = yup;  coords[k + 8 ] = base ;
                  coords[k + 9 ] = xlow; coords[k + 10] = yup;  coords[k + 11] = base ;
                  coords[k + 12] = xlow; coords[k + 13] = ylow; coords[k + 14] = top ;
                  coords[k + 15] = xup;  coords[k + 16] = ylow; coords[k + 17] = top ;
                  coords[k + 18] = xup;  coords[k + 19] = yup;  coords[k + 20] = top ;
                  coords[k + 21] = xlow; coords[k + 22] = yup;  coords[k + 23] = top ;
               }
            break;
      }
       outField.updateExtents();
       outField.setExtents(extents);
       outField.setPhysExts(physExtents);
   }

   @Override
   public void onActive()
   {
      boolean newField = false;
      fromUI = false;
      if (!params.isActive())
         return;
      VNRegularField inFld = (VNRegularField) getInputFirstValue("inField");
      if (inFld != null)
      {
         RegularField in = inFld.getField();
         if (in == null || in.getNData() < 1
                 || in.getDims().length != 2 || in.getDims()[0] < 2 || in.getDims()[1] < 2)
            return;
         if (in != inField)
         {
            if (!in.isDataCompatibleWith(inField))
            {
               ignoreUI = true;
               computeUI.setInField(in);
               ignoreUI = false;
               newField = true;
            }
            if (!in.isStructureCompatibleWith(inField))
               newField = true;
            inField = in;
            inDims = inField.getDims();
            if (inDims.length != 2 || inDims[0] < 2 || inDims[1] < 2)
               return;
            if (newField)
            {
               outField = new IrregularField(8 * inField.getNNodes());
               outField.setNSpace(3);
               coords = new float[3 * outField.getNNodes()];
               outField.setCoords(coords);
               updateCells();
               CellArray boxes = new CellArray(Cell.HEXAHEDRON, cells, orientations, null);
               CellSet cs = new CellSet();
               cs.setCellArray(boxes);
               cs.generateExternFaces();
               outField.addCellSet(cs);
            }
            for (DataArray dta : inField.getDataArrays())
               if (dta.isSimpleNumeric() && dta.getVeclen() == 1)
                  switch (dta.getType())
                  {
                     case DataArray.FIELD_DATA_BYTE:
                        byte[] inDB = dta.getBData();
                        byte[] outDB = new byte[8 * dta.getNData()];
                        for (int i = 0; i < outDB.length; i++)
                           outDB[i] = inDB[i / 8];
                        outField.addData(DataArray.create(outDB, 1, dta.getName()));
                        break;
                     case DataArray.FIELD_DATA_SHORT:
                        short[] inDS = dta.getSData();
                        short[] outDS = new short[8 * dta.getNData()];
                        for (int i = 0; i < outDS.length; i++)
                           outDS[i] = inDS[i / 8];
                        outField.addData(DataArray.create(outDS, 1, dta.getName()));
                        break;
                     case DataArray.FIELD_DATA_INT:
                        int[] inDI = dta.getIData();
                        int[] outDI = new int[8 * dta.getNData()];
                        for (int i = 0; i < outDI.length; i++)
                           outDI[i] = inDI[i / 8];
                        outField.addData(DataArray.create(outDI, 1, dta.getName()));
                        break;
                     case DataArray.FIELD_DATA_FLOAT:
                        float[] inDF = dta.getFData();
                        float[] outDF = new float[8 * dta.getNData()];
                        for (int i = 0; i < outDF.length; i++)
                           outDF[i] = inDF[i / 8];
                        outField.addData(DataArray.create(outDF, 1, dta.getName()));
                        break;
                     case DataArray.FIELD_DATA_DOUBLE:
                        double[] inDD = dta.getDData();
                        double[] outDD = new double[8 * dta.getNData()];
                        for (int i = 0; i < outDD.length; i++)
                           outDD[i] = inDD[i / 8];
                        outField.addData(DataArray.create(outDD, 1, dta.getName()));
                        break;
                  }
         }


         updateCoords();
         prepareOutputGeometry();
//         ui.getMapperGUI().getIrregularFieldPresentationGUI().getDisplayPropertiesGUI().setShadingMode(false);
         irregularFieldGeometry.getFieldDisplayParams().setShadingMode(AbstractRenderingParams.FLAT_SHADED);
         show();
      }
   }
}
