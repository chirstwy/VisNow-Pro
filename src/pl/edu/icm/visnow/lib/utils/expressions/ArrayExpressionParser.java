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
package pl.edu.icm.visnow.lib.utils.expressions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static pl.edu.icm.visnow.lib.utils.expressions.ExpressionOperators.*;

/**
 *
 * @author Bartosz Borucki (babor@icm.edu.pl), University of Warsaw, ICM based
 * on:
 * http://www.technical-recipes.com/2011/a-mathematical-expression-parser-in-java-and-cpp
 *
 */
public class ArrayExpressionParser {

    private boolean debug = false;
    public static final int PRECISION_SINGLE = 0;
    public static final int PRECISION_DOUBLE = 1;
    private int precision = PRECISION_SINGLE;
    private Map<String, Object> VARIABLES = new HashMap<String, Object>();
    private int dataSize = 1;

    public ArrayExpressionParser(int dataSize, Map<String, Object> variables) {
        this(dataSize, variables, false);
    }

    public ArrayExpressionParser(int dataSize, Map<String, Object> variables, boolean debug) {
        this.debug = debug;
        this.dataSize = dataSize;

        ArrayList<String> vars = new ArrayList<String>();
        vars.addAll(variables.keySet());
        if(!vars.isEmpty())
            if (variables.get(vars.get(0)) instanceof float[]) {
                precision = PRECISION_SINGLE;
                VARIABLES.put("PI", new float[]{(float) Math.PI});
                VARIABLES.put("E", new float[]{(float) Math.E});
            } else {
                precision = PRECISION_DOUBLE;
                VARIABLES.put("PI", new double[]{Math.PI});
                VARIABLES.put("E", new double[]{Math.E});
            }
        VARIABLES.putAll(variables);
    }

    public static String[] infixToRPN(String[] inputTokens) {
        ArrayList<String> out = new ArrayList<String>();
        Stack<String> stack = new Stack<String>();
        Operator op;
        for (String token : inputTokens) {
            if (isOperator(token)) {
                while (!stack.empty() && isOperator(stack.peek())) {
                    if ((isAssociative(token, LEFT_ASSOC)
                            && cmpPrecedence(token, stack.peek()) <= 0)
                            || (isAssociative(token, RIGHT_ASSOC)
                            && cmpPrecedence(token, stack.peek()) < 0)) {
                        out.add(stack.pop());
                        continue;
                    }
                    break;
                }
                stack.push(token);
            } else if (token.equals("(")) {
                stack.push(token);  //
            } else if (token.equals(")")) {
                while (!stack.empty() && !stack.peek().equals("(")) {
                    out.add(stack.pop());
                }
                stack.pop();
            } else {
                out.add(token);
            }
        }
        while (!stack.empty()) {
            out.add(stack.pop());
        }
        String[] output = new String[out.size()];
        return out.toArray(output);
    }

