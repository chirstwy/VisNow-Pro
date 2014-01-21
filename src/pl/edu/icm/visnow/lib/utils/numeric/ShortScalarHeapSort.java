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

package pl.edu.icm.visnow.lib.utils.numeric;


/**
 *
 * @author Krzysztof S. Nowinski, University of Warsaw ICM
 */
public class ShortScalarHeapSort
{

   private short[] sortedItems;
   private int[] indices;
   private int len;
   private int n;
   private int left;
   private int right;
   private int largest;

   public ShortScalarHeapSort(short[] sortedItems, int[] indices)
   {
      if (indices.length != sortedItems.length)
      {
         System.out.println("bad table lengths");
         return;
      }
      this.sortedItems = sortedItems;
      this.indices = indices;
      n = len = indices.length;
   }

   private void buildheap(short[] a)
   {
      n = len - 1;
      for (int i = n / 2; i >= 0; i--)
         maxheap(a, i);
   }
   
   private void maxheap(short[] a, int i)
   {
      left = 2 * i;
      right = 2 * i + 1;
      if (left <= n && sortedItems[left] > sortedItems[i])
         largest = left;
      else
         largest = i;

      if (right <= n && sortedItems[right] > sortedItems[largest])
         largest = right;
      if (largest != i)
      {
         exchange(i, largest);
         maxheap(a, largest);
      }
   }

   private void exchange(int i, int j)
   {
      short t;
      t = sortedItems[i];
      sortedItems[i] = sortedItems[j];
      sortedItems[j] = t;
      
      int u = indices[i];
      indices[i] = indices[j];
      indices[j] = u;
      
   }

   public void sort()
   {
      buildheap(sortedItems);

      for (int i = n; i > 0; i--)
      {
         exchange(0, i);
         n -= 1;
         maxheap(sortedItems, 0);
      }
   }

   public static void main(String[] args)
   {
      for (int n = 1000; n <= 10000000; n *= 10)
      {
         short[] t = new short[n];
         int[] ind = new int[n];
         for (int i = 0; i < t.length; i++)
            t[i] = (short)(Math.random() * Short.MAX_VALUE);
         for (int i = 0; i < ind.length; i++)
            ind[i] = i;
         ShortScalarHeapSort hs = new ShortScalarHeapSort(t, ind);
         long s = System.currentTimeMillis();
         hs.sort();
         System.out.printf("%6d %7d%n", n, System.currentTimeMillis() - s);
         if (n == 1000)
            for (int i = 0; i < n; i++)
               System.out.printf("%3d %3d%n", ind[i], t[i]);
      }
   }
}
