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
package pl.edu.icm.visnow.lib.basic.writers.ImageWriter;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.apache.log4j.Logger;
import pl.edu.icm.visnow.datasets.RegularField;
import pl.edu.icm.visnow.engine.core.InputEgg;
import pl.edu.icm.visnow.engine.core.LinkFace;
import pl.edu.icm.visnow.engine.core.OutputEgg;
import pl.edu.icm.visnow.geometries.parameters.DataMappingParams;
import pl.edu.icm.visnow.geometries.parameters.RegularFieldDisplayParams;
import pl.edu.icm.visnow.geometries.utils.ColorMapper;
import pl.edu.icm.visnow.lib.templates.visualization.modules.RegularOutFieldVisualizationModule;
import pl.edu.icm.visnow.lib.types.VNRegularField;
import pl.edu.icm.visnow.lib.utils.ImageUtilities;
import pl.edu.icm.visnow.lib.utils.SwingInstancer;

/**
 *
 * @author Bartosz Borucki (babor@icm.edu.pl) University of Warsaw,
 * Interdisciplinary Centre for Mathematical and Computational Modelling
 *
 */
public class ImageWriter extends RegularOutFieldVisualizationModule {

    public static InputEgg[] inputEggs = null;
    public static OutputEgg[] outputEggs = null;
    
    private static final Logger LOGGER = Logger.getLogger(ImageWriter.class);
    private ImageWriterGUI computeUI = null;
    protected ImageWriterParams params;

    public ImageWriter() {
        parameters = params = new ImageWriterParams();
        params.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent evt) {
                writeImage();
            }
        });

        SwingInstancer.swingRunAndWait(new Runnable() {
            @Override
            public void run() {
                computeUI = new ImageWriterGUI();
                computeUI.setParams(params);
                ui.addComputeGUI(computeUI);
                setPanel(ui);
            }
        });
    }

    @Override
    public void onActive() {
        if (getInputFirstValue("inField") == null) {
            return;
        }

        outField = ((VNRegularField) getInputFirstValue("inField")).getField();
        prepareOutputGeometry();
        show();
    }

    @Override
    public void onInputAttach(LinkFace link) {
        onActive();
    }

    private void writeImage() {
        if (outField == null || params.getFileName() == null) {
            return;
        }

        BufferedImage outImage = mapFieldToImage(outField, fieldDisplayParams.getMappingParams());
        if (params.isFlipHorizontal()) {
            outImage = ImageUtilities.flipImageHorizontal(outImage);
        }
        if (params.isFlipVertical()) {
            outImage = ImageUtilities.flipImageVertical(outImage);
        }

        if (outImage != null) {
            try {
                if (params.getFormat().equals("JPEG")) {
                    ImageUtilities.writeJpeg(outImage, 0.9f, new File(params.getFileName()));
                } else {
                    ImageUtilities.writePng(outImage, new File(params.getFileName()));
                }
            } catch (IOException ex) {
                LOGGER.error("Cannot write image to file " + params.getFileName());
            }
        }

    }

    private static BufferedImage mapFieldToImage(RegularField field, DataMappingParams mappingParams) {
        if (field == null) {
            return null;
        }

        int[] dims = field.getDims();
        int w = dims[0];
        int h = dims[1];
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        WritableRaster raster = image.getRaster();
        byte[] colors = null;
        colors = ColorMapper.map(field, mappingParams, colors);
        for (int j = 0, l = 0; j < h; j++) {
            for (int i = 0; i < w; i++, l++) {
                raster.setSample(i, j, 0, (int) (colors[4*l] & 0xff));
                raster.setSample(i, j, 1, (int) (colors[4*l + 1] & 0xff));
                raster.setSample(i, j, 2, (int) (colors[4*l + 2] & 0xff));
            }
        }
        return image;
    }
}
