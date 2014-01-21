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

package pl.edu.icm.visnow.lib.basic.mappers.CityPlot;

import pl.edu.icm.visnow.engine.core.ParameterEgg;
import pl.edu.icm.visnow.engine.core.ParameterType;
import pl.edu.icm.visnow.engine.core.Parameters;

/**
 *
 * @author  Krzysztof S. Nowinski, University of Warsaw, ICM
 */
public class Params extends Parameters
{

   public static final int LEFT = -1;
   public static final int CENTER = 0;
   public static final int RIGHT = 1;
   public static final int CONSTANT = 0;
   public static final int SQUARE = 1;
   public static final int RECT = 2;
   private static ParameterEgg[] eggs = new ParameterEgg[]
   {
      new ParameterEgg<Integer>("component", ParameterType.dependent, 0),
      new ParameterEgg<Float>("scale", ParameterType.dependent, 1.f),
      new ParameterEgg<Boolean>("zeroBased", ParameterType.independent, true),
      new ParameterEgg<Boolean>("adjusting", ParameterType.independent, false),
      new ParameterEgg<Integer>("type", ParameterType.dependent, CONSTANT),
      new ParameterEgg<float[]>("xRange", ParameterType.independent, null),
      new ParameterEgg<float[]>("yRange", ParameterType.independent, null),
      new ParameterEgg<Integer>("xyComponent", ParameterType.independent, -1),
      new ParameterEgg<Integer>("xComponent", ParameterType.independent, -1),
      new ParameterEgg<Integer>("yComponent", ParameterType.independent, -1),
      new ParameterEgg<Integer>("xyPosition", ParameterType.independent, CENTER),
      new ParameterEgg<Integer>("xPosition", ParameterType.independent, CENTER),
      new ParameterEgg<Integer>("yPosition", ParameterType.independent, CENTER)
   };

   public Params()
   {
      super(eggs);
      setValue("xRange", new float[]{0, 1});
      setValue("yRange", new float[]{0, 1});
   }

   public int getComponent()
   {
      return (Integer)getValue("component");
   }

   public void setComponent(int component)
   {
      setValue("component", component);
   }

   public int getType()
   {
      return  (Integer)getValue("type");
   }

   public void setType(int type)
   {
      setValue("type", type);
   }

   public int getXYComponent()
   {
      return  (Integer)getValue("xyComponent");
   }

   public void setXYComponent(int xyComponent)
   {
      setValue("xyComponent", xyComponent);
   }

   public int getXYPosition()
   {
      return  (Integer)getValue("xyPosition");
   }

   public void setXYPosition(int xyPosition)
   {
      setValue("xyPosition", xyPosition);
   }

   public int getXComponent()
   {
      return  (Integer)getValue("xComponent");
   }

   public void setXComponent(int xComponent)
   {
      setValue("xComponent", xComponent);
   }

   public int getXPosition()
   {
      return  (Integer)getValue("xPosition");
   }

   public void setXPosition(int xPosition)
   {
      setValue("xPosition", xPosition);
   }

   public int getYComponent()
   {
      return  (Integer)getValue("yComponent");
   }

   public void setYComponent(int yComponent)
   {
      setValue("yComponent", yComponent);
   }

   public int getYPosition()
   {
      return  (Integer)getValue("yPosition");
   }

   public void setYPosition(int yPosition)
   {
      setValue("yPosition", yPosition);
   }

   public float getScale()
   {
      return (Float)getValue("scale");
   }

   public void setScale(float scale)
   {
      setValue("scale", scale);
   }

   public float[] getXRange()
   {
      return (float[])getValue("xRange");
   }

   public void setXRange(float[] xRange)
   {
      setValue("xRange", xRange);
   }

   public float[] getYRange()
   {
      return (float[])getValue("yRange");
   }

   public void setYRange(float[] yRange)
   {
      setValue("yRange", yRange);
   }

   public void setZeroBased(boolean zeroBased)
   {
      setValue("zeroBased", zeroBased);
   }

   public boolean isZeroBased()
   {
      return (Boolean)getValue("zeroBased");
   }

   public boolean isAdjusting()
   {
      return (Boolean)getValue("adjusting");
   }

   public void setAdjusting(boolean adjusting)
   {
      setValue("adjusting", adjusting);
   }

   public boolean isRibbon()
   {
      return false;
   }

   public int getAxis()
   {
      return 0;
   }
}
