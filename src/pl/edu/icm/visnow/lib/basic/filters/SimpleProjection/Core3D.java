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

import pl.edu.icm.visnow.datasets.dataarrays.ComplexDataArray;
import pl.edu.icm.visnow.datasets.dataarrays.DataArray;
import pl.edu.icm.visnow.datasets.RegularField;


/**
 * @author  Bartosz Borucki (babor@icm.edu.pl)
 * Warsaw University, Interdisciplinary Centre
 * for Mathematical and Computational Modelling
 */
public class Core3D {
    private Params params = new Params();
    private RegularField inField = null;
    private RegularField outField = null;
        
    public Core3D() {
        
    }

    public void setParams(Params params) {
        this.params = params;
    }
    
    public void update() {
        if(inField == null) {
            outField = null;
            return;
        }

        if(inField.getDims().length != 3) {
            outField = null;
            return;
        }

        int[] inDims = inField.getDims();
        int[] outDims = new int[2];
        int u,v;
        if(inDims == null) {
            outField = null;
            return;
        }

        int axis = params.getAxisL0();
        int method = params.getMethod();

        switch(axis) {
            case 0:
                u = 1;
                v = 2;
                break;
            case 1:
                u = 0;
                v = 2;
                break;
            case 2:
                u = 0;
                v = 1;
                break;
            default:
                u = 0;
                v = 1;
        }
        outDims[0] = inDims[u];
        outDims[1] = inDims[v];
        outField = new RegularField(outDims);
        outField.setNSpace(3);


        float[][] inAffine = inField.getAffine();
        float[][] outAffine = new float[4][3];

        for (int i = 0; i < 3; i++) {
            outAffine[0][i] = inAffine[u][i];
            outAffine[1][i] = inAffine[v][i];
            outAffine[2][i] = 0.0f;
            outAffine[3][i] = inAffine[3][i];
        }
        outField.setAffine(outAffine);
        
        for (int i = 0; i < inField.getNData(); i++) {
            switch(inField.getData(i).getType()) {
                case DataArray.FIELD_DATA_SHORT:
                    short[] sOut;
                    sOut = projection(inField.getData(i).getSData(), inField.getData(i).getVeclen(), inDims, outDims, axis, method);
                    outField.addData(DataArray.create(sOut, inField.getData(i).getVeclen(), inField.getData(i).getName()));
                    break;
                case DataArray.FIELD_DATA_INT:
                    int[] iOut;
                    iOut = projection(inField.getData(i).getIData(), inField.getData(i).getVeclen(), inDims, outDims, axis, method);
                    outField.addData(DataArray.create(iOut, inField.getData(i).getVeclen(), inField.getData(i).getName()));
                    break;
                case DataArray.FIELD_DATA_FLOAT:
                    float[] fOut;
                    fOut = projection(inField.getData(i).getFData(), inField.getData(i).getVeclen(), inDims, outDims, axis, method);
                    outField.addData(DataArray.create(fOut, inField.getData(i).getVeclen(), inField.getData(i).getName()));
                    break;
                case DataArray.FIELD_DATA_DOUBLE:
                    double[] dOut;
                    dOut = projection(inField.getData(i).getDData(), inField.getData(i).getVeclen(), inDims, outDims, axis, method);
                    outField.addData(DataArray.create(dOut, inField.getData(i).getVeclen(), inField.getData(i).getName()));
                    break;
                case DataArray.FIELD_DATA_COMPLEX:
                    float[] fOutR, fOutI;
                    fOutR = projection(((ComplexDataArray)inField.getData(i)).getFRealData(), inField.getData(i).getVeclen(), inDims, outDims, axis, method);
                    fOutI = projection(((ComplexDataArray)inField.getData(i)).getFImagData(), inField.getData(i).getVeclen(), inDims, outDims, axis, method);
                    outField.addData(DataArray.create(fOutR, fOutI, inField.getData(i).getVeclen(), inField.getData(i).getName()));
                    break;
                case DataArray.FIELD_DATA_BYTE:
                    byte[] bOut;
                    bOut = projection(inField.getData(i).getBData(), inField.getData(i).getVeclen(), inDims, outDims, axis, method);
                    outField.addData(DataArray.create(bOut, inField.getData(i).getVeclen(), inField.getData(i).getName()));
                    break;
                default:
                    /* Not numeric type, like "String". */
            }
        }
    }

