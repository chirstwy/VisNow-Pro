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

package pl.edu.icm.visnow.lib.basic.filters.IntensityEqualizer;

import pl.edu.icm.visnow.datasets.dataarrays.DataArray;
import pl.edu.icm.visnow.datasets.RegularField;
import pl.edu.icm.visnow.gui.events.FloatValueModificationEvent;
import pl.edu.icm.visnow.gui.events.FloatValueModificationListener;


/**
 * @author  Bartosz Borucki (babor@icm.edu.pl)
 * Warsaw University, Interdisciplinary Centre
 * for Mathematical and Computational Modelling
 */
public class Core {
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(Core.class);


    private float progress = 0.0f;
    private Params params = new Params();
    private RegularField inField = null;
    private RegularField outField = null;

    private float[][] levels = null;
    private float mean = 0;
    private int[] outDims = null;

    private GaussianPyramid gp = new GaussianPyramid();

    //DataStruct test = null;

    public Core() {
        
    }

    public void setParams(Params params) {
        this.params = params;
    }
    
    public void setInField(RegularField inField) {
        this.inField = preprocess(inField);
    }

    public RegularField getPreprocessedInField() {
        return inField;
    }

    public RegularField getOutField() {
        return outField;
    }

    void updatePyramid() {
        if(params == null || inField == null) {
            outField = null;
            return;
        }

        if(inField.getDims().length < 2 || inField.getDims().length > 3) {
            outField = null;
            return;            
        }

        progress = 0.0f;
        fireStatusChanged(progress);

        int N = params.getNLevels();
        log.debug("calculating "+N+" levels");
        float[] weights = params.getWeights();
        if(weights == null) {
            return;
        }

        levels = new float[N][];
        int comp = 0; //params.getComponent();
        //DataStruct G0 = prepareData(inField, comp);
        DataStruct G0 = new DataStruct(inField.getData(comp).getFData(), inField.getDims());
        if(G0 == null)
            return;
        
        outDims = G0.getDims();

        DataStruct G = G0;
        DataStruct G1 = gp.reduce(G0.getData(), G0.getDims());


        progress = 1.0f/(float)N;
        fireStatusChanged(progress);

        for (int i = 0; i < N-1; i++) {
            DataStruct tmpG = substract(G, gp.expand(G1.getData(), G1.getDims()));
            for (int j = 0; j < i; j++) {
                tmpG = gp.expand(tmpG.getData(), tmpG.getDims());
            }
            levels[i] = tmpG.getData();

            G = G1;

            if(i == N-2)
                break;
            G1 = gp.reduce(G1.getData(), G1.getDims());
            tmpG = null;
            progress = (float)(i+1)/(float)N;
            fireStatusChanged(progress);
        }

        DataStruct tmpG = substractMean(G);
        //DataStruct tmpG = G;
        for (int j = 0; j < N-1; j++) {
            tmpG = gp.expand(tmpG.getData(), tmpG.getDims());
        }
        levels[N-1] = tmpG.getData();
        progress = 1.0f;
        fireStatusChanged(progress);
    }


