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

package pl.edu.icm.visnow.lib.utils.numeric.SGapproximation;

import java.util.Arrays;
import pl.edu.icm.visnow.lib.utils.numeric.NumericalMethods;

/**
 *
 * @author Krzysztof S. Nowinski, University of Warsaw ICM
 */


public class PolynomialApproximation3D
{

   private PolynomialApproximation3D()
   {
   }
   
/**
 * Computes local weighted approximation of values on a given regular grid by a polynomial of requested degree
 * @param v array of values to be approximated
 * @param mask false if the corresponding value is invalid
 * @param dims dimensions of this array
 * @param x0 the center of the area of approximation
 * @param deg degree of approximating polynomial
 * @param weight weight of a point y will be exp(-w * |x - y|^2)
 * @param r points with |x - y| < r will be used
 * @return coeffficients of approximating polynomial a00 + a10 x0 + a01 x1 + a20 x0^2 + a11 x0 x1 + a02 x1^2 ...
 */
   public static float[] coeffs(byte[] v, boolean[] mask, int[] dims, float[] x0, int deg, float weight, int r)
   {
      if (dims == null || dims.length != 3)
         return null;
      if (v == null || v.length != dims[0] * dims[1] * dims[2] || mask != null && mask.length != dims[0] * dims[1] * dims[2])
         return null;
      return computeApproximatingPolynomial(getData(dims, x0, deg, weight, v, mask), deg);
   }

   public static float[] coeffs(short[] v, boolean[] mask, int[] dims, float[] x0, int deg, float weight, int r)
   {
      if (dims == null || dims.length != 3)
         return null;
      if (v == null || v.length != dims[0] * dims[1] * dims[2] || mask != null && mask.length != dims[0] * dims[1] * dims[2])
         return null;
      return computeApproximatingPolynomial(getData(dims, x0, deg, weight, v, mask), deg);
   }

   public static float[] coeffs(int[] v, boolean[] mask, int[] dims, float[] x0, int deg, float weight, int r)
   {
      if (dims == null || dims.length != 3)
         return null;
      if (v == null || v.length != dims[0] * dims[1] * dims[2] || mask != null && mask.length != dims[0] * dims[1] * dims[2])
         return null;
      return computeApproximatingPolynomial(getData(dims, x0, deg, weight, v, mask), deg);
   }

   public static float[] coeffs(float[] v, boolean[] mask, int[] dims, float[] x0, int deg, float weight, int r)
   {
      if (dims == null || dims.length != 3)
         return null;
      if (v == null || v.length != dims[0] * dims[1] * dims[2] || mask != null && mask.length != dims[0] * dims[1] * dims[2])
         return null;
      return computeApproximatingPolynomial(getData(dims, x0, deg, weight, v, mask), deg);
   }

   public static float[] coeffs(double[] v, boolean[] mask, int[] dims, float[] x0, int deg, float weight, int r)
   {
      if (dims == null || dims.length != 3)
         return null;
      if (v == null || v.length != dims[0] * dims[1] * dims[2] || mask != null && mask.length != dims[0] * dims[1] * dims[2])
         return null;
      return computeApproximatingPolynomial(getData(dims, x0, deg, weight, v, mask), deg);
   }

/**
 * Gets sufficient number of valid nodes in the neighbourhood to compute approximating polynomial of a given degree
 * @param dims dimensions of input data array (length must be 2)
 * @param x0   coordinates of thew center of approximation area
 * @param deg  degree of approximating polynomial
 * @param weight coefficient of gaussian weight function
 * @param v values of data array to be approximated
 * @param mask   optional mask array (mask[i] indicates if the i-th point is valid)
 * @return array of 3 arrays of floats : selected node coordinates, values at selected nodes, weights of selected nodes
 * for use in the compouteApproximatingPolynomial method
 */
   public static float[][] getData(int[] dims, float[] x0, int deg, float weight, byte[] v, boolean[] mask)
   {
      float[][] out = new float[3][];
      int nCoeffs = ((deg + 1) * (deg + 2) * (deg + 3)) / 6;
      int r0 = (int)Math.pow(nCoeffs / 8, 1./3);
      int rmax = r0;
      int n = 0;
      for (int r = r0; r < r0 + 10 && n < nCoeffs; r++)
      {
         rmax = r;
         for (int i = -r; i <= r; i++)
         {
            int i0 = (int)(x0[2] - i + .5);
            if (i0 < 0 || i0 >= dims[2])
               continue;
            for (int j = -r; j <= r; j++)
            {
               int j0 = (int)(x0[1] - j + .5);
               if (j0 < 0 || j0 >= dims[1])
                  continue;
               for (int k = -r; k <= r; k++)
               {
                  int k0 = (int)(x0[0] - k + .5);
                  if (k0 < 0 || k0 >= dims[0])
                     continue;
                  int s = (i0 * dims[1] + j0) * dims[0] + k0;
                  if (mask == null || mask[s])
                     n += 1;
               }
            }
         }
      }
      out[0] = new float[3 * n];
      out[1] = new float[n];
      out[2] = new float[n];

      n = 0;
      for (int i = -rmax; i <= rmax; i++)
      {
         int i0 = (int)(x0[2] - i + .5);
         if (i0 < 0 || i0 >= dims[2])
            continue;
         for (int j = -rmax; j <= rmax; j++)
         {
            int j0 = (int)(x0[1] - j + .5);
            if (j0 < 0 || j0 >= dims[1])
               continue;
            for (int k = -rmax; k <= rmax; k++)
            {
               int k0 = (int)(x0[0] - k + .5);
               if (k0 < 0 || k0 >= dims[0])
                  continue;
               int s = (i0 * dims[1] + j0) * dims[0] + k0;
               if (mask == null || mask[s])
               {
                  out[0][3 * n] = k0 - x0[0];
                  out[0][3 * n + 1] = j0 - x0[1];
                  out[0][3 * n + 2] = i0 - x0[2];
                  out[1][n] = 0xff & v[s];
                  out[2][n] = (float)Math.exp(-weight * ((i0 - x0[2]) * (i0 - x0[2]) + (j0 - x0[1]) * (j0 - x0[1]) + (k0 - x0[0]) * (k0 - x0[0])));
                  n += 1;
               }
            }
         }
      }
      return out;
   }

