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
package pl.edu.icm.visnow.lib.utils.field;

import pl.edu.icm.visnow.lib.utils.PaddingType;

/**
 *
 * @author Krzysztof S. Nowinski
 * <p> University of Warsaw, ICM
 */
public class ExtendMargins {

    /**
     * Extends float array
     * <code>data</code> (regarded as array of dimensions
     * <code>dims</code>) by
     * <code>margins</code>. New array has dimensions
     * <code>extDims</code>,
     * <code>extDims[i] = margins[i][0] + dims[i] + margins[i][1]</code>.
     *
     * @param dims - dimensions of original array
     * @param vlen - length of vectors elements of original array
     * @param extDims - dimensions of extended array
     * @param margins - margin widths
     * @param data - original data array of length <code>vlen * dims[0] *
     * ...</code>
     * @param method - if <code>PaddingType.ZERO</code>, margin is filled by
     * zeros, if <code>PaddingType.FIXED</code>, margin is filled by nearest
     * values of original array, if <code>PaddingType.PERIODIC</code>, margin is
     * filled by periodic extension, if <code>PaddingType.REFLECTED</code>, margin is
     * filled by reflected extension.
     * @return extended data array of length <code>vlen * extDims[0] *
     * ...</code>
     */
    public static float[] extendMargins(int[] dims, int vlen, int[] extDims, int[][] margins, float[] data, PaddingType method) {
        int nExt = 1;
        for (int i = 0; i < dims.length; i++) {
            extDims[i] = dims[i] + margins[i][0] + margins[i][1];
            nExt *= extDims[i];
        }
        float[] extData = new float[vlen * nExt];
        for (int i = 0; i < extData.length; i++) {
            extData[i] = 0;
        }
      switch (dims.length) {
            case 3:
                if (method == PaddingType.ZERO) {
                    for (int i = 0, m = 0; i < dims[2]; i++) {
                        for (int j = 0; j < dims[1]; j++) {
                            for (int k = 0, l = ((margins[2][0] + i) * extDims[1] + margins[1][0] + j) * extDims[0] + margins[0][0]; k < dims[0]; k++, l++, m++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l * vlen + n] = data[m * vlen + n];
                                }
                            }
                        }
                    }
                } else if (method == PaddingType.FIXED) {
                    for (int i = 0, m = 0; i < dims[2]; i++) {
                        for (int j = 0; j < dims[1]; j++) {
                            for (int k = 0, l = ((margins[2][0] + i) * extDims[1] + margins[1][0] + j) * extDims[0] + margins[0][0]; k < dims[0]; k++, l++, m++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l * vlen + n] = data[m * vlen + n];
                                }
                            }
                        }
                    }
                    int l = 0;
                    for (int i = 0; i < dims[2]; i++) {
                        for (int j = 0; j < dims[1]; j++) {
                            l = (((margins[2][0] + i) * extDims[1] + margins[1][0] + j) * extDims[0] + margins[0][0]) * vlen;
                            for (int k = 1; k <= margins[0][0]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l - k * vlen + n] = extData[l + n];
                                }
                            }
                            l = (((margins[2][0] + i) * extDims[1] + margins[1][0] + j) * extDims[0] + margins[0][0] + dims[0] - 1) * vlen;
                            for (int k = 1; k <= margins[0][1]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l + k * vlen + n] = extData[l + n];
                                }
                            }
                        }
                    }
                    int m = extDims[0];
                    for (int i = 0; i < dims[2]; i++) {
                        for (int j = 0; j < extDims[0]; j++) {
                            l = (((margins[2][0] + i) * extDims[1] + margins[1][0]) * extDims[0] + j) * vlen;
                            for (int k = 1; k <= margins[1][0]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l - k * m * vlen + n] = extData[l + n];
                                }
                            }
                            l = (((margins[2][0] + i) * extDims[1] + margins[1][0] + dims[1] - 1) * extDims[0] + j) * vlen;
                            for (int k = 1; k <= margins[1][1]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l + k * m * vlen + n] = extData[l + n];
                                }
                            }
                        }
                    }
                    m = extDims[1] * extDims[0];
                    for (int i = 0; i < extDims[1]; i++) {
                        for (int j = 0; j < extDims[0]; j++) {
                            l = ((margins[2][0] * extDims[1] + i) * extDims[0] + j) * vlen;
                            for (int k = 1; k <= margins[2][0]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l - k * m * vlen + n] = extData[l + n];
                                }
                            }
                            l = (((margins[2][0] + dims[2] - 1) * extDims[1] + i) * extDims[0] + j) * vlen;
                            for (int k = 1; k <= margins[2][1]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l + k * m * vlen + n] = extData[l + n];
                                }
                            }
                        }
                    }
                } else if (method == PaddingType.PERIODIC) {
                    int sOff = margins[2][0];
                    int rOff = margins[1][0];
                    int cOff = margins[0][0];
                    for (int s = -sOff; s < extDims[2] - sOff; s++) {
                        int sOut = s + sOff;
                        int sIn = periodic(s, dims[2]);
                        for (int r = -rOff; r < extDims[1] - rOff; r++) {
                            int rOut = r + rOff;
                            int rIn = periodic(r, dims[1]);
                            for (int c = -cOff; c < extDims[0] - cOff; c++) {
                                int cOut = c + cOff;
                                int cIn = periodic(c, dims[0]);
                                int idx = (sIn * dims[1] * dims[0] + rIn * dims[0] + cIn) * vlen;
                                int idxExt = (sOut * extDims[1] * extDims[0] + rOut * extDims[0] + cOut) * vlen;
                                for (int n = 0; n < vlen; n++) {
                                    extData[idxExt + n] = data[idx + n];
                                }
                            }
                        }
                    }
                } else if (method == PaddingType.REFLECTED) {
                    int sOff = margins[2][0];
                    int rOff = margins[1][0];
                    int cOff = margins[0][0];
                    for (int s = -sOff; s < extDims[2] - sOff; s++) {
                        int sOut = s + sOff;
                        int sIn = reflected(s, dims[2]);
                        for (int r = -rOff; r < extDims[1] - rOff; r++) {
                            int rOut = r + rOff;
                            int rIn = reflected(r, dims[1]);
                            for (int c = -cOff; c < extDims[0] - cOff; c++) {
                                int cOut = c + cOff;
                                int cIn = reflected(c, dims[0]);
                                int idx = (sIn * dims[1] * dims[0] + rIn * dims[0] + cIn) * vlen;
                                int idxExt = (sOut * extDims[1] * extDims[0] + rOut * extDims[0] + cOut) * vlen;
                                for (int n = 0; n < vlen; n++) {
                                    extData[idxExt + n] = data[idx + n];
                                }
                            }
                        }
                    }
                }
                break;
            case 2:
                if (method == PaddingType.ZERO) {
                    for (int j = 0, m = 0; j < dims[1]; j++) {
                        for (int k = 0, l = (margins[1][0] + j) * extDims[0] + margins[0][0]; k < dims[0]; k++, l++, m++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l * vlen + n] = data[m * vlen + n];
                            }
                        }
                    }
                } else if (method == PaddingType.FIXED) {
                    for (int j = 0, m = 0; j < dims[1]; j++) {
                        for (int k = 0, l = (margins[1][0] + j) * extDims[0] + margins[0][0]; k < dims[0]; k++, l++, m++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l * vlen + n] = data[m * vlen + n];
                            }
                        }
                    }
                    int l = 0;
                    for (int j = 0; j < dims[1]; j++) {
                        l = ((margins[1][0] + j) * extDims[0] + margins[0][0]) * vlen;
                        for (int k = 1; k <= margins[0][0]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l - k * vlen + n] = extData[l + n];
                            }
                        }
                        l = ((margins[1][0] + j) * extDims[0] + margins[0][0] + dims[0] - 1) * vlen;
                        for (int k = 1; k <= margins[0][1]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l + k * vlen + n] = extData[l + n];
                            }
                        }
                    }
                    int m = extDims[0];
                    for (int j = 0; j < extDims[0]; j++) {
                        l = ((margins[1][0]) * extDims[0] + j) * vlen;
                        for (int k = 1; k <= margins[1][0]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l - k * m * vlen + n] = extData[l + n];
                            }
                        }
                        l = ((margins[1][0] + dims[1] - 1) * extDims[0] + j) * vlen;
                        for (int k = 1; k <= margins[1][1]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l + k * m * vlen + n] = extData[l + n];
                            }
                        }
                    }
                } else if (method == PaddingType.PERIODIC) {
                    int rOff = margins[1][0];
                    int cOff = margins[0][0];
                    for (int r = -rOff; r < extDims[1] - rOff; r++) {
                        int rOut = r + rOff;
                        int rIn = periodic(r, dims[1]);
                        for (int c = -cOff; c < extDims[0] - cOff; c++) {
                            int cOut = c + cOff;
                            int cIn = periodic(c, dims[0]);
                            int idx = (rIn * dims[0] + cIn) * vlen;
                            int idxExt = (rOut * extDims[0] + cOut) * vlen;
                            for (int n = 0; n < vlen; n++) {
                                extData[idxExt + n] = data[idx + n];
                            }
                        }
                    }

                } else if (method == PaddingType.REFLECTED) {
                    int rOff = margins[1][0];
                    int cOff = margins[0][0];
                    for (int r = -rOff; r < extDims[1] - rOff; r++) {
                        int rOut = r + rOff;
                        int rIn = reflected(r, dims[1]);
                        for (int c = -cOff; c < extDims[0] - cOff; c++) {
                            int cOut = c + cOff;
                            int cIn = reflected(c, dims[0]);
                            int idx = (rIn * dims[0] + cIn) * vlen;
                            int idxExt = (rOut * extDims[0] + cOut) * vlen;
                            for (int n = 0; n < vlen; n++) {
                                extData[idxExt + n] = data[idx + n];
                            }
                        }
                    }

                }
                break;
            case 1:
                if (method == PaddingType.ZERO) {
                    for (int k = 0, m = 0, l = margins[0][0]; k < dims[0]; k++, l++, m++) {
                        for (int n = 0; n < vlen; n++) {
                            extData[l * vlen + n] = data[m * vlen + n];
                        }
                    }
                } else if (method == PaddingType.FIXED) {
                    for (int k = 0, m = 0, l = margins[0][0]; k < dims[0]; k++, l++, m++) {
                        for (int n = 0; n < vlen; n++) {
                            extData[l * vlen + n] = data[m * vlen + n];
                        }
                    }
                    int l = (margins[0][0]) * vlen;
                    for (int k = 1; k <= margins[0][0]; k++) {
                        for (int n = 0; n < vlen; n++) {
                            extData[l - k * vlen + n] = extData[l + n];
                        }
                    }
                    l = (margins[0][0] + dims[0] - 1) * vlen;
                    for (int k = 1; k <= margins[0][1]; k++) {
                        for (int n = 0; n < vlen; n++) {
                            extData[l + k * vlen + n] = extData[l + n];
                        }
                    }
                } else if (method == PaddingType.PERIODIC) {
                    int cOff = margins[0][0];
                    for (int c = -cOff; c < extDims[0] - cOff; c++) {
                        int cOut = c + cOff;
                        int cIn = periodic(c, dims[0]);
                        int idx = cIn * vlen;
                        int idxExt = cOut * vlen;
                        for (int n = 0; n < vlen; n++) {
                            extData[idxExt + n] = data[idx + n];
                        }
                    }
                } else if (method == PaddingType.REFLECTED) {
                    int cOff = margins[0][0];
                    for (int c = -cOff; c < extDims[0] - cOff; c++) {
                        int cOut = c + cOff;
                        int cIn = reflected(c, dims[0]);
                        int idx = cIn * vlen;
                        int idxExt = cOut * vlen;
                        for (int n = 0; n < vlen; n++) {
                            extData[idxExt + n] = data[idx + n];
                        }
                    }
                }
                break;
        }
        return extData;
    }

    /*public static float[] extendMargins(int[] dims, int vlen, int[] extDims, int[][] margins, float[] data, int[][] method) {
        int nExt = 1;
        for (int i = 0; i < dims.length; i++) {
            extDims[i] = dims[i] + margins[i][0] + margins[i][1];
            nExt *= extDims[i];
        }
        float[] extData = new float[vlen * nExt];
        for (int i = 0; i < extData.length; i++) {
            extData[i] = 0;
        }
        switch (dims.length) {
            case 3: {
                for (int i = 0, m = 0; i < dims[2]; i++) {
                    for (int j = 0; j < dims[1]; j++) {
                        for (int k = 0, l = ((margins[2][0] + i) * extDims[1] + margins[1][0] + j) * extDims[0] + margins[0][0];
                                k < dims[0]; k++, l++, m++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l * vlen + n] = data[m * vlen + n];
                            }
                        }
                    }
                }
                int l = 0;
                for (int i = 0; i < dims[2]; i++) {
                    for (int j = 0; j < dims[1]; j++) {
                        l = (((margins[2][0] + i) * extDims[1] + margins[1][0] + j) * extDims[0] + margins[0][0]) * vlen;
                        if (method[0][0] >= PaddingType.FIXED) {
                            for (int k = 1; k <= margins[0][0]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l - k * vlen + n] = extData[l + n];
                                }
                            }
                        } else {
                            for (int k = 1; k <= margins[0][0]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l - k * vlen + n] = 0;
                                }
                            }
                        }
                        l = (((margins[2][0] + i) * extDims[1] + margins[1][0] + j) * extDims[0] + margins[0][0] + dims[0] - 1) * vlen;
                        if (method[0][1] >= PaddingType.FIXED) {
                            for (int k = 1; k <= margins[0][1]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l + k * vlen + n] = extData[l + n];
                                }
                            }
                        } else {
                            for (int k = 1; k <= margins[0][1]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l + k * vlen + n] = 0;
                                }
                            }
                        }
                    }
                }
                int m = extDims[0];
                for (int i = 0; i < dims[2]; i++) {
                    for (int j = 0; j < extDims[0]; j++) {
                        l = (((margins[2][0] + i) * extDims[1] + margins[1][0]) * extDims[0] + j) * vlen;
                        if (method[1][0] >= PaddingType.FIXED) {
                            for (int k = 1; k <= margins[1][0]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l - k * m * vlen + n] = extData[l + n];
                                }
                            }
                        } else {
                            for (int k = 1; k <= margins[1][0]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l - k * m * vlen + n] = 0;
                                }
                            }
                        }
                        l = (((margins[2][0] + i) * extDims[1] + margins[1][0] + dims[1] - 1) * extDims[0] + j) * vlen;
                        if (method[1][1] >= PaddingType.FIXED) {
                            for (int k = 1; k <= margins[1][1]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l + k * m * vlen + n] = extData[l + n];
                                }
                            }
                        } else {
                            for (int k = 1; k <= margins[1][1]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l + k * m * vlen + n] = 0;
                                }
                            }
                        }
                    }
                }
                m = extDims[1] * extDims[0];
                for (int i = 0; i < extDims[1]; i++) {
                    for (int j = 0; j < extDims[0]; j++) {
                        l = ((margins[2][0] * extDims[1] + i) * extDims[0] + j) * vlen;
                        if (method[2][0] >= PaddingType.FIXED) {
                            for (int k = 1; k <= margins[2][0]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l - k * m * vlen + n] = extData[l + n];
                                }
                            }
                        } else {
                            for (int k = 1; k <= margins[2][0]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l - k * m * vlen + n] = 0;
                                }
                            }
                        }
                        l = (((margins[2][0] + dims[2] - 1) * extDims[1] + i) * extDims[0] + j) * vlen;
                        if (method[2][1] >= PaddingType.FIXED) {
                            for (int k = 1; k <= margins[2][1]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l + k * m * vlen + n] = extData[l + n];
                                }
                            }
                        } else {
                            for (int k = 1; k <= margins[2][1]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l + k * m * vlen + n] = 0;
                                }
                            }
                        }
                    }
                }
            }
            break;
            case 2:
                for (int j = 0, m = 0; j < dims[1]; j++) {
                    for (int k = 0, l = (margins[1][0] + j) * extDims[0] + margins[0][0];
                            k < dims[0]; k++, l++, m++) {
                        for (int n = 0; n < vlen; n++) {
                            extData[l * vlen + n] = data[m * vlen + n];
                        }
                    }
                } {
                int l = 0;
                for (int j = 0; j < dims[1]; j++) {
                    l = ((margins[1][0] + j) * extDims[0] + margins[0][0]) * vlen;
                    if (method[0][0] >= PaddingType.FIXED) {
                        for (int k = 1; k <= margins[0][0]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l - k * vlen + n] = extData[l + n];
                            }
                        }
                    } else {
                        for (int k = 1; k <= margins[0][0]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l - k * vlen + n] = 0;
                            }
                        }
                    }
                    l = ((margins[1][0] + j) * extDims[0] + margins[0][0] + dims[0] - 1) * vlen;
                    if (method[0][1] >= PaddingType.FIXED) {
                        for (int k = 1; k <= margins[0][1]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l + k * vlen + n] = extData[l + n];
                            }
                        }
                    } else {
                        for (int k = 1; k <= margins[0][1]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l + k * vlen + n] = 0;
                            }
                        }
                    }
                }
                int m = extDims[0];
                for (int j = 0; j < extDims[0]; j++) {
                    l = ((margins[1][0]) * extDims[0] + j) * vlen;
                    if (method[1][0] >= PaddingType.FIXED) {
                        for (int k = 1; k <= margins[1][0]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l - k * m * vlen + n] = extData[l + n];
                            }
                        }
                    } else {
                        for (int k = 1; k <= margins[1][0]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l - k * m * vlen + n] = 0;
                            }
                        }
                    }
                    l = ((margins[1][0] + dims[1] - 1) * extDims[0] + j) * vlen;
                    if (method[1][1] >= PaddingType.FIXED) {
                        for (int k = 1; k <= margins[1][1]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l + k * m * vlen + n] = extData[l + n];
                            }
                        }
                    } else {
                        for (int k = 1; k <= margins[1][1]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l + k * m * vlen + n] = 0;
                            }
                        }
                    }
                }
            }
            break;
            case 1:
                for (int k = 0, m = 0, l = margins[0][0];
                        k < dims[0]; k++, l++, m++) {
                    for (int n = 0; n < vlen; n++) {
                        extData[l * vlen + n] = data[m * vlen + n];
                    }
                } {
                int l = (margins[0][0]) * vlen;
                if (method[0][0] >= PaddingType.FIXED) {
                    for (int k = 1; k <= margins[0][0]; k++) {
                        for (int n = 0; n < vlen; n++) {
                            extData[l - k * vlen + n] = extData[l + n];
                        }
                    }
                } else {
                    for (int k = 1; k <= margins[0][0]; k++) {
                        for (int n = 0; n < vlen; n++) {
                            extData[l - k * vlen + n] = 0;
                        }
                    }
                }
                l = (margins[0][0] + dims[0] - 1) * vlen;
                if (method[0][1] >= PaddingType.FIXED) {
                    for (int k = 1; k <= margins[0][1]; k++) {
                        for (int n = 0; n < vlen; n++) {
                            extData[l + k * vlen + n] = extData[l + n];
                        }
                    }
                } else {
                    for (int k = 1; k <= margins[0][1]; k++) {
                        for (int n = 0; n < vlen; n++) {
                            extData[l + k * vlen + n] = 0;
                        }
                    }
                }
            }
            break;
        }
        return extData;
    }*/

    public static byte[] extendMargins(int[] dims, int vlen, int[] extDims, int[][] margins, byte[] data, PaddingType method) {
        int nExt = 1;
        for (int i = 0; i < dims.length; i++) {
            extDims[i] = dims[i] + margins[i][0] + margins[i][1];
            nExt *= extDims[i];
        }
        byte[] extData = new byte[vlen * nExt];
        for (int i = 0; i < extData.length; i++) {
            extData[i] = 0;
        }
        switch (dims.length) {
            case 3:
                if (method == PaddingType.ZERO) {
                    for (int i = 0, m = 0; i < dims[2]; i++) {
                        for (int j = 0; j < dims[1]; j++) {
                            for (int k = 0, l = ((margins[2][0] + i) * extDims[1] + margins[1][0] + j) * extDims[0] + margins[0][0]; k < dims[0]; k++, l++, m++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l * vlen + n] = data[m * vlen + n];
                                }
                            }
                        }
                    }
                } else if (method == PaddingType.FIXED) {
                    for (int i = 0, m = 0; i < dims[2]; i++) {
                        for (int j = 0; j < dims[1]; j++) {
                            for (int k = 0, l = ((margins[2][0] + i) * extDims[1] + margins[1][0] + j) * extDims[0] + margins[0][0]; k < dims[0]; k++, l++, m++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l * vlen + n] = data[m * vlen + n];
                                }
                            }
                        }
                    }
                    int l = 0;
                    for (int i = 0; i < dims[2]; i++) {
                        for (int j = 0; j < dims[1]; j++) {
                            l = (((margins[2][0] + i) * extDims[1] + margins[1][0] + j) * extDims[0] + margins[0][0]) * vlen;
                            for (int k = 1; k <= margins[0][0]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l - k * vlen + n] = extData[l + n];
                                }
                            }
                            l = (((margins[2][0] + i) * extDims[1] + margins[1][0] + j) * extDims[0] + margins[0][0] + dims[0] - 1) * vlen;
                            for (int k = 1; k <= margins[0][1]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l + k * vlen + n] = extData[l + n];
                                }
                            }
                        }
                    }
                    int m = extDims[0];
                    for (int i = 0; i < dims[2]; i++) {
                        for (int j = 0; j < extDims[0]; j++) {
                            l = (((margins[2][0] + i) * extDims[1] + margins[1][0]) * extDims[0] + j) * vlen;
                            for (int k = 1; k <= margins[1][0]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l - k * m * vlen + n] = extData[l + n];
                                }
                            }
                            l = (((margins[2][0] + i) * extDims[1] + margins[1][0] + dims[1] - 1) * extDims[0] + j) * vlen;
                            for (int k = 1; k <= margins[1][1]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l + k * m * vlen + n] = extData[l + n];
                                }
                            }
                        }
                    }
                    m = extDims[1] * extDims[0];
                    for (int i = 0; i < extDims[1]; i++) {
                        for (int j = 0; j < extDims[0]; j++) {
                            l = ((margins[2][0] * extDims[1] + i) * extDims[0] + j) * vlen;
                            for (int k = 1; k <= margins[2][0]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l - k * m * vlen + n] = extData[l + n];
                                }
                            }
                            l = (((margins[2][0] + dims[2] - 1) * extDims[1] + i) * extDims[0] + j) * vlen;
                            for (int k = 1; k <= margins[2][1]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l + k * m * vlen + n] = extData[l + n];
                                }
                            }
                        }
                    }
                } else if (method == PaddingType.PERIODIC) {
                    int sOff = margins[2][0];
                    int rOff = margins[1][0];
                    int cOff = margins[0][0];
                    for (int s = -sOff; s < extDims[2] - sOff; s++) {
                        int sOut = s + sOff;
                        int sIn = periodic(s, dims[2]);
                        for (int r = -rOff; r < extDims[1] - rOff; r++) {
                            int rOut = r + rOff;
                            int rIn = periodic(r, dims[1]);
                            for (int c = -cOff; c < extDims[0] - cOff; c++) {
                                int cOut = c + cOff;
                                int cIn = periodic(c, dims[0]);
                                int idx = (sIn * dims[1] * dims[0] + rIn * dims[0] + cIn) * vlen;
                                int idxExt = (sOut * extDims[1] * extDims[0] + rOut * extDims[0] + cOut) * vlen;
                                for (int n = 0; n < vlen; n++) {
                                    extData[idxExt + n] = data[idx + n];
                                }
                            }
                        }
                    }
                } else if (method == PaddingType.REFLECTED) {
                    int sOff = margins[2][0];
                    int rOff = margins[1][0];
                    int cOff = margins[0][0];
                    for (int s = -sOff; s < extDims[2] - sOff; s++) {
                        int sOut = s + sOff;
                        int sIn = reflected(s, dims[2]);
                        for (int r = -rOff; r < extDims[1] - rOff; r++) {
                            int rOut = r + rOff;
                            int rIn = reflected(r, dims[1]);
                            for (int c = -cOff; c < extDims[0] - cOff; c++) {
                                int cOut = c + cOff;
                                int cIn = reflected(c, dims[0]);
                                int idx = (sIn * dims[1] * dims[0] + rIn * dims[0] + cIn) * vlen;
                                int idxExt = (sOut * extDims[1] * extDims[0] + rOut * extDims[0] + cOut) * vlen;
                                for (int n = 0; n < vlen; n++) {
                                    extData[idxExt + n] = data[idx + n];
                                }
                            }
                        }
                    }
                }
                break;
            case 2:
                if (method == PaddingType.ZERO) {
                    for (int j = 0, m = 0; j < dims[1]; j++) {
                        for (int k = 0, l = (margins[1][0] + j) * extDims[0] + margins[0][0]; k < dims[0]; k++, l++, m++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l * vlen + n] = data[m * vlen + n];
                            }
                        }
                    }
                } else if (method == PaddingType.FIXED) {
                    for (int j = 0, m = 0; j < dims[1]; j++) {
                        for (int k = 0, l = (margins[1][0] + j) * extDims[0] + margins[0][0]; k < dims[0]; k++, l++, m++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l * vlen + n] = data[m * vlen + n];
                            }
                        }
                    }
                    int l = 0;
                    for (int j = 0; j < dims[1]; j++) {
                        l = ((margins[1][0] + j) * extDims[0] + margins[0][0]) * vlen;
                        for (int k = 1; k <= margins[0][0]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l - k * vlen + n] = extData[l + n];
                            }
                        }
                        l = ((margins[1][0] + j) * extDims[0] + margins[0][0] + dims[0] - 1) * vlen;
                        for (int k = 1; k <= margins[0][1]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l + k * vlen + n] = extData[l + n];
                            }
                        }
                    }
                    int m = extDims[0];
                    for (int j = 0; j < extDims[0]; j++) {
                        l = ((margins[1][0]) * extDims[0] + j) * vlen;
                        for (int k = 1; k <= margins[1][0]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l - k * m * vlen + n] = extData[l + n];
                            }
                        }
                        l = ((margins[1][0] + dims[1] - 1) * extDims[0] + j) * vlen;
                        for (int k = 1; k <= margins[1][1]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l + k * m * vlen + n] = extData[l + n];
                            }
                        }
                    }
                } else if (method == PaddingType.PERIODIC) {
                    int rOff = margins[1][0];
                    int cOff = margins[0][0];
                    for (int r = -rOff; r < extDims[1] - rOff; r++) {
                        int rOut = r + rOff;
                        int rIn = periodic(r, dims[1]);
                        for (int c = -cOff; c < extDims[0] - cOff; c++) {
                            int cOut = c + cOff;
                            int cIn = periodic(c, dims[0]);
                            int idx = (rIn * dims[0] + cIn) * vlen;
                            int idxExt = (rOut * extDims[0] + cOut) * vlen;
                            for (int n = 0; n < vlen; n++) {
                                extData[idxExt + n] = data[idx + n];
                            }
                        }
                    }

                } else if (method == PaddingType.REFLECTED) {
                    int rOff = margins[1][0];
                    int cOff = margins[0][0];
                    for (int r = -rOff; r < extDims[1] - rOff; r++) {
                        int rOut = r + rOff;
                        int rIn = reflected(r, dims[1]);
                        for (int c = -cOff; c < extDims[0] - cOff; c++) {
                            int cOut = c + cOff;
                            int cIn = reflected(c, dims[0]);
                            int idx = (rIn * dims[0] + cIn) * vlen;
                            int idxExt = (rOut * extDims[0] + cOut) * vlen;
                            for (int n = 0; n < vlen; n++) {
                                extData[idxExt + n] = data[idx + n];
                            }
                        }
                    }

                }
                break;
            case 1:
                if (method == PaddingType.ZERO) {
                    for (int k = 0, m = 0, l = margins[0][0]; k < dims[0]; k++, l++, m++) {
                        for (int n = 0; n < vlen; n++) {
                            extData[l * vlen + n] = data[m * vlen + n];
                        }
                    }
                } else if (method == PaddingType.FIXED) {
                    for (int k = 0, m = 0, l = margins[0][0]; k < dims[0]; k++, l++, m++) {
                        for (int n = 0; n < vlen; n++) {
                            extData[l * vlen + n] = data[m * vlen + n];
                        }
                    }
                    int l = (margins[0][0]) * vlen;
                    for (int k = 1; k <= margins[0][0]; k++) {
                        for (int n = 0; n < vlen; n++) {
                            extData[l - k * vlen + n] = extData[l + n];
                        }
                    }
                    l = (margins[0][0] + dims[0] - 1) * vlen;
                    for (int k = 1; k <= margins[0][1]; k++) {
                        for (int n = 0; n < vlen; n++) {
                            extData[l + k * vlen + n] = extData[l + n];
                        }
                    }
                } else if (method == PaddingType.PERIODIC) {
                    int cOff = margins[0][0];
                    for (int c = -cOff; c < extDims[0] - cOff; c++) {
                        int cOut = c + cOff;
                        int cIn = periodic(c, dims[0]);
                        int idx = cIn * vlen;
                        int idxExt = cOut * vlen;
                        for (int n = 0; n < vlen; n++) {
                            extData[idxExt + n] = data[idx + n];
                        }
                    }
                } else if (method == PaddingType.REFLECTED) {
                    int cOff = margins[0][0];
                    for (int c = -cOff; c < extDims[0] - cOff; c++) {
                        int cOut = c + cOff;
                        int cIn = reflected(c, dims[0]);
                        int idx = cIn * vlen;
                        int idxExt = cOut * vlen;
                        for (int n = 0; n < vlen; n++) {
                            extData[idxExt + n] = data[idx + n];
                        }
                    }
                }
                break;
        }
        return extData;
    }

    /*public static byte[] extendMargins(int[] dims, int vlen, int[] extDims, int[][] margins, byte[] data, int[][] method) {
        int nExt = 1;
        for (int i = 0; i < dims.length; i++) {
            extDims[i] = dims[i] + margins[i][0] + margins[i][1];
            nExt *= extDims[i];
        }
        byte[] extData = new byte[vlen * nExt];
        for (int i = 0; i < extData.length; i++) {
            extData[i] = 0;
        }
        switch (dims.length) {
            case 3: {
                for (int i = 0, m = 0; i < dims[2]; i++) {
                    for (int j = 0; j < dims[1]; j++) {
                        for (int k = 0, l = ((margins[2][0] + i) * extDims[1] + margins[1][0] + j) * extDims[0] + margins[0][0];
                                k < dims[0]; k++, l++, m++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l * vlen + n] = data[m * vlen + n];
                            }
                        }
                    }
                }
                int l = 0;
                for (int i = 0; i < dims[2]; i++) {
                    for (int j = 0; j < dims[1]; j++) {
                        l = (((margins[2][0] + i) * extDims[1] + margins[1][0] + j) * extDims[0] + margins[0][0]) * vlen;
                        if (method[0][0] >= PaddingType.FIXED) {
                            for (int k = 1; k <= margins[0][0]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l - k * vlen + n] = extData[l + n];
                                }
                            }
                        } else {
                            for (int k = 1; k <= margins[0][0]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l - k * vlen + n] = 0;
                                }
                            }
                        }
                        l = (((margins[2][0] + i) * extDims[1] + margins[1][0] + j) * extDims[0] + margins[0][0] + dims[0] - 1) * vlen;
                        if (method[0][1] >= PaddingType.FIXED) {
                            for (int k = 1; k <= margins[0][1]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l + k * vlen + n] = extData[l + n];
                                }
                            }
                        } else {
                            for (int k = 1; k <= margins[0][1]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l + k * vlen + n] = 0;
                                }
                            }
                        }
                    }
                }
                int m = extDims[0];
                for (int i = 0; i < dims[2]; i++) {
                    for (int j = 0; j < extDims[0]; j++) {
                        l = (((margins[2][0] + i) * extDims[1] + margins[1][0]) * extDims[0] + j) * vlen;
                        if (method[1][0] >= PaddingType.FIXED) {
                            for (int k = 1; k <= margins[1][0]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l - k * m * vlen + n] = extData[l + n];
                                }
                            }
                        } else {
                            for (int k = 1; k <= margins[1][0]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l - k * m * vlen + n] = 0;
                                }
                            }
                        }
                        l = (((margins[2][0] + i) * extDims[1] + margins[1][0] + dims[1] - 1) * extDims[0] + j) * vlen;
                        if (method[1][1] >= PaddingType.FIXED) {
                            for (int k = 1; k <= margins[1][1]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l + k * m * vlen + n] = extData[l + n];
                                }
                            }
                        } else {
                            for (int k = 1; k <= margins[1][1]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l + k * m * vlen + n] = 0;
                                }
                            }
                        }
                    }
                }
                m = extDims[1] * extDims[0];
                for (int i = 0; i < extDims[1]; i++) {
                    for (int j = 0; j < extDims[0]; j++) {
                        l = ((margins[2][0] * extDims[1] + i) * extDims[0] + j) * vlen;
                        if (method[2][0] >= PaddingType.FIXED) {
                            for (int k = 1; k <= margins[2][0]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l - k * m * vlen + n] = extData[l + n];
                                }
                            }
                        } else {
                            for (int k = 1; k <= margins[2][0]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l - k * m * vlen + n] = 0;
                                }
                            }
                        }
                        l = (((margins[2][0] + dims[2] - 1) * extDims[1] + i) * extDims[0] + j) * vlen;
                        if (method[2][1] >= PaddingType.FIXED) {
                            for (int k = 1; k <= margins[2][1]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l + k * m * vlen + n] = extData[l + n];
                                }
                            }
                        } else {
                            for (int k = 1; k <= margins[2][1]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l + k * m * vlen + n] = 0;
                                }
                            }
                        }
                    }
                }
            }
            break;
            case 2:
                for (int j = 0, m = 0; j < dims[1]; j++) {
                    for (int k = 0, l = (margins[1][0] + j) * extDims[0] + margins[0][0];
                            k < dims[0]; k++, l++, m++) {
                        for (int n = 0; n < vlen; n++) {
                            extData[l * vlen + n] = data[m * vlen + n];
                        }
                    }
                } {
                int l = 0;
                for (int j = 0; j < dims[1]; j++) {
                    l = ((margins[1][0] + j) * extDims[0] + margins[0][0]) * vlen;
                    if (method[0][0] >= PaddingType.FIXED) {
                        for (int k = 1; k <= margins[0][0]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l - k * vlen + n] = extData[l + n];
                            }
                        }
                    } else {
                        for (int k = 1; k <= margins[0][0]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l - k * vlen + n] = 0;
                            }
                        }
                    }
                    l = ((margins[1][0] + j) * extDims[0] + margins[0][0] + dims[0] - 1) * vlen;
                    if (method[0][1] >= PaddingType.FIXED) {
                        for (int k = 1; k <= margins[0][1]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l + k * vlen + n] = extData[l + n];
                            }
                        }
                    } else {
                        for (int k = 1; k <= margins[0][1]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l + k * vlen + n] = 0;
                            }
                        }
                    }
                }
                int m = extDims[0];
                for (int j = 0; j < extDims[0]; j++) {
                    l = ((margins[1][0]) * extDims[0] + j) * vlen;
                    if (method[1][0] >= PaddingType.FIXED) {
                        for (int k = 1; k <= margins[1][0]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l - k * m * vlen + n] = extData[l + n];
                            }
                        }
                    } else {
                        for (int k = 1; k <= margins[1][0]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l - k * m * vlen + n] = 0;
                            }
                        }
                    }
                    l = ((margins[1][0] + dims[1] - 1) * extDims[0] + j) * vlen;
                    if (method[1][1] >= PaddingType.FIXED) {
                        for (int k = 1; k <= margins[1][1]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l + k * m * vlen + n] = extData[l + n];
                            }
                        }
                    } else {
                        for (int k = 1; k <= margins[1][1]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l + k * m * vlen + n] = 0;
                            }
                        }
                    }
                }
            }
            break;
            case 1:
                for (int k = 0, m = 0, l = margins[0][0];
                        k < dims[0]; k++, l++, m++) {
                    for (int n = 0; n < vlen; n++) {
                        extData[l * vlen + n] = data[m * vlen + n];
                    }
                } {
                int l = (margins[0][0]) * vlen;
                if (method[0][0] >= PaddingType.FIXED) {
                    for (int k = 1; k <= margins[0][0]; k++) {
                        for (int n = 0; n < vlen; n++) {
                            extData[l - k * vlen + n] = extData[l + n];
                        }
                    }
                } else {
                    for (int k = 1; k <= margins[0][0]; k++) {
                        for (int n = 0; n < vlen; n++) {
                            extData[l - k * vlen + n] = 0;
                        }
                    }
                }
                l = (margins[0][0] + dims[0] - 1) * vlen;
                if (method[0][1] >= PaddingType.FIXED) {
                    for (int k = 1; k <= margins[0][1]; k++) {
                        for (int n = 0; n < vlen; n++) {
                            extData[l + k * vlen + n] = extData[l + n];
                        }
                    }
                } else {
                    for (int k = 1; k <= margins[0][1]; k++) {
                        for (int n = 0; n < vlen; n++) {
                            extData[l + k * vlen + n] = 0;
                        }
                    }
                }
            }
            break;
        }
        return extData;
    }*/

    public static short[] extendMargins(int[] dims, int vlen, int[] extDims, int[][] margins, short[] data, PaddingType method) {
        int nExt = 1;
        for (int i = 0; i < dims.length; i++) {
            extDims[i] = dims[i] + margins[i][0] + margins[i][1];
            nExt *= extDims[i];
        }
        short[] extData = new short[vlen * nExt];
        for (int i = 0; i < extData.length; i++) {
            extData[i] = 0;
        }
        switch (dims.length) {
            case 3:
                if (method == PaddingType.ZERO) {
                    for (int i = 0, m = 0; i < dims[2]; i++) {
                        for (int j = 0; j < dims[1]; j++) {
                            for (int k = 0, l = ((margins[2][0] + i) * extDims[1] + margins[1][0] + j) * extDims[0] + margins[0][0]; k < dims[0]; k++, l++, m++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l * vlen + n] = data[m * vlen + n];
                                }
                            }
                        }
                    }
                } else if (method == PaddingType.FIXED) {
                    for (int i = 0, m = 0; i < dims[2]; i++) {
                        for (int j = 0; j < dims[1]; j++) {
                            for (int k = 0, l = ((margins[2][0] + i) * extDims[1] + margins[1][0] + j) * extDims[0] + margins[0][0]; k < dims[0]; k++, l++, m++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l * vlen + n] = data[m * vlen + n];
                                }
                            }
                        }
                    }
                    int l = 0;
                    for (int i = 0; i < dims[2]; i++) {
                        for (int j = 0; j < dims[1]; j++) {
                            l = (((margins[2][0] + i) * extDims[1] + margins[1][0] + j) * extDims[0] + margins[0][0]) * vlen;
                            for (int k = 1; k <= margins[0][0]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l - k * vlen + n] = extData[l + n];
                                }
                            }
                            l = (((margins[2][0] + i) * extDims[1] + margins[1][0] + j) * extDims[0] + margins[0][0] + dims[0] - 1) * vlen;
                            for (int k = 1; k <= margins[0][1]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l + k * vlen + n] = extData[l + n];
                                }
                            }
                        }
                    }
                    int m = extDims[0];
                    for (int i = 0; i < dims[2]; i++) {
                        for (int j = 0; j < extDims[0]; j++) {
                            l = (((margins[2][0] + i) * extDims[1] + margins[1][0]) * extDims[0] + j) * vlen;
                            for (int k = 1; k <= margins[1][0]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l - k * m * vlen + n] = extData[l + n];
                                }
                            }
                            l = (((margins[2][0] + i) * extDims[1] + margins[1][0] + dims[1] - 1) * extDims[0] + j) * vlen;
                            for (int k = 1; k <= margins[1][1]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l + k * m * vlen + n] = extData[l + n];
                                }
                            }
                        }
                    }
                    m = extDims[1] * extDims[0];
                    for (int i = 0; i < extDims[1]; i++) {
                        for (int j = 0; j < extDims[0]; j++) {
                            l = ((margins[2][0] * extDims[1] + i) * extDims[0] + j) * vlen;
                            for (int k = 1; k <= margins[2][0]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l - k * m * vlen + n] = extData[l + n];
                                }
                            }
                            l = (((margins[2][0] + dims[2] - 1) * extDims[1] + i) * extDims[0] + j) * vlen;
                            for (int k = 1; k <= margins[2][1]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l + k * m * vlen + n] = extData[l + n];
                                }
                            }
                        }
                    }
                } else if (method == PaddingType.PERIODIC) {
                    int sOff = margins[2][0];
                    int rOff = margins[1][0];
                    int cOff = margins[0][0];
                    for (int s = -sOff; s < extDims[2] - sOff; s++) {
                        int sOut = s + sOff;
                        int sIn = periodic(s, dims[2]);
                        for (int r = -rOff; r < extDims[1] - rOff; r++) {
                            int rOut = r + rOff;
                            int rIn = periodic(r, dims[1]);
                            for (int c = -cOff; c < extDims[0] - cOff; c++) {
                                int cOut = c + cOff;
                                int cIn = periodic(c, dims[0]);
                                int idx = (sIn * dims[1] * dims[0] + rIn * dims[0] + cIn) * vlen;
                                int idxExt = (sOut * extDims[1] * extDims[0] + rOut * extDims[0] + cOut) * vlen;
                                for (int n = 0; n < vlen; n++) {
                                    extData[idxExt + n] = data[idx + n];
                                }
                            }
                        }
                    }
                } else if (method == PaddingType.REFLECTED) {
                    int sOff = margins[2][0];
                    int rOff = margins[1][0];
                    int cOff = margins[0][0];
                    for (int s = -sOff; s < extDims[2] - sOff; s++) {
                        int sOut = s + sOff;
                        int sIn = reflected(s, dims[2]);
                        for (int r = -rOff; r < extDims[1] - rOff; r++) {
                            int rOut = r + rOff;
                            int rIn = reflected(r, dims[1]);
                            for (int c = -cOff; c < extDims[0] - cOff; c++) {
                                int cOut = c + cOff;
                                int cIn = reflected(c, dims[0]);
                                int idx = (sIn * dims[1] * dims[0] + rIn * dims[0] + cIn) * vlen;
                                int idxExt = (sOut * extDims[1] * extDims[0] + rOut * extDims[0] + cOut) * vlen;
                                for (int n = 0; n < vlen; n++) {
                                    extData[idxExt + n] = data[idx + n];
                                }
                            }
                        }
                    }
                }
                break;
            case 2:
                if (method == PaddingType.ZERO) {
                    for (int j = 0, m = 0; j < dims[1]; j++) {
                        for (int k = 0, l = (margins[1][0] + j) * extDims[0] + margins[0][0]; k < dims[0]; k++, l++, m++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l * vlen + n] = data[m * vlen + n];
                            }
                        }
                    }
                } else if (method == PaddingType.FIXED) {
                    for (int j = 0, m = 0; j < dims[1]; j++) {
                        for (int k = 0, l = (margins[1][0] + j) * extDims[0] + margins[0][0]; k < dims[0]; k++, l++, m++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l * vlen + n] = data[m * vlen + n];
                            }
                        }
                    }
                    int l = 0;
                    for (int j = 0; j < dims[1]; j++) {
                        l = ((margins[1][0] + j) * extDims[0] + margins[0][0]) * vlen;
                        for (int k = 1; k <= margins[0][0]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l - k * vlen + n] = extData[l + n];
                            }
                        }
                        l = ((margins[1][0] + j) * extDims[0] + margins[0][0] + dims[0] - 1) * vlen;
                        for (int k = 1; k <= margins[0][1]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l + k * vlen + n] = extData[l + n];
                            }
                        }
                    }
                    int m = extDims[0];
                    for (int j = 0; j < extDims[0]; j++) {
                        l = ((margins[1][0]) * extDims[0] + j) * vlen;
                        for (int k = 1; k <= margins[1][0]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l - k * m * vlen + n] = extData[l + n];
                            }
                        }
                        l = ((margins[1][0] + dims[1] - 1) * extDims[0] + j) * vlen;
                        for (int k = 1; k <= margins[1][1]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l + k * m * vlen + n] = extData[l + n];
                            }
                        }
                    }
                } else if (method == PaddingType.PERIODIC) {
                    int rOff = margins[1][0];
                    int cOff = margins[0][0];
                    for (int r = -rOff; r < extDims[1] - rOff; r++) {
                        int rOut = r + rOff;
                        int rIn = periodic(r, dims[1]);
                        for (int c = -cOff; c < extDims[0] - cOff; c++) {
                            int cOut = c + cOff;
                            int cIn = periodic(c, dims[0]);
                            int idx = (rIn * dims[0] + cIn) * vlen;
                            int idxExt = (rOut * extDims[0] + cOut) * vlen;
                            for (int n = 0; n < vlen; n++) {
                                extData[idxExt + n] = data[idx + n];
                            }
                        }
                    }

                } else if (method == PaddingType.REFLECTED) {
                    int rOff = margins[1][0];
                    int cOff = margins[0][0];
                    for (int r = -rOff; r < extDims[1] - rOff; r++) {
                        int rOut = r + rOff;
                        int rIn = reflected(r, dims[1]);
                        for (int c = -cOff; c < extDims[0] - cOff; c++) {
                            int cOut = c + cOff;
                            int cIn = reflected(c, dims[0]);
                            int idx = (rIn * dims[0] + cIn) * vlen;
                            int idxExt = (rOut * extDims[0] + cOut) * vlen;
                            for (int n = 0; n < vlen; n++) {
                                extData[idxExt + n] = data[idx + n];
                            }
                        }
                    }

                }
                break;
            case 1:
                if (method == PaddingType.ZERO) {
                    for (int k = 0, m = 0, l = margins[0][0]; k < dims[0]; k++, l++, m++) {
                        for (int n = 0; n < vlen; n++) {
                            extData[l * vlen + n] = data[m * vlen + n];
                        }
                    }
                } else if (method == PaddingType.FIXED) {
                    for (int k = 0, m = 0, l = margins[0][0]; k < dims[0]; k++, l++, m++) {
                        for (int n = 0; n < vlen; n++) {
                            extData[l * vlen + n] = data[m * vlen + n];
                        }
                    }
                    int l = (margins[0][0]) * vlen;
                    for (int k = 1; k <= margins[0][0]; k++) {
                        for (int n = 0; n < vlen; n++) {
                            extData[l - k * vlen + n] = extData[l + n];
                        }
                    }
                    l = (margins[0][0] + dims[0] - 1) * vlen;
                    for (int k = 1; k <= margins[0][1]; k++) {
                        for (int n = 0; n < vlen; n++) {
                            extData[l + k * vlen + n] = extData[l + n];
                        }
                    }
                } else if (method == PaddingType.PERIODIC) {
                    int cOff = margins[0][0];
                    for (int c = -cOff; c < extDims[0] - cOff; c++) {
                        int cOut = c + cOff;
                        int cIn = periodic(c, dims[0]);
                        int idx = cIn * vlen;
                        int idxExt = cOut * vlen;
                        for (int n = 0; n < vlen; n++) {
                            extData[idxExt + n] = data[idx + n];
                        }
                    }
                } else if (method == PaddingType.REFLECTED) {
                    int cOff = margins[0][0];
                    for (int c = -cOff; c < extDims[0] - cOff; c++) {
                        int cOut = c + cOff;
                        int cIn = reflected(c, dims[0]);
                        int idx = cIn * vlen;
                        int idxExt = cOut * vlen;
                        for (int n = 0; n < vlen; n++) {
                            extData[idxExt + n] = data[idx + n];
                        }
                    }
                }
                break;
        }
        return extData;
    }

    /*public static short[] extendMargins(int[] dims, int vlen, int[] extDims, int[][] margins, short[] data, int[][] method) {
        int nExt = 1;
        for (int i = 0; i < dims.length; i++) {
            extDims[i] = dims[i] + margins[i][0] + margins[i][1];
            nExt *= extDims[i];
        }
        short[] extData = new short[vlen * nExt];
        for (int i = 0; i < extData.length; i++) {
            extData[i] = 0;
        }
        switch (dims.length) {
            case 3: {
                for (int i = 0, m = 0; i < dims[2]; i++) {
                    for (int j = 0; j < dims[1]; j++) {
                        for (int k = 0, l = ((margins[2][0] + i) * extDims[1] + margins[1][0] + j) * extDims[0] + margins[0][0];
                                k < dims[0]; k++, l++, m++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l * vlen + n] = data[m * vlen + n];
                            }
                        }
                    }
                }
                int l = 0;
                for (int i = 0; i < dims[2]; i++) {
                    for (int j = 0; j < dims[1]; j++) {
                        l = (((margins[2][0] + i) * extDims[1] + margins[1][0] + j) * extDims[0] + margins[0][0]) * vlen;
                        if (method[0][0] >= PaddingType.FIXED) {
                            for (int k = 1; k <= margins[0][0]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l - k * vlen + n] = extData[l + n];
                                }
                            }
                        } else {
                            for (int k = 1; k <= margins[0][0]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l - k * vlen + n] = 0;
                                }
                            }
                        }
                        l = (((margins[2][0] + i) * extDims[1] + margins[1][0] + j) * extDims[0] + margins[0][0] + dims[0] - 1) * vlen;
                        if (method[0][1] >= PaddingType.FIXED) {
                            for (int k = 1; k <= margins[0][1]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l + k * vlen + n] = extData[l + n];
                                }
                            }
                        } else {
                            for (int k = 1; k <= margins[0][1]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l + k * vlen + n] = 0;
                                }
                            }
                        }
                    }
                }
                int m = extDims[0];
                for (int i = 0; i < dims[2]; i++) {
                    for (int j = 0; j < extDims[0]; j++) {
                        l = (((margins[2][0] + i) * extDims[1] + margins[1][0]) * extDims[0] + j) * vlen;
                        if (method[1][0] >= PaddingType.FIXED) {
                            for (int k = 1; k <= margins[1][0]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l - k * m * vlen + n] = extData[l + n];
                                }
                            }
                        } else {
                            for (int k = 1; k <= margins[1][0]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l - k * m * vlen + n] = 0;
                                }
                            }
                        }
                        l = (((margins[2][0] + i) * extDims[1] + margins[1][0] + dims[1] - 1) * extDims[0] + j) * vlen;
                        if (method[1][1] >= PaddingType.FIXED) {
                            for (int k = 1; k <= margins[1][1]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l + k * m * vlen + n] = extData[l + n];
                                }
                            }
                        } else {
                            for (int k = 1; k <= margins[1][1]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l + k * m * vlen + n] = 0;
                                }
                            }
                        }
                    }
                }
                m = extDims[1] * extDims[0];
                for (int i = 0; i < extDims[1]; i++) {
                    for (int j = 0; j < extDims[0]; j++) {
                        l = ((margins[2][0] * extDims[1] + i) * extDims[0] + j) * vlen;
                        if (method[2][0] >= PaddingType.FIXED) {
                            for (int k = 1; k <= margins[2][0]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l - k * m * vlen + n] = extData[l + n];
                                }
                            }
                        } else {
                            for (int k = 1; k <= margins[2][0]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l - k * m * vlen + n] = 0;
                                }
                            }
                        }
                        l = (((margins[2][0] + dims[2] - 1) * extDims[1] + i) * extDims[0] + j) * vlen;
                        if (method[2][1] >= PaddingType.FIXED) {
                            for (int k = 1; k <= margins[2][1]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l + k * m * vlen + n] = extData[l + n];
                                }
                            }
                        } else {
                            for (int k = 1; k <= margins[2][1]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l + k * m * vlen + n] = 0;
                                }
                            }
                        }
                    }
                }
            }
            break;
            case 2:
                for (int j = 0, m = 0; j < dims[1]; j++) {
                    for (int k = 0, l = (margins[1][0] + j) * extDims[0] + margins[0][0];
                            k < dims[0]; k++, l++, m++) {
                        for (int n = 0; n < vlen; n++) {
                            extData[l * vlen + n] = data[m * vlen + n];
                        }
                    }
                } {
                int l = 0;
                for (int j = 0; j < dims[1]; j++) {
                    l = ((margins[1][0] + j) * extDims[0] + margins[0][0]) * vlen;
                    if (method[0][0] >= PaddingType.FIXED) {
                        for (int k = 1; k <= margins[0][0]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l - k * vlen + n] = extData[l + n];
                            }
                        }
                    } else {
                        for (int k = 1; k <= margins[0][0]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l - k * vlen + n] = 0;
                            }
                        }
                    }
                    l = ((margins[1][0] + j) * extDims[0] + margins[0][0] + dims[0] - 1) * vlen;
                    if (method[0][1] >= PaddingType.FIXED) {
                        for (int k = 1; k <= margins[0][1]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l + k * vlen + n] = extData[l + n];
                            }
                        }
                    } else {
                        for (int k = 1; k <= margins[0][1]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l + k * vlen + n] = 0;
                            }
                        }
                    }
                }
                int m = extDims[0];
                for (int j = 0; j < extDims[0]; j++) {
                    l = ((margins[1][0]) * extDims[0] + j) * vlen;
                    if (method[1][0] >= PaddingType.FIXED) {
                        for (int k = 1; k <= margins[1][0]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l - k * m * vlen + n] = extData[l + n];
                            }
                        }
                    } else {
                        for (int k = 1; k <= margins[1][0]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l - k * m * vlen + n] = 0;
                            }
                        }
                    }
                    l = ((margins[1][0] + dims[1] - 1) * extDims[0] + j) * vlen;
                    if (method[1][1] >= PaddingType.FIXED) {
                        for (int k = 1; k <= margins[1][1]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l + k * m * vlen + n] = extData[l + n];
                            }
                        }
                    } else {
                        for (int k = 1; k <= margins[1][1]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l + k * m * vlen + n] = 0;
                            }
                        }
                    }
                }
            }
            break;
            case 1:
                for (int k = 0, m = 0, l = margins[0][0];
                        k < dims[0]; k++, l++, m++) {
                    for (int n = 0; n < vlen; n++) {
                        extData[l * vlen + n] = data[m * vlen + n];
                    }
                } {
                int l = (margins[0][0]) * vlen;
                if (method[0][0] >= PaddingType.FIXED) {
                    for (int k = 1; k <= margins[0][0]; k++) {
                        for (int n = 0; n < vlen; n++) {
                            extData[l - k * vlen + n] = extData[l + n];
                        }
                    }
                } else {
                    for (int k = 1; k <= margins[0][0]; k++) {
                        for (int n = 0; n < vlen; n++) {
                            extData[l - k * vlen + n] = 0;
                        }
                    }
                }
                l = (margins[0][0] + dims[0] - 1) * vlen;
                if (method[0][1] >= PaddingType.FIXED) {
                    for (int k = 1; k <= margins[0][1]; k++) {
                        for (int n = 0; n < vlen; n++) {
                            extData[l + k * vlen + n] = extData[l + n];
                        }
                    }
                } else {
                    for (int k = 1; k <= margins[0][1]; k++) {
                        for (int n = 0; n < vlen; n++) {
                            extData[l + k * vlen + n] = 0;
                        }
                    }
                }
            }
            break;
        }
        return extData;
    }*/

    public static int[] extendMargins(int[] dims, int vlen, int[] extDims, int[][] margins, int[] data, PaddingType method) {
        int nExt = 1;
        for (int i = 0; i < dims.length; i++) {
            extDims[i] = dims[i] + margins[i][0] + margins[i][1];
            nExt *= extDims[i];
        }
        int[] extData = new int[vlen * nExt];
        for (int i = 0; i < extData.length; i++) {
            extData[i] = 0;
        }
         switch (dims.length) {
            case 3:
                if (method == PaddingType.ZERO) {
                    for (int i = 0, m = 0; i < dims[2]; i++) {
                        for (int j = 0; j < dims[1]; j++) {
                            for (int k = 0, l = ((margins[2][0] + i) * extDims[1] + margins[1][0] + j) * extDims[0] + margins[0][0]; k < dims[0]; k++, l++, m++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l * vlen + n] = data[m * vlen + n];
                                }
                            }
                        }
                    }
                } else if (method == PaddingType.FIXED) {
                    for (int i = 0, m = 0; i < dims[2]; i++) {
                        for (int j = 0; j < dims[1]; j++) {
                            for (int k = 0, l = ((margins[2][0] + i) * extDims[1] + margins[1][0] + j) * extDims[0] + margins[0][0]; k < dims[0]; k++, l++, m++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l * vlen + n] = data[m * vlen + n];
                                }
                            }
                        }
                    }
                    int l = 0;
                    for (int i = 0; i < dims[2]; i++) {
                        for (int j = 0; j < dims[1]; j++) {
                            l = (((margins[2][0] + i) * extDims[1] + margins[1][0] + j) * extDims[0] + margins[0][0]) * vlen;
                            for (int k = 1; k <= margins[0][0]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l - k * vlen + n] = extData[l + n];
                                }
                            }
                            l = (((margins[2][0] + i) * extDims[1] + margins[1][0] + j) * extDims[0] + margins[0][0] + dims[0] - 1) * vlen;
                            for (int k = 1; k <= margins[0][1]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l + k * vlen + n] = extData[l + n];
                                }
                            }
                        }
                    }
                    int m = extDims[0];
                    for (int i = 0; i < dims[2]; i++) {
                        for (int j = 0; j < extDims[0]; j++) {
                            l = (((margins[2][0] + i) * extDims[1] + margins[1][0]) * extDims[0] + j) * vlen;
                            for (int k = 1; k <= margins[1][0]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l - k * m * vlen + n] = extData[l + n];
                                }
                            }
                            l = (((margins[2][0] + i) * extDims[1] + margins[1][0] + dims[1] - 1) * extDims[0] + j) * vlen;
                            for (int k = 1; k <= margins[1][1]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l + k * m * vlen + n] = extData[l + n];
                                }
                            }
                        }
                    }
                    m = extDims[1] * extDims[0];
                    for (int i = 0; i < extDims[1]; i++) {
                        for (int j = 0; j < extDims[0]; j++) {
                            l = ((margins[2][0] * extDims[1] + i) * extDims[0] + j) * vlen;
                            for (int k = 1; k <= margins[2][0]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l - k * m * vlen + n] = extData[l + n];
                                }
                            }
                            l = (((margins[2][0] + dims[2] - 1) * extDims[1] + i) * extDims[0] + j) * vlen;
                            for (int k = 1; k <= margins[2][1]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l + k * m * vlen + n] = extData[l + n];
                                }
                            }
                        }
                    }
                } else if (method == PaddingType.PERIODIC) {
                    int sOff = margins[2][0];
                    int rOff = margins[1][0];
                    int cOff = margins[0][0];
                    for (int s = -sOff; s < extDims[2] - sOff; s++) {
                        int sOut = s + sOff;
                        int sIn = periodic(s, dims[2]);
                        for (int r = -rOff; r < extDims[1] - rOff; r++) {
                            int rOut = r + rOff;
                            int rIn = periodic(r, dims[1]);
                            for (int c = -cOff; c < extDims[0] - cOff; c++) {
                                int cOut = c + cOff;
                                int cIn = periodic(c, dims[0]);
                                int idx = (sIn * dims[1] * dims[0] + rIn * dims[0] + cIn) * vlen;
                                int idxExt = (sOut * extDims[1] * extDims[0] + rOut * extDims[0] + cOut) * vlen;
                                for (int n = 0; n < vlen; n++) {
                                    extData[idxExt + n] = data[idx + n];
                                }
                            }
                        }
                    }
                } else if (method == PaddingType.REFLECTED) {
                    int sOff = margins[2][0];
                    int rOff = margins[1][0];
                    int cOff = margins[0][0];
                    for (int s = -sOff; s < extDims[2] - sOff; s++) {
                        int sOut = s + sOff;
                        int sIn = reflected(s, dims[2]);
                        for (int r = -rOff; r < extDims[1] - rOff; r++) {
                            int rOut = r + rOff;
                            int rIn = reflected(r, dims[1]);
                            for (int c = -cOff; c < extDims[0] - cOff; c++) {
                                int cOut = c + cOff;
                                int cIn = reflected(c, dims[0]);
                                int idx = (sIn * dims[1] * dims[0] + rIn * dims[0] + cIn) * vlen;
                                int idxExt = (sOut * extDims[1] * extDims[0] + rOut * extDims[0] + cOut) * vlen;
                                for (int n = 0; n < vlen; n++) {
                                    extData[idxExt + n] = data[idx + n];
                                }
                            }
                        }
                    }
                }
                break;
            case 2:
                if (method == PaddingType.ZERO) {
                    for (int j = 0, m = 0; j < dims[1]; j++) {
                        for (int k = 0, l = (margins[1][0] + j) * extDims[0] + margins[0][0]; k < dims[0]; k++, l++, m++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l * vlen + n] = data[m * vlen + n];
                            }
                        }
                    }
                } else if (method == PaddingType.FIXED) {
                    for (int j = 0, m = 0; j < dims[1]; j++) {
                        for (int k = 0, l = (margins[1][0] + j) * extDims[0] + margins[0][0]; k < dims[0]; k++, l++, m++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l * vlen + n] = data[m * vlen + n];
                            }
                        }
                    }
                    int l = 0;
                    for (int j = 0; j < dims[1]; j++) {
                        l = ((margins[1][0] + j) * extDims[0] + margins[0][0]) * vlen;
                        for (int k = 1; k <= margins[0][0]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l - k * vlen + n] = extData[l + n];
                            }
                        }
                        l = ((margins[1][0] + j) * extDims[0] + margins[0][0] + dims[0] - 1) * vlen;
                        for (int k = 1; k <= margins[0][1]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l + k * vlen + n] = extData[l + n];
                            }
                        }
                    }
                    int m = extDims[0];
                    for (int j = 0; j < extDims[0]; j++) {
                        l = ((margins[1][0]) * extDims[0] + j) * vlen;
                        for (int k = 1; k <= margins[1][0]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l - k * m * vlen + n] = extData[l + n];
                            }
                        }
                        l = ((margins[1][0] + dims[1] - 1) * extDims[0] + j) * vlen;
                        for (int k = 1; k <= margins[1][1]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l + k * m * vlen + n] = extData[l + n];
                            }
                        }
                    }
                } else if (method == PaddingType.PERIODIC) {
                    int rOff = margins[1][0];
                    int cOff = margins[0][0];
                    for (int r = -rOff; r < extDims[1] - rOff; r++) {
                        int rOut = r + rOff;
                        int rIn = periodic(r, dims[1]);
                        for (int c = -cOff; c < extDims[0] - cOff; c++) {
                            int cOut = c + cOff;
                            int cIn = periodic(c, dims[0]);
                            int idx = (rIn * dims[0] + cIn) * vlen;
                            int idxExt = (rOut * extDims[0] + cOut) * vlen;
                            for (int n = 0; n < vlen; n++) {
                                extData[idxExt + n] = data[idx + n];
                            }
                        }
                    }

                } else if (method == PaddingType.REFLECTED) {
                    int rOff = margins[1][0];
                    int cOff = margins[0][0];
                    for (int r = -rOff; r < extDims[1] - rOff; r++) {
                        int rOut = r + rOff;
                        int rIn = reflected(r, dims[1]);
                        for (int c = -cOff; c < extDims[0] - cOff; c++) {
                            int cOut = c + cOff;
                            int cIn = reflected(c, dims[0]);
                            int idx = (rIn * dims[0] + cIn) * vlen;
                            int idxExt = (rOut * extDims[0] + cOut) * vlen;
                            for (int n = 0; n < vlen; n++) {
                                extData[idxExt + n] = data[idx + n];
                            }
                        }
                    }

                }
                break;
            case 1:
                if (method == PaddingType.ZERO) {
                    for (int k = 0, m = 0, l = margins[0][0]; k < dims[0]; k++, l++, m++) {
                        for (int n = 0; n < vlen; n++) {
                            extData[l * vlen + n] = data[m * vlen + n];
                        }
                    }
                } else if (method == PaddingType.FIXED) {
                    for (int k = 0, m = 0, l = margins[0][0]; k < dims[0]; k++, l++, m++) {
                        for (int n = 0; n < vlen; n++) {
                            extData[l * vlen + n] = data[m * vlen + n];
                        }
                    }
                    int l = (margins[0][0]) * vlen;
                    for (int k = 1; k <= margins[0][0]; k++) {
                        for (int n = 0; n < vlen; n++) {
                            extData[l - k * vlen + n] = extData[l + n];
                        }
                    }
                    l = (margins[0][0] + dims[0] - 1) * vlen;
                    for (int k = 1; k <= margins[0][1]; k++) {
                        for (int n = 0; n < vlen; n++) {
                            extData[l + k * vlen + n] = extData[l + n];
                        }
                    }
                } else if (method == PaddingType.PERIODIC) {
                    int cOff = margins[0][0];
                    for (int c = -cOff; c < extDims[0] - cOff; c++) {
                        int cOut = c + cOff;
                        int cIn = periodic(c, dims[0]);
                        int idx = cIn * vlen;
                        int idxExt = cOut * vlen;
                        for (int n = 0; n < vlen; n++) {
                            extData[idxExt + n] = data[idx + n];
                        }
                    }
                } else if (method == PaddingType.REFLECTED) {
                    int cOff = margins[0][0];
                    for (int c = -cOff; c < extDims[0] - cOff; c++) {
                        int cOut = c + cOff;
                        int cIn = reflected(c, dims[0]);
                        int idx = cIn * vlen;
                        int idxExt = cOut * vlen;
                        for (int n = 0; n < vlen; n++) {
                            extData[idxExt + n] = data[idx + n];
                        }
                    }
                }
                break;
        }
        return extData;
    }

    /*public static int[] extendMargins(int[] dims, int vlen, int[] extDims, int[][] margins, int[] data, int[][] method) {
        int nExt = 1;
        for (int i = 0; i < dims.length; i++) {
            extDims[i] = dims[i] + margins[i][0] + margins[i][1];
            nExt *= extDims[i];
        }
        int[] extData = new int[vlen * nExt];
        for (int i = 0; i < extData.length; i++) {
            extData[i] = 0;
        }
        switch (dims.length) {
            case 3: {
                for (int i = 0, m = 0; i < dims[2]; i++) {
                    for (int j = 0; j < dims[1]; j++) {
                        for (int k = 0, l = ((margins[2][0] + i) * extDims[1] + margins[1][0] + j) * extDims[0] + margins[0][0];
                                k < dims[0]; k++, l++, m++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l * vlen + n] = data[m * vlen + n];
                            }
                        }
                    }
                }
                int l = 0;
                for (int i = 0; i < dims[2]; i++) {
                    for (int j = 0; j < dims[1]; j++) {
                        l = (((margins[2][0] + i) * extDims[1] + margins[1][0] + j) * extDims[0] + margins[0][0]) * vlen;
                        if (method[0][0] >= PaddingType.FIXED) {
                            for (int k = 1; k <= margins[0][0]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l - k * vlen + n] = extData[l + n];
                                }
                            }
                        } else {
                            for (int k = 1; k <= margins[0][0]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l - k * vlen + n] = 0;
                                }
                            }
                        }
                        l = (((margins[2][0] + i) * extDims[1] + margins[1][0] + j) * extDims[0] + margins[0][0] + dims[0] - 1) * vlen;
                        if (method[0][1] >= PaddingType.FIXED) {
                            for (int k = 1; k <= margins[0][1]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l + k * vlen + n] = extData[l + n];
                                }
                            }
                        } else {
                            for (int k = 1; k <= margins[0][1]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l + k * vlen + n] = 0;
                                }
                            }
                        }
                    }
                }
                int m = extDims[0];
                for (int i = 0; i < dims[2]; i++) {
                    for (int j = 0; j < extDims[0]; j++) {
                        l = (((margins[2][0] + i) * extDims[1] + margins[1][0]) * extDims[0] + j) * vlen;
                        if (method[1][0] >= PaddingType.FIXED) {
                            for (int k = 1; k <= margins[1][0]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l - k * m * vlen + n] = extData[l + n];
                                }
                            }
                        } else {
                            for (int k = 1; k <= margins[1][0]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l - k * m * vlen + n] = 0;
                                }
                            }
                        }
                        l = (((margins[2][0] + i) * extDims[1] + margins[1][0] + dims[1] - 1) * extDims[0] + j) * vlen;
                        if (method[1][1] >= PaddingType.FIXED) {
                            for (int k = 1; k <= margins[1][1]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l + k * m * vlen + n] = extData[l + n];
                                }
                            }
                        } else {
                            for (int k = 1; k <= margins[1][1]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l + k * m * vlen + n] = 0;
                                }
                            }
                        }
                    }
                }
                m = extDims[1] * extDims[0];
                for (int i = 0; i < extDims[1]; i++) {
                    for (int j = 0; j < extDims[0]; j++) {
                        l = ((margins[2][0] * extDims[1] + i) * extDims[0] + j) * vlen;
                        if (method[2][0] >= PaddingType.FIXED) {
                            for (int k = 1; k <= margins[2][0]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l - k * m * vlen + n] = extData[l + n];
                                }
                            }
                        } else {
                            for (int k = 1; k <= margins[2][0]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l - k * m * vlen + n] = 0;
                                }
                            }
                        }
                        l = (((margins[2][0] + dims[2] - 1) * extDims[1] + i) * extDims[0] + j) * vlen;
                        if (method[2][1] >= PaddingType.FIXED) {
                            for (int k = 1; k <= margins[2][1]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l + k * m * vlen + n] = extData[l + n];
                                }
                            }
                        } else {
                            for (int k = 1; k <= margins[2][1]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l + k * m * vlen + n] = 0;
                                }
                            }
                        }
                    }
                }
            }
            break;
            case 2:
                for (int j = 0, m = 0; j < dims[1]; j++) {
                    for (int k = 0, l = (margins[1][0] + j) * extDims[0] + margins[0][0];
                            k < dims[0]; k++, l++, m++) {
                        for (int n = 0; n < vlen; n++) {
                            extData[l * vlen + n] = data[m * vlen + n];
                        }
                    }
                } {
                int l = 0;
                for (int j = 0; j < dims[1]; j++) {
                    l = ((margins[1][0] + j) * extDims[0] + margins[0][0]) * vlen;
                    if (method[0][0] >= PaddingType.FIXED) {
                        for (int k = 1; k <= margins[0][0]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l - k * vlen + n] = extData[l + n];
                            }
                        }
                    } else {
                        for (int k = 1; k <= margins[0][0]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l - k * vlen + n] = 0;
                            }
                        }
                    }
                    l = ((margins[1][0] + j) * extDims[0] + margins[0][0] + dims[0] - 1) * vlen;
                    if (method[0][1] >= PaddingType.FIXED) {
                        for (int k = 1; k <= margins[0][1]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l + k * vlen + n] = extData[l + n];
                            }
                        }
                    } else {
                        for (int k = 1; k <= margins[0][1]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l + k * vlen + n] = 0;
                            }
                        }
                    }
                }
                int m = extDims[0];
                for (int j = 0; j < extDims[0]; j++) {
                    l = ((margins[1][0]) * extDims[0] + j) * vlen;
                    if (method[1][0] >= PaddingType.FIXED) {
                        for (int k = 1; k <= margins[1][0]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l - k * m * vlen + n] = extData[l + n];
                            }
                        }
                    } else {
                        for (int k = 1; k <= margins[1][0]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l - k * m * vlen + n] = 0;
                            }
                        }
                    }
                    l = ((margins[1][0] + dims[1] - 1) * extDims[0] + j) * vlen;
                    if (method[1][1] >= PaddingType.FIXED) {
                        for (int k = 1; k <= margins[1][1]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l + k * m * vlen + n] = extData[l + n];
                            }
                        }
                    } else {
                        for (int k = 1; k <= margins[1][1]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l + k * m * vlen + n] = 0;
                            }
                        }
                    }
                }
            }
            break;
            case 1:
                for (int k = 0, m = 0, l = margins[0][0];
                        k < dims[0]; k++, l++, m++) {
                    for (int n = 0; n < vlen; n++) {
                        extData[l * vlen + n] = data[m * vlen + n];
                    }
                } {
                int l = (margins[0][0]) * vlen;
                if (method[0][0] >= PaddingType.FIXED) {
                    for (int k = 1; k <= margins[0][0]; k++) {
                        for (int n = 0; n < vlen; n++) {
                            extData[l - k * vlen + n] = extData[l + n];
                        }
                    }
                } else {
                    for (int k = 1; k <= margins[0][0]; k++) {
                        for (int n = 0; n < vlen; n++) {
                            extData[l - k * vlen + n] = 0;
                        }
                    }
                }
                l = (margins[0][0] + dims[0] - 1) * vlen;
                if (method[0][1] >= PaddingType.FIXED) {
                    for (int k = 1; k <= margins[0][1]; k++) {
                        for (int n = 0; n < vlen; n++) {
                            extData[l + k * vlen + n] = extData[l + n];
                        }
                    }
                } else {
                    for (int k = 1; k <= margins[0][1]; k++) {
                        for (int n = 0; n < vlen; n++) {
                            extData[l + k * vlen + n] = 0;
                        }
                    }
                }
            }
            break;
        }
        return extData;
    }*/

    public static double[] extendMargins(int[] dims, int vlen, int[] extDims, int[][] margins, double[] data, PaddingType method) {
        int nExt = 1;
        for (int i = 0; i < dims.length; i++) {
            extDims[i] = dims[i] + margins[i][0] + margins[i][1];
            nExt *= extDims[i];
        }
        double[] extData = new double[vlen * nExt];
        for (int i = 0; i < extData.length; i++) {
            extData[i] = 0;
        }
        switch (dims.length) {
            case 3:
                if (method == PaddingType.ZERO) {
                    for (int i = 0, m = 0; i < dims[2]; i++) {
                        for (int j = 0; j < dims[1]; j++) {
                            for (int k = 0, l = ((margins[2][0] + i) * extDims[1] + margins[1][0] + j) * extDims[0] + margins[0][0]; k < dims[0]; k++, l++, m++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l * vlen + n] = data[m * vlen + n];
                                }
                            }
                        }
                    }
                } else if (method == PaddingType.FIXED) {
                    for (int i = 0, m = 0; i < dims[2]; i++) {
                        for (int j = 0; j < dims[1]; j++) {
                            for (int k = 0, l = ((margins[2][0] + i) * extDims[1] + margins[1][0] + j) * extDims[0] + margins[0][0]; k < dims[0]; k++, l++, m++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l * vlen + n] = data[m * vlen + n];
                                }
                            }
                        }
                    }
                    int l = 0;
                    for (int i = 0; i < dims[2]; i++) {
                        for (int j = 0; j < dims[1]; j++) {
                            l = (((margins[2][0] + i) * extDims[1] + margins[1][0] + j) * extDims[0] + margins[0][0]) * vlen;
                            for (int k = 1; k <= margins[0][0]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l - k * vlen + n] = extData[l + n];
                                }
                            }
                            l = (((margins[2][0] + i) * extDims[1] + margins[1][0] + j) * extDims[0] + margins[0][0] + dims[0] - 1) * vlen;
                            for (int k = 1; k <= margins[0][1]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l + k * vlen + n] = extData[l + n];
                                }
                            }
                        }
                    }
                    int m = extDims[0];
                    for (int i = 0; i < dims[2]; i++) {
                        for (int j = 0; j < extDims[0]; j++) {
                            l = (((margins[2][0] + i) * extDims[1] + margins[1][0]) * extDims[0] + j) * vlen;
                            for (int k = 1; k <= margins[1][0]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l - k * m * vlen + n] = extData[l + n];
                                }
                            }
                            l = (((margins[2][0] + i) * extDims[1] + margins[1][0] + dims[1] - 1) * extDims[0] + j) * vlen;
                            for (int k = 1; k <= margins[1][1]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l + k * m * vlen + n] = extData[l + n];
                                }
                            }
                        }
                    }
                    m = extDims[1] * extDims[0];
                    for (int i = 0; i < extDims[1]; i++) {
                        for (int j = 0; j < extDims[0]; j++) {
                            l = ((margins[2][0] * extDims[1] + i) * extDims[0] + j) * vlen;
                            for (int k = 1; k <= margins[2][0]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l - k * m * vlen + n] = extData[l + n];
                                }
                            }
                            l = (((margins[2][0] + dims[2] - 1) * extDims[1] + i) * extDims[0] + j) * vlen;
                            for (int k = 1; k <= margins[2][1]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l + k * m * vlen + n] = extData[l + n];
                                }
                            }
                        }
                    }
                } else if (method == PaddingType.PERIODIC) {
                    int sOff = margins[2][0];
                    int rOff = margins[1][0];
                    int cOff = margins[0][0];
                    for (int s = -sOff; s < extDims[2] - sOff; s++) {
                        int sOut = s + sOff;
                        int sIn = periodic(s, dims[2]);
                        for (int r = -rOff; r < extDims[1] - rOff; r++) {
                            int rOut = r + rOff;
                            int rIn = periodic(r, dims[1]);
                            for (int c = -cOff; c < extDims[0] - cOff; c++) {
                                int cOut = c + cOff;
                                int cIn = periodic(c, dims[0]);
                                int idx = (sIn * dims[1] * dims[0] + rIn * dims[0] + cIn) * vlen;
                                int idxExt = (sOut * extDims[1] * extDims[0] + rOut * extDims[0] + cOut) * vlen;
                                for (int n = 0; n < vlen; n++) {
                                    extData[idxExt + n] = data[idx + n];
                                }
                            }
                        }
                    }
                } else if (method == PaddingType.REFLECTED) {
                    int sOff = margins[2][0];
                    int rOff = margins[1][0];
                    int cOff = margins[0][0];
                    for (int s = -sOff; s < extDims[2] - sOff; s++) {
                        int sOut = s + sOff;
                        int sIn = reflected(s, dims[2]);
                        for (int r = -rOff; r < extDims[1] - rOff; r++) {
                            int rOut = r + rOff;
                            int rIn = reflected(r, dims[1]);
                            for (int c = -cOff; c < extDims[0] - cOff; c++) {
                                int cOut = c + cOff;
                                int cIn = reflected(c, dims[0]);
                                int idx = (sIn * dims[1] * dims[0] + rIn * dims[0] + cIn) * vlen;
                                int idxExt = (sOut * extDims[1] * extDims[0] + rOut * extDims[0] + cOut) * vlen;
                                for (int n = 0; n < vlen; n++) {
                                    extData[idxExt + n] = data[idx + n];
                                }
                            }
                        }
                    }
                }
                break;
            case 2:
                if (method == PaddingType.ZERO) {
                    for (int j = 0, m = 0; j < dims[1]; j++) {
                        for (int k = 0, l = (margins[1][0] + j) * extDims[0] + margins[0][0]; k < dims[0]; k++, l++, m++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l * vlen + n] = data[m * vlen + n];
                            }
                        }
                    }
                } else if (method == PaddingType.FIXED) {
                    for (int j = 0, m = 0; j < dims[1]; j++) {
                        for (int k = 0, l = (margins[1][0] + j) * extDims[0] + margins[0][0]; k < dims[0]; k++, l++, m++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l * vlen + n] = data[m * vlen + n];
                            }
                        }
                    }
                    int l = 0;
                    for (int j = 0; j < dims[1]; j++) {
                        l = ((margins[1][0] + j) * extDims[0] + margins[0][0]) * vlen;
                        for (int k = 1; k <= margins[0][0]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l - k * vlen + n] = extData[l + n];
                            }
                        }
                        l = ((margins[1][0] + j) * extDims[0] + margins[0][0] + dims[0] - 1) * vlen;
                        for (int k = 1; k <= margins[0][1]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l + k * vlen + n] = extData[l + n];
                            }
                        }
                    }
                    int m = extDims[0];
                    for (int j = 0; j < extDims[0]; j++) {
                        l = ((margins[1][0]) * extDims[0] + j) * vlen;
                        for (int k = 1; k <= margins[1][0]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l - k * m * vlen + n] = extData[l + n];
                            }
                        }
                        l = ((margins[1][0] + dims[1] - 1) * extDims[0] + j) * vlen;
                        for (int k = 1; k <= margins[1][1]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l + k * m * vlen + n] = extData[l + n];
                            }
                        }
                    }
                } else if (method == PaddingType.PERIODIC) {
                    int rOff = margins[1][0];
                    int cOff = margins[0][0];
                    for (int r = -rOff; r < extDims[1] - rOff; r++) {
                        int rOut = r + rOff;
                        int rIn = periodic(r, dims[1]);
                        for (int c = -cOff; c < extDims[0] - cOff; c++) {
                            int cOut = c + cOff;
                            int cIn = periodic(c, dims[0]);
                            int idx = (rIn * dims[0] + cIn) * vlen;
                            int idxExt = (rOut * extDims[0] + cOut) * vlen;
                            for (int n = 0; n < vlen; n++) {
                                extData[idxExt + n] = data[idx + n];
                            }
                        }
                    }

                } else if (method == PaddingType.REFLECTED) {
                    int rOff = margins[1][0];
                    int cOff = margins[0][0];
                    for (int r = -rOff; r < extDims[1] - rOff; r++) {
                        int rOut = r + rOff;
                        int rIn = reflected(r, dims[1]);
                        for (int c = -cOff; c < extDims[0] - cOff; c++) {
                            int cOut = c + cOff;
                            int cIn = reflected(c, dims[0]);
                            int idx = (rIn * dims[0] + cIn) * vlen;
                            int idxExt = (rOut * extDims[0] + cOut) * vlen;
                            for (int n = 0; n < vlen; n++) {
                                extData[idxExt + n] = data[idx + n];
                            }
                        }
                    }

                }
                break;
            case 1:
                if (method == PaddingType.ZERO) {
                    for (int k = 0, m = 0, l = margins[0][0]; k < dims[0]; k++, l++, m++) {
                        for (int n = 0; n < vlen; n++) {
                            extData[l * vlen + n] = data[m * vlen + n];
                        }
                    }
                } else if (method == PaddingType.FIXED) {
                    for (int k = 0, m = 0, l = margins[0][0]; k < dims[0]; k++, l++, m++) {
                        for (int n = 0; n < vlen; n++) {
                            extData[l * vlen + n] = data[m * vlen + n];
                        }
                    }
                    int l = (margins[0][0]) * vlen;
                    for (int k = 1; k <= margins[0][0]; k++) {
                        for (int n = 0; n < vlen; n++) {
                            extData[l - k * vlen + n] = extData[l + n];
                        }
                    }
                    l = (margins[0][0] + dims[0] - 1) * vlen;
                    for (int k = 1; k <= margins[0][1]; k++) {
                        for (int n = 0; n < vlen; n++) {
                            extData[l + k * vlen + n] = extData[l + n];
                        }
                    }
                } else if (method == PaddingType.PERIODIC) {
                    int cOff = margins[0][0];
                    for (int c = -cOff; c < extDims[0] - cOff; c++) {
                        int cOut = c + cOff;
                        int cIn = periodic(c, dims[0]);
                        int idx = cIn * vlen;
                        int idxExt = cOut * vlen;
                        for (int n = 0; n < vlen; n++) {
                            extData[idxExt + n] = data[idx + n];
                        }
                    }
                } else if (method == PaddingType.REFLECTED) {
                    int cOff = margins[0][0];
                    for (int c = -cOff; c < extDims[0] - cOff; c++) {
                        int cOut = c + cOff;
                        int cIn = reflected(c, dims[0]);
                        int idx = cIn * vlen;
                        int idxExt = cOut * vlen;
                        for (int n = 0; n < vlen; n++) {
                            extData[idxExt + n] = data[idx + n];
                        }
                    }
                }
                break;
        }
        return extData;
    }

    /*public static double[] extendMargins(int[] dims, int vlen, int[] extDims, int[][] margins, double[] data, int[][] method) {
        int nExt = 1;
        for (int i = 0; i < dims.length; i++) {
            extDims[i] = dims[i] + margins[i][0] + margins[i][1];
            nExt *= extDims[i];
        }
        double[] extData = new double[vlen * nExt];
        for (int i = 0; i < extData.length; i++) {
            extData[i] = 0;
        }
        switch (dims.length) {
            case 3: {
                for (int i = 0, m = 0; i < dims[2]; i++) {
                    for (int j = 0; j < dims[1]; j++) {
                        for (int k = 0, l = ((margins[2][0] + i) * extDims[1] + margins[1][0] + j) * extDims[0] + margins[0][0];
                                k < dims[0]; k++, l++, m++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l * vlen + n] = data[m * vlen + n];
                            }
                        }
                    }
                }
                int l = 0;
                for (int i = 0; i < dims[2]; i++) {
                    for (int j = 0; j < dims[1]; j++) {
                        l = (((margins[2][0] + i) * extDims[1] + margins[1][0] + j) * extDims[0] + margins[0][0]) * vlen;
                        if (method[0][0] >= PaddingType.FIXED) {
                            for (int k = 1; k <= margins[0][0]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l - k * vlen + n] = extData[l + n];
                                }
                            }
                        } else {
                            for (int k = 1; k <= margins[0][0]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l - k * vlen + n] = 0;
                                }
                            }
                        }
                        l = (((margins[2][0] + i) * extDims[1] + margins[1][0] + j) * extDims[0] + margins[0][0] + dims[0] - 1) * vlen;
                        if (method[0][1] >= PaddingType.FIXED) {
                            for (int k = 1; k <= margins[0][1]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l + k * vlen + n] = extData[l + n];
                                }
                            }
                        } else {
                            for (int k = 1; k <= margins[0][1]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l + k * vlen + n] = 0;
                                }
                            }
                        }
                    }
                }
                int m = extDims[0];
                for (int i = 0; i < dims[2]; i++) {
                    for (int j = 0; j < extDims[0]; j++) {
                        l = (((margins[2][0] + i) * extDims[1] + margins[1][0]) * extDims[0] + j) * vlen;
                        if (method[1][0] >= PaddingType.FIXED) {
                            for (int k = 1; k <= margins[1][0]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l - k * m * vlen + n] = extData[l + n];
                                }
                            }
                        } else {
                            for (int k = 1; k <= margins[1][0]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l - k * m * vlen + n] = 0;
                                }
                            }
                        }
                        l = (((margins[2][0] + i) * extDims[1] + margins[1][0] + dims[1] - 1) * extDims[0] + j) * vlen;
                        if (method[1][1] >= PaddingType.FIXED) {
                            for (int k = 1; k <= margins[1][1]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l + k * m * vlen + n] = extData[l + n];
                                }
                            }
                        } else {
                            for (int k = 1; k <= margins[1][1]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l + k * m * vlen + n] = 0;
                                }
                            }
                        }
                    }
                }
                m = extDims[1] * extDims[0];
                for (int i = 0; i < extDims[1]; i++) {
                    for (int j = 0; j < extDims[0]; j++) {
                        l = ((margins[2][0] * extDims[1] + i) * extDims[0] + j) * vlen;
                        if (method[2][0] >= PaddingType.FIXED) {
                            for (int k = 1; k <= margins[2][0]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l - k * m * vlen + n] = extData[l + n];
                                }
                            }
                        } else {
                            for (int k = 1; k <= margins[2][0]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l - k * m * vlen + n] = 0;
                                }
                            }
                        }
                        l = (((margins[2][0] + dims[2] - 1) * extDims[1] + i) * extDims[0] + j) * vlen;
                        if (method[2][1] >= PaddingType.FIXED) {
                            for (int k = 1; k <= margins[2][1]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l + k * m * vlen + n] = extData[l + n];
                                }
                            }
                        } else {
                            for (int k = 1; k <= margins[2][1]; k++) {
                                for (int n = 0; n < vlen; n++) {
                                    extData[l + k * m * vlen + n] = 0;
                                }
                            }
                        }
                    }
                }
            }
            break;
            case 2:
                for (int j = 0, m = 0; j < dims[1]; j++) {
                    for (int k = 0, l = (margins[1][0] + j) * extDims[0] + margins[0][0];
                            k < dims[0]; k++, l++, m++) {
                        for (int n = 0; n < vlen; n++) {
                            extData[l * vlen + n] = data[m * vlen + n];
                        }
                    }
                } {
                int l = 0;
                for (int j = 0; j < dims[1]; j++) {
                    l = ((margins[1][0] + j) * extDims[0] + margins[0][0]) * vlen;
                    if (method[0][0] >= PaddingType.FIXED) {
                        for (int k = 1; k <= margins[0][0]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l - k * vlen + n] = extData[l + n];
                            }
                        }
                    } else {
                        for (int k = 1; k <= margins[0][0]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l - k * vlen + n] = 0;
                            }
                        }
                    }
                    l = ((margins[1][0] + j) * extDims[0] + margins[0][0] + dims[0] - 1) * vlen;
                    if (method[0][1] >= PaddingType.FIXED) {
                        for (int k = 1; k <= margins[0][1]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l + k * vlen + n] = extData[l + n];
                            }
                        }
                    } else {
                        for (int k = 1; k <= margins[0][1]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l + k * vlen + n] = 0;
                            }
                        }
                    }
                }
                int m = extDims[0];
                for (int j = 0; j < extDims[0]; j++) {
                    l = ((margins[1][0]) * extDims[0] + j) * vlen;
                    if (method[1][0] >= PaddingType.FIXED) {
                        for (int k = 1; k <= margins[1][0]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l - k * m * vlen + n] = extData[l + n];
                            }
                        }
                    } else {
                        for (int k = 1; k <= margins[1][0]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l - k * m * vlen + n] = 0;
                            }
                        }
                    }
                    l = ((margins[1][0] + dims[1] - 1) * extDims[0] + j) * vlen;
                    if (method[1][1] >= PaddingType.FIXED) {
                        for (int k = 1; k <= margins[1][1]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l + k * m * vlen + n] = extData[l + n];
                            }
                        }
                    } else {
                        for (int k = 1; k <= margins[1][1]; k++) {
                            for (int n = 0; n < vlen; n++) {
                                extData[l + k * m * vlen + n] = 0;
                            }
                        }
                    }
                }
            }
            break;
            case 1:
                for (int k = 0, m = 0, l = margins[0][0];
                        k < dims[0]; k++, l++, m++) {
                    for (int n = 0; n < vlen; n++) {
                        extData[l * vlen + n] = data[m * vlen + n];
                    }
                } {
                int l = (margins[0][0]) * vlen;
                if (method[0][0] >= PaddingType.FIXED) {
                    for (int k = 1; k <= margins[0][0]; k++) {
                        for (int n = 0; n < vlen; n++) {
                            extData[l - k * vlen + n] = extData[l + n];
                        }
                    }
                } else {
                    for (int k = 1; k <= margins[0][0]; k++) {
                        for (int n = 0; n < vlen; n++) {
                            extData[l - k * vlen + n] = 0;
                        }
                    }
                }
                l = (margins[0][0] + dims[0] - 1) * vlen;
                if (method[0][1] >= PaddingType.FIXED) {
                    for (int k = 1; k <= margins[0][1]; k++) {
                        for (int n = 0; n < vlen; n++) {
                            extData[l + k * vlen + n] = extData[l + n];
                        }
                    }
                } else {
                    for (int k = 1; k <= margins[0][1]; k++) {
                        for (int n = 0; n < vlen; n++) {
                            extData[l + k * vlen + n] = 0;
                        }
                    }
                }
            }
            break;
        }
        return extData;
    }*/

    public static void fillOut3DMargins(byte[] data, int[] dims, byte val) {
        int vlen = data.length / (dims[0] * dims[1] * dims[2]);
        if (data.length != vlen * dims[0] * dims[1] * dims[2]) {
            return;
        }
        int k = dims[0] - 1;
        for (int i2 = 0; i2 < dims[2]; i2++) {
            for (int i1 = 0; i1 < dims[1]; i1++) {
                data[(i2 * dims[1] + i1) * dims[0]] =
                        data[(i2 * dims[1] + i1) * dims[0] + k] = val;
            }
        }
        k = dims[0] * (dims[1] - 1);
        for (int i2 = 0; i2 < dims[2]; i2++) {
            for (int i0 = 0; i0 < dims[0]; i0++) {
                data[i2 * dims[1] * dims[0] + i0] =
                        data[i2 * dims[1] * dims[0] + i0 + k] = val;
            }
        }
        k = dims[0] * dims[1] * (dims[2] - 1);
        for (int i1 = 0; i1 < dims[1]; i1++) {
            for (int i0 = 0; i0 < dims[0]; i0++) {
                data[dims[0] * i1 + i0] =
                        data[dims[0] * i1 + i0 + k] = val;
            }
        }
    }

    public static void fillOut3DMargins(byte[] data, int[] dims) {
        int vlen = data.length / (dims[0] * dims[1] * dims[2]);
        if (data.length != vlen * dims[0] * dims[1] * dims[2]) {
            return;
        }
        int k = dims[0] - 1;
        for (int i2 = 0; i2 < dims[2]; i2++) {
            for (int i1 = 0; i1 < dims[1]; i1++) {
                data[(i2 * dims[1] + i1) * dims[0]] = data[(i2 * dims[1] + i1) * dims[0] + 1];
                data[(i2 * dims[1] + i1) * dims[0] + k] = data[(i2 * dims[1] + i1) * dims[0] + k - 1];
            }
        }
        k = dims[0] * (dims[1] - 1);
        int l = dims[0];
        for (int i2 = 0; i2 < dims[2]; i2++) {
            for (int i0 = 0; i0 < dims[0]; i0++) {
                data[i2 * dims[1] * dims[0] + i0] = data[i2 * dims[1] * dims[0] + i0 + l];
                data[i2 * dims[1] * dims[0] + i0 + k] = data[i2 * dims[1] * dims[0] + i0 + k - l];
            }
        }
        k = dims[0] * dims[1] * (dims[2] - 1);
        l = dims[0] * dims[1];
        for (int i = 0; i < dims[1] * dims[0]; i++) {
            data[i] = data[i + l];
            data[i + k] = data[i + k + l];
        }
    }

    public static void fillOut3DMargins(short[] data, int[] dims, short val) {
        int vlen = data.length / (dims[0] * dims[1] * dims[2]);
        if (data.length != vlen * dims[0] * dims[1] * dims[2]) {
            return;
        }
        int k = dims[0] - 1;
        for (int i2 = 0; i2 < dims[2]; i2++) {
            for (int i1 = 0; i1 < dims[1]; i1++) {
                data[(i2 * dims[1] + i1) * dims[0]] =
                        data[(i2 * dims[1] + i1) * dims[0] + k] = val;
            }
        }
        k = dims[0] * (dims[1] - 1);
        for (int i2 = 0; i2 < dims[2]; i2++) {
            for (int i0 = 0; i0 < dims[0]; i0++) {
                data[i2 * dims[1] * dims[0] + i0] =
                        data[i2 * dims[1] * dims[0] + i0 + k] = val;
            }
        }
        k = dims[0] * dims[1] * (dims[2] - 1);
        for (int i1 = 0; i1 < dims[1]; i1++) {
            for (int i0 = 0; i0 < dims[0]; i0++) {
                data[dims[0] * i1 + i0] =
                        data[dims[0] * i1 + i0 + k] = val;
            }
        }
    }

    public static void fillOut3DMargins(short[] data, int[] dims) {
        int vlen = data.length / (dims[0] * dims[1] * dims[2]);
        if (data.length != vlen * dims[0] * dims[1] * dims[2]) {
            return;
        }
        int k = dims[0] - 1;
        for (int i2 = 0; i2 < dims[2]; i2++) {
            for (int i1 = 0; i1 < dims[1]; i1++) {
                data[(i2 * dims[1] + i1) * dims[0]] = data[(i2 * dims[1] + i1) * dims[0] + 1];
                data[(i2 * dims[1] + i1) * dims[0] + k] = data[(i2 * dims[1] + i1) * dims[0] + k - 1];
            }
        }
        k = dims[0] * (dims[1] - 1);
        int l = dims[0];
        for (int i2 = 0; i2 < dims[2]; i2++) {
            for (int i0 = 0; i0 < dims[0]; i0++) {
                data[i2 * dims[1] * dims[0] + i0] = data[i2 * dims[1] * dims[0] + i0 + l];
                data[i2 * dims[1] * dims[0] + i0 + k] = data[i2 * dims[1] * dims[0] + i0 + k - l];
            }
        }
        k = dims[0] * dims[1] * (dims[2] - 1);
        l = dims[0] * dims[1];
        for (int i = 0; i < dims[1] * dims[0]; i++) {
            data[i] = data[i + l];
            data[i + k] = data[i + k + l];
        }
    }

    public static void fillOut3DMargins(int[] data, int[] dims, int val) {
        int vlen = data.length / (dims[0] * dims[1] * dims[2]);
        if (data.length != vlen * dims[0] * dims[1] * dims[2]) {
            return;
        }
        int k = dims[0] - 1;
        for (int i2 = 0; i2 < dims[2]; i2++) {
            for (int i1 = 0; i1 < dims[1]; i1++) {
                data[(i2 * dims[1] + i1) * dims[0]] =
                        data[(i2 * dims[1] + i1) * dims[0] + k] = val;
            }
        }
        k = dims[0] * (dims[1] - 1);
        for (int i2 = 0; i2 < dims[2]; i2++) {
            for (int i0 = 0; i0 < dims[0]; i0++) {
                data[i2 * dims[1] * dims[0] + i0] =
                        data[i2 * dims[1] * dims[0] + i0 + k] = val;
            }
        }
        k = dims[0] * dims[1] * (dims[2] - 1);
        for (int i1 = 0; i1 < dims[1]; i1++) {
            for (int i0 = 0; i0 < dims[0]; i0++) {
                data[dims[0] * i1 + i0] =
                        data[dims[0] * i1 + i0 + k] = val;
            }
        }
    }

    public static void fillOut3DMargins(int[] data, int[] dims) {
        int vlen = data.length / (dims[0] * dims[1] * dims[2]);
        if (data.length != vlen * dims[0] * dims[1] * dims[2]) {
            return;
        }
        int k = dims[0] - 1;
        for (int i2 = 0; i2 < dims[2]; i2++) {
            for (int i1 = 0; i1 < dims[1]; i1++) {
                data[(i2 * dims[1] + i1) * dims[0]] = data[(i2 * dims[1] + i1) * dims[0] + 1];
                data[(i2 * dims[1] + i1) * dims[0] + k] = data[(i2 * dims[1] + i1) * dims[0] + k - 1];
            }
        }
        k = dims[0] * (dims[1] - 1);
        int l = dims[0];
        for (int i2 = 0; i2 < dims[2]; i2++) {
            for (int i0 = 0; i0 < dims[0]; i0++) {
                data[i2 * dims[1] * dims[0] + i0] = data[i2 * dims[1] * dims[0] + i0 + l];
                data[i2 * dims[1] * dims[0] + i0 + k] = data[i2 * dims[1] * dims[0] + i0 + k - l];
            }
        }
        k = dims[0] * dims[1] * (dims[2] - 1);
        l = dims[0] * dims[1];
        for (int i = 0; i < dims[1] * dims[0]; i++) {
            data[i] = data[i + l];
            data[i + k] = data[i + k + l];
        }
    }

    public static void fillOut3DMargins(float[] data, int[] dims, float val) {
        int vlen = data.length / (dims[0] * dims[1] * dims[2]);
        if (data.length != vlen * dims[0] * dims[1] * dims[2]) {
            return;
        }
        int k = dims[0] - 1;
        for (int i2 = 0; i2 < dims[2]; i2++) {
            for (int i1 = 0; i1 < dims[1]; i1++) {
                data[(i2 * dims[1] + i1) * dims[0]] =
                        data[(i2 * dims[1] + i1) * dims[0] + k] = val;
            }
        }
        k = dims[0] * (dims[1] - 1);
        for (int i2 = 0; i2 < dims[2]; i2++) {
            for (int i0 = 0; i0 < dims[0]; i0++) {
                data[i2 * dims[1] * dims[0] + i0] =
                        data[i2 * dims[1] * dims[0] + i0 + k] = val;
            }
        }
        k = dims[0] * dims[1] * (dims[2] - 1);
        for (int i1 = 0; i1 < dims[1]; i1++) {
            for (int i0 = 0; i0 < dims[0]; i0++) {
                data[dims[0] * i1 + i0] =
                        data[dims[0] * i1 + i0 + k] = val;
            }
        }
    }

    public static void fillOut3DMargins(float[] data, int[] dims) {
        int vlen = data.length / (dims[0] * dims[1] * dims[2]);
        if (data.length != vlen * dims[0] * dims[1] * dims[2]) {
            return;
        }
        int k = dims[0] - 1;
        for (int i2 = 0; i2 < dims[2]; i2++) {
            for (int i1 = 0; i1 < dims[1]; i1++) {
                data[(i2 * dims[1] + i1) * dims[0]] = data[(i2 * dims[1] + i1) * dims[0] + 1];
                data[(i2 * dims[1] + i1) * dims[0] + k] = data[(i2 * dims[1] + i1) * dims[0] + k - 1];
            }
        }
        k = dims[0] * (dims[1] - 1);
        int l = dims[0];
        for (int i2 = 0; i2 < dims[2]; i2++) {
            for (int i0 = 0; i0 < dims[0]; i0++) {
                data[i2 * dims[1] * dims[0] + i0] = data[i2 * dims[1] * dims[0] + i0 + l];
                data[i2 * dims[1] * dims[0] + i0 + k] = data[i2 * dims[1] * dims[0] + i0 + k - l];
            }
        }
        k = dims[0] * dims[1] * (dims[2] - 1);
        l = dims[0] * dims[1];
        for (int i = 0; i < dims[1] * dims[0]; i++) {
            data[i] = data[i + l];
            data[i + k] = data[i + k + l];
        }
    }

    public static void fillOut3DMargins(double[] data, int[] dims, double val) {
        int vlen = data.length / (dims[0] * dims[1] * dims[2]);
        if (data.length != vlen * dims[0] * dims[1] * dims[2]) {
            return;
        }
        int k = dims[0] - 1;
        for (int i2 = 0; i2 < dims[2]; i2++) {
            for (int i1 = 0; i1 < dims[1]; i1++) {
                data[(i2 * dims[1] + i1) * dims[0]] =
                        data[(i2 * dims[1] + i1) * dims[0] + k] = val;
            }
        }
        k = dims[0] * (dims[1] - 1);
        for (int i2 = 0; i2 < dims[2]; i2++) {
            for (int i0 = 0; i0 < dims[0]; i0++) {
                data[i2 * dims[1] * dims[0] + i0] =
                        data[i2 * dims[1] * dims[0] + i0 + k] = val;
            }
        }
        k = dims[0] * dims[1] * (dims[2] - 1);
        for (int i1 = 0; i1 < dims[1]; i1++) {
            for (int i0 = 0; i0 < dims[0]; i0++) {
                data[dims[0] * i1 + i0] =
                        data[dims[0] * i1 + i0 + k] = val;
            }
        }
    }

    public static void fillOut3DMargins(double[] data, int[] dims) {
        int vlen = data.length / (dims[0] * dims[1] * dims[2]);
        if (data.length != vlen * dims[0] * dims[1] * dims[2]) {
            return;
        }
        int k = dims[0] - 1;
        for (int i2 = 0; i2 < dims[2]; i2++) {
            for (int i1 = 0; i1 < dims[1]; i1++) {
                data[(i2 * dims[1] + i1) * dims[0]] = data[(i2 * dims[1] + i1) * dims[0] + 1];
                data[(i2 * dims[1] + i1) * dims[0] + k] = data[(i2 * dims[1] + i1) * dims[0] + k - 1];
            }
        }
        k = dims[0] * (dims[1] - 1);
        int l = dims[0];
        for (int i2 = 0; i2 < dims[2]; i2++) {
            for (int i0 = 0; i0 < dims[0]; i0++) {
                data[i2 * dims[1] * dims[0] + i0] = data[i2 * dims[1] * dims[0] + i0 + l];
                data[i2 * dims[1] * dims[0] + i0 + k] = data[i2 * dims[1] * dims[0] + i0 + k - l];
            }
        }
        k = dims[0] * dims[1] * (dims[2] - 1);
        l = dims[0] * dims[1];
        for (int i = 0; i < dims[1] * dims[0]; i++) {
            data[i] = data[i + l];
            data[i + k] = data[i + k + l];
        }
    }

    private static int reflected(int i, int n) {
        int ip = mod(i, 2 * n);
        if (ip < n) {
            return ip;
        } else {
            return n - (ip % n) - 1;
        }
    }

    private static int mod(int i, int n) {
        return ((i % n) + n) % n;
    }

    private static int periodic(int i, int n) {
        int ip = mod(i, 2 * n);
        if (ip < n) {
            return ip;
        } else {
            return (ip % n);
        }
    }

    private ExtendMargins() {
    }
}
