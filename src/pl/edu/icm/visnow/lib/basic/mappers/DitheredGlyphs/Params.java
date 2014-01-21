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

package pl.edu.icm.visnow.lib.basic.mappers.DitheredGlyphs;

import javax.swing.event.ChangeEvent;
import pl.edu.icm.visnow.engine.core.ParameterEgg;
import pl.edu.icm.visnow.engine.core.ParameterType;
import pl.edu.icm.visnow.engine.core.Parameters;

/**
 *
 * @author  Krzysztof S. Nowinski, University of Warsaw, ICM
 */
public class Params extends Parameters
{   
   public static final int LINEAR = 0;
   public static final int SQR     = 1;
   public static final int LOG     = 2;
   public static final int SQRT    = 3;
   public static final int ATAN    = 4;
   public static final int COUNT_CHANGED  = 3;
   public static final int GLYPHS_CHANGED = 2;
   public static final int COORDS_CHANGED = 1;
   protected int change = 0;
   protected static ParameterEgg[] eggs = new ParameterEgg[]
   {
      new ParameterEgg<Integer>("component",          ParameterType.dependent, 0),
      new ParameterEgg<Integer>("thrComponent",       ParameterType.dependent, -1),
      new ParameterEgg<int[]>("down",                 ParameterType.dependent, null),
      new ParameterEgg<int[]>("lowCrop",              ParameterType.dependent, null),
      new ParameterEgg<int[]>("upCrop",               ParameterType.dependent, null),
      new ParameterEgg<Integer>("type",               ParameterType.independent, 0),
      new ParameterEgg<Boolean>("constant diam",      ParameterType.dependent, true),
      new ParameterEgg<Boolean>("constant thickness", ParameterType.dependent, true),
      new ParameterEgg<Float>("scale",                ParameterType.dependent, .1f),
      new ParameterEgg<Float>("density",              ParameterType.dependent, .1f),
      new ParameterEgg<Float>("thickness",            ParameterType.dependent, .1f),
      new ParameterEgg<Float>("line thickness",       ParameterType.dependent, .1f),
      new ParameterEgg<Float>("thr",                  ParameterType.dependent, .1f),
      new ParameterEgg<Float>("shake",                ParameterType.dependent, .0f),
      new ParameterEgg<Integer>("lod",                ParameterType.independent, 1),
      new ParameterEgg<Integer>("function",           ParameterType.independent, LINEAR)
   };

   public Params()
   {
      super(eggs);
      setValue("down", new int[] { 2, 2, 2});
      setValue("lowCrop", new int[] {0, 0, 0});
      setValue("upCrop", new int[] {1, 1, 1});
   }

   public int getComponent()
   {
      return (Integer)getValue("component");
   }

   public void setComponent(int component)
   {
      setValue("component",component);
      change = COUNT_CHANGED;
      fireStateChanged();
   }

   public int[] getDown()
   {
      return (int[])getValue("down");
   }

   public void setDown(int[] down)
   {
      setValue("down",down);
      change = COUNT_CHANGED;
      fireStateChanged();
   }

public int getLod()
   {
      return (Integer)getValue("lod");
   }

   public void setLod(int lod)
   {
      setValue("lod",lod);
      change = Math.max(change, GLYPHS_CHANGED);
      fireStateChanged();
   }

   public int[] getLowCrop()
   {
      return (int[])getValue("lowCrop");
   }

   public boolean isConstantDiam()
   {
      return (Boolean)getValue("constant diam");
   }

   public void setConstantDiam(boolean cDiam)
   {
      setValue("constant diam",cDiam);
      change = Math.max(change, COORDS_CHANGED);
      fireStateChanged();
   }

   public boolean isConstantThickness()
   {
      return (Boolean)getValue("constant thickness");
   }

   public void setConstantThickness(boolean constantThickness)
   {
      setValue("constant thickness",constantThickness);
      change = Math.max(change, COORDS_CHANGED);
      fireStateChanged();
   }

   public float getScale()
   {
      return (Float)getValue("scale");
   }

   public void setScale(float scale)
   {
      setValue("scale",scale);
      change = Math.max(change, COORDS_CHANGED);
      fireStateChanged();
   }

   public float getShake()
   {
      return (Float)getValue("shake");
   }

   public void setShake(float shake)
   {
      setValue("shake",shake);
      change = Math.max(change, COORDS_CHANGED);
      fireStateChanged();
   }

   public float getDensity()
   {
      return (Float)getValue("density");
   }

   public void setDensity(float density)
   {
      setValue("density",density);
      change = COUNT_CHANGED;
      fireStateChanged();
   }

    public float getThickness()
   {
      return (Float)getValue("thickness");
   }

   public void setThickness(float thickness)
   {
      setValue("thickness",thickness);
      change = Math.max(change, COORDS_CHANGED);
      fireStateChanged();
   }

    public float getLineThickness()
   {
      return (Float)getValue("line thickness");
   }

   public void setLineThickness(float thickness)
   {
      setValue("line thickness",thickness);
      change = Math.max(change, COORDS_CHANGED);
      fireStateChanged();
   }

   public float getThr()
   {
      return (Float)getValue("thr");
   }

   public void setThr(float thr)
   {
      setValue("thr",thr);
      change = COUNT_CHANGED;
      fireStateChanged();
   }

   public int getType()
   {
      return (Integer)getValue("type");
   }

   public void setType(int type)
   {
      setValue("type",type);
      change = Math.max(change, GLYPHS_CHANGED);
      fireStateChanged();
   }

   public int[] getUpCrop()
   {
      return (int[])getValue("upCrop");
   }

   public void setCrop(int[] lowCrop, int[] upCrop)
   {
      setValue("lowCrop",lowCrop);
      setValue("upCrop",upCrop);
      change = COUNT_CHANGED;
      fireStateChanged();
   }

   public int getFunction()
   {
      return (Integer)getValue("function");
   }

   public void setFunction(int function)
   {
      setValue("function",function);
      change = COUNT_CHANGED;
      fireStateChanged();
   }

   public int getChange()
   {
      return change;
   }

   public void setChange(int change)
   {
      this.change = change;
   }
   
   @Override
   public void fireStateChanged()
   {
      if (!active)
         return;
      ChangeEvent e = new ChangeEvent(this);
      for (int i = 0; i < changeListenerList.size(); i++) {
          changeListenerList.get(i).stateChanged(e);          
       }
   }
   
}
