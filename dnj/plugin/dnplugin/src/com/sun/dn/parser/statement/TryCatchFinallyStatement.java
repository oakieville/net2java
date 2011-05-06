
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
import com.sun.dn.Library;

		/** .NET control strcuture for handling exceptions. A Visual Basic example  <br> <br>
	Dim GivenDate As Object   ' Should contain date/time information. <br>
	Dim NextCentury As Date <br>
	Try <br>
	   NextCentury = DateAdd("yyyy", 100, GivenDate) <br>
	   Catch ThisExcep As System.ArgumentException <br>
	   ' At least one argument has an invalid value. <br>
	   Catch ThisExcep As ArgumentOutOfRangeException <br>
	   ' The result is later than December 31, 9999. <br>
	   Catch ThisExcep As InvalidCastException <br>
	   ' GivenDate cannot be interpreted As a date/time. <br>
	   Catch <br>
	   ' An unforeseen exception has occurred. <br>
	   Finally <br>
	   ' This block is always executed before leaving. <br>
	End Try <br>
	@author danny.coward@sun.com
	**/

public class TryCatchFinallyStatement extends ControlFlowStatement {
	private CodeBlock tryBlock;
	private Map catchBlocks = new LinkedHashMap(); // all the case blocks
        private Map whenBlocks = new LinkedHashMap();
	private CodeBlock finallyBlock;
	private Library lib;

	public static boolean isVBTryCatchFinallyStatement(String code, InterpretationContext variables) {
		return code.trim().startsWith(VBKeywords.VB_Try);
	}

	public static boolean isCSTryCatchFinallyStatement(String code, InterpretationContext variables) {
		return code.trim().startsWith(CSKeywords.CS_Try);
	}

	public static TryCatchFinallyStatement createVBTryCatchFinallyStatement(String code, InterpretationContext context) {
		TryCatchFinallyStatement tcfs = new TryCatchFinallyStatement(code, context);
		tcfs.parseVB(code);
		return tcfs;
	}

	public static TryCatchFinallyStatement createCSTryCatchFinallyStatement(String code, InterpretationContext context) {
		TryCatchFinallyStatement tcfs = new TryCatchFinallyStatement(code, context);
		tcfs.parseCS(code);
		return tcfs;
	}

	private TryCatchFinallyStatement(String code, InterpretationContext context) {
		super(code, context);
		this.lib = super.context.getLibrary();
	}

	private CodeBlock getTryCodeBlock() {
		return this.tryBlock;
	}

	private Map getCatchCodeBlocks() {
		return this.catchBlocks;
	}

	private CodeBlock getFinallyCodeBlock() {
		return this.finallyBlock;
	}

	public List getVariables() {
		return super.context.getVariables();
	}

	private CodeBlock getCSCodeBlock(String code) {
		String toParse = code.trim();
		CodeBlock block = new CodeBlock(this);
		List blockStatements = CSClassStatement.tokenizeToClassStatements(toParse);
		block.parseCS(blockStatements );
		return block;
	}

	private void parseCS(String rawCode) {
		
		List blocks = Util.tokenizeSemiColonChunksAndPanhandles(rawCode);
		
		String tryBlockString = ((String) blocks.get(0)).trim();
		
		String tryBlockCode = tryBlockString.substring(tryBlockString.indexOf("{") + 1, tryBlockString.length() -1);
		this.tryBlock = this.getCSCodeBlock(tryBlockCode );
		for (int i = 1; i < blocks.size(); i++) {
			String blockString = (String) blocks.get(i);
			String blockStatementsString = blockString.substring(blockString.indexOf("{") + 1, blockString.length() -1);
			//CodeBlock block = getCSCodeBlock(blockStatementsString);
			CodeBlock block = new CodeBlock(this);
			List blockStatements = CSClassStatement.tokenizeToClassStatements(blockStatementsString);
			String blockDeclaration = blockString.substring(0, blockString.indexOf("{") - 1).trim();
			if (blockDeclaration.startsWith(CSKeywords.CS_Catch)) {
				String exceptionClause = blockDeclaration.substring(blockDeclaration.indexOf("(") + 1, blockDeclaration.indexOf(")"));
				
				StringTokenizer st = new StringTokenizer(exceptionClause);
				String eType = st.nextToken();
				String eName = st.nextToken();
                                
				DNVariable eVariable = DNVariable.createCSVariable(eName, eType);
                                
				block.addImplicitVariable(eVariable );
				block.parseCS(blockStatements );
				catchBlocks.put(eVariable, block);
			} else if (blockDeclaration.startsWith(CSKeywords.CS_Finally)) {
				finallyBlock = block;
			} else {
				throw new RuntimeException("wrong format");
			}
		}
	}
	
