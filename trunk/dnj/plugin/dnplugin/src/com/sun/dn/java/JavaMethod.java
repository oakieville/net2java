
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
import com.sun.dn.util.*;
import com.sun.dn.Library;

	/** An object representing a Java method.
	@author danny.coward@sun.com
	*/

public class JavaMethod {
	String name;
	List args;
	List exceptions;
	String returnType;
	List codeLines = new ArrayList();
	boolean isInstance = true;
	List modifiers;
	boolean isBodiless = false;
	Library library;
        List commentStrings = new ArrayList();
	
	
	public JavaMethod(List modifiers, String name, String returnType, List args, Library library) {
		this.modifiers = modifiers;
		this.args = args;
		this.name = name;
		this.library = library;
		if (returnType != null) {
			this.returnType = returnType;
		} else {
			this.returnType = JavaKeywords.J_VOID;
		}
	}

	public JavaMethod(List modifiers, String name, String returnType, List args, 
					boolean isInstance, boolean isBodiless, Library library) {
		this(modifiers, name, returnType, args, library);
		this.isInstance = isInstance;
		this.isBodiless = isBodiless;
		//System.out.println("JAVAMETHOD " + name + " " + isInstance + " " + modifiers);
	}
        
           protected boolean isConstructor() {
            return false;
        }
        
        public void addCommentString(String comment) {
            this.commentStrings.add(comment);
        }
        
        public void makeAbstract() {
            this.modifiers.remove(JavaKeywords.J_STATIC);
            this.modifiers.add(JavaKeywords.J_ABSTRACT);
        }
        
        public void isAbstract() {
            this.modifiers.contains(JavaKeywords.J_ABSTRACT);
        }

	public void addModifier(String modifier) {
		this.modifiers.add(modifier);
	}

	public List getArgs() {
		return this.args;
	}

	public void addCodeLine(String line) {
		this.codeLines.add(line);
	}

	public String getName() {
		return this.name;
	}

	public void disallowPrivate() {
		this.modifiers.remove(JavaKeywords.J_PRIVATE);
	}
        
        public void makeProtected() {
            this.modifiers.remove(JavaKeywords.J_PRIVATE);
            this.modifiers.remove(JavaKeywords.J_PUBLIC);
            this.modifiers.add(JavaKeywords.J_PROTECTED);
        }
        
        public void makePublic() {
            this.modifiers.remove(JavaKeywords.J_PRIVATE);
            this.modifiers.add(JavaKeywords.J_PUBLIC);
            this.modifiers.remove(JavaKeywords.J_PROTECTED);
        }

	private boolean isInstance() {
		return this.isInstance;
	}

	public String asCode(int indent) {
		if (Debug.isJavaDebug()) {
			codeLines.add(0, "System.out.println(\" entering " + this.getName() + "\");");
		}


		String indentString = "";
		for (int i = 0; i < indent; i++) {
			indentString = indentString + "\t";
		}
		
                StringBuffer sb = new StringBuffer();
                sb.append(indentString);
                if (!this.commentStrings.isEmpty()) {
                    sb.append("/**\n");
                    for (Iterator itr = this.commentStrings.iterator(); itr.hasNext();) {
                        String nextComment = (String) itr.next();
                        sb.append(indentString + "* " + nextComment + "\n");
                    }
                    sb.append(indentString + "**/\n");
                    sb.append(indentString);
                }
                
                
		for (Iterator itr = this.modifiers.iterator(); itr.hasNext();) {
			String next = (String) itr.next();
			// hackery...
			if (next.equals(JavaKeywords.J_STATIC) && !isInstance) {
				// let this happen below
			} else {
				if (!next.equals("")) {
					sb.append(next + " ");
				}
			}
		}
		if (!isInstance) {
			sb.append(JavaKeywords.J_STATIC + " ");
		}

		sb.append(returnType + " " + name + "(" + writeArgs() + ")");

		if (this.isBodiless) {
			sb.append(";");
		} else {
			sb.append(" {\n");
			for (Iterator itr = codeLines.iterator(); itr.hasNext();) {
				String nextCodeLine = (String) itr.next();
				//nextCodeLine.replaceAll("\n", "\n" + indent); 
				sb.append(indentString + "\t" + nextCodeLine + "\n");
			}
			sb.append(indentString + "}");
		}
		return new String(sb);
	}

	String writeArgs() {
		String s = "";
		for (Iterator itr = this.args.iterator(); itr.hasNext();) {
			JavaVariable jv = (JavaVariable) itr.next();
			String dimensionString = "";
			if (jv.getDimension() != null) {
				dimensionString = "[]";
			}
			//System.out.println("*" + jv);
			s = s + jv.getType() + dimensionString + " " + jv.getName();
			if (itr.hasNext()) {
				s = s + ", ";
			}
		}
		return s;
	}

	public String toString() {
		return "JMethod " + this.name + " " + this.isInstance;
	}
}
 