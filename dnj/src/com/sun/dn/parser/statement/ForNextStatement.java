
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
import com.sun.dn.java.JavaKeywords;
import com.sun.dn.parser.*;
import com.sun.dn.*;
import com.sun.dn.parser.expression.*;
import com.sun.dn.util.*;

	/** A VB control flow structure for iterating using a counter. Defined as <br>
	For counter [ As datatype ] = start To end [ Step step ] <br>
   	' Statement block to be executed for each value of counter. <br>
	Next [ counter ] <br>
	@author danny.coward@sun.com
	*/

public class ForNextStatement extends ControlFlowStatement {
	private DNVariable counter;
	private boolean isCounterInContext = false;
	private Expression startE;
	private Expression endE;
	private Expression stepE;
	private CodeBlock codeBlock;

	public static boolean isVBForNextStatement(String statement, InterpretationContext context) {
		return statement.trim().startsWith(VBKeywords.VB_For) &&
                        Util.codeContains(statement, VBKeywords.VB_For) 
				&& !ForEachNextStatement.isVBForEachNextStatement(statement, context);
	}

	public static boolean isCSForNextStatement(String statement, InterpretationContext context) {
		if ( statement.trim().startsWith(CSKeywords.CS_For)) {
                    String rest = statement.trim().substring(CSKeywords.CS_For.length(), statement.trim().length());
                    if (rest.trim().startsWith("(")) {
                        return true;
                    }
                } 
                return false;
	}

	public static ForNextStatement createCSForNextStatement(String code, InterpretationContext context) {
		ForNextStatement fns = new ForNextStatement(code, context);
                fns.stepE = IntegerLiteral.createCSIntegerLiteral("1", fns); 
		fns.parseCS(code.trim());
		return fns;
	}


	public static ForNextStatement createVBForNextStatement(String code, InterpretationContext context) {
		ForNextStatement fns = new ForNextStatement(code, context); 
		fns.stepE = IntegerLiteral.createVBIntegerLiteral("1", fns); 
		fns.parseVB(code);
		return fns;
	}

	private ForNextStatement(String code, InterpretationContext variables) {
		super(code, variables);
	}

	private boolean isCounterInContext() {
		return isCounterInContext;
	}

	private DNVariable getCounter() {
		return this.counter;
	}

	private CodeBlock getCodeBlock() {
		return this.codeBlock;
	}
	
	public List getVariables() {
            List myVariables = new ArrayList();
            myVariables.add(this.counter);
            myVariables.addAll(this.context.getVariables());
            return myVariables;
	}

	private void parseCS(String code) {
		Debug.logn("Parse " + code, this);
                super.setCode(code);
		String forDeclaration = code.substring(code.indexOf("("), code.indexOf("{")).trim();
		forDeclaration = Util.stripBrackets(forDeclaration);
		this.parseCSForDeclaration(forDeclaration);

		String forBlockStatementsS = code.substring(code.indexOf("{")+ 1, code.length() - 1).trim();

		List forBlockStatements = CSClassStatement.tokenizeToClassStatements(forBlockStatementsS);
		this.codeBlock = new CodeBlock(this);
		codeBlock.addImplicitVariable(this.counter);
		codeBlock.parseCS(forBlockStatements);
		//Debug.stop(this);
	}

	private void parseCSForDeclaration(String decl) {
		Debug.logn("Parse Fro decl " + decl, this);
		ExpressionFactory factory = new CSExpressionFactory();
		StringTokenizer st = new StringTokenizer(decl, ";");
		String varString = st.nextToken();
		String maxConditionString = st.nextToken();
		String stepString = st.nextToken();
		List varTokens = Util.tokenizeIgnoringEnclosers(varString, " ");
		String varType = "";
		String varName = "";
		String intialValueS = "";

		if (varTokens.get(2).equals("=")) {
			varType = (String) varTokens.get(0);
			varName = (String) varTokens.get(1);
			intialValueS = (String) varTokens.get(3);
			this.counter = DNVariable.createCSVariable(varName, varType);
		} else {
			varName = (String) varTokens.get(0);
			intialValueS = (String) varTokens.get(2);
			this.counter = this.context.getVariable(varName);
		}
		this.startE = factory.getExpression(intialValueS, this.context);
		
		List endTokens = Util.tokenizeIgnoringEnclosers(maxConditionString.trim(), " ");
		String endString = (String) endTokens.get(2);
		
		this.endE = factory.getExpression(endString, this.context);

		if (stepString.trim().equals(varName + "++")) {
			// the default is 1, which is already set
		} else {
			throw new RuntimeException("Need to implement step parsing properly");
		}
	}

	private void parseVB(String rawCode) {
		Debug.logn("Parse ForNext " + rawCode, this);
                super.setCode(rawCode);
		StringTokenizer st = new StringTokenizer(rawCode, "\n");
		String forNextDecl = st.nextToken();
		this.parseVBForDeclaration(forNextDecl);
		
		List codeBlockStatements = new ArrayList();
		while(st.hasMoreTokens()) {
			String next = st.nextToken();
			if (getEndStatement(this.counter.getName()).equals(next) || next.equals(VBKeywords.VB_Next)) {
				//reached the end
			} else {
				codeBlockStatements.add(next);
			}
		}

		Debug.logn(codeBlockStatements, this);
		this.codeBlock = new CodeBlock(this);
		this.codeBlock.parseVB(codeBlockStatements);
		Debug.logn(this, this);		
	}

