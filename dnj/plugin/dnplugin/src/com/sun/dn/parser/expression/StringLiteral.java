
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
	
import com.sun.dn.*;
import com.sun.dn.parser.*;
import com.sun.dn.util.*;

	/** A .NET expression that is a string value. Defined as <br>
	StringLiteral ::= " [ StringCharacter+ ] " <br>
	StringCharacter ::= < Character except for " > | "" <br>
	@author danny.coward@sun.com
	**/

public class StringLiteral extends Literal {
	private String s;
	private DNType c;

	public StringLiteral(String s, InterpretationContext context) {
            super(s, context);
            this.s = s;
	}

	public static boolean isStringLiteral(String code) {
            Debug.clogn("Is string literal " + code, StringLiteral.class);
            if (code.indexOf('"') == 0 && code.lastIndexOf('"') == code.length() -1) {
            	boolean inQuote = false;
                for (int i = 0; i < code.length(); i++) {
                    char c = code.charAt(i);
                    if (c == '"') { 				      
                    	if (inQuote && i != (code.length()-1) && i > 0 && code.charAt(i+1) != '"' && !("" + code.charAt(i-1)).equals("\\") ) {
                            Debug.clogn("no  - quote ended too early", StringLiteral.class);
                            return false;
			}
			inQuote = !inQuote;
                    }
                 }
                Debug.clogn("yes", StringLiteral.class);

                return true;
            } else {	
		Debug.clogn("no - either didn't start or end with quote", StringLiteral.class);
		return false;
            }
	}

	public String toString() {
		return "StrLiteral(" + s + ")";
	}

	public String tryAsJava() {
		if (s.equals("" + '"' + '"')) {
			return "\"" + "\"";
		}
		return "\"" + s + "\"";
	}	
	
	public String getTypeName() {
		return "String";
	}

	public DNType getDNType() {
		if (this.c == null) {
			this.c = context.getLibrary().getLibraryData().getLibraryClass(this.getTypeName());
		}
		return c;
	}

}

 