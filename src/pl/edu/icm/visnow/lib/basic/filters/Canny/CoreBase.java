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

package pl.edu.icm.visnow.lib.basic.filters.Canny;

import java.util.Stack;
import pl.edu.icm.visnow.datasets.dataarrays.DataArray;
import pl.edu.icm.visnow.datasets.RegularField;

/**
 *
 * @author Andrzej Rutkowski (rudy@mat.uni.torun.pl)
 * 
 */
abstract public class CoreBase {
    protected int[] dims;
    protected int[] dims2;
    protected int dim01; // dims[0]*dims[1]
    protected int nDims;
    
    protected DataArray dataArray;
    
    protected byte[] out = null; // inicjowane w nonMaxSupp3D, wykozystywane przez follow3
    
    public CoreBase(DataArray data, int[] dims) {
        this.dims = dims;
        this.dataArray = data;
        
        nDims = dims.length;
        dims2 = new int[dims.length];
        for (int k = 0; k < dims.length; ++k)
            dims2[k] = dims[k] - 1;
        if (dims.length > 1)
            dim01 = dims[0] * dims[1];
        else
            dim01 = dims[0];
        initData();
    }
    
    abstract protected void initData();
    
    public byte[] calculate(float thHigh, float thLow) {
        if (nDims == 2) {
            nonMaxSupp2D(thHigh, thLow);
        } else if (nDims == 3) {
            nonMaxSupp3D(thHigh, thLow);
        } else {
            out = new byte[1];
            out[0] = 1;
        }
        return out;
    }
       
    abstract protected float calc3MagSqr(int x, int y, int z);
    abstract protected float[] calc3Grad(int x, int y, int z);
    abstract protected void calcDirs3MagsLayer(int z, float[][] dout, float[] mout);
    abstract protected float[] calc2Grad(int x, int y);
    abstract protected void calcDirs2(float[][] dout, float[] mout);
    
