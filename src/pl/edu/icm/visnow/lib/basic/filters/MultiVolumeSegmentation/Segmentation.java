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

package pl.edu.icm.visnow.lib.basic.filters.MultiVolumeSegmentation;

import java.util.Stack;
import pl.edu.icm.visnow.datasets.CellSet;
import pl.edu.icm.visnow.datasets.dataarrays.DataArray;
import pl.edu.icm.visnow.datasets.IrregularField;
import pl.edu.icm.visnow.datasets.RegularField;
import pl.edu.icm.visnow.gui.events.FloatValueModificationEvent;
import pl.edu.icm.visnow.gui.events.FloatValueModificationListener;

/**
 *
 * @author Krzysztof S. Nowinski
 *   <p>   University of Warsaw, ICM
 */
public class Segmentation
{

   protected static final int SEGLEN = 1000;
   protected Params params;
   protected RegularField inField;
   protected IrregularField inPts;
   protected int[] dims;
   protected int nData;
   protected int[] offsets;
   protected byte[] inAreas;
   protected byte[] areas;
   protected float thr;
   protected byte[] data;
   protected Stack<int[]>[] queue = null;
   protected Stack<int[]> free = new Stack<int[]>();
   protected short[] outd = null;

   public Segmentation(Params params, RegularField inField, IrregularField inPts)
   {
      this.params = params;
      this.inField = inField;
      this.inPts = inPts;
      dims = inField.getDims();
      nData = inField.getNNodes();
      inAreas = new byte[nData];
      areas = new byte[nData];
      data = new byte[nData];
      thr = params.getThreshold();
      float[][] af = inField.getAffine();
      float[][] ia = inField.getInvAffine();
      DataArray da = inField.getData(params.getComponent());
      float minv = da.getMinv();
      float dv = 255 / (da.getMaxv() - minv);
      switch (da.getType())
      {
      case DataArray.FIELD_DATA_BYTE:
         data = da.getBData();
         for (int i = 0; i < nData; i++)
            inAreas[i] = (0xff & data[i]) < thr ? (byte) -128 : (byte) 127;
         break;
      case DataArray.FIELD_DATA_SHORT:
         short[] sd = da.getSData();
         for (int i = 0; i < nData; i++)
         {
            inAreas[i] = sd[i] < thr ? (byte) -128 : (byte) 127;
            data[i] = (byte) ((int) (dv * (sd[i] - minv)) & 0xff);
         }
         break;
      case DataArray.FIELD_DATA_INT:
         int[] id = da.getIData();
         for (int i = 0; i < nData; i++)
         {
            inAreas[i] = id[i] < thr ? (byte) -128 : (byte) 127;
            data[i] = (byte) ((int) (dv * (id[i] - minv)) & 0xff);
         }
         break;
      case DataArray.FIELD_DATA_FLOAT:
         float[] fd = da.getFData();
         for (int i = 0; i < nData; i++)
         {
            inAreas[i] = fd[i] < thr ? (byte) -128 : (byte) 127;
            data[i] = (byte) ((int) (dv * (fd[i] - minv)) & 0xff);
         }
         break;
      case DataArray.FIELD_DATA_DOUBLE:
         double[] dd = da.getDData();
         for (int i = 0; i < nData; i++)
         {
            inAreas[i] = dd[i] < thr ? (byte) -128 : (byte) 127;
            data[i] = (byte) ((int) (dv * (dd[i] - minv)) & 0xff);
         }
         break;
      }

      float[] startCoords = inPts.getCoords();
      float[] x = new float[3];
      float[] y = new float[3];
      float[] z = new float[3];
      int[] ix = new int[3];
      int[] off = inField.getPartNeighbOffsets();
      for (int iset = 0; iset < inPts.getNCellSets(); iset++)
      {
         CellSet cs = inPts.getCellSet(iset);
         for (int iarr = 0; iarr < cs.getCellArrays().length; iarr++)
            if (cs.getCellArray(iarr) != null)
            {
               int[] nodes = cs.getCellArray(iarr).getNodes();
               for (int n = 0; n < nodes.length; n++)
               {
                  int l = nodes[n];
                  System.arraycopy(startCoords, 3 * l, x, 0, 3);
                  for (int j = 0; j < 3; j++)
                     y[j] = x[j] - af[3][j];
                  for (int j = 0; j < 3; j++)
                  {
                     z[j] = 0;
                     for (int k = 0; k < z.length; k++)
                        z[k] += ia[j][k] * y[k];
                  }
                  ix = new int[]
                          {
                             (int) z[0], (int) z[1], (int) z[2]
                          };
                  if (ix[0] <= 0 || ix[0] >= dims[0] - 1
                          || ix[1] <= 0 || ix[1] >= dims[1] - 1
                          || ix[2] <= 0 || ix[2] >= dims[2] - 1)
                     continue;
                  int m = (dims[1] * ix[2] + ix[1]) * dims[0] + ix[0];
                  if (inAreas[m] == 127)
                     inAreas[m] = (byte)(iset + 1);
                  for (int i = 0; i < off.length; i++)
                     if (inAreas[m + off[i]] == 127)
                        inAreas[m + off[i]] = (byte)(iset + 1);
               }
            }
      }
   }

