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

package pl.edu.icm.visnow.lib.basic.filters.VolumeSegmentation;

import java.util.ArrayList;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import pl.edu.icm.visnow.datasets.RegularField;
import pl.edu.icm.visnow.datasets.dataarrays.DataArray;
import pl.edu.icm.visnow.engine.core.InputEgg;
import pl.edu.icm.visnow.engine.core.LinkFace;
import pl.edu.icm.visnow.engine.core.ModuleCore;
import pl.edu.icm.visnow.engine.core.OutputEgg;
import pl.edu.icm.visnow.gui.events.FloatValueModificationEvent;
import pl.edu.icm.visnow.gui.events.FloatValueModificationListener;
import pl.edu.icm.visnow.lib.basic.filters.AnisotropicDenoiser.AbstractAnisotropicWeightedAverageCompute;
import pl.edu.icm.visnow.lib.basic.filters.AnisotropicDenoiser.AnisotropicWeightedAverageCompute;
import pl.edu.icm.visnow.lib.basic.mappers.VolumeRenderer.FieldPick.FieldPickEvent;
import pl.edu.icm.visnow.lib.basic.mappers.VolumeRenderer.FieldPick.FieldPickListener;
import pl.edu.icm.visnow.lib.basic.mappers.VolumeRenderer.VolumeRenderer;
import pl.edu.icm.visnow.lib.basic.viewers.FieldViewer3D.DataProvider.*;
import pl.edu.icm.visnow.lib.basic.viewers.FieldViewer3D.FieldDisplay3DFrame;
import pl.edu.icm.visnow.lib.basic.viewers.FieldViewer3D.GUI;
import pl.edu.icm.visnow.lib.basic.viewers.FieldViewer3D.Geometry.CalculableParams;
import pl.edu.icm.visnow.lib.basic.viewers.FieldViewer3D.Geometry.GeometryParams;
import pl.edu.icm.visnow.lib.basic.viewers.FieldViewer3D.Geometry.GeometryUI;
import pl.edu.icm.visnow.lib.basic.viewers.FieldViewer3D.Geometry.Glypher;
import pl.edu.icm.visnow.lib.basic.viewers.FieldViewer3D.ViewPanels.OrthosliceNumberChangedEvent;
import pl.edu.icm.visnow.lib.types.VNRegularField;
import static pl.edu.icm.visnow.lib.utils.CropDown.*;
import pl.edu.icm.visnow.lib.utils.SwingInstancer;
import pl.edu.icm.visnow.lib.utils.field.FieldSmoothDown;

/**
 * @author Krzysztof S. Nowinski (know@icm.edu.pl) Warsaw University, Interdisciplinary Centre for
 * Mathematical and Computational Modelling
 */
public class VolumeSegmentation extends ModuleCore
{

   public static final int FREE = 0;
   public static final int BACKGROUND = 1;
   protected FieldDisplay3DFrame display3DFrame = null;
   protected VolumeRenderer volRender = new VolumeRenderer(false);
   protected GUI ui = null;
   protected DataProvider dp = new DataProvider();
   protected GeometryParams gparams = new GeometryParams();
   protected CalculableParams cparams = new CalculableParams();
   protected GeometryUI geomUI = null;
   protected Glypher glypher = new Glypher();
   protected VolumeSegmentationUI segUI = new VolumeSegmentationUI();
   protected SegmentVolume segmentVolume;
   protected RegularField inField = null;
   protected RegularField distField = null;
   protected RegularField outField = null;
   protected int[] dims = null;
   protected byte[] outData = null;
   protected short[] distData = null;
   protected boolean[] segData = null;
   protected int[] usedComponents = null;
   protected float[] weights = null;
   protected int segIndex = 0;
   protected int maxSegNumber = 1;
   protected boolean outputReady = false;
   protected ChangeListener segmentVolumeChangeListener = null;
   protected int tollerance = 100;
   protected float low = -1;
   protected float up = 256;


   /**
    * Creates a new instance of TestViewer3D
    */
   public VolumeSegmentation()
   {
      this(true);
   }

