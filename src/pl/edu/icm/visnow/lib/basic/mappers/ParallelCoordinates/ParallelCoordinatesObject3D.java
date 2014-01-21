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
import java.awt.Font;
import javax.media.j3d.Billboard;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Geometry;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.IndexedLineStripArray;
import javax.media.j3d.J3DGraphics2D;
import javax.media.j3d.Node;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.media.j3d.TriangleStripArray;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import pl.edu.icm.visnow.geometries.objects.GeometryObject;
import pl.edu.icm.visnow.geometries.objects.TextBillboard;
import pl.edu.icm.visnow.geometries.objects.generics.OpenAppearance;
import pl.edu.icm.visnow.geometries.objects.generics.OpenBranchGroup;
import pl.edu.icm.visnow.geometries.objects.generics.OpenLineAttributes;
import pl.edu.icm.visnow.geometries.objects.generics.OpenMaterial;
import pl.edu.icm.visnow.geometries.objects.generics.OpenShape3D;
import pl.edu.icm.visnow.geometries.objects.generics.OpenTransformGroup;
import pl.edu.icm.visnow.geometries.objects.generics.OpenTransparencyAttributes;
import pl.edu.icm.visnow.geometries.parameters.FontParams;
import pl.edu.icm.visnow.geometries.utils.transform.LocalToWindow;
import pl.edu.icm.visnow.lib.utils.VNFloatFormatter;
import pl.edu.icm.visnow.lib.utils.geometry2D.CXYZString;

/**
 * @author Bartosz Borucki (babor@icm.edu.pl) Warsaw University,
 * Interdisciplinary Centre for Mathematical and Computational Modelling 18
 * October 2013
 */
public class ParallelCoordinatesObject3D extends OpenBranchGroup {

    public static final int LINES3D_TYPE_LINES = 0;
    public static final int LINES3D_TYPE_PIPES = 1;
    private float planesTransparency = 0.5f;
    private float selectionPlaneTransparency = 0.5f;
    private int linesType = LINES3D_TYPE_LINES;
    private float lineWidth = 0.25f;
    private int nVariables = 0;
    private int nVariablesVisible = 0;
    private int nVariablesSelected = 0;
    private int firstVariableVisible = -1;
    private int nNodes = 0;
    private float[][] extents;
    private String[][] nameList;
    private float[][][] dataList;
    private float[][] minList;
    private float[][] lowList;
    private float[][] upList;
    private float[][] maxList;
    private byte[] selectionMask;
    private boolean[] selectionList;
    private boolean[] visibleList;
    private byte[] colors;
    private int selectionType = ParallelCoordinatesObject2D.SELECTION_TYPE_HIGHLIGHT;
    private float variableStep = 0;
    private Color axesColor = Color.WHITE;
    private Color lineColor = Color.BLUE;
    private Color selectionColor = Color.RED;
    private int dataMapping = ParallelCoordinatesObject2D.DATA_MAPPING_DIRECT;
    private float selectionDim = 1.0f;
    private float xSize = 1.0f;
    private float ySize = 1.0f;
    private float zSize = 1.0f;
    private OpenAppearance planeApp = new OpenAppearance();
    private OpenAppearance planeOutlineApp = new OpenAppearance();
    private OpenAppearance selectionOutlineApp = new OpenAppearance();
    private OpenAppearance selectionPlaneApp = new OpenAppearance();
    private OpenAppearance lineApp = new OpenAppearance();
    private OpenMaterial mat = new OpenMaterial();
    private OpenLineAttributes planeOutlineAttr = new OpenLineAttributes(1.f, OpenLineAttributes.PATTERN_SOLID, true);
    private OpenLineAttributes selectionOutlineAttr = new OpenLineAttributes(1.f, OpenLineAttributes.PATTERN_DASH, true);
    private OpenLineAttributes lineAttr = new OpenLineAttributes(1.f, OpenLineAttributes.PATTERN_SOLID, true);
    private TransparencyAttributes planeTransparencyAttributes = new OpenTransparencyAttributes(TransparencyAttributes.NICEST, 0.5f);
    private TransparencyAttributes selectionPlaneTransparencyAttributes = new OpenTransparencyAttributes(TransparencyAttributes.NICEST, 0.5f);
    private TransparencyAttributes noTransparencyAttributes = new OpenTransparencyAttributes(TransparencyAttributes.NONE, 0.0f);
    private OpenBranchGroup mbg;
    private OpenTransformGroup otg;
    private Transform3D tr = new Transform3D();
    private OpenBranchGroup[] planeGroups;
    private OpenShape3D[] planes;
    private float[] planeNormals = new float[]{0.0f, 0.0f, 1.0f,
        0.0f, 0.0f, 1.0f,
        0.0f, 0.0f, 1.0f,
        0.0f, 0.0f, 1.0f};
    private float[] planeColors;
    private int[] planeStrips = new int[]{4};
    private float[][] planeCoords;
    private TriangleStripArray[] planeGeometries;
    private OpenShape3D[] planeOutlines;
    private int[] planeOutlineStrips = new int[]{2, 2, 2, 2};
    private int[] planeOutlineIndices = new int[]{0, 1, 1, 3, 3, 2, 2, 0};
    private IndexedLineStripArray[] planeOutlineGeometries;
    private OpenBranchGroup[] selectionGroups;
    private OpenShape3D[] selectionOutlines;
    private int[] selectionOutlineStrips = new int[]{2, 2, 2, 2};
    private int[] selectionOutlineIndices = new int[]{0, 1, 2, 3, 4, 5, 6, 7};
    private IndexedLineStripArray[] selectionOutlineGeometries;
    private float[][] selectionOutlineCoords;
    private float[] selectionOutlineColors;
    private OpenShape3D[] selectionPlanes;
    private float[] selectionPlaneColors;
    private float[][] selectionPlaneCoords;
    private TriangleStripArray[] selectionPlaneGeometries;
    private OpenBranchGroup linesGroup;
    private OpenShape3D[] lines;
    private IndexedLineStripArray[] lineGeometries;
    private int nLineIndices = 0;
    private int nLineVertices = 0;
    private int[] lineStrips;
    private int[] lineIndices;
    private float[][] lineCoords;
    private byte[][] lineColors;
    private OpenBranchGroup[] textGroups;
    private FontParams fontParams;
    private CXYZString[][] texts2D = null;
    private BoundingSphere bSphere = null;
    private GeometryObject parentObject;

