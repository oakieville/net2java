
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
package com.sun.dn.parser.expression;

import com.sun.dn.parser.*;
import com.sun.dn.*;
import java.util.*;
import com.sun.dn.util.*;

	/** A VB expression that is only valid within the context
	** of a WithStatement. These are always of the form <br>
	** .[VariableMember] <br>
	** .[MethodCall] <br>
	** @author danny.coward@sun.com
	**
	*/

public class ImpliedExpression extends ExpressionAdapter {
	private Expression ic;

	public ImpliedExpression(String code, InterpretationContext context) {
            super(code, context);
            Debug.logn("Parse " + code, this);
            Debug.logn("implicit variable " + this.getImplicitVariable(context), this);

            String implicitVariableName = this.getImplicitVariable(context).getName();
            ic = (new VBExpressionFactory()).getExpression(implicitVariableName + code, context);
            Debug.logn("Expression to make is " + implicitVariableName + code, this);
            Debug.logn("Made expression " + ic, this);
            Debug.logn("Created Expression " + ic, this);
	}

	private DNVariable getImplicitVariable(InterpretationContext context) {
		boolean found = false;
		InterpretationContext ic = context;
		while (!found) {
			if (ic instanceof CodeBlock) {
				CodeBlock cb = (CodeBlock) ic;
				if (cb.getImplicitVariable() != null) {
					Debug.logn("Found implicit variable on " + ic.getClass(), this);

					return cb.getImplicitVariable();
				}
			}
			Debug.logn("Looked for implicit variable on " + ic.getClass() + " & didn't find", this);
			ic = ic.getParent();
		}
		throw new RuntimeException("Something is wrong: I'm an implied expression that a parent code block with an implicit variable");

	}	

	private CodeBlock getParentCodeBlock(InterpretationContext context) {
		InterpretationContext ic = context;
		boolean found = false;
		while (!found) {
			if (ic instanceof CodeBlock) {
				return (CodeBlock) ic;
			}
			ic = ic.getParent();
		}
		throw new RuntimeException("Something is wrong: I'm an implied expression that can't find its parent code block");
	}

	public static boolean isImpliedExpression(String code, InterpretationContext context) {
		return (code.trim().startsWith("."));
	}

	public MemberAccessExpression getMemberAccessExpression() {
		if (ic instanceof MemberAccessExpression) {
			return (MemberAccessExpression) ic;
		} else {
			return null;
		}
	}

	public String tryAsJava() {
		return ic.asJava();
	}

	public String getTypeName() {
		return ic.getTypeName();
	}

	public DNType getDNType() {
		return ic.getDNType();
	}



}

 