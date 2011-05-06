
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
package com.sun.dn.parser.statement;

import java.util.*;
import com.sun.dn.parser.expression.*;
import com.sun.dn.*;
import com.sun.dn.java.*;
import com.sun.dn.parser.*;
import com.sun.dn.util.*;

	/**
	**  A .NET statement that is a subroutine or function call.
	@author danny.coward@sun.com
	*/

public class MethodCall extends StatementAdapter {
	protected String name;
	protected List parameters = new ArrayList();
	public static String ACE_VARIABLE_NAME = "__aceVariableName";
	private static int aceCounter = 0;

	protected MethodCall(String code, InterpretationContext context) {
		super(code, context);
	}

	public MethodCall(InterpretationContext context, String name, String dnCode) {
		this(dnCode, context);
		this.name = name;
	}

	public static MethodCall createVBMethodCall(String code, InterpretationContext context) {
		if (TypeConversionExpression.isVBTypeConversionExpression(code, context)) {
			return TypeConversionExpression.createVBTypeConversionExpression(code, context);
		} else {
			MethodCall mc = new MethodCall(code, context);
			mc.parse(code, new VBExpressionFactory());
			return mc;
		}
	}

	public static MethodCall createCSMethodCall(String code, InterpretationContext context) {
		MethodCall mc = new MethodCall(code, context);
		mc.parse(code, new CSExpressionFactory());
		return mc;
	}

	
	public void setName(String name, MethodCall setter) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public List getParameters() {
		return this.parameters;
	}

	public boolean isNew() {
		return this.name.equals(VBKeywords.VB_New);
	}
	
	private void parse(String fragment, ExpressionFactory expressionFactory) {
		Debug.logn("Parse Method Call " + fragment, this);
		this.name = fragment.substring(0, fragment.indexOf("(")).trim();			
		String paramString = fragment.substring(1 + fragment.indexOf("("), fragment.lastIndexOf(")"));
		Debug.logn("-Params " + paramString, this);	
		this.parseParamStrings(paramString, expressionFactory);
	}

	protected void parseParamStrings(String paramString, ExpressionFactory expressionFactory ) {
            List paramStrings = Util.tokenizeIgnoringEnclosers(paramString, ',');
            for (Iterator itr = paramStrings.iterator(); itr.hasNext();) {
                String nextParam = ((String) itr.next()).trim();
                if (!(nextParam.equals(""))) {
                   Debug.logn("Find match for param " + nextParam, this);
                    if (CallExpression.isVariable(this.context, nextParam )) {
                        DNVariable v = CallExpression.getVariable(this.context, nextParam);
                        parameters.add(v);
                    } else if (expressionFactory.getExpression(nextParam, this.context) != null) {
                        
                        parameters.add(expressionFactory.getExpression(nextParam, this.context));
                    }
                }
            }
	}

	private static List createVBArrayCreationDeclatationsAndSubstituteParams(List oldParameters, List newParameters, InterpretationContext context) {
		List arrayCreationDeclarations = new ArrayList();
		String s = ACE_VARIABLE_NAME;
		for (Iterator itr = oldParameters.iterator(); itr.hasNext();) {
			Object o = itr.next();
			if (o instanceof ArrayCreationExpression) {
				ArrayCreationExpression ace = (ArrayCreationExpression) o;
				LocalVariableDeclaration ds = LocalVariableDeclaration.createVBLocalVariableDeclaration(context, ace, s+aceCounter);
				arrayCreationDeclarations .add(ds);
				newParameters.add(DNVariable.createVBVariable(s+aceCounter, ace.getName() + "()"));
				aceCounter++;
			} else {
				newParameters.add(o);
			}
		}
		return arrayCreationDeclarations ;
	}

	public static List createCSArrayCreationDeclarationsAndSubstituteParams(List oldParameters, List newParameters, InterpretationContext context) {
		List arrayCreationDeclarations = new ArrayList();
		String s = ACE_VARIABLE_NAME;
		for (Iterator itr = oldParameters.iterator(); itr.hasNext();) {
			Object o = itr.next();
			if (o instanceof ArrayCreationExpression) {
				ArrayCreationExpression ace = (ArrayCreationExpression) o;
				LocalVariableDeclaration ds = LocalVariableDeclaration.createCSLocalVariableDeclaration(context, ace, s+aceCounter);
				arrayCreationDeclarations .add(ds);
                                DNVariable var = DNVariable.createCSVariable(s+aceCounter, ace.getName() + "[]");
                                
                                newParameters.add(LocalVariableExpression.createCSLocalVariableExpression(var, context));
				
				aceCounter++;
			} else {
				newParameters.add(o);
			}
		}
		return arrayCreationDeclarations ;
	}


	public List replaceVBLiteralArrayParamaters(InvocationExpression ie) {
		List newParameters = new ArrayList();
		List arrayCreationDeclarations = createVBArrayCreationDeclatationsAndSubstituteParams(this.getParameters(), newParameters, ie.getContext());
		this.parameters = newParameters;
		return arrayCreationDeclarations;
	}

	public String tryAsJava() {
		String s = this.name + "(";
		s = s + this.paramsAsJava();
		s = s + ")"; 
		return s;	
	}
        
        public String asJava() {
            try {
                return this.tryAsJava();
            } catch (Throwable t) {
                ParseTree pt = ParseTree.getParseTree(this.context);
                pt.handleTypeResolveException(pt, this.getOriginalCode(), t, false);
                return UntranslatedExpression.createUntranslatedExpression(this.getOriginalCode(), context).asJava();
            }
        }

	public String paramsAsJava() {
		String s = "";
		for (Iterator itr = this.parameters.iterator(); itr.hasNext();) {
			Object o = itr.next();
			if (o instanceof DNVariable) {
				DNVariable var = (DNVariable) o;
				s = s + var.getName();
			} else if (o instanceof Expression) {
				s = s + ((Expression) o).asJava();
			} else {
				throw new RuntimeException("Something funny in the params " + parameters);
			}
			if (itr.hasNext()) {
				s = s + ", ";
			}
		}
		return s;
	}

	public String toString() {
		return "MethodCall: name-" + name + " params-" + parameters;
	}
}
 