
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

	/** The NET expression for true and false. Defined as <br>
	BooleanLiteral ::= True | False in VB
	or
	BooleanLiteral ::= true | false in C#
	@author danny.coward@sun.com
	**/


public class BooleanLiteral extends Literal implements BooleanExpression {
	private boolean b;
	private DNType c;

	public static BooleanLiteral createVBBooleanLiteral(String s,  InterpretationContext context) {
		BooleanLiteral bl = new BooleanLiteral(s, context);
		if (VBKeywords.VB_True.equals(s)) {
			bl.b = true;
		} else if (VBKeywords.VB_False.equals(s)) {
			bl.b = false;
		} else {
			throw new RuntimeException("Error creating Boolean Literal from: " + s);
		}
		return bl;
	}

	public static BooleanLiteral createCSBooleanLiteral(String s,  InterpretationContext context) {
		BooleanLiteral bl = new BooleanLiteral(s, context);
		if (CSKeywords.CS_True.equals(s)) {
			bl.b = true;
		} else if (CSKeywords.CS_False.equals(s)) {
			bl.b = false;
		} else {
			throw new RuntimeException("Error creating Boolean Literal from: " + s);
		}
		return bl;
	}


	private BooleanLiteral (String code, InterpretationContext context) {
            super(code, context);
	}

	public String toString() {
		return "Boolean(" + b + ")";
	}

	public String getTypeName() {
		return "Boolean";
	}

	public String tryAsJava() {
		return "" + b;
	}

	public DNType getDNType() {
            if (this.c == null) {
		this.c = this.context.getLibrary().getLibraryData().getLibraryClass(this.getTypeName());
            }
           return c;
	}

}

 