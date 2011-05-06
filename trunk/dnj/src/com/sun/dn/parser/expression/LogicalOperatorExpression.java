
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
import com.sun.dn.*;
import com.sun.dn.util.*;

	/** A .NET expression that is the result of the logical
	** combination of two other expressions. Defined as <br>
	LogicalOperatorExpression ::= <br>
	Expression And Expression | <br>
	Expression Or Expression | <br>
	Expression Xor Expression | <br>
	Expression AndAlso Expression | <br>
	Expression OrElse Expression  <br>
	@author danny.coward@sun.com
	*/

public class LogicalOperatorExpression extends ExpressionAdapter implements BooleanExpression {
	private String logicalOperator;
        private Operators operators;
	Expression lE;
	Expression rE;
        
        public static LogicalOperatorExpression createVBLogicalOperatorExpression(String code, InterpretationContext context) {
            LogicalOperatorExpression loe = new LogicalOperatorExpression(code, context);
            loe.parse(code, new VBOperators(), new VBExpressionFactory());
            return loe;
        }
        
         public static LogicalOperatorExpression createCSLogicalOperatorExpression(String code, InterpretationContext context) {
            LogicalOperatorExpression loe = new LogicalOperatorExpression(code, context);
            loe.parse(code, new CSOperators(), new CSExpressionFactory());
            return loe;
        }

	private LogicalOperatorExpression(String code, InterpretationContext context) {
            super(code, context);
	}
        
        public static boolean isCSLogicalOperatorExpression(String code, String operator, InterpretationContext context) {
            Debug.clogn("is ." + code + " a logical operator expression ? (" + operator + ")", LogicalOperatorExpression.class);
            if (operator == null) {
                Debug.clogn("no", LogicalOperatorExpression.class);
                return false;
            } 
            List l = Util.tokenizeIgnoringEnclosers(code, operator);
            String lhs = (String) l.get(0);
            String rhs = (String) l.get(1);
            Expression lE = (new CSExpressionFactory()).getExpression(lhs.trim(), context);
            Expression rE = (new CSExpressionFactory()).getExpression(rhs.trim(), context);
            boolean is =  isBoolean(lE) && isBoolean(rE);
            Debug.clogn("is it ? " + is, LogicalOperatorExpression.class);
            return is;
        }
        
        public static boolean isBoolean(Expression exp) {
            return DNType.shortNameEquivalent(exp.getTypeName(), "Boolean") || DNType.shortNameEquivalent(exp.getTypeName(), "bool");
        }

	public DNType getDNType() {
		return context.getLibrary().getLibraryData().getLibraryClass(this.getTypeName());
	}

	public String getTypeName() {
		return "System.Boolean";
	}
        
        

	private void parse(String s, Operators operators, ExpressionFactory factory) {
            Debug.logn("Parse " + s, this);
	    this.operators = operators;
            String code = Util.stripBrackets(s);
            List tokens = null;
            boolean found = false;
            for (Iterator itr = operators.getLogicals().iterator(); itr.hasNext();) {
		String nextOperator = (String) itr.next();
                //Debug.logn("Next operator " + nextOperator, this);
		List l = Util.tokenizeIgnoringEnclosers(code, nextOperator);
                
		if (l.size() > 1 && !found) {
                    logicalOperator = nextOperator;
                    tokens = l;
                    found = true;
          
		}
            }
            Debug.logn("Operator= " + logicalOperator, this);
            if (logicalOperator == null) {
		throw new RuntimeException("Null operator in " + code);
            }

		// need to respect the brackets !!

            String lhs = (String) tokens.get(0);
            String rhs = (String) tokens.get(1);

            Debug.logn("Left= " + lhs, this);
            Debug.logn("Right= " + rhs, this);

		
            lE = factory.getExpression(lhs.trim(), this.context);
            rE = factory.getExpression(rhs.trim(), this.context);

            Debug.logn("" + lE, this);
            Debug.logn("" + rE, this);
	}

	public String tryAsJava() {
            return "( " + lE.asJava() + " " + operators.translateLogical(logicalOperator) + " " + rE.asJava() + " )";
	}

}
 