
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
import com.sun.dn.*;
import com.sun.dn.parser.*;
import com.sun.dn.java.*;
import com.sun.dn.util.*;

	/** A .NET expression that initialises an array. For example, in C#, <br>
         * 
         * int[] i = new int[4]{0, 1, 2, 3};
         * 
         * 
	* @author danny.coward@sun.com

	**/


public class ArrayElementInitializer extends ExpressionAdapter {
	private List expressions = new ArrayList();
        
	public static boolean isVBArrayElementInitializer(String code, InterpretationContext context) {
		// does it start with a curly bracket, and end with one
		return code.startsWith("{") && code.endsWith("}");
	}
        
        public static boolean isCSArrayElementInitializer(String code, InterpretationContext context) {
		// does it start with a curly bracket, and end with one
		return code.startsWith("{") && code.endsWith("}");
	}

	public String getTypeName() {
		throw new RuntimeException("Not Implemented");
	}

	public DNType getDNType() {
		throw new RuntimeException("Not Implemented");
	}
        
        /**
         *Defined as <br>
	* ArrayElementInitializer ::= { [ VariableInitializerList ] } <br>
	* VariableInitializerList ::= <br>
	* VariableInitializer | <br>
	* VariableInitializerList , VariableInitializer <br>
	* VariableInitializer ::= Expression | ArrayElementInitializer <br>
         */
        public static ArrayElementInitializer createVBArrayElementInitializer(String code, InterpretationContext context) {
            return new ArrayElementInitializer(code, new VBExpressionFactory(), context);
        }
        
        public static ArrayElementInitializer createCSArrayElementInitializer(String code, InterpretationContext context) {
            return new ArrayElementInitializer(code, new CSExpressionFactory(), context);
        }
     

	private ArrayElementInitializer(String code, ExpressionFactory factory, InterpretationContext context) {
            super(code, context);
            Debug.logn("Parse " + code, this);
            String insideCode = code.substring(1, code.length() -1);

            List elements = Util.tokenizeIgnoringEnclosers(insideCode, ",");
            for (Iterator itr = elements.iterator(); itr.hasNext();) {
            	String next = ((String) itr.next()).trim();
		Expression e = factory.getExpression(next, context);// you could add the known type here
		expressions.add(e);
            }
		//System.out.println(expressions);
            Debug.logn("--" + this, this);
	}

	public String tryAsJava() {
		String s = "{";
		for (Iterator itr = expressions.iterator(); itr.hasNext();) {
			Expression next = (Expression) itr.next();
			s = s + next.asJava();
			if (itr.hasNext()) {
				s = s + ",";
			}
		}
		s = s + "}";
		return s;
	}

	public String toString() {
		return "ArrayElementInit: " + expressions;
	}
}
 