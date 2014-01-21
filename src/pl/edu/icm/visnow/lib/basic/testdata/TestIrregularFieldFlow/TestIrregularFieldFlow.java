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

package pl.edu.icm.visnow.lib.basic.testdata.TestIrregularFieldFlow;

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
 * @author Krzysztof S. Nowinski (know@icm.edu.pl) Warsaw University,
 * Interdisciplinary Centre for Mathematical and Computational Modelling
 */
public class TestIrregularFieldFlow extends IrregularOutFieldVisualizationModule
{

   /**
    * Creates a new instance of TestGeometryObject
    */
   protected GUI computeUI = null;
   public static OutputEgg[] outputEggs = null;

   public TestIrregularFieldFlow()
   {
      SwingInstancer.swingRunAndWait(new Runnable()
      {

         @Override
         public void run()
         {
            computeUI = new GUI();
         }
      });
      computeUI.addChangeListener(new ChangeListener()
      {

         @Override
         public void stateChanged(ChangeEvent evt)
         {
            startAction();
         }
      });
      ui.addComputeGUI(computeUI);
      setPanel(ui);
   }

   @Override
   public boolean isGenerator() {
      return true;
   }

   private void createTestField(int nPoints, int nTimeFrames)
   {
      int nnPoints = 2 * nPoints;

      outField = new IrregularField(nnPoints);
      outField.setNSpace(3);

      float[] coords = new float[3 * nnPoints * nTimeFrames];
      int[] data = new int[nnPoints];
      String[] texts = new String[nnPoints];
      int[] cells = new int[nnPoints];
      boolean[] orient = new boolean[nnPoints];
      for (int i = 0; i < data.length; i++)
      {
         data[i] = i + 1;
         texts[i] = "point " + i;
         cells[i] = i;
         orient[i] = true;
      }


      //random inital positions
      for (int i = 0; i < nnPoints; i++)
      {
         if (i % 2 == 1)
            for (int m = 0; m < 3; m++)
               coords[3 * i + m] = (float) Math.random();
      }
      for (int i = 0; i < nnPoints; i++)
      {
         if (i % 2 == 0)
            for (int m = 0; m < 3; m++)
               coords[3 * i + m] = 0;
         //coords[3*i + m] = coords[3*(i+1) + m];
      }



      int nSplit = nTimeFrames / 3;

      for (int n = 1; n < nTimeFrames; n++)
      {

         for (int i = 0; i < nnPoints; i++)
         {
            if (i % 2 == 1)
            {
               coords[n * nnPoints * 3 + i * 3] = coords[i * 3] + (float) Math.sin(14.0f * i * n / (nTimeFrames * nnPoints));
               coords[n * nnPoints * 3 + i * 3 + 1] = coords[(n - 1) * nnPoints * 3 + i * 3 + 1] + 1.0f / nnPoints;
               coords[n * nnPoints * 3 + i * 3 + 2] = coords[i * 3 + 2] + (float) Math.sin(7.0f * i * n / (nTimeFrames * nnPoints));
            }
         }

         for (int i = 0; i < nnPoints; i++)
         {
            if (i % 2 == 0)
            {
               if (n < nSplit)
               {
                  coords[n * nnPoints * 3 + i * 3] = 0;
                  coords[n * nnPoints * 3 + i * 3 + 1] = 0;
                  coords[n * nnPoints * 3 + i * 3 + 2] = 0;

//                        coords[n * nnPoints * 3 + i*3    ] = coords[n * nnPoints * 3 + (i+1)*3    ];
//                        coords[n * nnPoints * 3 + i*3 + 1] = coords[n * nnPoints * 3 + (i+1)*3 + 1];
//                        coords[n * nnPoints * 3 + i*3 + 2] = coords[n * nnPoints * 3 + (i+1)*3 + 2];
               } else
               {
                  int l = (nSplit) * nnPoints * 3 + (i + 1) * 3;
                  //int l = (nSplit - 1)*nnPoints*3 + i*3;
                  coords[n * nnPoints * 3 + i * 3] = coords[l] + (float) Math.sin(14.0f * i * (n - nSplit) / (nTimeFrames * nnPoints));
                  coords[n * nnPoints * 3 + i * 3 + 1] = coords[l + 1] + (n - nSplit) * 0.5f / nnPoints;
                  coords[n * nnPoints * 3 + i * 3 + 2] = coords[l + 2] + (float) Math.sin(7.0f * i * (n - nSplit) / (nTimeFrames * nnPoints));
               }
            }
         }


      }
      CellArray ca = new CellArray(Cell.POINT, cells, orient, null);
      CellSet cs = new CellSet();
      cs.setCellArray(ca);
      outField.addCellSet(cs);
      outField.setCoords(coords);
      outField.addData(DataArray.create(data, 1, "points"));
      outField.addData(DataArray.create(texts, 1, "texts"));

      boolean[] valid = new boolean[nnPoints * nTimeFrames];
      for (int n = 0, l = 0; n < nTimeFrames; n++)
      {
         for (int i = 0; i < nnPoints; i++, l++)
         {
            valid[l] = (n >= nSplit || i % 2 == 1);
         }
      }
      outField.setMask(valid);

   }

   @Override
   public void onInitFinishedLocal()
   {
      irregularFieldGeometry = new IrregularFieldGeometry();
      outObj.addBgrColorListener(irregularFieldGeometry.getBackgroundColorListener());
      createTestField(computeUI.getNParticles(), computeUI.getNTimeFrames());
      prepareOutputGeometry();
      show();
      setOutputValue("outField", new VNIrregularField(outField));
   }

   @Override
   public void onActive()
   {
      try
      {
         createTestField(computeUI.getNParticles(), computeUI.getNTimeFrames());
         prepareOutputGeometry();
         show();
         setOutputValue("outField", new VNIrregularField(outField));
      } catch (Exception ex)
      {
      }
   }
}
