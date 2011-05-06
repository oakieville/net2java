
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
import com.sun.dn.util.*;


	/** A VB statement that is a function. Defined as <br>
	[ <attrlist> ] [{ Overloads | Overrides | Overridable | NotOverridable | MustOverride | Shadows | Shared }]  <br>
	[{ Public | Protected | Friend | Protected Friend | Private }]  <br>
	Function name[(arglist)] <br>
	 [ As type ]  <br>
	[ Implements interface.definedname ] [ statements ] <br>
	 [ Exit Function ] [ statements ] <br>
	 End Function <br>

	2 args Function name args as return Implements.. <br>
	@author danny.coward@sun.com
	*/


public class VBFunctionStatement extends MemberStatement {

	public VBFunctionStatement(String code, InterpretationContext context) {
		super(code, context);
	}
        
        public static boolean isVBFunctionStatement(String code, InterpretationContext context) {
            return Util.codeContains(code , VBKeywords.VB_Function) && !DelegateStatementImpl.isVBDelegateStatement(code, context);
        }

	protected boolean isEndFunction(String statement) {
		return statement.trim().startsWith(VBKeywords.VB_End + " " + VBKeywords.VB_Function);
	}

	public static List getStatements(String firstLine, Iterator itr, boolean inInterface, VBFunctionStatement st) {
            List l = new ArrayList();
            l.add(firstLine);
            List tokns = Util.tokenizeIgnoringEnclosers(firstLine, " ");
            tokns = Util.trimStrings(tokns);
            if (inInterface || tokns.contains(VBKeywords.VB_MustOverride) || tokns.contains(VBKeywords.VB_Declare)) {
		return l;
            } else {
		String next = "";
		while ( !st.isEndFunction(next) ) {
                    next = (String) itr.next();
                    l.add(next);
		}
		return l;
            }
	}

	// functions implicitly have a variable in context
	// that is used for the return type
	// Function foo asString
	//	foo = "a string to rerturn"
	//   End Function
	// is legal...
	//

	public void parse(List allPassed) {
            //Debug.logn("Parse " + allPassed, this);
            if (allPassed.size() == 0) {
		throw new RuntimeException("AHAHAHAHA!!!");
            }

            String sigString = (String) allPassed.get(0);
            
            this.sig = new Signature(sigString);

            
            if (sig.isExternal()) {
                ParseTree pt = ParseTree.getParseTree(this.context);
                TranslationWarning trw = new TranslationWarning(sigString, "This function is implemented in an external dll.");
                pt.getTranslationReport().addTranslationWarning(trw);
             }
             super.constructedPreStatements.addAll(this.sig.getComments());
             if (allPassed.size() == 1) {
		super.isInterfaceMemberOrAbstract = true;
		return;
              }
             
            DNVariable defaultReturnVariable = DNVariable.createVBVariable(this.getJavaName(), sig.getReturnType());
            LocalVariableDeclaration defaultVariableDecl = new LocalVariableDeclaration(this, defaultReturnVariable);
            super.addStatement(defaultVariableDecl);
            
            List bodyStatements = new ArrayList();
            for (int i = 1; i < allPassed.size(); i++) {
		String nextStatement = (String) allPassed.get(i);
		if (!this.isEndFunction(nextStatement)) {
			bodyStatements.add(nextStatement);
		}
            }
            Parser.parseStatementStrings(bodyStatements, this, new VBStatementFactory());

            if (!this.hasExplicitReturnStatement(allPassed)) {
		ReturnStatement rs = ReturnStatement.createVBReturnStatement(this, defaultReturnVariable);
		super.addStatement(rs);
            }
            Debug.logn("" + this, this);
	}

	private boolean hasExplicitReturnStatement(List statements) {
            Debug.logn("Does this have a return statement ? " + statements, this);
            for (Iterator itr = statements.iterator(); itr.hasNext();) {
                String next = (String) itr.next();
                if (ReturnStatement.isVBReturnStatement(next, context)) {
                    Debug.logn("yes", this);
                    return true;
                }
            }
            Debug.logn("no", this);
            return false;
	}

	public String toString() {
		return "Function: name-" + this.getName() + " statements-" + statements;
	}

}

 