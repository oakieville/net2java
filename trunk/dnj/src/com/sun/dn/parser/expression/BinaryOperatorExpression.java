
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
import com.sun.dn.parser.statement.*;
import com.sun.dn.util.*;

	/** A .NET expression using a .NET operator and one or two operands. <br>
         * For example <br>
         * VB: Not (expression) <br>
         * C# or VB: i < 3 <br>
         * VB: apple.IsGreen AND apple.HasStalk
         * 
         * 
	@author danny.coward@sun.com
	**/

public abstract class BinaryOperatorExpression extends OperatorExpression {
	Expression leftExpression;
	Expression rightExpression;
        
        /**
         * Defined as <br> 
	OperatorExpression ::= UnaryOperatorExpression | BinaryOperatorExpression <br>
	<br>
	BinaryOperatorExpression ::= <br>
	ArithmeticOperatorExpression | <br>
	RelationalOperatorExpression | <br>
	LikeOperatorExpression | <br>
	ConcatenationOperatorExpression | <br>
	ShortCircuitLogicalOperatorExpression | <br>
	LogicalOperatorExpression | <br>
	ShiftOperatorExpression <br>
         */

	public static boolean isVBBinaryOperatorExpression(String code, InterpretationContext context) {
		return !(getVBOperatorString(code).equals("x"));
	}

	public static boolean isCSBinaryOperatorExpression(String code, InterpretationContext context) {
            if (Literal.getCSLiteral(code, context) != null && !CSExpressionFactory.isBooleanExpression(code, context)) {
                return false;
            }
            return !(getCSOperatorString(code).equals("x"));
	}
        
        protected BinaryOperatorExpression(String code, InterpretationContext context) {
            super(code, context);
        }
        
        static String exceptFirstAsString(List l, String sep) {
            String s = "";
            for (int i = 1; i < l.size(); i++) {
                s = s + (String) l.get(i);
                if (i != l.size()-1) {
                    s = s + sep;
                } 
            }
            return s;
        }

	static BinaryOperatorExpression createCSBinaryOperatorExpression(String code, InterpretationContext context) {
		String sep = getCSOperatorString(code);
		List l = Util.tokenizeIgnoringEnclosers(code, sep);
		
		ExpressionFactory factory = new CSExpressionFactory();
		Expression left = factory.getExpression((String) l.get(0), context);
		Expression right = factory.getExpression(exceptFirstAsString(l, sep), context);

		if (isString(left.getTypeName())) {
			ConcatenationOperatorExpression coe = new ConcatenationOperatorExpression(left, right, context);
			return coe;
		} else if (isIntegerNumeric(left) &&  isIntegerNumeric(right)) {
			ArithmeticOperatorExpression aoe = ArithmeticOperatorExpression.createCSArithmeticOperatorExpression (code, left, right, sep, context);
			return aoe;
		} else {
                        ArithmeticOperatorExpression aoe = ArithmeticOperatorExpression.createCSArithmeticOperatorExpression(code, left, right, sep, context);
                        return aoe;
                }
	}
        
        public Expression getLeftExpression() {
            return this.leftExpression;
        }
        
        public Expression getRightExpression() {
            return this.rightExpression;
        }
        
        
        
        private static boolean isString(String typeName) {
            return DNType.shortNameEquivalent(typeName, "String") ||
                    DNType.shortNameEquivalent(typeName, "string");
        }

	public static boolean isIntegerNumeric(Expression exp) {
		return DNType.shortNameEquivalent(exp.getTypeName(), "int") || DNType.shortNameEquivalent(exp.getTypeName(), "Integer");
	}
        
        public static boolean containsShift(String code, InterpretationContext context) {
           List l = Util.tokenizeIgnoringEnclosers(code, ">>");
           if (l.size() >1) {
               return true;
           }
           l = Util.tokenizeIgnoringEnclosers(code, "<<");
           if (l.size() >1) {
               return true;
           }
           return false;      
        }

	static BinaryOperatorExpression createVBBinaryOperatorExpression(String code, InterpretationContext context) {
		String sep = getVBOperatorString(code);
		if (sep.equals("&")) {
			return ConcatenationOperatorExpression.createVBConcatenationOperatorExpression(code, context);
		} else if (sep.equals("+")
				|| sep.equals("-")
				|| sep.equals("*")
				|| sep.equals("\\")
				|| sep.equals("" + '/')
				|| sep.equals(VBKeywords.VB_Mod)
				|| sep.equals("^")) {
			return ArithmeticOperatorExpression.createVBArithmeticOperatorExpression(code, context, sep);
		} else {
			throw new RuntimeException("Shouldn't be here");		
		}
	}

	protected void parseVB(String code, String op) {
		Debug.logn("Parse " + code, this);
		
		List l = Util.tokenizeIgnoringEnclosers(code, op);
		List shrunk = Util.shrinkListByOne(l);
		String last = ((String) l.get(l.size() - 1)).trim();
		String first = "";
		for (Iterator itr = shrunk.iterator(); itr.hasNext();) {
			String next = (String) itr.next();
			if (first.equals("")) {
				first = first + next;
			} else {
				first = first + " " + op + " " + next;
			}
		}
		first = first.trim();
		leftExpression = (new VBExpressionFactory()).getExpression(first, this.context);
		rightExpression = (new VBExpressionFactory()).getExpression(last, this.context);
		Debug.logn("" + this, this);
	}

	private static String getCSOperatorString(String code) {
            List allOperators = new ArrayList();
            allOperators.addAll(CSOperators.getBinaryOperators());
            allOperators.add("&");
                for (Iterator itr = allOperators.iterator(); itr.hasNext();) {
                    String nextOp = (String) itr.next();
                    List l = Util.tokenizeIgnoringEnclosers(code, nextOp);
                    if (l.size() > 1 ) {
			return nextOp;
                    }
                }
		return "x";
	}
        
        public abstract String getOperator();


	protected static String getVBOperatorString(String code) {
		List l = Util.tokenizeIgnoringEnclosers(code, '&');
		if (l.size() > 1 ) {
			return "&";
		}
		l = Util.tokenizeIgnoringEnclosers(code, '+');
		if (l.size() > 1 ) {
			return "+";
		}
		l = Util.tokenizeIgnoringEnclosers(code, '-');
		if (l.size() > 1 ) {
			return "-";
		}
		l = Util.tokenizeIgnoringEnclosers(code, '*');
		if (l.size() > 1 ) {
			return "*";
		}

		l = Util.tokenizeIgnoringEnclosers(code, "/");
		if (l.size() > 1 ) {
			return "/";
		}
		l = Util.tokenizeIgnoringEnclosers(code, "\\");
		if (l.size() > 1 ) {
			return "\\";
		}
		l = Util.tokenizeIgnoringEnclosers(code, "^");
		if (l.size() > 1 ) {
			return "^";
		}
		l = Util.tokenizeIgnoringEnclosers(code, " " + VBKeywords.VB_Mod + " ");
		if (l.size() > 1 ) {
			return VBKeywords.VB_Mod;
		}
		return "x";
	}
}
 