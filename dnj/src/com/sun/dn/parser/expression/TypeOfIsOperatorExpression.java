
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
import java.util.*;
import com.sun.dn.util.*;
import com.sun.dn.library.LibraryData;
import com.sun.dn.java.*;

	/** The TypeOf keyword introduces a comparison clause that tests 
	** whether an object is derived from or implements a particular type, such as an interface.
         ** TypeOfIsOperatorExpression ::= TypeOf Expression Is TypeName 
	** @author danny.coward@sun.com
	*/

public class TypeOfIsOperatorExpression extends SimpleExpression {
        private Expression expression;
        private DNType dnType;
        
        private TypeOfIsOperatorExpression(String code, InterpretationContext context) {
            super(code, context);
        }
    
        public static boolean isTypeOfIsOperatorExpression(String code, InterpretationContext context) {
            return code.trim().startsWith(VBKeywords.VB_TypeOf + " ");
        }
        
        public static TypeOfIsOperatorExpression createTypeOfIsOperatorExpression(String code, InterpretationContext context) {
            TypeOfIsOperatorExpression toie = new TypeOfIsOperatorExpression(code, context);
            List tokens = Util.tokenizeIgnoringEnclosers(code.trim(), " ");
            String expS = (String) tokens.get(1);
            String typeS = (String) tokens.get(3);
            toie.expression = (new VBExpressionFactory()).getExpression(expS, context);
            toie.dnType = context.getLibrary().getProgramDefinedOrLibraryDNTypeFor(typeS);
            return toie;
        }
        
        public String tryAsJava() {
                String expJ = expression.asJava();
                String typeJ = this.context.getLibrary().getJavaTypeFor(this.getTypeName());
                if (JavaPrimitives.isPrimitive(typeJ)) {
                    typeJ = JavaPrimitives.getJavaClassTypeForPrimitive(typeJ);
                }
		return expJ + " " + JavaKeywords.J_INSTANCEOF + " " + typeJ;
	}

	public String getTypeName() {
		return "System.Boolean";
	}

	public  DNType getDNType() {
            return this.context.getLibrary().getProgramDefinedOrLibraryDNTypeFor(this.getTypeName());
        }

}
 