    public ParallelCoordinatesObject3D(int nVariables, int nNodes,
            String[][] nameList, float[][][] dataList,
            float[][] minList, float[][] lowList, float[][] upList, float[][] maxList,
            byte[] selectionMask, boolean[] selectionList, int selectionType,
            float distanceScale,
            Color axesColor,
            int dataMappingType,
            Color lineColor, Color selectionColor, byte[] colors, float selectionDim,
            boolean[] visibleList,
            int linesType, float lineWidth,
            float planesTransparency, float selectionPlaneTransparency,
            FontParams fontParams,
            GeometryObject parentObject) {
        super("ParallelCoordinatesObject3D");
        this.parentObject = parentObject;
        this.fontParams = fontParams;
        this.planesTransparency = planesTransparency;
        this.selectionPlaneTransparency = selectionPlaneTransparency;
        this.linesType = linesType;
        this.lineWidth = lineWidth;
        this.nVariables = nVariables;
        this.nNodes = nNodes;
        this.nameList = nameList;
        this.dataList = dataList;
        this.minList = minList;
        this.lowList = lowList;
        this.upList = upList;
        this.maxList = maxList;
        this.selectionMask = selectionMask;
        this.selectionList = selectionList;
        this.selectionType = selectionType;
        this.axesColor = axesColor;
        this.dataMapping = dataMappingType;
        this.lineColor = lineColor;
        this.selectionColor = selectionColor;
        this.colors = colors;
        this.selectionDim = selectionDim;
        this.visibleList = visibleList;
        this.nVariablesVisible = 0;
        for (int i = 0; i < visibleList.length; i++) {
            if (visibleList[i]) {
                if (firstVariableVisible == -1) {
                    firstVariableVisible = i;
                }
                nVariablesVisible++;
            }
        }
        this.nVariablesSelected = 0;
        for (int i = 0; i < selectionList.length; i++) {
            if (selectionList[i]) {
                nVariablesSelected++;
            }
        }

        this.xSize = 1.0f;
        this.ySize = 1.0f;
        this.variableStep = 1.0f;
        this.zSize = (nVariablesVisible - 1) * variableStep;
        float w2 = xSize / 2.0f;
        float h2 = ySize / 2.0f;
        float d2 = zSize / 2.0f;
        this.extents = new float[][]{{-w2, -h2, -d2}, {w2, h2, d2}};
        if(parentObject != null)
            parentObject.setExtents(extents);        
        updateBoundingSphere();
        prepare3DParams();
        fullUpdate();
    }

    private void fullUpdate() {
        countVisibleLines();
        createShapes();
        updateGeometry();
        updateColors();
        updateTexts();
    }

    private void createShapes() {
        this.removeAllChildren();

        mbg = new OpenBranchGroup();
        otg = new OpenTransformGroup();
        tr.rotY(Math.PI / 2.0);
        otg.setTransform(tr);

        //---------------------planes and outlines-----------------------------------
        planeGroups = new OpenBranchGroup[nVariablesVisible];

        planes = new OpenShape3D[nVariablesVisible];
        planeCoords = new float[nVariablesVisible][];
        planeGeometries = new TriangleStripArray[nVariablesVisible];

        planeOutlines = new OpenShape3D[nVariablesVisible];
        planeOutlineGeometries = new IndexedLineStripArray[nVariablesVisible];

        for (int i = 0; i < nVariablesVisible; i++) {
            createPlane(i);
            planeGroups[i] = new OpenBranchGroup();
            planeGroups[i].addChild(planes[i]);
            planeGroups[i].addChild(planeOutlines[i]);
            otg.addChild(planeGroups[i]);
        }

        //---------------------selection-----------------------------------
        selectionGroups = new OpenBranchGroup[nVariablesSelected];
        selectionOutlines = new OpenShape3D[nVariablesSelected];
        selectionOutlineGeometries = new IndexedLineStripArray[nVariablesSelected];
        selectionOutlineCoords = new float[nVariablesSelected][];

        selectionPlanes = new OpenShape3D[nVariablesSelected];
        selectionPlaneCoords = new float[nVariablesSelected][];
        selectionPlaneGeometries = new TriangleStripArray[nVariablesSelected];

        for (int i = 0; i < nVariablesSelected; i++) {
            createSelection(i);
            selectionGroups[i] = new OpenBranchGroup();
            selectionGroups[i].addChild(selectionOutlines[i]);
            selectionGroups[i].addChild(selectionPlanes[i]);
            otg.addChild(selectionGroups[i]);
        }

        //--------------------texts---------------------
        textGroups = new OpenBranchGroup[nVariablesVisible];
        for (int i = 0; i < nVariablesVisible; i++) {
            textGroups[i] = new OpenBranchGroup();
            otg.addChild(textGroups[i]);
        }

        //---------------------plot lines-----------------------------------        
        if (nNodesVisible > 0) {
            switch (linesType) {
                case LINES3D_TYPE_LINES:
                    nLineVertices = nVariablesVisible;
                    nLineIndices = 2 * (nVariablesVisible - 1);
                    lineStrips = new int[]{nLineVertices};            
                    lineIndices = new int[nLineIndices];
                    for (int i = 0; i < nLineIndices; i++) {
                        lineIndices[i] = i;
                    }

                    linesGroup = new OpenBranchGroup();
                    lines = new OpenShape3D[nNodesVisible];
                    lineGeometries = new IndexedLineStripArray[nNodesVisible];
                    lineCoords = new float[nNodesVisible][];

                    otg.addChild(linesGroup);
                    switch (selectionType) {
                        case ParallelCoordinatesObject2D.SELECTION_TYPE_HIGHLIGHT:
                            for (int i = 0, n = 0; i < nNodes; i++) {
                                if (selectionMask[i] == 0) {
                                    continue;
                                }
                                createLine(n);
                                linesGroup.addChild(lines[n]);
                                n++;
                            }
                            break;
                        case ParallelCoordinatesObject2D.SELECTION_TYPE_LIMIT:
                        case ParallelCoordinatesObject2D.SELECTION_TYPE_HIGHLIGHT_AND_LIMIT:
                            for (int i = 0, n = 0; i < nNodes; i++) {
                                if (selectionMask[i] < 2) {
                                    continue;
                                }
                                createLine(n);
                                linesGroup.addChild(lines[n]);
                                n++;
                            }
                            break;
                    }
                    break;
                case LINES3D_TYPE_PIPES:
                    //TODO
                    break;
            }
        }

        //------------------------------------------------------------------
        mbg.addChild(otg);
        this.addChild(mbg);
    }

    private void updateGeometry() {
        updatePlanesGeometry();
        updateSelectionGeometry();
        updateLinesGeometry();
    }

    private void updatePlanesGeometry() {
        for (int i = 0; i < nVariablesVisible; i++) {
            updatePlaneGeometry(i);
        }
    }

