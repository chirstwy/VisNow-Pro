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

package pl.edu.icm.visnow.lib.basic.mappers.Skeletonizer;

import java.util.Arrays;
import pl.edu.icm.visnow.datasets.RegularField;
import pl.edu.icm.visnow.datasets.dataarrays.DataArray;
import pl.edu.icm.visnow.lib.utils.field.ExtendMargins;

/**
 ** @author Krzysztof S. Nowinski, University of Warsaw ICM
 */

public class Compute3D extends Compute
{

   public Compute3D()
   {
   }

   public Compute3D(RegularField inField, Params params)
   {
      this.params = params;
      setInField(inField);
   }


   private class ComputeSetBoundary3D implements Runnable
   {
      int iThread = 0, cSet = 0;

      public ComputeSetBoundary3D(int iThread, int cSet)
      {
         this.iThread = iThread;
         this.cSet = cSet;
      }

      @Override
      public void run()
      {
         for (int i2 = (iThread * dims[2]) / nThreads; i2 < ((iThread + 1) * dims[2]) / nThreads; i2++)
         {
            if (iThread == 0)
               fireStatusChanged((i2 * .1f) / dims[2]);
            if (i2 == 0 || i2 == dims[2] - 1)
               continue;
            for (int i1 = 1; i1 < dims[1] - 1; i1++)
               for (int i0 = 1, i = (i2 * dims[1] + i1) * dims[0] + 1; i0 < dims[0] - 1; i0++, i++)
                  if (bData[i] == cSet)
                     boundaryDistance[i] = BIGVAL;
                  else
                  {
                     for (int j = 0; j < nNeighb; j++)
                        if (bData[i + off[j]] == cSet)
                        {
                           boundaryDistance[i] = -1;
                           nodeQueue.insert(i);
                           break;
                        }
                  }
         }
      }
   }

   private class ComputeThresholdBoundary3D implements Runnable
   {
      int iThread = 0;

      public ComputeThresholdBoundary3D(int iThread)
      {
         this.iThread = iThread;
      }

      @Override
      public void run()
      {
         for (int i2 = (iThread * dims[2]) / nThreads; i2 < ((iThread + 1) * dims[2]) / nThreads; i2++)
         {
            if (iThread == 0)
               fireStatusChanged((i2 * .1f) / dims[2]);
            if (i2 == 0 || i2 == dims[2] - 1)
               continue;
            for (int i1 = 1; i1 < dims[1] - 1; i1++)
               for (int i0 = 1, i = (i2 * dims[1] + i1) * dims[0] + 1; i0 < dims[0] - 1; i0++, i++)
               {
                  if (bData[i] != 0)
                     boundaryDistance[i] = BIGVAL;
                  else
                  {
                     for (int j = 0; j < nNeighb; j++)
                     {
                        if (bData[i + off[j]] != 0)
                        {
                           boundaryDistance[i] = -1;
                           nodeQueue.insert(i);
                           break;
                        }
                     }
                  }
               }
         }
      }
   }


   @Override
   protected void findInnermostNodes()
   {
      nCentrePoints = 0;
      Arrays.fill(bData, (byte)0);
      for (int i2 = 1; i2 < dims[2] - 1; i2++)
         for (int i1 = 1; i1 < dims[1] - 1; i1++)
            for (int i0 = 1, i = (i2 * dims[1] + i1) * dims[0] + 1; i0 < dims[0] - 1; i0++, i++)
            {
               short d = boundaryDistance[i];
               if (d == 0)
                  continue;
               boolean ismax = true;
               for (int j = 0; j < nNeighb; j++)
                  if (bData[i + off[j]] == 1)
                     continue;
                  else if (boundaryDistance[i + off[j]] > d)
                  {
                     ismax = false;
                     break;
                  }
                  else if (boundaryDistance[i + off[j]] == d && bData[i] == 0)
                  {
                     ismax = false;
                     bData[i] = 1;
                     break;
                  }
               if (ismax)
               {
                  if (nCentrePoints >= centres.length)
                  {
                     int[] tmpCentres = new int[2 * centres.length];
                     short[] tmpRadii = new short[2 * centres.length];
                     System.arraycopy(centres, 0, tmpCentres, 0, centres.length);
                     System.arraycopy(radii, 0, tmpRadii, 0, radii.length);
                     centres = tmpCentres;
                     radii = tmpRadii;
                  }
                  centres[nCentrePoints] = i;
                  radii[nCentrePoints] = boundaryDistance[i];
                  nCentrePoints += 1;
               }
            }
   }


