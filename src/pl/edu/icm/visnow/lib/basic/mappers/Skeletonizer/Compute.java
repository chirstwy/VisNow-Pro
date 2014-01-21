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
import javax.swing.event.ChangeListener;
import pl.edu.icm.visnow.datasets.RegularField;
import pl.edu.icm.visnow.datasets.dataarrays.DataArray;
import pl.edu.icm.visnow.gui.events.FloatValueModificationEvent;
import pl.edu.icm.visnow.gui.events.FloatValueModificationListener;
import pl.edu.icm.visnow.lib.utils.FastIntQueue;
import pl.edu.icm.visnow.lib.utils.events.MessagedChangeEvent;
import pl.edu.icm.visnow.lib.utils.numeric.SGapproximation.PolynomialApproximation;
import pl.edu.icm.visnow.lib.utils.numeric.ShortScalarHeapSort;

/**
 ** @author Krzysztof S. Nowinski, University of Warsaw ICM
 */

abstract public class Compute
{

   protected static final int MAXPOLY   = 50000;
   protected static final int BIGVAL    = Short.MAX_VALUE;
   protected static final int[][] dists =
                                        {{4, 3, 4,
                                          3,    3,
                                          4, 3, 4},
                                         {5, 4, 5, 4, 3, 4, 5, 4, 5,
                                          4, 3, 4, 3,    3, 4, 3, 4,
                                          5, 4, 5, 4, 3, 4, 5, 4, 5}};
   protected int nNeighb                = 8;
   protected int[] dist                 = null;
   protected int[] off                  = null;
   
   protected RegularField inField       = null;
   protected Params params              = null;
   protected float threshold            = 25;
   protected boolean above              = true;
   protected int minLength              = 20;
   protected int[] dims                 = null;
   protected int ndata                  = 0;
   protected boolean[] mask             = null;
   protected DataArray inData           = null;
   
   protected byte[]   bData             = null;
   protected boolean[] effectiveMask    = null;
   protected short[] boundaryDistance   = null;
   protected int[] centres              = new int[MAXPOLY];
   protected int nCentrePoints          = 0;
   protected short[] radii              = new short[MAXPOLY];
   
   protected float[] skeletonDistance   = null;
   protected short[] centerDistance     = null;
   protected int kendpts                = 0;
   protected int[] endpts               = null;
   protected int[] startpts             = null;
   protected int[] parent               = null;
   protected int[][] nNodes             = null;
   
   protected int nNode                  = 0;
   protected float[][][] coords         = null;
   protected short[][][] radiiData      = null;
   protected float[] distData           = null;
   protected int nThreads               = pl.edu.icm.visnow.system.main.VisNow.availableProcessors();
   protected String message             = null;

   protected FastIntQueue nodeQueue     = new FastIntQueue();
   
   public Compute()
   {
   }

   abstract protected void clearOutDMargins();
   abstract protected void setBoundaryData(int cSet);
   abstract protected void setBoundaryData();
   abstract protected void findInnermostNodes();

   public final void setInField(RegularField inField)
   {
      this.inField = inField;
      dims = inField.getDims();
      dist = dists[dims.length - 2];
      nNeighb = dist.length;
      off = inField.getFullNeighbOffsets();
      ndata = 1;
      for (int i = 0; i < dims.length; i++)
         ndata *= dims[i];
      bData = new byte[ndata];
      boundaryDistance = new short[ndata];
      inData = inField.getData(params.getComponent());
      mask = inField.getMask();
   }

   protected void createEffectiveMask()
   {
      effectiveMask = new boolean[ndata];
      if (mask != null)
         System.arraycopy(mask, 0, effectiveMask, 0, ndata);
      for (int i = 0; i < ndata; i++)
         effectiveMask[i] = effectiveMask[i] && (boundaryDistance[i] > 0);
   }
   
