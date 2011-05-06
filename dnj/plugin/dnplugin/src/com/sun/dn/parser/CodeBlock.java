
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
import com.sun.dn.*;
import com.sun.dn.parser.expression.*;
import com.sun.dn.parser.statement.*;
import com.sun.dn.util.*;
import com.sun.dn.Library;

	/** A CodeBlock is a collection of .NET statements.
	** @author danny.coward@sun.com
	*/

public class CodeBlock implements InterpretationContextWithStatements {
	private List statements = new ArrayList();
	private InterpretationContext context;
	private List implicitVariables = new ArrayList(); // like catch (Exception e) things
         
	public CodeBlock(InterpretationContext context) {
		this.context = context;
	}

	public Expression getImplicitExpression() {
		if (context instanceof ControlFlowStatement) {
			return ((ControlFlowStatement) context).getImplicitExpression();
		} else {
			return null;
		}
	}

		// oops bad naming = these are for WithStatements.
	public DNVariable getImplicitVariable() {
		if (context instanceof ControlFlowStatement) {
			return ((ControlFlowStatement) context).getImplicitVariable();
		} else {
			return null;
		}
	}

	public Library getLibrary() {
		return this.context.getLibrary();
	}

	public MetaClass getMetaClass() {
		return this.context.getMetaClass();
	}

	private void parse(List rawStatementStrings, StatementFactory factory) {
		Debug.logn("--Code Block " + this.getVariables(), this);
		Debug.logn("--statements " + rawStatementStrings, this);
		Parser.parseStatementStrings(rawStatementStrings, this, factory);
	}

	public void parseVB(List rawStatementStrings) {
		parse(rawStatementStrings, new VBStatementFactory());
	}

	public void parseCS(List rawStatementStrings) {
		parse(rawStatementStrings, new CSStatementFactory());

	}

	public void addImplicitVariable(DNVariable v) {
		this.implicitVariables.add(v);
	}

	public List getVariables() {
		List variables = new ArrayList();
		variables.addAll(this.implicitVariables);
		if (this.getImplicitVariable() != null) {
			variables.add(this.getImplicitVariable());
		}
		for (Iterator itr = statements.iterator(); itr.hasNext();) {
			Object o = itr.next();
			if (o instanceof LocalVariableDeclaration) {
				variables.addAll( ((LocalVariableDeclaration) o).getVariables() );
			}
		}
		variables.addAll(this.context.getVariables());
		return variables;
	}

	public DNVariable getVariable(String name) {
		for (Iterator itr = this.getVariables().iterator(); itr.hasNext();) {
			DNVariable var = (DNVariable) itr.next();
			//Debug.logn("Test Variable " + var, this);
			if (var.getName().equals(name)) {
				return var;
			}
		}
		return null;
	}

	public InterpretationContext getParent() {
		return this.context;
	}

	public void addStatement(Statement s) {
            //System.out.println("Add + " + s);
		this.statements.add(s);
	}

	public List getJava() {
		Debug.logn("Write Code Block (" + this.context + ")", this);
		List allJava = new ArrayList();
		for (Iterator itr = statements.iterator(); itr.hasNext();) {
			Statement next = (Statement) itr.next();
			Debug.logn("wr: " + next, this);
                        
			List nextJ = next.getJava();
			Debug.logn(" " + nextJ, this);
			allJava.addAll(nextJ);
			Debug.logn("Done writing code block", this);
		}
		return allJava;
	}

	public String toString() {
		return "aCodeBlock with " + statements.size() + " statements.";
	}
	
}


 