    private void updateSelectionGeometry() {
        for (int i = 0, nVis = 0, nSel = 0; i < nVariables; i++) {
            if (!visibleList[i]) {
                continue;
            }
            if (!selectionList[i]) {
                nVis++;
                continue;
            }
            updateSelectionGeometry(nSel, nVis, i);
            nVis++;
            nSel++;
        }
    }

    private void updateLinesGeometry() {
        if (nNodesVisible <= 0) {
            //TODO clear lines??
            return;
        }

        switch (selectionType) {
            case ParallelCoordinatesObject2D.SELECTION_TYPE_HIGHLIGHT:
                for (int i = 0, n = 0; i < nNodes; i++) {
                    if (selectionMask[i] == 0) {
                        continue;
                    }
                    updateLineGeometry(i, n);
                    n++;
                }
                break;
            case ParallelCoordinatesObject2D.SELECTION_TYPE_LIMIT:
            case ParallelCoordinatesObject2D.SELECTION_TYPE_HIGHLIGHT_AND_LIMIT:
                for (int i = 0, n = 0; i < nNodes; i++) {
                    if (selectionMask[i] < 2) {
                        continue;
                    }
                    updateLineGeometry(i, n);
                    n++;
                }
                break;
        }
    }

    private void updateColors() {
        updatePlaneColors();
        updateSelectionColors();
        updateLineColors();
    }

    private void updateTexts() {
        if (parentObject == null || parentObject.getCurrentViewer() == null || parentObject.getLocalToWindow() == null) {
            return;
        }
        fontParams.createFontMetrics(parentObject.getLocalToWindow(),
                parentObject.getCurrentViewer().getWidth(),
                parentObject.getCurrentViewer().getHeight());
        Font font = new Font(fontParams.getFontName(), fontParams.getFontType(), 10);
        Color3f selectionColor3f = new Color3f(this.selectionColor);
        float w2 = xSize / 2.0f;
        float h2 = ySize / 2.0f;
        float z = 0.0f;        
        texts2D = new CXYZString[nVariablesVisible][10];
        for (int i = 0, c = 0 ; i < nVariables; i++) {
            if(!visibleList[i])
                continue;
            z = -zSize / 2.0f + c * variableStep;
            textGroups[c].removeAllChildren();
            float dName = 1.2f;
            float dValues = 1.1f;
            float[] xAxisLabelPoint = new float[]{0.0f, -(dName*h2), z};
            float[] xAxisMinPoint = new float[]{-w2, -(dValues*h2), z};
            float[] xAxisMaxPoint = new float[]{ w2, -(dValues*h2), z};
            float[] xAxisLowPoint = new float[]{ -w2 + 2 * w2 * (lowList[0][i] - minList[0][i]) / (maxList[0][i]-minList[0][i]), -(dValues*h2), z};
            float[] xAxisUpPoint = new float[]{ -w2 + 2 * w2 * (upList[0][i] - minList[0][i]) / (maxList[0][i]-minList[0][i]), -(dValues*h2), z};
            
            float[] yAxisLabelPoint = new float[]{-(dName*w2), 0.0f, z};            
            float[] yAxisMinPoint = new float[]{-(dValues*w2), -h2, z};
            float[] yAxisMaxPoint = new float[]{-(dValues*w2),  h2, z};
            float[] yAxisLowPoint = new float[]{-(dValues*w2), -h2 + 2 * h2 * (lowList[1][i] - minList[1][i]) / (maxList[1][i]-minList[1][i]),  z};
            float[] yAxisUpPoint = new float[]{-(dValues*w2), -h2 + 2 * h2 * (upList[1][i] - minList[1][i]) / (maxList[1][i]-minList[1][i]), z};
            
            if (fontParams.isThreeDimensional()) {
                OpenBranchGroup localGroup = new OpenBranchGroup();
                //xAxis
                localGroup.addChild(TextBillboard.createBillboard(nameList[0][i], fontParams, fontParams.getColor3f(), 0.5f, xAxisLabelPoint, Billboard.ROTATE_ABOUT_POINT, bSphere, true, false, false));
                localGroup.addChild(TextBillboard.createBillboard(VNFloatFormatter.defaultRangeFormatWithIntegers(minList[0][i]), fontParams, fontParams.getColor3f(), 0.5f, xAxisMinPoint, Billboard.ROTATE_ABOUT_POINT, bSphere, true, false, false));
                localGroup.addChild(TextBillboard.createBillboard(VNFloatFormatter.defaultRangeFormatWithIntegers(maxList[0][i]), fontParams, fontParams.getColor3f(), 0.5f, xAxisMaxPoint, Billboard.ROTATE_ABOUT_POINT, bSphere, true, false, false));
                if(selectionList[i]) {
                    localGroup.addChild(TextBillboard.createBillboard(VNFloatFormatter.defaultRangeFormatWithIntegers(lowList[0][i]), fontParams, selectionColor3f, 0.5f, xAxisLowPoint, Billboard.ROTATE_ABOUT_POINT, bSphere, true, false, false));
                    localGroup.addChild(TextBillboard.createBillboard(VNFloatFormatter.defaultRangeFormatWithIntegers( upList[0][i]), fontParams, selectionColor3f, 0.5f, xAxisUpPoint, Billboard.ROTATE_ABOUT_POINT, bSphere, true, false, false));
                }
                
                
                //yAxis
                localGroup.addChild(TextBillboard.createBillboard(nameList[1][i], fontParams, fontParams.getColor3f(), 0.5f, yAxisLabelPoint, Billboard.ROTATE_ABOUT_POINT, bSphere, true, false, true));
                localGroup.addChild(TextBillboard.createBillboard(VNFloatFormatter.defaultRangeFormatWithIntegers(minList[1][i]), fontParams, fontParams.getColor3f(), 0.5f, yAxisMinPoint, Billboard.ROTATE_ABOUT_POINT, bSphere, true, false, false));
                localGroup.addChild(TextBillboard.createBillboard(VNFloatFormatter.defaultRangeFormatWithIntegers(maxList[1][i]), fontParams, fontParams.getColor3f(), 0.5f, yAxisMaxPoint, Billboard.ROTATE_ABOUT_POINT, bSphere, true, false, false));
                if(selectionList[i]) {
                    localGroup.addChild(TextBillboard.createBillboard(VNFloatFormatter.defaultRangeFormatWithIntegers(lowList[1][i]), fontParams, selectionColor3f, 0.5f, yAxisLowPoint, Billboard.ROTATE_ABOUT_POINT, bSphere, true, false, false));
                    localGroup.addChild(TextBillboard.createBillboard(VNFloatFormatter.defaultRangeFormatWithIntegers( upList[1][i]), fontParams, selectionColor3f, 0.5f, yAxisUpPoint, Billboard.ROTATE_ABOUT_POINT, bSphere, true, false, false));
                }
                
                
                textGroups[c].addChild(localGroup);
            } else {
                //for 2D texts remap axes x=z, y=y, z=-x due to 90 rotation of geometry 3D aroud Y axis
                
                //xAxis                
                texts2D[c][0] = new CXYZString(" "+nameList[0][i]+" ", axesColor, xAxisLabelPoint[2], xAxisLabelPoint[1], -xAxisLabelPoint[0], font, 1.0f * fontParams.getSize());
                texts2D[c][1] = new CXYZString(" "+VNFloatFormatter.defaultRangeFormatWithIntegers(minList[0][i])+" ", axesColor, xAxisMinPoint[2], xAxisMinPoint[1], -xAxisMinPoint[0], font, 1.0f * fontParams.getSize());
                texts2D[c][2] = new CXYZString(" "+VNFloatFormatter.defaultRangeFormatWithIntegers(maxList[0][i])+" ", axesColor, xAxisMaxPoint[2], xAxisMaxPoint[1], -xAxisMaxPoint[0], font, 1.0f * fontParams.getSize());
                if(selectionList[i]) {
                    texts2D[c][3] = new CXYZString(" "+VNFloatFormatter.defaultRangeFormatWithIntegers(lowList[0][i])+" ", selectionColor, xAxisLowPoint[2], xAxisLowPoint[1], -xAxisLowPoint[0], font, 1.0f * fontParams.getSize());
                    texts2D[c][4] = new CXYZString(" "+VNFloatFormatter.defaultRangeFormatWithIntegers(upList[0][i])+" ", selectionColor, xAxisUpPoint[2], xAxisUpPoint[1], -xAxisUpPoint[0], font, 1.0f * fontParams.getSize());                    
                }                
                
                //yAxis                
                texts2D[c][5] = new CXYZString(" "+nameList[1][i]+" ", axesColor, yAxisLabelPoint[2], yAxisLabelPoint[1], -yAxisLabelPoint[0], font, 1.0f * fontParams.getSize());
                texts2D[c][6] = new CXYZString(" "+VNFloatFormatter.defaultRangeFormatWithIntegers(minList[1][i])+" ", axesColor, yAxisMinPoint[2], yAxisMinPoint[1], -yAxisMinPoint[0], font, 1.0f * fontParams.getSize());
                texts2D[c][7] = new CXYZString(" "+VNFloatFormatter.defaultRangeFormatWithIntegers(maxList[1][i])+" ", axesColor, yAxisMaxPoint[2], yAxisMaxPoint[1], -yAxisMaxPoint[0], font, 1.0f * fontParams.getSize());
                if(selectionList[i]) {
                    texts2D[c][8] = new CXYZString(" "+VNFloatFormatter.defaultRangeFormatWithIntegers(lowList[1][i])+" ", selectionColor, yAxisLowPoint[2], yAxisLowPoint[1], -yAxisLowPoint[0], font, 1.0f * fontParams.getSize());
                    texts2D[c][9] = new CXYZString(" "+VNFloatFormatter.defaultRangeFormatWithIntegers(upList[1][i])+" ", selectionColor, yAxisUpPoint[2], yAxisUpPoint[1], -yAxisUpPoint[0], font, 1.0f * fontParams.getSize());                    
                }                
                
            }
            c++;
        }

        if (getCurrentViewer() != null) {
            getCurrentViewer().refresh();
        }
    }

