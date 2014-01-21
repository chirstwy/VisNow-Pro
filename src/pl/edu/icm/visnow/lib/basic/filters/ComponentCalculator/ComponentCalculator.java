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
package pl.edu.icm.visnow.lib.basic.filters.ComponentCalculator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import pl.edu.icm.visnow.datasets.Field;
import pl.edu.icm.visnow.datasets.IrregularField;
import pl.edu.icm.visnow.datasets.RegularField;
import pl.edu.icm.visnow.datasets.dataarrays.DataArray;
import pl.edu.icm.visnow.engine.core.InputEgg;
import pl.edu.icm.visnow.engine.core.LinkFace;
import pl.edu.icm.visnow.engine.core.ModuleCore;
import pl.edu.icm.visnow.engine.core.OutputEgg;
import pl.edu.icm.visnow.lib.types.VNField;
import pl.edu.icm.visnow.lib.types.VNIrregularField;
import pl.edu.icm.visnow.lib.types.VNRegularField;
import pl.edu.icm.visnow.lib.utils.SwingInstancer;
import pl.edu.icm.visnow.lib.utils.expressions.ArrayExpressionParser;

/**
 *
 ** @author Bartosz Borucki (babor@icm.edu.pl), University of Warsaw, ICM
 *
 */
public class ComponentCalculator extends ModuleCore
{

   private GUI ui = null;
   protected Params params;
   private Field inField = null;
   private boolean fromGUI = false;

   public ComponentCalculator()
   {
      parameters = params = new Params();
      SwingInstancer.swingRunAndWait(new Runnable()
      {
         @Override
         public void run()
         {
            ui = new GUI();
            ui.addChangeListener(new ChangeListener()
            {
               @Override
               public void stateChanged(ChangeEvent evt)
               {
                  fromGUI = true;
                  startAction();
               }
            });
            ui.setParams(params);
            setPanel(ui);
         }
      });
   }

   @Override
   public void onInitFinished()
   {
      coveringLayerPanel.overlay(false);
      hideGUIwhenNoData = false;
   }


   @Override
   public void onInputDetach(LinkFace link)
   {
      inField = null;
      ui.setInField(inField);
      setOutputValue("outRegularField", null);
      setOutputValue("outIrregularField", null);
   }
   
    @Override
    public void onActive() {
        if (!fromGUI) {
            if (getInputFirstValue("inField") == null || ((VNField) getInputFirstValue("inField")).getField() == null) {
                inField = null;
            } else {
                Field inFld = ((VNField) getInputFirstValue("inField")).getField();
                if (inFld != inField) {
                    inField = inFld;
                    String[] aliases = new String[inField.getNData()];
                    for (int i = 0; i < aliases.length; i++) {
                        if(inField.getData(i).getName().length() > 3)
                            aliases[i] = inField.getData(i).getName().substring(0, 3);
                        else
                            aliases[i] = inField.getData(i).getName();
                        boolean ok = true;
                        for (int j = 0; j < i; j++) {
                            if (aliases[j].equals(aliases[i])) {
                                ok = false;
                                break;
                            }
                        }

                        while (!ok) {
                            aliases[i] += "1";
                            ok = true;
                            for (int j = 0; j < i; j++) {
                                if (aliases[j].equals(aliases[i])) {
                                    ok = false;
                                    break;
                                }
                            }
                        }

                    }
                    params.setAliases(aliases);

                }
            }
            ui.setInField(inField);
        } else {
            try {
                Field outField = null;
                if (inField == null) {
                    outField = generateOutField();
                } else {
                    outField = calculateOutField();
                }

                if (outField != null && outField instanceof RegularField) {
                    setOutputValue("outRegularField", new VNRegularField((RegularField)outField));
                    setOutputValue("outIrregularField", null);
                } else if(outField != null && outField instanceof IrregularField) {
                    setOutputValue("outRegularField", null);
                    setOutputValue("outIrregularField", new VNIrregularField((IrregularField)outField));
                } else {
                    setOutputValue("outRegularField", null);
                    setOutputValue("outIrregularField", null);
                }
                
            } catch (Exception ex) {
                ui.setInfoText(ex.getMessage());
                //VisNow.get().userMessageSend(this, "Error in expression", ex.getMessage(), Level.ERROR);
                setOutputValue("outRegularField", null);
                setOutputValue("outIrregularField", null);
            }
        }
        fromGUI = false;
    }
    public static InputEgg[] inputEggs = null;
    public static OutputEgg[] outputEggs = null;

