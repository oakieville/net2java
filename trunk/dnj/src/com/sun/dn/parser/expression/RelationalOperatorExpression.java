
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

import com.sun.dn.parser.*;
import com.sun.dn.*;
import java.util.*;
import com.sun.dn.java.JavaPrimitives;
import com.sun.dn.util.*;
import com.sun.dn.library.LibraryData;

	/** A .NET expression using a relational operator.
	expression = expr1 <op> expr2

	where <op> is a relational operator.
         * @see Operators.getComparisons()
	@author danny.coward@sun.com
	*/

public class RelationalOperatorExpression extends ExpressionAdapter implements BooleanExpression {
	private Expression le;
	private Expression re;
	private String operator;
	private Operators operators;
        
        public static RelationalOperatorExpression createVBRelationalOperatorExpression(String code, InterpretationContext context) {
            RelationalOperatorExpression roe = new RelationalOperatorExpression(code, context);
            roe.parseVB(code);
            return roe;
        }
        
         public static RelationalOperatorExpression createCSRelationalOperatorExpression(String code, InterpretationContext context) {
            RelationalOperatorExpression roe = new RelationalOperatorExpression(code, context);
            roe.parseCS(code);
            return roe;
        }
	
	private RelationalOperatorExpression(String code, InterpretationContext context) {
            super(code, context);
	}

	public static boolean isCSRelationalOperatorExpression(String code, InterpretationContext context) {
            if (BinaryOperatorExpression.containsShift(code, context)) {
                return false;
            }
		for (Iterator itr = (new CSOperators()).getRelationalOperators().iterator(); itr.hasNext();) {
			String nextOperator = (String) itr.next();
			if (Util.tokenizeIgnoringEnclosers(code, nextOperator).size() > 1) {
				return true;
			}
		}
		return false;
	}

	public String getTypeName() {
		return "System.Boolean";
	}

	public DNType getDNType() {
		return context.getLibrary().getLibraryData().getLibraryClass(this.getTypeName());
	}

	public void parseCS(String rawS) {
		this.operators = new CSOperators();
		this.parse(rawS, new CSExpressionFactory(), operators);
	}

	public void parseVB(String rawS) {
		this.operators = new VBOperators();
		this.parse(rawS, new VBExpressionFactory(), operators);
	}


	private void parse(String rawS, ExpressionFactory factory, Operators operators) {
		Debug.logn("Parse " + rawS, this);
		List sides = this.getSides(rawS, operators);
		String leftSide = (String) sides.get(0);
		String rightSide = (String) sides.get(1);
		
		Debug.logn("LHS " + leftSide, this);
		Debug.logn("RHS " + rightSide, this);
		Debug.logn("Operator " + this.operator, this);	

		this.le = factory.getExpression(leftSide, this.context);		
		this.re = factory.getExpression(rightSide, this.context);
	}

	private List getSides(String rawS, Operators operators) {
		for (Iterator itr = operators.getRelationalOperators().iterator(); itr.hasNext();) {
			String nextOperator = (String) itr.next();
			List l = Util.tokenizeIgnoringEnclosers(rawS, nextOperator);
			if (l.size() > 1) {
				this.operator = nextOperator;
				return l;
			}
		}
		throw new RuntimeException("Error finding operator in ." + rawS + ".");
	}


	public String tryAsJava() {
		String leftJava = this.le.asJava();
		String rightJava = this.re.asJava();
		boolean needsJavaEquals = false;

		if (VBOperators.isEquals(this.operator)) { 
			return equalityAsJava(this.le, this.re, context.getLibrary());
                } else if (this.operator.equals(VBKeywords.VB_Like)) {
                    return this.le.asJava() + ".matches(" + this.re.asJava() + ")";
		} else {
			return this.le.asJava() + " " + operators.translateComparison(this.operator) + " " + this.re.asJava();
		}
	}


		// the equals case needs special handling
		// for one, string equality is different
		// for another, if one side of the expression is primitive
		// and the other side isn't, we get into problems
		// unless we upcast from primitive to object and use
		// equals() 

	public static String equalityAsJava(Expression left, Expression right, Library lib) {
		String s = "";
		String leftJava = left.asJava();
		String rightJava = right.asJava();
		boolean needsJavaEquals = false;
                boolean needsUpcasting = Translator.needsUpcasting(left, right, lib);
		if (needsUpcasting) {
			needsJavaEquals = true;
			leftJava = Translator.ensureJUpcast(left, lib);
			rightJava = Translator.ensureJUpcast(right, lib);
		} else if (VBOperators.needsJavaEquals(left, "=")) {
			needsJavaEquals = true;
		}
		if (needsJavaEquals) {
			s = leftJava + ".equals(" + rightJava + ")";
		} else {
			s = leftJava + " == " + rightJava;
		}
		return s;
	}

	public String toString() {
		return "BoolExp: " + le + " " + operator + " " + re;
	}

	public boolean equals(Object o) {
		return false;
	}

}
 