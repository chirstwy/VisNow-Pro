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

import pl.edu.icm.visnow.datasets.dataarrays.ComplexDataArray;
import pl.edu.icm.visnow.datasets.dataarrays.DataArray;
import pl.edu.icm.visnow.datasets.RegularField;
import pl.edu.icm.visnow.gui.events.FloatValueModificationEvent;
import pl.edu.icm.visnow.gui.events.FloatValueModificationListener;

/**
 * @author  Bartosz Borucki (babor@icm.edu.pl)
 * University of Warsaw, Interdisciplinary Centre
 * for Mathematical and Computational Modelling
 */

public class Core {
    private Params params = new Params();
    private RegularField inField = null;
    private RegularField segmentationField = null;
    private RegularField outField = null;

    private float progress = 0.0f;

    public Core() {

    }


    public void update() {
        progress = 0.0f;
        if(inField == null || segmentationField == null) {
            outField = null;
            return;
        }

        int[] fieldDims = inField.getDims();
        int[] segmentationFieldDims = segmentationField.getDims();
        if(fieldDims.length != 3 || segmentationFieldDims.length != 3 || fieldDims[0] != segmentationFieldDims[0] || fieldDims[1] != segmentationFieldDims[1] || fieldDims[2] != segmentationFieldDims[2]) {
            outField = null;
            return;
        }

        String[] map = segmentationField.getData(0).getUserData();
        if(map == null || map.length < 2 || !map[0].equals("MAP")) {
            outField = null;
            return;
        }

        int fieldComponent = params.getSelectedComponent();
        int selectedSegmentation = params.getSelectedSegmentation();
        if(selectedSegmentation + 1 >= map.length) {
            outField = null;
            return;
        }
        String[] tmp = map[selectedSegmentation+1].split(":");
        String segName = tmp[1];
        int segValue = 0;
        try {
            segValue = Integer.parseInt(tmp[0]);
        } catch(NumberFormatException ex) {
            outField = null;
            return;
        }

        outField = new RegularField(fieldDims);
        if(inField.getCoords() != null) {
            outField.setCoords(inField.getCoords());
        } else {
            outField.setAffine(inField.getAffine());
        }

        byte[] inSegmentationData = segmentationField.getData(0).getBData();
        int progressStep = 1;

        switch(inField.getData(fieldComponent).getType()) {
            case DataArray.FIELD_DATA_BYTE:
                byte[] inBData = inField.getData(fieldComponent).getBData();
                byte[] outBData = new byte[inBData.length];
                progressStep = (int)Math.ceil((float)outBData.length/50.0f);
                int bMinv = Integer.MAX_VALUE;
                for (int i = 0; i < outBData.length; i++) {
                    if(inSegmentationData[i] == (byte)segValue) {
                        outBData[i] = inBData[i];
                        if( (int)(inBData[i]&0xFF) < bMinv)
                            bMinv = (int)(inBData[i]&0xFF);
                    }                    
                    if(i%progressStep == 0) {
                        progress = (i+1)*0.5f/(float)outBData.length;
                        fireStatusChanged(progress);
                    }
                }

                for (int i = 0; i < outBData.length; i++) {
                    if(inSegmentationData[i] != (byte)segValue) {
                        outBData[i] = (byte)bMinv;
                    }                    
                    if(i%progressStep == 0) {
                        progress = 0.5f + (i+1)*0.5f/(float)outBData.length;
                        fireStatusChanged(progress);
                    }
                }
                outField.addData(DataArray.create(outBData, inField.getData(fieldComponent).getVeclen(), inField.getData(fieldComponent).getName()+"_"+segName));
                break;
            case DataArray.FIELD_DATA_SHORT:
                short[] inSData = inField.getData(fieldComponent).getSData();
                short[] outSData = new short[inSData.length];
                progressStep = (int)Math.ceil((float)outSData.length/50.0f);
                short sMinv = Short.MAX_VALUE;
                for (int i = 0; i < outSData.length; i++) {
                    if(inSegmentationData[i] == (byte)segValue) {
                        outSData[i] = inSData[i];
                        if(inSData[i] < sMinv) {
                            sMinv = inSData[i];
                        }
                    }                    
                    if(i%progressStep == 0) {
                        progress = (i+1)*0.5f/(float)outSData.length;
                        fireStatusChanged(progress);
                    }
                }
                for (int i = 0; i < outSData.length; i++) {
                    if(inSegmentationData[i] != (byte)segValue) {
                        outSData[i] = (short)sMinv;
                    }                    
                    if(i%progressStep == 0) {
                        progress = 0.5f + (i+1)*0.5f/(float)outSData.length;
                        fireStatusChanged(progress);
                    }
                }
                outField.addData(DataArray.create(outSData, inField.getData(fieldComponent).getVeclen(), inField.getData(fieldComponent).getName()+"_"+segName));
                break;
            case DataArray.FIELD_DATA_INT:
                int[] inIData = inField.getData(fieldComponent).getIData();
                int[] outIData = new int[inIData.length];
                progressStep = (int)Math.ceil((float)outIData.length/50.0f);
                int iMinv = Integer.MAX_VALUE;
                for (int i = 0; i < outIData.length; i++) {
                    if(inSegmentationData[i] == (byte)segValue) {
                        outIData[i] = inIData[i];
                        if(inIData[i] < iMinv) {
                            iMinv = inIData[i];
                        }
                    }                    
                    if(i%progressStep == 0) {
                        progress = (i+1)*0.5f/(float)outIData.length;
                        fireStatusChanged(progress);
                    }
                }
                for (int i = 0; i < outIData.length; i++) {
                    if(inSegmentationData[i] != (byte)segValue) {
                        outIData[i] = iMinv;
                    }                    
                    if(i%progressStep == 0) {
                        progress = 0.5f + (i+1)*0.5f/(float)outIData.length;
                        fireStatusChanged(progress);
                    }
                }
                outField.addData(DataArray.create(outIData, inField.getData(fieldComponent).getVeclen(), inField.getData(fieldComponent).getName()+"_"+segName));
                break;
            case DataArray.FIELD_DATA_FLOAT:
                float[] inFData = inField.getData(fieldComponent).getFData();
                float[] outFData = new float[inFData.length];
                progressStep = (int)Math.ceil((float)outFData.length/50.0f);
                float fMinv = Float.POSITIVE_INFINITY;
                for (int i = 0; i < outFData.length; i++) {
                    if(inSegmentationData[i] == (byte)segValue) {
                        outFData[i] = inFData[i];
                        if(inFData[i] < fMinv)
                            fMinv = inFData[i];
                    }                    
                    if(i%progressStep == 0) {
                        progress = (i+1)*0.5f/(float)outFData.length;
                        fireStatusChanged(progress);
                    }
                }
                for (int i = 0; i < outFData.length; i++) {
                    if(inSegmentationData[i] != (byte)segValue) {
                        outFData[i] = fMinv;
                    }                    
                    if(i%progressStep == 0) {
                        progress = 0.5f + (i+1)*0.5f/(float)outFData.length;
                        fireStatusChanged(progress);
                    }
                }
                outField.addData(DataArray.create(outFData, inField.getData(fieldComponent).getVeclen(), inField.getData(fieldComponent).getName()+"_"+segName));
                break;
            case DataArray.FIELD_DATA_DOUBLE:
                double[] inDData = inField.getData(fieldComponent).getDData();
                double[] outDData = new double[inDData.length];
                progressStep = (int)Math.ceil((float)outDData.length/50.0f);
                double dMinv = Double.POSITIVE_INFINITY;
                for (int i = 0; i < outDData.length; i++) {
                    if(inSegmentationData[i] == (byte)segValue) {
                        outDData[i] = inDData[i];
                        if(inDData[i] < dMinv)
                            dMinv = inDData[i];
                    }                    
                    if(i%progressStep == 0) {
                        progress = (i+1)*0.5f/(float)outDData.length;
                        fireStatusChanged(progress);
                    }
                }
                for (int i = 0; i < outDData.length; i++) {
                    if(inSegmentationData[i] != (byte)segValue) {
                        outDData[i] = dMinv;
                    }                    
                    if(i%progressStep == 0) {
                        progress = 0.5f + (i+1)*0.5f/(float)outDData.length;
                        fireStatusChanged(progress);
                    }
                }
                outField.addData(DataArray.create(outDData, inField.getData(fieldComponent).getVeclen(), inField.getData(fieldComponent).getName()+"_"+segName));
                break;
            case DataArray.FIELD_DATA_COMPLEX:
                float[] inFRealData = ((ComplexDataArray)inField.getData(fieldComponent)).getFRealData();
                float[] inFImagData = ((ComplexDataArray)inField.getData(fieldComponent)).getFImagData();
                float[] outFRealData = new float[inFRealData.length];
                float[] outFImagData = new float[inFImagData.length];
                progressStep = (int)Math.ceil((float)outFRealData.length/50.0f);
                for (int i = 0; i < outFRealData.length; i++) {
                    if(inSegmentationData[i] == (byte)segValue) {
                        outFRealData[i] = inFRealData[i];
                        outFImagData[i] = inFImagData[i];
                    } else {
                        outFRealData[i] = 0;
                        outFImagData[i] = 0;
                    }
                    if(i%progressStep == 0) {
                        progress = (float)(i+1)/(float)outFRealData.length;
                        fireStatusChanged(progress);
                    }
                }
                outField.addData(DataArray.create(outFRealData, outFImagData, inField.getData(fieldComponent).getVeclen(), inField.getData(fieldComponent).getName()+"_"+segName));
                break;
        }
        progress = 1.0f;
        fireStatusChanged(progress);
    }


    /**
     * @param inField the inField to set
     */
    public void setInField(RegularField inField) {
        this.inField = inField;
    }

    /**
     * @param segmentationField the segmentationField to set
     */
    public void setSegmentationField(RegularField segmentationField) {
        this.segmentationField = segmentationField;
    }

    /**
     * @return the outField
     */
    public RegularField getOutField() {
        return outField;
    }

    /**
     * @param params the params to set
     */
    public void setParams(Params params) {
        this.params = params;
    }


    private transient FloatValueModificationListener statusListener = null;

    public void addFloatValueModificationListener(FloatValueModificationListener listener) {
        if (statusListener == null) {
            this.statusListener = listener;
        } else {
            System.out.println("" + this + ": only one status listener can be added");
        }
    }

    private void fireStatusChanged(float status) {
        FloatValueModificationEvent e = new FloatValueModificationEvent(this, status, true);
        if (statusListener != null) {
            statusListener.floatValueChanged(e);
        }
    }



}
