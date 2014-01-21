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

package pl.edu.icm.visnow.lib.basic.readers.ReadFloatDataArray;

import java.io.File;
import java.nio.ByteOrder;
import javax.imageio.stream.FileImageInputStream;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import pl.edu.icm.visnow.datasets.dataarrays.DataArray;
import pl.edu.icm.visnow.datasets.IrregularField;
import pl.edu.icm.visnow.engine.core.ModuleCore;
import pl.edu.icm.visnow.engine.core.OutputEgg;
import pl.edu.icm.visnow.lib.types.VNIrregularField;
import pl.edu.icm.visnow.lib.utils.SwingInstancer;

/**
 * @author  Krzysztof S. Nowinski (know@icm.edu.pl)
 * Warsaw University, Interdisciplinary Centre
 * for Mathematical and Computational Modelling
 */
public class ReadFloatDataArray extends ModuleCore
{

   protected GUI ui = null;
   protected Params params;
   protected String lastFileName = " ";
   protected IrregularField outField = null;

   /**
    * Creates a new instance of CreateGrid
    */
   public ReadFloatDataArray()
   {
      parameters = params = new Params();
      params.addChangeListener(new ChangeListener()
      {
         @Override
         public void stateChanged(ChangeEvent evt)
         {
            startAction();
         }
      });
      SwingInstancer.swingRunAndWait(new Runnable()
      {
         @Override
         public void run()
         {
            ui = new GUI("float data array file", "bin", "BIN");
         }
      });
      ui.setParams(params);
      ui.setParams(params);
      setPanel(ui);
   }

   @Override
   public boolean isGenerator() {
      return true;
   }

   public static OutputEgg[] outputEggs = null;

   private boolean readDataArray()
   {
      try
      {
         FileImageInputStream inStream = new FileImageInputStream(new File(params.getFileName()));
         inStream.setByteOrder(ByteOrder.BIG_ENDIAN);
         inStream.mark();
         int nnodes = inStream.readInt();
         if (nnodes < 0 || nnodes > 10000000)
         {
            inStream.reset();
            inStream.setByteOrder(ByteOrder.LITTLE_ENDIAN);
            nnodes = inStream.readInt();
            if (nnodes < 0 || nnodes > 10000000)
            {
               System.out.println("bad data file");
               return false;
            }
         }
         outField = new IrregularField(nnodes);
         outField.setNSpace(3);
         float[] f = new float[nnodes*params.getVeclen()];
         for (int i = 0; i < f.length; i++)
            f[i] = inStream.readFloat();
         outField.addData(DataArray.create(f, params.getVeclen(), params.getDataName(), params.getDataUnits(), null));
      } catch (Exception e)
      {
         e.printStackTrace();
         return false;
      }
      return true;
   }

   @Override
   public void onActive()
   {
      if (params.getFileName() != null && !params.getFileName().equals(lastFileName))
      {
         lastFileName = params.getFileName();
         if (!readDataArray())
            return;
         ui.setFieldDescription(outField.description());
         setOutputValue("UCD field", new VNIrregularField(outField));
      }
   }
   
   @Override
   public void onInitFinished() {
       if(isForceFlag()) 
           ui.activateOpenDialog();
   }
   
}
