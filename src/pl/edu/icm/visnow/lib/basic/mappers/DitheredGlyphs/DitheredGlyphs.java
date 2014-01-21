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


import javax.media.j3d.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import pl.edu.icm.visnow.datamaps.ColorMap;
import pl.edu.icm.visnow.datasets.RegularField;
import pl.edu.icm.visnow.datasets.dataarrays.DataArray;
import pl.edu.icm.visnow.engine.core.InputEgg;
import pl.edu.icm.visnow.engine.core.OutputEgg;
import pl.edu.icm.visnow.geometries.geometryTemplates.Glyph;
import pl.edu.icm.visnow.geometries.geometryTemplates.ScalarGlyphTemplates;
import pl.edu.icm.visnow.geometries.geometryTemplates.VectorGlyphTemplates;
import pl.edu.icm.visnow.geometries.objects.generics.*;
import pl.edu.icm.visnow.geometries.utils.ColorMapper;
import pl.edu.icm.visnow.geometries.viewer3d.eventslisteners.render.RenderEvent;
import pl.edu.icm.visnow.geometries.viewer3d.eventslisteners.render.RenderEventListener;
import pl.edu.icm.visnow.lib.templates.visualization.modules.VisualizationModule;
import pl.edu.icm.visnow.lib.types.VNRegularField;
import pl.edu.icm.visnow.lib.utils.SwingInstancer;

/**
 * @author  Krzysztof S. Nowinski (know@icm.edu.pl)
 * Warsaw University, Interdisciplinary Centre
 * for Mathematical and Computational Modelling
 */
public class DitheredGlyphs extends VisualizationModule
{

   /**
    * Creates a new instance of CreateGrid
    */
   public static InputEgg[] inputEggs = null;
   public static OutputEgg[] outputEggs = null;
   protected GUI ui = null;
   protected Params params;
   protected RegularField inField = null;
   protected float lowv = 0;
   protected float dv = 0;
   protected OpenBranchGroup outGroup = null;
   
   protected DataArray data;
   protected float[] vals = null;
   protected float[] baseCoords = null;
   protected float[] baseU = null;
   protected float[] baseV = null;
   protected float[] baseW = null;
   protected int nGlyphs, nstrip, nvert, nind, ncol;
   protected boolean isNormals = false;
   protected int[] glyphIn = null;
   protected int[] cIndex = null;
   protected int[] pIndex = null;
   protected int[] strips = null;
   protected float[] verts = null;
   protected float[] normals = null;
   protected byte[] colors = null;
   protected Glyph gt = null;
   protected IndexedGeometryStripArray surf = null;
   protected OpenAppearance appearance = new OpenAppearance();
   protected OpenLineAttributes lattr = new OpenLineAttributes(1.f, OpenLineAttributes.PATTERN_SOLID, true);
   protected ColorMap colorMap = null;
   
   private boolean fromUI = false;
   private boolean fromIn = false;
   
   public DitheredGlyphs()
   {
      parameters = params = new Params();
      params.addChangeListener(new ChangeListener()
      {
         public void stateChanged(ChangeEvent evt)
         {
            if (fromIn)
               return;
            fromUI = true;
            if (inField != null)
               update();
         }
      });
      dataMappingParams.addRenderEventListener(new RenderEventListener() 
      {
         public void renderExtentChanged(RenderEvent e)
         {
            if (!fromIn && inField != null)
               updateColors();
         }
      });
      renderingParams.addRenderEventListener(new RenderEventListener() 
      {
         public void renderExtentChanged(RenderEvent e)
         {
            if (!fromIn && inField != null)
               updateColors();
         }
      });
      SwingInstancer.swingRunAndWait(new Runnable()
      {
         public void run()
         {
            ui = new GUI();
            ui.setParams(params);
            ui.getDataMappingGUI().setRenderingParams(renderingParams);
            setPanel(ui);
         }
      });
   }
   
