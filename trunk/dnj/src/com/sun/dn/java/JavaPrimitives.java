
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
package com.sun.dn.java;

import java.util.*;

	/** A class containing class-primitive conversions
	** for Java primitive types.
	@author danny.coward@sun.com
	*/

public class JavaPrimitives {
	public static String J_BYTE = "byte";
	public static String J_SHORT = "short";
	public static String J_INT = "int";
	public static String J_LONG = "long";
	public static String J_FLOAT = "float";
	public static String J_DOUBLE = "double";
	public static String J_CHAR = "char";
	public static String J_BOOLEAN = "boolean";
	
	private static List all;


	public static String primitiveToClass(String javaExpression, String type) {
		if (type.equals(J_INT)) {
			return "(new Integer(" + javaExpression + "))";
		} else if (type.equals(J_BOOLEAN)) {
			return "(new Boolean(" + javaExpression + "))";
		} else if (type.equals(J_DOUBLE)) {
			return "(new Double(" + javaExpression + "))";
		} else if (type.equals(J_CHAR)) {
                    return "(new Character(" + javaExpression + "))";
                } else if (type.equals(J_SHORT)) {
                    return "(new Short(" + javaExpression + "))";
                } else if (type.equals(J_BYTE)) {
                    return "(new Byte(" + javaExpression + "))";
                } else if (type.equals(J_LONG)) {
                    return "(new Long(" + javaExpression + "))";
                } else if (type.equals(J_FLOAT)) {
                    return "(new Float(" + javaExpression + "))";
                }
		return javaExpression;	
	}

	public static String getJavaClassTypeForPrimitive(String javaPrimitive) {
		if (javaPrimitive.equals(J_INT)) {
			return "Integer";
                } else if (javaPrimitive.equals(J_DOUBLE)) {
                    return "Double";
		} else if (javaPrimitive.equals(J_BOOLEAN)) {
			return "Boolean";
                } else if (javaPrimitive.equals(J_BYTE)) {
                        return "Byte";
                } else if (javaPrimitive.equals(J_SHORT)) {
                    return "Short";
                } else if (javaPrimitive.equals(J_LONG)) {
                    return "Long";
                } else if (javaPrimitive.equals(J_FLOAT)) {
                    return "Float";
                } else if (javaPrimitive.equals(J_CHAR)) {
                    return "Character";
		} else {
			throw new RuntimeException("Haven't implemented this for " + javaPrimitive);
		}
	}

	public static boolean isPrimitive(String jType) {
		if (all == null) {
			all = new ArrayList();
			all.add(J_BYTE);
			all.add(J_SHORT);
			all.add(J_INT);
			all.add(J_LONG);
			all.add(J_FLOAT);
			all.add(J_DOUBLE);
			all.add(J_CHAR);
			all.add(J_BOOLEAN);
		}
		return all.contains(jType);
	}

	public static String getDefaultJavaFor(String jType) {
		if (jType.equals(J_BYTE)) {
			return "-1";
		} else if (jType.equals(J_SHORT)) {
			return "-1";
		} else if (jType.equals(J_INT)) {
			return "-1";
		} else if (jType.equals(J_LONG)) {
			return "-1";
		} else if (jType.equals(J_FLOAT)) {
			return "-1";
		} else if (jType.equals(J_DOUBLE)) {
			return "-1";
		} else if (jType.equals(J_CHAR)) {
			return "(char) -1";
		} else if (jType.equals(J_BOOLEAN)) {
			return "false";
		} else {
			return "null";
		}

	}


}
 