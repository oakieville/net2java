
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
import com.sun.dn.parser.statement.Assignment;
import com.sun.dn.parser.statement.CSComment;
	
	/** Factory class for creating Expression objects from CS expression strings
	** @author danny.coward@sun.com
	**/

public class CSExpressionFactory implements ExpressionFactory {
	
	public Expression getExpression(String code, InterpretationContext context) {
		Expression e =  CSExpressionFactory.getExpression(code, context, null, true);
		return e;
	}

	public Expression getExpression(String code, InterpretationContext context, boolean thrw) {
		return this.getExpression(code, context, null, thrw);
	}

	private static String getExpressionType(String type, String typeCharacter) {
		return type;
	}
        
        

        public static Expression getExpression(String s, InterpretationContext context, String type, boolean thrw) {   
        Debug.clogn("Get Expression for (raw) " + s, CSExpressionFactory.class);
                String code = Util.stripBrackets(s.trim());
                code = CSComment.stripComments(code, context);
                Debug.clogn("Get Expression for " + code, CSExpressionFactory.class);
                Expression e = null;
                try {
                    e = getExpressionPrivate(code, context, type, thrw);
                } catch (Throwable th) {
                    e = ExpressionAdapter.handleExpressionParseErrorReport(th, code, context); 
                    
                }
                
                Debug.clogn("Got " + e , CSExpressionFactory.class);
                return e;
        }
        
	private static Expression getExpressionPrivate(String code, InterpretationContext context, String type, boolean thrw) {
		
		//String typeCharacter = TypeCharacters.getTypeCharacter(code);
		//code = TypeCharacters.removeTypeCharacter(code);

		String expressionType = getExpressionType(type, "");
		if ("".equals(code)) {
			throw new RuntimeException("empty code");
		}
		if (false) {
			//
		} else if (NewExpression.isCSNewExpression(code, context)) {
                        Debug.clogn("its a newExpression", CSExpressionFactory.class);
			return NewExpression.createCSNewExpression(code, context);
                } else if (ArrayElementInitializer.isCSArrayElementInitializer(code, context)) {
                        return ArrayElementInitializer.createCSArrayElementInitializer(code, context);
                } else if (IndexExpression.isCSIndexExpression(code, context)) {
                        return IndexExpression.getCSIndexExpression(code, context);
                } else if (SizeOfExpression.isSizeOfExpression(code, context)) {
                        Debug.clogn("its a SizeOfExpression", CSExpressionFactory.class);
                        return SizeOfExpression.createSizeOfExpression(code, context);
                } else if (CSCrementExpression.isCSCrementExpression(code, context)) {
                        Debug.clogn("its a CSCrementExpression", CSExpressionFactory.class);
                        return CSCrementExpression.createCSCrementExpression(code, context);
                } else if (GetTypeExpression.isCSGetTypeExpression(code, context)) {
                        Debug.clogn("its a getTypeExpression", CSExpressionFactory.class);
			return GetTypeExpression.createCSGetTypeExpression(code, context);
		} else if (CSExpressionFactory.isBooleanExpression(code, context)) {
                        Debug.clogn("its a BooleanExpression", CSExpressionFactory.class);
			return CSExpressionFactory.getBooleanExpression(code, context);
		} else if (ConditionalOperatorExpression.isCSConditionalOperatorExpression(code, context)) {
                        Debug.clogn("its a ConditionalOperatorExpression", CSExpressionFactory.class);
			return ConditionalOperatorExpression.createCSConditionalOperatorExpression(code, context);
		} else if (TypeConversionExpression.isCSTypeConversionExpression(code, context)) {
                        Debug.clogn("its a TypeConversionExpression", CSExpressionFactory.class);
			return TypeConversionExpression.createCSTypeConversionExpression(code, context);
                } else if (RelationalOperatorExpression.isCSRelationalOperatorExpression(code, context)) {
                        Debug.clogn("its a RelationalOperatorExpression", CSExpressionFactory.class);
			return RelationalOperatorExpression.createCSRelationalOperatorExpression(code, context);
		} else if (BinaryOperatorExpression.isCSBinaryOperatorExpression(code, context)) {
                        Debug.clogn("its a BinaryOperatorExpression", CSExpressionFactory.class);
			return BinaryOperatorExpression.createCSBinaryOperatorExpression(code, context);
		} else if (Literal.getCSLiteral(code, context) != null) {
                        Debug.clogn("its a Literal", CSExpressionFactory.class);
			return Literal.getCSLiteral(code, context);
		} else if (MyBaseExpression.isCSBaseExpression (code, context)) {
                        Debug.clogn("its a MyBaseExpression", CSExpressionFactory.class);
			return new MyBaseExpression(code, context);
		} else if (LocalVariableExpression.isCSLocalVariable(code, context)) {
                        Debug.clogn("its a LocalVariableExpression", CSExpressionFactory.class);
			return LocalVariableExpression.createCSLocalVariableExpression(code, context);
		} else if (ClassExpression.isClassExpression(code, context)) {
                        Debug.clogn("its a ClassExpression", CSExpressionFactory.class);
			return new ClassExpression(code, context);
                } else if (Assignment.isCSAssignment(code, context)) {
                        Debug.clogn("Its an Assignment", CSExpressionFactory.class);
                        return Assignment.createCSAssignment(code, context);
                } else if (InvocationExpression.isVBInvocationExpression(code)) {
                        Debug.clogn("its a InvocationExpression", CSExpressionFactory.class);
			return InvocationExpression.createCSInvocationExpression(code, context);
		} else if (MemberAccessExpression.isCSMemberAccessExpression(code, context)) {
                        Debug.clogn("its a MemberAccessExpression", CSExpressionFactory.class);
			return MemberAccessExpression.createCSMemberAccessExpression(code, context);
		}
		if (thrw) {
                        Debug.clogn("couldn't make expression",  CSExpressionFactory.class);
			throw new RuntimeException("Can't make expression from " + code + " in " + context + " of type " + type);
		} else {
			return null;
		}
	}

