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

package pl.edu.icm.visnow.lib.basic.mappers.Trajectories;

import pl.edu.icm.visnow.datasets.Field;
import pl.edu.icm.visnow.datasets.RegularField;
import pl.edu.icm.visnow.datasets.dataarrays.DataArray;

/**
 *
 * @author creed
 */
public class MapCoordsToData {

    public static RegularField MapCoordsToData(Field field) {
        final int nNodes = field.getNNodes();
        final int nFrames = field.getNFrames();
        final int nSpace = field.getNSpace();
        int[] dims = new int[]{nFrames, nNodes};
//		float[][] points = {{-.5f, -.5f, 0}, {.5f, .5f, 0}};
//		RegularField mappedField = new RegularField(dims, points);
        RegularField mappedField = new RegularField(dims);

        float[][] mappedCoords = new float[nSpace][nNodes * nFrames];
        boolean[] mask = new boolean[nNodes * nFrames];
        boolean maskNeeded = false;

        for (int n = 0; n < nNodes; n++) {
            for (int t = 0; t < nFrames; t++) {
                boolean b = mask[ n * nFrames + t] = field.getMask(t)[n];
                if( b==false )
                    maskNeeded = true;
            }
        }

        if( maskNeeded ) {
            mappedField.setMask(mask);
        }

        for (int d = 0; d < nSpace; d++) {
            for (int n = 0; n < nNodes; n++) {
                for (int t = 0; t < nFrames; t++) {
                    float val = field.getCoords(t)[nSpace * n + d];
                    mappedCoords[d][ n * nFrames + t] = val;
//                  mappedCoords[d][ t * nNodes + n] = val;
                }
            }
        }

        String[] coordsDesc = new String[]{"x", "y", "z"};
        assert (nSpace <= coordsDesc.length);
        for (int d = 0; d < nSpace; d++) {
            DataArray da = DataArray.create(mappedCoords[d], 1, coordsDesc[d]);
            mappedField.addData(da);
        }

        return mappedField;
    }
}
