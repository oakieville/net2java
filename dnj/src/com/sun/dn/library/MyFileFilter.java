
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

class MyFileFilter extends javax.swing.filechooser.FileFilter
                                    implements FilenameFilter {
    java.util.List extensions = new ArrayList();
    
    public void addExtension(String s) {
        extensions.add(s);
    }
    
    public boolean accept(File dir, String name) {
        return this.accept(new File(dir, name));
    }
    
    public boolean accept(File f) {
        for (Iterator itr = extensions.iterator(); itr.hasNext();) {
            String next = (String) itr.next();
            if (f.isDirectory() || f.toString().endsWith(next)) {
                return true;
            }
        }
        return false;
    }
    
    public String getDescription() {
        return "Library Files";
    }
    
}
 