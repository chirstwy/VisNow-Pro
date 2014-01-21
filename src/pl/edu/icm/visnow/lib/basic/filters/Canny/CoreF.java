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

import pl.edu.icm.visnow.datasets.dataarrays.DataArray;

/**
 *
 * @author Andrzej Rutkowski (rudy@mat.uni.torun.pl)
 * @author pregulski, ICM, UW
 *
 */
public class CoreF extends CoreBase {
    private float[] data;
    
    public CoreF(DataArray datain, int[] dims) {
        super(datain, dims);
    }
    
    @Override
    protected void initData() {
        data = dataArray.getFData();
    }
    
    @Override
    protected float calc3MagSqr(int x, int y, int z) {
        float[] in = data;
        int za = z * dims[0]*dims[1];
        int z1 = z == 0 ? 0 : za - dims[0]*dims[1];
        int z2 = z == dims[2] - 1 ? za : za + dims[0]*dims[1];
        int w = dims[0];
        int ya = y*w;
        int y1 = y == 0 ? 0 : ya - w;
        int y2 = y == dims[1] - 1 ? ya : ya + w;
        int x1 = x == 0 ? 0 : x - 1;
        int x2 = x == w - 1 ? w - 1 : x + 1;
        float dx, dy, dz;
        
        dx =
                in[z1 + y1 + x1] - in[z1 + y1 + x2]
                + 2.f*(in[z1 + ya + x1] - in[z1 + ya + x2])
                + in[z1 + y2 + x1] - in[z1 + y2 + x2]
                + 2.f*(in[za + y1 + x1] - in[za + y1 + x2]
                    + 2.f*(in[za + ya + x1] - in[za + ya + x2])
                    + in[za + y2 + x1] - in[za + y2 + x2])
                + in[z2 + y1 + x1] - in[z2 + y1 + x2]
                + 2.f*(in[z2 + ya + x1] - in[z2 + ya + x2])
                + in[z2 + y2 + x1] - in[z2 + y2 + x2];
        dy = 
                in[z1 + y1 + x1] + 2.f*in[z1 + y1 + x] + in[z1 + y1 + x2]
                - in[z1 + y2 + x1] - 2.f*in[z1 + y2 + x] - in[z1 + y2 + x2]
                + 2.f*(in[za + y1 + x1] + 2.f*in[za + y1 + x] + in[za + y1 + x2]
                    - in[za + y2 + x1] - 2.f*in[za + y2 + x] - in[za + y2 + x2])
                + in[z2 + y1 + x1] + 2.f*in[z2 + y1 + x] + in[z2 + y1 + x2]
                - in[z2 + y2 + x1] - 2.f*in[z2 + y2 + x] - in[z2 + y2 + x2];
        dz = 
                in[z1 + y1 + x1] + 2.f*in[z1 + y1 + x] + in[z1 + y1 + x2]
                + 2.f*(in[z1 + ya + x1] + 2.f*in[z1 + ya + x] + in[z1 + ya + x2])
                + in[z1 + y2 + x1] + 2.f*in[z1 + y2 + x] + in[z1 + y2 + x2]
                - in[z2 + y1 + x1] - 2.f*in[z2 + y1 + x] - in[z2 + y1 + x2]
                - 2.f*(in[z2 + ya + x1] + 2.f*in[z2 + ya + x] + in[z2 + ya + x2])
                - in[z2 + y2 + x1] - 2.f*in[z2 + y2 + x] - in[z2 + y2 + x2];
        
        
        return dx*dx + dy*dy + dz*dz;
    }
    