   private void prepareGlyphCount()
   {
      if (inField == null)
         return;
      data = inField.getData(params.getComponent());
      if (data == null || data.getVeclen() != 1)
         return;
      nGlyphs = 0;
      int n = 1;
      int[] dims = inField.getDims();
      int ndims = dims.length;
      int[] low = params.getLowCrop();
      int[] up = params.getUpCrop();
      int[] down = params.getDown();
      
// creation of downsized transient data array      
      int[] ddims = new int[ndims];
      for (int i = 0; i < ndims; i++)
      {
         ddims[i] = (up[i] - low[i] - 1) / down[i] + 1;
         n *= ddims[i] + 2;
      }
      vals = new float[n];
      float[] v = data.getFData();
      switch (ndims)
      {
         case 3:
            for (int i = low[2], ii = 1; i < up[2]; i+=down[2], ii++)
               for (int j = low[1], jj = 1; j < up[1]; j+= down[1], jj++)
                  for (int k = low[0], 
                           l = (dims[1] * i + j) * dims[0] + low[0], 
                           kk = ((ii * (ddims[1] + 2)) + jj) * (ddims[0] + 2) + 1; 
                       k < up[0]; 
                       k += down[0], l += down[0], kk++)
                     vals[kk] = v[l];
            break;
         case 2:
            for (int j = low[1], jj = 1; j < up[1]; j+= down[1], jj++)
               for (int k = low[0], 
                        l = j * dims[0] + low[0],
                        kk = jj * (ddims[0] + 2) + 1; 
                   k < up[0]; 
                   k += down[0], l += down[0], kk++)
                  vals[kk] = v[l];
            break;
         case 1:
            for (int k = low[0], m = 1; k < up[0]; k += down[0], m ++)
               vals[m] = v[k];
            break;
      }
      float min = data.getMinv();
      float max = data.getMaxv();
      if (max <= min)
         max = min + 1;
      float d = 1/(max - min);
      if (data.getType() != DataArray.FIELD_DATA_FLOAT)
         v = null;
      
// data normalization to <0,1>      
      switch (params.getFunction())
      {
         case Params.LINEAR:
            for (int i = 0; i < n; i++)
               vals[i] = (vals[i] - min) * d;
            break;
         case Params.SQR:
            if (-min > max) max = min;
            d = 1 / (max * max);
            for (int i = 0; i < n; i++)
               vals[i] = vals[i] * vals[i] * d ;
            break;
         case Params.SQRT:
            d = 1 / max;
            for (int i = 0; i < n; i++)
               vals[i] = (vals[i] < 0 ? 0 : (float)Math.sqrt(vals[i] * d));
            break;
         case Params.LOG:
            min = max * (float)Math.exp(-10.);
            d   = (float)Math.log(min);
            for (int i = 0; i < n; i++)
               vals[i] = (vals[i] < min ? 0 : (float)Math.log(vals[i]) - d);
            break;
         case Params.ATAN:
            for (int i = 0; i < n; i++)
               vals[i] = (vals[i] < 0 ? 0 : (float)(Math.atan(vals[i]) * 2 / Math.PI));
            break;
      }
       for (int i = 0; i < n; i++)
          vals[i] *= params.getDensity();
      int[] gl = new int[inField.getNNodes()];
      for (int i = 0; i < gl.length; i++)
         gl[i] = -1;
      nGlyphs = 0;
      surf = null;
      float w1 = 0, w2 = 0, w3 = 0;
      int off1 = 1, off2 = 1;
      int[] off;
      float[] w;
      float err = 0;
      switch (dims.length)
      {
      case 3:
         w1 = (float)(1 / (3 + 6 / Math.sqrt(2) + 4 / Math.sqrt(3)));
         w2 = (float)(w1 / Math.sqrt(2));
         w3 = (float)(w1 / Math.sqrt(3));
         off1 = ddims[0] + 2;
         off2 = (ddims[1] + 2) * off1;
         off = new int[]{                               1, 
                   off1 - 1,        off1,        off1 + 1, 
            off2 - off1 - 1, off2 - off1, off2 - off1 + 1,
            off2        - 1, off2,        off2        + 1,
            off2 + off1 - 1, off2 + off1, off2 + off1 + 1};
         w = new float[]{        w1,
                         w2, w1, w2,
                         w3, w2, w3,
                         w2, w1, w2,
                         w3, w2, w3};
         for (int i = 1; i <= ddims[2]; i++)
            for (int j = 1; j <= ddims[1]; j++)
               for (int k = 1, 
                        l = (((low[2] + (i - 1) * down[2]) * dims[1]) + (low[1] + (j - 1) * down[1])) * dims[0] + low[0],
                        m = ((i * (ddims[1] + 2)) + j) * (ddims[0] + 2) + 1; 
                        k <= ddims[0]; 
                        k++, l += down[0], m++)
               {
                  if (vals[m] > .5)
                  {
                     gl[nGlyphs] = l;
                     nGlyphs += 1;
                     err = vals[m] - 1;
                  }
                  else
                     err = vals[m];
                  for (int p = 0; p < off.length; p++)
                     vals[m + off[p]] += err * w[p];
               }
         break;
      case 2:
         w1 = (float)(1 / (2 + Math.sqrt(2)));
         w2 = (float)(w1 / Math.sqrt(2));
         off1 = ddims[0] + 2;
         off = new int[]{                         1, 
                   off1 - 1,        off1,        off1 + 1};
         w = new float[]{w1,
                         w2, w1, w2};
         for (int j = 1; j <= ddims[1]; j++)
            for (int k = 1,  
                     l = (low[1] + (j - 1) * down[1]) * dims[0] + low[0],
                     m = j * (ddims[0] + 2) + 1; 
                     k <= ddims[0]; 
                     k++, l += down[0], m++)
            {
               if (vals[m] > .5)
                  {
                     gl[nGlyphs] = l;
                     nGlyphs += 1;
                     err = vals[m] - 1;
                  }
                  else
                     err = vals[m];
                  for (int p = 0; p < off.length; p++)
                     vals[m + off[p]] += err * w[p];
            }
         break;
      case 1:
         for (int k = low[0], m = 0, l = low[0]; k < up[0]; k += down[0], l += down[0], m++)
//            if (isValid(l))
            {
               gl[nGlyphs] = l;
               nGlyphs += 1;
            }
      }
      glyphIn = new int[nGlyphs];
      System.arraycopy(gl, 0, glyphIn, 0, nGlyphs);
      colors = new byte[3 * nGlyphs];
      baseCoords = new float[3 * nGlyphs];
      for (int i = 0; i < baseCoords.length; i++)
         baseCoords[i] = 0;
      if (inField.getCoords() != null)
      {
         int nSp = inField.getNSpace();
         float[] fldCoords = inField.getCoords();
         for (int i = 0; i < nGlyphs; i++)
            for (int j = 0; j < nSp; j++)
               baseCoords[3 * i + j] = fldCoords[nSp * glyphIn[i] + j];
      }
      else 
      {
         float[][] inAff = ((RegularField)inField).getAffine();
         int i0 = 0, i1 = 0, i2 = 0;
         for (int i = 0; i < nGlyphs; i++)
         {
            int j = glyphIn[i];
            i0 = j % dims[0];
            if (dims.length > 1)
            {
               j /= dims[0];
               i1 = j % dims[1];
               if (dims.length > 2)
                  i2 = j/dims[1];
            }
            for (int k = 0; k < 3; k++)
               baseCoords[3 * i + k] = inAff[3][k] + i0 * inAff[0][k] + i1 * inAff[1][k] + i2 * inAff[2][k];
         }
      }
   }
   
