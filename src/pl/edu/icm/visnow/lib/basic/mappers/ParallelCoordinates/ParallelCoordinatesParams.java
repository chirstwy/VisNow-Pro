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

import java.awt.Color;
import java.util.ArrayList;
import pl.edu.icm.visnow.datasets.Field;
import pl.edu.icm.visnow.engine.core.ParameterEgg;
import pl.edu.icm.visnow.engine.core.ParameterType;
import pl.edu.icm.visnow.engine.core.Parameters;
import pl.edu.icm.visnow.geometries.parameters.FontParams;

/**
 * @author Bartosz Borucki (babor@icm.edu.pl) Warsaw University,
 * Interdisciplinary Centre for Mathematical and Computational Modelling
 * 13 October 2013
 */
public class ParallelCoordinatesParams extends Parameters {

    public static final int GEOMETRY_TYPE_2D = 0;
    public static final int GEOMETRY_TYPE_3D = 1;
    public static final int SELECTION_LOGIC_AND = 0;
    public static final int SELECTION_LOGIC_OR = 1;

    
    public ParallelCoordinatesParams() {
        super(eggs);
        this.active = false;
        setAxesColor(Color.WHITE);
        setLineColor(Color.BLUE);
        setSelectionColor(Color.RED);
        setVariables(new ArrayList<PCVariable>());
        setDownsize(new int[]{1,1,1});
        setDefaultDownsize(this.getDownsize().clone());
        FontParams fp = new FontParams();
        fp.setSize(0.015f);
        setFontParams(fp);
        this.active = true;
    }
    private static ParameterEgg[] eggs = new ParameterEgg[]{
        new ParameterEgg<Integer>("nVariables", ParameterType.independent, 0),                                                        
        new ParameterEgg<int[]>("downsize", ParameterType.independent, null),                                                          
        new ParameterEgg<int[]>("defaultDownsize", ParameterType.independent, null),                                                          
        new ParameterEgg<Integer>("geometryType", ParameterType.independent, GEOMETRY_TYPE_2D),                                       
        new ParameterEgg<ArrayList<PCVariable>>("variables", ParameterType.dependent, null),                                          
        new ParameterEgg<Integer>("selectionLogic", ParameterType.independent, SELECTION_LOGIC_AND),                                  
        new ParameterEgg<Float>("scale", ParameterType.independent, 1.0f),                                                            
        new ParameterEgg<Integer>("selectionType", ParameterType.independent, ParallelCoordinatesObject2D.SELECTION_TYPE_HIGHLIGHT),  
        new ParameterEgg<Integer>("dataMapping", ParameterType.independent, ParallelCoordinatesObject2D.DATA_MAPPING_DIRECT),         
        new ParameterEgg<Color>("axesColor", ParameterType.independent, null),                                                        
        new ParameterEgg<Color>("lineColor", ParameterType.independent, null),                                                        
        new ParameterEgg<Color>("selectionColor", ParameterType.independent, null),                                                   
        new ParameterEgg<Float>("selectionDim", ParameterType.independent, 0.3f),                                                     
        new ParameterEgg<Boolean>("clipColorRange", ParameterType.independent, false),                                                     
        new ParameterEgg<Float>("planesTransparency", ParameterType.independent, 0.5f),                                                     
        new ParameterEgg<Float>("selectionAreaTransparency", ParameterType.independent, 0.5f),                                                     
        new ParameterEgg<Integer>("lines3DType", ParameterType.independent, ParallelCoordinatesObject3D.LINES3D_TYPE_LINES),                                                     
        new ParameterEgg<Float>("lineWidth", ParameterType.independent, 0.25f),
        new ParameterEgg<FontParams>("fontParams", ParameterType.independent, null),                                                     
    };
    
    

    public void setNVariables(int nVariables) {
        if (nVariables < 2) {
            return;
        }
        this.setValue("nVariables", nVariables);
    }

    public int getNVariables() {
        return (Integer) this.getValue("nVariables");
    }
    
    public void setClipColorRange(boolean clipColorRange) {
        this.setValue("clipColorRange", clipColorRange);
    }

