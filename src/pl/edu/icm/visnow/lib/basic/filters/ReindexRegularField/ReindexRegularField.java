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

package pl.edu.icm.visnow.lib.basic.filters.ReindexRegularField;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import pl.edu.icm.visnow.datasets.dataarrays.DataArray;
import pl.edu.icm.visnow.datasets.RegularField;
import pl.edu.icm.visnow.engine.core.InputEgg;
import pl.edu.icm.visnow.engine.core.ModuleCore;
import pl.edu.icm.visnow.engine.core.OutputEgg;
import pl.edu.icm.visnow.lib.types.VNRegularField;
import pl.edu.icm.visnow.lib.utils.SwingInstancer;

/**
 *
 * @author  Krzysztof S. Nowinski, University of Warsaw, ICM
 */
public class ReindexRegularField  extends ModuleCore
{
   protected GUI ui = null;
   protected RegularField inField = null;
   protected RegularField outField = null;
   protected Params params;
   protected int[] dims = {-1,-1,-1};
   protected int[] outDims ={-1,-1,-1};
   protected int[] cperm = {-1,-1,-1};
   protected int[] step = {1,1,1};
   protected int[] start = {0,0,0};
   protected int[] stop = {0,0,0};
   protected int ndims = 3;
   protected boolean fromInput = true;
   protected int n = 0;
   
