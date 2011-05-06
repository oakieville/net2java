
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
import com.sun.dn.java.JavaPrimitives;
import com.sun.dn.library.LibraryData;

	/**
        * A .NET expression denoting access to a DNType.
        * 
        * @author danny.coward@sun.com
        */

public class ClassExpression extends SimpleExpression {
	private DNType globalClass;

	public static boolean isClassExpression(String s, InterpretationContext context) {
		Library library = context.getLibrary();
		Debug.clogn("Is " + s + " a class expression ?", ClassExpression.class);
		long l = (new java.util.Date()).getTime();
		if (library == null) {
			library = context.getLibrary();
		}
		long ll = (new java.util.Date()).getTime();
		Debug.clogn("" + (ll-l) + " ms to get Library", ClassExpression.class);

		DNType c = library.getLibraryData().getLibraryClass(s);
		ll = (new java.util.Date()).getTime();
		Debug.clogn("" + (ll-l) + " ms so far", ClassExpression.class);
		if (c != null) {
			ll = (new java.util.Date()).getTime();
			Debug.clogn("yes - " + c + " " + (ll-l) + " ms", ClassExpression.class);
			return true;
		}
		if ( library.containsProgramDefinedDNType(s) ) {
			ll = (new java.util.Date()).getTime();
			Debug.clogn("yes - " + (ll-l) + " ms", ClassExpression.class);
			return true;
		}
		
		ll = (new java.util.Date()).getTime();
		Debug.clogn("no - " + (ll-l) + " ms", ClassExpression.class);
		return false;
	}

	public ClassExpression(String code, InterpretationContext context) {
            super(code, context);
            Library library = context.getLibrary();
            String s = code.trim();
            DNType c = library.getLibraryData().getLibraryClass(s);
            if (c != null) {
                this.globalClass = c;
            } else {
                this.globalClass = library.getProgramDefinedDNType(s);
                if (this.globalClass instanceof UnknownType) {
                    throw new TypeResolveException(s, "Unknown .NET Type " + s);
                }
            }
            if (this.globalClass == null) {
		throw new RuntimeException("Error");
            }
	}

	public String getTypeName() {
		return globalClass.getName();
	}

	public DNType getDNType() {
		return this.globalClass;
	}

	public String tryAsJava() {
          
            String jType = "";
            if (this.globalClass.isEnum()) {
                jType = this.globalClass.getName();
            } else {
                jType = this.context.getLibrary().getJavaTypeFor(this.globalClass.getName());
            }
          
            if (JavaPrimitives.isPrimitive(jType)) {
                jType = JavaPrimitives.getJavaClassTypeForPrimitive(jType);
            }
            Debug.logn(jType, this);
            return jType;
	}

	public String toString() {
		return "ClssExp: " + globalClass;
	}
}
 