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

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Scanner;
import java.util.Vector;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import pl.edu.icm.visnow.datasets.IrregularField;
import pl.edu.icm.visnow.datasets.cells.Cell;
import pl.edu.icm.visnow.datasets.dataarrays.DataArray;
import pl.edu.icm.visnow.engine.core.OutputEgg;
import pl.edu.icm.visnow.geometries.parameters.AbstractRenderingParams;
import pl.edu.icm.visnow.gui.widgets.FileErrorFrame;
import pl.edu.icm.visnow.lib.templates.visualization.modules.IrregularOutFieldVisualizationModule;
import pl.edu.icm.visnow.lib.types.VNIrregularField;
import pl.edu.icm.visnow.lib.utils.SwingInstancer;
import pl.edu.icm.visnow.lib.utils.io.InputSource;

/**
 * @author  Krzysztof S. Nowinski (know@icm.edu.pl)
 * Warsaw University, Interdisciplinary Centre
 * for Mathematical and Computational Modelling
 */
public class ReadEnSightGoldCase extends IrregularOutFieldVisualizationModule
{

   protected GUI computeUI = null;
   protected Params params;
   protected String lastFileName = " ";
   protected FileErrorFrame errorFrame = null;
   protected Cell[] stdCells = new Cell[Cell.TYPES];
   protected boolean ignoreUI = false;
   protected Scanner scanner = null;
   protected Vector<int[]> partCounts = new Vector<int[]>();
   protected Vector<String> partNames = new Vector<String>();
   

   protected String nextLine()
   {
      String line;
      try
      {
         do
         {
            line = scanner.nextLine().trim();
            System.out.println(line);
         } while (line.isEmpty());
         return line;
      } catch (Exception e)
      {
         return null;
      }
   }

   protected String nextLine(String[] begin)
   {
      String[] lBegin = new String[begin.length];
      for (int i = 0; i < begin.length; i++)
         lBegin[i] = begin[i].toLowerCase();
      String line;
      try
      {
         while ((line = scanner.nextLine().trim()) != null)
         {
            System.out.println(line);
            for (int i = 0; i < lBegin.length; i++)
               if (line.toLowerCase().startsWith(lBegin[i]))
                   return line;
         }
      } catch (Exception e)
      {
      }
      return null;
   }

   /**
    * Creates a new instance of CreateGrid
    */
   public ReadEnSightGoldCase()
   {
      for (int i = 0; i < stdCells.length; i++)
         stdCells[i] = Cell.createCell(i, 3, new int[Cell.nv[i]], true);
      parameters = params = new  Params();
      params.addChangeListener(new ChangeListener()
      {
         @Override
         public void stateChanged(ChangeEvent evt)
         {
            startAction();
         }
      });
      SwingInstancer.swingRunAndWait(new Runnable()
      {
         @Override
         public void run()
         {
            computeUI = new GUI("EnSight Gold reader", "EnSight Gold case file", 
                                new String[]{"case", "CASE", "encas", "ENCAS"});
            errorFrame = new FileErrorFrame();
         }
      });
      computeUI.setParams(params);
      ui.addComputeGUI(computeUI);
      setPanel(ui);
   }

   @Override
   public boolean isGenerator() {
      return true;
   }
   
   private File caseFile = null;
      
   private Scanner getScanner()
   {
      URL url = null;
      Scanner sc = null;
      try
      {
         if (params.getSource() == InputSource.URL)
         {
            sc = new Scanner(new InputStreamReader(new URL(params.getFileName()).openConnection().getInputStream()));
         } else
         {
            caseFile = new File(params.getFileName());
            sc = new Scanner(new FileReader(caseFile));
         }
      } catch (IOException iOException)
      {
      }
      return sc;
   }
   
   class TimeSet
   {
      int index;
      String description;
      Vector<Float> timeSteps = new Vector<Float>();
      Vector<String> timeFileSuffixes = new Vector<String>();

