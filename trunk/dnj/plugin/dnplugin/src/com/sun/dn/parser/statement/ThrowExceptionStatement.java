
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
import com.sun.dn.parser.*;
import com.sun.dn.*;
import com.sun.dn.util.*;
import com.sun.dn.java.*;

	
    /** @author danny.coward@sun.com */
public class ThrowExceptionStatement extends StatementAdapter {
        private Expression expression;


	protected List tryGetJava() {
		List l = new ArrayList();
                l.add(JavaKeywords.J_THROW + " " + expression.asJava() + ";");
                return l;
	}

	public static boolean isVBThrowExceptionStatement(String code, InterpretationContext context) {
            return code.trim().startsWith(VBKeywords.VB_Throw + " ");
        }
        
        public static boolean isCSThrowExceptionStatement(String code, InterpretationContext context) {
            return code.trim().startsWith(CSKeywords.CS_Throw + " ");
        }
        
        private ThrowExceptionStatement(String code, InterpretationContext context) {
            super(code, context);
        }
	
        public static ThrowExceptionStatement createVBThrowExceptionStatement( String code, InterpretationContext context) {
            String s = code.trim();
            ThrowExceptionStatement tes = new ThrowExceptionStatement(code, context);
            String expressionString = s.substring(VBKeywords.VB_Throw.length(), s.length());
            tes.expression = (new VBExpressionFactory()).getExpression(expressionString, context);
            return tes;
        }
        
        public static ThrowExceptionStatement createCSThrowExceptionStatement( String code, InterpretationContext context) {
            String s = code.trim();
            ThrowExceptionStatement tes = new ThrowExceptionStatement(code, context);
            String expressionString = s.substring(CSKeywords.CS_Throw.length(), s.length());
            tes.expression = (new CSExpressionFactory()).getExpression(expressionString, context);
            return tes;
        }
       
}

 