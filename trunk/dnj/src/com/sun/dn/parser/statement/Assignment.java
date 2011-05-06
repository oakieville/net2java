
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

import com.sun.dn.library.JavaExpression;
import java.util.*;
import com.sun.dn.parser.*;
import com.sun.dn.util.*;
import com.sun.dn.parser.expression.*;
import com.sun.dn.*;
import com.sun.dn.java.*;
import com.sun.dn.Library;

	/** Assignment ( = ).
	Assignment statements carry out assignment operations. 
	Simple assignment operations consist of taking the value on 
	the right side of the operator and assigning it to the variable 
	on the left.
	@author danny.coward@sun.com
	*/

public class Assignment extends StatementAdapter implements Expression {
	Expression assignee;
	String valueExpression;
	CallExpression value; // used when the rhs is a call expression
	Expression expression; // used for anything else
	List javaSupplements = new ArrayList(); // for things like adding action listeners after the assignment
	String operator;
	Operators operators;
	private Library library;
	
	private Assignment(String code, InterpretationContext context) {
            super(code, context);
	}

	public static boolean isVBAssignment(String code, InterpretationContext context) {
		return (getOperator(code, (new VBOperators()).getAssignmentOperators()) != null);
	}
        
        public static Assignment createVBAssignment(String code, InterpretationContext context) {
            Assignment assignment = new Assignment(code, context);
            assignment.parseVB(code);
            return assignment;
        }
        
         public static Assignment createCSAssignment(String code, InterpretationContext context) {
            Assignment assignment = new Assignment(code, context);
            assignment.parseCS(code);
            return assignment;
        }

	public static boolean isCSAssignment(String code, InterpretationContext context) {
		return (getOperator(code, (new CSOperators()).getAssignmentOperators()) != null);
	}


	private static String getOperator(String code, List ops) {
		for (Iterator itr = ops.iterator(); itr.hasNext();) {
			String nextOperator = (String) itr.next();
			List l = Util.tokenizeIgnoringEnclosers(code, nextOperator);
			if (l.size() > 1) {
				return nextOperator.trim() ;
			}
		}
		return null;
	}
        
        public String getAssignmentOperator() {
            return this.operator;
        }

	private Library getLibrary() {
		if (this.library == null) {
			library = this.context.getLibrary();
		}
		return library;
	}
        
        public String asJava() {
            String line = (String) this.getJava().get(0);
            return "(" + line.substring(0, line.length()-1) + ")";
        }
        
	public String getTypeName() {
            return this.assignee.getTypeName();
        }
	public DNType getDNType() {
            return this.assignee.getDNType();
        }

	public Expression getAssignee() {
		return this.assignee;
	}
	
	public CallExpression getValue() {
		return value;
	}

	public Expression getExpression() {
		return this.expression;
	}

	public Expression getRightExpression() {
		if (this.getValue() != null) {
			return this.getValue().getExpression();
		} else {
			return this.getExpression();
		}
	}

	public boolean isMemberAssignment() {
		boolean isNormalMAE = assignee instanceof MemberAccessExpression 
				&& ((MemberAccessExpression) assignee).getExpression() != null;
		boolean isImpliedMAE = assignee instanceof ImpliedExpression
				&& ((ImpliedExpression) assignee).getMemberAccessExpression() != null;
		return isNormalMAE || isImpliedMAE;
	}

	public MemberAccessExpression getMemberAccessExpression() {
		if (!this.isMemberAssignment()) {
			throw new RuntimeException("But this assignment is not a member variable assignment");
		}
		if (assignee instanceof MemberAccessExpression) {
			return (MemberAccessExpression) assignee;
		} 
		if (assignee instanceof ImpliedExpression) {
			return ((ImpliedExpression) assignee).getMemberAccessExpression();
		}
		return null; // shouldn't get here.
	}

	public void addJavaSupplement(String js) {
		//if (true) throw new Stop(this.getClass());
		this.javaSupplements.add(js);
	}