   public static float[][] getData(int[] dims, float[] x0, int deg, float weight, 
                                   short[] v, boolean[] mask)
   {
      float[][] out = new float[3][];
      int nCoeffs = ((deg + 1) * (deg + 2) * (deg + 3)) / 6;
      int r0 = (int)Math.pow(nCoeffs / 8, 1./3);
      int rmax = r0;
      int n = 0;
      for (int r = r0; r < r0 + 10 && n < nCoeffs; r++)
      {
         rmax = r;
         for (int i = -r; i <= r; i++)
         {
            int i0 = (int)(x0[2] - i + .5);
            if (i0 < 0 || i0 >= dims[2])
               continue;
            for (int j = -r; j <= r; j++)
            {
               int j0 = (int)(x0[1] - j + .5);
               if (j0 < 0 || j0 >= dims[1])
                  continue;
               for (int k = -r; k <= r; k++)
               {
                  int k0 = (int)(x0[0] - k + .5);
                  if (k0 < 0 || k0 >= dims[0])
                     continue;
                  int s = (i0 * dims[1] + j0) * dims[0] + k0;
                  if (mask == null || mask[s])
                     n += 1;
               }
            }
         }
      }
      out[0] = new float[3 * n];
      out[1] = new float[n];
      out[2] = new float[n];

      n = 0;
      for (int i = -rmax; i <= rmax; i++)
      {
         int i0 = (int)(x0[2] - i + .5);
         if (i0 < 0 || i0 >= dims[2])
            continue;
         for (int j = -rmax; j <= rmax; j++)
         {
            int j0 = (int)(x0[1] - j + .5);
            if (j0 < 0 || j0 >= dims[1])
               continue;
            for (int k = -rmax; k <= rmax; k++)
            {
               int k0 = (int)(x0[0] - k + .5);
               if (k0 < 0 || k0 >= dims[0])
                  continue;
               int s = (i0 * dims[1] + j0) * dims[0] + k0;
               if (mask == null || mask[s])
               {
                  out[0][3 * n] = k0 - x0[0];
                  out[0][3 * n + 1] = j0 - x0[1];
                  out[0][3 * n + 2] = i0 - x0[2];
                  out[1][n] = v[s];
                  out[2][n] = (float)Math.exp(-weight * ((i0 - x0[2]) * (i0 - x0[2]) + (j0 - x0[1]) * (j0 - x0[1]) + (k0 - x0[0]) * (k0 - x0[0])));
                  n += 1;
               }
            }
         }
      }
      return out;
   }

