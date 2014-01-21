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

package pl.edu.icm.visnow.lib.basic.filters.AccumulateTimesteps;


import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import pl.edu.icm.visnow.datasets.Field;
import pl.edu.icm.visnow.datasets.IrregularField;
import pl.edu.icm.visnow.datasets.RegularField;
import pl.edu.icm.visnow.engine.core.InputEgg;
import pl.edu.icm.visnow.engine.core.OutputEgg;
import pl.edu.icm.visnow.lib.templates.visualization.modules.OutFieldVisualizationModule;
import pl.edu.icm.visnow.lib.types.VNField;
import pl.edu.icm.visnow.lib.types.VNIrregularField;
import pl.edu.icm.visnow.lib.types.VNRegularField;
import pl.edu.icm.visnow.lib.utils.SwingInstancer;
import pl.edu.icm.visnow.lib.utils.field.MergeTimesteps;

/**
 *
 * @author Krzysztof S. Nowinski
 *         Warsaw University, ICM
 */
public class AccumulateTimesteps extends OutFieldVisualizationModule

{

   private GUI computeUI = null;
   protected Field inField = null;
   protected Params params;
   protected boolean fromGUI = false;
   protected boolean separate = false;


   public AccumulateTimesteps()
   {
      parameters = params = new Params();
      params.addChangeListener(new ChangeListener()
      {
         public void stateChanged(ChangeEvent evt)
         {
            fromGUI = true;
            startAction();
         }
      });
      SwingInstancer.swingRunAndWait(new Runnable()
      {
         public void run()
         {
            computeUI = new GUI();
         }
      });
      computeUI.setParams(params);
      ui.addComputeGUI(computeUI);
      setPanel(ui);
      outObj.setName("accumulated");
   }
   
   private void reset(){
       outField = null;
      setOutputValue("regularOutField", null);
      setOutputValue("irregularOutField", null);
      outObj.clearAllGeometry();
   }

   public static InputEgg[] inputEggs = null;
   public static OutputEgg[] outputEggs = null;


  @Override
   public void onActive()
   {
       if (params.isResetting()){
           reset();
           return;
       }
       if (!params.accumulating())
               return;
       
      if (getInputFirstValue("inField") == null)
         return;
      VNField input = ((VNField) getInputFirstValue("inField"));
      Field newInField = input.getField();  
      boolean newOutField = outField == null;
 //     if (inField != newInField)
      {
         inField = newInField;
         outField = MergeTimesteps.mergeTimesteps(outField, inField, params.mergeCoords(), params.mergeData(), params.appendTime());
      }
      if (newOutField)
         prepareOutputGeometry();
//      try
//      {
//         Thread.sleep(500);
//      } catch (Exception e)
//      {
//      }
      show();
      if (outField != null && outField instanceof RegularField) {
          setOutputValue("outRegularField", new VNRegularField((RegularField)outField));
          setOutputValue("outIrregularField", null);
      } else if(outField != null && outField instanceof IrregularField) {
          setOutputValue("outRegularField", null);
          setOutputValue("outIrregularField", new VNIrregularField((IrregularField)outField));
      } else {
          setOutputValue("outRegularField", null);
          setOutputValue("outIrregularField", null);
      }
      fromGUI = false;
   }
}
