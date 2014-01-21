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
exception statement from your version. */
//</editor-fold>

package pl.edu.icm.visnow.lib.basic.utilities.Annotations;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import pl.edu.icm.visnow.engine.core.ParameterEgg;
import pl.edu.icm.visnow.engine.core.ParameterType;
import pl.edu.icm.visnow.engine.core.Parameters;
import pl.edu.icm.visnow.geometries.parameters.FontParams;
import pl.edu.icm.visnow.geometries.viewer3d.eventslisteners.pick.Pick3DListener;

/**
 *
 * @author  Krzysztof S. Nowinski, University of Warsaw, ICM
 */
public class Params extends Parameters
{
   public static final int COUNT_CHANGED  = 3;
   public static final int GLYPHS_CHANGED = 2;
   public static final int COORDS_CHANGED = 1;
   public static final int GLYPHS2D       = 0;
   public static final int GLYPHS3DFLAT   = 1;
   public static final int GLYPHS3D       = 2;
   protected int change = 0;
   protected FontParams fontParams = new FontParams();
    /**
     * To be set by Annotations using setPick3DListener(). Needed to synchronize checkbox with
     * active or non-active state of the 3D pick listener.
     */
   protected Pick3DListener pick3DListener = null;
//
   protected static ParameterEgg[] eggs = new ParameterEgg[]
   {
      new ParameterEgg<Boolean>("active",             ParameterType.dependent, false),
      new ParameterEgg<float[]>("coords",             ParameterType.independent, null),
      new ParameterEgg<String[]>("texts",             ParameterType.independent, null),
      new ParameterEgg<String>("input",              ParameterType.independent, ""),
      new ParameterEgg<String>("output",              ParameterType.independent, "")
   };

   public Params()
   {
      super(eggs);
      setValue("coords", new float[0]);
      setValue("texts", new String[0]);
      fontParams.addChangeListener(new ChangeListener()
      {
         @Override
         public void stateChanged(ChangeEvent e)
         {
            change = GLYPHS_CHANGED;
            fireStateChanged();
         }
      });
   }

   public FontParams getFontParams()
   {
      return fontParams;
   }


   public String getInput()
   {
      return (String)getValue("input");
   }

   public void setInput(String input)
   {
      setValue("input", input);
      if (!input.isEmpty())
         fireStateChanged();
   }

   public String getOutput()
   {
      return (String)getValue("output");
   }

   public void setOutput(String output)
   {
      setValue("output", output);
      if (!output.isEmpty())
         fireStateChanged();
   }

   public String[] getTexts()
   {
      return (String[])getValue("texts");
   }

   public void setTexts(String[] texts)
   {
      setValue("texts", texts);
   }

   public float[] getCoords()
   {
      return (float[])getValue("coords");
   }

   public void setCoords(float[] coords)
   {
      setValue("coords", coords);
   }

   public int getChange()
   {
      return change;
   }

   public void setChange(int change)
   {
      this.change = change;
   }

    public void setPick3DListener(Pick3DListener pick3DListener) {
        this.pick3DListener = pick3DListener;
    }

    @Override
    public Pick3DListener getPick3DListener() {
        return pick3DListener;
    }

}
