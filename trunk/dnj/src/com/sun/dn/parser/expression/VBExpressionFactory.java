
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
	
	/** Factory class for creating Expression objects from VB expression strings
	** @author danny.coward@sun.com
	**/

public class VBExpressionFactory implements ExpressionFactory {
	
	public Expression getExpression(String code, InterpretationContext context) {
            Expression e =  VBExpressionFactory.getExpression(code, context, null, true);
		return e;
	}

	public Expression getExpression(String code, InterpretationContext context, boolean thrw) {
		return this.getExpression(code, context, null, thrw);
	}

	private static String getExpressionType(String type, String typeCharacter) {
            
		String expressionType = null;
		if (type == null && typeCharacter == null) {
			expressionType = null;
		} else if (type != null && typeCharacter == null) {
			expressionType = type;
		} else if (type == null && typeCharacter != null) {
			expressionType = TypeCharacters.getType(typeCharacter);
		} else if (type != null && typeCharacter != null) {
			if (type.equals( TypeCharacters.getType(typeCharacter) ) ) {
				expressionType = type;
                        } else if (DateLiteral.matchesTypeChar(type, typeCharacter)) {
                            return expressionType;
			} else {
				throw new RuntimeException("Types do not agree. Passed type = " + type + " and TypeChar = " + typeCharacter);
			}
		}
		return expressionType;
	}
        
        public static Expression getExpression(String s, InterpretationContext context, String type, boolean thrw) {
            //Debug.clogn("Get Expression for ." + s + ".", VBExpressionFactory.class);
            long l = (new Date()).getTime();
            Expression e = null; 
            try {
                e = getExpressionPrivate(s, context, type, thrw);
            } catch (Throwable th) {
                e = ExpressionAdapter.handleExpressionParseErrorReport(th, s, context);
            }
            Debug.clogn("-got " + ( (new Date()).getTime() -l) + "ms " + e,  VBExpressionFactory.class);
            return e;
        }

	private static Expression getExpressionPrivate(String s, InterpretationContext context, String type, boolean thrw) {
                Debug.clogn("Get Expression for ." + s + ".", VBExpressionFactory.class);
		String code = Util.stripBrackets(s.trim());
		String typeCharacter = TypeCharacters.getTypeCharacter(code);
		code = TypeCharacters.removeTypeCharacter(code);

		String expressionType = getExpressionType(type, typeCharacter);
		
		
		if ("".equals(code)) {
                    //return null;
                    throw new RuntimeException("empty code");
		}
		if (NewExpression.isVBNewExpression(code, context)) {
			return NewExpression.createVBNewExpression(code, context);
                } else if (AddressOfExpression.isAddressOfExpression(code, context)) {
			return AddressOfExpression.createAddressOfExpression(code, context);
                } else if (OfExpression.isVBOfExpression(code, context)) {
			return OfExpression.createVBOfExpression(code, context);
		} else if (Literal.getVBLiteral(expressionType, code, context) != null) {
			return Literal.getVBLiteral(expressionType, code, context);
		} else if (ArrayElementInitializer.isVBArrayElementInitializer(code, context)) {
			return ArrayElementInitializer.createVBArrayElementInitializer(code, context);
		} else if (BinaryOperatorExpression.isVBBinaryOperatorExpression(code, context)) {
			return BinaryOperatorExpression.createVBBinaryOperatorExpression(code, context);
		} else if (IndexExpression.isVBIndexExpression(code, context)) {
			return IndexExpression.getVBIndexExpression(code, context);
		} else if (VBExpressionFactory.isBooleanExpression(code, context)) {
			return VBExpressionFactory.getBooleanExpression(code, context);
		} else if (GetTypeExpression.isVBGetTypeExpression(code, context)) {
			return GetTypeExpression.createVBGetTypeExpression(code, context);
		} else if (InvocationExpression.isVBInvocationExpression(code)) {
			return InvocationExpression.createVBInvocationExpression(code, context);
		} else if (LocalVariableExpression.isVBLocalVariable(code, context)) {
			return LocalVariableExpression.createVBLocalVariableExpression(code, context);
		} else if (ClassExpression.isClassExpression(code, context)) {
			return new ClassExpression(code, context);
		} else if (MyBaseExpression.isVBMyBaseExpression(code, context)) {
			return new MyBaseExpression(code, context);
		} else if (VBMeExpression.isMeExpression(code, context)) {
			return new VBMeExpression(code, context);
		} else if (ImpliedExpression.isImpliedExpression(code, context)) {
			return new ImpliedExpression(code, context);
                } else if (TypeOfIsOperatorExpression.isTypeOfIsOperatorExpression(code, context)) {
                     return TypeOfIsOperatorExpression.createTypeOfIsOperatorExpression(code, context);
                } else if (MyClassExpression.isVBMyClassExpression(code, context)) {
                      return new MyClassExpression(code, context);
		} else if (MemberAccessExpression.isVBMemberAccessExpression(code, context)) {
			return MemberAccessExpression.createVBMemberAccessExpression(code, context);
		} 
		if (thrw) {
                    throw new RuntimeException("I don't know what kind of expression [ " + code + " ] is.");
		} 
                return UntranslatedExpression.createUntranslatedExpression(code, context);
	}

