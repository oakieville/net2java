
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

	/** A C# using statement denoting a namespace that this program will use.
         * <br> using [alias = ]class_or_namespace;
		where: 
		alias (optional) 
		A user-defined symbol that you want to represent a namespace. You will then be able to use alias to represent the namespace name. 
		class_or_namespace 
		The namespace name that you want to either use or alias, or the class name that you want to alias. 
	
		@author danny.coward@sun.com
	**/

public class CSUsingDirective extends ImportsStatement {

	public static boolean isUsingDirective(String code) {
		StringTokenizer st = new StringTokenizer(code);
		return st.nextToken().equals(CSKeywords.CS_Using);
	}

	public CSUsingDirective(String code, InterpretationContext context) {
            super(code, context);
		this.parse(code);
	}
	
	private void parse(String code) {
		String toParse = code.trim();
		Debug.logn("Parse " + toParse, this);
		List words = Util.tokenizeIgnoringEnclosers(toParse, " ");
		words = Util.trimStrings(words);
		if (words.get(0).equals(CSKeywords.CS_Using)) {
			String s = (String) words.get(1);
			super.namespace = s;
			Debug.logn("Created " + this, this);
		} else {
			throw new RuntimeException("Error parsing Using directive statement " + code);
		}
	}

	public String toString() {
		return "aUsingDir(" + super.namespace + ")"; 
	}

}
 