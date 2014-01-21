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

package pl.edu.icm.visnow.lib.basic.testdata.TestRegularField1D;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import pl.edu.icm.visnow.datasets.RegularField;
import pl.edu.icm.visnow.engine.core.OutputEgg;
import pl.edu.icm.visnow.datasets.dataarrays.DataArray;
import pl.edu.icm.visnow.lib.templates.visualization.modules.OutFieldVisualizationModule;
import pl.edu.icm.visnow.lib.types.VNRegularField;
import pl.edu.icm.visnow.lib.utils.SwingInstancer;

/**
 * @author  Krzysztof S. Nowinski (know@icm.edu.pl)
 * Warsaw University, Interdisciplinary Centre
 * for Mathematical and Computational Modelling
 */
public class TestRegularField1D extends OutFieldVisualizationModule
{

   public static OutputEgg[] outputEggs = null;

   private final double[][] d = new double[25][5];
  /**
    * Creates a new instance of TestGeometryObject
    */
   protected GUI computeUI = null;
   public TestRegularField1D()
   {
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
      computeUI.addChangeListener(new ChangeListener()
      {
         @Override
         public void stateChanged(ChangeEvent evt)
         {
            startAction();
         }
      });
   }

   @Override
   public boolean isGenerator() {
      return true;
   }

   private void generateField()
   {
      float[] data0, data1, data2, coords;
      int n = computeUI.getResolution();
      if (n < 20)
         n = 20;
      for (double[] d1 : d)
      {
         d1[0] = 2 * Math.random() - 1;
         d1[1] = .05 * (Math.random() + 1.2);
         d1[2] = .2 * Math.random() + .2;
      }
      int[] dims = new int[]{n};
      outField = new RegularField(dims);
      outRegularField = (RegularField)outField;
      outRegularField.setNSpace(3);
      data0 = new float[n];
      data1 = new float[n];
      data2 = new float[3*n];
      coords = new float[3 * n];
      for (int i = 0; i < n; i++)
      {
         double u = (2. * i - n) / n;
         double s = 0;
         for (double[] d1 : d)
         {
            double r = (u - d1[0]) * (u - d1[0]) / d1[1];
            s += Math.exp(-r) * d1[2];
         }
         data0[i] = (float) s;         
         data1[i] = (float) (Math.cos(6 * u) + u/2);
         for (int v = 0; v < 3; v++) {
             data2[3*i + v] = (float) (Math.cos(6 * u) + u/(v+1));            
         }
         float dd = 1.f / n;
         coords[3 * i] = (float)Math.cos(i * dd + s)/2.0f;
         coords[3 * i + 1] = (float)Math.sin(i * dd + s)/2.0f;
         coords[3 * i + 2] = 2.5f * (float)Math.sqrt(i * dd);
      }
      
     //center and normalize coords
      outRegularField.setCoords(coords);
      float[][] extents = outRegularField.getExtents();
      float[] p0 = new float[3];
      for (int i = 0; i < 3; i++) {
           p0[i] = (extents[1][i] + extents[0][i])/2.0f;           
      }
      for (int i = 0; i < n; i++) {
           for (int j = 0; j < 3; j++) {
               coords[3*i + j] -= p0[j];               
           }           
      }
      outRegularField.updateExtents();
      
      
      
      outRegularField.addData(DataArray.create(data0, 1, "gaussians"));
      outRegularField.addData(DataArray.create(data1, 1, "trig_function"));
      outRegularField.addData(DataArray.create(data2, 3, "vector_trig"));
      
      setOutputValue("outField", new VNRegularField(outRegularField));
      prepareOutputGeometry();
      show();
              
   }

   @Override
   public void onInitFinishedLocal()
   {
      generateField();
   }

   @Override
   public void onActive()
   { 
      generateField();
   }
}

