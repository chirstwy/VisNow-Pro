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

import java.util.ArrayList;
import javax.media.j3d.J3DGraphics2D;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import pl.edu.icm.visnow.datasets.Field;
import pl.edu.icm.visnow.datasets.RegularField;
import pl.edu.icm.visnow.engine.core.InputEgg;
import pl.edu.icm.visnow.engine.core.OutputEgg;
import pl.edu.icm.visnow.engine.core.ParameterChangeListener;
import pl.edu.icm.visnow.geometries.objects.DataMappedGeometryObject;
import pl.edu.icm.visnow.geometries.parameters.DataMappingParams;
import pl.edu.icm.visnow.geometries.utils.ColorMapper;
import pl.edu.icm.visnow.geometries.utils.transform.LocalToWindow;
import pl.edu.icm.visnow.geometries.viewer3d.eventslisteners.render.RenderEvent;
import pl.edu.icm.visnow.geometries.viewer3d.eventslisteners.render.RenderEventListener;
import pl.edu.icm.visnow.lib.templates.visualization.modules.VisualizationModule;
import pl.edu.icm.visnow.lib.types.VNField;
import pl.edu.icm.visnow.lib.types.VNGeometryObject;
import pl.edu.icm.visnow.lib.utils.SwingInstancer;
import pl.edu.icm.visnow.lib.utils.geometry2D.GeometryObject2D;

/**
 * @author Bartosz Borucki (babor@icm.edu.pl) Warsaw University,
 * Interdisciplinary Centre for Mathematical and Computational Modelling 13
 * October 2013
 */
public class ParallelCoordinates extends VisualizationModule {

    public static InputEgg[] inputEggs = null;
    public static OutputEgg[] outputEggs = null;
    private ParallelCoordinatesGUI ui;
    private ParallelCoordinatesParams params;
    private boolean fromGUI = false;
    private Field inField = null;
    private ParallelCoordinatesObject3D geometry3D = null;
    private GeometryObject2D geometry2D = null;