   public ReindexRegularField()
   {
      parameters = params = new Params();
      params.addChangeListener(new ChangeListener()
      {
         public void stateChanged(ChangeEvent evt)
         {
            fromInput = false;
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
      setPanel(ui);//WTF-MUI:addModuleUI(ui);
   }

    private static InputEgg[] inputEggs = null;
    private static OutputEgg[] outputEggs = null;

    public static InputEgg[] getInputEggs() {
        if (inputEggs == null) {
            inputEggs = new InputEgg[]{
                        new InputEgg("inField", VNRegularField.class, InputEgg.NECESSARY | InputEgg.TRIGGERING)
                    };
        }
        return inputEggs;
    }

    public static OutputEgg[] getOutputEggs() {
        if (outputEggs == null) {
            outputEggs = new OutputEgg[] {
                new OutputEgg("outField", VNRegularField.class)
            };
        }
        return outputEggs;
    }


/**
* crops and downsizes data array 
* no test for correct parameters
* (this is done once at field crop/down)
*/
   private byte[] reorderArray(byte[] data, int veclen)
   {
      byte[] dt = new byte[n*veclen];
      switch (dims.length)
      {
      case 3:
         for (int i = 0, l = 0; i < outDims[2]; i++)
            for (int j = 0; j < outDims[1]; j++)
            {
               int m = veclen*(start[2]+i*step[2]+start[1]+j*step[1]+start[0]);
               for (int k = 0; k < outDims[0]; k++, l+=veclen, m+=step[0]*veclen)
                  for (int c = 0; c<veclen; c++)
                     dt[l+c] = data[m+c];
            }
         break;
      case 2:
         for (int j = 0, l = 0; j < outDims[1]; j++)
         {
            int m = veclen*(start[1]+j*step[1]+start[0]);
            for (int k = 0; k < outDims[0]; k++, l+=veclen, m+=step[0]*veclen)
               for (int c = 0; c<veclen; c++)
                  dt[l+c] = data[m+c];
         }
         break;
      case 1:
         for (int k = 0, l = 0, m = start[0]*veclen; k < outDims[0]; k++, l+=veclen, m+=step[0]*veclen)
            for (int c = 0; c<veclen; c++)
               dt[l+c] = data[m+c];
         break;
      }
      return dt;
   }
   
   private short[] reorderArray(short[] data, int veclen)
   {
      short[] dt = new short[n*veclen];
      switch (dims.length)
      {
      case 3:
         for (int i = 0, l = 0; i < outDims[2]; i++)
            for (int j = 0; j < outDims[1]; j++)
            {
               int m = veclen*(start[2]+i*step[2]+start[1]+j*step[1]+start[0]);
               for (int k = 0; k < outDims[0]; k++, l+=veclen, m+=step[0]*veclen)
                  for (int c = 0; c<veclen; c++)
                     dt[l+c] = data[m+c];
            }
         break;
      case 2:
         for (int j = 0, l = 0; j < outDims[1]; j++)
         {
            int m = veclen*(start[1]+j*step[1]+start[0]);
            for (int k = 0; k < outDims[0]; k++, l+=veclen, m+=step[0]*veclen)
               for (int c = 0; c<veclen; c++)
                  dt[l+c] = data[m+c];
         }
         break;
      case 1:
         for (int k = 0, l = 0, m = start[0]*veclen; k < outDims[0]; k++, l+=veclen, m+=step[0]*veclen)
            for (int c = 0; c<veclen; c++)
               dt[l+c] = data[m+c];
         break;
      }
      return dt;
   }
   
   private int[] reorderArray(int[] data, int veclen)
   {
      int[] dt = new int[n*veclen];
      switch (dims.length)
      {
      case 3:
         for (int i = 0, l = 0; i < outDims[2]; i++)
            for (int j = 0; j < outDims[1]; j++)
            {
               int m = veclen*(start[2]+i*step[2]+start[1]+j*step[1]+start[0]);
               for (int k = 0; k < outDims[0]; k++, l+=veclen, m+=step[0]*veclen)
                  for (int c = 0; c<veclen; c++)
                     dt[l+c] = data[m+c];
            }
         break;
      case 2:
         for (int j = 0, l = 0; j < outDims[1]; j++)
         {
            int m = veclen*(start[1]+j*step[1]+start[0]);
            for (int k = 0; k < outDims[0]; k++, l+=veclen, m+=step[0]*veclen)
               for (int c = 0; c<veclen; c++)
                  dt[l+c] = data[m+c];
         }
         break;
      case 1:
         for (int k = 0, l = 0, m = start[0]*veclen; k < outDims[0]; k++, l+=veclen, m+=step[0]*veclen)
            for (int c = 0; c<veclen; c++)
               dt[l+c] = data[m+c];
         break;
      }
      return dt;
   }
   
   private float[] reorderArray(float[] data, int veclen)
   {
      float[] dt = new float[n*veclen];
      switch (dims.length)
      {
      case 3:
         for (int i = 0, l = 0; i < outDims[2]; i++)
            for (int j = 0; j < outDims[1]; j++)
            {
               int m = veclen*(start[2]+i*step[2]+start[1]+j*step[1]+start[0]);
               for (int k = 0; k < outDims[0]; k++, l+=veclen, m+=step[0]*veclen)
                  for (int c = 0; c<veclen; c++)
                     dt[l+c] = data[m+c];
            }
         break;
      case 2:
         for (int j = 0, l = 0; j < outDims[1]; j++)
         {
            int m = veclen*(start[1]+j*step[1]+start[0]);
            for (int k = 0; k < outDims[0]; k++, l+=veclen, m+=step[0]*veclen)
               for (int c = 0; c<veclen; c++)
                  dt[l+c] = data[m+c];
         }
         break;
      case 1:
         for (int k = 0, l = 0, m = start[0]*veclen; k < outDims[0]; k++, l+=veclen, m+=step[0]*veclen)
            for (int c = 0; c<veclen; c++)
               dt[l+c] = data[m+c];
         break;
      }
      return dt;
   }
   
   private double[] reorderArray(double[] data, int veclen)
   {
      double[] dt = new double[n*veclen];
      switch (dims.length)
      {
      case 3:
         for (int i = 0, l = 0; i < outDims[2]; i++)
            for (int j = 0; j < outDims[1]; j++)
            {
               int m = veclen*(start[2]+i*step[2]+start[1]+j*step[1]+start[0]);
               for (int k = 0; k < outDims[0]; k++, l+=veclen, m+=step[0]*veclen)
                  for (int c = 0; c<veclen; c++)
                     dt[l+c] = data[m+c];
            }
         break;
      case 2:
         for (int j = 0, l = 0; j < outDims[1]; j++)
         {
            int m = veclen*(start[1]+j*step[1]+start[0]);
            for (int k = 0; k < outDims[0]; k++, l+=veclen, m+=step[0]*veclen)
               for (int c = 0; c<veclen; c++)
                  dt[l+c] = data[m+c];
         }
         break;
      case 1:
         for (int k = 0, l = 0, m = start[0]*veclen; k < outDims[0]; k++, l+=veclen, m+=step[0]*veclen)
            for (int c = 0; c<veclen; c++)
               dt[l+c] = data[m+c];
         break;
      }
      return dt;
   }
   
   @Override
   public void onActive()
   {
       if (fromInput)
       {
           if (getInputFirstValue("inField")==null)
              return;
           inField =((VNRegularField)getInputFirstValue("inField")).getField();
           dims = inField.getDims();
           ui.setNDim(dims.length);
           outField = inField;
           setOutputValue("outField", new VNRegularField(outField));
           fromInput = false;
       }
       else
       {
          outDims = new int[dims.length];
          cperm = params.getCoordinate();
          n = 1;
          for (int i = 0; i < dims.length; i++)
          {
             outDims[i] = dims[cperm[i]];
             n *= outDims[i];
             if (params.isMirror(i))
             {
                start[i] = dims[cperm[i]]-1;
                stop[i]  = 0;
                step[i]  = -1;
             }
             else
             {
                start[i] = 0;
                stop[i]  = dims[cperm[i]]-1;
                step[i]  = 1;
             }
          }
          for (int i = 0; i < dims.length; i++)
          {
             if (cperm[i] > 0)
             {
                start[i] *= dims[0];
                stop[i]  *= dims[0];
                step[i]  *= dims[0];
             }
             if (cperm[i] > 1)
             {
                start[i] *= dims[1];
                stop[i]  *= dims[1];
                step[i]  *= dims[1];
             }
          }
          outField = new RegularField(outDims);
          outField.setNSpace(inField.getNSpace());
          if (inField.getCoords()!=null)
               outField.setCoords(reorderArray(inField.getCoords(), inField.getNSpace()));
          else
          {
             float[][] outAffine = new float[4][3];
             float[][] affine = inField.getAffine();
             for (int i = 0; i < 3; i++)
                 outAffine[3][i] = affine[3][cperm[i]];
             for (int i = 0; i<outDims.length; i++)
                 for (int j = 0; j < 3; j++)
                 {
                     outAffine[i][j] = affine[cperm[i]][j];
//                     if (params.isMirror(i))
//                        outAffine[3][i]+=affine[i][j]*(dims[i]-1);
//                        outAffine[3][i]+=affine[cperm[i]][j]*(dims[cperm[i]]-1);
                 }
             outField.setAffine(outAffine);
           }

           for (int i = 0; i<inField.getNData(); i++)
           {
               DataArray dta = inField.getData(i);
               switch (dta.getType())
               {
               case  DataArray.FIELD_DATA_BYTE:
                  outField.addData(DataArray.create(reorderArray(dta.getBData(), dta.getVeclen()), dta.getVeclen(), dta.getName()));
                  break;
               case  DataArray.FIELD_DATA_SHORT:
                  outField.addData(DataArray.create(reorderArray(dta.getSData(), dta.getVeclen()), dta.getVeclen(), dta.getName()));
                  break;
               case  DataArray.FIELD_DATA_INT:
                  outField.addData(DataArray.create(reorderArray(dta.getIData(), dta.getVeclen()), dta.getVeclen(), dta.getName()));
                  break;
               case  DataArray.FIELD_DATA_FLOAT:
                  outField.addData(DataArray.create(reorderArray(dta.getFData(), dta.getVeclen()), dta.getVeclen(), dta.getName()));
                  break;
               case  DataArray.FIELD_DATA_DOUBLE:
                  outField.addData(DataArray.create(reorderArray(dta.getDData(), dta.getVeclen()), dta.getVeclen(), dta.getName()));
                  break;
               }
           }
           setOutputValue("outField", new VNRegularField(outField));
           fromInput = true;
       }
     
   }

}