    private void createLine(int n) {
        lineGeometries[n] = new IndexedLineStripArray(nLineVertices,
                GeometryArray.COORDINATES | GeometryArray.BY_REFERENCE_INDICES
                | GeometryArray.BY_REFERENCE | GeometryArray.USE_COORD_INDEX_ONLY
                | GeometryArray.COLOR_3,
                nLineIndices, lineStrips);
        lineGeometries[n].setCoordIndicesRef(lineIndices);
        lineGeometries[n].setCapability(TriangleStripArray.ALLOW_COUNT_READ);
        lineGeometries[n].setCapability(TriangleStripArray.ALLOW_FORMAT_READ);
        lineGeometries[n].setCapability(TriangleStripArray.ALLOW_COORDINATE_READ);
        lineGeometries[n].setCapability(TriangleStripArray.ALLOW_COORDINATE_WRITE);
        lineGeometries[n].setCapability(TriangleStripArray.ALLOW_COLOR_READ);
        lineGeometries[n].setCapability(TriangleStripArray.ALLOW_COLOR_WRITE);
        lineGeometries[n].setCapability(TriangleStripArray.ALLOW_COORDINATE_READ);
        lineGeometries[n].setCapability(TriangleStripArray.ALLOW_COORDINATE_WRITE);
        lineGeometries[n].setCapability(GeometryArray.ALLOW_REF_DATA_READ);
        lineGeometries[n].setCapability(GeometryArray.ALLOW_REF_DATA_WRITE);

        lines[n] = new OpenShape3D();
        lines[n].addGeometry(lineGeometries[n]);
        lines[n].setCapability(Shape3D.ENABLE_PICK_REPORTING);
        lines[n].setCapability(Shape3D.ALLOW_GEOMETRY_READ);
        lines[n].setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
        lines[n].setCapability(Shape3D.ALLOW_APPEARANCE_READ);
        lines[n].setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
        lines[n].setCapability(Geometry.ALLOW_INTERSECT);
        lines[n].setCapability(Node.ALLOW_LOCAL_TO_VWORLD_READ);
        lines[n].setUserData("line" + n);
        lines[n].setAppearance(lineApp);
    }

    private void updateLineGeometry(int iNode, int iLine) {
        switch (linesType) {
            case LINES3D_TYPE_LINES:
                lineCoords[iLine] = new float[3 * nVariablesVisible];

                float x,
                 y,
                 z;
                for (int i = 0, n = 0; i < nVariables; i++) {
                    if (!visibleList[i]) {
                        continue;
                    }

                    z = -zSize / 2.0f + n * variableStep;

                    if (maxList[0][i] == minList[0][i]) {
                        x = 0.0f;
                    } else {
                        x = (dataList[0][i][iNode] - minList[0][i]) / (maxList[0][i] - minList[0][i]) - 0.5f;
                    }

                    if (maxList[1][i] == minList[1][i]) {
                        y = 0.0f;
                    } else {
                        y = (dataList[1][i][iNode] - minList[1][i]) / (maxList[1][i] - minList[1][i]) - 0.5f;
                    }

                    lineCoords[iLine][3 * n] = x;
                    lineCoords[iLine][3 * n + 1] = y;
                    lineCoords[iLine][3 * n + 2] = z;
                    n++;
                }
                lineGeometries[iLine].setCoordRefFloat(lineCoords[iLine]);
                break;
            case LINES3D_TYPE_PIPES:
                //TODO
                break;
        }

    }

