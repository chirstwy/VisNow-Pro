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
package pl.edu.icm.visnow.lib.basic.mappers.Trajectories;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import pl.edu.icm.visnow.datasets.Field;
import pl.edu.icm.visnow.datasets.RegularField;
import pl.edu.icm.visnow.engine.core.InputEgg;
import pl.edu.icm.visnow.engine.core.OutputEgg;
import pl.edu.icm.visnow.lib.templates.visualization.modules.IrregularOutFieldVisualizationModule;
import pl.edu.icm.visnow.lib.types.VNField;
import pl.edu.icm.visnow.lib.types.VNIrregularField;
import pl.edu.icm.visnow.lib.types.VNRegularField;
import pl.edu.icm.visnow.lib.utils.SwingInstancer;

/**
 * @author Bartosz Borucki (babor@icm.edu.pl) Warsaw University,
 * Interdisciplinary Centre for Mathematical and Computational Modelling
 */
public class Trajectories extends IrregularOutFieldVisualizationModule
{

   public static InputEgg[] inputEggs = null;
   public static OutputEgg[] outputEggs = null;
   protected Field inField;
   protected GUI computeUI = null;
   protected boolean fromGUI = false;
   protected Params params;
   protected Core core = new Core();

   public Trajectories()
   {
      parameters = params = new Params();
      core.setParams(params);
      SwingInstancer.swingRunAndWait(new Runnable()
      {

         public void run()
         {
            computeUI = new GUI();
         }
      });
      computeUI.setParams(params);
      ui.addComputeGUI(computeUI);
      outObj.setName("trajectories");
      params.addChangeListener(new ChangeListener()
      {

         public void stateChanged(ChangeEvent evt)
         {
            fromGUI = true;
            startAction();
         }
      });
      setPanel(ui);
   }

   public void update()
   {
      if (inField == null)
         return;

      core.setInField(inField);
      core.update();
      outField = core.getOutField();
      RegularField mappedField = core.getMappedField();

      prepareOutputGeometry();
      show();
      setOutputValue("trajectoriesField", new VNIrregularField(outField));
      setOutputValue("mappedTrajectoriesField", new VNRegularField(mappedField));
   }

   @Override
   public void onActive()
   {
      if (!fromGUI)
      {
         VNField inFld = (VNField) getInputFirstValue("inField");
         if (inFld != null)
         {
            Field newInField = inFld.getField();
            if (newInField != null && inField != newInField)
            {
               inField = newInField;
               if (inField.getNSpace() != 3 || inField.getNFrames() < 2)
               {
                  return;
               }
               computeUI.setInField(inField);
            }
         }
      }
      fromGUI = false;
      if (!params.isActive())
      {
         return;
      }
      update();
   }
}
