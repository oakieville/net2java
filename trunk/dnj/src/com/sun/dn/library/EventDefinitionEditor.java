
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
 class EventDefinitionEditor extends JPanel implements ChangeListener {
	JTextField dnTypenameTf = new JTextField("");
        JTextField delegateTypenameTF = new JTextField("");
        
        Entry entry;
        private boolean changed = false;
	static Dimension BORDER = new Dimension(10, 10);
        LibraryEntryEditor letmp;
		
	public EventDefinitionEditor (LibraryEntryEditor leep) {
            this.init();
            this.hookupGui();
            //setBackground(Color.BLUE);
    }
        
        public void stateChanged(ChangeEvent ce) {
            //setChanged(true);
        }
        
        public void hookupGui() {
            dnTypenameTf.getDocument().addDocumentListener(new MyTextListener() {
                public void changed() {
                    entry.setEventName(dnTypenameTf.getText());
                }
            });
            this.delegateTypenameTF.getDocument().addDocumentListener(new MyTextListener() {
                public void changed() {
                    entry.setDelegateTypename(delegateTypenameTF.getText());
                }
            });
            
        }
        
        private void setChanged(boolean changed) {
           
        }
        
        
        

	private void init() {
            this.setLayout(new BorderLayout());
            this.add(Box.createRigidArea(new Dimension(20, 20)), BorderLayout.NORTH);
            //this.add(Box.createRigidArea(BORDER), BorderLayout.NORTH);
            this.add(Box.createRigidArea(BORDER), BorderLayout.EAST);
            this.add(Box.createRigidArea(BORDER), BorderLayout.WEST);
            
            JPanel squashPanel = new JPanel();
            squashPanel.setLayout(new BorderLayout());
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            
            Vector labels = new Vector();
            JPanel p = null;
            JLabel lbl;
            
            p = new JPanel();
            p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
            lbl = new JLabel("Event .NET Typename");
            labels.add(lbl);
            p.add(lbl);
            p.add(this.dnTypenameTf);
            mainPanel.add(p);
            mainPanel.add(Box.createRigidArea((BORDER)));
            
            p = new JPanel();
            p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
            lbl = new JLabel("Event Delegate .NET Typename");
            labels.add(lbl);
            p.add(lbl);
            p.add(this.delegateTypenameTF);
            mainPanel.add(p);
            mainPanel.add(Box.createRigidArea((BORDER)));
            
            
            mainPanel.add(Box.createRigidArea((BORDER)));
            Util.normaliseComponents(labels);
            squashPanel.add(mainPanel, BorderLayout.SOUTH);
            
            this.add(squashPanel, BorderLayout.CENTER);
	}
        
        

	public void refreshGuiComponents() {
            this.delegateTypenameTF.setEnabled(this.entry != null);
            this.dnTypenameTf.setEnabled(this.entry != null);
            if (this.entry != null) {
                this.dnTypenameTf.setText(entry.getEvent().getName());
                this.delegateTypenameTF.setText(this.entry.getDelegateTypename());
            } else {
                 this.dnTypenameTf.setText("");
                 this.delegateTypenameTF.setText("");
                 
            }
            
	}
        
        public void setEntry(Entry entry) {
             this.entry = entry;
             this.refreshGuiComponents();
             this.setChanged(false);
        }
        
            
}

 