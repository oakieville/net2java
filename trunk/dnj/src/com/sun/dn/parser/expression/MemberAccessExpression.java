
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
import com.sun.dn.*;
import com.sun.dn.parser.*;
import com.sun.dn.parser.statement.*;
import com.sun.dn.java.*;
import com.sun.dn.util.*;
import com.sun.dn.library.LibraryData;

	/**
	A member access expression results in the value of a variable or a constant. 
	A member access expression consists either of a variable, property, constant, 
	or enumeration value name; or an identifier preceded by an optional member 
	access operator (".") and optional expression. 

	MemberAccessExpression ::=
	[ [ Expression ] . ] IdentifierOrKeyword |
	VariableMemberName |
	PropertyMemberName |
	ConstantMemberName |
	EnumMemberName |
	DictionaryAccessExpression

	condition for recognising == 
	it has dots in and has no brackets on the last bit
	@author danny.coward@sun.com
	**/


public class MemberAccessExpression extends ExpressionAdapter {
	private Expression expression;
	private String variableMemberName;
	private String type;
	private DNType vbClass;
        private PropertyStatement ps;

        
        public static MemberAccessExpression createVBMemberAccessExpression(String code, InterpretationContext context) {
            MemberAccessExpression mae = new MemberAccessExpression(code, context);
            mae.parseVB(code);
            return mae;
        }
        
        public static MemberAccessExpression createCSMemberAccessExpression(String code, InterpretationContext context) {
            MemberAccessExpression mae = new MemberAccessExpression(code, context);
            mae.parseCS(code);
            return mae;
        }

	private MemberAccessExpression(String code, InterpretationContext context) {
            super(code, context);
            
	}
        
        public boolean isPropertyGetOrSet() {
            return this.getPropertyStatement() != null;
                 
        }
        
        private PropertyStatement getPropertyStatement() {
            if (ps == null) {
                MetaClass mc;
                if (expression != null) {
                    mc = ParseTree.getParseTree(this.context).findMetaClassFor(expression.getDNType());
                } else {
                    mc = this.context.getMetaClass();
                }
                if (mc != null) { // e.g. it can be null if I'm in an enumeration, or library class
                    this.ps = mc.findPropertyStatementOnMe(variableMemberName);
                }
            }
            return ps;
        }

	public DNVariable getVariable() {
		DNVariable v = this.context.getVariable(this.variableMemberName);
		if (v == null) {
			//throw new TypeResolveException(super.code, "Couldn't get type for variable of name " + this.variableMemberName);
			v = DNVariable.createUnknownVariable(this.variableMemberName);
		}
		return v;
	}

	public Expression getExpression() {
		return expression;
	}

	public String getVariableMemberName() {
		return this.variableMemberName;
	}

	private Library getLibrary() {
            return context.getLibrary();
	}

	public InterpretationContext getContext() {
		return context;
	}

	private void parseCS(String code) {
            this.expression = new CSThisExpression("<no code>", this.context);
		this.parse(code, new CSExpressionFactory());
	}

	private void parse(String code, ExpressionFactory factory) {
            Debug.logn("Parse " + code, this);
            List l = Util.tokenizeIgnoringEnclosers(code.trim(), '.');
            variableMemberName = (String) l.get(l.size() -1);
            Debug.logn("VariableName is " + variableMemberName, this);
            if (l.size() == 1) {
		Debug.logn("--" + this, this);
		return;
            }
            List expList = Util.shrinkListByOne(l);
            String expressionS = "";
            for (Iterator itr = expList.iterator(); itr.hasNext();) {
		String s = (String) itr.next(); 
            	if (itr.hasNext()) {
                    expressionS = expressionS  + s + ".";
		} else {
                    expressionS = expressionS  + s;
                }
            }
            this.expression = factory.getExpression(expressionS, this.context);
            Debug.logn("Expression is " + expression, this);
            //Debug.logn("--" + this, this);
	}

	private void parseVB(String code) {
            this.expression = new VBMeExpression("<no code>", this.context);
            this.parse(code, new VBExpressionFactory());
	}