    private void nonMaxSupp3D(float thHigh, float thLow) {
        float[][] magl = new float[3][dims[0]*dims[1]];
        float[][][] dirl = new float[3][3][dims[0]*dims[1]];
        float magmin, magmax;
        out = new byte[dims[0]*dims[1]*dims[2]];
        
        for (int i = 0; i < 2; ++i)
            calcDirs3MagsLayer(i, dirl[i + 1], magl[i + 1]);
        
        magmin = magmax = magl[1][0];
                    
        int w = dims[0];
        int wh = dims[0]*dims[1];
        for (int z = 1; z < dims[2] - 1; ++z) {
            int alayer = z*wh;
            calcDirs3MagsLayer(z + 1, dirl[0], magl[0]);
            { float[][] t = dirl[0]; dirl[0] = dirl[1]; dirl[1] = dirl[2]; dirl[2] = t; }
            { float[]   t = magl[0]; magl[0] = magl[1]; magl[1] = magl[2]; magl[2] = t; }

            for (int y = 1; y < dims[1] - 1; ++y) {
                int a = y*w + 1;
                for (int x = 1; x < w - 1; ++x, ++a) {
                    out[alayer + a] = 0;
                    
                    if (magl[1][a] < 1e-6)
                        continue;
                    
                    if (magl[1][a] < magmin)
                        magmin = magl[1][a];
                    else if (magl[1][a] > magmax)
                        magmax = magl[1][a];
                    
                    float maga, magb, gx1, gx2, gy1, gy2;
                    float dxn = dirl[1][0][a] / magl[1][a];
                    float dyn = dirl[1][1][a] / magl[1][a];
                    float dzn = dirl[1][2][a] / magl[1][a];
                    if (dxn*dyn > 0) {
                        if (dxn*dzn > 0) {
                            /* dx dy dz
                             *  +  +  +
                             *  -  -  - */
                            dxn = Math.abs(dxn);
                            dyn = Math.abs(dyn);
                            dzn = Math.abs(dzn);
                            gx1 = magl[1][a] + dxn*(magl[1][a + 1] - magl[1][a]);
                            gx2 = magl[1][a + w] + dxn*(magl[1][a + w + 1] - magl[1][a + w]);
                            gy1 = gx1 + dyn*(gx2 - gx1);
                            gx1 = magl[2][a] + dxn*(magl[2][a + 1] - magl[2][a]);
                            gx2 = magl[2][a + w] + dxn*(magl[2][a + w + 1] - magl[2][a + w]);
                            gy2 = gx1 + dyn*(gx2 - gx1);
                            maga = gy1 + dzn*(gy2 - gy1);
                            if (maga > magl[1][a])
                                continue;

                            gx1 = magl[1][a] + dxn*(magl[1][a - 1] - magl[1][a]);
                            gx2 = magl[1][a - w] + dxn*(magl[1][a - w - 1] - magl[1][a - w]);
                            gy1 = gx1 + dyn*(gx2 - gx1);
                            gx1 = magl[0][a] + dxn*(magl[0][a - 1] - magl[0][a]);
                            gx2 = magl[0][a - w] + dxn*(magl[0][a - w - 1] - magl[0][a - w]);
                            gy2 = gx1 + dyn*(gx2 - gx1);
                            magb = gy1 + dzn*(gy2 - gy1);
                            if (magb > magl[1][a])
                                continue;
                        } else {
                            /* dx dy dz
                             *  -  -  +
                             *  +  +  - */
                            dxn = Math.abs(dxn);
                            dyn = Math.abs(dyn);
                            dzn = Math.abs(dzn);
                            gx1 = magl[1][a] + dxn*(magl[1][a - 1] - magl[1][a]);
                            gx2 = magl[1][a - w] + dxn*(magl[1][a - w - 1] - magl[1][a - w]);
                            gy1 = gx1 + dyn*(gx2 - gx1);
                            gx1 = magl[2][a] + dxn*(magl[2][a - 1] - magl[2][a]);
                            gx2 = magl[2][a - w] + dxn*(magl[2][a - w - 1] - magl[2][a - w]);
                            gy2 = gx1 + dyn*(gx2 - gx1);
                            maga = gy1 + dzn*(gy2 - gy1);
                            if (maga > magl[1][a])
                                continue;

                            gx1 = magl[1][a] + dxn*(magl[1][a + 1] - magl[1][a]);
                            gx2 = magl[1][a + w] + dxn*(magl[1][a + w + 1] - magl[1][a + w]);
                            gy1 = gx1 + dyn*(gx2 - gx1);
                            gx1 = magl[0][a] + dxn*(magl[0][a + 1] - magl[0][a]);
                            gx2 = magl[0][a + w] + dxn*(magl[0][a + w + 1] - magl[0][a + w]);
                            gy2 = gx1 + dyn*(gx2 - gx1);
                            magb = gy1 + dzn*(gy2 - gy1);
                            if (magb > magl[1][a])
                                continue;
                        }
                    } else {
                        if (dxn*dzn > 0) {
                            /* dx dy dz
                             *  +  -  +
                             *  -  +  - */
                            dxn = Math.abs(dxn);
                            dyn = Math.abs(dyn);
                            dzn = Math.abs(dzn);
                            gx1 = magl[1][a] + dxn*(magl[1][a + 1] - magl[1][a]);
                            gx2 = magl[1][a - w] + dxn*(magl[1][a - w + 1] - magl[1][a - w]);
                            gy1 = gx1 + dyn*(gx2 - gx1);
                            gx1 = magl[2][a] + dxn*(magl[2][a + 1] - magl[2][a]);
                            gx2 = magl[2][a - w] + dxn*(magl[2][a - w + 1] - magl[2][a - w]);
                            gy2 = gx1 + dyn*(gx2 - gx1);
                            maga = gy1 + dzn*(gy2 - gy1);
                            if (maga > magl[1][a])
                                continue;

                            gx1 = magl[1][a] + dxn*(magl[1][a - 1] - magl[1][a]);
                            gx2 = magl[1][a + w] + dxn*(magl[1][a + w - 1] - magl[1][a + w]);
                            gy1 = gx1 + dyn*(gx2 - gx1);
                            gx1 = magl[0][a] + dxn*(magl[0][a - 1] - magl[0][a]);
                            gx2 = magl[0][a + w] + dxn*(magl[0][a + w - 1] - magl[0][a + w]);
                            gy2 = gx1 + dyn*(gx2 - gx1);
                            magb = gy1 + dzn*(gy2 - gy1);
                            if (magb > magl[1][a])
                                continue;
                        } else {
                            /* dx dy dz
                             *  +  -  -
                             *  -  +  + */
                            dxn = Math.abs(dxn);
                            dyn = Math.abs(dyn);
                            dzn = Math.abs(dzn);
                            gx1 = magl[1][a] + dxn*(magl[1][a - 1] - magl[1][a]);
                            gx2 = magl[1][a + w] + dxn*(magl[1][a + w - 1] - magl[1][a + w]);
                            gy1 = gx1 + dyn*(gx2 - gx1);
                            gx1 = magl[2][a] + dxn*(magl[2][a - 1] - magl[2][a]);
                            gx2 = magl[2][a + w] + dxn*(magl[2][a + w - 1] - magl[2][a + w]);
                            gy2 = gx1 + dyn*(gx2 - gx1);
                            maga = gy1 + dzn*(gy2 - gy1);
                            if (maga > magl[1][a])
                                continue;

                            gx1 = magl[1][a] + dxn*(magl[1][a + 1] - magl[1][a]);
                            gx2 = magl[1][a - w] + dxn*(magl[1][a - w + 1] - magl[1][a - w]);
                            gy1 = gx1 + dyn*(gx2 - gx1);
                            gx1 = magl[0][a] + dxn*(magl[0][a + 1] - magl[0][a]);
                            gx2 = magl[0][a - w] + dxn*(magl[0][a - w + 1] - magl[0][a - w]);
                            gy2 = gx1 + dyn*(gx2 - gx1);
                            magb = gy1 + dzn*(gy2 - gy1);
                            if (magb > magl[1][a])
                                continue;
                        }
                    }
                    out[alayer + a] = 1;
                }
            }
        }
        
        thLow = magmin + thLow*(magmax - magmin);
        thHigh = magmin + thHigh*(magmax - magmin);
        thLow *= thLow;
        thHigh *= thHigh;
            
        for (int z = 1; z < dims[2] - 1; ++z) {
            for (int y = 1; y < dims[1] - 1; ++y) {
                int a = (z*dims[1] + y)*dims[0] + 1;
                for (int x = 1; x < dims[0] - 1; ++x, ++a) {
                    if (out[a] == 1) {
                        float mag = calc3MagSqr(x, y, z);
                        if (mag < thLow)
                            out[a] = 0;
                        else if (mag >= thHigh)
                            follow3(x, y, z, thLow);
                    }
                }
            }
        }
        
        for (int i = 0; i < out.length; ++i) {
            if (out[i] == 2) {
                out[i] = (byte)(255);
            } else {
                out[i] = 0;  
            }
        }
    }
    
