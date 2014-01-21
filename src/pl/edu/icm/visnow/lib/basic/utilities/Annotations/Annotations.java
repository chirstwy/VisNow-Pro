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


package pl.edu.icm.visnow.lib.basic.utilities.Annotations;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import pl.edu.icm.visnow.engine.core.InputEgg;
import pl.edu.icm.visnow.engine.core.OutputEgg;
import pl.edu.icm.visnow.geometries.viewer3d.eventslisteners.pick.Pick3DEvent;
import pl.edu.icm.visnow.geometries.viewer3d.eventslisteners.pick.Pick3DListener;
import pl.edu.icm.visnow.geometries.viewer3d.eventslisteners.pick.PickType;
import pl.edu.icm.visnow.lib.templates.visualization.modules.VisualizationModule;
import pl.edu.icm.visnow.lib.types.VNGeometryObject;
import pl.edu.icm.visnow.lib.utils.SwingInstancer;
import pl.edu.icm.visnow.system.main.VisNow;
import pl.edu.icm.visnow.system.utils.usermessage.Level;
import pl.edu.icm.visnow.system.utils.usermessage.UserMessage;

/**
 * @author  Krzysztof S. Nowinski (know@icm.edu.pl)
 * Warsaw University, Interdisciplinary Centre
 * for Mathematical and Computational Modelling
 */
public class Annotations extends VisualizationModule
{

   /**
    * Creates a new instance of CreateGrid
    */
   public static InputEgg[] inputEggs = null;
   public static OutputEgg[] outputEggs = null;
   private GUI ui = null;
   private Params params;
   private AnnotationsObject glyphObj = new AnnotationsObject();

   public Annotations()
   {
      parameters = params = new Params();
      params.setPick3DListener(pick3DListener);
      
      params.addChangeListener(new ChangeListener()
      {
         @Override
         public void stateChanged(ChangeEvent evt)
         {
            if (!params.getOutput().isEmpty())
            {
                writeToFile(params.getOutput());
                params.setOutput("");
            }
            else if (!params.getInput().isEmpty())
            {
               readFromFile(params.getInput());
               params.setInput("");
               
               ui.update();
               glyphObj.update(params);

            }
            else
                glyphObj.update(params);
         }
      });

      SwingInstancer.swingRunAndWait(new Runnable()
      {
         @Override
         public void run()
         {
            ui = new GUI();
            ui.setParams(params);
            setPanel(ui);
         }
      });
   }

  @Override
   public void onInitFinishedLocal()
   {
       outObj = glyphObj;
       outObj.setCreator(this);
       outObj.getGeometryObj().setUserData(new ModuleIdData(this));
       outObj2DStruct.setParentModulePort(this.getName() + ".out.outObj");
       setOutputValue("outObj", new VNGeometryObject(outObj, outObj2DStruct));
   }

   private void writeToFile(String fileName)
   {
      if (params.getTexts() == null)
         return;
      float[] coords = params.getCoords();
      String[] texts = params.getTexts();

      try
      {
         PrintWriter out = new PrintWriter(new FileOutputStream(new File(fileName)));
         out.println(texts.length);
         for (int i = 0; i < texts.length; i++)
            out.printf("%9.3f %9.3f %9.3f %s %n",
                        coords[3 * i], coords[3 * i + 1], coords[3 * i + 2], texts[i]);
         out.close();
      } catch (Exception exception)
      {
          VisNow.get().userMessageSend(this, "Error saving image.", "", Level.ERROR); 
          exception.printStackTrace();
      }
   }
   
   private void readFromFile(String fileName)
   {
      try
      {
         LineNumberReader in = new LineNumberReader(new FileReader(new File(fileName)));
         
         int nAnnotations = Integer.parseInt(in.readLine().trim());
         
         float[] coords = new float[3 * nAnnotations];
         String[] texts = new String[nAnnotations];
         
         for (int i = 0; i < nAnnotations; i++)
         {
            String[] items;
            items = (in.readLine().trim()).split("\\s+", 4);
            for (int j = 0; j < 3; j++)
               coords[3 * i + j] = Float.parseFloat(items[j]);
            texts[i] = items[3];
         }
         
         params.setCoords(coords);
         params.setTexts(texts);
         
         in.close();
      }
      catch (Exception exception)
      {
          VisNow.get().userMessageSend(this, "Error loading file.", "", Level.ERROR);
          exception.printStackTrace();
      }
   }

   @Override
   public void onActive()
   {
      glyphObj.update(params);
   }

    /** 
     * Pick3DListener
     * <p/>
     * No getter is required if
     * <code>parameters</code> store an object of class
     * <code>Params</code> and that object stores this Pick3DListener and overrides
     * <code>getPick3DListener()</code> method. */
   protected Pick3DListener pick3DListener = new Pick3DListener(PickType.POINT)
   {
      @Override
      public void handlePick3D(Pick3DEvent e)
      {
         float[] x = e.getPoint(); // x will always be not-null
         if (!params.isActive())
            return;
         ui.addPoint(x);
      }
   };

}