    private void updateLineColors() {
        if (nNodesVisible <= 0) {
            return;
        }

        switch (dataMapping) {

            case ParallelCoordinatesObject2D.DATA_MAPPING_DIRECT:

                byte[] lineRGB = new byte[3];
                lineRGB[0] = (byte) lineColor.getRed();
                lineRGB[1] = (byte) lineColor.getGreen();
                lineRGB[2] = (byte) lineColor.getBlue();
                byte[] selectionRGB = new byte[3];
                selectionRGB[0] = (byte) selectionColor.getRed();
                selectionRGB[1] = (byte) selectionColor.getGreen();
                selectionRGB[2] = (byte) selectionColor.getBlue();

                switch (linesType) {
                    case LINES3D_TYPE_LINES:
                        lineColors = new byte[nNodesVisible][3 * nVariablesVisible];
                        switch (selectionType) {
                            case ParallelCoordinatesObject2D.SELECTION_TYPE_HIGHLIGHT:
                                for (int i = 0, n = 0; i < nNodes; i++) {
                                    if (selectionMask[i] == 0) {
                                        continue;
                                    } else if (selectionMask[i] == 1) {
                                        for (int j = 0; j < nVariablesVisible; j++) {
                                            System.arraycopy(lineRGB, 0, lineColors[n], 3 * j, 3);
                                        }
                                    } else if (selectionMask[i] == 2) {
                                        for (int j = 0; j < nVariablesVisible; j++) {
                                            System.arraycopy(selectionRGB, 0, lineColors[n], 3 * j, 3);
                                        }
                                    }
                                    n++;
                                }
                                break;
                            case ParallelCoordinatesObject2D.SELECTION_TYPE_LIMIT:
                            case ParallelCoordinatesObject2D.SELECTION_TYPE_HIGHLIGHT_AND_LIMIT:
                                byte[] rgb = lineRGB;
                                if (selectionType == ParallelCoordinatesObject2D.SELECTION_TYPE_HIGHLIGHT_AND_LIMIT) {
                                    rgb = selectionRGB;
                                }
                                for (int i = 0, n = 0; i < nNodes; i++) {
                                    if (selectionMask[i] < 2) {
                                        continue;
                                    }

                                    for (int j = 0; j < nVariablesVisible; j++) {
                                        System.arraycopy(rgb, 0, lineColors[n], 3 * j, 3);
                                    }
                                    n++;
                                }
                                break;
                        }

                        for (int i = 0; i < lineGeometries.length; i++) {
                            lineGeometries[i].setColorRefByte(lineColors[i]);
                        }
                        break;
                    case LINES3D_TYPE_PIPES:
                        //TODO
                        break;
                }
                break;

            case ParallelCoordinatesObject2D.DATA_MAPPING_COLORMAP:

                if (colors == null) {
                    return;
                }

                float dim = 1.0f;
                boolean anySelected = false;
                for (int i = 0; i < selectionList.length; i++) {
                    if (selectionList[i]) {
                        anySelected = true;
                        break;
                    }
                }
                if (anySelected) {
                    dim = selectionDim;
                }

                switch (linesType) {
                    case LINES3D_TYPE_LINES:
                        lineColors = new byte[nNodesVisible][3 * nVariablesVisible];
                        switch (selectionType) {
                            case ParallelCoordinatesObject2D.SELECTION_TYPE_HIGHLIGHT:
                                for (int i = 0, n = 0; i < nNodes; i++) {
                                    if (selectionMask[i] == 0) {
                                        continue;
                                    } else if (selectionMask[i] == 1) {
                                        for (int j = 0; j < nVariablesVisible; j++) {
                                            for (int k = 0; k < 3; k++) {
                                                lineColors[n][3 * j + k] = (byte) (dim * (float) (colors[4 * i + k] & 0xFF));
                                            }
                                        }
                                    } else if (selectionMask[i] == 2) {
                                        for (int j = 0; j < nVariablesVisible; j++) {
                                            for (int k = 0; k < 3; k++) {
                                                lineColors[n][3 * j + k] = colors[4 * i + k];
                                            }
                                        }
                                    }
                                    n++;
                                }
                                break;
                            case ParallelCoordinatesObject2D.SELECTION_TYPE_LIMIT:
                            case ParallelCoordinatesObject2D.SELECTION_TYPE_HIGHLIGHT_AND_LIMIT:
                                for (int i = 0, n = 0; i < nNodes; i++) {
                                    if (selectionMask[i] < 2) {
                                        continue;
                                    }

                                    for (int j = 0; j < nVariablesVisible; j++) {
                                        for (int k = 0; k < 3; k++) {
                                            lineColors[n][3 * j + k] = colors[4 * i + k];
                                        }
                                    }
                                    n++;
                                }
                                break;
                        }

                        for (int i = 0; i < lineGeometries.length; i++) {
                            lineGeometries[i].setColorRefByte(lineColors[i]);
                        }
                        break;
                    case LINES3D_TYPE_PIPES:
                        //TODO
                        break;
                }
                break;
        }

    }

    private void updatePlaneGeometry(int n) {
        float w2 = xSize / 2.0f;
        float h2 = ySize / 2.0f;
        float z = -zSize / 2.0f + n * variableStep;
        planeCoords[n] = new float[]{
            -w2, -h2, z,
            -w2, h2, z,
            w2, -h2, z,
            w2, h2, z};
        planeGeometries[n].setCoordRefFloat(planeCoords[n]);
        planeOutlineGeometries[n].setCoordRefFloat(planeCoords[n]);

    }

    private void updateSelectionGeometry(int nSel, int nVis, int nVar) {
        float w2 = xSize / 2.0f;
        float h2 = ySize / 2.0f;
        float z = -zSize / 2.0f + nVis * variableStep;
        float xLow, xUp, yLow, yUp;

        if (maxList[0][nVar] == minList[0][nVar]) {
            xLow = 0.0f;
            xUp = 0.0f;
        } else {
            xLow = (lowList[0][nVar] - minList[0][nVar]) / (maxList[0][nVar] - minList[0][nVar]) - 0.5f;
            xUp = (upList[0][nVar] - minList[0][nVar]) / (maxList[0][nVar] - minList[0][nVar]) - 0.5f;
        }
        if (maxList[1][nVar] == minList[1][nVar]) {
            yLow = 0.0f;
            yUp = 0.0f;
        } else {
            yLow = (lowList[1][nVar] - minList[1][nVar]) / (maxList[1][nVar] - minList[1][nVar]) - 0.5f;
            yUp = (upList[1][nVar] - minList[1][nVar]) / (maxList[1][nVar] - minList[1][nVar]) - 0.5f;
        }
        selectionOutlineCoords[nSel] = new float[]{
            xLow, -h2, z,
            xLow, h2, z,
            xUp, -h2, z,
            xUp, h2, z,
            -w2, yLow, z,
            w2, yLow, z,
            -w2, yUp, z,
            w2, yUp, z
        };
        selectionOutlineGeometries[nSel].setCoordRefFloat(selectionOutlineCoords[nSel]);



        selectionPlaneCoords[nSel] = new float[]{
            xLow, yLow, z,
            xLow, yUp, z,
            xUp, yLow, z,
            xUp, yUp, z,};
        selectionPlaneGeometries[nSel].setCoordRefFloat(selectionPlaneCoords[nSel]);
    }

