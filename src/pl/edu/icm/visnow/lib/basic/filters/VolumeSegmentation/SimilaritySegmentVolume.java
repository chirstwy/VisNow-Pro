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

package pl.edu.icm.visnow.lib.basic.filters.VolumeSegmentation;

import java.util.ArrayList;
import java.util.Stack;
import javax.swing.ProgressMonitor;
import pl.edu.icm.visnow.datasets.RegularField;

/**
 *
 * @author  Krzysztof S. Nowinski, University of Warsaw, ICM
 */
public class SimilaritySegmentVolume extends SegmentVolume
{

   protected ArrayList<Integer> indices = null;
   protected byte[][] data = null;
   protected float[] weights = null;
/*
 * mask[i] = 1 for background,
 * mask[i] = 0 for voxels with unknown status
 * mask[i] = k>1 for k-throws segmented set
 */
   protected short tollerance = 300;
   protected Stack<int[]>[] queue = null;
   protected Stack<int[]> free = new Stack<int[]>();
   protected boolean inside;
   protected int[][] framePoints = null;
   protected int segIndex = 2;
   /**
    * Holds value of property backgroundThreshold.
    */
   /** Creates a new instance of SegmentVolume
    * @param inField 
    */
   public SimilaritySegmentVolume(RegularField inField, RegularField distField, RegularField outField)
   {
      dims = inField.getDims();
      off = inField.getPartNeighbOffsets();
      ndata = inField.getNNodes();
      this.inField = inField;
      outd = distField.getData(0).getSData();
      mask = outField.getData(0).getBData();
   }
   
   @Override
   public void computeDistance(ArrayList<int[]> selectedPoints, boolean[] allowed)
   {
      if (tollerance < 0 || allowed == null || selectedPoints.isEmpty())
         return;
      boolean isSomethingAllowed = false;
      for (int i = 0; i < allowed.length; i++)
         if (allowed[i])
         {
            isSomethingAllowed = true;
            break;
         }
      if (!isSomethingAllowed)
         return;
      this.selectedPoints = selectedPoints;
      this.allowed = allowed;

      data = new byte[indices.size()][];
      for (int i = 0; i < indices.size(); i++)
            data[i] = inField.getData(indices.get(i)).getBData();
      
      status = 0;
      new Thread(new ComputeSimilarity()).start();
   }
   
   private class ComputeSimilarity implements Runnable 
   {
    @SuppressWarnings("unchecked")
        @Override
      public synchronized void run()
      {
         int i, j, k, n;
         short cd, d;
         int[] p;
         int[] qSegment;
         int[] qSeg = null;
//fast implementation of a priority queue as table of stacks        
         ProgressMonitor monitor = new ProgressMonitor(null, "Computing similarity field...", " ", 0, 100);
         monitor.setMillisToDecideToPopup(100);
         queue = new Stack[tollerance];
         monitor.setMillisToPopup(100);
         for (i = 0; i < tollerance; i++)
            queue[i] = new Stack<int[]>();
         for (i = 0; i < ndata; i++)
            outd[i] = tollerance;
//set outd to 0 on boundary 
         extendMargins((short)0);
         qSegment = new int[SEGLEN]; 
//initializing queue to selected points and setting seed values at selected points        
         for (i = 0; i < selectedPoints.size(); i++)
         {
            p = selectedPoints.get(i);
            k = (dims[1] * p[2] + p[1]) * dims[0] + p[0];
            outd[k] = 0;
            if (i%SEGLEN == 0)
            {
               qSegment = new int[SEGLEN];
               queue[0].push(qSegment);
               for (j = 0; j < SEGLEN; j++)
                  qSegment[j] = -1;
            }
            qSegment[i%SEGLEN] = k;
         }
         for (i = 0; i < tollerance; i++)
         {
            monitor.setProgress((i * 100) / (tollerance - 1));
            while (!queue[i].empty())
            {
               qSegment = queue[i].pop();
               for (k = 0; k < qSegment.length && qSegment[k] != -1; k++)
               {
                  n = qSegment[k];
                  if (!allowed[mask[n]])
                     continue;
                  cd = outd[n];
                  
                  for (j = 0; j < 18; j++)
                  {
                     int of = off[j];
                     if (!allowed[mask[n + of]] || (0xFF & data[0][n+of]) < low || (0xFF & data[0][n+of]) > up)
                        continue;
                     d = outd[n + of];
                     if (d < cd)
                        continue;
                     float dd = 0;
                     for (int id = 0; id < data.length; id ++)
                     {
                        if (weights[id] == 0)
                           continue;
                        int ll = (0xFF & data[id][n+of]) - (0xFF & data[id][n]);
                        dd += weights[id] * ll * ll;
                     }
                     int l = cd + (int)dd;
                     if (l < d)
                     {
                        outd[n + of] = (short) l;
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
                              {
                                 qSeg = new int[SEGLEN];
                              } else
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
         status = SIMILARITY_COMPUTED;
         fireStateChanged();
      }
   }

   @Override
   public void setIndices(ArrayList<Integer> indices)
   {
      this.indices = indices;
   }

   @Override
   public void setWeights(float[] weights)
   {
      this.weights = weights;
   }

   @Override
   public void setTollerance(short tollerance)
   {
      this.tollerance = tollerance;
   }
   
   public void setComponent(int component)
   {
      
   }

}