    public boolean isClipColorRange() {
        return (Boolean) this.getValue("clipColorRange");
    }

    public void setDataMapping(int dataMapping) {
        this.setValue("dataMapping", dataMapping);
    }

    public int getDataMapping() {
        return (Integer) this.getValue("dataMapping");
    }

    public void setSelectionDim(float selectionDim) {
        this.setValue("selectionDim", selectionDim);
    }

    public float getSelectionDim() {
        return (Float) this.getValue("selectionDim");
    }
    
    public void setAxesColor(Color axesColor) {
        this.setValue("axesColor", axesColor);
    }

    public Color getAxesColor() {
        return (Color) this.getValue("axesColor");
    }

    public void setLineColor(Color lineColor) {
        this.setValue("lineColor", lineColor);
    }

    public Color getLineColor() {
        return (Color) this.getValue("lineColor");
    }

    public void setSelectionColor(Color selectionColor) {
        this.setValue("selectionColor", selectionColor);
    }

    public Color getSelectionColor() {
        return (Color) this.getValue("selectionColor");
    }    
    
    public void setDownsize(int[] downsize) {
        this.setValue("downsize", downsize);
    }

    public int[] getDownsize() {
        return (int[]) this.getValue("downsize");
    }

    public void setDefaultDownsize(int[] downsize) {
        this.setValue("defaultDownsize", downsize);
    }

    public int[] getDefaultDownsize() {
        return (int[]) this.getValue("defaultDownsize");
    }
    
    public void setGeometryType(int geometryType) {
        this.setValue("geometryType", geometryType);
    }

    public int getGeometryType() {
        return (Integer) this.getValue("geometryType");
    }

    public void setVariables(ArrayList<PCVariable> variables) {
        this.setValue("variables", variables);
    }
    
    public ArrayList<PCVariable> getVariables() {
        return (ArrayList<PCVariable>) this.getValue("variables");
    }
    
    public void setSelectionLogic(int selectionLogic) {
        this.setValue("selectionLogic", selectionLogic);
    }

    public int getSelectionLogic() {
        return (Integer) this.getValue("selectionLogic");
    }
    
    public void setScale(float scale) {
        this.setValue("scale", scale);
    }

    public float getScale() {
        return (Float) this.getValue("scale");
    }

    public void setSelectionType(int selectionType) {
        this.setValue("selectionType", selectionType);
    }

    public int getSelectionType() {
        return (Integer) this.getValue("selectionType");
    }

    public void setPlanesTransparency(float planesTransparency) {
        this.setValue("planesTransparency", planesTransparency);
    }

    public float getPlanesTransparency() {
        return (Float) this.getValue("planesTransparency");
    }
    
    public void setSelectionAreaTransparency(float selectionAreaTransparency) {
        this.setValue("selectionAreaTransparency", selectionAreaTransparency);
    }

    public float getSelectionAreaTransparency() {
        return (Float) this.getValue("selectionAreaTransparency");
    }
    
    public void setLines3DType(int lines3DType) {
        this.setValue("lines3DType", lines3DType);
    }

    public int getLines3DType() {
        return (Integer) this.getValue("lines3DType");
    }

    public void setLineWidth(float lineWidth) {
        this.setValue("lineWidth", lineWidth);
    }

    public float getLineWidth() {
        return (Float) this.getValue("lineWidth");
    }
    
    public void setFontParams(FontParams fontParams) {
        this.setValue("fontParams", fontParams);
    }

    public FontParams getFontParams() {
        return (FontParams) this.getValue("fontParams");
    }    
    