    private void createPlane(int n) {
        planeGeometries[n] = new TriangleStripArray(4,
                GeometryArray.COORDINATES | GeometryArray.NORMALS
                | GeometryArray.COLOR_3 | GeometryArray.BY_REFERENCE,
                planeStrips);
        planes[n] = new OpenShape3D();
        planeGeometries[n].setCapability(TriangleStripArray.ALLOW_COUNT_READ);
        planeGeometries[n].setCapability(TriangleStripArray.ALLOW_FORMAT_READ);
        planeGeometries[n].setCapability(TriangleStripArray.ALLOW_COORDINATE_READ);
        planeGeometries[n].setCapability(TriangleStripArray.ALLOW_COORDINATE_WRITE);
        planeGeometries[n].setCapability(TriangleStripArray.ALLOW_COLOR_READ);
        planeGeometries[n].setCapability(TriangleStripArray.ALLOW_COLOR_WRITE);
        planeGeometries[n].setCapability(TriangleStripArray.ALLOW_COORDINATE_READ);
        planeGeometries[n].setCapability(TriangleStripArray.ALLOW_COORDINATE_WRITE);
        planeGeometries[n].setCapability(GeometryArray.ALLOW_NORMAL_READ);
        planeGeometries[n].setCapability(GeometryArray.ALLOW_NORMAL_WRITE);
        planeGeometries[n].setCapability(GeometryArray.ALLOW_REF_DATA_READ);
        planeGeometries[n].setCapability(GeometryArray.ALLOW_REF_DATA_WRITE);
        planeGeometries[n].setNormalRefFloat(planeNormals);
        planes[n].addGeometry(planeGeometries[n]);
        planes[n].setCapability(Shape3D.ENABLE_PICK_REPORTING);
        planes[n].setCapability(Shape3D.ALLOW_GEOMETRY_READ);
        planes[n].setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
        planes[n].setCapability(Shape3D.ALLOW_APPEARANCE_READ);
        planes[n].setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
        planes[n].setCapability(Geometry.ALLOW_INTERSECT);
        planes[n].setCapability(Node.ALLOW_LOCAL_TO_VWORLD_READ);
        planes[n].setUserData("plane" + n);
        planes[n].setAppearance(planeApp);


        planeOutlineGeometries[n] = new IndexedLineStripArray(4,
                GeometryArray.COORDINATES | GeometryArray.BY_REFERENCE_INDICES
                | GeometryArray.BY_REFERENCE | GeometryArray.USE_COORD_INDEX_ONLY
                | GeometryArray.COLOR_3,
                8, planeOutlineStrips);
        planeOutlineGeometries[n].setCoordIndicesRef(planeOutlineIndices);

        planeOutlines[n] = new OpenShape3D();
        planeOutlineGeometries[n].setCapability(TriangleStripArray.ALLOW_COUNT_READ);
        planeOutlineGeometries[n].setCapability(TriangleStripArray.ALLOW_FORMAT_READ);
        planeOutlineGeometries[n].setCapability(TriangleStripArray.ALLOW_COORDINATE_READ);
        planeOutlineGeometries[n].setCapability(TriangleStripArray.ALLOW_COORDINATE_WRITE);
        planeOutlineGeometries[n].setCapability(TriangleStripArray.ALLOW_COLOR_READ);
        planeOutlineGeometries[n].setCapability(TriangleStripArray.ALLOW_COLOR_WRITE);
        planeOutlineGeometries[n].setCapability(TriangleStripArray.ALLOW_COORDINATE_READ);
        planeOutlineGeometries[n].setCapability(TriangleStripArray.ALLOW_COORDINATE_WRITE);
        planeOutlineGeometries[n].setCapability(GeometryArray.ALLOW_REF_DATA_READ);
        planeOutlineGeometries[n].setCapability(GeometryArray.ALLOW_REF_DATA_WRITE);

        planeOutlines[n].addGeometry(planeOutlineGeometries[n]);
        planeOutlines[n].setCapability(Shape3D.ENABLE_PICK_REPORTING);
        planeOutlines[n].setCapability(Shape3D.ALLOW_GEOMETRY_READ);
        planeOutlines[n].setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
        planeOutlines[n].setCapability(Shape3D.ALLOW_APPEARANCE_READ);
        planeOutlines[n].setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
        planeOutlines[n].setCapability(Geometry.ALLOW_INTERSECT);
        planeOutlines[n].setCapability(Node.ALLOW_LOCAL_TO_VWORLD_READ);
        planeOutlines[n].setUserData("planeOutline" + n);
        planeOutlines[n].setAppearance(planeOutlineApp);
    }

    private void updatePlaneColors() {
        float[] rgb = new float[4];
        rgb = axesColor.getRGBComponents(rgb);
        planeColors = new float[]{
            rgb[0], rgb[1], rgb[2],
            rgb[0], rgb[1], rgb[2],
            rgb[0], rgb[1], rgb[2],
            rgb[0], rgb[1], rgb[2]};
        for (int i = 0; i < nVariablesVisible; i++) {
            planeGeometries[i].setColorRefFloat(planeColors);
            planeOutlineGeometries[i].setColorRefFloat(planeColors);
        }
    }

    private void updateSelectionColors() {
        float[] rgb = new float[4];
        rgb = selectionColor.getRGBComponents(rgb);
        selectionOutlineColors = new float[]{
            rgb[0], rgb[1], rgb[2],
            rgb[0], rgb[1], rgb[2],
            rgb[0], rgb[1], rgb[2],
            rgb[0], rgb[1], rgb[2],
            rgb[0], rgb[1], rgb[2],
            rgb[0], rgb[1], rgb[2],
            rgb[0], rgb[1], rgb[2],
            rgb[0], rgb[1], rgb[2],};
        for (int i = 0; i < nVariablesSelected; i++) {
            selectionOutlineGeometries[i].setColorRefFloat(selectionOutlineColors);
        }

        selectionPlaneColors = new float[]{
            rgb[0], rgb[1], rgb[2],
            rgb[0], rgb[1], rgb[2],
            rgb[0], rgb[1], rgb[2],
            rgb[0], rgb[1], rgb[2],};
        for (int i = 0; i < nVariablesSelected; i++) {
            selectionPlaneGeometries[i].setColorRefFloat(selectionPlaneColors);
        }
    }