      public TimeSet(int index, String description)
      {
         this.index = index;
         this.description = description;
      }

      public void add(float time, String suffix)
      {
         timeSteps.add(time);
         timeFileSuffixes.add(suffix);
      }
      
      public float getTime(int i)
      {
         return timeSteps.get(i);
      }
      
      public String getSuffix(int i)
      {
         return timeFileSuffixes.get(i);
      }
   }
   
   private IrregularField readEnsightGoldCase()
   {
      
      URL url = null;
      caseFile = null;
      boolean binary = false;
      String line;
      Vector<TimeSet> timeSets = new Vector<TimeSet>();
      
      scanner = getScanner();
      if (scanner == null)
         return null;
      if (nextLine(new String[]{"# EnSight Gold", "#EnSight Gold"}) == null)
         return null;
      
      while ((line = nextLine(new String[]{"TIME", "FILE"})) != null)
      {
         if (line.trim().toUpperCase().startsWith("TIME"))
         {
            int n;
            int nSteps;
            line = scanner.nextLine();
            String[] items = nextLine().split("\\s+");
            StringBuilder desc = new StringBuilder();
            try
            {
               n = Integer.parseInt(items[2]);
               if (items.length > 3)
                  for (int i = 3; i < items.length; i++)
                     desc.append(items[i]).append(" ");
            } catch (Exception e)
            {
               continue;
            }
            TimeSet timeSet = new TimeSet(n, desc.toString());
            try
            {
               nSteps = Integer.parseInt(scanner.nextLine().split("\\s+")[3]);
            } catch (Exception e)
            {
               continue;
            }
            
         }
      }
      scanner.close();
      
      scanner = getScanner();
      
      if (nextLine(new String[]{"GEOMETRY"}) == null)
         return null;
      String[] items = nextLine().split("\\s+");
      if (!"model:".equalsIgnoreCase(items[0]))
         return null;     
      Reader reader = binary ? new BinaryReader() : new ASCIIReader();
      outField = reader.readEnSightGoldGeometry(params, caseFile.getParent() + File.separator + items[items.length - 1], errorFrame);

      partCounts = reader.getPartCounts();
      partNames  = reader.getPartNames();
      
      while (nextLine(new String[]{"VARIABLES"}) != null)
      {
         items = nextLine().split("\\s+");
         if (!items[2].startsWith("node"))
            continue;
         int veclen = 0;
         if (items[0].equalsIgnoreCase("scalar"))
            veclen = 1;
         else if (items[0].equalsIgnoreCase("vector"))
            veclen = 3;
         else if (items[0].equalsIgnoreCase("tensor"))
            veclen = 6;
         DataArray da = null;
         reader = binary ? new BinaryReader() : new ASCIIReader();
         reader.setPartCounts(partCounts);
         reader.setPartNames(partNames);
         da = reader.readEnSightGoldVariable(params, outField.getNNodes(), veclen, items[5], 
                                             caseFile.getParent() + File.separator + items[items.length - 1], errorFrame);
         if (da != null)
            outField.addData(da);
      }
      return outField;
   }

   public static OutputEgg[] outputEggs = null;

   @Override
   public void onActive()
   {
      outField = readEnsightGoldCase();
      if (outField == null)
         return;
      int[] nodeIdx = new int[outField.getNNodes()];
      for (int i = 0; i < nodeIdx.length; i++)
         nodeIdx[i] = i;
      outField.addData(DataArray.create(nodeIdx, 1, "node indices"));
      computeUI.setFieldDescription(outField.description());
      if (params.isShow())
      {
         prepareOutputGeometry();
         irregularFieldGeometry.getFieldDisplayParams().setShadingMode(AbstractRenderingParams.FLAT_SHADED);
         show();
      }
      setOutputValue("UCD field", new VNIrregularField(outField));
   }
   
   @Override
   public void onInitFinishedLocal() {
       if(isForceFlag()) 
           computeUI.activateOpenDialog();
   }
   
}
