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

package pl.edu.icm.visnow.lib.basic.mappers.Isovolume;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import pl.edu.icm.visnow.datasets.CellArray;
import pl.edu.icm.visnow.datasets.CellSet;
import pl.edu.icm.visnow.datasets.Field;
import pl.edu.icm.visnow.datasets.IrregularField;
import pl.edu.icm.visnow.datasets.RegularField;
import pl.edu.icm.visnow.datasets.cells.Cell;
import pl.edu.icm.visnow.datasets.dataarrays.DataArray;
import pl.edu.icm.visnow.engine.core.InputEgg;
import pl.edu.icm.visnow.engine.core.OutputEgg;
import pl.edu.icm.visnow.gui.events.FloatValueModificationEvent;
import pl.edu.icm.visnow.gui.events.FloatValueModificationListener;
import pl.edu.icm.visnow.lib.templates.visualization.modules.IrregularOutFieldVisualizationModule;
import pl.edu.icm.visnow.lib.types.VNField;
import pl.edu.icm.visnow.lib.types.VNIrregularField;
import pl.edu.icm.visnow.lib.utils.SwingInstancer;
import pl.edu.icm.visnow.lib.utils.numeric.IrregularFieldSplitter;
import pl.edu.icm.visnow.lib.utils.numeric.RegularFieldSplitter;

/**
 * @author  Krzysztof S. Nowinski (know@icm.edu.pl)
 * Warsaw University, Interdisciplinary Centre
 * for Mathematical and Computational Modelling
 */
public class Isovolume extends IrregularOutFieldVisualizationModule
{

   /**
    *
    * inField - a 3D field to create isovolume;
    * at least one scalar data component must be present.
    * 
    * outField -  isovolume field will be created by update method -
    * can be void, can contain no node data (geometry only)
    *
    */
   public static final int HISTOGRAM_BUCKETS_NUMBER = 1048576;
   public static final int BUCKETS_NUMBER = 65536;
   public static InputEgg[] inputEggs = null;
   public static OutputEgg[] outputEggs = null;
   protected Field inField;
   
   private int componentNumber = -1;
   private float[] isoData;
   
   protected IrregularField irregularInField;
   
   protected RegularField regularInField;
   private int[] dims;
   private int nDims;
   private int nCells;
   private int[] cellNodeOffsets;
   private float[] regularCellLowData;
   private float[] regularCellUpData;
   
   protected GUI computeUI = null;
   protected float lastThr = 127;
   protected float lastTime = -1;
   protected boolean fromGUI = false;
   protected Params params;
   protected boolean debug = true;
   protected boolean ignoreUI = false;
   private long lastRecompute;
   

   public Isovolume()
   {
      parameters = params = new Params();
      SwingInstancer.swingRunAndWait(new Runnable()
      {

         @Override
         public void run()
         {
            computeUI = new GUI();
         }
      });
      computeUI.setParams(params);
      ui.addComputeGUI(computeUI);
      outObj.setName("isosurface");
      params.addChangeListener(new ChangeListener()
      {
         @Override
         public void stateChanged(ChangeEvent evt)
         {
            if (ignoreUI)
            {
               ignoreUI = false;
               return;
            }
            fromGUI = true;
            startAction();
         }
      });
      setPanel(ui);
   }
   
   protected FloatValueModificationListener progressListener = new FloatValueModificationListener()
   {
      @Override
      public void floatValueChanged(FloatValueModificationEvent e)
      {
         setProgress(e.getVal());
      }
   };
   
   
   private void updateUI()
   {
      computeUI.setInField(inField);
   }

