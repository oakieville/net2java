
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
import com.sun.dn.*;
import com.sun.dn.parser.*;
import com.sun.dn.util.*;

	/** A VB statement importing a library of API classes. Defined as <br><br>
	ImportsDirective ::= Imports ImportsClauses LineTerminator <br>
	ImportsClauses ::= <br>
	ImportsClause | <br>
	ImportsClauses , ImportsClause <br>
	ImportsClause ::= ImportsAliasClause | RegularImportsClause <br> <br>

	Imports [ aliasname = ] namespace <br>
	-or- <br>
	Imports [ aliasname = ] namespace.element <br>

	alias name is a string used to give a long name a more convenient one <br>
	namespace is a string of things separated by dots <br>
	element is a class, enum, Module or Interface name within that namespace. <br>
	@author danny.coward@sun.com
	**/

public class VBImportsStatement extends ImportsStatement {

	public static boolean isImportsStatement(String code) {
		StringTokenizer st = new StringTokenizer(code);
		return st.nextToken().equals(VBKeywords.VB_Imports);
	}

	public VBImportsStatement(String code, InterpretationContext context) {
            super(code, context);
            this.parse(code);
	}
	
	private void parse(String code) {
		List words = Util.tokenizeIgnoringEnclosers(code, " ");
		if (words.get(0).equals(VBKeywords.VB_Imports)) {
			String s = (String) words.get(1);
			this.namespace = s;
		} else {
			throw new RuntimeException("Error parsing Imports statement " + code);
		}
	}

}
 