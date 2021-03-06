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

package pl.edu.icm.visnow.geometries.viewer3d.lights;

import javax.media.j3d.Bounds;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Light;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

/**
 *
 * @author Krzysztof S. Nowinski
 * University of Warsaw, ICM
 */
public class EditableDirectionalLight extends EditableLight
{
   protected Vector3f lightDirection    = new Vector3f(1.f,0.8f,-.5f);
   protected Vector3f backDirection     = new Vector3f(-1.f,-0.8f,.5f);
   protected DirectionalLight light     = new DirectionalLight(lightColor, lightDirection);
   protected DirectionalLight backLight = new DirectionalLight(lightColor, backDirection);
   protected boolean biDirectional = false;

   public EditableDirectionalLight(Color3f  lightCol, Vector3f lightDir, Bounds bounds,
                        String name, boolean enabled, boolean biDirectional)
   {
      type = DIRECTIONAL;
      lightName = name;
      light.setCapability(Light.ALLOW_STATE_WRITE);
      light.setCapability(Light.ALLOW_COLOR_WRITE);
      light.setCapability(DirectionalLight.ALLOW_DIRECTION_WRITE);
      backLight.setCapability(Light.ALLOW_STATE_WRITE);
      backLight.setCapability(Light.ALLOW_COLOR_WRITE);
      backLight.setCapability(DirectionalLight.ALLOW_DIRECTION_WRITE);
      setLightColor(lightCol);
      setLightDirection(lightDir);
      light.setInfluencingBounds(bounds);
      backLight.setInfluencingBounds(bounds);
      setEnabled(enabled);
      setBiDirectional(biDirectional);
   }

   public final void setBiDirectional(boolean biDirectional)
   {
      this.biDirectional = biDirectional;
      if (enabled)
         backLight.setEnable(biDirectional);
   }

   public final void setEnabled(boolean enabled)
   {
      this.enabled = enabled;
      light.setEnable(enabled);
      backLight.setEnable(biDirectional && enabled);

   }

   public final void setLightColor(Color3f lightCol)
   {
      lightColor = lightCol;
      light.setColor(lightColor);
      backLight.setColor(lightColor);
   }

   public final void setLightDirection(Vector3f lightDir)
   {
      lightDirection = lightDir;
      backDirection  = new Vector3f(lightDir);
      backDirection.scale(-1);
      light.setDirection(lightDirection);
      backLight.setDirection(backDirection);
   }

   public DirectionalLight getBackLight()
   {
      return backLight;
   }

   public DirectionalLight getLight()
   {
      return light;
   }

   public boolean isBiDirectional()
   {
      return biDirectional;
   }
   
   public int getState()
   {
      if (enabled && biDirectional)
         return 2;
      if (enabled)
         return 1;
      return 0;
   }
   
   public void setState(int state)
   {
      setEnabled(state > 0);
      setBiDirectional(state == 2);
   }

}