    /**
     * @param inField the inField to set
     */
    public void setInField(RegularField inField) {
        this.inField = inField;
    }

    /**
     * @return the outField
     */
    public RegularField getOutField() {
        return outField;
    }

    private byte[] projection(byte[] data, int veclen, int[] inDims, int[] outDims, int axis, int method) {
        if(inDims == null || inDims.length != 3)
            return null;
        if(outDims == null || outDims.length != 2)
            return null;

        byte[] out = new byte[outDims[0]*outDims[1]*veclen];
        byte[] tmp = new byte[inDims[axis]*veclen];
        int off;
        int off2;
        switch(axis) {
            case 0:
                for (int z = 0; z < inDims[2]; z++) {
                    for (int y = 0; y < inDims[1]; y++) {
                        off = z*inDims[1]*inDims[0]*veclen + y*inDims[0]*veclen;
                        for (int x = 0; x < inDims[0]; x++) {
                            off2 = x*veclen + off;
                            for (int v = 0; v < veclen; v++) {
                                tmp[x+v] = data[off2 + v];
                            }
                        }
                        off = z*inDims[1]*veclen + y*veclen;
                        switch(method) {
                            case Params.METHOD_MAX:
                                for (int v = 0; v < veclen; v++) {
                                    out[off + v] = max(tmp);
                                }
                                break;
                            case Params.METHOD_MEAN:
                            case Params.METHOD_NORMALIZED_MEAN:
                                for (int v = 0; v < veclen; v++) {
                                    out[off + v] = mean(tmp);
                                }
                                break;
                            case Params.METHOD_MIN:
                                for (int v = 0; v < veclen; v++) {
                                    out[off + v] = min(tmp);
                                }
                                break;
                        }
                    }
                }
                break;
            case 1:
                for (int z = 0; z < inDims[2]; z++) {
                    for (int x = 0; x < inDims[0]; x++) {
                        off = z*inDims[1]*inDims[0]*veclen + x*veclen;
                        for (int y = 0; y < inDims[1]; y++) {
                            off2 = y*inDims[0]*veclen + off;
                            for (int v = 0; v < veclen; v++) {
                                tmp[y+v] = data[off2 + v];
                            }
                        }
                        off = z*inDims[0]*veclen + x*veclen;
                        switch(method) {
                            case Params.METHOD_MAX:
                                for (int v = 0; v < veclen; v++) {
                                    out[off + v] = max(tmp);
                                }
                                break;
                            case Params.METHOD_MIN:
                                for (int v = 0; v < veclen; v++) {
                                    out[off + v] = min(tmp);
                                }
                                break;
                            case Params.METHOD_NORMALIZED_MEAN:
                            case Params.METHOD_MEAN:
                                for (int v = 0; v < veclen; v++) {
                                    out[off + v] = mean(tmp);
                                }
                                break;
                        }
                    }
                }
                break;
            case 2:
                for (int y = 0; y < inDims[1]; y++) {
                    for (int x = 0; x < inDims[0]; x++) {
                        off = y*inDims[0]*veclen + x*veclen;
                        for (int z = 0; z < inDims[2]; z++) {
                            off2 = z*inDims[0]*inDims[1]*veclen + off;
                            for (int v = 0; v < veclen; v++) {
                                tmp[z+v] = data[off2 + v];
                            }
                        }
                        off = y*inDims[0]*veclen + x*veclen;
                        switch(method) {
                            case Params.METHOD_MAX:
                                for (int v = 0; v < veclen; v++) {
                                    out[off + v] = max(tmp);
                                }
                                break;
                            case Params.METHOD_MEAN:
                            case Params.METHOD_NORMALIZED_MEAN:
                                for (int v = 0; v < veclen; v++) {
                                    out[off + v] = mean(tmp);
                                }
                                break;
                            case Params.METHOD_MIN:
                                for (int v = 0; v < veclen; v++) {
                                    out[off + v] = min(tmp);
                                }
                                break;
                        }
                    }
                }
                break;
        }

        if(method == Params.METHOD_NORMALIZED_MEAN) {
            int v = (out[0] & 0xff);
            int max = v;
            int min = v;
            for (int i = 0; i < out.length; i++) {
                v = (out[i] & 0xff);
                if(v > max) max = v;
                if(v < min) min = v;
            }

            float s = 255.0f / ((float)max - (float)min);
            int newv;
            for (int i = 0; i < out.length; i++) {
                v = (out[i] & 0xff);
                newv = Math.round(s * ((float) v - (float) min));
                out[i] = (byte)newv;
            }
        }

        return out;
    }