    private Field generateOutField() throws Exception {
        int[] dims = params.getDims();
        if (dims == null) {
            return null;
        }

        int dim = dims.length;
        float[][] extents = params.getExtents();
        if (extents == null) {
            return null;
        }

        RegularField out = new RegularField(dims);

        float[][] affine = new float[4][3];
        for (int i = 0; i < dim; i++) {
            affine[3][i] = extents[0][i];
            affine[i][i] = (extents[1][i] - extents[0][i]) / (dims[i] - 1);
        }
        out.setAffine(affine);
        int nNodes = out.getNNodes();

        String[] expressions = params.getExpressions();
        if (expressions == null) {
            return null;
        }
        
        String[] exprNames = new String[expressions.length];
        String[] exprExpr = new String[expressions.length];
        for (int i = 0; i < expressions.length; i++) {
            if (expressions[i].isEmpty() || !expressions[i].contains("=")) {
                exprNames[i] = null;
                exprExpr[i] = null;
                continue;
            }
            
            String[] tmp = expressions[i].split("=");
            exprNames[i] = tmp[0].trim();
            exprExpr[i] = tmp[1].trim();
            if (expressions[i].endsWith(";")) {
                expressions[i] = expressions[i].substring(0, expressions[i].length() - 1);
            }
        }
        

        String[] fieldVariables = listAllFieldVariables(out, null);
        String[] variablesInUse = ArrayExpressionParser.listVariablesInUse(fieldVariables, exprExpr);
        Map<String, Object> vars = prepareVariables(out, variablesInUse);

        for (int i = 0; i < expressions.length; i++) {
            if (exprExpr[i] == null || exprNames[i] == null) {
                continue;
            }

            String name = exprNames[i];
            String expr = exprExpr[i];

            if (out.getData(name) != null) {
                throw new Exception("ERROR: wrong result variable name!");
            }
            ArrayExpressionParser parser = new ArrayExpressionParser(nNodes, vars);
            if (params.getPrecision() == ArrayExpressionParser.PRECISION_SINGLE) {
                float[] result = parser.evaluateFloatExpr(expr);
                if (result != null) {
                    out.addData(DataArray.create(result, result.length/nNodes, name));
                    vars.put(name, result);
                }
            } else {
                double[] result = parser.evaluateDoubleExpr(expr);
                if (result != null) {
                    out.addData(DataArray.create(result, result.length/nNodes, name));
                    vars.put(name, result);
                }
            }
        }
        return out;
    }

    private Field calculateOutField() throws Exception {
        if (inField == null) {
            return null;
        }
        Field out = inField.clone();
        if (!params.isRetainComponents()) {
            out.removeAllData();
        }

        String[] expressions = params.getExpressions();
        if (expressions == null) {
            return null;
        }
        
        String[] exprNames = new String[expressions.length];
        String[] exprExpr = new String[expressions.length];
        for (int i = 0; i < expressions.length; i++) {
            if (expressions[i].isEmpty() || !expressions[i].contains("=")) {
                exprNames[i] = null;
                exprExpr[i] = null;
                continue;
            }
            
            String[] tmp = expressions[i].split("=");
            exprNames[i] = tmp[0].trim();
            exprExpr[i] = tmp[1].trim();
            if (expressions[i].endsWith(";")) {
                expressions[i] = expressions[i].substring(0, expressions[i].length() - 1);
            }
        }
        

        int nNodes = inField.getNNodes();
        String[] fieldVariables = listAllFieldVariables(inField, params.getAliases());
        String[] variablesInUse = ArrayExpressionParser.listVariablesInUse(fieldVariables, exprExpr);
        Map<String, Object> vars = prepareVariables(inField, variablesInUse);

        for (int i = 0; i < expressions.length; i++) {
            if (exprExpr[i] == null || exprNames[i] == null) {
                continue;
            }

            String name = exprNames[i];
            String expr = exprExpr[i];

            if (inField.getData(name) != null) {
                throw new Exception("ERROR: wrong result variable name!");
            }
            ArrayExpressionParser parser = new ArrayExpressionParser(nNodes, vars);
            if (params.getPrecision() == ArrayExpressionParser.PRECISION_SINGLE) {
                float[] result = parser.evaluateFloatExpr(expr);
                if (result != null) {
                    DataArray da = DataArray.create(result, result.length/nNodes, name); 
                    out.addData(da);
                    vars.put(name, result);
                    if(da.getVeclen() > 1) {
                        vars.put(name+".x", result);
                        vars.put(name+".y", result);
                        if(da.getVeclen() > 2)
                            vars.put(name+".z", result);
                    }
                }
            } else {
                double[] result = parser.evaluateDoubleExpr(expr);
                if (result != null) {
                    DataArray da = DataArray.create(result, result.length/nNodes, name);
                    out.addData(da);
                    vars.put(name, result);
                    if(da.getVeclen() > 1) {
                        vars.put(name+".x", result);
                        vars.put(name+".y", result);
                        if(da.getVeclen() > 2)
                            vars.put(name+".z", result);
                    }
                }
            }
        }

        return out;
    }

    private boolean isVariableInUse(String var, String[] variablesInUse) {
        for (int i = 0; i < variablesInUse.length; i++) {
            if (variablesInUse[i].equals(var)
                    || (variablesInUse[i].contains(".") && variablesInUse[i].subSequence(0, variablesInUse[i].indexOf(".")).equals(var))) {
                return true;
            }
        }
        return false;
    }

