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

package pl.edu.icm.visnow.lib.basic.mappers.Skeletonizer;

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
import pl.edu.icm.visnow.geometries.viewer3d.eventslisteners.pick.Pick3DEvent;
import pl.edu.icm.visnow.geometries.viewer3d.eventslisteners.pick.Pick3DListener;
import pl.edu.icm.visnow.geometries.viewer3d.eventslisteners.pick.PickType;
import pl.edu.icm.visnow.gui.events.FloatValueModificationEvent;
import pl.edu.icm.visnow.gui.events.FloatValueModificationListener;
import pl.edu.icm.visnow.lib.templates.visualization.modules.IrregularOutFieldVisualizationModule;
import pl.edu.icm.visnow.lib.types.VNRegularField;
import pl.edu.icm.visnow.lib.types.VNIrregularField;
import pl.edu.icm.visnow.lib.utils.SwingInstancer;

/**
 * @author  Krzysztof S. Nowinski (know@icm.edu.pl)
 * Warsaw University, Interdisciplinary Centre
 * for Mathematical and Computational Modelling
 */
public class Skeletonizer extends IrregularOutFieldVisualizationModule
{

   /**
    * Creates a new instance of CreateGrid
    */
   public static InputEgg[] inputEggs = null;
   public static OutputEgg[] outputEggs = null;
   protected Compute skeletonizer = null;
   protected GUI computeUI;
   protected RegularField lastInField = null;
   protected RegularField inField;
   protected RegularField mapField;
   protected RegularField segmentField; 
   protected Params params;
   protected float[] crds;
   protected short[] rData;
   protected int nPolys;
   protected int[] polylines;
   protected int pickedNode = -1;
   protected FloatValueModificationListener progressListener =
            new FloatValueModificationListener()
            {
               public void floatValueChanged(FloatValueModificationEvent e)
               {
                  setProgress(e.getVal());
               }
            };
   
   protected Pick3DListener pick3DListener = new Pick3DListener(PickType.POINT)
   {
      @Override
      public void handlePick3D(Pick3DEvent e)
      {
         float[] x = e.getPoint();
         if (x == null || outField == null || polylines == null)
            return;
         int k = -1;
         float fmin = Float.MAX_VALUE;
         float[] coords = outField.getCoords();
         for (int i = 0; i < coords.length; i += 3)
         {
            float f = (coords[i] - x[0]) * (coords[i] - x[0]) + 
                      (coords[i + 1] - x[1]) * (coords[i + 1] - x[1]) + 
                      (coords[i + 2] - x[2]) * (coords[i + 2] - x[2]);
            if (f < fmin)
            {
               fmin = f;
               k = i / 3;
            }
         }
         if (k >= 0)
         {
            pickedNode = k;
            params.setRecompute(Params.SELECT);
            startAction();
         }
      }
   };
   
