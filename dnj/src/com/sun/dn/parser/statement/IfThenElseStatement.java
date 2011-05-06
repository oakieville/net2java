
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
import com.sun.dn.java.*;
import com.sun.dn.util.*;

	/** A .NET control flow structure conditionally running code blocks
	** depending on the evaluation of boolean expressions. <br>
	@author danny.coward@sun.com
	*/

public class IfThenElseStatement extends ControlFlowStatement{
	private static String VB_EndIf = VBKeywords.VB_End + " " + VBKeywords.VB_If;
	private Expression ifBoolean;
 	private CodeBlock ifBlock;
	private Map elseIfBlocks = new LinkedHashMap(); // all the else ifs
	private CodeBlock elseBlock;

	public static boolean isVBIfThenElseStatement(String code, InterpretationContext context) {
		return Util.codeContains(code, VBKeywords.VB_If);
	}

	public static boolean isCSIfThenElseStatement(String code, InterpretationContext context) {
		return code.trim().startsWith(CSKeywords.CS_If);
	}

	public static IfThenElseStatement createVBIfThenElseStatement(String code, InterpretationContext context) {
		IfThenElseStatement ites = new IfThenElseStatement(code, context);
		ites.parseVB(code);
		return ites;
	}

	public static IfThenElseStatement createCSIfThenElseStatement(String code, InterpretationContext context) {
		IfThenElseStatement ites = new IfThenElseStatement(code, context);
		ites.parseCS(code.trim());
		return ites;
	}

	private IfThenElseStatement(String code, InterpretationContext context) {
		super(code, context);
	}

		// has no variables of its own
	public List getVariables() {
		return super.context.getVariables();
	}

	protected List tryGetJava() {
		List strings = new ArrayList();
		String s = JavaKeywords.J_IF;
		Debug.logn("Writing if Block", this);
		s = s + " ( " + this.ifBoolean.asJava() + " ) {";
		strings.add(s);
                
		for (Iterator itr = this.getIfBlock().getJava().iterator(); itr.hasNext();) {
			String next = (String) itr.next();
			strings.add("\t" + next);
		}
		Debug.logn("Written if Block", this);
		for (Iterator itr = this.getElseIfBlocks().keySet().iterator(); itr.hasNext();) {
			s = "}" + " " + JavaKeywords.J_ELSE + " " + JavaKeywords.J_IF;
			
			Object o = itr.next();
			
			Expression be = (Expression) o;
			
			s = s + " ( " + be.asJava() + " ) {";
			strings.add(s);
			CodeBlock cb = (CodeBlock) this.getElseIfBlocks().get(be);
			for (Iterator itrr = cb.getJava().iterator(); itrr.hasNext();) {
				s = "\t" + (String) itrr.next();
				strings.add(s);
			}			
		}
		s = "}";
		strings.add(s);
		Debug.logn("Written ElseIf Blocks", this);

		if (this.getElseBlock() != null) {
			s = " " + JavaKeywords.J_ELSE + " {";
			strings.add(s);
			for (Iterator itr = this.getElseBlock().getJava().iterator(); itr.hasNext();) {
				String nextLine = (String) itr.next();
				s = "\t" + nextLine;
				strings.add(s);
			}
			s = "}";
			strings.add(s);
		}
		return strings;
	}
        
        private String getCSDeclaration(String block) {
            if (block.indexOf("{") != -1) {
                return block.substring(0, block.indexOf("{"));
            } else {
                //String openExp = block.substring(block.indexOf("(" + 1), block.length());
                List l = Util.tokenizeIgnoringEnclosers(block, ")");
                return l.get(0) + ")";
                //throw new RuntimeException("Need to implement If statements with no {}");
            }
        }
        
        private String getCSBlockCode(String block) {
            if (block.indexOf("{") != -1) {
                return Util.getInsideFirstCurlyBrackets(block);
            } else {
                List l = Util.tokenizeIgnoringEnclosers(block, CSKeywords.CS_Else);
                String declAndBlock = (String) l.get(0);
                String bl = declAndBlock.substring(this.getCSDeclaration(declAndBlock).length(), declAndBlock.length());
                
                return bl;
            }
        }