	private void parseVBForDeclaration(String statement) {
            StringTokenizer st = new StringTokenizer(statement.trim());
            String forWord = st.nextToken();
            String counterName = st.nextToken();
            String nextToken = st.nextToken();
            String type = "";
            Debug.logn("nextToken ." + nextToken + ".", this);
            if (nextToken.equals(VBKeywords.VB_As)) {
                type = st.nextToken();
		Debug.logn("Type " + type, this);
		this.counter = DNVariable.createVBVariable(counterName, type);
		isCounterInContext = false;
		st.nextToken(); // skip = sign
            } else {
            	// nextToken is the equals sign
		// it needs to be resolved within its context
		DNVariable var = this.context.getVariable(counterName); //
		Debug.logn("Variables in context are: " + this.context.getVariables(), this);
		if (var != null) {
                    isCounterInContext = true;
                    this.counter = var;
                
		} else {
                    throw new RuntimeException("Error matching counter name=" + counterName + "  in context " + var);
		}
            }
            //String startS = st.nextToken();
            String startS = getUpTo(VBKeywords.VB_To, st);
                
            startE = (new VBExpressionFactory()).getExpression(startS , this);

            Debug.logn("this.startExpression " + startE, this );
            

            String endS = getUpTo(VBKeywords.VB_Step, st);
		
            this.endE = (new VBExpressionFactory()).getExpression(endS, this);
            Debug.logn("this.endExpression " + endE, this );
            
            if (st.hasMoreTokens()) {
                
                String stepString = getUpTo("Never get there", st);
                Debug.logn("Step stepString = " + stepString, this);
		this.stepE = (new VBExpressionFactory()).getExpression(stepString, this);
                
		Debug.logn("Step Exp = " + stepE , this);
            }
	}
        
        String getUpTo(String nextKW, StringTokenizer st) {
            String s = "";
            String next = "";
            while(st.hasMoreTokens()) {
                next = st.nextToken();
                if (next.equals(nextKW)) {
                    return s.trim();
                } else {
                    s = s + " " + next;
                }
            }
            return s.trim();
            
        }

	private static String getEndStatement(String variableName) {
		return VBKeywords.VB_Next + " " + variableName;

	}

	public static String parseVBForLoop(String forNextStatement, Iterator itr) {
		String statements = "";
	
		// chop off the end leaving only the For...Next code
	

		StringTokenizer st = new StringTokenizer(forNextStatement);
		String forWord = st.nextToken();
		String variableName = st.nextToken();
		String endStatement = getEndStatement(variableName);
				
		String nextStatement = forNextStatement;
		statements = statements + nextStatement + "\n";
		boolean done = false;
		while (!done) {
			nextStatement = (String) itr.next();
			statements = statements + nextStatement + "\n";
			if (nextStatement.equals(endStatement) || nextStatement.equals(VBKeywords.VB_Next)) {
				done = true;
			}
		}
		Debug.clogn("ForNext statements = " + statements, ForNextStatement.class);
		return statements;
	}


	private static boolean isFor(String statement) {
		StringTokenizer st = new StringTokenizer(statement);
		return st.nextToken().equals(VBKeywords.VB_For);
	}

	private static boolean isNext(String statement) {
		StringTokenizer st = new StringTokenizer(statement);
		return st.nextToken().equals(VBKeywords.VB_Next);
	}
        
        private boolean isSteppingUp() {
            if (stepE instanceof SignedNumeric) {
                return ((SignedNumeric) stepE).isPositive();
            } else {
                return true; // f it !!
            }
        }

	protected List tryGetJava() {
		List strings = new ArrayList();
		String s = JavaKeywords.J_FOR + " (";
		if (!this.isCounterInContext()) {
			s = s + JavaKeywords.J_INT + " ";
		}
		String cn = this.getCounter().getName();
		s = s + cn + " = " + this.startE.asJava() + "; ";
                if (this.isSteppingUp()) {
                    s = s + cn + " <= " + this.endE.asJava() + "; ";
                } else {
                    s = s + cn + " >= " + this.endE.asJava() + "; ";
                }
   
		s = s + cn + " = " + cn + " + " + stepE.asJava() + ") {";
		strings.add(s);

		List getJava = this.getCodeBlock().getJava();
		for (Iterator itr = getJava.iterator(); itr.hasNext();) {
			String next = (String) itr.next();
			s = "\t" + next;
			strings.add(s);
		}
		
		s = "}";
		strings.add(s);

		return strings;
	}

	public String toString() {
		return "ForNext: c " + this.counter + " start " + startE + 
				" end " + endE + " step " + stepE + " code " + this.codeBlock;
	}


}
 