
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

    /** The C# language operators. */

public class CSOperators implements Operators {

	public List getRelationalOperators() {
		List l = new ArrayList();
		l.add("==");
		l.add("!=");
		l.add("<=");
		l.add(">=");
		l.add(">");
		l.add("<");
		return l;
	}
        
        public static List getUnaryOperators() {
            List l = new ArrayList();
		l.add("!");
		l.add("~");
		l.add("++");
		l.add("--");
                l.add("true");
                l.add("false");
		return l;
        }
        
         public static List getBinaryOperators() {
            List l = new ArrayList(); // don't change the order
                l.add("<=");
                l.add("<<");
                l.add(">>");
                l.add(">=");
                l.add("==");
                //l.add("=");
		l.add("*");
		l.add("/");
		l.add("%");
		l.add("|");
		l.add("^");
                l.add(">");
                l.add("<");
                l.add("+");
		l.add("-");
                l.add("&");
		return l;
        }
         
         public static String translateBinaryOperator(String s) {
            if (!getBinaryOperators().contains(s)) {
                throw new RuntimeException("I thought this was a unary operator: " + s);
            }
            if (s.equals("*")) {
                return "__star";
            } else if (s.equals("/")) {
                return "__slash";
            } else if (s.equals("%")) {
                return "__percent";
            } else if (s.equals("|")) {
                return "__bar";
            } else if (s.equals("^")) {
                return "__hat";
            } else if (s.equals("<<")) {
                return "__leftshift";
            } else if (s.equals(">>")) {
                return "__rightshift";
            } else if (s.equals("==")) {
                return "__equalsequals";
            } else if (s.equals("=")) {
                return "__equals";
            } else if (s.equals(">")) {
                return "__greater";
            } else if (s.equals("<")) {
                return "__less";
            } else if (s.equals(">=")) {
                return "__greaterorequal";
            } else if (s.equals("<=")) {
                return "__lessorequal";
            } 
            return null;
        }
        
        public static String translateUnaryOperator(String s) {
            if (!getUnaryOperators().contains(s)) {
                throw new RuntimeException("I thought this was a unary operator: " + s);
            }
            if (s.equals("+")) {
                return "__plus";
            } else if (s.equals("-")) {
                return "__minus";
            } else if (s.equals("!")) {
                return "__not";
            } else if (s.equals("~")) {
                return "__twiddle";
            } else if (s.equals("++")) {
                return "__plusplus";
            } else if (s.equals("--")) {
                return "__minusminus";
            } else if (s.equals("true")) {
                return "__true";
            } else if (s.equals("false")) {
                return "__false";
            } 
            return null;
        }

	public String translateComparison(String s) {
		if (!this.getRelationalOperators().contains(s)) {
			throw new RuntimeException("Can't translate " + s);
		}

		if (s.equals("!=")) {
                    return "!=";
		} else if (s.equals("==")) {
                    return "==";
		} else if (s.equals("<")) {		
                    return "<";
		} else if (s.equals(">")) {
                    return ">";
                } else if (s.equals(">=")) {
                    return ">=";
                } else if (s.equals("<=")) {
                    return "<=";
		} 
		throw new RuntimeException("Can't translate relational operator " + s);
	}
        
        
	public List getLogicals() {
		List l = new ArrayList();
                l.add("&&");
		l.add("&");
                l.add("||");
		l.add("|");
                l.add("^");
		return l;
	}
        
        public String translateLogical(String logicalOperator) {
            if (logicalOperator.equals("&") || logicalOperator.equals("&&")) {
                return logicalOperator;
            } else if (logicalOperator.equals("^")) {
                return "|";
            } else if (logicalOperator.equals("|") || logicalOperator.equals("||")) {
                return logicalOperator;
            }
            
            throw new RuntimeException("Cannot translate logical symbol: " + logicalOperator);
        }

	public List getAssignmentOperators() {
		List l = new ArrayList();
		l.add("+=");
		l.add("-=");
		l.add("*=");
		l.add("/=");
		l.add("%=");
		l.add("&=");
		l.add("|=");
		l.add("^=");
		l.add("<<=");
		l.add(">>=");
		l.add("=");
		return l;
	}

	public String translateAssignmentOperator(String assignmentString, String operator, String valueString) {
		String s = "";
		if (operator.equals("=")) {
			s = assignmentString + " = " + valueString + ";";
		} else if (operator.equals("+=")) {
			s = assignmentString + " = " + assignmentString + " + " + valueString + ";";
                } else if (operator.equals("-=")) {
			s = assignmentString + " = " + assignmentString + " - " + valueString  + ";";
                } else if (operator.equals("*=")) {
			s = assignmentString + " = " + assignmentString + " * " + valueString  + ";";
                } else if (operator.equals("/=")) {
			s = assignmentString + " = " + assignmentString + " / " + valueString  + ";";
                } else if (operator.equals("%=")) {
			s = assignmentString + " = " + assignmentString + " % " + valueString  + ";";
                } else if (operator.equals("&=")) {
			s = assignmentString + " = " + assignmentString + " & " + valueString  + ";";
                } else if (operator.equals("|=")) {
			s = assignmentString + " = " + assignmentString + " | " + valueString  + ";";
                } else if (operator.equals("|=")) {
			s = assignmentString + " = " + assignmentString + " | " + valueString + ";";
                } else if (operator.equals("^=")) {
			s = assignmentString + " = Math.pow(" + assignmentString + ", " + valueString + ");";
                } else if (operator.equals("<<=")) {
			s = assignmentString + " = " + assignmentString + " << " + valueString + ";";
                } else if (operator.equals(">>=")) {
			s = assignmentString + " = " + assignmentString + " >> " + valueString + ";";
		} else {
			throw new RuntimeException("Don't know the Java for the operator: ." + operator + ".");
		}
		return s;
	}

        public boolean isDelegateAssignmentOperator(String op) { 
            return isDelegateAddOperator(op) || isDelegateRemoveOperator(op); 
        }
        
        public static boolean isDelegateAddOperator(String op) {
            return "+=".equals(op);
        
        }
        
        public static boolean isDelegateRemoveOperator(String op) {
            return "-=".equals(op);
        
        }

}

 