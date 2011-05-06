
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
import com.sun.dn.util.*;
import com.sun.dn.parser.*;
import com.sun.dn.parser.expression.*;
import com.sun.dn.java.JavaKeywords;

	/** A .NET control flow structure based on executing code blocks
	** depending on the value of a given variable. Defined in VB as <br> <br>
	SelectStatement ::= <br>
	Select [ Case ] Expression StatementTerminator <br>
	[ CaseStatement+ ] <br>
	[ CaseElseStatement ] <br>
	End Select StatementTerminator <br> <br>

	CaseStatement ::= <br>
	Case CaseClauses StatementTerminator <br>
	[ Block ] <br> <br>

	CaseClauses ::= <br>
	CaseClause | <br>
	CaseClauses , CaseClause <br> <br>

	CaseClause ::= <br>
	[ Is ] ComparisonOperator Expression | <br>
	Expression [ To Expression ] <br> <br>

	ComparisonOperator ::= = | < > | < | > | = > | = < <br> <br>

	CaseElseStatement ::= <br>
	Case Else StatementTerminator <br>
	[ Block ] <br>
	@author danny.coward@sun.com
	**/

public class SelectStatement extends ControlFlowStatement {
	private Expression sExpression;
	private Map caseBlocks = new LinkedHashMap(); // all the case blocks
	private CodeBlock elseBlock;

	public static boolean isVBSelectStatement(String code, InterpretationContext context) {
		return code.trim().startsWith(VBKeywords.VB_Select + " ");
	}

	public static boolean isCSSelectStatement(String code, InterpretationContext context) {
		return code.trim().startsWith(CSKeywords.CS_Switch);
	}

	public static SelectStatement createVBSelectStatement(String code, InterpretationContext context) {
		SelectStatement ss = new SelectStatement(code, context);
		ss.parseVB(code);
		return ss;
	}

	public static SelectStatement createCSSelectStatement(String code, InterpretationContext context) {
		SelectStatement ss = new SelectStatement(code, context);
		ss.parseCS(code);
		return ss;
	}

	private SelectStatement(String code, InterpretationContext context) {
		super(code, context);
	}

	public List getVariables() {
		return super.context.getVariables();
	}

	private Map getCaseBlocks() {
		return this.caseBlocks;
	}

	private void parseCS(String rawCode) {
		Debug.logn("Parse " + rawCode, this);
	
		String code = rawCode.trim();
                super.setCode(rawCode);
		String switchExpressionString = code.substring(CSKeywords.CS_Switch.length(), code.indexOf("{"));
		sExpression = (new CSExpressionFactory()).getExpression(switchExpressionString, this.context);

		String selectBodyCode = code.substring(code.indexOf("{") + 1, code.lastIndexOf("}")).trim();
		
		List restStatementStrings = CSClassStatement.tokenizeToClassStatements(selectBodyCode);
		

		List caseOrDefaultStatements = null;
		for (Iterator itr = restStatementStrings.iterator(); itr.hasNext();) {
			String nextStatement = ((String)itr.next()).trim();
			if (nextStatement.startsWith(CSKeywords.CS_Case)) {
				this.parseCSCaseBlock(nextStatement, itr);
			} else if (nextStatement.startsWith(CSKeywords.CS_Default)) {
				this.parseCSElseBlock(nextStatement, itr);
			}
		}
	}

	private void parseCSElseBlock(String nextStatement, Iterator itr) {
		String firstInElseBlock = nextStatement.substring(nextStatement.indexOf(":")+1, nextStatement.length());
		List elseStatements = new ArrayList();
		elseStatements.add(firstInElseBlock .trim());
		boolean reading = true;
		while(reading && itr.hasNext()) {
			String next = ((String) itr.next()).trim();
			elseStatements .add(next);
			if (JumpStatement.startsWithCSJumpStatement(next)) {
				reading = false;
			}
		}

		this.elseBlock = new CodeBlock(this);
		this.elseBlock .parseCS(elseStatements );
	}
	

	

	private void parseCSCaseBlock(String nextStatement, Iterator itr) {
		String caseConditionString = nextStatement.substring(CSKeywords.CS_Case.length(), nextStatement.indexOf(":"));
		String firstInCaseBlock = nextStatement.substring(nextStatement.indexOf(":")+1, nextStatement.length());
		List caseStatements = new ArrayList();
		caseStatements.add(firstInCaseBlock.trim());
		boolean reading = true;
		while(reading && itr.hasNext()) {
			String next = ((String) itr.next()).trim();
			caseStatements.add(next);
			if (JumpStatement.startsWithCSJumpStatement(next)) {
				reading = false;
			}
		}
		Debug.logn(caseConditionString + " " + caseStatements, this);

		CaseClause cc  =  CaseClause.createCSCaseClause (caseConditionString, sExpression, this);
		CodeBlock cb = new CodeBlock(this);
		cb.parseCS(caseStatements);
		this.caseBlocks.put(cc, cb);
	
	}

