
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
import com.sun.dn.*;
import com.sun.dn.parser.*;
import com.sun.dn.util.*;

	/** A VB statement that is a subroutine, i.e. a block of code
	** with no return value that takes some parameters. <br>
	[ <attrlist> ] [{ Overloads | Overrides | Overridable |  <br>
	NotOverridable | MustOverride | Shadows | Shared }]  <br>
	[{ Public | Protected | Friend | Protected Friend | Private }]  <br>
	Sub name [(arglist)] [ Implements interface.definedname ] <br>
	   [ statements ] <br>
	   [ Exit Sub ] <br>
	   [ statements ] <br>
	End Sub <br>

	2 keywords Sub name args Implemements <br>
	@author danny.coward@sun.com
	*/

public class VBSubroutineStatement extends MemberStatement {
	protected List javaSupplements = new ArrayList();

	public VBSubroutineStatement (String code, InterpretationContext context) {
		super(code, context);
	}
        
        public static boolean isVBSubroutineStatement(String code, InterpretationContext context) {
            return Util.codeContains(code , VBKeywords.VB_Sub) && !DelegateStatementImpl.isVBDelegateStatement(code, context);
        }

	public List getJavaSupplements() {
		return javaSupplements;
	}

	public void addJavaSupplement(String javaCode) {
		this.javaSupplements.add(javaCode);
	}

	public boolean isConstructor() {
		return super.getName().equals(VBKeywords.VB_New);
	}
        
        public static VBSubroutineStatement createConstructor(InterpretationContext context) {
            VBSubroutineStatement vbs = new VBSubroutineStatement("<created by parser>", context);
            vbs.sig = new Signature("Public Sub New()");
            return vbs;
        }

	public boolean isEventHandler() {
		return this.sig.getHandlesClause() != null;
	}

	public VBHandlesClause getHandlesClause() {
		return new VBHandlesClause(this.sig.getHandlesClause(), super.context);
	}

	private static boolean isEndSub(String statement) {
		return statement.trim().startsWith(VBKeywords.VB_End + " " + VBKeywords.VB_Sub);
	}

	public static List getStatements(String firstLine, Iterator itr, boolean inInterface ) {
		Debug.clogn("Get statements " + firstLine, VBSubroutineStatement.class);
		List l = new ArrayList();
                
		l.add(firstLine);
                List tokns = Util.tokenizeIgnoringEnclosers(firstLine, " ");
                tokns = Util.trimStrings(tokns);
		if (inInterface || tokns.contains(VBKeywords.VB_MustOverride) || tokns.contains(VBKeywords.VB_Declare)) {
			return l;
		} else {
			String next = "";
			while ( !isEndSub(next) ) {
				next = (String) itr.next();
				Debug.clogn("-next is " + next, VBSubroutineStatement.class);

				l.add(next);
			}
			return l;
		}
	}

	// need to parse the args list
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
		
		List bodyStatements = new ArrayList();
		for (int i = 1; i < allPassed.size(); i++) {
			String nextStatement = (String) allPassed.get(i);
			if (!isEndSub(nextStatement)) {
				bodyStatements.add(nextStatement);
			}
		}
		Debug.logn("about to parse body... ", this);

		Parser.parseStatementStrings(bodyStatements, this, new VBStatementFactory());
		Debug.logn("Finished Parsing " + this, this);
	}

	public String toString() {
		return "Subroutine: sig-" + this.sig + " statements-" + statements;
	}

}

 