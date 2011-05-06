
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

public class CSIsExpression extends SimpleExpression implements BooleanExpression {
	private Expression left;
	private Expression right;

        CSIsExpression(String code, InterpretationContext context) {
            super(code, context);
        }
        
        public static boolean isCSIsExpression(String code, InterpretationContext context) {
		List l = Util.tokenizeIgnoringEnclosers(code, " ");
		if (l.size() > 2) {
			String middle = (String) l.get(1);
			return middle.equals(CSKeywords.CS_Is);
		}
		return false;
	}
        
        
        public static CSIsExpression createCSIsExpression(String code, InterpretationContext context) {
                CSIsExpression is = new CSIsExpression(code, context);
		is.parseCS(code, context);
                return is;
	}
        
        private void parseCS(String code, InterpretationContext context) {
            if (!isCSIsExpression(code, context)) {
			throw new RuntimeException("Illegal");
		}
                this.context = context;
		List l = Util.tokenizeIgnoringEnclosers(code, " ");
		this.left = (new CSExpressionFactory()).getExpression((String) l.get(0), context);
		this.right = (new CSExpressionFactory()).getExpression((String) l.get(2), context);
        }

	public String getTypeName() {
		return "Boolean";
	}
        
        public DNType getDNType() {
            return this.context.getLibrary().getProgramDefinedOrLibraryDNTypeFor(this.getTypeName());
        }

	public String tryAsJava() {
		return left.asJava() + " " + JavaKeywords.J_INSTANCEOF + " " + right.asJava();
	}

}
 