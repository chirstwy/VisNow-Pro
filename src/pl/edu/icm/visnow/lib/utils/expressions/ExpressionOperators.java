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

package pl.edu.icm.visnow.lib.utils.expressions;

/**
 *
 * @author Bartosz Borucki (babor@icm.edu.pl), University of Warsaw, ICM
 *
 */
public class ExpressionOperators {

    public static final int LEFT_ASSOC = 0;
    public static final int RIGHT_ASSOC = 1;

    public static enum Operator {
        //<editor-fold defaultstate="collapsed" desc=" FUNCTION template ">
        /*
         * How to implement new function
         * 
         FUNCTION {
            @Override
            public String toString() {
                return "function";  //returns function name used in expression
            }

            @Override
            public int getPrecedence() {
                return 1; //returns function precedence, return 15 for functions or omit this Override
            }

            @Override
            public int getNArgumetns() {
                return 2; //returns number of arguments of, now only 1 is supported for functions, and 1 or 2 for operators, omit this Override to use 1
            }

            @Override
            public float[] evaluateFloat(int LEN, float[][] args) throws Exception {
               //returns single precision result array of size LEN,
               //first input argument is data size
               //second input argument is vector index to use (calculate veclen by length/LEN)
               //input arguments is N x float[] where N = getNArguments
               //use indexing of args[] arrays checking if length==1.
               //trow operatorException when evaluation is not possible

               float[] result = new float[LEN];
               int dj = args[1].length == 1 ? 0 : 1;
               int dk = args[0].length == 1 ? 0 : 1;
               for (int i = 0, j = 0, k = 0; i < LEN; i++, j += dj, k += dk) {
                  result[i] = args[1][j] + args[0][k];
               }
               return result;
            }

            @Override
            public double[] evaluateDouble(int LEN, double[][] args) {
               //returns double precision result array of size LEN,
               //input arguments is N x double[] where N = getNArguments
               //use indexing of args[] arrays checking if length==1.

               double[] result = new double[LEN];
               int dj = args[1].length == 1 ? 0 : 1;
               int dk = args[0].length == 1 ? 0 : 1;
               for (int i = 0, j = 0, k = 0; i < LEN; i++, j += dj, k += dk) {
                   result[i] = args[1][j] + args[0][k];
               }
               return result;
            }
         },
         */
        //</editor-fold>
        VECTOR {
            //<editor-fold defaultstate="collapsed" desc=" VECTOR ">            
            @Override
            public String toString() {
                return ",";
            }

            @Override
            public int getPrecedence() {
                return 1;
            }

            @Override
            public int getNArgumetns() {
                return 2;
            }

            @Override
            public float[] evaluateFloat(int LEN, float[][] args) throws Exception {
                float[] result;
                int veclen1 = args[1].length / LEN;
                if(veclen1 < 1) veclen1 = 1;
                int veclen0 = args[0].length / LEN;        
                if(veclen0 < 1) veclen0 = 1;
                int dj = args[1].length != veclen1*LEN ? 0 : 1;
                int dk = args[0].length != veclen0*LEN ? 0 : 1;
                int veclen = veclen0+veclen1;
                result = new float[LEN*veclen];
                for (int i = 0, j = 0, k = 0; i < LEN; i++, j += dj, k += dk) {
                    for (int v = 0; v < veclen; v++) {
                        if(v < veclen1) {
                            result[i*veclen + v] = args[1][j*veclen1 + v];
                        } else {
                            result[i*veclen + v] = args[0][k*veclen0 + (v-veclen1)];
                        }
                        
                    }
                }
                return result;
            }

            @Override
            public double[] evaluateDouble(int LEN, double[][] args) throws Exception {
                double[] result;
                int veclen1 = args[1].length / LEN;
                if(veclen1 < 1) veclen1 = 1;
                int veclen0 = args[0].length / LEN;        
                if(veclen0 < 1) veclen0 = 1;
                int dj = args[1].length != veclen1*LEN ? 0 : 1;
                int dk = args[0].length != veclen0*LEN ? 0 : 1;
                int veclen = veclen0+veclen1;
                result = new double[LEN*veclen];
                for (int i = 0, j = 0, k = 0; i < LEN; i++, j += dj, k += dk) {
                    for (int v = 0; v < veclen; v++) {
                        if(v < veclen1) {
                            result[i*veclen + v] = args[1][j*veclen1 + v];
                        } else {
                            result[i*veclen + v] = args[0][k*veclen0 + (v-veclen1)];
                        }
                        
                    }
                }
                return result;
            }
            //</editor-fold>
        },
        SUM {
            //<editor-fold defaultstate="collapsed" desc=" SUM ">
            @Override
            public String toString() {
                return "+";
            }

            @Override
            public int getPrecedence() {
                return 1;
            }

            @Override
            public int getNArgumetns() {
                return 2;
            }

            @Override
            public float[] evaluateFloat(int LEN, float[][] args) throws Exception {
                float[] result;
                int veclen1 = args[1].length / LEN;
                if(veclen1 < 1) veclen1 = 1;
                int veclen0 = args[0].length / LEN;        
                if(veclen0 < 1) veclen0 = 1;
                int dj = args[1].length != veclen1*LEN ? 0 : 1;
                int dk = args[0].length != veclen0*LEN ? 0 : 1;

                if (veclen1 == 1 && veclen0 == 1) {
                    //scalar + scalar
                    result = new float[LEN];
                    for (int i = 0, j = 0, k = 0; i < LEN; i++, j += dj, k += dk) {
                        result[i] = args[1][j] + args[0][k];
                    }
                } else if (veclen0 == 1) {
                    //vector + scalar
                    result = new float[LEN*veclen1];
                    for (int i = 0, j = 0, k = 0; i < LEN; i++, j += dj, k += dk) {
                        for (int v = 0; v < veclen1; v++) {
                            result[i*veclen1 + v] = args[1][j * veclen1 + v] + args[0][k];                            
                        }                        
                    }
                } else if (veclen1 == 1) {
                    //scalar + vector                    
                    result = new float[LEN*veclen0];
                    for (int i = 0, j = 0, k = 0; i < LEN; i++, j += dj, k += dk) {
                        for (int v = 0; v < veclen0; v++) {
                            result[i*veclen0 + v] = args[1][j] + args[0][k*veclen0 + v];
                        }
                    }
                } else if(veclen0 == veclen1) {
                    //vector + vector
                    result = new float[LEN*veclen0];
                    for (int i = 0, j = 0, k = 0; i < LEN; i++, j+=dj, k+=dk) {
                        for (int v = 0; v < veclen0; v++) {
                            result[i*veclen0 + v] = args[1][j*veclen0 + v] + args[0][k*veclen0 + v];
                        }
                    }
                } else {
                    throw new Exception("Cannot sum two vectors of different length");
                }
                return result;
            }

            @Override
            public double[] evaluateDouble(int LEN, double[][] args) throws Exception {
                double[] result;
                int veclen1 = args[1].length / LEN;
                if(veclen1 < 1) veclen1 = 1;
                int veclen0 = args[0].length / LEN;        
                if(veclen0 < 1) veclen0 = 1;
                int dj = args[1].length != veclen1*LEN ? 0 : 1;
                int dk = args[0].length != veclen0*LEN ? 0 : 1;

                if (veclen1 == 1 && veclen0 == 1) {
                    //scalar + scalar
                    result = new double[LEN];
                    for (int i = 0, j = 0, k = 0; i < LEN; i++, j += dj, k += dk) {
                        result[i] = args[1][j] + args[0][k];
                    }
                } else if (veclen0 == 1) {
                    //vector + scalar
                    result = new double[LEN*veclen1];
                    for (int i = 0, j = 0, k = 0; i < LEN; i++, j += dj, k += dk) {
                        for (int v = 0; v < veclen1; v++) {
                            result[i*veclen1 + v] = args[1][j * veclen1 + v] + args[0][k];                            
                        }                        
                    }
                } else if (veclen1 == 1) {
                    //scalar + vector                    
                    result = new double[LEN*veclen0];
                    for (int i = 0, j = 0, k = 0; i < LEN; i++, j += dj, k += dk) {
                        for (int v = 0; v < veclen0; v++) {
                            result[i*veclen0 + v] = args[1][j] + args[0][k*veclen0 + v];
                        }
                    }
                } else if(veclen0 == veclen1) {
                    //vector + vector
                    result = new double[LEN*veclen0];
                    for (int i = 0, j = 0, k = 0; i < LEN; i++, j+=dj, k+=dk) {
                        for (int v = 0; v < veclen0; v++) {
                            result[i*veclen0 + v] = args[1][j*veclen0 + v] + args[0][k*veclen0 + v];
                        }
                    }
                } else {
                    throw new Exception("Cannot sum two vectors of different length");
                }
                return result;
            }
            //</editor-fold>
        },
        DIFF {
            //<editor-fold defaultstate="collapsed" desc=" DIFF ">
            @Override
            public String toString() {
                return "-";
            }

            @Override
            public int getPrecedence() {
                return 1;
            }

            @Override
            public int getNArgumetns() {
                return 2;
            }

            @Override
            public float[] evaluateFloat(int LEN, float[][] args) throws Exception {
                float[] result;
                int veclen1 = args[1].length / LEN;
                if(veclen1 < 1) veclen1 = 1;
                int veclen0 = args[0].length / LEN;        
                if(veclen0 < 1) veclen0 = 1;
                int dj = args[1].length != veclen1*LEN ? 0 : 1;
                int dk = args[0].length != veclen0*LEN ? 0 : 1;

                if (veclen1 == 1 && veclen0 == 1) {
                    //scalar - scalar
                    result = new float[LEN];
                    for (int i = 0, j = 0, k = 0; i < LEN; i++, j += dj, k += dk) {
                        result[i] = args[1][j] - args[0][k];
                    }
                } else if (veclen0 == 1) {
                    //vector - scalar
                    result = new float[LEN*veclen1];
                    for (int i = 0, j = 0, k = 0; i < LEN; i++, j += dj, k += dk) {
                        for (int v = 0; v < veclen1; v++) {
                            result[i*veclen1 + v] = args[1][j * veclen1 + v] - args[0][k];
                        }                        
                    }
                } else if (veclen1 == 1) {
                    //scalar - vector                    
                    result = new float[LEN*veclen0];
                    for (int i = 0, j = 0, k = 0; i < LEN; i++, j += dj, k += dk) {
                        for (int v = 0; v < veclen0; v++) {
                            result[i*veclen0 + v] = args[1][j] - args[0][k*veclen0 + v];
                        }
                    }
                } else if(veclen0 == veclen1) {
                    //vector - vector
                    result = new float[LEN*veclen0];
                    for (int i = 0, j = 0, k = 0; i < LEN; i++, j+=dj, k+=dk) {
                        for (int v = 0; v < veclen0; v++) {
                            result[i*veclen0 + v] = args[1][j*veclen0 + v] - args[0][k*veclen0 + v];
                        }
                    }
                } else {
                    throw new Exception("Cannot differentiate two vectors of different length");
                }
                return result;
            }

            @Override
            public double[] evaluateDouble(int LEN, double[][] args) throws Exception {
                double[] result;
                int veclen1 = args[1].length / LEN;
                if(veclen1 < 1) veclen1 = 1;
                int veclen0 = args[0].length / LEN;        
                if(veclen0 < 1) veclen0 = 1;
                int dj = args[1].length != veclen1*LEN ? 0 : 1;
                int dk = args[0].length != veclen0*LEN ? 0 : 1;

                if (veclen1 == 1 && veclen0 == 1) {
                    //scalar - scalar
                    result = new double[LEN];
                    for (int i = 0, j = 0, k = 0; i < LEN; i++, j += dj, k += dk) {
                        result[i] = args[1][j] - args[0][k];
                    }
                } else if (veclen0 == 1) {
                    //vector - scalar
                    result = new double[LEN*veclen1];
                    for (int i = 0, j = 0, k = 0; i < LEN; i++, j += dj, k += dk) {
                        for (int v = 0; v < veclen1; v++) {
                            result[i*veclen1 + v] = args[1][j * veclen1 + v] - args[0][k];
                        }                        
                    }
                } else if (veclen1 == 1) {
                    //scalar - vector                    
                    result = new double[LEN*veclen0];
                    for (int i = 0, j = 0, k = 0; i < LEN; i++, j += dj, k += dk) {
                        for (int v = 0; v < veclen0; v++) {
                            result[i*veclen0 + v] = args[1][j] - args[0][k*veclen0 + v];
                        }
                    }
                } else if(veclen0 == veclen1) {
                    //vector - vector
                    result = new double[LEN*veclen0];
                    for (int i = 0, j = 0, k = 0; i < LEN; i++, j+=dj, k+=dk) {
                        for (int v = 0; v < veclen0; v++) {
                            result[i*veclen0 + v] = args[1][j*veclen0 + v] - args[0][k*veclen0 + v];
                        }
                    }
                } else {
                    throw new Exception("Cannot differentiate two vectors of different length");
                }
                return result;
            }
            //</editor-fold>
        },
        MULT {
            //<editor-fold defaultstate="collapsed" desc=" MULT ">
            @Override
            public String toString() {
                return "*";
            }

            @Override
            public int getPrecedence() {
                return 5;
            }

            @Override
            public int getNArgumetns() {
                return 2;
            }

            @Override
            public float[] evaluateFloat(int LEN, float[][] args) throws Exception {
                float[] result;
                int veclen1 = args[1].length / LEN;
                if(veclen1 < 1) veclen1 = 1;
                int veclen0 = args[0].length / LEN;        
                if(veclen0 < 1) veclen0 = 1;
                int dj = args[1].length != veclen1*LEN ? 0 : 1;
                int dk = args[0].length != veclen0*LEN ? 0 : 1;

                if (veclen1 == 1 && veclen0 == 1) {
                    //scalar * scalar
                    result = new float[LEN];
                    for (int i = 0, j = 0, k = 0; i < LEN; i++, j += dj, k += dk) {
                        result[i] = args[1][j] * args[0][k];
                    }
                } else if (veclen0 == 1) {
                    //vector * scalar
                    result = new float[LEN*veclen1];
                    for (int i = 0, j = 0, k = 0; i < LEN; i++, j += dj, k += dk) {
                        for (int v = 0; v < veclen1; v++) {
                            result[i*veclen1 + v] = args[1][j * veclen1 + v] * args[0][k];                            
                        }                        
                    }
                } else if (veclen1 == 1) {
                    //scalar * vector                    
                    result = new float[LEN*veclen0];
                    for (int i = 0, j = 0, k = 0; i < LEN; i++, j += dj, k += dk) {
                        for (int v = 0; v < veclen0; v++) {
                            result[i*veclen0 + v] = args[1][j] * args[0][k*veclen0 + v];
                        }
                    }
                } else if(veclen0 == veclen1) {
                    //vector * vector => vector dot product
                    result = new float[LEN];
                    for (int i = 0; i < result.length; i++) {
                        result[i] = 0.0f;                        
                    }
                    for (int i = 0, j = 0, k = 0; i < LEN; i++, j+=dj, k+=dk) {
                        for (int v = 0; v < veclen0; v++) {
                            result[i] += args[1][j*veclen0 + v] * args[0][k*veclen0 + v];
                        }
                    }
                } else {
                    throw new Exception("Cannot multiply two vectors of different length");
                }
                return result;
            }

            @Override
            public double[] evaluateDouble(int LEN, double[][] args) throws Exception {
                double[] result;
                int veclen1 = args[1].length / LEN;
                if(veclen1 < 1) veclen1 = 1;
                int veclen0 = args[0].length / LEN;        
                if(veclen0 < 1) veclen0 = 1;
                int dj = args[1].length != veclen1*LEN ? 0 : 1;
                int dk = args[0].length != veclen0*LEN ? 0 : 1;

                if (veclen1 == 1 && veclen0 == 1) {
                    //scalar * scalar
                    result = new double[LEN];
                    for (int i = 0, j = 0, k = 0; i < LEN; i++, j += dj, k += dk) {
                        result[i] = args[1][j] * args[0][k];
                    }
                } else if (veclen0 == 1) {
                    //vector * scalar
                    result = new double[LEN*veclen1];
                    for (int i = 0, j = 0, k = 0; i < LEN; i++, j += dj, k += dk) {
                        for (int v = 0; v < veclen1; v++) {
                            result[i*veclen1 + v] = args[1][j * veclen1 + v] * args[0][k];                            
                        }                        
                    }
                } else if (veclen1 == 1) {
                    //scalar * vector                    
                    result = new double[LEN*veclen0];
                    for (int i = 0, j = 0, k = 0; i < LEN; i++, j += dj, k += dk) {
                        for (int v = 0; v < veclen0; v++) {
                            result[i*veclen0 + v] = args[1][j] * args[0][k*veclen0 + v];
                        }
                    }
                } else if(veclen0 == veclen1) {
                    //vector * vector => vector dot product
                    result = new double[LEN];
                    for (int i = 0; i < result.length; i++) {
                        result[i] = 0.0f;                        
                    }
                    for (int i = 0, j = 0, k = 0; i < LEN; i++, j+=dj, k+=dk) {
                        for (int v = 0; v < veclen0; v++) {
                            result[i] += args[1][j*veclen0 + v] * args[0][k*veclen0 + v];
                        }
                    }
                } else {
                    throw new Exception("Cannot multiply two vectors of different length");
                }
                return result;
            }
            //</editor-fold>
        },                
        DIV {
            //<editor-fold defaultstate="collapsed" desc=" DIV ">
            @Override
            public String toString() {
                return "/";
            }

            @Override
            public int getPrecedence() {
                return 5;
            }

            @Override
            public int getNArgumetns() {
                return 2;
            }

            @Override
            public float[] evaluateFloat(int LEN, float[][] args) throws Exception {
                float[] result;
                int veclen1 = args[1].length / LEN;
                if(veclen1 < 1) veclen1 = 1;
                int veclen0 = args[0].length / LEN;        
                if(veclen0 < 1) veclen0 = 1;
                int dj = args[1].length != veclen1*LEN ? 0 : 1;
                int dk = args[0].length != veclen0*LEN ? 0 : 1;

                if (veclen1 == 1 && veclen0 == 1) {
                    //scalar / scalar
                    result = new float[LEN];
                    for (int i = 0, j = 0, k = 0; i < LEN; i++, j += dj, k += dk) {
                        result[i] = args[1][j] / args[0][k];
                    }
                } else if (veclen0 == 1) {
                    //vector / scalar
                    result = new float[LEN*veclen1];
                    for (int i = 0, j = 0, k = 0; i < LEN; i++, j += dj, k += dk) {
                        for (int v = 0; v < veclen1; v++) {
                            result[i*veclen1 + v] = args[1][j * veclen1 + v] / args[0][k];
                        }                        
                    }
                } else if (veclen1 == 1) {
                    //scalar / vector                    
                    throw new Exception("Cannot divide scalar by vector");
                } else if(veclen0 == veclen1) {
                    //vector / vector
                    throw new Exception("Cannot divide vector by vector");
                } else {
                    throw new Exception("Error in divide ");
                }
                return result;
            }

            @Override
            public double[] evaluateDouble(int LEN, double[][] args) throws Exception {
                double[] result;
                int veclen1 = args[1].length / LEN;
                if(veclen1 < 1) veclen1 = 1;
                int veclen0 = args[0].length / LEN;        
                if(veclen0 < 1) veclen0 = 1;
                int dj = args[1].length != veclen1*LEN ? 0 : 1;
                int dk = args[0].length != veclen0*LEN ? 0 : 1;

                if (veclen1 == 1 && veclen0 == 1) {
                    //scalar / scalar
                    result = new double[LEN];
                    for (int i = 0, j = 0, k = 0; i < LEN; i++, j += dj, k += dk) {
                        result[i] = args[1][j] / args[0][k];
                    }
                } else if (veclen0 == 1) {
                    //vector / scalar
                    result = new double[LEN*veclen1];
                    for (int i = 0, j = 0, k = 0; i < LEN; i++, j += dj, k += dk) {
                        for (int v = 0; v < veclen1; v++) {
                            result[i*veclen1 + v] = args[1][j * veclen1 + v] / args[0][k];
                        }                        
                    }
                } else if (veclen1 == 1) {
                    //scalar / vector                    
                    throw new Exception("Cannot divide scalar by vector");
                } else if(veclen0 == veclen1) {
                    //vector / vector
                    throw new Exception("Cannot divide vector by vector");
                } else {
                    throw new Exception("Error in divide ");
                }
                return result;
            }
            //</editor-fold>
        },
        POW {
            //<editor-fold defaultstate="collapsed" desc=" POW ">
            @Override
            public String toString() {
                return "^";
            }

            @Override
            public int getPrecedence() {
                return 10;
            }

            @Override
            public int getAssociativity() {
                return RIGHT_ASSOC;
            }

            @Override
            public int getNArgumetns() {
                return 2;
            }

            @Override
            public float[] evaluateFloat(int LEN, float[][] args) throws Exception {
                float[] result;
                int veclen1 = args[1].length / LEN;
                if(veclen1 < 1) veclen1 = 1;
                int veclen0 = args[0].length / LEN;        
                if(veclen0 < 1) veclen0 = 1;
                int dj = args[1].length != veclen1*LEN ? 0 : 1;
                int dk = args[0].length != veclen0*LEN ? 0 : 1;

                if (veclen1 == 1 && veclen0 == 1) {
                    //scalar ^ scalar
                    result = new float[LEN];
                    for (int i = 0, j = 0, k = 0; i < LEN; i++, j += dj, k += dk) {
                        result[i] = (float) Math.pow(args[1][j],args[0][k]);
                    }
                } else if (veclen0 == 1) {
                    //vector ^ scalar
                    throw new Exception("Cannot power vector");
                } else if (veclen1 == 1) {
                    //scalar ^ vector                    
                    throw new Exception("Cannot use vector as power");
                } else if(veclen0 == veclen1) {
                    //vector ^ vector
                    throw new Exception("Cannot power vector");
                } else {
                    throw new Exception("Error in power");
                }
                return result;
            }

            @Override
            public double[] evaluateDouble(int LEN, double[][] args) throws Exception {
                double[] result;
                int veclen1 = args[1].length / LEN;
                if(veclen1 < 1) veclen1 = 1;
                int veclen0 = args[0].length / LEN;        
                if(veclen0 < 1) veclen0 = 1;
                int dj = args[1].length != veclen1*LEN ? 0 : 1;
                int dk = args[0].length != veclen0*LEN ? 0 : 1;

                if (veclen1 == 1 && veclen0 == 1) {
                    //scalar ^ scalar
                    result = new double[LEN];
                    for (int i = 0, j = 0, k = 0; i < LEN; i++, j += dj, k += dk) {
                        result[i] = Math.pow(args[1][j],args[0][k]);
                    }
                } else if (veclen0 == 1) {
                    //vector ^ scalar
                    throw new Exception("Cannot power vector");
                } else if (veclen1 == 1) {
                    //scalar ^ vector                    
                    throw new Exception("Cannot use vector as power");
                } else if(veclen0 == veclen1) {
                    //vector ^ vector
                    throw new Exception("Cannot power vector");
                } else {
                    throw new Exception("Error in power");
                }
                return result;
            }
            //</editor-fold>
        },        
        NEG {
            //<editor-fold defaultstate="collapsed" desc=" NEG ">
            @Override
            public String toString() {
                return "~";
            }

            @Override
            public int getPrecedence() {
                return 12;
            }

            @Override
            public float[] evaluateFloat(int LEN, float[][] args) throws Exception {
                float[] result = new float[LEN];
                int veclen0 = args[0].length / LEN;        
                if(veclen0 < 1) veclen0 = 1;
                int dk = args[0].length != veclen0*LEN ? 0 : 1;
                for (int i = 0, k = 0; i < LEN; i++, k += dk) {
                    result[i] = -args[0][k];
                }
                return result;
            }

            @Override
            public double[] evaluateDouble(int LEN, double[][] args) throws Exception {
                double[] result = new double[LEN];
                int veclen0 = args[0].length / LEN;        
                if(veclen0 < 1) veclen0 = 1;
                int dk = args[0].length != veclen0*LEN ? 0 : 1;
                for (int i = 0, k = 0; i < LEN; i++, k += dk) {
                    result[i] = -args[0][k];
                }
                return result;
            }
            //</editor-fold>
        },
        SQRT {
            //<editor-fold defaultstate="collapsed" desc=" SQRT ">
            @Override
            public String toString() {
                return "sqrt";
            }

            @Override
            public float[] evaluateFloat(int LEN, float[][] args) throws Exception {
                float[] result = new float[LEN];
                int veclen0 = args[0].length / LEN;        
                if(veclen0 < 1) veclen0 = 1;
                int dk = args[0].length != veclen0*LEN ? 0 : 1;

                if (veclen0 == 1) {
                    for (int i = 0, k = 0; i < LEN; i++, k += dk) {
                        result[i] = (float) Math.sqrt(args[0][k]);
                    }
                } else {
                    throw new Exception("Cannot SQRT vector");
                }
                return result;
            }

            @Override
            public double[] evaluateDouble(int LEN, double[][] args) throws Exception {
                double[] result = new double[LEN];
                int veclen0 = args[0].length / LEN;        
                if(veclen0 < 1) veclen0 = 1;
                int dk = args[0].length != veclen0*LEN ? 0 : 1;

                if (veclen0 == 1) {
                    for (int i = 0, k = 0; i < LEN; i++, k += dk) {
                        result[i] = Math.sqrt(args[0][k]);
                    }
                } else {
                    throw new Exception("Cannot SQRT vector");
                }
                return result;
            }
            //</editor-fold>
        },
        LOG {
            //<editor-fold defaultstate="collapsed" desc=" LOG ">
            @Override
            public String toString() {
                return "log";
            }

            @Override
            public float[] evaluateFloat(int LEN, float[][] args) throws Exception {
                float[] result = new float[LEN];
                int veclen0 = args[0].length / LEN;        
                if(veclen0 < 1) veclen0 = 1;
                int dk = args[0].length != veclen0*LEN ? 0 : 1;

                if (veclen0 == 1) {
                    for (int i = 0, k = 0; i < LEN; i++, k += dk) {
                        result[i] = (float) Math.log(args[0][k]);
                    }
                } else {
                    throw new Exception("Cannot LOG vector");
                }
                return result;
            }

            @Override
            public double[] evaluateDouble(int LEN, double[][] args) throws Exception {
                double[] result = new double[LEN];
                int veclen0 = args[0].length / LEN;        
                if(veclen0 < 1) veclen0 = 1;
                int dk = args[0].length != veclen0*LEN ? 0 : 1;

                if (veclen0 == 1) {
                    for (int i = 0, k = 0; i < LEN; i++, k += dk) {
                        result[i] = Math.log(args[0][k]);
                    }
                } else {
                    throw new Exception("Cannot LOG vector");
                }
                return result;
            }
            //</editor-fold>
        },
        LOG10 {
            //<editor-fold defaultstate="collapsed" desc=" LOG10 ">
            @Override
            public String toString() {
                return "log10";
            }

            @Override
            public float[] evaluateFloat(int LEN, float[][] args) throws Exception {
                float[] result = new float[LEN];
                int veclen0 = args[0].length / LEN;        
                if(veclen0 < 1) veclen0 = 1;
                int dk = args[0].length != veclen0*LEN ? 0 : 1;

                if (veclen0 == 1) {
                    for (int i = 0, k = 0; i < LEN; i++, k += dk) {
                        result[i] = (float) Math.log10(args[0][k]);
                    }
                } else {
                    throw new Exception("Cannot LOG10 vector");
                }
                return result;
            }

            @Override
            public double[] evaluateDouble(int LEN, double[][] args) throws Exception {
                double[] result = new double[LEN];
                int veclen0 = args[0].length / LEN;        
                if(veclen0 < 1) veclen0 = 1;
                int dk = args[0].length != veclen0*LEN ? 0 : 1;

                if (veclen0 == 1) {
                    for (int i = 0, k = 0; i < LEN; i++, k += dk) {
                        result[i] = Math.log10(args[0][k]);
                    }
                } else {
                    throw new Exception("Cannot LOG10 vector");
                }
                return result;
            }
            //</editor-fold>
        },
        EXP {
            //<editor-fold defaultstate="collapsed" desc=" EXP ">
            @Override
            public String toString() {
                return "exp";
            }

            @Override
            public float[] evaluateFloat(int LEN, float[][] args) throws Exception {
                float[] result = new float[LEN];
                int veclen0 = args[0].length / LEN;        
                if(veclen0 < 1) veclen0 = 1;
                int dk = args[0].length != veclen0*LEN ? 0 : 1;

                if (veclen0 == 1) {
                    for (int i = 0, k = 0; i < LEN; i++, k += dk) {
                        result[i] = (float) Math.exp(args[0][k]);
                    }
                } else {
                    throw new Exception("Cannot EXP vector");
                }
                return result;
            }

            @Override
            public double[] evaluateDouble(int LEN, double[][] args) throws Exception {
                double[] result = new double[LEN];
                int veclen0 = args[0].length / LEN;        
                if(veclen0 < 1) veclen0 = 1;
                int dk = args[0].length != veclen0*LEN ? 0 : 1;

                if (veclen0 == 1) {
                    for (int i = 0, k = 0; i < LEN; i++, k += dk) {
                        result[i] = Math.exp(args[0][k]);
                    }
                } else {
                    throw new Exception("Cannot EXP vector");
                }
                return result;
            }
            //</editor-fold>
        },
        ABS {
            //<editor-fold defaultstate="collapsed" desc=" ABS ">
            @Override
            public String toString() {
                return "abs";
            }

            @Override
            public float[] evaluateFloat(int LEN, float[][] args) throws Exception {
                float[] result = new float[LEN];
                int veclen = args[0].length / LEN;        
                if(veclen < 1) veclen = 1;
                int dk = args[0].length != veclen*LEN ? 0 : 1;
                
                if (veclen == 1) {
                    for (int i = 0, k = 0; i < LEN; i++, k += dk) {
                        result[i] = Math.abs(args[0][k]);
                    }
                } else {
                    //abs of a vector component
                    for (int i = 0; i < LEN; i++) {
                        result[i] = 0;
                    }
                    for (int i = 0; i < LEN; i++) {
                        for (int j = 0; j < veclen; j++) {
                            result[i] += args[0][i * veclen + j] * args[0][i * veclen + j];
                        }
                    }
                    for (int i = 0; i < LEN; i++) {
                        result[i] = (float) Math.sqrt(result[i]);
                    }
                }
                return result;
            }

            @Override
            public double[] evaluateDouble(int LEN, double[][] args) throws Exception {
                double[] result = new double[LEN];
                int veclen = args[0].length / LEN;        
                if(veclen < 1) veclen = 1;
                int dk = args[0].length != veclen*LEN ? 0 : 1;
                
                if (veclen == 1) {
                    for (int i = 0, k = 0; i < LEN; i++, k += dk) {
                        result[i] = Math.abs(args[0][k]);
                    }
                } else {
                    //abs of a vector component
                    for (int i = 0; i < LEN; i++) {
                        result[i] = 0;
                    }
                    for (int i = 0; i < LEN; i++) {
                        for (int j = 0; j < veclen; j++) {
                            result[i] += args[0][i * veclen + j] * args[0][i * veclen + j];
                        }
                    }
                    for (int i = 0; i < LEN; i++) {
                        result[i] = Math.sqrt(result[i]);
                    }
                }
                return result;
            }
            //</editor-fold>
        },
        SIN {
            //<editor-fold defaultstate="collapsed" desc=" SIN ">
            @Override
            public String toString() {
                return "sin";
            }

            @Override
            public float[] evaluateFloat(int LEN, float[][] args) throws Exception {
                float[] result = new float[LEN];
                int veclen0 = args[0].length / LEN;        
                if(veclen0 < 1) veclen0 = 1;
                int dk = args[0].length != veclen0*LEN ? 0 : 1;

                if (veclen0 == 1) {
                    for (int i = 0, k = 0; i < LEN; i++, k += dk) {
                        result[i] = (float) Math.sin(args[0][k]);
                    }
                } else {
                    throw new Exception("Cannot SIN vector");
                }
                return result;
            }

            @Override
            public double[] evaluateDouble(int LEN, double[][] args) throws Exception {
                double[] result = new double[LEN];
                int veclen0 = args[0].length / LEN;        
                if(veclen0 < 1) veclen0 = 1;
                int dk = args[0].length != veclen0*LEN ? 0 : 1;

                if (veclen0 == 1) {
                    for (int i = 0, k = 0; i < LEN; i++, k += dk) {
                        result[i] = Math.sin(args[0][k]);
                    }
                } else {
                    throw new Exception("Cannot SIN vector");
                }
                return result;
            }
            //</editor-fold>
        },
        COS {
            @Override
            public String toString() {
                return "cos";
            }

            @Override
            public float[] evaluateFloat(int LEN, float[][] args) throws Exception {
                float[] result = new float[LEN];
                int veclen0 = args[0].length / LEN;        
                if(veclen0 < 1) veclen0 = 1;
                int dk = args[0].length != veclen0*LEN ? 0 : 1;

                if (veclen0 == 1) {
                    for (int i = 0, k = 0; i < LEN; i++, k += dk) {
                        result[i] = (float) Math.cos(args[0][k]);
                    }
                } else {
                    throw new Exception("Cannot COS vector");
                }
                return result;
            }

            @Override
            public double[] evaluateDouble(int LEN, double[][] args) throws Exception {
                double[] result = new double[LEN];
                int veclen0 = args[0].length / LEN;        
                if(veclen0 < 1) veclen0 = 1;
                int dk = args[0].length != veclen0*LEN ? 0 : 1;

                if (veclen0 == 1) {
                    for (int i = 0, k = 0; i < LEN; i++, k += dk) {
                        result[i] = Math.cos(args[0][k]);
                    }
                } else {
                    throw new Exception("Cannot COS vector");
                }
                return result;
            }
        },
        TAN {
            @Override
            public String toString() {
                return "tan";
            }

            @Override
            public float[] evaluateFloat(int LEN, float[][] args) throws Exception {
                float[] result = new float[LEN];
                int veclen0 = args[0].length / LEN;        
                if(veclen0 < 1) veclen0 = 1;
                int dk = args[0].length != veclen0*LEN ? 0 : 1;

                if (veclen0 == 1) {
                    for (int i = 0, k = 0; i < LEN; i++, k += dk) {
                        result[i] = (float) Math.tan(args[0][k]);
                    }
                } else {
                    throw new Exception("Cannot TAN vector");
                }
                return result;
            }

            @Override
            public double[] evaluateDouble(int LEN, double[][] args) throws Exception {
                double[] result = new double[LEN];
                int veclen0 = args[0].length / LEN;        
                if(veclen0 < 1) veclen0 = 1;
                int dk = args[0].length != veclen0*LEN ? 0 : 1;

                if (veclen0 == 1) {
                    for (int i = 0, k = 0; i < LEN; i++, k += dk) {
                        result[i] = Math.tan(args[0][k]);
                    }
                } else {
                    throw new Exception("Cannot TAN vector");
                }
                return result;
            }
        },
        ASIN {
            @Override
            public String toString() {
                return "asin";
            }

            @Override
            public float[] evaluateFloat(int LEN, float[][] args) throws Exception {
                float[] result = new float[LEN];
                int veclen0 = args[0].length / LEN;        
                if(veclen0 < 1) veclen0 = 1;
                int dk = args[0].length != veclen0*LEN ? 0 : 1;

                if (veclen0 == 1) {
                    for (int i = 0, k = 0; i < LEN; i++, k += dk) {
                        result[i] = (float) Math.asin(args[0][k]);
                    }
                } else {
                    throw new Exception("Cannot ASIN vector");
                }
                return result;
            }

            @Override
            public double[] evaluateDouble(int LEN, double[][] args) throws Exception {
                double[] result = new double[LEN];
                int veclen0 = args[0].length / LEN;        
                if(veclen0 < 1) veclen0 = 1;
                int dk = args[0].length != veclen0*LEN ? 0 : 1;

                if (veclen0 == 1) {
                    for (int i = 0, k = 0; i < LEN; i++, k += dk) {
                        result[i] = Math.asin(args[0][k]);
                    }
                } else {
                    throw new Exception("Cannot ASIN vector");
                }
                return result;
            }
        },
        ACOS {
            @Override
            public String toString() {
                return "acos";
            }

            @Override
            public float[] evaluateFloat(int LEN, float[][] args) throws Exception {
                float[] result = new float[LEN];
                int veclen0 = args[0].length / LEN;        
                if(veclen0 < 1) veclen0 = 1;
                int dk = args[0].length != veclen0*LEN ? 0 : 1;

                if (veclen0 == 1) {
                    for (int i = 0, k = 0; i < LEN; i++, k += dk) {
                        result[i] = (float) Math.acos(args[0][k]);
                    }
                } else {
                    throw new Exception("Cannot ACOS vector");
                }
                return result;
            }

            @Override
            public double[] evaluateDouble(int LEN, double[][] args) throws Exception {
                double[] result = new double[LEN];
                int veclen0 = args[0].length / LEN;        
                if(veclen0 < 1) veclen0 = 1;
                int dk = args[0].length != veclen0*LEN ? 0 : 1;

                if (veclen0 == 1) {
                    for (int i = 0, k = 0; i < LEN; i++, k += dk) {
                        result[i] = Math.acos(args[0][k]);
                    }
                } else {
                    throw new Exception("Cannot ACOS vector");
                }
                return result;
            }
        },
        ATAN {
            @Override
            public String toString() {
                return "atan";
            }

            @Override
            public float[] evaluateFloat(int LEN, float[][] args) throws Exception {
                float[] result = new float[LEN];
                int veclen0 = args[0].length / LEN;        
                if(veclen0 < 1) veclen0 = 1;
                int dk = args[0].length != veclen0*LEN ? 0 : 1;

                if (veclen0 == 1) {
                    for (int i = 0, k = 0; i < LEN; i++, k += dk) {
                        result[i] = (float) Math.atan(args[0][k]);
                    }
                } else {
                    throw new Exception("Cannot ATAN vector");
                }
                return result;
            }

            @Override
            public double[] evaluateDouble(int LEN, double[][] args) throws Exception {
                double[] result = new double[LEN];
                int veclen0 = args[0].length / LEN;        
                if(veclen0 < 1) veclen0 = 1;
                int dk = args[0].length != veclen0*LEN ? 0 : 1;

                if (veclen0 == 1) {
                    for (int i = 0, k = 0; i < LEN; i++, k += dk) {
                        result[i] = Math.atan(args[0][k]);
                    }
                } else {
                    throw new Exception("Cannot ATAN vector");
                }
                return result;
            }
        },
        SIG {
            @Override
            public String toString() {
                return "signum";
            }

            @Override
            public float[] evaluateFloat(int LEN, float[][] args) throws Exception {
                float[] result = new float[LEN];
                int veclen0 = args[0].length / LEN;        
                if(veclen0 < 1) veclen0 = 1;
                int dk = args[0].length != veclen0*LEN ? 0 : 1;

                for (int i = 0, k = 0; i < LEN; i++, k += dk) {
                    result[i] = (float) Math.signum(args[0][k]);
                }
                return result;
            }

            @Override
            public double[] evaluateDouble(int LEN, double[][] args) throws Exception {
                double[] result = new double[LEN];
                int veclen0 = args[0].length / LEN;        
                if(veclen0 < 1) veclen0 = 1;
                int dk = args[0].length != veclen0*LEN ? 0 : 1;

                for (int i = 0, k = 0; i < LEN; i++, k += dk) {
                    result[i] = Math.signum(args[0][k]);
                }
                return result;
            }
        },
        AVG {
            @Override
            public String toString() {
                return "avg";
            }

            @Override
            public float[] evaluateFloat(int LEN, float[][] args) throws Exception {
                float avg = 0.0f;
                float[] result = new float[LEN];
                int veclen0 = args[0].length / LEN;        
                if(veclen0 < 1) veclen0 = 1;
                int dk = args[0].length != veclen0*LEN ? 0 : 1;

                if (veclen0 == 1) {
                    for (int i = 0, k = 0; i < LEN; i++, k += dk) {
                        avg += args[0][k];
                    }
                } else {
                    throw new Exception("Cannot AVG vector");
                }
                avg /= (float) args[0].length;

                for (int i = 0; i < LEN; i++) {
                    result[i] = avg;
                }
                return result;
            }

            @Override
            public double[] evaluateDouble(int LEN, double[][] args) throws Exception {
                double avg = 0.0f;
                double[] result = new double[LEN];
                int veclen0 = args[0].length / LEN;        
                if(veclen0 < 1) veclen0 = 1;
                int dk = args[0].length != veclen0*LEN ? 0 : 1;

                if (veclen0 == 1) {
                    for (int i = 0, k = 0; i < LEN; i++, k += dk) {
                        avg += args[0][k];
                    }
                } else {
                    throw new Exception("Cannot AVG vector");
                }
                avg /= (double) args[0].length;

                for (int i = 0; i < LEN; i++) {
                    result[i] = avg;
                }
                return result;
            }
        },
        STDDEV {
            @Override
            public String toString() {
                return "stddev";
            }

            @Override
            public float[] evaluateFloat(int LEN, float[][] args) throws Exception {
                float sumx = 0.0f;
                float sumxx = 0.0f;
                float stddev;
                float[] result = new float[LEN];
                int veclen0 = args[0].length / LEN;        
                if(veclen0 < 1) veclen0 = 1;
                int dk = args[0].length != veclen0*LEN ? 0 : 1;

                if (veclen0 == 1) {
                    for (int i = 0, k = 0; i < LEN; i++, k += dk) {
                        sumx += args[0][k];
                        sumxx += args[0][k] * args[0][k];
                    }
                } else {
                    throw new Exception("Cannot STDDEV vector");
                }

                sumxx /= (float) LEN;
                sumx /= (float) LEN;
                stddev = (float) (sumxx - sumx * sumx);
                for (int i = 0; i < LEN; i++) {
                    result[i] = (float) Math.sqrt(stddev);
                }
                return result;
            }

            @Override
            public double[] evaluateDouble(int LEN, double[][] args) throws Exception {
                double sumx = 0.0f;
                double sumxx = 0.0f;
                double stddev;
                double[] result = new double[LEN];
                int veclen0 = args[0].length / LEN;        
                if(veclen0 < 1) veclen0 = 1;
                int dk = args[0].length != veclen0*LEN ? 0 : 1;

                if (veclen0 == 1) {
                    for (int i = 0, k = 0; i < LEN; i++, k += dk) {
                        sumx += args[0][k];
                        sumxx += args[0][k] * args[0][k];
                    }
                } else {
                    throw new Exception("Cannot STDDEV vector");
                }

                sumxx /= (double) LEN;
                sumx /= (double) LEN;
                stddev = sumxx - sumx * sumx;
                for (int i = 0; i < LEN; i++) {
                    result[i] = Math.sqrt(stddev);
                }
                return result;
            }
        };