	private void parseVB(String rawCode) {
		super.setCode(rawCode);
		Debug.logn("Parse " + rawCode, this);
		StringTokenizer st = new StringTokenizer(rawCode, "\n");
		String nextLine = st.nextToken();
		Debug.logn("First Line is " + nextLine, this);
		List currentStatements = new ArrayList();
		boolean reachedEnd = false;
		boolean doneTry = false;
		int nestingCounter = 0;
		while (!reachedEnd) {
			nextLine = st.nextToken();
			Debug.logn("Reading line " + nextLine, this);
			if (nextLine.startsWith(VBKeywords.VB_Try)) {
				nestingCounter++;
			}
			if (isEndTry(nextLine)) {
				
				if (nestingCounter == 0) {
					reachedEnd = true;
					// it could be a finally or a catch !
					String kw = (String) currentStatements.get(0);
					if (kw.startsWith(VBKeywords.VB_Catch)) {
						this.parseCatchBlock(currentStatements);
					} else {
						this.parseFinallyBlock(currentStatements);
					}
				} else {
					nestingCounter--;
				}
			}
			if (nestingCounter == 0 && (isCatch(nextLine) || isFinally(nextLine)) ) {
				Debug.logn("no nesting and its either catch or finally " + doneTry, this);
				if (!doneTry) {
					this.parseTryBlock(currentStatements );
					currentStatements = new ArrayList();
					doneTry = true;
				} else {
					this.parseCatchBlock(currentStatements);
					currentStatements = new ArrayList();
				}
			}
			currentStatements.add(nextLine);
		}

		Debug.logn("Finished parsing "+this, this);
	}

	private void parseTryBlock(List statements) {
		Debug.logn("TRY  " + statements, this);
		
		this.tryBlock = new CodeBlock(this);
		this.tryBlock.parseVB(statements);
	}

	private void parseCatchBlock(List statements) {
		Debug.logn("CATCH", this);
		Debug.logn(statements, this);
		Iterator itr = statements.iterator();
		String catchDeclaration = (String) itr.next();
		DNVariable exception = this.parseCatchDeclaration(catchDeclaration);
                
		List catchStatements = new ArrayList();
		while (itr.hasNext()) {
			catchStatements .add(itr.next());
		}
		CodeBlock catchBlock = new CodeBlock(this);
		catchBlock.addImplicitVariable(exception);
		catchBlock.parseVB(catchStatements);
		this.catchBlocks.put(exception, catchBlock);
	}

	private DNVariable parseCatchDeclaration(String statement) {
		StringTokenizer st = new StringTokenizer(statement);
		String catchWord = st.nextToken();
		if (!st.hasMoreTokens()) {
			return new CatchEverything("throwable"); // should check variable not in use
		}
		// something As ExceptionType
		String name = st.nextToken();
		String asW = st.nextToken();
		String exceptionType = st.nextToken();

		// yuck needs to be some type checking here. For now we will do this.
		//exceptionType = "Exception";
		DNVariable variable =  DNVariable.createVBVariable(name, exceptionType);
                
                if (st.hasMoreTokens()) {
                    String whenToken = st.nextToken();
                    if (whenToken.equals(VBKeywords.VB_When)) {
                        String whenES = st.nextToken();
                        Expression whenE = (new VBExpressionFactory()).getExpression(whenES, this);
                        this.whenBlocks.put(variable, whenE);
                    } else {
                        throw new RuntimeException("Error parsing: " + statement);
                    }
                }
                return variable;
	}


