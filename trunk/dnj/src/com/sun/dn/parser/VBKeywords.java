
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
	
	/** The VB language keywords. 
	@author danny.coward@sun.com
	*/

public class VBKeywords {

	public static String VB_Comment = "'";
	public static String VB_AddHandler = "AddHandler";
	public static String VB_AddressOf = "AddressOf";
	public static String VB_Alias = "Alias";
	public static String VB_And = "And";
	public static String VB_AndAlso = "AndAlso"; 	
	public static String VB_Ansi = "Ansi";	
	public static String VB_As	 = "As";
	public static String VB_Assembly = "Assembly";	  
	public static String VB_Auto = "Auto";	
	public static String VB_Boolean = "Boolean";	
	public static String VB_ByRef = "ByRef";	
	public static String VB_Byte = "Byte";	   
	public static String VB_ByVal = "ByVal";	
	public static String VB_Call = "Call";	
	public static String VB_Case = "Case";	
	public static String VB_Catch = "Catch";	   
	public static String VB_CBool = "CBool";	
	public static String VB_CByte = "CByte";	
	public static String VB_CChar = "CChar";	
	public static String VB_CDate = "CDate";	   
	public static String VB_CDec = "CDec";	
	public static String VB_CDbl = "CDbl";	
	public static String VB_Char = "Char";	
	public static String VB_CInt = "CInt";	   
	public static String VB_Class = "Class";	
	public static String VB_CLng = "CLng";	
	public static String VB_CObj = "CObj";	
	public static String VB_Const = "Const";
        public static String VB_Continue = "Continue";
	public static String VB_CShort = "CShort";	
	public static String VB_CSng = "CSng";	
	public static String VB_CStr = "CStr";	
	public static String VB_CType = "CType";
	public static String VB_Date = "Date";	
	public static String VB_Decimal = "Decimal";	
	public static String VB_Declare = "Declare";	
	public static String VB_Default = "Default";	   
	public static String VB_Delegate = "Delegate";	
	public static String VB_Dim = "Dim";	
	public static String VB_DirectCast = "DirectCast";	
	public static String VB_Do = "Do";	   
	public static String VB_Double = "Double";	
	public static String VB_Each = "Each";	
	public static String VB_Else = "Else";	
	public static String VB_ElseIf = "ElseIf";	   
	public static String VB_End = "End";	
	public static String VB_Enum = "Enum";	
	public static String VB_Erase = "Erase";	
	public static String VB_Error = "Error";	   
	public static String VB_Event = "Event";	
	public static String VB_Exit = "Exit";	
	public static String VB_False = "False";	
	public static String VB_Finally = "Finally";	   
	public static String VB_For = "For";	
	public static String VB_Friend = "Friend";	
	public static String VB_Function = "Function";	
	public static String VB_Get = "Get";	   
	public static String VB_GetType = "GetType";	
	public static String VB_GoSub = "GoSub";	
	public static String VB_GoTo = "GoTo";	
	public static String VB_Handles = "Handles";	   
	public static String VB_If = "If";	
	public static String VB_Implements = "Implements";	
	public static String VB_Imports = "Imports";	
	public static String VB_In = "In";	   
	public static String VB_Inherits = "Inherits";	
	public static String VB_Integer = "Integer";	
	public static String VB_Interface = "Interface";	
	public static String VB_Is	 = "Is"; 
        public static String VB_IsNot	 = "IsNot";
	public static String VB_Let = "Let";	
	public static String VB_Lib = "Lib";	
	public static String VB_Like = "Like";	
	public static String VB_Long = "Long";	   
	public static String VB_Loop = "Loop";	
	public static String VB_Me	 = "Me";
	public static String VB_Mod = "Mod";	
	public static String VB_Module = "Module";	   
	public static String VB_MustInherit = "MustInherit";	
	public static String VB_MustOverride = "MustOverride";	
	public static String VB_MyBase = "MyBase";	
	public static String VB_MyClass = "MyClass";	   
	public static String VB_Namespace = "Namespace";	
	public static String VB_New = "New";	
	public static String VB_Next = "Next";	
	public static String VB_Not = "Not";	   
	public static String VB_Nothing = "Nothing";	
	public static String VB_NotInheritable = "NotInheritable";	
	public static String VB_NotOverridable = "NotOverridable";	
	public static String VB_Object = "Object";
        public static String VB_Of = "Of";
	public static String VB_On = "On";
        public static String VB_Operator = "Operator";
	public static String VB_Option = "Option";	
	public static String VB_Optional = "Optional";	
	public static String VB_Or = "Or";	   
	public static String VB_OrElse = "OrElse";	
	public static String VB_Overloads = "Overloads";	
	public static String VB_Overridable = "Overridable";	
	public static String VB_Overrides = "Overrides";	   
	public static String VB_ParamArray = "ParamArray";
        public static String VB_Partial = "Partial";
	public static String VB_Preserve = "Preserve";	
	public static String VB_Private = "Private";	
	public static String VB_Property = "Property";	   
	public static String VB_Protected = "Protected";	
	public static String VB_Public = "Public";	
	public static String VB_RaiseEvent = "RaiseEvent";	
	public static String VB_ReadOnly	= "ReadOnly"; 
	public static String VB_ReDim = "ReDim";	
	public static String VB_REM = "REM";	
	public static String VB_RemoveHandler = "RemoveHandler";	
	public static String VB_Resume = "Resume";	   
	public static String VB_Return = "Return";	
	public static String VB_Select = "Select";	
	public static String VB_Set = "Set";	
	public static String VB_Shadows = "Shadows";	   
	public static String VB_Shared = "Shared";	
	public static String VB_Short = "Short";	
	public static String VB_Single = "Single";	
	public static String VB_Static = "Static"; 
	public static String VB_Step = "Step";	
	public static String VB_Stop = "Stop";	
	public static String VB_String = "String";	
	public static String VB_Structure = "Structure";	   
	public static String VB_Sub = "Sub";	
	public static String VB_SyncLock = "SyncLock";	
	public static String VB_Then = "Then";	
	public static String VB_Throw = "Throw";	   
	public static String VB_To = "To";	
	public static String VB_True = "True";	
	public static String VB_Try = "Try";	
	public static String VB_TypeOf = "TypeOf";	   
	public static String VB_Unicode = "Unicode";	
	public static String VB_Until = "Until";
        public static String VB_Using = "Using";
	public static String VB_Variant = "Variant";	
	public static String VB_When = "When";	   
	public static String VB_While = "While";	
	public static String VB_With = "With";	
	public static String VB_WithEvents = "WithEvents";	
	public static String VB_WriteOnly = "WriteOnly";	   
	public static String VB_Xor = "Xor";

