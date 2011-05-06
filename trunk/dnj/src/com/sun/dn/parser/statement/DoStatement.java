
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
import com.sun.dn.util.*;
import com.sun.dn.java.*;
import com.sun.dn.*;

		/** In C# this looks like
                 * do statement while (expression);
                 *
                 * A VB control flow statement for Do..While loops. Defined as <br>
	Do While  Loop. The Do While ... Loop evaluates the condition, and if the condition is true, then it evaluates the statements following the condition. 
	When it has finished doing this, it evaluates the condition again and if the condition is true, it evaluates the statements again. 
	It continues repeating this process until the condition is false.
 <br> <br>
	Do While condition <br>
	   statements <br>
	Loop <br>
 <br> <br>
	Do Until ... Loop. The Do Until ... Loop is similar to the Do While ... Loop except it keeps evaluating the statements until the condition is true rather than while it is true.  <br>
	Do Until condition <br>	
	   statements <br>
	Loop <br>
	Do ... Loop While. The Do ... Loop While evaluates the statements only once.  <br>
	It then evaluates the condition, and if the condition is true, evaluates the statements again. This process continues until the condition is false. <br> <br>
	Do <br>
	   statements <br>
	Loop While condition <br>
	Do ... Loop Until. Similar to Do ... Loop While except that it evaluates the statements until the condition is true.  <br> <br>
	Do <br>
	   statements <br>
	Loop Until condition <br>
	@author danny.coward@sun.com
	*/

public class DoStatement extends ControlFlowStatement {
	private boolean conditionInDo;
	private boolean isWhile;
	private Expression condition;
	private CodeBlock codeBlock;
	

	public static boolean isVBDoStatement(String code, InterpretationContext context) {
                if (code.trim().equals(VBKeywords.VB_Do)) {
                    return true;
                }
		boolean starts = code.trim().startsWith(VBKeywords.VB_Do);
                if (starts) {
                    String rest = code.trim().substring(2, code.trim().length());
                    boolean restStartsWithWs = !rest.trim().equals(rest);
                    return restStartsWithWs;
                } 
                return false;
	}
        
        public static boolean isCSDoStatement(String code, InterpretationContext context) {
                String ccode = code.trim();
		if (ccode.startsWith(CSKeywords.CS_Do)) {
                    String rest = ccode.substring(CSKeywords.CS_Do.length(), ccode.length());
                    if (rest.trim().startsWith ("{")) {
                        return true;
                    }
                }
                return false;        
	}
        
        public static DoStatement createVBDoStatement(String code, InterpretationContext context ) {
            DoStatement ds = new DoStatement(code, context);
            ds.parseVB(code);
            return ds;
        }
        
        public static DoStatement createCSDoStatement(String code, InterpretationContext context ) {
            DoStatement ds = new DoStatement(code, context);
            ds.parseCS(code);
            return ds;
        }

	private DoStatement(String code, InterpretationContext context) {
		super(code, context);
	}

	public List getVariables() {
		return super.context.getVariables();
	}

	public boolean isConditionInDo() {
		return this.conditionInDo;
	}

	public boolean isWhile() {
		return this.isWhile;
	}
        
	public CodeBlock getCodeBlock() {
		return this.codeBlock;
	}
        
        private void parseCS(String rawCode) {
          
            this.conditionInDo = false;
            this.isWhile = true;
            List l = Util.tokenizeSemiColonChunksAndPanhandles(rawCode);
            String doPart = (String) l.get(0);
            String doBlockS = Util.getInsideFirstCurlyBrackets(doPart).trim();
            List doBlockL = CSClassStatement.tokenizeToClassStatements(doBlockS);
            this.codeBlock = new CodeBlock(this);
        
            String whilePart = (String) l.get(1);
            String whileCondition = whilePart.substring( whilePart.indexOf("(") + 1, whilePart.lastIndexOf(")") );
            this.condition = (new CSExpressionFactory()).getExpression(whileCondition, context);
            this.codeBlock.parseCS(doBlockL);
            
        }

	private void parseVB(String rawCode) {
		Debug.logn("parse do loop \n" + rawCode, this);
		StringTokenizer st = new StringTokenizer(rawCode, "\n");
		String firstLine = st.nextToken();
		this.parseDoStatement(firstLine);

		// then merrily parse to the end of the loop

		
		List codeBlockStatements = new ArrayList();
		boolean reachedEnd = false;
		int nestingCounter = 0;
		while (!reachedEnd) {
			String nextLine = st.nextToken();
			Debug.logn("Read next line = " + nextLine, this);
			if (nextLine.startsWith(VBKeywords.VB_Do)) {
				nestingCounter++;
				Debug.logn("nesting up one to " + nestingCounter, this);
			} else if (nextLine.startsWith(VBKeywords.VB_Loop)) {
				if (nestingCounter == 0) {
					Debug.logn("got to the end Loop" + nestingCounter, this);
					reachedEnd = true;
					this.parseLoopStatement(nextLine);
				} else {
					nestingCounter--;
					Debug.logn("nesting down one to " + nestingCounter, this);
				}
			}
			if ( !nextLine.startsWith(VBKeywords.VB_Do) &&
				!nextLine.startsWith(VBKeywords.VB_Loop) &&
				nestingCounter == 0) {
				codeBlockStatements.add(nextLine);
			}
		}

		this.codeBlock = new CodeBlock(this);
		this.codeBlock.parseVB(codeBlockStatements);
		Debug.logn(this, this);		
	}

