
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

	

public class AddressOfExpression extends ExpressionAdapter {
        private String procedurename;

	public static boolean isAddressOfExpression(String code, InterpretationContext context) {
		return code.trim().startsWith(VBKeywords.VB_AddressOf + " ");
	}
        
        public Expression getTargetExpression() {
            if (procedurename.indexOf(".") == -1) {
                return new VBMeExpression("<The parser created expression from empty String because of its context in an AddressOf expression>", context);
            } else {
                String targetS = procedurename.substring(0, procedurename.indexOf("."));
                return (new VBExpressionFactory()).getExpression(targetS, context);
            }
        }
        
        public String getMethodName() {
             if (procedurename.indexOf(".") == -1) {
                return procedurename;
            } else {
                String methodS = procedurename.substring(procedurename.indexOf(".") + 1, procedurename.length());
                return methodS;
            }
        }
        
        private AddressOfExpression(String code, InterpretationContext context) {
            super(code, context);
        }

	public static AddressOfExpression createAddressOfExpression(String code, InterpretationContext context) {
                AddressOfExpression aoe = new AddressOfExpression(code.trim(), context);
		aoe.parseVB(code.trim());
                return aoe;
	}
        
        private void parseVB(String code) {
            this.procedurename = code.substring(VBKeywords.VB_AddressOf.length(), code.length()).trim();
        }

	public String getTypeName() {
            throw new RuntimeException("Shouldn't be asking this");
	}
        
        public DNType getDNType() {
            throw new RuntimeException("Shouldn't be asking this");
        }

	public String tryAsJava() {
            throw new RuntimeException("Shouldn't be asking this");
	}

}
 