
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
	/**
	
	**/

import com.sun.dn.parser.*;
import com.sun.dn.util.*;

	/**
 * A Literal is a .NET expression for a static value of some kind. 
 * Defined as <br>
 * Literal ::=  <br>
 * BooleanLiteral |   <br>
 * NumericLiteral |  <br>
 * StringLiteral |  <br>
 * CharacterLiteral |  <br>
 * DateLiteral |  <br>
 * VBNothing <br>
 * NumericLiteral ::= IntegerLiteral | FloatingPointLiteral  <br>
 * 
 * @author danny.coward@sun.com
 */
	
public abstract class Literal extends SimpleExpression {

    protected Literal(String code, InterpretationContext context) {
        super(code, context);
    }

	public static Literal getCSLiteral(String s,  InterpretationContext context) {
		String ss = s.trim();
		if (StringLiteral.isStringLiteral(ss)) {
			return new StringLiteral(ss.substring(1, ss.length()-1), context); 
		} else if (CSNull.isNull(s)) {
			return new CSNull(s, context);
		} else if (IntegerLiteral.isCSIntegerLiteral(s, context)) {
			return IntegerLiteral.createCSIntegerLiteral(s, context);
		} else if (isFloat(ss)) {
			return new FloatingPointLiteral (ss, context);
                }
		return null;
	}

	public static Literal getVBLiteral(String s, InterpretationContext context) {
		String ss = s.trim();

		if (ss.equals(VBKeywords.VB_True) || ss.equals(VBKeywords.VB_False)) {
			return BooleanLiteral.createVBBooleanLiteral(ss, context);
		} else if (StringLiteral.isStringLiteral(ss)) {
			return new StringLiteral(ss.substring(1, ss.length()-1), context); 
		} else if (ss.startsWith("#")) {
			return DateLiteral.createVBDateLiteral(ss.substring(1, ss.length()-1), context); 
		} else if (ss.equals(VBKeywords.VB_Nothing)) {
			return new VBNothing(ss, context);
		} else if (isInt(ss)) {
			return IntegerLiteral.createVBIntegerLiteral(ss, context);
		} else if (isFloat(ss)) {
			return new FloatingPointLiteral (ss, context);
		}
		return null;
	}

	public static Literal getVBLiteral(String type, String s, InterpretationContext context) {
                if (TypeConversionExpression.isVBTypeConversionExpression(s, context)) {
                    return null;
                }
                
		if ("Char".equals(type)) {
			return CharacterLiteral.createVBCharacterLiteral(s, context);
		} else if ( "Single".equals(type) ) {
			return new FloatingPointLiteral(s, context);
		} else { // the method above seems to be reliable
			return getVBLiteral(s, context);
		}
	}

	private static boolean isInt(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

	private static boolean isFloat(String s) {
		try {
			Float.parseFloat(s);

			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}

	}

}

 