    public static boolean isNumeric(String token) {
        try {
            double d = Double.parseDouble(token);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public boolean isVariable(String token) {
        return VARIABLES.containsKey(token);
    }

    public double[] RPNtoDoubleArray(String[] tokens) throws Exception {
        if (precision != PRECISION_DOUBLE) {
            return null;
        }

        Stack<Object> stack = new Stack<Object>();

        for (String token : tokens) {
            if (!isOperator(token)) {
                stack.push(token);
            } else {
                Operator op = getOperator(token);
                double[][] args = new double[op.getNArgumetns()][];
                Object tmp;
                String var;
                for (int i = 0; i < args.length; i++) {
                    tmp = stack.pop();
                    if (tmp instanceof String && isVariable((String) tmp)) {
                        var = (String)tmp;
                        args[i] = getDoubleVariable(var, true);
                        if(args[i] == null)
                            throw new Exception("ERROR evaluating expression");
                    } else if (tmp instanceof double[]) {
                        args[i] = (double[]) tmp;
                    } else if (tmp instanceof String && isNumeric((String) tmp)) {
                        args[i] = new double[]{Double.valueOf((String) tmp)};
                    } else {
                        throw new Exception("ERROR evaluating expression");
                    }
                }
                double[] result = op.evaluateDouble(dataSize, args);                
                stack.push(result);
            }
        }

        Object obj = stack.pop();
        if(obj instanceof double[])
            return (double[]) obj;
        else if(obj instanceof String && isVariable((String) obj)) {
            double[] out = getDoubleVariable((String)obj, true);
            if(out != null)
                return out;
        }
        
        throw new Exception("ERROR evaluating expression");        
    }

    public float[] RPNtoFloatArray(String[] tokens) throws Exception {
        if (precision != PRECISION_SINGLE) {
            return null;
        }

        Stack<Object> stack = new Stack<Object>();

        //TODO introduce chunks
        
        for (String token : tokens) {
            if (!isOperator(token)) {
                stack.push(token);
            } else {
                Operator op = getOperator(token);
                float[][] args = new float[op.getNArgumetns()][];
                Object tmp;
                String var;
                for (int i = 0; i < args.length; i++) {
                    tmp = stack.pop();
                    if (tmp instanceof String && isVariable((String) tmp)) {
                        var = (String)tmp;
                        args[i] = getFloatVariable(var,true);                        
                        if(args[i] == null)
                            throw new Exception("ERROR evaluating expression");
                    } else if (tmp instanceof float[]) {
                        args[i] = (float[]) tmp;
                    } else if (tmp instanceof String && isNumeric((String) tmp)) {
                        args[i] = new float[]{Float.valueOf((String) tmp)};
                    } else {
                        throw new Exception("ERROR evaluating expression");
                    }
                }

                float[] result = op.evaluateFloat(dataSize, args);
                stack.push(result);
            }
        }

        Object obj = stack.pop();
        if(obj instanceof float[])
            return (float[]) obj;
        else if(obj instanceof String && isVariable((String) obj)) {
            float[] out = getFloatVariable((String)obj,true);
            if(out != null)
                return out;
        } else if(obj instanceof String && isNumeric((String) obj)) {
            float[] out = new float[dataSize];
            float v = Float.valueOf((String) obj);
            for (int i = 0; i < out.length; i++) {
                out[i] = v;                
            }
            return out;
        }
        
        //this is error code - you should not be here if everything is OK                
        if(obj instanceof String) {
            throw new Exception("ERROR evaluating expression - wrong variable: "+((String)obj));        
        }
        
        throw new Exception("ERROR evaluating expression");        
    }

    private double[] getDoubleVariable(String name, boolean remap) throws Exception {
        if(remap && name.contains(".")) {
            double[] data;
            if(VARIABLES.containsKey(name)) {
                data = (double[]) VARIABLES.get(name);
            } else {
                throw new Exception("ERROR: No such variable "+name);            
            }
            int veclen = data.length/dataSize;
            int v = interpretVindex(name);
            if(v == -1)
                return null; //this should never happen
            double[] out = new double[dataSize];
            for (int i = 0; i < out.length; i++) {
                out[i] = data[i*veclen + v];
            }                
            return out;
        } else {
            if(VARIABLES.containsKey(name))
                return (double[]) VARIABLES.get(name);
            else
                throw new Exception("ERROR: No such variable "+name);
        }
    }

    private float[] getFloatVariable(String name, boolean remap) throws Exception {
        if(remap && (name.contains(".") || name.equals("x") || name.equals("y") || name.equals("z")) && 
                !(name.equals("index.i") || name.equals("index.j") || name.equals("index.k"))) {
            float[] data;
            if(VARIABLES.containsKey(name)) {
                data = (float[]) VARIABLES.get(name);
            } else {
                throw new Exception("ERROR: No such variable "+name);            
            }
            int veclen = data.length/dataSize;
            if(veclen == 1)
                return data;
            int v = interpretVindex(name);
            if(v == -1)
                return null;  //this should never happen
            float[] out = new float[dataSize];
            for (int i = 0; i < out.length; i++) {
                out[i] = data[i*veclen + v];
            }                
            return out;
        } else {
            if(VARIABLES.containsKey(name))
                return (float[]) VARIABLES.get(name);
            else
                throw new Exception("ERROR: No such variable "+name);
        }
    }
    
    public double[] evaluateDoubleExpr(String expr) throws Exception {
        if (precision != PRECISION_DOUBLE) {
            return null;
        }

        String str = preprocessExpr(expr);
        if (debug) {
            System.out.println(str);
        }
        String[] input = str.split(" ");
        String[] output = infixToRPN(input);
        if (debug) {
            for (String token : output) {
                System.out.print(token + " ");
            }
            System.out.println("");
        }
        return RPNtoDoubleArray(output);
    }

    public float[] evaluateFloatExpr(String expr) throws Exception {
        if (precision != PRECISION_SINGLE) {
            return null;
        }

        String str = preprocessExpr(expr);
        if (debug) {
            System.out.println(str);
        }
        String[] input = str.split(" ");
        String[] output = infixToRPN(input);
        if (debug) {
            for (String token : output) {
                System.out.print(token + " ");
            }
            System.out.println("");
        }
        return RPNtoFloatArray(output);
    }

    public static String[] mysplit(String text, String pattern) {
        List<String> s = new ArrayList<String>();
        Matcher m = Pattern.compile(pattern).matcher(text);
        while (m.find()) {
            s.add(m.group());
        }
        return s.toArray(new String[s.size()]);
    }

    private static String fixUnaryMinus(String input, String pattern) {
        String str = new String(input);
        String tmp2;
        Matcher m = Pattern.compile(pattern).matcher(str);
        while (m.find()) {
            tmp2 = m.group();
            tmp2 = tmp2.replaceFirst(pattern, pattern.substring(0, pattern.length() - 1) + "~");
            str = str.substring(0, m.start()) + tmp2 + str.substring(m.end(), str.length());
        }
        return str;
    }

    private String preprocessExpr(String input) {
        String str = new String(input);
        str = str.replaceAll("\\s", "");
        //str = str.replaceAll(",", ".");
        if (debug) {
            System.out.println(str);
        }

        String strMask = "";
        for (int i = 0; i < str.length(); i++) {
            strMask += "0";
        }

        //case #1 - minus at the beginning
        if (str.startsWith("-")) {
            str = str.replaceFirst("-", "~");
        }

        //case #2 - minus after (
        str = str.replaceAll("\\(-", "\\(~");

        //case #3 - minus after operators
        str = fixUnaryMinus(str, "\\+-");
        str = fixUnaryMinus(str, "--");
        str = fixUnaryMinus(str, "\\*-");
        str = fixUnaryMinus(str, "/-");
        str = fixUnaryMinus(str, "\\^-");
        if (debug) {
            System.out.println(str);
        }

        ArrayList<String> ops = new ArrayList<String>();
        Operator[] opsVals = Operator.values();
        for (int i = 0; i < opsVals.length; i++) {
            ops.add(opsVals[i].toString());
        }
        Collections.sort(ops, new StringLengthComparator());

        ArrayList<String> vars = new ArrayList<String>();
        vars.addAll(VARIABLES.keySet());
        Collections.sort(vars, new StringLengthComparator());

        String tmp, tmpOp = "", tmpVar = "";
        int j;
        for (int i = 0; i < str.length(); i++) {
            tmp = str.substring(i);
            boolean op = false;
            for (j = 0; j < ops.size(); j++) {
                if (tmp.startsWith(ops.get(j))) {
                    op = true;
                    tmpOp = ops.get(j);
                    break;
                }
            }

            if (op) {
                str = str.substring(0, i) + " " + str.substring(i, str.length());
                strMask = strMask.substring(0, i) + "0";
                for (int k = 0; k < tmpOp.length(); k++) {
                    strMask += "1";
                }
                int tail = str.length() - strMask.length();
                for (int k = 0; k < tail; k++) {
                    strMask += "0";
                }
                i += ops.get(j).length();
            } else {
                boolean var = false;
                for (j = 0; j < vars.size(); j++) {
                    if (tmp.startsWith(vars.get(j))) {
                        var = true;
                        tmpVar = vars.get(j);
                        break;
                    }
                }

                if (var) {
                    str = str.substring(0, i) + " " + str.substring(i, str.length());
                    strMask = strMask.substring(0, i) + "0";
                    for (int k = 0; k < tmpVar.length(); k++) {
                        strMask += "1";
                    }
                    int tail = str.length() - strMask.length();
                    for (int k = 0; k < tail; k++) {
                        strMask += "0";
                    }
                    i += vars.get(j).length();
                }
            }
        }

        //str = str.replace("(", " (");
        Matcher m = Pattern.compile("\\(").matcher(str);
        int c = 0;
        while (m.find()) {
            str = str.substring(0, m.start() + c) + " " + str.substring(m.start() + c, str.length());
            strMask = strMask.substring(0, m.start() + c) + "0" + strMask.substring(m.start() + c, strMask.length());
            c++;
        }

        //str = str.replace(")", " )");
        m = Pattern.compile("\\)").matcher(str);
        c = 0;
        while (m.find()) {
            str = str.substring(0, m.start() + c) + " " + str.substring(m.start() + c, str.length());
            strMask = strMask.substring(0, m.start() + c) + "0" + strMask.substring(m.start() + c, strMask.length());
            c++;
        }

        //TODO: change regexp to support scientific notation of numbers e.g. 1e-10
        m = Pattern.compile("[\\.0-9]+").matcher(str);
        c = 0;
        while (m.find()) {
            if (strMask.charAt(m.start()) == '1') {
                continue;
            }

            str = str.substring(0, m.start() + c) + " " + str.substring(m.start() + c, str.length());
            c++;
        }

        str = str.trim();
        return str;
    }

//    public static void main(String[] args) {
//
//        Map<String, double[]> vars = new HashMap<String, double[]>();
//        vars.put("a.x", new double[]{ 1.0,  2.0,  3.0});
//        vars.put("a.y", new double[]{-1.0, -2.0, -3.0});
//        String str = "0.1234 + log10(a.x) + abs(a.y)";
//        System.out.println(str);
//
//        ArrayExpressionParser parser = new ArrayExpressionParser(3, vars, true);
//        double[] result = parser.evaluateExpr(str);
//
//        System.out.print("result = [");
//        for (int i = 0; i < result.length-1; i++) {
//            System.out.print(""+result[i]+", ");
//        }
//        System.out.println(""+result[result.length-1]+"]");
//
//    }
    public static void main(String[] args) {
        int N = 100;
        double[] a = new double[N];
        double[] b = new double[N];
        double[] c = new double[N];
        double[] res1 = new double[N];
        for (int i = 0; i < N; i++) {
            a[i] = Math.random();
            b[i] = Math.random();
            c[i] = Math.random();

            //res1[i] = Math.sqrt(a[i]*a[i] + b[i]*b[i])/c[i];
            res1[i] = -2 * Math.log(c[i]) - (a[i] - (Math.pow(b[i], -2)));
        }


        Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("a", a);
        vars.put("b", b);
        vars.put("c", c);
        //String expr = "sqrt(a^2 + b^2)/c";  
        String expr = "-2*log(c)-(a-(b^-2))";

        ArrayExpressionParser parser = new ArrayExpressionParser(N, vars, true);
        try {
            double[] res2 = parser.evaluateDoubleExpr(expr);
            boolean test = true;
            System.out.print("Test... ");
            for (int i = 0; i < N; i++) {
                if (res1[i] != res2[i]) {
                    test = false;
                    break;
                }
            }
            System.out.println(test ? "PASSED" : "FAILED");
        } catch(Exception ex) {
            ex.printStackTrace();
        }

    }

    private int interpretVindex(String var) throws Exception {
        if(var.contains(".")) {
            String subVarName = var.substring(var.indexOf(".")+1,var.length());
            if(subVarName.equals("x"))
                return 0;
            if(subVarName.equals("y"))
                return 1;
            if(subVarName.equals("z"))
                return 2;
        } else {
            if(var.equals("x"))
                return 0;
            if(var.equals("y"))
                return 1;
            if(var.equals("z"))
                return 2;            
        }        
        return -1;
    }
    
    private class StringLengthComparator implements java.util.Comparator<String> {

        public StringLengthComparator() {
            super();
        }

        @Override
        public int compare(String s1, String s2) {
            if (s1.length() == s2.length()) {
                return s1.compareTo(s2);
            }
            return s2.length() - s1.length();
        }
    }
    
    public static String[] listVariablesInUse(String[] vars, String[] expressions) {
        Map<String, Object> variables = new HashMap<String, Object>();
        for (int i = 0; i < vars.length; i++) {
            variables.put(vars[i], null);            
        }        
        ArrayExpressionParser parser = new ArrayExpressionParser(1, variables);
        
        ArrayList<String> out = new ArrayList<String>();
        for (int n = 0; n < expressions.length; n++) {
            String expr = expressions[n];
            String str = parser.preprocessExpr(expr);
            String[] input = str.split(" ");        
            Map<String, Object> tokens = new HashMap<String, Object>();
            for (int i = 0; i < input.length; i++) {
                tokens.put(input[i], null);            
            }
            
            for (int i = 0; i < vars.length; i++) {
                if(tokens.containsKey(vars[i]))
                    out.add(vars[i]);
            }
        }
        
        String[] tmp = new String[out.size()];
        for (int i = 0; i < tmp.length; i++) {
            tmp[i] = out.get(i);            
        }
        return tmp;
    }
    
    
}
