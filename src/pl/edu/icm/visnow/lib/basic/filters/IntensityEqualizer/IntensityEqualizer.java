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

package pl.edu.icm.visnow.lib.basic.filters.IntensityEqualizer;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import pl.edu.icm.visnow.datasets.RegularField;
import pl.edu.icm.visnow.engine.core.InputEgg;
import pl.edu.icm.visnow.engine.core.ModuleCore;
import pl.edu.icm.visnow.engine.core.OutputEgg;
import pl.edu.icm.visnow.gui.events.FloatValueModificationEvent;
import pl.edu.icm.visnow.gui.events.FloatValueModificationListener;
import pl.edu.icm.visnow.lib.types.VNRegularField;
import pl.edu.icm.visnow.lib.utils.SwingInstancer;

/**
 *
 * @author Bartosz Borucki (babor@icm.edu.pl)
 * University of Warsaw, Interdisciplinary Centre
 * for Mathematical and Computational Modelling
 */
public class IntensityEqualizer extends ModuleCore {

    public static InputEgg[] inputEggs = null;
    public static OutputEgg[] outputEggs = null;
    private GUI ui = null;
    private Core core = new Core();
    protected Params params;
    RegularField inField = null;
    private boolean onlyOutput = false;
    private boolean forceRecreateWeights = false;

    private boolean oldFullPaddingValue;

    public IntensityEqualizer() {
        params = new Params();
        oldFullPaddingValue = params.isFullPadding();
        core.setParams(params);
        params.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent evt) {
                if(oldFullPaddingValue == params.isFullPadding()) {
                    onlyOutput = true;
                } else {
                    forceRecreateWeights = true;
                }
                startAction();
            }
        });
        
        SwingInstancer.swingRunAndWait(new Runnable() {
            public void run() {
                ui = new GUI();
            }
        });
        ui.setParams(params);
        setPanel(ui);

        core.addFloatValueModificationListener(new FloatValueModificationListener() {
            public void floatValueChanged(FloatValueModificationEvent e) {
                setProgress(e.getVal());
            }
        });
    }

    @Override
    public void onActive() {

        if (getInputFirstValue("inField") == null) {
            return;
        }
        RegularField inFld = ((VNRegularField) getInputFirstValue("inField")).getField();
        if (inFld == null) {
            return;
        }
        if (!onlyOutput) {
            core.setInField(inFld);

            if(inField == null || !inFld.isStructureCompatibleWith(inField) || forceRecreateWeights) {
                params.setupWeights(core.getPreprocessedInField());
                ui.recreateGUI();
                forceRecreateWeights = false;
            }
            
            inField = inFld;
            core.updatePyramid();            
        }

        core.updateOutput();
        setOutputValue("outField", new VNRegularField(core.getOutField()));

//        DataStruct ds = core.test;
//        RegularField rf = new RegularField(ds.getDims());
//        rf.setAffine(inFld.getAffine());
//        rf.addData(DataArray.create(ds.getData(), 1, "test"));
//        setOutputValue("testField", new VNRegularField(rf));

        onlyOutput = false;
        oldFullPaddingValue = params.isFullPadding();
    }
}
