
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
import com.sun.dn.*;
import com.sun.dn.parser.*;
import com.sun.dn.java.*;
import com.sun.dn.util.*;

	/** A VB expression to test if one expression is the same type as
	** the other. Defined as <br>
	**
	** IsOperatorExpression ::= Expression Is Expression
	** @author danny.coward@sun.com
	*/

public class IsOrIsNotExpression extends SimpleExpression implements BooleanExpression {
	private Expression left;
	private Expression right;
        private boolean is = true;

	public static boolean isVBIsOrIsNotExpression(String code, InterpretationContext context) {
		List l = Util.tokenizeIgnoringEnclosers(code, " ");
		if (l.size() > 2) {
                    String middle = (String) l.get(1);
                    return middle.equals(VBKeywords.VB_Is) || middle.equals(VBKeywords.VB_IsNot);
		}
		return false;
	}
        
        private IsOrIsNotExpression(String code, InterpretationContext context) {
            super(code, context);
        }

	public static IsOrIsNotExpression createVBIsOrIsNotExpression(String code, InterpretationContext context) {
                IsOrIsNotExpression is = new IsOrIsNotExpression(code, context);
		is.parseVB(code, context);
                return is;
	}
        
        private void parseVB(String code, InterpretationContext context) {
            if (!isVBIsOrIsNotExpression(code, context)) {
			throw new RuntimeException("Illegal");
		}
                this.context = context;
		List l = Util.tokenizeIgnoringEnclosers(code, " ");
		this.left = (new VBExpressionFactory()).getExpression((String) l.get(0), context);
                if ( VBKeywords.VB_Is.equals( ((String) l.get(1)).trim() ) ) {
                    is = true;
                } else {
                    is = false;
                }
		this.right = (new VBExpressionFactory()).getExpression((String) l.get(2), context);
            
        }

	public String getTypeName() {
		return "Boolean";
	}
        
        public DNType getDNType() {
            return this.context.getLibrary().getProgramDefinedOrLibraryDNTypeFor(this.getTypeName());
        }

	public String tryAsJava() {
		if (is) {
                    return left.asJava() + " == " + right.asJava();
                } else {
                    return left.asJava() + " != " + right.asJava();
                }
	}

}
 