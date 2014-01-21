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

package pl.edu.icm.visnow.lib.utils.numeric;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;
import pl.edu.icm.visnow.datasets.CellArray;
import pl.edu.icm.visnow.datasets.CellSet;
import pl.edu.icm.visnow.datasets.IrregularField;
import pl.edu.icm.visnow.datasets.RegularField;
import pl.edu.icm.visnow.datasets.cells.Cell;
import pl.edu.icm.visnow.datasets.cells.Hex;
import pl.edu.icm.visnow.datasets.cells.Point;
import pl.edu.icm.visnow.datasets.cells.Prism;
import pl.edu.icm.visnow.datasets.cells.Pyramid;
import pl.edu.icm.visnow.datasets.cells.Quad;
import pl.edu.icm.visnow.datasets.cells.RegularHex;
import pl.edu.icm.visnow.datasets.cells.Segment;
import pl.edu.icm.visnow.datasets.dataarrays.DataArray;
import static pl.edu.icm.visnow.lib.utils.numeric.SliceLookupTable.*;

/**
 *
 * @author Krzysztof S. Nowinski, University of Warsaw ICM
 */ 
public class RegularFieldSplitter 
{
   protected static final int CHUNK = 4096;
   
   class NewNode
   {
      public int p0, p1, index;
      public float ratio;

      public NewNode(int index, int p0, int p1, float ratio)
      {
         this.index = index;
         this.p0 = p0;
         this.p1 = p1;
         this.ratio = ratio;
      }
      
      public long getHash()
      {
         return (long)p1 << 32 | (long)p0;
      }
   }
   
   private static final int[] cellTypes = {Cell.POINT, Cell.SEGMENT, Cell.QUAD, Cell.HEXAHEDRON};
   
   protected HashMap<Long, NewNode> newNodes = new HashMap<Long, NewNode>();
   
   protected RegularField inField;
   protected int position = 0;
   
   protected boolean[] usedNodes;
   protected int nInNodes;
   protected int nOldNodes;
   protected int nNewNodes;
   protected int nOutNodes;
   protected int[] dims;
   protected int[] globalNewNodeIndices;
   
   protected int totalNewNodes = 0;
   
   protected int totalOutputCells = 0;
   
   protected ArrayList<int[]>[] newCells;
   
   protected int[] totalNewCells = new int[Cell.TYPES];
   protected int[] currentNewCells = new int[Cell.TYPES];
   
   protected int cellType;
   protected int nCellNodes;
   protected int[] nodes;
   protected int[] dataIndices;
   
   protected NewNode[] newNodesArray;
   protected IrregularField outField;   
   