   protected void updateSkeletonPart(int part)
   {
      createBoundaryDistanceMap();
      createEffectiveMask();
      findInnermostNodes();
      System.out.println("" + nCentrePoints +" candidates for centre points found");
      createCentreDistanceMaps();
      initParentArray();
      nNodes[0] = new int[kendpts];
      coords[0] = new float[kendpts][];
      int nlines = 0;
      short[] enddists = new short[endpts.length];
      for (int i = 0; i < enddists.length; i++)
         enddists[i] = centerDistance[endpts[i]];
      ShortScalarHeapSort hs = new ShortScalarHeapSort(enddists, endpts);
      hs.sort();
      int nStartPoints = 0;
      int lastpt = -1;
      for (int i = endpts.length - 1; i >= endpts.length - kendpts; i--)
      {
         if (endpts[i] != lastpt)
         {
            nStartPoints += 1;
            lastpt = endpts[i];
         }
      }
      System.out.println("" + nStartPoints +" candidates for line start points found");
      startpts = new int[nStartPoints];
      for (int i = endpts.length - 1, j = 0; i >= endpts.length - kendpts; i--)
      {
         if (endpts[i] != lastpt)
         {
            startpts[j] = lastpt = endpts[i];
            j += 1;
         }
      }
      for (int i = 0; i < startpts.length; i++)
      {
         int k = startpts[i];
         boolean inside = true;
         for (int j = 0; j < dims.length; j++)
         {
            int kj = k % dims[j];
            k /= dims[j];
            if (kj == 0 || kj == dims[j] - 1)
               inside = false;
         }
      }
      for (int i = 0; i < startpts.length; i++)
      {
         float[] startpt = new float[dims.length];
         int k = startpts[i];
         boolean inside = true;
         for (int j = 0; j < dims.length; j++)
         {
            startpt[j] = k % dims[j];
            k /= dims[j];
            if (startpt[j] == 0 || startpt[j] == dims[j] - 1)
               inside = false;
         }
         k = startpts[i];
         if (inside)
         {
            int k0 = -1;
            short cmin = centerDistance[k];
            for (int j = 0; j < nNeighb; j++)
            {
               int l = k + off[j];
               if (centerDistance[l] != 0 && centerDistance[l] < cmin)
               {
                  k0 = l;
                  cmin = centerDistance[l];
               }
            }
            if (k0 > -1)
            {
               for (int j = 0; j < dims.length; j++)
               {
                   startpt[j] = k0 % dims[j];
                   k0 /= dims[j];
               }
               float[] line = createLine(startpt);
               if (line == null)
                  continue;
               nNodes[part][nlines] = line.length / dims.length;
               coords[part][nlines] = line;
               nlines += 1;
               
            }
         }
      }
      System.out.println("" + nlines +" lines found");
   }

   public void updateSkeleton()
   {
      threshold = params.getThreshold();
      above = params.isAbove();
      nNode = 0;
      if (params.isSegmented())
      {
         int nSets = params.getNSets() - 2;
         nNodes = new int[nSets][];
         coords = new float[nSets][][];
         for (int nSet = 0; nSet < nSets; nSet++)
         {
            init();
            fireActivityChanged("finding boundary of the set "+nSet);
            setBoundaryData(nSet + 2);
            updateSkeletonPart(nSet);
         }
      }
      else
      {
         init();
         nNodes = new int[1][];
         coords = new float[1][][];
            fireActivityChanged("finding boundary of the set ");
         setBoundaryData();
         updateSkeletonPart(0);
      }
      createResults();
   }

   protected void init()
   {
      Arrays.fill(boundaryDistance, (short)0);
      nodeQueue.reset();
      centres = new int[MAXPOLY];
      radii = new short[MAXPOLY];
   }
   
      protected void createBoundaryDistanceMap()
   {
//   creation of the map of distances of th inside points from the boundary in 3-4-5 metric
//   outd[i]=0 if i outside segmented area
//   outd[i]>0 if i inside segmented area  and not in queue
//   outd[i]<0 if i outside segmented area and in queue
//   |outd[i]| is current val of distance from boundary +1
//   to avoid subtle cases distinction, 1-voxel wide margin is marked as "outside"

      fireActivityChanged("<html>creating map of <p>distance from boundary");

      for (int step = 0; step < ndata && !nodeQueue.isEmpty(); step++)
      {
         if (step % 100000 == 0)
            fireStatusChanged(.1f + (.01f * step) / ndata);
         int k = nodeQueue.get();
         if (boundaryDistance[k] != 0)
         {
            if (boundaryDistance[k] < 0)
               boundaryDistance[k] = (short) (-boundaryDistance[k]);
            else
               System.out.println("bad mark " + k);

            for (int j = 0; j < nNeighb; j++)
            {
               int k1 = k + off[j];
               if (boundaryDistance[k1] == 0)
                  continue;
               int i0 = boundaryDistance[k] + dist[j];
               if (Math.abs(boundaryDistance[k1]) > i0)
               {
                  if (boundaryDistance[k1] > 0)   // k1 not in queue
                     nodeQueue.insert(k1);
                  boundaryDistance[k1] = (short) (-i0);
               }
            }
         }
      }
   }

