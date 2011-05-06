
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

import com.sun.dn.library.JavaExpression;
import java.util.*;
import com.sun.dn.parser.*;
import com.sun.dn.*;
import com.sun.dn.java.*;
import com.sun.dn.parser.*;
import com.sun.dn.parser.statement.*;
import com.sun.dn.util.*;
import com.sun.dn.library.LibraryData;

	/**
	A .NET invocation expression consists of an invocation target, 
	followed by an opening parenthesis, an optional argument list, 
	and a closing parenthesis. <br>

	target(para1, para2, para3)  <br>

	InvocationExpression ::= InvocationTargetExpression [ ( [ ArgumentList ] ) ]  <br>
	InvocationTargetExpression ::=  <br>
	DelegateExpression | <br>
	[ [ Expression ] . ] IdentifierOrKeyword | <br>
	MyClass . IdentifierOrKeyword | <br>
	MyBase . IdentifierOrKeyword | <br>
	MethodMemberName <br>
	DelegateExpression ::= Expression <br>

	foo().baa().something()  <br>

	this is an Invocation of something() on [foo().baa()]  <br>
	which is an Invocation of something() on an Invocation of baa() on foo()  <br>
	which is an Invocation of something() on an Invocationof baa() on an Invocation of foo() on Me.  <br>

	foo(baa())  <br>

	is an invocation of foo() on Me with parameter and Invocation baa() on Me  <br>
	@author danny.coward@sun.com

	**/

public class InvocationExpression extends ExpressionAdapter implements HasConstructedStatements {
	private Expression target;
	private MethodCall methodCall;
	
	private ParseTree parseTree;
	private MetaClass metaClass;
	private String type;
	private DNType dnType;
	private List constructedPreStatements = new ArrayList();

	private InvocationExpression(String code, InterpretationContext context) {
            super(code, context);
            
            if (metaClass == null) {
		metaClass = context.getMetaClass();
            }
	}

	public InvocationExpression(MemberAccessExpression mae) {
		this(mae.getOriginalCode(), mae.getContext());
		this.target = mae.getExpression();
		this.methodCall = new MethodCall(this.context, mae.getVariableMemberName(), mae.getOriginalCode());
	}
        
        public static InvocationExpression createVBInvocationExpression(String code, InterpretationContext context) {
            InvocationExpression ie = new InvocationExpression(code, context);
            ie.parseVB(code);
            return ie;
        }
        
        public static InvocationExpression createCSInvocationExpression(String code, InterpretationContext context) {
            InvocationExpression ie = new InvocationExpression(code, context);
            ie.parseCS(code);
            return ie;
        }

	private void parseVB(String code) {

		Debug.logn("Parse Invocation " + code, this);
		String methodBit = "";
		String targetBit = "";
		List l = Util.tokenizeIgnoringEnclosers(code, '.');
		//Debug.logn(l, this);
		// take the last method call and its parameters
		// the rest of the items preceeding in the list are another Expression
		String lastMethodCall = (String) l.get(l.size() -1);
		Debug.logn("Last method call is " + lastMethodCall, this);

		this.methodCall = MethodCall.createVBMethodCall(lastMethodCall, this.context);

		Debug.logn("Method call = " + this.methodCall, this);

		List allButLast = new ArrayList();
		for (Iterator itr = l.iterator(); itr.hasNext();) {
			String next = (String) itr.next();
			if (itr.hasNext()) {
				allButLast.add(next);
			}
		}
		String expression = this.putBack(allButLast);
		if ("".equals(expression)) {
			// this can happen in 3 cases that I know of:
			// 1)TypeConversions
			// 2) When the method call is a function on Me (or a superclass)
			// 3) When the method call is a function call on a runtime class. Like MsgBox("Hello")
			// sigh
			// any which way, I'll have to model it as if
			// it is a method call on Me, since I don't know
			// until I look in the library whether its on Me
			// or from a Runtime class like Interaction.
			if (methodCall instanceof TypeConversionExpression) {
				target = null;
			} else {
                           target = new VBMeExpression("<The parser created this expression from the empty string because of the context>", this.context); 
			}
		} else { // what if its a Variable !!!!!
			target = (new VBExpressionFactory()).getExpression(expression, this.context);
		}
		this.constructedPreStatements = this.methodCall.replaceVBLiteralArrayParamaters(this);
		Debug.logn("constructedPreStatements  " + constructedPreStatements , this);

		Debug.logn("Expression = " + target, this);
	}

	private void parseCS(String code) {
		Debug.logn("Parse Invocation " + code, this);
		String methodBit = "";
		String targetBit = "";
		List l = Util.tokenizeIgnoringEnclosers(code, '.');
		//Debug.logn(l, this);
		// take the last method call and its parameters
		// the rest of the items preceeding in the list are another Expression
		String lastMethodCall = (String) l.get(l.size() -1);
		Debug.logn("Last method call is " + lastMethodCall, this);

		this.methodCall = MethodCall.createCSMethodCall(lastMethodCall, this.context);

		Debug.logn("Method call = " + this.methodCall, this);

		List allButLast = new ArrayList();
		for (Iterator itr = l.iterator(); itr.hasNext();) {
			String next = (String) itr.next();
			if (itr.hasNext()) {
				allButLast.add(next);
			}
		}
		String targetExpression = this.putBack(allButLast).trim();
		Debug.logn(" Target Expression is = " + targetExpression, this);

		if ("".equals(targetExpression)) {
                    
                    target = new CSThisExpression("<no code>", this.context);
                    
		} else {
			target = (new CSExpressionFactory()).getExpression(targetExpression , this.context);
		}
		Debug.logn("Expression = " + target, this);
		
	}
        