   public static float[][] getData(int[] dims, float[] x0, int deg, float weight, int[] v, boolean[] mask)
   {
      float[][] out = new float[3][];
      int nCoeffs = ((deg + 1) * (deg + 2) * (deg + 3)) / 6;
      int r0 = (int)Math.pow(nCoeffs / 8, 1./3);
      int rmax = r0;
      int n = 0;
      for (int r = r0; r < r0 + 10 && n < nCoeffs; r++)
      {
         rmax = r;
         for (int i = -r; i <= r; i++)
         {
            int i0 = (int)(x0[2] - i + .5);
            if (i0 < 0 || i0 >= dims[2])
               continue;
            for (int j = -r; j <= r; j++)
            {
               int j0 = (int)(x0[1] - j + .5);
               if (j0 < 0 || j0 >= dims[1])
                  continue;
               for (int k = -r; k <= r; k++)
               {
                  int k0 = (int)(x0[0] - k + .5);
                  if (k0 < 0 || k0 >= dims[0])
                     continue;
                  int s = (i0 * dims[1] + j0) * dims[0] + k0;
                  if (mask == null || mask[s])
                     n += 1;
               }
            }
         }
      }
      out[0] = new float[3 * n];
      out[1] = new float[n];
      out[2] = new float[n];

      n = 0;
      for (int i = -rmax; i <= rmax; i++)
      {
         int i0 = (int)(x0[2] - i + .5);
         if (i0 < 0 || i0 >= dims[2])
            continue;
         for (int j = -rmax; j <= rmax; j++)
         {
            int j0 = (int)(x0[1] - j + .5);
            if (j0 < 0 || j0 >= dims[1])
               continue;
            for (int k = -rmax; k <= rmax; k++)
            {
               int k0 = (int)(x0[0] - k + .5);
               if (k0 < 0 || k0 >= dims[0])
                  continue;
               int s = (i0 * dims[1] + j0) * dims[0] + k0;
               if (mask == null || mask[s])
               {
                  out[0][3 * n] = k0 - x0[0];
                  out[0][3 * n + 1] = j0 - x0[1];
                  out[0][3 * n + 2] = i0 - x0[2];
                  out[1][n] = v[s];
                  out[2][n] = (float)Math.exp(-weight * ((i0 - x0[2]) * (i0 - x0[2]) + (j0 - x0[1]) * (j0 - x0[1]) + (k0 - x0[0]) * (k0 - x0[0])));
                  n += 1;
               }
            }
         }
      }
      return out;
   }

   public static float[][] getData(int[] dims, float[] x0, int deg, float weight, float[] v, boolean[] mask)
   {
      float[][] out = new float[3][];
      int nCoeffs = ((deg + 1) * (deg + 2) * (deg + 3)) / 6;
      int r0 = (int)Math.pow(nCoeffs / 8, 1./3);
      int rmax = r0;
      int n = 0;
      for (int r = r0; r < r0 + 10 && n < nCoeffs; r++)
      {
         rmax = r;
         for (int i = -r; i <= r; i++)
         {
            int i0 = (int)(x0[2] - i + .5);
            if (i0 < 0 || i0 >= dims[2])
               continue;
            for (int j = -r; j <= r; j++)
            {
               int j0 = (int)(x0[1] - j + .5);
               if (j0 < 0 || j0 >= dims[1])
                  continue;
               for (int k = -r; k <= r; k++)
               {
                  int k0 = (int)(x0[0] - k + .5);
                  if (k0 < 0 || k0 >= dims[0])
                     continue;
                  int s = (i0 * dims[1] + j0) * dims[0] + k0;
                  if (mask == null || mask[s])
                     n += 1;
               }
            }
         }
      }
      out[0] = new float[3 * n];
      out[1] = new float[n];
      out[2] = new float[n];

      n = 0;
      for (int i = -rmax; i <= rmax; i++)
      {
         int i0 = (int)(x0[2] - i + .5);
         if (i0 < 0 || i0 >= dims[2])
            continue;
         for (int j = -rmax; j <= rmax; j++)
         {
            int j0 = (int)(x0[1] - j + .5);
            if (j0 < 0 || j0 >= dims[1])
               continue;
            for (int k = -rmax; k <= rmax; k++)
            {
               int k0 = (int)(x0[0] - k + .5);
               if (k0 < 0 || k0 >= dims[0])
                  continue;
               int s = (i0 * dims[1] + j0) * dims[0] + k0;
               if (mask == null || mask[s])
               {
                  out[0][3 * n] = k0 - x0[0];
                  out[0][3 * n + 1] = j0 - x0[1];
                  out[0][3 * n + 2] = i0 - x0[2];
                  out[1][n] = v[s];
                  out[2][n] = (float)Math.exp(-weight * ((i0 - x0[2]) * (i0 - x0[2]) + (j0 - x0[1]) * (j0 - x0[1]) + (k0 - x0[0]) * (k0 - x0[0])));
                  n += 1;
               }
            }
         }
      }
      return out;
   }

