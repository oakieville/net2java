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
import com.sun.dn.gui.*;
import javax.swing.*;

import java.io.File;
import org.openide.filesystems.*;
import org.openide.text.*;
import org.openide.cookies.*;
import org.openide.loaders.*;
import org.openide.nodes.*;
import javax.swing.text.*;
import com.sun.dn.library.*;



/**
 *
 * @author Danny  Coward
 */
public class SourceOpener implements SupportClassEditor {
    public void openFile(File f) {
        System.out.println("Open " + f);
        File normalisedFile = new File(f.getAbsolutePath());
        try {
            FileObject[] fo = FileUtil.fromFile(normalisedFile);
            DataObject d = DataObject.find(fo[0]); 
            EditorCookie ec = (EditorCookie) d.getCookie(EditorCookie.class);
            System.out.println(ec);
            ec.open();
           // StyledDocument doc = ec.openDocument();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        
    }
    
}
