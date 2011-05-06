
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
import com.sun.dn.parser.DNVariable;

	/** Object representing a Java variable. Could be used
	** in a Java method declaration, or as an instance variable
	** on a Java class.
	** @author danny.coward@sun.com
	*/

public class JavaVariable {
	String type;
	String name;
	List modifiers = new ArrayList();
	String initialValue;
	List dimension;
        List comments = new ArrayList();

	public JavaVariable(String name, String type) {
		this.name = name;
		if (type == null) {
			throw new RuntimeException("Null type");
		}
		this.type = type;
                //System.out.println("Type is " + type);
	}

	public String toString() {
		return "JV: " + name + ", " + type;
	}
        
        public void addComment(String comment) {
            this.comments.add(comment);
        }
        
        public List getComments() {
            return this.comments;
        
        }

	public void setDimension(List dimension) {
		this.dimension = dimension;
	}

	public List getDimension() {
		return this.dimension;
	}

	public JavaVariable(String name, String type, List modifiers, String initialValue, List dimension) {
		this(name, type);
		this.modifiers = modifiers;
		this.initialValue = initialValue;
		this.dimension = dimension;
	}
        
        public void makeStatic() {
            if (!this.modifiers.contains(JavaKeywords.J_STATIC)) {
                this.modifiers.add(JavaKeywords.J_STATIC);
            }
        }

	private String getModifierString() {
		String s = "";
		for (Iterator itr = this.modifiers.iterator(); itr.hasNext();) {
			String next = (String) itr.next();
			if (!"".equals(next)) {
				s = s + next + " ";
			}
		}
		return s;
	}

	public String asJava() {
		String s = this.getModifierString();
		s = s + DNVariable.writeJavaFor(this.name, this.type, this.dimension);
		if (this.initialValue != null) {
			s = s + " = " + initialValue;
		}
		return s;
	}

	public String getName() {
		return this.name;
	}

	public String getType() {
		return this.type;
	}
}

 