   @SuppressWarnings("unchecked")
   public RegularFieldSplitter(RegularField inField, int position)
   {
      this.inField = inField;
      this.position = position;
      dims = inField.getDims();
      nInNodes = inField.getNNodes();
      usedNodes = new boolean[nInNodes];
      nOldNodes = nNewNodes = nOutNodes = 0;
      nNewNodes = 0;
      totalOutputCells = 0;
      Arrays.fill(totalNewCells, 0);
      Arrays.fill(currentNewCells, CHUNK);
      newCells =  (ArrayList<int[]>[])(new ArrayList[Cell.TYPES]);
      for (int i = 0; i < Cell.TYPES; i++)
         newCells[i] = new ArrayList<int[]>();
      totalNewCells = new int[Cell.TYPES];
      totalNewCells = new int[Cell.TYPES];
      cellType = cellTypes[dims.length];
      nCellNodes = Cell.nv[cellType];
   }
   
   
   public void processCell(int[] cell, float[] vals, boolean even)
   {
         int newCellType;
         int[][] cellTriangulation;
         int[][] triangIndices;
         switch (cellType)
         {
         case Cell.QUAD:
            newCellType = Cell.TRIANGLE;
            cellTriangulation = Quad.triangulationVertices(cell);
            triangIndices     = Quad.triangulationIndices(cell);
            break;
         case Cell.HEXAHEDRON:
            newCellType = Cell.TETRA;
            cellTriangulation = RegularHex.triangulationVertices(cell, even);
            triangIndices     = RegularHex.triangulationIndices(even);
            break;
         default:
            return;
         }
         int nSimplexNodes = Cell.nv[newCellType];
         for (int iSimplex = 0; iSimplex < cellTriangulation.length; iSimplex++)
         {
            int[] slicedSimplex = cellTriangulation[iSimplex];
            float[] slicedVals = new float[nSimplexNodes];
            for (int i = 0; i < nSimplexNodes; i++)
               slicedVals[i] = vals[triangIndices[iSimplex][i]];
//                        for (int i = 0; i < nSimplexNodes; i++)
//                           System.out.printf("%4d ",slicedSimplex[i]);
//                        System.out.println("");
//                        for (int i = 0; i < nSimplexNodes; i++)
//                           System.out.printf("%4.1f ",slicedVals[i]);
//                        System.out.println("");
            int subcellType;    
            if (position != 0)
               subcellType = getSubcellType(newCellType, slicedVals, position > 0);
            else
               subcellType = getSliceType(newCellType, slicedVals);
            if (subcellType < 0)
               continue;
            int[] subcellNodes;
            if (position != 0)
               subcellNodes = getSubcellNodes(newCellType, slicedVals, position > 0);
            else
               subcellNodes = getSliceNodes(newCellType, slicedVals);
            int currentCell = currentNewCells[subcellType];
            int[] currentCells;
            if (currentCell >= CHUNK)
            {
               currentCells = new int[Cell.nv[subcellType] * CHUNK];
               newCells[subcellType].add(currentCells);
               currentCell = currentNewCells[subcellType] = 0;
            }
            else
               currentCells = newCells[subcellType].get(newCells[subcellType].size() - 1);
            int subcellSize = subcellNodes.length;
            for (int l = 0; l < subcellNodes.length; l++)
            {
//               System.out.printf("%3d: ", subcellNodes[l]);
               if (subcellNodes[l] < nSimplexNodes)
               {
                  int node = slicedSimplex[subcellNodes[l]];
                  currentCells[subcellSize * currentCell + l] = node;
                  usedNodes[node] = true;
//                  System.out.printf("%12d ", node);
               }
               else
               {
                  int n0 = addNodes[nSimplexNodes][subcellNodes[l]][0];
                  int n1 = addNodes[nSimplexNodes][subcellNodes[l]][1];
                  int p0 = slicedSimplex[n0];
                  int p1 = slicedSimplex[n1];
//                  System.out.printf(" %2d %2d %2d %2d ", n0, n1, p0, p1);
                  long key = p0 < p1 ? (long)p1 << 32 | (long)p0 : (long)p0 << 32 | (long)p1;

                  NewNode node = newNodes.get(key);
                  if (node == null)
                  {
                     newNodes.put(key, new NewNode(nNewNodes, p0, p1, 
                                                   slicedVals[n1] / (slicedVals[n1] -  slicedVals[n0])));
                     currentCells[subcellSize * currentCell + l] = nInNodes + nNewNodes;
                     nNewNodes += 1;
                  }
                  else
                  {
                     currentCells[subcellSize * currentCell + l] = nInNodes + node.index;
                  }
               }
//               System.out.println("");
            }
            totalNewCells[subcellType] += 1;
            currentNewCells[subcellType] += 1;
            totalOutputCells += 1;
         }
   }
   
   public void addCellTriangulation(int iCell0)
   {
      int[] slicedCell = new int[] {iCell0, iCell0 + 1};
      addCellTriangulation(slicedCell, iCell0 % 2 == 0);
   }
   
   public void addCellTriangulation(int iCell0, int iCell1)
   {
      int k = iCell1 * dims[0] + iCell0;
      int[] slicedCell = new int[] {k, k + 1, k + dims[0] + 1, k + dims[0]};
      addCellTriangulation(slicedCell, (iCell0 + iCell1)% 2 == 0);
   }
   
