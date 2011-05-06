
 /* 
 * Copyright (c) 2007 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *  
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *  
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *  
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *  
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */
package com.sun.dn.library;

import com.sun.dn.util.Util;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.Color;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import javax.swing.event.*;
/**
 * GUI for Editing the Properties on a .NET class
 * @author Danny  Coward
 */
class PropertiesEditor extends JPanel {
	private Entry entry = null;
        JList propertiesList;
        JTextField nameField = new JTextField();
        PropertyTranslation propertyTranslation = null;
        JTextField javaExpressionField = new JTextField();
        JTextField importField = new JTextField();
        JTextField dnTypeNameField = new JTextField();

        JButton newGetButton = new JButton("Add Get");
        JButton newSetButton = new JButton("Add Set");
        JButton deleteButton = new JButton("Remove");
        ChangeListener cl;
	        
	 PropertiesEditor (ChangeListener cl) {
            JPanel p = null;
            JPanel pp = null;
            this.cl = cl;
            		
            this.setLayout(new BorderLayout());
            
            String[] dummy = {};
            this.propertiesList = new JList(dummy );
            this.propertiesList.setCellRenderer(new PropertyRenderer(propertiesList.getCellRenderer()));
            
            p = new JPanel();
            p.setLayout(new BorderLayout());
            JLabel lbl = new JLabel("Property Names");
            JScrollPane flsp = new JScrollPane(propertiesList);
            
            JPanel buttonsPanel = new JPanel();
            buttonsPanel.add(newGetButton);
            buttonsPanel.add(newSetButton);
            buttonsPanel.add(deleteButton);
            
            p.add(lbl, BorderLayout.NORTH);
            p.add(flsp, BorderLayout.CENTER);
            p.add(buttonsPanel, BorderLayout.SOUTH);
            this.add(Box.createRigidArea(Util.BORDER));
            this.add(p, BorderLayout.WEST);
            
            pp = new JPanel();
            pp.setLayout(new BorderLayout());
            JPanel fieldsPanel = new JPanel();
            fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
            fieldsPanel.add(new JLabel("Property Name"));
            fieldsPanel.add(this.nameField);
            fieldsPanel.add(Box.createRigidArea(Util.BORDER));
            fieldsPanel.add(new JLabel(".NET Typename"));
            fieldsPanel.add(dnTypeNameField);
            fieldsPanel.add(Box.createRigidArea(Util.BORDER));
            fieldsPanel.add(new JLabel("Java Expression"));
            fieldsPanel.add(javaExpressionField);
            fieldsPanel.add(Box.createRigidArea(Util.BORDER));
            fieldsPanel.add(new JLabel("Import statement"));
            fieldsPanel.add(importField);
            fieldsPanel.add(Box.createRigidArea(new java.awt.Dimension(10, 35)));
            
            pp.add(fieldsPanel, BorderLayout.SOUTH);
            
            this.add(pp, BorderLayout.CENTER);
            this.hookupGui();
            this.refreshPropertyComponents();
         }
         
         private void setFixedWidth(Component c) {
             //Dimension d = new Dimension(50, c.getPreferredSize().height);
             Dimension d = new Dimension(50,50);
             c.setMaximumSize(d);
             c.setPreferredSize(d);
             //c.setMinimumSize(d);
         }
         
