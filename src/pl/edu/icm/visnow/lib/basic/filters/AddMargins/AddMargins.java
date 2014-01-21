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
package pl.edu.icm.visnow.lib.basic.filters.AddMargins;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import pl.edu.icm.visnow.datasets.dataarrays.DataArray;
import pl.edu.icm.visnow.datasets.RegularField;
import pl.edu.icm.visnow.engine.core.InputEgg;
import pl.edu.icm.visnow.engine.core.ModuleCore;
import pl.edu.icm.visnow.engine.core.OutputEgg;
import pl.edu.icm.visnow.lib.types.VNRegularField;
import pl.edu.icm.visnow.lib.utils.PaddingType;
import pl.edu.icm.visnow.lib.utils.SwingInstancer;
import static pl.edu.icm.visnow.lib.utils.field.ExtendMargins.*;

/**
 *
 ** @author Krzysztof S. Nowinski, University of Warsaw ICM
 */
public class AddMargins extends ModuleCore {

    private GUI ui = null;
    protected Params params;
    protected RegularField inField = null;
    protected boolean fromGUI = false;

    public AddMargins() {
        params = new Params();
        SwingInstancer.swingRunAndWait(new Runnable() {
            public void run() {
                ui = new GUI();
            }
        });

        ui.setParams(params);
        params.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                fromGUI = true;
                startAction();
            }
        });
        setPanel(ui);
    }
    public static InputEgg[] inputEggs = null;
    public static OutputEgg[] outputEggs = null;

    public static InputEgg[] getInputEggs() {
        if (inputEggs == null) {
            inputEggs = new InputEgg[]{
                new InputEgg("inField", VNRegularField.class, InputEgg.NECESSARY | InputEgg.TRIGGERING | InputEgg.NORMAL),};
        }
        return inputEggs;
    }

    public static OutputEgg[] getOutputEggs() {
        if (outputEggs == null) {
            outputEggs = new OutputEgg[]{
                new OutputEgg("outField", VNRegularField.class),};
        }
        return outputEggs;
    }

    protected RegularField extendedField() {
        int[][] margins = params.getMargins();
        int[] dims = inField.getDims();
        int[] outDims = new int[dims.length];
        for (int i = 0; i < dims.length; i++) {
            outDims[i] = dims[i] + margins[i][0] + margins[i][1];
        }

        RegularField outField = new RegularField(outDims);
        outField.setNSpace(inField.getNSpace());
        if (inField.getCoords() != null) {
            outField.setCoords(extendMargins(dims, inField.getNSpace(), outDims, margins, inField.getCoords(), PaddingType.FIXED));
        } else {
            float[][] inAffine = inField.getAffine();
            float[][] outAffine = new float[4][3];

            System.arraycopy(inAffine[3], 0, outAffine[3], 0, 3);

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    outAffine[i][j] = inAffine[i][j];
                    outAffine[3][i] -= margins[i][0] * inAffine[i][j];
                }
            }
            outField.setAffine(outAffine);
        }

        int[] interpolatedComponents = params.getComponents();

        for (int n = 0; n < interpolatedComponents.length; n++) {
            DataArray da = inField.getData(interpolatedComponents[n]);
            switch (da.getType()) {
                case DataArray.FIELD_DATA_BYTE:
                    outField.addData(DataArray.create(extendMargins(dims, da.getVeclen(), outDims, margins, da.getBData(), params.getMethod()), da.getVeclen(), da.getName()));
                    break;
                case DataArray.FIELD_DATA_SHORT:
                    outField.addData(DataArray.create(extendMargins(dims, da.getVeclen(), outDims, margins, da.getDData(), params.getMethod()), da.getVeclen(), da.getName()));
                    break;
                case DataArray.FIELD_DATA_INT:
                    outField.addData(DataArray.create(extendMargins(dims, da.getVeclen(), outDims, margins, da.getIData(), params.getMethod()), da.getVeclen(), da.getName()));
                    break;
                case DataArray.FIELD_DATA_FLOAT:
                    outField.addData(DataArray.create(extendMargins(dims, da.getVeclen(), outDims, margins, da.getFData(), params.getMethod()), da.getVeclen(), da.getName()));
                    break;
                case DataArray.FIELD_DATA_DOUBLE:
                    outField.addData(DataArray.create(extendMargins(dims, da.getVeclen(), outDims, margins, da.getDData(), params.getMethod()), da.getVeclen(), da.getName()));
                    break;
            }
        }
        return outField;
    }

    @Override
    public void onActive() {
        if (getInputFirstValue("inField") == null
                || ((VNRegularField) getInputFirstValue("inField")).getField() == null) {
            return;
        }
        if (!fromGUI) {
            inField = ((VNRegularField) getInputFirstValue("inField")).getField();
            if (inField.getDims() == null || inField.getDims().length == 0) {
                return;
            }
            ui.setField(inField);
        } else {
            setOutputValue("outField", new VNRegularField(extendedField()));
            fromGUI = false;
        }
    }
}