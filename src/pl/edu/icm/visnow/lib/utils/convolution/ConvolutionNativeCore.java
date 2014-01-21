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

import pl.edu.icm.visnow.lib.utils.PaddingType;
import pl.edu.icm.visnow.lib.utils.fft.FftCore;
import pl.edu.icm.visnow.lib.utils.fft.FftwCore;
import pl.edu.icm.visnow.lib.utils.field.ExtendMargins;

/**
 * @author Piotr Wendykier (piotrw@icm.edu.pl)
 */
public class ConvolutionNativeCore extends ConvolutionCore {
    private FftwCore fftwCore;    
    
    /**
     * Creates a new
     * <code>ConvolutionNativeCore</code> object.
     */
    public ConvolutionNativeCore(FftwCore fftwCore) {
        this.fftwCore = fftwCore;
    }

    @Override
    protected void convolve(float[] data, int dimsData, float[] kernel, int dimsKernel, PaddingType padding) {
        float[] paddedData;
        float[] paddedKernel;
        int[][] dataMargins = new int[1][2];
        int[][] kernelMargins = new int[1][2];
        int[] dimsPaddedData = {dimsData + dimsKernel};
        dataMargins[0][0] = (int) Math.floor(dimsKernel / 2.0);
        dataMargins[0][1] = (int) Math.floor((dimsKernel - 1) / 2.0);
        if ((dimsData + dataMargins[0][0] + dataMargins[0][1]) % 2 == 0) {
            dataMargins[0][1] += 1;
        }
        kernelMargins[0][0] = (int) Math.floor((dimsPaddedData[0] - dimsKernel) / 2.0);
        kernelMargins[0][1] = dimsPaddedData[0] - dimsKernel - kernelMargins[0][0];

        paddedData = ExtendMargins.extendMargins(new int[]{dimsData}, 1, dimsPaddedData, dataMargins, data, padding);
        paddedKernel = ExtendMargins.extendMargins(new int[]{dimsKernel}, 1, dimsPaddedData, kernelMargins, kernel, PaddingType.ZERO);

        int length = dimsPaddedData[0];
        float[] fftDataRe = new float[length];
        float[] fftDataIm = new float[length];
        float[] fftKernelRe = new float[length];
        float[] fftKernelIm = new float[length];
        System.arraycopy(paddedData, 0, fftDataRe, 0, length);
        System.arraycopy(paddedKernel, 0, fftKernelRe, 0, length);
        fftwCore.fft_r2c(fftDataRe, fftDataIm);
        fftwCore.fft_r2c(fftKernelRe, fftKernelIm);
        for (int i = 0; i < length; i++) {
            float reData = fftDataRe[i];
            float imData = fftDataIm[i];
            float reKernel = fftKernelRe[i];
            float imKernel = fftKernelIm[i];
            fftDataRe[i] = reData * reKernel - imData * imKernel;
            fftDataIm[i] = imData * reKernel + reData * imKernel;
        }
        fftwCore.ifft(fftDataRe, fftDataIm);
        paddedData = FftCore.circShift_1D(fftDataRe);
        for (int i = 0; i < dimsData; i++) {
            data[i] = paddedData[i + dataMargins[0][0]];
        }
    }

    @Override
    protected void convolve2(float[] data, int[] dimsData, float[] kernel, int[] dimsKernel, PaddingType padding) {
        int rowsData, colsData;
        int rowsPaddedData, colsPaddedData;
        float[] paddedData;
        float[] paddedKernel;
        int[][] dataMargins = new int[2][2];
        int[][] kernelMargins = new int[2][2];
        rowsData = dimsData[1];
        colsData = dimsData[0];

        for (int i = 0; i < 2; i++) {
            dataMargins[i][0] = (int) Math.floor(dimsKernel[i] / 2.0);
            dataMargins[i][1] = (int) Math.floor((dimsKernel[i] - 1) / 2.0);
            if ((dimsData[i] + dataMargins[i][0] + dataMargins[i][1]) % 2 == 0) {
                dataMargins[i][0] += 1;
            }
        }

        int[] dimsPaddedData = {dimsData[0] + dataMargins[0][0] + dataMargins[0][1], dimsData[1] + dataMargins[1][0] + dataMargins[1][1]};

        for (int i = 0; i < 2; i++) {
            kernelMargins[i][0] = (int) Math.floor((dimsPaddedData[i] - dimsKernel[i]) / 2.0);
            kernelMargins[i][1] = dimsPaddedData[i] - dimsKernel[i] - kernelMargins[i][0];
        }

        paddedData = ExtendMargins.extendMargins(dimsData, 1, dimsPaddedData, dataMargins, data, padding);
        paddedKernel = ExtendMargins.extendMargins(dimsKernel, 1, dimsPaddedData, kernelMargins, kernel, PaddingType.ZERO);

        rowsPaddedData = dimsPaddedData[1];
        colsPaddedData = dimsPaddedData[0];

        int length = rowsPaddedData * colsPaddedData;
        float[] fftDataRe = new float[length];
        float[] fftDataIm = new float[length];
        float[] fftKernelRe = new float[length];
        float[] fftKernelIm = new float[length];
        System.arraycopy(paddedData, 0, fftDataRe, 0, length);
        System.arraycopy(paddedKernel, 0, fftKernelRe, 0, length);
        fftwCore.fft2_r2c(fftDataRe, fftDataIm, colsPaddedData, rowsPaddedData);
        fftwCore.fft2_r2c(fftKernelRe, fftKernelIm, colsPaddedData, rowsPaddedData);
        for (int i = 0; i < length; i++) {
            float reData = fftDataRe[i];
            float imData = fftDataIm[i];
            float reKernel = fftKernelRe[i];
            float imKernel = fftKernelIm[i];
            fftDataRe[i] = reData * reKernel - imData * imKernel;
            fftDataIm[i] = imData * reKernel + reData * imKernel;
        }
        fftwCore.ifft2(fftDataRe, fftDataIm, colsPaddedData, rowsPaddedData);
        paddedData = FftCore.circShift_2D(fftDataRe, rowsPaddedData, colsPaddedData);
        int l = 0;
        for (int i = dataMargins[1][0]; i < rowsData + dataMargins[1][0]; i++) {
            for (int j = dataMargins[0][0]; j < colsData + dataMargins[0][0]; j++) {
                data[l++] = paddedData[i * colsPaddedData + j];
            }
        }
    }