   public VolumeSegmentation(boolean expert)
   {
      SwingInstancer.swingRunAndWait(new Runnable()
      {
         @Override
         public void run()
         {
            ui = new GUI();
            display3DFrame = new FieldDisplay3DFrame();
            geomUI = new GeometryUI(display3DFrame);
         }
      });
      display3DFrame.setLocation(100, 100);
      display3DFrame.setDataProvider(dp);
      geomUI.addChangeListener(display3DFrame.getManager());

      display3DFrame.setVisible(true);
      display3DFrame.getDisplay3DPanel().addChild(volRender.getOutObject());

      glypher.setParams(gparams);
      geomUI.setParams(gparams, cparams);
      display3DFrame.setGeometryParams(gparams);
      display3DFrame.getDisplay3DPanel().addChild(glypher.getGlyphsObject());
      glypher.addChangeListener(new ChangeListener()
      {
         @Override
         public void stateChanged(ChangeEvent e)
         {
            display3DFrame.getDisplay3DPanel().repaint();
         }
      });

      volRender.addPick3DListener(new FieldPickListener()
      {
         @Override
         public void handlePick3D(FieldPickEvent e)
         {
            gparams.addPoint(e.getIndices());
         }
      });

      ui.addChangeListener(new ChangeListener()
      {
         @Override
         public void stateChanged(ChangeEvent evt)
         {
            display3DFrame.setVisible(true);
         }
      });
      setPanel(ui);

      display3DFrame.addUIPage(segUI, "Segmentation");
      if (expert)
      {
         display3DFrame.addUI(dp.getUI(), "Slices");
         display3DFrame.addUI(volRender.getVolRenderUI(), "3D View");
      }
      display3DFrame.setTitle("Segmentation");
      segUI.setParentModule(this);
      dp.getParams().addDataProviderParamsListener(new DataProviderParamsListener()
      {
         @Override
         public void onOrthosliceNumberChanged(OrthosliceNumberChangedEvent evt)
         {
         }

         @Override
         public void onOverlayChanged(DataProviderParamsEvent evt)
         {
            segUI.getBackgroundChangeListener().stateChanged(evt);
         }

         @Override
         public void onOverlayOpacityChanged(DataProviderParamsEvent evt)
         {
         }

         @Override
         public void onRgbComponentChanged(RgbComponentChangedEvent evt)
         {
         }

         @Override
         public void onRgbComponentWeightChanged(RgbComponentWeightChangedEvent evt)
         {
         }

         @Override
         public void onCustomPlaneChanged(CustomPlaneChangedEvent evt)
         {
         }

         @Override
         public void onIsolineThresholdChanged(IsolineThresholdChangedEvent evt)
         {
         }

         @Override
         public void onCustomOrthoPlaneChanged(CustomOrthoPlaneChangedEvent evt)
         {
         }

         @Override
         public void onColormapChanged(ColormapChangedEvent evt)
         {
         }
      });

   }
   private static InputEgg[] inputEggs = null;
   private static OutputEgg[] outputEggs = null;

   public static InputEgg[] getInputEggs()
   {
      if (inputEggs == null)
      {
         inputEggs = new InputEgg[]
         {
            new InputEgg("inField", VNRegularField.class, InputEgg.NECESSARY | InputEgg.TRIGGERING)
         };
      }
      return inputEggs;
   }

   public static OutputEgg[] getOutputEggs()
   {
      if (outputEggs == null)
      {
         outputEggs = new OutputEgg[]
         {
            new OutputEgg("outField", VNRegularField.class)
         };
      }
      return outputEggs;
   }

   @Override
   public void onDelete()
   {
      display3DFrame.dispose();
      volRender = null;
      display3DFrame = null;
      dp = null;
   }

   private void prepareWorkObjects()
   {
      distField = new RegularField(dims);
      if (inField.getCoords() != null)
      {
         distField.setCoords(inField.getCoords());
      } else
      {
         distField.setAffine(inField.getAffine());
      }
      distData = new short[dims[0] * dims[1] * dims[2]];
      distField.addData(DataArray.create(distData, 1, "distances"));

      segUI.setInfield(inField);

      dp.setInField(inField);
      dp.setAuxField(distField);

      dp.resetCustomPlane();
      dp.updateOrthosliceImages();
      dp.updateCustomPlane();
      dp.centerSlices();
      volRender.setInField(inField);
      gparams.setInField(inField);

      outField = new RegularField(dims);
      if (inField.getCoords() != null)
      {
         outField.setCoords(inField.getCoords());
      } else
      {
         outField.setAffine(inField.getAffine());
      }
      outData = new byte[dims[0] * dims[1] * dims[2]];
      outField.addData(DataArray.create(outData, 1, "segmented data"));

      dp.setOverlayField(outField);

      segData = new boolean[dims[0] * dims[1] * dims[2]];
   }

