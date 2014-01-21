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

/*
 *Isosurface.java
 *
 *Created on August 14, 2004, 2:06 PM
 */
import java.util.Vector;
import javax.swing.JProgressBar;
import pl.edu.icm.visnow.datasets.dataarrays.DataArray;
import pl.edu.icm.visnow.datasets.RegularField;
import pl.edu.icm.visnow.gui.events.FloatValueModificationEvent;
import pl.edu.icm.visnow.gui.events.FloatValueModificationListener;
import pl.edu.icm.visnow.lib.utils.isosurface.AddEdges;
import pl.edu.icm.visnow.lib.utils.isosurface.IsoEdges;
import pl.edu.icm.visnow.lib.utils.isosurface.IsoTriangles;

/**
 *
 *@author  Krzysztof S. Nowinski, University of Warsaw, ICM
 */
public class SurfacesCompute
{
   protected static final int CHUNK_SIZE = 1000;
   protected static final int[][][][] ie = IsoTriangles.ie;
   protected static int[][][][] ig       = IsoEdges.ig;
   protected static int[][][][][] iaddg  = {AddEdges.ig0, AddEdges.ig1, AddEdges.ig2};
   protected RegularField inFld          = null;
   protected Params params               = null;
   protected JProgressBar progressBar    = null;
   protected int[] inDims                = null;
   protected float[][] inPtsOrig         = null;
   protected int nSets                   = 0;
   protected int[] nNodes                = null;
   protected float[][] coords            = null;
   protected float[][] normals           = null;
   protected int[] nTriangles            = null;
   protected int[][] triangles           = null;
   protected byte[] data                 = null;
   protected int[] low                   = null;
   protected int[] up                    = null;
   protected int[] d                     = null;
   protected int nThreads                = 1;
   
   /** Creates a new instance of Isosurface */
   public SurfacesCompute()
   {
   }
   
   public SurfacesCompute(RegularField in, JProgressBar progressBar, Params params)
   {
      if (in == null || params == null)
         return;
      this.inFld       = in;
      this.params      = params;
      this.progressBar = progressBar;
      inDims           = in.getDims();
      inPtsOrig        = in.getExtents();
      DataArray da     = in.getData(0);
      data             = da.getBData();
      nSets            = da.getUserData().length - 1;
      for (int i = 0; i < da.getUserData().length; i++)
         System.out.println(da.getUserData()[i]);
      nNodes           = new int[nSets];
      nTriangles       = new int[nSets];
      coords           = new float[nSets][];
      normals          = new float[nSets][];
      triangles        = new int[nSets][];
   }
   
   public void updateSurfaces()
   {
      low      = params.getLow(); 
      up       = params.getUp(); 
      d        = params.getDownsize();
      nThreads = params.getNThreads();
      makeIsosurfaces();
      params.setDimensionsChanged(false);
   }

   public void setNThreads(int nThreads)
   {
      this.nThreads = nThreads;
   }

   public float[] getCoords(int n)
   {
      return coords[n];
   }

   public float[] getNormals(int n)
   {
      return normals[n];
   }

   public int[] getTriangles(int n)
   {
      return triangles[n];
   }

   class Compute implements Runnable
   {
      int iThread;

      public Compute(int iThread)
      {
         this.iThread  = iThread;
      }

