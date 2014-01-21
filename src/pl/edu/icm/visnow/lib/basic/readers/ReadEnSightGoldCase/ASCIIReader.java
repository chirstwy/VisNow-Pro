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

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Vector;
import pl.edu.icm.visnow.datasets.CellArray;
import pl.edu.icm.visnow.datasets.CellSet;
import pl.edu.icm.visnow.datasets.IrregularField;
import pl.edu.icm.visnow.datasets.cells.Cell;
import pl.edu.icm.visnow.datasets.dataarrays.DataArray;
import pl.edu.icm.visnow.gui.widgets.FileErrorFrame;
import pl.edu.icm.visnow.lib.utils.io.InputSource;

/**
 *
 * @author Krzysztof S. Nowinski, University of Warsaw ICM
 */
public class ASCIIReader extends Reader
{

   private String line = null;
   private String[] tokens = null;
   private int currentLine = 0;
   private LineNumberReader reader = null;

   private enum nodeId
   {

      NONE, IGNORE, USE, ASSIGN
   };
   private nodeId useNodeId = nodeId.NONE;
   private nodeId useElementId = nodeId.NONE;
   private int cNode = 0;
   private float[] coords;
   private int[] parts;

   public ASCIIReader()
   {
   }

   private String nextLine()
   {
      try
      {
         do
            line = reader.readLine().trim();
         while (line.isEmpty());
         return line;
      } catch (Exception e)
      {
         return null;
      }
   }

   private String nextLine(String begin)
   {
      String lBegin = begin.toLowerCase();
      if (line != null && line.toLowerCase().startsWith(lBegin))
         return line;
      try
      {
         do
         {
            line = reader.readLine().trim();
            if (line == null)
               return line;
            if (line.trim().toLowerCase().startsWith(lBegin))
               return line;
         } while (true);
      } catch (Exception e)
      {
         e.printStackTrace();
      }
      return null;
   }

   private String nextLine(String[] begin)
   {
      String[] lBegin = new String[begin.length];
      for (int i = 0; i < begin.length; i++)
      {
         lBegin[i] = begin[i].toLowerCase();
         if (line != null && line.toLowerCase().startsWith(lBegin[i]))
            return line;
      }
      try
      {
         do
         {
            line = reader.readLine().trim();
            for (int i = 0; i < lBegin.length; i++)
               if (line.toLowerCase().startsWith(lBegin[i]))
                  return line;
         } while (line != null);
      } catch (Exception e)
      {
      }
      return null;
   }

   private void countItems()
   {
      int[] counts = null;
      String name = null;
      partCounts = new Vector<int[]>();
      partNames = new Vector<String>();
      while (nextLine(enSightSectionNames) != null)
         if (line.toLowerCase().startsWith("part"))
         {
            if (counts != null)
            {
               partCounts.add(counts);
               partNames.add(name);
            }
            counts = new int[9];
            for (int i = 0; i < counts.length; i++)
               counts[i] = 0;
            name = (nextLine() + " " + nextLine() + "                                 ").substring(0, 31);
         } else
            for (int i = 0; i < counts.length; i++)
               if (line.toLowerCase().startsWith(enSightSectionNames[i]))
               {
                  counts[i] += Integer.parseInt(nextLine());
                  break;
               }
      if (counts != null)
      {
         partCounts.add(counts);
         partNames.add(name);
      }
      System.out.println("    number                 name     nodes     points   segments  triangles      quads     tetras   pyramids     prisms  hexahedra");
      for (int i = 0; i < partCounts.size(); i++)
      {
         counts = partCounts.get(i);
         System.out.print(partNames.get(i));
         for (int j = 0; j < counts.length; j++)
            System.out.printf("%10d ", counts[j]);
         System.out.println("");
      }
   }

