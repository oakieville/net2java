
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
import com.sun.dn.java.*;
import com.sun.dn.parser.expression.*;
import com.sun.dn.util.*;

	/** A .NET control flow structure for iterating through
	** a collection. In VB for example, this is defined as <br> <br>
	For Each element [ As datatype ] In group <br>
		[ statements ] <br>
	 [ Exit For ] [ statements ] <br> 
	Next [ element ] <br>
	@author danny.coward@sun.com
	*/

public class ForEachNextStatement extends ControlFlowStatement {
	private static String endStatement = VBKeywords.VB_Next;
	private DNVariable counter;
	private CodeBlock codeBlock;
	private Expression collectionExpression;
	private static String INTERNAL_COUNTER_BASE_NAME = "forEachVar";

	public static boolean isVBForEachNextStatement(String statement, InterpretationContext context) {
		return statement.trim().startsWith(VBKeywords.VB_For + " " + VBKeywords.VB_Each);
	}
        
        public static boolean isCSForEachNextStatement(String statement, InterpretationContext context) {
		if ( statement.trim().startsWith(CSKeywords.CS_Foreach)) {
                    String rest = statement.trim().substring(CSKeywords.CS_Foreach.length(), statement.trim().length());
                    if (rest.trim().startsWith("(")) {
                        return true;
                    }
                } 
                return false;
	}

	public List getVariables() {
		List l = super.context.getVariables();
                l.add(this.counter);
                return l;
	}
        
        public static ForEachNextStatement createVBForEachNextStatement(String code, InterpretationContext context) {
            ForEachNextStatement fens = new ForEachNextStatement(code, context);
            fens.parseVB(code);
            return fens;
        }
        
        public static ForEachNextStatement createCSForEachNextStatement(String code, InterpretationContext context) {
            ForEachNextStatement fens = new ForEachNextStatement(code, context);
            fens.parseCS(code);
            return fens;
        }

	private ForEachNextStatement(String code, InterpretationContext context) {
		super(code, context);
                
                context.getMetaClass().addJavaImport("com.sun.dn.library.Microsoft.VisualBasic.CollectionSupport");
	}
	
	private String getInternalCounterName() {
		int ctr = 0;
		InterpretationContext ic = super.context;
		while (ic != null) {
			ic = ic.getParent();
			if (ic instanceof ForEachNextStatement) {
				ctr++;
			}
		}
		return INTERNAL_COUNTER_BASE_NAME + ctr;
	}
        
        public static String parseCSForEachLoop(String forEachNextStatement, Iterator itr) {
            return forEachNextStatement.trim();
        }

	public static String parseVBForEachLoop(String forEachNextStatement, Iterator itr) {
				
		String nextStatement = forEachNextStatement;
		String statements = nextStatement;
		int nestingCounter = 0;
		boolean done = false;
		while (!done) {
			nextStatement = (String) itr.next();
			statements = statements + "\n" + nextStatement;
			if (nextStatement.trim().startsWith(VBKeywords.VB_For)) {
				nestingCounter = 0;
			}

			if (isNext(nextStatement.trim())) {
				if (nestingCounter == 0) {
					done = true;
				} else {
					nestingCounter--;
				}
			}

		}
		Debug.clogn("ForNextEach statements = " + statements, ForEachNextStatement.class);
		return statements;
	}

	private void parseVB(String rawCode) {
		//throw new Stop(this.getClass());
		Debug.logn("Parse ForNextEach " + rawCode, this);
                super.setCode(rawCode);
		List lines = Util.tokenizeIgnoringEnclosers(rawCode, "\n");
		String forNextEachDecl = (String) lines.get(0);
		this.parseDeclaration(forNextEachDecl);

		List codeBlockStatements = new ArrayList();
		for (int i = 1; i < lines.size() - 1; i++ ) {
			codeBlockStatements.add(lines.get(i));
		}
		this.codeBlock = new CodeBlock(this);
		this.codeBlock.parseVB(codeBlockStatements);
                

		Debug.logn("Code Block = " + codeBlock, this);
	}
        
        //** foreach (type identifier in expression) statement */
        private void parseCS(String rawCode) {
            String declaration = rawCode.substring(rawCode.indexOf("("), rawCode.indexOf("{"));
            declaration = Util.stripBrackets(declaration);
            List declList = Util.tokenizeIgnoringEnclosers(declaration, " ");
            String type = (String) declList.get(0);
            String identifier = (String) declList.get(1);
            String collectionExp = (String) declList.get(3);
            
            this.counter = DNVariable.createVBVariable(identifier, type);
            this.collectionExpression = (new CSExpressionFactory()).getExpression(collectionExp, this);
            String codeBlockCode = rawCode.substring(rawCode.indexOf("{") + 1, rawCode.lastIndexOf("}")).trim();
            this.codeBlock = new CodeBlock(this);
            List codeBlockL = CSClassStatement.tokenizeToClassStatements(codeBlockCode);
            this.codeBlock.parseCS(codeBlockL);
        }

	private void parseDeclaration(String declaration) {
		List words = Util.tokenizeIgnoringEnclosers(declaration, " ");
		String counterName = (String) words.get(2);
		String next = (String) words.get(3);
		int indexOfIn = -1;
		if (next.equals(VBKeywords.VB_As)) {
			String counterType = (String) words.get(4);
			counter = DNVariable.createVBVariable(counterName, counterType);
			indexOfIn = 5;	
		} else {
			counter = super.context.getVariable(counterName);
			indexOfIn = 3;
		}
		Debug.logn("Counter = " + counter, this);
		String collectionName = (String) words.get(4);
		collectionExpression = (new VBExpressionFactory()).getExpression(collectionName, this);
		Debug.logn("Group (collection) name is " + collectionName, this);
		Debug.logn("Group (collection) expression is " + collectionExpression, this);
	}

	protected List tryGetJava() {
		List strings = new ArrayList();
		String ctrName = this.getInternalCounterName();

		// either the collection expression is not a VB multidimensional array, in which case
		// we model it as a *Java List*
		// or it is a VB multidimensional array and we 
		// model is as a *Java array*

		boolean usingList = collectionExpression.getDNType().isPoint();

		String upperBoundAsJava = "";
		String itemAsJava = "";
		if (usingList) {
			upperBoundAsJava = collectionExpression.asJava() + ".size()";
			itemAsJava = "CollectionSupport.getItem("+collectionExpression.asJava() +", "+ctrName+"+1)";
		} else {
			upperBoundAsJava = collectionExpression.asJava() + ".length";
			itemAsJava = collectionExpression.asJava() +"["+ctrName+"]";
		}
		String s = JavaKeywords.J_FOR + " (int " + ctrName + " = 0; "+ctrName+" < " + upperBoundAsJava +"; "+ctrName+"++) {";
		strings.add(s);
                if (this.context.getVariable(counter.getName()) != null) {
                    s = "\t";
                } else {
                    s = "\t" + counter.getType() + " ";
                }
		s = s + counter.getName() + " = "+itemAsJava+";";
		strings.add(s);

		List getJava = this.codeBlock.getJava();
		for (Iterator itr = getJava.iterator(); itr.hasNext();) {
			String next = (String) itr.next();
			s = "\t" + next;
			strings.add(s);
		}

		strings.add("}");
		Debug.logn("Java code is " + strings, this);
		return strings;
	}
	
	private static boolean isNext(String statement) {
		StringTokenizer st = new StringTokenizer(statement);
		return st.nextToken().equals(VBKeywords.VB_Next);
	}


}
 