	private void parseCS(String myCode) {
		this.operators = new CSOperators();
		String valueExpression = this.parseAssigneeAndGetExpression(myCode, new CSExpressionFactory(), operators.getAssignmentOperators());
		if (valueExpression != null) { // this means
			if (CallExpression.isCallExpression(valueExpression)) {
				CallExpression e = CallExpression.createCSCallExpression(valueExpression, this.context);
				Debug.logn("value - " + e, this);
				value = e;
			} else {
				throw new RuntimeException("Can't deal with literals and constants");
			}
		}
		this.context.getMetaClass().registerAssignmentForEventGenerator(this);
	}

	private String parseAssigneeAndGetExpression(String myCode, ExpressionFactory factory, List operators) {
		Debug.logn("Parse assignment " + myCode, this);

		operator = this.getOperator(myCode, operators);
		List pieces = Util.tokenizeIgnoringEnclosers(myCode, operator);

		String variableString  = ((String) pieces.get(0)).trim();
		Debug.logn("left side is " + variableString , this);
		this.assignee = factory.getExpression(variableString, context);

		Debug.logn("Left Hand Expression is " + assignee, this);

		String valueExpression = ((String) pieces.get(1)).trim();

		Debug.logn(" Value " + valueExpression, this);
		Expression ee = factory.getExpression(valueExpression, this.context, false);	
		Debug.logn(" Value expression is " + ee, this);
		if (ee != null) {
			this.expression = ee;
			super.addConstructedPreStatementsFrom(this.expression);
			return null;
		} else {
			return valueExpression;
		}
	}
	
	private void parseVB(String myCode) {
		this.operators = new VBOperators();
		String valueExpression = this.parseAssigneeAndGetExpression(myCode, new VBExpressionFactory(), operators.getAssignmentOperators());
		if (valueExpression != null) {
			if (CallExpression.isCallExpression(valueExpression)) {
				CallExpression e = CallExpression.createVBCallExpression (valueExpression, this.context);
				Debug.logn("value - " + e, this);
				value = e;
			} else {
				throw new RuntimeException("Can't deal with literals and constants");
			}
		}
		this.context.getMetaClass().registerAssignmentForEventGenerator(this);
	}

	public boolean isAddingCSEventListener() {
            Debug.logn("Am I adding an event listener ?", this);
            //Debug.logn(this.toString(), this);
            boolean libHasEvent = this.getLibrary().hasDNEvent(assignee.getTypeName());
            Debug.logn("Library has event of type ." + assignee.getTypeName() + ". " + libHasEvent, this);
            boolean isAddEventOperator = this.operators.isDelegateAssignmentOperator(this.operator);
            Debug.logn("Operator is event adder " + isAddEventOperator, this);
            boolean rhsIsDelegate = this.getRightExpression().getDNType().isDelegate();
            Debug.logn("RHS is Delegate " + rhsIsDelegate, this);
            boolean isAdding = libHasEvent && isAddEventOperator && rhsIsDelegate;
            Debug.logn("" + isAdding, this);
            return isAdding;
	}
        
        public boolean isAddingProgramDefinedDelegate() {
            boolean ok = isAddingCSEventListener();
            return ok && library.getProgramDefinedDelegate(this.getRightExpression().getTypeName()) != null;
        }