   protected void createCentreDistanceMaps()
   {
      int maxR = 0;
      for (int m = 0; m < nCentrePoints; m++)
         if (radii[m] > maxR)  maxR = radii[m];
      int[][] sCenters = new int[maxR + 1][];
      int[]   nCenters = new int[maxR + 1];
      for (int m = 0; m <= maxR; m++)
         nCenters[m] = 0;
      for (int m = 0; m < nCentrePoints; m++)
         nCenters[radii[m]] += 1;
      for (int m = 0; m <= maxR; m++)
      {
         sCenters[m] = new int[nCenters[m]];
         nCenters[m] = 0;
      }
      for (int m = 0; m < nCentrePoints; m++)
      {
         int n = radii[m];
         sCenters[n][nCenters[n]] = centres[m];
         nCenters[n] += 1;
      }
      centres = null; radii = null;
      fireActivityChanged("<html>finding innermost points <p>in connected components and <p>creating maps of distances from innermost point");
      centerDistance = new short[ndata];      //map of standard 3/4/5 distances from innermost points in connected components
      skeletonDistance = new float[ndata];    //map of 3/4/5 distances weighted by inverse distance from boundary from innermost points in connected components
      parent = new int[ndata];                //for each node inside skeletonized area its neighbor closest to the centre point
      
      for (int i = 0; i < ndata; i++)
      {
         if (boundaryDistance[i] == 0)
         {
            skeletonDistance[i] = 0;
            centerDistance[i] = 0;
            parent[i] = -BIGVAL;
         } else
         {
            skeletonDistance[i] = Float.MAX_VALUE;
            centerDistance[i] = Short.MAX_VALUE;
            parent[i] = -1;
         }
      }
            
      int nendpts = 1000;
      kendpts = 0;
      endpts = new int[nendpts];
      int lmax;
      for (int depth = maxR; depth >= 0; depth--)
      {
         fireStatusChanged(.1f + (.7f * (maxR - depth)) / maxR);
         for (int m = 0; m < nCenters[depth]; m++)
         {
            lmax = sCenters[depth][m];
            if (centerDistance[lmax] != Short.MAX_VALUE)
               continue;
            nodeQueue.reset();
            centerDistance[lmax] = -1;
            nodeQueue.insert(lmax);
            boolean isendpt = true;
            while (!nodeQueue.isEmpty())
            {
               isendpt = true;
               int k = nodeQueue.get();
               if (k < 0 || k >= ndata)
                  continue;
               if (centerDistance[k] < 0)
                  centerDistance[k] = (short)-centerDistance[k];
               else
                  System.out.println("bad mark " + k);
               for (int j = 0; j < nNeighb; j++)
               {
                  int k1 = k + off[j];
                  if (boundaryDistance[k1] == 0)
                     continue;
                  int newV = centerDistance[k] + dist[j];
                  int v = Math.abs(centerDistance[k1]);
                  if (v > newV)
                  {
                     if (centerDistance[k1] > 0)   // k1 not in queue
                        nodeQueue.insert(k1);
                     centerDistance[k1] = (short)-newV;   // k1 was already or is added to queue
                     v = newV;
                  }
                  if (v > centerDistance[k])
                     isendpt = false;
               }
               if (isendpt)
               {
                  if (kendpts >= nendpts - 1)
                  {
                     int[] tmp = new int[2 * nendpts];
                     System.arraycopy(endpts, 0, tmp, 0, kendpts);
                     nendpts *= 2;
                     endpts = tmp;
                  }
                  endpts[kendpts] = k;
                  kendpts += 1;
               }
            }
         }
      }
      int comp = 0;
      for (int depth = maxR; depth >= 0; depth--)
      {
         fireStatusChanged(.1f + (.7f * (maxR - depth)) / maxR);
         for (int m = 0; m < nCenters[depth]; m++)
         {
            lmax = sCenters[depth][m];
            if (skeletonDistance[lmax] != Float.MAX_VALUE)
               continue;
            comp += 1;
            nodeQueue.reset();
            skeletonDistance[lmax] = -1;
            nodeQueue.insert(lmax);
            while (!nodeQueue.isEmpty())
            {
               int k = nodeQueue.get();
               if (k < 0 || k >= ndata)
                  continue;
               if (skeletonDistance[k] < 0)
                  skeletonDistance[k] = -skeletonDistance[k];
               else
                  System.out.println("bad mark " + k);
               for (int j = 0; j < nNeighb; j++)
               {
                  int k1 = k + off[j];
                  if (boundaryDistance[k1] == 0)
                     continue;
                  float newV = skeletonDistance[k] + (float)dist[j] / (boundaryDistance[k] + boundaryDistance[k1]);
                  float v = Math.abs(skeletonDistance[k1]);
                  if (v > newV)
                  {
                     if (skeletonDistance[k1] > 0)   // k1 not in queue
                        nodeQueue.insert(k1);
                     skeletonDistance[k1] = -newV;   // k1 was already or is added to queue
                  }
               }
            }
         }
      }
      System.out.println(""+comp + " components found");
      fireActivityChanged(String.format("<html>Found %d outermost points in %d connected components<p>finding skeleton lines", kendpts, comp));
   }
   