    private int[] projection(int[] data, int veclen, int[] inDims, int[] outDims, int axis, int method) {
        if(inDims == null || inDims.length != 3)
            return null;
        if(outDims == null || outDims.length != 2)
            return null;

        int[] out = new int[outDims[0]*outDims[1]*veclen];
        int[] tmp = new int[inDims[axis]*veclen];
        int off;
        int off2;
        switch(axis) {
            case 0:
                for (int z = 0; z < inDims[2]; z++) {
                    for (int y = 0; y < inDims[1]; y++) {
                        off = z*inDims[1]*inDims[0]*veclen + y*inDims[0]*veclen;
                        for (int x = 0; x < inDims[0]; x++) {
                            off2 = x*veclen + off;
                            for (int v = 0; v < veclen; v++) {
                                tmp[x+v] = data[off2 + v];
                            }
                        }
                        off = z*inDims[1]*veclen + y*veclen;
                        switch(method) {
                            case Params.METHOD_MAX:
                                for (int v = 0; v < veclen; v++) {
                                    out[off + v] = max(tmp);
                                }
                                break;
                            case Params.METHOD_MEAN:
                            case Params.METHOD_NORMALIZED_MEAN:
                                for (int v = 0; v < veclen; v++) {
                                    out[off + v] = mean(tmp);
                                }
                                break;
                            case Params.METHOD_MIN:
                                for (int v = 0; v < veclen; v++) {
                                    out[off + v] = min(tmp);
                                }
                                break;
                        }
                    }
                }
                break;
            case 1:
                for (int z = 0; z < inDims[2]; z++) {
                    for (int x = 0; x < inDims[0]; x++) {
                        off = z*inDims[1]*inDims[0]*veclen + x*veclen;
                        for (int y = 0; y < inDims[1]; y++) {
                            off2 = y*inDims[0]*veclen + off;
                            for (int v = 0; v < veclen; v++) {
                                tmp[y+v] = data[off2 + v];
                            }
                        }
                        off = z*inDims[0]*veclen + x*veclen;
                        switch(method) {
                            case Params.METHOD_MAX:
                                for (int v = 0; v < veclen; v++) {
                                    out[off + v] = max(tmp);
                                }
                                break;
                            case Params.METHOD_MEAN:
                            case Params.METHOD_NORMALIZED_MEAN:
                                for (int v = 0; v < veclen; v++) {
                                    out[off + v] = mean(tmp);
                                }
                                break;
                            case Params.METHOD_MIN:
                                for (int v = 0; v < veclen; v++) {
                                    out[off + v] = min(tmp);
                                }
                                break;
                        }
                    }
                }
                break;
            case 2:
                for (int y = 0; y < inDims[1]; y++) {
                    for (int x = 0; x < inDims[0]; x++) {
                        off = y*inDims[0]*veclen + x*veclen;
                        for (int z = 0; z < inDims[2]; z++) {
                            off2 = z*inDims[0]*inDims[1]*veclen + off;
                            for (int v = 0; v < veclen; v++) {
                                tmp[z+v] = data[off2 + v];
                            }
                        }
                        off = y*inDims[0]*veclen + x*veclen;
                        switch(method) {
                            case Params.METHOD_MAX:
                                for (int v = 0; v < veclen; v++) {
                                    out[off + v] = max(tmp);
                                }
                                break;
                            case Params.METHOD_MEAN:
                            case Params.METHOD_NORMALIZED_MEAN:
                                for (int v = 0; v < veclen; v++) {
                                    out[off + v] = mean(tmp);
                                }
                                break;
                            case Params.METHOD_MIN:
                                for (int v = 0; v < veclen; v++) {
                                    out[off + v] = min(tmp);
                                }
                                break;
                        }
                    }
                }
                break;
        }
        return out;
    }

