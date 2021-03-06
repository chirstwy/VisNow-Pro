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

package pl.edu.icm.visnow.application.frames.tabs;

import java.awt.Point;
import pl.edu.icm.visnow.application.frames.*;
import java.util.Collection;
import java.util.HashMap;
import javax.swing.DefaultComboBoxModel;
import pl.edu.icm.visnow.engine.commands.Command;
import pl.edu.icm.visnow.engine.commands.SelectedModuleCommand;
import pl.edu.icm.visnow.engine.main.ModuleBox;
import pl.edu.icm.visnow.system.swing.VNSwingUtils;

/**
 *
 * @author gacek
 */
public class MPortsPanel extends javax.swing.JPanel {

    //<editor-fold defaultstate="collapsed" desc=" [VAR] Frames ">
    protected Frames frames;
    public Frames getFrames() {return frames;}
    //</editor-fold>

//    private javax.swing.JTree tree;

    private HashMap<String, InfoPanel> panels;
    public InfoPanel getPanel(String name) {
        if(!panels.containsKey(name)) {
            panels.put(name, new InfoPanel(frames.getApplication().getEngine().getModule(name)));
        }
        return panels.get(name);
    }

    /** Creates new form GUIPanel */
    public MPortsPanel(Frames frames) {
        this.frames = frames;
        initComponents();
        refreshModules();
        panels = new HashMap<String, InfoPanel>();
        enableScrollBar();
    }


    public void enableScrollBar() {
        this.scrollPane.getVerticalScrollBar().setEnabled(true);
        this.scrollPane.getVerticalScrollBar().setMaximum(10000);
    }





    

    

    //<editor-fold defaultstate="collapsed" desc=" Generated code ">
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        comboBox = new javax.swing.JComboBox();
        scrollPane = new javax.swing.JScrollPane();

        setLayout(new java.awt.BorderLayout());

        comboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxActionPerformed(evt);
            }
        });
        add(comboBox, java.awt.BorderLayout.NORTH);

        scrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    //</editor-fold>


    //<editor-fold defaultstate="collapsed" desc=" Actions ">
    public boolean selectModule(String name) {
        if(name == null) {
            scrollPane.setViewportView(null);
            if(comboBox.getItemCount()>0)
                comboBox.setSelectedIndex(comboBox.getItemCount()-1);            
        } else {
            comboBox.setSelectedItem(name);
            scrollPane.setViewportView(getPanel(name));
            getPanel(name).setParentScrollPane(scrollPane);
            VNSwingUtils.setConstantWidth(getPanel(name), scrollPane.getViewport().getWidth());//this.getWidth());
            enableScrollBar();
            repaint();
        }

        return true;
    }

    private void comboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxActionPerformed
        if(comboBox.getItemCount() > 0 && comboBox.getSelectedIndex() == comboBox.getItemCount()-1)
            return;
        
        if(getFrames().isSelectingEnabled()) {
            getFrames().getApplication().getReceiver().receive(
                    new SelectedModuleCommand(Command.UI_FRAME_SELECTED_MODULE, (String)comboBox.getSelectedItem())
                    );
        }
    }//GEN-LAST:event_comboBoxActionPerformed
    
    public void refreshModules() {
        Collection<ModuleBox> modules = getFrames().getApplication().getEngine().getModules().values();
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        for(ModuleBox module: modules) {
            model.addElement(module.getName());
        }
        model.addElement("");
        comboBox.setModel(model);

    }

    //</editor-fold>


    //<editor-fold defaultstate="collapsed" desc=" Generated variables ">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox comboBox;
    private javax.swing.JScrollPane scrollPane;
    // End of variables declaration//GEN-END:variables
    //</editor-fold>


}
