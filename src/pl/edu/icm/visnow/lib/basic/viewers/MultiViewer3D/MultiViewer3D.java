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

package pl.edu.icm.visnow.lib.basic.viewers.MultiViewer3D;

import java.util.Vector;
import javax.swing.JFrame;
import pl.edu.icm.visnow.engine.core.InputEgg;
import pl.edu.icm.visnow.engine.core.LinkFace;
import pl.edu.icm.visnow.engine.core.ModuleCore;
import pl.edu.icm.visnow.engine.core.OutputEgg;
import pl.edu.icm.visnow.geometries.objects.GeometryObject;
import pl.edu.icm.visnow.geometries.viewer3d.Display3DPanel;
import pl.edu.icm.visnow.geometries.viewer3d.controls.Display3DControlsPanel;
import pl.edu.icm.visnow.lib.types.VNGeometryObject;
import pl.edu.icm.visnow.lib.utils.SwingInstancer;
import pl.edu.icm.visnow.system.main.VisNow;

/**
 * @author  Krzysztof S. Nowinski (know@icm.edu.pl)
 * Warsaw University, Interdisciplinary Centre
 * for Mathematical and Computational Modelling
 */
public class MultiViewer3D extends ModuleCore
{
   public static InputEgg[] inputEggs = null;
   public static OutputEgg[] outputEggs = null;
   private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(MultiViewer3D.class);
   protected MultiviewFrame window = null;
   protected Display3DPanel displayPanel = null;
   protected Display3DControlsPanel controlsPanel = null;
   protected GUI ui = null;

   /**
    * Creates a new instance of Viewer3D
    */
   public MultiViewer3D()
   {
      SwingInstancer.swingRunAndWait(new Runnable()
      {
         public void run()
         {
            ui = new GUI();
            ui.setViewerModule(MultiViewer3D.this);
            window = new MultiviewFrame();
            window.setTitle("Viewer3D");
            window.setBounds(0, 20, VisNow.displayWidth, VisNow.displayHeight);
//            displayPanel = window.getDisplayPanel();
//            controlsPanel = new Display3DControlsPanel(displayPanel);
//            ui.setControlsPanel(controlsPanel);
//            displayPanel.setControlsFrame(null);
//            displayPanel.setControlsPanel(controlsPanel);
            window.setVisible(true);
         }
      });
      setPanel(ui);
   }
   
   @Override
   public void onInitFinished()
   {
   }

   @Override
   public boolean isViewer()
   {
      return true;
   }
   
   void showWindow()
   {
      window.setVisible(true);
   }
   
   void setTransientControlsFrame(JFrame transientControlsFrame)
   {
      displayPanel.setTransientControlsFrame(transientControlsFrame);
   }

   @Override
   public void onDelete()
   {
      window.dispose();
   }

   @Override
   public void onInputDetach(LinkFace link)
   {
      onActive();
   }

   @Override
   public void onInputAttach(LinkFace link)
   {
      onActive();
   }

   @Override
   public void onActive()
   {
       
//      window.getDisplayPanel().setPostRenderSilent(true);

      if(!window.isVisible()) window.setVisible(true);   
      window.getUniBuilder().getScene().clearAllGeometry();
      Vector ins = getInputValues("inObject");
      for (Object obj : ins)
         if ((VNGeometryObject) obj != null)
         {
            GeometryObject geomObj = ((VNGeometryObject) obj).getGeometryObject();
            if (geomObj != null)
            {
               geomObj.detach();
               window.getUniBuilder().getScene().addGeomObject(geomObj);
            }
         }
//      window.getDisplayPanel().setPostRenderSilent(false);
//      //if(window.getDisplayPanel().isStoringFrames())
//      if(window.getDisplayPanel().isWaitingForExternalTrigger())
//          window.getDisplayPanel().forceRender();

      
      
      //window.repaint();
   }

}

