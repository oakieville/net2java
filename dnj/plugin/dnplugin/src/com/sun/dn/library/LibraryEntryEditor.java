
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

import com.sun.dn.Interpreter;
import com.sun.dn.java.JavaProgram;
import com.sun.dn.util.Debug;
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
 class LibraryEntryEditor extends JPanel implements ChangeListener {
	
        LibraryChangeListener lee;
        String libraryPath;
        LibraryData library;
        JPanel mainDisplayPanel;
        
        JTextField libraryFileField = new JTextField("                         ");
        JTabbedPane typeDisplay;
        MemberMappingEditor mmp;
        TypeMappingEditor tmp;
        EventDefinitionEditor ede;
        JButton saveButton = new JButton("Save Changes");
        JButton cancelButton = new JButton("Discard Changes");
        
        Entry entry;
        private boolean entryChanged = false;
	static Dimension BORDER = new Dimension(10, 10);
		
	public LibraryEntryEditor (LibraryChangeListener lee, String defaultDirectory, LibraryData library) {
            this.lee = lee;
            this.libraryPath = defaultDirectory;
            	
            this.library = library;
            this.init();
            
    }
        
        public void stateChanged(ChangeEvent ce) {
            setChanged(true);
        }

        public void setChanged(boolean changed) {
            this.entryChanged = changed;
            this.saveButton.setEnabled(this.entryChanged);
            this.cancelButton.setEnabled(this.entryChanged);
        }
        
      

	private void init() {
            this.setLayout(new BorderLayout());
            
            JPanel p = new JPanel();
            
            JLabel entryNameLbl = new JLabel("Filename");
            p.add(entryNameLbl);
            libraryFileField.setEnabled(false);
            p.add(libraryFileField);
           
            JPanel pp = new JPanel();
            pp.setLayout(new BorderLayout());
            pp.add(p, BorderLayout.WEST);
            pp.add(Box.createRigidArea(BORDER), BorderLayout.NORTH);
           // this.add(pp, BorderLayout.NORTH); // don't need the library filename any more
            
            saveButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    doSave();
                }
            });
            cancelButton.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent ae) {
                   doCancel();
               } 
            });
            
            JPanel okCancelp = new JPanel();
            okCancelp.setLayout(new BorderLayout());
            JPanel lp = new JPanel();
            lp.add(saveButton);
            lp.add(cancelButton);
            okCancelp.add(lp, BorderLayout.EAST);
            this.add(okCancelp, BorderLayout.SOUTH);
            
            typeDisplay = new JTabbedPane();
            mmp = new MemberMappingEditor(this);
            tmp = new TypeMappingEditor(this);
            
            typeDisplay.add("Type Mapping", tmp);
            typeDisplay.add("Member Mapping", mmp);
            
            ede = new EventDefinitionEditor(this);
            
            this.mainDisplayPanel = new JPanel();
            this.mainDisplayPanel.setLayout(new GridLayout(1,1));
            
            this.add(mainDisplayPanel, BorderLayout.CENTER);
            
	}
        
        public void setDisplay(JComponent p) {
            this.mainDisplayPanel.removeAll();
            this.mainDisplayPanel.add(p);
        }
        
        
        
       

	public void refreshGuiComponents() {
           String displayString = this.entry.getFile().getName();
           if (this.entryChanged) {
               displayString = displayString + " (needs saving)";
           }
           this.libraryFileField.setText(displayString);
	}
        
        public void setEntry(Entry entry) {
             this.entry = entry;
             if (entry.isEvent()) {
                 ede.setEntry(entry);
                 this.setDisplay(ede);
             } else {
                mmp.setEntry(entry);
                tmp.setEntry(entry);
                this.setDisplay(typeDisplay);
             }
             this.refreshGuiComponents();
        }
        
        public boolean canOpenNewEntry() {
           if (this.entryChanged) {
            int ret = JOptionPane.showConfirmDialog(this, "Save before continuing ?", "Entry unsaved" , JOptionPane.YES_NO_CANCEL_OPTION);
                if (ret == JOptionPane.CANCEL_OPTION) {
                    return false;
                } else if (ret == JOptionPane.NO_OPTION) {
                    return true;
                } else if (ret == JOptionPane.YES_OPTION) {
                    this.doSave();
                    return true;
                } else {
                    throw new RuntimeException("Impossible");
                }
            } else {
                return true;
            }
        }
        
        public void doCancel() {
            this.openEntry(this.entry.getFile());
        }
        
        public void openEntry(File f) {
            Entry e = new Entry(f, this.library);
            this.setEntry(e);  
        }
        
        public void deleteMe() {
            int ret = JOptionPane.showConfirmDialog(this, "Really delete " + this.entry.getFile() + " ?", "Delete Library Entry", JOptionPane.YES_NO_OPTION);
            if (ret == JOptionPane.YES_OPTION) {
                this.entry.getFile().delete();
                lee.filesChanged(null);
            }
        }
        
        public void doSave() {
            if (this.entry == null) {
                return ;
            }
            this.mmp.save();
            try {
                File f = entry.getFile();
                
                FileOutputStream fos = new FileOutputStream(f);
                this.entry.write(fos);
                fos.close();
                entryChanged = false;
            } catch (Throwable t) {
                 t.printStackTrace();
                 JOptionPane.showMessageDialog(this, "Error saving " + this.entry);
            }
        }
        
       
                
          
    
}

 