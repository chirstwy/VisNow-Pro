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


public class PolynomialApproximation2D
{

   private PolynomialApproximation2D()
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
      if (dims == null || dims.length != 2)
         return null;
      if (v == null || v.length != dims[0] * dims[1] || mask != null && mask.length != dims[0] * dims[1])
         return null;
      return computeApproximatingPolynomial(getData(dims, x0, deg, weight, v, mask), deg);
   }
   
   public static float[] coeffs(short[] v, boolean[] mask, int[] dims, float[] x0, int deg, float weight, int r)
   {
      if (dims == null || dims.length != 2)
         return null;
      if (v == null || v.length != dims[0] * dims[1] || mask != null && mask.length != dims[0] * dims[1])
         return null;
      return computeApproximatingPolynomial(getData(dims, x0, deg, weight, v, mask), deg);
   }
   
   public static float[] coeffs(int[] v, boolean[] mask, int[] dims, float[] x0, int deg, float weight, int r)
   {
      if (dims == null || dims.length != 2)
         return null;
      if (v == null || v.length != dims[0] * dims[1] || mask != null && mask.length != dims[0] * dims[1])
         return null;
      return computeApproximatingPolynomial(getData(dims, x0, deg, weight, v, mask), deg);
   }
   
   public static float[] coeffs(float[] v, boolean[] mask, int[] dims, float[] x0, int deg, float weight, int r)
   {
      if (dims == null || dims.length != 2)
         return null;
      if (v == null || v.length != dims[0] * dims[1] || mask != null && mask.length != dims[0] * dims[1])
         return null;
      return computeApproximatingPolynomial(getData(dims, x0, deg, weight, v, mask), deg);
   }
   
   public static float[] coeffs(double[] v, boolean[] mask, int[] dims, float[] x0, int deg, float weight, int r)
   {
      if (dims == null || dims.length != 2)
         return null;
      if (v == null || v.length != dims[0] * dims[1] || mask != null && mask.length != dims[0] * dims[1])
         return null;
      return computeApproximatingPolynomial(getData(dims, x0, deg, weight, v, mask), deg);
   }
   
