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


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import pl.edu.icm.visnow.lib.utils.VNFloatFormatter;
import pl.edu.icm.visnow.lib.utils.geometry2D.GeometryObject2D;

/**
 * @author Bartosz Borucki (babor@icm.edu.pl) Warsaw University,
 * Interdisciplinary Centre for Mathematical and Computational Modelling
 * 13 October 2013
 */
public class ParallelCoordinatesObject2D extends GeometryObject2D implements Cloneable {

    public static final int SELECTION_TYPE_HIGHLIGHT = 0;
    public static final int SELECTION_TYPE_HIGHLIGHT_AND_LIMIT = 1;
    public static final int SELECTION_TYPE_LIMIT = 2;
    public static final int DATA_MAPPING_DIRECT = 0;
    public static final int DATA_MAPPING_COLORMAP = 1;
    private int nVariables = 0;
    private int nVariablesVisible = 0;
    private int firstVariableVisible = -1;
    private int nNodes = 0;
    private String[] nameList;
    private float[][] dataList;
    private float[] minList;
    private float[] lowList;
    private float[] upList;
    private float[] maxList;
    private byte[] selectionMask;
    private boolean[] selectionList;
    private boolean[] visibleList;
    private int selectionType = SELECTION_TYPE_HIGHLIGHT;
    private int variableStep = 0;
    private int marginLeft = 50;
    private int marginRight = 50;
    private int marginTop = 50;
    private int marginBottom = 50;
    private Color axesColor = Color.WHITE;
    private Color lineColor = Color.BLUE;
    private Color selectionColor = Color.RED;
    private int dataMapping = DATA_MAPPING_DIRECT;
    private byte[] colors = null;
    private float selectionDim = 1.0f;
    private FontMetrics fm;

    public ParallelCoordinatesObject2D(int nVariables, int nNodes,
            String[] nameList, float[][] dataList,
            float[] minList, float[] lowList, float[] upList, float[] maxList,
            byte[] selectionMask, boolean[] selectionList, int selectionType,
            float distanceScale,
            Color axesColor,
            int dataMappingType,
            Color lineColor, Color selectionColor, byte[] colors, float selectionDim,
            boolean[] visibleList) {
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
            if(visibleList[i]) {
                if(firstVariableVisible == -1)
                    firstVariableVisible = i;
                nVariablesVisible++;            
            }
        }

        marginTop = (int) (nNodes * 0.1);
        marginBottom = (int) (nNodes * 0.3);
        marginLeft = (int) (nNodes * 0.5);
        marginRight = (int) (nNodes * 0.3);

