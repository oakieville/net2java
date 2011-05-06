
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
import javax.swing.tree.*;
import javax.swing.event.*;
/**
 * Gui for editing Library entries, aka .NET class to Java translations.
 * @author Danny  Coward
 */
 class FolderPanel extends JPanel {
    private File f;
    private LibraryChangeListener lee;
    
    public FolderPanel(LibraryChangeListener lee) {
        this.lee = lee;
        this.initGui();
        this.hookupGui();
    }
    
    public void setFile(File f) {
        this.f = f;
        if (f != null) {
            
        }
    }
    
    
    
    private void hookupGui() {
       
    }
    
    public void initGui() {
        this.setLayout(new BorderLayout());
        JPanel lblp = new JPanel();
        lblp.add(new JLabel("Library Folder"));
        this.add(lblp, BorderLayout.NORTH);
        JPanel p = new JPanel();
        this.add(p, BorderLayout.CENTER);
    }
}

 