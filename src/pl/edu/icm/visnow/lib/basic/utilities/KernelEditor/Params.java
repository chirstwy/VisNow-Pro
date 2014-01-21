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

import pl.edu.icm.visnow.engine.core.ParameterEgg;
import pl.edu.icm.visnow.engine.core.ParameterType;
import pl.edu.icm.visnow.engine.core.Parameters;


/**
 *
 * @author Piotr Wendykier (piotrw@icm.edu.pl)
 */
public class Params extends Parameters {
    
    public static final int CONSTANT = 0;
    public static final int GAUSSIAN = 1;
    public static final int LINEAR = 2;
    public static final int CONICAL = 3;
    public static final int CUSTOM = 4;

    private static ParameterEgg[] eggs = new ParameterEgg[]{
        new ParameterEgg<Integer>("kernelType", ParameterType.independent, CONSTANT),
        new ParameterEgg<Integer>("rank", ParameterType.independent, 2),
        new ParameterEgg<Integer>("radius", ParameterType.independent, 1),
        new ParameterEgg<Boolean>("normalized", ParameterType.independent, false),
        new ParameterEgg<Float>("gaussianSigma", ParameterType.independent, 0.5f),
        new ParameterEgg<float[]>("kernel", ParameterType.independent, new float[] {1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f})
        
    };

    public Params() {
        super(eggs);
    }
    
    public int getKernelType() {
        return (Integer) getValue("kernelType");
    }

    public void setKernelType(int value) {
        setValue("kernelType", value);
        fireStateChanged();
    }
    
    public int getRank() {
        return (Integer) getValue("rank");
    }

    public void setRank(int value) {
        setValue("rank", value);
        fireStateChanged();
    }

    public int getRadius() {
        return (Integer) getValue("radius");
    }

    public void setRadius(int value) {
        setValue("radius", value);
        fireStateChanged();
    }
    
    public boolean isNormalized() {
        return (Boolean) getValue("normalized");
    }

    public void setNormalized(boolean value) {
        setValue("normalized", value);
        fireStateChanged();
    }

    public float getGaussianSigma() {
        return (Float) getValue("gaussianSigma");
    }

    public void setGaussianSigma(float value) {
        setValue("gaussianSigma", value);
        fireStateChanged();
    }
    
    public float[] getKernel() {
        return (float[]) getValue("kernel");
    }

    public void setKernel(float[] value) {
        setValue("kernel", value);
//        fireStateChanged();
    }
    
}
