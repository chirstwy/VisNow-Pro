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

package pl.edu.icm.visnow.gui.widgets;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;
import java.util.Vector;
import pl.edu.icm.visnow.gui.events.DoubleValueModificationEvent;
import pl.edu.icm.visnow.gui.events.DoubleValueModificationListener;

/**
 *
 * @author Krzysztof S. Nowinski, University of Warsaw, ICM
 */
public class DoubleSlider extends javax.swing.JPanel implements Serializable
{

   private double minX = -1.;
   private double valX = 0.;
   private double maxX = 1.;
   private double val0X = 0.;
   private double minY = -1.;
   private double valY = 0.;
   private double maxY = 1.;
   private double val0Y = 0.;
   private boolean isAdjusting = false;
   
   private Vector<DoubleValueModificationListener> listeners =
           new Vector<DoubleValueModificationListener>();
   private int x0, y0, xc, yc, xcl, ycl, x, y, xl, yl;

   /**
    * Creates new form DoubleSlider
    */
   public DoubleSlider()
   {
      initComponents();
   }

   /**
    * This method is called from within the constructor to initialize the form.
    * WARNING: Do NOT modify this code. The content of this method is always
    * regenerated by the Form Editor.
    */
   // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
   private void initComponents()
   {

      setLayout(new java.awt.BorderLayout());

      setBackground(new java.awt.Color(237, 238, 255));
      setBorder(new javax.swing.border.EtchedBorder());
      addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {

          @Override
          public void mouseDragged(java.awt.event.MouseEvent evt) {
              formMouseDragged(evt);
          }
      });
      addMouseListener(new java.awt.event.MouseAdapter() {

          @Override
          public void mouseClicked(java.awt.event.MouseEvent evt) {
              formMouseClicked(evt);
          }

          @Override
          public void mousePressed(java.awt.event.MouseEvent evt) {
              formMousePressed(evt);
          }

          @Override
          public void mouseReleased(java.awt.event.MouseEvent evt) {
              formMouseReleased(evt);
          }
      });

   }
   // </editor-fold>//GEN-END:initComponents

   private void formMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_formMouseClicked
   {//GEN-HEADEREND:event_formMouseClicked
      x = evt.getX();
      y = evt.getY();
      if (x0 - 7 < x && x < x0 + 7 && y0 - 7 < y && y < y0 + 7)
      {
         xc = xcl = x = xl = x0;
         yc = ycl = yl = y = getHeight() - y0;
         valX = val0X;
         valY = val0Y;

      } else
      {
         valX += (maxX - minX) * (x - xc) / 2000.;
         valY += (maxY - minY) * (getHeight() - yc - y) / 2000.;

      }
      fireDoubleValueModificationEvent();
   }//GEN-LAST:event_formMouseClicked

   private void formMouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_formMouseReleased
   {//GEN-HEADEREND:event_formMouseReleased
      isAdjusting = false;
   }//GEN-LAST:event_formMouseReleased

   private void formMousePressed(java.awt.event.MouseEvent evt)//GEN-FIRST:event_formMousePressed
   {//GEN-HEADEREND:event_formMousePressed
      isAdjusting = true;
      xl = x = evt.getX();
      yl = y = getHeight() - evt.getY();
      xcl = xc;
      ycl = yc;
   }//GEN-LAST:event_formMousePressed

   private void formMouseDragged(java.awt.event.MouseEvent evt)//GEN-FIRST:event_formMouseDragged
   {//GEN-HEADEREND:event_formMouseDragged
      x = evt.getX();
      y = getHeight() - evt.getY();
      xc = xcl + x - xl;
      yc = ycl + y - yl;
      valX = (xc * (maxX - minX)) / getWidth() + minX;
      if (valX < this.minX)
         valX = this.minX;
      if (valX > this.maxX)
         valX = this.maxX;
      valY = (yc * (maxY - minY)) / getHeight() + minY;
      if (valY < this.minY)
         valY = this.minY;
      if (valY > this.maxY)
         valY = this.maxY;
      fireDoubleValueModificationEvent();
   }//GEN-LAST:event_formMouseDragged

   // Variables declaration - do not modify//GEN-BEGIN:variables
   // End of variables declaration//GEN-END:variables
   /**
    * Getter for property minX.
    *
    * @return Value of property minX.
    */
   public double getMinX()
   {

      return this.minX;
   }

   /**
    * Setter for property minX.
    *
    * @param minX New value of property minX.
    */
   public void setMinX(double minX)
   {
      if (minX >= this.maxX)
         minX = this.maxX - 1.;
      this.minX = minX;
      if (this.valX < this.minX)
      {
         this.valX = this.minX;
         fireDoubleValueModificationEvent();
      }
   }

   /**
    * Getter for property valX.
    *
    * @return Value of property valX.
    */
   public double getValX()
   {

      return this.valX;
   }

   /**
    * Setter for property valX.
    *
    * @param valX New value of property valX.
    */
   public void setValX(double valX)
   {
      if (valX < this.minX)
         valX = this.minX;
      if (valX > this.maxX)
         valX = this.maxX;
      this.valX = valX;
      fireDoubleValueModificationEvent();
   }

   /**
    * Getter for property maxX.
    *
    * @return Value of property maxX.
    */
   public double getMaxX()
   {
      return this.maxX;
   }

   /**
    * Setter for property maxX.
    *
    * @param maxX New value of property maxX.
    */
   public void setMaxX(double maxX)
   {
      if (maxX <= this.minX)
         maxX = this.minX + 1.;
      this.maxX = maxX;
      if (this.valX > this.valX)
      {
         this.valX = this.maxX;
         fireDoubleValueModificationEvent();
      }
   }

   /**
    * Getter for property minY.
    *
    * @return Value of property minY.
    */
   public double getMinY()
   {
      return this.minY;
   }

   /**
    * Setter for property minY.
    *
    * @param minY New value of property minY.
    */
   public void setMinY(double minY)
   {
      if (minY >= this.maxY)
         minY = this.maxY - 1.;
      this.minY = minY;
      if (this.valY < this.minY)
      {
         this.valY = this.minY;
         fireDoubleValueModificationEvent();
      }
   }

   /**
    * Getter for property valY.
    *
    * @return Value of property valY.
    */
   public double getValY()
   {
      return this.valY;
   }

   /**
    * Setter for property valY.
    *
    * @param valY New value of property valY.
    */
   public void setValY(double valY)
   {
      if (valY < this.minY)
         valY = this.minY;
      if (valY > this.maxY)
         valY = this.maxY;
      this.valY = valY;
      fireDoubleValueModificationEvent();
   }

   /**
    * Getter for property maxY.
    *
    * @return Value of property maxY.
    */
   public double getMaxY()
   {

      return this.maxY;
   }

   /**
    * Setter for property maxY.
    *
    * @param maxY New value of property maxY.
    */
   public void setMaxY(double maxY)
   {
      if (maxY < this.minY)
         maxY = this.minY + 1.;
      this.maxY = maxY;
      if (valY > this.maxY)
      {
         this.valY = this.maxY;
         fireDoubleValueModificationEvent();
      }
   }

   /**
    * Getter for property isAdjusting.
    *
    * @return Value of property isAdjusting.
    */
   public boolean isAdjusting()
   {

      return this.isAdjusting;
   }

   /**
    * Setter for property isAdjusting.
    *
    * @param isAdjusting New value of property isAdjusting.
    */
   public void setAdjusting(boolean isAdjusting)
   {

      this.isAdjusting = isAdjusting;
   }

   /**
    * Registers DoubleValueModificationListener to receive events.
    *
    * @param listener The listener to register.
    */
   public synchronized void addDoubleValueModificationListener(DoubleValueModificationListener listener)
   {
      listeners.add(listener);
   }

   /**
    * Removes DoubleValueModificationListener from the list of listeners.
    *
    * @param listener The listener to remove.
    */
   public synchronized void removeDoubleValueModificationListener(DoubleValueModificationListener listener)
   {
      listeners.remove(listener);
   }

   public synchronized void fireDoubleValueModificationEvent()
   {
      repaint();
      DoubleValueModificationEvent evt = new DoubleValueModificationEvent(this, valX, valY);
      for (DoubleValueModificationListener l : listeners)
         l.doubleValueChanged(evt);
   }

   /**
    * Getter for property val0X.
    *
    * @return Value of property val0X.
    */
   public double getVal0X()
   {
      return this.val0X;
   }

   /**
    * Setter for property val0X.
    *
    * @param val0X New value of property val0X.
    */
   public void setVal0X(double val0X)
   {
      if (val0X < this.minX)
         val0X = this.minX;
      if (val0X > this.maxX)
         val0X = this.maxX;
      this.val0X = val0X;
      xc = x0 = (int) (getWidth() * (val0X - minX) / (maxX - minX));
      repaint();
   }

   /**
    * Getter for property val0Y.
    *
    * @return Value of property val0Y.
    */
   public double getVal0Y()
   {
      return this.val0Y;
   }

   /**
    * Setter for property val0Y.
    *
    * @param val0Y New value of property val0Y.
    */
   public void setVal0Y(double val0Y)
   {
      if (val0Y < this.minY)
         val0Y = this.minY;
      if (val0Y > this.maxY)
         val0Y = this.maxY;
      this.val0Y = val0Y;
      yc = y0 = (int) (getHeight() * (val0Y - minY) / (maxY - minY));
      repaint();
   }

   @Override
   public void paint(Graphics g)
   {
      x0 = (int) (getWidth() * (val0X - minX) / (maxX - minX));
      y0 = (int) (getHeight() * (val0Y - minY) / (maxY - minY));
      xc = (int) (getWidth() * (valX - minX) / (maxX - minX));
      yc = (int) (getHeight() * (valY - minY) / (maxY - minY));
      g.setColor(getBackground());
      g.fillRect(0, 0, getWidth(), getHeight());
      g.setColor(Color.GRAY);
      g.drawLine(x0 - 10, getHeight() - y0, x0 + 10, getHeight() - y0);
      g.drawLine(x0, getHeight() - y0 - 10, x0, getHeight() - y0 + 10);
      g.setColor(Color.BLACK);
      g.drawLine(xc - 5, getHeight() - yc, xc + 5, getHeight() - yc);
      g.drawLine(xc, getHeight() - yc - 5, xc, getHeight() - yc + 5);
   }
}