   public void prepareStructure()
   {
      if (nGlyphs < 1)
         return;
      data = inField.getData(params.getComponent());
      if (data == null || (data.getVeclen() != 3 && data.getVeclen() != 1))
         return;
      int type = params.getType();
      int lod = params.getLod();
      if (data.getVeclen() == 1)
         gt = ScalarGlyphTemplates.glyph(type, lod);
      else
         gt = VectorGlyphTemplates.glyph(type, lod);
      nstrip = nGlyphs * gt.getNstrips();
      nvert = nGlyphs * gt.getNverts();
      nind = nGlyphs * gt.getNinds();
      ncol = nGlyphs;
      strips = new int[nstrip];
      verts = new float[3 * nvert];
      normals = new float[3 * nvert];
      pIndex = new int[nind];
      cIndex = new int[nind];
      if (outGroup != null)
         outGroup.detach();
      outGroup = new OpenBranchGroup();
      makeIndices();
      if (verts == null || verts.length != 3 * nGlyphs * gt.getNverts())
         verts = new float[3 * nGlyphs * gt.getNverts()];
      if (isNormals && (normals == null || normals.length != 3 * nGlyphs *gt.getNverts() ))
         normals = new float[3 * nGlyphs * gt.getNverts()];
      switch (gt.getType())
         {
         case Glyph.TRIANGLE_STRIPS:
            isNormals = true;
            surf = new IndexedTriangleStripArray(nvert,
                    GeometryArray.COORDINATES | GeometryArray.NORMALS | GeometryArray.COLOR_4,
                    nind, strips);
            break;
         case Glyph.TRIANGLE_FANS:
            isNormals = true;
            surf = new IndexedTriangleFanArray(nvert,
                    GeometryArray.COORDINATES | GeometryArray.NORMALS | GeometryArray.COLOR_4,
                    nind, strips);
            break;
         case Glyph.LINE_STRIPS:
            isNormals = false;
            surf = new IndexedLineStripArray(nvert,
                    GeometryArray.COORDINATES | GeometryArray.COLOR_4,
                    nind, strips);
            break;
         }
      surf.setCapability(IndexedLineStripArray.ALLOW_COLOR_READ);
      surf.setCapability(IndexedLineStripArray.ALLOW_COLOR_WRITE);
      surf.setColorIndices(0, cIndex);
      surf.setCapability(IndexedLineStripArray.ALLOW_COUNT_READ);
      surf.setCapability(IndexedLineStripArray.ALLOW_FORMAT_READ);
      surf.setCapability(IndexedLineStripArray.ALLOW_COORDINATE_INDEX_READ);
      surf.setCapability(IndexedLineStripArray.ALLOW_COORDINATE_READ);
      surf.setCapability(IndexedLineStripArray.ALLOW_COORDINATE_WRITE);
      surf.setCoordinates(0, verts);
      surf.setCoordinateIndices(0, pIndex);
      if (isNormals)
      {
         surf.setCapability(IndexedLineStripArray.ALLOW_NORMAL_READ);
         surf.setCapability(IndexedLineStripArray.ALLOW_NORMAL_WRITE);
         surf.setNormals(0, normals);
         surf.setNormalIndices(0, pIndex);
      }
      OpenAppearance app = new OpenAppearance();      
      OpenMaterial mat = new OpenMaterial();
      mat.setShininess(15.f);
      mat.setColorTarget(OpenMaterial.AMBIENT_AND_DIFFUSE);
      app.setMaterial(mat);
      PolygonAttributes pattr = new PolygonAttributes(
              PolygonAttributes.POLYGON_FILL,
              PolygonAttributes.CULL_NONE, 0.f, true);
      app.setPolygonAttributes(pattr);
      OpenColoringAttributes colAttrs = new OpenColoringAttributes();
      colAttrs.setColor(renderingParams.getDiffuseColor());
      app.setColoringAttributes(colAttrs);
      app.setLineAttributes(lattr);
      OpenShape3D surfaces = new OpenShape3D();
      surfaces.setCapability(Shape3D.ENABLE_PICK_REPORTING);
      surfaces.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
      surfaces.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
      surfaces.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
      surfaces.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
      surfaces.setCapability(Geometry.ALLOW_INTERSECT);
      surfaces.setCapability(Node.ALLOW_LOCAL_TO_VWORLD_READ);
      updateColors();
      surfaces.setAppearance(app);
      surfaces.addGeometry(surf);
      outGroup.addChild(surfaces);
      outObj.addNode(outGroup);
      outObj.setExtents(inField.getExtents());
   }
   