   public FieldDisplay3DFrame getDisplay3DFrame()
   {
      return display3DFrame;
   }

   public RegularField getInField()
   {
      return inField;
   }

   public void setInField(RegularField in)
   {
      if (in == null)
      {
         inField = in;
         return;
      }

      dims = in.getDims();
      if (dims.length != 3 || dims[0] < 2 || dims[1] < 2 || dims[2] < 2 || in.getNData() < 1)
      {
         return;
      }
      if (segUI.presmooth() || segUI.presmoothSlices())
      {
         pl.edu.icm.visnow.lib.basic.filters.AnisotropicDenoiser.Params denoiserParams =
                 new pl.edu.icm.visnow.lib.basic.filters.AnisotropicDenoiser.Params();
         denoiserParams.setComputeBySlice(segUI.presmoothSlices());
         if (denoiserParams.isComputeBySlice())
         {
            denoiserParams.setPresmoothRadius(4);
            denoiserParams.setRadius(1);
            denoiserParams.setSlope(2);
            denoiserParams.setSlope1(4);
         } else
         {
            denoiserParams.setPresmoothRadius(4);
            denoiserParams.setRadius(1);
            denoiserParams.setSlope(2);
            denoiserParams.setSlope1(4);
         }
         RegularField anisotropyField = FieldSmoothDown.smoothDownToFloat(in, 1,
                 (float) denoiserParams.getPresmoothRadius(), denoiserParams.getNThreads());
         denoiserParams.setPresmooth(false);
         AbstractAnisotropicWeightedAverageCompute averageCompute = new AnisotropicWeightedAverageCompute();
         averageCompute.addFloatValueModificationListener(
                 new FloatValueModificationListener()
         {
            @Override
            public void floatValueChanged(FloatValueModificationEvent e)
            {
               setProgress(e.getVal());
            }
         });
         inField = averageCompute.compute(in, anisotropyField, denoiserParams);
         inField.getData(0).setPhysMin(in.getData(0).getPhysMin());
         inField.getData(0).setPhysMax(in.getData(0).getPhysMax());
      } else
      {
         inField = in;
      }
      reset();
   }

   @Override
   public void onActive()
   {
      if (!outputReady)
      {
         if (getInputFirstValue("inField") == null || ((VNRegularField) getInputFirstValue("inField")).getField() == null)
         {
            dp.setInField(null);
            dp.setAuxField(null);
            volRender.setInField(null);
            gparams.setInField(null);
            setInField(null);
            outField = null;
            return;
         }
         if (inField == ((VNRegularField) getInputFirstValue("inField")).getField())
         {
            return;
         }
         setInField(((VNRegularField) getInputFirstValue("inField")).getField());
      } else
      {
         if (outField != null)
         {
            ArrayList<String> names = segUI.getAllowedNames();
            String[] dataMap = new String[names.size() + 1];
            dataMap[0] = "MAP";
            for (int i = 0; i < dataMap.length - 1; i++)
            {
               dataMap[i + 1] = "" + i + ":" + names.get(i);
            }
            outField.getData(0).setUserData(dataMap);
            extendMargins(outField.getData(0).getBData());

            outField.setAffine(inField.getAffine());
            byte[] inData = inField.getData(0).getBData();
            byte[] visData = new byte[outData.length];
            int d = 220 / dataMap.length;
            for (int i = 0; i < visData.length; i++)
            {
               visData[i] = (byte) (0xff & ((0xff & inData[i]) / 8 + 64 + d * (0xff & outData[i])));
            }
            outField.addData(DataArray.create(visData, 1, "vis data"));
            setOutputValue("outField", new VNRegularField(outField));
         }
         outputReady = false;
      }
   }

