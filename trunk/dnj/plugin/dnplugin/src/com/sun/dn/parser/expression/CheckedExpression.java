
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

public class CheckedExpression extends ExpressionAdapter {
	private Expression expression;
	private String keyword;
        
        private CheckedExpression(String code, InterpretationContext context) {
            super(code, context);
        }

	public static boolean isCheckedExpression(String code, InterpretationContext context) {
		return code.startsWith(CSKeywords.CS_Checked) ||
                        code.startsWith(CSKeywords.CS_Unchecked);
	}
        
        private static boolean isUsing(String kw, String ccode) {
             if ( ccode.trim().startsWith(kw) ) { 
                    String rest = ccode.trim().substring(kw.length(), ccode.trim().length()); 
                    if (rest.trim().startsWith("(")) {
                        return true;
                    }
                } 
                return false;
        }

	public static CheckedExpression createCheckedExpression(String code, InterpretationContext context) {
                CheckedExpression is = new CheckedExpression(code, context);
		is.parseVB(code.trim(), context);
                return is;
	}
        
        private void parseVB(String code, InterpretationContext context) {
            if (isUsing(CSKeywords.CS_Checked, code)) {
                keyword = CSKeywords.CS_Checked;
            } else {
                keyword = CSKeywords.CS_Unchecked;
            }
            this.context = context;
            String expressionS = code.substring(keyword.length() + 1, code.length()-1);
            this.expression = (new VBExpressionFactory()).getExpression(expressionS, context);
        }

	public String getTypeName() {
		return this.expression.getTypeName();
	}
        
        public DNType getDNType() {
           return this.expression.getDNType();
        }

	public String tryAsJava() {
		return expression.asJava();
	}

}
 