	protected List tryGetJava() {

		Debug.logn("Write assignment: " + this, this);
		List getJava = new ArrayList();
		
		if (this.isAddingCSEventListener()) {		
			getJava.add("// the add listener line is replaced below");
		} else {

			Debug.logn(" right expression: " + this.getRightExpression().getClass(), this);
		
			String valueJavaType = this.getLibrary().getJavaTypeFor(this.getRightExpression().getDNType().getName());

			Debug.logn(" assignett vb class is " + assignee.getClass(), this);

			String assigneeJavaType = this.getLibrary().getJavaTypeFor(assignee.getDNType().getName());
			Debug.logn("got types ok", this);
			// we need to upcast the value in the library substitution case
			// when the Java value type is primitive
			// and the assignee JavaType is
			// not EXACTLY the corresponding
			// Java class type.
			// This is because the 
			// library uses the convention that
			// in providing a translation for an assignment
			// of a VB property that corresponding to a Java class
			// with a Java primitive type, then always make it
			// a bean property with the primitive type.
			// so
		
			JavaExpression je = this.getLibrary().getTranslationForAssignment(this);
			if (je != null) {
				
				boolean needValueUpcast = JavaPrimitives.isPrimitive(valueJavaType) && !JavaPrimitives.isPrimitive(assigneeJavaType)
								&& !DNType.shortNameEquals(assigneeJavaType, JavaPrimitives.getJavaClassTypeForPrimitive(valueJavaType));
                                JavaExpressionMatcher jem = new JavaExpressionMatcher(je, this.getLibrary());
				getJava.add(jem.getJavaForAssignment(this, needValueUpcast) + ";");
				this.context.getMetaClass().addJavaImports(je.getImportStrings());
				Debug.logn("Written assignment (from library)", this);
				return getJava;
			}
			Debug.logn(" I'm not in library ", this);

                        // PropertyStatements need special handling
                        if (this.assignee instanceof MemberAccessExpression &&
                                ((MemberAccessExpression) this.assignee).isPropertyGetOrSet()) { 
                            Debug.logn(" I'm a property get or set ", this);
                        
                            MemberAccessExpression mae = ((MemberAccessExpression) this.assignee);
                            getJava.add(mae.getJavaForPropertySet(this.expression) + ";");
    
                        } else {
                            Debug.logn(" I'm NOT a property get or set ", this);
                            String s = "";
                            String assignmentString = this.getJavaForAssignee(false);

                            String valueString = this.getJavaForValue(false);

                            s = this.operators.translateAssignmentOperator(assignmentString, operator, valueString);
                            
                            getJava.add(s);
                        }
		}

		if (this.javaSupplements.size() > 0) {
			getJava.add("//start: autogenerated");
			for (Iterator itr = this.javaSupplements.iterator(); itr.hasNext();) {
				getJava.add(itr.next() + ";");
			}
			getJava.add("//end: autogenerated");
		}
		Debug.logn("Written assignment ", this);

		return getJava;
	}

	public String getJavaForValue(boolean ensureUpcast) {
            if (ensureUpcast) {
		return Translator.ensureJUpcast(this.getRightExpression(), this.getLibrary());
            } else {
		return this.getRightExpression().asJava();
            }
	}

	public String getJavaForValue() {
		return this.getJavaForValue(false);
	}

	private String getJavaForAssignee(boolean ensureUpcast) {
                Debug.logn("Get java for assignee: " + this.getAssignee(), this);
		DNVariable lastV = null;
		Expression e = this.getAssignee();
		if (e instanceof LocalVariableExpression) {
			lastV = ((LocalVariableExpression) e).getVariable();
		} else if (e instanceof MemberAccessExpression) {
			lastV = ((MemberAccessExpression) e).getVariable();
                } else if (e instanceof IndexExpression) {
                        lastV = ((IndexExpression) e).getVariable();
		} else if (e instanceof ImpliedExpression) {
			MemberAccessExpression mae = ((ImpliedExpression) e).getMemberAccessExpression();
			if (mae != null) {
				lastV = mae.getVariable();
			} else {
				throw new RuntimeException("Should not get here. ");
			}
		} else {
			throw new TypeResolveException(this.getOriginalCode(), "Assignee is not a LocalVariable, MemberAcccess or Implied expression");
		}

		String s = "";
		if (lastV.isDimensionlessArray()) {
			// see DimStatement - it hasn't been declared
			// so we are going to use a static initializer
			// see
			// http://www.oreilly.com/catalog/javanut/excerpt/#arrays

			s = s + lastV.getJType() + "[] ";

		}
		String assigneeAsJava = "";
		if (ensureUpcast) {
			assigneeAsJava = Translator.ensureJUpcast(this.getAssignee(), this.getLibrary());
		} else {
			assigneeAsJava = this.getAssignee().asJava();
		}
		s = s + assigneeAsJava;
		return s;
	}

	public String toString() {
		return "Assmnt: as: " + assignee + " valEx: " + valueExpression + " val: " + value + 
				" exp: " + expression;
	}

}
 