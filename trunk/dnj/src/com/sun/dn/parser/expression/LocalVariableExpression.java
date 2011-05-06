
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
import java.util.*;
import com.sun.dn.*;
import com.sun.dn.util.*;
	
	/**
	A local variable expression consists of the name of a local variable. 
	The value of a local variable is the value stored in the local. <br>
	LocalVariableExpression ::= LocalVariableName
	@author danny.coward@sun.com
	*/

public class LocalVariableExpression extends SimpleExpression implements BooleanExpression {
	private DNVariable variable;
	private DNType vbClass;
        String typeName;
        
        private LocalVariableExpression(String code, InterpretationContext context) {
            super(code, context);
        }

	public static LocalVariableExpression createVBLocalVariableExpression(DNVariable variable, InterpretationContext context) {
            LocalVariableExpression lve = new LocalVariableExpression("<parser lost the original code for this local variable expression>", context);
            lve.variable = variable;
            lve.typeName = lve.variable.getVBFullType();
            return lve;
	}
        
        public static LocalVariableExpression createCSLocalVariableExpression(DNVariable variable, InterpretationContext context) {
            LocalVariableExpression lve = new LocalVariableExpression("<created by parser>", context);
            lve.variable = variable;
            lve.typeName = lve.variable.getVBFullType();
            return lve;
	}

	public static boolean isVBLocalVariable(String name, InterpretationContext context) {
            Debug.clogn("Is Local variable ." + name + ".", LocalVariableExpression.class);
            boolean isLV = false;
            if (name.equals(VBKeywords.VB_MyBase)) {
                isLV = false;
            } else {
                isLV = getVariable(name, context) != null;
            }
            Debug.clogn("IsLocalVariable: " + isLV, LocalVariableExpression.class);
            return isLV;
	}

	public static boolean isCSLocalVariable(String name, InterpretationContext context) {
		boolean isIt = getVariable(name, context) != null;
		return isIt;
	}

	private static DNVariable getVariable(String name, InterpretationContext context) {
                Debug.clogn("Get var of name " + name + " in context "  + " " + context.getClass() +  " " + context.getVariables(), LocalVariableExpression.class );
                return context.getVariable(name);
               
	}

	public static LocalVariableExpression createVBLocalVariableExpression(String name, InterpretationContext context) {
            LocalVariableExpression lve = new LocalVariableExpression(name, context);
            lve.variable = getVariable(name, context);
            lve.typeName = lve.variable.getVBFullType();
            return lve;
        }
        
        public static LocalVariableExpression createCSLocalVariableExpression(String name, InterpretationContext context) {
                LocalVariableExpression lve = new LocalVariableExpression(name, context);
		lve.variable = getVariable(name, context);
	       lve.typeName = lve.variable.getCSFullType();
               return lve;
        }
        
 

	public DNVariable getVariable() {
		return this.variable;
	}

	public String getTypeName() {
		return typeName;
	}

	public DNType getDNType() {
            Debug.logn("Get type on " + this, this);
		if (vbClass == null) {
			vbClass = this.context.getLibrary().getDNType(this.getTypeName());
		}
		return vbClass;
	}

	public String tryAsJava() {
		return this.variable.getName();
	}

	public String toString() {
		return "LocalVariableEx: " + variable;
	}

}
 