    void updateOutput() {
        if(params == null || inField == null || levels == null) {
            outField = null;
            return;
        }

        //progress = 0.0f;
        //fireStatusChanged(progress);

        int comp = 0; //params.getComponent();

        int N = outDims[0]*outDims[1];
        if(outDims.length == 3) {
            N = N * outDims[2];
        }
        float[] out = new float[N];
        float[] weights = params.getWeights();
        float gain = params.getGain();

        for (int i = 0; i < N; i++) {
            out[i] = mean;
        }

        for (int j = 0; j < weights.length; j++) {
            for (int i = 0; i < N; i++) {
                out[i] += gain*weights[j]*levels[j][i];
            }
            progress = 0.5f * (float)(j+1)/(float)(weights.length);
            fireStatusChanged(progress);
        }

        progress = 0.5f;
        fireStatusChanged(progress);

        outField = new RegularField(outDims);
        outField.setAffine(inField.getAffine());

        //normalizacja do zakresu wejscia
        float inMax = inField.getData(comp).getMaxv();
        float inMin = inField.getData(comp).getMinv();

        switch(inField.getData(comp).getType()) {
            case DataArray.FIELD_DATA_BYTE:
                byte[] outB = new byte[N];
                for (int i = 0; i < N; i++) {
                    if(out[i] < inMin)
                        outB[i] = (byte)inMin;
                    else if(out[i] > inMax)
                        outB[i] = (byte)inMax;
                    else
                        outB[i] = (byte)out[i];

                    if(i % 1000 == 0) {
                        progress = 0.5f + 0.5f * (float)(i+1)/(float)(N);
                        fireStatusChanged(progress);
                    }
                }
                outField.addData(DataArray.create(outB, 1, inField.getData(comp).getName()+"_eq"));
                break;
            case DataArray.FIELD_DATA_SHORT:
                short[] outS = new short[N];
                for (int i = 0; i < N; i++) {
                    if(out[i] < inMin)
                        outS[i] = (short)inMin;
                    else if(out[i] > inMax)
                        outS[i] = (short)inMax;
                    else
                        outS[i] = (short)out[i];

                    if(i % 1000 == 0) {
                        progress = 0.5f + 0.5f * (float)(i+1)/(float)(N);
                        fireStatusChanged(progress);
                    }
                }
                outField.addData(DataArray.create(outS, 1, inField.getData(comp).getName()+"_eq"));
                break;
            case DataArray.FIELD_DATA_INT:
                int[] outI = new int[N];
                for (int i = 0; i < N; i++) {
                    if(out[i] < inMin)
                        outI[i] = (int)inMin;
                    else if(out[i] > inMax)
                        outI[i] = (int)inMax;
                    else
                        outI[i] = (int)out[i];

                    if(i % 1000 == 0) {
                        progress = 0.5f + 0.5f * (float)(i+1)/(float)(N);
                        fireStatusChanged(progress);
                    }
                }
                outField.addData(DataArray.create(outI, 1, inField.getData(comp).getName()+"_eq"));
                break;
            default:
                float[] outF = new float[N];
                for (int i = 0; i < N; i++) {
                    if(out[i] < inMin)
                        outF[i] = inMin;
                    else if(out[i] > inMax)
                        outF[i] = inMax;
                    else
                        outF[i] = out[i];

                    if(i % 1000 == 0) {
                        progress = 0.5f + 0.5f * (float)(i+1)/(float)(N);
                        fireStatusChanged(progress);
                    }
                }
                outField.addData(DataArray.create(outF, 1, inField.getData(comp).getName()+"_eq"));
                break;
        }


        for (int i = 0; i < weights.length; i++) {
            outField.addData(DataArray.create(levels[i], 1, "level"+i));
        }
        //outField.addData(DataArray.create(out, 1, inField.getData(comp).getName()));
        progress = 1.0f;
        fireStatusChanged(progress);
    }
  
    private DataStruct substract(DataStruct G, DataStruct G1) {
        if(G == null || G1 == null)
            return null;

        int[] dims = G.getDims();
        int[] dims1 = G1.getDims();
        if(dims.length != dims1.length)
            return null;

        for (int i = 0; i < dims1.length; i++) {
            if(dims[i] != dims1[i])
                return null;
        }

        float[] in = G.getData();
        float[] in1 = G1.getData();
        float[] out = null;
        if(dims.length == 2) {
            out = new float[dims[0]*dims[1]];
        } else if(dims.length == 3) {
            out = new float[dims[0]*dims[1]*dims[2]];
        }
        
        int nThreads = pl.edu.icm.visnow.system.main.VisNow.availableProcessors();
        SubstractWorker[] workers = new SubstractWorker[nThreads];
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new SubstractWorker(i, nThreads, in, in1, out);
            workers[i].start();
        }

        for (int i = 0; i < workers.length; i++) {
            try {
                workers[i].join();
                workers[i] = null;
            } catch (InterruptedException ex) {
                return null;
            }
        }

//        for (int i = 0; i < out.length; i++) {
//            out[i] = in[i] - in1[i];
//        }


