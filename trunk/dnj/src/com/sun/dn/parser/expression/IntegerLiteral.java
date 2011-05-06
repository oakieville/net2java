
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


	/** A .NET expression of an integer value. Defined as <br>
	IntegerLiteral ::= IntegralLiteralValue [ IntegralTypeCharacter ] <br>
	IntegralLiteralValue ::= IntLiteral | HexLiteral | OctalLiteral  <br>
	IntegralTypeCharacter ::= <br>
	ShortCharacter | <br>
	IntegerCharacter | <br>
	LongCharacter | <br>
	IntegerTypeCharacter | <br>
	LongTypeCharacter <br>

	ShortCharacter ::= S <br>
	IntegerCharacter ::= I <br>
	LongCharacter ::= L <br>
	IntLiteral ::= Digit+ <br>
	HexLiteral ::= & H HexDigit+ <br>
	OctalLiteral ::= & O OctalDigit+ <br>
	Digit ::= 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 <br>
	HexDigit ::= 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | A | B | C | D | E | F <br>
	OctalDigit ::= 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7  <br>
	@author danny.coward@sun.com
	**/

public class IntegerLiteral extends Literal implements SignedNumeric {
	private int i;
	private DNType c;

	public static IntegerLiteral createVBIntegerLiteral(String s,  InterpretationContext context) {
		IntegerLiteral il = new IntegerLiteral(s, context);
		il.i = Integer.parseInt(s);
		return il;
	}
        
        public boolean isPositive() {
            return i >= 0;
        }

	public static IntegerLiteral createCSIntegerLiteral(String s,  InterpretationContext context) {
		IntegerLiteral il = new IntegerLiteral(s, context);
		il.i = Integer.parseInt(s);
		return il;
	}

	private IntegerLiteral(String code, InterpretationContext context) {
            super(code, context);
	}

	public static boolean isCSIntegerLiteral(String s, InterpretationContext context) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

	public String tryAsJava() {
		return "" + i;
	}

	public String getTypeName() {
		return "Integer";
	}

	public DNType getDNType() {
            if (this.c == null) {
		this.c = this.context.getLibrary().getLibraryData().getLibraryClass(this.getTypeName());
            }
            return c;
	}

	public String toString() {
		return "IntLit: " + i;
	}
}

 