	private void parseCS(String rawCode) {
		ExpressionFactory factory = new CSExpressionFactory();

		List blocks = Util.tokenizeSemiColonChunksAndPanhandles(rawCode);

		Debug.logn("Parse " + rawCode, this);

		for (Iterator itr = blocks.iterator(); itr.hasNext();) {
                    String nextBlock = (String) itr.next();
                    Debug.logn("Next Block = ." + nextBlock + ".", this);
                    String declaration = this.getCSDeclaration(nextBlock);
                    String blockCode = this.getCSBlockCode(nextBlock);
                    List blockStatements = CSClassStatement.tokenizeToClassStatements(blockCode);
                    CodeBlock codeBlock = new CodeBlock(this);
                    codeBlock.parseCS(blockStatements);

                    String elseIf = CSKeywords.CS_Else + " " + CSKeywords.CS_If;
                    if (nextBlock.startsWith(CSKeywords.CS_If)) {
			
			String boolS = declaration.substring(CSKeywords.CS_If.length(), declaration.length()).trim();
			boolS = Util.stripBrackets(boolS);
			Debug.logn("If boolean String ." + boolS + ".", this);
			this.ifBoolean = factory.getExpression(boolS, this.context);
			Debug.logn("If exp=" + ifBoolean, this);
			this.ifBlock = codeBlock;
                    } else if (nextBlock.startsWith(elseIf)) {
			
			String boolS = declaration.substring(elseIf.length(), declaration.length()).trim();
			
			boolS = Util.stripBrackets(boolS);
			Expression elseIfExpression = factory.getExpression(boolS, this.context);
			elseIfBlocks.put(elseIfExpression, codeBlock);
                    } else if (nextBlock.startsWith(CSKeywords.CS_Else) && !itr.hasNext()) {
			
			elseBlock = codeBlock;
                    } else {
			throw new RuntimeException("Can't parse: " + nextBlock);
                    }
		}
		//Debug.stop(this.getClass());
	}


	private void parseVB(String rawCode) {
		super.setCode(rawCode);
		List statements = VBParser.getVBStatements(rawCode);
		
		Debug.logn("-----------Parse IF THEN ELSE statement:", this);
		Debug.logn(rawCode, this);
		Debug.logn("---------End Parse", this);
		Iterator itr = statements.iterator();
		String ifDeclaration = (String) itr.next();
		Debug.logn("If Line is " + ifDeclaration, this);

		if (!ifDeclaration .trim().endsWith(VBKeywords.VB_Then)) {
			String ifStatement = ifDeclaration.substring(
				ifDeclaration.indexOf(VBKeywords.VB_Then) + VBKeywords.VB_Then.length(), 
				ifDeclaration.length());
			Debug.logn("If Line is now " + ifStatement , this);
			
			ifBoolean = this.getBooleanExpression(ifDeclaration);
			List ifStatementAsList = new ArrayList();
			ifStatementAsList.add(ifStatement );
			this.ifBlock = new CodeBlock(this);
			this.ifBlock.parseVB(ifStatementAsList);

			Debug.logn("Parsed single line IfThenElse " + this, this);
			return;
		}
		ifBoolean = this.getBooleanExpression(ifDeclaration);

		// the if code block is everything from here on until the
		// next ElseIf, or Else or EndIf

		List ifStatements = new ArrayList();
		String nextStatement = parseIfBlock(itr, ifStatements);
		ifBlock = new CodeBlock(this);
		ifBlock.parseVB(ifStatements);

		while (!isEndIf(nextStatement)) {
			if (isElseIf(nextStatement)) {
				Expression exp = getBooleanExpression(nextStatement);
				List elseifStatements = new ArrayList();
				nextStatement = parseIfBlock(itr, elseifStatements);
				CodeBlock elseIfBlock = new CodeBlock(this);
				elseIfBlock.parseVB(elseifStatements);
				elseIfBlocks.put(exp, elseIfBlock);
			} else if (isElse(nextStatement)) {
				List elseStatements = new ArrayList();
				nextStatement = parseIfBlock(itr, elseStatements);
				elseBlock = new CodeBlock(this);
				elseBlock.parseVB(elseStatements);
			}
		}
		Debug.logn(this, this);
	}

	private CodeBlock getIfBlock() {
		return this.ifBlock;
	}

	private Map getElseIfBlocks() {
		return this.elseIfBlocks;
	}

	private CodeBlock getElseBlock() {
		return this.elseBlock;
	}

