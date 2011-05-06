
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

import java.util.*;
import com.sun.dn.java.*;
import com.sun.dn.parser.*;
import com.sun.dn.parser.statement.*;
import com.sun.dn.*;
import com.sun.dn.util.*;
import com.sun.dn.Library;

    /** An .NET expression that creates an instance of a Delegate.
     *@author danny.coward@sun.com
     */

public class DelegateCreationExpression extends NewExpression {
	private String classname;
	private Expression target;
	private String methodName;

	public DelegateCreationExpression(String dnCode, String classname, Expression target, String methodName, InterpretationContext context) {
		super(dnCode, context);
		this.classname = classname;
		this.target = target;
		this.methodName = methodName;
	}

	public Signature resolveMethodName() {
		MetaClass myMetaClass = super.context.getMetaClass();
		List potentialMatches = myMetaClass.findMemberStatementsByName(this.methodName);
		if (potentialMatches.size() != 1) {
			throw new RuntimeException("Found " + potentialMatches.size() + " matches for " + methodName + " on " + myMetaClass + ". I'm only programmed to deal with one !!");
		} else {
			MemberStatement ms = (MemberStatement) potentialMatches.get(0);
			myMetaClass.addMemberStatementToMakePublic(ms);
			return ms.getSignature();
		}
	}


	public DNType getDNType() {
		DNType c = this.getLibrary().getProgramDefinedOrLibraryDNTypeFor(classname);
		return c;
	}

	public String getTypeName() {
		return this.getDNType().getName();
	}

	private Library getLibrary() {
            return context.getLibrary();
	}

	public String tryAsJava() {
            String jClassname = this.getLibrary().getJavaTypeFor(classname);
            return JavaKeywords.J_NEW + " " + jClassname + "( " + DelegateHelper.getCreationArgString(target.asJava(), methodName) + " )";
        }

	public String toString() {
		return "DelegateCreation " + classname;
	}

}
 