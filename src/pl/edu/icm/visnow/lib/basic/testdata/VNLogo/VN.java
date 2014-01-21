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

package pl.edu.icm.visnow.lib.basic.testdata.VNLogo;


import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import pl.edu.icm.visnow.datasets.CellArray;
import pl.edu.icm.visnow.datasets.CellSet;
import pl.edu.icm.visnow.datasets.IrregularField;
import pl.edu.icm.visnow.datasets.cells.Cell;
import pl.edu.icm.visnow.datasets.dataarrays.DataArray;
import pl.edu.icm.visnow.engine.core.OutputEgg;
import pl.edu.icm.visnow.geometries.objects.IrregularFieldGeometry;
import pl.edu.icm.visnow.lib.templates.visualization.modules.IrregularOutFieldVisualizationModule;
import pl.edu.icm.visnow.lib.types.VNGeometryObject;
import pl.edu.icm.visnow.lib.types.VNIrregularField;
import pl.edu.icm.visnow.lib.utils.SwingInstancer;

/**
 * @author Krzysztof Nowinski (know@icm.edu.pl) University of Warsaw,
 * Interdisciplinary Centre for Mathematical and Computational Modelling
 */
public class VN extends IrregularOutFieldVisualizationModule
{

   protected GUI computeUI = null;
   protected Params params;
   protected boolean fromUI = false;
   public static OutputEgg[] outputEggs = null;
   protected int level = 10;
   protected int nPoints = 4 + 10 * level;
   protected float aspect = .3f;
   protected float width = .3f;
   protected float distance = .3f;
   protected float[] coords = new float[3 * nPoints];
   protected float[] v = new float[3 * nPoints];
   protected float[] data = new float[nPoints];


   /**
    * Creates a new instance of TestGeometryObject
    */
   public VN()
   {
      params = new Params();
      params.addChangeListener(new ChangeListener()
      {
         @Override
         public void stateChanged(ChangeEvent evt)
         {
            if (params.isAdjusting())
            {
               params.setAdjusting(false);
               updateCoords();
               irregularFieldGeometry.updateCoords();
            }
            else
               startAction();
         }
      });
      SwingInstancer.swingRunAndWait(new Runnable()
      {
         @Override
         public void run()
         {
            computeUI = new GUI();
         }
      });
      ui.addComputeGUI(computeUI);
      setPanel(ui);
      computeUI.setParams(params);
   }

   @Override
   public boolean isGenerator() {
      return true;
   }

   private void updateCoords()
   {
      aspect = params.getAspect();
      width = params.getWidth();
      distance = params.getDistance();
      float d = (float)Math.PI / level;
      int k = 0;
      for (int i = 0; i <= 2 * level; i++, k += 2)
      {
         float cosPsi = (float) Math.cos(i * d);
         float sinPsi = (float) Math.sin(i * d);
         coords[3 * k]     = i * d * aspect;
         coords[3 * k + 3] = i * d * aspect + width;
         coords[3 * k + 1] = coords[3 * k + 4] = cosPsi;
         coords[3 * k + 2] = coords[3 * k + 5] = sinPsi;
      }
      for (int i = -level; i <= 2 * level; i++, k += 2)
      {
         float cosPsi = (float) Math.cos(i * d);
         float sinPsi = (float) Math.sin(i * d);
         coords[3 * k]     = i * d * aspect + distance + width;
         coords[3 * k + 3] = i * d * aspect + distance + 2 * width;
         coords[3 * k + 1] = coords[3 * k + 4] = cosPsi;
         coords[3 * k + 2] = coords[3 * k + 5] = sinPsi;
      }
      outField.setCoords(coords);
   }

   @Override
   public void onInitFinishedLocal()
   {
      irregularFieldGeometry = new IrregularFieldGeometry();
      outObj.addBgrColorListener(irregularFieldGeometry.getBackgroundColorListener());
      onActive();
   }

   public void createVNMesh()
   {
      level = params.getNPoints() / 5;
      nPoints = 4 + 10 * level;
      outField = new IrregularField(nPoints);
      outField.setNSpace(3);
      coords = new float[nPoints * 3];
      v = new float[nPoints * 3];
      data = new float[nPoints];
      float d = (float)Math.PI / level;
      int k = 0;
      for (int i = 0; i <= 2 * level; i++, k += 2)
      {
         float cosPsi = (float) Math.cos(i * d);
         float sinPsi = (float) Math.sin(i * d);
         v[3 * k]     = v[3 * k + 3] = 0;
         v[3 * k + 1] = cosPsi;
         v[3 * k + 4] = -cosPsi;
         v[3 * k + 2] = sinPsi;
         v[3 * k + 5] = -sinPsi;
         data[k] = data[k + 1] = 0;
      }
      for (int i = -level; i <= 2 * level; i++, k += 2)
      {
         float cosPsi = (float) Math.cos(i * d);
         float sinPsi = (float) Math.sin(i * d);
         v[3 * k]     = v[3 * k + 3] = 0;
         v[3 * k + 1] = cosPsi;
         v[3 * k + 4] = -cosPsi;
         v[3 * k + 2] = sinPsi;
         v[3 * k + 5] = -sinPsi;
         data[k] = data[k + 1] = 1;
      }
      updateCoords();
      outField.clearData();
      outField.addData(DataArray.create(data, 1, "z"));
      outField.setData(1, DataArray.create(v, 3, "v"));
      int nQuads = 5 * level;
      float[] data1 = {0,1};
      int[] cells = new int[4 * nQuads];
      int[] cellDataIndices = new int[nQuads];
      int l = 0;
      k = 0;
      for (int i = 0; i < 2 * level; i++, k++, l++)
      {
         cells[4 * k] =      2 * l;
         cells[4 * k + 1] =  2 * l + 1;
         cells[4 * k + 2] =  2 * l + 3;
         cells[4 * k + 3] =  2 * l + 2;
         cellDataIndices[k] = 0;
      }
      l += 1;
      for (int i = 0; i < 3 * level; i++, k++, l++)
      {
         cells[4 * k] =      2 * l;
         cells[4 * k + 1] =  2 * l + 1;
         cells[4 * k + 2] =  2 * l + 3;
         cells[4 * k + 3] =  2 * l + 2;
         cellDataIndices[k] = 1;
      }
      CellArray ca = new CellArray(Cell.QUAD, cells, null, cellDataIndices);
      CellSet cs = new CellSet();
      cs.setCellArray(ca);
      cs.addData(DataArray.create(data1, 1, "c"));
      cs.generateDisplayData(coords);
      outField.addCellSet(cs);
   }


   @Override
   public void onActive()
   {
      createVNMesh();
      prepareOutputGeometry();
      show();
//      Transform3D t = new Transform3D();
//      outObj.getGeometryObj().getLocalToVworld(t);
//      float[] trMat = new float[16];
//      t.get(trMat);
//      for (int i = 0; i < 4; i++)
//      {
//         System.out.printf("%6.3f %6.3f %6.3f   %6.3f%n",trMat[4*i],trMat[4*i+1],trMat[4*i+2],trMat[4*i+3]);
//         
//      }
      setOutputValue("outField", new VNIrregularField(outField));
   }
}
