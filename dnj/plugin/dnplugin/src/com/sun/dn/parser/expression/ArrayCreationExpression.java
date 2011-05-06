
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
package com.sun.dn.parser.expression;

import java.util.*;
import com.sun.dn.parser.*;
import com.sun.dn.util.*;
import com.sun.dn.*;
import com.sun.dn.library.LibraryData;


	/** A VB array. Defined as <br>
	ArrayCreationExpression ::= <br>
	New TypeName ( ArgumentList ) ArrayElementInitializer | <br>
	New ArrayTypeName ArrayElementInitializer <br>
	<br>
	Dim MyArray As Integer() <br>
	MyArray = New Integer() {0, 1, 2, 3} <br>
	@author danny.coward@sun.com

	**/


public class ArrayCreationExpression extends NewExpression {
	private String name;
	private List initExpressionArray = new ArrayList();
	private List args = new ArrayList();

	public ArrayCreationExpression(String dnCode, String name, List initExpressionArray, 
							List args, InterpretationContext context) {
            
            super(dnCode, context);
            this.name = name;
            this.initExpressionArray = initExpressionArray;
            this.args = args;
	}

	public String getName() {
		return this.name;
	}

	public String tryAsJava() {
		Library library = this.context.getLibrary();
		String jClass = library.getJavaTypeFor(this.name);
		String s = "new " + jClass + "[] {";
		for (Iterator itr = initExpressionArray.iterator(); itr.hasNext();) {
			Expression next = (Expression) itr.next();
			s = s + next.asJava();
			if (itr.hasNext()) {
				s = s + ", ";
			}
		}
		s = s + "}";
		return s;
	}

	public String getTypeName() {
		return name + "()";
	}

	public DNType getDNType() {
            Debug.logn("Get type for " + this.getOriginalCode(), this);
		return super.context.getLibrary().getProgramDefinedOrLibraryDNTypeFor(this.getTypeName());
	}


	
	

}
 