   protected void extendMargins()
   {
      if (inAreas == null)
         return;
      int i, j, k, l, m;
      k = dims[0] * dims[1] * (dims[2] - 1);
      l = dims[0] * dims[1];
      m = dims[0] * dims[1] * dims[2];
      for (i = 0; i < l; i++)
         inAreas[i] = inAreas[i + k] = -128;
      k = dims[0] * (dims[1] - 1);
      for (i = 0; i < m; i += l)
         for (j = 0; j < dims[0]; j++)
            inAreas[i + j] = inAreas[i + j + k] = -128;
      k = dims[0] - 1;
      for (i = 0; i < m; i += l)
         for (j = 0; j < l; j += dims[0])
            inAreas[i + j] = inAreas[i + j + k] = -128;
   }

   @SuppressWarnings("unchecked")
   public void compute()
   {
      short cd, d;
      outd = new short[nData];
      int[] qSegment;
      int[] qSeg = null;
      short tollerance = Short.MAX_VALUE;
      int[] off = inField.getPartNeighbOffsets();
      queue = new Stack[tollerance];
      for (int i = 0; i < tollerance; i++)
         queue[i] = new Stack<int[]>();
      for (int i = 0; i < nData; i++)
         outd[i] = tollerance;
//set outd to 0 on boundary 
      extendMargins();
      qSegment = new int[SEGLEN];
//initializing queue to selected points and setting seed values at selected points        
      for (int k = 0, i = 0; k < inAreas.length; k++)
      {
         if (inAreas[k] < 0 || inAreas[k] == 127)
            continue;
         outd[k] = 0;
         if (i % SEGLEN == 0)
         {
            qSegment = new int[SEGLEN];
            queue[0].push(qSegment);
            for (int j = 0; j < SEGLEN; j++)
               qSegment[j] = -1;
         }
         qSegment[i % SEGLEN] = k;
         i += 1;
      }
      for (int i = 0; i < tollerance; i++)
      {
         fireStatusChanged(i/(tollerance - 1.f));
         while (!queue[i].empty())
         {
            qSegment = queue[i].pop();
            for (int k = 0; k < qSegment.length && qSegment[k] != -1; k++)
            {
               int n = qSegment[k];
               cd = outd[n];
               for (int j = 0; j < off.length; j++)
               {
                  int of = off[j];
                  if (inAreas[n + of] < 0)
                     continue;
                  d = outd[n + of];
                  if (d < cd)
                     continue;
                  int dl = (0xFF & data[n + of]) - (0xFF & data[n]);
                  int l = cd + dl * dl;
                  if (l < d)
                  {
                     outd[n + of] = (short) l;
                     inAreas[n + of] = inAreas[n];
                     if (l == i && qSegment[SEGLEN - 1] == -1)
                        qSeg = qSegment;
                     else
                     {
                        qSeg = null;
                        if (!queue[l].empty())
                           qSeg = queue[l].peek();
                        if (queue[l].empty() || qSeg[SEGLEN - 1] != -1)
                        {
                           if (free.empty())
                              qSeg = new int[SEGLEN];
                           else
                              qSeg = free.pop();
                           for (int ll = 0; ll < SEGLEN; ll++)
                              qSeg[ll] = -1;
                           queue[l].push(qSeg);
                        }
                     }
                     for (int ll = 0; ll < SEGLEN; ll++)
                        if (qSeg[ll] == -1)
                        {
                           qSeg[ll] = n + of;
                           break;
                        }
                  }
               }
            }
            free.push(qSegment);
         }
      }
      free.clear();
      for (int i = 0; i < areas.length; i++)
         if (inAreas[i] == -128)
            areas[i] = 0;
         else if (inAreas[i] == 127)
            areas[i] = 1;
         else
            areas[i] = (byte)(inAreas[i] + 1);
   }

   public byte[] getBd()
   {
      return data;
   }

   public byte[] getAreas()
   {
      return areas;
   }

   private transient FloatValueModificationListener statusListener = null;

   public void addFloatValueModificationListener(FloatValueModificationListener listener)
   {
      if (statusListener == null)
         this.statusListener = listener;
      else
         System.out.println(""+this+": only one status listener can be added");
   }

   private void fireStatusChanged(float status)
   {
       FloatValueModificationEvent e = new FloatValueModificationEvent(this, status, true);
       if (statusListener != null)
          statusListener.floatValueChanged(e);
   }

}