    private void createSelection(int n) {
        selectionOutlineGeometries[n] = new IndexedLineStripArray(8,
                GeometryArray.COORDINATES | GeometryArray.BY_REFERENCE_INDICES
                | GeometryArray.BY_REFERENCE | GeometryArray.USE_COORD_INDEX_ONLY
                | GeometryArray.COLOR_3,
                8, selectionOutlineStrips);
        selectionOutlineGeometries[n].setCoordIndicesRef(selectionOutlineIndices);

        selectionOutlines[n] = new OpenShape3D();
        selectionOutlineGeometries[n].setCapability(TriangleStripArray.ALLOW_COUNT_READ);
        selectionOutlineGeometries[n].setCapability(TriangleStripArray.ALLOW_FORMAT_READ);
        selectionOutlineGeometries[n].setCapability(TriangleStripArray.ALLOW_COORDINATE_READ);
        selectionOutlineGeometries[n].setCapability(TriangleStripArray.ALLOW_COORDINATE_WRITE);
        selectionOutlineGeometries[n].setCapability(TriangleStripArray.ALLOW_COLOR_READ);
        selectionOutlineGeometries[n].setCapability(TriangleStripArray.ALLOW_COLOR_WRITE);
        selectionOutlineGeometries[n].setCapability(TriangleStripArray.ALLOW_COORDINATE_READ);
        selectionOutlineGeometries[n].setCapability(TriangleStripArray.ALLOW_COORDINATE_WRITE);
        selectionOutlineGeometries[n].setCapability(GeometryArray.ALLOW_REF_DATA_READ);
        selectionOutlineGeometries[n].setCapability(GeometryArray.ALLOW_REF_DATA_WRITE);

        selectionOutlines[n].addGeometry(selectionOutlineGeometries[n]);
        selectionOutlines[n].setCapability(Shape3D.ENABLE_PICK_REPORTING);
        selectionOutlines[n].setCapability(Shape3D.ALLOW_GEOMETRY_READ);
        selectionOutlines[n].setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
        selectionOutlines[n].setCapability(Shape3D.ALLOW_APPEARANCE_READ);
        selectionOutlines[n].setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
        selectionOutlines[n].setCapability(Geometry.ALLOW_INTERSECT);
        selectionOutlines[n].setCapability(Node.ALLOW_LOCAL_TO_VWORLD_READ);
        selectionOutlines[n].setUserData("selectionOutline" + n);
        selectionOutlines[n].setAppearance(selectionOutlineApp);


        selectionPlaneGeometries[n] = new TriangleStripArray(4,
                GeometryArray.COORDINATES | GeometryArray.NORMALS
                | GeometryArray.COLOR_3 | GeometryArray.BY_REFERENCE,
                planeStrips);
        selectionPlanes[n] = new OpenShape3D();
        selectionPlaneGeometries[n].setCapability(TriangleStripArray.ALLOW_COUNT_READ);
        selectionPlaneGeometries[n].setCapability(TriangleStripArray.ALLOW_FORMAT_READ);
        selectionPlaneGeometries[n].setCapability(TriangleStripArray.ALLOW_COORDINATE_READ);
        selectionPlaneGeometries[n].setCapability(TriangleStripArray.ALLOW_COORDINATE_WRITE);
        selectionPlaneGeometries[n].setCapability(TriangleStripArray.ALLOW_COLOR_READ);
        selectionPlaneGeometries[n].setCapability(TriangleStripArray.ALLOW_COLOR_WRITE);
        selectionPlaneGeometries[n].setCapability(TriangleStripArray.ALLOW_COORDINATE_READ);
        selectionPlaneGeometries[n].setCapability(TriangleStripArray.ALLOW_COORDINATE_WRITE);
        selectionPlaneGeometries[n].setCapability(GeometryArray.ALLOW_NORMAL_READ);
        selectionPlaneGeometries[n].setCapability(GeometryArray.ALLOW_NORMAL_WRITE);
        selectionPlaneGeometries[n].setCapability(GeometryArray.ALLOW_REF_DATA_READ);
        selectionPlaneGeometries[n].setCapability(GeometryArray.ALLOW_REF_DATA_WRITE);
        selectionPlaneGeometries[n].setNormalRefFloat(planeNormals);
        selectionPlanes[n].addGeometry(selectionPlaneGeometries[n]);
        selectionPlanes[n].setCapability(Shape3D.ENABLE_PICK_REPORTING);
        selectionPlanes[n].setCapability(Shape3D.ALLOW_GEOMETRY_READ);
        selectionPlanes[n].setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
        selectionPlanes[n].setCapability(Shape3D.ALLOW_APPEARANCE_READ);
        selectionPlanes[n].setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
        selectionPlanes[n].setCapability(Geometry.ALLOW_INTERSECT);
        selectionPlanes[n].setCapability(Node.ALLOW_LOCAL_TO_VWORLD_READ);
        selectionPlanes[n].setUserData("selectionPlane" + n);
        selectionPlanes[n].setAppearance(selectionPlaneApp);

    }

    private void prepare3DParams() {
        mat.setShininess(15.f);
        mat.setColorTarget(OpenMaterial.AMBIENT_AND_DIFFUSE);
        planeApp.setMaterial(mat);
        planeApp.setColoringAttributes(new ColoringAttributes(1.f, 1.f, 1.f, 0));
        PolygonAttributes pattr = new PolygonAttributes(
                PolygonAttributes.POLYGON_FILL,
                PolygonAttributes.CULL_NONE, 0.f, true);
        planeApp.setPolygonAttributes(pattr);
        if (planesTransparency < 0.1) {
            planeApp.setTransparencyAttributes(noTransparencyAttributes);
        } else {
            planeTransparencyAttributes.setTransparency(planesTransparency);
            planeApp.setTransparencyAttributes(planeTransparencyAttributes);
        }
        planeOutlineApp.setLineAttributes(planeOutlineAttr);

        selectionPlaneApp.setMaterial(mat);
        selectionPlaneApp.setColoringAttributes(new ColoringAttributes(1.f, 1.f, 1.f, 0));
        selectionPlaneApp.setPolygonAttributes(pattr);
        if (selectionPlaneTransparency < 0.1) {
            selectionPlaneApp.setTransparencyAttributes(noTransparencyAttributes);
        } else {
            selectionPlaneTransparencyAttributes.setTransparency(selectionPlaneTransparency);
            selectionPlaneApp.setTransparencyAttributes(selectionPlaneTransparencyAttributes);
        }
        selectionOutlineApp.setLineAttributes(selectionOutlineAttr);

        lineApp.setLineAttributes(lineAttr);
    }

