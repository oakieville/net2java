
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
import com.sun.dn.parser.*;
import com.sun.dn.util.*;
import com.sun.dn.Library;

	/** Class representing a member of a .NET MetaCass.
	** @author danny.coward@sun.com
	*/


public abstract class MemberStatement extends StatementAdapter implements InterpretationContextWithStatements, StatementWithStatements {
	List statements = new ArrayList();
	Signature sig;
	boolean isInterfaceMemberOrAbstract = false;
	private Library library = null;
        


	protected MemberStatement(String code, InterpretationContext context) {
		super(code, context);
	}
        
        public List getComments() {
            List l = new ArrayList();
            for (Iterator itr = this.constructedPreStatements.iterator(); itr.hasNext();) {
                Object next = itr.next();
                if (next instanceof Comment) {
                    l.add(next);
                 
                }
            
            }
            return l;
        }

	public abstract void parse(List stmts);

	public String getName() {
            return sig.getName();
	}
        
        public String getJavaName() {
            return this.getName();
        }

	public boolean isInterfaceMember() {
		return this.isInterfaceMemberOrAbstract;
	}
        
        public boolean isAbstract() {
            return sig.isAbstract();
        }
        
        public boolean isExternal() {
            return sig.isExternal();
        }

	public boolean isConstructor() {
		return false;
	}

	public String getReturnType() {
		return sig.getReturnType();
	}

	public List getModifiers() {
		return sig.getModifiers();
	}
        
        public void makePublic() {
            if (this instanceof CSMethodStatement) {
                this.sig.makeCSPublic();
            } else if (this instanceof VBSubroutineStatement ||
                    this instanceof VBFunctionStatement) {
                this.sig.makeVBPublic();
            } else {
                throw new RuntimeException("Weird!");
            }
        }

	public Signature getSignature() {
		return this.sig;
	}

	public List getStatements() {
		return this.statements;
	}

	public boolean isMain() {
		return sig.isMain();
	}

	public List getArgs() {
		return sig.getArgs();
	}
		// *ought* to care about order, but I suppose
		// that the VB is already valid - i.e. no
		// overlapping variable names
	public List getVariables() {
		List variables = new ArrayList();
		variables.addAll(this.sig.getArgs());
		for (Iterator itr = statements.iterator(); itr.hasNext();) {
                    Object o = itr.next();
                    if (o instanceof LocalVariableDeclaration) {
			variables.addAll(((LocalVariableDeclaration)o).getVariables());
                    }
		}
		variables.addAll(context.getVariables());
               
		return variables;
	}

	public DNVariable getVariable(String name) {
            String realName = name.trim();
            
            for (Iterator itr = this.getVariables().iterator(); itr.hasNext();) {
                Object o = (Object) itr.next();
                
		DNVariable var = (DNVariable) o;
		if (var.getName().equals(realName )) {
                   
                    return var;
                }
            }
		
            return null;
	}

	public void addStatement(Statement s) {
		this.statements.add(s);
	}

	public List getJavaSupplements() {
		return new ArrayList(); // only used by subroutine statements
	}


	public Library getLibrary() {
		if (this.library == null) {
			library = this.context.getLibrary();
		}
		return library;
	}

	public MetaClass getMetaClass() {
		return this.context.getMetaClass();
	}

	public InterpretationContext getParent() {
		return this.context;
	}

}

 