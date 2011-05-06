
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
 * GUI for Editing the list of Events on a .NET class.
 * @author Danny  Coward
 */
class EventsEditor extends JPanel {
	private Entry entry = null;
        private EventNameType ent = null;
        ChangeListener cl;
        JList eventsList = new JList();
        JTextField eventNameField = new JTextField();
        JTextField eventTypeField = new JTextField();
        JTextField javaListenerInterfaceNameField = new JTextField();
        JTextField eventListenerAddMethodNameField = new JTextField();
        JTextField listenerInvokeMethodNameField = new JTextField();
        JButton newEventButton = new JButton("Add");
        JButton deleteEventButton = new JButton("Remove");
       	        
	 EventsEditor (ChangeListener cl) {
            JPanel p = null;
            JPanel pp = null;
            this.cl = cl;
            		
            this.setLayout(new BorderLayout());
            
            String[] dummy = {};
            this.eventsList = new JList(dummy );
           
            this.eventsList.setCellRenderer(new EventRenderer(this.eventsList.getCellRenderer()));
            p = new JPanel();
            p.setLayout(new BorderLayout());
            JLabel lbl = new JLabel("Events");
            JScrollPane flsp = new JScrollPane(eventsList);
            p.add(lbl, BorderLayout.NORTH);
            p.add(flsp, BorderLayout.CENTER);
            pp = new JPanel();
            
            pp.add(newEventButton);
            pp.add(deleteEventButton);
            p.add(pp, BorderLayout.SOUTH);
            
            this.add(p, BorderLayout.WEST);
            
            pp = new JPanel();
            pp.setLayout(new BorderLayout());
            JPanel fieldsPanel = new JPanel();
            fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
            fieldsPanel.add(new JLabel("Event Name (e.g. 'Load')"));
            fieldsPanel.add(this.eventNameField);
            fieldsPanel.add(new JLabel("Event .NET type (e.g. 'System.Windows.Forms.Load')"));
            fieldsPanel.add(eventTypeField);
            fieldsPanel.add(Box.createRigidArea(Util.BORDER));  
            fieldsPanel.add(new JLabel("Java listener interface name (e.g. 'java.awt.event.WindowListener')"));
            fieldsPanel.add(javaListenerInterfaceNameField);
            fieldsPanel.add(Box.createRigidArea(Util.BORDER));  
            fieldsPanel.add(new JLabel("Java addListener Method name (e.g. 'addWindowListener')"));
            fieldsPanel.add(eventListenerAddMethodNameField);
            fieldsPanel.add(Box.createRigidArea(Util.BORDER)); 
            fieldsPanel.add(new JLabel("Listener invoke method name (e.g. 'windowOpened')"));
            fieldsPanel.add(listenerInvokeMethodNameField);
            fieldsPanel.add(Box.createRigidArea(new java.awt.Dimension(10, 35)));
            pp.add(fieldsPanel, BorderLayout.SOUTH);
            this.add(pp, BorderLayout.CENTER);
            this.hookupGui();
            refreshEventComponents();
         }
         