   public void addCellTriangulation(int iCell0, int iCell1, int iCell2)
   {
      int l = dims[1] * dims[0];
      int k = (iCell2 * dims[1] + iCell1) * dims[0] + iCell0;
      int[] slicedCell = new int[] {k,     k + 1,     k + dims[0] + 1,     k + dims[0], 
                                    k + l, k + l + 1, k + l + dims[0] + 1, k + l + dims[0]};
      addCellTriangulation(slicedCell, (iCell0 + iCell1 + iCell2)% 2 == 0);
   }
   
   
   public void addCellTriangulation(int[] cell,  boolean even)
   {
         for (int l = 0; l < cell.length; l++)
            usedNodes[cell[l]] = true;
         int newCellType;
         int[][] cellTriangulation;
         switch (cellType)
         {
         case Cell.POINT:
            newCellType = Cell.SEGMENT;
            cellTriangulation = Point.triangulationVertices(cell);
            break;
         case Cell.SEGMENT:
            newCellType = Cell.SEGMENT;
            cellTriangulation = Segment.triangulationVertices(cell);
            break;
         case Cell.QUAD:
            newCellType = Cell.TRIANGLE;
            cellTriangulation = Quad.triangulationVertices(cell);
            break;
         case Cell.HEXAHEDRON:
            newCellType = Cell.TETRA;
            cellTriangulation = RegularHex.triangulationVertices(cell, even);
            break;
         default:
            return;
         }
         for (int i = 0; i < cell.length; i++)
           usedNodes[cell[i]] = true;
         for (int i = 0; i < cellTriangulation.length; i++)
         {
            int[] simplex = cellTriangulation[i];
            int currentCell = currentNewCells[newCellType];
            int[] currentCells;
            int[] currentDataIndices;
            if (currentCell >= CHUNK)
            {
               currentCells = new int[Cell.nv[newCellType] * CHUNK];
               newCells[newCellType].add(currentCells);
               currentCell = currentNewCells[newCellType] = 0; 
            }
            else
               currentCells = newCells[newCellType].get(newCells[newCellType].size() - 1);
            System.arraycopy(simplex, 0, currentCells, simplex.length * currentCell, simplex.length);
            totalNewCells[newCellType] += 1;
            currentNewCells[newCellType] += 1;
            totalOutputCells += 1;
         }
   }
 