   private void readPartAsCellSet(IrregularField outField, int partN) throws IOException
   {
      String name = (nextLine() + " " + nextLine() + "                                 ").trim();
      if (name.length() > 20)
         name = name.substring(0, 20);
      nextLine("coordinates");
      int n = Integer.parseInt(nextLine());
      int[] nodeIdx = new int[n];
      System.out.println("reading indices and coords for " + n + " nodes");
      if (useNodeId == nodeId.USE)
         for (int j = 0; j < n; j++)
            nodeIdx[j] = Integer.parseInt(nextLine()) - 1;
      else if (useNodeId == nodeId.ASSIGN)
         for (int j = 0; j < n; j++)
            nodeIdx[j] = j;
      for (int j = 0; j < n; j++)
         coords[3 * nodeIdx[j]] = Float.parseFloat(nextLine());
      for (int j = 0; j < n; j++)
         coords[3 * nodeIdx[j] + 1] = Float.parseFloat(nextLine());
      for (int j = 0; j < n; j++)
         coords[3 * nodeIdx[j] + 2] = Float.parseFloat(nextLine());
      cNode += n;
      CellSet cs = new CellSet(name);
      CellArray[] arrays = new CellArray[Cell.TYPES];
      int[] cellN = new int[Cell.TYPES];
      for (int j = 0; j < cellN.length; j++)
         cellN[j] = 0;
      for (int j = 0; j < arrays.length; j++)
         if (partCounts.get(partN)[j + 1] != 0)
         {
            int[] nodes = new int[Cell.nv[j] * partCounts.get(partN)[j + 1]];
            int[] cellIdx = new int[partCounts.get(partN)[j + 1]];
            boolean[] orientations = new boolean[partCounts.get(partN)[j + 1]];
            for (int k = 0; k < orientations.length; k++)
            {
               orientations[k] = true;
               cellIdx[k] = k;
            }
            arrays[j] = new CellArray(j, nodes, orientations, cellIdx);
         }
      while (nextLine(enSightPartSectionNames) != null)
      {
         if (line.toLowerCase().startsWith("part"))
            break;
         for (int j = 0; j < Cell.TYPES; j++)
            if (line.toLowerCase().startsWith(enSightCellNames[j]))
            {
               int nCells = Integer.parseInt(nextLine());
               int ccn = cellN[j];
               int[] nodes = arrays[j].getNodes();
               int[] cellIdx = arrays[j].getDataIndices();
               if (useElementId == nodeId.USE)
                  for (int k = 0; k < nCells; k++)
                     cellIdx[ccn + k] = Integer.parseInt(nextLine());
               else if (useElementId == nodeId.ASSIGN)
                  for (int k = 0; k < nCells; k++)
                     cellIdx[ccn + k] = k;

               for (int k = 0, m = Cell.nv[j] * ccn; k < nCells; k++)
               {
                  tokens = nextLine().split("\\s+");
                  for (int l = 0; l < Cell.nv[j]; l++, m++)
                     nodes[m] = nodeIdx[Integer.parseInt(tokens[l]) - 1];
               }
               ccn += nCells;
            }
      }
      for (int j = 0; j < arrays.length; j++)
         if (partCounts.get(partN)[j + 1] != 0)
            cs.addCells(arrays[j]);
      cs.generateDisplayData(coords);
      outField.addCellSet(cs);
   }

