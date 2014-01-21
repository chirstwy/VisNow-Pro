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

package pl.edu.icm.visnow.lib.basic.mappers.SegmentedSurfaces;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import pl.edu.icm.visnow.datasets.cells.Cell;
import pl.edu.icm.visnow.datasets.CellArray;
import pl.edu.icm.visnow.datasets.CellSet;
import pl.edu.icm.visnow.datasets.dataarrays.DataArray;
import pl.edu.icm.visnow.datasets.IrregularField;
import pl.edu.icm.visnow.datasets.RegularField;
import pl.edu.icm.visnow.engine.core.InputEgg;
import pl.edu.icm.visnow.engine.core.OutputEgg;
import pl.edu.icm.visnow.gui.events.FloatValueModificationEvent;
import pl.edu.icm.visnow.gui.events.FloatValueModificationListener;
import pl.edu.icm.visnow.lib.utils.field.SmoothTriangulation;
import pl.edu.icm.visnow.lib.types.VNRegularField;
import pl.edu.icm.visnow.lib.templates.visualization.modules.IrregularOutFieldVisualizationModule;
import pl.edu.icm.visnow.lib.types.VNIrregularField;
import pl.edu.icm.visnow.lib.utils.SwingInstancer;

/**
 * @author Krzysztof S. Nowinski (know@icm.edu.pl) Warsaw University, Interdisciplinary Centre for
 * Mathematical and Computational Modelling
 */
public class SegmentedSurfaces extends IrregularOutFieldVisualizationModule
{

   /**
    *
    * inField - a 3D field to create isosurface; currently only regular 3D fields are accepted. at
    * least one scalar data component must be present.
    */
   /**
    * outField - isosurface field will be created by update method - can be void, can contain no
    * node data (geometry only)
    *
    */
   protected RegularField inField;
   protected Params params;
   protected DataArray segmentationData;
   protected GUI computeUI = null;
   protected boolean fromGUI = false;
   protected boolean ignoreGUI = false;
   protected SurfacesCompute surfaces = null;
   protected SmoothTriangulation smoother = new SmoothTriangulation();
   protected String[] names = null;
   protected float[] volumes = null;

   public SegmentedSurfaces()
   {
      parameters = params = new Params();
      outObj.setName("segmented surfaces");
      params.addChangeListener(new ChangeListener()
      {

         public void stateChanged(ChangeEvent evt)
         {
            if (ignoreGUI)
               return;
            fromGUI = true;
            startAction();
         }
      });
      SwingInstancer.swingRunAndWait(new Runnable()
      {

         public void run()
         {
            computeUI = new GUI();
         }
      });
      computeUI.setParams(params);
      ui.addComputeGUI(computeUI);
      setPanel(ui);
   }
   private static InputEgg[] inputEggs = null;
   private static OutputEgg[] outputEggs = null;

   public static InputEgg[] getInputEggs()
   {
      if (inputEggs == null)
         inputEggs = new InputEgg[]
         {
            new InputEgg("inField", VNRegularField.class, InputEgg.NECESSARY | InputEgg.TRIGGERING)
         };
      return inputEggs;
   }

   public static OutputEgg[] getOutputEggs()
   {
      if (outputEggs == null)
         outputEggs = new OutputEgg[]
         {
            new OutputEgg("surfacesField", VNIrregularField.class),
            geometryOutput
         };
      return outputEggs;
   }