    @Override
    protected float[] calc3Grad(int x, int y, int z) {
        float[] in = data;
        int za = z * dims[0]*dims[1];
        int z1 = z == 0 ? 0 : za - dims[0]*dims[1];
        int z2 = z == dims[2] - 1 ? za : za + dims[0]*dims[1];
        int w = dims[0];
        int ya = y*w;
        int y1 = y == 0 ? 0 : ya - w;
        int y2 = y == dims[1] - 1 ? ya : ya + w;
        int x1 = x == 0 ? 0 : x - 1;
        int x2 = x == w - 1 ? w - 1 : x + 1;
        float[] d = new float[3];
        d[0] =
                in[z1 + y1 + x1] - in[z1 + y1 + x2]
                + 2.f*(in[z1 + ya + x1] - in[z1 + ya + x2])
                + in[z1 + y2 + x1] - in[z1 + y2 + x2]
                + 2.f*(in[za + y1 + x1] - in[za + y1 + x2]
                    + 2.f*(in[za + ya + x1] - in[za + ya + x2])
                    + in[za + y2 + x1] - in[za + y2 + x2])
                + in[z2 + y1 + x1] - in[z2 + y1 + x2]
                + 2.f*(in[z2 + ya + x1] - in[z2 + ya + x2])
                + in[z2 + y2 + x1] - in[z2 + y2 + x2];
        d[1] = 
                in[z1 + y1 + x1] + 2.f*in[z1 + y1 + x] + in[z1 + y1 + x2]
                - in[z1 + y2 + x1] - 2.f*in[z1 + y2 + x] - in[z1 + y2 + x2]
                + 2.f*(in[za + y1 + x1] + 2.f*in[za + y1 + x] + in[za + y1 + x2]
                    - in[za + y2 + x1] - 2.f*in[za + y2 + x] - in[za + y2 + x2])
                + in[z2 + y1 + x1] + 2.f*in[z2 + y1 + x] + in[z2 + y1 + x2]
                - in[z2 + y2 + x1] - 2.f*in[z2 + y2 + x] - in[z2 + y2 + x2];
        d[2] = 
                in[z1 + y1 + x1] + 2.f*in[z1 + y1 + x] + in[z1 + y1 + x2]
                + 2.f*(in[z1 + ya + x1] + 2.f*in[z1 + ya + x] + in[z1 + ya + x2])
                + in[z1 + y2 + x1] + 2.f*in[z1 + y2 + x] + in[z1 + y2 + x2]
                - in[z2 + y1 + x1] - 2.f*in[z2 + y1 + x] - in[z2 + y1 + x2]
                - 2.f*(in[z2 + ya + x1] + 2.f*in[z2 + ya + x] + in[z2 + ya + x2])
                - in[z2 + y2 + x1] - 2.f*in[z2 + y2 + x] - in[z2 + y2 + x2];
        return d;
    }
    
    @Override
    protected void calcDirs3MagsLayer(int z, float[][] dout, float[] mout) {
        float[] in = data;
        int za = z * dims[0]*dims[1];
        int z1 = z == 0 ? 0 : za - dims[0]*dims[1];
        int z2 = z == dims[2] - 1 ? za : za + dims[0]*dims[1];
        int w = dims[0];
        for (int y = 0; y < dims[1]; ++y) {
            int ya = y*w;
            int y1 = y == 0 ? 0 : ya - w;
            int y2 = y == dims[1] - 1 ? ya : ya + w;
            for (int x = 0; x < w; ++x) {
                int x1 = x == 0 ? 0 : x - 1;
                int x2 = x == w - 1 ? w - 1 : x + 1;
                dout[0][/*za+ */ya + x] =
                        in[z1 + y1 + x1] - in[z1 + y1 + x2]
                        + 2.f*(in[z1 + ya + x1] - in[z1 + ya + x2])
                        + in[z1 + y2 + x1] - in[z1 + y2 + x2]
                        + 2.f*(in[za + y1 + x1] - in[za + y1 + x2]
                            + 2.f*(in[za + ya + x1] - in[za + ya + x2])
                            + in[za + y2 + x1] - in[za + y2 + x2])
                        + in[z2 + y1 + x1] - in[z2 + y1 + x2]
                        + 2.f*(in[z2 + ya + x1] - in[z2 + ya + x2])
                        + in[z2 + y2 + x1] - in[z2 + y2 + x2];
                dout[1][/*za + */ya + x] = 
                        in[z1 + y1 + x1] + 2.f*in[z1 + y1 + x] + in[z1 + y1 + x2]
                        - in[z1 + y2 + x1] - 2.f*in[z1 + y2 + x] - in[z1 + y2 + x2]
                        + 2.f*(in[za + y1 + x1] + 2.f*in[za + y1 + x] + in[za + y1 + x2]
                            - in[za + y2 + x1] - 2.f*in[za + y2 + x] - in[za + y2 + x2])
                        + in[z2 + y1 + x1] + 2.f*in[z2 + y1 + x] + in[z2 + y1 + x2]
                        - in[z2 + y2 + x1] - 2.f*in[z2 + y2 + x] - in[z2 + y2 + x2];
                dout[2][/*za + */ya + x] = 
                        in[z1 + y1 + x1] + 2.f*in[z1 + y1 + x] + in[z1 + y1 + x2]
                        + 2.f*(in[z1 + ya + x1] + 2.f*in[z1 + ya + x] + in[z1 + ya + x2])
                        + in[z1 + y2 + x1] + 2.f*in[z1 + y2 + x] + in[z1 + y2 + x2]
                        - in[z2 + y1 + x1] - 2.f*in[z2 + y1 + x] - in[z2 + y1 + x2]
                        - 2.f*(in[z2 + ya + x1] + 2.f*in[z2 + ya + x] + in[z2 + ya + x2])
                        - in[z2 + y2 + x1] - 2.f*in[z2 + y2 + x] - in[z2 + y2 + x2];
                
                mout[ya + x] = (float)Math.sqrt(
                        dout[0][ya + x]*dout[0][ya + x] + 
                        dout[1][ya + x]*dout[1][ya + x] +
                        dout[2][ya + x]*dout[2][ya + x]);
                
            }
        }
    }