        return new DataStruct(out, dims);
    }

    private class SubstractWorker extends Thread {
        private int iThread;
        private int nThreads;
        private float[] data1;
        private float[] data2;
        private float[] out;

        public SubstractWorker(int iThread, int nThreads, float[] data1, float[] data2, float[] out) {
            this.iThread = iThread;
            this.nThreads = nThreads;
            this.data1 = data1;
            this.data2 = data2;
            this.out = out;
        }

        @Override
        public void run() {
            log.debug("started substract worker "+(iThread+1)+" of "+nThreads);

            if(data1 == null || data2 == null || out == null)
                return;

            for (int i = iThread; i < out.length; i+=nThreads) {
                out[i] = data1[i] - data2[i];
            }

            log.debug("finished substract worker "+(iThread+1)+" of "+nThreads);
        }
    }


    private DataStruct substractMean(DataStruct G) {
       if(G == null)
            return null;

        int[] dims = G.getDims();
        float[] in = G.getData();
        float[] out = null;
        if(dims.length == 2) {
            out = new float[dims[0]*dims[1]];
        } else if(dims.length == 3) {
            out = new float[dims[0]*dims[1]*dims[2]];
        }

        mean = 0;
        for (int i = 0; i < out.length; i++) {
            mean += in[i];
        }
        mean = mean/(float)out.length;
        for (int i = 0; i < out.length; i++) {
            out[i] = in[i] - mean;
        }
        return new DataStruct(out, dims);
    }

    private RegularField preprocess(RegularField inField) {
        RegularField out = null;
        if(inField == null)
            return null;

        int[] inDims = inField.getDims();
        int[] outDimsL = new int[inDims.length];
        int[] offsets = {0,0,0};
        int N = 1;

        int max = 0;
        for (int i = 0; i < outDimsL.length; i++) {
             outDimsL[i] = getNearestGreater2Pow(inDims[i]);
             if(outDimsL[i] > max) max = outDimsL[i];
        }

        if(params.isFullPadding()) {
            for (int i = 0; i < outDimsL.length; i++) {
                 outDimsL[i] = max;
            }
        }

        for (int i = 0; i < outDimsL.length; i++) {
             offsets[i] = (outDimsL[i]-inDims[i])/2;
             log.debug("old dims["+i+"]="+inDims[i]);
             log.debug("new dims["+i+"]="+outDimsL[i]);
             log.debug("offsets["+i+"]="+offsets[i]);
             N = N*outDimsL[i];
        }




        out = new RegularField(outDimsL);

        int comp = 0;//params.getComponent();
        int ii,jj,kk;
        switch(inField.getData(comp).getType()) {
            case DataArray.FIELD_DATA_BYTE:
                byte[] bData = inField.getData(comp).getBData();
                byte[] bOut = new byte[N];
                if(inDims.length == 2) {
                    for (int j = 0; j < outDimsL[1]; j++) {
                        jj = j-offsets[1];
                        for (int i = 0; i < outDimsL[0]; i++) {
                            ii = i - offsets[0];
                            if(ii < 0 || jj < 0 || ii >= inDims[0] || jj >= inDims[1])
                                bOut[j*outDimsL[0] + i] = 0;
                            else
                                bOut[j*outDimsL[0] + i] = bData[jj*inDims[0] + ii];
                        }
                    }
                } else if(inDims.length == 3) {
                    for (int k = 0; k < outDimsL[2]; k++) {
                        kk = k - offsets[2];
                        for (int j = 0; j < outDimsL[1]; j++) {
                            jj = j-offsets[1];
                            for (int i = 0; i < outDimsL[0]; i++) {
                                ii = i - offsets[0];
                                if(ii < 0 || jj < 0 || kk < 0 || ii >= inDims[0] || jj >= inDims[1] || kk >= inDims[2])
                                    bOut[k*outDimsL[0]*outDimsL[1] + j*outDimsL[0] + i] = 0;
                                else
                                    bOut[k*outDimsL[0]*outDimsL[1] + j*outDimsL[0] + i] = bData[kk*inDims[0]*inDims[1] + jj*inDims[0] + ii];
                            }
                        }
                    }
                }
                out.addData(DataArray.create(bOut, 1, inField.getData(comp).getName()));
                break;
            case DataArray.FIELD_DATA_SHORT:
                short[] sData = inField.getData(comp).getSData();
                short[] sOut = new short[N];
                if(inDims.length == 2) {
                    for (int j = 0; j < outDimsL[1]; j++) {
                        jj = j-offsets[1];
                        for (int i = 0; i < outDimsL[0]; i++) {
                            ii = i - offsets[0];
                            if(ii < 0 || jj < 0 || ii >= inDims[0] || jj >= inDims[1])
                                sOut[j*outDimsL[0] + i] = 0;
                            else
                                sOut[j*outDimsL[0] + i] = sData[jj*inDims[0] + ii];
                        }
                    }
                } else if(inDims.length == 3) {
                    for (int k = 0; k < outDimsL[2]; k++) {
                        kk = k - offsets[2];
                        for (int j = 0; j < outDimsL[1]; j++) {
                            jj = j-offsets[1];
                            for (int i = 0; i < outDimsL[0]; i++) {
                                ii = i - offsets[0];
                                if(ii < 0 || jj < 0 || kk < 0 || ii >= inDims[0] || jj >= inDims[1] || kk >= inDims[2])
                                    sOut[k*outDimsL[0]*outDimsL[1] + j*outDimsL[0] + i] = 0;
                                else
                                    sOut[k*outDimsL[0]*outDimsL[1] + j*outDimsL[0] + i] = sData[kk*inDims[0]*inDims[1] + jj*inDims[0] + ii];
                            }
                        }
                    }
                }
                out.addData(DataArray.create(sOut, 1, inField.getData(comp).getName()));
                break;
            case DataArray.FIELD_DATA_INT:
                int[] iData = inField.getData(comp).getIData();
                int[] iOut = new int[N];
                if(inDims.length == 2) {
                    for (int j = 0; j < outDimsL[1]; j++) {
                        jj = j-offsets[1];
                        for (int i = 0; i < outDimsL[0]; i++) {
                            ii = i - offsets[0];
                            if(ii < 0 || jj < 0 || ii >= inDims[0] || jj >= inDims[1])
                                iOut[j*outDimsL[0] + i] = 0;
                            else
                                iOut[j*outDimsL[0] + i] = iData[jj*inDims[0] + ii];
                        }
                    }
                } else if(inDims.length == 3) {
                    for (int k = 0; k < outDimsL[2]; k++) {
                        kk = k - offsets[2];
                        for (int j = 0; j < outDimsL[1]; j++) {
                            jj = j-offsets[1];
                            for (int i = 0; i < outDimsL[0]; i++) {
                                ii = i - offsets[0];
                                if(ii < 0 || jj < 0 || kk < 0 || ii >= inDims[0] || jj >= inDims[1] || kk >= inDims[2])
                                    iOut[k*outDimsL[0]*outDimsL[1] + j*outDimsL[0] + i] = 0;
                                else
                                    iOut[k*outDimsL[0]*outDimsL[1] + j*outDimsL[0] + i] = iData[kk*inDims[0]*inDims[1] + jj*inDims[0] + ii];
                            }
                        }
                    }
                }
                out.addData(DataArray.create(iOut, 1, inField.getData(comp).getName()));
                break;
            default:
                float[] fData = inField.getData(comp).getFData();
                float[] fOut = new float[N];
                if(inDims.length == 2) {
                    for (int j = 0; j < outDimsL[1]; j++) {
                        jj = j-offsets[1];
                        for (int i = 0; i < outDimsL[0]; i++) {
                            ii = i - offsets[0];
                            if(ii < 0 || jj < 0 || ii >= inDims[0] || jj >= inDims[1])
                                fOut[j*outDimsL[0] + i] = 0;
                            else
                                fOut[j*outDimsL[0] + i] = fData[jj*inDims[0] + ii];
                        }
                    }
                } else if(inDims.length == 3) {
                    for (int k = 0; k < outDimsL[2]; k++) {
                        kk = k - offsets[2];
                        for (int j = 0; j < outDimsL[1]; j++) {
                            jj = j-offsets[1];
                            for (int i = 0; i < outDimsL[0]; i++) {
                                ii = i - offsets[0];
                                if(ii < 0 || jj < 0 || kk < 0 || ii >= inDims[0] || jj >= inDims[1] || kk >= inDims[2])
                                    fOut[k*outDimsL[0]*outDimsL[1] + j*outDimsL[0] + i] = 0;
                                else
                                    fOut[k*outDimsL[0]*outDimsL[1] + j*outDimsL[0] + i] = fData[kk*inDims[0]*inDims[1] + jj*inDims[0] + ii];
                            }
                        }
                    }
                }
                out.addData(DataArray.create(fOut, 1, inField.getData(comp).getName()));
                break;

        }

        float[][] inAffine = inField.getAffine();
        float[][] outAffine = new float[4][];
        outAffine[0] = inAffine[0];
        outAffine[1] = inAffine[1];
        outAffine[2] = inAffine[2];
        outAffine[3] = new float[3];
        if(inDims.length == 2)
            for (int i = 0; i < 3; i++) {
                outAffine[3][i] = inAffine[3][i] - offsets[0]*inAffine[0][i] - offsets[1]*inAffine[1][i];
            }
        else if(inDims.length == 3)
            for (int i = 0; i < 3; i++) {
                outAffine[3][i] = inAffine[3][i] - offsets[0]*inAffine[0][i] - offsets[1]*inAffine[1][i] - offsets[2]*inAffine[2][i];
            }
        out.setAffine(outAffine);
        return out;
    }


    private static int getNearestGreater2Pow(int n) {
        if(n<=1)
            return 0;

        int i = 0;
        while(Math.pow(2, i) < n) i++;

        return (int)Math.pow(2,i);
    }

   private transient FloatValueModificationListener statusListener = null;

   public void addFloatValueModificationListener(FloatValueModificationListener listener)
   {
      if (statusListener == null)
         this.statusListener = listener;
      else
         System.out.println(""+this+": only one status listener can be added");
   }

   private void fireStatusChanged(float status)
   {
       FloatValueModificationEvent e = new FloatValueModificationEvent(this, status, true);
       if (statusListener != null)
          statusListener.floatValueChanged(e);
   }

}












