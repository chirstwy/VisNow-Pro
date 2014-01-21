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

package pl.edu.icm.visnow.lib.basic.filters.Canny;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import pl.edu.icm.visnow.engine.core.InputEgg;
import pl.edu.icm.visnow.engine.core.OutputEgg;
import pl.edu.icm.visnow.datasets.RegularField;
import pl.edu.icm.visnow.lib.types.VNRegularField;
import pl.edu.icm.visnow.datasets.dataarrays.DataArray;
import pl.edu.icm.visnow.lib.templates.visualization.modules.RegularOutFieldVisualizationModule;
/**
 *
 * @author Andrzej Rutkowski (rudy@mat.uni.torun.pl)
 * @author modified by pregulski, ICM, UW
 */
public class Canny extends RegularOutFieldVisualizationModule {
    private GUI cannyPanel = new GUI();
    private final Params params;
    
    public Canny() {
        parameters = params = new Params(); 
        cannyPanel.setParams(params);
        cannyPanel.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                startAction();
            }
        });

        ui.addComputeGUI(cannyPanel); //to copy
        setPanel(ui); //to copy
    }
    
    public String getStandardName() {
        return "Canny";
    }
    
    public static Canny getInstance() {
        return new Canny();
    }
    
        public static InputEgg[] inputEggs = null;
        public static OutputEgg[] outputEggs = null;

    @Override
    public void onActive() {
        if (getInputFirstValue("inField") == null)
            return;
        RegularField inField =((VNRegularField)getInputFirstValue("inField")).getField();
        if (inField == null)
            return;
        int[] dims = inField.getDims();
        if (dims == null)
            return;

        RegularField outFld = null;

        if (dims.length < 2 || dims.length > 3) {
            outFld = inField.clone();
            // TODO: wiecej wymiarow - ogolna metoda
        } else {
            outFld = new RegularField (dims, inField.getExtents());
            for (int i = 0; i < inField.getNData(); i++) {
                CoreBase cannyCore = null;
                DataArray datain = inField.getData(i);
                switch (datain.getType()) {
                    case DataArray.FIELD_DATA_BYTE:
                        cannyCore = new CoreB(datain, dims);
                        break;
                    case DataArray.FIELD_DATA_SHORT:
                        cannyCore = new CoreS(datain, dims);
                        break;
                    case DataArray.FIELD_DATA_INT:
                        cannyCore = new CoreI(datain, dims);
                        break;
                    case DataArray.FIELD_DATA_FLOAT:
                        cannyCore = new CoreF(datain, dims);
                        break;
                    case DataArray.FIELD_DATA_DOUBLE:
                        cannyCore = new CoreD(datain, dims);
                        break;
                    default:
                        cannyCore = new CoreF(datain, dims);
                        break;
                }
                if (cannyCore != null) {
                    byte[] out = cannyCore.calculate(params.getThresholdHigh(), params.getThresholdLow());
                    outFld.addData(DataArray.create(out, 1, "canny_"+inField.getData(i).getName()));
                }
            }
        }

        setOutputValue("outField", new VNRegularField(outFld));
        outField = outFld.clone(); //to copy
        prepareOutputGeometry(); //to copy        
        show(); //to copy
        
    }   
}