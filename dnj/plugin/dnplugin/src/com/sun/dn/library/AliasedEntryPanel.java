
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

import com.sun.dn.parser.DNType;
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
 class AliasedEntryPanel extends JPanel {
    private Entry e;
    private LibraryEntryFrame lee;
    private LibraryData library;
    private JButton aliasBtn = new JButton("View Alias entry");
    
    
    public AliasedEntryPanel(LibraryEntryFrame lee) {
        this.lee = lee;
        library = lee.getLibrary();
        this.initGui();
        this.hookupGui();
    }
    
    public void setEntry(Entry e) {
        this.e = e;
        aliasBtn.setText("Go to " + this.e.getAliasName());
    }
    
    private void goToEntry() {
        DNType t = library.getLibraryClass(this.e.getAliasName());
        
        // just a cheap way of finding the type !!
        //System.out.println(t.getEntry());
        lee.filesChanged(t.getEntry().getFile());
        
    }
    
    
    
    private void hookupGui() {
       
    }
    
    public void initGui() {
        this.setLayout(new BorderLayout());
        JPanel lblp = new JPanel();
        lblp.add(new JLabel("This entry has been aliased to another entry."));
        this.add(lblp, BorderLayout.NORTH);
        JPanel p = new JPanel();
        p.add(aliasBtn);
        this.add(p, BorderLayout.CENTER);
        aliasBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                goToEntry();
            }
        });
    }
}


 