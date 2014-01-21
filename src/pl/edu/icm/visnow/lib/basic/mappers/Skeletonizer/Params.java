///<editor-fold defaultstate="collapsed" desc=" COPYRIGHT AND LICENSE ">
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
//</editor-fold>
package pl.edu.icm.visnow.lib.basic.mappers.Skeletonizer;

import pl.edu.icm.visnow.engine.core.ParameterEgg;
import pl.edu.icm.visnow.engine.core.ParameterType;
import pl.edu.icm.visnow.engine.core.Parameters;

/**
 *
 * @author Krzysztof S. Nowinski, University of Warsaw, ICM
 */
public class Params extends Parameters
{
   public static final int SKELETONIZE = 3;
   public static final int MIN_LENGTH  = 2;
   public static final int SELECT      = 1;
   protected static final String COMPONENT = "component";
   protected static final String THRESHOLD = "threshold";
   protected static final String ABOVE = "above";
   protected static final String MIN_SEG_LEN = "minimal segment length";
   protected static final String SEGMENTED = "segmented";
   protected static final String SETS = "sets";
   protected static final String NSETS = "nsets";
   protected static final String RECOMPUTE = "recompute";
   @SuppressWarnings("unchecked")
   private static ParameterEgg[] eggs = new ParameterEgg[]
   {
      new ParameterEgg<Integer>(COMPONENT, ParameterType.dependent, 0),
      new ParameterEgg<Float>(THRESHOLD, ParameterType.dependent, 128.f),
      new ParameterEgg<Boolean>(ABOVE, ParameterType.dependent, true),
      new ParameterEgg<Integer>(MIN_SEG_LEN, ParameterType.dependent, 20),
      new ParameterEgg<Boolean>(SEGMENTED, ParameterType.dependent, false),
      new ParameterEgg<int[]>(SETS, ParameterType.dependent, null),
      new ParameterEgg<Integer>(NSETS, ParameterType.dependent, 1),
      new ParameterEgg<Integer>(RECOMPUTE, ParameterType.independent, 0)
   };

   public Params()
   {
      super(eggs);
   }

   /**
    * Get the value of recompute
    *
    * @return the value of recompute
    */
   public int isRecompute()
   {
      return (Integer) getValue(RECOMPUTE);
   }

   /**
    * Set the value of recompute
    *
    * @param recompute new value of recompute
    */
   public void setRecompute(int recompute)
   {
      setValue(RECOMPUTE, recompute);
   }

   /**
    * Get the value of nSets
    *
    * @return the value of nSets
    */
   public int getNSets()
   {
      return (Integer) getValue(NSETS);
   }

   /**
    * Set the value of nSets
    *
    * @param nSets new value of nSets
    */
   public void setNSets(int nSets)
   {
      setValue(NSETS, nSets);
   }

   /**
    * Get the value of sets
    *
    * @return the value of sets
    */
   public int[] getSets()
   {
      return (int[]) getValue(SETS);
   }

   /**
    * Set the value of sets
    *
    * @param sets new value of sets
    */
   public void setSets(int[] sets)
   {
      setValue(SETS, sets);
      setValue(RECOMPUTE, SKELETONIZE);
   }

   /**
    * Get the value of sets at specified index
    *
    * @param index
    * @return the value of sets at specified index
    */
   public int getSets(int index)
   {
      return ((int[]) getValue(SETS))[index];
   }

   /**
    * Set the value of sets at specified index.
    *
    * @param index
    * @param newSets new value of sets at specified index
    */
   public void setSets(int index, int newSets)
   {
      ((int[]) getValue(SETS))[index] = newSets;
   }

   /**
    * Get the value of segmented
    *
    * @return the value of segmented
    */
   public boolean isSegmented()
   {
      return (Boolean) getValue(SEGMENTED);
   }

   /**
    * Set the value of segmented
    *
    * @param segmented new value of segmented
    */
   public void setSegmented(boolean segmented)
   {
      setValue(SEGMENTED, segmented);
   }

   public int getMinSegLen()
   {
      return (Integer) getValue(MIN_SEG_LEN);
   }

   public void setMinSegLen(int minSegLen)
   {
      setValue(MIN_SEG_LEN, minSegLen);
      setValue(RECOMPUTE, MIN_LENGTH);
      setActive(true);
   }

   public float getThreshold()
   {
      return (Float) getValue(THRESHOLD);
   }

   public void setThreshold(float threshold)
   {
      setValue(THRESHOLD, threshold);
      setValue(RECOMPUTE, SKELETONIZE);
   }

   public boolean isAbove()
   {
      return (Boolean) getValue(ABOVE);
   }

   public void setAbove(boolean above)
   {
      setValue(ABOVE, above);
      setValue(RECOMPUTE, SKELETONIZE);
   }

   public int getComponent()
   {
      return (Integer) getValue(COMPONENT);
   }

   public void setComponent(int colorComponent)
   {
      setValue(COMPONENT, colorComponent);
      setValue(RECOMPUTE, SKELETONIZE);
   }
}