    private Map<String, Object> prepareVariables(Field field, String[] variablesInUse) {
        Map<String, Object> vars = new HashMap<String, Object>();
        String name, alias;
        int veclen;
        for (int i = 0; i < field.getNData(); i++) {
            name = field.getData(i).getName();
            alias = params.getAliases()[i];
            veclen = field.getData(i).getVeclen();
            if (isVariableInUse(name, variablesInUse) && !vars.containsKey(name)) {
                if (params.getPrecision() == ArrayExpressionParser.PRECISION_SINGLE) {
                    vars.put(name, field.getData(i).getFData());
                } else {
                    vars.put(name, field.getData(i).getDData());
                }
                if (veclen == 2) {
                    vars.put(name + ".x", vars.get(name));
                    vars.put(name + ".y", vars.get(name));
                } else if (veclen == 3) {
                    vars.put(name + ".x", vars.get(name));
                    vars.put(name + ".y", vars.get(name));
                    vars.put(name + ".z", vars.get(name));
                }
            }

            if (isVariableInUse(alias, variablesInUse) && !vars.containsKey(alias)) {
                if (params.getPrecision() == ArrayExpressionParser.PRECISION_SINGLE) {
                    vars.put(alias, field.getData(i).getFData());
                } else {
                    vars.put(alias, field.getData(i).getDData());
                }
                if (veclen == 2) {
                    vars.put(alias + ".x", vars.get(alias));
                    vars.put(alias + ".y", vars.get(alias));
                } else if (veclen == 3) {
                    vars.put(alias + ".x", vars.get(alias));
                    vars.put(alias + ".y", vars.get(alias));
                    vars.put(alias + ".z", vars.get(alias));
                }
            }
        }
        
        
        if (field instanceof RegularField) {
            
            //indices
            if (isVariableInUse("index.i", variablesInUse) && !vars.containsKey("index.i")) {
                if (params.getPrecision() == ArrayExpressionParser.PRECISION_SINGLE) {
                    if(vars.containsKey("i"))
                        vars.put("index.i", vars.get("i"));
                    else                        
                        vars.put("index.i", field.getFIndices(0));
                } else {
                    if(vars.containsKey("i"))
                        vars.put("index.i", vars.get("i"));
                    else                        
                        vars.put("index.i", field.getDIndices(0));
                }
            }
            if (isVariableInUse("i", variablesInUse) && !vars.containsKey("i")) {
                if (params.getPrecision() == ArrayExpressionParser.PRECISION_SINGLE) {
                    if(vars.containsKey("index.i"))
                        vars.put("i", vars.get("index.i"));
                    else                        
                        vars.put("i", field.getFIndices(0));
                } else {
                    if(vars.containsKey("index.i"))
                        vars.put("i", vars.get("index.i"));
                    else                        
                        vars.put("i", field.getDIndices(0));
                }
            }

            if (isVariableInUse("index.j", variablesInUse) && !vars.containsKey("index.j")) {
                if (params.getPrecision() == ArrayExpressionParser.PRECISION_SINGLE) {
                    if(vars.containsKey("j"))
                        vars.put("index.j", vars.get("j"));
                    else                        
                        vars.put("index.j", field.getFIndices(1));
                } else {
                    if(vars.containsKey("j"))
                        vars.put("index.j", vars.get("j"));
                    else                        
                        vars.put("index.j", field.getDIndices(1));
                }
            }

            if (isVariableInUse("j", variablesInUse) && !vars.containsKey("j")) {
                if (params.getPrecision() == ArrayExpressionParser.PRECISION_SINGLE) {
                    if(vars.containsKey("index.j"))
                        vars.put("j", vars.get("index.j"));
                    else                        
                        vars.put("j", field.getFIndices(1));
                } else {
                     if(vars.containsKey("index.j"))
                        vars.put("j", vars.get("index.j"));
                    else                        
                        vars.put("j", field.getDIndices(1));
                }
            }
            
            if (isVariableInUse("index.k", variablesInUse) && !vars.containsKey("index.k")) {
                if (params.getPrecision() == ArrayExpressionParser.PRECISION_SINGLE) {
                    if(vars.containsKey("k"))
                        vars.put("index.k", vars.get("k"));
                    else                        
                        vars.put("index.k", field.getFIndices(2));
                 } else {
                    if(vars.containsKey("k"))
                        vars.put("index.k", vars.get("k"));
                    else                        
                        vars.put("index.k", field.getDIndices(2));
                }
            }

            if (isVariableInUse("k", variablesInUse) && !vars.containsKey("k")) {
                if (params.getPrecision() == ArrayExpressionParser.PRECISION_SINGLE) {
                    if(vars.containsKey("index.k"))
                        vars.put("k", vars.get("index.k"));
                    else                        
                        vars.put("k", field.getFIndices(2));
                } else {
                    if(vars.containsKey("index.k"))
                        vars.put("k", vars.get("index.k"));
                    else                        
                        vars.put("k", field.getDIndices(2));
                }
            }
            
            
            //coords
            if ((isVariableInUse("coords.x", variablesInUse) || 
                 isVariableInUse("coords.y", variablesInUse) || 
                 isVariableInUse("coords.z", variablesInUse)) ||
                 isVariableInUse("x", variablesInUse) || 
                 isVariableInUse("y", variablesInUse) || 
                 isVariableInUse("z", variablesInUse) ) {
                if (params.getPrecision() == ArrayExpressionParser.PRECISION_SINGLE) {
                    if (field.getCoords() == null) {
                        float[][] affine = ((RegularField) field).getAffine();
                        int[] dims = ((RegularField) field).getDims();
                        if (isVariableInUse("coords.x", variablesInUse) && !vars.containsKey("coords.x")) {
                            if(vars.containsKey("x")) {
                                vars.put("coords.x", vars.get("x"));
                            } else {
                                float[] coordsx = new float[field.getNNodes()];
                                switch (dims.length) {
                                    case 1:
                                        for (int x = 0; x < dims[0]; x++) {
                                            coordsx[x] = affine[3][0] + x * affine[0][0];
                                        }
                                        break;
                                    case 2:
                                        for (int y = 0, l = 0; y < dims[1]; y++) {
                                            for (int x = 0; x < dims[0]; x++, l++) {
                                                coordsx[l] = affine[3][0] + x * affine[0][0] + y * affine[1][0];
                                            }
                                        }
                                        break;
                                    case 3:
                                        for (int z = 0, l = 0; z < dims[2]; z++) {
                                            for (int y = 0; y < dims[1]; y++) {
                                                for (int x = 0; x < dims[0]; x++, l++) {
                                                    coordsx[l] = affine[3][0] + x * affine[0][0] + y * affine[1][0] + z * affine[2][0];
                                                }
                                            }
                                        }
                                        break;
                                }
                                vars.put("coords.x", coordsx);
                            }
                        }

                        if (isVariableInUse("x", variablesInUse) && !vars.containsKey("x")) {
                            if(vars.containsKey("coords.x")) {
                                vars.put("x", vars.get("coords.x"));
                            } else {
                                float[] coordsx = new float[field.getNNodes()];
                                switch (dims.length) {
                                    case 1:
                                        for (int x = 0; x < dims[0]; x++) {
                                            coordsx[x] = affine[3][0] + x * affine[0][0];
                                        }
                                        break;
                                    case 2:
                                        for (int y = 0, l = 0; y < dims[1]; y++) {
                                            for (int x = 0; x < dims[0]; x++, l++) {
                                                coordsx[l] = affine[3][0] + x * affine[0][0] + y * affine[1][0];
                                            }
                                        }
                                        break;
                                    case 3:
                                        for (int z = 0, l = 0; z < dims[2]; z++) {
                                            for (int y = 0; y < dims[1]; y++) {
                                                for (int x = 0; x < dims[0]; x++, l++) {
                                                    coordsx[l] = affine[3][0] + x * affine[0][0] + y * affine[1][0] + z * affine[2][0];
                                                }
                                            }
                                        }
                                        break;
                                }
                                vars.put("x", coordsx);
                            }
                        }
                        
                        
                        if (field.getNSpace() > 1 && isVariableInUse("coords.y", variablesInUse) && !vars.containsKey("coords.y")) {
                            if(vars.containsKey("y")) {
                                vars.put("coords.y", vars.get("y"));
                            } else {
                                float[] coordsy = new float[field.getNNodes()];
                                switch (dims.length) {
                                    case 1:
                                        for (int x = 0; x < dims[0]; x++) {
                                            coordsy[x] = affine[3][1] + x * affine[0][1];
                                        }
                                        break;
                                    case 2:
                                        for (int y = 0, l = 0; y < dims[1]; y++) {
                                            for (int x = 0; x < dims[0]; x++, l++) {
                                                coordsy[l] = affine[3][1] + x * affine[0][1] + y * affine[1][1];
                                            }
                                        }
                                        break;
                                    case 3:
                                        for (int z = 0, l = 0; z < dims[2]; z++) {
                                            for (int y = 0; y < dims[1]; y++) {
                                                for (int x = 0; x < dims[0]; x++, l++) {
                                                    coordsy[l] = affine[3][1] + x * affine[0][1] + y * affine[1][1] + z * affine[2][1];
                                                }
                                            }
                                        }
                                        break;
                                }
                                vars.put("coords.y", coordsy);
                            }
                        }

                        if (field.getNSpace() > 1 && isVariableInUse("y", variablesInUse) && !vars.containsKey("y")) {
                            if(vars.containsKey("coords.y")) {
                                vars.put("y", vars.get("coords.y"));
                            } else {
                                float[] coordsy = new float[field.getNNodes()];
                                switch (dims.length) {
                                    case 1:
                                        for (int x = 0; x < dims[0]; x++) {
                                            coordsy[x] = affine[3][1] + x * affine[0][1];
                                        }
                                        break;
                                    case 2:
                                        for (int y = 0, l = 0; y < dims[1]; y++) {
                                            for (int x = 0; x < dims[0]; x++, l++) {
                                                coordsy[l] = affine[3][1] + x * affine[0][1] + y * affine[1][1];
                                            }
                                        }
                                        break;
                                    case 3:
                                        for (int z = 0, l = 0; z < dims[2]; z++) {
                                            for (int y = 0; y < dims[1]; y++) {
                                                for (int x = 0; x < dims[0]; x++, l++) {
                                                    coordsy[l] = affine[3][1] + x * affine[0][1] + y * affine[1][1] + z * affine[2][1];
                                                }
                                            }
                                        }
                                        break;
                                }
                                vars.put("y", coordsy);
                            }
                        }
                        
                        
                        if (field.getNSpace() > 2 && isVariableInUse("coords.z", variablesInUse) && !vars.containsKey("coords.z")) {
                            if(vars.containsKey("z")) {
                                vars.put("coords.z", vars.get("z"));
                            } else {
                                float[] coordsz = new float[field.getNNodes()];
                                switch (dims.length) {
                                    case 1:
                                        for (int x = 0; x < dims[0]; x++) {
                                            coordsz[x] = affine[3][2] + x * affine[0][2];
                                        }
                                        break;
                                    case 2:
                                        for (int y = 0, l = 0; y < dims[1]; y++) {
                                            for (int x = 0; x < dims[0]; x++, l++) {
                                                coordsz[l] = affine[3][2] + x * affine[0][2] + y * affine[1][2];
                                            }
                                        }
                                        break;
                                    case 3:
                                        for (int z = 0, l = 0; z < dims[2]; z++) {
                                            for (int y = 0; y < dims[1]; y++) {
                                                for (int x = 0; x < dims[0]; x++, l++) {
                                                    coordsz[l] = affine[3][2] + x * affine[0][2] + y * affine[1][2] + z * affine[2][2];
                                                }
                                            }
                                        }
                                        break;
                                }
                                vars.put("coords.z", coordsz);
                            }
                        }
                        
                        if (field.getNSpace() > 2 && isVariableInUse("z", variablesInUse) && !vars.containsKey("z")) {
                            if(vars.containsKey("coords.z")) {
                                vars.put("z", vars.get("coords.z"));
                            } else {
                                float[] coordsz = new float[field.getNNodes()];
                                switch (dims.length) {
                                    case 1:
                                        for (int x = 0; x < dims[0]; x++) {
                                            coordsz[x] = affine[3][2] + x * affine[0][2];
                                        }
                                        break;
                                    case 2:
                                        for (int y = 0, l = 0; y < dims[1]; y++) {
                                            for (int x = 0; x < dims[0]; x++, l++) {
                                                coordsz[l] = affine[3][2] + x * affine[0][2] + y * affine[1][2];
                                            }
                                        }
                                        break;
                                    case 3:
                                        for (int z = 0, l = 0; z < dims[2]; z++) {
                                            for (int y = 0; y < dims[1]; y++) {
                                                for (int x = 0; x < dims[0]; x++, l++) {
                                                    coordsz[l] = affine[3][2] + x * affine[0][2] + y * affine[1][2] + z * affine[2][2];
                                                }
                                            }
                                        }
                                        break;
                                }
                                vars.put("z", coordsz);
                            }
                        }
                        
                        
                    } else {
                        
                        if (isVariableInUse("coords.x", variablesInUse) && !vars.containsKey("coords.x"))
                            vars.put("coords.x", field.getCoords());
                        if (isVariableInUse("x", variablesInUse) && !vars.containsKey("x"))
                            vars.put("x", field.getCoords());
                        if (field.getNSpace() > 1 && isVariableInUse("coords.y", variablesInUse) && !vars.containsKey("coords.y"))
                            vars.put("coords.y", field.getCoords());
                        if (field.getNSpace() > 1 && isVariableInUse("y", variablesInUse) && !vars.containsKey("y"))
                            vars.put("y", field.getCoords());
                        if (field.getNSpace() > 2 && isVariableInUse("coords.z", variablesInUse) && !vars.containsKey("coords.z"))
                            vars.put("coords.z", field.getCoords());
                        if (field.getNSpace() > 2 && isVariableInUse("z", variablesInUse) && !vars.containsKey("z"))
                            vars.put("z", field.getCoords());
                    }
                    
                } else { //RF coords DP
                    
                    if (field.getCoords() == null) {
                        float[][] affine = ((RegularField) field).getAffine();
                        int[] dims = ((RegularField) field).getDims();
                        if (isVariableInUse("coords.x", variablesInUse) && !vars.containsKey("coords.x")) {
                            if(vars.containsKey("x")) {
                                vars.put("coords.x", vars.get("x"));
                            } else {
                                double[] coordsx = new double[field.getNNodes()];
                                switch (dims.length) {
                                    case 1:
                                        for (int x = 0; x < dims[0]; x++) {
                                            coordsx[x] = affine[3][0] + x * affine[0][0];
                                        }
                                        break;
                                    case 2:
                                        for (int y = 0, l = 0; y < dims[1]; y++) {
                                            for (int x = 0; x < dims[0]; x++, l++) {
                                                coordsx[l] = affine[3][0] + x * affine[0][0] + y * affine[1][0];
                                            }
                                        }
                                        break;
                                    case 3:
                                        for (int z = 0, l = 0; z < dims[2]; z++) {
                                            for (int y = 0; y < dims[1]; y++) {
                                                for (int x = 0; x < dims[0]; x++, l++) {
                                                    coordsx[l] = affine[3][0] + x * affine[0][0] + y * affine[1][0] + z * affine[2][0];
                                                }
                                            }
                                        }
                                        break;
                                }
                                vars.put("coords.x", coordsx);
                            }
                        }

                        if (isVariableInUse("x", variablesInUse) && !vars.containsKey("x")) {
                            if(vars.containsKey("coords.x")) {
                                vars.put("x", vars.get("coords.x"));
                            } else {
                                double[] coordsx = new double[field.getNNodes()];
                                switch (dims.length) {
                                    case 1:
                                        for (int x = 0; x < dims[0]; x++) {
                                            coordsx[x] = affine[3][0] + x * affine[0][0];
                                        }
                                        break;
                                    case 2:
                                        for (int y = 0, l = 0; y < dims[1]; y++) {
                                            for (int x = 0; x < dims[0]; x++, l++) {
                                                coordsx[l] = affine[3][0] + x * affine[0][0] + y * affine[1][0];
                                            }
                                        }
                                        break;
                                    case 3:
                                        for (int z = 0, l = 0; z < dims[2]; z++) {
                                            for (int y = 0; y < dims[1]; y++) {
                                                for (int x = 0; x < dims[0]; x++, l++) {
                                                    coordsx[l] = affine[3][0] + x * affine[0][0] + y * affine[1][0] + z * affine[2][0];
                                                }
                                            }
                                        }
                                        break;
                                }
                                vars.put("x", coordsx);
                            }
                        }
                        
                        
                        if (field.getNSpace() > 1 && isVariableInUse("coords.y", variablesInUse) && !vars.containsKey("coords.y")) {
                            if(vars.containsKey("y")) {
                                vars.put("coords.y", vars.get("y"));
                            } else {
                                double[] coordsy = new double[field.getNNodes()];
                                switch (dims.length) {
                                    case 1:
                                        for (int x = 0; x < dims[0]; x++) {
                                            coordsy[x] = affine[3][1] + x * affine[0][1];
                                        }
                                        break;
                                    case 2:
                                        for (int y = 0, l = 0; y < dims[1]; y++) {
                                            for (int x = 0; x < dims[0]; x++, l++) {
                                                coordsy[l] = affine[3][1] + x * affine[0][1] + y * affine[1][1];
                                            }
                                        }
                                        break;
                                    case 3:
                                        for (int z = 0, l = 0; z < dims[2]; z++) {
                                            for (int y = 0; y < dims[1]; y++) {
                                                for (int x = 0; x < dims[0]; x++, l++) {
                                                    coordsy[l] = affine[3][1] + x * affine[0][1] + y * affine[1][1] + z * affine[2][1];
                                                }
                                            }
                                        }
                                        break;
                                }
                                vars.put("coords.y", coordsy);
                            }
                        }

                        if (field.getNSpace() > 1 && isVariableInUse("y", variablesInUse) && !vars.containsKey("y")) {
                            if(vars.containsKey("coords.y")) {
                                vars.put("y", vars.get("coords.y"));
                            } else {
                                double[] coordsy = new double[field.getNNodes()];
                                switch (dims.length) {
                                    case 1:
                                        for (int x = 0; x < dims[0]; x++) {
                                            coordsy[x] = affine[3][1] + x * affine[0][1];
                                        }
                                        break;
                                    case 2:
                                        for (int y = 0, l = 0; y < dims[1]; y++) {
                                            for (int x = 0; x < dims[0]; x++, l++) {
                                                coordsy[l] = affine[3][1] + x * affine[0][1] + y * affine[1][1];
                                            }
                                        }
                                        break;
                                    case 3:
                                        for (int z = 0, l = 0; z < dims[2]; z++) {
                                            for (int y = 0; y < dims[1]; y++) {
                                                for (int x = 0; x < dims[0]; x++, l++) {
                                                    coordsy[l] = affine[3][1] + x * affine[0][1] + y * affine[1][1] + z * affine[2][1];
                                                }
                                            }
                                        }
                                        break;
                                }
                                vars.put("y", coordsy);
                            }
                        }
                        
                        
                        if (field.getNSpace() > 2 && isVariableInUse("coords.z", variablesInUse) && !vars.containsKey("coords.z")) {
                            if(vars.containsKey("z")) {
                                vars.put("coords.z", vars.get("z"));
                            } else {
                                double[] coordsz = new double[field.getNNodes()];
                                switch (dims.length) {
                                    case 1:
                                        for (int x = 0; x < dims[0]; x++) {
                                            coordsz[x] = affine[3][2] + x * affine[0][2];
                                        }
                                        break;
                                    case 2:
                                        for (int y = 0, l = 0; y < dims[1]; y++) {
                                            for (int x = 0; x < dims[0]; x++, l++) {
                                                coordsz[l] = affine[3][2] + x * affine[0][2] + y * affine[1][2];
                                            }
                                        }
                                        break;
                                    case 3:
                                        for (int z = 0, l = 0; z < dims[2]; z++) {
                                            for (int y = 0; y < dims[1]; y++) {
                                                for (int x = 0; x < dims[0]; x++, l++) {
                                                    coordsz[l] = affine[3][2] + x * affine[0][2] + y * affine[1][2] + z * affine[2][2];
                                                }
                                            }
                                        }
                                        break;
                                }
                                vars.put("coords.z", coordsz);
                            }
                        }
                        
                        if (field.getNSpace() > 2 && isVariableInUse("z", variablesInUse) && !vars.containsKey("z")) {
                            if(vars.containsKey("coords.z")) {
                                vars.put("z", vars.get("coords.z"));
                            } else {
                                double[] coordsz = new double[field.getNNodes()];
                                switch (dims.length) {
                                    case 1:
                                        for (int x = 0; x < dims[0]; x++) {
                                            coordsz[x] = affine[3][2] + x * affine[0][2];
                                        }
                                        break;
                                    case 2:
                                        for (int y = 0, l = 0; y < dims[1]; y++) {
                                            for (int x = 0; x < dims[0]; x++, l++) {
                                                coordsz[l] = affine[3][2] + x * affine[0][2] + y * affine[1][2];
                                            }
                                        }
                                        break;
                                    case 3:
                                        for (int z = 0, l = 0; z < dims[2]; z++) {
                                            for (int y = 0; y < dims[1]; y++) {
                                                for (int x = 0; x < dims[0]; x++, l++) {
                                                    coordsz[l] = affine[3][2] + x * affine[0][2] + y * affine[1][2] + z * affine[2][2];
                                                }
                                            }
                                        }
                                        break;
                                }
                                vars.put("z", coordsz);
                            }
                        }
                        
                        
                    } else {
                        
                        if (isVariableInUse("coords.x", variablesInUse) && !vars.containsKey("coords.x")) {
                            if(vars.containsKey("x"))
                                vars.put("coords.x", vars.get("x"));
                            else if(vars.containsKey("y"))
                                vars.put("coords.x", vars.get("y"));
                            else if(vars.containsKey("z"))
                                vars.put("coords.x", vars.get("z"));
                            else if(vars.containsKey("coords.y"))
                                vars.put("coords.x", vars.get("coords.y"));
                            else if(vars.containsKey("coords.z"))
                                vars.put("coords.x", vars.get("coords.z"));
                            else
                                vars.put("coords.x", field.getCoordsDP());
                        }
                        
                        if (isVariableInUse("x", variablesInUse) && !vars.containsKey("x")) {
                            if(vars.containsKey("coords.x"))
                                vars.put("x", vars.get("coords.x"));
                            else if(vars.containsKey("y"))
                                vars.put("x", vars.get("y"));
                            else if(vars.containsKey("z"))
                                vars.put("x", vars.get("z"));
                            else if(vars.containsKey("coords.y"))
                                vars.put("x", vars.get("coords.y"));
                            else if(vars.containsKey("coords.z"))
                                vars.put("x", vars.get("coords.z"));
                            else
                                vars.put("x", field.getCoordsDP());
                        }

                        
                        if (field.getNSpace() > 1 && isVariableInUse("coords.y", variablesInUse) && !vars.containsKey("coords.y")) {
                            if(vars.containsKey("x"))
                                vars.put("coords.y", vars.get("x"));
                            else if(vars.containsKey("y"))
                                vars.put("coords.y", vars.get("y"));
                            else if(vars.containsKey("z"))
                                vars.put("coords.y", vars.get("z"));
                            else if(vars.containsKey("coords.x"))
                                vars.put("coords.y", vars.get("coords.x"));
                            else if(vars.containsKey("coords.z"))
                                vars.put("coords.y", vars.get("coords.z"));
                            else
                                vars.put("coords.y", field.getCoordsDP());
                        }
                        
                        if (isVariableInUse("y", variablesInUse) && !vars.containsKey("y")) {
                            if(vars.containsKey("coords.y"))
                                vars.put("y", vars.get("coords.y"));
                            else if(vars.containsKey("x"))
                                vars.put("y", vars.get("x"));
                            else if(vars.containsKey("z"))
                                vars.put("y", vars.get("z"));
                            else if(vars.containsKey("coords.x"))
                                vars.put("y", vars.get("coords.x"));
                            else if(vars.containsKey("coords.z"))
                                vars.put("y", vars.get("coords.z"));
                            else
                                vars.put("y", field.getCoordsDP());
                        }

                        if (field.getNSpace() > 2 && isVariableInUse("coords.z", variablesInUse) && !vars.containsKey("coords.z")) {
                            if(vars.containsKey("x"))
                                vars.put("coords.z", vars.get("x"));
                            else if(vars.containsKey("y"))
                                vars.put("coords.z", vars.get("y"));
                            else if(vars.containsKey("z"))
                                vars.put("coords.z", vars.get("z"));
                            else if(vars.containsKey("coords.x"))
                                vars.put("coords.z", vars.get("coords.x"));
                            else if(vars.containsKey("coords.y"))
                                vars.put("coords.z", vars.get("coords.y"));
                            else
                                vars.put("coords.z", field.getCoordsDP());
                        }
                        
                        if (field.getNSpace() > 2 && isVariableInUse("z", variablesInUse) && !vars.containsKey("z")) {
                            if(vars.containsKey("coords.z"))
                                vars.put("z", vars.get("coords.z"));
                            else if(vars.containsKey("x"))
                                vars.put("z", vars.get("x"));
                            else if(vars.containsKey("y"))
                                vars.put("z", vars.get("y"));
                            else if(vars.containsKey("coords.x"))
                                vars.put("z", vars.get("coords.x"));
                            else if(vars.containsKey("coords.y"))
                                vars.put("z", vars.get("coords.y"));
                            else
                                vars.put("z", field.getCoordsDP());
                        }
                        
                        
                    }
                }
            }
            
        } //end regular field

        if (field instanceof IrregularField) {
            if (isVariableInUse("index", variablesInUse) && !vars.containsKey("index")) {
                if (params.getPrecision() == ArrayExpressionParser.PRECISION_SINGLE) {
                    if(vars.containsKey("i"))
                        vars.put("index", vars.get("i"));
                    else                        
                        vars.put("index", field.getFIndices(0));
                } else {
                    if(vars.containsKey("i"))
                        vars.put("index", vars.get("i"));
                    else                        
                        vars.put("index", field.getDIndices(0));
                }
            }

            if (isVariableInUse("i", variablesInUse) && !vars.containsKey("i")) {
                if (params.getPrecision() == ArrayExpressionParser.PRECISION_SINGLE) {
                    if(vars.containsKey("index"))
                        vars.put("i", vars.get("index"));
                    else                        
                        vars.put("i", field.getFIndices(0));
                } else {
                    if(vars.containsKey("index"))
                        vars.put("i", vars.get("index"));
                    else                        
                        vars.put("i", field.getDIndices(0));
                }
            }
            
            //coords
            if (isVariableInUse("coords.x", variablesInUse) && !vars.containsKey("coords.x")) {
                if (params.getPrecision() == ArrayExpressionParser.PRECISION_SINGLE) {
                    vars.put("coords.x", field.getCoords());
                } else {
                    if(vars.containsKey("coords.y")) {
                        vars.put("coords.x", vars.get("coords.y"));
                    } else if(vars.containsKey("coords.z")) {
                        vars.put("coords.x", vars.get("coords.z"));
                    } else if(vars.containsKey("x")) {
                        vars.put("coords.x", vars.get("x"));
                    } else if(vars.containsKey("y")) {
                        vars.put("coords.x", vars.get("y"));
                    } else if(vars.containsKey("z")) {
                        vars.put("coords.x", vars.get("z"));
                    } else {
                        vars.put("coords.x", field.getCoordsDP());
                    }
                }
            }

            if (isVariableInUse("x", variablesInUse) && !vars.containsKey("x")) {
                if (params.getPrecision() == ArrayExpressionParser.PRECISION_SINGLE) {
                    vars.put("x", field.getCoords());
                } else {
                    if(vars.containsKey("coords.x")) {
                        vars.put("x", vars.get("coords.x"));
                    } else if(vars.containsKey("coords.y")) {
                        vars.put("x", vars.get("coords.y"));
                    } else if(vars.containsKey("coords.z")) {
                        vars.put("x", vars.get("coords.z"));
                    } else if(vars.containsKey("y")) {
                        vars.put("x", vars.get("y"));
                    } else if(vars.containsKey("z")) {
                        vars.put("x", vars.get("z"));
                    } else {
                        vars.put("x", field.getCoordsDP());
                    }
                }
            }

            if (isVariableInUse("coords.y", variablesInUse) && !vars.containsKey("coords.y")) {
                if (params.getPrecision() == ArrayExpressionParser.PRECISION_SINGLE) {
                    vars.put("coords.y", field.getCoords());
                } else {
                    if(vars.containsKey("coords.x")) {
                        vars.put("coords.y", vars.get("coords.x"));
                    } else if(vars.containsKey("coords.z")) {
                        vars.put("coords.y", vars.get("coords.z"));
                    } else if(vars.containsKey("x")) {
                        vars.put("coords.y", vars.get("x"));
                    } else if(vars.containsKey("y")) {
                        vars.put("coords.y", vars.get("y"));
                    } else if(vars.containsKey("z")) {
                        vars.put("coords.y", vars.get("z"));
                    } else {
                        vars.put("coords.y", field.getCoordsDP());
                    }
                }
            }

            if (isVariableInUse("y", variablesInUse) && !vars.containsKey("y")) {
                if (params.getPrecision() == ArrayExpressionParser.PRECISION_SINGLE) {
                    vars.put("y", field.getCoords());
                } else {
                    if(vars.containsKey("coords.x")) {
                        vars.put("y", vars.get("coords.x"));
                    } else if(vars.containsKey("coords.y")) {
                        vars.put("y", vars.get("coords.y"));
                    } else if(vars.containsKey("coords.z")) {
                        vars.put("y", vars.get("coords.z"));
                    } else if(vars.containsKey("x")) {
                        vars.put("y", vars.get("x"));
                    } else if(vars.containsKey("z")) {
                        vars.put("y", vars.get("z"));
                    } else {
                        vars.put("y", field.getCoordsDP());
                    }
                }
            }
            
            if (isVariableInUse("coords.z", variablesInUse) && !vars.containsKey("coords.z")) {
                if (params.getPrecision() == ArrayExpressionParser.PRECISION_SINGLE) {
                    vars.put("coords.z", field.getCoords());
                } else {
                    if(vars.containsKey("coords.x")) {
                        vars.put("coords.z", vars.get("coords.x"));
                    } else if(vars.containsKey("coords.y")) {
                        vars.put("coords.z", vars.get("coords.y"));
                    } else if(vars.containsKey("x")) {
                        vars.put("coords.z", vars.get("x"));
                    } else if(vars.containsKey("y")) {
                        vars.put("coords.z", vars.get("y"));
                    } else if(vars.containsKey("z")) {
                        vars.put("coords.z", vars.get("z"));
                    } else {
                        vars.put("coords.z", field.getCoordsDP());
                    }
                }
            }

            if (isVariableInUse("z", variablesInUse) && !vars.containsKey("z")) {
                if (params.getPrecision() == ArrayExpressionParser.PRECISION_SINGLE) {
                    vars.put("z", field.getCoords());
                } else {
                    if(vars.containsKey("coords.x")) {
                        vars.put("z", vars.get("coords.x"));
                    } else if(vars.containsKey("coords.y")) {
                        vars.put("z", vars.get("coords.y"));
                    } else if(vars.containsKey("coords.z")) {
                        vars.put("z", vars.get("coords.z"));
                    } else if(vars.containsKey("x")) {
                        vars.put("z", vars.get("x"));
                    } else if(vars.containsKey("y")) {
                        vars.put("z", vars.get("y"));
                    } else {
                        vars.put("z", field.getCoordsDP());
                    }
                }
            }
            
            
        } //end irregular field

        return vars;
    }

