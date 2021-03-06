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

package pl.edu.icm.visnow.lib.basic.filters.LocalOperations;

import pl.edu.icm.visnow.datasets.dataarrays.DataArray;
import pl.edu.icm.visnow.datasets.RegularField;

/**
 *
 * @author  Krzysztof S. Nowinski, University of Warsaw, ICM
 */
public class LocalOps2D implements LocalOps
{
   private RegularField inField = null;
   private int radius = 1;
   private int[] dims = null;
   private boolean[][] inside = null;

   public RegularField compute(RegularField inField, Params params)
   {
      if (inField == null)
         return null;
      this.inField = inField;
      radius = params.getRadius();
      dims = inField.getDims();
      for (int i = 0; i < dims.length; i++)
         if (dims[i] < 2 * radius + 2)
            return inField;
      inside = new boolean[2 * radius + 1][2 * radius + 1];
      for (int i = -radius; i < radius; i++)
         for (int j = -radius; j < radius; j++)
               inside[i + radius][j + radius] = (i * i + j * j <= radius * radius);
      String sequence = params.getSequence();
      int nData, n;
      nData = dims[0] * dims[1];

      byte[] outBData = null;
      short[] outSData = null;
      int[] outIData = null;
      float[] outFData = null;
      double[] outDData = null;
      byte[] tmpBData = null;
      short[] tmpSData = null;
      int[] tmpIData = null;
      float[] tmpFData = null;
      double[] tmpDData = null;
      int nThreads = params.getNThreads();
      Thread[] workThreads = new Thread[nThreads];

      RegularField outField = new RegularField(dims, inField.getExtents());
      outField.setAffine(inField.getAffine());
      for (n = 0; n < inField.getNData(); n++)
      {
         DataArray dataArr = inField.getData(n);
         if (dataArr.getVeclen() != 1)
            continue;
         switch (dataArr.getType())
         {
            case DataArray.FIELD_DATA_BYTE:
               byte[] inBData = dataArr.getBData();
               outBData = new byte[nData];
               tmpBData = new byte[nData];
               for (int i = 0; i < nData; i++)
                  outBData[i] = inBData[i];
               break;
            case DataArray.FIELD_DATA_SHORT:
               short[] inSData = dataArr.getSData();
               outSData = new short[nData];
               tmpSData = new short[nData];
               for (int i = 0; i < nData; i++)
                  outSData[i] = inSData[i];
               break;
            case DataArray.FIELD_DATA_INT:
               int[] inIData = dataArr.getIData();
               outIData = new int[nData];
               tmpIData = new int[nData];
               for (int i = 0; i < nData; i++)
                  outIData[i] = inIData[i];
               break;
            case DataArray.FIELD_DATA_FLOAT:
               float[] inFData = dataArr.getFData();
               outFData = new float[nData];
               tmpFData = new float[nData];
               for (int i = 0; i < nData; i++)
                  outFData[i] = inFData[i];
               break;
            case DataArray.FIELD_DATA_DOUBLE:
               double[] inDData = dataArr.getDData();
               outDData = new double[nData];
               tmpDData = new double[nData];
               for (int i = 0; i < nData; i++)
                  outDData[i] = inDData[i];
               break;
         }
         for (int s = 0; s < sequence.length(); s++)
            switch (dataArr.getType())
            {
               case DataArray.FIELD_DATA_BYTE:
                  for (int i = 0; i < outBData.length; i++)
                     tmpBData[i] = outBData[i];
                  for (int i = 0; i < workThreads.length; i++)
                  {
                     workThreads[i] = new Thread(new FilterByteArray(nThreads, i, outBData, tmpBData,
                             sequence.charAt(s) == 'd' || sequence.charAt(s) == 'D'));
                     workThreads[i].start();
                  }
                  for (int i = 0; i < workThreads.length; i++)
                     try
                     {
                        workThreads[i].join();
                     } catch (Exception e)
                     {
                     }
                  break;
               case DataArray.FIELD_DATA_SHORT:
                  for (int i = 0; i < outSData.length; i++)
                     tmpSData[i] = outSData[i];
                  for (int i = 0; i < workThreads.length; i++)
                  {
                     workThreads[i] = new Thread(new FilterShortArray(nThreads, i, outSData, tmpSData,
                             sequence.charAt(s) == 'd' || sequence.charAt(s) == 'D'));
                     workThreads[i].start();
                  }
                  for (int i = 0; i < workThreads.length; i++)
                     try
                     {
                        workThreads[i].join();
                     } catch (Exception e)
                     {
                     }
                  break;
               case DataArray.FIELD_DATA_INT:
                  for (int i = 0; i < outIData.length; i++)
                     tmpIData[i] = outIData[i];
                  for (int i = 0; i < workThreads.length; i++)
                  {
                     workThreads[i] = new Thread(new FilterIntArray(nThreads, i, outIData, tmpIData,
                             sequence.charAt(s) == 'd' || sequence.charAt(s) == 'D'));
                     workThreads[i].start();
                  }
                  for (int i = 0; i < workThreads.length; i++)
                     try
                     {
                        workThreads[i].join();
                     } catch (Exception e)
                     {
                     }
                  break;
               case DataArray.FIELD_DATA_FLOAT:
                  for (int i = 0; i < outFData.length; i++)
                     tmpFData[i] = outFData[i];
                  for (int i = 0; i < workThreads.length; i++)
                  {
                     workThreads[i] = new Thread(new FilterFloatArray(nThreads, i, outFData, tmpFData,
                             sequence.charAt(s) == 'd' || sequence.charAt(s) == 'D'));
                     workThreads[i].start();
                  }
                  for (int i = 0; i < workThreads.length; i++)
                     try
                     {
                        workThreads[i].join();
                     } catch (Exception e)
                     {
                     }
                  break;
               case DataArray.FIELD_DATA_DOUBLE:
                  for (int i = 0; i < outDData.length; i++)
                     tmpDData[i] = outDData[i];
                  for (int i = 0; i < workThreads.length; i++)
                  {
                     workThreads[i] = new Thread(new FilterDoubleArray(nThreads, i, outDData, tmpDData,
                             sequence.charAt(s) == 'd' || sequence.charAt(s) == 'D'));
                     workThreads[i].start();
                  }
                  for (int i = 0; i < workThreads.length; i++)
                     try
                     {
                        workThreads[i].join();
                     } catch (Exception e)
                     {
                     }
                  break;
            }
         switch (dataArr.getType())
         {
            case DataArray.FIELD_DATA_BYTE:
               outField.addData(DataArray.create(outBData, 1, dataArr.getName() + sequence, dataArr.getUnit(), dataArr.getUserData()));
               break;
            case DataArray.FIELD_DATA_SHORT:
               outField.addData(DataArray.create(outSData, 1, dataArr.getName() + sequence, dataArr.getUnit(), dataArr.getUserData()));
               break;
            case DataArray.FIELD_DATA_INT:
               outField.addData(DataArray.create(outIData, 1, dataArr.getName() + sequence, dataArr.getUnit(), dataArr.getUserData()));
               break;
            case DataArray.FIELD_DATA_FLOAT:
               outField.addData(DataArray.create(outFData, 1, dataArr.getName() + sequence, dataArr.getUnit(), dataArr.getUserData()));
               break;
            case DataArray.FIELD_DATA_DOUBLE:
               outField.addData(DataArray.create(outDData, 1, dataArr.getName() + sequence, dataArr.getUnit(), dataArr.getUserData()));
               break;
         }
      }
      if (outField.getNData() > 0)
         return outField;
      else
         return null;
   }