   protected void initParentArray()
   {
      parent = new int[ndata];
      Arrays.fill(parent, -1);
   }
   

   protected float[] createLine(float[] startPoint)
   { 
      int nSpace = startPoint.length;
      int maxpts = 1000, kpts = 1;
      float[] tmppts = new float[nSpace * maxpts];
      float[] p = new float[nSpace];
      float[] v = new float[nSpace];
      float[] vo = {0, 0, 0};
      int[] ind = new int[nSpace];
      System.arraycopy(startPoint, 0, tmppts, 0, nSpace);
      System.arraycopy(startPoint, 0, p, 0, nSpace);
      boolean end = false;
      int step;
      for (step = 1; !end; step++)
      {
         float[] coeffs = PolynomialApproximation.coeffs(skeletonDistance, null, dims, p, 1, 1.f, 2);
//         float[] coeffs = PolynomialApproximation.coeffs(skeletonDistance, null, dims, p, 2, 1.f, 2);
         if (coeffs == null)
            break;
         System.arraycopy(coeffs, 1, v, 0, nSpace);
         float d = 0;
         for (int i = 0; i < nSpace; i++)
            d += v[i] * v[i];
         float norm = (float)Math.sqrt(d);
         if (norm == 0)
            break;
         d = 0;
         for (int i = 0; i < nSpace; i++)
         {
            v[i] /= norm;
            d += v[i] * vo[i];
         }
         if (d < 0)
            break;
         System.arraycopy(v, 0, vo, 0, nSpace);
         end = false;
         int node = 0;
         for (int i =  nSpace - 1; i >= 0; i--)
         {
            p[i] -= v[i];
            ind[i] = (int)(p[i] + .5);
            if (ind[i] < 0 || ind[i] >= dims[i] - 1)
               end = true;
            node = node * dims[i] + ind[i];
         }
         if (end)
            break;
         if (parent[node] > 0) 
            end = true;
         if (step >= maxpts - 1)
         {
            float[] tmp = new float[2 * nSpace * maxpts];
            System.arraycopy(tmppts, 0, tmp, 0, tmppts.length);
            maxpts *= 2;
            tmppts = tmp;
         }
         System.arraycopy(p, 0, tmppts, nSpace * step, nSpace);
      }
      if (step < 2)
         return null;
      return updateLine(step, tmppts);
   }