    private static String[] listAllFieldVariables(Field field, String[] aliases) {
        if(field == null)
            return null;
        
        ArrayList<String> list = new ArrayList<String>();
        if(field instanceof RegularField) {
            switch(((RegularField)field).getDims().length) {
                case 3:
                    list.add("index.k");
                    list.add("k");
                case 2:
                    list.add("index.j");
                    list.add("j");
                case 1:
                    list.add("index.i");
                    list.add("i");
            }            
        } else if(field instanceof IrregularField) {
            list.add("index");
            list.add("i");
        }
        
        switch(field.getNSpace()) {
            case 3:
                list.add("coords.z");
                list.add("z");
            case 2:
                list.add("coords.y");
                list.add("y");
            case 1:
                list.add("coords.x");
                list.add("x");
        }
        
        for (int i = 0; i < field.getNData(); i++) {
            String name = field.getData(i).getName();
            int veclen = field.getData(i).getVeclen();
            String alias = aliases[i];
            list.add(name);
            list.add(alias);
            switch(veclen) {
                case 3:
                    list.add(name+".z");
                    list.add(alias+".z");
                case 2:
                    list.add(name+".y");
                    list.add(alias+".y");
                    list.add(name+".x");
                    list.add(alias+".x");
            }            
        }
        
        String[] out = new String[list.size()];
        for (int i = 0; i < out.length; i++) {
            out[i] = list.get(i);            
        }
        return out;
    }
}
