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

package pl.edu.icm.visnow.lib.utils;

/**
 ** @author Krzysztof S. Nowinski, University of Warsaw ICM
 */
public class FastIntQueue
{
   private static final int INIT_LEN = 32768;

   private int length = 0, first = 0, last = -1, arrLength = INIT_LEN;
   private int[] queue = new int[INIT_LEN];

   public FastIntQueue()
   {

   }

   public synchronized final void reset()
   {
      queue = new int[INIT_LEN];
      arrLength = INIT_LEN;
      length = 0;
      first = 0;
      last = -1;
   }

   public synchronized boolean isEmpty()
   {
      return length <= 0;
   }

   @Override
   public String toString()
   {
      StringBuilder b = new StringBuilder();
      b.append(String.format("   %3d %3d: ", first, last));
      for (int i = 0; i < length; i++)
      {
         int j = (first + i) % queue.length;
         b.append(String.format("%3d ", queue[j]));
      }
      return b.toString();
   }

   public synchronized void insert(int val)
   {
      length += 1;
      if (length > arrLength)
      {
         arrLength *= 2;
         int[] tmp = new int[arrLength];
         System.arraycopy(queue, first, tmp, 0, queue.length - first);
         if (first > 0)
            System.arraycopy(queue, 0, tmp, queue.length - first, first);
         queue = tmp;
         first = 0;
         last = length - 1;
      }
      else
         last = (last + 1) % arrLength;
      queue[last] = val;
   }

   public synchronized int get() throws IndexOutOfBoundsException
   {
      if (length == 0)
         throw new IndexOutOfBoundsException();
      length -= 1;
      int val = queue[first];
      first = (first + 1) % arrLength;
      return val;
   }

   public static void main(String[] argv)
   {
      FastIntQueue q = new FastIntQueue();
      for (int i = 0; i < 100; i++)
      {
         int val = (int) (10 * Math.random());
         System.out.printf("%2d ", val);
         if (val < 7)
         {
            q.insert(val);
            System.out.print("   ");
         }
         else if (!q.isEmpty())
         {
            System.out.printf("%2d ", q.get());
         }
         System.out.println(":" + q);
      }
   }
}
