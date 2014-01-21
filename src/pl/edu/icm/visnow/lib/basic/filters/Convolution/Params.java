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
package pl.edu.icm.visnow.lib.basic.filters.Convolution;

import pl.edu.icm.visnow.engine.core.ParameterEgg;
import pl.edu.icm.visnow.engine.core.ParameterType;
import pl.edu.icm.visnow.engine.core.Parameters;
import pl.edu.icm.visnow.lib.utils.PaddingType;

/**
 *
 * @author Piotr Wendykier (piotrw@icm.edu.pl)
 */
public class Params extends Parameters {

    private static ParameterEgg[] eggs = new ParameterEgg[]{
        new ParameterEgg<int[]>("components", ParameterType.independent, new int[]{0}),
        new ParameterEgg<PaddingType>("paddingType", ParameterType.independent, PaddingType.FIXED),
        new ParameterEgg<Boolean>("normalizeKernel", ParameterType.independent, true),
    };

    public Params() {
        super(eggs);
    }

    public int[] getComponents() {
        return (int[]) getValue("components");
    }

    public void setComponents(int[] components) {
        setValue("components", components);
        fireStateChanged();
    }

    public boolean isNormalizeKernel() {
        return (Boolean) getValue("normalizeKernel");
    }

    public void setNormalizeKernel(boolean value) {
        setValue("normalizeKernel", value);
        fireStateChanged();
    }

    public PaddingType getPaddingType() {
        return (PaddingType) getValue("paddingType");
    }

    public void setPaddingType(PaddingType value) {
        setValue("paddingType", value);
        fireStateChanged();
    }
}