    private void nonMaxSupp2D(float thHigh, float thLow) {
        float[] mags = new float[dims[0]*dims[1]];
        float[][] dirs = new float[2][dims[0]*dims[1]];
        out = new byte[dims[0]*dims[1]];
        float magmin, magmax;
        
        calcDirs2(dirs, mags);
        
        magmin = magmax = mags[0];
        for (int i = 0; i < mags.length; ++i) {
            if (mags[i] > magmax)
                magmax = mags[i];
            else if (mags[i] < magmin)
                magmin = mags[i];
        }
        if (thLow < 1e-6f)
            thLow = 1e-6f;
        thLow = magmin + thLow*(magmax - magmin);
        thHigh = magmin + thHigh*(magmax - magmin);
                    
        int w = dims[0];
        for (int y = 1; y < dims[1] - 1; ++y) {
            int a = y*w + 1;
            for (int x = 1; x < w - 1; ++x, ++a) {
                out[a] = 0;
                if (mags[a] < thLow) {
                    continue;
                }
                float maga, magb, gx1, gx2;
                float dxn = dirs[0][a] / mags[a];
                float dyn = dirs[1][a] / mags[a];

                if (dxn*dyn > 0) {
                    dxn = Math.abs(dxn);
                    dyn = Math.abs(dyn);
                    gx1 = mags[a] + dxn*(mags[a + 1] - mags[a]);
                    gx2 = mags[a + w] + dxn*(mags[a + w + 1] - mags[a + w]);
                    maga = gx1 + dyn*(gx2 - gx1);
                    if (maga > mags[a])
                        continue;
                    gx1 = mags[a] + dxn*(mags[a - 1] - mags[a]);
                    gx2 = mags[a - w] + dxn*(mags[a - w - 1] - mags[a - w]);
                    magb = gx1 + dyn*(gx2 - gx1);
                    if (magb > mags[a])
                        continue;
                } else {
                    dxn = Math.abs(dxn);
                    dyn = Math.abs(dyn);
                    gx1 = mags[a] + dxn*(mags[a + 1] - mags[a]);
                    gx2 = mags[a - w] + dxn*(mags[a - w + 1] - mags[a - w]);
                    maga = gx1 + dyn*(gx2 - gx1);
                    if (maga > mags[a])
                        continue;
                    gx1 = mags[a] + dxn*(mags[a - 1] - mags[a]);
                    gx2 = mags[a + w] + dxn*(mags[a + w - 1] - mags[a + w]);
                    magb = gx1 + dyn*(gx2 - gx1);
                    if (magb > mags[a])
                        continue;
                }
                out[a] = 1;
            }
        }
        
        for (int y = 1; y < dims[1] - 1; ++y) {
            int a = y*dims[0] + 1;
            for (int x = 1; x < dims[0] - 1; ++x, ++a) {
                if (out[a] == 1 && mags[a] >= thHigh) {
                    follow2(x, y, thLow, mags);
                }
            }
        }
        
        for (int i = 0; i < out.length; ++i) {
            if (out[i] == 2) {
                out[i] = (byte)(255);
            } else {
                out[i] = 0;
            }
        }
    }
    