	private void parseDoStatement(String statement) {

		StringTokenizer st = new StringTokenizer(statement);
		String doString = st.nextToken();
		if (st.hasMoreTokens()) {
			Debug.logn("Condition in the do", this);
			this.conditionInDo = true;
			String nextToken = st.nextToken();
			if (nextToken.equals(VBKeywords.VB_Until)) {
				isWhile = false;
			} else {
				isWhile = true;
			}
			// then parse the rest of the string.
			int startExp = statement.indexOf(nextToken) + nextToken.length();
			Debug.logn("index of end of keywords " + startExp, this );
			String rest = statement.substring(startExp + 1, statement.length());
			this.condition = (new VBExpressionFactory()).getExpression(rest, this);

			//this.condition = (new VBExpressionFactory()).getBooleanExpression(this, rest);
		} else {
			conditionInDo = false;
		}
	}

	public String toString() {
		String s = "DoStatement ";
		Debug.logn("" + conditionInDo, this);
		if (conditionInDo) {
			s = s + "doCond ";
		} else {
			s = s + "loopCond ";
		}
		if (isWhile) {
			s = s + "whle ";
		} else {
			s = s + "untl ";
		}
		s = s + condition + " ";
		s = s + this.codeBlock;
		return s;

	}

	protected List tryGetJava() {
		List strings = new ArrayList();
		String negation = "";
		String s;
		if (!this.isWhile()) {
			// then its Until
			negation = "!";
		}
		if (this.isConditionInDo()) {
			s = JavaKeywords.J_WHILE + " (" + negation + "(" + this.condition.asJava() + ")) {";
			strings.add(s);
		} else {
			s = JavaKeywords.J_DO + " {";
			strings.add(s);
		}
		for (Iterator itr = this.getCodeBlock().getJava().iterator(); itr.hasNext();) {
			String next = (String) itr.next();
			s = "\t" + next;
			strings.add(s);
		}
		
		if (this.isConditionInDo()) {
			// that was easy
			s = "}";
			strings.add(s);
		} else {
			s = "} " + JavaKeywords.J_WHILE + " ( " + negation + "(" + this.condition.asJava() + " ));";
			strings.add(s);
                        
		}
		return strings;
	}

	private void parseLoopStatement(String statement) {
		StringTokenizer st = new StringTokenizer(statement);
		String loop = st.nextToken();
		if (conditionInDo == false && !st.hasMoreTokens()) {
			throw new RuntimeException("Do loop with no condition...");
		}
		if (st.hasMoreTokens()) {
			
			String nextToken = st.nextToken();
			if (nextToken.equals(VBKeywords.VB_Until)) {
				isWhile = false;
			} else {
				isWhile = true;
			}
			int indexStartExp = statement.indexOf(nextToken) + nextToken.length();
			String rest = statement.substring(indexStartExp, statement.length());
			this.condition = (new VBExpressionFactory()).getExpression(rest, this);
		}
	}
        
        private static boolean isWhile(String code) {
            String ccode = code.trim();
            if (ccode.startsWith(CSKeywords.CS_While)) {
                String rest = ccode.substring(CSKeywords.CS_While.length(), ccode.length()).trim();
                if (rest.startsWith("(")) {
                    return true;
                }
            }
            return false;
        }
        
        public static String parseCSDoLoop(String doStatement, Iterator itr) {
            String code = doStatement;
            while (itr.hasNext()) {
                String nextStatement = (String) itr.next();
                code = code + nextStatement;
                if (isWhile(nextStatement)) {
                    break; 
                }
            }
            return code;    
        }

	public static String parseVBDoLoop(String doStatement, Iterator itr) {
		String toReturn = "";

		String s = doStatement;
		toReturn = s + "\n";
		int nestingCounter = 0;
		boolean reachedEnd = false;
		while (!reachedEnd) {
			s = (String) itr.next();
			if (s.startsWith(VBKeywords.VB_Do)) {
				nestingCounter++;
			}
			if (s.startsWith(VBKeywords.VB_Loop)) {
				if (nestingCounter == 0) {
					reachedEnd = true;
				} else {
					nestingCounter--;
				}
			}
			toReturn = toReturn  + s + "\n";
		}

		return toReturn;
	}

	private static boolean isLoop(String stat) {
		String s = stat.trim();
		String lw = VBKeywords.VB_Loop;
		boolean b = s.startsWith(lw);
		return b;

	}


	
}

 