   protected float[] updateLine(int kpts, float[] tmppts)
   {
      int nSpace = dims.length;
      int[] ind = new int[nSpace];
      float[] pts = new float[nSpace * kpts];
      float[] p = new float[3];
      System.arraycopy(tmppts, 0, pts, 0, pts.length);
      for (int i = 0; i < kpts; i++)
      {
         System.arraycopy(tmppts, nSpace * i, p, 0, nSpace);
         for (int j = 0; j < nSpace; j++)
            ind[j] = (int)(p[j] + .5);
         int l0 = Math.max(ind[0] - 2, 0);
         int u0 = Math.min(ind[0] + 3, dims[0]);
         int l1 = Math.max(ind[1] - 2, 0);
         int u1 = Math.min(ind[1] + 3, dims[1]);
         if (nSpace == 2)
         {
            for (int j0 = l0; j0 < u0; j0++)
               for (int j1 = l1; j1 < u1; j1++)
                  if ((j0 - p[0]) * (j0 - p[0]) + (j1 - p[1]) * (j1 - p[1]) < 8)
                     parent[j1 * dims[0] + j0] = nNode;
         }
         else
         {
            int l2 = Math.max(ind[2] - 2, 0);
            int u2 = Math.min(ind[2] + 3, dims[2]);
            for (int j0 = l0; j0 < u0; j0++)
               for (int j1 = l1; j1 < u1; j1++)
                  for (int j2 = l2; j2 < u2; j2++)
                     if ((j0 - p[0]) * (j0 - p[0]) + (j1 - p[1]) * (j1 - p[1]) + (j2 - p[2]) * (j2 - p[2]) < 8)
                        parent[(j2 * dims[1] + j1) * dims[0] + j0] = nNode;
         }
         nNode += 1;
      }
      return pts;
   }
   
   protected void createResults()
   {
      int nSpace = dims.length;
      float[] x;
      radiiData = new short[coords.length][][];
      for (int iSet = 0; iSet < coords.length; iSet++)
      {
         radiiData[iSet] = new short[coords[iSet].length][];
         for (int iLine = 0; iLine < coords[iSet].length; iLine++)
         {
            float[] cCoords = coords[iSet][iLine];
            if (cCoords == null)
               continue;
            int n = cCoords.length / nSpace;
            radiiData[iSet][iLine] = new short[n];
            float[] tCoords = cCoords;
            if (nSpace == 2)
               tCoords = new float[3 * n];
            for (int i = 0; i < n; i++)
            {
               if (nSpace == 2)
               {
                  x = inField.getGridCoords(cCoords[2 * i], cCoords[2 * i + 1]);
                  radiiData[iSet][iLine][i] = inField.getInterpolatedData(boundaryDistance, cCoords[2 * i], cCoords[2 * i + 1], 0)[0];
               }
               else
               {
                  x = inField.getGridCoords(cCoords[3 * i], cCoords[3 * i + 1], cCoords[3 * i + 2]);
                  radiiData[iSet][iLine][i] = inField.getInterpolatedData(boundaryDistance, cCoords[3 * i], cCoords[3 * i + 1], cCoords[3 * i + 2])[0];
               }
               System.arraycopy(x, 0, tCoords, 3 * i, x.length);
            }
            coords[iSet][iLine] = tCoords;
         }
      }
   }
   
   
   protected transient FloatValueModificationListener statusListener = null;

   public void addFloatValueModificationListener(FloatValueModificationListener listener)
   {
      if (statusListener == null)
         this.statusListener = listener;
      else
         System.out.println(""+this+": only one status listener can be added");
   }

   public void clearFloatValueModificationListener()
   {
      statusListener = null;
   }

   protected void fireStatusChanged(float status)
   {
       FloatValueModificationEvent e = new FloatValueModificationEvent(this, status, true);
       if (statusListener != null)
          statusListener.floatValueChanged(e);
   }

   protected transient ChangeListener activityListener = null;

   public void addActivityListener(ChangeListener listener)
   {
      if (activityListener == null)
         activityListener = listener;
      else
         System.out.println(""+this+": only one activity listener can be added");
   }

   public void clearActivityListener()
   {
      activityListener = null;
   }

   protected void fireActivityChanged(String msg)
   {
      message = msg;
      MessagedChangeEvent e = new MessagedChangeEvent(this, msg);
       if (activityListener != null)
          activityListener.stateChanged(e);
   }

   /**
    * Get the value of radiiData
    *
    * @return the value of radiiData
    */
   public short[][][] getRadiiData()
   {
      return radiiData;
   }

   /**
    * Get the value of coords
    *
    * @return the value of coords
    */
   public float[][][] getCoords()
   {
      return coords;
   }

   public float[] getDistData()
   {
      return distData;
   }

   /**
    * Get the value of nNodes
    *
    * @return the value of nNodes
    */
   public int[][] getNNodes()
   {
      return nNodes;
   }

   public short[] getOutd()
   {
      return boundaryDistance;
   }

   public float[] getOutd1()
   {
      return skeletonDistance;
   }

   public int[] getEndpts()
   {
      return endpts;
   }

   /**
    * Get the value of message
    *
    * @return the value of message
    */
   public String getMessage()
   {
      return message;
   }
}
