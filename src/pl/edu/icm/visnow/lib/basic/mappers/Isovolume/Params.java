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

package pl.edu.icm.visnow.lib.basic.mappers.Isovolume;


import pl.edu.icm.visnow.engine.core.ParameterEgg;
import pl.edu.icm.visnow.engine.core.ParameterType;
import pl.edu.icm.visnow.engine.core.Parameters;

/**
 *
 * 
 * @author  Krzysztof S. Nowinski, University of Warsaw, ICM
 */
public class Params extends Parameters {
   protected static final String ISOCOMPONENT = "component";
   protected static final String TYPE         = "type";
   protected static final String THRESHOLD    = "threshold";

    /**
     * isoComponent - data component for isosurfacing
     * threshold - isosurface threshold
     * low  - input field will be cropped from below according to these indices before isosurfacing
     * up   - input field will be cropped from above according to these indices before isosurfacing
     * downsize - input field will be downsized according to these indices before isosurfacing
     */

    private boolean recompute = true;


    public Params() 
    {
        super(eggs);
    }
    
    private static ParameterEgg[] eggs = new ParameterEgg[]
    {
        new ParameterEgg<Integer>(ISOCOMPONENT, ParameterType.dependent, 0),
        new ParameterEgg<Float>(THRESHOLD, ParameterType.dependent, 0.f),
        new ParameterEgg<Integer>(TYPE, ParameterType.independent, 1)
    };

   public int getIsoComponent()
   {
      return (Integer) getValue(ISOCOMPONENT);
   }

   public void setIsoComponent(int isoComponent)
   {
      this.setValue(ISOCOMPONENT, isoComponent);
      fireStateChanged();
   }

   public Float getThreshold()
   {
      return (Float) getValue(THRESHOLD);
   }

   public void setThreshold(float threshold)
   {
      this.setValue(THRESHOLD, threshold);
      recompute = true;
      fireStateChanged();
   }

   public int getType()
   {
      return (Integer)getValue(TYPE);
   }

   public void setType(int type)
   {
      setValue(TYPE,type);
      fireStateChanged();
   }

   /**
    * Get the value of recompute
    *
    * @return the value of recompute
    */
   public boolean isRecompute()
   {
      return recompute;
   }

   /**
    * Set the value of recompute
    *
    * @param recompute new value of recompute
    */
   public void setRecompute(boolean recompute)
   {
      this.recompute = recompute;
   }

}