    @Override
    protected void convolve3(float[] data, int[] dimsData, float[] kernel, int[] dimsKernel, PaddingType padding) {
        int slicesData, rowsData, colsData;
        int slicesPaddedData, rowsPaddedData, colsPaddedData;
        float[] paddedData;
        float[] paddedKernel;
        int[][] dataMargins = new int[3][2];
        int[][] kernelMargins = new int[3][2];
        slicesData = dimsData[2];
        rowsData = dimsData[1];
        colsData = dimsData[0];

        for (int i = 0; i < 3; i++) {
            dataMargins[i][0] = (int) Math.floor(dimsKernel[i] / 2.0);
            dataMargins[i][1] = (int) Math.floor((dimsKernel[i] - 1) / 2.0);
            if ((dimsData[i] + dataMargins[i][0] + dataMargins[i][1]) % 2 == 0) {
                dataMargins[i][0] += 1;
            }
        }

        int[] dimsPaddedData = {dimsData[0] + dataMargins[0][0] + dataMargins[0][1], dimsData[1] + dataMargins[1][0] + dataMargins[1][1], dimsData[2] + dataMargins[2][0] + dataMargins[2][1]};

        for (int i = 0; i < 3; i++) {
            kernelMargins[i][0] = (int) Math.floor((dimsPaddedData[i] - dimsKernel[i]) / 2.0);
            kernelMargins[i][1] = dimsPaddedData[i] - dimsKernel[i] - kernelMargins[i][0];
        }

        paddedData = ExtendMargins.extendMargins(dimsData, 1, dimsPaddedData, dataMargins, data, padding);
        paddedKernel = ExtendMargins.extendMargins(dimsKernel, 1, dimsPaddedData, kernelMargins, kernel, PaddingType.ZERO);

        slicesPaddedData = dimsPaddedData[2];
        rowsPaddedData = dimsPaddedData[1];
        colsPaddedData = dimsPaddedData[0];

        int length = slicesPaddedData * rowsPaddedData * colsPaddedData;
        float[] fftDataRe = new float[length];
        float[] fftDataIm = new float[length];
        float[] fftKernelRe = new float[length];
        float[] fftKernelIm = new float[length];
        System.arraycopy(paddedData, 0, fftDataRe, 0, length);
        System.arraycopy(paddedKernel, 0, fftKernelRe, 0, length);
        fftwCore.fft3_r2c(fftDataRe, fftDataIm, colsPaddedData, rowsPaddedData, slicesPaddedData);
        fftwCore.fft3_r2c(fftKernelRe, fftKernelIm, colsPaddedData, rowsPaddedData, slicesPaddedData);
        for (int i = 0; i < length; i++) {
            float reData = fftDataRe[i];
            float imData = fftDataIm[i];
            float reKernel = fftKernelRe[i];
            float imKernel = fftKernelIm[i];
            fftDataRe[i] = reData * reKernel - imData * imKernel;
            fftDataIm[i] = imData * reKernel + reData * imKernel;
        }
        fftwCore.ifft3(fftDataRe, fftDataIm, colsPaddedData, rowsPaddedData, slicesPaddedData);
        paddedData = FftCore.circShift_3D(fftDataRe, slicesPaddedData, rowsPaddedData, colsPaddedData);
        int l = 0;
        for (int i = dataMargins[2][0]; i < slicesData + dataMargins[2][0]; i++) {
            for (int j = dataMargins[1][0]; j < rowsData + dataMargins[1][0]; j++) {
                for (int k = dataMargins[0][0]; k < colsData + dataMargins[0][0]; k++) {
                    data[l++] = paddedData[i * rowsPaddedData * colsPaddedData + j * colsPaddedData + k];
                }
            }
        }
    }
}