    private short[] projection(short[] data, int veclen, int[] inDims, int[] outDims, int axis, int method) {
        if(inDims == null || inDims.length != 3)
            return null;
        if(outDims == null || outDims.length != 2)
            return null;

        short[] out = new short[outDims[0]*outDims[1]*veclen];
        short[] tmp = new short[inDims[axis]*veclen];
        int off;
        int off2;
        switch(axis) {
            case 0:
                for (int z = 0; z < inDims[2]; z++) {
                    for (int y = 0; y < inDims[1]; y++) {
                        off = z*inDims[1]*inDims[0]*veclen + y*inDims[0]*veclen;
                        for (int x = 0; x < inDims[0]; x++) {
                            off2 = x*veclen + off;
                            for (int v = 0; v < veclen; v++) {
                                tmp[x+v] = data[off2 + v];
                            }
                        }
                        off = z*inDims[1]*veclen + y*veclen;
                        switch(method) {
                            case Params.METHOD_MAX:
                                for (int v = 0; v < veclen; v++) {
                                    out[off + v] = max(tmp);
                                }
                                break;
                            case Params.METHOD_MEAN:
                            case Params.METHOD_NORMALIZED_MEAN:
                                for (int v = 0; v < veclen; v++) {
                                    out[off + v] = mean(tmp);
                                }
                                break;
                            case Params.METHOD_MIN:
                                for (int v = 0; v < veclen; v++) {
                                    out[off + v] = min(tmp);
                                }
                                break;
                        }
                    }
                }
                break;
            case 1:
                for (int z = 0; z < inDims[2]; z++) {
                    for (int x = 0; x < inDims[0]; x++) {
                        off = z*inDims[1]*inDims[0]*veclen + x*veclen;
                        for (int y = 0; y < inDims[1]; y++) {
                            off2 = y*inDims[0]*veclen + off;
                            for (int v = 0; v < veclen; v++) {
                                tmp[y+v] = data[off2 + v];
                            }
                        }
                        off = z*inDims[0]*veclen + x*veclen;
                        switch(method) {
                            case Params.METHOD_MAX:
                                for (int v = 0; v < veclen; v++) {
                                    out[off + v] = max(tmp);
                                }
                                break;
                            case Params.METHOD_MEAN:
                            case Params.METHOD_NORMALIZED_MEAN:
                                for (int v = 0; v < veclen; v++) {
                                    out[off + v] = mean(tmp);
                                }
                                break;
                            case Params.METHOD_MIN:
                                for (int v = 0; v < veclen; v++) {
                                    out[off + v] = min(tmp);
                                }
                                break;
                        }
                    }
                }
                break;
            case 2:
                for (int y = 0; y < inDims[1]; y++) {
                    for (int x = 0; x < inDims[0]; x++) {
                        off = y*inDims[0]*veclen + x*veclen;
                        for (int z = 0; z < inDims[2]; z++) {
                            off2 = z*inDims[0]*inDims[1]*veclen + off;
                            for (int v = 0; v < veclen; v++) {
                                tmp[z+v] = data[off2 + v];
                            }
                        }
                        off = y*inDims[0]*veclen + x*veclen;
                        switch(method) {
                            case Params.METHOD_MAX:
                                for (int v = 0; v < veclen; v++) {
                                    out[off + v] = max(tmp);
                                }
                                break;
                            case Params.METHOD_MEAN:
                            case Params.METHOD_NORMALIZED_MEAN:
                                for (int v = 0; v < veclen; v++) {
                                    out[off + v] = mean(tmp);
                                }
                                break;
                            case Params.METHOD_MIN:
                                for (int v = 0; v < veclen; v++) {
                                    out[off + v] = min(tmp);
                                }
                                break;
                        }
                    }
                }
                break;
        }
        return out;
    }


