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

package pl.edu.icm.visnow.lib.basic.filters.SimpleProjection;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import pl.edu.icm.visnow.datasets.RegularField;
import pl.edu.icm.visnow.engine.core.InputEgg;
import pl.edu.icm.visnow.engine.core.ModuleCore;
import pl.edu.icm.visnow.engine.core.OutputEgg;
import pl.edu.icm.visnow.lib.templates.visualization.modules.RegularOutFieldVisualizationModule;
import pl.edu.icm.visnow.lib.types.VNRegularField;
import pl.edu.icm.visnow.lib.utils.SwingInstancer;

/**
 * @author  Bartosz Borucki (babor@icm.edu.pl)
 * Warsaw University, Interdisciplinary Centre
 * for Mathematical and Computational Modelling
 */
public class SimpleProjection extends RegularOutFieldVisualizationModule {

    public static InputEgg[] inputEggs = null;
    public static OutputEgg[] outputEggs = null;
    private GUI gui;
    protected Params params;
    protected RegularField inField = null;
    protected boolean fromGUI = false;
    private Core3D core3d = new Core3D();
    private Core2D core2d = new Core2D();

    public SimpleProjection() {
        parameters = params = new Params();
        params.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent evt) {
                fromGUI = true;
                startAction();
            }
        });
        core3d.setParams(params);
        core2d.setParams(params);
        
        SwingInstancer.swingRunAndWait(new Runnable() {
            @Override
            public void run() {
                gui = new GUI();
                gui.setParams(params);
                ui.addComputeGUI(gui);
                setPanel(ui);
            }
        });        
    }

    @Override
    public void onActive() {
        if (getInputFirstValue("inField") == null
                || ((VNRegularField) getInputFirstValue("inField")).getField() == null) {
            outField = null;
            return;
        }
        if (!fromGUI) {
            inField = ((VNRegularField) getInputFirstValue("inField")).getField();
            if (inField == null || inField.getDims() == null || inField.getDims().length == 1) {
                outField = null;
                return;
            }

            if(inField.getDims().length == 2 && params.getLevels() == 2) {
                params.setLevels(1);
                if(params.getAxisL0() > 1) {
                    params.setAxisL0(0);
                }
            }
            gui.setInFieldDims(inField.getDims());
        } else {
            fromGUI = false;
        }

        if (inField.getDims().length == 3) {
            core3d.setInField(inField);
            core3d.update();
            if(params.getLevels() == 1) {
                outField = core3d.getOutField();
            } else {
                core2d.setInField(core3d.getOutField());
                core2d.update();
                outField = core2d.getOutField();
            }
        } else if (inField.getDims().length == 2) {
            core2d.setInField(inField);
            core2d.update();
            outField = core2d.getOutField();
        }
        setOutputValue("outField", new VNRegularField(outField));
        if(prepareOutputGeometry())
            show();
    }
}