   public void calculateCannyGradients(DataArray dataMaskIn, int[] inDims, 
           int[] calcDims, boolean normalize, CannyMaskedGradParams gradParams, 
           RegularField outField, String outNameBase) {
       byte[] dataMask = dataMaskIn.getBData();
       float[][] goutF = null;
       byte[][] goutB = null;
       int outType = gradParams.getOutputType();
       double angX = gradParams.getDirX()*Math.PI;
       double angY = gradParams.getDirY()*Math.PI;
       float dirX = (float)Math.cos(angY)*(float)Math.cos(angX);
       float dirY = (float)Math.cos(angY)*(float)Math.sin(angX);
       float dirZ = (float)Math.sin(angY);
       
       if (outType < 0 || outType > 1)
           outType = 1;
       
       int outDims = gradParams.isDirOnly() ? 1 : dims.length;
       int goutLen = 1;
       for (int d = 0; d < dims.length; ++d)
           goutLen *= dims[d];
       switch (outType) {
           case 0:
               goutF = new float[outDims][goutLen];
               break;
           case 1:
           default:
               outType = 1;
               goutB = new byte[outDims][goutLen];
               break;
       }
           
       if (dims.length == 2) {
           for (int y = 1; y < calcDims[1] - 1; ++y) {
               int ain = y*inDims[0] + 1;
               int aout = y*calcDims[0] + 1;
               for (int x = 1; x < calcDims[0] - 1; ++x, ++ain, ++aout) {
                   if (dataMask[ain] == 0)
                       continue;
                   float[] d = calc2Grad(x, y);
                   if (normalize) {
                       float l = (float)Math.sqrt(d[0]*d[0] + d[1]*d[1]);
                       if (l <= 0.f) {
                           d[0] = 1.f;
                           d[1] = 0.f;
                       } else {
                           l = 1.f/l;
                           d[0] *= l;
                           d[1] *= l;
                       }
                   }
                   switch (outType) {
                       case 0:
                           if (gradParams.isDirOnly()) {
                               goutF[0][aout] = d[0];
                           } else {
                               goutF[0][aout] = d[0];
                               goutF[1][aout] = d[1];
                           }
                           break;
                       case 1:
                           if (gradParams.isDirOnly()) {
                               goutB[0][aout] = (byte)(127.f*(1.f + d[0]));
                           } else {
                               goutB[0][aout] = (byte)(127.f*d[0]);
                               goutB[1][aout] = (byte)(127.f*d[1]);
                           }
                           break;
                   }
               }
           }
       } else if (dims.length == 3) {
           for (int z = 1; z < calcDims[2] - 1; ++z) {
               for (int y = 1; y < calcDims[1] - 1; ++y) {
                   int ain = (z*inDims[1] + y)*inDims[0] + 1;
                   int aout = (z*calcDims[1] + y)*calcDims[0] + 1;
                   for (int x = 1; x < calcDims[0] - 1; ++x, ++ain, ++aout) {
                       if (dataMask[ain] == 0)
                           continue;
                       float[] d = calc3Grad(x, y, z);
                       if (normalize) {
                           float l = (float)Math.sqrt(d[0]*d[0] + d[1]*d[1] + d[2]*d[2]);
                           if (l <= 0.f) {
                               d[0] = 1.f;
                               d[1] = d[2] = 0.f;
                           } else {
                               l = 1.f/l;
                               d[0] *= l;
                               d[1] *= l;
                               d[2] *= l;
                           }
                       }
                       switch (outType) {
                           case 0:
                               if (gradParams.isDirOnly()) {
                                   goutF[0][aout] = d[0]*dirX + d[1]*dirY + d[2]*dirZ;
                               } else {
                                   goutF[0][aout] = d[0];
                                   goutF[1][aout] = d[1];
                                   goutF[2][aout] = d[2];
                               }
                               break;
                           case 1:
                               if (gradParams.isDirOnly()) {
                                   goutB[0][aout] = (byte)(127.f*(1.f + d[0]*dirX + d[1]*dirY + d[2]*dirZ));
                               } else {
                                   goutB[0][aout] = (byte)(127.f*d[0]);
                                   goutB[1][aout] = (byte)(127.f*d[1]);
                                   goutB[2][aout] = (byte)(127.f*d[2]);
                               }
                               break;
                       }
                   }
               }
           }
       }
       
       if (gradParams.isDirOnly()) {
           switch (outType) {
               case 0:
                   outField.addData(DataArray.create(goutF[0], 1, outNameBase+".Dir"));
                   break;
               case 1:
                   outField.addData(DataArray.create(goutB[0], 1, outNameBase+".Dir"));
                   break;
           }
       } else {
           for (int d = 0; d < dims.length; ++d) {
               switch (outType) {
                   case 0:
                       outField.addData(DataArray.create(goutF[d], 1, outNameBase+"."+d));
                       break;
                   case 1:
                       outField.addData(DataArray.create(goutB[d], 1, outNameBase+"."+d));
                       break;
               }
           }
       }
       if (gradParams.isAppendMask())
           outField.addData(dataMaskIn);
   }
    
