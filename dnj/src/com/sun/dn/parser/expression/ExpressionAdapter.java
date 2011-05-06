
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
import com.sun.dn.util.*;

	/** Abstract type for expressions.
	** @author danny.coward@sun.com
	*/

public abstract class ExpressionAdapter implements Expression {
	private String code = null;
        InterpretationContext context;
        
        public ExpressionAdapter(String code, InterpretationContext context) {
            this.code = code;
            this.context = context;
        }
       
        public static Expression handleExpressionParseErrorReport(Throwable rte,
                                                String code, 
                                                InterpretationContext context) {
            
            ParseTree pt = ParseTree.getParseTree(context);
            if ( pt.getPolicy().isOfType(TranslationPolicy.GENTLE) ) {
                Expression e = UntranslatedExpression.createUntranslatedExpression(code, context);
                TypeResolveException tre = new TypeResolveException(code, rte.getMessage());
                pt.getTranslationReport().addTypeResolveException(tre);
                return e;
             } else {
                 rte.printStackTrace();
                 throw new TypeResolveException(code, rte.getMessage());
             }
        }

	public String getOriginalCode() {
		if (this.code == null) {
			throw new RuntimeException("code variable has not been used in " + this.getClass());
		} else {
			return this.code;
		}
	}

	public String asJava() {
            try {
                return this.tryAsJava();
            } catch (Throwable t) {
                ParseTree pt = ParseTree.getParseTree(this.context);
                pt.handleTypeResolveException(pt, this.code, t, false);
                return UntranslatedExpression.createUntranslatedExpression(this.code, context).asJava();
            }
        }
        
        public abstract String tryAsJava();

	public abstract String getTypeName();

	public abstract DNType getDNType();
}

 