   private void readPart(int partN, int[][] allNodes, int[] cellN, int[] parts) throws IOException
   {
      String name = (nextLine() + " " + nextLine() + "                                 ").trim();
      if (name.length() > 20)
         name = name.substring(0, 20);
      nextLine("coordinates");
      int n = Integer.parseInt(nextLine());
      int[] nodeIdx = new int[n];
      System.out.println("reading indices and coords for " + n + " nodes");
      if (useNodeId == nodeId.USE)
         for (int j = 0; j < n; j++)
         {
            nodeIdx[j] = Integer.parseInt(nextLine());
            parts[nodeIdx[j]] = partN;
         }
      else if (useNodeId == nodeId.ASSIGN)
         for (int j = 0; j < n; j++)
         {
            nodeIdx[j] = j + 1;
            parts[nodeIdx[j]] = partN;
         }
      for (int j = 0; j < n; j++)
         coords[3 * nodeIdx[j]] = Float.parseFloat(nextLine());
      for (int j = 0; j < n; j++)
         coords[3 * nodeIdx[j] + 1] = Float.parseFloat(nextLine());
      for (int j = 0; j < n; j++)
         coords[3 * nodeIdx[j] + 2] = Float.parseFloat(nextLine());
      cNode += n;
      while (nextLine(enSightPartSectionNames) != null)
      {
         if (line.toLowerCase().startsWith("part"))
            break;
         for (int j = 0; j < Cell.TYPES; j++)
            if (line.toLowerCase().startsWith(enSightCellNames[j]))
            {
               int nCells = Integer.parseInt(nextLine());
               int ccn = cellN[j];
               int[] nodes = allNodes[j];
               for (int k = 0; k < nCells; k++)
                  nextLine();
               for (int k = 0, m = Cell.nv[j] * ccn; k < nCells; k++)
               {
                  tokens = nextLine().split("\\s+");
                  for (int l = 0; l < Cell.nv[j]; l++, m++)
                  {
                     int nodeI = Integer.parseInt(tokens[l]);
                     for (int i = 0; i < nodeIdx.length; i++)
                        if (nodeIdx[i] == nodeI)
                           nodes[m] = i;
                  }
               }
               cellN[j] += nCells;
            }
      }
   }

   @Override
   public DataArray readEnSightGoldVariable(Params params, int nData, int veclen, String name, String filename, FileErrorFrame errorFrame)
   {
      float[] data = new float[nData * veclen];
      System.out.println("reading " + name + " from " + filename);
      try
      {
         File inFile = null;
         switch (params.getSource())
         {
            case InputSource.FILE:
               inFile = new File(filename);
               if (inFile == null)
                  return null;
               reader = new LineNumberReader(new FileReader(new File(filename)));
               break;
            case InputSource.URL:
               URL url = new URL(filename);
               URLConnection urlConnection = url.openConnection();
               reader = new LineNumberReader(new InputStreamReader(urlConnection.getInputStream()));
               break;
         }
         line = "";
         for (int part = 0, start = 0; part < partCounts.size(); part++)
         {
            nextLine("part");
            line = reader.readLine().trim();
            System.out.println("reading part " + line);
            int n = partCounts.get(part)[0];
            nextLine("coordinates");
            for (int i = 0; i < veclen; i++)
               for (int j = 0, k = start + i; j < n; j++, k += veclen)
                  data[k] = Float.parseFloat(reader.readLine().trim());
            start += veclen * n;
         }

         reader.close();
      } catch (FileNotFoundException e)
      {
         System.out.println("could not open file " + filename);
         return null;
      } catch (Exception e)
      {
         errorFrame.setErrorData("Error parsing EnSight geometry file ", filename, currentLine, e);
         e.printStackTrace();
         return null;
      }
      return DataArray.create(data, veclen, name);
   }

