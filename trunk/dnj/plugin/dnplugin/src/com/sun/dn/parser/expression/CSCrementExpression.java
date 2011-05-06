
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

	/**
	** @author danny.coward@sun.com
	*/

public class CSCrementExpression extends SimpleExpression  {
	String variableName;
        String operator;
       
        protected CSCrementExpression(String code, InterpretationContext context) {
            super(code, context);
        }

        
        public static boolean isCSCrementExpression(String code, InterpretationContext context) {
		return code.trim().startsWith("++") ||
                        code.trim().endsWith("++") ||
                        code.trim().startsWith("--") ||
                        code.trim().endsWith("--");
	}
       
        
        public static CSCrementExpression createCSCrementExpression(String code, InterpretationContext context) {
               String cc = code.trim();
               String variableName = cc.substring(0, cc.length()-2);
               String operator = cc.substring(cc.length()-2, cc.length());
               CSCrementExpression cce = new CSCrementExpression(code, context);
               cce.variableName = variableName;
               cce.operator = operator;
               return cce;
	}

	public String getTypeName() {
		return "Integer";
	}
        
        public DNType getDNType() {
            return this.context.getLibrary().getProgramDefinedOrLibraryDNTypeFor(this.getTypeName());
        }

	public String tryAsJava() {
		return variableName + operator;
	}

}
 