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

import pl.edu.icm.visnow.datasets.RegularField;
import pl.edu.icm.visnow.datasets.dataarrays.DataArray;
import pl.edu.icm.visnow.datasets.dataarrays.FloatDataArray;
import pl.edu.icm.visnow.lib.utils.convolution.ConvolutionCore;

/**
 *
 * @author Piotr Wendykier(piotrw@icm.edu.pl)
 *
 */
public class Core {

    private Params params;
    private RegularField inFieldData = null;
    private RegularField inFieldKernel = null;
    private RegularField outField = null;
    private ConvolutionCore conv;

    public Core() {
        conv = ConvolutionCore.loadConvolutionLibrary();
    }

    public void setParams(Params params) {
        this.params = params;
    }

    public void update() {

        if (inFieldData == null || inFieldKernel == null) {
            outField = null;
            return;
        }

        int[] components = params.getComponents();
        
        if(components == null) {
            return;
        }
        
        outField = new RegularField(inFieldData.getDims());
        if (inFieldData.getCoords() == null) {
            outField.setAffine(inFieldData.getAffine());
        } else {
            outField.setCoords(inFieldData.getCoords());
        }

        DataArray kernel = inFieldKernel.getData(0);
        
        if(params.isNormalizeKernel()) {
            float[] kdata = kernel.getFData().clone();
            double sum = 0;
            for(int i = 0; i < kdata.length; i++) {
                sum += kdata[i];
            }
            for(int i = 0; i < kdata.length; i++) {
                kdata[i] /= sum;
            }
            kernel = new FloatDataArray(kdata, 1, kernel.getName());
        }

        

        for (int n = 0; n < components.length; n++) {
            DataArray data = inFieldData.getData(components[n]);
            conv.setInput(data, inFieldData.getDims(), kernel, inFieldKernel.getDims(), params.getPaddingType());
            conv.calculateConvolution();
            outField.addData(conv.getOutput());
        }

    }

    public void setInFieldData(RegularField field) {
        this.inFieldData = field;
    }

    public void setInFieldKernel(RegularField field) {
        this.inFieldKernel = field;
    }

    /**
     * @return the outField
     */
    public RegularField getOutField() {
        return outField;
    }
}