   @Override
   public IrregularField readEnSightGoldGeometry(Params params, String filename, FileErrorFrame errorFrame)
   {
      IrregularField outField = null;
      try
      {
         File inFile = null;
         switch (params.getSource())
         {
            case InputSource.FILE:
               inFile = new File(filename);
               if (inFile == null)
                  return null;
               reader = new LineNumberReader(new FileReader(new File(filename)));
               break;
            case InputSource.URL:
               URL url = new URL(filename);
               URLConnection urlConnection = url.openConnection();
               reader = new LineNumberReader(new InputStreamReader(urlConnection.getInputStream()));
               break;
         }
         int nNodes = 0;
         countItems();
         reader.close();
         for (int i = 0; i < partCounts.size(); i++)
            nNodes += partCounts.get(i)[0];
         System.out.println("total " + nNodes + " nodes ");
         coords = new float[3 * nNodes];
         outField = new IrregularField(nNodes);
         outField.setName("case " + inFile.getName().split("\\\\|/|[.]")[0]);
         outField.setNSpace(3);
         float[][] extents = null;
         switch (params.getSource())
         {
            case InputSource.FILE:
               inFile = new File(filename);
               if (inFile == null)
                  return null;
               outField.setName("case " + inFile.getName().split("\\\\|/|[.]")[0]);
               reader = new LineNumberReader(new FileReader(new File(filename)));
               break;
            case InputSource.URL:
               URL url = new URL(filename);
               URLConnection urlConnection = url.openConnection();
               reader = new LineNumberReader(new InputStreamReader(urlConnection.getInputStream()));
               break;
         }
         while (!(nextLine().toLowerCase().startsWith("part")))
         {
            if (line.toLowerCase().startsWith("node"))
            {
               tokens = line.split("\\s+");
               if (tokens[2].equalsIgnoreCase("assign"))
                  useNodeId = nodeId.ASSIGN;
               if (tokens[2].equalsIgnoreCase("ignore"))
                  useNodeId = nodeId.IGNORE;
               if (tokens[2].equalsIgnoreCase("given"))
                  useNodeId = nodeId.USE;
            }
            if (line.toLowerCase().startsWith("element"))
            {
               tokens = line.split("\\s+");
               if (tokens[2].equalsIgnoreCase("assign"))
                  useElementId = nodeId.ASSIGN;
               if (tokens[2].equalsIgnoreCase("ignore"))
                  useElementId = nodeId.IGNORE;
               if (tokens[2].equalsIgnoreCase("given"))
                  useElementId = nodeId.USE;
            }

            if (line.toLowerCase().startsWith("extents"))
            {
               extents = new float[2][3];
               for (int i = 0; i < 3; i++)
               {
                  line = reader.readLine();
                  extents[0][i] = Float.parseFloat(line.substring(0, 12));
                  extents[1][i] = Float.parseFloat(line.substring(12));
               }
            }
         }
         cNode = 0;
         if (params.materialsAsSets())
            for (int i = 0; i < partCounts.size(); i++)
            {
               nextLine("part");
               readPartAsCellSet(outField, i);
            }
         else
         {
            parts = new int[nNodes];
            CellSet cs = new CellSet();
            CellArray[] arrays = new CellArray[Cell.TYPES];
            int[] nCells = new int[Cell.TYPES];
            int[] cellN = new int[Cell.TYPES];
            int[][] nodes = new int[Cell.TYPES][];
            for (int j = 0; j < cellN.length; j++)
               cellN[j] = nCells[j] = 0;
            for (int partN = 0; partN < partCounts.size(); partN++)
               for (int j = 0; j < Cell.TYPES; j++)
                  nCells[j] += partCounts.get(partN)[j + 1];
            for (int j = 0; j < Cell.TYPES; j++)
               if (nCells[j] > 0)
                  nodes[j] = new int[Cell.nv[j] * nCells[j]];
            for (int partN = 0; partN < partCounts.size(); partN++)
            {
               nextLine("part");
               readPart(partN, nodes, cellN, parts);
            }
            for (int j = 0; j < arrays.length; j++)
               if (nCells[j] != 0)
               {
                  boolean[] orientations = new boolean[nCells[j]];
                  int[] cellIdx = new int[nCells[j]];
                  for (int k = 0; k < orientations.length; k++)
                  {
                     orientations[k] = true;
                     cellIdx[k] = k;
                  }
                  cs.addCells(new CellArray(j, nodes[j], orientations, cellIdx));
               }

            cs.generateDisplayData(coords);
            outField.addCellSet(cs);
            outField.addData(DataArray.create(parts, 1, "parts"));
         }
         outField.setCoords(coords);
         if (extents != null)
            outField.setExtents(extents);
         reader.close();
      } catch (FileNotFoundException e)
      {
         System.out.println("could not open file " + filename);
      } catch (Exception e)
      {
         errorFrame.setErrorData("Error parsing EnSight geometry file ", filename, currentLine, e);
         e.printStackTrace();
         return null;
      }
      return outField;
   }
}