    public ParallelCoordinates() {
        parameters = params = new ParallelCoordinatesParams();
        params.addParameterChangelistener(new ParameterChangeListener() {
            @Override
            public void parameterChanged(String name) {
                fromGUI = true;

                if ("geometryType".equals(name)) {
                    params.reset(inField, params.getGeometryType());
                    ui.updateGUI();
                    update();
                }

                if ("nVariables".equals(name) && (geometry3D != null || geometry2D != null)) {
                    updateNVariables();
                }

                if ("variables".equals(name) && (geometry3D != null || geometry2D != null)) {
                    ArrayList<PCVariable> variables = params.getVariables();
                    if (variables.size() != params.getNVariables() || inField == null) {
                        return;
                    }
                    ui.updateVariablesGUI();
                    update();
                }

                if ("downsize".equals(name) && (geometry3D != null || geometry2D != null)) {
                    update();
                }

                if ("selectionLogic".equals(name)) {
                    update();
                }





                if ("selectionType".equals(name) && geometry2D != null && geometry2D instanceof ParallelCoordinatesObject2D) {
                    ((ParallelCoordinatesObject2D) geometry2D).setSelectionType(params.getSelectionType());
                }

                if ("axesColor".equals(name) && geometry2D != null && geometry2D instanceof ParallelCoordinatesObject2D) {
                    ((ParallelCoordinatesObject2D) geometry2D).setAxesColor(params.getAxesColor());
                }

                if ("lineColor".equals(name) && geometry2D != null && geometry2D instanceof ParallelCoordinatesObject2D) {
                    ((ParallelCoordinatesObject2D) geometry2D).setLineColor(params.getLineColor());
                }

                if ("selectionColor".equals(name) && geometry2D != null && geometry2D instanceof ParallelCoordinatesObject2D) {
                    ((ParallelCoordinatesObject2D) geometry2D).setSelectionColor(params.getSelectionColor());
                }

                if ("scale".equals(name) && geometry2D != null && geometry2D instanceof ParallelCoordinatesObject2D) {
                    ((ParallelCoordinatesObject2D) geometry2D).setDistanceScale(params.getScale());
                }

                if ("dataMapping".equals(name) && geometry2D != null && geometry2D instanceof ParallelCoordinatesObject2D) {
                    ((ParallelCoordinatesObject2D) geometry2D).setDataMappingType(params.getDataMapping());
                }

                if ("selectionDim".equals(name) && geometry2D != null && geometry2D instanceof ParallelCoordinatesObject2D) {
                    ((ParallelCoordinatesObject2D) geometry2D).setSelectionDim(params.getSelectionDim());
                }




                if ("selectionType".equals(name) && geometry3D != null) {
                    geometry3D.setSelectionType(params.getSelectionType());
                }

                if ("axesColor".equals(name) && geometry3D != null) {
                    geometry3D.setAxesColor(params.getAxesColor());
                }

                if ("lineColor".equals(name) && geometry3D != null) {
                    geometry3D.setLineColor(params.getLineColor());
                }

                if ("selectionColor".equals(name) && geometry3D != null) {
                    geometry3D.setSelectionColor(params.getSelectionColor());
                }

                if ("scale".equals(name) && geometry3D != null) {
                    geometry3D.setDistanceScale(params.getScale());
                }

                if ("dataMapping".equals(name) && geometry3D != null) {
                    geometry3D.setDataMappingType(params.getDataMapping());
                }

                if ("selectionDim".equals(name) && geometry3D != null) {
                    geometry3D.setSelectionDim(params.getSelectionDim());
                }

                if ("planesTransparency".equals(name) && geometry3D != null) {
                    geometry3D.setPlaneTransparency(params.getPlanesTransparency());
                }

                if ("selectionAreaTransparency".equals(name) && geometry3D != null) {
                    geometry3D.setSelectionAreaTransparency(params.getSelectionAreaTransparency());
                }

                if ("lines3DType".equals(name) && geometry3D != null) {
                    geometry3D.setLinesType(params.getLines3DType());
                }

                if ("lineWidth".equals(name) && geometry3D != null) {
                    geometry3D.setLineWidth(params.getLineWidth());
                }



//                if ("clipColorRange".equals(name) && outObj != null && outObj instanceof ParallelCoordinatesObject2D) {
//                    int colorComponent = dataMappingParams.getColorMap0Params().getDataComponent();
//                    if(params.isClipColorRange()) {
//                        int iVar = -1;
//                        ArrayList<PCVariable> variables = params.getVariables();
//                        for (int i = 0; i < variables.size(); i++) {
//                            if(variables.get(i).isSelected() && variables.get(i).getComponent() == colorComponent) {
//                                iVar = i;
//                                break;
//                            }                                
//                        }
//                        if(iVar != -1) {
//                            dataMappingParams.getColorMap0Params().setDataMinMax(variables.get(iVar).getLow(), variables.get(iVar).getUp());                
//                        }
//                    } else {
//                        dataMappingParams.getColorMap0Params().setDataMinMax(inField.getData(colorComponent).getMinv(), inField.getData(colorComponent).getMaxv());                                        
//                    }
//                    ui.setColormapRangeSliderEnabled(params.isClipColorRange());
//                    update();
//                }


                fromGUI = false;
            }
        });
        params.getFontParams().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (geometry3D != null) {
                    geometry3D.setFontParams(params.getFontParams());
                }
            }
        });
        dataMappingParams.addRenderEventListener(new RenderEventListener() {
            @Override
            public void renderExtentChanged(RenderEvent e) {
                if ((e.getUpdateExtent() & (RenderEvent.COLORS | RenderEvent.TRANSPARENCY | RenderEvent.TEXTURE)) != 0) {
                    if (geometry2D != null && geometry2D instanceof ParallelCoordinatesObject2D) {
                        byte[] colors = ((ParallelCoordinatesObject2D) geometry2D).getColors();
                        colors = ColorMapper.map(inField, dataMappingParams, colors);
                        ((ParallelCoordinatesObject2D) geometry2D).setColors(colors);
                    }
                }

                if (geometry3D != null) {
                    byte[] colors = geometry3D.getColors();
                    colors = ColorMapper.map(inField, dataMappingParams, colors);
                    geometry3D.setColors(colors);
                }

            }
        });

        SwingInstancer.swingRunAndWait(new Runnable() {
            @Override
            public void run() {
                ui = new ParallelCoordinatesGUI();
                ui.setParams(params, dataMappingParams, renderingParams);
                setPanel(ui);
            }
        });
    }

    @Override
    public void onInitFinishedLocal() {
        outObj = new ParallelCoordinatesGeometryObjectWrapper();
        outObj.setCreator(this);
        setOutputValue("outObj", new VNGeometryObject(outObj, outObj2DStruct));
    }

    @Override
    public void onActive() {
        if (!fromGUI) {
            if (getInputFirstValue("inField") == null) {
                return;
            }

            Field field = ((VNField) getInputFirstValue("inField")).getField();

            if (field == null) {
                ui.setInField(field);
                inField = field;
                update();
                return;
            }

            if (inField == null || !inField.isDataCompatibleWith(field)) {
                params.setParamaeterActive(false);
                params.reset(field, params.getGeometryType());
                params.setParamaeterActive(true);
                ui.setInField(field);
                inField = field;
                update();
                return;
            }

            ui.setInField(field);
            inField = field;
        }

        update();
        fromGUI = false;
    }

    private void update() {
        if (inField == null) {
            return;
        }

        outObj.clearAllGeometry();       
        if (params.getGeometryType() == ParallelCoordinatesParams.GEOMETRY_TYPE_2D) {
            geometry2D = createGeometry2D(inField, params, dataMappingParams);
            geometry3D = null;
        } else if (params.getGeometryType() == ParallelCoordinatesParams.GEOMETRY_TYPE_3D) {
            geometry2D = new GeometryObject2D();
            geometry3D = createGeometry3D(inField, params, dataMappingParams);
        }
        ((ParallelCoordinatesGeometryObjectWrapper)outObj).setParallelCoordinatesObject3D(geometry3D);
        outObj2DStruct.setGeometryObject2D(geometry2D);
    }

    public static ParallelCoordinatesObject2D createGeometry2D(Field field, ParallelCoordinatesParams params, DataMappingParams dataMappingParams) {
        if (field == null || params == null) {
            return null;
        }

        int nVariables = params.getNVariables();
        int nNodes = field.getNNodes();
        ArrayList<PCVariable> variables = params.getVariables();
        if (variables == null || variables.size() != nVariables) {
            return null;
        }

        String[] nameList = new String[nVariables];
        float[][] dataList = new float[nVariables][];
        float[] minList = new float[nVariables];
        float[] lowList = new float[nVariables];
        float[] upList = new float[nVariables];
        float[] maxList = new float[nVariables];
        boolean[] selectionList = new boolean[nVariables];
        boolean[] visibleList = new boolean[nVariables];
        for (int i = 0; i < nVariables; i++) {
            nameList[i] = variables.get(i).getXName();
            dataList[i] = field.getData(variables.get(i).getXComponent()).getFData();
            minList[i] = variables.get(i).getXMin();
            lowList[i] = variables.get(i).getXLow();
            upList[i] = variables.get(i).getXUp();
            maxList[i] = variables.get(i).getXMax();
            selectionList[i] = variables.get(i).isSelected();
            visibleList[i] = variables.get(i).isVisible();
        }

        byte[] selectionMask = computeSelectionMask2D(field, params.getDownsize(), nVariables, variables, dataList, minList, lowList, upList, maxList, params.getSelectionLogic());
        byte[] colors = mapColors(field, dataMappingParams, params.isClipColorRange(), variables);

        return new ParallelCoordinatesObject2D(nVariables, nNodes,
                nameList, dataList,
                minList, lowList, upList, maxList,
                selectionMask, selectionList, params.getSelectionType(),
                params.getScale(),
                params.getAxesColor(),
                params.getDataMapping(),
                params.getLineColor(), params.getSelectionColor(),
                colors, params.getSelectionDim(),
                visibleList);
    }

    private static byte[] computeSelectionMask2D(Field field, int[] downsize, int nVariables, ArrayList<PCVariable> variables,
            float[][] dataList, float[] minList, float[] lowList, float[] upList, float[] maxList, int selectionLogic) {
        //prepare selection mask
        //selection mask: 0 = out of min/max range, 1 = out of selection range, 2 = in selection range 
        int nNodes = field.getNNodes();
        byte[] selectionMask = new byte[nNodes];

        if (field instanceof RegularField) {
            int[] dims = ((RegularField) field).getDims();
            switch (dims.length) {
                case 3:
                    for (int k = 0; k < dims[2]; k += downsize[2]) {
                        for (int j = 0; j < dims[1]; j += downsize[1]) {
                            for (int i = 0; i < dims[0]; i += downsize[0]) {
                                selectionMask[k * dims[0] * dims[1] + j * dims[0] + i] = 1;
                            }
                        }
                    }
                    break;
                case 2:
                    for (int j = 0; j < dims[1]; j += downsize[1]) {
                        for (int i = 0; i < dims[0]; i += downsize[0]) {
                            selectionMask[j * dims[0] + i] = 1;
                        }
                    }
                    break;
                case 1:
                    for (int i = 0; i < dims[0]; i += downsize[0]) {
                        selectionMask[i] = 1;
                    }
                    break;
            }
        } else {
            for (int i = 0; i < selectionMask.length; i += downsize[0]) {
                selectionMask[i] = 1;
            }
        }

        for (int i = 0; i < nVariables; i++) {
            for (int j = 0; j < selectionMask.length; j++) {
                if (selectionMask[j] != 0 && !(dataList[i][j] >= minList[i] && dataList[i][j] <= maxList[i])) {
                    selectionMask[j] = 0;
                }
            }
        }

        boolean anySelection = false;
        int firstSelected = -1;
        for (int i = 0; i < nVariables; i++) {
            if (variables.get(i).isSelected()) {
                anySelection = true;
                firstSelected = i;
                break;
            }
        }
        if (anySelection) {
            switch (selectionLogic) {
                case ParallelCoordinatesParams.SELECTION_LOGIC_AND:
                    for (int j = 0; j < nNodes; j++) {
                        if (selectionMask[j] != 1) {
                            continue;
                        }

                        if (dataList[firstSelected][j] >= lowList[firstSelected] && dataList[firstSelected][j] <= upList[firstSelected]) {
                            selectionMask[j] = 2;
                        }

                        if (selectionMask[j] != 2) {
                            continue;
                        }

                        for (int i = firstSelected + 1; i < nVariables; i++) {
                            if (variables.get(i).isSelected() && !(dataList[i][j] >= lowList[i] && dataList[i][j] <= upList[i])) {
                                selectionMask[j] = 1;
                                break;
                            }
                        }
                    }
                    break;
                case ParallelCoordinatesParams.SELECTION_LOGIC_OR:
                    for (int j = 0; j < nNodes; j++) {
                        for (int i = 0; i < nVariables; i++) {
                            if (variables.get(i).isSelected() && selectionMask[j] > 0 && (dataList[i][j] >= lowList[i] && dataList[i][j] <= upList[i])) {
                                selectionMask[j] = 2;
                                break;
                            }
                        }
                    }
                    break;
            }
        }
        return selectionMask;
    }

    private static byte[] mapColors(Field field, DataMappingParams dataMappingParams, boolean clipColorRange, ArrayList<PCVariable> variables) {
        int nNodes = field.getNNodes();
        byte[] colors = new byte[4 * nNodes];
//        if(clipColorRange) {
//            int colorComponent = dataMappingParams.getColorMap0Params().getDataComponent();
//            int iVar = -1;
//            for (int i = 0; i < variables.size(); i++) {
//                if(variables.get(i).isSelected() && variables.get(i).getComponent() == colorComponent) {
//                    iVar = i;
//                    break;
//                }                                
//            }
//            if(iVar != -1) {
//                //map
//                dataMappingParams.getColorMap0Params().setDataMinMax(variables.get(iVar).getLow(), variables.get(iVar).getUp());                
//            }
//        }
        colors = ColorMapper.map(field, dataMappingParams, colors);
        return colors;
    }

    public ParallelCoordinatesObject3D createGeometry3D(Field field, ParallelCoordinatesParams params, DataMappingParams dataMappingParams) {
        if (field == null || params == null) {
            return null;
        }

        int nVariables = params.getNVariables();
        int nNodes = field.getNNodes();
        ArrayList<PCVariable> variables = params.getVariables();
        if (variables == null || variables.size() != nVariables) {
            return null;
        }

        String[][] nameList = new String[2][nVariables];
        float[][][] dataList = new float[2][nVariables][];
        float[][] minList = new float[2][nVariables];
        float[][] lowList = new float[2][nVariables];
        float[][] upList = new float[2][nVariables];
        float[][] maxList = new float[2][nVariables];
        boolean[] selectionList = new boolean[nVariables];
        boolean[] visibleList = new boolean[nVariables];
        for (int i = 0; i < nVariables; i++) {
            nameList[0][i] = variables.get(i).getXName();
            nameList[1][i] = variables.get(i).getYName();
            dataList[0][i] = field.getData(variables.get(i).getXComponent()).getFData();
            dataList[1][i] = field.getData(variables.get(i).getYComponent()).getFData();
            minList[0][i] = variables.get(i).getXMin();
            lowList[0][i] = variables.get(i).getXLow();
            upList[0][i] = variables.get(i).getXUp();
            maxList[0][i] = variables.get(i).getXMax();
            minList[1][i] = variables.get(i).getYMin();
            lowList[1][i] = variables.get(i).getYLow();
            upList[1][i] = variables.get(i).getYUp();
            maxList[1][i] = variables.get(i).getYMax();
            selectionList[i] = variables.get(i).isSelected();
            visibleList[i] = variables.get(i).isVisible();
        }

        byte[] selectionMask = computeSelectionMask3D(field, params.getDownsize(), nVariables, variables, dataList, minList, lowList, upList, maxList, params.getSelectionLogic());
        byte[] colors = mapColors(field, dataMappingParams, params.isClipColorRange(), variables);

        return new ParallelCoordinatesObject3D(nVariables, nNodes,
                nameList, dataList,
                minList, lowList, upList, maxList,
                selectionMask, selectionList, params.getSelectionType(),
                params.getScale(),
                params.getAxesColor(),
                params.getDataMapping(),
                params.getLineColor(), params.getSelectionColor(),
                colors, params.getSelectionDim(),
                visibleList,
                params.getLines3DType(), params.getLineWidth(),
                params.getPlanesTransparency(), params.getSelectionAreaTransparency(),
                params.getFontParams(),
                outObj);
    }

    private static byte[] computeSelectionMask3D(Field field, int[] downsize, int nVariables, ArrayList<PCVariable> variables,
            float[][][] dataList, float[][] minList, float[][] lowList, float[][] upList, float[][] maxList, int selectionLogic) {
        //prepare selection mask
        //selection mask: 0 = out of min/max range, 1 = out of selection range, 2 = in selection range 
        int nNodes = field.getNNodes();
        byte[] selectionMask = new byte[nNodes];

        if (field instanceof RegularField) {
            int[] dims = ((RegularField) field).getDims();
            switch (dims.length) {
                case 3:
                    for (int k = 0; k < dims[2]; k += downsize[2]) {
                        for (int j = 0; j < dims[1]; j += downsize[1]) {
                            for (int i = 0; i < dims[0]; i += downsize[0]) {
                                selectionMask[k * dims[0] * dims[1] + j * dims[0] + i] = 1;
                            }
                        }
                    }
                    break;
                case 2:
                    for (int j = 0; j < dims[1]; j += downsize[1]) {
                        for (int i = 0; i < dims[0]; i += downsize[0]) {
                            selectionMask[j * dims[0] + i] = 1;
                        }
                    }
                    break;
                case 1:
                    for (int i = 0; i < dims[0]; i += downsize[0]) {
                        selectionMask[i] = 1;
                    }
                    break;
            }
        } else {
            for (int i = 0; i < selectionMask.length; i += downsize[0]) {
                selectionMask[i] = 1;
            }
        }

        for (int i = 0; i < nVariables; i++) {
            for (int j = 0; j < selectionMask.length; j++) {
                if (selectionMask[j] != 0 && (!(dataList[0][i][j] >= minList[0][i] && dataList[0][i][j] <= maxList[0][i]) || !(dataList[1][i][j] >= minList[1][i] && dataList[1][i][j] <= maxList[1][i]))) {
                    selectionMask[j] = 0;
                }
            }
        }

        boolean anySelection = false;
        int firstSelected = -1;
        for (int i = 0; i < nVariables; i++) {
            if (variables.get(i).isSelected()) {
                anySelection = true;
                firstSelected = i;
                break;
            }
        }
        if (anySelection) {
            switch (selectionLogic) {
                case ParallelCoordinatesParams.SELECTION_LOGIC_AND:
                    for (int j = 0; j < nNodes; j++) {
                        if (selectionMask[j] != 1) {
                            continue;
                        }

                        if (dataList[0][firstSelected][j] >= lowList[0][firstSelected]
                                && dataList[0][firstSelected][j] <= upList[0][firstSelected]
                                && dataList[1][firstSelected][j] >= lowList[1][firstSelected]
                                && dataList[1][firstSelected][j] <= upList[1][firstSelected]) {
                            selectionMask[j] = 2;
                        }

                        if (selectionMask[j] != 2) {
                            continue;
                        }

                        for (int i = firstSelected + 1; i < nVariables; i++) {
                            if (variables.get(i).isSelected()
                                    && (!(dataList[0][i][j] >= lowList[0][i] && dataList[0][i][j] <= upList[0][i])
                                    || !(dataList[1][i][j] >= lowList[1][i] && dataList[1][i][j] <= upList[1][i]))) {
                                selectionMask[j] = 1;
                                break;
                            }
                        }
                    }
                    break;
                case ParallelCoordinatesParams.SELECTION_LOGIC_OR:
                    for (int j = 0; j < nNodes; j++) {
                        for (int i = 0; i < nVariables; i++) {
                            if (variables.get(i).isSelected() && selectionMask[j] > 0
                                    && (dataList[0][i][j] >= lowList[0][i] && dataList[0][i][j] <= upList[0][i])
                                    && (dataList[1][i][j] >= lowList[1][i] && dataList[1][i][j] <= upList[1][i])) {
                                selectionMask[j] = 2;
                                break;
                            }
                        }
                    }
                    break;
            }
        }
        return selectionMask;
    }

    private void updateNVariables() {
        int N = params.getNVariables();
        ArrayList<PCVariable> variables = params.getVariables();
        int diff = Math.abs(N - variables.size());
        if (variables.size() < N) {
            for (int i = 0; i < diff; i++) {
                int[] sci = inField.getScalarDataIndices();
                int n = inField.getFirstScalarComponentIndex();
                if (sci == null || n == -1) {
                    return;
                }
//                if(sci.length > variables.size()) {
//                    n = sci[variables.size()];                    
//                }
                switch (params.getGeometryType()) {
                    case ParallelCoordinatesParams.GEOMETRY_TYPE_2D:
                        variables.add(new PCVariable(n, inField.getData(n).getName(), inField.getData(n).getMinv(), inField.getData(n).getMaxv()));
                        break;
                    case ParallelCoordinatesParams.GEOMETRY_TYPE_3D:
                        variables.add(new PCVariable(n, n, inField.getData(n).getName(), inField.getData(n).getName(),
                                inField.getData(n).getMinv(), inField.getData(n).getMaxv(),
                                inField.getData(n).getMinv(), inField.getData(n).getMaxv()));
                        break;
                }
            }
        } else if (variables.size() > N) {
            for (int i = 0; i < diff; i++) {
                variables.remove(variables.size() - 1);
            }
        }
        params.setVariables(variables);
    }

    private class ParallelCoordinatesGeometryObjectWrapper extends DataMappedGeometryObject {

        private ParallelCoordinatesObject3D pco = null;

        public ParallelCoordinatesGeometryObjectWrapper() {
            super("ParallelCoordinates");
        }

        public void setParallelCoordinatesObject3D(ParallelCoordinatesObject3D pco) {
            this.pco = pco;
            if(pco != null)
                this.addNode(pco);
        }

        @Override
        public void drawLocal2D(J3DGraphics2D vGraphics, LocalToWindow ltw, int w, int h) {
            if (pco == null) {
                return;
            }
            pco.drawLocal2DWrapper(vGraphics, ltw, w, h);
        }
    }
}