   @Override
   protected void setBoundaryData(int cSet)
   {
      if (inData.getType() == DataArray.FIELD_DATA_BYTE)
         System.arraycopy(inData.getBData(), 0, bData, 0, bData.length);
      else
         bData = inData.getBData();
      if (mask != null)
         for (int m = 0; m < mask.length; m++)
            if (!mask[m])
               bData[m] = 0;
      Thread[] workThreads = new Thread[nThreads];
      for (int iThread = 0; iThread < nThreads; iThread++)
      {
         workThreads[iThread] = new Thread(new ComputeSetBoundary3D(iThread, cSet));
         workThreads[iThread].start();
      }
      for (int i = 0; i < workThreads.length; i++)
         try
         {
            workThreads[i].join();
         } catch (Exception e)
         {
         }
      for (int i2 = 1; i2 < dims[2] - 1; i2++)
      {
         fireStatusChanged((i2 * .1f) / dims[2]);
         for (int i1 = 1; i1 < dims[1] - 1; i1++)
            for (int i0 = 1, i = (i2 * dims[1] + i1) * dims[0] + 1; i0 < dims[0] - 1; i0++, i++)
               if (bData[i] == cSet)
               {
                  boundaryDistance[i] = BIGVAL;
                  for (int j = 0; j < nNeighb; j++)
                     if (bData[i + off[j]] != cSet)
                     {
                        boundaryDistance[i] = -1;
                        nodeQueue.insert(i);
                        break;
                     }
               }
      }
      ExtendMargins.fillOut3DMargins(boundaryDistance, dims, (byte)0);
      // outd is now initialized by: 0 if outside on 1 voxel wide margim, -1 if on the boundary, MAXVAL if inside
      // nodeQueue is initialized as the list of indices of boundary voxels
   }


   @Override
   protected void setBoundaryData()
   {
      fireActivityChanged("finding volume boundary");
      if (above)
         switch (inData.getType())
         {
            case DataArray.FIELD_DATA_BYTE:
            case DataArray.FIELD_DATA_SHORT:
               short[] sData = inData.getSData();
               for (int m = 0; m < ndata; m++)
                  bData[m] = (sData[m] > threshold) ? (byte)0xff : 0;
               break;
            case DataArray.FIELD_DATA_INT:
               int[] iData = inData.getIData();
               for (int m = 0; m < ndata; m++)
                  bData[m] = (iData[m] > threshold) ? (byte)0xff : 0;
               break;
            case DataArray.FIELD_DATA_FLOAT:
               float[] fData = inData.getFData();
               for (int m = 0; m < ndata; m++)
                  bData[m] = (fData[m] > threshold) ? (byte)0xff : 0;
               break;
            case DataArray.FIELD_DATA_DOUBLE:
               double[] dData = inData.getDData();
               for (int m = 0; m < ndata; m++)
                  bData[m] = (dData[m] > threshold) ? (byte)0xff : 0;
               break;
         }
      else
         switch (inData.getType())
         {
            case DataArray.FIELD_DATA_BYTE:
            case DataArray.FIELD_DATA_SHORT:
               short[] sData = inData.getSData();
               for (int m = 0; m < ndata; m++)
                  bData[m] = (sData[m] < threshold) ? (byte)0xff : 0;
               break;
            case DataArray.FIELD_DATA_INT:
               int[] iData = inData.getIData();
               for (int m = 0; m < ndata; m++)
                  bData[m] = (iData[m] < threshold) ? (byte)0xff : 0;
               break;
            case DataArray.FIELD_DATA_FLOAT:
               float[] fData = inData.getFData();
               for (int m = 0; m < ndata; m++)
                  bData[m] = (fData[m] < threshold) ? (byte)0xff : 0;
               break;
            case DataArray.FIELD_DATA_DOUBLE:
               double[] dData = inData.getDData();
               for (int m = 0; m < ndata; m++)
                  bData[m] = (dData[m] < threshold) ? (byte)0xff : 0;
               break;
         }      if (mask != null)
         for (int m = 0; m < mask.length; m++)
            if (!mask[m])
               bData[m] = 0;
      Thread[] workThreads = new Thread[nThreads];
      for (int iThread = 0; iThread < nThreads; iThread++)
      {
         workThreads[iThread] = new Thread(new ComputeThresholdBoundary3D(iThread));
         workThreads[iThread].start();
      }
      for (int i = 0; i < workThreads.length; i++)
         try
         {
            workThreads[i].join();
         } catch (Exception e)
         {
         }
      // bdata is now binary map of inside skeletonized area (255) and outside or masked (0)
      ExtendMargins.fillOut3DMargins(boundaryDistance, dims, (byte)0);
      // outd is now initialized by: 0 if outside or on 1 voxel wide margin, -1 if on the boundary, MAXVAL if inside
      // nodeQueue is initialized as the list of indices of boundary voxels

   }

   @Override
   protected void clearOutDMargins()
   {
      int k = dims[0] - 1;
      for (int i2 = 0; i2 < dims[2]; i2++)
         for (int i1 = 0; i1 < dims[1]; i1++)
            boundaryDistance[(i2 * dims[1] + i1) * dims[0]] =
                    boundaryDistance[(i2 * dims[1] + i1) * dims[0] + k] = 0;
      k = dims[0] * (dims[1] - 1);
      for (int i2 = 0; i2 < dims[2]; i2++)
         for (int i0 = 0; i0 < dims[0]; i0++)
            boundaryDistance[i2 * dims[1] * dims[0] + i0] =
                    boundaryDistance[i2 * dims[1] * dims[0] + i0 + k] = 0;
      k = dims[0] * dims[1] * (dims[2] - 1);
      for (int i1 = 0; i1 < dims[1]; i1++)
         for (int i0 = 0; i0 < dims[0]; i0++)
            boundaryDistance[dims[0] * i1 + i0] =
                    boundaryDistance[dims[0] * i1 + i0 + k] = 0;
   }


}
