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
package pl.edu.icm.visnow.lib.basic.filters.Convolution;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import pl.edu.icm.visnow.datasets.Field;

import pl.edu.icm.visnow.datasets.RegularField;
import pl.edu.icm.visnow.engine.core.InputEgg;
import pl.edu.icm.visnow.engine.core.OutputEgg;
import pl.edu.icm.visnow.lib.templates.visualization.modules.RegularOutFieldVisualizationModule;
import pl.edu.icm.visnow.lib.types.VNField;
import pl.edu.icm.visnow.lib.types.VNRegularField;
import pl.edu.icm.visnow.lib.utils.SwingInstancer;
import pl.edu.icm.visnow.system.main.VisNow;
import pl.edu.icm.visnow.system.utils.usermessage.Level;

/**
 *
 * @author Piotr Wendykier (piotrw@icm.edu.pl)
 */
public class Convolution extends RegularOutFieldVisualizationModule {

    public static InputEgg[] inputEggs = null;
    public static OutputEgg[] outputEggs = null;
    private GUI computeUI;
    protected RegularField inFieldData;
    protected RegularField inFieldKernel;
    protected Params params;
    private Core core = new Core();

    public Convolution() {
        parameters = params = new Params();
        core.setParams(params);
        SwingInstancer.swingRunAndWait(new Runnable() {
            public void run() {
                computeUI = new GUI();
            }
        });
        computeUI.setParams(params);
        ui.addComputeGUI(computeUI);
        params.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                startAction();
            }
        });
        setPanel(ui);
    }

    public void update() {
        if (inFieldData == null || inFieldKernel == null) {
            setOutputValue("outField", null);
            return;
        }
        
        computeUI.setInFieldData(inFieldData);
        core.setInFieldData(inFieldData);
        core.setInFieldKernel(inFieldKernel);
        core.update();
        setOutputValue("outField", new VNRegularField(core.getOutField()));
        outField = core.getOutField();
        if (!prepareOutputGeometry()) {
            return;
        }
        show();
    }

    @Override
    public void onActive() {

        VNField inFldData = (VNField) getInputFirstValue("inFieldData");
        VNField inFldKernel = (VNField) getInputFirstValue("inFieldKernel");
        if (inFldData != null && inFldKernel != null) {
            Field newInFieldData = inFldData.getField();
            if (newInFieldData != null && newInFieldData instanceof RegularField && inFieldData != newInFieldData) {
                inFieldData = (RegularField) newInFieldData;
                VisNow.get().userMessageSend(this, "Data field updated successfully.", inFieldData != null ? inFieldData.toMultilineString() : "", Level.INFO);
            }
            if(inFieldData != null) {
                int[] dimsData = inFieldData.getDims();
                Field newInFieldKernel = inFldKernel.getField();
                if (newInFieldKernel != null && newInFieldKernel instanceof RegularField) {
                    if (newInFieldKernel.getNData() > 1) {
                        VisNow.get().userMessageSend(this, "Kernel field cannot contain multiple components.", "", Level.ERROR);
                        setOutputValue("outField", null);
                        return;
                    }

                    if (newInFieldKernel.getData(0).getVeclen() > 1) {
                        VisNow.get().userMessageSend(this, "Kernel field cannot contain vector components.", "", Level.ERROR);
                        setOutputValue("outField", null);
                        return;
                    }

                    if (inFieldData.getNSpace() != newInFieldKernel.getNSpace()) {
                        VisNow.get().userMessageSend(this, "The rank of data field (" + inFieldData.getNSpace() + ") is different than the rank of kernel field (" + newInFieldKernel.getNSpace() + ").", "", Level.ERROR);
                        setOutputValue("outField", null);
                        return;
                    }
                    
                    int[] dimsKernel = ((RegularField) newInFieldKernel).getDims();
                    if(dimsKernel.length != dimsData.length) {
                        VisNow.get().userMessageSend(this, "The rank of data field (" + inFieldData.getNSpace() + ") is different than the rank of kernel field (" + newInFieldKernel.getNSpace() + ").", "", Level.ERROR);
                        return;
                    }
                    for (int i = 0; i < dimsKernel.length; i++) {
                        if(dimsKernel[i] > dimsData[i]) {
                            VisNow.get().userMessageSend(this, "The dimensions of kernel field cannot be larger that the dimensions of data field.", "", Level.ERROR);
                            setOutputValue("outField", null);
                            return;
                        }
                    }
    
                    inFieldKernel = (RegularField) newInFieldKernel;
                    VisNow.get().userMessageSend(this, "Kernel field updated successfully.", inFieldKernel != null ? inFieldKernel.toMultilineString() : "", Level.INFO);
                }
            }
        }

        if (!params.isActive()) {
            return;
        }
        update();
    }
}