   private void extendMargins(byte[] data)
   {
// along x axis
      for (int i = 0; i < dims[2]; i++)
      {
         for (int j = 0; j < dims[1]; j++)
         {
            int k = (i * dims[1] + j) * dims[0];
            data[k] = data[k + 1];
            data[k + dims[0] - 1] = data[k + dims[0] - 2];
         }
      }
// along y axis
      for (int i = 0; i < dims[2]; i++)
      {
         for (int j = 0; j < dims[0]; j++)
         {
            int k = i * dims[1] * dims[0] + j;
            data[k] = data[k + dims[0]];
            data[k + dims[0] * (dims[1] - 1)] = data[k + dims[0] * (dims[1] - 2)];
         }
      }
// along z axis
      for (int i = 0; i < dims[1]; i++)
      {
         for (int j = 0; j < dims[0]; j++)
         {
            int k = i * dims[0] + j;
            data[k] = data[k + dims[0] * dims[1]];
            data[k + dims[0] * dims[1] * (dims[2] - 1)] = data[k + dims[0] * dims[1] * (dims[2] - 2)];
         }
      }
   }

   public void renderVolume(int vol)
   {
      switch (vol)
      {
         case 0:
            if (inField != null)
               volRender.setInField(inField);
            break;
         case 1:
            if (distField != null)
               volRender.setInField(distField);
            break;
         case 2:
            if (outField != null)
               volRender.setInField(outField);
            break;
         default:
            if (inField != null)
               volRender.setInField(inField);
      }
   }

   public void setRange(float low, float up)
   {
      this.low = low;
      this.up = up;
      dp.getParams().setIsolineThresholds(new float[]{low, up});
   }

   public void clearLastPoint()
   {
      if (gparams != null)
      {
         gparams.clearLastPoint();
      }
   }

   public void resetSelection()
   {
      if (inField == null || segmentVolume == null)
      {
         return;
      }
      if (gparams != null)
      {
         gparams.clearPoints();
      }
   }

   public void addSelection()
   {
      updateOutField(dp.getParams().getIsolineThreshold());
      for (int i = 0; i < outData.length; i++)
      {
         if (segData[i])
         {
            outData[i] = (byte) segIndex;
         }
      }
      outField.getData(0).setMaxv(maxSegNumber - 1);
      outField.getData(0).setMinv(0);
      if (gparams != null)
      {
         gparams.clearPoints();
      }
      dp.updateOrthosliceOverlays();
      dp.updateCustomOrthoPlanesOverlays();
   }

   public void subtractSelection()
   {
      updateOutField(dp.getParams().getIsolineThreshold());
      for (int i = 0; i < outData.length; i++)
      {
         if (segData[i] && outData[i] == segIndex)
         {
            outData[i] = 0;
         }
      }
      outField.getData(0).setMaxv(maxSegNumber);
      outField.getData(0).setMinv(0);
      if (gparams != null)
      {
         gparams.clearPoints();
      }
      dp.updateOrthosliceOverlays();
      dp.updateCustomOrthoPlanesOverlays();
   }

   public int[] getVolumes()
   {
      int[] volumes = new int[256];
      for (int i = 0; i < volumes.length; i++)
      {
         volumes[i] = 0;
      }
      for (int i = 0; i < outData.length; i++)
      {
         volumes[outData[i] & 0xff] += 1;
      }
      return volumes;

   }

   public void reset()
   {
      if (inField == null)
      {
         return;
      }

      maxSegNumber = 1;

      if (outData != null)
         for (int i = 0; i < outData.length; i++)
         {
            outData[i] = 0;
         }

      if (outField != null)
      {
         outField.getData(0).setMaxv(maxSegNumber);
         outField.getData(0).setMinv(0);
      }

      if (gparams != null)
      {
         gparams.clearPoints();
      }

      dp.resetIsolines();
      dp.resetOverlays();
      prepareWorkObjects();
   }

   public void setBackgroundThresholdRange(float low, float up, boolean invert)
   {
      if (inField == null)
         return;
      float[] data = inField.getData(0).getFData();
      if (invert)
         for (int i = 0; i < data.length; i++)
         {
            if (data[i] <= low || data[i] >= up)
               outData[i] = BACKGROUND;
            else if (outData[i] == BACKGROUND)
               outData[i] = FREE;
         }
      else
         for (int i = 0; i < data.length; i++)
         {
            if (data[i] > low && data[i] < up)
               outData[i] = BACKGROUND;
            else if (outData[i] == BACKGROUND)
               outData[i] = FREE;
         }
      outField.getData(0).setMaxv(maxSegNumber);
      outField.getData(0).setMinv(0);

      if (dp.getParams().isSimpleOverlay())
         dp.getParams().setSimpleOverlay(false);
      dp.updateOrthosliceOverlays();
      dp.updateCustomOrthoPlanesOverlays();
   }

