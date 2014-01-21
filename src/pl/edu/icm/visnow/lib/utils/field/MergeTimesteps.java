
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

package pl.edu.icm.visnow.lib.utils.field;

import pl.edu.icm.visnow.datasets.Field;
import pl.edu.icm.visnow.datasets.TimeData;
import pl.edu.icm.visnow.datasets.dataarrays.DataArray;

/**
 *
 * @author Krzysztof S. Nowinski, University of Warsaw ICM
 */
public class MergeTimesteps
{
   public static Field mergeTimesteps(Field outField, Field inField1, boolean mergeCoords, boolean mergeData, boolean appendTime)
   {
      float maxt = -Float.MAX_VALUE;
      float t;
      if (outField == null)
         return inField1.cloneDeep();
      if (inField1.getNNodes() == outField.getNNodes())
      {
         if (mergeCoords && outField.getAllCoords() != null && inField1.getAllCoords() != null)
         {
            TimeData<float[]> outCoords = outField.getAllCoords();
            float endTime = outCoords.getEndTime();
            TimeData<float[]> inCoords = inField1.getAllCoords();
            int nSteps = inCoords.getNSteps();
            float startTime = inCoords.getStartTime();
            for (int i = 0; i < nSteps; i++)
            {
               t   = inCoords.getTime(i);
               if (appendTime)
                  t += endTime + 1 - startTime;
               outCoords.setData(inCoords.get(i), t);
               maxt = Math.max(maxt, t);
            }
         }
         if (mergeData)
         {
            for (int iData = 0; iData < inField1.getNData(); iData++)
            {
               DataArray inDA = inField1.getData(iData);
               DataArray outDA = outField.getData(inDA.getName());
               if (outDA == null)
               {
                  outField.addData(inDA.cloneDeep(inDA.getName()));
                  continue;
               }
               if (outDA.compatibleWith(inDA))
               {
                  float startTime, endTime;
                  int nSteps;
                  switch (inDA.getType())
                  {
                  case DataArray.FIELD_DATA_BYTE:
                     TimeData<byte[]> bOutData = (TimeData<byte[]>)outDA.getTimeData();
                     endTime = bOutData.getEndTime();
                     TimeData<byte[]> bInData = (TimeData<byte[]>)inDA.getTimeData();
                     nSteps = bInData.getNSteps();
                     startTime = bInData.getStartTime();
                     for (int i = 0; i < nSteps; i++)
                     {
                        t   = bInData.getTime(i);
                        if (appendTime)
                           t += endTime + 1 - startTime;
                        bOutData.setData(bInData.get(i), t);
                        maxt = Math.max(maxt, t);
                     }
                     break;
                  case DataArray.FIELD_DATA_SHORT:
                     TimeData<short[]> sOutData = (TimeData<short[]>)outDA.getTimeData();
                     endTime = sOutData.getEndTime();
                     TimeData<short[]> sInData = (TimeData<short[]>)inDA.getTimeData();
                     nSteps = sInData.getNSteps();
                     startTime = sInData.getStartTime();
                     for (int i = 0; i < nSteps; i++)
                     {
                        t   = sInData.getTime(i);
                        if (appendTime)
                           t += endTime + 1 - startTime;
                        sOutData.setData(sInData.get(i), t);
                        maxt = Math.max(maxt, t);
                     }
                     break;
                  case DataArray.FIELD_DATA_INT:
                     TimeData<int[]> iOutData = (TimeData<int[]>)outDA.getTimeData();
                     endTime = iOutData.getEndTime();
                     TimeData<int[]> iInData = (TimeData<int[]>)inDA.getTimeData();
                     nSteps = iInData.getNSteps();
                     startTime = iInData.getStartTime();
                     for (int i = 0; i < nSteps; i++)
                     {
                        t   = iInData.getTime(i);
                        if (appendTime)
                           t += endTime + 1 - startTime;
                        iOutData.setData(iInData.get(i), t);
                        maxt = Math.max(maxt, t);
                     }
                     break;
                  case DataArray.FIELD_DATA_FLOAT:
                     TimeData<float[]> fOutData = (TimeData<float[]>)outDA.getTimeData();
                     endTime = fOutData.getEndTime();
                     TimeData<float[]> fInData = (TimeData<float[]>)inDA.getTimeData();
                     nSteps = fInData.getNSteps();
                     startTime = fInData.getStartTime();
                     for (int i = 0; i < nSteps; i++)
                     {
                        t   = fInData.getTime(i);
                        if (appendTime)
                           t += endTime + 1 - startTime;
                        fOutData.setData(fInData.get(i), t);
                        maxt = Math.max(maxt, t);
                     }
                     break;
                  case DataArray.FIELD_DATA_DOUBLE:
                     TimeData<double[]> dOutData = (TimeData<double[]>)outDA.getTimeData();
                     endTime = dOutData.getEndTime();
                     TimeData<double[]> dInData = (TimeData<double[]>)inDA.getTimeData();
                     nSteps = dInData.getNSteps();
                     startTime = dInData.getStartTime();
                     for (int i = 0; i < nSteps; i++)
                     {
                        t   = dInData.getTime(i);
                        if (appendTime)
                           t += endTime + 1 - startTime;
                        dOutData.setData(dInData.get(i), t);
                        maxt = Math.max(maxt, t);
                     }
                     break;
                  }
               }
            }
         }
      }
      outField.setCurrentTime(maxt);
      return outField;
   }
}
