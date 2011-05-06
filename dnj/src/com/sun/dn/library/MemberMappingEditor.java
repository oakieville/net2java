
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
 class MemberMappingEditor extends JPanel implements ChangeListener {
       
        MethodsEditor methodsPanel;
        EventsEditor eventsPanel;
        PropertiesEditor propertiesPanel;
        ChangeListener clist;
        Entry entry;
        private boolean changed = false;
	static Dimension BORDER = new Dimension(10, 10);
		
	public MemberMappingEditor (ChangeListener clist) {
            this.clist = clist;
            this.init();
           
    }
        
        public void stateChanged(ChangeEvent ce) {
            setChanged(true);
        }

        private MethodsEditor getMethodsPanel() {
            if (this.methodsPanel != null) {
                return this.methodsPanel;
            } else {
                this.methodsPanel = new MethodsEditor(this);
            } 
            return methodsPanel;
        }
        
         private EventsEditor getEventsPanel() {
            if (this.eventsPanel != null) {
                return this.eventsPanel;
            } else {
                this.eventsPanel = new EventsEditor(this);
            } 
            return eventsPanel;
        }
        
        private PropertiesEditor getPropertiesPanel() {
            if (this.propertiesPanel != null) {
                return this.propertiesPanel;
            } else {
                this.propertiesPanel = new PropertiesEditor(this);
            }
            return propertiesPanel;
        }
        
        public void save() {
            this.getMethodsPanel().save();
            this.getPropertiesPanel().save();
            this.getEventsPanel().save();
        }
	
        
        private void setChanged(boolean changed) {
            this.changed = true;
        }
        
       

	private void init() {
            JTabbedPane jt = new JTabbedPane();
            jt.add(this.getPropertiesPanel(), "Properties");
            jt.add(this.getMethodsPanel(), "Methods");
            jt.add(this.getEventsPanel(), "Events");
           
            this.setLayout(new GridLayout(1, 1));
            this.add(jt);
	}
        
        

	public void refreshGuiComponents() {
            if (entry != null) {
                
            } else {
                
            }
            this.changed = false;
	}
        
        public void setEntry(Entry entry) {
             this.entry = entry;
             this.getPropertiesPanel().setEntry(entry);
             this.getMethodsPanel().setEntry(entry);
             this.getEventsPanel().setEntry(entry);
             this.refreshGuiComponents();
        }
        
}
 