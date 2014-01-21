//<editor-fold defaultstate="collapsed" desc=" COPYRIGHT AND LICENSE ">
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
//</editor-fold>

package pl.edu.icm.visnow.lib.basic.filters.ReindexRegularField;

import pl.edu.icm.visnow.engine.core.ParameterEgg;
import pl.edu.icm.visnow.engine.core.ParameterType;
import pl.edu.icm.visnow.engine.core.Parameters;

/**
 *
 * @author Krzysztof S. Nowinski
 *   <p>   University of Warsaw, ICM
 */
public class Params extends Parameters {


    private static ParameterEgg[] eggs = new ParameterEgg[]
    {
        new ParameterEgg<int[]>("coordinate", ParameterType.dependent, null),
        new ParameterEgg<boolean[]>("mirror", ParameterType.dependent, null)
    };


    public Params()
    {
        super(eggs);
        setValue("coordinate",new int[] {0, 1, 2});
        setValue("mirror",new boolean[] {false, false, false});
    }

    /**
     * Get the value of mirror
     *
     * @return the value of mirror
     */
    public boolean[] isMirror() {
        return (boolean[])getValue("mirror");
    }

    /**
     * Set the value of mirror
     *
     * @param mirror new value of mirror
     */
    public void setMirror(boolean[] mirror) {
        setValue("mirror",mirror);
    }

    /**
     * Get the value of mirror at specified index
     *
     * @param index
     * @return the value of mirror at specified index
     */
    public boolean isMirror(int index) {
        return ((boolean[])getValue("mirror"))[index];
    }

    /**
     * Set the value of mirror at specified index.
     *
     * @param index
     * @param newMirror new value of mirror at specified index
     */
    public void setMirror(int index, boolean newMirror) {
        ((boolean[])getValue("mirror"))[index] = newMirror;
    }

    /**
     * Get the value of coordinate
     *
     * @return the value of coordinate
     */
    public int[] getCoordinate() {
        return (int[])getValue("coordinate");
    }

    /**
     * Set the value of coordinate
     *
     * @param coordinate new value of coordinate
     */
    public void setCoordinate(int[] coordinate) {
        setValue("coordinate",coordinate);
    }

    /**
     * Get the value of coordinate at specified index
     *
     * @param index
     * @return the value of coordinate at specified index
     */
    public int getCoordinate(int index) {
        return ((int[])getValue("coordinate"))[index];
    }

    /**
     * Set the value of coordinate at specified index.
     *
     * @param index
     * @param newCoordinate new value of coordinate at specified index
     */
    public void setCoordinate(int index, int newCoordinate) {
        ((int[])getValue("coordinate"))[index] = newCoordinate;
    }
}