    // Field Filler 2d & 3d:
    
    class Follow2El {
        int lx, rx;
        int dadlx, dadrx;
        int y, dir;
        public Follow2El(int lx, int rx, int dadlx, int dadrx, int y, int dir) {
            this.lx = lx;
            this.rx = rx;
            this.dadlx = dadlx;
            this.dadrx = dadrx;
            this.y = y;
            this.dir = dir;
        }
    }
    
    private void follow2stack(Stack<Follow2El> s, int dir, int dadlx, int dadrx, int lx, int rx, int y) {
        lx -= 1;
        rx += 1;
        s.push(new Follow2El(lx, rx, lx - 1, rx + 1, y + dir, dir));
        if (rx > dadrx)
            s.push(new Follow2El(dadrx + 1, rx, lx - 1, rx + 1, y - dir, -dir));
        if (lx < dadlx)
            s.push(new Follow2El(lx, dadlx - 1, lx - 1, rx + 1, y - dir, -dir));
    }
    
    private void follow2(int sx, int sy, float th, float[] mags) {
        Stack<Follow2El> stack = new Stack<Follow2El>();
        int lx, rx, x;
        boolean wasIn = false;
        int w = dims[0];
        int h = dims[1];
        int a = sy*w;
        rx = lx = sx;
        while (out[a + lx - 1] == 1 && mags[a + lx - 1] >= th) {
            lx -= 1;
            out[a + lx] = 2;
        }
        while (out[a + rx + 1] == 1 && mags[a + rx + 1] >= th) {
            rx += 1;
            out[a + rx] = 2;
        }
        stack.push(new Follow2El(lx, rx, lx, rx, sy + 1, 1));
        stack.push(new Follow2El(lx, rx, lx, rx, sy - 1, -1));
        while (!stack.empty()) {
            Follow2El e = stack.pop();
            
            if (e.y < 0 || e.y >= h)
                continue;
            a = e.y*w;
            lx = e.lx;
            rx = e.rx;
            x = lx + 1;
            wasIn = (out[a + lx] == 1 && mags[a + lx] >= th);
            if (wasIn) {
                out[a + lx] = 2;
                while (lx > 0 && out[a + lx - 1] == 1 && mags[a + lx - 1] >= th) {
                    lx -= 1;
                    out[a + lx] = 2;
                }
            }
            while (x < w) {
                if (wasIn) {
                    if (out[a + x] == 1 && mags[a + x] >= th) {
                        out[a + x] = 2;
                    } else {
                        follow2stack(stack, e.dir, e.dadlx, e.dadrx, lx, x - 1, e.y);
                        wasIn = false;
                    }
                } else {
                    if (x > rx)
                        break;
                    if (out[a + x] == 1 && mags[a + x] >= th) {
                        out[a + x] = 2;
                        wasIn = true;
                        lx = x;
                    }
                }
                x += 1;
            }
            if (wasIn) {
                follow2stack(stack, e.dir, e.dadlx, e.dadrx, lx, x - 1, e.y);
            }
        }
    }
    