    public void setDistanceScale(float distanceScale) {
//        this.variableStep = (int) (2 * nNodes * distanceScale);
//        this.zSize = (nVariablesVisible - 1) * variableStep;
        this.variableStep = distanceScale;
        this.zSize = (nVariablesVisible - 1) * variableStep;
        float w2 = xSize / 2.0f;
        float h2 = ySize / 2.0f;
        float d2 = zSize / 2.0f;
        this.extents = new float[][]{{-w2, -h2, -d2}, {w2, h2, d2}};
        if(parentObject != null)
            parentObject.setExtents(extents);
        updateBoundingSphere();
        updateGeometry();

    }

    /**
     * @return the selectionType
     */
    public int getSelectionType() {
        return selectionType;
    }

    /**
     * @param selectionType the selectionType to set
     */
    public void setSelectionType(int selectionType) {
        this.selectionType = selectionType;
        fullUpdate();
    }

    public void setSelection(byte[] selectionMask, boolean[] selectionList) {
        if (selectionMask == null || selectionList == null || selectionList.length != nVariables || selectionMask.length != nNodes) {
            return;
        }
        this.selectionList = selectionList;
        this.selectionMask = selectionMask;
        this.nVariablesSelected = 0;
        for (int i = 0; i < selectionList.length; i++) {
            if (selectionList[i]) {
                nVariablesSelected++;
            }
        }
        fullUpdate();
    }

    /**
     * @return the axesColor
     */
    public Color getAxesColor() {
        return axesColor;
    }

    /**
     * @param axesColor the axesColor to set
     */
    public void setAxesColor(Color axesColor) {
        this.axesColor = axesColor;
        updatePlaneColors();

    }

    /**
     * @return the lineColor
     */
    public Color getLineColor() {
        return lineColor;
    }

    /**
     * @param lineColor the lineColor to set
     */
    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
        updateLineColors();
    }

    /**
     * @return the selectionColor
     */
    public Color getSelectionColor() {
        return selectionColor;
    }

    /**
     * @param selectionColor the selectionColor to set
     */
    public void setSelectionColor(Color selectionColor) {
        this.selectionColor = selectionColor;
        updateSelectionColors();
        updateLineColors();
    }

    /**
     * @return the dataMapping
     */
    public int getDataMappingType() {
        return dataMapping;
    }

    /**
     * @param dataMapping the dataMapping to set
     */
    public void setDataMappingType(int dataMapping) {
        this.dataMapping = dataMapping;
        updateLineColors();
    }

    /**
     * @return the colors
     */
    public byte[] getColors() {
        return colors;
    }

    /**
     * @param colors the colors to set
     */
    public void setColors(byte[] colors) {
        this.colors = colors;
        updateLineColors();
    }

    /**
     * @return the selectionDim
     */
    public float getSelectionDim() {
        return selectionDim;
    }

    /**
     * @param selectionDim the selectionDim to set
     */
    public void setSelectionDim(float selectionDim) {
        this.selectionDim = selectionDim;
        updateLineColors();
    }

    public void setPlaneTransparency(float planesTransparency) {
        this.planesTransparency = planesTransparency;
        if (planesTransparency < 0.1) {
            planeApp.setTransparencyAttributes(noTransparencyAttributes);
        } else {
            planeTransparencyAttributes.setTransparency(planesTransparency);
            planeApp.setTransparencyAttributes(planeTransparencyAttributes);
        }

    }

    public void setSelectionAreaTransparency(float selectionAreaTransparency) {
        this.selectionPlaneTransparency = selectionAreaTransparency;
        if (selectionPlaneTransparency < 0.1) {
            selectionPlaneApp.setTransparencyAttributes(noTransparencyAttributes);
        } else {
            selectionPlaneTransparencyAttributes.setTransparency(selectionPlaneTransparency);
            selectionPlaneApp.setTransparencyAttributes(selectionPlaneTransparencyAttributes);
        }
    }
    private int nNodesVisible = 0;

    private void countVisibleLines() {
        nNodesVisible = 0;
        if (selectionMask == null) {
            return;
        }

        switch (selectionType) {
            case ParallelCoordinatesObject2D.SELECTION_TYPE_HIGHLIGHT:
                for (int i = 0; i < selectionMask.length; i++) {
                    if (selectionMask[i] > 0) {
                        nNodesVisible++;
                    }
                }
                break;
            case ParallelCoordinatesObject2D.SELECTION_TYPE_LIMIT:
            case ParallelCoordinatesObject2D.SELECTION_TYPE_HIGHLIGHT_AND_LIMIT:
                for (int i = 0; i < selectionMask.length; i++) {
                    if (selectionMask[i] > 1) {
                        nNodesVisible++;
                    }
                }
                break;

        }
    }

    /**
     * @param linesType the linesType to set
     */
    public void setLinesType(int linesType) {
        this.linesType = linesType;
        updateLinesGeometry();
        updateLineColors();
    }

    void setLineWidth(float lineWidth) {
        if (lineWidth <= 0.0f) {
            this.lineWidth = 0.01f;
        } else {
            this.lineWidth = lineWidth;
        }
        switch (linesType) {
            case LINES3D_TYPE_LINES:
                lineAttr.setLineWidth(4 * this.lineWidth);
                break;
            case LINES3D_TYPE_PIPES:
                updateLinesGeometry();
                break;
        }
    }

    /**
     * @param fontParams the fontParams to set
     */
    public void setFontParams(FontParams fontParams) {
        this.fontParams = fontParams;
        updateTexts();
    }

    public void drawLocal2DWrapper(J3DGraphics2D vGraphics, LocalToWindow ltw, int w, int h) {
        if (fontParams == null || fontParams.isThreeDimensional() || ltw == null || vGraphics == null) {
            return;
        }

        if (texts2D != null) {
            for (int i = 0; i < texts2D.length; i++) {
                if (texts2D[i] != null) {
                    for (int j = 0; j < texts2D[i].length; j++) {
                        if (texts2D[i][j] != null) {
                            texts2D[i][j].update(ltw);
                            texts2D[i][j].draw(vGraphics, w, h);
                        }
                    }
                }
            }
        }
    }

    private void updateBoundingSphere() {
        double[] center = new double[]{0, 0, 0};
        double r = 0;
        for (int ii = 0; ii < center.length; ii++) {
            center[ii] = (extents[0][ii] + extents[1][ii]) / 2;
            r += (extents[0][ii] - extents[1][ii]) * (extents[0][ii] - extents[1][ii]);
        }
        r = Math.sqrt(r);
        bSphere = new BoundingSphere(new Point3d(center), r);
    }
}
