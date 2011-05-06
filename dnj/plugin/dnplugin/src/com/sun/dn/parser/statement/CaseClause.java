
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
import com.sun.dn.util.*;

	/** A statement that is one of the test clauses in a SelectStatement.
	** @author danny.coward@sun.com
	*/

public class CaseClause {
	private Expression expression;
	private InterpretationContext context;
	private Expression lower;
	private Expression upper;
	private String code;
	

	public CaseClause(Expression expression, InterpretationContext context) {
		this.expression = expression;
		this.context = context;
		Debug.logn("Created with " + expression, this);
	}
        
        public static CaseClause createCSCaseClause(String code, Expression expression, InterpretationContext context) {
            CaseClause cc = new CaseClause(expression, context);
            cc.parseCS(code);
            return cc;
        }
        
        public static CaseClause createVBCaseClause(String code, Expression expression, InterpretationContext context) {
            CaseClause cc = new CaseClause(expression, context);
            cc.parseVB(code);
            return cc;
        }

	private void parseCS(String code) {
		this.code = code;
		this.lower = (new CSExpressionFactory()).getExpression(code, this.context, false);
	}

	private void parseVB(String code) {
		Debug.logn("Parse " + code, this);
		this.code = code;
		List l = this.toTokens(code);
		String upperS = "";
		String lowerS = "";
		if (l.size() == 3) { // dealing with ranges...
			if (!l.get(1).equals(VBKeywords.VB_To)) {
				throw new RuntimeException("Can't deal with wrong format " + code);
			}
			lowerS = (String) l.get(0);
			upperS = (String) l.get(2);
                        
                        lower = (new VBExpressionFactory()).getExpression(lowerS, this.context, false);
                        upper = (new VBExpressionFactory()).getExpression(upperS, this.context, false);
                        
		} else if (l.size() == 1) { // then its an expression
			lowerS = (String) l.get(0);
			lower = (new VBExpressionFactory()).getExpression(lowerS, this.context);

		} else {
			throw new RuntimeException("Can't deal with " + code);
		}

		Debug.logn("Done " + expression + " l: " + lower + " u:" + upper, this);

	}

	public String asJava() {
		String s = "";
		if (upper == null) {
			Debug.logn(lower, this);
			Debug.logn("***HereA", this);
			s = RelationalOperatorExpression.equalityAsJava(lower, this.expression, context.getLibrary()); 
                        Debug.logn("***HereB", this);
		} else {
			s = lower.asJava() + " <= " + this.expression.asJava() + " && ";
			s = s + this.expression.asJava() + " <= " + upper.asJava();
		}
		Debug.logn("as Java = " + s, this);
		return s;
	}
        
       

	public String toString() {
		return "Case Clause";
	}
	
	private List toTokens(String s) {
		List l = new ArrayList();
		StringTokenizer st = new StringTokenizer(s);
		while(st.hasMoreTokens()) {
			l.add(st.nextToken());
		}
		return l;
	}

	private List getOperators() {
		List l = new ArrayList();
		l.add(VBKeywords.VB_To);
		l.add(VBKeywords.VB_Is);
		return l;
	}

	public boolean equals(Object o) {
		return false;
	}

}
 