   public void updateColors()
   {
      appearance.getMaterial().setAmbientColor(renderingParams.getAmbientColor());
      appearance.getMaterial().setDiffuseColor(renderingParams.getDiffuseColor());
      appearance.getMaterial().setSpecularColor(renderingParams.getSpecularColor());
      appearance.getColoringAttributes().setColor(renderingParams.getDiffuseColor());
      colors = ColorMapper.mapColorsIndexed(inField, dataMappingParams, glyphIn, renderingParams.getDiffuseColor(), colors);
      surf.setColors(0, colors);
   }
   
   private void updateCoords()
   {
      lattr.setLineWidth(params.getLineThickness());
      float scale = params.getScale();
      float sh = scale * params.getShake();
      float s = 0;
      float[] tVerts = gt.getVerts();
      float[] tNorms = gt.getNormals();
      float[] p = new float[3];
      s = scale;
      for (int i = 0, k = 0; i < nGlyphs; i++)
      {
         System.arraycopy(baseCoords, 3 * i, p, 0, 3);
         for (int j = 0; j < p.length; j++)
            p[j] += sh * (float)(Math.random() - .5);
         if (isNormals)
            for (int j = 0, m = 0; j < tVerts.length / 3; j++)
               for (int l = 0; l < 3; l++, k++, m++)
               {
                  verts[k] = p[l] + s * tVerts[m];
                  normals[k] = tNorms[m];
               }
         else
            for (int j = 0, m = 0; j < tVerts.length / 3; j++)
               for (int l = 0; l < 3; l++, k++, m++)
                  verts[k] = p[l] + s * tVerts[m];
      }
      surf.setCoordinates(0, verts);
      if (isNormals)
         surf.setNormals(0, normals);
   }

