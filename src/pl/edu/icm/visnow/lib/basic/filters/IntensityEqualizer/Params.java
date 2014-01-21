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

import java.util.ArrayList;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import pl.edu.icm.visnow.datasets.RegularField;

/**
 *
 ** @author Krzysztof S. Nowinski, University of Warsaw ICM
 */
public class Params {
    private float gain = 1.0f;
    private float[] weights = null;

    private boolean silent = false;

    private boolean fullPadding = false;

    public Params() {
    }

    /**
     * @return the gain
     */
    public float getGain() {
        return gain;
    }

    /**
     * @param gain the gain to set
     */
    public void setGain(float gain) {
        this.gain = gain;
        fireStateChanged();
    }

    /**
     * @return the weights
     */
    public float[] getWeights() {
        return weights;
    }

    public void setupWeights(RegularField inField) {
        if(inField == null || inField.getDims() == null || inField.getDims().length < 2 || inField.getDims().length > 3) {
            this.weights = new float[1];
            weights[0] = 1.0f;
        } else {
            int[] dims = inField.getDims();
            int min = Math.min(dims[0], dims[1]);
            if(dims.length == 3)
                min = Math.min(min, dims[2]);

            int n = 1;
            while(Math.pow(2, n) < min) n++;
            n-=1;

            System.out.println("min = "+min);
            System.out.println("n = "+n);

            if(n<1) n=1;
            weights = new float[n];
            for (int i = 0; i < weights.length; i++) {
                weights[i] = 1.0f;
            }

            gain = 1.0f;
        }
    }

    public void setWeight(int i, float value) {
        if(weights != null && i >=0 && i < weights.length) {
            weights[i] = value;
            //System.out.println("weight "+i+" set to "+value);
            fireStateChanged();
        }
    }

    public float getWeight(int i) {
        if(weights != null && i >=0 && i < weights.length)
            return weights[i];
        else
            return 1.0f;
    }

    public int getNLevels() {
        if(weights == null)
            return 0;

        return weights.length;
    }

    private transient ArrayList<ChangeListener> changeListenerList = new ArrayList<ChangeListener>();

    /**
     * Registers ChangeListener to receive events.
     * @param listener The listener to register.
     */
    public synchronized void addChangeListener(ChangeListener listener) {
        changeListenerList.add(listener);
    }

    /**
     * Removes ChangeListener from the list of listeners.
     * @param listener The listener to remove.
     */
    public synchronized void removeChangeListener(ChangeListener listener) {
        changeListenerList.remove(listener);
    }

    /**
     * Notifies all registered listeners about the event.
     *
     * @param object Parameter #1 of the <CODE>ChangeEvent<CODE> constructor.
     */
    public void fireStateChanged() {
        if(silent)
            return;
        
        ChangeEvent e = new ChangeEvent(this);
        for (ChangeListener listener : changeListenerList) {
            listener.stateChanged(e);
        }
    }

    /**
     * @return the silent
     */
    public boolean isSilent() {
        return silent;
    }

    /**
     * @param silent the silent to set
     */
    public void setSilent(boolean silent) {
        this.silent = silent;
        if(!silent)
            fireStateChanged();
    }

    /**
     * @return the fullPadding
     */
    public boolean isFullPadding() {
        return fullPadding;
    }

    /**
     * @param fullPadding the fullPadding to set
     */
    public void setFullPadding(boolean fullPadding) {
        this.fullPadding = fullPadding;
        fireStateChanged();
    }


}
