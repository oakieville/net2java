
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
import com.sun.dn.util.*;

	/** A VB expression for determining the type
	** of an expression. Defined as
	** MetaTypeExpression ::= GetType ( TypeName )
         * In C# this is tyhe typeof(expression) syntax
	** @author danny.coward@sun.com
	*/

public class GetTypeExpression extends ExpressionAdapter {
	private Expression expression;


	public static boolean isVBGetTypeExpression(String s, InterpretationContext context) {
		String code = s.trim();
		return code.startsWith(VBKeywords.VB_GetType);
	}
        
        public static boolean isCSGetTypeExpression(String s, InterpretationContext context) {
		String code = s.trim();
		return code.startsWith(CSKeywords.CS_Typeof + "(");
	}
        
        private GetTypeExpression(String code, InterpretationContext context) {
            super(code, context);
        }
        
         public static GetTypeExpression createCSGetTypeExpression(String s, InterpretationContext context) {
            GetTypeExpression gte = new GetTypeExpression(s, context);
            String code = s.trim();
            String expressionString = code.substring(CSKeywords.CS_Typeof.length(), code.length());
            expressionString = Util.stripBrackets(expressionString);
            Debug.clogn(expressionString, GetTypeExpression.class);
            gte.expression = (new CSExpressionFactory()).getExpression(expressionString, context);
            return gte;
        }
        
        public static GetTypeExpression createVBGetTypeExpression(String s, InterpretationContext context) {
            GetTypeExpression gte = new GetTypeExpression(s, context);
            String code = s.trim();
            String expressionString = code.substring(VBKeywords.VB_GetType.length() + 1, code.length() -1);
            Debug.clogn(expressionString, GetTypeExpression.class);
            gte.expression = (new VBExpressionFactory()).getExpression(expressionString, context);
            return gte;
        }

	public String getTypeName() {
		return "System.Type";
	}

	public DNType getDNType() {
		return context.getLibrary().getLibraryData().getLibraryClass(this.getTypeName());
	}

	public String tryAsJava() {
                String jExp = expression.asJava();
		return jExp + ".class";
	}

	public Expression getExpression() {
		return this.expression;
	}


}

 