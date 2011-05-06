
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

package com.sun.dn.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import com.sun.dn.util.*;
import com.sun.dn.*;
import com.sun.dn.java.*;
import com.sun.dn.parser.*;
import java.beans.*;
import java.io.*;


public class ImportProgressDialog extends JFrame {
    private int pWidth = 220;
    private int pHeight = 50;
    
    JProgressBar pp = new JProgressBar();
    
    public ImportProgressDialog(JFrame f) {
        //super(f, "Import Progress", true);
        this.setTitle("Importing project...");
        
        this.getContentPane().add(pp);
        pp.setIndeterminate(true);
        pp.setStringPainted(true);
        if (f != null) {
            int x = f.getBounds().x + ( f.getBounds().width / 2 ) - (pWidth / 2);
            int y = f.getBounds().y + ( f.getBounds().height / 2 ) - (pHeight / 2);
            this.setBounds(x, y, pWidth, pHeight);
        } else {
            this.setBounds(300, 300, pWidth, pHeight);
        }
        
    }
    
    public void started() {
        Thread t = new Thread() {
            public void run() {
                setLabel("Parsing source code...");
                setVisible(true);
                
            }
        };
        try {
            t.start();
            
        } catch (Throwable tt) { 
            
        }
        
    }
    
    public void parsed() {
        Thread t = new Thread() {
            public void run() {
                setLabel("Writing Java code...");
            }
        };
        try {
            t.start();
            
        } catch (Throwable tt) { 
            
        }
       
    }
    
    public void setLabel(String s) {
        pp.setString(s);
    }
    
    public void finished() {
        Thread t = new Thread() {
            public void run() {
                setLabel("Done");
                setVisible(false);
            }
        };
        try {
            t.start();
            
        } catch (Throwable tt) { 
            
        }
        //
    }
}
 