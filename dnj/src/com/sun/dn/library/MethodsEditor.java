
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

import com.sun.dn.parser.Signature;
import com.sun.dn.util.Util;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import javax.swing.event.*;
/**
 * GUI for editing the method translations on a .NET class.
 * @author Danny  Coward
 */
class MethodsEditor extends JPanel {
	JList methodsList = null;
        JTextField signatureField = new JTextField();
        JTextField javaExpressionField = new JTextField();
        JTextField importField = new JTextField();
        JButton deleteSigButton = new JButton("Remove");
        JButton newSigButton = new JButton("Add");
        Entry entry = null;
        Signature signature = null;
        ChangeListener cl;
        
	 MethodsEditor(ChangeListener cl) {
            JPanel p = null;
            JPanel pp = null;
            this.cl = cl;
            		
            this.setLayout(new BorderLayout());
           
	    
            
            
            String[] dummy = {};
            this.methodsList = new JList(dummy );
            
            this.methodsList.setCellRenderer(new SignatureRenderer(this.methodsList.getCellRenderer()));
            
            JPanel leftp = new JPanel();
            leftp.setLayout(new BorderLayout());
            JLabel lbl = new JLabel("Method signatures");
            JScrollPane flsp = new JScrollPane(methodsList);
            leftp.add(lbl, BorderLayout.NORTH);
            leftp.add(flsp, BorderLayout.CENTER);
            JPanel buttonsPanel = new JPanel();
            buttonsPanel.add(newSigButton);
            buttonsPanel.add(deleteSigButton);
            leftp.add(buttonsPanel, BorderLayout.SOUTH);
            
            this.add(leftp, BorderLayout.WEST);
            
            pp = new JPanel();
            pp.setLayout(new BorderLayout());
            JPanel fieldsPanel = new JPanel();
            fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
            fieldsPanel.add(new JLabel("Method Signature (VB form)"));
            fieldsPanel.add(this.signatureField);
            fieldsPanel.add(new JLabel("Java Expression"));
            //this.setFixedHeight(javaExpressionField);
            fieldsPanel.add(javaExpressionField);
            fieldsPanel.add(Box.createRigidArea(Util.BORDER));
            fieldsPanel.add(new JLabel("Import statement"));
            fieldsPanel.add(importField);
            fieldsPanel.add(Box.createRigidArea(new java.awt.Dimension(10, 35)));
            
            pp.add(fieldsPanel, BorderLayout.SOUTH);
            this.add(pp, BorderLayout.CENTER);
            hookupGui();
            refreshMethodComponents();
	}
         
         private void hookupGui() {
             newSigButton.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent ae) {
                   doAddNewSignature();
               } 
            });
             this.deleteSigButton.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent ae) {
                   doDeleteSignature();
               } 
            });
             
             this.methodsList.addListSelectionListener(new ListSelectionListener() {
                    
               public void valueChanged(ListSelectionEvent e) {
                   doChangeMethod();
               }     
                    
                    
         });
         }
         
         void changed() {
             cl.stateChanged(new ChangeEvent(this));
         }
         
         public void save() {
              if (this.signature != null) {
                JavaExpression je = (JavaExpression) this.entry.getMethodSigToJavaExpressionMap().get(this.signature);
                JavaExpression  newJE = new JavaExpression(this.javaExpressionField.getText(), this.getImportStringsAsList(), this.signature);
                this.entry.getMethodSigToJavaExpressionMap().remove(this.signature);
                this.entry.getMethodSigToJavaExpressionMap().put(this.signature, newJE);
                
                 try {
                    this.signature.parseVB(this.signatureField.getText());
                } catch (Throwable t) {
                    t.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error parsing signature as a VisualBasic string");
                }
              }
         }
         
        private void setFixedHeight(JComponent c) {
            Dimension d = c.getPreferredSize();
            c.setMaximumSize(new Dimension(1000, (int)d.getWidth()));
            c.setMinimumSize(new Dimension(200, (int)d.getWidth()));
        }
        
        void doAddNewSignature() {
            Signature s = new Signature("Public Function NewMethod1() As String");
            JavaExpression je = new JavaExpression("${this}.newMethod1(${arg0})", new ArrayList(), s);
            this.entry.getMethodSigToJavaExpressionMap().put(s, je);
            this.signature = s;
            this.changed();
            this.refreshGuiComponents();
         
        }
        
      
        
        void doDeleteSignature() {
            this.entry.getMethodSigToJavaExpressionMap().remove(this.signature);
            this.changed();
            this.refreshGuiComponents();
        }
         
        void setEntry(Entry e) {
            this.entry = e;
            this.signature = null;
            this.refreshGuiComponents();
        }
        
        public void doChangeMethod() {
            Signature s = (Signature) this.methodsList.getSelectedValue();
            this.signature = s;
            this.refreshMethodComponents();
        }
        
        public void refreshMethodComponents() {
            this.signatureField.setEnabled(this.signature != null);
            this.javaExpressionField.setEnabled(this.signature != null);
            this.importField.setEnabled(this.signature != null);
            if (signature != null) {
                signatureField.setText(this.signature.getOriginalCode());
                JavaExpression je = (JavaExpression) this.entry.getMethodSigToJavaExpressionMap().get(this.signature);
                javaExpressionField.setText(je.getExpression());
                String importString = je.getImportStrings().toString().substring(1, je.getImportStrings().toString().length()-1);
                this.importField.setText(importString);
            } else {
                 signatureField.setText("");
                 javaExpressionField.setText("");
                 this.importField.setText("");
                
            }
            
        
        }
        
        private List getImportStringsAsList() {
            List l = new ArrayList();
            String s = this.importField.getText();
            StringTokenizer st = new StringTokenizer(s, ",");
            while (st.hasMoreTokens()) {
                l.add(st.nextToken().trim());
            }
            return l;
        }
        public void refreshGuiComponents() {
            if (this.entry != null) {
                DefaultListModel dlm = new DefaultListModel();
                Map methodsMap = this.entry.getMethodSigToJavaExpressionMap();
                TreeSet ss = new TreeSet(new SignatureComparator());
                
                ss.addAll(methodsMap.keySet());
                for (Iterator itr = ss.iterator(); itr.hasNext();) {
                    Signature s = (Signature) itr.next();
                    dlm.addElement(s);
                }
                this.methodsList.setModel(dlm);
            } else {
                ((DefaultListModel) methodsList.getModel()).removeAllElements();
            }
            //if (this.signature != null) {
                //System.out.println("sign non null");
                this.methodsList.setSelectedValue(this.signature, true);
            //}
            this.refreshMethodComponents();
            
            
            
        }

}


class SignatureRenderer extends JLabel implements ListCellRenderer {
    private ListCellRenderer cr;
     public SignatureRenderer(ListCellRenderer cr) {
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
         if (c instanceof JLabel && value instanceof Signature) {
             Signature sig = (Signature) value;
             String text = sig.getName() + "  (" + sig.getArgs().size() + " args)";
             ((JLabel) c).setText(text);
         }return c;
     }
 }

class SignatureComparator implements java.util.Comparator {
    
    public int compare(Object o1,
                   Object o2) {
        String n1 = ((Signature)o1).getName();
        String n2 =  ((Signature)o2).getName();
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


 