   public void update()
   {
      if (params.getChange() == Params.COUNT_CHANGED)
         prepareGlyphCount();
      if (nGlyphs < 1)
         return;
      if (params.getChange() >= Params.GLYPHS_CHANGED)
         prepareStructure();
      if (params.getChange() >= Params.COORDS_CHANGED)
         updateCoords();
      updateColors();
      params.setChange(0);
   }

   protected void makeIndices()
   {
      int istrip = 0, iind = 0, ivert = 0, icol = 0;
      for (int n = 0; n < nGlyphs; n++)
      {
         for (int i = 0; i < gt.getNstrips(); i++, istrip++)
            strips[istrip] = gt.getStrips()[i];
         for (int i = 0; i < gt.getNinds(); i++, iind++)
         {
            pIndex[iind] = ivert + gt.getPntsIndex()[i];
            cIndex[iind] = icol;
         }
         ivert += gt.getNverts();
         icol += 1;
      }
   }

   @Override
   public void onActive()
   {
      RegularField inFld = ((VNRegularField) getInputFirstValue("inField")).getField();
      if (inField != inFld)
      {
          fromUI = false;
          fromIn = true;
      }       
      if (!fromUI)
      {
         fromIn = true;
         if (getInputFirstValue("inField") == null)
            return;
         if (inFld == null)
            return;
         
         inField = inFld;
         ui.setInData(inField, dataMappingParams);
         
         params.setChange(Params.COUNT_CHANGED);
         fromIn = false;
      }
      update();
      fromUI = false;
   }
}