   public void setBackgroundThresholdRange(float low, float up)
   {
      setBackgroundThresholdRange(low, up, false);
   }

   public void setThreshold(int threshold)
   {
      if (inField == null || segmentVolume == null)
         return;
      float t = (threshold * tollerance) / 100.f;
      dp.getParams().setIsolineThreshold(t);
   }

   private void updateOutField(float threshold)
   {
      int[] coeff =
      {
         1, 2, 1, 2, 4, 2, 1, 2, 1,
         2, 4, 2, 4, 8, 4, 2, 4, 2,
         1, 2, 1, 2, 4, 2, 1, 2, 1
      };
      int ndata = dims[0] * dims[1] * dims[2];
      int m, l;
      byte[] tData = new byte[ndata];
      m = dims[0] * dims[1];
      l = dims[0];
      int[] offsets = new int[]
      {
         -m - l - 1, -m - l, -m - l + 1,
         -m - 1, -m, -m + 1,
         -m + l - 1, -m + l, -m + l + 1,
         -l - 1, -l, -l + 1,
         -1, 0, 1,
         l - 1, l, l + 1,
         m - l - 1, m - l, m - l + 1,
         m - 1, m, m + 1,
         m + l - 1, m + l, m + l + 1
      };
      //threshold
      for (int i = 0; i < ndata; i++)
      {
         if (distData[i] < threshold)
         {
            tData[i] = 1;
         } else
         {
            tData[i] = 0;
         }
         segData[i] = false;
      }

      //smoothing out contour
      for (int i2 = 1; i2 < dims[2] - 1; i2++)
      {
         for (int i1 = 1; i1 < dims[1] - 1; i1++)
         {
            for (int i0 = 1, i = (dims[1] * i2 + i1) * dims[0] + 1;
                    i0 < dims[0] - 1; i0++, i++)
            {
               l = 0;
               for (int j = 0; j < 27; j++)
               {
                  l += coeff[j] * tData[i + offsets[j]];
               }
               if (l > 32)
               {
                  segData[i] = true;
               }
            }
         }
      }
   }

   void cropField()
   {
      int[] low = volRender.getVolRenderUI().getCropUI().getLow();
      int[] up = volRender.getVolRenderUI().getCropUI().getUp();
      int[] down = new int[dims.length];
      int[] outDims = new int[dims.length];
      for (int i = 0; i < outDims.length; i++)
      {
         if (low[i] < 0 || up[i] > dims[i])
         {
            return;
         }
         outDims[i] = up[i] - low[i];
         down[i] = 1;
      }
      RegularField tmpField = new RegularField(outDims);
      if (inField.getCoords() != null)
      {
         tmpField.setCoords(cropDownArray(inField.getCoords(), inField.getNSpace(), dims, low, up, down));
      } else
      {
         float[][] outAffine = new float[4][3];
         float[][] affine = inField.getAffine();
         System.arraycopy(affine[3], 0, outAffine[3], 0, 3);
         for (int i = 0; i < outDims.length; i++)
         {
            for (int j = 0; j < 3; j++)
            {
               outAffine[3][j] += low[i] * affine[i][j];
               outAffine[i][j] = affine[i][j] * down[i];
            }
         }
         tmpField.setAffine(outAffine);
      }
      for (int i = 0; i < inField.getNData(); i++)
      {
         DataArray dta = inField.getData(i);
         switch (dta.getType())
         {
            case DataArray.FIELD_DATA_BYTE:
               tmpField.addData(DataArray.create(cropDownArray(dta.getBData(), dta.getVeclen(),
                       dims, low, up, down),
                       dta.getVeclen(), dta.getName()));
               break;
            case DataArray.FIELD_DATA_SHORT:
               tmpField.addData(DataArray.create(cropDownArray(dta.getSData(), dta.getVeclen(),
                       dims, low, up, down),
                       dta.getVeclen(), dta.getName()));
               break;
            case DataArray.FIELD_DATA_INT:
               tmpField.addData(DataArray.create(cropDownArray(dta.getIData(), dta.getVeclen(),
                       dims, low, up, down),
                       dta.getVeclen(), dta.getName()));
               break;
            case DataArray.FIELD_DATA_FLOAT:
               float[] fData = cropDownArray(dta.getFData(), dta.getVeclen(), dims, low, up, down);
               tmpField.addData(DataArray.create(cropDownArray(dta.getFData(), dta.getVeclen(),
                       dims, low, up, down),
                       dta.getVeclen(), dta.getName()));
               break;
            case DataArray.FIELD_DATA_DOUBLE:
               tmpField.addData(DataArray.create(cropDownArray(dta.getDData(), dta.getVeclen(),
                       dims, low, up, down),
                       dta.getVeclen(), dta.getName()));
               break;
         }
      }

      //inField = tmpField;
      //prepareWorkObjects();
      setInField(tmpField);
   }