	public static boolean isBooleanExpression(String code, InterpretationContext context) {
            Debug.clogn("is ." + code + ". a boolean expression ?", CSExpressionFactory.class);
		String strippedCode = Util.stripBrackets(code);
		if (strippedCode.equals(CSKeywords.CS_True) 
			|| strippedCode .equals(CSKeywords.CS_False)) {
                        Debug.clogn("yes its a boolean literal", CSExpressionFactory.class);
			return true;
		} else if (CSIsExpression.isCSIsExpression(code, context)) {
                    Debug.clogn("yes its an IsExpression", CSExpressionFactory.class);
                    return true;
                } 
                
                List codeTokens = Util.tokenizeIgnoringEnclosers(strippedCode, " ");
                String logicalOperator = (String) Util.listContains(codeTokens, (new CSOperators()).getLogicals());
                if (LogicalOperatorExpression.isCSLogicalOperatorExpression(strippedCode, logicalOperator, context)) {
                    Debug.clogn("yes its an LogicalOperatorExpression", CSExpressionFactory.class);
                    return true;
                }
                if (strippedCode.startsWith("!")) {
                    Debug.clogn("yes its a NOT expression", CSExpressionFactory.class);
                    return true;
                }
                DNVariable var = context.getVariable(code.trim());
                if (var != null) {
		if (var.getType().equals("bool") || var.getType().equals("Boolean")) {
                    return true;
		} else {
                    return false;
		}
            }
                Debug.clogn("No", CSExpressionFactory.class);
		return false;
	}

	public static BooleanExpression getBooleanExpression(String code, InterpretationContext context) {
		String strippedCode = Util.stripBrackets(code.trim());
		if (strippedCode.equals(CSKeywords.CS_True) || strippedCode .equals(CSKeywords.CS_False)) {
			return BooleanLiteral.createCSBooleanLiteral(strippedCode, context);
		}  else if (CSIsExpression.isCSIsExpression(code, context)) {
                    return CSIsExpression.createCSIsExpression(code, context);
		} 
                DNVariable var = context.getVariable(code.trim());
		if (var != null) {
			if (var.getType().equals("Boolean") || var.getType().equals("bool")) {
				return LocalVariableExpression.createCSLocalVariableExpression(var, context);
			} else {
				throw new RuntimeException("found variable by name but it isn't a Boolean");
			}
		}
                List codeTokens = Util.tokenizeIgnoringEnclosers(strippedCode, " ");
		String logicalOperator = (String) Util.listContains(codeTokens, (new CSOperators()).getLogicals());
                if (logicalOperator != null) {
                    return LogicalOperatorExpression.createCSLogicalOperatorExpression(strippedCode, context);
                }
                if (strippedCode.startsWith("!")) {
			UnaryLogicalNotExpression ulne = UnaryLogicalNotExpression.createCSUnaryLogicalNotExpression(code, context);
			return ulne;
                }
                throw new RuntimeException("Cannot make boolean expression for " + strippedCode);
                
	}

}
 