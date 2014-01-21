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
package pl.edu.icm.visnow.lib.basic.utilities.KernelEditor;

import java.util.Arrays;
import pl.edu.icm.visnow.datasets.RegularField;
import pl.edu.icm.visnow.datasets.dataarrays.DataArray;

/**
 *
 * @author Piotr Wendykier (piotrw@icm.edu.pl)
 *
 */
public class Core {

    private Params params;
    private RegularField outField;

    public Core() {
    }

    public void setParams(Params params) {
        this.params = params;
    }

    public RegularField getOutField() {
        return outField;
    }

    public void update() {
        if (params == null) {
            throw new IllegalArgumentException("params argument cannot be null");
        }
        int size = 2 * params.getRadius() + 1;
        int rank = params.getRank();
        float[] data;
        float gaussianSigma = params.getGaussianSigma();
        boolean normalized = params.isNormalized();
        int dimsSize = 1;
        int type = params.getKernelType();


        if (rank == 1) {
            int[] dims = {size};
            outField = new RegularField(dims);
            dimsSize = size;
        } else if (rank == 2) {
            int[] dims = {size, size};
            outField = new RegularField(dims);
            dimsSize = size * size;
        } else if (rank == 3) {
            int[] dims = {size, size, size};
            outField = new RegularField(dims);
            dimsSize = size * size * size;
        }

        data = new float[dimsSize];

        float a = (size - 1) / 2.f;
        if (a < .5f) {
            a = .5f;
        }

        int i = 0;

        switch (rank) {
            case 1:
                if (type == Params.CONICAL) {
                    for (int x = 0; x < size; x++, i++) {
                        float u = Math.abs(x - a) / a;
                        float t = u * u;
                        if (t > 1) {
                            t = 0;
                        } else {
                            t = 1 - (float) Math.sqrt(1. * t);
                        }
                        data[i] = t;
                    }
                } else if (type == Params.LINEAR) {

                    for (int x = 0; x < size; x++, i++) {
                        float u = 1 - Math.abs(x - a) / a;
                        data[i] = u;
                    }
                } else if (type == Params.GAUSSIAN) {
                    gaussian1D(data, gaussianSigma, params.getRadius(), normalized);
                } else if (type == Params.CONSTANT) {

                    for (int x = 0; x < size; x++, i++) {
                        data[i] = 1.0f;

                    }
                } else if (type == Params.CUSTOM) {
                    if (data.length == params.getKernel().length) {
                        System.arraycopy(params.getKernel(), 0, data, 0, data.length);
                    } else {
                        Arrays.fill(data, 0);
                    }
                }
                break;
            case 2:
                if (type == Params.CONICAL) {
                    for (int y = 0; y < size; y++) {
                        for (int x = 0; x < size; x++, i++) {
                            float u = Math.abs(x - a) / a;
                            float v = Math.abs(y - a) / a;

                            float t = u * u + v * v;

                            if (t > 1) {
                                t = 0;
                            } else {
                                t = 1 - (float) Math.sqrt(1. * t);
                            }
                            data[i] = t;
                        }
                    }
                } else if (type == Params.LINEAR) {
                    for (int y = 0; y < size; y++) {
                        for (int x = 0; x < size; x++, i++) {
                            float u = 1 - Math.abs(x - a) / a;
                            if (u > 1 - Math.abs(y - a) / a) {
                                u = 1 - Math.abs(y - a) / a;
                            }
                            data[i] = u;
                        }
                    }
                } else if (type == Params.GAUSSIAN) {
                    gaussian2D(data, gaussianSigma, params.getRadius(), normalized);
                } else if (type == Params.CONSTANT) {
                    for (int y = 0; y < size; y++) {
                        for (int x = 0; x < size; x++, i++) {
                            data[i] = 1.0f;
                        }
                    }
                } else if (type == Params.CUSTOM) {
                    if (data.length == params.getKernel().length) {
                        System.arraycopy(params.getKernel(), 0, data, 0, data.length);
                    } else {
                        Arrays.fill(data, 0);
                    }
                }
                break;
            case 3:
                if (type == Params.CONICAL) {
                    for (int z = 0; z < size; z++) {

                        for (int y = 0; y < size; y++) {
                            for (int x = 0; x < size; x++, i++) {
                                float u = Math.abs(x - a) / a;
                                float v = Math.abs(y - a) / a;
                                float w = Math.abs(z - a) / a;
                                float t = u * u + v * v + w * w;
                                if (t > 1) {
                                    t = 0;
                                } else {
                                    t = 1 - (float) Math.sqrt(1. * t);
                                }
                                data[i] = t;
                            }
                        }
                    }
                } else if (type == Params.LINEAR) {
                    for (int z = 0; z < size; z++) {

                        for (int y = 0; y < size; y++) {
                            for (int x = 0; x < size; x++, i++) {
                                float u = 1 - Math.abs(x - a) / a;
                                if (u > 1 - Math.abs(z - a) / a) {
                                    u = 1 - Math.abs(z - a) / a;
                                }
                                data[i] = u;
                            }
                        }
                    }
                } else if (type == Params.GAUSSIAN) {
                    gaussian3D(data, gaussianSigma, params.getRadius(), normalized);
                } else if (type == Params.CONSTANT) {
                    for (int z = 0; z < size; z++) {

                        for (int y = 0; y < size; y++) {
                            for (int x = 0; x < size; x++, i++) {
                                data[i] = 1.0f;
                            }
                        }
                    }
                } else if (type == Params.CUSTOM) {
                    if (data.length == params.getKernel().length) {
                        System.arraycopy(params.getKernel(), 0, data, 0, data.length);
                    } else {
                        Arrays.fill(data, 0);
                    }
                }
                break;
        }
        if (normalized && type != Params.GAUSSIAN) {
            double data_sum = 0;

            for (int j = 0; j < data.length; j++) {
                data_sum += data[j];
            }

            if (data_sum != 0) {
                for (int j = 0; j < data.length; j++) {
                    data[j] = (float) (data[j] / data_sum);
                }
            }
        }

        outField.addData(DataArray.create(data, 1, "kernel"));
    }