    @Override
    protected float[] calc2Grad(int x, int y) {
        float[] in = data;
        float[] dout = new float[2];
        int w = dims[0];
        int ya = y * w;
        int y1 = y == 0 ? 0 : ya - w;
        int y2 = y == dims[1] - 1 ? ya : ya + w;
        int x1 = x == 0 ? 0 : x - 1;
        int x2 = x == w - 1 ? w - 1 : x + 1;
        /*dout[ya + x] = in[y1 + x1] - in[y1 + x2]
        + 2.f*(in[ya + x1] - in[ya + x2])
        + in[y2 + x1] - in[y2 + x2];
        dout[ya + x] = in[y1 + x1] + 2.f*in[y1 + x] + in[y1 + x2]
        - in[y2 + x1] - 2.f*in[y2 + x] - in[y2 + x2];*/
        dout[0] = in[y1 + x1] - in[y1 + x2]
                + 2.f*(in[ya + x1] - in[ya + x2])
                + in[y2 + x1] - in[y2 + x2];
        dout[1] = in[y1 + x1] + 2.f*in[y1 + x] + in[y1 + x2]
                - in[y2 + x1] - 2.f*in[y2 + x] - in[y2 + x2];
        return dout;
    }
    
    @Override
    protected void calcDirs2(float[][] dout, float[] mout) {
        float[] in = data;
        int w = dims[0];
        for (int y = 0; y < dims[1]; ++y) {
            int ya = y * w;
            int y1 = y == 0 ? 0 : ya - w;
            int y2 = y == dims[1] - 1 ? ya : ya + w;
            for (int x = 0; x < w; ++x) {
                int x1 = x == 0 ? 0 : x - 1;
                int x2 = x == w - 1 ? w - 1 : x + 1;
                dout[0][ya + x] = in[y1 + x1] - in[y1 + x2]
                        + 2.f*(in[ya + x1] - in[ya + x2])
                        + in[y2 + x1] - in[y2 + x2];
                dout[1][ya + x] = in[y1 + x1] + 2.f*in[y1 + x] + in[y1 + x2]
                        - in[y2 + x1] - 2.f*in[y2 + x] - in[y2 + x2];
                mout[ya + x] = (float)Math.sqrt(
                        dout[0][ya + x]*dout[0][ya + x] + 
                        dout[1][ya + x]*dout[1][ya + x]);
            }
        }
    }
}