   void computeSimilarityField(boolean[] allowed,
           ArrayList<Integer> indices, float[] weights,
           int t)
   {
      if (segmentVolume != null && segmentVolumeChangeListener != null)
      {
         segmentVolume.removeChangeListener(segmentVolumeChangeListener);
      }
      this.tollerance = t;
      segmentVolume = new SimilaritySegmentVolume(inField, distField, outField);
      segmentVolumeChangeListener = new ChangeListener()
      {
         @Override
         public void stateChanged(ChangeEvent evt)
         {
            if (segmentVolume.getStatus() == SimilaritySegmentVolume.SIMILARITY_COMPUTED) //dp.updateOrthosliceImages();
            {
               DataArray distDA = distField.getData(0);
               distDA.setMaxv(segmentVolume.getLmax());
               distDA.setMinv(0);
               dp.updateOrthosliceIsolines();
            }
         }
      };
      segmentVolume.addChangeListener(segmentVolumeChangeListener);
      segmentVolume.setIndices(indices);
      segmentVolume.setTollerance((short)tollerance);
      segmentVolume.setWeights(weights);
      segmentVolume.setRange((int)low, (int)up);
      segmentVolume.computeDistance(gparams.getPoints(), allowed);
   }

   void computeDistanceField(boolean[] allowed, int component, int t)
   {
      if (segmentVolume != null && segmentVolumeChangeListener != null)
      {
         segmentVolume.removeChangeListener(segmentVolumeChangeListener);
      }

      segmentVolume = new RangeSegmentVolume(inField, distField, outField);
      segmentVolumeChangeListener = new ChangeListener()
      {
         @Override
         public void stateChanged(ChangeEvent evt)
         {
            if (segmentVolume.getStatus() == SimilaritySegmentVolume.SIMILARITY_COMPUTED) //dp.updateOrthosliceImages();
            {
               DataArray distDA = distField.getData(0);
               tollerance = segmentVolume.getLmax();
               distDA.setMaxv(tollerance);
               distDA.setMinv(0);
               dp.updateOrthosliceIsolines();
            }
         }
      };
      segmentVolume.addChangeListener(segmentVolumeChangeListener);
      segmentVolume.setComponent(component);
      segmentVolume.setRange((int)low, (int)up);
      segmentVolume.computeDistance(gparams.getPoints(), allowed);
   }

   public void setSegIndex(int n)
   {
      segIndex = n + 1;
   }

   public void setMaxSegNumber(int maxSegNumber)
   {
      this.maxSegNumber = maxSegNumber;
   }

   void showDistFieldSlices(boolean showDist)
   {
      if (showDist && distField != null)
      {
         dp.setInField(distField);
      } else
      {
         dp.setInField(inField);
      }
   }

   void outputResultField()
   {
      outputReady = true;
      startAction();
   }

   public VolumeRenderer getVolRender()
   {
      return volRender;
   }

   public GeometryParams getGparams()
   {
      return gparams;
   }

   public DataProvider getDp()
   {
      return dp;
   }

   public VolumeSegmentationUI getSegUI()
   {
      return segUI;
   }

   @Override
   public void onInputDetach(LinkFace link)
   {
      onActive();
   }
}
