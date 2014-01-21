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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import pl.edu.icm.visnow.datasets.RegularField;

/**
 *
 * @author Krzysztof S. Nowinski, University of Warsaw ICM
 */
abstract public class SegmentVolume
{



   protected static final int SEGLEN            = 1000;
   public static final int SIMILARITY_COMPUTED  = 1;
   public static final int COMPUTING_OUT        = 2;
   protected static final int UNKNOWN           = 0;
   protected static final int BACKGROUND        = 1;
   protected int status = 0;
   protected RegularField inField = null;
   protected ArrayList<int[]> selectedPoints = null;
   protected int[] dims = null;
   protected byte[] mask = null;
   protected boolean[] allowed = null;
   protected int[] off;
   protected short[] dist;
   protected short[] outd = null;
   protected int ndata;
   protected int low = -1;
   protected int up = 256;
   protected int lmax= 256;
   
   protected void extendMargins(short val)
   {
      if (outd == null)
         return;
      int i, j, k, l, m;
      k = dims[0] * dims[1] * (dims[2] - 1);
      l = dims[0] * dims[1];
      m = dims[0] * dims[1] * dims[2];
      for (i = 0; i < l; i++)
         outd[i] = outd[i + k] = val;
      k = dims[0] * (dims[1] - 1);
      for (i = 0; i < m; i += l)
         for (j = 0; j < dims[0]; j++)
            outd[i + j] = outd[i + j + k] = val;
      k = dims[0] - 1;
      for (i = 0; i < m; i += l)
         for (j = 0; j < l; j += dims[0])
            outd[i + j] = outd[i + j + k] = val;
   }
   
   abstract public void setIndices(ArrayList<Integer> indices);
   abstract public void setWeights(float[] weights);
   abstract public void setTollerance(short tollerance);
   public void setRange(int low, int up)
   {
      this.low = low;
      this.up = up;
   }
   
   abstract public void computeDistance(ArrayList<int[]> selectedPoints, boolean[] allowed);
   abstract public void setComponent(int component);

   public int getStatus()
   {
      return status;
   }

  /**
    * Utility field holding list of ChangeListeners.
    */
   private transient ArrayList<ChangeListener> changeListenerList =
           new ArrayList<ChangeListener>();

   /**
    * Registers ChangeListener to receive events.
    * @param listener The listener to register.
    */
   public synchronized void addChangeListener(ChangeListener listener)
   {
      changeListenerList.add(listener);
   }

   /**
    * Removes ChangeListener from the list of listeners.
    * @param listener The listener to remove.
    */
   public synchronized void removeChangeListener(ChangeListener listener)
   {
      changeListenerList.remove(listener);
   }

   /**
    * Notifies all registered listeners about the event.
    *
    * @param object Parameter #1 of the <CODE>ChangeEvent<CODE> constructor.
    */
   public void fireStateChanged()
   {
      ChangeEvent e = new ChangeEvent(this);
      for (ChangeListener listener: changeListenerList)
         listener.stateChanged(e);
   }
   
   public int getLmax()
   {
      return lmax;
   }

   public void setLmax(int lmax)
   {
      this.lmax = lmax;
   }
}