         private void hookupGui() {
              this.eventsList.addListSelectionListener(new ListSelectionListener() {
                    
               public void valueChanged(ListSelectionEvent e) {
                   doChangeEvent();
               }     
      
         });    
            deleteEventButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    doDeleteEvent();
                }
            });
            newEventButton.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent ae) {
                   doAddNewEvent();
               } 
            });
         }
         
         void changed() {
             cl.stateChanged(new ChangeEvent(this));
         }
         
         
         public void refreshEventComponents() {
             this.eventNameField.setEnabled(this.ent != null);
             this.eventTypeField.setEnabled(this.ent != null);
             this.eventListenerAddMethodNameField.setEnabled(this.ent != null);
             this.listenerInvokeMethodNameField.setEnabled(this.ent != null);
             this.listenerInvokeMethodNameField.setEnabled(this.ent != null);
             this.javaListenerInterfaceNameField.setEnabled(this.ent != null);
             
             if (this.ent != null) {
                 this.eventNameField.setText(ent.getName());
                 this.eventTypeField.setText(ent.getType());
                 String eventListenerAddMethodName = (String) this.entry.getListenerAddMethods().get(this.ent);
                 this.eventListenerAddMethodNameField.setText(eventListenerAddMethodName);
                 String listenerInvokeMethodName = (String) this.entry.getListenMethods().get(this.ent);
                 if (listenerInvokeMethodName != null) {
                    this.listenerInvokeMethodNameField.setText(listenerInvokeMethodName);
                 } else {
                     listenerInvokeMethodNameField.setText("");
                 }
                 String javaListenerInterfaceName = (String) this.entry.getListenerJavaInterfaces().get(this.ent);
                 if (javaListenerInterfaceName != null) {
                    this.javaListenerInterfaceNameField.setText(javaListenerInterfaceName);
                 } else {
                     javaListenerInterfaceNameField.setText("");
                 }
                 
             } else {
                 this.eventNameField.setText("");
                 this.eventTypeField.setText("");
                 this.eventListenerAddMethodNameField.setText("");
                 this.listenerInvokeMethodNameField.setText("");
                 this.javaListenerInterfaceNameField.setText("");
             }
             
       
            
             
         }
         
         public void doChangeEvent() {
             this.ent = ((EventNameType) this.eventsList.getSelectedValue());
             this.refreshEventComponents();
         }
    
         public void doAddNewEvent() {
             EventNameType ent = new EventNameType("StuffHappened", "System.Foo.StuffHappenedEventType");
             String addListenerMethodName = "addJavaXXListener";
             this.entry.getListenerAddMethods().put(ent, addListenerMethodName);
             this.entry.getListenMethods().put(ent, "listenerInvokeMethodName");
             this.entry.getListenerJavaInterfaces().put(ent, "java.awt.event.FooListener");
             this.ent = ent;
             this.changed();
             this.refreshGuiComponents();
             this.eventsList.setSelectedValue(this.ent, true);
             
         }
         
         public void doDeleteEvent() {
             this.entry.getListenerAddMethods().remove(this.ent);
             this.entry.getListenMethods().remove(this.ent);
             this.changed();
             this.refreshGuiComponents();
         }
         
                
        void setEntry(Entry e) {
            this.entry = e;
            this.refreshGuiComponents();
        }
        
         public void save() {
             if (this.ent != null) {
                 this.ent.setName(this.eventNameField.getText()); 
                 this.ent.setType(this.eventTypeField.getText());
                 this.entry.getListenerAddMethods().remove(this.ent);
                 this.entry.getListenerAddMethods().put(this.ent, this.eventListenerAddMethodNameField.getText());
                 String jlin = this.javaListenerInterfaceNameField.getText();
                 if (jlin != null  && !("".equals(jlin))) {
                     this.entry.getListenerJavaInterfaces().remove(this.ent);
                     this.entry.getListenerJavaInterfaces().put(this.ent, jlin);
                 } 
                 String nLIName = this.listenerInvokeMethodNameField.getText();
                 if (nLIName != null  &&
                         !("".equals(nLIName))) {
                     this.entry.getListenMethods().remove(this.ent);
                     this.entry.getListenMethods().put(this.ent, nLIName);
                 } 
             }
         }
               
        public void refreshGuiComponents() {
            if (this.entry != null) {
                DefaultListModel dlm = new DefaultListModel();
                Map methodsMap = this.entry.getListenerAddMethods();
                TreeSet ts = new TreeSet(new EventNameTypeComparator());
                ts.addAll(methodsMap.keySet());
                for (Iterator itr = ts.iterator(); itr.hasNext();) {
                    EventNameType ent = (EventNameType) itr.next();
                    dlm.addElement(ent);
                }
                this.eventsList.setModel(dlm);
            } else {
                ((DefaultListModel) eventsList.getModel()).removeAllElements();
            }
        }
        
        

}


class EventRenderer extends JLabel implements ListCellRenderer {
    private ListCellRenderer cr;
     public EventRenderer(ListCellRenderer cr) {
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
         if (c instanceof JLabel && value instanceof EventNameType) {
             EventNameType ent = (EventNameType) value;
             String text = ent.getName();
             ((JLabel) c).setText(text);
         } 
         return c;
     }
 }

class EventNameTypeComparator implements java.util.Comparator {
    
    public int compare(Object o1,
                   Object o2) {
        String n1 = ((EventNameType)o1).getName();
        String n2 =  ((EventNameType)o2).getName();
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


 