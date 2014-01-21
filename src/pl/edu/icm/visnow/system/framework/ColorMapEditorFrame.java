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

package pl.edu.icm.visnow.system.framework;

import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.event.ListSelectionEvent;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import pl.edu.icm.visnow.datamaps.ColorMapManager;
import pl.edu.icm.visnow.datamaps.colormap1d.DefaultColorMap1D;
import pl.edu.icm.visnow.datamaps.colormap1d.RGBChannelColorMap1D;
import pl.edu.icm.visnow.datamaps.colormap1d.RGBChannelColorMap1D.ColorKnot;
import pl.edu.icm.visnow.datamaps.utils.Orientation;
import pl.edu.icm.visnow.datamaps.widgets.ColorMapCellRenderer;
import pl.edu.icm.visnow.engine.error.Displayer;
import pl.edu.icm.visnow.system.main.VisNow;
import pl.edu.icm.visnow.system.swing.filechooser.VNFileChooser;

/**
 * @author  Michał Łyczek (lyczek@icm.edu.pl)
 * University of Warsaw, Interdisciplinary Centre
 * for Mathematical and Computational Modelling
 */
public class ColorMapEditorFrame extends javax.swing.JFrame {

    private RGBChannelColorMap1D backupColormap;

    public ColorMapEditorFrame() {
        initComponents();

        listColorMap1D.setModel(ColorMapManager.getInstance().getColorMap1DListModel());
        listColorMap1D.setCellRenderer(ColorMapCellRenderer.getInstance());
        colorKnotEditor.setShowColorKnot(true);
        colorKnotEditor.setShowRuler(true);
        colorKnotEditor.setPadding2(30);
        colorKnotEditor.setOrientation(Orientation.HORIZONTAL);
        listColorMap1D.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                int index = listColorMap1D.getSelectionModel().getAnchorSelectionIndex();
                if (index >= 0 && index < ColorMapManager.getInstance().getColorMap1DCount()) {
                    RGBChannelColorMap1D colorMap = (RGBChannelColorMap1D) ColorMapManager.getInstance().getColorMap1DListModel().getElementAt(index);
                    colorKnotEditor.setColorMap(colorMap);
                    backupColormap = new RGBChannelColorMap1D(colorMap);
                }
            }
        });

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel9 = new javax.swing.JPanel();
        colorKnotEditor = new pl.edu.icm.visnow.datamaps.widgets.ColorKnotEditor();
        jPanel11 = new javax.swing.JPanel();
        newButton = new javax.swing.JButton();
        jButton21 = new javax.swing.JButton();
        jButton22 = new javax.swing.JButton();
        jButton23 = new javax.swing.JButton();
        jButton20 = new javax.swing.JButton();
        newButton1 = new javax.swing.JButton();
        newButton2 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        listColorMap1D = new javax.swing.JList();
        jButton7 = new javax.swing.JButton();

        setTitle("Colormap Editor");
        setMinimumSize(new java.awt.Dimension(400, 300));

        jTabbedPane1.setMinimumSize(new java.awt.Dimension(300, 200));
        jTabbedPane1.setPreferredSize(new java.awt.Dimension(300, 200));

        jPanel9.setMinimumSize(new java.awt.Dimension(300, 200));
        jPanel9.setPreferredSize(new java.awt.Dimension(400, 252));
        jPanel9.setLayout(new java.awt.GridBagLayout());

        colorKnotEditor.setMinimumSize(new java.awt.Dimension(300, 51));
        colorKnotEditor.setPreferredSize(new java.awt.Dimension(300, 51));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel9.add(colorKnotEditor, gridBagConstraints);

        jPanel11.setFont(new java.awt.Font("Tahoma", 0, 8));
        jPanel11.setMinimumSize(new java.awt.Dimension(100, 92));
        jPanel11.setPreferredSize(new java.awt.Dimension(100, 92));
        jPanel11.setLayout(new java.awt.GridLayout(4, 2));

        newButton.setFont(new java.awt.Font("Tahoma", 0, 10));
        newButton.setText("new");
        newButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        newButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newButtonActionPerformed(evt);
            }
        });
        jPanel11.add(newButton);

        jButton21.setFont(new java.awt.Font("Tahoma", 0, 10));
        jButton21.setText("delete");
        jButton21.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });
        jPanel11.add(jButton21);

        jButton22.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jButton22.setText("export");
        jButton22.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton22ActionPerformed(evt);
            }
        });
        jPanel11.add(jButton22);

        jButton23.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jButton23.setText("import");
        jButton23.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton23ActionPerformed(evt);
            }
        });
        jPanel11.add(jButton23);

        jButton20.setFont(new java.awt.Font("Tahoma", 0, 10));
        jButton20.setText("rename");
        jButton20.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });
        jPanel11.add(jButton20);

        newButton1.setFont(new java.awt.Font("Tahoma", 0, 10));
        newButton1.setText("reset");
        newButton1.setMargin(new java.awt.Insets(2, 2, 2, 2));
        newButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newButton1ActionPerformed(evt);
            }
        });
        jPanel11.add(newButton1);

        newButton2.setFont(new java.awt.Font("Tahoma", 0, 10));
        newButton2.setText("duplicate");
        newButton2.setMargin(new java.awt.Insets(2, 2, 2, 2));
        newButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newButton2ActionPerformed(evt);
            }
        });
        jPanel11.add(newButton2);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        jPanel9.add(jPanel11, gridBagConstraints);

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

        jScrollPane3.setBorder(null);
        jScrollPane3.setMinimumSize(new java.awt.Dimension(217, 128));

        listColorMap1D.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        listColorMap1D.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                listColorMap1DValueChanged(evt);
            }
        });
        jScrollPane3.setViewportView(listColorMap1D);

        jPanel1.add(jScrollPane3);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel9.add(jPanel1, gridBagConstraints);

        jTabbedPane1.addTab("1d Colormaps", jPanel9);

        jButton7.setText("OK");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton7)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // <editor-fold defaultstate="collapsed" desc="Actions">
