
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
import com.sun.dn.parser.expression.*;
import com.sun.dn.parser.*;
import com.sun.dn.util.*;


	/**
		A C# class statement. Classes in C# are declared using the keyword class. It takes the following form::
		[attributes] [modifiers] class identifier [:base-list] { class-body }[;]
		where: 
		attributes (Optional) 
		Additional declarative information. For more information on attributes and attribute classes, see 17. Attributes. 
		modifiers (Optional) 
		The allowed modifiers are new, abstract, sealed, and the four access modifiers. 
		identifier 
		The class name. 
		base-list (Optional) 
		A list that contains the one base class and any implemented interfaces, all separated by commas. 
		class-body 
		Declarations of the class members. 
         * @author danny.coward@sun.com
	*/


public class CSClassStatement extends CSMetaClass { 

	public CSClassStatement(String code, InterpretationContext context) {
		super(code, context);

	}
        
        protected String myKeyword() {
            return CSKeywords.CS_Class;
        }
        
        protected void registerType() {
            DNType type = this.getDNType();
        }


	public static boolean isCSClassStatement(String code, InterpretationContext context) {
            if (code.indexOf("{") != -1) {
		String potentialDeclaration = Util.getUpToOpeningCurlyBracket(code.trim());
		List l = getDeclarationAsTokens(potentialDeclaration);

                return l.contains(CSKeywords.CS_Class);
            }
            return false;
	}
        
        public String toString() {
            return "a CS Class (" + super.getFQName() + ")"; 
        
        }


}
 