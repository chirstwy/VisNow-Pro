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

import java.util.ArrayList;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import pl.edu.icm.visnow.engine.core.ParameterEgg;
import pl.edu.icm.visnow.engine.core.ParameterType;
import pl.edu.icm.visnow.engine.core.Parameters;
import pl.edu.icm.visnow.engine.core.Parameter;

/**
 *
 * @author Krzysztof S. Nowinski
 *   <p>   University of Warsaw, ICM
 */
public class Params extends Parameters
{

   /**
    * subsets - selected subsets to be displayed
    * low  - input field will be cropped from below according to these indices before isosurfacing
    * up   - input field will be cropped from above according to these indices before isosurfacing
    * downsize - input field will be downsized according to these indices before isosurfacing
    */
   protected Parameter<int[]> downsize;
   protected Parameter<int[]> low;
   protected Parameter<int[]> up;
   protected Parameter<Boolean> dimensionsChanged;
   protected Parameter<Integer> nThreads;
   protected Parameter<Boolean> smoothing;
   protected Parameter<Integer> smoothSteps;

   @SuppressWarnings("unchecked")
   private void initParameters()
   {
      downsize = getParameter("downsize");
      low = getParameter("low");
      up = getParameter("up");
      dimensionsChanged = getParameter("dimensionsChanged");
      nThreads = getParameter("nThreads");
      smoothing = getParameter("smoothing");
      smoothSteps = getParameter("smoothSteps");

      dimensionsChanged.setValue(true);
      downsize.setValue(new int[] {2, 2, 2});
      low.setValue(new int[] {0, 0, 0});
      up.setValue(new int[] {0, 0, 0 });
      nThreads.setValue(1);
      smoothing.setValue(false);
      smoothSteps.setValue(0);   
   }

   private static ParameterEgg[] eggs = new ParameterEgg[]
   {
      new ParameterEgg<int[]>("downsize", ParameterType.dependent),
      new ParameterEgg<int[]>("low", ParameterType.dependent),
      new ParameterEgg<int[]>("up", ParameterType.dependent),
      new ParameterEgg<Boolean>("dimensionsChanged", ParameterType.independent),
      new ParameterEgg<Integer>("nThreads", ParameterType.independent),
      new ParameterEgg<Boolean>("smoothing", ParameterType.independent),
      new ParameterEgg<Integer>("smoothSteps", ParameterType.independent)
   };

   public Params()
   {
      super(eggs);
      initParameters();
   }

   /**
    * Get the value of nThreads
    *
    * @return the value of nThreads
    */
   public int getNThreads()
   {
      return nThreads.getValue();
   }

   /**
    * Set the value of nThreads
    *
    * @param nThreads new value of nThreads
    */
   public void setNThreads(int nThreads)
   {
      this.nThreads.setValue(nThreads);
   }

   /**
    * Set the value of subsets
    *
    * @param subsets new value of subsets
    */
   public int[] getDownsize()
   {
      return downsize.getValue();
   }

   public void setDownsize(int[] downsize)
   {
      dimensionsChanged.setValue(false);
      int[] oldDown = this.downsize.getValue();
      if (oldDown == null)
         dimensionsChanged.setValue(true);
      else
         for (int i = 0; i < oldDown.length; i++)
            if (oldDown[i] != downsize[i])
               dimensionsChanged.setValue(true);
      this.downsize.setValue(downsize);
   }

   public int[] getLow()
   {
      return low.getValue();
   }

   public void setLowUp(int[] low, int[] up)
   {
      dimensionsChanged.setValue(this.up.getValue() != up || this.low.getValue() != low);
      if (dimensionsChanged.getValue())
         smoothing.setValue(smoothSteps.getValue() != 0);
      this.low.setValue(low);
      this.up.setValue(up);
   }

   public int[] getUp()
   {
      return up.getValue();
   }

   /**
    * Get the value of dimensionsChanged
    *
    * @return the value of dimensionsChanged
    */
   public boolean isDimensionsChanged()
   {
      return dimensionsChanged.getValue();
   }

   /**
    * Set the value of dimensionsChanged
    *
    * @param dimensionsChanged new value of dimensionsChanged
    */
   public void setDimensionsChanged(boolean dimensionsChanged)
   {
      this.dimensionsChanged.setValue(dimensionsChanged);
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
   @Override
   public synchronized void addChangeListener(ChangeListener listener)
   {
      changeListenerList.add(listener);
   }

   /**
    * Removes ChangeListener from the list of listeners.
    * @param listener The listener to remove.
    */
   @Override
   public synchronized void removeChangeListener(ChangeListener listener)
   {
      changeListenerList.remove(listener);
   }

   /**
    * Notifies all registered listeners about the event.
    *
    * @param object Parameter #1 of the <CODE>ChangeEvent<CODE> constructor.
    */
   @Override
   public void fireStateChanged()
   {
      ChangeEvent e = new ChangeEvent(this);
      for (ChangeListener listener : changeListenerList)
         listener.stateChanged(e);
   }

   public int getSmoothSteps()
   {
      return smoothSteps.getValue();
   }

   public void setSmoothSteps(int smoothSteps)
   {
      this.smoothSteps.setValue(smoothSteps);
      smoothing.setValue(smoothSteps != 0);
   }

   public boolean isSmoothing()
   {
      return smoothing.getValue();
   }

   public void setSmoothing(boolean smoothing)
   {
      this.smoothing.setValue(smoothing && smoothSteps.getValue() != 0);
   }
}