        //defaults set for functions, need to be Overriden in othe operators
        public int getPrecedence() {
            return 15;
        }

        public int getAssociativity() {
            return LEFT_ASSOC;
        }

        public int getNArgumetns() {
            return 1;
        }

        public abstract float[] evaluateFloat(int LEN, float[][] args) throws Exception;

        public abstract double[] evaluateDouble(int LEN, double[][] args) throws Exception;
    }

    public static boolean isOperator(String token) {
        return (getOperator(token) != null);
    }

    public static Operator getOperator(String token) {
        Operator[] operators = Operator.values();
        for (int i = 0; i < operators.length; i++) {
            if (operators[i].toString().equals(token)) {
                return operators[i];
            }
        }
        return null;
    }

    public static boolean isAssociative(String token, int type) {
        Operator op = getOperator(token);
        if (op == null) {
            throw new IllegalArgumentException("Invalid token: " + token);
        }

        return isAssociative(op, type);
    }

    public static boolean isAssociative(Operator op, int type) {
        return (op.getAssociativity() == type);
    }

    public static int cmpPrecedence(String token1, String token2) {
        Operator op1 = getOperator(token1);
        Operator op2 = getOperator(token2);
        if (op1 == null || op2 == null) {
            throw new IllegalArgumentException("Invalid tokens: " + token1 + " " + token2);
        }
        return cmpPrecedence(op1, op2);
    }

    public static int cmpPrecedence(Operator op1, Operator op2) {
        return op1.getPrecedence() - op2.getPrecedence();
    }
    
}
