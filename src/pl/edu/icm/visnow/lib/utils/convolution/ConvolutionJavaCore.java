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

import edu.emory.mathcs.jtransforms.fft.FloatFFT_1D;
import edu.emory.mathcs.jtransforms.fft.FloatFFT_2D;
import edu.emory.mathcs.jtransforms.fft.FloatFFT_3D;
import pl.edu.icm.visnow.lib.utils.PaddingType;
import pl.edu.icm.visnow.lib.utils.fft.FftCore;
import pl.edu.icm.visnow.lib.utils.field.ExtendMargins;

/**
 * @author Piotr Wendykier (piotrw@icm.edu.pl)
 */
public class ConvolutionJavaCore extends ConvolutionCore {

    private FloatFFT_1D fft1;
    private FloatFFT_2D fft2;
    private FloatFFT_3D fft3;

    /**
     * Creates a new
     * <code>ConvolutionJavaCore</code> object.
     */
    public ConvolutionJavaCore() {
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
        float[] fftData = new float[2 * length];
        float[] fftKernel = new float[2 * length];
        System.arraycopy(paddedData, 0, fftData, 0, length);
        System.arraycopy(paddedKernel, 0, fftKernel, 0, length);
        fft1 = null;
        fft1 = new FloatFFT_1D(length);
        fft1.realForwardFull(fftData);
        fft1.realForwardFull(fftKernel);
        for (int i = 0; i < length; i++) {
            float reData = fftData[2 * i];
            float imData = fftData[2 * i + 1];
            float reKernel = fftKernel[2 * i];
            float imKernel = fftKernel[2 * i + 1];
            fftData[2 * i] = reData * reKernel - imData * imKernel;
            fftData[2 * i + 1] = imData * reKernel + reData * imKernel;
        }
        fft1.complexInverse(fftData, true);
        for (int i = 0; i < length; i++) {
            paddedData[i] = fftData[2 * i];
        }
        paddedData = FftCore.circShift_1D(paddedData);
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
        float[] fftData = new float[2 * length];
        float[] fftKernel = new float[2 * length];
        System.arraycopy(paddedData, 0, fftData, 0, length);
        System.arraycopy(paddedKernel, 0, fftKernel, 0, length);
        fft2 = null;
        fft2 = new FloatFFT_2D(rowsPaddedData, colsPaddedData);
        fft2.realForwardFull(fftData);
        fft2.realForwardFull(fftKernel);
        for (int i = 0; i < length; i++) {
            float reData = fftData[2 * i];
            float imData = fftData[2 * i + 1];
            float reKernel = fftKernel[2 * i];
            float imKernel = fftKernel[2 * i + 1];
            fftData[2 * i] = reData * reKernel - imData * imKernel;
            fftData[2 * i + 1] = imData * reKernel + reData * imKernel;
        }
        fft2.complexInverse(fftData, true);
        for (int i = 0; i < length; i++) {
            paddedData[i] = fftData[2 * i];
        }
        paddedData = FftCore.circShift_2D(paddedData, rowsPaddedData, colsPaddedData);
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
        float[] fftData = new float[2 * length];
        float[] fftKernel = new float[2 * length];
        System.arraycopy(paddedData, 0, fftData, 0, length);
        System.arraycopy(paddedKernel, 0, fftKernel, 0, length);
        fft3 = null;
        fft3 = new FloatFFT_3D(slicesPaddedData, rowsPaddedData, colsPaddedData);
        fft3.realForwardFull(fftData);
        fft3.realForwardFull(fftKernel);
        for (int i = 0; i < length; i++) {
            float reData = fftData[2 * i];
            float imData = fftData[2 * i + 1];
            float reKernel = fftKernel[2 * i];
            float imKernel = fftKernel[2 * i + 1];
            fftData[2 * i] = reData * reKernel - imData * imKernel;
            fftData[2 * i + 1] = imData * reKernel + reData * imKernel;
        }
        fft3.complexInverse(fftData, true);
        int l = 0;
        for (int i = 0; i < length; i++) {
            paddedData[i] = fftData[2 * i];
        }
        paddedData = FftCore.circShift_3D(paddedData, slicesPaddedData, rowsPaddedData, colsPaddedData);

        for (int i = dataMargins[2][0]; i < slicesData + dataMargins[2][0]; i++) {
            for (int j = dataMargins[1][0]; j < rowsData + dataMargins[1][0]; j++) {
                for (int k = dataMargins[0][0]; k < colsData + dataMargins[0][0]; k++) {
                    data[l++] = paddedData[i * rowsPaddedData * colsPaddedData + j * colsPaddedData + k];
                }
            }
        }
    }
}
