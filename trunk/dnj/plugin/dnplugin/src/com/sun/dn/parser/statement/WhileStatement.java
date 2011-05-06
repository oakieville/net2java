
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
import com.sun.dn.parser.expression.*;
import com.sun.dn.java.JavaKeywords;
import com.sun.dn.util.*;

	/** A .NET control flow structure that executes a block repeatedly
	** while a given expression is true. In VB for example, <br>
		While condition <br>
   		[ statements ] <br>
		End While <br>
	@author danny.coward@sun.com
	*/

public class WhileStatement extends ControlFlowStatement {
	private Expression condition;
	private CodeBlock codeBlock;

	public static boolean isVBWhileStatement(String code, InterpretationContext context) {
		return code.trim().startsWith(VBKeywords.VB_While);
	}

	public static WhileStatement createVBWhileStatement(String code, InterpretationContext context) {
		WhileStatement ws = new WhileStatement(code, context);
		ws.parseVB(code);
		return ws;
	}

	public static boolean isCSWhileStatement(String code, InterpretationContext context) {
		return code.trim().startsWith(CSKeywords.CS_While);
	}

	public static WhileStatement createCSWhileStatement(String code, InterpretationContext context) {
		WhileStatement ws = new WhileStatement(code, context);
		ws.parseCS(code);
		return ws;
	}

	private WhileStatement(String code, InterpretationContext context) {
		super(code, context);
	}

	
	public List getVariables() {
		return super.context.getVariables();
	}

	public CodeBlock getCodeBlock() {
		return this.codeBlock;
	}

	private void parseVB(String rawCode) {
		Debug.logn("parse while loop " + rawCode, this);
                super.setCode(rawCode);
		StringTokenizer st = new StringTokenizer(rawCode, "\n");
		String firstLine = st.nextToken().trim();
		String conditionString = firstLine.substring(VBKeywords.VB_While.length(), firstLine.length());    
		condition = (new VBExpressionFactory()).getExpression(conditionString, super.context);
		String nextLine = st.nextToken();
		List whileStatements = new ArrayList();
		List codeBlockStatements = new ArrayList();
		while (!isEndWhile(nextLine)) {
			codeBlockStatements.add(nextLine);
			nextLine = st.nextToken();
		}
		Debug.logn(whileStatements, this);
		this.codeBlock = new CodeBlock(this);
		codeBlock.parseVB(codeBlockStatements);
		Debug.logn(""+this, this);		
	}

	private void parseCS(String rawCode) {
		String conditionStringRaw = rawCode.substring(rawCode.indexOf("(") + 1, rawCode.indexOf("{"));
		conditionStringRaw =  conditionStringRaw.substring(0, conditionStringRaw.lastIndexOf(")"));
		String blockCode = rawCode.substring(rawCode.indexOf("{") + 1, rawCode.lastIndexOf("}")).trim();
                Debug.logn("Condition string = " + conditionStringRaw, this);
		condition = (new CSExpressionFactory()).getExpression(conditionStringRaw, super.context);
		Debug.logn("Condition  = " + condition, this);
                this.codeBlock = new CodeBlock(this);
		List blockStatements = CSClassStatement.tokenizeToClassStatements(blockCode);
		this.codeBlock.parseCS(blockStatements);

	}


	public String toString() {
		String t = "WhileLoop: " + condition + " " + codeBlock;
		return t;
	}

	public static String parseVBWhileLoop(String whileStatement, Iterator itr) {
		String toReturn = "";
		String s = whileStatement;
		toReturn = s + "\n";
		while (!isEndWhile(s)) {
			s = (String) itr.next();
			toReturn = toReturn  + s + "\n";
		}
		return toReturn;
	}

	private static boolean isEndWhile(String stat) {
		String s = stat.trim();
		String ew = VBKeywords.VB_End + " " + VBKeywords.VB_While;
		boolean b = s.startsWith(ew);
		return b;
	}

	protected List tryGetJava() {
		List strings = new ArrayList();
		String s = JavaKeywords.J_WHILE + " (" + this.condition.asJava() + ") {";
		strings.add(s);
		for (Iterator itr = this.getCodeBlock().getJava().iterator(); itr.hasNext();) {
			strings.add("\t" + itr.next());
		}
		s = "}";
		strings.add(s); 
		return strings;
	}
}
 