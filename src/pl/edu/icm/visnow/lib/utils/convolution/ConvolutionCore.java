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
package pl.edu.icm.visnow.lib.utils.convolution;

import java.util.ArrayList;
import pl.edu.icm.visnow.datasets.dataarrays.DataArray;
import pl.edu.icm.visnow.datasets.dataarrays.FloatDataArray;
import pl.edu.icm.visnow.lib.utils.PaddingType;
import pl.edu.icm.visnow.lib.utils.fft.FftwCore;
import pl.edu.icm.visnow.system.main.VisNow;

/**
 * @author Piotr Wendykier (piotrw@icm.edu.pl)
 */
public abstract class ConvolutionCore {

    protected float[][] data = null;
    ArrayList<Float> timeSeries = null;
    protected String nameData = "";
    protected int[] dimsData;
    protected float[] kernel = null;
    protected String nameKernel = "";
    protected int[] dimsKernel;
    protected PaddingType padding = PaddingType.FIXED;
    protected boolean working = false;
    protected boolean done = false;

    /**
     * Creates a new
     * <code>ConvolutionCore</code> object.
     */
    public ConvolutionCore() {
    }

    protected abstract void convolve(float[] data, int dimsData, float[] kernel, int dimsKernel, PaddingType padding);

    protected abstract void convolve2(float[] data, int[] dimsData, float[] kernel, int[] dimsKernel, PaddingType padding);

    protected abstract void convolve3(float[] data, int[] dimsData, float[] kernel, int[] dimsKernel, PaddingType padding);

    /**
     * Runs convolution calculations on the previously set input data, with the
     * loaded core
     */
    public void calculateConvolution() {
        if (data == null || kernel == null || dimsData == null || dimsKernel == null || dimsData.length != dimsKernel.length) {
            return;
        }
        working = true;
        if (dimsData.length == 1) {
            for(int i = 0; i < data.length; i++) {
                convolve(data[i], dimsData[0], kernel, dimsKernel[0], padding);
            }
        } else if (dimsData.length == 2) {
            for(int i = 0; i < data.length; i++) {
                convolve2(data[i], dimsData, kernel, dimsKernel, padding);
            }
        } else if (dimsData.length == 3) {
            for(int i = 0; i < data.length; i++) {
                convolve3(data[i], dimsData, kernel, dimsKernel, padding);
            }
        }
        working = false;
        done = true;
    }

    /**
     * Sets 1D input data for convolution calculations. Dimension is calculated
     * as data length.
     *
     * @param	inData	Input data array.
     * @param _dimsData
     * @param inKernel
     * @param _dimsKernel
     * @param padding
     */
    public void setInput(DataArray inData, int[] _dimsData, DataArray inKernel, int[] _dimsKernel, PaddingType padding) {
        if (inData == null || inData.getVeclen() > 1 || inKernel == null || inKernel.getVeclen() > 1 || _dimsData.length != _dimsKernel.length) {
            data = null;
            kernel = null;
            dimsData = null;
            dimsKernel = null;
            return;
        }
        this.done = false;
        this.dimsData = new int[_dimsData.length];
        this.dimsKernel = new int[_dimsData.length];

        for (int i = 0; i < dimsData.length; i++) {
            if (_dimsKernel[i] > _dimsData[i]) {
                return;
            } else {
                this.dimsData[i] = _dimsData[i];
                this.dimsKernel[i] = _dimsKernel[i];
            }
        }
        int frames = inData.getNFrames();
        this.data = new float[frames][];
        if(frames == 1) {
           data[0] = inData.getFData().clone();
        }
        else {
            timeSeries = inData.getTimeSeries();
            int i = 0;
            float currentTime = inData.getCurrentTime();
            for (Float t : timeSeries) {
                inData.setCurrentTime(t);
                data[i++] = inData.getFData().clone();
            }
            inData.setCurrentTime(currentTime);
        }
        this.nameData = inData.getName();
        this.kernel = inKernel.getFData().clone();
        this.nameKernel = inKernel.getName();
        this.padding = padding;
    }

    /**
     * Returns the result.
     *
     * @return	A <code>float[]</code> object.
     */
    public DataArray getOutput() {
        if (done) {
            if(data.length == 1) {
                return new FloatDataArray(data[0], 1, nameData + " convolved with " + nameKernel);
            }
            else {
                FloatDataArray res = new FloatDataArray(data[0].length, 1);
                res.setName(nameData + " convolved with " + nameKernel);
                int i = 0;
                for (Float t : timeSeries) {
                     res.addData(data[i++], t);
                }
                res.setCurrentTime(timeSeries.get(0));
                return res;
            }
        }
        return null;
    }

    /**
     * Returns true if calculations are in progress, false otherwise.
     *
     * @return	<code>boolean</code> value.
     */
    public boolean isWorking() {
        return working;
    }

    /**
     * Static procedure that loads Convolution core library. It tries to load
     * the native FFTW library for the current architecture and OS. If fails it
     * loads the default Java Convolution core.
     *
     * @return	<code>ConvolutionCore</code> object - instance of ready
     * convolution calculation core
     */
    public static ConvolutionCore loadConvolutionLibrary() {
        if (VisNow.isNativeLibraryLoaded("fftw")) {
            return new ConvolutionNativeCore(new FftwCore());
        } else {
            return new ConvolutionJavaCore();
        }
    }
}
