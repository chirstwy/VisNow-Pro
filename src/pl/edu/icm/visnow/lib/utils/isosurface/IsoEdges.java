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

package pl.edu.icm.visnow.lib.utils.isosurface;

/**
 *
 * @author  Krzysztof S. Nowinski, University of Warsaw, ICM
 */
public class IsoEdges
{
   
 public static final int[][][][] ig =  
    {{{{-1,-1}},
      {{ 0, 4},{ 0, 8},{ 4, 8}},
      {{ 0, 5},{ 0, 9}},
      {{ 4, 5},{ 8, 9},{ 4, 8}},
      {{ 1, 4},{ 4,10}},
      {{ 0, 1},{ 0, 8},{ 8,10}},
      {{ 0, 5},{ 0, 9},{ 1, 4},{ 4,10}},
      {{ 1, 5},{ 8, 9},{ 8,10}},
      {{ 1, 5}},
      {{ 0, 4},{ 0, 8},{ 4, 8},{ 1, 5}},
      {{ 0, 1},{ 0, 9}},
      {{ 1, 4},{ 8, 9},{ 4, 8}},
      {{ 4, 5},{ 4,10}},
      {{ 0, 5},{ 0, 8},{ 8,10}},
      {{ 0, 9},{ 0, 4},{ 4,10}},
      {{ 8, 9},{ 8,10}},
      {{ 2, 8},{ 6, 8}},
      {{ 0, 4},{ 4, 6},{ 0, 2}},
      {{ 0, 5},{ 0, 9},{ 2, 8},{ 6, 8}},
      {{ 4, 5},{ 4, 6},{ 2, 9}},
      {{ 1, 4},{ 4,10},{ 2, 8},{ 6, 8}},
      {{ 0, 1},{ 6,10},{ 0, 2}},
      {{ 0, 5},{ 0, 9},{ 1, 4},{ 4,10},{ 2, 8},{ 6, 8}},
      {{ 1, 5},{ 6,10},{ 2, 9}},
      {{ 2, 8},{ 6, 8},{ 1, 5}},
      {{ 0, 4},{ 4, 6},{ 0, 2},{ 1, 5}},
      {{ 0, 1},{ 0, 9},{ 2, 8},{ 6, 8}},
      {{ 1, 4},{ 4, 6},{ 2, 9}},
      {{ 4, 5},{ 4,10},{ 2, 8},{ 6, 8}},
      {{ 0, 5},{ 6,10},{ 0, 2}},
      {{ 0, 9},{ 0, 4},{ 4,10},{ 2, 8},{ 6, 8}},
      {{ 6,10},{ 2, 9}},
      {{ 2, 9}},
      {{ 0, 4},{ 0, 9},{ 2, 8},{ 4, 8}},
      {{ 0, 5},{ 0, 2}},
      {{ 4, 5},{ 2, 8},{ 4, 8}},
      {{ 1, 4},{ 4,10},{ 2, 9}},
      {{ 0, 1},{ 0, 9},{ 2, 8},{ 8,10}},
      {{ 0, 5},{ 0, 2},{ 1, 4},{ 4,10}},
      {{ 1, 5},{ 2, 8},{ 8,10}},
      {{ 2, 9},{ 1, 5}},
      {{ 1, 4},{ 0, 5},{ 0, 9},{ 2, 8},{ 4, 8}},
      {{ 0, 1},{ 0, 2}},
      {{ 1, 4},{ 2, 8},{ 4, 8}},
      {{ 2, 9},{ 4, 5},{ 4,10}},
      {{ 0, 5},{ 0, 9},{ 2, 8},{ 8,10}},
      {{ 0, 2},{ 0, 4},{ 4,10}},
      {{ 2, 8},{ 8,10}},
      {{ 8, 9},{ 6, 8}},
      {{ 0, 4},{ 0, 9},{ 4, 6}},
      {{ 0, 5},{ 0, 8},{ 6, 8}},
      {{ 4, 5},{ 4, 6}},
      {{ 1, 4},{ 4,10},{ 8, 9},{ 6, 8}},
      {{ 0, 9},{ 6,10},{ 0, 1}},
      {{ 0, 5},{ 1, 4},{ 4,10},{ 0, 8},{ 6, 8}},
      {{ 6,10},{ 1, 5}},
      {{ 8, 9},{ 1, 5},{ 6, 8}},
      {{ 1, 4},{ 4, 6},{ 0, 5},{ 0, 9}},
      {{ 0, 1},{ 0, 8},{ 6, 8}},
      {{ 1, 4},{ 4, 6}},
      {{ 4, 5},{ 4,10},{ 8, 9},{ 6, 8}},
      {{ 0, 5},{ 0, 9},{ 6,10}},
      {{ 0, 4},{ 4,10},{ 0, 8},{ 6, 8}},
      {{ 6,10}},
      {{ 6,10}},
      {{ 0, 4},{ 4,10},{ 0, 8},{ 6, 8}},
      {{ 0, 5},{ 0, 9},{ 6,10}},
      {{ 4, 5},{ 4,10},{ 8, 9},{ 6, 8}},
      {{ 1, 4},{ 4, 6}},
      {{ 0, 8},{ 6, 8},{ 0, 1}},
      {{ 1, 4},{ 4, 6},{ 0, 5},{ 0, 9}},
      {{ 1, 5},{ 8, 9},{ 6, 8}},
      {{ 6,10},{ 1, 5}},
      {{ 0, 5},{ 1, 4},{ 4,10},{ 0, 8},{ 6, 8}},
      {{ 6,10},{ 0, 9},{ 0, 1}},
      {{ 1, 4},{ 4,10},{ 8, 9},{ 6, 8}},
      {{ 4, 5},{ 4, 6}},
      {{ 0, 5},{ 0, 8},{ 6, 8}},
      {{ 0, 4},{ 0, 9},{ 4, 6}},
      {{ 8, 9},{ 6, 8}},
      {{ 2, 8},{ 8,10}},
      {{ 0, 2},{ 0, 4},{ 4,10}},
      {{ 0, 5},{ 0, 9},{ 2, 8},{ 8,10}},
      {{ 2, 9},{ 4, 5},{ 4,10}},
      {{ 1, 4},{ 2, 8},{ 4, 8}},
      {{ 0, 1},{ 0, 2}},
      {{ 1, 4},{ 0, 5},{ 0, 9},{ 2, 8},{ 4, 8}},
      {{ 2, 9},{ 1, 5}},
      {{ 2, 8},{ 1, 5},{ 8,10}},
      {{ 0, 5},{ 0, 2},{ 1, 4},{ 4,10}},
      {{ 2, 9},{ 0, 1},{ 0, 8},{ 8,10}},
      {{ 1, 4},{ 4,10},{ 2, 9}},
      {{ 4, 5},{ 2, 8},{ 4, 8}},
      {{ 0, 5},{ 0, 2}},
      {{ 0, 4},{ 0, 9},{ 2, 8},{ 4, 8}},
      {{ 2, 9}},
      {{ 6,10},{ 2, 9}},
      {{ 0, 9},{ 0, 4},{ 4,10},{ 2, 8},{ 6, 8}},
      {{ 0, 5},{ 6,10},{ 0, 2}},
      {{ 4, 5},{ 4,10},{ 2, 8},{ 6, 8}},
      {{ 1, 4},{ 4, 6},{ 2, 9}},
      {{ 0, 1},{ 0, 9},{ 2, 8},{ 6, 8}},
      {{ 0, 4},{ 4, 6},{ 0, 2},{ 1, 5}},
      {{ 2, 8},{ 6, 8},{ 1, 5}},
      {{ 1, 5},{ 6,10},{ 2, 9}},
      {{ 0, 5},{ 0, 9},{ 1, 4},{ 4,10},{ 2, 8},{ 6, 8}},
      {{ 0, 1},{ 6,10},{ 0, 2}},
      {{ 1, 4},{ 4,10},{ 2, 8},{ 6, 8}},
      {{ 4, 5},{ 4, 6},{ 2, 9}},
      {{ 0, 5},{ 0, 9},{ 2, 8},{ 6, 8}},
      {{ 0, 4},{ 4, 6},{ 0, 2}},
      {{ 2, 8},{ 6, 8}},
      {{ 8, 9},{ 8,10}},
      {{ 0, 9},{ 0, 4},{ 4,10}},
      {{ 0, 5},{ 0, 8},{ 8,10}},
      {{ 4, 5},{ 4,10}},
      {{ 1, 4},{ 8, 9},{ 4, 8}},
      {{ 0, 1},{ 0, 9}},
      {{ 0, 4},{ 0, 8},{ 4, 8},{ 1, 5}},
      {{ 1, 5}},
      {{ 1, 5},{ 8, 9},{ 8,10}},
      {{ 0, 5},{ 0, 9},{ 1, 4},{ 4,10}},
      {{ 0, 1},{ 0, 8},{ 8,10}},
      {{ 1, 4},{ 4,10}},
      {{ 4, 5},{ 8, 9},{ 4, 8}},
      {{ 0, 5},{ 0, 9}},
      {{ 0, 4},{ 0, 8},{ 4, 8}},
      {{-1,-1}},
      {{-1,-1}},
      {{ 0, 4},{ 0, 8},{ 4, 8}},
      {{ 0, 5},{ 0, 9}},
      {{ 4, 5},{ 8, 9},{ 4, 8}},
      {{ 1, 4},{ 4,10}},
      {{ 0, 1},{ 0, 8},{ 8,10}},
      {{ 0, 5},{ 0, 9},{ 1, 4},{ 4,10}},
      {{ 1, 5},{ 8, 9},{ 8,10}},
      {{ 1, 5}},
      {{ 0, 4},{ 0, 8},{ 4, 8},{ 1, 5}},
      {{ 0, 1},{ 0, 9}},
      {{ 1, 4},{ 8, 9},{ 4, 8}},
      {{ 4, 5},{ 4,10}},
      {{ 0, 5},{ 0, 8},{ 8,10}},
      {{ 0, 9},{ 0, 4},{ 4,10}},
      {{ 8, 9},{ 8,10}},
      {{ 2, 8},{ 6, 8}},
      {{ 0, 4},{ 4, 6},{ 0, 2}},
      {{ 0, 5},{ 0, 9},{ 2, 8},{ 6, 8}},
      {{ 4, 5},{ 4, 6},{ 2, 9}},
      {{ 1, 4},{ 4,10},{ 2, 8},{ 6, 8}},
      {{ 0, 1},{ 6,10},{ 0, 2}},
      {{ 0, 5},{ 0, 9},{ 1, 4},{ 4,10},{ 2, 8},{ 6, 8}},
      {{ 1, 5},{ 6,10},{ 2, 9}},
      {{ 2, 8},{ 6, 8},{ 1, 5}},
      {{ 0, 4},{ 4, 6},{ 0, 2},{ 1, 5}},
      {{ 0, 1},{ 0, 9},{ 2, 8},{ 6, 8}},
      {{ 1, 4},{ 2, 9},{ 4, 6}},
      {{ 4, 5},{ 4,10},{ 2, 8},{ 6, 8}},
      {{ 0, 5},{ 6,10},{ 0, 2}},
      {{ 0, 9},{ 0, 4},{ 4,10},{ 2, 8},{ 6, 8}},
      {{ 6,10},{ 2, 9}},
      {{ 2, 9}},
      {{ 0, 4},{ 0, 9},{ 2, 8},{ 4, 8}},
      {{ 0, 5},{ 0, 2}},
      {{ 4, 5},{ 2, 8},{ 4, 8}},
      {{ 1, 4},{ 4,10},{ 2, 9}},
      {{ 0, 8},{ 8,10},{ 0, 1},{ 2, 9}},
      {{ 0, 5},{ 0, 2},{ 1, 4},{ 4,10}},
      {{ 2, 8},{ 1, 5},{ 8,10}},
      {{ 2, 9},{ 1, 5}},
      {{ 1, 4},{ 0, 5},{ 0, 9},{ 2, 8},{ 4, 8}},
      {{ 0, 1},{ 0, 2}},
      {{ 1, 4},{ 2, 8},{ 4, 8}},
      {{ 2, 9},{ 4, 5},{ 4,10}},
      {{ 0, 5},{ 0, 9},{ 2, 8},{ 8,10}},
      {{ 0, 2},{ 0, 4},{ 4,10}},
      {{ 2, 8},{ 8,10}},
      {{ 8, 9},{ 6, 8}},
      {{ 0, 4},{ 0, 9},{ 4, 6}},
      {{ 0, 5},{ 0, 8},{ 6, 8}},
      {{ 4, 5},{ 4, 6}},
      {{ 1, 4},{ 4,10},{ 8, 9},{ 6, 8}},
      {{ 0, 9},{ 6,10},{ 0, 1}},
      {{ 0, 5},{ 1, 4},{ 4,10},{ 0, 8},{ 6, 8}},
      {{ 6,10},{ 1, 5}},
      {{ 1, 5},{ 8, 9},{ 6, 8}},
      {{ 1, 4},{ 4, 6},{ 0, 5},{ 0, 9}},
      {{ 0, 1},{ 0, 8},{ 6, 8}},
      {{ 1, 4},{ 4, 6}},
      {{ 4, 5},{ 4,10},{ 8, 9},{ 6, 8}},
      {{ 0, 5},{ 0, 9},{ 6,10}},
      {{ 6,10},{ 0, 4},{ 4,10},{ 0, 8},{ 6, 8},{ 6,10}},
      {{ 6,10}},
      {{ 6,10}},
      {{ 0, 4},{ 4,10},{ 6, 8},{ 0, 8}},
      {{ 0, 5},{ 0, 9},{ 6,10}},
      {{ 4, 5},{ 4,10},{ 8, 9},{ 6, 8}},
      {{ 1, 4},{ 4, 6}},
      {{ 0, 1},{ 0, 8},{ 6, 8}},
      {{ 1, 4},{ 4, 6},{ 0, 5},{ 0, 9}},
      {{ 8, 9},{ 6, 8},{ 1, 5}},
      {{ 6,10},{ 1, 5}},
      {{ 0, 5},{ 1, 4},{ 4,10},{ 0, 8},{ 6, 8}},
      {{ 6,10},{ 0, 9},{ 0, 1}},
      {{ 1, 4},{ 4,10},{ 8, 9},{ 6, 8}},
      {{ 4, 5},{ 4, 6}},
      {{ 0, 5},{ 0, 8},{ 6, 8}},
      {{ 0, 4},{ 0, 9},{ 4, 6}},
      {{ 8, 9},{ 6, 8}},
      {{ 2, 8},{ 8,10}},
      {{ 0, 2},{ 0, 4},{ 4,10}},
      {{ 0, 5},{ 0, 9},{ 2, 8},{ 8,10}},
      {{ 2, 9},{ 4,10},{ 4, 5}},
      {{ 1, 4},{ 2, 8},{ 4, 8}},
      {{ 0, 1},{ 0, 2}},
      {{ 1, 4},{ 0, 5},{ 0, 9},{ 2, 8},{ 4, 8}},
      {{ 2, 9},{ 1, 5}},
      {{ 1, 5},{ 2, 8},{ 8,10}},
      {{ 0, 5},{ 0, 2},{ 1, 4},{ 4,10}},
      {{ 0, 1},{ 0, 9},{ 2, 8},{ 8,10}},
      {{ 1, 4},{ 4,10},{ 2, 9}},
      {{ 4, 5},{ 2, 8},{ 4, 8}},
      {{ 0, 5},{ 0, 2}},
      {{ 0, 4},{ 0, 9},{ 2, 9},{ 2, 8},{ 4, 8},{ 2, 9}},
      {{ 2, 9}},
      {{ 6,10},{ 2, 9}},
      {{ 0, 9},{ 0, 4},{ 4,10},{ 2, 8},{ 6, 8}},
      {{ 0, 5},{ 6,10},{ 0, 2}},
      {{ 4, 5},{ 4,10},{ 2, 8},{ 6, 8}},
      {{ 1, 4},{ 4, 6},{ 2, 9}},
      {{ 0, 1},{ 0, 9},{ 2, 8},{ 6, 8}},
      {{ 0, 4},{ 4, 6},{ 0, 2},{ 1, 5}},
      {{ 2, 8},{ 6, 8},{ 1, 5}},
      {{ 1, 5},{ 6,10},{ 2, 9}},
      {{ 0, 5},{ 0, 9},{ 1, 4},{ 4,10},{ 2, 8},{ 6, 8}},
      {{ 0, 1},{ 6,10},{ 0, 2}},
      {{ 1, 4},{ 4,10},{ 2, 8},{ 6, 8}},
      {{ 4, 5},{ 4, 6},{ 2, 9}},
      {{ 0, 5},{ 0, 9},{ 2, 8},{ 6, 8}},
      {{ 0, 4},{ 4, 6},{ 0, 2}},
      {{ 2, 8},{ 6, 8}},
      {{ 8, 9},{ 8,10}},
      {{ 0, 9},{ 0, 4},{ 4,10}},
      {{ 0, 5},{ 0, 8},{ 8,10}},
      {{ 4, 5},{ 4,10}},
      {{ 1, 4},{ 8, 9},{ 4, 8}},
      {{ 0, 1},{ 0, 9}},
      {{ 0, 4},{ 0, 8},{ 4, 8},{ 1, 5}},
      {{ 1, 5}},
      {{ 1, 5},{ 8, 9},{ 8,10}},
      {{ 0, 5},{ 0, 9},{ 1, 4},{ 4,10}},
      {{ 0, 1},{ 0, 8},{ 8,10}},
      {{ 1, 4},{ 4,10}},
      {{ 4, 5},{ 8, 9},{ 4, 8}},
      {{ 0, 5},{ 0, 9}},
      {{ 0, 4},{ 0, 8},{ 4, 8}},
      {{-1,-1}}},
     {{{-1,-1}},
      {{ 0, 4},{ 0, 8},{ 4, 8}},
      {{ 0, 5},{ 0, 9}},
      {{ 4, 5},{ 4, 8},{ 8, 9}},
      {{ 1, 4},{ 4,10}},
      {{ 0, 1},{ 0, 8},{ 8,10}},
      {{ 0, 5},{ 0, 9},{ 1, 4},{ 4,10}},
      {{ 1, 5},{ 8, 9},{ 8,10}},
      {{ 1, 5}},
      {{ 0, 4},{ 0, 8},{ 4, 8},{ 1, 5}},
      {{ 0, 1},{ 0, 9}},
      {{ 4, 8},{ 1, 4},{ 8, 9}},
      {{ 4, 5},{ 4,10}},
      {{ 0, 8},{ 0, 5},{ 8,10}},
      {{ 0, 4},{ 0, 9},{ 4,10}},
      {{ 8, 9},{ 8,10}},
      {{ 2, 8},{ 6, 8}},
      {{ 0, 4},{ 0, 2},{ 4, 6}},
      {{ 0, 5},{ 0, 8},{ 2, 9},{ 6, 8}},
      {{ 4, 5},{ 2, 9},{ 4, 6}},
      {{ 2, 8},{ 4, 8},{ 1, 4},{ 6,10}},
      {{ 0, 1},{ 0, 2},{ 6,10}},
      {{ 1, 5},{ 0, 4},{ 0, 8},{ 4, 8},{ 2, 9},{ 6,10}},
      {{ 1, 5},{ 2, 9},{ 6,10}},
      {{ 1, 5},{ 2, 8},{ 6, 8}},
      {{ 0, 4},{ 0, 2},{ 1, 5},{ 4, 6}},
      {{ 2, 9},{ 6, 8},{ 0, 1},{ 0, 8}},
      {{ 1, 4},{ 2, 9},{ 4, 6}},
      {{ 6,10},{ 2, 8},{ 4, 5},{ 4, 8}},
      {{ 0, 2},{ 0, 5},{ 6,10}},
      {{ 0, 4},{ 0, 8},{ 4, 8},{ 2, 9},{ 6,10}},
      {{ 2, 9},{ 6,10}},
      {{ 2, 9}},
      {{ 0, 4},{ 0, 8},{ 4, 8},{ 2, 9}},
      {{ 0, 5},{ 0, 2}},
      {{ 4, 5},{ 4, 8},{ 2, 8}},
      {{ 2, 9},{ 1, 4},{ 4,10}},
      {{ 0, 1},{ 0, 8},{ 2, 9},{ 8,10}},
      {{ 0, 5},{ 0, 2},{ 1, 4},{ 4,10}},
      {{ 1, 5},{ 2, 8},{ 8,10}},
      {{ 1, 5},{ 2, 9}},
      {{ 0, 4},{ 0, 8},{ 4, 8},{ 1, 5},{ 2, 9}},
      {{ 0, 1},{ 0, 2}},
      {{ 4, 8},{ 1, 4},{ 2, 8}},
      {{ 4, 5},{ 2, 9},{ 4,10}},
      {{ 0, 8},{ 0, 5},{ 2, 9},{ 8,10}},
      {{ 0, 4},{ 0, 2},{ 4,10}},
      {{ 2, 8},{ 8,10}},
      {{ 8, 9},{ 6, 8}},
      {{ 0, 4},{ 0, 9},{ 4, 6}},
      {{ 0, 5},{ 0, 8},{ 6, 8}},
      {{ 4, 5},{ 4, 6}},
      {{ 8, 9},{ 4, 8},{ 1, 4},{ 6,10}},
      {{ 0, 1},{ 0, 9},{ 6,10}},
      {{ 1, 5},{ 0, 4},{ 0, 8},{ 4, 8},{ 6,10}},
      {{ 1, 5},{ 6,10}},
      {{ 1, 5},{ 8, 9},{ 6, 8}},
      {{ 0, 4},{ 1, 5},{ 0, 9},{ 4, 6}},
      {{ 0, 8},{ 0, 1},{ 6, 8}},
      {{ 1, 4},{ 4, 6}},
      {{ 4, 5},{ 8, 9},{ 4,10},{ 6, 8}},
      {{ 0, 5},{ 0, 9},{ 6,10}},
      {{ 0, 4},{ 0, 8},{ 4, 8},{ 6,10}},
      {{ 6,10}},
      {{ 6,10}},
      {{ 0, 4},{ 0, 8},{ 4, 8},{ 6,10}},
      {{ 0, 5},{ 0, 9},{ 6,10}},
      {{ 4, 5},{ 4, 8},{ 8, 9},{ 6,10}},
      {{ 1, 4},{ 4, 6}},
      {{ 0, 1},{ 0, 8},{ 6, 8}},
      {{ 0, 5},{ 0, 9},{ 1, 4},{ 4, 6}},
      {{ 1, 5},{ 8, 9},{ 6, 8}},
      {{ 1, 5},{ 6,10}},
      {{ 0, 4},{ 0, 8},{ 4, 8},{ 1, 5},{ 6,10}},
      {{ 0, 1},{ 0, 9},{ 6,10}},
      {{ 4, 8},{ 1, 4},{ 8, 9},{ 6,10}},
      {{ 4, 5},{ 4, 6}},
      {{ 0, 8},{ 0, 5},{ 6, 8}},
      {{ 0, 4},{ 0, 9},{ 4, 6}},
      {{ 8, 9},{ 6, 8}},
      {{ 8,10},{ 2, 8}},
      {{ 0, 4},{ 0, 2},{ 4,10}},
      {{ 0, 5},{ 0, 8},{ 8,10},{ 2, 9}},
      {{ 4, 5},{ 2, 9},{ 4,10}},
      {{ 2, 8},{ 4, 8},{ 1, 4}},
      {{ 0, 1},{ 0, 2}},
      {{ 1, 5},{ 0, 4},{ 0, 8},{ 4, 8},{ 2, 9}},
      {{ 1, 5},{ 2, 9}},
      {{ 1, 5},{ 8,10},{ 2, 8}},
      {{ 0, 4},{ 0, 2},{ 1, 5},{ 4,10}},
      {{ 0, 1},{ 0, 8},{ 8,10},{ 2, 9}},
      {{ 4,10},{ 2, 9},{ 1, 4}},
      {{ 2, 8},{ 4, 8},{ 4, 5}},
      {{ 0, 2},{ 0, 5}},
      {{ 0, 4},{ 0, 8},{ 4, 8},{ 2, 9}},
      {{ 2, 9}},
      {{ 2, 9},{ 6,10}},
      {{ 0, 4},{ 0, 8},{ 4, 8},{ 2, 9},{ 6,10}},
      {{ 0, 5},{ 0, 2},{ 6,10}},
      {{ 4, 5},{ 4, 8},{ 2, 8},{ 6,10}},
      {{ 2, 9},{ 1, 4},{ 4, 6}},
      {{ 0, 1},{ 0, 8},{ 2, 9},{ 6, 8}},
      {{ 0, 5},{ 0, 2},{ 1, 4},{ 4, 6}},
      {{ 1, 5},{ 2, 8},{ 6, 8}},
      {{ 1, 5},{ 2, 9},{ 6,10}},
      {{ 0, 4},{ 0, 8},{ 4, 8},{ 1, 5},{ 2, 9},{ 6,10}},
      {{ 0, 1},{ 0, 2},{ 6,10}},
      {{ 4, 8},{ 1, 4},{ 2, 8},{ 6,10}},
      {{ 4, 5},{ 2, 9},{ 4, 6}},
      {{ 0, 8},{ 0, 5},{ 2, 9},{ 6, 8}},
      {{ 0, 4},{ 0, 2},{ 4, 6}},
      {{ 2, 8},{ 6, 8}},
      {{ 8, 9},{ 8,10}},
      {{ 0, 4},{ 0, 9},{ 4,10}},
      {{ 0, 5},{ 0, 8},{ 8,10}},
      {{ 4, 5},{ 4,10}},
      {{ 4, 8},{ 1, 4},{ 8, 9}},
      {{ 0, 1},{ 0, 9}},
      {{ 1, 5},{ 0, 4},{ 0, 8},{ 4, 8}},
      {{ 1, 5}},
      {{ 1, 5},{ 8, 9},{ 8,10}},
      {{ 0, 4},{ 1, 5},{ 0, 9},{ 4,10}},
      {{ 0, 8},{ 0, 1},{ 8,10}},
      {{ 1, 4},{ 4,10}},
      {{ 4, 8},{ 4, 5},{ 8, 9}},
      {{ 0, 5},{ 0, 9}},
      {{ 0, 4},{ 0, 8},{ 4, 8}},
      {{-1,-1}},
      {{-1,-1}},
      {{ 0, 4},{ 0, 8},{ 4, 8}},
      {{ 0, 5},{ 0, 9}},
      {{ 4, 8},{ 4, 5},{ 8, 9}},
      {{ 1, 4},{ 4,10}},
      {{ 0, 8},{ 0, 1},{ 8,10}},
      {{ 0, 4},{ 1, 5},{ 0, 9},{ 4,10}},
      {{ 1, 5},{ 8, 9},{ 8,10}},
      {{ 1, 5}},
      {{ 1, 5},{ 0, 4},{ 0, 8},{ 4, 8}},
      {{ 0, 1},{ 0, 9}},
      {{ 4, 8},{ 1, 4},{ 8, 9}},
      {{ 4, 5},{ 4,10}},
      {{ 0, 5},{ 0, 8},{ 8,10}},
      {{ 0, 4},{ 0, 9},{ 4,10}},
      {{ 8, 9},{ 8,10}},
      {{ 2, 8},{ 6, 8}},
      {{ 0, 4},{ 0, 2},{ 4, 6}},
      {{ 0, 8},{ 0, 5},{ 2, 9},{ 6, 8}},
      {{ 4, 5},{ 2, 9},{ 4, 6}},
      {{ 4, 8},{ 1, 4},{ 2, 8},{ 6,10}},
      {{ 0, 1},{ 0, 2},{ 6,10}},
      {{ 0, 4},{ 0, 8},{ 4, 8},{ 1, 5},{ 2, 9},{ 6,10}},
      {{ 1, 5},{ 2, 9},{ 6,10}},
      {{ 1, 5},{ 2, 8},{ 6, 8}},
      {{ 0, 5},{ 0, 2},{ 1, 4},{ 4, 6}},
      {{ 0, 1},{ 0, 8},{ 2, 9},{ 6, 8}},
      {{ 2, 9},{ 1, 4},{ 4, 6}},
      {{ 4, 5},{ 4, 8},{ 2, 8},{ 6,10}},
      {{ 0, 5},{ 0, 2},{ 6,10}},
      {{ 0, 4},{ 0, 8},{ 4, 8},{ 2, 9},{ 6,10}},
      {{ 2, 9},{ 6,10}},
      {{ 2, 9}},
      {{ 0, 4},{ 0, 8},{ 4, 8},{ 2, 9}},
      {{ 0, 2},{ 0, 5}},
      {{ 2, 8},{ 4, 8},{ 4, 5}},
      {{ 4,10},{ 2, 9},{ 1, 4}},
      {{ 0, 1},{ 0, 9},{ 8,10},{ 2, 8}},
      {{ 0, 4},{ 0, 2},{ 1, 5},{ 4,10}},
      {{ 1, 5},{ 8,10},{ 2, 8}},
      {{ 1, 5},{ 2, 9}},
      {{ 1, 5},{ 0, 4},{ 0, 8},{ 4, 8},{ 2, 9}},
      {{ 0, 1},{ 0, 2}},
      {{ 2, 8},{ 4, 8},{ 1, 4}},
      {{ 4, 5},{ 2, 9},{ 4,10}},
      {{ 0, 5},{ 0, 8},{ 2, 8},{ 2, 9},{ 8,10},{ 2, 8}},
      {{ 0, 4},{ 0, 2},{ 4,10}},
      {{ 8,10},{ 2, 8}},
      {{ 8, 9},{ 6, 8}},
      {{ 0, 4},{ 0, 9},{ 4, 6}},
      {{ 0, 8},{ 0, 5},{ 6, 8}},
      {{ 4, 5},{ 4, 6}},
      {{ 4, 8},{ 1, 4},{ 8, 9},{ 6,10}},
      {{ 0, 1},{ 0, 9},{ 6,10}},
      {{ 0, 4},{ 0, 8},{ 4, 8},{ 1, 5},{ 6,10}},
      {{ 1, 5},{ 6,10}},
      {{ 1, 5},{ 8, 9},{ 6, 8}},
      {{ 0, 5},{ 0, 9},{ 1, 4},{ 4, 6}},
      {{ 0, 1},{ 0, 8},{ 6, 8}},
      {{ 1, 4},{ 4, 6}},
      {{ 4, 5},{ 4, 8},{ 8, 9},{ 6,10}},
      {{ 0, 5},{ 0, 9},{ 6,10}},
      {{ 0, 4},{ 0, 8},{ 4, 8},{ 6,10}},
      {{ 6,10}},
      {{ 6,10}},
      {{ 0, 4},{ 0, 8},{ 4, 8},{ 6,10}},
      {{ 0, 5},{ 6,10},{ 0, 9}},
      {{ 6, 8},{ 4,10},{ 4, 5},{ 8, 9}},
      {{ 1, 4},{ 4, 6}},
      {{ 0, 8},{ 0, 1},{ 6, 8}},
      {{ 0, 4},{ 1, 5},{ 0, 9},{ 4, 6}},
      {{ 1, 5},{ 8, 9},{ 6, 8}},
      {{ 1, 5},{ 6,10}},
      {{ 1, 5},{ 0, 4},{ 0, 8},{ 4, 8},{ 6,10}},
      {{ 0, 1},{ 0, 9},{ 6,10}},
      {{ 8, 9},{ 4, 8},{ 1, 4},{ 6,10}},
      {{ 4, 5},{ 4, 6}},
      {{ 0, 5},{ 0, 8},{ 6, 8}},
      {{ 0, 4},{ 0, 9},{ 4, 6}},
      {{ 8, 9},{ 6, 8}},
      {{ 2, 8},{ 8,10}},
      {{ 0, 4},{ 0, 2},{ 4,10}},
      {{ 0, 8},{ 0, 5},{ 2, 9},{ 8,10}},
      {{ 4, 5},{ 2, 9},{ 4,10}},
      {{ 4, 8},{ 1, 4},{ 2, 8}},
      {{ 0, 1},{ 0, 2}},
      {{ 0, 4},{ 0, 8},{ 4, 8},{ 1, 5},{ 2, 9}},
      {{ 1, 5},{ 2, 9}},
      {{ 1, 5},{ 2, 8},{ 8,10}},
      {{ 0, 5},{ 0, 2},{ 1, 4},{ 4,10}},
      {{ 0, 1},{ 0, 8},{ 2, 9},{ 8,10}},
      {{ 2, 9},{ 1, 4},{ 4,10}},
      {{ 4, 5},{ 4, 8},{ 2, 8}},
      {{ 0, 5},{ 0, 2}},
      {{ 0, 4},{ 0, 8},{ 4, 8},{ 2, 9}},
      {{ 2, 9}},
      {{ 2, 9},{ 6,10}},
      {{ 0, 4},{ 0, 8},{ 4, 8},{ 2, 9},{ 6,10}},
      {{ 0, 2},{ 0, 5},{ 6,10}},
      {{ 4, 5},{ 4, 8},{ 2, 8},{ 6,10}},
      {{ 1, 4},{ 2, 9},{ 4, 6}},
      {{ 2, 9},{ 6, 8},{ 0, 1},{ 0, 8}},
      {{ 0, 4},{ 0, 2},{ 1, 5},{ 4, 6}},
      {{ 1, 5},{ 2, 8},{ 6, 8}},
      {{ 1, 5},{ 2, 9},{ 6,10}},
      {{ 1, 5},{ 0, 4},{ 0, 8},{ 4, 8},{ 2, 9},{ 6,10}},
      {{ 0, 1},{ 0, 2},{ 6,10}},
      {{ 2, 8},{ 4, 8},{ 1, 4},{ 6,10}},
      {{ 4, 5},{ 2, 9},{ 4, 6}},
      {{ 0, 5},{ 0, 8},{ 6, 8},{ 2, 9}},
      {{ 0, 4},{ 0, 2},{ 4, 6}},
      {{ 2, 8},{ 6, 8}},
      {{ 8, 9},{ 8,10}},
      {{ 0, 4},{ 0, 9},{ 4,10}},
      {{ 0, 8},{ 0, 5},{ 8,10}},
      {{ 4, 5},{ 4,10}},
      {{ 4, 8},{ 1, 4},{ 8, 9}},
      {{ 0, 1},{ 0, 9}},
      {{ 0, 4},{ 0, 8},{ 4, 8},{ 1, 5}},
      {{ 1, 5}},
      {{ 1, 5},{ 8, 9},{ 8,10}},
      {{ 0, 5},{ 0, 9},{ 1, 4},{ 4,10}},
      {{ 0, 1},{ 0, 8},{ 8,10}},
      {{ 1, 4},{ 4,10}},
      {{ 4, 5},{ 4, 8},{ 8, 9}},
      {{ 0, 5},{ 0, 9}},
      {{ 0, 4},{ 0, 8},{ 4, 8}},
      {{-1,-1}}}};

   public static int[][][][] ig01;
      
   
   
   /** Creates a new instance of Edges */
   public IsoEdges()
   {
   }
   
}
