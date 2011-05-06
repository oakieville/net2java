
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

	/** Startup wrapper class for starting up command line
	** applications.
	*@author danny.coward@sun.com
	*/

public class JavaMain extends JavaClass {
	public static String MAIN_NO_ARGS = "mainnoargs";
	public static String MAIN_STRING_ARGS = "mainstringargs";
	public String argsType;
	public String className;

	public JavaMain(String className, String[] args, String classImport, String argsType) {
		super(className + "_JavaMain", new ArrayList(), null, new ArrayList(), false);
		if (!"".equals(classImport)) {
                    super.imports.add(classImport);
                }
		this.className = className;
		this.argsType = argsType;
	}

	public String asCode() {
		String s = "";
                s = s + super.getImportsCode();
                s = s + "\n";
		s = s + super.getCommentCode();
		s = s + "public class " + this.getName() + " { \n";
		s = s + "	public static void main(String[] args) { \n";
		if (argsType.equals(MAIN_NO_ARGS)) {
			s = s + "		" + className + ".Main();\n";
		} else {
			s = s + "		" + className + ".Main(args);\n";
		}
		s = s + "	}\n";
		s = s + "}\n";
		return s;
	}

}


 