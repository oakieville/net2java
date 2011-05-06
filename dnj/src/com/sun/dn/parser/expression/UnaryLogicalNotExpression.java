
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

import java.util.*;
import com.sun.dn.parser.*;
import com.sun.dn.*;
import com.sun.dn.util.*;

	/** A VB expression that negates an expression. Defined as <br>
	UnaryLogicalNotExpression ::= Not Expression
	** @author danny.coward@sun.com
	*/

public class UnaryLogicalNotExpression extends ExpressionAdapter implements BooleanExpression {
	private Expression expression;
        
        private UnaryLogicalNotExpression(String code, InterpretationContext context) {
            super(code, context);
        }
        
        public static UnaryLogicalNotExpression createVBUnaryLogicalNotExpression(String code, InterpretationContext context) {
            UnaryLogicalNotExpression ulne = new UnaryLogicalNotExpression(code, context);
            ulne.parseVB(code);
            return ulne;
        }
        
        public static UnaryLogicalNotExpression createCSUnaryLogicalNotExpression(String code, InterpretationContext context) {
            UnaryLogicalNotExpression ulne = new UnaryLogicalNotExpression(code, context);
            ulne.parseCS(code);
            return ulne;
        }

	public String getTypeName() {
		return "System.Boolean";
	}

	private void parse(String code, String op, ExpressionFactory factory) {
		String s = code.trim();
		String expS = s.substring(s.indexOf(op) + op.length(), s.length()).trim();
		this.expression = factory.getExpression(expS, this.context);
		if (this.expression == null) {
			throw new RuntimeException("Couldn't get expression from " + expS);
		}
	}

	public DNType getDNType() {
		return this.context.getLibrary().getProgramDefinedOrLibraryDNTypeFor(this.getTypeName());
	}

	private void parseCS(String code) {
		this.parse(code, "!", new CSExpressionFactory());
	}	


	private void parseVB(String code) {
		this.parse(code, VBKeywords.VB_Not, new VBExpressionFactory());
	}

	public String tryAsJava() {
		return "!" + "(" + expression.asJava() + ")";
	}

	public String toString() {
		return "UnaryLogicalNot( " + this.expression + ")";
	}
}
 