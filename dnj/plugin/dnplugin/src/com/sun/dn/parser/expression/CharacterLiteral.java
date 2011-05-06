
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

	
	/** A .NET expression for a single character literal. 
         * Defined, for example, in VB as <br>
	CharacterLiteral ::= " StringCharacter " C <br>

	'c'
	@author danny.coward@sun.com
	**/

public class CharacterLiteral extends Literal {
	private String s;
        
        public static CharacterLiteral createVBCharacterLiteral(String s, InterpretationContext context) {
            CharacterLiteral cl = new CharacterLiteral(s, context);
            String ss = s.trim();
            String literal = ss.substring(ss.indexOf("\"") + 1, ss.lastIndexOf("\""));
            //System.out.println(literal); 
            if (literal.length() > 1) {
            	throw new RuntimeException("can't make a char out of " + literal );
            }
            cl.s = literal; 
            return cl;
        }
        
        public DNType getDNType() {
            return context.getLibrary().getLibraryData().getLibraryClass(this.getTypeName());
        }
        
        public String getTypeName() {
            return "System.Char";
        }

	private CharacterLiteral(String code, InterpretationContext context) {
            super(code, context);
	}

	public String tryAsJava() {
		return "'" + s.charAt(0) + "'";
	}


}

 