   @Override
   public void onActive()
   {
      boolean newField = false;
      if (!fromGUI)
      {
         ignoreUI = true;
         VNField inFld = (VNField) getInputFirstValue("inField");
         if (inFld != null)
         {
            Field newInField = inFld.getField();
            if (newInField == null || 
                newInField.getNSpace() != 3 || 
                newInField.getNData() < 1)
            {
               ignoreUI = false;
               return;
            }
            newField = true;
            if (inField == null || inField != newInField)
            {
               inField = newInField;
               if (inField instanceof IrregularField)
                  irregularInField = (IrregularField)inField;
               else
               {
                  regularInField = (RegularField)inField;
                  dims = regularInField.getDims();
                  nDims = dims.length;
               }
               updateUI();
               params.setRecompute(true);
               lastTime = newInField.getCurrentTime();
               componentNumber = -1;
            } 
            else if (newInField.getCurrentTime() != lastTime)
            {
               updateUI();
               params.setRecompute(true);
               lastTime = newInField.getCurrentTime();
            }
         ignoreUI = false;
         }
      }
      fromGUI = false;
      if (params.getIsoComponent() != componentNumber || newField)
      {
         newField = false;
         componentNumber = params.getIsoComponent();
         DataArray isoDataArray = inField.getData(params.getIsoComponent());
         isoData = isoDataArray.getFData();
         if (inField instanceof IrregularField)
         {
         for (CellSet cs : irregularInField.getCellSets())
            for (int i = 0; i < Cell.TYPES; i++)
               if (cs.getCellArray(i) != null)
                  cs.getCellArray(i).addComponentData(1, isoDataArray);
         }
         else
         {
            cellNodeOffsets = regularInField.getCellNodeOffsets();
            switch (nDims)
            {
            case 1:
               nCells = dims[0] - 1;
               regularCellLowData = new float[nCells];
               regularCellUpData = new float[nCells];
               for (int i0 = 0; i0 < dims[0] - 1; i0++)
               {
                  regularCellLowData[i0] = regularCellUpData[i0] = isoData[i0];
                     if (isoData[i0 + 1] < regularCellLowData[i0]) regularCellLowData[i0] = isoData[i0 + 1];
                     if (isoData[i0 + 1] > regularCellUpData[i0])  regularCellUpData[i0]  = isoData[i0 + 1];
               }
               break;
            case 2:
               nCells = (dims[0] - 1) * (dims[1] - 1);
               regularCellLowData = new float[nCells];
               regularCellUpData = new float[nCells];
               for (int i1 = 0, k = 0; i1 < dims[1] - 1; i1++)
                  for (int i0 = 0, l = i1 * dims[0]; i0 < dims[0] - 1; i0++, k++, l++)
                  {
                     regularCellLowData[k] = regularCellUpData[k] = isoData[l];
                     for (int i = 1; i < cellNodeOffsets.length; i++)
                     {
                        int j = l + cellNodeOffsets[i];
                        if (isoData[j] < regularCellLowData[k]) regularCellLowData[k] = isoData[j];
                        if (isoData[j] > regularCellUpData[k])  regularCellUpData[k]  = isoData[j];
                     }
                  }
               break;
            case 3:
               nCells = (dims[0] - 1) * (dims[1] - 1) * (dims[2] - 1);
               regularCellLowData = new float[nCells];
               regularCellUpData = new float[nCells];
               for (int i2 = 0, k = 0; i2 < dims[2] - 1; i2++)
                  for (int i1 = 0; i1 < dims[1] - 1; i1++)
                     for (int i0 = 0, l = (dims[1] * i2 + i1) * dims[0]; i0 < dims[0] - 1; i0++, k++, l++)
                     {
                        regularCellLowData[k] = regularCellUpData[k] = isoData[l];
                        for (int i = 1; i < cellNodeOffsets.length; i++)
                        {
                           int j = l + cellNodeOffsets[i];
                           if (isoData[j] < regularCellLowData[k]) regularCellLowData[k] = isoData[j];
                           if (isoData[j] > regularCellUpData[k])  regularCellUpData[k]  = isoData[j];
                        }
                     }
               break;
            }
         }
      }
      
      float threshold = params.getThreshold();
      if (inField instanceof IrregularField)
      {
         int[] cellTypes = null; 
         IrregularFieldSplitter splitter = new IrregularFieldSplitter(irregularInField, params.getType());

         for (int nSet = 0; nSet < irregularInField.getNCellSets(); nSet++)
         {
            CellSet trCS = irregularInField.getCellSet(nSet);
            if (debug)
            {
               int nC = 0;
               for (int iCellArray = 0; iCellArray < trCS.getCellArrays().length; iCellArray++)
                  if (trCS.getCellArray(iCellArray) != null)
                     nC += trCS.getCellArray(iCellArray).getNCells();
               cellTypes = new int[nC];
            }
            
            int iC = 0;
            splitter.initCellSetSplit(trCS);
            for (int iCellArray = 0; iCellArray < trCS.getCellArrays().length; iCellArray++)
            {
               if (trCS.getCellArray(iCellArray) == null)
                  continue;
               CellArray ca = trCS.getCellArray(iCellArray);
               boolean isTriangulated = ca.isTriangulation();
               if (debug)
               {
                  int[] indices = new int[ca.getNCells()]; 
                  for (int i = 0; i < indices.length; i++, iC++)
                     indices[i] = iC;
                  ca.setDataIndices(indices);
               }

               splitter.initCellArraySplit(ca);
               int nCellNodes = ca.getCellNodes();
               float[] cellLow = ca.getCellLow();
               float[] cellUp = ca.getCellUp();
               float[] vals = new float[nCellNodes];
               int[] indices = ca.getDataIndices();
               int[] nodes = ca.getNodes();
               int[] cellNodes = new int[ca.getCellNodes()];
               int m;
               for (int iCell = 0; iCell < ca.getNCells(); iCell++)
               {
                  int index = -1;
                  if (indices != null)
                     index = indices[iCell];
                  if (cellLow[iCell] < threshold && threshold < cellUp[iCell])
                  {
                     for (int i = 0; i < cellNodes.length; i++)
                     {
                        cellNodes[i] = m = nodes[nCellNodes * iCell + i];
                        vals[i] = isoData[m] - threshold;
                     }
                     if (isTriangulated)
                     {
                        if (debug)
                           cellTypes[indices[iCell]] = pl.edu.icm.visnow.lib.utils.numeric.SliceLookupTable.getSubcellType(ca.getType(), vals, params.getType() > 0);
                        splitter.processSimplex(cellNodes, vals, index);
                     }
                     else splitter.processCell(cellNodes, vals, index);
                  }

                  else if (params.getType() == -1 && cellUp[iCell] <= threshold ||
                           params.getType() == 1  && cellLow[iCell] >= threshold)
                  {
                     if (isTriangulated)
                     {
                        if (debug)
                           cellTypes[indices[iCell]] = pl.edu.icm.visnow.lib.utils.numeric.SliceLookupTable.getSubcellType(ca.getType(), vals, params.getType() > 0);
                        splitter.addSimplex(iCell);
                     }
                     else
                        splitter.addCellTriangulation(iCell);
                  }
               }
            }
            if (debug)
               trCS.addData(DataArray.create(cellTypes, 1, "cell types"));
         }     
         outField = splitter.createOutField(null);
      } 
      else
      {
         RegularFieldSplitter splitter = new RegularFieldSplitter(regularInField, params.getType());
         float[] vals;
         int[] splicedCell;
         switch (nDims)
         {
         case 1:
            vals = new float[2];
            splicedCell = new int[4];
            for (int i0 = 0; i0 < dims[0] - 1; i0++)
               if (regularCellLowData[i0] < threshold && threshold <regularCellUpData [i0])
               {
                  for (int i = 0; i < 2; i++)
                  {
                     splicedCell[i] = i0 + cellNodeOffsets[i];
                     vals[i] = isoData[splicedCell[i]] - threshold;
                  }
                  splitter.processCell(splicedCell, vals, i0 % 2 == 0);
               }

               else if (params.getType() == -1 && regularCellUpData[i0] < threshold ||
                        params.getType() == 1  && regularCellLowData[i0] > threshold)
               {
                     splitter.addCellTriangulation(i0);
               }
            break;
         case 2:
            vals = new float[4];
            splicedCell = new int[4];
            for (int i1 = 0, iCell = 0; i1 < dims[1] - 1; i1++)
               for (int i0 = 0, l = i1 * dims[0]; i0 < dims[0] - 1; i0++, iCell++, l++)
                  if (regularCellLowData[iCell] < threshold && threshold <regularCellUpData [iCell])
                  {
                     for (int i = 0; i < 4; i++)
                     {
                        splicedCell[i] = l + cellNodeOffsets[i];
                        vals[i] = isoData[splicedCell[i]] - threshold;
                     }
                     splitter.processCell(splicedCell, vals, (i0 + i1) % 2 == 0);
                  }

                  else if (params.getType() == -1 && regularCellUpData[iCell] < threshold ||
                           params.getType() == 1  && regularCellLowData[iCell] > threshold)
                  {
                        splitter.addCellTriangulation(i0, i1);
                  }
            break;
         case 3:
            vals = new float[8];
            splicedCell = new int[8];
            for (int i2 = 0, iCell = 0; i2 < dims[2] - 1; i2++)
               for (int i1 = 0; i1 < dims[1] - 1; i1++)
                  for (int i0 = 0, l = (dims[1] * i2 + i1) * dims[0]; i0 < dims[0] - 1; i0++, iCell++, l++)
                     if (regularCellLowData[iCell] < threshold && threshold <regularCellUpData [iCell])
                     {
                        for (int i = 0; i < 8; i++)
                        {
                           splicedCell[i] = l + cellNodeOffsets[i];
                           vals[i] = isoData[splicedCell[i]] - threshold;
                        }
                        splitter.processCell(splicedCell, vals, (i0 + i1 + i2) % 2 == 0);
                     }

                     else if (params.getType() == -1 && regularCellUpData[iCell] < threshold ||
                              params.getType() == 1  && regularCellLowData[iCell] > threshold)
                     {
                           splitter.addCellTriangulation(i0, i1, i2);
                     }
            break;
         }
         outField = splitter.createOutField(null);
      }
      if (outField != null)
      {
         prepareOutputGeometry();
         setOutputValue("outField", new VNIrregularField(outField));
         show();
      }
      
   }
   
    private transient FloatValueModificationListener statusListener = null;

    public void clearFloatValueModificationListener() {
        statusListener = null;
    }

    public void addFloatValueModificationListener(FloatValueModificationListener listener) {
        if (statusListener == null) {
            this.statusListener = listener;
        } else {
            throw new RuntimeException("" + this + ": only one status listener can be added");
        }
    }

    protected void fireStatusChanged(float status) {
        if (statusListener != null) {
            FloatValueModificationEvent e = new FloatValueModificationEvent(this, status, true);
            statusListener.floatValueChanged(e);
        }
    }
}
