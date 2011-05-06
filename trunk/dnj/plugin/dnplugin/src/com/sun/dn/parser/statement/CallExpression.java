
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
import com.sun.dn.parser.expression.*;
import com.sun.dn.*;
import com.sun.dn.parser.*;
import com.sun.dn.util.*;

	/** A statement that is a method call. <br>
	** like apple.Peel()
	** @author danny.coward@sun.com
	*/


public class CallExpression extends StatementAdapter {
	private Expression ic;

	private CallExpression(String code, InterpretationContext context) {
            super(code, context);
	}
        
        public static CallExpression createVBCallExpression(String code, InterpretationContext context) {
            CallExpression ce = new CallExpression(code, context);
            ce.parseVB(code.trim());
            return ce;
        }
        
        public static CallExpression createCSCallExpression(String code, InterpretationContext context) {
            CallExpression ce = new CallExpression(code, context);
            ce.parseCS(code);
            return ce;
        }

	public Expression getExpression() {
		return this.ic;
	}

	protected List tryGetJava() {
		List getJava = new ArrayList();
		getJava.add(this.asJava());
		return getJava;
	}

	private String asJava() {
		return ic.asJava() + ";";
	}

	public InterpretationContext getContext() {
		return this.context;
	}

	public static boolean isCallExpression(String code) {
		return true;
	}

	public static boolean isCSCallExpression(String code) {
		return true;
	}

	public static boolean isVariable(InterpretationContext variables, String code) {
		return (null != getVariable(variables, code));	
	}

	public static DNVariable getVariable(InterpretationContext variables, String code) {
		Debug.clogn("Get Variable " + variables + " from " + code, CallExpression.class);
		for (Iterator itr = variables.getVariables().iterator(); itr.hasNext();) {
			DNVariable v = (DNVariable) itr.next();
			if (code.trim().equals(v.getName())) {
				return v;
			}
		}
		return null;		
	}

	private void parseVB(String code) {
                Debug.logn("Parse " + code, this);
		if (ImpliedExpression.isImpliedExpression(code.trim(), this.context)) {
			this.ic = new ImpliedExpression(code, this.context);
			this.addConstructedPreStatementsFrom(this.ic);
		} else {
                        String invCode = code;
                        if (code.startsWith(VBKeywords.VB_Call + " ")) {
                            invCode = code.substring(VBKeywords.VB_Call.length() + 1, code.length()).trim();
                        }
			this.ic = InvocationExpression.createVBInvocationExpression(invCode, this.context);
			this.addConstructedPreStatementsFrom(this.ic);
		}
	}

	private void parseCS(String code) {
            this.ic = InvocationExpression.createCSInvocationExpression(code, this.context);
	}

	public String toString() {
		return "CllExp: ic-" + ic;
	}

}
 