
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

import com.sun.dn.util.*;
import com.sun.dn.parser.*;

	/** A .NET expression denoting a Float value. Defined as <br>
	**  <br>
	FloatingPointLiteral ::=  <br>
	FloatingPointLiteralValue [ FloatingPointTypeCharacter ] | <br>
	IntLiteral FloatingPointTypeCharacter <br>
	FloatingPointTypeCharacter ::= <br>
	SingleCharacter | <br>
	DoubleCharacter | <br>
	DecimalCharacter | <br>
	SingleTypeCharacter | <br>
	DoubleTypeCharacter | <br>
	DecimalTypeCharacter <br>
	SingleCharacter ::= F <br>
	DoubleCharacter ::= R <br>
	DecimalCharacter ::= D  <br>
	FloatingPointLiteralValue ::= <br>
	IntLiteral . IntLiteral [ Exponent ] | <br>
	. IntLiteral [ Exponent ] | <br>
	IntLiteral Exponent <br>
	Exponent ::= E [ Sign ] IntLiteral <br>
	Sign ::= + | - <br>
	@author danny.coward@sun.com
	**/

	// we will deal with 123.345 types for now

public class FloatingPointLiteral extends Literal implements SignedNumeric {
	private float f;

	public FloatingPointLiteral(String s, InterpretationContext context) {
            super(s, context);
            f = Float.parseFloat(s);
           
	}
        
        public boolean isPositive() {
            return f >= 0;
        }

	public String getTypeName() {
		return "System.Float";
	}
        
        public DNType getDNType() {
            return context.getLibrary().getLibraryData().getLibraryClass(this.getTypeName());
        }

	public String tryAsJava() {
		return  "" + f;
	}


}

 