private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
    this.setVisible(false);
    VisNow.get().getMainConfig().saveColorMaps();
}//GEN-LAST:event_jButton7ActionPerformed

private void listColorMap1DValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_listColorMap1DValueChanged
    // TODO add your handling code here:
}//GEN-LAST:event_listColorMap1DValueChanged

private void newButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newButtonActionPerformed

    String newName = JOptionPane.showInputDialog("Colormap name:");
    if (newName != null) {

        DefaultColorMap1D cm = new RGBChannelColorMap1D(newName, false);
        ColorMapManager.getInstance().registerColorMap(cm);

    }

}//GEN-LAST:event_newButtonActionPerformed

private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed

    RGBChannelColorMap1D colormap = (RGBChannelColorMap1D) listColorMap1D.getSelectedValue();
    String newName = JOptionPane.showInputDialog("Colormap name:", colormap.getName());
    if (newName != null) {

        colormap.setName(newName);
        listColorMap1D.repaint();

    }


}//GEN-LAST:event_jButton20ActionPerformed

private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed

    for (Object o : listColorMap1D.getSelectedValues()) {
        if (o instanceof RGBChannelColorMap1D) {
            RGBChannelColorMap1D colormap = (RGBChannelColorMap1D) o;
            if (!colormap.isBuildin()) {
                ColorMapManager.getInstance().unregisterColorMap(colormap);
            }
        }
    }

}//GEN-LAST:event_jButton21ActionPerformed

private void newButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newButton1ActionPerformed

    RGBChannelColorMap1D colormap = (RGBChannelColorMap1D) listColorMap1D.getSelectedValue();
    colormap.set(backupColormap);

}//GEN-LAST:event_newButton1ActionPerformed