    private static float computePDF(float mean, float sigma, float sample) {
        float SQRT_2_PI = (float) Math.sqrt(2 * Math.PI);
        float delta = sample - mean;
        return (float) Math.exp(-delta * delta / (2.0 * sigma * sigma)) / (sigma * SQRT_2_PI);
    }

    private static void normalizeSumToOne(float[] data) {
        float total = 0;
        for (int i = 0; i < data.length; i++) {
            total += data[i];
        }
        for (int i = 0; i < data.length; i++) {
            data[i] /= total;
        }
    }

    public static void gaussian1D(float[] gaussian, float sigma, int radius, boolean normalize) {
        int index = 0;

        for (int i = radius; i >= -radius; i--) {
            gaussian[index++] = computePDF(0, sigma, i);
        }

        if (normalize) {
            normalizeSumToOne(gaussian);
        }
    }

    private static void gaussian2D(float[] gaussian, float sigma, int radius, boolean normalize) {
        float[] kernel1D = new float[2 * radius + 1];
        gaussian1D(kernel1D, sigma, radius, false);
        convolve2D(gaussian, kernel1D, kernel1D);
        if (normalize) {
            normalizeSumToOne(gaussian);
        }
    }

    private static void gaussian3D(float[] gaussian, float sigma, int radius, boolean normalize) {
        float[] kernel1D = new float[2 * radius + 1];
        gaussian1D(kernel1D, sigma, radius, false);
        convolve3D(gaussian, kernel1D, kernel1D, kernel1D);
        if (normalize) {
            normalizeSumToOne(gaussian);
        }
    }

    private static void convolve2D(float[] ret, float[] a, float[] b) {
        int w = a.length;

        int index = 0;
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < w; j++) {
                ret[ index++] = a[i] * b[j];
            }
        }
    }

    private static void convolve3D(float[] ret, float[] a, float[] b, float[] c) {
        int w = a.length;

        int index = 0;
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < w; j++) {
                for (int k = 0; k < w; k++) {
                    ret[ index++] = a[i] * b[j] * c[k];
                }
            }
        }
    }
}