	//It says the following ones are only used by the compiler for mysterious things.	
	//public static String #Const	
	//public static String #ExternalSource	
	//public static String #If...Then...#Else	   
	public static String VB_Region = "Region";
	public static String VB_Shrp = "#";

	// better put these in too 	
	//-	&	&=	   
	//*	*=	/	/=	   
	//\	\=	^	^=	   
	//+	+=	=	-=	 
	//Note GoSub, Let, and Variant are retained as keywords, although they are no longer used in Visual Basic .NET.

	public static List getAccessKeywords() {
		List l = new ArrayList();
		l.add(VBKeywords.VB_Public);
		l.add(VBKeywords.VB_Protected);
		l.add(VBKeywords.VB_Friend);
		l.add(VBKeywords.VB_Private);
		return l;
	}

	public static List getSharedKeywords() {
		List l = new ArrayList();
		l.add(VBKeywords.VB_Shared);
		return l;
	}
        
        public static List getReadWriteKeywords() {
            List l = new ArrayList();
		l.add(VBKeywords.VB_ReadOnly);
                l.add(VBKeywords.VB_WriteOnly);
		return l;
        }
        
        public static List getOverLoadsOverRidesKeywords() {
            List l = new ArrayList();
		l.add(VBKeywords.VB_Overloads);
                l.add(VBKeywords.VB_Overrides);
		return l;
        }

	public static List getShadowKeywords() {
		List l = new ArrayList();
		l.add(VBKeywords.VB_Overloads);
		l.add(VBKeywords.VB_Overrides);
		l.add(VBKeywords.VB_Overridable);
		l.add(VBKeywords.VB_NotOverridable);
		l.add(VBKeywords.VB_MustOverride);
		l.add(VBKeywords.VB_Shadows);
		return l;
	}

	public static List getInheritanceKeywords() {
		List l = new ArrayList();
		l.add(VBKeywords.VB_MustInherit);
		l.add(VBKeywords.VB_NotInheritable);
		return l;
	}

	//VariableModifier := AccessModifiers | Shadows | Shared | ReadOnly | WithEvents
	public static List getVariableModifierKeywords() {
		List l = new ArrayList();
		l.addAll(getAccessKeywords());
		l.add(VB_Shadows);
		l.add(VB_Shared);
		l.add(VB_ReadOnly);
		l.add(VB_WithEvents);
		return l;
	}

}

 