    class Follow3El {
        int lx, rx;
        int dadlx, dadrx;
        int y, z, diry, dirz;
        public Follow3El(int lx, int rx, int dadlx, int dadrx, int y, int z, int diry, int dirz) {
            this.lx = lx;
            this.rx = rx;
            this.dadlx = dadlx;
            this.dadrx = dadrx;
            this.y = y;
            this.z = z;
            this.diry = diry;
            this.dirz = dirz;
        }
    }
    
    private void follow3stack(Stack<Follow3El> s, int diry, int dirz, int dadlx, int dadrx, int lx, int rx, int y, int z) {
        lx -= 1;
        rx += 1;
        s.push(new Follow3El(lx, rx, lx - 1, rx + 1, y + diry, z, diry, 0));
        s.push(new Follow3El(lx, rx, lx - 1, rx + 1, y + diry, z + dirz, diry, dirz));
        s.push(new Follow3El(lx, rx, lx - 1, rx + 1, y, z + dirz, 0, dirz));
        s.push(new Follow3El(lx, rx, lx - 1, rx + 1, y - diry, z + dirz, -diry, dirz));
        if (rx > dadrx) {
            s.push(new Follow3El(dadrx + 1, rx, lx - 1, rx + 1, y - diry, z - dirz, -diry, -dirz));
            s.push(new Follow3El(dadrx + 1, rx, lx - 1, rx + 1, y + diry, z - dirz, diry, -dirz));
            s.push(new Follow3El(dadrx + 1, rx, lx - 1, rx + 1, y, z - dirz, 0, -dirz));
            s.push(new Follow3El(dadrx + 1, rx, lx - 1, rx + 1, y - diry, z, -diry, 0));
        }
        if (lx < dadlx) {
            s.push(new Follow3El(lx, dadlx - 1, lx - 1, rx + 1, y - diry, z - dirz, -diry, -dirz));
            s.push(new Follow3El(lx, dadlx - 1, lx - 1, rx + 1, y + diry, z - dirz, diry, -dirz));
            s.push(new Follow3El(lx, dadlx - 1, lx - 1, rx + 1, y, z - dirz, 0, -dirz));
            s.push(new Follow3El(lx, dadlx - 1, lx - 1, rx + 1, y - diry, z, -diry, 0));
        }
    }
    
