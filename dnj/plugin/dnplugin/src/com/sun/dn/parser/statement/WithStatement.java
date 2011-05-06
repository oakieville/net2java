
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
import com.sun.dn.parser.*;
import com.sun.dn.*;
import com.sun.dn.parser.expression.*;
import com.sun.dn.java.JavaKeywords;
import com.sun.dn.util.*;

	/** A VB control flow structure that executes partial statements within
	** the context of a given expression. <br><br>

		WithStatement ::= <br>
		With Expression StatementTerminator <br>
		[ Block ] <br>
		End With StatementTerminator <br>
	@author danny.coward@sun.com
	*/

public class WithStatement extends ControlFlowStatement {
	private CodeBlock codeBlock;
	private Expression withExpression;
	private DNVariable implicitVariable;
	private static String VARIABLE_NAME = "withVar";

	public static boolean isWithStatement(String code, InterpretationContext context) {
		return code.trim().startsWith(VBKeywords.VB_With);
	}

	public WithStatement(String code, InterpretationContext context) {
		super(code, context);
		this.parseVB(code);
	}

	private String getWithVarName() {
		return VARIABLE_NAME + this.getNestLevel();
	}

	private int getNestLevel() {
		int nl = 0;
		boolean done = false;
		InterpretationContext parent = this;
		while(!done) {
			parent = parent.getParent();
			if (parent instanceof WithStatement) {
				nl++;
			} else if (parent == null) {
				done = true;
			}
		}
		return nl;
	}

	public DNVariable getImplicitVariable() {
		return this.implicitVariable;
	}

	public Expression getImplicitExpression() {
		return this.withExpression;
	}
	
	public List getVariables() {
		return super.context.getVariables();
	}

	private void parseVB(String rawCode) {
		Debug.logn("Parse " + rawCode, this);
                super.setCode(rawCode);
		List lines = Util.tokenizeIgnoringEnclosers(rawCode, "\n");
		String withDeclaration = (String) lines.get(0);
		String withExpressionString = this.parseWithDeclaration(withDeclaration);
                Debug.logn("-------------------------------------------------------", this);
		this.withExpression = (new VBExpressionFactory()).getExpression(withExpressionString, this);
		Debug.logn("withExpression class " + withExpression.getClass(), this);
                Debug.logn("withExpression.typre=" +  withExpression.getTypeName(), this);
		this.implicitVariable = DNVariable.createVBVariable(this.getWithVarName(), withExpression.getTypeName());
		
		List codeBlockStatements = new ArrayList();
		for (int i = 1; i < lines.size() - 1; i++) {
			codeBlockStatements.add(lines.get(i));
		}

		this.codeBlock = new CodeBlock(this);
		this.codeBlock.parseVB(codeBlockStatements);

		Debug.logn("Code Block = " + codeBlock, this);
	}

	private String parseWithDeclaration(String withDeclaration) {
		StringTokenizer st = new StringTokenizer(withDeclaration);
		st.nextToken(); // there goes the With keyword
		String withExpressionString = st.nextToken();
		Debug.logn(" exp is " + withExpressionString, this);
		return withExpressionString;
	}

	public String toString() {
		String t = "WithStatement: " + withExpression + " " + codeBlock;
		return t;
	}

	public static String parseWithLoop(String withStatement, Iterator itr) {
		String toReturn = "";
		String s = withStatement;
		toReturn = s + "\n";
		int nestingCounter = 0;
		boolean isDone = false;
		while (!isDone) {
			s = (String) itr.next();
			toReturn = toReturn  + s + "\n";
			if (s.trim().startsWith(VBKeywords.VB_With)) {
				nestingCounter++;
			}
			if (isEndWith(s)) {
				if (nestingCounter == 0) {
					isDone = true;
				} else {
					nestingCounter--;
				}
			}
		}
		return toReturn;
	}

	private static boolean isEndWith(String stat) {
		String s = stat.trim();
		String ew = VBKeywords.VB_End + " " + VBKeywords.VB_With;
		boolean b = s.startsWith(ew);
		return b;
	}

	protected List tryGetJava() {
		List strings = new ArrayList();
		LocalVariableDeclaration ds = new LocalVariableDeclaration(this, this.implicitVariable, this.withExpression);
		strings.addAll(ds.getJava());
		strings.addAll(this.codeBlock.getJava());
		return strings;
	}
}
 