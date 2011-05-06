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

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
//import org.netbeans.modules.projectimport.ProjectImporterException;
//import org.netbeans.modules.projectimport.eclipse.ProjectFactory;
import org.openide.ErrorManager;
import org.openide.WizardDescriptor;
import com.sun.dn.gui.*;

/**
 * Iterates on the sequence of  wizard panels.
 *
 * @author danny.coward@sun.com
 */
final class DNWizardIterator extends WizardDescriptor.ArrayIterator implements
         ChangeListener {
    
    /** Registered ChangeListeners */
    private List changeListeners;
    private String errorMessage;
    private DNProjectPanel dnProjectPanel;
    
    
    /** Initialize and create an instance. */
    DNWizardIterator(String language, String cpSourceDir) {
        super();
        this.getImportPanel().setLanguage(language);
        this.getImportPanel().setDefaultDestinationDirectory(cpSourceDir);
    }
    
    protected WizardDescriptor.Panel[] initializePanels() {
        dnProjectPanel = new DNProjectPanel();
        WizardDescriptor.Panel[] p = {dnProjectPanel};
        dnProjectPanel.addChangeListener(this);
        
        return p;
    }
    
    ImportPanel getImportPanel() {
        return dnProjectPanel.getImportPanel();
    }
    
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
    
    protected void fireChange() {
        if (changeListeners != null) {
            ChangeEvent e = new ChangeEvent(this);
            for (Iterator i = changeListeners.iterator(); i.hasNext(); ) {
                ((ChangeListener) i.next()).stateChanged(e);
            }
        }
    }
    
    
    
    public void stateChanged(javax.swing.event.ChangeEvent e) {
        System.out.println("State CHanged " + e);
       
    }
    
    void updateErrorMessage() {
        errorMessage = ((DNProjectPanel) current()).getErrorMessage();
        fireChange();
    }
    
    String getErrorMessage() {
        return errorMessage;
    }
}