      public void run()
      {
         int start = 1;
         int nRuns = (nSets + nThreads - start - 1)/ nThreads;
         for (int nSet = iThread+start; nSet < nSets; nSet += nThreads)
         {
            Vector<float[]>     ptVect  = new Vector<float[]>();
            Vector<float[]>     nvVect  = new Vector<float[]>();
            Vector<int[]>       trVect  = new Vector<int[]>();
            int[] clow = new int[] {low[0], low[1], low[2]};
            int[] cup  = new int[] {up[0],  up[1],  up[2]};

            float[]   pt = new float[3*CHUNK_SIZE];
            float[]   nv = new float[3*CHUNK_SIZE];
            int[]     tr = new int[3*CHUNK_SIZE];

            int ll,ip,it,jt,ix, iy, sign, p=0, t=0, e=0; 
            float u,v,x,y,z;
            int k0,k1,k2;
            
            boolean in = false, out = false;
        l2: for (int i = clow[2]; i < cup[2]; i+=d[2])
            {
               for (int j = clow[1]; j < cup[1]; j+=d[1])
                  for (int k = clow[0], l = (i*inDims[1]+j)*inDims[0]+clow[0]; k < cup[0]; k+=d[0], l+=d[0])
                  {
                     if (data[l] == nSet) in = true;
                     if (data[l] != nSet) out = true;
                     if (in && out)
                     {
                        clow[2] = i-d[2];
                        if (clow[2] < low[2]) clow[2] = low[2];
                        break l2;
                     }
                  }
            }
            in = out = false;
        u2: for (int i = cup[2]-1; i >=clow[2]; i-=d[2])
            {
               for (int j = clow[1]; j < cup[1]; j+=d[1])
                  for (int k = clow[0], l = (i*inDims[1]+j)*inDims[0]+clow[0]; k < cup[0]; k+=d[0], l+=d[0])
                  {
                     if (data[l] == nSet) in = true;
                     if (data[l] != nSet) out = true;
                     if (in && out)
                     {
                        cup[2] = i+2*d[2];
                        if (cup[2] >= up[2]) cup[2] = up[2];
                        break u2;
                     }
                  }
            }

            in = out = false;
        l1: for (int j = clow[1]; j < cup[1]; j+=d[1])
            {
               for (int i = clow[2]; i < cup[2]; i+=d[2])
                  for (int k = clow[0], l = (i*inDims[1]+j)*inDims[0]+clow[0]; k < cup[0]; k+=d[0], l+=d[0])
                  {
                     if (data[l] == nSet) in = true;
                     if (data[l] != nSet) out = true;
                     if (in && out)
                     {
                        clow[1] = j-d[1];
                        if (clow[1] < low[1]) clow[1] = low[1];
                        break l1;
                     }
                  }
            }
            in = out = false;
        u1: for (int j = cup[1]-1; j >= clow[1]; j-=d[1])
            {
               for (int i = clow[2]; i < cup[2]; i+=d[2])
                  for (int k = clow[0], l = (i*inDims[1]+j)*inDims[0]+clow[0]; k < cup[0]; k+=d[0], l+=d[0])
                  {
                     if (data[l] == nSet) in = true;
                     if (data[l] != nSet) out = true;
                     if (in && out)
                     {
                        cup[1] = j+2*d[1];
                        if (cup[1] >= up[1]) cup[1] = up[1];
                        break u1;
                     }
                  }
            }

            in = out = false;
        l0: for (int k = clow[0]; k < cup[0]; k+=d[0])
            {
               for (int i = clow[2]; i < cup[2]; i+=d[2])
                  for (int j = clow[1], l = (i*inDims[1]+j)*inDims[0]+k; j < cup[1]; j+=d[1], l+=inDims[0]*d[1])
                  {
                     if (data[l] == nSet) in = true;
                     if (data[l] != nSet) out = true;
                     if (in && out)
                     {
                        clow[0] = k-d[0];
                        if (clow[0] < low[0]) clow[0] = low[0];
                        break l0;
                     }
                  }
            }
            in = out = false;
        u0: for (int k = cup[0]-1; k >=clow[0]; k-=d[0])
            {
               for (int i = clow[2]; i < cup[2]; i+=d[2])
                  for (int j = clow[1], l = (i*inDims[1]+j)*inDims[0]+k; j < cup[1]; j+=d[1], l+=inDims[0]*d[1])
                  {
                     if (data[l] == nSet) in = true;
                     if (data[l] != nSet) out = true;
                     if (in && out)
                     {
                        cup[0] = k+2*d[0];
                        if (cup[0] >= up[0]) cup[0] = up[0];
                        break u0;
                     }
                  }
            }
        
            float[] inDataOrig = new float[inDims[0]*inDims[1]];
            int[] dind = {1,1,1};
            int[] dims = new int[3];
            for (int i=0;i<3;i++)
            {
               dims[i] = (cup[i]-clow[i]+d[i]-1)/d[i];
               dind[i] *= d[i];
            }
            ix=1; iy=dims[0];

            int[][][] lPts = new int[dims[1]-1][dims[0]-1][12];
            float[][] inData = new float[2][dims[1]*dims[0]];
            if (progressBar!=null)
               progressBar.setMaximum(dims[2]-2);
            for (ll=0; ll<dims[2]-1; ll++)
            {
               if (iThread == 0)
                  fireStatusChanged((nSet*1.f/nThreads+.4f*ll)/((dims[2]-1)*nRuns));
               int l = clow[2]+ll*d[2];
               if (ll==0)
               {
                  for (int i = 0, k = l * inDims[0]*inDims[1]; i < inDims[0]*inDims[1]; i++, k++)
                     if (data[k]==nSet) inDataOrig[i] = -1;
                     else               inDataOrig[i] = 1;
                  for (int j=0, k=0; j<dims[1]; j++)
                     for (int i=0, m=(j*d[1]+clow[1])*inDims[0]+clow[0];
                          i<dims[0];
                          i++,k++,m+=d[0])
                        inData[0][k]=inDataOrig[m];
               }
               else
                  System.arraycopy(inData[1], 0, inData[0], 0, dims[1]*dims[0]);
               for (int i = 0, k = (l+d[2]) * inDims[0]*inDims[1]; i < inDims[0]*inDims[1]; i++, k++)
                  if (data[k]==nSet) inDataOrig[i] = -1;
                  else               inDataOrig[i] = 1;
               for (int j=0, k=0; j<dims[1]; j++)
                  for (int i=0, m=(j*d[1]+clow[1])*inDims[0]+clow[0];
                       i<dims[0];
                       i++,k++,m+=d[0])
                     inData[1][k]=inDataOrig[m];
               // getting slices of original data, cropping and resizing them
               z = clow[2]+ll*dind[2];
               if (ll==0)
               {
                  for (int j=0; j<dims[1]; j++)         //finding points in x lines
                  {
                     y = clow[1]+j*dind[1];
                     for (int i=0; i<dims[0]-1; i++)
                     {
                        ip = j*dims[0]+i;
                        x=clow[0]+i*dind[0];
                        u=inData[0][ip];     if (u==0.f) u-=.01f;
                        v=inData[0][ip+1];   if (v==0.f) v-=.01f;
                        if (u*v<0)
                        {
                           int k=p%CHUNK_SIZE;
                           pt[3*k]  =x+u/(u-v)*dind[0];
                           pt[3*k+1]=y;
                           pt[3*k+2]=z;
                           nv[3*k]=(v-u)/dind[0];
                           if (j>0 && j<dims[1]-1)
                              nv[3*k+1]=.5f*(inData[0][ip+iy]-inData[0][ip-iy])/dind[1];
                           else
                           {
                              if (j>0)
                                 nv[3*k+1]=(inData[0][ip]-inData[0][ip-iy])/dind[1];
                              else
                                 nv[3*k+1]=(inData[0][ip+iy]-inData[0][ip])/dind[1];
                           }
                           nv[3*k+2]=(inData[1][ip]-inData[0][ip])/dind[2];
                           if (j<dims[1]-1)
                              lPts[j][i][0] = p;
                           if (j>0)
                              lPts[j-1][i][1] = p;
                           p+=1;k+=1;
                           if (k==CHUNK_SIZE)
                           {
                              ptVect.add(pt);
                              pt = new float[3*CHUNK_SIZE];
                              nvVect.add(nv);
                              nv = new float[3*CHUNK_SIZE];
                           }
                        }
                     }
                  }
                  for (int j=0; j<dims[1]-1; j++)         //finding points in y lines
                  {
                     y = clow[1]+j*dind[1];
                     for (int i=0; i<dims[0]; i++)
                     {
                        ip = j*dims[0]+i;
                        x=clow[0]+i*dind[0];
                        u=inData[0][ip];      if (u==0.f) u-=1.e-6f;
                        v=inData[0][ip+iy];   if (v==0.f) v-=1.e-6f;
                        if (u*v<0)
                        {
                           int k=p%CHUNK_SIZE;
                           pt[3*k]  =x;
                           pt[3*k+1]=y+u/(u-v)*dind[1];
                           pt[3*k+2]=z;
                           if (i>0&&i<dims[0]-1)
                              nv[3*k]=.5f*(inData[0][ip+ix]-inData[0][ip-ix])/dind[0];
                           else
                           {
                              if (i>0)
                                 nv[3*k]=(inData[0][ip]-inData[0][ip-ix])/dind[0];
                              else
                                 nv[3*k]=(inData[0][ip+ix]-inData[0][ip])/dind[0];
                           }
                           nv[3*k+1]=(v-u)/dind[1];
                           nv[3*k+2]=(inData[1][ip]-inData[0][ip])/dind[2];
                           if (i<dims[0]-1)
                              lPts[j][i][4] = p;
                           if (i>0)
                              lPts[j][i-1][5] = p;
                           p+=1;k+=1;
                           if (k==CHUNK_SIZE)
                           {
                              ptVect.add(pt);
                              pt = new float[3*CHUNK_SIZE];
                              nvVect.add(nv);
                              nv = new float[3*CHUNK_SIZE];
                           }
                        }
                     }
                  }
               }
               else
               {
                  for (int j=0;j<dims[1]-1;j++)
                     for (int i=0;i<dims[0]-1;i++)
                     {
                     lPts[j][i][0] = lPts[j][i][2];
                     lPts[j][i][1] = lPts[j][i][3];
                     lPts[j][i][4] = lPts[j][i][6];
                     lPts[j][i][5] = lPts[j][i][7];
                     }
               }
               for (int j=0;j<dims[1];j++)         //finding points in x lines
               {
                  y = clow[1]+j*dind[1];
                  for (int i=0;i<dims[0]-1;i++)
                  {
                     ip = j*dims[0]+i;
                     x=clow[0]+i*dind[0];
                     u=inData[1][ip];     if (u==0.f) u-=1.e-6f;
                     v=inData[1][ip+1];   if (v==0.f) v-=1.e-6f;
                     if (u*v<0)
                     {
                        int k=p%CHUNK_SIZE;
                        pt[3*k]  =x+u/(u-v)*dind[0];
                        pt[3*k+1]=y;
                        pt[3*k+2]=z+dind[2];
                        nv[3*k]=(v-u)/dind[0];
                        if (j>0&&j<dims[1]-1)
                           nv[3*k+1]=.5f*(inData[1][ip+iy]-inData[1][ip-iy])/dind[1];
                        else
                        {
                           if (j>0)
                              nv[3*k+1]=(inData[1][ip]-inData[1][ip-iy])/dind[1];
                           else
                              nv[3*k+1]=(inData[1][ip+iy]-inData[1][ip])/dind[1];
                        }
                        nv[3*k+2]=(inData[1][ip]-inData[0][ip])/dind[2];
                        if (j<dims[1]-1)
                           lPts[j][i][2] = p;
                        if (j>0)
                           lPts[j-1][i][3] = p;
                        p+=1;k+=1;
                        if (k==CHUNK_SIZE)
                        {
                           ptVect.add(pt);
                           pt = new float[3*CHUNK_SIZE];
                           nvVect.add(nv);
                           nv = new float[3*CHUNK_SIZE];
                        }
                     }
                  }
               }
               for (int j=0;j<dims[1]-1;j++)         //finding points in y lines
               {
                  y = clow[1]+j*dind[1];
                  for (int i=0;i<dims[0];i++)
                  {
                     ip = j*dims[0]+i;
                     x=clow[0]+i*dind[0];
                     u=inData[1][ip];      if (u==0.f) u-=1.e-6f;
                     v=inData[1][ip+iy];   if (v==0.f) v-=1.e-6f;
                     if (u*v<0)
                     {
                        int k=p%CHUNK_SIZE;
                        pt[3*k]  =x;
                        pt[3*k+1]=y+u/(u-v)*dind[1];
                        pt[3*k+2]=z+dind[2];
                        if (i>0&&i<dims[0]-1)
                           nv[3*k]=.5f*(inData[1][ip+ix]-inData[1][ip-ix])/dind[0];
                        else
                        {
                           if (i>0)
                              nv[3*k]=(inData[1][ip]-inData[1][ip-ix])/dind[0];
                           else
                              nv[3*k]=(inData[1][ip+ix]-inData[1][ip])/dind[0];
                        }
                        nv[3*k+1]=(v-u)/dind[1];
                        nv[3*k+2]=(inData[1][ip]-inData[0][ip])/dind[2];
                        if (i<dims[0]-1)
                           lPts[j][i][6] = p;
                        if (i>0)
                           lPts[j][i-1][7] = p;
                        p+=1;k+=1;
                        if (k==CHUNK_SIZE)
                        {
                           ptVect.add(pt);
                           pt = new float[3*CHUNK_SIZE];
                           nvVect.add(nv);
                           nv = new float[3*CHUNK_SIZE];
                        }
                     }
                  }
               }
               for (int j=0;j<dims[1];j++)         //finding points in z lines
               {
                  y = clow[1]+j*dind[1];
                  for (int i=0;i<dims[0];i++)
                  {
                     ip = j*dims[0]+i;
                     x=clow[0]+i*dind[0];
                     u=inData[0][ip];   if (u==0.f) u-=1.e-6f;
                     v=inData[1][ip];   if (v==0.f) v-=1.e-6f;
                     if (u*v<0)
                     {
                        int k=p%CHUNK_SIZE;
                        pt[3*k]  =x;
                        pt[3*k+1]=y;
                        pt[3*k+2]=z+u/(u-v)*dind[2];
                        if (i>0&&i<dims[0]-1)
                           nv[3*k]=.5f*(inData[0][ip+ix]-inData[0][ip-ix])/dind[0];
                        else
                        {
                           if (i>0)
                              nv[3*k]=(inData[0][ip]-inData[0][ip-ix])/dind[0];
                           else
                              nv[3*k]=(inData[0][ip+ix]-inData[0][ip])/dind[0];
                        }
                        if (j>0&&j<dims[1]-1)
                           nv[3*k+1]=.5f*(inData[0][ip+iy]-inData[0][ip-iy])/dind[1];
                        else
                        {
                           if (j>0)
                              nv[3*k+1]=(inData[0][ip]-inData[0][ip-iy])/dind[1];
                           else
                              nv[3*k+1]=(inData[0][ip+iy]-inData[0][ip])/dind[1];
                        }
                        nv[3*k+2]=(v-u)/dind[2];
                        if (j<dims[1]-1 && i<dims[0]-1)
                           lPts[j][i][8] = p;
                        if (j>0 && i<dims[0]-1)
                           lPts[j-1][i][10] = p;
                        if (j<dims[1]-1 && i>0)
                           lPts[j][i-1][9] = p;
                        if (j>0 && i>0)
                           lPts[j-1][i-1][11] = p;
                        p+=1;k+=1;
                        if (k==CHUNK_SIZE)
                        {
                           ptVect.add(pt);
                           pt = new float[3*CHUNK_SIZE];
                           nvVect.add(nv);
                           nv = new float[3*CHUNK_SIZE];
                        }
                     }
                  }
               }
               for (int j=0;j<dims[1]-1;j++)         //finding triangles
                  for (int i=0;i<dims[0]-1;i++)
                  {
                  sign = 0;
                  int k = j*dims[0]+i;
                  if (inData[0][k      ] > 0)
                     sign|=1;
                  if (inData[0][k   +ix] > 0)
                     sign|=1<<1;
                  if (inData[0][k+iy   ] > 0)
                     sign|=1<<2;
                  if (inData[0][k+iy+ix] > 0)
                     sign|=1<<3;
                  if (inData[1][k      ] > 0)
                     sign|=1<<4;
                  if (inData[1][k   +ix] > 0)
                     sign|=1<<5;
                  if (inData[1][k+iy   ] > 0)
                     sign|=1<<6;
                  if (inData[1][k+iy+ix] > 0)
                     sign|=1<<7;
                  int m = t%CHUNK_SIZE;
                  int[][] trIn = ie[(i+j+l)%2][sign];
                  for (it=0;it<trIn.length;it++)
                  {
                     if (trIn[it][0]<0)
                        break;
                     for (jt=0;jt<3;jt++)
                        tr[3*m+jt]=lPts[j][i][trIn[it][jt]];
                     t+=1;m+=1;
                     if (m==CHUNK_SIZE)
                     {
                        trVect.add(tr);
                        tr = new int[3*CHUNK_SIZE];
                        m=0;
                     }
                  }
                  m = e%CHUNK_SIZE;
               }
            }
            ptVect.add(pt);
            nvVect.add(nv);
            trVect.add(tr);
            nNodes[nSet] = p;
            nTriangles[nSet] = t;
//            System.out.println("set "+nSet+": "+p+" nodes, "+t+" triangles computed by thread "+iThread);
            if (p < 3)
               continue;
            float[] crds  = new float[3*p];
            float[] norms = new float[3*p];
            float[] a = new float[3];
            float[] b = new float[3];
            float[] c = new float[3];

         pointloop:
            for (int i = 0, l = 0; i < ptVect.size() && l < 3*p; i++)
            {
               pt = ptVect.get(i);
               nv = nvVect.get(i);
               for (int j = 0; j < CHUNK_SIZE; j++)
               {
                  u = (float) Math.sqrt(nv[3*j  ]*nv[3*j  ]+
                                        nv[3*j+1]*nv[3*j+1]+
                                        nv[3*j+2]*nv[3*j+2]);
                  for (ip = 0; ip < 3 && l < 3*p; ip++, l++)
                  {
                     crds[l]  = pt[3*j+ip];
                     norms[l] = nv[3*j+ip]/u;
                  }
               }
            }
            int[] cells = new int[3*t];
         triangleloop:
            for (int i = 0, l = 0; i < trVect.size(); i++)
            {
               tr = trVect.get(i);
               for (int j = 0; j < tr.length && l < 3 * t; j++, l++)
                  cells[l] = tr[j];
            }
            for (int i = 0; i < t; i++)
            {
               k0 = cells[3 * i];
               k1 = cells[3 * i + 1];
               k2 = cells[3 * i + 2];
               for (int j = 0; j < 3; j++)
               {
                  a[j] = crds[3 * k1 + j] - crds[3 * k0 + j];
                  b[j] = crds[3 * k2 + j] - crds[3 * k0 + j];
               }
               c[0] = a[1] * b[2] - a[2] * b[1];
               c[1] = a[2] * b[0] - a[0] * b[2];
               c[2] = a[0] * b[1] - a[1] * b[0];
               if (c[0] * norms[3 * k0] + c[1] * norms[3 * k0 + 1] + c[2] * norms[3 * k0 + 2] < 0)
               {
                  cells[3 * i + 1] = k2;
                  cells[3 * i + 2] = k1;
               }
            }
            for (int j = 0; j < p; j++)
            {
               float[] cc = inFld.getGridCoords(crds[3 * j], crds[3 * j + 1], crds[3 * j + 2]);
               System.arraycopy(cc, 0, crds, 3 * j, 3);
            }
            coords[nSet] = crds;
            normals[nSet] = norms;
            triangles[nSet] = cells;
         }
      }
   }


   private void makeIsosurfaces()
   {
      Thread[] workThreads = new Thread[nThreads];
      for (int i = 0; i < nThreads; i++)
      {
         workThreads[i] = new Thread(new Compute(i));
         workThreads[i].start();
      }
      for (int i = 0; i < workThreads.length; i++)
         try
         {
            workThreads[i].join();
         } catch (Exception e)
         {
         }
   }

   public float[][] getCoords()
   {
      return coords;
   }

   public int[] getNNodes()
   {
      return nNodes;
   }

   public int[] getNTriangles()
   {
      return nTriangles;
   }

   public float[][] getNormals()
   {
      return normals;
   }

   public int[][] getTriangles()
   {
      return triangles;
   }

   private transient FloatValueModificationListener statusListener = null;

   public void clearFloatValueModificationListener()
   {
      statusListener = null;
   }

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
