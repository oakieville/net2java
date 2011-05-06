
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
import java.awt.*;
import java.awt.event.*;
import javax.swing.filechooser.*;
import java.util.*;
import java.io.*;
import java.beans.*;
import javax.swing.event.*;
/**
 * Gui for editing Library entries, aka .NET class to Java translations.
 * @author Danny  Coward
 */
 class TypeMappingEditor extends JPanel implements ChangeListener {
	JButton browseButton = new JButton("Open");
        
        JTextField dnTypenameField = new JTextField("Type name");
        JTextField aliasField = new JTextField("");
        JTextField dnSuperTypenameField = new JTextField("Super name");
        JList implementsList = new JList();
        JButton addImplementsB = new JButton("Add...");
        JButton removeImplementsB = new JButton("Remove");
        JTextField javaClassnameField = new JTextField("Java name");
        JCheckBox isRuntimeCheckBox = new JCheckBox("Runtime class");
        JRadioButton classRB = new JRadioButton("Class");
        JRadioButton structureRB = new JRadioButton("Structure");
        JRadioButton interfaceRB = new JRadioButton("Interface");
        JRadioButton enumerationRB = new JRadioButton("Enumeration");
        JRadioButton delegateRB = new JRadioButton("Delegate");
        
        Entry entry;
        private boolean changed = false;
	static Dimension BORDER = new Dimension(10, 10);
        LibraryEntryEditor letmp;
		
	public TypeMappingEditor (LibraryEntryEditor letmp) {
            this.init();
            this.hookupGui();
            this.letmp = letmp;
    }
        
        public void stateChanged(ChangeEvent ce) {
            //setChanged(true);
        }

	private JPanel getTypeEditPanel() {
            
                Vector labels = new Vector();
                JLabel lbl = null;
		JPanel p = null;
		JPanel pp = null;
		JPanel typeEditPanel = new JPanel();
		
		typeEditPanel.setLayout(new BoxLayout(typeEditPanel, BoxLayout.X_AXIS));
		typeEditPanel.add(Box.createRigidArea(BORDER));
		
		JPanel cp = new JPanel();
		cp.setLayout(new BoxLayout(cp, BoxLayout.Y_AXIS));
		typeEditPanel.add(cp);
		typeEditPanel.add(Box.createRigidArea(BORDER));
                
                p = new JPanel();
		lbl = new JLabel(".NET Type name");
                labels.add(lbl);
		p.setLayout(new BorderLayout());
		p.add(lbl, BorderLayout.WEST);
		p.add(this.dnTypenameField, BorderLayout.CENTER);
		
		cp.add(Box.createRigidArea(BORDER));
		cp.add(p);
                
                p = new JPanel();
		lbl = new JLabel("Alias name");
                labels.add(lbl);
		p.setLayout(new BorderLayout());
		p.add(lbl, BorderLayout.WEST);
		p.add(this.aliasField, BorderLayout.CENTER);
               
                 p = new JPanel();
		lbl = new JLabel("Super Type name");
                labels.add(lbl);
		p.setLayout(new BorderLayout());
		p.add(lbl, BorderLayout.WEST);
		p.add(this.dnSuperTypenameField, BorderLayout.CENTER);
		
		
                cp.add(Box.createRigidArea(BORDER));
		cp.add(p);
                
                JPanel typesAndLabelPanel = new JPanel();
                typesAndLabelPanel.setLayout(new BorderLayout());
                
                lbl = new JLabel("Type:");
                labels.add(lbl);
                typesAndLabelPanel.add(lbl, BorderLayout.WEST);
                
                JPanel typesPanel = new JPanel();
                //typesPanel.setBackground(Color.YELLOW);
                typesPanel.setLayout(new BoxLayout(typesPanel, BoxLayout.Y_AXIS));
                
                // class checkbox and implements list
                JPanel classCBAndImplementsListPanel = new JPanel();
                
                pp = new JPanel();
		classCBAndImplementsListPanel.setLayout(new BorderLayout());
		classCBAndImplementsListPanel.add(new JLabel("Interfaces implemented:"), BorderLayout.NORTH);
                //classCBAndImplementsListPanel.add(Box.createRigidArea(new Dimension(20,10)), BorderLayout.WEST);
		
                // implements list and buttons
                pp = new JPanel();
                pp.setLayout(new BorderLayout());
                JScrollPane jsp = new JScrollPane(implementsList);
                pp.add(jsp, BorderLayout.CENTER);
                JPanel btnP = new JPanel();
                btnP.setLayout(new BoxLayout(btnP, BoxLayout.Y_AXIS));
                btnP.add(addImplementsB);
                btnP.add(Box.createRigidArea(BORDER));
                btnP.add(removeImplementsB);
                
                Vector btnsSizer = new Vector();
                btnsSizer.add(addImplementsB);
                btnsSizer.add(removeImplementsB);
                Util.normaliseComponents(btnsSizer);
                
                pp.add(btnP, BorderLayout.EAST);
                classCBAndImplementsListPanel.add(pp, BorderLayout.CENTER);
		
		typesPanel.add(classCBAndImplementsListPanel);
                
                ButtonGroup bg = new ButtonGroup();
		bg.add(classRB);
		bg.add(interfaceRB);
                bg.add(enumerationRB);
                bg.add(delegateRB);
                bg.add(structureRB);
                
                JPanel otherCBPanel = new JPanel();
                otherCBPanel.setLayout(new BorderLayout());
                otherCBPanel.add(classRB, BorderLayout.WEST);
                typesPanel.add(otherCBPanel);
                
                otherCBPanel = new JPanel();
                otherCBPanel.setLayout(new BorderLayout());
                otherCBPanel.add(structureRB, BorderLayout.WEST);
                typesPanel.add(otherCBPanel);
                
                otherCBPanel = new JPanel();
                otherCBPanel.setLayout(new BorderLayout());
                otherCBPanel.add(interfaceRB, BorderLayout.WEST);
                typesPanel.add(otherCBPanel);
                
                otherCBPanel = new JPanel();
                otherCBPanel.setLayout(new BorderLayout());
                otherCBPanel.add(enumerationRB, BorderLayout.WEST);
                typesPanel.add(otherCBPanel);
                
                otherCBPanel = new JPanel();
                otherCBPanel.setLayout(new BorderLayout());
                otherCBPanel.add(delegateRB, BorderLayout.WEST);
                typesPanel.add(otherCBPanel);
          
                typesAndLabelPanel.add(typesPanel, BorderLayout.CENTER);
                
                cp.add(Box.createRigidArea(BORDER));
                cp.add(typesAndLabelPanel);
        
               
                
                p = new JPanel();
		lbl = new JLabel("is runtime class");
		p.setLayout(new BorderLayout());
                cp.add(p);
                
                p = new JPanel();
		lbl = new JLabel("Java class name");
                labels.add(lbl);
		p.setLayout(new BorderLayout());
		p.add(lbl, BorderLayout.WEST);
		p.add(this.javaClassnameField, BorderLayout.CENTER);
		cp.add(p); 
                Util.normaliseComponents(labels);
                return typeEditPanel;
	}
        
        public void hookupGui() {
            this.addImplementsB.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent ae) {
                   addImplements();
               } 
            });
            this.removeImplementsB.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent ae) {
                   removeImplements();
               } 
            });
            dnTypenameField.getDocument().addDocumentListener(new MyTextListener() {
                public void changed() {
                   
                    entry.setDNTypename(dnTypenameField.getText());
                    setChanged(true);
                }
            });
            dnSuperTypenameField.getDocument().addDocumentListener(new MyTextListener() {
                public void changed() {
                    entry.setDNInheritsTypename(dnSuperTypenameField.getText());
                    setChanged(true);
                }
            });
     
            aliasField.getDocument().addDocumentListener(new MyTextListener() {
                    
                    public void changed() {
                        entry.setAliasName(aliasField.getText());
                        setChanged(true);
                    }
                }
                );
            isRuntimeCheckBox.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent ae) {
                       entry.setRuntime(isRuntimeCheckBox.isSelected());
                       setChanged(true);
                      
                   } 
                });
            javaClassnameField.getDocument().addDocumentListener(new MyTextListener() {
                    public void changed() {
                        entry.setJavaClassname(javaClassnameField.getText());
                        setChanged(true);
                    }
                }); 
            classRB.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        doChangeType(); 
                    }
                });
                structureRB.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        doChangeType(); 
                    }
                });
                interfaceRB.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        doChangeType(); 
                    }
                });
                enumerationRB.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        doChangeType(); 
                    }
                });
                 delegateRB.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        doChangeType(); 
                    }
                });
        }
        
        private void setChanged(boolean changed) {
            //this.changed = changed;
            //this.letmp.setChanged(changed);
        }
        
        public void addImplements() {
            String value = JOptionPane.showInputDialog( this, "Enter the full .NET Interface name", "Add Interface", JOptionPane.PLAIN_MESSAGE);
            if (value != null && !"".equals(value.trim())) {
                this.entry.addImplementsType(value);
                setChanged(true);
            }
            this.refreshGuiComponents();
            
        }
        
        public void removeImplements() {
            String value = (String) this.implementsList.getSelectedValue();
            if (value != null) {
                this.entry.removeImplementsType(value);
                this.setChanged(true);
            }
            this.refreshGuiComponents();
        }
        
        

	private void init() {
            this.setLayout(new GridLayout(1, 1));
            this.add(this.getTypeEditPanel());
		
	}
        
        public void doChangeType() {
            
            if (classRB.isSelected()) {
                this.entry.setType(Entry.CLASS_TYPE); 
                
            }
            if (interfaceRB.isSelected()) {
                this.entry.setType(Entry.INTERFACE_TYPE); 
                
            }
            if (delegateRB.isSelected()) {
                this.entry.setType(Entry.DELEGATE_TYPE);  
                
            }
            if (enumerationRB.isSelected()) {
                this.entry.setType(Entry.ENUMERATION_TYPE);
            } 
            if (this.structureRB.isSelected()) {
                this.entry.setType(Entry.STRUCTURE_TYPE);
            }
            this.setChanged(true);
            
            this.refreshGuiComponents();
            
            
        }

	public void refreshGuiComponents() {
            if (entry != null) {
                this.dnTypenameField.setText(entry.getDNTypename());
                this.aliasField.setText(entry.getAliasName());
                this.dnSuperTypenameField.setText(entry.getDNInheritsTypename());
                this.javaClassnameField.setText(entry.getJavaClassname());
                isRuntimeCheckBox.setSelected(entry.isRuntime());
                this.classRB.setSelected(entry.isClass());
                this.structureRB.setSelected(entry.isStructure());
                this.enumerationRB.setSelected(entry.isEnumeration());
                this.interfaceRB.setSelected(entry.isInterface());
                this.delegateRB.setSelected(entry.isDelegate());
                DefaultListModel dlm = new DefaultListModel();
                for (Iterator itr = this.entry.getImplementsList().iterator(); itr.hasNext();) {
                    String nextName = (String) itr.next();
                    dlm.addElement(nextName);
                }
                this.implementsList.setModel(dlm);
                boolean enableImplements = entry.isClass() || entry.isDelegate() || entry.isStructure();
                this.implementsList.setEnabled(enableImplements);
                this.addImplementsB.setEnabled(enableImplements);
                this.removeImplementsB.setEnabled(enableImplements);
            } else {
                this.dnTypenameField.setText("");
                this.aliasField.setText("");
                this.dnSuperTypenameField.setText("");
                this.javaClassnameField.setText("");
                isRuntimeCheckBox.setSelected(false);
                structureRB.setSelected(false);
                this.classRB.setSelected(false);
                this.enumerationRB.setSelected(false);
                this.interfaceRB.setSelected(false);
                this.delegateRB.setSelected(false);
                this.implementsList.setModel(new DefaultListModel());
                this.implementsList.setEnabled(false);
                this.addImplementsB.setEnabled(false);
                this.removeImplementsB.setEnabled(false);
            }
	}
        
        public void setEntry(Entry entry) {
             this.entry = entry;
             this.refreshGuiComponents();
             this.setChanged(false);
        }
        
            
}

 