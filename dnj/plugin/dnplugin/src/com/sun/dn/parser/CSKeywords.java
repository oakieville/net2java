
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

    /** The C# language keywords. */

public class CSKeywords {
    
        public static String CS_Get = "get";
        public static String CS_Set = "set";

	public static String CS_Abstract = "abstract";
	public static String CS_Event = "event";
	public static String CS_New = "new";
	public static String CS_Struct = "struct";
	public static String CS_As = "as";
	public static String CS_Explicit = "explicit";
	public static String CS_Null = "null";
	public static String CS_Switch = "switch";
	public static String CS_Base = "base";
	public static String CS_Extern = "extern";
	public static String CS_Object = "object";
	public static String CS_This = "this";
	public static String CS_Bool = "bool";
	public static String CS_False = "false";
	public static String CS_Operator = "operator";
	public static String CS_Throw = "throw";
	public static String CS_Break = "break";
	public static String CS_Finally = "finally";
	public static String CS_Out = "out";
	public static String CS_True = "true";
	public static String CS_Byte = "byte";
	public static String CS_Fixed = "fixed";
	public static String CS_Override = "override";
	public static String CS_Try = "try";
	public static String CS_Case = "case";
	public static String CS_Float = "float";
	public static String CS_Params = "params";
	public static String CS_Typeof = "typeof";
	public static String CS_Catch = "catch";
	public static String CS_For = "for";
	public static String CS_Private = "private";
        public static String CS_Partial = "partial";
	public static String CS_Uint = "uint";
	public static String CS_Char = "char";
	public static String CS_Foreach = "foreach";
	public static String CS_Protected = "protected";
	public static String CS_Ulong = "ulong";
	public static String CS_Checked = "checked";
	public static String CS_Goto = "goto";
	public static String CS_Public = "public";
	public static String CS_Unchecked = "unchecked";
	public static String CS_Class = "class";
	public static String CS_If = "if";
	public static String CS_Readonly = "readonly";
	public static String CS_Unsafe = "unsafe";
	public static String CS_Const = "const";
	public static String CS_Implicit = "implicit";
	public static String CS_Ref = "ref";
	public static String CS_Ushort = "ushort";
	public static String CS_Continue = "continue";
	public static String CS_In = "in";
	public static String CS_Return = "return";
	public static String CS_Using = "using";
	public static String CS_Decimal = "decimal";
	public static String CS_Int = "int";
	public static String CS_Sbyte = "sbyte";
	public static String CS_Virtual = "virtual";
	public static String CS_Default = "default";
	public static String CS_Interface = "interface";
	public static String CS_Sealed = "sealed";
	public static String CS_Volatile = "volatile";
	public static String CS_Delegate = "delegate";
	public static String CS_Internal = "internal";
	public static String CS_Short = "short";
	public static String CS_Void = "void";
	public static String CS_Do = "do";
	public static String CS_Is = "is";
	public static String CS_Sizeof = "sizeof";
	public static String CS_While = "while";
	public static String CS_Double = "double";
	public static String CS_Lock = "lock";
	public static String CS_Stackalloc = "stackalloc";
	public static String CS_Else = "else";
	public static String CS_Long = "long";
	public static String CS_Static = "static";
	public static String CS_Enum = "enum";
	public static String CS_Namespace = "namespace";
	public static String CS_String = "string";	


	public static List getMemberModifiers() {
		List l = new ArrayList();
		l.add(CS_Public);
		l.add(CS_Protected);
		l.add(CS_Internal);
		l.add(CS_Private);
                l.add(CS_Static);
                l.add(CS_Sealed);
		return l;
	}

	public static List getJumpKeywords() {
		List l = new ArrayList();
		l.add(CS_Break);
		l.add(CS_Continue);
		l.add(CS_Return);
		l.add(CS_Goto);
		l.add(CS_Default);
		return l;
	}	   
}

 