	public static boolean isCSMemberAccessExpression(String code, InterpretationContext context) {
		return isNETMemberAccessExpression(code, context);
	}

	public static boolean isVBMemberAccessExpression(String code, InterpretationContext context) {
		return isNETMemberAccessExpression(code, context);
	}

	private static boolean isNETMemberAccessExpression(String code, InterpretationContext context) {
            Debug.clogn("is " + code + " a member access expression ?", MemberAccessExpression.class);
            List l = Util.tokenizeIgnoringEnclosers(code, '.');
            String last = (String) l.get(l.size() - 1);
            boolean rightForm = last.charAt(last.length() -1) != ')';
            if (!rightForm) {
            	Debug.clogn("no - wrong form", MemberAccessExpression.class);
		return false;
            }
            // now it could be a local variable that we are looking at, so...
            DNVariable variable = context.getVariable(last);
            Debug.clogn("" + variable, MemberAccessExpression.class);
            if (l.size() == 1) {
		if (variable != null) {
                    if (!variable.isMember()) {
			// then it MUST be a local variable
			Debug.clogn("no - its a local variable", MemberAccessExpression.class);
			return false;
                    } else { // it is a member
			Debug.clogn("yes its a member of this class", MemberAccessExpression.class);
			return true;
                    }
		} else {
                    //size is one and variable is not in context..shouldn't be here
                    if (context.getMetaClass().findPropertyStatementOnMe(last) != null) {
                        Debug.clogn("its a property access on my class ", MemberAccessExpression.class);
                        return true;
                    } else {
                        Debug.clogn("size is one and its not a var name in context" + context, MemberAccessExpression.class);
                        return false;
                    }
                    //throw new TypeResolveException(code, "Cannot resolve variable=" + code + "=");
		}
            } else {
		Debug.clogn("yes its a member of another class", MemberAccessExpression.class);
		return true;
            }
	}

	public String asJava(boolean usePrimitives) {
		if (usePrimitives) {
			return this.asJava();
		} else {
			return JavaPrimitives.primitiveToClass(this.asJava(), getLibrary().getJavaTypeFor( this.getTypeName() ));
		}
	}
        
        // called by assignments
        public String getJavaForPropertySet(Expression e) {
            
            String expressionJava = e.asJava();
            GetOrSetStatement ss = this.getPropertyStatement().getSetStatement();
            if (ss == null) {
                throw new RuntimeException("Weird: Assignment thinks this is a property set, but there's no set definition in the propertydeclaration");
            } 
            
            String jName = ss.getJavaName();
            String s = this.expression.asJava() + "." + jName + "(" + expressionJava + ")";
            
            return s;
            
        }

	public String tryAsJava() {
		boolean b = false; //this.variableMemberName.equals("Text");
		if (b) System.out.println(this);
                Debug.logn("Get Java for " + this, this);
		JavaExpression je = this.getLibrary().getTranslationForMAE(this);
		if (b) System.out.println("exp is " + je);
		if (b) System.out.println(" type is " + this.getExpression().getTypeName());
		if (je != null) {
                        Debug.logn(" There is a lib expression for me ", this);
			MetaClass myMetaClass = this.context.getMetaClass();
			Debug.logn(" adding import strings " + je.getImportStrings(), this);
			myMetaClass.addJavaImports(je.getImportStrings());
                        JavaExpressionMatcher jem = new JavaExpressionMatcher(je, this.getLibrary());
			return jem.getJavaForMAE(this);
		}
                Debug.logn(" There's NO lib expression for me ", this);
                // this could be a property access. in which case ps != null
                if (this.getPropertyStatement() != null) {
                    Debug.logn(" I'm from a property expression ", this);
                    GetOrSetStatement gs = this.getPropertyStatement().getGetStatement();
                    if (this.expression != null) {
                        return this.expression.asJava() + "." + gs.getJavaName() + "()";
                    } else {
                        return JavaKeywords.J_THIS + "." + gs.getJavaName() + "()";
                    }
                }

		// now remember that this could actally be shorthand for an
		// invocation of a no-arg method
                InvocationExpression ie = new InvocationExpression(this);
                // first is it in my parser tree ?
                Signature s = ParseTree.getParseTree(this.context).resolveMethod(this.expression.getDNType(), variableMemberName, new ArrayList());
                if (s != null) {
                    //System.out.println("BBBBBBBBBBBBBingo!!!");
                    return ie.tryAsJava();
                }
		// next is it in the library ?
		je = this.getLibrary().getTranslationForInvocation(ie );
		if (je != null) {

			MetaClass myMetaClass = this.context.getMetaClass();
			Debug.logn(" adding import strings " + je.getImportStrings(), this);
			myMetaClass.addJavaImports(je.getImportStrings());
                        JavaExpressionMatcher jem = new JavaExpressionMatcher(je, this.getLibrary());
			return jem.getJava(ie );
		}


		if (this.expression == null) {
			return variableMemberName;
		} else {
			return this.expression.asJava() + "." + variableMemberName;
		}
	}
        
     
        
       