         private void hookupGui() {
              newGetButton.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent ae) {
                   addProperty("get");
               } 
            });
             this.propertiesList.addListSelectionListener(new ListSelectionListener() {     
               public void valueChanged(ListSelectionEvent e) {
                   doChangeProperty();
               }     
                    
                    
         });
            
            deleteButton.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent ae) {
                   removeProperty();
               } 
            });
            
            newSetButton.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent ae) {
                   addProperty("set");
               } 
            });
         }
         
         void changed() {
             cl.stateChanged(new ChangeEvent(this));
         }
         
         void doChangePropertyName() {
             if (this.propertyTranslation != null) {
                 String newName = this.nameField.getText();
                 String oldName = this.propertyTranslation.getName();
                 String type = (String) this.entry.getPropertyTypes().get(oldName);
                 this.entry.getPropertyTypes().remove(oldName);
                 this.entry.getPropertyTypes().put(newName, type);
                 
                 for (Iterator itr = this.entry.getPropertyTranslations().iterator(); itr.hasNext();) {
                     PropertyTranslation pt = (PropertyTranslation) itr.next();
                     if (pt.getName().equals(oldName)) {
                         pt.setName(newName);
                     }
                 }  
             }
             
         }
         
         public void save() {
             if (this.propertyTranslation != null) {
                 doChangePropertyName();
                 propertyTranslation.setJava(this.javaExpressionField.getText());
                 propertyTranslation.setImports(importField.getText());
                 String name = this.propertyTranslation.getName();
                 this.entry.getPropertyTypes().put(name, this.dnTypeNameField.getText());
                 
                 
             }
         }
         
         void addProperty(String getOrSet) {
             String name = "NewProperty1";
             PropertyTranslation pt = new PropertyTranslation(name, getOrSet, "${this}.setNewProperty1(${value})", "");
             this.entry.getPropertyTranslations().add(pt);
             //System.out.println(this.entry);
             String type = (String) entry.getPropertyTypes().get(pt.getName());
             if (type == null) {
                 entry.getPropertyTypes().put(name, "type1");
             }
             this.propertyTranslation = pt;
             this.changed();
             refreshGuiComponents();   
         }
         
         private boolean containsName(String name) {
             for (Iterator itr = this.entry.getPropertyTranslations().iterator(); itr.hasNext();) {
                 PropertyTranslation pt = (PropertyTranslation) itr.next();
                 if (pt.getName().equals(name)) {
                     return true;
                 }
             }
             return false;
         }
         
         void removeProperty() {
             if (this.propertyTranslation != null) {
                 String name = propertyTranslation.getName();
                 this.entry.getPropertyTranslations().remove(this.propertyTranslation);
                 if (!this.containsName(name)) {
                     //System.out.println("Removing name " + name);
                     this.entry.getPropertyTypes().remove(name);
                 }
                 this.changed();
                 refreshGuiComponents();
             }
         }
         
         void doChangeProperty() {
             PropertyTranslation pt = (PropertyTranslation) propertiesList.getSelectedValue();
             this.propertyTranslation = pt;
             this.refreshPropertyComponents();
         }
         
         public void refreshPropertyComponents() {
             this.nameField.setEnabled(this.propertyTranslation != null);
             this.dnTypeNameField.setEnabled(this.propertyTranslation != null);
             this.javaExpressionField.setEnabled(this.propertyTranslation != null);
             this.importField.setEnabled(this.propertyTranslation != null);
             if (this.propertyTranslation != null) {
                 this.nameField.setText(propertyTranslation.getName());
                 String type = (String) entry.getPropertyTypes().get(propertyTranslation.getName());
                 this.dnTypeNameField.setText(type);
                 this.javaExpressionField.setText(propertyTranslation.getJava());
                 String impS = propertyTranslation.getImports().toString();
                 this.importField.setText(impS.substring(1, impS.length() -1));
             } else {
                 this.nameField.setText("");
                 this.javaExpressionField.setText("");
                 this.dnTypeNameField.setText("");
                 this.importField.setText("");
             }
             
         }
         
                
        void setEntry(Entry e) {
            this.entry = e;
            this.refreshGuiComponents();
        }
        
               
        public void refreshGuiComponents() {
            DefaultListModel dlm = new DefaultListModel();
            if (this.entry != null) {
                TreeSet ts = new TreeSet(new PropertyTranslationComparator());
               List l = this.entry.getPropertyTranslations(); 
               ts.addAll(l);
               for (Iterator itr = ts.iterator(); itr.hasNext();) {
                   PropertyTranslation pt = (PropertyTranslation) itr.next();
                   dlm.addElement(pt);
               }
            } 
            this.propertiesList.setModel(dlm);
            this.propertiesList.setSelectedValue(this.propertyTranslation, true);
        }
        
        private void setFixedHeight(JComponent c) {
            Dimension d = c.getPreferredSize();
            c.setMaximumSize(new Dimension(1000, (int)d.getWidth()));
            c.setMinimumSize(new Dimension(200, (int)d.getWidth()));
        }

}

class PropertyRenderer extends JLabel implements ListCellRenderer {
    private ListCellRenderer cr;
     public PropertyRenderer(ListCellRenderer cr) {
         this.cr = cr;
         setOpaque(true);
     }
     public Component getListCellRendererComponent(
         JList list,
         Object value,
         int index,
         boolean isSelected,
         boolean cellHasFocus)
     {
         Component c = cr.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
         //System.out.println(c);
         if (c instanceof JLabel) {
             //System.out.println("hurrah");
             if (value instanceof PropertyTranslation) {
                PropertyTranslation pt = (PropertyTranslation) value;
                String text = pt.getDisplayString();
                ((JLabel)c).setText(text);
             }
         }
         
         return c;
     }
 }

class PropertyTranslationComparator implements java.util.Comparator {
    
    public int compare(Object o1,
                   Object o2) {
        String n1 = ((PropertyTranslation)o1).getName();
        String n2 =  ((PropertyTranslation)o2).getName();
        TreeSet ts = new TreeSet();
        ts.add(n1);
        ts.add(n2);
        if ( n1.equals(ts.first())){
            return -1;
        } else {
            return +1;
        }
    }
    
    public boolean equals(Object o1) {
        return false;
    }
}

 