	public String toString() {
		String t = "";
		String s =  "IfThenElseStatement: ifbool: " + ifBoolean + "...t";
		s = s + "\t i:" + "ifBlock" + "t";
		for (Iterator itr = elseIfBlocks.keySet().iterator(); itr.hasNext();) {
			Object key = itr.next();
			s = s + "\t e.if: " + key + "-" + "elseIfBlocks.get(key)" + "t";
		}
		s = s + "\t els: " +  "elseBlock" + "t";
		return s;
	}

	private Expression getBooleanExpression(String statement) {
		String expressionS = "";
		if (isIf(statement)) {
			expressionS = statement.substring(VBKeywords.VB_If.length(), statement.indexOf(VBKeywords.VB_Then)).trim();
		} else if (isElseIf(statement)) {
			expressionS = statement.substring(VBKeywords.VB_ElseIf.length(), statement.indexOf(VBKeywords.VB_Then)).trim();
		} else {
			throw new RuntimeException("Can't find boolean expression within " + statement);
		}

		Expression be = (new VBExpressionFactory()).getExpression(expressionS, this);
		return be;
	} 

	private String parseIfBlock(Iterator itr, List ifStatementList) {
		String nextStatement = ((String) itr.next()).trim();
		int nestingCounter = 0;
		boolean stop = false;
		while (!stop) {


			if (isElseIf(nextStatement) ||
				isElse(nextStatement) ||
				isEndIf(nextStatement) ) {

				if (nestingCounter==0) {
					stop = true;
				}
			}

			if (isIf(nextStatement)) {
				nestingCounter++;
			} else if (isEndIf(nextStatement)) {
				nestingCounter--; // this will get hit for the real end, which is why we started at 1
			}
			
			if (stop != true) {
				ifStatementList.add(nextStatement);
				nextStatement = ((String) itr.next()).trim();
			}
		}
		Debug.logn("Finished parsing if block at statement " + nextStatement, this);
		return nextStatement;
	}

	public static String parseCSIfThenElseBlock(String firstStatement, ListIterator remainingStatements) {
		String ifThenElseStatement = firstStatement;
		boolean done = false;
		String nextStatement = "";
		while (!done) {
			if (remainingStatements.hasNext()) {
				nextStatement = (String) remainingStatements.next();

				if (nextStatement.trim().startsWith(CSKeywords.CS_Else)) {
					ifThenElseStatement = ifThenElseStatement + nextStatement;
				} else {
					remainingStatements.previous();
					done = true;
				}
			} else {
				done = true;
			}
		}

		return ifThenElseStatement;
	}


	// pull out the statements that are in the if then else block
	// including the initial If and the final End If
	// incrementing the Iterator along the way
	public static String parseVBIfThenElseBlock(String ifStatement, Iterator remainingStatements) {
		String wholeIfTheElseCode = ifStatement; // to start with

		if (!ifStatement.trim().endsWith(VBKeywords.VB_Then)) {
			return ifStatement;
			// i.e. its a one line If..Then statement
		}

		String next = ifStatement;
		int nestingCounter = -1;
		boolean stop = false;
		while (!stop) {
			Debug.clogn("Parse next statement " + next, IfThenElseStatement.class);
			if (isIf(next)) {
				nestingCounter++;
				Debug.clogn("increment nest counter " + 	nestingCounter, IfThenElseStatement.class);						
			}
			Debug.clogn(next + " contains " + VB_EndIf + " ?", IfThenElseStatement.class);
			if (isEndIf(next)) {
				Debug.clogn("Hit end if " + nestingCounter, IfThenElseStatement.class);	
				if (nestingCounter == 0) {
					stop = true;
				} else {
					nestingCounter--;
				}
			}
			if (!stop) {
				next = (String) remainingStatements.next();
				wholeIfTheElseCode = wholeIfTheElseCode + "\n" + next;
			}
		}
		return wholeIfTheElseCode;
	}

	private static boolean isElse(String statement) {
		StringTokenizer st = new StringTokenizer(statement);
		return st.nextToken().equals(VBKeywords.VB_Else);
	}

	private static boolean isElseIf(String statement) {
		StringTokenizer st = new StringTokenizer(statement);
		return st.nextToken().equals(VBKeywords.VB_ElseIf);

	}

	private static boolean isIf(String statement) {
		StringTokenizer st = new StringTokenizer(statement);
		return st.nextToken().equals(VBKeywords.VB_If);

	}

	private static boolean isEndIf(String statement) {
		return statement.trim().startsWith(VB_EndIf);
	}

}
 