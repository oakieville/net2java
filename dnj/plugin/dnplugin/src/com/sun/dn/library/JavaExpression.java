
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
package com.sun.dn.library;

import com.sun.dn.parser.Signature;
import java.util.*;
import com.sun.dn.*;
import com.sun.dn.util.*;

	/** An expression in Java.
	@author danny.coward@sun.com
	*/

public class JavaExpression {
	private String ex;
	private List importStrings = new ArrayList();
	boolean isIndirect = false; // used to tell what kind of substitution to make for InvocationExpression
	private Signature sig;

	public static String ARG = "arg";
	public static String THIS = "this";
	public static String VALUE = "value";

	public JavaExpression(String ex, List importStrings, Signature sig) {
		this.ex = ex.trim();
		this.importStrings = importStrings;
		this.sig = sig;
	}
        
        public String getExpression() {
            return this.ex;
        }
        
        public Signature getSignature() {
            return this.sig;
        }

	public boolean isIndirect() {
		return isIndirect;
	}

	public void setIndirect(boolean isIndirect) {
		this.isIndirect = isIndirect;
	}

	public List getImportStrings() {
		return importStrings;
	}

	public String toString() {
            return "JavaExp: " + ex;
	}

}
 