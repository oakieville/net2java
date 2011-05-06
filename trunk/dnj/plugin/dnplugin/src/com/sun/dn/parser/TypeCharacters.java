
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
package com.sun.dn.parser;

import java.util.*;
	
	/** The .NET type characters. 
	@author danny.coward@sun.com
	*/

public class TypeCharacters {

	public static String LONG = "&";
	public static String DECIMAL = "@";
	public static String INTEGER = "%";
	public static String SINGLE = "!";
	public static String DOUBLE = "#"; 	
	public static String STRING = "$";	
	
	public static List getTypeCharacters() {
		List l = new ArrayList();
		l.add(INTEGER);
		l.add(LONG);
		l.add(DECIMAL);
		l.add(SINGLE);
		l.add(DOUBLE);
		l.add(STRING);
		return l;
	}

	public static String getType(String ch) {
		if (LONG.equals(ch)) {
			return "Long";
		} else if (DECIMAL.equals(ch)) {
			return "Decimal";
		} else if (INTEGER.equals(ch)) {
			return "Integer";
		} else if (SINGLE.equals(ch)) {
			return "Single";
		} else if (DOUBLE.equals(ch)) {
			return "Double";
		} else if (STRING.equals(ch)) {
			return "String";
		} else {
			return null;
		}
	}

	public static boolean usesTypeCharacter(String code) {
		if (code.length() < 2) {
			return false;
		} else {
			String lastChar = code.substring(code.length()-1, code.length());
			return containsChar(lastChar);
		}
	}

	public static String removeTypeCharacter(String code) {
		if (usesTypeCharacter(code)) {
			return code.substring(0, code.length()-1);
		} else {
			return code;
		}
	}

	public static String getTypeCharacter(String code) {
		if (usesTypeCharacter(code)) {
			return code.substring(code.length()-1, code.length());
		} else {
			return null;
		}
	}

	private static boolean containsChar(String s) {
		for (Iterator itr = getTypeCharacters().iterator(); itr.hasNext();) {
			String next = (String) itr.next();
			if (s.equals(next)) {
				return true;
			}
		}
		return false;
	}

}
 