        this.height = 2 * nNodes + marginTop + marginBottom;
        this.variableStep = (int) (2 * nNodes * distanceScale);
        this.width = (nVariablesVisible - 1) * variableStep + marginLeft + marginRight;
    }

    public void setDistanceScale(float distanceScale) {
        this.variableStep = (int) (2 * nNodes * distanceScale);
        this.width = (nVariablesVisible - 1) * variableStep + marginLeft + marginRight;
        fireStateChanged();
    }

    @Override
    public void drawLocal2D(Graphics2D g, AffineTransform tr) {
        if (nVariablesVisible < 2 || dataList == null || minList == null || lowList == null || upList == null || maxList == null || selectionMask == null) {
            return;
        }

        if (dataList.length != nVariables) {
            return;
        }

        for (int i = 0; i < nVariables; i++) {
            if (dataList[i].length != nNodes) {
                return;
            }
        }
        
        boolean anySelected = false;
        for (int i = 0; i < selectionList.length; i++) {
            if(selectionList[i]) {
                anySelected = true;
                break;
            }            
        }

        float lineWidth = 1.0f;
        g.translate(tr.getTranslateX(), tr.getTranslateY());
        //--------------draw here----------------------
        fm = g.getFontMetrics();

        //linie
        GeneralPath linePath;
        float fx, fy;
        switch (dataMapping) {
            
            case DATA_MAPPING_DIRECT:
                
                
                switch (selectionType) {
                    case SELECTION_TYPE_HIGHLIGHT:
                        g.setStroke(new BasicStroke(lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 2.0f));
                        g.setColor(lineColor);
                        for (int i = 0; i < nNodes; i++) {
                            if (selectionMask[i] != 1) {
                                continue;
                            }

                            linePath = new GeneralPath();
                            fx = marginLeft;
                            if(maxList[firstVariableVisible] == minList[firstVariableVisible])
                                fy = marginTop + nNodes;
                            else
                                fy = marginTop + 2 * nNodes * (1.0f - (dataList[firstVariableVisible][i] - minList[firstVariableVisible]) / (maxList[firstVariableVisible] - minList[firstVariableVisible]));
                            linePath.moveTo(fx * tr.getScaleX(), fy * tr.getScaleY());
                            for (int j = firstVariableVisible+1; j < nVariables; j++) {
                                if(!visibleList[j])
                                    continue;
                                fx += variableStep;
                                if(maxList[j] == minList[j])
                                    fy = marginTop + nNodes;
                                else
                                    fy = marginTop + 2 * nNodes * (1.0f - (dataList[j][i] - minList[j]) / (maxList[j] - minList[j]));
                                linePath.lineTo(fx * tr.getScaleX(), fy * tr.getScaleY());
                            }
                            g.draw(linePath);
                        }

                        g.setStroke(new BasicStroke(lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 2.0f));
                        g.setColor(selectionColor);
                        for (int i = 0; i < nNodes; i++) {
                            if (selectionMask[i] != 2) {
                                continue;
                            }

                            linePath = new GeneralPath();
                            fx = marginLeft;
                            if(maxList[firstVariableVisible] == minList[firstVariableVisible])
                                fy = marginTop + nNodes;
                            else
                                fy = marginTop + 2 * nNodes * (1.0f - (dataList[firstVariableVisible][i] - minList[firstVariableVisible]) / (maxList[firstVariableVisible] - minList[firstVariableVisible]));
                            linePath.moveTo(fx * tr.getScaleX(), fy * tr.getScaleY());
                            for (int j = firstVariableVisible+1; j < nVariables; j++) {
                                if(!visibleList[j])
                                    continue;
                                fx += variableStep;
                                if(maxList[j] == minList[j])
                                    fy = marginTop + nNodes;
                                else
                                    fy = marginTop + 2 * nNodes * (1.0f - (dataList[j][i] - minList[j]) / (maxList[j] - minList[j]));
                                linePath.lineTo(fx * tr.getScaleX(), fy * tr.getScaleY());
                            }
                            g.draw(linePath);
                        }
                        break;
                    case SELECTION_TYPE_LIMIT:
                    case SELECTION_TYPE_HIGHLIGHT_AND_LIMIT:
                        g.setStroke(new BasicStroke(lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 2.0f));
                        if (selectionType == SELECTION_TYPE_HIGHLIGHT_AND_LIMIT) {
                            g.setColor(selectionColor);
                        } else {
                            g.setColor(lineColor);
                        }
                        for (int i = 0; i < nNodes; i++) {
                            if (selectionMask[i] != 2) {
                                continue;
                            }

                            linePath = new GeneralPath();
                            fx = marginLeft;
                            if(maxList[firstVariableVisible] == minList[firstVariableVisible])
                                fy = marginTop + nNodes;
                            else
                                fy = marginTop + 2 * nNodes * (1.0f - (dataList[firstVariableVisible][i] - minList[firstVariableVisible]) / (maxList[firstVariableVisible] - minList[firstVariableVisible]));
                            linePath.moveTo(fx * tr.getScaleX(), fy * tr.getScaleY());
                            for (int j = firstVariableVisible+1; j < nVariables; j++) {
                                if(!visibleList[j])
                                    continue;
                                fx += variableStep;
                                if(maxList[j] == minList[j])
                                    fy = marginTop + nNodes;
                                else
                                    fy = marginTop + 2 * nNodes * (1.0f - (dataList[j][i] - minList[j]) / (maxList[j] - minList[j]));
                                linePath.lineTo(fx * tr.getScaleX(), fy * tr.getScaleY());
                            }
                            g.draw(linePath);
                        }
                        break;
                }
                break;
                
                
            case DATA_MAPPING_COLORMAP:
                
                
                switch (selectionType) {
                    case SELECTION_TYPE_HIGHLIGHT:
                        g.setStroke(new BasicStroke(lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 2.0f));
                        float dim = 1.0f;
                        if(anySelected)
                            dim = selectionDim;
                        for (int i = 0; i < nNodes; i++) {
                            if (selectionMask[i] != 1) {
                                continue;
                            }

                            g.setColor(new Color((int)((float)(colors[4*i  ]&0xFF)*dim), 
                                                 (int)((float)(colors[4*i+1]&0xFF)*dim), 
                                                 (int)((float)(colors[4*i+2]&0xFF)*dim), 
                                                 (int)(colors[4*i+3]&0xFF)));
                            linePath = new GeneralPath();
                            fx = marginLeft;
                            if(maxList[firstVariableVisible] == minList[firstVariableVisible])
                                fy = marginTop + nNodes;
                            else
                                fy = marginTop + 2 * nNodes * (1.0f - (dataList[firstVariableVisible][i] - minList[firstVariableVisible]) / (maxList[firstVariableVisible] - minList[firstVariableVisible]));
                            linePath.moveTo(fx * tr.getScaleX(), fy * tr.getScaleY());
                            for (int j = firstVariableVisible+1; j < nVariables; j++) {
                                if(!visibleList[j])
                                    continue;
                                fx += variableStep;
                                if(maxList[j] == minList[j])
                                    fy = marginTop + nNodes;
                                else
                                    fy = marginTop + 2 * nNodes * (1.0f - (dataList[j][i] - minList[j]) / (maxList[j] - minList[j]));
                                linePath.lineTo(fx * tr.getScaleX(), fy * tr.getScaleY());
                            }
                            g.draw(linePath);
                        }

                        g.setStroke(new BasicStroke(lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 2.0f));
                        for (int i = 0; i < nNodes; i++) {
                            if (selectionMask[i] != 2) {
                                continue;
                            }

                            g.setColor(new Color((int)(colors[4*i]&0xFF), (int)(colors[4*i+1]&0xFF), (int)(colors[4*i+2]&0xFF), (int)(colors[4*i+3]&0xFF)));
                            linePath = new GeneralPath();
                            fx = marginLeft;
                            if(maxList[firstVariableVisible] == minList[firstVariableVisible])
                                fy = marginTop + nNodes;
                            else
                                fy = marginTop + 2 * nNodes * (1.0f - (dataList[firstVariableVisible][i] - minList[firstVariableVisible]) / (maxList[firstVariableVisible] - minList[firstVariableVisible]));
                            linePath.moveTo(fx * tr.getScaleX(), fy * tr.getScaleY());
                            for (int j = firstVariableVisible+1; j < nVariables; j++) {
                                if(!visibleList[j])
                                    continue;
                                fx += variableStep;
                                if(maxList[j] == minList[j])
                                    fy = marginTop + nNodes;
                                else
                                    fy = marginTop + 2 * nNodes * (1.0f - (dataList[j][i] - minList[j]) / (maxList[j] - minList[j]));
                                linePath.lineTo(fx * tr.getScaleX(), fy * tr.getScaleY());
                            }
                            g.draw(linePath);
                        }
                        break;
                    case SELECTION_TYPE_LIMIT:
                    case SELECTION_TYPE_HIGHLIGHT_AND_LIMIT:
                        g.setStroke(new BasicStroke(lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 2.0f));
                        for (int i = 0; i < nNodes; i++) {
                            if (selectionMask[i] != 2) {
                                continue;
                            }

                            g.setColor(new Color((int)(colors[4*i]&0xFF), (int)(colors[4*i+1]&0xFF), (int)(colors[4*i+2]&0xFF), (int)(colors[4*i+3]&0xFF)));
                            linePath = new GeneralPath();
                            fx = marginLeft;
                            if(maxList[firstVariableVisible] == minList[firstVariableVisible])
                                fy = marginTop + nNodes;
                            else
                                fy = marginTop + 2 * nNodes * (1.0f - (dataList[firstVariableVisible][i] - minList[firstVariableVisible]) / (maxList[firstVariableVisible] - minList[firstVariableVisible]));
                            linePath.moveTo(fx * tr.getScaleX(), fy * tr.getScaleY());
                            for (int j = firstVariableVisible+1; j < nVariables; j++) {
                                if(!visibleList[j])
                                    continue;
                                fx += variableStep;
                                if(maxList[j] == minList[j])
                                    fy = marginTop + nNodes;
                                else
                                    fy = marginTop + 2 * nNodes * (1.0f - (dataList[j][i] - minList[j]) / (maxList[j] - minList[j]));
                                linePath.lineTo(fx * tr.getScaleX(), fy * tr.getScaleY());
                            }
                            g.draw(linePath);
                        }
                        break;
                }

                break;
        }




        //osie
        int x, y0, y1;
        String str;
        g.setStroke(new BasicStroke(lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 2.0f));
        for (int i = 0, c = 0 ; i < nVariables; i++) {
            if(!visibleList[i])
                continue;
            g.setColor(axesColor);
            x = (int) ((marginLeft + c * variableStep) * tr.getScaleX());
            y0 = (int) (marginTop * tr.getScaleY());
            y1 = (int) ((marginTop + 2 * nNodes) * tr.getScaleY());
            g.drawLine(x, y0, x, y1);
            g.drawLine(x - 2, y0, x + 2, y0);
            g.drawLine(x - 2, y1, x + 2, y1);

            g.drawString(nameList[i], x - fm.stringWidth(nameList[i]) / 2, y1 + 30);
            str = VNFloatFormatter.defaultRangeFormatWithIntegers(maxList[i]);
            g.drawString(str, x - fm.stringWidth(str) - 4, y0 + fm.getAscent() / 2);
            str = VNFloatFormatter.defaultRangeFormatWithIntegers(minList[i]);
            g.drawString(str, x - fm.stringWidth(str) - 4, y1 + fm.getAscent() / 2);
            if (selectionList[i]) {
                if(maxList[i] == minList[i]) {
                    y0 = (int) (marginTop + nNodes);
                    y1 = (int) (marginTop + nNodes);
                } else {
                    y0 = (int) ((marginTop + 2 * nNodes * (1.0f - (upList[i] - minList[i]) / (maxList[i] - minList[i]))) * tr.getScaleY());
                    y1 = (int) ((marginTop + 2 * nNodes * (1.0f - (lowList[i] - minList[i]) / (maxList[i] - minList[i]))) * tr.getScaleY());
                }
                str = VNFloatFormatter.defaultRangeFormatWithIntegers(upList[i]);
                g.drawString(str, x - fm.stringWidth(str) - 4, y0 + fm.getAscent() / 2);
                str = VNFloatFormatter.defaultRangeFormatWithIntegers(lowList[i]);
                g.drawString(str, x - fm.stringWidth(str) - 4, y1 + fm.getAscent() / 2);

                g.drawLine(x - 2, y0, x + 2, y0);
                g.drawLine(x - 2, y1, x + 2, y1);
                g.setColor(selectionColor);
                g.drawLine(x, y0 + 1, x, y1);
            }
            c++;
        }

        //---------------------------------------------            
        g.translate(-tr.getTranslateX(), -tr.getTranslateY());
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
        fireStateChanged();
    }

    public void setSelection(byte[] selectionMask, boolean[] selectionList) {
        if (selectionMask == null || selectionList == null || selectionList.length != nVariables || selectionMask.length != nNodes) {
            return;
        }

        this.selectionList = selectionList;
        this.selectionMask = selectionMask;
        fireStateChanged();
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
        fireStateChanged();
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
        fireStateChanged();
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
        fireStateChanged();
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
        fireStateChanged();
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
        fireStateChanged();
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
        fireStateChanged();
    }
}
