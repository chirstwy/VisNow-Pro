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

/**
 *
 * @author Bartosz Borucki (babor@icm.edu.pl)
 * University of Warsaw, Interdisciplinary Centre
 * for Mathematical and Computational Modelling
 */
public class GaussianPyramid {
    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GaussianPyramid.class);

    private float[] getDefaultKernel() {
        float[] out = new float[5];
        out[0] = 1.0f/16.0f;
        out[1] = 4.0f/16.0f;
        out[2] = 6.0f/16.0f;
        out[3] = 4.0f/16.0f;
        out[4] = 1.0f/16.0f;
        return out;
    }

    public DataStruct reduce(float[] inData, int[] inDims) {
        return reduce(inData, inDims, getDefaultKernel());
    }

    private DataStruct reduce(float[] inData, int[] inDims, float[] kernel) {
        if(inData == null || inDims == null || kernel == null) {
            log.debug("returning null - inData or inDims or kernel is null");
            return null;
        }

        if(kernel.length%2 == 0) {
            throw new UnsupportedOperationException("Kernel size must be odd number");
        }

        if(inDims.length == 2) {
            if(inDims[0]%2 != 0 || inDims[1]%2 != 0)
                throw new UnsupportedOperationException("Data dims must be even numbers");
            return reduce2D(inData, inDims[0], inDims[1], kernel);
        } else if(inDims.length == 3) {
            if(inDims[0]%2 != 0 || inDims[1]%2 != 0 || inDims[2]%2 != 0)
                throw new UnsupportedOperationException("Data dims must be even numbers");
            return reduce3D(inData, inDims[0], inDims[1], inDims[2], kernel);
        } else return null;
    }

    public DataStruct expand(float[] inData, int[] inDims) {
        return expand(inData, inDims, getDefaultKernel());
    }

    private DataStruct expand(float[] inData, int[] inDims, float[] kernel) {
         if(inData == null || inDims == null || kernel == null)
            return null;

        if(kernel.length%2 == 0) {
            throw new UnsupportedOperationException("Kernel size must be odd number");
        }

        if(inDims.length == 2) {
            if(inDims[0]%2 != 0 || inDims[1]%2 != 0)
                throw new UnsupportedOperationException("Data dims must be even numbers");
            return expand2D(inData, inDims[0], inDims[1], kernel);
        } else if(inDims.length == 3) {
            if(inDims[0]%2 != 0 || inDims[1]%2 != 0 || inDims[2]%2 != 0)
                throw new UnsupportedOperationException("Data dims must be even numbers");
            return expand3D(inData, inDims[0], inDims[1], inDims[2], kernel);
        } else return null;
    }

    private DataStruct reduce2D(float[] data, int w, int h, float[] kernel) {
        if(data.length != w*h)
            return null;

        int r = kernel.length/2;

        int neww = w/2;
        int newh = h/2;
        int[] dims = new int[2];
        dims[0] = neww;
        dims[1] = newh;

        // Rf(i,j) = sum sum w(m,n)*f(2i+m, 2j+n)
        float[] out = new float[neww*newh];
        int c, x, y;
        for (int j = 0; j < newh; j++) {
            for (int i = 0; i < neww; i++) {
                c = j*neww + i;
                out[c] = 0;
                for (int m = -r; m < r+1; m++) {
                    x = 2*i+m;
                    if(x < 0) x = 0;
                    if(x >= w) x = w-1;
                    for (int n = -r; n < r+1; n++) {
                        y = 2*j+n;
                        if(y < 0) y = 0;
                        if(y >= h) y = h-1;
                        out[c] += kernel[m+r]*kernel[n+r]*data[y*w + x];
                    }
                }                                
            }            
        }

        int[] outDims = new int[2];
        outDims[0] = neww;
        outDims[1] = newh;

        return new DataStruct(out, outDims);
    }

    private DataStruct reduce3D(float[] data, int w, int h, int d, float[] kernel) {
        log.debug("call to reduce3D");
        log.debug("  - data.length = "+data.length);
        log.debug("  - w = "+w);
        log.debug("  - h = "+h);
        log.debug("  - d = "+d);

        if(data.length != w*h*d) {
            log.debug("return ning null - data size wrong");
            return null;
        }


        int neww = w/2;
        int newh = h/2;
        int newd = d/2;
        int[] oldDims = new int[3];
        oldDims[0] = w;
        oldDims[1] = h;
        oldDims[2] = d;

        int[] outDims = new int[3];
        outDims[0] = neww;
        outDims[1] = newh;
        outDims[2] = newd;

        float[] out = new float[neww*newh*newd];

        int nThreads = pl.edu.icm.visnow.system.main.VisNow.availableProcessors();
        Reduce3DWorker[] workers = new Reduce3DWorker[nThreads];
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new Reduce3DWorker(i, nThreads, oldDims, outDims, data, kernel, out);
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

        return new DataStruct(out, outDims);
    }

    private class Reduce3DWorker extends Thread {
        private int iThread;
        private int nThreads;
        private int w,h,d,neww,newh,newd;
        private float[] data;
        private float[] out;
        private float[] kernel;

        public Reduce3DWorker(int iThread, int nThreads, int[] oldDims, int[] newDims, float[] data, float[] kernel, float[] out) {
            this.iThread = iThread;
            this.nThreads = nThreads;
            this.w = oldDims[0];
            this.h = oldDims[1];
            this.d = oldDims[2];
            this.neww = newDims[0];
            this.newh = newDims[1];
            this.newd = newDims[2];
            this.data = data;
            this.out = out;
            this.kernel = kernel;
        }

        @Override
        public void run() {
            log.debug("started reduce worker "+(iThread+1)+" of "+nThreads);

            int r = kernel.length/2;
            int c, x, y, z;
            for (int k = iThread; k < newd; k+=nThreads) {
                for (int j = 0; j < newh; j++) {
                    for (int i = 0; i < neww; i++) {
                        c = k*neww*newh + j*neww + i;
                        out[c] = 0;
                        for (int m = -r; m < r+1; m++) {
                            x = 2*i+m;
                            if(x < 0) x = 0;
                            if(x >= w) x = w-1;
                            for (int n = -r; n < r+1; n++) {
                                y = 2*j+n;
                                if(y < 0) y = 0;
                                if(y >= h) y = h-1;
                                for (int o = -r; o < r+1; o++) {
                                    z = 2*k+o;
                                    if(z < 0) z = 0;
                                    if(z >= d) z = d-1;
                                    out[c] += kernel[m+r]*kernel[n+r]*kernel[o+r]*data[z*w*h + y*w + x];
                                }
                            }
                        }
                    }
                }
            }
            log.debug("finished reduce worker "+(iThread+1)+" of "+nThreads);
        }


    }


    private DataStruct expand2D(float[] data, int w, int h, float[] kernel) {
        if(data.length != w*h)
            return null;

        int r = kernel.length/2;

        int neww = 2*w;
        int newh = 2*h;
        int[] dims = new int[2];
        dims[0] = neww;
        dims[1] = newh;

        // Ef(i,j) = 4 sum sum w(m,n)*f((i+m)/2,(j+n)/2) dla (i+m)/2... calkowitych
        float[] out = new float[neww*newh];
        int c;
        float x, y;
        for (int j = 0; j < newh; j++) {
            for (int i = 0; i < neww; i++) {
                c = j*neww + i;
                out[c] = 0;
                for (int m = -r; m < r+1; m++) {
                    x = (float)(i-m)/2.0f;
                    if(x != Math.floor(x))
                        continue;
                    if(x < 0) x = 0;
                    if(x >= w) x = w-1;

                    for (int n = -r; n < r+1; n++) {
                        y = (float)(j-n)/2.0f;
                        if(y != Math.floor(y))
                            continue;
                        if(y < 0) y = 0;
                        if(y >= h) y = h-1;

                        out[c] += kernel[m+r]*kernel[n+r]*data[(int)y*w + (int)x];
                    }
                }
                out[c] = 4*out[c];
            }
        }

        int[] outDims = new int[2];
        outDims[0] = neww;
        outDims[1] = newh;

        return new DataStruct(out, outDims);
    }

    private DataStruct expand3D(float[] data, int w, int h, int d, float[] kernel) {
        log.debug("call to expand3D");
        log.debug("  - data.length = "+data.length);
        log.debug("  - w = "+w);
        log.debug("  - h = "+h);
        log.debug("  - d = "+d);

        if(data.length != w*h*d) {
            log.debug("return ning null - data size wrong");
            return null;
        }

        int neww = 2*w;
        int newh = 2*h;
        int newd = 2*d;
        int[] oldDims = new int[3];
        oldDims[0] = w;
        oldDims[1] = h;
        oldDims[2] = d;

        int[] outDims = new int[3];
        outDims[0] = neww;
        outDims[1] = newh;
        outDims[2] = newd;

        // Ef(i,j,k) = 4 sum sum w(m,n,p)*f((i+m)/2,(j+n)/2,(k+p)/2) dla (i+m)/2... calkowitych
        float[] out = new float[neww*newh*newd];

        int nThreads = pl.edu.icm.visnow.system.main.VisNow.availableProcessors();
        Expand3DWorker[] workers = new Expand3DWorker[nThreads];
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new Expand3DWorker(i, nThreads, oldDims, outDims, data, kernel, out);
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

        return new DataStruct(out, outDims);
    }


    private class Expand3DWorker extends Thread {
        private int iThread;
        private int nThreads;
        private int w,h,d,neww,newh,newd;
        private float[] data;
        private float[] out;
        private float[] kernel;

        public Expand3DWorker(int iThread, int nThreads, int[] oldDims, int[] newDims, float[] data, float[] kernel, float[] out) {
            this.iThread = iThread;
            this.nThreads = nThreads;
            this.w = oldDims[0];
            this.h = oldDims[1];
            this.d = oldDims[2];
            this.neww = newDims[0];
            this.newh = newDims[1];
            this.newd = newDims[2];
            this.data = data;
            this.out = out;
            this.kernel = kernel;
        }

        @Override
        public void run() {
            log.debug("started expand worker "+(iThread+1)+" of "+nThreads);

            int c;
            float x, y, z;
            int r = kernel.length/2;

            for (int k = iThread; k < newd; k+=nThreads) {
                for (int j = 0; j < newh; j++) {
                    for (int i = 0; i < neww; i++) {
                        c = k*neww*newh + j*neww + i;
                        out[c] = 0;
                        for (int m = -r; m < r+1; m++) {
                            x = (float)(i-m)/2.0f;
                            //if(x != Math.floor(x))
                            if(x != (int)x) continue;
                            if(x < 0) x = 0;
                            if(x >= w) x = w-1;

                            for (int n = -r; n < r+1; n++) {
                                y = (float)(j-n)/2.0f;
                                //if(y != Math.floor(y))
                                if(y != (int)y) continue;
                                if(y < 0) y = 0;
                                if(y >= h) y = h-1;

                                for (int p = -r; p < r+1; p++) {
                                    z = (float)(k-p)/2.0f;
                                    //if(z != Math.floor(z))
                                    if(z != (int)z) continue;
                                    if(z < 0) z = 0;
                                    if(z >= d) z = d-1;

                                    out[c] += kernel[m+r]*kernel[n+r]*kernel[p+r]*data[(int)z*w*h + (int)y*w + (int)x];
                                }
                            }
                        }
                        out[c] = 8*out[c];
                    }
                }
            }

            log.debug("finished expand worker "+(iThread+1)+" of "+nThreads);
        }


    }

}
