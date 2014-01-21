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

package pl.edu.icm.visnow.lib.basic.filters.CellToNode;


import java.util.ArrayList;
import pl.edu.icm.visnow.datasets.CellArray;
import pl.edu.icm.visnow.datasets.CellSet;
import pl.edu.icm.visnow.datasets.IrregularField;
import pl.edu.icm.visnow.datasets.cells.Cell;
import pl.edu.icm.visnow.datasets.dataarrays.DataArray;
import pl.edu.icm.visnow.engine.core.InputEgg;
import pl.edu.icm.visnow.engine.core.OutputEgg;
import pl.edu.icm.visnow.lib.templates.visualization.modules.IrregularOutFieldVisualizationModule;
import pl.edu.icm.visnow.lib.types.VNField;
import pl.edu.icm.visnow.lib.types.VNIrregularField;


/**
 * @author  Krzysztof S. Nowinski (know@icm.edu.pl)
 * Warsaw University, Interdisciplinary Centre
 * for Mathematical and Computational Modelling
 */
public class CellToNode extends IrregularOutFieldVisualizationModule
{

   /**
    * Creates a new instance of SurfaceSmoother
    */
   public static InputEgg[] inputEggs = null;
   public static OutputEgg[] outputEggs = null;

   public CellToNode()
   {
      setPanel(ui);
   }
   
   

   @Override
   public void onActive()
   {
      if (getInputFirstValue("inField") == null || 
         ((VNField)getInputFirstValue("inField")).getField() == null ||
          !(((VNField)getInputFirstValue("inField")).getField() instanceof IrregularField))
         return;
      IrregularField inField = (IrregularField)(((VNField)getInputFirstValue("inField")).getField());
      outField = inField.clone();
      int nNodes = inField.getNNodes();
      int[] counts = new int[nNodes];
      int csId = 0;
      for (CellSet cs : inField.getCellSets())
      {
         ArrayList<DataArray> cellData = cs.getData();
         for (DataArray inDA : cellData)
         {
            float[] inD = inDA.getFData();
            int vlen = inDA.getVeclen();
            float[] outD = new float[nNodes * vlen];
            java.util.Arrays.fill(counts, 0);
            java.util.Arrays.fill(outD, 0.f);
            for (int i = 0; i < Cell.TYPES; i++)
            {
               int nv = Cell.nv[i]; 
               CellArray ca = cs.getCellArray(i);
               if (ca == null)
                  continue;
               int[] nodes = ca.getNodes();
               int[] indices = ca.getDataIndices();
               for (int j = 0; j < ca.getNCells(); j++)
               {
                  int iin = vlen * indices[j];
                  for (int l = nv * j; l < nv * (j + 1); l++)
                  {
                     int iout = vlen * nodes[l];
                     counts[nodes[l]] += 1;
                     for (int k = 0; k < vlen; k++)
                        outD[iout + k] += inD[iin + k];
                  }
               }
            }
            for (int i = 0; i < nNodes; i++)
            {
               if (counts[i] == 0) continue;
               float f = 1.f / counts[i];
               for (int j = i * vlen; j < (i + 1) * vlen; j++)
                  outD[j] *= f;
            }
            outField.addData(DataArray.create(outD, vlen, 
                                              inDA.getName() + csId, 
                                              inDA.getUnit(), 
                                              inDA.getUserData()));
         }
         csId += 1;
      }
      prepareOutputGeometry();
      show();
      setOutputValue("outField", new VNIrregularField(outField));
   }
}
