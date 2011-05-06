
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

	/** Statement of a C# namespace. The namespace keyword is used to declare a scope. 
	This namespace scope lets you organize code and gives you a way to create globally-unique types.
	namespace name[.name1] ...] {
   		type-declarations
	}
	where: 
	name, name1 
	A namespace name can be any legal identifier. A namespace name can contain periods. 
	type-declarations 
	Within a namespace, you can declare one or more of the following types: 
	another namespace 
	class 
	interface 
	struct 
	enum 
	delegate 
		@author danny.coward@sun.com
	**/


public class CSNamespaceStatement extends NamespaceStatement {
	
	public CSNamespaceStatement(String code, InterpretationContext context, CSParser parser) {
		super(code, context);
		Debug.logn("Parse " + code, this);
		
		String declaration = code.substring(0, code.indexOf("{"));
		String rest = code.substring(code.indexOf("{") + 1, code.length()).trim();
		
		StringTokenizer st = new StringTokenizer(declaration);
		st.nextToken(); // this was the namespace keyword
		super.shortName = st.nextToken();
		

		
		List l = parser.getTopLevelStatements(rest, new ArrayList(), this);

		super.topLevelStatementObjects = l;

	}

	public static boolean isNamespaceStatement(String statement) {
		return statement.trim().startsWith(CSKeywords.CS_Namespace);
	}

}

 