   class FilterByteArray implements Runnable
   {
      int from;
      int to;
      boolean dilate;
      byte[] outData;
      byte[] tmpData;

      public FilterByteArray(int nThreads, int iThread, byte[] outData, byte[] tmpData, boolean dilate)
      {
         from = (dims[1] * iThread) / nThreads;
         to = (dims[1] * (iThread + 1)) / nThreads;
         this.tmpData = tmpData;
         this.outData = outData;
         this.dilate  = dilate;
      }

      public void run()
      {
         for (int j = from, m = from * dims[0]; j < to; j++)
         {
            int j0 = j - radius;
            if (j0 < 0)       j0 = 0;
            int j1 = j + radius + 1;
            if (j1 > dims[1]) j1 = dims[1];
            for (int i = 0; i < dims[0]; i++, m++)
            {
               int p = 0xff & tmpData[m];
               int i0 = i-radius;
               if (i0 < 0)       i0 = 0;
               int i1 = i + radius + 1;
               if (i1 > dims[0]) i1 = dims[0];
                  for (int jj = j0, js = j0 - j + radius; jj < j1; jj++, js++)
                     for (int ii = i0, is = i0 - i + radius, l = jj * dims[0] + i0; ii < i1; ii++, is++, l++)
                        if (inside[is][js])
                        {
                           int v = 0xff & tmpData[l];
                           if (v > p && dilate)
                              p = v;
                           if (v < p && !dilate)
                              p = v;
                        }
               outData[m] = (byte) (0xff & p);
            }
         }
      }
   }

   class FilterShortArray implements Runnable
   {

      int from;
      int to;
      boolean dilate;
      short[] outData;
      short[] tmpData;

      public FilterShortArray(int nThreads, int iThread, short[] outData, short[] tmpData, boolean dilate)
      {
         from = (dims[1] * iThread) / nThreads;
         to = (dims[1] * (iThread + 1)) / nThreads;
         this.outData = outData;
         this.tmpData = tmpData;
         this.dilate  = dilate;
      }

