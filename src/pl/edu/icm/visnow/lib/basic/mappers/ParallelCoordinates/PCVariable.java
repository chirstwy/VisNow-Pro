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

package pl.edu.icm.visnow.lib.basic.mappers.ParallelCoordinates;

/**
 * @author Bartosz Borucki (babor@icm.edu.pl) Warsaw University,
 * Interdisciplinary Centre for Mathematical and Computational Modelling
 * 13 October 2013
 */
public class PCVariable {
    private int dims = 2; //2D or 3D
    private int xComponent = 0;
    private int yComponent = 0;
    private String xName = "N/A";
    private String yName = "N/A";
    private float xMin = 0.0f;
    private float xMax = 1.0f;
    private float xLow = 0.0f;
    private float xUp = 1.0f;
    private float yMin = 0.0f;
    private float yMax = 1.0f;
    private float yLow = 0.0f;
    private float yUp = 1.0f;
    private boolean selected = false;
    private boolean visible = true;

    public PCVariable(int xComponent, int yComponent, String xName, String yName, float xmin, float xmax, float ymin, float ymax) {
        this(xComponent, yComponent, xName, yName, xmin, xmin, xmax, xmax, ymin, ymin, ymax, ymax, false, true);        
    }    
    
    public PCVariable(int xComponent, int yComponent, String xName, String yName, float xmin, float xmax, float ymin, float ymax, boolean selected) {
        this(xComponent, yComponent, xName, yName, xmin, xmin, xmax, xmax, ymin, ymin, ymax, ymax, selected, true);        
    }

    public PCVariable(int xComponent, int yComponent, String xName, String yName, float xmin, float xlow, float xup, float xmax, float ymin, float ylow, float yup, float ymax, boolean selected, boolean visible) {
        this.dims = 3;
        this.xComponent = xComponent;
        this.yComponent = yComponent;
        this.xName = xName;
        this.yName = yName;
        this.xMin = xmin;
        this.xLow = xlow;
        this.xUp = xup;
        this.xMax = xmax;
        this.yMin = ymin;
        this.yLow = ylow;
        this.yUp = yup;
        this.yMax = ymax;
        this.selected = selected;
    }

    public PCVariable(int component, String xName, float xmin, float xmax) {
        this(component, xName, xmin, xmin, xmax, xmax, false, true);        
    }    
    
    public PCVariable(int component, String xName, float xmin, float xmax, boolean selected) {
        this(component, xName, xmin, xmin, xmax, xmax, selected, true);        
    }

    public PCVariable(int component, String xName, float xmin, float xlow, float xup, float xmax, boolean selected, boolean visible) {
        this.dims = 2;
        this.xComponent = component;
        this.xName = xName;
        this.xMin = xmin;
        this.xLow = xlow;
        this.xUp = xup;
        this.xMax = xmax;
        this.selected = selected;
    }

    public int getDims() {
        return dims;
    }

    /**
     * @return the component
     */
    public int getXComponent() {
        return xComponent;
    }

    /**
     * @param component the component to set
     */
    public void setXComponent(int component) {
        this.xComponent = component;
    }

    /**
     * @return the component
     */
    public int getYComponent() {
        return yComponent;
    }

    /**
     * @param component the component to set
     */
    public void setYComponent(int component) {
        this.yComponent = component;
    }
    
    /**
     * @return the min
     */
    public float getXMin() {
        return xMin;
    }

    /**
     * @param min the min to set
     */
    public void setXMin(float min) {
        this.xMin = min;
    }

    /**
     * @return the max
     */
    public float getXMax() {
        return xMax;
    }

    /**
     * @param max the max to set
     */
    public void setXMax(float max) {
        this.xMax = max;
    }

    /**
     * @return the low
     */
    public float getXLow() {
        return xLow;
    }

    /**
     * @param low the low to set
     */
    public void setXLow(float low) {
        this.xLow = low;
    }

    /**
     * @return the up
     */
    public float getXUp() {
        return xUp;
    }

    /**
     * @param up the up to set
     */
    public void setXUp(float up) {
        this.xUp = up;
    }

    /**
     * @return the min
     */
    public float getYMin() {
        return yMin;
    }

    /**
     * @param min the min to set
     */
    public void setYMin(float min) {
        this.yMin = min;
    }

    /**
     * @return the max
     */
    public float getYMax() {
        return yMax;
    }

    /**
     * @param max the max to set
     */
    public void setYMax(float max) {
        this.yMax = max;
    }

    /**
     * @return the low
     */
    public float getYLow() {
        return yLow;
    }

    /**
     * @param low the low to set
     */
    public void setYLow(float low) {
        this.yLow = low;
    }

    /**
     * @return the up
     */
    public float getYUp() {
        return yUp;
    }

    /**
     * @param up the up to set
     */
    public void setYUp(float up) {
        this.yUp = up;
    }
    
    
    /**
     * @return the selected
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * @param selected the selected to set
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * @return the name
     */
    public String getXName() {
        return xName;
    }

    /**
     * @param name the name to set
     */
    public void setXName(String name) {
        this.xName = name;
    }

    /**
     * @return the name
     */
    public String getYName() {
        return yName;
    }

    /**
     * @param name the name to set
     */
    public void setYName(String name) {
        this.yName = name;
    }
    
    /**
     * @return the visible
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * @param visible the visible to set
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    
}
