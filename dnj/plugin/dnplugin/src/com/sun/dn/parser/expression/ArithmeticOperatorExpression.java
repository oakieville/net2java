
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
import com.sun.dn.util.*;

import com.sun.dn.parser.statement.*;


	/** A VB expression on numbers. Defined as <br>
	ArithmeticOperatorExpression ::= <br>
	AdditionOperatorExpression | <br>
	SubtractionOperatorExpression | <br>
	MultiplicationOperatorExpression | <br>
	DivisionOperatorExpression | <br>
	ModuloOperatorExpression | <br>
	ExponentOperatorExpression <br>
		<br>

	DivisionOperatorExpression ::= <br>
	RegularDivisionOperatorExpression | <br>
	IntegerDivisionOperatorExpression <br>
	RegularDivisionOperatorExpression ::= Expression / Expression <br>
	IntegerDivisionOperatorExpression ::= Expression \ Expression <br>

	@author danny.coward@sun.com
	*/

public class ArithmeticOperatorExpression extends BinaryOperatorExpression {
	public static String Addition = "Addition";
	public static String Subtraction = "Subtraction";
	public static String Multiplication = "Multiplication";
	public static String RegularDivision = "RegularDivision";
	public static String ModuloDivision = "ModuloDivision";
	public static String Modulo = "Modulo";
	public static String Exponent = "Exponent";
        public static String LeftShift = "LeftShift";
        public static String RightShift = "RightShift";
        private OperatorStatement os;
	private String op;
        
        public static ArithmeticOperatorExpression createVBArithmeticOperatorExpression(String code, InterpretationContext context, String op) {
            ArithmeticOperatorExpression aos = new ArithmeticOperatorExpression(code, context);
            aos.context = context;
            aos.op = op;
            aos.parseVB(code, op);
            return aos;
        }
        
        public static ArithmeticOperatorExpression createCSArithmeticOperatorExpression(String dnCode, Expression left, Expression right, String op, InterpretationContext context) {
            ArithmeticOperatorExpression aos = new ArithmeticOperatorExpression(dnCode, context);
            aos.context = context;
            aos.op = op;
            aos.leftExpression = left;
            aos.rightExpression = right;
            return aos;
        }

	private ArithmeticOperatorExpression(String code, InterpretationContext context){
		super(code, context);
	}
        
        public String getOperator() {
            return op;
            
        }

	

	public DNType getDNType() {
		return this.context.getLibrary().getLibraryData().getLibraryClass(this.getTypeName());
	}
        
        private OperatorStatement getCSProgramDefinedOperator() {
            if (this.os == null) {
                ParseTree pt = ParseTree.getParseTree(context);
                for (Iterator i = pt.getMetaClasses().iterator(); i.hasNext();) {
                    Object o = i.next();
                    if (o instanceof MetaClass) {
                        MetaClass mc = (MetaClass) o;
                        List opStatements = mc.getOperatorStatements(); 
                        //System.out.println(opStatements);
                        for (Iterator itr = opStatements.iterator(); itr.hasNext();) {
                            OperatorStatement tryOs = (OperatorStatement) itr.next();
                            if (tryOs.matchesBinaryOperatorExpression(this)) {
                            
                                this.os = tryOs;
                            }
                        }
                    }
                }
            }
            return this.os;
        }

	public String getTypeName() { 
          
           if (this.getCSProgramDefinedOperator() != null) {
               return this.getCSProgramDefinedOperator().getReturnType();
           }
            
           if (op.equals("+")) {
		return this.getPlusMinusMultiplyType();
            } else if (op.equals("-")) {
            	return  this.getPlusMinusMultiplyType();
            } else if (op.equals("*")) {
            	return  this.getPlusMinusMultiplyType();
            } else if (op.equals("\\")) {
            	return "Integer";
            } else if (op.equals("/")) {
            	return "Integer";                  
            } else if (op.equals(VBKeywords.VB_Mod)) {
            	return "Integer";
            } else if (op.equals("^")) {
            	return "Double";
            } else if (op.equals("%")) {
               return "Integer";
            } else if (op.equals("<<")) {
               return "Integer";
            } else if (op.equals(">>")) {
               return "Integer";
            } else {
            	throw new ParserException("don't know type of " + op);
            }
	}