private void newButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newButton2ActionPerformed

    RGBChannelColorMap1D colormap = (RGBChannelColorMap1D) listColorMap1D.getSelectedValue();
    if (colormap != null) {
        DefaultColorMap1D cm = new RGBChannelColorMap1D(colormap);
        cm.setName("Copy of " + colormap.getName());
        cm.setBuildin(false);
        ColorMapManager.getInstance().registerColorMap(cm);
    }
}//GEN-LAST:event_newButton2ActionPerformed

    private void jButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton22ActionPerformed
        export_colormaps();
    }//GEN-LAST:event_jButton22ActionPerformed

    private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23ActionPerformed
        import_colormaps();
    }//GEN-LAST:event_jButton23ActionPerformed
    // </editor-fold>

    private boolean isCorrectChars(String name) {
        return name.matches("[- &+_a-zA-Z0-9]+");
    }

    private boolean isCorrectNewName(String name) {
//        MainColorMaps model = VisNow.getMainColorMaps();
//
//        if (!isCorrectChars(name)) {
//            return false;
//        }
//        for (int i = 0; i < model.getSize(); i++) {
//            pl.edu.icm.visnow.datamaps.old.colormap1d.ColorMap cm = (pl.edu.icm.visnow.datamaps.old.colormap1d.ColorMap) model.getElementAt(i);
//            if (cm.getName().equals(name)) {
//                return false;
//            }
//        }
        return true;
    }

        JFileChooser chooser = new JFileChooser( VisNow.get().getMainConfig().getAdditionalConfigPath() );
		FileNameExtensionFilter cmapFilter = new FileNameExtensionFilter("Color map (*.vnc)", "vnc", "VNC");
    public void export_colormaps() {
		chooser.setFileFilter(cmapFilter);
        int option = chooser.showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            if (chooser.getSelectedFile() != null) {
                try {
                    File file = VNFileChooser.fileWithExtensionAddedIfNecessary(chooser.getSelectedFile(), cmapFilter);
                    file.createNewFile();
                    FileWriter writer = new FileWriter(file);
                    writer.write("<colormaps>");

                    for (Object o : listColorMap1D.getSelectedValues()) {
                        if (o instanceof RGBChannelColorMap1D) {
                            RGBChannelColorMap1D colormap = (RGBChannelColorMap1D) o;
                            writer.write(String.format("<colormap name=\"%s\" type=\"rgbchannel\">\n", colormap.getName()));
                            for (ColorKnot ck : colormap.getColorKnots()) {
                                Color c = new Color(ck.getColor());
                                writer.write(String.format("<color position=\"%f\" r=\"%d\" g=\"%d\" b=\"%d\" />\n", ck.getPosition(), c.getRed(), c.getGreen(), c.getBlue()));
                            }
                            writer.write("</colormap>\n");
                        }
                    }
                    writer.write("</colormaps>");
                    writer.close();
                } catch (IOException ex) {
                    Logger.getLogger(ColorMapEditorFrame.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }

    }

    public void import_colormaps() {

//        JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(cmapFilter);
        int option = chooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            if (chooser.getSelectedFile() != null) {
                try {
                    File file = chooser.getSelectedFile();
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    Document doc = db.parse(file);

                    Element colormaps = doc.getDocumentElement();
                    NodeList colormaps_nodes = colormaps.getElementsByTagName("colormap");
                    for (int i = 0; i < colormaps_nodes.getLength(); i++) {
                        Element colormap_el = (Element) colormaps_nodes.item(i);

                        String name = colormap_el.getAttribute("name");
                        String type = colormap_el.getAttribute("type");

                        if (type.equals("rgbchannel")) {
                            NodeList colors_nodes = colormap_el.getElementsByTagName("color");
                            float[] pos = new float[colors_nodes.getLength()];
                            Color[] colors = new Color[colors_nodes.getLength()];
                            for (int j = 0; j < colors_nodes.getLength(); j++) {
                                Element color_el = (Element) colors_nodes.item(j);
                                pos[j] = Float.valueOf(color_el.getAttribute("position"));
                                colors[j] = new Color(Integer.valueOf(color_el.getAttribute("r")), Integer.valueOf(color_el.getAttribute("g")), Integer.valueOf(color_el.getAttribute("b")));
                            }
                            RGBChannelColorMap1D colormap = new RGBChannelColorMap1D(name, false, pos, colors);
                            ColorMapManager.getInstance().registerColorMap(colormap);
                        }
                    }
                } catch (Exception ex) {
                    Displayer.ddisplay(200907311202L, ex, this, "Cannot read colormap file.");
                }
            }
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private pl.edu.icm.visnow.datamaps.widgets.ColorKnotEditor colorKnotEditor;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JList listColorMap1D;
    private javax.swing.JButton newButton;
    private javax.swing.JButton newButton1;
    private javax.swing.JButton newButton2;
    // End of variables declaration//GEN-END:variables
}