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

/**
 ** @author Krzysztof S. Nowinski, University of Warsaw ICM
 */

public class PolynomialApproximation
{

   private PolynomialApproximation()
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
      if (dims == null)
         return null;
      if (dims.length == 2)
         return PolynomialApproximation2D.computeApproximatingPolynomial(getData(dims, x0, deg, weight, v, mask), deg);
      if (dims.length == 3)
         return PolynomialApproximation3D.computeApproximatingPolynomial(getData(dims, x0, deg, weight, v, mask), deg);
      return null;
   }
   
   public static float[] coeffs(short[] v, boolean[] mask, int[] dims, float[] x0, int deg, float weight, int r)
   {
      if (dims == null)
         return null;
      if (dims.length == 2)
         return PolynomialApproximation2D.computeApproximatingPolynomial(getData(dims, x0, deg, weight, v, mask), deg);
      if (dims.length == 3)
         return PolynomialApproximation3D.computeApproximatingPolynomial(getData(dims, x0, deg, weight, v, mask), deg);
      return null;
   }
   
   public static float[] coeffs(int[] v, boolean[] mask, int[] dims, float[] x0, int deg, float weight, int r)
   {
      if (dims == null)
         return null;
      if (dims.length == 2)
         return PolynomialApproximation2D.computeApproximatingPolynomial(getData(dims, x0, deg, weight, v, mask), deg);
      if (dims.length == 3)
         return PolynomialApproximation3D.computeApproximatingPolynomial(getData(dims, x0, deg, weight, v, mask), deg);
      return null;
   }
   
   public static float[] coeffs(float[] v, boolean[] mask, int[] dims, float[] x0, int deg, float weight, int r)
   {
      if (dims == null)
         return null;
      if (dims.length == 2)
         return PolynomialApproximation2D.computeApproximatingPolynomial(getData(dims, x0, deg, weight, v, mask), deg);
      if (dims.length == 3)
         return PolynomialApproximation3D.computeApproximatingPolynomial(getData(dims, x0, deg, weight, v, mask), deg);
      return null;
   }
   
   public static float[] coeffs(double[] v, boolean[] mask, int[] dims, float[] x0, int deg, float weight, int r)
   {
      if (dims == null)
         return null;
      if (dims.length == 2)
         return PolynomialApproximation2D.computeApproximatingPolynomial(getData(dims, x0, deg, weight, v, mask), deg);
      if (dims.length == 3)
         return PolynomialApproximation3D.computeApproximatingPolynomial(getData(dims, x0, deg, weight, v, mask), deg);
      return null;
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
      if (dims == null)
         return null;
      if (dims.length == 2)
         return PolynomialApproximation2D.getData(dims, x0, deg, weight, v, mask);
      if (dims.length == 3)
         return PolynomialApproximation3D.getData(dims, x0, deg, weight, v, mask);
      return null;
   }
   public static float[][] getData(int[] dims, float[] x0, int deg, float weight, short[] v, boolean[] mask)
   {
      if (dims == null)
         return null;
      if (dims.length == 2)
         return PolynomialApproximation2D.getData(dims, x0, deg, weight, v, mask);
      if (dims.length == 3)
         return PolynomialApproximation3D.getData(dims, x0, deg, weight, v, mask);
      return null;
   }
   public static float[][] getData(int[] dims, float[] x0, int deg, float weight, int[] v, boolean[] mask)
   {
      if (dims == null)
         return null;
      if (dims.length == 2)
         return PolynomialApproximation2D.getData(dims, x0, deg, weight, v, mask);
      if (dims.length == 3)
         return PolynomialApproximation3D.getData(dims, x0, deg, weight, v, mask);
      return null;
   }
   public static float[][] getData(int[] dims, float[] x0, int deg, float weight, float[] v, boolean[] mask)
   {
      if (dims == null)
         return null;
      if (dims.length == 2)
         return PolynomialApproximation2D.getData(dims, x0, deg, weight, v, mask);
      if (dims.length == 3)
         return PolynomialApproximation3D.getData(dims, x0, deg, weight, v, mask);
      return null;
   }
   public static float[][] getData(int[] dims, float[] x0, int deg, float weight, double[] v, boolean[] mask)
   {
      if (dims == null)
         return null;
      if (dims.length == 2)
         return PolynomialApproximation2D.getData(dims, x0, deg, weight, v, mask);
      if (dims.length == 3)
         return PolynomialApproximation3D.getData(dims, x0, deg, weight, v, mask);
      return null;
   }

   public static float[] computeApproximatingPolynomial(float[][] data, int ndim, int deg)
   {
       switch (ndim)
       {
       case 2:
          return PolynomialApproximation2D.computeApproximatingPolynomial(data, deg);   
       case 3:
          return PolynomialApproximation3D.computeApproximatingPolynomial(data, deg);   
       default:
           return null;
       }
   }
}
