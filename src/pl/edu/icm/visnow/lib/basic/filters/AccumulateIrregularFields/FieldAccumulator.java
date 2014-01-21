///<editor-fold defaultstate="collapsed" desc=" COPYRIGHT AND LICENSE ">
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


package pl.edu.icm.visnow.lib.basic.filters.AccumulateIrregularFields;


import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import pl.edu.icm.visnow.datasets.IrregularField;
import pl.edu.icm.visnow.engine.core.InputEgg;
import pl.edu.icm.visnow.engine.core.OutputEgg;
import pl.edu.icm.visnow.lib.templates.visualization.modules.IrregularOutFieldVisualizationModule;
import pl.edu.icm.visnow.lib.types.VNField;
import pl.edu.icm.visnow.lib.types.VNIrregularField;
import pl.edu.icm.visnow.lib.utils.SwingInstancer;
import pl.edu.icm.visnow.lib.utils.field.MergeIrregularField;

/**
 *
 * @author Krzysztof S. Nowinski
 *         Warsaw University, ICM
 */
public class FieldAccumulator extends IrregularOutFieldVisualizationModule

{

   private GUI computeUI = null;
   protected IrregularField inField = null;
   protected Params params;
   protected boolean fromGUI = false;
   protected int current = 0;
   protected boolean separate = false;
   protected int[][] outSNDS;


   public FieldAccumulator()
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

   public static InputEgg[] inputEggs = null;
   public static OutputEgg[] outputEggs = null;

  @Override
   public void onActive()
   {
      if (getInputFirstValue("inField") == null || !params.accumulating())
         return;
      if (outField == null || params.resetting())
      {
         params.setReset(false);
         VNField input = ((VNField) getInputFirstValue("inField"));
         inField = (IrregularField)input.getField();   
         if (inField  != null)
         {
            outField = inField.cloneDeep();
            outField.clearData();
            for (int i = 0; i < inField.getNData(); i++)
               if (inField.getData(i).isSimpleNumeric())
                  outField.addData(inField.getData(i));
            outSNDS = outField.getSimpleNumericDataSchema();
         }
         current = 0;
      }      
      else
      {
         VNField input = ((VNField) getInputFirstValue("inField"));
         IrregularField newInField = (IrregularField)input.getField();         
         if (newInField == null || newInField.getNNodes() < 1)
            return;
         int[][] inSNDS = newInField.getSimpleNumericDataSchema();
         if (outSNDS != null)
         {
            if (outSNDS.length != inSNDS.length)
               return;
            for (int i = 0; i < inSNDS.length; i++)
               if (inSNDS[i][0] != outSNDS[i][0] || inSNDS[i][1] != outSNDS[i][1])
                  return;
         }
         current += 1;
         if (newInField != null && inField != newInField)
            outField = MergeIrregularField.merge(outField, newInField, current, params.separateCellSets());
      }
      prepareOutputGeometry();
      try
      {
         Thread.sleep(500);
      } catch (Exception e)
      {
      }
      show();
      setOutputValue("outField", new VNIrregularField(outField));
      fromGUI = false;
   }
}
