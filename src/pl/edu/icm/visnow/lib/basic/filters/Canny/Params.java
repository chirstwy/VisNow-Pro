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

import pl.edu.icm.visnow.engine.core.ParameterEgg;
import pl.edu.icm.visnow.engine.core.ParameterType;
import pl.edu.icm.visnow.engine.core.Parameters;

/**
 *
 * @author Andrzej Rutkowski (rudy@mat.uni.torun.pl)
 * @author pregulski, ICM, UW
 */
public class Params extends Parameters{
    private static float THRESHOLDLOW = 0.3f;
    private static float THRESHOLDHIGH = 0.45f;    

    private static ParameterEgg[] eggs = new ParameterEgg[]{
        new ParameterEgg<Float>("thresholdLow", ParameterType.dependent, THRESHOLDLOW), 
        new ParameterEgg<Float>("thresholdHigh", ParameterType.dependent, THRESHOLDHIGH), 
    };
    
    Params(){
        super (eggs);
    }
            
    public float getThresholdHigh() {
        return (Float) getValue("thresholdHigh");
    }

    public void setThresholdHigh(float thresholdHigh) {
        setValue("thresholdHigh",thresholdHigh);
    }

    public float getThresholdLow() {
        return (Float) getValue("thresholdLow");
    }

    public void setThresholdLow(float thresholdLow) {
        setValue("thresholdLow",thresholdLow);
    }
}