	private void parseVB(String rawCode) {
		Debug.logn("Parse Select statement", this);
		Debug.logn(rawCode, this);
                super.setCode(rawCode);
		StringTokenizer st = new StringTokenizer(rawCode, "\n");
		String nextLine = st.nextToken();
		this.parseSelectDeclaration(nextLine);
		
		boolean reachedEnd = false;
		int nestingCounter = 0;
		List caseStatements = new ArrayList();
                List comments = new ArrayList();
		while(!reachedEnd) {
			nextLine = st.nextToken();

			boolean collectCase = false;
			
			if (nextLine.startsWith(VBKeywords.VB_Select)) {
				nestingCounter++;
			}
			if (isEndSelect(nextLine)) {
				if (nestingCounter == 0) {
					this.parseCase(caseStatements, comments);	
                                        comments = new ArrayList();
					reachedEnd = true;
				} else {
					nestingCounter--;
				}
			}
			
			if (nextLine.startsWith(VBKeywords.VB_Case) && nestingCounter == 0) {				
				this.parseCase(caseStatements, comments);
                                comments = new ArrayList();
				caseStatements = new ArrayList();
			} 
                        if (VBComment.isComment(nextLine, context)) {
                            comments.add(new VBComment(nextLine, context));
                        } else {
                            caseStatements.add(nextLine);	
                        }
		}
		
	}

	private void parseCase(List statements, List comments) {
		Debug.logn("---PARSE CASE CLAUSE " + statements, this);
		Debug.logn("statements are " + statements, this);
		if (statements.isEmpty()) {
			return;
		}
		Iterator itr = statements.iterator();
		String caseLine = (String) itr.next();
		String clause = caseLine.substring(VBKeywords.VB_Case.length() + 1, caseLine.length()).trim();
		List codeBlockStatements = new ArrayList();
		while(itr.hasNext()) {
			codeBlockStatements.add(itr.next());
		}
		if (clause.equals(VBKeywords.VB_Else)) {
			elseBlock = new CodeBlock(this);
			elseBlock .parseVB(codeBlockStatements);
		} else {
			CaseClause cc  =   CaseClause.createVBCaseClause (clause, sExpression, this);
			CodeBlock cb = new CodeBlock(this);
                        
			cb.parseVB(codeBlockStatements);
                        
			this.caseBlocks.put(cc, cb);
		}
	}

	private void parseSelectDeclaration(String nextLine) {
                Debug.logn("Pser Select Declaration: = " + nextLine, this);
		StringTokenizer st = new StringTokenizer(nextLine);
		String selectK = st.nextToken();
		String caseK = st.nextToken();

		String decl = selectK + " " + caseK;
		String remainder = nextLine.substring(decl.length() + 1, nextLine.length()).trim();
		Debug.logn("remainder = " + remainder, this);
		sExpression = (new VBExpressionFactory()).getExpression(remainder, this.context);
	}

	public static String parseVBSelectLoop(String selectStatement, Iterator itr) {
		String nextLine = selectStatement;
		String toReturn = nextLine + "\n";
		Debug.clogn("parseSelectLoop starting with " + selectStatement, SelectStatement.class);
		Debug.clogn("parseSelectLoop itr has next ? " + itr.hasNext(), SelectStatement.class);
		boolean reachedEnd = false;
		int nestingCounter = 0;
		while(!reachedEnd) {
			nextLine = (String) itr.next();
			Debug.clogn("Next line " + nextLine, SelectStatement.class);
			if (nextLine.startsWith(VBKeywords.VB_Select)) {
				nestingCounter++;
				Debug.clogn("nest++ " + nestingCounter, SelectStatement.class);
			}
			if (isEndSelect(nextLine)) {
				Debug.clogn("its end select & nest is " + nestingCounter, SelectStatement.class);

				if (nestingCounter == 0) {
					reachedEnd = true;
					Debug.clogn("the end", SelectStatement.class);

				} else {
					nestingCounter--;
				}
			}
			toReturn = toReturn + nextLine + "\n";
		}
		return toReturn;
	}

	private static boolean isEndSelect(String statement) {
		String endSelect = VBKeywords.VB_End + " " + VBKeywords.VB_Select;
		return statement.equals(endSelect);
	}

	protected List tryGetJava() {
		List strings = new ArrayList();
		String s = "";
		boolean isFirst = true;
		strings.add(s);
		for (Iterator itr = this.getCaseBlocks().keySet().iterator(); itr.hasNext();) {
			CaseClause be = (CaseClause) itr.next();
			CodeBlock cb = (CodeBlock) this.getCaseBlocks().get(be);
			String ifElse = "";
			if (isFirst) {
				ifElse = JavaKeywords.J_IF;
				isFirst = false;
				s = ifElse + " (" + be.asJava() + ") {";
			} else {
				ifElse = JavaKeywords.J_ELSE + " " + JavaKeywords.J_IF;
				s = ifElse + " ("  + be.asJava() + ") {";
			}
			strings.add(s);
			//Debug.logn("Here", this);
			
			for (Iterator itrr = cb.getJava().iterator(); itrr.hasNext();) {
				strings.add("\t" + itrr.next());
			}

			s = "}";
			strings.add(s);
		}
		s = JavaKeywords.J_ELSE + " {";
		strings.add(s);
		if (this.elseBlock != null) {
			for (Iterator itrr = this.elseBlock.getJava().iterator(); itrr.hasNext();) {
				strings.add("\t" + itrr.next());
			}	
		}
		s = "}";
		strings.add(s);
		return strings;
	}


	public String toString() {
		String s = "SelectStatement: " + this.sExpression + "  ";
		for (Iterator itr = this.caseBlocks.keySet().iterator(); itr.hasNext();) {
			Object o = itr.next();
			Debug.logn("" + o, this);
			CaseClause be = (CaseClause ) o;
			CodeBlock cb  = (CodeBlock) this.caseBlocks.get(be);
			s = s + "be: " + be + " cb: " + "cb" + "  ";	
		}
		s = s + "els: " + "this.elseBlock" + " ";
		return s;
	}
}
 