    private float[] projection(float[] data, int veclen, int[] inDims, int[] outDims, int axis, int method) {
        if(inDims == null || inDims.length != 3)
            return null;
        if(outDims == null || outDims.length != 2)
            return null;

        float[] out = new float[outDims[0]*outDims[1]*veclen];
        float[] tmp = new float[inDims[axis]*veclen];
        int off;
        int off2;
        switch(axis) {
            case 0:
                for (int z = 0; z < inDims[2]; z++) {
                    for (int y = 0; y < inDims[1]; y++) {
                        off = z*inDims[1]*inDims[0]*veclen + y*inDims[0]*veclen;
                        for (int x = 0; x < inDims[0]; x++) {
                            off2 = x*veclen + off;
                            for (int v = 0; v < veclen; v++) {
                                tmp[x+v] = data[off2 + v];
                            }
                        }
                        off = z*inDims[1]*veclen + y*veclen;
                        switch(method) {
                            case Params.METHOD_MAX:
                                for (int v = 0; v < veclen; v++) {
                                    out[off + v] = max(tmp);
                                }
                                break;
                            case Params.METHOD_MEAN:
                            case Params.METHOD_NORMALIZED_MEAN:
                                for (int v = 0; v < veclen; v++) {
                                    out[off + v] = mean(tmp);
                                }
                                break;
                            case Params.METHOD_MIN:
                                for (int v = 0; v < veclen; v++) {
                                    out[off + v] = min(tmp);
                                }
                                break;
                        }
                    }
                }
                break;
            case 1:
                for (int z = 0; z < inDims[2]; z++) {
                    for (int x = 0; x < inDims[0]; x++) {
                        off = z*inDims[1]*inDims[0]*veclen + x*veclen;
                        for (int y = 0; y < inDims[1]; y++) {
                            off2 = y*inDims[0]*veclen + off;
                            for (int v = 0; v < veclen; v++) {
                                tmp[y+v] = data[off2 + v];
                            }
                        }
                        off = z*inDims[0]*veclen + x*veclen;
                        switch(method) {
                            case Params.METHOD_MAX:
                                for (int v = 0; v < veclen; v++) {
                                    out[off + v] = max(tmp);
                                }
                                break;
                            case Params.METHOD_MEAN:
                            case Params.METHOD_NORMALIZED_MEAN:
                                for (int v = 0; v < veclen; v++) {
                                    out[off + v] = mean(tmp);
                                }
                                break;
                            case Params.METHOD_MIN:
                                for (int v = 0; v < veclen; v++) {
                                    out[off + v] = min(tmp);
                                }
                                break;
                        }
                    }
                }
                break;
            case 2:
                for (int y = 0; y < inDims[1]; y++) {
                    for (int x = 0; x < inDims[0]; x++) {
                        off = y*inDims[0]*veclen + x*veclen;
                        for (int z = 0; z < inDims[2]; z++) {
                            off2 = z*inDims[0]*inDims[1]*veclen + off;
                            for (int v = 0; v < veclen; v++) {
                                tmp[z+v] = data[off2 + v];
                            }
                        }
                        off = y*inDims[0]*veclen + x*veclen;
                        switch(method) {
                            case Params.METHOD_MAX:
                                for (int v = 0; v < veclen; v++) {
                                    out[off + v] = max(tmp);
                                }
                                break;
                            case Params.METHOD_MEAN:
                            case Params.METHOD_NORMALIZED_MEAN:
                                for (int v = 0; v < veclen; v++) {
                                    out[off + v] = mean(tmp);
                                }
                                break;
                            case Params.METHOD_MIN:
                                for (int v = 0; v < veclen; v++) {
                                    out[off + v] = min(tmp);
                                }
                                break;
                        }
                    }
                }
                break;
        }
        return out;
    }

