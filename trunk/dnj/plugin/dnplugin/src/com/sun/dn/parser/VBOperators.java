
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
package com.sun.dn.parser;

import java.util.*;
import com.sun.dn.parser.expression.*;

	/** The VB language operators.
	*@author danny.coward@sun.com
	*/

public class VBOperators implements Operators {
	private static String EQUALS = "=";
	private static String RAISE_EQUALS = "^=";
	private static String MULTIPLY_EQUALS = "*=";
	private static String DIVIDE_EQUALS = '/' + "=";
	private static String INT_DIVIDE_EQUALS = "\\" + "=";  
	private static String ADD_EQUALS = "+=";
	private static String MINUS_EQUALS = "-=";
	private static String LEFT_SHIFT_EQUALS = "<<=";
	private static String RIGHT_SHIFT_EQUALS = ">>=";
	private static String CONCAT_EQUALS = "&=";

	public List getRelationalOperators() {
		List l = new ArrayList();
		l.add("<=");
		l.add(">=");
		l.add("<>");
		l.add("<");
		l.add(">");
		l.add("=");
                l.add(VBKeywords.VB_Like);
		return l;
	}

	public static boolean isEquals(String operator) {
		return operator.equals("=");
	}

	public String translateComparison(String s) {
		if (!this.getRelationalOperators().contains(s)) {
			throw new RuntimeException("Can't translate " + s);
		}
		if (s.equals("=")) {
			return "==";
		} else if (s.equals("<>")) {
			return "!=";
		} else {
			return s;
		}
	}

	public List getAssignmentOperators() {
		List operators = new ArrayList();
		operators.add(RAISE_EQUALS);
		operators.add(MULTIPLY_EQUALS);
		operators.add(DIVIDE_EQUALS);
		operators.add(INT_DIVIDE_EQUALS);
		operators.add(ADD_EQUALS);
		operators.add(MINUS_EQUALS);
		operators.add(LEFT_SHIFT_EQUALS);
		operators.add(RIGHT_SHIFT_EQUALS);
		operators.add(CONCAT_EQUALS);
                operators.add(EQUALS);
		return operators;
	}
        
        public static List getBinaryOperators() {
            List l = new ArrayList();
            l.add("&");
            l.add("+");
            l.add("-");
            l.add("*");
            l.add("/");
            l.add("\\");
            l.add("^");
            l.add(VBKeywords.VB_Mod);
            return l;            
        }
        
        public static String getReadableNameForBinaryOperator(String s) {
            if (s.equals("&")) {
                return "__amp";
            } else if (s.equals("+")) {
                return "__plus";
            } else if (s.equals("-")) {
                return "__minus";
            } else if (s.equals("*")) {
                return "__multiply";
            } else if (s.equals("/")) {
                return "__divide";
            } else if (s.equals("//")) {
                return "__remainder";
            } else if (s.equals("^")) {
                return "__power";
            } else if (s.equals(VBKeywords.VB_Mod)) {
                return "__modulo";
            } else {
                return "__customOperator";
            
            }
        }

	public String translateAssignmentOperator(String assignmentString, String operator, String valueString) {
		String s = "";
                //operators.add(RAISE_EQUALS);
		//operators.add(MULTIPLY_EQUALS);
		//operators.add(DIVIDE_EQUALS);
		//operators.add(INT_DIVIDE_EQUALS);
		//operators.add(ADD_EQUALS);
		//operators.add(MINUS_EQUALS);
		//operators.add(LEFT_SHIFT_EQUALS);
		//operators.add(RIGHT_SHIFT_EQUALS);
		
                
                
		if (operator.equals(EQUALS)) {
			s = assignmentString + " " + operator + " " + valueString + ";";
		} else if (operator.equals(CONCAT_EQUALS)) {
			s = assignmentString + " = " + assignmentString + " + " + valueString + ";";
                } else if (operator.equals(RAISE_EQUALS)) {
			s = assignmentString + " = Math.pow(" + assignmentString + ", " + valueString + ");";
                } else if (operator.equals(MULTIPLY_EQUALS)) {
			s = assignmentString + " = " + assignmentString + " * " + valueString + ";";
                } else if (operator.equals(DIVIDE_EQUALS)) {
			s = assignmentString + " = " + assignmentString + " / " + valueString + ";";
                } else if (operator.equals(INT_DIVIDE_EQUALS)) {
			s = assignmentString + " = (int) " + assignmentString + " / " + valueString + ";";
                } else if (operator.equals(ADD_EQUALS)) {
			s = assignmentString + " = " + assignmentString + " + " + valueString + ";";
                } else if (operator.equals(MINUS_EQUALS)) {
			s = assignmentString + " = " + assignmentString + " - " + valueString + ";";
                } else if (operator.equals(LEFT_SHIFT_EQUALS)) {
			s = assignmentString + " = " + assignmentString + " << " + valueString + ";";
                } else if (operator.equals(RIGHT_SHIFT_EQUALS)) {
			s = assignmentString + " = " + assignmentString + " >> " + valueString + ";";
		} else {
			throw new RuntimeException("Don't know the Java for the operator: ." + operator + ".");
		}
		return s;
	}

	public boolean isDelegateAssignmentOperator(String op) {
		return false;
	}


	public static boolean needsJavaEquals(Expression operand, String s) {
		return DNType.shortNameEquals(operand.getTypeName(), "String")
				&& s.equals("=");
	}

	/**
	Precendence:-

	(Not)
	Conjunction (And, AndAlso)
	Disjunction (Or, OrElse, Xor)

	*/

	public List getLogicals() {
		List l = new ArrayList();
                l.add(VBKeywords.VB_Xor);
		l.add(VBKeywords.VB_OrElse);
		l.add(VBKeywords.VB_Or);
                l.add(VBKeywords.VB_AndAlso);
		l.add(VBKeywords.VB_And);
                l.add(VBKeywords.VB_Not);
		return l;
	}

            
	public String translateLogical(String vbOperator) {
		if (vbOperator.equals(VBKeywords.VB_Not)) {
			return "!";
		} else if (vbOperator.equals(VBKeywords.VB_And)) {
			return "&";
		} else if (vbOperator.equals(VBKeywords.VB_AndAlso)) {
                        return "&&";
		} else if (vbOperator.equals(VBKeywords.VB_Or)) {
			return "|";
		} else if (vbOperator.equals(VBKeywords.VB_OrElse)) {
                        return "||";
		} else if (vbOperator.equals(VBKeywords.VB_Xor)) {
			return "^";
		} else {
			throw new RuntimeException("don't know how to translate " + vbOperator);
		}
	}


}

 