   public IrregularField createOutField(float[] normal)
   {
      int k = 0;
      if (totalOutputCells == 0)
         return null;
      nOldNodes = 0;
      for (int i = 0; i < usedNodes.length; i++)
         if (usedNodes[i])
            nOldNodes += 1;
      
      nOutNodes = nOldNodes + nNewNodes;
      if (nOutNodes == 0)
         return null;
      newNodesArray = new NewNode[nNewNodes];
      for (NewNode newNode : newNodes.values())
         newNodesArray[newNode.index] = newNode;
      
      globalNewNodeIndices = new int[nInNodes + nNewNodes];
      Arrays.fill(globalNewNodeIndices, -1);
      int cNode = 0;
      for (int i = 0; i < usedNodes.length; i++)
         if (usedNodes[i])
         {
            globalNewNodeIndices[i] = cNode;
            cNode += 1;
         }
      for (int i = 0; i < nNewNodes; i++, cNode++)
         globalNewNodeIndices[nInNodes + i] = cNode;
      outField = new IrregularField(nOutNodes);
      
      float[] coords = inField.getCoords();
      float[] outCoords = new float[3 * nOutNodes];
      if (coords != null)
      {
         for (int i = 0; i < usedNodes.length; i++)
            if (usedNodes[i])
            {
               System.arraycopy(coords, 3 * i, outCoords, k, 3);
               k += 3;
            }   
         for (int i = 0; i < nNewNodes; i++)
         {
            int k0 = newNodesArray[i].p0;
            int k1 = newNodesArray[i].p1;
            float r = newNodesArray[i].ratio;
            for (int j = 0; j < 3; j++, k++)
               outCoords[k] = r * coords[3 * k0 + j] + (1 - r) * coords[3 * k1 + j];
         }
      }
      else
      {
         float[][] affine = inField.getAffine();
         k = 0;
         switch (dims.length)
         {
         case 1:
            for (int i0 = 0; i0 < outCoords.length; i0++)
               if (usedNodes[i0])
               {
                  for (int i = 0; i < inField.getNSpace(); i++)
                     outCoords[k + i] = affine[3][i] + i0 * affine[0][i];
                  k += 3;
               }
            break;
         case 2:
            for (int i1 = 0, l = 0; i1 <  dims[1]; i1++)
               for (int i0 = 0; i0 < dims[0]; i0++)
                  if (usedNodes[l])
                     {
                     for (int i = 0; i < inField.getNSpace(); i++)
                        outCoords[3 * k + i] = affine[3][i] + i1 * affine[1][i] + i0 * affine[0][i];
                        k += 3;
                     }
            break;
         case 3:
            for (int i2 = 0, l = 0; i2 < dims[2]; i2++)
               for (int i1 = 0; i1 < dims[1]; i1++)
                  for (int i0 = 0; i0 < dims[0]; i0++, l++)
                     if (usedNodes[l])
                     {
                        for (int i = 0; i < inField.getNSpace(); i++)
                           outCoords[k + i] = affine[3][i] + i2 * affine[2][i] + i1 * affine[1][i] + i0 * affine[0][i];
//                        System.out.printf("x%2d =         p%1d [%5.3f %5.3f %5.3f]%n",k/3, l, outCoords[k], outCoords[k+1], outCoords[k+2]);
                        k += 3;
                     }
            break;
         }
         for (int inewNode = 0; inewNode < nNewNodes; inewNode++)
         {
            int k0 = newNodesArray[inewNode].p0;
            int k1 = newNodesArray[inewNode].p1;
            float[] c0 = new float[3];
            float[] c1 = new float[3];
            float r = newNodesArray[inewNode].ratio;
            int i00, i01, i02, i10, i11, i12;
            switch (dims.length)
            {
            case 1:
               for (int i = 0; i < inField.getNSpace(); i++)
               {
                  c0[i] = affine[3][i] + k0 * affine[0][i];
                  c1[i] = affine[3][i] + k1 * affine[0][i];
               }
               break;
            case 2:
               i00 = k0 % dims[0];
               i01 = k0 / dims[0];
               i10 = k1 % dims[0];
               i11 = k1 / dims[0];
               for (int i = 0; i < inField.getNSpace(); i++)
               {
                  c0[i] = affine[3][i] + i01 * affine[1][i] + i00 * affine[0][i];
                  c1[i] = affine[3][i] + i11 * affine[1][i] + i10 * affine[0][i];
               }
               break;
            case 3:
               i00 = k0 % dims[0];
               i01 = k0 / dims[0];
               i02 = i01 / dims[1];
               i01 %= dims[1];
               i10 = k1 % dims[0];
               i11 = k1 / dims[0];
               i12 = i11 / dims[1];
               i11 %= dims[1];
               for (int i = 0; i < inField.getNSpace(); i++)
               {
                  c0[i] = affine[3][i] + i02 * affine[2][i] + i01 * affine[1][i] + i00 * affine[0][i];
                  c1[i] = affine[3][i] + i12 * affine[2][i] + i11 * affine[1][i] + i10 * affine[0][i];
               }
               break;
            }
            for (int j = 0; j < 3; j++, k++)
               outCoords[k] = r * c0[j] + (1 - r) * c1[j];
//            System.out.printf("x%2d = %5.3f * p%d [%5.3f %5.3f %5.3f] + %5.3f * p%d [%5.3f %5.3f %5.3f] = [%5.3f %5.3f %5.3f] %n", 
//                      k/3,  r,   k0, c0[0], c0[1], c0[2], 1-r, k1, c1[0], c1[1], c1[2], outCoords[k-3], outCoords[k-2], outCoords[k-1]);
         }
      }
      outField.setCoords(outCoords);
      interpolateData();
      int[] indices = new int[nOutNodes];
      for (int i = 0; i < indices.length; i++)
        indices[i] = i;
      outField.addData(DataArray.create(indices, 1, "indice"));
      
      CellSet outCS = new CellSet(inField.getName() + "split");

      for (int iArray = 0; iArray < Cell.TYPES; iArray++)
      {
         int nVertsInCell = Cell.nv[iArray];
         int nCellsInArray = totalNewCells[iArray];
         if (nCellsInArray < 1)
            continue;
         ArrayList<int[]> cellsInArray = newCells[iArray];
         int[] cellNodes = new int[nVertsInCell * nCellsInArray];
         boolean[] orientations = new boolean[nCellsInArray];
         k = 0;
         for (int i = 0; i < cellsInArray.size(); i++)
         {
            int n = CHUNK;
            if (i == cellsInArray.size() - 1)
               n = nCellsInArray % CHUNK;
            int[] nodesChunk = cellsInArray.get(i);
            int[] cellVerts = new int[nVertsInCell];
            for (int j = 0; j < n; j++, k++)
            {
               for (int l = 0; l < cellVerts.length; l++)
               {
                  cellVerts[l] = globalNewNodeIndices[nodesChunk[j * nVertsInCell + l]];
//                  System.out.printf("%2d ", cellVerts[l]);
               }
//               System.out.println("");
               int orientation = 0;
               Cell cell = Cell.createCell(iArray, 3, cellVerts, true);
               if (iArray <= Cell.QUAD)
                  orientation = cell.geomOrientation(outCoords, normal);
               else
                  orientation = cell.geomOrientation(outCoords);
               System.arraycopy(cell.getVertices(), 0, cellNodes, k * nVertsInCell, nVertsInCell);
               orientations[k] = orientation > 0;
            }
         }
         outCS.addCells(new CellArray(iArray, cellNodes, orientations, null));
      }
      outCS.generateDisplayData(outCoords);
      outField.addCellSet(outCS);
      return outField;
   }
   