    private void follow3(int sx, int sy, int sz, float th) {
        Stack<Follow3El> stack = new Stack<Follow3El>();
        int lx, rx, x;
        boolean wasIn = false;
        int w = dims[0];
        int h = dims[1];
        int d = dims[2];
        int wh = w*h;
        int a = sz*wh + sy*w;
        rx = lx = sx;
        while (out[a + lx - 1] == 1 && calc3MagSqr(lx - 1, sy, sz) >= th) {
            lx -= 1;
            out[a + lx] = 2;
        }
        while (out[a + rx + 1] == 1 && calc3MagSqr(rx + 1, sy, sz) >= th) {
            rx += 1;
            out[a + rx] = 2;
        }
        stack.push(new Follow3El(lx, rx, lx, rx, sy + 1, sz    ,  1, 0));
        stack.push(new Follow3El(lx, rx, lx, rx, sy - 1, sz    , -1, 0));
        stack.push(new Follow3El(lx, rx, lx, rx, sy + 1, sz + 1,  1, 1));
        stack.push(new Follow3El(lx, rx, lx, rx, sy    , sz + 1,  0, 1));
        stack.push(new Follow3El(lx, rx, lx, rx, sy - 1, sz + 1, -1, 1));
        stack.push(new Follow3El(lx, rx, lx, rx, sy + 1, sz - 1,  1, -1));
        stack.push(new Follow3El(lx, rx, lx, rx, sy    , sz - 1,  0, -1));
        stack.push(new Follow3El(lx, rx, lx, rx, sy - 1, sz - 1, -1, -1));
        
        while (!stack.empty()) {
            Follow3El e = stack.pop();
            
            if (e.y < 0 || e.y >= h || e.z < 0 || e.z >= d)
                continue;
            a = e.z*wh + e.y*w;
            lx = e.lx;
            rx = e.rx;
            x = lx + 1;
            wasIn = (out[a + lx] == 1 && calc3MagSqr(lx, e.y, e.z) >= th);
            if (wasIn) {
                out[a + lx] = 2;
                while (lx > 0 && out[a + lx - 1] == 1 && calc3MagSqr(lx - 1, e.y, e.z) >= th) {
                    lx -= 1;
                    out[a + lx] = 2;
                }
            }
            while (x < w) {
                if (wasIn) {
                    if (out[a + x] == 1 && calc3MagSqr(x, e.y, e.z) >= th) {
                        out[a + x] = 2;
                    } else {
                        follow3stack(stack, e.diry, e.dirz, e.dadlx, e.dadrx, lx, x - 1, e.y, e.z);
                        wasIn = false;
                    }
                } else {
                    if (x > rx)
                        break;
                    if (out[a + x] == 1 && calc3MagSqr(x, e.y, e.z) >= th) {
                        out[a + x] = 2;
                        wasIn = true;
                        lx = x;
                    }
                }
                x += 1;
            }
            if (wasIn) {
                follow3stack(stack, e.diry, e.dirz, e.dadlx, e.dadrx, lx, x - 1, e.y, e.z);
            }
        }
    }
    /* Naiwna wersja fillera 3d: 
     * zdaje sie dzialac troche dokladniej od follow3, ale wymaga nawet kilkadziesiat razy wiecej pamieci.
     * Zostawione jedynie jako referencja. follow3 musi byc poprawiony.
    private class Point3i {
        int x, y, z;
        public Point3i(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
    
    private void follow3Old(int x, int y, int z, float th) {
        Stack<Point3i> s = new Stack();
        Point3i p = new Point3i(x, y, z);
        s.push(p);
        
        while (!s.empty()) {
            p = s.pop();
            x = p.x;
            y = p.y;
            z = p.z;
            int a = (z*dims[1] + y)*dims[0];
            int l = x, r = x;
            out[a + x] = 2;
            while (l > 0) {
                l -= 1;
                if (out[a + l] != 1 || calc3MagSqr(l, y, z) < th)
                    break;
                out[a + l] = 2;
            }
            while (r <= dims[0] - 1) {
                r += 1;
                if (out[a + r] != 1 || calc3MagSqr(r, y, z) < th)
                    break;
                out[a + r] = 2;
            }
            for (int i = l; i <= r; ++i) {
                if (out[a + i - dims[0]] == 1 && calc3MagSqr(i, y - 1, z) >= th) {
                    s.push(new Point3i(i, y - 1, z));
                }
                    
                if (out[a + i + dims[0]] == 1 && calc3MagSqr(i, y + 1, z) >= th) {
                    s.push(new Point3i(i, y + 1, z));
                }

                if (out[a + i - dim01] == 1 && calc3MagSqr(i, y, z - 1) >= th) {
                    s.push(new Point3i(i, y, z - 1));
                }
                if (out[a + i - dim01 - dims[0]] == 1 && calc3MagSqr(i, y - 1, z - 1) >= th) {                    
                    s.push(new Point3i(i, y - 1, z - 1));                    
                }
                if (out[a + i - dim01 + dims[0]] == 1 &&  calc3MagSqr(i, y + 1, z - 1) >= th) {
                    s.push(new Point3i(i, y + 1, z - 1));                    
                }

                if (out[a + i + dim01] == 1 && calc3MagSqr(i, y, z + 1) >= th) {
                    s.push(new Point3i(i, y, z + 1));
                }
                if (out[a + i + dim01 - dims[0]] == 1 && calc3MagSqr(i, y - 1, z + 1) >= th) {
                    s.push(new Point3i(i, y - 1, z + 1));
                }
                if (out[a + i + dim01 + dims[0]] == 1 && calc3MagSqr(i, y + 1, z + 1) >= th) {
                    s.push(new Point3i(i, y + 1, z + 1));
                }
            }
        }
    }*/
}
