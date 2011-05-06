
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
package com.sun.dn.java;

import java.io.*;
import java.util.*;
import com.sun.dn.*;
import com.sun.dn.Library;

	/** A special kind of java method which creates new
	** instances of the owning class.
	** @author danny.coward@sun.com
	*/

public class Constructor extends JavaMethod {
	JavaClass jClass;
	
	public Constructor(List modifiers, JavaClass jClass, List args, Library library) {
		super(modifiers, JavaKeywords.J_NEW, "", args, library);
		this.jClass = jClass;
	}
        
        protected boolean isConstructor() {
            return true;
        }

	public String asCode(int indent) {
		String indentString = "";
		for (int i = 0; i < indent; i++) {
			indentString = indentString + "\t";
		}
		String s = "";
		s = indentString + JavaKeywords.J_PUBLIC + " " + jClass.getName() + "(" + super.writeArgs() + ") {\n";
		for (Iterator itr = codeLines.iterator(); itr.hasNext();) {
			String nextCodeLine = (String) itr.next();
			//nextCodeLine.replaceAll("\n", "\n" + indent); 
			s = s + indentString + "\t" + nextCodeLine + "\n";
		}
		s = s + indentString + "}";
		return s;
	}


}
 