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

import javax.media.j3d.*;
import pl.edu.icm.visnow.geometries.objects.DataMappedGeometryObject;
import pl.edu.icm.visnow.geometries.objects.GeometryObject;
import pl.edu.icm.visnow.geometries.objects.generics.OpenBranchGroup;
import pl.edu.icm.visnow.geometries.parameters.FontParams;
import pl.edu.icm.visnow.geometries.utils.Texts2D;
import pl.edu.icm.visnow.geometries.utils.Texts3D;
import pl.edu.icm.visnow.geometries.utils.transform.LocalToWindow;
import pl.edu.icm.visnow.geometries.viewer3d.Display3DPanel;
import pl.edu.icm.visnow.lib.utils.geometry2D.CXYZString;

/**
 *
 ** @author Krzysztof S. Nowinski, University of Warsaw ICM
 */
public class AnnotationsObject extends DataMappedGeometryObject
{
   private CXYZString[] glyphs = null;
   private Params params;
   private FontParams fontParams;
   private OpenBranchGroup outGroup = null;

   public AnnotationsObject()
   {
      name = "text glyphs";
   }

   @Override
   public void drawLocal2D(J3DGraphics2D vGraphics, LocalToWindow ltw, int width, int height)
   {
      if (ltw == null || params == null || glyphs == null || glyphs.length < 1)
         return;
      for (int i = 0; i < glyphs.length; i++)
      {
         if (glyphs[i] != null && glyphs[i].getString() != null && !glyphs[i].getString().isEmpty())
         {
            glyphs[i].update(ltw);
            glyphs[i].draw(vGraphics, width, height);
         }
      }
   }

   public void prepareGlyphs()
   {
      Display3DPanel panel = getCurrentViewer();
      if (panel == null)
          return;
      
      fontParams.createFontMetrics(localToWindow,
                                   panel.getWidth(),
                                   panel.getHeight());
      if (outGroup != null)
         outGroup.detach();
      outGroup = null;
      glyphs = null;
      if (params.getTexts() == null)
         return;
      if (fontParams.isThreeDimensional())
      {
         outGroup = new Texts3D(params.getCoords(), params.getTexts(), fontParams);
         addNode(outGroup);
         setExtents(((Texts3D)outGroup).getExtents());
      } else
      {
         Texts2D texts2D = new Texts2D(params.getCoords(), params.getTexts(), fontParams);
         glyphs = texts2D.getGlyphs();
         geometryObj.getCurrentViewer().refresh();
      }
   }

   public void update(Params params)
   {
      this.params = params;
      fontParams = params.getFontParams();
      prepareGlyphs();
      params.setChange(0);
   }
   
   /*
   @Override
   public void setCurrentViewer(Display3DPanel panel) {
       this.myViewer = panel;
       outObj.setCurrentViewer(panel);

       for(GeometryObject child : geomChildren) {
           child.setCurrentViewer(panel);
       }
       
      this.params = params;
      fontParams = params.getFontParams();
      prepareGlyphs();
      params.setChange(0);
   } */
}