   public static float[][] getData(int[] dims, float[] x0, int deg, float weight, double[] v, boolean[] mask)
   {
      float[][] out = new float[3][];
      int nCoeffs = ((deg + 1) * (deg + 2) * (deg + 3)) / 6;
      int r0 = (int)Math.pow(nCoeffs / 8, 1./3);
      int rmax = r0;
      int n = 0;
      for (int r = r0; r < r0 + 10 && n < nCoeffs; r++)
      {
         rmax = r;
         for (int i = -r; i <= r; i++)
         {
            int i0 = (int)(x0[2] - i + .5);
            if (i0 < 0 || i0 >= dims[2])
               continue;
            for (int j = -r; j <= r; j++)
            {
               int j0 = (int)(x0[1] - j + .5);
               if (j0 < 0 || j0 >= dims[1])
                  continue;
               for (int k = -r; k <= r; k++)
               {
                  int k0 = (int)(x0[0] - k + .5);
                  if (k0 < 0 || k0 >= dims[0])
                     continue;
                  int s = (i0 * dims[1] + j0) * dims[0] + k0;
                  if (mask == null || mask[s])
                     n += 1;
               }
            }
         }
      }
      out[0] = new float[3 * n];
      out[1] = new float[n];
      out[2] = new float[n];

      n = 0;
      for (int i = -rmax; i <= rmax; i++)
      {
         int i0 = (int)(x0[2] - i + .5);
         if (i0 < 0 || i0 >= dims[2])
            continue;
         for (int j = -rmax; j <= rmax; j++)
         {
            int j0 = (int)(x0[1] - j + .5);
            if (j0 < 0 || j0 >= dims[1])
               continue;
            for (int k = -rmax; k <= rmax; k++)
            {
               int k0 = (int)(x0[0] - k + .5);
               if (k0 < 0 || k0 >= dims[0])
                  continue;
               int s = (i0 * dims[1] + j0) * dims[0] + k0;
               if (mask == null || mask[s])
               {
                  out[0][3 * n] = k0 - x0[0];
                  out[0][3 * n + 1] = j0 - x0[1];
                  out[0][3 * n + 2] = i0 - x0[2];
                  out[1][n] = (float)v[s];
                  out[2][n] = (float)Math.exp(-weight * ((i0 - x0[2]) * (i0 - x0[2]) + (j0 - x0[1]) * (j0 - x0[1]) + (k0 - x0[0]) * (k0 - x0[0])));
                  n += 1;
               }
            }
         }
      }
      return out;
   }

/**
 * Computes local weighted approximation of values on a given point set by a polynomial of requested degree
 * @param data array of 3 arrays of floats : selected node coordinates, values at selected nodes, weights of selected nodes
 * @param deg degree of approximating polynomial
 * @return coefficients of approximating polynomial a00 + a10 x0 + a01 x1 + a20 x0^2 + a11 x0 x1 + a02 x1^2 ...
 */
   public static float[] computeApproximatingPolynomial(float[][] data, int deg)
   {
      int n = data[0].length / 3;
      int nCoeffs = ((deg + 1) * (deg + 2) * (deg + 3)) / 6;

      if (n < nCoeffs)
         return null;
      float[][] A = new float[nCoeffs][nCoeffs];
      float[] b = new float[nCoeffs];
      Arrays.fill(b, 0);
      for (int i = 0; i < nCoeffs; i++)
         Arrays.fill(A[i], 0);
      for (int k = 0; k < n; k++)
      {
         float xk = data[0][3 * k];
         float yk = data[0][3 * k + 1];
         float zk = data[0][3 * k + 2];
         float vk = data[1][k];
         float wk = data[2][k];
         for (int deq = 0, eq = 0; deq <= deg; deq++)
            for (int deqx = 0; deqx <= deq; deqx++)
               for (int deqy = 0; deqy <= deq - deqx; deqy++, eq ++)
               {
                  b[eq] += (float)(wk * vk * Math.pow(xk, deqx) *
                                             Math.pow(yk, deqy) *
                                             Math.pow(zk, deq - deqx - deqy));
                  for (int dvar = 0, var = 0; dvar <= deg; dvar++)
                     for (int dvarx = dvar; dvarx >= 0; dvarx--)
                        for (int dvary = dvar - dvarx; dvary >= 0; dvary--, var ++)
                           A[eq][var] += (float)(wk * Math.pow(xk, deqx + dvarx) *
                                                      Math.pow(yk, deqy + dvary) *
                                                      Math.pow(zk, deq - deqx - deqy + dvar - dvarx - dvary));
               }
      }
      return NumericalMethods.lsolve(A, b);
   }

   public static void main(String[] argv)
   {
      float[] vals = new float[125];
      int[] dims = {5,5,5};
      float[] p = {2.5f, 2.5f, 2.5f};
      for (int i = 0, l = 0; i < 5; i++)
         for (int j = 0; j < 5; j++)
         for (int k = 0; k < 5; k++, l++)
         {
            float x = k - 2.5f;
            float y = j - 2.5f;
            float z = i - 2.5f;
            vals[l] = 1 + 2 * x + 3 * y - z + x * x + 2 * x * y - y * y - z * z + (float)Math.random() - .5f;
         }
      float[] c = coeffs(vals, null, dims, p, 2, 2, 2);
      for (int i = 0; i < c.length; i++)
         System.out.printf("%8.3f ", c[i]);
      System.out.println("");
   }
}
