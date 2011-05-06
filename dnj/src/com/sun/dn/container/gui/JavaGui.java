
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
package com.sun.dn.container.gui;

import java.util.*;
import com.sun.dn.java.*;
import com.sun.dn.util.*;

	/** Startup wrapper class for starting up Gui
	** applications.
	** @author danny.coward@sun.com
	*/

public class JavaGui extends JavaClass {
	public String className;
        private boolean initialisedInConstructor;
        public static String FORM_ADAPTER = "com.sun.dn.library.System.Windows.Forms.FormAdapter";
        public static String INITIALIZE_METHOD_NAME = "InitializeComponent";

	public JavaGui (String className, String[] args, String classPackage, boolean initialisedInConstructor) {
		super(className + "_JavaGui", new ArrayList(), null, new ArrayList(), false);
                this.initialisedInConstructor = initialisedInConstructor;
                //System.out.println("VCreate java gui for " + className + " " + initialisedInConstructor);
		if (!classPackage.equals("")) {
			super.imports.add(classPackage);
		}
		this.className = className;
	}
        
        private static String getInitializeString() {
            return "((" + FORM_ADAPTER + ") j)." + INITIALIZE_METHOD_NAME + "()";
        }

	public String asCode() {
		String s = "";
                s = s + super.getImportsCode();
                s = s + "\n";
		s = s + super.getCommentCode();
		s = s + "public class " + this.getName() + " { \n";
		s = s + "	public static void main(String[] args) { \n";
		s = s + "		try {\n";
		//s = s + "System.out.println(\"here\");\n";
		s = s + "			javax.swing.JFrame j = new " + className + "();\n";
                if (!initialisedInConstructor) {
                    s = s + "			" + getInitializeString() + ";";
                }
		s = s + " 			j.setVisible(true);\n";
		//s = s + "			System.out.println(\"GUI: Made Visible.\");\n";
		s = s + "		} catch (Throwable t) {\n";
		s = s + "			t.printStackTrace();\n";
		s = s + "		}\n";
		s = s + "	}\n";
		s = s + "}\n";
		return s;
	}

	

}


 