    public void reset(Field field, int geometryType) {
        boolean oldActive = active;
        boolean oldParameterActive = parameterActive;
        active = false;
        parameterActive = false;
        
        setNVariables(2);
        
        ArrayList<PCVariable> variables = getVariables();
        variables.clear();        
        int nScalarComponents = 0;
        int firstScalar = -1;
        int secondScalar = -1;
        int thirdScalar = -1;
        int fourthScalar = -1;
        for (int i = 0; i < field.getNData(); i++) {
            if(field.getData(i).getVeclen() == 1) {
                nScalarComponents++;
                if(firstScalar == -1) firstScalar = i;
                else if(firstScalar != -1 && secondScalar == -1) secondScalar = i;                
                else if(firstScalar != -1 && secondScalar != -1 && thirdScalar == -1) thirdScalar = i;                
                else if(firstScalar != -1 && secondScalar != -1 && thirdScalar != -1 && fourthScalar == -1) fourthScalar = i;                
            }
        }        
        
        if(nScalarComponents > 3 && geometryType == GEOMETRY_TYPE_3D) {
            variables.add(new PCVariable(firstScalar, secondScalar, 
                    field.getData(firstScalar).getName(), field.getData(secondScalar).getName(), 
                    field.getData(firstScalar).getMinv(), field.getData(firstScalar).getMaxv(),
                    field.getData(secondScalar).getMinv(), field.getData(secondScalar).getMaxv()));                
            variables.add(new PCVariable(thirdScalar, fourthScalar,
                    field.getData(thirdScalar).getName(), field.getData(fourthScalar).getName(), 
                    field.getData(thirdScalar).getMinv(), field.getData(thirdScalar).getMaxv(),
                    field.getData(fourthScalar).getMinv(), field.getData(fourthScalar).getMaxv()));                
        } else if(nScalarComponents > 1) {
            switch(geometryType) {
                case GEOMETRY_TYPE_2D:
                    variables.add(new PCVariable(firstScalar, field.getData(firstScalar).getName(), field.getData(firstScalar).getMinv(), field.getData(firstScalar).getMaxv()));                
                    variables.add(new PCVariable(secondScalar, field.getData(secondScalar).getName(), field.getData(secondScalar).getMinv(), field.getData(secondScalar).getMaxv()));                
                    break;
                case GEOMETRY_TYPE_3D:
                    variables.add(new PCVariable(firstScalar, firstScalar, 
                            field.getData(firstScalar).getName(), field.getData(firstScalar).getName(), 
                            field.getData(firstScalar).getMinv(), field.getData(firstScalar).getMaxv(),
                            field.getData(firstScalar).getMinv(), field.getData(firstScalar).getMaxv()));                
                    variables.add(new PCVariable(secondScalar, secondScalar,
                            field.getData(secondScalar).getName(), field.getData(secondScalar).getName(), 
                            field.getData(secondScalar).getMinv(), field.getData(secondScalar).getMaxv(),
                            field.getData(secondScalar).getMinv(), field.getData(secondScalar).getMaxv()));                
                    break;
            }
        } else if(nScalarComponents > 0) {
            switch(geometryType) {
                case GEOMETRY_TYPE_2D:
                    variables.add(new PCVariable(firstScalar, field.getData(firstScalar).getName(), field.getData(firstScalar).getMinv(), field.getData(firstScalar).getMaxv()));                
                    variables.add(new PCVariable(firstScalar, field.getData(firstScalar).getName(), field.getData(firstScalar).getMinv(), field.getData(firstScalar).getMaxv()));                
                    break;
                case GEOMETRY_TYPE_3D:
                    variables.add(new PCVariable(firstScalar, firstScalar, 
                            field.getData(firstScalar).getName(), field.getData(firstScalar).getName(), 
                            field.getData(firstScalar).getMinv(), field.getData(firstScalar).getMaxv(),
                            field.getData(firstScalar).getMinv(), field.getData(firstScalar).getMaxv()));                
                    variables.add(new PCVariable(firstScalar, firstScalar, 
                            field.getData(firstScalar).getName(), field.getData(firstScalar).getName(), 
                            field.getData(firstScalar).getMinv(), field.getData(firstScalar).getMaxv(),
                            field.getData(firstScalar).getMinv(), field.getData(firstScalar).getMaxv()));                
                    break;
            }
        }
        setVariables(variables);        
        setDownsize(getDefaultDownsize().clone());
        setPlanesTransparency(0.5f);
        setLines3DType(ParallelCoordinatesObject3D.LINES3D_TYPE_LINES);
        active = oldActive;
        parameterActive = oldParameterActive;
    }
    
}