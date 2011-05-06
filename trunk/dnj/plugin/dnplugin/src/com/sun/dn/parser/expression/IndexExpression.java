
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

	/**
	A VB index expression results in the value of an array element or an indexed property. 
	An index expression consists of, in order, an expression, an opening parenthesis, 
	an index argument list, and a closing parenthesis. 
	The expression must result in a value whose type is an array, in a value 
	whose type has a set of overloaded default properties, or in a set of overloaded properties. <br>

	IndexExpression ::= Expression ( ArgumentList ) <br>

	not sure how to tell the difference between one of these and
	a method call to a method that takes int arguments, but....
	@author danny.coward@sun.com
	**/


public class IndexExpression extends ExpressionAdapter {
	private DNVariable variable;
	private List indices = new ArrayList();
        
        public static boolean isVBIndexExpression(String code, InterpretationContext context) {
		//parse the last chunk using .
		//then see if there are brackets
		//if so, pull out the name
		//look for variable in context	
		//of same name, otherwise, no.
		List l = Util.tokenizeIgnoringEnclosers(code, ".");
		if (l.size() == 1) {
			String s = (String) l.get(l.size()-1);
			if (s.endsWith(")")) {
				String name = s.substring(0, s.indexOf('('));
				DNVariable var = context.getVariable(name);
				if ((var != null) && !var.isPoint()) {
					return true;
				}
			}
		}
		return false;
	}
        
        public static boolean isCSIndexExpression(String code, InterpretationContext context) {
		//parse the last chunk using .
		//then see if there are brackets
		//if so, pull out the name
		//look for variable in context	
		//of same name, otherwise, no.
		List l = Util.tokenizeIgnoringEnclosers(code, ".");
		if (l.size() == 1) {
			String s = (String) l.get(l.size()-1);
			if (s.endsWith("]")) {
				String name = s.substring(0, s.indexOf('['));
				DNVariable var = context.getVariable(name);
				if ((var != null) && !var.isPoint()) {
					return true;
				}
			}
		}
		return false;
	}
        
        private static IndexExpression getIndexExpression(String openB, String closeB, String code, ExpressionFactory factory, InterpretationContext context) {
		Debug.clogn("Parse " + code, IndexExpression.class);
		List l = Util.tokenizeIgnoringEnclosers(code, ".");
		String s = (String) l.get(l.size()-1);
		String name = s.substring(0, s.indexOf(openB));
		DNVariable var = context.getVariable(name);
		if (var == null) {
			throw new RuntimeException("shouldn't be here");
		}
		List indicesList = new ArrayList();
		String indicesString = s.substring(s.indexOf(openB) + 1, s.indexOf(closeB));
		List indicesStrings = Util.tokenizeIgnoringEnclosers(indicesString, ",");
		for (Iterator itr = indicesStrings.iterator(); itr.hasNext();) {
			String next = (String) itr.next();
			Expression e = factory.getExpression(next, context);
			indicesList.add(e);
		}
		return new IndexExpression(code, var, indicesList, context);

	}
        
        public static IndexExpression getCSIndexExpression(String code, InterpretationContext context) {
		return getIndexExpression("[", "]", code, new CSExpressionFactory(), context);

	}

	public static IndexExpression getVBIndexExpression(String code, InterpretationContext context) {
		return getIndexExpression("(", ")", code, new VBExpressionFactory(), context);

	}
        

	private IndexExpression(String code, DNVariable variable, List indices, InterpretationContext context) {
            super(code, context);
            this.variable = variable;
            this.indices = indices;
	}

	public String tryAsJava() {
		String s = variable.getName() + "[";
		for (int i = 0; i < indices.size(); i++) {
			Expression e = (Expression) indices.get(i);
			if (i < indices.size() -1) {
				s = s + " " + e.asJava() + ",";
			} else {
				s = s + " " + e.asJava();
			}
		} 
		s = s + " ]";
		return s;
	}

	public String getTypeName() {
		return this.variable.getType();
	}

	public DNType getDNType() {
		return context.getLibrary().getLibraryData().getLibraryClass(this.getTypeName());
	}
        
        
        public DNVariable getVariable() {
            return this.variable;
        }

	

}

 