    private double[] projection(double[] data, int veclen, int[] inDims, int[] outDims, int axis, int method) {
        if(inDims == null || inDims.length != 3)
            return null;
        if(outDims == null || outDims.length != 2)
            return null;

        double[] out = new double[outDims[0]*outDims[1]*veclen];
        double[] tmp = new double[inDims[axis]*veclen];
        int off;
        int off2;
        switch(axis) {
            case 0:
                for (int z = 0; z < inDims[2]; z++) {
                    for (int y = 0; y < inDims[1]; y++) {
                        off = z*inDims[1]*inDims[0]*veclen + y*inDims[0]*veclen;
                        for (int x = 0; x < inDims[0]; x++) {
                            off2 = x*veclen + off;
                            for (int v = 0; v < veclen; v++) {
                                tmp[x+v] = data[off2 + v];
                            }
                        }
                        off = z*inDims[1]*veclen + y*veclen;
                        switch(method) {
                            case Params.METHOD_MAX:
                                for (int v = 0; v < veclen; v++) {
                                    out[off + v] = max(tmp);
                                }
                                break;
                            case Params.METHOD_MEAN:
                            case Params.METHOD_NORMALIZED_MEAN:
                                for (int v = 0; v < veclen; v++) {
                                    out[off + v] = mean(tmp);
                                }
                                break;
                            case Params.METHOD_MIN:
                                for (int v = 0; v < veclen; v++) {
                                    out[off + v] = min(tmp);
                                }
                                break;
                        }
                    }
                }
                break;
            case 1:
                for (int z = 0; z < inDims[2]; z++) {
                    for (int x = 0; x < inDims[0]; x++) {
                        off = z*inDims[1]*inDims[0]*veclen + x*veclen;
                        for (int y = 0; y < inDims[1]; y++) {
                            off2 = y*inDims[0]*veclen + off;
                            for (int v = 0; v < veclen; v++) {
                                tmp[y+v] = data[off2 + v];
                            }
                        }
                        off = z*inDims[0]*veclen + x*veclen;
                        switch(method) {
                            case Params.METHOD_MAX:
                                for (int v = 0; v < veclen; v++) {
                                    out[off + v] = max(tmp);
                                }
                                break;
                            case Params.METHOD_MEAN:
                            case Params.METHOD_NORMALIZED_MEAN:
                                for (int v = 0; v < veclen; v++) {
                                    out[off + v] = mean(tmp);
                                }
                                break;
                            case Params.METHOD_MIN:
                                for (int v = 0; v < veclen; v++) {
                                    out[off + v] = min(tmp);
                                }
                                break;
                        }
                    }
                }
                break;
            case 2:
                for (int y = 0; y < inDims[1]; y++) {
                    for (int x = 0; x < inDims[0]; x++) {
                        off = y*inDims[0]*veclen + x*veclen;
                        for (int z = 0; z < inDims[2]; z++) {
                            off2 = z*inDims[0]*inDims[1]*veclen + off;
                            for (int v = 0; v < veclen; v++) {
                                tmp[z+v] = data[off2 + v];
                            }
                        }
                        off = y*inDims[0]*veclen + x*veclen;
                        switch(method) {
                            case Params.METHOD_MAX:
                                for (int v = 0; v < veclen; v++) {
                                    out[off + v] = max(tmp);
                                }
                                break;
                            case Params.METHOD_MEAN:
                            case Params.METHOD_NORMALIZED_MEAN:
                                for (int v = 0; v < veclen; v++) {
                                    out[off + v] = mean(tmp);
                                }
                                break;
                            case Params.METHOD_MIN:
                                for (int v = 0; v < veclen; v++) {
                                    out[off + v] = min(tmp);
                                }
                                break;
                        }
                    }
                }
                break;
        }
        return out;
    }


    private byte max(byte[] in) {
        if(in == null || in.length < 1)
            return 0;

        int max = (int)(in[0]&0xff);
        int tmp;
        for (int i = 1; i < in.length; i++) {
            tmp = (int)(in[i]&0xff);
            if(tmp > max) max = tmp;
        }
        return (byte)max;
    }

