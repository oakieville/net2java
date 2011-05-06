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
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 *
 * @author Danny  Coward
 */
public class ProjectSelector extends JDialog {
    Project project = null;
    JList list = new JList();
    java.util.List projectList = new ArrayList();
    
    /** Creates a new instance of ProjectSelector */
    public ProjectSelector() {
           super(new JFrame(), true);
           this.setTitle("Select a target Java project");
           this.init();
    }
    
    public Project getProject() {
        int i = list.getSelectedIndex();
        if (i != -1) {
            return (Project) projectList.get(i);
        } else {
           return null; 
        }
    }
    
    private void init() {
        
        
        OpenProjects op = OpenProjects.getDefault();
        Project[] projects = op.getOpenProjects();
        for (int i = 0; i < projects.length; i++) {
            projectList.add(projects[i]);
        }
        
        
        DefaultListModel dlm = new DefaultListModel();
        for (Iterator itr = projectList.iterator(); itr.hasNext();) {
            Project p = (Project) itr.next();
            ProjectInformation pi = ProjectUtils.getInformation(p);
            dlm.addElement(pi.getDisplayName());
        }
        list.setModel(dlm);
        
        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());
       
        JScrollPane jsp = new JScrollPane(list);
        p.add(jsp, BorderLayout.CENTER);
        JButton doneB = new JButton("Import into this project");
        doneB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                doDone();
            }
        });
        JPanel pp = new JPanel();
        pp.add(doneB);
        p.add(pp, BorderLayout.SOUTH);
        
        Dimension border = new Dimension(10, 10);
        JPanel vertBord = new JPanel();
        vertBord.setLayout(new BoxLayout(vertBord, BoxLayout.Y_AXIS));
        vertBord.add(Box.createRigidArea(border));
        vertBord.add(p);
        vertBord.add(Box.createRigidArea(border));
        
        JPanel horizBord = new JPanel();
        horizBord.setLayout(new BoxLayout(horizBord, BoxLayout.X_AXIS));
        horizBord.add(Box.createRigidArea(border));
        horizBord.add(vertBord);
        horizBord.add(Box.createRigidArea(border));
        
        this.getContentPane().add(horizBord);
        
    }
    
    public void doDone() {
        this.setVisible(false);
    }
    
}

