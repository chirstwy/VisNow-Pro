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

package pl.edu.icm.visnow.lib.types;

import pl.edu.icm.visnow.geometries.objects.GeometryObject;
import pl.edu.icm.visnow.geometries.objects.SignalingTransform3D;
import pl.edu.icm.visnow.lib.utils.geometry2D.GeometryObject2DStruct;

/**
 * @author  Krzysztof S. Nowinski (know@icm.edu.pl)
 * Warsaw University, Interdisciplinary Centre
 * for Mathematical and Computational Modelling
 */

public class VNGeometryObject implements VNGeometryTransform

{
    GeometryObject geoObj = null;
    GeometryObject2DStruct geoObj2D = null;
    
    /**
    * Creates a new instance of VNGeometryObject
    */
    public VNGeometryObject() {
    }
    
    public VNGeometryObject(GeometryObject inObject) {
        geoObj = inObject;
    }

    public VNGeometryObject(GeometryObject inObject, GeometryObject2DStruct inObject2D) {
        geoObj = inObject;
        geoObj2D = inObject2D;
    }

    public VNGeometryObject(GeometryObject2DStruct inObject2D) {
        geoObj2D = inObject2D;
    }
    
    public GeometryObject getGeometryObject() {
        return geoObj;
    }

    public GeometryObject2DStruct getGeometryObject2DStruct() {
        return geoObj2D;
    }
    
   public SignalingTransform3D getTransform()
   {
      if (geoObj == null)
         return null;
      return geoObj.getCurrentTransform();
   }

   public void setTransform(SignalingTransform3D transform)
   {
      if (geoObj != null)
         geoObj.setCurrentTransform(transform);
   }

   public SignalingTransform3D getCurrentTransform()
   {
      if (geoObj == null)
         return null;
      return geoObj.getCurrentTransform();
   }

   public void setCurrentTransform(SignalingTransform3D transform)
   {
      if (geoObj != null)
         geoObj.setCurrentTransform(transform);
   }   
}