      public void run()
      {
         for (int j = from, m = from * dims[0]; j < to; j++)
         {
            int j0 = j - radius;
            if (j0 < 0)       j0 = 0;
            int j1 = j + radius + 1;
            if (j1 > dims[1]) j1 = dims[1];
            for (int i = 0; i < dims[0]; i++, m++)
            {
               short p = tmpData[m];
               int i0 = i-radius;
               if (i0 < 0)       i0 = 0;
               int i1 = i + radius + 1;
               if (i1 > dims[0]) i1 = dims[0];
                  for (int jj = j0, js = j0 - j + radius; jj < j1; jj++, js++)
                     for (int ii = i0, is = i0 - i + radius, l = jj * dims[0] + i0; ii < i1; ii++, is++, l++)
                        if (inside[is][js])
                        {
                           short v =tmpData[l];
                           if (v > p && dilate)
                              p = v;
                           if (v < p && !dilate)
                              p = v;
                        }
               outData[m] = p;
            }
         }
      }
   }

   class FilterIntArray implements Runnable
   {

      int from;
      int to;
      boolean dilate;
      int[] outData;
      int[] tmpData;

      public FilterIntArray(int nThreads, int iThread, int[] outData, int[] tmpData, boolean dilate)
      {
         from = (dims[1] * iThread) / nThreads;
         to = (dims[1] * (iThread + 1)) / nThreads;
         this.outData = outData;
         this.tmpData = tmpData;
         this.dilate  = dilate;
      }

      public void run()
      {
         for (int j = from, m = from * dims[0]; j < to; j++)
         {
            int j0 = j - radius;
            if (j0 < 0)       j0 = 0;
            int j1 = j + radius + 1;
            if (j1 > dims[1]) j1 = dims[1];
            for (int i = 0; i < dims[0]; i++, m++)
            {
               int p = tmpData[m];
               int i0 = i-radius;
               if (i0 < 0)       i0 = 0;
               int i1 = i + radius + 1;
               if (i1 > dims[0]) i1 = dims[0];
                  for (int jj = j0, js = j0 - j + radius; jj < j1; jj++, js++)
                     for (int ii = i0, is = i0 - i + radius, l = jj * dims[0] + i0; ii < i1; ii++, is++, l++)
                        if (inside[is][js])
                        {
                           int v =tmpData[l];
                           if (v > p && dilate)
                              p = v;
                           if (v < p && !dilate)
                              p = v;
                        }
               outData[m] = p;
            }
         }
      }
   }

   class FilterFloatArray implements Runnable
   {

      int from;
      int to;
      boolean dilate;
      float[] outData;
      float[] tmpData;

      public FilterFloatArray(int nThreads, int iThread, float[] outData, float[] tmpData, boolean dilate)
      {
         from = (dims[1] * iThread) / nThreads;
         to = (dims[1] * (iThread + 1)) / nThreads;
         this.outData = outData;
         this.tmpData = tmpData;
         this.dilate  = dilate;
      }

      public void run()
      {
         for (int j = from, m = from * dims[0]; j < to; j++)
         {
            int j0 = j - radius;
            if (j0 < 0)       j0 = 0;
            int j1 = j + radius + 1;
            if (j1 > dims[1]) j1 = dims[1];
            for (int i = 0; i < dims[0]; i++, m++)
            {
               float p = tmpData[m];
               int i0 = i-radius;
               if (i0 < 0)       i0 = 0;
               int i1 = i + radius + 1;
               if (i1 > dims[0]) i1 = dims[0];
                  for (int jj = j0, js = j0 - j + radius; jj < j1; jj++, js++)
                     for (int ii = i0, is = i0 - i + radius, l = jj * dims[0] + i0; ii < i1; ii++, is++, l++)
                        if (inside[is][js])
                        {
                           float v = tmpData[l];
                           if (v > p && dilate)
                              p = v;
                           if (v < p && !dilate)
                              p = v;
                        }
               outData[m] = p;
            }
         }
      }
   }

   class FilterDoubleArray implements Runnable
   {

      int from;
      int to;
      boolean dilate;
      double[] outData;
      double[] tmpData;

      public FilterDoubleArray(int nThreads, int iThread, double[] outData, double[] tmpData, boolean dilate)
      {
         from = (dims[1] * iThread) / nThreads;
         to = (dims[1] * (iThread + 1)) / nThreads;
         this.outData = outData;
         this.tmpData = tmpData;
         this.dilate  = dilate;
      }

      public void run()
      {
         for (int j = from, m =from * dims[0]; j < to; j++)
         {
            int j0 = j - radius;
            if (j0 < 0)       j0 = 0;
            int j1 = j + radius + 1;
            if (j1 > dims[1]) j1 = dims[1];
            for (int i = 0; i < dims[0]; i++, m++)
            {
               double p = tmpData[m];
               int i0 = i-radius;
               if (i0 < 0)       i0 = 0;
               int i1 = i + radius + 1;
               if (i1 > dims[0]) i1 = dims[0];
                  for (int jj = j0, js = j0 - j + radius; jj < j1; jj++, js++)
                     for (int ii = i0, is = i0 - i + radius, l = jj * dims[0] + i0; ii < i1; ii++, is++, l++)
                        if (inside[is][js])
                        {
                           double v = tmpData[l];
                           if (v > p && dilate)
                              p = v;
                           if (v < p && !dilate)
                              p = v;
                        }
               outData[m] = p;
            }
         }
      }
   }
}