    private int max(int[] in) {
        if(in == null || in.length < 1)
            return 0;

        int max = in[0];
        for (int i = 1; i < in.length; i++) {
            if(in[i] > max) max = in[i];
        }
        return max;
    }

    private short max(short[] in) {
        if(in == null || in.length < 1)
            return 0;

        short max = in[0];
        for (int i = 1; i < in.length; i++) {
            if(in[i] > max) max = in[i];
        }
        return max;
    }

    private float max(float[] in) {
        if(in == null || in.length < 1)
            return 0;

        float max = in[0];
        for (int i = 1; i < in.length; i++) {
            if(in[i] > max) max = in[i];
        }
        return max;
    }

    private double max(double[] in) {
        if(in == null || in.length < 1)
            return 0;

        double max = in[0];
        for (int i = 1; i < in.length; i++) {
            if(in[i] > max) max = in[i];
        }
        return max;
    }


    private byte min(byte[] in) {
        if(in == null || in.length < 1)
            return 0;

        int min = (int)(in[0]&0xff);
        int tmp;
        for (int i = 1; i < in.length; i++) {
            tmp = (int)(in[i]&0xff);
            if(tmp < min) min = tmp;
        }
        return (byte)min;
    }

    private int min(int[] in) {
        if(in == null || in.length < 1)
            return 0;

        int min = in[0];
        for (int i = 1; i < in.length; i++) {
            if(in[i] < min) min = in[i];
        }
        return min;
    }

    private short min(short[] in) {
        if(in == null || in.length < 1)
            return 0;

        short min = in[0];
        for (int i = 1; i < in.length; i++) {
            if(in[i] < min) min = in[i];
        }
        return min;
    }

    private float min(float[] in) {
        if(in == null || in.length < 1)
            return 0;

        float min = in[0];
        for (int i = 1; i < in.length; i++) {
            if(in[i] < min) min = in[i];
        }
        return min;
    }

    private double min(double[] in) {
        if(in == null || in.length < 1)
            return 0;

        double min = in[0];
        for (int i = 1; i < in.length; i++) {
            if(in[i] < min) min = in[i];
        }
        return min;
    }


    private int sum(byte[] in) {
        if(in == null || in.length < 1)
            return 0;

        int sum = 0;
        for (int i = 1; i < in.length; i++) {
            sum += (int)(in[i]&0xff);
        }
        return sum;
    }

    private int sum(int[] in) {
        if(in == null || in.length < 1)
            return 0;

        int sum = 0;
        for (int i = 1; i < in.length; i++) {
            sum += in[i];
        }
        return sum;
    }

    private short sum(short[] in) {
        if(in == null || in.length < 1)
            return 0;

        short sum = 0;
        for (int i = 1; i < in.length; i++) {
            sum += in[i];
        }
        return sum;
    }

    private float sum(float[] in) {
        if(in == null || in.length < 1)
            return 0;

        float sum = 0;
        for (int i = 1; i < in.length; i++) {
            sum += in[i];
        }
        return sum;
    }

    private double sum(double[] in) {
        if(in == null || in.length < 1)
            return 0;

        double sum = 0;
        for (int i = 1; i < in.length; i++) {
            sum += in[i];
        }
        return sum;
    }


    private byte mean(byte[] in) {
        if(in == null || in.length < 1)
            return 0;

        int sum = sum(in);
        int mean = (int)Math.round((float)sum/(float)in.length);
        return (byte)mean;
    }

    private int mean(int[] in) {
        if(in == null || in.length < 1)
            return 0;

        int sum = sum(in);
        return (int)Math.round((float)sum/(float)in.length);
    }

    private short mean(short[] in) {
        if(in == null || in.length < 1)
            return 0;

        short sum = sum(in);
        return (short)Math.round((float)sum/(float)in.length);
    }

    private float mean(float[] in) {
        if(in == null || in.length < 1)
            return 0;

        float sum = sum(in);
        return (sum/(float)in.length);
    }

    private double mean(double[] in) {
        if(in == null || in.length < 1)
            return 0;

        double sum = sum(in);
        return (sum/(double)in.length);
    }




}
