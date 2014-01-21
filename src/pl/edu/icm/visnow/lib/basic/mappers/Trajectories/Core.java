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

package pl.edu.icm.visnow.lib.basic.mappers.Trajectories;

import java.util.ArrayList;
import pl.edu.icm.visnow.datasets.*;
import pl.edu.icm.visnow.datasets.cells.Cell;
import pl.edu.icm.visnow.datasets.dataarrays.DataArray;

/**
 * @author  Bartosz Borucki (babor@icm.edu.pl)
 * University of Warsaw, Interdisciplinary Centre
 * for Mathematical and Computational Modelling
 */
public class Core {

    private Field inField;
    private IrregularField outField = null;
    private RegularField mappedField = null;
    private Params params;

    public Core() {
    }

    /**
     * @param inField the inField to set
     */
    public void setInField(Field inField) {
        this.inField = inField;
    }

    /**
     * @return the outField
     */
    public IrregularField getOutField() {
        return outField;
    }

	public RegularField getMappedField()
	{
		return mappedField;
	}

    /**
     * @param params the params to set
     */
    public void setParams(Params params) {
        this.params = params;
    }

    public void update() {
        if (inField == null || inField.getNFrames() < 2 || inField.getTimeCoords() == null || inField.getTimeCoords().isEmpty()) {
            outField = null;
            return;
        }

        int inNFrames = inField.getNFrames();
        int startFrame = params.getStartFrame();
        int endFrame = params.getEndFrame();
        int nFrames = endFrame - startFrame + 1;
        
        int inNNodes = inField.getNNodes();
        int outNNodes = nFrames * inNNodes;
        int outNCells = (nFrames-1) * inNNodes;
        int inNSpace = inField.getNSpace();

        ArrayList<DataArray> comps = new ArrayList<DataArray>();
                
        for (int c = 0; c < inField.getNData(); c++) {   
            int veclen = inField.getData(c).getVeclen(); 
            switch(inField.getData(c).getType()) {
                case DataArray.FIELD_DATA_BYTE:
                    byte[] bData = new byte[nFrames * inNNodes * veclen];
                    byte[] inDataB;                    
                    for (int n = 0; n < nFrames; n++) {
                        inField.getData(c).setCurrentTime(inField.getData(c).getTime(n));
                        inDataB = inField.getData(c).getBData();            
                        for (int i = 0; i < inNNodes; i++) {
                            if(veclen == 1) {
                                bData[i * nFrames + n] = inDataB[i];                                
                            } else {
                                for (int m = 0; m < veclen; m++) {
                                    bData[i * nFrames * veclen + n * veclen + m] = inDataB[i*veclen + m];                                
                                }
                            }                            
                        }
                    }
                    comps.add(DataArray.create(bData, veclen, inField.getData(c).getName()));
                    break;
                case DataArray.FIELD_DATA_SHORT:
                    short[] sData = new short[nFrames * inNNodes * veclen];
                    short[] inDataS;                    
                    for (int n = 0; n < nFrames; n++) {
                        inField.getData(c).setCurrentTime(inField.getData(c).getTime(n));
                        inDataS = inField.getData(c).getSData();            
                        for (int i = 0; i < inNNodes; i++) {
                            if(veclen == 1) {
                                sData[i * nFrames + n] = inDataS[i];                                
                            } else {
                                for (int m = 0; m < veclen; m++) {
                                    sData[i * nFrames * veclen + n * veclen + m] = inDataS[i*veclen + m];                                
                                }
                            }                            
                        }
                    }
                    comps.add(DataArray.create(sData, veclen, inField.getData(c).getName()));
                    break;
                case DataArray.FIELD_DATA_INT:
                    int[] iData = new int[nFrames * inNNodes * veclen];
                    int[] inDataI;                    
                    for (int n = 0; n < nFrames; n++) {
                        inField.getData(c).setCurrentTime(inField.getData(c).getTime(n));
                        inDataI = inField.getData(c).getIData();            
                        for (int i = 0; i < inNNodes; i++) {
                            if(veclen == 1) {
                                iData[i * nFrames + n] = inDataI[i];                                
                            } else {
                                for (int m = 0; m < veclen; m++) {
                                    iData[i * nFrames * veclen + n * veclen + m] = inDataI[i*veclen + m];    
                                }
                            }                            
                        }
                    }
                    comps.add(DataArray.create(iData, veclen, inField.getData(c).getName()));
                    break;
                case DataArray.FIELD_DATA_FLOAT:                    
                    float[] fData = new float[nFrames * inNNodes * veclen];
                    float[] inDataF;                    
                    for (int n = 0; n < nFrames; n++) {
                        inField.getData(c).setCurrentTime(inField.getData(c).getTime(n));
                        inDataF = inField.getData(c).getFData();            
                        for (int i = 0; i < inNNodes; i++) {
                            if(veclen == 1) {
                                fData[i * nFrames + n] = inDataF[i];                                
                            } else {
                                for (int m = 0; m < veclen; m++) {
                                    fData[i * nFrames * veclen + n * veclen + m] = inDataF[i*veclen + m];                                
                                }
                            }                            
                        }
                    }
                    comps.add(DataArray.create(fData, veclen, inField.getData(c).getName()));
                    break;
                case DataArray.FIELD_DATA_DOUBLE:
                    double[] dData = new double[nFrames * inNNodes * veclen];
                    double[] inDataD;                    
                    for (int n = 0; n < nFrames; n++) {
                        inField.getData(c).setCurrentTime(inField.getData(c).getTime(n));
                        inDataD = inField.getData(c).getDData();            
                        for (int i = 0; i < inNNodes; i++) {
                            if(veclen == 1) {
                                dData[i * nFrames + n] = inDataD[i];                                
                            } else {
                                for (int m = 0; m < veclen; m++) {
                                    dData[i * nFrames * veclen + n * veclen + m] = inDataD[i*veclen + m];                                
                                }
                            }                            
                        }
                    }
                    comps.add(DataArray.create(dData, veclen, inField.getData(c).getName()));
                    break;
            }
            inField.getData(c).setCurrentTime(inField.getData(c).getStartTime());
        }

        float[] coords = new float[inNNodes * nFrames * 3];
        for (int i = 0; i < inNNodes; i++) {

            float[] trj = inField.getTrajectory(i);
            
            if (inNSpace == 3 && nFrames == inNFrames) {

                System.arraycopy(trj, 0, coords, nFrames * 3 * i, trj.length);

            } else {

                for (int n = 0; n < nFrames; n++) {
                    for (int m = 0; m < inNSpace; m++) {
                        coords[i * nFrames * 3 + 3 * n + m] = trj[(n+startFrame-1) * inNSpace + m];
                    }
                    for (int m = inNSpace; m < 3; m++) {
                        coords[i * nFrames * 3 + 3 * n + m] = 0;
                    }
                }
            }

        }

        int[] cells = new int[2*outNCells];  //2 * (nFrames-1) * inNNodes
        for (int i = 0, l = 0; i < inNNodes; i++) {
            for (int n = 0; n < nFrames-1; n++, l+=2) {
                cells[l    ] = i*nFrames + n;
                cells[l + 1] = i*nFrames + n + 1;
            }
        }

        outField = new IrregularField(outNNodes);
        outField.setNSpace(3);
        outField.setCoords(coords);
        CellSet cellSet = new CellSet(inField.getName() + "_trajectories");
        for (int i = 0; i < comps.size(); i++) {
            outField.addData(comps.get(i));            
        }        
        CellArray segments = new CellArray(Cell.SEGMENT, cells, null, null);
        cellSet.setBoundaryCellArray(segments);
        cellSet.setCellArray(segments);
        
        outField.addCellSet(cellSet);

        if(inField.getMask() != null) {
            boolean[] valid = new boolean[inNNodes * nFrames];
            for (int i = 0; i < inNNodes; i++) {
                System.arraycopy(inField.getMaskTimeSlice(i), startFrame-1, valid, nFrames * i, nFrames);
            }

            outField.setMask(valid);
        }

        //TODO invalid aware trajectories
        
		mappedField = MapCoordsToData.MapCoordsToData( inField );		
    }
}