   public Skeletonizer()
   {
      parameters = params = new Params();
      params.addChangeListener(new ChangeListener()
      {
         public void stateChanged(ChangeEvent evt)
         {
            if (params.isRecompute()>0)  
               startAction();
            else
               show();
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
      outObj.setName("skeletonizer");
      setPanel(ui);
   }
   

   @Override
   public Pick3DListener getPick3DListener()
   {
      return pick3DListener;
   }
   
   public void onActive()
   {
      if (getInputFirstValue("inField") == null)
         return;
      inField = ((VNRegularField) getInputFirstValue("inField")).getField();
      if (inField == null)
         return;
      if (inField != lastInField)
      {
         lastInField = inField;
         if (skeletonizer != null)
            skeletonizer.clearFloatValueModificationListener();
         if (inField.getDims().length == 2)
            skeletonizer = new Compute2D(inField, params);
         else
            skeletonizer = new Compute3D(inField, params);
         skeletonizer.addFloatValueModificationListener(progressListener);
         skeletonizer.addActivityListener(computeUI.getActivityListener());
         computeUI.setInfield(inField);
         params.setRecompute(Params.SKELETONIZE);
         return;
      }
      if (inField == null)
         return;
      if (params.isRecompute() == Params.SKELETONIZE)
      {
         skeletonizer.updateSkeleton();
         mapField = inField.cloneBase();
         mapField.addData(inField.getData(params.getComponent()));
         mapField.addData(DataArray.create(skeletonizer.getOutd(), 1, "distance map"));
         setOutputValue("mapField", new VNRegularField(mapField));
      }
      if (params.isRecompute() >= Params.MIN_LENGTH)
      {
         nPolys = 0;
         int[] activeSets = params.getSets();
         if (activeSets == null || activeSets.length < 1)
            activeSets = new int[] {2};
         int[][] nNodes = skeletonizer.getNNodes();
         if (nNodes == null)
            return;
         float[][][] coords = skeletonizer.getCoords();
         short[][][] radii = skeletonizer.getRadiiData();
         int nVerts = 0, nCells = 0, nMax = 0;
         for (int iSet = 0; iSet < activeSets.length; iSet++)
         {
            int nSet = activeSets[iSet] - 2;
            if (nSet > nMax)
               nMax = nSet;
            if (nNodes[nSet] == null || nNodes[nSet].length < 1)
               continue;
            int cNPolys = nNodes[nSet].length;
            for (int iPoly = 0; iPoly < cNPolys; iPoly++)
               if (nNodes[nSet][iPoly] >= params.getMinSegLen())
               {
                  nVerts += nNodes[nSet][iPoly];
                  nCells += nNodes[nSet][iPoly] - 1;
                  nPolys += 1;
               }
         }
         polylines = new int[nPolys + 1];
         polylines[0] = 0;
         if (nVerts == 0)
            return;
         crds = new float[3 * nVerts];
         params.setRecompute(0);
         int[] cells = new int[2 * nCells];
         rData = new short[nVerts];
         for (int iSet = 0, k = 0, l = 0, m = 0, p = 1; iSet < activeSets.length; iSet++)
         {
            int nSet = activeSets[iSet] - 2;
            if (nNodes[nSet] == null || nNodes[nSet].length < 1)
               continue;
            int cNPolys = nNodes[nSet].length;
            for (int iPoly = 0; iPoly < cNPolys; iPoly++)
            {
               if (nNodes[nSet][iPoly] < params.getMinSegLen())
                  continue;
               System.arraycopy(radii[nSet][iPoly], 0, rData, m, nNodes[nSet][iPoly]);
               for (int i = 0; i < coords[nSet][iPoly].length; i++, k++)
                  crds[k] = coords[nSet][iPoly][i];
               polylines[p] = polylines[p - 1] + nNodes[nSet][iPoly];
               p += 1;
               for (int i = 0; i < nNodes[nSet][iPoly] - 1; i++, l += 2)
               {
                  cells[l] = m + i;
                  cells[l + 1] = m + i + 1;
               }
               m += nNodes[nSet][iPoly];
            }
         }
         boolean[] edgeOrientations = new boolean[nCells];
         for (int i = 0; i < edgeOrientations.length; i++)
            edgeOrientations[i] = true;
         outField = new IrregularField(nVerts);
         outField.setNSpace(3);
         outField.setCoords(crds);
         CellSet cellSet = new CellSet(inField.getName()+"skeleton");
         DataArray da = DataArray.create(rData, 1, "radii");
         outField.addData(da);
         CellArray skeletonSegments = new CellArray(Cell.SEGMENT, cells, edgeOrientations, null);
         cellSet.setBoundaryCellArray(skeletonSegments);
         cellSet.setCellArray(skeletonSegments);
         outField.addCellSet(cellSet);
         outField.setExtents(inField.getExtents());
         prepareOutputGeometry();
         setOutputValue("outField", new VNIrregularField(outField));
      }
      if (params.isRecompute() == Params.SELECT)
         for (int i = 1; i < polylines.length; i++)
         if (pickedNode < polylines[i])
         {
            int low = polylines[i - 1];
            int up = polylines[i];
            int n = up - low;
            System.out.printf("%5d < %5d < %5d%n", low, pickedNode, up);
            int[] segDims = new int[] {n};
            segmentField = new RegularField(segDims);
            segmentField.setNSpace(3);
            float[] segCoords = new float[3 * n];
            short[] segRData = new short[n];
            System.arraycopy(crds, 3 * low, segCoords, 0, 3 * n);
            System.arraycopy(rData, low, segRData, 0, n);
            segmentField.setCoords(segCoords);
            segmentField.addData(DataArray.create(segRData, 1, "radii"));
            setOutputValue("segmentField", new VNRegularField(segmentField));
            break;
      }
      show();
      params.setRecompute(0);
   }
}