/**
 * Gets sufficient number of valid nodes in the neighbourhood to compute approximating polynomial of a given degree
 * @param dims dimensions of input data array (length must be 2)
 * @param x0   coordinates of thew center of approximation area
 * @param deg  degree of approximating polynomial
 * @param weight coefficient of gaussian weight function
 * @param v array of values to be approximated
 * @param mask   optional mask array (mask[i] indicates if the i-th point is valid)
 * @return array of 3 arrays of floats : selected node coordinates, values at selected nodes, weights of selected nodes
 * for use in the compouteApproximatingPolynomial method
 */   
   public static float[][] getData(int[] dims, float[] x0, int deg, float weight, byte[] v, boolean[] mask)
   {
      float[][] out = new float[3][];
      int nCoeffs = ((deg + 1) * (deg + 2)) / 2;
      int r0 = (int)Math.sqrt(nCoeffs / 4);
      int rmax = r0;
      int n = 0;
      for (int r = r0; r < r0 + 10 && n < nCoeffs; r++)
      {
         rmax = r;
         for (int i = -r; i <= r; i++)
         {
            int i0 = (int)(x0[1] - i + .5);
            if (i0 < 0 || i0 >= dims[1])
               continue;
            for (int j = -r; j <= r; j++)
            {
               int j0 = (int)(x0[0] - j + .5);
               if (j0 < 0 || j0 >= dims[0])
                  continue;
               int k = i0 * dims[0] + j0;
               if (mask == null || mask[k])
                  n += 1;
            }
         }
      } 
      
      out[0] = new float[2 * n];
      out[1] = new float[n];
      out[2] = new float[n];
      n = 0;
      for (int i = -rmax; i <= rmax; i++)
      {
         int i0 = (int)(x0[1] - i + .5);
         if (i0 < 0 || i0 >= dims[1])
            continue;
         for (int j = -rmax; j <= rmax; j++)
         {
            int j0 = (int)(x0[0] - j + .5);
            if (j0 < 0 || j0 >= dims[0])
               continue;
            int k = i0 * dims[0] + j0;
            if (mask == null || mask[k])
            {
               out[1][n] = 0xff & v[k];
               out[2][n] = (float)Math.exp(-weight * ((i0 - x0[1]) * (i0 - x0[1]) + (j0 - x0[0]) * (j0 - x0[0])));
               out[0][2 * n] = j0 - x0[0];
               out[0][2 * n + 1] = i0 - x0[1];
               n += 1;
            }
         }
      }
      return out;
   }

   public static float[][] getData(int[] dims, float[] x0, int deg, float weight, short[] v, boolean[] mask)
   {
      float[][] out = new float[3][];
      int nCoeffs = ((deg + 1) * (deg + 2)) / 2;
      int r0 = (int)Math.sqrt(nCoeffs / 4);
      int rmax = r0;
      int n = 0;
      for (int r = r0; r < r0 + 10 && n < nCoeffs; r++)
      {
         rmax = r;
         for (int i = -r; i <= r; i++)
         {
            int i0 = (int)(x0[1] - i + .5);
            if (i0 < 0 || i0 >= dims[1])
               continue;
            for (int j = -r; j <= r; j++)
            {
               int j0 = (int)(x0[0] - j + .5);
               if (j0 < 0 || j0 >= dims[0])
                  continue;
               int k = i0 * dims[0] + j0;
               if (mask == null || mask[k])
                  n += 1;
            }
         }
      } 
      
      out[0] = new float[2 * n];
      out[1] = new float[n];
      out[2] = new float[n];
      n = 0;
      for (int i = -rmax; i <= rmax; i++)
      {
         int i0 = (int)(x0[1] - i + .5);
         if (i0 < 0 || i0 >= dims[1])
            continue;
         for (int j = -rmax; j <= rmax; j++)
         {
            int j0 = (int)(x0[0] - j + .5);
            if (j0 < 0 || j0 >= dims[0])
               continue;
            int k = i0 * dims[0] + j0;
            if (mask == null || mask[k])
            {
               out[1][n] = v[k];
               out[2][n] = (float)Math.exp(-weight * ((i0 - x0[1]) * (i0 - x0[1]) + (j0 - x0[0]) * (j0 - x0[0])));
               out[0][2 * n] = j0 - x0[0];
               out[0][2 * n + 1] = i0 - x0[1];
               n += 1;
            }
         }
      }
      return out;
   }

   public static float[][] getData(int[] dims, float[] x0, int deg, float weight, int[] v, boolean[] mask)
   {
      float[][] out = new float[3][];
      int nCoeffs = ((deg + 1) * (deg + 2)) / 2;
      int r0 = (int)Math.sqrt(nCoeffs / 4);
      int rmax = r0;
      int n = 0;
      for (int r = r0; r < r0 + 10 && n < nCoeffs; r++)
      {
         rmax = r;
         for (int i = -r; i <= r; i++)
         {
            int i0 = (int)(x0[1] - i + .5);
            if (i0 < 0 || i0 >= dims[1])
               continue;
            for (int j = -r; j <= r; j++)
            {
               int j0 = (int)(x0[0] - j + .5);
               if (j0 < 0 || j0 >= dims[0])
                  continue;
               int k = i0 * dims[0] + j0;
               if (mask == null || mask[k])
                  n += 1;
            }
         }
      } 
      
      out[0] = new float[2 * n];
      out[1] = new float[n];
      out[2] = new float[n];
      n = 0;
      for (int i = -rmax; i <= rmax; i++)
      {
         int i0 = (int)(x0[1] - i + .5);
         if (i0 < 0 || i0 >= dims[1])
            continue;
         for (int j = -rmax; j <= rmax; j++)
         {
            int j0 = (int)(x0[0] - j + .5);
            if (j0 < 0 || j0 >= dims[0])
               continue;
            int k = i0 * dims[0] + j0;
            if (mask == null || mask[k])
            {
               out[1][n] = v[k];
               out[2][n] = (float)Math.exp(-weight * ((i0 - x0[1]) * (i0 - x0[1]) + (j0 - x0[0]) * (j0 - x0[0])));
               out[0][2 * n] = j0 - x0[0];
               out[0][2 * n + 1] = i0 - x0[1];
               n += 1;
            }
         }
      }
      return out;
   }

   public static float[][] getData(int[] dims, float[] x0, int deg, float weight, float[] v, boolean[] mask)
   {
      float[][] out = new float[3][];
      int nCoeffs = ((deg + 1) * (deg + 2)) / 2;
      int r0 = (int)Math.sqrt(nCoeffs / 4);
      int rmax = r0;
      int n = 0;
      for (int r = r0; r < r0 + 10 && n < nCoeffs; r++)
      {
         rmax = r;
         for (int i = -r; i <= r; i++)
         {
            int i0 = (int)(x0[1] - i + .5);
            if (i0 < 0 || i0 >= dims[1])
               continue;
            for (int j = -r; j <= r; j++)
            {
               int j0 = (int)(x0[0] - j + .5);
               if (j0 < 0 || j0 >= dims[0])
                  continue;
               int k = i0 * dims[0] + j0;
               if (mask == null || mask[k])
                  n += 1;
            }
         }
      } 
      
      out[0] = new float[2 * n];
      out[1] = new float[n];
      out[2] = new float[n];
      n = 0;
      for (int i = -rmax; i <= rmax; i++)
      {
         int i0 = (int)(x0[1] - i + .5);
         if (i0 < 0 || i0 >= dims[1])
            continue;
         for (int j = -rmax; j <= rmax; j++)
         {
            int j0 = (int)(x0[0] - j + .5);
            if (j0 < 0 || j0 >= dims[0])
               continue;
            int k = i0 * dims[0] + j0;
            if (mask == null || mask[k])
            {
               out[1][n] = v[k];
               out[2][n] = (float)Math.exp(-weight * ((i0 - x0[1]) * (i0 - x0[1]) + (j0 - x0[0]) * (j0 - x0[0])));
               out[0][2 * n] = j0 - x0[0];
               out[0][2 * n + 1] = i0 - x0[1];
               n += 1;
            }
         }
      }
      return out;
   }

   public static float[][] getData(int[] dims, float[] x0, int deg, float weight, double[] v, boolean[] mask)
   {
      float[][] out = new float[3][];
      int nCoeffs = ((deg + 1) * (deg + 2)) / 2;
      int r0 = (int)Math.sqrt(nCoeffs / 4);
      int rmax = r0;
      int n = 0;
      for (int r = r0; r < r0 + 10 && n < nCoeffs; r++)
      {
         rmax = r;
         for (int i = -r; i <= r; i++)
         {
            int i0 = (int)(x0[1] - i + .5);
            if (i0 < 0 || i0 >= dims[1])
               continue;
            for (int j = -r; j <= r; j++)
            {
               int j0 = (int)(x0[0] - j + .5);
               if (j0 < 0 || j0 >= dims[0])
                  continue;
               int k = i0 * dims[0] + j0;
               if (mask == null || mask[k])
                  n += 1;
            }
         }
      } 
      
      out[0] = new float[2 * n];
      out[1] = new float[n];
      out[2] = new float[n];
      n = 0;
      for (int i = -rmax; i <= rmax; i++)
      {
         int i0 = (int)(x0[1] - i + .5);
         if (i0 < 0 || i0 >= dims[1])
            continue;
         for (int j = -rmax; j <= rmax; j++)
         {
            int j0 = (int)(x0[0] - j + .5);
            if (j0 < 0 || j0 >= dims[0])
               continue;
            int k = i0 * dims[0] + j0;
            if (mask == null || mask[k])
            {
               out[1][n] = (float)v[k];
               out[2][n] = (float)Math.exp(-weight * ((i0 - x0[1]) * (i0 - x0[1]) + (j0 - x0[0]) * (j0 - x0[0])));
               out[0][2 * n] = j0 - x0[0];
               out[0][2 * n + 1] = i0 - x0[1];
               n += 1;
            }
         }
      }
      return out;
   }


   public static float[] computeApproximatingPolynomial(float[][] data, int deg)
   {
      int n = data[0].length / 2;
      int nCoeffs = ((deg + 1) * (deg + 2)) / 2;
      // now, the problem is: interpolate vals[i] by a00 + a10 xi + a01 yi + a20 xi^2 + a11 xi yi + a02 yi^2, where i = 0...,n-1

      if (n < nCoeffs)
         return null;
      float[][] A = new float[nCoeffs][nCoeffs];
      float[] b = new float[nCoeffs];
      Arrays.fill(b, 0);
      for (int i = 0; i < nCoeffs; i++)
         Arrays.fill(A[i], 0);
      for (int k = 0; k < n; k++)
      {
         float xk = data[0][2 * k];
         float yk = data[0][2 * k + 1];
         float vk = data[1][k];
         float wk = data[2][k];
         for (int deq = 0, eq = 0; deq <= deg; deq++)
            for (int deqx = 0; deqx <= deq; deqx++, eq ++)
            {
               b[eq] += (float)(wk * vk * Math.pow(xk, deqx) * Math.pow(yk, deq - deqx));
               for (int dvar = 0, var = 0; dvar <= deg; dvar++)
                  for (int dvarx = dvar; dvarx >= 0; dvarx--, var ++)
                     A[eq][var] += (float)(wk * Math.pow(xk, deqx + dvarx) * Math.pow(yk, deq - deqx + dvar - dvarx));
            }
      }
      return NumericalMethods.lsolve(A, b);
   }

   public static void main(String[] argv)
   {
      float[] vals = new float[25];
      int[] dims = {5,5};
      float[] p = {2.5f, 2.5f};
      for (int i = 0, k = 0; i < 5; i++)
         for (int j = 0; j < 5; j++, k++)
         {
            float x = j - 2.5f;
            float y = i - 2.5f;
            vals[k] = 1 + 2 * x + 3 * y + x * x + 2 * x * y - y * y;
         }
      float[] c = coeffs(vals, null, dims, p, 2, 2, 2);
      for (int i = 0; i < c.length; i++)
         System.out.printf("%8.3f ", c[i]);
      System.out.println("");
   }
}