        private MemberStatement getMemberStatement(InterpretationContext context) {
            boolean reachedIt = false;
            InterpretationContext ctxt = context;
            while (!reachedIt && ctxt != null) {
                if (ctxt instanceof MemberStatement) {
                    return (MemberStatement) ctxt;
                } else {
                    ctxt = context.getParent();
                }
            }
            return null;
        }

	public List getConstructedPreStatements() {
		return this.constructedPreStatements;
	}

	public List getArgs() {
		return this.methodCall.getParameters();
	}

	public String getMethodName() {
		return this.methodCall.getName();
	}

	private Library getLibrary() {
            return this.context.getLibrary();
	}

	public InterpretationContext getContext() {
		return this.context;
	}

	private String putBack(List l) {
		String s = "";
		for (Iterator itr = l.iterator(); itr.hasNext();) {
			String next = (String) itr.next();
			if (itr.hasNext()) {
				s = s + next + ".";
			} else {
				s = s + next;
			}
		}
		return s;
	}

	public static boolean isVBInvocationExpression(String code) {
		return code.endsWith(")");
	}

	public static boolean isCSInvocationExpression(String code) {
		return code.endsWith(")");
	}

	private boolean isMethod(String codeFragment) {
		return (codeFragment.indexOf("(") != -1);
	}

	public Expression getTarget() {
		if (target == null) {
			return new VBNothing("<The parser created this because of the context>", context);
		}
		return target;
	}





	public String tryAsJava() {
            // ok I'm going to cheat a little here with this.foo()
            // we don't know whether I am calling a class or instance methof
            // so I'll cheat by not using this at all.
            Debug.logn("****AS JAVA [" + this.getOriginalCode() + "]", this);
            DNType t = this.getDNType(); // make sure this has been called
            Debug.logn("****t " + t + " isNull" + t.isNull(), this);
            if (t.isUnknown() || t.isNull()) {
                
                return UntranslatedExpression.createUntranslatedExpression(this.getOriginalCode(), context).asJava();
            }
            
            JavaExpression je = this.getLibrary().getTranslationForInvocation(this);
            
            if (je != null && this.context.getMetaClass().resolveMethodOnMe(this) == null) {
                 Debug.logn("***There's something in the library " + je, this);
		
		MetaClass myMetaClass = metaClass;
		Debug.logn(" adding import strings " + je.getImportStrings(), this);
		myMetaClass.addJavaImports(je.getImportStrings());
		return (new JavaExpressionMatcher(je, this.getLibrary())).getJava(this); 
            } else if (target instanceof VBMeExpression || target instanceof CSThisExpression) {
		return this.methodCall.asJava();
            } else if (target instanceof MyBaseExpression && this.methodCall.isNew()) {
		return JavaKeywords.J_SUPER + "(" + methodCall.paramsAsJava() + ")";
            } else if (target == null) {
            	return this.methodCall.asJava();
            } else {
		return this.target.asJava() + "." + this.methodCall.asJava();
            }
	}

	public String asJava(boolean usePrimitives) {
		if (usePrimitives) {
			return asJava();
		} else {
			return JavaPrimitives.primitiveToClass(this.asJava(), getLibrary().getJavaTypeFor(this.getTypeName()));
		}
	}

	public String getType() {
            Debug.logn("Get type on " + this, this);
		
            if (type == null) {
		if (this.methodCall instanceof TypeConversionExpression) {
                    type = ((TypeConversionExpression) this.methodCall).getTypeName();
                    Debug.logn(" (gettype) Found:  I'm a type conversion " + type, this);
                    return type;
                }
		Signature s = null;
		s = metaClass.resolveMethodOnMe(this);
		if (s == null) {
                    Debug.logn(" (gettype) can't find resolution on metaclass ", this);
                    //System.out.println("resolveMethod on " + this.getTarget().getDNType() + " of name " + this.getMethodName());
                    s = this.getParseTree().resolveMethod(this.getTarget().getDNType(), this.getMethodName(), this.getArgs());
                    if (s == null) {
			Debug.logn(" (gettype) can't find resolution on parse tree ", this);
			s = this.getLibrary().getSignatureFor(this);
                    }
                    Debug.logn(" (gettype) looked for sig and got " + s, this);
                    
                    
                    
                    if (s != null && ((type = s.getReturnType()) != null)) {
                        Debug.logn(" Finish (gettype)", this);
			return type;
                    } else {
                        Debug.logn("Return unknown", this);
                        ParseTree pt = ParseTree.getParseTree(this.context);
                        pt.handleTypeResolveException(pt, this.getOriginalCode(), new Throwable("Unknown method type for " + super.getOriginalCode()), false);
			return UnknownType.UNKNOWN;
                    }
		} else {
                    Debug.logn("Resolved method in parse tree ", this);
                    type = s.getReturnType();
                    return type;
                }
            } 
            Debug.logn("Here", this);
            return type;
	}

	public String getTypeName() {
		return this.getType();

	}

	public DNType getDNType() {
            if (dnType == null) {
		dnType = this.getLibrary().getDNType(this.getTypeName());
            }
            return dnType;
	}


	private ParseTree getParseTree() {
		if (parseTree == null) {
			boolean keepGoing = true;
			InterpretationContext ancestor = this.context;
			
			while (keepGoing) {
				ancestor = ancestor.getParent();
				if (ancestor instanceof ParseTree) {
					parseTree = (ParseTree) ancestor;
					return parseTree;
				}
			}
			if (true) {
				throw new RuntimeException("not in any tree !!");
			}
		}
		return parseTree;
	}

	public String toString() {
		return "InvokEx: targ: " + target + " mc: " + methodCall;
	}


	

	

}

 