	private void parseFinallyBlock(List statements) {
		Debug.logn("FINALLY " + statements, this);
		Iterator itr = statements.iterator();
		String finallyWord = (String) itr.next();
		List finallyStatements = new ArrayList();
		while(itr.hasNext()) {
			finallyStatements.add(itr.next());
		}
		this.finallyBlock = new CodeBlock(this);
		this.finallyBlock.parseVB(finallyStatements);
	}

	public static String parseCSTryCatchFinallyStatement(String tryStatement, ListIterator itr) {
		String statement = tryStatement;
		boolean done = false;
		while (itr.hasNext() && !done) {
			String next = (String) itr.next();
			if (next.trim().startsWith(CSKeywords.CS_Catch) || next.trim().startsWith(CSKeywords.CS_Finally)) {
				statement = statement + next;
			} else {
				itr.previous();
				done = true;
			}

		}
		return statement;
	}


	public static String parseVBTryCatchFinallyStatement(String tryStatement, Iterator itr) {
		String nextLine = tryStatement;
		String toReturn = nextLine + "\n";
		
		boolean reachedEnd = false;
		int nestingCounter = 0;
		while (!reachedEnd) {
			nextLine = (String) itr.next();
			if (nextLine.startsWith(VBKeywords.VB_Try)) {
				nestingCounter++;
			}
			if (isEndTry(nextLine)) {
				if (nestingCounter == 0) {
					reachedEnd = true;
				} else {
					nestingCounter--;
				}
			}
			toReturn = toReturn + nextLine + "\n";

		}
		return toReturn ;
	}

	private static boolean isEndTry(String statement) {
		
		String endTry = VBKeywords.VB_End + " " + VBKeywords.VB_Try;
		boolean b = statement.trim().equals(endTry);
		
		return b;
	}

	private  static boolean isCatch(String statement) {
		return statement.startsWith(VBKeywords.VB_Catch);
	}

	private static boolean isFinally(String statement) {
		return statement.startsWith(VBKeywords.VB_Finally);
	}

	public String toString() {
		String s = "TryCatchFinally: t" + "tryBlock" + " ";
		for (Iterator itr = this.catchBlocks.keySet().iterator(); itr.hasNext();) {
			DNVariable v = (DNVariable) itr.next();
			CodeBlock cb = (CodeBlock) this.catchBlocks.get(v);
			s = s + "c " + v + "=" + "cb" + " ";
		}
		s = s + "f " + "this.finallyBlock";
		return s;
	}

	protected List tryGetJava() {
		List strings = new ArrayList();
		String s = JavaKeywords.J_TRY + " {";
		strings.add(s);
		for (Iterator itr = this.getTryCodeBlock().getJava().iterator(); itr.hasNext();) {
			strings.add("\t" + itr.next());
		}
		strings.add("}");
		for (Iterator itr = this.getCatchCodeBlocks().keySet().iterator(); itr.hasNext();) {
			DNVariable v = (DNVariable) itr.next();
			CodeBlock cb = (CodeBlock ) this.getCatchCodeBlocks().get(v); 
                        String javaType = "";
                        if (v instanceof CatchEverything) {
                            javaType = "Throwable";
                            
                        } else {
                            javaType = lib.getJavaTypeFor(v.getType());
                        }
			s = JavaKeywords.J_CATCH + " ( " + javaType  + " " + v.getName() + " ) {";

			strings.add(s);
                        String wbIndent = "";
                        if (this.whenBlocks.get(v) != null) {
                            Expression whenE = (Expression) this.whenBlocks.get(v);
                            strings.add("\t" + JavaKeywords.J_IF + " (" + whenE.asJava() + ") {");
                            wbIndent = "\t";
                        }
			for (Iterator itrr = cb.getJava().iterator(); itrr.hasNext();) {
				strings.add(wbIndent + "\t" + itrr.next());
			}
                        if (!"".equals(wbIndent)) {
                            strings.add(wbIndent + "}");
                        }
			strings.add("}");
		}
		if (this.getFinallyCodeBlock() != null) {
			s = JavaKeywords.J_FINALLY + " {";
			strings.add(s);
			for (Iterator itr = this.getFinallyCodeBlock().getJava().iterator(); itr.hasNext();) {
				strings.add("\t" + itr.next());
			}
			strings.add("}");
		}
		return strings;
	}

}


 