   private void interpolateData()
   {
      for (int iData = 0; iData < inField.getNData(); iData++)
      {
         DataArray inDA = inField.getData(iData);
         if (!inDA.isSimpleNumeric())
            continue;
         int vlen = inDA.getVeclen();
         DataArray outDA;
         int k = 0;
         switch (inDA.getType())
         {
            case DataArray.FIELD_DATA_BYTE:
               break;
            case DataArray.FIELD_DATA_INT:
               int[] inID = inDA.getIData();
               int[] outID = new int[vlen * nOutNodes];
               for (int i = 0; i < usedNodes.length; i++)
                  if (usedNodes[i])
                  {
                     System.arraycopy(inID, vlen * i, outID, k, vlen);
                     k += vlen;
                  }
               for (int i = 0; i < nNewNodes; i++)
               {
                  int k0 = newNodesArray[i].p0;
                  int k1 = newNodesArray[i].p1;
                  float r = newNodesArray[i].ratio;
                  for (int j = 0; j < vlen; j++, k++)
                     outID[k] = (int)(r * inID[vlen * k0 + j] + (1 - r) * inID[vlen * k1 + j]);
               }
               
               outDA = DataArray.create(outID, vlen, inDA.getName());
               outDA.setMaxv(inDA.getMaxv());
               outDA.setMinv(inDA.getMinv());
               outField.addData(outDA);
               break;
            case DataArray.FIELD_DATA_FLOAT:
               float[] inFD = inDA.getFData();
               float[] outFD = new float[vlen * nOutNodes];
               for (int i = 0; i < nInNodes; i++)
                  if (usedNodes[i])
                  {
                     System.arraycopy(inFD, vlen * i, outFD, k, vlen);
                     k += vlen;
                  }
               for (int i = 0; i < nNewNodes; i++)
               {
                  int k0 = newNodesArray[i].p0;
                  int k1 = newNodesArray[i].p1;
                  float r = newNodesArray[i].ratio;
                  for (int j = 0; j < vlen; j++, k++)
                     outFD[k] = r * inFD[vlen * k0 + j] + (1 - r) * inFD[vlen * k1 + j];
               }
               outDA = DataArray.create(outFD, vlen, inDA.getName());
               outDA.setMaxv(inDA.getMaxv());
               outDA.setMinv(inDA.getMinv());
               outField.addData(outDA);
               break;
         }
      }
   }
   
}
