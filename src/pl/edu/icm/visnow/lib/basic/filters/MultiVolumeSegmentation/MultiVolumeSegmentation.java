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

package pl.edu.icm.visnow.lib.basic.filters.MultiVolumeSegmentation;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import pl.edu.icm.visnow.datasets.dataarrays.DataArray;
import pl.edu.icm.visnow.datasets.Field;
import pl.edu.icm.visnow.datasets.IrregularField;
import pl.edu.icm.visnow.datasets.RegularField;
import pl.edu.icm.visnow.engine.core.InputEgg;
import pl.edu.icm.visnow.engine.core.ModuleCore;
import pl.edu.icm.visnow.engine.core.OutputEgg;
import pl.edu.icm.visnow.gui.events.FloatValueModificationEvent;
import pl.edu.icm.visnow.gui.events.FloatValueModificationListener;
import pl.edu.icm.visnow.lib.types.VNField;
import pl.edu.icm.visnow.lib.types.VNRegularField;
import pl.edu.icm.visnow.lib.utils.SwingInstancer;

/**
 *
 * @author Krzysztof S. Nowinski
 * University of Warsaw, ICM
 */
public class MultiVolumeSegmentation  extends ModuleCore
{

   public static InputEgg[] inputEggs = null;
   public static OutputEgg[] outputEggs = null;
   protected GUI ui = new GUI();
   protected Params params;
   protected RegularField inField = null;
   protected IrregularField inPts = null;
   protected RegularField outField = null;
   protected Segmentation segmentation;
   protected boolean fromGUI = false;

   public MultiVolumeSegmentation()
   {
      parameters = params = new Params();
      params.addChangeListener(new ChangeListener()
      {
         public void stateChanged(ChangeEvent evt)
         {
            if (inField == null || inPts == null)
               return;
            fromGUI = true;
            startAction();
         }
      });
      SwingInstancer.swingRunAndWait(new Runnable()
      {
         public void run()
         {
            ui = new GUI();
         }
      });
      ui.setParams(params);
      setPanel(ui);
   }

   @Override
   public void onActive()
   {
      if (!fromGUI)
      {
         if (getInputFirstValue("inField") == null || getInputFirstValue("startPoints") == null)
            return;
         inField = ((VNRegularField) getInputFirstValue("inField")).getField();
         Field pts = ((VNField) getInputFirstValue("startPoints")).getField();
         if (inField == null || pts == null || !(pts instanceof IrregularField))
            return;
         inPts = (IrregularField) pts;
         return;
      }
      outField = inField.clone();
      outField.clearData();
      segmentation = new Segmentation(params, inField, inPts);
      segmentation.addFloatValueModificationListener(
              new FloatValueModificationListener()
              {
                 public void floatValueChanged(FloatValueModificationEvent e)
                 {
                    setProgress(e.getVal());
                 } 
              });
      segmentation.compute();
      outField.addData(DataArray.create(segmentation.getAreas(), 1, "areas"));
      outField.addData(DataArray.create(segmentation.getBd(), 1, "data"));
      String[] dataMap = new String[inPts.getNCellSets() + 3];
      dataMap[0] = "MAP";
      dataMap[1] = "0:background";
      dataMap[2] = "1:unassigned";
      for (int i = 0; i < inPts.getNCellSets(); i++)
         dataMap[i + 3] = "" + (i + 2) + ":" + (inPts.getCellSet(i).getName() + 2);
      outField.getData(0).setUserData(dataMap);
      setOutputValue("outField", new VNRegularField(outField));
   }
}