	private String error() {
		return "ArithmeticOperatorExpression: Need to implement getType() for op " + leftExpression.getTypeName() +  " " + op + " " + rightExpression.getTypeName();
	}

	private String getPlusMinusMultiplyType() {
		if (BinaryOperatorExpression.isIntegerNumeric(leftExpression) && 
				BinaryOperatorExpression.isIntegerNumeric(rightExpression)) {
			return "Integer";
		} else if (DNType.shortNameEquivalent(leftExpression.getTypeName(), "String") && 
			DNType.shortNameEquivalent(rightExpression.getTypeName(), "String") ) {
			return "String";
		} else if (DNType.shortNameEquivalent(leftExpression.getTypeName(), "Double") && 
			DNType.shortNameEquivalent(rightExpression.getTypeName(), "Double") ) {
			return "Double";

 		} else {
			throw new ParserException(this.error());
		}
	}

	private String getOperatorType() {
		if (op.equals("+")) {
			return Addition;
		} else if (op.equals("-")) {
			return Subtraction;
		} else if (op.equals("*")) {
			return Multiplication;
		} else if (op.equals("\\")) {
			return RegularDivision ;
		} else if (op.equals("/")) {
			return ModuloDivision;
		} else if (op.equals(VBKeywords.VB_Mod)) {
			return Modulo;
                } else if (op.equals("%")) {
                        return Modulo;
		} else if (op.equals("^")) {
			return Exponent;
                } else if (op.equals("<<")) {
                    return LeftShift;
                } else if (op.equals(">>")) {
                    return RightShift;
		} else {
			throw new RuntimeException("don't know type of ." + op + ".");
		}
	}

	private String getRegularDivisionAsJava() {
		String x = leftExpression.asJava();
		String y = rightExpression.asJava();
		return "(int) Math.floor( " + x + " / " + y + " )"; 
	}

	private String getModuloDivisionAsJava() {
		String x = leftExpression.asJava();
		String y = rightExpression.asJava();
		return "(int) ( (double)" + x + " / " + y + " )";
	}

	private String getExponentAsJava() {
		String x = leftExpression.asJava();
		String y = rightExpression.asJava();
		return "Math.pow( (double) " + x + ", (double) " + y + ")";
	}
	
    

	public String tryAsJava() {
            
                if (this.getCSProgramDefinedOperator() != null) {
                    String s = os.getMetaClass().getName() + "." + os.getJavaName();
                    s = s + "(" + leftExpression.asJava() + ", " + rightExpression.asJava()+ " )";
                    return s;
                }
                
		if (this.getOperatorType().equals(Addition)) {
			return leftExpression.asJava() + " + " + rightExpression.asJava();
		} else if (this.getOperatorType().equals(Subtraction)) {
			return leftExpression.asJava() + " - " + rightExpression.asJava();
		} else if (this.getOperatorType().equals(Multiplication)) {
			return leftExpression.asJava() + " * " + rightExpression.asJava();
		} else if (this.getOperatorType().equals(RegularDivision)) {
			return this.getRegularDivisionAsJava();
		} else if (this.getOperatorType().equals(ModuloDivision)) {
			return this.getModuloDivisionAsJava();
		} else if (this.getOperatorType().equals(Exponent)) { 
			return this.getExponentAsJava();
		} else if (this.getOperatorType().equals(Modulo)) { 
			return leftExpression.asJava() + " % " + rightExpression.asJava();
                } else if (this.getOperatorType().equals(LeftShift)) {
                        return  leftExpression.asJava() + " << " + rightExpression.asJava();
                } else if (this.getOperatorType().equals(RightShift)) {
                        return  leftExpression.asJava() + " >> " + rightExpression.asJava();
		} else {
			throw new RuntimeException("Not Implemented");
		}

	}

	public String toString() {
		return "ArithOpExp: " + leftExpression + " " + this.getOperator() + " " + rightExpression;

	}

	

}
 