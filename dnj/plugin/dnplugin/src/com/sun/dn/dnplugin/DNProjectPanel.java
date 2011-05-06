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


package com.sun.dn.DNPlugin;

import org.openide.util.actions.*;
import org.netbeans.api.project.ui.*;
import org.netbeans.api.project.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.awt.Component;
import java.awt.event.*;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;
import com.sun.dn.gui.ImportPanel;


/**
 *
 * @author Danny  Coward
 */
public class DNProjectPanel implements WizardDescriptor.FinishablePanel {  
     /** Registered ChangeListeners */
    private List changeListeners;
    private boolean isValid;
    private ImportPanel importPanel;

    public Component getComponent() {
        return getImportPanel();
    }
    
    public ImportPanel getImportPanel() {
        if (importPanel == null) {
            importPanel = new ImportPanel(System.getProperty("user.home") + "/dnj/");
        }
        return importPanel;
    }
    
    public String getErrorMessage() {
        return "DNProject Panel Error message";
    }
    
    public boolean isFinishPanel() {
        return true;
    }
   
    /**
     * Return message to be displayed as ErrorMessage by Eclipse importer
     * wizard. Default implementation returns null (no error message will be
     * displayed)
     */
    public void addChangeListener(ChangeListener l) { 
        if (changeListeners == null) {
            changeListeners = new ArrayList(2);
        }
        changeListeners.add(l);
    }
    
    public void removeChangeListener(ChangeListener l) { 
        if (changeListeners != null) {
            if (changeListeners.remove(l) && changeListeners.isEmpty()) {
                changeListeners = null;
            }
        }
    }
    
    public boolean isValid() {
        return true;
    }

    
    public HelpCtx getHelp() {
        return null;
    }
    
    public void storeSettings(Object settings) {;}
    
    public void readSettings(Object settings) {;}
}