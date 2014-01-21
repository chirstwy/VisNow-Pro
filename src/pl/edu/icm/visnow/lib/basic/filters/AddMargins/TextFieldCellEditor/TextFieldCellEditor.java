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

package pl.edu.icm.visnow.lib.basic.filters.AddMargins.TextFieldCellEditor;

import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;

/**
 *
 * @author Szymon Jaranowski (s.jaranowski@icm.edu.pl), Warsaw University, ICM
 */

/* The solution described in post http://stackoverflow.com/questions/1652942/can-a-jtable-save-data-whenever-a-cell-loses-focus */

public class TextFieldCellEditor extends DefaultCellEditor {
    TextFieldCell textField;    // an instance of edit field
    Class<?> columnClass;       // specifies cell type class
    Object valueObject;         // for storing correct value before editing
    public TextFieldCellEditor(TextFieldCell tf, Class<?> cc) {
        super(tf);
        textField = tf;
        columnClass = cc;
        valueObject = null;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        TextFieldCell tf = (TextFieldCell)super.getTableCellEditorComponent(table, value, isSelected, row, column);
        if (value != null) {
            tf.setText(value.toString());
        }
        // we have to save current value to restore it on another cell selection
        // if edited value couldn't be parsed to this cell's type
        valueObject = value;
        return tf;
    }

    @Override
    public Object getCellEditorValue() {
        try {
            // converting edited value to specified cell's type
            if (columnClass.equals(Double.class))
                return Double.parseDouble(textField.getText());
            else if (columnClass.equals(Float.class))
                return Float.parseFloat(textField.getText());
            else if (columnClass.equals(Integer.class))
                return Integer.parseInt(textField.getText());
            else if (columnClass.equals(Byte.class))
                return Byte.parseByte(textField.getText());
            else if (columnClass.equals(String.class))
                return textField.getText();
        }
        catch (NumberFormatException ex) {

        }

        // this handles restoring cell's value on jumping to another cell
        if (valueObject != null) {
            if (valueObject instanceof Double)
                return ((Double)valueObject).doubleValue();
            else if (valueObject instanceof Float)
                return ((Float)valueObject).floatValue();
            else if (valueObject instanceof Integer)
                return ((Integer)valueObject).intValue();
            else if (valueObject instanceof Byte)
                return ((Byte)valueObject).byteValue();
            else if (valueObject instanceof String)
                return (String)valueObject;
        }

        return null;
    }
}