		// its either a variable in my context, or an enumeration on my class, or something
		// in the library

	public String getTypeName() {
            if (type  != null) {
                return type;
            }
            Debug.logn("get type on " + this, this);
            Debug.logn("get variable member name " + variableMemberName, this);
			
            // is the variable on my metaclass ?
            DNVariable variable = this.context.getVariable(variableMemberName);
            Debug.logn("seek variable on my context of the same name, yield = " + variable, this);
            //Debug.logn("---" + expression, this);
            //Debug.logn("" + this.context.getMetaClass().isEnumeration(expression.getDNType()), this);
            //Debug.logn("After", this);
            if (variable != null) {
                Debug.logn("-return " + variable.getType(), this);
		type = variable.getType();
		return type;
            } else if (this.context.getMetaClass().isEnumeration(expression.getDNType())) {
                // then my expression is an Enumeration on my metaclass
                // so therefore I just need to go fetch its type
		Debug.logn("I'm from an EnumStatement ", this);

		EnumStatement es = this.context.getMetaClass().getEnumeration(expression.getDNType());
		type = es.getType();
                                
		return type;
            } else if (ParseTree.getParseTree(this.context).findMetaClassFor(expression.getDNType()) != null) {
                Debug.logn("I could be from a meta class ...", this);
                // is it a property statement ?
                MetaClass mc = ParseTree.getParseTree(this.context).findMetaClassFor(expression.getDNType());
                this.ps = mc.findPropertyStatementOnMe(variableMemberName);
                if (this.getPropertyStatement() != null) {
                    return this.getPropertyStatement().getDNType().getName();     
                }
                VariableMemberDeclaration vmd = mc.findVariableMemberDeclarationOnMe(variableMemberName);
                if (vmd != null) {
                    return vmd.getTypeName();      
                }
                // at this point, there was a class which matched the target, but no enum, property or
                // member has been found
                      
            }
             
            Debug.logn("can I find my type in the library? ", this);
            String libPropType = this.getLibrary().getTypeFor(this);
            if (libPropType != null) {
                Debug.logn("FOUND " + libPropType, this);
                return libPropType;
            }
            Debug.logn("last resort: is it an invocationexpression with no args ?", this);
            InvocationExpression ie = new InvocationExpression(this);
            String ret = ie.getType();
            if (ret != null) {
                type =  ret;
                Debug.logn("Type found: " + type, this);
                return type;
            } 
            
            type = UnknownType.UNKNOWN;
            Debug.logn("Type is: " + type, this);
            return type;
	}

	public DNType getDNType() {
            if (vbClass == null) {
                String t = this.getTypeName();
		if (!t.equals(UnknownType.UNKNOWN)) {
                    DNType c = this.getLibrary().getLibraryData().getLibraryClass(t);
                    if (c != null) {
                        vbClass = c;
			return vbClass ;
                    } else {
			vbClass = this.getLibrary().createProgramDefinedDNType(t, null);
			return vbClass;
                    }
		} else {
                    vbClass = new UnknownType(this.context);
                    return vbClass;
		}
            }
            return vbClass;
	}

	public String toString() {
		return "MAExp: targ: " + this.expression + " memName: " + variableMemberName;
	}
}
 