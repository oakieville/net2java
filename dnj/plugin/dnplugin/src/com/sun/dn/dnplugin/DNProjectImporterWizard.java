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

import java.awt.Dialog;
import java.util.Iterator;
import java.util.Set;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import com.sun.dn.Interpreter;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.WizardDescriptor;
import org.openide.util.NbBundle;
import org.netbeans.api.project.ui.*;
import org.netbeans.api.project.*;
import org.openide.filesystems.*;

    /** @author danny.coward@sun.com */

public class DNProjectImporterWizard {
    private boolean cancelled;
    private String language;
    private String cpSourceDir;
    private String languageLabel;
    
    public DNProjectImporterWizard(String language) {
        this.language = language;
        if (language.equals(Interpreter.VB_LANGUAGE)) {
            languageLabel = "VB";
        } else {
            languageLabel = "C#";
        }
        cpSourceDir = this.getCurrentProjectSourceDirectory();
       
    }
    
    public String getCurrentProjectSourceDirectory() {
        Project mainProject = this.getMainProject();
        if (mainProject != null) {
            FileObject projectDirectory = mainProject.getProjectDirectory();
            return projectDirectory.getPath() + "/src";
        } else {
            return System.getProperty("user.home") + "/dnj/temp";   
        }
    }
    
    public Project getMainProject() {
        OpenProjects ops = OpenProjects.getDefault();
        return ops.getMainProject();
    }
   
    public void start() {
        final DNWizardIterator iterator = new DNWizardIterator(language, cpSourceDir);
        final WizardDescriptor wizardDescriptor = new WizardDescriptor(iterator);
        iterator.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                wizardDescriptor.putProperty("WizardPanel_errorMessage", // NOI18N
                        iterator.getErrorMessage());
            }
        });
        //wizardDescriptor.setValue(WizardDescriptor.FINISH_OPTION);
        wizardDescriptor.putProperty("WizardPanel_contentDisplayed", Boolean.TRUE);
        wizardDescriptor.setTitleFormat(new java.text.MessageFormat("{1} Import " + languageLabel + " project")); // NOI18N
        wizardDescriptor.setTitle("Import .NET Project"); // NOI18N
        
        //wizardDescriptor.putProperty("WizardPanel_image", Image);
        Dialog dialog = DialogDisplayer.getDefault().createDialog(wizardDescriptor);
        dialog.setVisible(true);
        dialog.toFront();
        cancelled = wizardDescriptor.getValue() != WizardDescriptor.FINISH_OPTION;
        if (!cancelled) {
            iterator.getImportPanel().doTranslate();
        } else {
            System.out.println("Wizard closed");
        }
    }
    
    
    
    public boolean isCancelled() {
        return cancelled;
    }
    
    /** Gets message from properties bundle for this package. */
    static String getMessage(String key) {
        return "DNProjectImporter.getMessage()";
       
    }
    
    /** Gets message from properties bundle for this package. */
    static String getMessage(String key, Object param1) {
         return "DNProjectImporter.getMessage() with param";
    }
}