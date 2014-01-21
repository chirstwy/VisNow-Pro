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

package pl.edu.icm.visnow.lib.basic.filters.SegmentationMask;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import pl.edu.icm.visnow.datasets.RegularField;
import pl.edu.icm.visnow.engine.core.InputEgg;
import pl.edu.icm.visnow.engine.core.LinkFace;
import pl.edu.icm.visnow.engine.core.ModuleCore;
import pl.edu.icm.visnow.engine.core.OutputEgg;
import pl.edu.icm.visnow.gui.events.FloatValueModificationEvent;
import pl.edu.icm.visnow.gui.events.FloatValueModificationListener;
import pl.edu.icm.visnow.lib.types.VNRegularField;
import pl.edu.icm.visnow.lib.utils.SwingInstancer;

/**
 *
 * @author babor
 */
public class SegmentationMask extends ModuleCore {

    private GUI ui = null;
    protected Params params;
    private Core core = new Core();
    private RegularField inField = null;
    private RegularField inSegmentationField = null;

    public SegmentationMask() {
        params = new Params();
        core.setParams(params);
        params.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent evt) {
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
    public static InputEgg[] inputEggs = null;
    public static OutputEgg[] outputEggs = null;
//
//    public static InputEgg[] getInputEggs() {
//        return new InputEgg[]{
//                    new InputEgg("inField", VNRegularField.class, InputEgg.NECESSARY | InputEgg.TRIGGERING | InputEgg.NORMAL),
//                    new InputEgg("inSegmentationField", VNRegularField.class, InputEgg.NECESSARY | InputEgg.TRIGGERING | InputEgg.NORMAL),};
//    }
//
//    public static OutputEgg[] getOutputEggs() {
//        return new OutputEgg[]{
//                    new OutputEgg("outField", VNRegularField.class),};
//    }

    @Override
    public void onActive() {
        if (getInputFirstValue("inField") != null) {
            RegularField newInField = ((VNRegularField) getInputFirstValue("inField")).getField();
            if (newInField != null && inField != newInField) {
                inField = newInField;
                int[] inDims = inField.getDims();
                if (inDims.length != 3
                        || inDims[0] < 2
                        || inDims[1] < 2
                        || inDims[2] < 2
                        || inField.getNData() < 1) {
                    return;
                }
                ui.setInField(inField);
            }
        }

        if (getInputFirstValue("inSegmentationField") != null) {
            RegularField newInSegmentationField = ((VNRegularField) getInputFirstValue("inSegmentationField")).getField();
            if (newInSegmentationField != null && inSegmentationField != newInSegmentationField) {
                inSegmentationField = newInSegmentationField;
                int[] inDims = inSegmentationField.getDims();
                if (inDims.length != 3
                        || inDims[0] < 2
                        || inDims[1] < 2
                        || inDims[2] < 2
                        || inSegmentationField.getNData() < 1) {
                    return;
                }
                ui.setInSegmentationField(inSegmentationField);
            }
        }


        core.setInField(inField);
        core.setSegmentationField(inSegmentationField);
        core.update();

        setOutputValue("outField", new VNRegularField(core.getOutField()));
    }

    @Override
    public void onInputDetach(LinkFace link) {
        if (link.getInput().getName().equals("inField")) {
            inField = null;
            ui.setInField(null);
        }
        if (link.getInput().getName().equals("inSegmentationField")) {
            inSegmentationField = null;
            ui.setInSegmentationField(null);
        }

    }
}
