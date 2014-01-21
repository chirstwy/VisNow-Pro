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

package pl.edu.icm.visnow.lib.basic.readers.ReadEnSightGoldCase;

import java.util.Vector;
import pl.edu.icm.visnow.datasets.IrregularField;
import pl.edu.icm.visnow.datasets.cells.Cell;
import pl.edu.icm.visnow.datasets.dataarrays.DataArray;
import pl.edu.icm.visnow.gui.widgets.FileErrorFrame;

/**
 *
 * @author Krzysztof S. Nowinski, University of Warsaw ICM
 */
abstract public class Reader
{
   protected static final int[][] UCDnodeOrders =
   {
      { 0 },
      { 0, 1 },
      { 0, 1, 2 },
      { 0, 1, 2, 3 },
      { 0, 1, 2, 3 },
      { 4, 0, 3, 2, 1 },
      { 0, 1, 2, 3, 4, 5 },
      { 0, 1, 2, 3, 4, 5, 6, 7 }
   };
   
   protected static String[] enSightCellNames = 
      {"point",
       "bar",
       "tria", "quad",
       "tetra", "pyramid", "penta", "hexa"};
   
   protected static String[] enSightPartSectionNames = 
      {
       "point",
       "bar",
       "tria", "quad",
       "tetra", "pyramid", "penta", "hexa",
       "part"};
   
   protected static String[] enSightSectionNames = 
      {"coordinates",
       "point",
       "bar",
       "tria", "quad",
       "tetra", "pyramid", "penta", "hexa",
       "part"};
   
   protected Cell[] stdCells = new Cell[Cell.TYPES];
   protected enum nodeId {NONE, IGNORE, USE};
   protected nodeId useNodeId = nodeId.NONE;
   protected static final int NODES      = 0;
   protected static final int POINTS     = 1;
   protected static final int SEGMENTS   = 2;
   protected static final int TRIANGLES  = 3;
   protected static final int QUADS      = 4;
   protected static final int TETRAS     = 5;
   protected static final int PYRAMIDS   = 6;
   protected static final int PRISMS     = 7;
   protected static final int HEXAHEDRAS = 8;
   protected Vector<int[]> partCounts = new Vector<int[]>();
   protected Vector<String> partNames = new Vector<String>();
   
   Reader()
   {
      for (int i = 0; i < stdCells.length; i++)
         stdCells[i] = Cell.createCell(i, 3, new int[Cell.nv[i]], true);
   }

   public Vector<int[]> getPartCounts()
   {
      return partCounts;
   }

   public void setPartCounts(Vector<int[]> partCounts)
   {
      this.partCounts = partCounts;
   }

   public Vector<String> getPartNames()
   {
      return partNames;
   }

   public void setPartNames(Vector<String> partNames)
   {
      this.partNames = partNames;
   }
   
   abstract public IrregularField readEnSightGoldGeometry(Params params, String filename, FileErrorFrame errorFrame);
   abstract public DataArray readEnSightGoldVariable(Params params, int nData, int veclen, String name, String filename, FileErrorFrame errorFrame);
   
}
