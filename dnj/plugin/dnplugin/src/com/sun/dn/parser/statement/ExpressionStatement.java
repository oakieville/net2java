
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
import com.sun.dn.parser.expression.*;
import com.sun.dn.util.*;
import com.sun.dn.java.*;
import com.sun.dn.*;

/** @author danny.coward@sun.com */	

public class ExpressionStatement extends StatementAdapter {
	private Expression expression;
        
        private ExpressionStatement(String code, InterpretationContext context) {
            super(code, context);
        }
	
	public static boolean isVBExpressionStatement(String code, InterpretationContext context) {
		Expression e = (new VBExpressionFactory()).getExpression(code, context);
                return e != null;
	}
        
        public static boolean isCSExpressionStatement(String code, InterpretationContext context) {
               Expression e = (new CSExpressionFactory()).getExpression(code, context);
               return e != null;      
	}
        
        public static ExpressionStatement createVBExpressionStatement(String code, InterpretationContext context ) {
            Expression e = (new VBExpressionFactory()).getExpression(code, context);
            ExpressionStatement es = new ExpressionStatement(code, context);
            es.expression = e;
            return es;
        }
        
        public static ExpressionStatement createCSExpressionStatement(String code, InterpretationContext context ) {
            Expression e = (new CSExpressionFactory()).getExpression(code, context);
            ExpressionStatement es = new ExpressionStatement(code, context);
            es.expression = e;
            return es;
        }
        
        public String toString() {
            return "Expression Statement: exp[" + this.expression + "]";
        }


	
	protected List tryGetJava() {
		List strings = new ArrayList();
		strings.add(this.expression.asJava() + ";");
		return strings;
	}

	
	
}

 