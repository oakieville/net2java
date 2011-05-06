
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
import com.sun.dn.parser.*;
import com.sun.dn.*;
import com.sun.dn.util.*;
import com.sun.dn.Library;

	/** Abstract type representing any .NET control structure.
	@author danny.coward@sun.com
	*/

public abstract class ControlFlowStatement extends StatementAdapter implements InterpretationContext {
	private Library library;

	ControlFlowStatement(String code, InterpretationContext context) {
		super(code, context);
	}
	
	public DNVariable getVariable(String name) {
		for (Iterator itr = this.getVariables().iterator(); itr.hasNext();) {
			DNVariable var = (DNVariable ) itr.next();
			if (var.getName().equals(name)) {
				return var;
			}
		}
		return null;
	}

	public Expression getImplicitExpression() {
		if (this.getParent() instanceof ControlFlowStatement) {
			ControlFlowStatement parent = (ControlFlowStatement) this.getParent();
			return parent.getImplicitExpression();
		}
		return null;
	}

	public DNVariable getImplicitVariable() {
		if (this.getParent() instanceof ControlFlowStatement) {
			ControlFlowStatement parent = (ControlFlowStatement) this.getParent();
			return parent.getImplicitVariable();
		}
		return null;
	}

	public InterpretationContext getParent() {
		return this.context;
	}

	public Library getLibrary() {
		if (this.library == null) {
			this.library = this.context.getLibrary();
		}
		//Debug.logn("accessing library on " + this, this);
		return library;
	}

	public MetaClass getMetaClass() {
		return this.context.getMetaClass();
	}


}

 