
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

	

public class SizeOfExpression extends ExpressionAdapter {
        private Expression expression;
    
        protected SizeOfExpression(String code, InterpretationContext context) {
            super(code, context);
        }
        
        public static boolean isSizeOfExpression(String code, InterpretationContext context) {
            return code.startsWith(CSKeywords.CS_Sizeof + "(");
        }
        
        public static SizeOfExpression createSizeOfExpression(String code, InterpretationContext context) {
            SizeOfExpression soe = new SizeOfExpression(code, context);
            soe.parse(code.trim(), context);
            soe.context = context;
            return soe;
        }
        
        private void parse(String code, InterpretationContext context) {
            String expS = code.substring(code.indexOf("(") + 1, code.lastIndexOf(")"));
            this.expression = (new CSExpressionFactory()).getExpression(expS, context);
        }

	public String getTypeName() {
		return "System.Integer";
	}

	public DNType getDNType() {
		return this.context.getLibrary().getProgramDefinedOrLibraryDNTypeFor(this.getTypeName());
	}
        
        public String tryAsJava() {
            return "[untranslatable: sizeof( " + expression.asJava() + " )]";
        }


}
 