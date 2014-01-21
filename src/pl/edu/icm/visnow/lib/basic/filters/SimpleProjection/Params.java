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

package pl.edu.icm.visnow.lib.basic.filters.SimpleProjection;

import pl.edu.icm.visnow.engine.core.ParameterEgg;
import pl.edu.icm.visnow.engine.core.ParameterType;
import pl.edu.icm.visnow.engine.core.Parameters;
import pl.edu.icm.visnow.engine.core.Parameter;

/**
 * @author  Bartosz Borucki (babor@icm.edu.pl)
 * Warsaw University, Interdisciplinary Centre
 * for Mathematical and Computational Modelling
 */
public class Params extends Parameters
{

   public static final int METHOD_MAX = 0;
   public static final int METHOD_MIN = 1;
   public static final int METHOD_MEAN = 2;
   public static final int METHOD_NORMALIZED_MEAN = 3;
   public static final String[] methodsStrings =
   {
      "max", "min", "mean", "normalized mean"
   };
   private Parameter<Integer> levels;
   private Parameter<Integer> axis_l0;
   private Parameter<Integer> axis_l1;
   private Parameter<Integer> method;

   @SuppressWarnings("unchecked")
   private void initParameters()
   {
      method = getParameter("method");
      method.setValue(METHOD_MAX);
      axis_l0 = getParameter("axis_l0");
      axis_l0.setValue(0);
      axis_l1 = getParameter("axis_l1");
      axis_l1.setValue(1);
      levels = getParameter("levels");
      levels.setValue(1);
   }

   private static ParameterEgg[] eggs = new ParameterEgg[]
   {
      new ParameterEgg<Integer>("method", ParameterType.dependent),
      new ParameterEgg<Integer>("axis_l0", ParameterType.dependent),
      new ParameterEgg<Integer>("axis_l1", ParameterType.dependent),
      new ParameterEgg<Integer>("levels", ParameterType.dependent)
   };

   public Params()
   {
      super(eggs);
      initParameters();
   }

   public int getAxisL0()
   {
      return axis_l0.getValue();
   }

   public void setAxisL0(int axis)
   {
      this.axis_l0.setValue(axis);
      if(axis == this.getAxisL1()) {
          this.axis_l1.setValue(axis == 0 ? 1 : 0);
      }
      fireStateChanged();
   }

   public int getAxisL1()
   {
      return axis_l1.getValue();
   }

   public void setAxisL1(int axis)
   {
       if(axis == this.axis_l0.getValue())
           return;
       
      this.axis_l1.setValue(axis);
      fireStateChanged();
   }

   public int getLevels()
   {
      return levels.getValue();
   }

   public void setLevels(int levels)
   {
       if(levels < 1) levels = 1;
       if(levels > 2) levels = 2;
      this.levels.setValue(levels);
      fireStateChanged();
   }

   /**
    * @return the method
    */
   public int getMethod()
   {
      return method.getValue();
   }

   /**
    * @param method the method to set
    */
   public void setMethod(int method)
   {
      this.method.setValue(method);
      fireStateChanged();
   }
}