	public static boolean isBooleanExpression(String code, InterpretationContext context) {
            String strippedCode = Util.stripBrackets(code);
            List codeTokens = Util.tokenizeIgnoringEnclosers(strippedCode, " ");
            
            if (strippedCode.equals(VBKeywords.VB_True) || strippedCode.equals(VBKeywords.VB_False)) {
		return true;
            } 
            if (strippedCode.startsWith(VBKeywords.VB_Not)) {
		return true;
            }
            String logicalOperator = (String) Util.listContains(codeTokens, (new VBOperators()).getLogicals());
            String relationalOperator = (String) Util.listContains(codeTokens, (new VBOperators()).getRelationalOperators());
            if (logicalOperator != null || relationalOperator != null) {
            	return true;
            }
            DNVariable var = context.getVariable(code.trim());
            if (var != null) {
		if (var.getType().equals("Boolean")) {
                    return true;
		} else {
                    return false;
		}
            }
            if (IsOrIsNotExpression.isVBIsOrIsNotExpression(code, context)) {
		return true;
            }
            return false;
	}

	public static BooleanExpression getBooleanExpression(String code, InterpretationContext context) {
		String strippedCode = Util.stripBrackets(code.trim());
		if (strippedCode.equals(VBKeywords.VB_True) || strippedCode.equals(VBKeywords.VB_False)) {
			return (BooleanExpression) Literal.getVBLiteral(strippedCode, context); 
		}
		DNVariable var = context.getVariable(code.trim());
		if (var != null) {
			if (var.getType().equals("Boolean")) {
				return LocalVariableExpression.createVBLocalVariableExpression(var, context);
			} else {
				throw new RuntimeException("found variable by name but it isn't a Boolean");
			}
		}

		
		List codeTokens = Util.tokenizeIgnoringEnclosers(strippedCode, " ");

		String logicalOperator = (String) Util.listContains(codeTokens, (new VBOperators()).getLogicals());

		if (strippedCode.startsWith(VBKeywords.VB_Not)) {
			UnaryLogicalNotExpression une = UnaryLogicalNotExpression.createVBUnaryLogicalNotExpression(code, context);
			return une;
		} else if (logicalOperator != null) {
			LogicalOperatorExpression cbe = LogicalOperatorExpression.createVBLogicalOperatorExpression(code, context);
			return cbe;
		} else if (IsOrIsNotExpression.isVBIsOrIsNotExpression(code, context)) {
			IsOrIsNotExpression ie = IsOrIsNotExpression.createVBIsOrIsNotExpression(code, context);
			return ie;
		} else {			
			RelationalOperatorExpression se = RelationalOperatorExpression.createVBRelationalOperatorExpression(code, context);
			return se;
		}
	}


}
 