   public void update()
   {
      if (params.isDimensionsChanged())
      {
         if (surfaces != null)
            surfaces.clearFloatValueModificationListener();
         surfaces = new SurfacesCompute(inField, null, params);
         surfaces.addFloatValueModificationListener(
                 new FloatValueModificationListener()
                 {

                    public void floatValueChanged(FloatValueModificationEvent e)
                    {
                       setProgress(e.getVal());
                    }
                 });
         surfaces.updateSurfaces();
      }
      int[] nSurfNodes = surfaces.getNNodes();
      int[] nSurfTriangles = surfaces.getNTriangles();
      float[][] surfCoords = surfaces.getCoords();
      float[][] surfNormals = surfaces.getNormals();
      int[][] surfTriangles = surfaces.getTriangles();
      float[][] outCoords = new float[surfCoords.length][];
      if (params.isSmoothing())
         for (int i = 0; i < surfCoords.length; i++)
         {
            if (surfCoords[i] == null || surfTriangles[i] == null)
               continue;
            smoother.setGeometry(surfCoords[i], surfNormals[i], surfTriangles[i]);
            outCoords[i] = smoother.smoothCoords(params.getSmoothSteps(), .3f);
            surfNormals[i] = smoother.smoothNormals(params.getSmoothSteps(), .3f);
         }
      else
         for (int i = 0; i < outCoords.length; i++)
            outCoords[i] = surfCoords[i];
      params.setDimensionsChanged(false);

      int nNodes = 0;
      for (int i = 0; i < nSurfNodes.length; i++)
         nNodes += nSurfNodes[i];
      float[] coords = new float[3 * nNodes];
      float[] normals = new float[3 * nNodes];
      for (int i = 0, k = 0; i < nSurfNodes.length; i++)
      {
         if (surfCoords[i] == null || surfTriangles[i] == null)
            continue;
         for (int j = 0; j < surfCoords[i].length; j++, k++)
         {
            coords[k] = outCoords[i][j];
            normals[k] = surfNormals[i][j];
         }
      }

      outField = new IrregularField(nNodes);
      outField.setNSpace(3);
      outField.setCoords(coords);
      outField.setNormals(normals);

      short[] sets = new short[nNodes];
      for (int n = 0, k = 0, p = 0; n < nSurfNodes.length; n++)
      {
         for (int i = 0; i < nSurfNodes[n]; i++, k++)
            sets[k] = (short) n;
         CellSet cellSet = new CellSet(names[n]);
         if (nSurfNodes[n] == 0)
            continue;
         boolean[] orientations = new boolean[nSurfTriangles[n]];
         for (int i = 0; i < orientations.length; i++)
            orientations[i] = true;
         int[] nodes = new int[surfTriangles[n].length];
         for (int i = 0; i < nodes.length; i++)
            nodes[i] = surfTriangles[n][i] + p;
         p += nSurfNodes[n];
         CellArray triangleArray = new CellArray(Cell.TRIANGLE, nodes, orientations, null);
         cellSet.setCellArray(triangleArray);
         cellSet.setBoundaryCellArray(triangleArray);
         outField.addCellSet(cellSet);
      }

      DataArray da = DataArray.create(sets, 1, "sets");
      outField.addData(da);
      outField.setExtents(inField.getExtents());
      prepareOutputGeometry();
      setOutputValue("surfacesField", new VNIrregularField(outField));
      show(true);
   }

   public void setObjectData(String[] names)
   {
      this.names = new String[names.length - 1];
      int[] indices = new int[names.length - 1];
      int[] counts  = new int[names.length - 1];
      for (int i = 0; i < counts.length; i++)
         counts[i] = indices[i] = 0;
      for (int i = 1; i < names.length; i++)
      {
         String[] item = names[i].split(":");
         if (item.length == 2)
            try
            {
               int k = Integer.parseInt(item[0]);
               if (k < 0 || k >= names.length - 1)
               {
                  System.out.println("bad item " + names[i] + " : " + item[0] + " out of range");
                  continue;
               }
               indices[i - 1] = k;
               this.names[k] = item[1];
            } catch (Exception e)
            {
               System.out.println("bad item " + names[i] + " : " + item[0] + " cannot be parsed as int");
            }
      }
      byte[] bSegData = segmentationData.getBData();
      for (int i = 0; i < bSegData.length; i++)
      {
         int b = indices[bSegData[i] & 0xff];
         counts[b] += 1;
      }
      float[][] affine = inField.getAffine();
      float elementaryCellVolume = 
              affine[0][0] * affine[1][1] * affine[2][2] + affine[0][1] * affine[1][2] * affine[2][0] + affine[0][2] * affine[1][0] * affine[2][1] -
              affine[2][0] * affine[1][1] * affine[0][2] - affine[2][1] * affine[1][2] * affine[0][0] - affine[2][2] * affine[1][0] * affine[0][1];
      Object[][] volumeData = new Object[this.names.length][2];
      for (int i = 0; i < volumeData.length; i++)
      {
         volumeData[i][0] = this.names[i];
         volumeData[i][1] = elementaryCellVolume * counts[i];
      }
      computeUI.setVolumeTableContent(volumeData);
   }

   @Override
   public void onActive()
   {
      if (!fromGUI && getInputFirstValue("inField") != null)
      {
         ignoreGUI = true;
         VNRegularField input = ((VNRegularField) getInputFirstValue("inField"));
         if (input.getField() == null || input.getField().getDims().length != 3)
            return;
         if (inField == null || inField != input.getField())
         {
            inField = input.getField();
            segmentationData = inField.getData(0);
            if (segmentationData.getVeclen() != 1 || segmentationData.getType() != DataArray.FIELD_DATA_BYTE
                    || segmentationData.getUserData() == null || !segmentationData.getUserData()[0].equalsIgnoreCase("MAP"))
               return;
            setObjectData(inField.getData(0).getUserData());
            computeUI.setInField(inField);
            params.setDimensionsChanged(true);
         }
         ignoreGUI = false;
      }
      fromGUI = false;
      if (inField != null)
         update();
   }
}
